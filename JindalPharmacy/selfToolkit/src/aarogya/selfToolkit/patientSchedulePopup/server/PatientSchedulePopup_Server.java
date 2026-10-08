package aarogya.selfToolkit.patientSchedulePopup.server;

import aarogya.selfToolkit.patientSchedulePopup.shared.PatientSchedulePopup_Mdl;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Calendar;

public class PatientSchedulePopup_Server {

    public static String saveRecord(PatientSchedulePopup_Mdl mrTemp, String voucherPrefix, Connection cnTemp, DmRSSPrivate rss) {
        DmPreparedStatement psTemp = null, psTemp1 = null;
        String sqlQuery, siteCode, scheduleDate, shdlDoc = "";
        String[] currentDateTime;
        ResultSet rsTemp = null;
        int vsno = 0;
        Calendar cal;
        try {
            siteCode = DmServer.getSsnSiteCode(rss);
            if (DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue() == 0) {
                currentDateTime = DmServer.getServerDateTime();
                if (DmShared.dNull(mrTemp.getVoucherDate()).trim().equalsIgnoreCase("")) {
                    mrTemp.setVoucherDate(currentDateTime[0]);
                }
                if (DmShared.xNull(mrTemp.getVoucherTime()).trim().equalsIgnoreCase("")) {
                    mrTemp.setVoucherTime(currentDateTime[1]);
                }
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, rss));
                mrTemp.setRecIdSchedulePopup(DmServer.getRecId("patientSchedule", "docId", mrTemp.getSharedKeyFieldCode(), "recId", mrTemp.getRecIdSchedulePopup(), mrTemp.getVoucherDate(), mrTemp.getVoucherTypeCode(), voucherPrefix, cnTemp, rss, true));
                mrTemp.setSharedKeyFieldName(mrTemp.getRecIdSchedulePopup().getRecIdValue());
            }

            sqlQuery = "insert into patientSchedule (docId, recId, v_No, v_Date, v_Type, uhid, scheduleCode, recIdPrefix, v_Prefix, site_Code, "
                    + "         u_EntDt, preparedBy) "
                    + " values (:docId, :recId, :v_No, :v_Date, :v_Type, :uhid, :scheduleCode, :recIdPrefix, :v_Prefix, :site_Code, :u_EntDt, "
                    + "         :preparedBy) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", mrTemp.getSharedKeyFieldCode());
            psTemp.setString("recId", mrTemp.getSharedKeyFieldName());
            psTemp.setLong("v_No", mrTemp.getRecIdSchedulePopup().getCounter());
            psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
            psTemp.setString("v_Type", mrTemp.getVoucherTypeCode());
            psTemp.setString("uhid", DmShared.foriegnKey(mrTemp.getDocIdUHID()));
            psTemp.setString("scheduleCode", DmShared.foriegnKey(mrTemp.getScheduleCode()));
            psTemp.setString("recIdPrefix", mrTemp.getRecIdSchedulePopup().getPrefix());
            psTemp.setString("v_Prefix", voucherPrefix);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(rss));
            psTemp.setString("preparedBy", DmServer.getSsnUserName(rss));
            psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "select  description, cycleTime, days, printSno, guiTemplateCode, machineCode, consultantCode "
                    + "         from patientScheduleProcedure "
                    + "         where code=:code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", mrTemp.getScheduleCode());
            rsTemp = psTemp.executeQuery();
            vsno = 0;
            while (rsTemp.next()) {
                cal = Calendar.getInstance();
                cal.setTime(DmShared.convertDate(mrTemp.getScheduleDate()));
                if (DmShared.iNull(rsTemp.getString("days")).intValue() != 1) {
                    cal.add(Calendar.DATE, DmShared.iNull(rsTemp.getString("days")).intValue());
                }
                scheduleDate = DmShared.dNull(cal.getTime());
                vsno++;

                sqlQuery = "insert into patientScheduleDetail (docId, v_SNo, description, scheduleDate, printSNo, cycleTime,  guiConfigCode, "
                        + "         machineCode, consultantCode, u_Name, u_EntDt) "
                        + " values (:docId, :vSno, :description, :scheduleDate, :printSno, :cycleTime, :guiConfigCode, :machineCode, :consultantCode, "
                        + "         :u_Name, :u_EntDt)";
                psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp1.setString("docId", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                psTemp1.setString("vSno", DmShared.iNull(vsno).toString());
                psTemp1.setString("description", DmShared.xNull(rsTemp.getString("description")));
                psTemp1.setDate("scheduleDate", DmServer.convertDate(scheduleDate));
                psTemp1.setString("printSno", DmShared.iNull(rsTemp.getString("printSNo")).toString());
                psTemp1.setString("cycleTime", DmServer.vNull(rsTemp.getString("cycleTime")));
                psTemp1.setString("guiConfigCode", DmShared.foriegnKey(getGUIConfigCode(rsTemp.getString("guiTemplateCode"), siteCode, cnTemp)));
                psTemp1.setString("machineCode", DmShared.foriegnKey(rsTemp.getString("machineCode")));
                psTemp1.setString("consultantCode", DmShared.foriegnKey(rsTemp.getString("consultantCode")));
                psTemp1.setString("u_Name", DmServer.getSsnUserName(rss));
                psTemp1.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp1.executeUpdate();
                psTemp1.close();
            }

            psTemp.close();
            rsTemp.close();
            shdlDoc = mrTemp.getSharedKeyFieldCode();
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
        return shdlDoc;
    }

    private static String getGUIConfigCode(String guiTemplateCode, String siteCode, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, guiConfigCode = "";
        try {
            sqlQuery = " Select  gc.code  as guiTemplateCode     "
                    + " from guiConfig gc    "
                    + " left join guiConfigApplicableTo gcat on gc.code = gcat.code     "
                    + " where gcat.guiTemplateCode = :guiTemplateCode and gcat.site_code = :siteCode  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", siteCode);
            psTemp.setString("guiTemplateCode", guiTemplateCode);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                guiConfigCode = DmShared.xNull(rsTemp.getString("guiTemplateCode"));
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
        return guiConfigCode;
    }
}
