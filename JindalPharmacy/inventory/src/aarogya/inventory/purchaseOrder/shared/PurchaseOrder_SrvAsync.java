package aarogya.inventory.purchaseOrder.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.gsts.shared.GSTS_Mdl;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough_SrvAsync;
import java.util.List;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;

public interface PurchaseOrder_SrvAsync extends IWalkThrough_SrvAsync {

    public void getTempKeyCode(AsyncCallback<String> result);

    public void getNavigator(UserPermission userPermission, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<PurchaseOrder_Mdl>> result);

    public void deleteRecord(String strKey, String amdOrdDocId, String guiKeyCode, AsyncCallback<DmResult<String>> result);

    public void saveRecord(PurchaseOrder_Mdl mrTemp, String voucherPrefix, boolean isApprove, AsyncCallback<DmResult<String>> result);

    public void getFind(UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getTxtHVoucherType(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHVoucherType_Mdl>>> result);

    public void getTxtHSupplierPT(String voucherCategory, boolean flagFilterByVoucherCategory, String v_Type, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHSupplierPT_Mdl>>> result);

    public void getTxtHQuotationNo(boolean isShowUsedQuotationsAlso, String supplierCode, String v_Date,String voucherCategory, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHQuotationNo_Mdl>>> result);

    public void getTxtHItem(String v_Type,String voucherCategory, String vDate, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHItemName_Mdl>>> result);

    public void getIndentDetail(List<KeyFieldExtended> keyFields, ItemProperty_Mdl ippm_Mdl, AsyncCallback<DmResult<List<GrdIndentDetail_Mdl>>> result);

    public void getItemDetail(String v_Type, String indentDocId, String voucherCategory, String location, String billDate, String isStateOrUt, List<KeyFieldExtended> keyFields, AsyncCallback<DmResult<PurchaseOrder_Mdl>> result);

    public void getDefaultValues(AsyncCallback<DmResult<DefaultValue_Mdl>> result);

    public void getServerDateTime(AsyncCallback<DmResult<String[]>> result);

    public void setApproval(String approvedBy, boolean isApprove,boolean isUnApprove, String docId, AsyncCallback<DmResult<String>> result);

    public void getQuotationDetail(KeyFieldExtended keyField, String supplierCode, String v_Date, String voucherCategory, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResult<List<TxtHQuotationNo_Mdl>>> asyncCallback);

    public void getSupplierDetail(String vendorCode, String voucherCategory, boolean flagFilterByVoucherCategory, String v_Type, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResult<TxtHSupplierPT_Mdl>> result);

    public void getPrevGrn(ItemProperty_Mdl itemProperty, String itemCode, String supplierCode, boolean flagCurrentSupplier, DmHelper helper, boolean blnToEndOfFile, String param, AsyncCallback<DmResultPageing<List<GrdGrnPurchaseHistory_Mdl>>> result);

    public void getTxtHAmendmentOrder(String v_Date, String subCode, String amdOrdDocId, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHAmendmentOrder_Mdl>>> result);

    public void fillAmendmentGridDetail(String amdOrdDocId, AsyncCallback<DmResult<List<GrdItemDetail_Mdl>>> result);

   public void getGSTS(String itemCode, String itemRate, String itsBasedOn, String locationCode, String gstTypeCode,  boolean isManualGst, AsyncCallback<DmResult<List<GSTS_Mdl>>> result);

    public void getTxtHGstType(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> asyncCallback);

    public void getTxtHRCMApplicable(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> asyncCallback);
    
    public void getLastRate ( String strKey,AsyncCallback<DmResult<String>> result);

}
