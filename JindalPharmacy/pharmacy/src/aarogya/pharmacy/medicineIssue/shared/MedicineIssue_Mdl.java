package aarogya.pharmacy.medicineIssue.shared;

import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MedicineIssue_Mdl extends KeyField implements Serializable, IsSerializable {

    private RecId recIdMedicineIssue = new RecId();
    private String voucherTypeCode;
    private String voucherTypeName;
    private String voucherDate;
    private String issueToTag;
    private String issueToText;
    private String departmentTag;
    private String departmentText;
    private String lineItemCodesCsv;
    private String loginDate;
    private String requisitionDocId;
    private String requisitionNo;
    private String refNo;
    private String refDate;
    private String isExpiryMedicine;
    private String remarkHeader;
    private String requisitionItemChangeCondition;
    private String inCorrectSelectedBatchSql;
    private String inCorrectSelectedBarcodeSql;
    private ModifyReason_Mdl modifyReasonMdl;
    private String departmentToTag;
    private boolean inventoryAccountingActive;
    
    private List<GrdMedicineDetail_Mdl> listMedicineDetail = new ArrayList<GrdMedicineDetail_Mdl>();

    public MedicineIssue_Mdl() {
    }

    public String getIsExpiryMedicine() {
        return isExpiryMedicine;
    }

    public void setIsExpiryMedicine(String isExpiryMedicine) {
        this.isExpiryMedicine = isExpiryMedicine;
    }

    public String getRemarkHeader() {
        return remarkHeader;
    }

    public void setRemarkHeader(String remarkHeader) {
        this.remarkHeader = remarkHeader;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getRefDate() {
        return refDate;
    }

    public void setRefDate(String refDate) {
        this.refDate = refDate;
    }

    public ModifyReason_Mdl getModifyReasonMdl() {
        return modifyReasonMdl;
    }

    public void setModifyReasonMdl(ModifyReason_Mdl modifyReasonMdl) {
        this.modifyReasonMdl = modifyReasonMdl;
    }

    public String getIssueToTag() {
        return issueToTag;
    }

    public void setIssueToTag(String issueToTag) {
        this.issueToTag = issueToTag;
    }

    public String getIssueToText() {
        return issueToText;
    }

    public void setIssueToText(String issueToText) {
        this.issueToText = issueToText;
    }

    public RecId getRecIdMedicineIssue() {
        return recIdMedicineIssue;
    }

    public void setRecIdMedicineIssue(RecId recIdMedicineIssue) {
        this.recIdMedicineIssue = recIdMedicineIssue;
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

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
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

    public String getLineItemCodesCsv() {
        return lineItemCodesCsv;
    }

    public void setLineItemCodesCsv(String lineItemCodesCsv) {
        this.lineItemCodesCsv = lineItemCodesCsv;
    }

    public String getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(String loginDate) {
        this.loginDate = loginDate;
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

    public List<GrdMedicineDetail_Mdl> getListMedicineDetail() {
        return listMedicineDetail;
    }

    public void setListMedicineDetail(List<GrdMedicineDetail_Mdl> listMedicineDetail) {
        this.listMedicineDetail = listMedicineDetail;
    }

    public String getDepartmentToTag() {
        return departmentToTag;
    }

    public void setDepartmentToTag(String departmentToTag) {
        this.departmentToTag = departmentToTag;
    }

    public String getInCorrectSelectedBatchSql() {
        return inCorrectSelectedBatchSql;
    }

    public void setInCorrectSelectedBatchSql(String inCorrectSelectedBatchSql) {
        this.inCorrectSelectedBatchSql = inCorrectSelectedBatchSql;
    }

    public String getInCorrectSelectedBarcodeSql() {
        return inCorrectSelectedBarcodeSql;
    }

    public void setInCorrectSelectedBarcodeSql(String inCorrectSelectedBarcodeSql) {
        this.inCorrectSelectedBarcodeSql = inCorrectSelectedBarcodeSql;
    }

    public boolean isInventoryAccountingActive() {
        return inventoryAccountingActive;
    }

    public void setInventoryAccountingActive(boolean inventoryAccountingActive) {
        this.inventoryAccountingActive = inventoryAccountingActive;
    }

}
