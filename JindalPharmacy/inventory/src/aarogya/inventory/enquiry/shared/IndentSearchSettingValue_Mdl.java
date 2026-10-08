package aarogya.inventory.enquiry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class IndentSearchSettingValue_Mdl implements Serializable,IsSerializable{

    private String pendding;

    public IndentSearchSettingValue_Mdl() {
    }

    public String getPendding() {
        return pendding;
    }

    public void setPendding(String pendding) {
        this.pendding = pendding;
    }
}
