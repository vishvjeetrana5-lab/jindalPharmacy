package dataman.dmToolkit.dmMessageBox.client;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextArea;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import static com.sencha.gxt.widget.core.client.box.MessageBox.ICONS;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import dataman.dmToolkit.dmGUI.client.DmGUI;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmDateBox.client.DmDateBox;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxMobile.client.DmTextBoxMobile;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextBoxTime.client.DmTextBoxTime;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;

public class DmAlertMessageBox extends AlertMessageBox implements DmRootEvents, AttachEvent.Handler {

    public DmAlertMessageBox(String message, HideEvent.HideHandler hideEvent) {
        super("Alert window ....", message);
        setIcon(ICONS.info());
        this.addAttachHandler(this);
        this.addHideHandler(hideEvent);
    }
    
    public DmAlertMessageBox(String message, final DmDateBox textbox) {
        super("Alert window ....", message);
        setIcon(ICONS.info());
        this.addAttachHandler(this);
        this.addHideHandler(new HideEvent.HideHandler() {

            @Override
            public void onHide(HideEvent event) {
                if (textbox != null) {
                    if (textbox.isAttached() && textbox.isEnabled() && textbox.isVisible()) {
                        textbox.setFocus(true);
                    }
                }
            }
        });
    }
    
    public DmAlertMessageBox(String message, final DmTextBox textbox) {
        super("Alert window ....", message);
        setIcon(ICONS.info());
        this.addAttachHandler(this);
        this.addHideHandler(new HideEvent.HideHandler() {

            @Override
            public void onHide(HideEvent event) {
                if (textbox != null) {
                    if (textbox.isAttached() && textbox.isEnabled() && textbox.isVisible()) {
                        textbox.setFocus(true);
                    }
                }
            }
        });
    }

    public DmAlertMessageBox(String message, final DmTextBoxTime textbox) {
        super("Alert window ....", message);
        setIcon(ICONS.info());
        this.addAttachHandler(this);
        this.addHideHandler(new HideEvent.HideHandler() {

            @Override
            public void onHide(HideEvent event) {
                if (textbox != null) {
                    if (textbox.isAttached() && textbox.isEnabled() && textbox.isVisible()) {
                        textbox.setFocus(true);
                    }
                }
            }
        });
    }

    public DmAlertMessageBox(String message, final PasswordTextBox textbox) {
        super("Alert window ....", message);
        setIcon(ICONS.info());
        this.addAttachHandler(this);
        this.addHideHandler(new HideEvent.HideHandler() {

            @Override
            public void onHide(HideEvent event) {
                if (textbox != null) {
                    if (textbox.isAttached() && textbox.isEnabled() && textbox.isVisible()) {
                        textbox.setFocus(true);
                    }
                }
            }
        });
    }

    public DmAlertMessageBox(String message, final DmTextBoxNumeric textbox) {
        super("Alert window ....", message);
        setIcon(ICONS.info());
        this.addAttachHandler(this);
        this.addHideHandler(new HideEvent.HideHandler() {

            @Override
            public void onHide(HideEvent event) {
                if (textbox != null) {
                    if (textbox.isAttached() && textbox.isEnabled() && textbox.isVisible()) {
                        textbox.setFocus(true);
                    }
                }
            }
        });
    }

    public DmAlertMessageBox(String message, final DmTextBoxMobile textbox) {
        super("Alert window ....", message);
        setIcon(ICONS.info());
        this.addAttachHandler(this);
        this.addHideHandler(new HideEvent.HideHandler() {

            @Override
            public void onHide(HideEvent event) {
                if (textbox != null) {
                    if (textbox.isAttached() && textbox.isEnabled() && textbox.isVisible()) {
                        textbox.setFocus(true);
                    }
                }
            }
        });
    }

    public DmAlertMessageBox(String message, final DmSuggestBox textbox) {
        super("Alert window ....", message);
        setIcon(ICONS.info());
        this.addAttachHandler(this);
        this.addHideHandler(new HideEvent.HideHandler() {

            @Override
            public void onHide(HideEvent event) {
                if (textbox != null) {
                    if (textbox.isAttached() && textbox.isEnabled() && textbox.isVisible()) {
                        textbox.setFocus(true);
                    }
                }
            }
        });
    }

    public DmAlertMessageBox(String message, final DmTextHelp textbox) {
        super("Alert window ....", message);
        setIcon(ICONS.info());
        this.addAttachHandler(this);
        this.addHideHandler(new HideEvent.HideHandler() {

            @Override
            public void onHide(HideEvent event) {
                if (textbox != null) {
                    if (textbox.isEnabled()) {
                        textbox.setFocus(true);
                    }
                }
            }
        });
    }

    public DmAlertMessageBox(String message, final TextArea textArea) {
        super("Alert window ....", message);
        setIcon(ICONS.info());
        this.addAttachHandler(this);
        this.addHideHandler(new HideEvent.HideHandler() {

            @Override
            public void onHide(HideEvent event) {
                if (textArea != null) {
                    if (textArea.isAttached() && textArea.isEnabled() && textArea.isVisible()) {
                        textArea.setFocus(true);
                    }
                }
            }
        });
    }

    public DmAlertMessageBox(String message, final DmTextHelpMulti textbox) {
        super("Alert window ....", message);
        setIcon(ICONS.info());
        this.addAttachHandler(this);
        this.addHideHandler(new HideEvent.HideHandler() {

            @Override
            public void onHide(HideEvent event) {
                if (textbox != null) {
                    if (textbox.isEnabled()) {
                        textbox.setFocus(true);
                    }
                }
            }
        });
    }

    public DmAlertMessageBox(String message, final Button button) {
        super("Alert window ....", message);
        setIcon(ICONS.info());
        this.addAttachHandler(this);
        this.addHideHandler(new HideEvent.HideHandler() {

            @Override
            public void onHide(HideEvent event) {
                if (button != null) {
                    if (button.isEnabled() && button.isAttached() && button.isVisible()) {
                        button.setFocus(true);
                    }
                }
            }
        });
    }

    public DmAlertMessageBox(String message, final DmGUI gui, final String saveInField) {
        super("Alert window ....", message);
        setIcon(ICONS.info());
        this.addAttachHandler(this);

        this.addHideHandler(new HideEvent.HideHandler() {

            @Override
            public void onHide(HideEvent event) {
                if (gui != null) {
                    if (!DmShared.xNull(saveInField).trim().equalsIgnoreCase("")) {
                        gui.setFocus(saveInField);
                    } else {
                        gui.setFocus();
                    }
                }
            }
        });
    }

    public DmAlertMessageBox(String message, final DmRemarkTextBox textBox) {
        super("Alert window ....", message);
        setIcon(ICONS.info());
        this.addAttachHandler(this);
        this.addHideHandler(new HideEvent.HideHandler() {

            @Override
            public void onHide(HideEvent event) {
                if (textBox != null) {
                    if (textBox.isEnabled()) {
                        textBox.setFocus(true);
                    }
                }
            }
        });
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            this.hide();
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
