package aarogya.inventory.itemCategoryMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String shortname;

    public Find_Mdl() {
    }

    public String getShortName() {
        return shortname;
    }

    public void setShortName(String shortname) {
        this.shortname = shortname;
    }

}
