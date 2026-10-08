package aarogya.selfToolkit.serviceGrid.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class DefaultValue_Mdl implements Serializable, IsSerializable {

    private String ac_RoundOffGST;

    public DefaultValue_Mdl() {
    }

    public String getAc_RoundOffGST() {
        return ac_RoundOffGST;
    }

    public void setAc_RoundOffGST(String ac_RoundOffGST) {
        this.ac_RoundOffGST = ac_RoundOffGST;
    }
}
