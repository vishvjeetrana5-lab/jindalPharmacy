package aarogya.selfToolkit.patientSchedule.server;

import aarogya.selfToolkit.patientSchedule.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PatientSchedule_SrvImpl extends DmRSSPrivate implements PatientSchedule_Srv {

    public DmResult<List<GrdSchedules_Mdl>> getScheduleGrid(String uhid) {
        DmResult<List<GrdSchedules_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null, rsTemp1 = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        List<GrdSchedules_Mdl> lstNode = new ArrayList();
        GrdSchedules_Mdl node;
        String sqlQuery;
        dmrTemp.setData(lstNode);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*====================================<  >====================================*/
            sqlQuery = "   select  ps.docId, ps.v_No, ps.v_Type, ps.v_Date, ps.recId, psm.name as scheduleName, ps.recId as scheduleNo  "
                    + "		from patientSchedule ps     "
                    + "		left join voucher_Type vt on vt.v_Type = ps.v_Type  "
                    + "		left join patientScheduleMast psm on psm.code = ps.scheduleCode     "
                    + "		where ps.uhid=:uhid and ps.isStatus = 0  "
                    + "                 order by ps.v_Date desc, vt.short_Name, ps.v_No desc, recId ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (DmShared.iNull(uhid).longValue() != 0) {
                psTemp.setString("uhId", DmShared.iNull(uhid).toString());

            }
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {

                node = new GrdSchedules_Mdl();
                node.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("docId")).toString());
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("scheduleName")));
                node.setScheduleNo(DmShared.xNull(rsTemp.getString("scheduleNo")));
                node.setVDate(DmShared.dNull(rsTemp.getDate("v_Date")));

                sqlQuery = " select  top 2 scheduleDate, doneDate  "
                        + "		from PatientScheduleDetail psd"
                        + "		where psd.docId= :docId "
                        + "		order by psd.doneDate desc, psd.scheduleDate  ";
                psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp1.setString("docId", DmShared.iNull(node.getSharedKeyFieldCode()).toString());
                rsTemp1 = psTemp1.executeQuery();
                while (rsTemp1.next()) {
                    if (rsTemp1.getRow() == 1) {
                        node.setLastDoneDate(DmShared.dNull(rsTemp1.getDate("doneDate")));
                        node.setLastScheduleDate(DmShared.dNull(rsTemp1.getDate("scheduleDate")));
                        if (DmShared.dNull(rsTemp1.getDate("doneDate")).equalsIgnoreCase("")) {
                            node.setNxtFollowupDate(DmShared.dNull(rsTemp1.getDate("scheduleDate")));
                        }
                    } else if (rsTemp1.getRow() == 2) {
                        node.setNxtFollowupDate(DmShared.dNull(rsTemp1.getDate("scheduleDate")));
                    }
                }
                lstNode.add(node);
                rsTemp1.close();
                psTemp1.close();

            }

            rsTemp.close();
            psTemp.close();

            dmrTemp.setData(lstNode);

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
}
