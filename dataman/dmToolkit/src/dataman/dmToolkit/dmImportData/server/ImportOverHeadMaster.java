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

public class ImportOverHeadMaster implements DmImportData_Interface {

    class Model {

        private String name, manualCode, code;

        public Model() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getManualCode() {
            return manualCode;
        }

        public void setManualCode(String manualCode) {
            this.manualCode = manualCode;
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

    public ImportOverHeadMaster() {
    }

    @Override
    public ImportData_Mdl getImportDataModel() {
        ImportData_Mdl fileDetail = new ImportData_Mdl();
        CSVData node;
        List<CSVData> lstCSV = new ArrayList<CSVData>();

        node = new CSVData();
        node.setHeader("name");
        node.setSample("Gross Amount");
        node.setInstruction("{mandatory;alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("manualCode");
        node.setSample("G");
        node.setInstruction("{mandatory;alphanumeric;10c}");
        lstCSV.add(node);

        fileDetail.getHmCSV().addAll(lstCSV);
        fileDetail.setImportTo("Over Head Master");
        fileDetail.setServerClassName(ImportOverHeadMaster.class.getName());

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
                        if (headerName.trim().equalsIgnoreCase("name")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("manualCode")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setManualCode(fieldValue);
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

    private int saveMenuCodification(Model mrTemp) {
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        int status = 0, rtnStatus = 0;
        try {
            mrTemp.setManualCode(DmShared.removeSpace(mrTemp.getManualCode()).replace(" ", ""));
            mrTemp.setName(DmShared.removeSpace(mrTemp.getName()));

            DmShared.isValidText(mrTemp.getManualCode(), "Manual Code", 100);
            DmShared.isValidText(mrTemp.getName(), "Overhead Name", 100);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setCode(DmServer.getCodeViaManualCode(mrTemp.getManualCode(), "manualCode", "overheadmast", "code", cnTemp));
            if (DmShared.iNull(mrTemp.getCode()).longValue() == 0) {
                mrTemp.setCode(DmServer.getSerialNoInteger(rss, "overhead", cnTemp));

                if (mrTemp.getCode().equalsIgnoreCase("")) {
                    status = DmImportStatus.ERROR.getId();
                    throw new UnsupportedOperationException("Invalid key.");
                }

                sqlQuery = "Select Count(Name) As Cnt From overheadmast  Where Name= :name And Code <> :Code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("name", mrTemp.getName());
                psTemp.setString("Code", mrTemp.getCode());
                rsTemp = psTemp.executeQuery();
                rsTemp.next();
                if (rsTemp.getLong("cnt") > 0) {
                    status = DmImportStatus.WARNING.getId();
                    throw new UnsupportedOperationException(mrTemp.getName() + " overhead name is already exists.");
                }
                rsTemp.close();
                psTemp.close();

                sqlQuery = " Select Count(ManualCode) As Cnt From overheadmast  Where ManualCode = :ManualCode  And Code <> :Code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("ManualCode", mrTemp.getManualCode());
                psTemp.setString("Code", mrTemp.getCode());
                rsTemp = psTemp.executeQuery();
                rsTemp.next();
                if (rsTemp.getLong("cnt") > 0) {
                    status = DmImportStatus.WARNING.getId();
                    throw new UnsupportedOperationException(mrTemp.getManualCode() + " manual code is already exists.");
                }
                rsTemp.close();
                psTemp.close();

                sqlQuery = "Insert Into overheadmast(code ,name,manualCode, u_EntDt, preparedBy)"
                        + " Values (:code ,:name,:manualCode, :uEntDt, :preparedBy) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(mrTemp.getCode()));
                psTemp.setString("name", DmShared.xNull(mrTemp.getName()));
                psTemp.setString("manualCode", DmShared.xNull(mrTemp.getManualCode()));
                psTemp.setString("preparedBy", DmServer.getSsnUserName(rss));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.executeUpdate();
                psTemp.close();
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
