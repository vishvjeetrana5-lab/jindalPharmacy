package aarogya.pharmacy.purchaseBill.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Mdl;
import java.io.Serializable;
import java.util.List;

public class TxtHGrnNo_Mdl extends KeyField implements Serializable, IsSerializable {

    /* 
    sharedKeyFieldCode = pcDocId
    sharedKeyFieldName = pcRecId
     */
    private String pcV_SNo;
    private String pcRefNo;
    private String pcRefDate;
    private String pcDepartmentCode;
    private String poDocId;
    private String poRecId;
    private String poV_SNo;
    private String itemCode;
    private String itemName;
    private String hsnCode;
    private String itemGSTNature;
    private String itsBasedOn;
    private String purchaseUnitCode;
    private String purchaseUnitName;
    private String purchaseUnitDisplayName;
    private String purchaseUnitDecimalPlaces;
    private String purchaseUnitConversionRate;
    private String skuCode;
    private String skuName;
    private String skuDecimalPlaces;
    private String gstUnitCode;
    private String grnQty;
    private String pendingQty;
    private String pendingFreeQty;
    private String pendingSkuQty;
    private String rate;
    private String amount;
    private String mrp;
    private String skuMrp;
    private String saleRate;
    private String skuSaleRate;
    private String batchNo;
    private String ibCode;
    private String barcode;
    private String manufacturingDate;
    private String expiryDate;
    private List<OverHeadLine_Mdl> OHLListGRN;

    public TxtHGrnNo_Mdl() {
    }

    public String getPcV_SNo() {
        return pcV_SNo;
    }

    public void setPcV_SNo(String pcV_SNo) {
        this.pcV_SNo = pcV_SNo;
    }

    public String getPcRefNo() {
        return pcRefNo;
    }

    public void setPcRefNo(String pcRefNo) {
        this.pcRefNo = pcRefNo;
    }

    public String getPcRefDate() {
        return pcRefDate;
    }

    public void setPcRefDate(String pcRefDate) {
        this.pcRefDate = pcRefDate;
    }

    public String getPcDepartmentCode() {
        return pcDepartmentCode;
    }

    public void setPcDepartmentCode(String pcDepartmentCode) {
        this.pcDepartmentCode = pcDepartmentCode;
    }

    public String getPoDocId() {
        return poDocId;
    }

    public void setPoDocId(String poDocId) {
        this.poDocId = poDocId;
    }

    public String getPoRecId() {
        return poRecId;
    }

    public void setPoRecId(String poRecId) {
        this.poRecId = poRecId;
    }

    public String getPoV_SNo() {
        return poV_SNo;
    }

    public void setPoV_SNo(String poV_SNo) {
        this.poV_SNo = poV_SNo;
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

    public String getHsnCode() {
        return hsnCode;
    }

    public void setHsnCode(String hsnCode) {
        this.hsnCode = hsnCode;
    }

    public String getItemGSTNature() {
        return itemGSTNature;
    }

    public void setItemGSTNature(String itemGSTNature) {
        this.itemGSTNature = itemGSTNature;
    }

    public String getItsBasedOn() {
        return itsBasedOn;
    }

    public void setItsBasedOn(String itsBasedOn) {
        this.itsBasedOn = itsBasedOn;
    }

    public String getGstUnitCode() {
        return gstUnitCode;
    }

    public void setGstUnitCode(String gstUnitCode) {
        this.gstUnitCode = gstUnitCode;
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

    public String getPurchaseUnitDisplayName() {
        return purchaseUnitDisplayName;
    }

    public void setPurchaseUnitDisplayName(String purchaseUnitDisplayName) {
        this.purchaseUnitDisplayName = purchaseUnitDisplayName;
    }

    public String getPurchaseUnitConversionRate() {
        return purchaseUnitConversionRate;
    }

    public void setPurchaseUnitConversionRate(String purchaseUnitConversionRate) {
        this.purchaseUnitConversionRate = purchaseUnitConversionRate;
    }

    public String getPurchaseUnitDecimalPlaces() {
        return purchaseUnitDecimalPlaces;
    }

    public void setPurchaseUnitDecimalPlaces(String purchaseUnitDecimalPlaces) {
        this.purchaseUnitDecimalPlaces = purchaseUnitDecimalPlaces;
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

    public String getSkuDecimalPlaces() {
        return skuDecimalPlaces;
    }

    public void setSkuDecimalPlaces(String skuDecimalPlaces) {
        this.skuDecimalPlaces = skuDecimalPlaces;
    }

    public String getGrnQty() {
        return grnQty;
    }

    public void setGrnQty(String grnQty) {
        this.grnQty = grnQty;
    }

    public String getPendingQty() {
        return pendingQty;
    }

    public void setPendingQty(String pendingQty) {
        this.pendingQty = pendingQty;
    }

    public String getPendingFreeQty() {
        return pendingFreeQty;
    }

    public void setPendingFreeQty(String pendingFreeQty) {
        this.pendingFreeQty = pendingFreeQty;
    }

    public String getPendingSkuQty() {
        return pendingSkuQty;
    }

    public void setPendingSkuQty(String pendingSkuQty) {
        this.pendingSkuQty = pendingSkuQty;
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

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
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

    public String getSaleRate() {
        return saleRate;
    }

    public void setSaleRate(String saleRate) {
        this.saleRate = saleRate;
    }

    public String getSkuSaleRate() {
        return skuSaleRate;
    }

    public void setSkuSaleRate(String skuSaleRate) {
        this.skuSaleRate = skuSaleRate;
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

    public List<OverHeadLine_Mdl> getOHLListGRN() {
        return OHLListGRN;
    }

    public void setOHLListGRN(List<OverHeadLine_Mdl> OHLListGRN) {
        this.OHLListGRN = OHLListGRN;
    }

}
