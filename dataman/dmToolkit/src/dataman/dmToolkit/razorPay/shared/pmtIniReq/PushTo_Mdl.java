package dataman.dmToolkit.razorPay.shared.pmtIniReq;

import com.google.gson.annotations.SerializedName;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class PushTo_Mdl implements Serializable, IsSerializable {

    @SerializedName(value = "deviceId")
    private String deviceId;

    public PushTo_Mdl() {
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
