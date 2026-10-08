package aarogya.inventory.workBillDirect.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHUnit_Mdl extends KeyField implements Serializable, IsSerializable {

    /*
    sharedKeyFieldCode = unitCode AS code
    sharedKeyFieldName = unitName AS name
     */
    private String decimalPlaces;
    private String conversionRate;
    private String gstUnitCode;

    public TxtHUnit_Mdl() {
    }

    public String getConversionRate() {
        return conversionRate;
    }

    public void setConversionRate(String conversionRate) {
        this.conversionRate = conversionRate;
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
}
