package aarogya.pharmacy.walkInSearch.shared;

import aarogya.pharmacy.ledgerMaster.shared.LedgerMaster_Mdl;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface WalkInSearch_Srv extends RemoteService {

    public DmResultPageing<List<LedgerMaster_Mdl>> getSearchRecord(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl param);

}
