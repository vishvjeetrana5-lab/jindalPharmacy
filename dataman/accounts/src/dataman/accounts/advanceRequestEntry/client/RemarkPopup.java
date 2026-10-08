package dataman.accounts.advanceRequestEntry.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.IsWidget;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class RemarkPopup extends RemarkPopup_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, ClickHandler {

    public interface CallBack {

        public void onOk(String remark);

        public void onCancel();
    }

    private CallBack callBack;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void hide() {
        popupPanel.hide();
    }

    public void show() {
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        txtRemark.setText("");
        txtRemark.setFocus(true);
    }

    @Override
    public void onAttachOrDetach(AttachEvent event
    ) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent
    ) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide();
        }
    }

    @Override
    public void onClick(ClickEvent event
    ) {

        if (event.getSource() == btnCancel) {
            hide();
        } else if (event.getSource() == btnOk) {
            if (DmShared.xNull(txtRemark.getText()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Remark is Mandatory.", txtRemark).show();
                return;
            } else {
                hide();
                callBack.onOk(DmShared.xNull(txtRemark.getText()));
            }
        }

    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    public RemarkPopup() {
        super();
        popupPanel.addAttachHandler(this);
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);

    }

}
