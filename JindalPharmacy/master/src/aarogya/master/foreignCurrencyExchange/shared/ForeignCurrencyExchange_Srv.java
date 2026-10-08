package aarogya.master.foreignCurrencyExchange.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface ForeignCurrencyExchange_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResult<ForeignCurrencyExchange_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(ForeignCurrencyExchange_Mdl mrTemp);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<KeyFieldExtended>> getFind(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<List<GrdForeignCurrencyExchange_Mdl>> getCurrencyExchangeList();

}
