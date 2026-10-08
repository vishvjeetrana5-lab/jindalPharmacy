package aarogya.master.poolEarningDeductionEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String poolGrpName;
    private String salName;

    public Find_Mdl() {
    }

    public String getPoolGrpName() {
        return poolGrpName;
    }

    public void setPoolGrpName(String poolGrpName) {
        this.poolGrpName = poolGrpName;
    }

    public String getSalName() {
        return salName;
    }

    public void setSalName(String salName) {
        this.salName = salName;
    }

}
