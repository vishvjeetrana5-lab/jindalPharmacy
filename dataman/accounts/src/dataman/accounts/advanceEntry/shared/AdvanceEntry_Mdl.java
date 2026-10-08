package dataman.accounts.advanceEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import dataman.dmBase.self.shared.RecId;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AdvanceEntry_Mdl extends KeyField implements Serializable, IsSerializable {

    private RecId recId = new RecId();
    private String advReqDocId;
    private String advReqRecId;

    private String poDocId;
    private String poRecId;
    private String pBillDocid;
    private String poDate;
    private String supplierTag;
    private String supplierText;
    private String supplierDisplayName;
    private String poAmt;

    private String voucherDate;

    private String moduleTag;
    private String moduleText;

    private String voucherTypeTag;
    private String voucherTypeText;

    private String dueDate;
    private String remark;
    private String approvedBy;
    private String advanceReqAmt;
    private String totalPoAmt;
    private String totalMrnAmt;
    private String totalBillAmt;
    private String totalAdvancePaidAmt;
    private String totalBillPaymentAmt;
    private String partyLedgerBal;
    private String bankCashLedgerBal;
    private String paidAmount;
    private String originalAmount;
    private String tdsDeduction;
    private String tdsCategoryCode;
    private String tdsCategoryName;

    private List<GrdPODetail_Mdl> lstGrdPoDetail = new ArrayList<GrdPODetail_Mdl>();
    private List<GrdGRNDetail_Mdl> lstGrdMrnDetail = new ArrayList<GrdGRNDetail_Mdl>();
    private List<GrdBillDetail_Mdl> lstGrdBillDetail = new ArrayList<GrdBillDetail_Mdl>();
    private List<GrdAdvPaymentDetail_Mdl> lstGrdAdvPaymentDetails = new ArrayList<GrdAdvPaymentDetail_Mdl>();
    private List<GrdFinalPaymentDetail_Mdl> lstGrdFinalPaymentDetails = new ArrayList<GrdFinalPaymentDetail_Mdl>();
 //   private ModifyReason_Mdl reasonInfo_Mdl = new ModifyReason_Mdl();

    private PaymentPopUp_Mdl paymentPopUp_Mdl = new PaymentPopUp_Mdl();

    public AdvanceEntry_Mdl() {

    }

    public RecId getRecId() {
        return recId;
    }

    public void setRecId(RecId recId) {
        this.recId = recId;
    }

    public String getAdvReqDocId() {
        return advReqDocId;
    }

    public void setAdvReqDocId(String advReqDocId) {
        this.advReqDocId = advReqDocId;
    }

    public String getAdvReqRecId() {
        return advReqRecId;
    }

    public void setAdvReqRecId(String advReqRecId) {
        this.advReqRecId = advReqRecId;
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

    public String getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        this.paidAmount = paidAmount;
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

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getTdsCategoryCode() {
        return tdsCategoryCode;
    }

    public void setTdsCategoryCode(String tdsCategoryCode) {
        this.tdsCategoryCode = tdsCategoryCode;
    }

    public String getTdsCategoryName() {
        return tdsCategoryName;
    }

    public void setTdsCategoryName(String tdsCategoryName) {
        this.tdsCategoryName = tdsCategoryName;
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

    public String getTotalBillAmt() {
        return totalBillAmt;
    }

    public void setTotalBillAmt(String totalBillAmt) {
        this.totalBillAmt = totalBillAmt;
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

    public String getPartyLedgerBal() {
        return partyLedgerBal;
    }

    public void setPartyLedgerBal(String partyLedgerBal) {
        this.partyLedgerBal = partyLedgerBal;
    }

    public String getBankCashLedgerBal() {
        return bankCashLedgerBal;
    }

    public void setBankCashLedgerBal(String bankCashLedgerBal) {
        this.bankCashLedgerBal = bankCashLedgerBal;
    }


    public List<GrdPODetail_Mdl> getLstGrdPoDetail() {
        return lstGrdPoDetail;
    }

    public void setLstGrdPoDetail(List<GrdPODetail_Mdl> lstGrdPoDetail) {
        this.lstGrdPoDetail = lstGrdPoDetail;
    }

    public List<GrdGRNDetail_Mdl> getLstGrdMrnDetail() {
        return lstGrdMrnDetail;
    }

    public void setLstGrdMrnDetail(List<GrdGRNDetail_Mdl> lstGrdMrnDetail) {
        this.lstGrdMrnDetail = lstGrdMrnDetail;
    }

    public List<GrdBillDetail_Mdl> getLstGrdBillDetail() {
        return lstGrdBillDetail;
    }

    public void setLstGrdBillDetail(List<GrdBillDetail_Mdl> lstGrdBillDetail) {
        this.lstGrdBillDetail = lstGrdBillDetail;
    }

    public List<GrdAdvPaymentDetail_Mdl> getLstGrdAdvPaymentDetails() {
        return lstGrdAdvPaymentDetails;
    }

    public void setLstGrdAdvPaymentDetails(List<GrdAdvPaymentDetail_Mdl> lstGrdAdvPaymentDetails) {
        this.lstGrdAdvPaymentDetails = lstGrdAdvPaymentDetails;
    }

    public List<GrdFinalPaymentDetail_Mdl> getLstGrdFinalPaymentDetails() {
        return lstGrdFinalPaymentDetails;
    }

    public void setLstGrdFinalPaymentDetails(List<GrdFinalPaymentDetail_Mdl> lstGrdFinalPaymentDetails) {
        this.lstGrdFinalPaymentDetails = lstGrdFinalPaymentDetails;
    }
//
//    public ModifyReason_Mdl getReasonInfo_Mdl() {
//        return reasonInfo_Mdl;
//    }
//
//    public void setReasonInfo_Mdl(ModifyReason_Mdl reasonInfo_Mdl) {
//        this.reasonInfo_Mdl = reasonInfo_Mdl;
//    }

    public PaymentPopUp_Mdl getPaymentPopUp_Mdl() {
        return paymentPopUp_Mdl;
    }

    public void setPaymentPopUp_Mdl(PaymentPopUp_Mdl paymentPopUp_Mdl) {
        this.paymentPopUp_Mdl = paymentPopUp_Mdl;
    }

    public String getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(String originalAmount) {
        this.originalAmount = originalAmount;
    }

    public String getTdsDeduction() {
        return tdsDeduction;
    }

    public void setTdsDeduction(String tdsDeduction) {
        this.tdsDeduction = tdsDeduction;
    }

    public String getpBillDocid() {
        return pBillDocid;
    }

    public void setpBillDocid(String pBillDocid) {
        this.pBillDocid = pBillDocid;
    }

    public String getSupplierDisplayName() {
        return supplierDisplayName;
    }

    public void setSupplierDisplayName(String supplierDisplayName) {
        this.supplierDisplayName = supplierDisplayName;
    }


}
