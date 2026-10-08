package aarogya.pharmacy.medicineIssue.server;

import aarogya.pharmacy.medicineIssue.shared.*;
import aarogya.pharmacy.self.server.Pharmacy_Server;
import aarogya.pharmacy.self.shared.BarCode_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.itemBatch.server.ItemBatch_Server;
import aarogya.selfToolkit.modifyReason.server.ModifyReason_Server;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import aarogya.selfToolkit.stStatic.shared.STEnum.ItemBatchType;
import aarogya.selfToolkit.toolTip.shared.ToolTip_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmToolkit.accountPosting.server.AccountPosting_Server;
import dataman.dmToolkit.accountPosting.shared.AccountPosting_Mdl;
import dataman.dmToolkit.dmJasper.server.DmJasper;
import dataman.dmToolkit.stock.server.Stock_Server;
import dataman.dmToolkit.stock.shared.Stock_Mdl;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MedicineIssue_SrvImpl extends DmRSSPrivate implements MedicineIssue_Srv {

    private String vCat = new ProjectVoucherCategory().MEDICINE_ISSUE;
    private final String voucherTypefieldName = "sh.V_Type", voucherDateFieldName = "sh.V_Date";

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission) {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);
        String sqlQuery = "", sqlCondition = "";
        DmPreparedStatement psTemp = null;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " select sh.docId "
                    + "         from stockHead as sh "
                    + "         left join voucher_Type as vt on sh.v_Type = vt.v_Type  "
                    + "         where vt.category = '" + vCat + "' and sh.site_Code = :site_Code and sh.v_prefix = :vPrefix ";

            sqlCondition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypefieldName, userPermission);
            if (!sqlCondition.equalsIgnoreCase("")) {
                sqlQuery += "and " + sqlCondition;
            }

            sqlQuery += " order by sh.v_Date desc, vt.short_Name, sh.v_No desc, sh.recId  ";
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
    public DmResult<MedicineIssue_Mdl> getMoveRec(String strKey) {
        DmResult<MedicineIssue_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        MedicineIssue_Mdl mrTemp = new MedicineIssue_Mdl();
        dmrTemp.setData(mrTemp);
        List<GrdMedicineDetail_Mdl> lstNode = new ArrayList<GrdMedicineDetail_Mdl>();
        GrdMedicineDetail_Mdl node;
        String itemCsv = "";

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*==============================<< Get header detail >>===============================*/
            sqlQuery = "select	sh.docId, sh.recId, sh.v_No, sh.recIdPrefix, sh.v_Date, sh.u_Name, sh.preparedBy,"
                    + "  sh.v_Type, vt.description,sh.refNo,sh.refDate,sh.remark,  "
                    + "		st.departmentCode, sgd.name as departmentName, st.subCode as receivedByCode, "
                    + "		sgrb.name receivedByName, sh.isExpiredItem "
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
                mrTemp.getRecIdMedicineIssue().setRecIdValue(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.getRecIdMedicineIssue().setCounter(DmShared.iNull(rsTemp.getString("v_No")).longValue());
                mrTemp.getRecIdMedicineIssue().setPrefix(DmShared.xNull(rsTemp.getString("recIdPrefix")));
                mrTemp.setVoucherDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                mrTemp.setVoucherTypeCode(DmShared.xNull(rsTemp.getString("v_Type")));
                mrTemp.setVoucherTypeName(DmShared.xNull(rsTemp.getString("description")));
                mrTemp.setDepartmentTag(DmShared.xNull(rsTemp.getString("departmentCode")));
                mrTemp.setDepartmentText(DmShared.xNull(rsTemp.getString("departmentName")));
                mrTemp.setIssueToTag(DmShared.xNull(rsTemp.getString("receivedByCode")));
                mrTemp.setIssueToText(DmShared.xNull(rsTemp.getString("receivedByName")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setIsExpiryMedicine(DmShared.xNull(rsTemp.getString("isExpiredItem")));

                mrTemp.setRefDate(DmShared.dNull(rsTemp.getDate("refDate")));
                mrTemp.setRefNo(DmShared.xNull(rsTemp.getString("refNo")));
                mrTemp.setRemarkHeader(DmShared.xNull(rsTemp.getString("remark")));
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Get Item grid detail >>===============================*/
            sqlQuery = " Select	shd.itemCode, im.name as itemName, um.unit as skuName,im.sku as skuCode, shd.skuValue,shd.quantity , "
                    + " shd.remark, shd.ibCode, shd.batchNo, shd.expiryDate, shd.manufacturingDate, shd.mrp, im.propertyList ,  "
                    + " shd.saleRate,shd.skuMrp,shd.skuSaleRate, "
                    + " um.decimalPlaces,im.isBestBefore,shd.requisitionDocId,shd.requisitionV_SNo ,im.purchaseUnitConversionRate,"
                    + " mrq.recId as reqNo, shd.v_SNo,im.purchaseUnit as purchaseUnitCode,ump.unit as purchaseUnitName"
                    + ",im.allowNegativeStock, im.isBarcodeActive, mrq.departmentCode as requisitionDepartmentCode, shd.barCode "
                    + ",shd.oh_Ac_Item as inventoryAc,shd.GrIr_ConsumptionAc as consumptionAc,stk.grossPurchaseRate "
                    + " from stockHeadDetail  shd  "
                    + " left join itemMast as im on shd.itemCode = im.code  "
                    + " left join unitMast as um on im.sku = um.code  "
                    + " left join unitMast as ump on im.purchaseUnit = ump.code  "
                    + " LEFT JOIN requisition mrq ON mrq.docId = shd.requisitionDocId "
                    + " left join stock stk on stk.docid=shd.docid and stk.v_sno=shd.v_Sno "
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
                node.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                node.setQuantity(DmServer.vNull(rsTemp.getString("quantity")));
                node.setPropertyList(DmShared.xNull(rsTemp.getString("propertyList")));
                node.setIbCode(DmShared.xNull(rsTemp.getString("ibCode")));
                node.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                node.setManufacturingDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));
                node.setExpiryDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                node.setIsBestBefore(DmShared.xNull(rsTemp.getString("isBestBefore")));
                node.setMrp(DmServer.vNull(rsTemp.getString("mrp"), "0.00"));
                node.setSaleRate(DmServer.vNull(rsTemp.getString("saleRate"), "0.00"));
                node.setSkuMrp(DmServer.vNull(rsTemp.getString("skuMrp"), DmShared.getNumberFormat(7)));
                node.setSkuSaleRate(DmServer.vNull(rsTemp.getString("skuSaleRate"), DmShared.getNumberFormat(7)));
                node.setSkuQtyForRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));
                node.setPurchaseUnitCode(DmShared.xNull(rsTemp.getString("purchaseUnitCode")));
                node.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                node.setRequisitionDocId(DmShared.xNull(rsTemp.getString("requisitionDocId")));
                node.setRequisitionNo(DmShared.xNull(rsTemp.getString("reqNo")));
                node.setRequisitionV_SNo(DmShared.xNull(rsTemp.getString("requisitionV_SNo")));
                node.setAllowNegativeStock(DmShared.iNull(rsTemp.getString("allowNegativeStock")).intValue() == 1);
                node.setBarcodeActive(DmShared.iNull(rsTemp.getString("isBarcodeActive")).intValue() == 1);

                node.setInventoryAc(DmShared.xNull(rsTemp.getString("inventoryAc")));
                node.setConsumptionAc(DmShared.xNull(rsTemp.getString("consumptionAc")));
                node.setPurchaseValue(DmShared.xNull(rsTemp.getString("grossPurchaseRate")));

                if (!DmShared.xNull(rsTemp.getString("reqNo")).equalsIgnoreCase("") && DmShared.xNull(mrTemp.getRequisitionDocId()).trim().equals("")) {
                    mrTemp.setRequisitionDocId(DmShared.xNull(rsTemp.getString("requisitionDocId")));
                    mrTemp.setRequisitionNo(DmShared.xNull(rsTemp.getString("reqNo")));
                    mrTemp.setDepartmentToTag(DmShared.foriegnKey(rsTemp.getString("requisitionDepartmentCode")));
                }

                node.setBarCode(DmShared.xNull(rsTemp.getString("barCode")));

                ItemBatchType itemBatchType = null;
                if (DmShared.iNull(mrTemp.getIsExpiryMedicine()).intValue() == 1) {
                    itemBatchType = ItemBatchType.EXPIRED_BATCH;
                } else {
                    itemBatchType = ItemBatchType.NON_EXPIRED_BATCH;
                }
                itemCsv = Pharmacy_Server.getItemCodeCsv(node.getBarCode(), mrTemp.getVoucherDate(), mrTemp.getDepartmentTag(), DmServer.getSsnSiteCode(this), itemBatchType, false, true, cnTemp);
                node.setItemCodeCsv(itemCsv);

                lstNode.add(node);
            }
            rsTemp.close();
            psTemp.close();

            mrTemp.getListMedicineDetail().addAll(lstNode);

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
            /*==============================<< Delete Ledger Posting >>====================================*/
            AccountPosting_Server.deleteAccountPosting(keyCode, cnTemp);

            /*==============================<< Delete record from stock >>===============================*/
            Stock_Server.deleteStock(keyCode, cnTemp);

            manageRequisitionStatus(keyCode, true, cnTemp);

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
                if (e instanceof DmException) {
                    dmrTemp.setErrorNumber(((DmException) e).getErrorNo());
                    dmrTemp.setValue1(((DmException) e).getErrorSource());
                    dmrTemp.setValue2(((DmException) e).getErrorSourceCode());
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
    public DmResult<String> saveRecord(MedicineIssue_Mdl mrTemp, MedicineIssue_Mdl mrTempPrevious, String voucherPrefix) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery, userName, siteCode;
        ResultSet rsTemp = null;
        dmrTemp.setData("");

        try {
            MedicineIssue_Vld.chkSave(mrTemp);
            userName = DmServer.getSsnUserName(this);
            siteCode = DmServer.getSsnSiteCode(this);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            DmServer.checkLockDate(DmServer.getSsnSiteCode(this), DmServer.getSsnVoucherPrefix(this), cnTemp);
            DmServer.checkForwordVoucherDateTime(mrTemp.getVoucherDate(), "");
            if (mrTemp.isInventoryAccountingActive()) {
                DmServer.checkBackwordVoucherDateTime("stock", "v_Date", "", mrTemp.getVoucherDate(), "", cnTemp);
            }

            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                /*==============================<< Generate docId for new record >>====================================*/
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this));
                mrTemp.setRecIdMedicineIssue(DmServer.getRecId("stockHead", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecIdMedicineIssue(), mrTemp.getVoucherDate(), DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this, true));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(mrTemp.getRecIdMedicineIssue().getRecIdValue()));
            } else {
                DmServer.getRecId("stockHead", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecIdMedicineIssue(), mrTemp.getVoucherDate(), DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this, true);
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            /*==============================<< Check items for restricted department >>====================================*/
            STServer.checkItemsRestrictedDepartment(mrTemp.getLineItemCodesCsv(), mrTemp.getDepartmentTag(), mrTemp.getDepartmentText(), cnTemp);

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


            /*==============================<< Insert / Update record into stockHead >>====================================*/
            if (blnAdd) {
                sqlQuery = "Insert Into stockHead (docId, v_Type, v_No, v_Date, v_Prefix, recId,refNo,refDate,remark, "
                        + " site_Code, transferDt, preparedBy, u_EntDt,recIdPrefix,isExpiredItem) "
                        + " values (:docId, :v_Type, :v_No, :v_Date,  :v_Prefix, :recId,:refNo,:refDate,:remark, "
                        + " :site_Code, :transferDt, :preparedBy, :uEntDt,:recIdPrefix,:isExpiredItem) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("recId", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("v_Type", DmShared.foriegnKey(mrTemp.getVoucherTypeCode()));
                psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setString("v_Prefix", voucherPrefix);
                psTemp.setString("v_No", DmShared.iNull(mrTemp.getRecIdMedicineIssue().getCounter()).toString());
                psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getRecIdMedicineIssue().getPrefix()));
                psTemp.setString("site_Code", DmShared.foriegnKey(siteCode));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("transferDt", null);
                psTemp.setString("preparedBy", userName);
                psTemp.setString("refNo", DmShared.xNull(mrTemp.getRefNo()));
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemarkHeader()));
                psTemp.setString("isExpiredItem", DmShared.iNull(mrTemp.getIsExpiryMedicine()).toString());
                psTemp.setDate("refDate", DmServer.convertDate(mrTemp.getRefDate()));
            } else {
                sqlQuery = " update stockHead Set "
                        + " v_Date = :v_Date, "
                        + " isExpiredItem = :isExpiredItem, "
                        + " transferDt = :transferDt, "
                        + " u_Name = :u_Name, "
                        + " refNo=:refNo,"
                        + " refDate=:refDate, "
                        + " remark=:remark, "
                        + " u_EntDt = :uEntDt "
                        + " Where docId = :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setString("transferDt", null);
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("u_Name", userName);
                psTemp.setString("refNo", DmShared.xNull(mrTemp.getRefNo()));
                psTemp.setDate("refDate", DmServer.convertDate(mrTemp.getRefDate()));
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemarkHeader()));
                psTemp.setString("isExpiredItem", DmShared.iNull(mrTemp.getIsExpiryMedicine()).toString());
            }
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Delete records from stock >>====================================*/
            Stock_Server.deleteStock(DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString(), cnTemp, true);

            /*==============================<< Manage Requisition Status >>====================================*/
            manageRequisitionStatus(mrTemp.getSharedKeyFieldCode(), true, cnTemp);

            /*==============================<< Delete records from stockHeadDetail >>====================================*/
            sqlQuery = "Delete From stockHeadDetail Where docId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Insert records into stockHeadDetail & stockHeadBatchDetail >>====================================*/
            int v_SNo = 0;
            List<GrdMedicineDetail_Mdl> listDetail = mrTemp.getListMedicineDetail();
            for (GrdMedicineDetail_Mdl detail_Mdl : listDetail) {
                if (!DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                    detail_Mdl.setV_SNo(String.valueOf(++v_SNo));
                    sqlQuery = " insert into stockHeadDetail(docId, v_SNo, departmentCode,"
                            + " itemCode, quantity, remark,subCode,site_Code ,requisitionDocId,requisitionV_SNo,"
                            + " ibCode, batchNo, manufacturingDate,expiryDate,mrp,saleRate,skuMrp,skuSaleRate, barCode,"
                            + " oh_Ac_Item,GrIr_ConsumptionAc)"
                            + " values (:docId, :v_SNo, :departmentCode,"
                            + " :itemCode, :quantity, :remark,:subCode,:site_Code ,:requisitionDocId,:requisitionV_SNo,"
                            + " :ibCode,:batchNo, :manufacturingDate,:expiryDate,:mrp,:saleRate,:skuMrp,:skuSaleRate, "
                            + " :barCode,:oh_Ac_Item,:GrIr_ConsumptionAc) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                    psTemp.setString("v_SNo", DmShared.iNull(detail_Mdl.getV_SNo()).toString());
                    psTemp.setString("departmentCode", DmShared.foriegnKey(mrTemp.getDepartmentTag()));
                    psTemp.setString("itemCode", DmShared.foriegnKey(detail_Mdl.getSharedKeyFieldCode()));
                    psTemp.setString("quantity", DmServer.vNull(detail_Mdl.getQuantity()));
                    psTemp.setString("remark", DmShared.xNull(detail_Mdl.getRemark()));
                    psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getIssueToTag()));
                    psTemp.setString("site_Code", DmShared.foriegnKey(siteCode));
                    psTemp.setString("requisitionDocId", DmShared.foriegnKey(detail_Mdl.getRequisitionDocId()));
                    psTemp.setString("requisitionV_SNo", DmShared.foriegnKey(detail_Mdl.getRequisitionV_SNo()));

                    psTemp.setString("ibCode", DmShared.foriegnKey(detail_Mdl.getIbCode()));
                    psTemp.setString("batchNo", DmShared.foriegnKey(detail_Mdl.getBatchNo()));
                    psTemp.setDate("manufacturingDate", DmServer.convertDate(detail_Mdl.getManufacturingDate()));
                    psTemp.setDate("expiryDate", DmServer.convertDate(detail_Mdl.getExpiryDate()));
                    psTemp.setString("mrp", DmServer.vNull(detail_Mdl.getMrp()));
                    psTemp.setString("saleRate", DmServer.vNull(detail_Mdl.getSaleRate()));
                    psTemp.setString("skuMrp", DmServer.vNull(detail_Mdl.getSkuMrp()));
                    psTemp.setString("skuSaleRate", DmServer.vNull(detail_Mdl.getSkuSaleRate()));
                    psTemp.setString("barCode", DmShared.xNull(detail_Mdl.getBarCode()));
                    psTemp.setString("oh_Ac_Item", DmShared.foriegnKey(detail_Mdl.getInventoryAc()));
                    psTemp.setString("GrIr_ConsumptionAc", DmShared.foriegnKey(detail_Mdl.getConsumptionAc()));
                    psTemp.executeUpdate();
                    psTemp.close();

                    /*----------------------------------------------------------Stock Saving--------------------------------------------------------------*/
                    Stock_Mdl stock_Mdl = new Stock_Mdl();
                    stock_Mdl.setSharedKeyFieldCode(DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                    stock_Mdl.setSharedKeyFieldName(detail_Mdl.getV_SNo());
                    stock_Mdl.setSite_Code(DmShared.xNull(siteCode));
                    stock_Mdl.setV_Type(DmShared.xNull(mrTemp.getVoucherTypeCode()));
                    stock_Mdl.setV_No(DmShared.iNull(mrTemp.getRecIdMedicineIssue().getCounter()).toString());
                    stock_Mdl.setRecId(DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                    stock_Mdl.setRecIdPrefix(DmShared.xNull(mrTemp.getRecIdMedicineIssue().getPrefix()));
                    stock_Mdl.setV_Date(DmShared.dNull(mrTemp.getVoucherDate()));
                    stock_Mdl.setRefChallanNo(DmShared.xNull(mrTemp.getRefNo()));
                    stock_Mdl.setRefChallanDate(DmShared.dNull(mrTemp.getRefDate()));
                    stock_Mdl.setDepartmentCode(DmShared.xNull(mrTemp.getDepartmentTag()));
                    stock_Mdl.setStockContraSubCode(DmShared.xNull(mrTemp.getIssueToTag()));
                    stock_Mdl.setRemark(DmShared.xNull(mrTemp.getRemarkHeader()) + "\n" + DmShared.xNull(detail_Mdl.getRemark()));

                    stock_Mdl.setItemCode(DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()));
                    stock_Mdl.setIbCode(detail_Mdl.getIbCode());
                    stock_Mdl.setIsuQty(DmServer.vNull(detail_Mdl.getQuantity()));

                    Stock_Server.saveStock(stock_Mdl, DmEnum.StockEntryType.OTHER, cnTemp);

                    sqlQuery = "UPDATE itemDepartmentInfo SET isHoldRequirement = 0 "
                            + " WHERE itemCode = " + DmShared.xNull(detail_Mdl.getSharedKeyFieldCode())
                            + " AND departmentCode = " + DmShared.xNull(mrTemp.getDepartmentTag())
                            + " AND site_Code = " + DmShared.xNull(DmServer.getSsnSiteCode(this))
                            + " AND ISNULL(isHoldRequirement,0) <> 0 ";
                    DmServer.executeSQL(sqlQuery, cnTemp);

                }
            }

            /*==============================<< Account Posting >>====================================*/
            if (mrTemp.isInventoryAccountingActive()) {
                AccountPosting_Server.deleteAccountPosting(mrTemp.getSharedKeyFieldCode(), cnTemp);
                AccountPosting_Server.saveAccountPosting(iniAcPostList(mrTemp.getSharedKeyFieldCode(), cnTemp), "docId", mrTemp.getSharedKeyFieldCode(), mrTemp.getRecIdMedicineIssue(), mrTemp.getVoucherTypeCode(), voucherPrefix, siteCode, mrTemp.getVoucherDate(), "", "", "", "", "", true, true, cnTemp);
            }
            /*==============================<< ************** >>====================================*/
 /*==============================<< Manage Requisition Status >>====================================*/
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
                if (e instanceof DmException) {
                    dmrTemp.setErrorNumber(((DmException) e).getErrorNo());
                    dmrTemp.setValue1(((DmException) e).getErrorSource());
                    dmrTemp.setValue2(((DmException) e).getErrorSourceCode());
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
                    + "		where vt.category = '" + vCat + "' and sh.site_Code = :site_Code and sh.v_prefix = :vPrefix";
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
                listNode.setIssueTo(DmShared.xNull(rsTemp.getString("receivedByName")));
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
            sqlQuery = " select v_Type as code, description as name from voucher_Type where category = '" + vCat + "' and isActive = 1 ";
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
                    + " env.expiryAlertBeforeDays, env.pharmacyBarcodeActive  "
                    + " FROM enviro as env "
                    + " Where env.site_Code = :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmShared.xNull(DmServer.getSsnSiteCode(this)));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setExpiryAlertBeforeDays(DmShared.xNull(rsTemp.getString("expiryAlertBeforeDays")));
                defaultValue.setBarcodeActive(DmShared.iNull(rsTemp.getString("pharmacyBarcodeActive")).toString());
            }
            psTemp.close();
            rsTemp.close();

            /*==============================<< Get Enviro Company Data >>====================================*/
            sqlQuery = "SELECT ec.isInventoryAccountingActive \n"
                    + "FROM enviroCompany ec\n";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getString("isInventoryAccountingActive") == null) {
                    throw new DmException(0, "Inventory Accounting Active is not defined in Environment Setting (Company).");
                }
                defaultValue.setInventoryAccountingActive(DmShared.iNull(rsTemp.getString("isInventoryAccountingActive")).intValue() == 1);
            }
            rsTemp.close();
            psTemp.close();
            /*==============================<< **************************** >>====================================*/

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
    public DmResultPageing<List<TxtHRequisitionNo_Mdl>> getTxtHRequisitionNo(String departmentCode, String voucherDate, String issueDocId, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHRequisitionNo_Mdl>> rstTemp = new DmResultPageing<List<TxtHRequisitionNo_Mdl>>();
        List<TxtHRequisitionNo_Mdl> lstTemp = new ArrayList<TxtHRequisitionNo_Mdl>();
        TxtHRequisitionNo_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, condition = "", joinItemIssue = "", conditionItemIssue = "";
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            if (DmShared.dNull(voucherDate).trim().equalsIgnoreCase("")) {
                throw new DmException(0, "Voucher date is mandatory.");
            }
            if (DmShared.iNull(departmentCode).longValue() > 0) {
                condition += " and mrq.departmentToCode = :departmentCode ";
            }

            if (DmShared.iNull(issueDocId).longValue() != 0) {
                joinItemIssue = " LEFT JOIN stockHeadDetail sd ON sd.requisitionDocId = rd.docId AND sd.requisitionV_SNo = rd.v_SNo";
                conditionItemIssue += " WHERE CASE WHEN rd.isStatus = 0 AND ISNULL(rd.reqQty,0) - ISNULL(rd.issueQty,0) > 0 THEN 1 ELSE CASE WHEN rd.isStatus = 2 AND ISNULL(sd.docid,0) = :issueDocId  THEN 1 ELSE 0 END  END = 1 ";
            } else {
                conditionItemIssue += " WHERE rd.isStatus = 0 AND ISNULL(rd.reqQty,0) - ISNULL(rd.issueQty,0) > 0 ";
            }

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = " select mrq.docid, mrq.recid, mrq.departmentCode, dm.name as department, rbc.name as requestedByName,mrq.requestedByCode, vt.short_Name,"
                    + "         mrq.v_No,  mrq.v_Date, mrq.departmentToCode, dm1.name departmentTo "
                    + "	FROM requisition mrq "
                    + "	INNER JOIN ("
                    + "     SELECT rd.docId"
                    + "     FROM requisitionDetail rd" + joinItemIssue + conditionItemIssue
                    + "     GROUP BY rd.docId"
                    + "	) mrqd on mrqd.docId = mrq.docid"
                    + "	left join voucher_Type vt on vt.v_Type = mrq.v_Type "
                    + "	left join subGroup dm on dm.subCode = mrq.departmentCode "
                    + "	left join subGroup dm1 on dm1.subCode = mrq.departmentToCode "
                    + "	left join subGroup rbc on rbc.subCode =	mrq.requestedByCode "
                    + "	where vt.category in( " + getVoucherCategory(new ProjectVoucherCategory().PHARMACY_REQUISITION) + "," + getVoucherCategory(new ProjectVoucherCategory().NURSING_MEDICINE_REQUISTION) + ") "
                    + " and ISNULL(mrq.approvedBy,'') <> '' "
                    + " and mrq.site_Code_To = :siteCode  "
                    + " and mrq.v_Date <= :voucherDate " + condition;
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            sqlWithParam.setDate("voucherDate", DmServer.convertDate(voucherDate));
            if (DmShared.iNull(departmentCode).longValue() > 0) {
                sqlWithParam.setString("departmentCode", DmShared.iNull(departmentCode).toString());
            }
            if (DmShared.iNull(issueDocId).longValue() != 0) {
                sqlWithParam.setString("issueDocId", DmShared.iNull(issueDocId).toString());
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHRequisitionNo_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docid")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recid")));
                listNode.setDepartmentCode(DmShared.xNull(rsTemp.getString("departmentCode")));
                listNode.setDepartmentName(DmShared.xNull(rsTemp.getString("department")));
                listNode.setDepartmentToCode(DmShared.xNull(rsTemp.getString("departmentToCode")));
                listNode.setDepartmentToName(DmShared.xNull(rsTemp.getString("departmentTo")));
                listNode.setRequisitionBy(DmShared.xNull(rsTemp.getString("requestedByName")));
                listNode.setRequisitionByCode(DmShared.xNull(rsTemp.getString("requestedByCode")));
                listNode.setRequisitionDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());

            rstTemp.setStatus(true);
            rstTemp.setData(lstTemp);
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
    public DmResultPageing<List<TxtHIssueTo_Mdl>> getTxtHIssueTo(String v_Type, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHIssueTo_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHIssueTo_Mdl> lstTemp = new ArrayList<>();
        TxtHIssueTo_Mdl listNode;
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
                    + " AND sg.isActive = 1 "
                    + " AND sgsl.site_Code = :site_Code ";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("v_Type", DmShared.iNull(v_Type).toString());
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHIssueTo_Mdl();
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

    @Override
    public DmResult<List<GrdMedicineDetail_Mdl>> getMedicineDetail(String reqDocId, String MedicineIssueDocId, String departmentCode, String v_Date) {
        DmResult<List<GrdMedicineDetail_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, condition = "", recQty = "";

        List<GrdMedicineDetail_Mdl> lstTemp = new ArrayList<>(), lstBatch = new ArrayList<>();
        GrdMedicineDetail_Mdl node;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(MedicineIssueDocId).equalsIgnoreCase("")) {
                recQty = " ISNULL(mrqd.reqQty,0) - ISNULL(mrqd.issueQty,0) + ISNULL(sd.quantity,0) AS reqQty, ";
                condition += " AND (CASE WHEN (mrqd.isStatus = 0) THEN 1 WHEN (mrqd.isStatus = 2 And ISNULL(mrqd.reqQty,0) - ISNULL(mrqd.issueQty,0) + ISNULL(sd.quantity,0) > 0) THEN 1 ELSE 0 END) = 1 ";
            } else {
                recQty = " ISNULL(mrqd.reqQty,0) - ISNULL(mrqd.issueQty,0) AS reqQty, ";
                condition += " And ISNULL(mrqd.reqQty,0) - ISNULL(mrqd.issueQty,0) > 0 ";
                condition += " and mrqd.isStatus=0 ";
            }

            /*==============================<< Get Item detail (via list) >>===============================*/
            sqlQuery = " select mrq.docid,mrq.recid, mrqd.v_SNo, " + recQty
                    + "         mrq.requestedByCode "
                    + "         ,sg.name as requestedByName "
                    + "         ,mrqd.itemCode "
                    + "         ,im.name as itemName "
                    + "		,im.isBestBefore "
                    + "         ,im.allowNegativeStock "
                    + "         ,im.isBarcodeActive "
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
                    + "	from requisition mrq "
                    + "	left Join requisitionDetail as mrqd on mrqd.docId = mrq.docid  "
                    + "	left join subGroup sg on sg.subCode = mrq.requestedByCode "
                    + "	left join voucher_Type vt on vt.v_Type = mrq.v_Type "
                    + "	left join itemMast im on im.code = mrqd.itemCode "
                    + " left join itemGroup ig on ig.code = im.itemGroup "
                    + "	left join unitMast um ON um.code = im.sku "
                    + "	left join unitMast ump ON ump.code = im.purchaseUnit "
                    + "	left join itemColourMast icm ON icm.code = mrqd.colourCode "
                    + "	left join itemSizeMast ism ON ism.code = mrqd.sizeCode "
                    + "	left join hsnMast hm on hm.code = im.hsnCode ";

            if (!DmShared.xNull(MedicineIssueDocId).equalsIgnoreCase("")) {
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
            if (!DmShared.xNull(MedicineIssueDocId).equalsIgnoreCase("")) {
                psTemp.setString("itemIssueDocId", MedicineIssueDocId);
            }
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {

                node = new GrdMedicineDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                node.setRequisitionV_SNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                node.setQuantity(DmServer.vNull(rsTemp.getString("reqQty")));
                node.setQuantityTotalPerItem(DmServer.vNull(rsTemp.getString("reqQty")));
                node.setIsBestBefore(DmShared.xNull(rsTemp.getString("isBestBefore")));
                node.setAllowNegativeStock(DmShared.iNull(rsTemp.getString("allowNegativeStock")).intValue() == 1);
                node.setBarcodeActive(DmShared.iNull(rsTemp.getString("isBarcodeActive")).intValue() == 1);

                node.setPropertyList(DmShared.xNull(rsTemp.getString("propertyList")));
                node.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                node.setSkuCode(DmShared.xNull(rsTemp.getString("skuCode")));
                node.setPurchaseUnitCode(DmShared.xNull(rsTemp.getString("purchaseUnitCode")));
                node.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                node.setSkuDecimalPlaces(DmServer.vNull(rsTemp.getString("skuDecimalPlaces")));
                node.setSkuQtyForRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));
                node.setRequisitionDocId(DmShared.xNull(rsTemp.getString("docid")));
                node.setRequisitionNo(DmShared.xNull(rsTemp.getString("recid")));
                dmrTemp.setValue1(DmShared.xNull(rsTemp.getString("requestedByCode")));
                dmrTemp.setValue2(DmShared.xNull(rsTemp.getString("requestedByName")));
                lstBatch.add(node);
            }
            rsTemp.close();
            psTemp.close();

            lstTemp.clear();
            lstTemp.addAll(fillBatchFIFO(lstBatch, v_Date, departmentCode, cnTemp));

            cnTemp.close();
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
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;

    }

    private List<GrdMedicineDetail_Mdl> fillBatchFIFO(List<GrdMedicineDetail_Mdl> lstNode, String v_Date, String departmentCode, Connection cnTemp) throws Exception {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        List<GrdMedicineDetail_Mdl> listNode = new ArrayList<>();
        GrdMedicineDetail_Mdl node;
        double balanceQty = 0.0;
        String selectedIbCodeSQL = "";

        try {

            selectedIbCodeSQL = " SELECT 0 AS ibCode, 0 AS qty ";

            for (GrdMedicineDetail_Mdl node1 : lstNode) {

                balanceQty = DmShared.vNull(node1.getQuantityTotalPerItem()).doubleValue();

                sqlQuery = " SELECT (ISNULL(ibs.balanceQty,0) - ISNULL(ibt.qty,0)) as stockQuantity "
                        + " ,ib.code as ibCode"
                        + " ,ib.batchNo "
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
                        node.setSharedKeyFieldCode(DmShared.xNull(node1.getSharedKeyFieldCode()));//rsTemp
                        node.setSharedKeyFieldName(DmShared.xNull(node1.getSharedKeyFieldName()));//rsTemp
                        node.setSkuName(DmShared.xNull(node1.getSkuName()));
                        node.setSkuCode(DmShared.xNull(node1.getSkuCode()));
                        node.setPropertyList(DmShared.xNull(node1.getPropertyList()));
                        node.setRequisitionV_SNo(DmShared.xNull(node1.getRequisitionV_SNo()));
                        node.setRequisitionNo(DmShared.xNull(node1.getRequisitionNo()));
                        node.setRequisitionDocId(DmShared.xNull(node1.getRequisitionDocId()));
                        node.setQuantityTotalPerItem(DmShared.xNull(node1.getQuantityTotalPerItem()));
                        node.setIsBestBefore(DmShared.xNull(node1.getIsBestBefore()));
                        node.setAllowNegativeStock(node1.isAllowNegativeStock());
                        node.setBarcodeActive(node1.isBarcodeActive());
                        node.setPurchaseUnitCode(DmShared.xNull(node1.getPurchaseUnitCode()));//rsTemp
                        node.setPurchaseUnitName(DmShared.xNull(node1.getPurchaseUnitName()));//rsTemp
                        node.setSkuQtyForRate(DmShared.xNull(node1.getSkuQtyForRate()));//rsTemp
                        node.setSkuDecimalPlaces(DmShared.xNull(node1.getSkuDecimalPlaces()));//rsTemp

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
                    node.setSharedKeyFieldCode(DmShared.xNull(node1.getSharedKeyFieldCode()));//rsTemp
                    node.setSharedKeyFieldName(DmShared.xNull(node1.getSharedKeyFieldName()));//rsTemp
                    node.setSkuName(DmShared.xNull(node1.getSkuName()));
                    node.setSkuCode(DmShared.xNull(node1.getSkuCode()));
                    node.setPropertyList(DmShared.xNull(node1.getPropertyList()));
                    node.setRequisitionV_SNo(DmShared.xNull(node1.getRequisitionV_SNo()));
                    node.setRequisitionNo(DmShared.xNull(node1.getRequisitionNo()));
                    node.setRequisitionDocId(DmShared.xNull(node1.getRequisitionDocId()));
                    node.setQuantityTotalPerItem(DmShared.xNull(node1.getQuantityTotalPerItem()));
                    node.setIsBestBefore(DmShared.xNull(node1.getIsBestBefore()));
                    node.setAllowNegativeStock(node1.isAllowNegativeStock());
                    node.setBarcodeActive(node1.isBarcodeActive());
                    node.setPurchaseUnitCode(DmShared.xNull(node1.getPurchaseUnitCode()));//rsTemp
                    node.setPurchaseUnitName(DmShared.xNull(node1.getPurchaseUnitName()));//rsTemp
                    node.setSkuQtyForRate(DmShared.xNull(node1.getSkuQtyForRate()));//rsTemp
                    node.setSkuDecimalPlaces(DmShared.xNull(node1.getSkuDecimalPlaces()));//rsTemp

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

    private String getVoucherCategory(String voucherCategory) {
        String returnValue = "'" + voucherCategory + "'";
        return returnValue;
    }

    private void manageRequisitionStatus(String docId, boolean isDelete, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null, psTemp1 = null;
        ResultSet rsTemp = null;
        String sqlQuery, rqDocIds = "";
        double reqQty, issueQty, billQty;
        Integer isStatus;

        try {

            sqlQuery = "select	req.docId as rqDocId, reqd.v_SNo AS rqV_SNo, mbd.Qty as billQty, reqd.reqQty as reqQty, reqd.issueQty, reqd.isStatus, req.approvedBy,  "
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
                billQty = DmShared.vNull(rsTemp.getString("billQty")).doubleValue();

                if (isDelete) {
                    issueQty -= billQty;
                } else {
                    issueQty += billQty;
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
    public DmResult<BarCode_Mdl> getBarCodeSearch(String barCode, String voucherDate, String departmentCode, String siteCode, boolean isExpiryMedicine) {
        DmResult<BarCode_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            ItemBatchType itemBatchType = null;
            if (isExpiryMedicine) {
                itemBatchType = ItemBatchType.EXPIRED_BATCH;
            } else {
                itemBatchType = ItemBatchType.NON_EXPIRED_BATCH;
            }

            dmrTemp.setData(Pharmacy_Server.barCodeSearch(barCode, voucherDate, departmentCode, siteCode, itemBatchType, false, cnTemp, null));
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
    public String[] getServerDateTime() {
        return DmServer.getServerDateTime();
    }

    @Override
    public DmResultPageing<List<TxtHRequisitionNo_Mdl>> getRequisitionNoDetail(String requisitionDocID) {
        DmResultPageing<List<TxtHRequisitionNo_Mdl>> rstTemp = new DmResultPageing<List<TxtHRequisitionNo_Mdl>>();
        List<TxtHRequisitionNo_Mdl> lstTemp = new ArrayList<TxtHRequisitionNo_Mdl>();
        TxtHRequisitionNo_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, joinItemIssue = "", conditionItemIssue = "";
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = " select mrq.docid, mrq.recid, mrq.departmentCode, dm.name as department, rbc.name as requestedByName,mrq.requestedByCode, vt.short_Name,"
                    + "         mrq.v_No,  mrq.v_Date, mrq.departmentToCode, dm1.name departmentTo "
                    + "	FROM requisition mrq "
                    + "	INNER JOIN ("
                    + "     SELECT rd.docId"
                    + "     FROM requisitionDetail rd" + joinItemIssue + conditionItemIssue
                    + "     GROUP BY rd.docId"
                    + "	) mrqd on mrqd.docId = mrq.docid"
                    + "	left join voucher_Type vt on vt.v_Type = mrq.v_Type "
                    + "	left join subGroup dm on dm.subCode = mrq.departmentCode "
                    + "	left join subGroup dm1 on dm1.subCode = mrq.departmentToCode "
                    + "	left join subGroup rbc on rbc.subCode =	mrq.requestedByCode "
                    + "	where vt.category in( " + getVoucherCategory(new ProjectVoucherCategory().PHARMACY_REQUISITION) + "," + getVoucherCategory(new ProjectVoucherCategory().NURSING_MEDICINE_REQUISTION) + ") "
                    + " and mrq.docid=:requisitionDocID ";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("requisitionDocID", requisitionDocID);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHRequisitionNo_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docid")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recid")));
                listNode.setDepartmentCode(DmShared.xNull(rsTemp.getString("departmentCode")));
                listNode.setDepartmentName(DmShared.xNull(rsTemp.getString("department")));
                listNode.setDepartmentToCode(DmShared.xNull(rsTemp.getString("departmentToCode")));
                listNode.setDepartmentToName(DmShared.xNull(rsTemp.getString("departmentTo")));
                listNode.setRequisitionBy(DmShared.xNull(rsTemp.getString("requestedByName")));
                listNode.setRequisitionByCode(DmShared.xNull(rsTemp.getString("requestedByCode")));
                listNode.setRequisitionDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();

            rstTemp.setStatus(true);
            rstTemp.setData(lstTemp);
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
//<editor-fold defaultstate="collapsed" desc="Account Posting">

    private List<AccountPosting_Mdl> iniAcPostList(String docId, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<AccountPosting_Mdl> lstApm = new ArrayList<>();
        AccountPosting_Mdl apm;
        String sqlQuery = "", inventorySql = "", balancingAcSql = "", baseSql = "";
        String topInventoryAc = "";
        boolean flag = false;
        int sr = 0;

        try {
            //<editor-fold defaultstate="collapsed" desc="Prepare Account Posting SQL">
            sqlQuery = " SELECT \n"
                    + "		TOP 1 shd.oh_Ac_Item AS subCode\n"
                    + "	FROM stockHeadDetail shd\n"
                    + "	WHERE shd.docId = :docId\n"
                    + "	AND shd.oh_Ac_Item IS NOT NULL\n"
                    + " ORDER BY shd.v_SNo\n";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(docId));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                topInventoryAc = DmShared.xNull(rsTemp.getString("subCode"));
            }
            rsTemp.close();
            psTemp.close();

            ++sr;
            inventorySql = "SELECT t.subCode, MAX(t.contraSub) AS contraSub , SUM(t.amount) AS amount, " + sr + " AS sr\n"
                    + "FROM (\n"
                    + "	-- Inventory Posting\n"
                    + "	SELECT \n"
                    + "		shd.oh_Ac_Item AS subCode\n"
                    + "		, shd.GrIr_ConsumptionAc AS contraSub\n"
                    + "		, -ISNULL(stk.grossPurchaseRate, 0) AS amount\n"
                    + "	FROM stockHead sh\n"
                    + "	LEFT JOIN stockHeadDetail shd ON shd.docId = sh.docId \n"
                    + "	LEFT JOIN stock stk ON stk.docId = shd.docId AND stk.v_SNo = shd.v_SNo \n"
                    + "	WHERE sh.docId = :docId\n"
                    + "	AND shd.oh_Ac_Item IS NOT NULL\n"
                    + "	AND ISNULL(stk.grossPurchaseRate, 0) <> 0\n"
                    + "\n"
                    + "	) t\n"
                    + "GROUP BY t.subCode,t.contraSub\n";
            ++sr;
            balancingAcSql = "SELECT t.subCode, MAX(t.contraSub) AS contraSub , SUM(t.amount) AS amount, " + sr + " AS sr\n"
                    + "FROM (\n"
                    + "	-- Balancing A/c Posting\n"
                    + "	SELECT \n"
                    + "		shd.GrIr_ConsumptionAc AS subCode\n"
                    + "		, shd.oh_Ac_Item AS contraSub\n"
                    + "		, ISNULL(stk.grossPurchaseRate, 0) AS amount\n"
                    + "	FROM stockHead sh\n"
                    + "	LEFT JOIN stockHeadDetail shd ON shd.docId = sh.docId \n"
                    + "	LEFT JOIN stock stk ON stk.docId = shd.docId AND stk.v_SNo = shd.v_SNo \n"
                    + "	WHERE sh.docId = :docId\n"
                    + "	AND shd.GrIr_ConsumptionAc IS NOT NULL\n"
                    + "	AND ISNULL(stk.grossPurchaseRate, 0) <> 0\n"
                    + "	) t\n"
                    + "GROUP BY t.subCode,t.contraSub\n";

            baseSql = inventorySql
                    + "UNION ALL\n"
                    + "\n"
                    + balancingAcSql;

            sqlQuery = "SELECT CAST(SUM(t.amount) AS DECIMAL(18,2)) AS amount\n"
                    + "FROM (\n"
                    + baseSql
                    + "	) t\n";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(docId));
//            psTemp.setString("topInventoryAc", DmShared.iNull(topInventoryAc).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.vNull(rsTemp.getString("amount")).doubleValue() != 0) {
                    throw new DmException(0, "Error in ledger posting.");
                }
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "SELECT t.subCode, t.contraSub, t.amount, sgc.name as contraSubName\n"
                    + "FROM (\n"
                    + baseSql
                    + "	) t\n"
                    + "LEFT JOIN subGroup sgc ON sgc.subCode = t.contraSub\n"
                    + "ORDER BY sr";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(docId));
//            psTemp.setString("topInventoryAc", DmShared.iNull(topInventoryAc).toString());
            rsTemp = psTemp.executeQuery();
            //</editor-fold>

            flag = false;
            while (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("subCode")).longValue() > 0) {
                    if (!flag) {
                        flag = true;
                    }
                    apm = new AccountPosting_Mdl();
                    apm.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                    apm.setContraCode(DmShared.xNull(rsTemp.getString("contraSub")));
                    apm.setContraText(DmShared.xNull(rsTemp.getString("contraSubName")));
                    if (DmShared.vNull(rsTemp.getString("amount")).doubleValue() > 0) {
                        apm.setAmtDr(DmServer.vNull(rsTemp.getString("amount"), "0.00"));
                        apm.setAmtCr("");
                    } else if (DmShared.vNull(rsTemp.getString("amount")).doubleValue() < 0) {
                        apm.setAmtCr(DmServer.vNull(rsTemp.getString("amount"), "0.00"));
                        apm.setAmtDr("");
                    }
                    lstApm.add(apm);
                }
            }
            rsTemp.close();
            psTemp.close();

            if (!flag) {
                throw new DmException(0, "Error in ledger posting.");
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

        return lstApm;
    }
    //</editor-fold>
}
