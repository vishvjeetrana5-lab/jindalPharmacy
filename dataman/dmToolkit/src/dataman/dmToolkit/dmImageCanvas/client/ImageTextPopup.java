package dataman.dmToolkit.dmImageCanvas.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class ImageTextPopup extends ImageTextPopup_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler {

    public interface CallBack {

        public void hide(boolean isCancel);
    }

    private CallBack callBack;
    private HandlerRegistration handlerRegistrationOk;
    private HandlerRegistration handlerRegistrationClose;

    public ImageTextPopup() {
        super();
        popupPanel.addAttachHandler(this);
        toolContainer.setTitle("Image Text");
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwClose);
        toolContainer.addGap();
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void hide() {
        this.popupPanel.hide();
    }

    private void hide(boolean isCancel) {
        hide();
        callBack.hide(isCancel);
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == this.btnOk) {
            hide(false);
        } else if (event.getSource() == btnClose) {
            hide(true);
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide(false);
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
            handlerRegistrationClose = this.btnClose.addClickHandler(this);
            handlerRegistrationOk = this.btnOk.addClickHandler(this);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            if (handlerRegistrationOk != null) {
                handlerRegistrationClose.removeHandler();
            }
            if (handlerRegistrationOk != null) {
                handlerRegistrationOk.removeHandler();
            }
        }
    }

    public void show() {
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        if (DmShared.iNull(txtFontSize.getText()).longValue() <= 0) {
            txtFontSize.setText("12");
        }
        txtText.setFocus(true);
    }

    public String getText() {
        return DmShared.xNull(txtText.getText());
    }

    public Integer getFontSize() {
        return DmShared.vNull(txtFontSize.getText()).intValue();
    }

    public void center() {
        this.popupPanel.center();
    }

}
