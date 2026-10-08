package aarogya.inventory.itemTransfer.shared;

import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ItemTransfer_Mdl extends KeyField implements Serializable, IsSerializable {

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
    private String transferToTag;
    private String transferToText;
    private ModifyReason_Mdl modifyReasonMdl;
    private List<GrdItemDetail_Mdl> listItemDetail = new ArrayList<GrdItemDetail_Mdl>();
    private String requisitionDocId;
    private String requisitionNo;
    private String lineItemCodesCsv;
    private String requisitionItemChangeCondition;
    private String approvedBy;
    private String WtaCode;
    private String remark;

    public ItemTransfer_Mdl() {
    }

    public ItemTransfer_Mdl(ItemTransfer_Mdl itMdl) {
        this(itMdl.getLineItemCodesCsv(), itMdl.getRequisitionItemChangeCondition(), itMdl.getRequisitionDocId(), itMdl.getRequisitionNo(), itMdl.getVoucherTypeCode(), itMdl.getVoucherTypeName(), itMdl.getVoucherDate(), itMdl.getDepartmentFromTag(), itMdl.getDepartmentFromText(), itMdl.getDepartmentFromSite(), itMdl.getDepartmentToTag(), itMdl.getDepartmentToText(), itMdl.getDepartmentToSite(), itMdl.getRefNo(), itMdl.getRefDate(), itMdl.getTransferToTag(), itMdl.getTransferToText(), itMdl.getModifyReasonMdl(), itMdl.getListItemDetail(), itMdl.getSharedKeyFieldCode(), itMdl.getSharedKeyFieldName());
    }

    public ItemTransfer_Mdl(String lineItemCodesCsv, String requisitionItemChangeCondition, String requisitionDocId, String requisitionNo, String voucherTypeCode, String voucherTypeName, String voucherDate, String departmentFromTag, String departmentFromText, String departmentFromSite, String departmentToTag, String departmentToText, String departmentToSite, String refNo, String refDate, String transferToTag, String transferToText, ModifyReason_Mdl modifyReasonMdl, List<GrdItemDetail_Mdl> listItemDetail, String KeyFieldCode, String KeyFieldName) {
        super(KeyFieldCode, KeyFieldName);
        this.requisitionDocId = requisitionDocId;
        this.requisitionNo = requisitionNo;
        this.lineItemCodesCsv = lineItemCodesCsv;
        this.requisitionItemChangeCondition = requisitionItemChangeCondition;
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
        this.transferToTag = transferToTag;
        this.transferToText = transferToText;
        this.modifyReasonMdl = modifyReasonMdl != null ? new ModifyReason_Mdl(modifyReasonMdl) : null;
        this.listItemDetail = new ArrayList<>();
        for (GrdItemDetail_Mdl gidMdl : listItemDetail) {
            this.listItemDetail.add(new GrdItemDetail_Mdl(gidMdl));
        }
    }

    public ModifyReason_Mdl getModifyReasonMdl() {
        return modifyReasonMdl;
    }

    public void setModifyReasonMdl(ModifyReason_Mdl modifyReasonMdl) {
        this.modifyReasonMdl = modifyReasonMdl;
    }

    public String getTransferToTag() {
        return transferToTag;
    }

    public void setTransferToTag(String transferToTag) {
        this.transferToTag = transferToTag;
    }

    public String getTransferToText() {
        return transferToText;
    }

    public void setTransferToText(String transferToText) {
        this.transferToText = transferToText;
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

    public List<GrdItemDetail_Mdl> getListItemDetail() {
        return listItemDetail;
    }

    public String getDepartmentFromSite() {
        return departmentFromSite;
    }

    public void setDepartmentFromSite(String departmentFromSite) {
        this.departmentFromSite = departmentFromSite;
    }

    public String getDepartmentToSite() {
        return departmentToSite;
    }

    public void setDepartmentToSite(String departmentToSite) {
        this.departmentToSite = departmentToSite;
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

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getWtaCode() {
        return WtaCode;
    }

    public void setWtaCode(String WtaCode) {
        this.WtaCode = WtaCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
