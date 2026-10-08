package aarogya.pharmacy.pharmacyFinalDiscountEntry.shared;

import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.RecId;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PharmacyFinalDiscountEntry_Mdl extends KeyField implements Serializable, IsSerializable {

    private RecId recId = new RecId();
    private String voucherDate;
    private String voucherTypeCode;
    private String voucherTypeName;
    private String recptVoucherTypeCode;
    private String recptVoucherTypeName;
    private String recptAttachedPaymentMode;
    private String customerTypeCode;
    private String customerTypeName;
    private String lmCode;
    private String displayName;
    private String manualCode;
    private String idTypeCode;
    private String idTypeName;
    private String idNo;
    private String address;
    private String cityCode;
    private String cityName;
    private String siteStateCode;
    private String placeOfSupply;
    private String partyType;
    private String cgstAmt;
    private String sgstAmt;
    private String ugstAmt;
    private String mobile;
    private String subCode;
    private String regDocId;
    private String regRecID;
    private String taxableAmountToSave;
    private String netAmtToSave;
    private String pharmacyBalance;
    private String userEntDtLatestLine;
    private String remark;
    private String regSiteCode;
    private String roomCode;
    private String pharmacyFinalDiscountOHCode;
    private String isCancel;
    private String uhidDocId;
    private String uhidRecId;
    private String defaultPharmacyServiceCode;
    private RecId recIdRecipt = new RecId();
    private ModifyReason_Mdl modifyReason = new ModifyReason_Mdl();
    private DefaultValue_Mdl defaultValue = new DefaultValue_Mdl();
    private List<GrdDiscountDetails_Mdl> GrdDiscountDetails = new ArrayList<GrdDiscountDetails_Mdl>();

    public PharmacyFinalDiscountEntry_Mdl() {
    }

    public String getSiteStateCode() {
        return siteStateCode;
    }

    public void setSiteStateCode(String siteStateCode) {
        this.siteStateCode = siteStateCode;
    }

    public DefaultValue_Mdl getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(DefaultValue_Mdl defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getVoucherTypeCode() {
        return voucherTypeCode;
    }

    public void setVoucherTypeCode(String voucherTypeCode) {
        this.voucherTypeCode = voucherTypeCode;
    }

    public String getVoucherTypeName() {
        return voucherTypeName;
    }

    public void setVoucherTypeName(String voucherTypeName) {
        this.voucherTypeName = voucherTypeName;
    }

    public String getRecptVoucherTypeCode() {
        return recptVoucherTypeCode;
    }

    public void setRecptVoucherTypeCode(String recptVoucherTypeCode) {
        this.recptVoucherTypeCode = recptVoucherTypeCode;
    }

    public String getRecptVoucherTypeName() {
        return recptVoucherTypeName;
    }

    public void setRecptVoucherTypeName(String recptVoucherTypeName) {
        this.recptVoucherTypeName = recptVoucherTypeName;
    }

    public String getRecptAttachedPaymentMode() {
        return recptAttachedPaymentMode;
    }

    public void setRecptAttachedPaymentMode(String recptAttachedPaymentMode) {
        this.recptAttachedPaymentMode = recptAttachedPaymentMode;
    }

    public String getUhidDocId() {
        return uhidDocId;
    }

    public void setUhidDocId(String uhidDocId) {
        this.uhidDocId = uhidDocId;
    }

    public String getUhidRecId() {
        return uhidRecId;
    }

    public void setUhidRecId(String uhidRecId) {
        this.uhidRecId = uhidRecId;
    }

    public String getRegRecID() {
        return regRecID;
    }

    public void setRegRecID(String regRecID) {
        this.regRecID = regRecID;
    }

    public String getIsCancel() {
        return isCancel;
    }

    public void setIsCancel(String isCancel) {
        this.isCancel = isCancel;
    }

    public String getPharmacyFinalDiscountOHCode() {
        return pharmacyFinalDiscountOHCode;
    }

    public void setPharmacyFinalDiscountOHCode(String pharmacyFinalDiscountOHCode) {
        this.pharmacyFinalDiscountOHCode = pharmacyFinalDiscountOHCode;
    }

    public String getRegSiteCode() {
        return regSiteCode;
    }

    public void setRegSiteCode(String regSiteCode) {
        this.regSiteCode = regSiteCode;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public List<GrdDiscountDetails_Mdl> getGrdDiscountDetails() {
        return GrdDiscountDetails;
    }

    public void setGrdDiscountDetails(List<GrdDiscountDetails_Mdl> GrdDiscountDetails) {
        this.GrdDiscountDetails = GrdDiscountDetails;
    }

    public String getPartyType() {
        return partyType;
    }

    public void setPartyType(String partyType) {
        this.partyType = partyType;
    }

    public String getCgstAmt() {
        return cgstAmt;
    }

    public void setCgstAmt(String cgstAmt) {
        this.cgstAmt = cgstAmt;
    }

    public String getSgstAmt() {
        return sgstAmt;
    }

    public void setSgstAmt(String sgstAmt) {
        this.sgstAmt = sgstAmt;
    }

    public String getUgstAmt() {
        return ugstAmt;
    }

    public void setUgstAmt(String ugstAmt) {
        this.ugstAmt = ugstAmt;
    }

    public String getPlaceOfSupply() {
        return placeOfSupply;
    }

    public void setPlaceOfSupply(String placeOfSupply) {
        this.placeOfSupply = placeOfSupply;
    }

    public String getUserEntDtLatestLine() {
        return userEntDtLatestLine;
    }

    public void setUserEntDtLatestLine(String userEntDtLatestLine) {
        this.userEntDtLatestLine = userEntDtLatestLine;
    }

    public RecId getRecIdRecipt() {
        return recIdRecipt;
    }

    public void setRecIdRecipt(RecId recIdRecipt) {
        this.recIdRecipt = recIdRecipt;
    }

    public String getNetAmtToSave() {
        return netAmtToSave;
    }

    public void setNetAmtToSave(String netAmtToSave) {
        this.netAmtToSave = netAmtToSave;
    }

    public RecId getRecId() {
        return recId;
    }

    public void setRecId(RecId recId) {
        this.recId = recId;
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public String getCustomerTypeCode() {
        return customerTypeCode;
    }

    public void setCustomerTypeCode(String customerTypeCode) {
        this.customerTypeCode = customerTypeCode;
    }

    public String getCustomerTypeName() {
        return customerTypeName;
    }

    public void setCustomerTypeName(String customerTypeName) {
        this.customerTypeName = customerTypeName;
    }

    public String getLmCode() {
        return lmCode;
    }

    public void setLmCode(String lmCode) {
        this.lmCode = lmCode;
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

    public String getIdTypeCode() {
        return idTypeCode;
    }

    public void setIdTypeCode(String idTypeCode) {
        this.idTypeCode = idTypeCode;
    }

    public String getIdTypeName() {
        return idTypeName;
    }

    public void setIdTypeName(String idTypeName) {
        this.idTypeName = idTypeName;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getRegDocId() {
        return regDocId;
    }

    public void setRegDocId(String regDocId) {
        this.regDocId = regDocId;
    }

    public String getTaxableAmountToSave() {
        return taxableAmountToSave;
    }

    public void setTaxableAmountToSave(String taxableAmountToSave) {
        this.taxableAmountToSave = taxableAmountToSave;
    }

    public String getPharmacyBalance() {
        return pharmacyBalance;
    }

    public void setPharmacyBalance(String pharmacyBalance) {
        this.pharmacyBalance = pharmacyBalance;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public ModifyReason_Mdl getModifyReason() {
        return modifyReason;
    }

    public void setModifyReason(ModifyReason_Mdl modifyReason) {
        this.modifyReason = modifyReason;
    }

    public String getDefaultPharmacyServiceCode() {
        return defaultPharmacyServiceCode;
    }

    public void setDefaultPharmacyServiceCode(String defaultPharmacyServiceCode) {
        this.defaultPharmacyServiceCode = defaultPharmacyServiceCode;
    }
}
