package aarogya.selfToolkit.stImportData.server;

import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import com.jolbox.bonecp.ConnectionHandle;
import com.microsoft.sqlserver.jdbc.SQLServerCallableStatement;
import com.microsoft.sqlserver.jdbc.SQLServerConnection;
import com.microsoft.sqlserver.jdbc.SQLServerDataTable;
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
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.self.shared.RecId;
import dataman.dmToolkit.dmImportData.server.DmImportData_Interface;
import dataman.dmToolkit.dmImportData.shared.CSVData;
import dataman.dmToolkit.dmImportData.shared.DmImportStatus;
import dataman.dmToolkit.dmImportData.shared.ImportData_Mdl;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AssetMaster implements DmImportData_Interface {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String vCat = "'" + pvc.ASSET_ADD_REPAIR + "'";
    private String startDate = "";

    class Model {

        private String docID, typeMode, manualCode, assetItemCode, assteItemName, assetSupplierCode, supplierName, fromDepartmentCode, fromDepartment, currentDepartmentCode, currentDepartment, site_Code,
                dateOfPurchase, dateOfInstallation, assetNature, assetNatureName, purchaseValue, nature;

        public Model() {
        }

        public String getDocID() {
            return docID;
        }

        public void setDocID(String docID) {
            this.docID = docID;
        }

        public String getTypeMode() {
            return typeMode;
        }

        public String getManualCode() {
            return manualCode;
        }

        public void setManualCode(String manualCode) {
            this.manualCode = manualCode;
        }

        public void setTypeMode(String typeMode) {
            this.typeMode = typeMode;
        }

        public String getAssetItemCode() {
            return assetItemCode;
        }

        public void setAssetItemCode(String assetItemCode) {
            this.assetItemCode = assetItemCode;
        }

        public String getAssteItemName() {
            return assteItemName;
        }

        public void setAssteItemName(String assteItemName) {
            this.assteItemName = assteItemName;
        }

        public String getAssetSupplierCode() {
            return assetSupplierCode;
        }

        public void setAssetSupplierCode(String assetSupplierCode) {
            this.assetSupplierCode = assetSupplierCode;
        }

        public String getSupplierName() {
            return supplierName;
        }

        public void setSupplierName(String supplierName) {
            this.supplierName = supplierName;
        }

        public String getFromDepartmentCode() {
            return fromDepartmentCode;
        }

        public void setFromDepartmentCode(String fromDepartmentCode) {
            this.fromDepartmentCode = fromDepartmentCode;
        }

        public String getFromDepartment() {
            return fromDepartment;
        }

        public void setFromDepartment(String fromDepartment) {
            this.fromDepartment = fromDepartment;
        }

        public String getCurrentDepartmentCode() {
            return currentDepartmentCode;
        }

        public void setCurrentDepartmentCode(String currentDepartmentCode) {
            this.currentDepartmentCode = currentDepartmentCode;
        }

        public String getCurrentDepartment() {
            return currentDepartment;
        }

        public void setCurrentDepartment(String currentDepartment) {
            this.currentDepartment = currentDepartment;
        }

        public String getSite_Code() {
            return site_Code;
        }

        public String getDateOfPurchase() {
            return dateOfPurchase;
        }

        public void setDateOfPurchase(String dateOfPurchase) {
            this.dateOfPurchase = dateOfPurchase;
        }

        public void setSite_Code(String site_Code) {
            this.site_Code = site_Code;
        }

        public String getDateOfInstallation() {
            return dateOfInstallation;
        }

        public void setDateOfInstallation(String dateOfInstallation) {
            this.dateOfInstallation = dateOfInstallation;
        }

        public String getAssetNature() {
            return assetNature;
        }

        public void setAssetNature(String assetNature) {
            this.assetNature = assetNature;
        }

        public String getAssetNatureName() {
            return assetNatureName;
        }

        public void setAssetNatureName(String assetNatureName) {
            this.assetNatureName = assetNatureName;
        }

        public String getPurchaseValue() {
            return purchaseValue;
        }

        public void setPurchaseValue(String purchaseValue) {
            this.purchaseValue = purchaseValue;
        }

        public String getNature() {
            return nature;
        }

        public void setNature(String natureName) {
            this.nature = natureName;
        }

    }

    private DmRSS rss;
    private DmHandelFile handelFileLog;
    private String lineNoDisplay = "";

    public AssetMaster() {
    }

    @Override
    public ImportData_Mdl getImportDataModel() {
        ImportData_Mdl fileDetail = new ImportData_Mdl();
        CSVData node;
        List<CSVData> lstCSV = new ArrayList<CSVData>();

        node = new CSVData();
        node.setHeader("typeMode");
        node.setSample("Opening");
        node.setInstruction("{mandatory;Opening/New}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("assetItemName");
        node.setSample("Chair Revolving");
        node.setInstruction("{mandatory;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("ManualCode");
        node.setSample("ASSET/OP/2021/AS001");
        node.setInstruction("{mandatory;20c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("fromDepartment");
        node.setSample("Material Store");
        node.setInstruction("{mandatory;alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("currentDepartment");
        node.setSample("Accounts");
        node.setInstruction("{mandatory;alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("supplierName");
        node.setSample("Dataman Computer Systems PVT (L)");
        node.setInstruction("{alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("dateOfPurchase");
        node.setSample("01-04-2021");
        node.setInstruction("{alphanumeric;10c/DD-MM-YYYY}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("dateOfInstallation");
        node.setSample("01-04-2021");
        node.setInstruction("{alphanumeric;10c/DD-MM-YYYY}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("assetNature");
        node.setSample("General");
        node.setInstruction("{General/Vehicle/Ambulance}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("purchaseValue");
        node.setSample("99999999.99");
        node.setInstruction("{numeric;11c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("nature");
        node.setSample("Expose");
        node.setInstruction("{Expose/Restricted}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("site_Code");
        node.setSample("1");
        node.setInstruction("{numeric;3c}");
        lstCSV.add(node);

        fileDetail.getHmCSV().addAll(lstCSV);
        fileDetail.setImportTo("Asset Master");
        fileDetail.setServerClassName(AssetMaster.class.getName());

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
        int rtnStatus = 0, status = 0;
        try {
            this.rss = rss;
            if (DmShared.xNull(importData.getLogFilePath()).trim().equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new Exception("Log file path can not be blank.");
            }
            handelFileLog = new DmHandelFile(importData.getLogFilePath(), true);
            startDate = importData.getStartDate();
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
                        }// as it is

                        if (headerName.trim().equalsIgnoreCase("typeMode")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setTypeMode(fieldValue.equalsIgnoreCase("Opening")?"1":"2");
                            }
                        } else if (headerName.trim().equalsIgnoreCase("assetItemName")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setAssteItemName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("ManualCode")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 20, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setManualCode(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("fromDepartment")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setFromDepartment(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("currentDepartment")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setCurrentDepartment(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("supplierName")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setSupplierName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("dateOfPurchase")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setDateOfPurchase(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("dateOfInstallation")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setDateOfInstallation(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("assetNature")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setAssetNature(fieldValue.equalsIgnoreCase("General") ? "1" : fieldValue.equalsIgnoreCase("Vehicle") ? "2" : "3");
                            }
                        } else if (headerName.trim().equalsIgnoreCase("purchaseValue")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 11, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPurchaseValue(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("nature")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setNature(fieldValue.equalsIgnoreCase("Expose") ? "1" : "2");
                            }
                        } else if (headerName.trim().equalsIgnoreCase("site_Code")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 3, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setSite_Code(DmShared.removeSpace(fieldValue));
                            }
                        }
                    }
                    if (isFound) {
                        status = saveItemMast(mrTemp);
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

    private int saveItemMast(Model mrTemp) {
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, assetItemCode = "", supplierCode = "", fromDepartmentCode = "", currDepartmentCode = "", siteCode = "",
                vType = "", voucherPrefix = "";

        int rtnStatus = 0, status = 0;
        CallableStatement csTemp = null;
        try {
            voucherPrefix = DmServer.getSsnVoucherPrefix(rss);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));
            spBegin = new DmSavePoint(cnTemp);

            RecId recid = new RecId();

            /*====================================< Get Voucher Type >====================================*/
            sqlQuery = "SELECT v_Type FROM voucher_type  WHERE  short_Name = " + vCat + " ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                vType = DmShared.xNull(rsTemp.getString("v_Type"));
            }
            rsTemp.close();
            psTemp.close();
            
            //========================================== GET DOCID & RECID ============================================================
            mrTemp.setDocID(DmServer.getDocId(DmShared.xNull(vType), voucherPrefix, cnTemp, rss));
            recid = (DmServer.getRecId("stockHead", "docId", DmShared.xNull(mrTemp.getDocID()), "recId", recid, startDate, DmShared.xNull(vType), voucherPrefix, cnTemp, rss, true));

            mrTemp.setAssteItemName(DmShared.removeSpace(mrTemp.getAssteItemName()));
            assetItemCode = DmServer.getCodeViaManualCode(mrTemp.getAssteItemName(), "name", "assetItemMast", "code", cnTemp);

            //Asset Item CODE  
            if (assetItemCode.equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, mrTemp.getAssteItemName() + " asset item not exists.");
            }

            //Supplier CODE  
            supplierCode = DmServer.getCodeViaManualCode(mrTemp.getSupplierName(), "name", "subGroup", "subCode", cnTemp);

            if (supplierCode.equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, mrTemp.getSupplierName() + " supplier not exists.");
            }
            //From Department CODE 
            fromDepartmentCode = DmServer.getCodeViaManualCode(mrTemp.getFromDepartment(), "name", "subGroup", "subCode", cnTemp);

            if (fromDepartmentCode.equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, mrTemp.getFromDepartment() + " from department not exists.");
            }
            //Current Department CODE 
            currDepartmentCode = DmServer.getCodeViaManualCode(mrTemp.getCurrentDepartment(), "name", "subGroup", "subCode", cnTemp);

            if (currDepartmentCode.equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, mrTemp.getCurrentDepartment() + " current department not exists.");
            }

            //------------------< Check Asset Duplicacy >-----------------
            sqlQuery = "SELECT COUNT(manualCode) AS cnt FROM assetMast im WHERE manualCode = :manualCode AND code <> :code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.xNull(mrTemp.getDocID()));
            psTemp.setString("manualCode", DmShared.xNull(mrTemp.getManualCode()));
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                status = DmImportStatus.WARNING.getId();
                throw new UnsupportedOperationException(mrTemp.getManualCode()+ "Manualcode already exists.");
            }
            rsTemp.close();
            psTemp.close();
            
            //---------------------------------------------------------------
            sqlQuery = "SELECT vt.v_Type  FROM voucher_type vt WHERE  vt.category = " + vCat + " ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                vType = DmShared.xNull(rsTemp.getString("v_Type"));
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "insert Into assetMast(code, manualCode, assetItemCode, assetSupplierCode, "
                        + "         assetRemark, dateOfPurchase, dateOfInstallation, assetNature, vehicleNumber, preparedBy, site_code, u_EntDt,"
                        + "         currentDepartmentCode, currentSite_Code, typeMode, fromDepartmentCode) "
                        + " values (:code, :manualCode, :assetItemCode, :assetSupplierCode, "
                        + "         :assetRemark, :dateOfPurchase, :dateOfInstallation, :assetNature, :vehicleNumber, :preparedBy, :site_code, "
                        + "         :u_EntDt, :currentDepartment, :site_code, :typeMode, :fromDepartmentCode) ";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.iNull(mrTemp.getDocID()).toString());
                psTemp.setString("manualCode", DmShared.xNull(mrTemp.getManualCode()));
                psTemp.setString("assetItemCode", DmShared.foriegnKey(assetItemCode));
                psTemp.setString("assetSupplierCode", DmShared.foriegnKey(supplierCode));
                psTemp.setString("assetRemark", DmShared.xNull(""));
                psTemp.setDate("dateOfPurchase", DmServer.convertDate(mrTemp.getDateOfPurchase()));
                psTemp.setDate("dateOfInstallation", DmServer.convertDate(mrTemp.getDateOfInstallation()));
                psTemp.setString("assetNature", DmShared.iNull(mrTemp.getAssetNature()).toString());
                psTemp.setString("vehicleNumber", DmShared.xNull(""));
                psTemp.setString("preparedBy", DmShared.xNull(DmServer.getSsnUserName(rss)));
                psTemp.setString("site_Code", DmShared.foriegnKey(DmServer.getSsnSiteCode(rss)));
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.setString("currentDepartment", DmShared.foriegnKey(currDepartmentCode));
                psTemp.setString("typeMode", DmShared.xNull(mrTemp.getTypeMode()));
                psTemp.setString("fromDepartmentCode", DmShared.foriegnKey(fromDepartmentCode));
                psTemp.executeUpdate();
                psTemp.close();

//            /*===========================< Insert record in itemSiteList >===============================*/
//            sqlQuery = "INSERT INTO itemSiteList (CODE, site_Code)  "
//                    + " VALUES (:code, :site_Code)";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("code", DmShared.foriegnKey(assetItemCode));
//            psTemp.setString("site_Code", DmShared.foriegnKey(DmServer.getSsnSiteCode(rss)));
//            psTemp.executeUpdate();
//            psTemp.close();

            /*===========================< Insert record in assetStockHead >===============================*/
            sqlQuery = "Insert Into assetStockHead(docId, v_Type,v_No, recId, v_Date, recIdPrefix, v_Prefix, u_EntDt, site_Code, preparedBy)"
                        + " Values (:docId, :v_Type, :v_No, :recId, :v_Date, :recIdPrefix, :v_Prefix, :u_EntDt,:site_Code, :userName) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getDocID()).toString());
                psTemp.setString("v_Type", DmShared.foriegnKey(vType).toString());
                psTemp.setString("v_No", DmShared.iNull(recid.getCounter()).toString());
                psTemp.setString("recId", DmShared.xNull(recid.getRecIdValue()));
                psTemp.setDate("v_Date", DmServer.convertDate(startDate));
                psTemp.setString("recIdPrefix", DmShared.xNull(DmShared.xNull(recid.getPrefix())));
                psTemp.setString("v_Prefix", voucherPrefix);
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.setString("site_code", DmShared.foriegnKey(DmServer.getSsnSiteCode(rss)));
                psTemp.setString("userName", DmShared.xNull(DmServer.getSsnUserName(rss)));
                psTemp.executeUpdate();
                psTemp.close();

            /*===========================< Insert in to assetStock >===============================*/
            sqlQuery = " Insert Into assetStock(docId,v_SNo,v_Type,v_No,recIdPrefix,recId,v_Date,departmentCode,"
                        + "  assetCode,recQty,isSystemGenerated,site_Code,u_Name,u_EntDt)"
                        + "  Values   (:docId, 1,:v_Type,:v_No,:recIdPrefix,:recId,:v_Date,:departmentCode,"
                        + "   :assetCode,1,0,:site_Code,:userName,:u_EntDt) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getDocID()).toString());
                psTemp.setString("v_Type", DmShared.foriegnKey(vType));
                psTemp.setString("v_No", DmShared.iNull(recid.getCounter()).toString());
                psTemp.setString("recIdPrefix", DmShared.xNull(DmShared.xNull(recid.getPrefix())));
                psTemp.setString("recId", DmShared.xNull(DmShared.xNull(recid.getRecIdValue())));
                psTemp.setDate("v_Date", DmServer.convertDate(startDate));
                psTemp.setString("departmentCode", DmShared.foriegnKey(currDepartmentCode));
                psTemp.setString("assetCode", DmShared.foriegnKey(mrTemp.getDocID().toString()));
                psTemp.setString("site_code", DmShared.foriegnKey(DmServer.getSsnSiteCode(rss)));
                psTemp.setString("userName", DmShared.xNull(DmServer.getSsnUserName(rss)));
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = " Insert Into assetLedger(docId,v_SNo,v_No,recIdPrefix,recId,v_Type,"
                        + "   v_Prefix,v_Date,assetCode,amtDr,isNature,typeOfExpense)"
                        + "   Values   (:docId,:v_SNo,:v_No,:recIdPrefix,:recId,:v_Type,:v_Prefix,:v_Date,"
                        + "   :assetCode,:amtDr,:isNature,:typeOfExpense) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getDocID()).toString());
                psTemp.setString("v_SNo", "1");
                psTemp.setString("v_No", DmShared.iNull(recid.getCounter()).toString());
                psTemp.setString("recIdPrefix", DmShared.xNull(DmShared.xNull(recid.getPrefix())));
                psTemp.setString("recId", DmShared.xNull(DmShared.xNull(recid.getRecIdValue())));
                psTemp.setString("v_Type", DmShared.foriegnKey(vType));
                psTemp.setString("v_Prefix", voucherPrefix);
                psTemp.setDate("v_Date", DmServer.convertDate(startDate));
                psTemp.setString("assetCode", DmShared.foriegnKey(mrTemp.getDocID().toString()));
                psTemp.setString("amtDr", DmShared.vNull(mrTemp.getPurchaseValue()).toString());
                psTemp.setString("isNature", DmShared.iNull(mrTemp.getAssetNature()).toString());
                psTemp.setString("typeOfExpense", DmShared.iNull("2").toString());
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
