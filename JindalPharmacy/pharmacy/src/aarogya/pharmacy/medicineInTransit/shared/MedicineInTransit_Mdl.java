package aarogya.pharmacy.medicineInTransit.shared;

import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MedicineInTransit_Mdl extends KeyField implements Serializable, IsSerializable {

    private RecId recIdTransfer = new RecId();
    private String voucherTypeCode;
    private String voucherTypeName;
    private String voucherDate;
    private String departmentFromTag;
    private String departmentFromText;
    private String departmentFromSite;
    private String departmentToTag;
    private String departmentToText;
    private String departmentToSite;
    private String refNo;
    private String refDate;
    private String receivedByTag;
    private String receivedByText;
    private String transferToText;
    private String transferToTag;
    private ModifyReason_Mdl modifyReasonMdl;
    private List<GrdMedicineDetail_Mdl> listItemDetail = new ArrayList<GrdMedicineDetail_Mdl>();
    private String lineItemCodesCsv;

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

    public String getInTransitDocID() {
        return inTransitDocID;
    }

    public void setInTransitDocID(String inTransitDocID) {
        this.inTransitDocID = inTransitDocID;
    }
    private String requisitionItemChangeCondition;
    private String requisitionDocId;
    private String requisitionNo;
    private String inCorrectSelectedBatchSql;
    private String inCorrectSelectedBarcodeSql;
    private String inTransitDocID;
    private String inTransitRecID;

    public MedicineInTransit_Mdl() {
    }

    public MedicineInTransit_Mdl(MedicineInTransit_Mdl mtMdl) {
        this(mtMdl.getRequisitionDocId(), mtMdl.getRequisitionNo(), mtMdl.getLineItemCodesCsv(), mtMdl.getRequisitionItemChangeCondition(), mtMdl.getVoucherTypeCode(), mtMdl.getVoucherTypeName(), mtMdl.getVoucherDate(), mtMdl.getDepartmentFromTag(), mtMdl.getDepartmentFromText(), mtMdl.getDepartmentFromSite(), mtMdl.getDepartmentToTag(), mtMdl.getDepartmentToText(), mtMdl.getDepartmentToSite(), mtMdl.getRefNo(), mtMdl.getRefDate(), mtMdl.getTransferToTag(), mtMdl.getTransferToText(), mtMdl.getModifyReasonMdl(), mtMdl.getListItemDetail(), mtMdl.getSharedKeyFieldCode(), mtMdl.getSharedKeyFieldName(), mtMdl.getInCorrectSelectedBatchSql(), mtMdl.getInCorrectSelectedBarcodeSql(), mtMdl.getinTransitDocID(), mtMdl.getinTransitRecID(), mtMdl.getReceivedByTag(), mtMdl.getReceivedByText());
    }

    private MedicineInTransit_Mdl(String requisitionDocId, String requisitionNo, String lineItemCodesCsv, String requisitionItemChangeCondition, String voucherTypeCode, String voucherTypeName, String voucherDate, String departmentFromTag, String departmentFromText, String departmentFromSite, String departmentToTag, String departmentToText, String departmentToSite, String refNo, String refDate, String transferToTag, String transferToText, ModifyReason_Mdl modifyReasonMdl, List<GrdMedicineDetail_Mdl> listItemDetail, String KeyFieldCode, String KeyFieldName, String inCorrectSelectedBatchSql, String inCorrectSelectedBarcodeSql, String inTransitDocID, String inTransitRecID, String receivedByTag, String receivedBYText) {
        super(KeyFieldCode, KeyFieldName);
        this.voucherTypeCode = voucherTypeCode;
        this.voucherTypeName = voucherTypeName;
        this.voucherDate = voucherDate;
        this.departmentFromTag = departmentFromTag;
        this.departmentFromText = departmentFromText;
        this.departmentFromSite = departmentFromSite;
        this.departmentToTag = departmentToTag;
        this.departmentToText = departmentToText;
        this.departmentToSite = departmentToSite;
        this.refNo = refNo;
        this.refDate = refDate;
        this.receivedByTag = receivedByTag;
        this.receivedByText = receivedByText;
        this.modifyReasonMdl = modifyReasonMdl != null ? new ModifyReason_Mdl(modifyReasonMdl) : null;

        this.listItemDetail = new ArrayList<>();
        for (GrdMedicineDetail_Mdl gmdMdl : listItemDetail) {
            this.listItemDetail.add(new GrdMedicineDetail_Mdl(gmdMdl));
        }

        this.lineItemCodesCsv = lineItemCodesCsv;
        this.requisitionItemChangeCondition = requisitionItemChangeCondition;
        this.requisitionDocId = requisitionDocId;
        this.requisitionNo = requisitionNo;

        this.inCorrectSelectedBatchSql = inCorrectSelectedBatchSql;
        this.inCorrectSelectedBarcodeSql = inCorrectSelectedBarcodeSql;
        this.inTransitDocID = inTransitDocID;
    }

    public ModifyReason_Mdl getModifyReasonMdl() {
        return modifyReasonMdl;
    }

    public void setModifyReasonMdl(ModifyReason_Mdl modifyReasonMdl) {
        this.modifyReasonMdl = modifyReasonMdl;
    }

    public RecId getRecIdTransfer() {
        return recIdTransfer;
    }

    public void setRecIdTransfer(RecId recIdTransfer) {
        this.recIdTransfer = recIdTransfer;
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

    public String getDepartmentFromTag() {
        return departmentFromTag;
    }

    public void setDepartmentFromTag(String departmentFromTag) {
        this.departmentFromTag = departmentFromTag;
    }

    public String getDepartmentFromText() {
        return departmentFromText;
    }

    public void setDepartmentFromText(String departmentFromText) {
        this.departmentFromText = departmentFromText;
    }

    public String getDepartmentFromSite() {
        return departmentFromSite;
    }

    public void setDepartmentFromSite(String departmentFromSite) {
        this.departmentFromSite = departmentFromSite;
    }

    public String getDepartmentToTag() {
        return departmentToTag;
    }

    public void setDepartmentToTag(String departmentToTag) {
        this.departmentToTag = departmentToTag;
    }

    public String getDepartmentToText() {
        return departmentToText;
    }

    public void setDepartmentToText(String departmentToText) {
        this.departmentToText = departmentToText;
    }

    public String getDepartmentToSite() {
        return departmentToSite;
    }

    public void setDepartmentToSite(String departmentToSite) {
        this.departmentToSite = departmentToSite;
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

    public String getTransferToText() {
        return transferToText;
    }

    public void setTransferToText(String transferToText) {
        this.transferToText = transferToText;
    }

    public String getTransferToTag() {
        return transferToTag;
    }

    public void setTransferToTag(String transferToTag) {
        this.transferToTag = transferToTag;
    }

    public List<GrdMedicineDetail_Mdl> getListItemDetail() {
        return listItemDetail;
    }

    public void setListItemDetail(List<GrdMedicineDetail_Mdl> listItemDetail) {
        this.listItemDetail = listItemDetail;
    }

    public String getLineItemCodesCsv() {
        return lineItemCodesCsv;
    }

    public void setLineItemCodesCsv(String lineItemCodesCsv) {
        this.lineItemCodesCsv = lineItemCodesCsv;
    }

    public String getRequisitionItemChangeCondition() {
        return requisitionItemChangeCondition;
    }

    public void setRequisitionItemChangeCondition(String requisitionItemChangeCondition) {
        this.requisitionItemChangeCondition = requisitionItemChangeCondition;
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

    public String getinTransitDocID() {
        return inTransitDocID;
    }

    public void setinTransitDocID(String inTransitDocID) {
        this.inTransitDocID = inTransitDocID;
    }

    public String getinTransitRecID() {
        return inTransitRecID;
    }

    public void setinTransitRecID(String inTransitRecID) {
        this.inTransitRecID = inTransitRecID;
    }

}
