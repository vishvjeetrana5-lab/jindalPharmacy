package dataman.payroll.attendanceEntry.server;

import dataman.payroll.attendanceEntry.shared.Find_Mdl;
import dataman.payroll.attendanceEntry.shared.GrdAttendanceEntry_Mdl;
import dataman.payroll.attendanceEntry.shared.AttendanceEntry_Mdl;
import dataman.payroll.attendanceEntry.shared.AttendanceEntry_Vld;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import dataman.payroll.self.shared.PREnum.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import dataman.payroll.attendanceEntry.shared.AttendanceEntry_Srv;
import dataman.payroll.attendanceEntry.shared.EmployeeLeave_Mdl;
import dataman.payroll.attendanceEntry.shared.Attendance_Mdl;
import dataman.payroll.attendanceEntry.shared.Leave_Mdl;
import dataman.payroll.attendanceEntry.shared.SalaryMethod_Mdl;
import dataman.payroll.self.shared.PREnum;

public class AttendanceEntry_SrvImpl extends DmRSSPrivate implements AttendanceEntry_Srv {

    private LibraryVoucherCategory lvc = new LibraryVoucherCategory();
    String byDefaultAttendanceCode = "", offDayCode = "", holidayCode = "",notAppliedAttendanceCode = "";

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
            rsTemp = cnTemp.createStatement().executeQuery("select max(py.docId) docId from PY_AttendanceEntry py group by py.docId");

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

    private String getDate(String monthSelected) throws ParseException {

        String[] monthSelectedList;
        String monthName, monthYear, dateReturn;
        int monthNumber = 0;
        monthSelected = monthSelected.replace("'", "");
        monthSelected = monthSelected.replace(" ", "-");
        monthSelectedList = monthSelected.split("-");
        monthName = monthSelectedList[0];
        dateReturn = "01-" + monthSelected;
        //Start

        //End
        return dateReturn;
    }

    private List<String> getHolidaysList(Connection cnTemp, String currentYear) throws SQLException {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        List<String> dateList = new ArrayList<>();
        String[] stringList;
        List<String> rtnList = new ArrayList<>();
        String holiday = "";
        String holidayYear = "";
        Date date1;
        Calendar calendar = Calendar.getInstance();

        sqlQuery = "select date from holidayMast";
        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
        rsTemp = psTemp.executeQuery();
        while (rsTemp.next()) {
            dateList.add(rsTemp.getString("date"));
        }
        for (String dates : dateList) {

            //=====================================================
            date1 = DmDateTimeFormat.getFormat("yyyy-MM-dd").parse(dates);
            calendar.setTime(date1);

            DmEnum.DmMonths monthObject = DmEnum.DmMonths.getValueFrom(calendar.get(Calendar.MONTH));
            //=================================================
            stringList = dates.split("-");
            holidayYear = String.valueOf(calendar.get(Calendar.YEAR));
            holiday = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));

            if (holidayYear.equalsIgnoreCase(currentYear)) {
                rtnList.add(monthObject.getMonthName() + "," + holiday);
            }
        }
        psTemp.close();
        rsTemp.close();
        return rtnList;
    }

    private List<Leave_Mdl> getLeaveList(Connection cnTemp, String currentYear, String currentMonth) throws SQLException {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        List<Leave_Mdl> leaveMdlList = new ArrayList<>();
        List<Leave_Mdl> rtnLeaveMdlList = new ArrayList<>();
        Leave_Mdl leaveMdl, rtnLeaveMdl;
        String fromDate = "";
        String leaveYear = "";
        String leaveMonth = "";
        Date date1;
        Calendar calendar = Calendar.getInstance();

        sqlQuery = "select pyl.docId,pyl.employeeCode,pyl.fromDate,pyl.duration,lm.manualCode,pyl.leaveCode from pyLeaveEntry pyl\n"
                + "left join attendenceCodeDefinitionMast lm on lm.code = pyl.leaveCode where pyl.isStatus = 1";
        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
        // psTemp.setString(employeeCode, DmShared.xNull(employeeCode));
        rsTemp = psTemp.executeQuery();
        while (rsTemp.next()) {
            leaveMdl = new Leave_Mdl();
            leaveMdl.setFromDate(DmShared.xNull(rsTemp.getString("fromDate")));
            leaveMdl.setDuration(DmShared.xNull(rsTemp.getString("duration")));
            leaveMdl.setEmployeeCode(DmShared.xNull(rsTemp.getString("employeeCode")));
            leaveMdl.setLeaveManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
            leaveMdl.setLeaveCode(DmShared.xNull(rsTemp.getString("leaveCode")));
            leaveMdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
            leaveMdlList.add(leaveMdl);

        }
        // refining leaveMdlList as per year and month    
        for (Leave_Mdl monthWiseLeaveMdl : leaveMdlList) {
            fromDate = DmShared.xNull(monthWiseLeaveMdl.getFromDate());
            //    for (String dates : fromDateList) {

            //=====================================================
            date1 = DmDateTimeFormat.getFormat("yyyy-MM-dd").parse(fromDate);
            calendar.setTime(date1);

            DmEnum.DmMonths monthObject = DmEnum.DmMonths.getValueFrom(calendar.get(Calendar.MONTH));
            //=================================================

            leaveYear = String.valueOf(calendar.get(Calendar.YEAR));
            leaveMonth = monthObject.getMonthName();

            if (leaveYear.equalsIgnoreCase(currentYear)) {
                if (leaveMonth.toLowerCase().equalsIgnoreCase(currentMonth.toLowerCase())) {
                    rtnLeaveMdl = new Leave_Mdl();
                    rtnLeaveMdl.setFromDate(DmShared.xNull(monthWiseLeaveMdl.getFromDate()));
                    rtnLeaveMdl.setDuration(DmShared.xNull(monthWiseLeaveMdl.getDuration()));
                    rtnLeaveMdl.setEmployeeCode(DmShared.xNull(monthWiseLeaveMdl.getEmployeeCode()));
                    rtnLeaveMdl.setLeaveManualCode(DmShared.xNull(monthWiseLeaveMdl.getLeaveManualCode()));
                    rtnLeaveMdl.setLeaveCode(DmShared.xNull(monthWiseLeaveMdl.getLeaveCode()));
                    rtnLeaveMdl.setSharedKeyFieldCode(DmShared.xNull(monthWiseLeaveMdl.getSharedKeyFieldCode()));
                    rtnLeaveMdlList.add(rtnLeaveMdl);
                }
                //      rtnList.add(monthObject.getMonthName() + "," + leave);
            }
        }
        psTemp.close();
        rsTemp.close();
        return rtnLeaveMdlList;
    }

    private List<EmployeeLeave_Mdl> getEmployeeLeaveMonthWise(String employeeCode, List<Leave_Mdl> leaveMdlList) throws SQLException {
        List<String> leaveList;
        List<EmployeeLeave_Mdl> employeeLeaveMdlList = new ArrayList<>();
        EmployeeLeave_Mdl employeeLeaveMdl;
        String fromDate = "";
        Date date1;
        Calendar calendar = Calendar.getInstance();
        int leaveDate;
        int count;

        for (Leave_Mdl leaveMdl : leaveMdlList) {
            if (DmShared.xNull(leaveMdl.getEmployeeCode()).equalsIgnoreCase(employeeCode)) {
                employeeLeaveMdl = new EmployeeLeave_Mdl();
                leaveList = new ArrayList<>();
                fromDate = DmShared.xNull(leaveMdl.getFromDate());
                date1 = DmDateTimeFormat.getFormat("yyyy-MM-dd").parse(fromDate);
                calendar.setTime(date1);
                leaveDate = calendar.get(Calendar.DAY_OF_MONTH);
                count = 1;

                while (count <= Integer.valueOf(leaveMdl.getDuration())) {
                    leaveList.add(String.valueOf(leaveDate));
                    leaveDate++;
                    count++;
                }
                employeeLeaveMdl.setRtnList(leaveList);
                employeeLeaveMdl.setLeaveNature(leaveMdl.getLeaveManualCode());
                employeeLeaveMdl.setLeaveCode(leaveMdl.getLeaveCode());
                employeeLeaveMdlList.add(employeeLeaveMdl);
            }

        }
        return employeeLeaveMdlList;
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

    private LinkedHashMap<String, String> getDaysListCode(LinkedHashMap<String, String> daysList2, String byDefaultCode, String holidayCode) throws SQLException {
        LinkedHashMap<String, String> daysCodeList = new LinkedHashMap(31);
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "";
        List<String> leaveNatureList = new ArrayList<>();
        List<String> leaveCodeList = new ArrayList<>();

        cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
        sqlQuery = "select acm.manualCode,acm.code from attendenceCodeDefinitionMast acm ";
        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
        rsTemp = psTemp.executeQuery();
        while (rsTemp.next()) {
            leaveCodeList.add(DmShared.xNull(rsTemp.getString("code")));
            leaveNatureList.add(DmShared.xNull(rsTemp.getString("manualCode")));
        }
        rsTemp.close();
        psTemp.close();
        cnTemp.close();

        if (daysCodeList.isEmpty()) {
            for (int i = 1; i <= 31; i++) {
                daysCodeList.put(String.valueOf(i), "");
            }
        }

        for (int i = 0; i < daysList2.size(); i++) {

            if (!daysList2.get(String.valueOf(i)).equalsIgnoreCase("")) {
                daysCodeList.put(String.valueOf(i), byDefaultCode);
            }

        }
        //======================= Add holiday code =====================================
        for (int i = 0; i < daysList2.size(); i++) {

            if (daysList2.get(String.valueOf(i)).equalsIgnoreCase("HH")) {
                daysCodeList.put(String.valueOf(i), holidayCode);
            }
        }
//====================< Add Leave Code >==============================================
        for (int i = 0; i < daysList2.size(); i++) {

            for (int j = 0; j < leaveCodeList.size(); j++) {
                if (daysList2.get(String.valueOf(i)).equalsIgnoreCase(leaveNatureList.get(j))) {
                    daysCodeList.put(String.valueOf(i), leaveCodeList.get(j));
                }
            }

        }

        return daysCodeList;
    }

    private LinkedHashMap<String, String> getDaysList(Connection cnTemp, String code, String monthDays, String date, String month, String year, List<EmployeeLeave_Mdl> leaveList) throws SQLException {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        int weekDayCode = 0;
        String sqlQuery;

        LinkedHashMap<String, String> daysList2 = new LinkedHashMap(31);
        LinkedHashMap<String, String> daysCodeList = new LinkedHashMap(31);
        LinkedHashMap<String, String> offDayList = new LinkedHashMap(7);
        String firstDayOfMonth = "";
        int firstDayOfMonthCode = 0;
        String byDefault = "";
        String byDefaultCode = "";
        int weekDayFirstDate;
        int j = 0;
        List<String> holidayList = new ArrayList<String>();
        List<String> holidays = new ArrayList<String>();
        sqlQuery = "select sg2.offDay from subGroup2 sg2 where sg2.subCode = :code";
        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

        psTemp.setString("code", DmShared.iNull(code).toString());
        rsTemp = psTemp.executeQuery();
        while (rsTemp.next()) {
            weekDayCode = rsTemp.getInt("offDay");
        }
        psTemp.close();
        rsTemp.close();

//==========================================================================
        weekDayFirstDate = getReturnIndex(cnTemp, date, weekDayCode);

        if (daysList2.isEmpty()) {
            for (int i = 0; i < 31; i++) {
                daysList2.put(String.valueOf(i), "");
            }
        }
        // Now set byDafault = 'PP' value to entire list

        sqlQuery = "select acm.manualCode,acm.code from attendenceCodeDefinitionMast acm where acm.byDefault = 1";
        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
        rsTemp = psTemp.executeQuery();
        if (rsTemp.next()) {
            byDefault = DmShared.xNull(rsTemp.getString("manualCode"));
            byDefaultCode = DmShared.xNull(rsTemp.getString("code"));
        }
        rsTemp.close();
        psTemp.close();

//======================< Get Holidays List of Employee >===================================
        holidayList = getHolidaysList(cnTemp, year);

        for (String listNode : holidayList) {
            if (listNode.toLowerCase().contains(month.toLowerCase())) {
                listNode = listNode.split(",")[1];
                holidays.add(listNode);
            }
        }

//==============================< Fill values to List including 'Off Days' and 'Holidays' >=====================================================
        for (int i = 0; i < 31; i++) {

            if (i < Integer.valueOf(monthDays)) {
                if (i == weekDayFirstDate - 1) {
                    daysList2.put(String.valueOf(i), "");
                    weekDayFirstDate = weekDayFirstDate + 7;
                } else {
                    daysList2.put(String.valueOf(i), byDefault);
                    //    daysCodeList.put(String.valueOf(i),byDefaultAttendanceCode);
                }
//============== Adding holidays to the list ==============================
                for (String holidaysValue : holidays) {
                    if (i == Integer.valueOf(holidaysValue) - 1) {
                        daysList2.put(String.valueOf(i), "HH");
                    }
                }
//==================== Adding Leaves to the list ================================
                for (EmployeeLeave_Mdl empMdl : leaveList) {
                    for (int p = 0; p < empMdl.getRtnList().size(); p++) {
                        if (i == Integer.valueOf(empMdl.getRtnList().get(p)) - 1) {
                            daysList2.put(String.valueOf(i), empMdl.getLeaveNature());
                        }
                    }
                }

            } else {
                daysList2.put(String.valueOf(i), "NA");
            }

        }

        return daysList2;
//========================================================================================       
    }

    private int getReturnIndex(Connection cnTemp, String date, int weekDayCode) throws SQLException {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        LinkedHashMap<String, String> offDayList = new LinkedHashMap(7);
        String firstDayOfMonth = "";
        int firstDayOfMonthCode = 0;
        int j = 0;
        int rtnIndex = 0;

//==========< Start >=======================================
        sqlQuery = "select DATENAME(WEEKDAY,'" + date + "') as dayName";
        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
        rsTemp = psTemp.executeQuery();
        if (rsTemp.next()) {
            firstDayOfMonth = DmShared.xNull(rsTemp.getString("dayName"));
        }
        firstDayOfMonthCode = DmEnum.DmWeekdays.getValueFrom(firstDayOfMonth).getWeekDay();

        for (int i = 1; i <= 7; i++) {
            if (firstDayOfMonthCode > 7) {
                firstDayOfMonthCode = firstDayOfMonthCode - 7;
            }
            offDayList.put(String.valueOf(i), String.valueOf(firstDayOfMonthCode));
            firstDayOfMonthCode++;
        }

        rsTemp.close();
        psTemp.close();

        for (int i = 1; i <= 7; i++) {
            if (offDayList.get(String.valueOf(i)).equalsIgnoreCase(String.valueOf(weekDayCode))) {
                rtnIndex = i;
            }
        }
//=============< End >==================================================================

        return rtnIndex;
//========================================================================================       
    }

    @Override
    public List<GrdAttendanceEntry_Mdl> getFillRecords(AttendanceEntry_Mdl param) {

        List<GrdAttendanceEntry_Mdl> lstTemp = new ArrayList<>();
        List<Leave_Mdl> leaveMdlList = new ArrayList<>();
        LinkedHashMap<String, String> employeeLeaveMdlList = new LinkedHashMap();
        List<LinkedHashMap<String, String>> employeeLeaveRtnList = new ArrayList<>();
        List<EmployeeLeave_Mdl> leaveList = new ArrayList<>();
        LinkedHashMap<String, String> daysList = new LinkedHashMap();
        LinkedHashMap<String, String> daysCodeList = new LinkedHashMap(31);
        GrdAttendanceEntry_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery, condition, join;
        String totalDays;
        String holiDays;
        String monthDays = "";
        String date = "";
        DmHelper helper2 = new DmHelper();
        String monthSelected = "";
        String salaryMethodCode = "";
        String departmentCode = "";

        int holidays = 0;
        String byDefault = "";

        String month = "";
        String year = "";
        String byDefaultCode = "";
        String holidayCode = "";
        String leaveNature = "";
        String leaveCode = "";
        Date date1;
        Calendar calendar = Calendar.getInstance();

        try {

            condition = "";

            if (!DmShared.xNull(param.getSalaryMethodName()).equalsIgnoreCase("")) {
                monthSelected = param.getSalaryMethodName();
                date = param.getFromDate();
            }

            //=====================================================
            date1 = DmDateTimeFormat.getFormat("yyyy-MM-dd").parse(date);
            calendar.setTime(date1);

            DmEnum.DmMonths monthObject = DmEnum.DmMonths.getValueFrom(calendar.get(Calendar.MONTH));
            month = monthObject.getMonthName();
            year = String.valueOf(calendar.get(Calendar.YEAR));

            //=================================================
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            // calculate number of days in a month
            sqlQuery = "SELECT DAY(EOMONTH('" + date + "')) days";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                monthDays = rsTemp.getString("days");
            }
            rsTemp.close();
            psTemp.close();

            salaryMethodCode = param.getSalaryMethodCode();
            holidays = getHolidays(cnTemp);

//===================< Get Code of byDefault >================================
            sqlQuery = "select acm.name,acm.code from attendenceCodeDefinitionMast acm where acm.byDefault = 1 and acm.type_Leave_Attendance=" + TYPE_LEAVE_ATTENDANCE.getViaText("ATTENDANCE").getId();
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                byDefaultCode = DmShared.xNull(rsTemp.getString("code"));
            }
            rsTemp.close();
            psTemp.close();

//===================< Get Code of holiday >================================
            sqlQuery = "select acm.manualCode,acm.code from attendenceCodeDefinitionMast acm where acm.manualCode = 'HH' and acm.type_Leave_Attendance=" + TYPE_LEAVE_ATTENDANCE.getViaText("ATTENDANCE").getId();
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                holidayCode = DmShared.xNull(rsTemp.getString("code"));
            }
            rsTemp.close();
            psTemp.close();
