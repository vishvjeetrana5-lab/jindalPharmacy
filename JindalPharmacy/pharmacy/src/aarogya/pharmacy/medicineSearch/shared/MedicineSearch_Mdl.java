package aarogya.pharmacy.medicineSearch.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class MedicineSearch_Mdl extends KeyField implements Serializable, IsSerializable {

    private String compositionMatchStatus;
    private String displayName;
    private String manualCode;
    private String pack;
    private String makeCode;
    private String makeName;
    private String currentStock;
    private String skuCode;
    private String skuName;
    private String skuDecimalPlaces;
    private String purchaseUnitCode;
    private String purchaseUnitName;
    private String purchaseUnitDecimalPlaces;
    private String purchaseUnitConversionRate;
    private String saleUnitCode;
    private String saleUnitName;
    private String saleUnitDecimalPlaces;
    private String saleUnitConversionRate;
    private String hsnCode;
    private String hsnName;
    private String itemGSTNature;
    private String itemGSTNatureName;
    private String itsBasedOn;
    private String isBestBefore;
    private String isBarcodeActive;
    private String mrp;
    private String saleRate;
    private String purchaseRate;
    private String approvedPurchaseRate;
    private String saleDiscountPer;
    private String schemeOnQty;
    private String schemeFreeQty;
    private String purchaseDiscountPer;
    private String propertyList;
    private String isIncludeFreeQtyInSaleRate;
    private String categoryCode;
    private String categoryName;
    private String groupCode;
    private String groupName;
    private String restrictedGroupCode;
    private String restrictedGroupName;
    private String divisionCode;
    private String divisionName;
    private String minStock;
    private String maxStock;
    private String reOrder;
    private String binName;
    private String compositionCodeCsv;
    private String compositionCodeWithPowerCsv;
    private String compositionNameWithPowerCsv;
    private String manufacturerCode;
    private String manufacturerName;
    private String instruction;
    private String regionalInstruction;
    private String matchtStatus;
    private String compositionCode;
    private String compositionName;
    private String isSaleRateEditable;
    private String isReturnableItem;
    private String isAllowNegativeStock;
    private String isDiscountAllow;
    private String saleSchemeCode;
    private String purchaseUnitGstUnitCode;
    private String saleUnitGstUnitCode;
    private String itsPurchase;
    private String isSelected;
    private String priority;
    private String marginPercent;
    private String saleRateProffitMargine;
    private String isMarkupApplicable;
    
    private String inventoryAc;
    private String grIrAc;
    private String consumptionAc;
    private String cogsAc;
    private String pidAc;
    private String inTransitAc;
    private String remark;
    private String approvedMRP;

    public MedicineSearch_Mdl() {
    }

    public String getSaleUnitGstUnitCode() {
        return saleUnitGstUnitCode;
    }

    public void setSaleUnitGstUnitCode(String saleUnitGstUnitCode) {
        this.saleUnitGstUnitCode = saleUnitGstUnitCode;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getMarginPercent() {
        return marginPercent;
    }

    public void setMarginPercent(String marginPercent) {
        this.marginPercent = marginPercent;
    }

    public String getCompositionMatchStatus() {
        return compositionMatchStatus;
    }

    public void setCompositionMatchStatus(String compositionMatchStatus) {
        this.compositionMatchStatus = compositionMatchStatus;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getManualCode() {
        return manualCode;
    }

    public void setManualCode(String manualCode) {
        this.manualCode = manualCode;
    }

    public String getPack() {
        return pack;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }

    public String getMakeCode() {
        return makeCode;
    }

    public void setMakeCode(String makeCode) {
        this.makeCode = makeCode;
    }

    public String getMakeName() {
        return makeName;
    }

    public void setMakeName(String makeName) {
        this.makeName = makeName;
    }

    public String getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(String currentStock) {
        this.currentStock = currentStock;
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

    public String getPurchaseUnitDecimalPlaces() {
        return purchaseUnitDecimalPlaces;
    }

    public void setPurchaseUnitDecimalPlaces(String purchaseUnitDecimalPlaces) {
        this.purchaseUnitDecimalPlaces = purchaseUnitDecimalPlaces;
    }

    public String getPurchaseUnitConversionRate() {
        return purchaseUnitConversionRate;
    }

    public void setPurchaseUnitConversionRate(String purchaseUnitConversionRate) {
        this.purchaseUnitConversionRate = purchaseUnitConversionRate;
    }

    public String getSaleUnitCode() {
        return saleUnitCode;
    }

    public void setSaleUnitCode(String saleUnitCode) {
        this.saleUnitCode = saleUnitCode;
    }

    public String getSaleUnitName() {
        return saleUnitName;
    }

    public void setSaleUnitName(String saleUnitName) {
        this.saleUnitName = saleUnitName;
    }

    public String getSaleUnitDecimalPlaces() {
        return saleUnitDecimalPlaces;
    }

    public void setSaleUnitDecimalPlaces(String saleUnitDecimalPlaces) {
        this.saleUnitDecimalPlaces = saleUnitDecimalPlaces;
    }

    public String getSaleUnitConversionRate() {
        return saleUnitConversionRate;
    }

    public void setSaleUnitConversionRate(String saleUnitConversionRate) {
        this.saleUnitConversionRate = saleUnitConversionRate;
    }

    public String getHsnCode() {
        return hsnCode;
    }

    public void setHsnCode(String hsnCode) {
        this.hsnCode = hsnCode;
    }

    public String getHsnName() {
        return hsnName;
    }

    public void setHsnName(String hsnName) {
        this.hsnName = hsnName;
    }

    public String getItemGSTNature() {
        return itemGSTNature;
    }

    public void setItemGSTNature(String itemGSTNature) {
        this.itemGSTNature = itemGSTNature;
    }

    public String getItemGSTNatureName() {
        return itemGSTNatureName;
    }

    public void setItemGSTNatureName(String itemGSTNatureName) {
        this.itemGSTNatureName = itemGSTNatureName;
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

    public String getIsBarcodeActive() {
        return isBarcodeActive;
    }

    public void setIsBarcodeActive(String isBarcodeActive) {
        this.isBarcodeActive = isBarcodeActive;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getSaleRate() {
        return saleRate;
    }

    public void setSaleRate(String saleRate) {
        this.saleRate = saleRate;
    }

    public String getPurchaseRate() {
        return purchaseRate;
    }

    public void setPurchaseRate(String purchaseRate) {
        this.purchaseRate = purchaseRate;
    }

    public String getSaleDiscountPer() {
        return saleDiscountPer;
    }

    public void setSaleDiscountPer(String saleDiscountPer) {
        this.saleDiscountPer = saleDiscountPer;
    }

    public String getSchemeOnQty() {
        return schemeOnQty;
    }

    public void setSchemeOnQty(String schemeOnQty) {
        this.schemeOnQty = schemeOnQty;
    }

    public String getSchemeFreeQty() {
        return schemeFreeQty;
    }

    public void setSchemeFreeQty(String schemeFreeQty) {
        this.schemeFreeQty = schemeFreeQty;
    }

    public String getPurchaseDiscountPer() {
        return purchaseDiscountPer;
    }

    public void setPurchaseDiscountPer(String purchaseDiscountPer) {
        this.purchaseDiscountPer = purchaseDiscountPer;
    }

    public String getPropertyList() {
        return propertyList;
    }

    public void setPropertyList(String propertyList) {
        this.propertyList = propertyList;
    }

    public String getIsIncludeFreeQtyInSaleRate() {
        return isIncludeFreeQtyInSaleRate;
    }

    public void setIsIncludeFreeQtyInSaleRate(String isIncludeFreeQtyInSaleRate) {
        this.isIncludeFreeQtyInSaleRate = isIncludeFreeQtyInSaleRate;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getRestrictedGroupCode() {
        return restrictedGroupCode;
    }

    public void setRestrictedGroupCode(String restrictedGroupCode) {
        this.restrictedGroupCode = restrictedGroupCode;
    }

    public String getRestrictedGroupName() {
        return restrictedGroupName;
    }

    public void setRestrictedGroupName(String restrictedGroupName) {
        this.restrictedGroupName = restrictedGroupName;
    }

    public String getDivisionCode() {
        return divisionCode;
    }

    public void setDivisionCode(String divisionCode) {
        this.divisionCode = divisionCode;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public String getMinStock() {
        return minStock;
    }

    public void setMinStock(String minStock) {
        this.minStock = minStock;
    }

    public String getMaxStock() {
        return maxStock;
    }

    public void setMaxStock(String maxStock) {
        this.maxStock = maxStock;
    }

    public String getCompositionCodeCsv() {
        return compositionCodeCsv;
    }

    public void setCompositionCodeCsv(String compositionCodeCsv) {
        this.compositionCodeCsv = compositionCodeCsv;
    }

    public String getCompositionCodeWithPowerCsv() {
        return compositionCodeWithPowerCsv;
    }

    public void setCompositionCodeWithPowerCsv(String compositionCodeWithPowerCsv) {
        this.compositionCodeWithPowerCsv = compositionCodeWithPowerCsv;
    }

    public String getCompositionNameWithPowerCsv() {
        return compositionNameWithPowerCsv;
    }

    public void setCompositionNameWithPowerCsv(String compositionNameWithPowerCsv) {
        this.compositionNameWithPowerCsv = compositionNameWithPowerCsv;
    }

    public String getReOrder() {
        return reOrder;
    }

    public void setReOrder(String reOrder) {
        this.reOrder = reOrder;
    }

    public String getBinName() {
        return binName;
    }

    public void setBinName(String binName) {
        this.binName = binName;
    }

    public String getManufacturerCode() {
        return manufacturerCode;
    }

    public void setManufacturerCode(String manufacturerCode) {
        this.manufacturerCode = manufacturerCode;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getRegionalInstruction() {
        return regionalInstruction;
    }

    public void setRegionalInstruction(String regionalInstruction) {
        this.regionalInstruction = regionalInstruction;
    }

    public String getMatchtStatus() {
        return matchtStatus;
    }

    public void setMatchtStatus(String matchtStatus) {
        this.matchtStatus = matchtStatus;
    }

    public String getCompositionCode() {
        return compositionCode;
    }

    public void setCompositionCode(String compositionCode) {
        this.compositionCode = compositionCode;
    }

    public String getCompositionName() {
        return compositionName;
    }

    public void setCompositionName(String compositionName) {
        this.compositionName = compositionName;
    }

    public String getIsSaleRateEditable() {
        return isSaleRateEditable;
    }

    public void setIsSaleRateEditable(String isSaleRateEditable) {
        this.isSaleRateEditable = isSaleRateEditable;
    }

    public String getIsReturnableItem() {
        return isReturnableItem;
    }

    public void setIsReturnableItem(String isReturnableItem) {
        this.isReturnableItem = isReturnableItem;
    }

    public String getIsAllowNegativeStock() {
        return isAllowNegativeStock;
    }

    public void setIsAllowNegativeStock(String isAllowNegativeStock) {
        this.isAllowNegativeStock = isAllowNegativeStock;
    }

    public String getIsDiscountAllow() {
        return isDiscountAllow;
    }

    public void setIsDiscountAllow(String isDiscountAllow) {
        this.isDiscountAllow = isDiscountAllow;
    }

    public String getSaleSchemeCode() {
        return saleSchemeCode;
    }

    public void setSaleSchemeCode(String saleSchemeCode) {
        this.saleSchemeCode = saleSchemeCode;
    }

    public String getPurchaseUnitGstUnitCode() {
        return purchaseUnitGstUnitCode;
    }

    public void setPurchaseUnitGstUnitCode(String purchaseUnitGstUnitCode) {
        this.purchaseUnitGstUnitCode = purchaseUnitGstUnitCode;
    }

    public String getItsPurchase() {
        return itsPurchase;
    }

    public void setItsPurchase(String itsPurchase) {
        this.itsPurchase = itsPurchase;
    }

    public String getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(String isSelected) {
        this.isSelected = isSelected;
    }

    public String getSaleRateProffitMargine() {
        return saleRateProffitMargine;
    }

    public void setSaleRateProffitMargine(String saleRateProffitMargine) {
        this.saleRateProffitMargine = saleRateProffitMargine;
    }

    public String getInventoryAc() {
        return inventoryAc;
    }

    public void setInventoryAc(String inventoryAc) {
        this.inventoryAc = inventoryAc;
    }

    public String getGrIrAc() {
        return grIrAc;
    }

    public void setGrIrAc(String grIrAc) {
        this.grIrAc = grIrAc;
    }

    public String getConsumptionAc() {
        return consumptionAc;
    }

    public void setConsumptionAc(String consumptionAc) {
        this.consumptionAc = consumptionAc;
    }

    public String getCogsAc() {
        return cogsAc;
    }

    public void setCogsAc(String cogsAc) {
        this.cogsAc = cogsAc;
    }

    public String getPidAc() {
        return pidAc;
    }

    public void setPidAc(String pidAc) {
        this.pidAc = pidAc;
    }

    public String getInTransitAc() {
        return inTransitAc;
    }

    public void setInTransitAc(String inTransitAc) {
        this.inTransitAc = inTransitAc;
    }

    public String getApprovedPurchaseRate() {
        return approvedPurchaseRate;
    }

    public void setApprovedPurchaseRate(String approvedPurchaseRate) {
        this.approvedPurchaseRate = approvedPurchaseRate;
    }

    public String getIsMarkupApplicable() {
        return isMarkupApplicable;
    }

    public void setIsMarkupApplicable(String isMarkupApplicable) {
        this.isMarkupApplicable = isMarkupApplicable;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getApprovedMRP() {
        return approvedMRP;
    }

    public void setApprovedMRP(String approvedMRP) {
        this.approvedMRP = approvedMRP;
    }

}
