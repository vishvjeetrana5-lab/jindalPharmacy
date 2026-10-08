package aarogya.selfToolkit.cardPaymentPopup.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import dataman.dmBase.self.shared.RecId;
import dataman.dmToolkit.razorPay.shared.pmtStatusRes.GetStatusResponse_Mdl;
import java.io.Serializable;

public class PaymentProcess_Mdl extends KeyField implements Serializable, IsSerializable {

    private String cardTypeMastCode;
    private String appKey;
    private String userName;
    private String deviceId;
    private String requestURL;
    private String responseURL;
    private String cancelURL;
    private String amount;
    private String paymentMode;
    private String voucherPrefix;
    private RecId recId = new RecId();

    private int id;
    private String refName;
    private String email;
    private String mobile;
    private String gtwAckNo;
    private String gtwStatusCode;
    private String gtwStatusMsg;
    private String gtwBankTrnNo;
    private String gtwTrnId;
    private String abstractPaymentStatus;
    private String posPaymentDocid;
    private boolean paymentSuccess;
    private boolean blnAdd;
    private boolean cancelledCalled;
//    private boolean isPaymentSuccess ;
    

    private GetStatusResponse_Mdl respMdl = new GetStatusResponse_Mdl();

    public PaymentProcess_Mdl() {
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getRequestURL() {
        return requestURL;
    }

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public String getResponseURL() {
        return responseURL;
    }

    public void setResponseURL(String responseURL) {
        this.responseURL = responseURL;
    }

    public String getCancelURL() {
        return cancelURL;
    }

    public void setCancelURL(String cancelURL) {
        this.cancelURL = cancelURL;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getVoucherPrefix() {
        return voucherPrefix;
    }

    public void setVoucherPrefix(String voucherPrefix) {
        this.voucherPrefix = voucherPrefix;
    }

    public RecId getRecId() {
        return recId;
    }

    public void setRecId(RecId recId) {
        this.recId = recId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public boolean isPaymentSuccess() {
        return paymentSuccess;
    }

    public void setPaymentSuccess(boolean paymentSuccess) {
        this.paymentSuccess = paymentSuccess;
    }

    public String getAbstractPaymentStatus() {
        return abstractPaymentStatus;
    }

    public void setAbstractPaymentStatus(String abstractPaymentStatus) {
        this.abstractPaymentStatus = abstractPaymentStatus;
    }

    public GetStatusResponse_Mdl getRespMdl() {
        return respMdl;
    }

    public void setRespMdl(GetStatusResponse_Mdl respMdl) {
        this.respMdl = respMdl;
    }

    public boolean isBlnAdd() {
        return blnAdd;
    }

    public void setBlnAdd(boolean blnAdd) {
        this.blnAdd = blnAdd;
    }

    public boolean isCancelledCalled() {
        return cancelledCalled;
    }

    public void setCancelledCalled(boolean cancelledCalled) {
        this.cancelledCalled = cancelledCalled;
    }

    public String getCardTypeMastCode() {
        return cardTypeMastCode;
    }

    public void setCardTypeMastCode(String cardTypeMastCode) {
        this.cardTypeMastCode = cardTypeMastCode;
    }

//    public boolean isIsPaymentSuccess() {
//        return isPaymentSuccess;
//    }
//
//    public void setIsPaymentSuccess(boolean isPaymentSuccess) {
//        this.isPaymentSuccess = isPaymentSuccess;
//    }

    public String getPosPaymentDocid() {
        return posPaymentDocid;
    }

    public void setPosPaymentDocid(String posPaymentDocid) {
        this.posPaymentDocid = posPaymentDocid;
    }

}
