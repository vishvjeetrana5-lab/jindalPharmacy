package dataman.payroll.attendanceImport.server;

import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.dmSession.server.DmRequestWrapper;
import dataman.dmBase.self.server.DmHandelFile;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmToolkit.dmImportData.shared.ImportData_Mdl;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.payroll.attendanceImport.shared.AttendanceImport_Mdl;
import dataman.payroll.attendanceImport.shared.AttendanceImport_Srv;
import dataman.payroll.attendanceImport.shared.SearchValue_Mdl;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import dataman.dmToolkit.dmImportData.server.*;
import dataman.dmToolkit.dmImportData.shared.*;
import dataman.payroll.attendanceImport.shared.DefaultModel_Mdl;
import java.util.ArrayList;

public class AttendanceImport_SrvImpl extends DmRSSPrivate implements AttendanceImport_Srv {

    private DmHandelFile handelFileLog;

    @Override
    public DmResult<String> saveRecord(AttendanceImport_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean add = false;
        String sqlQuery;
        ResultSet rsTemp = null;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            if (DmShared.dNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                add = true;
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            /*============================<Check Duplicate data>=================================*/
            if (add) {
                sqlQuery = "Select count(Date) as cnt from holidayMast where Date = :Date ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setDate("Date", DmServer.convertDate(mrTemp.getSharedKeyFieldCode()));
                rsTemp = psTemp.executeQuery();
                rsTemp.next();
                if (rsTemp.getLong("cnt") > 0) {
                    throw new UnsupportedOperationException("Date already exists. ");
                }
                rsTemp.close();
                psTemp.close();
            }

            /*============================< Delete records from HolidayMaster >==============================*/
            sqlQuery = " Delete From holidayMast Where Date = :Date ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("Date", DmServer.convertDate(mrTemp.getSharedKeyFieldCode()));
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "insert into holidayMast (Date, holidayName, u_Name, u_EntDt, TypeCode )values "
                    + " (:Date, :holidayName, :u_Name, :uEntDt, :TypeCode) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("Date", DmServer.convertDate(mrTemp.getSharedKeyFieldCode()));
            psTemp.setString("holidayName", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
            psTemp.setString("u_Name", DmServer.getSsnUserName(this));
            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
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

        }

        return dmrTemp;
    }

    @Override
    public DmResult<ImportData_Mdl> importData(ImportData_Mdl mrTemp) {
        DmResult<ImportData_Mdl> dmrTemp = new DmResult<>();
        ImportAttendance attendance = new ImportAttendance();
        try {

            if (mrTemp != null) {
                if (!DmShared.xNull(ImportAttendance.class.getName()).trim().equalsIgnoreCase("")
                        && !DmShared.xNull(mrTemp.getDataFilePath()).equalsIgnoreCase("")) {
                    mrTemp.setImportStatus(new ImportAttendance().importCSV(this, mrTemp));
                }
            }
            dmrTemp.setData(mrTemp);
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return dmrTemp;
    }

    @Override
    public DmResult<String> getCSVFile(ImportData_Mdl mrTemp, String fileName) {
        DmResult<String> dmrTemp = new DmResult<String>();
        HashMap<String, String> hmCSV;
        String downloadFileName = "", downloadDirectoryPath, fileFullPath, sessionId;
        Class<?> clsType;
        try {
            clsType = Class.forName(mrTemp.getServerClassName());
            fileName = DmShared.xNull(fileName).trim().replace(" ", "");
            if (fileName.equals("")) {
                downloadFileName = clsType.getSimpleName() + String.valueOf(Calendar.getInstance().getTime().getTime()) + ".csv";
            } else {
                downloadFileName = fileName + String.valueOf(Calendar.getInstance().getTime().getTime()) + ".csv";
            }
            sessionId = ((DmRequestWrapper) this.getLocalRequest()).getSession().getId();
            downloadDirectoryPath = DmServer.Path.sessions + File.separatorChar + sessionId + File.separatorChar + "importData";
            downloadDirectoryPath = downloadDirectoryPath + File.separatorChar + "downloads";
            new File(downloadDirectoryPath).mkdirs();
            fileFullPath = downloadDirectoryPath + File.separatorChar + downloadFileName;
            hmCSV = DmImportData_Server.getCSVFormats(mrTemp.getHmCSV());
            handelFileLog = new DmHandelFile(fileFullPath, true);
            handelFileLog.writeLine(hmCSV.get("header"));
            handelFileLog.writeLine(hmCSV.get("sample"));
            handelFileLog.writeLine("");
            handelFileLog.writeLine("");
            handelFileLog.writeLine("");
            handelFileLog.writeLine(hmCSV.get("instruction"));

            dmrTemp.setData(downloadFileName);
            dmrTemp.setValue1(downloadDirectoryPath);
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (handelFileLog != null) {
                    handelFileLog.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

    @Override
    public DmResult<KeyFieldExtended> readLogFile(ImportData_Mdl importData_Mdl) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DmResultPageing<List<ImportData_Mdl>> getImportTo(SearchValue_Mdl searchValue_Mdl, DmHelper helper, boolean blnToEndOfFile) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DmResult<List<ImportData_Mdl>> getImportData() {
        DmResultPageing<List<ImportData_Mdl>> rstTemp = new DmResultPageing<>();
        String logDirectoryPath, sessionId;
        rstTemp.setData(new ArrayList<ImportData_Mdl>());

        try {
            sessionId = ((DmRequestWrapper) this.getLocalRequest()).getSession().getId();
            logDirectoryPath = DmServer.Path.sessions + File.separatorChar + sessionId + File.separatorChar + "importData";
            logDirectoryPath = logDirectoryPath + File.separatorChar + "logs";
            new File(logDirectoryPath).mkdirs();
            rstTemp.getData().add(new ImportData_Mdl());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResult<DefaultModel_Mdl> getDefaultMdl() {
        DmResultPageing<DefaultModel_Mdl> rstTemp = new DmResultPageing<>();
        String logDirectoryPath, sessionId;
        ImportData_Mdl importData = null;
        Class<?> clsType = null;
        DefaultModel_Mdl mrTemp = new DefaultModel_Mdl();

        try {
            sessionId = ((DmRequestWrapper) this.getLocalRequest()).getSession().getId();
            logDirectoryPath = DmServer.Path.sessions + File.separatorChar + sessionId + File.separatorChar + "Attendance";
            logDirectoryPath = logDirectoryPath + File.separatorChar + "logs";
            new File(logDirectoryPath).mkdirs();
            importData = new ImportAttendance().getImportDataModel();

            clsType = Class.forName(importData.getServerClassName());
            String logFileName = clsType.getSimpleName() + String.valueOf(Calendar.getInstance().getTime().getTime()) + ".log";
            importData.setLogFileName(logDirectoryPath + File.separatorChar + logFileName);

            importData.setFileFormat(DmImportData_Server.getCSVFormats(importData.getHmCSV()).get("header"));
            mrTemp.setImportMdl(importData);
            mrTemp.setLogFileName(logFileName);
            mrTemp.setLogFilePath(logDirectoryPath);
            
            rstTemp.setData(mrTemp);
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

}
