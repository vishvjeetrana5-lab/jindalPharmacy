package dataman.dmToolkit.ledgerPostingDisplayPopup.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmResult;
import java.util.List;

public interface LedgerPostingDisplayPopup_SrvAsync {
    
    public void getLedgerPosting(String docid, AsyncCallback<DmResult<List<GrdLedgerDisplayDetail_Mdl>>> result);
    
}
