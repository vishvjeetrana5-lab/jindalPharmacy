package aarogya.master.corporateEmployeeMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class CorporateEmployeeMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    private String manualCode;
    private String corporateCode;
    private String corporateName;
    private String policyNo;
    private String relationship;
    private String rank;
    private String cardHolderName;
    private String address1;
    private String address2;
    private String cityText;
    private String cityTag;
    private String state;
    private String pin;
    private String zoneText;
    private String zoneTag;
    private String mobile;
    private String genderText;
    private String genderTag;
    private String bloodGroupText;
    private String bloodGroupTag;
    private String ageYear;
    private String ageMonth;
    private String ageDay;
    private String dob;
    private String displayDOB;
    private String tempDOB;
    private String showDDUptoMonth;

    private String showMMUptoYear;
    private String dtVoucher;

    public CorporateEmployeeMaster_Mdl() {
    }

    public String getCorporateCode() {
        return corporateCode;
    }

    public String getShowMMUptoYear() {
        return showMMUptoYear;
    }

    public void setShowMMUptoYear(String showMMUptoYear) {
        this.showMMUptoYear = showMMUptoYear;
    }

    public String getAddress1() {
        return address1;
    }

    public String getShowDDUptoMonth() {
        return showDDUptoMonth;
    }

    public void setShowDDUptoMonth(String showDDUptoMonth) {
        this.showDDUptoMonth = showDDUptoMonth;
    }

    public String getDtVoucher() {
        return dtVoucher;
    }

    public void setDtVoucher(String dtVoucher) {
        this.dtVoucher = dtVoucher;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
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

    public String getCityText() {
        return cityText;
    }

    public void setCityText(String cityText) {
        this.cityText = cityText;
    }

    public String getCityTag() {
        return cityTag;
    }

    public void setCityTag(String cityTag) {
        this.cityTag = cityTag;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getZoneText() {
        return zoneText;
    }

    public void setZoneText(String zoneText) {
        this.zoneText = zoneText;
    }

    public String getZoneTag() {
        return zoneTag;
    }

    public void setZoneTag(String zoneTag) {
        this.zoneTag = zoneTag;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public String getDisplayDOB() {
        return displayDOB;
    }

    public void setDisplayDOB(String displayDOB) {
        this.displayDOB = displayDOB;
    }

    public String getTempDOB() {
        return tempDOB;
    }

    public void setTempDOB(String tempDOB) {
        this.tempDOB = tempDOB;
    }

    public String getGenderText() {
        return genderText;
    }

    public void setGenderText(String genderText) {
        this.genderText = genderText;
    }

    public String getGenderTag() {
        return genderTag;
    }

    public void setGenderTag(String genderTag) {
        this.genderTag = genderTag;
    }

    public String getBloodGroupText() {
        return bloodGroupText;
    }

    public void setBloodGroupText(String bloodGroupText) {
        this.bloodGroupText = bloodGroupText;
    }

    public String getBloodGroupTag() {
        return bloodGroupTag;
    }

    public void setBloodGroupTag(String bloodGroupTag) {
        this.bloodGroupTag = bloodGroupTag;
    }

    public void setCorporateCode(String corporateCode) {
        this.corporateCode = corporateCode;
    }

    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getManualCode() {
        return manualCode;
    }

    public void setManualCode(String manualCode) {
        this.manualCode = manualCode;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getCorporateName() {
        return corporateName;
    }

    public void setCorporateName(String corporateName) {
        this.corporateName = corporateName;
    }

}
