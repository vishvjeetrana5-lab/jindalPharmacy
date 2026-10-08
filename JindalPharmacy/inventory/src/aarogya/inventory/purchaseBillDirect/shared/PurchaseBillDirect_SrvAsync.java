package aarogya.inventory.purchaseBillDirect.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.gsts.shared.GSTS_Mdl;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough_SrvAsync;
import java.util.List;

public interface PurchaseBillDirect_SrvAsync extends IWalkThrough_SrvAsync {

    //<editor-fold defaultstate="collapsed" desc="TxtHelps">
    public void getTxtHVoucher(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHVoucherType_Mdl>>> result);

    public void getTxtHSupplier(String voucherCategory, String v_Type, String v_Date, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHSupplier_Mdl>>> result);

    public void getTxtHDepartment(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHItem(String v_Type, String skuCode, String purchaseUnitCode, String restrictedDepartment, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHItemName_Mdl>>> result);

    public void getTxtHOrderNo(String subCode, String v_Date, String docId, String v_SNo, String restrictedDepartment, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHOrderNo_Mdl>>> result);

    public void getTxtHImpurity(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Grids">
    public void getOrderDetail(String v_Type, List<KeyFieldExtended> keyFields, String subCode, String location, String billDate, String docId, String v_Date, String isSiteStateUT, String rcmApplicable, boolean isIncludeTaxInCost, String purchaseAcPostingBasedOn, boolean isInventoryAccountingActive, AsyncCallback<DmResult<List<GrdItemDetail_Mdl>>> result);

    public void getPrevGrn(String itemCode, String docId, String v_SNo, String supplierCode, boolean flagCurrentSupplier, DmHelper helper, boolean blnToEndOfFile, String param, AsyncCallback<DmResultPageing<List<GrdItemDetail_Mdl>>> result);
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Approval">
    public void setApproval(String approvedBy, boolean isApprove, String docId, AsyncCallback<DmResult<String>> result);
    //</editor-fold>

    public void getServerDateTime(AsyncCallback<DmResult<String[]>> result);

    public void getDefaultValues(AsyncCallback<DmResult<DefaultValue_Mdl>> result);

    public void saveRecord(PurchaseBillDirect_Mdl mrTempCurrent, PurchaseBillDirect_Mdl mrTempPrevious, String voucherPrefix, boolean isApprove, boolean updateItemPurchaseRate, AsyncCallback<DmResult<String>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<PurchaseBillDirect_Mdl>> result);

    public void deleteRecord(String strKey, String guiConfigCode, AsyncCallback<DmResult<String>> result);

    public void getNavigator(UserPermission userPermission, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getFind(UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);
    
    public void getTxtHCalculateOn(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getSupplierDetails(String supplierCode, String voucherCategory, String v_Type, String v_Date, DmHelper dmHelper, boolean b, AsyncCallback<DmResult<TxtHSupplier_Mdl>> asyncCallback);

    public void getVoucherType(String partyType, AsyncCallback<DmResult<TxtHVoucherType_Mdl>> result);

    public void getQCPaparameter(String itemCode, AsyncCallback<DmResult<List<GrdImpurity_Mdl>>> result);

    public void getTxtHTransporter(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHTransporter_Mdl>>> result);

    public void getTxtHGateEntryNo(String pBillDocId, String supplierCode, String billDate, String refDate, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHGateEntry_Mdl>>> result);

    public void fillGateEntryDetail(String v_Type, String gateDocId, String partyCode, String location, String billDate, String v_Date, String isSiteStateUT, String rcmApplicable, boolean isIncludeTaxInCost, String purchaseAcPostingBasedOn, boolean isInventoryAccountingActive, AsyncCallback<DmResult<List<GrdItemDetail_Mdl>>> result);

    public void getCreateHelp(String helpSource, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHGstType(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> asyncCallback);

    public void getGSTS(String itemCode, String itemRate, String itsBasedOn, String location, String gstTypeCode, boolean manualGst, AsyncCallback<DmResult<List<GSTS_Mdl>>> asyncCallback);

    public void updateRecord(PurchaseBillDirect_Mdl mrTempCurrent, boolean isApprove, AsyncCallback<DmResult<String>> result);
    
    public void getBillOtherDetails(String pbNo, String button, AsyncCallback<DmResult<List<GrdBillOtherDetails_Mdl>>> result);

    public void getTxtHRCMApplicable(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> asyncCallback);
}
