package aarogya.inventory.purchaseOrderDisplay.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class ApprovedVendors_Mdl extends KeyField implements Serializable, IsSerializable{
        private String v_SNo;
private String rate;
    public ApprovedVendors_Mdl() {
    }

    public String getV_SNo() {
        return v_SNo;
    }

    public void setV_SNo(String v_SNo) {
        this.v_SNo = v_SNo;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
    
}
