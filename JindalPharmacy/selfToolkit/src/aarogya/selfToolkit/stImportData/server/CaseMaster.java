package aarogya.selfToolkit.stImportData.server;

import dataman.dmToolkit.dmImportData.server.DmImportData_Interface;
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
import dataman.dmToolkit.dmImportData.shared.DmImportStatus;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CaseMaster implements DmImportData_Interface {

    class Model {

        private String code, name, departmentName, isOB;

        public Model() {
        }

        public String getDepartmentName() {
            return departmentName;
        }

        public void setDepartmentName(String departmentName) {
            this.departmentName = departmentName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIsOB() {
            return isOB;
        }

        public void setIsOB(String isOB) {
            this.isOB = isOB;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    private DmRSS rss;
    private DmHandelFile handelFileLog;
    private String lineNoDisplay = "";

    public CaseMaster() {
    }

    @Override
    public ImportData_Mdl getImportDataModel() {
        ImportData_Mdl fileDetail = new ImportData_Mdl();
        CSVData node;
        List<CSVData> lstCSV = new ArrayList<CSVData>();

        node = new CSVData();
        node.setHeader("caseName");
        node.setSample("Pregnancy");
        node.setInstruction("{unique;mandatory;alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("departmentName");
        node.setSample("Hospital");
        node.setInstruction("{alphanumeric;35c;mandatory}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("caseType");
        node.setSample("Adverse Drug Reaction");
        node.setInstruction("{mandatory;alphanumeric;25c;(Other/Obstetrics/Adverse Drug Reaction)}");
        lstCSV.add(node);

        fileDetail.getHmCSV().addAll(lstCSV);
        fileDetail.setImportTo("Case Master");
        fileDetail.setServerClassName(CaseMaster.class.getName());

        return fileDetail;
    }

    @Override
    public DmImportStatus importCSV(DmRSSPrivate rss, ImportData_Mdl importData) {
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
                csvFileFormat = ImportData_Server.getCSVFormats(importData.getHmCSV()).get("header").split(",");
                dataFile = new File(importData.getDataFilePath());
                if (dataFile.exists()) {
                    csvReader = new DmCsvReader(dataFile);
                    csvReader.readLines();
                    status = ImportData_Server.checkHeader(csvFileFormat, csvReader, handelFileLog);
                    if (status == DmImportStatus.ERROR.getId()) {

                        throw new DmException(0, "");
                    }
                } else {
                    status = DmImportStatus.ERROR.getId();
                    throw new Exception("Data file not found.");
                }

                for (int i = 0; i < csvReader.getRows().size(); i++) {
                    lineNoDisplay = "Line " + String.valueOf(i + 1) + ": ";
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
                        if (headerName.trim().equalsIgnoreCase("caseName")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("DepartmentName")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setDepartmentName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("CaseType")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 25, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setIsOB(fieldValue);
                            }
                        }
                    }
                    if (isFound) {
                        status = saveItemMast(mrTemp);
                    }
                    if (status > rtnStatus) {
                        rtnStatus = status;
                    }
                    if (rtnStatus > 20) {
                        throw new DmException(0, "");
                    }
                }
            }
        } catch (Exception e) {
            if (status > rtnStatus) {
                rtnStatus = status;
            }
            try {

                handelFileLog.writeLine(e.getMessage());
            } catch (Exception ex) {
            }
        } finally {
            importStatus = DmImportStatus.getImportStatus(rtnStatus);
            try {
                if (csvReader != null) {
                    csvReader = null;
                }
                if (dataFile != null) {
                    dataFile = null;
                }
                if (handelFileLog != null) {
                    handelFileLog.close();
                }
            } catch (Exception e) {
            }
        }
        return importStatus;
    }

    private int saveItemMast(Model mrTemp) {
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, departmentCode = "", isOB = "";
        int status = 0, rtnStatus = 0;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setName(DmShared.removeSpace(mrTemp.getName()));
            mrTemp.setCode(DmServer.getSerialNoInteger(rss, "caseMast", cnTemp));
            if (DmShared.iNull(mrTemp.getCode()).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new UnsupportedOperationException("Invalid key.");
            }

            sqlQuery = "select dm.subCode"
                    + " from departmentMast dm"
                    + " left join subGroup sg on sg.subCode = dm.subCode "
                    + " where sg.name = :department ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("department", mrTemp.getDepartmentName());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                departmentCode = DmShared.iNull(rsTemp.getString("subCode")).toString();
            }
            rsTemp.close();
            psTemp.close();
            if (DmShared.iNull(departmentCode).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, mrTemp.getDepartmentName() + " department not exists.");
            }

            if (DmShared.xNull(mrTemp.getIsOB()).trim().equalsIgnoreCase("Other")) {
                isOB = "0";
            } else if (DmShared.xNull(mrTemp.getIsOB()).trim().equalsIgnoreCase("Obstetrics")) {
                isOB = "1";
            } else if (DmShared.xNull(mrTemp.getIsOB()).trim().equalsIgnoreCase("Adverse Drug Reaction")) {
                isOB = "2";
            }

            sqlQuery = "select COUNT(cm.name) as cnt from caseMast as cm  where cm.name = :name  AND cm.code <> :code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getName()));
            psTemp.setString("code", DmShared.xNull(mrTemp.getCode()));
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                status = DmImportStatus.WARNING.getId();
                throw new DmException(0, DmShared.xNull(mrTemp.getName()) + " Name already exists.");
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "insert into caseMast (code, name,departmentCode,isOB,preparedBY, u_EntDt)"
                    + "  values(:code, :name,:departmentCode,:isOB,:preparedBY,:uEntDt);";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", mrTemp.getCode());
            psTemp.setString("name", mrTemp.getName());
            psTemp.setString("departmentCode", DmShared.foriegnKey(departmentCode));
            psTemp.setString("isOB", isOB);
            psTemp.setString("preparedBY", DmServer.getSsnUserName(rss));
            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            psTemp.executeUpdate();
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

                handelFileLog.writeLine(lineNoDisplay + e.getMessage());
                if (spBegin != null) {
                    spBegin.rollBack();
                }
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

            } catch (Exception e) {
            }
        }
        return rtnStatus;
    }
}
