package dataman.accounts.voucherEntryPaymentJV.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.overHeadLine.shared.OverHeadITS_Mdl;
import dataman.dmToolkit.self.shared.*;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough_SrvAsync;
import java.util.List;

public interface VoucherEntryPaymentJV_SrvAsync extends IWalkThrough_SrvAsync {

    public void getNavigator(UserPermission userPermission, String posted, String voucherCategory, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<VoucherEntryPaymentJV_Mdl>> result);

    public void deleteRecord(UserPermission userPermission, String strKey, AsyncCallback<DmResult<String>> result);

    public void printRecords(VoucherPrintPaymentJV_Mdl mdlDetails, AsyncCallback<DmResult<String>> result);

    public void saveRecord(UserPermission userPermission, VoucherEntryPaymentJV_Mdl mrTemp, String voucherShortName, AsyncCallback<DmResult<SaveRecord_Mdl>> result);

    public void getGetLedgerBalance(String search, String startDate,String loginDate, AsyncCallback<DmResult<LedgerBalance_Mdl>> result);

    public void getFind(UserPermission userPermission, String voucherCategory, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getTxtHVoucherType(String voucherCategory, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHVoucherType_Mdl>>> result);

    public void getTxtHCostCenter(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHCostCenterGrid(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHCostCenterGrid_Mdl>>> result);

    public void getTxtHAccountName(DmHelper pageing, boolean blnToEndOfFile, String v_Type, AsyncCallback<DmResultPageing<List<TxtHAccountName_Mdl>>> result);

    public void getTxtHAccountNameGrid(DmHelper pageing, boolean blnToEndOfFile, String widgetName, AsyncCallback<DmResultPageing<List<TxtHAccountNameGrid_Mdl>>> result);

    public void getTxtHTdsCategory(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHTdsDeduct(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHTdsDeduct_Mdl>>> result);

    public void getTxtHDrCr(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHTdsFor(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTDSData(String strCode, AsyncCallback<DmResult<List<TdsCategoryGrid_Mdl>>> result);

    public void setApproval(String approvedBy, boolean isApprove, String docId, AsyncCallback<DmResult<String>> result);

    public void getTxtHVoucherTypePrint(String voucherCategory, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<VoucherTypePrintPaymentJV_Mdl>>> result);

    public void getTxtHAccountNamePrint(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<VoucherEntryPrint_Mdl>>> result);

    public void getTxtHVoucherNoPrint(DmHelper helper, boolean blnToEndOfFile, String txtFromDate, String txtToDate, String txtHVoucherType, String txtHAccountName, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void isNegativeCash(String docId, String acCode, double amount, AsyncCallback<DmResult<Boolean>> result);

    public void getTxtHAccountNo(String accountCode, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHAccountNo_Mdl>>> result);

    public void getYesNo(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

}
