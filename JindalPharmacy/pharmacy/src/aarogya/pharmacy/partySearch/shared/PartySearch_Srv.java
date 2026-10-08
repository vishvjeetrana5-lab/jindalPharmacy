package aarogya.pharmacy.partySearch.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface PartySearch_Srv extends RemoteService {

    public DmResultPageing<List<PartySearch_Mdl>> getPartySearchDetail(PartySearchValue_Mdl searchValue_Mdl, DmHelper helper, boolean blnToEndOfFile);

}
