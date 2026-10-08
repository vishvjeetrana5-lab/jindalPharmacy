package aarogya.selfToolkit.stImportData.server;

import dataman.dmToolkit.dmImportData.server.DmImportData_Interface;
import dataman.dmToolkit.dmImportData.shared.CSVData;
import dataman.dmToolkit.dmImportData.shared.ImportData_Mdl;
import aarogya.projectMenu.self.shared.pmDiagnostic;
import aarogya.projectMenu.self.shared.pmMaster;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
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

public class ServiceCategoryMaster implements DmImportData_Interface {

    class Model {

        private String code, name,
                printSNo, itsName, serviceAccount;

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

        public String getPrintSNo() {
            return printSNo;
        }

        public void setPrintSNo(String printSNo) {
            this.printSNo = printSNo;
        }

        public String getItsName() {
            return itsName;
        }

        public void setItsName(String itsName) {
            this.itsName = itsName;
        }

        public String getServiceAccount() {
            return serviceAccount;
        }

        public void setServiceAccount(String serviceAccount) {
            this.serviceAccount = serviceAccount;
        }

    }
    private DmRSS rss;
    private DmHandelFile handelFileLog;
    private String lineNoDisplay = "";

    public ServiceCategoryMaster() {
    }

    @Override
    public ImportData_Mdl getImportDataModel() {
        ImportData_Mdl fileDetail = new ImportData_Mdl();
        CSVData node;
        List<CSVData> lstCSV = new ArrayList<CSVData>();

        node = new CSVData();
        node.setHeader("categoryName");
        node.setSample("Adjustment");
        node.setInstruction("{unique;mandatory;alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("printSNo");
        node.setSample("2");
        node.setInstruction("{numeric;4c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("purchaseServiceA/c");
        node.setSample("Computer Expenses");
        node.setInstruction("{alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("ITSOnSale");
        node.setSample("GST");
        node.setInstruction("{alphanumeric;50c}");
        lstCSV.add(node);

        fileDetail.getHmCSV().addAll(lstCSV);
        fileDetail.setImportTo("Service Category Master");
        fileDetail.setServerClassName(ServiceCategoryMaster.class.getName());

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
        String[] csvFileFormat;
        Object objValue;
        DmImportStatus importStatus = null;
        int rtnStatus = 0, status = 0;
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
                        if (headerName.trim().equalsIgnoreCase("categoryName")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setName(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("printSNo")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 4, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPrintSNo(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("purchaseServiceA/c")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setServiceAccount(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("ITSOnSale")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setItsName(DmShared.removeSpace(fieldValue));
                            }
                        }
                    }
                    if (isFound) {
                        status = saveServiceCategory(mrTemp);
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

    private int saveServiceCategory(Model mrTemp) {
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, serviceAc = "", itsCode = "";
        String errorMsg = "";
        ProjectVoucherCategory pvc = new ProjectVoucherCategory();
        String filterCategory = "'" + pvc.OPD_SERVICE + "','" + pvc.IPD_SERVICE + "','"
                + pvc.ORGANIZATION_RECEIPT + "','" + pvc.IPD_BILL + "','" + pvc.HOURLY_SERVICE + "','" + pvc.DAY_CARE_SERVICE + "','"
                + pvc.IPD_SERVICE_BILLABLE + "','" + pvc.OPENING_BILL_ENTRY + "','" + pvc.EMERGENCY_SERVICE + "'";
        int rtnStatus = 0, status = 0;

        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended lstNode;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setName(DmShared.removeSpace(mrTemp.getName()));
            mrTemp.setCode(DmServer.getSerialNoInteger(rss, "patientserviceCatMast", cnTemp));
            if (DmShared.iNull(mrTemp.getCode()).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new UnsupportedOperationException("Invalid key.");
            }

            // ======================= CHECK IF CATEGORY IS ALREADY EXISTS =========================================
            sqlQuery = "SELECT psc.filterNature FROM patientserviceCatMast as psc WHERE psc.name = :name AND psc.code <> :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getName()));
            psTemp.setString("code", DmShared.xNull(mrTemp.getCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                switch (DmShared.iNull(rsTemp.getString("filterNature")).intValue()) {
                    case 0:
                        errorMsg = "in " + new pmMaster().SERVICE_CATEGORY_MASTER.caption;
                        break;
                    case 1:
                        errorMsg = "in " + new pmDiagnostic().DIAGNOSTIC_CATEGORY_MASTER.caption;
                        break;
                    case 4:
                        errorMsg = "in " + new pmMaster().OVERHEAD_SERVICE_CATEGORY_MASTER.caption;
                        break;
                    default:
                        errorMsg = "";
                        break;
                }
                status = DmImportStatus.WARNING.getId();
                throw new DmException(0, DmShared.xNull(mrTemp.getName()) + " Name already exists " + errorMsg + ".");
            }
            rsTemp.close();
            psTemp.close();
            //================================< GET Service A/C CODE >====================================================
            if (!DmShared.xNull(mrTemp.getServiceAccount()).trim().equalsIgnoreCase("")) {
                serviceAc = DmServer.getCodeViaManualCode(mrTemp.getServiceAccount(), "name", "subGroup", "subCode", cnTemp);
                if (DmShared.iNull(serviceAc).longValue() == 0) {
                    status = DmImportStatus.WARNING.getId();
                    handelFileLog.writeLine(lineNoDisplay + mrTemp.getServiceAccount() + " not inserted. A/c not exists in Account Master.");
                }
            }
            //=========================================< END >=============================================================
            //================================< GET Its  CODE >====================================================
            if (!DmShared.xNull(mrTemp.getItsName()).trim().equalsIgnoreCase("")) {
                itsCode = DmServer.getCodeViaManualCode(mrTemp.getItsName(), "name", "its", "code", cnTemp);
                if (DmShared.iNull(itsCode).longValue() == 0) {
                    status = DmImportStatus.WARNING.getId();
                    handelFileLog.writeLine(lineNoDisplay + mrTemp.getItsName() + " not inserted. ITS not exists in ITS");
                }
            }
            //=========================================< END >=============================================================

            // ======================= INSERT PATIENT SERVICE CAT MAST  TABLE ======================================
            sqlQuery = "Insert Into patientservicecatmast(code,name,printSNo,filterNature,its_Code,serviceAc, "
                    + " u_EntDt, preparedBy) Values  "
                    + " (:code , :name, :printSNo,:filterNature,:its_Code,:serviceAc,:u_EntDt, "
                    + " :preparedBy) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", mrTemp.getCode());
            psTemp.setString("name", mrTemp.getName());
            psTemp.setString("printSNo", DmServer.vNull(mrTemp.getPrintSNo(), "0"));
            psTemp.setString("filterNature", "0");
            psTemp.setString("preparedBy", DmServer.getSsnUserName(rss));
            psTemp.setString("its_Code", DmShared.foriegnKey(itsCode));
            psTemp.setString("serviceAc", DmShared.foriegnKey(serviceAc));
            psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
            psTemp.executeUpdate();
            psTemp.close();
            // ====================================== END ==========================================================

            sqlQuery = " SELECT vt.v_Type, vt.description as voucherName, vt.category "
                    + " FROM voucher_Type AS vt "
                    + " WHERE (vt.category IN (" + filterCategory + "))";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lstNode = new KeyFieldExtended();
                lstNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("v_Type")));
                lstNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("voucherName")));
                lstTemp.add(lstNode);
            }
            rsTemp.close();
            psTemp.close();

            for (int i = 0; i < lstTemp.size(); i++) {
                KeyFieldExtended grdVoucher = lstTemp.get(i);
                if (!DmShared.xNull(grdVoucher.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                    sqlQuery = "Insert Into patientServiceApplicableTo(v_Type,serviceCategoryCode) Values  "
                            + "(:v_Type,:serviceCategoryCode) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("serviceCategoryCode", DmShared.foriegnKey(mrTemp.getCode()));
                    psTemp.setString("v_Type", DmShared.foriegnKey(grdVoucher.getSharedKeyFieldCode()));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

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
