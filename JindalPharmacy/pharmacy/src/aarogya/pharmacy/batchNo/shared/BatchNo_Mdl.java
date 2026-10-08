package aarogya.pharmacy.batchNo.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class BatchNo_Mdl extends KeyField implements Serializable, IsSerializable {

    private String batchNo;
    private String balanceQty;
    private String balanceQtyInPurcUnit;
    private String balanceQtyInSaleUnit;
    private String expiryDate;
    private String manufacturingDate;
    private String binName;
    private String minStock;
    private String reOrderLevel;
    private String mrp;
    private String grossPurchaseRate;
    private String discountPerUnit;
    private String receiveRecId;

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
    private String saleRate;
    private String skuCode;
    private String skuName;
    private String purchaseUnitCode;
    private String purchaseUnitName;
    private String ibCode;
    private String skuSaleRate;
    private String skuMrp;
    private String decimalPlaces;
    private String bestBeforeDays;
    private boolean expiredIbCode;
    private String barCode;
    private String skuLandedRate;
    private String skuStaffRate;
    private String skuGrossPurchseRate;
    private String wholeSaleRate;

    public String getSkuLandedRate() {
        return skuLandedRate;
    }

    public void setSkuLandedRate(String skuLandedRate) {
        this.skuLandedRate = skuLandedRate;
    }

    public BatchNo_Mdl() {
    }

    public boolean isExpiredIbCode() {
        return expiredIbCode;
    }

    public void setExpiredIbCode(boolean expiredIbCode) {
        this.expiredIbCode = expiredIbCode;
    }

    public String getReOrderLevel() {
        return reOrderLevel;
    }

    public void setReOrderLevel(String reOrderLevel) {
        this.reOrderLevel = reOrderLevel;
    }

    public String getBalanceQtyInPurcUnit() {
        return balanceQtyInPurcUnit;
    }

    public void setBalanceQtyInPurcUnit(String balanceQtyInPurcUnit) {
        this.balanceQtyInPurcUnit = balanceQtyInPurcUnit;
    }

    public String getBalanceQtyInSaleUnit() {
        return balanceQtyInSaleUnit;
    }

    public void setBalanceQtyInSaleUnit(String balanceQtyInSaleUnit) {
        this.balanceQtyInSaleUnit = balanceQtyInSaleUnit;
    }

    public String getMinStock() {
        return minStock;
    }

    public void setMinStock(String minStock) {
        this.minStock = minStock;
    }

    public String getBinName() {
        return binName;
    }

    public void setBinName(String binName) {
        this.binName = binName;
    }

    public String getBestBeforeDays() {
        return bestBeforeDays;
    }

    public void setBestBeforeDays(String bestBeforeDays) {
        this.bestBeforeDays = bestBeforeDays;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getBalanceQty() {
        return balanceQty;
    }

    public void setBalanceQty(String balanceQty) {
        this.balanceQty = balanceQty;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getManufacturingDate() {
        return manufacturingDate;
    }

    public void setManufacturingDate(String manufacturingDate) {
        this.manufacturingDate = manufacturingDate;
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

    public String getIbCode() {
        return ibCode;
    }

    public void setIbCode(String ibCode) {
        this.ibCode = ibCode;
    }

    public String getSkuSaleRate() {
        return skuSaleRate;
    }

    public void setSkuSaleRate(String skuSaleRate) {
        this.skuSaleRate = skuSaleRate;
    }

    public String getSkuMrp() {
        return skuMrp;
    }

    public void setSkuMrp(String skuMrp) {
        this.skuMrp = skuMrp;
    }

    public String getDecimalPlaces() {
        return decimalPlaces;
    }

    public void setDecimalPlaces(String decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getGrossPurchaseRate() {
        return grossPurchaseRate;
    }

    public void setGrossPurchaseRate(String grossPurchaseRate) {
        this.grossPurchaseRate = grossPurchaseRate;
    }

    public String getSkuStaffRate() {
        return skuStaffRate;
    }

    public void setSkuStaffRate(String skuStaffRate) {
        this.skuStaffRate = skuStaffRate;
    }

    public String getSkuGrossPurchseRate() {
        return skuGrossPurchseRate;
    }

    public void setSkuGrossPurchseRate(String skuGrossPurchseRate) {
        this.skuGrossPurchseRate = skuGrossPurchseRate;
    }

    public String getDiscountPerUnit() {
        return discountPerUnit;
    }

    public void setDiscountPerUnit(String discountPerUnit) {
        this.discountPerUnit = discountPerUnit;
    }

    public String getReceiveRecId() {
        return receiveRecId;
    }

    public void setReceiveRecId(String receiveRecId) {
        this.receiveRecId = receiveRecId;
    }

    public String getWholeSaleRate() {
        return wholeSaleRate;
    }

    public void setWholeSaleRate(String wholeSaleRate) {
        this.wholeSaleRate = wholeSaleRate;
    }

}
