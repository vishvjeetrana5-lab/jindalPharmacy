package dataman.accounts.accountOpeningEntry.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface AccountOpeningEntry_SrvAsync {

    public void getNavigator(UserPermission userPermission, String vPrefix, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<AccountOpeningEntry_Mdl>> result);

    public void deleteRecord(String strKey, AsyncCallback<DmResult<String>> result);

    public void saveRecord(UserPermission userPermission, AccountOpeningEntry_Mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void getFind(UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile, String vPrfix, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getTxtHAccountName(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHAccountName_Mdl>>> result);

    public void getTxtHCostCenter(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHCostCenter_Mdl>>> result);

    public void getTxtHCostCenter(DmHelper pageing, boolean blnToEndOfFile, boolean BlnInsert, AsyncCallback<DmResultPageing<List<TxtHCostCenter_Mdl>>> result);

    public void getTxtHVoucherType(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getFrmGrdData(String accountNameCode, String VPrefix, AsyncCallback<DmResultPageing<List<GrdAccountOpeningEntry_Mdl>>> result);

    public void setApproval(String apporvedBy, String docId, AsyncCallback<DmResult<String>> result);

    public void getDefaultValues(AsyncCallback<DmResult<DefaultValue_Mdl>> result);
}
