package aarogya.pharmacy.medicineMakeMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String shortName;
    private String purchaseDiscount;

    public Find_Mdl() {
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getPurchaseDiscount() {
        return purchaseDiscount;
    }

    public void setPurchaseDiscount(String purchaseDiscount) {
        this.purchaseDiscount = purchaseDiscount;
    }

}
