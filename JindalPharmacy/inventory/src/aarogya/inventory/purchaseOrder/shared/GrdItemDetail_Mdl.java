package aarogya.inventory.purchaseOrder.shared;

import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.overHeadLine.shared.OverHeadITS_Mdl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GrdItemDetail_Mdl extends KeyField implements Serializable {

    //Item Details
    private String itemGSTNature;
    private boolean manualTax;
    private boolean manualGstType;
    private String GstTypeCode;
    private String GstTypeName;
    private String itsBasedOn;
    private String hsnCode;
    private String isBestBefore;
    private String hsnNo;

    //SKU
    private String skuCode;
    private String skuName;
    private String skuDecimalPlaces;

    //Purchase Unit
    private String purchaseUnitCode;
    private String purchaseUnitDisplayName;
    private String purchaseUnitName;
    private String purchaseUnitDecimalPlaces;
    private String purchaseUnitConversionRate;

    //Properties
    private String itemMakeCode;
    private String itemMakeName;
    private String sizeCode;
    private String itemProperty;
    private String colorCode;
    private String color;
    private String size;
    private String properties;

    //Quantity
    private String orderQuantity;
    private String skuQty;
    private String challanQyt;

    //Rate
    private String rate;
    private String purchaseRate;
    private String approvedRateYN;
    private String amount;
    private String discountPer;
    private String discount;
    private String taxableAmt;
    private String sgstUgstPer;
    private String sgstUgst;
    private String sgstPer;
    private String sgst;
    private String ugstPer;
    private String ugst;
    private String cgstPer;
    private String cgst;
    private String igstPer;
    private String igst;
    private String itemValue;
    private String subTotal;

    //Others
    private String remark;
    private String reqDate;
    private String vSNo;
    private String isStatus;

    //Quotation
    private String quotationDocId;
    private String quotationV_SNo;

    //Servcie
    private boolean serviceRecord;
    private String smfCode;
    private String serviceEvalTypeCode;
    private String serviceEvalTypeName;
    private String serviceEvalRate;

    private String lastRateComparison;
    private String lastRateComparisonName;

    private Tolerance_Mdl toleranceMdl = new Tolerance_Mdl();
    private List<GrdIndentDetail_Mdl> listIndentDetail = new ArrayList<GrdIndentDetail_Mdl>();
    private List<OverHeadLine_Mdl> OHLListQuotation;
    private List<OverHeadITS_Mdl> lstITS;

    public GrdItemDetail_Mdl() {
    }

    public boolean isManualTax() {
        return manualTax;
    }

    public void setManualTax(boolean manualTax) {
        this.manualTax = manualTax;
    }

    public boolean isManualGstType() {
        return manualGstType;
    }

    public void setManualGstType(boolean manualGstType) {
        this.manualGstType = manualGstType;
    }

    public String getGstTypeCode() {
        return GstTypeCode;
    }

    public void setGstTypeCode(String GstTypeCode) {
        this.GstTypeCode = GstTypeCode;
    }

    public String getGstTypeName() {
        return GstTypeName;
    }

    public void setGstTypeName(String GstTypeName) {
        this.GstTypeName = GstTypeName;
    }

    public String getHsnNo() {
        return hsnNo;
    }

    public void setHsnNo(String hsnNo) {
        this.hsnNo = hsnNo;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getDiscountPer() {
        return discountPer;
    }

    public void setDiscountPer(String discountPer) {
        this.discountPer = discountPer;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getTaxableAmt() {
        return taxableAmt;
    }

    public void setTaxableAmt(String taxableAmt) {
        this.taxableAmt = taxableAmt;
    }

    public String getSgstUgstPer() {
        return sgstUgstPer;
    }

    public void setSgstUgstPer(String sgstUgstPer) {
        this.sgstUgstPer = sgstUgstPer;
    }

    public String getSgstUgst() {
        return sgstUgst;
    }

    public void setSgstUgst(String sgstUgst) {
        this.sgstUgst = sgstUgst;
    }

    public String getSgstPer() {
        return sgstPer;
    }

    public void setSgstPer(String sgstPer) {
        this.sgstPer = sgstPer;
    }

    public String getSgst() {
        return sgst;
    }

    public void setSgst(String sgst) {
        this.sgst = sgst;
    }

    public String getUgstPer() {
        return ugstPer;
    }

    public void setUgstPer(String ugstPer) {
        this.ugstPer = ugstPer;
    }

    public String getUgst() {
        return ugst;
    }

    public void setUgst(String ugst) {
        this.ugst = ugst;
    }

    public String getCgstPer() {
        return cgstPer;
    }

    public void setCgstPer(String cgstPer) {
        this.cgstPer = cgstPer;
    }

    public String getCgst() {
        return cgst;
    }

    public void setCgst(String cgst) {
        this.cgst = cgst;
    }

    public String getIgstPer() {
        return igstPer;
    }

    public void setIgstPer(String igstPer) {
        this.igstPer = igstPer;
    }

    public String getIgst() {
        return igst;
    }

    public void setIgst(String igst) {
        this.igst = igst;
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    public String getItemGSTNature() {
        return itemGSTNature;
    }

    public void setItemGSTNature(String itemGSTNature) {
        this.itemGSTNature = itemGSTNature;
    }

    public String getHsnCode() {
        return hsnCode;
    }

    public void setHsnCode(String hsnCode) {
        this.hsnCode = hsnCode;
    }

    public String getItsBasedOn() {
        return itsBasedOn;
    }

    public void setItsBasedOn(String itsBasedOn) {
        this.itsBasedOn = itsBasedOn;
    }

    public List<OverHeadITS_Mdl> getLstITS() {
        return lstITS;
    }

    public void setLstITS(List<OverHeadITS_Mdl> lstITS) {
        this.lstITS = lstITS;
    }

    public List<OverHeadLine_Mdl> getOHLListQuotation() {
        return OHLListQuotation;
    }

    public void setOHLListQuotation(List<OverHeadLine_Mdl> OHLListQuotation) {
        this.OHLListQuotation = OHLListQuotation;
    }

    public List<GrdIndentDetail_Mdl> getListIndentDetail() {
        return listIndentDetail;
    }

    public String getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(String orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public String getSkuQty() {
        return skuQty;
    }

    public void setSkuQty(String skuQty) {
        this.skuQty = skuQty;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getReqDate() {
        return reqDate;
    }

    public void setReqDate(String reqDate) {
        this.reqDate = reqDate;
    }

    public String getPurchaseUnitConversionRate() {
        return purchaseUnitConversionRate;
    }

    public void setPurchaseUnitConversionRate(String purchaseUnitConversionRate) {
        this.purchaseUnitConversionRate = purchaseUnitConversionRate;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getvSNo() {
        return vSNo;
    }

    public void setvSNo(String vSNo) {
        this.vSNo = vSNo;
    }

    public String getIsStatus() {
        return isStatus;
    }

    public void setIsStatus(String isStatus) {
        this.isStatus = isStatus;
    }

    public String getChallanQyt() {
        return challanQyt;
    }

    public void setChallanQyt(String challanQyt) {
        this.challanQyt = challanQyt;
    }

    public String getPurchaseUnitDecimalPlaces() {
        return purchaseUnitDecimalPlaces;
    }

    public void setPurchaseUnitDecimalPlaces(String purchaseUnitDecimalPlaces) {
        this.purchaseUnitDecimalPlaces = purchaseUnitDecimalPlaces;
    }

    public String getSkuDecimalPlaces() {
        return skuDecimalPlaces;
    }

    public void setSkuDecimalPlaces(String skuDecimalPlaces) {
        this.skuDecimalPlaces = skuDecimalPlaces;
    }

    public String getItemProperty() {
        return itemProperty;
    }

    public void setItemProperty(String itemProperty) {
        this.itemProperty = itemProperty;
    }

    public String getIsBestBefore() {
        return isBestBefore;
    }

    public void setIsBestBefore(String isBestBefore) {
        this.isBestBefore = isBestBefore;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getSizeCode() {
        return sizeCode;
    }

    public void setSizeCode(String sizeCode) {
        this.sizeCode = sizeCode;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public String getQuotationDocId() {
        return quotationDocId;
    }

    public void setQuotationDocId(String quotationDocId) {
        this.quotationDocId = quotationDocId;
    }

    public String getQuotationV_SNo() {
        return quotationV_SNo;
    }

    public void setQuotationV_SNo(String quotationV_SNo) {
        this.quotationV_SNo = quotationV_SNo;
    }

    public Tolerance_Mdl getToleranceMdl() {
        return toleranceMdl;
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

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public boolean isServiceRecord() {
        return serviceRecord;
    }

    public void setServiceRecord(boolean serviceRecord) {
        this.serviceRecord = serviceRecord;
    }

    public String getSmfCode() {
        return smfCode;
    }

    public void setSmfCode(String smfCode) {
        this.smfCode = smfCode;
    }

    public String getServiceEvalTypeCode() {
        return serviceEvalTypeCode;
    }

    public void setServiceEvalTypeCode(String serviceEvalTypeCode) {
        this.serviceEvalTypeCode = serviceEvalTypeCode;
    }

    public String getServiceEvalTypeName() {
        return serviceEvalTypeName;
    }

    public void setServiceEvalTypeName(String serviceEvalTypeName) {
        this.serviceEvalTypeName = serviceEvalTypeName;
    }

    public String getServiceEvalRate() {
        return serviceEvalRate;
    }

    public void setServiceEvalRate(String serviceEvalRate) {
        this.serviceEvalRate = serviceEvalRate;
    }

    public String getLastRateComparison() {
        return lastRateComparison;
    }

    public void setLastRateComparison(String lastRateComparison) {
        this.lastRateComparison = lastRateComparison;
    }

    public String getLastRateComparisonName() {
        return lastRateComparisonName;
    }

    public void setLastRateComparisonName(String lastRateComparisonName) {
        this.lastRateComparisonName = lastRateComparisonName;
    }

    public String getApprovedRateYN() {
        return approvedRateYN;
    }

    public void setApprovedRateYN(String approvedRateYN) {
        this.approvedRateYN = approvedRateYN;
    }

    public String getPurchaseRate() {
        return purchaseRate;
    }

    public void setPurchaseRate(String purchaseRate) {
        this.purchaseRate = purchaseRate;
    }

}
