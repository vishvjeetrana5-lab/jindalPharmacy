package aarogya.inventory.itemMakeMaster.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface ItemMakeMaster_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator(int entryType);

    public DmResultPageing<List<String>> getSuggestionCompanyName(DmHelper helper, boolean blnToEndOfFile,int entryType);

    public DmResultPageing<List<String>> getSuggestionShortName(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<ItemMakeMaster_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(ItemMakeMaster_Mdl mrTemp);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile,int entryType);

}
