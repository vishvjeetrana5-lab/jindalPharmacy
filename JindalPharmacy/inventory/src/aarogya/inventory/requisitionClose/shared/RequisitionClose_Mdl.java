package aarogya.inventory.requisitionClose.shared;

import aarogya.inventory.requisitionClose.shared.*;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RequisitionClose_Mdl extends KeyField implements Serializable, IsSerializable {

    private String FromDate;
    private String ToDate;
    private String ItemName;
    private String ItemCode;
    private String DepartmentName;
    private String DepartmentCode;
    private String StatusCode;
    private String StatusName;
    private String IndenterName;
    private String IndenterCode;
    private String requisitionDocIds;
    private String requisitionRecIds;

    private String requitDocId;
    private String requitRecId;
    private String vSNo;
    private String requitDate;
    private String requitType;
    private String siteCode;
    private String siteName;
    private String approvedBy;
    private String isStatus;
    private String approvedDateTime;
    private String requestedByCode;
    private String requestedByName;
    private String wtaCode;
    private String beforeDays;
    private List<GrdMedicineDetail_Mdl> grdItemDetail_Mdl = new ArrayList<GrdMedicineDetail_Mdl>();
    private List<GrdRequisitionClose_Mdl> requisitionApprovalEntry_Mdl = new ArrayList<GrdRequisitionClose_Mdl>();

    public RequisitionClose_Mdl() {
    }

    public String getRequisitionDocIds() {
        return requisitionDocIds;
    }

    public void setRequisitionDocIds(String requisitionDocIds) {
        this.requisitionDocIds = requisitionDocIds;
    }

    public String getRequisitionRecIds() {
        return requisitionRecIds;
    }

    public void setRequisitionRecIds(String requisitionRecIds) {
        this.requisitionRecIds = requisitionRecIds;
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

    public List<GrdRequisitionClose_Mdl> getRequisitionApprovalEntry_Mdl() {
        return requisitionApprovalEntry_Mdl;
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

    public String getRequitDocId() {
        return requitDocId;
    }

    public void setRequitDocId(String requitDocId) {
        this.requitDocId = requitDocId;
    }

    public String getRequitRecId() {
        return requitRecId;
    }

    public void setRequitRecId(String requitRecId) {
        this.requitRecId = requitRecId;
    }

    public String getvSNo() {
        return vSNo;
    }

    public void setvSNo(String vSNo) {
        this.vSNo = vSNo;
    }

    public String getRequitDate() {
        return requitDate;
    }

    public void setRequitDate(String requitDate) {
        this.requitDate = requitDate;
    }

    public String getRequitType() {
        return requitType;
    }

    public void setRequitType(String requitType) {
        this.requitType = requitType;
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

    public String getRequestedByCode() {
        return requestedByCode;
    }

    public void setRequestedByCode(String requestedByCode) {
        this.requestedByCode = requestedByCode;
    }

    public String getRequestedByName() {
        return requestedByName;
    }

    public void setRequestedByName(String requestedByName) {
        this.requestedByName = requestedByName;
    }

    public String getWtaCode() {
        return wtaCode;
    }

    public void setWtaCode(String wtaCode) {
        this.wtaCode = wtaCode;
    }

    public List<GrdMedicineDetail_Mdl> getGrdItemDetail_Mdl() {
        return grdItemDetail_Mdl;
    }

    public String getBeforeDays() {
        return beforeDays;
    }

    public void setBeforeDays(String beforeDays) {
        this.beforeDays = beforeDays;
    }

}
