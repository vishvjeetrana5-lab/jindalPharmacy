package redirect.changePassword.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;

public interface ChangePassword_SrvAsync {

    public void saveRecord(ChangePassword_Mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void getUserDetail(String strName, String strKey, AsyncCallback<DmResult<ChangePassword_Mdl>> result);

}
