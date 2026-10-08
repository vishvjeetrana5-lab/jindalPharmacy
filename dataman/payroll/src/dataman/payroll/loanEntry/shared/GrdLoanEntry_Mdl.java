package dataman.payroll.loanEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class GrdLoanEntry_Mdl extends KeyField implements Serializable, IsSerializable {
private String emiDate;
private String emiAmount;
private String emiPrincipal;
private String interest;
private String adjustedDocID;

    public String getEmiDate() {
        return emiDate;
    }

    public void setEmiDate(String emiDate) {
        this.emiDate = emiDate;
    }

    public String getEmiAmount() {
        return emiAmount;
    }

    public void setEmiAmount(String emiAmount) {
        this.emiAmount = emiAmount;
    }

    public String getEmiPrincipal() {
        return emiPrincipal;
    }

    public void setEmiPrincipal(String emiPrincipal) {
        this.emiPrincipal = emiPrincipal;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getAdjustedDocID() {
        return adjustedDocID;
    }

    public void setAdjustedDocID(String adjustedDocID) {
        this.adjustedDocID = adjustedDocID;
    }
}
