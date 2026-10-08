package redirect.changePassword.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;

public interface ChangePassword_Srv extends RemoteService {

    public DmResult<String> saveRecord(ChangePassword_Mdl mrTemp);

    public DmResult<ChangePassword_Mdl> getUserDetail(String strName, String strKey);

}
