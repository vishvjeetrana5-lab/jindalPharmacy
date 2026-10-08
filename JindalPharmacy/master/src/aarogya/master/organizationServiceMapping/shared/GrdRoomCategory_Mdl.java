package aarogya.master.organizationServiceMapping.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdRoomCategory_Mdl extends KeyField implements Serializable, IsSerializable {

    private String ratePercent;

    public GrdRoomCategory_Mdl() {
    }

    public String getRatePercent() {
        return ratePercent;
    }

    public void setRatePercent(String ratePercent) {
        this.ratePercent = ratePercent;
    }

}
