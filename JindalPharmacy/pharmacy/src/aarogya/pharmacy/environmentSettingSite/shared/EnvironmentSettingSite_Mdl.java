package aarogya.pharmacy.environmentSettingSite.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EnvironmentSettingSite_Mdl extends KeyField implements Serializable, IsSerializable {

    private static String receiptLogo = "companyLogo";
    private String patientAlphabateCaseCode;
    private String patientAlphabateCaseName;
    private String addressAlphabateCaseCode;
    private String addressAlphabateCaseName;

    private String pharmacyFinalDiscountOHCode;
    private String pharmacyFinalDiscountOHName;

    private String pharmacyFinalDiscountAcCode;
    private String pharmacyFinalDiscountAcName;

    private String allowReturnWithoutSaleCode;
    private String allowReturnWithoutSaleName;

    private String defaultServiceTag;
    private String defaultServiceText;

    private String uName;
    private String date;
    private String time;
    private String isSiteCopy;
    private String serverFileName;
    private String ServerDirectory;
    private String destSiteCode;
    private String pharmacyControlAcTag;
    private String pharmacyControlAcText;
    private String medicineAlphabateCaseCode;
    private String medicineAlphabateCaseName;
//    private String prntyesnotag;
//    private String prntyesnotext;
    private String pharmacySelfAcTag;
    private String pharmacySelfAcText;
    private String pharmacyHospitalAcTag;
    private String pharmacyHospitalAcText;
//    private String orgReceiptCardTypeTag;
//    private String orgReceiptCardTypeText;
//    private String hospitalHoldCardTypeTag;
//    private String hospitalHoldCardTypeText;
    private String expAlertBeforeDays;
    private String medicineSaleOnText;
    private String medicineSaleOnTag;
    private String phAllowPatientForSiteText;
    private String phAllowPatientForSiteTag;
    private String phAllowSaleBillForSiteText;
    private String phAllowSaleBillForSiteTag;
    private boolean fileUploaded;
    private String returnDays;
    private String isIncludeTaxInCostTag;
    private String isIncludeTaxInCostText;
    private String pharmacyBarcodeActive;
    private String pharmacyBarcodeActiveName;
    private String autoInTransitByMedicineTransfer;
    private String autoInTransitByMedicineTransferName;
    private String ph_SRateEditInPurchase;
    private String ph_SRateEditInPurchasetext;
    private String pharmacyCentralStoreTag;
    private String pharmacyCentralStoreText;
    private List<GrdLinkDepartment_Mdl> grdLinkDept = new ArrayList<GrdLinkDepartment_Mdl>();

    public EnvironmentSettingSite_Mdl() {
    }

    public List<GrdLinkDepartment_Mdl> getGrdLinkDept() {
        return grdLinkDept;
    }

    public static String getReceiptLogo() {
        return receiptLogo;
    }

    public static void setReceiptLogo(String receiptLogo) {
        EnvironmentSettingSite_Mdl.receiptLogo = receiptLogo;
    }

    public String getPatientAlphabateCaseCode() {
        return patientAlphabateCaseCode;
    }

    public void setPatientAlphabateCaseCode(String patientAlphabateCaseCode) {
        this.patientAlphabateCaseCode = patientAlphabateCaseCode;
    }

    public String getPatientAlphabateCaseName() {
        return patientAlphabateCaseName;
    }

    public void setPatientAlphabateCaseName(String patientAlphabateCaseName) {
        this.patientAlphabateCaseName = patientAlphabateCaseName;
    }

    public String getAddressAlphabateCaseCode() {
        return addressAlphabateCaseCode;
    }

    public void setAddressAlphabateCaseCode(String addressAlphabateCaseCode) {
        this.addressAlphabateCaseCode = addressAlphabateCaseCode;
    }

    public String getAddressAlphabateCaseName() {
        return addressAlphabateCaseName;
    }

    public void setAddressAlphabateCaseName(String addressAlphabateCaseName) {
        this.addressAlphabateCaseName = addressAlphabateCaseName;
    }

    public String getPharmacyFinalDiscountOHCode() {
        return pharmacyFinalDiscountOHCode;
    }

    public void setPharmacyFinalDiscountOHCode(String pharmacyFinalDiscountOHCode) {
        this.pharmacyFinalDiscountOHCode = pharmacyFinalDiscountOHCode;
    }

    public String getPharmacyFinalDiscountOHName() {
        return pharmacyFinalDiscountOHName;
    }

    public void setPharmacyFinalDiscountOHName(String pharmacyFinalDiscountOHName) {
        this.pharmacyFinalDiscountOHName = pharmacyFinalDiscountOHName;
    }

    public String getPharmacyFinalDiscountAcCode() {
        return pharmacyFinalDiscountAcCode;
    }

    public void setPharmacyFinalDiscountAcCode(String pharmacyFinalDiscountAcCode) {
        this.pharmacyFinalDiscountAcCode = pharmacyFinalDiscountAcCode;
    }

    public String getPharmacyFinalDiscountAcName() {
        return pharmacyFinalDiscountAcName;
    }

    public void setPharmacyFinalDiscountAcName(String pharmacyFinalDiscountAcName) {
        this.pharmacyFinalDiscountAcName = pharmacyFinalDiscountAcName;
    }

    public String getAllowReturnWithoutSaleCode() {
        return allowReturnWithoutSaleCode;
    }

    public void setAllowReturnWithoutSaleCode(String allowReturnWithoutSaleCode) {
        this.allowReturnWithoutSaleCode = allowReturnWithoutSaleCode;
    }

    public String getAllowReturnWithoutSaleName() {
        return allowReturnWithoutSaleName;
    }

    public void setAllowReturnWithoutSaleName(String allowReturnWithoutSaleName) {
        this.allowReturnWithoutSaleName = allowReturnWithoutSaleName;
    }

    public String getDefaultServiceTag() {
        return defaultServiceTag;
    }

    public void setDefaultServiceTag(String defaultServiceTag) {
        this.defaultServiceTag = defaultServiceTag;
    }

    public String getDefaultServiceText() {
        return defaultServiceText;
    }

    public void setDefaultServiceText(String defaultServiceText) {
        this.defaultServiceText = defaultServiceText;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIsSiteCopy() {
        return isSiteCopy;
    }

    public void setIsSiteCopy(String isSiteCopy) {
        this.isSiteCopy = isSiteCopy;
    }

    public String getServerFileName() {
        return serverFileName;
    }

    public void setServerFileName(String serverFileName) {
        this.serverFileName = serverFileName;
    }

    public String getServerDirectory() {
        return ServerDirectory;
    }

    public void setServerDirectory(String ServerDirectory) {
        this.ServerDirectory = ServerDirectory;
    }

    public String getDestSiteCode() {
        return destSiteCode;
    }

    public void setDestSiteCode(String destSiteCode) {
        this.destSiteCode = destSiteCode;
    }

    public String getPharmacyControlAcTag() {
        return pharmacyControlAcTag;
    }

    public void setPharmacyControlAcTag(String pharmacyControlAcTag) {
        this.pharmacyControlAcTag = pharmacyControlAcTag;
    }

    public String getPharmacyControlAcText() {
        return pharmacyControlAcText;
    }

    public void setPharmacyControlAcText(String pharmacyControlAcText) {
        this.pharmacyControlAcText = pharmacyControlAcText;
    }

    public String getMedicineAlphabateCaseCode() {
        return medicineAlphabateCaseCode;
    }

    public void setMedicineAlphabateCaseCode(String medicineAlphabateCaseCode) {
        this.medicineAlphabateCaseCode = medicineAlphabateCaseCode;
    }

    public String getMedicineAlphabateCaseName() {
        return medicineAlphabateCaseName;
    }

    public void setMedicineAlphabateCaseName(String medicineAlphabateCaseName) {
        this.medicineAlphabateCaseName = medicineAlphabateCaseName;
    }

//    public String getPrntyesnotag() {
//        return prntyesnotag;
//    }
//
//    public void setPrntyesnotag(String prntyesnotag) {
//        this.prntyesnotag = prntyesnotag;
//    }
//
//    public String getPrntyesnotext() {
//        return prntyesnotext;
//    }
//
//    public void setPrntyesnotext(String prntyesnotext) {
//        this.prntyesnotext = prntyesnotext;
//    }
    public String getPharmacySelfAcTag() {
        return pharmacySelfAcTag;
    }

    public void setPharmacySelfAcTag(String pharmacySelfAcTag) {
        this.pharmacySelfAcTag = pharmacySelfAcTag;
    }

    public String getPharmacySelfAcText() {
        return pharmacySelfAcText;
    }

    public void setPharmacySelfAcText(String pharmacySelfAcText) {
        this.pharmacySelfAcText = pharmacySelfAcText;
    }

    public String getPharmacyHospitalAcTag() {
        return pharmacyHospitalAcTag;
    }

    public void setPharmacyHospitalAcTag(String pharmacyHospitalAcTag) {
        this.pharmacyHospitalAcTag = pharmacyHospitalAcTag;
    }

    public String getPharmacyHospitalAcText() {
        return pharmacyHospitalAcText;
    }

    public void setPharmacyHospitalAcText(String pharmacyHospitalAcText) {
        this.pharmacyHospitalAcText = pharmacyHospitalAcText;
    }

//    public String getOrgReceiptCardTypeTag() {
//        return orgReceiptCardTypeTag;
//    }
//
//    public void setOrgReceiptCardTypeTag(String orgReceiptCardTypeTag) {
//        this.orgReceiptCardTypeTag = orgReceiptCardTypeTag;
//    }
//
//    public String getOrgReceiptCardTypeText() {
//        return orgReceiptCardTypeText;
//    }
//
//    public void setOrgReceiptCardTypeText(String orgReceiptCardTypeText) {
//        this.orgReceiptCardTypeText = orgReceiptCardTypeText;
//    }
//
//    public String getHospitalHoldCardTypeTag() {
//        return hospitalHoldCardTypeTag;
//    }
//
//    public void setHospitalHoldCardTypeTag(String hospitalHoldCardTypeTag) {
//        this.hospitalHoldCardTypeTag = hospitalHoldCardTypeTag;
//    }
//
//    public String getHospitalHoldCardTypeText() {
//        return hospitalHoldCardTypeText;
//    }
//
//    public void setHospitalHoldCardTypeText(String hospitalHoldCardTypeText) {
//        this.hospitalHoldCardTypeText = hospitalHoldCardTypeText;
//    }
    public String getExpAlertBeforeDays() {
        return expAlertBeforeDays;
    }

    public void setExpAlertBeforeDays(String expAlertBeforeDays) {
        this.expAlertBeforeDays = expAlertBeforeDays;
    }

    public String getMedicineSaleOnText() {
        return medicineSaleOnText;
    }

    public void setMedicineSaleOnText(String medicineSaleOnText) {
        this.medicineSaleOnText = medicineSaleOnText;
    }

    public String getMedicineSaleOnTag() {
        return medicineSaleOnTag;
    }

    public void setMedicineSaleOnTag(String medicineSaleOnTag) {
        this.medicineSaleOnTag = medicineSaleOnTag;
    }

    public String getPhAllowPatientForSiteText() {
        return phAllowPatientForSiteText;
    }

    public void setPhAllowPatientForSiteText(String phAllowPatientForSiteText) {
        this.phAllowPatientForSiteText = phAllowPatientForSiteText;
    }

    public String getPhAllowPatientForSiteTag() {
        return phAllowPatientForSiteTag;
    }

    public void setPhAllowPatientForSiteTag(String phAllowPatientForSiteTag) {
        this.phAllowPatientForSiteTag = phAllowPatientForSiteTag;
    }

    public String getPhAllowSaleBillForSiteText() {
        return phAllowSaleBillForSiteText;
    }

    public void setPhAllowSaleBillForSiteText(String phAllowSaleBillForSiteText) {
        this.phAllowSaleBillForSiteText = phAllowSaleBillForSiteText;
    }

    public String getPhAllowSaleBillForSiteTag() {
        return phAllowSaleBillForSiteTag;
    }

    public void setPhAllowSaleBillForSiteTag(String phAllowSaleBillForSiteTag) {
        this.phAllowSaleBillForSiteTag = phAllowSaleBillForSiteTag;
    }

    public boolean isFileUploaded() {
        return fileUploaded;
    }

    public void setFileUploaded(boolean fileUploaded) {
        this.fileUploaded = fileUploaded;
    }

    public String getReturnDays() {
        return returnDays;
    }

    public void setReturnDays(String returnDays) {
        this.returnDays = returnDays;
    }

    public String getIsIncludeTaxInCostTag() {
        return isIncludeTaxInCostTag;
    }

    public void setIsIncludeTaxInCostTag(String isIncludeTaxInCostTag) {
        this.isIncludeTaxInCostTag = isIncludeTaxInCostTag;
    }

    public String getIsIncludeTaxInCostText() {
        return isIncludeTaxInCostText;
    }

    public void setIsIncludeTaxInCostText(String isIncludeTaxInCostText) {
        this.isIncludeTaxInCostText = isIncludeTaxInCostText;
    }

    public String getPharmacyBarcodeActive() {
        return pharmacyBarcodeActive;
    }

    public void setPharmacyBarcodeActive(String pharmacyBarcodeActive) {
        this.pharmacyBarcodeActive = pharmacyBarcodeActive;
    }

    public String getPharmacyBarcodeActiveName() {
        return pharmacyBarcodeActiveName;
    }

    public void setPharmacyBarcodeActiveName(String pharmacyBarcodeActiveName) {
        this.pharmacyBarcodeActiveName = pharmacyBarcodeActiveName;
    }

    public String getAutoInTransitByMedicineTransfer() {
        return autoInTransitByMedicineTransfer;
    }

    public void setAutoInTransitByMedicineTransfer(String autoInTransitByMedicineTransfer) {
        this.autoInTransitByMedicineTransfer = autoInTransitByMedicineTransfer;
    }

    public String getAutoInTransitByMedicineTransferName() {
        return autoInTransitByMedicineTransferName;
    }

    public void setAutoInTransitByMedicineTransferName(String autoInTransitByMedicineTransferName) {
        this.autoInTransitByMedicineTransferName = autoInTransitByMedicineTransferName;
    }

    public String getPh_SRateEditInPurchase() {
        return ph_SRateEditInPurchase;
    }

    public void setPh_SRateEditInPurchase(String ph_SRateEditInPurchase) {
        this.ph_SRateEditInPurchase = ph_SRateEditInPurchase;
    }

    public String getPh_SRateEditInPurchasetext() {
        return ph_SRateEditInPurchasetext;
    }

    public void setPh_SRateEditInPurchasetext(String ph_SRateEditInPurchasetext) {
        this.ph_SRateEditInPurchasetext = ph_SRateEditInPurchasetext;
    }

    public String getPharmacyCentralStoreTag() {
        return pharmacyCentralStoreTag;
    }

    public void setPharmacyCentralStoreTag(String pharmacyCentralStoreTag) {
        this.pharmacyCentralStoreTag = pharmacyCentralStoreTag;
    }

    public String getPharmacyCentralStoreText() {
        return pharmacyCentralStoreText;
    }

    public void setPharmacyCentralStoreText(String pharmacyCentralStoreText) {
        this.pharmacyCentralStoreText = pharmacyCentralStoreText;
    }

}
