package aarogya.pharmacy.walkInSearch.shared;

import aarogya.pharmacy.ledgerMaster.shared.LedgerMaster_Mdl;
import com.google.gwt.user.client.rpc.AsyncCallback;

import dataman.dmBase.self.shared.*;
import java.util.List;

public interface WalkInSearch_SrvAsync {

    public void getSearchRecord(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl param, AsyncCallback<DmResultPageing<List<LedgerMaster_Mdl>>> result);

}
