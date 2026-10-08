package dataman.dmToolkit.dmRemarkTextBox.client;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.*;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmTextArea.client.DmTextArea;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;

class DmRemarkPopup extends DmRemarkPopup_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler {

    public interface CallBack {

        public void onOk(String textVal);

        public void onClose();
    }

    CallBack callBack;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(DmRemarkTextBox dmRemarkTextBox) {
        txtAreaRemark.setMaxLength(dmRemarkTextBox.getMaxLength());
        txtAreaRemark.setMandatory(dmRemarkTextBox.isMandatory());
        txtAreaRemark.setEnabled(dmRemarkTextBox.isEnabled());
        txtAreaRemark.setText(dmRemarkTextBox.getText());
        flwOk.clear();
        if (dmRemarkTextBox.isEnabled()) {
            flwOk.add(btnOk);
        }
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtAreaRemark.setFocus(true);
                int cursorPos = txtAreaRemark.getText() != null ? txtAreaRemark.getText().length() : 0;
                if (cursorPos > 0) {
                    txtAreaRemark.setCursorPos(cursorPos);
                }
            }
        });
    }

    private void handlerEvents() {
        btnOk.addClickHandler(this);
        btnClose.addClickHandler(this);
        popupPanel.addAttachHandler(this);
    }

    public void hide(boolean isOkPressed) {
        this.popupPanel.hide();
        if (callBack != null) {
            if (isOkPressed) {
                callBack.onOk(txtAreaRemark.getText() != null ? txtAreaRemark.getText() : "");
            } else {
                callBack.onClose();
            }
        }
    }

    public DmRemarkPopup() {
        super();
        handlerEvents();
    }

    @Override
    public Widget asWidget() {
        return popupPanel;
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnOk)) {
            if (txtAreaRemark.isMandatory()) {
                if (txtAreaRemark.getText() == null || txtAreaRemark.getText().trim().equals("")) {
                    new DmAlertMessageBox("Remark is mandatory!", txtAreaRemark).show();
                    return;
                }
            }
            hide(true);
        } else if (event.getSource().equals(btnClose)) {
            hide(false);
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
            hide(true);
        } else if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide(false);
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
