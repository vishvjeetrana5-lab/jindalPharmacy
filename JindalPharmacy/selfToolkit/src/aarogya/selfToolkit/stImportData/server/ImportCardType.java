package aarogya.selfToolkit.stImportData.server;

import dataman.dmToolkit.dmImportData.server.DmImportData_Interface;
import dataman.dmToolkit.dmImportData.shared.*;
import dataman.dmBase.self.shared.DmDynamicGridRow;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSS;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.server.DmCsvReader;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.self.shared.DmException;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.server.DmHandelFile;
import dataman.dmBase.self.shared.DmEnum;

public class ImportCardType implements DmImportData_Interface {

    class Model {

        private String code, name, shortName;
        private String linkAccount;
        private String nature;
        private String onAmount;
        private String points;
        private String valuePerPoint;
        private String validDays;
        private String emailAlert;
        private String smsAlert;
        private String physicallyRequired;
        private String cardType;
        private String currencyCode;

        public Model() {
        }

        public String getCode() {
            return code;
        }

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
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

        public String getShortName() {
            return shortName;
        }

        public void setShortName(String shortName) {
            this.shortName = shortName;
        }

        public String getLinkAccount() {
            return linkAccount;
        }

        public void setLinkAccount(String linkAccount) {
            this.linkAccount = linkAccount;
        }

        public String getNature() {
            return nature;
        }

        public void setNature(String nature) {
            this.nature = nature;
        }

        public String getOnAmount() {
            return onAmount;
        }

        public void setOnAmount(String onAmount) {
            this.onAmount = onAmount;
        }

        public String getPoints() {
            return points;
        }

        public void setPoints(String points) {
            this.points = points;
        }

        public String getValuePerPoint() {
            return valuePerPoint;
        }

        public void setValuePerPoint(String valuePerPoint) {
            this.valuePerPoint = valuePerPoint;
        }

        public String getValidDays() {
            return validDays;
        }

        public void setValidDays(String validDays) {
            this.validDays = validDays;
        }

        public String getEmailAlert() {
            return emailAlert;
        }

        public void setEmailAlert(String emailAlert) {
            this.emailAlert = emailAlert;
        }

        public String getSmsAlert() {
            return smsAlert;
        }

        public void setSmsAlert(String smsAlert) {
            this.smsAlert = smsAlert;
        }

        public String getPhysicallyRequired() {
            return physicallyRequired;
        }

        public void setPhysicallyRequired(String physicallyRequired) {
            this.physicallyRequired = physicallyRequired;
        }

        public String getCardType() {
            return cardType;
        }

        public void setCardType(String cardType) {
            this.cardType = cardType;
        }
    }

    private DmRSS rss;
    private DmHandelFile handelFileLog;
    private String lineNoDisplay = "";

    public ImportCardType() {
    }

