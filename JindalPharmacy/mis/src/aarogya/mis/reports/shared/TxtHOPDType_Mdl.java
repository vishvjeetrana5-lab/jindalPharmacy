package aarogya.mis.reports.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHOPDType_Mdl extends KeyField implements Serializable,IsSerializable{

    private String description;

    public TxtHOPDType_Mdl() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
