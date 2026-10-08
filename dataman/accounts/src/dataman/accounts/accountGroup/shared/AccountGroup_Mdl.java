package dataman.accounts.accountGroup.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class AccountGroup_Mdl extends KeyField implements Serializable, IsSerializable {

    private String sysGroup;
    private String contraGroupName;
    private String groupUnder;
    private String groupUnderName;
    private String groupNature;
    private String groupNatureName;
    private String natureCode;
    private String natureName;

    public AccountGroup_Mdl() {
    }

    public String getSysGroup() {
        return sysGroup;
    }

    public void setSysGroup(String sysGroup) {
        this.sysGroup = sysGroup;
    }

    public String getGroupUnder() {
        return groupUnder;
    }

    public void setGroupUnder(String groupUnder) {
        this.groupUnder = groupUnder;
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

    public String getGroupUnderName() {
        return groupUnderName;
    }

    public void setGroupUnderName(String groupUnderName) {
        this.groupUnderName = groupUnderName;
    }

    public String getGroupNature() {
        return groupNature;
    }

    public void setGroupNature(String groupNature) {
        this.groupNature = groupNature;
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
