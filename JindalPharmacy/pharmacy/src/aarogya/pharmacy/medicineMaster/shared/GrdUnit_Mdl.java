package aarogya.pharmacy.medicineMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdUnit_Mdl extends KeyField implements Serializable, IsSerializable {

//    private String secondaryUnit;
    private String conversionRate;

    public GrdUnit_Mdl() {
    }

    public String getConversionRate() {
        return conversionRate;
    }

    public void setConversionRate(String conversionRate) {
        this.conversionRate = conversionRate;
    }

}
