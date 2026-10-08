package aarogya.master.consultantMaster.server;

import aarogya.master.consultantMaster.shared.*;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.ServicePriceList_Mdl;
import aarogya.selfToolkit.stStatic.shared.STEnum.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmFileManager.server.DmFileManager_Server;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.self.shared.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ConsultantMaster_SrvImpl extends DmRSSPrivate implements ConsultantMaster_Srv {

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator() {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);
        String sqlQuery;
        DmPreparedStatement psTemp = null;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT cm.subCode FROM consultantmast cm "
                    + " Left Join subGroup sg on cm.subCode = sg.subCode "
                    + " where sg.filterNature = :filterNature "
                    + " ORDER BY sg.name ;";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("filterNature", DmShared.xNull(DmServer.getCodeViaManualCode("CN", "manualCode", "subgroupNature", "Code", cnTemp)));
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                lstNavigator.add(lstNavigatorTmp);
            }
            dmrTemp.setStatus(true);
            rsTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
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
    public DmResult<ConsultantMaster_Mdl> getMoveRec(String strKey) {
        DmResult<ConsultantMaster_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, organization, siteCode, defaultService, previousServiceCode = "";
        ConsultantMaster_Mdl mrTemp = new ConsultantMaster_Mdl();
        GrdClinicDetail_Mdl clinicDetail;
        GrdTiming_Mdl timing;
        GrdQueueSequence_Mdl queue;
        List<GrdService_Mdl> lstService = new ArrayList<GrdService_Mdl>();
        GrdService_Mdl service = new GrdService_Mdl();
        GrdBankDetail_Mdl bankDetail;
        GrdRoomCategory_Mdl roomCat = new GrdRoomCategory_Mdl();
        GrdSiteDetail_Mdl siteDetail;
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            organization = DmServer.getCodeViaManualCode("CP", "shortName", "organizationmast", "subCode", cnTemp);
            if (DmShared.iNull(organization).longValue() == 0) {
                throw new UnsupportedOperationException("Invalid 'CP' Code. Create 'CP' code.");
            }
            defaultService = DmServer.getCodeViaManualCode("CF", "shortName", "patientservicemast", "code", cnTemp);
            if (DmShared.iNull(defaultService).longValue() == 0) {
                throw new UnsupportedOperationException("Invalid 'CF' code. Create 'CF' Code");
            }
            siteCode = DmServer.getSsnSiteCode(this);
            mrTemp.setOrganizaton(organization);
            mrTemp.setService(defaultService);

            sqlQuery = "select  cm.subCode, sg.name, sg.initial, sg.displayName, sg.manualCode, cm.consultantSpeciCode, spec.name as specility,  "
                    + "		sg2.departmentCode, cm.queueAlgo, cm.deviceCode, cm.deviceBlock,   sdm.name AS department,  sg.mobile,cm.mobileHome,  "
                    + "		sg.eMail, sg2.dob, sg2.doa, cm.OPDValidity, sg.isActive,  cm.mciReg, sg.remark, sg.u_Name, sg.preparedBy,  "
                    + "		pspl.applicableFrom, cm.roomCode, rm.roomNo as hkLocationName, cm.nature, sg.TDS_Catg, TC.Name as TCName,  "
                    + "		cm.noOfVisit, sg.pan, cm.regionalDescription  "
                    + "		from consultantMast cm  "
                    + "		left join subgroup sg on cm.subCode = sg.subcode  "
                    + "		left join subgroup2 sg2 on cm.subCode = sg2.subcode  "
                    + "		left join subGroupNature as sgn on sg.nature=sgn.code  "
                    + "		left join consultantspecimast spec on cm.consultantSpeciCode = spec.code  "
                    + "		left join subgroup sdm on sg2.departmentCode = sdm.subcode  "
                    + "		left join roomMast rm on cm.roomCode = rm.code  "
                    + "		left join TDSCat tc on tc.code = sg.TDS_Catg  "
                    + "		left join ( "
                    + "             select  max(consultantCode) as consultantCode, max(applicableFrom) as applicableFrom  "
                    + "                     from patientservicepricelist  "
                    + "                     where organizationCode = :organizationCode and EntryFrom = :entryFrom and site_code = :siteCode  "
                    + "                     and applicableFrom <= cast(:vDate as date)  "
                    + "                     and consultantCode = :code  "
                    + "                     group by consultantCode  "
                    + "		) pspl on cm.subCode = pspl.consultantCode  "
                    + "		where cm.subcode= :code and sg.filterNature = :filterNature;";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            psTemp.setDate("vDate", new Date(DmServer.getServerTimestamp().getTime()));
            psTemp.setString("organizationCode", organization);
            psTemp.setString("siteCode", siteCode);
            psTemp.setString("entryFrom", String.valueOf(PriceListEntryFrom.CONSULTANT_MASTER.toInt()).trim());
            psTemp.setString("filterNature", DmShared.xNull(DmServer.getCodeViaManualCode("CN", "manualCode", "subgroupNature", "Code", cnTemp)));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.setDisplayName(DmShared.xNull(rsTemp.getString("displayName")));
                mrTemp.setInitial(DmShared.xNull(rsTemp.getString("initial")));

                mrTemp.setActiveTag(DmShared.vNull(rsTemp.getString("isActive")).toString());
                mrTemp.setActiveText(DmShared.iNull(rsTemp.getString("isActive")).intValue() == 0 ? "No" : "Yes");

                mrTemp.setApplicableFrom(DmShared.dNull(rsTemp.getDate("applicableFrom")));
                mrTemp.setDoa(DmShared.dNull(rsTemp.getDate("doa")));
                mrTemp.setDob(DmShared.dNull(rsTemp.getDate("dob")));
                mrTemp.setDepartmentTag(DmShared.xNull(rsTemp.getString("departmentCode")));
                mrTemp.setDepartmentText(DmShared.xNull(rsTemp.getString("department")));
                mrTemp.setDeviceBlock(DmShared.xNull(rsTemp.getString("deviceBlock")));
                mrTemp.setDeviceCode(DmShared.xNull(rsTemp.getString("deviceCode")));
                mrTemp.seteMail(DmShared.xNull(rsTemp.getString("eMail")));
                mrTemp.setMciReg(DmShared.xNull(rsTemp.getString("mciReg")));
                mrTemp.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                mrTemp.setMobileHome(DmShared.xNull(rsTemp.getString("mobileHome")));
                mrTemp.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                mrTemp.setShortName(DmShared.xNull(rsTemp.getString("manualCode")));
                mrTemp.setSpecilizationTag(DmShared.xNull(rsTemp.getString("consultantSpeciCode")));
                mrTemp.setSpecilizationText(DmShared.xNull(rsTemp.getString("specility")));
                mrTemp.setValidUpTo(DmServer.vNull(rsTemp.getString("OPDValidity"), "0"));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setLocationTag(DmShared.xNull(rsTemp.getString("roomCode")));
                mrTemp.setLocationText(DmShared.xNull(rsTemp.getString("hkLocationName")));
                mrTemp.setTdsCategory(DmShared.xNull(rsTemp.getString("TDS_Catg")));
                mrTemp.setTdsCategoryName(DmShared.xNull(rsTemp.getString("TCName")));
                mrTemp.setNoOfVisit(DmShared.xNull(rsTemp.getString("NoOfVisit")));
                mrTemp.setPan(DmShared.xNull(rsTemp.getString("pan")));
                mrTemp.setRegionalDescription(DmShared.xNull(rsTemp.getString("regionalDescription")));
                if (DmShared.iNull(rsTemp.getString("queueAlgo")).intValue() > 0) {
                    mrTemp.setQueueTag(DmShared.iNull(rsTemp.getString("queueAlgo")).toString());
                    mrTemp.setQueueText(QueueAlgoNature.getKeyOf(DmShared.iNull(rsTemp.getString("queueAlgo")).intValue()).getValue());
                }
                mrTemp.setConsultantNatureTag(DmShared.iNull(rsTemp.getString("nature")).toString());
                mrTemp.setConsultantNatureText(DmShared.xNull(rsTemp.getString("nature")).equalsIgnoreCase("1") ? "Consultant"
                        : (DmShared.xNull(rsTemp.getString("nature")).equalsIgnoreCase("2") ? "Referred"
                        : (DmShared.xNull(rsTemp.getString("nature")).equalsIgnoreCase("3") ? "Anaesthetist"
                        : (DmShared.xNull(rsTemp.getString("nature")).equalsIgnoreCase("4") ? "Pathologist"
                        : (DmShared.xNull(rsTemp.getString("nature")).equalsIgnoreCase("5") ? "Radiologist"
                        : (DmShared.xNull(rsTemp.getString("nature")).equalsIgnoreCase("6") ? "OPD Consultant" : ""))))));
            }
            psTemp.close();
            rsTemp.close();

            sqlQuery = " select cqd.consultantCode, cqd.opdType, cqd.queuePriority, cqd.noOfPatient "
                    + "         from consultantOPDQueueDetail cqd "
                    + "         where cqd.consultantCode = :code "
                    + "         order by cqd.queuePriority ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
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

            sqlQuery = " select sm.code as site_Code , sm.siteDisplayName , sgsl.isOPDConsultant, sgsl.allowForSite "
                    + " from  siteMast sm "
                    + " left join subGroupSiteList sgsl on sm.code = sgsl.site_Code "
                    + " and sgsl.subCode = :code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                siteDetail = new GrdSiteDetail_Mdl();
                siteDetail.setSiteName(DmShared.xNull(rsTemp.getString("siteDisplayName")));
                siteDetail.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("site_Code")));
                siteDetail.setAllowForSite(DmShared.iNull(rsTemp.getString("allowForSite")).intValue() == 1 ? true : false);
                siteDetail.setIsOPDConsultant(DmShared.iNull(rsTemp.getString("isOPDConsultant")).intValue() == 1 ? true : false);
                mrTemp.getGrdSitedetail().add(siteDetail);
            }
            psTemp.close();
            rsTemp.close();

            sqlQuery = "select  ccd.consultantCode, ccd.sNo, ccd.visitSite, ccd.timingShedule, ccd.Phone, ccd.address1, "
                    + "         ccd.address2, ccd.citycode, ct.cityName  "
                    + "         from consultantclinicdetail ccd  "
                    + "         left join city ct ON ccd.citycode = ct.cityCode  "
                    + "         where ccd.consultantCode = :code ; ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                clinicDetail = new GrdClinicDetail_Mdl();
                clinicDetail.setAddress(DmShared.xNull(rsTemp.getString("address1")));
                clinicDetail.setAddress1(DmShared.xNull(rsTemp.getString("address2")));
                clinicDetail.setCityTag(DmShared.xNull(rsTemp.getString("citycode")));
                clinicDetail.setCityText(DmShared.xNull(rsTemp.getString("cityName")));
                clinicDetail.setPhone(DmShared.xNull(rsTemp.getString("Phone")));
                clinicDetail.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("VisitSite")));
                clinicDetail.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("VisitSite")));
                clinicDetail.setTiming(DmShared.xNull(rsTemp.getString("timingShedule")));
                mrTemp.getGrdClinic().add(clinicDetail);
            }
            psTemp.close();
            rsTemp.close();

            sqlQuery = "select  ctd.dayNameCode, ctd.fromTime, ctd.toTime, ctd.emergency, ctd.appointment, ctd.patient, ctd.graceMinutes, "
                    + "         ctd.reVisit,  ctd.online, ctd.onlinePatient, ctd.opd, ctd.appointmentAdvance, ctd.payClinic "
                    + "         from consultantTimeDetail ctd   "
                    + "         where (ctd.consultantCode = :code) and (ctd.applicableFrom = :applicableFrom) and (ctd.site_code = :siteCode)"
                    + "         order by ctd.sNo;";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.setString("siteCode", siteCode);
            psTemp.setDate("applicableFrom", DmServer.convertDate(mrTemp.getApplicableFrom()));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                timing = new GrdTiming_Mdl();
                timing.setEmergency(DmServer.vNull(rsTemp.getString("emergency"), "0"));
                timing.setFromTime(DmServer.vNull(rsTemp.getString("fromTime"), "00.00"));
                timing.setGraceMin(DmServer.vNull(rsTemp.getString("graceMinutes"), "0"));
                timing.setOpd(DmServer.vNull(rsTemp.getString("opd"), "0"));
                timing.setAppointment(DmServer.vNull(rsTemp.getString("appointment"), "0"));
                timing.setAppointmentAdvance(DmServer.vNull(rsTemp.getString("appointmentAdvance"), "0"));
                timing.setPatient(DmServer.vNull(rsTemp.getString("patient"), "0"));
                timing.setReVisit(DmServer.vNull(rsTemp.getString("reVisit"), "0"));
                timing.setToTime(DmServer.vNull(rsTemp.getString("toTime"), "00.00"));
                timing.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("dayNameCode")));
                timing.setSharedKeyFieldName(DmWeekdays.getValueFrom(DmShared.iNull(rsTemp.getString("dayNameCode")).intValue()).getWeekDayName());
                timing.setOnlineCharge(DmServer.vNull(rsTemp.getString("online"), "0.00"));
                timing.setOnlinePatient(DmServer.vNull(rsTemp.getString("onlinePatient"), "0"));
                timing.setPayClinic(DmServer.vNull(rsTemp.getString("payClinic"), "0"));
                mrTemp.getGrdTime().add(timing);
            }
            psTemp.close();
            rsTemp.close();

            sqlQuery = "select  max(pspl.serviceCode) as serviceCode, max(psm.name) as service, max(pspl.rate) as rate, pspl.roomCatCode, "
                    + "         max(rcm.name) as roomCat  "
                    + "         from patientservicepricelist pspl  "
                    + "         left join patientservicemast psm ON pspl.serviceCode = psm.code  "
                    + "         left join siteMast AS sm ON sm.code=pspl.site_Code"
                    + "         left join roomcatmast rcm ON pspl.roomCatCode = rcm.code  "
                    + "         where pspl.organizationCode = :organizationCode and pspl.site_code = :siteCode and pspl.EntryFrom = :entryFrom "
                    + "         and pspl.consultantCode = :code and pspl.entryFromNature = '0' and pspl.applicableFrom = :applicableFrom  "
                    + "         group by pspl.serviceCode, pspl.roomCatCode  "
                    + "         order by max(psm.name), max(rcm.name) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.setString("organizationCode", organization);
            psTemp.setString("siteCode", siteCode);
            psTemp.setString("entryFrom", String.valueOf(PriceListEntryFrom.CONSULTANT_MASTER.toInt()).trim());
            psTemp.setDate("applicableFrom", DmServer.convertDate(mrTemp.getApplicableFrom()));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                if (!DmShared.xNull(rsTemp.getString("serviceCode")).equalsIgnoreCase(previousServiceCode)) {
                    service = new GrdService_Mdl();
                    lstService.add(service);
                }

                service.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("serviceCode")));
                service.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("service")));

                if (DmShared.xNull(rsTemp.getString("roomCatCode")).trim().equalsIgnoreCase("")) {
                    service.setAmount(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                } else {
                    roomCat = new GrdRoomCategory_Mdl();

                    roomCat.setAmount(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                    roomCat.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("roomCatCode")));
                    roomCat.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("roomCat")));
                    service.getLstRoomCategory().add(roomCat);
                }
                previousServiceCode = DmShared.xNull(rsTemp.getString("serviceCode"));
            }
            psTemp.close();
            rsTemp.close();
            mrTemp.getGrdService().clear();
            mrTemp.getGrdService().addAll(lstService);

            sqlQuery = " select subCode,name from subGroup sg left join consultantLinkDepartment cld on cld.departmentCode = sg.subCode "
                    + " where consultantCode = :code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            String linkDepartmentName = "";
            String linkDepartmentCode = "";
            while (rsTemp.next()) {
                linkDepartmentName += DmShared.xNull(rsTemp.getString("name")) + ",";
                linkDepartmentCode += DmShared.xNull(rsTemp.getString("subCode")) + ",";
            }
            if (!linkDepartmentCode.equals("")) {
                linkDepartmentCode = linkDepartmentCode.substring(0, linkDepartmentCode.length() - 1);
                linkDepartmentName = linkDepartmentName.substring(0, linkDepartmentName.length() - 1);
            }
            mrTemp.setLinkDepartmentName(linkDepartmentName);
            mrTemp.setLinkDepartmentCode(linkDepartmentCode);
            psTemp.close();
            rsTemp.close();

            /*=======================================< BANK DETAIL >=========================================*/
            sqlQuery = "SELECT subCode,sNo,bankName,accountNo,branch,swiftCode,isDefault,ifscCode,branchAddress "
                    + " FROM bankDetail "
                    + " WHERE subCode = :code "
                    + " ORDER BY sNo ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                bankDetail = new GrdBankDetail_Mdl();
                bankDetail.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                bankDetail.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("sNo")));
                bankDetail.setAccountNo(DmShared.xNull(rsTemp.getString("accountNo")));
                bankDetail.setBankName(DmShared.xNull(rsTemp.getString("bankName")));
                bankDetail.setBranch(DmShared.xNull(rsTemp.getString("branch")));
                bankDetail.setSwiftNo(DmShared.xNull(rsTemp.getString("swiftCode")));
                bankDetail.setIfscCode(DmShared.xNull(rsTemp.getString("ifscCode")));
                bankDetail.setBranchAddress(DmShared.xNull(rsTemp.getString("branchAddress")));
                bankDetail.setIsDefault(DmShared.xNull(rsTemp.getString("isDefault")));
                mrTemp.getGrdBankDetail().add(bankDetail);
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

    @Override
    public DmResult<String> deleteRecord(String strKey) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery, organization, service;
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            organization = DmServer.getCodeViaManualCode("CP", "shortName", "organizationmast", "subCode", cnTemp);
            service = DmServer.getCodeViaManualCode("CF", "shortName", "patientservicemast", "code", cnTemp);
            if (DmShared.xNull(organization).trim().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid 'CP' Code. Create 'CP' code.");
            }
            if (DmShared.xNull(service).trim().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid 'CF' code. Create 'CF' Code");
            }
            sqlQuery = "SELECT COUNT(cnt) AS cnt FROM (SELECT  pspl.applicableFrom AS cnt FROM patientservicepricelist pspl "
                    + "  WHERE pspl.EntryFrom = :entryFrom AND pspl.consultantCode = :code GROUP BY pspl.applicableFrom ) AS tab ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("entryFrom", String.valueOf(PriceListEntryFrom.CONSULTANT_MASTER.toInt()));
            psTemp.setString("code", strKey);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("cnt") > 1) {
                    throw new UnsupportedOperationException("Either Record have multiple price List or price List from different site. Could not delete this record.");
                }
            }
            rsTemp.close();

            sqlQuery = "DELETE FROM patientservicepricelist WHERE EntryFrom = :entryFrom AND consultantCode = :code ; ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            psTemp.setString("entryFrom", String.valueOf(PriceListEntryFrom.CONSULTANT_MASTER.toInt()));
            psTemp.executeUpdate();

            sqlQuery = "Delete From consultantOPDQueueDetail Where consultantCode = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            psTemp.executeUpdate();

            sqlQuery = "Delete From consultantclinicdetail Where consultantCode = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            psTemp.executeUpdate();

            sqlQuery = "Delete From consultanttimedetail Where consultantCode = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            psTemp.executeUpdate();

            sqlQuery = "Delete From subGroupSiteList Where subCode = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            psTemp.executeUpdate();

            sqlQuery = "Delete From consultantmast Where subcode = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            psTemp.executeUpdate();

            sqlQuery = "Delete From subGroup2 Where subcode = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            psTemp.executeUpdate();

            sqlQuery = "Delete From subGroup Where subcode = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            psTemp.executeUpdate();

            spBegin.commit();
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
    public DmResult<String> saveRecord(ConsultantMaster_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery, siteCode, filterNature = "", consultantAcGroup = "";
        ResultSet rsTemp = null;
        Integer srNo = 0;
        ServicePriceList_Mdl priceList;
        List<ServicePriceList_Mdl> lstServicePrice = new ArrayList<ServicePriceList_Mdl>();
        dmrTemp.setData("");

        try {
            ConsultantMaster_Vld.chkSave(mrTemp);
            if (DmShared.xNull(mrTemp.getOrganizaton()).trim().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid 'CP' Code. Create 'CP' code.");
            }
            if (DmShared.xNull(mrTemp.getService()).trim().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid 'CF' code. Create 'CF' Code");
            }

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            siteCode = DmServer.getSsnSiteCode(this);

            sqlQuery = "select  count(sg.name) as cnt "
                    + "         from subgroup sg "
                    + "         where sg.name = :name and sg.subCode <> :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()).trim());
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("cnt") > 0) {
                    throw new UnsupportedOperationException("Consultant Name already exists.");
                }
            }

            sqlQuery = "select  count(sg.manualCode) as cnt "
                    + "         from subgroup sg "
                    + "         where sg.manualCode = :shortName and sg.subCode <> :code AND IsNULL(sg.manualCode, '') <> '' ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("shortName", DmShared.xNull(mrTemp.getShortName()).replace(" ", "").trim());
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("cnt") > 0) {
                    throw new UnsupportedOperationException("Short Name is already exists.");
                }
            }
            psTemp.close();
            rsTemp.close();

            consultantAcGroup = DmServer.getCodeViaManualCode("1", "1", "enviroCompany", "acGroupConsultantCode", cnTemp);

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoBigInt(this, "subGroup", cnTemp));
                DmFileManager_Server.manageLinkFileCollection(DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString(), DmShared.xNull(mrTemp.getTempKeyCode()), this, cnTemp);
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            filterNature = DmServer.getCodeViaManualCode("CN", "manualCode", "subgroupNature", "Code", cnTemp);

            if (blnAdd) {
                sqlQuery = "insert into subGroup(subCode, initial, name, displayName,manualCode,email, mobile, isActive, remark, u_EntDt,"
                        + " preparedBy, nature, filterNature, groupCode, site_Code, TDS_Catg, pan) "
                        + " VALUES (:subCode,:initial,:name, :displayName, :manualCode,:email, :mobile, :isActive,:remark, :uEntDt,"
                        + " :preparedBy,:nature,:filterNature,:groupCode,:siteCode,:tdsCatg,:pan);";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("subCode", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("initial", DmShared.foriegnKey(mrTemp.getInitial()));
                psTemp.setString("name", mrTemp.getSharedKeyFieldName());
                psTemp.setString("displayName", DmShared.xNull(mrTemp.getDisplayName()));
                psTemp.setString("manualCode", mrTemp.getShortName());
                psTemp.setString("eMail", mrTemp.geteMail());
                psTemp.setString("mobile", mrTemp.getMobile());
                psTemp.setString("isActive", DmShared.iNull(mrTemp.getActiveTag()).intValue() == 0 ? "0" : "1");

                psTemp.setString("remark", mrTemp.getRemark());
                psTemp.setString("preparedBy", DmServer.getSsnUserName(this));
                psTemp.setString("nature", DmShared.foriegnKey(filterNature));
                psTemp.setString("groupCode", DmShared.foriegnKey(consultantAcGroup));
                psTemp.setString("filterNature", DmShared.foriegnKey(filterNature));
                psTemp.setString("siteCode", DmShared.foriegnKey(siteCode));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("tdsCatg", DmShared.foriegnKey(mrTemp.getTdsCategory()));
                psTemp.setString("pan", mrTemp.getPan());

                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = "INSERT INTO subGroup2(subCode, departmentCode, dob, doa) "
                        + " VALUES (:subCode, :departmentCode, :dob, :doa );";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("subcode", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                psTemp.setDate("dob", DmServer.convertDate(mrTemp.getDob()));
                psTemp.setDate("doa", DmServer.convertDate(mrTemp.getDoa()));
                psTemp.setString("departmentCode", DmShared.foriegnKey(mrTemp.getDepartmentTag()));
                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = "insert into consultantMast(subCode, consultantSpeciCode, mobileHome, OPDValidity, mciReg, roomCode, nature, queueAlgo, "
                        + "         deviceCode, deviceBlock, noOfVisit, regionalDescription) "
                        + " VALUES (:subCode, :consultantSpeciCode, :mobileHome, :OPDValidity, :mciReg, :roomCode, :nature, :queueAlgo, :deviceCode, "
                        + "         :deviceBlock, :noOfVisit, :regionalDescription) ;";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("mobileHome", mrTemp.getMobileHome());
                psTemp.setString("consultantSpeciCode", DmShared.foriegnKey(mrTemp.getSpecilizationTag()));
                psTemp.setString("roomCode", DmShared.foriegnKey(mrTemp.getLocationTag()));
                psTemp.setString("queueAlgo", DmShared.foriegnKey(mrTemp.getQueueTag()));
                psTemp.setString("OPDValidity", DmServer.vNull(mrTemp.getValidUpTo(), "0"));
                psTemp.setString("mciReg", mrTemp.getMciReg());
                psTemp.setString("nature", DmShared.xNull(mrTemp.getConsultantNatureTag()));
                psTemp.setString("deviceCode", DmShared.xNull(mrTemp.getDeviceCode()));
                psTemp.setString("deviceBlock", DmShared.xNull(mrTemp.getDeviceBlock()));
                psTemp.setString("noOfVisit", DmShared.xNull(mrTemp.getNoOfVisit()));
                psTemp.setString("regionalDescription", mrTemp.getRegionalDescription());

                psTemp.executeUpdate();
                psTemp.close();
                if (!DmShared.xNull(mrTemp.getLinkDepartmentCode()).equalsIgnoreCase("")) {
                    String[] linkDepartmentList = mrTemp.getLinkDepartmentCode().split(",");
                    for (String linkDepartment : linkDepartmentList) {
                        sqlQuery = "insert into consultantLinkDepartment(consultantCode, departmentCode) values (:consultantCode, :departmentCode)";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("consultantCode", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                        psTemp.setString("departmentCode", DmShared.xNull(linkDepartment));
                        psTemp.executeUpdate();
                        psTemp.close();
                    }
                }

            } else {
                sqlQuery = "UPDATE subGroup SET"
                        + "  NAME = :name,"
                        + "  displayName = :displayName,"
                        + "  manualCode = :manualCode,"
                        + "  initial = :initial,"
                        + "  nature = :nature,"
                        + "  mobile = :mobile,"
                        + "  eMail = :eMail,"
                        + "  isActive = :isActive,"
                        + "  remark = :remark,"
                        + "  u_Name = :userName,"
                        + "  filterNature = :filterNature,"
                        + "  TDS_Catg=:TDSCatg, "
                        + "  u_EntDt = :uEntDt,  "
                        + "  pan=:pan "
                        + "  WHERE subCode = :subCode ;";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("subCode", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("initial", DmShared.foriegnKey(mrTemp.getInitial()));
                psTemp.setString("name", mrTemp.getSharedKeyFieldName());
                psTemp.setString("displayName", DmShared.xNull(mrTemp.getDisplayName()));
                psTemp.setString("manualCode", mrTemp.getShortName());
                psTemp.setString("eMail", mrTemp.geteMail());
                psTemp.setString("mobile", mrTemp.getMobile());
                psTemp.setString("isActive", DmShared.iNull(mrTemp.getActiveTag()).intValue() == 0 ? "0" : "1");
                psTemp.setString("remark", mrTemp.getRemark());
                psTemp.setString("userName", DmServer.getSsnUserName(this));
                psTemp.setString("nature", DmShared.foriegnKey(filterNature));
                psTemp.setString("filterNature", DmShared.foriegnKey(filterNature));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("tdsCatg", DmShared.foriegnKey(mrTemp.getTdsCategory()));
                psTemp.setString("pan", mrTemp.getPan());

                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = "UPDATE subGroup2 SET "
                        + "  departmentCode = :departmentCode, "
                        + "  dob = :dob, "
                        + "  doa = :doa "
                        + " WHERE subCode = :subcode ;";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("subcode", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                psTemp.setDate("dob", DmServer.convertDate(mrTemp.getDob()));
                psTemp.setDate("doa", DmServer.convertDate(mrTemp.getDoa()));
                psTemp.setString("departmentCode", DmShared.foriegnKey(mrTemp.getDepartmentTag()));
                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = "UPDATE consultantMast SET "
                        + "  consultantSpeciCode = :consultantSpeciCode, "
                        + "  mobileHome = :mobileHome, "
                        + "  OPDValidity = :OPDValidity, "
                        + "  roomCode = :roomCode, "
                        + "  mciReg = :mciReg,"
                        + "  queueAlgo = :queueAlgo,"
                        + "  deviceBlock = :deviceBlock,"
                        + "  deviceCode = :deviceCode,"
                        + "  noOfVisit = :noOfVisit,"
                        + "  nature=:nature, "
                        + "  regionalDescription=:regionalDescription "
                        + " WHERE subCode = :subCode ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("subcode", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("mobileHome", mrTemp.getMobileHome());
                psTemp.setString("consultantSpeciCode", DmShared.foriegnKey(mrTemp.getSpecilizationTag()));
                psTemp.setString("queueAlgo", DmShared.foriegnKey(mrTemp.getQueueTag()));
                psTemp.setString("OPDValidity", DmServer.vNull(mrTemp.getValidUpTo(), "0"));
                psTemp.setString("mciReg", mrTemp.getMciReg());
                psTemp.setString("roomCode", DmShared.foriegnKey(mrTemp.getLocationTag()));
                psTemp.setString("nature", DmShared.xNull(mrTemp.getConsultantNatureTag()));
                psTemp.setString("deviceCode", DmShared.xNull(mrTemp.getDeviceCode()));
                psTemp.setString("deviceBlock", DmShared.xNull(mrTemp.getDeviceBlock()));
                psTemp.setString("noOfVisit", DmShared.xNull(mrTemp.getNoOfVisit()));
                psTemp.setString("regionalDescription", mrTemp.getRegionalDescription());

                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = " delete from consultantLinkDepartment where consultantCode  = :consultantCode ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("consultantCode", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                psTemp.executeUpdate();
                psTemp.close();

                if (!DmShared.xNull(mrTemp.getLinkDepartmentCode()).equalsIgnoreCase("")) {
                    String[] linkDepartmentList = mrTemp.getLinkDepartmentCode().split(",");
                    for (String linkDepartment : linkDepartmentList) {
                        sqlQuery = "insert into consultantLinkDepartment(consultantCode, departmentCode) values (:consultantCode, :departmentCode)";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("consultantCode", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                        psTemp.setString("departmentCode", DmShared.xNull(linkDepartment));
                        psTemp.executeUpdate();
                        psTemp.close();
                    }

                }
            }

            //-------------------------------------------subGroupSiteList-------------------
            sqlQuery = "Delete From subGroupSiteList Where subCode= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();

            for (GrdSiteDetail_Mdl siteDetail : mrTemp.getGrdSitedetail()) {
                if (siteDetail.isAllowForSite()) {
                    sqlQuery = "INSERT INTO subGroupSiteList(subCode, site_Code, isOPDConsultant, allowForSite) "
                            + " VALUES (:subcode, :siteCode, :isOPDConsultant, :allowForSite );";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("subcode", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                    psTemp.setString("siteCode", DmShared.xNull(siteDetail.getSharedKeyFieldCode()));
                    if (siteDetail.isOPDConsultant()) {
                        psTemp.setString("isOPDConsultant", "1");
                    } else {
                        psTemp.setString("isOPDConsultant", "0");
                    }
                    if (siteDetail.isAllowForSite()) {
                        psTemp.setString("allowForSite", "1");
                    } else {
                        psTemp.setString("allowForSite", "0");
                    }
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }
            //-----------------------------------------------------------------------
            sqlQuery = "Delete From consultantOPDQueueDetail Where consultantCode= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();

            for (GrdQueueSequence_Mdl sequenceMdl : mrTemp.getLstQueue()) {
                if (DmShared.iNull(sequenceMdl.getPrioritySerial()).intValue() > 0) {
                    sqlQuery = "insert into consultantOPDQueueDetail (consultantCode, opdType, queuePriority, noOfPatient) "
                            + "values (:subCode, :opdType, :queuePrioriy, :noOfPatient)";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                    psTemp.setString("opdType", DmShared.foriegnKey(sequenceMdl.getSharedKeyFieldCode()));
                    psTemp.setString("queuePrioriy", DmServer.vNull(sequenceMdl.getPrioritySerial()));
                    psTemp.setString("noOfPatient", DmShared.xNull(sequenceMdl.getTotalPatient()));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

            sqlQuery = "Delete From consultantClinicDetail Where consultantCode= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();

            srNo = 0;
            for (int i = 0; i < mrTemp.getGrdClinic().size(); i++) {
                ++srNo;
                GrdClinicDetail_Mdl lst = mrTemp.getGrdClinic().get(i);
                if (!DmShared.xNull(lst.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                    sqlQuery = "INSERT INTO consultantclinicdetail (consultantCode, sNo, visitSite, timingShedule, Phone, address1, address2, citycode) "
                            + " VALUES (:doctorCode, :srNo, :visitSite, :timingShedule, :Phone, :address, :address1, :citycode);";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setInt("srNo", srNo);
                    psTemp.setString("doctorCode", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                    psTemp.setString("visitSite", DmShared.xNull(lst.getSharedKeyFieldCode()));
                    psTemp.setString("timingShedule", DmShared.xNull(lst.getTiming()));
                    psTemp.setString("Phone", DmShared.xNull(lst.getPhone()));
                    psTemp.setString("address", DmShared.xNull(lst.getAddress()));
                    psTemp.setString("address1", DmShared.xNull(lst.getAddress1()));
                    psTemp.setString("citycode", DmShared.foriegnKey(lst.getCityTag()));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

            if (mrTemp.getGrdService() != null) {
                for (int i = 0; i < mrTemp.getGrdService().size(); i++) {
                    GrdService_Mdl lstService = mrTemp.getGrdService().get(i);
                    if (!DmShared.xNull(lstService.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                        priceList = new ServicePriceList_Mdl();
                        priceList.setConsultantCode(mrTemp.getSharedKeyFieldCode());
                        priceList.setDate(DmShared.dNull(mrTemp.getApplicableFrom()));
                        priceList.setOrganizationCode(mrTemp.getOrganizaton());
                        priceList.setPriceListEntryFrom(PriceListEntryFrom.CONSULTANT_MASTER);
                        priceList.setEntryFromNature("0");
                        priceList.setRateListCode(mrTemp.getRateListCode());
                        priceList.setPriceListNature(PriceListNature.None);
                        priceList.setRate(DmServer.vNull(lstService.getAmount(), "0.00"));
                        priceList.setServiceCode(DmShared.xNull(lstService.getSharedKeyFieldCode()));
                        priceList.setSiteCode(siteCode);
                        lstServicePrice.add(priceList);
                        for (int j = 0; j < lstService.getLstRoomCategory().size(); j++) {
                            GrdRoomCategory_Mdl lstRoomCategory = lstService.getLstRoomCategory().get(j);
                            if (!DmShared.xNull(lstRoomCategory.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                                if (DmShared.vNull(lstRoomCategory.getAmount()).doubleValue() > 0) {
                                    priceList = new ServicePriceList_Mdl();
                                    priceList.setConsultantCode(mrTemp.getSharedKeyFieldCode());
                                    priceList.setDate(mrTemp.getApplicableFrom());
                                    priceList.setOrganizationCode(mrTemp.getOrganizaton());
                                    priceList.setPriceListEntryFrom(PriceListEntryFrom.CONSULTANT_MASTER);
                                    priceList.setEntryFromNature("0");
                                    priceList.setRateListCode(mrTemp.getRateListCode());
                                    priceList.setPriceListNature(PriceListNature.None);
                                    priceList.setRate(DmServer.vNull(lstRoomCategory.getAmount(), "0.00"));
                                    priceList.setRoomCatCode(lstRoomCategory.getSharedKeyFieldCode());
                                    priceList.setServiceCode(lstService.getSharedKeyFieldCode());
                                    priceList.setSiteCode(siteCode);
                                    lstServicePrice.add(priceList);
                                }
                            }
                        }
                    }
                }
            }

            if (mrTemp.getGrdTime() != null) {
                for (int k = 0; k < mrTemp.getGrdTime().size(); k++) {
                    GrdTiming_Mdl timingNode = mrTemp.getGrdTime().get(k);
                    if (!DmShared.xNull(timingNode.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                        priceList = getTimeWisePriceList(timingNode, PriceListNature.ReVisit, DmServer.vNull(timingNode.getReVisit(), "0.00"), mrTemp.getSharedKeyFieldCode(), mrTemp.getApplicableFrom(), mrTemp.getOrganizaton(), mrTemp.getService(), mrTemp.getRateListCode());
                        lstServicePrice.add(priceList);
                        if (DmShared.vNull(timingNode.getEmergency()).doubleValue() > 0) {
                            priceList = getTimeWisePriceList(timingNode, PriceListNature.Emergency, DmServer.vNull(timingNode.getEmergency(), "0.00"), mrTemp.getSharedKeyFieldCode(), mrTemp.getApplicableFrom(), mrTemp.getOrganizaton(), mrTemp.getService(), mrTemp.getRateListCode());
                            lstServicePrice.add(priceList);
                        }
                        if (DmShared.vNull(timingNode.getOpd()).doubleValue() > 0) {
                            priceList = getTimeWisePriceList(timingNode, PriceListNature.OPD, DmServer.vNull(timingNode.getOpd(), "0.00"), mrTemp.getSharedKeyFieldCode(), mrTemp.getApplicableFrom(), mrTemp.getOrganizaton(), mrTemp.getService(), mrTemp.getRateListCode());
                            lstServicePrice.add(priceList);
                        }
                        if (DmShared.vNull(timingNode.getOnlineCharge()).doubleValue() > 0) {
                            priceList = getTimeWisePriceList(timingNode, PriceListNature.Online, DmServer.vNull(timingNode.getOnlineCharge(), "0.00"), mrTemp.getSharedKeyFieldCode(), mrTemp.getApplicableFrom(), mrTemp.getOrganizaton(), mrTemp.getService(), mrTemp.getRateListCode());
                            lstServicePrice.add(priceList);
                        }
                        if (DmShared.vNull(timingNode.getAppointment()).doubleValue() > 0) {
                            priceList = getTimeWisePriceList(timingNode, PriceListNature.PreAppointment, DmServer.vNull(timingNode.getAppointment(), "0.00"), mrTemp.getSharedKeyFieldCode(), mrTemp.getApplicableFrom(), mrTemp.getOrganizaton(), mrTemp.getService(), mrTemp.getRateListCode());
                            lstServicePrice.add(priceList);
                        }
                        if (DmShared.vNull(timingNode.getAppointmentAdvance()).doubleValue() > 0) {
                            priceList = getTimeWisePriceList(timingNode, PriceListNature.AppointmentAdvance, DmServer.vNull(timingNode.getAppointmentAdvance(), "0.00"), mrTemp.getSharedKeyFieldCode(), mrTemp.getApplicableFrom(), mrTemp.getOrganizaton(), mrTemp.getService(), mrTemp.getRateListCode());
                            lstServicePrice.add(priceList);
                        }
                        if (DmShared.vNull(timingNode.getPayClinic()).doubleValue() > 0) {
                            priceList = getTimeWisePriceList(timingNode, PriceListNature.PayClinic, DmServer.vNull(timingNode.getPayClinic(), "0.00"), mrTemp.getSharedKeyFieldCode(), mrTemp.getApplicableFrom(), mrTemp.getOrganizaton(), mrTemp.getService(), mrTemp.getRateListCode());
                            lstServicePrice.add(priceList);
                        }
                    }
                }
            }

            STServer.savePriceList(mrTemp.getApplicableFrom(), PriceListEntryFrom.CONSULTANT_MASTER, mrTemp.getSharedKeyFieldCode(), mrTemp.getRateListCode(), siteCode, lstServicePrice, cnTemp);

            sqlQuery = "DELETE FROM consultanttimedetail WHERE consultantCode = :code AND applicableFrom = :applicableFrom AND site_Code=:siteCode;";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
            psTemp.setDate("applicableFrom", DmServer.convertDate(mrTemp.getApplicableFrom()));
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp.executeUpdate();
            psTemp.close();

            srNo = 0;
            for (int i = 0; i < mrTemp.getGrdTime().size(); i++) {
                ++srNo;
                GrdTiming_Mdl lst = mrTemp.getGrdTime().get(i);
                if (!DmShared.xNull(lst.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                    sqlQuery = "INSERT INTO consultanttimedetail (consultantCode, sNo, dayNameCode, fromTime, toTime, opd, "
                            + "             emergency, appointment, patient, graceMinutes, reVisit, applicableFrom, "
                            + "             online, onlinePatient,site_Code, appointmentAdvance, payClinic) "
                            + " VALUES (:consultantCode, :sNo, :dayNameCode, :fromTime, :toTime, :opd, :emergency, :appointment, "
                            + "        :patient, :graceMinutes, :reVisit, :applicableFrom, :online, :onlinePatient,:siteCode, :appointmentAdvance, :payClinic);";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setInt("sNo", srNo);
                    psTemp.setString("consultantCode", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                    psTemp.setString("dayNameCode", DmShared.xNull(lst.getSharedKeyFieldCode()));
                    psTemp.setString("fromTime", DmServer.vNull(lst.getFromTime(), "0.00"));
                    psTemp.setString("toTime", DmServer.vNull(lst.getToTime(), "0.00"));
                    psTemp.setString("opd", DmServer.vNull(lst.getOpd(), "0.00"));
                    psTemp.setString("emergency", DmServer.vNull(lst.getEmergency(), "0.00"));
                    psTemp.setString("appointment", DmServer.vNull(lst.getAppointment(), "0.00"));
                    psTemp.setString("appointmentAdvance", DmServer.vNull(lst.getAppointmentAdvance(), "0.00"));
                    psTemp.setString("patient", DmServer.vNull(lst.getPatient(), "0"));
                    psTemp.setString("graceMinutes", DmServer.vNull(lst.getGraceMin(), "0"));
                    psTemp.setString("reVisit", DmServer.vNull(lst.getReVisit(), "0.00"));
                    psTemp.setDate("applicableFrom", DmServer.convertDate(mrTemp.getApplicableFrom()));
                    psTemp.setString("online", DmServer.vNull(lst.getOnlineCharge(), "0.00"));
                    psTemp.setString("onlinePatient", DmServer.vNull(lst.getOnlinePatient(), "0"));
                    psTemp.setString("payClinic", DmServer.vNull(lst.getPayClinic(), "0"));
                    psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
            spBegin.commit();
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

    private ServicePriceList_Mdl getTimeWisePriceList(GrdTiming_Mdl grdTiming, PriceListNature nature, String natureValue, String consultantCode, String applicableDate, String orgCode, String ServiceCode, String rateListCode) {
        ServicePriceList_Mdl priceList = new ServicePriceList_Mdl();
        priceList = new ServicePriceList_Mdl();
        priceList.setConsultantCode(consultantCode);
        priceList.setDate(applicableDate);
        priceList.setEntryFromNature("1");
        priceList.setRateListCode(DmShared.iNull(rateListCode).toString());
        priceList.setDayNameCode(DmShared.xNull(grdTiming.getSharedKeyFieldCode()));
        priceList.setOrganizationCode(orgCode);
        priceList.setPriceListEntryFrom(PriceListEntryFrom.CONSULTANT_MASTER);
        priceList.setPriceListNature(nature);
        priceList.setRate(natureValue);
        priceList.setServiceCode(DmShared.xNull(ServiceCode));
        priceList.setSiteCode(DmServer.getSsnSiteCode(this));
        priceList.setTime(DmServer.vNull(grdTiming.getFromTime(), "0.00"));
        priceList.setTimeTo(DmServer.vNull(grdTiming.getToTime(), "0.00"));
        return priceList;
    }

    @Override
    public String getTempKeyCode() {

        return UUID.randomUUID().toString();
    }

    @Override
    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String organization = "";

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            organization = DmServer.getCodeViaManualCode("CP", "shortName", "organizationmast", "subCode", cnTemp);
            if (DmShared.xNull(organization).trim().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid 'CP' Code. Create 'CP' code.");
            }

            sqlWithParam.setSQL(" SELECT cm.subCode, sg.name, sg.initial, sg.name AS ConsName , sg.manualCode,  "
                    + " spec.name AS specility, sdm.name AS department, "
                    + " sg.mobile,sg.eMail, sg2.dob, sg2.doa, cm.OPDValidity,  "
                    + " CASE sg.isActive  "
                    + "  WHEN 0 THEN 'No'  "
                    + "  WHEN 1 THEN 'Yes'   "
                    + "  ELSE 'No' "
                    + " END as isActive , "
                    + "   cm.mciReg, sg.remark,pspl.applicableFrom   "
                    + " FROM consultantMast cm  "
                    + "  LEFT JOIN subgroup sg ON cm.subCode = sg.subcode  "
                    + "  LEFT JOIN subgroup2 sg2 ON cm.subCode = sg2.subcode  "
                    + "  LEFT JOIN consultantspecimast spec ON cm.consultantSpeciCode = spec.code  "
                    + "  LEFT JOIN subgroup sdm ON sg2.departmentCode = sdm.subcode  "
                    + "  LEFT JOIN (SELECT Max(consultantCode) as consultantCode, MAX(applicableFrom) AS applicableFrom  "
                    + "  FROM patientservicepricelist  "
                    + "  WHERE organizationCode = :organizationCode AND EntryFrom = :entryFrom  "
                    + "  AND site_code = :site_Code   AND applicableFrom <= FORMAT(:uEntDt,'yyyy/MM/dd') "
                    + "  ) PSPL ON cm.subCode = pspl.consultantCode "
                    + " where sg.filterNature = :filterNature ");
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("organizationCode", organization);
            sqlWithParam.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            sqlWithParam.setString("entryFrom", String.valueOf(PriceListEntryFrom.CONSULTANT_MASTER.toInt()).trim());
            sqlWithParam.setString("filterNature", DmShared.xNull(DmServer.getCodeViaManualCode("CN", "manualCode", "subgroupNature", "Code", cnTemp)));
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("ConsName")));
                listNode.setDepartment(DmShared.xNull(rsTemp.getString("department")));
                listNode.setOpdValidUpto(DmServer.vNull(rsTemp.getString("OPDValidity"), "0"));
                listNode.setIsActive(DmShared.xNull(rsTemp.getString("isActive")));
                listNode.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                listNode.setShortName(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setSpecialization(DmShared.xNull(rsTemp.getString("specility")));
                listNode.setAnnivDate(DmShared.xNull(rsTemp.getString("doa")));
                listNode.setDob(DmShared.xNull(rsTemp.getString("dob")));
                listNode.setMciRegNo(DmShared.xNull(rsTemp.getString("mciReg")));
                listNode.setEmail(DmShared.xNull(rsTemp.getString("eMail")));
                listNode.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT sdm.subCODE, sdm.NAME "
                    + " FROM departmentMast dm "
                    + " left join subgroup sdm on dm.subcode = sdm.subcode "
                    + " Left join subGroupSiteList as sgsl on sdm.subCode=sgsl.subCode "
                    + " WHERE dm.nature in ('Hospital','Common') and sdm.isActive = 1 and  sgsl.site_Code=:siteCode");
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("NAME")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSpecialization(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT csm.code, csm.name, csm.guiTemplateCodePost, gtm.name AS guiTemplate "
                    + "FROM consultantSpeciMast AS csm "
                    + "  LEFT JOIN guiTemplateMast AS gtm ON gtm.code = csm.guiTemplateCodePost ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("guiTemplateCodePost")));
                listNode.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("guiTemplate")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHNature(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT code,Name FROM subgroupNature WHERE manualCode IN ('CN','RC')  ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHActive(DmHelper helper, boolean blnToEndOfFile) {
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
    public DmResultPageing<List<TxtHCity_Mdl>> getTxtHCity(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHCity_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHCity_Mdl> lstTemp = new ArrayList<>();
        TxtHCity_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL(" select	c.cityCode, c.cityName, sm.name as state  "
                    + "		from city as c "
                    + "		left join stateMast as sm on c.stateCode = sm.code");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHCity_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CityCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("CityName")));
                listNode.setStateName(DmShared.xNull(rsTemp.getString("state")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHWeek(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        try {
            for (DmWeekdays weekdays : DmWeekdays.values()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.iNull(weekdays.getWeekDay()).toString());
                listNode.setSharedKeyFieldName(weekdays.getWeekDayName());
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
    public DmResult<List<String>> getSuggetionConsultant(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<String>> rstTemp = new DmResultPageing<>();
        List<String> data = new ArrayList<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(data);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT sg.name FROM consultantmast cm "
                    + " LEFT JOIN subgroup sg ON cm.subCode = sg.subcode ");
            helper.addSort("name", DmSortType.ASC);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                data.add(DmShared.xNull(rsTemp.getString("name")));
            }
            helper.positioning(data.size(), blnToEndOfFile);
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
    public DmResultPageing<List<TxtHService_Mdl>> getTxtHService(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHService_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHService_Mdl> lstTemp = new ArrayList<>();
        TxtHService_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT psm.code, psm.name, psm.shortName  "
                    + " FROM patientServiceMast psm "
                    + " left join patientServiceSiteList as pssl on pssl.code=psm.code "
                    + " WHERE psm.serviceNature = '" + ServiceNature.OPERATION.getId() + "' AND psm.isActive = 1 "
                    + " and pssl.site_Code= :siteCode");
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHService_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setShortName(DmShared.xNull(rsTemp.getString("shortName")));
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
    public DmResult<DefaultValue_Mdl> getDefaultValue() {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult<>();
        DefaultValue_Mdl listNode = new DefaultValue_Mdl();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        GrdRoomCategory_Mdl categoryNode;
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "SELECT rcm.code AS roomCatCode, rcm.name, '0.00' AS rate  "
                    + "FROM roomcatmast rcm  "
                    + "ORDER BY rcm.name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                categoryNode = new GrdRoomCategory_Mdl();
                categoryNode.setAmount(rsTemp.getString("rate"));
                categoryNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("roomCatCode")));
                categoryNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.getCategory().add(categoryNode);
            }

            psTemp.close();
            rsTemp.close();

            sqlQuery = "select  subCode, rateListCode "
                    + "		from organizationMast om "
                    + "		where om.shortName = 'CP'";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                listNode.setOrganization(DmShared.xNull(rsTemp.getString("subCode")));
                listNode.setRateListCode(DmShared.xNull(rsTemp.getString("rateListCode")));
            } else {

                throw new UnsupportedOperationException("Invalid 'CP' Code. Create 'CP' code.");
            }
            rsTemp.close();
            psTemp.close();

//            listNode.setOrganization(DmShared.xNull(DmServer.getCodeViaManualCode("CP", "shortName", "organizationmast", "subCode", cnTemp)));
//            if (DmShared.xNull(listNode.getOrganization()).equalsIgnoreCase("")) {
//                throw new UnsupportedOperationException("Invalid 'CP' Code. Create 'CP' code.");
//            }
            listNode.setService(DmShared.xNull(DmServer.getCodeViaManualCode("CF", "shortName", "patientservicemast", "code", cnTemp)));
            if (DmShared.xNull(listNode.getService()).equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid 'CF' code. Create 'CF' Code");
            }
            listNode.setSiteCode(DmServer.getSsnSiteCode(this));

            dmrTemp.setData(listNode);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHInitial(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT name AS code, name FROM initialmast where name = 'Dr.'");
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
    public DmResult<ConsultantMaster_Mdl> getApplicableRate(String strKey, String applicableFrom, String siteCode) {
        DmResult<ConsultantMaster_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, organization, previousServiceCode = "";
        ConsultantMaster_Mdl mrTemp = new ConsultantMaster_Mdl();
        GrdTiming_Mdl timing;
        List<GrdService_Mdl> lstService = new ArrayList<GrdService_Mdl>();
        GrdService_Mdl service = new GrdService_Mdl();
        GrdRoomCategory_Mdl roomCat = new GrdRoomCategory_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            organization = DmServer.getCodeViaManualCode("CP", "shortName", "organizationmast", "subCode", cnTemp);
            if (DmShared.iNull(siteCode).intValue() == 0) {
                siteCode = DmServer.getSsnSiteCode(this);
            }
            sqlQuery = "SELECT ctd.dayNameCode, ctd.fromTime, ctd.toTime, ctd.emergency, ctd.appointment, ctd.patient, ctd.graceMinutes, "
                    + " ctd.reVisit,  ctd.online, ctd.onlinePatient, ctd.opd, ctd.appointmentAdvance, ctd.payClinic "
                    + " FROM consultantTimeDetail AS ctd    "
                    + " WHERE (ctd.consultantCode = :code ) AND (ctd.applicableFrom = :applicableFrom ) AND (site_code = :siteCode )"
                    + " ORDER BY ctd.sNo ;";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            psTemp.setString("siteCode", siteCode);
            psTemp.setDate("applicableFrom", DmServer.convertDate(applicableFrom));
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                timing = new GrdTiming_Mdl();
                timing.setEmergency(DmServer.vNull(rsTemp.getString("emergency"), "0"));
                timing.setFromTime(DmServer.vNull(rsTemp.getString("fromTime"), "00.00"));
                timing.setGraceMin(DmServer.vNull(rsTemp.getString("graceMinutes"), "0"));
                timing.setOpd(DmServer.vNull(rsTemp.getString("opd"), "0"));
                timing.setAppointment(DmServer.vNull(rsTemp.getString("appointment"), "0"));
                timing.setAppointmentAdvance(DmServer.vNull(rsTemp.getString("appointmentAdvance"), "0"));
                timing.setPatient(DmServer.vNull(rsTemp.getString("patient"), "0"));
                timing.setReVisit(DmServer.vNull(rsTemp.getString("reVisit"), "0"));
                timing.setToTime(DmServer.vNull(rsTemp.getString("toTime"), "00.00"));
                timing.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("dayNameCode")));
                timing.setSharedKeyFieldName(DmWeekdays.getValueFrom(DmShared.iNull(rsTemp.getString("dayNameCode")).intValue()).getWeekDayName());
                timing.setOnlineCharge(DmServer.vNull(rsTemp.getString("online"), "0.00"));
                timing.setOnlinePatient(DmServer.vNull(rsTemp.getString("onlinePatient"), "0"));
                timing.setPayClinic(DmServer.vNull(rsTemp.getString("payClinic"), "0"));

                mrTemp.getGrdTime().add(timing);
            }
            psTemp.close();
            rsTemp.close();

            sqlQuery = "SELECT MAX(pspl.serviceCode) AS serviceCode, MAX(psm.name) AS service, MAX(pspl.rate) AS rate,"
                    + " pspl.roomCatCode, MAX(rcm.name) AS roomCat "
                    + " FROM patientservicepricelist pspl  "
                    + " LEFT JOIN patientservicemast psm ON pspl.serviceCode = psm.code  "
                    + " LEFT JOIN roomcatmast rcm ON pspl.roomCatCode = rcm.code  "
                    + " WHERE pspl.site_code = :siteCode AND pspl.organizationCode = :organizationCode  "
                    + " AND pspl.EntryFrom = :entryFrom AND pspl.consultantCode = :code  "
                    + " AND pspl.entryFromNature = '0' AND pspl.applicableFrom =:applicableFrom  "
                    + " GROUP BY pspl.serviceCode, pspl.roomCatCode  "
                    + " ORDER BY  MAX(psm.name),MAX(rcm.name) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            psTemp.setString("organizationCode", organization);
            psTemp.setString("siteCode", siteCode);
            psTemp.setString("entryFrom", String.valueOf(PriceListEntryFrom.CONSULTANT_MASTER.toInt()).trim());
            psTemp.setDate("applicableFrom", DmServer.convertDate(applicableFrom));
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                if (!DmShared.xNull(rsTemp.getString("serviceCode")).equalsIgnoreCase(previousServiceCode)) {
                    service = new GrdService_Mdl();
                    lstService.add(service);
                }

                service.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("serviceCode")));
                service.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("service")));

                if (DmShared.xNull(rsTemp.getString("roomCatCode")).trim().equalsIgnoreCase("")) {
                    service.setAmount(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                } else {
                    roomCat = new GrdRoomCategory_Mdl();
                    roomCat.setAmount(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                    roomCat.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("roomCatCode")));
                    roomCat.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("roomCat")));
                    service.getLstRoomCategory().add(roomCat);
                }
                previousServiceCode = DmShared.xNull(rsTemp.getString("serviceCode"));
            }
            psTemp.close();
            rsTemp.close();
            mrTemp.getGrdService().clear();
            mrTemp.getGrdService().addAll(lstService);

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

    @Override
    public DmResult<String> deleteApplicableRecord(String strKey, String applicableFrom) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "Delete From consultanttimedetail Where consultantCode = :code AND applicableFrom = :applicableFrom";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            psTemp.setDate("applicableFrom", DmServer.convertDate(applicableFrom));
            psTemp.executeUpdate();

            spBegin.commit();
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
    public DmResult<List<String>> getSuggetionShortName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<String>> rstTemp = new DmResultPageing<>();
        List<String> data = new ArrayList<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(data);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT sg.manualCode as shortName FROM consultantmast cm "
                    + " LEFT JOIN subgroup sg ON cm.subCode = sg.subcode ");
            helper.addSort("shortName", DmSortType.ASC);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                data.add(DmShared.xNull(rsTemp.getString("shortName")));
            }
            helper.positioning(data.size(), blnToEndOfFile);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHLocation(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("select  rm.code, rm.roomNo "
                    + "		from roomMast rm "
                    + "		left join roomCatMast rcm on rcm.code = rm.roomCategoryCode "
                    + "		where rm.isActive = 1 and rcm.roomType = '" + RoomType.CONSULTATION.getValue() + "'");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("roomNo")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHConsultantNature(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Consultant");
            lstTemp.add(listNode);

            //2 for Referred
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("3");
            listNode.setSharedKeyFieldName("Anaesthetist");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("4");
            listNode.setSharedKeyFieldName("Pathologist");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("5");
            listNode.setSharedKeyFieldName("Radiologist");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHTDSCategory(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT CODE,NAME FROM TdsCat ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("NAME")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHIsOPDConsultant(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("No");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Yes");
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
    public DmResult<List<GrdSiteDetail_Mdl>> getSites() {
        DmResult<List<GrdSiteDetail_Mdl>> rstTemp = new DmResult<>();
        List<GrdSiteDetail_Mdl> lstNodes = new ArrayList<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = " select code,siteDisplayName as siteName from siteMast ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                GrdSiteDetail_Mdl lstNode = new GrdSiteDetail_Mdl();
                lstNode.setSiteName(DmShared.xNull(rsTemp.getString("siteName")));
                lstNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                lstNode.setIsOPDConsultant(false);
                lstNode.setAllowForSite(DmShared.iNull(DmServer.getSsnSiteCode(this)).intValue() == DmShared.iNull(lstNode.getSharedKeyFieldCode()).intValue());
                lstNodes.add(lstNode);
            }
            rstTemp.setData(lstNodes);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHLinkDept(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlWithParam.setSQL(" select sg.subCode as CODE, name as Name from  departmentMast dm left join subGroup sg on  sg.subCode = dm.subCode ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
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

}
