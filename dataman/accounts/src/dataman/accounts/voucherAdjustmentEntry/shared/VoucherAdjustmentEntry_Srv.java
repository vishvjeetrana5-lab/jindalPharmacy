package dataman.accounts.voucherAdjustmentEntry.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface VoucherAdjustmentEntry_Srv extends RemoteService {

    public DmResultPageing<List<GrdLedger_Mdl>> getLedger(Filter_Mdl param, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<List<GrdLedger_Mdl>> getLedgerByDocId(String voucherDocId);

    public DmResultPageing<List<TxtHLedger_Mdl>> getTxtHLedger(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHVoucher(DmHelper helper, boolean blnToEndOfFile);
    
    public DmResult<String> saveRecord(List<GrdLedger_Mdl> lstRecords);

}
