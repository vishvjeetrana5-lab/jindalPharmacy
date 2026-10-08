package aarogya.pharmacy.requisitionSearch.server;

import aarogya.pharmacy.requisitionSearch.shared.*;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.server.STServer;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RequisitionSearch_SrvImpl extends DmRSSPrivate implements RequisitionSearch_Srv {

    @Override
    public DmResult<List<GrdRequisitionDetailsSearch_Mdl>> getRequisitionSearchDetail(RequsitionSearchValue_Mdl searchValue_Mdl) {
        DmResult<List<GrdRequisitionDetailsSearch_Mdl>> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String condition = "", sqlQuery = "", join = "", quantity = "0", groupByCondition = "", havingCondition = "";
        List<GrdRequisitionDetailsSearch_Mdl> lstTemp = new ArrayList<GrdRequisitionDetailsSearch_Mdl>();
        GrdRequisitionDetailsSearch_Mdl node;
        rstTemp.setData(lstTemp);

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (searchValue_Mdl.getVoucherCategory().equalsIgnoreCase(new ProjectVoucherCategory().PHARMACY_BILL) 
                    || searchValue_Mdl.getVoucherCategory().equalsIgnoreCase(new ProjectVoucherCategory().PHARMACY_ESTIMATE_BILL)
                    || searchValue_Mdl.getVoucherCategory().equalsIgnoreCase(new ProjectVoucherCategory().PHARMACY_BILL_WHOLESALE_CASH)) {
                if (DmShared.iNull(searchValue_Mdl.getFormDocId()).longValue() != 0) {
                    join = "         left join ( \n"
                            + "             SELECT sbd.requisitionDocId,SUM(sbd.quantity) as quantity \n"
                            + "             FROM saleBillDetail sbd \n"
                            + "             group by sbd.requisitionDocId \n"
                            + "         ) vEid on vEid.requisitionDocId = mrq.docid \n";

                    quantity = " sum(ISNULL(mrqd.reqQty,0) - ISNULL(mrqd.issueQty,0)+ISNULL(vEid.quantity,0))  ";
                    havingCondition += " having sum(ISNULL(mrqd.reqQty,0) - ISNULL(mrqd.issueQty,0) + ISNULL(vEid.quantity,0)) > 0 \n";
                } else {
                    quantity = " sum(ISNULL(mrqd.reqQty,0) - ISNULL(mrqd.issueQty,0))  ";
                    havingCondition += " having sum(ISNULL(mrqd.reqQty,0) - ISNULL(mrqd.issueQty,0)) > 0  \n";
                    condition = " AND mrqd.isStatus=0  ";
                }

            }

            if (DmShared.iNull(searchValue_Mdl.getReqDocId()).longValue() > 0) {
                condition += " and mrqd.docId =:docIdReq ";
            }

            if (DmShared.iNull(searchValue_Mdl.getPriorityNameValue()).longValue() > 0) {
                condition += " and h7.code =:priorityCode ";
            }

            if (!DmShared.xNull(searchValue_Mdl.getFieldName()).equalsIgnoreCase("") && !DmShared.xNull(searchValue_Mdl.getFieldNameValue()).equalsIgnoreCase("")) {
                if (searchValue_Mdl.getFieldName().equalsIgnoreCase("mediname")) {
                    condition += " and im.name Like '" + searchValue_Mdl.getFieldNameValue().toLowerCase() + "%'";
                } else if (searchValue_Mdl.getFieldName().equalsIgnoreCase("quantity")) {
                    condition += " and " + quantity + " Like '" + searchValue_Mdl.getFieldNameValue().toLowerCase() + "%'";
                } else if (searchValue_Mdl.getFieldName().equalsIgnoreCase("skuname")) {
                    condition += " and um.unit Like '" + searchValue_Mdl.getFieldNameValue().toLowerCase() + "%'";
                } else if (searchValue_Mdl.getFieldName().equalsIgnoreCase("remark")) {
                    condition += " and mrqd.remark Like '" + searchValue_Mdl.getFieldNameValue().toLowerCase() + "%'";
                } else if (searchValue_Mdl.getFieldName().equalsIgnoreCase("recId")) {
                    condition += " and mrq.recid Like '" + searchValue_Mdl.getFieldNameValue().toLowerCase() + "%'";
                } else if (searchValue_Mdl.getFieldName().equalsIgnoreCase("departmentname")) {
                    condition += " and dm.name Like '" + searchValue_Mdl.getFieldNameValue().toLowerCase() + "%'";
                } else if (searchValue_Mdl.getFieldName().equalsIgnoreCase("reqDate")) {
                    condition += " and CONVERT(varchar, mrqd.redDate, 105) Like '" + searchValue_Mdl.getFieldNameValue().toLowerCase() + "%'";
                }
            }
            /*=================================  Set Conditions ===============================================*/
            if (!DmShared.xNull(searchValue_Mdl.getItemCode()).equalsIgnoreCase("")) {
                condition += " and mrqd.itemCode =:itemCode ";
            }
            if (DmShared.iNull(searchValue_Mdl.getPatientRegDocId()).longValue() != 0) {
                condition += " and mrq.regDocid =:regDocid ";
            }
            if (!DmShared.dNull(searchValue_Mdl.getV_Date()).equalsIgnoreCase("")) {
                condition += " AND mrq.v_Date <= :v_Date";
            } else {
                condition += " AND 1 = 2";
            }
            if (!DmShared.xNull(searchValue_Mdl.getDepartmentTag()).equalsIgnoreCase("")) {
                condition += " and mrq.departmentToCode='" + DmShared.xNull(searchValue_Mdl.getDepartmentTag()) + "'\n";
            } else {
                condition += " AND 1 = 2";
            }

            if (DmShared.iNull(searchValue_Mdl.getFromDepartmentTag()).intValue() != 0) {
                condition += " and mrq.departmentCode='" + DmShared.xNull(searchValue_Mdl.getFromDepartmentTag()) + "'\n";
            }

            if (searchValue_Mdl.getRequisitionWithoutUHID()) {
                groupByCondition = " group By mrq.docid\n";
            } else {
                groupByCondition = " group By mrq.docid,mrqd.v_Sno\n";
            }

//            sqlQuery = "select  mrq.docid as docid,"
//                    + "         mrqd.v_SNo,"
//                    + "         mrq.recid recid,"
//                    + "         mrq.v_Date as date,"
//                    + "         im.itsBasedOn,"
//                    + "         im.itemPacking,  "
//                    + "         mrq.departmentCode as departmentCode, "
//                    + "         dm.name as departmentName,  "
//                    + "         mrq.requestedByCode as requestedByCode, "
//                    + "         rbc.name as requestedByName, "
//                    + "		mrqd.itemCode, "
//                    + "         im.name as mediName, "
//                    + "         mrqd.isStatus , "
//                    + quantity + " as reqQty , "
//                    + "         mrqd.remark, "
//                    + "         mrqd.reqDate, "
//                    + "		um.unit as skuName, "
//                    + "		mrq.approvedBy , "
//                    + "		pu.patientName , "
//                    + "         um.code as unitCode,   "
//                    + "         h7.shortName as priority,   "
//                    + "         h7.colorName as priorityColor,"
//                    + "         mrq.regDocId as patientRegDocID   "
//                    + "         from requisition mrq  "
//                    + "         left Join requisitionDetail as mrqd on mrqd.docId=mrq.docid   "
//                    + join
//                    + "         left join voucher_Type vt on vt.v_Type=	mrq.v_Type  "
//                    + "         left join subGroup dm on dm.subCode=	mrq.departmentToCode  "
//                    + "         left join subGroup rbc on rbc.subCode=	mrq.requestedByCode  "
//                    + "		left join itemMast im on im.code = mrqd.itemCode "
//                    + "		lEFT JOIN unitMast um ON um.code = im.sku "
//                    + "         LEFT JOIN patientRegistration pr ON mrq.regDocId=pr.regDocId "
//                    + "         LEFT JOIN patientUHID pu ON pr.uhid=pu.docId"
//                    + "         LEFT JOIN hl7Mast H7 ON mrq.priorityTypeCode=h7.code"
//                    + "         where vt.category in ('" + new ProjectVoucherCategory().PHARMACY_REQUISITION + "','" + new ProjectVoucherCategory().NURSING_MEDICINE_REQUISTION + "')"
//                    + "         and ISNULL(mrq.approvedBy,'')<>''  "
//                    + "         and mrq.site_Code_To= :site_Code "
//                    + "" + condition
//                    + "" + havingCondition
//                    + "         ORDER BY priorityIndex,reqDate,recid,departmentName,mediName  ";
            sqlQuery = "select  mrq.docid as docid, max(mrq.v_Date) as date,max(mrq.v_Time) as v_Time,\n"
                    + "         max(mrq.departmentCode) as departmentCode,max(mrq.requestedByCode) as requestedByCode,\n"
                    + "         max(mrq.approvedBy) as approvedBy , max(mrq.regDocId) as patientRegDocID ,\n"
                    + "         max(mrq.recid) recid,\n"
                    + "         max(mrqd.isStatus) as isStatus , max(mrqd.v_SNo) as v_SNo,\n"
                    + quantity + " as reqQty ,\n"
                    + "         max(mrqd.remark) as remark,max(mrqd.itemCode) as itemCode,  max(mrqd.reqDate) as reqDate,\n"
                    + "         max(im.itsBasedOn) as itsBasedOn, max(im.itemPacking) as itemPacking,\n"
                    + "         max(dm.name) as departmentName,max(dmf.name) as departmentFrom,  max(rbc.name) as requestedByName,\n"
                    + "         max(im.name) as mediName,max(um.unit) as skuName, max(pu.patientName) as patientName ,  max(um.code) as unitCode,\n"
                    + "         max(h7.shortName) as priority,    max(h7.colorName) as priorityColor,max(case when H7.priorityIndex=0 or H7.priorityIndex is null then 99 else H7.priorityIndex end) as priorityIndex,\n"
                    + "         max(pu.recid) as patientUHID,max(pr.recid) as patientRegNo,max(pr.v_Date) as dateOfAdmission,\n"
                    + "         case when sum(ISNULL(mrqd.issueQty,0))>0 then 100 else 0 end PartialPending\n"
                    + "         from requisition mrq  \n"
                    + "         left Join requisitionDetail as mrqd on mrqd.docId=mrq.docid   \n"
                    + join
                    + "         left join voucher_Type vt on vt.v_Type=	mrq.v_Type  \n"
                    + "         left join subGroup dm on dm.subCode=	mrq.departmentToCode  \n"
                    + "         left join subGroup dmf on dmf.subCode=	mrq.departmentCode  \n"
                    + "         left join subGroup rbc on rbc.subCode=	mrq.requestedByCode  \n"
                    + "		left join itemMast im on im.code = mrqd.itemCode \n"
                    + "		lEFT JOIN unitMast um ON um.code = im.sku \n"
                    + "         LEFT JOIN patientRegistration pr ON mrq.regDocId=pr.regDocId \n"
                    + "         LEFT JOIN patientDischarge pd ON pd.regDocId=pr.regDocId \n"
                    + "         LEFT JOIN patientUHID pu ON pr.uhid=pu.docId \n"
                    + "         LEFT JOIN hl7Mast H7 ON mrq.priorityTypeCode=h7.code \n"
                    + "         where vt.category in ('" + new ProjectVoucherCategory().PHARMACY_REQUISITION + "','" + new ProjectVoucherCategory().NURSING_MEDICINE_REQUISTION + "')"
                    + "         and ISNULL(mrq.approvedBy,'')<>''  and pd.regDocId is null \n"
                    + "         and mrq.site_Code_To= :site_Code "
                    + "" + condition
                    + "" + groupByCondition
                    + "" + havingCondition
                    + "         ORDER BY priorityIndex,PartialPending,date,v_Time,reqDate,recid,departmentName,mediName  ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));

            if (!DmShared.xNull(searchValue_Mdl.getItemCode()).equalsIgnoreCase("")) {
                psTemp.setString("itemCode", DmShared.iNull(searchValue_Mdl.getItemCode()).toString());
            }
            if (!DmShared.dNull(searchValue_Mdl.getV_Date()).equalsIgnoreCase("")) {
                psTemp.setDate("v_Date", DmServer.convertDate(searchValue_Mdl.getV_Date()));
            }
            if (DmShared.iNull(searchValue_Mdl.getPatientRegDocId()).longValue() != 0) {
                psTemp.setString("regDocid", DmShared.iNull(searchValue_Mdl.getPatientRegDocId()).toString());
            }
            if (DmShared.iNull(searchValue_Mdl.getReqDocId()).longValue() > 0) {
                psTemp.setString("docIdReq", DmShared.iNull(searchValue_Mdl.getReqDocId()).toString());
            }

            if (DmShared.iNull(searchValue_Mdl.getPriorityNameValue()).longValue() > 0) {
                psTemp.setString("priorityCode", DmShared.iNull(searchValue_Mdl.getPriorityNameValue()).toString());
            }

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdRequisitionDetailsSearch_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docid")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recid")));
                node.setVoucherSNo(DmServer.vNull(rsTemp.getString("v_SNo"), "0"));
                node.setMediName(DmShared.xNull(rsTemp.getString("mediName")));
                node.setMediCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setDepartmentText(DmShared.xNull(rsTemp.getString("departmentName")));
                node.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                node.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                node.setSku(DmShared.xNull(rsTemp.getString("unitCode")));
                node.setItemPacking(DmShared.xNull(rsTemp.getString("itemPacking")));
                node.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                node.setApprovedby(DmShared.xNull(rsTemp.getString("approvedBy")));
                node.setQuantity(DmServer.vNull(rsTemp.getString("reqQty")));
                node.setRequisitionDate(DmShared.dNull(rsTemp.getDate("reqDate")));
                node.setRequisitionBy(DmShared.xNull(rsTemp.getString("requestedByName")));
                node.setStatus(DmShared.xNull(rsTemp.getString("isStatus")));
                node.setPatientName(DmShared.xNull(rsTemp.getString("patientName")));
                node.setPriority(DmShared.xNull(rsTemp.getString("priority")));
                node.setPriorityColor(DmShared.xNull(rsTemp.getString("priorityColor")));
                node.setPatientRegDocID(DmShared.xNull(rsTemp.getString("patientRegDocID")));
                node.setPatientUHID(DmShared.xNull(rsTemp.getString("patientUHID")));
                node.setPatientRegID(DmShared.xNull(rsTemp.getString("patientRegNo")));
                node.setReqTime(DmServer.vNull(rsTemp.getString("v_Time"), "00.00"));
                node.setDepartmentFromName(DmShared.xNull(rsTemp.getString("departmentFrom")));
                node.setEntryDate(DmShared.dNull(rsTemp.getDate("date")));
                node.setDateOfAdmission(DmShared.dNull(rsTemp.getDate("dateOfAdmission")));
                node.setPartialPending(DmShared.xNull(rsTemp.getString("partialPending")));
                node.setIsChecked("0");
                node.setIsCancelled("0");
                lstTemp.add(node);
            }
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartmentRequisitionSearch(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "", condition = "";
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                condition = " AND dm.subCode IN (" + DmServer.getParamList(":department", STServer.getSsnDepartmentCodeList(this)) + ")";
            }
            sqlQuery = "SELECT dm.subCode AS code, sg.name "
                    + "	FROM departmentMast dm"
                    + "	LEFT JOIN subGroup sg ON sg.subCode = dm.subCode"
                    + "	LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature"
                    //                    + "	LEFT JOIN subGroupSiteList sgsl ON sg.subCode = sgsl.sunCode "
                    + "	WHERE sg.isActive = 1 and sg.subcode in(select departmentCode departmentCode from requisition) " + condition;
