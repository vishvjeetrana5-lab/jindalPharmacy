package aarogya.inventory.itemGroupMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHItemType_Mdl extends KeyField implements Serializable, IsSerializable {

    private String shortname;

    public TxtHItemType_Mdl() {
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

}
