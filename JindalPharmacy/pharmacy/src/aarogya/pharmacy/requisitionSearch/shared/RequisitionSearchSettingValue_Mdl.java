package aarogya.pharmacy.requisitionSearch.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class RequisitionSearchSettingValue_Mdl implements Serializable, IsSerializable {

    private String pendding;

    public RequisitionSearchSettingValue_Mdl() {
    }

    public String getPendding() {
        return pendding;
    }

    public void setPendding(String pendding) {
        this.pendding = pendding;
    }
}
