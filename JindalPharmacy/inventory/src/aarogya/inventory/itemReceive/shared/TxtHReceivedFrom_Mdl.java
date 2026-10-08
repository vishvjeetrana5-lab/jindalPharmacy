package aarogya.inventory.itemReceive.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHReceivedFrom_Mdl extends KeyField implements Serializable, IsSerializable {

    public TxtHReceivedFrom_Mdl() {
    }
    private String cityName;
    private String manualCode;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getManualCode() {
        return manualCode;
    }

    public void setManualCode(String manualCode) {
        this.manualCode = manualCode;
    }

}
