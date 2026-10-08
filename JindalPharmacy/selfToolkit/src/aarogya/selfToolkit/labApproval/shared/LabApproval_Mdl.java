package aarogya.selfToolkit.labApproval.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LabApproval_Mdl extends KeyField implements Serializable, IsSerializable {

    private String FromDate;
    private String ToDate;
    private String vSNo;
    private String siteCode;
    private String siteName;
    private String approvedBy;
    private String isStatus;
    private String approvedDateTime;
    private String StatusCode;
    private String StatusName;
    private String labCode;
    private String labRecId;
    private String technicianCode;
    private String technicianName;
    private String testTemplateCode;
    private String testTemplateName;
    private String sampleCode;
    private String sampleRecId;
    private String itemCode;
    private String itemName;

    public LabApproval_Mdl() {
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

    public String getvSNo() {
        return vSNo;
    }

    public void setvSNo(String vSNo) {
        this.vSNo = vSNo;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
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

    public String getTechnicianCode() {
        return technicianCode;
    }

    public void setTechnicianCode(String technicianCode) {
        this.technicianCode = technicianCode;
    }

    public String getTechnicianName() {
        return technicianName;
    }

    public void setTechnicianName(String technicianName) {
        this.technicianName = technicianName;
    }

    public String getTestTemplateCode() {
        return testTemplateCode;
    }

    public void setTestTemplateCode(String testTemplateCode) {
        this.testTemplateCode = testTemplateCode;
    }

    public String getTestTemplateName() {
        return testTemplateName;
    }

    public void setTestTemplateName(String testTemplateName) {
        this.testTemplateName = testTemplateName;
    }

    public String getLabCode() {
        return labCode;
    }

    public void setLabCode(String labCode) {
        this.labCode = labCode;
    }

    public String getLabRecId() {
        return labRecId;
    }

    public void setLabRecId(String labRecId) {
        this.labRecId = labRecId;
    }

    public String getSampleCode() {
        return sampleCode;
    }

    public void setSampleCode(String sampleCode) {
        this.sampleCode = sampleCode;
    }

    public String getSampleRecId() {
        return sampleRecId;
    }

    public void setSampleRecId(String sampleRecId) {
        this.sampleRecId = sampleRecId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

}
