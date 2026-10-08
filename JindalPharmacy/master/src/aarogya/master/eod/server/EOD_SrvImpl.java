package aarogya.master.eod.server;

import aarogya.master.eod.shared.*;
import aarogya.selfToolkit.stStatic.shared.STEnum.ProcessEOD;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmSQLWithParam;
import dataman.dmBase.self.shared.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EOD_SrvImpl extends DmRSSPrivate implements EOD_Srv {

    private RoomPosting_Server roomPosting_Server = new RoomPosting_Server();
    private LabConsumption_Server labConsumption_Server = new LabConsumption_Server();
    private OPDAcPosting_Server opdAcPosting_Server = new OPDAcPosting_Server();
    private DayCareAcPosting_Server dayCareAcPosting_Server = new DayCareAcPosting_Server();
    private IPDAcPosting_Server ipdAcPosting_Server = new IPDAcPosting_Server();
    private EmergencyAcPosting_Server emergencyAcPosting_Server = new EmergencyAcPosting_Server();
    private DiagnosticAcPosting_Server diagnosticAcPosting_Server = new DiagnosticAcPosting_Server();
    private PharmacyAcPosting_Server pharmacyAcPosting_Server = new PharmacyAcPosting_Server();
    private MISAppUpdation_Server misAppPush_Server = new MISAppUpdation_Server();

    @Override
    public DmResult<List<GrdProcessDetail_Mdl>> getEOD(UserPermission_Mdl userPermissionMdl) {
        DmResult<List<GrdProcessDetail_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        List<GrdProcessDetail_Mdl> listNode = new ArrayList<GrdProcessDetail_Mdl>();
        GrdProcessDetail_Mdl node;
        dmrTemp.setData(listNode);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (userPermissionMdl.isDayCareAcPosting()) {
                node = new GrdProcessDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.iNull(ProcessEOD.DAY_CARE_AC_POSTING.getId()).toString());
                node.setSharedKeyFieldName(DmShared.xNull(ProcessEOD.DAY_CARE_AC_POSTING.getValue()).trim());
                getLastRunDetail(node, cnTemp);
                listNode.add(node);
            }

            if (userPermissionMdl.isDiagnosticAcPosting()) {
                node = new GrdProcessDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.iNull(ProcessEOD.DIAGNOSTIC_AC_POSTING.getId()).toString());
                node.setSharedKeyFieldName(DmShared.xNull(ProcessEOD.DIAGNOSTIC_AC_POSTING.getValue()).trim());
                getLastRunDetail(node, cnTemp);
                listNode.add(node);
            }

            if (userPermissionMdl.isEmergencyAcPosting()) {
                node = new GrdProcessDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.iNull(ProcessEOD.EMERGENCY_AC_POSTING.getId()).toString());
                node.setSharedKeyFieldName(DmShared.xNull(ProcessEOD.EMERGENCY_AC_POSTING.getValue()).trim());
                getLastRunDetail(node, cnTemp);
                listNode.add(node);
            }

            if (userPermissionMdl.isIpdAcPosting()) {
                node = new GrdProcessDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.iNull(ProcessEOD.IPD_AC_POSTING.getId()).toString());
                node.setSharedKeyFieldName(DmShared.xNull(ProcessEOD.IPD_AC_POSTING.getValue()).trim());
                getLastRunDetail(node, cnTemp);
                listNode.add(node);
            }

            if (userPermissionMdl.isLabConsumption()) {
                node = new GrdProcessDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.iNull(ProcessEOD.LAB_CONSUMPTION.getId()).toString());
                node.setSharedKeyFieldName(DmShared.xNull(ProcessEOD.LAB_CONSUMPTION.getValue()).trim());
                getLastRunDetail(node, cnTemp);
                listNode.add(node);
            }

            if (userPermissionMdl.isOpdAcPosting()) {
                node = new GrdProcessDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.iNull(ProcessEOD.OPD_AC_POSTING.getId()).toString());
                node.setSharedKeyFieldName(DmShared.xNull(ProcessEOD.OPD_AC_POSTING.getValue()).trim());
                getLastRunDetail(node, cnTemp);
                listNode.add(node);
            }

            if (userPermissionMdl.isPharmacyAcPosting()) {
                node = new GrdProcessDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.iNull(ProcessEOD.PHARMACY_AC_POSTING.getId()).toString());
                node.setSharedKeyFieldName(DmShared.xNull(ProcessEOD.PHARMACY_AC_POSTING.getValue()).trim());
                getLastRunDetail(node, cnTemp);
                listNode.add(node);
            }

            if (userPermissionMdl.isRoomPosting()) {
                node = new GrdProcessDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.iNull(ProcessEOD.ROOM_POSTING.getId()).toString());
                node.setSharedKeyFieldName(DmShared.xNull(ProcessEOD.ROOM_POSTING.getValue()).trim());
                getLastRunDetail(node, cnTemp);
                listNode.add(node);
            }

            if (userPermissionMdl.isMisAppPush()) {
                node = new GrdProcessDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.iNull(ProcessEOD.MIS_APP_UPDATION.getId()).toString());
                node.setSharedKeyFieldName(DmShared.xNull(ProcessEOD.MIS_APP_UPDATION.getValue()).trim());
                getLastRunDetail(node, cnTemp);
                listNode.add(node);
            }

            dmrTemp.setValue1(DmServer.getServerDateTime()[0]);
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

    private void getLastRunDetail(GrdProcessDetail_Mdl node, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {
            /*====================================< Get last run >====================================*/
            sqlQuery = "Select	remark, format(u_EntDt,'dd-MMM-yyyy / hh.mm tt') as lastRun, u_Name "
                    + "		from eodHistory "
                    + "		where processCode = :processCode and site_Code = :site_Code "
                    + "		order by u_EntDt desc ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("processCode", DmShared.iNull(node.getSharedKeyFieldCode()).toString());
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                node.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                node.setLastRun(DmShared.xNull(rsTemp.getString("lastRun")));
                node.setUser(DmShared.xNull(rsTemp.getString("u_Name")));
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

    @Override
    public DmResultPageing<List<GrdProcessDetail_Mdl>> getEODHistory(UserPermission_Mdl userPermissionMdl, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<GrdProcessDetail_Mdl>> rstTemp = new DmResultPageing<>();
        List<GrdProcessDetail_Mdl> lstTemp = new ArrayList<GrdProcessDetail_Mdl>();
        GrdProcessDetail_Mdl node;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery, condition = "";
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (userPermissionMdl.isDayCareAcPosting()) {
                condition = "'" + ProcessEOD.DAY_CARE_AC_POSTING.getId() + "'";
            }
            if (userPermissionMdl.isDiagnosticAcPosting()) {
                if (condition.trim().equalsIgnoreCase("")) {
                    condition += "'" + ProcessEOD.DIAGNOSTIC_AC_POSTING.getId() + "'";
                } else {
                    condition += ",'" + ProcessEOD.DIAGNOSTIC_AC_POSTING.getId() + "'";
                }
            }
            if (userPermissionMdl.isEmergencyAcPosting()) {
                if (condition.trim().equalsIgnoreCase("")) {
                    condition += "'" + ProcessEOD.EMERGENCY_AC_POSTING.getId() + "'";
                } else {
                    condition += ",'" + ProcessEOD.EMERGENCY_AC_POSTING.getId() + "'";
                }
            }
            if (userPermissionMdl.isIpdAcPosting()) {
                if (condition.trim().equalsIgnoreCase("")) {
                    condition += "'" + ProcessEOD.IPD_AC_POSTING.getId() + "'";
                } else {
                    condition += ",'" + ProcessEOD.IPD_AC_POSTING.getId() + "'";
                }
            }
            if (userPermissionMdl.isLabConsumption()) {
                if (condition.trim().equalsIgnoreCase("")) {
                    condition += "'" + ProcessEOD.LAB_CONSUMPTION.getId() + "'";
                } else {
                    condition += ",'" + ProcessEOD.LAB_CONSUMPTION.getId() + "'";
                }
            }
            if (userPermissionMdl.isOpdAcPosting()) {
                if (condition.trim().equalsIgnoreCase("")) {
                    condition += "'" + ProcessEOD.OPD_AC_POSTING.getId() + "'";
                } else {
                    condition += ",'" + ProcessEOD.OPD_AC_POSTING.getId() + "'";
                }
            }
            if (userPermissionMdl.isPharmacyAcPosting()) {
                if (condition.trim().equalsIgnoreCase("")) {
                    condition += "'" + ProcessEOD.PHARMACY_AC_POSTING.getId() + "'";
                } else {
                    condition += ",'" + ProcessEOD.PHARMACY_AC_POSTING.getId() + "'";
                }
            }
            if (userPermissionMdl.isRoomPosting()) {
                if (condition.trim().equalsIgnoreCase("")) {
                    condition += "'" + ProcessEOD.ROOM_POSTING.getId() + "'";
                } else {
                    condition += ",'" + ProcessEOD.ROOM_POSTING.getId() + "'";
                }
            }
            if (userPermissionMdl.isMisAppPush()) {
                if (condition.trim().equalsIgnoreCase("")) {
                    condition += "'" + ProcessEOD.MIS_APP_UPDATION.getId() + "'";
                } else {
                    condition += ",'" + ProcessEOD.MIS_APP_UPDATION.getId() + "'";
                }
            }

            if (!condition.trim().equalsIgnoreCase("")) {
                condition = " and processCode in (" + condition + ") ";
            }

            sqlQuery = "Select	processCode, eodDate, remark, format(u_EntDt,'dd-MMM-yyyy / hh.mm tt') as lastRun, u_Name, u_EntDt "
                    + "		from eodHistory "
                    + "		where site_Code = :site_Code " + condition;
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdProcessDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("processCode")));
                node.setSharedKeyFieldName(ProcessEOD.getKeyOf(DmShared.iNull(node.getSharedKeyFieldCode()).intValue()).getValue());
                node.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                node.setEodDate(DmShared.dNull(rsTemp.getDate("eodDate")));
                node.setLastRun(DmShared.xNull(rsTemp.getString("lastRun")));
                node.setUser(DmShared.xNull(rsTemp.getString("u_Name")));
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
    public DmResult<String> saveEODRecord(String forDate, String regDocId, String voucherPrefix, UserPermission_Mdl userPermissionMdl) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery = "";
        dmrTemp.setData("");

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            /*====================================< EOD for Room Posting >====================================*/
            EOD_Mdl eodMdl = new EOD_Mdl();
            eodMdl.setCurrentDate(forDate);
            eodMdl.setCurrentTime(DmServer.getServerDateTime()[1]);
            eodMdl.setDatabaseName(DmServer.getSsnDBName(this));
            eodMdl.setSiteCode(DmServer.getSsnSiteCode(this));
            eodMdl.setSiteShortName(DmServer.getSsnSiteSName(this));
            eodMdl.setCompanyCode(DmServer.getSsnCompanyCode(this));
            eodMdl.setUserName(DmServer.getSsnUserName(this));
            eodMdl.setVoucherPrefix(DmServer.getSsnVoucherPrefix(this));

            if (userPermissionMdl.isRoomPosting()) {
                /*====================================< Save Room Posting >====================================*/
                RoomPosting_Mdl roomPosting_Mdl = roomPosting_Server.getRPRoomDetail(eodMdl, DmShared.xNull(regDocId), cnTemp);
                roomPosting_Server.save(roomPosting_Mdl, eodMdl, cnTemp);
            }

            if (userPermissionMdl.isLabConsumption()) {
                /*====================================< EOD for Lab Consumption Posting >====================================*/
                AcPosting_Mdl labConsumptionPosting_Mdl = new AcPosting_Mdl();
                labConsumptionPosting_Mdl.setForDate(forDate);
                labConsumption_Server.save(labConsumptionPosting_Mdl, eodMdl, cnTemp);
            }

            if (userPermissionMdl.isOpdAcPosting()) {
                /*=========================< Get Control Ac >========================*/
                sqlQuery = "Select  e.opdControlAc, sg.name as controlAcName "
                        + "         from enviro as e "
                        + "         left join subGroup as sg on e.opdControlAc = sg.subCode "
                        + "         where e.site_Code = :site_Code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("site_Code", eodMdl.getSiteCode());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    eodMdl.setControlAc(DmShared.xNull(rsTemp.getString("opdControlAc")));
                    eodMdl.setControlAcName(DmShared.xNull(rsTemp.getString("controlAcName")));
                }
                rsTemp.close();
                psTemp.close();

                /*====================================< EOD for OPD A/c Posting >====================================*/
                AcPosting_Mdl accountPosting_Mdl = new AcPosting_Mdl();
                accountPosting_Mdl.setForDate(forDate);
                opdAcPosting_Server.save(accountPosting_Mdl, eodMdl, cnTemp);
            }

            if (userPermissionMdl.isIpdAcPosting()) {
                /*=========================< Get Control Ac >========================*/
                sqlQuery = "Select  e.ipdControlAc, sg.name as controlAcName "
                        + "         from enviro as e "
                        + "         left join subGroup as sg on e.ipdControlAc = sg.subCode "
                        + "         where e.site_Code = :site_Code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("site_Code", eodMdl.getSiteCode());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    eodMdl.setControlAc(DmShared.xNull(rsTemp.getString("ipdControlAc")));
                    eodMdl.setControlAcName(DmShared.xNull(rsTemp.getString("controlAcName")));
                }
                rsTemp.close();
                psTemp.close();

                /*====================================< EOD for OPD A/c Posting >====================================*/
                AcPosting_Mdl accountPosting_Mdl = new AcPosting_Mdl();
                accountPosting_Mdl.setForDate(forDate);
                ipdAcPosting_Server.save(accountPosting_Mdl, eodMdl, cnTemp);
            }

            if (userPermissionMdl.isEmergencyAcPosting()) {
                /*=========================< Get Control Ac >========================*/
                sqlQuery = "Select  e.emergencyControlAc, sg.name as controlAcName "
                        + "         from enviro as e "
                        + "         left join subGroup as sg on e.emergencyControlAc = sg.subCode "
                        + "         where e.site_Code = :site_Code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("site_Code", eodMdl.getSiteCode());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    eodMdl.setControlAc(DmShared.xNull(rsTemp.getString("emergencyControlAc")));
                    eodMdl.setControlAcName(DmShared.xNull(rsTemp.getString("controlAcName")));
                }
                rsTemp.close();
                psTemp.close();

                /*====================================< EOD for OPD A/c Posting >====================================*/
                AcPosting_Mdl accountPosting_Mdl = new AcPosting_Mdl();
                accountPosting_Mdl.setForDate(forDate);
                emergencyAcPosting_Server.save(accountPosting_Mdl, eodMdl, cnTemp);
            }

            if (userPermissionMdl.isDayCareAcPosting()) {
                /*=========================< Get Control Ac >========================*/
                sqlQuery = "Select  e.dayCareControlAc, sg.name as controlAcName "
                        + "         from enviro as e "
                        + "         left join subGroup as sg on e.dayCareControlAc = sg.subCode "
                        + "         where e.site_Code = :site_Code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("site_Code", eodMdl.getSiteCode());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    eodMdl.setControlAc(DmShared.xNull(rsTemp.getString("dayCareControlAc")));
                    eodMdl.setControlAcName(DmShared.xNull(rsTemp.getString("controlAcName")));
                }
                rsTemp.close();
                psTemp.close();

                /*====================================< EOD for OPD A/c Posting >====================================*/
                AcPosting_Mdl accountPosting_Mdl = new AcPosting_Mdl();
                accountPosting_Mdl.setForDate(forDate);
                dayCareAcPosting_Server.save(accountPosting_Mdl, eodMdl, cnTemp);
            }

            if (userPermissionMdl.isDiagnosticAcPosting()) {
                /*=========================< Get Control Ac >========================*/
                sqlQuery = "Select  e.diagnosticControlAc, sg.name as controlAcName, e.diagnosticSelfAc, sgs.name as diagnosticSelfAcName, "
                        + "         e.diagnosticHospitalAc, sgh.name as diagnosticHospitalAcName "
                        + "         from enviro as e "
                        + "         left join subGroup as sg on e.diagnosticControlAc = sg.subCode "
                        + "         left join subGroup as sgs on e.diagnosticSelfAc = sgs.subCode "
                        + "         left join subGroup as sgh on e.diagnosticHospitalAc = sgh.subCode "
                        + "         where e.site_Code = :site_Code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("site_Code", eodMdl.getSiteCode());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    eodMdl.setControlAc(DmShared.xNull(rsTemp.getString("diagnosticControlAc")));
                    eodMdl.setControlAcName(DmShared.xNull(rsTemp.getString("controlAcName")));
                    eodMdl.setSelfAc(DmShared.xNull(rsTemp.getString("diagnosticSelfAc")));
                    eodMdl.setSelfAcName(DmShared.xNull(rsTemp.getString("diagnosticSelfAcName")));
                    eodMdl.setHospitalAc(DmShared.xNull(rsTemp.getString("diagnosticHospitalAc")));
                    eodMdl.setHospitalAcName(DmShared.xNull(rsTemp.getString("diagnosticHospitalAcName")));
                }
                rsTemp.close();
                psTemp.close();

                /*====================================< EOD for OPD A/c Posting >====================================*/
                AcPosting_Mdl accountPosting_Mdl = new AcPosting_Mdl();
                accountPosting_Mdl.setForDate(forDate);
                diagnosticAcPosting_Server.save(accountPosting_Mdl, eodMdl, cnTemp);
            }

            if (userPermissionMdl.isPharmacyAcPosting()) {
                /*=========================< Get Control Ac >========================*/
                sqlQuery = "Select  e.pharmacyControlAc, sg.name as controlAcName, e.pharmacySelfAc, sgs.name as pharmacySelfAcName, "
                        + "         e.pharmacyHospitalAc, sgh.name as pharmacyHospitalAcName "
                        + "         from enviro as e "
                        + "         left join subGroup as sg on e.pharmacyControlAc = sg.subCode "
                        + "         left join subGroup as sgs on e.pharmacySelfAc = sgs.subCode "
                        + "         left join subGroup as sgh on e.pharmacyHospitalAc = sgh.subCode "
                        + "         where e.site_Code = :site_Code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("site_Code", eodMdl.getSiteCode());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    eodMdl.setControlAc(DmShared.xNull(rsTemp.getString("pharmacyControlAc")));
                    eodMdl.setControlAcName(DmShared.xNull(rsTemp.getString("controlAcName")));
                    eodMdl.setSelfAc(DmShared.xNull(rsTemp.getString("pharmacySelfAc")));
                    eodMdl.setSelfAcName(DmShared.xNull(rsTemp.getString("pharmacySelfAcName")));
                    eodMdl.setHospitalAc(DmShared.xNull(rsTemp.getString("pharmacyHospitalAc")));
                    eodMdl.setHospitalAcName(DmShared.xNull(rsTemp.getString("pharmacyHospitalAcName")));
                }
                rsTemp.close();
                psTemp.close();

                /*====================================< EOD for OPD A/c Posting >====================================*/
                AcPosting_Mdl accountPosting_Mdl = new AcPosting_Mdl();
                accountPosting_Mdl.setForDate(forDate);
                pharmacyAcPosting_Server.save(accountPosting_Mdl, eodMdl, cnTemp);
            }

            if (userPermissionMdl.isMisAppPush()) {
                /*====================================< Save MIS Posting >====================================*/
                misAppPush_Server.save(eodMdl, DmServer.getSsnSiteSerialKey(this), cnTemp);
            }

            dmrTemp.setData("");
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
    public DmResult<RoomPosting_Mdl> getRPRoomDetails(String forDate, String regDocId) {
        DmResult<RoomPosting_Mdl> dmrTemp = new DmResult<RoomPosting_Mdl>();
        RoomPosting_Mdl mrTemp = new RoomPosting_Mdl();
        Connection cnTemp = null;
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            EOD_Mdl eodMdl = new EOD_Mdl();
            eodMdl.setCurrentDate(forDate);
            eodMdl.setCurrentTime(DmServer.getServerDateTime()[1]);
            eodMdl.setDatabaseName(DmServer.getSsnDBName(this));
            eodMdl.setSiteCode(DmServer.getSsnSiteCode(this));
            eodMdl.setUserName(DmServer.getSsnUserName(this));
            eodMdl.setVoucherPrefix(DmServer.getSsnVoucherPrefix(this));

            mrTemp = roomPosting_Server.getRPRoomDetail(eodMdl, DmShared.xNull(regDocId), cnTemp);

            dmrTemp.setData(mrTemp);
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

    @Override
    public DmResult<List<GrdServiceDetail_Mdl>> getRPDeleteDetails(String forDate, String regDocId) {
        DmResult<List<GrdServiceDetail_Mdl>> dmrTemp = new DmResult<List<GrdServiceDetail_Mdl>>();
        List<GrdServiceDetail_Mdl> lstRPDeleteDetail = new ArrayList<GrdServiceDetail_Mdl>();
        Connection cnTemp = null;
        dmrTemp.setData(lstRPDeleteDetail);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            EOD_Mdl eodMdl = new EOD_Mdl();
            eodMdl.setCurrentDate(forDate);
            eodMdl.setCurrentTime(DmServer.getServerDateTime()[1]);
            eodMdl.setDatabaseName(DmServer.getSsnDBName(this));
            eodMdl.setSiteCode(DmServer.getSsnSiteCode(this));
            eodMdl.setUserName(DmServer.getSsnUserName(this));
            eodMdl.setVoucherPrefix(DmServer.getSsnVoucherPrefix(this));

            lstRPDeleteDetail = roomPosting_Server.getRPDeleteDetails(eodMdl, DmShared.xNull(regDocId), cnTemp);

            dmrTemp.setData(lstRPDeleteDetail);
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

    @Override
    public DmResult<String> saveRPRecord(String forDate, String regDocId, String voucherPrefix) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        dmrTemp.setData("");

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            EOD_Mdl eodMdl = new EOD_Mdl();
            eodMdl.setCurrentDate(forDate);
            eodMdl.setCurrentTime(DmServer.getServerDateTime()[1]);
            eodMdl.setDatabaseName(DmServer.getSsnDBName(this));
            eodMdl.setSiteCode(DmServer.getSsnSiteCode(this));
            eodMdl.setSiteShortName(DmServer.getSsnSiteSName(this));
            eodMdl.setCompanyCode(DmServer.getSsnCompanyCode(this));
            eodMdl.setUserName(DmServer.getSsnUserName(this));
            eodMdl.setVoucherPrefix(DmServer.getSsnVoucherPrefix(this));

            /*====================================< Save Room Posting >====================================*/
            RoomPosting_Mdl mrTemp = roomPosting_Server.getRPRoomDetail(eodMdl, DmShared.xNull(regDocId), cnTemp);
            roomPosting_Server.save(mrTemp, eodMdl, cnTemp);

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
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

    @Override
    public DmResult<String> deleteRPRecord(String forDate, List<KeyFieldExtended> listRPDetail) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        dmrTemp.setData("");

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            EOD_Mdl eodMdl = new EOD_Mdl();
            eodMdl.setCurrentDate(forDate);
            eodMdl.setDatabaseName(DmServer.getSsnDBName(this));
            eodMdl.setSiteCode(DmServer.getSsnSiteCode(this));
            eodMdl.setSiteShortName(DmServer.getSsnSiteSName(this));
            eodMdl.setCompanyCode(DmServer.getSsnCompanyCode(this));
            eodMdl.setUserName(DmServer.getSsnUserName(this));
            eodMdl.setVoucherPrefix(DmServer.getSsnVoucherPrefix(this));

            /*====================================< Delete Room Posting >====================================*/
            roomPosting_Server.delete(listRPDetail, eodMdl, cnTemp);

            dmrTemp.setData("");
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
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

    @Override
    public DmResult<LabConsumptionPosting_Mdl> getLCPostingDetails(String forDate, String voucherPrefix) {
        DmResult<LabConsumptionPosting_Mdl> dmrTemp = new DmResult<LabConsumptionPosting_Mdl>();
        LabConsumptionPosting_Mdl mrTemp = new LabConsumptionPosting_Mdl();
        Connection cnTemp = null;
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            EOD_Mdl eodMdl = new EOD_Mdl();
            eodMdl.setCurrentDate(forDate);
            eodMdl.setCurrentTime(DmServer.getServerDateTime()[1]);
            eodMdl.setDatabaseName(DmServer.getSsnDBName(this));
            eodMdl.setSiteCode(DmServer.getSsnSiteCode(this));
            eodMdl.setUserName(DmServer.getSsnUserName(this));
            eodMdl.setVoucherPrefix(DmServer.getSsnVoucherPrefix(this));

            mrTemp.getListAcPostingDetail().addAll(labConsumption_Server.getLCPostingDetail(eodMdl, cnTemp));
            mrTemp.getListAcPostedDetail().addAll(labConsumption_Server.getLCDeleteDetail(eodMdl, cnTemp));

            dmrTemp.setData(mrTemp);
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

    @Override
    public DmResult<List<GrdLabConsumptionDetail_Mdl>> getLCDeleteDetails(String forDate, String voucherPrefix) {
        DmResult<List<GrdLabConsumptionDetail_Mdl>> dmrTemp = new DmResult<List<GrdLabConsumptionDetail_Mdl>>();
        List<GrdLabConsumptionDetail_Mdl> lstLCDetail = new ArrayList<GrdLabConsumptionDetail_Mdl>();
        Connection cnTemp = null;
        dmrTemp.setData(lstLCDetail);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            EOD_Mdl eodMdl = new EOD_Mdl();
            eodMdl.setCurrentDate(forDate);
            eodMdl.setCurrentTime(DmServer.getServerDateTime()[1]);
            eodMdl.setDatabaseName(DmServer.getSsnDBName(this));
            eodMdl.setSiteCode(DmServer.getSsnSiteCode(this));
            eodMdl.setUserName(DmServer.getSsnUserName(this));
            eodMdl.setVoucherPrefix(DmServer.getSsnVoucherPrefix(this));

            lstLCDetail = labConsumption_Server.getLCDeleteDetail(eodMdl, cnTemp);

            dmrTemp.setData(lstLCDetail);
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

    @Override
    public DmResult<List<KeyFieldExtended>> getLCPendingDates(String forDate, String voucherPrefix) {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult<List<KeyFieldExtended>>();
        List<KeyFieldExtended> lstPendingDates = new ArrayList<KeyFieldExtended>();
        Connection cnTemp = null;
        dmrTemp.setData(lstPendingDates);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            EOD_Mdl eodMdl = new EOD_Mdl();
            eodMdl.setCurrentDate(forDate);
            eodMdl.setCurrentTime(DmServer.getServerDateTime()[1]);
            eodMdl.setDatabaseName(DmServer.getSsnDBName(this));
            eodMdl.setSiteCode(DmServer.getSsnSiteCode(this));
            eodMdl.setUserName(DmServer.getSsnUserName(this));
            eodMdl.setVoucherPrefix(DmServer.getSsnVoucherPrefix(this));

            lstPendingDates = labConsumption_Server.getPendingDates(eodMdl, cnTemp);

            dmrTemp.setData(lstPendingDates);
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

    @Override
    public DmResult<String> saveLCRecord(String forDate, String voucherPrefix) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        dmrTemp.setData("");

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            EOD_Mdl eodMdl = new EOD_Mdl();
            eodMdl.setCurrentDate(forDate);
            eodMdl.setCurrentTime(DmServer.getServerDateTime()[1]);
            eodMdl.setDatabaseName(DmServer.getSsnDBName(this));
            eodMdl.setSiteCode(DmServer.getSsnSiteCode(this));
            eodMdl.setSiteShortName(DmServer.getSsnSiteSName(this));
            eodMdl.setCompanyCode(DmServer.getSsnCompanyCode(this));
            eodMdl.setUserName(DmServer.getSsnUserName(this));
            eodMdl.setVoucherPrefix(DmServer.getSsnVoucherPrefix(this));

            AcPosting_Mdl mrTemp = new AcPosting_Mdl();
            mrTemp.setForDate(forDate);

            /*====================================< Save Lab Consumption Posting >====================================*/
            labConsumption_Server.save(mrTemp, eodMdl, cnTemp);

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
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

    @Override
    public DmResult<String> deleteLC(String forDate) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        dmrTemp.setData("");

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            EOD_Mdl eodMdl = new EOD_Mdl();
            eodMdl.setCurrentDate(forDate);
            eodMdl.setCurrentTime(DmServer.getServerDateTime()[1]);
            eodMdl.setDatabaseName(DmServer.getSsnDBName(this));
            eodMdl.setSiteCode(DmServer.getSsnSiteCode(this));
            eodMdl.setSiteShortName(DmServer.getSsnSiteSName(this));
            eodMdl.setCompanyCode(DmServer.getSsnCompanyCode(this));
            eodMdl.setUserName(DmServer.getSsnUserName(this));
            eodMdl.setVoucherPrefix(DmServer.getSsnVoucherPrefix(this));

            /*====================================< Delete Lab Consumption Posting >====================================*/
            labConsumption_Server.delete(eodMdl, cnTemp, true);

            dmrTemp.setData(forDate);
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
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

    @Override
    public DmResult<AcPosting_Mdl> getAPDetails(String forDate, String voucherPrefix, String processCode) {
        DmResult<AcPosting_Mdl> dmrTemp = new DmResult<AcPosting_Mdl>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        AcPosting_Mdl mrTemp = new AcPosting_Mdl();
        String sqlQuery = "";
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            EOD_Mdl eodMdl = new EOD_Mdl();
            eodMdl.setCurrentDate(forDate);
            eodMdl.setCurrentTime(DmServer.getServerDateTime()[1]);
            eodMdl.setDatabaseName(DmServer.getSsnDBName(this));
            eodMdl.setSiteCode(DmServer.getSsnSiteCode(this));
            eodMdl.setUserName(DmServer.getSsnUserName(this));
            eodMdl.setVoucherPrefix(DmServer.getSsnVoucherPrefix(this));

            /*=========================< Get Control Ac >========================*/
            sqlQuery = "Select	e.opdControlAc, e.diagnosticControlAc, e.pharmacyControlAc, e.emergencyControlAc, e.ipdControlAc, "
                    + "         e.dayCareControlAc, sg.name as controlAcName , isNull(e.allowDiscountPosting,0) as allowDiscountPosting , "
                    + "   e.IPD_UnbilledService_AC as IPDunbilledServiceCode, sgunb.name as IPDunbilledServiceAcName   "
                    + "		from enviro as e "
                    + "		left join subGroup as sg on (case when " + DmShared.iNull(processCode).intValue() + " = " + ProcessEOD.OPD_AC_POSTING.getId() + " then e.opdControlAc "
                    + "         when " + DmShared.iNull(processCode).intValue() + " = " + ProcessEOD.IPD_AC_POSTING.getId() + " then e.ipdControlAc "
                    + "         when " + DmShared.iNull(processCode).intValue() + " = " + ProcessEOD.EMERGENCY_AC_POSTING.getId() + " then e.emergencyControlAc "
                    + "         when " + DmShared.iNull(processCode).intValue() + " = " + ProcessEOD.DAY_CARE_AC_POSTING.getId() + " then e.dayCareControlAc "
                    + "         when " + DmShared.iNull(processCode).intValue() + " = " + ProcessEOD.DIAGNOSTIC_AC_POSTING.getId() + " then e.diagnosticControlAc "
                    + "         when " + DmShared.iNull(processCode).intValue() + " = " + ProcessEOD.PHARMACY_AC_POSTING.getId() + " then e.pharmacyControlAc "
                    + "         end = sg.subCode) "
                    + " left join subGroup as sgunb on e.IPD_UnbilledService_AC = sgunb.subCode "
                    + "		where e.site_Code = :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", eodMdl.getSiteCode());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.OPD_AC_POSTING) {
                    eodMdl.setControlAc(DmShared.xNull(rsTemp.getString("opdControlAc")));
                } else if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.DAY_CARE_AC_POSTING) {
                    eodMdl.setControlAc(DmShared.xNull(rsTemp.getString("dayCareControlAc")));
                } else if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.IPD_AC_POSTING) {
                    eodMdl.setControlAc(DmShared.xNull(rsTemp.getString("ipdControlAc")));
                } else if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.EMERGENCY_AC_POSTING) {
                    eodMdl.setControlAc(DmShared.xNull(rsTemp.getString("emergencyControlAc")));
                } else if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.DIAGNOSTIC_AC_POSTING) {
                    eodMdl.setControlAc(DmShared.xNull(rsTemp.getString("diagnosticControlAc")));
                } else if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.PHARMACY_AC_POSTING) {
                    eodMdl.setControlAc(DmShared.xNull(rsTemp.getString("pharmacyControlAc")));
                }
                eodMdl.setControlAcName(DmShared.xNull(rsTemp.getString("controlAcName")));
                eodMdl.setAllowDiscountPosting(DmShared.iNull(rsTemp.getString("allowDiscountPosting")).toString());

                eodMdl.setIPDunbilledserviceAcsubCode(DmShared.iNull(rsTemp.getString("IPDunbilledServiceCode")).toString());
                eodMdl.setIPDunbilledServiceAcName(DmShared.xNull(rsTemp.getString("IPDunbilledServiceAcName")));
            }
            rsTemp.close();
            psTemp.close();

