package dataman.dmToolkit.overHead.server;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.overHead.shared.AccountHelp_Mdl;
import dataman.dmToolkit.overHead.shared.OverHead_Mdl;
import dataman.dmToolkit.overHead.shared.OverHead_Srv;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OverHead_SrvImpl extends DmRSSPrivate implements OverHead_Srv {

    @Override
    public DmResultPageing<List<AccountHelp_Mdl>> getTxtHPostingAC(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<AccountHelp_Mdl>> rstTemp = new DmResultPageing<>();
        List<AccountHelp_Mdl> lstTemp = new ArrayList<>();
        AccountHelp_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("Select SubCode,Name,ManualCode From Subgroup");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new AccountHelp_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("SubCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("ManualCode")));

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
    public DmResult<List<OverHead_Mdl>> getOverHead(String vType, String vDate, String overHeadTableName, String overHeadTableNameMA) {
        DmResult<List<OverHead_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (DmShared.xNull(overHeadTableName).trim().equalsIgnoreCase("") && DmShared.xNull(overHeadTableNameMA).trim().equalsIgnoreCase("")) {
                dmrTemp.setData(OverHead_Server.getOverHead(vType, vDate, DmServer.getSsnSiteCode(this), "", cnTemp));
            } else if ((!DmShared.xNull(overHeadTableName).trim().equalsIgnoreCase("")) && (!DmShared.xNull(overHeadTableNameMA).trim().equalsIgnoreCase(""))) {
                dmrTemp.setData(OverHead_Server.getOverHead(vType, vDate, DmServer.getSsnSiteCode(this), "", overHeadTableName, overHeadTableNameMA, cnTemp));
            } else {
                throw new DmException(0, "Invalid request for getOverHead.");
            }
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
