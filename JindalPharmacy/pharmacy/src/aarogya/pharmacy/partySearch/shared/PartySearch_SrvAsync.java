package aarogya.pharmacy.partySearch.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface PartySearch_SrvAsync {

    public void getPartySearchDetail(PartySearchValue_Mdl searchValue_Mdl, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<PartySearch_Mdl>>> result);

}
