package aarogya.pharmacy.saleBillItemSearch.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.overHeadLine.shared.OverHeadITS_Mdl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GrdSaleBillItemSearch_Mdl extends KeyField implements Serializable, IsSerializable {

    //sharedkeyFieldCode = docid
    //sharedkeyFieldName = recId
    private String itsCode;
    private String itemCode;
    private String itemName;
    private String billDate;
    private String v_Sno;
    private String skuCode;
    private String skuName;
    private String skuGSTUnit;
    private String saleUnitCode;
    private String saleUnitName;
    private String conversionRateSaleUnit;
    private String itemProperty;
    private String restrictGroup;
    private String itemGstNature;
    private String itsBasedOn;
    private String hsnName;
    private String hsnCode;
    private String discountPer;
    private String tempDiscountPer;
    private String purchaseUnitConversionRate;
    private String purchaseUnitCode;
    private String purchaseUnitName;
    private String ibCode;
    private String batchNo;
    private String expDate;
    private String mfgDate;
    private String isSaleRateEditable;
    private List<OverHeadITS_Mdl> lstITS = new ArrayList<OverHeadITS_Mdl>();
    private int allowSaleUnitDecimalPlaces;
    private int allowSkuDecimalPlaces;
    private boolean allowNegativeStock = false, bestBefore = false, returnableMedicine = false;
    private String saleAcCode;
    private String quantity;
//    private String freeQuantity;
    private String netAmount;
    private String skuMrp;
    private String skuRate;
    private String rate;
    private String mrp;
    private String quantitySKU;
//    private String freeQuantitySKU;
    private String isChecked;
    private String patientName;
    private String grossPurchaseRate;

    public GrdSaleBillItemSearch_Mdl() {
    }

//    public String getFreeQuantity() {
//        return freeQuantity;
//    }
//
//    public void setFreeQuantity(String freeQuantity) {
//        this.freeQuantity = freeQuantity;
//    }
//
//    public String getFreeQuantitySKU() {
//        return freeQuantitySKU;
//    }
//
//    public void setFreeQuantitySKU(String freeQuantitySKU) {
//        this.freeQuantitySKU = freeQuantitySKU;
//    }
    public String getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(String netAmount) {
        this.netAmount = netAmount;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }

    public String getSkuMrp() {
        return skuMrp;
    }

    public void setSkuMrp(String skuMrp) {
        this.skuMrp = skuMrp;
    }

    public String getSkuRate() {
        return skuRate;
    }

    public void setSkuRate(String skuRate) {
        this.skuRate = skuRate;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getQuantitySKU() {
        return quantitySKU;
    }

    public void setQuantitySKU(String quantitySKU) {
        this.quantitySKU = quantitySKU;
    }

    public String getV_Sno() {
        return v_Sno;
    }

    public void setV_Sno(String v_Sno) {
        this.v_Sno = v_Sno;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSaleAcCode() {
        return saleAcCode;
    }

    public void setSaleAcCode(String saleAcCode) {
        this.saleAcCode = saleAcCode;
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

    public String getConversionRateSaleUnit() {
        return conversionRateSaleUnit;
    }

    public void setConversionRateSaleUnit(String conversionRateSaleUnit) {
        this.conversionRateSaleUnit = conversionRateSaleUnit;
    }

    public String getItemProperty() {
        return itemProperty;
    }

    public void setItemProperty(String itemProperty) {
        this.itemProperty = itemProperty;
    }

    public String getRestrictGroup() {
        return restrictGroup;
    }

    public void setRestrictGroup(String restrictGroup) {
        this.restrictGroup = restrictGroup;
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

    public String getDiscountPer() {
        return discountPer;
    }

    public void setDiscountPer(String discountPer) {
        this.discountPer = discountPer;
    }

    public String getTempDiscountPer() {
        return tempDiscountPer;
    }

    public void setTempDiscountPer(String tempDiscountPer) {
        this.tempDiscountPer = tempDiscountPer;
    }

    public String getPurchaseUnitConversionRate() {
        return purchaseUnitConversionRate;
    }

    public void setPurchaseUnitConversionRate(String purchaseUnitConversionRate) {
        this.purchaseUnitConversionRate = purchaseUnitConversionRate;
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

    public String getIbCode() {
        return ibCode;
    }

    public void setIbCode(String ibCode) {
        this.ibCode = ibCode;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public String getMfgDate() {
        return mfgDate;
    }

    public void setMfgDate(String mfgDate) {
        this.mfgDate = mfgDate;
    }

    public String getIsSaleRateEditable() {
        return isSaleRateEditable;
    }

    public void setIsSaleRateEditable(String isSaleRateEditable) {
        this.isSaleRateEditable = isSaleRateEditable;
    }

    public List<OverHeadITS_Mdl> getLstITS() {
        return lstITS;
    }

    public void setLstITS(List<OverHeadITS_Mdl> lstITS) {
        this.lstITS = lstITS;
    }

    public int getAllowSaleUnitDecimalPlaces() {
        return allowSaleUnitDecimalPlaces;
    }

    public void setAllowSaleUnitDecimalPlaces(int allowSaleUnitDecimalPlaces) {
        this.allowSaleUnitDecimalPlaces = allowSaleUnitDecimalPlaces;
    }

    public int getAllowSkuDecimalPlaces() {
        return allowSkuDecimalPlaces;
    }

    public void setAllowSkuDecimalPlaces(int allowSkuDecimalPlaces) {
        this.allowSkuDecimalPlaces = allowSkuDecimalPlaces;
    }

    public boolean isAllowNegativeStock() {
        return allowNegativeStock;
    }

    public void setAllowNegativeStock(boolean allowNegativeStock) {
        this.allowNegativeStock = allowNegativeStock;
    }

    public boolean isBestBefore() {
        return bestBefore;
    }

    public void setBestBefore(boolean bestBefore) {
        this.bestBefore = bestBefore;
    }

    public boolean isReturnableMedicine() {
        return returnableMedicine;
    }

    public void setReturnableMedicine(boolean returnableMedicine) {
        this.returnableMedicine = returnableMedicine;
    }

    public String getGrossPurchaseRate() {
        return grossPurchaseRate;
    }

    public void setGrossPurchaseRate(String grossPurchaseRate) {
        this.grossPurchaseRate = grossPurchaseRate;
    }

}
