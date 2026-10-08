package dataman.webJobs.notificationEntry.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface NotificationEntry_SrvAsync {

    public void getNavigator(AsyncCallback<DmResult<List<KeyFieldExtended>>> result);



    public void getMoveRec(String strKey, AsyncCallback<DmResult<NotificationEntry_Mdl>> result);

    public void deleteRecord(String strKey, AsyncCallback<DmResult<String>> result);

    public void saveRecord(NotificationEntry_Mdl mrTemp, AsyncCallback<DmResult<String>> result);


    public void getFind(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void exportCSV(String sessionId, AsyncCallback<DmResult<KeyFieldExtended>> result);

    public void getServerDateTime(AsyncCallback<String[]> asyncCallback);
}
