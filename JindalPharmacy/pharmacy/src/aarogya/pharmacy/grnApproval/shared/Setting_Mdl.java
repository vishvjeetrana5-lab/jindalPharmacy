package aarogya.pharmacy.grnApproval.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Setting_Mdl extends KeyField implements Serializable, IsSerializable {

    private String FromDate;
    private String ToDate;
    private String DepartmentName;
    private String DepartmentCode;
    private String StatusCode;
    private String StatusName;
    private String siteCode;
    private String siteName;
    private String approvedBy;
    private String isStatus;
    private String approvedDateTime;
    private String wtaCode;
    private String SupplierName;
    private String SupplierCode;
    private String OrderNo;
    private String OrderCode;
    private List<GrdItemDetail_Mdl> grdItemDetail_Mdl = new ArrayList<GrdItemDetail_Mdl>();
    private List<GrdGrnApproval_Mdl> grdIndentApproval_Mdl = new ArrayList<GrdGrnApproval_Mdl>();

    public Setting_Mdl() {
    }

    public String getSupplierName() {
        return SupplierName;
    }

    public void setSupplierName(String SupplierName) {
        this.SupplierName = SupplierName;
    }

    public String getSupplierCode() {
        return SupplierCode;
    }

    public void setSupplierCode(String SupplierCode) {
        this.SupplierCode = SupplierCode;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String OrderNo) {
        this.OrderNo = OrderNo;
    }

    public String getOrderCode() {
        return OrderCode;
    }

    public void setOrderCode(String OrderCode) {
        this.OrderCode = OrderCode;
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

    public String getDepartmentName() {
        return DepartmentName;
    }

    public void setDepartmentName(String DepartmentName) {
        this.DepartmentName = DepartmentName;
    }

    public String getDepartmentCode() {
        return DepartmentCode;
    }

    public void setDepartmentCode(String DepartmentCode) {
        this.DepartmentCode = DepartmentCode;
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

    public List<GrdItemDetail_Mdl> getGrdItemDetail_Mdl() {
        return grdItemDetail_Mdl;
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

    public String getIsStatus() {
        return isStatus;
    }

    public void setIsStatus(String isStatus) {
        this.isStatus = isStatus;
    }

    public List<GrdGrnApproval_Mdl> getGrdIndentApproval_Mdl() {
        return grdIndentApproval_Mdl;
    }

    public String getWtaCode() {
        return wtaCode;
    }

    public void setWtaCode(String wtaCode) {
        this.wtaCode = wtaCode;
    }

}
