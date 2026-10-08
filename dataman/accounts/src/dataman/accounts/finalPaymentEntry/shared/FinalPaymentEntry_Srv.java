package dataman.accounts.finalPaymentEntry.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.UserPermission;
import java.util.List;

public interface FinalPaymentEntry_Srv extends RemoteService {

    public DmResult<DefaultValue_Mdl> getDefaultValues();

    public DmResult<List<GrdPendingFPReqDetail_Mdl>> getPendingFinalPaymnetReqDetail(String supplierCode, String moduleType);

    public DmResult<List<GridDrdAdjust_Mdl>> getPendingFinalDebitAdjmentRec(String supplierCode);

    public String[] getServerDateTime();

    public DmResult<String> saveRecord(FinalPaymentEntry_Mdl mrTemp, boolean isApprove);

    public DmResult<FinalPaymentEntry_Mdl> getMoveRec(String strKey);

    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission);

    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<GrdPayment_Mdl>> getTxtHCardType(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHAccounts(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHOtherAC_Mdl>> getTxtHOtherAC(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<String> setApproval(String approvedBy, boolean isApprove, String docId);

    public DmResult<List<TdsCategoryGrid_Mdl>> getTDSData(long tdsCatCode);

    public DmResultPageing<List<TxtHSupplier_Mdl>> getTxtHSupplier(String v_Type, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHModuleType(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<String> printRecords(ChequePrint_Mdl mdlDetails);

}
