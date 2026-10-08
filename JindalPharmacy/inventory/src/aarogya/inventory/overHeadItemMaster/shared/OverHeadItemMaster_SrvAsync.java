package aarogya.inventory.overHeadItemMaster.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import java.util.List;

public interface OverHeadItemMaster_SrvAsync {

    public void getTxtHHSN(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHHSN_Mdl>>> result);

    public void getSuggestionItem(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<String>>> result);

    public void getTxtHGroup(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHCategory(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHCategory_Mdl>>> result);

    public void getSKU(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Sku_Mdl>>> result);

    public void getNavigator(AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<OverHeadItemMaster_mdl>> result);

    public void deleteRecord(String strKey, AsyncCallback<DmResult<String>> result);

    public void saveRecord(OverHeadItemMaster_mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void getFind(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getYesNo(DmHelper helper, boolean blnToEndOfFile, boolean isActiveCall, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

}
