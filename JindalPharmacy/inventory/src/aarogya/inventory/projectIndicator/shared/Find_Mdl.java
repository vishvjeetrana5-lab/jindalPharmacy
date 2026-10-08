package aarogya.inventory.projectIndicator.shared;

import aarogya.inventory.projectIndicator.shared.*;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String vrDate;
    private String vrTypeDescription;

    public Find_Mdl() {
    }

    public String getVrDate() {
        return vrDate;
    }

    public void setVrDate(String vrDate) {
        this.vrDate = vrDate;
    }

    public String getVrTypeDescription() {
        return vrTypeDescription;
    }

    public void setVrTypeDescription(String vrTypeDescription) {
        this.vrTypeDescription = vrTypeDescription;
    }

}
