package aarogya.pharmacy.purchaseOrderClose.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface PurchaseOrderClose_Srv extends RemoteService {

    public DmResult<String> openRecord(boolean isOpen, String docId, String vSNo, String statusRemark);

    public DmResultPageing<List<GrdPurchaseOrderClose_Mdl>> getFillRecords(PurchaseOrderClose_Mdl param, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHStatus(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<PurchaseOrderClose_Mdl> fillItemGrid(String code);

    public DmResultPageing<List<TxtHPurchaseOrder_Mdl>> getTxtHPurchaseOrder(DmHelper pageing, boolean blnToEndOfFile);

  //  public DmResultPageing<List<TxtHSupplierPT_Mdl>> getTxtHSupplierPT(String voucherCategory, boolean flagFilterByVoucherCategory, String v_Type, DmHelper pageing, boolean blnToEndOfFile);

  //  public DmResultPageing<List<TxtHItemName_Mdl>> getTxtHItem(String v_Type, String voucherCategory, String vDate, DmHelper pageing, boolean blnToEndOfFile);

}
