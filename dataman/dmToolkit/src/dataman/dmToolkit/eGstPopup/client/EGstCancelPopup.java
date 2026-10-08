package dataman.dmToolkit.eGstPopup.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.gst.shared.EwayCancelRequest_Mdl;
import java.util.List;
import dataman.dmToolkit.eGstPopup.shared.EGstPopup_Srv;
import dataman.dmToolkit.eGstPopup.shared.EGstPopup_SrvAsync;

public class EGstCancelPopup extends EGstCancelPopup_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, ClickHandler {

    public interface CallBack {

        public void onOk(EwayCancelRequest_Mdl cancelField, int actionCode);

    }
    CallBack callBack;
    private EwayCancelRequest_Mdl cancelRequest_Mdl = null;
    private final ServiceInvoker<EGstPopup_SrvAsync> srvAsync;
    private int actionCode = 0;

    private void setEgst_Mdl(EwayCancelRequest_Mdl cancelRequest_Mdl) {
        this.cancelRequest_Mdl = cancelRequest_Mdl;
    }

    public EGstCancelPopup() {
        super();
        srvAsync = new ServiceInvoker<EGstPopup_SrvAsync>(GWT.create(EGstPopup_Srv.class), EGstPopup.class.getName());
        toolContainer.setTitle("Egst Popup");
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();
        textHelpEvents();
        handlerEvents();
    }

    private void clear() {
        txtHCancelReason.setText("");
        txtHCancelReason.setTag("");
        txtCancelRemark.setText("");
        txtCancelRemark.setTag("");
        actionCode = 0;
    }

    private void textHelpEvents() {

        txtHCancelReason.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {

                srvAsync.invokeRPC(blnCallBackWait).getTxtHCancelReason(actionCode, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCancelReason).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHCancelReason.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCancelReason).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });

    }

    private void handlerEvents() {
        btnCancel.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        btnOk.addClickHandler(this);
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
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
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            this.hide();
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            hide();
        } else if (event.getSource() == btnOk) {
            onOk();
        }
    }

    private void onOk() {
        if (DmShared.xNull(txtHCancelReason.getTag()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Reason is mandatory!", txtHCancelReason).show();
            return;
        }
        if (DmShared.xNull(txtCancelRemark.getText()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Remark is mandatory!", txtCancelRemark).show();
            return;
        }
        fillRecords();
        hide(true);
    }

    private void fillRecords() {
        cancelRequest_Mdl = new EwayCancelRequest_Mdl();
        cancelRequest_Mdl.setCancelRsnCode(DmShared.xNull(txtHCancelReason.getTag()));
        cancelRequest_Mdl.setCancelRmrk(DmShared.xNull(txtCancelRemark.getText()));
    }

    public void show(int actionCode) {
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        clear();
        this.actionCode = actionCode;
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtHCancelReason.setFocus(true);
            }
        });
    }

    public void hide() {
        hide(false);
    }

    public void hide(boolean isCallEvent) {
        if (isCallEvent) {
            callBack.onOk(cancelRequest_Mdl, actionCode);
        }
        this.popupPanel.hide();
    }

}
