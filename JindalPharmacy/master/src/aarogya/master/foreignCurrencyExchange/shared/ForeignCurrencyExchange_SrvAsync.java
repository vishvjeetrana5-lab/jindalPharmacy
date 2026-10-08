package aarogya.master.foreignCurrencyExchange.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface ForeignCurrencyExchange_SrvAsync {

    public void getNavigator(AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<ForeignCurrencyExchange_Mdl>> result);

    public void deleteRecord(String strKey, AsyncCallback<DmResult<String>> result);

    public void saveRecord(ForeignCurrencyExchange_Mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void getFind(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getCurrencyExchangeList(AsyncCallback<DmResult<List<GrdForeignCurrencyExchange_Mdl>>> result);

}
