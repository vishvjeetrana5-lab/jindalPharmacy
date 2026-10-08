package aarogya.master.prescriptionGuiMapping.client;

import aarogya.master.prescriptionGuiMapping.shared.*;
import aarogya.master.prescriptionGuiMapping.shared.PrescriptionGuiMapping_Srv;
import aarogya.master.prescriptionGuiMapping.shared.PrescriptionGuiMapping_SrvAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import java.util.HashMap;

public class ExportPopup extends ExportPopup_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, ClickHandler, BlurHandler {

    interface CallBack {

        public void onOk(PrescriptionGuiMapping_Mdl mrTemp);
    }
    private CallBack callBack;
    private final ServiceInvoker<PrescriptionGuiMapping_SrvAsync> srvAsync;
    PrescriptionGuiMapping_Mdl Export_Mdl = new PrescriptionGuiMapping_Mdl();
    String natureCode = "";

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show() {
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        onAdd();
    }

    public void hide() {
        this.popupPanel.hide();
    }

    @Override
    public void onBlur(BlurEvent event) {

    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);

        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    private void fillNatureCode() {
        natureCode = "";
        if (chkPreAnesthesia.getValue()) {
            natureCode += "0" + ",";
        }
        if (chkPreOperation.getValue()) {
            natureCode += "1" + ",";
        }
        if (chkOperationStart.getValue()) {
            natureCode += "2 " + ",";
        }
        if (chkAnesthesia.getValue()) {
            natureCode += "3 " + ",";
        }
        if (chkOperation.getValue()) {
            natureCode += "4 " + ",";
        }
        if (chkPostOperation.getValue()) {
            natureCode += "5 " + ",";
        }
        if (chkPostAnesthesia.getValue()) {
            natureCode += "6 " + ",";
        }
        if (chkRoutine.getValue()) {
            natureCode += "7 " + ",";
        }
        if (chkSterilization.getValue()) {
            natureCode += "8 " + ",";
        }
        if (natureCode.contains(",")) {
            this.natureCode = natureCode.substring(0, natureCode.length() - 1);
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        PrescriptionGuiMapping_Mdl mrTemp = new PrescriptionGuiMapping_Mdl();
        if (event.getSource() == btnCancel) {
            popupPanel.hide();
        } else if (event.getSource() == btnOk) {
            confirfMessage();
            hide();
            callBack.onOk(mrTemp);

        }
    }

    private void confirfMessage() {
        new DmConfirmMessageBox("Are you sure? You want to export this record.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                    fillNatureCode();
                    exportJson(natureCode);
                }
            }
        }).show();
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            this.hide();
        }
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    public ExportPopup() {
        super();
        srvAsync = new ServiceInvoker<PrescriptionGuiMapping_SrvAsync>(GWT.create(PrescriptionGuiMapping_Srv.class), PrescriptionGuiMapping.class.getName());
        handlerEvents();
    }

    private void handlerEvents() {
        popupPanel.addAttachHandler(this);
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
    }

    private void onAdd() {
        chkPreAnesthesia.setValue(true);
        chkAnesthesia.setValue(true);
        chkPreOperation.setValue(true);
        chkOperationStart.setValue(true);
        chkOperationStart.setValue(true);
        chkOperation.setValue(true);
        chkPostOperation.setValue(true);
        chkPostAnesthesia.setValue(true);
        chkRoutine.setValue(true);
        chkSterilization.setValue(true);
    }

    private void exportJson(String natureCode) {
        srvAsync.invokeRPC().exportJson(natureCode, DmRequestBuilder.getSessionId(), new AsyncCallback<DmResult<KeyFieldExtended>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<KeyFieldExtended> result) {
                if (result.isStatus()) {
                    KeyFieldExtended field = result.getData();
                    startDownloading(DmShared.xNull(field.getSharedKeyFieldName()), DmShared.xNull(field.getSharedKeyFieldCode()));
                }
            }
        });
    }

    private void startDownloading(String fileName, String filePath) {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("u", DmRequestBuilder.getSessionId());
            params.put("f", fileName);
            params.put("p", filePath);
            Window.open(DmClient.concatURL("dataman/dmToolkit/dmFileUploadButton/servlets/DmFileDownloader_Srvlt", params), "", "");
        } catch (Exception ex) {
        }
    }

}
