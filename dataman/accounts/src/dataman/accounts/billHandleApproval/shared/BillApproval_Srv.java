package dataman.accounts.billHandleApproval.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface BillApproval_Srv extends RemoteService {

    public DmResultPageing<List<GrdBillApproval_Mdl>> getFillRecords(DefaultValue_Mdl param, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHToLocation(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHStatus(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<DefaultValue_Mdl> getDefalutValue();

}
