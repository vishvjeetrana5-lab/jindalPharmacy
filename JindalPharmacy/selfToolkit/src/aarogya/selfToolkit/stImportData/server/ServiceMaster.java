package aarogya.selfToolkit.stImportData.server;

import dataman.dmToolkit.dmImportData.server.DmImportData_Interface;
import dataman.dmToolkit.dmImportData.shared.CSVData;
import dataman.dmToolkit.dmImportData.shared.ImportData_Mdl;
import aarogya.projectMenu.self.shared.pmDiagnostic;
import aarogya.projectMenu.self.shared.pmIPD;
import aarogya.projectMenu.self.shared.pmMaster;
import aarogya.selfToolkit.stStatic.shared.STEnum.*;
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

public class ServiceMaster implements DmImportData_Interface {

    class Model {

        private String code, name, shortName, categoryName, printSNo, groupName, serviceRoutine, discountAllow,
                rateEditable, nightShift, nightShiftAmount;

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

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getPrintSNo() {
            return printSNo;
        }

        public void setPrintSNo(String printSNo) {
            this.printSNo = printSNo;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getServiceRoutine() {
            return serviceRoutine;
        }

        public void setServiceRoutine(String serviceRoutine) {
            this.serviceRoutine = serviceRoutine;
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

    public ServiceMaster() {
    }

    @Override
    public ImportData_Mdl getImportDataModel() {
        ImportData_Mdl fileDetail = new ImportData_Mdl();
        CSVData node;
        List<CSVData> lstCSV = new ArrayList<CSVData>();

        node = new CSVData();
        node.setHeader("serviceName");
        node.setSample("Consultation Fees");
        node.setInstruction("{unique,mandatory,alphanumeric,160c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("shortName");
        node.setSample("CF");
        node.setInstruction("{unique,mandatory,alphanumeric,10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("categoryName");
        node.setSample("Consultation");
        node.setInstruction("{mandatory;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("printSNo");
        node.setSample("1");
        node.setInstruction("{numeric,negative allow,4(length)}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("groupName");
        node.setSample("Service Group");
        node.setInstruction("{mandatory;200c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("serviceRoutine");
        node.setSample("As Applicable");
        node.setInstruction("0=As Applicable/1=Daily/2=Hourly");
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
        node.setSample("0");
        node.setInstruction("numeric:");
        lstCSV.add(node);

        fileDetail.getHmCSV().addAll(lstCSV);
        fileDetail.setImportTo("Service Master");
        fileDetail.setServerClassName(ServiceMaster.class.getName());

        return fileDetail;
    }

    @Override
    public DmImportStatus importCSV(DmRSSPrivate rss, ImportData_Mdl importData) {
        Model mrTemp;
        DmCsvReader csvReader = null;
        File dataFile = null;
        boolean isFound, isNightShift;
        DmDynamicGridRow dynamicGridRow;
        String fieldValue;
        String[] csvFileFormat;
        Object objValue;
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
                    isNightShift = false;
                    for (String headerName : csvFileFormat) {
                        objValue = dynamicGridRow.getCells().get(headerName.toLowerCase());
                        if (objValue == null) {
                            fieldValue = "";
                        } else {
                            fieldValue = String.valueOf(objValue);
                        }

                        if (headerName.trim().equalsIgnoreCase("serviceName")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 160, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("shortName")) {

                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setShortName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("categoryName")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setCategoryName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("printSNo")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 4, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPrintSNo(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("groupName")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setGroupName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("serviceRoutine")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 13, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setServiceRoutine(fieldValue);
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
                            isNightShift = fieldValue.equalsIgnoreCase("yes") ? true : false;
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 3, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setNightShift(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("nightShiftAmount")) {
                            if (isNightShift) {
                                if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 3, lineNoDisplay, handelFileLog)) {
                                    status = DmImportStatus.ERROR.getId();
                                    isFound = false;
                                } else {
                                    mrTemp.setNightShiftAmount(fieldValue);
                                }
                            } else {
                                mrTemp.setNightShiftAmount(null);
                            }

                        }
                    }
                    if (isFound) {
                        status = saveServiceMast(mrTemp);
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

    private int saveServiceMast(Model mrTemp) {
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, organizationCode = "", categoryCode = "", groupCode = "", serviceRoutine = "",
                discountAllow = "", ratedEditable = "", nightShift = "";
        int rtnStatus = 0, status = 0;
        try {
            discountAllow = DmShared.xNull(mrTemp.getDiscountAllow()).trim().equalsIgnoreCase("Yes") ? "1" : "0";
            ratedEditable = DmShared.xNull(mrTemp.getRateEditable()).trim().equalsIgnoreCase("Yes") ? "1" : "0";
            nightShift = DmShared.xNull(mrTemp.getNightShift()).trim().equalsIgnoreCase("Yes") ? "1" : "0";

            if (DmShared.xNull(mrTemp.getServiceRoutine()).trim().equalsIgnoreCase("Daily")) {
                serviceRoutine = "1";
            } else if (DmShared.xNull(mrTemp.getServiceRoutine()).trim().equalsIgnoreCase("Hourly")) {
                serviceRoutine = "2";
            } else {
                serviceRoutine = "0";
            }
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setName(DmShared.removeSpace(mrTemp.getName()));
            mrTemp.setCode(DmServer.getSerialNoInteger(rss, "patientServiceMast", cnTemp));

            if (DmShared.iNull(mrTemp.getCode()).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new UnsupportedOperationException("Invalid key.");
            }

            //CATEGORY CODE  
            categoryCode = DmServer.getCodeViaManualCode(mrTemp.getCategoryName(), "name", "patientServiceCatMast", "code", cnTemp);

            if (categoryCode.equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, mrTemp.getCategoryName() + "Category not exists.");
            }
            //GROUP CODE 
            groupCode = DmServer.getCodeViaManualCode(mrTemp.getGroupName(), "name", "serviceGroup", "code", cnTemp);

            if (groupCode.equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, mrTemp.getGroupName() + "Group not exists.");
            }

            sqlQuery = "Select psm.serviceNature from patientServiceMast psm where psm.name = :name and psm.code <> :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getName()));
            psTemp.setString("code", DmShared.xNull(mrTemp.getCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                String msg = "";
                switch (ServiceNature.getKeyOf(DmShared.iNull(rsTemp.getString("serviceNature")).intValue())) {
                    case SERVICE:
                        msg = "in " + new pmMaster().SERVICE_MASTER.caption;
                        break;
                    case PATHOLOGY_TEST:
                        msg = "in " + new pmDiagnostic().PATHOLOGY_TEST_MASTER.caption;
                        break;
                    case OPERATION:
                        msg = "in " + new pmIPD().OPERATION_MASTER.caption;
                        break;
                    case RADIOLOGY_TEST:
                        msg = "in " + new pmDiagnostic().RADIOLOGY_TEST_MASTER.caption;
                        break;
                    case OVERHEAD_SERVICE:
                        msg = "in " + new pmMaster().OVERHEAD_SERVICE_MASTER.caption;
                        break;
                    default:
                        msg = "";
                        break;
                }
                status = DmImportStatus.WARNING.getId();
                throw new DmException(0, "Name already exists " + msg + ".");

            }
            rsTemp.close();
            psTemp.close();

            if (!DmShared.xNull(mrTemp.getShortName()).equalsIgnoreCase("")) {
                sqlQuery = "select  count(psm.shortName) as cnt "
                        + "         from patientServiceMast psm "
                        + "         where psm.shortName = :name and psm.code <> :code and isnull(psm.shortName,'') <> ''";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("name", DmShared.xNull(mrTemp.getShortName()).trim());
                psTemp.setString("code", DmShared.xNull(mrTemp.getCode()));
                rsTemp = psTemp.executeQuery();
                rsTemp.next();
                if (rsTemp.getLong("cnt") > 0) {
                    status = DmImportStatus.WARNING.getId();
                    throw new UnsupportedOperationException("Short Name already exists.");
                }
                rsTemp.close();
                psTemp.close();
            }
            organizationCode = DmShared.xNull(DmServer.getCodeViaManualCode("CP", "shortName", "organizationmast", "subCode", cnTemp));
            if (DmShared.xNull(organizationCode).equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new UnsupportedOperationException("Invalid CP Code.");
            }

            //======================= INSERT IN PATIENTSERVICEMAST TABLE  ====================================
            sqlQuery = "Insert into patientServiceMast(code, name, shortName, categoryCode, groupCode, isNightShift, nightShiftAmount, "
                    + " printSNo, u_EntDt, preparedBy, serviceRoutine, serviceNature, isActive, isDiscountAllow, isRateEditable) "
                    + " values (:code, :name, :shortName, :categoryCode, :groupCode, :isNightShift, :nightShiftAmount, "
                    + " :printSNo, :u_EntDt, :preparedBy, :serviceRoutine,  :serviceNature, 1, :isDiscountAllow, :isRateEditable);";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.xNull(mrTemp.getCode()));
            psTemp.setString("name", DmShared.xNull(mrTemp.getName()));
            psTemp.setString("shortName", DmShared.xNull(mrTemp.getShortName()));
            psTemp.setString("categoryCode", DmShared.foriegnKey(categoryCode));
            psTemp.setString("groupCode", DmShared.foriegnKey(groupCode));
            psTemp.setString("printSNo", DmServer.vNull(mrTemp.getPrintSNo(), "0"));
            psTemp.setString("preparedBy", DmServer.getSsnUserName(rss));
            psTemp.setString("serviceRoutine", DmServer.vNull(serviceRoutine, "0"));
            psTemp.setString("serviceNature", DmShared.iNull(ServiceNature.SERVICE.getId()).toString());
            psTemp.setString("isRateEditable", ratedEditable);
            psTemp.setString("isNightShift", nightShift);
            psTemp.setString("nightShiftAmount", DmShared.xNull(mrTemp.getNightShiftAmount()));
            psTemp.setString("isDiscountAllow", discountAllow);
            psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
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

            //<editor-fold defaultstate="collapsed" desc="INSERT IN PATIENTSERVICE_SITE_LIST">
            sqlQuery = "INSERT INTO patientServiceSiteList (CODE, site_Code)  "
                    + " VALUES (:code, :site_Code)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.foriegnKey(mrTemp.getCode()));
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

                if (cnTemp != null) {
                    cnTemp.close();
                }

            } catch (Exception e) {
            }
        }
        return rtnStatus;
    }
}