//=====================< Fetch Leave List >====================================================
            leaveMdlList = getLeaveList(cnTemp, year, month);
//=============================================================================================

            if (!DmShared.xNull(param.getDepartmentCode()).equalsIgnoreCase("")) {
                condition += " and sg2.hrDepartmentCode =" + param.getDepartmentCode();
            }
            sqlQuery = "select sg.subcode as code,sg.isActive as isActive,sg.name EmployeeName,sgn.name EmployeeNatureName,\n"
                    + "sg2.hrDepartmentCode DepartmentCode,sgdep.name as DepartmentName\n"
                    + "from subGroup sg \n"
                    + "left join subGroupNature sgn on sg.nature = sgn.code \n"
                    + "left join subGroupNature sgf on sg.filterNature = sgf.code\n"
                    + "left join subGroup2 sg2 on sg2.subCode = sg.subCode\n"
                    + "left join subGroup sgd on sgd.subCode = sg2.DepartmentCode\n"
                    + "left join subGroup sgdep on sgdep.subCode = sg2.hrDepartmentCode\n"
                    + "where sgf.manualCode = 'EM' and sg.isActive = 1 " + condition + "\n"
                    + "order by sg.name";

            sqlWithParam.setSQL(sqlQuery);

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new GrdAttendanceEntry_Mdl();

                //===========================================================================================
                leaveList = getEmployeeLeaveMonthWise(rsTemp.getString("code"), leaveMdlList);
                //============================================================================================

