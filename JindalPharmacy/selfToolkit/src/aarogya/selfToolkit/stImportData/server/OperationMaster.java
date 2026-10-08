package aarogya.selfToolkit.stImportData.server;

import aarogya.selfToolkit.stStatic.shared.STEnum;
import aarogya.selfToolkit.stStatic.shared.STEnum.ServiceNature;
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
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmImportData.shared.DmImportStatus;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OperationMaster implements DmImportData_Interface {

    class Model extends KeyField {

        private String consentFrom;
        private String operationNote;
        private String shortName;
        private String categoryTag;
        private String categoryText;
        private String consentFormEnglish, discountAllow,
                rateEditable, nightShift, nightShiftAmount;

        public String getConsentFrom() {
            return consentFrom;
        }

        public void setConsentFrom(String consentFrom) {
            this.consentFrom = consentFrom;
        }

        public String getOperationNote() {
            return operationNote;
        }

        public void setOperationNote(String operationNote) {
            this.operationNote = operationNote;
        }

        public String getShortName() {
            return shortName;
        }

        public void setShortName(String shortName) {
            this.shortName = shortName;
        }

        public String getCategoryTag() {
            return categoryTag;
        }

        public void setCategoryTag(String categoryTag) {
            this.categoryTag = categoryTag;
        }

        public String getCategoryText() {
            return categoryText;
        }

        public void setCategoryText(String categoryText) {
            this.categoryText = categoryText;
        }

        public String getConsentFormEnglish() {
            return consentFormEnglish;
        }

        public void setConsentFormEnglish(String consentFormEnglish) {
            this.consentFormEnglish = consentFormEnglish;
        }

        public String getDiscountAllow() {
            return discountAllow;
        }

        public void setDiscountAllow(String discountAllow) {
            this.discountAllow = discountAllow;
        }

        public String getRateEditable() {
            return rateEditable;
        }

        public void setRateEditable(String rateEditable) {
            this.rateEditable = rateEditable;
        }

        public String getNightShift() {
            return nightShift;
        }

        public void setNightShift(String nightShift) {
            this.nightShift = nightShift;
        }

        public String getNightShiftAmount() {
            return nightShiftAmount;
        }

        public void setNightShiftAmount(String nightShiftAmount) {
            this.nightShiftAmount = nightShiftAmount;
        }
    }

    private DmRSS rss;
    private DmHandelFile handelFileLog;

    private String lineNoDisplay = "";

    public OperationMaster() {
    }

    @Override
    public ImportData_Mdl getImportDataModel() {
        final ImportData_Mdl fileDetail = new ImportData_Mdl();
        CSVData node;
        List<CSVData> lstCSV = new ArrayList<CSVData>();

        node = new CSVData();
        node.setHeader("OperationName");
        node.setSample("BRAIN + PERFUSION CONTRAST");
        node.setInstruction("{unique;mandatory;alphanumeric;160c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("ShortName");
        node.setSample("SUITE");
        node.setInstruction("{unique;mandatory;alphanumeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("Category");
        node.setSample("131-lodine Therapy");
        node.setInstruction("{mandatory;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("ConsentForm");
        node.setSample("XYZCons");
        node.setInstruction("{alphanumeric;5000c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("ConsentFormEnglish");
        node.setSample("XYZCons");
        node.setInstruction("{alphanumeric;5000c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("OperationNotes");
        node.setSample("XYZOpNotes");
        node.setInstruction("{alphanumeric;5000c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("discountAllow");
        node.setSample("Yes");
        node.setInstruction("0=No/1=Yes");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("rateEditable");
        node.setSample("Yes");
        node.setInstruction("0=No/1=Yes");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("nightShift");
        node.setSample("Yes");
        node.setInstruction("0=No/1=Yes");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("nightShiftAmount");
        node.setSample("Yes");
        node.setInstruction("numeric:");
        lstCSV.add(node);

        fileDetail.getHmCSV().addAll(lstCSV);
        fileDetail.setImportTo("Operation Master");
        fileDetail.setServerClassName(OperationMaster.class.getName());

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
                        if (headerName.trim().equalsIgnoreCase("OperationName")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 160, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setSharedKeyFieldName(fieldValue);
                            }

                        } else if (headerName.trim().equalsIgnoreCase("ShortName")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setShortName(fieldValue);
                            }

                        } else if (headerName.trim().equalsIgnoreCase("Category")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setCategoryText(fieldValue);
                            }

                        } else if (headerName.trim().equalsIgnoreCase("ConsentForm")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 5000, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setConsentFrom(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("ConsentFormEnglish")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 5000, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setConsentFormEnglish(fieldValue);
                            }

                        } else if (headerName.trim().equalsIgnoreCase("OperationNotes")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 5000, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setOperationNote(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("discountAllow")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 3, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setDiscountAllow(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("rateEditable")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 3, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setRateEditable(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("nightShift")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 3, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setNightShift(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("nightShiftAmount")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 3, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setNightShiftAmount(fieldValue);
                            }
                        }
                    }
                    if (isFound) {
                        status = saveOperationMaster(mrTemp);
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

    private int saveOperationMaster(Model mrTemp) {
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, categoryCode = "";
        int status = 0, rtnStatus = 0;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            mrTemp.setShortName(mrTemp.getShortName().replace(" ", "").trim());
            if (!DmShared.xNull(mrTemp.getSharedKeyFieldName()).equalsIgnoreCase("")) {

                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoInteger(rss, "patientservicemast", cnTemp));
            }
            categoryCode = DmServer.getCodeViaManualCode(mrTemp.getCategoryText(), "name", "patientservicecatmast", "code", cnTemp);

            if (DmShared.iNull(categoryCode).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, DmShared.xNull(mrTemp.getCategoryText()) + " category does not exist");
            }

            if (DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new UnsupportedOperationException("Invalid key.");
            }

            sqlQuery = "SELECT COUNT(name) AS cnt FROM patientservicemast WHERE name = :name AND code <> :code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()).toLowerCase());
            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                status = DmImportStatus.WARNING.getId();
                throw new UnsupportedOperationException("Operation Name already exists.");
            }
            rsTemp.close();
            psTemp.close();
            if (!DmShared.xNull(mrTemp.getShortName()).equalsIgnoreCase("")) {
                sqlQuery = "select count(shortName) as cnt from patientservicemast where shortName = :shortName and code <> :code";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("shortName", DmShared.xNull(mrTemp.getShortName()).toLowerCase());
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                rsTemp = psTemp.executeQuery();
                rsTemp.next();
                if (rsTemp.getLong("cnt") > 0) {
                    status = DmImportStatus.WARNING.getId();
                    throw new UnsupportedOperationException("Short Name already exists.");
                }
                rsTemp.close();
                psTemp.close();
            }

            sqlQuery = "Insert Into patientservicemast(code, name, shortName, categoryCode, serviceRoutine, serviceNature, isActive, u_EntDt, preparedBy) "
                    + " Values(:code, :name, :shortName, :categoryCode, :serviceRoutine, :serviceNature, :isActive, :u_EntDt, :preparedBy) ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
            psTemp.setString("name", mrTemp.getSharedKeyFieldName());
            psTemp.setString("shortName", DmShared.xNull(mrTemp.getShortName()));
            psTemp.setString("categoryCode", DmShared.foriegnKey(categoryCode));
            psTemp.setString("isActive", DmShared.xNull("1"));
            psTemp.setString("serviceRoutine", "0");
            psTemp.setInt("serviceNature", ServiceNature.OPERATION.getId());
            psTemp.setString("preparedBy", DmServer.getSsnUserName(rss));
            psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "INSERT INTO patientOperationmast (serviceCODE,  operationNote, consentForm,consentFormEnglish) "
                    + "VALUES (:serviceCODE, :operationNote, :consentForm,:consentFormEnglish) ;";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("serviceCODE", mrTemp.getSharedKeyFieldCode());
            psTemp.setString("operationNote", mrTemp.getOperationNote());
            psTemp.setString("consentForm", mrTemp.getConsentFrom());
            psTemp.setString("consentFormEnglish", mrTemp.getConsentFormEnglish());
            psTemp.executeUpdate();
            psTemp.close();

            /*===========================< Update record of patientServiceMast >===============================*/
            sqlQuery = "Update  psm set "
                    + "         psm.its_Code = pscm.its_Code, "
                    + "         psm.serviceAc = pscm.serviceAc "
                    + "         from patientServiceMast as psm "
                    + "         left join patientServiceCatMast as pscm on psm.categoryCode = pscm.code "
                    + "         where psm.code = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();

            //<editor-fold defaultstate="collapsed" desc="INSERT IN PATIENTSERVICE_SITE_LIST">
            sqlQuery = "INSERT INTO patientServiceSiteList (CODE, site_Code)  "
                    + " VALUES (:code, :site_Code)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
            psTemp.setString("site_Code", DmShared.foriegnKey(DmServer.getSsnSiteCode(rss)));
            psTemp.executeUpdate();
            psTemp.close();
            //</editor-fold>

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
