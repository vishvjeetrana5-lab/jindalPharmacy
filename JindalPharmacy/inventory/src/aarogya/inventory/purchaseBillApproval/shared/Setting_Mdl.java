package aarogya.inventory.purchaseBillApproval.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Setting_Mdl extends KeyField implements Serializable, IsSerializable {

    private String FromDate;
    private String ToDate;
    private String ItemName;
    private String ItemCode;
    private String grnNoTag;
    private String grnNoText;
    private String supplierName;
    private String supplierCode;
    private String StatusCode;
    private String StatusName;
    private String IndDocId;
    private String IndReccId;
    private String categoryCode;
    private String categoryName;
    private String billDocIds;
    private String billReccIds;
    private String vSNo;
    private String IndDate;
    private String IndType;
    private String siteCode;
    private String siteName;
    private String approvedBy;
    private String isStatus;
    private String approvedDateTime;
    private String wtaCode;

    private List<GrdPurchaseBillApproval_Mdl> grdPurchaseBillApproval_Mdl = new ArrayList<GrdPurchaseBillApproval_Mdl>();

    public Setting_Mdl() {
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

    public String getGrnNoTag() {
        return grnNoTag;
    }

    public void setGrnNoTag(String grnNoTag) {
        this.grnNoTag = grnNoTag;
    }

    public String getGrnNoText() {
        return grnNoText;
    }

    public void setGrnNoText(String grnNoText) {
        this.grnNoText = grnNoText;
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

    public String getBillDocIds() {
        return billDocIds;
    }

    public void setBillDocIds(String billDocIds) {
        this.billDocIds = billDocIds;
    }

    public String getBillReccIds() {
        return billReccIds;
    }

    public void setBillReccIds(String billReccIds) {
        this.billReccIds = billReccIds;
    }

    public List<GrdPurchaseBillApproval_Mdl> getGrdPurchaseBillApproval_Mdl() {
        return grdPurchaseBillApproval_Mdl;
    }

    public void setGrdPurchaseBillApproval_Mdl(List<GrdPurchaseBillApproval_Mdl> grdPurchaseBillApproval_Mdl) {
        this.grdPurchaseBillApproval_Mdl = grdPurchaseBillApproval_Mdl;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

}
