package dataman.dmToolkit.ledgerPostingDisplayPopup.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmResult;
import java.util.List;


public interface LedgerPostingDisplayPopup_Srv extends RemoteService {
    
    public DmResult<List<GrdLedgerDisplayDetail_Mdl>> getLedgerPosting(String docid);
    
}
