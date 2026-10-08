package aarogya.pharmacy.wholeSaleSettings.server;

import aarogya.pharmacy.wholeSaleSettings.shared.DefaultValue_Mdl;
import aarogya.pharmacy.wholeSaleSettings.shared.Find_Mdl;
import aarogya.pharmacy.wholeSaleSettings.shared.WholeSaleSetting_Mdl;
import aarogya.pharmacy.wholeSaleSettings.shared.WholeSaleSetting_Srv;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmSQLWithParam;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class WholeSaleSetting_SrvImpl extends DmRSSPrivate implements WholeSaleSetting_Srv {

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCentralStore(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL(" select sg.name AS Name , sg.subCode AS Code "
                    + "           from subGroup  sg \n"
                    + "           left join subGroupNature sgn on sgn.code = sg.nature \n"
                    + "		  left join subGroupNature sgn1 on sgn1.code = sg.filterNature\n"
                    + "           where sgn1.manualCode='DP' and   sgn.manualCode in ('PH','CM')");
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
    public DmResult<WholeSaleSetting_Mdl> getMoveRec(String strKey) {
        DmResult<WholeSaleSetting_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        WholeSaleSetting_Mdl mrTemp = new WholeSaleSetting_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select env.pharmacyCentralStore AS pharmacyCentralStoreCode, sg.name as pharmacyCentralStoreName, env.ewayLimit "
                    + "		from enviro AS env "
                    + "		left join subgroup sg on env.pharmacyCentralStore = sg.subcode  "
                    + "         WHERE env.site_Code = :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setPharmacyCentralStoreCode(DmShared.xNull(rsTemp.getString("pharmacyCentralStoreCode")));
                mrTemp.setPharmacyCentralStoreName(DmShared.xNull(rsTemp.getString("pharmacyCentralStoreName")));
                mrTemp.setEwayBillLimit(DmServer.vNull(rsTemp.getString("ewayLimit"), "0.00"));

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

    @Override
    public DmResult<String> saveRecord(WholeSaleSetting_Mdl mrTemp, String topControlMode) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData("");

        try {

//             EnvironmentSettingSite_Vld.chkSave(mrTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "SELECT  site_Code FROM enviro where site_Code = :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            if (!rsTemp.next()) {
                sqlQuery = " INSERT INTO enviro(site_Code) VALUES(:site_Code) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
                psTemp.executeUpdate();
                psTemp.close();
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "update enviro  SET "
                    + " pharmacyCentralStore  = :pharmacyCentralStore ,"
                    + " ewayLimit  = :ewayLimit ,"
                    + " u_EntDt = :u_EntDt"
                    + " where site_Code= :site_Code ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp.setString("pharmacyCentralStore", DmShared.xNull(mrTemp.getPharmacyCentralStoreCode()));
            psTemp.setString("ewayLimit", DmServer.vNull(mrTemp.getEwayBillLimit(), "0.00"));
            psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());

            psTemp.executeUpdate();
            psTemp.close();
            dmrTemp.setData(DmServer.getSsnSiteCode(this));

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
    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DmResult<DefaultValue_Mdl> getDefaultValue() {
        DmResultPageing<DefaultValue_Mdl> dmrTemp = new DmResultPageing();
        DefaultValue_Mdl mrTemp = new DefaultValue_Mdl();
        dmrTemp.setData(mrTemp);
        String siteCode = "";

        try {

            siteCode = DmServer.getSsnSiteCode(this);

            mrTemp.setSharedKeyFieldCode(DmShared.xNull(siteCode));

            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {

            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }
}
