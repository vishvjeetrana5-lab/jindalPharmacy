package dataman.payroll.leaveEntry.server;

import dataman.payroll.leaveEntry.shared.*;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.payroll.self.shared.PREnum;
import dataman.payroll.self.shared.PREnum.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

public class LeaveEntry_SrvImpl extends DmRSSPrivate implements LeaveEntry_Srv {

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator() {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            rsTemp = cnTemp.createStatement().executeQuery("select max(py.docId) docId from pyLeaveEntry py group by py.docId");

            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
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

    private String getDate(String monthSelected) {

        String[] monthSelectedList;
        String monthName, dateReturn;
        monthSelected = monthSelected.replace("'", "");
        monthSelectedList = monthSelected.split("-");
        monthName = monthSelectedList[0];
        dateReturn = "01-" + monthSelected;
        return dateReturn;
    }

    @Override
    public DmResult<String> getAvailableLeave(LeaveEntry_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<EmployeeLeave_Mdl> employeeLeaveList = new ArrayList<EmployeeLeave_Mdl>();
        List<EmployeeLeave_Mdl> takenLeaveList = new ArrayList<EmployeeLeave_Mdl>();
        List<String> leaveLst = new ArrayList<>();
        List<String> leaveLst2 = new ArrayList<>();
        int openingLeave = 0;
        int takenLeave = 0;
        String available = "";

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(mrTemp.getEmployeeCode()).equalsIgnoreCase("") && !DmShared.xNull(mrTemp.getLeaveName()).equalsIgnoreCase("")) {
                employeeLeaveList = getOpeningLeaveList(DmShared.xNull(mrTemp.getEmployeeCode()), cnTemp);
                takenLeaveList = getTakenLeaveList(DmShared.xNull(mrTemp.getEmployeeCode()), cnTemp);

                leaveLst = getLeaveList(employeeLeaveList);
                leaveLst2 = getLeaveList2(takenLeaveList);

                openingLeave = getOpeningLeave(leaveLst, mrTemp.getLeaveCode());
                takenLeave = getTakenLeave(leaveLst2, mrTemp.getLeaveCode());

                available = String.valueOf(openingLeave - takenLeave);

            }

            dmrTemp.setStatus(true);
            dmrTemp.setData(available);
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
    public DmResult<List<GrdLeaveStatus_Mdl>> fillLeaveStatusGrd(LeaveEntry_Mdl mrTemp) {
        DmResult<List<GrdLeaveStatus_Mdl>> dmrTemp = new DmResult();
        List<GrdLeaveStatus_Mdl> grdLeaveStatusLst = new ArrayList<>();
        GrdLeaveStatus_Mdl grdLeaveStatus;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<EmployeeLeave_Mdl> employeeLeaveList = new ArrayList<EmployeeLeave_Mdl>();
        List<EmployeeLeave_Mdl> takenLeaveList = new ArrayList<EmployeeLeave_Mdl>();
        List<String> openingLeaveLst = new ArrayList<>();
        List<String> takenLeaveLst = new ArrayList<>();
        int openingLeave = 0;
        int takenLeave = 0;
        String available = "";
        String sqlQuery = "";
        List<String> strList = new ArrayList<>();
        dmrTemp.setData(grdLeaveStatusLst);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            rsTemp = cnTemp.createStatement().executeQuery("select name,manualCode from attendenceCodeDefinitionMast where type_Leave_Attendance=" + TYPE_LEAVE_ATTENDANCE.getViaText("LEAVE").getId());

            while (rsTemp.next()) {
                strList.add(rsTemp.getString("manualCode"));
            }
            rsTemp.close();

            if (!DmShared.xNull(mrTemp.getEmployeeCode()).equalsIgnoreCase("")) {
                employeeLeaveList = getOpeningLeaveList(DmShared.xNull(mrTemp.getEmployeeCode()), cnTemp);
                takenLeaveList = getTakenLeaveList(DmShared.xNull(mrTemp.getEmployeeCode()), cnTemp);

                openingLeaveLst = getLeaveList(employeeLeaveList);
                takenLeaveLst = getLeaveList2(takenLeaveList);

                for (int i = 0; i < strList.size(); i++) {
                    openingLeave = getOpeningLeave(openingLeaveLst, DmShared.xNull(strList.get(i)));
                    takenLeave = getTakenLeave(takenLeaveLst, DmShared.xNull(strList.get(i)));

                    available = String.valueOf(openingLeave - takenLeave);

                    grdLeaveStatus = new GrdLeaveStatus_Mdl();
                    grdLeaveStatus.setLeave(DmShared.xNull(strList.get(i)));
                    grdLeaveStatus.setOpening(DmShared.xNull(String.valueOf(openingLeave)));
                    grdLeaveStatus.setTaken(DmShared.xNull(String.valueOf(takenLeave)));
                    grdLeaveStatus.setBalance(DmShared.xNull(String.valueOf(available)));

                    grdLeaveStatusLst.add(grdLeaveStatus);

                }

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

    private int getOpeningLeave(List<String> leaveList, String leaveCode) {
        int openingLeave = 0;

        if (DmShared.iNull(leaveList.size()).longValue() > 0) {
            for (String leaveString : leaveList) {
                if (DmShared.xNull(leaveString.toLowerCase()).contains(DmShared.xNull(leaveCode.toLowerCase()))) {
                    openingLeave = openingLeave + Integer.valueOf(leaveString.split(",")[1]);
                }
            }
        }
        return openingLeave;
    }

    private int getTakenLeave(List<String> leaveList, String leaveCode) {
        int takenLeave = 0;

        if (DmShared.iNull(leaveList.size()).longValue() > 0) {
            for (String leaveString : leaveList) {
                if (DmShared.xNull(leaveString.toLowerCase()).contains(DmShared.xNull(leaveCode.toLowerCase()))) {
                    takenLeave = takenLeave + Integer.valueOf(leaveString.split(",")[1]);
                }
            }
        }
        return takenLeave;
    }

    private int getTotalLeave(Connection cnTemp, String code) throws SQLException {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        int totalLeave = 0;
        String sqlQuery;
        //Calculating total leaves of employee 
        sqlQuery = "select  sum(eld.leaves) Leaves "
                + "from EmployeeLeave_Detail eld "
                + "where eld.EmployeeCode = :code";
        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

        psTemp.setString("code", DmShared.iNull(code).toString());
        rsTemp = psTemp.executeQuery();
        while (rsTemp.next()) {
            totalLeave = rsTemp.getInt("Leaves");
        }
        psTemp.close();
        rsTemp.close();
        return totalLeave;
    }

    private int getHolidays(Connection cnTemp) throws SQLException {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        int holidays = 0;
        String sqlQuery;
        //Calculating total leaves of employee 
        sqlQuery = "select count(date) as holidays from holidayMast";
        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

        rsTemp = psTemp.executeQuery();
        while (rsTemp.next()) {
            holidays = rsTemp.getInt("holidays");
        }
        psTemp.close();
        rsTemp.close();
        return holidays;
    }

    @Override
    public DmResultPageing<List<Employee_Mdl>> getTxtHEmployee(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<Employee_Mdl>> rstTemp = new DmResultPageing<>();
        List<Employee_Mdl> data = new ArrayList<>();
        Employee_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(data);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("select sg.subCode as code,sg.name as Employee,isNull(sg2.fathername,'') fatherName,isNull(sg2.doj,'') doj,\n"
                    + "isNull(sgd.name,'') as department,isNull(sg2.hrDepartmentCode,'') DepartmentCode \n"
                    + ",sm.fromTime,sm.toTime \n"
                    + "from subGroup  sg\n"
                    + "left join subGroup2 sg2 on sg2.subCode = sg.subCode\n"
                    + "left join subGroupNature sgn on sgn.code = sg.filterNature \n"
                    + "left join subGroup sgd on sgd.subCode = sg2.hrDepartmentCode\n"
                    + " left join pyEmployeeShiftDetail pesd on sg.subCode = pesd.employeeCode and '"+getServerDateTime()[0]+"' between pesd.fromDate and pesd.toDate\n"
                    + " left join shiftMast sm on isNull(pesd.shiftCode,sg.shiftCode) = sm.code\n"
                    + "where sgn.manualCode = 'EM' and sg.isActive = 1");

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Employee_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Employee")));
                listNode.setEmployee(DmShared.xNull(rsTemp.getString("Employee")));
                listNode.setDepartment(DmShared.xNull(rsTemp.getString("department")));
                listNode.setFatherName(DmShared.xNull(rsTemp.getString("fatherName")));
                listNode.setId("");
                listNode.setDOJ(DmShared.xNull(rsTemp.getString("doj")));
                listNode.setDepartmentCode(DmShared.xNull(rsTemp.getString("DepartmentCode")));
                listNode.setFromTime(DmShared.vNull(rsTemp.getDouble("fromTime")).toString());
                listNode.setToTime(DmShared.vNull(rsTemp.getDouble("toTime")).toString());
                data.add(listNode);
            }
            helper.positioning(data.size(), blnToEndOfFile);
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

    public DmResult<String> deleteRecord(String strKey) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "delete from Employee_Attendance_Leave_Detail where docId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", strKey);
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "delete from pyLeaveEntry where docId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", strKey);
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
    public DmResultPageing<List<LeaveTextHelp_Mdl>> getTxtHLeave(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<LeaveTextHelp_Mdl>> rstTemp = new DmResultPageing<>();
        List<LeaveTextHelp_Mdl> data = new ArrayList<>();
        LeaveTextHelp_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(data);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("select lm.code,lm.name,lm.manualCode,lm.days,lm.plusMinus from attendenceCodeDefinitionMast lm where type_Leave_Attendance=:type_Leave_Attendance");
            sqlWithParam.setString("type_Leave_Attendance", DmServer.vNull(TYPE_LEAVE_ATTENDANCE.getViaText("LEAVE").getId()));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new LeaveTextHelp_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setLeave(DmShared.xNull(rsTemp.getString("name")));
                listNode.setShortName(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setDays(DmShared.xNull(rsTemp.getString("days")));
                listNode.setPlusMinus(DmShared.xNull(rsTemp.getString("plusMinus")));
                data.add(listNode);
            }
            helper.positioning(data.size(), blnToEndOfFile);
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

    private List<String> getLeaveList(List<EmployeeLeave_Mdl> employeeLeaveList) {
        LinkedHashMap<String, String> leaveList = new LinkedHashMap();
        List<String> rtnLst = new ArrayList<>();
        String rtnLstNode = "";

        if (DmShared.iNull(employeeLeaveList.size()).longValue() > 0) {
            for (EmployeeLeave_Mdl employeeLeave : employeeLeaveList) {
                rtnLstNode = employeeLeave.getLeaveName() + "," + employeeLeave.getTotalLeave();
                rtnLst.add(rtnLstNode);
            }
        }
        return rtnLst;
    }

    private List<String> getLeaveList2(List<EmployeeLeave_Mdl> employeeTakenLeaveList) {
        LinkedHashMap<String, String> leaveList = new LinkedHashMap();
        List<String> rtnLst = new ArrayList<>();
        String rtnLstNode = "";

        if (DmShared.iNull(employeeTakenLeaveList.size()).longValue() > 0) {
            for (EmployeeLeave_Mdl employeeLeave : employeeTakenLeaveList) {
                rtnLstNode = employeeLeave.getLeaveName() + "," + employeeLeave.getTotalLeave();
                rtnLst.add(rtnLstNode);
            }
        }
        return rtnLst;
    }

    private List<EmployeeLeave_Mdl> getOpeningLeaveList(String employeeCode, Connection cnTemp) {
        String sqlQuery = "";
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<EmployeeLeave_Mdl> openingLeaveList = new ArrayList<EmployeeLeave_Mdl>();
        EmployeeLeave_Mdl employeeLeave;

        try {
            sqlQuery = "select epld.EmployeeCode,lm.manualCode as leaveName,lmd.fromDate,lmd.toDate \n"
                    + ",case when lm.typeCode=" + LEAVE_TYPE.getViaText(DmShared.xNull("Brought forward")).getId() + " then epld.leaves else (case when getDate() between lmd.fromDate and lmd.toDate then epld.leaves else 0 end) end as leaves \n"
                    + "from EmployeeLeave_Detail epld \n"
                    + "left join attendenceCodeDefinitionMast lm on lm.code = epld.LeaveCode\n"
                    + "left join attendenceCodeDefinitionMastDetail lmd on lmd.code = lm.code  and lmd.docid=epld.leaveDocID\n"
                    + "where EmployeeCode = :employeeCode";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (!DmShared.xNull(employeeCode).trim().equalsIgnoreCase("")) {
                psTemp.setString("employeeCode", DmShared.iNull(employeeCode).toString());
            }

            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                employeeLeave = new EmployeeLeave_Mdl();
                employeeLeave.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("EmployeeCode")));
                employeeLeave.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("EmployeeCode")));

