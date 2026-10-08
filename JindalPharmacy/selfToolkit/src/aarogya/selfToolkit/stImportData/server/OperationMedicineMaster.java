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

public class OperationMedicineMaster implements DmImportData_Interface {

    public OperationMedicineMaster() {
    }

    class Model {

        private String code, opName, qty, medi;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getOpName() {
            return opName;
        }

        public void setOpName(String opName) {
            this.opName = opName;
        }

        public String getQty() {
            return qty;
        }

        public void setQty(String qty) {
            this.qty = qty;
        }

        public String getMedi() {
            return medi;
        }

        public void setMedi(String medi) {
            this.medi = medi;
        }

        public Model() {
        }

    }

    private DmRSS rss;
    private DmHandelFile handelFileLog;
    private String lineNoDisplay = "";

    @Override
    public ImportData_Mdl getImportDataModel() {
        final ImportData_Mdl fileDetail = new ImportData_Mdl();
        CSVData node;
        List<CSVData> lstCSV = new ArrayList<CSVData>();

        node = new CSVData();
        node.setHeader("Operation Name");
        node.setSample("Cancer");
        node.setInstruction("{mandatory;alphanumeric;60c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("Medicine");
        node.setSample("brufen");
        node.setInstruction("{mandatory;alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("Qty");
        node.setSample("28");
        node.setInstruction("{mandatory;numric;8c}");
        lstCSV.add(node);

        fileDetail.getHmCSV().addAll(lstCSV);
        fileDetail.setImportTo("Operation Medi Master");
        fileDetail.setServerClassName(OperationMedicineMaster.class.getName());

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
                        if (headerName.trim().equalsIgnoreCase("Operation Name")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 60, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setOpName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("Qty")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, true, false, false, false, false, 8, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setQty(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("Medicine")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setMedi(fieldValue);
                            }
                        }

                    }
                    if (isFound) {
                        status = saveOperationMediMaster(mrTemp);
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

    private int saveOperationMediMaster(Model mrTemp) {
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, serviceCode = "", medicineCode = "";
        int status = 0, rtnStatus = 0;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));
            spBegin = new DmSavePoint(cnTemp);

            serviceCode = DmServer.getCodeViaManualCode(mrTemp.getOpName(), "name", "patientservicemast", "code", cnTemp);
            if (DmShared.iNull(serviceCode).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, DmShared.xNull(mrTemp.getOpName()) + " operation does not exist");
            }
            medicineCode = DmServer.getCodeViaManualCode(mrTemp.getMedi(), "name", "itemMast", "code", cnTemp);
            if (DmShared.iNull(medicineCode).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, DmShared.xNull(mrTemp.getMedi()) + " medicine does not exist");
            }

            sqlQuery = " Insert Into operationmedidetail(serviceCode, "
                    + " itemCode,qty) Values  "
                    + "(:serviceCode,:itemCode,:qty) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("serviceCode", DmShared.foriegnKey(serviceCode));
            psTemp.setString("itemCode", DmShared.foriegnKey(medicineCode));
            psTemp.setString("qty", mrTemp.getQty());
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
