package aarogya.pharmacy.proformaPurchaseReturn.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmToolkit.overHeadLine.shared.OverHeadITS_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough_Srv;
import java.util.List;

public interface ProformaPurchaseReturn_Srv extends RemoteService, IWalkThrough_Srv {

    //<editor-fold defaultstate="collapsed" desc="TextHelps">
    public DmResultPageing<List<KeyFieldExtended>> getTxtHVoucher(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHSupplier_Mdl>> getTxtHSupplier(String voucherCategory, String v_Type, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHReason(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<GrdBillDetail_Mdl>> getTxtHBillNo(String subCode, String voucherCategory, String v_Date, String billDocId, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHItemName_Mdl>> getTxtHItem(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHBatchNo_Mdl>> getTxtHBatchNo(String medicineCode, DmHelper pageing, boolean blnToEndOfFile);
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Grids">
    public DmResult<ProformaPurchaseReturn_Mdl> getItemDetail(String v_Type, List<KeyFieldExtended> svms, String location, String v_Date, String docId);
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Approval">
    public DmResult<String> setApproval(String approvedBy, boolean isApprove, String docId , String voucherType);
    //</editor-fold>

    public DmResult<String[]> getServerDateTime();

    public DmResult<DefaultValue_Mdl> getDefaultValues();

    public DmResult<String> saveRecord(ProformaPurchaseReturn_Mdl mrTempCurrent, ProformaPurchaseReturn_Mdl mrTempPrevious, String voucherPrefix, boolean isApprove);

    public DmResult<ProformaPurchaseReturn_Mdl> getMoveRec(String strKey);

    public DmResult<String> deleteRecord(String strKey, String guiConfigCode, ProformaPurchaseReturn_Mdl mrTempPrevious);

    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission);

    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<List<OverHeadITS_Mdl>> getITS(String itemCode, String itemRate, String location, String itsBasedOn, String billDate);
    

    public DmResult<KeyFieldExtended> getLandedSkuRateForValidation(String ibCode, String itemCode);

}
