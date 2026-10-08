package aarogya.pharmacy.d365StockRecive.shared;

import aarogya.selfToolkit.stStatic.shared.SaleRateRule_Mdl;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DefaultValue_Mdl implements Serializable, IsSerializable {

    private String voucherTypeCode;
    private String voucherTypeName;
    private String voucherCategory;
    private String balancingAc;
    private String balancingAcName;
    private String guiTemplateCode;
    private String departmentCode;
    private String departmentName;
    private String currentDate;
    private String siteCode;
    private String siteStateCode;
    private String isSiteStateUT;
    private String siteGstin;
    private String siteGstinDate;
    private String siteRegistrationType;
    private List<SaleRateRule_Mdl> lstSaleRateRule = new ArrayList<>();
    //New Fields
    private boolean isContinuousNewMode = false;
    private String newVoucherTypeCode;
    private String newVoucherTypeName;
    private String newVoucherTypeCategory;
    private String newBalancingAc;
    private String newBalancingAcName;
    private String newGuiTemplateCode;
    private String newVoucherDate;
    private boolean gstApplicable = false;
    private boolean singlePOFlowInPurchase = false;

    private boolean includeTaxInCost = false;
    private boolean inventoryAccountingActive = false;
    private boolean ph_SRateEditInPurchase = false;
    private boolean calculateSaleRate_On_TaxInclusiveValue = false;
    private String purchaseAcPostingBasedOn;
    
    private String defaultPurchaseDepartment;
    private String defaultPurchaseDepartmentText;

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

    public String getGuiTemplateCode() {
        return guiTemplateCode;
    }

    public void setGuiTemplateCode(String guiTemplateCode) {
        this.guiTemplateCode = guiTemplateCode;
    }

    public String getVoucherCategory() {
        return voucherCategory;
    }

    public void setVoucherCategory(String voucherCategory) {
        this.voucherCategory = voucherCategory;
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

    public List<SaleRateRule_Mdl> getLstSaleRateRule() {
        return lstSaleRateRule;
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

    public String getNewVoucherTypeCategory() {
        return newVoucherTypeCategory;
    }

    public void setNewVoucherTypeCategory(String newVoucherTypeCategory) {
        this.newVoucherTypeCategory = newVoucherTypeCategory;
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

    public boolean isGstApplicable() {
        return gstApplicable;
    }

    public void setGstApplicable(boolean gstApplicable) {
        this.gstApplicable = gstApplicable;
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

    public String getNewBalancingAc() {
        return newBalancingAc;
    }

    public void setNewBalancingAc(String newBalancingAc) {
        this.newBalancingAc = newBalancingAc;
    }

    public String getBalancingAcName() {
        return balancingAcName;
    }

    public void setBalancingAcName(String balancingAcName) {
        this.balancingAcName = balancingAcName;
    }

    public String getNewBalancingAcName() {
        return newBalancingAcName;
    }

    public void setNewBalancingAcName(String newBalancingAcName) {
        this.newBalancingAcName = newBalancingAcName;
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

    public String getPurchaseAcPostingBasedOn() {
        return purchaseAcPostingBasedOn;
    }

    public void setPurchaseAcPostingBasedOn(String purchaseAcPostingBasedOn) {
        this.purchaseAcPostingBasedOn = purchaseAcPostingBasedOn;
    }

    public boolean isPh_SRateEditInPurchase() {
        return ph_SRateEditInPurchase;
    }

    public void setPh_SRateEditInPurchase(boolean ph_SRateEditInPurchase) {
        this.ph_SRateEditInPurchase = ph_SRateEditInPurchase;
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

    public boolean isCalculateSaleRate_On_TaxInclusiveValue() {
        return calculateSaleRate_On_TaxInclusiveValue;
    }

    public void setCalculateSaleRate_On_TaxInclusiveValue(boolean calculateSaleRate_On_TaxInclusiveValue) {
        this.calculateSaleRate_On_TaxInclusiveValue = calculateSaleRate_On_TaxInclusiveValue;
    }

}
