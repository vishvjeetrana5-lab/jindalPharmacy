package aarogya.pharmacy.purchaseOrderClose.shared;


import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PurchaseOrderClose_Mdl extends KeyField implements Serializable, IsSerializable {

    private String FromDate;
    private String ToDate;
    private String StatusCode;
    private String StatusName;
    private String suppleirName;
    private String suppleirCode;
    private String itemName;
    private String itemCode;
    private String PurchaseDocIds;
    private String PurchaseReccIds;
    private String vSNo;
    private String PurchaseDate;
    private String PurchaseType;
    private String siteCode;
    private String siteName;
    private String departmentCode;
    private String departmentName;
    private String approvedBy;
    private String isStatus;
    private String approvedDateTime;
    private String quotationNo;
    private String currency;
    private String wtaCode;
    private String beforeDays;

    private List<GrdItemDetail_Mdl> grdItemDetail_Mdl = new ArrayList<GrdItemDetail_Mdl>();
    private List<GrdPurchaseOrderClose_Mdl> grdPurchaseApprovalEntry_Lst = new ArrayList<GrdPurchaseOrderClose_Mdl>();

    public PurchaseOrderClose_Mdl() {
    }

    public String getSuppleirName() {
        return suppleirName;
    }

    public void setSuppleirName(String suppleirName) {
        this.suppleirName = suppleirName;
    }

    public String getSuppleirCode() {
        return suppleirCode;
    }

    public void setSuppleirCode(String suppleirCode) {
        this.suppleirCode = suppleirCode;
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

    public List<GrdPurchaseOrderClose_Mdl> getGrdPurchaseApprovalEntry_Lst() {
        return grdPurchaseApprovalEntry_Lst;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getvSNo() {
        return vSNo;
    }

    public void setvSNo(String vSNo) {
        this.vSNo = vSNo;
    }

    public String getPurchaseDate() {
        return PurchaseDate;
    }

    public void setPurchaseDate(String PurchaseDate) {
        this.PurchaseDate = PurchaseDate;
    }

    public String getPurchaseType() {
        return PurchaseType;
    }

    public void setPurchaseType(String PurchaseType) {
        this.PurchaseType = PurchaseType;
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

    public List<GrdItemDetail_Mdl> getGrdItemDetail_Mdl() {
        return grdItemDetail_Mdl;
    }

    public String getQuotationNo() {
        return quotationNo;
    }

    public void setQuotationNo(String quotationNo) {
        this.quotationNo = quotationNo;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getWtaCode() {
        return wtaCode;
    }

    public void setWtaCode(String wtaCode) {
        this.wtaCode = wtaCode;
    }

    public String getPurchaseDocIds() {
        return PurchaseDocIds;
    }

    public void setPurchaseDocIds(String PurchaseDocIds) {
        this.PurchaseDocIds = PurchaseDocIds;
    }

    public String getPurchaseReccIds() {
        return PurchaseReccIds;
    }

    public void setPurchaseReccIds(String PurchaseReccIds) {
        this.PurchaseReccIds = PurchaseReccIds;
    }

    public String getBeforeDays() {
        return beforeDays;
    }

    public void setBeforeDays(String beforeDays) {
        this.beforeDays = beforeDays;
    }

}
