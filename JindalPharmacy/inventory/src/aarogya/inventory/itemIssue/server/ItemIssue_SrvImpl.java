package aarogya.inventory.itemIssue.server;

import aarogya.inventory.itemIssue.shared.ItemIssue_Srv;
import aarogya.inventory.itemIssue.shared.GrdItemDetail_Mdl;
import aarogya.inventory.itemIssue.shared.ItemIssue_Mdl;
import aarogya.inventory.itemIssue.shared.TxtHItemName_Mdl;
import aarogya.inventory.itemIssue.shared.ItemIssue_Vld;
import aarogya.inventory.itemIssue.shared.Find_Mdl;
import aarogya.inventory.itemIssue.shared.DefaultValue_Mdl;
import aarogya.inventory.itemIssue.shared.ItemIssueWithRequisition_Mdl;
import aarogya.inventory.itemIssue.shared.TxtHRequisitionNo_Mdl;
import aarogya.inventory.itemIssue.shared.TxtHIssueTo_Mdl;
import aarogya.inventory.itemIssue.shared.TxtHProject_Mdl;
import aarogya.inventory.itemIssue.shared.TxtHRegNo_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.itemBatch.server.ItemBatch_Server;
import aarogya.selfToolkit.itemBatch.shared.ItemBatch_Mdl;
import dataman.dmToolkit.stock.shared.Stock_Mdl;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import aarogya.selfToolkit.modifyReason.server.ModifyReason_Server;
import aarogya.selfToolkit.stStatic.server.STServer;
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
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemPropertyPopup_Vld;

