package aarogya.inventory.purchaseOrderDisplay.shared;

import aarogya.inventory.purchaseOrder.shared.*;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHSupplier_Mdl extends KeyField implements Serializable, IsSerializable {

    private String manualCode;
    private String cityName;
    private String guiConfigCode;

    public TxtHSupplier_Mdl() {
    }

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

    public String getGuiConfigCode() {
        return guiConfigCode;
    }

    public void setGuiConfigCode(String guiConfigCode) {
        this.guiConfigCode = guiConfigCode;
    }
    
}
