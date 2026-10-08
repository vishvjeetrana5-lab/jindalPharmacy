package aarogya.inventory.quotation.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmToolkit.overHead.shared.OverHead_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGUI.shared.DmGUI_Mdl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import aarogya.selfToolkit.serviceGrid.shared.ServiceDetail_Mdl;

public class Quotation_Mdl extends KeyField implements Serializable, IsSerializable {

    private RecId objRecId = new RecId();
    private String voucherTypeCode;
    private String voucherTypeName;
    private String voucherCategory;
    private String quotationDate;
    private String vendorTag;
    private String vendorText;
    private String vendorRefNo;
    private String vendorRefDate;
    private String enquiryTag;
    private String enquiryText;
    private String enquiryDate;
    private String enquiryRefDate;
    private String enquiryRefNo;

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

    private String indentV_DateChangeCondition;
    private String enqItemChangeCondition;
    private String indApprovalChangeCondition;
    private String indItemChangeCondition;
    private String validUptoDate;
    private String tempKeyCode;

    private List<GrdItemDetail_Mdl> listItemDetail = new ArrayList<GrdItemDetail_Mdl>();
    private List<ServiceDetail_Mdl> grdServiceDetail = new ArrayList<ServiceDetail_Mdl>();
    private ModifyReason_Mdl modifyReasonMdl;
    private String guiConfigCode;
    private DmGUI_Mdl guiMdl = new DmGUI_Mdl();

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

    public Quotation_Mdl() {
    }

    public String getTempKeyCode() {
        return tempKeyCode;
    }

    public void setTempKeyCode(String tempKeyCode) {
        this.tempKeyCode = tempKeyCode;
    }

    public String getIndItemChangeCondition() {
        return indItemChangeCondition;
    }

    public void setIndItemChangeCondition(String indItemChangeCondition) {
        this.indItemChangeCondition = indItemChangeCondition;
    }

    public String getIndApprovalChangeCondition() {
        return indApprovalChangeCondition;
    }

    public void setIndApprovalChangeCondition(String indApprovalChangeCondition) {
        this.indApprovalChangeCondition = indApprovalChangeCondition;
    }

    public String getEnqItemChangeCondition() {
        return enqItemChangeCondition;
    }

    public void setEnqItemChangeCondition(String enqItemChangeCondition) {
        this.enqItemChangeCondition = enqItemChangeCondition;
    }

    public String getIndentV_DateChangeCondition() {
        return indentV_DateChangeCondition;
    }

    public void setIndentV_DateChangeCondition(String indentV_DateChangeCondition) {
        this.indentV_DateChangeCondition = indentV_DateChangeCondition;
    }

    public String getEnquiryDate() {
        return enquiryDate;
    }

    public void setEnquiryDate(String enquiryDate) {
        this.enquiryDate = enquiryDate;
    }

    public ModifyReason_Mdl getModifyReasonMdl() {
        return modifyReasonMdl;
    }

    public void setModifyReasonMdl(ModifyReason_Mdl modifyReasonMdl) {
        this.modifyReasonMdl = modifyReasonMdl;
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

    
    public String getQuotationDate() {
        return quotationDate;
    }

    public void setQuotationDate(String quotationDate) {
        this.quotationDate = quotationDate;
    }

    public String getVendorTag() {
        return vendorTag;
    }

    public void setVendorTag(String vendorTag) {
        this.vendorTag = vendorTag;
    }

    public String getVendorText() {
        return vendorText;
    }

    public void setVendorText(String vendorText) {
        this.vendorText = vendorText;
    }

    public String getVendorRefNo() {
        return vendorRefNo;
    }

    public void setVendorRefNo(String vendorRefNo) {
        this.vendorRefNo = vendorRefNo;
    }

    public String getVendorRefDate() {
        return vendorRefDate;
    }

    public void setVendorRefDate(String vendorRefDate) {
        this.vendorRefDate = vendorRefDate;
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

    public String getEnquiryRefDate() {
        return enquiryRefDate;
    }

    public void setEnquiryRefDate(String enquiryRefDate) {
        this.enquiryRefDate = enquiryRefDate;
    }

    public String getEnquiryRefNo() {
        return enquiryRefNo;
    }

    public void setEnquiryRefNo(String enquiryRefNo) {
        this.enquiryRefNo = enquiryRefNo;
    }

    public String getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(String grossAmount) {
        this.grossAmount = grossAmount;
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

    public String getValidUptoDate() {
        return validUptoDate;
    }

    public void setValidUptoDate(String validUptoDate) {
        this.validUptoDate = validUptoDate;
    }

}
