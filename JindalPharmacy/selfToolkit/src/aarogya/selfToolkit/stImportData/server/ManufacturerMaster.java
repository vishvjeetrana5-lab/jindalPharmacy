package aarogya.selfToolkit.stImportData.server;

import dataman.dmToolkit.dmImportData.server.DmImportData_Interface;
import dataman.dmToolkit.dmImportData.shared.CSVData;
import dataman.dmToolkit.dmImportData.shared.ImportData_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
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
import dataman.dmToolkit.dmImportData.shared.DmImportStatus;

public class ManufacturerMaster implements DmImportData_Interface {

    private final String vCat = new ProjectVoucherCategory().MANUFACTURER_MAST;

    class Model {

        private String code, acInitialCode, acInitialName, accountName, displayName, acGroupCode, acGroupName, natureCode, natureName, costCenterCode, costCenterName, address1, address2,
                mannualCode, cityCode, cityName, pinCode, partyTypeCode, partyTypeName, gstin, gstinDate, aadhaar, pan, fatherName, zoneCode, zoneName, phoneNo, mobileNo, faxNo,
                email, tdsCategoryCode, tdsCategoryName, referenceToCode, referenceToName, contactPerson, policyNo, TIN, isDuplicate, cstNo, stNo, ECCNo, ExciseNo, range, division,
                creditLimit, dueDays, IECNo, ledgerGroupCode, ledgerGroupName, remark;

