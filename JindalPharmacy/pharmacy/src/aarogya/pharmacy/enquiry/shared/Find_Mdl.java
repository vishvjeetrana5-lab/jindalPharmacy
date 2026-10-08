package aarogya.pharmacy.enquiry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String enqiryDate;
    private String remarks;
    private String referenceNo;
    private String approvedBy;
    private String approval;
    private String approvalDateTime;
    private String isStatus;
    private String enquiryTypeCode;
    private String enquiryTypeName;

    public Find_Mdl() {
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

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getIsStatus() {
        return isStatus;
    }

    public void setIsStatus(String isStatus) {
        this.isStatus = isStatus;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getEnqiryDate() {
        return enqiryDate;
    }

    public void setEnqiryDate(String enqiryDate) {
        this.enqiryDate = enqiryDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getApproval() {
        return approval;
    }

    public void setApproval(String approval) {
        this.approval = approval;
    }

    public String getApprovalDateTime() {
        return approvalDateTime;
    }

    public void setApprovalDateTime(String approvalDateTime) {
        this.approvalDateTime = approvalDateTime;
    }

}
