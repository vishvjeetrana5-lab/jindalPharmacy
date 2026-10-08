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
import dataman.dmBase.self.shared.KeyField;
import dataman.dmToolkit.dmImportData.shared.DmImportStatus;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ImportCity implements DmImportData_Interface {

    class Model extends KeyField {

        private String stateTag;
        private String stateText;
        private String countryTag;
        private String countryText;
        private String stdCode;
        private String stateNumericCode;
        private String isUT;
        private String stateTextCode;

        public String getStdCode() {
            return stdCode;
        }

        public void setStdCode(String stdCode) {
            this.stdCode = stdCode;
        }

        public String getStateTag() {
            return stateTag;
        }

        public void setStateTag(String stateTag) {
            this.stateTag = stateTag;
        }

        public String getStateText() {
            return stateText;
        }

        public void setStateText(String stateText) {
            this.stateText = stateText;
        }

        public String getCountryTag() {
            return countryTag;
        }

        public void setCountryTag(String countryTag) {
            this.countryTag = countryTag;
        }

        public String getCountryText() {
            return countryText;
        }

        public void setCountryText(String countryText) {
            this.countryText = countryText;
        }

        public String getStateNumericCode() {
            return stateNumericCode;
        }

        public void setStateNumericCode(String stateNumericCode) {
            this.stateNumericCode = stateNumericCode;
        }

        public String getStateTextCode() {
            return stateTextCode;
        }

        public void setStateTextCode(String stateTextCode) {
            this.stateTextCode = stateTextCode;
        }

        public String getIsUT() {
            return isUT;
        }

        public void setIsUT(String isUT) {
            this.isUT = isUT;
        }

    }
    private DmRSS rss;
    private DmHandelFile handelFileLog;
    private String LineNoDisplay = "";

    public ImportCity() {
    }

    @Override
    public ImportData_Mdl getImportDataModel() {
        ImportData_Mdl fileDetail = new ImportData_Mdl();
        CSVData node;
        List<CSVData> lstCSV = new ArrayList<CSVData>();

        node = new CSVData();
        node.setHeader("countryName");
        node.setSample("India");
        node.setInstruction("{mandatory;25c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("stateName");
        node.setSample("Andhra Pradesh");
        node.setInstruction("{mandatory;30c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("unionTerritory");
        node.setSample("No");
        node.setInstruction("{mandatory;(Yes/No);alphanumeric;3c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("stateNumericCode");
        node.setSample("28");
        node.setInstruction("{mandatory;numeric;2c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("stateTextCode");
        node.setSample("AP");
        node.setInstruction("{mandatory;alphanumeric;2c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("cityName");
        node.setSample("kanpur");
        node.setInstruction("{mandatory;alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("STDCode");
        node.setSample("208004");
        node.setInstruction("{alphanumeric;15c}");
        lstCSV.add(node);

        fileDetail.getHmCSV().addAll(lstCSV);
        fileDetail.setImportTo("City Master");
        fileDetail.setServerClassName(ImportCity.class.getName());

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
                        if (headerName.trim().equalsIgnoreCase("countryName")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 25, LineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setCountryText(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("stateName")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 30, LineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setStateText(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("unionTerritory")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 3, LineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setIsUT(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("stateNumericCode")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, true, true, false, false, false, false, 2, LineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setStateNumericCode(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("stateTextCode")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 2, LineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setStateTextCode(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("cityName")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 50, LineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setSharedKeyFieldName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("STDCode")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 15, LineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setStdCode(fieldValue);
                            }
                        }
                    }
                    if (isFound) {
                        status = saveCityMaster(mrTemp);
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

    private int saveCityMaster(Model mrTemp) {
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        String stateCode, countryCode;
        int status = 0, rtnStatus = 0;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoInteger(rss, "city", cnTemp));
            if (DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new UnsupportedOperationException("Invalid key.");
            }

            countryCode = saveCountry(mrTemp.getCountryText(), cnTemp);
            stateCode = saveState(mrTemp, cnTemp);

            sqlQuery = "SELECT COUNT(cityName) AS cnt FROM city "
                    + "WHERE (stateCode =:stateCode) AND (countryCode=:countryCode) "
                    + "AND LOWER(cityName)=:cityName AND (cityCode<>:cityCode)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("cityCode", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.setString("cityName", DmShared.xNull(mrTemp.getSharedKeyFieldName()).toLowerCase());
            psTemp.setString("stateCode", DmShared.iNull(stateCode).toString());
            psTemp.setString("countryCode", DmShared.iNull(countryCode).toString());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                status = DmImportStatus.WARNING.getId();
                throw new DmException(400, "City is already exists.");
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "Insert Into City(CityCode, CityName, stateCode, stdCode, countryCode, U_EntDt, preparedBy) "
                    + " Values (:cityCode, :cityName, :stateCode, :stdCode, :countryCode, :U_EntDt, :u_Name) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("cityCode", mrTemp.getSharedKeyFieldCode());
            psTemp.setString("cityName", mrTemp.getSharedKeyFieldName());
            psTemp.setString("stateCode", DmShared.foriegnKey(stateCode));
            psTemp.setString("countryCode", DmShared.foriegnKey(countryCode));
            psTemp.setString("stdCode", mrTemp.getStdCode());
            psTemp.setString("u_Name", DmServer.getSsnUserName(rss));
            psTemp.setTimestamp("U_EntDt", DmServer.getServerTimestamp());
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
                handelFileLog.writeLine(LineNoDisplay + e.getMessage());
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

    private String saveCountry(String countryName, Connection cnTemp) {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, countryCode = "";
        try {
            countryCode = DmServer.getCodeViaManualCode(countryName, "name", "country", "code", cnTemp);

            if (DmShared.iNull(countryCode).intValue() == 0) {
                countryCode = DmServer.getSerialNoInteger(rss, "country", cnTemp);
                sqlQuery = "insert into country(code,name,U_EntDt, preparedBy)"
                        + "values(:code,:name,:U_EntDt, :preparedBy)";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", countryCode);
                psTemp.setString("name", countryName);
                psTemp.setTimestamp("U_EntDt", DmServer.getServerTimestamp());
                psTemp.setString("preparedBy", DmServer.getSsnUserName(rss));
                psTemp.executeUpdate();
                psTemp.close();
            }
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
        return countryCode;
    }

    private String saveState(Model mrTemp, Connection cnTemp) {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, stateCode = "", yesNo = "";

        try {
            stateCode = DmServer.getCodeViaManualCode(mrTemp.getStateText(), "name", "stateMast", "code", cnTemp);

            if (DmShared.iNull(stateCode).intValue() == 0) {
                if (DmShared.xNull(mrTemp.getIsUT()).equalsIgnoreCase("Yes")) {
                    yesNo = "1";
                } else if (DmShared.xNull(mrTemp.getIsUT()).equalsIgnoreCase("No")) {
                    yesNo = "0";
                }
                stateCode = DmServer.getSerialNoInteger(rss, "stateMast", cnTemp);
                sqlQuery = "insert into stateMast(code,name,numericCode,textCode, isUT, preparedBy, u_EntDt)"
                        + "values(:code,:name,:numericCode,:textCode, :isUT, :preparedBy, :u_EntDt)";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", stateCode);
                psTemp.setString("name", mrTemp.getStateText());
                psTemp.setString("numericCode", mrTemp.getStateNumericCode());
                psTemp.setString("textCode", mrTemp.getStateTextCode());
                psTemp.setString("isUT", yesNo);
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.setString("preparedBy", DmServer.getSsnUserName(rss));
                psTemp.executeUpdate();
                psTemp.close();
            }
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
        return stateCode;
    }
}
