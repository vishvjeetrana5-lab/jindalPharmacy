package dataman.accounts.billHandle.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdBillDetail_Mdl extends KeyField implements Serializable, IsSerializable {

    private String type;
    private String vSno;
    private String voucherNo;
    private String date;
    private String partyBillNo;
    private String partyDate;
    private String partyName;
    private String netAmt;
    private String paymentDueDt;
    private String lastStatusDt;
    private String remark;
    private String calendar;
    private String isStatus;
    private String lastLoc;
    private boolean check;
    public GrdBillDetail_Mdl() {
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }


    public String getLastLoc() {
        return lastLoc;
    }

    public void setLastLoc(String lastLoc) {
        this.lastLoc = lastLoc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getNetAmt() {
        return netAmt;
    }

    public void setNetAmt(String netAmt) {
        this.netAmt = netAmt;
    }

    public String getPaymentDueDt() {
        return paymentDueDt;
    }

    public void setPaymentDueDt(String paymentDueDt) {
        this.paymentDueDt = paymentDueDt;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLastStatusDt() {
        return lastStatusDt;
    }

    public void setLastStatusDt(String lastStatusDt) {
        this.lastStatusDt = lastStatusDt;
    }

    public String getvSno() {
        return vSno;
    }

    public void setvSno(String vSno) {
        this.vSno = vSno;
    }

    public String getPartyBillNo() {
        return partyBillNo;
    }

    public void setPartyBillNo(String partyBillNo) {
        this.partyBillNo = partyBillNo;
    }

    public String getPartyDate() {
        return partyDate;
    }

    public void setPartyDate(String partyDate) {
        this.partyDate = partyDate;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getCalendar() {
        return calendar;
    }

    public void setCalendar(String calendar) {
        this.calendar = calendar;
    }

    public String getIsStatus() {
        return isStatus;
    }

    public void setIsStatus(String isStatus) {
        this.isStatus = isStatus;
    }

}
