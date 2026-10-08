package redirect.changePassword.server;

import redirect.changePassword.shared.ChangePassword_Mdl;
import redirect.changePassword.shared.ChangePassword_Srv;
import redirect.changePassword.shared.ChangePassword_Vld;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPublic;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import java.sql.Connection;
import java.sql.ResultSet;

public class ChangePassword_SrvImpl extends DmRSSPublic implements ChangePassword_Srv {

    @Override
    public DmResult<String> saveRecord(ChangePassword_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData("");

        try {
            ChangePassword_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "SELECT code,username FROM linkVerification WHERE code=:code and userName=:name AND expiryDate > :uEntDt ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
            psTemp.setString("name", mrTemp.getSharedKeyFieldName());
            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            rsTemp = psTemp.executeQuery();
            if (!rsTemp.next()) {
                throw new UnsupportedOperationException("Your link has been expired");
            }
            if (DmShared.xNull(mrTemp.getSharedKeyFieldName()).equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Your link has been expired");
            }

            sqlQuery = "Update UserMast Set PassWd=convert(varchar(32),HashBytes('MD5',:passWd),2) where user_name= :user_name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("PassWd", mrTemp.getSharedKeyFieldName().toLowerCase() + mrTemp.getNewPassword());
            psTemp.setString("user_name", mrTemp.getSharedKeyFieldName().toLowerCase());
            psTemp.executeUpdate();

            sqlQuery = " delete from  linkVerification where code=:code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();

            spBegin.commit();
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
    public DmResult<ChangePassword_Mdl> getUserDetail(String strName, String strKey) {
        DmResult<ChangePassword_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        ChangePassword_Mdl mrTemp = new ChangePassword_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            sqlQuery = "select  lv.code,um.user_name,un.manualCode as natureCode "
                    + "         from userMast um "
                    + "         left join userNature un on um.nature = un.code "
                    + "         left join linkVerification lv on um.user_name = lv.userName and lv.code = :code and lv.expiryDate > :uEntDt "
                    + "         where um.user_Name = :name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            psTemp.setString("name", strName);
            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setUserName(DmShared.xNull(rsTemp.getString("user_name")));
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setUserNatureCode(DmShared.xNull(rsTemp.getString("natureCode")));
            } else {
                throw new UnsupportedOperationException("Your link has been expired.");
            }

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
}
