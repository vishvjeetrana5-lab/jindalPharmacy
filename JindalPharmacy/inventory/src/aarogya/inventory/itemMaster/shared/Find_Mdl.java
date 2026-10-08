package aarogya.inventory.itemMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable,IsSerializable{

    private String category;
    private String company;
    private String composition;
    private String medGroup;
    private String restrictGroup;
    private String sku;
    private String active;
    private String itemType;
    private String itemTypeTag;
    private String manualCode;
    private String hsn;
    private String purchaseUnit;
    private String saleUnit;

    public Find_Mdl() {
    }

    public String getHsn() {
        return hsn;
    }

    public void setHsn(String hsn) {
        this.hsn = hsn;
    }

    public String getPurchaseUnit() {
        return purchaseUnit;
    }

    public void setPurchaseUnit(String purchaseUnit) {
        this.purchaseUnit = purchaseUnit;
    }

    public String getSaleUnit() {
        return saleUnit;
    }

    public void setSaleUnit(String saleUnit) {
        this.saleUnit = saleUnit;
    }

    
    public String getManualCode() {
        return manualCode;
    }

    public void setManualCode(String manualCode) {
        this.manualCode = manualCode;
    }

    public String getItemTypeTag() {
        return itemTypeTag;
    }

    public void setItemTypeTag(String itemTypeTag) {
        this.itemTypeTag = itemTypeTag;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public String getMedGroup() {
        return medGroup;
    }

    public void setMedGroup(String medGroup) {
        this.medGroup = medGroup;
    }

    public String getRestrictGroup() {
        return restrictGroup;
    }

    public void setRestrictGroup(String restrictGroup) {
        this.restrictGroup = restrictGroup;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

}
