package dataman.dmToolkit.partyDetailPopup.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface PartyDetailPopup_SrvAsync {

    public void getTxtHCity(String gstin,String subCode,String stateNumericCode,String siteCode,DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHCity_Mdl>>> asyncCallback);
    
    public void getTxtHAdPlaceOfBusiness(String subCode,String siteCode,DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHAdPlaceOfBusiness_Mdl>>> asyncCallback);

}
