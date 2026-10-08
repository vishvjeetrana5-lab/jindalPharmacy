package aarogya.pharmacy.organizationBillingOnRateMaster.shared;

import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class OrganizationBillingOnRateMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    private String MedicineSaleOnTag;
    private String MedicineSaleOnText;
    private ModifyReason_Mdl modifyReason = new ModifyReason_Mdl();

    public OrganizationBillingOnRateMaster_Mdl() {
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

    public ModifyReason_Mdl getModifyReason() {
        return modifyReason;
    }

    public void setModifyReason(ModifyReason_Mdl modifyReason) {
        this.modifyReason = modifyReason;
    }

}
