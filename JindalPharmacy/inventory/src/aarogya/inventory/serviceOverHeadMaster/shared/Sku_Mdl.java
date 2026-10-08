package aarogya.inventory.serviceOverHeadMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Sku_Mdl extends KeyField implements IsSerializable, Serializable {

    public Sku_Mdl() {
    }

    private String allowedDecimalPlaces;

    public String getAllowedDecimalPlaces() {
        return allowedDecimalPlaces;
    }

    public void setAllowedDecimalPlaces(String allowedDecimalPlaces) {
        this.allowedDecimalPlaces = allowedDecimalPlaces;
    }

}
