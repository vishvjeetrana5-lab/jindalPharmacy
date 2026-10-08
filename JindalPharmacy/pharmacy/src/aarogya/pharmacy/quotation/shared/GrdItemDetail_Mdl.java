package aarogya.pharmacy.quotation.shared;

import dataman.dmToolkit.overHeadLine.shared.OHLExtended;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.overHeadLine.shared.OverHeadITS_Mdl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GrdItemDetail_Mdl extends KeyField implements Serializable, OHLExtended {

    private String itsBasedOn;
    private String quantity;
    private String rate;
    private String amount;
    private String remark;
    private String vSNo;
    private String deliveryDate;
    private String decimalPlacesPurchaseUnit;
    private String skuDecimalPlaces;
    private String purchaseUnitCode;
    private String purchaseUnitName;
    private String skuQuantity;
    private String conversionRate;
    private String enquiryV_SNo;
    private String enquiryDocId;
    private String displayUnit;
    private String mrp;
    private String discountper;

    private  List<OverHeadITS_Mdl> overHeadITSList = new ArrayList<>();
    private List<GrdIndentDetail_Mdl> listIndentDetail = new ArrayList<GrdIndentDetail_Mdl>();
    private List<OverHeadLine_Mdl> lstOHL;
    private List<OverHeadITS_Mdl> lstITS;

    public GrdItemDetail_Mdl() {
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

    public String getDecimalPlacesPurchaseUnit() {
        return decimalPlacesPurchaseUnit;
    }

    public void setDecimalPlacesPurchaseUnit(String decimalPlacesPurchaseUnit) {
        this.decimalPlacesPurchaseUnit = decimalPlacesPurchaseUnit;
    }

    public String getDisplayUnit() {
        return displayUnit;
    }

    public void setDisplayUnit(String displayUnit) {
        this.displayUnit = displayUnit;
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
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

    public List<OverHeadLine_Mdl> getLstOHL() {
        return lstOHL;
    }

    public void setLstOHL(List<OverHeadLine_Mdl> lstOHL) {
        this.lstOHL = lstOHL;
    }

    public String getSkuDecimalPlaces() {
        return skuDecimalPlaces;
    }

    public void setSkuDecimalPlaces(String skuDecimalPlaces) {
        this.skuDecimalPlaces = skuDecimalPlaces;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getDiscountper() {
        return discountper;
    }

    public void setDiscountper(String discountper) {
        this.discountper = discountper;
    }

    public List<OverHeadITS_Mdl> getOverHeadITSList() {
        return overHeadITSList;
    }

}
