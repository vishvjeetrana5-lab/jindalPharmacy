package aarogya.selfToolkit.remarkPopup.client;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.*;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class RemarkPopup extends RemarkPopup_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler {

    public interface CallBack {

        public void onOk(String textVal);
    }

    CallBack callBack;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(String remarkValue) {
        txtRemark.setText(DmShared.xNull(remarkValue));
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtRemark.setFocus(true);
            }
        });
    }

    private void handlerEvents() {
        btnOk.addClickHandler(this);
        popupPanel.addAttachHandler(this);
    }

    public void hide() {
        hide(false);
    }

    private void hide(boolean callBackNeeded) {
        this.popupPanel.hide();
        if (callBackNeeded) {
            callBack.onOk(DmShared.removeSpace(DmShared.xNull(txtRemark.getText())));
        }
    }

    public RemarkPopup() {
        super();
        handlerEvents();
    }

    public void RemarkPopup(int maxLengtgh, boolean isMandatory) {
        txtRemark.setMaxLength(DmShared.iNull(maxLengtgh).intValue());
        txtRemark.setMandatory(isMandatory);
    }

    @Override
    public Widget asWidget() {
        return popupPanel;
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnOk)) {
            try {
                hide();
            } catch (Exception e) {
                new DmAlertMessageBox(e.getMessage(), btnOk).show();
            }
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            this.hide(true);
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
