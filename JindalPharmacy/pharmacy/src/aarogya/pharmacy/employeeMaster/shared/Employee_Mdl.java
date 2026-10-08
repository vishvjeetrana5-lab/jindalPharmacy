package aarogya.pharmacy.employeeMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Employee_Mdl extends KeyField implements Serializable, IsSerializable {

    private String employeeID;
    private String joinDate;
    private String accountGroupTag;
    private String accountGroupText;
    private String accountGroupDesc;
    private String address1;
    private String address2;
    private String cityTag;
    private String cityText;
    private String countryTag;
    private String countryText;
    private String pinCode;
    private String paddress1;            //p-Permanent
    private String paddress2;
    private String pCityTag;
    private String pCityText;
    private String pCountryTag;
    private String pCountryText;
    private String ppinCode;
    private String mobileNo;
    private String phoneNo;
    private String emailId;
    private String departmentTag;
    private String departmentText;
    private String designationTag;
    private String designationText;
    private String dob;
    private String genderTag;
    private String genderText;
    private String religionTag;
    private String religionText;
    private String panNo;
    private String maritalStatusTag;
    private String maritalStatusText;
    private String anvDate;
    private String activeTag;
    private String activeText;
    private String nature;

    public Employee_Mdl() {
    }

    public String getAccountGroupDesc() {
        return accountGroupDesc;
    }

    public void setAccountGroupDesc(String accountGroupDesc) {
        this.accountGroupDesc = accountGroupDesc;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getActiveText() {
        return activeText;
    }

    public void setActiveText(String activeText) {
        this.activeText = activeText;
    }

    public String getActiveTag() {
        return activeTag;
    }

    public void setActiveTag(String ActiveTag) {
        this.activeTag = ActiveTag;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    public String getAccountGroupTag() {
        return accountGroupTag;
    }

    public void setAccountGroupTag(String accountGroupTag) {
        this.accountGroupTag = accountGroupTag;
    }

    public String getAccountGroupText() {
        return accountGroupText;
    }

    public void setAccountGroupText(String accountGroupText) {
        this.accountGroupText = accountGroupText;
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

    public String getCityTag() {
        return cityTag;
    }

    public void setCityTag(String cityTag) {
        this.cityTag = cityTag;
    }

    public String getCityText() {
        return cityText;
    }

    public void setCityText(String cityText) {
        this.cityText = cityText;
    }

    public String getCountryTag() {
        return countryTag;
    }

    public void setCountryTag(String countryTag) {
        this.countryTag = countryTag;
    }

    public String getCountryText() {
        return countryText;
    }

    public void setCountryText(String countryText) {
        this.countryText = countryText;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getPaddress1() {
        return paddress1;
    }

    public void setPaddress1(String paddress1) {
        this.paddress1 = paddress1;
    }

    public String getPaddress2() {
        return paddress2;
    }

    public void setPaddress2(String paddress2) {
        this.paddress2 = paddress2;
    }

    public String getpCityTag() {
        return pCityTag;
    }

    public void setpCityTag(String pCityTag) {
        this.pCityTag = pCityTag;
    }

    public String getpCityText() {
        return pCityText;
    }

    public void setpCityText(String pCityText) {
        this.pCityText = pCityText;
    }

    public String getpCountryTag() {
        return pCountryTag;
    }

    public void setpCountryTag(String pCountryTag) {
        this.pCountryTag = pCountryTag;
    }

    public String getpCountryText() {
        return pCountryText;
    }

    public void setpCountryText(String pCountryText) {
        this.pCountryText = pCountryText;
    }

    public String getPpinCode() {
        return ppinCode;
    }

    public void setPpinCode(String ppinCode) {
        this.ppinCode = ppinCode;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getDepartmentTag() {
        return departmentTag;
    }

    public void setDepartmentTag(String departmentTag) {
        this.departmentTag = departmentTag;
    }

    public String getDepartmentText() {
        return departmentText;
    }

    public void setDepartmentText(String departmentText) {
        this.departmentText = departmentText;
    }

    public String getDesignationTag() {
        return designationTag;
    }

    public void setDesignationTag(String designationTag) {
        this.designationTag = designationTag;
    }

    public String getDesignationText() {
        return designationText;
    }

    public void setDesignationText(String designationText) {
        this.designationText = designationText;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGenderTag() {
        return genderTag;
    }

    public void setGenderTag(String genderTag) {
        this.genderTag = genderTag;
    }

    public String getGenderText() {
        return genderText;
    }

    public void setGenderText(String genderText) {
        this.genderText = genderText;
    }

    public String getReligionTag() {
        return religionTag;
    }

    public void setReligionTag(String religionTag) {
        this.religionTag = religionTag;
    }

    public String getReligionText() {
        return religionText;
    }

    public void setReligionText(String religionText) {
        this.religionText = religionText;
    }

    public String getPanNo() {
        return panNo;
    }

    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    public String getMaritalStatusTag() {
        return maritalStatusTag;
    }

    public void setMaritalStatusTag(String maritalStatusTag) {
        this.maritalStatusTag = maritalStatusTag;
    }

    public String getMaritalStatusText() {
        return maritalStatusText;
    }

    public void setMaritalStatusText(String maritalStatusText) {
        this.maritalStatusText = maritalStatusText;
    }

    public String getAnvDate() {
        return anvDate;
    }

    public void setAnvDate(String anvDate) {
        this.anvDate = anvDate;
    }

}
