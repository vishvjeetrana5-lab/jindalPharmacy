package dataman.payroll.attendanceImport.server;

import dataman.dmToolkit.dmImportData.shared.CSVData;
import dataman.dmToolkit.dmImportData.shared.ImportData_Mdl;
import dataman.dmBase.self.shared.DmDynamicGridRow;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSS;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.server.DmCsvReader;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.server.DmHandelFile;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.KeyField;
import dataman.dmBase.self.shared.RecId;
import dataman.dmToolkit.dmImportData.server.DmImportData_Interface;
import dataman.dmToolkit.dmImportData.server.DmImportData_Server;
import dataman.dmToolkit.dmImportData.shared.DmImportStatus;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import dataman.payroll.attendanceImport.client.AttendanceImport;
import dataman.dmBase.self.shared.*;
import dataman.payroll.self.shared.PREnum;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ImportAttendance implements DmImportData_Interface {

    private LibraryVoucherCategory lvc = new LibraryVoucherCategory();
    String tableName = "";
    AttendanceImport attendanceImport;
    String logFilePath;

    class Model extends KeyField {

        private String machineEmployeeId;
        private String attendanceDate;
        private String attendanceTime;

        public String getMachineEmployeeId() {
            return machineEmployeeId;
        }

        public void setMachineEmployeeId(String machineEmployeeId) {
            this.machineEmployeeId = machineEmployeeId;
        }

        public String getAttendanceDate() {
            return attendanceDate;
        }

        public void setAttendanceDate(String attendanceDate) {
            this.attendanceDate = attendanceDate;
        }

        public String getAttendanceTime() {
            return attendanceTime;
        }

        public void setAttendanceTime(String attendanceTime) {
            this.attendanceTime = attendanceTime;
        }
    }
    private DmRSS rss;
    private DmHandelFile handelFileLog;
    private String LineNoDisplay = "";

    public ImportAttendance() {
//        this.attendanceImport = AttendanceImport;
    }

    @Override
    public ImportData_Mdl getImportDataModel() {
        ImportData_Mdl fileDetail = new ImportData_Mdl();
        CSVData node;
        List<CSVData> lstCSV = new ArrayList<CSVData>();

        node = new CSVData();
        node.setHeader("machineEmployeeId");
        node.setSample("DM0000131");
        node.setInstruction("{mandatory;20c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("attendanceDate");
        node.setSample("01/Jan/2021");
        node.setInstruction("{mandatory;15c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("attendanceTime");
        node.setSample("01.30");
        node.setInstruction("{mandatory;double;5c}");
        lstCSV.add(node);

        fileDetail.getHmCSV().addAll(lstCSV);
        fileDetail.setImportTo("Attendance Import");
        fileDetail.setServerClassName(ImportAttendance.class.getName());

        return fileDetail;

    }

    @Override
    public DmImportStatus importCSV(DmRSSPrivate rss, ImportData_Mdl importData) {
        DmSavePoint spBegin = null;
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";

        Model mrTemp;
        DmCsvReader csvReader = null;
        File dataFile = null;
        boolean isFound;
        DmDynamicGridRow dynamicGridRow;
        String fieldValue;
        Object objValue;
        String[] csvFileFormat;
        DmImportStatus importStatus = null;
        int status = 0, rtnStatus = 0;
        try {
            logFilePath = importData.getLogFilePath();
            handelFileLog = new DmHandelFile(importData.getLogFilePath(), true);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));
            spBegin = new DmSavePoint(cnTemp);
            tableName = getTable(rss);
            sqlQuery = "IF OBJECT_ID('tempdb.." + tableName + "') IS NOT NULL "
                    + " DROP TABLE " + tableName + " ;"
                    + " create  table " + tableName + " (machineEmployeeId varchar(20), attendanceDate date, attendanceTime float)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.execute();
            psTemp.close();

            this.rss = rss;

            if (DmShared.xNull(importData.getLogFilePath()).trim().equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new Exception("Log file path can not be blank.");
            }
            handelFileLog = new DmHandelFile(importData.getLogFilePath(), true);

            if (DmShared.xNull(importData.getDataFilePath()).trim().equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new Exception("Data file path can not be blank.");
            } else {
                csvFileFormat = DmImportData_Server.getCSVFormats(importData.getHmCSV()).get("header").split(",");
                dataFile = new File(importData.getDataFilePath());
                if (dataFile.exists()) {
                    csvReader = new DmCsvReader(dataFile);
                    csvReader.readLines();
                    status = DmImportData_Server.checkHeader(csvFileFormat, csvReader, handelFileLog);
                    if (status == DmImportStatus.ERROR.getId()) {
                        throw new DmException(0, "");
                    }
                } else {
                    status = DmImportStatus.ERROR.getId();
                    throw new Exception("Data file not found.");
                }

                for (int i = 0; i < csvReader.getRows().size(); i++) {
                    LineNoDisplay = "Line " + String.valueOf(i + 1) + ": ";
                    dynamicGridRow = csvReader.getRows().get(i);
                    mrTemp = new Model();
                    isFound = true;

                    for (String headerName : csvFileFormat) {
                        objValue = dynamicGridRow.getCells().get(headerName.toLowerCase());
                        if (objValue == null) {
                            fieldValue = "";
                        } else {
                            fieldValue = String.valueOf(objValue);
                        }
                        if (headerName.trim().equalsIgnoreCase("machineEmployeeId")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 25, LineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setMachineEmployeeId(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("attendanceDate")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 30, LineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setAttendanceDate(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("attendanceTime")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, true, 5, LineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setAttendanceTime(fieldValue);
                            }
                        }
                    }
                    if (isFound) {
                        status = importAttendance(mrTemp, cnTemp);
                    }
                    if (status > rtnStatus) {
                        rtnStatus = status;
                    }
                }
            }
            spBegin.commit();
            cnTemp.close();
            rtnStatus = saveAttendance(importData.getStartDate(), importData.getEndDate());
        } catch (Exception e) {
            try {
                if (status > rtnStatus) {
                    rtnStatus = status;
                }
                if (spBegin != null) {
                    spBegin.rollBack();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
                handelFileLog.writeLine(e.getMessage());
            } catch (Exception ex) {

            }
        } finally {
            try {
                importStatus = DmImportStatus.getImportStatus(rtnStatus);
                if (csvReader != null) {
                    csvReader = null;
                }
                if (dataFile != null) {
                    dataFile = null;
                }
                if (handelFileLog != null) {
                    handelFileLog.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return importStatus;
    }

    private int importAttendance(Model mrTemp, Connection cnTemp) {
//        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        int status = 0, rtnStatus = 0;
        try {

            sqlQuery = " insert into " + tableName + " values(:machineEmployeeId,:attendanceDate,:attendanceTime)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("machineEmployeeId", mrTemp.getMachineEmployeeId());
            psTemp.setString("attendanceDate", mrTemp.getAttendanceDate());
            psTemp.setString("attendanceTime", mrTemp.getAttendanceTime());
            psTemp.executeUpdate();
            psTemp.close();

            rtnStatus = DmImportStatus.SUCCESS.getId();
        } catch (Exception e) {
            try {
                if (status == DmImportStatus.NONE.getId()) {
                    status = DmImportStatus.ERROR.getId();
                }
                if (status > rtnStatus) {
                    rtnStatus = status;
                }
                handelFileLog.writeLine(LineNoDisplay + e.getMessage());
//                if (spBegin != null) {
//                    spBegin.rollBack();
//                }
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
        return rtnStatus;
    }

    private int saveAttendance(String startDate, String endDate) {
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTempChk = null;
        DmPreparedStatement psTempChk = null;
        ResultSet rsTemp1 = null;
        DmPreparedStatement psTemp1 = null;
        ResultSet rsTempSave = null;
        DmPreparedStatement psTempSave = null;

        String sqlQuery;
        int status = 0, rtnStatus = 0;

        String v_Type = null;
        String[] currentDateTime;
        String docID;
        RecId recId = new RecId();
        String currentLineDet = "";
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));
            spBegin = new DmSavePoint(cnTemp);

            v_Type = DmServer.getVType(lvc.ATTENDANCE_ENTRY_MANUAL, cnTemp);
            currentDateTime = getServerDateTime();

            sqlQuery = " select max(sg.name) as employeeName,tmp.machineEmployeeId,sg.subCode,min(tmp.attendanceTime) as inTime\n"
                    + " ,max(tmp.attendanceTime) as outTime,tmp.attendanceDate as attendanceDate\n"
                    + " ,max(esd.shiftCode) as shiftCode,max(sm.shiftName) as shiftName,max(sm.code) as shiftCode,max(sm.fromTime) as fromTime\n"
                    + " ,max(sm.toTime) as toTime,max(sm.duration) as duration\n"
                    + " ,concat(tmp.attendanceDate,' ',replace(min(cast (tmp.attendanceTime as numeric(6,2))),'.',':')) as inDateTime\n"
                    + " ,concat(tmp.attendanceDate,' ',replace(max(cast (tmp.attendanceTime as numeric(6,2))),'.',':')) as outDateTime\n"
                    + " ,datediff(minute,concat(tmp.attendanceDate,' ',replace(min(cast (tmp.attendanceTime as numeric(6,2))),'.',':')) \n"
                    + " ,concat(tmp.attendanceDate,' ',replace(max(cast (tmp.attendanceTime as numeric(6,2))),'.',':'))) as actualDuration\n"
                    + " ,datediff(minute,concat(tmp.attendanceDate,' ',replace(min(cast (tmp.attendanceTime as numeric(6,2))),'.',':')) \n"
                    + " ,concat(tmp.attendanceDate,' ',replace(max(sm.fromTime),'.',':'))) as lateDuration\n"
                    + " ,max(acm.code) as attCode,max(acm.days) as dayPeriod,max(acm.manualCode) as attManualCode\n"
                    + " ,max(acm.type_Leave_Attendance) as type_Leave_Attendance,max(acm.plusMinus) as plusMinus\n"
                    + " from " + tableName + " tmp "
                    + " left join EmployeeSoftId_Detail eid on eid.softId=tmp.machineEmployeeId collate DATABASE_DEFAULT\n"
                    + " left join subGroup sg on sg.subCode=eid.employeeCode \n"
                    + " left join pyEmployeeShiftDetail esd on sg.subCode=esd.employeeCode and tmp.attendanceDate between esd.fromDate and esd.toDate\n"
                    + " left join shiftMast sm on esd.shiftCode=sm.code\n"
                    + " left join attendenceCodeDefinitionMast acm on 1=1 and acm.byDefault=1\n"
                    + " where tmp.attendanceDate between :fromDate and :toDate and tmp.machineEmployeeId is not Null"
                    + " group by tmp.machineEmployeeId,tmp.attendanceDate,sg.subCode\n";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("fromDate", startDate);
            psTemp.setString("toDate", endDate);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                /**
                 * **************Checking previous entry for same employee and
                 * date******************
                 */
                currentLineDet = "Soft ID : " + DmShared.xNull(rsTemp.getString("machineEmployeeId")) + " Date :" + DmShared.dNull(rsTemp.getDate("attendanceDate"));
                sqlQuery = "select docid \n"
                        + "	from dailyAttendance da\n"
                        + " where da.employeeCode = :employeeCode and da.fromDate=:fromDate ";
                psTempChk = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTempChk.setString("employeeCode", DmShared.foriegnKey(rsTemp.getString("subCode")));
                psTempChk.setString("fromDate", DmShared.dNull(rsTemp.getDate("attendanceDate")));
                rsTempChk = psTempChk.executeQuery();
                if (rsTempChk.next()) {
                    if (DmShared.iNull(rsTempChk.getString("docid")).longValue() != 0) {
                        status = DmImportStatus.ERROR.getId();
                        throw new DmException(100, "Record already exist.");
                    }
                }
                rsTempChk.close();
                psTempChk.close();
                /**
                 * ***********************************************************************************
                 */
                recId = new RecId();
                docID = DmServer.getDocId(DmShared.xNull(v_Type), DmServer.getSsnVoucherPrefix(rss), cnTemp, rss);
                recId = DmServer.getRecId("dailyAttendance", "docId", DmShared.xNull(docID), "recId", recId, currentDateTime[0], DmShared.xNull(v_Type), DmServer.getSsnVoucherPrefix(rss), cnTemp, rss, true);

                sqlQuery = "insert into dailyAttendance(docId,v_Type,recId,v_No,recIdPrefix,v_Prefix,v_Date,employeeCode,attendanceCode,fromDate\n"
                        + " ,fromTime,toDate,toTime,duration,days,plusMinus,remark,site_Code,u_Name,u_EntDt,isImported)  "
                        + " values(:docId,:v_Type,:recId,:v_No,:recIdPrefix,:v_Prefix,:v_Date,:employeeCode,:attendanceCode,:fromDate\n"
                        + " ,:fromTime,:toDate,:toTime,:duration,:days,:plusMinus,:remark,:site_Code,:u_Name,:u_EntDt,1)";

                psTempSave = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTempSave.setString("docId", DmShared.iNull(docID).toString());
                psTempSave.setString("v_Type", DmShared.foriegnKey(v_Type));
                psTempSave.setString("recId", DmShared.xNull(recId.getRecIdValue()));
                psTempSave.setString("v_No", DmShared.iNull(recId.getCounter()).toString());
                psTempSave.setString("recIdPrefix", DmShared.xNull(recId.getPrefix()));
                psTempSave.setString("v_Prefix", DmServer.getSsnVoucherPrefix(rss));
                psTempSave.setString("v_Date", DmShared.xNull(currentDateTime[0]));
                psTempSave.setString("employeeCode", DmShared.foriegnKey(rsTemp.getString("subCode")));
                psTempSave.setString("attendanceCode", DmShared.foriegnKey(rsTemp.getString("attCode")));
                psTempSave.setString("fromDate", DmShared.xNull(rsTemp.getString("attendanceDate")));
                psTempSave.setString("fromTime", DmServer.vNull(rsTemp.getString("inTime")));
                psTempSave.setString("toDate", DmShared.xNull(rsTemp.getString("attendanceDate")));
                psTempSave.setString("toTime", DmServer.vNull(rsTemp.getString("outTime")));
                psTempSave.setString("duration", DmServer.vNull(rsTemp.getString("actualDuration")));
                psTempSave.setString("days", DmServer.vNull(rsTemp.getString("dayPeriod")));
                psTempSave.setString("plusMinus", DmServer.vNull(rsTemp.getString("plusMinus")));
                psTempSave.setString("remark", DmShared.xNull("Imported"));
                psTempSave.setString("site_Code", DmServer.getSsnSiteCode(rss));
                psTempSave.setString("U_name", DmServer.getSsnUserName(rss));
                psTempSave.setTimestamp("U_EntDt", DmServer.getServerTimestamp());

                psTempSave.executeUpdate();
                psTempSave.close();

                sqlQuery = "insert into Employee_Attendance_Leave_Detail(docId,v_Date,employeeCode,att_Lev_OT,attendanceCode,fromDate\n"
                        + " ,fromTime,toDate,toTime,totMinuts,days,from_Eff_DateTime,to_Eff_DateTime,site_Code,isImported,isStatus)  "
                        + " values(:docId,:v_Date,:employeeCode,:att_Lev_OT,:attendanceCode,:fromDate\n"
                        + " ,:fromTime,:toDate,:toTime,:totMinuts,:days,:from_Eff_DateTime,:to_Eff_DateTime,:site_Code,1,:isStatus)";

                psTempSave = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTempSave.setString("docId", DmShared.iNull(docID).toString());
                psTempSave.setString("v_Date", DmShared.xNull(currentDateTime[0]));
                psTempSave.setString("employeeCode", DmShared.foriegnKey(rsTemp.getString("subCode")));
                psTempSave.setString("att_Lev_OT", DmServer.vNull(PREnum.TYPE_LEAVE_ATTENDANCE.getViaText("ATTENDANCE").getId()));
                psTempSave.setString("attendanceCode", DmShared.foriegnKey(rsTemp.getString("attCode")));
                psTempSave.setString("fromDate", DmShared.xNull(rsTemp.getString("attendanceDate")));
                psTempSave.setString("fromTime", DmServer.vNull(rsTemp.getString("inTime")));
                psTempSave.setString("toDate", DmShared.xNull(rsTemp.getString("attendanceDate")));
                psTempSave.setString("toTime", DmServer.vNull(rsTemp.getString("outTime")));
                psTempSave.setString("totMinuts", DmServer.vNull(rsTemp.getString("actualDuration")));
                psTempSave.setString("days", DmServer.vNull(rsTemp.getString("dayPeriod")));
                psTemp.setString("isStatus", DmShared.xNull(String.valueOf(PREnum.LEAVE_STATUS.APPROVED.getId())));
                psTempSave.setString("from_Eff_DateTime", DmShared.xNull(rsTemp.getString("attendanceDate")) + " " + DmShared.xNull(rsTemp.getString("inTime")).replace(".", ":"));
                psTempSave.setString("to_Eff_DateTime", DmShared.xNull(rsTemp.getString("attendanceDate")) + " " + DmShared.xNull(rsTemp.getString("outTime")).replace(".", ":"));
                psTempSave.setString("site_Code", DmServer.getSsnSiteCode(rss));
                psTempSave.executeUpdate();
                psTempSave.close();
            }
            rsTemp.close();
            psTemp.close();

            spBegin.commit();
            cnTemp.close();
            rtnStatus = DmImportStatus.SUCCESS.getId();
        } catch (Exception e) {
            try {
                if (status == DmImportStatus.NONE.getId()) {
                    status = DmImportStatus.ERROR.getId();
                }
                if (status > rtnStatus) {
                    rtnStatus = status;
                }
                if (spBegin != null) {
                    spBegin.rollBack();
                }
                handelFileLog.writeLine(currentLineDet + e.getMessage());
                rtnStatus = DmImportStatus.ERROR.getId();
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
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
                if (handelFileLog != null) {
                    handelFileLog.close();
                }
            } catch (Exception e) {
            }
        }
        return rtnStatus;
    }

    private String getTable(DmRSSPrivate rssPrivate) {
        String sessionName;
        sessionName = (rssPrivate.getLocalRequest().getSession().getId()).replaceAll("-", "_");
        return "##t" + sessionName + "_" + "AttendanceImport";
    }

    public String[] getServerDateTime() {
        String[] dateTime, returnValue;

        dateTime = DmServer.getServerDateTime();
        returnValue = new String[3];
        returnValue[0] = dateTime[0];
        returnValue[1] = dateTime[1];
        returnValue[2] = UUID.randomUUID().toString();

        return returnValue;
    }
}
