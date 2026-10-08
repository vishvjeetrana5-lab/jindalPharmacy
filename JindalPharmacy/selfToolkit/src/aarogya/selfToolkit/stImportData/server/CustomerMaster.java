package aarogya.selfToolkit.stImportData.server;

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
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmToolkit.dmImportData.server.DmImportData_Interface;
import dataman.dmToolkit.dmImportData.shared.CSVData;
import dataman.dmToolkit.dmImportData.shared.DmImportStatus;
import dataman.dmToolkit.dmImportData.shared.ImportData_Mdl;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;

public class CustomerMaster implements DmImportData_Interface {

    private final String vCat = new LibraryVoucherCategory().CUSTOMER_MASTER;

    class Model {

        private String code, acInitialCode, acInitialName, accountName, displayName, acGroupCode, acGroupName, natureCode, natureName,
                costCenterCode, costCenterName, partyCategoryCode, partyCategoryName, address1, address2, cityCode, cityName, pinCode,
                partyTypeCode, partyTypeName, gstin, gstinDate, aadhaar, pan, zoneCode, zoneName, phoneNo, mobileNo, faxNo,
                email, expTerms, invoiceDis, creditLimit, creditPeriod, TIN, isDuplicate, IECNo, ledgerGroupCode, ledgerGroupName, drugLicense, registrationNo,
                referenceToCode, referenceToName, isNonMoving, isMonopoly, minimum, maximum, reOrderLevel, tdsCategoryCode, tdsCategoryName, remark;

