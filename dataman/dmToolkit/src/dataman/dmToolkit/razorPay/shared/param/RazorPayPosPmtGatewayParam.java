package dataman.dmToolkit.razorPay.shared.param;

 
import com.google.gson.annotations.SerializedName;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RazorPayPosPmtGatewayParam implements 
//        IPaymentGatewayParam,
        Serializable, IsSerializable {

    @SerializedName(value = "App_Key")
    private String appKey;

    @SerializedName(value = "User_Name")
    private String username;

    @SerializedName(value = "Mode")
    private String mode;

    @SerializedName(value = "defaultDevice")
    private String defaultDeviceId;

//    @SerializedName(value = "availableDevices")
//    private List<RazorPayPosPGDeviceId_Mdl> availableDeviceIds;

    @SerializedName(value = "url_initiate")
    private String urlInitiate;

    @SerializedName(value = "url_status")
    private String urlStatus;

    @SerializedName(value = "url_cancel")
    private String urlCancel;

    public RazorPayPosPmtGatewayParam() {
//        availableDeviceIds = new ArrayList<>();
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getDefaultDeviceId() {
        return defaultDeviceId;
    }

    public void setDefaultDeviceId(String defaultDeviceId) {
        this.defaultDeviceId = defaultDeviceId;
    }

//    public List<RazorPayPosPGDeviceId_Mdl> getAvailableDeviceIds() {
//        return availableDeviceIds;
//    }
//
//    public void setAvailableDeviceIds(List<RazorPayPosPGDeviceId_Mdl> availableDeviceIds) {
//        this.availableDeviceIds = availableDeviceIds;
//    }

    public String getUrlInitiate() {
        return urlInitiate;
    }

    public void setUrlInitiate(String urlInitiate) {
        this.urlInitiate = urlInitiate;
    }

    public String getUrlStatus() {
        return urlStatus;
    }

    public void setUrlStatus(String urlStatus) {
        this.urlStatus = urlStatus;
    }

    public String getUrlCancel() {
        return urlCancel;
    }

    public void setUrlCancel(String urlCancel) {
        this.urlCancel = urlCancel;
    }

}
