package aarogya.localSetting.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdLabTestCodification_Mdl extends KeyField implements Serializable, IsSerializable {

    private String machineTestName;
    private String manualName;

    public GrdLabTestCodification_Mdl() {
    }

    public String getMachineTestName() {
        return machineTestName;
    }

    public void setMachineTestName(String machineTestName) {
        this.machineTestName = machineTestName;
    }

    public String getManualName() {
        return manualName;
    }

    public void setManualName(String manualName) {
        this.manualName = manualName;
    }

}
