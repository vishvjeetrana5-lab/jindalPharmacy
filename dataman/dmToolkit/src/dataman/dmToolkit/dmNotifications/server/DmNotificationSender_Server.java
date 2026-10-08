package dataman.dmToolkit.dmNotifications.server;

import com.google.gwt.user.server.rpc.impl.ServerSerializationStreamWriter;
import dataman.dmToolkit.dmNotifications.shared.TransportationStatus;
import dataman.dmToolkit.dmNotifications.shared.DmNotification_Mdl;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.Iterator;
import javax.websocket.Session;

public class DmNotificationSender_Server {

    public DmNotification_Mdl uploadNotification(DmNotification_Mdl model) {
        try {
            String msgStr = serializeMessage(model);

            if (!model.isBroadCast()) {
                Iterator<String> sessIter = DmSession.getSessions().keySet().iterator();
                while (sessIter.hasNext()) {
                    String sessId = sessIter.next();
                    if ((DmSession.getSessions().get(sessId.trim()).getAttribute("UserName") != null) && (DmSession.getSessions().get(sessId.trim()).getReceiverSession() != null)) {
                        String userName = DmShared.xNull(DmSession.getSessions().get(sessId.trim()).getAttribute("UserName").toString());
                        for (String uName : model.getToUserList()) {
                            if (DmShared.xNull(uName).trim().equalsIgnoreCase(userName)) {
                                sendMessage(DmSession.getSessions().get(sessId.trim()).getReceiverSession(), msgStr);
                            }
                        }
                    }
                }
            } else if (model.isBroadCast()) {
                Iterator<String> sessIter = DmSession.getSessions().keySet().iterator();
                while (sessIter.hasNext()) {
                    String sessId = sessIter.next();
                    if (DmSession.getSessions().get(sessId.trim()).getReceiverSession() != null) {
                        sendMessage(DmSession.getSessions().get(sessId.trim()).getReceiverSession(), msgStr);
                    }
                }
            }

            model.setStatusTransportation(TransportationStatus.SENT_TO_REPIENT);
            model.setStatusMessage("Message sent Successfully to all Subjected Participants...");
            model.setStatus(true);

        } catch (Exception ex) {
            model.setStatusTransportation(TransportationStatus.FAILED_TO_SEND_TO_RECIPIENT);
            model.setStatusMessage(ex.getMessage());
            model.setStatus(false);
        }
        return model;
    }

    private void sendMessage(Session session, String message) throws Exception {
        if (session != null) {
            if (session.isOpen()) {
                session.getBasicRemote().sendText(message);
            }
        }
    }

    private String serializeMessage(DmNotification_Mdl model) throws Exception {
        return DmServer.<DmNotification_Mdl>objectToBase64(model);
//        ServerSerializationStreamWriter writer = new ServerSerializationStreamWriter(new SimpleSerializationPolicy());
//        writer.writeObject(model);
//        return writer.toString();
    }
}