//            /*=========================< Get Unbilled Service Ac >========================*/
//            sqlQuery = "select sb.subCode as unbilledServiceCode , sb.name as unbilledServiceAcName from subgroup as sb    "
//                    + " where sb.name = 'Unbilled Service A/c' ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            rsTemp = psTemp.executeQuery();
//            if (rsTemp.next()) {
//                eodMdl.setUnbilledserviceAcsubCode(DmShared.iNull(rsTemp.getString("unbilledServiceCode")).toString());
//                eodMdl.setUnbilledServiceAcName(DmShared.xNull(rsTemp.getString("unbilledServiceAcName")));
//            }
//            rsTemp.close();
//            psTemp.close();
            if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.OPD_AC_POSTING) {
                if (DmShared.iNull(eodMdl.getControlAc()).longValue() == 0l) {
                    throw new DmException(0, "Please set OPD Control A/c in Environment Setting.");
                }

                mrTemp.getListAcPostingDetail().addAll(opdAcPosting_Server.getReceiptPostingDetail(eodMdl, cnTemp));
                mrTemp.getListAcPostingDetail().addAll(opdAcPosting_Server.getBankPostingDetail(eodMdl, cnTemp));
                mrTemp.getListAcPostingDetail().addAll(opdAcPosting_Server.getServicePostingDetail(eodMdl, cnTemp));
                mrTemp.getListAcPostedDetail().addAll(opdAcPosting_Server.getPostedDetail(eodMdl, cnTemp));

            } else if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.DAY_CARE_AC_POSTING) {
                if (DmShared.iNull(eodMdl.getControlAc()).longValue() == 0l) {
                    throw new DmException(0, "Please set Day Care Control A/c in Environment Setting.");
                }

                mrTemp.getListAcPostingDetail().addAll(dayCareAcPosting_Server.getReceiptPostingDetail(eodMdl, cnTemp));
                mrTemp.getListAcPostingDetail().addAll(dayCareAcPosting_Server.getBankPostingDetail(eodMdl, cnTemp));
                mrTemp.getListAcPostedDetail().addAll(dayCareAcPosting_Server.getPostedDetail(eodMdl, cnTemp));

            } else if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.IPD_AC_POSTING) {
                if (DmShared.iNull(eodMdl.getControlAc()).longValue() == 0l) {
                    throw new DmException(0, "Please set IPD Control A/c in Environment Setting.");
                }

                mrTemp.getListAcPostingDetail().addAll(ipdAcPosting_Server.getReceiptPostingDetail(eodMdl, cnTemp));
                mrTemp.getListAcPostingDetail().addAll(ipdAcPosting_Server.getIPDServicePostingDetail(eodMdl, cnTemp));
                mrTemp.getListAcPostingDetail().addAll(ipdAcPosting_Server.getIPDBillPostingDisplayOnlySummary(eodMdl, cnTemp));
                mrTemp.getListAcPostingDetail().addAll(ipdAcPosting_Server.getIPDSecurityPostingDetailDisplay(eodMdl, cnTemp));

                mrTemp.getListAcPostingDetail().addAll(ipdAcPosting_Server.getIPDBillableRefundPostingDetailDisplay(eodMdl, cnTemp));

                mrTemp.getListAcPostedDetail().addAll(ipdAcPosting_Server.getPostedDetail(eodMdl, cnTemp));

                mrTemp.getListAcPostingDetail().addAll(ipdAcPosting_Server.getBankPostingDetail(eodMdl, cnTemp));

            } else if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.EMERGENCY_AC_POSTING) {
                if (DmShared.iNull(eodMdl.getControlAc()).longValue() == 0l) {
                    throw new DmException(0, "Please set Emergency Control A/c in Environment Setting.");
                }

                mrTemp.getListAcPostingDetail().addAll(emergencyAcPosting_Server.getReceiptPostingDetail(eodMdl, cnTemp));
                mrTemp.getListAcPostingDetail().addAll(emergencyAcPosting_Server.getBankPostingDetail(eodMdl, cnTemp));
                mrTemp.getListAcPostedDetail().addAll(emergencyAcPosting_Server.getPostedDetail(eodMdl, cnTemp));

            } else if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.DIAGNOSTIC_AC_POSTING) {

                /*=========================< Get Control Ac >========================*/
                sqlQuery = "Select  e.diagnosticSelfAc, sg.name as diagnosticSelfAcName, e.diagnosticHospitalAc, sgh.name as diagnosticHospitalAcName "
                        + "         from enviro as e "
                        + "         left join subGroup as sg on e.diagnosticSelfAc = sg.subCode "
                        + "         left join subGroup as sgh on e.diagnosticHospitalAc = sgh.subCode "
                        + "         where e.site_Code = :site_Code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("site_Code", eodMdl.getSiteCode());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    eodMdl.setSelfAc(DmShared.xNull(rsTemp.getString("diagnosticSelfAc")));
                    eodMdl.setSelfAcName(DmShared.xNull(rsTemp.getString("diagnosticSelfAcName")));
                    eodMdl.setHospitalAc(DmShared.xNull(rsTemp.getString("diagnosticHospitalAc")));
                    eodMdl.setHospitalAcName(DmShared.xNull(rsTemp.getString("diagnosticHospitalAcName")));
                }
                rsTemp.close();
                psTemp.close();

                if (DmShared.iNull(eodMdl.getSelfAc()).longValue() == 0l) {
                    throw new DmException(0, "Please set Diagnostic A/c in Environment Setting.");
                }
                if (DmShared.iNull(eodMdl.getHospitalAc()).longValue() == 0l) {
                    throw new DmException(0, "Please set Hospital A/c in Environment Setting.");
                }
                if (DmShared.iNull(eodMdl.getControlAc()).longValue() == 0l) {
                    throw new DmException(0, "Please set Diagnostic Control A/c in Environment Setting.");
                }

                mrTemp.getListAcPostingDetail().addAll(diagnosticAcPosting_Server.getReceiptPostingDetail(eodMdl, cnTemp));
                mrTemp.getListAcPostingDetail().addAll(diagnosticAcPosting_Server.getBankPostingDetail(eodMdl, cnTemp));
                mrTemp.getListAcPostingDetail().addAll(diagnosticAcPosting_Server.getCTRReceiptPostingDetail(eodMdl, cnTemp));
                mrTemp.getListAcPostingDetail().addAll(diagnosticAcPosting_Server.getServicePostingDetail(eodMdl, cnTemp));
