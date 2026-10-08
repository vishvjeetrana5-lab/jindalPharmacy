package aarogya.master.additionalUserPermission.shared;

import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdService_Mdl extends KeyField implements Serializable {

    private String serviceName;
    private String serviceCode;

    public GrdService_Mdl() {
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

}
