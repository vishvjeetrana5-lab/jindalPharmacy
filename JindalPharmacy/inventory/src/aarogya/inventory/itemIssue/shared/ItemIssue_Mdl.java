package aarogya.inventory.itemIssue.shared;

import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ItemIssue_Mdl extends KeyField implements Serializable, IsSerializable {

    private RecId recIdItemIssue = new RecId();
    private String voucherTypeCode;
    private String voucherTypeName;
    private String voucherTypeShortName;
    private String balancingAc;
    private String voucherDate;
    private String refDate;
    private String refNo;
    private String remarkHeader;
    private String receivedByTag;
    private String receivedByText;
    private String departmentTag;
    private String departmentText;
    private String lineItemCodesCsv;
//    private String loginDate;
    private String requisitionDocId;
    private String requisitionNo;
    private String requisitionItemChangeCondition;
    private ModifyReason_Mdl modifyReasonMdl;
    private String departmentToTag;
    private boolean inventoryAccountingActive;
    private String projectName;
    private String projectCode;
    private String projectAcName;
    private String projectAcCode;
    private String patientName;
    private String consultantName;
    private String patientRegDocID;
    private String patientRegRecID;
    private String roomSearchType;
    private String roomSearchTypeName;
    private String patientTypeName;
    private String patientTypeCode;

    private List<GrdItemDetail_Mdl> listItemDetail = new ArrayList<GrdItemDetail_Mdl>();

    public ItemIssue_Mdl() {
    }

    public String getRemarkHeader() {
        return remarkHeader;
    }

    public void setRemarkHeader(String remarkHeader) {
        this.remarkHeader = remarkHeader;
    }

    public String getRefDate() {
        return refDate;
    }

    public void setRefDate(String refDate) {
        this.refDate = refDate;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public ModifyReason_Mdl getModifyReasonMdl() {
        return modifyReasonMdl;
    }

    public void setModifyReasonMdl(ModifyReason_Mdl modifyReasonMdl) {
        this.modifyReasonMdl = modifyReasonMdl;
    }

    public String getLineItemCodesCsv() {
        return lineItemCodesCsv;
    }

    public void setLineItemCodesCsv(String lineItemCodesCsv) {
        this.lineItemCodesCsv = lineItemCodesCsv;
    }

    public RecId getRecIdItemIssue() {
        return recIdItemIssue;
    }

    public void setRecIdItemIssue(RecId recIdItemIssue) {
        this.recIdItemIssue = recIdItemIssue;
    }

    public String getVoucherTypeCode() {
        return voucherTypeCode;
    }

    public void setVoucherTypeCode(String voucherTypeCode) {
        this.voucherTypeCode = voucherTypeCode;
    }

    public String getVoucherTypeName() {
        return voucherTypeName;
    }

    public void setVoucherTypeName(String voucherTypeName) {
        this.voucherTypeName = voucherTypeName;
    }

    public String getBalancingAc() {
        return balancingAc;
    }

    public void setBalancingAc(String balancingAc) {
        this.balancingAc = balancingAc;
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public String getReceivedByTag() {
        return receivedByTag;
    }

    public void setReceivedByTag(String receivedByTag) {
        this.receivedByTag = receivedByTag;
    }

    public String getReceivedByText() {
        return receivedByText;
    }

    public void setReceivedByText(String receivedByText) {
        this.receivedByText = receivedByText;
    }

    public String getDepartmentTag() {
        return departmentTag;
    }

    public void setDepartmentTag(String departmentTag) {
        this.departmentTag = departmentTag;
    }

    public String getDepartmentText() {
        return departmentText;
    }

    public void setDepartmentText(String departmentText) {
        this.departmentText = departmentText;
    }

    public String getRequisitionDocId() {
        return requisitionDocId;
    }

    public void setRequisitionDocId(String requisitionDocId) {
        this.requisitionDocId = requisitionDocId;
    }

    public String getRequisitionNo() {
        return requisitionNo;
    }

    public void setRequisitionNo(String requisitionNo) {
        this.requisitionNo = requisitionNo;
    }

    public String getRequisitionItemChangeCondition() {
        return requisitionItemChangeCondition;
    }

    public void setRequisitionItemChangeCondition(String requisitionItemChangeCondition) {
        this.requisitionItemChangeCondition = requisitionItemChangeCondition;
    }

    public List<GrdItemDetail_Mdl> getListItemDetail() {
        return listItemDetail;
    }

    public void setListItemDetail(List<GrdItemDetail_Mdl> listItemDetail) {
        this.listItemDetail = listItemDetail;
    }

    public String getDepartmentToTag() {
        return departmentToTag;
    }

    public void setDepartmentToTag(String departmentToTag) {
        this.departmentToTag = departmentToTag;
    }

    public boolean isInventoryAccountingActive() {
        return inventoryAccountingActive;
    }

    public void setInventoryAccountingActive(boolean inventoryAccountingActive) {
        this.inventoryAccountingActive = inventoryAccountingActive;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectAcName() {
        return projectAcName;
    }

    public void setProjectAcName(String projectAcName) {
        this.projectAcName = projectAcName;
    }

    public String getProjectAcCode() {
        return projectAcCode;
    }

    public void setProjectAcCode(String projectAcCode) {
        this.projectAcCode = projectAcCode;
    }

    public String getVoucherTypeShortName() {
        return voucherTypeShortName;
    }

    public void setVoucherTypeShortName(String voucherTypeShortName) {
        this.voucherTypeShortName = voucherTypeShortName;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getConsultantName() {
        return consultantName;
    }

    public void setConsultantName(String consultantName) {
        this.consultantName = consultantName;
    }

    public String getPatientRegDocID() {
        return patientRegDocID;
    }

    public void setPatientRegDocID(String patientRegDocID) {
        this.patientRegDocID = patientRegDocID;
    }

    public String getPatientRegRecID() {
        return patientRegRecID;
    }

    public void setPatientRegRecID(String patientRegRecID) {
        this.patientRegRecID = patientRegRecID;
    }

    public String getRoomSearchType() {
        return roomSearchType;
    }

    public void setRoomSearchType(String roomSearchType) {
        this.roomSearchType = roomSearchType;
    }

    public String getPatientTypeName() {
        return patientTypeName;
    }

    public void setPatientTypeName(String patientTypeName) {
        this.patientTypeName = patientTypeName;
    }

    public String getPatientTypeCode() {
        return patientTypeCode;
    }

    public void setPatientTypeCode(String patientTypeCode) {
        this.patientTypeCode = patientTypeCode;
    }

    public String getRoomSearchTypeName() {
        return roomSearchTypeName;
    }

    public void setRoomSearchTypeName(String roomSearchTypeName) {
        this.roomSearchTypeName = roomSearchTypeName;
    }
}
