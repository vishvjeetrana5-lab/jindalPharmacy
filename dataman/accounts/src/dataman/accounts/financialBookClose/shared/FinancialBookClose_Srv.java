package dataman.accounts.financialBookClose.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.UserPermission;
import java.util.List;

public interface FinancialBookClose_Srv extends RemoteService {

    public DmResult<DefaultValue_Mdl> getDefaultValues();

    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission, String vPrefix);

    public DmResult<FinancialBookClose_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(FinancialBookClose_Mdl mrTemp);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile, String vPrfix);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHVoucherType(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHAccountName_Mdl>> getTxtHAccountName(DmHelper pageing, boolean blnToEndOfFile);

}
