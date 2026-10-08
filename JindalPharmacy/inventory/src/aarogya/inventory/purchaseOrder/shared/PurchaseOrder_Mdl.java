package aarogya.inventory.purchaseOrder.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmToolkit.overHead.shared.OverHead_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.RecId;
import dataman.dmToolkit.dmGUI.shared.DmGUI_Mdl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import aarogya.selfToolkit.serviceGrid.shared.ServiceDetail_Mdl;

public class PurchaseOrder_Mdl extends KeyField implements Serializable, IsSerializable {

    private RecId recIdOrder = new RecId();
    private String orderNoDate;
    private String refNo;
    private String refDate;
    private String quotationDate;
    private String quotationNoTag;
    private String quotationNoText;
    private String voucherTypeTag;
    private String voucherCategory;
    private String voucherDate;
    private String voucherTypeText;
    private String enquiryTag;
    private String enquiryText;
    private String indentNoTag;
    private String indentNoText;
    private String wtaCode;
    private String u_EntDt_LatestLine;
    private DmGUI_Mdl guiMdl = new DmGUI_Mdl();
    private String guiConfigCode;
    private String isShowUsedQuotationsAlso;
    private String quotationItemChangeCondition;
    private String indItemChangeCondition;

    private String amdOrdDocId;
    private String amdOrdNo;
    private String amdOrdDate;

    private String supplierTag;
    private String supplierText;
    private String supplierGstin;
    private String supplierGstinDate;
    private String supplierStateCode;
    private String partyType;
    private String partyTypeForEntry;
    private String rcmApplicable;
    private String placeOfSupply;
    private String locationCode;

    private String grossAmount;
    private String goodsGrossAmt;
    private String serviceGrossAmt;
    private String discount;
    private String goodsTaxableAmt;
    private String serviceTaxableAmt;
    private String taxableAmt;
    private String sgstUgst;
    private String sgst;
    private String ugst;
    private String cgst;
    private String igst;
    private String subTotal;
    private String totalAmt;
    private String roundOffAmount;
    private String netAmount;

    private List<GrdItemDetail_Mdl> grdItemDetail = new ArrayList<GrdItemDetail_Mdl>();
    private List<ServiceDetail_Mdl> grdServiceDetail = new ArrayList<ServiceDetail_Mdl>();
    private List<GrdIndentDetail_Mdl> gridIndentDetail = new ArrayList<GrdIndentDetail_Mdl>();
    private List<OverHead_Mdl> overHeadList = new ArrayList<OverHead_Mdl>();
    private String modifiedBy;
    private String preparedBy;
    private String approvedBy;
    private String tempKeyCode;
    private ModifyReason_Mdl modifyReasonMdl;

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getGoodsGrossAmt() {
        return goodsGrossAmt;
    }

    public void setGoodsGrossAmt(String goodsGrossAmt) {
        this.goodsGrossAmt = goodsGrossAmt;
    }

    public String getServiceGrossAmt() {
        return serviceGrossAmt;
    }

