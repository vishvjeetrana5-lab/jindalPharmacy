package dataman.dmToolkit.overHeadLine.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class AccountHelp_Mdl extends KeyField implements Serializable, IsSerializable {

    String manualCode;

    public AccountHelp_Mdl() {
    }

    public String getManualCode() {
        return manualCode;
    }

    public void setManualCode(String manualCode) {
        this.manualCode = manualCode;
    }
}
