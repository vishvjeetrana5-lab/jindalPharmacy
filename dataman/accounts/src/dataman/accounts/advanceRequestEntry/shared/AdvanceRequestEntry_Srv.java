package dataman.accounts.advanceRequestEntry.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.UserPermission;
import java.util.List;

public interface AdvanceRequestEntry_Srv extends RemoteService {

    public DmResult<DefaultValue_Mdl> getDefaultValues();

    public String[] getServerDateTime();

    public DmResultPageing<List<KeyFieldExtended>> getTxtHModuleType(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<GrdPurchaseOrderSearchDetail_Mdl>> getPoSearch(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl param);

    public DmResult<String> saveRecord(AdvanceRequestEntry_Mdl mrTemp);

    public DmResult<AdvanceRequestEntry_Mdl> getMoveRec(String strKey);

    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission);

    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<String> deleteRecord(String strKey,String remark);

    public DmResult<AdvanceRequestEntry_Mdl> getPurchaseOrderDetail(String poNo, String poDocId, String type);

}
