package dataman.accounts.accountGroup.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String contraGroupName;
    private String groupUnderName;
    private String groupNatureName;
    private String nature;
    private String sysGroup;

    public Find_Mdl() {
    }

    public String getSysGroup() {
        return sysGroup;
    }

    public void setSysGroup(String sysGroup) {
        this.sysGroup = sysGroup;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getGroupUnderName() {
        return groupUnderName;
    }

    public void setGroupUnderName(String groupUnderName) {
        this.groupUnderName = groupUnderName;
    }

    public String getGroupNatureName() {
        return groupNatureName;
    }

    public void setGroupNatureName(String groupNatureName) {
        this.groupNatureName = groupNatureName;
    }

    public String getContraGroupName() {
        return contraGroupName;
    }

    public void setContraGroupName(String contraGroupName) {
        this.contraGroupName = contraGroupName;
    }
}
