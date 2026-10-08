package dataman.accounts.gstVoucherEntry.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough_SrvAsync;
import java.util.List;

public interface GstVoucherEntry_SrvAsync extends IWalkThrough_SrvAsync {

    public void getServerDateTime(AsyncCallback<String[]> result);

    public void getNavigator(UserPermission userPermission, String voucherCategory, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<GstVoucherEntry_Mdl>> result);

    public void deleteRecord(UserPermission userPermission, String strKey, AsyncCallback<DmResult<String>> result);

    public void saveRecord(UserPermission userPermission, GstVoucherEntry_Mdl mrTemp, AsyncCallback<DmResult<SaveRecord_Mdl>> result);

    public void getFind(UserPermission userPermission, String voucherCategory, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getTxtHVoucherType(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHVoucherType_Mdl>>> result);

    public void getTxtHAccountName(DmHelper pageing, boolean blnToEndOfFile, String v_Type, String v_Date, AsyncCallback<DmResultPageing<List<TxtHAccountName_Mdl>>> result);

    public void getTxtHAccountNameGrid(DmHelper pageing, boolean blnToEndOfFile, String exceptAcCode, String v_Date, AsyncCallback<DmResultPageing<List<TxtHAccountNameGrid_Mdl>>> result);

    public void getTxtHReason(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getYesNo(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getDefaultValues(AsyncCallback<DmResult<DefaultValue_Mdl>> result);

    public void getTxtHHSN(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHHSN_Mdl>>> result);

    public void getTxtHState(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHRoundOffGst(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void setApproval(String approvedBy, boolean isApprove, String docId, AsyncCallback<DmResult<String>> result);

    public void getTxtHGstType(String voucherCategory, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHRefVoucher(DmHelper pageing, boolean blnToEndOfFile, String subCode, AsyncCallback<DmResultPageing<List<TxtHRefVoucher_Mdl>>> result);
    
    public void getTxtHTaxAccount(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

}
