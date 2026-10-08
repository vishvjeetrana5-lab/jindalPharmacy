package dataman.dmToolkit.itemAdjustment.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface ItemAdjustment_SrvAsync {

    public void getItem(Filter_Mdl param, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<GrdItemAdjustment_Mdl>>> result);

    public void getTxtHLedger(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHLedger_Mdl>>> result);

    public void getTxtHVoucher(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

}
