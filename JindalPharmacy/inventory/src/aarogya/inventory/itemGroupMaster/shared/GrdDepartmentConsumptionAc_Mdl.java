package aarogya.inventory.itemGroupMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class GrdDepartmentConsumptionAc_Mdl extends KeyField implements Serializable,IsSerializable{
    private String consumptionAcCode;
    private String consumptionAcName;

    public String getConsumptionAcCode() {
        return consumptionAcCode;
    }

    public void setConsumptionAcCode(String consumptionAcCode) {
        this.consumptionAcCode = consumptionAcCode;
    }

    public String getConsumptionAcName() {
        return consumptionAcName;
    }

    public void setConsumptionAcName(String consumptionAcName) {
        this.consumptionAcName = consumptionAcName;
    }
    
    
}
