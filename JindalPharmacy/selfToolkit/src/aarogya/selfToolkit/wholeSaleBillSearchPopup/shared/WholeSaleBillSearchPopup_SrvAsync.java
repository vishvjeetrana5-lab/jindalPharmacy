package aarogya.selfToolkit.wholeSaleBillSearchPopup.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import java.util.List;

public interface WholeSaleBillSearchPopup_SrvAsync extends RemoteService {

    public void getFind(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl param, AsyncCallback<DmResultPageing<List<GrdWholeSaleBill_Mdl>>> result);

}
