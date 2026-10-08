package aarogya.pharmacy.wholeSalePaymentRecipt.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String voucherDate;
    private String chequeNo;
    private String chequeDate;
    private String partyName;
    private String tdsAc;
    private String depositeAC;
    private String amount;
    private String unSetteldAmt;
    private String setteldAmt;
    private String receivedAmt;
    private String tdsAmt;

    public String getVoucherDate() {
        return voucherDate;
    }

    public String getUnSetteldAmt() {
        return unSetteldAmt;
    }

    public void setUnSetteldAmt(String unSetteldAmt) {
        this.unSetteldAmt = unSetteldAmt;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public String getChequeNo() {
        return chequeNo;
    }

    public void setChequeNo(String chequeNo) {
        this.chequeNo = chequeNo;
    }

    public String getChequeDate() {
        return chequeDate;
    }

    public void setChequeDate(String chequeDate) {
        this.chequeDate = chequeDate;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    
    
      public String getTdsAc() {
        return tdsAc;
    }

    public void setTdsAc(String tdsAc) {
        this.tdsAc = tdsAc;
    }

    public String getDepositeAC() {
        return depositeAC;
    }

    public void setDepositeAC(String depositeAC) {
        this.depositeAC = depositeAC;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getReceivedAmt() {
        return receivedAmt;
    }

    public void setReceivedAmt(String receivedAmt) {
        this.receivedAmt = receivedAmt;
    }

    public String getTdsAmt() {
        return tdsAmt;
    }

    public void setTdsAmt(String tdsAmt) {
        this.tdsAmt = tdsAmt;
    }

    public String getSetteldAmt() {
        return setteldAmt;
    }

    public void setSetteldAmt(String setteldAmt) {
        this.setteldAmt = setteldAmt;
    }

}
