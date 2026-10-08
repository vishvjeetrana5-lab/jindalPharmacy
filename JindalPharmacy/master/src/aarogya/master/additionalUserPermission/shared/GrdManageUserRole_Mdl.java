package aarogya.master.additionalUserPermission.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdManageUserRole_Mdl extends KeyField implements Serializable, IsSerializable {

    private String description;
    private String roleName;

    public GrdManageUserRole_Mdl() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

}
