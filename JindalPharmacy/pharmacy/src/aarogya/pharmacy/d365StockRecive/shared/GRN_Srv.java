package aarogya.pharmacy.d365StockRecive.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmToolkit.overHeadLine.shared.OverHeadITS_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.modifyReason.shared.ModifyReason_Mdl;
import dataman.dmToolkit.self.shared.*;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough_Srv;
import java.util.List;

public interface GRN_Srv extends RemoteService, IWalkThrough_Srv {

    //<editor-fold defaultstate="collapsed" desc="TxtHelps">
    public DmResultPageing<List<TxtHVoucher_Mdl>> getTxtHVoucher(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHSupplier_Mdl>> getTxtHSupplier(String voucherCategory, boolean flagFilterByVoucherCategory, String v_Type, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHMedicineName_Mdl>> getTxtHMedicineName(String skuCode, String purchaseUnitCode, String restrictedDepartment, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHOrderNo_Mdl>> getTxtHOrderNo(String subCode, String v_Date, String docId, String v_SNo, String restrictedDepartment, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHBatchNo_Mdl>> getTxtHBatchNo(String medicineCode, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHImpurity(DmHelper pageing, boolean blnToEndOfFile);
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Grids">
    public DmResult<List<GrdGrnDetail_Mdl>> getGrnDetail(List<KeyFieldExtended> keyFields, String subCode, String location, String billDate, String docId, String v_Date);

    public DmResultPageing<List<GrdGrnDetail_Mdl>> getPrevGrn(String itemCode, String docId, String v_SNo, String supplierCode, boolean flagCurrentSupplier, DmHelper helper, boolean blnToEndOfFile, String param);
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Approval">
    public DmResult<String> setApproval(String approvedBy, boolean isApprove, String docId);
    //</editor-fold>

    public DmResult<String[]> getServerDateTime();

    public DmResult<DefaultValue_Mdl> getDefaultValues();

    public DmResult<String> saveRecord(GRN_Mdl mrTempCurrent, GRN_Mdl mrTempPrevious, String voucherPrefix, boolean isApprove);

    public DmResult<GRN_Mdl> getMoveRec(String strKey);

    public DmResult<String> deleteRecord(String strKey, String recId, String guiConfigCode, ModifyReason_Mdl modifyReasonMdl);

    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission);

    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<List<OverHeadITS_Mdl>> getITS(String itemCode, String itemRate, String location, String itsBasedOn, String billDate);

    public DmResult<List<OverHeadITS_Mdl>> getStockPostingLedgers(String itemCode);

    public DmResult<String> updateRecord(GRN_Mdl mrTempCurrent, boolean isApprove);

    public DmResult<String> getD65DataDeatails();

    public DmResult<String> getStockIssueData();

}
