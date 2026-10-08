package aarogya.inventory.purchaseBill.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGUI.shared.DmGUI_Mdl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import aarogya.selfToolkit.serviceGrid.shared.ServiceDetail_Mdl;
import dataman.dmToolkit.tdsCategoryPopup.shared.TdsCategoryPopup_Mdl;

public class PurchaseBill_Mdl extends KeyField implements Serializable, IsSerializable {

    private RecId objRecId = new RecId();
    private ModifyReason_Mdl modifyReasonMdl;
    private String wtaCode;
    private String voucherTypeCode;
    private String voucherTypeName;
    private String voucherCategory;
    private String balancingAc;
    private String supplierTag;
    private String supplierText;
    private String supplierBillNo;
    private String supplierBillDate;

    private String supplierGstin;
    private String supplierGstinDate;
    private String supplierStateCode;
    private String partyType;
    private String partyTypeForEntry;
    private String rcmApplicable;
    private String placeOfSupply;
    private String locationCode;
    private String u_EntDt_LatestLine;
    private String tdsAmt;
    private String voucherDate;
    private String calculateOnTag;
    private String remark;
    private String approvedBy;
    private String approvalDateTime;
    private String calculateOnText;
    private String guiConfigCode;
    private String grnItemChangeCondition;
    private String paymentDueDate;

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
    private String finalDiscountPer;
    private String finalDiscount;
    private String finalDiscountAcCode;
    private String totalAmt;
    private String roundOffAmount;
    private String roundOffAcCode;
    private String netAmount;
    private String landedValue;
    private String lastTransactionDate;

    private DefaultValue_Mdl defaultValueMdl;

    private List<GrdItemDetail_Mdl> listItemDetail = new ArrayList<GrdItemDetail_Mdl>();
    private List<ServiceDetail_Mdl> grdServiceDetail = new ArrayList<ServiceDetail_Mdl>();
    private DmGUI_Mdl guiMdl = new DmGUI_Mdl();
    private TdsCategoryPopup_Mdl tdsCatMdl = new TdsCategoryPopup_Mdl();

    public PurchaseBill_Mdl() {
    }

    public String getTdsAmt() {
        return tdsAmt;
    }

    public void setTdsAmt(String tdsAmt) {
        this.tdsAmt = tdsAmt;
    }

    public TdsCategoryPopup_Mdl getTdsCatMdl() {
        return tdsCatMdl;
    }

    public void setTdsCatMdl(TdsCategoryPopup_Mdl tdsCatMdl) {
        this.tdsCatMdl = tdsCatMdl;
    }

    public List<GrdItemDetail_Mdl> getListItemDetail() {
        return listItemDetail;
    }

    public RecId getObjRecId() {
        return objRecId;
    }

    public void setObjRecId(RecId objRecId) {
        this.objRecId = objRecId;
    }

    public String getVoucherTypeCode() {
        return voucherTypeCode;
    }

    public void setVoucherTypeCode(String voucherTypeCode) {
        this.voucherTypeCode = voucherTypeCode;
    }

    public String getVoucherTypeName() {
        return voucherTypeName;
    }

    public void setVoucherTypeName(String voucherTypeName) {
        this.voucherTypeName = voucherTypeName;
    }

    public String getVoucherCategory() {
        return voucherCategory;
    }

    public void setVoucherCategory(String voucherCategory) {
        this.voucherCategory = voucherCategory;
    }

    public String getBalancingAc() {
        return balancingAc;
    }

