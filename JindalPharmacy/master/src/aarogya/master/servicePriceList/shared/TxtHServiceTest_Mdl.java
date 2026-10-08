package aarogya.master.servicePriceList.shared;

import aarogya.selfToolkit.stStatic.shared.STEnum.ServiceNature;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHServiceTest_Mdl extends KeyField implements Serializable, IsSerializable {

    private ServiceNature nature = ServiceNature.SERVICE;
//    private String serviceNatureCode;
    private String natureName;

    public TxtHServiceTest_Mdl() {
    }
//
//    public String getServiceNatureCode() {
//        return serviceNatureCode;
//    }
//
//    public void setServiceNatureCode(String serviceNatureCode) {
//        this.serviceNatureCode = serviceNatureCode;
//    }

    public String getNatureName() {
        return natureName;
    }

    public void setNatureName(String natureName) {
        this.natureName = natureName;
    }

    public ServiceNature getNature() {
        return nature;
    }

    public void setNature(ServiceNature nature) {
        this.nature = nature;
    }
}