//=========================< Get Days List >==============================================
                daysList = getDaysList(cnTemp, rsTemp.getString("code"), monthDays, date, month, year, leaveList);
                daysCodeList = getDaysListCode(daysList, byDefaultCode, holidayCode);

                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("EmployeeName")));
                listNode.setEmployeeName(DmShared.xNull(rsTemp.getString("EmployeeName")));
                listNode.setEmployeeCode(DmShared.xNull(rsTemp.getString("code")));

                listNode.setDay1(daysList.get("0"));
                listNode.setDay1Code(daysCodeList.get("0"));
                listNode.setDay2(daysList.get("1"));
                listNode.setDay2Code(daysCodeList.get("1"));
                listNode.setDay3(daysList.get("2"));
                listNode.setDay3Code(daysCodeList.get("2"));
                listNode.setDay4(daysList.get("3"));
                listNode.setDay4Code(daysCodeList.get("3"));
                listNode.setDay5(daysList.get("4"));
                listNode.setDay5Code(daysCodeList.get("4"));
                listNode.setDay6(daysList.get("5"));
                listNode.setDay6Code(daysCodeList.get("5"));
                listNode.setDay7(daysList.get("6"));
                listNode.setDay7Code(daysCodeList.get("6"));
                listNode.setDay8(daysList.get("7"));
                listNode.setDay8Code(daysCodeList.get("7"));
                listNode.setDay9(daysList.get("8"));
                listNode.setDay9Code(daysCodeList.get("8"));
                listNode.setDay10(daysList.get("9"));
                listNode.setDay10Code(daysCodeList.get("9"));
                listNode.setDay11(daysList.get("10"));
                listNode.setDay11Code(daysCodeList.get("10"));
                listNode.setDay12(daysList.get("11"));
                listNode.setDay12Code(daysCodeList.get("11"));
                listNode.setDay13(daysList.get("12"));
                listNode.setDay13Code(daysCodeList.get("12"));
                listNode.setDay14(daysList.get("13"));
                listNode.setDay14Code(daysCodeList.get("13"));
                listNode.setDay15(daysList.get("14"));
                listNode.setDay15Code(daysCodeList.get("14"));
                listNode.setDay16(daysList.get("15"));
                listNode.setDay16Code(daysCodeList.get("15"));
                listNode.setDay17(daysList.get("16"));
                listNode.setDay17Code(daysCodeList.get("16"));
                listNode.setDay18(daysList.get("17"));
                listNode.setDay18Code(daysCodeList.get("17"));
                listNode.setDay19(daysList.get("18"));
                listNode.setDay19Code(daysCodeList.get("18"));
                listNode.setDay20(daysList.get("19"));
                listNode.setDay20Code(daysCodeList.get("19"));
                listNode.setDay21(daysList.get("20"));
                listNode.setDay21Code(daysCodeList.get("20"));
                listNode.setDay22(daysList.get("21"));
                listNode.setDay22Code(daysCodeList.get("21"));
                listNode.setDay23(daysList.get("22"));
                listNode.setDay23Code(daysCodeList.get("22"));
                listNode.setDay24(daysList.get("23"));
                listNode.setDay24Code(daysCodeList.get("23"));
                listNode.setDay25(daysList.get("24"));
                listNode.setDay25Code(daysCodeList.get("24"));
                listNode.setDay26(daysList.get("25"));
                listNode.setDay26Code(daysCodeList.get("25"));
                listNode.setDay27(daysList.get("26"));
                listNode.setDay27Code(daysCodeList.get("26"));
                listNode.setDay28(daysList.get("27"));
                listNode.setDay28Code(daysCodeList.get("27"));
                listNode.setDay29(daysList.get("28"));
                listNode.setDay29Code(daysCodeList.get("28"));
                listNode.setDay30(daysList.get("29"));
                listNode.setDay30Code(daysCodeList.get("29"));
                listNode.setDay31(daysList.get("30"));
                listNode.setDay31Code(daysCodeList.get("30"));

                lstTemp.add(listNode);
            }

            rsTemp.close();
            psTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
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
        return lstTemp;
    }

    @Override
    public DmResultPageing<List<Attendance_Mdl>> getTxtHStatus(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<Attendance_Mdl>> rstTemp = new DmResultPageing<>();
        List<Attendance_Mdl> lstTemp = new ArrayList<>();
        Attendance_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("select acm.manualCode,acm.code,acm.days,acm.natureCode,acm.type_Leave_Attendance,acm.plusMinus from attendenceCodeDefinitionMast acm where isNull(isActive,1)=1");

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Attendance_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setDays(DmShared.xNull(rsTemp.getString("days")));
                listNode.setNatureCode(DmShared.xNull(rsTemp.getString("natureCode")));
                listNode.setType_Leave_Attendance(DmShared.xNull(rsTemp.getString("type_Leave_Attendance")));
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

    @Override
    public DmResultPageing<List<SalaryMethod_Mdl>> getTxtHSalaryMethod(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<SalaryMethod_Mdl>> rstTemp = new DmResultPageing<>();
        List<SalaryMethod_Mdl> data = new ArrayList<>();
        SalaryMethod_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        String salaryMethodCode = "";
        String[] salaryMethodCodeList;
        DmHelper helper2 = new DmHelper();
        String condition = "";
        String siteCode = "";

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            siteCode = DmServer.getSsnSiteCode(this);
            sqlWithParam.setSQL("select code,name,fromDate from salaryCodeDefinitionMast where site_code =" + siteCode);

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            // psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new SalaryMethod_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSalaryMethodName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setFromDate(DmShared.xNull(rsTemp.getString("fromDate")));
                data.add(listNode);
            }
            helper.positioning(data.size(), blnToEndOfFile);
            rstTemp.setData(data);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartmentName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> data = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(data);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("select subCode as code,name  from subGroup where nature = "
                    + "(select code from subGroupNature where manualCode = 'PY')");

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
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

    private List<GrdAttendanceEntry_Mdl> getAttendanceMdlList(String docId, AttendanceEntry_Mdl mrTemp, Connection cnTemp) {
        String sqlQuery = "", condition = "";
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<GrdAttendanceEntry_Mdl> grdAttendanceEntryList = new ArrayList<GrdAttendanceEntry_Mdl>();
        GrdAttendanceEntry_Mdl grdAttendanceEntry_Mdl;

        try {

            sqlQuery = "SELECT py.docId, py.vSno, isNull(py.v_Type,'') v_Type, sdm.name salaryMethod, sg.name departmentName,py.salaryMethod salaryMethodCode,py.departmentCode departmentCode,py.employeeCode, sge.name as employeeName, \n"
                    + "isNull(acdm1.manualCode,'') day_1,isNull(py.day_1,'') day1Code, isNull(acdm2.manualCode,'') day_2,isNull(py.day_2,'') day2Code, isNull(acdm3.manualCode,'') day_3,isNull(py.day_3,'') day3Code, \n"
                    + "isNull(acdm4.manualCode,'') day_4,isNull(py.day_4,'') day4Code, isNull(acdm5.manualCode,'') day_5,isNull(py.day_5,'') day5Code, isNull(acdm6.manualCode,'') day_6,isNull(py.day_6,'') day6Code,\n"
                    + "isNull(acdm7.manualCode,'') day_7,isNull(py.day_7,'') day7Code,isNull(acdm8.manualCode,'') day_8,isNull(py.day_8,'') day8Code, isNull(acdm9.manualCode,'') day_9,isNull(py.day_9,'') day9Code, isNull(acdm10.manualCode,'') day_10,isNull(py.day_10,'') day10Code,\n"
                    + "isNull(acdm11.manualCode,'') day_11,isNull(py.day_11,'') day11Code,isNull(acdm12.manualCode,'') day_12,isNull(py.day_12,'') day12Code, isNull(acdm13.manualCode,'') day_13,isNull(py.day_13,'') day13Code, \n"
                    + "isNull(acdm14.manualCode,'') day_14,isNull(py.day_14,'') day14Code, isNull(acdm15.manualCode,'') day_15,isNull(py.day_15,'') day15Code,isNull(acdm16.manualCode,'') day_16,isNull(py.day_16,'') day16Code,\n"
                    + "isNull(acdm17.manualCode,'') day_17,isNull(py.day_17,'') day17Code, isNull(acdm18.manualCode,'') day_18,isNull(py.day_18,'') day18Code,isNull( acdm19.manualCode,'') day_19,isNull(py.day_19,'') day19Code,\n"
                    + "isNull(acdm20.manualCode,'') day_20,isNull(py.day_20,'') day20Code, isNull(acdm21.manualCode,'') day_21,isNull(py.day_21,'') day21Code,isNull(acdm22.manualCode,'') day_22,isNull(py.day_22,'') day22Code,\n"
                    + "isNull(acdm23.manualCode,'') day_23,isNull(py.day_23,'') day23Code, isNull(acdm24.manualCode,'') day_24,isNull(py.day_24,'') day24Code, isNull(acdm25.manualCode,'') day_25,isNull(py.day_25,'') day25Code, \n"
                    + "isNull(acdm26.manualCode,'') day_26,isNull(py.day_26,'') day26Code,isNull(acdm27.manualCode,'') day_27,isNull(py.day_27,'') day27Code, isNull(acdm28.manualCode,'') day_28,isNull(py.day_28,'') day28Code, \n"
                    + "isNull(acdm29.manualCode,'') day_29,isNull(py.day_29,'') day29Code, isNull(acdm30.manualCode,'') day_30,isNull(py.day_30,'') day30Code, isNull(acdm31.manualCode,'') day_31,isNull(py.day_31,'') day31Code\n"
                    + "FROM  PY_AttendanceEntry py\n"
                    + " left join salaryCodeDefinitionMast sdm on py.salaryMethod = sdm.code\n"
                    + " left join subGroup sge on sge.subCode= py.employeeCode\n"
                    + " left join subGroup sg on sg.subCode= py.departmentCode\n"
                    + " left join attendenceCodeDefinitionMast acdm1 on py.day_1 = acdm1.code\n"
                    + " left join attendenceCodeDefinitionMast acdm2 on py.day_2 = acdm2.code\n"
                    + " left join attendenceCodeDefinitionMast acdm3 on py.day_3 = acdm3.code\n"
                    + " left join attendenceCodeDefinitionMast acdm4 on py.day_4 = acdm4.code\n"
                    + " left join attendenceCodeDefinitionMast acdm5 on py.day_5 = acdm5.code\n"
                    + " left join attendenceCodeDefinitionMast acdm6 on py.day_6 = acdm6.code\n"
                    + " left join attendenceCodeDefinitionMast acdm7 on py.day_7 = acdm7.code\n"
                    + " left join attendenceCodeDefinitionMast acdm8 on py.day_8 = acdm8.code\n"
                    + " left join attendenceCodeDefinitionMast acdm9 on py.day_9 = acdm9.code\n"
                    + " left join attendenceCodeDefinitionMast acdm10 on py.day_10 = acdm10.code\n"
                    + " left join attendenceCodeDefinitionMast acdm11 on py.day_11 = acdm11.code\n"
                    + " left join attendenceCodeDefinitionMast acdm12 on py.day_12 = acdm12.code\n"
                    + " left join attendenceCodeDefinitionMast acdm13 on py.day_13 = acdm13.code\n"
                    + " left join attendenceCodeDefinitionMast acdm14 on py.day_14 = acdm14.code\n"
                    + " left join attendenceCodeDefinitionMast acdm15 on py.day_15 = acdm15.code\n"
                    + " left join attendenceCodeDefinitionMast acdm16 on py.day_16 = acdm16.code\n"
                    + " left join attendenceCodeDefinitionMast acdm17 on py.day_17 = acdm17.code\n"
                    + " left join attendenceCodeDefinitionMast acdm18 on py.day_18 = acdm18.code\n"
                    + " left join attendenceCodeDefinitionMast acdm19 on py.day_19 = acdm19.code\n"
                    + " left join attendenceCodeDefinitionMast acdm20 on py.day_20 = acdm20.code\n"
                    + " left join attendenceCodeDefinitionMast acdm21 on py.day_21 = acdm21.code\n"
                    + " left join attendenceCodeDefinitionMast acdm22 on py.day_22 = acdm22.code\n"
                    + " left join attendenceCodeDefinitionMast acdm23 on py.day_23 = acdm23.code\n"
                    + " left join attendenceCodeDefinitionMast acdm24 on py.day_24 = acdm24.code\n"
                    + " left join attendenceCodeDefinitionMast acdm25 on py.day_25 = acdm25.code\n"
                    + " left join attendenceCodeDefinitionMast acdm26 on py.day_26 = acdm26.code\n"
                    + " left join attendenceCodeDefinitionMast acdm27 on py.day_27 = acdm27.code\n"
                    + " left join attendenceCodeDefinitionMast acdm28 on py.day_28 = acdm28.code\n"
                    + " left join attendenceCodeDefinitionMast acdm29 on py.day_29 = acdm29.code\n"
                    + " left join attendenceCodeDefinitionMast acdm30 on py.day_30 = acdm30.code\n"
                    + " left join attendenceCodeDefinitionMast acdm31 on py.day_31 = acdm31.code where py.docId = :docId";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (!DmShared.xNull(docId).trim().equalsIgnoreCase("")) {
                psTemp.setString("docId", DmShared.iNull(docId).toString());
            }

            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                grdAttendanceEntry_Mdl = new GrdAttendanceEntry_Mdl();
                mrTemp.setSalaryMethodName(DmShared.xNull(rsTemp.getString("salaryMethod")));
                mrTemp.setDepartmentName(DmShared.xNull(rsTemp.getString("departmentName")));
                mrTemp.setSalaryMethodCode(DmShared.xNull(rsTemp.getString("salaryMethodCode")));
                mrTemp.setDepartmentCode(DmShared.xNull(rsTemp.getString("departmentCode")));

                grdAttendanceEntry_Mdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                grdAttendanceEntry_Mdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("employeeName")));
                grdAttendanceEntry_Mdl.setEmployeeName(DmShared.xNull(rsTemp.getString("employeeName")));
                grdAttendanceEntry_Mdl.setEmployeeCode(DmShared.xNull(rsTemp.getString("employeeCode")));
                grdAttendanceEntry_Mdl.setDay1(DmShared.xNull(rsTemp.getString("day_1")));
                grdAttendanceEntry_Mdl.setDay1Code(DmShared.xNull(rsTemp.getString("day1Code")));
                grdAttendanceEntry_Mdl.setDay2(DmShared.xNull(rsTemp.getString("day_2")));
                grdAttendanceEntry_Mdl.setDay2Code(DmShared.xNull(rsTemp.getString("day2Code")));
                grdAttendanceEntry_Mdl.setDay3(DmShared.xNull(rsTemp.getString("day_3")));
                grdAttendanceEntry_Mdl.setDay3Code(DmShared.xNull(rsTemp.getString("day3Code")));
                grdAttendanceEntry_Mdl.setDay4(DmShared.xNull(rsTemp.getString("day_4")));
                grdAttendanceEntry_Mdl.setDay4Code(DmShared.xNull(rsTemp.getString("day4Code")));
                grdAttendanceEntry_Mdl.setDay5(DmShared.xNull(rsTemp.getString("day_5")));
                grdAttendanceEntry_Mdl.setDay5Code(DmShared.xNull(rsTemp.getString("day5Code")));
                grdAttendanceEntry_Mdl.setDay6(DmShared.xNull(rsTemp.getString("day_6")));
                grdAttendanceEntry_Mdl.setDay6Code(DmShared.xNull(rsTemp.getString("day6Code")));
                grdAttendanceEntry_Mdl.setDay7(DmShared.xNull(rsTemp.getString("day_7")));
                grdAttendanceEntry_Mdl.setDay7Code(DmShared.xNull(rsTemp.getString("day7Code")));
                grdAttendanceEntry_Mdl.setDay8(DmShared.xNull(rsTemp.getString("day_8")));
                grdAttendanceEntry_Mdl.setDay8Code(DmShared.xNull(rsTemp.getString("day8Code")));
                grdAttendanceEntry_Mdl.setDay9(DmShared.xNull(rsTemp.getString("day_9")));
                grdAttendanceEntry_Mdl.setDay9Code(DmShared.xNull(rsTemp.getString("day9Code")));
                grdAttendanceEntry_Mdl.setDay10(DmShared.xNull(rsTemp.getString("day_10")));
                grdAttendanceEntry_Mdl.setDay10Code(DmShared.xNull(rsTemp.getString("day10Code")));
                grdAttendanceEntry_Mdl.setDay11(DmShared.xNull(rsTemp.getString("day_11")));
                grdAttendanceEntry_Mdl.setDay11Code(DmShared.xNull(rsTemp.getString("day11Code")));
                grdAttendanceEntry_Mdl.setDay12(DmShared.xNull(rsTemp.getString("day_12")));
                grdAttendanceEntry_Mdl.setDay12Code(DmShared.xNull(rsTemp.getString("day12Code")));
                grdAttendanceEntry_Mdl.setDay13(DmShared.xNull(rsTemp.getString("day_13")));
                grdAttendanceEntry_Mdl.setDay13Code(DmShared.xNull(rsTemp.getString("day13Code")));
                grdAttendanceEntry_Mdl.setDay14(DmShared.xNull(rsTemp.getString("day_14")));
                grdAttendanceEntry_Mdl.setDay14Code(DmShared.xNull(rsTemp.getString("day14Code")));
                grdAttendanceEntry_Mdl.setDay15(DmShared.xNull(rsTemp.getString("day_15")));
                grdAttendanceEntry_Mdl.setDay15Code(DmShared.xNull(rsTemp.getString("day15Code")));
                grdAttendanceEntry_Mdl.setDay16(DmShared.xNull(rsTemp.getString("day_16")));
                grdAttendanceEntry_Mdl.setDay16Code(DmShared.xNull(rsTemp.getString("day16Code")));
                grdAttendanceEntry_Mdl.setDay17(DmShared.xNull(rsTemp.getString("day_17")));
                grdAttendanceEntry_Mdl.setDay17Code(DmShared.xNull(rsTemp.getString("day17Code")));
                grdAttendanceEntry_Mdl.setDay18(DmShared.xNull(rsTemp.getString("day_18")));
                grdAttendanceEntry_Mdl.setDay18Code(DmShared.xNull(rsTemp.getString("day18Code")));
                grdAttendanceEntry_Mdl.setDay19(DmShared.xNull(rsTemp.getString("day_19")));
                grdAttendanceEntry_Mdl.setDay19Code(DmShared.xNull(rsTemp.getString("day19Code")));
                grdAttendanceEntry_Mdl.setDay20(DmShared.xNull(rsTemp.getString("day_20")));
                grdAttendanceEntry_Mdl.setDay20Code(DmShared.xNull(rsTemp.getString("day20Code")));
                grdAttendanceEntry_Mdl.setDay21(DmShared.xNull(rsTemp.getString("day_21")));
                grdAttendanceEntry_Mdl.setDay21Code(DmShared.xNull(rsTemp.getString("day21Code")));
                grdAttendanceEntry_Mdl.setDay22(DmShared.xNull(rsTemp.getString("day_22")));
                grdAttendanceEntry_Mdl.setDay22Code(DmShared.xNull(rsTemp.getString("day22Code")));
                grdAttendanceEntry_Mdl.setDay23(DmShared.xNull(rsTemp.getString("day_23")));
                grdAttendanceEntry_Mdl.setDay23Code(DmShared.xNull(rsTemp.getString("day23Code")));
                grdAttendanceEntry_Mdl.setDay24(DmShared.xNull(rsTemp.getString("day_24")));
                grdAttendanceEntry_Mdl.setDay24Code(DmShared.xNull(rsTemp.getString("day24Code")));
                grdAttendanceEntry_Mdl.setDay25(DmShared.xNull(rsTemp.getString("day_25")));
                grdAttendanceEntry_Mdl.setDay25Code(DmShared.xNull(rsTemp.getString("day25Code")));
                grdAttendanceEntry_Mdl.setDay26(DmShared.xNull(rsTemp.getString("day_26")));
                grdAttendanceEntry_Mdl.setDay26Code(DmShared.xNull(rsTemp.getString("day26Code")));
                grdAttendanceEntry_Mdl.setDay27(DmShared.xNull(rsTemp.getString("day_27")));
                grdAttendanceEntry_Mdl.setDay27Code(DmShared.xNull(rsTemp.getString("day27Code")));
                grdAttendanceEntry_Mdl.setDay28(DmShared.xNull(rsTemp.getString("day_28")));
                grdAttendanceEntry_Mdl.setDay28Code(DmShared.xNull(rsTemp.getString("day28Code")));
                grdAttendanceEntry_Mdl.setDay29(DmShared.xNull(rsTemp.getString("day_29")));
                grdAttendanceEntry_Mdl.setDay29Code(DmShared.xNull(rsTemp.getString("day29Code")));
                grdAttendanceEntry_Mdl.setDay30(DmShared.xNull(rsTemp.getString("day_30")));
                grdAttendanceEntry_Mdl.setDay30Code(DmShared.xNull(rsTemp.getString("day30Code")));
                grdAttendanceEntry_Mdl.setDay31(DmShared.xNull(rsTemp.getString("day_31")));
                grdAttendanceEntry_Mdl.setDay31Code(DmShared.xNull(rsTemp.getString("day31Code")));

                grdAttendanceEntryList.add(grdAttendanceEntry_Mdl);
            }
            rsTemp.close();
            psTemp.close();

        } catch (Exception e) {
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
        return grdAttendanceEntryList;
    }

    @Override
    public DmResult<AttendanceEntry_Mdl> getMoveRec(String strKey) {
        DmResult<AttendanceEntry_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        AttendanceEntry_Mdl mrTemp = new AttendanceEntry_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            mrTemp.getAttendanceMdlList().clear();
            if (!DmShared.xNull(strKey).equalsIgnoreCase("")) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(strKey).toString());
                mrTemp.getAttendanceMdlList().addAll(getAttendanceMdlList(strKey, mrTemp, cnTemp));
            }

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

            sqlQuery = "delete from dailyAttendance_Final where docId in(select docid from Employee_Attendance_Leave_Detail_Final  where mainDocID=:mainDocID )";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("mainDocID", strKey);
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "delete from Employee_Attendance_Leave_Detail_Final where mainDocId =:mainDocID ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("mainDocID", strKey);
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "delete from PY_AttendanceEntry where docId = :docId";
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
    public DmResult<String> saveRecord(AttendanceEntry_Mdl mrTemp, String voucherPrefix) {

        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery, filterNature = "", acGroupConsCode = "", acLocationCode = "", acDepartmentCode = "";
        ResultSet rsTemp = null;
        int vSno = 0;
        String v_Type = "";
        String condition = "";

        String totalDed = "";
        try {

            AttendanceEntry_Vld.chkSave(mrTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            v_Type = DmServer.getVType("ATTEN", cnTemp);

            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(v_Type), voucherPrefix, cnTemp, this));
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            if (!DmShared.xNull(mrTemp.getDepartmentCode()).equalsIgnoreCase("")) {
                condition += " and py.departmentCode = :departmentCode";
            } else {
                condition += " and isNull(py.departmentCode,'') = ''";
            }
//    check Duplicate values for Salary Method and Department Name
            sqlQuery = "select py.salaryMethod,py.departmentCode from PY_AttendanceEntry py\n"
                    + "where py.salaryMethod = :salaryMethodCode\n"
                    + "and py.docId <> :docId" + condition;
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("salaryMethodCode", DmShared.xNull(mrTemp.getSalaryMethodCode()));
            psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            if (!DmShared.xNull(mrTemp.getDepartmentCode()).equalsIgnoreCase("")) {
                psTemp.setString("departmentCode", DmShared.xNull(mrTemp.getDepartmentCode()));
            }
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                throw new DmException(500, "Record already exists.");
            }

            rsTemp.close();
            psTemp.close();

            if (DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue() > 0) {

                /*================================< Delete Record from Attendance Entry   >======================================================*/
                sqlQuery = "Delete From PY_AttendanceEntry Where docId= :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.executeUpdate();
                psTemp.close();
                blnAdd = true;
            }

            // if blnAdd is true, this means new record should be inserted into database.
            if (blnAdd) {
                if (mrTemp.getAttendanceMdlList() != null && DmShared.iNull(mrTemp.getAttendanceMdlList().size()).intValue() > 0) {
                    for (GrdAttendanceEntry_Mdl attendanceEntryMdl : mrTemp.getAttendanceMdlList()) {

                        sqlQuery = "insert into PY_AttendanceEntry (docId,vSno,v_Type,salaryMethod, departmentCode, employeeCode, day_1, day_2,\n"
                                + "day_3, day_4, day_5, day_6, day_7, day_8, day_9, day_10, day_11, day_12, day_13, day_14,\n"
                                + "day_15, day_16, day_17, day_18, day_19, day_20, day_21, day_22, day_23, day_24, day_25,\n"
                                + "day_26, day_27, day_28, day_29, day_30, day_31)\n"
                                + "values(:docId,:vSno,:v_Type,:salaryMethod, :departmentCode, :employeeCode, :day_1, :day_2,\n"
                                + ":day_3, :day_4, :day_5, :day_6, :day_7, :day_8, :day_9, :day_10, :day_11, :day_12, :day_13, :day_14,\n"
                                + ":day_15, :day_16, :day_17, :day_18, :day_19, :day_20, :day_21, :day_22, :day_23, :day_24, :day_25,\n"
                                + ":day_26, :day_27, :day_28, :day_29, :day_30, :day_31)";

                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                        psTemp.setInt("vSno", ++vSno);
                        psTemp.setString("v_Type", DmShared.xNull(v_Type));
                        psTemp.setString("salaryMethod", DmShared.foriegnKey(mrTemp.getSalaryMethodCode()));
                        psTemp.setString("departmentCode", DmShared.foriegnKey(mrTemp.getDepartmentCode()));
                        psTemp.setString("employeeCode", DmShared.foriegnKey(attendanceEntryMdl.getEmployeeCode()));
                        psTemp.setString("day_1", DmShared.foriegnKey(attendanceEntryMdl.getDay1Code()));
                        psTemp.setString("day_2", DmShared.foriegnKey(attendanceEntryMdl.getDay2Code()));
                        psTemp.setString("day_3", DmShared.foriegnKey(attendanceEntryMdl.getDay3Code()));
                        psTemp.setString("day_4", DmShared.foriegnKey(attendanceEntryMdl.getDay4Code()));
                        psTemp.setString("day_5", DmShared.foriegnKey(attendanceEntryMdl.getDay5Code()));
                        psTemp.setString("day_6", DmShared.foriegnKey(attendanceEntryMdl.getDay6Code()));
                        psTemp.setString("day_7", DmShared.foriegnKey(attendanceEntryMdl.getDay7Code()));
                        psTemp.setString("day_8", DmShared.foriegnKey(attendanceEntryMdl.getDay8Code()));
                        psTemp.setString("day_9", DmShared.foriegnKey(attendanceEntryMdl.getDay9Code()));
                        psTemp.setString("day_10", DmShared.foriegnKey(attendanceEntryMdl.getDay10Code()));
                        psTemp.setString("day_11", DmShared.foriegnKey(attendanceEntryMdl.getDay11Code()));
                        psTemp.setString("day_12", DmShared.foriegnKey(attendanceEntryMdl.getDay12Code()));
                        psTemp.setString("day_13", DmShared.foriegnKey(attendanceEntryMdl.getDay13Code()));
                        psTemp.setString("day_14", DmShared.foriegnKey(attendanceEntryMdl.getDay14Code()));
                        psTemp.setString("day_15", DmShared.foriegnKey(attendanceEntryMdl.getDay15Code()));
                        psTemp.setString("day_16", DmShared.foriegnKey(attendanceEntryMdl.getDay16Code()));
                        psTemp.setString("day_17", DmShared.foriegnKey(attendanceEntryMdl.getDay17Code()));
                        psTemp.setString("day_18", DmShared.foriegnKey(attendanceEntryMdl.getDay18Code()));
                        psTemp.setString("day_19", DmShared.foriegnKey(attendanceEntryMdl.getDay19Code()));
                        psTemp.setString("day_20", DmShared.foriegnKey(attendanceEntryMdl.getDay20Code()));
                        psTemp.setString("day_21", DmShared.foriegnKey(attendanceEntryMdl.getDay21Code()));
                        psTemp.setString("day_22", DmShared.foriegnKey(attendanceEntryMdl.getDay22Code()));
                        psTemp.setString("day_23", DmShared.foriegnKey(attendanceEntryMdl.getDay23Code()));
                        psTemp.setString("day_24", DmShared.foriegnKey(attendanceEntryMdl.getDay24Code()));
                        psTemp.setString("day_25", DmShared.foriegnKey(attendanceEntryMdl.getDay25Code()));
                        psTemp.setString("day_26", DmShared.foriegnKey(attendanceEntryMdl.getDay26Code()));
                        psTemp.setString("day_27", DmShared.foriegnKey(attendanceEntryMdl.getDay27Code()));
                        psTemp.setString("day_28", DmShared.foriegnKey(attendanceEntryMdl.getDay28Code()));
                        psTemp.setString("day_29", DmShared.foriegnKey(attendanceEntryMdl.getDay29Code()));
                        psTemp.setString("day_30", DmShared.foriegnKey(attendanceEntryMdl.getDay30Code()));
                        psTemp.setString("day_31", DmShared.foriegnKey(attendanceEntryMdl.getDay31Code()));

                        psTemp.executeUpdate();
                        psTemp.close();
                        /**
                         * ****************************Entry of Employee Wise
                         * Day wise
                         * records****************************************
                         */
                        if (!DmShared.xNull(attendanceEntryMdl.getDay1Code()).trim().equalsIgnoreCase("")) {
                            saveEmployeeAttendanceDet(attendanceEntryMdl, attendanceEntryMdl.getDay1(), attendanceEntryMdl.getOf_Ho_Lv1(), attendanceEntryMdl.getDay1Code(), attendanceEntryMdl.getFromDate1(), attendanceEntryMdl.getFromTime1(), attendanceEntryMdl.getToTime1(), attendanceEntryMdl.getPlusMinus1(), attendanceEntryMdl.getDays1(), attendanceEntryMdl.getType_Leave_Attendance1(), attendanceEntryMdl.getNatureCode1(), attendanceEntryMdl.getPreDocID1(), mrTemp.getSharedKeyFieldCode(), cnTemp);
                        }
                        if (!DmShared.xNull(attendanceEntryMdl.getDay2Code()).trim().equalsIgnoreCase("")) {
                            saveEmployeeAttendanceDet(attendanceEntryMdl, attendanceEntryMdl.getDay2(), attendanceEntryMdl.getOf_Ho_Lv2(), attendanceEntryMdl.getDay2Code(), attendanceEntryMdl.getFromDate2(), attendanceEntryMdl.getFromTime2(), attendanceEntryMdl.getToTime2(), attendanceEntryMdl.getPlusMinus2(), attendanceEntryMdl.getDays2(), attendanceEntryMdl.getType_Leave_Attendance2(), attendanceEntryMdl.getNatureCode2(), attendanceEntryMdl.getPreDocID2(), mrTemp.getSharedKeyFieldCode(), cnTemp);
                        }
                        if (!DmShared.xNull(attendanceEntryMdl.getDay3Code()).trim().equalsIgnoreCase("")) {
                            saveEmployeeAttendanceDet(attendanceEntryMdl, attendanceEntryMdl.getDay3(), attendanceEntryMdl.getOf_Ho_Lv3(), attendanceEntryMdl.getDay3Code(), attendanceEntryMdl.getFromDate3(), attendanceEntryMdl.getFromTime3(), attendanceEntryMdl.getToTime3(), attendanceEntryMdl.getPlusMinus3(), attendanceEntryMdl.getDays3(), attendanceEntryMdl.getType_Leave_Attendance3(), attendanceEntryMdl.getNatureCode3(), attendanceEntryMdl.getPreDocID3(), mrTemp.getSharedKeyFieldCode(), cnTemp);
                        }
                        if (!DmShared.xNull(attendanceEntryMdl.getDay4Code()).trim().equalsIgnoreCase("")) {
                            saveEmployeeAttendanceDet(attendanceEntryMdl, attendanceEntryMdl.getDay4(), attendanceEntryMdl.getOf_Ho_Lv4(), attendanceEntryMdl.getDay4Code(), attendanceEntryMdl.getFromDate4(), attendanceEntryMdl.getFromTime4(), attendanceEntryMdl.getToTime4(), attendanceEntryMdl.getPlusMinus4(), attendanceEntryMdl.getDays4(), attendanceEntryMdl.getType_Leave_Attendance4(), attendanceEntryMdl.getNatureCode4(), attendanceEntryMdl.getPreDocID4(), mrTemp.getSharedKeyFieldCode(), cnTemp);
                        }
                        if (!DmShared.xNull(attendanceEntryMdl.getDay5Code()).trim().equalsIgnoreCase("")) {
                            saveEmployeeAttendanceDet(attendanceEntryMdl, attendanceEntryMdl.getDay5(), attendanceEntryMdl.getOf_Ho_Lv5(), attendanceEntryMdl.getDay5Code(), attendanceEntryMdl.getFromDate5(), attendanceEntryMdl.getFromTime5(), attendanceEntryMdl.getToTime5(), attendanceEntryMdl.getPlusMinus5(), attendanceEntryMdl.getDays5(), attendanceEntryMdl.getType_Leave_Attendance5(), attendanceEntryMdl.getNatureCode5(), attendanceEntryMdl.getPreDocID5(), mrTemp.getSharedKeyFieldCode(), cnTemp);
                        }
                        if (!DmShared.xNull(attendanceEntryMdl.getDay6Code()).trim().equalsIgnoreCase("")) {
                            saveEmployeeAttendanceDet(attendanceEntryMdl, attendanceEntryMdl.getDay6(), attendanceEntryMdl.getOf_Ho_Lv6(), attendanceEntryMdl.getDay6Code(), attendanceEntryMdl.getFromDate6(), attendanceEntryMdl.getFromTime6(), attendanceEntryMdl.getToTime6(), attendanceEntryMdl.getPlusMinus6(), attendanceEntryMdl.getDays6(), attendanceEntryMdl.getType_Leave_Attendance6(), attendanceEntryMdl.getNatureCode6(), attendanceEntryMdl.getPreDocID6(), mrTemp.getSharedKeyFieldCode(), cnTemp);
                        }
                        if (!DmShared.xNull(attendanceEntryMdl.getDay7Code()).trim().equalsIgnoreCase("")) {
                            saveEmployeeAttendanceDet(attendanceEntryMdl, attendanceEntryMdl.getDay7(), attendanceEntryMdl.getOf_Ho_Lv7(), attendanceEntryMdl.getDay7Code(), attendanceEntryMdl.getFromDate7(), attendanceEntryMdl.getFromTime7(), attendanceEntryMdl.getToTime7(), attendanceEntryMdl.getPlusMinus7(), attendanceEntryMdl.getDays7(), attendanceEntryMdl.getType_Leave_Attendance7(), attendanceEntryMdl.getNatureCode7(), attendanceEntryMdl.getPreDocID7(), mrTemp.getSharedKeyFieldCode(), cnTemp);
                        }
                        if (!DmShared.xNull(attendanceEntryMdl.getDay8Code()).trim().equalsIgnoreCase("")) {
                            saveEmployeeAttendanceDet(attendanceEntryMdl, attendanceEntryMdl.getDay8(), attendanceEntryMdl.getOf_Ho_Lv8(), attendanceEntryMdl.getDay8Code(), attendanceEntryMdl.getFromDate8(), attendanceEntryMdl.getFromTime8(), attendanceEntryMdl.getToTime8(), attendanceEntryMdl.getPlusMinus8(), attendanceEntryMdl.getDays8(), attendanceEntryMdl.getType_Leave_Attendance8(), attendanceEntryMdl.getNatureCode8(), attendanceEntryMdl.getPreDocID8(), mrTemp.getSharedKeyFieldCode(), cnTemp);
                        }
                        if (!DmShared.xNull(attendanceEntryMdl.getDay9Code()).trim().equalsIgnoreCase("")) {
                            saveEmployeeAttendanceDet(attendanceEntryMdl, attendanceEntryMdl.getDay9(), attendanceEntryMdl.getOf_Ho_Lv9(), attendanceEntryMdl.getDay9Code(), attendanceEntryMdl.getFromDate9(), attendanceEntryMdl.getFromTime9(), attendanceEntryMdl.getToTime9(), attendanceEntryMdl.getPlusMinus9(), attendanceEntryMdl.getDays9(), attendanceEntryMdl.getType_Leave_Attendance9(), attendanceEntryMdl.getNatureCode9(), attendanceEntryMdl.getPreDocID9(), mrTemp.getSharedKeyFieldCode(), cnTemp);
                        }
                        if (!DmShared.xNull(attendanceEntryMdl.getDay10Code()).trim().equalsIgnoreCase("")) {
                            saveEmployeeAttendanceDet(attendanceEntryMdl, attendanceEntryMdl.getDay10(), attendanceEntryMdl.getOf_Ho_Lv10(), attendanceEntryMdl.getDay10Code(), attendanceEntryMdl.getFromDate10(), attendanceEntryMdl.getFromTime10(), attendanceEntryMdl.getToTime10(), attendanceEntryMdl.getPlusMinus10(), attendanceEntryMdl.getDays10(), attendanceEntryMdl.getType_Leave_Attendance10(), attendanceEntryMdl.getNatureCode10(), attendanceEntryMdl.getPreDocID10(), mrTemp.getSharedKeyFieldCode(), cnTemp);
                        }
                        if (!DmShared.xNull(attendanceEntryMdl.getDay11Code()).trim().equalsIgnoreCase("")) {
                            saveEmployeeAttendanceDet(attendanceEntryMdl, attendanceEntryMdl.getDay11(), attendanceEntryMdl.getOf_Ho_Lv11(), attendanceEntryMdl.getDay11Code(), attendanceEntryMdl.getFromDate11(), attendanceEntryMdl.getFromTime11(), attendanceEntryMdl.getToTime11(), attendanceEntryMdl.getPlusMinus11(), attendanceEntryMdl.getDays11(), attendanceEntryMdl.getType_Leave_Attendance11(), attendanceEntryMdl.getNatureCode11(), attendanceEntryMdl.getPreDocID11(), mrTemp.getSharedKeyFieldCode(), cnTemp);
                        }
                        if (!DmShared.xNull(attendanceEntryMdl.getDay12Code()).trim().equalsIgnoreCase("")) {
                            saveEmployeeAttendanceDet(attendanceEntryMdl, attendanceEntryMdl.getDay12(), attendanceEntryMdl.getOf_Ho_Lv12(), attendanceEntryMdl.getDay12Code(), attendanceEntryMdl.getFromDate12(), attendanceEntryMdl.getFromTime12(), attendanceEntryMdl.getToTime12(), attendanceEntryMdl.getPlusMinus12(), attendanceEntryMdl.getDays12(), attendanceEntryMdl.getType_Leave_Attendance12(), attendanceEntryMdl.getNatureCode12(), attendanceEntryMdl.getPreDocID12(), mrTemp.getSharedKeyFieldCode(), cnTemp);
                        }
                        if (!DmShared.xNull(attendanceEntryMdl.getDay13Code()).trim().equalsIgnoreCase("")) {
                            saveEmployeeAttendanceDet(attendanceEntryMdl, attendanceEntryMdl.getDay13(), attendanceEntryMdl.getOf_Ho_Lv13(), attendanceEntryMdl.getDay13Code(), attendanceEntryMdl.getFromDate13(), attendanceEntryMdl.getFromTime13(), attendanceEntryMdl.getToTime13(), attendanceEntryMdl.getPlusMinus13(), attendanceEntryMdl.getDays13(), attendanceEntryMdl.getType_Leave_Attendance13(), attendanceEntryMdl.getNatureCode13(), attendanceEntryMdl.getPreDocID13(), mrTemp.getSharedKeyFieldCode(), cnTemp);
                        }
                        if (!DmShared.xNull(attendanceEntryMdl.getDay14Code()).trim().equalsIgnoreCase("")) {
                            saveEmployeeAttendanceDet(attendanceEntryMdl, attendanceEntryMdl.getDay14(), attendanceEntryMdl.getOf_Ho_Lv14(), attendanceEntryMdl.getDay14Code(), attendanceEntryMdl.getFromDate14(), attendanceEntryMdl.getFromTime14(), attendanceEntryMdl.getToTime14(), attendanceEntryMdl.getPlusMinus14(), attendanceEntryMdl.getDays14(), attendanceEntryMdl.getType_Leave_Attendance14(), attendanceEntryMdl.getNatureCode14(), attendanceEntryMdl.getPreDocID14(), mrTemp.getSharedKeyFieldCode(), cnTemp);
                        }
                        if (!DmShared.xNull(attendanceEntryMdl.getDay15Code()).trim().equalsIgnoreCase("")) {
                            saveEmployeeAttendanceDet(attendanceEntryMdl, attendanceEntryMdl.getDay15(), attendanceEntryMdl.getOf_Ho_Lv15(), attendanceEntryMdl.getDay15Code(), attendanceEntryMdl.getFromDate15(), attendanceEntryMdl.getFromTime15(), attendanceEntryMdl.getToTime15(), attendanceEntryMdl.getPlusMinus15(), attendanceEntryMdl.getDays15(), attendanceEntryMdl.getType_Leave_Attendance15(), attendanceEntryMdl.getNatureCode15(), attendanceEntryMdl.getPreDocID15(), mrTemp.getSharedKeyFieldCode(), cnTemp);
                        }
                        if (!DmShared.xNull(attendanceEntryMdl.getDay16Code()).trim().equalsIgnoreCase("")) {
                            saveEmployeeAttendanceDet(attendanceEntryMdl, attendanceEntryMdl.getDay16(), attendanceEntryMdl.getOf_Ho_Lv16(), attendanceEntryMdl.getDay16Code(), attendanceEntryMdl.getFromDate16(), attendanceEntryMdl.getFromTime16(), attendanceEntryMdl.getToTime16(), attendanceEntryMdl.getPlusMinus16(), attendanceEntryMdl.getDays16(), attendanceEntryMdl.getType_Leave_Attendance16(), attendanceEntryMdl.getNatureCode16(), attendanceEntryMdl.getPreDocID16(), mrTemp.getSharedKeyFieldCode(), cnTemp);
                        }
                        if (!DmShared.xNull(attendanceEntryMdl.getDay17Code()).trim().equalsIgnoreCase("")) {
                            saveEmployeeAttendanceDet(attendanceEntryMdl, attendanceEntryMdl.getDay17(), attendanceEntryMdl.getOf_Ho_Lv17(), attendanceEntryMdl.getDay17Code(), attendanceEntryMdl.getFromDate17(), attendanceEntryMdl.getFromTime17(), attendanceEntryMdl.getToTime17(), attendanceEntryMdl.getPlusMinus17(), attendanceEntryMdl.getDays17(), attendanceEntryMdl.getType_Leave_Attendance17(), attendanceEntryMdl.getNatureCode17(), attendanceEntryMdl.getPreDocID17(), mrTemp.getSharedKeyFieldCode(), cnTemp);
                        }
                        if (!DmShared.xNull(attendanceEntryMdl.getDay18Code()).trim().equalsIgnoreCase("")) {
                            saveEmployeeAttendanceDet(attendanceEntryMdl, attendanceEntryMdl.getDay18(), attendanceEntryMdl.getOf_Ho_Lv18(), attendanceEntryMdl.getDay18Code(), attendanceEntryMdl.getFromDate18(), attendanceEntryMdl.getFromTime18(), attendanceEntryMdl.getToTime18(), attendanceEntryMdl.getPlusMinus18(), attendanceEntryMdl.getDays18(), attendanceEntryMdl.getType_Leave_Attendance18(), attendanceEntryMdl.getNatureCode18(), attendanceEntryMdl.getPreDocID18(), mrTemp.getSharedKeyFieldCode(), cnTemp);
                        }
                        if (!DmShared.xNull(attendanceEntryMdl.getDay19Code()).trim().equalsIgnoreCase("")) {
                            saveEmployeeAttendanceDet(attendanceEntryMdl, attendanceEntryMdl.getDay19(), attendanceEntryMdl.getOf_Ho_Lv19(), attendanceEntryMdl.getDay19Code(), attendanceEntryMdl.getFromDate19(), attendanceEntryMdl.getFromTime19(), attendanceEntryMdl.getToTime19(), attendanceEntryMdl.getPlusMinus19(), attendanceEntryMdl.getDays19(), attendanceEntryMdl.getType_Leave_Attendance19(), attendanceEntryMdl.getNatureCode19(), attendanceEntryMdl.getPreDocID19(), mrTemp.getSharedKeyFieldCode(), cnTemp);
                        }
                        if (!DmShared.xNull(attendanceEntryMdl.getDay20Code()).trim().equalsIgnoreCase("")) {
                            saveEmployeeAttendanceDet(attendanceEntryMdl, attendanceEntryMdl.getDay20(), attendanceEntryMdl.getOf_Ho_Lv20(), attendanceEntryMdl.getDay20Code(), attendanceEntryMdl.getFromDate20(), attendanceEntryMdl.getFromTime20(), attendanceEntryMdl.getToTime20(), attendanceEntryMdl.getPlusMinus20(), attendanceEntryMdl.getDays20(), attendanceEntryMdl.getType_Leave_Attendance20(), attendanceEntryMdl.getNatureCode20(), attendanceEntryMdl.getPreDocID20(), mrTemp.getSharedKeyFieldCode(), cnTemp);
                        }
                        if (!DmShared.xNull(attendanceEntryMdl.getDay21Code()).trim().equalsIgnoreCase("")) {
                            saveEmployeeAttendanceDet(attendanceEntryMdl, attendanceEntryMdl.getDay21(), attendanceEntryMdl.getOf_Ho_Lv21(), attendanceEntryMdl.getDay21Code(), attendanceEntryMdl.getFromDate21(), attendanceEntryMdl.getFromTime21(), attendanceEntryMdl.getToTime21(), attendanceEntryMdl.getPlusMinus21(), attendanceEntryMdl.getDays21(), attendanceEntryMdl.getType_Leave_Attendance21(), attendanceEntryMdl.getNatureCode21(), attendanceEntryMdl.getPreDocID21(), mrTemp.getSharedKeyFieldCode(), cnTemp);
                        }
                        if (!DmShared.xNull(attendanceEntryMdl.getDay22Code()).trim().equalsIgnoreCase("")) {
                            saveEmployeeAttendanceDet(attendanceEntryMdl, attendanceEntryMdl.getDay22(), attendanceEntryMdl.getOf_Ho_Lv22(), attendanceEntryMdl.getDay22Code(), attendanceEntryMdl.getFromDate22(), attendanceEntryMdl.getFromTime22(), attendanceEntryMdl.getToTime22(), attendanceEntryMdl.getPlusMinus22(), attendanceEntryMdl.getDays22(), attendanceEntryMdl.getType_Leave_Attendance22(), attendanceEntryMdl.getNatureCode22(), attendanceEntryMdl.getPreDocID22(), mrTemp.getSharedKeyFieldCode(), cnTemp);
                        }
                        if (!DmShared.xNull(attendanceEntryMdl.getDay23Code()).trim().equalsIgnoreCase("")) {
                            saveEmployeeAttendanceDet(attendanceEntryMdl, attendanceEntryMdl.getDay23(), attendanceEntryMdl.getOf_Ho_Lv23(), attendanceEntryMdl.getDay23Code(), attendanceEntryMdl.getFromDate23(), attendanceEntryMdl.getFromTime23(), attendanceEntryMdl.getToTime23(), attendanceEntryMdl.getPlusMinus23(), attendanceEntryMdl.getDays23(), attendanceEntryMdl.getType_Leave_Attendance23(), attendanceEntryMdl.getNatureCode23(), attendanceEntryMdl.getPreDocID23(), mrTemp.getSharedKeyFieldCode(), cnTemp);
                        }
                        if (!DmShared.xNull(attendanceEntryMdl.getDay24Code()).trim().equalsIgnoreCase("")) {
                            saveEmployeeAttendanceDet(attendanceEntryMdl, attendanceEntryMdl.getDay24(), attendanceEntryMdl.getOf_Ho_Lv24(), attendanceEntryMdl.getDay24Code(), attendanceEntryMdl.getFromDate24(), attendanceEntryMdl.getFromTime24(), attendanceEntryMdl.getToTime24(), attendanceEntryMdl.getPlusMinus24(), attendanceEntryMdl.getDays24(), attendanceEntryMdl.getType_Leave_Attendance24(), attendanceEntryMdl.getNatureCode24(), attendanceEntryMdl.getPreDocID24(), mrTemp.getSharedKeyFieldCode(), cnTemp);
                        }
                        if (!DmShared.xNull(attendanceEntryMdl.getDay25Code()).trim().equalsIgnoreCase("")) {
                            saveEmployeeAttendanceDet(attendanceEntryMdl, attendanceEntryMdl.getDay25(), attendanceEntryMdl.getOf_Ho_Lv25(), attendanceEntryMdl.getDay25Code(), attendanceEntryMdl.getFromDate25(), attendanceEntryMdl.getFromTime25(), attendanceEntryMdl.getToTime25(), attendanceEntryMdl.getPlusMinus25(), attendanceEntryMdl.getDays25(), attendanceEntryMdl.getType_Leave_Attendance25(), attendanceEntryMdl.getNatureCode25(), attendanceEntryMdl.getPreDocID25(), mrTemp.getSharedKeyFieldCode(), cnTemp);
                        }
                        if (!DmShared.xNull(attendanceEntryMdl.getDay26Code()).trim().equalsIgnoreCase("")) {
                            saveEmployeeAttendanceDet(attendanceEntryMdl, attendanceEntryMdl.getDay26(), attendanceEntryMdl.getOf_Ho_Lv26(), attendanceEntryMdl.getDay26Code(), attendanceEntryMdl.getFromDate26(), attendanceEntryMdl.getFromTime26(), attendanceEntryMdl.getToTime26(), attendanceEntryMdl.getPlusMinus26(), attendanceEntryMdl.getDays26(), attendanceEntryMdl.getType_Leave_Attendance26(), attendanceEntryMdl.getNatureCode26(), attendanceEntryMdl.getPreDocID26(), mrTemp.getSharedKeyFieldCode(), cnTemp);
                        }
                        if (!DmShared.xNull(attendanceEntryMdl.getDay27Code()).trim().equalsIgnoreCase("")) {
                            saveEmployeeAttendanceDet(attendanceEntryMdl, attendanceEntryMdl.getDay27(), attendanceEntryMdl.getOf_Ho_Lv27(), attendanceEntryMdl.getDay27Code(), attendanceEntryMdl.getFromDate27(), attendanceEntryMdl.getFromTime27(), attendanceEntryMdl.getToTime27(), attendanceEntryMdl.getPlusMinus27(), attendanceEntryMdl.getDays27(), attendanceEntryMdl.getType_Leave_Attendance27(), attendanceEntryMdl.getNatureCode27(), attendanceEntryMdl.getPreDocID27(), mrTemp.getSharedKeyFieldCode(), cnTemp);
                        }
                        if (!DmShared.xNull(attendanceEntryMdl.getDay28Code()).trim().equalsIgnoreCase("")) {
                            saveEmployeeAttendanceDet(attendanceEntryMdl, attendanceEntryMdl.getDay28(), attendanceEntryMdl.getOf_Ho_Lv28(), attendanceEntryMdl.getDay28Code(), attendanceEntryMdl.getFromDate28(), attendanceEntryMdl.getFromTime28(), attendanceEntryMdl.getToTime28(), attendanceEntryMdl.getPlusMinus28(), attendanceEntryMdl.getDays28(), attendanceEntryMdl.getType_Leave_Attendance28(), attendanceEntryMdl.getNatureCode28(), attendanceEntryMdl.getPreDocID28(), mrTemp.getSharedKeyFieldCode(), cnTemp);
                        }
                        if (!DmShared.xNull(attendanceEntryMdl.getDay29Code()).trim().equalsIgnoreCase("")) {
                            saveEmployeeAttendanceDet(attendanceEntryMdl, attendanceEntryMdl.getDay29(), attendanceEntryMdl.getOf_Ho_Lv29(), attendanceEntryMdl.getDay29Code(), attendanceEntryMdl.getFromDate29(), attendanceEntryMdl.getFromTime29(), attendanceEntryMdl.getToTime29(), attendanceEntryMdl.getPlusMinus29(), attendanceEntryMdl.getDays29(), attendanceEntryMdl.getType_Leave_Attendance29(), attendanceEntryMdl.getNatureCode29(), attendanceEntryMdl.getPreDocID29(), mrTemp.getSharedKeyFieldCode(), cnTemp);
                        }
                        if (!DmShared.xNull(attendanceEntryMdl.getDay30Code()).trim().equalsIgnoreCase("")) {
                            saveEmployeeAttendanceDet(attendanceEntryMdl, attendanceEntryMdl.getDay30(), attendanceEntryMdl.getOf_Ho_Lv30(), attendanceEntryMdl.getDay30Code(), attendanceEntryMdl.getFromDate30(), attendanceEntryMdl.getFromTime30(), attendanceEntryMdl.getToTime30(), attendanceEntryMdl.getPlusMinus30(), attendanceEntryMdl.getDays30(), attendanceEntryMdl.getType_Leave_Attendance30(), attendanceEntryMdl.getNatureCode30(), attendanceEntryMdl.getPreDocID30(), mrTemp.getSharedKeyFieldCode(), cnTemp);
                        }
                        if (!DmShared.xNull(attendanceEntryMdl.getDay31Code()).trim().equalsIgnoreCase("")) {
                            saveEmployeeAttendanceDet(attendanceEntryMdl, attendanceEntryMdl.getDay31(), attendanceEntryMdl.getOf_Ho_Lv31(), attendanceEntryMdl.getDay31Code(), attendanceEntryMdl.getFromDate31(), attendanceEntryMdl.getFromTime31(), attendanceEntryMdl.getToTime31(), attendanceEntryMdl.getPlusMinus31(), attendanceEntryMdl.getDays31(), attendanceEntryMdl.getType_Leave_Attendance31(), attendanceEntryMdl.getNatureCode31(), attendanceEntryMdl.getPreDocID31(), mrTemp.getSharedKeyFieldCode(), cnTemp);
                        }
                    }
                }
            }

            // DmResult data = ....
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

            sqlQuery = "select max(py.docId) docId ,max(isNull(scdm.name,'')) salaryMethod,max(isNull(sg.name,'')) departmentName \n"
                    + "from PY_AttendanceEntry py\n"
                    + "left join salaryCodeDefinitionMast scdm on scdm.code = py.salaryMethod\n"
                    + "left join subGroup sg on sg.subCode = py.departmentCode\n"
                    + "group by py.docId";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                listNode.setSalaryMethod(DmShared.xNull(rsTemp.getString("salaryMethod")));
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
    public List<GrdAttendanceEntry_Mdl> getAttendance(AttendanceEntry_Mdl param) {

        List<GrdAttendanceEntry_Mdl> lstTemp = new ArrayList<>();
        List<Leave_Mdl> leaveMdlList = new ArrayList<>();
        GrdAttendanceEntry_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery, condition, join;
        String totalDays;
        String holiDays;
        String monthDays = "";
        String date = "";
        DmHelper helper2 = new DmHelper();

        String startDate = "", endDate = "";
        Calendar calendar = Calendar.getInstance();
        String salaryMethodCode;

        try {

            condition = "";

            //=================================================
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            // calculate number of days in a month
            sqlQuery = "SELECT fromDate,toDate from salaryCodeDefinitionMast where code=" + param.getSalaryMethodCode();

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();

            if (rsTemp.next()) {
                startDate = DmShared.dNull(rsTemp.getDate("fromDate"));
                endDate = DmShared.dNull(rsTemp.getDate("toDate"));
            }
            rsTemp.close();
            psTemp.close();

            salaryMethodCode = param.getSalaryMethodCode();

            if (!DmShared.xNull(param.getDepartmentCode()).equalsIgnoreCase("")) {
                condition += " and sg2.hrDepartmentCode =" + param.getDepartmentCode();
            }

            sqlQuery = " select main.employeeCode,dateName(day,main.fromDate) as dayNumber,main.fromDate,main.docid as detDocId,main.att_Lev_OT\n"
                    + " ,main.attendanceCode,acdm.manualCode\n"
                    + " ,case when (main.offDay_HoliDayCode='OFD' or main.offDay_HoliDayCode='HH') then main.offDay_HoliDayCode  when (acdm.manualcode='AA' or acdm.manualcode='PP') then acdm.manualcode when acdm.type_Leave_Attendance=2 then 'LV' WHEN acdm.manualcode='NA' THEN 'N/A' end as offDay_HoliDayCode\n"
                    + " ,main.holiDayCode,acdm.type_Leave_Attendance,acdm.plusMinus,acdm.natureCode ,sm.fromTime,sm.toTime,"
                    + " case when isNUll(sm.includesNextDay,0)=0 then main.fromDate else dateadd(d,1,main.fromDate) end as todate\n"
                    + " ,case when acdm.plusMinus =0 then -acdm.days else acdm.days end as [days],main.isImported\n"
                    + " ,sg.name as employeeName"
                    + " 	from \n"
                    + " 		(\n"
                    + " 		select a.employeeCode,a.fromDate,eald.docid,eald.att_Lev_OT,eald.isImported\n"
                    + " 		,case when (a.fromDate <isNull(a.doj,a.fromDate) or a.fromDate >isNull(a.lastDuty,a.fromDate)) then :notAppliedAttendanceCode else\n"
                    + " 		(case when eald.docId is null \n"
                    + " 		then(case when isNull(a.offDay_HoliDayCode,'')<>'OFD' and isNull(a.holiDayCode,'')<>'HH' \n"
                    + " 			then :defaultAttendanceCode when isNull(a.offDay_HoliDayCode,'')='OFD' \n"
                    + " 			then :offDayCode else :holiDayCode end \n"
                    + " 			) else (isNull(eald.attendanceCode,eald.leaveCode)) end) end as attendanceCode\n"
                    + " 		,a.offDay_HoliDayCode,a.holiDayCode\n"
                    + " 		/************************Main Recordset(Date wise Employee wise)*******************************/\n"
                    + " 			from (\n"
                    + " 				select sg.subCode as employeeCode,sg.name as employeeName,a.fromDate,a.dayNumber,sg2.offDay\n"
                    + " 				,case when a.dayNumber=sg2.offDay then 'OFD' when a.fromDate=hm.date then 'HH' else '' end as offDay_HoliDayCode\n"
                    + " 				,case when a.fromDate=hm.date then 'HH' else '' end as holiDayCode,sg2.doj,ple.lastDuty\n"
                    + " 					from subGroup sg \n"
                    + " 					left join subGroupNature sgn on sg.filterNature =sgn.code\n"
                    + " 					left join subgroup2 sg2 on sg.subCode=sg2.subCode\n"
                    + " 					left join pyLeavingEntry ple on sg.subCode=ple.employeeCode\n"
                    + " 					left join (\n"
                    + " 								SELECT  TOP (DATEDIFF(DAY, :startDate, :endDate) + 1)\n"
                    + " 								fromDate = DATEADD(DAY, ROW_NUMBER() OVER(ORDER BY a.code) - 1, :startDate)\n"
                    + " 								,datePart(dw,DATEADD(DAY, ROW_NUMBER() OVER(ORDER BY a.code) - 1, :startDate)) as daynumber\n"
                    + " 									FROM    hl7Mast a\n"
                    + " 							  ) a on 1=1\n"
                    + " 					left join holidayMast hm on hm.date=a.fromDate\n"
                    + " 					where sgn.manualCode = 'EM' " + condition + " \n"
                    + " 					) a\n"
                    + " 		/*********************************************************************************************/\n"
                    + " 			left join Employee_Attendance_Leave_Detail eald on a.employeeCode=eald.employeeCode \n"
                    + " 			and a.fromDate =eald.fromDate and eald.isStatus=:isStatus\n"
                    + " 		) main\n"
                    + " 		left join attendenceCodeDefinitionMast acdm on main.attendanceCode=acdm.code\n"
                    + " 		left join subGroup sg on main.employeeCode=sg.subCode\n"
                    + " 		left join pyEmployeeShiftDetail pesd on main.employeeCode=pesd.employeeCode and main.fromDate between pesd.fromDate and pesd.toDate\n"
                    + " 		left join shiftMast sm on pesd.shiftCode=sm.code\n"
                    + " 		order by employeeName,fromDate,employeeCode";

            sqlWithParam.setSQL(sqlQuery);

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("startDate", startDate);
            psTemp.setString("endDate", endDate);
            psTemp.setString("defaultAttendanceCode", byDefaultAttendanceCode);
            psTemp.setString("holiDayCode", holidayCode);
            psTemp.setString("offDayCode", offDayCode);
            psTemp.setString("isStatus", DmShared.xNull(String.valueOf(PREnum.LEAVE_STATUS.APPROVED.getId())));
            psTemp.setString("notAppliedAttendanceCode", notAppliedAttendanceCode);

            rsTemp = psTemp.executeQuery();
            listNode = new GrdAttendanceEntry_Mdl();
            String prevEmpCode = "";
            while (rsTemp.next()) {

                if (!prevEmpCode.equalsIgnoreCase((DmShared.xNull(rsTemp.getString("employeeCode"))))) {
                    if (!prevEmpCode.trim().equalsIgnoreCase("")) {
                        lstTemp.add(listNode);
                    }
                    listNode = new GrdAttendanceEntry_Mdl();
                    listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("employeeCode")));
                    listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("EmployeeName")));
                    listNode.setEmployeeName(DmShared.xNull(rsTemp.getString("EmployeeName")));
                    listNode.setEmployeeCode(DmShared.xNull(rsTemp.getString("employeeCode")));
                    prevEmpCode = DmShared.xNull(rsTemp.getString("employeeCode"));
                }

                switch (DmShared.iNull(rsTemp.getString("dayNumber")).intValue()) {
                    case 1:
                        listNode.setDay1(DmShared.xNull(rsTemp.getString("manualCode")));
                        listNode.setDay1Code(DmShared.xNull(rsTemp.getString("attendanceCode")));
                        listNode.setPreDocID1(DmShared.xNull(rsTemp.getString("detDocId")));
                        listNode.setFromDate1(DmShared.xNull(rsTemp.getString("fromDate")));
                        listNode.setType_Leave_Attendance1(DmShared.xNull(rsTemp.getString("type_Leave_Attendance")));
                        listNode.setPlusMinus1(DmShared.xNull(rsTemp.getString("plusMinus")));
                        listNode.setNatureCode1(DmShared.xNull(rsTemp.getString("natureCode")));
                        listNode.setFromTime1(DmShared.xNull(rsTemp.getString("fromTime")));
                        listNode.setToTime1(DmShared.xNull(rsTemp.getString("toTime")));
                        listNode.setDays1(DmShared.xNull(rsTemp.getString("plusMinus")).trim() == "0" ? "-" + DmShared.xNull(rsTemp.getString("days")) : DmShared.xNull(rsTemp.getString("days")));
                        listNode.setOf_Ho_Lv1(DmShared.xNull(rsTemp.getString("offDay_HoliDayCode")));
                        break;
                    case 2:
                        listNode.setDay2(DmShared.xNull(rsTemp.getString("manualCode")));
                        listNode.setDay2Code(DmShared.xNull(rsTemp.getString("attendanceCode")));
                        listNode.setPreDocID2(DmShared.xNull(rsTemp.getString("detDocId")));
                        listNode.setFromDate2(DmShared.xNull(rsTemp.getString("fromDate")));
                        listNode.setType_Leave_Attendance2(DmShared.xNull(rsTemp.getString("type_Leave_Attendance")));
                        listNode.setPlusMinus2(DmShared.xNull(rsTemp.getString("plusMinus")));
                        listNode.setNatureCode2(DmShared.xNull(rsTemp.getString("natureCode")));
                        listNode.setFromTime2(DmShared.xNull(rsTemp.getString("fromTime")));
                        listNode.setToTime2(DmShared.xNull(rsTemp.getString("toTime")));
                        listNode.setDays2(DmShared.xNull(rsTemp.getString("plusMinus")).trim() == "0" ? "-" + DmShared.xNull(rsTemp.getString("days")) : DmShared.xNull(rsTemp.getString("days")));
                        listNode.setOf_Ho_Lv2(DmShared.xNull(rsTemp.getString("offDay_HoliDayCode")));
                        break;
                    case 3:
                        listNode.setDay3(DmShared.xNull(rsTemp.getString("manualCode")));
                        listNode.setDay3Code(DmShared.xNull(rsTemp.getString("attendanceCode")));
                        listNode.setPreDocID3(DmShared.xNull(rsTemp.getString("detDocId")));
                        listNode.setFromDate3(DmShared.xNull(rsTemp.getString("fromDate")));
                        listNode.setType_Leave_Attendance3(DmShared.xNull(rsTemp.getString("type_Leave_Attendance")));
                        listNode.setPlusMinus3(DmShared.xNull(rsTemp.getString("plusMinus")));
                        listNode.setNatureCode3(DmShared.xNull(rsTemp.getString("natureCode")));
                        listNode.setFromTime3(DmShared.xNull(rsTemp.getString("fromTime")));
                        listNode.setToTime3(DmShared.xNull(rsTemp.getString("toTime")));
                        listNode.setDays3(DmShared.xNull(rsTemp.getString("plusMinus")).trim() == "0" ? "-" + DmShared.xNull(rsTemp.getString("days")) : DmShared.xNull(rsTemp.getString("days")));
                        listNode.setOf_Ho_Lv3(DmShared.xNull(rsTemp.getString("offDay_HoliDayCode")));
                        break;
                    case 4:
                        listNode.setDay4(DmShared.xNull(rsTemp.getString("manualCode")));
                        listNode.setDay4Code(DmShared.xNull(rsTemp.getString("attendanceCode")));
                        listNode.setPreDocID4(DmShared.xNull(rsTemp.getString("detDocId")));
                        listNode.setFromDate4(DmShared.xNull(rsTemp.getString("fromDate")));
                        listNode.setType_Leave_Attendance4(DmShared.xNull(rsTemp.getString("type_Leave_Attendance")));
                        listNode.setPlusMinus4(DmShared.xNull(rsTemp.getString("plusMinus")));
                        listNode.setNatureCode4(DmShared.xNull(rsTemp.getString("natureCode")));
                        listNode.setFromTime4(DmShared.xNull(rsTemp.getString("fromTime")));
                        listNode.setToTime4(DmShared.xNull(rsTemp.getString("toTime")));
                        listNode.setDays4(DmShared.xNull(rsTemp.getString("plusMinus")).trim() == "0" ? "-" + DmShared.xNull(rsTemp.getString("days")) : DmShared.xNull(rsTemp.getString("days")));
                        listNode.setOf_Ho_Lv4(DmShared.xNull(rsTemp.getString("offDay_HoliDayCode")));
                        break;
                    case 5:
                        listNode.setDay5(DmShared.xNull(rsTemp.getString("manualCode")));
                        listNode.setDay5Code(DmShared.xNull(rsTemp.getString("attendanceCode")));
                        listNode.setPreDocID5(DmShared.xNull(rsTemp.getString("detDocId")));
                        listNode.setFromDate5(DmShared.xNull(rsTemp.getString("fromDate")));
                        listNode.setType_Leave_Attendance5(DmShared.xNull(rsTemp.getString("type_Leave_Attendance")));
                        listNode.setPlusMinus5(DmShared.xNull(rsTemp.getString("plusMinus")));
                        listNode.setNatureCode5(DmShared.xNull(rsTemp.getString("natureCode")));
                        listNode.setFromTime5(DmShared.xNull(rsTemp.getString("fromTime")));
                        listNode.setToTime5(DmShared.xNull(rsTemp.getString("toTime")));
                        listNode.setDays5(DmShared.xNull(rsTemp.getString("plusMinus")).trim() == "0" ? "-" + DmShared.xNull(rsTemp.getString("days")) : DmShared.xNull(rsTemp.getString("days")));
                        listNode.setOf_Ho_Lv5(DmShared.xNull(rsTemp.getString("offDay_HoliDayCode")));
                        break;
                    case 6:
                        listNode.setDay6(DmShared.xNull(rsTemp.getString("manualCode")));
                        listNode.setDay6Code(DmShared.xNull(rsTemp.getString("attendanceCode")));
                        listNode.setPreDocID6(DmShared.xNull(rsTemp.getString("detDocId")));
                        listNode.setFromDate6(DmShared.xNull(rsTemp.getString("fromDate")));
                        listNode.setType_Leave_Attendance6(DmShared.xNull(rsTemp.getString("type_Leave_Attendance")));
                        listNode.setPlusMinus6(DmShared.xNull(rsTemp.getString("plusMinus")));
                        listNode.setNatureCode6(DmShared.xNull(rsTemp.getString("natureCode")));
                        listNode.setFromTime6(DmShared.xNull(rsTemp.getString("fromTime")));
                        listNode.setToTime6(DmShared.xNull(rsTemp.getString("toTime")));
                        listNode.setDays6(DmShared.xNull(rsTemp.getString("plusMinus")).trim() == "0" ? "-" + DmShared.xNull(rsTemp.getString("days")) : DmShared.xNull(rsTemp.getString("days")));
                        listNode.setOf_Ho_Lv6(DmShared.xNull(rsTemp.getString("offDay_HoliDayCode")));
                        break;
                    case 7:
                        listNode.setDay7(DmShared.xNull(rsTemp.getString("manualCode")));
                        listNode.setDay7Code(DmShared.xNull(rsTemp.getString("attendanceCode")));
                        listNode.setPreDocID7(DmShared.xNull(rsTemp.getString("detDocId")));
                        listNode.setFromDate7(DmShared.xNull(rsTemp.getString("fromDate")));
                        listNode.setType_Leave_Attendance7(DmShared.xNull(rsTemp.getString("type_Leave_Attendance")));
                        listNode.setPlusMinus7(DmShared.xNull(rsTemp.getString("plusMinus")));
                        listNode.setNatureCode7(DmShared.xNull(rsTemp.getString("natureCode")));
                        listNode.setFromTime7(DmShared.xNull(rsTemp.getString("fromTime")));
                        listNode.setToTime7(DmShared.xNull(rsTemp.getString("toTime")));
                        listNode.setDays7(DmShared.xNull(rsTemp.getString("plusMinus")).trim() == "0" ? "-" + DmShared.xNull(rsTemp.getString("days")) : DmShared.xNull(rsTemp.getString("days")));
                        listNode.setOf_Ho_Lv7(DmShared.xNull(rsTemp.getString("offDay_HoliDayCode")));
                        break;
                    case 8:
                        listNode.setDay8(DmShared.xNull(rsTemp.getString("manualCode")));
                        listNode.setDay8Code(DmShared.xNull(rsTemp.getString("attendanceCode")));
                        listNode.setPreDocID8(DmShared.xNull(rsTemp.getString("detDocId")));
                        listNode.setFromDate8(DmShared.xNull(rsTemp.getString("fromDate")));
                        listNode.setType_Leave_Attendance8(DmShared.xNull(rsTemp.getString("type_Leave_Attendance")));
                        listNode.setPlusMinus8(DmShared.xNull(rsTemp.getString("plusMinus")));
                        listNode.setNatureCode8(DmShared.xNull(rsTemp.getString("natureCode")));
                        listNode.setFromTime8(DmShared.xNull(rsTemp.getString("fromTime")));
                        listNode.setToTime8(DmShared.xNull(rsTemp.getString("toTime")));
                        listNode.setDays8(DmShared.xNull(rsTemp.getString("plusMinus")).trim() == "0" ? "-" + DmShared.xNull(rsTemp.getString("days")) : DmShared.xNull(rsTemp.getString("days")));
                        listNode.setOf_Ho_Lv8(DmShared.xNull(rsTemp.getString("offDay_HoliDayCode")));
                        break;
                    case 9:
                        listNode.setDay9(DmShared.xNull(rsTemp.getString("manualCode")));
                        listNode.setDay9Code(DmShared.xNull(rsTemp.getString("attendanceCode")));
                        listNode.setPreDocID9(DmShared.xNull(rsTemp.getString("detDocId")));
                        listNode.setFromDate9(DmShared.xNull(rsTemp.getString("fromDate")));
                        listNode.setType_Leave_Attendance9(DmShared.xNull(rsTemp.getString("type_Leave_Attendance")));
                        listNode.setPlusMinus9(DmShared.xNull(rsTemp.getString("plusMinus")));
                        listNode.setNatureCode9(DmShared.xNull(rsTemp.getString("natureCode")));
                        listNode.setFromTime9(DmShared.xNull(rsTemp.getString("fromTime")));
                        listNode.setToTime9(DmShared.xNull(rsTemp.getString("toTime")));
                        listNode.setDays9(DmShared.xNull(rsTemp.getString("plusMinus")).trim() == "0" ? "-" + DmShared.xNull(rsTemp.getString("days")) : DmShared.xNull(rsTemp.getString("days")));
                        listNode.setOf_Ho_Lv9(DmShared.xNull(rsTemp.getString("offDay_HoliDayCode")));
                        break;
                    case 10:
                        listNode.setDay10(DmShared.xNull(rsTemp.getString("manualCode")));
                        listNode.setDay10Code(DmShared.xNull(rsTemp.getString("attendanceCode")));
                        listNode.setPreDocID10(DmShared.xNull(rsTemp.getString("detDocId")));
                        listNode.setFromDate10(DmShared.xNull(rsTemp.getString("fromDate")));
                        listNode.setType_Leave_Attendance10(DmShared.xNull(rsTemp.getString("type_Leave_Attendance")));
                        listNode.setPlusMinus10(DmShared.xNull(rsTemp.getString("plusMinus")));
                        listNode.setNatureCode10(DmShared.xNull(rsTemp.getString("natureCode")));
                        listNode.setFromTime10(DmShared.xNull(rsTemp.getString("fromTime")));
                        listNode.setToTime10(DmShared.xNull(rsTemp.getString("toTime")));
                        listNode.setDays10(DmShared.xNull(rsTemp.getString("plusMinus")).trim() == "0" ? "-" + DmShared.xNull(rsTemp.getString("days")) : DmShared.xNull(rsTemp.getString("days")));
                        listNode.setOf_Ho_Lv10(DmShared.xNull(rsTemp.getString("offDay_HoliDayCode")));
                        break;
                    case 11:
                        listNode.setDay11(DmShared.xNull(rsTemp.getString("manualCode")));
                        listNode.setDay11Code(DmShared.xNull(rsTemp.getString("attendanceCode")));
                        listNode.setPreDocID11(DmShared.xNull(rsTemp.getString("detDocId")));
                        listNode.setFromDate11(DmShared.xNull(rsTemp.getString("fromDate")));
                        listNode.setType_Leave_Attendance11(DmShared.xNull(rsTemp.getString("type_Leave_Attendance")));
                        listNode.setPlusMinus11(DmShared.xNull(rsTemp.getString("plusMinus")));
                        listNode.setNatureCode11(DmShared.xNull(rsTemp.getString("natureCode")));
                        listNode.setFromTime11(DmShared.xNull(rsTemp.getString("fromTime")));
                        listNode.setToTime11(DmShared.xNull(rsTemp.getString("toTime")));
                        listNode.setDays11(DmShared.xNull(rsTemp.getString("plusMinus")).trim() == "0" ? "-" + DmShared.xNull(rsTemp.getString("days")) : DmShared.xNull(rsTemp.getString("days")));
                        listNode.setOf_Ho_Lv11(DmShared.xNull(rsTemp.getString("offDay_HoliDayCode")));
                        break;
                    case 12:
                        listNode.setDay12(DmShared.xNull(rsTemp.getString("manualCode")));
                        listNode.setDay12Code(DmShared.xNull(rsTemp.getString("attendanceCode")));
                        listNode.setPreDocID12(DmShared.xNull(rsTemp.getString("detDocId")));
                        listNode.setFromDate12(DmShared.xNull(rsTemp.getString("fromDate")));
                        listNode.setType_Leave_Attendance12(DmShared.xNull(rsTemp.getString("type_Leave_Attendance")));
                        listNode.setPlusMinus12(DmShared.xNull(rsTemp.getString("plusMinus")));
                        listNode.setNatureCode12(DmShared.xNull(rsTemp.getString("natureCode")));
                        listNode.setFromTime12(DmShared.xNull(rsTemp.getString("fromTime")));
                        listNode.setToTime12(DmShared.xNull(rsTemp.getString("toTime")));
                        listNode.setDays12(DmShared.xNull(rsTemp.getString("plusMinus")).trim() == "0" ? "-" + DmShared.xNull(rsTemp.getString("days")) : DmShared.xNull(rsTemp.getString("days")));
                        listNode.setOf_Ho_Lv12(DmShared.xNull(rsTemp.getString("offDay_HoliDayCode")));
                        break;
                    case 13:
                        listNode.setDay13(DmShared.xNull(rsTemp.getString("manualCode")));
                        listNode.setDay13Code(DmShared.xNull(rsTemp.getString("attendanceCode")));
                        listNode.setPreDocID13(DmShared.xNull(rsTemp.getString("detDocId")));
                        listNode.setFromDate13(DmShared.xNull(rsTemp.getString("fromDate")));
                        listNode.setType_Leave_Attendance13(DmShared.xNull(rsTemp.getString("type_Leave_Attendance")));
                        listNode.setPlusMinus13(DmShared.xNull(rsTemp.getString("plusMinus")));
                        listNode.setNatureCode13(DmShared.xNull(rsTemp.getString("natureCode")));
                        listNode.setFromTime13(DmShared.xNull(rsTemp.getString("fromTime")));
                        listNode.setToTime13(DmShared.xNull(rsTemp.getString("toTime")));
                        listNode.setDays13(DmShared.xNull(rsTemp.getString("plusMinus")).trim() == "0" ? "-" + DmShared.xNull(rsTemp.getString("days")) : DmShared.xNull(rsTemp.getString("days")));
                        listNode.setOf_Ho_Lv13(DmShared.xNull(rsTemp.getString("offDay_HoliDayCode")));
                        break;
                    case 14:
                        listNode.setDay14(DmShared.xNull(rsTemp.getString("manualCode")));
                        listNode.setDay14Code(DmShared.xNull(rsTemp.getString("attendanceCode")));
                        listNode.setPreDocID14(DmShared.xNull(rsTemp.getString("detDocId")));
                        listNode.setFromDate14(DmShared.xNull(rsTemp.getString("fromDate")));
                        listNode.setType_Leave_Attendance14(DmShared.xNull(rsTemp.getString("type_Leave_Attendance")));
                        listNode.setPlusMinus14(DmShared.xNull(rsTemp.getString("plusMinus")));
                        listNode.setNatureCode14(DmShared.xNull(rsTemp.getString("natureCode")));
                        listNode.setFromTime14(DmShared.xNull(rsTemp.getString("fromTime")));
                        listNode.setToTime14(DmShared.xNull(rsTemp.getString("toTime")));
                        listNode.setDays14(DmShared.xNull(rsTemp.getString("plusMinus")).trim() == "0" ? "-" + DmShared.xNull(rsTemp.getString("days")) : DmShared.xNull(rsTemp.getString("days")));
                        listNode.setOf_Ho_Lv14(DmShared.xNull(rsTemp.getString("offDay_HoliDayCode")));
                        break;
                    case 15:
                        listNode.setDay15(DmShared.xNull(rsTemp.getString("manualCode")));
                        listNode.setDay15Code(DmShared.xNull(rsTemp.getString("attendanceCode")));
                        listNode.setPreDocID15(DmShared.xNull(rsTemp.getString("detDocId")));
                        listNode.setFromDate15(DmShared.xNull(rsTemp.getString("fromDate")));
                        listNode.setType_Leave_Attendance15(DmShared.xNull(rsTemp.getString("type_Leave_Attendance")));
                        listNode.setPlusMinus15(DmShared.xNull(rsTemp.getString("plusMinus")));
                        listNode.setNatureCode15(DmShared.xNull(rsTemp.getString("natureCode")));
                        listNode.setFromTime15(DmShared.xNull(rsTemp.getString("fromTime")));
                        listNode.setToTime15(DmShared.xNull(rsTemp.getString("toTime")));
                        listNode.setDays15(DmShared.xNull(rsTemp.getString("plusMinus")).trim() == "0" ? "-" + DmShared.xNull(rsTemp.getString("days")) : DmShared.xNull(rsTemp.getString("days")));
                        listNode.setOf_Ho_Lv15(DmShared.xNull(rsTemp.getString("offDay_HoliDayCode")));
                        break;
                    case 16:
                        listNode.setDay16(DmShared.xNull(rsTemp.getString("manualCode")));
                        listNode.setDay16Code(DmShared.xNull(rsTemp.getString("attendanceCode")));
                        listNode.setPreDocID16(DmShared.xNull(rsTemp.getString("detDocId")));
                        listNode.setFromDate16(DmShared.xNull(rsTemp.getString("fromDate")));
                        listNode.setType_Leave_Attendance16(DmShared.xNull(rsTemp.getString("type_Leave_Attendance")));
                        listNode.setPlusMinus16(DmShared.xNull(rsTemp.getString("plusMinus")));
                        listNode.setNatureCode16(DmShared.xNull(rsTemp.getString("natureCode")));
                        listNode.setFromTime16(DmShared.xNull(rsTemp.getString("fromTime")));
                        listNode.setToTime16(DmShared.xNull(rsTemp.getString("toTime")));
                        listNode.setDays16(DmShared.xNull(rsTemp.getString("plusMinus")).trim() == "0" ? "-" + DmShared.xNull(rsTemp.getString("days")) : DmShared.xNull(rsTemp.getString("days")));
                        listNode.setOf_Ho_Lv16(DmShared.xNull(rsTemp.getString("offDay_HoliDayCode")));
                        break;
                    case 17:
                        listNode.setDay17(DmShared.xNull(rsTemp.getString("manualCode")));
                        listNode.setDay17Code(DmShared.xNull(rsTemp.getString("attendanceCode")));
                        listNode.setPreDocID17(DmShared.xNull(rsTemp.getString("detDocId")));
                        listNode.setFromDate17(DmShared.xNull(rsTemp.getString("fromDate")));
                        listNode.setType_Leave_Attendance17(DmShared.xNull(rsTemp.getString("type_Leave_Attendance")));
                        listNode.setPlusMinus17(DmShared.xNull(rsTemp.getString("plusMinus")));
                        listNode.setNatureCode17(DmShared.xNull(rsTemp.getString("natureCode")));
                        listNode.setFromTime17(DmShared.xNull(rsTemp.getString("fromTime")));
                        listNode.setToTime17(DmShared.xNull(rsTemp.getString("toTime")));
                        listNode.setDays17(DmShared.xNull(rsTemp.getString("plusMinus")).trim() == "0" ? "-" + DmShared.xNull(rsTemp.getString("days")) : DmShared.xNull(rsTemp.getString("days")));
                        listNode.setOf_Ho_Lv17(DmShared.xNull(rsTemp.getString("offDay_HoliDayCode")));
                        break;
                    case 18:
                        listNode.setDay18(DmShared.xNull(rsTemp.getString("manualCode")));
                        listNode.setDay18Code(DmShared.xNull(rsTemp.getString("attendanceCode")));
                        listNode.setPreDocID18(DmShared.xNull(rsTemp.getString("detDocId")));
                        listNode.setFromDate18(DmShared.xNull(rsTemp.getString("fromDate")));
                        listNode.setType_Leave_Attendance18(DmShared.xNull(rsTemp.getString("type_Leave_Attendance")));
                        listNode.setPlusMinus18(DmShared.xNull(rsTemp.getString("plusMinus")));
                        listNode.setNatureCode18(DmShared.xNull(rsTemp.getString("natureCode")));
                        listNode.setFromTime18(DmShared.xNull(rsTemp.getString("fromTime")));
                        listNode.setToTime18(DmShared.xNull(rsTemp.getString("toTime")));
                        listNode.setDays18(DmShared.xNull(rsTemp.getString("plusMinus")).trim() == "0" ? "-" + DmShared.xNull(rsTemp.getString("days")) : DmShared.xNull(rsTemp.getString("days")));
                        listNode.setOf_Ho_Lv18(DmShared.xNull(rsTemp.getString("offDay_HoliDayCode")));
                        break;
                    case 19:
                        listNode.setDay19(DmShared.xNull(rsTemp.getString("manualCode")));
                        listNode.setDay19Code(DmShared.xNull(rsTemp.getString("attendanceCode")));
                        listNode.setPreDocID19(DmShared.xNull(rsTemp.getString("detDocId")));
                        listNode.setFromDate19(DmShared.xNull(rsTemp.getString("fromDate")));
                        listNode.setType_Leave_Attendance19(DmShared.xNull(rsTemp.getString("type_Leave_Attendance")));
                        listNode.setPlusMinus19(DmShared.xNull(rsTemp.getString("plusMinus")));
                        listNode.setNatureCode19(DmShared.xNull(rsTemp.getString("natureCode")));
                        listNode.setFromTime19(DmShared.xNull(rsTemp.getString("fromTime")));
                        listNode.setToTime19(DmShared.xNull(rsTemp.getString("toTime")));
                        listNode.setDays19(DmShared.xNull(rsTemp.getString("plusMinus")).trim() == "0" ? "-" + DmShared.xNull(rsTemp.getString("days")) : DmShared.xNull(rsTemp.getString("days")));
                        listNode.setOf_Ho_Lv19(DmShared.xNull(rsTemp.getString("offDay_HoliDayCode")));
                        break;
                    case 20:
                        listNode.setDay20(DmShared.xNull(rsTemp.getString("manualCode")));
                        listNode.setDay20Code(DmShared.xNull(rsTemp.getString("attendanceCode")));
                        listNode.setPreDocID20(DmShared.xNull(rsTemp.getString("detDocId")));
                        listNode.setFromDate20(DmShared.xNull(rsTemp.getString("fromDate")));
                        listNode.setType_Leave_Attendance20(DmShared.xNull(rsTemp.getString("type_Leave_Attendance")));
                        listNode.setPlusMinus20(DmShared.xNull(rsTemp.getString("plusMinus")));
                        listNode.setNatureCode20(DmShared.xNull(rsTemp.getString("natureCode")));
                        listNode.setFromTime20(DmShared.xNull(rsTemp.getString("fromTime")));
                        listNode.setToTime20(DmShared.xNull(rsTemp.getString("toTime")));
                        listNode.setDays20(DmShared.xNull(rsTemp.getString("plusMinus")).trim() == "0" ? "-" + DmShared.xNull(rsTemp.getString("days")) : DmShared.xNull(rsTemp.getString("days")));
                        listNode.setOf_Ho_Lv20(DmShared.xNull(rsTemp.getString("offDay_HoliDayCode")));
                        break;
                    case 21:
                        listNode.setDay21(DmShared.xNull(rsTemp.getString("manualCode")));
                        listNode.setDay21Code(DmShared.xNull(rsTemp.getString("attendanceCode")));
                        listNode.setPreDocID21(DmShared.xNull(rsTemp.getString("detDocId")));
                        listNode.setFromDate21(DmShared.xNull(rsTemp.getString("fromDate")));
                        listNode.setType_Leave_Attendance21(DmShared.xNull(rsTemp.getString("type_Leave_Attendance")));
                        listNode.setPlusMinus21(DmShared.xNull(rsTemp.getString("plusMinus")));
                        listNode.setNatureCode21(DmShared.xNull(rsTemp.getString("natureCode")));
                        listNode.setFromTime21(DmShared.xNull(rsTemp.getString("fromTime")));
                        listNode.setToTime21(DmShared.xNull(rsTemp.getString("toTime")));
                        listNode.setDays21(DmShared.xNull(rsTemp.getString("plusMinus")).trim() == "0" ? "-" + DmShared.xNull(rsTemp.getString("days")) : DmShared.xNull(rsTemp.getString("days")));
                        listNode.setOf_Ho_Lv21(DmShared.xNull(rsTemp.getString("offDay_HoliDayCode")));
                        break;
                    case 22:
                        listNode.setDay22(DmShared.xNull(rsTemp.getString("manualCode")));
                        listNode.setDay22Code(DmShared.xNull(rsTemp.getString("attendanceCode")));
                        listNode.setPreDocID22(DmShared.xNull(rsTemp.getString("detDocId")));
                        listNode.setFromDate22(DmShared.xNull(rsTemp.getString("fromDate")));
                        listNode.setType_Leave_Attendance22(DmShared.xNull(rsTemp.getString("type_Leave_Attendance")));
                        listNode.setPlusMinus22(DmShared.xNull(rsTemp.getString("plusMinus")));
                        listNode.setNatureCode22(DmShared.xNull(rsTemp.getString("natureCode")));
                        listNode.setFromTime22(DmShared.xNull(rsTemp.getString("fromTime")));
                        listNode.setToTime22(DmShared.xNull(rsTemp.getString("toTime")));
                        listNode.setDays22(DmShared.xNull(rsTemp.getString("plusMinus")).trim() == "0" ? "-" + DmShared.xNull(rsTemp.getString("days")) : DmShared.xNull(rsTemp.getString("days")));
                        listNode.setOf_Ho_Lv22(DmShared.xNull(rsTemp.getString("offDay_HoliDayCode")));
                        break;
                    case 23:
                        listNode.setDay23(DmShared.xNull(rsTemp.getString("manualCode")));
                        listNode.setDay23Code(DmShared.xNull(rsTemp.getString("attendanceCode")));
                        listNode.setPreDocID23(DmShared.xNull(rsTemp.getString("detDocId")));
                        listNode.setFromDate23(DmShared.xNull(rsTemp.getString("fromDate")));
                        listNode.setType_Leave_Attendance23(DmShared.xNull(rsTemp.getString("type_Leave_Attendance")));
                        listNode.setPlusMinus23(DmShared.xNull(rsTemp.getString("plusMinus")));
                        listNode.setNatureCode23(DmShared.xNull(rsTemp.getString("natureCode")));
                        listNode.setFromTime23(DmShared.xNull(rsTemp.getString("fromTime")));
                        listNode.setToTime23(DmShared.xNull(rsTemp.getString("toTime")));
                        listNode.setDays23(DmShared.xNull(rsTemp.getString("plusMinus")).trim() == "0" ? "-" + DmShared.xNull(rsTemp.getString("days")) : DmShared.xNull(rsTemp.getString("days")));
                        listNode.setOf_Ho_Lv23(DmShared.xNull(rsTemp.getString("offDay_HoliDayCode")));
                        break;
                    case 24:
                        listNode.setDay24(DmShared.xNull(rsTemp.getString("manualCode")));
                        listNode.setDay24Code(DmShared.xNull(rsTemp.getString("attendanceCode")));
                        listNode.setPreDocID24(DmShared.xNull(rsTemp.getString("detDocId")));
                        listNode.setFromDate24(DmShared.xNull(rsTemp.getString("fromDate")));
                        listNode.setType_Leave_Attendance24(DmShared.xNull(rsTemp.getString("type_Leave_Attendance")));
                        listNode.setPlusMinus24(DmShared.xNull(rsTemp.getString("plusMinus")));
                        listNode.setNatureCode24(DmShared.xNull(rsTemp.getString("natureCode")));
                        listNode.setFromTime24(DmShared.xNull(rsTemp.getString("fromTime")));
                        listNode.setToTime24(DmShared.xNull(rsTemp.getString("toTime")));
                        listNode.setDays24(DmShared.xNull(rsTemp.getString("plusMinus")).trim() == "0" ? "-" + DmShared.xNull(rsTemp.getString("days")) : DmShared.xNull(rsTemp.getString("days")));
                        listNode.setOf_Ho_Lv24(DmShared.xNull(rsTemp.getString("offDay_HoliDayCode")));
                        break;
                    case 25:
                        listNode.setDay25(DmShared.xNull(rsTemp.getString("manualCode")));
                        listNode.setDay25Code(DmShared.xNull(rsTemp.getString("attendanceCode")));
                        listNode.setPreDocID25(DmShared.xNull(rsTemp.getString("detDocId")));
                        listNode.setFromDate25(DmShared.xNull(rsTemp.getString("fromDate")));
                        listNode.setType_Leave_Attendance25(DmShared.xNull(rsTemp.getString("type_Leave_Attendance")));
                        listNode.setPlusMinus25(DmShared.xNull(rsTemp.getString("plusMinus")));
                        listNode.setNatureCode25(DmShared.xNull(rsTemp.getString("natureCode")));
                        listNode.setFromTime25(DmShared.xNull(rsTemp.getString("fromTime")));
                        listNode.setToTime25(DmShared.xNull(rsTemp.getString("toTime")));
                        listNode.setDays25(DmShared.xNull(rsTemp.getString("plusMinus")).trim() == "0" ? "-" + DmShared.xNull(rsTemp.getString("days")) : DmShared.xNull(rsTemp.getString("days")));
                        listNode.setOf_Ho_Lv25(DmShared.xNull(rsTemp.getString("offDay_HoliDayCode")));
                        break;
                    case 26:
                        listNode.setDay26(DmShared.xNull(rsTemp.getString("manualCode")));
                        listNode.setDay26Code(DmShared.xNull(rsTemp.getString("attendanceCode")));
                        listNode.setPreDocID26(DmShared.xNull(rsTemp.getString("detDocId")));
                        listNode.setFromDate26(DmShared.xNull(rsTemp.getString("fromDate")));
                        listNode.setType_Leave_Attendance26(DmShared.xNull(rsTemp.getString("type_Leave_Attendance")));
                        listNode.setPlusMinus26(DmShared.xNull(rsTemp.getString("plusMinus")));
                        listNode.setNatureCode26(DmShared.xNull(rsTemp.getString("natureCode")));
                        listNode.setFromTime26(DmShared.xNull(rsTemp.getString("fromTime")));
                        listNode.setToTime26(DmShared.xNull(rsTemp.getString("toTime")));
                        listNode.setDays26(DmShared.xNull(rsTemp.getString("plusMinus")).trim() == "0" ? "-" + DmShared.xNull(rsTemp.getString("days")) : DmShared.xNull(rsTemp.getString("days")));
                        listNode.setOf_Ho_Lv26(DmShared.xNull(rsTemp.getString("offDay_HoliDayCode")));
                        break;
                    case 27:
                        listNode.setDay27(DmShared.xNull(rsTemp.getString("manualCode")));
                        listNode.setDay27Code(DmShared.xNull(rsTemp.getString("attendanceCode")));
                        listNode.setPreDocID27(DmShared.xNull(rsTemp.getString("detDocId")));
                        listNode.setFromDate27(DmShared.xNull(rsTemp.getString("fromDate")));
                        listNode.setType_Leave_Attendance27(DmShared.xNull(rsTemp.getString("type_Leave_Attendance")));
                        listNode.setPlusMinus27(DmShared.xNull(rsTemp.getString("plusMinus")));
                        listNode.setNatureCode27(DmShared.xNull(rsTemp.getString("natureCode")));
                        listNode.setFromTime27(DmShared.xNull(rsTemp.getString("fromTime")));
                        listNode.setToTime27(DmShared.xNull(rsTemp.getString("toTime")));
                        listNode.setDays27(DmShared.xNull(rsTemp.getString("plusMinus")).trim() == "0" ? "-" + DmShared.xNull(rsTemp.getString("days")) : DmShared.xNull(rsTemp.getString("days")));
                        listNode.setOf_Ho_Lv27(DmShared.xNull(rsTemp.getString("offDay_HoliDayCode")));
                        break;
                    case 28:
                        listNode.setDay28(DmShared.xNull(rsTemp.getString("manualCode")));
                        listNode.setDay28Code(DmShared.xNull(rsTemp.getString("attendanceCode")));
                        listNode.setPreDocID28(DmShared.xNull(rsTemp.getString("detDocId")));
                        listNode.setFromDate28(DmShared.xNull(rsTemp.getString("fromDate")));
                        listNode.setType_Leave_Attendance28(DmShared.xNull(rsTemp.getString("type_Leave_Attendance")));
                        listNode.setPlusMinus28(DmShared.xNull(rsTemp.getString("plusMinus")));
                        listNode.setNatureCode28(DmShared.xNull(rsTemp.getString("natureCode")));
                        listNode.setFromTime28(DmShared.xNull(rsTemp.getString("fromTime")));
                        listNode.setToTime28(DmShared.xNull(rsTemp.getString("toTime")));
                        listNode.setDays28(DmShared.xNull(rsTemp.getString("plusMinus")).trim() == "0" ? "-" + DmShared.xNull(rsTemp.getString("days")) : DmShared.xNull(rsTemp.getString("days")));
                        listNode.setOf_Ho_Lv28(DmShared.xNull(rsTemp.getString("offDay_HoliDayCode")));
                        break;
                    case 29:
                        listNode.setDay29(DmShared.xNull(rsTemp.getString("manualCode")));
                        listNode.setDay29Code(DmShared.xNull(rsTemp.getString("attendanceCode")));
                        listNode.setPreDocID29(DmShared.xNull(rsTemp.getString("detDocId")));
                        listNode.setFromDate29(DmShared.xNull(rsTemp.getString("fromDate")));
                        listNode.setType_Leave_Attendance29(DmShared.xNull(rsTemp.getString("type_Leave_Attendance")));
                        listNode.setPlusMinus29(DmShared.xNull(rsTemp.getString("plusMinus")));
                        listNode.setNatureCode29(DmShared.xNull(rsTemp.getString("natureCode")));
                        listNode.setFromTime29(DmShared.xNull(rsTemp.getString("fromTime")));
                        listNode.setToTime29(DmShared.xNull(rsTemp.getString("toTime")));
                        listNode.setDays29(DmShared.xNull(rsTemp.getString("plusMinus")).trim() == "0" ? "-" + DmShared.xNull(rsTemp.getString("days")) : DmShared.xNull(rsTemp.getString("days")));
                        listNode.setOf_Ho_Lv29(DmShared.xNull(rsTemp.getString("offDay_HoliDayCode")));
                        break;
                    case 30:
                        listNode.setDay30(DmShared.xNull(rsTemp.getString("manualCode")));
                        listNode.setDay30Code(DmShared.xNull(rsTemp.getString("attendanceCode")));
                        listNode.setPreDocID30(DmShared.xNull(rsTemp.getString("detDocId")));
                        listNode.setFromDate30(DmShared.xNull(rsTemp.getString("fromDate")));
                        listNode.setType_Leave_Attendance30(DmShared.xNull(rsTemp.getString("type_Leave_Attendance")));
                        listNode.setPlusMinus30(DmShared.xNull(rsTemp.getString("plusMinus")));
                        listNode.setNatureCode30(DmShared.xNull(rsTemp.getString("natureCode")));
                        listNode.setFromTime30(DmShared.xNull(rsTemp.getString("fromTime")));
                        listNode.setToTime30(DmShared.xNull(rsTemp.getString("toTime")));
                        listNode.setDays30(DmShared.xNull(rsTemp.getString("plusMinus")).trim() == "0" ? "-" + DmShared.xNull(rsTemp.getString("days")) : DmShared.xNull(rsTemp.getString("days")));
                        listNode.setOf_Ho_Lv30(DmShared.xNull(rsTemp.getString("offDay_HoliDayCode")));
                        break;
                    case 31:
                        listNode.setDay31(DmShared.xNull(rsTemp.getString("manualCode")));
                        listNode.setDay31Code(DmShared.xNull(rsTemp.getString("attendanceCode")));
                        listNode.setPreDocID31(DmShared.xNull(rsTemp.getString("detDocId")));
                        listNode.setFromDate31(DmShared.xNull(rsTemp.getString("fromDate")));
                        listNode.setType_Leave_Attendance31(DmShared.xNull(rsTemp.getString("type_Leave_Attendance")));
                        listNode.setPlusMinus31(DmShared.xNull(rsTemp.getString("plusMinus")));
                        listNode.setNatureCode31(DmShared.xNull(rsTemp.getString("natureCode")));
                        listNode.setFromTime31(DmShared.xNull(rsTemp.getString("fromTime")));
                        listNode.setToTime31(DmShared.xNull(rsTemp.getString("toTime")));
                        listNode.setDays31(DmShared.xNull(rsTemp.getString("plusMinus")).trim() == "0" ? "-" + DmShared.xNull(rsTemp.getString("days")) : DmShared.xNull(rsTemp.getString("days")));
                        listNode.setOf_Ho_Lv31(DmShared.xNull(rsTemp.getString("offDay_HoliDayCode")));
                        break;
                    default:
//                        new DmAlertMessageBox("", txt).show();
                        break;
                }
