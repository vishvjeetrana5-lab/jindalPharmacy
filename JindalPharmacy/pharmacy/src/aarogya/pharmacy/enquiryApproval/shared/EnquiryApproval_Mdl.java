package aarogya.pharmacy.enquiryApproval.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EnquiryApproval_Mdl extends KeyField implements Serializable, IsSerializable {

    private String IndentNo;
    private String FromDate;
    private String ToDate;
    private String StatusCode;
    private String StatusName;
    private String vendorName;
    private String vendorCode;
    private String enqDate;
    private String refNo;
    private String remark;
    private String siteName;
    private String approvedBy;
    private String isStatus;
    private String approvedDateTime;
    private String wtaCode;
    private String enquiryRecIds;
    private String enquiryDocIds;

    public EnquiryApproval_Mdl() {
    }

    private List<GrdVendorDetaill_Mdl> grdVendorDetail_Mdl = new ArrayList<GrdVendorDetaill_Mdl>();

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorCode() {
        return vendorCode;
    }

    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }

    public List<GrdEnquiryApproval_Mdl> getEnquiryApprovalEntry_Mdl() {
        return enquiryApprovalEntry_Mdl;
    }

    public void setIndentApprovalEntry_Mdl(List<GrdEnquiryApproval_Mdl> enquiryApprovalEntry_Mdl) {
        this.enquiryApprovalEntry_Mdl = enquiryApprovalEntry_Mdl;
    }

    private List<GrdEnquiryApproval_Mdl> enquiryApprovalEntry_Mdl = new ArrayList<GrdEnquiryApproval_Mdl>();

    public String getIndentNo() {
        return IndentNo;
    }

    public void setIndentNo(String IndentNo) {
        this.IndentNo = IndentNo;
    }

    public String getFromDate() {
        return FromDate;
    }

    public void setFromDate(String FromDate) {
        this.FromDate = FromDate;
    }

    public String getToDate() {
        return ToDate;
    }

    public void setToDate(String ToDate) {
        this.ToDate = ToDate;
    }

    public String getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(String StatusCode) {
        this.StatusCode = StatusCode;
    }

    public String getStatusName() {
        return StatusName;
    }

    public void setStatusName(String StatusName) {
        this.StatusName = StatusName;
    }

    public List<GrdVendorDetaill_Mdl> getGrdVendorDetail_Mdl() {
        return grdVendorDetail_Mdl;
    }

    public String getEnqDate() {
        return enqDate;
    }

    public void setEnqDate(String enqDate) {
        this.enqDate = enqDate;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
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

    public String getApprovedDateTime() {
        return approvedDateTime;
    }

    public void setApprovedDateTime(String approvedDateTime) {
        this.approvedDateTime = approvedDateTime;
    }

    public String getWtaCode() {
        return wtaCode;
    }

    public void setWtaCode(String wtaCode) {
        this.wtaCode = wtaCode;
    }

    public String getEnquiryRecIds() {
        return enquiryRecIds;
    }

    public void setEnquiryRecIds(String enquiryRecIds) {
        this.enquiryRecIds = enquiryRecIds;
    }

    public String getEnquiryDocIds() {
        return enquiryDocIds;
    }

    public void setEnquiryDocIds(String enquiryDocIds) {
        this.enquiryDocIds = enquiryDocIds;
    }

}
