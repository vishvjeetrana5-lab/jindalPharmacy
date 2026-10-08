package aarogya.master.cardTypeMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class CardTypeMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    private String linkedAccountName, linkedAccountCode;
    private String natureName, natureCode;
    private String activeName, activeCode;
    private String physicalIdReqName, physicalIdReqCode;
    private String onAmount;
    private String points;
    private String valuePerPoint;
    private String shortName;
    private String cardTypeName, cardTypeCode;
    private String smsAlertName, smsAlertCode;
    private String isAccountEditableName, isAccountEditableCode;

    private String emailAlertName, emailAlertCode;
    private String validDays;

    private String currencyCode;
    private String currencyName;
    //=============================<MJ>=====================//
    private String isPOSIntegrationCode;
    private String isPOSIntegrationName;
    private String isPOSIntegrationTypeCode;
    private String isPOSIntegrationTypeName;
    private String integrationParams;

    //=============================<****>=====================//
    public CardTypeMaster_Mdl() {
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getLinkedAccountName() {
        return linkedAccountName;
    }

    public void setLinkedAccountName(String linkedAccountName) {
        this.linkedAccountName = linkedAccountName;
    }

    public String getLinkedAccountCode() {
        return linkedAccountCode;
    }

    public void setLinkedAccountCode(String linkedAccountCode) {
        this.linkedAccountCode = linkedAccountCode;
    }

    public String getNatureName() {
        return natureName;
    }

    public void setNatureName(String natureName) {
        this.natureName = natureName;
    }

    public String getNatureCode() {
        return natureCode;
    }

    public void setNatureCode(String natureCode) {
        this.natureCode = natureCode;
    }

    public String getActiveName() {
        return activeName;
    }

    public void setActiveName(String activeName) {
        this.activeName = activeName;
    }

    public String getActiveCode() {
        return activeCode;
    }

    public void setActiveCode(String activeCode) {
        this.activeCode = activeCode;
    }

    public String getPhysicalIdReqName() {
        return physicalIdReqName;
    }

    public void setPhysicalIdReqName(String physicalIdReqName) {
        this.physicalIdReqName = physicalIdReqName;
    }

    public String getPhysicalIdReqCode() {
        return physicalIdReqCode;
    }

    public void setPhysicalIdReqCode(String physicalIdReqCode) {
        this.physicalIdReqCode = physicalIdReqCode;
    }

    public String getOnAmount() {
        return onAmount;
    }

    public void setOnAmount(String onAmount) {
        this.onAmount = onAmount;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getValuePerPoint() {
        return valuePerPoint;
    }

    public void setValuePerPoint(String valuePerPoint) {
        this.valuePerPoint = valuePerPoint;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getCardTypeName() {
        return cardTypeName;
    }

    public void setCardTypeName(String cardTypeName) {
        this.cardTypeName = cardTypeName;
    }

    public String getCardTypeCode() {
        return cardTypeCode;
    }

    public void setCardTypeCode(String cardTypeCode) {
        this.cardTypeCode = cardTypeCode;
    }

    public String getSmsAlertName() {
        return smsAlertName;
    }

    public void setSmsAlertName(String smsAlertName) {
        this.smsAlertName = smsAlertName;
    }

    public String getSmsAlertCode() {
        return smsAlertCode;
    }

    public void setSmsAlertCode(String smsAlertCode) {
        this.smsAlertCode = smsAlertCode;
    }

    public String getEmailAlertName() {
        return emailAlertName;
    }

    public void setEmailAlertName(String emailAlertName) {
        this.emailAlertName = emailAlertName;
    }

    public String getEmailAlertCode() {
        return emailAlertCode;
    }

    public void setEmailAlertCode(String emailAlertCode) {
        this.emailAlertCode = emailAlertCode;
    }

    public String getValidDays() {
        return validDays;
    }

    public void setValidDays(String validDays) {
        this.validDays = validDays;
    }

    public String getIsAccountEditableName() {
        return isAccountEditableName;
    }

    public void setIsAccountEditableName(String isAccountEditableName) {
        this.isAccountEditableName = isAccountEditableName;
    }

    public String getIsAccountEditableCode() {
        return isAccountEditableCode;
    }

    public void setIsAccountEditableCode(String isAccountEditableCode) {
        this.isAccountEditableCode = isAccountEditableCode;
    }

    public String getIsPOSIntegrationCode() {
        return isPOSIntegrationCode;
    }

    public void setIsPOSIntegrationCode(String isPOSIntegrationCode) {
        this.isPOSIntegrationCode = isPOSIntegrationCode;
    }

    public String getIsPOSIntegrationName() {
        return isPOSIntegrationName;
    }

    public void setIsPOSIntegrationName(String isPOSIntegrationName) {
        this.isPOSIntegrationName = isPOSIntegrationName;
    }

    public String getIsPOSIntegrationTypeCode() {
        return isPOSIntegrationTypeCode;
    }

    public void setIsPOSIntegrationTypeCode(String isPOSIntegrationTypeCode) {
        this.isPOSIntegrationTypeCode = isPOSIntegrationTypeCode;
    }

    public String getIsPOSIntegrationTypeName() {
        return isPOSIntegrationTypeName;
    }

    public void setIsPOSIntegrationTypeName(String isPOSIntegrationTypeName) {
        this.isPOSIntegrationTypeName = isPOSIntegrationTypeName;
    }

    public String getIntegrationParams() {
        return integrationParams;
    }

    public void setIntegrationParams(String integrationParams) {
        this.integrationParams = integrationParams;
    }
    
    
}
