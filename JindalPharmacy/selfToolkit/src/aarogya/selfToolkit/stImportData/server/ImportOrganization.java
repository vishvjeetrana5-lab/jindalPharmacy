package aarogya.selfToolkit.stImportData.server;

import aarogya.selfToolkit.stStatic.shared.STEnum.MedicineSaleOn;
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

public class ImportOrganization implements DmImportData_Interface {

    class Model {

        private String code, name, shortName;
        private String serviceDiscountPer;
        private String testDiscountPer;
        private String feesDiscountPer;
        private String pharmacyDiscountPer;
        private String gstinNo;
        private String nature;
        private String medicineSaleOn;
        private String pharmacyDiscount;
        private String tdsAccount;

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

        public String getShortName() {
            return shortName;
        }

        public void setShortName(String shortName) {
            this.shortName = shortName;
        }

        public String getServiceDiscountPer() {
            return serviceDiscountPer;
        }

        public void setServiceDiscountPer(String serviceDiscountPer) {
            this.serviceDiscountPer = serviceDiscountPer;
        }

        public String getTestDiscountPer() {
            return testDiscountPer;
        }

        public void setTestDiscountPer(String testDiscountPer) {
            this.testDiscountPer = testDiscountPer;
        }

        public String getFeesDiscountPer() {
            return feesDiscountPer;
        }

        public void setFeesDiscountPer(String feesDiscountPer) {
            this.feesDiscountPer = feesDiscountPer;
        }

        public String getPharmacyDiscountPer() {
            return pharmacyDiscountPer;
        }

        public void setPharmacyDiscountPer(String pharmacyDiscountPer) {
            this.pharmacyDiscountPer = pharmacyDiscountPer;
        }

        public String getGstinNo() {
            return gstinNo;
        }

        public void setGstinNo(String gstinNo) {
            this.gstinNo = gstinNo;
        }

        public String getNature() {
            return nature;
        }

        public void setNature(String nature) {
            this.nature = nature;
        }

        public String getMedicineSaleOn() {
            return medicineSaleOn;
        }

        public void setMedicineSaleOn(String medicineSaleOn) {
            this.medicineSaleOn = medicineSaleOn;
        }

        public String getPharmacyDiscount() {
            return pharmacyDiscount;
        }

        public void setPharmacyDiscount(String pharmacyDiscount) {
            this.pharmacyDiscount = pharmacyDiscount;
        }

        public String getTdsAccount() {
            return tdsAccount;
        }

        public void setTdsAccount(String tdsAccount) {
            this.tdsAccount = tdsAccount;
        }
    }

    private DmRSS rss;
    private DmHandelFile handelFileLog;
    private String lineNoDisplay = "", filterNature, groupCode;

    public ImportOrganization() {
    }

