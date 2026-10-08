package dataman.dmToolkitR.dmAlertBox.client;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkitR.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkitR.dmTextBox.client.DmTextBox;
import dataman.dmToolkitR.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkitR.dmTextHelp.client.DmTextHelp;
import org.gwtbootstrap3.client.shared.event.ModalHideEvent;
import org.gwtbootstrap3.client.shared.event.ModalHideHandler;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.extras.datepicker.client.ui.DatePicker;

public class DmAlertBox extends DmAlertBox_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, ValueChangeHandler<String>, ClickHandler {

    private String message;

    public void show() {
        lblMessage.getElement().setInnerHTML(message);
        modal.show();
    }

    public void hide() {

        modal.hide();
    }

    public DmAlertBox(String messageHTML, ModalHideHandler hideEvent) {
        super();
        this.message = messageHTML;
        modal.addAttachHandler(this);
        btnOk.addClickHandler(this);
        modal.addHideHandler(hideEvent);
    }

    public DmAlertBox(String messageHTML, final DmTextBox textbox) {
        super();
        this.message = messageHTML;
        modal.addAttachHandler(this);
        btnOk.addClickHandler(this);
        modal.addHideHandler(new ModalHideHandler() {
            @Override
            public void onHide(ModalHideEvent evt) {
                if (textbox != null) {
                    if (textbox.isAttached() && textbox.isEnabled() && textbox.isVisible()) {
                        textbox.setFocus(true);
                    }
                }
            }
        });
    }

    public DmAlertBox(String messageHTML, final DatePicker textbox) {
        super();
        this.message = messageHTML;
        modal.addAttachHandler(this);
        btnOk.addClickHandler(this);
        modal.addHideHandler(new ModalHideHandler() {
            @Override
            public void onHide(ModalHideEvent evt) {
                if (textbox != null) {
                    if (textbox.isAttached() && textbox.isEnabled() && textbox.isVisible()) {
                        textbox.getTextBox().setFocus(true);
                    }
                }
            }
        });
    }

    public DmAlertBox(String messageHTML, final DmTextBoxNumeric textbox) {
        super();
        this.message = messageHTML;
        modal.addAttachHandler(this);
        btnOk.addClickHandler(this);
        modal.addHideHandler(new ModalHideHandler() {
            @Override
            public void onHide(ModalHideEvent evt) {
                if (textbox != null) {
                    if (textbox.isAttached() && textbox.isEnabled() && textbox.isVisible()) {
                        textbox.setFocus(true);
                    }
                }
            }
        });
    }

    public DmAlertBox(String messageHTML, final PasswordTextBox textbox) {
        super();
        this.message = messageHTML;
        modal.addAttachHandler(this);
        btnOk.addClickHandler(this);
        modal.addHideHandler(new ModalHideHandler() {
            @Override
            public void onHide(ModalHideEvent evt) {
                if (textbox != null) {
                    if (textbox.isAttached() && textbox.isEnabled() && textbox.isVisible()) {
                        textbox.setFocus(true);
                    }
                }
            }
        });
    }

    public DmAlertBox(String messageHTML, final DmTextHelp textbox) {
        super();
        this.message = messageHTML;
        modal.addAttachHandler(this);
        btnOk.addClickHandler(this);
        modal.addHideHandler(new ModalHideHandler() {
            @Override
            public void onHide(ModalHideEvent evt) {
                if (textbox != null) {
                    if (textbox.isEnabled()) {
                        textbox.setFocus(true);

                    }
                }
            }
        });
    }

    public DmAlertBox(String messageHTML, final TextArea textArea) {
        super();
        this.message = messageHTML;
        modal.addAttachHandler(this);
        btnOk.addClickHandler(this);
        modal.addHideHandler(new ModalHideHandler() {
            @Override
            public void onHide(ModalHideEvent evt) {
                if (textArea != null) {
                    if (textArea.isAttached() && textArea.isEnabled() && textArea.isVisible()) {
                        textArea.setFocus(true);
                    }
                }
            }
        });
    }

    public DmAlertBox(String messageHTML, final Button button) {
        super();
        this.message = messageHTML;
        modal.addAttachHandler(this);
        btnOk.addClickHandler(this);
        modal.addHideHandler(new ModalHideHandler() {
            @Override
            public void onHide(ModalHideEvent evt) {
                if (button != null) {
                    if (button.isAttached() && button.isEnabled() && button.isVisible()) {
                        button.setFocus(true);
                    }
                }
            }
        });
    }

    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnOk) {
            hide();
        }
    }

    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
//        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
//            hide();
//        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    @Override
    public Widget asWidget() {
        return this.modal;
    }
}
