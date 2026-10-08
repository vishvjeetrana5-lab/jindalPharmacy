package aarogya.pharmacy.medicineReceive.server;

import aarogya.pharmacy.medicineReceive.shared.MedicineReceive_Srv;
import aarogya.pharmacy.medicineReceive.shared.MedicineReceive_Vld;
import aarogya.pharmacy.medicineReceive.shared.MedicineReceive_Mdl;
import aarogya.pharmacy.medicineReceive.shared.Find_Mdl;
import aarogya.pharmacy.medicineReceive.shared.GrdMedicineDetail_Mdl;
import aarogya.pharmacy.medicineReceive.shared.DefaultValue_Mdl;
import aarogya.pharmacy.medicineReceive.shared.TxtHReceivedFrom_Mdl;
import aarogya.pharmacy.self.server.Pharmacy_Server;
import aarogya.pharmacy.self.shared.BarCode_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.itemBatch.server.ItemBatch_Server;
import aarogya.selfToolkit.itemBatch.shared.CompareProperties_Mdl;
import aarogya.selfToolkit.itemBatch.shared.ItemBatch_Mdl;
import dataman.dmToolkit.stock.shared.StockFifo_Mdl;
import dataman.dmToolkit.stock.shared.Stock_Mdl;
import aarogya.selfToolkit.modifyReason.server.ModifyReason_Server;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import aarogya.selfToolkit.stStatic.shared.STEnum.ItemBatchType;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmToolkit.stock.server.Stock_Server;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MedicineReceive_SrvImpl extends DmRSSPrivate implements MedicineReceive_Srv {

    private String vCat = "'" + new ProjectVoucherCategory().MEDICINE_RECEIVE + "'";
    private final String voucherTypefieldName = "sh.V_Type", voucherDateFieldName = "sh.V_Date";

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission) {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);
        String sqlQuery, sqlCondition = "";
        DmPreparedStatement psTemp = null;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " select sh.docId "
                    + "         from stockHead as sh "
                    + "         left join voucher_Type as vt on sh.v_Type = vt.v_Type  "
                    + "         where vt.category = " + vCat + " and sh.site_Code = :site_Code and sh.v_prefix = :vPrefix";

            sqlCondition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypefieldName, userPermission);
            if (!sqlCondition.equalsIgnoreCase("")) {
                sqlQuery += "and " + sqlCondition;
            }

            sqlQuery += "    order by sh.v_Date desc, vt.short_Name, sh.v_No desc, sh.recId  ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp.setString("vPrefix", DmServer.getSsnVoucherPrefix(this));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                lstNavigator.add(lstNavigatorTmp);
            }
            rsTemp.close();
            psTemp.close();

            dmrTemp.setStatus(true);
            cnTemp.close();
        } catch (Exception e) {
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

    @Override
    public DmResult<MedicineReceive_Mdl> getMoveRec(String strKey) {
        DmResult<MedicineReceive_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        MedicineReceive_Mdl mrTemp = new MedicineReceive_Mdl();
        dmrTemp.setData(mrTemp);
        List<GrdMedicineDetail_Mdl> lstNode = new ArrayList<GrdMedicineDetail_Mdl>();
        GrdMedicineDetail_Mdl node;
        String itemCsv = "";

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*==============================<< Get header detail >>===============================*/
            sqlQuery = "select	sh.docId, sh.recId, sh.v_No, sh.recIdPrefix, sh.v_Date, sh.u_Name, sh.preparedBy, sh.v_Type, vt.description,  "
                    + "		st.departmentCode, sgd.name as departmentName, st.subCode as receivedByCode,sh.refNo,sh.refDate,sh.remark, "
                    + "		sgrb.name receivedByName "
                    + "		from stockHead as sh  "
                    + "		left join voucher_Type as vt on sh.v_Type = vt.v_Type  "
                    + "		left join ( "
                    + "                     select s.docId, max(s.departmentCode) as departmentCode"
                    + "                     , max(subCode) as subCode "
                    + "                     from stockHeadDetail s "
                    + "                     where s.docId = :docId "
                    + "                     group by s.docId "
                    + "		)   as st on sh.docId = st.docId "
                    + "		left join subGroup as sgd on st.departmentCode = sgd.subCode  "
                    + "		left join subGroup as sgrb on st.subCode = sgrb.subCode "
                    + "		where sh.docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("docId")).toString());
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.getRecIdItemReceive().setRecIdValue(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.getRecIdItemReceive().setCounter(DmShared.iNull(rsTemp.getString("v_No")).longValue());
                mrTemp.getRecIdItemReceive().setPrefix(DmShared.xNull(rsTemp.getString("recIdPrefix")));
                mrTemp.setVoucherDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                mrTemp.setVoucherTypeCode(DmShared.xNull(rsTemp.getString("v_Type")));
                mrTemp.setVoucherTypeName(DmShared.xNull(rsTemp.getString("description")));
                mrTemp.setDepartmentTag(DmShared.xNull(rsTemp.getString("departmentCode")));
                mrTemp.setDepartmentText(DmShared.xNull(rsTemp.getString("departmentName")));
                mrTemp.setReceivedByTag(DmShared.xNull(rsTemp.getString("receivedByCode")));
                mrTemp.setReceivedByText(DmShared.xNull(rsTemp.getString("receivedByName")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setRefDate(DmShared.dNull(rsTemp.getDate("refDate")));
                mrTemp.setRefNo(DmShared.xNull(rsTemp.getString("refNo")));
                mrTemp.setRemarkHeader(DmShared.xNull(rsTemp.getString("remark")));
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Get Item grid detail >>===============================*/
            sqlQuery = " Select	shd.itemCode, im.name as itemName, um.unit as skuName,im.sku as skuCode, shd.skuValue,shd.quantity , "
                    + " shd.remark, shd.ibCode, shd.batchNo, shd.expiryDate, shd.manufacturingDate, shd.mrp,shd.saleRate,shd.skuMrp,shd.skuSaleRate,  "
                    + " DATEDIFF(day, shd.manufacturingDate, shd.expiryDate) AS bestBeforeDays, "
                    + " um.decimalPlaces,im.isBestBefore, shd.v_SNo,ump.unit as purchaseUnitName  "
                    + ",shd.skuRate"
                    + ",shd.rate"
                    + ",shd.skuValue"
                    + ",shd.skuQtyForRate"
                    + ",shd.unitCode as purchaseUnitCode"
                    + ",shd.unitQuantity"
                    + ",ump.decimalPlaces as purchaseUnitDecimalPlaces, shd.barcode"
                    + " from stockHeadDetail  shd  "
                    + " left join itemMast as im on shd.itemCode = im.code  "
                    + " left join unitMast as um on im.sku = um.code  "
                    + " left join unitMast as ump on shd.unitCode = ump.code  "
                    + " left join itemBatch as ib on shd.ibCode = ib.code  "
                    + " where shd.docId = :docId "
                    + " order by shd.v_SNo ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdMedicineDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                node.setV_SNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                node.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                node.setSkuCode(DmShared.xNull(rsTemp.getString("skuCode")));
                node.setSkuDecimalPlaces(DmShared.iNull(rsTemp.getString("decimalPlaces")).toString());
                node.setPurchaseUnitDecimalPlaces(DmShared.iNull(rsTemp.getString("purchaseUnitDecimalPlaces")).toString());
                node.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                node.setQuantity(DmServer.vNull(rsTemp.getString("quantity")));

                //properties
                node.setIbCode(DmShared.xNull(rsTemp.getString("ibCode")));
                node.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                node.setBestBeforeDays(DmShared.xNull(rsTemp.getString("bestBeforeDays")));
                node.setManufacturingDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));
                node.setExpiryDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                node.setIsBestBefore(DmShared.xNull(rsTemp.getString("isBestBefore")));
                node.setMrp(DmServer.vNull(rsTemp.getString("mrp"), "0.00"));
                node.setSaleRate(DmServer.vNull(rsTemp.getString("saleRate"), "0.00"));
                node.setSkuMrp(DmServer.vNull(rsTemp.getString("skuMrp"), DmShared.getNumberFormat(7)));
                node.setSkuSaleRate(DmServer.vNull(rsTemp.getString("skuSaleRate"), DmShared.getNumberFormat(7)));
                node.setSkuQtyForRate(DmServer.vNull(rsTemp.getString("skuQtyForRate")));
                node.setPurchaseUnitCode(DmShared.xNull(rsTemp.getString("purchaseUnitCode")));
                node.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                node.setAmount(DmServer.vNull(rsTemp.getString("skuValue"), "0.00"));
                node.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                node.setSkuRate(DmServer.vNull(rsTemp.getString("skuRate")));
                node.setTotalpacks(DmServer.vNull(rsTemp.getString("unitQuantity")));
                node.setBarCode(DmShared.xNull(rsTemp.getString("barCode")));

                itemCsv = Pharmacy_Server.getItemCodeCsv(node.getBarCode(), mrTemp.getVoucherDate(), mrTemp.getDepartmentTag(), DmServer.getSsnSiteCode(this), ItemBatchType.NON_EXPIRED_BATCH, false, true, cnTemp);
                node.setItemCodeCsv(itemCsv);

                lstNode.add(node);
            }
            rsTemp.close();
            psTemp.close();

            mrTemp.getListItemDetail().addAll(lstNode);

            dmrTemp.setData(mrTemp);

            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
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

    @Override
    public DmResult<String> deleteRecord(String keyCode) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData(keyCode);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            DmServer.checkLockDate(DmServer.getSsnSiteCode(this), DmServer.getSsnVoucherPrefix(this), cnTemp);
            /*==============================<< Delete record from stock >>===============================*/
            Stock_Server.deleteStock(keyCode, cnTemp);

            /*==============================<< Delete record from stockHeadDetail >>===============================*/
            sqlQuery = " Delete From stockHeadDetail Where docId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Delete record from stockHead >>===============================*/
            sqlQuery = " Delete From stockHead Where docId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            psTemp.executeUpdate();
            psTemp.close();

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

    @Override
    public DmResult<String> saveRecord(MedicineReceive_Mdl mrTemp, MedicineReceive_Mdl prevMdl, String voucherPrefix) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery, userName, siteCode;
        ResultSet rsTemp = null;
        dmrTemp.setData("");

        try {
            MedicineReceive_Vld.chkSave(mrTemp);
            userName = DmServer.getSsnUserName(this);
            siteCode = DmServer.getSsnSiteCode(this);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            DmServer.checkLockDate(DmServer.getSsnSiteCode(this), DmServer.getSsnVoucherPrefix(this), cnTemp);
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                /*==============================<< Generate docId for new record >>====================================*/
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this));
                mrTemp.setRecIdItemReceive(DmServer.getRecId("stockHead", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecIdItemReceive(), mrTemp.getVoucherDate(), DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this, true));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(mrTemp.getRecIdItemReceive().getRecIdValue()));
            } else {
                DmServer.getRecId("stockHead", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecIdItemReceive(), mrTemp.getVoucherDate(), DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this, true);
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            /*==============================<< Check items for restricted department >>====================================*/
            STServer.checkItemsRestrictedDepartment(mrTemp.getLineItemCodesCsv(), mrTemp.getDepartmentTag(), mrTemp.getDepartmentText(), cnTemp);

            //<editor-fold defaultstate="collapsed" desc="CHK For Incorrect Batch">
            /*==============================<<  >>====================================*/
            if (!DmShared.xNull(mrTemp.getInCorrectSelectedBatchSql()).trim().equalsIgnoreCase("")) {
                sqlQuery = " SELECT STUFF((SELECT char(10)+','+ vib.itemName + '-' + vib.batchNo AS [text()]"
                        + " FROM (" + mrTemp.getInCorrectSelectedBatchSql() + ") vIb "
                        + " LEFT JOIN itemBatch ib  ON vIb.ibCode = ib.code  AND vIb.itemCode = ib.itemCode "
                        + " WHERE vIb.ibCode is Not Null and IB.itemCode IS NULL"
                        + " FOR XML PATH(''), TYPE).value('.[1]','nvarchar(max)'), 1,2,'') "
                        + " as incorrectMedicineBatches ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (!DmShared.xNull(rsTemp.getString("incorrectMedicineBatches")).trim().equalsIgnoreCase("")) {
                        throw new DmException(0, "Selected medicines with batches are invalid : " + DmShared.xNull(rsTemp.getString("incorrectMedicineBatches")).trim());
                    }
                }
                rsTemp.close();
                psTemp.close();
            }
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="CHK For Incorrect Barcode">
            /*==============================<<  >>====================================*/
            if (!DmShared.xNull(mrTemp.getInCorrectSelectedBarcodeSql()).trim().equalsIgnoreCase("")) {
                sqlQuery = " SELECT STUFF((SELECT char(10)+','+ vib.itemName + '-' + vib.batchNo + '-' + vib.barcode AS [text()]"
                        + " FROM (" + mrTemp.getInCorrectSelectedBarcodeSql() + ") vIb "
                        + " LEFT JOIN itemBatch ib  ON vIb.ibCode = ib.code AND vIb.itemCode = ib.itemCode AND ISNULL(vIb.barcode,'') = ISNULL(ib.barcode,'') "
                        + " WHERE vIb.ibCode is Not Null and IB.itemCode IS NULL"
                        + " FOR XML PATH(''), TYPE).value('.[1]','nvarchar(max)'), 1,2,'') "
                        + " as incorrectMedicineBarcodes ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (!DmShared.xNull(rsTemp.getString("incorrectMedicineBarcodes")).trim().equalsIgnoreCase("")) {
                        throw new DmException(0, "Selected medicines with barcodes are invalid : " + DmShared.xNull(rsTemp.getString("incorrectMedicineBarcodes")).trim());
                    }
                }
                rsTemp.close();
                psTemp.close();
            }
            //</editor-fold>

            /*==============================<< Insert / Update record into stockHead >>====================================*/
            if (blnAdd) {
                sqlQuery = "Insert Into stockHead (docId, v_Type, v_No, v_Date, v_Prefix, recId,refNo,refDate,remark, "
                        + " site_Code, transferDt, preparedBy, u_EntDt,recIdPrefix) "
                        + " values (:docId, :v_Type, :v_No, :v_Date,  :v_Prefix, :recId,:refNo,:refDate,:remark, "
                        + " :site_Code, :transferDt, :preparedBy, :uEntDt,:recIdPrefix) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("recId", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("v_Type", DmShared.foriegnKey(mrTemp.getVoucherTypeCode()));
                psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setString("v_Prefix", voucherPrefix);
                psTemp.setString("v_No", DmShared.iNull(mrTemp.getRecIdItemReceive().getCounter()).toString());
                psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getRecIdItemReceive().getPrefix()));
                psTemp.setString("site_Code", DmShared.foriegnKey(siteCode));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("transferDt", null);
                psTemp.setString("preparedBy", userName);

                psTemp.setString("refNo", DmShared.xNull(mrTemp.getRefNo()));
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemarkHeader()));
                psTemp.setDate("refDate", DmServer.convertDate(mrTemp.getRefDate()));
            } else {
                sqlQuery = " update stockHead Set "
                        + " v_Date = :v_Date, "
                        + " transferDt = :transferDt, "
                        + " u_Name = :u_Name, "
                        + " refNo = :refNo, "
                        + " refDate = :refDate, "
                        + " remark = :remark, "
                        + " u_EntDt = :uEntDt "
                        + " Where docId = :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setString("transferDt", null);
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("u_Name", userName);
                psTemp.setString("refNo", DmShared.xNull(mrTemp.getRefNo()));
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemarkHeader()));
                psTemp.setDate("refDate", DmServer.convertDate(mrTemp.getRefDate()));
            }
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Validate Removed Received Items >>====================================*/
            if (!blnAdd) {
                ItemBatch_Server.validateRemovedReceivedItems(iniItemBatchProperties(mrTemp, prevMdl), DmEnum.StockEntryType.OTHER, cnTemp, this);
            }

            /*==============================<< Delete records from stock >>====================================*/
            Stock_Server.deleteStock(DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString(), cnTemp, true);

            /*==============================<< Delete records from stockHeadDetail >>====================================*/
            sqlQuery = "Delete From stockHeadDetail Where docId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Insert records into stockHeadDetail & stockHeadBatchDetail >>====================================*/
            int v_SNo = 0;
            List<GrdMedicineDetail_Mdl> listDetail = mrTemp.getListItemDetail();
            for (GrdMedicineDetail_Mdl detail_Mdl : listDetail) {
                if (!DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                    detail_Mdl.setV_SNo(String.valueOf(++v_SNo));
                    sqlQuery = " insert into stockHeadDetail(docId, v_SNo, departmentCode,"
                            + " itemCode, quantity, remark,subCode,site_Code ,"
                            + " ibCode, batchNo, manufacturingDate,expiryDate,  mrp,saleRate,skuMrp,skuSaleRate"
                            + ",skuRate"
                            + ",rate"
                            + ",skuValue"
                            + ",skuQtyForRate"
                            + ",unitCode"
                            + ",unitQuantity"
                            + ", barCode "
                            + ")"
                            + " values (:docId, :v_SNo, :departmentCode,"
                            + " :itemCode, :quantity, :remark,:subCode,:site_Code ,"
                            + " :ibCode,:batchNo, :manufacturingDate,:expiryDate, :mrp,:saleRate,:skuMrp,:skuSaleRate"
                            + ",:skuRate"
                            + ",:rate"
                            + ",:skuValue"
                            + ",:skuQtyForRate"
                            + ",:unitCode"
                            + ",:unitQuantity"
                            + ",:barCode"
                            + ") ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                    psTemp.setString("v_SNo", DmShared.iNull(detail_Mdl.getV_SNo()).toString());
                    psTemp.setString("departmentCode", DmShared.foriegnKey(mrTemp.getDepartmentTag()));

                    psTemp.setString("itemCode", DmShared.foriegnKey(detail_Mdl.getSharedKeyFieldCode()));
                    psTemp.setString("quantity", DmServer.vNull(detail_Mdl.getQuantity()));
                    psTemp.setString("remark", DmShared.xNull(detail_Mdl.getRemark()));
                    psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getReceivedByTag()));
                    psTemp.setString("site_Code", DmShared.foriegnKey(siteCode));

                    psTemp.setString("rate", DmServer.vNull(detail_Mdl.getRate(), "0.00"));
                    psTemp.setString("skuRate", DmServer.vNull(detail_Mdl.getSkuRate()));
                    psTemp.setString("skuValue", DmServer.vNull(detail_Mdl.getAmount(), "0.00"));
                    psTemp.setString("skuQtyForRate", DmServer.vNull(detail_Mdl.getSkuQtyForRate()));
                    psTemp.setString("unitCode", DmShared.foriegnKey(detail_Mdl.getPurchaseUnitCode()));
                    psTemp.setString("unitQuantity", DmServer.vNull(detail_Mdl.getTotalpacks()));

                    psTemp.setString("ibCode", DmShared.foriegnKey(detail_Mdl.getIbCode()));
                    psTemp.setString("batchNo", DmShared.xNull(detail_Mdl.getBatchNo()));
                    psTemp.setDate("manufacturingDate", DmServer.convertDate(detail_Mdl.getManufacturingDate()));
                    psTemp.setDate("expiryDate", DmServer.convertDate(detail_Mdl.getExpiryDate()));
                    psTemp.setString("mrp", DmServer.vNull(detail_Mdl.getMrp()));
                    psTemp.setString("saleRate", DmServer.vNull(detail_Mdl.getSaleRate()));
                    psTemp.setString("skuMrp", DmServer.vNull(detail_Mdl.getSkuMrp()));
                    psTemp.setString("skuSaleRate", DmServer.vNull(detail_Mdl.getSkuSaleRate()));
                    psTemp.setString("barcode", DmShared.xNull(detail_Mdl.getBarCode()));
                    psTemp.executeUpdate();
                    psTemp.close();

                    /*----------------------------------------------------------Stock Saving--------------------------------------------------------------*/
                    Stock_Mdl stock_Mdl = new Stock_Mdl();
                    stock_Mdl.setSharedKeyFieldCode(DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                    stock_Mdl.setSharedKeyFieldName(detail_Mdl.getV_SNo());
                    stock_Mdl.setSite_Code(DmShared.xNull(siteCode));
                    stock_Mdl.setV_Type(DmShared.xNull(mrTemp.getVoucherTypeCode()));
                    stock_Mdl.setV_No(DmShared.iNull(mrTemp.getRecIdItemReceive().getCounter()).toString());
                    stock_Mdl.setRecId(DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                    stock_Mdl.setRecIdPrefix(DmShared.xNull(mrTemp.getRecIdItemReceive().getPrefix()));
                    stock_Mdl.setV_Date(DmShared.dNull(mrTemp.getVoucherDate()));
                    stock_Mdl.setRefChallanNo(DmShared.xNull(mrTemp.getRefNo()));
                    stock_Mdl.setRefChallanDate(DmShared.dNull(mrTemp.getRefDate()));
                    stock_Mdl.setDepartmentCode(DmShared.xNull(mrTemp.getDepartmentTag()));
                    stock_Mdl.setStockContraSubCode(DmShared.xNull(mrTemp.getReceivedByTag()));
                    stock_Mdl.setRemark(DmShared.xNull(mrTemp.getRemarkHeader()) + "\n" + DmShared.xNull(detail_Mdl.getRemark()));
                    stock_Mdl.setItemCode(DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()));
                    stock_Mdl.setIbCode(detail_Mdl.getIbCode());
                    stock_Mdl.setRecQty(DmServer.vNull(detail_Mdl.getQuantity()));
                    stock_Mdl.setSkuValue(DmServer.vNull(detail_Mdl.getAmount(), "0.00"));
                    stock_Mdl.setLandedSkuValue(DmServer.vNull(detail_Mdl.getAmount(), "0.00")); //Landed Value == Receive Value

                    StockFifo_Mdl stockFifo_Mdl = new StockFifo_Mdl();
                    stockFifo_Mdl.setSharedKeyFieldCode(DmShared.xNull(stock_Mdl.getSharedKeyFieldCode()));
                    stockFifo_Mdl.setSharedKeyFieldName(DmShared.xNull(stock_Mdl.getSharedKeyFieldName()));
                    stockFifo_Mdl.setRecQty(DmServer.vNull(detail_Mdl.getQuantity()));
                    stockFifo_Mdl.setStockValueDate(DmShared.dNull(stock_Mdl.getV_Date()));

                    stock_Mdl.setStockFifo_Mdl(stockFifo_Mdl);

                    Stock_Server.saveStock(stock_Mdl, DmEnum.StockEntryType.ITEM_RECEIVE, cnTemp);

                }
            }

            if (mrTemp.getModifyReasonMdl() != null && !blnAdd) {
                ModifyReason_Server.save(mrTemp.getModifyReasonMdl(), mrTemp.getSharedKeyFieldCode(), DmServer.getSsnUserName(this), cnTemp);
            }

            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
            spBegin.commit();
            dmrTemp.setStatus(true);
            cnTemp.close();

            /*====================================< Send mail & SMS >====================================*/
            if (blnAdd) {
                STServer.sendMail(mrTemp.getSharedKeyFieldCode(), mrTemp.getVoucherTypeCode(), this);
            }
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

    @Override
    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery, sqlCondition = "";

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " select	sh.docId, sh.recId, sh.v_Date, sgd.name as departmentName, sgrb.name receivedByName, vt.short_Name,sh.v_No "
                    + "		from stockHead as sh  "
                    + "		left join voucher_Type as vt on sh.v_Type = vt.v_Type  "
                    + "		left join ( "
                    + "                     select docId, max(departmentCode) as departmentCode, max(subCOde) as subCOde "
                    + "                     from stockHeadDetail  "
                    + "                     group by docId "
                    + "		) as st on sh.docId = st.docId "
                    + "		left join subGroup as sgd on st.departmentCode = sgd.subCode  "
                    + "		left join subGroup as sgrb on st.subCode = sgrb.subCode "
                    + "		where vt.category = " + vCat + " and sh.site_Code = :site_Code and sh.v_prefix = :vPrefix";

            sqlCondition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypefieldName, userPermission);
            if (!sqlCondition.equalsIgnoreCase("")) {
                sqlQuery += "and " + sqlCondition;
            }
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("vPrefix", DmServer.getSsnVoucherPrefix(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("docId")).toString());
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                listNode.setVoucherType(DmShared.xNull(rsTemp.getString("short_Name")));
                listNode.setVoucherDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setReceivedBy(DmShared.xNull(rsTemp.getString("receivedByName")));
                listNode.setDepartment(DmShared.xNull(rsTemp.getString("departmentName")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHVoucher(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {

            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL(" select v_Type as code, description as name from voucher_Type where category = " + vCat + " and isActive = 1 ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
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

    @Override
    public DmResult<DefaultValue_Mdl> getDefaultValues() {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult();
        DefaultValue_Mdl defaultValue = new DefaultValue_Mdl();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*==============================<< Get default voucher+_Type >>===============================*/
            sqlQuery = " select v_Type as code, description as name from voucher_Type where category = " + vCat + " and isActive = 1 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setVoucherTypeCode(DmShared.xNull(rsTemp.getString("code")));
                defaultValue.setVoucherTypeName(DmShared.xNull(rsTemp.getString("name")));
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "select  dm.subCode as code, sg.displayName as name  "
                    + "		from departmentMast as dm "
                    + "		left join subGroup as sg on dm.subCode = sg.subCode "
                    + "  where sg.IsActive = 1 and dm.subCode=:depCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("depCode", DmShared.xNull(STServer.getSsnDepartmentCode(this)));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setDepartmentTag(DmShared.xNull(rsTemp.getString("code")));
                defaultValue.setDepartmentText(DmShared.xNull(rsTemp.getString("name")));

            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "SELECT  "
                    + " env.pharmacyBarcodeActive  "
                    + " FROM enviro as env "
                    + " Where env.site_Code = :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmShared.xNull(DmServer.getSsnSiteCode(this)));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setPharmacyBarcodeActive(DmShared.iNull(rsTemp.getString("pharmacyBarcodeActive")).toString());
            }
            psTemp.close();
            rsTemp.close();

            defaultValue.setCurrentDate(DmServer.getServerDateTime()[0]);

            defaultValue.setSiteCode(DmServer.getSsnSiteCode(this));

            dmrTemp.setData(defaultValue);
            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
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

    @Override
    public DmResultPageing<List<TxtHReceivedFrom_Mdl>> getTxtHReceivedFrom(String v_Type, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHReceivedFrom_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHReceivedFrom_Mdl> lstTemp = new ArrayList<>();
        TxtHReceivedFrom_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT"
                    + "   sg.subCode AS code"
                    + " , sg.name"
                    + " , sg.manualCode"
                    + " , c.cityName"
                    + " FROM subGroup sg"
                    + " LEFT JOIN subGroupSiteList sgsl ON sgsl.subCode = sg.subCode"
                    + " LEFT JOIN city c ON c.cityCode = sg.cityCode"
                    + "	LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature"
                    + " WHERE sgn.code IN (SELECT acf.Nature FROM AcFilteration acf WHERE acf.v_Type = :v_Type)"
                    + " AND sg.isActive = 1"
                    + " AND sgsl.site_Code = :site_Code";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("v_Type", DmShared.iNull(v_Type).toString());
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHReceivedFrom_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                lstTemp.add(listNode);
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String condition = "";

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                condition = " and dm.subCode in (" + DmServer.getParamList(":department", STServer.getSsnDepartmentCodeList(this)) + ")";
            }

            sqlQuery = "select	dm.subCode as code, sg.name  "
                    + "		from departmentMast as dm "
                    + "		left join subGroup as sg on dm.subCode = sg.subCode "
                    + "         left join subGroupNature sgn on sgn.code = sg.nature"
                    + "         where sgn.manualCode IN ('CM','PH') and sg.isActive = 1 " + condition;
            sqlWithParam.setSQL(sqlQuery);
            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("department", STServer.getSsnDepartmentCodeList(this));
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
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

    private CompareProperties_Mdl iniItemBatchProperties(MedicineReceive_Mdl mrTempCurrent, MedicineReceive_Mdl mrTempPrevious) {
        CompareProperties_Mdl compareProperties_Mdl = new CompareProperties_Mdl();
        List<ItemBatch_Mdl> lstItemBatch_MdlCurnt = new ArrayList<>();
        List<ItemBatch_Mdl> lstItemBatch_MdlPrev = new ArrayList<>();

        lstItemBatch_MdlCurnt.clear();
        for (GrdMedicineDetail_Mdl detail_MdlCurrent : mrTempCurrent.getListItemDetail()) {
            ItemBatch_Mdl batch_Mdl = new ItemBatch_Mdl();
            if (DmShared.iNull(detail_MdlCurrent.getSharedKeyFieldCode()).longValue() != 0) {
                batch_Mdl.getIpm().setItemCode(DmShared.xNull(detail_MdlCurrent.getSharedKeyFieldCode()));
                batch_Mdl.setCode(DmShared.xNull(detail_MdlCurrent.getIbCode()));
                batch_Mdl.setReceiveQty(DmServer.vNull(DmShared.vNull(detail_MdlCurrent.getQuantity())));
                batch_Mdl.setReceiveDate(DmShared.dNull(mrTempCurrent.getVoucherDate()));
                batch_Mdl.setStockDepartmentCode(DmShared.xNull(mrTempCurrent.getDepartmentTag()));
                lstItemBatch_MdlCurnt.add(batch_Mdl);
            }
        }

        lstItemBatch_MdlPrev.clear();
        for (GrdMedicineDetail_Mdl detail_MdlPrev : mrTempPrevious.getListItemDetail()) {
            ItemBatch_Mdl batch_MdlPrev = new ItemBatch_Mdl();
            if (DmShared.iNull(detail_MdlPrev.getSharedKeyFieldCode()).longValue() != 0) {
                batch_MdlPrev.getIpm().setItemCode(DmShared.xNull(detail_MdlPrev.getSharedKeyFieldCode()));
                batch_MdlPrev.setCode(DmShared.xNull(detail_MdlPrev.getIbCode()));
                batch_MdlPrev.setReceiveQty(DmServer.vNull(DmShared.vNull(detail_MdlPrev.getQuantity())));
                batch_MdlPrev.setReceiveDate(DmShared.dNull(mrTempPrevious.getVoucherDate()));
                batch_MdlPrev.setStockDepartmentCode(DmShared.xNull(mrTempCurrent.getDepartmentTag()));
                lstItemBatch_MdlPrev.add(batch_MdlPrev);
            }
        }

        compareProperties_Mdl.setDocId(DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
        compareProperties_Mdl.setRecId(DmShared.xNull(mrTempCurrent.getSharedKeyFieldName()));
        compareProperties_Mdl.setRecIdPrefix(DmShared.xNull(mrTempCurrent.getRecIdItemReceive().getPrefix()));
        compareProperties_Mdl.setV_No(DmShared.xNull(mrTempCurrent.getRecIdItemReceive().getCounter().toString()));
        compareProperties_Mdl.setV_Type(DmShared.xNull(mrTempCurrent.getVoucherTypeCode()));
//        compareProperties_Mdl.setDepartmentCode(DmShared.xNull(mrTempCurrent.getDepartmentTag()));
        compareProperties_Mdl.setCurrentReceiveDate(DmShared.dNull(mrTempCurrent.getVoucherDate()));
        compareProperties_Mdl.setSite_Code(DmServer.getSsnSiteCode(this));
        compareProperties_Mdl.getCurrentIbm().addAll(lstItemBatch_MdlCurnt);
        compareProperties_Mdl.getPreviousIbm().addAll(lstItemBatch_MdlPrev);
        compareProperties_Mdl.setPreviousReceiveDate(DmShared.dNull(mrTempPrevious.getVoucherDate()));

        return compareProperties_Mdl;
    }

    @Override
    public DmResult<String> getSkuLandedRate(String itemCode, String ibCode, String departmentCode, String v_Date) {
        DmResult<String> dmrtemp = new DmResult<String>();
        String skuLandedRate = "";
        Connection cnTemp = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            skuLandedRate = DmServer.vNull(STServer.getSkuLandedRate(itemCode, ibCode, departmentCode, DmServer.getSsnSiteCode(this), v_Date, cnTemp), "0.00");
            dmrtemp.setData(skuLandedRate);
            dmrtemp.setStatus(true);
            cnTemp.close();
        } catch (Exception ex) {
            dmrtemp.setErrorMessage(ex.getMessage());
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrtemp;
    }

    @Override
    public DmResult<BarCode_Mdl> getBarCodeSearch(String barCode, String voucherDate, String departmentCode, String siteCode) {
        DmResult<BarCode_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            dmrTemp.setData(Pharmacy_Server.barCodeSearch(barCode, voucherDate, departmentCode, siteCode, ItemBatchType.NON_EXPIRED_BATCH, false, cnTemp, null));
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {

                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }
}