    @Override
    public ImportData_Mdl getImportDataModel() {
        final ImportData_Mdl fileDetail = new ImportData_Mdl();
        CSVData node;
        List<CSVData> lstCSV = new ArrayList<CSVData>();

        node = new CSVData();
        node.setHeader("name");
        node.setSample("Cash");
        node.setInstruction("{unique;mandatory;alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("shortName");
        node.setSample("CP");
        node.setInstruction("{unique;mandatory;alphanumeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("cardType");
        node.setSample("Cash");
        node.setInstruction("{mandatory;alphanumeric;(Cash/Adjust/Credit Card/Bank/Digital/Wallet);15c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("nature");
        node.setSample("Cash");
        node.setInstruction("{mandatory;alphanumeric;(Value/Point);15c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("linkAccount");
        node.setSample("Cash in hand");
        node.setInstruction("{mandatory;alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("onAmount");
        node.setSample("500");
        node.setInstruction("{numeric;}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("points");
        node.setSample("10");
        node.setInstruction("{numeric;}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("valuePerPoint");
        node.setSample("10");
        node.setInstruction("{numeric;}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("validDays");
        node.setSample("10");
        node.setInstruction("{numeric;}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("EmailAlert");
        node.setSample("Yes");
        node.setInstruction("mandatory;{alphanumeric;(Yes/No);3c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("SMSAlert");
        node.setSample("Yes");
        node.setInstruction("mandatory;{alphanumeric;(Yes/No);3c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("Physically Required");
        node.setSample("Yes");
        node.setInstruction("mandatory;{alphanumeric;(Yes/No);3c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("Currency");
        node.setSample("Cash in hand");
        node.setInstruction("{mandatory;alphanumeric;100c}");
        lstCSV.add(node);

        fileDetail.getHmCSV().addAll(lstCSV);
        fileDetail.setImportTo("Card Type Master");
        fileDetail.setServerClassName(ImportCardType.class.getName());

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
                        if (headerName.trim().equalsIgnoreCase("Name")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("shortName")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setShortName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("cardType")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setCardType(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("nature")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setNature(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("linkAccount")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setLinkAccount(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("onAmount")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 11, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setOnAmount(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("points")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPoints(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("valuePerPoint")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setValuePerPoint(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("validDays")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 3, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setValidDays(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("EmailAlert")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setEmailAlert(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("SMSAlert")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setSmsAlert(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("Physically Required")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPhysicallyRequired(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("Currency")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 20, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setCurrencyCode(fieldValue);
                            }
                        }
                    }
                    if (isFound) {
                        status = saveCard(mrTemp);
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

    private int saveCard(Model mrTemp) {
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmEnum.CardType cardType = null;
        boolean isAdd = false;
        String sqlQuery;
        int status = 0, rtnStatus = 0;
        try {

            mrTemp.setName(DmShared.removeSpace(mrTemp.getName()));
            mrTemp.setShortName(DmShared.removeSpace(mrTemp.getShortName()));
            mrTemp.setCardType(DmShared.removeSpace(mrTemp.getCardType()));
            mrTemp.setNature(DmShared.removeSpace(mrTemp.getNature()));
            cardType = DmEnum.CardType.getValueOf(DmShared.xNull(mrTemp.getCardType()));
            if (cardType == null) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, "Card Type On not found for " + mrTemp.getName());
            }

            mrTemp.setEmailAlert(DmShared.xNull(mrTemp.getEmailAlert()).trim().equalsIgnoreCase("Yes") ? "1" : "0");
            mrTemp.setPhysicallyRequired(DmShared.xNull(mrTemp.getPhysicallyRequired()).trim().equalsIgnoreCase("Yes") ? "1" : "0");
            mrTemp.setSmsAlert(DmShared.xNull(mrTemp.getSmsAlert()).trim().equalsIgnoreCase("Yes") ? "1" : "0");

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setCode(DmServer.getCodeViaManualCode(mrTemp.getName(), "name", "cardTypeMast", "code", cnTemp));
            if (DmShared.iNull(mrTemp.getCode()).longValue() == 0) {
                isAdd = true;
                mrTemp.setCode(DmServer.getSerialNoInteger(rss, "cardTypeMast", cnTemp));
            }

            if (DmShared.iNull(mrTemp.getCode()).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new UnsupportedOperationException("Invalid key.");
            }

            sqlQuery = "select code from cardTypeMast cc where cc.name = :name and cc.code <> :code;";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", mrTemp.getCode());
            psTemp.setString("name", mrTemp.getName());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                status = DmImportStatus.WARNING.getId();
                throw new UnsupportedOperationException(mrTemp.getName() + " card type already exists.");
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "select code from cardTypeMast cc where cc.shortName = :shortName and cc.code <> :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", mrTemp.getCode());
            psTemp.setString("shortName", mrTemp.getShortName());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                status = DmImportStatus.WARNING.getId();
                throw new UnsupportedOperationException(mrTemp.getShortName() + " short name already exists.");
            }
            rsTemp.close();
            psTemp.close();

            mrTemp.setLinkAccount(DmServer.getCodeViaManualCode(mrTemp.getLinkAccount(), "name", "subGroup", "subCode", cnTemp));
            if (DmShared.iNull(mrTemp.getLinkAccount()).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, "Link A/c not found for " + mrTemp.getName());
            }

            mrTemp.setCurrencyCode(DmServer.getCodeViaManualCode(mrTemp.getCurrencyCode(), "name", "currency", "code", cnTemp));
            if (DmShared.iNull(mrTemp.getCurrencyCode()).longValue() == 0) {
                status = DmImportStatus.WARNING.getId();
                handelFileLog.writeLine(mrTemp.getCurrencyCode() + " Currency not found for " + mrTemp.getName());
            }

            if (isAdd) {
                sqlQuery = "Insert Into cardTypeMast(Code, Name, shortName, Nature, isActive, isPhysicallyRequired, isSmsAlert, isEmailAlert, SubCode, "
                        + "         preparedBy, U_EntDt, OnAmount, ValuePerPoint, Points, cardType, validDays,currencyCode) "
                        + " Values(:Code, :Name, :shortName, :Nature, :isActive, :isPhysicallyRequired, :isSmsAlert, :isEmailAlert, :SubCode, :U_Name, "
                        + "         :uEntDt, :OnAmount, :ValuePerPoint, :Points, :cardType, :validDays,:currencyCode) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("Code", DmShared.xNull(mrTemp.getCode()));
                psTemp.setString("Name", DmShared.xNull(mrTemp.getName()));
                psTemp.setString("shortName", DmShared.xNull(mrTemp.getShortName()));
                psTemp.setString("Nature", DmShared.xNull(mrTemp.getNature()));
                psTemp.setString("isActive", "1");
                psTemp.setString("isPhysicallyRequired", mrTemp.getPhysicallyRequired());
                psTemp.setString("SubCode", DmShared.foriegnKey(mrTemp.getLinkAccount()));
                psTemp.setString("OnAmount", DmServer.vNull(mrTemp.getOnAmount(), "0.00"));
                psTemp.setString("ValuePerPoint", DmServer.vNull(mrTemp.getValuePerPoint(), "0.00"));
                psTemp.setString("Points", DmServer.vNull(mrTemp.getPoints(), "0.00"));
                psTemp.setString("u_Name", DmServer.getSsnUserName(rss));
                psTemp.setString("cardType", DmShared.iNull(cardType.getEnumValue()).toString());
                psTemp.setString("isSmsAlert", DmShared.xNull(mrTemp.getSmsAlert()));
                psTemp.setString("isEmailAlert", DmShared.xNull(mrTemp.getEmailAlert()));
                psTemp.setString("validDays", DmShared.xNull(mrTemp.getValidDays()));
                psTemp.setString("currencyCode", DmShared.foriegnKey(mrTemp.getCurrencyCode()));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            } else {
                sqlQuery = "update  cardTypeMast set "
                        + "         name = :name, "
                        + "         shortName = :shortName, "
                        + "         nature = :nature, "
                        + "         isPhysicallyRequired = :isPhysicallyRequired, "
                        + "         subCode = :subCode,"
                        + "         onAmount = :onAmount,"
                        + "         valuePerPoint = :valuePerPoint,"
                        + "         points = :points,"
                        + "         u_Name = :userName,"
                        + "         u_EntDt = :uEntDt,"
                        + "         cardType = :cardType, "
                        + "         isSmsAlert = :isSmsAlert, "
                        + "         isEmailAlert = :isEmailAlert,"
                        + "         validDays= :validDays, "
                        + "         currencyCode = :currencyCode "
                        + "         where code = :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(mrTemp.getCode()));
                psTemp.setString("name", DmShared.xNull(mrTemp.getName()));
                psTemp.setString("shortName", DmShared.xNull(mrTemp.getShortName()));
                psTemp.setString("Nature", DmShared.xNull(mrTemp.getNature()));
                psTemp.setString("isPhysicallyRequired", mrTemp.getPhysicallyRequired());
                psTemp.setString("SubCode", DmShared.foriegnKey(mrTemp.getLinkAccount()));
                psTemp.setString("OnAmount", DmServer.vNull(mrTemp.getOnAmount(), "0.00"));
                psTemp.setString("ValuePerPoint", DmServer.vNull(mrTemp.getValuePerPoint(), "0.00"));
                psTemp.setString("Points", DmServer.vNull(mrTemp.getPoints(), "0.00"));
                psTemp.setString("userName", DmServer.getSsnUserName(rss));
                psTemp.setInt("cardType", cardType.getEnumValue());
                psTemp.setString("isSmsAlert", mrTemp.getSmsAlert());
                psTemp.setString("isEmailAlert", mrTemp.getEmailAlert());
                psTemp.setString("validDays", DmShared.xNull(mrTemp.getValidDays()));
                psTemp.setString("currencyCode", DmShared.foriegnKey(mrTemp.getCurrencyCode()));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            }
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
