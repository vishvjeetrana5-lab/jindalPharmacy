package dataman.payroll.attendenceEntryManual.server;

import dataman.payroll.attendenceEntryManual.shared.*;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import dataman.payroll.self.shared.PREnum.*;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.payroll.self.shared.PREnum;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AttendenceEntryManual_SrvImpl extends DmRSSPrivate implements AttendenceEntryManual_Srv {

    private LibraryVoucherCategory lvc = new LibraryVoucherCategory();

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
            rsTemp = cnTemp.createStatement().executeQuery("select docid as docId from dailyAttendance ");

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
        String monthName, monthYear, dateReturn;
        int monthNumber = 0;
        monthSelected = monthSelected.replace("'", "");
        monthSelectedList = monthSelected.split("-");
        monthName = monthSelectedList[0];
        dateReturn = "01-" + monthSelected;
        return dateReturn;
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
                    + " from subGroup  sg\n"
                    + " left join subGroup2 sg2 on sg2.subCode = sg.subCode\n"
                    + " left join subGroupNature sgn on sgn.code = sg.filterNature \n"
                    + " left join subGroup sgd on sgd.subCode = sg2.hrDepartmentCode\n"
                    + " left join pyEmployeeShiftDetail pesd on sg.subCode = pesd.employeeCode and '"+getServerDateTime()[0]+"' between pesd.fromDate and pesd.toDate\n"
                    + " left join shiftMast sm on isNull(pesd.shiftCode,sg.shiftCode) = sm.code\n"
                    + " where sgn.manualCode = 'EM' and sg.isActive = 1");

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Employee_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Employee")));
                listNode.setEmployee(DmShared.xNull(rsTemp.getString("Employee")));
                listNode.setDepartment(DmShared.xNull(rsTemp.getString("department")));
                listNode.setFatherName(DmShared.xNull(rsTemp.getString("fatherName")));
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
    public DmResult<Default_Mdl> getDefaultValue() {
        DmResult<Default_Mdl> dmrTemp = new DmResult<>();
        Default_Mdl mrTemp = new Default_Mdl();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            //==============================================<>==========================================================
            sqlQuery = "select acd.code,acd.name from attendenceCodeDefinitionMast acd \n"
                    + "where acd.manualCode = 'PP' ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                mrTemp.setAttendanceName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.setAttendanceCode(DmShared.xNull(rsTemp.getString("code")));
            }

            rsTemp.close();
            psTemp.close();

            dmrTemp.setData(mrTemp);
            dmrTemp.setStatus(true);
            dmrTemp.setValue1(DmServer.getServerDateTime()[0]);
            dmrTemp.setValue2(DmServer.getServerDateTime()[1]);
            cnTemp.close();

        } catch (Exception e) {
            dmrTemp.setErrorMessage(e.getMessage());
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

            sqlQuery = "delete from dailyAttendance where docId = :docId";
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
    public DmResult<AttendenceEntryManual_Mdl> getMoveRec(String strKey) {
        DmResult<AttendenceEntryManual_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        AttendenceEntryManual_Mdl mrTemp = new AttendenceEntryManual_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select da.docId,da.v_Date,da.recID,da.v_No,da.employeeCode,da.attendanceCode,da.fromDate,da.fromTime,da.toDate\n"
                    + ",da.toTime,da.days,da.remark,acd.name as attendanceName,acd.manualCode as shortName,sg.name as employeeName\n"
                    + ",da.duration,da.v_Type,da.plusMinus,sg2.hrDepartmentCode,sgd.name as deptName\n"
                    + "	from dailyAttendance da\n"
                    + "	left join attendenceCodeDefinitionMast acd on da.attendanceCode=acd.code\n"
                    + "	left join subGroup sg on da.employeeCode=sg.subCode\n"
                    + "	left join subGroup2 sg2 on sg2.subCode=sg.subCode\n"
                    + "	left join subGroup sgd on sg2.hrDepartmentCode=sgd.subCode\n"
                    + " where da.docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", strKey);
            rsTemp = psTemp.executeQuery();

            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                mrTemp.setEntryDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                mrTemp.setEmployeeCode(DmShared.xNull(rsTemp.getString("EmployeeCode")));
                mrTemp.setEmployeeName(DmShared.xNull(rsTemp.getString("EmployeeName")));
                mrTemp.setDepartmentCode(DmShared.xNull(rsTemp.getString("hrDepartmentCode")));
                mrTemp.setDepartmentName(DmShared.xNull(rsTemp.getString("deptName")));
                mrTemp.getRecId().setRecIdValue(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.getRecId().setCounter(DmShared.iNull(rsTemp.getString("v_No")).longValue());
                mrTemp.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                mrTemp.setFromDate(DmShared.dNull(rsTemp.getDate("fromDate")));
                mrTemp.setFromTime(DmServer.vNull(rsTemp.getDouble("fromTime"), "0.00"));
                mrTemp.setToDate(DmShared.dNull(rsTemp.getDate("toDate")));
                mrTemp.setToTime(DmServer.vNull(rsTemp.getDouble("toTime"), "0.00"));
                mrTemp.setDuration(DmShared.xNull(rsTemp.getString("duration")));
                mrTemp.setAttendanceCode(DmShared.xNull(rsTemp.getString("attendanceCode")));
                mrTemp.setAttendanceName(DmShared.xNull(rsTemp.getString("attendanceName")));
                mrTemp.setAttendanceShortName(DmShared.xNull(rsTemp.getString("shortName")));
                mrTemp.setDays(DmShared.xNull(rsTemp.getString("days")));
                mrTemp.setPlusMinus(DmShared.xNull(rsTemp.getString("plusMinus")));
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

                sqlQuery = "select DATEDIFF(minute, :fromDate, :toDate) duration";
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
    public DmResult<String> saveRecord(AttendenceEntryManual_Mdl mrTemp, String voucherPrefix) {

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

            AttendenceEntryManual_Vld.chkSave(mrTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            v_Type = DmServer.getVType(lvc.ATTENDANCE_ENTRY_MANUAL, cnTemp);
            currentDateTime = getServerDateTime();
            if (DmShared.xNull(mrTemp.getDtVoucher()).trim().equalsIgnoreCase("")) {
                mrTemp.setDtVoucher(currentDateTime[0]);
            }

            /**
             * ****************Checking previous entry for same employee and
             * date********************
             */
            sqlQuery = "select docid \n"
                    + "	from dailyAttendance da\n"
                    + " where da.employeeCode = :employeeCode and da.fromDate=:fromDate ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("employeeCode", DmShared.foriegnKey(mrTemp.getEmployeeCode()));
            psTemp.setString("fromDate", DmShared.xNull(mrTemp.getFromDate()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("docid")).longValue() != 0) {
                    throw new DmException(100, "Record already exist.");
                }
            }
            rsTemp.close();
            psTemp.close();
            /**
             * ****************************************************************************************
             */

            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(v_Type), voucherPrefix, cnTemp, this));
                mrTemp.setRecId(DmServer.getRecId("dailyAttendance", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecId(), mrTemp.getEntryDate(), DmShared.xNull(v_Type), voucherPrefix, cnTemp, this, true));
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            if (blnAdd) {
                sqlQuery = "insert into dailyAttendance(docId,v_Type,recId,v_No,recIdPrefix,v_Prefix,v_Date,employeeCode,attendanceCode,fromDate\n"
                        + " ,fromTime,toDate,toTime,duration,days,plusMinus,remark,site_Code,u_Name,u_EntDt)  "
                        + " values(:docId,:v_Type,:recId,:v_No,:recIdPrefix,:v_Prefix,:v_Date,:employeeCode,:attendanceCode,:fromDate\n"
                        + " ,:fromTime,:toDate,:toTime,:duration,:days,:plusMinus,:remark,:site_Code,:u_Name,:u_EntDt)";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("v_Type", DmShared.foriegnKey(v_Type));
                psTemp.setString("recId", DmShared.xNull(mrTemp.getRecId().getRecIdValue()));
                psTemp.setString("v_No", DmShared.iNull(mrTemp.getRecId().getCounter()).toString());
                psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getRecId().getPrefix()));
                psTemp.setString("v_Prefix", voucherPrefix);
                psTemp.setString("v_Date", DmShared.xNull(mrTemp.getEntryDate()));
                psTemp.setString("employeeCode", DmShared.foriegnKey(mrTemp.getEmployeeCode()));
                psTemp.setString("attendanceCode", DmShared.foriegnKey(mrTemp.getAttendanceCode()));
                psTemp.setString("fromDate", DmShared.xNull(mrTemp.getFromDate()));
                psTemp.setString("fromTime", DmServer.vNull(mrTemp.getFromTime()));
                psTemp.setString("toDate", DmShared.xNull(mrTemp.getToDate()));
                psTemp.setString("toTime", DmServer.vNull(mrTemp.getToTime()));
                psTemp.setString("duration", DmServer.vNull(mrTemp.getDuration()));
                psTemp.setString("days", DmServer.vNull(mrTemp.getDays()));
                psTemp.setString("plusMinus", DmServer.vNull(mrTemp.getPlusMinus()));
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
                psTemp.setString("U_name", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("U_EntDt", DmServer.getServerTimestamp());
            }else {
                sqlQuery = "update dailyAttendance set employeeCode=:employeeCode "
                        + ",attendanceCode=:attendanceCode"
                        + ",fromDate=:fromDate\n"
                        + " ,fromTime=:fromTime"
                        + ",toDate=:toDate"
                        + ",toTime=:toTime"
                        + ",duration=:duration"
                        + ",days=:days"
                        + ",plusMinus=:plusMinus\n"
                        + ",remark=:remark"
                        + ",u_Name=:u_Name"
                        + ",u_EntDt=:u_EntDt "
                        + " where docid=:Docid ";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("employeeCode", DmShared.foriegnKey(mrTemp.getEmployeeCode()));
                psTemp.setString("attendanceCode", DmShared.foriegnKey(mrTemp.getAttendanceCode()));
                psTemp.setString("fromDate", DmShared.xNull(mrTemp.getFromDate()));
                psTemp.setString("fromTime", DmServer.vNull(mrTemp.getFromTime()));
                psTemp.setString("toDate", DmShared.xNull(mrTemp.getToDate()));
                psTemp.setString("toTime", DmServer.vNull(mrTemp.getToTime()));
                psTemp.setString("duration", DmServer.vNull(mrTemp.getDuration()));
                psTemp.setString("days", DmServer.vNull(mrTemp.getDays()));
                psTemp.setString("plusMinus", DmServer.vNull(mrTemp.getPlusMinus()));
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setString("U_name", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("U_EntDt", DmServer.getServerTimestamp());
            }
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "delete from Employee_Attendance_Leave_Detail where docId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "insert into Employee_Attendance_Leave_Detail(docId,v_Date,employeeCode,att_Lev_OT,attendanceCode,fromDate\n"
                    + " ,fromTime,toDate,toTime,totMinuts,days,from_Eff_DateTime,to_Eff_DateTime,site_Code,isStatus)  "
                    + " values(:docId,:v_Date,:employeeCode,:att_Lev_OT,:attendanceCode,:fromDate\n"
                    + " ,:fromTime,:toDate,:toTime,:totMinuts,:days,:from_Eff_DateTime,:to_Eff_DateTime,:site_Code,:isStatus)";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.setString("v_Date", DmShared.xNull(mrTemp.getEntryDate()));
            psTemp.setString("employeeCode", DmShared.foriegnKey(mrTemp.getEmployeeCode()));
            psTemp.setString("att_Lev_OT", DmServer.vNull(TYPE_LEAVE_ATTENDANCE.getViaText("ATTENDANCE").getId()));
            psTemp.setString("attendanceCode", DmShared.foriegnKey(mrTemp.getAttendanceCode()));
            psTemp.setString("fromDate", DmShared.xNull(mrTemp.getFromDate()));
            psTemp.setString("fromTime", DmServer.vNull(mrTemp.getFromTime()));
            psTemp.setString("toDate", DmShared.xNull(mrTemp.getToDate()));
            psTemp.setString("toTime", DmServer.vNull(mrTemp.getToTime()));
            psTemp.setString("totMinuts", DmServer.vNull(mrTemp.getDuration()));
            psTemp.setString("isStatus", DmShared.xNull(String.valueOf(PREnum.LEAVE_STATUS.APPROVED.getId())));
            if (DmShared.iNull(mrTemp.getPlusMinus()).intValue() == 1) {
                psTemp.setString("days", DmServer.vNull(mrTemp.getDays()));
            } else {
                psTemp.setString("days", "-" + DmServer.vNull(mrTemp.getDays(), "0.00"));
            }

            psTemp.setString("from_Eff_DateTime", DmShared.xNull(mrTemp.getFromDate()) + " " + DmShared.xNull(mrTemp.getFromTime()).replace(".", ":"));
            psTemp.setString("to_Eff_DateTime", DmShared.xNull(mrTemp.getToDate()) + " " + DmShared.xNull(mrTemp.getToTime()).replace(".", ":"));
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
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

            sqlQuery = "select da.docId,isNull(da.recID,'') EntryNo,sg.name as EmployeeName,da.v_Date,da.fromDate,da.toDate,lm.name as attendanceName"
                    + " from dailyAttendance da \n"
                    + " left join subGroup sg on sg.subCode = da.employeeCode\n"
                    + " left join attendenceCodeDefinitionMast lm on lm.code = da.attendanceCode ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                listNode.setEmployeeName(DmShared.xNull(rsTemp.getString("EmployeeName")));
                listNode.setEntryDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setFromDate(DmShared.dNull(rsTemp.getDate("fromDate")));
                listNode.setToDate(DmShared.dNull(rsTemp.getDate("toDate")));
                listNode.setEntryNo(DmShared.xNull(rsTemp.getString("EntryNo")));
                listNode.setAttendanceName(DmShared.xNull(rsTemp.getString("attendanceName")));
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
    public DmResultPageing<List<AttendenceTextHelp_Mdl>> getTxtHAttendence(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<AttendenceTextHelp_Mdl>> rstTemp = new DmResultPageing<>();
        List<AttendenceTextHelp_Mdl> lstTemp = new ArrayList<>();
        AttendenceTextHelp_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery;

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select acd.code,acd.name,acd.days,acd.manualCode,acd.plusMinus\n"
                    + "	from attendenceCodeDefinitionMast acd \n"
                    + "	where acd.type_Leave_Attendance=1";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new AttendenceTextHelp_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setAttendance(DmShared.xNull(rsTemp.getString("name")));
                listNode.setShortName(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setDays(DmShared.xNull(rsTemp.getString("days")));
                listNode.setPlusMinus(DmShared.xNull(rsTemp.getString("plusMinus")));
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
