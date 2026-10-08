package dataman.dmToolkit.dmNotifications.server;

import dataman.dmToolkit.dmNotifications.shared.DmNotificationSender_Srv;
import dataman.dmToolkit.dmNotifications.shared.DmNotification_Mdl;
import dataman.dmBase.dmSession.server.DmRSSPrivate;

public class DmNotificationSender_SrvImpl extends DmRSSPrivate implements DmNotificationSender_Srv {

    @Override
    public DmNotification_Mdl uploadNotification(DmNotification_Mdl model) {
//        try {
//            model.setFromUser(DmShared.xNull(DmServer.getSsnUserName(this)));
//            String msgStr = serializeMessage(model);
//            
//            if (!model.getBroadCast()) {
//                Iterator<String> sessIter=DmSession.getSessions().keySet().iterator();
//                while(sessIter.hasNext()){
//                    String sessId=sessIter.next();
//                    if((DmSession.getSessions().get(sessId.trim()).getAttribute("UserName")!=null) && (DmSession.getSessions().get(sessId.trim()).getReceiverSession()!=null))
//                    {
//                       String userName=DmShared.xNull(DmSession.getSessions().get(sessId.trim()).getAttribute("UserName").toString());
//                       if(model.getToUsers().contains(userName.trim())){
//                           sendMessage(DmSession.getSessions().get(sessId.trim()).getReceiverSession(), msgStr);
//                       }
//                    }
//                }
//            } else if (model.getBroadCast()) {
//                Iterator<String> sessIter=DmSession.getSessions().keySet().iterator();
//                while(sessIter.hasNext()){
//                    String sessId=sessIter.next();
//                    if(DmSession.getSessions().get(sessId.trim()).getReceiverSession()!=null)
//                    {
//                       sendMessage(DmSession.getSessions().get(sessId.trim()).getReceiverSession(), msgStr);                       
//                    }
//                }
//            }
//
//            model.setTransportStatus(TransportationStatus.SENT_TO_REPIENT);
//            model.setServerMessage("Message sent Successfully to all Subjected Participants...");
//            model.setServerStatus(true);
//
//        } catch (Exception ex) {
//            model.setTransportStatus(TransportationStatus.FAILED_TO_SEND_TO_RECIPIENT);
//            model.setServerStatus(false);
//            model.setException(ex.getMessage());
//            ex.printStackTrace();
//        }
        return new DmNotificationSender_Server().uploadNotification(model);
    }

//    private void sendMessage(Session session, String message) throws Exception {
//        if (session != null) {
//            if (session.isOpen()) {
//                session.getBasicRemote().sendText(message);
//            }
//        }
//    }
//
//    private String serializeMessage(WSTransport_Mdl model) throws Exception {
//        ServerSerializationStreamWriter writer = new ServerSerializationStreamWriter(new SimpleSerializationPolicy());
//        writer.writeObject(model);
//        return writer.toString();
//    }
}
