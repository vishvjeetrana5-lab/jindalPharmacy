package dataman.accounts.wholesaleBillSettlement.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class GrdPendingFPReqDetail_Mdl extends KeyField implements Serializable, IsSerializable {

    private String lgV_SNo;
    private String voucherType;
    private String voucherDate;
    private String partyBillNo;
    private String partyBillDate;
    private String debtorName;
    private String debtorCode;
    private String billAmountAmt;
    private String adjustedAmt;
    private String pendingToAdjustAmt;
    private String checked;

    public GrdPendingFPReqDetail_Mdl() {

    }

    public String getLgV_SNo() {
        return lgV_SNo;
    }

    public void setLgV_SNo(String lgV_SNo) {
        this.lgV_SNo = lgV_SNo;
    }

    public String getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(String voucherType) {
        this.voucherType = voucherType;
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public String getPartyBillNo() {
        return partyBillNo;
    }

    public void setPartyBillNo(String partyBillNo) {
        this.partyBillNo = partyBillNo;
    }

    public String getPartyBillDate() {
        return partyBillDate;
    }

    public void setPartyBillDate(String partyBillDate) {
        this.partyBillDate = partyBillDate;
    }

    public String getDebtorName() {
        return debtorName;
    }

    public void setDebtorName(String debtorName) {
        this.debtorName = debtorName;
    }

    public String getDebtorCode() {
        return debtorCode;
    }

    public void setDebtorCode(String debtorCode) {
        this.debtorCode = debtorCode;
    }

    public String getBillAmountAmt() {
        return billAmountAmt;
    }

    public void setBillAmountAmt(String billAmountAmt) {
        this.billAmountAmt = billAmountAmt;
    }

    public String getAdjustedAmt() {
        return adjustedAmt;
    }

    public void setAdjustedAmt(String adjustedAmt) {
        this.adjustedAmt = adjustedAmt;
    }

    public String getPendingToAdjustAmt() {
        return pendingToAdjustAmt;
    }

    public void setPendingToAdjustAmt(String pendingToAdjustAmt) {
        this.pendingToAdjustAmt = pendingToAdjustAmt;
    }

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

}
