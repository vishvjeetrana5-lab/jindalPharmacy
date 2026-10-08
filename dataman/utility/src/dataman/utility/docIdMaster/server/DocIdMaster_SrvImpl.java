package dataman.utility.docIdMaster.server;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.utility.docIdMaster.shared.DocIdMaster_Mdl;
import dataman.utility.docIdMaster.shared.DocIdMaster_Srv;
import dataman.utility.docIdMaster.shared.Find_Mdl;
import dataman.utility.docIdMaster.shared.GrdDocIdMaster_Mdl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DocIdMaster_SrvImpl extends DmRSSPrivate implements DocIdMaster_Srv {

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator() {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            rsTemp = cnTemp.createStatement().executeQuery("SELECT DISTINCT  CONCAT(Site_Code , '|' , V_Prefix) AS SearchCode FROM DocIdMaster ORDER BY CONCAT(Site_Code , '|' , V_Prefix)");

            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("SearchCode")));
                lstNavigator.add(lstNavigatorTmp);
            }
            dmrTemp.setStatus(true);
            rsTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
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
    public DmResult<DocIdMaster_Mdl> getMoveRec(String strKey) {
        DmResult<DocIdMaster_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String strSQL;
        List<GrdDocIdMaster_Mdl> grdListTemp = new ArrayList<>();
        DocIdMaster_Mdl mrTemp = new DocIdMaster_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            strSQL = "SELECT VT.V_Type,VT.Description,DM.V_Prefix,VT.Short_Name, "
                    + "isNull(DM.serialNo,0) AS serialNo,SM.Name AS SiteName, "
                    + "SM.Code AS SiteCode "
                    + "FROM DocIdMaster DM "
                    + "LEFT JOIN Voucher_Type VT ON VT.V_Type = DM.V_Type "
                    + "LEFT JOIN SiteMast SM ON DM.Site_Code = SM.Code "
                    + "WHERE CONCAT(DM.Site_Code , '|' ,  DM.V_Prefix) = :code order by Description";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", strKey);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("SiteCode")));
                mrTemp.setSiteCode(DmShared.xNull(rsTemp.getString("SiteCode")));
                mrTemp.setSiteName(DmShared.xNull(rsTemp.getString("SiteName")));
                mrTemp.setVoucherPrefix(DmShared.xNull(rsTemp.getString("V_Prefix")));

                GrdDocIdMaster_Mdl grdData = new GrdDocIdMaster_Mdl();
                grdData.setSharedKeyFieldCode(rsTemp.getString("V_Type"));
                grdData.setSharedKeyFieldName(rsTemp.getString("Short_Name"));
                grdData.setVoucherDescription(rsTemp.getString("Description"));
                grdData.setVoucherSerise(rsTemp.getString("serialNo"));

                grdListTemp.add(grdData);
            }
            mrTemp.setGrdData(grdListTemp);
            psTemp.close();
            rsTemp.close();
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSite(DmHelper helper, boolean blnToEndOfFile, String strCode) {
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
            sqlWithParam.setSQL("SELECT SM.Code,SM.Name FROM SiteMast SM  ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
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
    public DmResultPageing<List<Find_Mdl>> getFind(String vPrefix, DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL(" SELECT CONCAT(SM.Code ,'|', DM.V_Prefix) AS SearchCode,VT.V_Type,VT.Description,DM.V_Prefix,DM.serialNo, "
                    + " SM.Name AS Site_Name,SM.Code  "
                    + " FROM DocIdMaster DM  "
                    + " LEFT JOIN SiteMast SM ON DM.Site_Code = SM.Code "
                    + " LEFT JOIN Voucher_Type VT ON DM.V_Type = VT.V_Type "
                    + " WHERE DM.V_Prefix = :v_Prefix ");

            sqlWithParam.setString("v_Prefix", vPrefix);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("SearchCode")));
                listNode.setSerachCode(DmShared.xNull(rsTemp.getString("SearchCode")));
                listNode.setVoucherType(DmShared.xNull(rsTemp.getString("V_Type")));
                listNode.setVoucherDescription(DmShared.xNull(rsTemp.getString("Description")));
                listNode.setPrefix(DmShared.xNull(rsTemp.getString("V_Prefix")));
                listNode.setVoucherSerise(DmShared.xNull(rsTemp.getString("serialNo")));
                listNode.setSiteName(DmShared.xNull(rsTemp.getString("Site_Name")));
                listNode.setSiteCode(DmShared.xNull(rsTemp.getString("Code")));
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
    public DmResult<List<GrdDocIdMaster_Mdl>> fillGrid(String siteName, String vprefix) {
        DmResult<List<GrdDocIdMaster_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<GrdDocIdMaster_Mdl> data = new ArrayList<>();

        dmrTemp.setData(data);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, "SELECT VT.V_Type,VT.Description,VT.Short_Name "
                    + "FROM Voucher_Type VT "
                    + "LEFT JOIN DocIdMaster DM ON VT.V_Type = DM.V_Type AND "
                    + "DM.Site_Code = :siteCode AND "
                    + "DM.V_Prefix = :vPrefix "
                    + "WHERE isNull(DM.V_Type,'') = '' "
                    + "ORDER BY VT.V_Type ");
            psTemp.setString("siteCode", siteName);
            psTemp.setString("vPrefix", vprefix);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                GrdDocIdMaster_Mdl grd = new GrdDocIdMaster_Mdl();
                grd.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("V_Type")));
                grd.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Short_Name")));
                grd.setVoucherDescription(DmShared.xNull(rsTemp.getString("Description")));
                grd.setVoucherSerise(DmShared.xNull("0"));

                data.add(grd);
            }
            dmrTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
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
}
