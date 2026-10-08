package dataman.dmToolkit.dmImportData.server;

import dataman.dmToolkit.dmImportData.shared.CSVData;
import dataman.dmToolkit.dmImportData.shared.ImportData_Mdl;
import dataman.dmBase.self.shared.DmDynamicGridRow;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSS;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.server.DmCsvReader;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.jolbox.bonecp.ConnectionHandle;
import com.microsoft.sqlserver.jdbc.SQLServerCallableStatement;
import com.microsoft.sqlserver.jdbc.SQLServerConnection;
import com.microsoft.sqlserver.jdbc.SQLServerDataTable;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.server.DmHandelFile;
import dataman.dmToolkit.dmImportData.shared.DmImportStatus;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;

public class ImportAccountMaster implements DmImportData_Interface {

    LibraryVoucherCategory lvc = new LibraryVoucherCategory();
    private String vCat = lvc.ACCOUNT_MASTER;

    class Model {

        private String acNamePrefix;
        private String code;
        private String manualCode;
        private String accountName;
        private String displayName;
        private String acGroup;
        private String nature;
        private String costCenter;
        private String address1;
        private String address2;
        private String city;
        private String zone;
        private String pinCode;
        private String phoneNo;
        private String mobileNo;
        private String faxNo;
        private String email;
        private String location;
        private String partyCategory;
        private String tdsCategory;
        private String referenceTo;
        private String creditLimit;
        private String creditPeriod;
        private String PAN;
        private String TIN;
        private String isDuplicate;
        private String gstin;
        private String IECNo;
        private String ledgerGroup;
        private String partyType;
        private String remark;

        public Model() {
        }

        public String getAcNamePrefix() {
            return acNamePrefix;
        }

        public void setAcNamePrefix(String acNamePrefix) {
            this.acNamePrefix = acNamePrefix;
        }

        public String getMannualCode() {
            return manualCode;
        }

