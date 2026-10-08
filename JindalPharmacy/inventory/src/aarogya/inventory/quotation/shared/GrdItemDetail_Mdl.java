package aarogya.inventory.quotation.shared;

import dataman.dmToolkit.overHeadLine.shared.OHLExtended;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GrdItemDetail_Mdl extends KeyField implements Serializable, OHLExtended {

    private String quantity;
    private String remark;
    private String deliveryDate;
    private String decimalPlaces;
    private String skuDecimalPlaces;
    private String propertyList;
    private String colourCode;
    private String colourName;
    private String sizeCode;
    private String sizeName;

    private String itemMakeCode;
    private String itemMakeName;

    private String vSNo;
    private String purchaseUnitCode;
    private String purchaseUnitName;
    private String skuQuantity;
    private String conversionRate;
    private String enquiryV_SNo;
    private String enquiryDocId;
    private String properties;
    private String displayUnit;

    //Item Details
    private String itemGSTNature;
    private boolean manualTax;
    private boolean manuaGstType;
    private String gstTypeCode;
    private String gstTypeName;
    private String itsBasedOn;
    private String hsnCode;
    private String isBestBefore;
    private String hsnNo;

    //Rate
    private String rate;
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

    //Servcie
    private boolean serviceRecord;
    private String smfCode;
    private String serviceEvalTypeCode;
    private String serviceEvalTypeName;
    private String serviceEvalRate;

    private List<GrdIndentDetail_Mdl> listIndentDetail = new ArrayList<GrdIndentDetail_Mdl>();

    private List<OverHeadLine_Mdl> lstOHL;

    public GrdItemDetail_Mdl() {
    }

    public String getItemGSTNature() {
        return itemGSTNature;
    }

    public void setItemGSTNature(String itemGSTNature) {
        this.itemGSTNature = itemGSTNature;
    }

    public boolean isManualTax() {
        return manualTax;
    }

    public void setManualTax(boolean manualTax) {
        this.manualTax = manualTax;
    }

    public boolean isManuaGstType() {
        return manuaGstType;
    }

    public void setManuaGstType(boolean manuaGstType) {
        this.manuaGstType = manuaGstType;
    }

    public String getGstTypeCode() {
        return gstTypeCode;
    }

    public void setGstTypeCode(String gstTypeCode) {
        this.gstTypeCode = gstTypeCode;
    }

    public String getGstTypeName() {
        return gstTypeName;
    }

    public void setGstTypeName(String gstTypeName) {
        this.gstTypeName = gstTypeName;
    }

    public String getItsBasedOn() {
        return itsBasedOn;
    }

    public void setItsBasedOn(String itsBasedOn) {
        this.itsBasedOn = itsBasedOn;
    }

    public String getHsnCode() {
        return hsnCode;
    }

    public void setHsnCode(String hsnCode) {
        this.hsnCode = hsnCode;
    }

    public String getIsBestBefore() {
        return isBestBefore;
    }

    public void setIsBestBefore(String isBestBefore) {
        this.isBestBefore = isBestBefore;
    }

    public String getHsnNo() {
        return hsnNo;
    }

    public void setHsnNo(String hsnNo) {
        this.hsnNo = hsnNo;
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

    public String getDisplayUnit() {
        return displayUnit;
    }

    public void setDisplayUnit(String displayUnit) {
        this.displayUnit = displayUnit;
    }

    public String getSkuDecimalPlaces() {
        return skuDecimalPlaces;
    }

    public void setSkuDecimalPlaces(String skuDecimalPlaces) {
        this.skuDecimalPlaces = skuDecimalPlaces;
    }

    public List<GrdIndentDetail_Mdl> getListIndentDetail() {
        return listIndentDetail;
    }

    @Override
    public List<OverHeadLine_Mdl> getOHLList() {
        return this.lstOHL;
    }

    @Override
    public void setOHLList(List<OverHeadLine_Mdl> ohlList) {
        this.lstOHL = ohlList;
    }

    public String getPropertyList() {
        return propertyList;
    }

    public void setPropertyList(String propertyList) {
        this.propertyList = propertyList;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
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

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDecimalPlaces() {
        return decimalPlaces;
    }

    public void setDecimalPlaces(String decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
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

    public String getvSNo() {
        return vSNo;
    }

    public void setvSNo(String vSNo) {
        this.vSNo = vSNo;
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

    public String getSkuQuantity() {
        return skuQuantity;
    }

    public void setSkuQuantity(String skuQuantity) {
        this.skuQuantity = skuQuantity;
    }

    public String getConversionRate() {
        return conversionRate;
    }

    public void setConversionRate(String conversionRate) {
        this.conversionRate = conversionRate;
    }

    public List<OverHeadLine_Mdl> getLstOHL() {
        return lstOHL;
    }

    public void setLstOHL(List<OverHeadLine_Mdl> lstOHL) {
        this.lstOHL = lstOHL;
    }

    public String getEnquiryV_SNo() {
        return enquiryV_SNo;
    }

    public void setEnquiryV_SNo(String enquiryV_SNo) {
        this.enquiryV_SNo = enquiryV_SNo;
    }

    public String getEnquiryDocId() {
        return enquiryDocId;
    }

    public void setEnquiryDocId(String enquiryDocId) {
        this.enquiryDocId = enquiryDocId;
    }

}
