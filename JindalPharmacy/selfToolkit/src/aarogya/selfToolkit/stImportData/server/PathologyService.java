package aarogya.selfToolkit.stImportData.server;

import aarogya.projectMenu.self.shared.pmDiagnostic;
import aarogya.projectMenu.self.shared.pmIPD;
import aarogya.projectMenu.self.shared.pmMaster;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSS;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.server.DmHandelFile;
import dataman.dmBase.self.server.DmCsvReader;
import dataman.dmBase.self.shared.DmDynamicGridRow;
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmImportData.server.DmImportData_Interface;
import dataman.dmToolkit.dmImportData.shared.CSVData;
import dataman.dmToolkit.dmImportData.shared.DmImportStatus;
import dataman.dmToolkit.dmImportData.shared.ImportData_Mdl;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PathologyService implements DmImportData_Interface {

//<editor-fold defaultstate="collapsed" desc="Class Model">
    class Model {

        private String code, name, shortName, categoryCode, categoryName, printSNo, isSubCategory, testType,
                serviceNature, isRateEditable, isNightShift, nightShiftAmount, reportNamePrefix, isActive,
                reportMinute, tatMinute, groupCode, sampleCode, groupName, sampleName, sampleQty, subTestFormula,
                isBold, isItalic, isUnderline, isDiscountAllow, pageContent;

        public Model() {
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getSampleName() {
            return sampleName;
        }

        public void setSampleName(String sampleName) {
            this.sampleName = sampleName;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
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

        public String getCategoryCode() {
            return categoryCode;
        }

        public void setCategoryCode(String categoryCode) {
            this.categoryCode = categoryCode;
        }

        public String getPrintSNo() {
            return printSNo;
        }

        public void setPrintSNo(String printSNo) {
            this.printSNo = printSNo;
        }

        public String getIsSubCategory() {
            return isSubCategory;
        }

        public void setIsSubCategory(String isSubCategory) {
            this.isSubCategory = isSubCategory;
        }

        public String getTestType() {
            return testType;
        }

        public void setTestType(String testType) {
            this.testType = testType;
        }

        public String getServiceNature() {
            return serviceNature;
        }

        public void setServiceNature(String serviceNature) {
            this.serviceNature = serviceNature;
        }

        public String getIsRateEditable() {
            return isRateEditable;
        }

        public void setIsRateEditable(String isRateEditable) {
            this.isRateEditable = isRateEditable;
        }

        public String getIsNightShift() {
            return isNightShift;
        }

        public void setIsNightShift(String isNightShift) {
            this.isNightShift = isNightShift;
        }

        public String getNightShiftAmount() {
            return nightShiftAmount;
        }

        public void setNightShiftAmount(String nightShiftAmount) {
            this.nightShiftAmount = nightShiftAmount;
        }

        public String getReportNamePrefix() {
            return reportNamePrefix;
        }

        public void setReportNamePrefix(String reportNamePrefix) {
            this.reportNamePrefix = reportNamePrefix;
        }

        public String getIsActive() {
            return isActive;
        }

        public void setIsActive(String isActive) {
            this.isActive = isActive;
        }

        public String getReportMinute() {
            return reportMinute;
        }

        public void setReportMinute(String reportMinute) {
            this.reportMinute = reportMinute;
        }

        public String getTatMinute() {
            return tatMinute;
        }

        public void setTatMinute(String tatMinute) {
            this.tatMinute = tatMinute;
        }

        public String getGroupCode() {
            return groupCode;
        }

        public void setGroupCode(String groupCode) {
            this.groupCode = groupCode;
        }

        public String getSampleCode() {
            return sampleCode;
        }

        public void setSampleCode(String sampleCode) {
            this.sampleCode = sampleCode;
        }

        public String getSampleQty() {
            return sampleQty;
        }

        public void setSampleQty(String sampleQty) {
            this.sampleQty = sampleQty;
        }

        public String getSubTestFormula() {
            return subTestFormula;
        }

        public void setSubTestFormula(String subTestFormula) {
            this.subTestFormula = subTestFormula;
        }

        public String getIsBold() {
            return isBold;
        }

        public void setIsBold(String isBold) {
            this.isBold = isBold;
        }

        public String getIsItalic() {
            return isItalic;
        }

        public void setIsItalic(String isItalic) {
            this.isItalic = isItalic;
        }

        public String getIsUnderline() {
            return isUnderline;
        }

        public void setIsUnderline(String isUnderline) {
            this.isUnderline = isUnderline;
        }

        public String getIsDiscountAllow() {
            return isDiscountAllow;
        }

        public void setIsDiscountAllow(String isDiscountAllow) {
            this.isDiscountAllow = isDiscountAllow;
        }

        public String getPageContent() {
            return pageContent;
        }

        public void setPageContent(String pageContent) {
            this.pageContent = pageContent;
        }

    }
    //</editor-fold>

    private DmRSS rss;
    private DmHandelFile handelFileLog;
    private String lineNoDisplay = "";

    public PathologyService() {
    }

    @Override
    public ImportData_Mdl getImportDataModel() {
        ImportData_Mdl fileDetail = new ImportData_Mdl();
        CSVData node;
        List<CSVData> lstCSV = new ArrayList<CSVData>();

        node = new CSVData();
        node.setHeader("name");
        node.setSample("*A11");
        node.setInstruction("{mandatory;alphanumeric;160c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("CategoryName");
        node.setSample("PATHOLOGY");
        node.setInstruction("{mandatory;alphanumeric;160c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("shortName");
        node.setSample("CF");
        node.setInstruction("{mandatory;alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("printSNo");
        node.setSample("1");
        node.setInstruction("{numeric}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("isSubCategory");
        node.setSample("Yes");
        node.setInstruction("{mandatory;alphanumeric;9c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("testType");
        node.setSample("Same");
        node.setInstruction("{mandatory;alphanumeric;30c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("isRateEditable");
        node.setSample("Yes");
        node.setInstruction("{mandatory;alphanumeric;3c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("isNightShift");
        node.setSample("Yes");
        node.setInstruction("{mandatory;alphanumeric;3c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("nightShiftAmount");
        node.setSample("100");
        node.setInstruction("{numeric;8c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("reportNamePrefix");
        node.setSample("Yes");
        node.setInstruction("{alphanumeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("reportMinute");
        node.setSample("0");
        node.setInstruction("{numeric;8c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("tatMinute");
        node.setSample("0");
        node.setInstruction("{numeric;8c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("isActive");
        node.setSample("Yes");
        node.setInstruction("{mandatory;alphanumeric;3c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("groupName");
        node.setSample("BIO-CHEMISTRY");
        node.setInstruction("{mandatory;alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("sampleName");
        node.setSample("BLOOD");
        node.setInstruction("{alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("sampleQty");
        node.setSample("0");
        node.setInstruction("{numeric;9c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("subTestFormula");
        node.setSample("");
        node.setInstruction("{alphanumeric;500c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("isBold");
        node.setSample("Yes");
        node.setInstruction("{numeric;3c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("isItalic");
        node.setSample("Yes");
        node.setInstruction("{numeric;3c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("isUnderline");
        node.setSample("Yes");
        node.setInstruction("{numeric;3c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("isDiscountAllow");
        node.setSample("Yes");
        node.setInstruction("{numeric;3c}");
        lstCSV.add(node);

        fileDetail.getHmCSV().addAll(lstCSV);
        fileDetail.setImportTo("Pathology Master");
        fileDetail.setServerClassName(PathologyService.class.getName());

        return fileDetail;

    }

    @Override
    public DmImportStatus importCSV(DmRSSPrivate rss, ImportData_Mdl importData) {
        Connection cnTemp = null;
        Model mrTemp;
        DmCsvReader csvReader = null;
        File dataFile = null;
        boolean isFound;
        DmDynamicGridRow dynamicGridRow;
        String fieldValue;
        String[] csvFileFormat;
        Object objValue;
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

                // fill model and save data
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
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 25, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setShortName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("categoryName")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setCategoryName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("printSNo")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 5, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPrintSNo(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("isSubCategory")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setIsSubCategory(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("isRateEditable")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 0, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setIsRateEditable(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("isNightShift")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 9, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setIsNightShift(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("isActive")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 25, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;

                            } else {
                                mrTemp.setIsActive(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("reportMinute")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 30, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setReportMinute(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("tatMinute")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setTatMinute(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("testType")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setTatMinute(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("groupName")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 250, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setGroupName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("subTestFormula")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 250, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setSubTestFormula(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("sampleName")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 250, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setSampleName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("sampleQty")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 250, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setSampleQty(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("isBold")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 250, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setIsBold(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("isItalic")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 250, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setIsItalic(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("isUnderline")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 250, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setIsUnderline(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("isDiscountAllow")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 250, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setIsDiscountAllow(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("pageContent")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 250, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPageContent(fieldValue);
                            }
                        }
                    }
                    if (isFound) {
                        status = savePathologyService(mrTemp);
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
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return importStatus;
    }

    private int savePathologyService(Model mrTemp) {
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        CallableStatement csTemp = null;
        String sqlQuery;
        int rtnStatus = 0, status = 0;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setName(DmShared.removeSpace(mrTemp.getName()));
            mrTemp.setCode(DmServer.getSerialNoInteger(rss, "patientServiceMast", cnTemp));
            if (DmShared.iNull(mrTemp.getCode()).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new UnsupportedOperationException("Invalid key.");
            }

            /*====================================< Check whether Investigation already exists or not >====================================*/
            sqlQuery = "Select serviceNature From patientServiceMast Where name =:name and code <> :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getName()));
            psTemp.setString("code", DmShared.xNull(mrTemp.getCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                String msg = "Investigation already exists";
                switch (STEnum.ServiceNature.getKeyOf(DmShared.iNull(rsTemp.getString("serviceNature")).intValue())) {
                    case SERVICE:
                        msg += " in " + new pmMaster().SERVICE_MASTER.caption;
                        break;
                    case OPERATION:
                        msg += " in " + new pmIPD().OPERATION_MASTER.caption;
                        break;
                    case RADIOLOGY_TEST:
                        msg += " in " + new pmDiagnostic().RADIOLOGY_TEST_MASTER.caption;
                        break;
                    case OVERHEAD_SERVICE:
                        msg += " in " + new pmMaster().OVERHEAD_SERVICE_MASTER.caption;
                        break;
                    default:
                        msg += " ";
                        break;
                }
                msg += ".";
                throw new UnsupportedOperationException(msg);
            }
            rsTemp.close();
            psTemp.close();

            /*====================================< Check whether Short name already exists or not >====================================*/
            sqlQuery = "Select serviceNature From patientServiceMast Where shortName =:shortName and code <> :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("shortName", DmShared.xNull(mrTemp.getShortName()));
            psTemp.setString("code", DmShared.xNull(mrTemp.getCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                String msg = "Short Name already exists";
                switch (STEnum.ServiceNature.getKeyOf(DmShared.iNull(rsTemp.getString("serviceNature")).intValue())) {
                    case SERVICE:
                        msg += " in " + new pmMaster().SERVICE_MASTER.caption;
                        break;
                    case OPERATION:
                        msg += " in " + new pmIPD().OPERATION_MASTER.caption;
                        break;
                    case RADIOLOGY_TEST:
                        msg += " in " + new pmDiagnostic().RADIOLOGY_TEST_MASTER.caption;
                        break;
                    case OVERHEAD_SERVICE:
                        msg += " in " + new pmMaster().OVERHEAD_SERVICE_MASTER.caption;
                        break;
                    default:
                        msg += " ";
                        break;
                }
                msg += ".";
                throw new UnsupportedOperationException(msg);
            }
            rsTemp.close();
            psTemp.close();

            // ************************************** GET Category Name ****************************************
            mrTemp.setCategoryCode(DmServer.getCodeViaManualCode(mrTemp.getCategoryName(), "Name", "patientServiceCatMast", "Code", cnTemp));
            if (DmShared.iNull(mrTemp.getCategoryCode()).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, mrTemp.getCategoryName() + " Category does not exists.");
            }
            // ******************************************* END *************************************************

            // ************************************** GET Group Name ****************************************
            mrTemp.setGroupCode((DmServer.getCodeViaManualCode(mrTemp.getGroupName(), "Name", "serviceGroup", "Code", cnTemp)));
            if (DmShared.iNull(mrTemp.getGroupCode()).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, mrTemp.getGroupName() + " Group does not exists.");
            }
            // ******************************************* END *************************************************

            // ************************************** GET Sample Name ****************************************
            mrTemp.setSampleCode((DmServer.getCodeViaManualCode(mrTemp.getSampleName(), "SampleName", "sampleMast", "Code", cnTemp)));
            if (DmShared.iNull(mrTemp.getSampleCode()).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, mrTemp.getSampleName() + " Sample does not exists.");
            }
            // ******************************************* END *************************************************

            /*=============================< Delete records from patientServiceBOM >=============================*/
            sqlQuery = "Delete from patientServiceBOM where serviceCode = :serviceCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("serviceCode", DmShared.iNull(mrTemp.getCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*===========================< Delete from diagnosticTemplateMast >=========================*/
            sqlQuery = "Delete From diagnosticTemplateMast Where code = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*=============================< Delete records from testMethodAge >=============================*/
            sqlQuery = "Delete From testMethodAge Where Code= :Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("Code", mrTemp.getCode());
            psTemp.executeUpdate();
            psTemp.close();

            /*=============================< Delete records from testMethod >=============================*/
            sqlQuery = "Delete From testMethod Where Code= :Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("Code", mrTemp.getCode());
            psTemp.executeUpdate();
            psTemp.close();

            /*=============================< Delete records from testSubCategory >=============================*/
            sqlQuery = "Delete From testSubCategory Where Code= :Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("Code", mrTemp.getCode());
            psTemp.executeUpdate();
            psTemp.close();

            /*=============================< Delete records from testSensitivity >=============================*/
            sqlQuery = "Delete From testSensitivity Where Code= :Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("Code", mrTemp.getCode());
            psTemp.executeUpdate();
            psTemp.close();

            /*=============================< Delete records from testWidal >=============================*/
            sqlQuery = "Delete From testWidal Where Code= :Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("Code", mrTemp.getCode());
            psTemp.executeUpdate();
            psTemp.close();

            /*====================================< Insert/ Update patientServiceMast >====================================*/
            sqlQuery = "Insert Into patientServiceMast(code, name, shortName, categoryCode, printSNo, isSubCategory, testType, serviceRoutine, "
                    + " serviceNature, isRateEditable, isNightShift, nightShiftAmount, reportNamePrefix, isActive, u_EntDt, preparedBy, "
                    + " reportMinute, tatMinute, groupCode, sampleCode, sampleQty, subTestFormula, "
                    + " isBold, isItalic, isUnderline, isDiscountAllow,pageContent) "
                    + " Values(:code, :name, :shortName, :categoryCode, :printSNo, :isSubCategory, :testType, :serviceRoutine, "
                    + " :serviceNature, :isRateEditable, :isNightShift, :nightShiftAmount, :reportNamePrefix, :isActive, :u_EntDt, :preparedBy, "
                    + " :reportMinute, :tatMinute, :groupCode, :sampleCode, :sampleQty,  :subTestFormula, "
                    + " :isBold, :isItalic, :isUnderline, :isDiscountAllow,:pageContent) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getCode()).toString());
            psTemp.setString("name", mrTemp.getName().trim());
            psTemp.setString("shortName", DmShared.xNull(mrTemp.getShortName()));
            psTemp.setString("categoryCode", DmShared.foriegnKey(mrTemp.getCategoryCode()));
            psTemp.setString("printSNo", DmServer.vNull(mrTemp.getPrintSNo(), "0"));
            psTemp.setString("testType", DmShared.xNull(mrTemp.getTestType()));
            psTemp.setString("sampleQty", DmServer.vNull(mrTemp.getSampleQty(), "0.00"));
            psTemp.setString("sampleCode", DmShared.foriegnKey(mrTemp.getSampleCode()));
            psTemp.setString("serviceRoutine", "0");
            psTemp.setString("serviceNature", DmShared.iNull(STEnum.ServiceNature.PATHOLOGY_TEST.getId()).toString());
            psTemp.setString("preparedBy", DmServer.getSsnUserName(rss));
            psTemp.setString("groupCode", DmShared.foriegnKey(mrTemp.getGroupCode()));
            psTemp.setString("reportMinute", DmShared.iNull(mrTemp.getReportMinute()).toString());
            psTemp.setString("nightShiftAmount", DmShared.xNull(mrTemp.getNightShiftAmount()));
            psTemp.setString("tatMinute", DmShared.iNull(mrTemp.getTatMinute()).toString());
            psTemp.setString("subTestFormula", DmShared.xNull(mrTemp.getSubTestFormula()));
            psTemp.setString("isRateEditable", DmShared.xNull(mrTemp.getIsRateEditable()).trim().equalsIgnoreCase("Yes") ? "1" : "0");
            psTemp.setString("isBold", DmShared.xNull(mrTemp.getIsBold()).trim().equalsIgnoreCase("Yes") ? "1" : "0");
            psTemp.setString("isNightShift", DmShared.xNull(mrTemp.getIsNightShift()).trim().equalsIgnoreCase("Yes") ? "1" : "0");
            psTemp.setString("isActive", DmShared.xNull(mrTemp.getIsActive()).trim().equalsIgnoreCase("Yes") ? "1" : "0");
            psTemp.setString("isSubCategory", DmShared.xNull(mrTemp.getIsSubCategory()).trim().equalsIgnoreCase("Yes") ? "1" : "0");
            psTemp.setString("isItalic", DmShared.xNull(mrTemp.getIsItalic()).trim().equalsIgnoreCase("Yes") ? "1" : "0");
            psTemp.setString("isUnderline", DmShared.xNull(mrTemp.getIsUnderline()).trim().equalsIgnoreCase("Yes") ? "1" : "0");
            psTemp.setString("isDiscountAllow", DmShared.xNull(mrTemp.getIsDiscountAllow()).trim().equalsIgnoreCase("Yes") ? "1" : "0");
            psTemp.setString("reportNamePrefix", DmShared.xNull(mrTemp.getReportNamePrefix()).trim());
            psTemp.setString("pageContent", DmShared.xNull(mrTemp.getPageContent()));
            psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
            psTemp.executeUpdate();
            psTemp.close();

            //<editor-fold defaultstate="collapsed" desc="INSERT IN PATIENTSERVICE_SITE_LIST">
            sqlQuery = "INSERT INTO patientServiceSiteList (CODE, site_Code)  "
                    + " VALUES (:code, :site_Code)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.foriegnKey(mrTemp.getCode()));
            psTemp.setString("site_Code", DmShared.foriegnKey(DmServer.getSsnSiteCode(rss)));
            //</editor-fold>

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
            psTemp.setString("code", mrTemp.getCode());
            psTemp.executeUpdate();
            psTemp.close();

            /*===============================< Insert into testMethod >===============================*/
            sqlQuery = "Insert into testMethod (code, v_SNo, method, workSheetPrint, unit, valueType, valuePattern, "
                    + " formula, isBold, isItalic, isUnderline, pageContent) "
                    + " values (:code, :v_SNo, :method, :worksheetprint, :unit, :valueType, :valuePattern, "
                    + " :formula, :isBold, :isItalic, :isUnderline, :pageContent)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", mrTemp.getCode());
            psTemp.setString("v_SNo", "1");
            psTemp.setString("method", "");
            psTemp.setString("valuePattern", "");
            psTemp.setString("workSheetPrint", "1");
            psTemp.setString("unit", null);
            psTemp.setString("valueType", "Remarks");
            psTemp.setString("formula", "");
            psTemp.setString("isBold", "0");
            psTemp.setString("isItalic", "0");
            psTemp.setString("isUnderline", "0");
            psTemp.setString("pageContent", "");
            psTemp.executeUpdate();
            psTemp.close();

            /*===========================< Insert record into testMethodAge >=========================*/
            sqlQuery = "Insert into testMethodAge (code, v_Sno, Sno, fromAge, toAge, male, maleTo, female, femaleTo) "
                    + " values (:code, :v_Sno, :sno, :fromAge,  :toAge, :male, :maleTo, :female, :femaleTo) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getCode()).toString());
            psTemp.setString("v_Sno", "1");
            psTemp.setString("sno", "1");
            psTemp.setString("fromAge", "0");
            psTemp.setString("toAge", "91250");
            psTemp.setString("male", "");
            psTemp.setString("maleTo", "");
            psTemp.setString("female", "");
            psTemp.setString("femaleTo", "");
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
