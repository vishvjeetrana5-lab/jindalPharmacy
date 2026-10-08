package dataman.dmToolkit.self.server;

import dataman.dmBase.dmSession.server.DmRSS;
import dataman.dmBase.self.shared.UserMaster_Mdl;
import java.sql.Connection;

public abstract class ManageUser_Abstract {

    public abstract void saveUser(UserMaster_Mdl mrTemp, DmRSS rssTemp, Connection cnTemp) throws Exception;

    public abstract void sendMail(String userName, DmRSS rssTemp) throws Exception;

    public abstract void deleteUser(String userName, Connection cnTemp) throws Exception;

    public abstract void saveRole(String userName, String roleName, Connection cnTemp) throws Exception;
}
