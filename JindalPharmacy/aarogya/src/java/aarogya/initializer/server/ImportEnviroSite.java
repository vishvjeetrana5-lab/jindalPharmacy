package aarogya.initializer.server;

import aarogya.initializer.shared.GrdInitializer_Mdl;
import aarogya.master.environmentSettingSite.shared.*;
import com.google.gson.Gson;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSS;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.self.server.DmHandelFile;
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmImportData.shared.DmImportStatus;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;

public class ImportEnviroSite implements Initializer_Interface {

    private DmRSS rssTemp;
    private DmHandelFile refreshDataStructure;

    @Override
    public DmImportStatus start(DmRSS rssTemp, GrdInitializer_Mdl model) {
        File sessionDir = null;
        BufferedReader br = null;
        String fileName;
        int status = 0, rtnStatus = 0;
        DmImportStatus importStatus = null;
        try {
            this.rssTemp = rssTemp;
            this.refreshDataStructure = new DmHandelFile(model.getLogFilePath(), true);
            Gson gson = new Gson();
            sessionDir = new File(model.getDataPath());
            if (!sessionDir.exists()) {
                sessionDir.mkdir();
            }

            if (sessionDir.list() != null) {
                for (int i = 0; i < sessionDir.list().length; i++) {
                    fileName = sessionDir.list()[i];
                    String tempReadPath = model.getDataPath() + File.separatorChar + fileName;
                    br = new BufferedReader(new FileReader(tempReadPath));
                    status = save(gson.fromJson(br, ExportEnviro_Mdl.class));
                    if (status > rtnStatus) {
                        rtnStatus = status;
                    }
                    br.close();
                    br = null;
                }
            }
        } catch (Exception e) {
            try {
                if (status > rtnStatus) {
                    rtnStatus = status;
                }
                throw new Exception(e);
            } catch (Exception ex) {
            }
        } finally {
            try {
                importStatus = DmImportStatus.getImportStatus(rtnStatus);
                if (refreshDataStructure != null) {
                    refreshDataStructure.close();
                }
                if (br != null) {
                    br.close();
                    br = null;
                }
                if (sessionDir != null) {
                    sessionDir = null;
                }
            } catch (Exception e) {

            }
        }
        return importStatus;
    }

