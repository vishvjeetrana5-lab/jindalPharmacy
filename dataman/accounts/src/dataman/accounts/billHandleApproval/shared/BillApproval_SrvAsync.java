package dataman.accounts.billHandleApproval.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface BillApproval_SrvAsync {

    public void getFillRecords(DefaultValue_Mdl param, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<GrdBillApproval_Mdl>>> result);

    public void getTxtHToLocation(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHStatus(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getDefalutValue(AsyncCallback<DmResult<DefaultValue_Mdl>> result);
}
