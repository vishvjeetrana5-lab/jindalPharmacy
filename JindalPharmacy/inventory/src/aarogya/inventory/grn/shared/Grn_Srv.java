package aarogya.inventory.grn.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmToolkit.overHeadLine.shared.OverHeadITS_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.gsts.shared.GSTS_Mdl;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough_Srv;
import java.util.List;

public interface Grn_Srv extends RemoteService, IWalkThrough_Srv {

    //<editor-fold defaultstate="collapsed" desc="TxtHelps">
    public DmResultPageing<List<TxtHVoucherType_Mdl>> getTxtHVoucher(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHSupplier_Mdl>> getTxtHSupplier(String voucherCategory, boolean flagFilterByVoucherCategory, String v_Type, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHItemName_Mdl>> getTxtHItem(String v_Type, String skuCode, String purchaseUnitCode, String restrictedDepartment, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHOrderNo_Mdl>> getTxtHOrderNo(String subCode, String v_Date, String docId, String v_SNo, String restrictedDepartment, String voucherCategory, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHImpurity(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHGstType(DmHelper pageing, boolean blnToEndOfFile);
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Grids">
    public DmResult<List<GrdGrnDetail_Mdl>> getGrnDetail(String v_Type, List<KeyFieldExtended> keyFields, String subcode, String location, String billDate, String docId, String v_Date, String isSiteStateUT);

    public DmResultPageing<List<GrdGrnDetail_Mdl>> getPrevGrn(String itemCode, String docId, String v_SNo, String supplierCode, boolean flagCurrentSupplier, DmHelper helper, boolean blnToEndOfFile, String param);
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Approval">
    public DmResult<String> setApproval(String approvedBy, boolean isApprove, String docId);
    //</editor-fold>

    public DmResult<String[]> getServerDateTime();

    public DmResult<DefaultValue_Mdl> getDefaultValues();

    public DmResult<String> saveRecord(Grn_Mdl mrTempCurrent, Grn_Mdl mrTempPrevious, String voucherPrefix, boolean isApprove);

    public DmResult<Grn_Mdl> getMoveRec(String strKey);

    public DmResult<String> deleteRecord(String strKey, String guiConfigCode);

    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission);

    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<List<GSTS_Mdl>> getGSTS(String itemCode, String itemRate, String itsBasedOn, String locationCode, String gstTypeCode, boolean isManualGst);

    public DmResult<TxtHSupplier_Mdl> getSupplierDetails(String supplierCode, String voucherCategory, boolean flagFilterByVoucherCategory, String v_Type, DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<TxtHVoucherType_Mdl> getVoucherType(String partyType);

    public DmResultPageing<List<TxtHTransporter_Mdl>> getTxtHTransporter(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHGateEntry_Mdl>> getTxtHGateEntryNo(String pChallanDocId, String supplierCode, String grnDate, String refDate, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<List<GrdGrnDetail_Mdl>> fillGateEntryDetail(String v_Type, String gateDocId, String partyCode, String location, String billDate, String v_Date, String isSiteStateUT);

    public DmResultPageing<List<KeyFieldExtended>> getCreateHelp(String helpSource, DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<List<GrdImpurity_Mdl>> getQCPaparameter(String itemCode);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHRCMApplicable(DmHelper pageing, boolean blnToEndOfFile);
    
    public DmResult<String> updateRecord(Grn_Mdl mrTempCurrent, boolean isApprove);
}
