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

public class MedicineCategoryMaster implements DmImportData_Interface {

    class Model {

        private String code, name, allowBifurcationTag, allowBifurcationText, commonTag, commonText;

        public Model() {
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAllowBifurcationTag() {
            return allowBifurcationTag;
        }

        public void setAllowBifurcationTag(String allowBifurcationTag) {
            this.allowBifurcationTag = allowBifurcationTag;
        }

        public String getAllowBifurcationText() {
            return allowBifurcationText;
        }

        public void setAllowBifurcationText(String allowBifurcationText) {
            this.allowBifurcationText = allowBifurcationText;
        }

        public String getCommonTag() {
            return commonTag;
        }

        public void setCommonTag(String commonTag) {
            this.commonTag = commonTag;
        }

        public String getCommonText() {
            return commonText;
        }

        public void setCommonText(String commonText) {
            this.commonText = commonText;
        }

    }
    private DmRSS rss;
    private DmHandelFile handelFileLog;
    private String lineNoDisplay = "";

    public MedicineCategoryMaster() {
    }

    @Override
    public ImportData_Mdl getImportDataModel() {
        ImportData_Mdl fileDetail = new ImportData_Mdl();
        CSVData node;
        List<CSVData> lstCSV = new ArrayList<CSVData>();

        node = new CSVData();
        node.setHeader("categoryName");
        node.setSample("HEADACHE");
        node.setInstruction("{unique,mandatory,alphanumeric,50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("allowBifurcation");
        node.setSample("No");
        node.setInstruction("{mandatory;Yes/No;3c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("common");
        node.setSample("No");
        node.setInstruction("{mandatory;Yes/No;3c}");
        lstCSV.add(node);

        fileDetail.getHmCSV().addAll(lstCSV);
        fileDetail.setImportTo("Medicine Category Master");
        fileDetail.setServerClassName(MedicineCategoryMaster.class.getName());

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
        int rtnStatus = 0, status = 0;
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
                        if (headerName.trim().equalsIgnoreCase("categoryName")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("allowBifurcation")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 3, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setAllowBifurcationText(fieldValue);
                            }

                        } else if (headerName.trim().equalsIgnoreCase("common")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 3, lineNoDisplay, handelFileLog)) {

                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setCommonText(fieldValue);
                            }
                        }
                    }
                    if (isFound) {
                        status = saveMedCategory(mrTemp);
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

    private int saveMedCategory(Model mrTemp) {
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        int rtnStatus = 0, status = 0;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setName(DmShared.removeSpace(mrTemp.getName()));
            mrTemp.setCode(DmServer.getSerialNoInteger(rss, "itemCat", cnTemp));

            if (DmShared.iNull(mrTemp.getCode()).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new UnsupportedOperationException("Invalid key.");
            }

            // =========================== CHECK CATEGORY NAME IF EXISTS ==========================
            sqlQuery = "SELECT COUNT(name) AS cnt FROM itemCat WHERE name = :name AND code <> :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getName()));
            psTemp.setString("code", DmShared.xNull(mrTemp.getCode()));
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                status = DmImportStatus.WARNING.getId();
                throw new DmException(0, DmShared.xNull(mrTemp.getName()) + " medicine category already exists.");
            }
            rsTemp.close();
            psTemp.close();
            // ====================================== END ==============================================

            // ======================= CHECK FOr ALLOW BIFURCATION  ===========================================================
            if (DmShared.xNull(mrTemp.getAllowBifurcationText()).trim().equalsIgnoreCase("Yes")) {
                mrTemp.setAllowBifurcationTag("1");
            } else if (DmShared.xNull(mrTemp.getAllowBifurcationText()).trim().equalsIgnoreCase("No")) {
                mrTemp.setAllowBifurcationTag("0");
            }
            if (DmShared.xNull(mrTemp.getAllowBifurcationTag()).equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, DmShared.xNull(mrTemp.getAllowBifurcationText()) + " Allow Bifurcation does not exist");
            }
            // ====================================== END ==========================================================

            // ======================= CHECK FOr COMMON  ===========================================================
            if (DmShared.xNull(mrTemp.getCommonText()).trim().equalsIgnoreCase("Yes")) {
                mrTemp.setCommonTag("1");
            } else if (DmShared.xNull(mrTemp.getCommonText()).trim().equalsIgnoreCase("No")) {
                mrTemp.setCommonTag("0");
            }
            if (DmShared.xNull(mrTemp.getCommonTag()).equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, DmShared.xNull(mrTemp.getCommonText()) + " Common does not exist");
            }
            // ====================================== END ==========================================================

            //======================= INSERT IN ITEMCAT TABLE  =========================================
            sqlQuery = "Insert Into itemCat(code ,name, filterItemType, "
                    + " preparedBy, u_EntDt,isCommon,allowBifurcation) Values  "
                    + "(:code, :name, :filterItemType, :preparedBy, :u_EntDt,:isCommon,:allowBifurcation) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", mrTemp.getCode());
            psTemp.setString("name", mrTemp.getName().trim());
            psTemp.setString("filterItemType", DmShared.foriegnKey(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            psTemp.setString("isCommon", DmShared.xNull(mrTemp.getCommonTag()));
            psTemp.setString("allowBifurcation", DmShared.xNull(mrTemp.getAllowBifurcationTag()));
            psTemp.setString("preparedBy", DmServer.getSsnUserName(rss));
            psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
            psTemp.executeUpdate();

            // ====================================== END ================================================
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
