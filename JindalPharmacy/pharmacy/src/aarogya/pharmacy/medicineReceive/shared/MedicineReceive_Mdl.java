package aarogya.pharmacy.medicineReceive.shared;

import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MedicineReceive_Mdl extends KeyField implements Serializable, IsSerializable {

    private RecId recIdItemReceive = new RecId();
    private String voucherTypeCode;
    private String voucherTypeName;
    private String voucherDate;
    private String refDate;
    private String refNo;
    private String remarkHeader;
    private String receivedByTag;
    private String receivedByText;
    private String departmentTag;
    private String departmentText;
    private String lineItemCodesCsv;
    private String inCorrectSelectedBatchSql;
    private String inCorrectSelectedBarcodeSql;
    private ModifyReason_Mdl modifyReasonMdl;
    private List<GrdMedicineDetail_Mdl> listItemDetail = new ArrayList<GrdMedicineDetail_Mdl>();

    public MedicineReceive_Mdl() {
    }

    public MedicineReceive_Mdl(MedicineReceive_Mdl mrMdl) {
        this(mrMdl.getVoucherTypeCode(), mrMdl.getVoucherTypeName(), mrMdl.getVoucherDate(), mrMdl.getRefDate(), mrMdl.getRefNo(), mrMdl.getRemarkHeader(), mrMdl.getReceivedByTag(), mrMdl.getReceivedByText(), mrMdl.getDepartmentTag(), mrMdl.getDepartmentText(), mrMdl.getLineItemCodesCsv(), mrMdl.getModifyReasonMdl(), mrMdl.getListItemDetail(), mrMdl.getSharedKeyFieldCode(), mrMdl.getSharedKeyFieldName(), mrMdl.getInCorrectSelectedBatchSql(), mrMdl.getInCorrectSelectedBarcodeSql());
    }

    private MedicineReceive_Mdl(String voucherTypeCode, String voucherTypeName, String voucherDate, String refDate, String refNo, String remarkHeader, String receivedByTag, String receivedByText, String departmentTag, String departmentText, String lineItemCodesCsv, ModifyReason_Mdl modifyReasonMdl, List<GrdMedicineDetail_Mdl> listItemDetail, String KeyFieldCode, String KeyFieldName, String inCorrectSelectedBatchSql, String inCorrectSelectedBarcodeSql) {
        super(KeyFieldCode, KeyFieldName);
        this.voucherTypeCode = voucherTypeCode;
        this.voucherTypeName = voucherTypeName;
        this.voucherDate = voucherDate;
        this.refDate = refDate;
        this.refNo = refNo;
        this.remarkHeader = remarkHeader;
        this.receivedByTag = receivedByTag;
        this.receivedByText = receivedByText;
        this.departmentTag = departmentTag;
        this.departmentText = departmentText;
        this.lineItemCodesCsv = lineItemCodesCsv;
        this.inCorrectSelectedBatchSql = inCorrectSelectedBatchSql;
        this.inCorrectSelectedBarcodeSql = inCorrectSelectedBarcodeSql;
        this.modifyReasonMdl = modifyReasonMdl != null ? new ModifyReason_Mdl(modifyReasonMdl) : null;

        this.listItemDetail = new ArrayList<>();
        for (GrdMedicineDetail_Mdl gmMdl : listItemDetail) {
            this.listItemDetail.add(new GrdMedicineDetail_Mdl(gmMdl));
        }
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

    public String getRemarkHeader() {
        return remarkHeader;
    }

    public void setRemarkHeader(String remarkHeader) {
        this.remarkHeader = remarkHeader;
    }

    public ModifyReason_Mdl getModifyReasonMdl() {
        return modifyReasonMdl;
    }

    public void setModifyReasonMdl(ModifyReason_Mdl modifyReasonMdl) {
        this.modifyReasonMdl = modifyReasonMdl;
    }

    public RecId getRecIdItemReceive() {
        return recIdItemReceive;
    }

    public void setRecIdItemReceive(RecId recIdItemReceive) {
        this.recIdItemReceive = recIdItemReceive;
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

    public String getLineItemCodesCsv() {
        return lineItemCodesCsv;
    }

    public void setLineItemCodesCsv(String lineItemCodesCsv) {
        this.lineItemCodesCsv = lineItemCodesCsv;
    }

    public List<GrdMedicineDetail_Mdl> getListItemDetail() {
        return listItemDetail;
    }

    public void setListItemDetail(List<GrdMedicineDetail_Mdl> listItemDetail) {
        this.listItemDetail = listItemDetail;
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

}
