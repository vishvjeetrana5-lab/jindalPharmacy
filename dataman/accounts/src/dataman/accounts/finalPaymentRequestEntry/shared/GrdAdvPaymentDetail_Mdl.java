package dataman.accounts.finalPaymentRequestEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class GrdAdvPaymentDetail_Mdl extends KeyField implements Serializable, IsSerializable {

    private String no;
    private String mode;
    private String bank;
    private String chqDate;
    private String chqNo;
    private String amt;
    private String tds;
    private String tdsDeduction;
    private String netPay;
    private String approvedBy;
    private String advancePaidSettel;
    private String advancePaidUnSettel;

    public GrdAdvPaymentDetail_Mdl() {

    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getChqDate() {
        return chqDate;
    }

    public void setChqDate(String chqDate) {
        this.chqDate = chqDate;
    }

    public String getChqNo() {
        return chqNo;
    }

    public void setChqNo(String chqNo) {
        this.chqNo = chqNo;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getTds() {
        return tds;
    }

    public void setTds(String tds) {
        this.tds = tds;
    }

    public String getTdsDeduction() {
        return tdsDeduction;
    }

    public void setTdsDeduction(String tdsDeduction) {
        this.tdsDeduction = tdsDeduction;
    }

    public String getNetPay() {
        return netPay;
    }

    public void setNetPay(String netPay) {
        this.netPay = netPay;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getAdvancePaidSettel() {
        return advancePaidSettel;
    }

    public void setAdvancePaidSettel(String advancePaidSettel) {
        this.advancePaidSettel = advancePaidSettel;
    }

    public String getAdvancePaidUnSettel() {
        return advancePaidUnSettel;
    }

    public void setAdvancePaidUnSettel(String advancePaidUnSettel) {
        this.advancePaidUnSettel = advancePaidUnSettel;
    }

}
