package aarogya.inventory.workOrder.shared;

import dataman.dmToolkit.overHeadLine.shared.OHLExtended;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Mdl;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GrdItemDetail_Mdl extends KeyField implements Serializable, OHLExtended {

    private String orderQuantity;
    private String skuQty;
    private String purchaseUnitConversionRate;
    private String purchaseUnitCode;
    private String purchaseUnitName;
    private String purchaseUnitDisplayName;
    private String purchaseUnitDecimalPlaces;
    private String skuName;
    private String skuDecimalPlaces;
    private String rate;
    private String amount;
    private String remark;
    private String reqDate;
    private String vSNo;
    private String isStatus;
    private String challanQyt;
    private String itemProperty;
    private String isBestBefore;
    private String colorCode;
    private String color;
    private String sizeCode;
    private String size;
    private String properties;
    private String quotationDocId;
    private String quotationV_SNo;

    private Tolerance_Mdl toleranceMdl = new Tolerance_Mdl();
    private List<GrdIndentDetail_Mdl> listIndentDetail = new ArrayList<GrdIndentDetail_Mdl>();
    private List<OverHeadLine_Mdl> OHLList;
    private List<OverHeadLine_Mdl> OHLListQuotation;

    public GrdItemDetail_Mdl() {
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

    @Override
    public List<OverHeadLine_Mdl> getOHLList() {
        return this.OHLList;
    }

    @Override
    public void setOHLList(List<OverHeadLine_Mdl> OHLList) {
        this.OHLList = OHLList;
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
}
