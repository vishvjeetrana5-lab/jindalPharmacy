package aarogya.inventory.requisition.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHItem_Mdl extends KeyField implements Serializable, IsSerializable {

    private String manualCode;
    private String skuCode;
    private String skuName;
    private String itemGroup;
    private String itemCategory;
    private String itemProperty;
    private String allowDecimalPlaces;
    private String isBestBefore;
private String curStkOfToDepartment;

    public String getManualCode() {
        return manualCode;
    }

    public void setManualCode(String manualCode) {
        this.manualCode = manualCode;
    }

    public String getIsBestBefore() {
        return isBestBefore;
    }

    public void setIsBestBefore(String isBestBefore) {
        this.isBestBefore = isBestBefore;
    }

    public String getAllowDecimalPlaces() {
        return allowDecimalPlaces;
    }

    public void setAllowDecimalPlaces(String allowDecimalPlaces) {
        this.allowDecimalPlaces = allowDecimalPlaces;
    }

    public TxtHItem_Mdl() {
    }

    public String getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(String itemGroup) {
        this.itemGroup = itemGroup;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
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

    public String getItemProperty() {
        return itemProperty;
    }

    public void setItemProperty(String itemProperty) {
        this.itemProperty = itemProperty;
    }

    public String getCurStkOfToDepartment() {
        return curStkOfToDepartment;
    }

    public void setCurStkOfToDepartment(String curStkOfToDepartment) {
        this.curStkOfToDepartment = curStkOfToDepartment;
    }

}
