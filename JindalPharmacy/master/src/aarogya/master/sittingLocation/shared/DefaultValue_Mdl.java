package aarogya.master.sittingLocation.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class DefaultValue_Mdl implements Serializable, IsSerializable {

    private String consultantCode, consultantName;
    private String hkLocationCode, hkLocationName;
    private String deviceCode, deviceBlock;

    public DefaultValue_Mdl() {
    }

    public String getConsultantCode() {
        return consultantCode;
    }

    public void setConsultantCode(String consultantCode) {
        this.consultantCode = consultantCode;
    }

    public String getConsultantName() {
        return consultantName;
    }

    public void setConsultantName(String consultantName) {
        this.consultantName = consultantName;
    }

    public String getHkLocationCode() {
        return hkLocationCode;
    }

    public void setHkLocationCode(String hkLocationCode) {
        this.hkLocationCode = hkLocationCode;
    }

    public String getHkLocationName() {
        return hkLocationName;
    }

    public void setHkLocationName(String hkLocationName) {
        this.hkLocationName = hkLocationName;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getDeviceBlock() {
        return deviceBlock;
    }

    public void setDeviceBlock(String deviceBlock) {
        this.deviceBlock = deviceBlock;
    }
}
