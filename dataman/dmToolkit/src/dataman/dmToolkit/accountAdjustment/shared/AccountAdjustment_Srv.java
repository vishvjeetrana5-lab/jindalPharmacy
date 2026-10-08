package dataman.dmToolkit.accountAdjustment.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface AccountAdjustment_Srv extends RemoteService {

    public DmResultPageing<List<GrdAdjustment_Mdl>> getLedger(Filter_Mdl param, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHLedger_Mdl>> getTxtHLedger(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHVoucher(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getDrCr(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<List<GrdHistory_Mdl>> getHistory(String adjDocId, String adjV_Sno);
}
