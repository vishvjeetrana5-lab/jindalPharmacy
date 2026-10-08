package dataman.accounts.advanceEntry.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.UserPermission;
import java.util.List;

public interface AdvanceEntry_Srv extends RemoteService {

    public DmResult<String> printRecords(ChequePrint_Mdl mdlDetails);

    public DmResult<DefaultValue_Mdl> getDefaultValues();

    public String[] getServerDateTime();

    public DmResultPageing<List<GrdAdvReqSearchDetail_Mdl>> getAdvReqSearch(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl param);

    public DmResult<String> saveRecord(AdvanceEntry_Mdl mrTemp, boolean isApprove);

    public DmResult<AdvanceEntry_Mdl> getMoveRec(String strKey);

    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission);

    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<String> deleteRecord(String strKey);

    public DmResult<AdvanceEntry_Mdl> getAdvReqEntryDetail(String advReqNo, String advReqDocId, String type);

    public DmResultPageing<List<GrdPayment_Mdl>> getTxtHCardType(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHAccounts(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<String> setApproval(String approvedBy, boolean isApprove, String docId);

    public DmResult<List<TdsCategoryGrid_Mdl>> getTDSData(long tdsCatCode);

}
