package aarogya.pharmacy.purchaseOrder.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.overHeadLine.shared.OverHeadITS_Mdl;
import dataman.dmToolkit.self.shared.*;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough_SrvAsync;
import java.util.List;

public interface PurchaseOrder_SrvAsync extends IWalkThrough_SrvAsync {

    //<editor-fold defaultstate="collapsed" desc="TextHelps">
    public void getTxtHVoucherType(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHSupplierPT(boolean flagFilterByVoucherCategory, String v_Type, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHSupplier_Mdl>>> result);

    public void getTxtHQuotationNo(boolean isShowUsedQuotationsAlso, String supplierCode, String v_Date, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHQuotationNo_Mdl>>> result);

    public void getTxtHMedicine(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHMedicineName_Mdl>>> result);
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Grids">
    public void getItemDetail(String indentDocId, List<KeyFieldExtended> keyFields, String subCode, String location, String billDate, String vDate, AsyncCallback<DmResult<PurchaseOrder_Mdl>> result);

    public void getIndentDetail(List<KeyFieldExtended> keyFields, String itemCode, AsyncCallback<DmResult<List<GrdIndentDetail_Mdl>>> result);

    public void getPrevGrn(String itemCode, String supplierCode, boolean flagCurrentSupplier, DmHelper helper, boolean blnToEndOfFile, String param, AsyncCallback<DmResultPageing<List<GrdGrnPurchaseHistory_Mdl>>> result);
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Approval">
    public void setApproval(String approvedBy, boolean isApprove,boolean isUNnApprove, String docId, AsyncCallback<DmResult<String>> result);
    //</editor-fold>

    public void getServerDateTime(AsyncCallback<DmResult<String[]>> result);

    public void getDefaultValues(AsyncCallback<DmResult<DefaultValue_Mdl>> result);

    public void saveRecord(PurchaseOrder_Mdl mrTemp, String voucherPrefix, boolean isApprove, AsyncCallback<DmResult<String>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<PurchaseOrder_Mdl>> result);

    public void deleteRecord(String strKey, String guiConfigCode, AsyncCallback<DmResult<String>> result);

    public void getNavigator(UserPermission userPermission, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getFind(UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getLastRate(String strKey, AsyncCallback<DmResult<String>> result);

    public void getITS(String itemCode, String itemRate, String location, String itsBasedOn, String billDate, AsyncCallback<DmResult<List<OverHeadITS_Mdl>>> result);

    public void getCDConfiguration(String supplierCode, String medicineCode, AsyncCallback<DmResult<String>> asyncCallback);

    public void getCDdetailForIndent(List<CDdetail_Mdl> lstCDdetail, AsyncCallback<DmResult<  List<CDdetail_Mdl>>> result);

}
