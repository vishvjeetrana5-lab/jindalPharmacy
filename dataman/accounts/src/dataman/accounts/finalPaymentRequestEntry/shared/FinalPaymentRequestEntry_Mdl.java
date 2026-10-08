package dataman.accounts.finalPaymentRequestEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import dataman.dmBase.self.shared.RecId;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FinalPaymentRequestEntry_Mdl extends KeyField implements Serializable, IsSerializable {

    private RecId recId = new RecId();
    private String supplierTag;
    private String supplierText;

    private String voucherDate;

    private String moduleTag;
    private String moduleText;

    private String voucherTypeTag;
    private String voucherTypeText;

    private String pBillDocId;
    private String pBillRecId;
    private String pBillDate;
    private String pBillAmount;
    private String pBillTDS;
    private String pBillNET;
    private String pBillApprovedBy;
    private String pBillApprovedDt;
    private String pBillSupplierTag;
    private String headerSupplierText;
    private String pBillSelectedSupplierText;
    private String pBillSupplierText;
    private String pBillRemark;
    private String payDueDate;
    private String totalPOAmount;
    private String totalGrnAmount;
    private String totalBillAmount;
    private String totalTds;
    private String totalNetBillEntry;
    private String totalAdvNet;
    private String totalBillPaymnet;
    private String paymentDate;
    private String finalDuePayment;
    private String remark;
    private String partPay;
    private String approvedBy;
    private String poAdvanceSetteled;
    private String pBillAdvanceSetteled;
    private List<GrdPODetail_Mdl> lstGrdPoDetail = new ArrayList<GrdPODetail_Mdl>();
    private List<GrdGRNDetail_Mdl> lstGrdMrnDetail = new ArrayList<GrdGRNDetail_Mdl>();
    private List<GrdBillDetail_Mdl> lstGrdBillDetail = new ArrayList<GrdBillDetail_Mdl>();
    private List<GrdAdvPaymentDetail_Mdl> lstGrdAdvPaymentDetails = new ArrayList<GrdAdvPaymentDetail_Mdl>();
    private List<GrdFinalPaymentDetail_Mdl> lstGrdFinalPaymentDetails = new ArrayList<GrdFinalPaymentDetail_Mdl>();

    public FinalPaymentRequestEntry_Mdl() {

    }

    public String getpBillTDS() {
        return pBillTDS;
    }

    public void setpBillTDS(String pBillTDS) {
        this.pBillTDS = pBillTDS;
    }

    public String getpBillNET() {
        return pBillNET;
    }

    public void setpBillNET(String pBillNET) {
        this.pBillNET = pBillNET;
    }

    public String getpBillAdvanceSetteled() {
        return pBillAdvanceSetteled;
    }

    public void setpBillAdvanceSetteled(String pBillAdvanceSetteled) {
        this.pBillAdvanceSetteled = pBillAdvanceSetteled;
    }

    public String getPoAdvanceSetteled() {
        return poAdvanceSetteled;
    }

    public void setPoAdvanceSetteled(String poAdvanceSetteled) {
        this.poAdvanceSetteled = poAdvanceSetteled;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public RecId getRecId() {
        return recId;
    }

    public void setRecId(RecId recId) {
        this.recId = recId;
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

    public String getpBillDocId() {
        return pBillDocId;
    }

    public void setpBillDocId(String pBillDocId) {
        this.pBillDocId = pBillDocId;
    }

    public String getpBillRecId() {
        return pBillRecId;
    }

    public void setpBillRecId(String pBillRecId) {
        this.pBillRecId = pBillRecId;
    }

    public String getpBillDate() {
        return pBillDate;
    }

    public void setpBillDate(String pBillDate) {
        this.pBillDate = pBillDate;
    }

    public String getpBillAmount() {
        return pBillAmount;
    }

    public void setpBillAmount(String pBillAmount) {
        this.pBillAmount = pBillAmount;
    }

    public String getpBillApprovedBy() {
        return pBillApprovedBy;
    }

    public void setpBillApprovedBy(String pBillApprovedBy) {
        this.pBillApprovedBy = pBillApprovedBy;
    }

    public String getpBillApprovedDt() {
        return pBillApprovedDt;
    }

    public void setpBillApprovedDt(String pBillApprovedDt) {
        this.pBillApprovedDt = pBillApprovedDt;
    }

    public String getpBillSupplierTag() {
        return pBillSupplierTag;
    }

    public void setpBillSupplierTag(String pBillSupplierTag) {
        this.pBillSupplierTag = pBillSupplierTag;
    }

    public String getpBillSupplierText() {
        return pBillSupplierText;
    }

    public void setpBillSupplierText(String pBillSupplierText) {
        this.pBillSupplierText = pBillSupplierText;
    }

    public String getpBillRemark() {
        return pBillRemark;
    }

    public void setpBillRemark(String pBillRemark) {
        this.pBillRemark = pBillRemark;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getFinalDuePayment() {
        return finalDuePayment;
    }

    public void setFinalDuePayment(String finalDuePayment) {
        this.finalDuePayment = finalDuePayment;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTotalPOAmount() {
        return totalPOAmount;
    }

    public void setTotalPOAmount(String totalPOAmount) {
        this.totalPOAmount = totalPOAmount;
    }

    public String getTotalGrnAmount() {
        return totalGrnAmount;
    }

    public void setTotalGrnAmount(String totalGrnAmount) {
        this.totalGrnAmount = totalGrnAmount;
    }

    public String getTotalBillAmount() {
        return totalBillAmount;
    }

    public void setTotalBillAmount(String totalBillAmount) {
        this.totalBillAmount = totalBillAmount;
    }

    public String getTotalTds() {
        return totalTds;
    }

    public void setTotalTds(String totalTds) {
        this.totalTds = totalTds;
    }

    public String getTotalNetBillEntry() {
        return totalNetBillEntry;
    }

    public void setTotalNetBillEntry(String totalNetBillEntry) {
        this.totalNetBillEntry = totalNetBillEntry;
    }

    public String getTotalAdvNet() {
        return totalAdvNet;
    }

    public void setTotalAdvNet(String totalAdvNet) {
        this.totalAdvNet = totalAdvNet;
    }

    public String getTotalBillPaymnet() {
        return totalBillPaymnet;
    }

    public void setTotalBillPaymnet(String totalBillPaymnet) {
        this.totalBillPaymnet = totalBillPaymnet;
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

    public String getPayDueDate() {
        return payDueDate;
    }

    public void setPayDueDate(String payDueDate) {
        this.payDueDate = payDueDate;
    }

    public String getPartPay() {
        return partPay;
    }

    public void setPartPay(String partPay) {
        this.partPay = partPay;
    }

    public String getHeaderSupplierText() {
        return headerSupplierText;
    }

    public void setHeaderSupplierText(String headerSupplierText) {
        this.headerSupplierText = headerSupplierText;
    }

    public String getpBillSelectedSupplierText() {
        return pBillSelectedSupplierText;
    }

    public void setpBillSelectedSupplierText(String pBillSelectedSupplierText) {
        this.pBillSelectedSupplierText = pBillSelectedSupplierText;
    }

}