    private int save(ExportEnviro_Mdl mrTemp) {
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        String sqlQuery, vType, lstVType = "";

        int status = 0, rtnStatus = 0;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rssTemp));
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "select count(*) as cnt from enviro where site_Code = :siteCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(rssTemp));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("cnt")).longValue() == 0) {
                    sqlQuery = " INSERT INTO enviro(site_Code) VALUES(:site_Code) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("site_Code", DmServer.getSsnSiteCode(rssTemp));
                    psTemp.executeUpdate();
                }
            }

            //===========================================================================================================================
            mrTemp.setCityCode("");
            mrTemp.setCityCode(DmServer.getCodeViaManualCode(mrTemp.getCityName(), "cityName", "city", "cityCode", cnTemp));
            if (DmShared.iNull(mrTemp.getCityCode()).longValue() == 0) {
                status = DmImportStatus.WARNING.getId();
                refreshDataStructure.writeLine(DmShared.xNull(mrTemp.getCityName()) + " City does not exist");
            }

            mrTemp.setNationalityTag("");
            mrTemp.setNationalityTag(DmServer.getCodeViaManualCode(mrTemp.getNationalityText(), "name", "nationalityMast", "code", cnTemp));
            if (DmShared.iNull(mrTemp.getNationalityTag()).longValue() == 0) {
                status = DmImportStatus.WARNING.getId();
                refreshDataStructure.writeLine(DmShared.xNull(mrTemp.getNationalityText()) + " Nationality does not exist");
            }
            mrTemp.setOrgReceiptCardTypeTag("");
            mrTemp.setOrgReceiptCardTypeTag(DmServer.getCodeViaManualCode(mrTemp.getOrgReceiptCardTypeText(), "name", "cardTypeMast", "code", cnTemp));
            if (DmShared.iNull(mrTemp.getOrgReceiptCardTypeTag()).longValue() == 0) {
                status = DmImportStatus.WARNING.getId();
                refreshDataStructure.writeLine(DmShared.xNull(mrTemp.getOrgReceiptCardTypeText()) + " Card Type does not exist");
            }

            mrTemp.setHospitalHoldCardTypeTag("");
            mrTemp.setHospitalHoldCardTypeTag(DmServer.getCodeViaManualCode(mrTemp.getHospitalHoldCardTypeText(), "name", "cardTypeMast", "code", cnTemp));
            if (DmShared.iNull(mrTemp.getHospitalHoldCardTypeTag()).longValue() == 0) {
                status = DmImportStatus.WARNING.getId();
                refreshDataStructure.writeLine(DmShared.xNull(mrTemp.getHospitalHoldCardTypeText()) + " Card Type does not exist");
            }
            mrTemp.setUhidDefaultCardTypeCode("");
            mrTemp.setUhidDefaultCardTypeCode(DmServer.getCodeViaManualCode(mrTemp.getUhidDefaultCardName(), "name", "cardTypeMast", "code", cnTemp));
            if (DmShared.iNull(mrTemp.getUhidDefaultCardTypeCode()).longValue() == 0) {
                status = DmImportStatus.WARNING.getId();
                refreshDataStructure.writeLine(DmShared.xNull(mrTemp.getUhidDefaultCardName()) + " Card Type does not exist");
            }
            mrTemp.setCtrlAcOPDTag("");
            mrTemp.setCtrlAcOPDTag(DmServer.getCodeViaManualCode(mrTemp.getCtrlAcOPDText(), "name", "subgroup", "subcode", cnTemp));
            if (DmShared.iNull(mrTemp.getCtrlAcOPDTag()).longValue() == 0) {
                status = DmImportStatus.WARNING.getId();
                refreshDataStructure.writeLine(DmShared.xNull(mrTemp.getCtrlAcOPDText()) + " A/c does not exist");
            }

            mrTemp.setCtrlAcDayCareTag("");
            mrTemp.setCtrlAcDayCareTag(DmServer.getCodeViaManualCode(mrTemp.getCtrlAcDayCareText(), "name", "subgroup", "subcode", cnTemp));
            if (DmShared.iNull(mrTemp.getCtrlAcDayCareTag()).longValue() == 0) {
                status = DmImportStatus.WARNING.getId();
                refreshDataStructure.writeLine(DmShared.xNull(mrTemp.getCtrlAcDayCareText()) + " A/c does not exist");
            }
            mrTemp.setCtrlAcEmergencyTag("");
            mrTemp.setCtrlAcEmergencyTag(DmServer.getCodeViaManualCode(mrTemp.getCtrlAcEmergencyText(), "name", "subgroup", "subcode", cnTemp));
            if (DmShared.iNull(mrTemp.getCtrlAcEmergencyTag()).longValue() == 0) {
                status = DmImportStatus.WARNING.getId();
                refreshDataStructure.writeLine(DmShared.xNull(mrTemp.getCtrlAcEmergencyText()) + " A/c does not exist");
            }
            mrTemp.setCtrlAcIPDTag("");
            mrTemp.setCtrlAcIPDTag(DmServer.getCodeViaManualCode(mrTemp.getCtrlAcIPDText(), "name", "subgroup", "subcode", cnTemp));
            if (DmShared.iNull(mrTemp.getCtrlAcIPDTag()).longValue() == 0) {
                status = DmImportStatus.WARNING.getId();
                refreshDataStructure.writeLine(DmShared.xNull(mrTemp.getCtrlAcIPDText()) + " A/c does not exist");
            }
            mrTemp.setCtrlAcLABTag("");
            mrTemp.setCtrlAcLABTag(DmServer.getCodeViaManualCode(mrTemp.getCtrlAcLABText(), "name", "subgroup", "subcode", cnTemp));
            if (DmShared.iNull(mrTemp.getCtrlAcLABTag()).longValue() == 0) {
                status = DmImportStatus.WARNING.getId();
                refreshDataStructure.writeLine(DmShared.xNull(mrTemp.getCtrlAcLABText()) + " A/c does not exist");
            }
            mrTemp.setCtrlAcPharmacyTag("");
            mrTemp.setCtrlAcPharmacyTag(DmServer.getCodeViaManualCode(mrTemp.getCtrlAcPharmacyText(), "name", "subgroup", "subcode", cnTemp));
            if (DmShared.iNull(mrTemp.getCtrlAcPharmacyTag()).longValue() == 0) {
                status = DmImportStatus.WARNING.getId();
                refreshDataStructure.writeLine(DmShared.xNull(mrTemp.getCtrlAcPharmacyText()) + " A/c does not exist");
            }
            mrTemp.setOpdCaseCode("");
            mrTemp.setOpdCaseCode(DmServer.getCodeViaManualCode(mrTemp.getOpdCaseName(), "name", "casemast", "code", cnTemp));
            if (DmShared.iNull(mrTemp.getOpdCaseCode()).longValue() == 0) {
                status = DmImportStatus.WARNING.getId();
                refreshDataStructure.writeLine(DmShared.xNull(mrTemp.getOpdCaseName()) + " Case does not exist");
            }
            mrTemp.setDefaultPharmacySiteTag("");
            mrTemp.setDefaultPharmacySiteTag(DmServer.getCodeViaManualCode(mrTemp.getDefaultPharmacySiteText(), "name", "sitemast", "code", cnTemp));
            if (DmShared.iNull(mrTemp.getDefaultPharmacySiteTag()).longValue() == 0) {
                status = DmImportStatus.WARNING.getId();
                refreshDataStructure.writeLine(DmShared.xNull(mrTemp.getDefaultPharmacySiteText()) + " Site does not exist");
            }
            mrTemp.setIpdInfantUnitTag("");
            mrTemp.setIpdInfantUnitTag(DmServer.getCodeViaManualCode(mrTemp.getIpdInfantUnitText(), "unit", "unitmast", "code", cnTemp));
            if (DmShared.iNull(mrTemp.getIpdInfantUnitTag()).longValue() == 0) {
                status = DmImportStatus.WARNING.getId();
                refreshDataStructure.writeLine(DmShared.xNull(mrTemp.getIpdInfantUnitText()) + " Unit does not exist");
            }

            mrTemp.setDiagnosticRegAdditionTag("");
            mrTemp.setDiagnosticRegAdditionTag(DmServer.getCodeViaManualCode(mrTemp.getDiagnosticRegAdditionText(), "name", "patientServiceMast", "code", cnTemp));
            if (DmShared.iNull(mrTemp.getDiagnosticRegAdditionTag()).longValue() == 0) {
                status = DmImportStatus.WARNING.getId();
                refreshDataStructure.writeLine(DmShared.xNull(mrTemp.getDiagnosticRegAdditionText()) + " Service does not exist");
            }
            mrTemp.setDiagnosticHospitalAcTag("");
            mrTemp.setDiagnosticHospitalAcTag(DmServer.getCodeViaManualCode(mrTemp.getDiagnosticHospitalAcText(), "name", "subgroup", "subcode", cnTemp));
            if (DmShared.iNull(mrTemp.getDiagnosticHospitalAcTag()).longValue() == 0) {
                status = DmImportStatus.WARNING.getId();
                refreshDataStructure.writeLine(DmShared.xNull(mrTemp.getDiagnosticHospitalAcText()) + " A/c does not exist");
            }
            mrTemp.setDiagnosticSelfAcTag("");
            mrTemp.setDiagnosticSelfAcTag(DmServer.getCodeViaManualCode(mrTemp.getDiagnosticSelfAcText(), "name", "subgroup", "subcode", cnTemp));
            if (DmShared.iNull(mrTemp.getDiagnosticSelfAcTag()).longValue() == 0) {
                status = DmImportStatus.WARNING.getId();
                refreshDataStructure.writeLine(DmShared.xNull(mrTemp.getDiagnosticSelfAcText()) + " A/c does not exist");
            }

            if (mrTemp.getLstDischargeGUI() != null) {
                for (GrdStatusGUITemp_Mdl grdStatusGUITemp : mrTemp.getLstDischargeGUI()) {
                    grdStatusGUITemp.setSharedKeyFieldCode("");

                    if (!DmShared.xNull(grdStatusGUITemp.getSharedKeyFieldName()).trim().equals("")) {
                        grdStatusGUITemp.setSharedKeyFieldCode(DmServer.getCodeViaManualCode(grdStatusGUITemp.getSharedKeyFieldName(), "name", "guiTemplateMast", "code", cnTemp));
                        if (DmShared.iNull(grdStatusGUITemp.getSharedKeyFieldCode()).longValue() == 0) {
                            status = DmImportStatus.WARNING.getId();
                            refreshDataStructure.writeLine(DmShared.xNull(grdStatusGUITemp.getSharedKeyFieldName()) + "  GUI does not exist.");
                        }
                    }

                }
            }

            if (mrTemp.getLstDepartmentLink() != null) {
                for (GrdLinkDepartment_Mdl grdLinkDepartment : mrTemp.getLstDepartmentLink()) {
                    grdLinkDepartment.setSharedKeyFieldCode("");

                    if (!DmShared.xNull(grdLinkDepartment.getSharedKeyFieldName()).trim().equals("")) {

                        sqlQuery = " SELECT dm.subcode as code    "
                                + "       FROM departmentMast dm     "
                                + "       LEFT join subGroup as sg ON dm.subCode = sg.subCode     "
                                + "       WHERE  sg.isActive='1' and sg.name=:departmentName  ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("departmentName", DmShared.xNull(grdLinkDepartment.getSharedKeyFieldName()));
                        rsTemp = psTemp.executeQuery();
                        if (rsTemp.next()) {
                            grdLinkDepartment.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("code")).toString());
                        }

                        if (DmShared.iNull(grdLinkDepartment.getSharedKeyFieldCode()).longValue() == 0) {
                            status = DmImportStatus.WARNING.getId();
                            refreshDataStructure.writeLine(DmShared.xNull(grdLinkDepartment.getSharedKeyFieldName()) + "Department does not exist.");
                        }
                    }

                    if (!DmShared.xNull(grdLinkDepartment.getLinkSiteName()).trim().equals("")) {
                        grdLinkDepartment.setLinkSiteCode(DmServer.getCodeViaManualCode(grdLinkDepartment.getLinkSiteName(), "name", "site", "code", cnTemp));
                        if (DmShared.iNull(grdLinkDepartment.getLinkSiteCode()).longValue() == 0) {
                            status = DmImportStatus.WARNING.getId();
                            refreshDataStructure.writeLine(DmShared.xNull(grdLinkDepartment.getLinkSiteName()) + "Site does not exist.");
                        }
                    }

                }
                psTemp.close();
                rsTemp.close();
            }

            mrTemp.setPharmacyDefaultServiceTag("");
            mrTemp.setPharmacyDefaultServiceTag(DmServer.getCodeViaManualCode(mrTemp.getPharmacyDefaultServiceText(), "name", "patientServiceMast", "code", cnTemp));
            if (DmShared.iNull(mrTemp.getPharmacyDefaultServiceTag()).longValue() == 0) {
                status = DmImportStatus.WARNING.getId();
                refreshDataStructure.writeLine(DmShared.xNull(mrTemp.getPharmacyDefaultServiceText()) + " Service does not exist");
            }
            mrTemp.setPharmacyFinalDiscountAcCode("");
            mrTemp.setPharmacyFinalDiscountAcCode(DmServer.getCodeViaManualCode(mrTemp.getPharmacyFinalDiscountAcName(), "name", "subGroup", "subcode", cnTemp));
            if (DmShared.iNull(mrTemp.getPharmacyFinalDiscountAcCode()).longValue() == 0) {
                status = DmImportStatus.WARNING.getId();
                refreshDataStructure.writeLine(DmShared.xNull(mrTemp.getPharmacyFinalDiscountAcName()) + " A/c does not exist");
            }

            mrTemp.setPackageCode("");
            mrTemp.setPackageCode(DmServer.getCodeViaManualCode(mrTemp.getPackageName(), "name", "packageMast", "code", cnTemp));
            if (DmShared.iNull(mrTemp.getPackageCode()).longValue() == 0) {
                status = DmImportStatus.WARNING.getId();
                refreshDataStructure.writeLine(DmShared.xNull(mrTemp.getPackageName()) + " Package does not exist");
            }

            mrTemp.setPharmacyHospitalAcTag("");
            mrTemp.setPharmacyHospitalAcTag(DmServer.getCodeViaManualCode(mrTemp.getPharmacyHospitalAcText(), "name", "subGroup", "subcode", cnTemp));
            if (DmShared.iNull(mrTemp.getPharmacyHospitalAcTag()).longValue() == 0) {
                status = DmImportStatus.WARNING.getId();
                refreshDataStructure.writeLine(DmShared.xNull(mrTemp.getPharmacyHospitalAcText()) + " A/c does not exist");
            }
            mrTemp.setPharmacyFinalDiscountOHCode("");
            mrTemp.setPharmacyFinalDiscountOHCode(DmServer.getCodeViaManualCode(mrTemp.getPharmacyFinalDiscountOHName(), "name", "overHeadMast", "code", cnTemp));
            if (DmShared.iNull(mrTemp.getPharmacyFinalDiscountOHCode()).longValue() == 0) {
                status = DmImportStatus.WARNING.getId();
                refreshDataStructure.writeLine(DmShared.xNull(mrTemp.getPharmacyFinalDiscountOHName()) + " OverHead does not exist");
            }
            mrTemp.setPharmacySelfAcTag("");
            mrTemp.setPharmacySelfAcTag(DmServer.getCodeViaManualCode(mrTemp.getPharmacySelfAcText(), "name", "subGroup", "subcode", cnTemp));
            if (DmShared.iNull(mrTemp.getPharmacySelfAcTag()).longValue() == 0) {
                status = DmImportStatus.WARNING.getId();
                refreshDataStructure.writeLine(DmShared.xNull(mrTemp.getPharmacySelfAcText()) + "A/c does not exist");
            }

            if (!DmShared.xNull(mrTemp.getStockAdjustmentVTypeName()).equalsIgnoreCase("")) {
                for (String ShortName : DmShared.xNull(mrTemp.getStockAdjustmentVTypeName()).split(",")) {
                    vType = getVoucherType(ShortName, cnTemp);

                    if (DmShared.iNull(vType).longValue() != 0) {

                        lstVType += ", ";
                        lstVType += DmShared.xNull(vType).trim();
                    } else {
                        status = DmImportStatus.WARNING.getId();
                        refreshDataStructure.writeLine(DmShared.xNull(ShortName) + " Voucher type does not exist");
                    }
                }
                mrTemp.setStockAdjustmentVTypeCode(lstVType);
                if (DmShared.xNull(mrTemp.getStockAdjustmentVTypeCode()) == null) {
                    status = DmImportStatus.WARNING.getId();
                    refreshDataStructure.writeLine(" Voucher type does not exist");
                }

            }
            psTemp.close();
            rsTemp.close();

            mrTemp.setConsultantCode("");
            sqlQuery = " SELECT cm.subcode as code "
                    + "       FROM consultantmast cm  "
                    + "       LEFT join subGroup as sg ON cm.subCode = sg.subCode  "
                    + "       WHERE  sg.isActive='1'   "
                    + "	and sg.name=:consultantName ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("consultantName", DmShared.xNull(mrTemp.getConsultantName()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setConsultantCode(DmShared.iNull(rsTemp.getString("code")).toString());
            }
            if (DmShared.iNull(mrTemp.getConsultantCode()).longValue() == 0) {
                status = DmImportStatus.WARNING.getId();
                refreshDataStructure.writeLine(DmShared.xNull(mrTemp.getConsultantName()) + "Consultant does not exist");
            }
            psTemp.close();
            rsTemp.close();

            sqlQuery = "update enviro  SET "
                    + " alphCase_Patient = :alphCase_Patient,"
                    + " alphaCase_Address = :alphaCase_Address,"
                    + " diagnostic_Reg_Addition = :diagnostic_Reg_Addition,"
                    + " caseCode = :caseCode,"
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
                    + " cashpaymentLimit= :cashpaymentLimit,"
                    + " isIPDExistingPatient= :isIPDExistingPatient, "
                    + " isPatientInitialMandatory = :isPatientInitialMandatory, "
                    + " isAddressMandatory= :isAddressMandatory, "
                    + " isCityMandatory= :isCityMandatory, "
                    + " isGuardianInitialMandatory= :isGuardianInitialMandatory, "
                    + " isCasteMandatory= :isCasteMandatory, "
                    + " isMaritalStatusMandatory= :isMaritalStatusMandatory, "
                    + " isBloodGroupMandatory= :isBloodGroupMandatory, "
                    + " isEmailMandatory= :isEmailMandatory, "
                    + " dietScheduleDays= :dietScheduleDays, "
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
                    + " isServiceUnitEditable= :isServiceUnitEditable ,"
                    + " orgReceiptCardCode=:orgReceiptCardCode,"
                    + " hospitalHoldCardCode=:hospitalHoldCardCode,"
                    + " uhidDefaultCardType=:uhidDefaultCardType,"
                    + " isPatientImageMandatory=:isPatientImageMandatory,"
                    + " isAreaMandatory=:isAreaMandatory, "
                    + " isGuardianNameMandatory=:isGuardianNameMandatory,"
                    + " isDiagnosticPrintOnLetterhead=:isDiagnosticPrintOnLetterhead,"
                    + " tokenBlockNature=:tokenBlockNature,"
                    + " isHourlyServiceRoundOff=:isHourlyServiceRoundOff ,"
                    + " disallowType= :disallowType,"
                    + " eodPostingType= :eodPostingType,"
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
                    + " donorDefaultCampCode = :donorDefaultCampCode, "
                    + " isOtpRequiredForRefund = :isOtpRequiredForRefund, "
                    + " isIcdxMandatory = :isIcdxMandatory, "
                    + " appointmentBookUptoDays = :appointmentBookUptoDays, "
                    + " appointmentPaymentGatewayAllowed = :appointmentPaymentGatewayAllowed, "
                    + " paymentGatewayURL = :paymentGatewayURL, "
                    + " paymentOption = :paymentOption, "
                    + " autoOPDRegAfterAppointment = :autoOPDRegAfterAppointment, "
                    + " runFromWebsite = :runFromWebsite, "
                    + " isAutoInTransitByMedicineTransfer = :isAutoInTransitByMedicineTransfer "
                    + " where site_Code= :site_Code ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmShared.foriegnKey(DmServer.getSsnSiteCode(rssTemp)));
            psTemp.setString("alphCase_Patient", DmShared.xNull(mrTemp.getPatientAlpha().getShortName()));
            psTemp.setString("alphaCase_Address", DmShared.xNull(mrTemp.getAddressAlpha().getShortName()));
            psTemp.setString("caseCode", DmShared.foriegnKey(mrTemp.getOpdCaseCode()));
            psTemp.setString("consultantCode", DmShared.foriegnKey(mrTemp.getConsultantCode()));
            psTemp.setString("registrationFee", DmServer.vNull(mrTemp.getDefaultRegFee(), "0.00"));
            psTemp.setString("documentFooter", DmShared.xNull((mrTemp.getDocumentFooter().trim())));
            psTemp.setString("documentFooterRegional", DmShared.xNull((mrTemp.getDocumentFooterRegional().trim())));
            psTemp.setString("documentHeader", DmShared.xNull((mrTemp.getDocumentHeader().trim())));
            psTemp.setString("showDDUptoMonth", DmShared.iNull(mrTemp.getIncludeDDInAge()).toString());
            psTemp.setString("showMMUptoYear", DmShared.iNull(mrTemp.getIncludeMMInAge()).toString());
            psTemp.setString("defaultCity", DmShared.foriegnKey(mrTemp.getCityCode()));
            psTemp.setString("defaultNationality", DmShared.foriegnKey(mrTemp.getNationalityTag()));
            psTemp.setString("cancelDaysOPD", DmShared.xNull(mrTemp.getCancelDaysOPD()));
            psTemp.setString("serviceDaysOPD", DmShared.xNull(mrTemp.getServiceDays()));
            psTemp.setString("cancelDaysDiagnostic", DmShared.xNull(mrTemp.getLabCancelDays()));
            psTemp.setString("serviceDaysDiagnostic", DmShared.xNull(mrTemp.getDiagnosticDays()));
            psTemp.setString("isReligionMandatory", DmShared.xNull((mrTemp.isReligionMandatory() ? "1" : "0")));
            psTemp.setString("tokenBlockNature", DmShared.iNull(mrTemp.getTokenBlockNature().getId()).toString());
            psTemp.setString("isIdTypeMandatory", DmShared.xNull(mrTemp.isIdTypeMandatory() ? "1" : "0"));
            psTemp.setString("infantUnit", DmShared.foriegnKey(mrTemp.getIpdInfantUnitTag()));
            psTemp.setString("isRoomClean", DmShared.xNull(mrTemp.isRoomClean() ? "1" : "0"));
            psTemp.setString("roomPostIsAuto", DmShared.xNull(mrTemp.isAutoRoomPost() ? "1" : "0"));
            psTemp.setString("roomPostTimeFormat", DmShared.xNull(mrTemp.getIpdTimeFormatRoomTag()));
            psTemp.setString("roomPostCheckoutTime", DmServer.vNull(mrTemp.getIpdCheckoutTime(), "0.00"));
            psTemp.setString("roomPostHalfDayUpto", DmServer.vNull(mrTemp.getIpdHalfDayUpToMin(), "0"));
            psTemp.setString("roomPostGraceMinuteIn", DmShared.xNull(mrTemp.getIpdGraceMinuteIn()));
            psTemp.setString("roomPostGraceMinuteOut", DmShared.xNull(mrTemp.getIpdGraceMinuteOut()));
            psTemp.setString("roomPostPercentage", DmShared.xNull(mrTemp.getIpdHalfDayPercentage()));
            psTemp.setString("IPDControlAc", DmShared.foriegnKey(mrTemp.getCtrlAcIPDTag()));
            psTemp.setString("OPDControlAc", DmShared.foriegnKey(mrTemp.getCtrlAcOPDTag()));
            psTemp.setString("diagnosticControlAc", DmShared.foriegnKey(mrTemp.getCtrlAcLABTag()));
            psTemp.setString("emergencyControlAc", DmShared.foriegnKey(mrTemp.getCtrlAcEmergencyTag()));
            psTemp.setString("dayCareControlAc", DmShared.foriegnKey(mrTemp.getCtrlAcDayCareTag()));
            psTemp.setString("directPrescription", DmShared.xNull("0"));
            psTemp.setString("disallowType", DmShared.xNull(mrTemp.getDisallowTypeTag()));
            psTemp.setString("eodPostingType", DmShared.xNull(mrTemp.getEodPostingType()));
            psTemp.setString("opdNoOfSticker", DmServer.vNull(mrTemp.getStickerOPD(), "0"));
            psTemp.setString("ipdNoOfSticker", DmServer.vNull(mrTemp.getStickerIPD(), "0"));
            psTemp.setString("diagnosticNoOfSticker", DmServer.vNull(mrTemp.getStickerLAB(), "0"));
            psTemp.setString("isDirectSampleLabCollection", DmShared.xNull(mrTemp.isDirectLabCollection() ? "1" : "0"));
            psTemp.setString("pancardLimit", DmServer.vNull(mrTemp.getPanCardLimit(), "0.00"));
            psTemp.setString("cashpaymentLimit", DmServer.vNull(mrTemp.getCashPaymentLimit(), "0.00"));
            psTemp.setString("isIPDExistingPatient", DmShared.xNull(mrTemp.isExistingPatient() ? "1" : "0"));
            psTemp.setString("isPatientInitialMandatory", DmServer.vNull(mrTemp.isPatientInitialMandatory() ? "1" : "0"));
            psTemp.setString("isAddressMandatory", DmServer.vNull(mrTemp.isAddressMandatory() ? "1" : "0"));
            psTemp.setString("isCityMandatory", DmServer.vNull(mrTemp.isCityMandatory() ? "1" : "0"));
            psTemp.setString("isGuardianInitialMandatory", DmServer.vNull(mrTemp.isGuardianInitialMandatory() ? "1" : "0"));
            psTemp.setString("isCasteMandatory", DmServer.vNull(mrTemp.isCasteMandatory() ? "1" : "0"));
            psTemp.setString("isMaritalStatusMandatory", DmServer.vNull(mrTemp.isMaritalMandatory() ? "1" : "0"));
            psTemp.setString("isBloodGroupMandatory", DmServer.vNull(mrTemp.isBloodGroupMandatory() ? "1" : "0"));
            psTemp.setString("isEmailMandatory", DmServer.vNull(mrTemp.isEmailMandatory() ? "1" : "0"));
            psTemp.setString("isDiagnosticAutoVerify", DmServer.vNull(mrTemp.isLabAutoVerify() ? "1" : "0"));
            psTemp.setString("isDiagnosticBalancePrintAllow", DmServer.vNull(mrTemp.isLabBalancePrint() ? "1" : "0"));
            psTemp.setString("isIPDRoomMandatory", DmServer.vNull(mrTemp.isRoomMandatory() ? "1" : "0"));
            psTemp.setString("isServiceUnitEditable", DmServer.vNull(mrTemp.isIsServiceUnitEditable()  ? "1" : "0"));
            psTemp.setString("nightShiftFromTime", DmServer.vNull(mrTemp.getNightshiftFrom(), "0.00"));
            psTemp.setString("nightShiftToTime", DmServer.vNull(mrTemp.getNightshiftTo(), "0.00"));
            psTemp.setString("newBornDD", DmServer.vNull(mrTemp.getNewBornDay(), "0"));
            psTemp.setString("newBornMM", DmServer.vNull(mrTemp.getNewBornMonth(), "0"));
            psTemp.setString("newBornYY", DmServer.vNull(mrTemp.getNewBornYear(), "0"));
            psTemp.setString("infantMM", DmServer.vNull(mrTemp.getInfantMonth(), "0"));
            psTemp.setString("infantYY", DmServer.vNull(mrTemp.getInfantYear(), "0"));
            psTemp.setString("childMM", DmServer.vNull(mrTemp.getChildMonth(), "0"));
            psTemp.setString("childYY", DmServer.vNull(mrTemp.getChildYear(), "0"));
            psTemp.setString("userName", DmShared.xNull("sa"));
            psTemp.setString("diagnosticHospitalAc", DmShared.foriegnKey(mrTemp.getDiagnosticHospitalAcTag()));
            psTemp.setString("diagnosticSelfAc", DmShared.foriegnKey(mrTemp.getDiagnosticSelfAcTag()));
            psTemp.setString("diagnostic_Reg_Addition", DmShared.foriegnKey(mrTemp.getDiagnosticRegAdditionTag()));
            psTemp.setString("orgReceiptCardCode", DmShared.foriegnKey(mrTemp.getOrgReceiptCardTypeTag()));
            psTemp.setString("hospitalHoldCardCode", DmShared.foriegnKey(mrTemp.getHospitalHoldCardTypeTag()));
            psTemp.setString("uhidDefaultCardType", DmShared.foriegnKey(mrTemp.getUhidDefaultCardTypeCode()));
            psTemp.setString("isPatientImageMandatory", DmServer.vNull(mrTemp.isPatientImageMandatory() ? "1" : "0"));
            psTemp.setString("isOtpRequiredForRefund", DmServer.vNull(mrTemp.isIsOtpRequiredForRefund() ? "1" : "0"));
            psTemp.setString("isIcdxMandatory", DmServer.vNull(mrTemp.isIsIcdxMandatory() ? "1" : "0"));
            psTemp.setString("isAreaMandatory", DmServer.vNull(mrTemp.isAreaMandatory() ? "1" : "0"));
            psTemp.setString("isGuardianNameMandatory", DmServer.vNull(mrTemp.isGuardianMandatory() ? "1" : "0"));
            psTemp.setString("isDiagnosticPrintOnLetterhead", DmServer.vNull(mrTemp.isDiagPrintOnLetterHead() ? "1" : "0"));
            psTemp.setString("isHourlyServiceRoundOff", DmServer.vNull(mrTemp.getHourlyServiceRoundOffTag()));
            psTemp.setString("dietary_property1", DmShared.xNull(mrTemp.getDietaryProperty1()));
            psTemp.setString("dietary_property2", DmShared.xNull(mrTemp.getDietaryProperty2()));
            psTemp.setString("dietary_property3", DmShared.xNull(mrTemp.getDietaryProperty3()));
            psTemp.setString("dietary_property4", DmShared.xNull(mrTemp.getDietaryProperty4()));
            psTemp.setString("dietary_property5", DmShared.xNull(mrTemp.getDietaryProperty5()));
            psTemp.setString("dietary_property6", DmShared.xNull(mrTemp.getDietaryProperty6()));
            psTemp.setString("opdAllowPatientForSite", DmShared.xNull(DmServer.getSsnSiteCode(rssTemp)));
            psTemp.setString("ipdAllowPatientForSite", DmShared.xNull(DmServer.getSsnSiteCode(rssTemp)));
            psTemp.setString("diagnosticAllowPatientForSite", DmShared.xNull(DmServer.getSsnSiteCode(rssTemp)));
            psTemp.setString("dayCareAllowPatientForSite", DmShared.xNull(DmServer.getSsnSiteCode(rssTemp)));
            psTemp.setString("defaultEstimatePackageCode", DmShared.foriegnKey(mrTemp.getPackageCode()));
            psTemp.setString("defaultOpdPharmacySiteCode", DmShared.xNull(mrTemp.getDefaultPharmacySiteTag()));
            psTemp.setString("opdHistoryViewDays", DmShared.xNull(mrTemp.getHistoryViewDays()));
            psTemp.setString("appointmentHistoryViewDays", DmShared.xNull(mrTemp.getAppointmentHistoryViewDays()));
            psTemp.setString("labHistoryViewDays", DmShared.xNull(mrTemp.getLabHistoryViewDays()));

