package aarogya.inventory.purchaseReturnApproval.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Setting_Mdl extends KeyField implements Serializable, IsSerializable {

    private String fromDate;
    private String toDate;

    private String departmentName;
    private String departmentCode;
    private String supplierName;
    private String supplierCode;
    private String StatusCode;
    private String StatusName;
    private String grnDocId;
    private String grnReccId;
    private String billnoDocId;
    private String billnoRecId;
    private String vSNo;
    private String grnDate;

    private String siteCode;
    private String siteName;
    private String approvedBy;
    private String isStatus;
    private String approvedDateTime;
    private String wtaCode;

    private List<GrdPurchaseReturnApproval_Mdl> grdPurchaseBillApproval_Mdl = new ArrayList<GrdPurchaseReturnApproval_Mdl>();

    public Setting_Mdl() {
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

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getGrnDocId() {
        return grnDocId;
    }

    public void setGrnDocId(String grnDocId) {
        this.grnDocId = grnDocId;
    }

    public String getGrnReccId() {
        return grnReccId;
    }

    public void setGrnReccId(String grnReccId) {
        this.grnReccId = grnReccId;
    }

    public String getBillnoDocId() {
        return billnoDocId;
    }

    public void setBillnoDocId(String billnoDocId) {
        this.billnoDocId = billnoDocId;
    }

    public String getBillnoRecId() {
        return billnoRecId;
    }

    public void setBillnoRecId(String billnoRecId) {
        this.billnoRecId = billnoRecId;
    }

    public String getGrnDate() {
        return grnDate;
    }

    public void setGrnDate(String grnDate) {
        this.grnDate = grnDate;
    }

    public List<GrdPurchaseReturnApproval_Mdl> getGrdPurchaseBillApproval_Mdl() {
        return grdPurchaseBillApproval_Mdl;
    }

    public void setGrdPurchaseBillApproval_Mdl(List<GrdPurchaseReturnApproval_Mdl> grdPurchaseBillApproval_Mdl) {
        this.grdPurchaseBillApproval_Mdl = grdPurchaseBillApproval_Mdl;
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

    public String getApprovedDateTime() {
        return approvedDateTime;
    }

    public void setApprovedDateTime(String approvedDateTime) {
        this.approvedDateTime = approvedDateTime;
    }

    public String getvSNo() {
        return vSNo;
    }

    public void setvSNo(String vSNo) {
        this.vSNo = vSNo;
    }

    public String getIsStatus() {
        return isStatus;
    }

    public void setIsStatus(String isStatus) {
        this.isStatus = isStatus;
    }

    public List<GrdPurchaseReturnApproval_Mdl> getGrdIndentApproval_Mdl() {
        return grdPurchaseBillApproval_Mdl;
    }

    public String getWtaCode() {
        return wtaCode;
    }

    public void setWtaCode(String wtaCode) {
        this.wtaCode = wtaCode;
    }

}
