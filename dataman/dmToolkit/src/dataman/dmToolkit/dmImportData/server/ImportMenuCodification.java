package dataman.dmToolkit.dmImportData.server;

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

public class ImportMenuCodification implements DmImportData_Interface {

    class Model {

        private String module, menuName, menuText, shortHand;

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

        public String getMenuText() {
            return menuText;
        }

        public void setMenuText(String menuText) {
            this.menuText = menuText;
        }

        public String getShortHand() {
            return shortHand;
        }

        public void setShortHand(String shortHand) {
            this.shortHand = shortHand;
        }
    }

    private DmRSS rss;
    private DmHandelFile handelFileLog;

    private String lineNoDisplay = "";

    public ImportMenuCodification() {
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
        node.setSample("OPDService");
        node.setInstruction("{mandatory;alphanumeric;250c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("menuText");
        node.setSample("OPD Service");
        node.setInstruction("{mandatory;alphanumeric;250c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("codification");
        node.setSample("OS");
        node.setInstruction("{mandatory;alphanumeric;6c}");
        lstCSV.add(node);

        fileDetail.getHmCSV().addAll(lstCSV);
        fileDetail.setImportTo("Menu Codification");
        fileDetail.setServerClassName(ImportMenuCodification.class.getName());

        return fileDetail;
    }

    @Override
    public DmImportStatus importCSV(DmRSSPrivate rss, ImportData_Mdl importData) {
        Model mrTemp;
        List<Model> models = new ArrayList<Model>();
        DmCsvReader csvReader = null;
        File dataFile = null;
        boolean isFound, isSuccess = true;;
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
                        if (headerName.trim().equalsIgnoreCase("module")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setModule(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("menuName")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setMenuName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("menuText")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setMenuText(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("codification")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setShortHand(fieldValue);
                            }
                        }
                    }
                    if (isFound) {
                        models.add(mrTemp);
                    }
                    if (status > rtnStatus) {
                        rtnStatus = status;
                    }
                }

                status = saveMenuCodification(models);
                if (status > rtnStatus) {
                    rtnStatus = status;
                }
            }

        } catch (Exception e) {
            try {
                if (status > rtnStatus) {
                    rtnStatus = status;
                }
                isSuccess = false;
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

    private int saveMenuCodification(List<Model> models) {
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        int status = 0, rtnStatus = 0;
        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "delete from menuCodification ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.executeUpdate();
            psTemp.close();

            for (Model mrTemp : models) {
                mrTemp.setMenuName(DmShared.removeSpace(mrTemp.getMenuName()).replace(" ", ""));
                mrTemp.setMenuText(DmShared.removeSpace(mrTemp.getMenuText()));
                mrTemp.setModule(DmShared.removeSpace(mrTemp.getModule()).replace(" ", ""));
                mrTemp.setShortHand(DmShared.removeSpace(mrTemp.getShortHand()).replace(" ", ""));

                DmShared.isValidText(mrTemp.getMenuName(), "Menu Name", 100);
                DmShared.isValidText(mrTemp.getMenuText(), "Menu Text", 100);
                DmShared.isValidText(mrTemp.getModule(), "Module", 100);
                DmShared.isValidText(mrTemp.getShortHand(), "Codification", 100);

                sqlQuery = "select  count(mnuShortHand) as cnt "
                        + "         from  menuCodification as mc"
                        + "         where mnuShortHand=:mnuShortHand "
                        + "         group by mnuShortHand";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("mnuShortHand", mrTemp.getShortHand());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (DmShared.iNull(rsTemp.getString("cnt")).longValue() > 0) {
                        status = DmImportStatus.WARNING.getId();
                        throw new DmException(0, "Menu code already exist.");
                    }
                }
                rsTemp.close();
                psTemp.close();

                if (!DmShared.xNull(mrTemp.getMenuText()).equalsIgnoreCase("")) {
                    sqlQuery = "insert into menuCodification(mnuModule, mnuName, mnuShortHand, mnuText) "
                            + " values (:mnuModule, :mnuName, :mnuShortHand, :mnuText) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("mnuModule", DmShared.xNull(mrTemp.getModule()));
                    psTemp.setString("mnuName", DmShared.xNull(mrTemp.getMenuName()));
                    psTemp.setString("mnuText", DmShared.xNull(mrTemp.getMenuText()));
                    psTemp.setString("mnuShortHand", DmShared.xNull(mrTemp.getShortHand()).toUpperCase());
                    psTemp.executeUpdate();
                    psTemp.close();
                }

            }

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
