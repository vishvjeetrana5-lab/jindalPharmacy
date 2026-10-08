package aarogya.selfToolkit.uhidPopup.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class Layout_Mdl implements Serializable, IsSerializable {

    private boolean isMandatory;
    private boolean isVisible;
    private int serial;
    private String code;
    private String nature;

    public Layout_Mdl() {
    }

    public boolean isIsMandatory() {
        return isMandatory;
    }

    public void setIsMandatory(boolean isMandatory) {
        this.isMandatory = isMandatory;
    }

    public boolean isIsVisible() {
        return isVisible;
    }

    public void setIsVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }
}
