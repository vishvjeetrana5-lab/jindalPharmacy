package aarogya.selfToolkit.itemBatch.shared;

import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class ItemBatch_Mdl implements Serializable, IsSerializable {

    private String code;
    private String saleRate;
    private String skuSaleRate;
    private String barcode;
    private ItemProperty_Mdl ipm = new ItemProperty_Mdl();
    private String status;
    private String receiveQty;
    private String receiveDate;
    private String previousDate;
    private String increaseQty;
    private String decreaseQty;
    private String statusDate;
    private String statusQuantity;

    private String stockSkuValue;
    private String stockLandedSkuValue;
    private String staffSaleRate;
    private String skuStaffSaleRate;
    private String stockRemark;
    private String stockV_Sno;
    private String stockRefChallanNo;
    private String stockRefChallanDate;
    private String stockRefDocId;
    private String stockRefV_Type;
    private String stockRefV_SNo;
    private String stockDepartmentCode;
    private String docId;
    private String recId;
    private String vType;
    private String vSno;
    private String purchaseRateExFree;
    private String purchaseTaxableRateExFree;

    public String getStockDepartmentCode() {
        return stockDepartmentCode;
    }

    public void setStockDepartmentCode(String stockDepartmentCode) {
        this.stockDepartmentCode = stockDepartmentCode;
    }

    public String getStaffSaleRate() {
        return staffSaleRate;
    }

    public void setStaffSaleRate(String staffSaleRate) {
        this.staffSaleRate = staffSaleRate;
    }

    public String getSkuStaffSaleRate() {
        return skuStaffSaleRate;
    }

    public void setSkuStaffSaleRate(String skuStaffSaleRate) {
        this.skuStaffSaleRate = skuStaffSaleRate;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getRecId() {
        return recId;
    }

    public void setRecId(String recId) {
        this.recId = recId;
    }

    public String getvType() {
        return vType;
    }

    public void setvType(String vType) {
        this.vType = vType;
    }

    public String getvSno() {
        return vSno;
    }

    public void setvSno(String vSno) {
        this.vSno = vSno;
    }

    public String getPurchaseRateExFree() {
        return purchaseRateExFree;
    }

    public void setPurchaseRateExFree(String purchaseRateExFree) {
        this.purchaseRateExFree = purchaseRateExFree;
    }

    public String getPurchaseTaxableRateExFree() {
        return purchaseTaxableRateExFree;
    }

    public void setPurchaseTaxableRateExFree(String purchaseTaxableRateExFree) {
        this.purchaseTaxableRateExFree = purchaseTaxableRateExFree;
    }

    public class ItemBatchStatus {

        public static final String EXISTING_BATCH = "Existing";
        public static final String NEW_BATCH = "New";
        public static final String MATCHED = "Matched";
        public static final String CHANGED = "Changed";
        public static final String REMOVED = "Removed";
        public static final String NON_BATCH = "Non-Batch";
    }

    public ItemBatch_Mdl() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSaleRate() {
        return saleRate;
    }

    public void setSaleRate(String saleRate) {
        this.saleRate = saleRate;
    }

    public ItemProperty_Mdl getIpm() {
        return ipm;
    }

    public void setIpm(ItemProperty_Mdl ipm) {
        this.ipm = ipm;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReceiveQty() {
        return receiveQty;
    }

    public void setReceiveQty(String receiveQty) {
        this.receiveQty = receiveQty;
    }

    public String getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(String receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getPreviousDate() {
        return previousDate;
    }

    public void setPreviousDate(String previousDate) {
        this.previousDate = previousDate;
    }

    public String getIncreaseQty() {
        return increaseQty;
    }

    public void setIncreaseQty(String increaseQty) {
        this.increaseQty = increaseQty;
    }

    public String getDecreaseQty() {
        return decreaseQty;
    }

    public void setDecreaseQty(String decreaseQty) {
        this.decreaseQty = decreaseQty;
    }

    public String getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(String statusDate) {
        this.statusDate = statusDate;
    }

    public String getStatusQuantity() {
        return statusQuantity;
    }

    public void setStatusQuantity(String statusQuantity) {
        this.statusQuantity = statusQuantity;
    }

    public String getStockSkuValue() {
        return stockSkuValue;
    }

    public void setStockSkuValue(String stockSkuValue) {
        this.stockSkuValue = stockSkuValue;
    }

    public String getStockLandedSkuValue() {
        return stockLandedSkuValue;
    }

    public void setStockLandedSkuValue(String stockLandedSkuValue) {
        this.stockLandedSkuValue = stockLandedSkuValue;
    }

    public String getStockRemark() {
        return stockRemark;
    }

    public void setStockRemark(String stockRemark) {
        this.stockRemark = stockRemark;
    }

    public String getStockV_Sno() {
        return stockV_Sno;
    }

    public void setStockV_Sno(String stockV_Sno) {
        this.stockV_Sno = stockV_Sno;
    }

    public String getStockRefChallanNo() {
        return stockRefChallanNo;
    }

    public void setStockRefChallanNo(String stockRefChallanNo) {
        this.stockRefChallanNo = stockRefChallanNo;
    }

    public String getStockRefChallanDate() {
        return stockRefChallanDate;
    }

    public void setStockRefChallanDate(String stockRefChallanDate) {
        this.stockRefChallanDate = stockRefChallanDate;
    }

    public String getStockRefDocId() {
        return stockRefDocId;
    }

    public void setStockRefDocId(String stockRefDocId) {
        this.stockRefDocId = stockRefDocId;
    }

    public String getStockRefV_Type() {
        return stockRefV_Type;
    }

    public void setStockRefV_Type(String stockRefV_Type) {
        this.stockRefV_Type = stockRefV_Type;
    }

    public String getStockRefV_SNo() {
        return stockRefV_SNo;
    }

    public void setStockRefV_SNo(String stockRefV_SNo) {
        this.stockRefV_SNo = stockRefV_SNo;
    }

    public String getSkuSaleRate() {
        return skuSaleRate;
    }

    public void setSkuSaleRate(String skuSaleRate) {
        this.skuSaleRate = skuSaleRate;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
