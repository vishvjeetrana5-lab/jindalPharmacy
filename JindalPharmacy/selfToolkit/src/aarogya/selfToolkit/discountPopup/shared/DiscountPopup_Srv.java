package aarogya.selfToolkit.discountPopup.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface DiscountPopup_Srv extends RemoteService {

    public DmResultPageing<List<KeyFieldExtended>> getTxtHConsultant(DmHelper helper, boolean blnToEndOfFile, String vType);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHGroupName(DmHelper helper, boolean blnToEndOfFile);
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDiscountReason(DmHelper helper, boolean blnToEndOfFile);
}
