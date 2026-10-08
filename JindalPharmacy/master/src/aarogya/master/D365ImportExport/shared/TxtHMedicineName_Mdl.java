package aarogya.master.D365ImportExport.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHMedicineName_Mdl extends KeyField implements Serializable, IsSerializable {

    private String manualCode;
    private String hsnCode;
    private String itemGSTNature;
    private String itsBasedOn;
    private String isBestBefore;
    private String isIncludeFreeQtyInSaleRate;
    private String itemProperty;

    private String skuCode;
    private String skuName;
    private String skuDecimalPlaces;

    private String purchaseUnitCode;
    private String purchaseUnitName;
    private String purchaseUnitDisplayName;
    private String purchaseUnitConversionRate;
    private String purchaseUnitDecimalPlaces;

    private String masterMrp;
    private String masterSaleRate;
    private String masterPurchaseRate;
    private String masterSchemeOnQty;
    private String masterSchemeFreeQty;
    private String masterPurchaseDiscountPer;

    private String isBarcodeActive;
    private String saleRateProffitMargine;
    private String ItemCategoryProffitMargine;

    public TxtHMedicineName_Mdl() {
    }

    public String getManualCode() {
        return manualCode;
    }

    public void setManualCode(String manualCode) {
        this.manualCode = manualCode;
    }

    public String getHsnCode() {
        return hsnCode;
    }

    public void setHsnCode(String hsnCode) {
        this.hsnCode = hsnCode;
    }

    public String getItemGSTNature() {
        return itemGSTNature;
    }

    public void setItemGSTNature(String itemGSTNature) {
        this.itemGSTNature = itemGSTNature;
    }

    public String getItsBasedOn() {
        return itsBasedOn;
    }

    public void setItsBasedOn(String itsBasedOn) {
        this.itsBasedOn = itsBasedOn;
    }

    public String getIsBestBefore() {
        return isBestBefore;
    }

    public void setIsBestBefore(String isBestBefore) {
        this.isBestBefore = isBestBefore;
    }

    public String getIsIncludeFreeQtyInSaleRate() {
        return isIncludeFreeQtyInSaleRate;
    }

    public void setIsIncludeFreeQtyInSaleRate(String isIncludeFreeQtyInSaleRate) {
        this.isIncludeFreeQtyInSaleRate = isIncludeFreeQtyInSaleRate;
    }

    public String getItemProperty() {
        return itemProperty;
    }

    public void setItemProperty(String itemProperty) {
        this.itemProperty = itemProperty;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
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

    public String getPurchaseUnitCode() {
        return purchaseUnitCode;
    }

    public void setPurchaseUnitCode(String purchaseUnitCode) {
        this.purchaseUnitCode = purchaseUnitCode;
    }

    public String getPurchaseUnitName() {
        return purchaseUnitName;
    }

    public void setPurchaseUnitName(String purchaseUnitName) {
        this.purchaseUnitName = purchaseUnitName;
    }

    public String getPurchaseUnitDisplayName() {
        return purchaseUnitDisplayName;
    }

    public void setPurchaseUnitDisplayName(String purchaseUnitDisplayName) {
        this.purchaseUnitDisplayName = purchaseUnitDisplayName;
    }

    public String getPurchaseUnitConversionRate() {
        return purchaseUnitConversionRate;
    }

    public void setPurchaseUnitConversionRate(String purchaseUnitConversionRate) {
        this.purchaseUnitConversionRate = purchaseUnitConversionRate;
    }

    public String getPurchaseUnitDecimalPlaces() {
        return purchaseUnitDecimalPlaces;
    }

    public void setPurchaseUnitDecimalPlaces(String purchaseUnitDecimalPlaces) {
        this.purchaseUnitDecimalPlaces = purchaseUnitDecimalPlaces;
    }

    public String getMasterMrp() {
        return masterMrp;
    }

    public void setMasterMrp(String masterMrp) {
        this.masterMrp = masterMrp;
    }

    public String getMasterSaleRate() {
        return masterSaleRate;
    }

    public void setMasterSaleRate(String masterSaleRate) {
        this.masterSaleRate = masterSaleRate;
    }

    public String getMasterPurchaseRate() {
        return masterPurchaseRate;
    }

    public void setMasterPurchaseRate(String masterPurchaseRate) {
        this.masterPurchaseRate = masterPurchaseRate;
    }

    public String getMasterSchemeOnQty() {
        return masterSchemeOnQty;
    }

    public void setMasterSchemeOnQty(String masterSchemeOnQty) {
        this.masterSchemeOnQty = masterSchemeOnQty;
    }

    public String getMasterSchemeFreeQty() {
        return masterSchemeFreeQty;
    }

    public void setMasterSchemeFreeQty(String masterSchemeFreeQty) {
        this.masterSchemeFreeQty = masterSchemeFreeQty;
    }

    public String getMasterPurchaseDiscountPer() {
        return masterPurchaseDiscountPer;
    }

    public void setMasterPurchaseDiscountPer(String masterPurchaseDiscountPer) {
        this.masterPurchaseDiscountPer = masterPurchaseDiscountPer;
    }

    public String getIsBarcodeActive() {
        return isBarcodeActive;
    }

    public void setIsBarcodeActive(String isBarcodeActive) {
        this.isBarcodeActive = isBarcodeActive;
    }

    public String getSaleRateProffitMargine() {
        return saleRateProffitMargine;
    }

    public void setSaleRateProffitMargine(String saleRateProffitMargine) {
        this.saleRateProffitMargine = saleRateProffitMargine;
    }

    public String getItemCategoryProffitMargine() {
        return ItemCategoryProffitMargine;
    }

    public void setItemCategoryProffitMargine(String ItemCategoryProffitMargine) {
        this.ItemCategoryProffitMargine = ItemCategoryProffitMargine;
    }
}
