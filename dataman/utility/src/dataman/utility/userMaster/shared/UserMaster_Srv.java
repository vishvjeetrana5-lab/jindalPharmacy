package dataman.utility.userMaster.shared;

import dataman.dmBase.self.shared.UserMaster_Mdl;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface UserMaster_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResultPageing<List<String>> getSuggestion(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<UserMaster_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(UserMaster_Mdl mrTemp);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHAdministrator(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHNature(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHRole(DmHelper pageing, boolean blnToEndOfFile, boolean isResetCall, String userName);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<List<GrdLinkedUser_Mdl>> fillGridLinkedUser(String condition);

    public DmResult<String> resetPassword(String manualCode);

    public DmResultPageing<List<GrdLinkedUser_Mdl>> getTxtHLinkedUser(DmHelper pageing, String userName, boolean blnToEndOfFile);
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile);
    
}
