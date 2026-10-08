package aarogya.master.organizationServiceMapping.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHServiceTest_Mdl extends KeyField implements Serializable, IsSerializable {

    private String orgServiceCode;
    private String serviceCode;
    private String serviceName;

    public TxtHServiceTest_Mdl() {
    }

    public String getOrgServiceCode() {
        return orgServiceCode;
    }

    public void setOrgServiceCode(String orgServiceCode) {
        this.orgServiceCode = orgServiceCode;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

}
