package dataman.dmToolkit.razorPay.shared.pmtIniRes;

import com.google.gson.annotations.SerializedName;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class PmtIniResponse_Mdl implements Serializable, IsSerializable {

    @SerializedName(value = "success")
    private Boolean success;
    
    private String responseString;

    @SerializedName(value = "messageCode")
    private String messageCode;

    @SerializedName(value = "message")
    private String message;

    @SerializedName(value = "errorCode")
    private String errorCode;

    @SerializedName(value = "errorMessage")
    private String errorMessage;

    @SerializedName(value = "realCode")
    private String realCode;

    @SerializedName(value = "apiMessageTitle")
    private String apiMessageTitle;

    @SerializedName(value = "apiMessage")
    private String apiMessage;

    @SerializedName(value = "apiMessageText")
    private String apiMessageText;

    @SerializedName(value = "apiWarning")
    private String apiWarning;

    @SerializedName(value = "p2pRequestId")
    private String p2pRequestId;

    public PmtIniResponse_Mdl() {
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getRealCode() {
        return realCode;
    }

    public void setRealCode(String realCode) {
        this.realCode = realCode;
    }

    public String getApiMessageTitle() {
        return apiMessageTitle;
    }

    public void setApiMessageTitle(String apiMessageTitle) {
        this.apiMessageTitle = apiMessageTitle;
    }

    public String getApiMessage() {
        return apiMessage;
    }

    public void setApiMessage(String apiMessage) {
        this.apiMessage = apiMessage;
    }

    public String getApiMessageText() {
        return apiMessageText;
    }

    public void setApiMessageText(String apiMessageText) {
        this.apiMessageText = apiMessageText;
    }

    public String getApiWarning() {
        return apiWarning;
    }

    public void setApiWarning(String apiWarning) {
        this.apiWarning = apiWarning;
    }

    public String getP2pRequestId() {
        return p2pRequestId;
    }

    public void setP2pRequestId(String p2pRequestId) {
        this.p2pRequestId = p2pRequestId;
    }

    public String getResponseString() {
        return responseString;
    }

    public void setResponseString(String responseString) {
        this.responseString = responseString;
    }
}