//                lstTemp.add(listNode);
            }
            lstTemp.add(listNode);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
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
        return lstTemp;
    }

    private void saveEmployeeAttendanceDet(GrdAttendanceEntry_Mdl attendanceEntryMdl, String attendanceManualCode, String of_Ho_Lv,
            String attendanceCode, String fromDate, String fromTime, String toTime, String plusMinus, String days, String type_Leave_Attendance,
            String natureCode, String preDocID, String mainDocID, Connection cnTemp) {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";
        String attendanceDocID = "";
        RecId recIdDet = new RecId();
        String attendancev_Type = "";
        String off_holidayCode = "";
        try {
            attendancev_Type = DmServer.getVType(lvc.ATTENDANCE_ENTRY_FINAL, cnTemp);

            sqlQuery = "delete from dailyAttendance_Final where employeeCode=:employeeCode and fromDate=:fromDate";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("employeeCode", DmShared.xNull(attendanceEntryMdl.getEmployeeCode()));
            psTemp.setString("fromDate", DmShared.xNull(fromDate));
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "delete from Employee_Attendance_Leave_Detail_Final where employeeCode=:employeeCode and fromDate=:fromDate and att_Lev_Ot in(1,2)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("employeeCode", DmShared.xNull(attendanceEntryMdl.getEmployeeCode()));
            psTemp.setString("fromDate", DmShared.xNull(fromDate));
            psTemp.executeUpdate();
            psTemp.close();

            if (of_Ho_Lv.trim().equalsIgnoreCase("OFD")) {
                off_holidayCode = offDayCode;
            } else if (of_Ho_Lv.trim().equalsIgnoreCase("HH")) {
                off_holidayCode = holidayCode;
            }

            if ((of_Ho_Lv.trim().equalsIgnoreCase("OFD") || of_Ho_Lv.trim().equalsIgnoreCase("HH"))
                    && !attendanceCode.trim().equalsIgnoreCase(offDayCode.trim()) && !attendanceCode.trim().equalsIgnoreCase(holidayCode.trim())) {
                attendanceDocID = DmServer.getDocId(DmShared.xNull(attendancev_Type), DmServer.getSsnVoucherPrefix(this), cnTemp, this);
                recIdDet = DmServer.getRecId("dailyAttendance_Final", "docId", attendanceDocID, "recId", recIdDet, DmServer.getServerDateTime()[0], DmShared.xNull(attendancev_Type), DmServer.getSsnVoucherPrefix(this), cnTemp, this, true);

                sqlQuery = "insert into dailyAttendance_Final(docId,v_Type,recId,v_No,recIdPrefix,v_Prefix,v_Date,employeeCode,attendanceCode,fromDate\n"
                        + " ,fromTime,toDate,toTime,days,plusMinus,site_Code,u_Name,u_EntDt)  "//duration,remark,
                        + " values(:docId,:v_Type,:recId,:v_No,:recIdPrefix,:v_Prefix,:v_Date,:employeeCode,:attendanceCode,:fromDate\n"
                        + " ,:fromTime,:toDate,:toTime,:days,:plusMinus,:site_Code,:u_Name,:u_EntDt)";
                //:duration,:remark,

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(attendanceDocID).toString());
                psTemp.setString("v_Type", DmShared.foriegnKey(attendancev_Type));
                psTemp.setString("recId", DmShared.xNull(recIdDet.getRecIdValue()));
                psTemp.setString("v_No", DmShared.iNull(recIdDet.getCounter()).toString());
                psTemp.setString("recIdPrefix", DmShared.xNull(recIdDet.getPrefix()));
                psTemp.setString("v_Prefix", DmServer.getSsnVoucherPrefix(this));
                psTemp.setString("v_Date", DmShared.xNull(DmServer.getServerDateTime()[0]));
                psTemp.setString("employeeCode", DmShared.foriegnKey(attendanceEntryMdl.getEmployeeCode()));
                psTemp.setString("attendanceCode", DmShared.foriegnKey(off_holidayCode));
                psTemp.setString("fromDate", DmShared.xNull(fromDate));
                psTemp.setString("fromTime", DmServer.vNull(fromTime));
                psTemp.setString("toDate", DmShared.xNull(fromDate));
                psTemp.setString("toTime", DmServer.vNull(toTime));
//                psTemp.setString("duration", DmServer.vNull(mrTemp.getDuration()));
                psTemp.setString("days", "1");
                psTemp.setString("plusMinus", "1");
//                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
                psTemp.setString("U_name", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("U_EntDt", DmServer.getServerTimestamp());

                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = "insert into Employee_Attendance_Leave_Detail_Final(docId,v_Date,employeeCode,att_Lev_OT,attendanceCode,fromDate\n"
                        + " ,fromTime,toDate,toTime,days,from_Eff_DateTime,to_Eff_DateTime,site_Code,mainDocID)  "//,totMinuts
                        + " values(:docId,:v_Date,:employeeCode,:att_Lev_OT,:attendanceCode,:fromDate\n"
                        + " ,:fromTime,:toDate,:toTime,:days,:from_Eff_DateTime,:to_Eff_DateTime,:site_Code,:mainDocID)";
//,:totMinuts
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(attendanceDocID).toString());
                psTemp.setString("v_Date", DmShared.xNull(DmServer.getServerDateTime()[0]));
                psTemp.setString("employeeCode", DmShared.foriegnKey(attendanceEntryMdl.getEmployeeCode()));
                psTemp.setString("att_Lev_OT", DmServer.vNull(type_Leave_Attendance));
                psTemp.setString("attendanceCode", DmShared.foriegnKey(off_holidayCode));
                psTemp.setString("fromDate", DmShared.xNull(fromDate));
                psTemp.setString("fromTime", DmServer.vNull(fromTime));
                psTemp.setString("toDate", DmShared.xNull(fromDate));
                psTemp.setString("toTime", DmServer.vNull(toTime));
//                psTemp.setString("totMinuts", DmServer.vNull(mrTemp.getDuration()));

                psTemp.setString("days", "1");

                psTemp.setString("from_Eff_DateTime", DmShared.xNull(fromDate));
                psTemp.setString("to_Eff_DateTime", DmShared.xNull(fromDate));

//                psTemp.setString("from_Eff_DateTime", DmShared.xNull(fromDate) + " " + DmShared.xNull(fromTime).replace(".", ":"));
//                psTemp.setString("to_Eff_DateTime", DmShared.xNull(fromDate) + " " + DmShared.xNull(toTime).replace(".", ":"));
                psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
                psTemp.setString("mainDocID", DmShared.foriegnKey(mainDocID));
                psTemp.executeUpdate();
                psTemp.close();
            }

            recIdDet = new RecId();
            attendanceDocID = DmServer.getDocId(DmShared.xNull(attendancev_Type), DmServer.getSsnVoucherPrefix(this), cnTemp, this);
            recIdDet = DmServer.getRecId("dailyAttendance_Final", "docId", attendanceDocID, "recId", recIdDet, DmServer.getServerDateTime()[0], DmShared.xNull(attendancev_Type), DmServer.getSsnVoucherPrefix(this), cnTemp, this, true);

            sqlQuery = "insert into dailyAttendance_Final(docId,v_Type,recId,v_No,recIdPrefix,v_Prefix,v_Date,employeeCode,attendanceCode,fromDate\n"
                    + " ,fromTime,toDate,toTime,days,plusMinus,site_Code,u_Name,u_EntDt)  "//duration,remark,
                    + " values(:docId,:v_Type,:recId,:v_No,:recIdPrefix,:v_Prefix,:v_Date,:employeeCode,:attendanceCode,:fromDate\n"
                    + " ,:fromTime,:toDate,:toTime,:days,:plusMinus,:site_Code,:u_Name,:u_EntDt)";
            //:duration,:remark,

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(attendanceDocID).toString());
            psTemp.setString("v_Type", DmShared.foriegnKey(attendancev_Type));
            psTemp.setString("recId", DmShared.xNull(recIdDet.getRecIdValue()));
            psTemp.setString("v_No", DmShared.iNull(recIdDet.getCounter()).toString());
            psTemp.setString("recIdPrefix", DmShared.xNull(recIdDet.getPrefix()));
            psTemp.setString("v_Prefix", DmServer.getSsnVoucherPrefix(this));
            psTemp.setString("v_Date", DmShared.xNull(DmServer.getServerDateTime()[0]));
            psTemp.setString("employeeCode", DmShared.foriegnKey(attendanceEntryMdl.getEmployeeCode()));
            psTemp.setString("attendanceCode", DmShared.foriegnKey(attendanceCode));
            psTemp.setString("fromDate", DmShared.xNull(fromDate));
            psTemp.setString("fromTime", DmServer.vNull(fromTime));
            psTemp.setString("toDate", DmShared.xNull(fromDate));
            psTemp.setString("toTime", DmServer.vNull(toTime));
