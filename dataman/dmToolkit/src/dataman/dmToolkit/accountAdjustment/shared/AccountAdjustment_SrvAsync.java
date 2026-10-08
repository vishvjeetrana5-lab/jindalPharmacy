package dataman.dmToolkit.accountAdjustment.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface AccountAdjustment_SrvAsync {

    public void getLedger(Filter_Mdl param, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<GrdAdjustment_Mdl>>> result);

    public void getTxtHLedger(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHLedger_Mdl>>> result);

    public void getTxtHVoucher(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getDrCr(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getHistory(String adjDocId, String adjV_Sno, AsyncCallback<DmResult<List<GrdHistory_Mdl>>> result);

}
