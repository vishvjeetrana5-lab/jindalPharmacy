package dataman.dmToolkit.razorPay.shared.pmtCancelReq;

import com.google.gson.annotations.SerializedName;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class PmtCancelRequest_Mdl implements Serializable, IsSerializable {

    @SerializedName(value = "username")
    private String username;

    @SerializedName(value = "appKey")
    private String appKey;

    @SerializedName(value = "origP2pRequestId")
    private String origP2pRequestId;

    @SerializedName(value = "pushTo")
    private PmtCancelPushTo_Mdl pushTo;

    public PmtCancelRequest_Mdl() {
        pushTo = new PmtCancelPushTo_Mdl();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getOrigP2pRequestId() {
        return origP2pRequestId;
    }

    public void setOrigP2pRequestId(String origP2pRequestId) {
        this.origP2pRequestId = origP2pRequestId;
    }

    public PmtCancelPushTo_Mdl getPushTo() {
        return pushTo;
    }

    public void setPushTo(PmtCancelPushTo_Mdl pushTo) {
        this.pushTo = pushTo;
    }
}
