package dataman.dmToolkit.dmNotifications.shared;

import dataman.dmToolkit.dmWebSocket.shared.CloseEvent;

public interface IReceiver {    
    
    public void onClosed(CloseEvent event);
    
    public void onError();
    
    public void onOpened();
    
    public void onMessageReceive(String msg);
}