//                mrTemp.getListAcPostingDetail().addAll(diagnosticAcPosting_Server.getCTRServicePostingDetail(eodMdl, cnTemp));
                mrTemp.getListAcPostedDetail().addAll(diagnosticAcPosting_Server.getPostedDetail(eodMdl, cnTemp));

            } else if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.PHARMACY_AC_POSTING) {

                /*=========================< Get Control Ac >========================*/
                sqlQuery = "Select  e.pharmacySelfAc, sg.name as pharmacySelfAcName, e.pharmacyHospitalAc, sgh.name as pharmacyHospitalAcName "
                        + "         from enviro as e "
                        + "         left join subGroup as sg on e.pharmacySelfAc = sg.subCode "
                        + "         left join subGroup as sgh on e.pharmacyHospitalAc = sgh.subCode "
                        + "         where e.site_Code = :site_Code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("site_Code", eodMdl.getSiteCode());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    eodMdl.setSelfAc(DmShared.xNull(rsTemp.getString("pharmacySelfAc")));
                    eodMdl.setSelfAcName(DmShared.xNull(rsTemp.getString("pharmacySelfAcName")));
                    eodMdl.setHospitalAc(DmShared.xNull(rsTemp.getString("pharmacyHospitalAc")));
                    eodMdl.setHospitalAcName(DmShared.xNull(rsTemp.getString("pharmacyHospitalAcName")));
                }
                rsTemp.close();
                psTemp.close();

                if (DmShared.iNull(eodMdl.getSelfAc()).longValue() == 0l) {
                    throw new DmException(0, "Please set Pharmacy A/c in Environment Setting.");
                }
                if (DmShared.iNull(eodMdl.getHospitalAc()).longValue() == 0l) {
                    throw new DmException(0, "Please set Hospital A/c in Environment Setting.");
                }
                if (DmShared.iNull(eodMdl.getControlAc()).longValue() == 0l) {
                    throw new DmException(0, "Please set Pharmacy Control A/c in Environment Setting.");
                }

                mrTemp.getListAcPostingDetail().addAll(pharmacyAcPosting_Server.getReceiptPostingDetail(eodMdl, cnTemp));
                mrTemp.getListAcPostingDetail().addAll(pharmacyAcPosting_Server.getServicePostingDetail(eodMdl, cnTemp));
