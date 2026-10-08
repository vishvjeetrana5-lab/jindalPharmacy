package aarogya.inventory.grn.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.gsts.shared.GSTS_Mdl;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough_SrvAsync;
import java.util.List;

public interface Grn_SrvAsync extends IWalkThrough_SrvAsync {

    //<editor-fold defaultstate="collapsed" desc="TxtHHelps">
    public void getTxtHVoucher(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHVoucherType_Mdl>>> result);

    public void getTxtHSupplier(String voucherCategory, boolean flagFilterByVoucherCategory, String v_Type, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHSupplier_Mdl>>> result);

    public void getTxtHDepartment(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHItem(String v_Type, String skuCode, String purchaseUnitCode, String restrictedDepartment, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHItemName_Mdl>>> result);

    public void getTxtHOrderNo(String subCode, String v_date, String docId, String v_SNo, String restrictedDepartment, String voucherCategory, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHOrderNo_Mdl>>> result);

    public void getTxtHImpurity(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);
       
    public void getTxtHGstType(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Grids">
    public void getGrnDetail(String v_Type, List<KeyFieldExtended> keyFields, String subcode, String location, String billDate, String docId, String v_Date, String isSiteStateUT, AsyncCallback<DmResult<List<GrdGrnDetail_Mdl>>> result);

    public void getPrevGrn(String itemCode, String docId, String v_SNo, String supplierCode, boolean flagCurrentSupplier, DmHelper helper, boolean blnToEndOfFile, String param, AsyncCallback<DmResultPageing<List<GrdGrnDetail_Mdl>>> result);
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Approval">
    public void setApproval(String approvedBy, boolean isApprove, String docId, AsyncCallback<DmResult<String>> result);
    //</editor-fold>

    public void getServerDateTime(AsyncCallback<DmResult<String[]>> result);

    public void getDefaultValues(AsyncCallback<DmResult<DefaultValue_Mdl>> result);

    public void saveRecord(Grn_Mdl mrTempCurrent, Grn_Mdl mrTempPrevious, String voucherPrefix, boolean isApprove, AsyncCallback<DmResult<String>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<Grn_Mdl>> result);

    public void deleteRecord(String strKey, String guiConfigCode, AsyncCallback<DmResult<String>> result);

    public void getNavigator(UserPermission userPermission, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getFind(UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getGSTS(String itemCode, String itemRate, String itsBasedOn, String locationCode, String gstTypeCode,  boolean isManualGst, AsyncCallback<DmResult<List<GSTS_Mdl>>> result);

    public void getSupplierDetails(String supplierCode, String voucherCategory, boolean flagFilterByVoucherCategory, String v_Type, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResult<TxtHSupplier_Mdl>> result);

    public void getVoucherType(String partyType, AsyncCallback<DmResult<TxtHVoucherType_Mdl>> result);

    public void getTxtHTransporter(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHTransporter_Mdl>>> result);

    public void getTxtHGateEntryNo(String pChallanDocId, String supplierCode, String grnDate, String refDate, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHGateEntry_Mdl>>> asyncCallback);

    public void fillGateEntryDetail(String v_Type, String gateDocId, String partyCode, String location, String billDate, String v_Date, String isSiteStateUT, AsyncCallback<DmResult<List<GrdGrnDetail_Mdl>>> result);

    public void getCreateHelp(String helpSource, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getQCPaparameter(String itemCode, AsyncCallback<DmResult<List<GrdImpurity_Mdl>>> result);

    public void getTxtHRCMApplicable(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> asyncCallback);
    
    public void updateRecord(Grn_Mdl mrTempCurrent, boolean isApprove, AsyncCallback<DmResult<String>> result);
    
}
