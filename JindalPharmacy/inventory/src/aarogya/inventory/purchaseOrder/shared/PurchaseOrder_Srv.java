package aarogya.inventory.purchaseOrder.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.gsts.shared.GSTS_Mdl;
import dataman.dmToolkit.overHeadLine.shared.OverHeadITS_Mdl;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough_Srv;
import java.util.List;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;

public interface PurchaseOrder_Srv extends RemoteService, IWalkThrough_Srv {

    public String getTempKeyCode();

    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission);

    public DmResult<PurchaseOrder_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(PurchaseOrder_Mdl mrTemp, String voucherPrefix, boolean isApprove);

    public DmResult<String> deleteRecord(String strKey, String amdOrdDocId, String guiConfigCode);

    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHVoucherType_Mdl>> getTxtHVoucherType(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHSupplierPT_Mdl>> getTxtHSupplierPT(String voucherCategory, boolean flagFilterByVoucherCategory, String v_Type, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHQuotationNo_Mdl>> getTxtHQuotationNo(boolean isShowUsedQuotationsAlso, String supplierCode, String v_Date,String voucherCategory, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHItemName_Mdl>> getTxtHItem(String v_Type,String voucherCategory, String vDate, DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<PurchaseOrder_Mdl> getItemDetail(String v_Type, String indentDocId, String voucherCategory, String location, String billDate, String isStateOrUt, List<KeyFieldExtended> keyFields);

    public DmResult<DefaultValue_Mdl> getDefaultValues();

    public DmResult<String[]> getServerDateTime();

    public DmResult<List<GrdIndentDetail_Mdl>> getIndentDetail(List<KeyFieldExtended> keyFields, ItemProperty_Mdl ippm_Mdl);

    public DmResult<String> setApproval(String approvedBy, boolean isApprove,boolean isUnApprove, String docId);

    public DmResult<List<TxtHQuotationNo_Mdl>> getQuotationDetail(KeyFieldExtended keyField, String supplierCode, String v_Date, String voucherCategory, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<TxtHSupplierPT_Mdl> getSupplierDetail(String vendorCode, String voucherCategory, boolean flagFilterByVoucherCategory, String v_Type, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<GrdGrnPurchaseHistory_Mdl>> getPrevGrn(ItemProperty_Mdl itemProperty, String itemCode, String supplierCode, boolean flagCurrentSupplier, DmHelper helper, boolean blnToEndOfFile, String param);

    public DmResultPageing<List<TxtHAmendmentOrder_Mdl>> getTxtHAmendmentOrder(String v_Date, String subCode, String amdOrdDocId, DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<List<GrdItemDetail_Mdl>> fillAmendmentGridDetail(String amdOrdDocId);

    public DmResult<List<GSTS_Mdl>> getGSTS(String itemCode, String itemRate, String itsBasedOn, String locationCode, String gstTypeCode, boolean isManualGst);
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHGstType(DmHelper pageing, boolean blnToEndOfFile);
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHRCMApplicable(DmHelper pageing, boolean blnToEndOfFile);
    
    public DmResult<String> getLastRate(String strKey);
}
