package aarogya.pharmacy.addBatchSingle.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHBatch_Mdl extends KeyField implements Serializable, IsSerializable {

    private String expDate;
    private String qty;
    private String mrp;
    private String skuMrp;
    private String saleRate;
    private String skuSaleRate;
    private String rowId;
    private String mfgDate;
    private String itemCode;
    private String bestBeforeDays;
    private String barcode;
    private String isBarcodeActive;
    private String skuLandedRate;
    private String discountPerUnit;
    private String wholeSaleRate;

    public String getSkuLandedRate() {
        return skuLandedRate;
    }

    public void setSkuLandedRate(String skuLandedRate) {
        this.skuLandedRate = skuLandedRate;
    }

    public TxtHBatch_Mdl() {
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
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

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    public String getMfgDate() {
        return mfgDate;
    }

    public void setMfgDate(String mfgDate) {
        this.mfgDate = mfgDate;
    }

    public String getBestBeforeDays() {
        return bestBeforeDays;
    }

    public void setBestBeforeDays(String bestBeforeDays) {
        this.bestBeforeDays = bestBeforeDays;
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

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getIsBarcodeActive() {
        return isBarcodeActive;
    }

    public void setIsBarcodeActive(String isBarcodeActive) {
        this.isBarcodeActive = isBarcodeActive;
    }

    public String getDiscountPerUnit() {
        return discountPerUnit;
    }

    public void setDiscountPerUnit(String discountPerUnit) {
        this.discountPerUnit = discountPerUnit;
    }

    public String getWholeSaleRate() {
        return wholeSaleRate;
    }

    public void setWholeSaleRate(String wholeSaleRate) {
        this.wholeSaleRate = wholeSaleRate;
    }

}
