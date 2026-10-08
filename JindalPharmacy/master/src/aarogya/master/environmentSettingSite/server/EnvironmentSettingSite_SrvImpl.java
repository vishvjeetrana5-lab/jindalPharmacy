package aarogya.master.environmentSettingSite.server;

import aarogya.master.environmentService.server.EnviroService_Server;
import aarogya.master.environmentSettingSite.shared.*;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import aarogya.selfToolkit.stStatic.shared.STEnum.DischargeStatus;
import aarogya.selfToolkit.stStatic.shared.STEnum.PurchaseSalesAcPostingBasedOn;
import aarogya.selfToolkit.stStatic.shared.STEnum.QueueAlgoNature;
import aarogya.selfToolkit.stStatic.shared.STEnum.QueueAlgoType;
import aarogya.selfToolkit.stStatic.shared.STEnum.ServiceNature;
import aarogya.selfToolkit.stStatic.shared.STEnum.TokenBlockNature;
import com.google.gson.Gson;
import com.mongodb.gridfs.GridFSInputFile;
import dataman.dmBase.dmFileManager.shared.DmAttachment_Mdl;
import dataman.dmBase.dmFileManagerMongo.server.DmFileManagerMongo;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmBase.self.server.ZipUtils;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.AlphabetCase;
import dataman.dmBase.self.shared.DmEnum.CardType;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EnvironmentSettingSite_SrvImpl extends DmRSSPrivate implements EnvironmentSettingSite_Srv {

    @Override
    public DmResult<String> saveRecord(EnvironmentSettingSite_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;

        dmrTemp.setData("");

        try {
            EnvironmentSettingSite_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoInteger(this, "enviro", cnTemp));
            }
            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            String readFilePath = DmShared.xNull(mrTemp.getServerDirectory()) + "/" + DmShared.xNull(mrTemp.getServerFileName());
            if (readFilePath.equalsIgnoreCase("/")) {
                getDeleteLogo(mrTemp);
            } else {
                getSaveImage(mrTemp);
            }

            String tempNewBornDOB = STServer.getTempDOB(DmShared.iNull(mrTemp.getNewBornDay()).intValue(), DmShared.iNull(mrTemp.getNewBornMonth()).intValue(), DmShared.iNull(mrTemp.getNewBornYear()).intValue(), "");

            String tempInfantDOB = STServer.getTempDOB(0, DmShared.iNull(mrTemp.getInfantMonth()).intValue(), DmShared.iNull(mrTemp.getInfantYear()).intValue(), "");

            String tempChildDOB = STServer.getTempDOB(0, DmShared.iNull(mrTemp.getChildMonth()).intValue(), DmShared.iNull(mrTemp.getChildYear()).intValue(), "");

            sqlQuery = "select dateDiff(dd, '" + DmServer.convertDate(tempChildDOB) + "',:uEntDt) as totalDayChild, dateDiff(dd, '" + DmServer.convertDate(tempNewBornDOB) + "',:uEntDt) as totalDaynewBorn, dateDiff(dd, '" + DmServer.convertDate(tempInfantDOB) + "',:uEntDt) aS totalInfant ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                int totalNoofDayChild = DmShared.iNull(rsTemp.getString("totalDayChild")).intValue();
                int totalNoofDayInfant = DmShared.iNull(rsTemp.getString("totalInfant")).intValue();
                int totalNoofDayNewBorn = DmShared.iNull(rsTemp.getString("totalDaynewBorn")).intValue();

                if (totalNoofDayChild > 0) {
                    if (totalNoofDayChild <= totalNoofDayInfant || totalNoofDayChild <= totalNoofDayNewBorn) {
                        throw new UnsupportedOperationException("Child age can't be less or equal to than New Born ,Infant age.");
                    }
                }
                if (totalNoofDayInfant > 0) {
                    if (totalNoofDayInfant <= totalNoofDayNewBorn) {
                        throw new UnsupportedOperationException("Infant can't be less than or equal to New Born .");
                    }
                }
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "SELECT  site_Code FROM enviro where site_Code = :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            if (!rsTemp.next()) {
                sqlQuery = " INSERT INTO enviro(site_Code) VALUES(:site_Code) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("site_Code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.executeUpdate();
                psTemp.close();
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "update enviro  SET "
                    + " isBillH_Approval = :isBillH_Approval,"
                    + " alphCase_Patient = :alphCase_Patient,"
                    + " alphaCase_Address = :alphaCase_Address,"
                    + " diagnostic_Reg_Addition = :diagnostic_Reg_Addition,"
                    + " caseCode = :caseCode,"
                    + " refConsultantCode = :refConsultantCode,"
                    + " consultantCode = :consultantCode,"
                    + " registrationFee = :registrationFee,"
                    + " documentFooter = :documentFooter,"
                    + " documentFooterRegional = :documentFooterRegional,"
                    + " documentHeader = :documentHeader,"
                    + " showDDUptoMonth = :showDDUptoMonth,"
                    + " showMMUptoYear = :showMMUptoYear ,"
                    + " cancelDaysOPD = :cancelDaysOPD,"
                    + " serviceDaysOPD = :serviceDaysOPD,"
                    + " u_Name = :userName,"
                    + " u_EntDt = :u_EntDt,"
                    + " defaultCity = :defaultCity,"
                    + " defaultNationality = :defaultNationality,"
                    + " isReligionMandatory = :isReligionMandatory,"
                    + " infantUnit = :infantUnit,"
                    + " isIdTypeMandatory = :isIdTypeMandatory, "
                    + " isRoomClean = :isRoomClean, "
                    + " roomPostIsAuto = :roomPostIsAuto, "
                    + " roomPostTimeFormat = :roomPostTimeFormat, "
                    + " roomPostCheckoutTime = :roomPostCheckoutTime, "
                    + " roomPostGraceMinuteIn = :roomPostGraceMinuteIn, "
                    + " roomPostGraceMinuteOut = :roomPostGraceMinuteOut, "
                    + " roomPostHalfDayUpto = :roomPostHalfDayUpto, "
                    + " roomPostPercentage = :roomPostPercentage, "
                    + " IPDControlAc = :IPDControlAc,"
                    + " OPDControlAc = :OPDControlAc,"
                    + " diagnosticControlAc = :diagnosticControlAc,"
                    + " emergencyControlAc = :emergencyControlAc,"
                    + " dayCareControlAc = :dayCareControlAc, "
                    + " directPrescription= :directPrescription, "
                    + " opdNoOfSticker = :opdNoOfSticker,"
                    + " ipdNoOfSticker = :ipdNoOfSticker, "
                    + " diagnosticNoOfSticker = :diagnosticNoOfSticker, "
                    + " isDirectSampleLabCollection= :isDirectSampleLabCollection, "
                    + " pancardLimit= :pancardLimit,"
                    + " eodPostingType=:eodPostingType, "
                    + " cashpaymentLimit= :cashpaymentLimit,"
                    + " isIPDExistingPatient= :isIPDExistingPatient, "
                    + " isLABExistingPatient= :isLABExistingPatient, "
                    + " isPatientInitialMandatory = :isPatientInitialMandatory, "
                    + " isAddressMandatory= :isAddressMandatory, "
                    + " isCityMandatory= :isCityMandatory, "
                    + " isReceiptGenerateWithFullDiscount = :isReceiptGenerateWithFullDiscount, "
                    + " isGuardianInitialMandatory= :isGuardianInitialMandatory, "
                    + " isCasteMandatory= :isCasteMandatory, "
                    + " isMaritalStatusMandatory= :isMaritalStatusMandatory, "
                    + " isBloodGroupMandatory= :isBloodGroupMandatory, "
                    + " isEmailMandatory= :isEmailMandatory, "
                    + " isDiagnosticAutoVerify= :isDiagnosticAutoVerify, "
                    + " isDiagnosticBalancePrintAllow= :isDiagnosticBalancePrintAllow,  "
                    + " nightShiftFromTime= :nightShiftFromTime,  "
                    + " nightShiftToTime= :nightShiftToTime,  "
                    + " newBornDD= :newBornDD, "
                    + " newBornMM = :newBornMM, "
                    + " newBornYY= :newBornYY, "
                    + " infantMM = :infantMM, "
                    + " infantYY = :infantYY, "
                    + " childMM= :childMM, "
                    + " childYY = :childYY, "
                    + " diagnosticHospitalAc =:diagnosticHospitalAc,"
                    + " diagnosticSelfAc=:diagnosticSelfAc,"
                    + " isIPDRoomMandatory= :isIPDRoomMandatory ,"
                    + " orgReceiptCardCode=:orgReceiptCardCode,"
                    + " hospitalHoldCardCode=:hospitalHoldCardCode,"
                    + " isPatientImageMandatory=:isPatientImageMandatory,"
                    + " isAreaMandatory=:isAreaMandatory, "
                    + " isGuardianNameMandatory=:isGuardianNameMandatory,"
                    + " isDiagnosticPrintOnLetterhead=:isDiagnosticPrintOnLetterhead,"
                    + " tokenBlockNature=:tokenBlockNature,"
                    + " isHourlyServiceRoundOff=:isHourlyServiceRoundOff ,"
                    + " disallowType= :disallowType,"
                    + " dietScheduleDays= :dietScheduleDays, "
                    + " dietary_property1= :dietary_property1, "
                    + " dietary_property2= :dietary_property2, "
                    + " dietary_property3= :dietary_property3, "
                    + " dietary_property4= :dietary_property4, "
                    + " dietary_property5= :dietary_property5, "
                    + " dietary_property6= :dietary_property6, "
                    + " opdAllowPatientForSite= :opdAllowPatientForSite, "
                    + " ipdAllowPatientForSite= :ipdAllowPatientForSite, "
                    + " diagnosticAllowPatientForSite= :diagnosticAllowPatientForSite, "
                    + " dayCareAllowPatientForSite= :dayCareAllowPatientForSite, "
                    + " defaultOpdPharmacyDepartmentCode= :defaultOpdPharmacyDepartmentCode, "
                    + " defaultEstimatePackageCode  = :defaultEstimatePackageCode, "
                    + " cancelDaysDiagnostic = :cancelDaysDiagnostic,"
                    + " serviceDaysDiagnostic = :serviceDaysDiagnostic,"
                    + " defaultOpdPharmacySiteCode = :defaultOpdPharmacySiteCode, "
                    + " opdHistoryViewDays = :opdHistoryViewDays, "
                    + " appointmentHistoryViewDays = :appointmentHistoryViewDays,"
                    + " labHistoryViewDays =:labHistoryViewDays , "
                    // + " bloodBagIssueDepartmentCode =:bloodBagIssueDepartmentCode , "
                    //                    + " createComponentDepartmentCode =:createComponentDepartmentCode, "
                    + " secondaryCurrencyCode = :secondaryCurrencyCode, "
                    + " bloodBank_CIP_Addition = :bloodBank_CIP_Addition,"
                    + " OPDQueueAlgo   = :OPDQueueAlgo , "
                    + " OPDValidity    = :OPDValidity ,"
                    + " OPDNoOfVisit   = :OPDNoOfVisit,"
                    + " OPDisRoomWiseRegistration  = :OPDisRoomWiseRegistration, "
                    + " uhidDefaultCardType  = :uhidDefaultCardType, "
                    + " donorDefaultCampCode = :donorDefaultCampCode, "
                    + " isOtpRequiredForRefund = :isOtpRequiredForRefund, "
                    + " isIcdxMandatory = :isIcdxMandatory, "
                    + " isServiceUnitEditable = :isServiceUnitEditable, "
                    + " appointmentBookUptoDays = :appointmentBookUptoDays, "
                    + " appointmentPaymentGatewayAllowed = :appointmentPaymentGatewayAllowed, "
                    + " paymentGatewayURL = :paymentGatewayURL, "
                    + " paymentOption = :paymentOption, "
                    + " autoOPDRegAfterAppointment = :autoOPDRegAfterAppointment, "
                    + " isIncludeTaxInCost = :isIncludeTaxInCost,"
                    + " purchaseSalesAcPostingBasedOn = :purchaseSalesAcPostingBasedOn, "
                    + " runFromWebsite = :runFromWebsite, "
                    + " indentRequired = :indentRequired, "
                    + " requisitionMandatoryInTransfer = :requisitionMandatoryInTransfer, "
                    + " singlePOFlowInPurchase= :singlePOFlowInPurchase, "
                    + " inv_DefaultPurchaseDepartment= :inv_DefaultPurchaseDepartment, "
                    + " autoBedConfirmation= :autoBedConfirmation,"
                    + " asset_DepreciationAc= :asset_DepreciationAc,"
                    + " asset_DepreciationPostOn= :asset_DepreciationPostOn,"
                    + " IsCommonPricelistForSite= :IsCommonPricelistForSite ,"
                    + "  lockDate= :lockDate ,"
                    + "  lockVoucherPrefix= :lockVoucherPrefix "
                    + " where site_Code= :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("isBillH_Approval", DmShared.xNull(mrTemp.getIsBillHApprovalTag()));
            psTemp.setString("IsCommonPricelistForSite", DmShared.xNull(mrTemp.getIsCommonPricelistForSiteTag()));
            psTemp.setString("site_Code", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
            psTemp.setString("alphCase_Patient", DmShared.xNull(mrTemp.getPatientAlphabateCaseCode()));
            psTemp.setString("alphaCase_Address", DmShared.xNull(mrTemp.getAddressAlphabateCaseCode()));
            psTemp.setString("caseCode", DmShared.foriegnKey(mrTemp.getOpdCaseCode()));
            psTemp.setString("refConsultantCode", DmShared.foriegnKey(mrTemp.getOpdRefConsultantCode()));
            psTemp.setString("consultantCode", DmShared.foriegnKey(mrTemp.getOpdConsultantCode()));
            psTemp.setString("registrationFee", DmServer.vNull(mrTemp.getDefaultRegFee(), "0.00"));
            psTemp.setString("documentFooter", DmShared.xNull((mrTemp.getDocumentFooter().trim())));
            psTemp.setString("documentFooterRegional", DmShared.xNull((mrTemp.getDocumentFooterRegional().trim())));
            psTemp.setString("documentHeader", DmShared.xNull((mrTemp.getDocumentHeader().trim())));
            psTemp.setString("showDDUptoMonth", DmShared.iNull(mrTemp.getIncludeDDInAge()).toString());
            psTemp.setString("showMMUptoYear", DmShared.iNull(mrTemp.getIncludeMMInAge()).toString());
            psTemp.setString("defaultCity", DmShared.foriegnKey(mrTemp.getCityCode()));
            psTemp.setString("defaultNationality", DmShared.foriegnKey(mrTemp.getNationalityTag()));
            psTemp.setString("cancelDaysOPD", DmShared.xNull(mrTemp.getCancelDaysOPD()));
            psTemp.setString("serviceDaysOPD", DmShared.xNull(mrTemp.getServiceDaysOPD()));

            psTemp.setString("cancelDaysDiagnostic", DmShared.xNull(mrTemp.getCancelDaysDiagnostic()));
            psTemp.setString("serviceDaysDiagnostic", DmShared.xNull(mrTemp.getDiagnosticDays()));
            psTemp.setString("eodPostingType", DmShared.xNull(mrTemp.getEodPostingTypeCode()));
            psTemp.setString("isReligionMandatory", DmServer.vNull(mrTemp.getOpdIsReligionMandatory(), "0"));
            psTemp.setString("tokenBlockNature", DmShared.iNull(mrTemp.getTokenBlockTag()).toString());
            psTemp.setString("isIdTypeMandatory", DmServer.vNull(mrTemp.getOpdIsIdTypeMandatory(), "0"));
            psTemp.setString("infantUnit", DmShared.foriegnKey(mrTemp.getIpdInfantUnitTag()));
            psTemp.setString("isRoomClean", DmServer.vNull(mrTemp.getIpdIsRoomCleanTag(), "0"));
            psTemp.setString("roomPostIsAuto", DmShared.iNull(mrTemp.getIpdIsAutoRoomTag()).toString());
            psTemp.setString("roomPostTimeFormat", DmShared.xNull(mrTemp.getIpdTimeFormatRoomTag()));
            psTemp.setString("roomPostCheckoutTime", DmServer.vNull(mrTemp.getIpdCheckoutTime(), "0.00"));
            psTemp.setString("roomPostHalfDayUpto", DmServer.vNull(mrTemp.getIpdHalfDayUpToMin(), "0"));
            psTemp.setString("roomPostGraceMinuteIn", DmShared.xNull(mrTemp.getIpdGraceMinuteIn()));
            psTemp.setString("roomPostGraceMinuteOut", DmShared.xNull(mrTemp.getIpdGraceMinuteOut()));
            psTemp.setString("roomPostPercentage", DmShared.xNull(mrTemp.getIpdHalfDayPercentage()));
            psTemp.setString("IPDControlAc", DmShared.foriegnKey(mrTemp.getIpdControlAcTag()));
            psTemp.setString("OPDControlAc", DmShared.foriegnKey(mrTemp.getOpdControlAcTag()));
            psTemp.setString("diagnosticControlAc", DmShared.foriegnKey(mrTemp.getDiagnosticControlAcTag()));
            psTemp.setString("emergencyControlAc", DmShared.foriegnKey(mrTemp.getEmergencyControlAcTag()));
            psTemp.setString("dayCareControlAc", DmShared.foriegnKey(mrTemp.getDayCareControlAcTag()));
            psTemp.setString("directPrescription", DmShared.xNull("0"));
            psTemp.setString("disallowType", DmShared.xNull(mrTemp.getDisallowTypeTag()));
            psTemp.setString("opdNoOfSticker", DmServer.vNull(mrTemp.getOpdNoOfSticker(), "0"));
            psTemp.setString("ipdNoOfSticker", DmServer.vNull(mrTemp.getIpdNoOfSticker(), "0"));
            psTemp.setString("diagnosticNoOfSticker", DmServer.vNull(mrTemp.getDiagnosticNoOfSticker(), "0"));
            psTemp.setString("isDirectSampleLabCollection", DmShared.xNull(mrTemp.getDiagnosticSampleCollectionTag()));
            psTemp.setString("pancardLimit", DmServer.vNull(mrTemp.getPanCardLimit(), "0.00"));
            psTemp.setString("cashpaymentLimit", DmServer.vNull(mrTemp.getCashPaymentLimit(), "0.00"));
            psTemp.setString("isIPDExistingPatient", DmShared.xNull(mrTemp.getIsExistingPatientTag()));
            psTemp.setString("isLABExistingPatient", DmShared.xNull(mrTemp.getIsLABExistingPatientTag()));
            psTemp.setString("isPatientInitialMandatory", DmServer.vNull(mrTemp.getIsPatientInitialTag(), "0"));
            psTemp.setString("isAddressMandatory", DmServer.vNull(mrTemp.getIsAddressTag(), "0"));
            psTemp.setString("isCityMandatory", DmServer.vNull(mrTemp.getIsCityMandatoryTag(), "0"));
            psTemp.setString("isGuardianInitialMandatory", DmServer.vNull(mrTemp.getIsGuardianInitialTag(), "0"));

            psTemp.setString("isServiceUnitEditable", DmServer.vNull(mrTemp.getServiceUnitEditableCode()));
            psTemp.setString("isCasteMandatory", DmServer.vNull(mrTemp.getIsCasteTag(), "0"));
            psTemp.setString("isMaritalStatusMandatory", DmServer.vNull(mrTemp.getIsMaritalStatusTag(), "0"));
            psTemp.setString("isBloodGroupMandatory", DmServer.vNull(mrTemp.getIsBloodGroupTag(), "0"));
            psTemp.setString("isEmailMandatory", DmServer.vNull(mrTemp.getIsEmailTag(), "0"));
            psTemp.setString("isDiagnosticAutoVerify", DmServer.vNull(mrTemp.getDiagnosticIsDiagAutoTag(), "0"));
            psTemp.setString("isDiagnosticBalancePrintAllow", DmServer.vNull(mrTemp.getPrntyesnotag(), "0"));
            psTemp.setString("isIPDRoomMandatory", DmServer.vNull(mrTemp.getRoomMandatoryTag(), "0"));
            psTemp.setString("nightShiftFromTime", DmServer.vNull(mrTemp.getNightshiftFrom(), "0.00"));
            psTemp.setString("nightShiftToTime", DmServer.vNull(mrTemp.getNightshiftTo(), "0.00"));
            psTemp.setString("newBornDD", DmServer.vNull(mrTemp.getNewBornDay(), "0"));
            psTemp.setString("newBornMM", DmServer.vNull(mrTemp.getNewBornMonth(), "0"));
            psTemp.setString("newBornYY", DmServer.vNull(mrTemp.getNewBornYear(), "0"));
            psTemp.setString("infantMM", DmServer.vNull(mrTemp.getInfantMonth(), "0"));
            psTemp.setString("infantYY", DmServer.vNull(mrTemp.getInfantYear(), "0"));
            psTemp.setString("childMM", DmServer.vNull(mrTemp.getChildMonth(), "0"));
            psTemp.setString("childYY", DmServer.vNull(mrTemp.getChildYear(), "0"));
            psTemp.setString("userName", DmServer.getSsnUserName(this));
            psTemp.setString("diagnosticHospitalAc", DmShared.foriegnKey(mrTemp.getDiagnosticHospitalAcTag()));
            psTemp.setString("diagnosticSelfAc", DmShared.foriegnKey(mrTemp.getDiagnosticSelfAcTag()));
            psTemp.setString("diagnostic_Reg_Addition", DmShared.foriegnKey(mrTemp.getDiagnosticRegAdditionTag()));
            psTemp.setString("orgReceiptCardCode", DmShared.foriegnKey(mrTemp.getOrgReceiptCardTypeTag()));
            psTemp.setString("hospitalHoldCardCode", DmShared.foriegnKey(mrTemp.getHospitalHoldCardTypeTag()));
            psTemp.setString("uhidDefaultCardType", DmShared.foriegnKey(mrTemp.getUhidDefaultCardTypeCode()));
            psTemp.setString("isPatientImageMandatory", DmServer.vNull(mrTemp.getIsPatientImgMandatoryTag(), "0"));
            psTemp.setString("isAreaMandatory", DmServer.vNull(mrTemp.getIsAreaMandatoryTag(), "0"));
            psTemp.setString("isGuardianNameMandatory", DmServer.vNull(mrTemp.getIsGuardianNameMandatoryTag(), "0"));
            psTemp.setString("isDiagnosticPrintOnLetterhead", DmServer.vNull(mrTemp.getDiagIsPrintOnLetterheadTag(), "0"));
            psTemp.setString("isHourlyServiceRoundOff", DmServer.vNull(mrTemp.getHourlyServiceRoundOffTag(), "0"));
            psTemp.setString("dietScheduleDays", DmShared.xNull(mrTemp.getDietScheduleDays()));
            psTemp.setString("dietary_property1", DmShared.xNull(mrTemp.getDietaryProperty1()));
            psTemp.setString("dietary_property2", DmShared.xNull(mrTemp.getDietaryProperty2()));
            psTemp.setString("dietary_property3", DmShared.xNull(mrTemp.getDietaryProperty3()));
            psTemp.setString("dietary_property4", DmShared.xNull(mrTemp.getDietaryProperty4()));
            psTemp.setString("dietary_property5", DmShared.xNull(mrTemp.getDietaryProperty5()));
            psTemp.setString("dietary_property6", DmShared.xNull(mrTemp.getDietaryProperty6()));
            psTemp.setString("opdAllowPatientForSite", DmShared.xNull(mrTemp.getOpdAllowPatientForSiteTag()));
            psTemp.setString("ipdAllowPatientForSite", DmShared.xNull(mrTemp.getIpdAllowPatientForSiteTag()));
            psTemp.setString("diagnosticAllowPatientForSite", DmShared.xNull(mrTemp.getDiagAllowPatientForSiteTag()));
            psTemp.setString("dayCareAllowPatientForSite", DmShared.xNull(mrTemp.getDaycAllowPatientForSiteTag()));
            psTemp.setString("defaultOpdPharmacyDepartmentCode", DmShared.xNull(mrTemp.getDefaultOpdPharmacyDepartmentTag()));
            psTemp.setString("defaultEstimatePackageCode", DmShared.foriegnKey(mrTemp.getPackageCode()));
            psTemp.setString("defaultOpdPharmacySiteCode", DmShared.xNull(mrTemp.getDefaultPharmacySiteTag()));

            psTemp.setString("opdHistoryViewDays", DmShared.xNull(mrTemp.getHistoryViewDays()));
            psTemp.setString("appointmentHistoryViewDays", DmShared.xNull(mrTemp.getAppointmentHistoryViewDays()));
            psTemp.setString("labHistoryViewDays", DmShared.xNull(mrTemp.getLabHistoryViewDays()));

//            psTemp.setString("bloodBagIssueDepartmentCode", DmShared.foriegnKey(mrTemp.getBloodBagDepartmentCode()));
//            psTemp.setString("createComponentDepartmentCode", DmShared.foriegnKey(mrTemp.getBloodComponentDepartmentCode()));
            psTemp.setString("bloodBank_CIP_Addition", DmShared.foriegnKey(mrTemp.getBloodBankCipAdditionTag()));
            psTemp.setString("secondaryCurrencyCode", DmShared.foriegnKey(mrTemp.getSecodaryCurrencyTag()));

            psTemp.setString("OPDQueueAlgo", DmShared.foriegnKey(mrTemp.getQueueTag()));
            psTemp.setString("OPDValidity", DmShared.foriegnKey(mrTemp.getValidUpTo()));
            psTemp.setString("OPDNoOfVisit", DmShared.foriegnKey(mrTemp.getNoOfVisit()));
            psTemp.setString("OPDisRoomWiseRegistration", DmShared.iNull(mrTemp.getConsultationRegistrationTag()).toString());

            psTemp.setString("donorDefaultCampCode", DmShared.foriegnKey(mrTemp.getDonorDefaultCampCode()));
            psTemp.setString("isReceiptGenerateWithFullDiscount", DmShared.iNull(mrTemp.getIsReceiptGenerateWithFullDiscountTag()).toString());
            psTemp.setString("isOtpRequiredForRefund", DmServer.vNull(mrTemp.getIsOtpRequiredForRefundTag(), "0"));
            psTemp.setString("isIcdxMandatory", DmServer.vNull(mrTemp.getIsIcdxMandatoryTag(), "0"));
            psTemp.setString("appointmentBookUptoDays", DmServer.vNull(mrTemp.getAppointmentBookUptoDays(), "0"));
            psTemp.setString("appointmentPaymentGatewayAllowed", DmServer.vNull(mrTemp.getAppointmentPaymentGatewayAllowedTag(), "0"));
            psTemp.setString("paymentGatewayURL", DmShared.xNull(mrTemp.getPaymentGatewayURL()));
            psTemp.setString("paymentOption", DmShared.xNull(mrTemp.getPaymentOptionTag()));
            psTemp.setString("autoOPDRegAfterAppointment", DmServer.vNull(mrTemp.getAutoOPDRegAfterAppointmentTag(), "0"));
            psTemp.setString("runFromWebsite", DmServer.vNull(mrTemp.getRunFromWebsiteTag(), "0"));
            psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
            psTemp.setString("isIncludeTaxInCost", DmShared.iNull(mrTemp.getIncludeTaxInCost()).toString());
            psTemp.setString("purchaseSalesAcPostingBasedOn", DmShared.foriegnKey(mrTemp.getPurchaseSalesAcPostingBasedOnCode()));
            psTemp.setString("indentRequired", DmShared.foriegnKey(mrTemp.getIndentRequired()));
            psTemp.setString("requisitionMandatoryInTransfer", DmShared.foriegnKey(mrTemp.getRequisitionMandatoryInTransfer()));
            psTemp.setString("singlePOFlowInPurchase", DmShared.foriegnKey(mrTemp.getSinglePOFlowInPurchase()));
            psTemp.setString("autoBedConfirmation", DmShared.xNull(mrTemp.getAutoBedConfirmation()));
            psTemp.setString("inv_DefaultPurchaseDepartment", DmShared.xNull(mrTemp.getInv_DefaultPurchaseDepartment()));

            psTemp.setString("asset_DepreciationAc", DmShared.foriegnKey(mrTemp.getAsset_DepreciationAcTag()));
            psTemp.setString("asset_DepreciationPostOn", DmShared.xNull(mrTemp.getAsset_DepreciationPostOn()));
            psTemp.setDate("lockDate", DmServer.convertDate(mrTemp.getTransactionLockDate()));
            psTemp.setString("lockVoucherPrefix", DmShared.xNull(mrTemp.getTransactionLockYear()));

            psTemp.executeUpdate();
            psTemp.close();

            ////////////////////////////////////Department Site Link Grid ////////////////////////////////////////////////
            sqlQuery = "Delete From enviroDepartmentSiteLink where site_Code=:siteCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
            psTemp.executeUpdate();
            psTemp.close();
            if (mrTemp.getGrdLinkDept() != null) {
                for (GrdLinkDepartment_Mdl grdDept_Mdl : mrTemp.getGrdLinkDept()) {
                    if (DmShared.iNull(grdDept_Mdl.getSharedKeyFieldCode()).longValue() > 0) {
                        sqlQuery = " Insert Into enviroDepartmentSiteLink(site_Code,departmentCode, "
                                + " link_Site_Code) Values  "
                                + "(:site_Code,:departmentCode,:link_Site_Code) ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("site_Code", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                        psTemp.setString("departmentCode", DmShared.foriegnKey(grdDept_Mdl.getSharedKeyFieldCode()));
                        psTemp.setString("link_Site_Code", DmShared.foriegnKey(grdDept_Mdl.getLinkSiteCode()));
                        psTemp.executeUpdate();
                        psTemp.close();

                    }
                }
            }

            ////////////////////////////////////Status GuiTemplate Grid ////////////////////////////////////////////////
            sqlQuery = "Delete From enviroDischargeCardGUI where site_Code=:siteCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
            psTemp.executeUpdate();
            psTemp.close();
            if (mrTemp.getGrdStatusTemplate() != null) {
                for (GrdStatusGUITemp_Mdl grdStatusTempl_Mdl : mrTemp.getGrdStatusTemplate()) {
                    if (DmShared.iNull(grdStatusTempl_Mdl.getSharedKeyFieldCode()).longValue() > 0 && DmShared.iNull(grdStatusTempl_Mdl.getTemplateCode()).longValue() > 0) {
                        sqlQuery = "insert Into enviroDischargeCardGUI(site_Code,dischargeStatus, guiTemplateCode) "
                                + " values (:site_Code, :dischargeStatus, :guiTemplateCode) ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("site_Code", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                        psTemp.setString("dischargeStatus", DmShared.foriegnKey(grdStatusTempl_Mdl.getSharedKeyFieldCode()));
                        psTemp.setString("guiTemplateCode", DmShared.foriegnKey(grdStatusTempl_Mdl.getTemplateCode()));
                        psTemp.executeUpdate();
                        psTemp.close();
                    }
                }
            }

//            if (mrTemp.getGrdTimingDetail() != null) {
//                for (int k = 0; k < mrTemp.getGrdTimingDetail().size(); k++) {
//                    GrdTiming_Mdl timingNode = mrTemp.getGrdTimingDetail().get(k);
//                    if (!DmShared.xNull(timingNode.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
//                        priceList = getTimeWisePriceList(timingNode, PriceListNature.ReVisit, DmServer.vNull(timingNode.getReVisit(), "0.00"), "1-Jan-2001", mrTemp.getOrganizaton(), mrTemp.getService(), mrTemp.getSharedKeyFieldCode());
//                        lstServicePrice.add(priceList);
//                        if (DmShared.vNull(timingNode.getEmergency()).doubleValue() > 0) {
//                            priceList = getTimeWisePriceList(timingNode, PriceListNature.Emergency, DmServer.vNull(timingNode.getEmergency(), "0.00"), "1-Jan-2001", mrTemp.getOrganizaton(), mrTemp.getService(), mrTemp.getSharedKeyFieldCode());
//                            lstServicePrice.add(priceList);
//                        }
//                        if (DmShared.vNull(timingNode.getOpd()).doubleValue() > 0) {
//                            priceList = getTimeWisePriceList(timingNode, PriceListNature.OPD, DmServer.vNull(timingNode.getOpd(), "0.00"), "1-Jan-2001", mrTemp.getOrganizaton(), mrTemp.getService(), mrTemp.getSharedKeyFieldCode());
//                            lstServicePrice.add(priceList);
//                        }
//                        if (DmShared.vNull(timingNode.getOnlineCharge()).doubleValue() > 0) {
//                            priceList = getTimeWisePriceList(timingNode, PriceListNature.Online, DmServer.vNull(timingNode.getOnlineCharge(), "0.00"), "1-Jan-2001", mrTemp.getOrganizaton(), mrTemp.getService(), mrTemp.getSharedKeyFieldCode());
//                            lstServicePrice.add(priceList);
//                        }
//                        if (DmShared.vNull(timingNode.getAppointment()).doubleValue() > 0) {
//                            priceList = getTimeWisePriceList(timingNode, PriceListNature.PreAppointment, DmServer.vNull(timingNode.getAppointment(), "0.00"), "1-Jan-2001", mrTemp.getOrganizaton(), mrTemp.getService(), mrTemp.getSharedKeyFieldCode());
//                            lstServicePrice.add(priceList);
//                        }
//                        if (DmShared.vNull(timingNode.getAppointmentAdvance()).doubleValue() > 0) {
//                            priceList = getTimeWisePriceList(timingNode, PriceListNature.AppointmentAdvance, DmServer.vNull(timingNode.getAppointmentAdvance(), "0.00"), "1-Jan-2001", mrTemp.getOrganizaton(), mrTemp.getService(), mrTemp.getSharedKeyFieldCode());
//                            lstServicePrice.add(priceList);
//                        }
//                    }
//                }
//            }
//            STServer.savePriceList("1-Jan-2001", PriceListEntryFrom.ENVIRO, mrTemp.getService(), mrTemp.getOrganizaton(), mrTemp.getSharedKeyFieldCode(), lstServicePrice, cnTemp);
//            sqlQuery = "Delete From enviroConsultantTimedetail Where site_code = :siteCode ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("siteCode", mrTemp.getSharedKeyFieldCode());
//            psTemp.executeUpdate();
//            psTemp.close();
//
//            Integer srNo = 0;
//            for (int i = 0; i < mrTemp.getGrdTimingDetail().size(); i++) {
//                ++srNo;
//                GrdTiming_Mdl lst = mrTemp.getGrdTimingDetail().get(i);
//                if (!DmShared.xNull(lst.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
//
//                    sqlQuery = "INSERT INTO enviroConsultantTimedetail ( sNo, dayNameCode, fromTime, toTime, opd, "
//                            + "             emergency, appointment, patient, graceMinutes, reVisit,  "
//                            + "             online, onlinePatient,site_Code, appointmentAdvance) "
//                            + " VALUES ( :sNo, :dayNameCode, :fromTime, :toTime, :opd, :emergency, :appointment, "
//                            + "        :patient, :graceMinutes, :reVisit , :online, :onlinePatient,:siteCode, :appointmentAdvance);";
//                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//                    psTemp.setInt("sNo", srNo);
//                    psTemp.setString("dayNameCode", DmShared.xNull(lst.getSharedKeyFieldCode()));
//                    psTemp.setString("fromTime", DmServer.vNull(lst.getFromTime(), "0.00"));
//                    psTemp.setString("toTime", DmServer.vNull(lst.getToTime(), "0.00"));
//                    psTemp.setString("opd", DmServer.vNull(lst.getOpd(), "0.00"));
//                    psTemp.setString("emergency", DmServer.vNull(lst.getEmergency(), "0.00"));
//                    psTemp.setString("appointment", DmServer.vNull(lst.getAppointment(), "0.00"));
//                    psTemp.setString("appointmentAdvance", DmServer.vNull(lst.getAppointmentAdvance(), "0.00"));
//                    psTemp.setString("patient", DmServer.vNull(lst.getPatient(), "0"));
//                    psTemp.setString("graceMinutes", DmServer.vNull(lst.getGraceMin(), "0"));
//                    psTemp.setString("reVisit", DmServer.vNull(lst.getReVisit(), "0.00"));
//                    psTemp.setString("online", DmServer.vNull(lst.getOnlineCharge(), "0.00"));
//                    psTemp.setString("onlinePatient", DmServer.vNull(lst.getOnlinePatient(), "0"));
//                    psTemp.setString("siteCode", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
//                    psTemp.executeUpdate();
//                    psTemp.close();
//                }
//            }
            sqlQuery = "Delete From consultantOPDQueueDetail Where site_code = :siteCode and consultantCode is null ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();

            for (GrdQueueSequence_Mdl sequenceMdl : mrTemp.getLstQueue()) {
                if (DmShared.iNull(sequenceMdl.getPrioritySerial()).intValue() > 0) {
                    sqlQuery = "insert into consultantOPDQueueDetail (site_Code, opdType, queuePriority, noOfPatient) "
                            + "values (:siteCode, :opdType, :queuePrioriy, :noOfPatient)";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("siteCode", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                    psTemp.setString("opdType", DmShared.foriegnKey(sequenceMdl.getSharedKeyFieldCode()));
                    psTemp.setString("queuePrioriy", DmServer.vNull(sequenceMdl.getPrioritySerial()));
                    psTemp.setString("noOfPatient", DmShared.xNull(sequenceMdl.getTotalPatient()));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }
            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
            spBegin.commit();
            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {

                    psTemp.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSite(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT sm.code AS CODE,sm.name AS Name "
                    + "FROM sitemast sm ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }

                if (psTemp != null) {
                    psTemp.close();
                }

                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHAddAlphaCase(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("L");
            listNode.setSharedKeyFieldName("lower");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("P");
            listNode.setSharedKeyFieldName("Proper");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("S");
            listNode.setSharedKeyFieldName("Sentence");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("U");
            listNode.setSharedKeyFieldName("UPPER");
            lstTemp.add(listNode);

            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCase(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT cm.code AS CODE,cm.name AS Name  "
                    + "FROM casemast cm ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }

                if (psTemp != null) {
                    psTemp.close();
                }

                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHReferred(String siteCode, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "";
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT cm.subcode as code,sg.name "
                    + "FROM consultantmast cm "
                    + " LEFT join subGroup as sg ON cm.subCode = sg.subCode "
                    + " LEFT JOIN subGroupSiteList CSL ON cm.subcode=CSL.subcode "
                    + " WHERE CSL.site_Code=:site_Code AND sg.isActive='1' ";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmShared.xNull(siteCode));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.foriegnKey(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.foriegnKey(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }

                if (psTemp != null) {
                    psTemp.close();
                }

                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHConsultant(String siteCode, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "";
        rstTemp.setData(lstTemp);

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT cm.subcode as code,sg.name "
                    + "FROM consultantmast cm "
                    + " LEFT join subGroup as sg ON cm.subCode = sg.subCode "
                    + " LEFT join subGroupNature as sgn ON sg.nature = sgn.Code "
                    + " LEFT JOIN subGroupSiteList CSL ON cm.subcode=CSL.subcode "
                    + " WHERE CSL.site_Code=:site_Code AND sg.isActive='1' AND sgn.manualCode='CN' ";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmShared.xNull(siteCode));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.foriegnKey(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.foriegnKey(rsTemp.getString("Name")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rsTemp.close();
            psTemp.close();

            rstTemp.setStatus(true);
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHNationality(DmHelper helper, boolean blnToEndOfFile) {

        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("select code, name from nationalityMast ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.foriegnKey(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.foriegnKey(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }

                if (psTemp != null) {
                    psTemp.close();
                }

                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTokenBlockNature(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        try {
            for (TokenBlockNature token : TokenBlockNature.values()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.iNull(token.getId()).toString());
                listNode.setSharedKeyFieldName(token.getValue());
                lstTemp.add(listNode);
            }
            lstTemp = DmShared.filterKeyFieldList(helper, lstTemp);
            rstTemp.setData(lstTemp);
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResult<EnvironmentSettingSite_Mdl> getMoveRec(String strKey, String ruleCode) {
        DmResult<EnvironmentSettingSite_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        GrdQueueSequence_Mdl queue;
        ResultSet rsTemp = null;

        String sqlQuery, defaultPharmacyAllowSiteCode = "", defPharmSiteName = "", opdAllowPatientSiteCode = "", ipdAllowPatientSiteCode = "", diagAllowPatientSiteCode = "",
                daycAllowPatientSiteCode = "", opdSiteName = "", ipdSiteName = "", daycSiteName = "", diagSiteName = "", paymentOptionCode = "", paymentOptionName = "";
        String[] defaultPharmacyAlloSiteCodeArray, opdAllowPatientSiteCodeArray, ipdAllowPatientSiteCodeArray, diagAllowPatientSiteCodeArray,
                daycAllowPatientSiteCodeArray, paymentOptionArray;
        EnvironmentSettingSite_Mdl mrTemp = new EnvironmentSettingSite_Mdl();

        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT  env.isBillH_Approval,sm.code AS site_code, sm.name AS site_name, cam.name AS case_name,cam.code AS case_code,defaultCity,env.eodPostingType ,"
                    + "         case when defaultCity is null then '' else CONCAT(ct.cityName,', ',stm.name,', ',con.name) end AS cityName, "
                    + "         defaultNationality, nm.name as Nationality, sgrefconm.name AS refered_name, refconm.subcode AS refered_code, "
                    + "         sgconm.name AS consultant_name, conm.subcode AS consultant_code, env.alphCase_Patient, env.alphaCase_Address, "
                    + "         env.u_Name, FORMAT(env.u_EntDt,'dd-MMM-yyyy HH:mm') AS u_EntDt,env.registrationFee, env.documentFooter, env.documentFooterRegional, "
                    + "         env.documentHeader, env.nightShiftFromTime, env.nightShiftToTime, env.showDDUptoMonth, env.showMMUptoYear ,env.pancardLimit, "
                    + "         env.cashpaymentLimit, env.isIPDExistingPatient, env.isLABExistingPatient, env.cancelDaysOPD, env.serviceDaysOPD, env.isReligionMandatory, "
                    + "         env.isIdTypeMandatory,env.infantUnit,um.unit, env.isRoomClean, env.roomPostIsAuto, env.roomPostTimeFormat, "
                    + "         env.roomPostCheckoutTime,env.roomPostGraceMinuteIn,roomPostGraceMinuteOut,env.roomPostHalfDayUpto, "
                    + "         env.roomPostPercentage, sgIPDCon.name AS sgIPDCon,sgOPDCon.name AS sgOPDCon,sgDiaCon.name AS sgDiaCon, "
                    + "         env.IPDControlAc,env.OPDControlAc,env.diagnosticControlAc, "
                    + "         env.directPrescription, env.opdNoOfSticker,env.ipdNoOfSticker,env.diagnosticNoOfSticker, "
                    + "         env.isDirectSampleLabCollection, env.isPatientInitialMandatory, env.isAddressMandatory, env.isDiagnosticAutoVerify, "
                    + "         env.isDiagnosticBalancePrintAllow,env.isIPDRoomMandatory, env.isCityMandatory, env.isReceiptGenerateWithFullDiscount, env.isGuardianInitialMandatory, "
                    + "         env.isCasteMandatory, env.isMaritalStatusMandatory, env.isBloodGroupMandatory, env.isEmailMandatory, "
                    + "         env.emergencyControlAc,sgEmerCon.name as sgEmerCon, env.dayCareControlAc,sgDayCareCon.name as sgDayCareCon, "
                    + "         env.newBornDD,env.newBornMM,env.newBornYY, env.infantMM, env.infantYY, env.childMM, env.childYY, "
                    + "         env.diagnostic_Reg_Addition,psm.name, env.orgReceiptCardCode, orgRecCard.name as orgRecCardName, "
                    + "         env.hospitalHoldCardCode,hosHoldCard.name as hosHoldCardName, env.isPatientImageMandatory, env.isAreaMandatory, "
                    + "         env.isGuardianNameMandatory,env.isDiagnosticPrintOnLetterhead, env.diagnosticHospitalAc, sgDHos.name as sgDHosAc, "
                    + "         env.diagnosticSelfAc,sgDSf.name as sgDSelfAc, env.isHourlyServiceRoundOff, env.tokenBlockNature, env.disallowType, "
                    + "         env.dietScheduleDays, env.dietary_property1, env.dietary_property2, env.dietary_property3, "
                    + "         env.dietary_property4, env.dietary_property5, env.dietary_property6, env.opdAllowPatientForSite, "
                    + "         env.ipdAllowPatientForSite, env.diagnosticAllowPatientForSite, "
                    + "         env.dayCareAllowPatientForSite, env.defaultOpdPharmacyDepartmentCode, env.cancelDaysDiagnostic, "
                    + "         env.serviceDaysDiagnostic, env.isOtpRequiredForRefund, env.isIcdxMandatory,env.uhidDefaultCardType, "
                    + "         uhidCT.name as uhidDefaultCardTypeName,  "
                    // + " env.bloodBagIssueDepartmentCode,env.createComponentDepartmentCode,"
                    //                    + "          sgBBD.name as bloodBagIssueDepartmentName ,"
                    //                    + "          sgCD.name as createComponentDepartmentName,  "
                    + "         pm.name as packageName, env.defaultEstimatePackageCode as packageCode, "
                    + "         env.defaultOpdPharmacySiteCode ,env.labHistoryViewDays,env.opdHistoryViewDays,env.appointmentHistoryViewDays, "
                    + "         env.bloodBank_CIP_Addition, env.secondaryCurrencyCode, psma.name as  bloodBank_CIP_AdditionName, cur.name as secondaryCurrencyName , "
                    + "         env.OPDQueueAlgo , env.OPDValidity ,env.OPDNoOfVisit , env.OPDisRoomWiseRegistration  ,env.isServiceUnitEditable, env.donorDefaultCampCode ,"
                    + "         bdcm.name  as donorDefaultCampName, env.appointmentBookUptoDays, env.appointmentPaymentGatewayAllowed, "
                    + "         env.paymentGatewayURL, env.paymentOption, env.autoOPDRegAfterAppointment, env.runFromWebsite, "
                    + "         env.isIncludeTaxInCost, env.purchaseSalesAcPostingBasedOn, env.indentRequired, "
                    + "         env.requisitionMandatoryInTransfer,env.singlePOFlowInPurchase, env.autoBedConfirmation,env.inv_DefaultPurchaseDepartment, "
                    + "         sgDPD.name as invDefaultPurchaseDepartment,env.IsCommonPricelistForSite,env.appointmentCountMonthWise,"
                    + "         env.asset_DepreciationAc,sgAsset.name as assetDepreciationAcName,env.asset_DepreciationPostOn , "
                    + "         env.lockDate ,env.lockVoucherPrefix "
                    + "         FROM  enviro AS env "
                    + "         LEFT JOIN sitemast AS sm ON (env.site_Code = sm.code) "
                    //                    + "		LEFT JOIN subgroup sgBBD on sgBBD.subCode =env.bloodBagIssueDepartmentCode"
                    //                    + "		LEFT JOIN subgroup sgCD on sgCD.subCode =env.createComponentDepartmentCode"
                    + "         LEFT JOIN subgroup AS sgPhSf ON (env.pharmacySelfAc = sgPhSf.subcode) "
                    + "         LEFT JOIN subgroup AS sgDHos ON (env.diagnosticHospitalAc = sgDHos.subcode) "
                    + "         LEFT JOIN subgroup AS sgDSf ON (env.diagnosticSelfAc = sgDSf.subcode) "
                    + "         LEFT JOIN consultantmast AS conm ON (env.consultantCode = conm.subcode) "
                    + "         LEFT join subGroup as sgconm ON conm.subCode = sgconm.subCode "
                    + "         LEFT JOIN consultantmast AS refconm ON (env.refConsultantCode = refconm.subcode) "
                    + "         LEFT join subGroup as sgrefconm ON refconm.subCode = sgrefconm.subCode "
                    + "         LEFT JOIN subgroup AS sgIPDCon ON (env.IPDControlAc = sgIPDCon.subcode) "
                    + "         LEFT JOIN subgroup AS sgOPDCon ON (env.OPDControlAc = sgOPDCon.subcode) "
                    + "         LEFT JOIN subgroup AS sgDiaCon ON (env.diagnosticControlAc = sgDiaCon.subcode) "
                    + "         LEFT JOIN subgroup AS sgEmerCon ON (env.emergencyControlAc = sgEmerCon.subcode) "
                    + "         LEFT JOIN subgroup AS sgDayCareCon ON (env.dayCareControlAc = sgDayCareCon.subcode) "
                    + "         LEFT JOIN subGroupsitelist ON subGroupsitelist.subcode = conm.subcode AND subGroupsitelist.site_Code = sm.code "
                    + "         LEFT JOIN overHeadMast ohm ON (env.pharmacyFinalDiscountOHCode = ohm.code) "
                    + "         LEFT JOIN casemast AS cam ON (env.caseCode = cam.code) "
                    + "         LEFT JOIN city ct on ct.cityCode = env.defaultCity "
                    + "         Left Join country con on ct.countryCode=con.code "
                    + "         Left Join stateMast stm on ct.stateCode=stm.code "
                    + "         LEFT JOIN nationalityMast nm on nm.code = env.defaultNationality "
                    + "         LEFT JOIN unitMast as um on um.code = env.infantUnit "
                    + "         LEFT JOIN patientServiceMast psm on env.diagnostic_Reg_Addition = psm.code "
                    + "         LEFT JOIN cardTypeMast as orgRecCard on env.orgReceiptCardCode=orgRecCard.code "
                    + "         LEFT JOIN cardTypeMast as hosHoldCard on env.hospitalHoldCardCode=hosHoldCard.code "
                    + "         LEFT JOIN cardTypeMast as uhidCT on env.uhidDefaultCardType=uhidCT.code "
                    + "         LEFT JOIN packageMast as pm on env.defaultEstimatePackageCode = pm.code "
                    + "         LEFT JOIN currency as cur on env.secondaryCurrencyCode = cur.code "
                    + "         LEFT JOIN patientServiceMast as psma on env.bloodBank_CIP_Addition = psma.code"
                    + "         LEFT JOIN bloodDonationCampMast bdcm on env.donorDefaultCampCode = bdcm.code "
                    + "         LEFT JOIN subgroup AS sgDPD ON (env.inv_DefaultPurchaseDepartment = sgDPD.subcode) "
                    + "         LEFT JOIN subgroup AS sgAsset ON (env.asset_DepreciationAc = sgAsset.subcode) "
                    + "         WHERE env.site_Code = :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("site_code")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("site_name")));

                if (DmShared.xNull(rsTemp.getString("isReligionMandatory")).equalsIgnoreCase("1")) {
                    mrTemp.setOpdIsReligionMandatory("1");
                    mrTemp.setOpdIsReligionMandatoryName("Yes");
                } else {
                    mrTemp.setOpdIsReligionMandatory("0");
                    mrTemp.setOpdIsReligionMandatoryName("No");
                }
                if (DmShared.xNull(rsTemp.getString("isIdTypeMandatory")).equalsIgnoreCase("1")) {
                    mrTemp.setOpdIsIdTypeMandatory("1");
                    mrTemp.setOpdIsIdTypeMandatoryName("Yes");
                } else {
                    mrTemp.setOpdIsIdTypeMandatory("0");
                    mrTemp.setOpdIsIdTypeMandatoryName("No");
                }
                mrTemp.setTransactionLockDate(DmShared.dNull(rsTemp.getDate("lockDate")));
                mrTemp.setTransactionLockYear(DmShared.xNull(rsTemp.getString("lockVoucherPrefix")));
                
                
                mrTemp.setDiagnosticControlAcTag(DmShared.xNull(rsTemp.getString("diagnosticControlAc")));
                mrTemp.setDiagnosticControlAcText(DmShared.xNull(rsTemp.getString("sgDiaCon")));
                mrTemp.setEmergencyControlAcTag(DmShared.xNull(rsTemp.getString("emergencyControlAc")));
                mrTemp.setEmergencyControlAcText(DmShared.xNull(rsTemp.getString("sgEmerCon")));
                mrTemp.setDayCareControlAcTag(DmShared.xNull(rsTemp.getString("dayCareControlAc")));
                mrTemp.setDayCareControlAcText(DmShared.xNull(rsTemp.getString("sgDayCareCon")));
                mrTemp.setOpdControlAcTag(DmShared.xNull(rsTemp.getString("OPDControlAc")));
                mrTemp.setOpdControlAcText(DmShared.xNull(rsTemp.getString("sgOPDCon")));
                mrTemp.setIpdControlAcTag(DmShared.xNull(rsTemp.getString("IPDControlAc")));
                mrTemp.setIpdControlAcText(DmShared.xNull(rsTemp.getString("sgIPDCon")));

                mrTemp.setPackageCode(DmShared.xNull(rsTemp.getString("packageCode")));
                mrTemp.setPackageName(DmShared.xNull(rsTemp.getString("packageName")));
                mrTemp.setDiagnosticIsDiagAutoTag(DmShared.xNull(rsTemp.getString("isDiagnosticAutoVerify")));

                mrTemp.setDiagnosticIsDiagAutoText(DmShared.xNull(rsTemp.getString("isDiagnosticAutoVerify")).equalsIgnoreCase("0") ? "No"
                        : (DmShared.xNull(rsTemp.getString("isDiagnosticAutoVerify")).equalsIgnoreCase("1") ? "Yes" : ""));

                mrTemp.setPrntyesnotag(DmShared.xNull(rsTemp.getString("isDiagnosticBalancePrintAllow")));

                mrTemp.setPrntyesnotext(DmShared.xNull(rsTemp.getString("isDiagnosticBalancePrintAllow")).equalsIgnoreCase("0") ? "No"
                        : (DmShared.xNull(rsTemp.getString("isDiagnosticBalancePrintAllow")).equalsIgnoreCase("1") ? "Yes" : ""));

                mrTemp.setPatientAlphabateCaseCode(DmShared.xNull(rsTemp.getString("alphCase_Patient")));
                mrTemp.setPatientAlphabateCaseName(DmShared.xNull(rsTemp.getString("alphCase_Patient")).equalsIgnoreCase("U") ? "UPPER"
                        : (DmShared.xNull(rsTemp.getString("alphCase_Patient")).equalsIgnoreCase("L") ? "lower"
                        : (DmShared.xNull(rsTemp.getString("alphCase_Patient")).equalsIgnoreCase("S") ? "Sentence"
                        : (DmShared.xNull(rsTemp.getString("alphCase_Patient")).equalsIgnoreCase("P") ? "Proper" : ""))));

                mrTemp.setAddressAlphabateCaseCode(DmShared.xNull(rsTemp.getString("alphaCase_Address")));
                mrTemp.setAddressAlphabateCaseName(DmShared.xNull(rsTemp.getString("alphaCase_Address")).equalsIgnoreCase("U") ? "UPPER"
                        : (DmShared.xNull(rsTemp.getString("alphaCase_Address")).equalsIgnoreCase("L") ? "lower"
                        : (DmShared.xNull(rsTemp.getString("alphaCase_Address")).equalsIgnoreCase("S") ? "Sentence"
                        : (DmShared.xNull(rsTemp.getString("alphaCase_Address")).equalsIgnoreCase("P") ? "Proper" : ""))));

                mrTemp.setOpdCaseCode(DmShared.xNull(rsTemp.getString("case_code")));
                mrTemp.setOpdCaseName(DmShared.xNull(rsTemp.getString("case_name")));
                mrTemp.setOpdRefConsultantCode(DmShared.xNull(rsTemp.getString("refered_code")));
                mrTemp.setOpdRefConsultantName(DmShared.xNull(rsTemp.getString("refered_name")));
                mrTemp.setOpdConsultantCode(DmShared.xNull(rsTemp.getString("consultant_code")));
                mrTemp.setOpdConsultantName(DmShared.xNull(rsTemp.getString("consultant_name")));
                mrTemp.setuName(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setDefaultRegFee(DmShared.xNull(rsTemp.getString("registrationFee")));
                mrTemp.setDocumentFooter(DmShared.xNull(rsTemp.getString("documentFooter")));
                mrTemp.setDocumentFooterRegional(DmShared.xNull(rsTemp.getString("documentFooterRegional")));
                mrTemp.setDocumentHeader(DmShared.xNull(rsTemp.getString("documentHeader")));
                mrTemp.setIncludeDDInAge(DmShared.xNull(rsTemp.getString("showDDUptoMonth")));
                mrTemp.setIncludeMMInAge(DmShared.xNull(rsTemp.getString("showMMUptoYear")));
                mrTemp.setCityCode(DmShared.xNull(rsTemp.getString("defaultCity")));
                mrTemp.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                mrTemp.setNationalityTag(DmShared.xNull(rsTemp.getString("defaultNationality")));
                mrTemp.setNationalityText(DmShared.xNull(rsTemp.getString("Nationality")));
                mrTemp.setIpdInfantUnitTag(DmShared.xNull(rsTemp.getString("infantUnit")));
                mrTemp.setIpdInfantUnitText(DmShared.xNull(rsTemp.getString("unit")));
                mrTemp.setCancelDaysDiagnostic(DmShared.xNull(rsTemp.getString("cancelDaysDiagnostic")));
                mrTemp.setDiagnosticDays(DmShared.xNull(rsTemp.getString("serviceDaysDiagnostic")));

                mrTemp.setLabHistoryViewDays(DmShared.xNull(rsTemp.getString("labHistoryViewDays")));
                mrTemp.setHistoryViewDays(DmShared.xNull(rsTemp.getString("opdHistoryViewDays")));
                mrTemp.setAppointmentHistoryViewDays(DmShared.xNull(rsTemp.getString("appointmentHistoryViewDays")));

                mrTemp.setCancelDaysOPD(DmShared.xNull(rsTemp.getString("cancelDaysOPD")));
                mrTemp.setServiceDaysOPD(DmShared.xNull(rsTemp.getString("serviceDaysOPD")));
                mrTemp.setNewBornDay(DmServer.vNull(rsTemp.getString("newBornDD"), "0"));
                mrTemp.setNewBornMonth(DmServer.vNull(rsTemp.getString("newBornMM"), "0"));
                mrTemp.setNewBornYear(DmServer.vNull(rsTemp.getString("newBornYY"), "0"));
                mrTemp.setInfantMonth(DmServer.vNull(rsTemp.getString("infantMM"), "0"));
                mrTemp.setInfantYear(DmServer.vNull(rsTemp.getString("infantYY"), "0"));
                mrTemp.setChildMonth(DmServer.vNull(rsTemp.getString("childMM"), "0"));
                mrTemp.setChildYear(DmServer.vNull(rsTemp.getString("childYY"), "0"));
                mrTemp.setDiagnosticHospitalAcTag(DmShared.xNull(rsTemp.getString("diagnosticHospitalAc")));
                mrTemp.setDiagnosticHospitalAcText(DmShared.xNull(rsTemp.getString("sgDHosAc")));
                mrTemp.setDiagnosticSelfAcTag(DmShared.xNull(rsTemp.getString("diagnosticSelfAc")));
                mrTemp.setDiagnosticSelfAcText(DmShared.xNull(rsTemp.getString("sgDSelfAc")));
                mrTemp.setIsExistingPatientTag(DmShared.xNull(rsTemp.getString("isIPDExistingPatient")));
                mrTemp.setIsExistingPatientText(DmShared.xNull(rsTemp.getString("isIPDExistingPatient")).equalsIgnoreCase("0") ? "No"
                        : (DmShared.xNull(rsTemp.getString("isIPDExistingPatient")).equalsIgnoreCase("1") ? "Yes" : ""));

                mrTemp.setIsLABExistingPatientTag(DmShared.xNull(rsTemp.getString("isLABExistingPatient")));
                mrTemp.setIsLABExistingPatientText(DmShared.xNull(rsTemp.getString("isLABExistingPatient")).equalsIgnoreCase("0") ? "No"
                        : (DmShared.xNull(rsTemp.getString("isLABExistingPatient")).equalsIgnoreCase("1") ? "Yes" : ""));

                mrTemp.setIsBillHApprovalTag(DmShared.xNull(rsTemp.getString("isBillH_Approval")));
                mrTemp.setIsBillHApprovalText(DmShared.xNull(rsTemp.getString("isBillH_Approval")).equalsIgnoreCase("0") ? "No"
                        : (DmShared.xNull(rsTemp.getString("isBillH_Approval")).equalsIgnoreCase("1") ? "Yes" : ""));

                mrTemp.setIsCommonPricelistForSiteTag(DmShared.xNull(rsTemp.getString("IsCommonPricelistForSite")));
                mrTemp.setIsCommonPricelistForSiteText(DmShared.xNull(rsTemp.getString("IsCommonPricelistForSite")).equalsIgnoreCase("0") ? "No"
                        : (DmShared.xNull(rsTemp.getString("IsCommonPricelistForSite")).equalsIgnoreCase("1") ? "Yes" : ""));

                mrTemp.setDirectPrescriptionTag(DmShared.xNull(rsTemp.getString("directPrescription")));
                mrTemp.setDirectPrescriptionText(DmShared.xNull(rsTemp.getString("directPrescription")).equalsIgnoreCase("0") ? "Disallow"
                        : (DmShared.xNull(rsTemp.getString("directPrescription")).equalsIgnoreCase("1") ? "Allow" : ""));

                mrTemp.setIpdIsRoomCleanTag(DmServer.vNull(rsTemp.getString("isRoomClean"), "0"));
                mrTemp.setIpdIsRoomCleanText(DmServer.vNull(rsTemp.getString("isRoomClean"), "0").equalsIgnoreCase("1") ? "Clean" : "Dirty");

                mrTemp.setIpdCheckoutTime(DmServer.vNull(rsTemp.getString("roomPostCheckoutTime"), "00.00"));
                mrTemp.setIpdGraceMinuteIn(DmShared.xNull(rsTemp.getString("roomPostGraceMinuteIn")));
                mrTemp.setIpdGraceMinuteOut(DmShared.xNull(rsTemp.getString("roomPostGraceMinuteOut")));
                mrTemp.setIpdHalfDayPercentage(DmShared.xNull(rsTemp.getString("roomPostPercentage")));
                mrTemp.setIpdIsAutoRoomTag(DmShared.iNull(rsTemp.getString("roomPostIsAuto")).toString());
                mrTemp.setIpdIsAutoRoomText(DmShared.iNull(mrTemp.getIpdIsAutoRoomTag()).intValue() == 1 ? "Auto" : "Manual");
                mrTemp.setIpdTimeFormatRoomTag(DmShared.iNull(rsTemp.getString("roomPostTimeFormat")).toString());
                mrTemp.setDiagnosticRegAdditionTag(DmShared.xNull(rsTemp.getString("diagnostic_Reg_Addition")));
                mrTemp.setDiagnosticRegAdditionText(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.setEodPostingTypeCode(DmShared.xNull(rsTemp.getString("eodPostingType")));
                mrTemp.setEodPostingTypeName(DmShared.xNull(rsTemp.getString("eodPostingType")));
                mrTemp.setTokenBlockTag(DmShared.xNull(rsTemp.getString("tokenBlockNature")));
//                if (DmShared.iNull(rsTemp.getString("tokenBlockNature")).intValue() != 0) {
                mrTemp.setTokenBlockText(TokenBlockNature.getKeyOf(DmShared.iNull(rsTemp.getString("tokenBlockNature")).intValue()).getValue());
//                }
                mrTemp.setIpdTimeFormatRoomText(DmShared.xNull(mrTemp.getIpdTimeFormatRoomTag()).equalsIgnoreCase("0") ? "24 Hours"
                        : DmShared.xNull(mrTemp.getIpdTimeFormatRoomTag()).equalsIgnoreCase("1") ? "Checkout Time" : "");
                mrTemp.setIpdHalfDayUpToMin(DmShared.iNull(rsTemp.getString("roomPostHalfDayUpto")).toString());

                if (!DmShared.xNull(rsTemp.getString("u_EntDt")).equalsIgnoreCase("")) {
                    String date = DmShared.xNull(rsTemp.getString("u_EntDt"));
                    String[] a = date.split(" ");
                    mrTemp.setDate(a[0]);
                    mrTemp.setTime(DmShared.xNull(a[1]));
                } else {
                    mrTemp.setDate("");
                    mrTemp.setTime("");
                }

                mrTemp.setDisallowTypeTag(DmShared.iNull(rsTemp.getString("disallowType")).toString());
                mrTemp.setDisallowTypeText(DmShared.iNull(rsTemp.getString("disallowType")).intValue() == 1 ? "Disallow (Reg. wise)" : "Disallow (Service wise)");
                mrTemp.setOpdNoOfSticker(DmServer.vNull(rsTemp.getString("opdNoOfSticker"), "0"));
                mrTemp.setIpdNoOfSticker(DmServer.vNull(rsTemp.getString("ipdNoOfSticker"), "0"));
                mrTemp.setDiagnosticNoOfSticker(DmServer.vNull(rsTemp.getString("diagnosticNoOfSticker"), "0"));
                mrTemp.setDiagnosticSampleCollectionTag(DmShared.xNull(rsTemp.getString("isDirectSampleLabCollection")));
                mrTemp.setDiagnosticSampleCollectionText(DmShared.xNull(rsTemp.getString("isDirectSampleLabCollection")).equalsIgnoreCase("0") ? "No"
                        : (DmShared.xNull(rsTemp.getString("isDirectSampleLabCollection")).equalsIgnoreCase("1") ? "Yes" : ""));

                mrTemp.setPanCardLimit(DmServer.vNull(rsTemp.getString("pancardLimit"), "0.00"));
                mrTemp.setCashPaymentLimit(DmServer.vNull(rsTemp.getString("cashpaymentLimit"), "0.00"));
                mrTemp.setNightshiftFrom(DmServer.vNull(rsTemp.getString("nightShiftFromTime"), "00.00"));
                mrTemp.setNightshiftTo(DmServer.vNull(rsTemp.getString("nightShiftToTime"), "00.00"));
                if (DmShared.xNull(rsTemp.getString("isPatientInitialMandatory")).equalsIgnoreCase("1")) {
                    mrTemp.setIsPatientInitialTag("1");
                    mrTemp.setIsPatientInitialText("Yes");
                } else {
                    mrTemp.setIsPatientInitialTag("0");
                    mrTemp.setIsPatientInitialText("No");
                }
                if (DmShared.xNull(rsTemp.getString("isIPDRoomMandatory")).equalsIgnoreCase("1")) {
                    mrTemp.setRoomMandatoryTag("1");
                    mrTemp.setRoomMandatoryText("Yes");
                } else {
                    mrTemp.setRoomMandatoryTag("0");
                    mrTemp.setRoomMandatoryText("No");
                }
                if (DmShared.xNull(rsTemp.getString("isAddressMandatory")).equalsIgnoreCase("1")) {
                    mrTemp.setIsAddressTag("1");
                    mrTemp.setIsAddressText("Yes");
                } else {
                    mrTemp.setIsAddressTag("0");
                    mrTemp.setIsAddressText("No");
                }

                if (DmShared.xNull(rsTemp.getString("isCityMandatory")).equalsIgnoreCase("1")) {
                    mrTemp.setIsCityMandatoryTag("1");
                    mrTemp.setIsCityMandatoryText("Yes");
                } else {
                    mrTemp.setIsCityMandatoryTag("0");
                    mrTemp.setIsCityMandatoryText("No");
                }
                if (DmShared.xNull(rsTemp.getString("isReceiptGenerateWithFullDiscount")).equalsIgnoreCase("1")) {
                    mrTemp.setIsReceiptGenerateWithFullDiscountTag("1");
                    mrTemp.setIsReceiptGenerateWithFullDiscountText("Yes");
                } else {
                    mrTemp.setIsReceiptGenerateWithFullDiscountTag("0");
                    mrTemp.setIsReceiptGenerateWithFullDiscountText("No");
                }

                if (DmShared.xNull(rsTemp.getString("isGuardianInitialMandatory")).equalsIgnoreCase("1")) {
                    mrTemp.setIsGuardianInitialTag("1");
                    mrTemp.setIsGuardianInitialText("Yes");
                } else {
                    mrTemp.setIsGuardianInitialTag("0");
                    mrTemp.setIsGuardianInitialText("No");
                }

                if (DmShared.xNull(rsTemp.getString("isCasteMandatory")).equalsIgnoreCase("1")) {
                    mrTemp.setIsCasteTag("1");
                    mrTemp.setIsCasteText("Yes");
                } else {
                    mrTemp.setIsCasteTag("0");
                    mrTemp.setIsCasteText("No");
                }

                if (DmShared.xNull(rsTemp.getString("isMaritalStatusMandatory")).equalsIgnoreCase("1")) {
                    mrTemp.setIsMaritalStatusTag("1");
                    mrTemp.setIsMaritalStatusText("Yes");
                } else {
                    mrTemp.setIsMaritalStatusTag("0");
                    mrTemp.setIsMaritalStatusText("No");
                }

                if (DmShared.xNull(rsTemp.getString("isBloodGroupMandatory")).equalsIgnoreCase("1")) {
                    mrTemp.setIsBloodGroupTag("1");
                    mrTemp.setIsBloodGroupText("Yes");
                } else {
                    mrTemp.setIsBloodGroupTag("0");
                    mrTemp.setIsBloodGroupText("No");
                }

                if (DmShared.xNull(rsTemp.getString("isEmailMandatory")).equalsIgnoreCase("1")) {
                    mrTemp.setIsEmailTag("1");
                    mrTemp.setIsEmailText("Yes");
                } else {
                    mrTemp.setIsEmailTag("0");
                    mrTemp.setIsEmailText("No");
                }

                mrTemp.setOrgReceiptCardTypeTag(DmShared.xNull(rsTemp.getString("orgReceiptCardCode")));
                mrTemp.setOrgReceiptCardTypeText(DmShared.xNull(rsTemp.getString("orgRecCardName")));
                mrTemp.setHospitalHoldCardTypeTag(DmShared.xNull(rsTemp.getString("hospitalHoldCardCode")));
                mrTemp.setHospitalHoldCardTypeText(DmShared.xNull(rsTemp.getString("hosHoldCardName")));
                mrTemp.setUhidDefaultCardTypeCode(DmShared.xNull(rsTemp.getString("uhidDefaultCardType")));
                mrTemp.setUhidDefaultCardName(DmShared.xNull(rsTemp.getString("uhidDefaultCardTypeName")));

                if (DmShared.xNull(rsTemp.getString("isPatientImageMandatory")).equalsIgnoreCase("1")) {
                    mrTemp.setIsPatientImgMandatoryTag("1");
                    mrTemp.setIsPatientImgMandatoryText("Yes");
                } else {
                    mrTemp.setIsPatientImgMandatoryTag("0");
                    mrTemp.setIsPatientImgMandatoryText("No");
                }
                if (DmShared.xNull(rsTemp.getString("isAreaMandatory")).equalsIgnoreCase("1")) {
                    mrTemp.setIsAreaMandatoryTag("1");
                    mrTemp.setIsAreaMandatoryText("Yes");
                } else {
                    mrTemp.setIsAreaMandatoryTag("0");
                    mrTemp.setIsAreaMandatoryText("No");
                }

                if (DmShared.xNull(rsTemp.getString("isGuardianNameMandatory")).equalsIgnoreCase("1")) {
                    mrTemp.setIsGuardianNameMandatoryTag("1");
                    mrTemp.setIsGuardianNameMandatoryText("Yes");
                } else {
                    mrTemp.setIsGuardianNameMandatoryTag("0");
                    mrTemp.setIsGuardianNameMandatoryText("No");
                }
                if (DmShared.xNull(rsTemp.getString("isDiagnosticPrintOnLetterhead")).equalsIgnoreCase("1")) {
                    mrTemp.setDiagIsPrintOnLetterheadTag("1");
                    mrTemp.setDiagIsPrintOnLetterheadText("Yes");
                } else {
                    mrTemp.setDiagIsPrintOnLetterheadTag("0");
                    mrTemp.setDiagIsPrintOnLetterheadText("No");
                }
                if (DmShared.xNull(rsTemp.getString("isOtpRequiredForRefund")).equalsIgnoreCase("1")) {
                    mrTemp.setIsOtpRequiredForRefundTag("1");
                    mrTemp.setIsOtpRequiredForRefundText("Yes");
                } else {
                    mrTemp.setIsOtpRequiredForRefundTag("0");
                    mrTemp.setIsOtpRequiredForRefundText("No");
                }
                if (DmShared.xNull(rsTemp.getString("isIcdxMandatory")).equalsIgnoreCase("1")) {
                    mrTemp.setIsIcdxMandatoryTag("1");
                    mrTemp.setIsIcdxMandatoryText("Yes");
                } else {
                    mrTemp.setIsIcdxMandatoryTag("0");
                    mrTemp.setIsIcdxMandatoryText("No");
                }
                if (DmShared.xNull(rsTemp.getString("isServiceUnitEditable")).equalsIgnoreCase("1")) {
                    mrTemp.setServiceUnitEditableCode("1");
                    mrTemp.setServiceUnitEditableName("Yes");
                } else {
                    mrTemp.setServiceUnitEditableCode("0");
                    mrTemp.setServiceUnitEditableName("No");
                }

                mrTemp.setHourlyServiceRoundOffTag(DmShared.xNull(rsTemp.getString("isHourlyServiceRoundOff")));
                mrTemp.setHourlyServiceRoundOffText(DmShared.xNull(rsTemp.getString("isHourlyServiceRoundOff")).equalsIgnoreCase("0") ? "Normal"
                        : (DmShared.xNull(rsTemp.getString("isHourlyServiceRoundOff")).equalsIgnoreCase("1") ? "Higher"
                        : (DmShared.xNull(rsTemp.getString("isHourlyServiceRoundOff")).equalsIgnoreCase("2") ? "Lower" : "")));
                mrTemp.setDietScheduleDays(DmShared.xNull(rsTemp.getString("dietScheduleDays")));
                mrTemp.setDietaryProperty1(DmShared.xNull(rsTemp.getString("dietary_property1")));
                mrTemp.setDietaryProperty2(DmShared.xNull(rsTemp.getString("dietary_property2")));
                mrTemp.setDietaryProperty3(DmShared.xNull(rsTemp.getString("dietary_property3")));
                mrTemp.setDietaryProperty4(DmShared.xNull(rsTemp.getString("dietary_property4")));
                mrTemp.setDietaryProperty5(DmShared.xNull(rsTemp.getString("dietary_property5")));
                mrTemp.setDietaryProperty6(DmShared.xNull(rsTemp.getString("dietary_property6")));
                mrTemp.setOpdAllowPatientForSiteTag(DmShared.xNull(rsTemp.getString("opdAllowPatientForSite")));
                mrTemp.setIpdAllowPatientForSiteTag(DmShared.xNull(rsTemp.getString("ipdAllowPatientForSite")));
                mrTemp.setDiagAllowPatientForSiteTag(DmShared.xNull(rsTemp.getString("diagnosticAllowPatientForSite")));
                mrTemp.setDaycAllowPatientForSiteTag(DmShared.xNull(rsTemp.getString("dayCareAllowPatientForSite")));
                mrTemp.setDefaultPharmacySiteTag(DmShared.xNull(rsTemp.getString("defaultOpdPharmacySiteCode")));

//                mrTemp.setBloodBagDepartmentCode(DmShared.xNull(rsTemp.getString("bloodBagIssueDepartmentCode")));
//                mrTemp.setBloodComponentDepartmentCode(DmShared.xNull(rsTemp.getString("createComponentDepartmentCode")));
//                mrTemp.setBloodBagDepartmentText(DmShared.xNull(rsTemp.getString("bloodBagIssueDepartmentName")));
//                mrTemp.setBloodComponentDepartmentText(DmShared.xNull(rsTemp.getString("createComponentDepartmentName")));
                mrTemp.setSecodaryCurrencyTag(DmShared.xNull(rsTemp.getString("secondaryCurrencyCode")));
                mrTemp.setSecodaryCurrencyText(DmShared.xNull(rsTemp.getString("secondaryCurrencyName")));
                mrTemp.setBloodBankCipAdditionTag(DmShared.xNull(rsTemp.getString("bloodBank_CIP_Addition")));
                mrTemp.setBloodBankCipAdditionText(DmShared.xNull(rsTemp.getString("bloodBank_CIP_AdditionName")));

                if (DmShared.iNull(rsTemp.getString("OPDQueueAlgo")).intValue() > 0) {
                    mrTemp.setQueueTag(DmShared.iNull(rsTemp.getString("OPDQueueAlgo")).toString());
                    mrTemp.setQueueText(QueueAlgoNature.getKeyOf(DmShared.iNull(rsTemp.getString("OPDQueueAlgo")).intValue()).getValue());
                }

                mrTemp.setNoOfVisit(DmShared.xNull(rsTemp.getString("OPDNoOfVisit")));
                mrTemp.setValidUpTo(DmServer.vNull(rsTemp.getString("OPDValidity"), "0"));

                if (DmShared.iNull(rsTemp.getString("OPDisRoomWiseRegistration")).intValue() == 1) {
                    mrTemp.setConsultationRegistrationTag("1");
                    mrTemp.setConsultationRegistrationText("Consultation Room Wise");
                } else {
                    mrTemp.setConsultationRegistrationTag("0");
                    mrTemp.setConsultationRegistrationText("Consultant Wise");
                }

                mrTemp.setAsset_DepreciationAcTag(DmShared.xNull(rsTemp.getString("asset_DepreciationAc")));
                mrTemp.setAsset_DepreciationAcText(DmShared.xNull(rsTemp.getString("assetDepreciationAcName")));
                if (DmShared.xNull(rsTemp.getString("asset_DepreciationPostOn")).equalsIgnoreCase("1")) {
                    mrTemp.setAsset_DepreciationPostOn("1");
                    mrTemp.setAsset_DepreciationPostOnText("Purchase Date");
                } else {
                    mrTemp.setAsset_DepreciationPostOn("1");
                    mrTemp.setAsset_DepreciationPostOnText("Installation Date");
                }

                mrTemp.setDonorDefaultCampCode(DmShared.xNull(rsTemp.getString("donorDefaultCampCode")));
                mrTemp.setDonorDefaultCampName(DmShared.xNull(rsTemp.getString("donorDefaultCampName")));

                mrTemp.setIncludeTaxInCost(DmShared.xNull(rsTemp.getString("isIncludeTaxInCost")));
                if (!DmShared.xNull(rsTemp.getString("purchaseSalesAcPostingBasedOn")).trim().equalsIgnoreCase("")) {
                    mrTemp.setPurchaseSalesAcPostingBasedOnCode(DmShared.xNull(rsTemp.getString("purchaseSalesAcPostingBasedOn")));
                    mrTemp.setPurchaseSalesAcPostingBasedOnName(DmShared.xNull(PurchaseSalesAcPostingBasedOn.getViaKey(DmShared.iNull(rsTemp.getString("purchaseSalesAcPostingBasedOn")).intValue()).getValue()));
                }
                mrTemp.setIndentRequired(DmShared.xNull(rsTemp.getString("indentRequired")));
                mrTemp.setRequisitionMandatoryInTransfer(DmShared.xNull(rsTemp.getString("requisitionMandatoryInTransfer")));
                mrTemp.setSinglePOFlowInPurchase(DmShared.xNull(rsTemp.getString("singlePOFlowInPurchase")));
                mrTemp.setAutoBedConfirmation(DmShared.xNull(rsTemp.getString("autoBedConfirmation")));

                mrTemp.setInv_DefaultPurchaseDepartmentText(DmShared.xNull(rsTemp.getString("inv_DefaultPurchaseDepartment")));
                mrTemp.setInv_DefaultPurchaseDepartmentText(DmShared.xNull(rsTemp.getString("invDefaultPurchaseDepartment")));
                //--------------------------- OPD Allow Patient For Site ---------------------------------------------
                opdAllowPatientSiteCode = DmShared.xNull(mrTemp.getOpdAllowPatientForSiteTag());
                if (!DmShared.xNull(opdAllowPatientSiteCode).equalsIgnoreCase("")) {
                    opdAllowPatientSiteCodeArray = opdAllowPatientSiteCode.split(",");
                    if (opdAllowPatientSiteCodeArray != null) {
                        for (String siteCode : opdAllowPatientSiteCodeArray) {
                            if (DmShared.iNull(siteCode).intValue() > 0) {
                                opdSiteName += getSiteName(siteCode, cnTemp);
                                opdSiteName += ", ";

                            }
                        }
                    }
                }

                if (opdSiteName.endsWith(", ")) {
                    opdSiteName = opdSiteName.substring(0, opdSiteName.lastIndexOf(","));
                }
                //--------------------------- END ---------------------------------------------
                //--------------------------- IPD Allow Patient For Site ---------------------------------------------
                ipdAllowPatientSiteCode = DmShared.xNull(mrTemp.getIpdAllowPatientForSiteTag());
                if (!DmShared.xNull(ipdAllowPatientSiteCode).equalsIgnoreCase("")) {
                    ipdAllowPatientSiteCodeArray = ipdAllowPatientSiteCode.split(",");
                    if (ipdAllowPatientSiteCodeArray != null) {
                        for (String siteCode : ipdAllowPatientSiteCodeArray) {
                            if (DmShared.iNull(siteCode).intValue() > 0) {
                                ipdSiteName += getSiteName(siteCode, cnTemp);
                                ipdSiteName += ", ";

                            }
                        }
                    }
                }

                if (ipdSiteName.endsWith(", ")) {
                    ipdSiteName = ipdSiteName.substring(0, ipdSiteName.lastIndexOf(","));
                }
                //--------------------------- END ---------------------------------------------
                //--------------------------- Diagnostic Allow Patient For Site ---------------------------------------------
                diagAllowPatientSiteCode = DmShared.xNull(mrTemp.getDiagAllowPatientForSiteTag());
                if (!DmShared.xNull(diagAllowPatientSiteCode).equalsIgnoreCase("")) {
                    diagAllowPatientSiteCodeArray = diagAllowPatientSiteCode.split(",");
                    if (diagAllowPatientSiteCodeArray != null) {
                        for (String siteCode : diagAllowPatientSiteCodeArray) {
                            if (DmShared.iNull(siteCode).intValue() > 0) {
                                diagSiteName += getSiteName(siteCode, cnTemp);
                                diagSiteName += ", ";

                            }
                        }
                    }
                }

                if (diagSiteName.endsWith(", ")) {
                    diagSiteName = diagSiteName.substring(0, diagSiteName.lastIndexOf(","));
                }
                //--------------------------- END ---------------------------------------------
                //--------------------------- DayCare Allow Patient For Site ---------------------------------------------
                daycAllowPatientSiteCode = DmShared.xNull(mrTemp.getDaycAllowPatientForSiteTag());
                if (!DmShared.xNull(daycAllowPatientSiteCode).equalsIgnoreCase("")) {
                    daycAllowPatientSiteCodeArray = daycAllowPatientSiteCode.split(",");
                    if (daycAllowPatientSiteCodeArray != null) {
                        for (String siteCode : daycAllowPatientSiteCodeArray) {
                            if (DmShared.iNull(siteCode).intValue() > 0) {
                                daycSiteName += getSiteName(siteCode, cnTemp);
                                daycSiteName += ", ";

                            }
                        }
                    }
                }

                if (daycSiteName.endsWith(", ")) {
                    daycSiteName = daycSiteName.substring(0, daycSiteName.lastIndexOf(","));
                }
                //--------------------------- END ---------------------------------------------
                mrTemp.setOpdAllowPatientForSiteText(DmShared.xNull(opdSiteName));
                mrTemp.setIpdAllowPatientForSiteText(DmShared.xNull(ipdSiteName));
                mrTemp.setDiagAllowPatientForSiteText(DmShared.xNull(diagSiteName));
                mrTemp.setDaycAllowPatientForSiteText(DmShared.xNull(daycSiteName));

                //--------------------------- Default Pharmacy Department  ---------------------------------------------
                mrTemp.setDefaultOpdPharmacyDepartmentTag(DmShared.xNull(rsTemp.getString("defaultOpdPharmacyDepartmentCode")));
                if (!DmShared.xNull(mrTemp.getDefaultOpdPharmacyDepartmentTag()).equalsIgnoreCase("")) {
                    mrTemp.setDefaultOpdPharmacyDepartmentText(getDepartmentName(mrTemp.getDefaultOpdPharmacyDepartmentTag(), cnTemp));
                }

                defaultPharmacyAllowSiteCode = DmShared.xNull(mrTemp.getDefaultPharmacySiteTag());
                if (!DmShared.xNull(defaultPharmacyAllowSiteCode).equalsIgnoreCase("")) {
                    defaultPharmacyAlloSiteCodeArray = defaultPharmacyAllowSiteCode.split(",");
                    if (defaultPharmacyAlloSiteCodeArray != null) {
                        for (String PharmSiteCode : defaultPharmacyAlloSiteCodeArray) {
                            if (DmShared.iNull(PharmSiteCode).intValue() > 0) {
                                defPharmSiteName += getSiteName(PharmSiteCode, cnTemp);
                                defPharmSiteName += ", ";

                            }
                        }
                    }
                }

                if (defPharmSiteName.endsWith(", ")) {
                    defPharmSiteName = defPharmSiteName.substring(0, defPharmSiteName.lastIndexOf(","));
                }

                mrTemp.setDefaultPharmacySiteText(DmShared.xNull(defPharmSiteName));

                mrTemp.setAppointmentBookUptoDays(DmShared.xNull(rsTemp.getString("appointmentBookUptoDays")));
                if (DmShared.xNull(rsTemp.getString("appointmentPaymentGatewayAllowed")).equalsIgnoreCase("1")) {
                    mrTemp.setAppointmentPaymentGatewayAllowedTag("1");
                    mrTemp.setAppointmentPaymentGatewayAllowedText("Yes");
                } else {
                    mrTemp.setAppointmentPaymentGatewayAllowedTag("0");
                    mrTemp.setAppointmentPaymentGatewayAllowedText("No");
                }
                mrTemp.setAppointmentCountMonthwise(rsTemp.getString("appointmentCountMonthWise"));
                mrTemp.setPaymentGatewayURL(DmShared.xNull(rsTemp.getString("paymentGatewayURL")));

                mrTemp.setPaymentOptionTag(DmShared.xNull(rsTemp.getString("paymentOption")));
                paymentOptionCode = DmShared.xNull(rsTemp.getString("paymentOption"));
                if (!paymentOptionCode.equalsIgnoreCase("")) {
                    paymentOptionArray = paymentOptionCode.split(",");
                    if (paymentOptionArray != null) {
                        for (String optionCode : paymentOptionArray) {
                            if (DmShared.iNull(optionCode).intValue() > 0) {
                                paymentOptionName += getPaymentOption(DmShared.iNull(optionCode).intValue());
                                paymentOptionName += ", ";

                            }
                        }
                    }
                }
                if (paymentOptionName.endsWith(", ")) {
                    paymentOptionName = paymentOptionName.substring(0, paymentOptionName.lastIndexOf(","));
                }
                mrTemp.setPaymentOptionText(DmShared.xNull(paymentOptionName));

                if (DmShared.xNull(rsTemp.getString("autoOPDRegAfterAppointment")).equalsIgnoreCase("1")) {
                    mrTemp.setAutoOPDRegAfterAppointmentTag("1");
                    mrTemp.setAutoOPDRegAfterAppointmentText("Yes");
                } else {
                    mrTemp.setAutoOPDRegAfterAppointmentTag("0");
                    mrTemp.setAutoOPDRegAfterAppointmentText("No");
                }

                if (DmShared.xNull(rsTemp.getString("runFromWebsite")).equalsIgnoreCase("1")) {
                    mrTemp.setRunFromWebsiteTag("1");
                    mrTemp.setRunFromWebsiteText("Yes");
                } else {
                    mrTemp.setRunFromWebsiteTag("0");
                    mrTemp.setRunFromWebsiteText("No");
                }
            }

            rsTemp.close();
            psTemp.close();

            //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< LINK DEPARTMENT GRID  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
            mrTemp.getGrdLinkDept().addAll(getLinkDepartment(strKey, cnTemp));
            mrTemp.getGrdStatusTemplate().addAll(getDischargeStatus(strKey, cnTemp));

            //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< Check whether File Uploaded or not >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
            GrdDiagnosticFile_Mdl node = new GrdDiagnosticFile_Mdl();
            node.setSharedKeyFieldCode("1");
            node.setSharedKeyFieldName("With letterhead");
            mrTemp.getGrdDiagnosticFilelist().add(node);

            node = new GrdDiagnosticFile_Mdl();
            node.setSharedKeyFieldCode("2");
            node.setSharedKeyFieldName("Without letterhead");
            mrTemp.getGrdDiagnosticFilelist().add(node);

            sqlQuery = "SELECT docId, sNo, fileLink, ruleCode, fileName "
                    + " FROM LINKFILECOLLECTION "
                    + " WHERE docId = :docId and sNo in (1,2) and ruleCode = :ruleCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            psTemp.setString("ruleCode", DmShared.xNull(ruleCode));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                for (GrdDiagnosticFile_Mdl nodMdl : mrTemp.getGrdDiagnosticFilelist()) {
                    if (DmShared.iNull(rsTemp.getString("sNo")).intValue() == DmShared.iNull(nodMdl.getSharedKeyFieldCode()).intValue()) {
                        nodMdl.setFileName(DmShared.xNull(rsTemp.getString("fileName")));
                    }
                }
            }
            rsTemp.close();
            psTemp.close();

//            GrdTiming_Mdl timing;
//            sqlQuery = " SELECT ctd.dayNameCode, ctd.fromTime, ctd.toTime, ctd.emergency, ctd.appointment, ctd.patient, ctd.graceMinutes, "
//                    + "         ctd.reVisit,  ctd.online, ctd.onlinePatient, ctd.opd, ctd.appointmentAdvance "
//                    + "         FROM enviroConsultantTimeDetail AS ctd   "
//                    + "         WHERE ctd.site_code = :siteCode    "
//                    + "         ORDER BY ctd.sNo ;";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("siteCode", DmShared.xNull(strKey));
////            psTemp.setDate("applicableFrom", DmServer.convertDate(mrTemp.getApplicableFrom()));
//            rsTemp = psTemp.executeQuery();
//            while (rsTemp.next()) {
//                timing = new GrdTiming_Mdl();
//                timing.setEmergency(DmServer.vNull(rsTemp.getString("emergency"), "0"));
//                timing.setFromTime(DmServer.vNull(rsTemp.getString("fromTime"), "00.00"));
//                timing.setGraceMin(DmServer.vNull(rsTemp.getString("graceMinutes"), "0"));
//                timing.setOpd(DmServer.vNull(rsTemp.getString("opd"), "0"));
//                timing.setAppointment(DmServer.vNull(rsTemp.getString("appointment"), "0"));
//                timing.setAppointmentAdvance(DmServer.vNull(rsTemp.getString("appointmentAdvance"), "0"));
//                timing.setPatient(DmServer.vNull(rsTemp.getString("patient"), "0"));
//                timing.setReVisit(DmServer.vNull(rsTemp.getString("reVisit"), "0"));
//                timing.setToTime(DmServer.vNull(rsTemp.getString("toTime"), "00.00"));
//                timing.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("dayNameCode")));
//                timing.setSharedKeyFieldName(DmWeekdays.getValueFrom(DmShared.iNull(rsTemp.getString("dayNameCode")).intValue()).getWeekDayName());
//                timing.setOnlineCharge(DmServer.vNull(rsTemp.getString("online"), "0.00"));
//                timing.setOnlinePatient(DmServer.vNull(rsTemp.getString("onlinePatient"), "0"));
//                mrTemp.getGrdTimingDetail().add(timing);
//            }
//            psTemp.close();
//            rsTemp.close();
            sqlQuery = " select cqd.consultantCode, cqd.opdType, cqd.queuePriority, cqd.noOfPatient "
                    + "         from consultantOPDQueueDetail cqd "
                    + "         where cqd.site_Code = :siteCode  and cqd.consultantCode is null"
                    + "         order by cqd.queuePriority ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                queue = new GrdQueueSequence_Mdl();
                queue.setPrioritySerial(DmServer.vNull(rsTemp.getString("queuePriority")));
                queue.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("opdType")));
                queue.setSharedKeyFieldName(QueueAlgoType.getKeyOf(DmShared.iNull(rsTemp.getString("opdType")).intValue()).getValue());
                queue.setTotalPatient(DmShared.iNull(rsTemp.getString("noOfPatient")).toString());
                mrTemp.getLstQueue().add(queue);
            }
            psTemp.close();
            rsTemp.close();

            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

    private String getSiteName(String siteCode, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "", siteName = "";
        try {
            sqlQuery = "SELECT name as siteName"
                    + " FROM siteMast  "
                    + " where  code=:siteCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", siteCode);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                siteName = DmShared.xNull(rsTemp.getString("siteName"));
            }
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }

            } catch (Exception e) {

            }
        }

        return siteName;
    }

    private String getDepartmentName(String departmentCodeList, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "", departmentName = "";
        try {
            sqlQuery = "SELECT name as departmentName"
                    + " FROM subgroup  "
                    + " where  subcode in (" + DmServer.getParamList(":department", departmentCodeList) + ") ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setStringList("department", departmentCodeList);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                if (DmShared.xNull(departmentName).equalsIgnoreCase("")) {
                    departmentName = DmShared.xNull(rsTemp.getString("departmentName"));
                } else {
                    departmentName += ", " + DmShared.xNull(rsTemp.getString("departmentName"));
                }
            }

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }

            } catch (Exception e) {

            }
        }

        return departmentName;
    }

    private List<GrdLinkDepartment_Mdl> getLinkDepartment(String siteCode, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        List<GrdLinkDepartment_Mdl> lstTemp = new ArrayList<>();
        GrdLinkDepartment_Mdl node;
        try {

            //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< LINK DEPARTMENT GRID  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
            sqlQuery = "select  eDSL.departmentCode,sg.name as deptName,edSL.link_Site_Code,sm.name as siteName "
                    + "         from enviroDepartmentSiteLink as eDSL "
                    + "         left join enviro as env on env.site_Code=eDSL.site_Code "
                    + "         left join subGroup as sg on eDSl.departmentCode=sg.subCode "
                    + "         left join siteMast as sm on eDSL.link_Site_Code=sm.code "
                    + "         where env.site_Code = :site_Code "
                    + "         order by sg.name,sm.name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmShared.iNull(siteCode).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdLinkDepartment_Mdl();
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("deptName")));
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("departmentCode")));
                node.setLinkSiteCode(DmShared.xNull(rsTemp.getString("link_Site_Code")));
                node.setLinkSiteName(DmShared.xNull(rsTemp.getString("siteName")));
                lstTemp.add(node);
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return lstTemp;
    }

    private List<GrdStatusGUITemp_Mdl> getDischargeStatus(String strKey, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        List<GrdStatusGUITemp_Mdl> lstTemp = new ArrayList<>();
        GrdStatusGUITemp_Mdl node = null;
        try {
            for (DischargeStatus discharge : DischargeStatus.values()) {
                node = new GrdStatusGUITemp_Mdl();
                node.setSharedKeyFieldCode(DmShared.iNull(discharge.getId()).toString());
                node.setSharedKeyFieldName(DischargeStatus.getKeyOf(discharge.getId()).getValue());
                lstTemp.add(node);
            }

            sqlQuery = "select  edcg.dischargeStatus, gtm.code, gtm.name "
                    + "         from enviroDischargeCardGUI edcg "
                    + "         left join guiTemplateMast gtm on gtm.code = edcg.guiTemplateCode "
                    + "         where site_Code = :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                for (DischargeStatus discharge : DischargeStatus.values()) {
                    if (discharge == DischargeStatus.getKeyOf(DmShared.iNull(rsTemp.getString("dischargeStatus")).intValue())
                            && DmShared.iNull(rsTemp.getString("code")).longValue() > 0) {
                        node = new GrdStatusGUITemp_Mdl();
                        node.setSharedKeyFieldCode(DmShared.iNull(discharge.getId()).toString());
                        node.setSharedKeyFieldName(DischargeStatus.getKeyOf(DmShared.iNull(rsTemp.getString("dischargeStatus")).intValue()).getValue());
                        node.setTemplateCode(DmShared.xNull(rsTemp.getString("code")));
                        node.setTemplateName(DmShared.xNull(rsTemp.getString("name")));
                        lstTemp.set(lstTemp.indexOf(node), node);
                        break;
                    }
                }
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return lstTemp;
    }

    @Override
    public DmResult<DefaultValue_Mdl> getDefaultValue() {
        DmResultPageing<DefaultValue_Mdl> dmrTemp = new DmResultPageing();
        Connection cnTemp = null;
        String siteCode, sqlQuery;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DefaultValue_Mdl mrTemp = new DefaultValue_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            siteCode = DmServer.getSsnSiteCode(this);
            sqlQuery = "SELECT sm.name AS site_name from sitemast sm "
                    + " WHERE sm.Code=:site_Code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", siteCode);
            rsTemp = psTemp.executeQuery();

            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(siteCode));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("site_name")));
                mrTemp.setImagePath(DmServer.Path.images);
            }
            psTemp.close();
            rsTemp.close();

            dmrTemp.setStatus(true);
            cnTemp.close();
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHyesNo(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Yes");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("No");
            lstTemp.add(listNode);

            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHServiceUnitEditable(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Yes");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("No");
            lstTemp.add(listNode);

            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDirectPrescription(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Allow");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("Disallow");
            lstTemp.add(listNode);

            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHTemplate(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT Code,Name FROM guiTemplateMast ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.foriegnKey(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.foriegnKey(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rsTemp.close();
            psTemp.close();

            rstTemp.setStatus(true);
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile, boolean isPharmacy) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String condition = "";
        rstTemp.setData(lstTemp);

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (isPharmacy) {
                condition = " and sgn.manualCode in ('CM','PH')";
            }

            sqlWithParam.setSQL("select sg.subCode, sg.name "
                    + "  from departmentMast dep"
                    + "  left join subgroup sg on sg.subCode = dep.subCode"
                    + "  left join subGroupNature as sgn on sgn.code = sg.nature "
                    + "  left join subGroupSiteList sgsl on sgsl.subCode = sg.subCode"
                    + "  where sg.isActive = 1 and sgsl.site_Code = :siteCode " + condition);
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.foriegnKey(rsTemp.getString("subCode")));
                listNode.setSharedKeyFieldName(DmShared.foriegnKey(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rsTemp.close();
            psTemp.close();

            rstTemp.setStatus(true);
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    private void getSaveImage(EnvironmentSettingSite_Mdl mrTemp) {
        try {

            String readFilePath = DmShared.xNull(mrTemp.getServerDirectory()) + "/" + DmShared.xNull(mrTemp.getServerFileName());
            String writeDirectoryPath = DmServer.Path.images + "/" + mrTemp.getDestSiteCode();
            String writelFilePath = writeDirectoryPath + "/" + EnvironmentSettingSite_Mdl.getReceiptLogo();

            if (readFilePath.equalsIgnoreCase(writelFilePath)) {
                return;
            }
            File rFile = new File(readFilePath);
            if (!rFile.exists()) {
                getDeleteLogo(mrTemp);
            } else {
                File wFile = new File(writeDirectoryPath);
                if (!wFile.exists()) {
                    wFile.mkdirs();
                }
                FileInputStream fis = null;
                FileOutputStream fos = null;
                try {
                    fis = new FileInputStream(readFilePath);
                    fos = new FileOutputStream(writelFilePath);
                    byte[] bytes = new byte[2 * 1024 * 1024];
                    int readLen = fis.read(bytes);

                    while (readLen != -1) {
                        if (readLen >= bytes.length) {
                            fos.write(bytes);
                        } else if (readLen < bytes.length) {
                            fos.write(bytes, 0, readLen);
                        }
                        readLen = fis.read(bytes);
                    }
                } catch (Exception exc) {
                    exc.printStackTrace();
                } finally {
                    if (fis != null) {
                        fis.close();
                        fis = null;
                    }
                    if (fos != null) {
                        fos.close();
                        fos = null;
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void getDeleteLogo(EnvironmentSettingSite_Mdl mrTemp) {

        try {
            String deleteDirectoryPath = DmServer.Path.images + "/" + DmShared.iNull(mrTemp.getSharedKeyFieldCode());
            String deleteFilePath = deleteDirectoryPath + "/" + EnvironmentSettingSite_Mdl.getReceiptLogo();
            File dFile = new File(deleteFilePath);
            if (dFile.exists()) {
                dFile.delete();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public DmResultPageing<List<TxtHCity_Mdl>> getTxtHCity(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHCity_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHCity_Mdl> lstTemp = new ArrayList<>();
        TxtHCity_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("SELECT c.cityCode, c.cityName, c.stateCode, c.countryCode, con.name as CountryName, sm.name as stateName, "
                    + "         CONCAT(c.cityName,', ',sm.name,', ',con.name) as cityDetails "
                    + "         FROM city c "
                    + "         Left Join country con on c.countryCode=con.code "
                    + "         Left Join stateMast sm on c.stateCode=sm.code ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHCity_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("cityCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("cityDetails")));
                listNode.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                listNode.setStateCode(DmShared.xNull(rsTemp.getString("stateCode")));
                listNode.setStateName(DmShared.xNull(rsTemp.getString("stateName")));
                listNode.setCountryCode(DmShared.xNull(rsTemp.getString("countryCode")));
                listNode.setCountryName(DmShared.xNull(rsTemp.getString("CountryName")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCountry(DmHelper helper, boolean blnToEndOfFile) {

        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("select code, name from country ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.foriegnKey(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.foriegnKey(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }

                if (psTemp != null) {
                    psTemp.close();
                }

                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSecondaryCurrency(DmHelper helper, boolean blnToEndOfFile) {

        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("select code, name from currency ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.foriegnKey(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.foriegnKey(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }

                if (psTemp != null) {
                    psTemp.close();
                }

                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHInfantUnit(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("select code, unit from unitmast");

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("unit")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }

                if (psTemp != null) {
                    psTemp.close();
                }

                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHRoomVacant(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Clean");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("Dirty");
            lstTemp.add(listNode);

            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHIsManualRoom(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Auto");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("Manual");
            lstTemp.add(listNode);

            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHTimeFormatRoom(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("24 Hours");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Checkout Time");
            lstTemp.add(listNode);

            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHAccountName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL(" SELECT dm.subcode AS code, dm.name FROM subgroup AS dm  ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rsTemp.close();

            psTemp.close();
            rstTemp.setStatus(true);
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getServices(DmHelper helper, boolean blnToEndOfFile, ServiceNature[] serviceNatures, String serviceCategory) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String condition = "", filterNature = "";
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            if (serviceNatures != null) {
                if (serviceNatures.length > 0) {
                    for (ServiceNature nature : serviceNatures) {
                        if (filterNature.equalsIgnoreCase("")) {
                            filterNature = DmShared.iNull(nature.getId()).toString();
                        } else {
                            filterNature += "," + DmShared.iNull(nature.getId()).toString();
                        }
                    }
                }
            }
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (!filterNature.equalsIgnoreCase("")) {
                condition += " and psm.serviceNature in (" + filterNature + ")";
            }
            if (DmShared.iNull(serviceCategory).longValue() > 0) {
                condition += " and psm.categoryCode = :categoryCode";
            }
            sqlWithParam.setSQL("select  psm.code, psm.name "
                    + "		from patientServiceMast psm "
                    + "		left join patientServiceSiteList pssl on pssl.code = psm.code "
                    + "		where pssl.site_Code = :siteCode and psm.isActive = 1" + condition);
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            if (DmShared.iNull(serviceCategory).longValue() > 0) {
                sqlWithParam.setString("categoryCode", serviceCategory);
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rsTemp.close();

            psTemp.close();
            rstTemp.setStatus(true);
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCardType(CardType cardType, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String condition = "";
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {

            if (cardType != null) {
                condition += " and ctm.cardType = :cardType ";
            }
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL(" select ctm.code,ctm.name from cardTypeMast ctm where ctm.isActive=1  " + condition);
            if (cardType != null) {
                sqlWithParam.setInteger("cardType", cardType.getEnumValue());
            }

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rsTemp.close();

            psTemp.close();
            rstTemp.setStatus(true);
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHHourlyServiceRoundOff(DmHelper helper, boolean blnToEndOfFile
    ) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("Normal");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Higher");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("2");
            listNode.setSharedKeyFieldName("Lower");
            lstTemp.add(listNode);

            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDisallowType(DmHelper helper, boolean blnToEndOfFile
    ) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Disallow (Reg. wise)");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("Disallow (Service wise)");
            lstTemp.add(listNode);

            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHPackage(DmHelper helper, boolean blnToEndOfFile
    ) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<KeyFieldExtended> lstTemp = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended node;
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("Select	pm.code, pm.name, pm.shortName "
                    + "		from packageMast pm "
                    + "         left join patientServiceSiteList psl on psl.code = pm.code"
                    + "		where pm.nature = 0 and psl.site_Code = :siteCode");
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new KeyFieldExtended();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                node.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("shortName")));

                lstTemp.add(node);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResult<FileUpload_Mdl> uploadFile(FileUpload_Mdl mrTemp
    ) {
        DmResult<FileUpload_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        byte[] byteBuffer;
        dmrTemp.setData(mrTemp);

        int READ_BUFFER_SIZE = 1 * 1024 * 1024;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            DmFileManagerMongo fileManagerMongo = DmServer.getConnectionMongo(DmServer.getSsnFileDBName(this));
            GridFSInputFile fsInputFile = fileManagerMongo.createFileOnDBFolder(mrTemp.getRuleCode());
            mrTemp.setFileLink(fsInputFile.getId().toString());

            File file = new File(mrTemp.getDirectoryPath() + File.separatorChar + mrTemp.getServerFileName());

            FileInputStream fis = new FileInputStream(file);
            OutputStream outputStream = fsInputFile.getOutputStream();
            byteBuffer = new byte[READ_BUFFER_SIZE];

            int readLength = fis.read(byteBuffer, 0, READ_BUFFER_SIZE);
            while (readLength != -1) {
                if (readLength >= READ_BUFFER_SIZE) {
                    outputStream.write(byteBuffer);
                } else if (readLength < READ_BUFFER_SIZE) {
                    outputStream.write(byteBuffer, 0, readLength);
                }
                readLength = fis.read(byteBuffer);
            }
            mrTemp.setFileSize(String.valueOf(file.length() / 1024));
            fis.close();

            saveLinkFileCollection(mrTemp, cnTemp);

            fileManagerMongo.saveFileFromStream(mrTemp.getFileName(), fsInputFile);

            dmrTemp.setData(mrTemp);
            spBegin.commit();
            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setStatus(false);
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return dmrTemp;
    }

    private void saveLinkFileCollection(FileUpload_Mdl upload_Mdl, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmFileManagerMongo mongoCon = null;
        String sqlQuery;

        try {
            mongoCon = DmServer.getConnectionMongo(DmServer.getSsnFileDBName(this));
            if (DmShared.xNull(upload_Mdl.getDocId()).trim().equalsIgnoreCase("")) {
                throw new Exception("Unable to attach file. Invalid DocId.");
            }

            if (DmShared.xNull(upload_Mdl.getRuleCode()).trim().equalsIgnoreCase("")) {
                throw new Exception("Unable to attach file. Invalid Rule Code.");
            }

            if (DmShared.xNull(upload_Mdl.getFileTypeName()).trim().equalsIgnoreCase("")) {
                throw new Exception("Unable to attach file. Invalid File Type.");
            }

            sqlQuery = "SELECT fileLink,docId,sNo,ruleCode FROM LINKFILECOLLECTION WHERE (docId=:docId) and (sNo=:sNo) and (ruleCode=:ruleCode)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", upload_Mdl.getDocId());
            psTemp.setString("sNo", DmServer.vNull(upload_Mdl.getsNo()));
            psTemp.setString("ruleCode", upload_Mdl.getRuleCode());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                String mongoId = DmShared.xNull(rsTemp.getString("fileLink"));

                if (!mongoCon.deleteFile(upload_Mdl.getRuleCode(), mongoId)) {
                    throw new Exception("Unable to Delete File Link.");
                }
            }
            rsTemp.close();
            psTemp.close();
            sqlQuery = "DELETE FROM linkFileCollection WHERE (docId=:docId) and (sNo=:sNo) and (ruleCode=:ruleCode)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", upload_Mdl.getDocId());
            psTemp.setString("sNo", DmServer.vNull(upload_Mdl.getsNo()));
            psTemp.setString("ruleCode", upload_Mdl.getRuleCode());
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "Insert into linkFileCollection (fileLink, docId, sNo, ruleCode, fileType, fileName, fileSize, remarks, "
                    + " u_Name, u_EntDt, tempDocId, sessionId) "
                    + " values (:fileLink, :docId, :sNo, :ruleCode,  :fileType, :fileName, :fileSize, :remarks, "
                    + " :u_name, :u_EntDt, :tempDocId, :sessionId)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("fileLink", upload_Mdl.getFileLink());
            psTemp.setString("docid", DmShared.foriegnKey(upload_Mdl.getDocId()));
            psTemp.setString("rulecode", upload_Mdl.getRuleCode());
            psTemp.setString("sNo", DmServer.vNull(upload_Mdl.getsNo()));
            psTemp.setString("filesize", DmServer.vNull(upload_Mdl.getFileSize(), "0"));
            psTemp.setString("filename", upload_Mdl.getFileName());
            psTemp.setString("filetype", upload_Mdl.getFileTypeName());
            psTemp.setString("remarks", "");
            psTemp.setString("u_name", DmServer.getSsnUserName(this));
            psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
            psTemp.setString("tempDocId", DmShared.foriegnKey(""));
            psTemp.setString("sessionId", DmShared.foriegnKey(upload_Mdl.getSessionId()));

            psTemp.executeUpdate();
            psTemp.close();

        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
    }

    @Override
    public DmResult<DmAttachment_Mdl> downloadDocument(String siteCode, String sNo, String ruleCode) {
        DmResult<DmAttachment_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        String sqlQuery = null;
        DmAttachment_Mdl attachment = new DmAttachment_Mdl();

        dmrTemp.setData(attachment);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = " SELECT fileLink,docId,sNo,ruleCode,fileType,fileName,filesize,remarks,sessionId,u_Name,u_EntDt "
                    + "  FROM LINKFILECOLLECTION"
                    + "  WHERE (docId=:docId) and (sNo=:sNo) and (ruleCode=:ruleCode)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", siteCode);
            psTemp.setString("sNo", DmServer.vNull(sNo));
            psTemp.setString("ruleCode", ruleCode);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                attachment.setSessionId(rsTemp.getString("sessionId"));
                attachment.setDocId(rsTemp.getString("docId"));
                attachment.setsNo(DmServer.vNull(rsTemp.getString("sNo")));
                attachment.setFileLink(DmShared.xNull(rsTemp.getString("fileLink")));
                attachment.setRuleCode(DmShared.xNull(rsTemp.getString("ruleCode")));
                attachment.setFileTypeName(DmShared.xNull(rsTemp.getString("fileType")));
                attachment.setFileName(DmShared.xNull(rsTemp.getString("fileName")));
                attachment.setFileSize(DmShared.xNull(rsTemp.getString("fileSize")));
                attachment.setRemarks(DmShared.xNull(rsTemp.getString("remarks")));
                attachment.setUserName(DmShared.xNull(rsTemp.getString("u_Name")));
                attachment.setU_EntDt(rsTemp.getTimestamp("u_EntDt"));

            }
            rsTemp.close();
            psTemp.close();

            String resourceAlocationId = UUID.randomUUID().toString();
            attachment.setResourceAlocationId(resourceAlocationId);
            this.getLocalRequest().getSession().setAttribute(resourceAlocationId, attachment);

            dmrTemp.setData(attachment);
            spBegin.commit();
            dmrTemp.setStatus(true);
            cnTemp.close();
        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {

                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

    @Override
    public DmResult<KeyFieldExtended> exportJSON(String session, String siteCode) {
        DmResult<KeyFieldExtended> rstTemp = new DmResult<>();
        String sqlQuery, sessionId = "", fileName = "", directoryPath, fullFilePath = "", serverFileName = "", shortName = "";
        Connection cnTemp = null;
        ExportEnviro_Mdl mrTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        KeyFieldExtended field = null;
        File sFile;
        FileWriter writer;
        try {
            sessionId = session;
            if (!DmSession.getSessions().containsKey(sessionId)) {
                throw new DmException(0, "Invalid request.");
            }
            directoryPath = DmServer.Path.sessions + File.separatorChar + sessionId + File.separatorChar + "enviroSite";
            sFile = new File(directoryPath);
            if (!sFile.exists()) {
                sFile.mkdirs();
            }
            if (DmShared.iNull(siteCode).intValue() > 0) {
                mrTemp = new ExportEnviro_Mdl();
                cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
                sqlQuery = "select "
                        //                        + "       en.bloodBagIssueDepartmentCode ,en.createComponentDepartmentCode,"
                        + "       en.bloodBank_CIP_Addition  , en.isIncludeTaxInCost , en.pharmacyBarcodeActive "
                        + "       ,en.secondaryCurrencyCode,en.showMMUptoYear,en.stockAdjustmentVType , en.wj_MsgLength, "
                        + "	  en.site_Code, en.alphaCase_Medicine, en.alphCase_Patient, en.alphaCase_Address, en.caseCode, cm.name as caseName,    "
                        + "       en.refConsultantCode, ref.name as referredBy, en.consultantCode, con.name as consultant, en.showDDUptoMonth,    "
                        + "       en.documentFooter, en.documentFooterRegional, en.documentHeader, en.registrationFee, en.cancelDaysOPD, en.serviceDaysOPD, en.defaultCity,    "
                        + "       ct.cityName, en.ipdNoOfSticker, en.defaultNationality, nm.name as nationality, en.isReligionMandatory, en.isRoomClean,    "
                        + "       en.isIdTypeMandatory, en.isPatientInitialMandatory, en.isAddressMandatory, en.isCityMandatory,en.isReceiptGenerateWithFullDiscount, en.isCasteMandatory,    "
                        + "       en.isGuardianInitialMandatory, en.isMaritalStatusMandatory, en.isBloodGroupMandatory, en.isEmailMandatory,    "
                        + "       en.infantUnit, ium.unit, en.roomPostIsAuto, en.roomPostTimeFormat, en.roomPostCheckoutTime, en.tokenBlockNature,    "
                        + "       en.roomPostGraceMinuteOut, en.roomPostGraceMinuteIn, en.roomPostHalfDayUpto, en.roomPostPercentage, en.opdControlAc,    "
                        + "       opdCtrl.name as opdCtrlAc, en.diagnosticControlAc, labCtrl.name as labCtrlAc, en.pharmacyControlAc,    "
                        + "       phrCtrl.name as phCtrlAc, en.emergencyControlAc, emrCtrl.name as emrCtrlAc, en.dayCareControlAc,    "
                        + "       dayCtrl.name as dayCtrlAc, en.ipdControlAc, ipdCtrl.name as ipdCtrlAc, en.directPrescription, en.opdNoOfSticker,    "
                        + "       en.isIPDExistingPatient, en.diagnosticNoOfSticker, en.isDirectSampleLabCollection, en.panCardLimit, en.cashPaymentLimit,    "
                        + "       en.nightShiftFromTime, en.nightShiftToTime, en.isDiagnosticAutoVerify, en.isDiagnosticBalancePrintAllow,    "
                        + "       en.isDiagnosticPrintOnLetterhead, en.isIPDRoomMandatory,en.isServiceUnitEditable, en.diagnostic_Reg_Addition, en.isHourlyServiceRoundOff,    "
                        + "       en.pharmacyHospitalAc, phHospital.name as phHospitalAc, en.pharmacySelfAc, phSelf.name as phSelfAc,    "
                        + "       en.diagnosticHospitalAc, labHospital.name as labHosptialAc, en.diagnosticSelfAc, labSelf.name as labSelfAc,    "
                        + "       en.newBornDD, en.newBornMM, en.newBornYY, en.infantMM, en.infantYY, en.childMM, en.childYY, en.orgReceiptCardCode,    "
                        + "       orgRec.name as orgRec, en.hospitalHoldCardCode, hhRec.name as hospitalHold, en.isPatientImageMandatory,  "
                        + "       en.isAreaMandatory, en.isGuardianNameMandatory, en.disallowType, en.dietary_property1, en.dietary_property2,    "
                        + "       en.dietary_property3, en.dietary_property4, en.dietary_property5, en.dietary_property6, en.uhidDefaultCardType,uhidCT.name as uhidDefaultCardTypeName, "
                        + "       en.dietScheduleDays, "
                        //+ "  en.disallowserviceCode,    "
                        // + "       disSrv.name as disAllowSrv, "
                        + "         en.ac_RoundOffTDS, en.expiryAlertBeforeDays, en.medicineSaleOn, en.opdAllowPatientForSite, en.eodPostingType,    "
                        + "       en.ipdAllowPatientForSite, en.pharmacyAllowPatientForSite, en.diagnosticAllowPatientForSite,    "
                        + "       en.dayCareAllowPatientForSite, en.pharmacyAllowSaleBillForSite, en.ac_RoundOffGST,    "
                        + "       en.defaultOpdPharmacyDepartmentCode, en.defaultPharmacyServiceCode, phSrv.name as phService, labSrv.name as regAddition,    "
                        + "       en.pharmacyFinalDiscountOHCode, ohm.name as phDisOH, en.pharmacyFinalDiscountAcCode,phDisAc.name as phDisAc,     "
                        + "       en.defaultEstimatePackageCode, pm.name as estimatePkg, en.cancelDaysDiagnostic, en.serviceDaysDiagnostic,    "
                        + "       en.defaultOpdPharmacySiteCode, en.pharmacyAllowReturnWithoutSale,    "
                        + "       en.pharmacyReturnDays,en.opdHistoryViewDays,en.appointmentHistoryViewDays,en.labHistoryViewDays,"
                        + "       sgPhHos.name as sgPHHospAc, sgPharmFinalDisAc.name as pharmacyFinalDiscountAcName,  ohm.name as pharmacyFinalDiscountOHName,"
                        + "       psmPharmacyDefaultService.name as defaultPharmacyServiceName, sgPhSf.name as sgPhSelfAc, en.OPDQueueAlgo , en.OPDNoOfVisit, en.OPDValidity, en.OPDisRoomWiseRegistration ,"
                        + "       en.isOtpRequiredForRefund, en.isIcdxMandatory, "
                        + "       en.appointmentBookUptoDays, en.appointmentPaymentGatewayAllowed, "
                        + "       en.paymentGatewayURL, en.paymentOption, en.autoOPDRegAfterAppointment, en.runFromWebsite, en.isAutoInTransitByMedicineTransfer "
                        + "       from enviro en    "
                        + "       left join city ct on ct.cityCode = en.defaultCity    "
                        + "       left join caseMast cm on cm.code = en.caseCode    "
                        + "       left join subGroup con on con.subCode = en.consultantCode    "
                        + "       left join subGroup ref on ref.subCode = en.refConsultantCode    "
                        + "       left join nationalityMast nm on nm.code = en.defaultNationality    "
                        + "       left join unitMast ium on ium.code = en.infantUnit    "
                        + "       left join subGroup opdCtrl on opdCtrl.subCode = en.opdControlAc    "
                        + "       left join subGroup labCtrl on labCtrl.subCode = en.diagnosticControlAc    "
                        + "       left join subGroup phrCtrl on phrCtrl.subCode = en.pharmacyControlAc    "
                        + "       left join subGroup emrCtrl on emrCtrl.subCode = en.emergencyControlAc    "
                        + "       left join subGroup dayCtrl on dayCtrl.subCode = en.dayCareControlAc    "
                        + "       left join subGroup ipdCtrl on ipdCtrl.subCode = en.ipdControlAc    "
                        + "       left join subGroup phHospital on phHospital.subCode = en.pharmacyHospitalAc    "
                        + "       left join subGroup phSelf on phSelf.subCode = en.pharmacySelfAc    "
                        + "       left join subGroup labHospital on labHospital.subCode = en.diagnosticHospitalAc    "
                        + "       left join subGroup labSelf on labSelf.subCode = en.diagnosticSelfAc   "
                        //                        + "	  left join subGroup bbicd on bbicd.subcode = en.bloodBagIssueDepartmentCode  "
                        //                        + "	  left join subGroup ccd on ccd.subcode = en.createComponentDepartmentCode "
                        + "       left join cardTypeMast orgRec on orgRec.code = en.orgReceiptCardCode    "
                        + "       left join cardTypeMast hhRec on hhRec.code = en.hospitalHoldCardCode    "
                        //+ "       left join patientServiceMast disSrv on disSrv.code = en.disallowserviceCode    "
                        + "       left join patientServiceMast phSrv on phSrv.code = en.defaultPharmacyServiceCode    "
                        + "       left join overHeadMast ohm on ohm.code = en.pharmacyFinalDiscountOHCode   "
                        + "       left join subGroup phDisAc on phDisAc.subCode = en.pharmacyFinalDiscountAcCode    "
                        + "       left join packageMast pm on pm.code = en.defaultEstimatePackageCode    "
                        + "        LEFT JOIN cardTypeMast as uhidCT on en.uhidDefaultCardType=uhidCT.code "
                        + "       left join patientServiceMast labSrv on phSrv.code = en.diagnostic_Reg_Addition "
                        + "	  left join patientServiceMast bbcip on bbcip.code = en.bloodBank_CIP_Addition "
                        + "	  left join currency cur on cur.code = en.secondaryCurrencyCode "
                        + "       left join subgroup sgPhHos on en.pharmacyHospitalAc = sgPhHos.subcode "
                        + "	  left join subgroup sgPharmFinalDisAc on en.pharmacyFinalDiscountAcCode = sgPharmFinalDisAc.subcode "
                        + "	  left join patientServiceMast psmPharmacyDefaultService on psmPharmacyDefaultService.code = en.defaultPharmacyServiceCode "
                        + "       left join subgroup sgPhSf on en.pharmacySelfAc = sgPhSf.subcode "
                        + "       where en.site_Code = :siteCode";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("siteCode", siteCode);
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    mrTemp.setAddressAlpha(AlphabetCase.getViaShortName(DmShared.xNull(rsTemp.getString("alphaCase_Address"))));
                    mrTemp.setAddressMandatory(DmShared.iNull(rsTemp.getString("isAddressMandatory")).intValue() == 1);
                    mrTemp.setAreaMandatory(DmShared.iNull(rsTemp.getString("isAreaMandatory")).intValue() == 1);
                    mrTemp.setAutoRoomPost(DmShared.iNull(rsTemp.getString("roomPostIsAuto")).intValue() == 1);
                    mrTemp.setBloodGroupMandatory(DmShared.iNull(rsTemp.getString("isBloodGroupMandatory")).intValue() == 1);
                    mrTemp.setCancelDaysOPD(DmShared.xNull(rsTemp.getString("cancelDaysOPD")));
                    mrTemp.setCashPaymentLimit(DmServer.vNull(rsTemp.getString("cashPaymentLimit"), "0.00"));
                    mrTemp.setCasteMandatory(DmShared.iNull(rsTemp.getString("isCasteMandatory")).intValue() == 1);
                    mrTemp.setChildMonth(DmServer.vNull(rsTemp.getString("childMM"), "0"));
                    mrTemp.setChildYear(DmServer.vNull(rsTemp.getString("childYY"), "0"));
                    mrTemp.setCityMandatory(DmShared.iNull(rsTemp.getString("isCityMandatory")).intValue() == 1);
                    mrTemp.setIsReceiptGenerateWithFullDiscount(DmShared.iNull(rsTemp.getString("isReceiptGenerateWithFullDiscount")).intValue() == 1);
                    mrTemp.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                    mrTemp.setConsultantName(DmShared.xNull(rsTemp.getString("consultant")));
                    mrTemp.setCtrlAcDayCareText(DmShared.xNull(rsTemp.getString("dayCtrlAc")));
                    mrTemp.setCtrlAcEmergencyText(DmShared.xNull(rsTemp.getString("emrCtrlAc")));
                    mrTemp.setCtrlAcIPDText(DmShared.xNull(rsTemp.getString("ipdCtrlAc")));
                    mrTemp.setCtrlAcLABText(DmShared.xNull(rsTemp.getString("labCtrlAc")));
                    mrTemp.setEodPostingType(DmShared.xNull(rsTemp.getString("eodPostingType")));
                    mrTemp.setCtrlAcOPDText(DmShared.xNull(rsTemp.getString("opdCtrlAc")));
                    mrTemp.setCtrlAcPharmacyText(DmShared.xNull(rsTemp.getString("phCtrlAc")));
                    mrTemp.setDefaultRegFee(DmServer.vNull(rsTemp.getString("registrationFee"), "0.00"));
                    mrTemp.setDiagPrintOnLetterHead(DmShared.iNull(rsTemp.getString("isDiagnosticPrintOnLetterhead")).intValue() == 1);
                    mrTemp.setDiagnosticDays(DmShared.xNull(rsTemp.getString("serviceDaysDiagnostic")));

                    mrTemp.setAppointmentHistoryViewDays(DmShared.xNull(rsTemp.getString("appointmentHistoryViewDays")));
                    mrTemp.setLabHistoryViewDays(DmShared.xNull(rsTemp.getString("labHistoryViewDays")));
                    mrTemp.setHistoryViewDays(DmShared.xNull(rsTemp.getString("opdHistoryViewDays")));

                    mrTemp.setDiagnosticHospitalAcText(DmShared.xNull(rsTemp.getString("labHosptialAc")));
                    mrTemp.setDiagnosticRegAdditionText(DmShared.xNull(rsTemp.getString("regAddition")));
                    mrTemp.setDiagnosticSelfAcTag(DmShared.xNull(rsTemp.getString("labSelfAc")));
                    mrTemp.setDietScheduleDays(DmShared.xNull(rsTemp.getString("dietScheduleDays")));
                    mrTemp.setDietaryProperty1(DmShared.xNull(rsTemp.getString("dietary_property1")));
                    mrTemp.setDietaryProperty2(DmShared.xNull(rsTemp.getString("dietary_property2")));
                    mrTemp.setDietaryProperty3(DmShared.xNull(rsTemp.getString("dietary_property3")));
                    mrTemp.setDietaryProperty4(DmShared.xNull(rsTemp.getString("dietary_property4")));
                    mrTemp.setDietaryProperty5(DmShared.xNull(rsTemp.getString("dietary_property5")));
                    mrTemp.setDietaryProperty6(DmShared.xNull(rsTemp.getString("dietary_property6")));
                    mrTemp.setDirectLabCollection(DmShared.iNull(rsTemp.getString("isDiagnosticPrintOnLetterhead")).intValue() == 1);
                    mrTemp.setDirectPrescription(DmShared.iNull(rsTemp.getString("directPrescription")).intValue() == 1);
                    mrTemp.setDisallowTypeText(DmShared.iNull(rsTemp.getString("disallowType")).intValue() == 1 ? "Disallow (Reg. wise)" : "Disallow (Service wise)");
                    mrTemp.setDocumentFooter(DmShared.xNull(rsTemp.getString("documentFooter")));
                    mrTemp.setDocumentFooterRegional(DmShared.xNull(rsTemp.getString("documentFooterRegional")));
                    mrTemp.setDocumentHeader(DmShared.xNull(rsTemp.getString("documentHeader")));
                    mrTemp.setEmailMandatory(DmShared.iNull(rsTemp.getString("isEmailMandatory")).intValue() == 1);
                    mrTemp.setGuardianInitialMandatory(DmShared.iNull(rsTemp.getString("isGuardianInitialMandatory")).intValue() == 1);
                    mrTemp.setGuardianMandatory(DmShared.iNull(rsTemp.getString("isGuardianNameMandatory")).intValue() == 1);
                    mrTemp.setHospitalHoldCardTypeText(DmShared.xNull(rsTemp.getString("hospitalHold")));
                    mrTemp.setUhidDefaultCardName(DmShared.xNull(rsTemp.getString("uhidDefaultCardTypeName")));
                    mrTemp.setHourlyServiceRoundOffText(DmShared.xNull(rsTemp.getString("isHourlyServiceRoundOff")).equalsIgnoreCase("0") ? "Normal"
                            : (DmShared.xNull(rsTemp.getString("isHourlyServiceRoundOff")).equalsIgnoreCase("1") ? "Higher"
                            : (DmShared.xNull(rsTemp.getString("isHourlyServiceRoundOff")).equalsIgnoreCase("2") ? "Lower" : "")));
                    mrTemp.setIdTypeMandatory(DmShared.iNull(rsTemp.getString("isIdTypeMandatory")).intValue() == 1);
                    mrTemp.setIncludeDDInAge(DmShared.xNull(rsTemp.getString("showDDUptoMonth")));
                    mrTemp.setInfantMonth(DmShared.xNull(rsTemp.getString("infantMM")));
                    mrTemp.setInfantYear(DmShared.xNull(rsTemp.getString("infantYY")));
                    mrTemp.setIpdCheckoutTime(DmServer.vNull(rsTemp.getString("roomPostCheckoutTime"), "00.00"));
                    mrTemp.setIpdGraceMinuteIn(DmShared.xNull(rsTemp.getString("roomPostGraceMinuteIn")));
                    mrTemp.setIpdGraceMinuteOut(DmShared.xNull(rsTemp.getString("roomPostGraceMinuteOut")));
                    mrTemp.setIpdHalfDayPercentage(DmShared.xNull(rsTemp.getString("roomPostPercentage")));
                    mrTemp.setIpdHalfDayUpToMin(DmShared.iNull(rsTemp.getString("roomPostHalfDayUpto")).toString());
                    mrTemp.setIpdInfantUnitText(DmShared.xNull(rsTemp.getString("unit")));
                    mrTemp.setIpdTimeFormatRoomText(DmShared.xNull(mrTemp.getIpdTimeFormatRoomTag()).equalsIgnoreCase("0") ? "24 Hours"
                            : DmShared.xNull(mrTemp.getIpdTimeFormatRoomTag()).equalsIgnoreCase("1") ? "Checkout Time" : "");
                    mrTemp.setLabAutoVerify(DmShared.iNull(rsTemp.getString("isDiagnosticAutoVerify")).intValue() == 1);
                    mrTemp.setLabBalancePrint(DmShared.iNull(rsTemp.getString("isDiagnosticBalancePrintAllow")).intValue() == 1);
                    mrTemp.setLabCancelDays(DmShared.xNull(rsTemp.getString("cancelDaysDiagnostic")));
                    mrTemp.setMaritalMandatory(DmShared.iNull(rsTemp.getString("isMaritalStatusMandatory")).intValue() == 1);
                    mrTemp.setMedicineAlpha(AlphabetCase.getViaShortName(DmShared.xNull(rsTemp.getString("alphaCase_Medicine"))));
                    mrTemp.setNationalityText(DmShared.xNull(rsTemp.getString("nationality")));
                    mrTemp.setNewBornDay(DmShared.xNull(rsTemp.getString("newBornDD")));
                    mrTemp.setNewBornMonth(DmShared.xNull(rsTemp.getString("newBornMM")));
                    mrTemp.setNewBornYear(DmShared.xNull(rsTemp.getString("newBornYY")));
                    mrTemp.setNightshiftFrom(DmShared.xNull(rsTemp.getString("nightShiftFromTime")));
                    mrTemp.setNightshiftTo(DmShared.xNull(rsTemp.getString("nightShiftToTime")));
                    mrTemp.setOpdCaseName(DmShared.xNull(rsTemp.getString("caseName")));
                    mrTemp.setOrgReceiptCardTypeText(DmShared.xNull(rsTemp.getString("orgRec")));
                    mrTemp.setPackageName(DmShared.xNull(rsTemp.getString("estimatePkg")));
                    mrTemp.setPanCardLimit(DmShared.xNull(rsTemp.getString("panCardLimit")));
                    mrTemp.setPatientAlpha(AlphabetCase.getViaShortName(DmShared.xNull(rsTemp.getString("alphCase_Patient"))));
                    mrTemp.setPatientImageMandatory(DmShared.iNull(rsTemp.getString("isPatientImageMandatory")).intValue() == 1);
                    mrTemp.setIsOtpRequiredForRefund(DmShared.iNull(rsTemp.getString("isOtpRequiredForRefund")).intValue() == 1);
                    mrTemp.setIsIcdxMandatory(DmShared.iNull(rsTemp.getString("isIcdxMandatory")).intValue() == 1);
                    mrTemp.setPatientInitialMandatory(DmShared.iNull(rsTemp.getString("isPatientInitialMandatory")).intValue() == 1);
                    mrTemp.setReferredByName(DmShared.xNull(rsTemp.getString("referredBy")));
                    mrTemp.setReligionMandatory(DmShared.iNull(rsTemp.getString("isReligionMandatory")).intValue() == 1);
                    mrTemp.setRoomClean(DmShared.iNull(rsTemp.getString("isRoomClean")).intValue() == 1);
                    mrTemp.setRoomMandatory(DmShared.iNull(rsTemp.getString("isIPDRoomMandatory")).intValue() == 1);
                    mrTemp.setIsServiceUnitEditable(DmShared.iNull(rsTemp.getString("isServiceUnitEditable")).intValue() == 1);
                    mrTemp.setRoundOffGST(DmShared.xNull(rsTemp.getString("ac_RoundOffGST")));
                    mrTemp.setRoundOffTDS(DmShared.xNull(rsTemp.getString("ac_RoundOffTDS")));
                    mrTemp.setServiceDays(DmShared.xNull(rsTemp.getString("serviceDaysOPD")));
                    mrTemp.setStickerIPD(DmShared.xNull(rsTemp.getString("ipdNoOfSticker")));
                    mrTemp.setStickerLAB(DmShared.xNull(rsTemp.getString("diagnosticNoOfSticker")));
                    mrTemp.setStickerOPD(DmShared.xNull(rsTemp.getString("opdNoOfSticker")));

//                    mrTemp.setBloodBagIssueDepartmentCode(DmShared.xNull(rsTemp.getString("bloodBagIssueDepartmentCode")));
                    mrTemp.setBloodBank_CIP_Addition(DmShared.xNull(rsTemp.getString("bloodBank_CIP_Addition")));
//                    mrTemp.setCreateComponentDepartmentCode(DmShared.xNull(rsTemp.getString("createComponentDepartmentCode")));
                    mrTemp.setIncludeTaxInCost(DmShared.xNull(rsTemp.getString("isIncludeTaxInCost")).equalsIgnoreCase("0") ? "N"
                            : (DmShared.xNull(rsTemp.getString("isIncludeTaxInCost")).equalsIgnoreCase("1") ? "Y" : ""));

                    mrTemp.setPharmacyBarcodeActive(DmShared.xNull(rsTemp.getString("pharmacyBarcodeActive")).equalsIgnoreCase("0") ? "N"
                            : (DmShared.xNull(rsTemp.getString("pharmacyBarcodeActive")).equalsIgnoreCase("1") ? "Y" : ""));

                    mrTemp.setAutoInTransitByMedicineTransfer(DmShared.iNull(rsTemp.getString("isAutoInTransitByMedicineTransfer")).intValue() == 1);
                    mrTemp.setSecondaryCurrencyCode(DmShared.xNull(rsTemp.getString("secondaryCurrencyCode")));
                    mrTemp.setIncludeMMInAge(DmShared.xNull(rsTemp.getString("showMMUptoYear")));
                    mrTemp.setStockAdjustmentVTypeCode(DmShared.xNull(rsTemp.getString("stockAdjustmentVType")));
                    mrTemp.setStockAdjustmentVTypeName(getVoucherTypeShortName(DmShared.xNull(rsTemp.getString("stockAdjustmentVType")), cnTemp));
                    mrTemp.setWj_MsgLength(DmShared.xNull(rsTemp.getString("wj_MsgLength")));
                    mrTemp.setTokenBlockNature(TokenBlockNature.getKeyOf(DmShared.iNull(rsTemp.getString("tokenBlockNature")).intValue()));

                    //----------------------------------------------------------------------------------------------------
                    mrTemp.setPharmacyHospitalAcTag(DmShared.xNull(rsTemp.getString("pharmacyHospitalAc")));
                    mrTemp.setPharmacyHospitalAcText(DmShared.xNull(rsTemp.getString("sgPHHospAc")));
                    mrTemp.setPharmacySelfAcTag(DmShared.xNull(rsTemp.getString("pharmacySelfAc")));
                    mrTemp.setPharmacySelfAcText(DmShared.xNull(rsTemp.getString("sgPhSelfAc")));
                    mrTemp.setPharmacyFinalDiscountAcCode(DmShared.xNull(rsTemp.getString("pharmacyFinalDiscountAcCode")));
                    mrTemp.setPharmacyFinalDiscountAcName(DmShared.xNull(rsTemp.getString("pharmacyFinalDiscountAcName")));
                    mrTemp.setPharmacyFinalDiscountOHCode(DmShared.xNull(rsTemp.getString("pharmacyFinalDiscountOHCode")));
                    mrTemp.setPharmacyFinalDiscountOHName(DmShared.xNull(rsTemp.getString("pharmacyFinalDiscountOHName")));
                    mrTemp.setPharmacyDefaultServiceTag(rsTemp.getString("defaultPharmacyServiceCode"));
                    mrTemp.setPharmacyDefaultServiceText(rsTemp.getString("defaultPharmacyServiceName"));
                    mrTemp.setAlgoNature(QueueAlgoNature.getKeyOf((DmShared.iNull(rsTemp.getString("OPDQueueAlgo")).intValue())));
                    mrTemp.setOpdValidity(DmShared.xNull(rsTemp.getString("OPDValidity")));
                    mrTemp.setOpdNoOfVisit(DmShared.xNull(rsTemp.getString("OPDNoOfVisit")));
                    mrTemp.setOpdIsRoomWiseRegistration(DmShared.xNull(rsTemp.getString("OPDisRoomWiseRegistration")));
                    mrTemp.setAppointmentBookUptoDays(DmShared.xNull(rsTemp.getString("appointmentBookUptoDays")));
                    mrTemp.setAppointmentPaymentGatewayAllowed(DmShared.iNull(rsTemp.getString("appointmentPaymentGatewayAllowed")).intValue() == 1);
                    mrTemp.setPaymentGatewayURL(DmShared.xNull(rsTemp.getString("paymentGatewayURL")));
                    mrTemp.setPaymentOptionTag(DmShared.xNull(rsTemp.getString("paymentOption")));
                    mrTemp.setAutoOPDRegAfterAppointment(DmShared.iNull(rsTemp.getString("autoOPDRegAfterAppointment")).intValue() == 1);
                    mrTemp.setRunFromWebsite(DmShared.iNull(rsTemp.getString("runFromWebsite")).intValue() == 1);

                }
                rsTemp.close();
                psTemp.close();

                mrTemp.getLstGeneralService().addAll(EnviroService_Server.getServiceCategory(siteCode, "0", cnTemp));
                mrTemp.getLstDiagnostic().addAll(EnviroService_Server.getServiceCategory(siteCode, "1", cnTemp));
                mrTemp.getLstDepartmentLink().addAll(getLinkDepartment(siteCode, cnTemp));
                mrTemp.getLstDischargeGUI().addAll(getDischargeStatus(siteCode, cnTemp));

                fileName = "enviroSite";
                if (DmShared.xNull(fileName).equalsIgnoreCase("")) {
                    throw new DmException(0, "File Name not given.");
                }
                serverFileName = fileName + ".json";
                fullFilePath = directoryPath + File.separatorChar + serverFileName;
                Gson gson = new Gson();
                String json = gson.toJson(mrTemp);
                writer = new FileWriter(fullFilePath);
                writer.write(json);
                writer.close();
            }
            ZipUtils zipUtils = new ZipUtils(directoryPath, directoryPath + File.separatorChar + "enviroSite.zip");
            zipUtils.zipIt();
            field = new KeyFieldExtended();
            field.setSharedKeyFieldCode(directoryPath);
            field.setSharedKeyFieldName("enviroSite.zip");
            rstTemp.setStatus(true);
            rstTemp.setData(field);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;

    }

    private String getVoucherTypeShortName(String vTypeList, Connection cnTemp) {
        String rntValue = "", sqlQuery;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        try {
            if (!vTypeList.equalsIgnoreCase("")) {
                sqlQuery = " select short_Name from voucher_Type where v_Type in (" + DmServer.getParamList(":vType", vTypeList) + ")";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setStringList("vType", vTypeList);
                rsTemp = psTemp.executeQuery();
                while (rsTemp.next()) {
                    if (rntValue.equalsIgnoreCase("")) {
                        rntValue = DmShared.xNull(rsTemp.getString("short_Name"));
                    } else {
                        rntValue += "," + DmShared.xNull(rsTemp.getString("short_Name"));
                    }
                }
                rsTemp.close();
                psTemp.close();
            }
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (rsTemp != null) {
                    rsTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rntValue;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHQueueAlgoNature(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            for (QueueAlgoNature algoNature : QueueAlgoNature.values()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.iNull(algoNature.getId()).toString());
                listNode.setSharedKeyFieldName(algoNature.getValue());
                lstTemp.add(listNode);
            }
            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHConsulatationRegistration(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("Consultant Wise");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Consultation Room Wise");
            lstTemp.add(listNode);

            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDonorCampCode(String siteCode, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("select code , name from bloodDonationCampMast where site_Code = :siteCode");
            sqlWithParam.setString("siteCode", DmShared.xNull(siteCode));

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }

                if (psTemp != null) {
                    psTemp.close();
                }

                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;

    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHPaymentOption(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Paytm");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("2");
            listNode.setSharedKeyFieldName("PhonePe");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("3");
            listNode.setSharedKeyFieldName("Google Pay");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("4");
            listNode.setSharedKeyFieldName("UPI");
            lstTemp.add(listNode);

            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    private String getPaymentOption(int optionCode) {
        String optionName = "";
        switch (optionCode) {
            case 1:
                optionName = "Paytm";
                break;
            case 2:
                optionName = "PhonePe";
                break;
            case 3:
                optionName = "Google Pay";
                break;
            case 4:
                optionName = "UPI";
                break;

            default:
                optionName = "";
        }

        return optionName;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHEodPosting(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Detail");
            listNode.setSharedKeyFieldName("Detail");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Summary");
            listNode.setSharedKeyFieldName("Summary");
            lstTemp.add(listNode);

            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHPurchaseSalesAcPostingBasedOn(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            for (STEnum.PurchaseSalesAcPostingBasedOn nature : STEnum.PurchaseSalesAcPostingBasedOn.values()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.iNull(nature.getId()).toString());
                listNode.setSharedKeyFieldName(nature.getValue());
                lstTemp.add(listNode);
            }
            lstTemp = DmShared.filterKeyFieldList(helper, lstTemp);
            rstTemp.setData(lstTemp);

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getHYesNo(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Yes");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("No");
            lstTemp.add(listNode);

            lstTemp = DmShared.filterKeyFieldList(helper, lstTemp);
            rstTemp.setData(lstTemp);

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHIndentRequired(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Yes");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("2");
            listNode.setSharedKeyFieldName("No");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("Depend On Master");
            lstTemp.add(listNode);

            lstTemp = DmShared.filterKeyFieldList(helper, lstTemp);
            rstTemp.setData(lstTemp);

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHAutoBedConfirmation(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Yes");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("No");
            lstTemp.add(listNode);

            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepreciationPostOn(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Purchase Date");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("Installation Date");
            lstTemp.add(listNode);

            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHVoucherPrefix(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            sqlWithParam.setSQL(" select v_Prefix as code ,cYear as name from company");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("code")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }

                if (psTemp != null) {
                    psTemp.close();
                }

                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }
}
