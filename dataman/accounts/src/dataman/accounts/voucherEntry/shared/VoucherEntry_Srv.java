package dataman.accounts.voucherEntry.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.overHeadLine.shared.OverHeadITS_Mdl;
import dataman.dmToolkit.self.shared.*;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough_Srv;
import java.util.List;

public interface VoucherEntry_Srv extends RemoteService, IWalkThrough_Srv {
    
    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission, String posted, String voucherCategory);

    public DmResult<VoucherEntry_Mdl> getMoveRec(String strKey);

    public DmResult<SaveRecord_Mdl> saveRecord(UserPermission userPermission, VoucherEntry_Mdl mrTemp);

    public DmResult<String> deleteRecord(UserPermission userPermission, String strKey);

    public DmResult<String> printRecords(VoucherPrint_Mdl mdlDetails);

    public DmResult<Double[]> getGetLedgerBalance(String search);

    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, String voucherCategory, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHVoucherType_Mdl>> getTxtHVoucherType(String voucherCategory, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHCostCenter(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHCostCenterGrid_Mdl>> getTxtHCostCenterGrid(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHAccountName_Mdl>> getTxtHAccountName(DmHelper pageing, boolean blnToEndOfFile, String v_Type);

    public DmResultPageing<List<TxtHAccountNameGrid_Mdl>> getTxtHAccountNameGrid(DmHelper pageing, boolean blnToEndOfFile, String widgetName);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHTdsCategory(String voucherCategory,DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHTdsDeduct_Mdl>> getTxtHTdsDeduct(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHTdsFor(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDrCr(DmHelper helper, boolean blnToEndOfFile);
    
    public DmResult<List<TdsCategoryGrid_Mdl>> getTDSData(String strCode,double tdsOnAmt, String Vr_Date);

    public DmResult<String> setApproval(String approvedBy, boolean isApprove, String docId);

    public DmResultPageing<List<VoucherTypePrint_Mdl>> getTxtHVoucherTypePrint(String voucherCategory, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<VoucherEntryPrint_Mdl>> getTxtHAccountNamePrint(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHVoucherNoPrint(DmHelper helper, boolean blnToEndOfFile, String txtFromDate, String txtToDate, String txtHVoucherType, String txtHAccountName);

    public DmResult<Boolean> isNegativeCash(String docId, String acCode, double amount);

    public DmResultPageing<List<TxtHAccountNo_Mdl>> getTxtHAccountNo(String accountCode, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getYesNo(DmHelper pageing, boolean blnToEndOfFile);
}
