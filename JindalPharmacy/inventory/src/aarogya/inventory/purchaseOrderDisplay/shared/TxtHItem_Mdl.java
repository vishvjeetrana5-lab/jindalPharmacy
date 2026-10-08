package aarogya.inventory.purchaseOrderDisplay.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHItem_Mdl extends KeyField implements Serializable, IsSerializable {

    private String manualCode;

    public TxtHItem_Mdl() {
    }

    public String getManualCode() {
        return manualCode;
    }

    public void setManualCode(String manualCode) {
        this.manualCode = manualCode;
    }

}
