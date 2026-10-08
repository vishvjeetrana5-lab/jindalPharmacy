package aarogya.master.organizationServiceMapping.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdOrganizationDetail_Mdl extends KeyField implements Serializable, IsSerializable {

    private String applicableFrom;
    private String applicableTo;

    public GrdOrganizationDetail_Mdl() {
    }

    public String getApplicableFrom() {
        return applicableFrom;
    }

    public void setApplicableFrom(String applicableFrom) {
        this.applicableFrom = applicableFrom;
    }

    public String getApplicableTo() {
        return applicableTo;
    }

    public void setApplicableTo(String applicableTo) {
        this.applicableTo = applicableTo;
    }

}
