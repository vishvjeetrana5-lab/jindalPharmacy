package aarogya.inventory.itemIssue.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class TxtHProject_Mdl  extends KeyField implements Serializable, IsSerializable {
    private String projectAcName;
    private String projectAcCode;
    public TxtHProject_Mdl(){
        
    }

    public String getProjectAcName() {
        return projectAcName;
    }

    public void setProjectAcName(String projectAcName) {
        this.projectAcName = projectAcName;
    }

    public String getProjectAcCode() {
        return projectAcCode;
    }

    public void setProjectAcCode(String projectAcCode) {
        this.projectAcCode = projectAcCode;
    }
    
}
