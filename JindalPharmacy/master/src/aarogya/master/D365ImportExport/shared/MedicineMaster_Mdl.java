package aarogya.master.D365ImportExport.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MedicineMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    private String categoryTag;
    private String categoryText;
    private String materialCategoryTag;
    private String materialCategoryText;
    private String strength;
    private String model;
    private String companyTag;
    private String companyText;
    private String medGroupTag;
    private String medGroupText;
    private String restrictGroupText;
    private String restrictGroupTag;
    private String prvSkuTag;
    private String skuTag;
    private String skuText;
    private String statusTag;
    private String statusText;

    private String saleRate;
    private String purchaseRate;
    private String mrp;
    private String schemeOnQty;
    private String schemeFreeQty;
    private String purchaseDisPer;
    private String saleDisPer;
    private String priority;
    private String purchaseDecimalPlaces;

    private String divisionText;
    private String divisionTag;

    private String manualCode;
    private String isSkuEdit;
    private String hsnName;
    private String hsnCode;
    private String its_Code_Purchase;
    private String its_Code_PurchaseRet;
    private String its_Code_Sales;
    private String its_Code_SalesRet;
    private String itsPurName;
    private String itsPurRetName;
    private String itsSaleName;
    private String itsSaleReturnName;
    private String itsPurAcCode;
    private String itsPurRetAcCode;
    private String itsSaleAcCode;
    private String itsSaleReturnAcCode;
    private String itsPurAcName;
    private String itsPurRetAcName;
    private String itsSaleAcName;
    private String itsSaleReturnAcName;

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

    private String bestBeforeText;
    private String bestBeforeTag;
    private String lifeSavingText;
    private String lifeSavingTag;
    private String highValueText;
    private String highValueTag;
    private String flowThroughText;
    private String flowThroughTag;
    private String narcoticsText;
    private String narcoticsTag;
    private String consignmentText;
    private String consignmentTag;
    private String returnableItemText;
    private String returnableItemTag;
    private String billedItemText;
    private String billedItemTag;
    private String gstNatureTag;
    private String gstNatureText;
    private String isItemUsed;
    private String packSize;

    private String prvConversionRatePurchase;
    private String prvConversionRateSale;

    private String prvPurchaseUnitSkuCode;
    private String purchaseUnitSkuCode;
    private String purchaseUnitSkuName;
    private String conversionRatePurchase;
    private String prvSaleUnitSkuCode;
    private String saleUnitSkuCode;
    private String saleUnitSkuName;
    private String conversionRateSale;
    private String allowDecimalPlaces;

    private String allowNegetiveStockCode;
    private String allowNegetiveStockName;

    private String manufacturerCode;
    private String manufacturerName;

    private String displayName;
    private String isSaleRateEditableName;
    private String isSaleRateEditableCode;
    private String isIncludeFreeQtyInSaleRateName;
    private String isIncludeFreeQtyInSaleRateCode;
    private String isDiscountAllowTag;
    private String isDiscountAllowText;
    private String isCommonItemTag;
    private String isCommonItemText;
    private String instruction;
    private String regionalInstruction;
    private String companyShortName;

    private String poReqText;
    private String poReqTag;
    private String indentReqText;
    private String indentReqTag;
    private String allowMRPDiscText;
    private String allowMRPDiscTag;
    private String quotationReqText;
    private String quotationReqTag;
    private boolean isNegativeStock;
    private String barcodeActiveTag;
    private String barcodeActiveText;
    private String modelName;
    private String strengthName;
    private String pharDeptCode;
    private String subCategoryCode;
    private String subCategoryName;
    private String pharDeptName;
    private boolean stockExist;
    //===============================<MJ>=============================//
    private String marginRateTag;
    private String marginRateText;

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

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getSaleDisPer() {
        return saleDisPer;
    }

    public void setSaleDisPer(String saleDisPer) {
        this.saleDisPer = saleDisPer;
    }

    public String getPurchaseDecimalPlaces() {
        return purchaseDecimalPlaces;
    }

    public void setPurchaseDecimalPlaces(String purchaseDecimalPlaces) {
        this.purchaseDecimalPlaces = purchaseDecimalPlaces;
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

    public String getPurchaseDisPer() {
        return purchaseDisPer;
    }

    public void setPurchaseDisPer(String purchaseDisPer) {
        this.purchaseDisPer = purchaseDisPer;
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

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getDivisionText() {
        return divisionText;
    }

    public void setDivisionText(String divisionText) {
        this.divisionText = divisionText;
    }

    public String getDivisionTag() {
        return divisionTag;
    }

    public void setDivisionTag(String divisionTag) {
        this.divisionTag = divisionTag;
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

    public String getCompanyShortName() {
        return companyShortName;
    }

    public void setCompanyShortName(String companyShortName) {
        this.companyShortName = companyShortName;
    }

    public String getPackSize() {
        return packSize;
    }

    public void setPackSize(String packSize) {
        this.packSize = packSize;
    }

    public String getIsDiscountAllowTag() {
        return isDiscountAllowTag;
    }

    public void setIsDiscountAllowTag(String isDiscountAllowTag) {
        this.isDiscountAllowTag = isDiscountAllowTag;
    }

    public String getIsDiscountAllowText() {
        return isDiscountAllowText;
    }

    public void setIsDiscountAllowText(String isDiscountAllowText) {
        this.isDiscountAllowText = isDiscountAllowText;
    }

    public String getIsCommonItemTag() {
        return isCommonItemTag;
    }

    public void setIsCommonItemTag(String isCommonItemTag) {
        this.isCommonItemTag = isCommonItemTag;
    }

    public String getIsCommonItemText() {
        return isCommonItemText;
    }

    public void setIsCommonItemText(String isCommonItemText) {
        this.isCommonItemText = isCommonItemText;
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

    public String getIsIncludeFreeQtyInSaleRateName() {
        return isIncludeFreeQtyInSaleRateName;
    }

    public void setIsIncludeFreeQtyInSaleRateName(String isIncludeFreeQtyInSaleRateName) {
        this.isIncludeFreeQtyInSaleRateName = isIncludeFreeQtyInSaleRateName;
    }

    public String getIsIncludeFreeQtyInSaleRateCode() {
        return isIncludeFreeQtyInSaleRateCode;
    }

    public void setIsIncludeFreeQtyInSaleRateCode(String isIncludeFreeQtyInSaleRateCode) {
        this.isIncludeFreeQtyInSaleRateCode = isIncludeFreeQtyInSaleRateCode;
    }

    public String getIsSaleRateEditableName() {
        return isSaleRateEditableName;
    }

    public void setIsSaleRateEditableName(String isSaleRateEditableName) {
        this.isSaleRateEditableName = isSaleRateEditableName;
    }

    public String getIsSaleRateEditableCode() {
        return isSaleRateEditableCode;
    }

    public void setIsSaleRateEditableCode(String isSaleRateEditableCode) {
        this.isSaleRateEditableCode = isSaleRateEditableCode;
    }

    public String getAllowNegetiveStockCode() {
        return allowNegetiveStockCode;
    }

    public void setAllowNegetiveStockCode(String allowNegetiveStockCode) {
        this.allowNegetiveStockCode = allowNegetiveStockCode;
    }

    public String getAllowNegetiveStockName() {
        return allowNegetiveStockName;
    }

    public void setAllowNegetiveStockName(String allowNegetiveStockName) {
        this.allowNegetiveStockName = allowNegetiveStockName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getMaterialCategoryTag() {
        return materialCategoryTag;
    }

    public void setMaterialCategoryTag(String materialCategoryTag) {
        this.materialCategoryTag = materialCategoryTag;
    }

    public String getMaterialCategoryText() {
        return materialCategoryText;
    }

    public void setMaterialCategoryText(String materialCategoryText) {
        this.materialCategoryText = materialCategoryText;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
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

    public boolean isStockExist() {
        return stockExist;
    }

    public void setStockExist(boolean stockExist) {
        this.stockExist = stockExist;
    }

    public String getPharDeptCode() {
        return pharDeptCode;
    }

    public void setPharDeptCode(String pharDeptCode) {
        this.pharDeptCode = pharDeptCode;
    }

    public String getPharDeptName() {
        return pharDeptName;
    }

    public void setPharDeptName(String pharDeptName) {
        this.pharDeptName = pharDeptName;
    }

    public String getSubCategoryCode() {
        return subCategoryCode;
    }

    public void setSubCategoryCode(String subCategoryCode) {
        this.subCategoryCode = subCategoryCode;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public enum MedicineProperties implements Serializable, IsSerializable {

        PROPERTIES("ED,MRP,BN");

        private String sName;

        public String getProperties() {
            return this.sName;
        }

        public static MedicineProperties getValueFrom() {
            MedicineProperties source = null;

            source = MedicineProperties.PROPERTIES;

            return source;
        }

        private MedicineProperties(String sourceName) {
            this.sName = sourceName;
        }
    }

    public MedicineMaster_Mdl() {
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

    public String getStatusTag() {
        return statusTag;
    }

    public void setStatusTag(String statusTag) {
        this.statusTag = statusTag;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getManualCode() {
        return manualCode;
    }

    public void setManualCode(String manualCode) {
        this.manualCode = manualCode;
    }

    public String getIsSkuEdit() {
        return isSkuEdit;
    }

    public void setIsSkuEdit(String isSkuEdit) {
        this.isSkuEdit = isSkuEdit;
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

    public String getIts_Code_Purchase() {
        return its_Code_Purchase;
    }

    public void setIts_Code_Purchase(String its_Code_Purchase) {
        this.its_Code_Purchase = its_Code_Purchase;
    }

    public String getIts_Code_PurchaseRet() {
        return its_Code_PurchaseRet;
    }

    public void setIts_Code_PurchaseRet(String its_Code_PurchaseRet) {
        this.its_Code_PurchaseRet = its_Code_PurchaseRet;
    }

    public String getIts_Code_Sales() {
        return its_Code_Sales;
    }

    public void setIts_Code_Sales(String its_Code_Sales) {
        this.its_Code_Sales = its_Code_Sales;
    }

    public String getIts_Code_SalesRet() {
        return its_Code_SalesRet;
    }

    public void setIts_Code_SalesRet(String its_Code_SalesRet) {
        this.its_Code_SalesRet = its_Code_SalesRet;
    }

    public String getItsPurName() {
        return itsPurName;
    }

    public void setItsPurName(String itsPurName) {
        this.itsPurName = itsPurName;
    }

    public String getItsPurRetName() {
        return itsPurRetName;
    }

    public void setItsPurRetName(String itsPurRetName) {
        this.itsPurRetName = itsPurRetName;
    }

    public String getItsSaleName() {
        return itsSaleName;
    }

    public void setItsSaleName(String itsSaleName) {
        this.itsSaleName = itsSaleName;
    }

    public String getItsSaleReturnName() {
        return itsSaleReturnName;
    }

    public void setItsSaleReturnName(String itsSaleReturnName) {
        this.itsSaleReturnName = itsSaleReturnName;
    }

    public String getItsPurAcCode() {
        return itsPurAcCode;
    }

    public void setItsPurAcCode(String itsPurAcCode) {
        this.itsPurAcCode = itsPurAcCode;
    }

    public String getItsPurRetAcCode() {
        return itsPurRetAcCode;
    }

    public void setItsPurRetAcCode(String itsPurRetAcCode) {
        this.itsPurRetAcCode = itsPurRetAcCode;
    }

    public String getItsSaleAcCode() {
        return itsSaleAcCode;
    }

    public void setItsSaleAcCode(String itsSaleAcCode) {
        this.itsSaleAcCode = itsSaleAcCode;
    }

    public String getItsSaleReturnAcCode() {
        return itsSaleReturnAcCode;
    }

    public void setItsSaleReturnAcCode(String itsSaleReturnAcCode) {
        this.itsSaleReturnAcCode = itsSaleReturnAcCode;
    }

    public String getItsPurAcName() {
        return itsPurAcName;
    }

    public void setItsPurAcName(String itsPurAcName) {
        this.itsPurAcName = itsPurAcName;
    }

    public String getItsPurRetAcName() {
        return itsPurRetAcName;
    }

    public void setItsPurRetAcName(String itsPurRetAcName) {
        this.itsPurRetAcName = itsPurRetAcName;
    }

    public String getItsSaleAcName() {
        return itsSaleAcName;
    }

    public void setItsSaleAcName(String itsSaleAcName) {
        this.itsSaleAcName = itsSaleAcName;
    }

    public String getItsSaleReturnAcName() {
        return itsSaleReturnAcName;
    }

    public void setItsSaleReturnAcName(String itsSaleReturnAcName) {
        this.itsSaleReturnAcName = itsSaleReturnAcName;
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

    public String getLifeSavingText() {
        return lifeSavingText;
    }

    public void setLifeSavingText(String lifeSavingText) {
        this.lifeSavingText = lifeSavingText;
    }

    public String getLifeSavingTag() {
        return lifeSavingTag;
    }

    public void setLifeSavingTag(String lifeSavingTag) {
        this.lifeSavingTag = lifeSavingTag;
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

    public String getNarcoticsText() {
        return narcoticsText;
    }

    public void setNarcoticsText(String narcoticsText) {
        this.narcoticsText = narcoticsText;
    }

    public String getNarcoticsTag() {
        return narcoticsTag;
    }

    public void setNarcoticsTag(String narcoticsTag) {
        this.narcoticsTag = narcoticsTag;
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

    public String getConversionRatePurchase() {
        return conversionRatePurchase;
    }

    public void setConversionRatePurchase(String conversionRatePurchase) {
        this.conversionRatePurchase = conversionRatePurchase;
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

    public String getConversionRateSale() {
        return conversionRateSale;
    }

    public void setConversionRateSale(String conversionRateSale) {
        this.conversionRateSale = conversionRateSale;
    }

    public String getAllowDecimalPlaces() {
        return allowDecimalPlaces;
    }

    public void setAllowDecimalPlaces(String allowDecimalPlaces) {
        this.allowDecimalPlaces = allowDecimalPlaces;
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

    public String getBarcodeActiveTag() {
        return barcodeActiveTag;
    }

    public void setBarcodeActiveTag(String barcodeActiveTag) {
        this.barcodeActiveTag = barcodeActiveTag;
    }

    public String getBarcodeActiveText() {
        return barcodeActiveText;
    }

    public void setBarcodeActiveText(String barcodeActiveText) {
        this.barcodeActiveText = barcodeActiveText;
    }

    public String getAllowMRPDiscText() {
        return allowMRPDiscText;
    }

    public void setAllowMRPDiscText(String allowMRPDiscText) {
        this.allowMRPDiscText = allowMRPDiscText;
    }

    public String getAllowMRPDiscTag() {
        return allowMRPDiscTag;
    }

    public void setAllowMRPDiscTag(String allowMRPDiscTag) {
        this.allowMRPDiscTag = allowMRPDiscTag;
    }

    public String getMarginRateTag() {
        return marginRateTag;
    }

    public void setMarginRateTag(String marginRateTag) {
        this.marginRateTag = marginRateTag;
    }

    public String getMarginRateText() {
        return marginRateText;
    }

    public void setMarginRateText(String marginRateText) {
        this.marginRateText = marginRateText;
    }

}
