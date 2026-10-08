package aarogya.pharmacy.medicineOpening.server;

import aarogya.pharmacy.medicineOpening.shared.*;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.itemBatch.server.ItemBatch_Server;
import static dataman.dmToolkit.stock.server.Stock_Server.*;
import aarogya.selfToolkit.itemBatch.shared.CompareProperties_Mdl;
import aarogya.selfToolkit.itemBatch.shared.ItemBatch_Mdl;
import aarogya.selfToolkit.stStatic.server.STServer;
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
import java.util.Calendar;
import java.util.List;

public class MedicineOpening_SrvImpl extends DmRSSPrivate implements MedicineOpening_Srv {

    private String vCat = new ProjectVoucherCategory().MEDICINE_OPENING;
    private final String voucherTypefieldName = "sh.V_Type", voucherDateFieldName = "sh.V_Date";

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission) {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);
        String sqlQuery;
        DmPreparedStatement psTemp = null;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " select sh.docId "
                    + "         from stockHead as sh "
                    + "         left join voucher_Type as vt on sh.v_Type = vt.v_Type  "
                    + "         where vt.category = '" + vCat + "' and sh.site_Code = :site_Code and sh.v_prefix = :vPrefix "
                    + "         order by sh.v_Date desc, vt.short_Name, sh.v_No desc, sh.recId  ";
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
    public DmResult<MedicineOpening_Mdl> getMoveRec(String strKey) {
        DmResult<MedicineOpening_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        MedicineOpening_Mdl mrTemp = new MedicineOpening_Mdl();
        dmrTemp.setData(mrTemp);
        List<GrdMedicineDetail_Mdl> lstNode = new ArrayList<GrdMedicineDetail_Mdl>();
        GrdMedicineDetail_Mdl node;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*==============================<< Get header detail >>===============================*/
            sqlQuery = "select	sh.docId, sh.recId, sh.v_No, sh.recIdPrefix, sh.v_Date, sh.u_Name, sh.preparedBy, sh.v_Type, vt.description,  "
                    + "		st.departmentCode, sgd.name as departmentName, st.subCode as receivedByCode, sgrb.name receivedByName, "
                    + "		sk.stockV_Date "
                    + "		from stockHead as sh  "
                    + "		left join voucher_Type as vt on sh.v_Type = vt.v_Type  "
                    + "		left join ( "
                    + "                     select s.docId, max(s.departmentCode) as departmentCode, "
                    + "                     max(s.subCode) as subCode "
                    + "                     from stockHeadDetail s "
                    + "                     where s.docId = :docId "
                    + "                     group by s.docId "
                    + "		)   as st on sh.docId = st.docId "
                    + "		left join ( "
                    + "                     select s.docId, "
                    + "                     max(s.v_Date) as stockV_Date "
                    + "                     from stock s "
                    + "                     where s.docId = :docId "
                    + "                     group by s.docId "
                    + "		)   as sk on sh.docId = sk.docId "
                    + "		left join subGroup as sgd on st.departmentCode = sgd.subCode  "
                    + "		left join subGroup as sgrb on st.subCode = sgrb.subCode "
                    + "		where sh.docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("docId")).toString());
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.getRecIdItemOpening().setRecIdValue(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.getRecIdItemOpening().setCounter(DmShared.iNull(rsTemp.getString("v_No")).longValue());
                mrTemp.getRecIdItemOpening().setPrefix(DmShared.xNull(rsTemp.getString("recIdPrefix")));
                mrTemp.setVoucherDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                mrTemp.setStockVoucherDate(DmShared.dNull(rsTemp.getDate("stockV_Date")));
                mrTemp.setVoucherTypeCode(DmShared.xNull(rsTemp.getString("v_Type")));
                mrTemp.setVoucherTypeName(DmShared.xNull(rsTemp.getString("description")));
                mrTemp.setDepartmentTag(DmShared.xNull(rsTemp.getString("departmentCode")));
                mrTemp.setDepartmentText(DmShared.xNull(rsTemp.getString("departmentName")));
                mrTemp.setReceivedByTag(DmShared.xNull(rsTemp.getString("receivedByCode")));
                mrTemp.setReceivedByText(DmShared.xNull(rsTemp.getString("receivedByName")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Get Medicine grid detail >>===============================*/
            sqlQuery = " Select	shd.itemCode as medicineCode, im.name as medicineName,im.sku as skuCode, um.unit as skuName, shd.refNo as refChallanNo, shd.refDate as refChallanDate,  "
                    + " shd.skuValue,shd.quantity , shd.rate,shd.skuRate,shd.skuValue , shd.remark, shd.ibCode, shd.batchNo, shd.expiryDate, "
                    + " shd.manufacturingDate, shd.mrp, im.propertyList ,   DATEDIFF(day, shd.manufacturingDate, shd.expiryDate) AS bestBeforeDays,  "
                    + " shd.colourCode, shd.sizeCode, shd.skuQtyForRate ,  icm.name AS colourName, ism.name AS sizeName,  um.decimalPlaces,shd.saleRate,shd.skuMrp,shd.skuSaleRate, "
                    + " im.isBestBefore, shd.v_SNo,shd.unitCode as purchaseUnitCode,ump.unit as purchaseUnitName, "
                    + " ump.decimalPlaces as purchaseUnitDecimalPlaces, shd.unitQuantity, "
                    + " im.isBarcodeActive, shd.barcode "
                    + " from stockHeadDetail  shd  "
                    + " left join itemMast as im on shd.itemCode = im.code  "
                    + " left join unitMast as um on im.sku = um.code   "
                    + " left join unitMast as ump on shd.unitCode = ump.code   "
                    + " LEFT JOIN itemColourMast icm ON shd.colourCode = icm.code "
                    + " LEFT JOIN itemSizeMast ism ON shd.sizeCode = ism.code "
                    + " where shd.docId = :docId "
                    + " order by shd.v_SNo ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdMedicineDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("medicineCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("medicineName")));
                node.setV_SNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                node.setTotalQuantity(DmServer.vNull(rsTemp.getString("quantity")));
                node.setSkuDecimalPlaces(DmShared.iNull(rsTemp.getString("decimalPlaces")).toString());
                node.setRefDate(DmShared.dNull(rsTemp.getDate("refChallanDate")));
                node.setRefNo(DmShared.xNull(rsTemp.getString("refChallanNo")));
                node.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                node.setSkuCode(DmShared.xNull(rsTemp.getString("skuCode")));
                node.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                node.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                node.setSkuRate(DmServer.vNull(rsTemp.getString("skuRate")));
                node.setSkuValue(DmServer.vNull(rsTemp.getString("skuValue"), "0.00"));
                node.setIbCode(DmShared.xNull(rsTemp.getString("ibCode")));
                node.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                node.setIsBarcodeActive(DmShared.xNull(rsTemp.getString("isBarcodeActive")));
                node.setBarcode(DmShared.xNull(rsTemp.getString("barcode")));
                node.setBestBeforeDays(DmShared.iNull(rsTemp.getString("bestBeforeDays")).toString());
                node.setManufacturingDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));
                node.setExpiryDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                node.setMrp(DmServer.vNull(rsTemp.getString("mrp"), "0.00"));
                node.setSkuQtyForRate(DmServer.vNull(rsTemp.getString("skuQtyForRate")));
                node.setSaleRate(DmServer.vNull(rsTemp.getString("saleRate"), "0.00"));
                node.setIsBestBefore(DmShared.xNull(rsTemp.getString("isBestBefore")));
                node.setPurchaseUnitCode(DmShared.xNull(rsTemp.getString("purchaseUnitCode")));
                node.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                node.setPurchaseUnitDecimalPlaces(DmShared.iNull(rsTemp.getString("purchaseUnitDecimalPlaces")).toString());
                node.setPurchaseUnitQuantity(DmServer.vNull(rsTemp.getString("unitQuantity")));
                node.setSkuMrp(DmServer.vNull(rsTemp.getString("skuMrp"), DmShared.getNumberFormat(7)));
                node.setSkuSaleRate(DmServer.vNull(rsTemp.getString("skuSaleRate"), DmShared.getNumberFormat(7)));

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
    public DmResult<String> deleteRecord(String keyCode, MedicineOpening_Mdl mrTempPrevious) {
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
            /*==============================<< Manage Batch Detail >>====================================*/
            Stock_Server.deleteStock(keyCode, cnTemp);

            /*==============================<< Delete record from stockHeadDetail >>===============================*/
            String ibCodeCsv = STServer.getCsvFieldValue("stockHeadDetail", "ibCode", " AND docid = '" + DmShared.iNull(keyCode).toString() + "'", cnTemp);
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

            //Delete Unused Item Batches
            if (!DmShared.xNull(ibCodeCsv).trim().equalsIgnoreCase("")) {
                deleteUnusedItemBatches(ibCodeCsv, "", cnTemp);
            }

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
    public DmResult<String> saveRecord(MedicineOpening_Mdl mrTempCurrent, MedicineOpening_Mdl mrTempPrevious, String voucherPrefix) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery, userName, siteCode;
        ResultSet rsTemp = null;
        dmrTemp.setData("");

        try {
            MedicineOpening_Vld.chkSave(mrTempCurrent);
            userName = DmServer.getSsnUserName(this);
            siteCode = DmServer.getSsnSiteCode(this);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            DmServer.checkLockDate(DmServer.getSsnSiteCode(this), DmServer.getSsnVoucherPrefix(this), cnTemp);
            if (DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                /*==============================<< Generate docId for new record >>====================================*/
                blnAdd = true;
                mrTempCurrent.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(mrTempCurrent.getVoucherTypeCode()), voucherPrefix, cnTemp, this));
                mrTempCurrent.setRecIdItemOpening(DmServer.getRecId("stockHead", "docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()), "recId", mrTempCurrent.getRecIdItemOpening(), mrTempCurrent.getVoucherDate(), DmShared.xNull(mrTempCurrent.getVoucherTypeCode()), voucherPrefix, cnTemp, this, true));
                mrTempCurrent.setSharedKeyFieldName(DmShared.xNull(mrTempCurrent.getRecIdItemOpening().getRecIdValue()));
            } else {
                DmServer.getRecId("stockHead", "docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()), "recId", mrTempCurrent.getRecIdItemOpening(), mrTempCurrent.getVoucherDate(), DmShared.xNull(mrTempCurrent.getVoucherTypeCode()), voucherPrefix, cnTemp, this, true);
            }

            if (mrTempCurrent.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DmShared.convertDate(mrTempCurrent.getVoucherDate()));
            calendar.add(Calendar.DATE, -1);
            mrTempCurrent.setStockVoucherDate(DmShared.dNull(calendar.getTime()));


            /*==============================<< Insert / Update record into stockHead >>====================================*/
            if (blnAdd) {
                sqlQuery = "Insert Into stockHead (docId, v_Type, v_No, v_Date, v_Prefix, recId, "
                        + " site_Code, transferDt, preparedBy, u_EntDt,recIdPrefix) "
                        + " values (:docId, :v_Type, :v_No, :v_Date,  :v_Prefix, :recId, "
                        + " :site_Code, :transferDt, :preparedBy, :uEntDt,:recIdPrefix) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTempCurrent.getSharedKeyFieldCode()).toString());
                psTemp.setString("recId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldName()));
                psTemp.setString("v_Type", DmShared.foriegnKey(mrTempCurrent.getVoucherTypeCode()));
                psTemp.setDate("v_Date", DmServer.convertDate(mrTempCurrent.getVoucherDate()));
                psTemp.setString("v_Prefix", voucherPrefix);
                psTemp.setString("v_No", DmShared.iNull(mrTempCurrent.getRecIdItemOpening().getCounter()).toString());
                psTemp.setString("site_Code", siteCode);
                psTemp.setString("transferDt", null);
                psTemp.setString("preparedBy", userName);
                psTemp.setString("recIdPrefix", DmShared.xNull(mrTempCurrent.getRecIdItemOpening().getPrefix()));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            } else {
                sqlQuery = " update stockHead Set "
                        + " transferDt = :transferDt, "
                        + " u_Name = :u_Name, "
                        + " u_EntDt = :uEntDt "
                        + " Where docId = :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTempCurrent.getSharedKeyFieldCode()).toString());
                psTemp.setString("transferDt", null);
                psTemp.setString("u_Name", userName);
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            }
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Delete records from stockHeadDetail >>====================================*/
            String ibCodeCsv = STServer.getCsvFieldValue("stockHeadDetail", "ibCode", " AND docid = '" + DmShared.iNull(mrTempCurrent.getSharedKeyFieldCode()).toString() + "'", cnTemp);
            sqlQuery = "Delete From stockHeadDetail Where docId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTempCurrent.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Insert records into stockHeadDetail & stockHeadBatchDetail >>====================================*/
            int v_SNo = 0;
            List<GrdMedicineDetail_Mdl> listDetail = mrTempCurrent.getListItemDetail();
            for (GrdMedicineDetail_Mdl detail_Mdl : listDetail) {
                if (!DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                    detail_Mdl.setV_SNo(String.valueOf(++v_SNo));
                    sqlQuery = " insert into stockHeadDetail(docId, v_SNo, departmentCode, refNo,refDate,"
                            + " itemCode, quantity, remark,subCode,site_Code,rate,skuRate,skuValue,skuQtyForRate,"
                            + " batchNo, barcode, manufacturingDate,expiryDate, mrp,saleRate,skuMrp,skuSaleRate,unitCode,unitQuantity)"
                            + " values (:docId, :v_SNo, :departmentCode, :refNo, :refDate,"
                            + " :itemCode, :quantity, :remark,:subCode,:site_Code,:rate,:skuRate,:skuValue,:skuQtyForRate,"
                            + " :batchNo, :barcode, :manufacturingDate,:expiryDate, :mrp,:saleRate,:skuMrp,:skuSaleRate,:unitCode,:unitQuantity) ";

                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", DmShared.iNull(mrTempCurrent.getSharedKeyFieldCode()).toString());
                    psTemp.setString("v_SNo", DmShared.iNull(detail_Mdl.getV_SNo()).toString());
                    psTemp.setString("departmentCode", DmShared.foriegnKey(mrTempCurrent.getDepartmentTag()));
                    psTemp.setString("refNo", DmShared.xNull(detail_Mdl.getRefNo()));
                    psTemp.setDate("refDate", DmServer.convertDate(detail_Mdl.getRefDate()));
                    psTemp.setString("itemCode", DmShared.foriegnKey(detail_Mdl.getSharedKeyFieldCode()));
                    psTemp.setString("quantity", DmServer.vNull(detail_Mdl.getTotalQuantity()));
                    psTemp.setString("rate", DmServer.vNull(detail_Mdl.getRate()));
                    psTemp.setString("skuRate", DmServer.vNull(detail_Mdl.getSkuRate()));
                    psTemp.setString("skuValue", DmServer.vNull(detail_Mdl.getSkuValue()));
                    psTemp.setString("skuQtyForRate", DmServer.vNull(detail_Mdl.getSkuQtyForRate()));
                    psTemp.setString("remark", DmShared.xNull(detail_Mdl.getRemark()));
                    psTemp.setString("subCode", DmShared.foriegnKey(mrTempCurrent.getReceivedByTag()));
                    psTemp.setString("site_Code", DmShared.foriegnKey(siteCode));
                    psTemp.setString("batchNo", DmShared.xNull(detail_Mdl.getBatchNo()));
                    psTemp.setString("barcode", DmShared.xNull(detail_Mdl.getBarcode()));
                    psTemp.setDate("manufacturingDate", DmServer.convertDate(detail_Mdl.getManufacturingDate()));
                    psTemp.setDate("expiryDate", DmServer.convertDate(detail_Mdl.getExpiryDate()));
                    psTemp.setString("mrp", DmServer.vNull(detail_Mdl.getMrp()));
                    psTemp.setString("saleRate", DmServer.vNull(detail_Mdl.getSaleRate()));
                    psTemp.setString("skuMrp", DmServer.vNull(detail_Mdl.getSkuMrp()));
                    psTemp.setString("skuSaleRate", DmServer.vNull(detail_Mdl.getSkuSaleRate()));
                    psTemp.setString("unitCode", DmShared.foriegnKey(detail_Mdl.getPurchaseUnitCode()));
                    psTemp.setString("unitQuantity", DmServer.vNull(detail_Mdl.getPurchaseUnitQuantity()));

                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

            /*==============================<< Insert records into stock >>====================================*/
            ItemBatch_Server.compareProperties(iniItemBatchProperties(mrTempCurrent, mrTempPrevious, false), cnTemp, this);

            /*==============================<< Update ibCode From Stock Table >>====================================*/
            sqlQuery = "UPDATE stockHeadDetail "
                    + " SET stockHeadDetail.ibCode = stock.ibCode "
                    + " FROM stock "
                    + " WHERE stock.docid = stockHeadDetail.docId "
                    + " AND stock.v_SNo = stockHeadDetail.v_SNo "
                    + " AND stockHeadDetail.docid = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTempCurrent.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();
            /*==============================<< ************************* >>====================================*/

            if (mrTempCurrent.getModifyReasonMdl() != null && !blnAdd) {
//                ModifyReason_Server.save(mrTempCurrent.getModifyReasonMdl(), mrTempCurrent.getSharedKeyFieldCode(), DmServer.getSsnUserName(this), cnTemp);
            }

            //Delete Unused Item Batches
            if (!DmShared.xNull(ibCodeCsv).trim().equalsIgnoreCase("")) {
                deleteUnusedItemBatches(ibCodeCsv, "", cnTemp);
            }

            dmrTemp.setData(mrTempCurrent.getSharedKeyFieldCode());
            spBegin.commit();
            dmrTemp.setStatus(true);
            cnTemp.close();

            /*====================================< Send mail & SMS >====================================*/
            if (blnAdd) {
                STServer.sendMail(mrTempCurrent.getSharedKeyFieldCode(), mrTempCurrent.getVoucherTypeCode(), this);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
                    + "                     select docId, max(departmentCode) as departmentCode, max(subCode) as subCode "
                    + "                     from stockHeadDetail  "
                    + "                     group by docId "
                    + "		) as st on sh.docId = st.docId "
                    + "		left join subGroup as sgd on st.departmentCode = sgd.subCode  "
                    + "		left join subGroup as sgrb on st.subCode = sgrb.subCode "
                    + "		where vt.category = '" + vCat + "' and sh.site_Code = :site_Code and sh.v_prefix = :vPrefix ";
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
            sqlWithParam.setSQL(" select v_Type as code, description as name from voucher_Type where category = '" + vCat + "' and isActive = 1 ");
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
            defaultValue.getLstSaleRateRule().addAll(STServer.getSaleRateRule(cnTemp));

            /*==============================<< Get default voucher+_Type >>===============================*/
            sqlQuery = " select TOP 1 v_Type as code, description as name from voucher_Type where category = '" + vCat + "' and isActive = 1 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setVoucherTypeCode(DmShared.xNull(rsTemp.getString("code")));
                defaultValue.setVoucherTypeName(DmShared.xNull(rsTemp.getString("name")));
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Get default department >>===============================*/
            sqlQuery = "SELECT"
                    + "   dm.subCode AS code"
                    + " , sg.name"
                    + " FROM departmentMast dm"
                    + " LEFT JOIN subGroup sg ON dm.subCode = sg.subCode"
                    + " WHERE sg.IsActive = 1"
                    + " AND dm.subCode = :depCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("depCode", DmShared.xNull(STServer.getSsnDepartmentCode(this)));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setDepartmentTag(DmShared.xNull(rsTemp.getString("code")));
                defaultValue.setDepartmentText(DmShared.xNull(rsTemp.getString("name")));
            }
            rsTemp.close();
            psTemp.close();

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
    public DmResultPageing<List<TxtHMedicineName_Mdl>> getTxtHMedicineName(String departmentCode, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHMedicineName_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHMedicineName_Mdl> lstTemp = new ArrayList<TxtHMedicineName_Mdl>();
        TxtHMedicineName_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {

            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT  im.code, im.name, im.manualCode,"
                    + " im.purchaseUnit as purchaseUnitCode,ump.unit as purchaseUnitName,"
                    + " im.sku as skuCode, um.unit skuName,"
                    + " vis.currentStock, ump.decimalPlaces as purchaseUnitDecimalPlaces, "
                    + " im.propertyList, im.isBestBefore, im.isBarcodeActive, um.decimalPlaces, im.purchaseUnitConversionRate  "
                    + "		FROM itemMast im  "
                    + "         LEFT JOIN itemSiteList sl ON sl.code = im.code"
                    + "		LEFT JOIN unitmast um ON im.sku = um.code  "
                    + "		LEFT JOIN unitmast ump ON im.purchaseUnit = ump.code  "
                    + "		LEFT JOIN ( "
                    + "                     SELECT iStk.itemCode, SUM(iStk.balanceQty) AS currentStock  "
                    + "                     FROM itemStock iStk "
                    + "                     WHERE iStk.site_Code = :site_Code AND iStk.departmentCode = :departmentCode  "
                    + "                     GROUP BY iStk.itemCode "
                    + "		) AS vis ON im.code = vis.itemCode  "
                    + " 	LEFT JOIN ( "
                    + "                 SELECT irtd.code "
                    + "                 FROM itemRestrictedToDepartment irtd "
                    + "                 WHERE irtd.departmentCode = :departmentCode "
                    + "		) virtd ON virtd.code = im.code "
                    + "		WHERE im.isActive = 1 "
                    + "		AND (im.filterItemType = :filterItemType OR im.isCommon = 1)"
                    + "         AND virtd.code IS NULL "
                    + "         AND sl.site_Code = :site_Code";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("departmentCode", DmShared.iNull(departmentCode).toString());
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHMedicineName_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setSkuCode(DmShared.xNull(rsTemp.getString("skuCode")));
                listNode.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                listNode.setSkuDecimalPlaces(DmShared.iNull(rsTemp.getString("decimalPlaces")).toString());
                listNode.setPurchaseUnitCode(DmShared.xNull(rsTemp.getString("purchaseUnitCode")));
                listNode.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                listNode.setPurchaseUnitDecimalPlaces(DmShared.iNull(rsTemp.getString("purchaseUnitDecimalPlaces")).toString());
                listNode.setPurchaseUnitConversionRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));
                listNode.setStock(DmServer.vNull(rsTemp.getString("currentStock")));
                listNode.setIsBestBefore(DmShared.iNull(rsTemp.getString("isBestBefore")).toString());
                listNode.setIsBarcodeActive(DmShared.xNull(rsTemp.getString("isBarcodeActive")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rsTemp.close();
            psTemp.close();

            rstTemp.setStatus(true);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "";
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

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

    @Override
    public DmResultPageing<List<TxtHBatchNo_Mdl>> getTxtHBatchNo(String medicineCode, DmHelper helper, boolean blnToEndOfFile) {

        DmResultPageing<List<TxtHBatchNo_Mdl>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<TxtHBatchNo_Mdl> lstTemp = new ArrayList<>();
        TxtHBatchNo_Mdl listNode;
        String sqlQuery;
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT ib.code "
                    + " , ib.batchNo"
                    + " , ib.mrp"
                    + " , ib.saleRate"
                    + " , ib.expiryDate"
                    + " , ib.manufacturingDate"
                    + " , DATEDIFF(day, ib.manufacturingDate, ib.expiryDate) AS bestBeforeDays "
                    + " , ib.barcode"
                    + " FROM itemBatch ib"
                    + " WHERE ib.itemCode = :itemCode"
                    + " AND ISNULL(ib.batchNo, '') <> ''"
                    + " ";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("itemCode", DmShared.xNull(medicineCode));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHBatchNo_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("batchNo")));
                listNode.setMrp(DmServer.vNull(rsTemp.getString("mrp"), "0.00"));
                listNode.setSaleRate(DmServer.vNull(rsTemp.getString("saleRate"), "0.00"));
                listNode.setExpireDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                listNode.setMfgDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));
                listNode.setBestBeforeDays(DmShared.iNull(rsTemp.getString("bestBeforeDays")).toString());
                listNode.setBarcode(DmShared.xNull(rsTemp.getString("barcode")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
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

    private CompareProperties_Mdl iniItemBatchProperties(MedicineOpening_Mdl mrTempCurrent, MedicineOpening_Mdl mrTempPrevious, boolean isDelete) {
        CompareProperties_Mdl compareProperties_Mdl = new CompareProperties_Mdl();
        List<ItemBatch_Mdl> lstItemBatch_MdlCurnt = new ArrayList<>();
        List<ItemBatch_Mdl> lstItemBatch_MdlPrev = new ArrayList<>();
        if (!isDelete) {
            lstItemBatch_MdlCurnt.clear();
            for (GrdMedicineDetail_Mdl detail_MdlCurrent : mrTempCurrent.getListItemDetail()) {
                ItemBatch_Mdl batch_Mdl = new ItemBatch_Mdl();
                if (DmShared.iNull(detail_MdlCurrent.getSharedKeyFieldCode()).longValue() != 0) {
                    batch_Mdl.getIpm().setItemCode(DmShared.xNull(detail_MdlCurrent.getSharedKeyFieldCode()));
                    batch_Mdl.getIpm().setBatchNo(DmShared.xNull(detail_MdlCurrent.getBatchNo()));
                    batch_Mdl.setBarcode(DmShared.xNull(detail_MdlCurrent.getBarcode()));
                    batch_Mdl.getIpm().setExpiryDate(DmShared.dNull(detail_MdlCurrent.getExpiryDate()));
                    batch_Mdl.getIpm().setManufacturingDate(DmShared.dNull(detail_MdlCurrent.getManufacturingDate()));
                    batch_Mdl.getIpm().setMrp(DmShared.xNull(detail_MdlCurrent.getMrp()));
                    batch_Mdl.setSaleRate(DmShared.xNull(detail_MdlCurrent.getSaleRate()));
                    batch_Mdl.getIpm().setSkuMrp(DmShared.xNull(detail_MdlCurrent.getSkuMrp()));
                    batch_Mdl.setSkuSaleRate(DmShared.xNull(detail_MdlCurrent.getSkuSaleRate()));

                    batch_Mdl.setCode(DmShared.xNull(detail_MdlCurrent.getIbCode()));
                    batch_Mdl.setReceiveQty(DmShared.xNull(detail_MdlCurrent.getTotalQuantity()));
                    batch_Mdl.setReceiveDate(DmShared.dNull(mrTempCurrent.getStockVoucherDate()));
                    batch_Mdl.setDocId(DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
                    batch_Mdl.setRecId(DmShared.xNull(mrTempCurrent.getSharedKeyFieldName()));
                    batch_Mdl.setvType(DmShared.xNull(mrTempCurrent.getVoucherTypeCode()));
                    batch_Mdl.setvSno(DmShared.xNull(detail_MdlCurrent.getV_SNo()));
                    batch_Mdl.setPurchaseRateExFree(DmShared.xNull(detail_MdlCurrent.getRate()));
                    batch_Mdl.setPurchaseTaxableRateExFree(DmShared.xNull(detail_MdlCurrent.getRate()));

                    batch_Mdl.setStockSkuValue(DmServer.vNull(detail_MdlCurrent.getSkuValue(), "0.00"));
                    batch_Mdl.setStockLandedSkuValue(DmServer.vNull(detail_MdlCurrent.getSkuValue()));
                    batch_Mdl.setStockV_Sno(detail_MdlCurrent.getV_SNo());
                    batch_Mdl.setStockRefChallanNo(DmShared.xNull(detail_MdlCurrent.getRefNo()));
                    batch_Mdl.setStockRefChallanDate(DmShared.dNull(detail_MdlCurrent.getRefDate()));
                    batch_Mdl.setStockRemark(DmShared.dNull(detail_MdlCurrent.getRemark()));
                    batch_Mdl.setStockDepartmentCode(DmShared.xNull(mrTempCurrent.getDepartmentTag()));

                    lstItemBatch_MdlCurnt.add(batch_Mdl);
                }
            }
        }

        lstItemBatch_MdlPrev.clear();
        if (mrTempPrevious != null) {
            for (GrdMedicineDetail_Mdl detail_MdlPrev : mrTempPrevious.getListItemDetail()) {
                ItemBatch_Mdl batch_MdlPrev = new ItemBatch_Mdl();
                if (DmShared.iNull(detail_MdlPrev.getSharedKeyFieldCode()).longValue() != 0) {
                    batch_MdlPrev.getIpm().setItemCode(DmShared.xNull(detail_MdlPrev.getSharedKeyFieldCode()));
                    batch_MdlPrev.getIpm().setBatchNo(DmShared.xNull(detail_MdlPrev.getBatchNo()));
                    batch_MdlPrev.setBarcode(DmShared.xNull(detail_MdlPrev.getBarcode()));
                    batch_MdlPrev.getIpm().setExpiryDate(DmShared.dNull(detail_MdlPrev.getExpiryDate()));
                    batch_MdlPrev.getIpm().setManufacturingDate(DmShared.dNull(detail_MdlPrev.getManufacturingDate()));
                    batch_MdlPrev.getIpm().setMrp(DmShared.xNull(detail_MdlPrev.getMrp()));
                    batch_MdlPrev.setSaleRate(DmShared.xNull(detail_MdlPrev.getSaleRate()));
                    batch_MdlPrev.getIpm().setSkuMrp(DmShared.xNull(detail_MdlPrev.getSkuMrp()));
                    batch_MdlPrev.setSkuSaleRate(DmShared.xNull(detail_MdlPrev.getSkuSaleRate()));

                    batch_MdlPrev.setCode(DmShared.xNull(detail_MdlPrev.getIbCode()));
                    batch_MdlPrev.setReceiveQty(DmShared.xNull(detail_MdlPrev.getTotalQuantity()));
                    batch_MdlPrev.setReceiveDate(DmShared.dNull(mrTempPrevious.getStockVoucherDate()));
                    batch_MdlPrev.setDocId(DmShared.xNull(mrTempPrevious.getSharedKeyFieldCode()));
                    batch_MdlPrev.setRecId(DmShared.xNull(mrTempPrevious.getSharedKeyFieldName()));
                    batch_MdlPrev.setvType(DmShared.xNull(mrTempPrevious.getVoucherTypeCode()));
                    batch_MdlPrev.setvSno(DmShared.xNull(detail_MdlPrev.getV_SNo()));
                    batch_MdlPrev.setPurchaseRateExFree(DmShared.xNull(detail_MdlPrev.getRate()));

                    batch_MdlPrev.setStockSkuValue(DmServer.vNull(detail_MdlPrev.getSkuValue(), "0.00"));
                    batch_MdlPrev.setStockLandedSkuValue(DmServer.vNull(detail_MdlPrev.getSkuValue()));
                    batch_MdlPrev.setStockV_Sno(detail_MdlPrev.getV_SNo());
                    batch_MdlPrev.setStockRefChallanNo(DmShared.xNull(detail_MdlPrev.getRefNo()));
                    batch_MdlPrev.setStockRefChallanDate(DmShared.dNull(detail_MdlPrev.getRefDate()));
                    batch_MdlPrev.setStockRemark(DmShared.dNull(detail_MdlPrev.getRemark()));
                    batch_MdlPrev.setStockDepartmentCode(DmShared.xNull(mrTempCurrent.getDepartmentTag()));

                    lstItemBatch_MdlPrev.add(batch_MdlPrev);
                }
            }
        }

        if (mrTempCurrent != null) {
            compareProperties_Mdl.setDocId(DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
            compareProperties_Mdl.setRecId(DmShared.xNull(mrTempCurrent.getSharedKeyFieldName()));
            compareProperties_Mdl.setRecIdPrefix(DmShared.xNull(mrTempCurrent.getRecIdItemOpening().getPrefix()));
            compareProperties_Mdl.setV_No(DmShared.xNull(mrTempCurrent.getRecIdItemOpening().getCounter().toString()));
            compareProperties_Mdl.setV_Type(DmShared.xNull(mrTempCurrent.getVoucherTypeCode()));
//            compareProperties_Mdl.setDepartmentCode(DmShared.xNull(mrTempCurrent.getDepartmentTag()));
            compareProperties_Mdl.setCurrentReceiveDate(DmShared.dNull(mrTempCurrent.getStockVoucherDate()));
            compareProperties_Mdl.setContraSubCode(DmShared.xNull(mrTempCurrent.getReceivedByTag()));

        } else if (mrTempPrevious != null) { //Delete Case
            compareProperties_Mdl.setDocId(DmShared.xNull(mrTempPrevious.getSharedKeyFieldCode()));
            compareProperties_Mdl.setRecId(DmShared.xNull(mrTempPrevious.getSharedKeyFieldName()));
            compareProperties_Mdl.setRecIdPrefix(DmShared.xNull(mrTempPrevious.getRecIdItemOpening().getPrefix()));
            compareProperties_Mdl.setV_No(DmShared.xNull(mrTempPrevious.getRecIdItemOpening().getCounter().toString()));
            compareProperties_Mdl.setV_Type(DmShared.xNull(mrTempPrevious.getVoucherTypeCode()));
//            compareProperties_Mdl.setDepartmentCode(DmShared.xNull(mrTempPrevious.getDepartmentTag()));
            compareProperties_Mdl.setCurrentReceiveDate(DmShared.dNull(mrTempPrevious.getStockVoucherDate()));
            compareProperties_Mdl.setContraSubCode(DmShared.xNull(mrTempPrevious.getReceivedByTag()));

        }
        compareProperties_Mdl.setSite_Code(DmServer.getSsnSiteCode(this));
        compareProperties_Mdl.getCurrentIbm().addAll(lstItemBatch_MdlCurnt);
        compareProperties_Mdl.getPreviousIbm().addAll(lstItemBatch_MdlPrev);
        if (mrTempPrevious != null) { //Edit Case
            compareProperties_Mdl.setPreviousReceiveDate(DmShared.dNull(mrTempPrevious.getStockVoucherDate()));
        }
        return compareProperties_Mdl;
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
            rsTemp.close();
            psTemp.close();
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());

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

}
