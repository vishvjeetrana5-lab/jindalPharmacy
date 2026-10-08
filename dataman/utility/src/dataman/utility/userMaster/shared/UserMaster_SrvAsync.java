package dataman.utility.userMaster.shared;

import dataman.dmBase.self.shared.UserMaster_Mdl;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface UserMaster_SrvAsync {

    public void getNavigator(AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getSuggestion(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<String>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<UserMaster_Mdl>> result);

    public void deleteRecord(String strKey, AsyncCallback<DmResult<String>> result);

    public void saveRecord(UserMaster_Mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void getTxtHAdministrator(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHNature(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHRole(DmHelper pageing, boolean blnToEndOfFile, boolean isResetCall, String userName, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getFind(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getTxtHLinkedUser(DmHelper pageing, String userName, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<GrdLinkedUser_Mdl>>> result);

    public void fillGridLinkedUser(String condition, AsyncCallback<DmResult<List<GrdLinkedUser_Mdl>>> result);

    public void resetPassword(String manualCode, AsyncCallback<DmResult<String>> result);
    
    public void getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);
}
