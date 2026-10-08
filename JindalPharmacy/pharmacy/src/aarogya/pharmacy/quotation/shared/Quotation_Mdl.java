package aarogya.pharmacy.quotation.shared;

import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmToolkit.overHead.shared.OverHead_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGUI.shared.DmGUI_Mdl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Quotation_Mdl extends KeyField implements Serializable, IsSerializable {

    private RecId objRecId = new RecId();
    private String voucherTypeCode;
    private String voucherTypeName;
    private String quotationDate;
    private String vendorTag;
    private String vendorText;
    private String vendorRefNo;
    private String vendorRefDate;
    private String enquiryTag;
    private String enquiryText;
    private String enquiryRefDate;
    private String enquiryRefNo;
    private String grossAmount;
    private List<GrdItemDetail_Mdl> listItemDetail = new ArrayList<GrdItemDetail_Mdl>();
    private List<OverHead_Mdl> overHeadList = new ArrayList<OverHead_Mdl>();
    private ModifyReason_Mdl modifyReasonMdl;
    private String guiConfigCode;
    private DmGUI_Mdl guiMdl = new DmGUI_Mdl();
    private String indentV_DateChangeCondition;
    private String enqItemChangeCondition;
    private String indApprovalChangeCondition;
    private String indItemChangeCondition;
    private String locationCode;

    public Quotation_Mdl() {
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

    public RecId getObjRecId() {
        return objRecId;
    }

    public void setObjRecId(RecId objRecId) {
        this.objRecId = objRecId;
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

    public List<OverHead_Mdl> getOverHeadList() {
        return overHeadList;
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

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

}
