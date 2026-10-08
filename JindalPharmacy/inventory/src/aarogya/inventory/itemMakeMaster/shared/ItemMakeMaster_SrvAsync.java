package aarogya.inventory.itemMakeMaster.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface ItemMakeMaster_SrvAsync {

    public void getNavigator(int entryType,AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getSuggestionCompanyName(DmHelper helper, boolean blnToEndOfFile,int entryType, AsyncCallback<DmResultPageing<List<String>>> result);

    public void getSuggestionShortName(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<String>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<ItemMakeMaster_Mdl>> result);

    public void deleteRecord(String strKey, AsyncCallback<DmResult<String>> result);

    public void saveRecord(ItemMakeMaster_Mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void getFind(DmHelper pageing, boolean blnToEndOfFile,int entryType, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

}
