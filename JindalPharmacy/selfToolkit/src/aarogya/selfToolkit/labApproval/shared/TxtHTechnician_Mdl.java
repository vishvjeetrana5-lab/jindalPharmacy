package aarogya.selfToolkit.labApproval.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

    public class TxtHTechnician_Mdl extends KeyField implements Serializable, IsSerializable {

    private String manualCode;

    public TxtHTechnician_Mdl() {
    }

    public String getManualCode() {
        return manualCode;
    }

    public void setManualCode(String manualCode) {
        this.manualCode = manualCode;
    }

}


