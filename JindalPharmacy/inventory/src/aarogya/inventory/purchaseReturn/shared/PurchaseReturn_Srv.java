package aarogya.inventory.purchaseReturn.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmToolkit.overHeadLine.shared.OverHeadITS_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.gsts.shared.GSTS_Mdl;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough_Srv;
import java.util.List;

public interface PurchaseReturn_Srv extends RemoteService, IWalkThrough_Srv {

    //<editor-fold defaultstate="collapsed" desc="TextHelps">
    public DmResultPageing<List<TxtHVoucherType_Mdl>> getTxtHVoucher(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHSupplier_Mdl>> getTxtHSupplier(String voucherCategory, boolean flagFilterByVoucherCategory, String v_Type, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(String itemCode, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHReason(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHBillNo_Mdl>> getTxtHBillNo(String subCode, String voucherCategory, String v_Date, String billDocId, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHItemName_Mdl>> getTxtHItem(String v_Type, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getEwayBillReq(DmHelper pageing, boolean blnToEndOfFile);
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Grids">
    public DmResult<PurchaseReturn_Mdl> getItemDetail(String v_Type, String billDocId, String location, String v_Date, String docId, String rcmApplicable, boolean isIncludeTaxInCost, String isSiteStateUT, String purchaseAcPostingBasedOn, boolean isInventoryAccountingActive);
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Approval">
    public DmResult<String> setApproval(String approvedBy, boolean isApprove, String docId);
    //</editor-fold>

    public DmResult<String[]> getServerDateTime();

    public DmResult<DefaultValue_Mdl> getDefaultValues();

    public DmResult<String> saveRecord(PurchaseReturn_Mdl mrTempCurrent, PurchaseReturn_Mdl mrTempPrevoius, String voucherPrefix, boolean isApprove);

    public DmResult<PurchaseReturn_Mdl> getMoveRec(String strKey);

    public DmResult<String> deleteRecord(String strKey, String guiConfigCode);

    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission);

    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<List<GSTS_Mdl>> getGSTS(String itemCode, String itemRate, String itsBasedOn, String locationCode, String gstTypeCode, boolean isManualGst);

    public DmResultPageing<List<TxtHTransporter_Mdl>> getTxtHTransporter(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHGateEntry_Mdl>> getTxtHGateEntryNo(String pBillDocId, String supplierCode, String returnDate, String refDate, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHTranspostMode(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHRCMApplicable(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHGstType(DmHelper pageing, boolean blnToEndOfFile);

}
