package aarogya.pharmacy.pendingPODisplay.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface PendingPO_Display_Srv extends RemoteService {

    public DmResult<String> openRecord(boolean isOpen, String docId, String vSNo);

    public DmResultPageing<List<GrdPendingPO_Display_Mdl>> getFillRecords(PendingPO_Dispaly_Mdl param, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<PendingPO_Dispaly_Mdl> fillItemGrid(String code);
    
    public DmResultPageing<List<TxtHPurchaseOrder_Mdl>> getTxtHPurchaseOrder(DmHelper pageing, boolean blnToEndOfFile);
    
    public DmResult<String> sendMail(String docId);
}
