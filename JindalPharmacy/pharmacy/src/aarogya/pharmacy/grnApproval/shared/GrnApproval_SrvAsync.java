package aarogya.pharmacy.grnApproval.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface GrnApproval_SrvAsync {

    public void getFillRecords(Setting_Mdl param, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<GrdGrnApproval_Mdl>>> result);

    public void getTxtHStatus(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHOrderNo(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

}
