package dataman.accounts.finalPaymentRequestEntry.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.UserPermission;
import java.util.List;

public interface FinalPaymentRequestEntry_Srv extends RemoteService {

    public DmResult<DefaultValue_Mdl> getDefaultValues();

    public String[] getServerDateTime();

    public DmResultPageing<List<KeyFieldExtended>> getTxtHModuleType(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<GrdPBillSearchDetail_Mdl>> getPBSearch(String supplierTag, DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl param);

    public DmResult<String> saveRecord(FinalPaymentRequestEntry_Mdl mrTemp);

    public DmResult<FinalPaymentRequestEntry_Mdl> getMoveRec(String strKey);

    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission);

    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<String> deleteRecord(String strKey);

    public DmResult<FinalPaymentRequestEntry_Mdl> getPurchaseBillDetail(String pbNo, String pbDocId, String type, String supplierType);

    public DmResultPageing<List<TxtHSupplier_Mdl>> getTxtHSupplier(String v_Type, DmHelper helper, boolean blnToEndOfFile);

}
