/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aarogya.inventory.overHeadItemMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

/**
 *
 * @author Dataman
 */
public class OverHeadItemMaster_mdl extends KeyField implements Serializable, IsSerializable {

    public OverHeadItemMaster_mdl() {
    }

    //sharedKeyFieldCode=docId
    //sharedKeyFieldName=name
    private String categoryTag;
    private String categoryText;
    private String activeTag;
    private String activeText;
    private String groupTag;
    private String groupText;
    private String manualCode;
    private String hsn;
    private String hsnName;
    private String gstItemNature;
    private String skuTag;
    private String skuText;

    private String prvSkuTag;
    private String purchaseUnitSkuCode;
    private String purchaseUnitSkuName;
    private String saleUnitSkuCode;
    private String saleUnitSkuName;

    private String gstNatureTag;
    private String gstNatureText;
    private String isItemUsed;

    public String getActiveTag() {
        return activeTag;
    }

    public void setActiveTag(String activeTag) {
        this.activeTag = activeTag;
    }

    public String getActiveText() {
        return activeText;
    }

    public void setActiveText(String activeText) {
        this.activeText = activeText;
    }

    public String getHsnName() {
        return hsnName;
    }

    public void setHsnName(String hsnName) {
        this.hsnName = hsnName;
    }

    public String getGstNatureTag() {
        return gstNatureTag;
    }

    public void setGstNatureTag(String gstNatureTag) {
        this.gstNatureTag = gstNatureTag;
    }

    public String getGstNatureText() {
        return gstNatureText;
    }

    public void setGstNatureText(String gstNatureText) {
        this.gstNatureText = gstNatureText;
    }

    public String getIsItemUsed() {
        return isItemUsed;
    }

    public void setIsItemUsed(String isItemUsed) {
        this.isItemUsed = isItemUsed;
    }

    public String getCategoryTag() {
        return categoryTag;
    }

    public String getPrvSkuTag() {
        return prvSkuTag;
    }

    public void setPrvSkuTag(String prvSkuTag) {
        this.prvSkuTag = prvSkuTag;
    }

    public String getPurchaseUnitSkuCode() {
        return purchaseUnitSkuCode;
    }

    public void setPurchaseUnitSkuCode(String purchaseUnitSkuCode) {
        this.purchaseUnitSkuCode = purchaseUnitSkuCode;
    }

    public String getPurchaseUnitSkuName() {
        return purchaseUnitSkuName;
    }

    public void setPurchaseUnitSkuName(String purchaseUnitSkuName) {
        this.purchaseUnitSkuName = purchaseUnitSkuName;
    }

    public String getSaleUnitSkuCode() {
        return saleUnitSkuCode;
    }

    public void setSaleUnitSkuCode(String saleUnitSkuCode) {
        this.saleUnitSkuCode = saleUnitSkuCode;
    }

    public String getSaleUnitSkuName() {
        return saleUnitSkuName;
    }

    public void setSaleUnitSkuName(String saleUnitSkuName) {
        this.saleUnitSkuName = saleUnitSkuName;
    }

    public void setCategoryTag(String categoryTag) {
        this.categoryTag = categoryTag;
    }

    public String getCategoryText() {
        return categoryText;
    }

    public void setCategoryText(String categoryText) {
        this.categoryText = categoryText;
    }

    public String getGroupTag() {
        return groupTag;
    }

    public void setGroupTag(String groupTag) {
        this.groupTag = groupTag;
    }

    public String getGroupText() {
        return groupText;
    }

    public void setGroupText(String groupText) {
        this.groupText = groupText;
    }

    public String getManualCode() {
        return manualCode;
    }

    public void setManualCode(String manualCode) {
        this.manualCode = manualCode;
    }

    public String getHsn() {
        return hsn;
    }

    public void setHsn(String hsn) {
        this.hsn = hsn;
    }

    public String getGstItemNature() {
        return gstItemNature;
    }

    public void setGstItemNature(String gstItemNature) {
        this.gstItemNature = gstItemNature;
    }

    public String getSkuTag() {
        return skuTag;
    }

    public void setSkuTag(String skuTag) {
        this.skuTag = skuTag;
    }

    public String getSkuText() {
        return skuText;
    }

    public void setSkuText(String skuText) {
        this.skuText = skuText;
    }

}
