package aarogya.inventory.itemTransferApproval.shared;

import aarogya.inventory.itemTransferApproval.shared.*;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ItemTransferApproval_Mdl extends KeyField implements Serializable, IsSerializable {

    private String FromDate;
    private String ToDate;
    private String ItemName;
    private String ItemCode;
    private String DepartmentName;
    private String DepartmentCode;
    private String toDepartmentName;
    private String toDepartmentCode;
    private String StatusCode;
    private String StatusName;
    private String TransferToName;
    private String TransferCode;
    private String trfDocId;
    private String trfReccId;

    private String transferDocIds;
    private String transferRecIds;
    private String vSNo;
    private String trfDate;
    private String trfType;
    private String siteCode;
    private String siteName;
    private String approvedBy;
    private String isStatus;
    private String approvedDateTime;
    private String wtaCode;
    private List<GrdItemDetail_Mdl> grdItemDetail_Mdl = new ArrayList<GrdItemDetail_Mdl>();
    private List<GrdItemTransferApproval_Mdl> itemTransferApprovalEntry_Mdl = new ArrayList<GrdItemTransferApproval_Mdl>();

    public ItemTransferApproval_Mdl() {
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

    public List<GrdItemTransferApproval_Mdl> getItemTransferApprovalEntry_Mdl() {
        return itemTransferApprovalEntry_Mdl;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String ItemCode) {
        this.ItemCode = ItemCode;
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

    public String getTransferToName() {
        return TransferToName;
    }

    public void setTransferToName(String TransferToName) {
        this.TransferToName = TransferToName;
    }

    public String getTransferCode() {
        return TransferCode;
    }

    public void setTransferCode(String TransferCode) {
        this.TransferCode = TransferCode;
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

    public String getTrfDocId() {
        return trfDocId;
    }

    public void setTrfDocId(String trfDocId) {
        this.trfDocId = trfDocId;
    }

    public String getTrfReccId() {
        return trfReccId;
    }

    public void setTrfReccId(String trfReccId) {
        this.trfReccId = trfReccId;
    }

    public String getvSNo() {
        return vSNo;
    }

    public void setvSNo(String vSNo) {
        this.vSNo = vSNo;
    }

    public String getTrfDate() {
        return trfDate;
    }

    public void setTrfDate(String trfDate) {
        this.trfDate = trfDate;
    }

    public String getTrfType() {
        return trfType;
    }

    public void setTrfType(String trfType) {
        this.trfType = trfType;
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

    public String getWtaCode() {
        return wtaCode;
    }

    public void setWtaCode(String wtaCode) {
        this.wtaCode = wtaCode;
    }

    public List<GrdItemDetail_Mdl> getGrdItemDetail_Mdl() {
        return grdItemDetail_Mdl;
    }

    public String getTransferDocIds() {
        return transferDocIds;
    }

    public void setTransferDocIds(String transferDocIds) {
        this.transferDocIds = transferDocIds;
    }

    public String getTransferRecIds() {
        return transferRecIds;
    }

    public void setTransferRecIds(String transferRecIds) {
        this.transferRecIds = transferRecIds;
    }

    public String getToDepartmentName() {
        return toDepartmentName;
    }

    public void setToDepartmentName(String toDepartmentName) {
        this.toDepartmentName = toDepartmentName;
    }

    public String getToDepartmentCode() {
        return toDepartmentCode;
    }

    public void setToDepartmentCode(String toDepartmentCode) {
        this.toDepartmentCode = toDepartmentCode;
    }

}
