package aarogya.pharmacy.purchaseBillApproval.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface PurchaseBillApproval_SrvAsync {

    public void getFillRecords(Setting_Mdl param, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<GrdPurchaseBillApproval_Mdl>>> result);

    public void getTxtHItem(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHGrnNo(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHCategory(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHCategory_Mdl>>> result);

    public void getTxtHSupplier(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHBillNo(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHPurchaseBillNo_Mdl>>> result);

    public void getTxtHStatus(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

}
