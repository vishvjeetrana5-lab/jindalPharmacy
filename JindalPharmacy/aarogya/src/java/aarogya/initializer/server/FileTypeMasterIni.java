package aarogya.initializer.server;

import aarogya.projectMenu.self.shared.ProjectRuleCode;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSS;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.server.DmCsvReader;
import dataman.dmBase.self.server.DmHandelFile;
import dataman.dmBase.self.shared.DmDynamicGridRow;
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmImportData.server.DmImportData_Interface;
import dataman.dmToolkit.dmImportData.server.DmImportData_Server;
import dataman.dmToolkit.dmImportData.shared.CSVData;
import dataman.dmToolkit.dmImportData.shared.DmImportStatus;
import dataman.dmToolkit.dmImportData.shared.ImportData_Mdl;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.libraryMenu.self.shared.LibraryRuleCode;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FileTypeMasterIni implements DmImportData_Interface {

    class Model {

        private String ruleCode, description, fileType, maxSize, filters, defaultValue;

        public String getRuleCode() {
            return ruleCode;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setRuleCode(String ruleCode) {
            this.ruleCode = ruleCode;
        }

        public String getFileType() {
            return fileType;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
        }

        public String getMaxSize() {
            return maxSize;
        }

        public void setMaxSize(String maxSize) {
            this.maxSize = maxSize;
        }

        public String getFilters() {
            return filters;
        }

        public void setFilters(String filters) {
            this.filters = filters;
        }

        public String getDefaultValue() {
            return defaultValue;
        }

        public void setDefaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
        }

        public Model() {
        }

    }
    protected DmRSS rss;
    protected DmHandelFile handelFileLog;
    protected String lineNoDisplay = "";

    @Override
    public ImportData_Mdl getImportDataModel() {
        ImportData_Mdl fileDetail = new ImportData_Mdl();
        CSVData node;
        List<CSVData> lstCSV = new ArrayList<CSVData>();

        node = new CSVData();
        node.setHeader("ruleCode");
        node.setSample("Sundry Creditors");
        node.setInstruction("{unique;mandatory;alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("description");
        node.setSample("Sundry Creditors");
        node.setInstruction("{mandatory;alphanumeric;250c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("fileType");
        node.setSample("Sundry Creditors");
        node.setInstruction("{mandatory;alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("maxSize");
        node.setSample("Current Liabilities");
        node.setInstruction("{alphanumeric;mandatory}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("filters");
        node.setSample("Supplier");
        node.setInstruction("{mandatory;alphanumeric;mandatory;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("default");
        node.setSample("Supplier");
        node.setInstruction("{mandatory;alphanumeric;mandatory;50c}");
        lstCSV.add(node);

        fileDetail.getHmCSV().addAll(lstCSV);
        fileDetail.setImportTo("File Type Master");
        fileDetail.setServerClassName(FileTypeMasterIni.class.getName());

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
                    lineNoDisplay = "Line " + String.valueOf(i + 1) + ": ";
                    dynamicGridRow = csvReader.getRows().get(i);
                    mrTemp = new Model();
                    isFound = true;

                    for (String headerName : csvFileFormat) {
                        objValue = dynamicGridRow.getCells().get(headerName.toLowerCase());
                        if (objValue == null) {
                            status = DmImportStatus.ERROR.getId();
                            fieldValue = "";
                        } else {
                            fieldValue = String.valueOf(objValue);
                        }
                        if (headerName.trim().equalsIgnoreCase("ruleCode")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setRuleCode(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("description")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 500, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setDescription(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("fileType")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setFileType(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("maxSize")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, true, true, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setMaxSize(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("filters")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setFilters(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("default")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setDefaultValue(fieldValue);
                            }
                        }
                    }
                    if (isFound) {
                        status = saveRecord(mrTemp);
                    }
                    if (status > rtnStatus) {
                        rtnStatus = status;
                    }
                }
            }

        } catch (Exception e) {
            try {
                if (status > rtnStatus) {
                    rtnStatus = status;
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
            } catch (Exception e) {
            }
        }
        return importStatus;
    }

    public int saveRecord(Model mrTemp) {
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        int status = 0, rtnStatus = 0;
        boolean ruleFound = true;
        ProjectRuleCode ruleCodeProject = null;
        LibraryRuleCode ruleCodeLibrary = null;
        String sqlQuery, yesNo;
        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            spBegin = new DmSavePoint(cnTemp);

            ruleCodeLibrary = LibraryRuleCode.getLibraryRuleCode(mrTemp.getRuleCode());
            if (ruleCodeLibrary == null) {
                ruleFound = false;
            }

            if (!ruleFound) {
                ruleCodeProject = ProjectRuleCode.getProjectRuleCode(mrTemp.getRuleCode());
                ruleFound = true;
            }
            if (ruleCodeProject == null) {
                ruleFound = false;
            }

            if (!ruleFound) {
                status = DmImportStatus.WARNING.getId();
                handelFileLog.writeLine("File Type not found for " + mrTemp.getRuleCode() + " Rule Code.");
            }

            if (DmShared.iNull(mrTemp.getMaxSize()).longValue() <= 0) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, "Define max size for " + mrTemp.getFileType() + " File Type.");
            }
            if (DmShared.xNull(mrTemp.getFilters()).equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, "Define Filters(eg:-.pdf,.docx) for " + mrTemp.getFileType() + " File Type.");
            }

            if (DmShared.xNull(mrTemp.getDefaultValue()).equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, "Define Default (eg:Yes/No) for  " + mrTemp.getFileType() + " File Type.");
            } else {
                yesNo = DmShared.xNull(mrTemp.getDefaultValue()).trim().equalsIgnoreCase("Yes") ? "1" : "0";
            }

            if (DmShared.xNull(mrTemp.getRuleCode()).equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new UnsupportedOperationException("Invalid key.");
            }

            sqlQuery = "delete from fileTypeMast where ruleCode = :code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.xNull(mrTemp.getRuleCode()));
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "Insert Into filetypemast(ruleCode, description ,fileType, maxSize, isDefault, extFilters, u_Name, u_EntDt) Values  "
                    + "(:ruleCode,:description ,:fileType, :maxSize, :isDefault, :extFilters, :uName, :uEntDt ) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("ruleCode", DmShared.xNull(mrTemp.getRuleCode()));
            psTemp.setString("description", DmShared.xNull(mrTemp.getDescription()));
            psTemp.setString("fileType", DmShared.xNull(mrTemp.getFileType()));
            psTemp.setString("maxSize", DmShared.xNull(mrTemp.getMaxSize()));
            psTemp.setString("isDefault", DmServer.vNull(yesNo, "0"));
            psTemp.setString("extFilters", DmShared.xNull(mrTemp.getFilters()));
            psTemp.setString("uName", DmServer.getSsnUserName(rss));
            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            psTemp.executeUpdate();
            psTemp.close();

            spBegin.commit();
            rtnStatus = DmImportStatus.SUCCESS.getId();
        } catch (Exception e) {
            try {
                if (status == DmImportStatus.NONE.getId()) {
                    status = DmImportStatus.ERROR.getId();
                }
                if (status > rtnStatus) {
                    rtnStatus = status;
                }
                handelFileLog.writeLine(e.getMessage());
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
