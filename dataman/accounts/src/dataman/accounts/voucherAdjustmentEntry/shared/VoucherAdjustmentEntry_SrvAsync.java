package dataman.accounts.voucherAdjustmentEntry.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface VoucherAdjustmentEntry_SrvAsync {

    public void getLedger(Filter_Mdl param, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<GrdLedger_Mdl>>> result);

    public void getLedgerByDocId(String oucherDocId, AsyncCallback<DmResult<List<GrdLedger_Mdl>>> result);

    public void getTxtHLedger(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHLedger_Mdl>>> result);

    public void getTxtHVoucher(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void saveRecord(List<GrdLedger_Mdl> lstRecords, AsyncCallback<DmResult<String>> result);

}
