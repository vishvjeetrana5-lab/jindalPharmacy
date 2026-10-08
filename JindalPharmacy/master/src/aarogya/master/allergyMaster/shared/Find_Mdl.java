package aarogya.master.allergyMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;

import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String active;
    private String nature;

    public Find_Mdl() {
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

}
