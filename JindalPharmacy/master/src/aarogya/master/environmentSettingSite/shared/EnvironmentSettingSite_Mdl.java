package aarogya.master.environmentSettingSite.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EnvironmentSettingSite_Mdl extends KeyField implements Serializable, IsSerializable {

    private static String receiptLogo = "companyLogo";
    private String isBillHApprovalTag;
    private String isBillHApprovalText;
    private String patientAlphabateCaseCode;
    private String patientAlphabateCaseName;
    private String addressAlphabateCaseCode;
    private String addressAlphabateCaseName;
    private String opdCaseCode;
    private String opdCaseName;
    private String opdRefConsultantCode;
    private String opdRefConsultantName;
    private String opdConsultantCode;
    private String opdConsultantName;
    private String directPrescriptionTag;
    private String directPrescriptionText;
    private String opdIsReligionMandatory;
    private String opdIsReligionMandatoryName;
    private String opdIsIdTypeMandatory;
    private String opdIsIdTypeMandatoryName;
    private String uName;
    private String date;
    private String time;
    private String defaultRegFee;
    private String includeDDInAge;
    private String includeMMInAge;
    private String documentFooter;
    private String documentFooterRegional;
    private String documentHeader;
    private String isSiteCopy;
    private String serverFileName;
    private String ServerDirectory;
    private String destSiteCode;
    private String cityCode;
    private String cityName;
    private String nationalityTag;
    private String nationalityText;
    private String ipdInfantUnitTag;
    private String ipdInfantUnitText;
    private String ipdIsRoomCleanTag;
    private String ipdIsRoomCleanText;
    private String ipdIsAutoRoomTag;
    private String ipdIsAutoRoomText;
    private String ipdTimeFormatRoomTag;
    private String ipdTimeFormatRoomText;
    private String ipdCheckoutTime;
    private String ipdGraceMinuteIn;
    private String ipdGraceMinuteOut;
    private String ipdHalfDayUpToMin;
    private String ipdHalfDayPercentage;
    private String ipdControlAcTag;
    private String ipdControlAcText;
    private String eodPostingTypeCode;
    private String eodPostingTypeName;
    private String uhidDefaultCardTypeCode;
    private String uhidDefaultCardName;

    private String opdControlAcTag;
    private String opdControlAcText;
    private String tokenBlockTag;
    private String tokenBlockText;
    private String diagnosticControlAcTag;
    private String diagnosticControlAcText;
    private String opdNoOfSticker;
    private String ipdNoOfSticker;
    private String diagnosticNoOfSticker;
    private String diagnosticSampleCollectionText;
    private String diagnosticSampleCollectionTag;
    private String isExistingPatientTag;
    private String isExistingPatientText;
    private String isLABExistingPatientTag;
    private String isLABExistingPatientText;
    private String isPatientInitialTag;
    private String isPatientInitialText;
    private String isAddressText;
    private String isAddressTag;
    private String isGuardianInitialText;
    private String isGuardianInitialTag;
    private String isCityMandatoryText;
    private String isCityMandatoryTag;
    private String isMaritalStatusText;
    private String isMaritalStatusTag;
    private String isCasteTag;
    private String isCasteText;
    private String isBloodGroupTag;
    private String isBloodGroupText;
    private String isEmailTag;
    private String isEmailText;
    private String emergencyControlAcTag;
    private String emergencyControlAcText;
    private String dayCareControlAcTag;
    private String dayCareControlAcText;
    private String diagnosticIsDiagAutoTag;
    private String diagnosticIsDiagAutoText;
    private String prntyesnotag;
    private String prntyesnotext;
    private String panCardLimit;
    private String cashPaymentLimit;
    private String roomMandatoryTag;
    private String roomMandatoryText;
    private String nightshiftFrom;
    private String nightshiftTo;
    private String cancelDaysOPD;
    private String serviceDaysOPD;
    private String NewBornDay;
    private String NewBornMonth;
    private String NewBornYear;
    private String InfantMonth;
    private String InfantYear;
    private String ChildMonth;
    private String ChildYear;
    private String diagnosticSelfAcTag;
    private String diagnosticSelfAcText;
    private String diagnosticHospitalAcTag;
    private String diagnosticHospitalAcText;
    private String diagnosticRegAdditionText;
    private String diagnosticRegAdditionTag;

    private String secodaryCurrencyText;
    private String secodaryCurrencyTag;

    private String orgReceiptCardTypeTag;
    private String orgReceiptCardTypeText;
    private String hospitalHoldCardTypeTag;
    private String hospitalHoldCardTypeText;
    private String isPatientImgMandatoryText;
    private String isPatientImgMandatoryTag;
    private String isAreaMandatoryText;
    private String isAreaMandatoryTag;
    private String isGuardianNameMandatoryText;
    private String isGuardianNameMandatoryTag;
    private String diagIsPrintOnLetterheadText;
    private String diagIsPrintOnLetterheadTag;
    private String hourlyServiceRoundOffText;
    private String hourlyServiceRoundOffTag;
    private String disallowTypeText;
    private String disallowTypeTag;
    private String dietScheduleDays;
    private String dietaryProperty1;
    private String dietaryProperty2;
    private String dietaryProperty3;
    private String dietaryProperty4;
    private String dietaryProperty5;
    private String dietaryProperty6;
    private String opdAllowPatientForSiteText;
    private String opdAllowPatientForSiteTag;
    private String ipdAllowPatientForSiteText;
    private String ipdAllowPatientForSiteTag;
    private String daycAllowPatientForSiteText;
    private String daycAllowPatientForSiteTag;
    private String diagAllowPatientForSiteText;
    private String diagAllowPatientForSiteTag;
    private String defaultOpdPharmacyDepartmentTag;
    private String defaultOpdPharmacyDepartmentText;
    private String packageCode;
    private String packageName;
    private String diagnosticCancelDays;
    private String diagnosticDays;
    private boolean fileUploaded;
    private String defaultPharmacySiteTag;
    private String defaultPharmacySiteText;
    private String appointmentHistoryViewDays;
    private String historyViewDays;
    private String labHistoryViewDays;
    private String bloodBagDepartmentCode;
    private String bloodComponentDepartmentCode;
    private String bloodBagDepartmentText;
    private String bloodComponentDepartmentText;

    private String bloodBankCipAdditionText;
    private String bloodBankCipAdditionTag;

    private String noOfVisit;
    private String validUpTo;
    private String queueTag, queueText;
    private String consultationRegistrationTag, consultationRegistrationText;
    private String donorDefaultCampCode;
    private String donorDefaultCampName;
    private String isReceiptGenerateWithFullDiscountText;
    private String isReceiptGenerateWithFullDiscountTag;
    private String isOtpRequiredForRefundText;
    private String isOtpRequiredForRefundTag;
    private String isIcdxMandatoryText;
    private String isIcdxMandatoryTag;

    private String appointmentBookUptoDays;
    private String appointmentCountMonthwise;
    private String appointmentPaymentGatewayAllowedTag;
    private String appointmentPaymentGatewayAllowedText;
    private String paymentGatewayURL;
    private String paymentOptionTag;
    private String paymentOptionText;
    private String autoOPDRegAfterAppointmentTag;
    private String autoOPDRegAfterAppointmentText;
    private String runFromWebsiteTag;
    private String runFromWebsiteText;
    private String serviceUnitEditableName;

    private String serviceUnitEditableCode;
    private String includeTaxInCost;
    private String purchaseSalesAcPostingBasedOnCode;
    private String purchaseSalesAcPostingBasedOnName;
    private String indentRequired;
    private String requisitionMandatoryInTransfer;
    private String singlePOFlowInPurchase;
    private String autoBedConfirmation;
    private String IsCommonPricelistForSiteTag;
    private String IsCommonPricelistForSiteText;

    private String inv_DefaultPurchaseDepartment;
    private String inv_DefaultPurchaseDepartmentText;
    
    private String asset_DepreciationAcTag;
    private String asset_DepreciationAcText;
    private String asset_DepreciationPostOn;
    private String asset_DepreciationPostOnText;
    private String transactionLockDate;
    private String transactionLockYear;
    

    private List<GrdLinkDepartment_Mdl> grdLinkDept = new ArrayList<GrdLinkDepartment_Mdl>();
    private List<GrdStatusGUITemp_Mdl> grdStatusTemplate = new ArrayList<GrdStatusGUITemp_Mdl>();
    private List<GrdDiagnosticFile_Mdl> grdDiagnosticFilelist = new ArrayList<GrdDiagnosticFile_Mdl>();
    private List<GrdQueueSequence_Mdl> lstQueue = new ArrayList<GrdQueueSequence_Mdl>();

    public EnvironmentSettingSite_Mdl() {
    }

    public String getIsBillHApprovalText() {
        return isBillHApprovalText;
    }

    public void setIsBillHApprovalText(String isBillHApprovalText) {
        this.isBillHApprovalText = isBillHApprovalText;
    }

    public String getIsBillHApprovalTag() {
        return isBillHApprovalTag;
    }

    public void setIsBillHApprovalTag(String isBillHApprovalTag) {
        this.isBillHApprovalTag = isBillHApprovalTag;
    }

    public String getBloodBagDepartmentText() {
        return bloodBagDepartmentText;
    }

    public void setBloodBagDepartmentText(String bloodBagDepartmentText) {
        this.bloodBagDepartmentText = bloodBagDepartmentText;
    }

    public String getBloodComponentDepartmentText() {
        return bloodComponentDepartmentText;
    }

    public void setBloodComponentDepartmentText(String bloodComponentDepartmentText) {
        this.bloodComponentDepartmentText = bloodComponentDepartmentText;
    }

    public String getBloodBagDepartmentCode() {
        return bloodBagDepartmentCode;
    }

    public void setBloodBagDepartmentCode(String bloodBagDepartmentCode) {
        this.bloodBagDepartmentCode = bloodBagDepartmentCode;
    }

    public String getBloodComponentDepartmentCode() {
        return bloodComponentDepartmentCode;
    }

    public void setBloodComponentDepartmentCode(String bloodComponentDepartmentCode) {
        this.bloodComponentDepartmentCode = bloodComponentDepartmentCode;
    }

    public List<GrdDiagnosticFile_Mdl> getGrdDiagnosticFilelist() {
        return grdDiagnosticFilelist;
    }

    public void setGrdDiagnosticFilelist(List<GrdDiagnosticFile_Mdl> grdDiagnosticFilelist) {
        this.grdDiagnosticFilelist = grdDiagnosticFilelist;
    }

    public List<GrdLinkDepartment_Mdl> getGrdLinkDept() {
        return grdLinkDept;
    }

    public List<GrdStatusGUITemp_Mdl> getGrdStatusTemplate() {
        return grdStatusTemplate;
    }

    public String getOpdNoOfSticker() {
        return opdNoOfSticker;
    }

    public void setOpdNoOfSticker(String opdNoOfSticker) {
        this.opdNoOfSticker = opdNoOfSticker;
    }

    public String getIpdNoOfSticker() {
        return ipdNoOfSticker;
    }

    public void setIpdNoOfSticker(String ipdNoOfSticker) {
        this.ipdNoOfSticker = ipdNoOfSticker;
    }

    public String getDiagnosticNoOfSticker() {
        return diagnosticNoOfSticker;
    }

    public void setDiagnosticNoOfSticker(String diagnosticNoOfSticker) {
        this.diagnosticNoOfSticker = diagnosticNoOfSticker;
    }

    public String getDiagnosticSampleCollectionText() {
        return diagnosticSampleCollectionText;
    }

    public void setDiagnosticSampleCollectionText(String diagnosticSampleCollectionText) {
        this.diagnosticSampleCollectionText = diagnosticSampleCollectionText;
    }

    public String getDiagnosticSampleCollectionTag() {
        return diagnosticSampleCollectionTag;
    }

    public void setDiagnosticSampleCollectionTag(String diagnosticSampleCollectionTag) {
        this.diagnosticSampleCollectionTag = diagnosticSampleCollectionTag;
    }

    public static String getReceiptLogo() {
        return receiptLogo;
    }

    public static void setReceiptLogo(String aReceiptLogo) {
        receiptLogo = aReceiptLogo;
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

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
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

    public String getOpdCaseCode() {
        return opdCaseCode;
    }

    public void setOpdCaseCode(String opdCaseCode) {
        this.opdCaseCode = opdCaseCode;
    }

    public String getOpdCaseName() {
        return opdCaseName;
    }

    public void setOpdCaseName(String opdCaseName) {
        this.opdCaseName = opdCaseName;
    }

    public String getOpdRefConsultantCode() {
        return opdRefConsultantCode;
    }

    public void setOpdRefConsultantCode(String opdRefConsultantCode) {
        this.opdRefConsultantCode = opdRefConsultantCode;
    }

    public String getOpdRefConsultantName() {
        return opdRefConsultantName;
    }

    public void setOpdRefConsultantName(String opdRefConsultantName) {
        this.opdRefConsultantName = opdRefConsultantName;
    }

    public String getOpdConsultantCode() {
        return opdConsultantCode;
    }

    public void setOpdConsultantCode(String opdConsultantCode) {
        this.opdConsultantCode = opdConsultantCode;
    }

    public String getOpdConsultantName() {
        return opdConsultantName;
    }

    public void setOpdConsultantName(String opdConsultantName) {
        this.opdConsultantName = opdConsultantName;
    }

    public String getDefaultRegFee() {
        return defaultRegFee;
    }

    public void setDefaultRegFee(String defaultRegFee) {
        this.defaultRegFee = defaultRegFee;
    }

    public String getIncludeDDInAge() {
        return includeDDInAge;
    }

    public void setIncludeDDInAge(String includeDDInAge) {
        this.includeDDInAge = includeDDInAge;
    }

    public String getIncludeMMInAge() {
        return includeMMInAge;
    }

    public void setIncludeMMInAge(String includeMMInAge) {
        this.includeMMInAge = includeMMInAge;
    }

    public String getDocumentFooter() {
        return documentFooter;
    }

    public void setDocumentFooter(String documentFooter) {
        this.documentFooter = documentFooter;
    }

    public String getDocumentFooterRegional() {
        return documentFooterRegional;
    }

    public void setDocumentFooterRegional(String documentFooterRegional) {
        this.documentFooterRegional = documentFooterRegional;
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

    public String getNationalityTag() {
        return nationalityTag;
    }

    public void setNationalityTag(String countryTag) {
        this.nationalityTag = countryTag;
    }

    public String getNationalityText() {
        return nationalityText;
    }

    public void setNationalityText(String countryText) {
        this.nationalityText = countryText;
    }

    public String getOpdIsReligionMandatory() {
        return opdIsReligionMandatory;
    }

    public String getTokenBlockTag() {
        return tokenBlockTag;
    }

    public void setTokenBlockTag(String tokenBlockTag) {
        this.tokenBlockTag = tokenBlockTag;
    }

    public String getTokenBlockText() {
        return tokenBlockText;
    }

    public void setTokenBlockText(String tokenBlockText) {
        this.tokenBlockText = tokenBlockText;
    }

    public void setOpdIsReligionMandatory(String opdIsReligionMandatory) {
        this.opdIsReligionMandatory = opdIsReligionMandatory;
    }

    public String getOpdIsReligionMandatoryName() {
        return opdIsReligionMandatoryName;
    }

    public void setOpdIsReligionMandatoryName(String opdIsReligionMandatoryName) {
        this.opdIsReligionMandatoryName = opdIsReligionMandatoryName;
    }

    public String getOpdIsIdTypeMandatory() {
        return opdIsIdTypeMandatory;
    }

    public void setOpdIsIdTypeMandatory(String opdIsIdTypeMandatory) {
        this.opdIsIdTypeMandatory = opdIsIdTypeMandatory;
    }

    public String getOpdIsIdTypeMandatoryName() {
        return opdIsIdTypeMandatoryName;
    }

    public void setOpdIsIdTypeMandatoryName(String opdIsIdTypeMandatoryName) {
        this.opdIsIdTypeMandatoryName = opdIsIdTypeMandatoryName;
    }

    public String getIpdInfantUnitTag() {
        return ipdInfantUnitTag;
    }

    public void setIpdInfantUnitTag(String ipdInfantUnitTag) {
        this.ipdInfantUnitTag = ipdInfantUnitTag;
    }

    public String getIpdInfantUnitText() {
        return ipdInfantUnitText;
    }

    public void setIpdInfantUnitText(String ipdInfantUnitText) {
        this.ipdInfantUnitText = ipdInfantUnitText;
    }

    public String getIpdIsRoomCleanTag() {
        return ipdIsRoomCleanTag;
    }

    public void setIpdIsRoomCleanTag(String ipdIsRoomCleanTag) {
        this.ipdIsRoomCleanTag = ipdIsRoomCleanTag;
    }

    public String getIpdIsRoomCleanText() {
        return ipdIsRoomCleanText;
    }

    public void setIpdIsRoomCleanText(String ipdIsRoomCleanText) {
        this.ipdIsRoomCleanText = ipdIsRoomCleanText;
    }

    public String getIpdIsAutoRoomTag() {
        return ipdIsAutoRoomTag;
    }

    public void setIpdIsAutoRoomTag(String ipdIsAutoRoomTag) {
        this.ipdIsAutoRoomTag = ipdIsAutoRoomTag;
    }

    public String getIpdIsAutoRoomText() {
        return ipdIsAutoRoomText;
    }

    public void setIpdIsAutoRoomText(String ipdIsAutoRoomText) {
        this.ipdIsAutoRoomText = ipdIsAutoRoomText;
    }

    public String getIpdTimeFormatRoomTag() {
        return ipdTimeFormatRoomTag;
    }

    public void setIpdTimeFormatRoomTag(String ipdTimeFormatRoomTag) {
        this.ipdTimeFormatRoomTag = ipdTimeFormatRoomTag;
    }

    public String getIpdTimeFormatRoomText() {
        return ipdTimeFormatRoomText;
    }

    public void setIpdTimeFormatRoomText(String ipdTimeFormatRoomText) {
        this.ipdTimeFormatRoomText = ipdTimeFormatRoomText;
    }

    public String getIpdCheckoutTime() {
        return ipdCheckoutTime;
    }

    public void setIpdCheckoutTime(String ipdCheckoutTime) {
        this.ipdCheckoutTime = ipdCheckoutTime;
    }

    public String getIpdGraceMinuteIn() {
        return ipdGraceMinuteIn;
    }

    public void setIpdGraceMinuteIn(String ipdGraceMinuteIn) {
        this.ipdGraceMinuteIn = ipdGraceMinuteIn;
    }

    public String getIpdHalfDayUpToMin() {
        return ipdHalfDayUpToMin;
    }

    public void setIpdHalfDayUpToMin(String ipdHalfDayUpToMin) {
        this.ipdHalfDayUpToMin = ipdHalfDayUpToMin;
    }

    public String getIpdHalfDayPercentage() {
        return ipdHalfDayPercentage;
    }

    public void setIpdHalfDayPercentage(String ipdHalfDayPercentage) {
        this.ipdHalfDayPercentage = ipdHalfDayPercentage;
    }

    public String getIpdControlAcTag() {
        return ipdControlAcTag;
    }

    public void setIpdControlAcTag(String ipdControlAcTag) {
        this.ipdControlAcTag = ipdControlAcTag;
    }

    public String getIpdControlAcText() {
        return ipdControlAcText;
    }

    public void setIpdControlAcText(String ipdControlAcText) {
        this.ipdControlAcText = ipdControlAcText;
    }

    public String getOpdControlAcTag() {
        return opdControlAcTag;
    }

    public void setOpdControlAcTag(String opdControlAcTag) {
        this.opdControlAcTag = opdControlAcTag;
    }

    public String getOpdControlAcText() {
        return opdControlAcText;
    }

    public void setOpdControlAcText(String opdControlAcText) {
        this.opdControlAcText = opdControlAcText;
    }

    public String getDiagnosticControlAcTag() {
        return diagnosticControlAcTag;
    }

    public void setDiagnosticControlAcTag(String diagnosticControlAcTag) {
        this.diagnosticControlAcTag = diagnosticControlAcTag;
    }

    public String getDiagnosticControlAcText() {
        return diagnosticControlAcText;
    }

    public void setDiagnosticControlAcText(String diagnosticControlAcText) {
        this.diagnosticControlAcText = diagnosticControlAcText;
    }

    public String getDirectPrescriptionTag() {
        return directPrescriptionTag;
    }

    public void setDirectPrescriptionTag(String directPrescriptionTag) {
        this.directPrescriptionTag = directPrescriptionTag;
    }

    public String getDirectPrescriptionText() {
        return directPrescriptionText;
    }

    public void setDirectPrescriptionText(String directPrescriptionText) {
        this.directPrescriptionText = directPrescriptionText;
    }

    public String getIsExistingPatientTag() {
        return isExistingPatientTag;
    }

    public void setIsExistingPatientTag(String isExistingPatientTag) {
        this.isExistingPatientTag = isExistingPatientTag;
    }

    public String getIsLABExistingPatientTag() {
        return isLABExistingPatientTag;
    }

    public void setIsLABExistingPatientTag(String isLABExistingPatientTag) {
        this.isLABExistingPatientTag = isLABExistingPatientTag;
    }

    public String getIsLABExistingPatientText() {
        return isLABExistingPatientText;
    }

    public void setIsLABExistingPatientText(String isLABExistingPatientText) {
        this.isLABExistingPatientText = isLABExistingPatientText;
    }

    public String getIsPatientInitialTag() {
        return isPatientInitialTag;
    }

    public void setIsPatientInitialTag(String isPatientInitialTag) {
        this.isPatientInitialTag = isPatientInitialTag;
    }

    public String getIsPatientInitialText() {
        return isPatientInitialText;
    }

    public void setIsPatientInitialText(String isPatientInitialText) {
        this.isPatientInitialText = isPatientInitialText;
    }

    public String getIsAddressText() {
        return isAddressText;
    }

    public void setIsAddressText(String isAddressText) {
        this.isAddressText = isAddressText;
    }

    public String getIsAddressTag() {
        return isAddressTag;
    }

    public void setIsAddressTag(String isAddressTag) {
        this.isAddressTag = isAddressTag;
    }

    public String getIsGuardianInitialText() {
        return isGuardianInitialText;
    }

    public void setIsGuardianInitialText(String isGuardianInitialText) {
        this.isGuardianInitialText = isGuardianInitialText;
    }

    public String getIsGuardianInitialTag() {
        return isGuardianInitialTag;
    }

    public void setIsGuardianInitialTag(String isGuardianInitialTag) {
        this.isGuardianInitialTag = isGuardianInitialTag;
    }

    public String getIsCityMandatoryText() {
        return isCityMandatoryText;
    }

    public void setIsCityMandatoryText(String isCityMandatoryText) {
        this.isCityMandatoryText = isCityMandatoryText;
    }

    public String getIsCityMandatoryTag() {
        return isCityMandatoryTag;
    }

    public void setIsCityMandatoryTag(String isCityMandatoryTag) {
        this.isCityMandatoryTag = isCityMandatoryTag;
    }

    public String getIsMaritalStatusText() {
        return isMaritalStatusText;
    }

    public void setIsMaritalStatusText(String isMaritalStatusText) {
        this.isMaritalStatusText = isMaritalStatusText;
    }

    public String getIsMaritalStatusTag() {
        return isMaritalStatusTag;
    }

    public void setIsMaritalStatusTag(String isMaritalStatusTag) {
        this.isMaritalStatusTag = isMaritalStatusTag;
    }

    public String getIsCasteTag() {
        return isCasteTag;
    }

    public void setIsCasteTag(String isCasteTag) {
        this.isCasteTag = isCasteTag;
    }

    public String getIsCasteText() {
        return isCasteText;
    }

    public void setIsCasteText(String isCasteText) {
        this.isCasteText = isCasteText;
    }

    public String getIsBloodGroupTag() {
        return isBloodGroupTag;
    }

    public void setIsBloodGroupTag(String isBloodGroupTag) {
        this.isBloodGroupTag = isBloodGroupTag;
    }

    public String getIsBloodGroupText() {
        return isBloodGroupText;
    }

    public void setIsBloodGroupText(String isBloodGroupText) {
        this.isBloodGroupText = isBloodGroupText;
    }

    public String getIsEmailTag() {
        return isEmailTag;
    }

    public void setIsEmailTag(String isEmailTag) {
        this.isEmailTag = isEmailTag;
    }

    public String getIsEmailText() {
        return isEmailText;
    }

    public void setIsEmailText(String isEmailText) {
        this.isEmailText = isEmailText;
    }

    public String getIsExistingPatientText() {
        return isExistingPatientText;
    }

    public void setIsExistingPatientText(String isExistingPatientText) {
        this.isExistingPatientText = isExistingPatientText;
    }

    public String getEmergencyControlAcTag() {
        return emergencyControlAcTag;
    }

    public void setEmergencyControlAcTag(String emergencyControlAcTag) {
        this.emergencyControlAcTag = emergencyControlAcTag;
    }

    public String getEmergencyControlAcText() {
        return emergencyControlAcText;
    }

    public void setEmergencyControlAcText(String emergencyControlAcText) {
        this.emergencyControlAcText = emergencyControlAcText;
    }

    public String getDayCareControlAcTag() {
        return dayCareControlAcTag;
    }

    public void setDayCareControlAcTag(String dayCareControlAcTag) {
        this.dayCareControlAcTag = dayCareControlAcTag;
    }

    public String getDayCareControlAcText() {
        return dayCareControlAcText;
    }

    public void setDayCareControlAcText(String dayCareControlAcText) {
        this.dayCareControlAcText = dayCareControlAcText;
    }

    public String getDiagnosticIsDiagAutoTag() {
        return diagnosticIsDiagAutoTag;
    }

    public void setDiagnosticIsDiagAutoTag(String diagnosticIsDiagAutoTag) {
        this.diagnosticIsDiagAutoTag = diagnosticIsDiagAutoTag;
    }

    public String getDiagnosticIsDiagAutoText() {
        return diagnosticIsDiagAutoText;
    }

    public void setDiagnosticIsDiagAutoText(String diagnosticIsDiagAutoText) {
        this.diagnosticIsDiagAutoText = diagnosticIsDiagAutoText;
    }

    public String getCancelDaysDiagnostic() {
        return diagnosticCancelDays;
    }

    public void setCancelDaysDiagnostic(String cancelDays) {
        this.diagnosticCancelDays = cancelDays;
    }

    public String getServiceDaysOPD() {
        return serviceDaysOPD;
    }

    public void setServiceDaysOPD(String serviceDays) {
        this.serviceDaysOPD = serviceDays;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getNewBornDay() {
        return NewBornDay;
    }

    public void setNewBornDay(String NewBornDay) {
        this.NewBornDay = NewBornDay;
    }

    public String getNewBornMonth() {
        return NewBornMonth;
    }

    public void setNewBornMonth(String NewBornMonth) {
        this.NewBornMonth = NewBornMonth;
    }

    public String getNewBornYear() {
        return NewBornYear;
    }

    public void setNewBornYear(String NewBornYear) {
        this.NewBornYear = NewBornYear;
    }

    public String getInfantMonth() {
        return InfantMonth;
    }

    public void setInfantMonth(String InfantMonth) {
        this.InfantMonth = InfantMonth;
    }

    public String getInfantYear() {
        return InfantYear;
    }

    public void setInfantYear(String InfantYear) {
        this.InfantYear = InfantYear;
    }

    public String getChildMonth() {
        return ChildMonth;
    }

    public void setChildMonth(String ChildMonth) {
        this.ChildMonth = ChildMonth;
    }

    public String getChildYear() {
        return ChildYear;
    }

    public void setChildYear(String ChildYear) {
        this.ChildYear = ChildYear;
    }

    public String getDiagnosticRegAdditionText() {
        return diagnosticRegAdditionText;
    }

    public void setDiagnosticRegAdditionText(String diagnosticRegAdditionText) {
        this.diagnosticRegAdditionText = diagnosticRegAdditionText;
    }

    public String getDiagnosticRegAdditionTag() {
        return diagnosticRegAdditionTag;
    }

    public void setDiagnosticRegAdditionTag(String diagnosticRegAdditionTag) {
        this.diagnosticRegAdditionTag = diagnosticRegAdditionTag;
    }

    public String getOrgReceiptCardTypeTag() {
        return orgReceiptCardTypeTag;
    }

    public void setOrgReceiptCardTypeTag(String orgReceiptCardTypeTag) {
        this.orgReceiptCardTypeTag = orgReceiptCardTypeTag;
    }

    public String getOrgReceiptCardTypeText() {
        return orgReceiptCardTypeText;
    }

    public void setOrgReceiptCardTypeText(String orgReceiptCardTypeText) {
        this.orgReceiptCardTypeText = orgReceiptCardTypeText;
    }

    public String getHospitalHoldCardTypeTag() {
        return hospitalHoldCardTypeTag;
    }

    public void setHospitalHoldCardTypeTag(String hospitalHoldCardTypeTag) {
        this.hospitalHoldCardTypeTag = hospitalHoldCardTypeTag;
    }

    public String getHospitalHoldCardTypeText() {
        return hospitalHoldCardTypeText;
    }

    public void setHospitalHoldCardTypeText(String hospitalHoldCardTypeText) {
        this.hospitalHoldCardTypeText = hospitalHoldCardTypeText;
    }

    public String getIsPatientImgMandatoryText() {
        return isPatientImgMandatoryText;
    }

    public void setIsPatientImgMandatoryText(String isPatientImgMandatoryText) {
        this.isPatientImgMandatoryText = isPatientImgMandatoryText;
    }

    public String getIsPatientImgMandatoryTag() {
        return isPatientImgMandatoryTag;
    }

    public void setIsPatientImgMandatoryTag(String isPatientImgMandatoryTag) {
        this.isPatientImgMandatoryTag = isPatientImgMandatoryTag;
    }

    public String getIsAreaMandatoryText() {
        return isAreaMandatoryText;
    }

    public void setIsAreaMandatoryText(String isAreaMandatoryText) {
        this.isAreaMandatoryText = isAreaMandatoryText;
    }

    public String getIsAreaMandatoryTag() {
        return isAreaMandatoryTag;
    }

    public void setIsAreaMandatoryTag(String isAreaMandatoryTag) {
        this.isAreaMandatoryTag = isAreaMandatoryTag;
    }

    public String getIsGuardianNameMandatoryText() {
        return isGuardianNameMandatoryText;
    }

    public void setIsGuardianNameMandatoryText(String isGuardianNameMandatoryText) {
        this.isGuardianNameMandatoryText = isGuardianNameMandatoryText;
    }

    public String getIsGuardianNameMandatoryTag() {
        return isGuardianNameMandatoryTag;
    }

    public void setIsGuardianNameMandatoryTag(String isGuardianNameMandatoryTag) {
        this.isGuardianNameMandatoryTag = isGuardianNameMandatoryTag;
    }

    public void setGrdStatusTemplate(List<GrdStatusGUITemp_Mdl> grdStatusTemplate) {
        this.grdStatusTemplate = grdStatusTemplate;
    }

    public String getDiagIsPrintOnLetterheadTag() {
        return diagIsPrintOnLetterheadTag;
    }

    public void setDiagIsPrintOnLetterheadTag(String diagIsPrintOnLetterheadTag) {
        this.diagIsPrintOnLetterheadTag = diagIsPrintOnLetterheadTag;
    }

    public String getDiagIsPrintOnLetterheadText() {
        return diagIsPrintOnLetterheadText;
    }

    public void setDiagIsPrintOnLetterheadText(String diagIsPrintOnLetterheadText) {
        this.diagIsPrintOnLetterheadText = diagIsPrintOnLetterheadText;
    }

    public String getDiagnosticSelfAcTag() {
        return diagnosticSelfAcTag;
    }

    public void setDiagnosticSelfAcTag(String diagnosticSelfAcTag) {
        this.diagnosticSelfAcTag = diagnosticSelfAcTag;
    }

    public String getDiagnosticSelfAcText() {
        return diagnosticSelfAcText;
    }

    public void setDiagnosticSelfAcText(String diagnosticSelfAcText) {
        this.diagnosticSelfAcText = diagnosticSelfAcText;
    }

    public String getDiagnosticHospitalAcTag() {
        return diagnosticHospitalAcTag;
    }

    public void setDiagnosticHospitalAcTag(String diagnosticHospitalAcTag) {
        this.diagnosticHospitalAcTag = diagnosticHospitalAcTag;
    }

    public String getDiagnosticHospitalAcText() {
        return diagnosticHospitalAcText;
    }

    public void setDiagnosticHospitalAcText(String diagnosticHospitalAcText) {
        this.diagnosticHospitalAcText = diagnosticHospitalAcText;
    }

    public String getHourlyServiceRoundOffText() {
        return hourlyServiceRoundOffText;
    }

    public void setHourlyServiceRoundOffText(String hourlyServiceRoundOffText) {
        this.hourlyServiceRoundOffText = hourlyServiceRoundOffText;
    }

    public String getHourlyServiceRoundOffTag() {
        return hourlyServiceRoundOffTag;
    }

    public void setHourlyServiceRoundOffTag(String hourlyServiceRoundOffTag) {
        this.hourlyServiceRoundOffTag = hourlyServiceRoundOffTag;
    }

    public String getDisallowTypeText() {
        return disallowTypeText;
    }

    public void setDisallowTypeText(String disallowTypeText) {
        this.disallowTypeText = disallowTypeText;
    }

    public String getDisallowTypeTag() {
        return disallowTypeTag;
    }

    public void setDisallowTypeTag(String disallowTypeTag) {
        this.disallowTypeTag = disallowTypeTag;
    }

    public String getDietaryProperty1() {
        return dietaryProperty1;
    }

    public void setDietaryProperty1(String dietaryProperty1) {
        this.dietaryProperty1 = dietaryProperty1;
    }

    public String getDietaryProperty2() {
        return dietaryProperty2;
    }

    public void setDietaryProperty2(String dietaryProperty2) {
        this.dietaryProperty2 = dietaryProperty2;
    }

    public String getDietaryProperty3() {
        return dietaryProperty3;
    }

    public void setDietaryProperty3(String dietaryProperty3) {
        this.dietaryProperty3 = dietaryProperty3;
    }

    public String getDietaryProperty4() {
        return dietaryProperty4;
    }

    public void setDietaryProperty4(String dietaryProperty4) {
        this.dietaryProperty4 = dietaryProperty4;
    }

    public String getDietaryProperty5() {
        return dietaryProperty5;
    }

    public void setDietaryProperty5(String dietaryProperty5) {
        this.dietaryProperty5 = dietaryProperty5;
    }

    public String getDietaryProperty6() {
        return dietaryProperty6;
    }

    public void setDietaryProperty6(String dietaryProperty6) {
        this.dietaryProperty6 = dietaryProperty6;
    }

    public String getOpdAllowPatientForSiteText() {
        return opdAllowPatientForSiteText;
    }

    public void setOpdAllowPatientForSiteText(String opdAllowPatientForSiteText) {
        this.opdAllowPatientForSiteText = opdAllowPatientForSiteText;
    }

    public String getOpdAllowPatientForSiteTag() {
        return opdAllowPatientForSiteTag;
    }

    public void setOpdAllowPatientForSiteTag(String opdAllowPatientForSiteTag) {
        this.opdAllowPatientForSiteTag = opdAllowPatientForSiteTag;
    }

    public String getIpdAllowPatientForSiteText() {
        return ipdAllowPatientForSiteText;
    }

    public void setIpdAllowPatientForSiteText(String ipdAllowPatientForSiteText) {
        this.ipdAllowPatientForSiteText = ipdAllowPatientForSiteText;
    }

    public String getIpdAllowPatientForSiteTag() {
        return ipdAllowPatientForSiteTag;
    }

    public void setIpdAllowPatientForSiteTag(String ipdAllowPatientForSiteTag) {
        this.ipdAllowPatientForSiteTag = ipdAllowPatientForSiteTag;
    }

    public String getDaycAllowPatientForSiteText() {
        return daycAllowPatientForSiteText;
    }

    public void setDaycAllowPatientForSiteText(String daycAllowPatientForSiteText) {
        this.daycAllowPatientForSiteText = daycAllowPatientForSiteText;
    }

    public String getDaycAllowPatientForSiteTag() {
        return daycAllowPatientForSiteTag;
    }

    public void setDaycAllowPatientForSiteTag(String daycAllowPatientForSiteTag) {
        this.daycAllowPatientForSiteTag = daycAllowPatientForSiteTag;
    }

    public String getDiagAllowPatientForSiteText() {
        return diagAllowPatientForSiteText;
    }

    public void setDiagAllowPatientForSiteText(String diagAllowPatientForSiteText) {
        this.diagAllowPatientForSiteText = diagAllowPatientForSiteText;
    }

    public String getDiagAllowPatientForSiteTag() {
        return diagAllowPatientForSiteTag;
    }

    public void setDiagAllowPatientForSiteTag(String diagAllowPatientForSiteTag) {
        this.diagAllowPatientForSiteTag = diagAllowPatientForSiteTag;
    }

    public String getDefaultOpdPharmacyDepartmentTag() {
        return defaultOpdPharmacyDepartmentTag;
    }

    public void setDefaultOpdPharmacyDepartmentTag(String defaultOpdPharmacyDepartmentTag) {
        this.defaultOpdPharmacyDepartmentTag = defaultOpdPharmacyDepartmentTag;
    }

    public String getDefaultOpdPharmacyDepartmentText() {
        return defaultOpdPharmacyDepartmentText;
    }

    public void setDefaultOpdPharmacyDepartmentText(String defaultOpdPharmacyDepartmentText) {
        this.defaultOpdPharmacyDepartmentText = defaultOpdPharmacyDepartmentText;
    }

    public String getDocumentHeader() {
        return documentHeader;
    }

    public void setDocumentHeader(String documentHeader) {
        this.documentHeader = documentHeader;
    }

    public String getIpdGraceMinuteOut() {
        return ipdGraceMinuteOut;
    }

    public void setIpdGraceMinuteOut(String ipdGraceMinuteOut) {
        this.ipdGraceMinuteOut = ipdGraceMinuteOut;
    }

    public String getPrntyesnotag() {
        return prntyesnotag;
    }

    public void setPrntyesnotag(String prntyesnotag) {
        this.prntyesnotag = prntyesnotag;
    }

    public String getPrntyesnotext() {
        return prntyesnotext;
    }

    public void setPrntyesnotext(String prntyesnotext) {
        this.prntyesnotext = prntyesnotext;
    }

    public String getPanCardLimit() {
        return panCardLimit;
    }

    public void setPanCardLimit(String panCardLimit) {
        this.panCardLimit = panCardLimit;
    }

    public String getCashPaymentLimit() {
        return cashPaymentLimit;
    }

    public void setCashPaymentLimit(String cashPaymentLimit) {
        this.cashPaymentLimit = cashPaymentLimit;
    }

    public String getRoomMandatoryTag() {
        return roomMandatoryTag;
    }

    public void setRoomMandatoryTag(String roomMandatoryTag) {
        this.roomMandatoryTag = roomMandatoryTag;
    }

    public String getRoomMandatoryText() {
        return roomMandatoryText;
    }

    public void setRoomMandatoryText(String roomMandatoryText) {
        this.roomMandatoryText = roomMandatoryText;
    }

    public String getNightshiftFrom() {
        return nightshiftFrom;
    }

    public void setNightshiftFrom(String nightshiftFrom) {
        this.nightshiftFrom = nightshiftFrom;
    }

    public String getNightshiftTo() {
        return nightshiftTo;
    }

    public void setNightshiftTo(String nightshiftTo) {
        this.nightshiftTo = nightshiftTo;
    }

    public String getCancelDaysOPD() {
        return cancelDaysOPD;
    }

    public void setCancelDaysOPD(String cancelDaysOPD) {
        this.cancelDaysOPD = cancelDaysOPD;
    }

    public String getPackageCode() {
        return packageCode;
    }

    public void setPackageCode(String packageCode) {
        this.packageCode = packageCode;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getDiagnosticDays() {
        return diagnosticDays;
    }

    public void setDiagnosticDays(String DiagnosticDays) {
        this.diagnosticDays = DiagnosticDays;
    }

    public boolean isFileUploaded() {
        return fileUploaded;
    }

    public void setFileUploaded(boolean fileUploaded) {
        this.fileUploaded = fileUploaded;
    }

    public String getDefaultPharmacySiteTag() {
        return defaultPharmacySiteTag;
    }

    public void setDefaultPharmacySiteTag(String defaultPharmacySiteTag) {
        this.defaultPharmacySiteTag = defaultPharmacySiteTag;
    }

    public String getDefaultPharmacySiteText() {
        return defaultPharmacySiteText;
    }

    public void setDefaultPharmacySiteText(String defaultPharmacySiteText) {
        this.defaultPharmacySiteText = defaultPharmacySiteText;
    }

    public String getAppointmentHistoryViewDays() {
        return appointmentHistoryViewDays;
    }

    public void setAppointmentHistoryViewDays(String appointmentHistoryViewDays) {
        this.appointmentHistoryViewDays = appointmentHistoryViewDays;
    }

    public String getHistoryViewDays() {
        return historyViewDays;
    }

    public void setHistoryViewDays(String historyViewDays) {
        this.historyViewDays = historyViewDays;
    }

    public String getLabHistoryViewDays() {
        return labHistoryViewDays;
    }

    public void setLabHistoryViewDays(String labHistoryViewDays) {
        this.labHistoryViewDays = labHistoryViewDays;
    }

    public String getSecodaryCurrencyText() {
        return secodaryCurrencyText;
    }

    public void setSecodaryCurrencyText(String secodaryCurrencyText) {
        this.secodaryCurrencyText = secodaryCurrencyText;
    }

    public String getSecodaryCurrencyTag() {
        return secodaryCurrencyTag;
    }

    public void setSecodaryCurrencyTag(String secodaryCurrencyTag) {
        this.secodaryCurrencyTag = secodaryCurrencyTag;
    }

    public String getBloodBankCipAdditionText() {
        return bloodBankCipAdditionText;
    }

    public void setBloodBankCipAdditionText(String bloodBankCipAdditionText) {
        this.bloodBankCipAdditionText = bloodBankCipAdditionText;
    }

    public String getBloodBankCipAdditionTag() {
        return bloodBankCipAdditionTag;
    }

    public void setBloodBankCipAdditionTag(String bloodBankCipAdditionTag) {
        this.bloodBankCipAdditionTag = bloodBankCipAdditionTag;
    }

    public String getNoOfVisit() {
        return noOfVisit;
    }

    public void setNoOfVisit(String noOfVisit) {
        this.noOfVisit = noOfVisit;
    }

    public String getValidUpTo() {
        return validUpTo;
    }

    public void setValidUpTo(String validUpTo) {
        this.validUpTo = validUpTo;
    }

    public String getQueueTag() {
        return queueTag;
    }

    public void setQueueTag(String queueTag) {
        this.queueTag = queueTag;
    }

    public String getQueueText() {
        return queueText;
    }

    public void setQueueText(String queueText) {
        this.queueText = queueText;
    }

    public String getConsultationRegistrationTag() {
        return consultationRegistrationTag;
    }

    public void setConsultationRegistrationTag(String consultationRegistrationTag) {
        this.consultationRegistrationTag = consultationRegistrationTag;
    }

    public String getConsultationRegistrationText() {
        return consultationRegistrationText;
    }

    public void setConsultationRegistrationText(String consultationRegistrationText) {
        this.consultationRegistrationText = consultationRegistrationText;
    }

    public List<GrdQueueSequence_Mdl> getLstQueue() {
        return lstQueue;
    }

    public String getIsReceiptGenerateWithFullDiscountText() {
        return isReceiptGenerateWithFullDiscountText;
    }

    public void setIsReceiptGenerateWithFullDiscountText(String isReceiptGenerateWith0AmountText) {
        this.isReceiptGenerateWithFullDiscountText = isReceiptGenerateWith0AmountText;
    }

    public String getDonorDefaultCampCode() {
        return donorDefaultCampCode;
    }

    public void setDonorDefaultCampCode(String donorDefaultCampCode) {
        this.donorDefaultCampCode = donorDefaultCampCode;
    }

    public String getDonorDefaultCampName() {
        return donorDefaultCampName;
    }

    public void setDonorDefaultCampName(String donorDefaultCampName) {
        this.donorDefaultCampName = donorDefaultCampName;
    }

    public String getIsOtpRequiredForRefundText() {
        return isOtpRequiredForRefundText;
    }

    public void setIsOtpRequiredForRefundText(String isOtpRequiredForRefundText) {
        this.isOtpRequiredForRefundText = isOtpRequiredForRefundText;
    }

    public String getIsOtpRequiredForRefundTag() {
        return isOtpRequiredForRefundTag;
    }

    public void setIsOtpRequiredForRefundTag(String isOtpRequiredForRefundTag) {
        this.isOtpRequiredForRefundTag = isOtpRequiredForRefundTag;
    }

    public String getIsIcdxMandatoryText() {
        return isIcdxMandatoryText;
    }

    public void setIsIcdxMandatoryText(String isIcdxMandatoryText) {
        this.isIcdxMandatoryText = isIcdxMandatoryText;
    }

    public String getIsIcdxMandatoryTag() {
        return isIcdxMandatoryTag;
    }

    public void setIsIcdxMandatoryTag(String isIcdxMandatoryTag) {
        this.isIcdxMandatoryTag = isIcdxMandatoryTag;
    }

    public String getIsReceiptGenerateWithFullDiscountTag() {
        return isReceiptGenerateWithFullDiscountTag;
    }

    public void setIsReceiptGenerateWithFullDiscountTag(String isReceiptGenerateWith0AmountTag) {
        this.isReceiptGenerateWithFullDiscountTag = isReceiptGenerateWith0AmountTag;
    }

    public String getDiagnosticCancelDays() {
        return diagnosticCancelDays;
    }

    public void setDiagnosticCancelDays(String diagnosticCancelDays) {
        this.diagnosticCancelDays = diagnosticCancelDays;
    }

    public String getAppointmentBookUptoDays() {
        return appointmentBookUptoDays;
    }

    public void setAppointmentBookUptoDays(String appointmentBookUptoDays) {
        this.appointmentBookUptoDays = appointmentBookUptoDays;
    }

    public String getAppointmentPaymentGatewayAllowedTag() {
        return appointmentPaymentGatewayAllowedTag;
    }

    public void setAppointmentPaymentGatewayAllowedTag(String appointmentPaymentGatewayAllowedTag) {
        this.appointmentPaymentGatewayAllowedTag = appointmentPaymentGatewayAllowedTag;
    }

    public String getAppointmentPaymentGatewayAllowedText() {
        return appointmentPaymentGatewayAllowedText;
    }

    public void setAppointmentPaymentGatewayAllowedText(String appointmentPaymentGatewayAllowedText) {
        this.appointmentPaymentGatewayAllowedText = appointmentPaymentGatewayAllowedText;
    }

    public String getPaymentGatewayURL() {
        return paymentGatewayURL;
    }

    public void setPaymentGatewayURL(String paymentGatewayURL) {
        this.paymentGatewayURL = paymentGatewayURL;
    }

    public String getPaymentOptionTag() {
        return paymentOptionTag;
    }

    public void setPaymentOptionTag(String paymentOptionTag) {
        this.paymentOptionTag = paymentOptionTag;
    }

    public String getPaymentOptionText() {
        return paymentOptionText;
    }

    public void setPaymentOptionText(String paymentOptionText) {
        this.paymentOptionText = paymentOptionText;
    }

    public String getAutoOPDRegAfterAppointmentTag() {
        return autoOPDRegAfterAppointmentTag;
    }

    public void setAutoOPDRegAfterAppointmentTag(String autoOPDRegAfterAppointmentTag) {
        this.autoOPDRegAfterAppointmentTag = autoOPDRegAfterAppointmentTag;
    }

    public String getAutoOPDRegAfterAppointmentText() {
        return autoOPDRegAfterAppointmentText;
    }

    public void setAutoOPDRegAfterAppointmentText(String autoOPDRegAfterAppointmentText) {
        this.autoOPDRegAfterAppointmentText = autoOPDRegAfterAppointmentText;
    }

    public String getRunFromWebsiteTag() {
        return runFromWebsiteTag;
    }

    public void setRunFromWebsiteTag(String runFromWebsiteTag) {
        this.runFromWebsiteTag = runFromWebsiteTag;
    }

    public String getRunFromWebsiteText() {
        return runFromWebsiteText;
    }

    public void setRunFromWebsiteText(String runFromWebsiteText) {
        this.runFromWebsiteText = runFromWebsiteText;
    }

    public String getEodPostingTypeCode() {
        return eodPostingTypeCode;
    }

    public void setEodPostingTypeCode(String eodPostingTypeCode) {
        this.eodPostingTypeCode = eodPostingTypeCode;
    }

    public String getEodPostingTypeName() {
        return eodPostingTypeName;
    }

    public void setEodPostingTypeName(String eodPostingTypeName) {
        this.eodPostingTypeName = eodPostingTypeName;
    }

    public String getServiceUnitEditableName() {
        return serviceUnitEditableName;
    }

    public void setServiceUnitEditableName(String serviceUnitEditableName) {
        this.serviceUnitEditableName = serviceUnitEditableName;
    }

    public String getServiceUnitEditableCode() {
        return serviceUnitEditableCode;
    }

    public void setServiceUnitEditableCode(String serviceUnitEditableCode) {
        this.serviceUnitEditableCode = serviceUnitEditableCode;
    }

    public String getUhidDefaultCardTypeCode() {
        return uhidDefaultCardTypeCode;
    }

    public void setUhidDefaultCardTypeCode(String uhidDefaultCardTypeCode) {
        this.uhidDefaultCardTypeCode = uhidDefaultCardTypeCode;
    }

    public String getUhidDefaultCardName() {
        return uhidDefaultCardName;
    }

    public void setUhidDefaultCardName(String uhidDefaultCardName) {
        this.uhidDefaultCardName = uhidDefaultCardName;
    }

    public String getDietScheduleDays() {
        return dietScheduleDays;
    }

    public void setDietScheduleDays(String dietScheduleDays) {
        this.dietScheduleDays = dietScheduleDays;
    }

    public String getIncludeTaxInCost() {
        return includeTaxInCost;
    }

    public void setIncludeTaxInCost(String includeTaxInCost) {
        this.includeTaxInCost = includeTaxInCost;
    }

    public String getPurchaseSalesAcPostingBasedOnCode() {
        return purchaseSalesAcPostingBasedOnCode;
    }

    public void setPurchaseSalesAcPostingBasedOnCode(String purchaseSalesAcPostingBasedOnCode) {
        this.purchaseSalesAcPostingBasedOnCode = purchaseSalesAcPostingBasedOnCode;
    }

    public String getPurchaseSalesAcPostingBasedOnName() {
        return purchaseSalesAcPostingBasedOnName;
    }

    public void setPurchaseSalesAcPostingBasedOnName(String purchaseSalesAcPostingBasedOnName) {
        this.purchaseSalesAcPostingBasedOnName = purchaseSalesAcPostingBasedOnName;
    }

    public String getIndentRequired() {
        return indentRequired;
    }

    public void setIndentRequired(String indentRequired) {
        this.indentRequired = indentRequired;
    }

    public String getRequisitionMandatoryInTransfer() {
        return requisitionMandatoryInTransfer;
    }

    public void setRequisitionMandatoryInTransfer(String requisitionMandatoryInTransfer) {
        this.requisitionMandatoryInTransfer = requisitionMandatoryInTransfer;
    }

    public String getSinglePOFlowInPurchase() {
        return singlePOFlowInPurchase;
    }

    public void setSinglePOFlowInPurchase(String singlePOFlowInPurchase) {
        this.singlePOFlowInPurchase = singlePOFlowInPurchase;
    }

    public String getAutoBedConfirmation() {
        return autoBedConfirmation;
    }

    public void setAutoBedConfirmation(String autoBedConfirmation) {
        this.autoBedConfirmation = autoBedConfirmation;
    }

    public String getInv_DefaultPurchaseDepartment() {
        return inv_DefaultPurchaseDepartment;
    }

    public void setInv_DefaultPurchaseDepartment(String inv_DefaultPurchaseDepartment) {
        this.inv_DefaultPurchaseDepartment = inv_DefaultPurchaseDepartment;
    }

    public String getInv_DefaultPurchaseDepartmentText() {
        return inv_DefaultPurchaseDepartmentText;
    }

    public void setInv_DefaultPurchaseDepartmentText(String inv_DefaultPurchaseDepartmentText) {
        this.inv_DefaultPurchaseDepartmentText = inv_DefaultPurchaseDepartmentText;
    }

    public String getIsCommonPricelistForSiteTag() {
        return IsCommonPricelistForSiteTag;
    }

    public void setIsCommonPricelistForSiteTag(String IsCommonPricelistForSiteTag) {
        this.IsCommonPricelistForSiteTag = IsCommonPricelistForSiteTag;
    }

    public String getIsCommonPricelistForSiteText() {
        return IsCommonPricelistForSiteText;
    }

    public void setIsCommonPricelistForSiteText(String IsCommonPricelistForSiteText) {
        this.IsCommonPricelistForSiteText = IsCommonPricelistForSiteText;
    }

    public String getAsset_DepreciationAcTag() {
        return asset_DepreciationAcTag;
    }

    public void setAsset_DepreciationAcTag(String asset_DepreciationAcTag) {
        this.asset_DepreciationAcTag = asset_DepreciationAcTag;
    }

    public String getAsset_DepreciationAcText() {
        return asset_DepreciationAcText;
    }

    public void setAsset_DepreciationAcText(String asset_DepreciationAcText) {
        this.asset_DepreciationAcText = asset_DepreciationAcText;
    }

    public String getAsset_DepreciationPostOn() {
        return asset_DepreciationPostOn;
    }

    public void setAsset_DepreciationPostOn(String asset_DepreciationPostOn) {
        this.asset_DepreciationPostOn = asset_DepreciationPostOn;
    }

    public String getAsset_DepreciationPostOnText() {
        return asset_DepreciationPostOnText;
    }

    public void setAsset_DepreciationPostOnText(String asset_DepreciationPostOnText) {
        this.asset_DepreciationPostOnText = asset_DepreciationPostOnText;
    }
public String getAppointmentCountMonthwise() {
        return appointmentCountMonthwise;
    }

    public void setAppointmentCountMonthwise(String appointmentCountMonthwise) {
        this.appointmentCountMonthwise = appointmentCountMonthwise;
    }

    public String getTransactionLockDate() {
        return transactionLockDate;
    }

    public void setTransactionLockDate(String transactionLockDate) {
        this.transactionLockDate = transactionLockDate;
    }

    public String getTransactionLockYear() {
        return transactionLockYear;
    }

    public void setTransactionLockYear(String transactionLockYear) {
        this.transactionLockYear = transactionLockYear;
    }
}
