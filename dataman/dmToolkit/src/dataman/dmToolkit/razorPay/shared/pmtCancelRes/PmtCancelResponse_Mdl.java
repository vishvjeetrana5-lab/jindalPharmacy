package dataman.dmToolkit.razorPay.shared.pmtCancelRes;

import com.google.gson.annotations.SerializedName;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmToolkit.razorPay.shared.pmtStatusRes.GetStatusResponse_Mdl;
import java.io.Serializable;

public class PmtCancelResponse_Mdl implements Serializable, IsSerializable {

    @SerializedName(value = "success")
    private String success;

    @SerializedName(value = "messageCode")
    private String messageCode;

    @SerializedName(value = "message")
    private String message;

    @SerializedName(value = "errorCode")
    private String errorCode;

    @SerializedName(value = "errorMessage")
    private String errorMessage;

    @SerializedName(value = "apiMessage")
    private String apiMessage;

    @SerializedName(value = "apiWarning")
    private String apiWarning;

    @SerializedName(value = "origP2pRequestId")
    private String origP2pRequestId;
 private GetStatusResponse_Mdl respMdl = new GetStatusResponse_Mdl();
    //      Constructor
    public PmtCancelResponse_Mdl() {

    }

    // Getter && Setter
    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
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

    public String getApiMessage() {
        return apiMessage;
    }

    public void setApiMessage(String apiMessage) {
        this.apiMessage = apiMessage;
    }

    public String getApiWarning() {
        return apiWarning;
    }

    public void setApiWarning(String apiWarning) {
        this.apiWarning = apiWarning;
    }

    public String getOrigP2pRequestId() {
        return origP2pRequestId;
    }

    public void setOrigP2pRequestId(String origP2pRequestId) {
        this.origP2pRequestId = origP2pRequestId;
    }

    public GetStatusResponse_Mdl getRespMdl() {
        return respMdl;
    }

    public void setRespMdl(GetStatusResponse_Mdl respMdl) {
        this.respMdl = respMdl;
    }

}