        public Model() {
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getAcInitialCode() {
            return acInitialCode;
        }

        public void setAcInitialCode(String acInitialCode) {
            this.acInitialCode = acInitialCode;
        }

        public String getAcInitialName() {
            return acInitialName;
        }

        public void setAcInitialName(String acInitialName) {
            this.acInitialName = acInitialName;
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

        public String getAcGroupCode() {
            return acGroupCode;
        }

        public void setAcGroupCode(String acGroupCode) {
            this.acGroupCode = acGroupCode;
        }

        public String getAcGroupName() {
            return acGroupName;
        }

        public void setAcGroupName(String acGroupName) {
            this.acGroupName = acGroupName;
        }

        public String getNatureCode() {
            return natureCode;
        }

        public void setNatureCode(String natureCode) {
            this.natureCode = natureCode;
        }

        public String getNatureName() {
            return natureName;
        }

        public void setNatureName(String natureName) {
            this.natureName = natureName;
        }

        public String getCostCenterCode() {
            return costCenterCode;
        }

        public void setCostCenterCode(String costCenterCode) {
            this.costCenterCode = costCenterCode;
        }

        public String getCostCenterName() {
            return costCenterName;
        }

        public void setCostCenterName(String costCenterName) {
            this.costCenterName = costCenterName;
        }

        public String getPartyCategoryCode() {
            return partyCategoryCode;
        }

        public void setPartyCategoryCode(String partyCategoryCode) {
            this.partyCategoryCode = partyCategoryCode;
        }

        public String getPartyCategoryName() {
            return partyCategoryName;
        }

        public void setPartyCategoryName(String partyCategoryName) {
            this.partyCategoryName = partyCategoryName;
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

        public String getCityCode() {
            return cityCode;
        }

        public void setCityCode(String cityCode) {
            this.cityCode = cityCode;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getPinCode() {
            return pinCode;
        }

        public void setPinCode(String pinCode) {
            this.pinCode = pinCode;
        }

        public String getPartyTypeCode() {
            return partyTypeCode;
        }

        public void setPartyTypeCode(String partyTypeCode) {
            this.partyTypeCode = partyTypeCode;
        }

        public String getPartyTypeName() {
            return partyTypeName;
        }

        public void setPartyTypeName(String partyTypeName) {
            this.partyTypeName = partyTypeName;
        }

        public String getGstin() {
            return gstin;
        }

        public void setGstin(String gstin) {
            this.gstin = gstin;
        }

        public String getGstinDate() {
            return gstinDate;
        }

        public void setGstinDate(String gstinDate) {
            this.gstinDate = gstinDate;
        }

        public String getAadhaar() {
            return aadhaar;
        }

        public void setAadhaar(String aadhaar) {
            this.aadhaar = aadhaar;
        }

        public String getPan() {
            return pan;
        }

        public void setPan(String pan) {
            this.pan = pan;
        }

        public String getZoneCode() {
            return zoneCode;
        }

        public void setZoneCode(String zoneCode) {
            this.zoneCode = zoneCode;
        }

        public String getZoneName() {
            return zoneName;
        }

        public void setZoneName(String zoneName) {
            this.zoneName = zoneName;
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

        public String getExpTerms() {
            return expTerms;
        }

        public void setExpTerms(String expTerms) {
            this.expTerms = expTerms;
        }

        public String getInvoiceDis() {
            return invoiceDis;
        }

        public void setInvoiceDis(String invoiceDis) {
            this.invoiceDis = invoiceDis;
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

        public String getIECNo() {
            return IECNo;
        }

        public void setIECNo(String IECNo) {
            this.IECNo = IECNo;
        }

        public String getLedgerGroupCode() {
            return ledgerGroupCode;
        }

        public void setLedgerGroupCode(String ledgerGroupCode) {
            this.ledgerGroupCode = ledgerGroupCode;
        }

        public String getLedgerGroupName() {
            return ledgerGroupName;
        }

        public void setLedgerGroupName(String ledgerGroupName) {
            this.ledgerGroupName = ledgerGroupName;
        }

        public String getDrugLicense() {
            return drugLicense;
        }

        public void setDrugLicense(String drugLicense) {
            this.drugLicense = drugLicense;
        }

        public String getRegistrationNo() {
            return registrationNo;
        }

        public void setRegistrationNo(String registrationNo) {
            this.registrationNo = registrationNo;
        }

        public String getReferenceToCode() {
            return referenceToCode;
        }

        public void setReferenceToCode(String referenceToCode) {
            this.referenceToCode = referenceToCode;
        }

        public String getReferenceToName() {
            return referenceToName;
        }

        public void setReferenceToName(String referenceToName) {
            this.referenceToName = referenceToName;
        }

        public String getIsNonMoving() {
            return isNonMoving;
        }

        public void setIsNonMoving(String isNonMoving) {
            this.isNonMoving = isNonMoving;
        }

        public String getIsMonopoly() {
            return isMonopoly;
        }

        public void setIsMonopoly(String isMonopoly) {
            this.isMonopoly = isMonopoly;
        }

        public String getMinimum() {
            return minimum;
        }

        public void setMinimum(String minimum) {
            this.minimum = minimum;
        }

        public String getMaximum() {
            return maximum;
        }

        public void setMaximum(String maximum) {
            this.maximum = maximum;
        }

        public String getReOrderLevel() {
            return reOrderLevel;
        }

        public void setReOrderLevel(String reOrderLevel) {
            this.reOrderLevel = reOrderLevel;
        }

        public String getTdsCategoryCode() {
            return tdsCategoryCode;
        }

        public void setTdsCategoryCode(String tdsCategoryCode) {
            this.tdsCategoryCode = tdsCategoryCode;
        }

        public String getTdsCategoryName() {
            return tdsCategoryName;
        }

        public void setTdsCategoryName(String tdsCategoryName) {
            this.tdsCategoryName = tdsCategoryName;
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
    private String[] currentDate;

    public CustomerMaster() {
    }

    @Override
    public ImportData_Mdl getImportDataModel() {
        final ImportData_Mdl fileDetail = new ImportData_Mdl();
        CSVData node;
        List<CSVData> lstCSV = new ArrayList<CSVData>();

        node = new CSVData();
        node.setHeader("initial");
        node.setSample("Mr");
        node.setInstruction("{alphanumeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("name");
        node.setSample("Mohit");
        node.setInstruction("{unique;mandatory;alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("displayName");
        node.setSample("Harry");
        node.setInstruction("{mandatory;alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("acGroup");
        node.setSample("Bank OD A/c");
        node.setInstruction("{mandatory;alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("nature");
        node.setSample("Cash");
        node.setInstruction("{mandatory;alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("costCenter");
        node.setSample("CostCenter1");
        node.setInstruction("{alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("partyCategory");
        node.setSample("Retailer");
        node.setInstruction("(C & F/Distributor/Retailer);11c");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("address1");
        node.setSample("CP");
        node.setInstruction("{alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("address2");
        node.setSample("Sipri");
        node.setInstruction("{alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("city");
        node.setSample("Kanpur");
        node.setInstruction("{mandatory;alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("pin");
        node.setSample("284002");
        node.setInstruction("{alphanumeric;6c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("partyType(GST)");
        node.setSample("Composite");
        node.setInstruction("{mandatory;(Registered/Unregistered/Composite);15c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("GSTIN");
        node.setSample("09AAKPC7554P1Z7");
        node.setInstruction("{alphanumeric;15c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("GSTINDate");
        node.setSample("22-June-2018");
        node.setInstruction("{alphanumeric;11c;dd-MMM-yyyy}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("aadhaar");
        node.setSample("983567432145");
        node.setInstruction("{numeric;12c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("pan");
        node.setSample("AAKPC7554P");
        node.setInstruction("{alphanumeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("zone");
        node.setSample("bhiwani");
        node.setInstruction("{alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("phoneNo");
        node.setSample("6884571454");
        node.setInstruction("{alphanumeric;35c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("mobileNo");
        node.setSample("6887125648");
        node.setInstruction("{alphanumeric;35c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("faxNo");
        node.setSample("115544");
        node.setInstruction("{alphanumeric;35c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("email");
        node.setSample("emailm13@gmail.com");
        node.setInstruction("{alphanumeric;250c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("expTerms");
        node.setSample("123");
        node.setInstruction("{numeric;8c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("invoiceDis");
        node.setSample("456");
        node.setInstruction("{numeric;8c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("creditLimit");
        node.setSample("5000");
        node.setInstruction("{numeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("creditPeriod");
        node.setSample("2");
        node.setInstruction("{numeric;5c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("TIN");
        node.setSample("17234567890");
        node.setInstruction("{alphanumeric;20c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("duplicate");
        node.setSample("Yes");
        node.setInstruction("{alphanumeric(Yes/No);3c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("iecNo");
        node.setSample("6253673");
        node.setInstruction("{alphanumeric;35c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("ledgerGroup");
        node.setSample("group1");
        node.setInstruction("{alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("drugLicense");
        node.setSample("DL1");
        node.setInstruction("{alphanumeric;20c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("registrationNo");
        node.setSample("098123abcd");
        node.setInstruction("{alphanumeric;35c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("referencedTo");
        node.setSample("qwerty0628");
        node.setInstruction("{alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("nonMoving");
        node.setSample("Yes");
        node.setInstruction("{Yes/No};3c");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("monopoly");
        node.setSample("No");
        node.setInstruction("{Yes/No};3c");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("minimum");
        node.setSample("2804");
        node.setInstruction("{numeric;8c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("maximum");
        node.setSample("9628");
        node.setInstruction("{numeric;8c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("reOrderLevel");
        node.setSample("09876");
        node.setInstruction("{numeric;8c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("TDSCategory");
        node.setSample("category1");
        node.setInstruction("{alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("remark");
        node.setSample("testing");
        node.setInstruction("{alphanumeric;250c}");
        lstCSV.add(node);

        fileDetail.getHmCSV().addAll(lstCSV);
        fileDetail.setImportTo("Customer Master");
        fileDetail.setServerClassName(CustomerMaster.class.getName());

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
        int rtnStatus = 0, status = 0;
        DmImportStatus importStatus = null;
        try {

            this.rss = rss;
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));
            if (DmShared.xNull(importData.getLogFilePath()).trim().equalsIgnoreCase("")) {
                status = importStatus.ERROR.getId();
                throw new Exception("Log file path can not be blank.");
            }
            handelFileLog = new DmHandelFile(importData.getLogFilePath(), true);
            currentDate = DmServer.getServerDateTime();
            filterNature = DmServer.getCodeViaManualCode("CT", "manualCode", "subgroupNature", "Code", cnTemp);
            if (DmShared.iNull(filterNature).longValue() == 0) {
                status = importStatus.ERROR.getId();
                throw new DmException(0, "Customer 'CT' nature not exists.");
            }
            cnTemp.close();
            if (DmShared.xNull(importData.getDataFilePath()).trim().equalsIgnoreCase("")) {
                status = importStatus.ERROR.getId();
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
                        if (headerName.trim().equalsIgnoreCase("initial")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = importStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setAcInitialName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("name")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = importStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setAccountName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("displayName")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = importStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setDisplayName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("acGroup")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = importStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setAcGroupName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("nature")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = importStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setNatureName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("costCenter")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = importStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setCostCenterName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("partyCategory")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 11, lineNoDisplay, handelFileLog)) {
                                status = importStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPartyCategoryName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("address1")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = importStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setAddress1(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("address2")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = importStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setAddress2(fieldValue);
                            }

                        } else if (headerName.trim().equalsIgnoreCase("city")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = importStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setCityName(fieldValue);
                            }

                        } else if (headerName.trim().equalsIgnoreCase("pin")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 6, lineNoDisplay, handelFileLog)) {
                                status = importStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPinCode(fieldValue);
                            }

                        } else if (headerName.trim().equalsIgnoreCase("partyType(GST)")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 15, lineNoDisplay, handelFileLog)) {
                                status = importStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPartyTypeName(fieldValue);
                            }

                        } else if (headerName.trim().equalsIgnoreCase("GSTIN")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 15, lineNoDisplay, handelFileLog)) {
                                status = importStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setGstin(fieldValue);
                            }

                        } else if (headerName.trim().equalsIgnoreCase("GSTINDate")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, true, false, 11, lineNoDisplay, handelFileLog)) {
                                status = importStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setGstinDate(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("aadhaar")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 12, lineNoDisplay, handelFileLog)) {
                                status = importStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setAadhaar(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("pan")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = importStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPan(fieldValue);
                            }

                        } else if (headerName.trim().equalsIgnoreCase("zone")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = importStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setZoneName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("phoneNo")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 35, lineNoDisplay, handelFileLog)) {
                                status = importStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPhoneNo(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("mobileNo")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 35, lineNoDisplay, handelFileLog)) {
                                status = importStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setMobileNo(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("faxNo")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 35, lineNoDisplay, handelFileLog)) {
                                status = importStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setFaxNo(fieldValue);
                            }

                        } else if (headerName.trim().equalsIgnoreCase("email")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 250, lineNoDisplay, handelFileLog)) {
                                status = importStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setEmail(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("expTerms")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 8, lineNoDisplay, handelFileLog)) {
                                status = importStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setExpTerms(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("invoiceDis")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = importStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setInvoiceDis(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("creditLimit")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = importStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setCreditLimit(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("creditPeriod")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 5, lineNoDisplay, handelFileLog)) {
                                status = importStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setCreditPeriod(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("TIN")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 20, lineNoDisplay, handelFileLog)) {
                                status = importStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setTIN(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("duplicate")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 3, lineNoDisplay, handelFileLog)) {
                                status = importStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setIsDuplicate(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("iecNo")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 35, lineNoDisplay, handelFileLog)) {
                                status = importStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setIECNo(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("ledgerGroup")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = importStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setLedgerGroupName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("drugLicense")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 20, lineNoDisplay, handelFileLog)) {
                                status = importStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setDrugLicense(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("registrationNo")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 35, lineNoDisplay, handelFileLog)) {
                                status = importStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setRegistrationNo(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("referencedTo")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = importStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setReferenceToName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("nonMoving")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 3, lineNoDisplay, handelFileLog)) {
                                status = importStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setIsNonMoving(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("monopoly")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 3, lineNoDisplay, handelFileLog)) {
                                status = importStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setIsMonopoly(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("minimum")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 8, lineNoDisplay, handelFileLog)) {
                                status = importStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setMinimum(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("maximum")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 8, lineNoDisplay, handelFileLog)) {
                                status = importStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setMaximum(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("reOrderLevel")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 8, lineNoDisplay, handelFileLog)) {
                                status = importStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setReOrderLevel(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("TDSCategory")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = importStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setTdsCategoryName(fieldValue);
                            }

                        } else if (headerName.trim().equalsIgnoreCase("remark")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 250, lineNoDisplay, handelFileLog)) {
                                status = importStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setRemark(fieldValue);
                            }
                        }
                    }

                    if (isFound) {
                        status = saveCustomer(mrTemp);
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

    private int saveCustomer(Model mrTemp) {
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        CallableStatement csTemp = null;
        String sqlQuery, vType = "", manualCode = "", duplicateCode = "", nonmovingCode = "", monopolyCode = "", stateNumericCode = "";
        boolean isGstApplicable = false;
        int rtnStatus = 0, status = 0;
        try {
            isGstApplicable = !DmShared.xNull(DmServer.getSsnSiteGSTIN(rss)).trim().equalsIgnoreCase("");
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
                    status = DmImportStatus.ERROR.getId();
                    throw new UnsupportedOperationException("Duplicate TIN Not Allowed !!!");
                }
                rsTemp.close();
                psTemp.close();
            }
            // ******************************************* END ****************************************************

            // ************************************** GET VOUCHER TYPE *********************************************
            sqlQuery = "SELECT vt.v_Type  FROM voucher_type vt WHERE  vt.category = '" + vCat + " ' ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                vType = DmShared.xNull(rsTemp.getString("v_Type"));
            }
            rsTemp.close();
            psTemp.close();
            // ******************************************* END ****************************************************

            // ************************************** GET MANUAL CODE *********************************************
            HashMap<String, FieldUDT_Mdl> fieldUDTHashMap = new HashMap<String, FieldUDT_Mdl>();
            fieldUDTHashMap.put("customerName", new FieldUDT_Mdl("customerName", DmShared.xNull(mrTemp.getAccountName()), DmShared.xNull(mrTemp.getAccountName()).trim()));
            fieldUDTHashMap.put("acGroup", new FieldUDT_Mdl("acGroup", DmShared.xNull(mrTemp.getAcGroupName()).toString(), DmShared.xNull(mrTemp.getAcGroupName()).trim()));
            fieldUDTHashMap.put("nature", new FieldUDT_Mdl("nature", DmShared.xNull(mrTemp.getNatureName()).toString(), DmShared.xNull(mrTemp.getNatureName()).trim()));
            fieldUDTHashMap.put("partyCategory", new FieldUDT_Mdl("partyCategory", DmShared.xNull(mrTemp.getPartyCategoryName()).toString(), DmShared.xNull(mrTemp.getPartyCategoryName()).trim()));
            csTemp = ((SQLServerConnection) ((ConnectionHandle) cnTemp).getInternalConnection()).prepareCall("{call getCustomerId(?, ?)}");
            if (csTemp instanceof SQLServerCallableStatement) {
                ((SQLServerCallableStatement) csTemp).setString(1, DmShared.iNull(vType).toString());
                ((SQLServerCallableStatement) csTemp).setStructured(2, "dbo.fieldUDT", (SQLServerDataTable) DmServer.getFieldUDT(fieldUDTHashMap));
            }
            rsTemp = csTemp.executeQuery();
            if (rsTemp.next()) {
                manualCode = DmShared.xNull(rsTemp.getString(1));
            }
            rsTemp.close();
            csTemp.close();

            sqlQuery = "Select Count(*) As Cnt From SubGroup where ManualCode = :manualCode  and SubCode<> :subCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("manualCode", manualCode);
            psTemp.setString("subCode", mrTemp.getCode());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                status = DmImportStatus.ERROR.getId();
                throw new UnsupportedOperationException("Duplicate Manual Code Not Allowed !!!");
            }
            rsTemp.close();
            psTemp.close();

            // ******************************************* END ***********************************************
            if (!DmShared.xNull(mrTemp.getAcInitialName()).equalsIgnoreCase("")) {
                mrTemp.setAcInitialCode(DmServer.getCodeViaManualCode(mrTemp.getAcInitialName(), "name", "initialmast", "name", cnTemp));
                if (DmShared.xNull(mrTemp.getAcInitialCode()).trim().equalsIgnoreCase("")) {
                    status = DmImportStatus.WARNING.getId();
                    handelFileLog.writeLine(lineNoDisplay + mrTemp.getAcInitialName() + " not inserted.A/c Initial not exists.");
                }
            }

            // ************************************** GET ACCOUNT GROUP ****************************************
            mrTemp.setAcGroupCode(DmServer.getCodeViaManualCode(mrTemp.getAcGroupName(), "GroupName", "AcGroup", "GroupCode", cnTemp));
            if (DmShared.iNull(mrTemp.getAcGroupCode()).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, mrTemp.getAcGroupName() + " a/c Group not exists.");
            }
            // ******************************************* END *************************************************

            // ************************************** GET NATURE ***********************************************
            mrTemp.setNatureCode(DmServer.getCodeViaManualCode(mrTemp.getNatureName(), "name", "subGroupNature", "code", cnTemp));
            if (DmShared.iNull(mrTemp.getNatureCode()).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, mrTemp.getNatureName() + " nature not exists.");
            }
            // ******************************************* END *************************************************
            // ************************************** GET COST CENTER CODE *************************************
            if (!DmShared.xNull(mrTemp.getCostCenterName()).equalsIgnoreCase("")) {
                mrTemp.setCostCenterCode(DmServer.getCodeViaManualCode(mrTemp.getCostCenterName(), "name", "CostCenterMast", "code", cnTemp));
                if (DmShared.iNull(mrTemp.getCostCenterCode()).longValue() == 0) {
                    status = DmImportStatus.WARNING.getId();
                    handelFileLog.writeLine(lineNoDisplay + " " + mrTemp.getCostCenterName() + " cost center not exists.");
                }
            }
            // ******************************************* END *************************************************
            // ************************************** GET PARTY CATEGORY CODE **********************************
            if (DmShared.xNull(mrTemp.getPartyCategoryName().trim()).equalsIgnoreCase("C & F")) {
                mrTemp.setPartyCategoryCode("C");
            } else if (DmShared.xNull(mrTemp.getPartyCategoryName().trim()).equalsIgnoreCase("Distributor")) {
                mrTemp.setPartyCategoryCode("D");
            } else if (DmShared.xNull(mrTemp.getPartyCategoryName().trim()).equalsIgnoreCase("Retailer")) {
                mrTemp.setPartyCategoryCode("R");
            }
            if (!DmShared.xNull(mrTemp.getPartyCategoryName()).trim().equalsIgnoreCase("")) {
                if (DmShared.xNull(mrTemp.getPartyCategoryCode()).equalsIgnoreCase("")) {
                    status = DmImportStatus.WARNING.getId();
                    handelFileLog.writeLine(lineNoDisplay + " " + mrTemp.getPartyCategoryName() + " Party Category not exists.");
                }
            }
            // ******************************************* END **************************************************

            // ************************************** GET CITY **********************************************
            sqlQuery = "SELECT c.cityCode ,sm.numericCode"
                    + "  FROM city c"
                    + " left join stateMast sm on c.stateCode = sm.code"
                    + " WHERE  c.cityName = :cityName ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("cityName", mrTemp.getCityName());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                mrTemp.setCityCode(DmShared.xNull(rsTemp.getString("cityCode")));
                stateNumericCode = DmShared.xNull(rsTemp.getString("numericCode"));
            }
            rsTemp.close();
            psTemp.close();

            if (DmShared.iNull(mrTemp.getCityCode()).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, mrTemp.getCityName() + " city not exists.");
            }
            // ******************************************* END **************************************************
            // ************************************** CHECK FOR PAN NO **********************************************
            if (!DmShared.xNull(mrTemp.getPan()).trim().equalsIgnoreCase("")) {
                if (DmShared.xNull(mrTemp.getPan()).length() != 10) {
                    status = DmImportStatus.WARNING.getId();
                    throw new DmException(0, mrTemp.getPan() + "PAN should be of 10(Ten) characters.");
                } else {
                    DmShared.isValidPanNo(mrTemp.getPan(), 0);
                }
            }

            // ******************************** query FOR state numeric code *****************************************
            // ******************************************* END ******************************************************
            // ************************************** GET PARTY TYPE ********************************************
            if (!DmShared.xNull(mrTemp.getPartyTypeName()).equalsIgnoreCase("")) {
                if (DmShared.xNull(mrTemp.getPartyTypeName()).equalsIgnoreCase(DmEnum.DmPartyType.Registered.getValue())) {
                    mrTemp.setPartyTypeCode(DmEnum.DmPartyType.Registered.getId());
                } else if (DmShared.xNull(mrTemp.getPartyTypeName()).equalsIgnoreCase(DmEnum.DmPartyType.Unregistered.getValue())) {
                    mrTemp.setPartyTypeCode(DmEnum.DmPartyType.Unregistered.getId());
                } else if (DmShared.xNull(mrTemp.getPartyTypeName()).equalsIgnoreCase(DmEnum.DmPartyType.Composite.getValue())) {
                    mrTemp.setPartyTypeCode(DmEnum.DmPartyType.Composite.getId());
                }
                if (DmShared.xNull(mrTemp.getPartyTypeCode()).equalsIgnoreCase("")) {
                    status = DmImportStatus.WARNING.getId();
                    throw new DmException(0, mrTemp.getPartyTypeName() + " Party Type not exists.");
                }
            }
            // ******************************************* END **************************************************

            if (DmShared.xNull(mrTemp.getPartyTypeCode()).equalsIgnoreCase((DmEnum.DmPartyType.Registered.getId()))
                    || DmShared.xNull(mrTemp.getPartyTypeCode()).equalsIgnoreCase((DmEnum.DmPartyType.Composite.getId()))) {
                DmShared.isValidText(mrTemp.getGstin(), "GSTIN No.", 0);
                DmShared.isValidText(mrTemp.getGstinDate(), "GSTIN Date", 0);
            }
            if (!DmShared.xNull(mrTemp.getGstin()).equalsIgnoreCase("")
                    || !DmShared.dNull(mrTemp.getGstinDate()).equalsIgnoreCase("")) {
                if (DmShared.xNull(mrTemp.getPartyTypeName()).trim().equalsIgnoreCase("")) {
                    status = DmImportStatus.WARNING.getId();
                    throw new DmException(600, "For defining GST detail, you should have to fill Party Type first.");
                } else if (DmShared.xNull(mrTemp.getPan()).trim().equals("")) {
                    status = DmImportStatus.WARNING.getId();
                    throw new DmException(900, "Please fill the Pan No.");
                } else {
                    DmShared.isValidGSTIN(stateNumericCode, mrTemp.getPan(), mrTemp.getGstin(), 700);
                }
            }
            if (DmShared.xNull(mrTemp.getPartyTypeCode()).equalsIgnoreCase((DmEnum.DmPartyType.Unregistered.getId()))) {
                if (!DmShared.xNull(mrTemp.getGstin()).equalsIgnoreCase("") || !DmShared.xNull(mrTemp.getGstinDate()).equalsIgnoreCase("")) {
                    status = DmImportStatus.WARNING.getId();
                    throw new DmException(0, "In case of unregistered party type, you should not fill GSTIN No. and GSTIN Date.");
                }
                if (isGstApplicable == true) {
                    if (DmShared.xNull(mrTemp.getAadhaar()).equalsIgnoreCase("")) {
                        if (DmShared.xNull(mrTemp.getPan()).equalsIgnoreCase("")) {
                            status = DmImportStatus.WARNING.getId();
                            throw new DmException(0, "In case of unregistered party type AADHAAR No. or PAN No. is mandatory.");
                        }
                    }
                }
            }

            if (!DmShared.xNull(mrTemp.getGstinDate()).equalsIgnoreCase("")) {
                if (DmShared.convertDate(mrTemp.getGstinDate()).getTime() > (DmShared.convertDate(currentDate[0])).getTime()) {
                    status = DmImportStatus.WARNING.getId();
                    throw new DmException(0, "GSTIN Date should not be greater than current date.");
                }
            }

            // ******************************************* END **************************************************
            // ************************************** GET ZONE ***********************************************
            if (!DmShared.xNull(mrTemp.getZoneName()).equalsIgnoreCase("")) {
                mrTemp.setZoneCode(DmServer.getCodeViaManualCode(mrTemp.getZoneName(), "name", "zoneMast", "code", cnTemp));
                if (DmShared.iNull(mrTemp.getZoneCode()).longValue() == 0) {
                    status = DmImportStatus.WARNING.getId();
                    handelFileLog.writeLine(lineNoDisplay + " " + mrTemp.getZoneName() + " zone not exists.");
                }
            }
            // ******************************************* END ***********************************************
            // ************************************** GET DUPLICATE *********************************************
            duplicateCode = DmShared.xNull(mrTemp.getIsDuplicate()).trim().equalsIgnoreCase("Yes") ? "1"
                    : (DmShared.xNull(mrTemp.getIsDuplicate()).trim().equalsIgnoreCase("No") ? "0" : "");
            // ******************************************* END **************************************************

            // ************************************** GET LEDGER GROUP *****************************************
            if (!DmShared.xNull(mrTemp.getLedgerGroupName()).equalsIgnoreCase("")) {
                mrTemp.setLedgerGroupCode(DmServer.getCodeViaManualCode(mrTemp.getLedgerGroupName(), "name", "LedgerGroup", "code", cnTemp));
                if (DmShared.iNull(mrTemp.getLedgerGroupCode()).longValue() == 0) {
                    status = DmImportStatus.WARNING.getId();
                    handelFileLog.writeLine(lineNoDisplay + " " + mrTemp.getLedgerGroupName() + " Ledger Group not exists.");
                }
            }
            // ******************************************* END **************************************************
            // ************************************** GET TDS REFERENCE CODE ****************************************
            if (!DmShared.xNull(mrTemp.getReferenceToName()).equalsIgnoreCase("")) {
                mrTemp.setReferenceToCode(DmServer.getCodeViaManualCode(mrTemp.getReferenceToName(), "name", "SubGroup", "SubCode", cnTemp));
                if (DmShared.iNull(mrTemp.getReferenceToCode()).longValue() == 0) {
                    status = DmImportStatus.WARNING.getId();
                    handelFileLog.writeLine(lineNoDisplay + " " + mrTemp.getReferenceToName() + " Reference To not exists.");
                }
            }
            // ******************************************* END *****************************************************

            // ************************************** GET NON MOVING *********************************************
            nonmovingCode = DmShared.xNull(mrTemp.getIsNonMoving()).trim().equalsIgnoreCase("Yes") ? "1"
                    : (DmShared.xNull(mrTemp.getIsNonMoving()).trim().equalsIgnoreCase("No") ? "0" : "");
            // ******************************************* END **************************************************
            // ************************************** GET MONOPOLY *********************************************
            monopolyCode = DmShared.xNull(mrTemp.getIsMonopoly()).trim().equalsIgnoreCase("Yes") ? "1"
                    : (DmShared.xNull(mrTemp.getIsMonopoly()).trim().equalsIgnoreCase("No") ? "0" : "");
            // ******************************************* END **************************************************
            if (DmShared.iNull(mrTemp.getMaximum()).longValue() < DmShared.iNull(mrTemp.getMinimum()).longValue()) {
                status = DmImportStatus.WARNING.getId();
                throw new DmException(0, "Min Level value must be less than or equal to Max Level value.");
            }

            // ************************************** GET TDS CATEGORY CODE ****************************************
            if (!DmShared.xNull(mrTemp.getTdsCategoryName()).equalsIgnoreCase("")) {
                mrTemp.setTdsCategoryCode(DmServer.getCodeViaManualCode(mrTemp.getTdsCategoryName(), "name", "TdsCat", "code", cnTemp));
                if (DmShared.iNull(mrTemp.getTdsCategoryCode()).longValue() == 0) {
                    status = DmImportStatus.WARNING.getId();
                    handelFileLog.writeLine(lineNoDisplay + " " + mrTemp.getTdsCategoryName() + " TDS category not exists.");
                }
                // ******************************************* END *****************************************************
            }

            // ************************************** CHECK FOR TIN NO **********************************************
            if (!DmShared.xNull(mrTemp.getTIN()).equalsIgnoreCase("") && DmShared.xNull(mrTemp.getTIN()).length() < 11) {
                status = DmImportStatus.WARNING.getId();
                throw new UnsupportedOperationException("TIN Should Not Be Greater Than 11(Eleven) Characters.");
            }
            // ******************************************* END ******************************************************

            // ************************************** INSERT INTO SUBGROUP ********************************************
            sqlQuery = "Insert Into SubGroup(SubCode,Name,GroupCode,ManualCode,Nature,Add1,Add2,CityCode,PIN,drugLicense,registrationNo, "
                    + " Phone,Mobile,FAX,Remark,isDuplicateTIN,U_EntDt,CostCenter,PAN,EMail,TINNo,"
                    + " CreditLimit,DueDays,PartyCat,PartyType,IECCode,TDS_Catg,Zone,LedgerGroup,"
                    + " Distributor,Site_Code,DisplayName,Initial, preparedBy,filterNature,invoiceDiscount,expiryTerms,isNonMoving,"
                    + " minimum,maximum,reorderLevel,isMonopoly,aadhaarNo,gsTinDate,gsTIN) "
                    + " Values (:subCode,  :name, :groupCode, :manualCode, :nature, :add1, :add2, :cityCode, :pin,:drugLicense,:registrationNo, "
                    + " :phone, :mobile, :fax,:remark, :duplicateTIN, :uEntDt, :costCenter, :pan, :email,"
                    + " :tinNo,  :creditLimit, :dueDays, :partyCat, :partyType, :iecCode,"
                    + " :tdsCatg, :zone, :ledgerGroup, :distributor, :siteCode, :displayName, :initial, :userName,:filterNature,"
                    + "  :invoiceDiscount,:expiryTerms,:isNonMoving,:minimum, :maximum, :reorderLevel, :isMonopoly,:aadhaarNo,:gsTinDate,:gsTIN) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("subCode", mrTemp.getCode());
            psTemp.setString("initial", DmShared.xNull(mrTemp.getAcInitialCode()));
            psTemp.setString("name", DmShared.xNull(mrTemp.getAccountName()));
            psTemp.setString("displayName", DmShared.xNull(mrTemp.getDisplayName()));
            psTemp.setString("groupCode", DmShared.foriegnKey(mrTemp.getAcGroupCode()));
            psTemp.setString("manualCode", DmShared.foriegnKey(manualCode));
            psTemp.setString("nature", DmShared.foriegnKey(mrTemp.getNatureCode()));
            psTemp.setString("add1", DmShared.xNull(mrTemp.getAddress1()));
            psTemp.setString("add2", DmShared.xNull(mrTemp.getAddress2()));
            psTemp.setString("filterNature", DmShared.foriegnKey(filterNature));
            psTemp.setString("cityCode", DmShared.foriegnKey(mrTemp.getCityCode()));
            psTemp.setString("pin", DmShared.xNull(mrTemp.getPinCode()));
            psTemp.setString("phone", mrTemp.getPhoneNo());
            psTemp.setString("mobile", mrTemp.getMobileNo());
            psTemp.setString("fax", DmShared.xNull(mrTemp.getFaxNo()));
            psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
            psTemp.setString("duplicateTIN", DmServer.vNull(duplicateCode, "0"));
            psTemp.setString("userName", DmServer.getSsnUserName(rss));
            psTemp.setString("costCenter", DmShared.foriegnKey(mrTemp.getCostCenterCode()));
            psTemp.setString("pan", DmShared.xNull(mrTemp.getPan().toUpperCase()));
            psTemp.setString("email", mrTemp.getEmail());
            psTemp.setString("tinNo", DmShared.xNull(mrTemp.getTIN()));
            psTemp.setString("creditLimit", DmServer.vNull(mrTemp.getCreditLimit(), "0.00"));
            psTemp.setString("dueDays", DmServer.vNull(mrTemp.getCreditPeriod(), "0"));
            psTemp.setString("partyCat", DmShared.foriegnKey(mrTemp.getPartyCategoryCode()));
            psTemp.setString("partyType", mrTemp.getPartyTypeCode());
            psTemp.setString("iecCode", mrTemp.getIECNo());
            psTemp.setString("tdsCatg", DmShared.foriegnKey(mrTemp.getTdsCategoryCode()));
            psTemp.setString("zone", DmShared.foriegnKey(mrTemp.getZoneCode()));
            psTemp.setString("ledgerGroup", DmShared.foriegnKey(mrTemp.getLedgerGroupCode()));
            psTemp.setString("distributor", DmShared.foriegnKey(mrTemp.getReferenceToCode()));
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(rss));
            psTemp.setString("drugLicense", DmShared.xNull(mrTemp.getDrugLicense()));
            psTemp.setString("registrationNo", DmShared.xNull(mrTemp.getRegistrationNo()));
            psTemp.setString("invoiceDiscount", DmServer.vNull(mrTemp.getInvoiceDis(), "0.00"));
            psTemp.setString("expiryTerms", DmServer.vNull(mrTemp.getExpTerms(), "0"));
            psTemp.setString("minimum", DmServer.vNull(mrTemp.getMinimum(), "0"));
            psTemp.setString("maximum", DmServer.vNull(mrTemp.getMaximum(), "0"));
            psTemp.setString("reorderLevel", DmServer.vNull(mrTemp.getReOrderLevel(), "0.00"));
            psTemp.setString("isNonMoving", DmServer.vNull(nonmovingCode, "0"));
            psTemp.setString("isMonopoly", DmServer.vNull(monopolyCode, "0"));
            psTemp.setString("aadhaarNo", DmShared.xNull(mrTemp.getAadhaar()));
            psTemp.setDate("gsTinDate", DmServer.convertDate(mrTemp.getGstinDate()));
            psTemp.setString("gsTIN", DmShared.xNull(mrTemp.getGstin().toUpperCase()));
            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
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

            // ************************************** INSERT INTO SUBGROUP2 ******************************
            sqlQuery = "Select Count(*) As Cnt From SubGroup2 Where SubCode= :subCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("subCode", mrTemp.getCode());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") == 0) {
                sqlQuery = "Insert Into SubGroup2 (SubCode) "
                        + "Values (:subCode) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("subCode", mrTemp.getCode());

            }
            psTemp.executeUpdate();
            rsTemp.close();
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
