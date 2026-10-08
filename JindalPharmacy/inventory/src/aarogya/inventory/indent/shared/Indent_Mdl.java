package aarogya.inventory.indent.shared;

import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Indent_Mdl extends KeyField implements Serializable, IsSerializable {

    private String dtIndent;
    private String indentTypeTag;
    private String indentTypeText;
    private String DepartmentTag;
    private String DepartmentText;
    private String toDepartmentTag;
    private String toDepartmentText;
    private String IndentorTag;
    private String IndentorText;
    private String RequisitionTag;
    private String RequisitionText;
    private String UserEntDtLatestLine;
    private String wtaCode;
    private String remark;
    private String isConsentForReqQtyChng;
    private RecId recid = new RecId();

    private String lineItemCodesCsv;
    private String reqItemChangeCondition;
    private String reqQtyChangeCondition;
    
    private String procurementPlanDocID;
    private String procurementPlanRecID;

    private String approvedBy;
    private String vNo;

    private List<GrdItemDetail_Mdl> grdItemDetails = new ArrayList<GrdItemDetail_Mdl>();
    private ModifyReason_Mdl modifyReasonMdl;

    public String getIsConsentForReqQtyChng() {
        return isConsentForReqQtyChng;
    }

    public void setIsConsentForReqQtyChng(String isConsentForReqQtyChng) {
        this.isConsentForReqQtyChng = isConsentForReqQtyChng;
    }

    public String getLineItemCodesCsv() {
        return lineItemCodesCsv;
    }

    public void setLineItemCodesCsv(String lineItemCodesCsv) {
        this.lineItemCodesCsv = lineItemCodesCsv;
    }

    public String getReqItemChangeCondition() {
        return reqItemChangeCondition;
    }

    public void setReqItemChangeCondition(String reqItemChangeCondition) {
        this.reqItemChangeCondition = reqItemChangeCondition;
    }

    public String getReqQtyChangeCondition() {
        return reqQtyChangeCondition;
    }

    public void setReqQtyChangeCondition(String reqQtyChangeCondition) {
        this.reqQtyChangeCondition = reqQtyChangeCondition;
    }

    public String getvNo() {
        return vNo;
    }

    public void setvNo(String vNo) {
        this.vNo = vNo;
    }

    public ModifyReason_Mdl getModifyReasonMdl() {
        return modifyReasonMdl;
    }

    public void setModifyReasonMdl(ModifyReason_Mdl modifyReasonMdl) {
        this.modifyReasonMdl = modifyReasonMdl;
    }

    public Indent_Mdl() {
    }

    public List<GrdItemDetail_Mdl> getGrdItemDetails() {
        return grdItemDetails;
    }

    public String getDtIndent() {
        return dtIndent;
    }

    public void setDtIndent(String dtIndent) {
        this.dtIndent = dtIndent;
    }

    public String getIndentTypeTag() {
        return indentTypeTag;
    }

    public void setIndentTypeTag(String indentTypeTag) {
        this.indentTypeTag = indentTypeTag;
    }

    public String getIndentTypeText() {
        return indentTypeText;
    }

    public void setIndentTypeText(String indentTypeText) {
        this.indentTypeText = indentTypeText;
    }

    public String getDepartmentTag() {
        return DepartmentTag;
    }

    public void setDepartmentTag(String DepartmentTag) {
        this.DepartmentTag = DepartmentTag;
    }

    public String getDepartmentText() {
        return DepartmentText;
    }

    public void setDepartmentText(String DepartmentText) {
        this.DepartmentText = DepartmentText;
    }

    public String getIndentorTag() {
        return IndentorTag;
    }

    public void setIndentorTag(String IndentorTag) {
        this.IndentorTag = IndentorTag;
    }

    public String getIndentorText() {
        return IndentorText;
    }

    public void setIndentorText(String IndentorText) {
        this.IndentorText = IndentorText;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getRequisitionTag() {
        return RequisitionTag;
    }

    public void setRequisitionTag(String RequisitionTag) {
        this.RequisitionTag = RequisitionTag;
    }

    public String getRequisitionText() {
        return RequisitionText;
    }

    public void setRequisitionText(String RequisitionText) {
        this.RequisitionText = RequisitionText;
    }

    public String getUserEntDtLatestLine() {
        return UserEntDtLatestLine;
    }

    public void setUserEntDtLatestLine(String UserEntDtLatestLine) {
        this.UserEntDtLatestLine = UserEntDtLatestLine;
    }

    public RecId getRecid() {
        return recid;
    }

    public void setRecid(RecId recid) {
        this.recid = recid;
    }

    public String getWtaCode() {
        return wtaCode;
    }

    public void setWtaCode(String wtaCode) {
        this.wtaCode = wtaCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getToDepartmentTag() {
        return toDepartmentTag;
    }

    public void setToDepartmentTag(String toDepartmentTag) {
        this.toDepartmentTag = toDepartmentTag;
    }

    public String getToDepartmentText() {
        return toDepartmentText;
    }

    public void setToDepartmentText(String toDepartmentText) {
        this.toDepartmentText = toDepartmentText;
    }

    public String getProcurementPlanDocID() {
        return procurementPlanDocID;
    }

    public void setProcurementPlanDocID(String procurementPlanDocID) {
        this.procurementPlanDocID = procurementPlanDocID;
    }

    public String getProcurementPlanRecID() {
        return procurementPlanRecID;
    }

    public void setProcurementPlanRecID(String procurementPlanRecID) {
        this.procurementPlanRecID = procurementPlanRecID;
    }

}
