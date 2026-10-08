package aarogya.master.cardReceive.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface CardReceive_SrvAsync {

    public void getNavigator(AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<CardReceive_Mdl>> result);

    public void deleteRecord(String strKey, AsyncCallback<DmResult<String>> result);

    public void saveRecord(CardReceive_Mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void getFind(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void fillDeleteGrd(DmHelper pageing, boolean blnToEndOfFile, String recDate, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getReceiveCards(DmHelper pageing, boolean blnToEndOfFile, String recDate, AsyncCallback<DmResultPageing<List<GrdCardReceive_Mdl>>> result);

}
