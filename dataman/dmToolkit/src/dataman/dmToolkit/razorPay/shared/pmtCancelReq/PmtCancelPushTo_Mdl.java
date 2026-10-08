package dataman.dmToolkit.razorPay.shared.pmtCancelReq;

import com.google.gson.annotations.SerializedName;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class PmtCancelPushTo_Mdl implements Serializable, IsSerializable {

    @SerializedName(value = "deviceId")
    private String deviceId;

    public PmtCancelPushTo_Mdl() {
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
