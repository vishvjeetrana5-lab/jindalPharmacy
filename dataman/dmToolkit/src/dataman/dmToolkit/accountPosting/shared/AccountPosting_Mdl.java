package dataman.dmToolkit.accountPosting.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class AccountPosting_Mdl extends KeyField implements Serializable, IsSerializable {

    private String contraCode;
    private String contraText;
    private String amtDr;
    private String amtCr;

    public AccountPosting_Mdl() {
    }

    public String getContraCode() {
        return contraCode;
    }

    public void setContraCode(String contraCode) {
        this.contraCode = contraCode;
    }

    public String getContraText() {
        return contraText;
    }

    public void setContraText(String contraText) {
        this.contraText = contraText;
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