        public void setMannualCode(String manualCode) {
            this.manualCode = manualCode;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getAccountName() {
            return accountName;
        }

        public void setAccountName(String accountName) {
            this.accountName = accountName;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getAcGroup() {
            return acGroup;
        }

        public void setAcGroup(String acGroup) {
            this.acGroup = acGroup;
        }

        public String getNature() {
            return nature;
        }

        public void setNature(String nature) {
            this.nature = nature;
        }

        public String getCostCenter() {
            return costCenter;
        }

        public void setCostCenter(String costCenter) {
            this.costCenter = costCenter;
        }

        public String getAddress1() {
            return address1;
        }

        public void setAddress1(String address1) {
            this.address1 = address1;
        }

        public String getAddress2() {
            return address2;
        }

        public void setAddress2(String address2) {
            this.address2 = address2;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getZone() {
            return zone;
        }

        public void setZone(String zone) {
            this.zone = zone;
        }

        public String getPinCode() {
            return pinCode;
        }

        public void setPinCode(String pinCode) {
            this.pinCode = pinCode;
        }

        public String getPhoneNo() {
            return phoneNo;
        }

        public void setPhoneNo(String phoneNo) {
            this.phoneNo = phoneNo;
        }

        public String getMobileNo() {
            return mobileNo;
        }

        public void setMobileNo(String mobileNo) {
            this.mobileNo = mobileNo;
        }

        public String getFaxNo() {
            return faxNo;
        }

        public void setFaxNo(String faxNo) {
            this.faxNo = faxNo;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getPartyCategory() {
            return partyCategory;
        }

        public void setPartyCategory(String partyCategory) {
            this.partyCategory = partyCategory;
        }

        public String getTdsCategory() {
            return tdsCategory;
        }

        public void setTdsCategory(String tdsCategory) {
            this.tdsCategory = tdsCategory;
        }

        public String getReferenceTo() {
            return referenceTo;
        }

        public void setReferenceTo(String referenceTo) {
            this.referenceTo = referenceTo;
        }

        public String getCreditLimit() {
            return creditLimit;
        }

        public void setCreditLimit(String creditLimit) {
            this.creditLimit = creditLimit;
        }

        public String getCreditPeriod() {
            return creditPeriod;
        }

        public void setCreditPeriod(String creditPeriod) {
            this.creditPeriod = creditPeriod;
        }

        public String getPAN() {
            return PAN;
        }

        public void setPAN(String PAN) {
            this.PAN = PAN;
        }

        public String getTIN() {
            return TIN;
        }

        public void setTIN(String TIN) {
            this.TIN = TIN;
        }

        public String getIsDuplicate() {
            return isDuplicate;
        }

        public void setIsDuplicate(String isDuplicate) {
            this.isDuplicate = isDuplicate;
        }

        public String getGstin() {
            return gstin;
        }

        public void setGstin(String gstin) {
            this.gstin = gstin;
        }

        public String getIECNo() {
            return IECNo;
        }

        public void setIECNo(String IECNo) {
            this.IECNo = IECNo;
        }

        public String getLedgerGroup() {
            return ledgerGroup;
        }

        public void setLedgerGroup(String ledgerGroup) {
            this.ledgerGroup = ledgerGroup;
        }

        public String getPartyType() {
            return partyType;
        }

        public void setPartyType(String partyType) {
            this.partyType = partyType;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

    }

    private DmRSS rss;
    private DmHandelFile handelFileLog;
    private String lineNoDisplay = "", filterNature;

    public ImportAccountMaster() {
    }

    @Override
    public ImportData_Mdl getImportDataModel() {
        final ImportData_Mdl fileDetail = new ImportData_Mdl();
        CSVData node;
        List<CSVData> lstCSV = new ArrayList<CSVData>();

        node = new CSVData();
        node.setHeader("Initial");
        node.setSample("Mr");
        node.setInstruction("{alphanumeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("Name");
        node.setSample("Mohit");
        node.setInstruction("{unique;mandatory;alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("manualCode");
        node.setSample("manualCode");
        node.setInstruction("{unique;alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("DisplayName");
        node.setSample("Harry");
        node.setInstruction("{mandatory;alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("groupName");
        node.setSample("Bank OD A/c");
        node.setInstruction("{mandatory;alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("Nature");
        node.setSample("Cash");
        node.setInstruction("{mandatory;alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("LedgerGroup");
        node.setSample("Reliance Group");
        node.setInstruction("{alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("CostCenter");
        node.setSample("CostCenter1");
        node.setInstruction("{alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("Address1");
        node.setSample("CP");
        node.setInstruction("{alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("Address2");
        node.setSample("Sipri");
        node.setInstruction("{alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("City");
        node.setSample("Kanpur");
        node.setInstruction("{alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("Zone");
        node.setSample("jhansi Zone");
        node.setInstruction("{alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("Pin");
        node.setSample("284002");
        node.setInstruction("{alphanumeric;6c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("Phone");
        node.setSample("6884571454");
        node.setInstruction("{alphanumeric;35c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("Mobile");
        node.setSample("6887125648");
        node.setInstruction("{alphanumeric;35c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("Fax");
        node.setSample("115544");
        node.setInstruction("{alphanumeric;35c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("Email");
        node.setSample("emailm13@gmail.com");
        node.setInstruction("{alphanumeric;250c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("Location");
        node.setSample("Out Of State");
        node.setInstruction("{alphanumeric;Out Of State/With In State/Out Of Country/Not Applicable/;35c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("PartyCategory");
        node.setSample("With In District");
        node.setInstruction("{alphanumeric;With In District/Out Of District;35c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("TDSCategory");
        node.setSample("TDSCategory1");
        node.setInstruction("{alphanumeric;35c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("ReferencedTo");
        node.setSample("Wafa Khan");
        node.setInstruction("{alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("CreditLimit");
        node.setSample("1234567890");
        node.setInstruction("{alphanumeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("CreditPeriod");
        node.setSample("10");
        node.setInstruction("{alphanumeric;5c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("Pan");
        node.setSample("6444444666");
        node.setInstruction("{alphanumeric;20c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("Tin");
        node.setSample("12345678901");
        node.setInstruction("{alphanumeric;20c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("Duplicate");
        node.setSample("No");
        node.setInstruction("{Yes/No;alphanumeric;3c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("GSTIN");
        node.setSample("12345678901");
        node.setInstruction("{alphanumeric;20c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("IECNo");
        node.setSample("113156545654");
        node.setInstruction("{alphanumeric;35c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("PartyType");
        node.setSample("Registered");
        node.setInstruction("{alphanumeric;Registered/Unregistered;25c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("Remark");
        node.setSample("M");
        node.setInstruction("{alphanumeric;250c}");
        lstCSV.add(node);

        fileDetail.getHmCSV().addAll(lstCSV);
        fileDetail.setImportTo("Account Master");
        fileDetail.setServerClassName(ImportAccountMaster.class.getName());

        return fileDetail;
    }

    @Override
    public DmImportStatus importCSV(DmRSSPrivate rss, ImportData_Mdl importData) {
        Model mrTemp;
        DmCsvReader csvReader = null;
        File dataFile = null;
        Connection cnTemp = null;
        boolean isFound;
        DmDynamicGridRow dynamicGridRow;
        String fieldValue;
        Object objValue;
        String[] csvFileFormat;
        DmImportStatus importStatus = null;
        int status = 0, rtnStatus = 0;
        try {

            this.rss = rss;
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));
            if (DmShared.xNull(importData.getLogFilePath()).trim().equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new Exception("Log file path can not be blank.");
            }
            handelFileLog = new DmHandelFile(importData.getLogFilePath(), true);

            filterNature = DmServer.getCodeViaManualCode("LG", "manualCode", "subgroupNature", "Code", cnTemp);

            if (DmShared.iNull(filterNature).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, "Ledger 'LG' nature not exists.");
            }
            cnTemp.close();
            if (DmShared.xNull(importData.getDataFilePath()).trim().equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new Exception("Data file path can not be blank.");
            } else {
                csvFileFormat = DmImportData_Server.getCSVFormats(importData.getHmCSV()).get("header").split(",");
                dataFile = new File(importData.getDataFilePath());

                if (dataFile.exists()) {
                    csvReader = new DmCsvReader(dataFile);
                    csvReader.readLines();
                    status = DmImportData_Server.checkHeader(csvFileFormat, csvReader, handelFileLog);
                    if (status == DmImportStatus.ERROR.getId()) {
                        throw new DmException(0, "");
                    }
                } else {
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
                        if (headerName.trim().equalsIgnoreCase("Initial")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setAcNamePrefix(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("Name")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setAccountName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("manualCode")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setMannualCode(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("DisplayName")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setDisplayName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("groupName")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setAcGroup(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("Nature")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setNature(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("CostCenter")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setCostCenter(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("Address1")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setAddress1(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("Address2")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setAddress2(fieldValue);
                            }

                        } else if (headerName.trim().equalsIgnoreCase("City")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setCity(fieldValue);
                            }

                        } else if (headerName.trim().equalsIgnoreCase("Zone")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setZone(fieldValue);
                            }

                        } else if (headerName.trim().equalsIgnoreCase("Pin")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 6, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPinCode(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("Phone")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 35, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPhoneNo(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("Mobile")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 35, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setMobileNo(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("Fax")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 35, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setFaxNo(fieldValue);
                            }

                        } else if (headerName.trim().equalsIgnoreCase("Email")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 250, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setEmail(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("Location")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 35, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setLocation(fieldValue);
                            }

                        } else if (headerName.trim().equalsIgnoreCase("PartyCategory")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 35, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPartyCategory(fieldValue);
                            }

                        } else if (headerName.trim().equalsIgnoreCase("TDSCategory")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 35, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setTdsCategory(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("ReferencedTo")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setReferenceTo(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("CreditLimit")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setCreditLimit(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("CreditPeriod")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 5, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setCreditPeriod(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("Pan")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 20, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPAN(fieldValue);
                            }

                        } else if (headerName.trim().equalsIgnoreCase("Tin")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 20, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setTIN(fieldValue);
                            }

                        } else if (headerName.trim().equalsIgnoreCase("Duplicate")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 3, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setIsDuplicate(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("GSTIN")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 35, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setGstin(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("IECNo")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 35, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setIECNo(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("LedgerGroup")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setLedgerGroup(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("PartyType")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 35, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPartyType(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("Remark")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 250, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setRemark(fieldValue);
                            }
                        }
                    }
                    if (isFound) {
                        status = saveAccount(mrTemp);
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
            importStatus = DmImportStatus.getImportStatus(rtnStatus);
            try {
                if (csvReader != null) {
                    csvReader = null;
                }
                if (dataFile != null) {
                    dataFile = null;
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
                if (handelFileLog != null) {
                    handelFileLog.close();
                }
            } catch (Exception e) {
            }
        }

        return importStatus;
    }

    private int saveAccount(Model mrTemp) {
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        CallableStatement csTemp = null;
        String sqlQuery, manualCode = "", natureCode = "", costCenterCode = "", locationCode = "", partyCatCode = "", tdsCatCode = "",
                referenceCode = "", ledgerCode = "", duplicateCode = "", partyType = "", groupCode = "", city = "", zone = "", vType = "";
        int status = 0, rtnStatus = 0;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setAccountName(DmShared.removeSpace(mrTemp.getAccountName()));
            mrTemp.setCode(DmServer.getSerialNoBigInt(rss, "subGroup", cnTemp));
            if (DmShared.iNull(mrTemp.getCode()).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new UnsupportedOperationException("Invalid key.");
            }

            // ******************************** CHECK FOR DUPLICATE A/C NAME *****************************************
            sqlQuery = "SELECT COUNT(sG.name) AS cnt FROM subGroup as sG WHERE sG.name = :Name AND sG.subCode <> :subCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("subCode", DmShared.xNull(mrTemp.getCode()));
            psTemp.setString("Name", DmShared.xNull(mrTemp.getAccountName()));
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                status = DmImportStatus.WARNING.getId();
                throw new DmException(0, DmShared.xNull(mrTemp.getAccountName()) + " a/c name already exists.");
            }
            rsTemp.close();
            psTemp.close();
            // ******************************************* END ******************************************************

            // ******************************** CHECK FOR DUPLICATE TIN NO *****************************************
            if (!DmShared.xNull(mrTemp.getTIN()).equalsIgnoreCase("")) {
                sqlQuery = "Select Count(*) As Cnt From SubGroup where TinNo = :tinNo  and SubCode<> :subCode ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("tinNo", mrTemp.getTIN());
                psTemp.setString("subCode", mrTemp.getCode());
                rsTemp = psTemp.executeQuery();
                rsTemp.next();
                if (rsTemp.getLong("cnt") > 0) {
                    status = DmImportStatus.WARNING.getId();
                    throw new UnsupportedOperationException("Duplicate TIN Not Allowed !!!");
                }
                rsTemp.close();
                psTemp.close();
            }
            // ******************************************* END ****************************************************

            // ************************************** GET VOUCHER TYPE *********************************************
            vType = DmServer.getVType(vCat, cnTemp);
            if (DmShared.iNull(vType).longValue() == 0) {
                status = DmImportStatus.WARNING.getId();
                throw new DmException(0, "'" + vCat + "' is not found in voucher Type.");
            }
            // ******************************************* END ****************************************************

            // ************************************** GET MANUAL CODE *********************************************
            if (DmShared.xNull(mrTemp.getMannualCode()).trim().equalsIgnoreCase("")) {
                HashMap<String, FieldUDT_Mdl> fieldUDTHashMap = new HashMap<String, FieldUDT_Mdl>();
                fieldUDTHashMap.put("accountName", new FieldUDT_Mdl("accountName", DmShared.xNull(mrTemp.getAccountName()), DmShared.xNull(mrTemp.getAccountName()).trim()));
                fieldUDTHashMap.put("acGroup", new FieldUDT_Mdl("acGroup", DmShared.xNull(mrTemp.getAcGroup()).toString(), DmShared.xNull(mrTemp.getAcGroup()).trim()));
                fieldUDTHashMap.put("nature", new FieldUDT_Mdl("nature", DmShared.xNull(mrTemp.getNature()).toString(), DmShared.xNull(mrTemp.getNature()).trim()));
                csTemp = ((SQLServerConnection) ((ConnectionHandle) cnTemp).getInternalConnection()).prepareCall("{call getAccountId(?, ?)}");
                if (csTemp instanceof SQLServerCallableStatement) {
                    ((SQLServerCallableStatement) csTemp).setString(1, DmShared.iNull(vType).toString());
                    ((SQLServerCallableStatement) csTemp).setStructured(2, "dbo.fieldUDT", (SQLServerDataTable) DmServer.getFieldUDT(fieldUDTHashMap));
                }
                rsTemp = csTemp.executeQuery();
                if (rsTemp.next()) {
                    manualCode = DmShared.xNull(rsTemp.getString(1)).toString();
                }
                rsTemp.close();
                csTemp.close();
            }
            // ******************************************* END ***********************************************

            // ************************************** GET ACCOUNT GROUP ****************************************
            if (!DmShared.xNull(mrTemp.getAcGroup()).trim().equalsIgnoreCase("")) {

                groupCode = DmServer.getCodeViaManualCode(mrTemp.getAcGroup(), "GroupName", "AcGroup", "GroupCode", cnTemp);
                if (DmShared.iNull(groupCode).longValue() == 0) {
                    status = DmImportStatus.WARNING.getId();
                    throw new DmException(0, mrTemp.getAcGroup() + " a/c Group not exists.");
                }
            }
            // ******************************************* END *************************************************

            // ************************************** GET NATURE ***********************************************
            if (!DmShared.xNull(mrTemp.getNature()).trim().equalsIgnoreCase("")) {

                natureCode = DmServer.getCodeViaManualCode(mrTemp.getNature(), "name", "subGroupNature", "code", cnTemp);
                if (DmShared.iNull(natureCode).longValue() == 0) {
                    status = DmImportStatus.WARNING.getId();
                    throw new DmException(0, mrTemp.getNature() + " nature not exists.");
                }
            }
            // ******************************************* END *************************************************

            // ************************************** GET COST CENTER CODE *************************************
            if (!DmShared.xNull(mrTemp.getCostCenter()).trim().equalsIgnoreCase("")) {
                costCenterCode = DmServer.getCodeViaManualCode(mrTemp.getCostCenter(), "name", "CostCenterMast", "code", cnTemp);
                if (DmShared.iNull(costCenterCode).longValue() == 0) {
                    status = DmImportStatus.WARNING.getId();
                    handelFileLog.writeLine(lineNoDisplay + " " + mrTemp.getCostCenter() + " cost center not exists.");
                }
            }
            // ******************************************* END *************************************************

            // ************************************** GET CITY **********************************************
            if (!DmShared.xNull(mrTemp.getCity()).trim().equalsIgnoreCase("")) {
                city = DmServer.getCodeViaManualCode(mrTemp.getCity(), "cityName", "city", "cityCode", cnTemp);
                if (DmShared.iNull(city).longValue() == 0) {
                    status = DmImportStatus.WARNING.getId();
                    handelFileLog.writeLine(lineNoDisplay + " " + mrTemp.getCity() + " city not exists.");
                }
            }
            // ******************************************* END ***********************************************

            // ************************************** GET ZONE ***********************************************
            if (!DmShared.xNull(mrTemp.getZone()).equalsIgnoreCase("")) {

                zone = DmServer.getCodeViaManualCode(mrTemp.getZone(), "name", "zoneMast", "code", cnTemp);
                if (DmShared.iNull(zone).longValue() == 0) {
                    status = DmImportStatus.WARNING.getId();
                    handelFileLog.writeLine(lineNoDisplay + " " + mrTemp.getZone() + " zone not exists.");
                }
            }
            // ******************************************* END ***********************************************

            // ************************************** GET LOCATION *********************************************
            locationCode = DmShared.xNull(mrTemp.getLocation()).trim().equalsIgnoreCase("Out Of State") ? "O"
                    : (DmShared.xNull(mrTemp.getLocation()).trim().equalsIgnoreCase("With In State") ? "W"
                    : DmShared.xNull(mrTemp.getLocation()).trim().equalsIgnoreCase("Out Of Country") ? "C"
                    : DmShared.xNull(mrTemp.getLocation()).trim().equalsIgnoreCase("Not Applicable") ? "N" : "");
            // ******************************************* END **************************************************

            // *********************************** GET PARTY CATEGORY *******************************************
            partyCatCode = DmShared.xNull(mrTemp.getPartyCategory()).trim().equalsIgnoreCase("With In District") ? "D"
                    : (DmShared.xNull(mrTemp.getPartyCategory()).trim().equalsIgnoreCase("Out Of District") ? "O" : "");
            // ******************************************* END **************************************************

            // ************************************** GET TDS CATEGORY CODE ****************************************
            if (!DmShared.xNull(mrTemp.getTdsCategory()).equalsIgnoreCase("")) {
                tdsCatCode = DmServer.getCodeViaManualCode(mrTemp.getTdsCategory(), "name", "TdsCat", "code", cnTemp);
                if (DmShared.iNull(tdsCatCode).longValue() == 0) {
                    status = DmImportStatus.WARNING.getId();
                    handelFileLog.writeLine(lineNoDisplay + " " + mrTemp.getTdsCategory() + " TDS category not exists.");
                }
            }
            // ******************************************* END *****************************************************

            // ************************************** GET TDS REFERENCE CODE ****************************************
            if (!DmShared.xNull(mrTemp.getReferenceTo()).equalsIgnoreCase("")) {
                referenceCode = DmServer.getCodeViaManualCode(mrTemp.getReferenceTo(), "name", "SubGroup", "SubCode", cnTemp);
                if (DmShared.iNull(referenceCode).longValue() == 0) {
                    status = DmImportStatus.WARNING.getId();
                    handelFileLog.writeLine(lineNoDisplay + " " + mrTemp.getReferenceTo() + " Reference To not exists.");
                }
            }
            // ******************************************* END *****************************************************

            // ************************************** CHECK FOR TIN NO **********************************************
            if (!DmShared.xNull(mrTemp.getTIN()).equalsIgnoreCase("") && DmShared.xNull(mrTemp.getTIN()).length() < 11) {
                status = DmImportStatus.WARNING.getId();
                throw new UnsupportedOperationException("TIN Should Not Be Greater Than 11(Eleven) Characters.");
            }
            // ******************************************* END ******************************************************

            // ************************************** CHECK FOR PAN NO **********************************************
            if (!DmShared.xNull(mrTemp.getPAN()).equalsIgnoreCase("") && DmShared.xNull(mrTemp.getPAN()).length() != 10) {
                status = DmImportStatus.WARNING.getId();
                throw new UnsupportedOperationException("PAN Should Be Of 10(Ten) Characters.");
            }
            // ******************************************* END **************************************************

            // ************************************** GET DUPLICATE *********************************************
            duplicateCode = DmShared.xNull(mrTemp.getIsDuplicate()).trim().equalsIgnoreCase("Yes") ? "1"
                    : (DmShared.xNull(mrTemp.getIsDuplicate()).trim().equalsIgnoreCase("No") ? "0" : "");
            // ******************************************* END **************************************************

            // ************************************** GET LEDGER GROUP *****************************************
            if (!DmShared.xNull(mrTemp.getLedgerGroup()).equalsIgnoreCase("")) {
                ledgerCode = DmServer.getCodeViaManualCode(mrTemp.getLedgerGroup(), "name", "LedgerGroup", "code", cnTemp);
                if (DmShared.iNull(ledgerCode).longValue() == 0) {
                    status = DmImportStatus.WARNING.getId();
                    handelFileLog.writeLine(lineNoDisplay + " " + mrTemp.getLedgerGroup() + " Ledger Group not exists.");
                }
            }
            // ******************************************* END **************************************************

            // ************************************** GET PARTY TYPE ********************************************
            partyType = DmShared.xNull(mrTemp.getPartyType()).trim().equalsIgnoreCase("Registered") ? "R"
                    : (DmShared.xNull(mrTemp.getIsDuplicate()).trim().equalsIgnoreCase("Unregistered") ? "U" : "");
            // ******************************************* END **************************************************

            // ************************************** INSERT INTO SUBGROUP ********************************************
            sqlQuery = "Insert Into SubGroup(SubCode,Initial,Name,DisplayName,ManualCode,GroupCode,nature,costCenter,Add1,Add2,filterNature,"
                    + " CityCode,Zone,PIN,Phone,Mobile,FAX,EMail,Location,PartyCat,TDS_Catg,distributor,CreditLimit,DueDays,PAN,TINNo,isDuplicateTin,"
                    + " IECCode,LedgerGroup,PartyType,Remark,U_EntDt,Site_Code, preparedBy) "
                    + " Values (:subCode, :initial,  :name,:displayName, :manualCode, :groupCode, :nature, :costCenter ,:add1, :add2,:filterNature, "
                    + " :cityCode,:zone, :pin, :phone, :mobile, :fax,  :email, :location, :partyCat, :tdsCatCode ,:distributor,:creditLimit, :dueDays,"
                    + " :pan,:tinNo,:isDuplicateTin, :iecCode, :ledgerGroup,:partyType,:remark, getdate(), :siteCode, :userName) ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("subCode", mrTemp.getCode());
            psTemp.setString("initial", mrTemp.getAcNamePrefix());
            psTemp.setString("name", mrTemp.getAccountName());
            psTemp.setString("displayName", mrTemp.getDisplayName());
            psTemp.setString("manualCode", manualCode);
            psTemp.setString("groupCode", DmShared.foriegnKey(groupCode));
            psTemp.setString("nature", DmShared.foriegnKey(natureCode));
            psTemp.setString("costCenter", DmShared.foriegnKey(costCenterCode));
            psTemp.setString("add1", mrTemp.getAddress1());
            psTemp.setString("add2", mrTemp.getAddress2());
            psTemp.setString("filterNature", DmShared.foriegnKey(filterNature));
            psTemp.setString("cityCode", DmShared.foriegnKey(city));
            psTemp.setString("zone", DmShared.foriegnKey(zone));
            psTemp.setString("pin", mrTemp.getPinCode());
            psTemp.setString("phone", mrTemp.getPhoneNo());
            psTemp.setString("mobile", mrTemp.getMobileNo());
            psTemp.setString("fax", mrTemp.getFaxNo());
            psTemp.setString("email", mrTemp.getEmail());
            psTemp.setString("location", locationCode);
            psTemp.setString("partyCat", DmShared.foriegnKey(partyCatCode));
            psTemp.setString("tdsCatCode", DmShared.foriegnKey(tdsCatCode));
            psTemp.setString("distributor", DmShared.foriegnKey(referenceCode));
            psTemp.setString("creditLimit", DmServer.vNull(mrTemp.getCreditLimit(), "0.00"));
            psTemp.setString("dueDays", DmServer.vNull(mrTemp.getCreditPeriod(), "0"));
            psTemp.setString("pan", mrTemp.getPAN());
            psTemp.setString("tinNo", mrTemp.getTIN());
            psTemp.setString("isDuplicateTin", duplicateCode);
            psTemp.setString("iecCode", mrTemp.getIECNo());
            psTemp.setString("ledgerGroup", DmShared.foriegnKey(ledgerCode));
            psTemp.setString("partyType", partyType);
            psTemp.setString("remark", mrTemp.getRemark());
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(rss));
            psTemp.setString("userName", DmServer.getSsnUserName(rss));
            psTemp.executeUpdate();
            psTemp.close();
            // ******************************************* END **************************************************

            // ************************************** INSERT INTO SUBGROUPSITELIST ******************************
            sqlQuery = "INSERT INTO subgroupsitelist (subCode, site_Code) "
                    + "VALUES (:subCode, :siteCode) ;";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getCode()));
            psTemp.setString("siteCode", DmShared.foriegnKey(DmServer.getSsnSiteCode(rss)));
            psTemp.executeUpdate();
            psTemp.close();
            // ******************************************* END **************************************************

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
