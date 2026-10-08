package aarogya.master.prescriptionCustomItemMast.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class PrescriptionCustomItemMast_Mdl extends KeyField implements Serializable, IsSerializable {

    private String regionalLanguageName;
    private String shortName;
    private String natureCode;
    private String natureName;
    private String groupCode;
    private String groupName;
    private String prnSrNo;
    private String graceMinute;

    public PrescriptionCustomItemMast_Mdl() {
    }

    public String getRegionalLanguageName() {
        return regionalLanguageName;
    }

    public void setRegionalLanguageName(String regionalLanguageName) {
        this.regionalLanguageName = regionalLanguageName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getNatureCode() {
        return natureCode;
    }

    public void setNatureCode(String natureCode) {
        this.natureCode = natureCode;
    }

    public String getNatureName() {
        return natureName;
    }

    public void setNatureName(String natureName) {
        this.natureName = natureName;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getPrnSrNo() {
        return prnSrNo;
    }

    public void setPrnSrNo(String prnSrNo) {
        this.prnSrNo = prnSrNo;
    }

    public String getGraceMinute() {
        return graceMinute;
    }

    public void setGraceMinute(String graceMinute) {
        this.graceMinute = graceMinute;
    }

}
