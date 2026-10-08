package dataman.accounts.acLedgerDetail.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class GrdAcLedgerDetail_Mdl implements Serializable, IsSerializable {

    private String amtDr;
    private String amtCr;
    private String particularsTag;
    private String particularsText;

    private String drCr;
    private String balance;
    private String type;
    private String date;
    private String vrNo;
    private String vrNoTag;
    private String totalBalance;

    public GrdAcLedgerDetail_Mdl() {

    }

    public String getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(String totalBalance) {
        this.totalBalance = totalBalance;
    }

    public String getVrNo() {
        return vrNo;
    }

    public void setVrNo(String vrNo) {
        this.vrNo = vrNo;
    }

    public String getVrNoTag() {
        return vrNoTag;
    }

    public void setVrNoTag(String vrNoTag) {
        this.vrNoTag = vrNoTag;
    }

    public String getDrCr() {
        return drCr;
    }

    public void setDrCr(String drCr) {
        this.drCr = drCr;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
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

    public String getParticularsTag() {
        return particularsTag;
    }

    public void setParticularsTag(String particularsTag) {
        this.particularsTag = particularsTag;
    }

    public String getParticularsText() {
        return particularsText;
    }

    public void setParticularsText(String particularsText) {
        this.particularsText = particularsText;
    }

    public String getAmtDr() {
        return amtDr;
    }

    public void setAmtDr(String amtDr) {
        this.amtDr = amtDr;
    }

    public String getAmtCr() {
        return amtCr;
    }

    public void setAmtCr(String amtCr) {
        this.amtCr = amtCr;
    }

}