        public Model() {
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMannualCode() {
            return mannualCode;
        }

        public void setMannualCode(String mannualCode) {
            this.mannualCode = mannualCode;
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

        public String getFatherName() {
            return fatherName;
        }

        public void setFatherName(String fatherName) {
            this.fatherName = fatherName;
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

        public String getContactPerson() {
            return contactPerson;
        }

        public void setContactPerson(String contactPerson) {
            this.contactPerson = contactPerson;
        }

        public String getPolicyNo() {
            return policyNo;
        }

        public void setPolicyNo(String policyNo) {
            this.policyNo = policyNo;
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

        public String getCstNo() {
            return cstNo;
        }

        public void setCstNo(String cstNo) {
            this.cstNo = cstNo;
        }

        public String getStNo() {
            return stNo;
        }

        public void setStNo(String stNo) {
            this.stNo = stNo;
        }

        public String getECCNo() {
            return ECCNo;
        }

        public void setECCNo(String ECCNo) {
            this.ECCNo = ECCNo;
        }

        public String getExciseNo() {
            return ExciseNo;
        }

        public void setExciseNo(String ExciseNo) {
            this.ExciseNo = ExciseNo;
        }

        public String getRange() {
            return range;
        }

        public void setRange(String range) {
            this.range = range;
        }

        public String getDivision() {
            return division;
        }

        public void setDivision(String division) {
            this.division = division;
        }

        public String getCreditLimit() {
            return creditLimit;
        }

        public void setCreditLimit(String creditLimit) {
            this.creditLimit = creditLimit;
        }

        public String getDueDays() {
            return dueDays;
        }

        public void setDueDays(String dueDays) {
            this.dueDays = dueDays;
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

    public ManufacturerMaster() {
    }

    @Override
    public ImportData_Mdl getImportDataModel() {
        final ImportData_Mdl fileDetail = new ImportData_Mdl();
        CSVData node;
        List<CSVData> lstCSV = new ArrayList<CSVData>();

        node = new CSVData();
        node.setHeader("initial");
        node.setSample("Mr.");
        node.setInstruction("{alphanumeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("name");
        node.setSample("Suraj");
        node.setInstruction("{unique;mandatory;alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("mannualCode");
        node.setSample("mannualCode");
        node.setInstruction("{unique;alphanumeri;50c}");
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
        node.setInstruction("{alphanumeric;50c}");
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
        node.setHeader("fatherName");
        node.setSample("Shiv Kumar");
        node.setInstruction("{alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("zone");
        node.setSample("jhansi Zone");
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
        node.setHeader("TDSCategory");
        node.setSample("category1");
        node.setInstruction("{alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("referenceTo");
        node.setSample("reference1");
        node.setInstruction("{alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("contactPerson");
        node.setSample("sumit");
        node.setInstruction("{alphanumeric;35c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("policyNo");
        node.setSample("pol123");
        node.setInstruction("{alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("TIN");
        node.setSample("71234567890");
        node.setInstruction("{alphanumeric;20c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("duplicate");
        node.setSample("Yes");
        node.setInstruction("{alphanumeric(Yes/No);3c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("CSTNo");
        node.setSample("6444444666");
        node.setInstruction("{alphanumeric;40c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("STNo");
        node.setSample("12345678901");
        node.setInstruction("{alphanumeric;40c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("ECCNo");
        node.setSample("2537652376");
        node.setInstruction("{alphanumeric;35c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("exciseNo");
        node.setSample("12345678901");
        node.setInstruction("{alphanumeric;35c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("range");
        node.setSample("113156545654");
        node.setInstruction("{alphanumeric;35c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("division");
        node.setSample("Reliance Group");
        node.setInstruction("{alphanumeric;35c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("creditLimit");
        node.setSample("5000");
        node.setInstruction("{numeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("duedays");
        node.setSample("23");
        node.setInstruction("{numeric;5c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("IECNo");
        node.setSample("6253673");
        node.setInstruction("{alphanumeric;35c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("ledgerGroup");
        node.setSample("group1");
        node.setInstruction("{alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("remark");
        node.setSample("testing");
        node.setInstruction("{alphanumeric;250c}");
        lstCSV.add(node);

        fileDetail.getHmCSV().addAll(lstCSV);
        fileDetail.setImportTo("Manfacturer Master");
        fileDetail.setServerClassName(ManufacturerMaster.class.getName());

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
        int status = 0, rtnStatus = 0;
        DmImportStatus importStatus = null;
        try {

            this.rss = rss;
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));
            if (DmShared.xNull(importData.getLogFilePath()).trim().equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new Exception("Log file path can not be blank.");
            }
            handelFileLog = new DmHandelFile(importData.getLogFilePath(), true);
            currentDate = DmServer.getServerDateTime();
            filterNature = DmServer.getCodeViaManualCode("MF", "manualCode", "subgroupNature", "Code", cnTemp);
            if (DmShared.iNull(filterNature).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, "Manufacturer 'MF' nature not exists.");
            }
            cnTemp.close();
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
                        if (headerName.trim().equalsIgnoreCase("initial")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setAcInitialName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("name")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setAccountName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("mannualCode")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setMannualCode(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("displayName")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setDisplayName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("acGroup")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setAcGroupName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("nature")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setNatureName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("costCenter")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setCostCenterName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("address1")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setAddress1(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("address2")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setAddress2(fieldValue);
                            }
                            //<editor-fold defaultstate="collapsed" desc="temprorary mandatory false for city">
                        } else if (headerName.trim().equalsIgnoreCase("city")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setCityName(fieldValue);
                            }
                            //</editor-fold>

                        } else if (headerName.trim().equalsIgnoreCase("pin")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 6, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPinCode(fieldValue);
                            }
                            //<editor-fold defaultstate="collapsed" desc="temprorary false for Party Type">
                        } else if (headerName.trim().equalsIgnoreCase("partyType(GST)")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 15, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPartyTypeName(fieldValue);
                            }
                            //</editor-fold>
                        } else if (headerName.trim().equalsIgnoreCase("GSTIN")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 15, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setGstin(fieldValue);
                            }

                        } else if (headerName.trim().equalsIgnoreCase("GSTINDate")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, true, false, 11, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setGstinDate(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("aadhaar")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 12, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setAadhaar(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("pan")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPan(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("fatherName")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setFatherName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("zone")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setZoneName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("phoneNo")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 35, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPhoneNo(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("mobileNo")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 35, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setMobileNo(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("faxNo")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 35, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setFaxNo(fieldValue);
                            }

                        } else if (headerName.trim().equalsIgnoreCase("email")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 250, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setEmail(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("TDSCategory")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setTdsCategoryName(fieldValue);
                            }

                        } else if (headerName.trim().equalsIgnoreCase("referenceTo")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setReferenceToName(fieldValue);
                            }

                        } else if (headerName.trim().equalsIgnoreCase("contactPerson")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 35, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setContactPerson(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("policyNo")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPolicyNo(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("TIN")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 20, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setTIN(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("duplicate")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 3, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setIsDuplicate(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("CSTNo")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 40, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setCstNo(fieldValue);
                            }

                        } else if (headerName.trim().equalsIgnoreCase("STNo")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 40, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setStNo(fieldValue);
                            }

                        } else if (headerName.trim().equalsIgnoreCase("ECCNo")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 35, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setECCNo(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("exciseNo")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 35, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setExciseNo(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("range")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 35, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setRange(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("division")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 35, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setDivision(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("creditLimit")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setCreditLimit(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("duedays")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 5, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setDueDays(fieldValue);
                            }

                        } else if (headerName.trim().equalsIgnoreCase("IECNo")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 35, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setIECNo(fieldValue);
                            }

                        } else if (headerName.trim().equalsIgnoreCase("ledgerGroup")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setLedgerGroupName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("remark")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 250, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setRemark(fieldValue);
                            }
                        }
                    }
                    if (isFound) {
                        status = saveManufacturer(mrTemp);
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

    private int saveManufacturer(Model mrTemp) {
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        CallableStatement csTemp = null;
        String sqlQuery, vType = "", manualCode = "", duplicateCode = "", stateNumericCode = "";
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
            if (!DmShared.xNull(mrTemp.getMannualCode()).trim().equalsIgnoreCase("")) {
                HashMap<String, FieldUDT_Mdl> fieldUDTHashMap = new HashMap<String, FieldUDT_Mdl>();
                fieldUDTHashMap.put("manufacturerName", new FieldUDT_Mdl("manufacturerName", DmShared.xNull(mrTemp.getAccountName()), DmShared.xNull(mrTemp.getAccountName()).trim()));
                fieldUDTHashMap.put("acGroup", new FieldUDT_Mdl("acGroup", DmShared.xNull(mrTemp.getAcGroupName()).toString(), DmShared.xNull(mrTemp.getAcGroupName()).trim()));
                fieldUDTHashMap.put("nature", new FieldUDT_Mdl("nature", DmShared.xNull(mrTemp.getNatureName()).toString(), DmShared.xNull(mrTemp.getNatureName()).trim()));
                csTemp = ((SQLServerConnection) ((ConnectionHandle) cnTemp).getInternalConnection()).prepareCall("{call getManufacturerId(?, ?)}");
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

            // ************************************** GET CITY **********************************************
            stateNumericCode = "";
            if (!DmShared.xNull(mrTemp.getCityName()).trim().equalsIgnoreCase("")) {
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
            }

            //<editor-fold defaultstate="collapsed" desc="City Is Mandatory If Party Type Exists">
            if (!DmShared.xNull(mrTemp.getPartyTypeName()).equalsIgnoreCase("")) {
                if (DmShared.iNull(mrTemp.getCityCode()).longValue() == 0) {
                    status = DmImportStatus.ERROR.getId();
                    throw new DmException(0, mrTemp.getCityName() + " city not exists.");
                }
            }
            //</editor-fold>
            // ******************************************* END ***********************************************
            // ************************************** GET ZONE ***********************************************
            if (!DmShared.xNull(mrTemp.getZoneName()).equalsIgnoreCase("")) {
                mrTemp.setZoneCode(DmServer.getCodeViaManualCode(mrTemp.getZoneName(), "name", "zoneMast", "code", cnTemp));
                if (DmShared.iNull(mrTemp.getZoneCode()).longValue() == 0) {
                    status = DmImportStatus.WARNING.getId();
                    handelFileLog.writeLine(lineNoDisplay + " " + mrTemp.getZoneName() + " zone not exists.");
                }
            }
            // ******************************************* END ***********************************************

            // ************************************** GET TDS CATEGORY CODE ****************************************
            if (!DmShared.xNull(mrTemp.getTdsCategoryName()).equalsIgnoreCase("")) {
                mrTemp.setTdsCategoryCode(DmServer.getCodeViaManualCode(mrTemp.getTdsCategoryName(), "name", "TdsCat", "code", cnTemp));
                if (DmShared.iNull(mrTemp.getTdsCategoryCode()).longValue() == 0) {
                    status = DmImportStatus.WARNING.getId();
                    handelFileLog.writeLine(lineNoDisplay + " " + mrTemp.getTdsCategoryName() + " TDS category not exists.");
                }
                // ******************************************* END *****************************************************
            }

            // ************************************** GET TDS REFERENCE CODE ****************************************
            if (!DmShared.xNull(mrTemp.getReferenceToName()).equalsIgnoreCase("")) {
                mrTemp.setReferenceToCode(DmServer.getCodeViaManualCode(mrTemp.getReferenceToName(), "name", "SubGroup", "SubCode", cnTemp));
                if (DmShared.iNull(mrTemp.getReferenceToCode()).longValue() == 0) {
                    status = DmImportStatus.WARNING.getId();
                    handelFileLog.writeLine(lineNoDisplay + " " + mrTemp.getReferenceToName() + " Reference To not exists.");
                }
            }
            // ******************************************* END *****************************************************

            // ************************************** CHECK FOR TIN NO **********************************************
            if (!DmShared.xNull(mrTemp.getTIN()).equalsIgnoreCase("") && DmShared.xNull(mrTemp.getTIN()).length() < 11) {
                status = DmImportStatus.WARNING.getId();
                throw new UnsupportedOperationException("TIN Should Not Be Greater Than 11(Eleven) Characters.");
            }
            // ******************************************* END ******************************************************

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

            if (!DmShared.xNull(mrTemp.getPartyTypeName()).equalsIgnoreCase("")) {
                // ************************************** GET PARTY TYPE ********************************************
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
                // ******************************************* END **************************************************
            }

            if (DmShared.xNull(mrTemp.getPartyTypeCode()).equalsIgnoreCase((DmEnum.DmPartyType.Registered.getId()))
                    || DmShared.xNull(mrTemp.getPartyTypeCode()).equalsIgnoreCase((DmEnum.DmPartyType.Composite.getId()))) {
                DmShared.isValidText(mrTemp.getGstin(), "GSTIN No.", 0);
                DmShared.isValidText(mrTemp.getGstinDate(), "GSTIN Date", 0);
            }
            if (!DmShared.xNull(mrTemp.getGstin()).equalsIgnoreCase("")
                    || !DmShared.dNull(mrTemp.getGstinDate()).equalsIgnoreCase("")) {
                if (DmShared.xNull(mrTemp.getPartyTypeName()).trim().equalsIgnoreCase("")) {
                    status = DmImportStatus.ERROR.getId();
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
                    status = DmImportStatus.ERROR.getId();
                    throw new DmException(0, "In case of unregistered party type, you should not fill GSTIN No. and GSTIN Date.");
                }
                if (isGstApplicable == true) {
                    if (DmShared.xNull(mrTemp.getAadhaar()).equalsIgnoreCase("")) {
                        if (DmShared.xNull(mrTemp.getPan()).equalsIgnoreCase("")) {
                            status = DmImportStatus.ERROR.getId();
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

            // ************************************** CHECK FOR PAN NO **********************************************
            if (!DmShared.xNull(mrTemp.getPan()).trim().equalsIgnoreCase("")) {
                if (DmShared.xNull(mrTemp.getPan()).length() != 10) {
                    status = DmImportStatus.WARNING.getId();
                    throw new DmException(0, mrTemp.getPan() + "PAN should be of 10(Ten) characters.");
                } else {
                    DmShared.isValidPanNo(mrTemp.getPan(), 0);
                }
            }
            // ******************************************* END **************************************************

            // ************************************** INSERT INTO SUBGROUP ********************************************
            sqlQuery = "Insert Into SubGroup(SubCode,Name,GroupCode,ManualCode,Nature,Add1,Add2,CityCode,PIN,"
                    + "Phone,Mobile,FAX,ContactPerson,Remark,isDuplicateTIN,PolicyNo,U_EntDt,CostCenter,PAN,CSTNo,EMail,TINNo,"
                    + "CreditLimit,DueDays,PartyType,ECCCode,Excise,Range,Division,STNo,IECCode,TDS_Catg,Zone,LedgerGroup,"
                    + "Distributor,Site_Code,DisplayName,Initial, preparedBy,filterNature,aadhaarNo,gsTinDate,gsTIN) "
                    + "Values (:subCode,  :name, :groupCode, :manualCode, :nature, :add1, :add2, :cityCode, :pin, "
                    + ":phone, :mobile, :fax, :contactPerson, :remark, :duplicateTIN, :policyNo, :uEntDt, :costCenter, :pan, :cstNo, :email,"
                    + ":tinNo, :creditLimit, :dueDays, :partyType, :eccCode, :Excise, :range, :division, :stNo, :iecCode,"
                    + ":tdsCatg, :zone, :ledgerGroup, :distributor, :siteCode, :displayName, :initial, :userName,:filterNature,:aadhaarNo,:gsTinDate,:gsTIN) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("subCode", mrTemp.getCode());
            psTemp.setString("name", DmShared.xNull(mrTemp.getAccountName()));
            psTemp.setString("groupCode", DmShared.foriegnKey(mrTemp.getAcGroupCode()));
            psTemp.setString("manualCode", DmShared.xNull(manualCode));
            psTemp.setString("nature", DmShared.foriegnKey(mrTemp.getNatureCode()));
            psTemp.setString("add1", DmShared.xNull(mrTemp.getAddress1()));
            psTemp.setString("add2", DmShared.xNull(mrTemp.getAddress2()));
            psTemp.setString("filterNature", DmShared.foriegnKey(filterNature));
            psTemp.setString("cityCode", DmShared.foriegnKey(mrTemp.getCityCode()));
            psTemp.setString("pin", DmShared.xNull(mrTemp.getPinCode()));
            psTemp.setString("phone", DmShared.xNull(mrTemp.getPhoneNo()));
            psTemp.setString("mobile", DmShared.xNull(mrTemp.getMobileNo()));
            psTemp.setString("fax", DmShared.xNull(mrTemp.getFaxNo()));
            psTemp.setString("contactPerson", DmShared.xNull(mrTemp.getContactPerson()));
            psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
            psTemp.setString("duplicateTIN", DmShared.xNull(duplicateCode));
            psTemp.setString("policyNo", DmShared.xNull(mrTemp.getPolicyNo()));
            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            psTemp.setString("userName", DmServer.getSsnUserName(rss));
            psTemp.setString("costCenter", DmShared.foriegnKey(mrTemp.getCostCenterCode()));
            psTemp.setString("pan", DmShared.xNull(mrTemp.getPan().toUpperCase()));
            psTemp.setString("cstNo", DmShared.xNull(mrTemp.getCstNo()));
            psTemp.setString("email", DmShared.xNull(mrTemp.getEmail()));
            psTemp.setString("tinNo", DmShared.xNull(mrTemp.getTIN()));
            psTemp.setString("creditLimit", DmServer.vNull(mrTemp.getCreditLimit(), "0.00"));
            psTemp.setString("dueDays", DmServer.vNull(mrTemp.getDueDays(), "0"));
            psTemp.setString("partyType", mrTemp.getPartyTypeCode());
            psTemp.setString("eccCode", DmShared.xNull(mrTemp.getECCNo()));
            psTemp.setString("Excise", DmShared.xNull(mrTemp.getExciseNo()));
            psTemp.setString("range", DmShared.xNull(mrTemp.getRange()));
            psTemp.setString("division", DmShared.xNull(mrTemp.getDivision()));
            psTemp.setString("stNo", DmShared.xNull(mrTemp.getStNo()));
            psTemp.setString("iecCode", DmShared.xNull(mrTemp.getIECNo()));
            psTemp.setString("tdsCatg", DmShared.foriegnKey(mrTemp.getTdsCategoryCode()));
            psTemp.setString("zone", DmShared.foriegnKey(mrTemp.getZoneCode()));
            psTemp.setString("ledgerGroup", DmShared.foriegnKey(mrTemp.getLedgerGroupCode()));
            psTemp.setString("distributor", DmShared.foriegnKey(mrTemp.getReferenceToCode()));
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(rss));
            psTemp.setString("displayName", DmShared.xNull(mrTemp.getDisplayName()));
            psTemp.setString("initial", DmShared.xNull(mrTemp.getAcInitialCode()));
            psTemp.setString("aadhaarNo", DmShared.xNull(mrTemp.getAadhaar()));
            psTemp.setDate("gsTinDate", DmServer.convertDate(mrTemp.getGstinDate()));
            psTemp.setString("gsTIN", DmShared.xNull(mrTemp.getGstin().toUpperCase()));
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
                sqlQuery = "Insert Into SubGroup2 (SubCode,FatherName) "
                        + "Values (:subCode,:fatherName) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("subCode", mrTemp.getCode());
                psTemp.setString("fatherName", mrTemp.getFatherName());

            } else {
                sqlQuery = "update SubGroup2 Set "
                        + "FatherName=:fatherName "
                        + " where SubCode=:subCode";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("fatherName", mrTemp.getFatherName());
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
