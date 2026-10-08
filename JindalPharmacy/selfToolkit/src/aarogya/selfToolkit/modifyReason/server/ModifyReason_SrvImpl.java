package aarogya.selfToolkit.modifyReason.server;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import aarogya.selfToolkit.modifyReason.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class ModifyReason_SrvImpl extends DmRSSPrivate implements ModifyReason_Srv {

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHReason(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT code, name FROM modifyReasonMast");
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
    public DmResult<List<GrdCancelReason_Mdl>> getEditResion(String docId, int sNo) {
        DmResult<List<GrdCancelReason_Mdl>> dmrTemp = new DmResult();
        GrdCancelReason_Mdl cancelReason;
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, dt, condition = "";

        List<GrdCancelReason_Mdl> cancelReasonList = new ArrayList<>();
        dmrTemp.setData(cancelReasonList);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (DmShared.iNull(sNo).longValue() != 0) {
                condition = " and sNo = " + sNo + " ";
            }

            sqlQuery = "select mr.docId,mr.v_SNo,mr.mode, mr.modifyReason AS remark, mrm.name AS reason, "
                    + " mr.u_Name AS userName, mr.u_EntDt  "
                    + " FROM modifyReason AS mr "
                    + " LEFT JOIN modifyReasonMast AS mrm "
                    + " ON mr.modifyReasonCode = mrm.code "
                    + " WHERE mr.docId = :docId " + condition
                    + " ORDER BY mr.u_EntDt DESC, mrm.name ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                cancelReason = new GrdCancelReason_Mdl();
                cancelReason.setDocId(DmShared.xNull(rsTemp.getString("docId")));
                cancelReason.setV_SNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                cancelReason.setMode(DmShared.xNull(rsTemp.getString("mode")));
                cancelReason.setModifyReason(DmShared.xNull(rsTemp.getString("reason")));
                cancelReason.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                cancelReason.setUserName(DmShared.xNull(rsTemp.getString("userName")));
                if (!DmShared.xNull(rsTemp.getString("u_EntDt")).equalsIgnoreCase("")) {
                    String date = DmShared.dNull(rsTemp.getDate("u_EntDt"));
                    Time time = rsTemp.getTime("u_EntDt");
                    dt = DmShared.dNull(date) + "/" + time;
                } else {
                    dt = "";
                }
                cancelReason.setDateTime(dt);
                cancelReasonList.add(cancelReason);
            }
            rsTemp.close();
            psTemp.close();

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
