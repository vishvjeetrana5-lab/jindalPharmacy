package dataman.dmToolkit.manageSite.server;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmToolkit.manageSite.shared.GridSite_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.manageSite.shared.ManageSite_Srv;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import java.sql.Connection;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;

public class ManageSite_SrvImpl extends DmRSSPrivate implements ManageSite_Srv {

    @Override
    public DmResult<List<GridSite_Mdl>> getSiteList(String tableName, String keyFieldName, String keyFieldValue, String siteFieldName) {
        DmResultPageing<List<GridSite_Mdl>> rstTemp = new DmResultPageing<>();
        List<GridSite_Mdl> data = new ArrayList<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlWithParam;
        try {
            rstTemp.setData(data);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam = "SELECT sm.name,sm.code FROM " + tableName + " sl "
                    + " LEFT JOIN sitemast sm ON sl." + siteFieldName + "=sm.code "
                    + " WHERE sl." + keyFieldName + " = :code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlWithParam);
            psTemp.setString("code", keyFieldValue);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                GridSite_Mdl fld = new GridSite_Mdl();
                fld.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                fld.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                data.add(fld);
            }
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
    public DmResult<String> saveRecord(List<GridSite_Mdl> mrTemp, String tableName, String keyFieldName, String keyFieldValue, String siteFieldName) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String strSQL;
        ResultSet rsTemp = null;

        dmrTemp.setData("");

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            if (DmShared.xNull(tableName).equalsIgnoreCase("") || DmShared.xNull(keyFieldName).equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Parameters Name is Blank");
            }

            strSQL = "DELETE FROM " + tableName + " where " + keyFieldName + " = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", DmShared.xNull(keyFieldValue));
            psTemp.executeUpdate();
            psTemp.close();

            for (int i = 0; i < mrTemp.size(); i++) {
                if (!DmShared.xNull(mrTemp.get(i).getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                    strSQL = "Insert Into " + tableName + "(" + keyFieldName + " ," + siteFieldName + ") Values  "
                            + " ( :code ,:name ) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                    psTemp.setString("code", DmShared.xNull(keyFieldValue));
                    psTemp.setString("name", DmShared.xNull(mrTemp.get(i).getSharedKeyFieldCode()));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

            spBegin.commit();
            dmrTemp.setData(keyFieldValue);
            dmrTemp.setStatus(true);
            cnTemp.close();
        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage() + e.toString()));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSiteList(DmHelper helper, boolean blnToEndOfFile, String siteList) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "";

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT CODE,NAME FROM sitemast WHERE CODE NOT IN (" + DmServer.getParamList(":siteList", siteList) + ")");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            psTemp.setStringList("siteList", siteList);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
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
    public DmResult<String> deleteSiteList(String tableName, String keyFieldName, String keyFieldValue, String siteFieldName, String siteFieldValue) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String strSQL;
        ResultSet rsTemp = null;
        dmrTemp.setData(keyFieldValue);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            strSQL = " Delete From " + tableName + " where " + keyFieldName + " =:code and " + siteFieldName + " =:sCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", keyFieldValue);
            psTemp.setString("sCode", siteFieldValue);
            psTemp.executeUpdate();
            psTemp.close();

            spBegin.commit();
            dmrTemp.setStatus(true);
            cnTemp.close();
            psTemp.close();
            rsTemp.close();
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

}
