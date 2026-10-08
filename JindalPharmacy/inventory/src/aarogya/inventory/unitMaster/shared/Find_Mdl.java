package aarogya.inventory.unitMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String decimalPlaces;
    private String gstUnitName;

    public Find_Mdl() {
    }

    public String getDecimalPlaces() {
        return decimalPlaces;
    }

    public void setDecimalPlaces(String decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

    public String getGstUnitName() {
        return gstUnitName;
    }

    public void setGstUnitName(String gstUnitName) {
        this.gstUnitName = gstUnitName;
    }

}
