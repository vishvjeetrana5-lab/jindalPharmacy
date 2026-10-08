package aarogya.pharmacy.medicineMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHMedicineType_Mdl extends KeyField implements Serializable, IsSerializable {

    private String shortname;

    public TxtHMedicineType_Mdl() {
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

}
