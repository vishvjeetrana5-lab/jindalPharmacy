package aarogya.inventory.unitMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class UnitMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    private String decimalPlaces;
    private String gstUnitCode;
    private String gstUnitName;

    public UnitMaster_Mdl() {
    }

    public String getDecimalPlaces() {
        return decimalPlaces;
    }

    public void setDecimalPlaces(String decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

    public String getGstUnitCode() {
        return gstUnitCode;
    }

    public void setGstUnitCode(String gstUnitCode) {
        this.gstUnitCode = gstUnitCode;
    }

    public String getGstUnitName() {
        return gstUnitName;
    }

    public void setGstUnitName(String gstUnitName) {
        this.gstUnitName = gstUnitName;
    }

}
