package aarogya.pharmacy.purchaseOrderApproval.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface PurchaseApproval_SrvAsync {

    public void openRecord(boolean isOpen, String docId, String vSNo,String closedPurchaseOrderApproval, AsyncCallback<DmResult<String>> result);

    public void getFillRecords(PurchaseOrderApproval_Mdl param, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<GrdPurchaseOrderApproval_Mdl>>> result);

    public void getTxtHStatus(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void fillItemGrid(String code, AsyncCallback<DmResult<PurchaseOrderApproval_Mdl>> result);

    public void getTxtHPurchaseOrder(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHPurchaseOrder_Mdl>>> result);
    
    //public void fillIndentGrid(String code,String v_Sno,AsyncCallback<DmResultPageing<List<GrdPo_IndentDetail_Mdl>>> result)  ;

   // public void fillPOGrd(String code, AsyncCallback<DmResult<GrdPurchaseOrderDetail_Mdl>> asyncCallback);

    public void fillOrdIndentDetail(String xNull, String vSNo,GrdMedicineDetail_Mdl param, AsyncCallback<DmResult<GrdMedicineDetail_Mdl>> asyncCallback);

    public void deducationfromIndentD(GrdMedicineDetail_Mdl param, AsyncCallback<DmResult<String>> asyncCallback);




}
