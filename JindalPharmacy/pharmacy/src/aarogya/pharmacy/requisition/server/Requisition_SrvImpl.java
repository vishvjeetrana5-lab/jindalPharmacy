package aarogya.pharmacy.requisition.server;

import aarogya.pharmacy.requisition.shared.*;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.modifyReason.server.ModifyReason_Server;
import aarogya.selfToolkit.stStatic.server.STServer;
import dataman.dmToolkit.walkThrough.server.WalkThrough_Server;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough;
import dataman.dmToolkit.walkThrough.shared.WalkThroughDefault_Mdl;
import dataman.dmToolkit.walkThrough.shared.WalkThroughEnum;
import dataman.dmToolkit.walkThrough.shared.WalkThrough_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Requisition_SrvImpl extends DmRSSPrivate implements Requisition_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String vCatPatient = "'" + pvc.IPD + "','" + pvc.EMERGENCY + "'";
    private String v_Category = pvc.PHARMACY_REQUISITION;
    private final String voucherTypeFieldName = "rq.V_Type", voucherDateFieldName = "rq.V_Date";
    private String v_CategoryTransfer = new ProjectVoucherCategory().MEDICINE_TRANSFER;
    boolean InventoryAccountingActive = false;

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission) {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        List<KeyFieldExtended> lstKeyField = new ArrayList<>();
        KeyFieldExtended lstKeyFieldTmp;
        String sqlQuery = "", sqlCondition = "";
        dmrTemp.setData(lstKeyField);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " SELECT docid as code "
                    + " FROM requisition rq "
                    + " LEFT JOIN voucher_Type vt ON vt.v_Type=rq.v_Type"
                    + " where vt.category='" + v_Category + "' and rq.site_Code = :site_Code and rq.v_prefix = :vPrefix ";

            sqlCondition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypeFieldName, userPermission);
            if (!sqlCondition.equalsIgnoreCase("")) {
                sqlQuery += "and " + sqlCondition;
            }
            sqlQuery += " Order By rq.v_Date desc , vt.short_Name, rq.v_No desc, rq.recId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp.setString("vPrefix", DmServer.getSsnVoucherPrefix(this));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lstKeyFieldTmp = new KeyFieldExtended();
                lstKeyFieldTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
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
        String sqlQuery;
        Requisition_Mdl mrTemp = new Requisition_Mdl();
        List<GrdMedicineDetail_Mdl> lstTemp = new ArrayList<GrdMedicineDetail_Mdl>();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT rq.docId, rq.recId, rq.v_Date,sg.subCode,sg.name AS subgroup,vt.description,rq.remark, "
                    + " sdp.subcode AS departmentCode,sdp.name departmentName,rq.preparedBy,"
                    + " rq.departmentToCode,sdpto.name departmentToName,rq.regDocId,"
                    + " rq.u_Name,rq.u_EntDt_LatestLine,rq.wtaCode,rq.approvedBy ,rq.v_type,hl7.shortName as priorityName,rq.priorityTypeCode,rq.recIdPrefix,rq.v_No, "
                    + " rq.site_Code_to,sm.name as siteName "
                    + " from requisition rq "
                    + " LEFT JOIN subGroup sg ON sg.subCode = rq.requestedByCode "
                    + " LEFT JOIN subgroup sdp ON sdp.subcode = rq.departmentCode"
                    + " LEFT JOIN subgroup sdpto ON sdpto.subcode = rq.departmentToCode"
                    + " LEFT JOIN voucher_Type vt ON vt.v_Type=rq.v_Type "
                    + " left join hl7Mast  as hl7 on hl7.code  = rq.priorityTypeCode"
                    + " left join siteMast as sm on sm.code =rq.site_Code_To  "
                    + " where rq.docid=:docid ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docid", strKey);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docid")));
                mrTemp.getRecid().setRecIdValue(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                mrTemp.setDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                mrTemp.setDepartmentByName(DmShared.xNull(rsTemp.getString("departmentName")));
                mrTemp.setDepartmentByCode(DmShared.xNull(rsTemp.getString("departmentCode")));
                mrTemp.setRequisitionTypeCode(DmShared.xNull(rsTemp.getString("v_Type")));
                mrTemp.setRequisitionTypeName(DmShared.xNull(rsTemp.getString("description")));
                mrTemp.setDepartmentToName(DmShared.xNull(rsTemp.getString("departmentToName")));
                mrTemp.setDepartmentToCode(DmShared.xNull(rsTemp.getString("departmentToCode")));
                mrTemp.setSubCode(DmShared.xNull(rsTemp.getString("subCode")));
                mrTemp.setSubName(DmShared.xNull(rsTemp.getString("subgroup")));
                mrTemp.setWtaCode(DmShared.xNull(rsTemp.getString("wtaCode")));
                mrTemp.setPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setApprovedBy(DmShared.xNull(rsTemp.getString("approvedBy")));
                mrTemp.setDocIdOPD(DmShared.xNull(rsTemp.getString("regDocId")));
                mrTemp.setPriorityTypeCode(DmShared.xNull(rsTemp.getString("priorityTypeCode")));
                mrTemp.setPriorityTypeName(DmShared.xNull(rsTemp.getString("priorityName")));
                mrTemp.setLinkSiteCode(DmShared.xNull(rsTemp.getString("site_Code_to")));
                mrTemp.setLinkSiteName(DmShared.xNull(rsTemp.getString("siteName")));

                mrTemp.getRecid().setRecIdValue(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.getRecid().setCounter(DmShared.iNull(rsTemp.getString("v_No")).longValue());
                mrTemp.getRecid().setPrefix(DmShared.xNull(rsTemp.getString("recIdPrefix")));

                if (rsTemp.getTimestamp("u_EntDt_LatestLine") != null) {
                    mrTemp.setUserEntDtLatestLine(String.valueOf(rsTemp.getTimestamp("u_EntDt_LatestLine").getTime()));
                }
            }
            psTemp.close();
            rsTemp.close();

            sqlQuery = " SELECT rqd.itemCode AS medicineCode, mm.name as medicineName, rqd.reqQty, rqd.reqDate,  "
                    + " rqd.remark, rqd.isStatus, ump.unit AS unitName, ump.unit as skuName,ump.decimalPlaces,rqd.v_SNo,rqd.itemCurrPosition "
                    + " ,um.unit as purchaseUnitName, um.decimalPlaces as purchaseUnitDecimalPlaces"
                    + " from requisitionDetail rqd "
                    + " LEFT JOIN itemMast mm ON mm.code = rqd.itemCode "
                    + " LEFT JOIN unitmast AS um ON mm.purchaseUnit = um.code"
                    + " LEFT JOIN unitMast ump ON ump.code = mm.sku "
                    + " where docid = :docid ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docid", strKey);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                GrdMedicineDetail_Mdl node = new GrdMedicineDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("medicineCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("medicineName")));
                node.setDate(DmShared.dNull(rsTemp.getDate("reqDate")));
                node.setvSNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                node.setQty(DmServer.vNull(rsTemp.getString("reqQty")));
                node.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                node.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                node.setAllowDecimalPlaces(DmShared.xNull(rsTemp.getString("decimalPlaces")));
                node.setIsStatus(DmShared.xNull(rsTemp.getString("isStatus")));
                node.setItemCurrPosition(DmShared.xNull(rsTemp.getString("itemCurrPosition")));
                node.setPurchaseUnit(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                node.setPurchaseDecimalPlaces(DmShared.xNull(rsTemp.getString("purchaseUnitDecimalPlaces")));
                lstTemp.add(node);
            }

            mrTemp.getGrdData().addAll(lstTemp);

            getPatientDetail(mrTemp, cnTemp);
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

            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                DmServer.checkBackwordVoucherDateTime("stock", "v_Date", "", mrTemp.getDate(), "", cnTemp);
            }

            if (!DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                STServer.chkIfDocumentUsed("requisitionDocId", "indentDetail", mrTemp.getSharedKeyFieldCode(), cnTemp);
                STServer.chkIfDocumentUsed("requisitionDocId", "stockHeadDetail", mrTemp.getSharedKeyFieldCode(), cnTemp);

                sqlQuery = "SELECT u_EntDt_LatestLine FROM Requisition WHERE docId = :docId";
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
                                throw new UnsupportedOperationException("Medicine " + mrTemp.getGrdData().get(i).getSharedKeyFieldName() + " is not applicable in given department.");
                            }
                        }
                        psTemp.close();
                        rsTemp.close();
                    }
                }
            }

            spBegin = new DmSavePoint(cnTemp);
            DmServer.checkLockDate(DmServer.getSsnSiteCode(this), DmServer.getSsnVoucherPrefix(this), cnTemp);
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

            if (blnAdd) {

                //=======================================Check for Medicine Transfer======================================
                sqlQuery = "select COUNT(sh.docID) AS totalInvalidRecords   "
                        + "    from stockHead sh  "
                        + "    left join stockHeadDetail shd on sh.docID=shd.docID"
                        + "    left join voucher_Type vt on sh.v_Type=vt.v_Type  "
                        + "  where vt.category= '" + v_CategoryTransfer + "' and shd.departmentToCode= :departmentCode "
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

                sqlQuery = "INSERT INTO Requisition (docId, v_Type,v_No,  recId, v_Date, v_Prefix, departmentCode, requestedByCode, site_Code,"
                        + " preparedBy,  u_EntDt,u_EntDt_LatestLine,wtaCode,departmentToCode,regDocId,priorityTypeCode,remark,site_Code_to,recIdPrefix)"
                        + " VALUES (:docid, :v_type,:v_No,  :recid, :v_date, :v_prefix, :departmentcode, :requestedbycode, :site_code, :preparedby,"
                        + " :uEntDt,:uEntDt,:wtaCode,:departmentToCode,:regDocId,:priorityTypeCode,:remark,:site_Code_to,:recIdPrefix) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("docid", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("v_type", DmShared.xNull(voucherType));
                psTemp.setString("v_No", DmShared.iNull(vNo).toString());
                psTemp.setString("recid", DmShared.xNull(mrTemp.getRecid().getRecIdValue()));
                psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getRecid().getPrefix()));
                psTemp.setDate("v_date", DmServer.convertDate(mrTemp.getDate()));
                psTemp.setString("v_prefix", DmShared.xNull(voucherPrefix));
                psTemp.setString("requestedbycode", DmShared.foriegnKey(mrTemp.getSubCode()));
                psTemp.setString("departmentcode", DmShared.foriegnKey(mrTemp.getDepartmentByCode()));
                psTemp.setString("departmentToCode", DmShared.foriegnKey(mrTemp.getDepartmentToCode()));
                psTemp.setString("wtaCode", DmShared.foriegnKey(mrTemp.getWtaCode()));
                psTemp.setString("regDocId", DmShared.foriegnKey(mrTemp.getDocIdOPD()));
                psTemp.setString("priorityTypeCode", DmShared.foriegnKey(mrTemp.getPriorityTypeCode()));
                psTemp.setString("site_code", DmShared.xNull(DmServer.getSsnSiteCode(this)));
                psTemp.setString("preparedby", DmShared.xNull(DmServer.getSsnUserName(this)));
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setString("site_Code_to", DmShared.foriegnKey(mrTemp.getLinkSiteCode()));

            } else {
                sqlQuery = "UPDATE requisition set "
                        + " departmentCode = :departmentcode, "
                        + " departmentToCode = :departmentToCode, "
                        + " requestedByCode = :requestedbycode, "
                        + " priorityTypeCode = :priorityTypeCode,"
                        + " site_Code_to=:site_Code_to, "
                        + " u_Name = :u_name,"
                        + " remark = :remark,"
                        + " u_EntDt=:uEntDt, "
                        + " v_date=:v_date, "
                        + " u_EntDt_LatestLine=:uEntDt"
                        + " where docid=:docid ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("departmentcode", DmShared.foriegnKey(mrTemp.getDepartmentByCode()));
                psTemp.setString("departmentToCode", DmShared.foriegnKey(mrTemp.getDepartmentToCode()));
                psTemp.setString("requestedbycode", DmShared.foriegnKey(mrTemp.getSubCode()));
                psTemp.setString("priorityTypeCode", DmShared.foriegnKey(mrTemp.getPriorityTypeCode()));
                psTemp.setString("u_Name", DmShared.xNull(DmServer.getSsnUserName(this)));
                psTemp.setString("docid", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setString("site_Code_to", DmShared.foriegnKey(mrTemp.getLinkSiteCode()));
                psTemp.setDate("v_date", DmServer.convertDate(mrTemp.getDate()));

            }
            psTemp.executeUpdate();
            psTemp.close();

            //==================================================================
            sqlQuery = "Delete From requisitionDetail where docid= :docid and (isStatus) not in ('1','2') ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docid", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();

            int vSNo = 0;
            for (int i = 0; i < mrTemp.getGrdData().size(); i++) {
                GrdMedicineDetail_Mdl node = mrTemp.getGrdData().get(i);
                if (!DmShared.xNull(node.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                    if (DmShared.iNull(node.getvSNo()).intValue() > vSNo) {
                        vSNo = DmShared.iNull(node.getvSNo()).intValue();
                    }
                    if (!DmShared.xNull(node.getIsStatus()).equalsIgnoreCase("1") && !DmShared.xNull(node.getIsStatus()).equalsIgnoreCase("2")) {
                        node.setvSNo(String.valueOf(++vSNo));
                        sqlQuery = " INSERT INTO requisitionDetail (docId, v_SNo, itemCode, reqQty, reqDate,remark)"
                                + "VALUES (:docid, :v_sno, :itemCode, :reqqty, :reqdate,:remark) ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docid", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                        psTemp.setString("v_SNo", DmShared.iNull(node.getvSNo()).toString());
                        psTemp.setString("itemCode", DmShared.foriegnKey(node.getSharedKeyFieldCode()));
                        psTemp.setString("reqqty", DmShared.xNull(node.getQty()));
                        psTemp.setDate("reqdate", DmServer.convertDate(node.getDate()));
                        psTemp.setString("remark", DmShared.xNull(node.getRemark()));
                        psTemp.executeUpdate();
                        psTemp.close();

//                        ===============================================================================================================
//                        ===============================================================================================================
                        for (int j = 0; j < node.getLstOldRequisition().size(); j++) {
                            KeyFieldExtended reqisitiondet = node.getLstOldRequisition().get(j);
                            sqlQuery = " update requisitiondetail set indentQty=reqQty-isnull(issueQty,0) "
                                    + "where docid=:docid and v_Sno=:v_Sno ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("docid", DmShared.xNull(reqisitiondet.getSharedKeyFieldCode()));
                            psTemp.setString("v_SNo", DmShared.xNull(reqisitiondet.getSharedKeyFieldName()));
                            psTemp.executeUpdate();
                            psTemp.close();
                        }
//                        ===============================================================================================================
//                        ===============================================================================================================
                    }
                }
            }

            //========================Updating current Bal stk, last 90days consumption, pending balance approved Qty====================================//
            sqlQuery = "update rqd set rqd.itemCurrPosition =crs.itemCurrPosition \n"
                    + "from requisitionDetail rqd \n"
                    + "left join ( \n"
                    + "select rqd.itemCode,'Bal:' +cast(convert(decimal(13,2), round(max(isnull(ibs.balanceQty,0)),2)) as varchar(15)) + \n"
                    + "'    90 days Consumption:'+cast(convert(decimal(13,2), round(max(isNull(stk.last90daysConsumption,0)),2)) as varchar(15)) + \n"
                    + "'     Apr Qty:'+cast(convert(decimal(13,2), round(max(isNull(rqp.pendingApprovedQty,0)),2)) as varchar(15)) as itemCurrPosition \n"
                    + "from requisition rq \n"
                    + "left join requisitionDetail rqd on rq.docid=rqd.docid \n"
                    + "left join (\n"
                    + " select sum(balanceQty) as balanceQty,itemCode \n"
                    + "		from itemBatchStock ibs where ibs.departmentCode=:departmentCode  \n"
                    + "		and ibs.site_Code=:site_Code\n"
                    + "		group by itemCode \n"
                    + "		) as ibs on rqd.itemCode=ibs.itemCode \n"
                    + "left join( \n"
                    + "	select stk.itemCode,sum(isNull(stk.isuQty,0)) as last90daysConsumption \n"
                    + "		from stock stk \n"
                    + "		where stk.v_Date between dateAdd(d,-90,:vDate) and :vDate \n"
                    + "		and stk.departmentCode=:departmentCode \n"
                    + "		and stk.site_Code=:site_Code\n"
                    + "		group by stk.itemCode \n"
                    + "		) stk on ibs.itemCode=stk.itemCode \n"
                    + "left join( \n"
                    + "	select rqd.itemCode,sum(rqd.reqQty-isNull(rqd.issueQty,0)-isNull(rqd.indentQty,0)) as pendingApprovedQty  \n"
                    + "		from requisition rq \n"
                    + "		left join requisitionDetail rqd on rq.docid=rqd.docid \n"
                    + "		where isnull(rq.approvedBy,'')<>'' and rqd.isStatus=0 and rq.departmentCode=:departmentCode \n"
                    + "		and rq.site_Code=:site_Code\n"
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
            psTemp.setString("site_Code", DmShared.xNull(DmServer.getSsnSiteCode(this)));
            psTemp.executeUpdate();
            psTemp.close();
            //==================================================================//

            if (DmShared.iNull(mrTemp.getWtaCode()).longValue() == 0 && isApprove) {
                documentApproval(WalkThroughEnum.ACCEPT, mrTemp.getSharedKeyFieldCode(), DmServer.getSsnUserName(this), cnTemp);
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
        String sqlQuery, wtaCode = "";
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            STServer.chkIfDocumentUsed("requisitionDocId", "indentDetail", strKey, cnTemp);
            STServer.chkIfDocumentUsed("requisitionDocId", "stockHeadDetail", strKey, cnTemp);

            sqlQuery = "select wtaCode,approvedBy  FROM requisition WHERE  docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
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
            DmServer.checkLockDate(DmServer.getSsnSiteCode(this), DmServer.getSsnVoucherPrefix(this), cnTemp);
            WalkThrough_Server.deleteWalkThrough(wtaCode, cnTemp);

            ModifyReason_Server.delete(strKey, cnTemp);

            sqlQuery = "Delete From requisitionDetail Where docid= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "Delete From requisition Where docid= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
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
        String sqlQuery, sqlCondition = "";

        try {
            rstTemp.setData(lstTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "SELECT rq.docId,rq.recId as name, rq.recId, rq.v_Date,sg.name AS subgroup,rq.v_No,"
                    + " sdp.name departmentName,rq.approvedBy,format(rq.approvalDateTime,'dd-MMM-yyyy HH.mm') as approvalDateTime,"
                    + " sdpto.name departmentToName,rq.departmentToCode,vt.short_Name  "
                    + " from requisition rq "
                    + " LEFT JOIN subGroup sg ON sg.subCode = rq.requestedByCode "
                    + " LEFT JOIN subgroup sdp ON sdp.subcode= rq.departmentCode "
                    + " LEFT JOIN subgroup sdpto ON sdpto.subcode = rq.departmentToCode"
                    + " LEFT JOIN voucher_Type vt ON vt.v_Type=rq.v_Type"
                    + " where vt.category='" + v_Category + "' and rq.site_Code = :site_Code and rq.v_prefix = :vPrefix ";
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
                listNode.setDepartmentBy(DmShared.xNull(rsTemp.getString("departmentName")));
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
    public DmResultPageing<List<TxtHMedicine_Mdl>> getTxtHMedicineName(DmHelper helper, boolean blnToEndOfFile, String restrictedDepartments, String toDepartment) {
        DmResultPageing<List<TxtHMedicine_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHMedicine_Mdl> lstTemp = new ArrayList<>();
        TxtHMedicine_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String condition = "";
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            condition += " and isNull(mm.isActive,0) <> 0 ";
            sqlWithParam.setSQL("SELECT mm.code,mm.name as medicineName, mm.manualCode, ump.decimalPlaces as decimalPlaces, mg.name as medicineGroup,"
                    + " mc.name as medicinecategory,"
                    + " ump.unit as skuName,isNUll(cstk.stkQty,0) as curStkOfToDepartment , um.unit as purchaseUnitName, um.decimalPlaces as purchaseUnitDecimalPlaces "
                    + " FROM itemMast mm "
                    + " LEFT JOIN unitmast AS um ON mm.purchaseUnit = um.code"
                    + " LEFT JOIN itemSiteList sl ON sl.code = mm.code"
                    + " LEFT JOIN itemGroup mg ON mg.code = mm.itemGroup "
                    + " LEFT JOIN itemCat mc ON mc.code = mm.itemCategoryCode "
                    + " LEFT JOIN ( "
                    + "                 SELECT irtd.code "
                    + "                 FROM itemRestrictedToDepartment irtd "
                    + "                 WHERE irtd.departmentCode IN (" + DmServer.getParamList(":restrictedDepartments", restrictedDepartments) + " )"
                    + "		) virtd ON virtd.code = mm.code "
                    + " LEFT JOIN unitMast ump ON ump.code = mm.sku "
                    + "LEFT JOIN (      \n"
                    + "		SELECT ibs.itemCode,sum(ibs.balanceQty) as stkQty     \n"
                    + "		FROM itemBatchStock ibs      \n"
                    + "		WHERE ibs.site_Code='1' and ibs.departmentCode =:toDepartment group by ibs.itemCode 	\n"
                    + "		) cstk ON cstk.itemCode = mm.code  "
                    + " WHERE (mm.filterItemType = :filterItemType OR mm.isCommon = 1)"
                    + " AND sl.site_Code = :site_Code"
                    + " AND virtd.code IS NULL " + condition);
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setStringList("restrictedDepartments", restrictedDepartments);
            sqlWithParam.setString("toDepartment", toDepartment);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHMedicine_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("medicineName")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setMedicineCategory(DmShared.xNull(rsTemp.getString("medicinecategory")));
                listNode.setMedicineGroup(DmShared.xNull(rsTemp.getString("medicineGroup")));
                listNode.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                listNode.setCurStkOfToDepartment(DmServer.vNull(rsTemp.getString("curStkOfToDepartment")));
//                listNode.setUnitName(DmShared.xNull(rsTemp.getString("unitName")));
                listNode.setAllowDecimalPlaces(DmShared.xNull(rsTemp.getString("decimalPlaces")));
                listNode.setPurchaseUnit(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                listNode.setPurchaseDecimalPlaces(DmShared.xNull(rsTemp.getString("purchaseUnitDecimalPlaces")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(boolean alldepartment, DmHelper helper, boolean blnToEndOfFile, String department) {
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
            if (!alldepartment) {
                if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                    condition += " and dm.subCode in (" + DmServer.getParamList(":department", STServer.getSsnDepartmentCodeList(this)) + ")";
                }
            }

            condition += " and sgn.manualCode in ('CM','PH')";
            sqlWithParam.setSQL("select	dm.subCode as code, sg.name,"
                    + "          isnull(envDept.link_Site_Code, :siteCode) as link_Site_Code,sm.name as siteName "
                    + "		from departmentMast as dm "
                    + "		left join subGroup as sg on dm.subCode = sg.subCode "
                    + "		left join subGroupNature as sgn on sgn.code = sg.nature "
                    + "         left join enviroDepartmentSiteLink as envDept on dm.subcode = envDept.departmentCode "
                    + "         left join siteMast as sm   on sm.code = isNull(envDept.link_Site_Code,:siteCode) "
                    + "         left join subGroupSiteList sgl ON sg.subcode = sgl.subCode"
                    + "		where sg.isActive = 1 and dm.subCode <> :subCode and sgl.site_Code=:siteCode " + condition);
            if (!alldepartment) {
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
    public DmResult<DefaultValue_Mdl> getDefaultValue() {
        DmResultPageing<DefaultValue_Mdl> dmrTemp = new DmResultPageing();
        Connection cnTemp = null;
        String sqlQuery;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DefaultValue_Mdl mrTemp = new DefaultValue_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "select  dm.subCode as code, sg.displayName as name  "
                    + "		from departmentMast as dm "
                    + "		left join subGroup as sg on dm.subCode = sg.subCode "
                    + "  where sg.IsActive = 1 and dm.subCode=:depCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("depCode", DmShared.xNull(STServer.getSsnDepartmentCode(this)));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setDepartmentCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setDepartmentName(DmShared.xNull(rsTemp.getString("name")));

            }
            psTemp.close();
            rsTemp.close();

            sqlQuery = "SELECT V_type as code, Description as name FROM Voucher_Type VT WHERE VT.IsActive=1 And Category = '" + v_Category + "'";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setRequisitionTypeCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setRequisitionTypeName(DmShared.xNull(rsTemp.getString("name")));
            }
            psTemp.close();
            rsTemp.close();
            mrTemp.setCurrentDate(DmServer.getServerDateTime()[0]);
            mrTemp.setCurrentTime(DmServer.getServerDateTime()[1]);

            sqlQuery = "SELECT sg.subCode AS code, sg.name FROM subGroup sg WHERE sg.subCode = :subCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("subCode", DmShared.xNull(DmServer.getSsnUserLinkedAccountCode(this)));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setRequisitionByCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setRequisitionByName(DmShared.xNull(rsTemp.getString("name")));
            }
            psTemp.close();
            rsTemp.close();

            /*==============================<< Get Enviro Data >>====================================*/
            sqlQuery = "SELECT e.inv_DefaultPurchaseDepartment,sg.name as defaultPurchaseDepartmentName \n"
                    + "FROM enviro e\n"
                    + "left join subGroup sg on e.inv_DefaultPurchaseDepartment=sg.subCode \n"
                    + "WHERE e.site_Code = :site_Code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setDefaultPurchaseDepartment(DmShared.xNull(rsTemp.getString("inv_DefaultPurchaseDepartment")));
                mrTemp.setDefaultPurchaseDepartmentText(DmShared.xNull(rsTemp.getString("defaultPurchaseDepartmentName")));
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
                InventoryAccountingActive = (DmShared.iNull(rsTemp.getString("isInventoryAccountingActive")).intValue() == 1);
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
    public DmResultPageing<List<TxtHRequisition_Mdl>> getTxtHSubGroup(String vType, DmHelper helper, boolean blnToEndOfFile) {
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

            sqlWithParam.setSQL(" SELECT sg.subCode as code, sg.name, sg1.departmentCode, sdm.name as departmentName "
                    + "        FROM subGroup AS sg "
                    + "        left join subGroupSiteList as sgsl on sg.subCode = sgsl.subCode "
                    + "	       LEFT JOIN subGroup2 AS sg1 ON sg.subCode=sg1.subCode "
                    + "        LEFT JOIN departmentMast AS dm ON sg1.departmentCode=dm.subCode "
                    + "        left join subgroup sdm on dm.subcode = sdm.subcode "
                    + "        left join subGroupNature sgn ON sgn.code = sg.nature"
                    + "        WHERE sgsl.site_Code = :site_Code and sg.isActive = 1 "
                    + "        and sgn.code IN (SELECT acf.Nature FROM AcFilteration acf WHERE acf.v_Type = :v_Type) ");
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("v_Type", vType);

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHRequisition_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
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
                documentApproval(WalkThroughEnum.ACCEPT, docId, approvedBy, cnTemp);
            } else {
                documentApproval(WalkThroughEnum.DECLINE, docId, approvedBy, cnTemp);
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

    private void documentApproval(WalkThroughEnum walkThroughMove, String keyCode, String userName, Connection cnTemp) throws Exception {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";

        try {
            STServer.chkIfDocumentUsed("requisitionDocId", "indentDetail", keyCode, cnTemp);
            STServer.chkIfDocumentUsed("requisitionDocId", "stockHeadDetail", keyCode, cnTemp);

            sqlQuery = "select approvedBy,approvalDateTime,"
                    + "(case when :userName IN (ISNULL(approvedBy,''),'SA','SUPER') then 1 else 0 end) canApprove "
                    + "from requisition where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            psTemp.setString("userName", userName);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (walkThroughMove == WalkThroughEnum.ACCEPT) {
                    if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {

                        rsTemp.updateString("approvedBy", DmShared.xNull(userName).trim());
                        rsTemp.updateTimestamp("approvalDateTime", new Timestamp(new java.util.Date().getTime()));
                        rsTemp.updateRow();
                        rsTemp.close();
                        psTemp.close();

                        sqlQuery = " Update requisition set "
                                + " u_EntDt_LatestLine=:uEntDt "
                                + " where docId=:docId ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docId", DmShared.iNull(keyCode).toString());
                        psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                        psTemp.executeUpdate();
                        psTemp.close();
                    } else {
                        throw new Exception("Document already approved.");
                    }
                } else {
                    if (!DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                        if (DmShared.iNull(rsTemp.getString("canApprove")).longValue() == 1) {

                            rsTemp.updateString("approvedBy", null);
                            rsTemp.updateDate("approvalDateTime", null);
                            rsTemp.updateRow();
                            rsTemp.close();
                            psTemp.close();

                            sqlQuery = " Update requisition set "
                                    + " u_EntDt_LatestLine=:uEntDt "
                                    + " where docId=:docId ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
                            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                            psTemp.executeUpdate();
                            psTemp.close();
                        } else {
                            throw new Exception("Unauthorized user to unapprove.");
                        }
                    } else {
                        throw new Exception("Document already un-approved.");
                    }
                }
            }
            rsTemp.close();
            psTemp.close();
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
            } catch (Exception e) {
            }
        }
    }

    private void getPatientDetail(Requisition_Mdl mrTemp, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        PatientDetail_Mdl lstTemp = mrTemp.getPatientDetail();
        String sqlQuery;
        try {

            sqlQuery = "SELECT uhid.recId AS uhidRecID, uhid.docId AS uhidDocID,"
                    + "  CONCAT(uhid.patientInitial, ' ', uhid.patientAliasName) AS patientName,"
                    + "  CONCAT(uhid.guardianInitial, ' ', uhid.guardianName) AS guardianName,"
                    + "  uhid2.genderCode,hl7.name as gender, uhid.address1, uhid.address2,   uhid.dobDisplay, pr.recId AS opdRecid,"
                    + "  pr.regDocId AS OPDDocID, pr.organizationCode,   caseMast.name AS casemastName,"
                    + "  sg.name AS organizationName, sgcmr.name AS referredbyname,"
                    + "  CASE WHEN ISNULL(sg.initial, '') <> '' THEN CONCAT(sg.initial, ' ', sg.name) ELSE sg.name END AS consultantName,"
                    + "  vtr.category, uhid.mobile "
                    + " FROM patientUHID AS uhid "
                    + "  LEFT JOIN patientRegistration AS pr ON pr.uhid = uhid.docId "
                    + "  LEFT JOIN caseMast ON pr.caseCode = caseMast.code "
                    + "  LEFT JOIN organizationMast AS om ON pr.organizationCode = om.subcode "
                    + "  Left Join subGroup sg on om.subCode=sg.subCode"
                    + "  LEFT JOIN consultantMast AS cmr ON pr.referredBy = cmr.subCode "
                    + " LEFT join subGroup as sgcmr ON cmr.subCode = sgcmr.subCode "
                    + "  LEFT JOIN voucher_Type AS vtr ON vtr.v_Type = pr.v_Type "
                    + "  LEFT JOIN consultantMast AS cmc ON pr.consultantCode = cmc.subcode "
                    + " LEFT join subGroup as sgcmc ON cmc.subCode = sgcmc.subCode"
                    + " left join patientUHID2 as uhid2 on uhid.docid=uhid2.uhid "
                    + " left join hl7Mast as hl7 on uhid2.genderCode=hl7.code "
                    + " WHERE (uhid.isCancelled = 0) AND (pr.isCancelled = 0) AND (pr.regDocId = :docId)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTemp.getDocIdOPD()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                lstTemp.setDocIdUHID(DmShared.xNull(rsTemp.getString("uhidDocID")));
                lstTemp.setRecIdUHID(DmShared.xNull(rsTemp.getString("uhidRecID")));
                lstTemp.setRecIdOPD(DmShared.xNull(rsTemp.getString("opdRecid")));
                lstTemp.setDocIdOPD(DmShared.xNull(rsTemp.getString("OPDDocID")));
                lstTemp.setDisplayDOB(DmShared.xNull(rsTemp.getString("dobDisplay")));
                lstTemp.setGender(DmShared.xNull(rsTemp.getString("gender")));
                lstTemp.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                lstTemp.setAddress(DmShared.xNull(rsTemp.getString("address1")) + DmShared.xNull(rsTemp.getString("address2")));
                lstTemp.setGuardian(DmShared.xNull(rsTemp.getString("guardianName")));
                lstTemp.setPatient(DmShared.xNull(rsTemp.getString("patientName")));
                lstTemp.setCaseText(DmShared.xNull(rsTemp.getString("casemastName")));
                lstTemp.setReferredText(DmShared.xNull(rsTemp.getString("referredbyname")));
                lstTemp.setOrganizationText(DmShared.xNull(rsTemp.getString("organizationname")));
                lstTemp.setConsultantText(DmShared.xNull(rsTemp.getString("consultantName")));
                rsTemp.close();
                psTemp.close();
            }
            psTemp.close();
            rsTemp.close();
        } catch (SQLException | DmException e) {
            throw new DmException(0, e.getMessage());
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
    public DmResult<String> walkThrough(WalkThrough_Mdl walkThrough) {
        final DmResult<String> dmrTemp = new DmResult();

        try {
            WalkThrough_Server.walkThrough(walkThrough, new IWalkThrough() {
                @Override
                public void changeStatus(WalkThroughEnum walkThroughEnum, String keyCode, String userName, Connection cnTemp) throws Exception {
                    documentApproval(walkThroughEnum, keyCode, userName, cnTemp);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHRequisitiontype(DmHelper helper, boolean blnToEndOfFile) {
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
}
