package aarogya.pharmacy.enquiry.shared;

import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.RecId;
import dataman.dmToolkit.dmGUI.shared.DmGUIRow_Mdl;
import dataman.dmToolkit.dmGUI.shared.DmGUI_Mdl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Enquiry_Mdl extends KeyField implements Serializable, IsSerializable {

    private List<GrdItemDetail_Mdl> grdItem_Mdl = new ArrayList<GrdItemDetail_Mdl>();
    private List<GrdVendor_Mdl> grdVen_Mdl = new ArrayList<GrdVendor_Mdl>();
    private RecId recIdEnquiry = new RecId();
    private ModifyReason_Mdl modifyReasonMdl;
    private String enquiryDt;
    private String remark;
    private String enquiryTypeCode;
    private String enquiryTypeName;
    private String modifiedBy;
    private String preparedBy;
    private String referenceNo;
    private String approvedBy;
    private String wtaCode;
    private String UserEntDtLatestLine;
    private DmGUI_Mdl guiMdl = new DmGUI_Mdl();
    private String guiConfigCode;
    private String indentV_DateChangeCondition;
    private String indApprovalChangeCondition;
    private String indItemChangeCondition;

    public Enquiry_Mdl() {
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

    public String getIndentV_DateChangeCondition() {
        return indentV_DateChangeCondition;
    }

    public void setIndentV_DateChangeCondition(String indentV_DateChangeCondition) {
        this.indentV_DateChangeCondition = indentV_DateChangeCondition;
    }

    public ModifyReason_Mdl getModifyReasonMdl() {
        return modifyReasonMdl;
    }

    public void setModifyReasonMdl(ModifyReason_Mdl modifyReasonMdl) {
        this.modifyReasonMdl = modifyReasonMdl;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getModifiedBy() {
        return modifiedBy;
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

    public String getEnquiryTypeCode() {
        return enquiryTypeCode;
    }

    public void setEnquiryTypeCode(String enquiryTypeCode) {
        this.enquiryTypeCode = enquiryTypeCode;
    }

    public String getEnquiryTypeName() {
        return enquiryTypeName;
    }

    public void setEnquiryTypeName(String enquiryTypeName) {
        this.enquiryTypeName = enquiryTypeName;
    }

    public RecId getRecIdEnquiry() {
        return recIdEnquiry;
    }

    public void setRecIdEnquiry(RecId recIdEnquiry) {
        this.recIdEnquiry = recIdEnquiry;
    }

    public List<GrdVendor_Mdl> getGrdVen_Mdl() {
        return grdVen_Mdl;
    }

    public void setGrdVen_Mdl(List<GrdVendor_Mdl> grdVen_Mdl) {
        this.grdVen_Mdl = grdVen_Mdl;
    }

    public String getEnquiryDt() {
        return enquiryDt;
    }

    public void setEnquiryDt(String enquiryDt) {
        this.enquiryDt = enquiryDt;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<GrdItemDetail_Mdl> getGrdItem_Mdl() {
        return grdItem_Mdl;
    }

    public void setGrdItem_Mdl(List<GrdItemDetail_Mdl> grdItem_Mdl) {
        this.grdItem_Mdl = grdItem_Mdl;
    }

    public List<GrdItemDetail_Mdl> getgrdItem_Mdl() {
        return grdItem_Mdl;
    }

    public String getWtaCode() {
        return wtaCode;
    }

    public void setWtaCode(String wtaCode) {
        this.wtaCode = wtaCode;
    }

    public String getUserEntDtLatestLine() {
        return UserEntDtLatestLine;
    }

    public void setUserEntDtLatestLine(String UserEntDtLatestLine) {
        this.UserEntDtLatestLine = UserEntDtLatestLine;
    }

    public DmGUI_Mdl getGuiMdl() {
        return guiMdl;
    }

    public void setGuiMdl(DmGUI_Mdl guiMdl) {
        this.guiMdl = guiMdl;
    }

    public String getGuiConfigCode() {
        return guiConfigCode;
    }

    public void setGuiConfigCode(String guiConfigCode) {
        this.guiConfigCode = guiConfigCode;
    }
}
