package aarogya.inventory.grnSearch.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class SettingValue_Mdl implements Serializable, IsSerializable {

    private String pendding;

    public SettingValue_Mdl() {
    }

    public String getPendding() {
        return pendding;
    }

    public void setPendding(String pendding) {
        this.pendding = pendding;
    }
}
