package aarogya.master.tabIndexSettings.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface TabIndexSettings_SrvAsync {

    public void getTxtHEntry(String module, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHModule(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void saveRecord(TabIndexSettings_Mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void getMoveRec(String module, String entry, AsyncCallback<DmResult<TabIndexSettings_Mdl>> result);

    public void deleteRecord(String module, String mnuName, AsyncCallback<DmResult<String>> result);

    public void exportCSV(String sessionId, AsyncCallback<DmResult<KeyFieldExtended>> result);

}
