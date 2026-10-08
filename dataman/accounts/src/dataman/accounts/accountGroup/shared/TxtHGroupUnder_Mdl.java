package dataman.accounts.accountGroup.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHGroupUnder_Mdl extends KeyField implements Serializable, IsSerializable {

    private String mainGroupCode;
    private String mainGroupName;
    private String natureCode;
    private String natureName;

    public TxtHGroupUnder_Mdl() {
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

    public String getMainGroupName() {
        return mainGroupName;
    }

    public void setMainGroupName(String mainGroupName) {
        this.mainGroupName = mainGroupName;
    }

    public String getMainGroupCode() {
        return mainGroupCode;
    }

    public void setMainGroupCode(String mainGroupCode) {
        this.mainGroupCode = mainGroupCode;
    }
}
