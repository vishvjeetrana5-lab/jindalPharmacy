package dataman.dmToolkit.dmGUI.client;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.*;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextArea.client.DmTextArea;

public class DmGUI_MultiLine extends DmGUI_MultiLine_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler {

    public interface CallBack {

        public void onOk(String textVal, DmTextArea textArea);

        public void onClose(DmTextArea textArea);
    }

    private CallBack callBack;
    private DmTextArea textArea;
    private DmLabel label;

    public void addCallBack(DmTextArea textArea, DmLabel label, CallBack callBack) {
        this.callBack = callBack;
        this.textArea = textArea;
        this.label = label;
        this.textArea.setMaxLength(textArea.getMaxLength());
        this.txtRemark.setMaxLength(textArea.getMaxLength());
        this.label.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                DmGUI_MultiLine.this.show();
            }
        });
//        textArea.addKeyDownHandler(new KeyDownHandler() {
//            @Override
//            public void onKeyDown(KeyDownEvent event) {
//                if (event.getNativeKeyCode() == KeyCodes.KEY_SPACE) {
//                    DmGUI_MultiLine.this.show();
//                }
//            }
//        });
    }

    public void show() {
        toolContainer.setTitle(label.getText());
        txtRemark.setText(textArea.getText());
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);

        flwOk.clear();
        flwCancel.clear();
        flwOk.add(btnOk);
//        if (okNeeded) {
//            flwOk.add(btnOk);
//        } else {
//            flwCancel.add(btnCancel);
//        }

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

    private void hide(boolean callBackNeeded) {
        if (callBackNeeded) {
            if (btnOk.isAttached() && btnOk.isEnabled() && btnOk.isVisible()) {
                this.callBack.onOk(DmShared.removeSpace(DmShared.xNull(txtRemark.getText())), textArea);
            } else if (btnCancel.isAttached() && btnCancel.isEnabled() && btnCancel.isVisible()) {
                this.callBack.onClose(textArea);
            }
        }
        this.popupPanel.hide();
    }

    public DmGUI_MultiLine() {
        super();
        btnOk.addClickHandler(this);
        btnCancel.addClickHandler(this);
        popupPanel.addAttachHandler(this);
    }

    @Override
    public Widget asWidget() {
        return popupPanel;
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnOk) || event.getSource().equals(btnCancel)) {
            this.hide(true);
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
