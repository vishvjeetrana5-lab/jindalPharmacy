package dataman.dmToolkit.dmNotifications.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;

public interface DmNotificationSender_SrvAsync extends RemoteService {
    public void uploadNotification(DmNotification_Mdl model, AsyncCallback<DmNotification_Mdl> callback);
}
