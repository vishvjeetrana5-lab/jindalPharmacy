package dataman.dmToolkit.partyDetailPopup.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import java.util.List;

public interface PartyDetailPopup_Srv extends RemoteService {

    public DmResultPageing<List<TxtHCity_Mdl>> getTxtHCity(String gstin,String stateNumericCode,String subCode,String siteCode,DmHelper helper, boolean blnToEndOfFile);
    
    public DmResultPageing<List<TxtHAdPlaceOfBusiness_Mdl>> getTxtHAdPlaceOfBusiness(String subCode,String siteCode,DmHelper helper, boolean blnToEndOfFile);
    
}
