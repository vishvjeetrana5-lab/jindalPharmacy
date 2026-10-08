package dataman.utility.changePassword.server;

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
import dataman.utility.changePassword.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import java.sql.Connection;
import java.sql.ResultSet;

public class ChangePassword_SrvImpl extends DmRSSPrivate implements ChangePassword_Srv {

    @Override
    public DmResult<String> saveRecord(ChangePassword_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String strSQL;
        ResultSet rsTemp = null;
        dmrTemp.setData("");

        try {
            ChangePassword_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            spBegin = new DmSavePoint(cnTemp);

            strSQL = "SELECT count(*) as Cnt FROM usermast WHERE passwd = convert(varchar(32),HashBytes('MD5',CONCAT( :user_name , :userPass)),2) AND user_name = :user_name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("user_name", DmServer.getSsnUserName(this).toLowerCase());
            psTemp.setString("userPass", DmShared.xNull(mrTemp.getOldPassword()));
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("Cnt") <= 0) {
                throw new UnsupportedOperationException("Old Password Is Wrong.");
            }
            rsTemp.close();
            psTemp.close();

            strSQL = "Update UserMast Set PassWd=convert(varchar(32),HashBytes('MD5',:passWd),2) where user_name= :user_name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("PassWd", DmServer.getSsnUserName(this).toLowerCase() + mrTemp.getNewPassword());
            psTemp.setString("user_name", DmServer.getSsnUserName(this).toLowerCase());
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

}
