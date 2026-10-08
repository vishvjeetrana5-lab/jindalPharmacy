package aarogya.selfToolkit.displaySystemWS.server;

import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.Hashtable;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/queueDisplaySystem")
public class DisplaySystemWs {

    private static final Hashtable<String, Session> webSocketSessions = new Hashtable<>();

    public DisplaySystemWs() {
        System.out.println("displaySystem Endpoint is up and running...");
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig conf) {
        try {
            System.out.println("Connection open for:" + session.getId());

            if (!getWebSocketSessions().containsKey(session.getId())) {
                getWebSocketSessions().put(session.getId(), session);
                System.out.println("Session : " + session.getId() + " has been added. Total Connected Sessions : " + getWebSocketSessions().size() + " Actual Connected Websockets : " + session.getOpenSessions().size());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            System.out.println("Received WS Message : " + message);

            String receivedMessage = DmShared.xNull(message).trim();

            if (receivedMessage.equals("")) {
                session.close(new CloseReason(CloseReason.CloseCodes.PROTOCOL_ERROR, "First message must be a device Id to communicate further..."));
            }

            if (!getWebSocketSessions().containsKey(session.getId())) {
                session.close(new CloseReason(CloseReason.CloseCodes.PROTOCOL_ERROR, "Session Doesn't exit to communicate further..."));
            }

            if (!getWebSocketSessions().get(session.getId()).getUserProperties().containsKey("did")) {
                getWebSocketSessions().get(session.getId()).getUserProperties().put("did", receivedMessage);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        try {
            System.out.println("Connection is closing for ws : " + session.getId() + " for reason : " + closeReason.getReasonPhrase());

            if (getWebSocketSessions().containsKey(session.getId())) {
                getWebSocketSessions().remove(session.getId());
                session.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Hashtable<String, Session> getWebSocketSessions() {
        return webSocketSessions;
    }
}
