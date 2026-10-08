package aarogya.selfToolkit.walletSystem.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class CardRegistration_Mdl extends KeyField implements Serializable, IsSerializable {

    private RecId recIdReg = new RecId();
    private String voucherType;
    private String expiryDate;
    private String voucherDate;
    private String remark;
    private String cardNo, physicalId;
    private double balance;
    private PersonDetail_Mdl personMdl;
    private TxtHCardType_Mdl cardMdl;

    public CardRegistration_Mdl() {
    }

    public RecId getRecIdReg() {
        return recIdReg;
    }

    public void setRecIdReg(RecId recIdReg) {
        this.recIdReg = recIdReg;
    }

    public String getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(String voucherType) {
        this.voucherType = voucherType;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getPhysicalId() {
        return physicalId;
    }

    public void setPhysicalId(String physicalId) {
        this.physicalId = physicalId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public PersonDetail_Mdl getPersonMdl() {
        return personMdl;
    }

    public void setPersonMdl(PersonDetail_Mdl personMdl) {
        this.personMdl = personMdl;
    }

    public TxtHCardType_Mdl getCardMdl() {
        return cardMdl;
    }

    public void setCardMdl(TxtHCardType_Mdl cardMdl) {
        this.cardMdl = cardMdl;
    }
}
