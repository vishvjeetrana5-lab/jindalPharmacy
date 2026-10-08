package dataman.dmToolkit.dmWebSocket.shared;

public interface IWebsocketReceiver extends IWebsocketListener {
    public void receivedMessage(String msg) throws Exception;
}
