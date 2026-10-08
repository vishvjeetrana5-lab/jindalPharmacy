package aarogya.pharmacy.schemeMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.overHeadLine.shared.OverHeadITS_Mdl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TxtHMedicine_Mdl extends KeyField implements Serializable, IsSerializable {

    private String itsCode;
    private String medicineCategory;
    private String medicineGroupCode;
    private String medicineGroupName;
    private String skuCode;
    private String skuName;
    private String skuGSTUnit;
    private String saleUnitCode;
    private String saleUnitName;
    private String conversionRate;
    private String itemProperty;
    private String restrictGroup;
    private String itemGstNature;
    private String itsBasedOn;
    private String hsnName;
    private String hsnCode;
    private String discountPer;
    private String purchaseUnitConversionRate;
    private String purchaseUnit;
    private String purchaseUnitName;
    private String compositionCode;
    private String compositionName;
    private String manualCode;
//    private String ibCode;
//    private String batchNo;
//    private String expDate;
//    private String mfgDate;
    private String isSaleRateEditable;
    private String itemPacking;
    private List<OverHeadITS_Mdl> lstITS = new ArrayList<OverHeadITS_Mdl>();
    private int allowDecimalPlaces;
    private int allowSkuDecimalPlaces;
    private boolean allowNegativeStock = false, bestBefore = false, returnableMedicine = false, barcodeActive = false;
    private boolean discountAllow;
    private boolean schemeExists;

    public TxtHMedicine_Mdl() {
    }

    public String getItemPacking() {
        return itemPacking;
    }

    public void setItemPacking(String itemPacking) {
        this.itemPacking = itemPacking;
    }

    public boolean isSchemeExists() {
        return schemeExists;
    }

    public void setSchemeExists(boolean schemeExists) {
        this.schemeExists = schemeExists;
    }

    public String getManualCode() {
        return manualCode;
    }

    public void setManualCode(String manualCode) {
        this.manualCode = manualCode;
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

    public String getMedicineCategory() {
        return medicineCategory;
    }

    public void setMedicineCategory(String medicineCategory) {
        this.medicineCategory = medicineCategory;
    }

    public String getMedicineGroupCode() {
        return medicineGroupCode;
    }

    public void setMedicineGroupCode(String medicineGroupCode) {
        this.medicineGroupCode = medicineGroupCode;
    }

    public String getMedicineGroupName() {
        return medicineGroupName;
    }

    public void setMedicineGroupName(String medicineGroupName) {
        this.medicineGroupName = medicineGroupName;
    }

    public boolean isDiscountAllow() {
        return discountAllow;
    }

    public void setDiscountAllow(boolean discountAllow) {
        this.discountAllow = discountAllow;
    }

    public String getIsSaleRateEditable() {
        return isSaleRateEditable;
    }

    public void setIsSaleRateEditable(String isSaleRateEditable) {
        this.isSaleRateEditable = isSaleRateEditable;
    }

    public String getItsCode() {
        return itsCode;
    }

    public void setItsCode(String itsCode) {
        this.itsCode = itsCode;
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

    public String getItemGstNature() {
        return itemGstNature;
    }

    public void setItemGstNature(String itemGstNature) {
        this.itemGstNature = itemGstNature;
    }

    public String getItsBasedOn() {
        return itsBasedOn;
    }

    public void setItsBasedOn(String itsBasedOn) {
        this.itsBasedOn = itsBasedOn;
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

    public String getSkuGSTUnit() {
        return skuGSTUnit;
    }

    public void setSkuGSTUnit(String skuGSTUnit) {
        this.skuGSTUnit = skuGSTUnit;
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

    public String getConversionRate() {
        return conversionRate;
    }

    public void setConversionRate(String conversionRate) {
        this.conversionRate = conversionRate;
    }

    public boolean isAllowNegativeStock() {
        return allowNegativeStock;
    }

    public void setAllowNegativeStock(boolean allowNegativeStock) {
        this.allowNegativeStock = allowNegativeStock;
    }

    public boolean isBarcodeActive() {
        return barcodeActive;
    }

    public void setBarcodeActive(boolean barcodeActive) {
        this.barcodeActive = barcodeActive;
    }

    public int getAllowDecimalPlaces() {
        return allowDecimalPlaces;
    }

    public void setAllowDecimalPlaces(int allowDecimalPlaces) {
        this.allowDecimalPlaces = allowDecimalPlaces;
    }

    public boolean isBestBefore() {
        return bestBefore;
    }

    public void setBestBefore(boolean bestBefore) {
        this.bestBefore = bestBefore;
    }

    public int getAllowSkuDecimalPlaces() {
        return allowSkuDecimalPlaces;
    }

    public void setAllowSkuDecimalPlaces(int allowSkuDecimalPlaces) {
        this.allowSkuDecimalPlaces = allowSkuDecimalPlaces;
    }

    public boolean isReturnableMedicine() {
        return returnableMedicine;
    }

    public void setReturnableMedicine(boolean returnableMedicine) {
        this.returnableMedicine = returnableMedicine;
    }

    public List<OverHeadITS_Mdl> getLstITS() {
        return lstITS;
    }

    public String getDiscountPer() {
        return discountPer;
    }

    public void setDiscountPer(String discountPer) {
        this.discountPer = discountPer;
    }

    public String getPurchaseUnitConversionRate() {
        return purchaseUnitConversionRate;
    }

    public void setPurchaseUnitConversionRate(String purchaseUnitConversionRate) {
        this.purchaseUnitConversionRate = purchaseUnitConversionRate;
    }

    public String getPurchaseUnit() {
        return purchaseUnit;
    }

    public void setPurchaseUnit(String purchaseUnit) {
        this.purchaseUnit = purchaseUnit;
    }

    public String getPurchaseUnitName() {
        return purchaseUnitName;
    }

    public void setPurchaseUnitName(String purchaseUnitName) {
        this.purchaseUnitName = purchaseUnitName;
    }

//    public String getIbCode() {
//        return ibCode;
//    }
//
//    public void setIbCode(String ibCode) {
//        this.ibCode = ibCode;
//    }
//
//    public String getBatchNo() {
//        return batchNo;
//    }
//
//    public void setBatchNo(String batchNo) {
//        this.batchNo = batchNo;
//    }
//
//    public String getExpDate() {
//        return expDate;
//    }
//
//    public void setExpDate(String expDate) {
//        this.expDate = expDate;
//    }
//
//    public String getMfgDate() {
//        return mfgDate;
//    }
//
//    public void setMfgDate(String mfgDate) {
//        this.mfgDate = mfgDate;
//    }
    public String getRestrictGroup() {
        return restrictGroup;
    }

    public void setRestrictGroup(String restrictGroup) {
        this.restrictGroup = restrictGroup;
    }

    public TxtHMedicine_Mdl clone() {
        TxtHMedicine_Mdl mdl = new TxtHMedicine_Mdl();

        copy(mdl);

        return mdl;
    }

    public void copy(TxtHMedicine_Mdl mdl) {
        mdl.setSharedKeyFieldCode(this.getSharedKeyFieldCode());
        mdl.setSharedKeyFieldName(this.getSharedKeyFieldName());
        mdl.setAllowDecimalPlaces(this.getAllowDecimalPlaces());
        mdl.setAllowNegativeStock(this.isAllowNegativeStock());
        mdl.setBarcodeActive(this.isBarcodeActive());
        mdl.setAllowSkuDecimalPlaces(this.getAllowSkuDecimalPlaces());
//        mdl.setIbCode(this.getIbCode());
//        mdl.setBatchNo(this.getBatchNo());
//        mdl.setMfgDate(this.getMfgDate());
//        mdl.setExpDate(this.getExpDate());
        mdl.setBestBefore(this.isBestBefore());
        mdl.setCompositionCode(this.getCompositionCode());
        mdl.setCompositionName(this.getCompositionName());
        mdl.setConversionRate(this.getConversionRate());
        mdl.setDiscountAllow(this.isDiscountAllow());
        mdl.setDiscountPer(this.getDiscountPer());
        mdl.setHsnCode(this.getHsnCode());
        mdl.setHsnName(this.getHsnName());
        mdl.setIsSaleRateEditable(this.getIsSaleRateEditable());
        mdl.setItemGstNature(this.getItemGstNature());
        mdl.setItemProperty(this.getItemProperty());
        mdl.setItsBasedOn(this.getItsBasedOn());
        mdl.setItsCode(this.getItsCode());
        mdl.setManualCode(this.getManualCode());
        mdl.setMedicineCategory(this.getMedicineCategory());
        mdl.setMedicineGroupCode(this.getMedicineGroupCode());
        mdl.setMedicineGroupName(this.getMedicineGroupName());
        mdl.setPurchaseUnit(this.getPurchaseUnit());
        mdl.setPurchaseUnitConversionRate(this.getPurchaseUnitConversionRate());
        mdl.setPurchaseUnitName(this.getPurchaseUnitName());
        mdl.setRestrictGroup(this.getRestrictGroup());
        mdl.setReturnableMedicine(this.isReturnableMedicine());
        mdl.setSaleUnitCode(this.getSaleUnitCode());
        mdl.setSaleUnitName(this.getSaleUnitName());
        mdl.setSchemeExists(this.isSchemeExists());
        mdl.setSkuCode(this.getSkuCode());
        mdl.setSkuGSTUnit(this.getSkuGSTUnit());
        mdl.setSkuName(this.getSkuName());
        mdl.setItemPacking(this.getItemPacking());
        mdl.getLstITS().addAll(OverHeadITS_Mdl.getITSListClone(this.getLstITS()));
    }

}
