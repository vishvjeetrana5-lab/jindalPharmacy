package aarogya.pharmacy.purchaseOrder.shared;

import dataman.dmToolkit.overHeadLine.shared.OHLExtended;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.overHeadLine.shared.OverHeadITS_Mdl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GrdMedicineDetail_Mdl extends KeyField implements Serializable, OHLExtended {

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
    private String purchaseRate;
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
    private String quotationDocId;
    private String quotationV_SNo;
    private String lastPurchaseDiscount;
    private String approvedRateYN;

    private String lastRateComparison;
    private String lastRateComparisonName;
    private String itsBasedOn;
    private String mrp;
    private String medicineCompanyCode;
    private String medicineCompanyName;

    private Tolerance_Mdl toleranceMdl = new Tolerance_Mdl();
    private List<GrdIndentDetail_Mdl> listIndentDetail = new ArrayList<GrdIndentDetail_Mdl>();
    private List<OverHeadLine_Mdl> OHLList;
    private List<OverHeadLine_Mdl> OHLListQuotation;
    private List<OverHeadITS_Mdl> lstITS;

    public GrdMedicineDetail_Mdl() {
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

    public String getPurchaseUnitConversionRate() {
        return purchaseUnitConversionRate;
    }

    public void setPurchaseUnitConversionRate(String purchaseUnitConversionRate) {
        this.purchaseUnitConversionRate = purchaseUnitConversionRate;
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

    public String getPurchaseUnitDecimalPlaces() {
        return purchaseUnitDecimalPlaces;
    }

    public void setPurchaseUnitDecimalPlaces(String purchaseUnitDecimalPlaces) {
        this.purchaseUnitDecimalPlaces = purchaseUnitDecimalPlaces;
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

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSizeCode() {
        return sizeCode;
    }

    public void setSizeCode(String sizeCode) {
        this.sizeCode = sizeCode;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
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

    public List<GrdIndentDetail_Mdl> getListIndentDetail() {
        return listIndentDetail;
    }

    @Override
    public List<OverHeadLine_Mdl> getOHLList() {
        return this.OHLList;
    }

    @Override
    public void setOHLList(List<OverHeadLine_Mdl> ohlList) {
        this.OHLList = ohlList;
    }

    public List<OverHeadLine_Mdl> getOHLListQuotation() {
        return OHLListQuotation;
    }

    public void setOHLListQuotation(List<OverHeadLine_Mdl> OHLListQuotation) {
        this.OHLListQuotation = OHLListQuotation;
    }

    public String gretlastPurchaseDiscount() {
        return lastPurchaseDiscount;
    }

    public void setlastPurchaseDiscount(String lastPurchaseDiscount) {
        this.lastPurchaseDiscount = lastPurchaseDiscount;
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

    public String getLastPurchaseDiscount() {
        return lastPurchaseDiscount;
    }

    public void setLastPurchaseDiscount(String lastPurchaseDiscount) {
        this.lastPurchaseDiscount = lastPurchaseDiscount;
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

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getMedicineCompanyCode() {
        return medicineCompanyCode;
    }

    public void setMedicineCompanyCode(String medicineCompanyCode) {
        this.medicineCompanyCode = medicineCompanyCode;
    }

    public String getMedicineCompanyName() {
        return medicineCompanyName;
    }

    public void setMedicineCompanyName(String medicineCompanyName) {
        this.medicineCompanyName = medicineCompanyName;
    }

}
