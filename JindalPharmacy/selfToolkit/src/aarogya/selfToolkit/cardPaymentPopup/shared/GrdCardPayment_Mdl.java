package aarogya.selfToolkit.cardPaymentPopup.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdCardPayment_Mdl extends KeyField implements Serializable, IsSerializable {

    private String cardTypeCode;
    private String cardTypeName;
    private String cardTypeNatureCode;
    private String cardTypeNatureName;
    private String cardTypeShortName;
    private String amount;
    private String cardNo;
    private String cardDate;
    private String cardPhysicalId;
    private String cardHolderName;
    private String cardNature;
    private String cardAuthorization;
    private String cardAuthorizationDocId;
    private String acNameCode;
    private String acNameName;
    private String acNature;
    private String currencyCode;
    private String currencyName;
    private double balance;
    private String conversionRate;
    private String foreignCurrencyAmount;
    private String favourOf;
    private String appKey;
    private String userName;
    private String mobileNo;
    private String refNo;
    private String deviceId;
    private String paymentModeCode;
    private String paymentModeName;
    private String requestUrl;
    private String responseUrl;
    private String cancelUrl;
    private String posPaymentMode;
    private String posPayByMobile;
    private String posPaymentDocid;
    private String cardFavourOf;

    private boolean accountEditable = false;
    private boolean paymentSuccess = false;

    public GrdCardPayment_Mdl() {
    }

    public String getForeignCurrencyAmount() {
        return foreignCurrencyAmount;
    }

    public void setForeignCurrencyAmount(String foreignCurrencyAmount) {
        this.foreignCurrencyAmount = foreignCurrencyAmount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getConversionRate() {
        return conversionRate;
    }

    public void setConversionRate(String conversionRate) {
        this.conversionRate = conversionRate;
    }

    public String getCardTypeCode() {
        return cardTypeCode;
    }

    public void setCardTypeCode(String cardTypeCode) {
        this.cardTypeCode = cardTypeCode;
    }

    public String getCardTypeName() {
        return cardTypeName;
    }

    public void setCardTypeName(String cardTypeName) {
        this.cardTypeName = cardTypeName;
    }

    public String getCardTypeNatureCode() {
        return cardTypeNatureCode;
    }

    public void setCardTypeNatureCode(String cardTypeNatureCode) {
        this.cardTypeNatureCode = cardTypeNatureCode;
    }

    public String getCardTypeNatureName() {
        return cardTypeNatureName;
    }

    public void setCardTypeNatureName(String cardTypeNatureName) {
        this.cardTypeNatureName = cardTypeNatureName;
    }

    public String getCardTypeShortName() {
        return cardTypeShortName;
    }

    public void setCardTypeShortName(String cardTypeShortName) {
        this.cardTypeShortName = cardTypeShortName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardDate() {
        return cardDate;
    }

    public void setCardDate(String cardDate) {
        this.cardDate = cardDate;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getCardNature() {
        return cardNature;
    }

    public void setCardNature(String cardNature) {
        this.cardNature = cardNature;
    }

    public String getCardAuthorization() {
        return cardAuthorization;
    }

    public void setCardAuthorization(String cardAuthorization) {
        this.cardAuthorization = cardAuthorization;
    }

    public String getAcNameCode() {
        return acNameCode;
    }

    public void setAcNameCode(String acNameCode) {
        this.acNameCode = acNameCode;
    }

    public String getAcNameName() {
        return acNameName;
    }

    public void setAcNameName(String acNameName) {
        this.acNameName = acNameName;
    }

    public String getAcNature() {
        return acNature;
    }

    public void setAcNature(String acNature) {
        this.acNature = acNature;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getCardPhysicalId() {
        return cardPhysicalId;
    }

    public void setCardPhysicalId(String cardPhysicalId) {
        this.cardPhysicalId = cardPhysicalId;
    }

    public String getCardAuthorizationDocId() {
        return cardAuthorizationDocId;
    }

    public void setCardAuthorizationDocId(String cardAuthorizationDocId) {
        this.cardAuthorizationDocId = cardAuthorizationDocId;
    }

    public GrdCardPayment_Mdl clone() {
        GrdCardPayment_Mdl cardPayment = new GrdCardPayment_Mdl();

        copy(cardPayment);

        return cardPayment;
    }

    public void copy(GrdCardPayment_Mdl cardPayment) {
        cardPayment.setCardTypeCode(this.getCardTypeCode());
        cardPayment.setCardTypeName(this.getCardTypeName());
        cardPayment.setCardTypeNatureCode(this.getCardTypeNatureCode());
        cardPayment.setCardTypeNatureName(this.getCardTypeNatureName());
        cardPayment.setCardTypeShortName(this.getCardTypeShortName());
        cardPayment.setAmount(this.getAmount());
        cardPayment.setCardNo(this.getCardNo());
        cardPayment.setCardDate(this.getCardDate());
        cardPayment.setCardPhysicalId(this.getCardPhysicalId());
        cardPayment.setCardHolderName(this.getCardHolderName());
        cardPayment.setCardNature(this.getCardNature());
        cardPayment.setCardAuthorization(this.getCardAuthorization());
        cardPayment.setCardAuthorizationDocId(this.getCardAuthorizationDocId());
        cardPayment.setAcNameCode(this.getAcNameCode());
        cardPayment.setAcNameName(this.getAcNameName());
        cardPayment.setAcNature(this.getAcNature());
        cardPayment.setBalance(this.getBalance());
        cardPayment.setCurrencyCode(this.getCurrencyCode());
        cardPayment.setCurrencyName(this.getCurrencyName());
        cardPayment.setForeignCurrencyAmount(this.getForeignCurrencyAmount());
        cardPayment.setConversionRate(this.getConversionRate());
        cardPayment.setAccountEditable(this.isAccountEditable());
        cardPayment.setPaymentSuccess(this.isPaymentSuccess());
        cardPayment.setAppKey(this.getAppKey());
        cardPayment.setUserName(this.getUserName());
        cardPayment.setMobileNo(this.getMobileNo());
        cardPayment.setRefNo(this.getRefNo());
        cardPayment.setDeviceId(this.getDeviceId());
        cardPayment.setPaymentModeCode(this.getPaymentModeCode());
        cardPayment.setPaymentModeName(this.getPaymentModeName());
        cardPayment.setRequestUrl(this.getRequestUrl());
        cardPayment.setResponseUrl(this.getResponseUrl());
        cardPayment.setCancelUrl(this.getCancelUrl());
        cardPayment.setPosPaymentMode(this.getPosPaymentMode());
        cardPayment.setPosPayByMobile(this.getPosPayByMobile());
        cardPayment.setPosPaymentDocid(this.getPosPaymentDocid());
    }

    public boolean isAccountEditable() {
        return accountEditable;
    }

    public void setAccountEditable(boolean accountEditable) {
        this.accountEditable = accountEditable;
    }

    public boolean isPaymentSuccess() {
        return paymentSuccess;
    }

    public void setPaymentSuccess(boolean paymentSuccess) {
        this.paymentSuccess = paymentSuccess;
    }

    public String getFavourOf() {
        return favourOf;
    }

    public void setFavourOf(String favourOf) {
        this.favourOf = favourOf;
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

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPaymentModeCode() {
        return paymentModeCode;
    }

    public void setPaymentModeCode(String paymentModeCode) {
        this.paymentModeCode = paymentModeCode;
    }

    public String getPaymentModeName() {
        return paymentModeName;
    }

    public void setPaymentModeName(String paymentModeName) {
        this.paymentModeName = paymentModeName;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getResponseUrl() {
        return responseUrl;
    }

    public void setResponseUrl(String responseUrl) {
        this.responseUrl = responseUrl;
    }

    public String getCancelUrl() {
        return cancelUrl;
    }

    public void setCancelUrl(String cancelUrl) {
        this.cancelUrl = cancelUrl;
    }

    public String getPosPaymentMode() {
        return posPaymentMode;
    }

    public void setPosPaymentMode(String posPaymentMode) {
        this.posPaymentMode = posPaymentMode;
    }

    public String getPosPayByMobile() {
        return posPayByMobile;
    }

    public void setPosPayByMobile(String posPayByMobile) {
        this.posPayByMobile = posPayByMobile;
    }

    public String getPosPaymentDocid() {
        return posPaymentDocid;
    }

    public void setPosPaymentDocid(String posPaymentDocid) {
        this.posPaymentDocid = posPaymentDocid;
    }

    public String getCardFavourOf() {
        return cardFavourOf;
    }

    public void setCardFavourOf(String cardFavourOf) {
        this.cardFavourOf = cardFavourOf;
    }
    
    

}