//                psTemp.setString("duration", DmServer.vNull(mrTemp.getDuration()));
            psTemp.setString("days", DmServer.vNull(days));
            psTemp.setString("plusMinus", DmServer.vNull(plusMinus));
//                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp.setString("U_name", DmServer.getSsnUserName(this));
            psTemp.setTimestamp("U_EntDt", DmServer.getServerTimestamp());

            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "insert into Employee_Attendance_Leave_Detail_Final(docId,v_Date,employeeCode,att_Lev_OT,attendanceCode,fromDate\n"
                    + " ,fromTime,toDate,toTime,days,from_Eff_DateTime,to_Eff_DateTime,site_Code,mainDocID)  "//,totMinuts
                    + " values(:docId,:v_Date,:employeeCode,:att_Lev_OT,:attendanceCode,:fromDate\n"
                    + " ,:fromTime,:toDate,:toTime,:days,:from_Eff_DateTime,:to_Eff_DateTime,:site_Code,:mainDocID)";
//,:totMinuts
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(attendanceDocID).toString());
            psTemp.setString("v_Date", DmShared.xNull(DmServer.getServerDateTime()[0]));
            psTemp.setString("employeeCode", DmShared.foriegnKey(attendanceEntryMdl.getEmployeeCode()));
            psTemp.setString("att_Lev_OT", DmServer.vNull(type_Leave_Attendance));
            psTemp.setString("attendanceCode", DmShared.foriegnKey(attendanceCode));
            psTemp.setString("fromDate", DmShared.xNull(fromDate));
            psTemp.setString("fromTime", DmServer.vNull(fromTime));
            psTemp.setString("toDate", DmShared.xNull(fromDate));
            psTemp.setString("toTime", DmServer.vNull(toTime));
