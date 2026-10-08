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

public class ConsultantSpeciMaster implements DmImportData_Interface {

    class Model {

        private String code, name, nature, prescriptionTemplate, assesmentTemplate;

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

        public String getPrescriptionTemplate() {
            return prescriptionTemplate;
        }

        public void setPrescriptionTemplate(String prescriptionTemplate) {
            this.prescriptionTemplate = prescriptionTemplate;
        }

        public String getAssesmentTemplate() {
            return assesmentTemplate;
        }

        public void setAssesmentTemplate(String assesmentTemplate) {
            this.assesmentTemplate = assesmentTemplate;
        }
    }
    private DmRSS rss;
    private DmHandelFile handelFileLog;
    private String lineNoDisplay = "";

    public ConsultantSpeciMaster() {
    }

    @Override
    public ImportData_Mdl getImportDataModel() {
        ImportData_Mdl fileDetail = new ImportData_Mdl();
        CSVData node;
        List<CSVData> lstCSV = new ArrayList<CSVData>();

        node = new CSVData();
        node.setHeader("SpecialisationName");
        node.setSample("Special");
        node.setInstruction("{unique;mandatory;alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("Nature");
        node.setSample("Anesthesia");
        node.setInstruction("{mandatory;Anesthesia/Others/Physician/Surgeon/Radiologist/Pathologist;20c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("Prescription");
        node.setSample("General Prescription");
        node.setInstruction("{alphanumeric;35c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("Assesment");
        node.setSample("Initial Assesment");
        node.setInstruction("{alphanumeric;35c}");
        lstCSV.add(node);

        fileDetail.getHmCSV().addAll(lstCSV);
        fileDetail.setImportTo("Consultant Specilization Master");
        fileDetail.setServerClassName(ConsultantSpeciMaster.class.getName());

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
                        if (headerName.trim().equalsIgnoreCase("SpecialisationName")) {

                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("Nature")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 20, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setNature(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("Prescription")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 35, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPrescriptionTemplate(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("Assesment")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 35, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setAssesmentTemplate(fieldValue);
                            }
                        }
                    }
                    if (isFound) {
                        status = saveConsultantSpeciMaster(mrTemp);
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

    private int saveConsultantSpeciMaster(Model mrTemp) {
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, prescriptionTemplate = "", assesmentTemplate = "";
        int status = 0, rtnStatus = 0;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setName(DmShared.removeSpace(mrTemp.getName()));
            mrTemp.setCode(DmServer.getSerialNoInteger(rss, "consultantspecimast", cnTemp));
            if (DmShared.iNull(mrTemp.getCode()).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new UnsupportedOperationException("Invalid key.");
            }

            sqlQuery = "select count(name) as cnt from consultantspecimast where name = :name and code <> :code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getName()).toLowerCase());
            psTemp.setString("code", DmShared.xNull(mrTemp.getCode()));
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                status = DmImportStatus.WARNING.getId();
                throw new UnsupportedOperationException(mrTemp.getName() + " specialization already exists.");
            }
            rsTemp.close();
            psTemp.close();

            if (!DmShared.xNull(mrTemp.getNature()).equalsIgnoreCase("")) {
                if ((!DmShared.xNull(mrTemp.getNature()).equalsIgnoreCase("Anesthesia")) && (!DmShared.xNull(mrTemp.getNature()).equalsIgnoreCase("Others"))
                        && (!DmShared.xNull(mrTemp.getNature()).equalsIgnoreCase("Physician")) && (!DmShared.xNull(mrTemp.getNature()).equalsIgnoreCase("Surgeon"))
                        && (!DmShared.xNull(mrTemp.getNature()).equalsIgnoreCase("Radiologist")) && (!DmShared.xNull(mrTemp.getNature()).equalsIgnoreCase("Pathologist"))) {
                    status = DmImportStatus.ERROR.getId();
                    throw new UnsupportedOperationException(mrTemp.getNature() + "nature not allow.only Anesthesia/Others/Physician/Surgeon/Radiologist/Pathologist allow.");
                }
            }

            if (!DmShared.xNull(mrTemp.getPrescriptionTemplate()).equalsIgnoreCase("")) {
                prescriptionTemplate = DmServer.getCodeViaManualCode(mrTemp.getPrescriptionTemplate(), "name", "guiTemplateMast", "code", cnTemp);
                if (DmShared.iNull(prescriptionTemplate).longValue() == 0) {
                    status = DmImportStatus.WARNING.getId();
                    handelFileLog.writeLine(mrTemp.getPrescriptionTemplate() + " GUI Template not found.");
                }
            }
            if (!DmShared.xNull(mrTemp.getAssesmentTemplate()).equalsIgnoreCase("")) {
                assesmentTemplate = DmServer.getCodeViaManualCode(mrTemp.getAssesmentTemplate(), "name", "guiTemplateMast", "code", cnTemp);
                if (DmShared.iNull(assesmentTemplate).longValue() == 0) {
                    status = DmImportStatus.WARNING.getId();
                    handelFileLog.writeLine(mrTemp.getAssesmentTemplate() + " GUI Template not found.");
                }
            }

            sqlQuery = "insert into consultantSpeciMast(code, name, nature, guiTemplateCodePost, guiTemplateCodeIniAsses, preparedBy, u_EntDt) "
                    + " values (:code, :name, :nature, :guiTemplateCodePost, :guiTemplateCodeIniAsses, :preparedBy, :u_EntDt) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.xNull(mrTemp.getCode()));
            psTemp.setString("name", DmShared.xNull(mrTemp.getName()));
            psTemp.setString("nature", DmShared.xNull(mrTemp.getNature()));
            psTemp.setString("guiTemplateCodePost", DmShared.foriegnKey(prescriptionTemplate));
            psTemp.setString("guiTemplateCodeIniAsses", DmShared.foriegnKey(assesmentTemplate));
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

            } catch (Exception e) {
            }
        }
        return rtnStatus;
    }
}
