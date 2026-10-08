package aarogya.inventory.purchaseBillDirect.shared;

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
import dataman.dmToolkit.tdsCategoryPopup.shared.TdsCategoryPopup_Mdl;

public class PurchaseBillDirect_Mdl extends KeyField implements Serializable, IsSerializable {

    private RecId objRecId = new RecId();
    private String wtaCode;
    private String voucherTypeCode;
    private String voucherTypeName;
    private String voucherCategory;
    private String voucherDate;
    private String paymentDueDate;
    private String supplierGstin;
    private String supplierGstinDate;
    private String supplierStateCode;
    private String supplierTag;
    private String supplierText;
    private String supplierBillNo;
    private String supplierBillDate;
    private String calculateOnTag;
    private String calculateOnText;
    private String remark;
    private String approvedBy;
    private String approvedDateTime;
    private String partyTypeForEntry;
    private String partyType;
    private String rcmApplicable;
    private String placeOfSupply;
    private String locationCode;
    private String gateNoTag;
    private String gateNoText;
    private String transporterCode;
    private String transporterName;
    private String ewayNo;
    private String ewayDate;
    private String lrDate;
    private String lrNo;
    private ModifyReason_Mdl modifyReasonMdl;
    private String guiConfigCode;
    private String u_EntDt_LatestLine;
    private String poItemChangeCondition;
    private boolean isPurchaseOrderExists;

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
    private String balancingAc;
    private String lastTransactionDate;
    private String tdsAmt;

    private DefaultValue_Mdl defaultValueMdl;
    private DmGUI_Mdl guiMdl = new DmGUI_Mdl();
    private List<GrdItemDetail_Mdl> grdItemDetail = new ArrayList<GrdItemDetail_Mdl>();
    // private List<OverHead_Mdl> overHeadList = new ArrayList<OverHead_Mdl>();
    private List<ServiceDetail_Mdl> grdServiceDetail = new ArrayList<ServiceDetail_Mdl>();
    private TdsCategoryPopup_Mdl tdsCatMdl = new TdsCategoryPopup_Mdl();

