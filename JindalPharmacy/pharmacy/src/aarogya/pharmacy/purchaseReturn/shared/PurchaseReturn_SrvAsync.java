package aarogya.pharmacy.purchaseReturn.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmToolkit.overHeadLine.shared.OverHeadITS_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough_SrvAsync;
import java.util.List;

public interface PurchaseReturn_SrvAsync extends IWalkThrough_SrvAsync {

    //<editor-fold defaultstate="collapsed" desc="TextHelps">
    public void getTxtHVoucher(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHSupplier(boolean flagFilterByVoucherCategory, String v_Type, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHSupplier_Mdl>>> result);

    public void getTxtHDepartment(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHReason(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHProformaBillNo(String subCode, String voucherCategory, String v_Date, String billDocId, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHProformaBillNo_Mdl>>> result);

    public void getTxtHBillNo(String subCode, String voucherCategory, String v_Date, String billDocId, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHGrdBillDetail_Mdl>>> result);

    public void getTxtHItem(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHItemName_Mdl>>> result);

    public void getTxtHBatchNo(String medicineCode, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHBatchNo_Mdl>>> result);
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Grids">
    public void getProformaPurchaseReturnItemDetail(String pfReturnDocId, String location, String v_Date, AsyncCallback<DmResult<List<GrdItemDetail_Mdl>>> result);

    public void getItemDetail(String v_Type, List<KeyFieldExtended> svms, String location, String v_Date, String docId, AsyncCallback<DmResult<PurchaseReturn_Mdl>> result);
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Approval">
    public void setApproval(String approvedBy, boolean isApprove, String docId, AsyncCallback<DmResult<String>> result);
    //</editor-fold>

    public void getServerDateTime(AsyncCallback<DmResult<String[]>> result);

    public void getDefaultValues(AsyncCallback<DmResult<DefaultValue_Mdl>> result);

    public void saveRecord(PurchaseReturn_Mdl mrTempCurrent, PurchaseReturn_Mdl mrTempPrevious, String voucherPrefix, boolean isApprove, AsyncCallback<DmResult<String>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<PurchaseReturn_Mdl>> result);

    public void deleteRecord(String strKey, String guiConfigCode, PurchaseReturn_Mdl mrTempPrevious, AsyncCallback<DmResult<String>> result);

    public void getNavigator(UserPermission userPermission, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getFind(UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getITS(String itemCode, String itemRate, String location, String itsBasedOn, String billDate, AsyncCallback<DmResult<List<OverHeadITS_Mdl>>> result);

    public void getStockPostingLedgers(String itemCode, AsyncCallback<DmResult<List<OverHeadITS_Mdl>>> result);

    public void getLandedSkuRateForValidation(String ibCode, String itemCode, AsyncCallback<DmResult<KeyFieldExtended>> result);

    public void processAutoVoucherAdjustment(String strKey, String supplierCode, AsyncCallback<DmResult<String>> result);

}
