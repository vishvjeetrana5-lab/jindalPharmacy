package dataman.dmToolkit.dmGUI.server;

import dataman.dmToolkit.dmGUI.shared.DmGUI_Mdl;
import dataman.dmToolkit.dmGUI.shared.DmGUI_Srv;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DmGUI_SrvImpl extends DmRSSPrivate implements DmGUI_Srv {

    @Override
    public DmResult<DmGUI_Mdl> getGUI(String guiConfigCode, String guiTemplateCode) {
        DmResult<DmGUI_Mdl> rstTemp = new DmResult<>();
        DmGUI_Mdl gui;
        Connection cnTemp = null;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (DmShared.iNull(guiConfigCode).longValue() == 0) {
                gui = DmGUI_Server.getGUIViaTemplateCode(guiTemplateCode, DmServer.getSsnSiteCode(this), cnTemp);
            } else {
                gui = DmGUI_Server.getGUIViaConfigCode(guiConfigCode, "", DmServer.getSsnSiteCode(this), cnTemp);
            }
            cnTemp.close();

            rstTemp.setData(gui);
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getHelp(byte[] helpSQLQuery, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        rstTemp.setData(lstTemp);

        try {
            sqlWithParam.setSQL((new String(helpSQLQuery)).trim());
            sqlWithParam.setSQL(sqlWithParam.getSQL().replace(";", ""));
            if (sqlWithParam.getSQL().split(" ")[0].equalsIgnoreCase("select")) {
                cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

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
            }
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
    public DmResult<DmGUI_Mdl> getCopyGUIDetails(String guiCodeFrom, String docIdGUIFrom) {
        DmResult<DmGUI_Mdl> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmGUI_Mdl mrTemp = new DmGUI_Mdl();

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*====================================< Get GUI From Details >====================================*/
            mrTemp = DmGUI_Server.getGUIViaConfigCode(guiCodeFrom, docIdGUIFrom, DmServer.getSsnSiteCode(this), cnTemp);

            cnTemp.close();
            rstTemp.setData(mrTemp);
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return rstTemp;
    }

}
