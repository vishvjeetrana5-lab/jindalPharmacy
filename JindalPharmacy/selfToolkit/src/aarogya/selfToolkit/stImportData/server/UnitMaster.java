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

public class UnitMaster implements DmImportData_Interface {

    class Model {

        private String code, name, decimalPlaces, GSTUnit, GSTUnitCode;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDecimalPlaces() {
            return decimalPlaces;
        }

        public void setDecimalPlaces(String decimalPlaces) {
            this.decimalPlaces = decimalPlaces;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGSTUnit() {
            return GSTUnit;
        }

        public void setGSTUnit(String GSTUnit) {
            this.GSTUnit = GSTUnit;
        }

        public Model() {
        }

        public String getGSTUnitCode() {
            return GSTUnitCode;
        }

        public void setGSTUnitCode(String GSTUnitCode) {
            this.GSTUnitCode = GSTUnitCode;
        }

    }

    private DmRSS rss;
    private DmHandelFile handelFileLog;

    private String lineNoDisplay = "";

    public UnitMaster() {
    }

    @Override
    public ImportData_Mdl getImportDataModel() {
        ImportData_Mdl fileDetail = new ImportData_Mdl();
        List<CSVData> lstCSV = new ArrayList<CSVData>();
        CSVData node;

        node = new CSVData();
        node.setHeader("name");
        node.setSample("Gram");
        node.setInstruction("{unique;mandatory;alphanumeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("decimalPlaces");
        node.setSample("5");
        node.setInstruction("{mandatory;numeric;1c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("gstUnit");
        node.setSample("Others");
        node.setInstruction("{alphanumeric;20c}");
        lstCSV.add(node);

        fileDetail.getHmCSV().addAll(lstCSV);
        fileDetail.setImportTo("Unit Master");
        fileDetail.setServerClassName(UnitMaster.class.getName());

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
                        if (headerName.trim().equalsIgnoreCase("name")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("decimalPlaces")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 1, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setDecimalPlaces(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("gstUnit")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 20, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setGSTUnit(fieldValue);
                            }
                        }
                    }
                    if (isFound) {
                        status = saveUnitMast(mrTemp);
                    }
                    if (status > rtnStatus) {
                        rtnStatus = status;
                    }
                }
            }
            isFound = true;
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

    private int saveUnitMast(Model mrTemp) {
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, gstUnitCode = "";
        int status = 0, rtnStatus = 0;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setName(DmShared.removeSpace(mrTemp.getName()));
            mrTemp.setCode(DmServer.getSerialNoInteger(rss, "unitMast", cnTemp));
            if (DmShared.iNull(mrTemp.getCode()).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new UnsupportedOperationException("Invalid key.");
            }

            sqlQuery = "select COUNT(um.unit) as cnt from unitMast as um  where um.unit = :name  AND um.code <> :code";
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

            if (!DmShared.xNull(mrTemp.getGSTUnit()).equalsIgnoreCase("")) {
                mrTemp.setGSTUnitCode(DmServer.getCodeViaManualCode(mrTemp.getGSTUnit(), "name", "gstUnitMast", "code", cnTemp));

            }
            gstUnitCode = DmServer.getCodeViaManualCode("OTH", "manualCode", "gstUnitMast", "code", cnTemp);

            sqlQuery = "INSERT INTO unitMast (code, unit,decimalPlaces,gstUnitCode,preparedBY,u_EntDt)"
                    + "  VALUES(:code, :unit,:decimalPlaces,:gstUnitCode,:preparedBY,:u_EntDt);";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", mrTemp.getCode());
            psTemp.setString("unit", mrTemp.getName());
            psTemp.setString("decimalPlaces", DmServer.vNull(mrTemp.getDecimalPlaces(), "0"));
            if (DmShared.iNull(mrTemp.getGSTUnitCode()).intValue() > 0) {
                psTemp.setString("gstUnitCode", DmShared.foriegnKey(mrTemp.getGSTUnitCode()));
            } else {
                psTemp.setString("gstUnitCode", DmShared.foriegnKey(gstUnitCode));
            }
            psTemp.setString("preparedBY", DmServer.getSsnUserName(rss));
            psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());

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

                if (cnTemp != null) {
                    cnTemp.close();
                }

            } catch (Exception e) {
            }
        }
        return rtnStatus;
    }
}
