package aarogya.inventory.pendingPODisplay.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface PendingPO_Display_SrvAsync {

    public void openRecord(boolean isOpen, String docId, String vSNo, AsyncCallback<DmResult<String>> result);

    public void getFillRecords(PendingPO_Dispaly_Mdl param, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<GrdPendingPO_Display_Mdl>>> result);

    public void fillItemGrid(String code, AsyncCallback<DmResult<PendingPO_Dispaly_Mdl>> result);

    public void getTxtHPurchaseOrder(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHPurchaseOrder_Mdl>>> result);
    
    public void sendMail(String docId, AsyncCallback<DmResult<String>> result);
    
    public void getTxtHItem(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHItemName_Mdl>>> result);
    
    public void  getTxtHSupplier(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHSupplier_Mdl>>> result);
}
