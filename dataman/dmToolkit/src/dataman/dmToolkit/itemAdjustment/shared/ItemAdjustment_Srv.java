package dataman.dmToolkit.itemAdjustment.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface ItemAdjustment_Srv extends RemoteService {

    public DmResultPageing<List<GrdItemAdjustment_Mdl>> getItem(Filter_Mdl param, DmHelper helper, boolean blnToEndOfFile);
    
    public DmResultPageing<List<TxtHLedger_Mdl>> getTxtHLedger(DmHelper helper, boolean blnToEndOfFile);
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHVoucher(DmHelper helper, boolean blnToEndOfFile);
}
