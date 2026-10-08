package dataman.accounts.voucherEntryApproval.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface VoucherEntryApproval_Srv extends RemoteService {

    public DmResultPageing<List<GrdVoucherEntryApproval_Mdl>> getFillRecords(Setting_Mdl param, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHStatus(DmHelper pageing, boolean blnToEndOfFile);
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCostCenter(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHVoucherNo_Mdl>> getTxtHVoucherNo(DmHelper pageing, boolean blnToEndOfFile);
  public DmResultPageing<List<KeyFieldExtended>> getTxtHVoucherType(DmHelper helper, boolean blnToEndOfFile);

}
