package dataman.utility.overheadConfiguration.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHPostAC_Mdl extends KeyField implements Serializable,IsSerializable{

    private String manualCode;

    public TxtHPostAC_Mdl() {
    }

    public String getManualCode() {
        return manualCode;
    }

    public void setManualCode(String manualCode) {
        this.manualCode = manualCode;
    }
}
