package aarogya.inventory.purchaseOrder.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class DefaultValue_Mdl implements Serializable, IsSerializable {

    private String currentDate;
    private String guiTemplateCode;
    //New Fields
    private boolean isContinuousNewMode = false;
    private String newVoucherTypeCode;
    private String newVoucherTypeName;
    private String newGuiTemplateCode;
    private String newVoucherDate;

    //====================<< *********** >>=====================
    private TxtHVoucherType_Mdl voucherType = new TxtHVoucherType_Mdl();
    private TxtHVoucherType_Mdl newVoucherType = new TxtHVoucherType_Mdl();
    private String siteCode;
    private String siteStateCode;
    private String siteRegistrationType;
    private String siteGstinDate;
    private String isSiteStateUT;
    private String isSystemAdminUser;
    private String userName;
    private boolean gstApplicable = false;
    private boolean includeTaxInCost = false;
    private boolean inventoryAccountingActive = false;

    public DefaultValue_Mdl() {
    }

    public boolean isIncludeTaxInCost() {
        return includeTaxInCost;
    }

    public void setIncludeTaxInCost(boolean includeTaxInCost) {
        this.includeTaxInCost = includeTaxInCost;
    }

    public boolean isInventoryAccountingActive() {
        return inventoryAccountingActive;
    }

    public void setInventoryAccountingActive(boolean inventoryAccountingActive) {
        this.inventoryAccountingActive = inventoryAccountingActive;
    }
    
    public boolean isIsContinuousNewMode() {
        return isContinuousNewMode;
    }

    public void setIsContinuousNewMode(boolean isContinuousNewMode) {
        this.isContinuousNewMode = isContinuousNewMode;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getGuiTemplateCode() {
        return guiTemplateCode;
    }

    public void setGuiTemplateCode(String guiTemplateCode) {
        this.guiTemplateCode = guiTemplateCode;
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

    public TxtHVoucherType_Mdl getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(TxtHVoucherType_Mdl voucherType) {
        this.voucherType = voucherType;
    }

    public TxtHVoucherType_Mdl getNewVoucherType() {
        return newVoucherType;
    }

    public void setNewVoucherType(TxtHVoucherType_Mdl newVoucherType) {
        this.newVoucherType = newVoucherType;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getSiteStateCode() {
        return siteStateCode;
    }

    public void setSiteStateCode(String siteStateCode) {
        this.siteStateCode = siteStateCode;
    }

    public String getSiteRegistrationType() {
        return siteRegistrationType;
    }

    public void setSiteRegistrationType(String siteRegistrationType) {
        this.siteRegistrationType = siteRegistrationType;
    }

    public String getSiteGstinDate() {
        return siteGstinDate;
    }

    public void setSiteGstinDate(String siteGstinDate) {
        this.siteGstinDate = siteGstinDate;
    }

    public String getIsSiteStateUT() {
        return isSiteStateUT;
    }

    public void setIsSiteStateUT(String isSiteStateUT) {
        this.isSiteStateUT = isSiteStateUT;
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

    public boolean isGstApplicable() {
        return gstApplicable;
    }

    public void setGstApplicable(boolean gstApplicable) {
        this.gstApplicable = gstApplicable;
    }

}
