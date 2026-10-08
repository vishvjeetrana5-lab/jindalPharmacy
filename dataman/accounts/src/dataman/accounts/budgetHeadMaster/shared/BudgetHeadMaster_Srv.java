package dataman.accounts.budgetHeadMaster.shared;

import dataman.accounts.budgetHeadMaster.shared.*;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface BudgetHeadMaster_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResultPageing<List<String>> getSuggestion(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<BudgetHeadMaster_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(BudgetHeadMaster_Mdl mrTemp);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHHeadUnder(DmHelper pageing, boolean blnToEndOfFile, String strCode);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);
}
