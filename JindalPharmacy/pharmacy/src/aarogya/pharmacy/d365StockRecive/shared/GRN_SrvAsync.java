package aarogya.pharmacy.d365StockRecive.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmToolkit.overHeadLine.shared.OverHeadITS_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.modifyReason.shared.ModifyReason_Mdl;
import dataman.dmToolkit.self.shared.*;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough_SrvAsync;
import java.util.List;

public interface GRN_SrvAsync extends IWalkThrough_SrvAsync {

    //<editor-fold defaultstate="collapsed" desc="TxtHelps">
    public void getTxtHVoucher(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHVoucher_Mdl>>> result);

    public void getTxtHSupplier(String voucherCategory, boolean flagFilterByVoucherCategory, String v_Type, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHSupplier_Mdl>>> result);

    public void getTxtHDepartment(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHMedicineName(String skuCode, String purchaseUnitCode, String restrictedDepartment, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHMedicineName_Mdl>>> result);

    public void getTxtHOrderNo(String subCode, String v_Date, String docId, String v_SNo, String restrictedDepartment, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHOrderNo_Mdl>>> result);

    public void getTxtHBatchNo(String medicineCode, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHBatchNo_Mdl>>> result);

    public void getTxtHImpurity(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Grids">
    public void getGrnDetail(List<KeyFieldExtended> keyFields, String subCode, String location, String billDate, String docId, String v_Date, AsyncCallback<DmResult<List<GrdGrnDetail_Mdl>>> result);

    public void getPrevGrn(String itemCode, String docId, String v_SNo, String supplierCode, boolean flagCurrentSupplier, DmHelper helper, boolean blnToEndOfFile, String param, AsyncCallback<DmResultPageing<List<GrdGrnDetail_Mdl>>> result);
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Approval">
    public void setApproval(String approvedBy, boolean isApprove, String docId, AsyncCallback<DmResult<String>> result);
    //</editor-fold>

    public void getServerDateTime(AsyncCallback<DmResult<String[]>> result);

    public void getDefaultValues(AsyncCallback<DmResult<DefaultValue_Mdl>> result);

    public void saveRecord(GRN_Mdl mrTempCurrent, GRN_Mdl mrTempPrevious, String voucherPrefix, boolean isApprove, AsyncCallback<DmResult<String>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<GRN_Mdl>> result);

    public void deleteRecord(String strKey, String recId, String guiConfigCode, ModifyReason_Mdl modifyReasonMdl, AsyncCallback<DmResult<String>> result);

    public void getNavigator(UserPermission userPermission, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getFind(UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getITS(String itemCode, String itemRate, String location, String itsBasedOn, String billDate, AsyncCallback<DmResult<List<OverHeadITS_Mdl>>> result);

    public void getStockPostingLedgers(String itemCode, AsyncCallback<DmResult<List<OverHeadITS_Mdl>>> result);

    public void updateRecord(GRN_Mdl mrTempCurrent, boolean isApprove, AsyncCallback<DmResult<String>> result);

    public void getD65DataDeatails(AsyncCallback<DmResult<String>> result);

    public void getStockIssueData(AsyncCallback<DmResult<String>> result);

}
