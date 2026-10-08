package dataman.dmToolkit.dmWebSocket.shared;

public interface IWebsocketListener {

    public void onClosed(CloseEvent event);    

    public void onOpened();
    
    public void onError();
}
