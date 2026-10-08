package dataman.dmToolkit.razorPay.shared.pmtIntegration.shared;

import com.google.gson.annotations.SerializedName;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmToolkit.razorPay.shared.pmtStatusRes.GetStatusResponse_Mdl;
import java.io.Serializable;

public class PmtGatewayDetail_Mdl implements Serializable, IsSerializable {

    @SerializedName(value = "Id")
    private String id;
    private String responseString;
    @SerializedName(value = "Amount")
    private String amount;

    @SerializedName(value = "DeviceId")
    private String deviceId;

    @SerializedName(value = "RefName")
    private String refName;

    @SerializedName(value = "Email")
    private String email;

    @SerializedName(value = "Mobile")
    private String mobile;

    @SerializedName(value = "GtwAckNo")
    private String gtwAckNo;

    @SerializedName(value = "GtwStatusCode")
    private String gtwStatusCode;

    @SerializedName(value = "GtwStatusMsg")
    private String gtwStatusMsg;

    @SerializedName(value = "GtwPmtMode")
    private String gtwPmtMode;

    @SerializedName(value = "GtwBankTrnNo")
    private String gtwBankTrnNo;

    @SerializedName(value = "GtwTrnId")
    private String gtwTrnId;

    @SerializedName(value = "Status")
    private int status;

    private GetStatusResponse_Mdl respMdl = new GetStatusResponse_Mdl();

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getRefName() {
        return refName;
    }

    public void setRefName(String refName) {
        this.refName = refName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getGtwAckNo() {
        return gtwAckNo;
    }

    public void setGtwAckNo(String gtwAckNo) {
        this.gtwAckNo = gtwAckNo;
    }

    public String getGtwStatusCode() {
        return gtwStatusCode;
    }

    public void setGtwStatusCode(String gtwStatusCode) {
        this.gtwStatusCode = gtwStatusCode;
    }

    public String getGtwStatusMsg() {
        return gtwStatusMsg;
    }

    public void setGtwStatusMsg(String gtwStatusMsg) {
        this.gtwStatusMsg = gtwStatusMsg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getGtwPmtMode() {
        return gtwPmtMode;
    }

    public void setGtwPmtMode(String gtwPmtMode) {
        this.gtwPmtMode = gtwPmtMode;
    }

    public String getGtwBankTrnNo() {
        return gtwBankTrnNo;
    }

    public void setGtwBankTrnNo(String gtwBankTrnNo) {
        this.gtwBankTrnNo = gtwBankTrnNo;
    }

    public String getGtwTrnId() {
        return gtwTrnId;
    }

    public void setGtwTrnId(String gtwTrnId) {
        this.gtwTrnId = gtwTrnId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GetStatusResponse_Mdl getRespMdl() {
        return respMdl;
    }

    public void setRespMdl(GetStatusResponse_Mdl respMdl) {
        this.respMdl = respMdl;
    }

    public String getResponseString() {
        return responseString;
    }

    public void setResponseString(String responseString) {
        this.responseString = responseString;
    }

}
