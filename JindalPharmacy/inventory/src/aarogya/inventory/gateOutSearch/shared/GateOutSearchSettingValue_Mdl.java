package aarogya.inventory.gateOutSearch.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class GateOutSearchSettingValue_Mdl implements Serializable, IsSerializable {

    private String pendding;

    public GateOutSearchSettingValue_Mdl() {
    }

    public String getPendding() {
        return pendding;
    }

    public void setPendding(String pendding) {
        this.pendding = pendding;
    }
}
