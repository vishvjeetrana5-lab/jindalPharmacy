package dataman.dmToolkit.razorPay.shared.param;

import com.google.gson.annotations.SerializedName;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class RazorPayPosPGDeviceId_Mdl implements Serializable, IsSerializable {

    @SerializedName(value = "id")
    private String deviceId;

    @SerializedName(value = "name")
    private String deviceName;

//    Constructor
    public RazorPayPosPGDeviceId_Mdl() {
    }
    
//    Getter && Setter

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

}
