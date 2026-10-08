package aarogya.pharmacy.purchaseOrderApproval.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface PurchaseApproval_Srv extends RemoteService {

    public DmResult<String> openRecord(boolean isOpen, String docId, String vSNo,String closedPurchaseOrderApproval);

    public DmResult<GrdMedicineDetail_Mdl> fillOrdIndentDetail(String xNull, String vSNo, GrdMedicineDetail_Mdl param);

    public DmResultPageing<List<GrdPurchaseOrderApproval_Mdl>> getFillRecords(PurchaseOrderApproval_Mdl param, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHStatus(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<PurchaseOrderApproval_Mdl> fillItemGrid(String code);
    

    //  public DmResult<PurchaseOrderApproval_Mdl> fillPOGrd(String code);
    public DmResultPageing<List<TxtHPurchaseOrder_Mdl>> getTxtHPurchaseOrder(DmHelper pageing, boolean blnToEndOfFile);

    // public DmResult<List<GrdPo_IndentDetail_Mdl>> fillIndentGrid(String code, String v_Sno);
      public DmResult<String> deducationfromIndentD(GrdMedicineDetail_Mdl param);

}
