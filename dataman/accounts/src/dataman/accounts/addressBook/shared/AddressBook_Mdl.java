package dataman.accounts.addressBook.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class AddressBook_Mdl extends KeyField implements Serializable, IsSerializable {

    private String CompanyGroup;
    private String Nature;
    private String Designation;
    private String ContactPerson;
    private String LinkAccountText;
    private String LinkAccountTag;
    private String Address;
    private String Address1;
    private String CityName;
    private String CityCode;
    private String StateName;
    private String ZoneName;
    private String ZoneCode;
    private String PinCode;
    private String PhoneNo;
    private String MobileNo;
    private String FaxNo;
    private String Email;
    private String Remark;
    private String isDefault;
    private String isDefaultTag;
    private String initialTag;
    private String initialText;

    public AddressBook_Mdl() {
    }

    public String getCompanyGroup() {
        return CompanyGroup;
    }

    public void setCompanyGroup(String CompanyGroup) {
        this.CompanyGroup = CompanyGroup;
    }

    public String getNature() {
        return Nature;
    }

    public void setNature(String Nature) {
        this.Nature = Nature;
    }

    public String getContactPerson() {
        return ContactPerson;
    }

    public void setContactPerson(String ContactPerson) {
        this.ContactPerson = ContactPerson;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getAddress1() {
        return Address1;
    }

    public void setAddress1(String Address1) {
        this.Address1 = Address1;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String CityName) {
        this.CityName = CityName;
    }

  

    public String getZoneName() {
        return ZoneName;
    }

    public void setZoneName(String ZoneName) {
        this.ZoneName = ZoneName;
    }

    public String getPinCode() {
        return PinCode;
    }

    public void setPinCode(String PinCode) {
        this.PinCode = PinCode;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String PhoneNo) {
        this.PhoneNo = PhoneNo;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String MobileNo) {
        this.MobileNo = MobileNo;
    }

    public String getFaxNo() {
        return FaxNo;
    }

    public void setFaxNo(String FaxNo) {
        this.FaxNo = FaxNo;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(String Designation) {
        this.Designation = Designation;
    }

    public String getLinkAccountText() {
        return LinkAccountText;
    }

    public void setLinkAccountText(String LinkAccountText) {
        this.LinkAccountText = LinkAccountText;
    }

    public String getLinkAccountTag() {
        return LinkAccountTag;
    }

    public void setLinkAccountTag(String LinkAccountTag) {
        this.LinkAccountTag = LinkAccountTag;
    }

    public String getCityCode() {
        return CityCode;
    }

    public void setCityCode(String CityCode) {
        this.CityCode = CityCode;
    }

    public String getStateName() {
        return StateName;
    }

    public void setStateName(String StateName) {
        this.StateName = StateName;
    }

    public String getZoneCode() {
        return ZoneCode;
    }

    public void setZoneCode(String ZoneCode) {
        this.ZoneCode = ZoneCode;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getIsDefaultTag() {
        return isDefaultTag;
    }

    public void setIsDefaultTag(String isDefaultTag) {
        this.isDefaultTag = isDefaultTag;
    }

    public String getInitialTag() {
        return initialTag;
    }

    public void setInitialTag(String initialTag) {
        this.initialTag = initialTag;
    }

    public String getInitialText() {
        return initialText;
    }

    public void setInitialText(String initialText) {
        this.initialText = initialText;
    }

}
