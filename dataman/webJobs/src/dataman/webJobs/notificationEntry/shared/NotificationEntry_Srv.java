package dataman.webJobs.notificationEntry.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface NotificationEntry_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

 public String[] getServerDateTime();

    public DmResult<NotificationEntry_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(NotificationEntry_Mdl mrTemp);

    public DmResult<String> deleteRecord(String strKey);


    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<KeyFieldExtended> exportCSV(String sessionId);
}
