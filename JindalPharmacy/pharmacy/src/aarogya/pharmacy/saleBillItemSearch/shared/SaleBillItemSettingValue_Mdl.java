package aarogya.pharmacy.saleBillItemSearch.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class SaleBillItemSettingValue_Mdl implements Serializable, IsSerializable {

    private String pendding;

    public SaleBillItemSettingValue_Mdl() {
    }

    public String getPendding() {
        return pendding;
    }

    public void setPendding(String pendding) {
        this.pendding = pendding;
    }
}
