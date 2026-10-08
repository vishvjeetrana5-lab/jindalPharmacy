package aarogya.inventory.requisition.server;

import aarogya.inventory.requisition.shared.TxtHPriority_Mdl;
import aarogya.inventory.requisition.shared.Requisition_Vld;
import aarogya.inventory.requisition.shared.TxtHItem_Mdl;
import aarogya.inventory.requisition.shared.Requisition_Mdl;
import aarogya.inventory.requisition.shared.DefaultValue_Mdl;
import aarogya.inventory.requisition.shared.GrdRequisition_Mdl;
import aarogya.inventory.requisition.shared.Find_Mdl;
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
import aarogya.inventory.requisition.shared.Requisition_Srv;
import aarogya.inventory.requisition.shared.TxtHRequisition_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import aarogya.selfToolkit.modifyReason.server.ModifyReason_Server;
import aarogya.selfToolkit.stStatic.server.STServer;
import dataman.dmToolkit.walkThrough.server.WalkThrough_Server;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough;
import dataman.dmToolkit.walkThrough.shared.WalkThroughDefault_Mdl;
import dataman.dmToolkit.walkThrough.shared.WalkThroughEnum;
import dataman.dmToolkit.walkThrough.shared.WalkThrough_Mdl;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import aarogya.selfToolkit.stStatic.server.STApprovals;

public class Requisition_SrvImpl extends DmRSSPrivate implements Requisition_Srv {

