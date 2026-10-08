package aarogya.master.disabilityMaster.shared;

import aarogya.master.disabilityMaster.shared.*;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;

public class DisabilityMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    private String manualCode;

    public DisabilityMaster_Mdl() {
    }

    public String getManualCode() {
        return manualCode;
    }

    public void setManualCode(String manualCode) {
        this.manualCode = manualCode;
    }

}
