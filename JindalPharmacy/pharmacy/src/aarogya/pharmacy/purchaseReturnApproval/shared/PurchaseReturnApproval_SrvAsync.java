package aarogya.pharmacy.purchaseReturnApproval.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface PurchaseReturnApproval_SrvAsync {

    public void getFillRecords(Setting_Mdl param, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<GrdPurchaseReturnApproval_Mdl>>> result);

    public void getTxtHStatus(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHGrnNo(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHGrnNo_Mdl>>> result);

    public void getTxtHBillNo(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHBillNo_Mdl>>> result);

}
