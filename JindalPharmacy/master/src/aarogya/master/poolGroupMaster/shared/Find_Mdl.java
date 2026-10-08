package aarogya.master.poolGroupMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;

import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String active;
    

    public Find_Mdl() {
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

   

}
