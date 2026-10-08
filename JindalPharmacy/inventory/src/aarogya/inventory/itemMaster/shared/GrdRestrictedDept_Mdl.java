package aarogya.inventory.itemMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdRestrictedDept_Mdl extends KeyField implements Serializable,IsSerializable{

    private String v_SNo;

    public GrdRestrictedDept_Mdl() {
    }

    public String getV_SNo() {
        return v_SNo;
    }

    public void setV_SNo(String v_SNo) {
        this.v_SNo = v_SNo;
    }

}
