package aarogya.master.sittingLocation.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class SittingLocation_Mdl extends KeyField implements Serializable, IsSerializable {

    private String locationTag, locationText;
    private String deviceCode, deviceBlock;

    public SittingLocation_Mdl() {
    }

    public String getLocationTag() {
        return locationTag;
    }

    public void setLocationTag(String locationTag) {
        this.locationTag = locationTag;
    }

    public String getLocationText() {
        return locationText;
    }

    public void setLocationText(String locationText) {
        this.locationText = locationText;
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
