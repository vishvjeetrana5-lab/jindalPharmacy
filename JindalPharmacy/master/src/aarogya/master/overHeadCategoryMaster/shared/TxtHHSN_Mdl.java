package aarogya.master.overHeadCategoryMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHHSN_Mdl extends KeyField implements Serializable, IsSerializable {

    private String hsn;

    public TxtHHSN_Mdl() {
    }

    public String getHsn() {
        return hsn;
    }

    public void setHsn(String hsn) {
        this.hsn = hsn;
    }

}
