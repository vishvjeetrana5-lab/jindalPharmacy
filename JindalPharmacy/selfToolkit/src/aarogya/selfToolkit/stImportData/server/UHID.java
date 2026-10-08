package aarogya.selfToolkit.stImportData.server;

import dataman.dmToolkit.dmImportData.server.DmImportData_Interface;
import dataman.dmToolkit.dmImportData.shared.CSVData;
import dataman.dmToolkit.dmImportData.shared.ImportData_Mdl;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.shared.STShared;
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

public class UHID implements DmImportData_Interface {

    class UHID_Mdl {

        private String UHIDDocId, recIdUHID, patientInitial, patientFirstName, patientMiddleName, patientLastName, patientName, patientAliasName, guardianInitial,
                guardianName, address1, address2, areaText, mobNo, idTypeText, idNo, email, dob, genderText, bloodGroupText, maritalStatusText,
                ageYear, ageMonth, ageDay, approvedBy, city, nationalityName, religionText, remark, occupation, pinCode, emgContactName,
                emgContactMobile, classText, tempDOB, displayDOB, orgText, orgShortName, orgPolcyNo, orgHolderName, orgRelationShip,
                guardianRelationText, castText, relationName, rankNo;
//showDDUptoMonth, showMMUptoYear,

        public UHID_Mdl() {
        }

        public String getUHIDDocId() {
            return UHIDDocId;
        }

        public void setUHIDDocId(String UHIDDocId) {
            this.UHIDDocId = UHIDDocId;
        }

        public String getPatientInitial() {
            return patientInitial;
        }

        public void setPatientInitial(String patientInitial) {
            this.patientInitial = patientInitial;
        }

        public String getPatientFirstName() {
            return patientFirstName;
        }

        public void setPatientFirstName(String patientFirstName) {
            this.patientFirstName = patientFirstName;
        }

        public String getPatientMiddleName() {
            return patientMiddleName;
        }

        public void setPatientMiddleName(String patientMiddleName) {
            this.patientMiddleName = patientMiddleName;
        }

        public String getPatientLastName() {
            return patientLastName;
        }

        public void setPatientLastName(String patientLastName) {
            this.patientLastName = patientLastName;
        }

        public String getPatientName() {
            return patientName;
        }

        public void setPatientName(String patientName) {
            this.patientName = patientName;
        }

        public String getPatientAliasName() {
            return patientAliasName;
        }

        public void setPatientAliasName(String patientAliasName) {
            this.patientAliasName = patientAliasName;
        }

        public String getGuardianInitial() {
            return guardianInitial;
        }

        public void setGuardianInitial(String guardianInitial) {
            this.guardianInitial = guardianInitial;
        }

        public String getGuardianName() {
            return guardianName;
        }

