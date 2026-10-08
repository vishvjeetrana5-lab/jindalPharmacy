package aarogya.selfToolkit.medicineAdvise.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHCompany_Mdl extends KeyField implements Serializable, IsSerializable {

    private String shortName;
    private String purchaseDisPer;

    public TxtHCompany_Mdl() {
    }

    public String getPurchaseDisPer() {
        return purchaseDisPer;
    }

    public void setPurchaseDisPer(String purchaseDisPer) {
        this.purchaseDisPer = purchaseDisPer;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}
