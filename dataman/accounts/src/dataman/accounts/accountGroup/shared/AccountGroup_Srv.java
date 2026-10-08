package dataman.accounts.accountGroup.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface AccountGroup_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResultPageing<List<String>> getSuggestion(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<AccountGroup_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(AccountGroup_Mdl mrTemp);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<TxtHGroupUnder_Mdl>> getTxtHGroupUnder(DmHelper pageing, boolean blnToEndOfFile, String strCode);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHMainGroup(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHNature(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<KeyFieldExtended> exportCSV(String sessionId);
}
