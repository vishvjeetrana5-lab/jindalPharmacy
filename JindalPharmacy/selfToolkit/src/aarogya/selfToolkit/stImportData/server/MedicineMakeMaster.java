package aarogya.selfToolkit.stImportData.server;

import dataman.dmToolkit.dmImportData.server.DmImportData_Interface;
import dataman.dmToolkit.dmImportData.shared.CSVData;
import dataman.dmToolkit.dmImportData.shared.ImportData_Mdl;
import dataman.dmBase.self.shared.DmDynamicGridRow;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
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

public class MedicineMakeMaster implements DmImportData_Interface {

    class Model {

        private String code, companyName, shortName, lclPerson, lclMobile, lclFax, lclRPhone, lclOPhone,
                lclEmail, lclAddress1, lclAddress2, regPerson, regMobile, regFax, regRPhone, regOPhone, regEmail,
                regAddress1, regAddress2, natPerson, natMobile, natFax, natRPhone, natOPhone, natEmail,
                natAddress1, natAddress2, purchaseDiscountPer;

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getShortName() {
            return shortName;
        }

        public void setShortName(String shortName) {
            this.shortName = shortName;
        }

        public String getLclPerson() {
            return lclPerson;
        }

        public void setLclPerson(String lclPerson) {
            this.lclPerson = lclPerson;
        }

        public String getLclMobile() {
            return lclMobile;
        }

        public void setLclMobile(String lclMobile) {
            this.lclMobile = lclMobile;
        }

        public String getLclFax() {
            return lclFax;
        }

        public void setLclFax(String lclFax) {
            this.lclFax = lclFax;
        }

        public String getLclRPhone() {
            return lclRPhone;
        }

        public void setLclRPhone(String lclRPhone) {
            this.lclRPhone = lclRPhone;
        }

        public String getLclOPhone() {
            return lclOPhone;
        }

        public void setLclOPhone(String lclOPhone) {
            this.lclOPhone = lclOPhone;
        }

        public String getLclEmail() {
            return lclEmail;
        }

        public void setLclEmail(String lclEmail) {
            this.lclEmail = lclEmail;
        }

        public String getLclAddress1() {
            return lclAddress1;
        }

        public void setLclAddress1(String lclAddress1) {
            this.lclAddress1 = lclAddress1;
        }

        public String getLclAddress2() {
            return lclAddress2;
        }

        public void setLclAddress2(String lclAddress2) {
            this.lclAddress2 = lclAddress2;
        }

        public String getRegPerson() {
            return regPerson;
        }

        public void setRegPerson(String regPerson) {
            this.regPerson = regPerson;
        }

        public String getRegMobile() {
            return regMobile;
        }

        public void setRegMobile(String regMobile) {
            this.regMobile = regMobile;
        }

        public String getRegFax() {
            return regFax;
        }

        public void setRegFax(String regFax) {
            this.regFax = regFax;
        }

        public String getRegRPhone() {
            return regRPhone;
        }

        public void setRegRPhone(String regRPhone) {
            this.regRPhone = regRPhone;
        }

        public String getRegOPhone() {
            return regOPhone;
        }

        public void setRegOPhone(String regOPhone) {
            this.regOPhone = regOPhone;
        }

        public String getRegEmail() {
            return regEmail;
        }

        public void setRegEmail(String regEmail) {
            this.regEmail = regEmail;
        }

        public String getRegAddress1() {
            return regAddress1;
        }

        public void setRegAddress1(String regAddress1) {
            this.regAddress1 = regAddress1;
        }

        public String getRegAddress2() {
            return regAddress2;
        }

        public void setRegAddress2(String regAddress2) {
            this.regAddress2 = regAddress2;
        }

        public String getNatPerson() {
            return natPerson;
        }

        public void setNatPerson(String natPerson) {
            this.natPerson = natPerson;
        }

        public String getNatMobile() {
            return natMobile;
        }

        public void setNatMobile(String natMobile) {
            this.natMobile = natMobile;
        }

        public String getNatFax() {
            return natFax;
        }

        public void setNatFax(String natFax) {
            this.natFax = natFax;
        }

        public String getNatRPhone() {
            return natRPhone;
        }

        public void setNatRPhone(String natRPhone) {
            this.natRPhone = natRPhone;
        }

        public String getNatOPhone() {
            return natOPhone;
        }

        public void setNatOPhone(String natOPhone) {
            this.natOPhone = natOPhone;
        }

        public String getNatEmail() {
            return natEmail;
        }

        public void setNatEmail(String natEmail) {
            this.natEmail = natEmail;
        }

        public String getNatAddress1() {
            return natAddress1;
        }

        public void setNatAddress1(String natAddress1) {
            this.natAddress1 = natAddress1;
        }

        public String getNatAddress2() {
            return natAddress2;
        }

        public void setNatAddress2(String natAddress2) {
            this.natAddress2 = natAddress2;
        }

