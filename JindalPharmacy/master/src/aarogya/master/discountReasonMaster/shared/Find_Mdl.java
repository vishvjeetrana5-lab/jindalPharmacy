package aarogya.master.discountReasonMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String discountAccountName;


    public Find_Mdl() {
    }

    public String getDiscountAccountName() {
        return discountAccountName;
    }

    public void setDiscountAccountName(String discountAccountName) {
        this.discountAccountName = discountAccountName;
    }

 

}
