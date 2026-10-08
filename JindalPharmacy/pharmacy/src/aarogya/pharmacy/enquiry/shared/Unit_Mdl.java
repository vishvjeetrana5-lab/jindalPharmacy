package aarogya.pharmacy.enquiry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Unit_Mdl extends KeyField implements Serializable, IsSerializable {

    private String decimalPlaces;

    public Unit_Mdl() {
    }

    public String getDecimalPlaces() {
        return decimalPlaces;
    }

    public void setDecimalPlaces(String decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }
}
