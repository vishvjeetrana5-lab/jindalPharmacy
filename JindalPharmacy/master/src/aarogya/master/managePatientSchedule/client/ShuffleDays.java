package aarogya.master.managePatientSchedule.client;

import aarogya.master.managePatientSchedule.shared.*;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;

public class ShuffleDays extends ShuffleDays_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler {

    public interface CallBack {

        public void ok(String schdDocId);

        public void close();
    }

    private CallBack callBack;
    private final ServiceInvoker<ManagePatientSchedule_SrvAsync> srvAsync;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(ShuffleDays_Mdl mrTemp) {
        if (mrTemp != null) {
            popupPanel.show();
            popupPanel.center();
            popupPanel.setGlassEnabled(true);
            txtNoOfDays.setFocus(true);
            txtNoOfDays.setText(DmShared.iNull(mrTemp.getShuffleDays()).toString());
            txtNoOfDays.setTag(mrTemp.getDocId());
            txtNoOfDays.setTag2nd(DmShared.dNull(mrTemp.getSchduleDate()));
        }
    }

    public void hide() {
        this.popupPanel.hide();
    }

    private void hide(boolean isCallEvent) {
        this.popupPanel.hide();
        if (isCallEvent) {
            this.callBack.close();
        }
    }

    @Override
    public void onClick(ClickEvent event) {

        if (event.getSource().equals(btnCancel)) {
            hide(true);
        } else if (event.getSource().equals(btnOk)) {
            try {
                ShuffleDays_Mdl mrTemp = new ShuffleDays_Mdl();
                mrTemp.setDocId(txtNoOfDays.getTag());
                mrTemp.setSchduleDate(txtNoOfDays.getTag2nd());
                mrTemp.setShuffleDays(DmShared.iNull(txtNoOfDays.getText()).intValue());
                changeSchedule(mrTemp);
            } catch (Exception e) {
                new DmAlertMessageBox(e.getMessage(), btnOk).show();
            }
        }
    }

    private void changeSchedule(ShuffleDays_Mdl mrTemp) {
        srvAsync.invokeRPC().changeSchedules(mrTemp, new AsyncCallback<DmResult<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtNoOfDays).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {

                    //new DmAlertMessageBox("Schedules are suffle successfully.", txtNoOfDays).show();
                    callBack.ok(DmShared.iNull(result.getData()).toString());
                    ShuffleDays.this.hide(false);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtNoOfDays).show();
                }
            }
        });
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide(true);
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

    public ShuffleDays() {
        super();
        srvAsync = new ServiceInvoker<ManagePatientSchedule_SrvAsync>(GWT.create(ManagePatientSchedule_Srv.class), ManagePatientSchedule.class.getName());
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
        popupPanel.addAttachHandler(this);
    }

}
