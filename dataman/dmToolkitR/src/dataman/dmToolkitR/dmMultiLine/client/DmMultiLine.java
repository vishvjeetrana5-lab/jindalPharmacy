package dataman.dmToolkitR.dmMultiLine.client;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkitR.dmLabel.client.DmLabel;
import dataman.dmToolkitR.dmStatic.client.DmClient;
import dataman.dmToolkitR.dmStatic.shared.DmShared;
import dataman.dmToolkitR.dmTextArea.client.DmTextArea;

public class DmMultiLine extends DmMultiLine_Dsg implements IsWidget, DmRootEvents, AttachEvent.Handler, ClickHandler {

    void addCallBack(String text, String remark, CallBack callBack) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public interface CallBack {

        public void onOk(String textVal);

    }

    public CallBack callBack;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;

    }

    public void show(String pText, String pTitle, int maxLength) {

        this.txtRemark.setMaxLength(maxLength);
        this.txtRemark.setText(pText);
        modal.setTitle(pTitle);

        modal.show();

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtRemark.setFocus(true);
            }
        });
    }

    public void hide() {
        hide(false);
    }

    private void hide(boolean isOk) {
        if (isOk) {
            this.callBack.onOk(DmShared.removeSpace(DmShared.xNull(txtRemark.getText())));
        }

        this.modal.hide();
    }

    public DmMultiLine() {
        super();
        modal.addAttachHandler(this);
        btnOk.addClickHandler(this);
    }

    @Override
    public Widget asWidget() {
        return modal;
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

    @Override
    public void searchRecord(String key) {

    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnOk)) {
            hide(true);
        }
    }

}
