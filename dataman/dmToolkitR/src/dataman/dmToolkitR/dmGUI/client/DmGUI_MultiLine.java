package dataman.dmToolkitR.dmGUI.client;

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

public class DmGUI_MultiLine extends DmGUI_MultiLine_Dsg implements IsWidget, DmRootEvents, AttachEvent.Handler, ClickHandler {

    public interface CallBack {

        public void onOk(String textVal, DmTextArea textArea);

        public void onClose(DmTextArea textArea);
    }

    public CallBack callBack;
    private DmTextArea textArea;
    private DmLabel label;

    public void addCallBack(DmTextArea textArea, DmLabel label, CallBack callBack) {
        this.callBack = callBack;
        this.label = label;
        this.textArea = textArea;
        this.txtRemark.setText(textArea.getText());
        this.txtRemark.setMaxLength(textArea.getMaxLength());
        this.label.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                DmGUI_MultiLine.this.show();
            }
        });

    }

    public void show() {
        modal.show();
        modal.setTitle(label.getText());
        this.txtRemark.setText(this.textArea.getText());
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
            this.callBack.onOk(DmShared.removeSpace(DmShared.xNull(txtRemark.getText())), textArea);
        } else {
            this.callBack.onClose(textArea);
        }

        this.modal.hide();
    }

    public DmGUI_MultiLine() {
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
