package aarogya.inventory.purchaseBillDirect.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class DefaultValue_Mdl implements Serializable, IsSerializable {

    private TxtHVoucherType_Mdl voucherType = new TxtHVoucherType_Mdl();
    private TxtHVoucherType_Mdl newVoucherType = new TxtHVoucherType_Mdl();
    private String departmentCode;
    private String departmentName;
    private String currentDate;
    private String siteCode;
    private String siteStateCode;
    private String isSiteStateUT;
    private String siteGstin;
    private String siteGstinDate;
    private String siteRegistrationType;
    private String newVoucherDate;
    private boolean isContinuousNewMode = false;
    private boolean gstApplicable = false;
    private boolean inventoryAccountingActive = false;
    private boolean includeTaxInCost = false;
    private boolean includeOverHeadsInCost = false;
    private String finalDicountAcCode;
    private String roundOffAcCode;
    private String purchaseAcPostingBasedOn;
    private boolean singlePOFlowInPurchase = false;
    private String balancingAc;
    private String defaultPurchaseDepartment;
    private String defaultPurchaseDepartmentText;
    private String tdsOn;

    public DefaultValue_Mdl() {
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

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
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

    public String getNewVoucherDate() {
        return newVoucherDate;
    }

    public void setNewVoucherDate(String newVoucherDate) {
        this.newVoucherDate = newVoucherDate;
    }

    public boolean getIsContinuousNewMode() {
        return isContinuousNewMode;
    }

    public void setIsContinuousNewMode(boolean isContinuousNewMode) {
        this.isContinuousNewMode = isContinuousNewMode;
    }

    public boolean isGstApplicable() {
        return gstApplicable;
    }

    public void setGstApplicable(boolean gstApplicable) {
        this.gstApplicable = gstApplicable;
    }

    public boolean isInventoryAccountingActive() {
        return inventoryAccountingActive;
    }

    public void setInventoryAccountingActive(boolean inventoryAccountingActive) {
        this.inventoryAccountingActive = inventoryAccountingActive;
    }

    public boolean isIncludeTaxInCost() {
        return includeTaxInCost;
    }

    public void setIncludeTaxInCost(boolean includeTaxInCost) {
        this.includeTaxInCost = includeTaxInCost;
    }

    public boolean isIncludeOverHeadsInCost() {
        return includeOverHeadsInCost;
    }

    public void setIncludeOverHeadsInCost(boolean includeOverHeadsInCost) {
        this.includeOverHeadsInCost = includeOverHeadsInCost;
    }

    public String getFinalDicountAcCode() {
        return finalDicountAcCode;
    }

    public void setFinalDicountAcCode(String finalDicountAcCode) {
        this.finalDicountAcCode = finalDicountAcCode;
    }

    public String getRoundOffAcCode() {
        return roundOffAcCode;
    }

    public void setRoundOffAcCode(String roundOffAcCode) {
        this.roundOffAcCode = roundOffAcCode;
    }

    public String getPurchaseAcPostingBasedOn() {
        return purchaseAcPostingBasedOn;
    }

    public void setPurchaseAcPostingBasedOn(String purchaseAcPostingBasedOn) {
        this.purchaseAcPostingBasedOn = purchaseAcPostingBasedOn;
    }

    public boolean isSinglePOFlowInPurchase() {
        return singlePOFlowInPurchase;
    }

    public void setSinglePOFlowInPurchase(boolean singlePOFlowInPurchase) {
        this.singlePOFlowInPurchase = singlePOFlowInPurchase;
    }

    public String getBalancingAc() {
        return balancingAc;
    }

    public void setBalancingAc(String balancingAc) {
        this.balancingAc = balancingAc;
    }

    public String getDefaultPurchaseDepartment() {
        return defaultPurchaseDepartment;
    }

    public void setDefaultPurchaseDepartment(String defaultPurchaseDepartment) {
        this.defaultPurchaseDepartment = defaultPurchaseDepartment;
    }

    public String getDefaultPurchaseDepartmentText() {
        return defaultPurchaseDepartmentText;
    }

    public void setDefaultPurchaseDepartmentText(String defaultPurchaseDepartmentText) {
        this.defaultPurchaseDepartmentText = defaultPurchaseDepartmentText;
    }

    public String getTdsOn() {
        return tdsOn;
    }

    public void setTdsOn(String tdsOn) {
        this.tdsOn = tdsOn;
    }
    
    

}
