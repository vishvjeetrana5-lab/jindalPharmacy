package aarogya.inventory.purchaseOrderApproval.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;

public class TxtHPurchaseOrder_Mdl extends KeyField implements Serializable, IsSerializable {

    private String poType;
    private String poDate;

    public TxtHPurchaseOrder_Mdl() {
    }

    public String getPoType() {
        return poType;
    }

    public void setPoType(String poType) {
        this.poType = poType;
    }

    public String getPoDate() {
        return poDate;
    }

    public void setPoDate(String poDate) {
        this.poDate = poDate;
    }

}
