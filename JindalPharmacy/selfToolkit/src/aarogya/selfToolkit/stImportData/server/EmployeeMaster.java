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

public class EmployeeMaster implements DmImportData_Interface {

    private final LibraryVoucherCategory lvc = new LibraryVoucherCategory();
    private final String vCat = lvc.EMPLOYEE_MASTER;

    class Model {

        private String Prefix;
        private String code;
        private String mannualCode;
        private String empName;
        private String displayName;
        private String gender;
        private String maritalStatus;
        private String religion;
        private String acGroup;
        private String nature;
        private String zone;
        private String designation;
        private String department;
        private String fatherName;
        private String phoneNo;
        private String mobileNo;
        private String address1;
        private String address2;
        private String addressTemp1;
        private String addressTemp2;
        private String city;
        private String cityTemp;
        private String pin;
        private String pinTemp;
        private String dob;
        private String anniDOB;
        private String jnDOB;
        private String resigDOB;
        private String bloodGroup;
        private String pan;
        private String ifsc;
        private String bankACNo;
        private String pfACNo;
        private String licenseNo;
        private String passportNo;
        private String passIssueDt;
        private String passExpDt;
        private String passIssuePlace;
        private String email;
        private String qualification;
        private String tdsCat;
        private String remark;
        private String active;

        public Model() {
        }

        public String getPrefix() {
            return Prefix;
        }

        public void setPrefix(String Prefix) {
            this.Prefix = Prefix;
        }

        public String getMannualCode() {
            return mannualCode;
        }