public class ItemIssue_SrvImpl extends DmRSSPrivate implements ItemIssue_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String vCat = new ProjectVoucherCategory().ITEM_ISSUE;
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
    public DmResult<ItemIssue_Mdl> getMoveRec(String strKey) {
        DmResult<ItemIssue_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        ItemIssue_Mdl mrTemp = new ItemIssue_Mdl();
        dmrTemp.setData(mrTemp);
        List<GrdItemDetail_Mdl> lstNode = new ArrayList<GrdItemDetail_Mdl>();
        GrdItemDetail_Mdl node;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*==============================<< Get header detail >>===============================*/
            sqlQuery = "select	sh.docId, sh.recId, sh.v_No, sh.recIdPrefix, sh.v_Date, sh.u_Name, sh.preparedBy, sh.v_Type, vt.description,vt.short_Name, \n"
                    + "		st.departmentCode, sgd.name as departmentName, st.subCode as receivedByCode,sh.refNo,sh.refDate,sh.remark, \n"
                    + "		sgrb.name receivedByName, sh.preparedDate, sh.u_EntDt, sh.balancingAc,sh.projectCode,sh.projectAcCode, \n"
                    + "		pm.name as projectName,sgp.name as projectAcName, vt.short_Name,sh.patientRegDocID \n"
                    + "		from stockHead as sh  \n"
                    + "		left join voucher_Type as vt on sh.v_Type = vt.v_Type  \n"
                    + "		left join projectMAst as pm on sh.projectCode = pm.code \n"
                    + "		left join ( \n"
                    + "                     select s.docId, max(s.departmentCode) as departmentCode \n"
                    + "                     , max(subCode) as subCode \n"
                    + "                     from stockHeadDetail s \n"
                    + "                     where s.docId = :docId \n"
                    + "                     group by s.docId \n"
                    + "		)   as st on sh.docId = st.docId \n"
                    + "		left join subGroup as sgd on st.departmentCode = sgd.subCode  \n"
                    + "		left join subGroup as sgrb on st.subCode = sgrb.subCode \n"
                    + "		left join subGroup as sgp on sh.projectAcCode = sgp.subCode \n"
                    + "		where sh.docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("docId")).toString());
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.getRecIdItemIssue().setRecIdValue(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.getRecIdItemIssue().setCounter(DmShared.iNull(rsTemp.getString("v_No")).longValue());
                mrTemp.getRecIdItemIssue().setPrefix(DmShared.xNull(rsTemp.getString("recIdPrefix")));
                mrTemp.setVoucherDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                mrTemp.setVoucherTypeCode(DmShared.xNull(rsTemp.getString("v_Type")));
                mrTemp.setVoucherTypeShortName(DmShared.xNull(rsTemp.getString("short_Name")));
                mrTemp.setVoucherTypeName(DmShared.xNull(rsTemp.getString("description")));
                mrTemp.setBalancingAc(DmShared.xNull(rsTemp.getString("balancingAc")));
                mrTemp.setRefDate(DmShared.dNull(rsTemp.getDate("refDate")));
                mrTemp.setRefNo(DmShared.xNull(rsTemp.getString("refNo")));
                mrTemp.setRemarkHeader(DmShared.xNull(rsTemp.getString("remark")));
                mrTemp.setDepartmentTag(DmShared.xNull(rsTemp.getString("departmentCode")));
                mrTemp.setDepartmentText(DmShared.xNull(rsTemp.getString("departmentName")));
                mrTemp.setReceivedByTag(DmShared.xNull(rsTemp.getString("receivedByCode")));
                mrTemp.setReceivedByText(DmShared.xNull(rsTemp.getString("receivedByName")));
                mrTemp.setSharedPreparedBy(getUserWithDateTime(DmShared.xNull(rsTemp.getString("preparedBY")), rsTemp.getTimestamp("preparedDate")));
                mrTemp.setSharedModifiedBy(getUserWithDateTime(DmShared.xNull(rsTemp.getString("u_Name")), rsTemp.getTimestamp("u_EntDt")));
                mrTemp.setProjectAcCode(DmShared.xNull(rsTemp.getString("projectAcCode")));
                mrTemp.setProjectAcName(DmShared.xNull(rsTemp.getString("projectAcName")));
                mrTemp.setProjectCode(DmShared.xNull(rsTemp.getString("projectCode")));
                mrTemp.setProjectName(DmShared.xNull(rsTemp.getString("projectName")));
                mrTemp.setPatientRegDocID(DmShared.xNull(rsTemp.getString("patientRegDocID")));
            }
            rsTemp.close();
            psTemp.close();

            if (DmShared.xNull(mrTemp.getVoucherTypeShortName()).trim().equalsIgnoreCase("PASIS")) {
                sqlQuery = "select pr.regDocid as code,pr.recId as registrationID,pu.patientName, vt.description,vt.category,vt.short_Name,vt.nCat \n"
                        + "		from patientRegistration pr \n"
                        + "		left join voucher_Type vt ON pr.v_Type = vt.v_Type \n"
                        + "		left join patientUHID pu ON pu.docId = pr.uhid \n"
                        + "		where pr.regDocId = :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getPatientRegDocID()).toString());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    mrTemp.setPatientRegDocID(DmShared.xNull(rsTemp.getString("code")));
                    mrTemp.setPatientRegRecID(DmShared.xNull(rsTemp.getString("registrationID")));
                    mrTemp.setPatientName(DmShared.xNull(rsTemp.getString("patientName")));
                    mrTemp.setRoomSearchType(DmShared.xNull(rsTemp.getString("short_Name")));
                    mrTemp.setRoomSearchTypeName(DmShared.xNull(rsTemp.getString("nCat")));
                }
            }
            /*==============================<< Get Item grid detail >>===============================*/
            sqlQuery = " Select	shd.itemCode, im.name as itemName, um.unit as skuName, shd.skuValue,shd.quantity , "
                    + " shd.remark, shd.ibCode, shd.batchNo, shd.lotNo, shd.expiryDate, shd.manufacturingDate, shd.mrp, im.propertyList ,  "
                    + " DATEDIFF(day, shd.manufacturingDate, shd.expiryDate) AS bestBeforeDays,  shd.colourCode, shd.sizeCode,shd.itemMakeCode ,"
                    + " im.purchaseUnitConversionRate as skuQtyForRate ,  "
                    + " icm.name AS colourName, ism.name AS sizeName, imm.companyName as itemMakeName ,  um.decimalPlaces,im.isBestBefore,shd.requisitionDocId,shd.requisitionV_SNo ,"
                    + " mrq.recId as reqNo, shd.v_SNo,shd.skuMrp ,mrq.departmentCode As requisitionDepartmentCode, shd.oh_Ac_Item,shd.balancingAc "
                    + " from stockHeadDetail  shd  "
                    + " left join itemMast as im on shd.itemCode = im.code  "
                    + " left join unitMast as um on im.sku = um.code  "
                    + " LEFT JOIN requisition mrq ON mrq.docId = shd.requisitionDocId "
                    + " LEFT JOIN itemColourMast icm ON shd.colourCode = icm.code  "
                    + " LEFT JOIN itemSizeMast ism ON shd.sizeCode = ism.code"
                    + " LEFT JOIN itemMakeMast imm ON imm.code = shd.itemMakeCode "
                    + " where shd.docId = :docId "
                    + " order by shd.v_SNo ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdItemDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                node.setV_SNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                node.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                node.setGrIrConsumptionAc(DmShared.xNull(rsTemp.getString("balancingAc")));
                node.setSkuDecimalPlaces(DmShared.iNull(rsTemp.getString("decimalPlaces")).toString());
                node.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                node.setQuantity(DmServer.vNull(rsTemp.getString("quantity")));
                node.setPropertyList(DmShared.xNull(rsTemp.getString("propertyList")));
                node.setIbCode(DmShared.xNull(rsTemp.getString("ibCode")));
                node.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                node.setLotNo(DmShared.xNull(rsTemp.getString("lotNo")));
                node.setBestBeforeDays(DmShared.xNull(rsTemp.getString("bestBeforeDays")));
                node.setManufacturingDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));
                node.setExpiryDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                node.setColourCode(DmShared.xNull(rsTemp.getString("colourCode")));
                node.setColourName(DmShared.xNull(rsTemp.getString("colourName")));
                node.setSizeCode(DmShared.xNull(rsTemp.getString("sizeCode")));
                node.setSizeName(DmShared.xNull(rsTemp.getString("sizeName")));
                node.setItemMakeCode(DmShared.xNull(rsTemp.getString("itemMakeCode")));
                node.setItemMakeName(DmShared.xNull(rsTemp.getString("itemMakeName")));
                node.setIsBestBefore(DmShared.xNull(rsTemp.getString("isBestBefore")));
                node.setMrp(DmServer.vNull(rsTemp.getString("mrp"), "0.00"));
                node.setSkuQtyForRate(DmServer.vNull(rsTemp.getString("skuQtyForRate")));
                node.setSkuMrp(DmServer.vNull(rsTemp.getString("skuMrp"), DmShared.getNumberFormat(7)));
                node.setRequisitionDocId(DmShared.xNull(rsTemp.getString("requisitionDocId")));
                node.setRequisitionNo(DmShared.xNull(rsTemp.getString("reqNo")));
                node.setRequisitionV_SNo(DmShared.xNull(rsTemp.getString("requisitionV_SNo")));

                if (!DmShared.xNull(rsTemp.getString("reqNo")).equalsIgnoreCase("") && DmShared.xNull(mrTemp.getRequisitionDocId()).trim().equals("")) {
                    mrTemp.setRequisitionDocId(DmShared.xNull(rsTemp.getString("requisitionDocId")));
                    mrTemp.setRequisitionNo(DmShared.xNull(rsTemp.getString("reqNo")));
                    mrTemp.setDepartmentToTag(DmShared.foriegnKey(rsTemp.getString("requisitionDepartmentCode")));
                }

                //IAP
                node.setIsubCode(DmShared.xNull(rsTemp.getString("oh_Ac_Item")));

                //======================< Set Item Properties Display >======================
                ItemProperty_Mdl ipm = new ItemProperty_Mdl();
                ipm.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                ipm.setLotNo(DmShared.xNull(rsTemp.getString("lotNo")));
                ipm.setItemName(DmShared.xNull(rsTemp.getString("itemName")));
                ipm.setColourName(DmShared.xNull(rsTemp.getString("colourName")));
                ipm.setSizeName(DmShared.xNull(rsTemp.getString("sizeName")));
                ipm.setMrp(DmShared.xNull(rsTemp.getString("mrp")));
                ipm.setExpiryDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                ipm.setManufacturingDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));
                ipm.setItemMakeCode(DmShared.xNull(rsTemp.getString("itemMakeCode")));
                ipm.setItemMakeName(DmShared.xNull(rsTemp.getString("itemMakeName")));
                node.setPropertyDisplay(ipm.getPropertiesCsv());
                //============================================================================

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

    private String getUserWithDateTime(String userName, Timestamp entryDateTime) {
        String rtnValue = "";
        Calendar cal;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy - HH:mm");
        try {
            if (!DmShared.xNull(userName).equalsIgnoreCase("") && entryDateTime != null) {
                cal = Calendar.getInstance();
                cal.setTime(new Date(entryDateTime.getTime()));
                rtnValue = userName + " - " + sdf.format(cal.getTime());
            }
        } catch (Exception e) {
        }
        return rtnValue;
    }

    @Override
    public DmResult<String> deleteRecord(String keyCode) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        dmrTemp.setData(keyCode);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            DmServer.checkLockDate(DmServer.getSsnSiteCode(this), DmServer.getSsnVoucherPrefix(this), cnTemp);
            /*==============================<< Delete Ledger Posting >>====================================*/
            AccountPosting_Server.deleteAccountPosting(keyCode, cnTemp);

            /*==============================<< Delete record from stock >>===============================*/
            Stock_Server.deleteStock(keyCode, cnTemp);

            /*==============================<< Manage Requisition Status >>====================================*/
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
    public DmResult<String> saveRecord(ItemIssue_Mdl mrTemp, ItemIssue_Mdl mrTempPrevious, String voucherPrefix) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery, userName, siteCode;
        ResultSet rsTemp = null;
        dmrTemp.setData("");

        try {
            ItemIssue_Vld.chkSave(mrTemp);
            userName = DmServer.getSsnUserName(this);
            siteCode = DmServer.getSsnSiteCode(this);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            DmServer.checkForwordVoucherDateTime(mrTemp.getVoucherDate(), "");
            if (mrTemp.isInventoryAccountingActive()) {
//                DmServer.checkBackwordVoucherDateTime("stock", "v_Date", "", mrTemp.getVoucherDate(), "", cnTemp);
            }

            spBegin = new DmSavePoint(cnTemp);
            DmServer.checkLockDate(DmServer.getSsnSiteCode(this), DmServer.getSsnVoucherPrefix(this), cnTemp);
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                /*==============================<< Generate docId for new record >>====================================*/
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this));
                mrTemp.setRecIdItemIssue(DmServer.getRecId("stockHead", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecIdItemIssue(), mrTemp.getVoucherDate(), DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this, true));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(mrTemp.getRecIdItemIssue().getRecIdValue()));
            } else {
                DmServer.getRecId("stockHead", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecIdItemIssue(), mrTemp.getVoucherDate(), DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this, true);
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

////            /*==============================<< Check items for Department item change >>====================================*/
//            sqlQuery = "select stk.itemCode, allowedQty -(sum(isNull(stk.isuQty,0))+currentQty) as last30daysConsumption \n"
//                    + "                    		from stock stk \n"
//                    + "                    		where stk.v_Date between dateAdd(d,-30,:vDate) and :vDate \n"
//                    + "                    		and stk.departmentCode=:departmentCode \n"
//                    + "                    		group by stk.itemCode";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
//            psTemp.setString("departmentCode", DmShared.foriegnKey(mrTemp.getDepartmentTag()));
//            rsTemp = psTemp.executeQuery();
//            if (rsTemp.next()) {
//                if (DmShared.iNull(rsTemp.getString("totalInvalidRecords")).intValue() > 0) {
//                    throw new DmException(0, "Requisition has been changed, can't save record, refresh record then save");
//                }
//            }
//            rsTemp.close();
//            psTemp.close();

            /*==============================<< Insert / Update record into stockHead >>====================================*/
            if (blnAdd) {
                sqlQuery = "Insert Into stockHead (docId, v_Type, v_No, v_Date, v_Prefix, recId,refNo,refDate,remark, "
                        + " balancingAc, site_Code, transferDt, preparedBy, preparedDate ,recIdPrefix,projectCode,projectAcCode,patientRegDocID) "
                        + " values (:docId, :v_Type, :v_No, :v_Date,  :v_Prefix, :recId,:refNo,:refDate,:remark, "
                        + " :balancingAc, :site_Code, :transferDt, :preparedBy, :preparedDate,:recIdPrefix,:projectCode,:projectAcCode,:patientRegDocID) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("recId", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("v_Type", DmShared.foriegnKey(mrTemp.getVoucherTypeCode()));
                psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setString("v_Prefix", voucherPrefix);
                psTemp.setString("v_No", DmShared.iNull(mrTemp.getRecIdItemIssue().getCounter()).toString());
                psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getRecIdItemIssue().getPrefix()));
                psTemp.setString("balancingAc", DmShared.foriegnKey(mrTemp.getBalancingAc()));
                psTemp.setString("site_Code", DmShared.foriegnKey(siteCode));
                psTemp.setString("transferDt", null);
                psTemp.setString("preparedBy", userName);
                psTemp.setString("refNo", DmShared.xNull(mrTemp.getRefNo()));
                psTemp.setDate("refDate", DmServer.convertDate(mrTemp.getRefDate()));
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemarkHeader()));
                psTemp.setTimestamp("preparedDate", DmServer.getServerTimestamp());
                psTemp.setString("projectCode", DmShared.foriegnKey(mrTemp.getProjectCode()));
                psTemp.setString("projectAcCode", DmShared.foriegnKey(mrTemp.getProjectAcCode()));
                psTemp.setString("patientRegDocID", DmShared.foriegnKey(mrTemp.getPatientRegDocID()));
            } else {
                sqlQuery = " update stockHead Set "
                        + " v_Date = :v_Date, "
                        + " balancingAc = :balancingAc, "
                        + " transferDt = :transferDt, "
                        + " u_Name = :u_Name, "
                        + " refNo = :refNo, "
                        + " refDate = :refDate, "
                        + " remark = :remark, "
                        + " u_EntDt = :uEntDt, "
                        + " projectCode = :projectCode, "
                        + " projectAcCode = :projectAcCode, "
                        + " patientRegDocID =:patientRegDocID "
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
                psTemp.setString("balancingAc", DmShared.foriegnKey(mrTemp.getBalancingAc()));
                psTemp.setString("projectCode", DmShared.foriegnKey(mrTemp.getProjectCode()));
                psTemp.setString("projectAcCode", DmShared.foriegnKey(mrTemp.getProjectAcCode()));
                psTemp.setString("patientRegDocID", DmShared.foriegnKey(mrTemp.getPatientRegDocID()));
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
            List<GrdItemDetail_Mdl> listDetail = mrTemp.getListItemDetail();
            for (GrdItemDetail_Mdl detail_Mdl : listDetail) {
                if (!DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                    detail_Mdl.setV_SNo(String.valueOf(++v_SNo));
                    sqlQuery = " insert into stockHeadDetail(docId, v_SNo, departmentCode,"
                            + " itemCode, quantity, remark,subCode,site_Code ,requisitionDocId,requisitionV_SNo,"
                            + " ibCode, batchNo, lotNo, manufacturingDate,expiryDate, colourCode, sizeCode,itemMakeCode, "
                            + " mrp,skuMrp, oh_Ac_Item,balancingAc)"
                            + " values (:docId, :v_SNo, :departmentCode,"
                            + " :itemCode, :quantity, :remark,:subCode,:site_Code ,:requisitionDocId,:requisitionV_SNo,"
                            + " :ibCode,:batchNo, :lotNo, :manufacturingDate,:expiryDate, :colourCode, :sizeCode,:itemMakeCode, "
                            + "  :mrp,:skuMrp, :oh_Ac_Item,:balancingAc)";

                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                    psTemp.setString("v_SNo", DmShared.iNull(detail_Mdl.getV_SNo()).toString());
                    psTemp.setString("departmentCode", DmShared.foriegnKey(mrTemp.getDepartmentTag()));
                    psTemp.setString("itemCode", DmShared.foriegnKey(detail_Mdl.getSharedKeyFieldCode()));
                    psTemp.setString("quantity", DmServer.vNull(detail_Mdl.getQuantity()));
                    psTemp.setString("remark", DmShared.xNull(detail_Mdl.getRemark()));
                    psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getReceivedByTag()));
                    psTemp.setString("site_Code", DmShared.foriegnKey(siteCode));
                    psTemp.setString("requisitionDocId", DmShared.foriegnKey(detail_Mdl.getRequisitionDocId()));
                    psTemp.setString("requisitionV_SNo", DmShared.foriegnKey(detail_Mdl.getRequisitionV_SNo()));
                    psTemp.setString("skuMrp", DmServer.vNull(detail_Mdl.getSkuMrp()));

                    psTemp.setString("oh_Ac_Item", DmShared.foriegnKey(detail_Mdl.getIsubCode()));
                    psTemp.setString("ibCode", DmShared.foriegnKey(detail_Mdl.getIbCode()));
                    psTemp.setString("batchNo", DmShared.foriegnKey(detail_Mdl.getBatchNo()));
                    psTemp.setString("lotNo", DmShared.foriegnKey(detail_Mdl.getLotNo()));
                    psTemp.setDate("manufacturingDate", DmServer.convertDate(detail_Mdl.getManufacturingDate()));
                    psTemp.setDate("expiryDate", DmServer.convertDate(detail_Mdl.getExpiryDate()));
                    psTemp.setString("colourCode", DmShared.foriegnKey(detail_Mdl.getColourCode()));
                    psTemp.setString("sizeCode", DmShared.foriegnKey(detail_Mdl.getSizeCode()));
                    psTemp.setString("itemMakeCode", DmShared.foriegnKey(detail_Mdl.getItemMakeCode()));
                    psTemp.setString("mrp", DmServer.vNull(detail_Mdl.getMrp()));
                    if (!DmShared.xNull(mrTemp.getProjectAcCode()).trim().equalsIgnoreCase("")) {
                        psTemp.setString("balancingAc", DmShared.foriegnKey(mrTemp.getProjectAcCode()));
                    } else {
                        psTemp.setString("balancingAc", DmShared.foriegnKey(detail_Mdl.getGrIrConsumptionAc()));
                    }

                    psTemp.executeUpdate();
                    psTemp.close();

                    /*----------------------------------------------------------Stock Saving--------------------------------------------------------------*/
                    Stock_Mdl stock_Mdl = new Stock_Mdl();
                    stock_Mdl.setSharedKeyFieldCode(DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                    stock_Mdl.setSharedKeyFieldName(detail_Mdl.getV_SNo());
                    stock_Mdl.setSite_Code(DmShared.xNull(siteCode));
                    stock_Mdl.setV_Type(DmShared.xNull(mrTemp.getVoucherTypeCode()));
                    stock_Mdl.setV_No(DmShared.iNull(mrTemp.getRecIdItemIssue().getCounter()).toString());
                    stock_Mdl.setRecId(DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                    stock_Mdl.setRecIdPrefix(DmShared.xNull(mrTemp.getRecIdItemIssue().getPrefix()));
                    stock_Mdl.setV_Date(DmShared.dNull(mrTemp.getVoucherDate()));
                    stock_Mdl.setRefChallanNo(DmShared.xNull(mrTemp.getRefNo()));
                    stock_Mdl.setRefChallanDate(DmShared.dNull(mrTemp.getRefDate()));
                    stock_Mdl.setDepartmentCode(DmShared.xNull(mrTemp.getDepartmentTag()));
                    stock_Mdl.setStockContraSubCode(DmShared.xNull(mrTemp.getReceivedByTag()));
                    stock_Mdl.setRemark(DmShared.xNull(mrTemp.getRemarkHeader()) + "\n" + DmShared.xNull(detail_Mdl.getRemark()));

                    stock_Mdl.setItemCode(DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()));
                    stock_Mdl.setIbCode(detail_Mdl.getIbCode());
                    stock_Mdl.setIsuQty(DmServer.vNull(detail_Mdl.getQuantity()));

                    Stock_Server.saveStock(stock_Mdl, DmEnum.StockEntryType.OTHER, cnTemp);

                }
            }

            /*==============================<< Account Posting >>====================================*/
            if (mrTemp.isInventoryAccountingActive()) {
                AccountPosting_Server.saveAccountPosting(iniAcPostList(mrTemp.getSharedKeyFieldCode(), cnTemp), "docId", mrTemp.getSharedKeyFieldCode(), mrTemp.getRecIdItemIssue(), mrTemp.getVoucherTypeCode(), voucherPrefix, siteCode, mrTemp.getVoucherDate(), "", "", "", "", "", true, false, cnTemp);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHVoucher(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile) {
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

            if (!DmServer.getUserVoucherTypes(userPermission).equalsIgnoreCase("")) {
                condition = " and vt.v_Type in (" + DmServer.getUserVoucherTypes(userPermission) + ")";
            }
            sqlWithParam.setSQL(" select vt.v_Type as code, vt.description as name, vt.balancingAc, vt.short_Name \n"
                    + "from voucher_Type vt\n"
                    + "where vt.category = '" + vCat + "'\n"
                    + "and vt.isActive = 1 " + condition);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("balancingAc")));
                listNode.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("short_Name")));
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
    public DmResult<DefaultValue_Mdl> getDefaultValues(UserPermission userPermission) {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult();
        DefaultValue_Mdl defaultValue = new DefaultValue_Mdl();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, condition = "";
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*==============================<< Get default voucher_Type >>===============================*/
            if (!DmServer.getUserVoucherTypes(userPermission).equalsIgnoreCase("")) {
                condition = " and vt.v_Type in (" + DmServer.getUserVoucherTypes(userPermission) + ")";
            }
            sqlQuery = " select vt.v_Type as code, vt.description as name, vt.balancingAc,vt.short_Name \n"
                    + "from voucher_Type vt \n"
                    + "where vt.category = '" + vCat + "' \n"
                    + "and vt.isActive = 1 " + condition;
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setVoucherTypeCode(DmShared.xNull(rsTemp.getString("code")));
                defaultValue.setVoucherTypeName(DmShared.xNull(rsTemp.getString("name")));
                defaultValue.setVoucherTypeShortName(DmShared.xNull(rsTemp.getString("short_Name")));
                defaultValue.setBalancingAc(DmShared.xNull(rsTemp.getString("balancingAc")));
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

            sqlQuery = "select msgOnItemIssueExceed, case when msgOnItemIssueExceed =1 then 'Warning' else 'Stop' end as msgOnItemIssueExceedName from enviro where site_Code = :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmShared.xNull(DmServer.getSsnSiteCode(this)));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setItemIssueExceedCode(DmShared.xNull(rsTemp.getString("msgOnItemIssueExceed")));
                defaultValue.setItemIssueExceedName(DmShared.xNull(rsTemp.getString("msgOnItemIssueExceedName")));
            }
            rsTemp.close();
            psTemp.close();

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
    public DmResultPageing<List<TxtHItemName_Mdl>> getTxtHItemName(String v_Type, String departmentCode, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHItemName_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHItemName_Mdl> lstTemp = new ArrayList<TxtHItemName_Mdl>();
        TxtHItemName_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "", itemCatFilteration = "";
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (DmShared.xNull(departmentCode).equalsIgnoreCase("")) {
                condition = " AND 1 = 2 ";
            }

            itemCatFilteration = DmServer.getItemCatFilteration(v_Type, cnTemp);

            if (!DmShared.xNull(itemCatFilteration).equalsIgnoreCase("")) {
                condition += " AND im.itemCategoryCode IN (" + itemCatFilteration + ")";
            }

            sqlQuery = "SELECT  im.code, im.name, im.manualCode, um.unit AS skuName, vis.currentStock,"
                    + "         im.propertyList, im.isBestBefore, um.decimalPlaces, im.purchaseUnitConversionRate, im.subCode AS iSubCode, "
                    + "		case when isNull(igdc.consumptionAc,'')='' then im.consumptionAc else igdc.consumptionAc end as consumptionAc,"
                    + "         igdf.allowedConsumption  "
                    + "		FROM itemMast im"
                    + "         LEFT JOIN itemSiteList sl ON sl.code = im.code"
                    + "		LEFT JOIN unitmast um ON im.sku = um.code"
                    + "		LEFT JOIN itemGroupDepartmentConsumptionAc igdc on im.itemGroup=igdc.code and igdc.DepartmentCode=:departmentCode "
                    + "         LEFT JOIN itemDepartmentInfo igdf on igdf.itemCode = im.code "
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
                    + "		AND (im.filterItemType = :filterItemType OR im.isCommon = 1) "
                    + "         AND sl.site_Code = :site_Code"
                    + "         AND virtd.code IS NULL "
                    + "         AND CASE WHEN ISNULL(im.allowNegativeStock,0) = 1 THEN 1 ELSE CASE WHEN ISNULL(vis.currentStock, 0) > 0 THEN 1 ELSE 0 END END = 1 "
                    + condition;
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("departmentCode", DmShared.iNull(departmentCode).toString());
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHItemName_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setIsubCode(DmShared.xNull(rsTemp.getString("iSubCode")));
                listNode.setConsumptionAc(DmShared.xNull(rsTemp.getString("consumptionAc")));
                listNode.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                listNode.setSkuDecimalPlaces(DmShared.iNull(rsTemp.getString("decimalPlaces")).toString());
                listNode.setPurchaseUnitConversionRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));
                listNode.setItemProperty(DmShared.xNull(rsTemp.getString("propertyList")));
                listNode.setStock(DmServer.vNull(rsTemp.getString("currentStock")));
                listNode.setIsBestBefore(DmShared.iNull(rsTemp.getString("isBestBefore")).toString());
                listNode.setAllowedMonthlyConsumption(DmShared.iNull(rsTemp.getString("allowedConsumption")).toString());
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
                    + " AND sg.isActive = 1"
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

            sqlQuery = "select	dm.subCode as code, sg.name ,sgn.manualCode "
                    + "		from departmentMast as dm "
                    + "		left join subGroup as sg on dm.subCode = sg.subCode "
                    + "         left join subGroupNature sgn on sgn.code = sg.nature"
                    + "		Left join subGroupSiteList as sgsl on sg.subCode=sgsl.subCode "
                    + "         where sgn.manualCode IN ('CM','PR')"
                    + "            and  sgsl.site_Code = :siteCode "
                    + "         and sg.isActive = 1 " + condition;
            sqlWithParam.setSQL(sqlQuery);
            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("department", STServer.getSsnDepartmentCodeList(this));
            }
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                rstTemp.setValue1(DmShared.xNull(rsTemp.getString("manualCode")));
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
    public DmResultPageing<List<TxtHRequisitionNo_Mdl>> getTxtHRequisitionNo(String voucherDate, String issueDocId, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHRequisitionNo_Mdl>> rstTemp = new DmResultPageing<List<TxtHRequisitionNo_Mdl>>();
        List<TxtHRequisitionNo_Mdl> lstTemp = new ArrayList<TxtHRequisitionNo_Mdl>();
        Connection cnTemp = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            lstTemp = getRequisitionDetail("", voucherDate, issueDocId, helper, blnToEndOfFile, cnTemp);

            rstTemp.setData(lstTemp);
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());

            cnTemp.close();
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    private List<TxtHRequisitionNo_Mdl> getRequisitionDetail(String reqDocid, String voucherDate, String issueDocId, DmHelper helper, boolean blnToEndOfFile, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<TxtHRequisitionNo_Mdl> lstTemp = new ArrayList<>();
        TxtHRequisitionNo_Mdl listNode;
        String sqlQuery, condition = "", joinItemIssue = "", conditionItemIssue = "";
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            if (DmShared.dNull(voucherDate).trim().equalsIgnoreCase("")) {
                throw new DmException(0, "Voucher date is mandatory.");
            }
            if (DmShared.iNull(reqDocid).longValue() > 0) {
                condition += " and mrq.docId = :docId ";
            }

            if (DmShared.iNull(issueDocId).longValue() != 0) {
                joinItemIssue = " LEFT JOIN stockHeadDetail sd ON sd.requisitionDocId = rd.docId AND sd.requisitionV_SNo = rd.v_SNo";
                conditionItemIssue += " WHERE CASE WHEN rd.isStatus = 0 AND ISNULL(rd.reqQty,0) - ISNULL(rd.issueQty,0) > 0 THEN 1 ELSE CASE WHEN rd.isStatus = 2 AND ISNULL(sd.docid,0) = :issueDocId  THEN 1 ELSE 0 END  END = 1 ";
            } else {
                conditionItemIssue += " WHERE rd.isStatus = 0 AND ISNULL(rd.reqQty,0) - ISNULL(rd.issueQty,0) > 0 ";
            }

            sqlQuery = " select mrq.docid, mrq.recid, mrq.departmentCode, dm.name as department, rbc.name as requestedByName,mrq.requestedByCode, vt.short_Name,"
                    + "         mrq.v_No,sgn.manualCode AS departmentNature, mrq.v_Date, mrq.departmentToCode, dm1.name departmentTo "
                    + "	FROM requisition mrq "
                    + "	INNER JOIN ("
                    + "     SELECT rd.docId"
                    + "     FROM requisitionDetail rd" + joinItemIssue + conditionItemIssue
                    + "     GROUP BY rd.docId"
                    + "	) mrqd on mrqd.docId = mrq.docid"
                    + "	left join voucher_Type vt on vt.v_Type = mrq.v_Type "
                    + "	left join subGroup dm on dm.subCode = mrq.departmentCode "
                    + "	left join subGroup dm1 on dm1.subCode = mrq.departmentToCode "
                    + "	left join subGroup rbc on rbc.subCode =	mrq.requestedByCode"
                    + "	left join subGroupNature sgn on sgn.code = dm1.nature "
                    + "	where vt.category = " + getVoucherCategory(new ProjectVoucherCategory().INVENTORY_REQUISITION) + " "
                    + " and ISNULL(mrq.approvedBy,'') <> '' "
                    + " and mrq.site_Code_To = :siteCode  "
                    + " and mrq.v_Date <= :voucherDate " + condition;
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            sqlWithParam.setDate("voucherDate", DmServer.convertDate(voucherDate));
            if (DmShared.iNull(reqDocid).longValue() > 0) {
                sqlWithParam.setString("docId", DmShared.iNull(reqDocid).toString());
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
                listNode.setDepartmentNature(DmShared.xNull(rsTemp.getString("departmentNature")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();

        } catch (Exception ex) {
            new DmException(0, ex.getMessage());
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
        return lstTemp;
    }

    @Override
    public DmResult<List<TxtHRequisitionNo_Mdl>> getRequisitionDetail(String reqDocid, String voucherDate, DmHelper helper, boolean blnToEndOfFile) {
        DmResult<List<TxtHRequisitionNo_Mdl>> rstTemp = new DmResult<List<TxtHRequisitionNo_Mdl>>();
        List<TxtHRequisitionNo_Mdl> lstTemp = new ArrayList<TxtHRequisitionNo_Mdl>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            lstTemp = getRequisitionDetail(reqDocid, voucherDate, "", helper, blnToEndOfFile, cnTemp);
            rstTemp.setData(lstTemp);

            cnTemp.close();
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
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

    @Override
    public DmResult<List<GrdItemDetail_Mdl>> getItemDetail(String reqDocId, String itemIssueDocId, String departmentCode) {
        DmResult<List<GrdItemDetail_Mdl>> dmrTemp = new DmResult();

        try {
            dmrTemp.setData(getItemDetails(reqDocId, itemIssueDocId, departmentCode).getGrdItemDetailList());
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }

        return dmrTemp;
    }

    private ItemIssueWithRequisition_Mdl getItemDetails(String reqDocId, String itemIssueDocId, String departmentCode) throws Exception {
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, condition = "", recQty = "";
        ItemIssueWithRequisition_Mdl itemIssueWithRequisition = new ItemIssueWithRequisition_Mdl();
        GrdItemDetail_Mdl node;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(itemIssueDocId).equalsIgnoreCase("")) {
                recQty = " ISNULL(mrqd.reqQty,0) - ISNULL(mrqd.issueQty,0) + ISNULL(sd.quantity,0) AS reqQty, ";
                condition += " AND (CASE WHEN (mrqd.isStatus = 0) THEN 1 WHEN (mrqd.isStatus = 2 And ISNULL(mrqd.reqQty,0) - ISNULL(mrqd.issueQty,0) + ISNULL(sd.quantity,0) > 0) THEN 1 ELSE 0 END) = 1 ";
            } else {
                recQty = " ISNULL(mrqd.reqQty,0) - ISNULL(mrqd.issueQty,0) AS reqQty, ";
                condition += " And ISNULL(mrqd.reqQty,0) - ISNULL(mrqd.issueQty,0) > 0 ";
                condition += " and mrqd.isStatus=0 ";
            }

            /*==============================<< Get Item detail (via list) >>===============================*/
            sqlQuery = " select mrq.docid,mrq.recid, mrqd.v_SNo, " + recQty
                    + "         mrqd.itemCode,im.name as itemName, "
                    + "		im.isBestBefore, "
                    + "         im.propertyList,um.unit as skuName, um.decimalPlaces AS skuDecimalPlaces, "
                    + "		im.sku as skuCode,im.purchaseUnitConversionRate,mrqd.colourCode,mrqd.sizeCode,icm.name as colourName,ism.name as sizeName, "
                    + "         mrqd.itemMakeCode, imm.companyName as itemMakeName, im.subCode AS iSubCode,  "
                    + "		case when isNull(igdc.consumptionAc,'')='' then im.consumptionAc else igdc.consumptionAc end as consumptionAc "
                    + "	from requisition mrq "
                    + "	left Join requisitionDetail as mrqd on mrqd.docId = mrq.docid  "
                    + "	left join voucher_Type vt on vt.v_Type = mrq.v_Type "
                    + "	left join itemMast im on im.code = mrqd.itemCode "
                    + " left join itemGroup ig on ig.code = im.itemGroup "
                    + "	left join unitMast um ON um.code = im.sku "
                    + "	left join itemGroupDepartmentConsumptionAc igdc on im.itemGroup=igdc.code and igdc.DepartmentCode=:departmentCode "
                    + "	left join itemColourMast icm ON icm.code = mrqd.colourCode "
                    + " LEFT JOIN itemMakeMast imm ON imm.code = mrqd.itemMakeCode "
                    + "	left join itemSizeMast ism ON ism.code = mrqd.sizeCode "
                    + "	left join hsnMast hm on hm.code = im.hsnCode ";

            if (!DmShared.xNull(itemIssueDocId).equalsIgnoreCase("")) {
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
            psTemp.setString("departmentCode", DmShared.iNull(departmentCode).toString());
            if (!DmShared.xNull(itemIssueDocId).equalsIgnoreCase("")) {
                psTemp.setString("itemIssueDocId", itemIssueDocId);
            }
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {

                itemIssueWithRequisition.setRequisitionCode(DmShared.xNull(rsTemp.getString("docId")));
                itemIssueWithRequisition.setRequisitionName(DmShared.xNull(rsTemp.getString("recid")));

                node = new GrdItemDetail_Mdl();

                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                node.setRequisitionV_SNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                node.setQuantity(DmServer.vNull(rsTemp.getString("reqQty")));
                node.setIsBestBefore(DmShared.xNull(rsTemp.getString("isBestBefore")));
                node.setPropertyList(DmShared.xNull(rsTemp.getString("propertyList")));
                node.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                node.setSkuDecimalPlaces(DmShared.xNull(rsTemp.getString("skuDecimalPlaces")));
                node.setSkuQtyForRate(DmShared.xNull(rsTemp.getString("purchaseUnitConversionRate")));
                node.setRequisitionDocId(DmShared.xNull(rsTemp.getString("docid")));
                node.setRequisitionNo(DmShared.xNull(rsTemp.getString("recid")));
                node.setGrIrConsumptionAc(DmShared.xNull(rsTemp.getString("consumptionAc")));

                node.setColourCode(DmShared.xNull(rsTemp.getString("colourCode")));
                node.setColourName(DmShared.xNull(rsTemp.getString("colourName")));
                node.setSizeCode(DmShared.xNull(rsTemp.getString("sizeCode")));
                node.setSizeName(DmShared.xNull(rsTemp.getString("sizeName")));
                node.setItemMakeCode(DmShared.xNull(rsTemp.getString("itemMakeCode")));
                node.setItemMakeName(DmShared.xNull(rsTemp.getString("itemMakeName")));

                //IAP
                node.setIsubCode(DmShared.xNull(rsTemp.getString("iSubCode")));

                //======================< Set Item Properties Display >======================
                ItemProperty_Mdl ipm = new ItemProperty_Mdl();
                ipm.setItemCode(DmShared.xNull(rsTemp.getString("itemCode")));
                ipm.setItemName(DmShared.xNull(rsTemp.getString("itemName")));
                ipm.setColourCode(DmShared.xNull(rsTemp.getString("colourCode")));
                ipm.setColourName(DmShared.xNull(rsTemp.getString("colourName")));
                ipm.setSizeCode(DmShared.xNull(rsTemp.getString("sizeCode")));
                ipm.setSizeName(DmShared.xNull(rsTemp.getString("sizeName")));
                ipm.setItemMakeCode(node.getItemMakeCode());
                ipm.setItemMakeName(node.getItemMakeName());
                node.setPropertyDisplay(ipm.getPropertiesCsv());
                boolean flagGetIbCode = false;
                if (DmShared.xNull(rsTemp.getString("propertyList")).trim().equals("") && DmShared.xNull(node.getPropertyDisplay()).trim().equals("")) {
                    flagGetIbCode = true;
                } else {
                    try {
                        ItemPropertyPopup_Vld.validateProperties(DmShared.xNull(rsTemp.getString("propertyList")), DmShared.xNull(rsTemp.getString("isBestBefore")), DmServer.getServerDateTime()[0], ipm);
                        flagGetIbCode = true;
                    } catch (Exception e) {
                        flagGetIbCode = false;
                    }
                }
                if (flagGetIbCode) {
                    ItemBatch_Mdl ibm = new ItemBatch_Mdl();
                    ibm.setCode("");
                    ibm.setIpm(ipm);
                    node.setIbCode(ItemBatch_Server.createItemBatch(ibm, cnTemp, this));
                }
                //============================================================================

                itemIssueWithRequisition.getGrdItemDetailList().add(node);
            }
            rsTemp.close();
            psTemp.close();
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

    private void manageRequisitionStatus(String docId, boolean isDelete, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null, psTemp1 = null;
        ResultSet rsTemp = null;
        String sqlQuery, rqDocIds = "";
        double reqQty, issueQty, billQty;
        Integer isStatus;

        try {
            sqlQuery = "select	req.docId as rqDocId, reqd.v_SNo AS rqV_SNo, mbd.Qty as billQty, reqd.reqQty as reqQty, reqd.issueQty, reqd.isStatus, req.approvedBy,  "
                    + "        	req.recId as reqNo, vt.short_name as reqType"
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
                        + " where docId = :docId and v_SNo = :v_SNo";
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
    public DmResult<String> getIbCode(ItemProperty_Mdl mdl) {
        DmResult<String> dmrTemp = new DmResult();
        String ibCode = "";
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ItemBatch_Mdl ibm;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            ibm = new ItemBatch_Mdl();
            ibm.setCode("");
            ibm.setIpm(mdl);
            ibCode = ItemBatch_Server.getItemBatchForIssue(ibm, cnTemp);

            cnTemp.close();
            dmrTemp.setData(ibCode);
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
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
                    + "		, shd.balancingAc AS contraSub\n"
                    + "		, -ISNULL(stk.landedSkuValue, 0) AS amount\n"
                    + "	FROM stockHead sh\n"
                    + "	LEFT JOIN stockHeadDetail shd ON shd.docId = sh.docId \n"
                    + "	LEFT JOIN stock stk ON stk.docId = shd.docId AND stk.v_SNo = shd.v_SNo \n"
                    + "	WHERE sh.docId = :docId\n"
                    + "	AND shd.oh_Ac_Item IS NOT NULL\n"
                    + "	AND ISNULL(stk.landedSkuValue, 0) <> 0\n"
                    + "\n"
                    + "	) t\n"
                    + "GROUP BY t.subCode,t.contraSub\n";
            ++sr;
            balancingAcSql = "SELECT t.subCode, MAX(t.contraSub) AS contraSub , SUM(t.amount) AS amount, " + sr + " AS sr\n"
                    + "FROM (\n"
                    + "	-- Balancing A/c Posting\n"
                    + "	SELECT \n"
                    + "		shd.balancingAc AS subCode\n"
                    + "		, shd.oh_Ac_Item AS contraSub\n"
                    + "		, ISNULL(stk.landedSkuValue, 0) AS amount\n"
                    + "	FROM stockHead sh\n"
                    + "	LEFT JOIN stockHeadDetail shd ON shd.docId = sh.docId \n"
                    + "	LEFT JOIN stock stk ON stk.docId = shd.docId AND stk.v_SNo = shd.v_SNo \n"
                    + "	WHERE sh.docId = :docId\n"
                    + "	AND sh.balancingAc IS NOT NULL\n"
                    + "	AND ISNULL(stk.landedSkuValue, 0) <> 0\n"
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

    @Override
    public DmResultPageing<List<TxtHProject_Mdl>> getTxtHProject(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHProject_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHProject_Mdl> lstTemp = new ArrayList<>();
        TxtHProject_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select pm.code,pm.name,pm.projectAc,sg.name as projectAcName "
                    + "	from projectMast pm"
                    + "	left join subGroup sg on pm.projectAc=sg.subCode"
                    + " LEFT JOIN projectSiteList psl ON psl.code = pm.code \n"
                    + " WHERE pm.active = 1 and psl.site_Code=:site_Code ";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHProject_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setProjectAcCode(DmShared.xNull(rsTemp.getString("projectAc")));
                listNode.setProjectAcName(DmShared.xNull(rsTemp.getString("projectAcName")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCustomerType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(pvc.IPD);
            listNode.setSharedKeyFieldName(pvc.IPD);
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(pvc.OPD);
            listNode.setSharedKeyFieldName(pvc.OPD);
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(pvc.DAY_CARE);
            listNode.setSharedKeyFieldName("Day Care");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(pvc.EMERGENCY);
            listNode.setSharedKeyFieldName("Emergency");
            lstTemp.add(listNode);

            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<TxtHRegNo_Mdl>> getTxtHRegNo(String vType, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHRegNo_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHRegNo_Mdl> lstTemp = new ArrayList<>();
        TxtHRegNo_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select pr.regDocid as code,pr.recId as registrationID,pu.recId as UHID,pu.patientName,sg.name as departmentName, \n"
                    + "vt.description,vt.category,sgc.name as consultantName \n"
                    + "	from patientRegistration pr \n"
                    + "	left join voucher_Type vt ON pr.v_Type = vt.v_Type \n"
                    + "	left join patientUHID pu ON pu.docId = pr.uhid \n"
                    + "	left join subGroup sg ON pr.departmentCode = sg.subCode \n"
                    + "	left join subGroup sgc ON pr.consultantCode = sgc.subCode \n"
                    + " where vt.category=:vType and pu.patientName is not null and pu.isCancelled = 0 and  pr.isCancelled = 0";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("vType", DmShared.xNull(vType));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHRegNo_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("registrationID")));
                listNode.setPatientName(DmShared.xNull(rsTemp.getString("patientName")));
                listNode.setUhid(DmShared.xNull(rsTemp.getString("UHID")));
                listNode.setConsultantName(DmShared.xNull(rsTemp.getString("consultantName")));
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
    public DmResult<String> getCheckExceedConsumption(String itemCode, String departmentCode, String currentQty, String allowedQty, String VoucherDate) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData("");
        String exceeddQty = "";

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

//            if (DmShared.iNull(ibCode).longValue() != 0) {
//                condition += " AND iStk.ibCode = :ibCode ";
//            }
            /*==============================<< Get header detail >>===============================*/
            sqlQuery = "select stk.itemCode, round(" + DmShared.vNull(allowedQty) + "-(sum(isNull(stk.isuQty,0))+ :currentQty),2) as exceeddQty\n"
                    //            sqlQuery = "select stk.itemCode, round("+DmShared.vNull(allowedQty)+"-(sum(isNull(stk.isuQty,0))+ "+DmShared.vNull(currentQty)+"),2) as exceeddQty\n"
                    + "  from stock stk \n"
                    + "  where stk.v_Date between dateAdd(d,-30,:voucherDate) and :voucherDate and stk.departmentCode=:departmentCode \n"
                    + "	 and stk.itemCode=:itemCode\n"
                    + "  group by stk.itemCode; ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("itemCode", DmShared.iNull(itemCode).toString());
            psTemp.setString("departmentCode", DmShared.iNull(departmentCode).toString());
            psTemp.setDate("voucherDate", DmServer.convertDate(VoucherDate));
            psTemp.setString("currentQty", DmServer.vNull(currentQty));
//            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
//            if (DmShared.iNull(ibCode).longValue() != 0) {
//                psTemp.setString("ibCode", DmShared.iNull(ibCode).toString());
//            }
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                exceeddQty = DmServer.vNull(rsTemp.getString("exceeddQty"), "0.00");
//                 node.setSkuMrp(DmServer.vNull(rsTemp.getString("skuMrp"), DmShared.getNumberFormat(7)));
            }
//            rsTemp.close();
//            psTemp.close();

//            dmrTemp.setData(exceeddQty);
//            cnTemp.close();
//            dmrTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            dmrTemp.setData(exceeddQty);
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
    public DmResult<List<GrdItemDetail_Mdl>> getNewIbCode(GrdItemDetail_Mdl mrTemp, String departmentCode) {
        DmResult<List<GrdItemDetail_Mdl>> dmrTemp = new DmResult();
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        Connection cnTemp = null;
        String sqlQuery;
        List<GrdItemDetail_Mdl> listNode = new ArrayList<>();
        GrdItemDetail_Mdl node;
        ItemProperty_Mdl propertyMdl;
        double balanceQty = 0.0;
        String requiredQty = "";
        double batchStockQty = 0.0;
        String selectedIbCodeSQL = "";
        boolean fillBatch = false;
        dmrTemp.setData(listNode);

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            selectedIbCodeSQL = " SELECT 0 AS ibCode, 0 AS qty ";
            balanceQty = DmShared.vNull(mrTemp.getQuantity()).doubleValue();
            requiredQty = DmShared.vNull(mrTemp.getQuantity()).toString();

            sqlQuery = " SELECT "
                    + "     vib.runningTotal "
                    + "   , vib.stockQuantity "
                    + "   , vib.ibCode "
                    + "   , vib.batchNo"
                    + "   , vib.manufacturingDate "
                    + "   , vib.expiryDate "
                    + "   , vib.saleRate "
                    + "   , vib.staffSaleRate "
                    + "   , vib.mrp "
                    + "   , vib.skuSaleRate "
                    + "   , vib.skuStaffSaleRate "
                    + "   , vib.skuMrp "
                    + "   ,vib.accountSubcode "
                    + "   ,vib.consumptionAc "
                    + " FROM ("
                    + "		SELECT "
                    + "              (ISNULL(ibs.balanceQty,0) - ISNULL(ibt.qty,0)) as stockQuantity  "
                    + "		 ,SUM(ISNULL(ibs.balanceQty,0) - ISNULL(ibt.qty,0)) OVER (ORDER BY ib.expiryDate , (ISNULL(ibs.balanceQty,0) - ISNULL(ibt.qty,0)) DESC, ib.code)  as runningTotal \n"
                    + "		 ,ib.code as ibCode ,ib.batchNo  "
                    + "		 ,ib.manufacturingDate  "
                    + "		 ,ib.expiryDate  "
                    + "		 ,ib.saleRate  "
                    + "		 ,ib.staffSaleRate  "
                    + "		 ,ib.mrp  "
                    + "		 ,ib.skuStaffSaleRate  "
                    + "		 ,ib.skuSaleRate  "
                    + "		 ,ib.skuMrp "
                    + "          ,im.subCode as accountSubcode "
                    + "          ,im.consumptionAc as consumptionAc "
                    + "		 FROM itembatch ib  "
                    + "		 LEFT JOIN itemBatchStock ibs ON ibs.ibCode=ib.code"
                    + "          left join itemmast im on im.code = ib.itemCode  "
                    + "		 LEFT JOIN ( "
                    + "			      SELECT ibTemp.ibCode, SUM(ibTemp.qty) AS qty "
                    + "			      FROM (" + selectedIbCodeSQL + ") AS ibTemp "
                    + "			      GROUP BY ibTemp.ibCode              "
                    + "			   ) AS ibt ON ibt.ibCode = ib.code "
                    + "              WHERE ib.itemCode=:itemCode "
                    + "              AND ISNULL(ibs.departmentCode,:departmentCode) = :departmentCode "
                    + "              AND ISNULL(ibs.site_Code,:site_Code) = :site_Code "
                    + "              AND (ISNULL(ibs.balanceQty,0) - ISNULL(ibt.qty,0)) > 0 "
                    + "	) as vib"
                    + " WHERE ISNULL(vib.runningTotal,0) < ISNULL(vib.stockQuantity,0) + :requiredQty ";

            sqlQuery += " ORDER BY vib.expiryDate , vib.stockQuantity DESC ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("itemCode", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            psTemp.setString("requiredQty", DmShared.xNull(requiredQty));
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp.setString("departmentCode", DmShared.iNull(departmentCode).toString());
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                if (balanceQty != 0) {
                    fillBatch = true;

                    node = new GrdItemDetail_Mdl();
                    node.setSharedKeyFieldCode(DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                    node.setSharedKeyFieldName(DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                    node.setRequisitionV_SNo(DmShared.xNull(mrTemp.getRequisitionV_SNo()));
                    node.setQuantity(DmServer.vNull(mrTemp.getQuantity()));
                    node.setSkuQtyForRate(DmServer.vNull(mrTemp.getSkuQtyForRate()));
                    node.setSkuDecimalPlaces(DmShared.iNull(mrTemp.getSkuDecimalPlaces()).toString());
                    node.setSkuName(DmShared.xNull(mrTemp.getSkuName()));
                    node.setRequisitionDocId(DmShared.xNull(mrTemp.getRequisitionDocId()));
                    node.setRequisitionNo(DmShared.xNull(mrTemp.getRequisitionNo()));

                    //======================< Set Item Properties >======================                
                    node.setColourCode(DmShared.xNull(mrTemp.getColourCode()));
                    node.setColourName(DmShared.xNull(mrTemp.getColourName()));
                    node.setSharedKeyFieldCode(DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                    node.setSharedKeyFieldName(DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                    node.setSizeCode(DmShared.xNull(mrTemp.getSizeCode()));
                    node.setSizeName(DmShared.xNull(mrTemp.getSizeName()));
                    node.setSkuQtyForRate(DmServer.vNull(mrTemp.getSkuQtyForRate()));

                    if (fillBatch) {

                        if (DmShared.vNull(rsTemp.getString("stockQuantity")).doubleValue() < balanceQty) {
                            node.setQuantity(DmServer.vNull(rsTemp.getString("stockQuantity")));
                            node.setQuantity(DmServer.vNull(rsTemp.getString("stockQuantity"), DmShared.getNumberFormat(DmShared.iNull(mrTemp.getSkuDecimalPlaces()).intValue())));
                            balanceQty = balanceQty - DmShared.vNull(node.getQuantity()).doubleValue();
                            batchStockQty = 0;
                        } else if (DmShared.vNull(rsTemp.getString("stockQuantity")).doubleValue() == balanceQty) {
                            node.setQuantity(DmServer.vNull(rsTemp.getString("stockQuantity")));
                            node.setQuantity(DmServer.vNull(rsTemp.getString("stockQuantity"), DmShared.getNumberFormat(DmShared.iNull(mrTemp.getSkuDecimalPlaces()).intValue())));
                            balanceQty = 0;
                            batchStockQty = 0;
                        } else {
                            node.setQuantity(DmServer.vNull(balanceQty));
                            node.setQuantity(DmServer.vNull(balanceQty, DmShared.getNumberFormat(DmShared.iNull(mrTemp.getSkuDecimalPlaces()).intValue())));
                            batchStockQty = DmShared.vNull(rsTemp.getString("stockQuantity")).doubleValue() - balanceQty;
                            balanceQty = 0;
                        }

                        node.setIbCode(DmShared.iNull(rsTemp.getString("ibCode")).toString());
                        node.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                        node.setManufacturingDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));
                        node.setExpiryDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                        node.setMrp(DmServer.vNull(rsTemp.getString("mrp"), "0.00"));
                        node.setSkuMrp(DmServer.vNull(rsTemp.getString("skuMrp")));
                        node.setIsubCode(DmShared.xNull(rsTemp.getString("accountSubcode")));
                        node.setGrIrConsumptionAc(DmShared.xNull(rsTemp.getString("consumptionAc")));

                        selectedIbCodeSQL += " UNION ALL SELECT " + node.getIbCode() + " AS ibCode, " + DmServer.vNull(node.getQuantity()) + " AS qty ";

                        if (DmShared.vNull(node.getQuantity()) > 0) {
                            double qty = DmShared.vNull(node.getQuantity()) / DmShared.vNull(node.getSkuQtyForRate());
                            node.setQuantity(DmServer.vNull(qty, DmShared.getNumberFormat(DmShared.iNull(node.getSkuDecimalPlaces()).intValue())));

                        }

                    } else {

                        node.setQuantity(DmServer.vNull(node.getQuantity()));
                        balanceQty = 0;
                        batchStockQty = 0;

                        selectedIbCodeSQL += " UNION ALL SELECT " + node.getIbCode() + " AS ibCode, " + DmServer.vNull(node.getQuantity()) + " AS qty ";

                        node.setIbCode(DmShared.iNull(mrTemp.getIbCode()).toString());
                        node.setBatchNo(DmShared.xNull(mrTemp.getBatchNo()));
                        node.setManufacturingDate(DmShared.dNull(mrTemp.getManufacturingDate()));
                        node.setExpiryDate(DmShared.dNull(mrTemp.getExpiryDate()));

                        node.setMrp(DmServer.vNull(mrTemp.getMrp(), "0.00"));
                        node.setSkuMrp(DmServer.vNull(mrTemp.getSkuMrp()));

                        if (DmShared.vNull(node.getQuantity()) > 0) {
                            double qty = DmShared.vNull(node.getQuantity()) / DmShared.vNull(mrTemp.getSkuQtyForRate());
                            node.setQuantity(DmServer.vNull(qty, DmShared.getNumberFormat(DmShared.iNull(mrTemp.getSkuDecimalPlaces()).intValue())));
                        }
                    }

                    listNode.add(node);

                }
            }
            rsTemp.close();
            psTemp.close();

            if (balanceQty != 0) {
                node = new GrdItemDetail_Mdl();

                node = new GrdItemDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                node.setSharedKeyFieldName(DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                node.setRequisitionV_SNo(DmShared.xNull(mrTemp.getRequisitionV_SNo()));
                node.setQuantity(DmServer.vNull(mrTemp.getQuantity()));
                node.setSkuQtyForRate(DmServer.vNull(mrTemp.getSkuQtyForRate()));
                node.setSkuDecimalPlaces(DmShared.iNull(mrTemp.getSkuDecimalPlaces()).toString());
                node.setSkuName(DmShared.xNull(mrTemp.getSkuName()));
                node.setRequisitionDocId(DmShared.xNull(mrTemp.getRequisitionDocId()));
                node.setRequisitionNo(DmShared.xNull(mrTemp.getRequisitionNo()));

                //======================< Set Item Properties >======================                
                node.setColourCode(DmShared.xNull(mrTemp.getColourCode()));
                node.setColourName(DmShared.xNull(mrTemp.getColourName()));
                node.setSharedKeyFieldCode(DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                node.setSharedKeyFieldName(DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                node.setSizeCode(DmShared.xNull(mrTemp.getSizeCode()));
                node.setSizeName(DmShared.xNull(mrTemp.getSizeName()));
                node.setSkuQtyForRate(DmServer.vNull(mrTemp.getSkuQtyForRate()));

                node.setQuantity(DmServer.vNull(balanceQty));
                balanceQty = 0;

                selectedIbCodeSQL += " UNION ALL SELECT " + node.getIbCode() + " AS ibCode, " + DmServer.vNull(node.getQuantity()) + " AS qty ";
                if (DmShared.vNull(node.getQuantity()) > 0) {
                    double qty = DmShared.vNull(node.getQuantity()) / DmShared.vNull(node.getSkuQtyForRate());
                    node.setQuantity(DmServer.vNull(qty, DmShared.getNumberFormat(DmShared.iNull(node.getSkuDecimalPlaces()).intValue())));
                }

                listNode.add(node);
            }

            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }

        return dmrTemp;
    }

}
