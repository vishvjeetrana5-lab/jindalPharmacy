package dataman.accounts.accountOpeningEntry.shared;

import com.google.gwt.user.client.rpc.RemoteService;
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

public interface AccountOpeningEntry_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission, String vPrefix);

    public DmResult<AccountOpeningEntry_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(UserPermission userPermission, AccountOpeningEntry_Mdl mrTemp);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile, String vPrfix);

    public DmResultPageing<List<TxtHAccountName_Mdl>> getTxtHAccountName(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHCostCenter_Mdl>> getTxtHCostCenter(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHCostCenter_Mdl>> getTxtHCostCenter(DmHelper pageing, boolean blnToEndOfFile, boolean BlnInsert);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHVoucherType(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<GrdAccountOpeningEntry_Mdl>> getFrmGrdData(String accountNameCode, String VPrefix);

    public DmResult<String> setApproval(String approvedBy, String docId);

    public DmResult<DefaultValue_Mdl> getDefaultValues();

}