//               ///// mrTemp.getListAcPostingDetail().addAll(pharmacyAcPosting_Server.getCTR_CTO_ServicePostingDetail(eodMdl, cnTemp));
                mrTemp.getListAcPostedDetail().addAll(pharmacyAcPosting_Server.getPostedDetail(eodMdl, cnTemp));
                mrTemp.getListAcPostingDetail().addAll(pharmacyAcPosting_Server.getSaleStockPostingDetail(eodMdl, cnTemp));
                mrTemp.getListAcPostingDetail().addAll(pharmacyAcPosting_Server.getBankPostingDetail(eodMdl, cnTemp));
            }

            dmrTemp.setData(mrTemp);
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
    public DmResult<List<GrdAcPostedDetail_Mdl>> getAPDeleteDetails(String forDate, String voucherPrefix, String processCode) {
        DmResult<List<GrdAcPostedDetail_Mdl>> dmrTemp = new DmResult<List<GrdAcPostedDetail_Mdl>>();
        List<GrdAcPostedDetail_Mdl> lstNode = new ArrayList<GrdAcPostedDetail_Mdl>();
        Connection cnTemp = null;
        dmrTemp.setData(lstNode);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            EOD_Mdl eodMdl = new EOD_Mdl();
            eodMdl.setCurrentDate(forDate);
            eodMdl.setCurrentTime(DmServer.getServerDateTime()[1]);
            eodMdl.setDatabaseName(DmServer.getSsnDBName(this));
            eodMdl.setSiteCode(DmServer.getSsnSiteCode(this));
            eodMdl.setUserName(DmServer.getSsnUserName(this));
            eodMdl.setVoucherPrefix(DmServer.getSsnVoucherPrefix(this));

            if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.OPD_AC_POSTING) {
                lstNode = opdAcPosting_Server.getPostedDetail(eodMdl, cnTemp);
            } else if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.DAY_CARE_AC_POSTING) {
                lstNode = dayCareAcPosting_Server.getPostedDetail(eodMdl, cnTemp);
            } else if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.IPD_AC_POSTING) {
                lstNode = ipdAcPosting_Server.getPostedDetail(eodMdl, cnTemp);
            } else if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.EMERGENCY_AC_POSTING) {
                lstNode = emergencyAcPosting_Server.getPostedDetail(eodMdl, cnTemp);
            } else if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.DIAGNOSTIC_AC_POSTING) {
                lstNode = diagnosticAcPosting_Server.getPostedDetail(eodMdl, cnTemp);
            } else if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.PHARMACY_AC_POSTING) {
                lstNode = pharmacyAcPosting_Server.getPostedDetail(eodMdl, cnTemp);
            }

            dmrTemp.setData(lstNode);
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

    @Override
    public DmResult<List<KeyFieldExtended>> getAPPendingDates(String forDate, String voucherPrefix, String processCode) {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult<List<KeyFieldExtended>>();
        List<KeyFieldExtended> lstPendingDates = new ArrayList<KeyFieldExtended>();
        Connection cnTemp = null;
        dmrTemp.setData(lstPendingDates);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            EOD_Mdl eodMdl = new EOD_Mdl();
            eodMdl.setCurrentDate(forDate);
            eodMdl.setCurrentTime(DmServer.getServerDateTime()[1]);
            eodMdl.setDatabaseName(DmServer.getSsnDBName(this));
            eodMdl.setSiteCode(DmServer.getSsnSiteCode(this));
            eodMdl.setUserName(DmServer.getSsnUserName(this));
            eodMdl.setVoucherPrefix(DmServer.getSsnVoucherPrefix(this));

            if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.OPD_AC_POSTING) {
                lstPendingDates = opdAcPosting_Server.getPendingDates(eodMdl, cnTemp);
            } else if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.DAY_CARE_AC_POSTING) {
                lstPendingDates = dayCareAcPosting_Server.getPendingDates(eodMdl, cnTemp);
            } else if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.IPD_AC_POSTING) {
                lstPendingDates = ipdAcPosting_Server.getPendingDates(eodMdl, cnTemp);
            } else if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.EMERGENCY_AC_POSTING) {
                lstPendingDates = emergencyAcPosting_Server.getPendingDates(eodMdl, cnTemp);
            } else if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.DIAGNOSTIC_AC_POSTING) {
                lstPendingDates = diagnosticAcPosting_Server.getPendingDates(eodMdl, cnTemp);
            } else if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.PHARMACY_AC_POSTING) {
                lstPendingDates = pharmacyAcPosting_Server.getPendingDates(eodMdl, cnTemp);
            }

            dmrTemp.setData(lstPendingDates);
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

    private long getPreviousDateDifference(String date) {
        // int diffInDays = 0;

        long diffInMillisec = 0;
        long diffInDays = 0;
        Calendar firstDate = null;
        Calendar secondDate = null;

        try {
            // Create two calendars instances
            firstDate = Calendar.getInstance();
            secondDate = Calendar.getInstance();

            //Set the dates
            firstDate.setTime(DmShared.convertDate(DmServer.getServerDateTime()[0]));
            secondDate.setTime(DmShared.convertDate(date));

            // Get the difference between two dates in milliseconds    
            diffInMillisec = firstDate.getTimeInMillis() - secondDate.getTimeInMillis();

            // Get difference between two dates in days
            diffInDays = diffInMillisec / (24 * 60 * 60 * 1000);

        } catch (Exception ex) {

        }

        return diffInDays;
    }

    @Override
    public DmResult<String> saveAPRecord(String forDate, ArrayList<String> forDates, ArrayList<String> pendingEodDates, String fromDate, String voucherPrefix, String processCode) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery = "";
        dmrTemp.setData("");
        Calendar cal = null;
        int eodRunAllowDays = 0;
        try {
            AcPosting_Mdl mrTemp = new AcPosting_Mdl();
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            eodRunAllowDays = DmShared.iNull(DmServer.getCodeViaManualCode("1", "1", "enviroCompany", "eodRunAllowDays", cnTemp)).intValue();

            if (forDates != null && forDates.size() > 0) {

                if (eodRunAllowDays != 0) {

                    if (getPreviousDateDifference(fromDate) > eodRunAllowDays && (!DmServer.getSsnUserName(this).equalsIgnoreCase("SA") && !DmServer.getSsnUserName(this).equalsIgnoreCase("SUPER"))) {
                        throw new DmException(0, "Only " + DmShared.iNull(eodRunAllowDays).toString() + " days previous EOD run allowed ! please contact to administrator.");
                    }
                }

                if (pendingEodDates.size() > 0) {

                    cal = Calendar.getInstance();
                    cal.setTime(DmShared.convertDate(fromDate));
                    cal.add(Calendar.DATE, -1);
                    SimpleDateFormat s = new SimpleDateFormat("dd-MMM-yyyy");
                    String result = s.format(new Date(cal.getTimeInMillis()));

                    for (int j = 0; j < pendingEodDates.size(); j++) {
                        if (result.equalsIgnoreCase(pendingEodDates.get(j))) {
                            throw new DmException(0, "Please run EOD from Date " + pendingEodDates.get(0));
                        }
                    }
                }

                for (int i = 0; i < forDates.size(); i++) {
                    forDate = forDates.get(i);
                    saveAccountPosting(forDate, pendingEodDates, mrTemp, psTemp, rsTemp, processCode, cnTemp, dmrTemp);
                }

            } else {
//

                if (eodRunAllowDays != 0) {

                    if (getPreviousDateDifference(forDate) > eodRunAllowDays && (!DmServer.getSsnUserName(this).equalsIgnoreCase("SA") && !DmServer.getSsnUserName(this).equalsIgnoreCase("SUPER"))) {
                        throw new DmException(0, "Only " + DmShared.iNull(eodRunAllowDays).toString() + " days previous EOD run allowed ! please contact to administrator.");
                    }
                }

                if (pendingEodDates.size() > 0) {

                    cal = Calendar.getInstance();

                    cal.setTime(DmShared.convertDate(forDate));
                    cal.add(Calendar.DATE, -1);

                    SimpleDateFormat s = new SimpleDateFormat("dd-MMM-yyyy");
                    String result = s.format(new Date(cal.getTimeInMillis()));

                    for (int j = 0; j < pendingEodDates.size(); j++) {
                        if (result.equalsIgnoreCase(pendingEodDates.get(j))) {
                            throw new DmException(0, "Please run previous EOD for Date " + result);

                        }
                    }

                }

                //
                saveAccountPosting(forDate, pendingEodDates, mrTemp, psTemp, rsTemp, processCode, cnTemp, dmrTemp);

            }

            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
            spBegin.commit();
            cnTemp.close();
//            dmrTemp.setStatus(true);

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

    private void saveAccountPosting(String forDate, ArrayList<String> pendingEodDates, AcPosting_Mdl mrTemp, DmPreparedStatement psTemp, ResultSet rsTemp, String processCode, Connection cnTemp, DmResult<String> dmrTemp) {
        String sqlQuery = "";
        Calendar cal = null;
        try {
            EOD_Mdl eodMdl = new EOD_Mdl();
            eodMdl.setCurrentDate(forDate);
            eodMdl.setCurrentTime(DmServer.getServerDateTime()[1]);
            eodMdl.setDatabaseName(DmServer.getSsnDBName(this));
            eodMdl.setSiteCode(DmServer.getSsnSiteCode(this));
            eodMdl.setSiteShortName(DmServer.getSsnSiteSName(this));
            eodMdl.setCompanyCode(DmServer.getSsnCompanyCode(this));
            eodMdl.setUserName(DmServer.getSsnUserName(this));
            eodMdl.setVoucherPrefix(DmServer.getSsnVoucherPrefix(this));

            /*=========================< Get Control Ac >========================*/
            sqlQuery = "Select	e.opdControlAc, e.diagnosticControlAc, e.pharmacyControlAc, e.emergencyControlAc, e.ipdControlAc, "
                    + "         e.dayCareControlAc, sg.name as controlAcName , isNull(e.allowDiscountPosting,0) as allowDiscountPosting , "
                    + "   e.IPD_UnbilledService_AC as IPDunbilledServiceCode, sgunb.name as IPDunbilledServiceAcName  "
                    + "		from enviro as e "
                    + "		left join subGroup as sg on (case when " + DmShared.iNull(processCode).intValue() + " = " + ProcessEOD.OPD_AC_POSTING.getId() + " then e.opdControlAc "
                    + "         when " + DmShared.iNull(processCode).intValue() + " = " + ProcessEOD.IPD_AC_POSTING.getId() + " then e.ipdControlAc "
                    + "         when " + DmShared.iNull(processCode).intValue() + " = " + ProcessEOD.EMERGENCY_AC_POSTING.getId() + " then e.emergencyControlAc "
                    + "         when " + DmShared.iNull(processCode).intValue() + " = " + ProcessEOD.DAY_CARE_AC_POSTING.getId() + " then e.dayCareControlAc "
                    + "         when " + DmShared.iNull(processCode).intValue() + " = " + ProcessEOD.DIAGNOSTIC_AC_POSTING.getId() + " then e.diagnosticControlAc "
                    + "         when " + DmShared.iNull(processCode).intValue() + " = " + ProcessEOD.PHARMACY_AC_POSTING.getId() + " then e.pharmacyControlAc "
                    + "         end = sg.subCode) "
                    + " left join subGroup as sgunb on e.IPD_UnbilledService_AC = sgunb.subCode "
                    + "		where e.site_Code = :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", eodMdl.getSiteCode());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.OPD_AC_POSTING) {
                    eodMdl.setControlAc(DmShared.xNull(rsTemp.getString("opdControlAc")));
                } else if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.DAY_CARE_AC_POSTING) {
                    eodMdl.setControlAc(DmShared.xNull(rsTemp.getString("dayCareControlAc")));
                } else if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.IPD_AC_POSTING) {
                    eodMdl.setControlAc(DmShared.xNull(rsTemp.getString("ipdControlAc")));
                } else if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.EMERGENCY_AC_POSTING) {
                    eodMdl.setControlAc(DmShared.xNull(rsTemp.getString("emergencyControlAc")));
                } else if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.DIAGNOSTIC_AC_POSTING) {
                    eodMdl.setControlAc(DmShared.xNull(rsTemp.getString("diagnosticControlAc")));
                } else if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.PHARMACY_AC_POSTING) {
                    eodMdl.setControlAc(DmShared.xNull(rsTemp.getString("pharmacyControlAc")));
                }
                eodMdl.setControlAcName(DmShared.xNull(rsTemp.getString("controlAcName")));

                eodMdl.setAllowDiscountPosting(DmShared.iNull(rsTemp.getString("allowDiscountPosting")).toString());

                eodMdl.setIPDunbilledserviceAcsubCode(DmShared.iNull(rsTemp.getString("IPDunbilledServiceCode")).toString());
                eodMdl.setIPDunbilledServiceAcName(DmShared.xNull(rsTemp.getString("IPDunbilledServiceAcName")));
            }
            rsTemp.close();
            psTemp.close();

            /*====================================< Save Account Posting >====================================*/
