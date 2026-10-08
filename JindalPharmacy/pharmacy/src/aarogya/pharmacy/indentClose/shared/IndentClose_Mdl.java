package aarogya.pharmacy.indentClose.shared;

import aarogya.pharmacy.indentApproval.shared.*;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class IndentClose_Mdl extends KeyField implements Serializable, IsSerializable {

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
    private String IndDocId;
    private String IndReccId;
    private String beforeDay;

    private String indentDocIds;
    private String indentRecIds;
    private String vSNo;
    private String IndDate;
    private String IndType;
    private String siteCode;
    private String siteName;
    private String approvedBy;
    private String isStatus;
    private String approvedDateTime;
    private String wtaCode;
    private List<GrdMedicineDetail_Mdl> grdItemDetail_Mdl = new ArrayList<GrdMedicineDetail_Mdl>();
    private List<GrdIndentClose_Mdl> indentApprovalEntry_Mdl = new ArrayList<GrdIndentClose_Mdl>();

    public IndentClose_Mdl() {
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

    public List<GrdIndentClose_Mdl> getIndentApprovalEntry_Mdl() {
        return indentApprovalEntry_Mdl;
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

    public String getvSNo() {
        return vSNo;
    }

    public void setvSNo(String vSNo) {
        this.vSNo = vSNo;
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

    public List<GrdMedicineDetail_Mdl> getGrdItemDetail_Mdl() {
        return grdItemDetail_Mdl;
    }

    public String getIndentDocIds() {
        return indentDocIds;
    }

    public void setIndentDocIds(String indentDocIds) {
        this.indentDocIds = indentDocIds;
    }

    public String getIndentRecIds() {
        return indentRecIds;
    }

    public void setIndentRecIds(String indentRecIds) {
        this.indentRecIds = indentRecIds;
    }

    public String getBeforeDay() {
        return beforeDay;
    }

    public void setBeforeDay(String beforeDay) {
        this.beforeDay = beforeDay;
    }

}
