package aarogya.inventory.grnApproval.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface GrnApproval_Srv extends RemoteService {

    public DmResultPageing<List<GrdGrnApproval_Mdl>> getFillRecords(Setting_Mdl param, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHStatus(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHOrderNo(DmHelper pageing, boolean blnToEndOfFile);

}
