package aarogya.pharmacy.procurementPlannerDisplay.shared;

import aarogya.pharmacy.procurementPlannerDisplay.shared.*;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHMedicine_Mdl extends KeyField implements Serializable, IsSerializable {

    private String manualCode;

    public TxtHMedicine_Mdl() {
    }

    public String getManualCode() {
        return manualCode;
    }

    public void setManualCode(String manualCode) {
        this.manualCode = manualCode;
    }

}
