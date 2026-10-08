package aarogya.inventory.pendingPODisplay.shared;

import aarogya.inventory.purchaseOrder.shared.*;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHSupplier_Mdl extends KeyField implements Serializable, IsSerializable {

    private String manualCode;
    private String suppCity;
    private String cityName;

    public TxtHSupplier_Mdl() {
    }

    public String getManualCode() {
        return manualCode;
    }

    public void setManualCode(String manualCode) {
        this.manualCode = manualCode;
    }

    public String getSuppCity() {
        return suppCity;
    }

    public void setSuppCity(String suppCity) {
        this.suppCity = suppCity;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    
}