//                    + "	WHERE sgn.manualCode IN ('CM','PH') AND sg.isActive = 1 " + condition;
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
            rsTemp.close();
            psTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());

            rstTemp.setData(lstTemp);
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
    public DmResult<String> openRecord(boolean isOpen, String docId, String v_SNo) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData("");

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            if (isOpen) {
                sqlQuery = " Update requisitionDetail set "
                        + " isStatus=:isStatus "
                        + " where docId=:docId AND v_SNo=:vSNo";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(docId).toString());
                psTemp.setString("vSNo", DmShared.iNull(v_SNo).toString());
                psTemp.setString("isStatus", "0");
                psTemp.executeUpdate();

                psTemp.close();
            } else {
                sqlQuery = " select Count(isStatus) as cnt from requisitionDetail where docId = :docId AND v_SNo=:VSNo AND isStatus = 1";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(docId).toString());
                psTemp.setString("VSNo", DmShared.iNull(v_SNo).toString());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (DmShared.iNull(rsTemp.getString("cnt")).intValue() == 1) {
                        throw new DmException(0, "Record is already cancelled,Please refresh record.");
                    }
                }
                rsTemp.close();
                psTemp.close();

                sqlQuery = " select Count(isStatus) as cnt from requisitionDetail where docId = :docId AND v_SNo=:VSNo AND isStatus = 2";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(docId).toString());
                psTemp.setString("VSNo", DmShared.iNull(v_SNo).toString());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (DmShared.iNull(rsTemp.getString("cnt")).intValue() == 1) {
                        throw new DmException(0, "Record is completed,Please refresh record.");
                    }
                }
                rsTemp.close();
                psTemp.close();

                sqlQuery = " UPDATE requisitionDetail SET "
                        + " isStatus = 1 "
                        + " WHERE docId = :docId AND v_SNo =:vSNo";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(docId).toString());
                psTemp.setString("vSNo", DmShared.iNull(v_SNo).toString());
                psTemp.executeUpdate();
                psTemp.close();
            }
            sqlQuery = " Update requisition set "
                    + " u_EntDt_LatestLine=:uEntDt "
                    + " where docId=:docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            psTemp.executeUpdate();
            psTemp.close();

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
    public DmResultPageing<List<aarogya.pharmacy.requisitionSearch.shared.TxtHPriority_Mdl>> getTxtHPriority(DmHelper helper, boolean blnToEndOfFile) {
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

}
