package aarogya.pharmacy.ledgerMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class LedgerMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    //SharedKeyFieldCode = code
    //SharedKeyFieldName = name
    private String initial;
    private String partyTypeCode;
    private String partyTypeName;
    private String partyManualCode;
    private String displayName;
    private String manualCode;
    private String address;
    private String cityCode;
    private String cityName;
    private String mobile;
    private String idTypeCode;
    private String idTypeName;
    private String idNo;
    private String accountCode;
    private String accountName;
    private String regType;
    private String regDocId;
    private String regRecId;
    private String uhidDocid;
    private String uhidRecId;
    private String regBalance;
    private String pharmacyBalance;
    private String isActive;
    private String lastUpdatedOn;
    private String subCode;
    private String stateCode;
    private String stateName;
    private String formV_Cat;
    private String gstinNo;
    private String gstinDate;

    public LedgerMaster_Mdl() {
    }

    public String getGstinNo() {
        return gstinNo;
    }

    public void setGstinNo(String gstinNo) {
        this.gstinNo = gstinNo;
    }

    public String getGstinDate() {
        return gstinDate;
    }

    public void setGstinDate(String gstinDate) {
        this.gstinDate = gstinDate;
    }

    public String getPartyManualCode() {
        return partyManualCode;
    }

    public void setPartyManualCode(String partyManualCode) {
        this.partyManualCode = partyManualCode;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getPartyTypeCode() {
        return partyTypeCode;
    }

    public void setPartyTypeCode(String partyTypeCode) {
        this.partyTypeCode = partyTypeCode;
    }

    public String getPartyTypeName() {
        return partyTypeName;
    }

    public void setPartyTypeName(String partyTypeName) {
        this.partyTypeName = partyTypeName;
    }

    public String getFormV_Cat() {
        return formV_Cat;
    }

    public void setFormV_Cat(String formV_Cat) {
        this.formV_Cat = formV_Cat;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getRegDocId() {
        return regDocId;
    }

    public void setRegDocId(String regDocId) {
        this.regDocId = regDocId;
    }

    public String getRegRecId() {
        return regRecId;
    }

    public void setRegRecId(String regRecId) {
        this.regRecId = regRecId;
    }

    public String getRegBalance() {
        return regBalance;
    }

    public void setRegBalance(String regBalance) {
        this.regBalance = regBalance;
    }

    public String getPharmacyBalance() {
        return pharmacyBalance;
    }

    public void setPharmacyBalance(String pharmacyBalance) {
        this.pharmacyBalance = pharmacyBalance;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    public void setLastUpdatedOn(String lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
    }

    public String getRegType() {
        return regType;
    }

    public void setRegType(String regType) {
        this.regType = regType;
    }

    public String getUhidDocid() {
        return uhidDocid;
    }

    public void setUhidDocid(String uhidDocid) {
        this.uhidDocid = uhidDocid;
    }

    public String getUhidRecId() {
        return uhidRecId;
    }

    public void setUhidRecId(String uhidRecId) {
        this.uhidRecId = uhidRecId;
    }

}
