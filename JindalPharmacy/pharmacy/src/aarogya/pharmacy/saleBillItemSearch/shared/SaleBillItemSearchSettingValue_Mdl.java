package aarogya.pharmacy.requisitionSearch.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class SaleBillItemSearchSettingValue_Mdl implements Serializable, IsSerializable {

    private String pendding;

    public SaleBillItemSearchSettingValue_Mdl() {
    }

    public String getPendding() {
        return pendding;
    }

    public void setPendding(String pendding) {
        this.pendding = pendding;
    }
}
