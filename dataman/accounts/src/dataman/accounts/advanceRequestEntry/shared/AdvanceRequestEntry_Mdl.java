package dataman.accounts.advanceRequestEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import dataman.dmBase.self.shared.RecId;
import dataman.dmToolkit.dmGUI.shared.DmGUI_Mdl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AdvanceRequestEntry_Mdl extends KeyField implements IsSerializable, Serializable {

    private RecId recId = new RecId();
    private String poDocId;
    private String poRecId;
    private String pBillDocid;
    private String poDate;
    private String supplierTag;
    private String supplierText;
    private String poAmt;

    private String voucherDate;

    private String moduleTag;
    private String moduleText;

    private String voucherTypeTag;
    private String voucherTypeText;
    private String advanceEntryDocId;

    private String dueDate;
    private String remark;
    private String isStatus;
    private String approvedBy;
    private String advanceReqAmt;
    private String totalPoAmt;
    private String totalMrnAmt;
    private String totaPlBillAmt;
    private String totalAdvancePaidAmt;
    private String totalBillPaymentAmt;
    private List<GrdPODetail_Mdl> lstGrdPoDetail = new ArrayList<GrdPODetail_Mdl>();
    private List<GrdGRNDetail_Mdl> lstGrdMrnDetail = new ArrayList<GrdGRNDetail_Mdl>();
    private List<GrdBillDetail_Mdl> lstGrdBillDetail = new ArrayList<GrdBillDetail_Mdl>();
    private List<GrdAdvPaymentDetail_Mdl> lstGrdAdvPaymentDetails = new ArrayList<GrdAdvPaymentDetail_Mdl>();
    private List<GrdFinalPaymentDetail_Mdl> lstGrdFinalPaymentDetails = new ArrayList<GrdFinalPaymentDetail_Mdl>();
    //  private ModifyReason_Mdl reasonInfo_Mdl = new ModifyReason_Mdl();

    public AdvanceRequestEntry_Mdl() {

    }

    public RecId getRecId() {
        return recId;
    }

    public void setRecId(RecId recId) {
        this.recId = recId;
    }

    public String getPoDocId() {
        return poDocId;
    }

    public void setPoDocId(String poDocId) {
        this.poDocId = poDocId;
    }

    public String getPoRecId() {
        return poRecId;
    }

    public void setPoRecId(String poRecId) {
        this.poRecId = poRecId;
    }

    public String getPoDate() {
        return poDate;
    }

    public void setPoDate(String poDate) {
        this.poDate = poDate;
    }

    public String getSupplierTag() {
        return supplierTag;
    }

    public void setSupplierTag(String supplierTag) {
        this.supplierTag = supplierTag;
    }

    public String getSupplierText() {
        return supplierText;
    }

    public void setSupplierText(String supplierText) {
        this.supplierText = supplierText;
    }

    public String getPoAmt() {
        return poAmt;
    }

    public void setPoAmt(String poAmt) {
        this.poAmt = poAmt;
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public String getModuleTag() {
        return moduleTag;
    }

    public void setModuleTag(String moduleTag) {
        this.moduleTag = moduleTag;
    }

    public String getModuleText() {
        return moduleText;
    }

    public void setModuleText(String moduleText) {
        this.moduleText = moduleText;
    }

    public String getVoucherTypeTag() {
        return voucherTypeTag;
    }

    public void setVoucherTypeTag(String voucherTypeTag) {
        this.voucherTypeTag = voucherTypeTag;
    }

    public String getVoucherTypeText() {
        return voucherTypeText;
    }

    public void setVoucherTypeText(String voucherTypeText) {
        this.voucherTypeText = voucherTypeText;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAdvanceReqAmt() {
        return advanceReqAmt;
    }

    public void setAdvanceReqAmt(String advanceReqAmt) {
        this.advanceReqAmt = advanceReqAmt;
    }

    public String getTotalPoAmt() {
        return totalPoAmt;
    }

    public void setTotalPoAmt(String totalPoAmt) {
        this.totalPoAmt = totalPoAmt;
    }

    public String getTotalMrnAmt() {
        return totalMrnAmt;
    }

    public void setTotalMrnAmt(String totalMrnAmt) {
        this.totalMrnAmt = totalMrnAmt;
    }

    public String getTotaPlBillAmt() {
        return totaPlBillAmt;
    }

    public void setTotaPlBillAmt(String totaPlBillAmt) {
        this.totaPlBillAmt = totaPlBillAmt;
    }

    public String getTotalAdvancePaidAmt() {
        return totalAdvancePaidAmt;
    }

    public void setTotalAdvancePaidAmt(String totalAdvancePaidAmt) {
        this.totalAdvancePaidAmt = totalAdvancePaidAmt;
    }

    public String getTotalBillPaymentAmt() {
        return totalBillPaymentAmt;
    }

    public void setTotalBillPaymentAmt(String totalBillPaymentAmt) {
        this.totalBillPaymentAmt = totalBillPaymentAmt;
    }

    public List<GrdPODetail_Mdl> getLstGrdPoDetail() {
        return lstGrdPoDetail;
    }

    public List<GrdGRNDetail_Mdl> getLstGrdMrnDetail() {
        return lstGrdMrnDetail;
    }

    public List<GrdBillDetail_Mdl> getLstGrdBillDetail() {
        return lstGrdBillDetail;
    }

    public List<GrdAdvPaymentDetail_Mdl> getLstGrdAdvPaymentDetails() {
        return lstGrdAdvPaymentDetails;
    }

    public List<GrdFinalPaymentDetail_Mdl> getLstGrdFinalPaymentDetails() {
        return lstGrdFinalPaymentDetails;
    }

//    public ModifyReason_Mdl getReasonInfo_Mdl() {
//        return reasonInfo_Mdl;
//    }
//
//    public void setReasonInfo_Mdl(ModifyReason_Mdl reasonInfo_Mdl) {
//        this.reasonInfo_Mdl = reasonInfo_Mdl;
//    }
    public String getAdvanceEntryDocId() {
        return advanceEntryDocId;
    }

    public void setAdvanceEntryDocId(String advanceEntryDocId) {
        this.advanceEntryDocId = advanceEntryDocId;
    }

    public String getIsStatus() {
        return isStatus;
    }

    public void setIsStatus(String isStatus) {
        this.isStatus = isStatus;
    }

    public String getpBillDocid() {
        return pBillDocid;
    }

    public void setpBillDocid(String pBillDocid) {
        this.pBillDocid = pBillDocid;
    }

}
