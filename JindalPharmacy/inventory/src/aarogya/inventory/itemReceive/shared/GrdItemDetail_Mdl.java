package aarogya.inventory.itemReceive.shared;

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

    private String skuCode;
    private String rate;
    private String amount;
    private String totalpacks;
    private String purchaseUnitCode;
    private String purchaseUnitName;
    private String purchaseUnitDecimalPlaces;
    private String skuRate;

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

    public GrdItemDetail_Mdl(GrdItemDetail_Mdl gidMdl) {
        this(gidMdl.getV_SNo(), gidMdl.getQuantity(), gidMdl.getSkuName(), gidMdl.getSkuDecimalPlaces(), gidMdl.getRemark(), gidMdl.getPropertyDisplay(), gidMdl.getSkuQtyForRate(), gidMdl.getPropertyList(), gidMdl.getIsBestBefore(), gidMdl.getIbCode(), gidMdl.getBatchNo(), gidMdl.getLotNo(), gidMdl.getManufacturingDate(), gidMdl.getBestBeforeDays(), gidMdl.getExpiryDate(), gidMdl.getColourCode(), gidMdl.getColourName(), gidMdl.getSizeCode(), gidMdl.getSizeName(), gidMdl.getMrp(), gidMdl.getSkuMrp(), gidMdl.getSharedKeyFieldCode(), gidMdl.getSharedKeyFieldName(), gidMdl.getSkuCode(), gidMdl.getRate(),
                gidMdl.getAmount(),
                gidMdl.getTotalpacks(),
                gidMdl.getPurchaseUnitCode(),
                gidMdl.getPurchaseUnitName(),
                gidMdl.getPurchaseUnitDecimalPlaces(),
                gidMdl.getSkuRate(),
                gidMdl.getIsubCode());
    }

    private GrdItemDetail_Mdl(String v_SNo, String quantity, String skuName, String skuDecimalPlaces, String remark, String propertyDisplay, String skuQtyForRate, String propertyList, String isBestBefore, String ibCode, String batchNo, String lotNo, String manufacturingDate, String bestBeforeDays, String expiryDate, String colourCode, String colourName, String sizeCode, String sizeName, String mrp, String skuMrp, String KeyFieldCode, String KeyFieldName, String skuCode,
            String purchaseRate,
            String amount,
            String totalpacks,
            String purchaseUnitCode,
            String purchaseUnitName,
            String purchaseUnitDecimalPlaces,
            String skuRate,
            String isubCode) {
        super(KeyFieldCode, KeyFieldName);
        this.v_SNo = v_SNo;
        this.quantity = quantity;
        this.skuName = skuName;
        this.skuDecimalPlaces = skuDecimalPlaces;
        this.remark = remark;
        this.propertyDisplay = propertyDisplay;
        this.skuQtyForRate = skuQtyForRate;
        this.propertyList = propertyList;
        this.isBestBefore = isBestBefore;
        this.ibCode = ibCode;
        this.batchNo = batchNo;
        this.lotNo = lotNo;
        this.manufacturingDate = manufacturingDate;
        this.bestBeforeDays = bestBeforeDays;
        this.expiryDate = expiryDate;
        this.colourCode = colourCode;
        this.colourName = colourName;
        this.sizeCode = sizeCode;
        this.sizeName = sizeName;
        this.mrp = mrp;
        this.skuMrp = skuMrp;

        this.skuCode = skuCode;
        this.rate = purchaseRate;
        this.amount = amount;
        this.totalpacks = totalpacks;
        this.skuRate = skuRate;
        this.purchaseUnitCode = purchaseUnitCode;
        this.purchaseUnitName = purchaseUnitName;
        this.purchaseUnitDecimalPlaces = purchaseUnitDecimalPlaces;
        
        //IAP
        this.isubCode = isubCode;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
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

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTotalpacks() {
        return totalpacks;
    }

    public void setTotalpacks(String totalpacks) {
        this.totalpacks = totalpacks;
    }

    public String getSkuRate() {
        return skuRate;
    }

    public void setSkuRate(String skuRate) {
        this.skuRate = skuRate;
    }

    public String getSkuMrp() {
        return skuMrp;
    }

    public void setSkuMrp(String skuMrp) {
        this.skuMrp = skuMrp;
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

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
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
}
