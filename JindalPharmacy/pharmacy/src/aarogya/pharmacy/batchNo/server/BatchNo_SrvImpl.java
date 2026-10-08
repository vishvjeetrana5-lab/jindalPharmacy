package aarogya.pharmacy.batchNo.server;

import aarogya.pharmacy.batchNo.shared.BatchNoSearchValue_Mdl;
import aarogya.pharmacy.batchNo.shared.BatchNo_Mdl;
import aarogya.pharmacy.batchNo.shared.BatchNo_Srv;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.STEnum.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BatchNo_SrvImpl extends DmRSSPrivate implements BatchNo_Srv {

    public BatchNo_SrvImpl() {
    }

    @Override
    public DmResultPageing<List<BatchNo_Mdl>> getBatchNoDetail(BatchNoSearchValue_Mdl searchValue_Mdl, MedicineSaleOn sRateType, String profitPercentage, boolean isAllowDiscountToParty, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<BatchNo_Mdl>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, join = "", balanceQty = "";
        List<BatchNo_Mdl> lstTemp = new ArrayList<BatchNo_Mdl>();
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);
        BatchNo_Mdl node;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(searchValue_Mdl.getSelectedBatchSQL()).equalsIgnoreCase("")) {
                join = " LEFT JOIN "
                        + "     (SELECT "
                        + "     vib.ibCode"
                        + "     , ISNULL(SUM(vib.qty),0) as qty "
                        + "     FROM (" + searchValue_Mdl.getSelectedBatchSQL() + ") as vib "
                        + "     GROUP BY vib.ibCode"
                        + "     ) as vib ON vib.ibCode = ib.code ";
                balanceQty = ",  max( (ISNULL(ibs.balanceQty,0) - ISNULL(vib.qty,0)) ) AS balanceQty ";
            } else {
                balanceQty = ", max(ibs.balanceQty) as balanceQty";
            }

            sqlQuery = " SELECT   max(  ( (  pbd.oh_Amt_Taxable    )/pbd.recQty )/im.purchaseUnitConversionRate )   as grossPurchaseRate  "
                    + " ,  max( ib.code ) as code "
                    + " , max( ib.batchNo ) as batchNo " + balanceQty
                    + " , max( ib.expiryDate)  as expiryDate "
                    + " , max( ib.manufacturingDate) as manufacturingDate "
                    + " , max( ib.mrp)  as mrp "
                    + " , max( ib.saleRate) as saleRate"
                    + " , max( ib.staffSaleRate) as staffSaleRate"
                    + " , max( ib.skuSaleRate)  as skuSaleRate"
                    + " , max( ib.skuStaffSaleRate)  as skuStaffSaleRate"
                    + " , max( ib.purchaseRateExFree) as purchaseRateExFree"
                    + " , max( ib.purchaseTaxableRateExFree) as purchaseTaxableRateExFree"
                    + " , max( ib.recId) as recId"
                    + " , max( ib.skuMrp) as skuMrp "
                    + " , max( um.unit ) as skuName "
                    + " , max( DATEDIFF(day, ib.manufacturingDate, ib.expiryDate) ) AS bestBeforeDays "
                    + " , max( im.sku) as skuCode "
                    + " , max( im.purchaseUnitConversionRate)  as purchaseUnitConversionRate "
                    + " , max( idi.reOrder) as reOrder "
                    + " , max( im.saleUnitConversionRate) as saleUnitConversionRate  "
                    + " , max( idi.binName) as binName "
                    + " , max( idi.minStock) as minStock "
                    + " , max( um.decimalPlaces) as decimalPlaces "
                    + " , max( ump.decimalPlaces ) as pUnitDecimalPlaces "
                    + " , max( ums.decimalPlaces ) as sUnitDecimalPlaces  "
                    + " , max( ib.barcode ) as barcode "
                    + " FROM itemBatch ib "
                    + " LEFT JOIN itemBatchStock ibs ON ibs.ibCode = ib.code "
                    + " LEFT JOIN itemMast im ON im.code = ib.itemCode "
                    + " LEFT JOIN unitMast um ON um.code = im.sku "
                    + " LEFT JOIN unitMast ump ON ump.code = im.purchaseUnit "
                    + " LEFT JOIN unitMast ums ON ums.code = im.saleUnit "
                    + " left join pBillDetail as pbd on pbd.itemCode=ib.itemCode and pbd.ibCode=ib.code and pbd.skuSaleRate=ib.skuSaleRate  \n"
                    + " and pbd.skuMrp=ib.skuMrp and pbd.expiryDate=ib.expiryDate   "
                    + join
                    + " LEFT JOIN itemDepartmentInfo idi ON idi.itemCode = ib.itemCode AND idi.departmentCode = ISNULL(ibs.departmentCode,:departmentCode) AND idi.site_Code = ISNULL(ibs.site_Code,:site_Code) "
                    + " WHERE ib.itemCode = :itemCode "
                    + " AND ISNULL(ibs.departmentCode,:departmentCode) = :departmentCode "
                    + " AND ISNULL(ibs.site_Code,:site_Code) = :site_Code ";

            if (searchValue_Mdl.getBatchType().getId() == ItemBatchType.EXPIRED_BATCH.getId()) {
                sqlQuery += " AND ib.expiryDate <= :v_Date ";
            } else if (searchValue_Mdl.getBatchType().getId() == ItemBatchType.NON_EXPIRED_BATCH.getId()) {
                sqlQuery += " AND ib.expiryDate > :v_Date ";
            }
            if (!searchValue_Mdl.isIsReceive()) {
                if (searchValue_Mdl.getIsAllwNegStk()) {
                    sqlQuery += " AND CASE WHEN ISNULL(im.allowNegativeStock,0) = 1 THEN 1 ELSE CASE WHEN ISNULL(ibs.balanceQty,0) > 0 THEN 1 ELSE 0 END END = 1 ";
                } else {
                    sqlQuery += " AND ISNULL(ibs.balanceQty,0) > 0  ";
                }
            }
            if (!DmShared.xNull(searchValue_Mdl.getBarCode()).equalsIgnoreCase("")) {
                sqlQuery += " AND ISNULL(ib.barcode,'') = :barcode";
            }

            sqlQuery += "   group by ib.code,ib.skuSaleRate ,ib.skuMrp ,ib.expiryDate ";

            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("departmentCode", DmShared.iNull(searchValue_Mdl.getDepartmentCode()).toString());
            sqlWithParam.setString("itemCode", DmShared.iNull(searchValue_Mdl.getItemCode()).toString());
            if (searchValue_Mdl.getBatchType().getId() == ItemBatchType.EXPIRED_BATCH.getId()) {
                sqlWithParam.setDate("v_Date", DmServer.convertDate(searchValue_Mdl.getV_Date()));
            } else if (searchValue_Mdl.getBatchType().getId() == ItemBatchType.NON_EXPIRED_BATCH.getId()) {
                sqlWithParam.setDate("v_Date", DmServer.convertDate(searchValue_Mdl.getV_Date()));
            }
            if (!DmShared.xNull(searchValue_Mdl.getBarCode()).equalsIgnoreCase("")) {
                sqlWithParam.setString("barcode", DmShared.xNull(searchValue_Mdl.getBarCode()));
            }

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new BatchNo_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("batchNo")));
                node.setIbCode(DmShared.xNull(rsTemp.getString("code")));
                node.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                node.setBinName(DmShared.xNull(rsTemp.getString("binName")));
                node.setBalanceQty(DmServer.vNull(rsTemp.getString("balanceQty")));
                node.setExpiryDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                node.setManufacturingDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));
                node.setMrp(DmServer.vNull(rsTemp.getString("mrp"), "0.00"));

                if (sRateType == MedicineSaleOn.WHOLESALE_RATE) {
                    String wholeSaleRate = DmServer.vNull(DmShared.vNull(rsTemp.getString("purchaseRateExFree")).doubleValue() + (DmShared.vNull(rsTemp.getString("purchaseRateExFree")).doubleValue() * DmShared.vNull(profitPercentage).doubleValue() * 0.01));
                    node.setSaleRate(DmServer.vNull(wholeSaleRate, "0.00"));
                    node.setSkuSaleRate("0");
                    node.setWholeSaleRate(DmServer.vNull(wholeSaleRate));
                    if (isAllowDiscountToParty) {
                        String discountPerUnit = DmServer.vNull(DmShared.vNull(rsTemp.getString("purchaseRateExFree")).doubleValue() - DmShared.vNull(rsTemp.getString("purchaseTaxableRateExFree")).doubleValue());
                        node.setDiscountPerUnit(DmServer.vNull(discountPerUnit));
                    }
                } else {
                    node.setSaleRate(DmServer.vNull(rsTemp.getString("saleRate"), "0.00"));
                    node.setSkuSaleRate(DmServer.vNull(rsTemp.getString("skuSaleRate")));
                }
                node.setReceiveRecId(DmShared.xNull(rsTemp.getString("recId")));
                node.setSkuCode(DmShared.xNull(rsTemp.getString("skuCode")));
                node.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                node.setMinStock(DmServer.vNull(rsTemp.getString("minStock")));
                node.setReOrderLevel(DmServer.vNull(rsTemp.getString("reOrder")));

                node.setSkuMrp(DmServer.vNull(rsTemp.getString("skuMrp")));
                node.setDecimalPlaces(DmServer.vNull(rsTemp.getString("decimalPlaces")));
                node.setBestBeforeDays(DmServer.vNull(rsTemp.getString("bestBeforeDays")));
                node.setGrossPurchaseRate(DmShared.vNull(rsTemp.getString("grossPurchaseRate")).toString());

                node.setSkuStaffRate(DmServer.vNull(STServer.getSkuLandedRate(DmShared.iNull(searchValue_Mdl.getItemCode()).toString(), rsTemp.getString("code"), DmShared.iNull(searchValue_Mdl.getDepartmentCode()).toString(), DmServer.getSsnSiteCode(this), searchValue_Mdl.getV_Date(), cnTemp), "0.00"));

                if (DmShared.vNull(node.getSkuStaffRate()) == 0) {
                    node.setSkuStaffRate(DmServer.vNull(STServer.getSkuStaffRate(DmShared.iNull(searchValue_Mdl.getItemCode()).toString(), rsTemp.getString("code"), DmShared.iNull(searchValue_Mdl.getDepartmentCode()).toString(), DmServer.getSsnSiteCode(this), searchValue_Mdl.getV_Date(), cnTemp), "0.00"));

                }

                node.setSkuGrossPurchseRate(DmServer.vNull(STServer.getSkuPurchaseRate(DmShared.iNull(searchValue_Mdl.getItemCode()).toString(), rsTemp.getString("code"), DmShared.iNull(searchValue_Mdl.getDepartmentCode()).toString(), DmServer.getSsnSiteCode(this), searchValue_Mdl.getV_Date(), cnTemp), "0.00"));

                if (sRateType == MedicineSaleOn.COST_PRICE) {

                    node.setSkuLandedRate(DmServer.vNull(rsTemp.getString("skuStaffSaleRate")));

                    if (DmShared.vNull(node.getSkuLandedRate()) == 0) {
                        node.setSkuLandedRate(DmServer.vNull(STServer.getSkuLandedRate(DmShared.iNull(searchValue_Mdl.getItemCode()).toString(), rsTemp.getString("code"), DmShared.iNull(searchValue_Mdl.getDepartmentCode()).toString(), DmServer.getSsnSiteCode(this), searchValue_Mdl.getV_Date(), cnTemp), "0.00"));

                    }

                } else {
                    node.setSkuLandedRate("0");
                }

                if (DmServer.convertDate(DmShared.dNull(rsTemp.getDate("expiryDate"))).getTime() <= DmServer.convertDate(searchValue_Mdl.getV_Date()).getTime()) {
                    node.setExpiredIbCode(true);
                } else {
                    node.setExpiredIbCode(false);
                }
                double pQty = DmShared.vNull(rsTemp.getString("balanceQty")) / DmShared.vNull(rsTemp.getString("purchaseUnitConversionRate"));
                double sQty = DmShared.vNull(rsTemp.getString("balanceQty")) / DmShared.vNull(rsTemp.getString("saleUnitConversionRate"));
                node.setBalanceQtyInPurcUnit(DmServer.vNull(pQty, DmShared.getNumberFormat(DmShared.iNull(rsTemp.getString("pUnitDecimalPlaces")).intValue())));
                node.setBalanceQtyInSaleUnit(DmServer.vNull(sQty, DmShared.getNumberFormat(DmShared.iNull(rsTemp.getString("sUnitDecimalPlaces")).intValue())));
                node.setBarCode(DmShared.xNull(rsTemp.getString("barcode")));
                lstTemp.add(node);
            }

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rsTemp.close();
            psTemp.close();

            cnTemp.close();
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return rstTemp;
    }

    private String getItemName(String itemCode, Connection cnTemp) {
        String rtnValue = "";
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {
            sqlQuery = "SELECT im.name, um.unit"
                    + " FROM itemMast im"
                    + " INNER JOIN unitMast um ON um.code = im.sku"
                    + " WHERE im.code=:itemCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("itemCode", DmShared.iNull(itemCode).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                rtnValue = DmShared.xNull(rsTemp.getString("name")) + " " + "(" + DmShared.xNull(rsTemp.getString("unit")) + ")";
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rtnValue;
    }

    @Override
    public DmResult<String> getDefaultValue(String itemCode) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery, wtaCode = "";
        ResultSet rsTemp = null;

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            dmrTemp.setData(getItemName(itemCode, cnTemp));

            spBegin.commit();
            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

}
