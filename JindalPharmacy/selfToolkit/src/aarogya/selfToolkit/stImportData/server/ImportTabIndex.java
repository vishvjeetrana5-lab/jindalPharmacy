package aarogya.selfToolkit.stImportData.server;

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
import dataman.dmToolkit.dmImportData.server.DmImportData_Interface;
import dataman.dmToolkit.dmImportData.shared.DmImportStatus;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ImportTabIndex implements DmImportData_Interface {

    class Model {

        private String module, menuName, caption, serial;

        public Model() {
        }

        public String getModule() {
            return module;
        }

        public void setModule(String module) {
            this.module = module;
        }

        public String getMenuName() {
            return menuName;
        }

        public void setMenuName(String menuName) {
            this.menuName = menuName;
        }

        public String getCaption() {
            return caption;
        }

        public void setCaption(String caption) {
            this.caption = caption;
        }

        public String getSerial() {
            return serial;
        }

        public void setSerial(String serial) {
            this.serial = serial;
        }
    }

    private DmRSS rss;
    private DmHandelFile handelFileLog;

    private String lineNoDisplay = "";

    public ImportTabIndex() {
    }

    @Override
    public ImportData_Mdl getImportDataModel() {
        ImportData_Mdl fileDetail = new ImportData_Mdl();
        CSVData node;
        List<CSVData> lstCSV = new ArrayList<CSVData>();

        node = new CSVData();
        node.setHeader("module");
        node.setSample("OPD");
        node.setInstruction("{mandatory;alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("menuName");
        node.setSample("Consultation");
        node.setInstruction("{mandatory;alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("caption");
        node.setSample("OPD No.");
        node.setInstruction("{mandatory;alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("serial");
        node.setSample("1");
        node.setInstruction("{mandatory;numeric;2c}");
        lstCSV.add(node);

        fileDetail.getHmCSV().addAll(lstCSV);
        fileDetail.setImportTo("Menu Codification");
        fileDetail.setServerClassName(ImportTabIndex.class.getName());

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
        int status = 0, rtnStatus = 0;
        DmImportStatus importStatus = null;
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
                        if (headerName.trim().equalsIgnoreCase("module")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setModule(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("menuName")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setMenuName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("caption")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setCaption(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("serial")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, true, false, false, false, false, 2, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setSerial(fieldValue);
                            }
                        }
                    }
                    if (isFound) {
                        status = saveMenuCodification(mrTemp);
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

    private int saveMenuCodification(Model mrTemp) {
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        int status = 0, rtnStatus = 0;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));
            spBegin = new DmSavePoint(cnTemp);
            mrTemp.setModule(DmShared.removeSpace(mrTemp.getModule()));

            if (mrTemp.getModule().equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new UnsupportedOperationException("Invalid key.");
            }
            sqlQuery = " select count(*) as cnt "
                    + " from tabIndexSettings   "
                    + " where mnuModule = :mnuModule and mnuName = :mnuName and caption=:caption and sNo=:sNo  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("mnuModule", DmShared.xNull(mrTemp.getModule()));
            psTemp.setString("mnuName", DmShared.xNull(mrTemp.getMenuName()));
            psTemp.setString("caption", DmShared.xNull(mrTemp.getCaption()));
            psTemp.setString("sNo", DmShared.xNull(mrTemp.getSerial()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("cnt") > 1) {
                    status = DmImportStatus.ERROR.getId();
                    throw new DmException(0, "Tab index detail is duplicate");
                }
            }

            /*==============================< Delete & Insert records into tabIndexSettings >================================*/
            sqlQuery = "Delete From tabIndexSettings where mnuModule= :mnuModule and mnuName=:mnuName and caption=:caption and sNo=:sNo ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("mnuModule", DmShared.xNull(mrTemp.getModule()));
            psTemp.setString("mnuName", DmShared.xNull(mrTemp.getMenuName()));
            psTemp.setString("caption", DmShared.xNull(mrTemp.getCaption()));
            psTemp.setString("sNo", DmShared.xNull(mrTemp.getSerial()));
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "insert Into tabIndexSettings(mnuModule,mnuName,caption,sNo) Values  "
                    + "(:mnuModule,:mnuName,:caption,:sNo) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("mnuModule", DmShared.xNull(mrTemp.getModule()));
            psTemp.setString("mnuName", DmShared.xNull(mrTemp.getMenuName()));
            psTemp.setString("caption", DmShared.xNull(mrTemp.getCaption()));
            psTemp.setString("sNo", DmShared.xNull(mrTemp.getSerial()));
            psTemp.executeUpdate();
            psTemp.close();

            spBegin.commit();
            cnTemp.close();
            rtnStatus = DmImportStatus.SUCCESS.getId();
        } catch (Exception e) {
            try {
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
