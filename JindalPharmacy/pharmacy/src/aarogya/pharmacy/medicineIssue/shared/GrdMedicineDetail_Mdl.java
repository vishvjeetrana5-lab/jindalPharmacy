package aarogya.pharmacy.medicineIssue.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdMedicineDetail_Mdl extends KeyField implements Serializable, IsSerializable {

    private String v_SNo;
    private String quantity;
    private String skuName;
    private String skuCode;
    private String purchaseUnitName;
    private String purchaseUnitCode;
    private String skuDecimalPlaces;
    private String remark;
    private String skuQtyForRate;
    private String requisitionDocId;
    private String requisitionNo;
    private String requisitionV_SNo;
    private String quantityTotalPerItem;

    //Properties
    private String propertyList;
    private String isBestBefore;
    private String ibCode;
    private String batchNo;
    private String manufacturingDate;
    private String expiryDate;
    private String mrp;
    private String saleRate;
    private String skuMrp;
    private String skuSaleRate;
    private boolean allowNegativeStock;
    private boolean barcodeActive;
    private String barCode;
    private String itemCodeCsv;
    
    private String inventoryAc;
    private String consumptionAc;
    private String purchaseValue;

    public GrdMedicineDetail_Mdl() {
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

    public String getQuantityTotalPerItem() {
        return quantityTotalPerItem;
    }

    public void setQuantityTotalPerItem(String quantityTotalPerItem) {
        this.quantityTotalPerItem = quantityTotalPerItem;
    }

    public String getSkuMrp() {
        return skuMrp;
    }

    public void setSkuMrp(String skuMrp) {
        this.skuMrp = skuMrp;
    }

    public String getSkuSaleRate() {
        return skuSaleRate;
    }

    public void setSkuSaleRate(String skuSaleRate) {
        this.skuSaleRate = skuSaleRate;
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

    public String getPurchaseUnitCode() {
        return purchaseUnitCode;
    }

    public void setPurchaseUnitCode(String purchaseUnitCode) {
        this.purchaseUnitCode = purchaseUnitCode;
    }

    public String getSkuQtyForRate() {
        return skuQtyForRate;
    }

    public void setSkuQtyForRate(String skuQtyForRate) {
        this.skuQtyForRate = skuQtyForRate;
    }

    public String getSaleRate() {
        return saleRate;
    }

    public void setSaleRate(String saleRate) {
        this.saleRate = saleRate;
    }

    public String getV_SNo() {
        return v_SNo;
    }

    public void setV_SNo(String v_SNo) {
        this.v_SNo = v_SNo;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
//
//    public String getSkuQtyForRate() {
//        return skuQtyForRate;
//    }
//
//    public void setSkuQtyForRate(String skuQtyForRate) {
//        this.skuQtyForRate = skuQtyForRate;
//    }

    public String getRequisitionDocId() {
        return requisitionDocId;
    }

    public void setRequisitionDocId(String requisitionDocId) {
        this.requisitionDocId = requisitionDocId;
    }

    public String getRequisitionNo() {
        return requisitionNo;
    }

    public void setRequisitionNo(String requisitionNo) {
        this.requisitionNo = requisitionNo;
    }

    public String getRequisitionV_SNo() {
        return requisitionV_SNo;
    }

    public void setRequisitionV_SNo(String requisitionV_SNo) {
        this.requisitionV_SNo = requisitionV_SNo;
    }

    public String getPropertyList() {
        return propertyList;
    }

    public void setPropertyList(String propertyList) {
        this.propertyList = propertyList;
    }

    public String getIsBestBefore() {
        return isBestBefore;
    }

    public void setIsBestBefore(String isBestBefore) {
        this.isBestBefore = isBestBefore;
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

    public String getManufacturingDate() {
        return manufacturingDate;
    }

    public void setManufacturingDate(String manufacturingDate) {
        this.manufacturingDate = manufacturingDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getItemCodeCsv() {
        return itemCodeCsv;
    }

    public void setItemCodeCsv(String itemCodeCsv) {
        this.itemCodeCsv = itemCodeCsv;
    }

    public String getInventoryAc() {
        return inventoryAc;
    }

    public void setInventoryAc(String inventoryAc) {
        this.inventoryAc = inventoryAc;
    }

    public String getConsumptionAc() {
        return consumptionAc;
    }

    public void setConsumptionAc(String consumptionAc) {
        this.consumptionAc = consumptionAc;
    }

    public String getPurchaseValue() {
        return purchaseValue;
    }

    public void setPurchaseValue(String purchaseValue) {
        this.purchaseValue = purchaseValue;
    }

}
