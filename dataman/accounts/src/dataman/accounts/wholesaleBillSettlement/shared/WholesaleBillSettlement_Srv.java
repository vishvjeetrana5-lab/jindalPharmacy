package dataman.accounts.wholesaleBillSettlement.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.UserPermission;
import java.util.List;

public interface WholesaleBillSettlement_Srv extends RemoteService {

    public DmResult<DefaultValue_Mdl> getDefaultValues();

    public DmResult<List<GrdPendingFPReqDetail_Mdl>> getPendingFinalPaymnetReqDetail(String supplierCode);

    public DmResult<List<GridDrAdjustBills_Mdl>> getPendingFinalCreditAdjmentRec(String supplierCode);

    public String[] getServerDateTime();

    public DmResult<String> saveRecord(WholesaleBillSettlement_Mdl mrTemp, boolean isApprove);

    public DmResult<WholesaleBillSettlement_Mdl> getMoveRec(String strKey);

    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission);

    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<String> deleteRecord(String strKey);

    public DmResult<String> setApproval(String approvedBy, boolean isApprove, String docId);

    public DmResultPageing<List<TxtHSupplier_Mdl>> getTxtHSupplier(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHVoucherType_Mdl>> getTxtHVoucherType(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHAccountName_Mdl>> getTxtHAccountName(DmHelper pageing, boolean blnToEndOfFile, String v_Type);

}
