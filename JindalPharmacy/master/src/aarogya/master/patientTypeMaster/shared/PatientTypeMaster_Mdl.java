package aarogya.master.patientTypeMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class PatientTypeMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    private String className;
    private String classCode;
    private String activeCode;
    private String activeName;
//    private String shortName;

    public PatientTypeMaster_Mdl() {
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getActiveCode() {
        return activeCode;
    }

    public void setActiveCode(String activeCode) {
        this.activeCode = activeCode;
    }

    public String getActiveName() {
        return activeName;
    }

    public void setActiveName(String activeName) {
        this.activeName = activeName;
    }

//    public String getShortName() {
//        return shortName;
//    }
//
//    public void setShortName(String shortName) {
//        this.shortName = shortName;
//    }

}
