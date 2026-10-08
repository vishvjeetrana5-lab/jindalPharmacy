package aarogya.inventory.purchaseBillApproval.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PurchaseBillApproval_Mdl extends KeyField implements Serializable, IsSerializable {

    private String FromDate;
    private String ToDate;
    private String ItemName;
    private String ItemCode;
    private String ItemGroupName;
    private String ItemGroupCode;
    private String DepartmentName;
    private String DepartmentCode;
    private String StatusCode;
    private String StatusName;
    private String IndenterName;
    private String IndenterCode;
    private String IndDocId;
    private String IndReccId;

    private String IndentDocIds;
    private String IndentReccIds;
    private String vSNo;
    private String IndDate;
    private String IndType;
    private String siteCode;
    private String siteName;
    private String approvedBy;
    private String isStatus;
    private String approvedDateTime;
    private String wtaCode;

    private List<GrdItemDetail_Mdl> grdItemDetail_Mdl = new ArrayList<GrdItemDetail_Mdl>();
    private List<GrdPurchaseBillApproval_Mdl> grdPurchaseBillApproval_Mdl = new ArrayList<GrdPurchaseBillApproval_Mdl>();

    public PurchaseBillApproval_Mdl() {
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

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String ItemName) {
        this.ItemName = ItemName;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String ItemCode) {
        this.ItemCode = ItemCode;
    }

    public String getItemGroupName() {
        return ItemGroupName;
    }

    public void setItemGroupName(String ItemGroupName) {
        this.ItemGroupName = ItemGroupName;
    }

    public String getItemGroupCode() {
        return ItemGroupCode;
    }

    public void setItemGroupCode(String ItemGroupCode) {
        this.ItemGroupCode = ItemGroupCode;
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

    public String getIndenterName() {
        return IndenterName;
    }

    public void setIndenterName(String IndenterName) {
        this.IndenterName = IndenterName;
    }

    public String getIndenterCode() {
        return IndenterCode;
    }

    public void setIndenterCode(String IndenterCode) {
        this.IndenterCode = IndenterCode;
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

    public String getIndDocId() {
        return IndDocId;
    }

    public void setIndDocId(String IndDocId) {
        this.IndDocId = IndDocId;
    }

    public String getIndReccId() {
        return IndReccId;
    }

    public void setIndReccId(String IndReccId) {
        this.IndReccId = IndReccId;
    }

    public String getIndDate() {
        return IndDate;
    }

    public void setIndDate(String IndDate) {
        this.IndDate = IndDate;
    }

    public String getIndType() {
        return IndType;
    }

    public void setIndType(String IndType) {
        this.IndType = IndType;
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

    public List<GrdPurchaseBillApproval_Mdl> getGrdIndentApproval_Mdl() {
        return grdPurchaseBillApproval_Mdl;
    }

    public String getWtaCode() {
        return wtaCode;
    }

    public void setWtaCode(String wtaCode) {
        this.wtaCode = wtaCode;
    }

    public String getIndentDocIds() {
        return IndentDocIds;
    }

    public void setIndentDocIds(String IndentDocIds) {
        this.IndentDocIds = IndentDocIds;
    }

    public String getIndentReccIds() {
        return IndentReccIds;
    }

    public void setIndentReccIds(String IndentReccIds) {
        this.IndentReccIds = IndentReccIds;
    }

}
