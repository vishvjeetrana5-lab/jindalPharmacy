package aarogya.inventory.wipToStockConversion.shared;

import aarogya.inventory.wipToStockConversion.shared.*;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHProject_Mdl extends KeyField implements Serializable, IsSerializable {

    public TxtHProject_Mdl() {
    }
    private String manualCode;
    private String projectAc;
    private String projectAcName;

    public String getManualCode() {
        return manualCode;
    }

    public void setManualCode(String manualCode) {
        this.manualCode = manualCode;
    }

    public String getProjectAc() {
        return projectAc;
    }

    public void setProjectAc(String projectAc) {
        this.projectAc = projectAc;
    }

    public String getProjectAcName() {
        return projectAcName;
    }

    public void setProjectAcName(String projectAcName) {
        this.projectAcName = projectAcName;
    }

}