//                    AcPosting_Mdl mrTemp = new AcPosting_Mdl();
            mrTemp.setForDate(forDate);

            if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.OPD_AC_POSTING) {
                if (DmShared.iNull(eodMdl.getControlAc()).longValue() == 0l) {
                    throw new DmException(0, "Please set OPD Control A/c in Environment Setting.");
                }
                opdAcPosting_Server.save(mrTemp, eodMdl, cnTemp);

            } else if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.DAY_CARE_AC_POSTING) {
                if (DmShared.iNull(eodMdl.getControlAc()).longValue() == 0l) {
                    throw new DmException(0, "Please set Day Care Control A/c in Environment Setting.");
                }
                dayCareAcPosting_Server.save(mrTemp, eodMdl, cnTemp);

            } else if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.IPD_AC_POSTING) {
                if (DmShared.iNull(eodMdl.getControlAc()).longValue() == 0l) {
                    throw new DmException(0, "Please set IPD Control A/c in Environment Setting.");
                }
                if (DmShared.iNull(eodMdl.getIPDunbilledserviceAcsubCode()).longValue() == 0l) {
                    throw new DmException(0, "Unbilled Service A/c not found.");
                }
                ipdAcPosting_Server.save(mrTemp, eodMdl, cnTemp);

            } else if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.EMERGENCY_AC_POSTING) {
                if (DmShared.iNull(eodMdl.getControlAc()).longValue() == 0l) {
                    throw new DmException(0, "Please set Emergency Control A/c in Environment Setting.");
                }
                emergencyAcPosting_Server.save(mrTemp, eodMdl, cnTemp);

            } else if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.DIAGNOSTIC_AC_POSTING) {

                /*=========================< Get Control Ac >========================*/
                sqlQuery = "Select  e.diagnosticSelfAc, sg.name as diagnosticSelfAcName, e.diagnosticHospitalAc, sgh.name as diagnosticHospitalAcName "
                        + "         from enviro as e "
                        + "         left join subGroup as sg on e.diagnosticSelfAc = sg.subCode "
                        + "         left join subGroup as sgh on e.diagnosticHospitalAc = sgh.subCode "
                        + "         where e.site_Code = :site_Code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("site_Code", eodMdl.getSiteCode());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    eodMdl.setSelfAc(DmShared.xNull(rsTemp.getString("diagnosticSelfAc")));
                    eodMdl.setSelfAcName(DmShared.xNull(rsTemp.getString("diagnosticSelfAcName")));
                    eodMdl.setHospitalAc(DmShared.xNull(rsTemp.getString("diagnosticHospitalAc")));
                    eodMdl.setHospitalAcName(DmShared.xNull(rsTemp.getString("diagnosticHospitalAcName")));
                }
                rsTemp.close();
                psTemp.close();

                if (DmShared.iNull(eodMdl.getSelfAc()).longValue() == 0l) {
                    throw new DmException(0, "Please set Diagnostic A/c in Environment Setting.");
                }
                if (DmShared.iNull(eodMdl.getHospitalAc()).longValue() == 0l) {
                    throw new DmException(0, "Please set Hospital A/c in Environment Setting.");
                }
                if (DmShared.iNull(eodMdl.getControlAc()).longValue() == 0l) {
                    throw new DmException(0, "Please set Diagnostic Control A/c in Environment Setting.");
                }
                diagnosticAcPosting_Server.save(mrTemp, eodMdl, cnTemp);

            } else if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.PHARMACY_AC_POSTING) {

                /*=========================< Get Control Ac >========================*/
                sqlQuery = "Select  e.pharmacySelfAc, sg.name as pharmacySelfAcName, e.pharmacyHospitalAc, sgh.name as pharmacyHospitalAcName "
                        + "         from enviro as e "
                        + "         left join subGroup as sg on e.pharmacySelfAc = sg.subCode "
                        + "         left join subGroup as sgh on e.pharmacyHospitalAc = sgh.subCode "
                        + "         where e.site_Code = :site_Code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("site_Code", eodMdl.getSiteCode());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    eodMdl.setSelfAc(DmShared.xNull(rsTemp.getString("pharmacySelfAc")));
                    eodMdl.setSelfAcName(DmShared.xNull(rsTemp.getString("pharmacySelfAcName")));
                    eodMdl.setHospitalAc(DmShared.xNull(rsTemp.getString("pharmacyHospitalAc")));
                    eodMdl.setHospitalAcName(DmShared.xNull(rsTemp.getString("pharmacyHospitalAcName")));
                }
                rsTemp.close();
                psTemp.close();

                if (DmShared.iNull(eodMdl.getSelfAc()).longValue() == 0l) {
                    throw new DmException(0, "Please set Pharmacy A/c in Environment Setting.");
                }
                if (DmShared.iNull(eodMdl.getHospitalAc()).longValue() == 0l) {
                    throw new DmException(0, "Please set Hospital A/c in Environment Setting.");
                }
                if (DmShared.iNull(eodMdl.getControlAc()).longValue() == 0l) {
                    throw new DmException(0, "Please set Pharmacy Control A/c in Environment Setting.");
                }
                pharmacyAcPosting_Server.save(mrTemp, eodMdl, cnTemp);
            }

            dmrTemp.setStatus(true);

        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
    }

    @Override
    public DmResult<String> deleteAP(String forDate, ArrayList<String> forDates, boolean isRunEODDateWise, String processCode) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        dmrTemp.setData("");
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;

        String fromDate = "", toDate = "", sqlQuery = "";
        ArrayList<String> tempAllDates = new ArrayList<String>();
        ArrayList<String> finalforDates = new ArrayList<String>();

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            if (forDates != null && forDates.size() > 0) {

                fromDate = forDates.get(0);
                toDate = forDates.get(1);

                tempAllDates.clear();
                /*====================================< Get EOD Post Dates >====================================*/
                sqlQuery = "select distinct lg.v_Date from ledger lg \n"
                        + " where lg.site_Code = :site_Code and lg.v_Prefix = :VoucherPrefix ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("VoucherPrefix", DmServer.getSsnVoucherPrefix(this));
                psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));

                rsTemp = psTemp.executeQuery();
                while (rsTemp.next()) {
                    tempAllDates.add(DmShared.dNull(rsTemp.getDate("v_Date")));
                }
                rsTemp.close();
                psTemp.close();
                /////////////////////////////////////////////////////////////////////////////////////////////

                //=================================final for Dates================
                finalforDates.clear();
                for (int i = 0; i < tempAllDates.size(); i++) {

                    if (DmShared.convertDate(tempAllDates.get(i)).getTime() >= DmShared.convertDate(fromDate).getTime()
                            && DmShared.convertDate(tempAllDates.get(i)).getTime() <= DmShared.convertDate(toDate).getTime()) {

                        finalforDates.add(DmShared.xNull(tempAllDates.get(i)).trim());

                    }
                }

                if (finalforDates != null && finalforDates.size() > 0) {
                    for (int i = 0; i < finalforDates.size(); i++) {
                        forDate = finalforDates.get(i);
                        deletePosting(forDate, processCode, cnTemp);

                    }
                }

            } else {

                deletePosting(forDate, processCode, cnTemp);

            }

