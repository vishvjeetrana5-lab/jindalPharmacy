package aarogya.pharmacy.purchaseReturn.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmToolkit.overHeadLine.shared.OverHeadITS_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough_Srv;
import java.util.List;

public interface PurchaseReturn_Srv extends RemoteService, IWalkThrough_Srv {

    //<editor-fold defaultstate="collapsed" desc="TextHelps">
    public DmResultPageing<List<KeyFieldExtended>> getTxtHVoucher(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHSupplier_Mdl>> getTxtHSupplier(boolean flagFilterByVoucherCategory, String v_Type, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHReason(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHProformaBillNo_Mdl>> getTxtHProformaBillNo(String subCode, String voucherCategory, String v_Date, String billDocId, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHGrdBillDetail_Mdl>> getTxtHBillNo(String subCode, String voucherCategory, String v_Date, String billDocId, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHItemName_Mdl>> getTxtHItem(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHBatchNo_Mdl>> getTxtHBatchNo(String medicineCode, DmHelper pageing, boolean blnToEndOfFile);
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Grids">
    public DmResult<List<GrdItemDetail_Mdl>> getProformaPurchaseReturnItemDetail(String pfReturnDocId, String location, String v_Date);

    public DmResult<PurchaseReturn_Mdl> getItemDetail(String v_Type, List<KeyFieldExtended> svms, String location, String v_Date, String docId);
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Approval">
    public DmResult<String> setApproval(String approvedBy, boolean isApprove, String docId);
    //</editor-fold>

    public DmResult<String[]> getServerDateTime();

    public DmResult<DefaultValue_Mdl> getDefaultValues();

    public DmResult<String> saveRecord(PurchaseReturn_Mdl mrTempCurrent, PurchaseReturn_Mdl mrTempPrevious, String voucherPrefix, boolean isApprove);

    public DmResult<PurchaseReturn_Mdl> getMoveRec(String strKey);

    public DmResult<String> deleteRecord(String strKey, String guiConfigCode, PurchaseReturn_Mdl mrTempPrevious);

    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission);

    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<List<OverHeadITS_Mdl>> getITS(String itemCode, String itemRate, String location, String itsBasedOn, String billDate);

    public DmResult<List<OverHeadITS_Mdl>> getStockPostingLedgers(String itemCode);

    public DmResult<KeyFieldExtended> getLandedSkuRateForValidation(String ibCode, String itemCode);

    public DmResult<String> processAutoVoucherAdjustment(String strKey, String supplierCode);

}
