package aarogya.pharmacy.purchaseBill.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHSupplier_Mdl extends KeyField implements Serializable, IsSerializable {

    /*
    SharedKeyFieldCode = subcode AS code;
    SharedKeyFieldName = name;
     */
    private String stateCode;
    private String stateName;
    private String manualCode;
    private String cityName;
    private String partyType;
    private String gstin;
    private String gstinDate;
    private String supplierGuiConfigCode;
    private String DueDays;

    public String getSupplierGuiConfigCode() {
        return supplierGuiConfigCode;
    }

    public void setSupplierGuiConfigCode(String supplierGuiConfigCode) {
        this.supplierGuiConfigCode = supplierGuiConfigCode;
    }

    public TxtHSupplier_Mdl() {
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

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getPartyType() {
        return partyType;
    }

    public void setPartyType(String partyType) {
        this.partyType = partyType;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public String getGstinDate() {
        return gstinDate;
    }

    public void setGstinDate(String gstinDate) {
        this.gstinDate = gstinDate;
    }

    public String getManualCode() {
        return manualCode;
    }

    public void setManualCode(String manualCode) {
        this.manualCode = manualCode;
    }

    public String getDueDays() {
        return DueDays;
    }

    public void setDueDays(String DueDays) {
        this.DueDays = DueDays;
    }

}
