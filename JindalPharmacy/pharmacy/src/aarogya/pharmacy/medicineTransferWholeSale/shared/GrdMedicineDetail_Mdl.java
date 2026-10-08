package aarogya.pharmacy.medicineTransferWholeSale.shared;

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
    private String remark;
    private String skuQtyForRate;
    private String ibCode;
    private String batchNo;
    private String manufacturingDate;
    private String bestBeforeDays;
    private String expiryDate;
    private String mrp;
    private String saleRate;
    private String packSize;
    private String skuMrp;
    private String skuSaleRate;
    private TxtHMedicineName_Mdl medicineMdl;
    private String requisitionDocId;
    private String requisitionNo;
    private String requisitionV_SNo;
    private String quantityTotalPerItem;
    private String barCode;
    private String itemCodeCsv;
    private String isBarcodeActive;
    private String trfQuantity;
    private String trfRate;

    public String getTrfQuantity() {
        return trfQuantity;
    }

    public void setTrfQuantity(String trfQuantity) {
        this.trfQuantity = trfQuantity;
    }

    public GrdMedicineDetail_Mdl() {
    }

    public GrdMedicineDetail_Mdl(GrdMedicineDetail_Mdl gmdMdl) {
        this(gmdMdl.getV_SNo(), gmdMdl.getQuantity(), gmdMdl.getSkuName(), gmdMdl.getSkuCode(), gmdMdl.getPurchaseUnitName(), gmdMdl.getPurchaseUnitCode(), gmdMdl.getRemark(), gmdMdl.getSkuQtyForRate(), gmdMdl.getIbCode(), gmdMdl.getBatchNo(), gmdMdl.getManufacturingDate(), gmdMdl.getBestBeforeDays(), gmdMdl.getExpiryDate(), gmdMdl.getMrp(), gmdMdl.getSaleRate(), gmdMdl.getPackSize(), gmdMdl.getSkuMrp(), gmdMdl.getSkuSaleRate(), gmdMdl.getMedicineMdl(), gmdMdl.getSharedKeyFieldCode(), gmdMdl.getSharedKeyFieldName(), gmdMdl.getRequisitionDocId(), gmdMdl.getRequisitionNo(), gmdMdl.getRequisitionV_SNo(), gmdMdl.getQuantityTotalPerItem(), gmdMdl.getBarCode(), gmdMdl.getItemCodeCsv(), gmdMdl.getIsBarcodeActive(), gmdMdl.getTrfRate());
    }

   

    private GrdMedicineDetail_Mdl(String v_SNo, String quantity, String skuName, String skuCode, String purchaseUnitName, String purchaseUnitCode, String remark, String skuQtyForRate, String ibCode, String batchNo, String manufacturingDate, String bestBeforeDays, String expiryDate, String mrp, String saleRate, String packSize, String skuMrp, String skuSaleRate, TxtHMedicineName_Mdl medicineMdl, String KeyFieldCode, String KeyFieldName, String requisitionDocId, String requisitionNo, String requisitionV_SNo, String quantityTotalPerItem, String barcode, String itemCodeCsv, String isBarcodeActive,String trfRate) {
        super(KeyFieldCode, KeyFieldName);
        this.v_SNo = v_SNo;
        this.quantity = quantity;
        this.skuName = skuName;
        this.skuCode = skuCode;
        this.purchaseUnitName = purchaseUnitName;
        this.purchaseUnitCode = purchaseUnitCode;
        this.remark = remark;
        this.skuQtyForRate = skuQtyForRate;
        this.ibCode = ibCode;
        this.batchNo = batchNo;
        this.manufacturingDate = manufacturingDate;
        this.bestBeforeDays = bestBeforeDays;
        this.expiryDate = expiryDate;
        this.mrp = mrp;
        this.saleRate = saleRate;
        this.packSize = packSize;
        this.skuMrp = skuMrp;
        this.skuSaleRate = skuSaleRate;
        this.medicineMdl = medicineMdl != null ? new TxtHMedicineName_Mdl(medicineMdl) : null;
        this.requisitionDocId = requisitionDocId;
        this.requisitionNo = requisitionNo;
        this.requisitionV_SNo = requisitionV_SNo;
        this.barCode = barcode;
        this.itemCodeCsv = itemCodeCsv;
        this.isBarcodeActive = isBarcodeActive;
        this.trfRate = trfRate;
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

     public String getTrfRate() {
        return trfRate;
    }

    public void setTrfRate(String trfRate) {
        this.trfRate = trfRate;
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

    public String getSkuQtyForRate() {
        return skuQtyForRate;
    }

    public void setSkuQtyForRate(String skuQtyForRate) {
        this.skuQtyForRate = skuQtyForRate;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIbCode() {
        return ibCode;
    }

    public void setIbCode(String ibCode) {
        this.ibCode = ibCode;
    }

    public TxtHMedicineName_Mdl getMedicineMdl() {
        return medicineMdl;
    }

    public void setMedicineMdl(TxtHMedicineName_Mdl medicineMdl) {
        this.medicineMdl = medicineMdl;
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

    public String getSaleRate() {
        return saleRate;
    }

    public void setSaleRate(String saleRate) {
        this.saleRate = saleRate;
    }

    public String getPackSize() {
        return packSize;
    }

    public void setPackSize(String packSize) {
        this.packSize = packSize;
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

    public String getQuantityTotalPerItem() {
        return quantityTotalPerItem;
    }

    public void setQuantityTotalPerItem(String quantityTotalPerItem) {
        this.quantityTotalPerItem = quantityTotalPerItem;
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

    public String getIsBarcodeActive() {
        return isBarcodeActive;
    }

    public void setIsBarcodeActive(String isBarcodeActive) {
        this.isBarcodeActive = isBarcodeActive;
    }
}
