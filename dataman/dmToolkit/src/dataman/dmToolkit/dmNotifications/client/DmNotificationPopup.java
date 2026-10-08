package dataman.dmToolkit.dmNotifications.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkit.dmNotifications.shared.DmNotification_Mdl;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public final class DmNotificationPopup extends DmNotificationPopup_Dsg  {

    DmNotification_Mdl notification;

    @Override
    public void searchRecord(String key) {

    }

    @Override
    public void onBtnClick(ClickEvent event) {
        Object aSource = event.getSource();
        if (aSource.equals(aBtn)) {
            this.flwForm.removeFromParent();
        }
    }


    public DmNotificationPopup(DmNotification_Mdl notification) {
        super();
        this.asWidget().setTitle(widgetName);
        flwForm.getElement().setAttribute("style", "max-height:90px; min-height:60px; width:100%;");
        this.notification = notification;
        onLoad();
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    public void onLoad() {
        LblMessage.setText(DmShared.xNull(notification.getMessage()));
        LblMessageDateTime.setText(DmShared.xNull(notification.getMessageDate()) + " " +DmShared.xNull(notification.getMessageTime())); 
        LblTitle.setText(DmShared.xNull(notification.getMessageTitle()));
        LblUserName.setText(DmShared.xNull(notification.getFromUser()));

    }

}
