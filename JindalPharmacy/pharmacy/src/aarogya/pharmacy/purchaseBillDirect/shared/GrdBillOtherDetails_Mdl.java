package aarogya.pharmacy.purchaseBillDirect.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class GrdBillOtherDetails_Mdl extends KeyField implements Serializable, IsSerializable {

    private String fromLocation;
    private String toLocation;
    private String dateTime;
    private String remark;
    private String billNo;
    private String cheqNo;
    private String bank;
    private String amount;
    private String payType;
    private String finalPayReq;

    public GrdBillOtherDetails_Mdl() {
    }

    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getCheqNo() {
        return cheqNo;
    }

    public void setCheqNo(String cheqNo) {
        this.cheqNo = cheqNo;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getFinalPayReq() {
        return finalPayReq;
    }

    public void setFinalPayReq(String finalPayReq) {
        this.finalPayReq = finalPayReq;
    }

}
