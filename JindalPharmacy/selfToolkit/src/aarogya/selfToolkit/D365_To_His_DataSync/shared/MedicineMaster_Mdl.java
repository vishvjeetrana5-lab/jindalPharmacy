package aarogya.selfToolkit.D365_To_His_DataSync.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class MedicineMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    // ================= BASIC ITEM DETAILS =================
    private String d365_code;
    private String name;
    private String displayName;
    private String manualCode;
    private String skuName;
    private String skuCode;

    private String purchaseUnitName;
    private String saleUnitName;
    private String itemPacking;

    private String purchaseUnitConversionRate;
    private String saleUnitConversionRate;

    private String hsnCode;

    private String modelName;
    private String model;

    private String strengthName;
    private String strength;

    private String propertyList;

    private String itemCategoryCode;
    private String subCategoryCodeName;

    private String materialCategoryCode;
    private String materialTypeCode;

    private String marketingCompanyName;
    private String itemGroupName;
    private String itemMakeCode;

    private String filterItemType;
    private String itemType;

    private String manufacturerCode;

    // ================= FLAGS =================
    private String isActive;
    private String isBarcodeActive;
    private String isConsignment;
    private String isNarcotics;
    private String isReturnableItem;
    private String isSaleRateEditable;
    private String isIncludeFreeQtyInSaleRate;
    private String isDiscountAllow;
    private String isQuotationMandatory;
    private String allowMRPDiscPattern;
    private String marginRateFix;

    private String remark;
    private String its_Code_Purchase;
    private String its_Code_PurchaseRet;
    private String its_Code_Sales;
    private String its_Code_SalesRet;

    // ================= JOINED (HIS MASTER CODES) =================
    private String purchaseUnitCode;
    private String saleUnitCode;
    private String hsnMasterCode;
    private String itemGroupCode;
    private String itemMakeCodeRef;
    private String modelCode;
    private String strengthCode;
    private String subCategoryCodeRef;
    private String itemCategoryCodeRef;
    private String materialTypeCodeRef;
    private String materialCategoryCodeRef;
    private String specialityCode;
    private String medicineSubCategoryCode;

    // ================= D365 TRACKING =================
    private String d365Status;
    private String d365Code;
    private String d365Timestamp;

    // ================= HIS TRACKING =================
    private String hisCode;
    private String hisStatus;
    private String hisTimestamp;

    private String AllowNegativeStock;
    private String ItemGSTNature;
    private String SubCode;
    private String PurchaseAc;
    private String PurchaseReturnAc;
    private String SalesAc;
    private String SalesReturnAc;
    private String GrIrAc;
    private String ConsumptionAc;
    private String CogsAc;
    private String InTransitAc;
    private String IsIndentMandatory;
    private String IsCommon;
    private String MRP;
    private String SaleRate;
    private String PurchaseRate;
    private String SchemeOnQty;
    private String SchemeFreeQty;
    private String PurchaseDiscountPer;
    private String SaleDiscountPer;
    private String RestrictGroup;
    private String RestrictGroupName;
    private String IsLifeSaving;
    private String IsHighValue;
    private String IsFlowThrough;
    private String IsBilledItem;
    private String ItemSpecialityCode;
    private String ItemSpecialityName;
    private String DivisionCode;
    private String DivisionName;
    private String ManufacturerName;
    private String Instruction;
    private String RegionalInstruction;
    private String StatusChangedDate;
    private String TaxPerc;

    public MedicineMaster_Mdl() {
    }

    public String getD365_code() {
        return d365_code;
    }

    public void setD365_code(String d365_code) {
        this.d365_code = d365_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
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

    public String getSaleUnitName() {
        return saleUnitName;
    }

    public void setSaleUnitName(String saleUnitName) {
        this.saleUnitName = saleUnitName;
    }

    public String getItemPacking() {
        return itemPacking;
    }

    public void setItemPacking(String itemPacking) {
        this.itemPacking = itemPacking;
    }

    public String getPurchaseUnitConversionRate() {
        return purchaseUnitConversionRate;
    }

    public void setPurchaseUnitConversionRate(String purchaseUnitConversionRate) {
        this.purchaseUnitConversionRate = purchaseUnitConversionRate;
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

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getStrengthName() {
        return strengthName;
    }

    public void setStrengthName(String strengthName) {
        this.strengthName = strengthName;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getPropertyList() {
        return propertyList;
    }

    public void setPropertyList(String propertyList) {
        this.propertyList = propertyList;
    }

    public String getItemCategoryCode() {
        return itemCategoryCode;
    }

    public void setItemCategoryCode(String itemCategoryCode) {
        this.itemCategoryCode = itemCategoryCode;
    }

    public String getSubCategoryCodeName() {
        return subCategoryCodeName;
    }

    public void setSubCategoryCodeName(String subCategoryCodeName) {
        this.subCategoryCodeName = subCategoryCodeName;
    }

    public String getMaterialCategoryCode() {
        return materialCategoryCode;
    }

    public void setMaterialCategoryCode(String materialCategoryCode) {
        this.materialCategoryCode = materialCategoryCode;
    }

    public String getMaterialTypeCode() {
        return materialTypeCode;
    }

    public void setMaterialTypeCode(String materialTypeCode) {
        this.materialTypeCode = materialTypeCode;
    }

    public String getMarketingCompanyName() {
        return marketingCompanyName;
    }

    public void setMarketingCompanyName(String marketingCompanyName) {
        this.marketingCompanyName = marketingCompanyName;
    }

    public String getItemGroupName() {
        return itemGroupName;
    }

    public void setItemGroupName(String itemGroupName) {
        this.itemGroupName = itemGroupName;
    }

    public String getItemMakeCode() {
        return itemMakeCode;
    }

    public void setItemMakeCode(String itemMakeCode) {
        this.itemMakeCode = itemMakeCode;
    }

    public String getFilterItemType() {
        return filterItemType;
    }

    public void setFilterItemType(String filterItemType) {
        this.filterItemType = filterItemType;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getManufacturerCode() {
        return manufacturerCode;
    }

    public void setManufacturerCode(String manufacturerCode) {
        this.manufacturerCode = manufacturerCode;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getIsBarcodeActive() {
        return isBarcodeActive;
    }

    public void setIsBarcodeActive(String isBarcodeActive) {
        this.isBarcodeActive = isBarcodeActive;
    }

    public String getIsConsignment() {
        return isConsignment;
    }

    public void setIsConsignment(String isConsignment) {
        this.isConsignment = isConsignment;
    }

    public String getIsNarcotics() {
        return isNarcotics;
    }

    public void setIsNarcotics(String isNarcotics) {
        this.isNarcotics = isNarcotics;
    }

    public String getIsReturnableItem() {
        return isReturnableItem;
    }

    public void setIsReturnableItem(String isReturnableItem) {
        this.isReturnableItem = isReturnableItem;
    }

    public String getIsSaleRateEditable() {
        return isSaleRateEditable;
    }

    public void setIsSaleRateEditable(String isSaleRateEditable) {
        this.isSaleRateEditable = isSaleRateEditable;
    }

    public String getIsIncludeFreeQtyInSaleRate() {
        return isIncludeFreeQtyInSaleRate;
    }

    public void setIsIncludeFreeQtyInSaleRate(String isIncludeFreeQtyInSaleRate) {
        this.isIncludeFreeQtyInSaleRate = isIncludeFreeQtyInSaleRate;
    }

    public String getIsDiscountAllow() {
        return isDiscountAllow;
    }

    public void setIsDiscountAllow(String isDiscountAllow) {
        this.isDiscountAllow = isDiscountAllow;
    }

    public String getIsQuotationMandatory() {
        return isQuotationMandatory;
    }

    public void setIsQuotationMandatory(String isQuotationMandatory) {
        this.isQuotationMandatory = isQuotationMandatory;
    }

    public String getAllowMRPDiscPattern() {
        return allowMRPDiscPattern;
    }

    public void setAllowMRPDiscPattern(String allowMRPDiscPattern) {
        this.allowMRPDiscPattern = allowMRPDiscPattern;
    }

    public String getMarginRateFix() {
        return marginRateFix;
    }

    public void setMarginRateFix(String marginRateFix) {
        this.marginRateFix = marginRateFix;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPurchaseUnitCode() {
        return purchaseUnitCode;
    }

    public void setPurchaseUnitCode(String purchaseUnitCode) {
        this.purchaseUnitCode = purchaseUnitCode;
    }

    public String getSaleUnitCode() {
        return saleUnitCode;
    }

    public void setSaleUnitCode(String saleUnitCode) {
        this.saleUnitCode = saleUnitCode;
    }

    public String getHsnMasterCode() {
        return hsnMasterCode;
    }

    public void setHsnMasterCode(String hsnMasterCode) {
        this.hsnMasterCode = hsnMasterCode;
    }

    public String getItemGroupCode() {
        return itemGroupCode;
    }

    public void setItemGroupCode(String itemGroupCode) {
        this.itemGroupCode = itemGroupCode;
    }

    public String getItemMakeCodeRef() {
        return itemMakeCodeRef;
    }

    public void setItemMakeCodeRef(String itemMakeCodeRef) {
        this.itemMakeCodeRef = itemMakeCodeRef;
    }

    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    public String getStrengthCode() {
        return strengthCode;
    }

    public void setStrengthCode(String strengthCode) {
        this.strengthCode = strengthCode;
    }

    public String getSubCategoryCodeRef() {
        return subCategoryCodeRef;
    }

    public void setSubCategoryCodeRef(String subCategoryCodeRef) {
        this.subCategoryCodeRef = subCategoryCodeRef;
    }

    public String getItemCategoryCodeRef() {
        return itemCategoryCodeRef;
    }

    public void setItemCategoryCodeRef(String itemCategoryCodeRef) {
        this.itemCategoryCodeRef = itemCategoryCodeRef;
    }

    public String getMaterialTypeCodeRef() {
        return materialTypeCodeRef;
    }

    public void setMaterialTypeCodeRef(String materialTypeCodeRef) {
        this.materialTypeCodeRef = materialTypeCodeRef;
    }

    public String getMaterialCategoryCodeRef() {
        return materialCategoryCodeRef;
    }

    public void setMaterialCategoryCodeRef(String materialCategoryCodeRef) {
        this.materialCategoryCodeRef = materialCategoryCodeRef;
    }

    public String getSpecialityCode() {
        return specialityCode;
    }

    public void setSpecialityCode(String specialityCode) {
        this.specialityCode = specialityCode;
    }

    public String getMedicineSubCategoryCode() {
        return medicineSubCategoryCode;
    }

    public void setMedicineSubCategoryCode(String medicineSubCategoryCode) {
        this.medicineSubCategoryCode = medicineSubCategoryCode;
    }

    public String getD365Status() {
        return d365Status;
    }

    public void setD365Status(String d365Status) {
        this.d365Status = d365Status;
    }

    public String getD365Code() {
        return d365Code;
    }

    public void setD365Code(String d365Code) {
        this.d365Code = d365Code;
    }

    public String getD365Timestamp() {
        return d365Timestamp;
    }

    public void setD365Timestamp(String d365Timestamp) {
        this.d365Timestamp = d365Timestamp;
    }

    public String getHisCode() {
        return hisCode;
    }

    public void setHisCode(String hisCode) {
        this.hisCode = hisCode;
    }

    public String getHisStatus() {
        return hisStatus;
    }

    public void setHisStatus(String hisStatus) {
        this.hisStatus = hisStatus;
    }

    public String getHisTimestamp() {
        return hisTimestamp;
    }

    public void setHisTimestamp(String hisTimestamp) {
        this.hisTimestamp = hisTimestamp;
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

    public String getAllowNegativeStock() {
        return AllowNegativeStock;
    }

    public void setAllowNegativeStock(String AllowNegativeStock) {
        this.AllowNegativeStock = AllowNegativeStock;
    }

    public String getItemGSTNature() {
        return ItemGSTNature;
    }

    public void setItemGSTNature(String ItemGSTNature) {
        this.ItemGSTNature = ItemGSTNature;
    }

    public String getSubCode() {
        return SubCode;
    }

    public void setSubCode(String SubCode) {
        this.SubCode = SubCode;
    }

    public String getPurchaseAc() {
        return PurchaseAc;
    }

    public void setPurchaseAc(String PurchaseAc) {
        this.PurchaseAc = PurchaseAc;
    }

    public String getPurchaseReturnAc() {
        return PurchaseReturnAc;
    }

    public void setPurchaseReturnAc(String PurchaseReturnAc) {
        this.PurchaseReturnAc = PurchaseReturnAc;
    }

    public String getSalesAc() {
        return SalesAc;
    }

    public void setSalesAc(String SalesAc) {
        this.SalesAc = SalesAc;
    }

    public String getSalesReturnAc() {
        return SalesReturnAc;
    }

    public void setSalesReturnAc(String SalesReturnAc) {
        this.SalesReturnAc = SalesReturnAc;
    }

    public String getGrIrAc() {
        return GrIrAc;
    }

    public void setGrIrAc(String GrIrAc) {
        this.GrIrAc = GrIrAc;
    }

    public String getConsumptionAc() {
        return ConsumptionAc;
    }

    public void setConsumptionAc(String ConsumptionAc) {
        this.ConsumptionAc = ConsumptionAc;
    }

    public String getCogsAc() {
        return CogsAc;
    }

    public void setCogsAc(String CogsAc) {
        this.CogsAc = CogsAc;
    }

    public String getInTransitAc() {
        return InTransitAc;
    }

    public void setInTransitAc(String InTransitAc) {
        this.InTransitAc = InTransitAc;
    }

    public String getIsIndentMandatory() {
        return IsIndentMandatory;
    }

    public void setIsIndentMandatory(String IsIndentMandatory) {
        this.IsIndentMandatory = IsIndentMandatory;
    }

    public String getIsCommon() {
        return IsCommon;
    }

    public void setIsCommon(String IsCommon) {
        this.IsCommon = IsCommon;
    }

    public String getMRP() {
        return MRP;
    }

    public void setMRP(String MRP) {
        this.MRP = MRP;
    }

    public String getSaleRate() {
        return SaleRate;
    }

    public void setSaleRate(String SaleRate) {
        this.SaleRate = SaleRate;
    }

    public String getPurchaseRate() {
        return PurchaseRate;
    }

    public void setPurchaseRate(String PurchaseRate) {
        this.PurchaseRate = PurchaseRate;
    }

    public String getSchemeOnQty() {
        return SchemeOnQty;
    }

    public void setSchemeOnQty(String SchemeOnQty) {
        this.SchemeOnQty = SchemeOnQty;
    }

    public String getSchemeFreeQty() {
        return SchemeFreeQty;
    }

    public void setSchemeFreeQty(String SchemeFreeQty) {
        this.SchemeFreeQty = SchemeFreeQty;
    }

    public String getPurchaseDiscountPer() {
        return PurchaseDiscountPer;
    }

    public void setPurchaseDiscountPer(String PurchaseDiscountPer) {
        this.PurchaseDiscountPer = PurchaseDiscountPer;
    }

    public String getSaleDiscountPer() {
        return SaleDiscountPer;
    }

    public void setSaleDiscountPer(String SaleDiscountPer) {
        this.SaleDiscountPer = SaleDiscountPer;
    }

    public String getRestrictGroup() {
        return RestrictGroup;
    }

    public void setRestrictGroup(String RestrictGroup) {
        this.RestrictGroup = RestrictGroup;
    }

    public String getRestrictGroupName() {
        return RestrictGroupName;
    }

    public void setRestrictGroupName(String RestrictGroupName) {
        this.RestrictGroupName = RestrictGroupName;
    }

    public String getIsLifeSaving() {
        return IsLifeSaving;
    }

    public void setIsLifeSaving(String IsLifeSaving) {
        this.IsLifeSaving = IsLifeSaving;
    }

    public String getIsHighValue() {
        return IsHighValue;
    }

    public void setIsHighValue(String IsHighValue) {
        this.IsHighValue = IsHighValue;
    }

    public String getIsFlowThrough() {
        return IsFlowThrough;
    }

    public void setIsFlowThrough(String IsFlowThrough) {
        this.IsFlowThrough = IsFlowThrough;
    }

    public String getIsBilledItem() {
        return IsBilledItem;
    }

    public void setIsBilledItem(String IsBilledItem) {
        this.IsBilledItem = IsBilledItem;
    }

    public String getItemSpecialityCode() {
        return ItemSpecialityCode;
    }

    public void setItemSpecialityCode(String ItemSpecialityCode) {
        this.ItemSpecialityCode = ItemSpecialityCode;
    }

    public String getItemSpecialityName() {
        return ItemSpecialityName;
    }

    public void setItemSpecialityName(String ItemSpecialityName) {
        this.ItemSpecialityName = ItemSpecialityName;
    }

    public String getDivisionCode() {
        return DivisionCode;
    }

    public void setDivisionCode(String DivisionCode) {
        this.DivisionCode = DivisionCode;
    }

    public String getDivisionName() {
        return DivisionName;
    }

    public void setDivisionName(String DivisionName) {
        this.DivisionName = DivisionName;
    }

    public String getManufacturerName() {
        return ManufacturerName;
    }

    public void setManufacturerName(String ManufacturerName) {
        this.ManufacturerName = ManufacturerName;
    }

    public String getInstruction() {
        return Instruction;
    }

    public void setInstruction(String Instruction) {
        this.Instruction = Instruction;
    }

    public String getRegionalInstruction() {
        return RegionalInstruction;
    }

    public void setRegionalInstruction(String RegionalInstruction) {
        this.RegionalInstruction = RegionalInstruction;
    }

    public String getStatusChangedDate() {
        return StatusChangedDate;
    }

    public void setStatusChangedDate(String StatusChangedDate) {
        this.StatusChangedDate = StatusChangedDate;
    }

    public String getTaxPerc() {
        return TaxPerc;
    }

    public void setTaxPerc(String TaxPerc) {
        this.TaxPerc = TaxPerc;
    }

}