//            psTemp.setString("bloodBagIssueDepartmentCode", DmShared.foriegnKey(mrTemp.getBloodBagDepartmentCode()));
//            psTemp.setString("createComponentDepartmentCode", DmShared.foriegnKey(mrTemp.getBloodComponentDepartmentCode()));
            psTemp.setString("bloodBank_CIP_Addition", DmShared.foriegnKey(mrTemp.getBloodBank_CIP_Addition()));
            psTemp.setString("secondaryCurrencyCode", DmShared.foriegnKey(mrTemp.getSecondaryCurrencyCode()));
            if (mrTemp.getAlgoNature() != null) {
                psTemp.setString("OPDQueueAlgo", DmShared.iNull(mrTemp.getAlgoNature().getId()).toString());
            } else {
                psTemp.setString("OPDQueueAlgo", null);
            }

            psTemp.setString("OPDValidity", DmShared.foriegnKey(mrTemp.getOpdValidity()));
            psTemp.setString("OPDNoOfVisit", DmShared.foriegnKey(mrTemp.getOpdNoOfVisit()));
            psTemp.setString("donorDefaultCampCode", DmShared.foriegnKey(mrTemp.getDonorDefaultCampCode()));
            psTemp.setString("OPDisRoomWiseRegistration", DmShared.iNull(mrTemp.getOpdIsRoomWiseRegistration()).toString());
            psTemp.setString("appointmentBookUptoDays", DmServer.vNull(mrTemp.getAppointmentBookUptoDays(), "0"));
            psTemp.setString("dietScheduleDays", DmShared.xNull(mrTemp.getDietScheduleDays()));
            psTemp.setString("appointmentPaymentGatewayAllowed", DmServer.vNull(mrTemp.isAppointmentPaymentGatewayAllowed() ? "1" : "0"));
            psTemp.setString("paymentGatewayURL", DmShared.xNull(mrTemp.getPaymentGatewayURL()));
            psTemp.setString("paymentOption", DmShared.xNull(mrTemp.getPaymentOptionTag()));
            psTemp.setString("autoOPDRegAfterAppointment", DmServer.vNull(mrTemp.isAutoOPDRegAfterAppointment() ? "1" : "0"));
            psTemp.setString("runFromWebsite", DmServer.vNull(mrTemp.isRunFromWebsite() ? "1" : "0"));
            psTemp.setString("isAutoInTransitByMedicineTransfer", DmServer.vNull(mrTemp.isAutoInTransitByMedicineTransfer() ? "1" : "0"));
            psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
            psTemp.executeUpdate();
            psTemp.close();

            //===========================================================================================================================
            spBegin.commit();
            rtnStatus = DmImportStatus.SUCCESS.getId();

        } catch (Exception e) {
            try {
                if (status == DmImportStatus.NONE.getId()) {
                    status = DmImportStatus.ERROR.getId();
                }
                if (status > rtnStatus) {
                    rtnStatus = status;
                }
                refreshDataStructure.writeLine(e.getMessage());
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }
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
        return rtnStatus;
    }

    private String getVoucherType(String vTypeShortName, Connection cnTemp) {
        String rntValue = "", sqlQuery;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        try {
            if (!vTypeShortName.equalsIgnoreCase("")) {
                sqlQuery = " select v_Type from voucher_Type where short_Name =:short_Name";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("short_Name", DmShared.xNull(vTypeShortName));
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    rntValue = DmShared.xNull(rsTemp.getString("v_Type"));
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
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rntValue;
    }
}
