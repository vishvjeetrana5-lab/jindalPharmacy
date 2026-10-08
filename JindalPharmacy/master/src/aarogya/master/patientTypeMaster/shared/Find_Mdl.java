package aarogya.master.patientTypeMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;

import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String active;
    private String className;
//    private String shortName;

    public Find_Mdl() {
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

//    public String getShortName() {
//        return shortName;
//    }
//
//    public void setShortName(String shortName) {
//        this.shortName = shortName;
//    }

}
