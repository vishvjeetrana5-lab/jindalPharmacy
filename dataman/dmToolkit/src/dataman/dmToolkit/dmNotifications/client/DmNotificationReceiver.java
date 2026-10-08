package dataman.dmToolkit.dmNotifications.client;

import com.google.gwt.user.client.rpc.SerializationStreamFactory;
import dataman.dmToolkit.dmNotifications.shared.IReceiver;
import dataman.dmToolkit.dmWebSocket.client.WebsocketReceiver;
import dataman.dmToolkit.dmWebSocket.shared.CloseEvent;
import dataman.dmToolkit.dmWebSocket.shared.IWebsocketReceiver;

public class DmNotificationReceiver implements IWebsocketReceiver {

    private String urlReceiver;
    WebsocketReceiver wsReceiver;
    IReceiver receiver;
    SerializationStreamFactory streamFactory;

    boolean boolOpened = false;

    public DmNotificationReceiver(String url, IReceiver receiver) {
        this.urlReceiver = url.trim();
        this.receiver = receiver;
        this.wsReceiver = new WebsocketReceiver(this.urlReceiver);
        this.wsReceiver.open();
        this.wsReceiver.addListener(this);
//        this.streamFactory = (SerializationStreamFactory) GWT.create(DmNotificationSender_Srv.class);
    }

    @Override
    public void receivedMessage(String msg) throws Exception {
//        DmNotification_Mdl notification = deserializeString(msg);
//        switch (notification.getNotificationType()) {
//            case NONE: 
//                break;
//            case ALERT:
//                this.receiver.onMessageReceive(notification);
//                break;
//            case NOTIFICATION:
//                this.receiver.onMessageReceive(notification);                
//                break;
//        }
        this.receiver.onMessageReceive(msg);                
    }

//    private DmNotification_Mdl deserializeString(String msg) throws Exception {
//        final SerializationStreamReader reader = this.streamFactory.createStreamReader(msg);
//        DmNotification_Mdl model = (DmNotification_Mdl) reader.readObject();
//        return model;
//    }

    @Override
    public void onClosed(CloseEvent event) {
        receiver.onClosed(event);
    }

    @Override
    public void onOpened() {
        this.boolOpened = true;
        receiver.onOpened();
    }

    @Override
    public void onError() {
        receiver.onError();
    }

}
