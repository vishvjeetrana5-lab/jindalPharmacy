package aarogya.inventory.projectMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String projectHeadName;
    private String projectAcName;
    private String approvedAmount;

    public Find_Mdl() {
    }

    public String getProjectHeadName() {
        return projectHeadName;
    }

    public void setProjectHeadName(String projectHeadName) {
        this.projectHeadName = projectHeadName;
    }

    public String getProjectAcName() {
        return projectAcName;
    }

    public void setProjectAcName(String projectAcName) {
        this.projectAcName = projectAcName;
    }

    public String getApprovedAmount() {
        return approvedAmount;
    }

    public void setApprovedAmount(String approvedAmount) {
        this.approvedAmount = approvedAmount;
    }

}
