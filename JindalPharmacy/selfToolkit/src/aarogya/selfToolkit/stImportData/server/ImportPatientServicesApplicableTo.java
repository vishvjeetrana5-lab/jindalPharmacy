package aarogya.selfToolkit.stImportData.server;

import aarogya.selfToolkit.stStatic.shared.STEnum;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSS;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.server.DmCsvReader;
import dataman.dmBase.self.server.DmHandelFile;
import dataman.dmBase.self.shared.DmDynamicGridRow;
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmImportData.server.DmImportData_Interface;
import dataman.dmToolkit.dmImportData.shared.CSVData;
import dataman.dmToolkit.dmImportData.shared.DmImportStatus;
import dataman.dmToolkit.dmImportData.shared.ImportData_Mdl;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ImportPatientServicesApplicableTo implements DmImportData_Interface {

    class Model {

        private String servicesName;
        private String code;
        private String shortName;

        public Model() {

        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getServicesName() {
            return servicesName;
        }

        public void setServicesName(String servicesName) {
            this.servicesName = servicesName;
        }

        public String getShortName() {
            return shortName;
        }

        public void setShortName(String shortName) {
            this.shortName = shortName;
        }

    }
    private DmRSS rss;
    private DmHandelFile handelFileLog;
    private String lineNoDisplay = "";

    @Override
    public ImportData_Mdl getImportDataModel() {
        final ImportData_Mdl fileDetail = new ImportData_Mdl();
        CSVData node;
        List<CSVData> lstCSV = new ArrayList<>();

        node = new CSVData();
        node.setHeader("servicesName");
        node.setSample("Custom");
        node.setInstruction("{unique;mandatory;alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("voucherShortName");
        node.setSample("DYCSV");
        node.setInstruction("{unique;mandatory;alphanumeric;5c}");
        lstCSV.add(node);

        fileDetail.getHmCSV().addAll(lstCSV);
        fileDetail.setImportTo(" Services Apllicable To");
        fileDetail.setServerClassName(ImportPatientServicesApplicableTo.class.getName());
        return fileDetail;

    }

    @Override
    public DmImportStatus importCSV(DmRSSPrivate rss, ImportData_Mdl importData) {
        ImportPatientServicesApplicableTo.Model mrTemp;
        DmCsvReader csvReader = null;
        File dataFile = null;
        boolean isFound;
        DmDynamicGridRow dynamicGridRow;
        String fieldValue;
        Object objValue;
        String[] csvfileFormat;
        DmImportStatus importStatus = null;
        int status = 0, rtnStatus = 0;

        try {
            this.rss = rss;
            if (DmShared.xNull(importData.getLogFilePath()).trim().equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new Exception("Log file path can not be blank");
            }
            handelFileLog = new DmHandelFile(importData.getLogFilePath(), true);

            if (DmShared.xNull(importData.getDataFilePath()).trim().equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new Exception("Data file path can not be blank");
            } else {
                csvfileFormat = ImportData_Server.getCSVFormats(importData.getHmCSV()).get("header").split(",");
                dataFile = new File(importData.getDataFilePath());

                if (dataFile.exists()) {
                    csvReader = new DmCsvReader(dataFile);
                    csvReader.readLines();
                    status = ImportData_Server.checkHeader(csvfileFormat, csvReader, handelFileLog);
                    if (status == DmImportStatus.ERROR.getId()) {
                        throw new DmException(0, "");
                    }

                } else {
                    status = DmImportStatus.ERROR.getId();
                    throw new Exception("Data file not found");
                }
                for (int i = 0; i < csvReader.getRows().size(); i++) {
                    lineNoDisplay = "Line " + String.valueOf(i + 1) + ": ";
                    dynamicGridRow = csvReader.getRows().get(i);
                    mrTemp = new ImportPatientServicesApplicableTo.Model();
                    isFound = true;

                    for (String headerName : csvfileFormat) {
                        objValue = dynamicGridRow.getCells().get(headerName.toLowerCase());
                        if (objValue == null) {
                            fieldValue = "";
                        } else {
                            fieldValue = String.valueOf(objValue);
                        }
                        if (headerName.trim().equalsIgnoreCase("servicesName")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setServicesName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("voucherShortName")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setShortName(fieldValue);
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
            } catch (Exception ex) {

            }
        }
        return importStatus;

    }

    private int saveCard(ImportPatientServicesApplicableTo.Model mrTemp) {
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        int rtnStatus = 0, status = 0;
        String sqlQuery, vType;

        try {
            mrTemp.setShortName(DmShared.removeSpace(mrTemp.getShortName()).trim());
            mrTemp.setServicesName(DmShared.removeSpace(mrTemp.getServicesName()));

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));
            spBegin = new DmSavePoint(cnTemp);
            vType = DmServer.getVType(mrTemp.getShortName(), cnTemp);

            if (DmShared.iNull(vType).intValue() == 0) {
                status = DmImportStatus.WARNING.getId();
                throw new DmException(0, mrTemp.getShortName() + " voucher not found.");
            }

            mrTemp.setCode(DmServer.getCodeViaManualCode(mrTemp.getServicesName(), "name", "patientServiceCatMast", "code", cnTemp));

            if (DmShared.iNull(mrTemp.getCode()).intValue() == 0) {
                status = DmImportStatus.WARNING.getId();
                throw new DmException(0, mrTemp.getServicesName() + " service Category not found.");
            }

            sqlQuery = "Delete From patientServiceApplicableTo where serviceCategoryCode= :serviceCategoryCode  and v_Type = :v_Type";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("serviceCategoryCode", mrTemp.getCode());
            psTemp.setString("v_Type", vType);
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "Insert Into patientServiceApplicableTo(v_Type,serviceCategoryCode) Values  "
                    + "(:v_Type,:serviceCategoryCode) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("serviceCategoryCode", DmShared.foriegnKey(mrTemp.getCode()));
            psTemp.setString("v_Type", DmShared.foriegnKey(vType));
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
            } catch (Exception ex) {

            }
        }
        return rtnStatus;
    }

}
