package dataman.payroll.leaveEntryApproval.server;

import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.payroll.leaveEntryApproval.shared.DefaultValue_Mdl;
import dataman.payroll.leaveEntryApproval.shared.Filter_Mdl;
import dataman.payroll.leaveEntryApproval.shared.GrdLeaveEntryApproval_Mdl;
import dataman.payroll.leaveEntryApproval.shared.LeaveEntryApproval_Mdl;
import dataman.payroll.leaveEntryApproval.shared.LeaveEntryApproval_Srv;
import dataman.payroll.leaveEntryApproval.shared.Search_Mdl;
import dataman.payroll.self.shared.PREnum;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LeaveEntryApproval_SrvImpl extends DmRSSPrivate implements LeaveEntryApproval_Srv {

    @Override
    public DmResultPageing<List<GrdLeaveEntryApproval_Mdl>> getFillRecords(Search_Mdl value_Mdl, Filter_Mdl param, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<GrdLeaveEntryApproval_Mdl>> rstTemp = new DmResultPageing<>();
        List<GrdLeaveEntryApproval_Mdl> lstTemp = new ArrayList<>();
        GrdLeaveEntryApproval_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery, condition = "", join = "";
        String leaveStatus = "";

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (!DmShared.dNull(param.getFromDate()).trim().equalsIgnoreCase("")) {
                condition += " AND  pyl.applyDate >= :fromDate ";
            }
            if (!DmShared.dNull(param.getToDate()).trim().equalsIgnoreCase("")) {
                condition += " AND  pyl.applyDate <= :toDate ";
            }
            if (!DmShared.xNull(param.getStatusCode()).equalsIgnoreCase("")) {
                condition += " AND  pyl.isStatus = :statusCode ";
            }

//            condition += " AND po.site_Code='" + DmServer.getSsnSiteCode(this) + "' ";
            sqlQuery = "select pyl.docId,isNull(pyl.v_No,'') EntryNo,pyl.employeeCode,sg2.hrDepartmentCode,sg.name as EmployeeName,pyl.applyDate,pyl.fromDate,\n"
                    + "pyl.toDate,lm.name as LeaveNature,pyl.duration,isNull(pyl.reason,'') reason,isNull(sgdep.name,'') as Department,\n"
                    + "isNull(pyl.approvedBy,'') approvedBy,pyl.isStatus "
                    + "from pyLeaveEntry pyl\n"
                    + "left join subGroup sg on sg.subCode = pyl.employeeCode\n"
                    + "left join subGroup2 sg2 on sg.subCode = sg2.subCode\n"
                    + "left join subGroup sgdep on sgdep.subCode = sg2.hrDepartmentCode\n"
                    + "left join attendenceCodeDefinitionMast lm on lm.code = pyl.leaveCode where pyl.docId <> '' " + condition;

            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            if (!DmShared.dNull(param.getFromDate()).equalsIgnoreCase("")) {
                psTemp.setDate("fromDate", DmServer.convertDate(param.getFromDate()));
            }
            if (!DmShared.dNull(param.getToDate()).equalsIgnoreCase("")) {
                psTemp.setDate("toDate", DmServer.convertDate(param.getToDate()));
            }
            if (!DmShared.xNull(param.getStatusCode()).equalsIgnoreCase("")) {
                psTemp.setString("statusCode", DmShared.xNull(param.getStatusCode()));
            }

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new GrdLeaveEntryApproval_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("EntryNo")));
                listNode.setEntryNo(DmShared.xNull(rsTemp.getString("EntryNo")));
                listNode.setEmployeeName(DmShared.xNull(rsTemp.getString("EmployeeName")));
                listNode.setEmployeeCode(DmShared.xNull(rsTemp.getString("employeeCode")));
                listNode.setApplyDate(DmShared.dNull(rsTemp.getDate("applyDate")));
                listNode.setFromDate(DmShared.dNull(rsTemp.getDate("fromDate")));
                listNode.setToDate(DmShared.dNull(rsTemp.getDate("toDate")));
                listNode.setLeaveName(DmShared.xNull(rsTemp.getString("LeaveNature")));
                listNode.setDuration(DmShared.xNull(rsTemp.getString("duration")));
                listNode.setReason(DmShared.xNull(rsTemp.getString("reason")));
                listNode.setDepartment(DmShared.xNull(rsTemp.getString("Department")));
                listNode.setDepartmentCode(DmShared.xNull(rsTemp.getString("hrDepartmentCode")));
                listNode.setApprovedBy(DmShared.xNull(rsTemp.getString("approvedBy")));
                listNode.setStatusCode(DmShared.xNull(rsTemp.getString("isStatus")));
                leaveStatus = PREnum.LEAVE_STATUS.getViaKey(DmShared.iNull(rsTemp.getString("isStatus")).intValue()).getValue();
                listNode.setStatus(DmShared.xNull(leaveStatus));
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
        DefaultValue_Mdl defaultValue = new DefaultValue_Mdl();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        Date date;
        int month;
        Calendar calendar = Calendar.getInstance();

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            
            defaultValue.setToDate(String.valueOf(java.time.LocalDate.now()));

            date = DmDateTimeFormat.getFormat("yyyy-MM-dd").parse(DmShared.dNull(defaultValue.getToDate()));
            calendar.setTime(date);

            calendar.setTime(new Date());
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
            calendar.set(Calendar.DAY_OF_MONTH,
            calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            Timestamp start = new Timestamp(calendar.getTimeInMillis());
            defaultValue.setFromDate(DmShared.dNull(new Date(start.getTime())));
             
            defaultValue.setStatus(PREnum.LEAVE_STATUS.PENDING.getValue());
            defaultValue.setStatusCode(String.valueOf(PREnum.LEAVE_STATUS.PENDING.getId()));
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
    public DmResult<String> saveRecord(GrdLeaveEntryApproval_Mdl mrTemp) {

        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery, filterNature = "", acGroupConsCode = "", acLocationCode = "", acDepartmentCode = "";
        ResultSet rsTemp = null;
        int vSno = 0;
        String v_Type = null;
        String condition = "";
        String[] currentDateTime;
        String totalDed = "";
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "update pyLeaveEntry set isStatus = :isStatus where docId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

            psTemp.setString("isStatus", DmShared.xNull(mrTemp.getStatusCode()));
            psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            psTemp.executeUpdate();
            psTemp.close();
            
            sqlQuery = "update Employee_Attendance_Leave_Detail set isStatus = :isStatus where docId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

            psTemp.setString("isStatus", DmShared.xNull(mrTemp.getStatusCode()));
            psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            psTemp.executeUpdate();
            psTemp.close();

            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHStatus(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            for (PREnum.LEAVE_STATUS node : PREnum.LEAVE_STATUS.values()) {

                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(String.valueOf(node.getId()));
                listNode.setSharedKeyFieldName(node.getValue());
                lstTemp.add(listNode);
            }

            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

//    @Override
//    public DmResultPageing<List<TxtHPurchaseOrder_Mdl>> getTxtHPurchaseOrder(DmHelper helper, boolean blnToEndOfFile) {
//        DmResultPageing<List<TxtHPurchaseOrder_Mdl>> rstTemp = new DmResultPageing<>();
//        List<TxtHPurchaseOrder_Mdl> lstTemp = new ArrayList<>();
//        TxtHPurchaseOrder_Mdl listNode;
//        Connection cnTemp = null;
//        ResultSet rsTemp = null;
//        DmPreparedStatement psTemp = null;
//        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
//        String sqlQuery = "";
//        rstTemp.setData(lstTemp);
//        try {
//
//            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
//            sqlQuery = " SELECT  "
//                    + " po.docId, po.recId as pOrderNo, po.v_Date as pOrderDate, vt.category "
//                    + " FROM pOrder AS po  "
//                    + " left join voucher_Type as vt on vt.v_Type = po.v_Type "
//                    + " where po.site_Code= '" + DmServer.getSsnSiteCode(this) + "' "
//                    + " and  vt.Category = '" + pvc.PURCHASE_ORDER + "'";
//            sqlWithParam.setSQL(sqlQuery);
//            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
//
//            rsTemp = psTemp.executeQuery();
//
//            while (rsTemp.next()) {
//                listNode = new TxtHPurchaseOrder_Mdl();
//                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
//                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("pOrderNo")));
//                listNode.setPoDate(DmShared.dNull(rsTemp.getDate("pOrderDate")));
//                listNode.setPoType(DmShared.xNull(rsTemp.getString("category")));
//                lstTemp.add(listNode);
//            }
//            rsTemp.close();
//            psTemp.close();
//            helper.positioning(lstTemp.size(), blnToEndOfFile);
//            rstTemp.setPaging(helper.getPaging());
//            rstTemp.setStatus(true);
//            rstTemp.setData(lstTemp);
//            cnTemp.close();
//        } catch (Exception e) {
//            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
//        } finally {
//            try {
//                if (rsTemp != null) {
//                    rsTemp.close();
//                }
//
//                if (psTemp != null) {
//                    psTemp.close();
//                }
//
//                if (cnTemp != null && !cnTemp.isClosed()) {
//                    cnTemp.close();
//                }
//            } catch (Exception e) {
//            }
//        }
//        return rstTemp;
//    }
}
