package aarogya.pharmacy.purchaseOrder.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class DefaultValue_Mdl implements Serializable, IsSerializable {

    private String voucherTypeCode;
    private String voucherTypeName;
    private String currentDate;
    private String guiTemplateCode;
    //New Fields
    private boolean isContinuousNewMode = false;
    private String newVoucherTypeCode;
    private String newVoucherTypeName;
    private String newGuiTemplateCode;
    private String newVoucherDate;
    private String indentRequired;

    private String siteStateCode;
    private String isSiteStateUT;
    private String siteGstin;
    private String siteGstinDate;
    private String siteRegistrationType;
    private boolean gstApplicable = false;
    private String requiredLastBillDiscountInPO;
    private String isSystemAdminUser;
    private String userName;

    public String getGuiTemplateCode() {
        return guiTemplateCode;
    }

    public void setGuiTemplateCode(String guiTemplateCode) {
        this.guiTemplateCode = guiTemplateCode;
    }

    public DefaultValue_Mdl() {
    }

    public String getVoucherTypeCode() {
        return voucherTypeCode;
    }

    public void setVoucherTypeCode(String voucherTypeCode) {
        this.voucherTypeCode = voucherTypeCode;
    }

    public String getVoucherTypeName() {
        return voucherTypeName;
    }

    public void setVoucherTypeName(String voucherTypeName) {
        this.voucherTypeName = voucherTypeName;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public boolean getIsContinuousNewMode() {
        return isContinuousNewMode;
    }

    public void setIsContinuousNewMode(boolean isContinuousNewMode) {
        this.isContinuousNewMode = isContinuousNewMode;
    }

    public String getNewVoucherTypeCode() {
        return newVoucherTypeCode;
    }

    public void setNewVoucherTypeCode(String newVoucherTypeCode) {
        this.newVoucherTypeCode = newVoucherTypeCode;
    }

    public String getNewVoucherTypeName() {
        return newVoucherTypeName;
    }

    public void setNewVoucherTypeName(String newVoucherTypeName) {
        this.newVoucherTypeName = newVoucherTypeName;
    }

    public String getNewGuiTemplateCode() {
        return newGuiTemplateCode;
    }

    public void setNewGuiTemplateCode(String newGuiTemplateCode) {
        this.newGuiTemplateCode = newGuiTemplateCode;
    }

    public String getNewVoucherDate() {
        return newVoucherDate;
    }

    public void setNewVoucherDate(String newVoucherDate) {
        this.newVoucherDate = newVoucherDate;
    }

    public String getIndentRequired() {
        return indentRequired;
    }

    public void setIndentRequired(String indentRequired) {
        this.indentRequired = indentRequired;
    }

    public String getSiteStateCode() {
        return siteStateCode;
    }

    public void setSiteStateCode(String siteStateCode) {
        this.siteStateCode = siteStateCode;
    }

    public String getIsSiteStateUT() {
        return isSiteStateUT;
    }

    public void setIsSiteStateUT(String isSiteStateUT) {
        this.isSiteStateUT = isSiteStateUT;
    }

    public String getSiteGstin() {
        return siteGstin;
    }

    public void setSiteGstin(String siteGstin) {
        this.siteGstin = siteGstin;
    }

    public String getSiteGstinDate() {
        return siteGstinDate;
    }

    public void setSiteGstinDate(String siteGstinDate) {
        this.siteGstinDate = siteGstinDate;
    }

    public String getSiteRegistrationType() {
        return siteRegistrationType;
    }

    public void setSiteRegistrationType(String siteRegistrationType) {
        this.siteRegistrationType = siteRegistrationType;
    }

    public boolean isGstApplicable() {
        return gstApplicable;
    }

    public void setGstApplicable(boolean gstApplicable) {
        this.gstApplicable = gstApplicable;
    }

    public String getRequiredLastBillDiscountInPO() {
        return requiredLastBillDiscountInPO;
    }

    public void setRequiredLastBillDiscountInPO(String requiredLastBillDiscountInPO) {
        this.requiredLastBillDiscountInPO = requiredLastBillDiscountInPO;
    }

    public String getIsSystemAdminUser() {
        return isSystemAdminUser;
    }

    public void setIsSystemAdminUser(String isSystemAdminUser) {
        this.isSystemAdminUser = isSystemAdminUser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
