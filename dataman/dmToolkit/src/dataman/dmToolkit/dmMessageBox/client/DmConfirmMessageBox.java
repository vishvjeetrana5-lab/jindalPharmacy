package dataman.dmToolkit.dmMessageBox.client;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;

public class DmConfirmMessageBox extends ConfirmMessageBox implements DmRootEvents, AttachEvent.Handler {

    public DmConfirmMessageBox(String message, DialogHideEvent.DialogHideHandler hideEvent) {
//        super("Confirm window ....", message, new DmWindowAppearance(), new DmMessageBoxAppearance());
        super("Confirm window ....", message);
//        setIcon(ICONS.question());
//        setPredefinedButtons(PredefinedButton.YES, PredefinedButton.NO);
        this.addAttachHandler(this);
        this.addDialogHideHandler(hideEvent);
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();

        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            this.hide();
        } else if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_Y) {
            rootEvent.getEvent().getNativeEvent().preventDefault();
            this.onButtonPressed(this.getButton(PredefinedButton.YES));
        } else if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_N) {
            rootEvent.getEvent().getNativeEvent().preventDefault();
            this.onButtonPressed(this.getButton(PredefinedButton.NO));
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }
}
