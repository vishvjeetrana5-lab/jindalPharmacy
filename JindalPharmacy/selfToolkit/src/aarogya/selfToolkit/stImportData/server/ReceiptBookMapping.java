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

public class ReceiptBookMapping implements DmImportData_Interface {

    class Model {

        private String entryPointCode, entryVoucherCode, entryVoucherName, receiptVoucherCode, receiptVoucherName, isDefaultValue;

        public Model() {
        }

        public String getEntryPointCode() {
            return entryPointCode;
        }

        public void setEntryPointCode(String entryPointCode) {
            this.entryPointCode = entryPointCode;
        }

        public String getEntryVoucherCode() {
            return entryVoucherCode;
        }

        public void setEntryVoucherCode(String entryVoucherCode) {
            this.entryVoucherCode = entryVoucherCode;
        }

        public String getReceiptVoucherCode() {
            return receiptVoucherCode;
        }

        public void setReceiptVoucherCode(String receiptVoucherCode) {
            this.receiptVoucherCode = receiptVoucherCode;
        }

        public String getEntryVoucherName() {
            return entryVoucherName;
        }

        public void setEntryVoucherName(String entryVoucherName) {
            this.entryVoucherName = entryVoucherName;
        }

        public String getReceiptVoucherName() {
            return receiptVoucherName;
        }

        public void setReceiptVoucherName(String receiptVoucherName) {
            this.receiptVoucherName = receiptVoucherName;
        }

        public String getIsDefaultValue() {
            return isDefaultValue;
        }

        public void setIsDefaultValue(String isDefaultValue) {
            this.isDefaultValue = isDefaultValue;
        }

    }

    private DmRSS rss;
    private DmHandelFile handelFileLog;

    private String lineNoDisplay = "";

    public ReceiptBookMapping() {
    }

    @Override
    public ImportData_Mdl getImportDataModel() {
        final ImportData_Mdl fileDetail = new ImportData_Mdl();
        CSVData node;
        List<CSVData> lstCSV = new ArrayList<CSVData>();

        node = new CSVData();
        node.setHeader("Entry Point Name");
        node.setSample("UHID");
        node.setInstruction("{mandatory;alphanumeric;200c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("Entry Voucher Name");
        node.setSample("UHID");
        node.setInstruction("{mandatory;alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("Receipt Voucher Name");
        node.setSample("Other");
        node.setInstruction("{alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("Is Default");
        node.setSample("No");
        node.setInstruction("{mandatory;Yes/No;5c}");
        lstCSV.add(node);

        fileDetail.getHmCSV().addAll(lstCSV);
        fileDetail.setImportTo("Receipt Book Mapping");
        fileDetail.setServerClassName(ReceiptBookMapping.class.getName());

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
                        if (headerName.trim().equalsIgnoreCase("Entry Point Name")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 200, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setEntryPointCode(fieldValue);
                            }
                        }

                        if (headerName.trim().equalsIgnoreCase("Entry Voucher Name")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setEntryVoucherName(fieldValue);
                            }
                        }
                        if (headerName.trim().equalsIgnoreCase("Receipt Voucher Name")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setReceiptVoucherName(fieldValue);
                            }
                        }
                        if (headerName.trim().equalsIgnoreCase("Is Default")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 5, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setIsDefaultValue(fieldValue);
                            }
                        }
                    }
                    if (isFound) {
                        status = saveCheckList(mrTemp);
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

    private int saveCheckList(Model mrTemp) {
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, isDefaultCode;
        int status = 0, rtnStatus = 0;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setEntryPointCode(DmShared.removeSpace(mrTemp.getEntryPointCode()));
            mrTemp.setEntryVoucherCode(DmServer.getCodeViaManualCode(mrTemp.getEntryVoucherName(), "short_Name", "voucher_Type", "v_Type", cnTemp));
            mrTemp.setReceiptVoucherCode(DmServer.getCodeViaManualCode(mrTemp.getReceiptVoucherName(), "short_Name", "voucher_Type", "v_Type", cnTemp));
            mrTemp.setEntryVoucherName(DmShared.xNull(mrTemp.getEntryVoucherName()));

            if (DmShared.xNull(mrTemp.getEntryPointCode()).equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new UnsupportedOperationException("Invalid key.");
            }
            isDefaultCode = DmShared.xNull(mrTemp.getIsDefaultValue()).trim().equalsIgnoreCase("No") ? "0"
                    : (DmShared.xNull(mrTemp.getIsDefaultValue()).trim().equalsIgnoreCase("Yes") ? "1" : "");
            if (isDefaultCode.equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, isDefaultCode + " Is Default doesn't exist..");
            }

            sqlQuery = " SELECT COUNT(entryPoint) AS cnt FROM receiptBookMapping WHERE LOWER(entryPoint) = :entryPoint and v_Type= :v_Type ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("entryPoint", DmShared.xNull(mrTemp.getEntryPointCode()));
            psTemp.setString("v_Type", DmShared.foriegnKey(mrTemp.getEntryVoucherCode()));

            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                throw new UnsupportedOperationException("Record already exists.");
            }
            rsTemp.close();
            psTemp.close();

            if (isDefaultCode.equalsIgnoreCase("1")) {
                sqlQuery = " SELECT COUNT(entryPoint) AS cnt FROM receiptBookMapping WHERE LOWER(entryPoint) = :entryPoint "
                        + "  and isDefault = 1  ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("entryPoint", DmShared.xNull(mrTemp.getEntryPointCode()));
                rsTemp = psTemp.executeQuery();
                rsTemp.next();
                if (rsTemp.getLong("cnt") > 0) {
                    throw new DmException(0, "Record with Is Default YES already exists.");
                }
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "Insert Into receiptBookMapping( entryPoint ,v_Type, v_Receipt,isDefault ) Values  "
                    + "(:entryPoint, :v_Type, :v_Receipt, :isDefault) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("entryPoint", DmShared.xNull(mrTemp.getEntryPointCode()));
            psTemp.setString("v_Type", DmShared.foriegnKey(mrTemp.getEntryVoucherCode()));
            psTemp.setString("v_Receipt", DmShared.foriegnKey(mrTemp.getReceiptVoucherCode()));
            psTemp.setString("isDefault", isDefaultCode);
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
