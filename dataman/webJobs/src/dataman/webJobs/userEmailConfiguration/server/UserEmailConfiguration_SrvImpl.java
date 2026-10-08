package dataman.webJobs.userEmailConfiguration.server;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.webJobs.userEmailConfiguration.shared.TxtHUsers_Mdl;
import dataman.webJobs.userEmailConfiguration.shared.UserEmailConfiguration_Srv;
import dataman.webJobs.userEmailConfiguration.shared.UserEmailConfiguration_Vld;
import dataman.webJobs.userEmailConfiguration.shared.GrdUserEmaiEdit_Mdl;
import dataman.webJobs.userEmailConfiguration.shared.DefaultValue_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserEmailConfiguration_SrvImpl extends DmRSSPrivate implements UserEmailConfiguration_Srv {

    @Override
    public DmResult<GrdUserEmaiEdit_Mdl> saveRecord(String userName, GrdUserEmaiEdit_Mdl mrTemp) {
        DmResult<GrdUserEmaiEdit_Mdl> dmrTemp = new DmResult<GrdUserEmaiEdit_Mdl>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String strSQL;
        ResultSet rsTemp = null;
        dmrTemp.setData(mrTemp);

        try {

            UserEmailConfiguration_Vld.chkSave(mrTemp);
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setConfirmPassword(DmServer.codify(mrTemp.getConfirmPassword()));

            mrTemp.setCc(mrTemp.getCc().replace(",", "|"));

            mrTemp.setBcc(mrTemp.getBcc().replace(",", "|"));

            strSQL = "Delete From userPLTConfig Where userName= :code and pltCode = :pltCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", userName);
            psTemp.setString("pltCode", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();

            strSQL = "INSERT INTO userPLTConfig (userName, pltCode, emailId, password, bcc, cc) "
                    + "VALUES (:userName, :pltCode, :emailId, :password ,:bcc ,:cc);";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("userName", userName);
            psTemp.setString("pltCode", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            psTemp.setString("emailId", DmShared.xNull(mrTemp.getEmail()));
            psTemp.setString("password", DmShared.xNull(mrTemp.getConfirmPassword()));
            psTemp.setString("bcc", DmShared.xNull(mrTemp.getBcc()));
            psTemp.setString("cc", DmShared.xNull(mrTemp.getCc()));

            psTemp.executeUpdate();
            psTemp.close();

            dmrTemp.setData(mrTemp);
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
    public DmResultPageing<List<TxtHUsers_Mdl>> getTxtHUser(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHUsers_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHUsers_Mdl> lstTemp = new ArrayList<>();
        TxtHUsers_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "";

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            sqlQuery = "Select User_Name as Name,Description,isAdmin as admin From UserMast  ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHUsers_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setDescription(DmShared.xNull(rsTemp.getString("Description")));
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
    public DmResult<List<GrdUserEmaiEdit_Mdl>> fillGrid(String userName) {
        DmResult<List<GrdUserEmaiEdit_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQry = "";
        List<GrdUserEmaiEdit_Mdl> data = new ArrayList<>();

        dmrTemp.setData(data);

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            sqlQry = "select  plt.pltCode as pltCode,plt.comp_Name as compName,upc.emailId as emailId, upc.bcc as bcc, upc.cc as cc "
                    + "from " + DmServer.dbCompany + ".dbo.productLicensedTo plt "
                    + "left join " + DmServer.dbCompany + ".dbo.userMast um on 1 = 1 "
                    + "left join " + DmServer.dbCompany + ".dbo.userPltConfig upc on upc.pltCode = plt.pltCode and upc.userName = um.user_Name "
                    + "where isNull(um.user_Name,:userName) = :userName ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQry);
            psTemp.setString("userName", userName);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                GrdUserEmaiEdit_Mdl grd = new GrdUserEmaiEdit_Mdl();
                grd.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("pltCode")));
                grd.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("compName")));
                grd.setEmail((DmShared.xNull(rsTemp.getString("emailId"))));
                grd.setBcc((DmShared.xNull(rsTemp.getString("bcc")).replace("|", ",")));
                grd.setCc((DmShared.xNull(rsTemp.getString("cc")).replace("|", ",")));

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

    @Override
    public DmResult<DefaultValue_Mdl> getDefaultValues() {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult();
        DefaultValue_Mdl defaultValue = new DefaultValue_Mdl();

        try {
            defaultValue.setUserName(DmServer.getSsnUserName(this));
            dmrTemp.setData(defaultValue);
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }

        return dmrTemp;
    }

}
