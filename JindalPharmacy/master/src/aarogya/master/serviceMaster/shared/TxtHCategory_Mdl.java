package aarogya.master.serviceMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHCategory_Mdl extends KeyField implements Serializable, IsSerializable {

    private String its_Name;
    private String serviceAcName;

    public TxtHCategory_Mdl() {
    }

    public String getIts_Name() {
        return its_Name;
    }

    public void setIts_Name(String its_Name) {
        this.its_Name = its_Name;
    }

    public String getServiceAcName() {
        return serviceAcName;
    }

    public void setServiceAcName(String serviceAcName) {
        this.serviceAcName = serviceAcName;
    }

}
