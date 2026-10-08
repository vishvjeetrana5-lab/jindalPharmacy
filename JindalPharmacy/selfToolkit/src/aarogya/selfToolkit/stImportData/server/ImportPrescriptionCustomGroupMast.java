package aarogya.selfToolkit.stImportData.server;

import aarogya.selfToolkit.stStatic.shared.STEnum.CustomWidgetNature;
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

public class ImportPrescriptionCustomGroupMast implements DmImportData_Interface {

    class Model {

        private String code;
        private String name;
        private String nature;
        private String printSNo;

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

        public String getNature() {
            return nature;
        }

        public void setNature(String nature) {
            this.nature = nature;
        }

        public String getPrintSNo() {
            return printSNo;
        }

        public void setPrintSNo(String printSNo) {
            this.printSNo = printSNo;
        }

    }

    private DmRSS rss;
    private DmHandelFile handelFileLog;
    private String lineNoDisplay = "";

    public ImportPrescriptionCustomGroupMast() {
    }

    @Override
    public ImportData_Mdl getImportDataModel() {
        final ImportData_Mdl fileDetail = new ImportData_Mdl();
        CSVData node;
        List<CSVData> lstCSV = new ArrayList<>();

        node = new CSVData();
        node.setHeader("Name");
        node.setSample("Custom");
        node.setInstruction("{unique;mandatory;alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("Nature");
        node.setSample("Custom");
        node.setInstruction("Physical Examination/Complaints/Comorbid/Family History/Habit/History Of Past Illness/History Of Present Illness/Systemic Examination/"
                + " Instruction/Medicine Instruction/Precaution/Urgent Care/Personal History/Obstetrics History/Menstrual History/Obstetric Examination/Gynaecological Examination/"
                + " Menstrual Examination/Development History/Perinatal History/Immunization History/Feeding History");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("printSNo");
        node.setSample("Yes");
        node.setInstruction("alphanumeric;(Yes/No);3c");
        lstCSV.add(node);

        fileDetail.getHmCSV().addAll(lstCSV);
        fileDetail.setImportTo("Prescription Custom Group");
        fileDetail.setServerClassName(ImportPrescriptionCustomGroupMast.class.getName());
        return fileDetail;

    }

    @Override
    public DmImportStatus importCSV(DmRSSPrivate rss, ImportData_Mdl importData) {
        ImportPrescriptionCustomGroupMast.Model mrTemp;
        DmCsvReader csvReader = null;
        File dataFile = null;
        boolean isFound;
        DmDynamicGridRow dynamicGridRow;
        String fieldValue;
        Object objValue;
        String[] csvfileFormat;
        int status = 0, rtnStatus = 0;
        DmImportStatus importStatus = null;

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
                    mrTemp = new ImportPrescriptionCustomGroupMast.Model();
                    isFound = true;

                    for (String headerName : csvfileFormat) {
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
                        } else if (headerName.trim().equalsIgnoreCase("Nature")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setNature(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("printSNo")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPrintSNo(fieldValue);
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

    private int saveCard(ImportPrescriptionCustomGroupMast.Model mrTemp) {
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        CustomWidgetNature prescription = null;

        String sqlQuery;
        int status = 0, rtnStatus = 0;
        try {

            mrTemp.setName(DmShared.removeSpace(mrTemp.getName()));
            prescription = CustomWidgetNature.getValueOf(DmShared.xNull(mrTemp.getNature()));
            if (prescription == null) {
                status = DmImportStatus.ERROR.getId();
                throw new Exception("Nature not found for" + mrTemp.getName());
            }
            mrTemp.setPrintSNo(DmShared.removeSpace(mrTemp.getPrintSNo()));

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setCode(DmServer.getSerialNoInteger(rss, "prescriptionDetailGroupMast", cnTemp));
            if (DmShared.iNull(mrTemp.getCode()).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new UnsupportedOperationException("Invalid Key");
            }

            sqlQuery = "select count(pdm.code) as cnt from prescriptionDetailGroupMast as pdm where pdm.name = :name and pdm.code <> :code and nature = :nature ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getName()));
            psTemp.setString("code", DmShared.xNull(mrTemp.getCode()));
            psTemp.setString("nature", DmShared.xNull(prescription.getId()).trim());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                status = DmImportStatus.WARNING.getId();
                throw new DmException(0, DmShared.xNull(mrTemp.getName()) + " Name already exists.");
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "Insert into prescriptionDetailGroupMast(code, name, nature, printSNo, preparedBy, u_EntDt) "
                    + " values (:code, :name, :nature, :printSNo, :preparedBy, :u_EntDt) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getCode()).toString());
            psTemp.setString("name", DmShared.xNull(mrTemp.getName()).trim());
            psTemp.setString("nature", DmShared.xNull(prescription.getId()).trim());
            psTemp.setString("printSNo", DmServer.vNull(mrTemp.getPrintSNo(), "0"));
            psTemp.setString("preparedBy", DmServer.getSsnUserName(rss));
            psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());

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