    public void setServiceGrossAmt(String serviceGrossAmt) {
        this.serviceGrossAmt = serviceGrossAmt;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getGoodsTaxableAmt() {
        return goodsTaxableAmt;
    }

    public void setGoodsTaxableAmt(String goodsTaxableAmt) {
        this.goodsTaxableAmt = goodsTaxableAmt;
    }

    public String getServiceTaxableAmt() {
        return serviceTaxableAmt;
    }

    public void setServiceTaxableAmt(String serviceTaxableAmt) {
        this.serviceTaxableAmt = serviceTaxableAmt;
    }

    public String getTaxableAmt() {
        return taxableAmt;
    }

    public void setTaxableAmt(String taxableAmt) {
        this.taxableAmt = taxableAmt;
    }

    public String getSgstUgst() {
        return sgstUgst;
    }

    public void setSgstUgst(String sgstUgst) {
        this.sgstUgst = sgstUgst;
    }

    public String getSgst() {
        return sgst;
    }

    public void setSgst(String sgst) {
        this.sgst = sgst;
    }

    public String getUgst() {
        return ugst;
    }

    public void setUgst(String ugst) {
        this.ugst = ugst;
    }

    public String getCgst() {
        return cgst;
    }

    public void setCgst(String cgst) {
        this.cgst = cgst;
    }

    public String getIgst() {
        return igst;
    }

    public void setIgst(String igst) {
        this.igst = igst;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(String totalAmt) {
        this.totalAmt = totalAmt;
    }

    public String getRoundOffAmount() {
        return roundOffAmount;
    }

    public void setRoundOffAmount(String roundOffAmount) {
        this.roundOffAmount = roundOffAmount;
    }

    public String getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(String netAmount) {
        this.netAmount = netAmount;
    }

    public List<ServiceDetail_Mdl> getGrdServiceDetail() {
        return grdServiceDetail;
    }

    public void setGrdServiceDetail(List<ServiceDetail_Mdl> grdServiceDetail) {
        this.grdServiceDetail = grdServiceDetail;
    }

    public PurchaseOrder_Mdl() {
    }

    public String getAmdOrdNo() {
        return amdOrdNo;
    }

    public void setAmdOrdNo(String amdOrdNo) {
        this.amdOrdNo = amdOrdNo;
    }

    public String getAmdOrdDate() {
        return amdOrdDate;
    }

    public void setAmdOrdDate(String amdOrdDate) {
        this.amdOrdDate = amdOrdDate;
    }

    public String getAmdOrdDocId() {
        return amdOrdDocId;
    }

    public void setAmdOrdDocId(String amdOrdDocId) {
        this.amdOrdDocId = amdOrdDocId;
    }

    public String getPlaceOfSupply() {
        return placeOfSupply;
    }

    public void setPlaceOfSupply(String placeOfSupply) {
        this.placeOfSupply = placeOfSupply;
    }

    public String getSupplierStateCode() {
        return supplierStateCode;
    }

    public void setSupplierStateCode(String supplierStateCode) {
        this.supplierStateCode = supplierStateCode;
    }

    public String getSupplierGstin() {
        return supplierGstin;
    }

    public void setSupplierGstin(String supplierGstin) {
        this.supplierGstin = supplierGstin;
    }

    public String getSupplierGstinDate() {
        return supplierGstinDate;
    }

    public void setSupplierGstinDate(String supplierGstinDate) {
        this.supplierGstinDate = supplierGstinDate;
    }

    public String getPartyType() {
        return partyType;
    }

    public void setPartyType(String partyType) {
        this.partyType = partyType;
    }

    public String getPartyTypeForEntry() {
        return partyTypeForEntry;
    }

    public void setPartyTypeForEntry(String partyTypeForEntry) {
        this.partyTypeForEntry = partyTypeForEntry;
    }

    public String getRcmApplicable() {
        return rcmApplicable;
    }

    public void setRcmApplicable(String rcmApplicable) {
        this.rcmApplicable = rcmApplicable;
    }

    public String getIndItemChangeCondition() {
        return indItemChangeCondition;
    }

    public void setIndItemChangeCondition(String indItemChangeCondition) {
        this.indItemChangeCondition = indItemChangeCondition;
    }

    public ModifyReason_Mdl getModifyReasonMdl() {
        return modifyReasonMdl;
    }

    public void setModifyReasonMdl(ModifyReason_Mdl modifyReasonMdl) {
        this.modifyReasonMdl = modifyReasonMdl;
    }

    public List<OverHead_Mdl> getOverHeadList() {
        return overHeadList;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public String getGuiConfigCode() {
        return guiConfigCode;
    }

    public void setGuiConfigCode(String guiConfigCode) {
        this.guiConfigCode = guiConfigCode;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getPreparedBy() {
        return preparedBy;
    }

    public void setPreparedBy(String preparedBy) {
        this.preparedBy = preparedBy;
    }

    public String getOrderNoDate() {
        return orderNoDate;
    }

    public void setOrderNoDate(String orderNoDate) {
        this.orderNoDate = orderNoDate;
    }

    public String getSupplierTag() {
        return supplierTag;
    }

    public void setSupplierTag(String supplierTag) {
        this.supplierTag = supplierTag;
    }

    public String getSupplierText() {
        return supplierText;
    }

    public void setSupplierText(String supplierText) {
        this.supplierText = supplierText;
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

    public String getQuotationDate() {
        return quotationDate;
    }

    public void setQuotationDate(String quotationDate) {
        this.quotationDate = quotationDate;
    }

    public String getQuotationNoTag() {
        return quotationNoTag;
    }

    public void setQuotationNoTag(String quotationNoTag) {
        this.quotationNoTag = quotationNoTag;
    }

    public String getQuotationNoText() {
        return quotationNoText;
    }

    public void setQuotationNoText(String quotationNoText) {
        this.quotationNoText = quotationNoText;
    }

    public String getIndentNoTag() {
        return indentNoTag;
    }

    public void setIndentNoTag(String indentNoTag) {
        this.indentNoTag = indentNoTag;
    }

    public String getIndentNoText() {
        return indentNoText;
    }

    public void setIndentNoText(String indentNoText) {
        this.indentNoText = indentNoText;
    }

    public String getEnquiryTag() {
        return enquiryTag;
    }

    public void setEnquiryTag(String enquiryTag) {
        this.enquiryTag = enquiryTag;
    }

    public String getEnquiryText() {
        return enquiryText;
    }

    public void setEnquiryText(String enquiryText) {
        this.enquiryText = enquiryText;
    }

    public String getVoucherTypeTag() {
        return voucherTypeTag;
    }

    public void setVoucherTypeTag(String voucherTypeTag) {
        this.voucherTypeTag = voucherTypeTag;
    }

    public String getVoucherTypeText() {
        return voucherTypeText;
    }

    public void setVoucherTypeText(String voucherTypeText) {
        this.voucherTypeText = voucherTypeText;
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public List<GrdItemDetail_Mdl> getGrdItemDetail() {
        return grdItemDetail;
    }

    public List<GrdIndentDetail_Mdl> getGridIndentDetail() {
        return gridIndentDetail;
    }

    public RecId getRecIdOrder() {
        return recIdOrder;
    }

    public void setRecIdOrder(RecId recIdOrder) {
        this.recIdOrder = recIdOrder;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getWtaCode() {
        return wtaCode;
    }

    public void setWtaCode(String wtaCode) {
        this.wtaCode = wtaCode;
    }

    public String getU_EntDt_LatestLine() {
        return u_EntDt_LatestLine;
    }

    public void setU_EntDt_LatestLine(String u_EntDt_LatestLine) {
        this.u_EntDt_LatestLine = u_EntDt_LatestLine;
    }

    public DmGUI_Mdl getGuiMdl() {
        return guiMdl;
    }

    public void setGuiMdl(DmGUI_Mdl guiMdl) {
        this.guiMdl = guiMdl;
    }

    public String getIsShowUsedQuotationsAlso() {
        return isShowUsedQuotationsAlso;
    }

    public void setIsShowUsedQuotationsAlso(String isShowUsedQuotationsAlso) {
        this.isShowUsedQuotationsAlso = isShowUsedQuotationsAlso;
    }

    public String getQuotationItemChangeCondition() {
        return quotationItemChangeCondition;
    }

    public void setQuotationItemChangeCondition(String quotationItemChangeCondition) {
        this.quotationItemChangeCondition = quotationItemChangeCondition;
    }

    public String getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(String grossAmount) {
        this.grossAmount = grossAmount;
    }

    public String getTempKeyCode() {
        return tempKeyCode;
    }

    public void setTempKeyCode(String tempKeyCode) {
        this.tempKeyCode = tempKeyCode;
    }

    public String getVoucherCategory() {
        return voucherCategory;
    }

    public void setVoucherCategory(String voucherCategory) {
        this.voucherCategory = voucherCategory;
    }

}
