package aarogya.inventory.itemOpening.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdItemDetail_Mdl extends KeyField implements Serializable, IsSerializable {

    private String v_SNo;
    private String quantity;
    private String skuName;
    private String skuCode;
    private String refChallanNo;
    private String refChallanDate;
    private String rate;
    private String skuValue;
    private String skuRate;
    private String skuDecimalPlaces;
    private String remark;
    private String propertyDisplay;
    private String skuQtyForRate;
    private String purchaseUnitName;
    private String purchaseUnitCode;
    private String purchaseUnitDecimalPlaces;
    private String purchaseUnitQuantity;

    //IAP
    private String isubCode;
    
    //Properties
    private String propertyList;
    private String isBestBefore;
    private String ibCode;
    private String batchNo;
    private String lotNo;
    private String manufacturingDate;
    private String bestBeforeDays;
    private String expiryDate;
    private String colourCode;
    private String colourName;
    private String sizeCode;
    private String sizeName;
    private String mrp;
    private String skuMrp;
    private String itemMakeCode;
    private String itemMakeName;

    public GrdItemDetail_Mdl() {
    }

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
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

    public String getSkuRate() {
        return skuRate;
    }

    public void setSkuRate(String skuRate) {
        this.skuRate = skuRate;
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

    public String getRefChallanNo() {
        return refChallanNo;
    }

    public void setRefChallanNo(String refChallanNo) {
        this.refChallanNo = refChallanNo;
    }

    public String getRefChallanDate() {
        return refChallanDate;
    }

    public void setRefChallanDate(String refChallanDate) {
        this.refChallanDate = refChallanDate;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getSkuValue() {
        return skuValue;
    }

    public void setSkuValue(String skuValue) {
        this.skuValue = skuValue;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPropertyDisplay() {
        return propertyDisplay;
    }

    public void setPropertyDisplay(String propertyDisplay) {
        this.propertyDisplay = propertyDisplay;
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

    public String getBestBeforeDays() {
        return bestBeforeDays;
    }

    public void setBestBeforeDays(String bestBeforeDays) {
        this.bestBeforeDays = bestBeforeDays;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getColourCode() {
        return colourCode;
    }

    public void setColourCode(String colourCode) {
        this.colourCode = colourCode;
    }

    public String getColourName() {
        return colourName;
    }

    public void setColourName(String colourName) {
        this.colourName = colourName;
    }

    public String getSizeCode() {
        return sizeCode;
    }

    public void setSizeCode(String sizeCode) {
        this.sizeCode = sizeCode;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getSkuMrp() {
        return skuMrp;
    }

    public void setSkuMrp(String skuMrp) {
        this.skuMrp = skuMrp;
    }

    public String getIbCode() {
        return ibCode;
    }

    public void setIbCode(String ibCode) {
        this.ibCode = ibCode;
    }

    public String getV_SNo() {
        return v_SNo;
    }

    public void setV_SNo(String v_SNo) {
        this.v_SNo = v_SNo;
    }

    public String getSkuDecimalPlaces() {
        return skuDecimalPlaces;
    }

    public void setSkuDecimalPlaces(String skuDecimalPlaces) {
        this.skuDecimalPlaces = skuDecimalPlaces;
    }

    public String getSkuQtyForRate() {
        return skuQtyForRate;
    }

    public void setSkuQtyForRate(String skuQtyForRate) {
        this.skuQtyForRate = skuQtyForRate;
    }

    public String getPurchaseUnitDecimalPlaces() {
        return purchaseUnitDecimalPlaces;
    }

    public void setPurchaseUnitDecimalPlaces(String purchaseUnitDecimalPlaces) {
        this.purchaseUnitDecimalPlaces = purchaseUnitDecimalPlaces;
    }

    public String getPurchaseUnitQuantity() {
        return purchaseUnitQuantity;
    }

    public void setPurchaseUnitQuantity(String purchaseUnitQuantity) {
        this.purchaseUnitQuantity = purchaseUnitQuantity;
    }

    public String getItemMakeCode() {
        return itemMakeCode;
    }

    public void setItemMakeCode(String itemMakeCode) {
        this.itemMakeCode = itemMakeCode;
    }

    public String getItemMakeName() {
        return itemMakeName;
    }

    public void setItemMakeName(String itemMakeName) {
        this.itemMakeName = itemMakeName;
    }

    public String getIsubCode() {
        return isubCode;
    }

    public void setIsubCode(String isubCode) {
        this.isubCode = isubCode;
    }

    public GrdItemDetail_Mdl clone() {
        GrdItemDetail_Mdl grdItemDetail_Mdl = new GrdItemDetail_Mdl();

        copy(grdItemDetail_Mdl);

        return grdItemDetail_Mdl;
    }

    public void copy(GrdItemDetail_Mdl grdItemDetail_Mdl) {
        grdItemDetail_Mdl.setSharedKeyFieldCode(this.getSharedKeyFieldCode());
        grdItemDetail_Mdl.setSharedKeyFieldName(this.getSharedKeyFieldName());
        grdItemDetail_Mdl.setSharedPreparedBy(this.getSharedPreparedBy());
        grdItemDetail_Mdl.setSharedModifiedBy(this.getSharedModifiedBy());
        grdItemDetail_Mdl.setV_SNo(this.getV_SNo());
        grdItemDetail_Mdl.setQuantity(this.getQuantity());
        grdItemDetail_Mdl.setSkuName(this.getSkuName());
        grdItemDetail_Mdl.setSkuCode(this.getSkuCode());
        grdItemDetail_Mdl.setRefChallanNo(this.getRefChallanNo());
        grdItemDetail_Mdl.setRefChallanDate(this.getRefChallanDate());
        grdItemDetail_Mdl.setRate(this.getRate());
        grdItemDetail_Mdl.setSkuValue(this.getSkuValue());
        grdItemDetail_Mdl.setSkuDecimalPlaces(this.getSkuDecimalPlaces());
        grdItemDetail_Mdl.setRemark(this.getRemark());
        grdItemDetail_Mdl.setPropertyDisplay(this.getPropertyDisplay());
        grdItemDetail_Mdl.setPropertyList(this.getPropertyList());
        grdItemDetail_Mdl.setIsBestBefore(this.getIsBestBefore());
        grdItemDetail_Mdl.setIbCode(this.getIbCode());
        grdItemDetail_Mdl.setBatchNo(this.getBatchNo());
        grdItemDetail_Mdl.setLotNo(this.getLotNo());
        grdItemDetail_Mdl.setManufacturingDate(this.getManufacturingDate());
        grdItemDetail_Mdl.setBestBeforeDays(this.getBestBeforeDays());
        grdItemDetail_Mdl.setExpiryDate(this.getExpiryDate());
        grdItemDetail_Mdl.setColourCode(this.getColourCode());
        grdItemDetail_Mdl.setColourName(this.getColourName());
        grdItemDetail_Mdl.setSizeCode(this.getSizeCode());
        grdItemDetail_Mdl.setSizeName(this.getSizeName());
        grdItemDetail_Mdl.setSkuDecimalPlaces(this.getSkuDecimalPlaces());
        grdItemDetail_Mdl.setMrp(this.getMrp());
        grdItemDetail_Mdl.setSkuMrp(this.getSkuMrp());
        grdItemDetail_Mdl.setSkuQtyForRate(this.getSkuQtyForRate());
        grdItemDetail_Mdl.setPurchaseUnitCode(this.getPurchaseUnitCode());
        grdItemDetail_Mdl.setPurchaseUnitName(this.getPurchaseUnitName());
        grdItemDetail_Mdl.setPurchaseUnitDecimalPlaces(this.getPurchaseUnitDecimalPlaces());
        grdItemDetail_Mdl.setPurchaseUnitQuantity(this.getPurchaseUnitQuantity());
        grdItemDetail_Mdl.setIsubCode(this.getIsubCode());
    }

}
