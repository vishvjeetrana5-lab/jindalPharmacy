package aarogya.inventory.supplierMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGUI.shared.DmGUI_Mdl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SupplierMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    private String tempKeyCode;
    private String supplierNamePrefix;
    private String displayName;
    private String manualCode;
    private String accGroup;
    private String accGroupName;
    private String natureCode;
    private String natureName;
    private String costCenter;
    private String costCenterName;
    private String guiConfigCode;
    private String address;
    private String address1;
    private String city;
    private String cityName;
    private String stateName;
    private String stateNumericCode;
    private String zone;
    private String zoneName;
    private String pin;
    private String phoneNo;
    private String mobileNo;
    private String faxNo;
    private String email;
    private String partyCategory;
    private String partyCategoryName;
    private String tdsCategory;
    private String tdsCategoryName;
    private String referencedTo;
    private String referencedToName;
    private String pan;
    private String tin;
    private String duplicate;
    private String creditLimit;
    private String creditPeriod;
    private String iecNo;
    private String ledgerGroup;
    private String ledgerGroupName;
    private String partyType;
    private String remark;
    private String drugLicense;
    private String registrationNo;
    private String invoiceDiscount;
    private String expiryTerm;
    private String nonmoving;
    private String max;
    private String min;
    private String reOrderLevel;
    private String monopoly;
    private String vTypeTag;
    private String Aadhar;
    private String gsTINDate;
    private String gsTin;
    private String currentDate;
    private String activeTag;
    private String activeText;
    private String approvedBy;
    private boolean gstApplicable = false;
    private DmGUI_Mdl guiMdl = new DmGUI_Mdl();

    private List<GrdBankDetail_Mdl> grdBankDetail = new ArrayList<GrdBankDetail_Mdl>();
    private List<GrdContactPerson_Mdl> listContectPerson = new ArrayList<GrdContactPerson_Mdl>();
    private List<GrdManufacturer_Mdl> listManufacturer = new ArrayList<GrdManufacturer_Mdl>();
    private List<GrdCDConfiguration_Mdl> listCDConfiguration = new ArrayList<GrdCDConfiguration_Mdl>();

    public SupplierMaster_Mdl() {
    }

    public String getTempKeyCode() {
        return tempKeyCode;
    }

    public void setTempKeyCode(String tempKeyCode) {
        this.tempKeyCode = tempKeyCode;
    }

    public String getSupplierNamePrefix() {
        return supplierNamePrefix;
    }

    public void setSupplierNamePrefix(String supplierNamePrefix) {
        this.supplierNamePrefix = supplierNamePrefix;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getManualCode() {
        return manualCode;
    }

    public void setManualCode(String manualCode) {
        this.manualCode = manualCode;
    }

    public String getAccGroup() {
        return accGroup;
    }

    public void setAccGroup(String accGroup) {
        this.accGroup = accGroup;
    }

    public String getAccGroupName() {
        return accGroupName;
    }

    public void setAccGroupName(String accGroupName) {
        this.accGroupName = accGroupName;
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

    public String getCostCenter() {
        return costCenter;
    }

    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }

    public String getCostCenterName() {
        return costCenterName;
    }

    public void setCostCenterName(String costCenterName) {
        this.costCenterName = costCenterName;
    }

    public String getGuiConfigCode() {
        return guiConfigCode;
    }

    public void setGuiConfigCode(String guiConfigCode) {
        this.guiConfigCode = guiConfigCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
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

    public String getPartyCategory() {
        return partyCategory;
    }

    public void setPartyCategory(String partyCategory) {
        this.partyCategory = partyCategory;
    }

    public String getTdsCategory() {
        return tdsCategory;
    }

    public void setTdsCategory(String tdsCategory) {
        this.tdsCategory = tdsCategory;
    }

    public String getTdsCategoryName() {
        return tdsCategoryName;
    }

    public void setTdsCategoryName(String tdsCategoryName) {
        this.tdsCategoryName = tdsCategoryName;
    }

    public String getReferencedTo() {
        return referencedTo;
    }

    public void setReferencedTo(String referencedTo) {
        this.referencedTo = referencedTo;
    }

    public String getReferencedToName() {
        return referencedToName;
    }

    public void setReferencedToName(String referencedToName) {
        this.referencedToName = referencedToName;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getTin() {
        return tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }

    public String getDuplicate() {
        return duplicate;
    }

    public void setDuplicate(String duplicate) {
        this.duplicate = duplicate;
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

    public String getIecNo() {
        return iecNo;
    }

    public void setIecNo(String iecNo) {
        this.iecNo = iecNo;
    }

    public String getLedgerGroup() {
        return ledgerGroup;
    }

    public void setLedgerGroup(String ledgerGroup) {
        this.ledgerGroup = ledgerGroup;
    }

    public String getLedgerGroupName() {
        return ledgerGroupName;
    }

    public void setLedgerGroupName(String ledgerGroupName) {
        this.ledgerGroupName = ledgerGroupName;
    }

    public String getPartyType() {
        return partyType;
    }

    public void setPartyType(String partyType) {
        this.partyType = partyType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getGsTin() {
        return gsTin;
    }

    public void setGsTin(String gsTin) {
        this.gsTin = gsTin;
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

    public DmGUI_Mdl getGuiMdl() {
        return guiMdl;
    }

    public void setGuiMdl(DmGUI_Mdl guiMdl) {
        this.guiMdl = guiMdl;
    }

    public List<GrdBankDetail_Mdl> getGrdBankDetail() {
        return grdBankDetail;
    }

    public List<GrdContactPerson_Mdl> getListContectPerson() {
        return listContectPerson;
    }

    public List<GrdManufacturer_Mdl> getListManufacturer() {
        return listManufacturer;
    }

    public String getInvoiceDiscount() {
        return invoiceDiscount;
    }

    public void setInvoiceDiscount(String invoiceDiscount) {
        this.invoiceDiscount = invoiceDiscount;
    }

    public String getExpiryTerm() {
        return expiryTerm;
    }

    public void setExpiryTerm(String expiryTerm) {
        this.expiryTerm = expiryTerm;
    }

    public String getNonmoving() {
        return nonmoving;
    }

    public void setNonmoving(String nonmoving) {
        this.nonmoving = nonmoving;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getReOrderLevel() {
        return reOrderLevel;
    }

    public void setReOrderLevel(String reOrderLevel) {
        this.reOrderLevel = reOrderLevel;
    }

    public String getMonopoly() {
        return monopoly;
    }

    public void setMonopoly(String monopoly) {
        this.monopoly = monopoly;
    }

    public String getvTypeTag() {
        return vTypeTag;
    }

    public void setvTypeTag(String vTypeTag) {
        this.vTypeTag = vTypeTag;
    }

    public String getPartyCategoryName() {
        return partyCategoryName;
    }

    public void setPartyCategoryName(String partyCategoryName) {
        this.partyCategoryName = partyCategoryName;
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

    public String getActiveTag() {
        return activeTag;
    }

    public void setActiveTag(String activeTag) {
        this.activeTag = activeTag;
    }

    public String getActiveText() {
        return activeText;
    }

    public void setActiveText(String activeText) {
        this.activeText = activeText;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public List<GrdCDConfiguration_Mdl> getListCDConfiguration() {
        return listCDConfiguration;
    }

}
