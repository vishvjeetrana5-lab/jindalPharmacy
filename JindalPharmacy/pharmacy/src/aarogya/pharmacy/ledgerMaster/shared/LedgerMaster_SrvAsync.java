package aarogya.pharmacy.ledgerMaster.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface LedgerMaster_SrvAsync {

    public void getTxtHCity(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHCity_Mdl>>> result);

    public void getTxtHIdType(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHInitial(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHIsActive(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getFind(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<LedgerMaster_Mdl>>> result);

    public void getNavigator(AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void saveRecord(LedgerMaster_Mdl mrTemp, AsyncCallback<DmResult<LedgerMaster_Mdl>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<LedgerMaster_Mdl>> result);
}
