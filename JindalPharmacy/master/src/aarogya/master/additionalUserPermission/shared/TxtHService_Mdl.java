package aarogya.master.additionalUserPermission.shared;

import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHService_Mdl extends KeyField implements Serializable {

    private String serviceCatCode;
    private String serviceCatName;

    public TxtHService_Mdl() {
    }

    public String getServiceCatCode() {
        return serviceCatCode;
    }

    public void setServiceCatCode(String serviceCatCode) {
        this.serviceCatCode = serviceCatCode;
    }

    public String getServiceCatName() {
        return serviceCatName;
    }

    public void setServiceCatName(String serviceCatName) {
        this.serviceCatName = serviceCatName;
    }

}
