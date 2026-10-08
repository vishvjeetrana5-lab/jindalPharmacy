package aarogya.inventory.divisionMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHAccountGroup_Mdl extends KeyField implements Serializable, IsSerializable {

    private String natureCode;
    private String natureName;

    public TxtHAccountGroup_Mdl() {
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

}
