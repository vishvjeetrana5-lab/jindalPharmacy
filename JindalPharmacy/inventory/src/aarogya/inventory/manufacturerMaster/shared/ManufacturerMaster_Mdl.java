package aarogya.inventory.manufacturerMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ManufacturerMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    private String tempKeyCode;
    private String AccountNamePrefix;
    private String DisplayName;
    private String ManualCode;
    private String AccGroup;
    private String AccGroupName;
    private String NatureCode;
    private String NatureName;
    private String CostCenter;
    private String CostCenterName;
    private String FatherName;
    private String Address;
    private String Address1;
    private String City;
    private String CityName;
    private String StateName;
    private String Zone;
    private String ZoneName;
    private String Pin;
    private String PhoneNo;
    private String MobileNo;
    private String FaxNo;
    private String Email;
    private String TdsCategory;
    private String TdsCategoryName;
    private String ReferencedTo;
    private String ReferencedToName;
    private String ContactPerson;
    private String Pan;
    private String Tin;
    private String Duplicate;
    private String CstNo;
    private String Stno;
    private String EccNo;
    private String ExciseNo;
    private String Range;
    private String Division;
    private String CreditLimit;
    private String DueDays;
    private String IecNo;
    private String LedgerGroup;
    private String LedgerGroupName;
    private String PolicyNo;
    private String PartyType;
    private String Remark;
    private String Aadhar;
    private String gsTINDate;
    private String gsTin;
    private String currentDate;
    private String stateNumericCode;
    private boolean gstApplicable = false;
    private List<GrdBankDetail_Mdl> grdBankDetail = new ArrayList<GrdBankDetail_Mdl>();

    public List<GrdBankDetail_Mdl> getGrdBankDetail() {
        return grdBankDetail;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getAadhar() {
        return Aadhar;
    }

    public void setAadhar(String Aadhar) {
        this.Aadhar = Aadhar;
    }

    public String getGsTINDate() {
        return gsTINDate;
    }

    public void setGsTINDate(String gsTINDate) {
        this.gsTINDate = gsTINDate;
    }

    public String getGsTin() {
        return gsTin;
    }

    public void setGsTin(String gsTin) {
        this.gsTin = gsTin;
    }

    public ManufacturerMaster_Mdl() {
    }

    public String getTempKeyCode() {
        return tempKeyCode;
    }

    public void setTempKeyCode(String tempKeyCode) {
        this.tempKeyCode = tempKeyCode;
    }

    public String getNatureCode() {
        return NatureCode;
    }

    public void setNatureCode(String NatureCode) {
        this.NatureCode = NatureCode;
    }

    public String getNatureName() {
        return NatureName;
    }

    public void setNatureName(String NatureName) {
        this.NatureName = NatureName;
    }

    public String getAccountNamePrefix() {
        return AccountNamePrefix;
    }

    public void setAccountNamePrefix(String AccountNamePrefix) {
        this.AccountNamePrefix = AccountNamePrefix;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String DisplayName) {
        this.DisplayName = DisplayName;
    }

    public String getManualCode() {
        return ManualCode;
    }

    public void setManualCode(String ManualCode) {
        this.ManualCode = ManualCode;
    }

    public String getAccGroup() {
        return AccGroup;
    }

    public void setAccGroup(String AccGroup) {
        this.AccGroup = AccGroup;
    }

    public String getAccGroupName() {
        return AccGroupName;
    }

    public void setAccGroupName(String AccGroupName) {
        this.AccGroupName = AccGroupName;
    }

    public String getCostCenter() {
        return CostCenter;
    }

    public void setCostCenter(String CostCenter) {
        this.CostCenter = CostCenter;
    }

    public String getCostCenterName() {
        return CostCenterName;
    }

    public void setCostCenterName(String CostCenterName) {
        this.CostCenterName = CostCenterName;
    }

    public String getFatherName() {
        return FatherName;
    }

    public void setFatherName(String FatherName) {
        this.FatherName = FatherName;
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

    public String getCity() {
        return City;
    }

    public void setCity(String City) {
        this.City = City;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String CityName) {
        this.CityName = CityName;
    }

    public String getStateName() {
        return StateName;
    }

    public void setStateName(String StateName) {
        this.StateName = StateName;
    }

    public String getZone() {
        return Zone;
    }

    public void setZone(String Zone) {
        this.Zone = Zone;
    }

    public String getZoneName() {
        return ZoneName;
    }

    public void setZoneName(String ZoneName) {
        this.ZoneName = ZoneName;
    }

    public String getPin() {
        return Pin;
    }

    public void setPin(String Pin) {
        this.Pin = Pin;
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

    public String getTdsCategory() {
        return TdsCategory;
    }

    public void setTdsCategory(String TdsCategory) {
        this.TdsCategory = TdsCategory;
    }

    public String getTdsCategoryName() {
        return TdsCategoryName;
    }

    public void setTdsCategoryName(String TdsCategoryName) {
        this.TdsCategoryName = TdsCategoryName;
    }

    public String getReferencedTo() {
        return ReferencedTo;
    }

    public void setReferencedTo(String ReferencedTo) {
        this.ReferencedTo = ReferencedTo;
    }

    public String getReferencedToName() {
        return ReferencedToName;
    }

    public void setReferencedToName(String ReferencedToName) {
        this.ReferencedToName = ReferencedToName;
    }

    public String getContactPerson() {
        return ContactPerson;
    }

    public void setContactPerson(String ContactPerson) {
        this.ContactPerson = ContactPerson;
    }

    public String getPan() {
        return Pan;
    }

    public void setPan(String Pan) {
        this.Pan = Pan;
    }

    public String getTin() {
        return Tin;
    }

    public void setTin(String Tin) {
        this.Tin = Tin;
    }

    public String getDuplicate() {
        return Duplicate;
    }

    public void setDuplicate(String Duplicate) {
        this.Duplicate = Duplicate;
    }

    public String getCstNo() {
        return CstNo;
    }

    public void setCstNo(String CstNo) {
        this.CstNo = CstNo;
    }

    public String getStno() {
        return Stno;
    }

    public void setStno(String Stno) {
        this.Stno = Stno;
    }

    public String getEccNo() {
        return EccNo;
    }

    public void setEccNo(String EccNo) {
        this.EccNo = EccNo;
    }

    public String getExciseNo() {
        return ExciseNo;
    }

    public void setExciseNo(String ExciseNo) {
        this.ExciseNo = ExciseNo;
    }

    public String getRange() {
        return Range;
    }

    public void setRange(String Range) {
        this.Range = Range;
    }

    public String getDivision() {
        return Division;
    }

    public void setDivision(String Division) {
        this.Division = Division;
    }

    public String getCreditLimit() {
        return CreditLimit;
    }

    public void setCreditLimit(String CreditLimit) {
        this.CreditLimit = CreditLimit;
    }

    public String getDueDays() {
        return DueDays;
    }

    public void setDueDays(String DueDays) {
        this.DueDays = DueDays;
    }

    public String getIecNo() {
        return IecNo;
    }

    public void setIecNo(String IecNo) {
        this.IecNo = IecNo;
    }

    public String getLedgerGroup() {
        return LedgerGroup;
    }

    public void setLedgerGroup(String LedgerGroup) {
        this.LedgerGroup = LedgerGroup;
    }

    public String getLedgerGroupName() {
        return LedgerGroupName;
    }

    public void setLedgerGroupName(String LedgerGroupName) {
        this.LedgerGroupName = LedgerGroupName;
    }

    public String getPolicyNo() {
        return PolicyNo;
    }

    public void setPolicyNo(String PolicyNo) {
        this.PolicyNo = PolicyNo;
    }

    public String getPartyType() {
        return PartyType;
    }

    public void setPartyType(String PartyType) {
        this.PartyType = PartyType;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    public String getStateNumericCode() {
        return stateNumericCode;
    }

    public void setStateNumericCode(String stateNumericCode) {
        this.stateNumericCode = stateNumericCode;
    }

    public boolean isGstApplicable() {
        return gstApplicable;
    }

    public void setGstApplicable(boolean gstApplicable) {
        this.gstApplicable = gstApplicable;
    }

}
