package aarogya.pharmacy.requisition.shared;

import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.RecId;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Requisition_Mdl extends KeyField implements Serializable, IsSerializable {

    private RecId recid = new RecId();
    private String date;
    private String departmentByCode;
    private String departmentByName;
    private String departmentToCode;
    private String departmentToName;
    private String requisitionTypeCode;
    private String requisitionTypeName;
    private String subCode;
    private String subName;
    private String approvedBy;
    private String modifiedBy;
    private String preparedBy;
    private String userEntDtLatestLine;
    private String wtaCode;
    private String docIdOPD;
    private String remark;
    private String priorityTypeCode;
    private String priorityTypeName;
    private String linkSiteCode;
    private String linkSiteName;
//    private String recIdPrefix;
//    private String vNo;
    private ModifyReason_Mdl modifyReasonMdl;

    private PatientDetail_Mdl patientDetail = new PatientDetail_Mdl();
    private List<GrdMedicineDetail_Mdl> grdData = new ArrayList<GrdMedicineDetail_Mdl>();

    public Requisition_Mdl() {
    }

    public List<GrdMedicineDetail_Mdl> getGrdData() {
        return grdData;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDepartmentByCode() {
        return departmentByCode;
    }

    public void setDepartmentByCode(String departmentByCode) {
        this.departmentByCode = departmentByCode;
    }

    public String getDepartmentByName() {
        return departmentByName;
    }

    public void setDepartmentByName(String departmentByName) {
        this.departmentByName = departmentByName;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getPreparedBy() {
        return preparedBy;
    }

    public void setPreparedBy(String preparedBy) {
        this.preparedBy = preparedBy;
    }

    public RecId getRecid() {
        return recid;
    }

    public void setRecid(RecId recid) {
        this.recid = recid;
    }

    public String getUserEntDtLatestLine() {
        return userEntDtLatestLine;
    }

    public void setUserEntDtLatestLine(String userEntDtLatestLine) {
        this.userEntDtLatestLine = userEntDtLatestLine;
    }

    public String getWtaCode() {
        return wtaCode;
    }

    public void setWtaCode(String wtaCode) {
        this.wtaCode = wtaCode;
    }

    public String getDepartmentToCode() {
        return departmentToCode;
    }

    public void setDepartmentToCode(String departmentToCode) {
        this.departmentToCode = departmentToCode;
    }

    public String getDepartmentToName() {
        return departmentToName;
    }

    public void setDepartmentToName(String departmentToName) {
        this.departmentToName = departmentToName;
    }

    public String getDocIdOPD() {
        return docIdOPD;
    }

    public void setDocIdOPD(String docIdOPD) {
        this.docIdOPD = docIdOPD;
    }

    public PatientDetail_Mdl getPatientDetail() {
        return patientDetail;
    }

    public void setPatientDetail(PatientDetail_Mdl patientDetail) {
        this.patientDetail = patientDetail;
    }

    public String getPriorityTypeCode() {
        return priorityTypeCode;
    }

    public void setPriorityTypeCode(String priorityTypeCode) {
        this.priorityTypeCode = priorityTypeCode;
    }

    public String getPriorityTypeName() {
        return priorityTypeName;
    }

    public void setPriorityTypeName(String priorityTypeName) {
        this.priorityTypeName = priorityTypeName;
    }

    public String getRequisitionTypeCode() {
        return requisitionTypeCode;
    }

    public void setRequisitionTypeCode(String requisitionTypeCode) {
        this.requisitionTypeCode = requisitionTypeCode;
    }

    public String getRequisitionTypeName() {
        return requisitionTypeName;
    }

    public void setRequisitionTypeName(String requisitionTypeName) {
        this.requisitionTypeName = requisitionTypeName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public ModifyReason_Mdl getModifyReasonMdl() {
        return modifyReasonMdl;
    }

    public void setModifyReasonMdl(ModifyReason_Mdl modifyReasonMdl) {
        this.modifyReasonMdl = modifyReasonMdl;
    }

    public String getLinkSiteCode() {
        return linkSiteCode;
    }

    public void setLinkSiteCode(String linkSiteCode) {
        this.linkSiteCode = linkSiteCode;
    }

    public String getLinkSiteName() {
        return linkSiteName;
    }

    public void setLinkSiteName(String linkSiteName) {
        this.linkSiteName = linkSiteName;
    }

//    public String getRecIdPrefix() {
//        return recIdPrefix;
//    }
//
//    public void setRecIdPrefix(String recIdPrefix) {
//        this.recIdPrefix = recIdPrefix;
//    }
//    public String getvNo() {
//        return vNo;
//    }
//
//    public void setvNo(String vNo) {
//        this.vNo = vNo;
//    }
}