                employeeLeave.setLeaveName(DmShared.xNull(rsTemp.getString("leaveName")));
                employeeLeave.setTotalLeave(DmShared.xNull(rsTemp.getString("leaves")));
                employeeLeave.setFromDate(DmShared.xNull(rsTemp.getString("fromDate")));
                employeeLeave.setToDate(DmShared.xNull(rsTemp.getString("toDate")));

                openingLeaveList.add(employeeLeave);
            }
            rsTemp.close();
            psTemp.close();

        } catch (Exception e) {
            try {

            } catch (Exception ex) {
            }
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

        return openingLeaveList;
    }

    private List<EmployeeLeave_Mdl> getTakenLeaveList(String employeeCode, Connection cnTemp) {

        String sqlQuery = "", condition = "";
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<EmployeeLeave_Mdl> takenLeaveList = new ArrayList<EmployeeLeave_Mdl>();
        EmployeeLeave_Mdl employeeLeave;

        try {
            sqlQuery = "select pyl.employeeCode,sg.name as EmployeeName,pyl.fromDate,pyl.toDate,"
                    + "pyl.duration leaves,lm.manualCode as leaveName "
                    + "from pyLeaveEntry pyl\n"
                    + "left join attendenceCodeDefinitionMast lm on lm.code = pyl.leaveCode\n"
                    + "left join subGroup sg on sg.subCode = pyl.employeeCode\n"
                    + "where pyl.employeeCode = :employeeCode";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (!DmShared.xNull(employeeCode).trim().equalsIgnoreCase("")) {
                psTemp.setString("employeeCode", DmShared.iNull(employeeCode).toString());
            }

            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                employeeLeave = new EmployeeLeave_Mdl();
                employeeLeave.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("EmployeeCode")));
                employeeLeave.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("EmployeeCode")));

                employeeLeave.setLeaveName(DmShared.xNull(rsTemp.getString("leaveName")));
                employeeLeave.setTotalLeave(DmShared.xNull(rsTemp.getString("leaves")));
                employeeLeave.setFromDate(DmShared.xNull(rsTemp.getString("fromDate")));
                employeeLeave.setToDate(DmShared.xNull(rsTemp.getString("toDate")));

                takenLeaveList.add(employeeLeave);
            }
            rsTemp.close();
            psTemp.close();

        } catch (Exception e) {
            try {

            } catch (Exception ex) {
            }
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

        return takenLeaveList;
    }

    @Override
    public DmResult<LeaveEntry_Mdl> getMoveRec(String strKey) {
        DmResult<LeaveEntry_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        LeaveEntry_Mdl mrTemp = new LeaveEntry_Mdl();
        List<EmployeeLeave_Mdl> employeeLeaveList = new ArrayList<EmployeeLeave_Mdl>();
        List<EmployeeLeave_Mdl> takenLeaveList = new ArrayList<EmployeeLeave_Mdl>();
        List<String> leaveLst = new ArrayList<>();
        List<String> leaveLst2 = new ArrayList<>();
        int openingLeave = 0;
        int takenLeave = 0;
        String status = "";
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT   pyl.docId,pyl.recId,pyl.isStatus, pyl.vSno,pyl.v_No,sg.name as EmployeeName,sgDep.name as Department  \n"
                    + ",sg2.hrDepartmentCode as departmentCode,pyl.employeeCode,pyl.reason, pyl.applyDate, pyl.fromDate, pyl.toDate\n"
                    + ", pyl.duration, pyl.leaveCode,lm.name as leaveName,pyl.fromTime,pyl.totime,pyl.days,pyl.plusMinus\n"
                    + "FROM   pyLeaveEntry pyl\n"
                    + "left join subGroup sg on sg.subCode = pyl.employeeCode\n"
                    + "left join subGroup2 sg2 on sg2.subCode = sg.subCode\n"
                    + "left join subGroup sgDep on sg2.hrDepartmentCode = sgDep.subCode\n"
                    + "left join attendenceCodeDefinitionMast lm on lm.code = pyl.leaveCode\n"
                    + "where pyl.docId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", strKey);
            rsTemp = psTemp.executeQuery();

            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("EmployeeName")));
                mrTemp.setEmployeeCode(DmShared.xNull(rsTemp.getString("employeeCode")));
                mrTemp.setEmployeeName(DmShared.xNull(rsTemp.getString("EmployeeName")));
                mrTemp.setReason(DmShared.xNull(rsTemp.getString("reason")));
                mrTemp.setApplyDate(DmShared.dNull(rsTemp.getDate("applyDate")));
                mrTemp.setFromDate(DmShared.dNull(rsTemp.getDate("fromDate")));
                mrTemp.setFromTime(DmServer.vNull(rsTemp.getDouble("fromTime"), "0.00"));
                mrTemp.setToDate(DmShared.dNull(rsTemp.getDate("toDate")));
                mrTemp.setToTime(DmServer.vNull(rsTemp.getDouble("toTime"), "0.00"));
                mrTemp.setDuration(DmShared.xNull(rsTemp.getString("duration")));
                mrTemp.setDays(DmShared.xNull(rsTemp.getString("days")));
                mrTemp.setPlusMinus(DmShared.xNull(rsTemp.getString("plusMinus")));
                mrTemp.setLeaveCode(DmShared.xNull(rsTemp.getString("leaveCode")));
                mrTemp.setLeaveName(DmShared.xNull(rsTemp.getString("leaveName")));
                mrTemp.setDepartmentName(DmShared.xNull(rsTemp.getString("Department")));
                mrTemp.setDepartmentCode(DmShared.xNull(rsTemp.getString("departmentCode")));
                mrTemp.getRecId().setRecIdValue(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.getRecId().setCounter(DmShared.iNull(rsTemp.getString("v_No")).longValue());
                mrTemp.setStatusCode(DmShared.xNull(rsTemp.getString("isStatus")));
                status = PREnum.LEAVE_STATUS.getViaKey(Integer.valueOf(DmShared.xNull(mrTemp.getStatusCode()))).getValue();
                mrTemp.setStatus(DmShared.xNull((status)));
            }
            employeeLeaveList = getOpeningLeaveList(DmShared.xNull(mrTemp.getEmployeeCode()), cnTemp);
            takenLeaveList = getTakenLeaveList(DmShared.xNull(mrTemp.getEmployeeCode()), cnTemp);

            leaveLst = getLeaveList(employeeLeaveList);
            leaveLst2 = getLeaveList2(takenLeaveList);

            openingLeave = getOpeningLeave(leaveLst, mrTemp.getLeaveName());
            takenLeave = getTakenLeave(leaveLst2, mrTemp.getLeaveName());

            mrTemp.setBalanceLeave(String.valueOf(openingLeave - takenLeave));
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
    public String[] getServerDateTime() {
        String[] dateTime, returnValue;

        dateTime = DmServer.getServerDateTime();
        returnValue = new String[3];
        returnValue[0] = dateTime[0];
        returnValue[1] = dateTime[1];
        returnValue[2] = UUID.randomUUID().toString();

        return returnValue;
    }

    @Override

    public DmResult<String> getDuration(String fromDate, String toDate) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        String duration = "";

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            if (!DmShared.xNull(fromDate).equalsIgnoreCase("") && !DmShared.xNull(toDate).equalsIgnoreCase("")) {

                sqlQuery = "select DATEDIFF(d, :fromDate, :toDate)+1 duration";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("fromDate", fromDate);
                psTemp.setString("toDate", toDate);
                rsTemp = psTemp.executeQuery();
                while (rsTemp.next()) {
                    duration = rsTemp.getString("duration");
                }

                psTemp.close();

                spBegin.commit();
                cnTemp.close();
            }

            dmrTemp.setStatus(true);
            dmrTemp.setData(duration);

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

    public DmResult<String> getCurrentEntryNo() {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        String currentEntryNo = "";

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "select max(v_No)+1 v_No from pyLeaveEntry";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                currentEntryNo = rsTemp.getString("v_No");
            }

            psTemp.close();

            spBegin.commit();
            dmrTemp.setStatus(true);
            dmrTemp.setData(currentEntryNo);
            cnTemp.close();

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

    public DmResult<List<GrdLeaveHistory_Mdl>> getLeaveHistory(LeaveEntry_Mdl mrTemp) {
        DmResult<List<GrdLeaveHistory_Mdl>> dmrTemp = new DmResult();
        List<GrdLeaveHistory_Mdl> leaveHistoryList = new ArrayList<>();
        GrdLeaveHistory_Mdl leaveHistoryNode;
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData(leaveHistoryList);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (!DmShared.xNull(mrTemp.getEmployeeCode()).equalsIgnoreCase("")) {

                sqlQuery = "select pyl.v_No,pyl.leaveCode,lm.name as LeaveName,pyl.fromDate,pyl.toDate,pyl.duration\n"
                        + "from pyLeaveEntry pyl \n"
                        + "left join attendenceCodeDefinitionMast lm on lm.code = pyl.leaveCode\n"
                        + "where pyl.employeeCode = :employeeCode";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("employeeCode", mrTemp.getEmployeeCode());
                rsTemp = psTemp.executeQuery();
                while (rsTemp.next()) {
                    leaveHistoryNode = new GrdLeaveHistory_Mdl();
                    leaveHistoryNode.setLeaveName(DmShared.xNull(rsTemp.getString("LeaveName")));
                    leaveHistoryNode.setFromDate(DmShared.dNull(rsTemp.getDate("fromDate")));
                    leaveHistoryNode.setToDate(DmShared.dNull(rsTemp.getDate("toDate")));
                    leaveHistoryNode.setDuration(DmShared.xNull(rsTemp.getString("duration")));
                    leaveHistoryNode.setEntryNo(DmShared.xNull(rsTemp.getString("v_No")));
                    leaveHistoryList.add(leaveHistoryNode);
                }

                psTemp.close();
            }
            dmrTemp.setStatus(true);
            cnTemp.close();

        } catch (Exception e) {
            try {
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
    public DmResult<String> saveRecord(LeaveEntry_Mdl mrTemp, String voucherPrefix) {

        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery;
        ResultSet rsTemp = null;
        int vSno = 0;
        String v_Type = null;
        String[] currentDateTime;
        try {

            LeaveEntry_Vld.chkSave(mrTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            v_Type = DmServer.getVType("LVENT", cnTemp);
            currentDateTime = getServerDateTime();
            if (DmShared.xNull(mrTemp.getDtVoucher()).trim().equalsIgnoreCase("")) {
                mrTemp.setDtVoucher(currentDateTime[0]);
            }

            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(v_Type), voucherPrefix, cnTemp, this));
                mrTemp.setRecId(DmServer.getRecId("pyLeaveEntry", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecId(), mrTemp.getDtVoucher(), DmShared.xNull(v_Type), voucherPrefix, cnTemp, this, true));

            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            sqlQuery = "select count(docid) as totRec \n"
                    + "	from Employee_Attendance_Leave_Detail eald \n"
                    + "	where eald.employeeCode=:employeeCode and fromDate between :fromDate and :toDate";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("employeeCode", DmShared.xNull(mrTemp.getEmployeeCode()));
            psTemp.setString("fromDate", DmShared.xNull(mrTemp.getFromDate()));
            psTemp.setString("toDate", DmShared.xNull(mrTemp.getToDate()));
            rsTemp=psTemp.executeQuery();
            if (rsTemp.next()){
                if (DmShared.vNull(rsTemp.getInt("totRec"))>0){
                    throw new UnsupportedOperationException("Entry exists for duration.");
                }
            }
            
            // if blnAdd is true, this means new record should be inserted into database.
            if (blnAdd) {
                sqlQuery = "insert into pyLeaveEntry(docId, vSno,recId,recIdPrefix,v_Type,v_Prefix,v_No,site_Code, employeeCode, applyDate\n"
                        + ", fromDate, toDate, duration, leaveCode,Uname,UEntDt,reason,isStatus,fromTime,toTime,days,plusMinus)"
                        + "values(:docId, :vSno,:recId,:recIdPrefix,:v_Type,:v_Prefix,:v_No,:site_Code, :employeeCode, :applyDate\n "
                        + ", :fromDate, :toDate, :duration, :leaveCode,:Uname,:UEntDt,:reason,:isStatus,:fromTime,:toTime,:days,:plusMinus)";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setInt("vSno", ++vSno);
                psTemp.setString("recId", DmShared.xNull(mrTemp.getRecId().getRecIdValue()));
                psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getRecId().getPrefix()));
                psTemp.setString("v_Type", DmShared.foriegnKey(v_Type));
                psTemp.setString("v_Prefix", voucherPrefix);
                psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
                psTemp.setString("employeeCode", DmShared.xNull(mrTemp.getEmployeeCode()));
                psTemp.setString("applyDate", DmShared.xNull(mrTemp.getApplyDate()));
                psTemp.setString("fromDate", DmShared.xNull(mrTemp.getFromDate()));
                psTemp.setString("fromTime", DmServer.vNull(mrTemp.getFromTime()));
                psTemp.setString("toDate", DmShared.xNull(mrTemp.getToDate()));
                psTemp.setString("toTime", DmServer.vNull(mrTemp.getToTime()));
                psTemp.setString("duration", DmShared.xNull(mrTemp.getDuration()));
                psTemp.setString("leaveCode", DmShared.xNull(mrTemp.getLeaveCode()));
                psTemp.setString("days", DmServer.vNull(mrTemp.getDays()));
                psTemp.setString("plusMinus", DmServer.vNull(mrTemp.getPlusMinus()));
                psTemp.setString("reason", DmShared.xNull(mrTemp.getReason()));

                psTemp.setString("isStatus", DmShared.xNull(String.valueOf(PREnum.LEAVE_STATUS.PENDING.getId())));

                psTemp.setString("v_No", DmShared.iNull(mrTemp.getRecId().getCounter()).toString());
                psTemp.setString("Uname", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("UEntDt", DmServer.getServerTimestamp());

                psTemp.executeUpdate();
                psTemp.close();
            }

            sqlQuery = "delete from Employee_Attendance_Leave_Detail where docId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();

            for (int i = 0; i < DmShared.vNull(mrTemp.getDuration()).intValue(); i++) {
                sqlQuery = "insert into Employee_Attendance_Leave_Detail(docId,v_Date,employeeCode,att_Lev_OT,leaveCode,fromDate\n"
                        + " ,fromTime,toDate,toTime,totMinuts,days,from_Eff_DateTime,to_Eff_DateTime,site_Code,isStatus)  "
                        + " values(:docId,:v_Date,:employeeCode,:att_Lev_OT,:leaveCode,dateAdd(day," + i + ",:fromDate)\n"
                        + " ,:fromTime,dateAdd(day," + i + ",:fromDate),:toTime,:totMinuts,:days\n"
                        + " ,dateAdd(day," + i + ",:from_Eff_DateTime),dateAdd(day," + i + ",:from_Eff_DateTime),:site_Code,:isStatus)";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("v_Date", DmShared.xNull(mrTemp.getApplyDate()));
                psTemp.setString("employeeCode", DmShared.xNull(mrTemp.getEmployeeCode()));
                psTemp.setString("att_Lev_OT", DmServer.vNull(TYPE_LEAVE_ATTENDANCE.getViaText("LEAVE").getId()));
                psTemp.setString("leaveCode", DmShared.xNull(mrTemp.getLeaveCode()));
                psTemp.setString("fromDate", DmShared.xNull(mrTemp.getFromDate()));
                psTemp.setString("fromTime", DmServer.vNull(mrTemp.getFromTime()));
                psTemp.setString("toTime", DmServer.vNull(mrTemp.getToTime()));
                psTemp.setString("totMinuts", DmServer.vNull(mrTemp.getDuration()));
                psTemp.setString("isStatus", DmShared.xNull(String.valueOf(PREnum.LEAVE_STATUS.PENDING.getId())));

                if (DmShared.iNull(mrTemp.getPlusMinus()).intValue() == 1) {
                    psTemp.setString("days", DmServer.vNull(mrTemp.getDays()));
                } else {
                    psTemp.setString("days", "-" + DmServer.vNull(mrTemp.getDays(), "0.00"));
                }

                psTemp.setString("from_Eff_DateTime", DmShared.xNull(mrTemp.getFromDate()) + " " + DmShared.xNull(mrTemp.getFromTime()).replace(".", ":"));
                psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
                psTemp.executeUpdate();
                psTemp.close();
            }

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
    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery;

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select pyl.docId,isNull(pyl.v_No,'') EntryNo,sg.name as EmployeeName,pyl.applyDate,pyl.fromDate,pyl.toDate,"
                    + "lm.name as LeaveNature,pyl.isStatus\n"
                    + "from pyLeaveEntry pyl\n"
                    + "left join subGroup sg on sg.subCode = pyl.employeeCode\n"
                    + "left join attendenceCodeDefinitionMast lm on lm.code = pyl.leaveCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                listNode.setEmployeeName(DmShared.xNull(rsTemp.getString("EmployeeName")));
                listNode.setApplyDate(DmShared.dNull(rsTemp.getDate("applyDate")));
                listNode.setFromDate(DmShared.dNull(rsTemp.getDate("fromDate")));
                listNode.setToDate(DmShared.dNull(rsTemp.getDate("toDate")));
                listNode.setLeaveName(DmShared.xNull(rsTemp.getString("LeaveNature")));
                listNode.setEntryNo(DmShared.xNull(rsTemp.getString("EntryNo")));
                listNode.setStatusCode(DmShared.xNull(rsTemp.getString("isStatus")));
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
    public DmResult<KeyFieldExtended> getCurrentShiftTime(String strKey) {
        DmResult<KeyFieldExtended> dmrTemp = new DmResult();
        KeyFieldExtended shiftDet = new KeyFieldExtended();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData(shiftDet);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "select top 1 sm.shiftName,sm.fromTime,sm.toTime\n"
                    + "	from subGroup sg \n"
                    + "	left join pyEmployeeShiftDetail esd on sg.subCode=esd.employeeCode and '"+getServerDateTime()[0]+"' between esd.fromDate and esd.toDate\n"
                    + "	left join shiftMast sm on isNull(esd.shiftCode,sg.shiftCode)=sm.code "
                    + "	where sg.subCode= :employeeCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("employeeCode", strKey);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                shiftDet = new KeyFieldExtended();
                shiftDet.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("fromTime")));
                shiftDet.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("toTime")));
            }

            psTemp.close();
            dmrTemp.setData(shiftDet);
            dmrTemp.setStatus(true);
            cnTemp.close();

        } catch (Exception e) {
            try {
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
}
