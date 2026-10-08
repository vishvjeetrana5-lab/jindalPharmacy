package aarogya.pharmacy.medicineOpening.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;

public class GrdMedicineDetail_Mdl extends KeyField implements Serializable, IsSerializable {

    //sharedkeyFieldCode=medicineCode
    //sharedkeyFieldName=medicineName   
    private String v_SNo;
    private String skuName;
    private String skuCode;
    private String totalQuantity;
    private String rate;
    private String skuValue;
    private String batchNo;
    private String mrp;
    private String saleRate;
    private String skuMrp;
    private String skuSaleRate;
    private String skuQtyForRate;
    private String manufacturingDate;
    private String bestBeforeDays;
    private String expiryDate;
    private String refNo;
    private String refDate;
    private String remark;
    private String skuDecimalPlaces;
    private String isBestBefore;
    private String ibCode;
    private String skuRate;
    private String purchaseUnitName;
    private String purchaseUnitCode;
    private String purchaseUnitDecimalPlaces;
    private String purchaseUnitQuantity;
    private String barcode;
    private String isBarcodeActive;

    public GrdMedicineDetail_Mdl() {
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

    public String getSkuRate() {
        return skuRate;
    }

    public void setSkuRate(String skuRate) {
        this.skuRate = skuRate;
    }

    public String getIsBestBefore() {
        return isBestBefore;
    }

    public void setIsBestBefore(String isBestBefore) {
        this.isBestBefore = isBestBefore;
    }

    public String getSkuDecimalPlaces() {
        return skuDecimalPlaces;
    }

    public void setSkuDecimalPlaces(String skuDecimalPlaces) {
        this.skuDecimalPlaces = skuDecimalPlaces;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
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

    public String getSaleRate() {
        return saleRate;
    }

    public void setSaleRate(String saleRate) {
        this.saleRate = saleRate;
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

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getRefDate() {
        return refDate;
    }

    public void setRefDate(String refDate) {
        this.refDate = refDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getV_SNo() {
        return v_SNo;
    }

    public void setV_SNo(String v_SNo) {
        this.v_SNo = v_SNo;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getIbCode() {
        return ibCode;
    }

    public void setIbCode(String ibCode) {
        this.ibCode = ibCode;
    }

    public String getSkuQtyForRate() {
        return skuQtyForRate;
    }

    public void setSkuQtyForRate(String skuQtyForRate) {
        this.skuQtyForRate = skuQtyForRate;
    }

    public String getSkuValue() {
        return skuValue;
    }

    public void setSkuValue(String skuValue) {
        this.skuValue = skuValue;
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

    public GrdMedicineDetail_Mdl clone() {
        GrdMedicineDetail_Mdl grdMedicineDetail_Mdl = new GrdMedicineDetail_Mdl();

        copy(grdMedicineDetail_Mdl);

        return grdMedicineDetail_Mdl;
    }

    public void copy(GrdMedicineDetail_Mdl grdMedicineDetail_Mdl) {
        grdMedicineDetail_Mdl.setSharedKeyFieldCode(this.getSharedKeyFieldCode());
        grdMedicineDetail_Mdl.setSharedKeyFieldName(this.getSharedKeyFieldName());
        grdMedicineDetail_Mdl.setSharedPreparedBy(this.getSharedPreparedBy());
        grdMedicineDetail_Mdl.setSharedModifiedBy(this.getSharedModifiedBy());
        grdMedicineDetail_Mdl.setV_SNo(this.getV_SNo());
        grdMedicineDetail_Mdl.setSkuName(this.getSkuName());
        grdMedicineDetail_Mdl.setSkuCode(this.getSkuCode());
        grdMedicineDetail_Mdl.setTotalQuantity(this.getTotalQuantity());
        grdMedicineDetail_Mdl.setRate(this.getRate());
        grdMedicineDetail_Mdl.setSkuValue(this.getSkuValue());
        grdMedicineDetail_Mdl.setBatchNo(this.getBatchNo());
        grdMedicineDetail_Mdl.setBarcode(this.getBarcode());
        grdMedicineDetail_Mdl.setIsBarcodeActive(this.getIsBarcodeActive());
        grdMedicineDetail_Mdl.setMrp(this.getMrp());
        grdMedicineDetail_Mdl.setSaleRate(this.getSaleRate());
        grdMedicineDetail_Mdl.setSkuQtyForRate(this.getSkuQtyForRate());
        grdMedicineDetail_Mdl.setManufacturingDate(this.getManufacturingDate());
        grdMedicineDetail_Mdl.setBestBeforeDays(this.getBestBeforeDays());
        grdMedicineDetail_Mdl.setExpiryDate(this.getExpiryDate());
        grdMedicineDetail_Mdl.setRefNo(this.getRefNo());
        grdMedicineDetail_Mdl.setRefDate(this.getRefDate());
        grdMedicineDetail_Mdl.setRemark(this.getRemark());
        grdMedicineDetail_Mdl.setSkuDecimalPlaces(this.getSkuDecimalPlaces());
        grdMedicineDetail_Mdl.setIsBestBefore(this.getIsBestBefore());
        grdMedicineDetail_Mdl.setIbCode(this.getIbCode());
        grdMedicineDetail_Mdl.setSkuMrp(this.getSkuMrp());
        grdMedicineDetail_Mdl.setSkuSaleRate(this.getSkuSaleRate());
        grdMedicineDetail_Mdl.setPurchaseUnitCode(this.getPurchaseUnitCode());
        grdMedicineDetail_Mdl.setPurchaseUnitName(this.getPurchaseUnitName());
        grdMedicineDetail_Mdl.setPurchaseUnitDecimalPlaces(this.getPurchaseUnitDecimalPlaces());
        grdMedicineDetail_Mdl.setPurchaseUnitQuantity(this.getPurchaseUnitQuantity());
    }
}
