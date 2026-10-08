package aarogya.selfToolkit.stImportData.server;

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
import dataman.dmToolkit.dmImportData.server.DmImportData_Interface;
import dataman.dmToolkit.dmImportData.shared.CSVData;
import dataman.dmToolkit.dmImportData.shared.DmImportStatus;
import dataman.dmToolkit.dmImportData.shared.ImportData_Mdl;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MedicineDepartmentInfo implements DmImportData_Interface {

    class Model {

        private String siteCode, site;
        private String departmentCode, department, medicineCode, medicine, minStock, maxStock, reOrder, binName;

        public Model() {
        }

        public String getSiteCode() {
            return siteCode;
        }

        public void setSiteCode(String siteCode) {
            this.siteCode = siteCode;
        }

        public String getSite() {
            return site;
        }

        public void setSite(String site) {
            this.site = site;
        }

        public String getDepartmentCode() {
            return departmentCode;
        }

        public void setDepartmentCode(String departmentCode) {
            this.departmentCode = departmentCode;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public String getMedicineCode() {
            return medicineCode;
        }

        public void setMedicineCode(String medicineCode) {
            this.medicineCode = medicineCode;
        }

        public String getMedicine() {
            return medicine;
        }

        public void setMedicine(String medicine) {
            this.medicine = medicine;
        }

        public String getMinStock() {
            return minStock;
        }

        public void setMinStock(String minStock) {
            this.minStock = minStock;
        }

        public String getMaxStock() {
            return maxStock;
        }

        public void setMaxStock(String maxStock) {
            this.maxStock = maxStock;
        }

        public String getReOrder() {
            return reOrder;
        }

        public void setReOrder(String reOrder) {
            this.reOrder = reOrder;
        }

        public String getBinName() {
            return binName;
        }

        public void setBinName(String binName) {
            this.binName = binName;
        }

    }
    private DmRSS rss;
    private DmHandelFile handelFileLog;
    private String lineNoDisplay = "";

    public MedicineDepartmentInfo() {

    }

    @Override
    public ImportData_Mdl getImportDataModel() {
        ImportData_Mdl fileDetail = new ImportData_Mdl();
        CSVData node;
        List<CSVData> lstCSV = new ArrayList<CSVData>();

        node = new CSVData();
        node.setHeader("Site Name");
        node.setSample("Head office [DDP]");
        node.setInstruction("{unique;mandatory;alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("Department Name");
        node.setSample("Pharmacy");
        node.setInstruction("{unique;mandatory;alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("Medicine");
        node.setSample("Paracetamol");
        node.setInstruction("{unique;mandatory;alphanumeric;250c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("Min. Level");
        node.setSample("10");
        node.setInstruction("{numeric;8c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("Max. Level");
        node.setSample("20");
        node.setInstruction("{numeric;8c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("Re Order Level");
        node.setSample("15");
        node.setInstruction("{numeric;8c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("Bin");
        node.setSample("Bin1");
        node.setInstruction("{numeric;20c}");
        lstCSV.add(node);

        fileDetail.getHmCSV().addAll(lstCSV);
        fileDetail.setImportTo("Medicine Department Info");
        fileDetail.setServerClassName(MedicineDepartmentInfo.class.getName());

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
                        if (headerName.trim().equalsIgnoreCase("Site Name")) {

                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setSite(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("Department Name")) {

                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setDepartment(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("Medicine")) {

                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 250, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setMedicine(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("Min. Level")) {

                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 8, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setMinStock(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("Max. Level")) {

                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 8, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setMaxStock(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("Re Order Level")) {

                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 8, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setReOrder(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("Bin")) {

                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 20, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setBinName(fieldValue);
                            }
                        }

                    }
                    if (isFound) {
                        status = saveMedicineDepartmentInfo(mrTemp);
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

    private int saveMedicineDepartmentInfo(Model mrTemp) {
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        int status = 0, rtnStatus = 0;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));
            spBegin = new DmSavePoint(cnTemp);

// site Name
            if (!DmShared.xNull(mrTemp.getSite()).equalsIgnoreCase("")) {
                mrTemp.setSiteCode(DmServer.getCodeViaManualCode(mrTemp.getSite(), "name", "siteMast", "code", cnTemp));
                if (DmShared.iNull(mrTemp.getSiteCode()).longValue() == 0) {
                    status = DmImportStatus.ERROR.getId();
                    throw new DmException(0, mrTemp.getSite() + " not Exists");
                }
            }

            //Department
            if (!DmShared.xNull(mrTemp.getDepartment()).equalsIgnoreCase("")) {
                mrTemp.setDepartmentCode(DmServer.getCodeViaManualCode(mrTemp.getDepartment(), "name", "subGroup", "subCode", cnTemp));
                if (DmShared.iNull(mrTemp.getDepartmentCode()).longValue() == 0) {
                    status = DmImportStatus.ERROR.getId();
                    throw new DmException(0, mrTemp.getDepartment() + " not Exists");
                }
            }

            //medicine
            if (!DmShared.xNull(mrTemp.getMedicine()).equalsIgnoreCase("")) {
                mrTemp.setMedicineCode(DmServer.getCodeViaManualCode(mrTemp.getMedicine(), "name", "itemMast", "code", cnTemp));
                if (DmShared.iNull(mrTemp.getMedicineCode()).longValue() == 0) {
                    status = DmImportStatus.ERROR.getId();
                    throw new DmException(0, mrTemp.getMedicine() + " not Exists");
                }
            }

            if (DmShared.vNull(mrTemp.getMinStock()).doubleValue() > DmShared.vNull(mrTemp.getMaxStock()).doubleValue()) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(600, " Min. Level cannot greater than Max. Level check the index ");
            }

            if ((DmShared.vNull(mrTemp.getReOrder()).doubleValue() < DmShared.vNull(mrTemp.getMinStock()).doubleValue()) || (DmShared.vNull(mrTemp.getReOrder()).doubleValue() > DmShared.vNull(mrTemp.getMaxStock()).doubleValue())) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(600, "Re-Order Level cannot is greater than  Max. Level or less than Min. Level check the index ");
            }

            sqlQuery = "Delete From itemDepartmentInfo where site_Code= :siteCode  and departmentCode =:departmentCode and itemCode = :medicine ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", mrTemp.getSiteCode());
            psTemp.setString("departmentCode", mrTemp.getDepartmentCode());
            psTemp.setString("medicine", mrTemp.getMedicineCode());

            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "insert into itemDepartmentInfo(departmentCode,itemCode, minStock, maxStock, reOrder, binName,site_Code )  "
                    + " VALUES (:departmentCode,:itemCode, :minStock, :maxStock, :reOrder,:binName, :siteCode ) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("departmentCode", DmShared.foriegnKey(mrTemp.getDepartmentCode()));
            psTemp.setString("itemCode", DmShared.foriegnKey(mrTemp.getMedicineCode()));
            psTemp.setString("minStock", DmServer.vNull(mrTemp.getMinStock()));
            psTemp.setString("maxStock", DmServer.vNull(mrTemp.getMaxStock()));
            psTemp.setString("reOrder", DmServer.vNull(mrTemp.getReOrder()));
            psTemp.setString("binName", DmShared.xNull(mrTemp.getBinName()));
            psTemp.setString("siteCode", DmShared.foriegnKey(mrTemp.getSiteCode()));

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
