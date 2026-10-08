package aarogya.selfToolkit.stStatic.server;

import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.shared.PatientMRID_Mdl;
import aarogya.selfToolkit.stStatic.shared.PatientTreatmentId_Mdl;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;

public class ManagePatientId {

    private static final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private static String vrCategory_MRID = pvc.PATIENT_MRID;
    private static String vrCategory_TRTID = pvc.PATIENT_TREATMENTID;

    public static void savePatientMRID(PatientMRID_Mdl mrTemp, boolean isNewRecord, Connection cnTemp, DmRSSPrivate rss) {
        String sqlQuery;
        DmPreparedStatement psTemp = null;
        boolean blnAdd = false;
        try {
            if (mrTemp != null && isNewRecord) {
                PatientMRID_Mdl temp = getLatestMRID(mrTemp.getRegDocId(), mrTemp.getUhid(), cnTemp);
                if (temp != null) {
                    //mrTemp = temp;
                    mrTemp.setDocId(temp.getDocId());
                    mrTemp.setRecId(temp.getRecId());
                }
                if (DmShared.iNull(mrTemp.getDocId()).longValue() == 0) {
                    blnAdd = true;
                    mrTemp.setVoucherType(DmServer.getVType(vrCategory_MRID, cnTemp));
                    mrTemp.setDocId(DmServer.getDocId(mrTemp.getVoucherType(), DmServer.getSsnVoucherPrefix(rss), cnTemp, rss));
                    mrTemp.setRecId(DmServer.getRecId("patientMRID", "docId", mrTemp.getDocId(), "recId", mrTemp.getRecId(), mrTemp.getVoucherDate(), mrTemp.getVoucherType(), DmServer.getSsnVoucherPrefix(rss), cnTemp, rss, true));
                }

                if (DmShared.iNull(mrTemp.getDocId()).longValue() == 0) {
                    throw new DmException(0, "Invalid MRID.");
                }

                if (blnAdd) {
                    sqlQuery = "insert into patientMRID (docId, v_Type, v_No, recIdPrefix, recId, v_Prefix, v_Date, v_Time, uhid, regDocId, "
                            + "		preparedBy, site_Code, u_EntDt)"
                            + "values (:docId, :vType, :vNo, :recIdPrefix, :recId, :vPrefix, :vDate, :vTime, :uhid, :regDocId, :userName, "
                            + "		:siteCode, :uEntDt)";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", mrTemp.getDocId());
                    psTemp.setString("vType", mrTemp.getVoucherType());
                    psTemp.setLong("vNo", mrTemp.getRecId().getCounter());
                    psTemp.setString("vPrefix", DmServer.getSsnVoucherPrefix(rss));
                    psTemp.setString("recIdPrefix", mrTemp.getRecId().getPrefix());
                    psTemp.setString("recId", mrTemp.getRecId().getRecIdValue());
                    psTemp.setDate("vDate", DmServer.convertDate(mrTemp.getVoucherDate()));
                    psTemp.setString("vTime", DmServer.vNull(mrTemp.getVoucherTime(), "00.00"));
                    psTemp.setString("uhid", DmShared.foriegnKey(mrTemp.getUhid()));
                    psTemp.setString("regDocId", DmShared.foriegnKey(mrTemp.getRegDocId()));
                    psTemp.setString("userName", DmServer.getSsnUserName(rss));
                    psTemp.setString("siteCode", DmServer.getSsnSiteCode(rss));
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                } else {
                    sqlQuery = "update  patientMRID set"
                            + "		regDocId=:regDocId, "
                            + "		u_Name = :userName, "
                            + "		u_EntDt = :uEntDt"
                            + "		where docId = :docId";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", mrTemp.getDocId());
                    psTemp.setString("regDocId", DmShared.foriegnKey(mrTemp.getRegDocId()));
                    psTemp.setString("userName", DmServer.getSsnUserName(rss));
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                }
                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = "update  patientRegistration set"
                        + "         mrDocId = :docId "
                        + "         where regDocId = :regDocId";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", mrTemp.getDocId());
                psTemp.setString("regDocId", DmShared.foriegnKey(mrTemp.getRegDocId()));
                psTemp.executeUpdate();
                psTemp.close();
            }
        } catch (Exception e) {
            mrTemp = null;
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public static PatientMRID_Mdl getMRID(String mrDocId, Connection cnTemp) {
        String sqlQuery;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        PatientMRID_Mdl mrTemp = null;
        try {
            sqlQuery = "select  pm.docId, pm.recId, pm.recIdPrefix, pm.v_No, pm.v_Date, pm.v_Time, pm.isStatus, pm.v_Type, pm.regDocId, pm.uhid "
                    + "		from patientMRID pm "
                    + "		left join voucher_Type vt on vt.v_Type = pm.v_Type "
                    + "		where pm.docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrDocId).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp = new PatientMRID_Mdl();
                mrTemp.setDocId(DmShared.xNull(rsTemp.getString("docId")));
                mrTemp.setOpen(DmShared.iNull(rsTemp.getString("isStatus")).intValue() == 1);
                mrTemp.setVoucherType(DmShared.xNull(rsTemp.getString("v_Type")));
                mrTemp.getRecId().setCounter(DmShared.iNull(rsTemp.getString("v_No")).longValue());
                mrTemp.getRecId().setPrefix(DmShared.xNull(rsTemp.getString("recIdPrefix")));
                mrTemp.getRecId().setRecIdValue(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.setUhid(DmShared.xNull(rsTemp.getString("regDocId")));
                mrTemp.setRegDocId(DmShared.xNull(rsTemp.getString("uhid")));
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            mrTemp = null;
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

    public static PatientMRID_Mdl getLatestMRID(String regDocId, String UHIDdocId, Connection cnTemp) {
        String sqlQuery;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        PatientMRID_Mdl mrTemp = null;
        try {
            sqlQuery = "select  top 1 pm.docId, pm.recId, pm.recIdPrefix, pm.v_No, pm.v_Date, pm.v_Time, pm.isStatus, pm.v_Type, pm.regDocId, pm.uhid "
                    + "		from patientMRID pm "
                    + "		left join voucher_Type vt on vt.v_Type = pm.v_Type "
                    + "		where isStatus = 0 and (regDocId = :regDocId or uhid = :uhid)"
                    + "		order by pm.v_Date desc, pm.v_Time desc, vt.short_Name, pm.v_No desc, pm.recId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("regDocId", DmShared.iNull(regDocId).toString());
            psTemp.setString("uhid", DmShared.iNull(UHIDdocId).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp = new PatientMRID_Mdl();
                mrTemp.setDocId(DmShared.xNull(rsTemp.getString("docId")));
                mrTemp.setOpen(true);
                mrTemp.setVoucherType(DmShared.xNull(rsTemp.getString("v_Type")));
                mrTemp.getRecId().setCounter(DmShared.iNull(rsTemp.getString("v_No")).longValue());
                mrTemp.getRecId().setPrefix(DmShared.xNull(rsTemp.getString("recIdPrefix")));
                mrTemp.getRecId().setRecIdValue(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.setUhid(DmShared.xNull(rsTemp.getString("UHID")));
                mrTemp.setRegDocId(DmShared.xNull(rsTemp.getString("regDocID")));
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            mrTemp = null;
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

    public static PatientTreatmentId_Mdl getLatestTreatmentId(String regDocId, String departmentCode, String nursingStationCode, String roomCode, Connection cnTemp) {
        String sqlQuery, condition = "";
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        PatientTreatmentId_Mdl mrTemp = null;
        try {

            if (DmShared.iNull(roomCode).longValue() > 0) {
                condition = " and pti.roomCode = :roomCode";
            }
            if (DmShared.iNull(nursingStationCode).longValue() > 0) {
                condition += " and pti.nursingStationCode = :nsCode";
            }
            sqlQuery = "select  top 1 pti.docId, pti.recId, pti.recIdPrefix, pti.v_No, pti.v_Type, pti.regDocId"
                    + "		from patientTreatment pti "
                    + "		left join voucher_Type vt on vt.v_Type = pti.v_Type "
                    + "		where pti.regDocId = :regDocId and pti.departmentCode = :department " + condition
                    + "		order by pti.v_Date desc, vt.short_Name, pti.v_No desc, pti.recId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("regDocId", DmShared.iNull(regDocId).toString());
            psTemp.setString("department", DmShared.iNull(departmentCode).toString());
            if (DmShared.iNull(roomCode).longValue() > 0) {
                psTemp.setString("roomCode", DmShared.iNull(roomCode).toString());
            }
            if (DmShared.iNull(nursingStationCode).longValue() > 0) {
                psTemp.setString("nsCode", DmShared.iNull(nursingStationCode).toString());
            }
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp = new PatientTreatmentId_Mdl();
                mrTemp.setDocId(DmShared.xNull(rsTemp.getString("docId")));
                mrTemp.getRecId().setCounter(DmShared.iNull(rsTemp.getString("v_No")).longValue());
                mrTemp.getRecId().setPrefix(DmShared.xNull(rsTemp.getString("recIdPrefix")));
                mrTemp.getRecId().setRecIdValue(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.setRegDocId(DmShared.xNull(rsTemp.getString("regDocId")));
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            mrTemp = null;
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

    public static PatientTreatmentId_Mdl getTreatmentId(String trtDocId, Connection cnTemp) {
        String sqlQuery;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        PatientTreatmentId_Mdl mrTemp = null;
        try {
            sqlQuery = "select  pti.docId, pti.recId, pti.recIdPrefix, pti.v_No, pti.v_Type, pti.regDocId, nsm.code as nursingStationCode, "
                    + "		nsm.name as nursingStation, pti.roomCode, rm.roomNo, pti.departmentCode, dep.name as department, rm.roomCategoryCode "
                    + "		from patientTreatment pti "
                    + "		left join voucher_Type vt on vt.v_Type = pti.v_Type "
                    + "		left join roomMast rm on rm.code = pti.roomCode "
                    + "		left join nursingStationMast nsm on nsm.code = case when pti.nursingStationCode is null then rm.nursingStationCode else pti.nursingStationCode end "
                    + "		left join subGroup dep on dep.subCode = pti.departmentCode "
                    + "		where pti.docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(trtDocId).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp = new PatientTreatmentId_Mdl();
                mrTemp.setDocId(DmShared.xNull(rsTemp.getString("docId")));
                mrTemp.getRecId().setCounter(DmShared.iNull(rsTemp.getString("v_No")).longValue());
                mrTemp.getRecId().setPrefix(DmShared.xNull(rsTemp.getString("recIdPrefix")));
                mrTemp.getRecId().setRecIdValue(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.setRegDocId(DmShared.xNull(rsTemp.getString("regDocId")));
                mrTemp.setRoomCode(DmShared.xNull(rsTemp.getString("roomCode")));
                mrTemp.setRoomNo(DmShared.xNull(rsTemp.getString("roomNo")));
                mrTemp.setDepartmentCode(DmShared.xNull(rsTemp.getString("departmentCode")));
                mrTemp.setDepartmentName(DmShared.xNull(rsTemp.getString("department")));
                mrTemp.setNursingStation(DmShared.xNull(rsTemp.getString("nursingStation")));
                mrTemp.setNursingStationCode(DmShared.xNull(rsTemp.getString("nursingStationCode")));
                mrTemp.setRoomCategory(DmShared.xNull(rsTemp.getString("roomCategoryCode")));
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            mrTemp = null;
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

    public static String saveTreatmentId(PatientTreatmentId_Mdl mrTemp, boolean isNewRecord, Connection cnTemp, DmRSSPrivate rss) {
        String sqlQuery, entryPointDocId = "";
        DmPreparedStatement psTemp = null;
        boolean blnAdd = false;
        try {
            if (mrTemp != null && isNewRecord) {
                entryPointDocId = mrTemp.getEntryPointDocId();
                PatientTreatmentId_Mdl temp = getLatestTreatmentId(mrTemp.getRegDocId(), mrTemp.getDepartmentCode(), mrTemp.getNursingStationCode(), mrTemp.getRoomCode(), cnTemp);
                if (temp != null) {
//                    mrTemp = temp;
                    mrTemp.setDocId(temp.getDocId());
                    mrTemp.setRecId(temp.getRecId());
                }
                if (DmShared.iNull(mrTemp.getDocId()).longValue() == 0) {
                    blnAdd = true;
                    mrTemp.setVoucherType(DmServer.getVType(vrCategory_TRTID, cnTemp));
                    mrTemp.setDocId(DmServer.getDocId(mrTemp.getVoucherType(), DmServer.getSsnVoucherPrefix(rss), cnTemp, rss));
                    mrTemp.setRecId(DmServer.getRecId("patientTreatment", "docId", mrTemp.getDocId(), "recId", mrTemp.getRecId(), mrTemp.getVoucherDate(), mrTemp.getVoucherType(), DmServer.getSsnVoucherPrefix(rss), cnTemp, rss, true));
                }

                if (DmShared.iNull(mrTemp.getDocId()).longValue() == 0) {
                    throw new DmException(0, "Invalid Treatment ID.");
                }

                if (blnAdd) {
                    sqlQuery = "insert into patientTreatment (docId, v_Type, v_No, recIdPrefix, recId, v_Prefix, v_Date, regDocId, departmentCode, "
                            + "		nursingStationCode, roomCode, preparedBy, site_Code, u_EntDt)"
                            + "values (:docId, :vType, :vNo, :recIdPrefix, :recId, :vPrefix, :vDate, :regDocId, :department, :nsCode, :roomCode, "
                            + "		:userName, :siteCode, :uEntDt)";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", mrTemp.getDocId());
                    psTemp.setString("vType", mrTemp.getVoucherType());
                    psTemp.setLong("vNo", mrTemp.getRecId().getCounter());
                    psTemp.setString("vPrefix", DmServer.getSsnVoucherPrefix(rss));
                    psTemp.setString("recIdPrefix", mrTemp.getRecId().getPrefix());
                    psTemp.setString("recId", mrTemp.getRecId().getRecIdValue());
                    psTemp.setDate("vDate", DmServer.convertDate(mrTemp.getVoucherDate()));
                    psTemp.setString("department", DmShared.foriegnKey(mrTemp.getDepartmentCode()));
                    psTemp.setString("nsCode", DmShared.foriegnKey(mrTemp.getNursingStationCode()));
                    psTemp.setString("roomCode", DmShared.foriegnKey(mrTemp.getRoomCode()));
                    psTemp.setString("regDocId", DmShared.foriegnKey(mrTemp.getRegDocId()));
                    psTemp.setString("userName", DmServer.getSsnUserName(rss));
                    psTemp.setString("siteCode", DmServer.getSsnSiteCode(rss));
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                    psTemp.executeUpdate();
                    psTemp.close();
                }

                sqlQuery = "update  patientRegistration set trtDocId = :trtDocId where regDocId = :regDocId";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("trtDocId", DmShared.foriegnKey(mrTemp.getDocId()));
                psTemp.setString("regDocId", mrTemp.getRegDocId());
                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = "update  patientServiceDetail set trtDocId = :trtDocId where docId = :docId";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("trtDocId", DmShared.foriegnKey(mrTemp.getDocId()));
                psTemp.setString("docId", DmShared.iNull(entryPointDocId).toString());
                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = "update  patientReceipt set trtDocId = :trtDocId where docId = :docId";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("trtDocId", DmShared.foriegnKey(mrTemp.getDocId()));
                psTemp.setString("docId", DmShared.iNull(entryPointDocId).toString());
                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = "update  patientLimitApproval set trtDocId = :trtDocId where docId = :docId";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("trtDocId", DmShared.foriegnKey(mrTemp.getDocId()));
                psTemp.setString("docId", DmShared.iNull(entryPointDocId).toString());
                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = "update  patientPrescription set trtDocId = :trtDocId where docId = :docId";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("trtDocId", DmShared.foriegnKey(mrTemp.getDocId()));
                psTemp.setString("docId", DmShared.iNull(entryPointDocId).toString());
                psTemp.executeUpdate();
                psTemp.close();
            }

        } catch (Exception e) {
            mrTemp = null;
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return mrTemp.getDocId();
    }
}
