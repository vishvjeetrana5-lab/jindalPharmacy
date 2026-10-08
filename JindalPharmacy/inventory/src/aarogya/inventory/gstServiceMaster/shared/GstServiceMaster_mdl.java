package aarogya.inventory.gstServiceMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GstServiceMaster_mdl extends KeyField implements Serializable, IsSerializable {

    public GstServiceMaster_mdl() {
    }

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

    private String typeName;
    private String typeTag;
    private String typeManualCode;
    private String uniqueId;
    private String mapWithFieldTag;
    private String mapWithField;
    private String serviceEvalTypeCode;
    private String serviceEvalType;
    private String value;
    private String printSrNo;
    private String taxBasedOn;
    private String taxBasedOnTag;
    private String inventoryAcTag;
    private String inventoryAcText;
    private String purchaseAcTag;
    private String purchaseAcText;
    private String salesAcTag;
    private String salesAcText;
    private String purchaseReturnAcTag;
    private String salesReturnAcTag;
    private String purchaseReturnAcText;
    private String salesReturnAcText;
    private String payableAcTag;
    private String payableAcText;

    public String getTaxBasedOnTag() {
        return taxBasedOnTag;
    }

    public void setTaxBasedOnTag(String taxBasedOnTag) {
        this.taxBasedOnTag = taxBasedOnTag;
    }

    private List<GrdItsRateSlab_Mdl> grdRateIts_Mdls = new ArrayList<GrdItsRateSlab_Mdl>();

    public List<GrdItsRateSlab_Mdl> getGrdRateIts_Mdls() {
        return grdRateIts_Mdls;
    }

    public void setGrdRateIts_Mdls(List<GrdItsRateSlab_Mdl> grdRateIts_Mdls) {
        this.grdRateIts_Mdls = grdRateIts_Mdls;
    }

    public String getTaxBasedOn() {
        return taxBasedOn;
    }

    public void setTaxBasedOn(String taxBasedOn) {
        this.taxBasedOn = taxBasedOn;
    }

    public String getTypeTag() {
        return typeTag;
    }

    public void setTypeTag(String typeTag) {
        this.typeTag = typeTag;
    }

    public String getPrintSrNo() {
        return printSrNo;
    }

    public void setPrintSrNo(String printSrNo) {
        this.printSrNo = printSrNo;
    }

    public String getServiceEvalTypeCode() {
        return serviceEvalTypeCode;
    }

    public void setServiceEvalTypeCode(String serviceEvalTypeCode) {
        this.serviceEvalTypeCode = serviceEvalTypeCode;
    }

    public String getServiceEvalType() {
        return serviceEvalType;
    }

    public void setServiceEvalType(String serviceEvalType) {
        this.serviceEvalType = serviceEvalType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMapWithFieldTag() {
        return mapWithFieldTag;
    }

    public void setMapWithFieldTag(String mapWithFieldTag) {
        this.mapWithFieldTag = mapWithFieldTag;
    }

    public String getMapWithField() {
        return mapWithField;
    }

    public void setMapWithField(String mapWithField) {
        this.mapWithField = mapWithField;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

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

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeManualCode() {
        return typeManualCode;
    }

    public void setTypeManualCode(String typeManualCode) {
        this.typeManualCode = typeManualCode;
    }

    public String getInventoryAcTag() {
        return inventoryAcTag;
    }

    public void setInventoryAcTag(String inventoryAcTag) {
        this.inventoryAcTag = inventoryAcTag;
    }

    public String getInventoryAcText() {
        return inventoryAcText;
    }

    public void setInventoryAcText(String inventoryAcText) {
        this.inventoryAcText = inventoryAcText;
    }

    public String getPurchaseAcTag() {
        return purchaseAcTag;
    }

    public void setPurchaseAcTag(String purchaseAcTag) {
        this.purchaseAcTag = purchaseAcTag;
    }

    public String getPurchaseAcText() {
        return purchaseAcText;
    }

    public void setPurchaseAcText(String purchaseAcText) {
        this.purchaseAcText = purchaseAcText;
    }

    public String getSalesAcTag() {
        return salesAcTag;
    }

    public void setSalesAcTag(String salesAcTag) {
        this.salesAcTag = salesAcTag;
    }

    public String getSalesAcText() {
        return salesAcText;
    }

    public void setSalesAcText(String salesAcText) {
        this.salesAcText = salesAcText;
    }

    public String getPurchaseReturnAcTag() {
        return purchaseReturnAcTag;
    }

    public void setPurchaseReturnAcTag(String purchaseReturnAcTag) {
        this.purchaseReturnAcTag = purchaseReturnAcTag;
    }

    public String getSalesReturnAcTag() {
        return salesReturnAcTag;
    }

    public void setSalesReturnAcTag(String salesReturnAcTag) {
        this.salesReturnAcTag = salesReturnAcTag;
    }

    public String getPurchaseReturnAcText() {
        return purchaseReturnAcText;
    }

    public void setPurchaseReturnAcText(String purchaseReturnAcText) {
        this.purchaseReturnAcText = purchaseReturnAcText;
    }

    public String getSalesReturnAcText() {
        return salesReturnAcText;
    }

    public void setSalesReturnAcText(String salesReturnAcText) {
        this.salesReturnAcText = salesReturnAcText;
    }

    public String getPayableAcTag() {
        return payableAcTag;
    }

    public void setPayableAcTag(String payableAcTag) {
        this.payableAcTag = payableAcTag;
    }

    public String getPayableAcText() {
        return payableAcText;
    }

    public void setPayableAcText(String payableAcText) {
        this.payableAcText = payableAcText;
    }
        
}
