package dataman.accounts.addressBook.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String CompanyGroup;
    private String Nature;
    private String Designation;
    private String ContactPerson;
    private String LinkAccount;
    private String Address;
    private String City;
    private String State;
    private String Country;
    private String Zone;
    private String PinCode;
    private String PhoneNo;
    private String MobileNo;
    private String FaxNo;
    private String Email;

    public Find_Mdl() {
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

    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(String Designation) {
        this.Designation = Designation;
    }

    public String getContactPerson() {
        return ContactPerson;
    }

    public void setContactPerson(String ContactPerson) {
        this.ContactPerson = ContactPerson;
    }

    public String getLinkAccount() {
        return LinkAccount;
    }

    public void setLinkAccount(String LinkAccount) {
        this.LinkAccount = LinkAccount;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String City) {
        this.City = City;
    }

    public String getState() {
        return State;
    }

    public void setState(String State) {
        this.State = State;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String Country) {
        this.Country = Country;
    }

    public String getZone() {
        return Zone;
    }

    public void setZone(String Zone) {
        this.Zone = Zone;
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
}
