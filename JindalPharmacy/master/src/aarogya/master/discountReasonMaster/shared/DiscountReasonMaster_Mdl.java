package aarogya.master.discountReasonMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class DiscountReasonMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    private String discountAccountName;
    private String discountAccountCode;

    public DiscountReasonMaster_Mdl() {
    }

    public String getDiscountAccountName() {
        return discountAccountName;
    }

    public void setDiscountAccountName(String discountAccountName) {
        this.discountAccountName = discountAccountName;
    }

    public String getDiscountAccountCode() {
        return discountAccountCode;
    }

    public void setDiscountAccountCode(String discountAccountCode) {
        this.discountAccountCode = discountAccountCode;
    }

}
