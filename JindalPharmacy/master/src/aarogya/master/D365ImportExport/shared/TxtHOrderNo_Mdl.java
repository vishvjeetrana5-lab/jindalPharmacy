package aarogya.master.D365ImportExport.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Mdl;
import java.io.Serializable;
import java.util.List;

public class TxtHOrderNo_Mdl extends KeyField implements Serializable, IsSerializable {

    private String poV_SNo;
    private String poRefNo;
    private String podMrp;
    private String rate;
    private String orderQty;
    private String pendingQty;
    private String skuQty;
    private String amount;
    private String itemCode;
    private String itemName;
    private String unitCode;
    private String unitName;

    // Added fields
    private String batchNo;
    private String barcode;
    private String manufacturingDate;
    private String expiryDate;
    private String skuMrp;
    private String challanQty;
    private String recQty;
    private String rejQty;
    private String acceptQty;
    private String freeQty;
    private String skuDifference;
    private String skuSaleRate;
    private String staffSaleRate;
    private String skuStaffSaleRate;
    private String landedSkuValue;
    private String landedSkuRate;
    private String skuQtyForRate;
    private String skuRate;
    private String remark;
    private String isStatus;

    private List<OverHeadLine_Mdl> OHLListPurchaseOrder;

    private TxtHMedicineName_Mdl txtHMedName_Mdl = new TxtHMedicineName_Mdl();

    public TxtHOrderNo_Mdl() {
    }

    public String getPoV_SNo() {
        return poV_SNo;
    }

    public void setPoV_SNo(String poV_SNo) {
        this.poV_SNo = poV_SNo;
    }

    public String getPoRefNo() {
        return poRefNo;
    }

    public void setPoRefNo(String poRefNo) {
        this.poRefNo = poRefNo;
    }

    public String getPodMrp() {
        return podMrp;
    }

    public void setPodMrp(String podMrp) {
        this.podMrp = podMrp;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(String orderQty) {
        this.orderQty = orderQty;
    }

    public String getPendingQty() {
        return pendingQty;
    }

    public void setPendingQty(String pendingQty) {
        this.pendingQty = pendingQty;
    }

    public String getSkuQty() {
        return skuQty;
    }

    public void setSkuQty(String skuQty) {
        this.skuQty = skuQty;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
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

    public String getSkuMrp() {
        return skuMrp;
    }

    public void setSkuMrp(String skuMrp) {
        this.skuMrp = skuMrp;
    }

    public String getChallanQty() {
        return challanQty;
    }

    public void setChallanQty(String challanQty) {
        this.challanQty = challanQty;
    }

    public String getRecQty() {
        return recQty;
    }

    public void setRecQty(String recQty) {
        this.recQty = recQty;
    }

    public String getRejQty() {
        return rejQty;
    }

    public void setRejQty(String rejQty) {
        this.rejQty = rejQty;
    }

    public String getAcceptQty() {
        return acceptQty;
    }

    public void setAcceptQty(String acceptQty) {
        this.acceptQty = acceptQty;
    }

    public String getFreeQty() {
        return freeQty;
    }

    public void setFreeQty(String freeQty) {
        this.freeQty = freeQty;
    }

    public String getSkuDifference() {
        return skuDifference;
    }

    public void setSkuDifference(String skuDifference) {
        this.skuDifference = skuDifference;
    }

    public String getSkuSaleRate() {
        return skuSaleRate;
    }

    public void setSkuSaleRate(String skuSaleRate) {
        this.skuSaleRate = skuSaleRate;
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

    public String getLandedSkuValue() {
        return landedSkuValue;
    }

    public void setLandedSkuValue(String landedSkuValue) {
        this.landedSkuValue = landedSkuValue;
    }

    public String getLandedSkuRate() {
        return landedSkuRate;
    }

    public void setLandedSkuRate(String landedSkuRate) {
        this.landedSkuRate = landedSkuRate;
    }

    public String getSkuQtyForRate() {
        return skuQtyForRate;
    }

    public void setSkuQtyForRate(String skuQtyForRate) {
        this.skuQtyForRate = skuQtyForRate;
    }

    public String getSkuRate() {
        return skuRate;
    }

    public void setSkuRate(String skuRate) {
        this.skuRate = skuRate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIsStatus() {
        return isStatus;
    }

    public void setIsStatus(String isStatus) {
        this.isStatus = isStatus;
    }

    public List<OverHeadLine_Mdl> getOHLListPurchaseOrder() {
        return OHLListPurchaseOrder;
    }

    public void setOHLListPurchaseOrder(List<OverHeadLine_Mdl> OHLListPurchaseOrder) {
        this.OHLListPurchaseOrder = OHLListPurchaseOrder;
    }

    public TxtHMedicineName_Mdl getTxtHMedName_Mdl() {
        return txtHMedName_Mdl;
    }

    public void setTxtHMedName_Mdl(TxtHMedicineName_Mdl txtHMedName_Mdl) {
        this.txtHMedName_Mdl = txtHMedName_Mdl;
    }

}