    @Override
    public ImportData_Mdl getImportDataModel() {
        final ImportData_Mdl fileDetail = new ImportData_Mdl();
        CSVData node;
        List<CSVData> lstCSV = new ArrayList<CSVData>();

        node = new CSVData();
        node.setHeader("name");
        node.setSample("Cash Patient");
        node.setInstruction("{alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("shortName");
        node.setSample("CP");
        node.setInstruction("{unique;mandatory;alphanumeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("serviceDiscount%");
        node.setSample("10.5");
        node.setInstruction("{numeric;}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("investigationDiscount%");
        node.setSample("10.5");
        node.setInstruction("{numeric;}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("pharmacyDiscount%");
        node.setSample("10.5");
        node.setInstruction("{numeric;}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("GSTinNo.");
        node.setSample("09ABCDE1234PZ1R");
        node.setInstruction("{alphanumeric;11c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("nature");
        node.setSample("General");
        node.setInstruction("{alphanumeric;(General/TPA)}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("medicineSaleOn");
        node.setSample("Sale Rate");
        node.setInstruction("{alphanumeric;(Sale Rate/MRP)}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("pharmacyDiscount");
        node.setSample("Yes");
        node.setInstruction("{alphanumeric;(Yes/No)}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("TDS A/c");
        node.setSample("TDS A/c");
        node.setInstruction("{alphanumeric;100c}");
        lstCSV.add(node);

        fileDetail.getHmCSV().addAll(lstCSV);
        fileDetail.setImportTo("Organization Master");
        fileDetail.setServerClassName(ImportOrganization.class.getName());

        return fileDetail;
    }

    @Override
    public DmImportStatus importCSV(DmRSSPrivate rss, ImportData_Mdl importData) {
        Model mrTemp;
        DmCsvReader csvReader = null;
        File dataFile = null;
        Connection cnTemp = null;
        boolean isFound;
        DmDynamicGridRow dynamicGridRow;
        String fieldValue;
        Object objValue;
        String[] csvFileFormat;
        int status = 0, rtnStatus = 0;
        DmImportStatus importStatus = null;
        try {
            this.rss = rss;
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));
            if (DmShared.xNull(importData.getLogFilePath()).trim().equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new Exception("Log file path can not be blank.");
            }
            handelFileLog = new DmHandelFile(importData.getLogFilePath(), true);

            filterNature = DmServer.getCodeViaManualCode("OR", "manualCode", "subgroupNature", "Code", cnTemp);
            if (DmShared.iNull(filterNature).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, "Ledger 'OR' nature not exists.");
            }

            groupCode = DmServer.getCodeViaManualCode("1", "1", "enviroCompany", "acGroupOrganizationCode", cnTemp);
            if (DmShared.iNull(groupCode).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, "A/c Group not found in envrio Company");
            }
            cnTemp.close();
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
                        } else if (headerName.trim().equalsIgnoreCase("serviceDiscount%")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setServiceDiscountPer(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("investigationDiscount%")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setTestDiscountPer(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("pharmacyDiscount%")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPharmacyDiscountPer(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("GSTinNo.")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 11, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setGstinNo(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("nature")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setNature(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("medicineSaleOn")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setMedicineSaleOn(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("pharmacyDiscount")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 3, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPharmacyDiscount(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("TDS A/c")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setTdsAccount(fieldValue);
                            }
                        }
                    }
                    if (isFound) {
                        status = saveAccount(mrTemp);
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
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
                if (handelFileLog != null) {
                    handelFileLog.close();
                }
            } catch (Exception e) {
            }
        }

        return importStatus;
    }

    private int saveAccount(Model mrTemp) {
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null, rsTemp1 = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        MedicineSaleOn saleOn = null;
        String sqlQuery;
        int status = 0, rtnStatus = 0;
        try {

            mrTemp.setName(DmShared.removeSpace(mrTemp.getName()));
            mrTemp.setMedicineSaleOn(DmShared.removeSpace(mrTemp.getMedicineSaleOn()));
            mrTemp.setNature(DmShared.removeSpace(mrTemp.getNature()));
            saleOn = MedicineSaleOn.getValueOf(DmShared.xNull(mrTemp.getMedicineSaleOn()));
            if (saleOn == null) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, "Medicine Sale On not found for " + mrTemp.getName());
            }
            if (!(mrTemp.getNature().equalsIgnoreCase("General") || mrTemp.getNature().equalsIgnoreCase("TPA"))) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, "Nature not found for " + mrTemp.getName());
            } else {
                mrTemp.setNature(DmShared.xNull(mrTemp.getNature()).equalsIgnoreCase("TPA") ? "1" : "0");
            }

            mrTemp.setPharmacyDiscount(DmShared.xNull(mrTemp.getPharmacyDiscount()).equalsIgnoreCase("Yes") ? "1" : "0");

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setCode(DmServer.getSerialNoBigInt(rss, "subGroup", cnTemp));
            if (DmShared.iNull(mrTemp.getCode()).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new UnsupportedOperationException("Invalid key.");
            }

            sqlQuery = "Select Count(*) As Cnt From SubGroup Where Name = :name and SubCode<> :subCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", mrTemp.getName());
            psTemp.setString("subCode", mrTemp.getCode());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                status = DmImportStatus.WARNING.getId();
                throw new UnsupportedOperationException(mrTemp.getName() + " duplicate name not allowed !!!");
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "Select Count(*) As Cnt From organizationmast Where shortName =:shortName and subcode <>:code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("shortName", DmShared.xNull(mrTemp.getShortName()));
            psTemp.setString("code", DmShared.xNull(mrTemp.getCode()));
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("Cnt") > 0) {
                status = DmImportStatus.WARNING.getId();
                throw new UnsupportedOperationException("Short Name already exists.");
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "select om.isTPA from organizationMast om where om.subCode = :Code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.xNull(mrTemp.getCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("isTPA")).intValue() != DmShared.iNull(mrTemp.getNature()).intValue()) {
                    sqlQuery = "select organizationCode from patientRegistration pr where pr.organizationCode = :Code";
                    psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp1.setString("code", DmShared.xNull(mrTemp.getCode()));
                    rsTemp1 = psTemp1.executeQuery();
                    if (rsTemp1.next()) {
                        mrTemp.setNature(DmShared.xNull(rsTemp.getString("isTPA")));
                        status = DmImportStatus.WARNING.getId();
                        throw new DmException(0, " 'Nature' value not change beacause " + mrTemp.getName() + " in use.");
                    }
                    rsTemp1.close();
                    psTemp1.close();
                }
            }
            rsTemp.close();
            psTemp.close();

            if (!DmShared.xNull(mrTemp.getTdsAccount()).equalsIgnoreCase("")) {
                mrTemp.setTdsAccount(DmServer.getCodeViaManualCode(mrTemp.getTdsAccount(), "name", "subGroup", "subCode", cnTemp));
                if (DmShared.iNull(mrTemp.getTdsAccount()).longValue() == 0) {
                    status = DmImportStatus.WARNING.getId();
                    handelFileLog.writeLine("TDS A/c not found for: " + mrTemp.getName());
                }
            }

            // ************************************** INSERT INTO SUBGROUP *****************************************
            sqlQuery = "insert into subGroup(subCode, name, groupCode, manualCode, nature, filterNature, u_EntDt, site_Code, displayName, preparedBy, "
                    + "         isActive, gsTin) "
                    + " values (:subCode, :name, :groupCode, :manualCode, :nature,:filterNature, :uEntDt, :siteCode, :name, :userName, "
                    + "         :isActive, :gsTin) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            psTemp.setString("subCode", DmShared.xNull(mrTemp.getCode()));
            psTemp.setString("name", DmShared.xNull(mrTemp.getName()));
            psTemp.setString("gsTin", DmShared.xNull(mrTemp.getGstinNo()));
            psTemp.setString("groupCode", DmShared.xNull(groupCode));
            psTemp.setString("manualCode", DmShared.xNull(mrTemp.getShortName()));
            psTemp.setString("filterNature", DmShared.xNull(filterNature));
            psTemp.setString("nature", DmShared.xNull(filterNature));
            psTemp.setString("userName", DmServer.getSsnUserName(rss));
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(rss));
            psTemp.setString("isActive", "1");
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "insert into organizationMast(shortName, subCode, serviceDiscountPer, testDiscountPer, feesDiscountPer, isTPA, "
                    + "         medicineSaleOn, tdsAc, pharmacyDiscountPer, isPharmacyDiscount) "
                    + " Values (:shortName, :subCode, :serviceDiscountPer, :testDiscountPer, :feesDiscountPer, :isTPA, :medicineSaleOn, "
                    + "         :tdsAc, :pharmacyDiscountPer, :isPharmacyDiscount ) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("subCode", DmShared.xNull(mrTemp.getCode()));
            psTemp.setString("shortName", DmShared.xNull(mrTemp.getShortName()));
            psTemp.setString("serviceDiscountPer", DmServer.vNull(mrTemp.getServiceDiscountPer(), "0.00"));
            psTemp.setString("testDiscountPer", DmServer.vNull(mrTemp.getTestDiscountPer(), "0.00"));
            psTemp.setString("feesDiscountPer", DmServer.vNull(mrTemp.getFeesDiscountPer(), "0.00"));
            psTemp.setString("pharmacyDiscountPer", DmServer.vNull(mrTemp.getPharmacyDiscountPer(), "0.00"));
            psTemp.setString("isTPA", DmShared.xNull(mrTemp.getNature()));
            psTemp.setString("isPharmacyDiscount", DmShared.xNull(mrTemp.getPharmacyDiscount()));
            psTemp.setString("medicineSaleOn", DmShared.iNull(saleOn.getId()).toString());
            psTemp.setString("tdsAc", DmShared.foriegnKey(mrTemp.getTdsAccount()));
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "insert into subGroupSiteList (subCode, site_Code) values (:subCode, :siteCode) ;";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getCode()));
            psTemp.setString("siteCode", DmShared.foriegnKey(DmServer.getSsnSiteCode(rss)));
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