    public PurchaseBillDirect_Mdl() {
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

    public PurchaseBillDirect_Mdl clone() {
        PurchaseBillDirect_Mdl grn_Mdl = new PurchaseBillDirect_Mdl();

        copy(grn_Mdl);

        return grn_Mdl;
    }

    public String getGateNoTag() {
        return gateNoTag;
    }

    public void setGateNoTag(String gateNoTag) {
        this.gateNoTag = gateNoTag;
    }

    public String getGateNoText() {
        return gateNoText;
    }

    public void setGateNoText(String gateNoText) {
        this.gateNoText = gateNoText;
    }

    public String getTransporterCode() {
        return transporterCode;
    }

    public void setTransporterCode(String transporterCode) {
        this.transporterCode = transporterCode;
    }

    public String getTransporterName() {
        return transporterName;
    }

    public void setTransporterName(String transporterName) {
        this.transporterName = transporterName;
    }

    public String getEwayNo() {
        return ewayNo;
    }

    public void setEwayNo(String ewayNo) {
        this.ewayNo = ewayNo;
    }

    public String getEwayDate() {
        return ewayDate;
    }

    public void setEwayDate(String ewayDate) {
        this.ewayDate = ewayDate;
    }

    public String getLrDate() {
        return lrDate;
    }

    public void setLrDate(String lrDate) {
        this.lrDate = lrDate;
    }

    public String getLrNo() {
        return lrNo;
    }

    public void setLrNo(String lrNo) {
        this.lrNo = lrNo;
    }

    public ModifyReason_Mdl getModifyReasonMdl() {
        return modifyReasonMdl;
    }

    public void setModifyReasonMdl(ModifyReason_Mdl modifyReasonMdl) {
        this.modifyReasonMdl = modifyReasonMdl;
    }

    public RecId getObjRecId() {
        return objRecId;
    }

    public void setObjRecId(RecId objRecId) {
        this.objRecId = objRecId;
    }

    public String getWtaCode() {
        return wtaCode;
    }

    public void setWtaCode(String wtaCode) {
        this.wtaCode = wtaCode;
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

    public String getVoucherDate() {
        return voucherDate;
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

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
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

    public String getCalculateOnTag() {
        return calculateOnTag;
    }

    public void setCalculateOnTag(String calculateOnTag) {
        this.calculateOnTag = calculateOnTag;
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

    public String getApprovedDateTime() {
        return approvedDateTime;
    }

    public void setApprovedDateTime(String approvedDateTime) {
        this.approvedDateTime = approvedDateTime;
    }

    public List<GrdItemDetail_Mdl> getGrdItemDetail() {
        return grdItemDetail;
    }

    public String getGuiConfigCode() {
        return guiConfigCode;
    }

    public void setGuiConfigCode(String guiConfigCode) {
        this.guiConfigCode = guiConfigCode;
    }

    public DmGUI_Mdl getGuiMdl() {
        return guiMdl;
    }

    public void setGuiMdl(DmGUI_Mdl guiMdl) {
        this.guiMdl = guiMdl;
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

    public String getPartyType() {
        return partyType;
    }

    public void setPartyType(String partyType) {
        this.partyType = partyType;
    }

    public String getRoundOffAmount() {
        return roundOffAmount;
    }

    public void setRoundOffAmount(String roundOffAmount) {
        this.roundOffAmount = roundOffAmount;
    }

    public String getSupplierStateCode() {
        return supplierStateCode;
    }

    public void setSupplierStateCode(String supplierStateCode) {
        this.supplierStateCode = supplierStateCode;
    }

    public String getU_EntDt_LatestLine() {
        return u_EntDt_LatestLine;
    }

    public void setU_EntDt_LatestLine(String u_EntDt_LatestLine) {
        this.u_EntDt_LatestLine = u_EntDt_LatestLine;
    }

    public String getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(String grossAmount) {
        this.grossAmount = grossAmount;
    }

    public String getPoItemChangeCondition() {
        return poItemChangeCondition;
    }

    public void setPoItemChangeCondition(String poItemChangeCondition) {
        this.poItemChangeCondition = poItemChangeCondition;
    }

    public DefaultValue_Mdl getDefaultValueMdl() {
        return defaultValueMdl;
    }

    public void setDefaultValueMdl(DefaultValue_Mdl defaultValueMdl) {
        this.defaultValueMdl = defaultValueMdl;
    }

    public boolean getIsPurchaseOrderExists() {
        return isPurchaseOrderExists;
    }

    public void setIsPurchaseOrderExists(boolean isPurchaseOrderExists) {
        this.isPurchaseOrderExists = isPurchaseOrderExists;
    }

    public List<ServiceDetail_Mdl> getGrdServiceDetail() {
        return grdServiceDetail;
    }

    public void setGrdServiceDetail(List<ServiceDetail_Mdl> grdServiceDetail) {
        this.grdServiceDetail = grdServiceDetail;
    }

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

    public void copy(PurchaseBillDirect_Mdl pbd_Mdl) {
        pbd_Mdl.setSharedKeyFieldCode(this.getSharedKeyFieldCode());
        pbd_Mdl.setSharedKeyFieldName(this.getSharedKeyFieldName());
        pbd_Mdl.setApprovedBy(this.getApprovedBy());
        pbd_Mdl.setApprovedDateTime(this.getApprovedDateTime());
        pbd_Mdl.setGuiMdl(this.getGuiMdl());
        pbd_Mdl.setGuiConfigCode(this.getGuiConfigCode());
        pbd_Mdl.setU_EntDt_LatestLine(this.getU_EntDt_LatestLine());
        pbd_Mdl.setPoItemChangeCondition(this.getPoItemChangeCondition());
        pbd_Mdl.setModifyReasonMdl(this.getModifyReasonMdl());
        pbd_Mdl.setObjRecId(this.getObjRecId());
        pbd_Mdl.setRemark(this.getRemark());
        pbd_Mdl.setSupplierTag(this.getSupplierTag());
        pbd_Mdl.setSupplierText(this.getSupplierText());
        pbd_Mdl.setSupplierBillNo(this.getSupplierBillNo());
        pbd_Mdl.setSupplierBillDate(this.getSupplierBillDate());
        pbd_Mdl.setCalculateOnTag(this.getCalculateOnTag());
        pbd_Mdl.setCalculateOnText(this.getCalculateOnText());
        pbd_Mdl.setSupplierGstin(this.getSupplierGstin());
        pbd_Mdl.setSupplierGstinDate(this.getSupplierGstinDate());
        pbd_Mdl.setLocationCode(this.getLocationCode());
        pbd_Mdl.setVoucherDate(this.getVoucherDate());
        pbd_Mdl.setVoucherTypeCode(this.getVoucherTypeCode());
        pbd_Mdl.setVoucherTypeName(this.getVoucherTypeName());
        pbd_Mdl.setVoucherCategory(this.getVoucherCategory());
        pbd_Mdl.setWtaCode(this.getWtaCode());
        pbd_Mdl.setPartyTypeForEntry(this.getPartyTypeForEntry());
        pbd_Mdl.setPartyType(this.getPartyType());
        pbd_Mdl.setRcmApplicable(this.getRcmApplicable());
        pbd_Mdl.setPlaceOfSupply(this.getPlaceOfSupply());
        pbd_Mdl.setRoundOffAmount(this.getRoundOffAmount());
        pbd_Mdl.setGrossAmount(this.getGrossAmount());
        pbd_Mdl.setDefaultValueMdl(this.getDefaultValueMdl());
        pbd_Mdl.setIsPurchaseOrderExists(this.getIsPurchaseOrderExists());
        pbd_Mdl.setGateNoTag(this.getGateNoTag());
        pbd_Mdl.setGateNoText(this.getGateNoText());
        pbd_Mdl.setTransporterCode(this.getTransporterCode());
        pbd_Mdl.setTransporterName(this.getTransporterName());
        pbd_Mdl.setEwayNo(this.getEwayNo());
        pbd_Mdl.setEwayDate(this.getEwayDate());
        pbd_Mdl.setLrDate(this.getLrDate());
        pbd_Mdl.setLrNo(this.getLrNo());
        pbd_Mdl.setGrossAmount(this.getGrossAmount());
        pbd_Mdl.setGoodsGrossAmt(this.getGoodsGrossAmt());
        pbd_Mdl.setServiceGrossAmt(this.getServiceGrossAmt());
        pbd_Mdl.setDiscount(this.getDiscount());
        pbd_Mdl.setGoodsTaxableAmt(this.getGoodsTaxableAmt());
        pbd_Mdl.setServiceTaxableAmt(this.getServiceTaxableAmt());
        pbd_Mdl.setTaxableAmt(this.getTaxableAmt());
        pbd_Mdl.setSgstUgst(this.getSgstUgst());
        pbd_Mdl.setSgst(this.getSgst());
        pbd_Mdl.setUgst(this.getUgst());
        pbd_Mdl.setCgst(this.getCgst());
        pbd_Mdl.setIgst(this.getIgst());
        pbd_Mdl.setSubTotal(this.getSubTotal());
        pbd_Mdl.setFinalDiscountPer(this.getFinalDiscountPer());
        pbd_Mdl.setFinalDiscount(this.getFinalDiscount());
        pbd_Mdl.setFinalDiscountAcCode(this.getFinalDiscountAcCode());
        pbd_Mdl.setTotalAmt(this.getTotalAmt());
        pbd_Mdl.setRoundOffAmount(this.getRoundOffAmount());
        pbd_Mdl.setRoundOffAcCode(this.getRoundOffAcCode());
        pbd_Mdl.setNetAmount(this.getNetAmount());
        pbd_Mdl.setLandedValue(this.getLandedValue());
        pbd_Mdl.getGrdItemDetail().addAll(cloneGrdGrnDetailList(this.getGrdItemDetail()));
        pbd_Mdl.getGrdServiceDetail().addAll(cloneGrdServiceDetailList(this.getGrdServiceDetail()));
        pbd_Mdl.setTdsCatMdl(this.getTdsCatMdl());
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

    public String getBalancingAc() {
        return balancingAc;
    }

    public void setBalancingAc(String balancingAc) {
        this.balancingAc = balancingAc;
    }

    public String getLastTransactionDate() {
        return lastTransactionDate;
    }

    public void setLastTransactionDate(String lastTransactionDate) {
        this.lastTransactionDate = lastTransactionDate;
    }

}