        public Model() {
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getPurchaseDiscountPer() {
            return purchaseDiscountPer;
        }

        public void setPurchaseDiscountPer(String purchaseDiscountPer) {
            this.purchaseDiscountPer = purchaseDiscountPer;
        }

    }

    private DmRSSPrivate rss;
    private DmHandelFile handelFileLog;

    private String lineNoDisplay = "";

    public MedicineMakeMaster() {
    }

    @Override
    public ImportData_Mdl getImportDataModel() {
        ImportData_Mdl fileDetail = new ImportData_Mdl();
        CSVData node;
        List<CSVData> lstCSV = new ArrayList<CSVData>();

        node = new CSVData();
        node.setHeader("companyName");
        node.setSample("Dataman");
        node.setInstruction("{unique;mandatory;alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("shortName");
        node.setSample("Dm");
        node.setInstruction("{unique;mandatory;alphanumeric;20c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("purchaseDiscountPer");
        node.setSample("5");
        node.setInstruction("{numeric;4c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("localPerson");
        node.setSample("mohit");
        node.setInstruction("{alphanumeric;35c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("localMobile");
        node.setSample("8655984587");
        node.setInstruction("{numeric;30c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("localFax");
        node.setSample("4");
        node.setInstruction("{alphanumeric;30c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("localRPhone");
        node.setSample("4521548545");
        node.setInstruction("{numeric;30c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("localOPhone");
        node.setSample("4785198745");
        node.setInstruction("{numeric;30c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("localEmail");
        node.setSample("trimohit6@gmail.com");
        node.setInstruction("{alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("localAddress1");
        node.setSample("Mall Raod");
        node.setInstruction("{alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("localAddress2");
        node.setSample("Bara Bazar");
        node.setInstruction("{alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("regPerson");
        node.setSample("Ram");
        node.setInstruction("{alphanumeric;35c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("regMobile");
        node.setSample("4452365115");
        node.setInstruction("{numeric;30c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("regFax");
        node.setSample("5");
        node.setInstruction("{alphanumeric;30c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("regRPhone");
        node.setSample("5512652415");
        node.setInstruction("{numeric;30c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("regOPhone");
        node.setSample("4599552154");
        node.setInstruction("{numeric;30c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("regEmail");
        node.setSample("tri@gmail.com");
        node.setInstruction("{alphanumeric;30c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("regAddress1");
        node.setSample("CP");
        node.setInstruction("{alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("regAddress2");
        node.setSample("Civil Lines");
        node.setInstruction("{alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("natPerson");
        node.setSample("shyam");
        node.setInstruction("{alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("natMobile");
        node.setSample("8845627854");
        node.setInstruction("{numeric;30c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("natFax");
        node.setSample("8");
        node.setInstruction("{numeric;30c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("natRPhone");
        node.setSample("7754125984");
        node.setInstruction("{numeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("natOPhone");
        node.setSample("8955125478");
        node.setInstruction("{numeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("natEmail");
        node.setSample("mohit5@gmail.com");
        node.setInstruction("{alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("natAddress1");
        node.setSample("Sipri");
        node.setInstruction("{alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("natAddress2");
        node.setSample("Sadar");
        node.setInstruction("{alphanumeric;100c}");
        lstCSV.add(node);

        fileDetail.getHmCSV().addAll(lstCSV);
        fileDetail.setImportTo("Medicine Company Master");
        fileDetail.setServerClassName(MedicineMakeMaster.class.getName());

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
                        if (headerName.trim().equalsIgnoreCase("companyName")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setCompanyName(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("shortName")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 20, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setShortName(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("purchaseDiscountPer")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 4, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPurchaseDiscountPer(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("localPerson")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 35, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setLclPerson(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("localMobile")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 30, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setLclMobile(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("localFax")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 30, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setLclFax(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("localRPhone")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 30, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setLclRPhone(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("localOPhone")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 30, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setLclOPhone(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("localEmail")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, true, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setLclEmail(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("localAddress1")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setLclAddress1(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("localAddress2")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setLclAddress2(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("regPerson")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 35, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setRegPerson(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("regMobile")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 30, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setRegMobile(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("regFax")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 30, lineNoDisplay, handelFileLog)) {

                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setRegFax(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("regRPhone")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 30, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setRegRPhone(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("regOPhone")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 30, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setRegOPhone(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("regEmail")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, true, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setRegEmail(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("regAddress1")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setRegAddress1(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("regAddress2")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setRegAddress2(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("natPerson")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 35, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setNatPerson(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("natMobile")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 30, lineNoDisplay, handelFileLog)) {

                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setNatMobile(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("natFax")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 30, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setNatFax(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("natRPhone")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 30, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setNatRPhone(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("natOPhone")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 30, lineNoDisplay, handelFileLog)) {

                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setNatOPhone(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("natEmail")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, true, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setNatEmail(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("natAddress1")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {

                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setNatAddress1(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("natAddress2")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setNatAddress2(DmShared.removeSpace(fieldValue));
                            }
                        }
                    }
                    if (isFound) {
                        status = saveMedicineMakeMast(mrTemp);
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

    private int saveMedicineMakeMast(Model mrTemp) {
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        int status = 0, rtnStatus = 0;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setCompanyName(DmShared.removeSpace(mrTemp.getCompanyName()));
            mrTemp.setCode(DmServer.getSerialNoInteger(rss, "itemMakeMast", cnTemp));
            if (DmShared.iNull(mrTemp.getCode()).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new UnsupportedOperationException("Invalid key.");
            }

            sqlQuery = "select COUNT(imm.companyName) as cnt from itemMakeMast as imm  where imm.companyName = :name  AND imm.code <> :code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getCompanyName()));
            psTemp.setString("code", DmShared.xNull(mrTemp.getCode()));
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                status = DmImportStatus.WARNING.getId();
                throw new DmException(0, DmShared.xNull(mrTemp.getCompanyName()) + " Name already exists.");
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "SELECT COUNT(shortName) AS cnt FROM itemMakeMast WHERE shortName = :shortName AND code <> :code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("shortName", DmShared.xNull(mrTemp.getShortName()).toLowerCase().trim());
            psTemp.setString("code", DmShared.xNull(mrTemp.getCode()));
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                status = DmImportStatus.WARNING.getId();
                throw new UnsupportedOperationException("Short Name already exists.");
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "INSERT INTO itemMakeMast (CODE, companyName, shortName,filterItemType, "
                    + "  lclPerson, lclMobile, lclFax, lclRPhone, lclOPhone, lclEmail, lclAddress1, lclAddress2,"
                    + "  regPerson, regMobile, regFax, regRPhone, regOPhone, regEmail, regAddress1, regAddress2,"
                    + "  natPerson, natMobile, natFax, natRPhone, natOPhone, natEmail, natAddress1, natAddress2,"
                    + "  preparedBY, u_EntDt, transferDt,purchaseDiscountPer) "
                    + "  VALUES(:code, :companyName, :shortName, :filterItemType,"
                    + "  :lclPerson, :lclMobile, :lclFax, :lclRPhone, :lclOPhone, :lclEmail, :lclAddress1, :lclAddress2,"
                    + "  :regPerson, :regMobile, :regFax, :regRPhone, :regOPhone, :regEmail, :regAddress1, :regAddress2,"
                    + "  :natPerson, :natMobile, :natFax, :natRPhone, :natOPhone, :natEmail, :natAddress1, :natAddress2,"
                    + "  :preparedBY, :u_EntDt, NULL,:purchaseDiscountPer) ;";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", mrTemp.getCode());
            psTemp.setString("companyName", mrTemp.getCompanyName());
            psTemp.setString("shortName", mrTemp.getShortName());
            psTemp.setString("filterItemType", DmShared.foriegnKey(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            psTemp.setString("lclPerson", mrTemp.getLclPerson());
            psTemp.setString("lclMobile", mrTemp.getLclMobile());
            psTemp.setString("lclFax", mrTemp.getLclFax());
            psTemp.setString("lclRPhone", mrTemp.getLclRPhone());
            psTemp.setString("lclOPhone", mrTemp.getLclOPhone());
            psTemp.setString("lclEmail", mrTemp.getLclEmail());
            psTemp.setString("lclAddress1", mrTemp.getLclAddress1());
            psTemp.setString("lclAddress2", mrTemp.getLclAddress2());
            psTemp.setString("regPerson", mrTemp.getRegPerson());
            psTemp.setString("regMobile", mrTemp.getRegMobile());
            psTemp.setString("regFax", mrTemp.getRegFax());
            psTemp.setString("regRPhone", mrTemp.getRegRPhone());
            psTemp.setString("regOPhone", mrTemp.getRegOPhone());
            psTemp.setString("regEmail", mrTemp.getRegEmail());
            psTemp.setString("regAddress1", mrTemp.getRegAddress1());
            psTemp.setString("regAddress2", mrTemp.getRegAddress2());
            psTemp.setString("natPerson", mrTemp.getNatPerson());
            psTemp.setString("natMobile", mrTemp.getNatMobile());
            psTemp.setString("natFax", mrTemp.getNatFax());
            psTemp.setString("natRPhone", mrTemp.getNatRPhone());
            psTemp.setString("natOPhone", mrTemp.getNatOPhone());
            psTemp.setString("natEmail", mrTemp.getNatEmail());
            psTemp.setString("natAddress1", mrTemp.getNatAddress1());
            psTemp.setString("natAddress2", mrTemp.getNatAddress2());
            psTemp.setString("preparedBY", DmServer.getSsnUserName(rss));
            psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
            psTemp.setString("purchaseDiscountPer", DmServer.vNull(mrTemp.getPurchaseDiscountPer()));

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
