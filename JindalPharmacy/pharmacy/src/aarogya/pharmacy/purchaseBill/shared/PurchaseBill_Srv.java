package aarogya.pharmacy.purchaseBill.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.overHeadLine.shared.OverHeadITS_Mdl;
import dataman.dmToolkit.self.shared.*;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough_Srv;
import java.util.List;

public interface PurchaseBill_Srv extends RemoteService, IWalkThrough_Srv {

    //<editor-fold defaultstate="collapsed" desc="TextHelps">
    public DmResultPageing<List<TxtHVoucher_Mdl>> getTxtHVoucher(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHSupplier_Mdl>> getTxtHSupplier(String voucherCategory, String v_Type, String v_Date, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHGrnNo_Mdl>> getTxtHGrnNo(String subCode, String v_Date, String docId, String v_SNo, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHCalcOn(DmHelper helper, boolean blnToEndOfFile);
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Grids">
    public DmResult<List<GrdItemDetail_Mdl>> getItemDetail(List<KeyFieldExtended> keyFields, String subCode, String location, String billDate, String docId, String v_Date,Boolean singlePOFlowInPurchase);
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Approval">
    public DmResult<String> setApproval(String approvedBy, boolean isApprove, String docId);
    //</editor-fold>

    public DmResult<String[]> getServerDateTime();

    public DmResult<DefaultValue_Mdl> getDefaultValues();

    public DmResult<String> saveRecord(PurchaseBill_Mdl mrTemp, String voucherPrefix, boolean isApprove);

    public DmResult<PurchaseBill_Mdl> getMoveRec(String strKey);

    public DmResult<String> deleteRecord(String strKey, String guiConfigCode);

    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission);

    public DmResult<List<OverHeadITS_Mdl>> getITS(String itemCode, String itemRate, String location, String itsBasedOn, String billDate);
    
    public DmResult<String> updateRecord(PurchaseBill_Mdl mrTemp, boolean isApprove);
    
    public DmResult<List<GrdBillOtherDetails_Mdl>> getBillOtherDetails(String pbNo, String button);

}
