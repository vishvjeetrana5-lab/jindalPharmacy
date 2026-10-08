package dataman.accounts.acGroupDetailCompare.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class AcGroupDetailCompare_Mdl extends KeyField implements Serializable, IsSerializable {

    private String debit;
    private String credit;
    private String subCode;
    private String subName;
    private String closing;
    private String closingDrCr;
    private String lstDebit;
    private String lstCredit;
    private String lstClosing;
    private String lstClosingDrCr;

    public AcGroupDetailCompare_Mdl() {
    }

    public String getClosing() {
        return closing;
    }

    public void setClosing(String closing) {
        this.closing = closing;
    }

    public String getClosingDrCr() {
        return closingDrCr;
    }

    public void setClosingDrCr(String closingDrCr) {
        this.closingDrCr = closingDrCr;
    }

    public String getDebit() {
        return debit;
    }

    public void setDebit(String debit) {
        this.debit = debit;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getLstDebit() {
        return lstDebit;
    }

    public void setLstDebit(String lstDebit) {
        this.lstDebit = lstDebit;
    }

    public String getLstCredit() {
        return lstCredit;
    }

    public void setLstCredit(String lstCredit) {
        this.lstCredit = lstCredit;
    }

    public String getLstClosing() {
        return lstClosing;
    }

    public void setLstClosing(String lstClosing) {
        this.lstClosing = lstClosing;
    }

    public String getLstClosingDrCr() {
        return lstClosingDrCr;
    }

    public void setLstClosingDrCr(String lstClosingDrCr) {
        this.lstClosingDrCr = lstClosingDrCr;
    }
    
}
