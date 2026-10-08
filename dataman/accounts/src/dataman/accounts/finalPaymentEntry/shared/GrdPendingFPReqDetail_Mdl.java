package dataman.accounts.finalPaymentEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class GrdPendingFPReqDetail_Mdl extends KeyField implements Serializable, IsSerializable {

    private String date;
    private String pbillNo;
    private String pbillDocId;
    private String pbillDate;
    private String totalPo;
    private String totalGrn;
    private String totalBill;
    private String totalTds;
    private String netBill;
    private String totalAdv;
    private String totalAdvNet;
    private String totalAdvTds;
    private String totalBillpayment;
    private String finalDuePaymnet;
    private String finalPayReq;
    private String billDueDt;
    private String payDueDt;
    private String enterdBy;
    private String checked;
    private String advanceAdjusted;
    private String partyBillNo;
    private String drAdjustAmt;
    private String pendingToAdjustAmt;
    private String lgV_SNo;

    private boolean isRemove;

    public GrdPendingFPReqDetail_Mdl() {

    }

    public String getPartyBillNo() {
        return partyBillNo;
    }

    public void setPartyBillNo(String partyBillNo) {
        this.partyBillNo = partyBillNo;
    }

    public String getAdvanceAdjusted() {
        return advanceAdjusted;
    }

    public void setAdvanceAdjusted(String advanceAdjusted) {
        this.advanceAdjusted = advanceAdjusted;
    }

    public String getPbillDocId() {
        return pbillDocId;
    }

    public void setPbillDocId(String pbillDocId) {
        this.pbillDocId = pbillDocId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTotalPo() {
        return totalPo;
    }

    public void setTotalPo(String totalPo) {
        this.totalPo = totalPo;
    }

    public String getPbillNo() {
        return pbillNo;
    }

    public void setPbillNo(String pbillNo) {
        this.pbillNo = pbillNo;
    }

    public String getPbillDate() {
        return pbillDate;
    }

    public void setPbillDate(String pbillDate) {
        this.pbillDate = pbillDate;
    }

    public String getTotalGrn() {
        return totalGrn;
    }

    public void setTotalGrn(String totalGrn) {
        this.totalGrn = totalGrn;
    }

    public String getTotalBill() {
        return totalBill;
    }

    public void setTotalBill(String totalBill) {
        this.totalBill = totalBill;
    }

    public String getTotalTds() {
        return totalTds;
    }

    public void setTotalTds(String totalTds) {
        this.totalTds = totalTds;
    }

    public String getNetBill() {
        return netBill;
    }

    public void setNetBill(String netBill) {
        this.netBill = netBill;
    }

    public String getTotalAdv() {
        return totalAdv;
    }

    public void setTotalAdv(String totalAdv) {
        this.totalAdv = totalAdv;
    }

    public String getTotalBillpayment() {
        return totalBillpayment;
    }

    public void setTotalBillpayment(String totalBillpayment) {
        this.totalBillpayment = totalBillpayment;
    }

    public String getFinalDuePaymnet() {
        return finalDuePaymnet;
    }

    public void setFinalDuePaymnet(String finalDuePaymnet) {
        this.finalDuePaymnet = finalDuePaymnet;
    }

    public String getFinalPayReq() {
        return finalPayReq;
    }

    public void setFinalPayReq(String finalPayReq) {
        this.finalPayReq = finalPayReq;
    }

    public String getBillDueDt() {
        return billDueDt;
    }

    public void setBillDueDt(String billDueDt) {
        this.billDueDt = billDueDt;
    }

    public String getPayDueDt() {
        return payDueDt;
    }

    public void setPayDueDt(String payDueDt) {
        this.payDueDt = payDueDt;
    }

    public String getEnterdBy() {
        return enterdBy;
    }

    public void setEnterdBy(String enterdBy) {
        this.enterdBy = enterdBy;
    }

    public String getTotalAdvNet() {
        return totalAdvNet;
    }

    public void setTotalAdvNet(String totalAdvNet) {
        this.totalAdvNet = totalAdvNet;
    }

    public String getTotalAdvTds() {
        return totalAdvTds;
    }

    public void setTotalAdvTds(String totalAdvTds) {
        this.totalAdvTds = totalAdvTds;
    }

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public boolean isIsRemove() {
        return isRemove;
    }

    public void setIsRemove(boolean isRemove) {
        this.isRemove = isRemove;
    }

    public String getDrAdjustAmt() {
        return drAdjustAmt;
    }

    public void setDrAdjustAmt(String drAdjustAmt) {
        this.drAdjustAmt = drAdjustAmt;
    }

    public String getPendingToAdjustAmt() {
        return pendingToAdjustAmt;
    }

    public void setPendingToAdjustAmt(String pendingToAdjustAmt) {
        this.pendingToAdjustAmt = pendingToAdjustAmt;
    }

    public String getLgV_SNo() {
        return lgV_SNo;
    }

    public void setLgV_SNo(String lgV_SNo) {
        this.lgV_SNo = lgV_SNo;
    }

}
