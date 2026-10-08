package aarogya.pharmacy.quotation.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHItemName_Mdl extends KeyField implements Serializable, IsSerializable {

    private String manualCode;
    private String sku;
    private String unit;
    private String decimalPlaces;
    private String purchaseUnitName;
    private String purchaseUnitCode;
    private String decimalPlacesPurchaseUnit;
    private String conversionRate;
    private String isIndentMandatory;
    private String displayUnit;
    private String itsBasedOn;
    private String rate;

    public TxtHItemName_Mdl() {
    }

    public String getManualCode() {
        return manualCode;
    }

    public void setManualCode(String manualCode) {
        this.manualCode = manualCode;
    }

    public String getDisplayUnit() {
        return displayUnit;
    }

    public void setDisplayUnit(String displayUnit) {
        this.displayUnit = displayUnit;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDecimalPlaces() {
        return decimalPlaces;
    }

    public void setDecimalPlaces(String decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

    public String getPurchaseUnitName() {
        return purchaseUnitName;
    }

    public void setPurchaseUnitName(String purchaseUnitName) {
        this.purchaseUnitName = purchaseUnitName;
    }

    public String getPurchaseUnitCode() {
        return purchaseUnitCode;
    }

    public void setPurchaseUnitCode(String purchaseUnitCode) {
        this.purchaseUnitCode = purchaseUnitCode;
    }

    public String getDecimalPlacesPurchaseUnit() {
        return decimalPlacesPurchaseUnit;
    }

    public void setDecimalPlacesPurchaseUnit(String decimalPlacesPurchaseUnit) {
        this.decimalPlacesPurchaseUnit = decimalPlacesPurchaseUnit;
    }

    public String getConversionRate() {
        return conversionRate;
    }

    public void setConversionRate(String conversionRate) {
        this.conversionRate = conversionRate;
    }

    public String getIsIndentMandatory() {
        return isIndentMandatory;
    }

    public void setIsIndentMandatory(String isIndentMandatory) {
        this.isIndentMandatory = isIndentMandatory;
    }

    public String getItsBasedOn() {
        return itsBasedOn;
    }

    public void setItsBasedOn(String itsBasedOn) {
        this.itsBasedOn = itsBasedOn;
    }

}
