package dataman.dmToolkit.razorPay.shared.pmtStatusRes;

import com.google.gson.annotations.SerializedName;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PmtStatusResponse_Mdl implements Serializable, IsSerializable {

    @SerializedName(value = "success")
    private Boolean success;

    @SerializedName(value = "messageCode")
    private String messageCode;

    @SerializedName(value = "message")
    private String message;

    @SerializedName(value = "realCode")
    private String realCode;

    @SerializedName(value = "amount")
    private String amount;

    @SerializedName(value = "externalRefNumber")
    private String externalRefNumber;

    @SerializedName(value = "externalRefNumber2")
    private String externalRefNumber2;

    @SerializedName(value = "externalRefNumber4")
    private String externalRefNumber4;

    @SerializedName(value = "externalRefNumbers")
    private List<String> externalRefNumbers;

    @SerializedName(value = "txnId")
    private String txnId;

    @SerializedName(value = "status")
    private String status;

    @SerializedName(value = "cardType")
    private String cardType;

    @SerializedName(value = "p2pRequestId")
    private String p2pRequestId;

    @SerializedName(value = "errorCode")
    private String errorCode;

    @SerializedName(value = "errorMessage")
    private String errorMessage;

    @SerializedName(value = "postingDate")
    private String postingDate;

    @SerializedName(value = "totalAmount")
    private String totalAmount;

    @SerializedName(value = "paymentCardType")
    private String paymentCardType;

    @SerializedName(value = "rrNumber")
    private String rrNumber;

    private GetStatusResponse_Mdl respMdl = new GetStatusResponse_Mdl();

    public PmtStatusResponse_Mdl() {
        externalRefNumbers = new ArrayList<>();
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

    public String getRealCode() {
        return realCode;
    }

    public void setRealCode(String realCode) {
        this.realCode = realCode;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getExternalRefNumber() {
        return externalRefNumber;
    }

    public void setExternalRefNumber(String externalRefNumber) {
        this.externalRefNumber = externalRefNumber;
    }

    public String getExternalRefNumber2() {
        return externalRefNumber2;
    }

    public void setExternalRefNumber2(String externalRefNumber2) {
        this.externalRefNumber2 = externalRefNumber2;
    }

    public String getExternalRefNumber4() {
        return externalRefNumber4;
    }

    public void setExternalRefNumber4(String externalRefNumber4) {
        this.externalRefNumber4 = externalRefNumber4;
    }

    public List<String> getExternalRefNumbers() {
        return externalRefNumbers;
    }

    public void setExternalRefNumbers(List<String> externalRefNumbers) {
        this.externalRefNumbers = externalRefNumbers;
    }

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getP2pRequestId() {
        return p2pRequestId;
    }

    public void setP2pRequestId(String p2pRequestId) {
        this.p2pRequestId = p2pRequestId;
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

    public String getPostingDate() {
        return postingDate;
    }

    public void setPostingDate(String postingDate) {
        this.postingDate = postingDate;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentCardType() {
        return paymentCardType;
    }

    public void setPaymentCardType(String paymentCardType) {
        this.paymentCardType = paymentCardType;
    }

    public String getRrNumber() {
        return rrNumber;
    }

    public void setRrNumber(String rrNumber) {
        this.rrNumber = rrNumber;
    }

    public GetStatusResponse_Mdl getRespMdl() {
        return respMdl;
    }

    public void setRespMdl(GetStatusResponse_Mdl respMdl) {
        this.respMdl = respMdl;
    }

}
