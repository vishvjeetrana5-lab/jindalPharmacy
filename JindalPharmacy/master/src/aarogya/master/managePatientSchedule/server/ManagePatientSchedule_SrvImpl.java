package aarogya.master.managePatientSchedule.server;

import aarogya.master.managePatientSchedule.shared.*;
import aarogya.projectMenu.self.shared.ProjectRuleCode;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.server.STServer;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ManagePatientSchedule_SrvImpl extends DmRSSPrivate implements ManagePatientSchedule_Srv {

    private ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private String regCat = "'" + pvc.OPD + "','" + pvc.EMERGENCY_OPD + "','" + pvc.EMERGENCY + "','" + pvc.IPD + "','" + pvc.DAY_CARE + "','" + pvc.PAY_CLINIC + "'";

    @Override

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("   select ps.docId, ps.recId, ps.uhid, psm.name as scheduleName, puh.patientName, ps.v_date,"
                    + "                  puh.mobile,  case when isstatus<>1 then 'Yes'   else 'No' end as active , "
                    + "                  puh.recId as patientRecId "
                    + "                 from patientSchedule ps "
                    + "                 left join patientScheduleMast psm on psm.code=ps.scheduleCode  "
                    + "                 left join patientUHID puh on puh.docId = ps.uhid  ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                listNode.setUhid(DmShared.xNull(rsTemp.getString("uhid")));
                listNode.setPatientName(DmShared.xNull(rsTemp.getString("patientName")));
                listNode.setScheduleName(DmShared.xNull(rsTemp.getString("scheduleName")));
                listNode.setPatientRecId(DmShared.xNull(rsTemp.getString("patientRecId")));
                listNode.setSchdlDate(DmShared.dNull(rsTemp.getDate("v_date")));
                listNode.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                listNode.setActive(DmShared.xNull(rsTemp.getString("active")));
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

    private PatientDetail_Mdl getPatientDetail(String regDocId, String uhidDocId, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        PatientDetail_Mdl mrTemp = new PatientDetail_Mdl();
        String sqlQuery;
        try {
            sqlQuery = "select  pu.docId as uhidDocId, pu.recId as uhidRecId,pu.guardianName, pu.patientName, pu2.genderCode, gen.name as gender, "
                    + "         pu.idNo, pu.dobDisplay, pu.v_No AS uhidCounter, pu.mobile, org.subCode as organizationCode, org.name as organization, "
                    + "         pr.regDocId, pr.recId as regNo, pr.category as regCategory, "
                    + "		case when isnull(pu.address1, '') = '' then '' else concat(pu.address1, "
                    + "		(case when isnull(pu.address1, '') = '' then '' else concat(', ', pu.address2, (case when pu.cityCode is null then '' "
                    + "			else concat(', ', ct.cityName, ', ', sm.name, ', ', co.name) end)) end)) end as patientAddress "
                    + "		from patientUHID pu "
                    + "		left join patientUHID2 pu2 on pu2.uhid = pu.docId "
                    + "		left join ("
                    + "			select  pr.regDocId, pr.recId, pr.uhid, vt.category, pr.organizationCode"
                    + "				from patientRegistration pr "
                    + "				left join voucher_Type vt on vt.v_Type = pr.v_Type "
                    + "				where pr.regDocId = :regDocId "
                    + "		) pr on pr.uhid = pu.docId "
                    + "		left join subGroup org on org.subCode = (case when pr.regDocId is null then pr.organizationCode else pu.organizationCode end)"
                    + "		left join hl7Mast gen on gen.code = pu2.genderCode "
                    + "		left join city ct on ct.cityCode = pu.cityCode "
                    + "		left join stateMast sm on sm.code = ct.stateCode "
                    + "		left join country co on co.code = ct.countryCode "
                    + "		where pu.docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(uhidDocId).toString());
            psTemp.setString("regDocId", DmShared.iNull(regDocId).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setDocIdUHID(DmShared.xNull(rsTemp.getString("uhidDocID")));
                mrTemp.setRecIdUHID(DmShared.xNull(rsTemp.getString("uhidRecID")));
                mrTemp.setDisplayDOB(DmShared.xNull(rsTemp.getString("dobDisplay")));
                mrTemp.setGender(DmShared.xNull(rsTemp.getString("gender")));
                mrTemp.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                mrTemp.setAddress(DmShared.xNull(rsTemp.getString("patientAddress")));
                mrTemp.setGuardian(DmShared.xNull(rsTemp.getString("guardianName")));
                mrTemp.setPatient(DmShared.xNull(rsTemp.getString("patientName")));
                mrTemp.setOrganizationText(DmShared.xNull(rsTemp.getString("organization")));
                mrTemp.setOrganizationTag(DmShared.xNull(rsTemp.getString("organizationCode")));
                mrTemp.setDocIdReg(DmShared.xNull(rsTemp.getString("regDocId")));
                mrTemp.setRecIdReg(DmShared.xNull(rsTemp.getString("regNo")));
                mrTemp.setRegCategory(DmShared.xNull(rsTemp.getString("regCategory")));
                /*====================================< Get multi DocId With Rule Code details >====================================*/
                mrTemp.setMultiDocIdWithRuleCode(STServer.getPatientDocIdsWithRuleCode(mrTemp.getDocIdUHID(), cnTemp));
            }
            psTemp.close();
            rsTemp.close();

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
        return mrTemp;
    }

    @Override
    public DmResult<ManagePatientSchedule_Mdl> getMoveRec(String regDocId, String recId, String schdlDoc, String uhidocId) {
        DmResult<ManagePatientSchedule_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ManagePatientSchedule_Mdl mrTemp = new ManagePatientSchedule_Mdl();
        PatientDetail_Mdl patntDtlMdl;
        String sqlQuery;

        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        dmrTemp.setData(mrTemp);
        String condition = "", condition2 = "";

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (DmShared.iNull(uhidocId).longValue() > 0) {
                condition = " and pu.docId = :docId";
            } else if (DmShared.iNull(schdlDoc).longValue() > 0) {
                condition = "  and ps.docId = :docIdSch ";
            } else if (!DmShared.xNull(recId).equalsIgnoreCase("")) {
                condition = " and pu.recId = :recId ";
            } else {
                condition = " 0=1 ";
            }

            if (DmShared.iNull(regDocId).longValue() > 0) {
                sqlQuery = "select  pu.docId, pr.regDocId, ps.uhid "
                        + "         from patientRegistration pr "
                        + "         left join voucher_Type vt on vt.v_Type = pr.v_Type "
                        + "         left join patientUHID pu on pu.docId = pr.uhid "
                        + "         left join patientSchedule ps on ps.uhid = pr.uhid "
                        + "         where pr.isCancelled = 0 and pr.site_Code = 1 and vt.category in (" + regCat + ") "
                        + "         and pu.isExpired = 0 and isnull(pu.approvedBy, '') <> '' and pu.isCancelled = 0 and pr.regDocId = :regDocId "
                        + "         order by pr.v_Date desc, pr.regTime, vt.short_Name, pr.v_No, pr.recId";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("regDocId", regDocId);
            } else {
                sqlQuery = "select  pu.docId, ps.uhid, 0 as regDocId  "
                        + "         from patientUHID pu  "
                        + "         left join voucher_Type vt on vt.v_type = pu.v_Type  "
                        + "         left join patientSchedule ps on ps.uhid = pu.docId  "
                        + "         where pu.isCancelled = 0 and vt.category in ('" + pvc.UHID + "') and  "
                        + "         pu.isExpired = 0 and isnull(pu.approvedBy, '') <> ''  " + condition;

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                if (DmShared.iNull(uhidocId).longValue() > 0) {
                    psTemp.setString("docId", uhidocId);
                } else if (DmShared.iNull(schdlDoc).longValue() > 0) {
                    psTemp.setString("docIdSch", schdlDoc);
                } else if (!DmShared.xNull(recId).equalsIgnoreCase("")) {
                    psTemp.setString("recId", recId);
                }
            }
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                uhidocId = DmShared.xNull(rsTemp.getString("docId"));
                regDocId = DmShared.xNull(rsTemp.getString("regDocId"));
            } else {
                throw new DmException(0, "Invalid UHID No.");
            }

            rsTemp.close();
            psTemp.close();

            //===========================================get Patient Details==================================================//
            patntDtlMdl = getPatientDetail(DmShared.xNull(regDocId), DmShared.xNull(uhidocId), cnTemp);
            dmrTemp.setStatus(true);
            mrTemp.setPatientDetailMdl(patntDtlMdl);
            if (DmShared.iNull(schdlDoc).longValue() != 0) {
                condition = "  where docId=:docId ";
                condition2 = " (select count (uhid) from  patientSchedule where docId=:docId ) count, ";
            } else if (DmShared.iNull(uhidocId).longValue() != 0) {
                condition = "  where uhid=:uhid ";
                condition2 = " (select count (uhid) from  patientSchedule  where uhid=:uhid ) count, ";
            }

            sqlQuery = "  select " + condition2 + " ps.docId, ps.recId as scheduleNo, ps.v_Date, ps.isStatus, ps.uhid, psm.Name as scheduleName "
                    + "         from  patientSchedule ps "
                    + "         left join  patientScheduleMast psm on psm.code=ps.scheduleCode " + condition;
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (DmShared.iNull(schdlDoc).longValue() != 0) {
                psTemp.setString("docId", schdlDoc);
            } else if (DmShared.iNull(mrTemp.getPatientDetailMdl().getDocIdUHID()).longValue() != 0) {
                psTemp.setString("uhid", DmShared.iNull(mrTemp.getPatientDetailMdl().getDocIdUHID()).toString());
            }

            rsTemp = psTemp.executeQuery();

            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("docId")).toString());
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("scheduleNo")));
                mrTemp.setEntryDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                mrTemp.setActive(DmShared.iNull(rsTemp.getString("isStatus")).toString());
                mrTemp.setUhid(DmShared.xNull(rsTemp.getString("uhid")));
                mrTemp.setScheduleHlp(DmShared.xNull(rsTemp.getString("scheduleName")));
                mrTemp.setShdlcount(DmShared.iNull(rsTemp.getString("count")).toString());

            }
            rsTemp.close();
            psTemp.close();
            //===========================================Fetch PatientScheduleDetail ==================================================//
            if (DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue() != 0) {
                //------------------------------ getPatientSchedule---------------------------------------------------
                mrTemp.getGrdPatnSchdlMdl().addAll(getSchedule(DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString(), cnTemp));
            }
            dmrTemp.setValue1(DmServer.getServerDateTime()[0]);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

    public List<GrdSchedules_Mdl> getSchedule(String schdlDocId, Connection cnTemp) {
        List<GrdSchedules_Mdl> lstNode = new ArrayList<GrdSchedules_Mdl>();
        GrdSchedules_Mdl node;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        Calendar cal;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyy - HH:mm");
        String sqlQuery;

        try {


            /*====================================< Get Layout List >====================================*/
            sqlQuery = "select  psd.docId,psd.v_Sno, psd.description, psd.scheduleDate, psd.cycleTime, psd.printSNo, ps.v_date, ps.recId, ps.isStatus, "
                    + "         psm.Name as scheduleName, ps.scheduleCode as scheduleCode, psd.machineCode, psd.guiConfigCode, gca.guiTemplateCode, "
                    + "         psd.consultantCode, rcm.name as machine, gtm.name as guiTemplate, sg.displayName as consultant, psd.doneDate, "
                    + "         psd.remark, psd.u_EntDt, psd.U_Name, rd.tokenNo, psd.rDiary_DocId, psd.rDiary_V_Sno, rd.roomCode, rm.roomNo, "
                    + "         rd.v_date as bookDate "
                    + "         from  PatientSchedule ps  "
                    + "         left join PatientScheduleDetail psd on ps.docId = psd.docId  "
                    + "         left join patientScheduleMast psm on psm.code=ps.scheduleCode"
                    + "         left join guiConfigApplicableTo gca on gca.code = psd.guiConfigCode  "
                    + "         left join guiTemplateMast gtm on gtm.code = gca.guiTemplateCode  "
                    + "         left join roomCatMast rcm on rcm.code = psd.machineCode   "
                    + "         left join consultantMast cm on cm.subcode = psd.consultantCode   "
                    + "         left join  subGroup sg on cm.subCode=sg.subCode"
                    + "         left join roomDiary rd on rd.docId = psd.rDiary_DocId and rd.v_Sno = psd.rDiary_V_Sno "
                    + "         left join roomMast rm on rd.roomCode = rm.code  "
                    + "         where psd.docId = :docId  "
                    + "         order by psd.scheduleDate, psd.printSNo  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (DmShared.iNull(schdlDocId).longValue() != 0) {
                psTemp.setString("docId", DmShared.iNull(schdlDocId).toString());
            }
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdSchedules_Mdl();
                node.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("docId")).toString());
                node.setCycleTime(DmShared.iNull(rsTemp.getString("cycleTime")).toString());
                node.setPrintSno((DmShared.iNull(rsTemp.getString("printSNo")).longValue() == 0 ? "" : DmShared.xNull(rsTemp.getString("printSNo"))));
                node.setvSno(DmShared.iNull(rsTemp.getString("v_Sno")).toString());
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("description")));
                node.setDescription(DmShared.xNull(rsTemp.getString("description")));
                node.setvDate(DmShared.dNull(rsTemp.getDate("v_date")));
                node.setSchdleNo(DmShared.xNull(rsTemp.getString("recId")));
                node.setActive(DmShared.iNull(rsTemp.getString("isStatus")).toString());
                node.setScheduleDate(DmShared.dNull(rsTemp.getDate("scheduleDate")));
                node.setScheduleName(DmShared.xNull(rsTemp.getString("scheduleName")));
                node.setScheduleCode(DmShared.iNull(rsTemp.getString("scheduleCode")).toString());
                node.setMachineCode(DmShared.iNull(rsTemp.getString("machineCode")).toString());
                node.setGuiTemplateCode(DmShared.iNull(rsTemp.getString("guiTemplateCode")).toString());
                node.setGuiConfigCode(DmShared.iNull(rsTemp.getString("guiConfigCode")).toString());
                node.setConsultantCode(DmShared.iNull(rsTemp.getString("consultantCode")).toString());
                node.setMachine(DmShared.xNull(rsTemp.getString("machine")));
                node.setConsultantName(DmShared.xNull(rsTemp.getString("consultant")));
                node.setGuiTemplate(DmShared.xNull(rsTemp.getString("guiTemplate")));
                node.setDoneDate(DmShared.dNull(rsTemp.getDate("doneDate")));
                node.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                node.setTokenNo(DmShared.xNull(rsTemp.getString("tokenNo")));
                node.setBookDate(DmShared.dNull(rsTemp.getDate("bookDate")));
                node.setDoneStatus(!DmShared.dNull(rsTemp.getDate("doneDate")).equalsIgnoreCase(""));
                node.setRoomDiaryDocId(DmShared.xNull(rsTemp.getString("rDiary_DocId")));
                node.setRoomDiaryVSno(DmShared.xNull(rsTemp.getString("rDiary_V_Sno")));
                node.setRoomCode(DmShared.xNull(rsTemp.getString("roomCode")));
                node.setRoomNo(DmShared.xNull(rsTemp.getString("roomNo")));
                node.setFileCollection(getFileCollection(schdlDocId, DmShared.xNull(rsTemp.getString("v_Sno")), cnTemp));

                if (rsTemp.getTimestamp("u_EntDt") != null) {
                    cal = Calendar.getInstance();
                    cal.setTime(new Date(rsTemp.getTimestamp("u_EntDt").getTime()));
                    sdf.format(cal.getTime());
                    node.setEntryDateTime(DmShared.xNull(rsTemp.getString("u_Name")) + " - " + sdf.format(cal.getTime()));
                }
                lstNode.add(node);

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
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return lstNode;
    }

    @Override
    public DmResult<String> changeSchedules(ShuffleDays_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData(mrTemp.getDocId());
        Calendar cal;
        boolean isUpdate = true;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "select  scheduleDate, doneDate, v_Sno "
                    + "         from  PatientScheduleDetail "
                    + "         where docId = :docId "
                    + "         order by scheduleDate, printSno ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", mrTemp.getDocId());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                isUpdate = true;
                java.util.Date queryDate = DmShared.convertDate(DmShared.dNull(rsTemp.getDate("scheduleDate")));
                if (queryDate != null) {
                    if (queryDate.getTime() >= DmShared.convertDate(mrTemp.getSchduleDate()).getTime() && isUpdate) {

                        isUpdate = DmShared.dNull(rsTemp.getDate("doneDate")).equalsIgnoreCase("");
                        if (isUpdate) {
                            cal = Calendar.getInstance();
                            cal.setTime(queryDate);
                            cal.add(Calendar.DATE, mrTemp.getShuffleDays());
                            sqlQuery = "update  PatientScheduleDetail set "
                                    + " scheduleDate = :scheduleDate "
                                    + " where docId = :docId and v_Sno = :v_Sno ";
                            psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp1.setString("docId", mrTemp.getDocId());
                            psTemp1.setString("v_Sno", rsTemp.getString("v_Sno"));
                            psTemp1.setDate("scheduleDate", DmServer.convertDate(DmShared.dNull(cal.getTime())));
                            psTemp1.executeUpdate();
                            psTemp1.close();
                        }
                    }
                }
            }
            rsTemp.close();
            psTemp.close();

            dmrTemp.setData(DmShared.iNull(mrTemp.getDocId()).toString());
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
                if (psTemp1 != null) {
                    psTemp1.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

    private void registrationCheck(String uhidDoc, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        try {
            sqlQuery = "            Select  pu.approvedBy, pu.isExpired, pu.isCancelled as uhidIsCancelled"
                    + "                         from patientUHID pu "
                    + "                         where pu.docId =:uhidDoc ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("uhidDoc", DmShared.iNull(uhidDoc).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("uhidIsCancelled")).intValue() != 0) {
                    throw new DmException(0, "UHID discarded. You can not modify this record.");
                }

                if (DmShared.iNull(rsTemp.getString("isExpired")).intValue() == 1) {
                    throw new DmException(0, "Patient has been expired. You can not modify this record.");
                }
                if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                    throw new DmException(0, "Patient is not Approved. You can not modify this record.");
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
    }

    private String getFileCollection(String regDocId, String vSNo, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        String sqlQuery, file = "";
        ResultSet rsTemp = null;
        String ruleCode = ProjectRuleCode.PATIENT_SCHEDULE_FOLLOW.getRuleCode();
        try {
            sqlQuery = "SELECT CONCAT(fileType, ' ', '(', COUNT(docId), ')') AS fileCollection "
                    + "FROM linkFileCollection "
                    + "WHERE (docId = :docId) AND (RuleCode = '" + ruleCode + "') AND sNo=:sNo "
                    + "GROUP BY fileType ORDER BY MAX(u_EntDt)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(regDocId).toString());
            psTemp.setString("sNo", DmShared.xNull(vSNo));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                file = (DmShared.xNull(rsTemp.getString("fileCollection")));
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
        return file;
    }

    @Override
    public DmResult<GrdSchedules_Mdl> saveSchedule(GrdSchedules_Mdl mrTemp, String uhid) {
        DmResult<GrdSchedules_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData(null);
        boolean blnAdd = false;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            registrationCheck(uhid, cnTemp);

            blnAdd = DmShared.iNull(mrTemp.getvSno()).longValue() < 1;

            if (blnAdd) {
                sqlQuery = "insert into patientScheduleDetail (docId, v_SNo, description, scheduleDate, remark, printSNo, cycleTime, guiConfigCode, machineCode, consultantCode, "
                        + "         u_Name, u_EntDt)"
                        + " select  :docId, isnull(max(v_Sno),0)+1 , :description, :scheduleDate, :remark, :printSNo, :cycleTime, :guiConfigCode, :machineCode, :consultantCode, :userName, :uEntDt "
                        + "		from patientScheduleDetail "
                        + "		where docId = :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("description", DmShared.xNull(mrTemp.getDescription()));
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setString("cycleTime", DmShared.iNull(mrTemp.getCycleTime()).toString());
                psTemp.setString("printSNo", DmShared.iNull(mrTemp.getPrintSno()).toString());
                psTemp.setString("guiConfigCode", DmShared.foriegnKey(mrTemp.getGuiConfigCode()));
                psTemp.setString("machineCode", DmShared.foriegnKey(mrTemp.getMachineCode()));
                psTemp.setString("consultantCode", DmShared.foriegnKey(mrTemp.getConsultantCode()));
                psTemp.setString("userName", DmServer.getSsnUserName(this));
                psTemp.setDate("scheduleDate", DmServer.convertDate(mrTemp.getScheduleDate()));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            } else {
                sqlQuery = "select  rDiary_DocId "
                        + "         from patientScheduleDetail "
                        + "         where docId = :docId and v_Sno = :v_Sno ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("v_Sno", DmShared.iNull(mrTemp.getvSno()).toString());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (DmShared.iNull(rsTemp.getString("rDiary_DocId")).longValue() > 0) {
                        throw new DmException(0, "Schedule already booked.");
                    }
                }
                rsTemp.close();
                psTemp.close();

                sqlQuery = "update  PatientScheduleDetail set "
                        + " description = :description, "
                        + " remark = :remark, "
                        + " cycleTime = :cycleTime, "
                        + " printSno = :printSno, "
                        + " scheduleDate = :scheduleDate,"
                        + " guiConfigCode=:guiConfigCode, "
                        + " machineCode=:machineCode, "
                        + " consultantCode=:consultantCode, "
                        + " u_Name = :userName, "
                        + " u_EntDt = :uEntDt "
                        + " where docId = :docId and v_Sno = :vSno";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("vSno", DmShared.iNull(mrTemp.getvSno()).toString());
                psTemp.setString("description", DmShared.xNull(mrTemp.getDescription()));
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setString("cycleTime", DmShared.xNull(mrTemp.getCycleTime()));
                psTemp.setString("printSNo", DmShared.iNull(mrTemp.getPrintSno()).toString());
                psTemp.setString("guiConfigCode", DmShared.foriegnKey(mrTemp.getGuiConfigCode()));
                psTemp.setString("machineCode", DmShared.foriegnKey(mrTemp.getMachineCode()));
                psTemp.setString("consultantCode", DmShared.foriegnKey(mrTemp.getConsultantCode()));
                psTemp.setString("userName", DmServer.getSsnUserName(this));
                psTemp.setDate("scheduleDate", DmServer.convertDate(mrTemp.getScheduleDate()));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            }
            psTemp.executeUpdate();
            psTemp.close();

            dmrTemp.setData(mrTemp);
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
    public DmResult<KeyFieldExtended> deleteRecord(String uhidDoc, String shdlDocId) {
        DmResult<KeyFieldExtended> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        KeyFieldExtended mrTemp = new KeyFieldExtended();
        dmrTemp.setData(mrTemp);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            registrationCheck(uhidDoc, cnTemp);
            /*====================================< Update patientService >==========================================*/
            sqlQuery = "Update patientSchedule set isStatus = 1 Where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(shdlDocId).toString());
            psTemp.executeUpdate();
            psTemp.close();

            mrTemp.setSharedKeyFieldCode(shdlDocId);
            mrTemp.setSharedKeyFieldName(uhidDoc);
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
    public DmResult<String> deleteDoneDate(String docIdUHID, String docId, String v_SNo) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData(docId);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "select  doneDate "
                    + "         from  PatientScheduleDetail "
                    + "         where docId = :docId and v_Sno = :vSno ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            psTemp.setString("vSno", DmShared.iNull(v_SNo).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {

                if (!DmShared.dNull(rsTemp.getDate("doneDate")).equalsIgnoreCase("")) {
                    throw new DmException(0, "Schedule already Done.");
                }
            }
            rsTemp.close();
            psTemp.close();

            registrationCheck(docIdUHID, cnTemp);
            sqlQuery = "delete from  PatientScheduleDetail where docId = :docId and v_Sno = :vSno ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            psTemp.setString("vSno", DmShared.iNull(v_SNo).toString());
            psTemp.executeUpdate();
            psTemp.close();

            dmrTemp.setData(DmShared.xNull(docId));
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
                if (psTemp1 != null) {
                    psTemp1.close();
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHGuiTemplate(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL(" Select gtm.name as Name , gtm.code as templateCode , gcf.code as configCode "
                    + "                 from guiTemplateMast gtm "
                    + "                 left join guiConfigApplicableTo gca on gca.guiTemplateCode = gtm.code "
                    + "                 left join guiConfig gcf on gcf.code=gca.code where gca.site_code =:siteCode ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("configCode")));
                listNode.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("templateCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                lstTemp.add(listNode);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHMachineBed(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("Select name as name, code from roomCatMast ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHConsultant(DmHelper helper, boolean blnToEndOfFile, String doneDate) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("select  cm.subCode, sg.name "
                    + "		from consultantMast cm "
                    + "		left join subGroup sg on sg.subCode = cm.subCode "
                    + "		left join subGroupNature sgn on sgn.code = sg.filterNature "
                    + "		left join subGroupSiteList sgsl on sgsl.subCode = sg.subCode "
                    + "		left join ("
                    + "			select  clm.consultantcode "
                    + "				from consultantLeaveMast clm "
                    + "				where cast(:uEntDt as date) between clm.fromDate and clm.toDate "
                    + "				group by clm.consultantcode "
                    + "		) clm on clm.consultantcode = sg.subCode "
                    + "		where sg.isActive = 1 and sgn.manualCode in ('CN') and sgsl.site_Code = :siteCode "
                    + "			and clm.consultantcode is null ");
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            sqlWithParam.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rstTemp.setData(lstTemp);
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
    public DmResultPageing<List<TxtHSchedule_Mdl>> getTxtHSchedule(String uhidDoc, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHSchedule_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHSchedule_Mdl> lstTemp = new ArrayList<>();
        TxtHSchedule_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            if (DmShared.iNull(uhidDoc).longValue() < 1) {

                throw new DmException(0, "Please select UHID No.");
            }

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("       select  ps.docId as code, ps.recId, psm.manualCode as sName, ps.v_Date , psm.name, psgm.name as groupName  "
                    + "                 from patientSchedule ps  "
                    + "                 left join  patientScheduleMast psm on psm.code = ps.scheduleCode"
                    + "                 left join  patientScheduleGroupMast psgm on psgm.code = psm.scheduleGroupCode  "
                    + "                  where ps.uhid = :uhid and ps.isStatus<>1 ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            psTemp.setString("uhid", DmShared.iNull(uhidDoc).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHSchedule_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("code")).toString());
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                listNode.setScheduleManual(DmShared.xNull(rsTemp.getString("sName")));
                listNode.setVDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setGroup(DmShared.xNull(rsTemp.getString("groupName")));
                listNode.setScheduleName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
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
    public DmResult<List<GrdSchedules_Mdl>> getScheduleGrid(String schdlDocId) {
        DmResult<List<GrdSchedules_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            dmrTemp.setData(getSchedule(schdlDocId, cnTemp));
            dmrTemp.setStatus(true);
            cnTemp.close();
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {

                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

}
