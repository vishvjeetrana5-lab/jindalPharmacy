package aarogya.inventory.itemIssue.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdItemDetail_Mdl extends KeyField implements Serializable, IsSerializable {

    private String v_SNo;
    private String quantity;
    private String skuName;
    private String skuDecimalPlaces;
    private String remark;
    private String propertyDisplay;
    private String skuQtyForRate;
    private String requisitionDocId;
    private String requisitionNo;
    private String requisitionV_SNo;

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
    private String grIrConsumptionAc;

    public GrdItemDetail_Mdl() {
    }

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    public String getSkuMrp() {
        return skuMrp;
    }

    public void setSkuMrp(String skuMrp) {
        this.skuMrp = skuMrp;
    }

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

    public String getPropertyDisplay() {
        return propertyDisplay;
    }

    public void setPropertyDisplay(String propertyDisplay) {
        this.propertyDisplay = propertyDisplay;
    }

    public String getSkuQtyForRate() {
        return skuQtyForRate;
    }

    public void setSkuQtyForRate(String skuQtyForRate) {
        this.skuQtyForRate = skuQtyForRate;
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

    public String getGrIrConsumptionAc() {
        return grIrConsumptionAc;
    }

    public void setGrIrConsumptionAc(String grIrConsumptionAc) {
        this.grIrConsumptionAc = grIrConsumptionAc;
    }
}
