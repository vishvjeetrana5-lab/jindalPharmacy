package dataman.accounts.gstVoucherEntry.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough_Srv;
import java.util.List;


public interface GstVoucherEntry_Srv extends RemoteService, IWalkThrough_Srv{

    public String[] getServerDateTime();

    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission, String voucherCategory);

    public DmResult<GstVoucherEntry_Mdl> getMoveRec(String strKey);

    public DmResult<SaveRecord_Mdl> saveRecord(UserPermission userPermission, GstVoucherEntry_Mdl mrTemp);

    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, String voucherCategory, DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<String> deleteRecord(UserPermission userPermission, String strKey);

    public DmResultPageing<List<TxtHVoucherType_Mdl>> getTxtHVoucherType(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHAccountName_Mdl>> getTxtHAccountName(DmHelper pageing, boolean blnToEndOfFile, String v_Type, String v_Date);

    public DmResultPageing<List<TxtHAccountNameGrid_Mdl>> getTxtHAccountNameGrid(DmHelper pageing, boolean blnToEndOfFile, String exceptAcCode, String v_Date);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHReason(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getYesNo(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<DefaultValue_Mdl> getDefaultValues();

    public DmResultPageing<List<KeyFieldExtended>> getTxtHState(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHRoundOffGst(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHGstType(String voucherCategory, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<String> setApproval(String approvedBy, boolean isApprove, String docId);

    public DmResultPageing<List<TxtHHSN_Mdl>> getTxtHHSN(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHRefVoucher_Mdl>> getTxtHRefVoucher(DmHelper pageing, boolean blnToEndOfFile, String subCode);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHTaxAccount(DmHelper pageing, boolean blnToEndOfFile);
}
