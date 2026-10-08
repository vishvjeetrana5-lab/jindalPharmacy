package dataman.dmToolkit.dmWebSocket.client;

import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmToolkit.dmWebSocket.shared.CloseEvent;
import java.util.HashSet;
import java.util.Set;
import dataman.dmToolkit.dmWebSocket.shared.IWebsocketReceiver;


public class WebsocketReceiver {    

    private static int counter = 1;
    

    private static native boolean _isWebsocket() /*-{
        return ("WebSocket" in window);
    }-*/;

    public static boolean isSupported() {
        return _isWebsocket();
    }

    private final Set<IWebsocketReceiver> listeners = new HashSet<IWebsocketReceiver>();

    private final String varName;
    private final String url;

    public WebsocketReceiver(String url) {
        this.url = url;
        this.varName = "gwtws-" + counter++;
    }

    private native void _close(String s) /*-{
        $wnd[s].close();
    }-*/;

    private native void _open(WebsocketReceiver ws, String s, String url) /*-{
        $wnd[s] = new WebSocket(url);
        $wnd[s].onopen = function() { ws.@dataman.dmToolkit.dmWebSocket.client.WebsocketReceiver::onOpen()(); };
        $wnd[s].onclose = function(evt) { ws.@dataman.dmToolkit.dmWebSocket.client.WebsocketReceiver::onClose(SLjava/lang/String;Z)(evt.code, evt.reason, evt.wasClean); };
        $wnd[s].onerror = function() { ws.@dataman.dmToolkit.dmWebSocket.client.WebsocketReceiver::onError()(); };
        $wnd[s].onmessage = function(msg) { ws.@dataman.dmToolkit.dmWebSocket.client.WebsocketReceiver::onMessage(Ljava/lang/String;)(msg.data); }
    }-*/;

    private native int _state(String s) /*-{
        return $wnd[s].readyState;
    }-*/;

    public void addListener(IWebsocketReceiver listener) {
        listeners.add(listener);
    }

    public void removeListener(IWebsocketReceiver listener) {
        listeners.remove(listener);
    }

    public void close() {
        _close(varName);
    }

    public int getState() {
        return _state(varName);
    }

    protected void onClose(short code, String reason, boolean wasClean) {
        CloseEvent event = new CloseEvent(code, reason, wasClean);
        for (IWebsocketReceiver listener : listeners) {
            listener.onClosed(event);
        }
    }

    public void onError() {
        for (IWebsocketReceiver listener : listeners) {
            ((IWebsocketReceiver) listener).onError();
        }
    }

    private void onOpen() {
        _send(varName, DmRequestBuilder.getSessionId());
        for (IWebsocketReceiver listener : listeners) {
            listener.onOpened();
        }
    }
    
    public void open() {
        _open(this, varName, url);        
    }
    
    private native void _send(String s, String msg) /*-{
        $wnd[s].send(msg);
    }-*/;

    private void onMessage(String msg) throws Exception {
        for (IWebsocketReceiver listener : listeners) {
            listener.receivedMessage(msg);            
        }
    }
}
