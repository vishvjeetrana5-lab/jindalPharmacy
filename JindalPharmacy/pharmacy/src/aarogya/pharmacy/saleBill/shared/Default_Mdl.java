package aarogya.pharmacy.saleBill.shared;

import aarogya.selfToolkit.cardPaymentPopup.shared.CardPaymentPopup_Mdl;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import aarogya.selfToolkit.uhidPopup.shared.Layout_Mdl;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Default_Mdl implements Serializable, IsSerializable {

    private CardPaymentPopup_Mdl cardPayment = new CardPaymentPopup_Mdl();
    private TxtHVType_Mdl voucherTypeMdl;
    private String controlAccount, roundOffAcCode;
    private String departmentCode, departmentName;
    private String siteCode;
    private String siteShortName;
    private String stateCode;
    private STEnum.MedicineSaleOn medicineSaleOn;
    private String partyType;
    private String expiryAlertBeforeDays;
    private String patientAlphabetCase;
    private String addressAlphabetCase;
    private String defaultPharmacyServiceCode;
    private String allowReturnWithoutSale;
    private String pharmacyBarcodeActive;
    private String requisitionWithoutUHID;
    private boolean estimateMandatoryInSaleBill;

    private List<Layout_Mdl> lstLayout = new ArrayList<Layout_Mdl>();
    private boolean unionTerritory = false;
    private boolean includeTaxInCost = false;
    private boolean gstApplicable = false;
    private String backDateForEstimateHelpInSaleBill;

    public String getSiteShortName() {
        return siteShortName;
    }

    public void setSiteShortName(String siteShortName) {
        this.siteShortName = siteShortName;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getDefaultPharmacyServiceCode() {
        return defaultPharmacyServiceCode;
    }

    public void setDefaultPharmacyServiceCode(String defaultPharmacyServiceCode) {
        this.defaultPharmacyServiceCode = defaultPharmacyServiceCode;
    }

    public String getAddressAlphabetCase() {
        return addressAlphabetCase;
    }

    public void setAddressAlphabetCase(String addressAlphabetCase) {
        this.addressAlphabetCase = addressAlphabetCase;
    }

    public String getPatientAlphabetCase() {
        return patientAlphabetCase;
    }

    public void setPatientAlphabetCase(String patientAlphabetCase) {
        this.patientAlphabetCase = patientAlphabetCase;
    }

    public STEnum.MedicineSaleOn getMedicineSaleOn() {
        return medicineSaleOn;
    }

    public void setMedicineSaleOn(STEnum.MedicineSaleOn medicineSaleOn) {
        this.medicineSaleOn = medicineSaleOn;
    }

    public String getExpiryAlertBeforeDays() {
        return expiryAlertBeforeDays;
    }

    public void setExpiryAlertBeforeDays(String expiryAlertBeforeDays) {
        this.expiryAlertBeforeDays = expiryAlertBeforeDays;
    }

    public Default_Mdl() {
    }

    public String getPartyType() {
        return partyType;
    }

    public void setPartyType(String partyType) {
        this.partyType = partyType;
    }

    public List<Layout_Mdl> getLstLayout() {
        return lstLayout;
    }

    public TxtHVType_Mdl getVoucherTypeMdl() {
        return voucherTypeMdl;
    }

    public void setVoucherTypeMdl(TxtHVType_Mdl voucherTypeMdl) {
        this.voucherTypeMdl = voucherTypeMdl;
    }

    public CardPaymentPopup_Mdl getCardPayment() {
        return cardPayment;
    }

    public void setCardPayment(CardPaymentPopup_Mdl cardPayment) {
        this.cardPayment = cardPayment;
    }

    public String getControlAccount() {
        return controlAccount;
    }

    public void setControlAccount(String controlAccount) {
        this.controlAccount = controlAccount;
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

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public boolean isUnionTerritory() {
        return unionTerritory;
    }

    public void setUnionTerritory(boolean unionTerritory) {
        this.unionTerritory = unionTerritory;
    }

    public String getRoundOffAcCode() {
        return roundOffAcCode;
    }

    public void setRoundOffAcCode(String roundOffAcCode) {
        this.roundOffAcCode = roundOffAcCode;
    }

    public String getAllowReturnWithoutSale() {
        return allowReturnWithoutSale;
    }

    public void setAllowReturnWithoutSale(String allowReturnWithoutSale) {
        this.allowReturnWithoutSale = allowReturnWithoutSale;
    }

    public boolean isIncludeTaxInCost() {
        return includeTaxInCost;
    }

    public void setIncludeTaxInCost(boolean includeTaxInCost) {
        this.includeTaxInCost = includeTaxInCost;
    }

    public String getPharmacyBarcodeActive() {
        return pharmacyBarcodeActive;
    }

    public void setPharmacyBarcodeActive(String pharmacyBarcodeActive) {
        this.pharmacyBarcodeActive = pharmacyBarcodeActive;
    }

    public boolean isGstApplicable() {
        return gstApplicable;
    }

    public void setGstApplicable(boolean gstApplicable) {
        this.gstApplicable = gstApplicable;
    }

    public String getRequisitionWithoutUHID() {
        return requisitionWithoutUHID;
    }

    public void setRequisitionWithoutUHID(String requisitionWithoutUHID) {
        this.requisitionWithoutUHID = requisitionWithoutUHID;
    }

    public String getBackDateForEstimateHelpInSaleBill() {
        return backDateForEstimateHelpInSaleBill;
    }

    public void setBackDateForEstimateHelpInSaleBill(String backDateForEstimateHelpInSaleBill) {
        this.backDateForEstimateHelpInSaleBill = backDateForEstimateHelpInSaleBill;
    }

    public boolean isEstimateMandatoryInSaleBill() {
        return estimateMandatoryInSaleBill;
    }

    public void setEstimateMandatoryInSaleBill(boolean estimateMandatoryInSaleBill) {
        this.estimateMandatoryInSaleBill = estimateMandatoryInSaleBill;
    }

}
