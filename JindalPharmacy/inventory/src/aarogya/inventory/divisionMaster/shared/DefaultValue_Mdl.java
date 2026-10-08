package aarogya.inventory.divisionMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class DefaultValue_Mdl implements Serializable, IsSerializable {

    private String acGroupDivisionCode;
    private String acGroupDivisionName;
    private boolean gstApplicable = false;

    public DefaultValue_Mdl() {
    }

    public String getAcGroupDivisionCode() {
        return acGroupDivisionCode;
    }

    public void setAcGroupDivisionCode(String acGroupDivisionCode) {
        this.acGroupDivisionCode = acGroupDivisionCode;
    }

    public String getAcGroupDivisionName() {
        return acGroupDivisionName;
    }

    public void setAcGroupDivisionName(String acGroupDivisionName) {
        this.acGroupDivisionName = acGroupDivisionName;
    }

    public boolean isGstApplicable() {
        return gstApplicable;
    }

    public void setGstApplicable(boolean gstApplicable) {
        this.gstApplicable = gstApplicable;
    }

}
