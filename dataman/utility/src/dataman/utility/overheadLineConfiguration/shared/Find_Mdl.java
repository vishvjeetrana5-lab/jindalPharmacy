package dataman.utility.overheadLineConfiguration.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable,IsSerializable{

    private String overheadName;

    public Find_Mdl() {
    }

    public String getOverheadName() {
        return overheadName;
    }

    public void setOverheadName(String overheadName) {
        this.overheadName = overheadName;
    }

}
