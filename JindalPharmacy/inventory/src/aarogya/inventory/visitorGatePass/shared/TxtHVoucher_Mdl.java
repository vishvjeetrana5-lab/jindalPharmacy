package aarogya.inventory.visitorGatePass.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class TxtHVoucher_Mdl extends KeyField implements Serializable, IsSerializable {

    private String shortName;

    public TxtHVoucher_Mdl() {
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

}
