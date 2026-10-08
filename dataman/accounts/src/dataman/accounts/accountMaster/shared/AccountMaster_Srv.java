package dataman.accounts.accountMaster.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface AccountMaster_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResultPageing<List<String>> getAccountNameSuggestion(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<AccountMaster_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(AccountMaster_Mdl mrTemp);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHAccountNamePrefix(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHAccountGroup_Mdl>> getTxtHAccountGroup(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHCostCenter(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHCity_Mdl>> getTxtHCity(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHZone(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHLocation(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHPartyCategory(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHTDSCategory(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHReferencedTo_Mdl>> getTxtHReferencedTo(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDuplicate(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHLadgerGroup(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHPartyType(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHNature(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHYesNo(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<KeyFieldExtended> exportCSV(String sessionId);

    public DmResult<DefaultValue_Mdl> getDefaultValues();
}