        public void setMannualCode(String mannualCode) {
            this.mannualCode = mannualCode;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getEmpName() {
            return empName;
        }

        public void setEmpName(String empName) {
            this.empName = empName;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getMaritalStatus() {
            return maritalStatus;
        }

        public void setMaritalStatus(String maritalStatus) {
            this.maritalStatus = maritalStatus;
        }

        public String getReligion() {
            return religion;
        }

        public void setReligion(String religion) {
            this.religion = religion;
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

        public String getZone() {
            return zone;
        }

        public void setZone(String zone) {
            this.zone = zone;
        }

        public String getDesignation() {
            return designation;
        }

        public void setDesignation(String designation) {
            this.designation = designation;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public String getFatherName() {
            return fatherName;
        }

        public void setFatherName(String fatherName) {
            this.fatherName = fatherName;
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

        public String getAddressTemp1() {
            return addressTemp1;
        }

        public void setAddressTemp1(String addressTemp1) {
            this.addressTemp1 = addressTemp1;
        }

        public String getAddressTemp2() {
            return addressTemp2;
        }

        public void setAddressTemp2(String addressTemp2) {
            this.addressTemp2 = addressTemp2;
        }

        public String getCityTemp() {
            return cityTemp;
        }

        public void setCityTemp(String cityTemp) {
            this.cityTemp = cityTemp;
        }

        public String getPinTemp() {
            return pinTemp;
        }

        public void setPinTemp(String pinTemp) {
            this.pinTemp = pinTemp;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getPin() {
            return pin;
        }

        public void setPin(String pin) {
            this.pin = pin;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getAnniDOB() {
            return anniDOB;
        }

        public void setAnniDOB(String anniDOB) {
            this.anniDOB = anniDOB;
        }

        public String getJnDOB() {
            return jnDOB;
        }

        public void setJnDOB(String jnDOB) {
            this.jnDOB = jnDOB;
        }

        public String getResigDOB() {
            return resigDOB;
        }

        public void setResigDOB(String resigDOB) {
            this.resigDOB = resigDOB;
        }

        public String getBloodGroup() {
            return bloodGroup;
        }

        public void setBloodGroup(String bloodGroup) {
            this.bloodGroup = bloodGroup;
        }

        public String getPan() {
            return pan;
        }

        public void setPan(String pan) {
            this.pan = pan;
        }

        public String getIfsc() {
            return ifsc;
        }

        public void setIfsc(String ifsc) {
            this.ifsc = ifsc;
        }

        public String getBankACNo() {
            return bankACNo;
        }

        public void setBankACNo(String bankACNo) {
            this.bankACNo = bankACNo;
        }

        public String getPfACNo() {
            return pfACNo;
        }

        public void setPfACNo(String pfACNo) {
            this.pfACNo = pfACNo;
        }

        public String getLicenseNo() {
            return licenseNo;
        }

        public void setLicenseNo(String licenseNo) {
            this.licenseNo = licenseNo;
        }

        public String getPassportNo() {
            return passportNo;
        }

        public void setPassportNo(String passportNo) {
            this.passportNo = passportNo;
        }

        public String getPassIssueDt() {
            return passIssueDt;
        }

        public void setPassIssueDt(String passIssueDt) {
            this.passIssueDt = passIssueDt;
        }

        public String getPassExpDt() {
            return passExpDt;
        }

        public void setPassExpDt(String passExpDt) {
            this.passExpDt = passExpDt;
        }

        public String getPassIssuePlace() {
            return passIssuePlace;
        }

        public void setPassIssuePlace(String passIssuePlace) {
            this.passIssuePlace = passIssuePlace;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getQualification() {
            return qualification;
        }

        public void setQualification(String qualification) {
            this.qualification = qualification;
        }

        public String getTdsCat() {
            return tdsCat;
        }

        public void setTdsCat(String tdsCat) {
            this.tdsCat = tdsCat;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getActive() {
            return active;
        }

        public void setActive(String active) {
            this.active = active;
        }

    }

    private DmRSS rss;
    private DmHandelFile handelFileLog;
    private String lineNoDisplay = "", filterNature;

    public EmployeeMaster() {
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
        node.setSample("Shaktiman");
        node.setInstruction("{unique;mandatory;alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("mannualCode");
        node.setSample("MannualCode");
        node.setInstruction("{unique;alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("DisplayName");
        node.setSample("Harry");
        node.setInstruction("{mandatory;alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("Gender");
        node.setSample("Male");
        node.setInstruction("{mandatory;alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("MaritalStatus");
        node.setSample("UnMarried");
        node.setInstruction("{mandatory;alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("Religion");
        node.setSample("Hindu");
        node.setInstruction("{alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("AccountGroup");
        node.setSample("Employee");
        node.setInstruction("{mandatory;alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("Nature");
        node.setSample("employee");
        node.setInstruction("{mandatory;alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("Zone");
        node.setSample("Gomti nagar");
        node.setInstruction("{alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("Designation");
        node.setSample("Team Leader");
        node.setInstruction("{alphanumeric;35c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("Department");
        node.setSample("Accounts");
        node.setInstruction("{alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("FatherName");
        node.setSample("Sushil Kumar Shinde");
        node.setInstruction("{alphanumeric;50c}");
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
        node.setHeader("Address1");
        node.setSample("Mall Road");
        node.setInstruction("{alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("Address2");
        node.setSample("Kanpur");
        node.setInstruction("{alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("City");
        node.setSample("Kanpur");
        node.setInstruction("{alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("Pin");
        node.setSample("284002");
        node.setInstruction("{alphanumeric;6c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("TemporaryAddress1");
        node.setSample("Mall Road");
        node.setInstruction("{alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("TemporaryAddress2");
        node.setSample("Kanpur");
        node.setInstruction("{alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("TemporaryCity");
        node.setSample("Kanpur");
        node.setInstruction("{alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("TemporaryPin");
        node.setSample("284002");
        node.setInstruction("{alphanumeric;6c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("DateOfBirth");
        node.setSample("28-Oct-1985");
        node.setInstruction("{Date;11c;dd-MMM-yyyy}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("AnniversaryDate");
        node.setSample("22-June-2014");
        node.setInstruction("{Date;11c;dd-MMM-yyyy}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("JoiningDate");
        node.setSample("22-June-2014");
        node.setInstruction("{Date;mandatory;11c;dd-MMM-yyyy}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("ResignationDate");
        node.setSample("22-June-2014");
        node.setInstruction("{Date;11c;dd-MMM-yyyy}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("BloodGroup");
        node.setSample("A+");
        node.setInstruction("{alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("Pan");
        node.setSample("6444444666");
        node.setInstruction("{alphanumeric;20c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("IFSC");
        node.setSample("PUNB0002100");
        node.setInstruction("{alphanumeric;15c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("BankAcoountNo");
        node.setSample("1314423242343");
        node.setInstruction("{alphanumeric;20c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("PFAcoountNo");
        node.setSample("1314423242343");
        node.setInstruction("{alphanumeric;25c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("LicenseNo");
        node.setSample("w24345");
        node.setInstruction("{alphanumeric;25c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("PassportNo");
        node.setSample("1224345");
        node.setInstruction("{alphanumeric;20c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("PassportIssueDate");
        node.setSample("22-June-2014");
        node.setInstruction("{Date;11c;dd-MMM-yyyy}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("ExpiryDate");
        node.setSample("22-June-2017");
        node.setInstruction("{Date;11c;dd-MMM-yyyy}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("PassportIssuePlace");
        node.setSample("Delhi");
        node.setInstruction("{alphanumeric;20c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("Email");
        node.setSample("emailt51@gmail.com");
        node.setInstruction("{alphanumeric;250c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("Qualification");
        node.setSample("MCA");
        node.setInstruction("{alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("TDSCategory");
        node.setSample("TDSCategory1");
        node.setInstruction("{alphanumeric;35c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("Remark");
        node.setSample("M");
        node.setInstruction("{alphanumeric;250c}");
        lstCSV.add(node);

        fileDetail.getHmCSV().addAll(lstCSV);
        fileDetail.setImportTo("Employee Master");
        fileDetail.setServerClassName(EmployeeMaster.class.getName());

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
        int rtnStatus = 0, status = 0;
        try {

            this.rss = rss;
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));
            if (DmShared.xNull(importData.getLogFilePath()).trim().equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new Exception("Log file path can not be blank.");
            }
            handelFileLog = new DmHandelFile(importData.getLogFilePath(), true);

            filterNature = DmServer.getCodeViaManualCode("EM", "manualCode", "subgroupNature", "Code", cnTemp);

            if (DmShared.iNull(filterNature).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, "Employee 'EM' nature not exists.");
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
                        if (headerName.trim().equalsIgnoreCase("Initial")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPrefix(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("Name")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setEmpName(fieldValue);
                            }

                        } else if (headerName.trim().equalsIgnoreCase("mannualCode")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setMannualCode(fieldValue);
                            }

                        } else if (headerName.trim().equalsIgnoreCase("DisplayName")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setDisplayName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("Gender")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setGender(fieldValue);
                            }

                        } else if (headerName.trim().equalsIgnoreCase("MaritalStatus")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setMaritalStatus(fieldValue);
                            }

                        } else if (headerName.trim().equalsIgnoreCase("Religion")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setReligion(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("AccountGroup")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setAcGroup(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("Nature")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setNature(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("Zone")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setZone(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("Designation")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 35, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setDesignation(fieldValue);
                            }

                        } else if (headerName.trim().equalsIgnoreCase("Department")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setDepartment(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("FatherName")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setFatherName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("Phone")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 35, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPhoneNo(fieldValue);
                            }

                        } else if (headerName.trim().equalsIgnoreCase("Mobile")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 35, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setMobileNo(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("Address1")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setAddress1(fieldValue);
                            }

                        } else if (headerName.trim().equalsIgnoreCase("Address2")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setAddress2(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("City")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setCity(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("Pin")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 6, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPin(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("TemporaryAddress1")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setAddressTemp1(fieldValue);
                            }

                        } else if (headerName.trim().equalsIgnoreCase("TemporaryAddress2")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setAddressTemp2(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("TemporaryCity")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setCityTemp(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("TemporaryPin")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 6, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPinTemp(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("DateOfBirth")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, true, false, 11, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setDob(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("DateOfBirth")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, true, false, 11, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setDob(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("DateOfBirth")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, true, false, 11, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setDob(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("AnniversaryDate")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, true, false, 11, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setAnniDOB(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("JoiningDate")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, true, false, 11, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setJnDOB(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("ResignationDate")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, true, false, 11, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setResigDOB(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("BloodGroup")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setBloodGroup(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("Pan")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 20, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPan(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("IFSC")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 15, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setIfsc(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("BankAcoountNo")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 20, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setBankACNo(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("PFAcoountNo")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 25, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPfACNo(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("LicenseNo")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 25, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;

                            } else {
                                mrTemp.setLicenseNo(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("PassportNo")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 20, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPassportNo(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("PassportIssueDate")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, true, false, 11, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPassIssueDt(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("ExpiryDate")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, true, false, 11, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPassExpDt(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("PassportIssuePlace")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 20, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPassIssuePlace(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("Email")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 250, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setEmail(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("Qualification")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setQualification(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("TDSCategory")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 35, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setTdsCat(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("Remark")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 250, lineNoDisplay, handelFileLog)) {
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

    private int saveAccount(Model mrTemp) {
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        CallableStatement csTemp = null;
        String sqlQuery, manualCode = "", natureCode = "", genderCode = "", maritalStatusCode = "", bloodGrpCode = "", departmentCode = "",
                designationCode = "", tdsCatCode = "", groupCode = "", city = "", cityTemp = "", zone = "", vType = "", religionCode = "";
        int rtnStatus = 0, status = 0;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setEmpName(DmShared.removeSpace(mrTemp.getEmpName()));
            mrTemp.setCode(DmServer.getSerialNoBigInt(rss, "subGroup", cnTemp));
            if (DmShared.iNull(mrTemp.getCode()).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new UnsupportedOperationException("Invalid key.");
            }

            // ******************************** CHECK FOR DUPLICATE  NAME *****************************************
            sqlQuery = "SELECT COUNT(sG.name) AS cnt FROM subGroup as sG WHERE sG.name = :Name AND sG.subCode <> :subCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("subCode", DmShared.xNull(mrTemp.getCode()));
            psTemp.setString("Name", DmShared.xNull(mrTemp.getEmpName()));
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                status = DmImportStatus.WARNING.getId();
                throw new DmException(0, DmShared.xNull(mrTemp.getEmpName()) + " a/c name already exists.");
            }
            rsTemp.close();
            psTemp.close();
            // ******************************************* END ******************************************************

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
                fieldUDTHashMap.put("empName", new FieldUDT_Mdl("empName", DmShared.xNull(mrTemp.getEmpName()), DmShared.xNull(mrTemp.getEmpName()).trim()));
                fieldUDTHashMap.put("acGroup", new FieldUDT_Mdl("acGroup", DmShared.xNull(mrTemp.getAcGroup()).toString(), DmShared.xNull(mrTemp.getAcGroup()).trim()));
                fieldUDTHashMap.put("nature", new FieldUDT_Mdl("nature", DmShared.xNull(mrTemp.getNature()).toString(), DmShared.xNull(mrTemp.getNature()).trim()));
                csTemp = ((SQLServerConnection) ((ConnectionHandle) cnTemp).getInternalConnection()).prepareCall("{call getEmployeeId(?, ?)}");
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
            // ************************************** CHECK FOR MANUAL CODE  ****************************************
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
            // ************************************** GET GENDER CODE ****************************************
            sqlQuery = "SELECT CODE FROM hl7Mast where tableName='administrativeSex'  and name= :name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", mrTemp.getGender());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                genderCode = DmShared.iNull(rsTemp.getString("CODE")).toString();
            }
            rsTemp.close();
            psTemp.close();

            if (DmShared.iNull(genderCode).intValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, mrTemp.getGender() + " gender not exists.");
            }
            // ******************************************* END *****************************************************
            // ************************************** GET MARITAL STATUS CODE **************************************
            sqlQuery = "SELECT CODE FROM hl7Mast where tableName='maritalStatus' and name= :name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", mrTemp.getMaritalStatus());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                maritalStatusCode = DmShared.iNull(rsTemp.getString("CODE")).toString();
            }
            rsTemp.close();
            psTemp.close();

            if (DmShared.iNull(maritalStatusCode).intValue() == 0) {
                status = DmImportStatus.WARNING.getId();
                throw new DmException(0, mrTemp.getMaritalStatus() + " marital status not exists.");
            }
            // ******************************************* END *************************************************
            // ************************************** GET RELIGION CODE ****************************************
            sqlQuery = "SELECT CODE FROM hl7Mast where tableName='religion' and name= :name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", mrTemp.getReligion());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                religionCode = DmShared.iNull(rsTemp.getString("CODE")).toString();
            }
            rsTemp.close();
            psTemp.close();

            if (DmShared.iNull(religionCode).intValue() == 0) {
                status = DmImportStatus.WARNING.getId();
                handelFileLog.writeLine(lineNoDisplay + " " + mrTemp.getReligion() + " religion not exists.");
            }
            // ******************************************* END ************************************************

            // ************************************** GET ACCOUNT GROUP ****************************************
            groupCode = DmServer.getCodeViaManualCode(mrTemp.getAcGroup(), "GroupName", "AcGroup", "GroupCode", cnTemp);
            if (DmShared.iNull(groupCode).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, mrTemp.getAcGroup() + " a/c Group not exists.");
            }
            // ******************************************* END *************************************************

            // ************************************** GET NATURE ***********************************************
            natureCode = DmServer.getCodeViaManualCode(mrTemp.getNature(), "name", "subGroupNature", "code", cnTemp);
            if (DmShared.iNull(natureCode).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, mrTemp.getNature() + " nature not exists.");
            }
            // ******************************************* END ***********************************************
            // ************************************** GET ZONE ***********************************************
            zone = DmServer.getCodeViaManualCode(mrTemp.getZone(), "name", "zoneMast", "code", cnTemp);
            if (DmShared.iNull(zone).longValue() == 0) {
                status = DmImportStatus.WARNING.getId();
                handelFileLog.writeLine(lineNoDisplay + " " + mrTemp.getZone() + " zone not exists.");
            }
            // ******************************************* END ***********************************************
            // ************************************** GET DESIGNATION CODE ************************************
            designationCode = DmServer.getCodeViaManualCode(mrTemp.getDesignation(), "name", "DesignationMast", "code", cnTemp);
            if (DmShared.iNull(designationCode).longValue() == 0) {
                status = DmImportStatus.WARNING.getId();
                handelFileLog.writeLine(lineNoDisplay + " " + mrTemp.getDesignation() + " designation not exists.");
            }
            // ******************************************* END ***************************************************

            // ************************************** GET DEPARTMENT CODE ****************************************
            sqlQuery = "select dm.subCode from departmentMast dm "
                    + " left join subGroup sg on sg.subCode = dm.subCode "
                    + " where sg.name = :name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", mrTemp.getDepartment());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                departmentCode = DmShared.iNull(rsTemp.getString("subCode")).toString();
            }
            rsTemp.close();
            psTemp.close();

            if (DmShared.iNull(departmentCode).longValue() == 0) {
                status = DmImportStatus.WARNING.getId();
                handelFileLog.writeLine(lineNoDisplay + " " + mrTemp.getDepartment() + " department not exists.");
            }
            // ******************************************* END **********************************************

            // ************************************** GET CITY **********************************************
            if (!DmShared.xNull(mrTemp.getCity()).equalsIgnoreCase("")) {
                city = DmServer.getCodeViaManualCode(mrTemp.getCity(), "cityName", "city", "cityCode", cnTemp);
                if (DmShared.iNull(city).longValue() == 0) {
                    status = DmImportStatus.WARNING.getId();
                    handelFileLog.writeLine(lineNoDisplay + " " + mrTemp.getCity() + " permanent address city not exists.");
                }
            }
            // ******************************************* END ***********************************************

            // ************************************** GET CITY TEMP ******************************************
            if (!DmShared.xNull(mrTemp.getCityTemp()).equalsIgnoreCase("")) {
                cityTemp = DmServer.getCodeViaManualCode(mrTemp.getCityTemp(), "cityName", "city", "cityCode", cnTemp);
                if (DmShared.iNull(cityTemp).longValue() == 0) {
                    status = DmImportStatus.WARNING.getId();
                    handelFileLog.writeLine(lineNoDisplay + " " + mrTemp.getCityTemp() + " temporary address city not exists.");
                }
            }
            // ******************************************* END ***********************************************

            // ***************************** CHECK FOR DOB,DOA,DOJ,DOR,PASSPORT DATE **************************
            if (!DmShared.dNull(mrTemp.getDob()).trim().equalsIgnoreCase("")) {
                if ((DmShared.convertDate(mrTemp.getDob()).getTime() >= (DmShared.convertDate(DmServer.getServerDateTime()[0])).getTime())) {
                    status = DmImportStatus.WARNING.getId();
                    handelFileLog.writeLine(lineNoDisplay + " " + mrTemp.getDob() + "DOB should be less than  Current Date.");
                }
                if ((!DmShared.dNull(mrTemp.getAnniDOB()).trim().equalsIgnoreCase("")) && (!DmShared.dNull(mrTemp.getDob()).trim().equalsIgnoreCase(""))) {
                    if ((DmShared.convertDate(mrTemp.getDob()).getTime() >= (DmShared.convertDate(mrTemp.getAnniDOB())).getTime())) {
                        status = DmImportStatus.WARNING.getId();
                        handelFileLog.writeLine(lineNoDisplay + " " + mrTemp.getAnniDOB() + " Anniv. Date should be greater than  DOB.");
                    }
                }
                if ((!DmShared.dNull(mrTemp.getJnDOB()).trim().equalsIgnoreCase("")) && (!DmShared.dNull(mrTemp.getDob()).trim().equalsIgnoreCase(""))) {
                    if ((DmShared.convertDate(mrTemp.getDob()).getTime() >= (DmShared.convertDate(mrTemp.getJnDOB())).getTime())) {
                        status = DmImportStatus.WARNING.getId();
                        handelFileLog.writeLine(lineNoDisplay + " " + mrTemp.getJnDOB() + "Joining Date should be greater than DOB.");
                    }
                }
                if ((!DmShared.dNull(mrTemp.getDob()).trim().equalsIgnoreCase("")) && (!DmShared.dNull(mrTemp.getPassIssueDt()).trim().equalsIgnoreCase(""))) {
                    if ((DmShared.convertDate(mrTemp.getDob()).getTime() >= (DmShared.convertDate(mrTemp.getPassIssueDt())).getTime())) {
                        status = DmImportStatus.WARNING.getId();
                        handelFileLog.writeLine(lineNoDisplay + " " + mrTemp.getPassIssueDt() + "Passport Issue Date should be greater than  DOB.");
                    }
                }
            }
            if (!DmShared.dNull(mrTemp.getJnDOB()).trim().equalsIgnoreCase("")) {
                if ((DmShared.convertDate(mrTemp.getJnDOB()).getTime() > (DmShared.convertDate(DmServer.getServerDateTime()[0])).getTime())) {
                    status = DmImportStatus.WARNING.getId();
                    handelFileLog.writeLine(lineNoDisplay + " " + mrTemp.getJnDOB() + "Joining Date should be less than or equal to Current Date.");
                }
            }
            if ((!DmShared.dNull(mrTemp.getJnDOB()).trim().equalsIgnoreCase("")) && (!DmShared.dNull(mrTemp.getResigDOB()).trim().equalsIgnoreCase(""))) {
                if ((DmShared.convertDate(mrTemp.getJnDOB()).getTime() > (DmShared.convertDate(mrTemp.getResigDOB())).getTime())) {
                    status = DmImportStatus.WARNING.getId();
                    handelFileLog.writeLine(lineNoDisplay + " " + mrTemp.getJnDOB() + "Resignation Date should be greater than or equal  Joining Date.");
                }
            }
            if ((!DmShared.dNull(mrTemp.getPassIssueDt()).equalsIgnoreCase("")) && (DmShared.dNull(mrTemp.getPassExpDt()).trim().equalsIgnoreCase(""))) {
                status = DmImportStatus.WARNING.getId();
                handelFileLog.writeLine(lineNoDisplay + " " + mrTemp.getPassExpDt() + "Passport Expiry Date is mandatory when inserting Passport Issue Date.");
            }
            if ((!DmShared.dNull(mrTemp.getPassExpDt()).equalsIgnoreCase("")) && (DmShared.dNull(mrTemp.getPassIssueDt()).trim().equalsIgnoreCase(""))) {
                DmShared.isValidText(mrTemp.getPassIssueDt(), "Passport Issue Date", 1600);
                status = DmImportStatus.WARNING.getId();
                handelFileLog.writeLine(lineNoDisplay + " " + mrTemp.getResigDOB() + "Joining Date is mandatory when inserting Resignation Date.");
            }
            if ((!DmShared.dNull(mrTemp.getPassIssueDt()).trim().equalsIgnoreCase("")) && (!DmShared.dNull(mrTemp.getPassExpDt()).trim().equalsIgnoreCase(""))) {
                if ((DmShared.convertDate(mrTemp.getPassIssueDt()).getTime() >= (DmShared.convertDate(mrTemp.getPassExpDt())).getTime())) {
                    status = DmImportStatus.WARNING.getId();
                    handelFileLog.writeLine(lineNoDisplay + " " + mrTemp.getPassExpDt() + "Passport Expiry Date should be greater than  Passport Issue Date.");
                }
            }

            // ************************************** GET BLOOD GROUP CODE *************************************
            sqlQuery = "SELECT CODE FROM hl7Mast where tableName='bloodGroup' and name= :name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", mrTemp.getBloodGroup());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                bloodGrpCode = DmShared.iNull(rsTemp.getString("CODE")).toString();
            }
            rsTemp.close();
            psTemp.close();

            if (DmShared.iNull(bloodGrpCode).intValue() == 0) {
                status = DmImportStatus.WARNING.getId();
                handelFileLog.writeLine(lineNoDisplay + " " + mrTemp.getBloodGroup() + " bloodgroup not exists.");
            }
            // ******************************************* END ************************************************

            // ************************************** CHECK FOR PAN NO ****************************************
            if (!DmShared.xNull(mrTemp.getPan()).equalsIgnoreCase("") && DmShared.xNull(mrTemp.getPan()).length() != 10) {
                status = DmImportStatus.WARNING.getId();
                handelFileLog.writeLine(lineNoDisplay + " " + mrTemp.getPan() + " PAN should be of 10(Ten) characters.");
                // ******************************************* END *************************************************
            }

            // ************************************** GET TDS CATEGORY CODE ****************************************
            tdsCatCode = DmServer.getCodeViaManualCode(mrTemp.getTdsCat(), "name", "TdsCat", "code", cnTemp);
            if (DmShared.iNull(tdsCatCode).longValue() == 0) {
                status = DmImportStatus.WARNING.getId();
                handelFileLog.writeLine(lineNoDisplay + " " + mrTemp.getTdsCat() + " TDS category not exists.");
            }
            // ******************************************* END *****************************************************

            // ************************************** INSERT INTO SUBGROUP *****************************************
            sqlQuery = "Insert Into SubGroup(SubCode,Initial,Name,DisplayName,ManualCode,GroupCode,Nature,Zone,Phone,Mobile,PAN, ifscCode,EMail, "
                    + " Add1,Add2,filterNature,CityCode,PIN,Remark,U_EntDt,TDS_Catg, preparedBy,isActive) "
                    + "Values (:subCode, :initial,  :name,:displayName, :manualCode, :groupCode, :nature,:zone,:phone, :mobile,:pan,:ifscCode, "
                    + " :email, :add1, :add2, :filterNature, :cityCode,:pin,:remark, :uEntDt,:tdsCatg, :userName,:isActive) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("subCode", mrTemp.getCode());
            psTemp.setString("initial", mrTemp.getPrefix());
            psTemp.setString("name", mrTemp.getEmpName());
            psTemp.setString("displayName", mrTemp.getDisplayName());
            psTemp.setString("manualCode", manualCode);
            psTemp.setString("groupCode", DmShared.foriegnKey(groupCode));
            psTemp.setString("nature", DmShared.foriegnKey(natureCode));
            psTemp.setString("zone", DmShared.foriegnKey(zone));
            psTemp.setString("phone", mrTemp.getPhoneNo());
            psTemp.setString("mobile", mrTemp.getMobileNo());
            psTemp.setString("pan", mrTemp.getPan());
            psTemp.setString("ifscCode", mrTemp.getIfsc());
            psTemp.setString("email", mrTemp.getEmail());
            psTemp.setString("add1", mrTemp.getAddress1());
            psTemp.setString("add2", mrTemp.getAddress2());
            psTemp.setString("filterNature", DmShared.foriegnKey(filterNature));
            psTemp.setString("cityCode", DmShared.foriegnKey(city));
            psTemp.setString("pin", mrTemp.getPin());
            psTemp.setString("remark", mrTemp.getRemark());
            psTemp.setString("tdsCatg", DmShared.foriegnKey(tdsCatCode));
            psTemp.setString("userName", DmServer.getSsnUserName(rss));
            psTemp.setString("isActive", DmShared.xNull("1"));
            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            psTemp.executeUpdate();
            psTemp.close();
            // ******************************************* END **************************************************

            // ************************************** INSERT INTO SUBGROUP2 ************************************
            sqlQuery = "Select Count(*) As Cnt From SubGroup2 Where SubCode= :subCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("subCode", mrTemp.getCode());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") == 0) {
                sqlQuery = "Insert Into SubGroup2 (SubCode,genderCode,maritalStatusCode,religionCode,FatherName,DesignationCode, "
                        + " departmentCode,tADD1,tADD2,tCityCode,tPIN,dob,doa,doj,dor,bloodGroupCode,pfAcNo,bankAcNo,licenseNo,passportNo, "
                        + " pIssueDate,pExpDate,pIssuePlace,qualification) "
                        + " Values (:subCode,:genderCode,:maritalStatusCode,:religionCode,:fatherName,:DesignationCode,:departmentCode,:tADD1,"
                        + " :tADD2,:tCityCode,:tPIN,:dob,:doa,:doj,:dor,:bloodGroupCode,:pfAcNo,:bankAcNo,:licenseNo,:passportNo,"
                        + " :pIssueDate,:pExpDate,:pIssuePlace,:qualification) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("subCode", mrTemp.getCode());
                psTemp.setString("genderCode", DmShared.foriegnKey(genderCode));
                psTemp.setString("maritalStatusCode", DmShared.foriegnKey(maritalStatusCode));
                psTemp.setString("religionCode", DmShared.foriegnKey(religionCode));
                psTemp.setString("fatherName", mrTemp.getFatherName());
                psTemp.setString("departmentCode", DmShared.foriegnKey(departmentCode));
                psTemp.setString("DesignationCode", DmShared.foriegnKey(designationCode));
                psTemp.setString("tADD1", mrTemp.getAddressTemp1());
                psTemp.setString("tADD2", mrTemp.getAddressTemp2());
                psTemp.setString("tCityCode", DmShared.foriegnKey(cityTemp));
                psTemp.setString("tPIN", mrTemp.getPinTemp());
                psTemp.setDate("dob", DmServer.convertDate(mrTemp.getDob()));
                psTemp.setDate("doa", DmServer.convertDate(mrTemp.getAnniDOB()));
                psTemp.setDate("doj", DmServer.convertDate(mrTemp.getJnDOB()));
                psTemp.setDate("dor", DmServer.convertDate(mrTemp.getResigDOB()));
                psTemp.setString("bloodGroupCode", DmShared.foriegnKey(bloodGrpCode));
                psTemp.setString("pfAcNo", mrTemp.getPfACNo());
                psTemp.setString("bankAcNo", mrTemp.getBankACNo());
                psTemp.setString("licenseNo", mrTemp.getLicenseNo());
                psTemp.setString("passportNo", mrTemp.getPassportNo());
                psTemp.setDate("pIssueDate", DmServer.convertDate(mrTemp.getPassIssueDt()));
                psTemp.setDate("pExpDate", DmServer.convertDate(mrTemp.getPassExpDt()));
                psTemp.setString("pIssuePlace", mrTemp.getPassIssuePlace());
                psTemp.setString("qualification", mrTemp.getQualification());
                psTemp.executeUpdate();
                psTemp.close();
            }
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
