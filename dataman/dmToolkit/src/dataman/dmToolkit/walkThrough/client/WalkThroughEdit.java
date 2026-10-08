package dataman.dmToolkit.walkThrough.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
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
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.walkThrough.shared.*;

public class WalkThroughEdit extends WalkThroughEdit_Dsg implements IsWidget, ClickHandler, AttachEvent.Handler, DmRootEvents {

    interface CallBack {

        public void onOk(WalkThrough_Mdl grdWalkThrough_Mdl);

        public void onClose();

    }

    private ServiceInvoker<WalkThrough_SrvAsync> SrvAsync;
    private CallBack callBack;

    private WalkThrough_Mdl walkThroughDetail_Mdl;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void hide() {
        popupPanel.hide();
    }

    public void show(WalkThrough_Mdl walkThroughDetail_Mdl) {
        this.walkThroughDetail_Mdl = walkThroughDetail_Mdl;
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);

        flwAccept.clear();
        flwDecline.clear();

        if (DmShared.iNull(walkThroughDetail_Mdl.getWtaV_SNo()).intValue() != 1) {
            flwDecline.add(btnDecline);
        }
        if (DmShared.iNull(walkThroughDetail_Mdl.getStatus()).intValue() == 0) {
            flwAccept.add(btnAccept);
        }

        fillData(walkThroughDetail_Mdl);

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtRemark.setFocus(true);
            }
        });
    }

    private void fillData(WalkThrough_Mdl walkThrough_Mdl) {
        txtAuthority.setEnabled(false);
        txtAuthorisedBy.setEnabled(false);
        txtLinkedUser.setEnabled(false);
        txtAuthorisedDateTime.setEnabled(false);
        txtAuthority.setText(walkThrough_Mdl.getAuthority());
        txtAuthorisedBy.setText(walkThrough_Mdl.getAuthorisedBy());
        txtAuthorisedDateTime.setText(walkThrough_Mdl.getAuthorisedDateTime());
        txtLinkedUser.setText(walkThrough_Mdl.getLinkedUser());
        txtRemark.setText(walkThrough_Mdl.getRemark());
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            this.popupPanel.hide();
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            txtAuthority.setFocus(true);
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    private void txtKeyEvents() {

    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            popupPanel.hide();
            callBack.onClose();
        } else if (event.getSource() == btnAccept) {
            onOK(true);
        } else if (event.getSource() == btnDecline) {
            onOK(false);
        }
    }

    private void onOK(boolean isAccept) {
        WalkThrough_Mdl mrTemp;
        mrTemp = grdWalkThroughInitialize();
        mrTemp.setAccept(isAccept);
        popupPanel.hide();
        callBack.onOk(mrTemp);
    }

    private WalkThrough_Mdl grdWalkThroughInitialize() {
        WalkThrough_Mdl mrTemp = new WalkThrough_Mdl();

        mrTemp = walkThroughDetail_Mdl;
        mrTemp.setRemark(DmShared.xNull(txtRemark.getText()));

        return mrTemp;
    }

    public WalkThroughEdit() {
        super();
        SrvAsync = new ServiceInvoker<WalkThrough_SrvAsync>(GWT.create(WalkThrough_Srv.class), WalkThrough.class.getName());
        btnCancel.addClickHandler(this);
        btnAccept.addClickHandler(this);
        btnDecline.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        txtKeyEvents();
    }
}
