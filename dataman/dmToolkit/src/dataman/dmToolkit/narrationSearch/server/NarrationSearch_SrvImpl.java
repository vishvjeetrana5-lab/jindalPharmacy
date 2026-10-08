package dataman.dmToolkit.narrationSearch.server;

import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.narrationSearch.shared.NarrationSearch_Mdl;
import dataman.dmToolkit.narrationSearch.shared.NarrationSearch_Srv;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class NarrationSearch_SrvImpl extends DmRSSPrivate implements NarrationSearch_Srv {

    @Override
    public DmResultPageing<List<NarrationSearch_Mdl>> getNarration(String searchValue, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<NarrationSearch_Mdl>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<NarrationSearch_Mdl> lstTemp = new ArrayList<NarrationSearch_Mdl>();
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery;
        rstTemp.setData(lstTemp);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT nm.code, nm.name FROM narrationMast nm";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                NarrationSearch_Mdl objListTemp = new NarrationSearch_Mdl();
                objListTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                objListTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(objListTemp);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rsTemp.close();
            psTemp.close();

            cnTemp.close();
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
}
