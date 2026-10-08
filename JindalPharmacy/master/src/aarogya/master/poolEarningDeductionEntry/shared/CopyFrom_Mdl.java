package aarogya.master.poolEarningDeductionEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class CopyFrom_Mdl extends KeyField implements Serializable, IsSerializable {

    private String poolGrpName;
    private String poolGrpCode;
    private String salName;
    private String salCode;

    public CopyFrom_Mdl() {
    }

    public String getPoolGrpName() {
        return poolGrpName;
    }

    public void setPoolGrpName(String poolGrpName) {
        this.poolGrpName = poolGrpName;
    }

    public String getPoolGrpCode() {
        return poolGrpCode;
    }

    public void setPoolGrpCode(String poolGrpCode) {
        this.poolGrpCode = poolGrpCode;
    }

    public String getSalName() {
        return salName;
    }

    public void setSalName(String salName) {
        this.salName = salName;
    }

    public String getSalCode() {
        return salCode;
    }

    public void setSalCode(String salCode) {
        this.salCode = salCode;
    }

}
