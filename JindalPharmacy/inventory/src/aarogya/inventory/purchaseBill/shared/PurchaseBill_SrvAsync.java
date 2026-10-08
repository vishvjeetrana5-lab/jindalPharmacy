package aarogya.inventory.purchaseBill.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.gsts.shared.GSTS_Mdl;
import dataman.dmToolkit.overHeadLine.shared.OverHeadITS_Mdl;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough_SrvAsync;
import java.util.List;

public interface PurchaseBill_SrvAsync extends IWalkThrough_SrvAsync {

    //<editor-fold defaultstate="collapsed" desc="TextHelps">
    public void getTxtHVoucher(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHVoucherType_Mdl>>> result);

    public void getTxtHSupplier(String voucherCategory, String v_Type, String v_Date, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHSupplier_Mdl>>> result);

    public void getTxtHGrnNo(String v_Type, String subCode, String v_Date, String docId, String v_SNo, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHGrnNo_Mdl>>> result);

    public void getTxtHCalcOn(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHRCMApplicable(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> asyncCallback);

    public void getTxtHGstType(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Grids">
    public void getItemDetail(String v_Type, List<KeyFieldExtended> keyFields, String grnDocId, String isStateOrUt, String subCode, String location, String billDate, String docId, String v_Date, Boolean singlePOFlowInPurchase, String rcmApplicable, boolean isIncludeTaxInCost, String isSiteStateUT, String purchaseAcPostingBasedOn, boolean isInventoryAccountingActive, AsyncCallback<DmResult<PurchaseBill_Mdl>> result);
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Approval">
    public void setApproval(String approvedBy, boolean isApprove, String docId, AsyncCallback<DmResult<String>> result);
    //</editor-fold>

    public void getServerDateTime(AsyncCallback<DmResult<String[]>> result);

    public void getDefaultValues(AsyncCallback<DmResult<DefaultValue_Mdl>> result);

    public void getBillOtherDetails(String pbNo, String button, AsyncCallback<DmResult<List<GrdBillOtherDetails_Mdl>>> result);

    public void saveRecord(PurchaseBill_Mdl mrTemp, String voucherPrefix, boolean isApprove, boolean updateItemPurchaseRate, AsyncCallback<DmResult<String>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<PurchaseBill_Mdl>> result);

    public void deleteRecord(String strKey, String guiConfigCode, AsyncCallback<DmResult<String>> result);

    public void getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getNavigator(UserPermission userPermission, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getGSTS(String itemCode, String itemRate, String itsBasedOn, String locationCode, String gstTypeCode, boolean isManualGst, AsyncCallback<DmResult<List<GSTS_Mdl>>> result);

    public void getSupplierDetails(String supplierCode, String voucherCategory, String v_Type, String v_Date, DmHelper dmHelper, boolean blnToEndOfFile, AsyncCallback<DmResult<TxtHSupplier_Mdl>> asyncCallback);

    public void getVoucherType(String partyType, AsyncCallback<DmResult<TxtHVoucherType_Mdl>> result);

    public void getTxtHHeaderGrnNo(String voucherCategory, String txtHSupplierTag, String txtSupplierBillDate, String docId, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHHeaderGrnNo_Mdl>>> asyncCallback);

    public void updateRecord(PurchaseBill_Mdl mrTemp, boolean isApprove, AsyncCallback<DmResult<String>> result);

}
