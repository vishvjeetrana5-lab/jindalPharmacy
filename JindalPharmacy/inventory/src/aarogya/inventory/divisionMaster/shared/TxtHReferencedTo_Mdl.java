package aarogya.inventory.divisionMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHReferencedTo_Mdl extends KeyField implements Serializable, IsSerializable {

    private String CityName;
    private String ManualCode;

    public TxtHReferencedTo_Mdl() {
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String CityName) {
        this.CityName = CityName;
    }

    public String getManualCode() {
        return ManualCode;
    }

    public void setManualCode(String ManualCode) {
        this.ManualCode = ManualCode;
    }
}
