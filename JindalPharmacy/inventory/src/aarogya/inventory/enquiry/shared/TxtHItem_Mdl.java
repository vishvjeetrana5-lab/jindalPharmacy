package aarogya.inventory.enquiry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHItem_Mdl extends KeyField implements Serializable, IsSerializable {

    private String manualCode;
    private String skuName;
    private String sku;
    private String purchaseUnitName;
    private String purchaseUnitCode;
    private String decimalPlacesPurchaseUnit;
    private String decimalPlaces;
    private String itemProperty;
    private String conversionRate;
    private String displayUnit;

    public TxtHItem_Mdl() {
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

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getDecimalPlaces() {
        return decimalPlaces;
    }

    public void setDecimalPlaces(String decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

    public String getItemProperty() {
        return itemProperty;
    }

    public void setItemProperty(String itemProperty) {
        this.itemProperty = itemProperty;
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

}
