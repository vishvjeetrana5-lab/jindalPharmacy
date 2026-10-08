package aarogya.inventory.itemIssue.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHItemName_Mdl extends KeyField implements Serializable, IsSerializable {

    private String manualCode;
    private String skuName;
    private String skuDecimalPlaces;
    private String stock;
    private String isBestBefore;
    private String itemProperty;
    private String purchaseUnitConversionRate;
    private String isubCode;
    private String consumptionAc;
    private String allowedMonthlyConsumption;

    public TxtHItemName_Mdl() {
    }

    public String getManualCode() {
        return manualCode;
    }

    public void setManualCode(String manualCode) {
        this.manualCode = manualCode;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getSkuDecimalPlaces() {
        return skuDecimalPlaces;
    }

    public void setSkuDecimalPlaces(String skuDecimalPlaces) {
        this.skuDecimalPlaces = skuDecimalPlaces;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getIsBestBefore() {
        return isBestBefore;
    }

    public void setIsBestBefore(String isBestBefore) {
        this.isBestBefore = isBestBefore;
    }

    public String getItemProperty() {
        return itemProperty;
    }

    public void setItemProperty(String itemProperty) {
        this.itemProperty = itemProperty;
    }

    public String getPurchaseUnitConversionRate() {
        return purchaseUnitConversionRate;
    }

    public void setPurchaseUnitConversionRate(String purchaseUnitConversionRate) {
        this.purchaseUnitConversionRate = purchaseUnitConversionRate;
    }

    public String getIsubCode() {
        return isubCode;
    }

    public void setIsubCode(String isubCode) {
        this.isubCode = isubCode;
    }

    public String getConsumptionAc() {
        return consumptionAc;
    }

    public void setConsumptionAc(String consumptionAc) {
        this.consumptionAc = consumptionAc;
    }

    public String getAllowedMonthlyConsumption() {
        return allowedMonthlyConsumption;
    }

    public void setAllowedMonthlyConsumption(String allowedMonthlyConsumption) {
        this.allowedMonthlyConsumption = allowedMonthlyConsumption;
    }
}
