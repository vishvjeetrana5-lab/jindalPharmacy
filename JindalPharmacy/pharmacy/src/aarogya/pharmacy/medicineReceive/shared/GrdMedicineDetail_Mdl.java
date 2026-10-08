package aarogya.pharmacy.medicineReceive.shared;

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
    private String saleRate;
    private String rate;
    private String skuRate;
    private String amount;
    private String totalpacks;
    private String purchaseUnitDecimalPlaces;

    //Properties
    private String isBestBefore;
    private String ibCode;
    private String batchNo;
    private String manufacturingDate;
    private String bestBeforeDays;
    private String expiryDate;
    private String mrp;
    private String skuMrp;
    private String skuSaleRate;
    private String barCode;
    private String itemCodeCsv;

    public GrdMedicineDetail_Mdl() {
    }

    public GrdMedicineDetail_Mdl(GrdMedicineDetail_Mdl gmdMdl) {
        this(gmdMdl.getV_SNo(), gmdMdl.getQuantity(), gmdMdl.getSkuName(), gmdMdl.getSkuCode(), gmdMdl.getPurchaseUnitName(), gmdMdl.getPurchaseUnitCode(), gmdMdl.getSkuDecimalPlaces(), gmdMdl.getRemark(), gmdMdl.getSkuQtyForRate(), gmdMdl.getSaleRate(), gmdMdl.getIsBestBefore(), gmdMdl.getIbCode(), gmdMdl.getBatchNo(), gmdMdl.getManufacturingDate(), gmdMdl.getBestBeforeDays(), gmdMdl.getExpiryDate(), gmdMdl.getMrp(), gmdMdl.getSkuMrp(), gmdMdl.getSkuSaleRate(), gmdMdl.getSharedKeyFieldCode(), gmdMdl.getSharedKeyFieldName(), gmdMdl.getRate(), gmdMdl.getAmount(), gmdMdl.getTotalpacks(), gmdMdl.getPurchaseUnitDecimalPlaces(), gmdMdl.getSkuRate(), gmdMdl.getBarCode(), gmdMdl.getItemCodeCsv());
    }

    private GrdMedicineDetail_Mdl(String v_SNo, String quantity, String skuName, String skuCode, String purchaseUnitName, String purchaseUnitCode, String skuDecimalPlaces, String remark, String skuQtyForRate, String saleRate, String isBestBefore, String ibCode, String batchNo, String manufacturingDate, String bestBeforeDays, String expiryDate, String mrp, String skuMrp, String skuSaleRate, String KeyFieldCode, String KeyFieldName, String purchaseRate,
            String amount,
            String totalpacks,
            String purchaseUnitDecimalPlaces, String skuRate, String barCode, String itemCodeCsv) {
        super(KeyFieldCode, KeyFieldName);
        this.v_SNo = v_SNo;
        this.quantity = quantity;
        this.skuName = skuName;
        this.skuCode = skuCode;
        this.purchaseUnitName = purchaseUnitName;
        this.purchaseUnitCode = purchaseUnitCode;
        this.skuDecimalPlaces = skuDecimalPlaces;
        this.remark = remark;
        this.skuQtyForRate = skuQtyForRate;
        this.saleRate = saleRate;
        this.isBestBefore = isBestBefore;
        this.ibCode = ibCode;
        this.batchNo = batchNo;
        this.manufacturingDate = manufacturingDate;
        this.bestBeforeDays = bestBeforeDays;
        this.expiryDate = expiryDate;
        this.mrp = mrp;
        this.skuMrp = skuMrp;
        this.skuSaleRate = skuSaleRate;
        this.rate = purchaseRate;
        this.amount = amount;
        this.totalpacks = totalpacks;
        this.skuRate = skuRate;
        this.purchaseUnitDecimalPlaces = purchaseUnitDecimalPlaces;
        this.barCode = barCode;
        this.itemCodeCsv = itemCodeCsv;
    }

    public String getPurchaseUnitDecimalPlaces() {
        return purchaseUnitDecimalPlaces;
    }

    public void setPurchaseUnitDecimalPlaces(String purchaseUnitDecimalPlaces) {
        this.purchaseUnitDecimalPlaces = purchaseUnitDecimalPlaces;
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

    public String getSkuQtyForRate() {
        return skuQtyForRate;
    }

    public void setSkuQtyForRate(String skuQtyForRate) {
        this.skuQtyForRate = skuQtyForRate;
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

}
