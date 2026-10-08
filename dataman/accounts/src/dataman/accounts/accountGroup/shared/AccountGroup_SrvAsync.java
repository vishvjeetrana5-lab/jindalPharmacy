package dataman.accounts.accountGroup.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface AccountGroup_SrvAsync {

    public void getNavigator(AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getSuggestion(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<String>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<AccountGroup_Mdl>> result);

    public void deleteRecord(String strKey, AsyncCallback<DmResult<String>> result);

    public void saveRecord(AccountGroup_Mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void getTxtHGroupUnder(DmHelper pageing, boolean blnToEndOfFile, String strCode, AsyncCallback<DmResultPageing<List<TxtHGroupUnder_Mdl>>> result);

    public void getTxtHMainGroup(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHNature(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getFind(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void exportCSV(String sessionId, AsyncCallback<DmResult<KeyFieldExtended>> result);
}
