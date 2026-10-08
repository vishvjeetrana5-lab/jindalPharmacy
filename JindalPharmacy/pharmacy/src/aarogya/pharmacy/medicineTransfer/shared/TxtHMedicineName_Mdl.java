package aarogya.pharmacy.medicineTransfer.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHMedicineName_Mdl extends KeyField implements Serializable, IsSerializable {

    private String manualCode;
    private String skuName;
    private String purchaseUnitConversionRate;
    private String stock;
    private int skuDecimalPlaces;
    private boolean bestBefore = false;
    private String skuCode;
    private String purchaseUnitName;
    private String purchaseUnitCode;
    private boolean allowNegativeStock;
    private String isBarcodeActive;
    private String fromDeptStk;

    public TxtHMedicineName_Mdl() {
    }

    public TxtHMedicineName_Mdl(TxtHMedicineName_Mdl medMdl) {
        this(medMdl.getManualCode(), medMdl.getSkuName(), medMdl.getPurchaseUnitConversionRate(), medMdl.getStock(), medMdl.getSkuDecimalPlaces(), medMdl.getSkuCode(), medMdl.getPurchaseUnitName(), medMdl.getPurchaseUnitCode(), medMdl.getSharedKeyFieldCode(), medMdl.getSharedKeyFieldName(), medMdl.isAllowNegativeStock(), medMdl.getIsBarcodeActive(), medMdl.getFromDeptStk());
    }

    public TxtHMedicineName_Mdl(String manualCode, String skuName, String purchaseUnitConversionRate, String stock, int skuDecimalPlaces, String skuCode, String purchaseUnitName, String purchaseUnitCode, String KeyFieldCode, String KeyFieldName, Boolean allowNegativeStock, String isBarcodeActive, String fromDeptStk) {
        super(KeyFieldCode, KeyFieldName);
        this.manualCode = manualCode;
        this.skuName = skuName;
        this.purchaseUnitConversionRate = purchaseUnitConversionRate;
        this.stock = stock;
        this.skuDecimalPlaces = skuDecimalPlaces;
        this.skuCode = skuCode;
        this.purchaseUnitName = purchaseUnitName;
        this.purchaseUnitCode = purchaseUnitCode;
        this.allowNegativeStock = allowNegativeStock;
        this.isBarcodeActive = isBarcodeActive;
        this.fromDeptStk = fromDeptStk;
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

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getPurchaseUnitConversionRate() {
        return purchaseUnitConversionRate;
    }

    public void setPurchaseUnitConversionRate(String purchaseUnitConversionRate) {
        this.purchaseUnitConversionRate = purchaseUnitConversionRate;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public int getSkuDecimalPlaces() {
        return skuDecimalPlaces;
    }

    public void setSkuDecimalPlaces(int skuDecimalPlaces) {
        this.skuDecimalPlaces = skuDecimalPlaces;
    }

    public boolean isBestBefore() {
        return bestBefore;
    }

    public void setBestBefore(boolean bestBefore) {
        this.bestBefore = bestBefore;
    }

    public boolean isAllowNegativeStock() {
        return allowNegativeStock;
    }

    public void setAllowNegativeStock(boolean allowNegativeStock) {
        this.allowNegativeStock = allowNegativeStock;
    }

    public String getIsBarcodeActive() {
        return isBarcodeActive;
    }

    public void setIsBarcodeActive(String isBarcodeActive) {
        this.isBarcodeActive = isBarcodeActive;
    }

    public String getFromDeptStk() {
        return fromDeptStk;
    }

    public void setFromDeptStk(String fromDeptStk) {
        this.fromDeptStk = fromDeptStk;
    }

}
