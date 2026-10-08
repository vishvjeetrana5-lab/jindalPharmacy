package aarogya.master.tabIndexSettings.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface TabIndexSettings_Srv extends RemoteService {

    public DmResultPageing<List<KeyFieldExtended>> getTxtHEntry(String module, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHModule(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<String> saveRecord(TabIndexSettings_Mdl mrTemp);

    public DmResult<TabIndexSettings_Mdl> getMoveRec(String module, String entry);

    public DmResult<String> deleteRecord(String module, String entry);

    public DmResult<KeyFieldExtended> exportCSV(String sessionId);

}
