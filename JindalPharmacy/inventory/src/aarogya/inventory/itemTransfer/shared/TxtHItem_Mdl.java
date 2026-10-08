package aarogya.inventory.itemTransfer.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHItem_Mdl extends KeyField implements Serializable, IsSerializable {

    private String manualCode;
    private String skuName;
    private String purchaseUnitConversionRate;
    private String itemProperty;
    private String stock;
    private int skuDecimalPlaces;
    private boolean bestBefore = false;
    private String toCurrentStock;

    public TxtHItem_Mdl() {
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

    public String getPurchaseUnitConversionRate() {
        return purchaseUnitConversionRate;
    }

    public void setPurchaseUnitConversionRate(String purchaseUnitConversionRate) {
        this.purchaseUnitConversionRate = purchaseUnitConversionRate;
    }

    public String getItemProperty() {
        return itemProperty;
    }

    public void setItemProperty(String itemProperty) {
        this.itemProperty = itemProperty;
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

    public String getToCurrentStock() {
        return toCurrentStock;
    }

    public void setToCurrentStock(String toCurrentStock) {
        this.toCurrentStock = toCurrentStock;
    }

}
