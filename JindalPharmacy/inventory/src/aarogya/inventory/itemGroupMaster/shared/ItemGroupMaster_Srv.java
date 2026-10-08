package aarogya.inventory.itemGroupMaster.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface ItemGroupMaster_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResultPageing<List<String>> getSuggestionGroup(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<ItemGroupMaster_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(ItemGroupMaster_Mdl mrTemp);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<KeyFieldExtended> exportCSV(String sessionId);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHCategory(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHGroup_Mdl>> getTxtHGroup(DmHelper pageing, boolean blnToEndOfFile, String strCode);
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSubCode(String subgroupNature, DmHelper pageing, boolean blnToEndOfFile);
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHRestrictedDepartment(DmHelper helper, boolean blnToEndOfFile);

}
