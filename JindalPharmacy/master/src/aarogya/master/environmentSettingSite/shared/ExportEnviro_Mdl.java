package aarogya.master.environmentSettingSite.shared;

import aarogya.master.environmentService.shared.GrdServiceCategory_Mdl;
import aarogya.selfToolkit.stStatic.shared.STEnum.*;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.DmEnum.AlphabetCase;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ExportEnviro_Mdl implements Serializable, IsSerializable {

    private String roundOffTDS;
    private String roundOffGST;
    private String opdCaseCode;
    private String opdCaseName;
    private String referredByCode;
    private String referredByName;
    private String consultantCode;
    private String consultantName;
    private String defaultRegFee;
    private String includeDDInAge;
    private String documentFooter;
    private String documentFooterRegional;
    private String documentHeader;
    private String cityCode;
    private String cityName;
    private String nationalityTag;
    private String nationalityText;
    private String ipdInfantUnitTag;
    private String ipdInfantUnitText;
    private String ipdTimeFormatRoomTag;
    private String ipdTimeFormatRoomText;
    private String ipdCheckoutTime;
    private String ipdGraceMinuteIn;
    private String ipdGraceMinuteOut;
    private String ipdHalfDayUpToMin;
    private String ipdHalfDayPercentage;
    private String ctrlAcIPDTag;
    private String ctrlAcIPDText;
    private String ctrlAcOPDTag;
    private String ctrlAcOPDText;
    private String ctrlAcLABTag;
    private String ctrlAcLABText;
    private String stickerOPD;
    private String stickerIPD;
    private String eodPostingType;

    private String stickerLAB;
    private String ctrlAcEmergencyTag;
    private String ctrlAcEmergencyText;
    private String ctrlAcDayCareTag;
    private String ctrlAcDayCareText;
    private String ctrlAcPharmacyText;
    private String ctrlAcPharmacyTag;
    private String panCardLimit;
    private String cashPaymentLimit;
    private String nightshiftFrom;
    private String nightshiftTo;
    private String cancelDaysOPD;
    private String serviceDays;
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
    private String orgReceiptCardTypeTag;
    private String orgReceiptCardTypeText;
    private String hospitalHoldCardTypeTag;
    private String hospitalHoldCardTypeText;
    private String uhidDefaultCardTypeCode;
    private String uhidDefaultCardName;
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
    private String packageCode;
    private String packageName;
    private String labCancelDays;
    private String DiagnosticDays;
    private String defaultPharmacySiteTag;
    private String defaultPharmacySiteText;
    private String bloodBagIssueDepartmentCode;
    private String bloodBank_CIP_Addition;
    private String createComponentDepartmentCode;
    private String IncludeTaxInCost;
    private String pharmacyBarcodeActive;
    private String secondaryCurrencyCode;
    private String includeMMInAge;
    private String stockAdjustmentVTypeCode;
    private String stockAdjustmentVTypeName;

    private String wj_MsgLength;
    private String appointmentHistoryViewDays;
    private String historyViewDays;
    private String labHistoryViewDays;
    private TokenBlockNature tokenBlockNature;
//    private OTBookingType bookingType;
    private AlphabetCase patientAlpha, addressAlpha, medicineAlpha;
    private boolean religionMandatory = false, idTypeMandatory = false, roomClean = false, autoRoomPost = false, existingPatient = false,
            patientInitialMandatory = false, addressMandatory = false, guardianInitialMandatory = false, cityMandatory = false, isServiceUnitEditable = false,
            isReceiptGenerateWithFullDiscount = false, maritalMandatory = false, casteMandatory = false, bloodGroupMandatory = false, emailMandatory = false,
            roomMandatory = false, patientImageMandatory = false, areaMandatory = false, guardianMandatory = false, diagPrintOnLetterHead = false,
            labAutoVerify = false, directLabCollection = false, directPrescription = false, labBalancePrint = false,
            isOtpRequiredForRefund = false, isIcdxMandatory = false, appointmentPaymentGatewayAllowed = false,
            autoOPDRegAfterAppointment = false, runFromWebsite = false, autoInTransitByMedicineTransfer = false;
    private List<GrdLinkDepartment_Mdl> lstDepartmentLink = new ArrayList<GrdLinkDepartment_Mdl>();
    private List<GrdStatusGUITemp_Mdl> lstDischargeGUI = new ArrayList<GrdStatusGUITemp_Mdl>();
    private List<GrdServiceCategory_Mdl> lstGeneralService = new ArrayList<GrdServiceCategory_Mdl>();
    private List<GrdServiceCategory_Mdl> lstDiagnostic = new ArrayList<GrdServiceCategory_Mdl>();
//-------------------------------------------------------
    private String pharmacyHospitalAcText;
    private String pharmacyHospitalAcTag;
    private String pharmacySelfAcTag;
    private String pharmacySelfAcText;
    private String pharmacyFinalDiscountOHCode;
    private String pharmacyFinalDiscountOHName;
    private String pharmacyFinalDiscountAcCode;
    private String pharmacyFinalDiscountAcName;
    private String pharmacyDefaultServiceTag;
    private String pharmacyDefaultServiceText;
    private QueueAlgoNature algoNature;
    private String opdValidity, opdNoOfVisit, opdIsRoomWiseRegistration;
    private String donorDefaultCampCode;
    private String appointmentBookUptoDays;
    private String paymentGatewayURL;
    private String paymentOptionTag;
    private String paymentOptionText;
    private String lockDate;

    public ExportEnviro_Mdl() {
    }

    public QueueAlgoNature getAlgoNature() {
        return algoNature;
    }

    public String getDonorDefaultCampCode() {
        return donorDefaultCampCode;
    }

    public void setDonorDefaultCampCode(String donorDefaultCampCode) {
        this.donorDefaultCampCode = donorDefaultCampCode;
    }

    public String getOpdValidity() {
        return opdValidity;
    }

    public void setOpdValidity(String opdValidity) {
        this.opdValidity = opdValidity;
    }

    public String getOpdNoOfVisit() {
        return opdNoOfVisit;
    }

    public void setOpdNoOfVisit(String opdNoOfVisit) {
        this.opdNoOfVisit = opdNoOfVisit;
    }

    public String getOpdIsRoomWiseRegistration() {
        return opdIsRoomWiseRegistration;
    }

    public void setOpdIsRoomWiseRegistration(String opdIsRoomWiseRegistration) {
        this.opdIsRoomWiseRegistration = opdIsRoomWiseRegistration;
    }

    public void setAlgoNature(QueueAlgoNature algoNature) {
        this.algoNature = algoNature;
    }

    public String getPharmacyDefaultServiceTag() {
        return pharmacyDefaultServiceTag;
    }

    public void setPharmacyDefaultServiceTag(String pharmacyDefaultServiceTag) {
        this.pharmacyDefaultServiceTag = pharmacyDefaultServiceTag;
    }

    public String getPharmacyDefaultServiceText() {
        return pharmacyDefaultServiceText;
    }

    public void setPharmacyDefaultServiceText(String pharmacyDefaultServiceText) {
        this.pharmacyDefaultServiceText = pharmacyDefaultServiceText;
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

    public String getPharmacyHospitalAcText() {
        return pharmacyHospitalAcText;
    }

    public void setPharmacyHospitalAcText(String pharmacyHospitalAcText) {
        this.pharmacyHospitalAcText = pharmacyHospitalAcText;
    }

    public String getPharmacyHospitalAcTag() {
        return pharmacyHospitalAcTag;
    }

    public void setPharmacyHospitalAcTag(String pharmacyHospitalAcTag) {
        this.pharmacyHospitalAcTag = pharmacyHospitalAcTag;
    }

    public List<GrdLinkDepartment_Mdl> getLstDepartmentLink() {
        return lstDepartmentLink;
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

    public List<GrdStatusGUITemp_Mdl> getLstDischargeGUI() {
        return lstDischargeGUI;
    }

    public List<GrdServiceCategory_Mdl> getLstGeneralService() {
        return lstGeneralService;
    }

    public List<GrdServiceCategory_Mdl> getLstDiagnostic() {
        return lstDiagnostic;
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

    public String getReferredByCode() {
        return referredByCode;
    }

    public void setReferredByCode(String referredByCode) {
        this.referredByCode = referredByCode;
    }

    public String getReferredByName() {
        return referredByName;
    }

    public void setReferredByName(String referredByName) {
        this.referredByName = referredByName;
    }

    public String getConsultantCode() {
        return consultantCode;
    }

    public void setConsultantCode(String consultantCode) {
        this.consultantCode = consultantCode;
    }

    public String getConsultantName() {
        return consultantName;
    }

    public void setConsultantName(String consultantName) {
        this.consultantName = consultantName;
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

    public String getDocumentHeader() {
        return documentHeader;
    }

    public void setDocumentHeader(String documentHeader) {
        this.documentHeader = documentHeader;
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

    public String getNationalityTag() {
        return nationalityTag;
    }

    public void setNationalityTag(String nationalityTag) {
        this.nationalityTag = nationalityTag;
    }

    public String getNationalityText() {
        return nationalityText;
    }

    public void setNationalityText(String nationalityText) {
        this.nationalityText = nationalityText;
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

    public String getIpdGraceMinuteOut() {
        return ipdGraceMinuteOut;
    }

    public void setIpdGraceMinuteOut(String ipdGraceMinuteOut) {
        this.ipdGraceMinuteOut = ipdGraceMinuteOut;
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

    public String getCtrlAcIPDTag() {
        return ctrlAcIPDTag;
    }

    public void setCtrlAcIPDTag(String ctrlAcIPDTag) {
        this.ctrlAcIPDTag = ctrlAcIPDTag;
    }

    public String getCtrlAcIPDText() {
        return ctrlAcIPDText;
    }

    public void setCtrlAcIPDText(String ctrlAcIPDText) {
        this.ctrlAcIPDText = ctrlAcIPDText;
    }

    public String getCtrlAcOPDTag() {
        return ctrlAcOPDTag;
    }

    public void setCtrlAcOPDTag(String ctrlAcOPDTag) {
        this.ctrlAcOPDTag = ctrlAcOPDTag;
    }

    public String getCtrlAcOPDText() {
        return ctrlAcOPDText;
    }

    public void setCtrlAcOPDText(String ctrlAcOPDText) {
        this.ctrlAcOPDText = ctrlAcOPDText;
    }

    public String getCtrlAcLABTag() {
        return ctrlAcLABTag;
    }

    public void setCtrlAcLABTag(String ctrlAcLABTag) {
        this.ctrlAcLABTag = ctrlAcLABTag;
    }

    public String getCtrlAcLABText() {
        return ctrlAcLABText;
    }

    public void setCtrlAcLABText(String ctrlAcLABText) {
        this.ctrlAcLABText = ctrlAcLABText;
    }

    public String getStickerOPD() {
        return stickerOPD;
    }

    public void setStickerOPD(String StrickerOPD) {
        this.stickerOPD = StrickerOPD;
    }

    public String getStickerIPD() {
        return stickerIPD;
    }

    public void setStickerIPD(String stickerIPD) {
        this.stickerIPD = stickerIPD;
    }

    public String getStickerLAB() {
        return stickerLAB;
    }

    public void setStickerLAB(String stickerLAB) {
        this.stickerLAB = stickerLAB;
    }

    public String getCtrlAcEmergencyTag() {
        return ctrlAcEmergencyTag;
    }

    public void setCtrlAcEmergencyTag(String ctrlAcEmergencyTag) {
        this.ctrlAcEmergencyTag = ctrlAcEmergencyTag;
    }

    public String getCtrlAcEmergencyText() {
        return ctrlAcEmergencyText;
    }

    public void setCtrlAcEmergencyText(String ctrlAcEmergencyText) {
        this.ctrlAcEmergencyText = ctrlAcEmergencyText;
    }

    public String getCtrlAcDayCareTag() {
        return ctrlAcDayCareTag;
    }

    public void setCtrlAcDayCareTag(String ctrlAcDayCareTag) {
        this.ctrlAcDayCareTag = ctrlAcDayCareTag;
    }

    public String getCtrlAcDayCareText() {
        return ctrlAcDayCareText;
    }

    public void setCtrlAcDayCareText(String ctrlAcDayCareText) {
        this.ctrlAcDayCareText = ctrlAcDayCareText;
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

    public String getServiceDays() {
        return serviceDays;
    }

    public void setServiceDays(String serviceDays) {
        this.serviceDays = serviceDays;
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

    public String getLabCancelDays() {
        return labCancelDays;
    }

    public void setLabCancelDays(String labCancelDays) {
        this.labCancelDays = labCancelDays;
    }

    public String getDiagnosticDays() {
        return DiagnosticDays;
    }

    public void setDiagnosticDays(String DiagnosticDays) {
        this.DiagnosticDays = DiagnosticDays;
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

    public TokenBlockNature getTokenBlockNature() {
        return tokenBlockNature;
    }

    public void setTokenBlockNature(TokenBlockNature tokenBlockNature) {
        this.tokenBlockNature = tokenBlockNature;
    }

//    public OTBookingType getBookingType() {
//        return bookingType;
//    }
//
//    public void setBookingType(OTBookingType bookingType) {
//        this.bookingType = bookingType;
//    }
    public AlphabetCase getPatientAlpha() {
        return patientAlpha;
    }

    public void setPatientAlpha(AlphabetCase patientAlpha) {
        this.patientAlpha = patientAlpha;
    }

    public AlphabetCase getAddressAlpha() {
        return addressAlpha;
    }

    public void setAddressAlpha(AlphabetCase addressAlpha) {
        this.addressAlpha = addressAlpha;
    }

    public AlphabetCase getMedicineAlpha() {
        return medicineAlpha;
    }

    public void setMedicineAlpha(AlphabetCase medicineAlpha) {
        this.medicineAlpha = medicineAlpha;
    }

    public boolean isReligionMandatory() {
        return religionMandatory;
    }

    public void setReligionMandatory(boolean religionMandatory) {
        this.religionMandatory = religionMandatory;
    }

    public boolean isIdTypeMandatory() {
        return idTypeMandatory;
    }

    public void setIdTypeMandatory(boolean idTypeMandatory) {
        this.idTypeMandatory = idTypeMandatory;
    }

    public boolean isRoomClean() {
        return roomClean;
    }

    public void setRoomClean(boolean roomClean) {
        this.roomClean = roomClean;
    }

    public boolean isAutoRoomPost() {
        return autoRoomPost;
    }

    public void setAutoRoomPost(boolean autoRoomPost) {
        this.autoRoomPost = autoRoomPost;
    }

    public boolean isExistingPatient() {
        return existingPatient;
    }

    public void setExistingPatient(boolean existingPatient) {
        this.existingPatient = existingPatient;
    }

    public boolean isPatientInitialMandatory() {
        return patientInitialMandatory;
    }

    public void setPatientInitialMandatory(boolean patientInitialMandatory) {
        this.patientInitialMandatory = patientInitialMandatory;
    }

    public boolean isAddressMandatory() {
        return addressMandatory;
    }

    public void setAddressMandatory(boolean addressMandatory) {
        this.addressMandatory = addressMandatory;
    }

    public boolean isGuardianInitialMandatory() {
        return guardianInitialMandatory;
    }

    public void setGuardianInitialMandatory(boolean guardianInitialMandatory) {
        this.guardianInitialMandatory = guardianInitialMandatory;
    }

    public boolean isCityMandatory() {
        return cityMandatory;
    }

    public void setCityMandatory(boolean cityMandatory) {
        this.cityMandatory = cityMandatory;
    }

    public boolean isMaritalMandatory() {
        return maritalMandatory;
    }

    public void setMaritalMandatory(boolean maritalMandatory) {
        this.maritalMandatory = maritalMandatory;
    }

    public boolean isCasteMandatory() {
        return casteMandatory;
    }

    public void setCasteMandatory(boolean casteMandatory) {
        this.casteMandatory = casteMandatory;
    }

    public boolean isBloodGroupMandatory() {
        return bloodGroupMandatory;
    }

    public void setBloodGroupMandatory(boolean bloodGroupMandatory) {
        this.bloodGroupMandatory = bloodGroupMandatory;
    }

    public boolean isEmailMandatory() {
        return emailMandatory;
    }

    public void setEmailMandatory(boolean emailMandatory) {
        this.emailMandatory = emailMandatory;
    }

    public boolean isRoomMandatory() {
        return roomMandatory;
    }

    public void setRoomMandatory(boolean roomMandatory) {
        this.roomMandatory = roomMandatory;
    }

    public boolean isPatientImageMandatory() {
        return patientImageMandatory;
    }

    public void setPatientImageMandatory(boolean patientImageMandatory) {
        this.patientImageMandatory = patientImageMandatory;
    }

    public boolean isAreaMandatory() {
        return areaMandatory;
    }

    public void setAreaMandatory(boolean areaMandatory) {
        this.areaMandatory = areaMandatory;
    }

    public boolean isGuardianMandatory() {
        return guardianMandatory;
    }

    public void setGuardianMandatory(boolean guardianMandatory) {
        this.guardianMandatory = guardianMandatory;
    }

    public boolean isDiagPrintOnLetterHead() {
        return diagPrintOnLetterHead;
    }

    public void setDiagPrintOnLetterHead(boolean diagPrintOnLetterHead) {
        this.diagPrintOnLetterHead = diagPrintOnLetterHead;
    }

    public boolean isLabAutoVerify() {
        return labAutoVerify;
    }

    public void setLabAutoVerify(boolean labAutoVerify) {
        this.labAutoVerify = labAutoVerify;
    }

    public String getCtrlAcPharmacyText() {
        return ctrlAcPharmacyText;
    }

    public void setCtrlAcPharmacyText(String ctrlAcPharmacyText) {
        this.ctrlAcPharmacyText = ctrlAcPharmacyText;
    }

    public String getCtrlAcPharmacyTag() {
        return ctrlAcPharmacyTag;
    }

    public void setCtrlAcPharmacyTag(String ctrlAcPharmacyTag) {
        this.ctrlAcPharmacyTag = ctrlAcPharmacyTag;
    }

    public boolean isDirectLabCollection() {
        return directLabCollection;
    }

    public void setDirectLabCollection(boolean directLabCollection) {
        this.directLabCollection = directLabCollection;
    }

    public boolean isDirectPrescription() {
        return directPrescription;
    }

    public void setDirectPrescription(boolean directPrescription) {
        this.directPrescription = directPrescription;
    }

    public boolean isLabBalancePrint() {
        return labBalancePrint;
    }

    public void setLabBalancePrint(boolean labBalancePrint) {
        this.labBalancePrint = labBalancePrint;
    }

    public String getRoundOffTDS() {
        return roundOffTDS;
    }

    public void setRoundOffTDS(String roundOffTDS) {
        this.roundOffTDS = roundOffTDS;
    }

    public String getRoundOffGST() {
        return roundOffGST;
    }

    public void setRoundOffGST(String roundOffGST) {
        this.roundOffGST = roundOffGST;
    }

    public String getBloodBagIssueDepartmentCode() {
        return bloodBagIssueDepartmentCode;
    }

    public void setBloodBagIssueDepartmentCode(String bloodBagIssueDepartmentCode) {
        this.bloodBagIssueDepartmentCode = bloodBagIssueDepartmentCode;
    }

    public String getBloodBank_CIP_Addition() {
        return bloodBank_CIP_Addition;
    }

    public void setBloodBank_CIP_Addition(String bloodBank_CIP_Addition) {
        this.bloodBank_CIP_Addition = bloodBank_CIP_Addition;
    }

    public String getCreateComponentDepartmentCode() {
        return createComponentDepartmentCode;
    }

    public void setCreateComponentDepartmentCode(String createComponentDepartmentCode) {
        this.createComponentDepartmentCode = createComponentDepartmentCode;
    }

    public String getIncludeTaxInCost() {
        return IncludeTaxInCost;
    }

    public void setIncludeTaxInCost(String IncludeTaxInCost) {
        this.IncludeTaxInCost = IncludeTaxInCost;
    }

    public String getPharmacyBarcodeActive() {
        return pharmacyBarcodeActive;
    }

    public void setPharmacyBarcodeActive(String pharmacyBarcodeActive) {
        this.pharmacyBarcodeActive = pharmacyBarcodeActive;
    }

    public boolean isAutoInTransitByMedicineTransfer() {
        return autoInTransitByMedicineTransfer;
    }

    public void setAutoInTransitByMedicineTransfer(boolean autoInTransitByMedicineTransfer) {
        this.autoInTransitByMedicineTransfer = autoInTransitByMedicineTransfer;
    }

    public String getSecondaryCurrencyCode() {
        return secondaryCurrencyCode;
    }

    public void setSecondaryCurrencyCode(String secondaryCurrencyCode) {
        this.secondaryCurrencyCode = secondaryCurrencyCode;
    }

    public String getIncludeMMInAge() {
        return includeMMInAge;
    }

    public void setIncludeMMInAge(String includeMMInAge) {
        this.includeMMInAge = includeMMInAge;
    }

    public String getStockAdjustmentVTypeCode() {
        return stockAdjustmentVTypeCode;
    }

    public void setStockAdjustmentVTypeCode(String stockAdjustmentVTypeCode) {
        this.stockAdjustmentVTypeCode = stockAdjustmentVTypeCode;
    }

    public String getWj_MsgLength() {
        return wj_MsgLength;
    }

    public void setWj_MsgLength(String wj_MsgLength) {
        this.wj_MsgLength = wj_MsgLength;
    }

    public String getStockAdjustmentVTypeName() {
        return stockAdjustmentVTypeName;
    }

    public void setStockAdjustmentVTypeName(String stockAdjustmentVTypeName) {
        this.stockAdjustmentVTypeName = stockAdjustmentVTypeName;
    }

    public boolean getIsReceiptGenerateWithFullDiscount() {
        return isReceiptGenerateWithFullDiscount;
    }

    public void setIsReceiptGenerateWithFullDiscount(boolean isReceiptGenerateWithFullDiscount) {
        this.isReceiptGenerateWithFullDiscount = isReceiptGenerateWithFullDiscount;
    }

    public boolean isIsOtpRequiredForRefund() {
        return isOtpRequiredForRefund;
    }

    public void setIsOtpRequiredForRefund(boolean isOtpRequiredForRefund) {
        this.isOtpRequiredForRefund = isOtpRequiredForRefund;
    }

    public boolean isIsIcdxMandatory() {
        return isIcdxMandatory;
    }

    public void setIsIcdxMandatory(boolean isIcdxMandatory) {
        this.isIcdxMandatory = isIcdxMandatory;
    }

    public boolean isAppointmentPaymentGatewayAllowed() {
        return appointmentPaymentGatewayAllowed;
    }

    public void setAppointmentPaymentGatewayAllowed(boolean appointmentPaymentGatewayAllowed) {
        this.appointmentPaymentGatewayAllowed = appointmentPaymentGatewayAllowed;
    }

    public boolean isAutoOPDRegAfterAppointment() {
        return autoOPDRegAfterAppointment;
    }

    public void setAutoOPDRegAfterAppointment(boolean autoOPDRegAfterAppointment) {
        this.autoOPDRegAfterAppointment = autoOPDRegAfterAppointment;
    }

    public String getAppointmentBookUptoDays() {
        return appointmentBookUptoDays;
    }

    public void setAppointmentBookUptoDays(String appointmentBookUptoDays) {
        this.appointmentBookUptoDays = appointmentBookUptoDays;
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

    public boolean isRunFromWebsite() {
        return runFromWebsite;
    }

    public void setRunFromWebsite(boolean runFromWebsite) {
        this.runFromWebsite = runFromWebsite;
    }

    public String getEodPostingType() {
        return eodPostingType;
    }

    public void setEodPostingType(String eodPostingType) {
        this.eodPostingType = eodPostingType;
    }

    public boolean isIsServiceUnitEditable() {
        return isServiceUnitEditable;
    }

    public void setIsServiceUnitEditable(boolean isServiceUnitEditable) {
        this.isServiceUnitEditable = isServiceUnitEditable;
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

    public String getLockDate() {
        return lockDate;
    }

    public void setLockDate(String lockDate) {
        this.lockDate = lockDate;
    }
}
