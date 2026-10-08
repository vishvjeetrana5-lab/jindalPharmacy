package dataman.dmToolkit.razorPay.shared.pmtStatusRes;

import com.google.gson.annotations.SerializedName;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GetStatusResponse_Mdl implements Serializable, IsSerializable {

    private boolean success;
    private String responseString;
    private String messageCode;
    private String message;
    private String realCode;
    private String sessionKey;
    private String username;
    private List<String> apps;
    private boolean enableRki;
    private String tid;
    private double amount;
    private double amountOriginal;
    private String authCode;
    private String currencyCode;
    private String customerName;
    private String customerMobile;
    private String customerReceiptUrl;
    private String deviceSerial;
    private String externalRefNumber;
    private String txnId;
    private String latitude;
    private String longitude;
    private String merchantName;
    private String mid;
    private String nonceStatus;
    private String orgCode;
    private String merchantCode;
    private String payerName;
    private String paymentCardType;
    private String paymentMode;
    private long postingDate;
    private String processCode;
    private String rrNumber;
    private String settlementStatus;
    private String signatureId;
    private String status;
    private List<String> states;
    private String userMobile;
    private String txnType;
    private boolean dccOpted;
    private int cardHolderCurrencyExponent;
    private String userAgreement;
    private boolean signable;
    private boolean voidable;
    private boolean refundable;
    private String chargeSlipDate;
    private String readableChargeSlipDate;
    private String issuerCode;
    private int maximumPayAttemptsAllowed;
    private int maximumSuccessfulPaymentAllowed;
    private boolean noExpiryFlag;
    private long settlementTime;
    private String receiptUrl;
    private boolean signReqd;
    private String txnTypeDesc;
    private String paymentGateway;
    private String acquirerCode;
    private long createdTime;
    private boolean customerNameAvailable;
    private boolean callbackEnabled;
    private boolean onlineRefundable;
    private boolean manualRefund;
    private int shiftNo;
    private boolean shouldRoundOffExchangeRate;
    private boolean shouldRoundOffConvertedAmount;
    private String orderNumber;
    private String reverseReferenceNumber;
    private double totalAmount;
    private String nameOnCard;
    private boolean tipEnabled;
    private boolean callTC;
    private String acquisitionId;
    private String acquisitionKey;
    private boolean processCronOutput;
    private boolean externalDevice;
    private boolean tipAdjusted;
    private List<String> txnMetadata;
    private int middlewareStanNumber;
    private boolean otpRequired;
    private String abstractPaymentStatus;
    private String p2pRequestId;
    private boolean twoStepConfirmPreAuth;
    private boolean redirect;
    private boolean reload;
    private String formattedPan;
    private String paymentCardBrand;

    public GetStatusResponse_Mdl() {

    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
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

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getApps() {
        return apps;
    }

    public void setApps(List<String> apps) {
        this.apps = apps;
    }

    public boolean isEnableRki() {
        return enableRki;
    }

    public void setEnableRki(boolean enableRki) {
        this.enableRki = enableRki;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmountOriginal() {
        return amountOriginal;
    }

    public void setAmountOriginal(double amountOriginal) {
        this.amountOriginal = amountOriginal;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getCustomerReceiptUrl() {
        return customerReceiptUrl;
    }

    public void setCustomerReceiptUrl(String customerReceiptUrl) {
        this.customerReceiptUrl = customerReceiptUrl;
    }

    public String getDeviceSerial() {
        return deviceSerial;
    }

    public void setDeviceSerial(String deviceSerial) {
        this.deviceSerial = deviceSerial;
    }

    public String getExternalRefNumber() {
        return externalRefNumber;
    }

    public void setExternalRefNumber(String externalRefNumber) {
        this.externalRefNumber = externalRefNumber;
    }

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getNonceStatus() {
        return nonceStatus;
    }

    public void setNonceStatus(String nonceStatus) {
        this.nonceStatus = nonceStatus;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getPaymentCardType() {
        return paymentCardType;
    }

    public void setPaymentCardType(String paymentCardType) {
        this.paymentCardType = paymentCardType;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public long getPostingDate() {
        return postingDate;
    }

    public void setPostingDate(long postingDate) {
        this.postingDate = postingDate;
    }

    public String getProcessCode() {
        return processCode;
    }

    public void setProcessCode(String processCode) {
        this.processCode = processCode;
    }

    public String getRrNumber() {
        return rrNumber;
    }

    public void setRrNumber(String rrNumber) {
        this.rrNumber = rrNumber;
    }

    public String getSettlementStatus() {
        return settlementStatus;
    }

    public void setSettlementStatus(String settlementStatus) {
        this.settlementStatus = settlementStatus;
    }

    public String getSignatureId() {
        return signatureId;
    }

    public void setSignatureId(String signatureId) {
        this.signatureId = signatureId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getStates() {
        return states;
    }

    public void setStates(List<String> states) {
        this.states = states;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getTxnType() {
        return txnType;
    }

    public void setTxnType(String txnType) {
        this.txnType = txnType;
    }

    public boolean isDccOpted() {
        return dccOpted;
    }

    public void setDccOpted(boolean dccOpted) {
        this.dccOpted = dccOpted;
    }

    public int getCardHolderCurrencyExponent() {
        return cardHolderCurrencyExponent;
    }

    public void setCardHolderCurrencyExponent(int cardHolderCurrencyExponent) {
        this.cardHolderCurrencyExponent = cardHolderCurrencyExponent;
    }

    public String getUserAgreement() {
        return userAgreement;
    }

    public void setUserAgreement(String userAgreement) {
        this.userAgreement = userAgreement;
    }

    public boolean isSignable() {
        return signable;
    }

    public void setSignable(boolean signable) {
        this.signable = signable;
    }

    public boolean isVoidable() {
        return voidable;
    }

    public void setVoidable(boolean voidable) {
        this.voidable = voidable;
    }

    public boolean isRefundable() {
        return refundable;
    }

    public void setRefundable(boolean refundable) {
        this.refundable = refundable;
    }

    public String getChargeSlipDate() {
        return chargeSlipDate;
    }

    public void setChargeSlipDate(String chargeSlipDate) {
        this.chargeSlipDate = chargeSlipDate;
    }

    public String getReadableChargeSlipDate() {
        return readableChargeSlipDate;
    }

    public void setReadableChargeSlipDate(String readableChargeSlipDate) {
        this.readableChargeSlipDate = readableChargeSlipDate;
    }

    public String getIssuerCode() {
        return issuerCode;
    }

    public void setIssuerCode(String issuerCode) {
        this.issuerCode = issuerCode;
    }

    public int getMaximumPayAttemptsAllowed() {
        return maximumPayAttemptsAllowed;
    }

    public void setMaximumPayAttemptsAllowed(int maximumPayAttemptsAllowed) {
        this.maximumPayAttemptsAllowed = maximumPayAttemptsAllowed;
    }

    public int getMaximumSuccessfulPaymentAllowed() {
        return maximumSuccessfulPaymentAllowed;
    }

    public void setMaximumSuccessfulPaymentAllowed(int maximumSuccessfulPaymentAllowed) {
        this.maximumSuccessfulPaymentAllowed = maximumSuccessfulPaymentAllowed;
    }

    public boolean isNoExpiryFlag() {
        return noExpiryFlag;
    }

    public void setNoExpiryFlag(boolean noExpiryFlag) {
        this.noExpiryFlag = noExpiryFlag;
    }

    public long getSettlementTime() {
        return settlementTime;
    }

    public void setSettlementTime(long settlementTime) {
        this.settlementTime = settlementTime;
    }

    public String getReceiptUrl() {
        return receiptUrl;
    }

    public void setReceiptUrl(String receiptUrl) {
        this.receiptUrl = receiptUrl;
    }

    public boolean isSignReqd() {
        return signReqd;
    }

    public void setSignReqd(boolean signReqd) {
        this.signReqd = signReqd;
    }

    public String getTxnTypeDesc() {
        return txnTypeDesc;
    }

    public void setTxnTypeDesc(String txnTypeDesc) {
        this.txnTypeDesc = txnTypeDesc;
    }

    public String getPaymentGateway() {
        return paymentGateway;
    }

    public void setPaymentGateway(String paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    public String getAcquirerCode() {
        return acquirerCode;
    }

    public void setAcquirerCode(String acquirerCode) {
        this.acquirerCode = acquirerCode;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public boolean isCustomerNameAvailable() {
        return customerNameAvailable;
    }

    public void setCustomerNameAvailable(boolean customerNameAvailable) {
        this.customerNameAvailable = customerNameAvailable;
    }

    public boolean isCallbackEnabled() {
        return callbackEnabled;
    }

    public void setCallbackEnabled(boolean callbackEnabled) {
        this.callbackEnabled = callbackEnabled;
    }

    public boolean isOnlineRefundable() {
        return onlineRefundable;
    }

    public void setOnlineRefundable(boolean onlineRefundable) {
        this.onlineRefundable = onlineRefundable;
    }

    public boolean isManualRefund() {
        return manualRefund;
    }

    public void setManualRefund(boolean manualRefund) {
        this.manualRefund = manualRefund;
    }

    public int getShiftNo() {
        return shiftNo;
    }

    public void setShiftNo(int shiftNo) {
        this.shiftNo = shiftNo;
    }

    public boolean isShouldRoundOffExchangeRate() {
        return shouldRoundOffExchangeRate;
    }

    public void setShouldRoundOffExchangeRate(boolean shouldRoundOffExchangeRate) {
        this.shouldRoundOffExchangeRate = shouldRoundOffExchangeRate;
    }

    public boolean isShouldRoundOffConvertedAmount() {
        return shouldRoundOffConvertedAmount;
    }

    public void setShouldRoundOffConvertedAmount(boolean shouldRoundOffConvertedAmount) {
        this.shouldRoundOffConvertedAmount = shouldRoundOffConvertedAmount;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getReverseReferenceNumber() {
        return reverseReferenceNumber;
    }

    public void setReverseReferenceNumber(String reverseReferenceNumber) {
        this.reverseReferenceNumber = reverseReferenceNumber;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getNameOnCard() {
        return nameOnCard;
    }

    public void setNameOnCard(String nameOnCard) {
        this.nameOnCard = nameOnCard;
    }

    public boolean isTipEnabled() {
        return tipEnabled;
    }

    public void setTipEnabled(boolean tipEnabled) {
        this.tipEnabled = tipEnabled;
    }

    public boolean isCallTC() {
        return callTC;
    }

    public void setCallTC(boolean callTC) {
        this.callTC = callTC;
    }

    public String getAcquisitionId() {
        return acquisitionId;
    }

    public void setAcquisitionId(String acquisitionId) {
        this.acquisitionId = acquisitionId;
    }

    public String getAcquisitionKey() {
        return acquisitionKey;
    }

    public void setAcquisitionKey(String acquisitionKey) {
        this.acquisitionKey = acquisitionKey;
    }

    public boolean isProcessCronOutput() {
        return processCronOutput;
    }

    public void setProcessCronOutput(boolean processCronOutput) {
        this.processCronOutput = processCronOutput;
    }

    public boolean isExternalDevice() {
        return externalDevice;
    }

    public void setExternalDevice(boolean externalDevice) {
        this.externalDevice = externalDevice;
    }

    public boolean isTipAdjusted() {
        return tipAdjusted;
    }

    public void setTipAdjusted(boolean tipAdjusted) {
        this.tipAdjusted = tipAdjusted;
    }

    public List<String> getTxnMetadata() {
        return txnMetadata;
    }

    public void setTxnMetadata(List<String> txnMetadata) {
        this.txnMetadata = txnMetadata;
    }

    public int getMiddlewareStanNumber() {
        return middlewareStanNumber;
    }

    public void setMiddlewareStanNumber(int middlewareStanNumber) {
        this.middlewareStanNumber = middlewareStanNumber;
    }

    public boolean isOtpRequired() {
        return otpRequired;
    }

    public void setOtpRequired(boolean otpRequired) {
        this.otpRequired = otpRequired;
    }

    public String getAbstractPaymentStatus() {
        return abstractPaymentStatus;
    }

    public void setAbstractPaymentStatus(String abstractPaymentStatus) {
        this.abstractPaymentStatus = abstractPaymentStatus;
    }

    public String getP2pRequestId() {
        return p2pRequestId;
    }

    public void setP2pRequestId(String p2pRequestId) {
        this.p2pRequestId = p2pRequestId;
    }

    public boolean isTwoStepConfirmPreAuth() {
        return twoStepConfirmPreAuth;
    }

    public void setTwoStepConfirmPreAuth(boolean twoStepConfirmPreAuth) {
        this.twoStepConfirmPreAuth = twoStepConfirmPreAuth;
    }

    public boolean isRedirect() {
        return redirect;
    }

    public void setRedirect(boolean redirect) {
        this.redirect = redirect;
    }

    public boolean isReload() {
        return reload;
    }

    public void setReload(boolean reload) {
        this.reload = reload;
    }

    public String getResponseString() {
        return responseString;
    }

    public void setResponseString(String responseString) {
        this.responseString = responseString;
    }

    public String getFormattedPan() {
        return formattedPan;
}

    public void setFormattedPan(String formattedPan) {
        this.formattedPan = formattedPan;
    }

    public String getPaymentCardBrand() {
        return paymentCardBrand;
    }

    public void setPaymentCardBrand(String paymentCardBrand) {
        this.paymentCardBrand = paymentCardBrand;
    }

}
