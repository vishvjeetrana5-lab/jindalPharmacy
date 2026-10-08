package dataman.accounts.ledgerGroup.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.accounts.ledgerGroup.shared.*;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface LedgerGroup_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResultPageing<List<String>> getSuggestion(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<LedgerGroup_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(LedgerGroup_Mdl mrTemp);

    public DmResult<String> deleteRecord(String strKey);

  public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);

}
