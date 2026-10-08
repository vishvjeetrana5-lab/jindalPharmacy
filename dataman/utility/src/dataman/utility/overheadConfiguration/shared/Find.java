package dataman.utility.overheadConfiguration.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Find extends KeyField implements Serializable,IsSerializable{

    private String overheadName;

    public Find() {
    }

    public String getOverheadName() {
        return overheadName;
    }

    public void setOverheadName(String overheadName) {
        this.overheadName = overheadName;
    }

}
