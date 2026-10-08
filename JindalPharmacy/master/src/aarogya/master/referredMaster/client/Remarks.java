package aarogya.master.referredMaster.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class Remarks extends Remarks_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler {

    public interface CallBack {

        public void onOk(String remark);

        public void onClose();
    }
    private ReferredMaster consultant;
    private CallBack callBack;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(ReferredMaster consultantMaster) {
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        txtRemark.setFocus(true);
        this.consultant = consultantMaster;
        clear();
        flwOk.clear();
        if (!consultant.topControl.getMode().equalsIgnoreCase("Browse")) {
            flwOk.add(btnOk);
        }
        txtRemark.setText(DmShared.xNull(consultant.strRemark));
    }

    public void hide() {
        this.popupPanel.hide();
    }

    private void textHelpEvents() {

    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            popupPanel.hide();
            callBack.onClose();
        } else if (event.getSource() == btnOk) {
            popupPanel.hide();
            callBack.onOk(DmShared.xNull(txtRemark.getText()));
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            callBack.onClose();
            hide();
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
    public Widget asWidget() {
        return this.popupPanel;
    }

    public Remarks() {
        super();
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        textHelpEvents();
    }

    private void clear() {
        txtRemark.setText("");
    }

}
