
package aarogya.selfToolkit.wholeSaleBillSearchPopup.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import java.util.List;


public interface WholeSaleBillSearchPopup_Srv extends RemoteService {
    
    public DmResultPageing<List<GrdWholeSaleBill_Mdl>> getFind(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl param);
    
}
