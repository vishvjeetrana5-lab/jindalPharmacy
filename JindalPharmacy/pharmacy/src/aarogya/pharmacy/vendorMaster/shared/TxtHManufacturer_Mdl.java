package aarogya.pharmacy.vendorMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHManufacturer_Mdl extends KeyField implements Serializable, IsSerializable {

    private String CityName;
    private String ManualCode;

    public TxtHManufacturer_Mdl() {
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
