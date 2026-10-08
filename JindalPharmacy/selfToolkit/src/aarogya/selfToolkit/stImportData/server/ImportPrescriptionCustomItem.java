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

public class ImportPrescriptionCustomItem implements DmImportData_Interface {

    class Model {

        private String code;
        private String name;
        private String nature;
        private String shortName;
        private String regionalName;
        private String printSno;
        private String graceMinute;
        private String groupName;

        public Model() {

        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
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

        public String getShortName() {
            return shortName;
        }

        public void setShortName(String shortName) {
            this.shortName = shortName;
        }

        public String getRegionalName() {
            return regionalName;
        }

        public void setRegionalName(String regionalName) {
            this.regionalName = regionalName;
        }

        public String getPrintSno() {
            return printSno;
        }

        public void setPrintSno(String printSno) {
            this.printSno = printSno;
        }

        public String getGraceMinute() {
            return graceMinute;
        }

        public void setGraceMinute(String graceMinute) {
            this.graceMinute = graceMinute;
        }

    }
    private DmRSS rss;
    private DmHandelFile handelFileLog;
    private String lineNoDisplay = "";

    public ImportPrescriptionCustomItem() {

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
        node.setHeader("Name(Regional)");
        node.setSample("Dont Know");
        node.setInstruction("alphanumeric;50c");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("shortName");
        node.setSample("CS");
        node.setInstruction("{unique;mandatory;alphanumeric;10c}");
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

        node = new CSVData();
        node.setHeader("groupName");
        node.setSample("Custom");
        node.setInstruction("numeric;50c");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("graceMinute");
        node.setSample("Custom");
        node.setInstruction("numeric;50c");
        lstCSV.add(node);

        fileDetail.getHmCSV().addAll(lstCSV);
        fileDetail.setImportTo("Prescription Custom Item");
        fileDetail.setServerClassName(ImportPrescriptionCustomItem.class.getName());
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
                    mrTemp = new Model();
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
                        } else if (headerName.trim().equalsIgnoreCase("Name(Regional)")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 500, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setRegionalName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("shortName")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;

                            } else {
                                mrTemp.setShortName(fieldValue);
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
                                mrTemp.setPrintSno(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("groupName")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setGroupName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("graceMinute")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setGraceMinute(fieldValue);
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

    private int saveCard(Model mrTemp) {
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        CustomWidgetNature prescription = null;
        String sqlQuery, groupCode = "";
        int rtnStatus = 0, status = 0;

        try {

            mrTemp.setName(DmShared.removeSpace(mrTemp.getName()));
            mrTemp.setRegionalName(DmShared.removeSpace(mrTemp.getRegionalName()));
            mrTemp.setShortName(DmShared.removeSpace(mrTemp.getShortName()));
            prescription = CustomWidgetNature.getValueOf(DmShared.xNull(mrTemp.getNature()));
            if (prescription == null) {
                status = DmImportStatus.ERROR.getId();
                throw new Exception("Nature not found for" + mrTemp.getName());
            }
            mrTemp.setPrintSno(DmShared.removeSpace(mrTemp.getPrintSno()));
            mrTemp.setGroupName(DmShared.removeSpace(mrTemp.getGroupName()));

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setCode(DmServer.getSerialNoInteger(rss, "prescriptionDetailMast", cnTemp));
            if (DmShared.iNull(mrTemp.getCode()).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new UnsupportedOperationException("Invalid Key");
            }

            if (!DmShared.xNull(mrTemp.getGroupName()).trim().equalsIgnoreCase("")) {
                groupCode = DmServer.getCodeViaManualCode(mrTemp.getGroupName(), "name", "prescriptionDetailGroupMast", "code", cnTemp);
                if (DmShared.iNull(groupCode).longValue() == 0) {
                    status = DmImportStatus.WARNING.getId();
                    handelFileLog.writeLine(lineNoDisplay + " " + mrTemp.getGroupName() + " not found.");
                }
            }
            sqlQuery = "select count(pdm.name) as cnt from prescriptionDetailMast as pdm where pdm.name = :name and pdm.code <> :code and nature = :nature ";
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

            if (!DmShared.xNull(mrTemp.getShortName()).trim().equalsIgnoreCase("")) {
                sqlQuery = "Select code from prescriptionDetailMast where code <> :code and shortName = :shortName and nature = :nature ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.iNull(mrTemp.getCode()).toString());
                psTemp.setString("shortName", DmShared.xNull(mrTemp.getShortName()).trim());
                psTemp.setString("nature", DmShared.xNull(prescription.getId()).trim());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    status = DmImportStatus.WARNING.getId();
                    throw new UnsupportedOperationException(mrTemp.getShortName() + " short Name already exists.");
                }
                rsTemp.close();
                psTemp.close();
            }

            sqlQuery = "Insert into prescriptionDetailMast(code, name, shortName, regionalName, nature, groupCode, printSNo, preparedBy, u_EntDt,graceMinute) "
                    + " values (:code, :name, :shortName, :regionalName, :nature, :groupCode, :printSNo, :preparedBy, :u_EntDt,:graceMinute) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getCode()).toString());
            psTemp.setString("name", DmShared.xNull(mrTemp.getName()).trim());
            psTemp.setString("shortName", DmShared.xNull(mrTemp.getShortName()).trim());
            psTemp.setString("regionalName", DmShared.xNull(mrTemp.getRegionalName()).trim());
            psTemp.setString("nature", DmShared.xNull(prescription.getId()).trim());
            psTemp.setString("groupCode", DmShared.foriegnKey(groupCode));
            psTemp.setString("printSNo", DmServer.vNull(mrTemp.getPrintSno(), "0"));
            psTemp.setString("preparedBy", DmServer.getSsnUserName(rss));
            psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
            psTemp.setString("graceMinute", DmServer.vNull(mrTemp.getGraceMinute(), "0"));

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
