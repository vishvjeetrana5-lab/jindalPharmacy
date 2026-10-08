package aarogya.pharmacy.medicineTransferWholeSale.server;

import aarogya.pharmacy.medicineTransferWholeSale.shared.MedicineTransferWholeSale_Srv;
import aarogya.pharmacy.medicineTransferWholeSale.shared.Find_Mdl;
import aarogya.pharmacy.medicineTransferWholeSale.shared.MedicineTransferWholeSale_Mdl;
import aarogya.pharmacy.medicineTransferWholeSale.shared.TxtHMedicineName_Mdl;
import aarogya.pharmacy.medicineTransferWholeSale.shared.DefaultValue_Mdl;
import aarogya.pharmacy.medicineTransferWholeSale.shared.GrdMedicineDetail_Mdl;
import aarogya.pharmacy.medicineTransferWholeSale.shared.MedicineIssueWithRequisition_Mdl;
import aarogya.pharmacy.medicineTransferWholeSale.shared.MedicineTransferWholeSale_Vld;
import aarogya.pharmacy.medicineTransferWholeSale.shared.TxtHRequisitionNo_Mdl;
import aarogya.pharmacy.medicineTransferWholeSale.shared.TxtHTransferTo_Mdl;
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
import aarogya.selfToolkit.stStatic.shared.STEnum.ItemBatchType;
import aarogya.selfToolkit.toolTip.shared.ToolTip_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmToolkit.dmJasper.server.DmJasper;
import dataman.dmToolkit.stock.server.Stock_Server;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MedicineTransferWholeSale_SrvImpl extends DmRSSPrivate implements MedicineTransferWholeSale_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String vCat = pvc.MEDICINE_TRANSFER_WHOLE_SALE;
    private final String voucherTypefieldName = "sh.V_Type", voucherDateFieldName = "sh.V_Date";

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission) {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);
        String sqlQuery, sqlCondition;
        DmPreparedStatement psTemp = null;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " select sh.docId "
                    + "         from stockHead as sh "
                    + "         left join voucher_Type as vt on sh.v_Type = vt.v_Type  "
                    + "         where vt.category in ('" + vCat + "') and sh.site_Code = :site_Code and sh.v_prefix = :vPrefix ";
            sqlCondition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypefieldName, userPermission);
            if (!sqlCondition.equalsIgnoreCase("")) {
                sqlQuery += "and " + sqlCondition;
            }
            sqlQuery += " order by sh.v_Date desc, vt.short_Name, sh.v_No desc, sh.recId ";
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
    public DmResult<MedicineTransferWholeSale_Mdl> getMoveRec(String strKey) {
        DmResult<MedicineTransferWholeSale_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        MedicineTransferWholeSale_Mdl mrTemp = new MedicineTransferWholeSale_Mdl();
        dmrTemp.setData(mrTemp);
        GrdMedicineDetail_Mdl node;
        TxtHMedicineName_Mdl itemMdl;
        String itemCsv = "";

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*==============================<< Get header detail >>===============================*/
            sqlQuery = "select	sh.docId, sh.recId, sh.v_Date, sh.u_Name, sh.preparedBy, sh.v_Type, vt.description"
                    + "         , sh.recIdPrefix, sh.v_No, sh.refNo, sh.refDate, st.subCode as transferToCode, "
                    + "		sgrb.name as transferToName "
                    + "		from stockHead as sh "
                    + "		left join ( "
                    + "                     select s.docId,"
                    + "                     max(subCode) as subCode "
                    + "                     from stockHeadDetail s "
                    + "                     where s.docId = :docId "
                    + "                     group by s.docId "
                    + "		)   as st on sh.docId = st.docId "
                    + "		left join voucher_Type as vt on sh.v_Type = vt.v_Type "
                    + "		left join subGroup as sgrb on st.subCode = sgrb.subCode "
                    + "		where sh.docId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("docId")).toString());
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.getRecIdTransfer().setRecIdValue(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.getRecIdTransfer().setCounter(DmShared.iNull(rsTemp.getString("v_No")).longValue());
                mrTemp.getRecIdTransfer().setPrefix(DmShared.xNull(rsTemp.getString("recIdPrefix")));
                mrTemp.setVoucherDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                mrTemp.setVoucherTypeCode(DmShared.xNull(rsTemp.getString("v_Type")));
                mrTemp.setVoucherTypeName(DmShared.xNull(rsTemp.getString("description")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setRefNo(DmShared.xNull(rsTemp.getString("refNo")));
                mrTemp.setRefDate(DmShared.dNull(rsTemp.getDate("refDate")));
                mrTemp.setTransferToTag(DmShared.xNull(rsTemp.getString("transferToCode")));
                mrTemp.setTransferToText(DmShared.xNull(rsTemp.getString("transferToName")));
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Get header detail (Ref No, Ref Date, Department To and Department From) >>===============================*/
            sqlQuery = "select shd.departmentCode, depFr.name departmentFrom, shd.departmentToCode,im.sku as skuCode, depTo.name departmentTo, "
                    + "		shd.itemCode, im.name item, shd.quantity, shd.site_Code, shd.site_Code_To, "
                    + "		shd.ibCode, shd.batchNo, shd.manufacturingDate, shd.expiryDate,shd.saleRate,shd.mrp, "
                    + "		um.decimalPlaces, "
                    + "         um.unit skuName, shd.remark, shd.v_SNo,im.purchaseUnit as purchaseUnitCode,ump.unit as purchaseUnitName,"
                    + "         im.purchaseUnitConversionRate,shd.skuMrp,shd.skuSaleRate , shd.requisitionDocId,shd.requisitionV_SNo ,"
                    + "         mrq.recId as reqNo, shd.barCode,shd.tempQty,shd.rate "
                    + "	from stockHeadDetail shd "
                    + "	left join subGroup depTo on shd.departmentToCode = depTo.subCode "
                    + "	left join subGroup depFr on shd.departmentCode = depFr.subCode "
                    + "	left join itemMast im on im.code = shd.itemCode "
                    + "	left join unitMast um on um.code = im.sku "
                    + " left join unitMast as ump on im.purchaseUnit = ump.code  "
                    + " left join requisition mrq ON mrq.docId = shd.requisitionDocId "
                    + "	where shd.docId = :docId "
                    + "	order by shd.v_SNo ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdMedicineDetail_Mdl();

                itemMdl = new TxtHMedicineName_Mdl();
                itemMdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                itemMdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("item")));
                itemMdl.setBestBefore(!DmShared.dNull(rsTemp.getDate("manufacturingDate")).trim().equals(""));
                itemMdl.setPurchaseUnitConversionRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));
                itemMdl.setSkuDecimalPlaces(DmShared.iNull(rsTemp.getString("decimalPlaces")).intValue());
                itemMdl.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                itemMdl.setSkuCode(DmShared.xNull(rsTemp.getString("skuCode")));
                node.setMedicineMdl(itemMdl);

                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("item")));
                node.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                node.setBarCode(DmShared.xNull(rsTemp.getString("barCode")));
                itemCsv = Pharmacy_Server.getItemCodeCsv(node.getBarCode(), mrTemp.getVoucherDate(), mrTemp.getDepartmentFromTag(), DmServer.getSsnSiteCode(this), ItemBatchType.ALL_BATCH, false, true, cnTemp);
                node.setItemCodeCsv(itemCsv);
                node.setExpiryDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                node.setIbCode(DmShared.xNull(rsTemp.getString("ibCode")));
                node.setManufacturingDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));
                node.setMrp(DmServer.vNull(rsTemp.getString("mrp"), "0.00"));
                node.setSaleRate(DmServer.vNull(rsTemp.getString("saleRate"), "0.00"));
                node.setSkuMrp(DmServer.vNull(rsTemp.getString("skuMrp"), DmShared.getNumberFormat(7)));
                node.setSkuSaleRate(DmServer.vNull(rsTemp.getString("skuSaleRate"), DmShared.getNumberFormat(7)));
                node.setTrfRate(DmServer.vNull(rsTemp.getString("rate"), DmShared.getNumberFormat(7)));
                node.setSkuQtyForRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));
                node.setPurchaseUnitCode(DmShared.xNull(rsTemp.getString("purchaseUnitCode")));
                node.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));

                node.setV_SNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                node.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                node.setSkuCode(DmShared.xNull(rsTemp.getString("skuCode")));
                mrTemp.setDepartmentFromSite(DmShared.xNull(rsTemp.getString("site_Code")));
                mrTemp.setDepartmentFromTag(DmShared.xNull(rsTemp.getString("departmentCode")));
                mrTemp.setDepartmentFromText(DmShared.xNull(rsTemp.getString("departmentFrom")));
                mrTemp.setDepartmentToSite(DmShared.xNull(rsTemp.getString("site_Code")));
                mrTemp.setDepartmentToTag(DmShared.xNull(rsTemp.getString("departmentToCode")));
                mrTemp.setDepartmentToText(DmShared.xNull(rsTemp.getString("departmentTo")));
                node.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                node.setQuantity(DmServer.vNull(Math.abs(DmShared.vNull(rsTemp.getString("quantity")))));
                node.setTrfQuantity(DmServer.vNull(Math.abs(DmShared.vNull(rsTemp.getString("tempQty")))));
                if (DmShared.iNull(rsTemp.getString("requisitionDocId")).longValue() != 0) {
                    mrTemp.setRequisitionDocId(DmShared.foriegnKey(rsTemp.getString("requisitionDocId")));
                    mrTemp.setRequisitionNo(DmShared.foriegnKey(rsTemp.getString("reqNo")));
                }
                node.setRequisitionDocId(DmShared.xNull(rsTemp.getString("requisitionDocId")));
                node.setRequisitionNo(DmShared.xNull(rsTemp.getString("reqNo")));
                node.setRequisitionV_SNo(DmShared.xNull(rsTemp.getString("requisitionV_SNo")));

                mrTemp.getListItemDetail().add(node);
            }
            rsTemp.close();
            psTemp.close();

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

            /*==============================<< Manage Batch Detail >>====================================*/
            Stock_Server.deleteStock(keyCode, cnTemp);

            manageRequisitionStatus(keyCode, true, cnTemp);

            /*==============================<< Delete record from stock >>===============================*/
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
    public DmResult<String> saveRecord(UserPermission userPermission, MedicineTransferWholeSale_Mdl mrTemp, MedicineTransferWholeSale_Mdl prevMdl, String voucherPrefix) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery, userName, siteCode;
        ResultSet rsTemp = null;
        dmrTemp.setData("");

        try {
            MedicineTransferWholeSale_Vld.chkSave(mrTemp);
            userName = DmServer.getSsnUserName(this);
            siteCode = DmServer.getSsnSiteCode(this);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this));
                mrTemp.setRecIdTransfer(DmServer.getRecId("stockHead", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecIdTransfer(), mrTemp.getVoucherDate(), DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this, true));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(mrTemp.getRecIdTransfer().getRecIdValue()));
            } else {
                DmServer.getRecId("stockHead", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecIdTransfer(), mrTemp.getVoucherDate(), DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this, true);
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            STServer.checkItemsRestrictedDepartment(mrTemp.getLineItemCodesCsv(), mrTemp.getDepartmentFromTag(), mrTemp.getDepartmentFromText(), cnTemp);

            STServer.checkItemsRestrictedDepartment(mrTemp.getLineItemCodesCsv(), mrTemp.getDepartmentToTag(), mrTemp.getDepartmentToText(), cnTemp);

            /*==============================<< Check items for Requisition item change >>====================================*/
            sqlQuery = "SELECT COUNT(*) AS totalInvalidRecords FROM requisitionDetail rd" + DmShared.xNull(mrTemp.getRequisitionItemChangeCondition());
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("totalInvalidRecords")).intValue() > 0) {
                    throw new DmException(0, "Requisition has been changed, can't save record, refresh record then save");
                }
            }
            rsTemp.close();
            psTemp.close();

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

            if (blnAdd) {
                sqlQuery = "insert into stockHead (docId, v_Type, v_No, recId, v_Prefix, v_Date, refNo, refDate, u_EntDt, site_Code, preparedBy, recIdPrefix) "
                        + " values (:docId, :vType, :vNo, :recId, :vPrefix, :vDate, :refNo, :refDate, :uEntDt, :siteCode, :userName, :recIdPrefix)";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("recId", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getRecIdTransfer().getPrefix()));
                psTemp.setString("vType", DmShared.foriegnKey(mrTemp.getVoucherTypeCode()));
                psTemp.setString("refNo", DmShared.xNull(mrTemp.getRefNo()));
                psTemp.setDate("refDate", DmServer.convertDate(mrTemp.getRefDate()));
                psTemp.setDate("vDate", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setString("vPrefix", voucherPrefix);
                psTemp.setLong("vNo", DmShared.iNull(mrTemp.getRecIdTransfer().getCounter()).longValue());
                psTemp.setString("siteCode", siteCode);
                psTemp.setString("userName", userName);
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            } else {
                sqlQuery = " update stockHead Set "
                        + " v_Date = :vDate, "
                        + " transferDt = :transferDt, "
                        + " u_Name = :u_Name, "
                        + " refNo = :refNo, "
                        + " refDate = :refDate, "
                        + " u_EntDt = :uEntDt "
                        + " Where docId = :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setDate("vDate", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("refNo", DmShared.xNull(mrTemp.getRefNo()));
                psTemp.setDate("refDate", DmServer.convertDate(mrTemp.getRefDate()));
                psTemp.setString("transferDt", null);
                psTemp.setString("u_Name", userName);
            }
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Validate Removed Received Items >>====================================*/
            if (!blnAdd) {
                ItemBatch_Server.validateRemovedReceivedItems(iniItemBatchProperties(mrTemp, prevMdl), DmEnum.StockEntryType.STOCK_TRANSFER_RECEIVE, cnTemp, this);
            }

            Stock_Server.deleteStock(mrTemp.getSharedKeyFieldCode(), cnTemp, true);

            manageRequisitionStatus(mrTemp.getSharedKeyFieldCode(), true, cnTemp);

            sqlQuery = " Delete From stockHeadDetail Where docId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            int v_SNo = 0;
            for (GrdMedicineDetail_Mdl detail_Mdl : mrTemp.getListItemDetail()) {
                if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() > 0) {
                    detail_Mdl.setV_SNo(String.valueOf(++v_SNo));
                    sqlQuery = " insert into stockHeadDetail (docId, v_SNo, departmentCode, departmentToCode, itemCode, quantity, requisitionDocId, requisitionV_SNo, site_Code, "
                            + " site_Code_To, remark, isSystemGenerated, ibCode, batchNo, manufacturingDate, expiryDate,"
                            + " saleRate,mrp,subCode,skuMrp,skuSaleRate, barCode,tempQty,rate) "
                            + " values (:docId, :vSno, :departmentCode, :departmentToCode, :itemCode, :quantity, :requisitionDocId, :requisitionV_SNo,  :siteCode, :siteToCode, "
                            + " :remark, 0, :ibCode, :batchNo, :mfgDate, :expDate,:saleRate,:mrp,:subCode,:skuMrp,:skuSaleRate, :barCode, :tempQty, :rate)";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                    psTemp.setString("vSno", detail_Mdl.getV_SNo());
                    psTemp.setString("departmentCode", DmShared.foriegnKey(mrTemp.getDepartmentFromTag()));
                    psTemp.setString("departmentToCode", DmShared.foriegnKey(mrTemp.getDepartmentToTag()));
                    psTemp.setString("itemCode", DmShared.foriegnKey(detail_Mdl.getSharedKeyFieldCode()));
                    psTemp.setString("quantity", DmServer.vNull(detail_Mdl.getQuantity()));
                    psTemp.setString("tempQty", DmServer.vNull(detail_Mdl.getTrfQuantity()));
                    psTemp.setString("requisitionDocId", DmShared.foriegnKey(detail_Mdl.getRequisitionDocId()));
                    psTemp.setString("requisitionV_SNo", DmShared.foriegnKey(detail_Mdl.getRequisitionV_SNo()));

                    psTemp.setString("remark", DmShared.xNull(detail_Mdl.getRemark()));
                    psTemp.setString("siteCode", DmShared.foriegnKey(mrTemp.getDepartmentFromSite()));
                    psTemp.setString("siteToCode", DmShared.foriegnKey(mrTemp.getDepartmentToSite()));
                    psTemp.setString("ibCode", DmShared.foriegnKey(detail_Mdl.getIbCode()));
                    psTemp.setString("quantity", DmServer.vNull(detail_Mdl.getQuantity()));
                    psTemp.setString("batchNo", DmShared.xNull(detail_Mdl.getBatchNo()));
                    psTemp.setDate("mfgDate", DmServer.convertDate(detail_Mdl.getManufacturingDate()));
                    psTemp.setDate("expDate", DmServer.convertDate(detail_Mdl.getExpiryDate()));
                    psTemp.setString("mrp", DmServer.vNull(detail_Mdl.getMrp()));
                    psTemp.setString("saleRate", DmServer.vNull(detail_Mdl.getSaleRate()));
                    psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getTransferToTag()));
                    psTemp.setString("skuMrp", DmServer.vNull(detail_Mdl.getSkuMrp()));
                    psTemp.setString("rate", DmServer.vNull(detail_Mdl.getTrfRate()));
                    psTemp.setString("skuSaleRate", DmServer.vNull(detail_Mdl.getSkuSaleRate()));
                    psTemp.setString("barCode", DmShared.xNull(detail_Mdl.getBarCode()));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

            //============< Save Issue Stock >===================//
            v_SNo = saveStockRecord(mrTemp, cnTemp, 0, false);
            //============< Save Receive Stock >===================//
            saveStockRecord(mrTemp, cnTemp, v_SNo, true);
            //============< **************** >===================//

            manageRequisitionStatus(mrTemp.getSharedKeyFieldCode(), false, cnTemp);

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
        String sqlQuery, sqlCondition;

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select sh.docId, sh.recId, sh.v_No, vt.short_Name, sh.v_Date, depTo.name departmentTo, depFr.name departmentFrom, "
                    + "         sh.refNo, sh.refDate "
                    + "	from stockHead sh "
                    + "	left join voucher_Type vt on vt.v_Type = sh.v_Type "
                    + "	left join ( "
                    + "             select shd.docId, max(shd.departmentCode) departmentCode, max(shd.departmentToCode) departmentToCode "
                    + "                 from stockHeadDetail shd "
                    + "			group by shd.docId "
                    + "         ) shd on shd.docId = sh.docId "
                    + "	left join subGroup depTo on shd.departmentToCode = depTo.subCode "
                    + "	left join subGroup depFr on shd.departmentCode = depFr.subCode "
                    + "	where sh.site_Code = :siteCode and vt.category in (" + getVoucherCategory(vCat) + ")  and sh.v_prefix = :vPrefix ";
            sqlCondition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypefieldName, userPermission);
            if (!sqlCondition.equalsIgnoreCase("")) {
                sqlQuery += "and " + sqlCondition;
            }
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("vPrefix", DmServer.getSsnVoucherPrefix(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("docId")).toString());
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                listNode.setVoucherType(DmShared.xNull(rsTemp.getString("short_Name")));
                listNode.setVoucherDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setDepartmentFrom(DmShared.xNull(rsTemp.getString("departmentFrom")));
                listNode.setDepartmentTo(DmShared.xNull(rsTemp.getString("departmentTo")));
                listNode.setRefNo(DmShared.xNull(rsTemp.getString("refNo")));
                listNode.setRefDate(DmShared.dNull(rsTemp.getDate("refDate")));
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
            sqlWithParam.setSQL(" select v_Type as code, description as name from voucher_Type where category = " + getVoucherCategory(vCat) + " and isActive = 1 ");
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
            sqlQuery = " select v_Type, description from voucher_Type where category = " + getVoucherCategory(vCat) + " and isActive = 1 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setVoucherTypeCode(DmShared.xNull(rsTemp.getString("v_Type")));
                defaultValue.setVoucherTypeName(DmShared.xNull(rsTemp.getString("description")));
            }
            rsTemp.close();
            psTemp.close();

            //Department
            sqlQuery = "select dm.subCode, sg.name , isnull(envDept.link_Site_Code, :siteCode) linkSite"
                    + "     from departmentMast dm "
                    + "     left join subGroup sg on sg.subCode = dm.subCode "
                    + "     left join enviroDepartmentSiteLink as envDept on dm.subcode = envDept.departmentCode "
                    + "     where dm.subCode = :subCode and sg.isActive = 1 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("subCode", STServer.getSsnDepartmentCode(this));
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setDepartmentTag(DmShared.xNull(rsTemp.getString("subCode")));
                defaultValue.setDepartmentText(DmShared.xNull(rsTemp.getString("name")));
                defaultValue.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("linkSite")));
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
                defaultValue.setBarcodeActive(DmShared.iNull(rsTemp.getString("pharmacyBarcodeActive")).toString());
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile, String department, boolean flagFromDepartment) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String condition = "";
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (flagFromDepartment) {
                condition += " and isnull(envDept.link_Site_Code, :siteCode) = :siteCode ";
                if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                    condition += " and dm.subCode in (" + DmServer.getParamList(":department", STServer.getSsnDepartmentCodeList(this)) + ")";
                }
                condition += " and sgn.manualCode in ('CM','PH') ";
            }

            sqlWithParam.setSQL("select	dm.subCode as code, sg.name, isnull(envDept.link_Site_Code, :siteCode) linkSite "
                    + "		from departmentMast as dm "
                    + "		left join subGroup as sg on dm.subCode = sg.subCode "
                    + "		left join subGroupNature as sgn on sgn.code = sg.nature "
                    + "         left join enviroDepartmentSiteLink as envDept on dm.subcode = envDept.departmentCode "
                    + "		where sg.isActive = 1 and dm.subCode <> :subCode " + condition);
            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("department", STServer.getSsnDepartmentCodeList(this));
            }
            sqlWithParam.setString("subCode", DmShared.iNull(department).toString());
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("linkSite")));
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

    private String getVoucherCategory(String voucherCategory) {
        String returnValue = "'" + voucherCategory + "'";
        return returnValue;
    }

    private int saveStockRecord(MedicineTransferWholeSale_Mdl mrTemp, Connection cnTemp, int maxVSno, boolean flagSystemGenerated) {
        int returnValue = 0, vSNo = 0;
        String sqlQuery;
        try {
            if (flagSystemGenerated) {
                vSNo = maxVSno;
            }
            for (GrdMedicineDetail_Mdl detail_Mdl : mrTemp.getListItemDetail()) {
                if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() > 0) {
                    Stock_Mdl stock_Mdl = new Stock_Mdl();
                    stock_Mdl.setSharedKeyFieldCode(DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                    stock_Mdl.setV_Type(DmShared.xNull(mrTemp.getVoucherTypeCode()));
                    stock_Mdl.setV_No(DmShared.iNull(mrTemp.getRecIdTransfer().getCounter()).toString());
                    stock_Mdl.setRecId(DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                    stock_Mdl.setRecIdPrefix(DmShared.xNull(mrTemp.getRecIdTransfer().getPrefix()));
                    stock_Mdl.setV_Date(DmShared.dNull(mrTemp.getVoucherDate()));
                    stock_Mdl.setRefChallanNo(DmShared.xNull(mrTemp.getRefNo()));
                    stock_Mdl.setRefChallanDate(DmShared.dNull(mrTemp.getRefDate()));
                    stock_Mdl.setStockContraSubCode(DmShared.xNull(mrTemp.getTransferToTag()));
                    stock_Mdl.setItemCode(DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()));
                    stock_Mdl.setIbCode(detail_Mdl.getIbCode());

                    if (flagSystemGenerated == false) {
                        stock_Mdl.setSharedKeyFieldName(detail_Mdl.getV_SNo());
                        stock_Mdl.setSite_Code(DmShared.xNull(mrTemp.getDepartmentFromSite()));
                        stock_Mdl.setDepartmentCode(DmShared.xNull(mrTemp.getDepartmentFromTag()));
                        stock_Mdl.setIsSystemGenerated("0");
                        stock_Mdl.setIsuQty(DmServer.vNull(detail_Mdl.getQuantity()));
                    } else {
                        stock_Mdl.setRefV_SNo(detail_Mdl.getV_SNo());
                        stock_Mdl.setSharedKeyFieldName(String.valueOf(++vSNo));
                        stock_Mdl.setSite_Code(DmShared.xNull(mrTemp.getDepartmentToSite()));
                        stock_Mdl.setDepartmentCode(DmShared.xNull(mrTemp.getDepartmentToTag()));
                        stock_Mdl.setIsSystemGenerated("1");
                        stock_Mdl.setRecQty(DmServer.vNull(detail_Mdl.getQuantity()));

                        StockFifo_Mdl stockFifo_Mdl = new StockFifo_Mdl();
                        stockFifo_Mdl.setSharedKeyFieldCode(DmShared.xNull(stock_Mdl.getSharedKeyFieldCode()));
                        stockFifo_Mdl.setSharedKeyFieldName(DmShared.xNull(stock_Mdl.getSharedKeyFieldName()));
                        stockFifo_Mdl.setRecQty(DmServer.vNull(detail_Mdl.getQuantity()));
                        stockFifo_Mdl.setStockValueDate(DmShared.dNull(stock_Mdl.getV_Date()));

                        stock_Mdl.setStockFifo_Mdl(stockFifo_Mdl);

                    }

                    if (returnValue < DmShared.iNull(detail_Mdl.getV_SNo()).intValue()) {
                        returnValue = DmShared.iNull(detail_Mdl.getV_SNo()).intValue();
                    }

                    Stock_Server.saveStock(stock_Mdl, flagSystemGenerated ? DmEnum.StockEntryType.STOCK_TRANSFER_RECEIVE : DmEnum.StockEntryType.OTHER, cnTemp);
                    
                    if (flagSystemGenerated == false) {
                        sqlQuery = "UPDATE itemDepartmentInfo SET isHoldRequirement = 0 "
                                + " WHERE itemCode = " + DmShared.xNull(detail_Mdl.getSharedKeyFieldCode())
                                + " AND departmentCode = " + DmShared.xNull(mrTemp.getDepartmentFromTag())
                                + " AND site_Code = " + DmShared.xNull(mrTemp.getDepartmentFromSite())
                                + " AND ISNULL(isHoldRequirement,0) <> 0 ";
                        DmServer.executeSQL(sqlQuery, cnTemp);
                    }
                }
            }
            return returnValue;
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }
    }

    @Override
    public DmResultPageing<List<TxtHTransferTo_Mdl>> getTxtHTransferTo(String v_Type, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHTransferTo_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHTransferTo_Mdl> lstTemp = new ArrayList<>();
        TxtHTransferTo_Mdl listNode;
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
                listNode = new TxtHTransferTo_Mdl();
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

    private CompareProperties_Mdl iniItemBatchProperties(MedicineTransferWholeSale_Mdl mrTempCurrent, MedicineTransferWholeSale_Mdl mrTempPrevious) {
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
                batch_Mdl.setStockDepartmentCode(DmShared.xNull(mrTempCurrent.getDepartmentToTag()));
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
                batch_MdlPrev.setStockDepartmentCode(DmShared.xNull(mrTempCurrent.getDepartmentToTag()));
                lstItemBatch_MdlPrev.add(batch_MdlPrev);
            }
        }

        compareProperties_Mdl.setDocId(DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
        compareProperties_Mdl.setRecId(DmShared.xNull(mrTempCurrent.getSharedKeyFieldName()));
        compareProperties_Mdl.setRecIdPrefix(DmShared.xNull(mrTempCurrent.getRecIdTransfer().getPrefix()));
        compareProperties_Mdl.setV_No(DmShared.xNull(mrTempCurrent.getRecIdTransfer().getCounter().toString()));
        compareProperties_Mdl.setV_Type(DmShared.xNull(mrTempCurrent.getVoucherTypeCode()));
//        compareProperties_Mdl.setDepartmentCode(DmShared.xNull(mrTempCurrent.getDepartmentToTag()));
        compareProperties_Mdl.setCurrentReceiveDate(DmShared.dNull(mrTempCurrent.getVoucherDate()));
        compareProperties_Mdl.setSite_Code(DmServer.getSsnSiteCode(this));
        compareProperties_Mdl.getCurrentIbm().addAll(lstItemBatch_MdlCurnt);
        compareProperties_Mdl.getPreviousIbm().addAll(lstItemBatch_MdlPrev);
        compareProperties_Mdl.setPreviousReceiveDate(DmShared.dNull(mrTempPrevious.getVoucherDate()));

        return compareProperties_Mdl;
    }

    @Override
    public DmResult<List<GrdMedicineDetail_Mdl>> getMedicineDetail(String reqDocId, String MedicineIssueDocId, String departmentCode, String v_Date) {
        DmResult<List<GrdMedicineDetail_Mdl>> dmrTemp = new DmResult();

        try {
            dmrTemp.setData(getMedicineDetails(reqDocId, MedicineIssueDocId, departmentCode, v_Date).getGrdItemDetailList());
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }

        return dmrTemp;
    }

    private MedicineIssueWithRequisition_Mdl getMedicineDetails(String reqDocId, String MedicineTransferDocId, String departmentCode, String v_Date) throws Exception {
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, condition = "", recQty = "";
        MedicineIssueWithRequisition_Mdl itemIssueWithRequisition = new MedicineIssueWithRequisition_Mdl();
        GrdMedicineDetail_Mdl node;
        TxtHMedicineName_Mdl itemMdl;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(MedicineTransferDocId).equalsIgnoreCase("")) {
                recQty = " ISNULL(mrqd.reqQty,0) - ISNULL(mrqd.issueQty,0) + ISNULL(sd.quantity,0) AS reqQty, ";
                condition += " AND (CASE WHEN (mrqd.isStatus = 0) THEN 1 WHEN (mrqd.isStatus = 2 And ISNULL(mrqd.reqQty,0) - ISNULL(mrqd.issueQty,0) + ISNULL(sd.quantity,0) > 0) THEN 1 ELSE 0 END) = 1 ";
            } else {
                recQty = " ISNULL(mrqd.reqQty,0) - ISNULL(mrqd.issueQty,0) AS reqQty, ";
                condition += " And ISNULL(mrqd.reqQty,0) - ISNULL(mrqd.issueQty,0) > 0 ";
                condition += " and mrqd.isStatus=0 ";
            }

            /*==============================<< Get Item detail (via list) >>===============================*/
            sqlQuery = " select mrq.docid,mrq.recid, mrqd.v_SNo, " + recQty
                    + "         mrqd.itemCode "
                    + "         ,im.name as itemName "
                    + "		,im.isBestBefore "
                    + "         ,im.allowNegativeStock "
                    + "         ,im.propertyList "
                    + "         ,um.unit as skuName "
                    + "         ,um.decimalPlaces AS skuDecimalPlaces "
                    + "		,im.sku as skuCode "
                    + "         ,im.purchaseUnitConversionRate "
                    + "         ,mrqd.colourCode,mrqd.sizeCode "
                    + "         ,icm.name as colourName "
                    + "         ,ism.name as sizeName "
                    + "         ,im.purchaseUnit as purchaseUnitCode "
                    + "         ,ump.unit as purchaseUnitName "
                    + "         ,im.isBarcodeActive "
                    + "	from requisition mrq "
                    + "	left Join requisitionDetail as mrqd on mrqd.docId = mrq.docid  "
                    + "	left join voucher_Type vt on vt.v_Type = mrq.v_Type "
                    + "	left join itemMast im on im.code = mrqd.itemCode "
                    + " left join itemGroup ig on ig.code = im.itemGroup "
                    + "	left join unitMast um ON um.code = im.sku "
                    + "	left join unitMast ump ON ump.code = im.purchaseUnit "
                    + "	left join itemColourMast icm ON icm.code = mrqd.colourCode "
                    + "	left join itemSizeMast ism ON ism.code = mrqd.sizeCode "
                    + "	left join hsnMast hm on hm.code = im.hsnCode ";

            if (!DmShared.xNull(MedicineTransferDocId).equalsIgnoreCase("")) {
                sqlQuery += "LEFT JOIN ( "
                        + " SELECT shd.* "
                        + " from stockHeadDetail shd "
                        + " where shd.docid=:itemIssueDocId "
                        + " )as sd ON sd.requisitionDocId=mrqd.docId and sd.requisitionV_SNo=mrqd.V_SNo ";

            }
            sqlQuery += "	where mrq.docId = :docId  and ISNULL(mrq.approvedBy,'') <> '' and mrq.site_Code_To= :siteCode "
                    + condition;
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp.setString("docId", reqDocId);
            if (!DmShared.xNull(MedicineTransferDocId).equalsIgnoreCase("")) {
                psTemp.setString("itemIssueDocId", MedicineTransferDocId);
            }
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                itemIssueWithRequisition.setRequisitionCode(DmShared.xNull(rsTemp.getString("docId")));
                itemIssueWithRequisition.setRequisitionName(DmShared.xNull(rsTemp.getString("recid")));

                node = new GrdMedicineDetail_Mdl();

                itemMdl = new TxtHMedicineName_Mdl();
                itemMdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                itemMdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                itemMdl.setBestBefore(DmShared.iNull(rsTemp.getString("isBestBefore")).intValue() == 1);
                itemMdl.setAllowNegativeStock(DmShared.iNull(rsTemp.getString("allowNegativeStock")).intValue() == 1);
                itemMdl.setIsBarcodeActive(DmShared.iNull(rsTemp.getString("isBarcodeActive")).toString());
                itemMdl.setPurchaseUnitConversionRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));
                itemMdl.setSkuDecimalPlaces(DmShared.iNull(rsTemp.getString("skuDecimalPlaces")).intValue());
                itemMdl.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                itemMdl.setSkuCode(DmShared.xNull(rsTemp.getString("skuCode")));
                node.setMedicineMdl(itemMdl);

                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                node.setQuantity(DmServer.vNull(rsTemp.getString("reqQty")));
                node.setQuantityTotalPerItem(DmServer.vNull(rsTemp.getString("reqQty")));
                node.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                node.setSkuCode(DmShared.xNull(rsTemp.getString("skuCode")));
                node.setPurchaseUnitCode(DmShared.xNull(rsTemp.getString("purchaseUnitCode")));
                node.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                node.setSkuQtyForRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));
                node.setRequisitionDocId(DmShared.xNull(rsTemp.getString("docid")));
                node.setRequisitionNo(DmShared.xNull(rsTemp.getString("recid")));
                node.setRequisitionV_SNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                node.setIsBarcodeActive(DmShared.iNull(rsTemp.getString("isBarcodeActive")).toString());
                itemIssueWithRequisition.getGrdItemDetailList().add(node);
            }
            rsTemp.close();
            psTemp.close();

            List<GrdMedicineDetail_Mdl> lstTemp = new ArrayList<>();
            lstTemp.addAll(itemIssueWithRequisition.getGrdItemDetailList());
            itemIssueWithRequisition.getGrdItemDetailList().clear();
            itemIssueWithRequisition.getGrdItemDetailList().addAll(fillBatchFIFO(lstTemp, v_Date, departmentCode, cnTemp));

            cnTemp.close();

        } catch (Exception e) {
            throw new Exception(e.getMessage());
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

        return itemIssueWithRequisition;
    }

    private List<GrdMedicineDetail_Mdl> fillBatchFIFO(List<GrdMedicineDetail_Mdl> lstNode, String v_Date, String departmentCode, Connection cnTemp) throws Exception {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        List<GrdMedicineDetail_Mdl> listNode = new ArrayList<>();
        GrdMedicineDetail_Mdl node;
        double balanceQty = 0.0;
        String selectedIbCodeSQL = "";
        TxtHMedicineName_Mdl itemMdl;

        try {

            selectedIbCodeSQL = " SELECT 0 AS ibCode, 0 AS qty ";

            for (GrdMedicineDetail_Mdl node1 : lstNode) {

                balanceQty = DmShared.vNull(node1.getQuantityTotalPerItem()).doubleValue();

                sqlQuery = " SELECT (ISNULL(ibs.balanceQty,0) - ISNULL(ibt.qty,0)) as stockQuantity "
                        + " ,ib.code as ibCode"
                        + " ,ib.batchNo "
                        + " ,ib.barcode "
                        + " ,ib.manufacturingDate "
                        + " ,ib.expiryDate "
                        + " ,ib.saleRate "
                        + " ,ib.mrp "
                        + " ,ib.skuSaleRate "
                        + " ,ib.skuMrp "
                        + " FROM itembatch ib "
                        + " LEFT JOIN itemBatchStock ibs ON ibs.ibCode=ib.code "
                        + " LEFT JOIN ("
                        + "             SELECT ibTemp.ibCode, SUM(ibTemp.qty) AS qty "
                        + "             FROM (" + selectedIbCodeSQL + ") AS ibTemp "
                        + "             GROUP BY ibTemp.ibCode "
                        + "             ) AS ibt ON ibt.ibCode = ib.code"
                        + " WHERE ib.itemCode=:itemCode "
                        + " AND ISNULL(ibs.departmentCode,:departmentCode) = :departmentCode "
                        + " AND ISNULL(ibs.site_Code,:site_Code) = :site_Code "
                        + " AND (ISNULL(ibs.balanceQty,0) - ISNULL(ibt.qty,0)) > 0 "
                        + " AND ib.expiryDate > :v_Date ";
                if (DmShared.iNull(node1.getIbCode()).longValue() > 0) {
                    sqlQuery += " AND ib.code = :ibCode ";
                }
                sqlQuery += " ORDER BY ib.expiryDate , ISNULL(ibs.balanceQty,0) DESC";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setDate("v_Date", DmServer.convertDate(DmShared.dNull(v_Date)));
                psTemp.setString("itemCode", DmShared.xNull(node1.getSharedKeyFieldCode()));
                psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
                psTemp.setString("departmentCode", DmShared.iNull(departmentCode).toString());
                if (DmShared.iNull(node1.getIbCode()).longValue() > 0) {
                    psTemp.setString("ibCode", DmShared.iNull(node1.getIbCode()).toString());
                }
                rsTemp = psTemp.executeQuery();

                while (rsTemp.next()) {
                    if (balanceQty != 0) {
                        node = new GrdMedicineDetail_Mdl();

                        itemMdl = new TxtHMedicineName_Mdl();
                        itemMdl.setSharedKeyFieldCode(DmShared.xNull(node1.getMedicineMdl().getSharedKeyFieldCode()));
                        itemMdl.setSharedKeyFieldName(DmShared.xNull(node1.getMedicineMdl().getSharedKeyFieldCode()));
                        itemMdl.setBestBefore(node1.getMedicineMdl().isBestBefore());
                        itemMdl.setAllowNegativeStock(node1.getMedicineMdl().isAllowNegativeStock());
                        itemMdl.setIsBarcodeActive(DmShared.iNull(node1.getIsBarcodeActive()).toString());
                        itemMdl.setPurchaseUnitConversionRate(DmServer.vNull(node1.getMedicineMdl().getPurchaseUnitConversionRate()));
                        itemMdl.setSkuDecimalPlaces(DmShared.iNull(node1.getMedicineMdl().getSkuDecimalPlaces()).intValue());
                        itemMdl.setSkuName(DmShared.xNull(node1.getMedicineMdl().getSkuName()));
                        itemMdl.setSkuCode(DmShared.xNull(node1.getMedicineMdl().getSkuCode()));
                        node.setMedicineMdl(itemMdl);

                        node.setSharedKeyFieldCode(DmShared.xNull(node1.getSharedKeyFieldCode()));
                        node.setSharedKeyFieldName(DmShared.xNull(node1.getSharedKeyFieldName()));
                        node.setSkuName(DmShared.xNull(node1.getSkuName()));
                        node.setSkuCode(DmShared.xNull(node1.getSkuCode()));
                        node.setRequisitionV_SNo(DmShared.xNull(node1.getRequisitionV_SNo()));
                        node.setRequisitionNo(DmShared.xNull(node1.getRequisitionNo()));
                        node.setRequisitionDocId(DmShared.xNull(node1.getRequisitionDocId()));
                        node.setQuantityTotalPerItem(DmShared.xNull(node1.getQuantityTotalPerItem()));
                        node.setPurchaseUnitCode(DmShared.xNull(node1.getPurchaseUnitCode()));
                        node.setPurchaseUnitName(DmShared.xNull(node1.getPurchaseUnitName()));
                        node.setSkuQtyForRate(DmShared.xNull(node1.getSkuQtyForRate()));
                        node.setIsBarcodeActive(DmShared.iNull(node1.getIsBarcodeActive()).toString());

                        if (DmShared.vNull(rsTemp.getString("stockQuantity")).doubleValue() < balanceQty) {
                            node.setQuantity(DmServer.vNull(rsTemp.getString("stockQuantity")));
                            balanceQty = balanceQty - DmShared.vNull(node.getQuantity()).doubleValue();
                        } else if (DmShared.vNull(rsTemp.getString("stockQuantity")).doubleValue() == balanceQty) {
                            node.setQuantity(DmServer.vNull(rsTemp.getString("stockQuantity")));
                            balanceQty = 0;
                        } else {
                            node.setQuantity(DmServer.vNull(balanceQty));
                            balanceQty = 0;
                        }
                        //<editor-fold defaultstate="collapsed" desc="BATCH Detail">
                        node.setIbCode(DmShared.iNull(rsTemp.getString("ibCode")).toString());
                        node.setBarCode(DmShared.xNull(rsTemp.getString("barcode")));
                        node.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                        node.setManufacturingDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));
                        node.setExpiryDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                        node.setMrp(DmServer.vNull(rsTemp.getString("mrp"), "0.00"));
                        node.setSkuMrp(DmServer.vNull(rsTemp.getString("skuMrp")));
                        node.setSaleRate(DmServer.vNull(rsTemp.getString("saleRate"), "0.00"));
                        node.setSkuSaleRate(DmServer.vNull(rsTemp.getString("skuSaleRate")));
//</editor-fold>
                        selectedIbCodeSQL += " UNION ALL SELECT " + node.getIbCode() + " AS ibCode, " + DmServer.vNull(node.getQuantity()) + " AS qty ";

                        listNode.add(node);

                    }
                }
                rsTemp.close();
                psTemp.close();

                if (balanceQty != 0) {
                    node = new GrdMedicineDetail_Mdl();

                    itemMdl = new TxtHMedicineName_Mdl();
                    itemMdl.setSharedKeyFieldCode(DmShared.xNull(node1.getMedicineMdl().getSharedKeyFieldCode()));
                    itemMdl.setSharedKeyFieldName(DmShared.xNull(node1.getMedicineMdl().getSharedKeyFieldCode()));
                    itemMdl.setBestBefore(node1.getMedicineMdl().isBestBefore());
                    itemMdl.setAllowNegativeStock(node1.getMedicineMdl().isAllowNegativeStock());
                    itemMdl.setIsBarcodeActive(DmShared.iNull(node1.getIsBarcodeActive()).toString());
                    itemMdl.setPurchaseUnitConversionRate(DmServer.vNull(node1.getMedicineMdl().getPurchaseUnitConversionRate()));
                    itemMdl.setSkuDecimalPlaces(DmShared.iNull(node1.getMedicineMdl().getSkuDecimalPlaces()).intValue());
                    itemMdl.setSkuName(DmShared.xNull(node1.getMedicineMdl().getSkuName()));
                    itemMdl.setSkuCode(DmShared.xNull(node1.getMedicineMdl().getSkuCode()));
                    node.setMedicineMdl(itemMdl);

                    node.setSharedKeyFieldCode(DmShared.xNull(node1.getSharedKeyFieldCode()));
                    node.setSharedKeyFieldName(DmShared.xNull(node1.getSharedKeyFieldName()));
                    node.setSkuName(DmShared.xNull(node1.getSkuName()));
                    node.setSkuCode(DmShared.xNull(node1.getSkuCode()));
                    node.setRequisitionV_SNo(DmShared.xNull(node1.getRequisitionV_SNo()));
                    node.setRequisitionNo(DmShared.xNull(node1.getRequisitionNo()));
                    node.setRequisitionDocId(DmShared.xNull(node1.getRequisitionDocId()));
                    node.setQuantityTotalPerItem(DmShared.xNull(node1.getQuantityTotalPerItem()));
                    node.setPurchaseUnitCode(DmShared.xNull(node1.getPurchaseUnitCode()));
                    node.setPurchaseUnitName(DmShared.xNull(node1.getPurchaseUnitName()));
                    node.setSkuQtyForRate(DmShared.xNull(node1.getSkuQtyForRate()));
                    node.setIsBarcodeActive(DmShared.iNull(node1.getIsBarcodeActive()).toString());

                    node.setQuantity(DmServer.vNull(balanceQty));
                    balanceQty = 0;

                    listNode.add(node);

                }
            }

        } catch (Exception e) {
            throw e;
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
        return listNode;
    }

    private void manageRequisitionStatus(String docId, boolean isDelete, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null, psTemp1 = null;
        ResultSet rsTemp = null;
        String sqlQuery, rqDocIds = "";
        double reqQty, issueQty, transferQty;
        Integer isStatus;

        try {

            sqlQuery = "select	req.docId as rqDocId, reqd.v_SNo AS rqV_SNo, mbd.Qty as transferQty, reqd.reqQty as reqQty, reqd.issueQty, reqd.isStatus, req.approvedBy,  "
                    + "        	req.recId as reqNo, vt.short_name as reqType  "
                    + "        	from requisitionDetail reqd  "
                    + "        	left join requisition req on req.docId = reqd.docId  "
                    + "        	left join voucher_type vt on vt.v_type = req.v_Type  "
                    + "        	inner join (  "
                    + "        			select	mbd.requisitionDocId, mbd.requisitionV_SNo, sum(mbd.quantity) as Qty  "
                    + "        			from	stockHeadDetail mbd  "
                    + "        			where mbd.docId = :docId  "
                    + "        			group by mbd.requisitionDocId, mbd.requisitionV_SNo  "
                    + "        	) mbd on reqd.docId = mbd.requisitionDocId and reqd.v_SNo = mbd.requisitionV_SNo  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                isStatus = DmShared.iNull(rsTemp.getString("isStatus")).intValue();
                if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                    throw new DmException(500, DmShared.xNull(rsTemp.getString("rqDocId")), DmShared.xNull(rsTemp.getString("rqV_SNo")), "Requisition is not Approved");
                }
                if (isStatus.intValue() == 1) {
                    throw new DmException(500, DmShared.xNull(rsTemp.getString("rqDocId")), DmShared.xNull(rsTemp.getString("rqV_SNo")), "Requisition is Cancelled");
                }
                reqQty = DmShared.vNull(rsTemp.getString("reqQty")).doubleValue();
                issueQty = DmShared.vNull(rsTemp.getString("issueQty")).doubleValue();
                transferQty = DmShared.vNull(rsTemp.getString("transferQty")).doubleValue();

                if (isDelete) {
                    issueQty -= transferQty;
                } else {
                    issueQty += transferQty;
                }

                if (issueQty > reqQty) {
                    throw new DmException(500, DmShared.xNull(rsTemp.getString("rqDocId")), DmShared.xNull(rsTemp.getString("rqV_SNo")), "Requisition overflows by " + DmServer.vNull(issueQty - reqQty));
                } else if (issueQty < 0) {
                    throw new DmException(500, DmShared.xNull(rsTemp.getString("rqDocId")), DmShared.xNull(rsTemp.getString("rqV_SNo")), "Requisition overflows by " + DmServer.vNull(issueQty));
                } else if (issueQty == reqQty) {
                    isStatus = 2;
                } else {
                    isStatus = 0;
                }

                if (!rqDocIds.equalsIgnoreCase("")) {
                    rqDocIds += ",";
                }
                rqDocIds += DmShared.iNull(rsTemp.getString("rqDocId")).toString();

                sqlQuery = "update requisitionDetail set "
                        + " isStatus = :isStatus,"
                        + " issueQty = :issueQty "
                        + " where docId = :docId and v_SNo = :v_SNo ";
                psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp1.setString("docId", DmShared.iNull(rsTemp.getString("rqDocId")).toString());
                psTemp1.setString("v_SNo", DmShared.iNull(rsTemp.getString("rqV_SNo")).toString());
                psTemp1.setString("isStatus", DmShared.iNull(isStatus).toString());
                psTemp1.setString("issueQty", DmServer.vNull(issueQty));
                psTemp1.execute();
                psTemp1.close();
            }
            rsTemp.close();
            psTemp.close();

            if (!rqDocIds.equalsIgnoreCase("")) {
                sqlQuery = " update requisition set u_EntDt_LatestLine = :uEntDt where docId in (" + rqDocIds + ") ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.executeUpdate();
                psTemp.close();
            }

        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp1 != null) {
                    psTemp1.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
    }

    @Override
    public DmResult<List<ToolTip_Mdl>> getToolTipShowStock(String itemCode, String ibCode, String fromDepartment, String toDepartment) {
        DmResult<List<ToolTip_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "";
        ToolTip_Mdl node;
        List<ToolTip_Mdl> lstTemp = new ArrayList<>();
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (DmShared.iNull(ibCode).longValue() != 0) {
                condition += " AND iStk.ibCode = :ibCode ";
            }
            /*==============================<< Get header detail >>===============================*/
            sqlQuery = "SELECT sg.Name AS departmentName "
                    + ",  vIStk.itemName "
                    + ",  vIStk.currentStock "
                    + ",  vIStk.skuName "
                    + ",  vIStk.decimalPlaces "
                    + " FROM subGroup sg   "
                    + " LEFT JOIN ("
                    + "     SELECT iStk.departmentCode"
                    + "     , MAX(im.Name) AS itemName "
                    + "     , SUM(iStk.balanceQty) AS currentStock "
                    + "     , MAX(um.unit) as skuName "
                    + "     , MAX(um.decimalPlaces) as decimalPlaces "
                    + "     FROM itemStock iStk "
                    + "     LEFT JOIN itemMast im ON im.code = iStk.itemCode "
                    + "     LEFT JOIN unitMast um ON um.code = im.sku "
                    + "     WHERE iStk.itemCode = :itemCode "
                    + "     AND iStk.site_Code = :site_Code "
                    + "     AND iStk.departmentCode IN (:fromDepartment, :toDepartment)  " + condition
                    + "     GROUP BY iStk.itemCode, iStk.departmentCode"
                    + "     ) vIStk ON sg.subCode = vIStk.departmentCode   "
                    + " WHERE sg.subCode IN (:fromDepartment, :toDepartment)  "
                    + " ORDER BY CASE WHEN sg.subCode = :fromDepartment THEN 1 WHEN sg.subCode = :toDepartment THEN 2 ELSE 99999 END ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("itemCode", DmShared.iNull(itemCode).toString());
            psTemp.setString("fromDepartment", DmShared.iNull(fromDepartment).toString());
            psTemp.setString("toDepartment", DmShared.iNull(toDepartment).toString());
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            if (DmShared.iNull(ibCode).longValue() != 0) {
                psTemp.setString("ibCode", DmShared.iNull(ibCode).toString());
            }
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new ToolTip_Mdl();
                node.setCaption(DmShared.xNull(rsTemp.getString("departmentName")));

                int decimalPlaces = DmShared.iNull(rsTemp.getString("decimalPlaces")).intValue();
                node.setValue(DmJasper.formatAmount(BigDecimal.valueOf(DmShared.vNull(rsTemp.getString("currentStock")).doubleValue()), decimalPlaces, false) + " " + DmShared.xNull(rsTemp.getString("skuName")));

                lstTemp.add(node);
            }

            rsTemp.close();
            psTemp.close();
            dmrTemp.setData(lstTemp);
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

            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

    @Override
    public DmResult<BarCode_Mdl> getBarCodeSearch(String barCode, String voucherDate, String departmentCode, String siteCode) {
        DmResult<BarCode_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            dmrTemp.setData(Pharmacy_Server.barCodeSearch(barCode, voucherDate, departmentCode, siteCode, ItemBatchType.ALL_BATCH, false, cnTemp, null));
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
    
}
