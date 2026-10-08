package aarogya.inventory.grn.shared;

import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.RecId;
import dataman.dmToolkit.dmGUI.shared.DmGUI_Mdl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import aarogya.selfToolkit.serviceGrid.shared.ServiceDetail_Mdl;

public class Grn_Mdl extends KeyField implements Serializable, IsSerializable {

    private RecId recIdGRN = new RecId();
    private String wtaCode;
    private String voucherTypeCode;
    private String voucherTypeName;
    private String voucherCategory;
    private String balancingAc;
    private String voucherDate;
    private String supplierGstin;
    private String supplierGstinDate;
    private String supplierStateCode;
    private String supplierTag;
    private String supplierText;
    private String challanNo;
    private String challanDate;
    private String remark;
    private String approvedBy;
    private String approvedDateTime;
    private String guiConfigCode;
    private String partyType;
    private String partyTypeForEntry;
    private String locationCode;
    private String placeOfSupply;
    private String rcmApplicable;
    private String u_EntDt_LatestLine;
    private String poItemChangeCondition;
 
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
    
    
    private boolean isPurchaseOrderExists;
    private String gateNoTag;
    private String gateNoText;
    private String transporterCode;
    private String transporterName;
    private String ewayNo;
    private String ewayDate;
    private String lrDate;
    private String lrNo;
    private String lastTransactionDate;
    private boolean inventoryAccountingActive = false;
    private ModifyReason_Mdl modifyReasonMdl;
    private DefaultValue_Mdl defaultValueMdl;
    private DmGUI_Mdl guiMdl = new DmGUI_Mdl();
    private List<GrdGrnDetail_Mdl> grdGrnDetail = new ArrayList<GrdGrnDetail_Mdl>();
    private List<ServiceDetail_Mdl> grdServiceDetail = new ArrayList<ServiceDetail_Mdl>();

    public Grn_Mdl() {
    }

    public DefaultValue_Mdl getDefaultValueMdl() {
        return defaultValueMdl;
    }

    public void setDefaultValueMdl(DefaultValue_Mdl defaultValueMdl) {
        this.defaultValueMdl = defaultValueMdl;
    }

    public ModifyReason_Mdl getModifyReasonMdl() {
        return modifyReasonMdl;
    }

    public void setModifyReasonMdl(ModifyReason_Mdl modifyReasonMdl) {
        this.modifyReasonMdl = modifyReasonMdl;
    }

    public RecId getRecIdGRN() {
        return recIdGRN;
    }

    public void setRecIdGRN(RecId recIdGRN) {
        this.recIdGRN = recIdGRN;
    }

    public String getWtaCode() {
        return wtaCode;
    }

    public void setWtaCode(String wtaCode) {
        this.wtaCode = wtaCode;
    }

    public String getBalancingAc() {
        return balancingAc;
    }

