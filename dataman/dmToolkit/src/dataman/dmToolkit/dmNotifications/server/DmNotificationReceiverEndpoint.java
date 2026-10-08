package dataman.dmToolkit.dmNotifications.server;

import dataman.dmToolkit.dmStatic.server.DmServer;
import javax.websocket.EndpointConfig;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/receiver")
public class DmNotificationReceiverEndpoint {

    public DmNotificationReceiverEndpoint() {
        System.out.println("Notification Endpoint is up and running...");
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig conf) {
        System.out.println("Commection open for:" + session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            System.out.println("Received WS Message : " + message);
            DmServer.setNotificationSession(message, session);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