//                psTemp.setString("totMinuts", DmServer.vNull(mrTemp.getDuration()));

            psTemp.setString("days", DmServer.vNull(days));
//            if (DmShared.iNull(plusMinus).intValue() == 1) {
//                psTemp.setString("days", DmServer.vNull(days));
//            } else {
//                psTemp.setString("days", "-" + DmServer.vNull(days, "0.00"));
//            }

            psTemp.setString("from_Eff_DateTime", DmShared.xNull(fromDate));
            psTemp.setString("to_Eff_DateTime", DmShared.xNull(fromDate));

//            psTemp.setString("from_Eff_DateTime", DmShared.xNull(fromDate) + " " + DmShared.xNull(fromTime).replace(".", ":"));
//            psTemp.setString("to_Eff_DateTime", DmShared.xNull(fromDate) + " " + DmShared.xNull(toTime).replace(".", ":"));
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp.setString("mainDocID", DmShared.foriegnKey(mainDocID));
            psTemp.executeUpdate();
            psTemp.close();
        } catch (Exception ex) {
            throw new DmException(0, ex.getMessage());
        }
    }

    @Override
    public DmResult<String> getDefaultValue() {
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        DmPreparedStatement psTemp = null;
        DmResult<String> dmrTemp = new DmResult();

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            //===================< Get Code of byDefault >================================
            sqlQuery = "select acm.name,acm.code from attendenceCodeDefinitionMast acm where acm.manualCode = 'NA' and acm.type_Leave_Attendance=" + TYPE_LEAVE_ATTENDANCE.getViaText("ATTENDANCE").getId();
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                notAppliedAttendanceCode = DmShared.xNull(rsTemp.getString("code"));
            }
            rsTemp.close();
            psTemp.close();
            
            //===================< Get Code of byDefault >================================
            sqlQuery = "select acm.name,acm.code from attendenceCodeDefinitionMast acm where acm.byDefault = 1 and acm.type_Leave_Attendance=" + TYPE_LEAVE_ATTENDANCE.getViaText("ATTENDANCE").getId();
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                byDefaultAttendanceCode = DmShared.xNull(rsTemp.getString("code"));
            }
            rsTemp.close();
            psTemp.close();

            //===================< Get Code of holiday >================================
            sqlQuery = "select acm.manualCode,acm.code from attendenceCodeDefinitionMast acm where acm.manualCode = 'HH' and acm.type_Leave_Attendance=" + TYPE_LEAVE_ATTENDANCE.getViaText("ATTENDANCE").getId();
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                holidayCode = DmShared.xNull(rsTemp.getString("code"));
            }
            rsTemp.close();
            psTemp.close();

            //===================< Get Code of offDay >================================
            sqlQuery = "select acm.name,acm.code from attendenceCodeDefinitionMast acm where acm.manualCode = 'OFD' and acm.type_Leave_Attendance=" + TYPE_LEAVE_ATTENDANCE.getViaText("ATTENDANCE").getId();
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                offDayCode = DmShared.xNull(rsTemp.getString("code"));
            }
            rsTemp.close();
            psTemp.close();

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
}
