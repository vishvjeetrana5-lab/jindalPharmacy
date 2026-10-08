package dataman.accounts.detailTrialBalance.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;

public class DetailTrialBalance_Mdl extends KeyField implements Serializable, IsSerializable {
//sharedKeyFieldCode= subCode
//sharedKeyFieldName= name

    private String debit;
    private String credit;
    private String opening;
    private String openingDrCr;
    private String closing;
    private String closingDrCr;

    public DetailTrialBalance_Mdl() {
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

    public String getOpening() {
        return opening;
    }

    public void setOpening(String opening) {
        this.opening = opening;
    }

    public String getOpeningDrCr() {
        return openingDrCr;
    }

    public void setOpeningDrCr(String openingDrCr) {
        this.openingDrCr = openingDrCr;
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
}
