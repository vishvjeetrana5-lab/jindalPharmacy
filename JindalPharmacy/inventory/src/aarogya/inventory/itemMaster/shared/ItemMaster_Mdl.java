package aarogya.inventory.itemMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ItemMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    private String categoryTag;
    private String categoryText;
    private String MaterialCategoryTag;
    private String MaterialCategoryText;
    private String MaterialTypeTag;
    private String MaterialTypeText;
    private String companyTag;
    private String companyText;
    private String compositionTag;
    private String compositionText;
    private String medGroupTag;
    private String medGroupText;
    private String restrictGroupText;
    private String restrictGroupTag;
    private String prvSkuTag;
    private String skuTag;
    private String skuText;
    private String itemTypeTag;
    private String itemTypeText;
    private String manualCode;
    private String wtaCode;
    private String hsnName;
    private String hsnCode;
    private String bestBeforeText;
    private String bestBeforeTag;
    private String highValueText;
    private String highValueTag;
    private String flowThroughText;
    private String flowThroughTag;
    private String consignmentText;
    private String consignmentTag;
    private String returnableItemText;
    private String returnableItemTag;
    private String billedItemText;
    private String billedItemTag;

    private String propertyCode;
    private String propertyName;
    private String gstNatureTag;
    private String gstNatureText;
    private String isItemUsed;
    private String prvPurchaseUnitSkuCode;
    private String purchaseUnitSkuCode;
    private String purchaseUnitSkuName;
    private String conversionRatePurchase;
    private String prvConversionRatePurchase;
    private String prvConversionRateSale;
    private String prvSaleUnitSkuCode;
    private String saleUnitSkuCode;
    private String saleUnitSkuName;
    private String conversionRateSale;
    private String allowDecimalPlaces;
    private String allowNegetiveStockName;
    private String allowNegetiveStockCode;

    private String poReqText;
    private String poReqTag;
    private String indentReqText;
    private String indentReqTag;
    private String quotationReqText;
    private String quotationReqTag;
    private boolean isNegativeStock;
    private String itemStatusCode;
    private String itemStatusName;
    private String labRequiredTag;
    private String labRequiredText;
    private String packSize;
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

    private String grIrAcTag;
    private String grIrAcText;
    private String consumptionAcTag;
    private String consumptionAcText;
    private String cogsAcTag;
    private String cogsAcText;
    private String pidAcTag;
    private String pidAcText;
    private String inTransitAcTag;
    private String inTransitAcText;

    private String itemSpecialityText;
    private String itemSpecialityTag;
    private String manufacturerText;
    private String manufacturerTag;
    private String divisionText;
    private String divisionTag;
    private String itemMakeText;
    private String itemMakeTag;
    private String model;
    private String modelName;
    private String strength;
    private String strengthName;
    
    private String  costCenterTag;
    private String  costCenterText;
    private String  budgetHeadTag;
    private String  budgetHeadText;

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getStrengthName() {
        return strengthName;
    }

    public void setStrengthName(String strengthName) {
        this.strengthName = strengthName;
    }
    private String marketingCompany;
    private String marketingCompanyCode;
    private String narcoticsTag;
    private String narcoticsText;

    private String activeTag;
    private String activeText;
    //=========== user controls =========//
    private String statusChangedBy;
    private String statusChangedDate;
    private String tolerancePlus;
    private String toleranceMinus;
    private String itsBasedOnTag;
    private String itsBasedOn;

    private List<GrdItemDepartmentInfo_Mdl> grdItem_Mdl = new ArrayList<GrdItemDepartmentInfo_Mdl>();
    private List<GrdAlternativeItems_Mdl> grdAlternativeItems_Mdls = new ArrayList<GrdAlternativeItems_Mdl>();
    private List<GrdApprovedVendors_Mdl> grdApprovedVendors_Mdls = new ArrayList<GrdApprovedVendors_Mdl>();
    private List<GrdQcParameter_Mdl> grdQcParameter_Mdls = new ArrayList<GrdQcParameter_Mdl>();
    private List<GrdRestrictedDept_Mdl> grdRestrictedDept_Mdls = new ArrayList<GrdRestrictedDept_Mdl>();
    private List<GrdSoundLookAlike_Mdl> grdSoundLookAlike_Mdls = new ArrayList<GrdSoundLookAlike_Mdl>();
    private List<GrdItsRateSlab_Mdl> grdRateIts_Mdls = new ArrayList<GrdItsRateSlab_Mdl>();

    public ItemMaster_Mdl() {
    }

    public List<GrdItsRateSlab_Mdl> getGrdRateIts_Mdls() {
        return grdRateIts_Mdls;
    }

    public void setGrdRateIts_Mdls(List<GrdItsRateSlab_Mdl> grdRateIts_Mdls) {
        this.grdRateIts_Mdls = grdRateIts_Mdls;
    }

    public String getItsBasedOnTag() {
        return itsBasedOnTag;
    }

    public void setItsBasedOnTag(String itsBasedOnTag) {
        this.itsBasedOnTag = itsBasedOnTag;
    }

    public String getItsBasedOn() {
        return itsBasedOn;
    }

    public void setItsBasedOn(String itsBasedOn) {
        this.itsBasedOn = itsBasedOn;
    }

    public String getPrvConversionRatePurchase() {
        return prvConversionRatePurchase;
    }

    public void setPrvConversionRatePurchase(String prvConversionRatePurchase) {
        this.prvConversionRatePurchase = prvConversionRatePurchase;
    }

    public String getPrvConversionRateSale() {
        return prvConversionRateSale;
    }

    public void setPrvConversionRateSale(String prvConversionRateSale) {
        this.prvConversionRateSale = prvConversionRateSale;
    }

    public String getPrvSkuTag() {
        return prvSkuTag;
    }

    public void setPrvSkuTag(String prvSkuTag) {
        this.prvSkuTag = prvSkuTag;
    }

    public String getPrvPurchaseUnitSkuCode() {
        return prvPurchaseUnitSkuCode;
    }

    public void setPrvPurchaseUnitSkuCode(String prvPurchaseUnitSkuCode) {
        this.prvPurchaseUnitSkuCode = prvPurchaseUnitSkuCode;
    }

    public String getPrvSaleUnitSkuCode() {
        return prvSaleUnitSkuCode;
    }

    public void setPrvSaleUnitSkuCode(String prvSaleUnitSkuCode) {
        this.prvSaleUnitSkuCode = prvSaleUnitSkuCode;
    }

    public String getAllowNegetiveStockName() {
        return allowNegetiveStockName;
    }

    public void setAllowNegetiveStockName(String allowNegetiveStockName) {
        this.allowNegetiveStockName = allowNegetiveStockName;
    }

    public String getAllowNegetiveStockCode() {
        return allowNegetiveStockCode;
    }

    public void setAllowNegetiveStockCode(String allowNegetiveStockCode) {
        this.allowNegetiveStockCode = allowNegetiveStockCode;
    }

    public String getPackSize() {
        return packSize;
    }

    public void setPackSize(String packSize) {
        this.packSize = packSize;
    }

    public List<GrdItemDepartmentInfo_Mdl> getGrdItem_Mdl() {
        return grdItem_Mdl;
    }

    public void setGrdItem_Mdl(List<GrdItemDepartmentInfo_Mdl> grdItem_Mdl) {
        this.grdItem_Mdl = grdItem_Mdl;
    }

    public String getManualCode() {
        return manualCode;
    }

    public void setManualCode(String manualCode) {
        this.manualCode = manualCode;
    }

    public String getItemTypeText() {
        return itemTypeText;
    }

    public void setItemTypeText(String itemTypeText) {
        this.itemTypeText = itemTypeText;
    }

    public String getItemTypeTag() {
        return itemTypeTag;
    }

    public void setItemTypeTag(String itemTypeTag) {
        this.itemTypeTag = itemTypeTag;
    }

    public String getCategoryTag() {
        return categoryTag;
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

    public String getCompanyTag() {
        return companyTag;
    }

    public void setCompanyTag(String companyTag) {
        this.companyTag = companyTag;
    }

    public String getCompanyText() {
        return companyText;
    }

    public void setCompanyText(String companyText) {
        this.companyText = companyText;
    }

    public String getCompositionTag() {
        return compositionTag;
    }

    public void setCompositionTag(String compositionTag) {
        this.compositionTag = compositionTag;
    }

    public String getCompositionText() {
        return compositionText;
    }

    public void setCompositionText(String compositionText) {
        this.compositionText = compositionText;
    }

    public String getMedGroupTag() {
        return medGroupTag;
    }

    public void setMedGroupTag(String medGroupTag) {
        this.medGroupTag = medGroupTag;
    }

    public String getMedGroupText() {
        return medGroupText;
    }

    public void setMedGroupText(String medGroupText) {
        this.medGroupText = medGroupText;
    }

    public String getRestrictGroupText() {
        return restrictGroupText;
    }

    public void setRestrictGroupText(String restrictGroupText) {
        this.restrictGroupText = restrictGroupText;
    }

    public String getRestrictGroupTag() {
        return restrictGroupTag;
    }

    public void setRestrictGroupTag(String restrictGroupTag) {
        this.restrictGroupTag = restrictGroupTag;
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

    public List<GrdAlternativeItems_Mdl> getGrdAlternativeItems_Mdls() {
        return grdAlternativeItems_Mdls;
    }

    public void setGrdAlternativeItems_Mdls(List<GrdAlternativeItems_Mdl> grdAlternativeItems_Mdls) {
        this.grdAlternativeItems_Mdls = grdAlternativeItems_Mdls;
    }

    public List<GrdApprovedVendors_Mdl> getGrdApprovedVendors_Mdls() {
        return grdApprovedVendors_Mdls;
    }

    public void setGrdApprovedVendors_Mdls(List<GrdApprovedVendors_Mdl> grdApprovedVendors_Mdls) {
        this.grdApprovedVendors_Mdls = grdApprovedVendors_Mdls;
    }

    public List<GrdRestrictedDept_Mdl> getGrdRestrictedDept_Mdls() {
        return grdRestrictedDept_Mdls;
    }

    public void setGrdRestrictedDept_Mdls(List<GrdRestrictedDept_Mdl> grdRestrictedDept_Mdls) {
        this.grdRestrictedDept_Mdls = grdRestrictedDept_Mdls;
    }

    public List<GrdSoundLookAlike_Mdl> getGrdSoundLookAlike_Mdls() {
        return grdSoundLookAlike_Mdls;
    }

    public void setGrdSoundLookAlike_Mdls(List<GrdSoundLookAlike_Mdl> grdSoundLookAlike_Mdls) {
        this.grdSoundLookAlike_Mdls = grdSoundLookAlike_Mdls;
    }

    public String getBestBeforeText() {
        return bestBeforeText;
    }

    public void setBestBeforeText(String bestBeforeText) {
        this.bestBeforeText = bestBeforeText;
    }

    public String getBestBeforeTag() {
        return bestBeforeTag;
    }

    public void setBestBeforeTag(String bestBeforeTag) {
        this.bestBeforeTag = bestBeforeTag;
    }

    public String getHighValueText() {
        return highValueText;
    }

    public void setHighValueText(String highValueText) {
        this.highValueText = highValueText;
    }

    public String getHighValueTag() {
        return highValueTag;
    }

    public void setHighValueTag(String highValueTag) {
        this.highValueTag = highValueTag;
    }

    public String getFlowThroughText() {
        return flowThroughText;
    }

    public void setFlowThroughText(String flowThroughText) {
        this.flowThroughText = flowThroughText;
    }

    public String getFlowThroughTag() {
        return flowThroughTag;
    }

    public void setFlowThroughTag(String flowThroughTag) {
        this.flowThroughTag = flowThroughTag;
    }

    public String getConsignmentText() {
        return consignmentText;
    }

    public void setConsignmentText(String consignmentText) {
        this.consignmentText = consignmentText;
    }

    public String getConsignmentTag() {
        return consignmentTag;
    }

    public void setConsignmentTag(String consignmentTag) {
        this.consignmentTag = consignmentTag;
    }

    public String getReturnableItemText() {
        return returnableItemText;
    }

    public void setReturnableItemText(String returnableItemText) {
        this.returnableItemText = returnableItemText;
    }

    public String getReturnableItemTag() {
        return returnableItemTag;
    }

    public void setReturnableItemTag(String returnableItemTag) {
        this.returnableItemTag = returnableItemTag;
    }

    public String getBilledItemText() {
        return billedItemText;
    }

    public void setBilledItemText(String billedItemText) {
        this.billedItemText = billedItemText;
    }

    public String getBilledItemTag() {
        return billedItemTag;
    }

    public void setBilledItemTag(String billedItemTag) {
        this.billedItemTag = billedItemTag;
    }

    public String getHsnName() {
        return hsnName;
    }

    public void setHsnName(String hsnName) {
        this.hsnName = hsnName;
    }

    public String getHsnCode() {
        return hsnCode;
    }

    public void setHsnCode(String hsnCode) {
        this.hsnCode = hsnCode;
    }

    public String getPropertyCode() {
        return propertyCode;
    }

    public void setPropertyCode(String propertyCode) {
        this.propertyCode = propertyCode;
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

    public String getConversionRatePurchase() {
        return conversionRatePurchase;
    }

    public void setConversionRatePurchase(String conversionRatePurchase) {
        this.conversionRatePurchase = conversionRatePurchase;
    }

    public String getConversionRateSale() {
        return conversionRateSale;
    }

    public void setConversionRateSale(String conversionRateSale) {
        this.conversionRateSale = conversionRateSale;
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

    public String getAllowDecimalPlaces() {
        return allowDecimalPlaces;
    }

    public void setAllowDecimalPlaces(String allowDecimalPlaces) {
        this.allowDecimalPlaces = allowDecimalPlaces;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPoReqText() {
        return poReqText;
    }

    public void setPoReqText(String poReqText) {
        this.poReqText = poReqText;
    }

    public String getPoReqTag() {
        return poReqTag;
    }

    public void setPoReqTag(String poReqTag) {
        this.poReqTag = poReqTag;
    }

    public String getIndentReqText() {
        return indentReqText;
    }

    public void setIndentReqText(String indentReqText) {
        this.indentReqText = indentReqText;
    }

    public String getIndentReqTag() {
        return indentReqTag;
    }

    public void setIndentReqTag(String indentReqTag) {
        this.indentReqTag = indentReqTag;
    }

    public String getQuotationReqText() {
        return quotationReqText;
    }

    public void setQuotationReqText(String quotationReqText) {
        this.quotationReqText = quotationReqText;
    }

    public String getQuotationReqTag() {
        return quotationReqTag;
    }

    public void setQuotationReqTag(String quotationReqTag) {
        this.quotationReqTag = quotationReqTag;
    }

    public boolean getIsNegativeStock() {
        return isNegativeStock;
    }

    public void setIsNegativeStock(boolean isNegativeStock) {
        this.isNegativeStock = isNegativeStock;
    }

    public String getWtaCode() {
        return wtaCode;
    }

    public void setWtaCode(String wtaCode) {
        this.wtaCode = wtaCode;
    }

    public String getItemStatusCode() {
        return itemStatusCode;
    }

    public void setItemStatusCode(String itemStatusCode) {
        this.itemStatusCode = itemStatusCode;
    }

    public String getItemStatusName() {
        return itemStatusName;
    }

    public void setItemStatusName(String itemStatusName) {
        this.itemStatusName = itemStatusName;
    }

    public String getStatusChangedBy() {
        return statusChangedBy;
    }

    public void setStatusChangedBy(String statusChangedBy) {
        this.statusChangedBy = statusChangedBy;
    }

    public String getStatusChangedDate() {
        return statusChangedDate;
    }

    public void setStatusChangedDate(String statusChangedDate) {
        this.statusChangedDate = statusChangedDate;
    }

    public String getTolerancePlus() {
        return tolerancePlus;
    }

    public void setTolerancePlus(String tolerancePlus) {
        this.tolerancePlus = tolerancePlus;
    }

    public String getToleranceMinus() {
        return toleranceMinus;
    }

    public void setToleranceMinus(String toleranceMinus) {
        this.toleranceMinus = toleranceMinus;
    }

    public String getLabRequiredTag() {
        return labRequiredTag;
    }

    public void setLabRequiredTag(String labRequiredTag) {
        this.labRequiredTag = labRequiredTag;
    }

    public String getLabRequiredText() {
        return labRequiredText;
    }

    public void setLabRequiredText(String labRequiredText) {
        this.labRequiredText = labRequiredText;
    }

    public List<GrdQcParameter_Mdl> getGrdQcParameter_Mdls() {
        return grdQcParameter_Mdls;
    }

    public void setGrdQcParameter_Mdls(List<GrdQcParameter_Mdl> grdQcParameter_Mdls) {
        this.grdQcParameter_Mdls = grdQcParameter_Mdls;
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

    public String getItemSpecialityTag() {
        return itemSpecialityTag;
    }

    public void setItemSpecialityTag(String itemSpecialityTag) {
        this.itemSpecialityTag = itemSpecialityTag;
    }

    public String getManufacturerTag() {
        return manufacturerTag;
    }

    public void setManufacturerTag(String manufacturerTag) {
        this.manufacturerTag = manufacturerTag;
    }

    public String getDivisionTag() {
        return divisionTag;
    }

    public void setDivisionTag(String divisionTag) {
        this.divisionTag = divisionTag;
    }

    public String getItemMakeTag() {
        return itemMakeTag;
    }

    public void setItemMakeTag(String itemMakeTag) {
        this.itemMakeTag = itemMakeTag;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getMarketingCompanyCode() {
        return marketingCompanyCode;
    }

    public void setMarketingCompanyCode(String marketingCompanyCode) {
        this.marketingCompanyCode = marketingCompanyCode;
    }

    public String getNarcoticsTag() {
        return narcoticsTag;
    }

    public void setNarcoticsTag(String narcoticsTag) {
        this.narcoticsTag = narcoticsTag;
    }

    public String getItemSpecialityText() {
        return itemSpecialityText;
    }

    public void setItemSpecialityText(String itemSpecialityText) {
        this.itemSpecialityText = itemSpecialityText;
    }

    public String getManufacturerText() {
        return manufacturerText;
    }

    public void setManufacturerText(String manufacturerText) {
        this.manufacturerText = manufacturerText;
    }

    public String getDivisionText() {
        return divisionText;
    }

    public void setDivisionText(String divisionText) {
        this.divisionText = divisionText;
    }

    public String getItemMakeText() {
        return itemMakeText;
    }

    public void setItemMakeText(String itemMakeText) {
        this.itemMakeText = itemMakeText;
    }

    public String getMarketingCompany() {
        return marketingCompany;
    }

    public void setMarketingCompany(String marketingCompany) {
        this.marketingCompany = marketingCompany;
    }

    public String getNarcoticsText() {
        return narcoticsText;
    }

    public void setNarcoticsText(String narcoticsText) {
        this.narcoticsText = narcoticsText;
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

    public String getGrIrAcTag() {
        return grIrAcTag;
    }

    public void setGrIrAcTag(String grIrAcTag) {
        this.grIrAcTag = grIrAcTag;
    }

    public String getGrIrAcText() {
        return grIrAcText;
    }

    public void setGrIrAcText(String grIrAcText) {
        this.grIrAcText = grIrAcText;
    }

    public String getConsumptionAcTag() {
        return consumptionAcTag;
    }

    public void setConsumptionAcTag(String consumptionAcTag) {
        this.consumptionAcTag = consumptionAcTag;
    }

    public String getConsumptionAcText() {
        return consumptionAcText;
    }

    public void setConsumptionAcText(String consumptionAcText) {
        this.consumptionAcText = consumptionAcText;
    }

    public String getCogsAcTag() {
        return cogsAcTag;
    }

    public void setCogsAcTag(String cogsAcTag) {
        this.cogsAcTag = cogsAcTag;
    }

    public String getCogsAcText() {
        return cogsAcText;
    }

    public void setCogsAcText(String cogsAcText) {
        this.cogsAcText = cogsAcText;
    }

    public String getPidAcTag() {
        return pidAcTag;
    }

    public void setPidAcTag(String pidAcTag) {
        this.pidAcTag = pidAcTag;
    }

    public String getPidAcText() {
        return pidAcText;
    }

    public void setPidAcText(String pidAcText) {
        this.pidAcText = pidAcText;
    }

    public String getInTransitAcTag() {
        return inTransitAcTag;
    }

    public void setInTransitAcTag(String inTransitAcTag) {
        this.inTransitAcTag = inTransitAcTag;
    }

    public String getInTransitAcText() {
        return inTransitAcText;
    }

    public void setInTransitAcText(String inTransitAcText) {
        this.inTransitAcText = inTransitAcText;
    }

    public String getMaterialCategoryTag() {
        return MaterialCategoryTag;
    }

    public void setMaterialCategoryTag(String MaterialCategoryTag) {
        this.MaterialCategoryTag = MaterialCategoryTag;
    }

    public String getMaterialCategoryText() {
        return MaterialCategoryText;
    }

    public void setMaterialCategoryText(String MaterialCategoryText) {
        this.MaterialCategoryText = MaterialCategoryText;
    }

    public String getMaterialTypeTag() {
        return MaterialTypeTag;
    }

    public void setMaterialTypeTag(String MaterialTypeTag) {
        this.MaterialTypeTag = MaterialTypeTag;
    }

    public String getMaterialTypeText() {
        return MaterialTypeText;
    }

    public void setMaterialTypeText(String MaterialTypeText) {
        this.MaterialTypeText = MaterialTypeText;
    }

    public String getCostCenterTag() {
        return costCenterTag;
    }

    public void setCostCenterTag(String costCenterTag) {
        this.costCenterTag = costCenterTag;
    }

    public String getCostCenterText() {
        return costCenterText;
    }

    public void setCostCenterText(String costCenterText) {
        this.costCenterText = costCenterText;
    }

    public String getBudgetHeadTag() {
        return budgetHeadTag;
    }

    public void setBudgetHeadTag(String budgetHeadTag) {
        this.budgetHeadTag = budgetHeadTag;
    }

    public String getBudgetHeadText() {
        return budgetHeadText;
    }

    public void setBudgetHeadText(String budgetHeadText) {
        this.budgetHeadText = budgetHeadText;
    }

}
