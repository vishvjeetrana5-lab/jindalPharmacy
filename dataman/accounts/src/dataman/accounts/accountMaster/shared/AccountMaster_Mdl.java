package dataman.accounts.accountMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.accounts.addressBook.shared.GrdContactPerson_Mdl;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AccountMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    private String AccountNamePrefix;
    private String DisplayName;
    private String ManualCode;
    private String AccGroup;
    private String AccGroupName;
    private String NatureCode;
    private String NatureName;
    private String CostCenter;
    private String CostCenterName;
    private String Address;
    private String Address1;
    private String City;
    private String CityName;
    private String Zone;
    private String ZoneName;
    private String Pin;
    private String PhoneNo;
    private String MobileNo;
    private String FaxNo;
    private String Email;
    private String PartyCategory;
    private String TdsCategory;
    private String TdsCategoryName;
    private String ReferencedTo;
    private String ReferencedToName;
    private String Pan;
    private String Tin;
    private String Duplicate;
    private String CreditLimit;
    private String DueDays;
    private String IecNo;
    private String LedgerGroup;
    private String LedgerGroupName;
    private String PartyType;
    private String Remark;
    private String stateName;
    private String gsTIN;
    private String aadhaar;
    private String gsTINDate;
    private String currentDate;
    private String stateNumericCode;
    //===================================<MJ>==================================//
    private String natureManualCode;
    private String transportId;
    private String isSelfTransportTag;
    private String isSelfTransportText;
    //===================================<*****>================================//

    private boolean gstApplicable = false;
    private List<GrdBankDetail_Mdl> grdBankDetail = new ArrayList<GrdBankDetail_Mdl>();
    private List<GrdContactPerson_Mdl> listContectPerson = new ArrayList<GrdContactPerson_Mdl>();
    private List<GrdAdditionalPlace_Mdl> grdAdditionalPlaceDetail = new ArrayList<GrdAdditionalPlace_Mdl>();

    public AccountMaster_Mdl() {
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

    public String getPartyCategory() {
        return PartyCategory;
    }

    public void setPartyCategory(String PartyCategory) {
        this.PartyCategory = PartyCategory;
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

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getGsTIN() {
        return gsTIN;
    }

    public void setGsTIN(String gsTIN) {
        this.gsTIN = gsTIN;
    }

    public List<GrdBankDetail_Mdl> getGrdBankDetail() {
        return grdBankDetail;
    }

    public void setGrdBankDetail(List<GrdBankDetail_Mdl> grdBankDetail) {
        this.grdBankDetail = grdBankDetail;
    }

    public List<GrdContactPerson_Mdl> getListContectPerson() {
        return listContectPerson;
    }

    public void setListContectPerson(List<GrdContactPerson_Mdl> listContectPerson) {
        this.listContectPerson = listContectPerson;
    }

    public List<GrdAdditionalPlace_Mdl> getGrdAdditionalPlaceDetail() {
        return grdAdditionalPlaceDetail;
    }

    public String getAadhaar() {
        return aadhaar;
    }

    public void setAadhaar(String aadhaar) {
        this.aadhaar = aadhaar;
    }

    public String getGsTINDate() {
        return gsTINDate;
    }

    public void setGsTINDate(String gsTINDate) {
        this.gsTINDate = gsTINDate;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
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

    public String getNatureManualCode() {
        return natureManualCode;
    }

    public void setNatureManualCode(String natureManualCode) {
        this.natureManualCode = natureManualCode;
    }

    public String getTransportId() {
        return transportId;
    }

    public void setTransportId(String transportId) {
        this.transportId = transportId;
    }

    public String getIsSelfTransportTag() {
        return isSelfTransportTag;
    }

    public void setIsSelfTransportTag(String isSelfTransportTag) {
        this.isSelfTransportTag = isSelfTransportTag;
    }

    public String getIsSelfTransportText() {
        return isSelfTransportText;
    }

    public void setIsSelfTransportText(String isSelfTransportText) {
        this.isSelfTransportText = isSelfTransportText;
    }

}
