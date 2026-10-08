package aarogya.pharmacy.medicineOpening.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHMedicineName_Mdl extends KeyField implements Serializable, IsSerializable {

    private String manualCode;
    private String skuName;
    private String skuCode;
    private String stock;
    private String skuDecimalPlaces;
    private String isBestBefore;
    private String purchaseUnitConversionRate;
    private String purchaseUnitName;
    private String purchaseUnitCode;
    private String purchaseUnitDecimalPlaces;
    private String isBarcodeActive;

    public TxtHMedicineName_Mdl() {
    }

    public String getManualCode() {
        return manualCode;
    }

    public void setManualCode(String manualCode) {
        this.manualCode = manualCode;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
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

    public String getIsBestBefore() {
        return isBestBefore;
    }

    public void setIsBestBefore(String isBestBefore) {
        this.isBestBefore = isBestBefore;
    }

    public String getSkuDecimalPlaces() {
        return skuDecimalPlaces;
    }

    public void setSkuDecimalPlaces(String skuDecimalPlaces) {
        this.skuDecimalPlaces = skuDecimalPlaces;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
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

    public String getIsBarcodeActive() {
        return isBarcodeActive;
    }

    public void setIsBarcodeActive(String isBarcodeActive) {
        this.isBarcodeActive = isBarcodeActive;
    }

}