//            EOD_Mdl eodMdl = new EOD_Mdl();
//            eodMdl.setCurrentDate(forDate);
//            eodMdl.setCurrentTime(DmServer.getServerDateTime()[1]);
//            eodMdl.setDatabaseName(DmServer.getSsnDBName(this));
//            eodMdl.setSiteCode(DmServer.getSsnSiteCode(this));
//            eodMdl.setSiteShortName(DmServer.getSsnSiteSName(this));
//            eodMdl.setCompanyCode(DmServer.getSsnCompanyCode(this));
//            eodMdl.setUserName(DmServer.getSsnUserName(this));
//            eodMdl.setVoucherPrefix(DmServer.getSsnVoucherPrefix(this));
//
//            /*====================================< Delete Account Posting >====================================*/
//            if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.OPD_AC_POSTING) {
//                opdAcPosting_Server.delete(eodMdl, cnTemp, true);
//            } else if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.DAY_CARE_AC_POSTING) {
//                dayCareAcPosting_Server.delete(eodMdl, cnTemp, true);
//            } else if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.IPD_AC_POSTING) {
//                ipdAcPosting_Server.delete(eodMdl, cnTemp, true);
//            } else if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.EMERGENCY_AC_POSTING) {
//                emergencyAcPosting_Server.delete(eodMdl, cnTemp, true);
//            } else if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.DIAGNOSTIC_AC_POSTING) {
//                diagnosticAcPosting_Server.delete(eodMdl, cnTemp, true);
//            } else if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.PHARMACY_AC_POSTING) {
//                pharmacyAcPosting_Server.delete(eodMdl, cnTemp, true);
//            }
            dmrTemp.setData(forDate);
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
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (rsTemp != null) {
                    rsTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

    private void deletePosting(String forDate, String processCode, Connection cnTemp) {
        try {
            EOD_Mdl eodMdl = new EOD_Mdl();
            eodMdl.setCurrentDate(forDate);
            eodMdl.setCurrentTime(DmServer.getServerDateTime()[1]);
            eodMdl.setDatabaseName(DmServer.getSsnDBName(this));
            eodMdl.setSiteCode(DmServer.getSsnSiteCode(this));
            eodMdl.setSiteShortName(DmServer.getSsnSiteSName(this));
            eodMdl.setCompanyCode(DmServer.getSsnCompanyCode(this));
            eodMdl.setUserName(DmServer.getSsnUserName(this));
            eodMdl.setVoucherPrefix(DmServer.getSsnVoucherPrefix(this));

            /*====================================< Delete Account Posting >====================================*/
            if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.OPD_AC_POSTING) {
                opdAcPosting_Server.delete(eodMdl, cnTemp, true);
            } else if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.DAY_CARE_AC_POSTING) {
                dayCareAcPosting_Server.delete(eodMdl, cnTemp, true);
            } else if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.IPD_AC_POSTING) {
                ipdAcPosting_Server.delete(eodMdl, cnTemp, true);
            } else if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.EMERGENCY_AC_POSTING) {
                emergencyAcPosting_Server.delete(eodMdl, cnTemp, true);
            } else if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.DIAGNOSTIC_AC_POSTING) {
                diagnosticAcPosting_Server.delete(eodMdl, cnTemp, true);
            } else if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.PHARMACY_AC_POSTING) {
                pharmacyAcPosting_Server.delete(eodMdl, cnTemp, true);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public DmResult<String> saveMISPosting(String forDate) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        dmrTemp.setData("");

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            EOD_Mdl eodMdl = new EOD_Mdl();
            eodMdl.setCurrentDate(forDate);
            eodMdl.setCurrentTime(DmServer.getServerDateTime()[1]);
            eodMdl.setDatabaseName(DmServer.getSsnDBName(this));
            eodMdl.setSiteCode(DmServer.getSsnSiteCode(this));
            eodMdl.setSiteShortName(DmServer.getSsnSiteSName(this));
            eodMdl.setCompanyCode(DmServer.getSsnCompanyCode(this));
            eodMdl.setUserName(DmServer.getSsnUserName(this));
            eodMdl.setVoucherPrefix(DmServer.getSsnVoucherPrefix(this));

            /*====================================< Save MIS Posting >====================================*/
            misAppPush_Server.save(eodMdl, DmServer.getSsnSiteSerialKey(this), cnTemp);

            dmrTemp.setData("");
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
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }
}
