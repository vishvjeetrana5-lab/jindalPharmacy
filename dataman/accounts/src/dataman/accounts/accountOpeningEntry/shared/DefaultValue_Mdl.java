package dataman.accounts.accountOpeningEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class DefaultValue_Mdl extends KeyField implements Serializable, IsSerializable {

    public DefaultValue_Mdl() {
    }

    private String currentDate;
    private String bankNatureCode;

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }
    public String getBankNatureCode() {
        return bankNatureCode;
    }

    public void setBankNatureCode(String bankNatureCode) {
        this.bankNatureCode = bankNatureCode;
    }

}
