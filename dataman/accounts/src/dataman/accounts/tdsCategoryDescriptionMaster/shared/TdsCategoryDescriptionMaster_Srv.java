package dataman.accounts.tdsCategoryDescriptionMaster.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface TdsCategoryDescriptionMaster_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResultPageing<List<String>> getSuggestionDescription(DmHelper helper, boolean blnToEndOfFile);
    public DmResultPageing<List<String>> getSuggestionManualCode(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<TdsCategoryDescriptionMaster_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(TdsCategoryDescriptionMaster_Mdl mrTemp);

    public DmResult<String> deleteRecord(String strKey);


    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);
}
