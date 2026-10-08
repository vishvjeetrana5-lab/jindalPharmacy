package aarogya.inventory.gateInSearch.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class GateInSearchSettingValue_Mdl implements Serializable, IsSerializable {

    private String pendding;

    public GateInSearchSettingValue_Mdl() {
    }

    public String getPendding() {
        return pendding;
    }

    public void setPendding(String pendding) {
        this.pendding = pendding;
    }
}
