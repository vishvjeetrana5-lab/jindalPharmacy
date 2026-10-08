package aarogya.inventory.quotation.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class DefaultValue_Mdl implements Serializable, IsSerializable {

    private String currentDate;

    //New Fields
    private boolean isContinuousNewMode = false;

    private String siteCode;
    private String siteStateCode;
    private String siteRegistrationType;
    private String siteGstinDate;
    private String isSiteStateUT;
    private boolean gstApplicable = false;
    private boolean includeTaxInCost = false;
    private boolean inventoryAccountingActive = false;
    private boolean isCommonPriceList = false;
    private boolean updatePriceListFromQuotation = false;
    private TxtHVoucherType_Mdl voucherType = new TxtHVoucherType_Mdl();
    private TxtHVoucherType_Mdl newVoucherType = new TxtHVoucherType_Mdl();

    public DefaultValue_Mdl() {
    }

    public String getSiteCode() {
        return siteCode;
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

    public boolean isGstApplicable() {
        return gstApplicable;
    }

    public void setGstApplicable(boolean gstApplicable) {
        this.gstApplicable = gstApplicable;
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

    public boolean isIsCommonPriceList() {
        return isCommonPriceList;
    }

    public void setIsCommonPriceList(boolean isCommonPriceList) {
        this.isCommonPriceList = isCommonPriceList;
    }

    public boolean isUpdatePriceListFromQuotation() {
        return updatePriceListFromQuotation;
    }

    public void setUpdatePriceListFromQuotation(boolean updatePriceListFromQuotation) {
        this.updatePriceListFromQuotation = updatePriceListFromQuotation;
    }

}