    public void setBalancingAc(String balancingAc) {
        this.balancingAc = balancingAc;
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

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public String getCalculateOnTag() {
        return calculateOnTag;
    }

    public void setCalculateOnTag(String calculateOnTag) {
        this.calculateOnTag = calculateOnTag;
    }

    public String getSupplierBillNo() {
        return supplierBillNo;
    }

    public void setSupplierBillNo(String supplierBillNo) {
        this.supplierBillNo = supplierBillNo;
    }

    public String getSupplierBillDate() {
        return supplierBillDate;
    }

    public void setSupplierBillDate(String supplierBillDate) {
        this.supplierBillDate = supplierBillDate;
    }

    public String getCalculateOnText() {
        return calculateOnText;
    }

    public void setCalculateOnText(String calculateOnText) {
        this.calculateOnText = calculateOnText;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getApprovalDateTime() {
        return approvalDateTime;
    }

    public void setApprovalDateTime(String approvalDateTime) {
        this.approvalDateTime = approvalDateTime;
    }

    public String getWtaCode() {
        return wtaCode;
    }

    public void setWtaCode(String wtaCode) {
        this.wtaCode = wtaCode;
    }

    public DmGUI_Mdl getGuiMdl() {
        return guiMdl;
    }

    public void setGuiMdl(DmGUI_Mdl guiMdl) {
        this.guiMdl = guiMdl;
    }

    public ModifyReason_Mdl getModifyReasonMdl() {
        return modifyReasonMdl;
    }

    public void setModifyReasonMdl(ModifyReason_Mdl modifyReasonMdl) {
        this.modifyReasonMdl = modifyReasonMdl;
    }

    public String getGuiConfigCode() {
        return guiConfigCode;
    }

    public void setGuiConfigCode(String guiConfigCode) {
        this.guiConfigCode = guiConfigCode;
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

    public String getSupplierStateCode() {
        return supplierStateCode;
    }

    public void setSupplierStateCode(String supplierStateCode) {
        this.supplierStateCode = supplierStateCode;
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

    public String getPlaceOfSupply() {
        return placeOfSupply;
    }

    public void setPlaceOfSupply(String placeOfSupply) {
        this.placeOfSupply = placeOfSupply;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getU_EntDt_LatestLine() {
        return u_EntDt_LatestLine;
    }

    public void setU_EntDt_LatestLine(String u_EntDt_LatestLine) {
        this.u_EntDt_LatestLine = u_EntDt_LatestLine;
    }

    public String getRoundOffAmount() {
        return roundOffAmount;
    }

    public void setRoundOffAmount(String roundOffAmount) {
        this.roundOffAmount = roundOffAmount;
    }

    public String getGrnItemChangeCondition() {
        return grnItemChangeCondition;
    }

    public void setGrnItemChangeCondition(String grnItemChangeCondition) {
        this.grnItemChangeCondition = grnItemChangeCondition;
    }

    public String getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(String grossAmount) {
        this.grossAmount = grossAmount;
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

    public String getFinalDiscountPer() {
        return finalDiscountPer;
    }

    public void setFinalDiscountPer(String finalDiscountPer) {
        this.finalDiscountPer = finalDiscountPer;
    }

    public String getFinalDiscount() {
        return finalDiscount;
    }

    public void setFinalDiscount(String finalDiscount) {
        this.finalDiscount = finalDiscount;
    }

    public String getFinalDiscountAcCode() {
        return finalDiscountAcCode;
    }

    public void setFinalDiscountAcCode(String finalDiscountAcCode) {
        this.finalDiscountAcCode = finalDiscountAcCode;
    }

    public String getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(String totalAmt) {
        this.totalAmt = totalAmt;
    }

    public String getRoundOffAcCode() {
        return roundOffAcCode;
    }

    public void setRoundOffAcCode(String roundOffAcCode) {
        this.roundOffAcCode = roundOffAcCode;
    }

    public String getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(String netAmount) {
        this.netAmount = netAmount;
    }

    public String getLandedValue() {
        return landedValue;
    }

    public void setLandedValue(String landedValue) {
        this.landedValue = landedValue;
    }

    public DefaultValue_Mdl getDefaultValueMdl() {
        return defaultValueMdl;
    }

    public void setDefaultValueMdl(DefaultValue_Mdl defaultValueMdl) {
        this.defaultValueMdl = defaultValueMdl;
    }

    public List<ServiceDetail_Mdl> getGrdServiceDetail() {
        return grdServiceDetail;
    }

    public void setGrdServiceDetail(List<ServiceDetail_Mdl> grdServiceDetail) {
        this.grdServiceDetail = grdServiceDetail;
    }

    public PurchaseBill_Mdl clone() {
        PurchaseBill_Mdl grn_Mdl = new PurchaseBill_Mdl();

        copy(grn_Mdl);

        return grn_Mdl;
    }

    public void copy(PurchaseBill_Mdl grn_Mdl) {
        grn_Mdl.setSharedKeyFieldCode(this.getSharedKeyFieldCode());
        grn_Mdl.setSharedKeyFieldName(this.getSharedKeyFieldName());
        grn_Mdl.setApprovalDateTime(this.getApprovalDateTime());
        grn_Mdl.setApprovedBy(this.getApprovedBy());
        grn_Mdl.setCalculateOnTag(this.getCalculateOnTag());
        grn_Mdl.setCalculateOnText(this.getCalculateOnText());
        grn_Mdl.setGuiMdl(this.getGuiMdl());
        grn_Mdl.setModifyReasonMdl(this.getModifyReasonMdl());
        grn_Mdl.setObjRecId(this.getObjRecId());
        grn_Mdl.setRemark(this.getRemark());
        grn_Mdl.setSupplierBillDate(this.getSupplierBillDate());
        grn_Mdl.setSupplierBillNo(this.getSupplierBillNo());
        grn_Mdl.setSupplierTag(this.getSupplierTag());
        grn_Mdl.setSupplierText(this.getSupplierText());
        grn_Mdl.setVoucherDate(this.getVoucherDate());
        grn_Mdl.setVoucherTypeCode(this.getVoucherTypeCode());
        grn_Mdl.setVoucherTypeName(this.getVoucherTypeName());
        grn_Mdl.setVoucherCategory(this.getVoucherCategory());
        grn_Mdl.setBalancingAc(this.getBalancingAc());
        grn_Mdl.setWtaCode(this.getWtaCode());
        grn_Mdl.setGuiConfigCode(this.getGuiConfigCode());
        grn_Mdl.setSupplierGstin(this.getSupplierGstin());
        grn_Mdl.setSupplierGstinDate(this.getSupplierGstinDate());
        grn_Mdl.setSupplierStateCode(this.getSupplierStateCode());
        grn_Mdl.setPartyType(this.getPartyType());
        grn_Mdl.setPartyTypeForEntry(this.getPartyTypeForEntry());
        grn_Mdl.setRcmApplicable(this.getRcmApplicable());
        grn_Mdl.setPlaceOfSupply(this.getPlaceOfSupply());
        grn_Mdl.setU_EntDt_LatestLine(this.getU_EntDt_LatestLine());
        grn_Mdl.setGrnItemChangeCondition(this.getGrnItemChangeCondition());
        grn_Mdl.setGoodsGrossAmt(this.getGoodsGrossAmt());
        grn_Mdl.setServiceGrossAmt(this.getServiceGrossAmt());
        grn_Mdl.setDiscount(this.getDiscount());
        grn_Mdl.setGoodsTaxableAmt(this.getGoodsTaxableAmt());
        grn_Mdl.setServiceTaxableAmt(this.getServiceTaxableAmt());
        grn_Mdl.setTaxableAmt(this.getTaxableAmt());
        grn_Mdl.setSgstUgst(this.getSgstUgst());
        grn_Mdl.setSgst(this.getSgst());
        grn_Mdl.setUgst(this.getUgst());
        grn_Mdl.setCgst(this.getCgst());
        grn_Mdl.setIgst(this.getIgst());
        grn_Mdl.setSubTotal(this.getSubTotal());
        grn_Mdl.setFinalDiscountPer(this.getFinalDiscountPer());
        grn_Mdl.setFinalDiscount(this.getFinalDiscount());
        grn_Mdl.setFinalDiscountAcCode(this.getFinalDiscountAcCode());
        grn_Mdl.setTotalAmt(this.getTotalAmt());
        grn_Mdl.setRoundOffAmount(this.getRoundOffAmount());
        grn_Mdl.setRoundOffAcCode(this.getRoundOffAcCode());
        grn_Mdl.setNetAmount(this.getNetAmount());
        grn_Mdl.setLandedValue(this.getLandedValue());
        grn_Mdl.setDefaultValueMdl(this.getDefaultValueMdl());

        grn_Mdl.getListItemDetail().addAll(cloneGrdGrnDetailList(this.getListItemDetail()));
        grn_Mdl.getGrdServiceDetail().addAll(cloneGrdServiceDetailList(this.getGrdServiceDetail()));
        grn_Mdl.setTdsCatMdl(this.getTdsCatMdl());
    }

    public List<GrdItemDetail_Mdl> cloneGrdGrnDetailList(List<GrdItemDetail_Mdl> list) {
        List<GrdItemDetail_Mdl> grdGrn_MdlList = null;

        if (list != null) {
            grdGrn_MdlList = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                grdGrn_MdlList.add(list.get(i).clone());
            }
        }

        return grdGrn_MdlList;
    }

    public List<ServiceDetail_Mdl> cloneGrdServiceDetailList(List<ServiceDetail_Mdl> list) {
        List<ServiceDetail_Mdl> grdServiceDetail_Mdls = null;

        if (list != null) {
            grdServiceDetail_Mdls = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                grdServiceDetail_Mdls.add(list.get(i).clone());
            }
        }

        return grdServiceDetail_Mdls;
    }

    public String getPaymentDueDate() {
        return paymentDueDate;
    }

    public void setPaymentDueDate(String paymentDueDate) {
        this.paymentDueDate = paymentDueDate;
    }

    public String getLastTransactionDate() {
        return lastTransactionDate;
    }

    public void setLastTransactionDate(String lastTransactionDate) {
        this.lastTransactionDate = lastTransactionDate;
    }

}
