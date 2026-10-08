package aarogya.pharmacy.medicineCompositionMaster.shared;

import aarogya.pharmacy.medicineCompositionMaster.shared.*;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String active;
    private String restrictGroupCode;
    private String restrictGroupName;

    public Find_Mdl() {
    }

    public String getRestrictGroupCode() {
        return restrictGroupCode;
    }

    public void setRestrictGroupCode(String restrictGroupCode) {
        this.restrictGroupCode = restrictGroupCode;
    }

    public String getRestrictGroupName() {
        return restrictGroupName;
    }

    public void setRestrictGroupName(String restrictGroupName) {
        this.restrictGroupName = restrictGroupName;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

}
