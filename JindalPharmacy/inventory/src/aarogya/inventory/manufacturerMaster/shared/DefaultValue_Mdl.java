package aarogya.inventory.manufacturerMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class DefaultValue_Mdl implements Serializable, IsSerializable {

    private String acGroupManufacturerCode;
    private String acGroupManufacturerName;
    private boolean gstApplicable = false;

    public DefaultValue_Mdl() {
    }

    public String getAcGroupManufacturerCode() {
        return acGroupManufacturerCode;
    }

    public void setAcGroupManufacturerCode(String acGroupManufacturerCode) {
        this.acGroupManufacturerCode = acGroupManufacturerCode;
    }

    public String getAcGroupManufacturerName() {
        return acGroupManufacturerName;
    }

    public void setAcGroupManufacturerName(String acGroupManufacturerName) {
        this.acGroupManufacturerName = acGroupManufacturerName;
    }

    public boolean isGstApplicable() {
        return gstApplicable;
    }

    public void setGstApplicable(boolean gstApplicable) {
        this.gstApplicable = gstApplicable;
    }

}
