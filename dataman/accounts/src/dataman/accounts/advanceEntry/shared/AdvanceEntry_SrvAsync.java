package dataman.accounts.advanceEntry.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.UserPermission;
import java.util.List;

public interface AdvanceEntry_SrvAsync {

    public void printRecords(ChequePrint_Mdl mdlDetails, AsyncCallback<DmResult<String>> result);

    public void getDefaultValues(AsyncCallback<DmResult<DefaultValue_Mdl>> result);

    public void getServerDateTime(AsyncCallback<String[]> result);

    public void getAdvReqSearch(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl param, AsyncCallback<DmResultPageing<List<GrdAdvReqSearchDetail_Mdl>>> result);

    public void saveRecord(AdvanceEntry_Mdl mrTemp, boolean isApprove, AsyncCallback<DmResult<String>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<AdvanceEntry_Mdl>> result);

    public void deleteRecord(String strKey, AsyncCallback<DmResult<String>> result);

    public void getNavigator(UserPermission userPermission, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getAdvReqEntryDetail(String advReqNo, String advReqDocId, String type, AsyncCallback<DmResult<AdvanceEntry_Mdl>> result);

    public void getFind(UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getTxtHCardType(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<GrdPayment_Mdl>>> result);

    public void getTxtHAccounts(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void setApproval(String approvedBy, boolean isApprove, String docId, AsyncCallback<DmResult<String>> result);

    public void getTDSData(long tdsCatCode, AsyncCallback<DmResult<List<TdsCategoryGrid_Mdl>>> result);

}