    public void setBalancingAc(String balancingAc) {
        this.balancingAc = balancingAc;
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

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
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

    public String getChallanNo() {
        return challanNo;
    }

    public void setChallanNo(String challanNo) {
        this.challanNo = challanNo;
    }

    public String getChallanDate() {
        return challanDate;
    }

    public void setChallanDate(String challanDate) {
        this.challanDate = challanDate;
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

    public List<GrdGrnDetail_Mdl> getGrdGrnDetail() {
        return grdGrnDetail;
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

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }
    
    public String getPlaceOfSupply() {
        return placeOfSupply;
    }

    public void setPlaceOfSupply(String placeOfSupply) {
        this.placeOfSupply = placeOfSupply;
    }

    public String getRcmApplicable() {
        return rcmApplicable;
    }

    public void setRcmApplicable(String rcmApplicable) {
        this.rcmApplicable = rcmApplicable;
    }

    public String getU_EntDt_LatestLine() {
        return u_EntDt_LatestLine;
    }

    public void setU_EntDt_LatestLine(String u_EntDt_LatestLine) {
        this.u_EntDt_LatestLine = u_EntDt_LatestLine;
    }

    public String getPoItemChangeCondition() {
        return poItemChangeCondition;
    }

    public void setPoItemChangeCondition(String poItemChangeCondition) {
        this.poItemChangeCondition = poItemChangeCondition;
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

    public String getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(String totalAmt) {
        this.totalAmt = totalAmt;
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
    
    public String getRoundOffAmount() {
        return roundOffAmount;
    }

    public void setRoundOffAmount(String roundOffAmount) {
        this.roundOffAmount = roundOffAmount;
    }

    public String getPartyTypeForEntry() {
        return partyTypeForEntry;
    }

    public void setPartyTypeForEntry(String partyTypeForEntry) {
        this.partyTypeForEntry = partyTypeForEntry;
    }

    public String getPartyType() {
        return partyType;
    }

    public void setPartyType(String partyType) {
        this.partyType = partyType;
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

    public String getFinalDiscountAcCode() {
        return finalDiscountAcCode;
    }

    public void setFinalDiscountAcCode(String finalDiscountAcCode) {
        this.finalDiscountAcCode = finalDiscountAcCode;
    }

    public String getRoundOffAcCode() {
        return roundOffAcCode;
    }

    public void setRoundOffAcCode(String roundOffAcCode) {
        this.roundOffAcCode = roundOffAcCode;
    }

    public boolean isInventoryAccountingActive() {
        return inventoryAccountingActive;
    }

    public void setInventoryAccountingActive(boolean inventoryAccountingActive) {
        this.inventoryAccountingActive = inventoryAccountingActive;
    }

    public Grn_Mdl clone() {
        Grn_Mdl grn_Mdl = new Grn_Mdl();

        copy(grn_Mdl);

        return grn_Mdl;
    }

    public void copy(Grn_Mdl grn_Mdl) {
        grn_Mdl.setSharedKeyFieldCode(this.getSharedKeyFieldCode());
        grn_Mdl.setSharedKeyFieldName(this.getSharedKeyFieldName());
        grn_Mdl.setApprovedBy(this.getApprovedBy());
        grn_Mdl.setApprovedDateTime(this.getApprovedDateTime());
        grn_Mdl.setChallanDate(this.getChallanDate());
        grn_Mdl.setChallanNo(this.getChallanNo());
        grn_Mdl.setGuiMdl(this.getGuiMdl());
        grn_Mdl.setGuiConfigCode(this.getGuiConfigCode());
        grn_Mdl.setModifyReasonMdl(this.getModifyReasonMdl());
        grn_Mdl.setRecIdGRN(this.getRecIdGRN());
        grn_Mdl.setRemark(this.getRemark());
        grn_Mdl.setSupplierGstin(this.getSupplierGstin());
        grn_Mdl.setSupplierGstinDate(this.getSupplierGstinDate());
        grn_Mdl.setSupplierStateCode(this.getSupplierStateCode());
        grn_Mdl.setSupplierTag(this.getSupplierTag());
        grn_Mdl.setSupplierText(this.getSupplierText());
        grn_Mdl.setVoucherDate(this.getVoucherDate());
        grn_Mdl.setBalancingAc(this.getBalancingAc());
        grn_Mdl.setVoucherTypeCode(this.getVoucherTypeCode());
        grn_Mdl.setVoucherTypeName(this.getVoucherTypeName());
        grn_Mdl.setVoucherCategory(this.getVoucherCategory());
        grn_Mdl.setWtaCode(this.getWtaCode());
        grn_Mdl.setPartyType(this.getPartyType());
        grn_Mdl.setPartyTypeForEntry(this.getPartyTypeForEntry());        
        grn_Mdl.setLocationCode(this.getLocationCode());
        grn_Mdl.setPlaceOfSupply(this.getPlaceOfSupply());
        grn_Mdl.setRcmApplicable(this.getRcmApplicable());
        grn_Mdl.setU_EntDt_LatestLine(this.getU_EntDt_LatestLine());
        grn_Mdl.setPoItemChangeCondition(this.getPoItemChangeCondition());
        grn_Mdl.setGrossAmount(this.getGrossAmount());      
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
        grn_Mdl.setIsPurchaseOrderExists(this.getIsPurchaseOrderExists());
        grn_Mdl.setDefaultValueMdl(this.getDefaultValueMdl());
        grn_Mdl.setGateNoTag(this.getGateNoTag());
        grn_Mdl.setGateNoText(this.getGateNoText());
        grn_Mdl.setTransporterCode(this.getTransporterCode());
        grn_Mdl.setTransporterName(this.getTransporterName());
        grn_Mdl.setEwayNo(this.getEwayNo());
        grn_Mdl.setEwayDate(this.getEwayDate());
        grn_Mdl.setLrDate(this.getLrDate());
        grn_Mdl.setLrNo(this.getLrNo());
        grn_Mdl.setInventoryAccountingActive(this.isInventoryAccountingActive());
        grn_Mdl.getGrdGrnDetail().addAll(cloneGrdGrnDetailList(this.getGrdGrnDetail()));
        grn_Mdl.getGrdServiceDetail().addAll(cloneGrdServiceDetailList(this.getGrdServiceDetail()));
    }

    public List<GrdGrnDetail_Mdl> cloneGrdGrnDetailList(List<GrdGrnDetail_Mdl> list) {
        List<GrdGrnDetail_Mdl> grdGrn_MdlList = null;

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

    public String getLastTransactionDate() {
        return lastTransactionDate;
    }

    public void setLastTransactionDate(String lastTransactionDate) {
        this.lastTransactionDate = lastTransactionDate;
    }

}
