package aarogya.pharmacy.medicineSearch.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHHsn_Mdl extends KeyField implements Serializable, IsSerializable {

    private String hsn;

    public TxtHHsn_Mdl() {
    }

    public String getHsn() {
        return hsn;
    }

    public void setHsn(String hsn) {
        this.hsn = hsn;
    }

}