    private String v_Category = new ProjectVoucherCategory().INVENTORY_REQUISITION;
    private String v_CategoryTransfer = new ProjectVoucherCategory().ITEM_TRANSFER;
    private final String voucherTypeFieldName = "rq.V_Type", voucherDateFieldName = "rq.V_Date";

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission) {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        List<KeyFieldExtended> lstKeyField = new ArrayList<>();
        KeyFieldExtended lstKeyFieldTmp;
        dmrTemp.setData(lstKeyField);
        String sqlQuery = "", sqlCondition = "";

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = " SELECT docid as code "
                    + " FROM requisition rq "
                    + " LEFT JOIN voucher_Type vt ON vt.v_Type=rq.v_Type"
                    + " where vt.Category='" + v_Category + "' and rq.site_Code = :site_Code and rq.v_prefix = :vPrefix ";

            sqlCondition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypeFieldName, userPermission);
            if (!sqlCondition.equalsIgnoreCase("")) {
                sqlQuery += "and " + sqlCondition;
            }
            sqlQuery += " Order by rq.v_Date desc , vt.short_Name, rq.v_No desc, rq.recId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp.setString("vPrefix", DmServer.getSsnVoucherPrefix(this));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lstKeyFieldTmp = new KeyFieldExtended();
                lstKeyFieldTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                lstKeyField.add(lstKeyFieldTmp);
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
    public DmResult<Requisition_Mdl> getMoveRec(String strKey) {
        DmResult<Requisition_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String strSQL;
        Requisition_Mdl mrTemp = new Requisition_Mdl();
        List<GrdRequisition_Mdl> lstTemp = new ArrayList<>();
        dmrTemp.setData(mrTemp);

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            strSQL = "SELECT rq.docId, rq.recId, rq.v_Date,sg.subCode,sg.name AS subgroup,rq.v_type,rq.remark,vt.description,"
                    + "sdp.subcode AS departmentCode,rq.departmentToCode,sdp.name departmentName,rq.preparedBy,sdpto.name departmentToName,"
                    + " rq.u_EntDt_LatestLine,rq.approvedBy,rq.approvalDateTime, rq.wtaCode,hl7.shortName as priorityName,rq.priorityTypeCode,rq.recIdPrefix,rq.v_No, "
                    + " rq.site_Code_to,sm.name as siteName, "
                    + " rq.preparedDate, "
                    + " rq.u_name , rq.u_EntDt "
                    + " from requisition rq "
                    + " LEFT JOIN subGroup sg ON sg.subCode = rq.requestedByCode "
                    + " LEFT JOIN subgroup sdp ON sdp.subcode = rq.departmentCode"
                    + " LEFT JOIN subgroup sdpto ON sdpto.subcode = rq.departmentToCode"
                    + " LEFT JOIN voucher_Type vt ON vt.v_Type=rq.v_Type"
                    + " left join hl7Mast  as hl7 on hl7.code  = rq.priorityTypeCode "
                    + " left join siteMast as sm on sm.code =rq.site_Code_To  "
                    + " where rq.docid=:docid  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("docid", strKey);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docid")));

                mrTemp.getRecid().setRecIdValue(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.getRecid().setCounter(DmShared.iNull(rsTemp.getString("v_No")).longValue());
                mrTemp.getRecid().setPrefix(DmShared.xNull(rsTemp.getString("recIdPrefix")));

                mrTemp.setDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                mrTemp.setDepartmentByName(DmShared.xNull(rsTemp.getString("departmentName")));
                mrTemp.setDepartmentByCode(DmShared.xNull(rsTemp.getString("departmentCode")));
                mrTemp.setDepartmentToName(DmShared.xNull(rsTemp.getString("departmentToName")));
                mrTemp.setDepartmentToCode(DmShared.xNull(rsTemp.getString("departmentToCode")));
                mrTemp.setSubCode(DmShared.xNull(rsTemp.getString("subCode")));
                mrTemp.setSubName(DmShared.xNull(rsTemp.getString("subgroup")));
                mrTemp.setWtaCode(DmShared.xNull(rsTemp.getString("wtaCode")));
                mrTemp.setPriorityTypeCode(DmShared.xNull(rsTemp.getString("priorityTypeCode")));
                mrTemp.setPriorityTypeName(DmShared.xNull(rsTemp.getString("priorityName")));
                mrTemp.setRequisitionTypeCode(DmShared.xNull(rsTemp.getString("v_Type")));
                mrTemp.setRequisitionTypeName(DmShared.xNull(rsTemp.getString("description")));
                mrTemp.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                mrTemp.setLinkSiteCode(DmShared.xNull(rsTemp.getString("site_Code_to")));
                mrTemp.setLinkSiteName(DmShared.xNull(rsTemp.getString("siteName")));
                mrTemp.setRecIdPrefix(DmShared.xNull(rsTemp.getString("recIdPrefix")));
                mrTemp.setvNo(DmShared.xNull(rsTemp.getString("v_No")));
                if (rsTemp.getTimestamp("u_EntDt_LatestLine") != null) {
                    mrTemp.setUserEntDtLatestLine(String.valueOf(rsTemp.getTimestamp("u_EntDt_LatestLine").getTime()));
                }

                mrTemp.setSharedPreparedBy(getUserWithDateTime(DmShared.xNull(rsTemp.getString("preparedBY")), rsTemp.getTimestamp("preparedDate")));
                mrTemp.setSharedModifiedBy(getUserWithDateTime(DmShared.xNull(rsTemp.getString("u_name")), rsTemp.getTimestamp("u_EntDt")));
                mrTemp.setApprovedBy(getUserWithDateTime(DmShared.xNull(rsTemp.getString("approvedBy")), rsTemp.getTimestamp("approvalDateTime")));
            }
            psTemp.close();
            rsTemp.close();

            strSQL = " SELECT "
                    + "  rqd.docId, rqd.v_SNo,im.propertyList,"
                    + "  rqd.reqQty,"
                    + "  rqd.reqDate,rqd.remark,rqd.isStatus,"
                    + "  im.code,im.name,um.unit AS sku,um.code skuCode,rqd.colourCode,icm.name as colourName, "
                    + "  rqd.sizeCode as sizeCode, "
                    + "  imm.companyName as itemMakeName, rqd.itemMakeCode as itemMakeCode,ism.name sizeName,um.decimalPlaces,rqd.itemCurrPosition "
                    + "   from requisitiondetail rqd  "
                    + "                 LEFT JOIN itemMast im ON im.code = rqd.itemCode "
                    + "                 LEFT JOIN itemColourMast icm ON icm.code = rqd.colourCode "
                    + "                 LEFT JOIN itemSizeMast ism ON ism.code = rqd.sizeCode "
                    + "                 LEFT JOIN unitMast um ON um.code = im.sku "
                    + "			left join itemMakeMast as imm ON imm.code = rqd.itemMakeCode "
                    + " where docid=:docid ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("docid", strKey);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                GrdRequisition_Mdl objListTemp = new GrdRequisition_Mdl();
                objListTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                objListTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                objListTemp.setDate(DmShared.dNull(rsTemp.getDate("reqDate")));
                objListTemp.setQty(DmServer.vNull(rsTemp.getString("reqQty")));
                objListTemp.setvSNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                objListTemp.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                objListTemp.setItemCurrPosition(DmShared.xNull(rsTemp.getString("itemCurrPosition")));
                objListTemp.setUnitCode(DmShared.xNull(rsTemp.getString("skuCode")));
                objListTemp.setUnitName(DmShared.xNull(rsTemp.getString("sku")));
                objListTemp.setAllowDecimalPlaces(DmShared.xNull(rsTemp.getString("decimalPlaces")));
                objListTemp.setIsStatus(DmShared.xNull(rsTemp.getString("isStatus")));
                objListTemp.setPropertyList(DmShared.xNull(rsTemp.getString("propertyList")));
                objListTemp.setColourCode(DmShared.xNull(rsTemp.getString("colourCode")));
                objListTemp.setColourName(DmShared.xNull(rsTemp.getString("colourName")));
                objListTemp.setSizeCode(DmShared.xNull(rsTemp.getString("sizeCode")));
                objListTemp.setSizeName(DmShared.xNull(rsTemp.getString("sizeName")));

                objListTemp.setItemMakeCode(DmShared.xNull(rsTemp.getString("itemMakeCode")));
                objListTemp.setItemMakeName(DmShared.xNull(rsTemp.getString("itemMakeName")));

                ItemProperty_Mdl ipm = new ItemProperty_Mdl();
                ipm.setColourCode(DmShared.xNull(rsTemp.getString("colourCode")));
                ipm.setColourName(DmShared.xNull(rsTemp.getString("colourName")));
                ipm.setSizeCode(DmShared.xNull(rsTemp.getString("sizeCode")));
                ipm.setSizeName(DmShared.xNull(rsTemp.getString("sizeName")));

                ipm.setItemMakeCode(DmShared.xNull(rsTemp.getString("itemMakeCode")));
                ipm.setItemMakeName(DmShared.xNull(rsTemp.getString("itemMakeName")));

                objListTemp.setProperties(ipm.getPropertiesCsv());

                lstTemp.add(objListTemp);
            }
            if (lstTemp.isEmpty()) {
                GrdRequisition_Mdl ObjListTemp = new GrdRequisition_Mdl();
                ObjListTemp.setSharedKeyFieldName("");
                ObjListTemp.setSharedKeyFieldCode("");
                ObjListTemp.setQty("");
                lstTemp.add(ObjListTemp);
                mrTemp.getGrdData().addAll(lstTemp);
            } else {
                mrTemp.getGrdData().addAll(lstTemp);
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

    private WalkThroughDefault_Mdl getWalkThroughFor(Requisition_Mdl mrTemp, String v_Type) {
        WalkThroughDefault_Mdl walkThroughDefault = new WalkThroughDefault_Mdl();

        walkThroughDefault.setDepartmentCode(mrTemp.getDepartmentByCode());
        walkThroughDefault.setSiteCode(DmServer.getSsnSiteCode(this));
        walkThroughDefault.setUserName(DmServer.getSsnUserName(this));
        walkThroughDefault.setValueUpto("");
        walkThroughDefault.setvType(v_Type);

        return walkThroughDefault;
    }

    @Override
    public DmResult<String> saveRecord(Requisition_Mdl mrTemp, String voucherPrefix, boolean isApprove) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery, voucherType = "", vNo = "";
        ResultSet rsTemp = null;
        dmrTemp.setData("");

        try {
            Requisition_Vld.chkSave(mrTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            voucherType = DmShared.xNull(mrTemp.getRequisitionTypeCode());
            
            DmServer.checkBackwordVoucherDateTime("stock", "v_Date", "", mrTemp.getDate(), "", cnTemp);
            
            if (!DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {

                STServer.chkIfDocumentUsed("requisitionDocId", "indentDetail", mrTemp.getSharedKeyFieldCode(), cnTemp);
                STServer.chkIfDocumentUsed("requisitionDocId", "stockHeadDetail", mrTemp.getSharedKeyFieldCode(), cnTemp);

                sqlQuery = "SELECT u_EntDt_LatestLine FROM requisition WHERE docId = :docId";
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
            }

            for (int i = 0; i < mrTemp.getGrdData().size(); i++) {
                if (!DmShared.xNull(mrTemp.getGrdData().get(i).getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                    if (!DmShared.xNull(mrTemp.getGrdData().get(i).getIsStatus()).equalsIgnoreCase("1") && !DmShared.xNull(mrTemp.getGrdData().get(i).getIsStatus()).equalsIgnoreCase("2")) {
                        sqlQuery = " Select ird.code,ird.departmentCode from itemRestrictedtoDepartment as ird  "
                                + " where ird.code = :itemCode  ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("itemCode", DmShared.iNull(mrTemp.getGrdData().get(i).getSharedKeyFieldCode()).toString());
                        rsTemp = psTemp.executeQuery();
                        while (rsTemp.next()) {
                            if (DmShared.xNull(rsTemp.getString("departmentCode")).equalsIgnoreCase(mrTemp.getDepartmentByCode())
                                    || DmShared.xNull(rsTemp.getString("departmentCode")).equalsIgnoreCase(mrTemp.getDepartmentToCode())) {
                                throw new UnsupportedOperationException("Item " + mrTemp.getGrdData().get(i).getSharedKeyFieldName() + " is not applicable in given department.");
                            }
                        }
                        psTemp.close();
                        rsTemp.close();
                    }
                }
            }

            spBegin = new DmSavePoint(cnTemp);
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(voucherType), voucherPrefix, cnTemp, this));
                mrTemp.setRecid(DmServer.getRecId("requisition", "docid", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecid(), mrTemp.getDate(), DmShared.xNull(voucherType), voucherPrefix, cnTemp, this, true));
                mrTemp.setWtaCode(WalkThrough_Server.applyWalkThrough(getWalkThroughFor(mrTemp, voucherType), DmServer.getSsnUserName(this), voucherType, mrTemp.getSharedKeyFieldCode(), cnTemp, this));
            } else {
                DmServer.getRecId("requisition", "docid", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecid(), mrTemp.getDate(), DmShared.xNull(voucherType), voucherPrefix, cnTemp, this, true);
            }
            vNo = DmShared.iNull(mrTemp.getRecid().getCounter()).toString();
            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            sqlQuery = "select COUNT(sh.docID) AS totalInvalidRecords   "
                    + "	from stockHead sh  "
                    + "	left join stockHeadDetail shd on sh.docID=shd.docID"
                    + "	left join voucher_Type vt on sh.v_Type=vt.v_Type  "
                    + "  where vt.category= '"+v_CategoryTransfer+"' and shd.departmentToCode= :departmentCode "
                    + "  and isnull(sh.approvedBy,'')=''";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("departmentCode", DmShared.iNull(mrTemp.getDepartmentByCode()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("totalInvalidRecords")).intValue() > 0) {
                    throw new DmException(0, "Some inward transfers are pending for approval. First approve...");
                }
            }
            rsTemp.close();
            psTemp.close();
            
            if (blnAdd) {
                sqlQuery = "INSERT INTO requisition (docId, v_Type,v_No,  recId, v_Date, v_Prefix, departmentCode, requestedByCode, site_Code,wtaCode, preparedBy,"
                        + " preparedDate,u_EntDt_LatestLine,departmentToCode,priorityTypeCode,remark,site_Code_to,recIdPrefix)"
                        + "VALUES (:docid, :v_type, :v_No, :recid, :v_date, :v_prefix, :departmentcode, :requestedbycode, :site_code,:wtaCode ,"
                        + ":preparedby, :preparedDate,:uEntDt,:departmentToCode,:priorityTypeCode,:remark,:site_Code_to,:recIdPrefix) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("docid", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("v_type", DmShared.xNull(voucherType));
                psTemp.setString("v_No", DmShared.iNull(vNo).toString());
                psTemp.setString("recid", DmShared.xNull(mrTemp.getRecid().getRecIdValue()));
                psTemp.setString("wtaCode", DmShared.foriegnKey(mrTemp.getWtaCode()));
                psTemp.setDate("v_date", DmServer.convertDate(mrTemp.getDate()));
                psTemp.setString("v_prefix", DmShared.xNull(voucherPrefix));
                psTemp.setString("departmentcode", DmShared.foriegnKey(mrTemp.getDepartmentByCode()));
                psTemp.setString("departmentToCode", DmShared.foriegnKey(mrTemp.getDepartmentToCode()));
                psTemp.setString("requestedbycode", DmShared.foriegnKey(mrTemp.getSubCode()));
                psTemp.setString("priorityTypeCode", DmShared.foriegnKey(mrTemp.getPriorityTypeCode()));
                psTemp.setString("site_code", DmShared.xNull(DmServer.getSsnSiteCode(this)));
                psTemp.setString("preparedby", DmShared.xNull(DmServer.getSsnUserName(this)));
                psTemp.setTimestamp("preparedDate", DmServer.getServerTimestamp());
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setString("site_Code_to", DmShared.foriegnKey(mrTemp.getLinkSiteCode()));
                psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getRecid().getPrefix()));

            } else {
                sqlQuery = "UPDATE requisition set "
                        + " departmentCode = :departmentcode, "
                        + " departmentToCode = :departmentToCode, "
                        + " requestedByCode = :requestedbycode, "
                        + " priorityTypeCode = :priorityTypeCode, "
                        + " site_Code_to= :site_Code_to,"
                        + " remark = :remark, "
                        + " v_date=:v_date, "
                        + " u_Name = :u_name, "
                        + " u_EntDt=:uEntDt, "
                        + " u_EntDt_LatestLine=:uEntDt "
                        + " where docid=:docid ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("u_name", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("departmentcode", DmShared.foriegnKey(mrTemp.getDepartmentByCode()));
                psTemp.setString("departmentToCode", DmShared.foriegnKey(mrTemp.getDepartmentToCode()));
                psTemp.setString("requestedbycode", DmShared.foriegnKey(mrTemp.getSubCode()));
                psTemp.setString("priorityTypeCode", DmShared.foriegnKey(mrTemp.getPriorityTypeCode()));
                psTemp.setString("docid", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setString("site_Code_to", DmShared.foriegnKey(mrTemp.getLinkSiteCode()));
                psTemp.setString("v_date", DmShared.xNull(mrTemp.getDate()));
            }
            psTemp.executeUpdate();
            psTemp.close();

            //==================================================================//
            sqlQuery = "Delete From requisitiondetail where docid= :docid and (isStatus) not in('1','2') ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docid", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();
            //==================================================================//

            int grdCount = mrTemp.getGrdData().size();
            int vSNo = 0, previous_VSno = 0;
            for (int i = 0; i < grdCount; i++) {
                if (!DmShared.xNull(mrTemp.getGrdData().get(i).getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                    previous_VSno = DmShared.iNull(mrTemp.getGrdData().get(i).getvSNo()).intValue();
                    ++vSNo;
                    if (DmShared.iNull(mrTemp.getGrdData().get(i).getIsStatus()).intValue() != 1 && DmShared.iNull(mrTemp.getGrdData().get(i).getIsStatus()).intValue() != 2) {
                        mrTemp.getGrdData().get(i).setvSNo(String.valueOf(vSNo));
                        sqlQuery = " INSERT INTO requisitionDetail (docId, v_SNo, itemCode, reqQty, reqDate, remark,colourCode,sizeCode , "
                                + " itemMakeCode , isStatus )"
                                + "VALUES (:docid, :v_sno, :itemcode, :reqqty, :reqdate, :remark,:colourCode,:sizeCode ,:itemMakeCode ,:isStatus) ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docid", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                        psTemp.setString("v_SNo", DmShared.iNull(mrTemp.getGrdData().get(i).getvSNo()).toString());
                        psTemp.setString("itemcode", DmShared.foriegnKey(mrTemp.getGrdData().get(i).getSharedKeyFieldCode()));
                        psTemp.setString("reqqty", DmServer.vNull(mrTemp.getGrdData().get(i).getQty()));
                        psTemp.setDate("reqdate", DmServer.convertDate(mrTemp.getGrdData().get(i).getDate()));
                        psTemp.setString("remark", DmShared.xNull(mrTemp.getGrdData().get(i).getRemark()));
                        psTemp.setString("colourCode", DmShared.foriegnKey(mrTemp.getGrdData().get(i).getColourCode()));
                        psTemp.setString("sizeCode", DmShared.foriegnKey(mrTemp.getGrdData().get(i).getSizeCode()));
                        psTemp.setString("itemMakeCode", DmShared.foriegnKey(mrTemp.getGrdData().get(i).getItemMakeCode()));
                        psTemp.setString("isStatus", "0");

                        psTemp.executeUpdate();
                        psTemp.close();
                    } else {
                        if (DmShared.iNull(mrTemp.getGrdData().get(i).getIsStatus()).intValue() == 1 && previous_VSno > 0) {
                            //Update New v_SNo For Cancelled record
                            mrTemp.getGrdData().get(i).setvSNo(String.valueOf(vSNo));
                            sqlQuery = "UPDATE requisitionDetail "
                                    + " SET v_SNo = :v_SNo "
                                    + " WHERE docid = :docId "
                                    + " AND  v_SNo = :previous_VSno ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                            psTemp.setInt("v_SNo", DmShared.iNull(mrTemp.getGrdData().get(i).getvSNo()).intValue());
                            psTemp.setInt("previous_VSno", previous_VSno);
                            psTemp.executeUpdate();
                            psTemp.close();
                        }
                    }
                }
            }

            //========================Updating current Bal stk, last 90days consumption, pending balance approved Qty====================================//
            sqlQuery = "update rqd set rqd.itemCurrPosition =crs.itemCurrPosition \n"
                    + "from requisitionDetail rqd \n"
                    + "left join ( \n"
                    + "select rqd.itemCode,'Bal:' +cast(convert(decimal(13,2), round(sum(isnull(ibs.balanceQty,0)),2)) as varchar(15)) + \n"
                    + "'    90 days Consumption:'+cast(convert(decimal(13,2), round(sum(isNull(stk.last90daysConsumption,0)),2)) as varchar(15)) + \n"
                    + "'     Apr Qty:'+cast(convert(decimal(13,2), round(sum(isNull(rqp.pendingApprovedQty,0)),2)) as varchar(15)) as itemCurrPosition \n"
                    + "from requisition rq \n"
                    + "left join requisitionDetail rqd on rq.docid=rqd.docid \n"
                    + "left join( \n"
                    + " select sum(balanceQty) as balanceQty,itemCode \n"
                    + "		from itemBatchStock ibs where ibs.departmentCode=:departmentCode  \n"
                    + "		and ibs.site_Code= :site_code\n"
                    + "		group by itemCode \n"
                    + "		) as ibs on rqd.itemCode=ibs.itemCode \n"
                    + "left join( \n"
                    + "	select stk.itemCode,sum(isNull(stk.isuQty,0)) as last90daysConsumption \n"
                    + "		from stock stk \n"
                    + "		where stk.v_Date between dateAdd(d,-90,:vDate) and :vDate \n"
                    + "		and stk.departmentCode=:departmentCode \n"
                    + "		and stk.site_Code= :site_code\n"
                    + "		group by stk.itemCode \n"
                    + "		) stk on ibs.itemCode=stk.itemCode \n"
                    + "left join( \n"
                    + "	select rqd.itemCode,sum(rqd.reqQty-isNull(rqd.issueQty,0)-isNull(rqd.indentQty,0)) as pendingApprovedQty  \n"
                    + "		from requisition rq \n"
                    + "		left join requisitionDetail rqd on rq.docid=rqd.docid \n"
                    + "		where isnull(rq.approvedBy,'')<>'' and rqd.isStatus=0 and rq.departmentCode=:departmentCode \n"
                    + "		and rq.site_Code= :site_code\n"                    
                    + "		group By rqd.itemCode \n"
                    + "		) rqp on ibs.itemCode=rqp.itemCode \n"
                    + "where rq.Docid=:docid and rq.departmentCode=:departmentCode \n"
                    + "group by rqd.itemCode \n"
                    + ") crs on rqd.itemCode=crs.itemCode \n"
                    + "where rqd.DocId=:docid ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docid", mrTemp.getSharedKeyFieldCode());
            psTemp.setString("departmentCode", mrTemp.getDepartmentByCode());
            psTemp.setString("vDate", mrTemp.getDate());
            psTemp.setString("site_code", DmShared.xNull(DmServer.getSsnSiteCode(this)));
            psTemp.executeUpdate();
            psTemp.close();
            //==================================================================//

            if (mrTemp.getModifyReasonMdl() != null && !blnAdd) {
                ModifyReason_Server.save(mrTemp.getModifyReasonMdl(), mrTemp.getSharedKeyFieldCode(), DmServer.getSsnUserName(this), cnTemp);
            }
            if (DmShared.iNull(mrTemp.getWtaCode()).longValue() == 0 && isApprove) {
                STApprovals.requisitionApproval(WalkThroughEnum.ACCEPT, mrTemp.getSharedKeyFieldCode(), DmServer.getSsnUserName(this), cnTemp);
            }

            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
            spBegin.commit();
            dmrTemp.setStatus(true);
            cnTemp.close();

            /*====================================< Send mail & SMS >====================================*/
            if (blnAdd) {
                STServer.sendMail(mrTemp.getSharedKeyFieldCode(), mrTemp.getRequisitionTypeCode(), this);
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

            STServer.chkIfDocumentUsed("requisitionDocId", "indentDetail", strKey, cnTemp);
            STServer.chkIfDocumentUsed("requisitionDocId", "stockHeadDetail", strKey, cnTemp);

            strSQL = "select wtaCode,approvedBy  FROM requisition WHERE  docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                wtaCode = DmShared.xNull(rsTemp.getString("wtaCode"));
                if (!DmShared.xNull(rsTemp.getString("approvedBy")).equalsIgnoreCase("")) {
                    throw new DmException(0, "Record is approved. You can't delete this record.");
                }
            }
            rsTemp.close();
            psTemp.close();

            spBegin = new DmSavePoint(cnTemp);

            WalkThrough_Server.deleteWalkThrough(wtaCode, cnTemp);
            ModifyReason_Server.delete(strKey, cnTemp);

            strSQL = "Delete From requisitiondetail Where docid= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", strKey);
            psTemp.executeUpdate();
            psTemp.close();

            strSQL = "Delete From requisition Where docid= :code ";
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
            sqlQuery = "SELECT rq.docId,rq.recId as name, rq.recId, rq.v_Date,sg.name AS subgroup,rq.v_No,"
                    + " sdp.name departmentByName,rq.approvedBy,format(rq.approvalDateTime,'dd-MMM-yyyy HH.mm') as approvalDateTime,"
                    + "sdpto.name departmentToName,rq.departmentToCode, vt.short_Name "
                    + " from requisition rq "
                    + " LEFT JOIN subGroup sg ON sg.subCode = rq.requestedByCode "
                    + " LEFT JOIN subgroup sdp ON sdp.subcode = rq.departmentCode "
                    + "LEFT JOIN subgroup sdpto ON sdpto.subcode = rq.departmentToCode"
                    + " LEFT JOIN voucher_Type vt ON vt.v_Type=rq.v_Type"
                    + " where  vt.Category='" + v_Category + "'  and rq.site_Code = :site_Code and rq.v_prefix = :vPrefix ";
            sqlCondition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypeFieldName, userPermission);
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
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                listNode.setRequisitionNo(DmShared.xNull(rsTemp.getString("name")));
                listNode.setDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setDepartmentBy(DmShared.xNull(rsTemp.getString("departmentByName")));
                listNode.setDepartmentTo(DmShared.xNull(rsTemp.getString("departmentToName")));
                listNode.setSubName(DmShared.xNull(rsTemp.getString("subgroup")));
                listNode.setApproval(DmShared.xNull(rsTemp.getString("approvedBy")) + " " + DmShared.xNull(rsTemp.getString("approvalDateTime")));
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
    public DmResultPageing<List<TxtHItem_Mdl>> getTxtHItem(String v_Type, DmHelper helper, boolean blnToEndOfFile, String restrictedDepartments, String toDepartment) {
        DmResultPageing<List<TxtHItem_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHItem_Mdl> lstTemp = new ArrayList<>();
        TxtHItem_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String condition = "", itemCatFilteration = "";
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            itemCatFilteration = DmServer.getItemCatFilteration(v_Type, cnTemp);

            if (!DmShared.xNull(itemCatFilteration).equalsIgnoreCase("")) {
                condition += " AND im.itemCategoryCode IN (" + itemCatFilteration + ")";
            }

            sqlWithParam.setSQL("SELECT im.code,im.name as itemName, im.manualCode ,ig.name as itemGroup,im.sku AS sku,um.unit as skuName, "
                    + "     um.decimalPlaces as decimalPlaces ,im.propertyList as propertyList, ic.name as itemcategory,"
                    + "     im.isBestBefore as isBestBefore, isNUll(cstk.stkQty,0) as curStkOfToDepartment  "
                    + " FROM itemMast im "
                    + " LEFT JOIN itemSiteList sl ON sl.code = im.code"
                    + " LEFT JOIN itemGroup ig ON ig.code = im.itemGroup "
                    + " LEFT JOIN itemCat ic ON ic.code = im.itemCategoryCode "
                    + " LEFT JOIN ( "
                    + "     SELECT irtd.code "
                    + "     FROM itemRestrictedToDepartment irtd "
                    + "     WHERE irtd.departmentCode IN (" + DmServer.getParamList(":restrictedDepartments", restrictedDepartments) + " )"
                    + "	) virtd ON virtd.code = im.code "
                    + " LEFT JOIN ( "
                    + "     SELECT ibs.itemCode,sum(ibs.balanceQty) as stkQty "
                    + "     FROM itemBatchStock ibs "
                    + "     WHERE ibs.site_Code=:site_Code and ibs.departmentCode =:toDepartment group by ibs.itemCode "
                    + "	) cstk ON cstk.itemCode = im.code "
                    + " LEFT JOIN unitMast um ON um.code = im.sku"
                    + " WHERE im.isActive = 1 "
                    + " AND (im.filterItemType = :filterItemType OR im.isCommon = 1)"
                    + " AND virtd.code IS NULL "
                    + " AND sl.site_Code = :site_Code" + condition);
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            sqlWithParam.setStringList("restrictedDepartments", restrictedDepartments);
            sqlWithParam.setString("toDepartment", toDepartment);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHItem_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setItemCategory(DmShared.xNull(rsTemp.getString("itemcategory")));
                listNode.setItemGroup(DmShared.xNull(rsTemp.getString("itemGroup")));
                listNode.setSkuCode(DmShared.xNull(rsTemp.getString("sku")));
                listNode.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                listNode.setItemProperty(DmShared.xNull(rsTemp.getString("propertyList")));
                listNode.setAllowDecimalPlaces(DmShared.xNull(rsTemp.getString("decimalPlaces")));
                listNode.setIsBestBefore(DmShared.xNull(rsTemp.getString("isBestBefore")));
                listNode.setCurStkOfToDepartment(DmServer.vNull(rsTemp.getString("curStkOfToDepartment")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean allDepartment, boolean blnToEndOfFile, String department) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String condition = "";
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!allDepartment) {
                if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                    condition += " and dm.subCode in (" + DmServer.getParamList(":department", STServer.getSsnDepartmentCodeList(this)) + ")";
                }
            }

            sqlWithParam.setSQL("select	dm.subCode as code, sg.name,"
                    + "          sgsl.site_Code as link_Site_Code,sm.name as siteName "
                    + "		from departmentMast as dm "
                    + "		left join subGroup as sg on dm.subCode = sg.subCode "
                    + "		left join subGroupNature as sgn on sgn.code = sg.nature "
                    + "         Left join subGroupSiteList as sgsl on sg.subCode=sgsl.subCode "
                    + "         left join siteMast as sm on sm.code = sgsl.site_Code "
                    + "		where sg.isActive = 1 "
                    + "         and  sgsl.site_Code = :siteCode "
                    + "         and sgn.manualCode in ('CM','DP','PR') "
                    + "         and dm.subCode <> :subCode " + condition);
            if (!allDepartment) {
                if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                    sqlWithParam.setStringList("department", STServer.getSsnDepartmentCodeList(this));
                }
            }
            sqlWithParam.setString("subCode", DmShared.iNull(department).toString());
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("siteName")));
                listNode.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("link_Site_Code")));
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
    public DmResultPageing<List<TxtHRequisition_Mdl>> getTxtHSubGroup(String v_Type, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHRequisition_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHRequisition_Mdl> lstTemp = new ArrayList<>();
        TxtHRequisition_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("SELECT sg.subCode as CODE, sg.name as NAME, sg1.departmentCode, sdm.name as departmentName "
                    + "        FROM subGroup AS sg "
                    + "        LEFT JOIN subgroupNature AS sgn ON sg.nature = sgn.code "
                    + "        left join subGroupSiteList as sgsl on sg.subCode = sgsl.subCode "
                    + "	       LEFT JOIN subGroup2 AS sg1 ON sg.subCode=sg1.subCode "
                    + "        LEFT JOIN departmentMast AS dm ON sg1.departmentCode=dm.subCode "
                    + "        left join subgroup sdm on dm.subcode = sdm.subcode "
                    + "        WHERE  sgsl.site_Code = :site_Code and sg.isActive = 1 and sgn.code IN (SELECT acf.Nature FROM AcFilteration acf WHERE acf.v_Type = :v_Type)");
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("v_Type", DmShared.iNull(v_Type).toString());
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHRequisition_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("NAME")));
                listNode.setDepartmentCode(DmShared.xNull(rsTemp.getString("departmentCode")));
                listNode.setDepartmentName(DmShared.xNull(rsTemp.getString("departmentName")));
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
    public DmResult<String> setApproval(String approvedBy, boolean isApprove, String docId) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        dmrTemp.setData("");

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            spBegin = new DmSavePoint(cnTemp);

            if (isApprove) {
                STApprovals.requisitionApproval(WalkThroughEnum.ACCEPT, docId, approvedBy, cnTemp);
            } else {
                STApprovals.requisitionApproval(WalkThroughEnum.DECLINE, docId, approvedBy, cnTemp);
            }
            dmrTemp.setData(docId);

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
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
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
                    STApprovals.requisitionApproval(walkThroughEnum, keyCode, userName, cnTemp);
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
    public DmResultPageing<List<TxtHPriority_Mdl>> getTxtHPriority(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHPriority_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHPriority_Mdl> lstTemp = new ArrayList<TxtHPriority_Mdl>();
        TxtHPriority_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " select code, name, shortName, priorityIndex from hl7Mast where tableName='priorityType' and isActive = 1 ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHPriority_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("shortName")));
                listNode.setPriorityFullName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setPriorityIndex(DmShared.xNull(rsTemp.getString("priorityIndex")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHRequisitionType(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT V_type, Description FROM Voucher_Type VT WHERE Category = '" + v_Category + "'");
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
    public DmResult<DefaultValue_Mdl> getDefaultValue() {
        DmResultPageing<DefaultValue_Mdl> dmrTemp = new DmResultPageing();
        Connection cnTemp = null;
        String strSQL;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DefaultValue_Mdl mrTemp = new DefaultValue_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            strSQL = "select  dm.subCode as code, sg.displayName as name  "
                    + "		from departmentMast as dm "
                    + "		left join subGroup as sg on dm.subCode = sg.subCode "
                    + "		left join subGroupNature as sgn on sgn.code = sg.nature "
                    + "  where sg.IsActive = 1 and dm.subCode=:depCode and sgn.manualCode IN ('CM','DP','PR') ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("depCode", DmShared.xNull(STServer.getSsnDepartmentCode(this)));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setDepartmentCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setDepartmentName(DmShared.xNull(rsTemp.getString("name")));
            }
            psTemp.close();
            rsTemp.close();

            strSQL = "SELECT V_type as code, Description as name FROM Voucher_Type VT WHERE Category = '" + v_Category + "' AND VT.isActive = 1";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setRequisitionTypeCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setRequisitionTypeName(DmShared.xNull(rsTemp.getString("name")));
            }
            psTemp.close();
            rsTemp.close();
            mrTemp.setCurrentDate(DmServer.getServerDateTime()[0]);

            strSQL = "SELECT sg.subCode AS code, sg.name FROM subGroup sg WHERE sg.subCode = :subCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("subCode", DmShared.xNull(DmServer.getSsnUserLinkedAccountCode(this)));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setRequisitionByCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setRequisitionByName(DmShared.xNull(rsTemp.getString("name")));
            }
            psTemp.close();
            rsTemp.close();

            /*==============================<< Get Enviro Data >>====================================*/
            strSQL = "SELECT e.inv_DefaultPurchaseDepartment,sg.name as defaultPurchaseDepartmentName \n"
                    + "FROM enviro e\n"
                    + "left join subGroup sg on e.inv_DefaultPurchaseDepartment=sg.subCode \n"
                    + "WHERE e.site_Code = :site_Code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setDefaultPurchaseDepartment(DmShared.xNull(rsTemp.getString("inv_DefaultPurchaseDepartment")));
                mrTemp.setDefaultPurchaseDepartmentText(DmShared.xNull(rsTemp.getString("defaultPurchaseDepartmentName")));
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