        public void setGuardianName(String guardianName) {
            this.guardianName = guardianName;
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

        public String getAreaText() {
            return areaText;
        }

        public void setAreaText(String areaText) {
            this.areaText = areaText;
        }

        public String getMobNo() {
            return mobNo;
        }

        public void setMobNo(String mobNo) {
            this.mobNo = mobNo;
        }

        public String getIdTypeText() {
            return idTypeText;
        }

        public void setIdTypeText(String idTypeText) {
            this.idTypeText = idTypeText;
        }

        public String getIdNo() {
            return idNo;
        }

        public void setIdNo(String idNo) {
            this.idNo = idNo;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getGenderText() {
            return genderText;
        }

        public void setGenderText(String genderText) {
            this.genderText = genderText;
        }

        public String getBloodGroupText() {
            return bloodGroupText;
        }

        public void setBloodGroupText(String bloodGroupText) {
            this.bloodGroupText = bloodGroupText;
        }

        public String getMaritalStatusText() {
            return maritalStatusText;
        }

        public void setMaritalStatusText(String maritalStatusText) {
            this.maritalStatusText = maritalStatusText;
        }

        public String getAgeYear() {
            return ageYear;
        }

        public void setAgeYear(String ageYear) {
            this.ageYear = ageYear;
        }

        public String getAgeMonth() {
            return ageMonth;
        }

        public void setAgeMonth(String ageMonth) {
            this.ageMonth = ageMonth;
        }

        public String getAgeDay() {
            return ageDay;
        }

        public void setAgeDay(String ageDay) {
            this.ageDay = ageDay;
        }

        public String getApprovedBy() {
            return approvedBy;
        }

        public void setApprovedBy(String approvedBy) {
            this.approvedBy = approvedBy;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getNationalityName() {
            return nationalityName;
        }

        public void setNationalityName(String nationalityName) {
            this.nationalityName = nationalityName;
        }

        public String getReligionText() {
            return religionText;
        }

        public void setReligionText(String religionText) {
            this.religionText = religionText;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getOccupation() {
            return occupation;
        }

        public void setOccupation(String occupation) {
            this.occupation = occupation;
        }

        public String getPinCode() {
            return pinCode;
        }

        public void setPinCode(String pinCode) {
            this.pinCode = pinCode;
        }

        public String getEmgContactName() {
            return emgContactName;
        }

        public void setEmgContactName(String emgContactName) {
            this.emgContactName = emgContactName;
        }

        public String getEmgContactMobile() {
            return emgContactMobile;
        }

        public void setEmgContactMobile(String emgContactMobile) {
            this.emgContactMobile = emgContactMobile;
        }

        public String getClassText() {
            return classText;
        }

        public void setClassText(String classText) {
            this.classText = classText;
        }

        public String getTempDOB() {
            return tempDOB;
        }

        public void setTempDOB(String tempDOB) {
            this.tempDOB = tempDOB;
        }

        public String getDisplayDOB() {
            return displayDOB;
        }

        public void setDisplayDOB(String displayDOB) {
            this.displayDOB = displayDOB;
        }

        public String getOrgText() {
            return orgText;
        }

        public void setOrgText(String orgText) {
            this.orgText = orgText;
        }

        public String getOrgShortName() {
            return orgShortName;
        }

        public void setOrgShortName(String orgShortName) {
            this.orgShortName = orgShortName;
        }

        public String getOrgPolcyNo() {
            return orgPolcyNo;
        }

        public void setOrgPolcyNo(String orgPolcyNo) {
            this.orgPolcyNo = orgPolcyNo;
        }

        public String getOrgHolderName() {
            return orgHolderName;
        }

        public void setOrgHolderName(String orgHolderName) {
            this.orgHolderName = orgHolderName;
        }

        public String getOrgRelationShip() {
            return orgRelationShip;
        }

        public void setOrgRelationShip(String orgRelationShip) {
            this.orgRelationShip = orgRelationShip;
        }

//        public String getShowDDUptoMonth() {
//            return showDDUptoMonth;
//        }
//
//        public void setShowDDUptoMonth(String showDDUptoMonth) {
//            this.showDDUptoMonth = showDDUptoMonth;
//        }
//
//        public String getShowMMUptoYear() {
//            return showMMUptoYear;
//        }
//
//        public void setShowMMUptoYear(String showMMUptoYear) {
//            this.showMMUptoYear = showMMUptoYear;
//        }
        public String getGuardianRelationText() {
            return guardianRelationText;
        }

        public void setGuardianRelationText(String guardianRelationText) {
            this.guardianRelationText = guardianRelationText;
        }

        public String getCastText() {
            return castText;
        }

        public void setCastText(String castText) {
            this.castText = castText;
        }

        public String getRelationName() {
            return relationName;
        }

        public void setRelationName(String relationName) {
            this.relationName = relationName;
        }

        public String getRankNo() {
            return rankNo;
        }

        public void setRankNo(String rankNo) {
            this.rankNo = rankNo;
        }

        public String getRecIdUHID() {
            return recIdUHID;
        }

        public void setRecIdUHID(String recIdUHID) {
            this.recIdUHID = recIdUHID;
        }

    }

    class Default_Mdl {

        private String showDDUptoMonth, showMMUptoYear, cityCode, nationalityCode, organizationCode, voucherTag, alphCaseAddress, alphCasePatient;
        private boolean religionMandatory = false, idTypeMandatory = false, patientInitialMandatory = false,
                guardianInitialMandatory = false, addressMandatory = false, cityMandatory = false, casteMandatory = false, maritalStatusMandatory = false,
                bloodGroupMandatory = false, emailMandatory = false, areaMandatory = false, guardianNameMandatory = false;

        public Default_Mdl() {
        }

        public String getShowDDUptoMonth() {
            return showDDUptoMonth;
        }

        public void setShowDDUptoMonth(String showDDUptoMonth) {
            this.showDDUptoMonth = showDDUptoMonth;
        }

        public String getShowMMUptoYear() {
            return showMMUptoYear;
        }

        public void setShowMMUptoYear(String showMMUptoYear) {
            this.showMMUptoYear = showMMUptoYear;
        }

        public String getCityCode() {
            return cityCode;
        }

        public void setCityCode(String cityCode) {
            this.cityCode = cityCode;
        }

        public String getNationalityCode() {
            return nationalityCode;
        }

        public void setNationalityCode(String nationalityCode) {
            this.nationalityCode = nationalityCode;
        }

        public String getAlphCaseAddress() {
            return alphCaseAddress;
        }

        public void setAlphCaseAddress(String alphCaseAddress) {
            this.alphCaseAddress = alphCaseAddress;
        }

        public String getAlphCasePatient() {
            return alphCasePatient;
        }

        public void setAlphCasePatient(String alphCasePatient) {
            this.alphCasePatient = alphCasePatient;
        }

        public boolean isReligionMandatory() {
            return religionMandatory;
        }

        public void setReligionMandatory(boolean religionMandatory) {
            this.religionMandatory = religionMandatory;
        }

        public boolean isIdTypeMandatory() {
            return idTypeMandatory;
        }

        public void setIdTypeMandatory(boolean idTypeMandatory) {
            this.idTypeMandatory = idTypeMandatory;
        }

        public boolean isAddressMandatory() {
            return addressMandatory;
        }

        public void setAddressMandatory(boolean addressMandatory) {
            this.addressMandatory = addressMandatory;
        }

        public boolean isCityMandatory() {
            return cityMandatory;
        }

        public void setCityMandatory(boolean cityMandatory) {
            this.cityMandatory = cityMandatory;
        }

        public boolean isGuardianInitialMandatory() {
            return guardianInitialMandatory;
        }

        public void setGuardianInitialMandatory(boolean guardianInitialMandatory) {
            this.guardianInitialMandatory = guardianInitialMandatory;
        }

        public boolean isCasteMandatory() {
            return casteMandatory;
        }

        public void setCasteMandatory(boolean casteMandatory) {
            this.casteMandatory = casteMandatory;
        }

        public boolean isMaritalStatusMandatory() {
            return maritalStatusMandatory;
        }

        public void setMaritalStatusMandatory(boolean maritalStatusMandatory) {
            this.maritalStatusMandatory = maritalStatusMandatory;
        }

        public boolean isBloodGroupMandatory() {
            return bloodGroupMandatory;
        }

        public void setBloodGroupMandatory(boolean bloodGroupMandatory) {
            this.bloodGroupMandatory = bloodGroupMandatory;
        }

        public boolean isEmailMandatory() {
            return emailMandatory;
        }

        public void setEmailMandatory(boolean emailMandatory) {
            this.emailMandatory = emailMandatory;
        }

        public String getOrganizationCode() {
            return organizationCode;
        }

        public void setOrganizationCode(String organizationCode) {
            this.organizationCode = organizationCode;
        }

        public String getVoucherTag() {
            return voucherTag;
        }

        public void setVoucherTag(String voucherTag) {
            this.voucherTag = voucherTag;
        }

        public boolean isPatientInitialMandatory() {
            return patientInitialMandatory;
        }

        public void setPatientInitialMandatory(boolean patientInitialMandatory) {
            this.patientInitialMandatory = patientInitialMandatory;
        }

        public boolean isAreaMandatory() {
            return areaMandatory;
        }

        public void setAreaMandatory(boolean areaMandatory) {
            this.areaMandatory = areaMandatory;
        }

        public boolean isGuardianNameMandatory() {
            return guardianNameMandatory;
        }

        public void setGuardianNameMandatory(boolean guardianNameMandatory) {
            this.guardianNameMandatory = guardianNameMandatory;
        }

    }

    private DmRSS rss;
    private DmHandelFile handelFileLog;
    private String lineNoDisplay = "";
    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String vCat = "'" + pvc.UHID + "'";
    private Default_Mdl dvm;
    private String[] currentDate;

    public UHID() {
    }

    @Override
    public ImportData_Mdl getImportDataModel() {
        ImportData_Mdl fileDetail = new ImportData_Mdl();
        CSVData node;
        List<CSVData> lstCSV = new ArrayList<CSVData>();

        node = new CSVData();
        node.setHeader("uhidNo");
        node.setSample("12345abcde");
        node.setInstruction("{mandatory;alphanumeric;20c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("patientInitial");
        node.setSample("Mr./Mrs./Ms./Shri./Dr.");
        node.setInstruction("{mandatory according to Enviro;alphanumeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("firstName");
        node.setSample("Keshav");
        node.setInstruction("{mandatory;alphanumeric;20c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("middleName");
        node.setSample("Kumar");
        node.setInstruction("{alphanumeric;14c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("lastName");
        node.setSample("Gupta");
        node.setInstruction("{alphanumeric;14c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("guardianInitial");
        node.setSample("Mr./Mrs./Ms./Shri./Dr.");
        node.setInstruction("{mandatory according to Enviro;alphanumeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("guardianName");
        node.setSample("Vipul Gupta");
        node.setInstruction("{mandatory;alphanumeric;50}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("guardianRelation");
        node.setSample("Self/Borther/Sister/Mother/Father/Wife/Husband/Son/Daughter/Grandson/Granddaughter/Grandmother/Grandfather");
        node.setInstruction("{alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("mobile");
        node.setSample("9876543210");
        node.setInstruction("{numeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("email");
        node.setSample("keshv@gmail.com");
        node.setInstruction("{mandatory according to Enviro;alphanumeric:correct emailID;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("idType");
        node.setSample("Ration Card");
        node.setInstruction("{mandatory according to Enviro;alphanumeric;100c;Aadhaar/Voter Id/BPL No./Ration Card}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("idNo");
        node.setSample("1232345678");
        node.setInstruction("{alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("caste");
        node.setSample("General");
        node.setInstruction("{mandatory according to Enviro;alphanumeric;100c;General/OBC/SC/ST}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("religion");
        node.setSample("hindu");
        node.setInstruction("{mandatory according to Enviro;alphanumeric;100c;Buddhist/Christian/Hindu/Jain/Muslim/Sikh}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("gender");
        node.setSample("Female");
        node.setInstruction("{mandatory;alphanumeric;100c;Female/Male/Unknown}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("maritalStatus");
        node.setSample("Separated");
        node.setInstruction("{mandatory according to Enviro;alphanumeric;100c;Separated/Unmarried/Divorced/Married/Widowed}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("dob");
        node.setSample("27-Oct-1987");
        node.setInstruction("{date;dd-MMM-yyyy;11c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("ageYY");
        node.setSample("12");
        node.setInstruction("{numeric}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("ageMM");
        node.setSample("01");
        node.setInstruction("{numeric}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("ageDD");
        node.setSample("10");
        node.setInstruction("{numeric}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("bloodGroup");
        node.setSample("A+");
        node.setInstruction("{mandatory according to Enviro;alphanumeric;100c;A+/A-/B+/B-/AB+/AB-/O+/O-}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("class");
        node.setSample("VIP");
        node.setInstruction("{alphanumeric;100c;VIP/VVIP/Democrat}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("address1");
        node.setSample("Mall Road");
        node.setInstruction("{mandatory according to Enviro;alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("address2");
        node.setSample("kanpur");
        node.setInstruction("{alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("area");
        node.setSample("Karachi Khana");
        node.setInstruction("{alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("pincode");
        node.setSample("208012");
        node.setInstruction("{numeric}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("city");
        node.setSample("kanpur");
        node.setInstruction("{alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("nationality");
        node.setSample("Indian");
        node.setInstruction("{alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("emergencyPersonName");
        node.setSample("Vipul Kumar");
        node.setInstruction("{alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("emergencyMobileNo");
        node.setSample("1234567896");
        node.setInstruction("{numeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("occupation");
        node.setSample("Teacher");
        node.setInstruction("{alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("remark");
        node.setSample("No Allergy");
        node.setInstruction("{alphanumeric;250c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("organization");
        node.setSample("Cash Patient");
        node.setInstruction("{alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("holderName");
        node.setSample("Sunil");
        node.setInstruction("{alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("policyNo");
        node.setSample("15483CUFM");
        node.setInstruction("{alphanumeric;50c}");
        lstCSV.add(node);
        node = new CSVData();
        node.setHeader("relationship");
        node.setSample("Father");
        node.setInstruction("{alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("rankNo");
        node.setSample("155");
        node.setInstruction("{alphanumeric;20c}");
        lstCSV.add(node);

        fileDetail.getHmCSV().addAll(lstCSV);
        fileDetail.setImportTo("UHID");
        fileDetail.setServerClassName(UHID.class.getName());

        return fileDetail;
    }

    @Override
    public DmImportStatus importCSV(DmRSSPrivate rss, ImportData_Mdl importData) {
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        UHID_Mdl mrTemp = new UHID_Mdl();
        DmCsvReader csvReader = null;
        File dataFile = null;
        boolean isFound;
        DmDynamicGridRow dynamicGridRow;
        String fieldValue, siteCode, sqlQuery;
        String[] csvFileFormat;
        Object objValue;
        int rtnStatus = 0, status = 0;
        DmImportStatus importStatus = null;

        dvm = new Default_Mdl();

        try {
            this.rss = rss;

            if (DmShared.xNull(importData.getLogFilePath()).trim().equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new Exception("Log file path can not be blank.");
            }
            handelFileLog = new DmHandelFile(importData.getLogFilePath(), true);
            currentDate = DmServer.getServerDateTime();
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));
            siteCode = DmServer.getSsnSiteCode(rss);

            /*====================================< Get Voucher Type >====================================*/
            sqlQuery = "SELECT VT.V_Type FROM voucher_type VT WHERE  VT.short_Name = " + vCat + " ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                dvm.setVoucherTag(DmShared.xNull(rsTemp.getString("V_Type")));
            }
            rsTemp.close();
            psTemp.close();
            /*====================================< Get Default Values >====================================*/
            sqlQuery = "select en.showDDUptoMonth, en.showMMUptoYear, en.defaultCity, en.isReligionMandatory, en.isIdTypeMandatory, en.isPatientInitialMandatory,"
                    + "         en.isAddressMandatory, en.isCityMandatory, en.isGuardianInitialMandatory, en.isCasteMandatory, "
                    + "         en.isMaritalStatusMandatory, en.isBloodGroupMandatory, en.isEmailMandatory, en.defaultNationality, "
                    + "         en.isPatientImageMandatory, en.isAreaMandatory, en.isGuardianNameMandatory , en.defaultNationality,"
                    + "         en.alphCase_Patient, en.alphaCase_Address "
                    + " from enviro en "
                    + " where en.site_Code= :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", siteCode);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                dvm.setAlphCaseAddress(DmShared.xNull(rsTemp.getString("alphaCase_Address")));
                dvm.setAlphCasePatient(DmShared.xNull(rsTemp.getString("alphCase_Patient")));
                dvm.setShowDDUptoMonth(DmShared.iNull(rsTemp.getString("showDDUptoMonth")).toString());
                dvm.setShowMMUptoYear(DmShared.iNull(rsTemp.getString("showMMUptoYear")).toString());
                dvm.setNationalityCode(DmShared.xNull(rsTemp.getString("defaultNationality")));
                dvm.setCityCode(DmShared.xNull(rsTemp.getString("defaultCity")));
                dvm.setReligionMandatory(DmShared.iNull(rsTemp.getString("isReligionMandatory")).intValue() == 1);
                dvm.setIdTypeMandatory(DmShared.iNull(rsTemp.getString("isIdTypeMandatory")).intValue() == 1);
                dvm.setPatientInitialMandatory(DmShared.iNull(rsTemp.getString("isPatientInitialMandatory")).intValue() == 1);
                dvm.setAddressMandatory(DmShared.iNull(rsTemp.getString("isAddressMandatory")).intValue() == 1);
                dvm.setCityMandatory(DmShared.iNull(rsTemp.getString("isCityMandatory")).intValue() == 1);
                dvm.setGuardianInitialMandatory(DmShared.iNull(rsTemp.getString("isGuardianInitialMandatory")).intValue() == 1);
                dvm.setCasteMandatory(DmShared.iNull(rsTemp.getString("isCasteMandatory")).intValue() == 1);
                dvm.setMaritalStatusMandatory(DmShared.iNull(rsTemp.getString("isMaritalStatusMandatory")).intValue() == 1);
                dvm.setBloodGroupMandatory(DmShared.iNull(rsTemp.getString("isBloodGroupMandatory")).intValue() == 1);
                dvm.setEmailMandatory(DmShared.iNull(rsTemp.getString("isEmailMandatory")).intValue() == 1);
                dvm.setGuardianNameMandatory(DmShared.iNull(rsTemp.getString("isGuardianNameMandatory")).intValue() == 1);
                dvm.setAreaMandatory(DmShared.iNull(rsTemp.getString("isAreaMandatory")).intValue() == 1);
            } else {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, "Enviro not set.");
            }
            rsTemp.close();
            psTemp.close();
            if (DmShared.xNull(dvm.getNationalityCode()).equalsIgnoreCase("")) {
//                handelFileLog.writeLine(lineNoDisplay + DmShared.xNull(mrTemp.getNationalityName()) + " not inserted.Nationality not found in enviro");
//                throw new DmException(0, "Nationality not found in enviro");
            }
            /*====================================< Get default Organization >====================================*/
            dvm.setOrganizationCode(DmServer.getCodeViaManualCode("CP", "shortname", "organizationMast", "subCode", cnTemp));
            if (DmShared.iNull(dvm.getOrganizationCode()).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, "Organization not found");
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

                // fill model and save data
                for (int i = 0; i < csvReader.getRows().size(); i++) {
                    lineNoDisplay = "Line " + String.valueOf(i + 1) + ": ";
                    dynamicGridRow = csvReader.getRows().get(i);
                    mrTemp = new UHID_Mdl();
                    isFound = true;

                    for (String headerName : csvFileFormat) {
                        objValue = dynamicGridRow.getCells().get(headerName.toLowerCase());
                        if (objValue == null) {
                            fieldValue = "";
                        } else {
                            fieldValue = String.valueOf(objValue);
                        }
                        if (headerName.trim().equalsIgnoreCase("uhidNo")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 20, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setRecIdUHID(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("patientInitial")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, dvm.isPatientInitialMandatory(), false, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPatientInitial(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("firstName")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 20, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPatientFirstName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("middleName")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 15, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPatientMiddleName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("lastName")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 15, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPatientLastName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("guardianInitial")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, dvm.isGuardianInitialMandatory(), false, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setGuardianInitial(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("guardianName")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, dvm.isGuardianNameMandatory(), false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setGuardianName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("guardianRelation")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setGuardianRelationText(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("mobile")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, true, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setMobNo(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("email")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, dvm.isEmailMandatory(), false, false, true, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setEmail(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("idType")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, dvm.isIdTypeMandatory(), false, false, false, false, false, 25, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setIdTypeText(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("idNo")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, dvm.isIdTypeMandatory(), false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setIdNo(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("caste")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, dvm.isCasteMandatory(), false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setCastText(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("religion")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, dvm.isReligionMandatory(), false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setReligionText(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("gender")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setGenderText(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("maritalStatus")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, dvm.isMaritalStatusMandatory(), false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setMaritalStatusText(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("dob")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, true, false, 11, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setDob(DmShared.dNull(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("ageYY")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 3, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setAgeYear(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("ageMM")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 3, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setAgeMonth(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("ageDD")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 3, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setAgeDay(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("bloodGroup")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, dvm.isBloodGroupMandatory(), false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setBloodGroupText(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("class")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setClassText(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("address1")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, dvm.isAddressMandatory(), false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setAddress1(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("address2")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setAddress2(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("area")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, dvm.isAreaMandatory(), false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setAreaText(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("pincode")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 6, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPinCode(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("city")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, dvm.isCityMandatory(), false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setCity(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("nationality")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setNationalityName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("emergencyPersonName")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setEmgContactName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("emergencyMobileNo")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 35, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setEmgContactMobile(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("occupation")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setOccupation(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("remark")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 250, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setRemark(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("organization")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setOrgText(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("holderName")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setOrgHolderName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("policyNo")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setOrgPolcyNo(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("relationship")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setOrgRelationShip(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("rankNo")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 20, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setRankNo(fieldValue);
                            }
                        }
                    }
                    if (isFound) {
                        String pName = DmShared.xNull(mrTemp.getPatientFirstName()) + " " + DmShared.xNull(mrTemp.getPatientMiddleName()) + " "
                                + DmShared.xNull(mrTemp.getPatientLastName());

                        mrTemp.setPatientName(DmShared.removeSpace(pName));
                        mrTemp.setPatientMiddleName(DmShared.removeSpace(DmShared.xNull(mrTemp.getPatientMiddleName()).toUpperCase()));
                        mrTemp.setPatientLastName(DmShared.removeSpace(DmShared.xNull(mrTemp.getPatientLastName()).toUpperCase()));
                        mrTemp.setGuardianName(DmShared.removeSpace(DmShared.xNull(mrTemp.getGuardianName()).toUpperCase()));
                        mrTemp.setPatientAliasName(DmShared.removeSpace(DmShared.xNull(mrTemp.getPatientAliasName()).toUpperCase()));
                        //========================================== SET PATIENT DETAIL ACC. TO ENVIRO =======================================
                        if (dvm.getAlphCasePatient().equalsIgnoreCase("U")) {
                            mrTemp.setPatientName(pName.toUpperCase());
                            mrTemp.setPatientFirstName(DmShared.xNull(mrTemp.getPatientFirstName()).toUpperCase());
                            mrTemp.setPatientMiddleName(DmShared.xNull(mrTemp.getPatientMiddleName()).toUpperCase());
                            mrTemp.setPatientLastName(DmShared.xNull(mrTemp.getPatientLastName()).toUpperCase());
                            mrTemp.setGuardianName(DmShared.xNull(mrTemp.getGuardianName()).toUpperCase());
                            mrTemp.setPatientAliasName(DmShared.xNull(mrTemp.getPatientAliasName()).toUpperCase());
                        } else if (dvm.getAlphCasePatient().equalsIgnoreCase("L")) {
                            mrTemp.setPatientName(pName.toLowerCase());
                            mrTemp.setPatientFirstName(DmShared.xNull(mrTemp.getPatientFirstName()).toLowerCase());
                            mrTemp.setPatientMiddleName(DmShared.xNull(mrTemp.getPatientMiddleName()).toLowerCase());
                            mrTemp.setPatientLastName(DmShared.xNull(mrTemp.getPatientLastName()).toLowerCase());
                            mrTemp.setGuardianName(DmShared.xNull(mrTemp.getGuardianName()).toLowerCase());
                            mrTemp.setPatientAliasName(DmShared.xNull(mrTemp.getPatientAliasName()).toLowerCase());
                        } else if (dvm.getAlphCasePatient().equalsIgnoreCase("P")) {
                            mrTemp.setPatientName(DmShared.properCase(pName));
                            mrTemp.setPatientFirstName(DmShared.properCase(mrTemp.getPatientFirstName()));
                            mrTemp.setPatientMiddleName(DmShared.properCase(mrTemp.getPatientMiddleName()));
                            mrTemp.setPatientLastName(DmShared.properCase(mrTemp.getPatientLastName()));
                            mrTemp.setGuardianName(DmShared.properCase(mrTemp.getGuardianName()));
                            mrTemp.setPatientAliasName(DmShared.properCase(mrTemp.getPatientAliasName()));
                        } else if (dvm.getAlphCasePatient().equalsIgnoreCase("s")) {
                            mrTemp.setPatientName(DmShared.sentenceCase(pName));
                            mrTemp.setPatientFirstName(DmShared.sentenceCase(mrTemp.getPatientFirstName()));
                            mrTemp.setPatientMiddleName(DmShared.sentenceCase(mrTemp.getPatientMiddleName()));
                            mrTemp.setPatientLastName(DmShared.sentenceCase(mrTemp.getPatientLastName()));
                            mrTemp.setGuardianName(DmShared.sentenceCase(mrTemp.getGuardianName()));
                            mrTemp.setPatientAliasName(DmShared.sentenceCase(mrTemp.getPatientAliasName()));
                        }
                        //========================================== END ============================================================
                        mrTemp.setAddress1(DmShared.removeSpace(DmShared.xNull(mrTemp.getAddress1()).toUpperCase()));
                        mrTemp.setAddress2(DmShared.removeSpace(DmShared.xNull(mrTemp.getAddress2()).toUpperCase()));
                        //========================================== SET ADDRESS ACC. TO ENVIRO======================================
                        if (dvm.getAlphCaseAddress().equalsIgnoreCase("U")) {
                            mrTemp.setAddress1(DmShared.xNull(mrTemp.getAddress1()).toUpperCase());
                            mrTemp.setAddress2(DmShared.xNull(mrTemp.getAddress2()).toUpperCase());
                        } else if (dvm.getAlphCaseAddress().equalsIgnoreCase("L")) {
                            mrTemp.setAddress1(DmShared.xNull(mrTemp.getAddress1()).toLowerCase());
                            mrTemp.setAddress2(DmShared.xNull(mrTemp.getAddress2()).toLowerCase());
                        } else if (dvm.getAlphCaseAddress().equalsIgnoreCase("P")) {
                            mrTemp.setAddress1(DmShared.properCase(DmShared.xNull(mrTemp.getAddress1())));
                            mrTemp.setAddress2(DmShared.properCase(DmShared.xNull(mrTemp.getAddress2())));
                        } else if (dvm.getAlphCaseAddress().equalsIgnoreCase("S")) {
                            mrTemp.setAddress1(DmShared.sentenceCase(DmShared.xNull(mrTemp.getAddress1())));
                            mrTemp.setAddress2(DmShared.sentenceCase(DmShared.xNull(mrTemp.getAddress2())));
                        }

                        if (DmShared.dNull(mrTemp.getDob()).equalsIgnoreCase("") && DmShared.iNull(mrTemp.getAgeDay()).intValue() <= 0
                                && DmShared.iNull(mrTemp.getAgeMonth()).intValue() <= 0 && DmShared.iNull(mrTemp.getAgeYear()).intValue() <= 0) {
                            isFound = false;
                            status = DmImportStatus.ERROR.getId();
                            handelFileLog.writeLine(lineNoDisplay + " age is mandatory.");
                        } else if (DmShared.dNull(mrTemp.getDob()).equalsIgnoreCase("") && !(DmShared.iNull(mrTemp.getAgeDay()).intValue() == 0
                                && DmShared.iNull(mrTemp.getAgeMonth()).intValue() == 0 && DmShared.iNull(mrTemp.getAgeYear()).intValue() == 0)) {

                            if (DmShared.iNull(mrTemp.getAgeDay()).intValue() <= 0 && DmShared.iNull(mrTemp.getAgeDay()).intValue() > 31
                                    && DmShared.iNull(mrTemp.getAgeMonth()).intValue() <= 0 && DmShared.iNull(mrTemp.getAgeYear()).intValue() <= 0) {
                                isFound = false;
                                status = DmImportStatus.ERROR.getId();
                                handelFileLog.writeLine(lineNoDisplay + " day should be greater than 0 and less than 31.");
                            }
                            if (DmShared.iNull(mrTemp.getAgeMonth()).intValue() <= 0 && DmShared.iNull(mrTemp.getAgeDay()).intValue() > 12
                                    && DmShared.iNull(mrTemp.getAgeDay()).intValue() <= 0 && DmShared.iNull(mrTemp.getAgeYear()).intValue() <= 0) {
                                isFound = false;
                                status = DmImportStatus.ERROR.getId();
                                handelFileLog.writeLine(lineNoDisplay + " month should be greater than 0 and less than 12.");
                            }

                        }

                        //========================================== END ============================================================
                        if (isFound) {
                            status = saveUHID(mrTemp);
                        }
                        if (status > rtnStatus) {
                            rtnStatus = status;
                        }
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
                if (cnTemp != null) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return importStatus;
    }

    private int saveUHID(UHID_Mdl mrTemp) {
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, patientInitialCode = "", guardianInitial = "", idCode = "", userName = "", siteCode = "", casteCode = "", bgCode = "",
                maritalCode = "", genderCode = "", religionCode = "", classCode = "", nationalityCode = "", voucherPrefix, cityCode = "",
                organizationCode = "", areaCode = "", tempDOB = "", occupationCode = "";
        int rtnStatus = 0, status = 0;
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));
            spBegin = new DmSavePoint(cnTemp);
            userName = DmServer.getSsnUserName(rss);
            siteCode = DmServer.getSsnSiteCode(rss);
            voucherPrefix = DmServer.getSsnVoucherPrefix(rss);

            //========================================== GET DOCID & RECID ============================================================
            if (DmShared.xNull(mrTemp.getUHIDDocId()).equalsIgnoreCase("")) {
                mrTemp.setUHIDDocId(DmServer.getDocId(DmShared.xNull(dvm.getVoucherTag()), voucherPrefix, cnTemp, rss));
            }
            sqlQuery = " select recId from patientUHID where recId = :recId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("recId", mrTemp.getRecIdUHID());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                status = DmImportStatus.WARNING.getId();
                throw new DmException(0, "UHID No. already exists.");
            }
            rsTemp.close();
            psTemp.close();
            //========================================== END ==========================================================================

            //========================================== CHECK IF DOCID IS NULL =======================================================
            if (mrTemp.getUHIDDocId().equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new UnsupportedOperationException("Invalid key.");
            }
            //========================================== END ==========================================================================

            //========================================== GET PATIENT INITIAL===========================================================
            patientInitialCode = DmServer.getCodeViaManualCode(mrTemp.getPatientInitial(), "name", "initialmast", "code", cnTemp);
            if (dvm.isPatientInitialMandatory()) {
                if (DmShared.xNull(patientInitialCode).trim().equalsIgnoreCase("")) {
                    status = DmImportStatus.ERROR.getId();
                    throw new DmException(0, mrTemp.getPatientInitial() + "Patient Initial not found");
                }
            } else {
                if (!DmShared.xNull(mrTemp.getPatientInitial()).trim().equalsIgnoreCase("")) {
                    {
                        if (DmShared.xNull(patientInitialCode).trim().equalsIgnoreCase("")) {
                            status = DmImportStatus.WARNING.getId();
                            handelFileLog.writeLine(lineNoDisplay + mrTemp.getPatientInitial() + " not inserted.Patient Initial not found in help");
                        }
                    }
                }
            }
            //========================================== END ==========================================================================

            //========================================== GET GUARDIAN INITIAL =========================================================
            guardianInitial = DmServer.getCodeViaManualCode(mrTemp.getGuardianInitial(), "name", "initialmast", "code", cnTemp);
            if (dvm.isGuardianInitialMandatory()) {
                if (DmShared.xNull(guardianInitial).trim().equalsIgnoreCase("")) {
                    status = DmImportStatus.ERROR.getId();
                    throw new DmException(0, mrTemp.getGuardianInitial() + "Guardian Initial not found");
                }
            } else {
                if (!DmShared.xNull(mrTemp.getGuardianInitial()).trim().equalsIgnoreCase("")) {
                    if (DmShared.xNull(guardianInitial).trim().equalsIgnoreCase("")) {
                        status = DmImportStatus.WARNING.getId();
                        handelFileLog.writeLine(lineNoDisplay + mrTemp.getGuardianInitial() + " not inserted.Guardian Initial not found in help");
                    }
                }
            }
            //========================================== END ==========================================================================

            //========================================== GET ID CODE ==================================================================
            sqlQuery = "SELECT CODE  FROM hl7Mast where tableName='identifiertype' and isActive=1 and name=:name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getIdTypeText()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                idCode = DmShared.xNull(rsTemp.getString("CODE"));
            }
            rsTemp.close();
            psTemp.close();
            if (dvm.isIdTypeMandatory()) {
                if (DmShared.iNull(idCode).intValue() == 0) {
                    status = DmImportStatus.ERROR.getId();
                    throw new DmException(0, mrTemp.getIdTypeText() + "id type not found");
                }
            } else {
                if (!DmShared.xNull(mrTemp.getIdTypeText()).trim().equalsIgnoreCase("")) {
                    if (DmShared.iNull(idCode).intValue() == 0) {
                        status = DmImportStatus.WARNING.getId();
                        handelFileLog.writeLine(lineNoDisplay + mrTemp.getIdTypeText() + " not inserted.Id type not found in help");
                    }
                }
            }
            //========================================== END ==========================================================================

            //  ========================================== CHECK FOR AADHAAR NO RANGE =================================================
            if (DmShared.xNull(mrTemp.getIdTypeText()).trim().equalsIgnoreCase("Aadhaar")) {
                mrTemp.setIdNo(DmShared.xNull(mrTemp.getIdNo()).replaceAll(" ", ""));
                if (DmShared.xNull(mrTemp.getIdNo()).trim().length() != 12) {
                    status = DmImportStatus.WARNING.getId();
                    throw new DmException(0, "Aadhar No. should be 12 digit.");
                }
                try {
                    long l = Long.parseLong(DmShared.xNull(mrTemp.getIdNo()));
                } catch (NumberFormatException nfe) {
                    status = DmImportStatus.ERROR.getId();
                    throw new DmException(0, "Aadhar No. should be numeric.");
                }
            }
            //========================================== END ==========================================================================

            //========================================== GET CASTE CODE ===============================================================
            sqlQuery = "SELECT CODE  FROM hl7Mast where tableName='castName' and isActive=1 and name=:name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getCastText()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                casteCode = DmShared.xNull(rsTemp.getString("CODE"));
            }
            rsTemp.close();
            psTemp.close();
            if (dvm.isCasteMandatory()) {
                if (DmShared.iNull(casteCode).intValue() == 0) {
                    status = DmImportStatus.ERROR.getId();
                    throw new DmException(0, mrTemp.getCastText() + "caste not found");
                }
            } else {
                if (!DmShared.xNull(mrTemp.getCastText()).trim().equalsIgnoreCase("")) {
                    if (DmShared.iNull(casteCode).intValue() == 0) {
                        status = DmImportStatus.WARNING.getId();
                        handelFileLog.writeLine(lineNoDisplay + mrTemp.getCastText() + " not inserted.Caste is not in Hl7Mast");
                    }
                }
            }
            //========================================== END ==========================================================================

            //========================================== GET BLOODGROUP CODE ==========================================================
            sqlQuery = "SELECT CODE FROM hl7Mast where tableName='bloodGroup' and isActive=1 and name=:name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getBloodGroupText()));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                bgCode = DmShared.xNull(rsTemp.getString("CODE"));
            }
            rsTemp.close();
            psTemp.close();
            if (dvm.isBloodGroupMandatory()) {
                if (DmShared.iNull(bgCode).intValue() == 0) {
                    status = DmImportStatus.ERROR.getId();
                    throw new DmException(0, mrTemp.getBloodGroupText() + "blood group not found");
                }
            } else {
                if (!DmShared.xNull(mrTemp.getBloodGroupText()).trim().equalsIgnoreCase("")) {
                    if (DmShared.iNull(bgCode).intValue() == 0) {
                        status = DmImportStatus.WARNING.getId();
                        handelFileLog.writeLine(lineNoDisplay + mrTemp.getBloodGroupText() + " not inserted.Blood Group is not in Hl7Mast");
                    }
                }
            }
            //========================================== END ==========================================================================

            //========================================== GET MARITAL STATUS CODE ======================================================
            sqlQuery = "SELECT CODE  FROM hl7Mast where tableName='maritalStatus' and isActive=1 and name=:name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getMaritalStatusText()));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                maritalCode = DmShared.xNull(rsTemp.getString("CODE"));
            }
            rsTemp.close();
            psTemp.close();
            if (dvm.isMaritalStatusMandatory()) {
                if (DmShared.iNull(maritalCode).intValue() == 0) {
                    status = DmImportStatus.ERROR.getId();
                    throw new DmException(0, mrTemp.getMaritalStatusText() + "marital status not found");
                }
            } else {
                if (!DmShared.xNull(mrTemp.getMaritalStatusText()).trim().equalsIgnoreCase("")) {
                    if (DmShared.iNull(maritalCode).intValue() == 0) {
                        status = DmImportStatus.WARNING.getId();
                        handelFileLog.writeLine(lineNoDisplay + mrTemp.getMaritalStatusText() + " not inserted.Marital Status is not in Hl7Mast");
                    }
                }
            }
            //========================================== END ==========================================================================

            //========================================== GET GENDER CODE ==============================================================
            sqlQuery = "SELECT CODE FROM hl7Mast where tableName='administrativeSex' and isActive=1 and name=:name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getGenderText()));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                genderCode = DmShared.xNull(rsTemp.getString("CODE"));
            }
            rsTemp.close();
            psTemp.close();
            if (DmShared.iNull(genderCode).intValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, mrTemp.getGenderText() + "gender not found");
            }
            //========================================== END ==========================================================================

            //========================================== GET RELIGION CODE ============================================================
            sqlQuery = "SELECT CODE  FROM hl7Mast where tableName='religion' and isActive=1 and name=:name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getReligionText()));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                religionCode = DmShared.xNull(rsTemp.getString("CODE"));
            }
            rsTemp.close();
            psTemp.close();
            if (dvm.isReligionMandatory()) {
                if (DmShared.iNull(religionCode).intValue() == 0) {
                    status = DmImportStatus.ERROR.getId();
                    throw new DmException(0, mrTemp.getReligionText() + "religion not found");
                }
            } else {
                if (!DmShared.xNull(mrTemp.getReligionText()).trim().equalsIgnoreCase("")) {
                    if (DmShared.iNull(religionCode).intValue() == 0) {
                        status = DmImportStatus.WARNING.getId();
                        handelFileLog.writeLine(lineNoDisplay + mrTemp.getReligionText() + " not inserted.Religion is not in Hl7Mast");
                    }
                }
            }
            //========================================== END ==========================================================================

            //========================================== GET NATIONALITY CODE =========================================================
            nationalityCode = DmServer.getCodeViaManualCode(mrTemp.getNationalityName(), "name", "nationalityMast", "code", cnTemp);
            //========================================== END ==========================================================================

            //========================================== GET CLASS CODE ===============================================================
            sqlQuery = "SELECT CODE  FROM hl7Mast where tableName='vipType' and isActive=1 and name=:name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getClassText()));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                classCode = DmShared.xNull(rsTemp.getString("CODE"));
            }
            rsTemp.close();
            psTemp.close();
            if (!DmShared.xNull(mrTemp.getClassText()).equalsIgnoreCase("")) {
                if (DmShared.iNull(classCode).intValue() == 0) {
                    status = DmImportStatus.WARNING.getId();
                    handelFileLog.writeLine(lineNoDisplay + mrTemp.getClassText() + " not inserted.class is not in Hl7Mast");
                }
            }
            //========================================== END ==========================================================================

            //========================================== GET ORGANIZATION CODE ========================================================
            sqlQuery = "select om.subCode "
                    + " from OrganizationMast om "
                    + " left join subGroup sg on om.subCode=sg.subCode "
                    + " where sg.name=:name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getOrgText()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                organizationCode = DmShared.xNull(rsTemp.getString("subCode"));
            }
            rsTemp.close();
            psTemp.close();
            //========================================== END ==========================================================================

            //========================================== GET AREA CODE ================================================================
            areaCode = DmServer.getCodeViaManualCode(mrTemp.getAreaText(), "name", "zonemast", "code", cnTemp);
            if (DmShared.xNull(areaCode).trim().equalsIgnoreCase("") && dvm.isAreaMandatory()) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, mrTemp.getAreaText() + "area not found");
            }
            //========================================== END ==========================================================================

            //========================================== GET OCCUPATION CODE ================================================================
            occupationCode = DmServer.getCodeViaManualCode(mrTemp.getOccupation(), "name", "occupationMast", "code", cnTemp);
            if (DmShared.xNull(occupationCode).trim().equalsIgnoreCase("")) {
                status = DmImportStatus.WARNING.getId();
                handelFileLog.writeLine(lineNoDisplay + mrTemp.getOccupation() + " not inserted. Occupation not found in Occupation Master");
            }
            //========================================== END ==========================================================================

            //========================================== GET CITY CODE ================================================================
            cityCode = DmServer.getCodeViaManualCode(mrTemp.getCity(), "cityName", "city", "cityCode", cnTemp);
            if (DmShared.iNull(cityCode).intValue() == 0) {
                if (dvm.isCityMandatory()) {
                    status = DmImportStatus.ERROR.getId();
                    throw new DmException(0, mrTemp.getCity() + "city not found");
                }
            } else {
                if (!DmShared.xNull(mrTemp.getCity()).equalsIgnoreCase("")) {
                    if (DmShared.iNull(cityCode).intValue() == 0) {
                        status = DmImportStatus.WARNING.getId();
                        handelFileLog.writeLine(lineNoDisplay + mrTemp.getCity() + " not inserted. city not found in city Master");
                    }
                }
            }
            //========================================== END ==========================================================================

            //========================================== CHECK GUARDIAN RELATION ======================================================
            if (!DmShared.xNull(mrTemp.getGuardianRelationText()).equalsIgnoreCase("")) {
                if (!DmShared.xNull(mrTemp.getGuardianRelationText()).equalsIgnoreCase("Self")
                        && !DmShared.xNull(mrTemp.getGuardianRelationText()).equalsIgnoreCase("Brother")
                        && !DmShared.xNull(mrTemp.getGuardianRelationText()).equalsIgnoreCase("Sister")
                        && !DmShared.xNull(mrTemp.getGuardianRelationText()).equalsIgnoreCase("Mother")
                        && !DmShared.xNull(mrTemp.getGuardianRelationText()).equalsIgnoreCase("Father")
                        && !DmShared.xNull(mrTemp.getGuardianRelationText()).equalsIgnoreCase("Wife")
                        && !DmShared.xNull(mrTemp.getGuardianRelationText()).equalsIgnoreCase("Husband")
                        && !DmShared.xNull(mrTemp.getGuardianRelationText()).equalsIgnoreCase("Daughter")
                        && !DmShared.xNull(mrTemp.getGuardianRelationText()).equalsIgnoreCase("Grandson")
                        && !DmShared.xNull(mrTemp.getGuardianRelationText()).equalsIgnoreCase("Granddaughter")
                        && !DmShared.xNull(mrTemp.getGuardianRelationText()).equalsIgnoreCase("Grandmother")
                        && !DmShared.xNull(mrTemp.getGuardianRelationText()).equalsIgnoreCase("Grandfather")
                        && !DmShared.xNull(mrTemp.getGuardianRelationText()).equalsIgnoreCase("Son")) {
                    status = DmImportStatus.WARNING.getId();
                    handelFileLog.writeLine(lineNoDisplay + mrTemp.getGuardianRelationText() + " not inserted.Guardian Relation not found");
                }
            }
            //========================================== END ===========================================================================

            //========================================== CHECK EMPANELMENT RELATIONSHIP ================================================
            if (!DmShared.xNull(mrTemp.getOrgRelationShip()).equalsIgnoreCase("")) {
                if (!DmShared.xNull(mrTemp.getOrgRelationShip()).equalsIgnoreCase("Self")
                        && !DmShared.xNull(mrTemp.getOrgRelationShip()).equalsIgnoreCase("Brother")
                        && !DmShared.xNull(mrTemp.getOrgRelationShip()).equalsIgnoreCase("Sister")
                        && !DmShared.xNull(mrTemp.getOrgRelationShip()).equalsIgnoreCase("Mother")
                        && !DmShared.xNull(mrTemp.getOrgRelationShip()).equalsIgnoreCase("Father")
                        && !DmShared.xNull(mrTemp.getOrgRelationShip()).equalsIgnoreCase("Wife")
                        && !DmShared.xNull(mrTemp.getOrgRelationShip()).equalsIgnoreCase("Husband")
                        && !DmShared.xNull(mrTemp.getOrgRelationShip()).equalsIgnoreCase("Daughter")
                        && !DmShared.xNull(mrTemp.getOrgRelationShip()).equalsIgnoreCase("Grandson")
                        && !DmShared.xNull(mrTemp.getOrgRelationShip()).equalsIgnoreCase("Granddaughter")
                        && !DmShared.xNull(mrTemp.getOrgRelationShip()).equalsIgnoreCase("Grandmother")
                        && !DmShared.xNull(mrTemp.getOrgRelationShip()).equalsIgnoreCase("Grandfather")
                        && !DmShared.xNull(mrTemp.getOrgRelationShip()).equalsIgnoreCase("Son")) {
                    status = DmImportStatus.WARNING.getId();
                    handelFileLog.writeLine(lineNoDisplay + mrTemp.getOrgRelationShip() + " not inserted.Organization Relationship not found");
                }
            }
            //========================================== END ==========================================================================

            //========================================== SET DATE OF BIRTH ============================================================
            if (DmShared.xNull(mrTemp.getDob()).equalsIgnoreCase("")) {
                tempDOB = STServer.getTempDOB(DmShared.iNull(mrTemp.getAgeDay()).intValue(), DmShared.iNull(mrTemp.getAgeMonth()).intValue(), DmShared.iNull(mrTemp.getAgeYear()).intValue(), currentDate[0]);
            } else {
                tempDOB = DmShared.dNull(mrTemp.getDob());
            }
            mrTemp.setDisplayDOB(STServer.getAgeDisplay(tempDOB, currentDate[0], DmShared.iNull(dvm.getShowDDUptoMonth()).intValue(), DmShared.iNull(dvm.getShowMMUptoYear()).intValue()));
            //========================================== END ============================================================================

            //========================================== INSERT INTO PATIENT UHID =======================================================================
            sqlQuery = "INSERT INTO patientUHID (docId, v_Type, v_No, recId, v_Date, v_Prefix, patientInitial, patientName, guardianInitial, guardianName, "
                    + " address1, address2, zoneCode, mobile, eMail, dob, dobDisplay, dobTemp, u_EntDt, site_Code, "
                    + " preparedBy, approvedBy, transferDt, idNo, occupationCode, remark, cityCode, patientAliasName, patientNameSearch, "
                    + "  pinCode, emgyContactPerson, emgyMobile, patientFirstName, patientMiddleName, patientLastName, guardianNameSearch, "
                    + " organizationCode, orgPolicyNo, orgHolderName, orgRelationShip, guardianRelationship, rankNo, recIdPrefix)  "
                    + " VALUES (:docId, :v_Type, :v_No, :recId, :v_Date, :v_Prefix, :patientInitial, :patientName, :guardianInitial, :guardianName, :address1, "
                    + " :address2, :zoneCode, :mobile, :eMail, :dob, :dobDisplay, :dobTemp, :uEntDt, :siteCode, "
                    + " :userName, :approvedBy, NULL,:idNo,:occupationCode,:remark,:cityCode,:patientAliasName, :patientNameSearch, "
                    + " :pinCode, :emgyContactPerson, :emgyMobile, :patientFirstName, :patientMiddleName, :patientLastName, :guardianNameSearch, "
                    + " :organizationCode, :orgPolicyNo, :orgHolderName, :orgRelationShip, :guardianRelationship, :rankNo, :recIdPrefix)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTemp.getUHIDDocId()).toString());
            psTemp.setString("recId", mrTemp.getRecIdUHID());
            psTemp.setString("recIdPrefix", DmShared.foriegnKey(""));
            psTemp.setString("v_No", DmShared.isNumeric(mrTemp.getRecIdUHID()) ? DmShared.iNull(mrTemp.getRecIdUHID()).toString() : "0");
            psTemp.setString("userName", userName);
            psTemp.setString("siteCode", siteCode);
            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            psTemp.setString("v_Type", dvm.getVoucherTag());
            psTemp.setDate("v_Date", DmServer.convertDate(currentDate[0]));
            psTemp.setString("v_Prefix", voucherPrefix);
            psTemp.setString("patientInitial", mrTemp.getPatientInitial());
            psTemp.setString("patientName", DmShared.xNull(mrTemp.getPatientName()));
            if ((DmShared.xNull(mrTemp.getPatientAliasName()).equalsIgnoreCase(""))
                    || (DmShared.xNull(classCode).equalsIgnoreCase(""))) {
                psTemp.setString("patientAliasName", DmShared.xNull(mrTemp.getPatientName()));
            } else {
                psTemp.setString("patientAliasName", DmShared.xNull(mrTemp.getPatientAliasName()));
            }
            psTemp.setString("patientFirstName", DmShared.xNull(mrTemp.getPatientFirstName()));
            psTemp.setString("patientMiddleName", DmShared.xNull(mrTemp.getPatientMiddleName()));
            psTemp.setString("patientLastName", DmShared.xNull(mrTemp.getPatientLastName()));
            psTemp.setString("guardianInitial", DmShared.xNull(mrTemp.getGuardianInitial()));
            psTemp.setString("guardianName", mrTemp.getGuardianName());
            psTemp.setString("patientNameSearch", DmShared.xNull(STShared.getOnlyAlphabets(DmShared.xNull(mrTemp.getPatientName()))));
            psTemp.setString("guardianNameSearch", DmShared.xNull(STShared.getOnlyAlphabets(DmShared.xNull(mrTemp.getGuardianName()))));
            psTemp.setString("address1", DmShared.xNull(mrTemp.getAddress1()));
            psTemp.setString("address2", DmShared.xNull(mrTemp.getAddress2()));
            psTemp.setString("zoneCode", DmShared.foriegnKey(areaCode));
            psTemp.setString("mobile", mrTemp.getMobNo());
            psTemp.setString("eMail", DmShared.xNull(mrTemp.getEmail()));
            psTemp.setString("guardianRelationship", DmShared.xNull(mrTemp.getGuardianRelationText()));
            psTemp.setString("approvedBy", (DmServer.getSsnUserName(rss)));
            psTemp.setDate("dob", DmServer.convertDate(mrTemp.getDob()));
            psTemp.setString("idNo", mrTemp.getIdNo());
            psTemp.setString("occupationCode", DmShared.foriegnKey(occupationCode));
            psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
            psTemp.setString("cityCode", DmShared.foriegnKey(cityCode));
            if (DmShared.iNull(organizationCode).longValue() > 0) {
                psTemp.setString("organizationCode", DmShared.foriegnKey(organizationCode));
            } else {
                psTemp.setString("organizationCode", DmShared.foriegnKey(dvm.getOrganizationCode()));
            }
            psTemp.setString("orgPolicyNo", DmShared.xNull(mrTemp.getOrgPolcyNo()));
            psTemp.setString("orgHolderName", DmShared.xNull(mrTemp.getOrgHolderName()));
            psTemp.setString("orgRelationShip", DmShared.xNull(mrTemp.getOrgRelationShip()));
            psTemp.setString("pinCode", DmShared.xNull(mrTemp.getPinCode()));
            psTemp.setString("emgyContactPerson", DmShared.xNull(mrTemp.getEmgContactName()));
            psTemp.setString("emgyMobile", DmShared.xNull(mrTemp.getEmgContactMobile()));
            psTemp.setString("dobDisplay", DmShared.xNull(mrTemp.getDisplayDOB()));
            psTemp.setDate("dobTemp", DmServer.convertDate(tempDOB));
            psTemp.setString("rankNo", DmShared.xNull(mrTemp.getRankNo()));
            psTemp.executeUpdate();
            psTemp.close();
            //========================================== END ==========================================================================

            //====================================< INSERT INTO PATIENTUHID2 >=========================================================
            sqlQuery = "INSERT INTO patientuhid2 (uhid, maritalStatusCode, genderCode, religionCode, idTypeCode, vipTypeCode, "
                    + " bloodGroupCode,castCode, nationalityCode)"
                    + " VALUES (:uhid, :maritalStatusCode, :genderCode, :religionCode, :idTypeCode, :vipTypeCode,"
                    + " :bloodGroupCode,:castCode, :nationalityCode) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("uhid", DmShared.iNull(mrTemp.getUHIDDocId()).toString());
            psTemp.setString("maritalStatusCode", DmShared.foriegnKey(maritalCode));
            psTemp.setString("genderCode", DmShared.foriegnKey(genderCode));
            psTemp.setString("religionCode", DmShared.foriegnKey(religionCode));
            psTemp.setString("idTypeCode", DmShared.foriegnKey(idCode));
            psTemp.setString("vipTypeCode", DmShared.foriegnKey(classCode));
            psTemp.setString("bloodGroupCode", DmShared.foriegnKey(bgCode));
            psTemp.setString("castCode", DmShared.foriegnKey(casteCode));
            if (DmShared.iNull(nationalityCode).intValue() > 0) {
                psTemp.setString("nationalityCode", DmShared.foriegnKey(nationalityCode));
            } else {
                psTemp.setString("nationalityCode", DmShared.foriegnKey(dvm.getNationalityCode()));
            }
            psTemp.executeUpdate();
            psTemp.close();
            //========================================== END ==========================================================================

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
