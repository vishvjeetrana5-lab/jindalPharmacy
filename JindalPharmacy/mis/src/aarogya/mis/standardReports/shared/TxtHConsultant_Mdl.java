package aarogya.mis.standardReports.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHConsultant_Mdl extends KeyField implements Serializable, IsSerializable {

    private String nature;

    public TxtHConsultant_Mdl() {
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }
}
