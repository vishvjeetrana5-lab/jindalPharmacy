package dataman.accounts.finalPaymentRequestEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class GrdFinalPaymentDetail_Mdl extends KeyField implements Serializable, IsSerializable {

    private String no;
    private String mode;
    private String bank;
    private String chqDate;
    private String chqNo;
    private String amt;
    private String tds;
    private String tdsDeduction;
    private String netPay;

    public GrdFinalPaymentDetail_Mdl() {

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

}
