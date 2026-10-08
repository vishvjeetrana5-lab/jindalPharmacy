package dataman.dmToolkit.dmNotifications.shared;

import com.google.gwt.user.client.rpc.RemoteService;

public interface DmNotificationSender_Srv extends RemoteService {
    public DmNotification_Mdl uploadNotification(DmNotification_Mdl model);
}
