package dataman.accounts.acGroupDetail.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class AcGroupDetail_Mdl extends KeyField implements Serializable, IsSerializable {

    private String debit;
    private String credit;
    private String subCode;
    private String subName;
    private String closing;
    private String closingDrCr;

    public AcGroupDetail_Mdl() {
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
}
