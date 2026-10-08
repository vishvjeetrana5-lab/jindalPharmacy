package aarogya.pharmacy.organizationBillingOnRateMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String MedicineSaleOnTag;
    private String MedicineSaleOnText;

    public Find_Mdl() {
    }

    public String getMedicineSaleOnTag() {
        return MedicineSaleOnTag;
    }

    public void setMedicineSaleOnTag(String MedicineSaleOnTag) {
        this.MedicineSaleOnTag = MedicineSaleOnTag;
    }

    public String getMedicineSaleOnText() {
        return MedicineSaleOnText;
    }

    public void setMedicineSaleOnText(String MedicineSaleOnText) {
        this.MedicineSaleOnText = MedicineSaleOnText;
    }

}
