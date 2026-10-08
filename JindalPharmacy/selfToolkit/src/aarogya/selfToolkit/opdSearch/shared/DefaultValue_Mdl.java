package aarogya.selfToolkit.opdSearch.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class DefaultValue_Mdl extends KeyField implements Serializable, IsSerializable {

    private String cancelDaysOPD;
    private String serviceDaysOPD;

    public DefaultValue_Mdl() {
    }

    public String getCancelDaysOPD() {
        return cancelDaysOPD;
    }

    public void setCancelDaysOPD(String cancelDaysOPD) {
        this.cancelDaysOPD = cancelDaysOPD;
    }

    public String getServiceDaysOPD() {
        return serviceDaysOPD;
    }

    public void setServiceDaysOPD(String serviceDaysOPD) {
        this.serviceDaysOPD = serviceDaysOPD;
    }
}
