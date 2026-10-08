package aarogya.pharmacy.purchaseOrderClose.shared;


import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface PurchaseOrderClose_SrvAsync {

    public void openRecord(boolean isOpen, String docId, String vSNo,String statusRemark, AsyncCallback<DmResult<String>> result);

    public void getFillRecords(PurchaseOrderClose_Mdl param, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<GrdPurchaseOrderClose_Mdl>>> result);

    public void getTxtHStatus(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void fillItemGrid(String code, AsyncCallback<DmResult<PurchaseOrderClose_Mdl>> result);

    public void getTxtHPurchaseOrder(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHPurchaseOrder_Mdl>>> result);

   // public void getTxtHSupplierPT(String string, boolean b, Object object, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHSupplierPT_Mdl>>> asyncCallback);

   // public void getTxtHItem(String string, String string0, String string1, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHItemName_Mdl>>> asyncCallback);

}
