package aarogya.inventory.indent.server;

import aarogya.inventory.indent.shared.TxtHRequisition_Mdl;
import aarogya.inventory.indent.shared.GrdItemDetail_Mdl;
import aarogya.inventory.indent.shared.Find_Mdl;
import aarogya.inventory.indent.shared.DefaultValue_Mdl;
import aarogya.inventory.indent.shared.Indent_Srv;
import aarogya.inventory.indent.shared.Indent_Vld;
import aarogya.inventory.indent.shared.Indent_Mdl;
import aarogya.inventory.indent.shared.TxtHItemName_Mdl;
import aarogya.inventory.indent.shared.ItemWithRequisition_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.D365_To_His_DataSync.server.IndentSync;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import aarogya.selfToolkit.modifyReason.server.ModifyReason_Server;
import aarogya.selfToolkit.stStatic.server.STServer;
import dataman.dmToolkit.walkThrough.server.WalkThrough_Server;
import dataman.dmToolkit.walkThrough.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import aarogya.selfToolkit.stStatic.server.STApprovals;

public class Indent_SrvImpl extends DmRSSPrivate implements Indent_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String vCatIND = "'" + pvc.INVENTORY_INDENT + "'";
    private final String vCatReq = "'" + pvc.INVENTORY_REQUISITION + "'";
    private final String voucherTypefieldName = "mid.V_Type", voucherDateFieldName = "mid.V_Date";

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission) {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);
        DmPreparedStatement psTemp = null;
        String sqlQuery = "", sqlCondition = "";

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "SELECT docid From Indent mid "
                    + "         LEFT JOIN voucher_Type vt ON vt.v_Type=mid.v_Type "
                    + "         where vt.category= " + vCatIND + " and mid.site_Code = :site_Code and mid.v_prefix = :vPrefix ";

            sqlCondition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypefieldName, userPermission);
            if (!sqlCondition.equalsIgnoreCase("")) {
                sqlQuery += " and " + sqlCondition;
            }
            sqlQuery += " Order By mid.v_Date Desc,vt.short_Name,mid.v_No Desc,mid.recId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp.setString("vPrefix", DmServer.getSsnVoucherPrefix(this));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docid")));
                lstNavigator.add(lstNavigatorTmp);
            }
            dmrTemp.setStatus(true);
            rsTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
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
    public DmResult<Indent_Mdl> getMoveRec(String strKey) {
        DmResult<Indent_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        Indent_Mdl mrTemp = new Indent_Mdl();
        dmrTemp.setData(mrTemp);
        GrdItemDetail_Mdl node;
        dmrTemp.setData(mrTemp);

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT mind.docId, mind.v_Type, mind.recId, mind.v_Date, mind.departmentCode,mind.todepartmentCode,vt.description,vt.v_Type,  "
                    + " sdm.name AS deptName,sdm.subcode AS deptCode,mind.remark, "
                    + " mind.indentorCode, sg.name AS indentor,mind.preparedBy,mind.preparedDate, mind.u_name, mind.u_EntDt, "
                    + " mind.u_EntDt_LatestLine,mind.wtaCode,mind.approvedBy, "
                    + " mind.v_No,mind.recIdPrefix ,mind.approvalDateTime,sdmto.name as toDeptName, "
                    + " prcpl.docID as procurementDocID,prcpl.recID as procurementNo "
                    + " FROM indent mind  "
                    + " LEFT JOIN subgroup sdm ON mind.departmentCode = sdm.subcode  "
                    + " LEFT JOIN subgroup sdmto ON mind.toDepartmentCode = sdmto.subcode  "
                    + " LEFT JOIN subgroup sg ON mind.indentorCode = sg.subCode  "
                    + " LEFT JOIN voucher_type vt ON mind.v_Type = vt.v_Type  "
                    + " LEFT JOIN procurementPlanner prcpl on prcpl.indentDocID = mind.docID "
                    + " WHERE mind.docId = :docId ;";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", strKey);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.setDepartmentTag(DmShared.xNull(rsTemp.getString("deptCode")));
                mrTemp.setDepartmentText(DmShared.xNull(rsTemp.getString("deptName")));
                mrTemp.setToDepartmentTag(DmShared.xNull(rsTemp.getString("todepartmentCode")));
                mrTemp.setToDepartmentText(DmShared.xNull(rsTemp.getString("toDeptName")));
                mrTemp.setDtIndent(DmShared.dNull(rsTemp.getDate("v_Date")));
                mrTemp.setIndentTypeTag(DmShared.xNull(rsTemp.getString("v_Type")));
                mrTemp.setIndentTypeText(DmShared.xNull(rsTemp.getString("description")));
                mrTemp.setIndentorTag(DmShared.xNull(rsTemp.getString("indentorCode")));
                mrTemp.setIndentorText(DmShared.xNull(rsTemp.getString("indentor")));
                mrTemp.setWtaCode(DmShared.xNull(rsTemp.getString("wtaCode")));
                mrTemp.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                mrTemp.setProcurementPlanRecID(DmShared.xNull(rsTemp.getString("procurementNo")));
                mrTemp.setProcurementPlanDocID(DmShared.xNull(rsTemp.getString("procurementDocID")));
                if (rsTemp.getTimestamp("u_EntDt_LatestLine") != null) {
                    mrTemp.setUserEntDtLatestLine(String.valueOf(rsTemp.getTimestamp("u_EntDt_LatestLine").getTime()));
                }

                mrTemp.setvNo(DmShared.xNull(rsTemp.getString("v_No")));
                mrTemp.getRecid().setPrefix(DmShared.xNull(rsTemp.getString("recIdPrefix")));
                mrTemp.getRecid().setCounter(DmShared.iNull(rsTemp.getString("v_No")).longValue());
                mrTemp.getRecid().setRecIdValue(DmShared.xNull(rsTemp.getString("recId")));

                mrTemp.setSharedPreparedBy(getUserWithDateTime(DmShared.xNull(rsTemp.getString("preparedBY")), rsTemp.getTimestamp("preparedDate")));
                mrTemp.setSharedModifiedBy(getUserWithDateTime(DmShared.xNull(rsTemp.getString("u_name")), rsTemp.getTimestamp("u_EntDt")));
                mrTemp.setApprovedBy(getUserWithDateTime(DmShared.xNull(rsTemp.getString("approvedBy")), rsTemp.getTimestamp("approvalDateTime")));
            }
            psTemp.close();
            rsTemp.close();

            sqlQuery = "SELECT mid.docId,mid.v_SNo,mid.itemCode, mm.name as itemName, mid.reqQty, mid.reqDate, mid.remark, mid.isStatus, "
                    + " ump.unit as skuName,mrq.docId reqCode,mrq.recId  AS reqName,mid.colourCode, icm.name as colourName,"
                    + " mrqd.colourCode as requisitionColorCode,mrqd.sizeCode as requisitionSizeCode, icmd.name as requisitionColor, ismd.name as requisitionSize,"
                    + " mmd.name as requisitionItem,mid.requisitionV_SNo,mrq.v_Date as requisitionDate, "
                    + " mid.sizeCode as sizeCode, ism.name sizeName, mm.propertyList,IsNull(ump.decimalPlaces,0) as decimalPlaces "
                    + " , imm.companyName as itemMakeName, mid.itemMakeCode as itemMakeCode"
                    + " FROM indentDetail mid  "
                    + " LEFT JOIN itemMast mm ON mm.code = mid.itemCode "
                    + " LEFT JOIN unitMast ump ON ump.code = mm.sku "
                    + " LEFT JOIN itemColourMast icm ON icm.code = mid.colourCode "
                    + " LEFT JOIN itemSizeMast ism ON ism.code = mid.sizeCode "
                    + " left join itemMakeMast as imm ON imm.code = mid.itemMakeCode"
                    + " LEFT JOIN requisition mrq ON mrq.docId = mid.requisitionDocId "
                    + " LEFT JOIN requisitionDetail mrqd ON mrqd.v_SNo = mid.requisitionV_SNo and mrqd.DocId = mid.requisitionDocId "
                    + " LEFT JOIN itemColourMast icmd ON icmd.code = mrqd.colourCode  "
                    + " LEFT JOIN itemSizeMast ismd ON ismd.code = mrqd.sizeCode  "
                    + " left join itemMakeMast as imm1 ON imm1.code = mrqd.itemMakeCode "
                    + " LEFT JOIN itemMast mmd ON mmd.code = mrqd.itemCode  "
                    + " where mid.docid=:docid ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docid", strKey);
            rsTemp = psTemp.executeQuery();
            boolean flag = false;
            while (rsTemp.next()) {
                node = new GrdItemDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                node.setDocId(DmShared.xNull(rsTemp.getString("docId")));
                node.setvSno(DmShared.xNull(rsTemp.getString("v_SNo")));
                node.setRemarks(DmShared.xNull(rsTemp.getString("remark")));
                node.setUnitName(DmShared.xNull(rsTemp.getString("skuName")));
                node.setReqDate(DmShared.dNull(rsTemp.getDate("reqDate")));
                node.setRequisitionDate(DmShared.dNull(rsTemp.getDate("requisitionDate")));
                node.setIsStatus(DmShared.xNull(rsTemp.getString("isStatus")).equalsIgnoreCase("0") ? "open"
                        : (DmShared.xNull(rsTemp.getString("isStatus")).equalsIgnoreCase("1") ? "cancelled"
                        : (DmShared.xNull(rsTemp.getString("isStatus")).equalsIgnoreCase("2") ? "completed" : "")));
                node.setQty(DmServer.vNull(rsTemp.getString("reqQty")));
                node.setRequisitionDocId(DmShared.xNull(rsTemp.getString("reqCode")));
                node.setRequisitionNo(DmShared.xNull(rsTemp.getString("reqName")));
                node.setItemProperty(DmShared.xNull(rsTemp.getString("propertyList")));
                node.setColorCode(DmShared.xNull(rsTemp.getString("colourCode")));
                node.setColor(DmShared.xNull(rsTemp.getString("colourName")));
                node.setSizeCode(DmShared.xNull(rsTemp.getString("sizeCode")));
                node.setSize(DmShared.xNull(rsTemp.getString("sizeName")));
                node.setItemMakeCode(DmShared.xNull(rsTemp.getString("itemMakeCode")));
                node.setItemMakeName(DmShared.xNull(rsTemp.getString("itemMakeName")));

                node.setRequisitionFor(DmShared.xNull(rsTemp.getString("requisitionItem")));
                node.setRequisitionVsNo(DmShared.xNull(rsTemp.getString("requisitionV_SNo")));
                node.setDecimalPlace(DmShared.xNull(rsTemp.getString("decimalPlaces")));

                ItemProperty_Mdl ipm = new ItemProperty_Mdl();
                ipm.setColourCode(DmShared.xNull(rsTemp.getString("colourCode")));
                ipm.setColourName(DmShared.xNull(rsTemp.getString("colourName")));
                ipm.setSizeCode(DmShared.xNull(rsTemp.getString("sizeCode")));
                ipm.setSizeName(DmShared.xNull(rsTemp.getString("sizeName")));
                ipm.setItemMakeCode(DmShared.xNull(rsTemp.getString("itemMakeCode")));
                ipm.setItemMakeName(DmShared.xNull(rsTemp.getString("itemMakeName")));

                node.setProperties(ipm.getPropertiesCsv());

                mrTemp.getGrdItemDetails().add(node);
                if (!flag && !DmShared.xNull(rsTemp.getString("reqCode")).equalsIgnoreCase("")) {
                    mrTemp.setRequisitionTag(DmShared.xNull(rsTemp.getString("reqCode")));
                    mrTemp.setRequisitionText(DmShared.xNull(rsTemp.getString("reqName")));
                    flag = true;
                }
            }

            psTemp.close();
            rsTemp.close();
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

    private WalkThroughDefault_Mdl getWalkThroughFor(Indent_Mdl mrTemp) {
        WalkThroughDefault_Mdl walkThroughDefault = new WalkThroughDefault_Mdl();

        walkThroughDefault.setDepartmentCode(mrTemp.getDepartmentTag());
        walkThroughDefault.setSiteCode(DmServer.getSsnSiteCode(this));
        walkThroughDefault.setUserName(DmServer.getSsnUserName(this));
        walkThroughDefault.setValueUpto("");
        walkThroughDefault.setvType(mrTemp.getIndentTypeTag());

        return walkThroughDefault;
    }

    @Override
    public DmResult<String> saveRecord(Indent_Mdl mrTemp, String voucherPrefix, boolean isApprove) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery, vNo = "", lstItemRequisitionQtyChange = "";
        dmrTemp.setData("");
        try {
            Indent_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            DmServer.checkLockDate(DmServer.getSsnSiteCode(this), DmServer.getSsnVoucherPrefix(this), cnTemp);
            if (!DmShared.xNull(mrTemp.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {

                STServer.chkIfDocumentUsed("indentDocId", "pOrderIndentDetail", mrTemp.getSharedKeyFieldCode(), cnTemp);
                STServer.chkIfDocumentUsed("indentDocId", "enquiryIndentDetail", mrTemp.getSharedKeyFieldCode(), cnTemp);
                STServer.chkIfDocumentUsed("indentDocId", "quotationIndentDetail", mrTemp.getSharedKeyFieldCode(), cnTemp);

                sqlQuery = "select count(*) as cnt from  indent where docid = :docId and ISNULL(approvedBy,'') <> '' ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (DmShared.vNull(rsTemp.getString("cnt")).doubleValue() > 0) {
                        throw new DmException(0, "Record is approved. You can not edit this record.");
                    }
                }
                rsTemp.close();
                psTemp.close();

            }

            if (mrTemp.getIsConsentForReqQtyChng().equalsIgnoreCase("0")) {
                lstItemRequisitionQtyChange = chkLstItemRequisitionQtyChange(mrTemp, cnTemp);
            }
            if (!DmShared.xNull(lstItemRequisitionQtyChange).equalsIgnoreCase("")) {
                dmrTemp.setErrorNumber(12);
                throw new UnsupportedOperationException(lstItemRequisitionQtyChange);
            }

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(mrTemp.getIndentTypeTag()), voucherPrefix, cnTemp, this));
                mrTemp.setRecid(DmServer.getRecId("indent", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecid(), mrTemp.getDtIndent(), DmShared.xNull(mrTemp.getIndentTypeTag()), voucherPrefix, cnTemp, this, true));
                mrTemp.setWtaCode(WalkThrough_Server.applyWalkThrough(getWalkThroughFor(mrTemp), DmServer.getSsnUserName(this), DmShared.xNull(mrTemp.getIndentTypeTag()), mrTemp.getSharedKeyFieldCode(), cnTemp, this));
            } else {
                DmServer.getRecId("indent", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecid(), mrTemp.getDtIndent(), DmShared.xNull(mrTemp.getIndentTypeTag()), voucherPrefix, cnTemp, this, true);
            }
            vNo = DmShared.iNull(mrTemp.getRecid().getCounter()).toString();

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            sqlQuery = "SELECT u_EntDt_LatestLine FROM indent WHERE docId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getTimestamp("u_EntDt_LatestLine") != null) {
                    if (DmShared.iNull(mrTemp.getUserEntDtLatestLine()).longValue() != rsTemp.getTimestamp("u_EntDt_LatestLine").getTime()) {
                        throw new DmException(0, "Record has been changed, can't update record, refresh record then update");
                    }
                }
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "select docId "
                    + "from indentDetail "
                    + "where docId = :docId and isNull(orderedQty,0) <> 0 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                throw new Exception("Document is in use. you can not unapprove.");
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Check items for restricted department >>====================================*/
            STServer.checkItemsRestrictedDepartment(mrTemp.getLineItemCodesCsv(), mrTemp.getDepartmentTag(), mrTemp.getDepartmentText(), cnTemp);

            /*==============================<< Check items for Requisition item change >>====================================*/
            if (!DmShared.xNull(mrTemp.getReqItemChangeCondition()).equalsIgnoreCase("")) {
                sqlQuery = "SELECT COUNT(*) AS totalInvalidRecords FROM requisitionDetail rqd " + DmShared.xNull(mrTemp.getReqItemChangeCondition());
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (DmShared.iNull(rsTemp.getString("totalInvalidRecords")).intValue() > 0) {
                        throw new DmException(0, "Requisition has been changed, can't save record, refresh record then save");
                    }
                }
                rsTemp.close();
                psTemp.close();
            }

            /*==============================<< Check items for REQUISITION approval change >>====================================*/
            sqlQuery = "SELECT COUNT(*) AS totalInvalidRecords FROM requisition rq where rq.docid='" + DmShared.xNull(mrTemp.getRequisitionTag()) + "' and ISNULL(rq.approvedBy,'')='' ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("totalInvalidRecords")).intValue() > 0) {
                    throw new DmException(0, "Requisition has been UnApproved, can't save record, refresh record then save");
                }
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< SAVE & UPDATE >>====================================*/
            if (blnAdd) {
                DmServer.checkBackwordVoucherDateTime("indent", "v_Date", "", mrTemp.getDtIndent(), "", cnTemp);

                sqlQuery = "INSERT INTO indent (docId, v_Type,v_No, recId, v_Date, v_Prefix, departmentCode,toDepartmentCode, indentorCode, "
                        + "  site_Code, preparedBy, preparedDate, u_EntDt_LatestLine,wtaCode,remark,recIdPrefix) "
                        + "VALUES (:docId, :v_Type,:v_No,  :recId, :v_Date, :v_Prefix, :departmentCode,:toDepartmentCode, :indentorCode, "
                        + "    :site_Code, :preparedBy, :preparedDate, :uEntDt, :wtaCode, :remark, :recIdPrefix) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("docId", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("v_Type", DmShared.xNull(mrTemp.getIndentTypeTag()));
                psTemp.setString("v_No", DmShared.iNull(vNo).toString());
                psTemp.setString("recId", mrTemp.getRecid().getRecIdValue());
                psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getDtIndent()));
                psTemp.setString("v_Prefix", voucherPrefix);
                psTemp.setString("departmentCode", DmShared.foriegnKey(mrTemp.getDepartmentTag()));
                psTemp.setString("indentorCode", DmShared.foriegnKey(mrTemp.getIndentorTag()));
                psTemp.setString("wtaCode", DmShared.foriegnKey(mrTemp.getWtaCode()));
                psTemp.setString("site_code", DmServer.getSsnSiteCode(this));
                psTemp.setString("preparedBy", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("preparedDate", DmServer.getServerTimestamp());
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getRecid().getPrefix()));
                if (DmShared.iNull(mrTemp.getToDepartmentTag()).longValue() > 0) {
                    psTemp.setString("toDepartmentCode", DmShared.foriegnKey(mrTemp.getToDepartmentTag()));
                } else {
                    psTemp.setString("toDepartmentCode", DmShared.foriegnKey(mrTemp.getDepartmentTag()));
                }
            } else {
                sqlQuery = "UPDATE indent SET"
                        + "  departmentCode = :departmentCode ,"
                        + "  toDepartmentCode = :toDepartmentCode ,"
                        + "  indentorCode = :subCode ,"
                        + "  u_EntDt = :uEntDt ,"
                        + "  u_EntDt_LatestLine= :uEntDt ,"
                        + "  u_name = :u_name, "
                        + "  remark = :remark, "
                        + "  recId = :recId, "
                        + "  v_No = :v_No, "
                        + "  v_Date = :v_Date "
                        + "  WHERE docId = :docId ;";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("u_name", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("departmentCode", DmShared.foriegnKey(mrTemp.getDepartmentTag()));
                psTemp.setString("toDepartmentCode", DmShared.foriegnKey(mrTemp.getToDepartmentTag()));
                psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getIndentorTag()));
                psTemp.setString("v_No", DmShared.iNull(mrTemp.getvNo()).toString());
                psTemp.setString("recId", mrTemp.getRecid().getRecIdValue());
                psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getDtIndent()));
                psTemp.setString("docId", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
            }
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Update Procurement Planner >>====================================*/
            if (!DmShared.xNull(mrTemp.getProcurementPlanDocID()).trim().equalsIgnoreCase("")) {
                sqlQuery = "update procurementPlanner set indentDocID=:indentDocID WHERE docId = :procurementDocID ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("indentDocID", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("procurementDocID", mrTemp.getProcurementPlanDocID());
                psTemp.executeUpdate();
                psTemp.close();
            }

            /*==============================<< Manage Requisition (Indent Qty) >>====================================*/
            manageRequisionIndentQty(DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString(), true, cnTemp);

            sqlQuery = "DELETE FROM indentDetail WHERE docId = :docId  and isStatus not in ('1','2') ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< ********** >>====================================*/
            int vSNo = 0, previous_VSno = 0;
            for (int i = 0; i < mrTemp.getGrdItemDetails().size(); i++) {
                GrdItemDetail_Mdl detail = mrTemp.getGrdItemDetails().get(i);
                if (!DmShared.xNull(detail.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                    previous_VSno = DmShared.iNull(detail.getvSno()).intValue();
                    ++vSNo;
                    if (!DmShared.xNull(detail.getIsStatus()).equalsIgnoreCase("cancelled") && !DmShared.xNull(detail.getIsStatus()).equalsIgnoreCase("completed")) {
                        detail.setvSno(String.valueOf(vSNo));
                        sqlQuery = "INSERT INTO indentDetail (docId, v_SNo, itemCode, reqQty, aprQty,"
                                + " reqDate, remark, site_code,requisitionDocId,colourCode,sizeCode,"
                                + " itemMakeCode ,requisitionV_SNo ,isStatus) "
                                + " VALUES (:docId, :v_SNo, :itemCode, :reqQty, :aprQty,"
                                + " :reqDate, :remark, :site_code,:requisitionDocId,:colourCode,:sizeCode, "
                                + " :itemMakeCode ,:requisitionV_SNo,:isStatus)";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docId", mrTemp.getSharedKeyFieldCode());
                        psTemp.setInt("v_SNo", DmShared.iNull(detail.getvSno()).intValue());
                        psTemp.setString("itemCode", DmShared.foriegnKey(detail.getSharedKeyFieldCode()));
                        psTemp.setString("reqQty", DmShared.xNull(detail.getQty()));
                        psTemp.setString("aprQty", DmShared.xNull(detail.getQty()));
                        psTemp.setDate("reqDate", DmServer.convertDate(detail.getReqDate()));
                        psTemp.setString("requisitionDocId", DmShared.foriegnKey(detail.getRequisitionDocId()));
                        psTemp.setString("remark", DmShared.xNull(detail.getRemarks()));
                        psTemp.setString("colourCode", DmShared.foriegnKey(mrTemp.getGrdItemDetails().get(i).getColorCode()));
                        psTemp.setString("sizeCode", DmShared.foriegnKey(mrTemp.getGrdItemDetails().get(i).getSizeCode()));
                        psTemp.setString("itemMakeCode", DmShared.foriegnKey(mrTemp.getGrdItemDetails().get(i).getItemMakeCode()));
                        psTemp.setString("isStatus", "0");
                        psTemp.setString("site_code", DmShared.xNull(DmServer.getSsnSiteCode(this)));
                        psTemp.setString("requisitionV_SNo", DmShared.foriegnKey(detail.getRequisitionVsNo()));
                        psTemp.executeUpdate();
                        psTemp.close();
                    } else {
                        if (DmShared.xNull(detail.getIsStatus()).equalsIgnoreCase("cancelled") && previous_VSno > 0) {
                            //Update New v_SNo For Cancelled record
                            detail.setvSno(String.valueOf(vSNo));
                            sqlQuery = "UPDATE indentDetail "
                                    + " SET v_SNo = :v_SNo "
                                    + " WHERE docid = :docId"
                                    + " AND  v_SNo = :previous_VSno ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                            psTemp.setInt("v_SNo", DmShared.iNull(detail.getvSno()).intValue());
                            psTemp.setInt("previous_VSno", previous_VSno);
                            psTemp.executeUpdate();
                            psTemp.close();
                        }
                    }
                }

            }

            /*==============================<< Manage Requisition (Indent Qty) >>====================================*/
            manageRequisionIndentQty(DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString(), false, cnTemp);

            if (DmShared.iNull(mrTemp.getWtaCode()).longValue() == 0 && isApprove) {
//                STApprovals.indentApproval(WalkThroughEnum.ACCEPT, mrTemp.getSharedKeyFieldCode(), DmServer.getSsnUserName(this), cnTemp);
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
                STServer.sendMail(mrTemp.getSharedKeyFieldCode(), mrTemp.getIndentTypeTag(), this);
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
    public DmResult<String> deleteRecord(String strKey) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String strSQL, wtaCode = "";
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            DmServer.checkLockDate(DmServer.getSsnSiteCode(this), DmServer.getSsnVoucherPrefix(this), cnTemp);
            strSQL = "select approvedBy,wtaCode FROM indent WHERE  docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                wtaCode = DmShared.xNull(rsTemp.getString("wtaCode"));
                if (!DmShared.xNull(rsTemp.getString("approvedBy")).equalsIgnoreCase("")) {
                    throw new DmException(0, "Record is approved. You can't delete this record.");
                }
            }
            rsTemp.close();
            psTemp.close();

            STServer.chkIfDocumentUsed("indentDocId", "pOrderIndentDetail", strKey, cnTemp);
            STServer.chkIfDocumentUsed("indentDocId", "enquiryIndentDetail", strKey, cnTemp);
            STServer.chkIfDocumentUsed("indentDocId", "quotationIndentDetail", strKey, cnTemp);

            /*==============================<< Manage Requisition (Indent Qty) >>====================================*/
            manageRequisionIndentQty(DmShared.iNull(strKey).toString(), true, cnTemp);

            WalkThrough_Server.deleteWalkThrough(wtaCode, cnTemp);
            ModifyReason_Server.delete(strKey, cnTemp);

            strSQL = "Delete From indentDetail Where docid= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", strKey);
            psTemp.executeUpdate();
            psTemp.close();

            strSQL = "Delete From indent Where docid= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", strKey);
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
    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "", sqlCondition = "";

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " SELECT	mid.docId,mid.recId as name, mid.recId as recId,mid.v_No as v_No ,mid.v_Date as v_Date,    "
                    + "        sg.name as indentorName,mrq.recId AS reqName,mid.approvedBy as approvedBy,    "
                    + "        format(mid.approvalDateTime,'dd-MMM-yyyy HH.mm') as approvalDateTime,     "
                    + "        sdp.name as departmentName,vt.short_Name as  short_Name     "
                    + "        FROM indent mid     "
                    + "        left join ("
                    + "             select id.docId,max(id.requisitionDocId) as requisitionDocId   "
                    + "             from indentDetail id    "
                    + "		    group by id.docId) as id on id.DocId = mid.docId  "
                    + "        LEFT JOIN voucher_Type vt ON vt.v_Type = mid.v_Type     "
                    + "        LEFT JOIN subGroup sg ON sg.subCode = mid.indentorCode      "
                    + "        LEFT JOIN subgroup sdp ON sdp.subcode = mid.departmentCode     "
                    + "        LEFT JOIN requisition mrq ON mrq.docId = id.requisitionDocId  "
                    + " where vt.category=" + vCatIND + " and mid.site_Code =  :site_Code and mid.v_prefix=:v_prefix ";
            sqlCondition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypefieldName, userPermission);
            if (!sqlCondition.equalsIgnoreCase("")) {
                sqlQuery += "and " + sqlCondition;
            }
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("v_prefix", DmServer.getSsnVoucherPrefix(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setDepartment(DmShared.xNull(rsTemp.getString("departmentName")));
                listNode.setApproval(DmShared.xNull(rsTemp.getString("approvedBy")) + " " + DmShared.xNull(rsTemp.getString("approvalDateTime")));
                listNode.setIndentDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setIndentType(DmShared.xNull(rsTemp.getString("short_Name")));
                listNode.setIndentor(DmShared.xNull(rsTemp.getString("indentorName")));
                listNode.setRequisition(DmShared.xNull(rsTemp.getString("reqName")));
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
    public DmResultPageing<List<TxtHItemName_Mdl>> getTxtHItemName(String v_Type, String itemSku, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHItemName_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHItemName_Mdl> lstTemp = new ArrayList<>();
        TxtHItemName_Mdl node;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, condition = "", itemCatFilteration = "";
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            itemCatFilteration = DmServer.getItemCatFilteration(v_Type, cnTemp);

            if (!DmShared.xNull(itemCatFilteration).equalsIgnoreCase("")) {
                condition += " AND mm.itemCategoryCode IN (" + itemCatFilteration + ")";
            }
            if (!DmShared.xNull(itemSku).equalsIgnoreCase("")) {
                condition += " And ump.unit = '" + itemSku + "'";
            }
            sqlQuery = " SELECT mm.code, mm.name, mm.manualCode,mm.propertyList,mm.isBestBefore, "
                    + "  ump.unit as skuName,IsNull(ump.decimalPlaces,0) as decimalPlaces  "
                    + "  FROM itemMast mm "
                    + "  LEFT JOIN itemSiteList sl ON sl.code = mm.code"
                    + "  LEFT JOIN unitMast ump ON ump.code = mm.sku "
                    + "  WHERE (mm.filterItemType = :filterItemType OR mm.isCommon = 1) "
                    + "  AND mm.isActive = 1"
                    + "  AND sl.site_Code = :site_Code" + condition;
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new TxtHItemName_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                node.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                node.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                node.setDecimalPlace(DmShared.xNull(rsTemp.getString("decimalPlaces")));
                node.setItemProperty(DmShared.xNull(rsTemp.getString("propertyList")));
                node.setIsBestBefore(DmShared.xNull(rsTemp.getString("isBestBefore")));
                lstTemp.add(node);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHIndentType(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT V_type, Description FROM Voucher_Type VT WHERE Category = " + vCatIND + " ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("V_type")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Description")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean allDepartment, boolean blnToEndOfFile) {
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

            if (!allDepartment) {
                if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                    condition = " AND dm.subCode IN (" + DmServer.getParamList(":department", STServer.getSsnDepartmentCodeList(this)) + ")";
                }
            }

            sqlWithParam.setSQL("SELECT dm.subCode AS code, sg.name "
                    + "	FROM departmentMast dm"
                    + "	LEFT JOIN subGroup sg ON sg.subCode = dm.subCode"
                    + "	LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature"
                    + " Left join subGroupSiteList as sgsl on sg.subCode=sgsl.subCode "
                    + "	WHERE sgn.manualCode IN ('CM','DP','PR') "
                    + " and   sgsl.site_Code = :siteCode "
                    + " and sg.isActive = 1 " + condition);
            if (!allDepartment) {
                if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                    sqlWithParam.setStringList("department", STServer.getSsnDepartmentCodeList(this));
                }
            }

            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHIndentor(String v_Type, DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT sg.subCode,sg.name "
                    + "  FROM subgroup sg "
                    + "  LEFT JOIN subgroupsitelist SL ON Sg.subcode = SL.subcode "
                    + " LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature"
                    + "  WHERE SL.site_Code = :site_Code and sg.isActive = 1 and sgn.code IN (SELECT acf.Nature FROM AcFilteration acf WHERE acf.v_Type = :v_Type)");
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("v_Type", DmShared.iNull(v_Type).toString());
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
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
    public DmResultPageing<List<TxtHRequisition_Mdl>> getTxtHRequisition(String v_Date, String docId, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHRequisition_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHRequisition_Mdl> lstTemp = new ArrayList<>();
        TxtHRequisition_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String condition = "", conditionInner = "", join = "";
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(docId).equalsIgnoreCase("")) {
                conditionInner += " WHERE (ISNULL(rd.reqQty, 0) - ISNULL(rd.issueQty, 0) - ISNULL(rd.indentQty, 0) + ISNULL(id.reqQty, 0)) > 0";
                join = " LEFT JOIN ("
                        + "     SELECT id.requisitionDocId, id.requisitionV_SNo, id.reqQty "
                        + "     FROM indentDetail id"
                        + "     WHERE id.docid = :docId"
                        + " ) AS id ON rd.docId = id.requisitionDocId AND rd.v_SNo = id.requisitionV_SNo";
            } else {
                conditionInner += " WHERE (ISNULL(rd.reqQty, 0) - ISNULL(rd.issueQty, 0) - ISNULL(rd.indentQty, 0)) > 0";
            }

            conditionInner += " AND ISNULL(rd.isStatus, 0) = 0 ";

            if (!DmShared.dNull(v_Date).trim().equals("")) {
                condition += " AND mrq.v_Date <= :v_Date";
            } else {
                condition += " AND 1 = 2";
            }

            sqlWithParam.setSQL("SELECT mrq.docId  AS code,mrq.recId, mrq.recId AS name,mrq.v_Date, sdm.name departmentname, sg.name subgroupName "
                    + "  FROM requisition mrq "
                    + "  LEFT JOIN subgroup sdm ON sdm.subcode = mrq.departmentCode "
                    + "  LEFT JOIN subGroup sg ON sg.subCode = mrq.requestedByCode "
                    + "  LEFT JOIN voucher_Type AS vt on vt.v_Type=mrq.v_Type"
                    + "  INNER JOIN ("
                    + "             SELECT rd.docId "
                    + "             FROM requisitionDetail rd " + join + conditionInner
                    + "             GROUP BY rd.docId"
                    + "  ) rId ON rId.docId = mrq.docid "
                    + "  where mrq.site_Code_To=:site_code  AND  ISNULL(mrq.approvedBy, '') <> ''"
                    + "  AND vt.category = " + vCatReq
                    + condition);
            sqlWithParam.setString("site_code", DmServer.getSsnSiteCode(this));
            if (!DmShared.xNull(docId).equalsIgnoreCase("")) {
                sqlWithParam.setString("docId", DmShared.xNull(docId));
            }
            if (!DmShared.dNull(v_Date).trim().equals("")) {
                sqlWithParam.setDate("v_Date", DmServer.convertDate(v_Date));
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHRequisition_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setDepartment(DmShared.xNull(rsTemp.getString("departmentname")));
                listNode.setRequisitionBy(DmShared.xNull(rsTemp.getString("subgroupName")));
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
    public DmResult<DefaultValue_Mdl> getDefaultValues() {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult();
        DefaultValue_Mdl lstTemp = new DefaultValue_Mdl();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "select top 1 description,V_Type from voucher_Type where category= " + vCatIND + " ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

            rsTemp = psTemp.executeQuery();

            if (rsTemp.next()) {
                lstTemp.setIndentTypeTag(DmShared.xNull(rsTemp.getString("V_Type")));
                lstTemp.setIndentTypeText(DmShared.xNull(rsTemp.getString("description")));

            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "select  dm.subCode as code, sg.displayName as name  "
                    + "		from departmentMast as dm "
                    + "		left join subGroup as sg on dm.subCode = sg.subCode "
                    + "		left join subGroupNature as sgn on sgn.code = sg.nature "
                    + "  where sg.IsActive = 1 and dm.subCode=:depCode and sgn.manualCode IN ('CM','DP','PR') ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("depCode", DmShared.xNull(STServer.getSsnDepartmentCode(this)));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                lstTemp.setDepartmentTag(DmShared.xNull(rsTemp.getString("code")));
                lstTemp.setDepartmentText(DmShared.xNull(rsTemp.getString("name")));

            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Get Enviro Data >>====================================*/
            sqlQuery = "SELECT e.inv_DefaultPurchaseDepartment,sg.name as defaultPurchaseDepartmentName \n"
                    + "FROM enviro e\n"
                    + "left join subGroup sg on e.inv_DefaultPurchaseDepartment=sg.subCode \n"
                    + "WHERE e.site_Code = :site_Code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                lstTemp.setDefaultPurchaseDepartment(DmShared.xNull(rsTemp.getString("inv_DefaultPurchaseDepartment")));
                lstTemp.setDefaultPurchaseDepartmentText(DmShared.xNull(rsTemp.getString("defaultPurchaseDepartmentName")));
            }
            rsTemp.close();
            psTemp.close();

            dmrTemp.setData(lstTemp);

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
    public DmResult<String> setApproval(String approvedBy, boolean isApprove, String docId) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        dmrTemp.setData("");
        String sqlQuery;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        Connection stagingCnTemp = null;
        DmSavePoint d365SpBegin = null;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            spBegin = new DmSavePoint(cnTemp);
            String databaseName = DmServer.executeScalar(" select bc_databaseName from enviroCompany ", cnTemp);
            stagingCnTemp = DmServer.getConnection(databaseName);
            d365SpBegin = new DmSavePoint(stagingCnTemp);

            if (isApprove) {
                sqlQuery = "update indent set \n"
                        + " approvedBy = :approvedBy ,\n"
                        + " approvalDateTime = :approvalDateTime \n "
                        + " where docid = :docId";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.xNull(docId));
                psTemp.setString("approvedBy", DmShared.xNull(DmServer.getSsnUserName(this)));
                psTemp.setTimestamp("approvalDateTime", DmServer.getServerTimestamp());
                psTemp.executeUpdate();
                psTemp.close();
                IndentSync.insertIntoD365DataBase(IndentSync.getIndentData(cnTemp, docId,"Inventory"), stagingCnTemp, this);
            }
            dmrTemp.setData(docId);
            d365SpBegin.commit();
            spBegin.commit();
            cnTemp.close();
            stagingCnTemp.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
                if (d365SpBegin != null) {
                    d365SpBegin.rollBack();
                }
            } catch (Exception ex) {
            }
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
                if (stagingCnTemp != null) {
                    stagingCnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

    @Override
    public DmResult<String> walkThrough(WalkThrough_Mdl walkThrough) {
        final DmResult<String> dmrTemp = new DmResult();

        try {
            WalkThrough_Server.walkThrough(walkThrough, new IWalkThrough() {
                @Override
                public void changeStatus(WalkThroughEnum walkThroughEnum, String keyCode, String userName, Connection cnTemp) throws Exception {
                    STApprovals.indentApproval(walkThroughEnum, keyCode, userName, cnTemp);
                }
            }, this);

            dmrTemp.setData("");
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(e.getMessage());
            dmrTemp.setStatus(false);
        }
        return dmrTemp;
    }

    @Override
    public DmResult<List<GrdItemDetail_Mdl>> fillIndentDetails(String v_Type, String requisitionDocid, String v_Date, String docId) {
        DmResult<List<GrdItemDetail_Mdl>> dmrTemp = new DmResult();

        try {
            dmrTemp.setData(getDetail(v_Type, requisitionDocid, v_Date, docId).getGrdItemDetailList());
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }

        return dmrTemp;
    }

    public ItemWithRequisition_Mdl getDetail(String v_Type, String requisitionDocid, String v_Date, String docId) throws Exception {
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, condition = "", pendingQty = "", join = "", itemCatFilteration = "";
        ItemWithRequisition_Mdl itemIssueWithRequisition = new ItemWithRequisition_Mdl();
        GrdItemDetail_Mdl node;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            itemCatFilteration = DmServer.getItemCatFilteration(v_Type, cnTemp);

            if (!DmShared.xNull(docId).equalsIgnoreCase("")) {
                pendingQty = " , ISNULL(mrqd.reqQty, 0) - ISNULL(mrqd.issueQty, 0) - ISNULL(mrqd.indentQty, 0) + ISNULL(id.reqQty, 0) AS pendingQty";
                condition += " AND (ISNULL(mrqd.reqQty,0) - ISNULL(mrqd.issueQty, 0) - ISNULL(mrqd.indentQty,0) + ISNULL(id.reqQty,0)) > 0";

                join = " LEFT JOIN ("
                        + "     SELECT id.*"
                        + "     FROM indentDetail id"
                        + "     WHERE id.docid = :docId"
                        + " ) AS id ON mrqd.docId = id.requisitionDocId AND mrqd.v_SNo = id.requisitionV_SNo";
            } else {
                pendingQty = " , ISNULL(mrqd.reqQty, 0) - ISNULL(mrqd.issueQty, 0) - ISNULL(mrqd.indentQty, 0) AS pendingQty";
                condition += " AND (ISNULL(mrqd.reqQty,0) - ISNULL(mrqd.issueQty, 0) - ISNULL(mrqd.indentQty,0)) > 0";
            }

            if (!DmShared.dNull(v_Date).trim().equals("")) {
                condition += " AND mrq.v_Date <= :v_Date";
            } else {
                condition += " AND 1 = 2";
            }

            if (!DmShared.xNull(itemCatFilteration).equalsIgnoreCase("")) {
                condition += " AND mm.itemCategoryCode IN (" + itemCatFilteration + ")";
            }

            /*==============================<< Get Item detail (via list) >>===============================*/
            sqlQuery = "SELECT mrqd.docId,mrq.docId as requisitionDocId,mrq.recid,mrqd.reqDate,mrqd.remark, mrqd.isStatus, "
                    + " mm.code ItemCode,mm.name ItemName, mrq.approvedBy, ump.unit as skuName,IsNull(ump.decimalPlaces,0) as decimalPlaces , "
                    + " format(mrq.approvalDateTime,'dd-MMM-yyyy HH.mm') as approvalDateTime,mrq.departmentToCode, sgd.Name as depToName,mrqd.colourCode,icm.name as colourName,"
                    + " mrqd.sizeCode as sizeCode, "
                    + " imm.companyName  as itemMakeName, mrqd.itemMakeCode as itemMakeCode ,"
                    + " ism.name sizeName, mm.propertyList,mrqd.v_SNo as requisitionVsNo,mrq.v_Date,mrqd.itemCurrPosition  " + pendingQty
                    + " FROM requisition mrq"
                    + " LEFT JOIN requisitionDetail mrqd ON mrqd.docId = mrq.docId" + join
                    + " LEFT JOIN itemMast mm ON mm.code = mrqd.itemCode"
                    + " LEFT JOIN unitMast ump ON ump.code = mm.sku"
                    + " LEFT JOIN itemColourMast icm ON icm.code = mrqd.colourCode"
                    + " LEFT JOIN itemSizeMast ism ON ism.code = mrqd.sizeCode"
                    + "	left join itemMakeMast as imm ON imm.code = mrqd.itemMakeCode "
                    + " LEFT JOIN subgroup as sgd on sgd.subcode = mrq.departmentToCode"
                    + " LEFT JOIN voucher_Type as vt ON vt.v_Type = mrq.v_Type"
                    + " WHERE (mrqd.isStatus) NOT IN ('1','2') "
                    + " AND ISNULL(mrq.approvedBy, '') <> '' "
                    + " AND mrq.site_Code_To = :site_Code "
                    + " AND mrqd.docId = :requisitionDocid " + condition;
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp.setString("requisitionDocid", DmShared.iNull(requisitionDocid).toString());
            if (!DmShared.xNull(docId).equalsIgnoreCase("")) {
                psTemp.setString("docId", DmShared.xNull(docId));
            }
            if (!DmShared.dNull(v_Date).trim().equals("")) {
                psTemp.setDate("v_Date", DmServer.convertDate(v_Date));
            }
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {

                itemIssueWithRequisition.setRequisitionCode(DmShared.xNull(rsTemp.getString("docId")));
                itemIssueWithRequisition.setRequisitionName(DmShared.xNull(rsTemp.getString("recid")));

                node = new GrdItemDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("ItemCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("ItemName")));
                node.setApproval(DmShared.xNull(rsTemp.getString("approvedBy")) + " " + DmShared.xNull(rsTemp.getString("approvalDateTime")));
                node.setRemarks(DmShared.xNull(rsTemp.getString("itemCurrPosition")) + " " + DmShared.xNull(rsTemp.getString("remark")));
                node.setReqDate(DmShared.dNull(rsTemp.getDate("reqDate")));
                node.setUnitName(DmShared.xNull(rsTemp.getString("skuName")));
                node.setRequisitionDocId(DmShared.xNull(rsTemp.getString("requisitionDocId")));
                node.setRequisitionNo(DmShared.xNull(rsTemp.getString("recId")));
                node.setRequisitionDate(DmShared.xNull(rsTemp.getString("v_Date")));
                node.setQty(DmServer.vNull(rsTemp.getString("pendingQty")));
                node.setDecimalPlace(DmShared.xNull(rsTemp.getString("decimalPlaces")));
                node.setRequisitionVsNo(DmShared.xNull(rsTemp.getString("requisitionVsNo")));
                node.setItemProperty(DmShared.xNull(rsTemp.getString("propertyList")));
                node.setColorCode(DmShared.xNull(rsTemp.getString("colourCode")));
                node.setColor(DmShared.xNull(rsTemp.getString("colourName")));
                node.setSizeCode(DmShared.xNull(rsTemp.getString("sizeCode")));
                node.setSize(DmShared.xNull(rsTemp.getString("sizeName")));
                node.setItemMakeCode(DmShared.xNull(rsTemp.getString("itemMakeCode")));
                node.setItemMakeName(DmShared.xNull(rsTemp.getString("itemMakeName")));

                ItemProperty_Mdl ipm = new ItemProperty_Mdl();
                ipm.setColourCode(DmShared.xNull(rsTemp.getString("colourCode")));
                ipm.setColourName(DmShared.xNull(rsTemp.getString("colourName")));
                ipm.setSizeCode(DmShared.xNull(rsTemp.getString("sizeCode")));
                ipm.setSizeName(DmShared.xNull(rsTemp.getString("sizeName")));

                ipm.setItemMakeCode(DmShared.xNull(rsTemp.getString("itemMakeCode")));
                ipm.setItemMakeName(DmShared.xNull(rsTemp.getString("itemMakeName")));

                node.setProperties(ipm.getPropertiesCsv());
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

    @Override
    public DmResult<ItemWithRequisition_Mdl> getRequisitionDetail(String v_Type, String requisitionDocid, String v_Date, String docId) {
        DmResult<ItemWithRequisition_Mdl> dmrTemp = new DmResult();

        try {
            dmrTemp.setData(getDetail(v_Type, requisitionDocid, v_Date, docId));
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }

        return dmrTemp;
    }

    public String chkLstItemRequisitionQtyChange(Indent_Mdl mrTemp, Connection cnTemp) {
        ResultSet rsTemp = null;
        String itemNames = "";
        String sqlQuery = "";
        DmPreparedStatement psTemp = null;

        try {
            if (!DmShared.xNull(mrTemp.getReqQtyChangeCondition()).trim().equals("")) {
//                sqlQuery = "SELECT STUFF ("
//                        + "     (SELECT DISTINCT ', ' + im.name AS [text()]"
//                        + "     FROM requisitionDetail rqd "
//                        + "     INNER JOIN itemMast im ON im.code = rqd.itemCode"
//                        + "     " + DmShared.xNull(mrTemp.getReqQtyChangeCondition()) + " "
//                        + "     FOR XML PATH('')), 1,1,''"
//                        + " ) AS csv";
                sqlQuery = "SELECT STUFF ("
                        + "     (SELECT DISTINCT ', ' + im.name AS [text()]"
                        + "     FROM requisitionDetail rqd "
                        + "     INNER JOIN itemMast im ON im.code = rqd.itemCode"
                        + "     " + DmShared.xNull(mrTemp.getReqQtyChangeCondition()) + " "
                        + "     FOR XML PATH(''), TYPE).value('.[1]','nvarchar(max)'), 1,1,''"
                        + " ) AS csv";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    itemNames = DmShared.xNull(rsTemp.getString("csv")).trim();
                }
            }
        } catch (Exception e) {
            itemNames = "";
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

        return itemNames;
    }

    private void manageRequisionIndentQty(String docId, boolean isDelete, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null, psTemp1 = null;
        ResultSet rsTemp = null;
        String sqlQuery, requisitionDocId = "";
        double indentReqQty, requisitionIndentQty, requisitionReqQty, requisitionIssueQty;

        try {
            sqlQuery = "SELECT"
                    + "   r.docId AS requisitionDocId"
                    + " , id.requisitionV_SNo"
                    + " , id.reqQty AS indentReqQty"
                    + " , rd.reqQty AS requisitionReqQty"
                    + " , rd.issueQty AS requisitionIssueQty"
                    + " , rd.indentQty AS requisitionIndentQty"
                    + " , r.approvedBy"
                    + " , r.recId AS pbNo"
                    + " , vt.short_name AS pbType"
                    + " FROM requisitionDetail rd"
                    + " LEFT JOIN requisition r ON rd.docId = r.docId"
                    + " LEFT JOIN voucher_type vt ON r.v_Type = vt.v_type"
                    + " INNER JOIN ("
                    + "     SELECT id.requisitionDocId, id.requisitionV_SNo, sum(id.reqQty) AS reqQty"
                    + "     FROM indentDetail id"
                    + "     WHERE id.docId = :docId AND id.isStatus <> 1 "
                    + "     GROUP BY id.requisitionDocId, id.requisitionV_SNo"
                    + " ) id ON rd.docId = id.requisitionDocId AND rd.v_SNo = id.requisitionV_SNo";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(docId));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                    throw new DmException(500, DmShared.xNull(rsTemp.getString("requisitionDocId")), DmShared.xNull(rsTemp.getString("requisitionV_SNo")), "Requisition is not Approved");
                }
                indentReqQty = rsTemp.getDouble("indentReqQty");
                requisitionReqQty = rsTemp.getDouble("requisitionReqQty");
                requisitionIssueQty = rsTemp.getDouble("requisitionIssueQty");
                requisitionIndentQty = rsTemp.getDouble("requisitionIndentQty");

                if (isDelete) {
                    requisitionIndentQty -= indentReqQty;
                } else {
                    requisitionIndentQty += indentReqQty;
                }

                if (requisitionIndentQty > (requisitionReqQty - requisitionIssueQty)) {
                    throw new DmException(500, DmShared.xNull(rsTemp.getString("requisitionDocId")), DmShared.xNull(rsTemp.getString("requisitionV_SNo")), "Requisition overflows by " + DmServer.vNull(requisitionIndentQty - (requisitionReqQty - requisitionIssueQty)));
                } else if (requisitionIndentQty < 0) {
                    throw new DmException(500, DmShared.xNull(rsTemp.getString("requisitionDocId")), DmShared.xNull(rsTemp.getString("requisitionV_SNo")), "Requisition overflows by " + DmServer.vNull(requisitionIndentQty));
                }

                if (!requisitionDocId.equalsIgnoreCase("")) {
                    requisitionDocId += ",";
                }
                requisitionDocId += DmShared.iNull(rsTemp.getString("requisitionDocId")).toString();

                sqlQuery = "UPDATE requisitionDetail SET"
                        + " indentQty = :indentQty"
                        + " WHERE docId = :requisitionDocId AND v_SNo = :requisitionV_SNo";
                psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp1.setString("requisitionDocId", DmShared.xNull(rsTemp.getString("requisitionDocId")));
                psTemp1.setString("requisitionV_SNo", DmShared.xNull(rsTemp.getString("requisitionV_SNo")));
                psTemp1.setString("indentQty", DmServer.vNull(requisitionIndentQty, "0.0000000"));
                psTemp1.execute();
                psTemp1.close();
            }
            rsTemp.close();
            psTemp.close();

            if (!requisitionDocId.equalsIgnoreCase("")) {
                sqlQuery = "UPDATE requisition SET u_EntDt_LatestLine = :uEntDt where docId in (" + requisitionDocId + ")";
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
}
