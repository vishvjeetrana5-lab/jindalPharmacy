package aarogya.master.priceListMaster.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmBase.dmFileManager.shared.DmIFile_Mdl;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import java.util.HashMap;
import aarogya.master.priceListMaster.shared.PriceListMaster_Srv;
import aarogya.master.priceListMaster.shared.PriceListMaster_SrvAsync;

public class ImportPopup extends ImportPopup_Dsg implements IsWidget, DmRootEvents, ClickHandler, BlurHandler, AttachEvent.Handler {

    interface CallBack {

        public void getNewPriceList(String newApplicableDate);
    }

    private String rateListCode = "";
    private CallBack callBack;
    private final ServiceInvoker<PriceListMaster_SrvAsync> SrvAsync;
    private int errrorNo;
    private String path = "", fileName = "";

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void chooseFile(String ServerDirectory, String serverFileName) {
        SrvAsync.invokeRPC().getListFromCSV(rateListCode, txtApplicableDate.getText(), ServerDirectory, serverFileName, new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    path = DmShared.xNull(result.getValue1());
                    fileName = DmShared.xNull(result.getValue2());
                    errrorNo = result.getErrorNumber();
                    downloadImport(path, fileName);

                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
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
    public void notifyForUpload(String notifier) {
    }

    @Override
    public void processAfterUploaded(DmIFile_Mdl file) {
        lblFileName.setText(file.getFileName());
        chooseFile(file.getServerDirectory(), file.getServerFileName());
    }

    private void startDownloading(String filePath, String fileName) {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("u", DmRequestBuilder.getSessionId());
            params.put("f", fileName);
            params.put("p", filePath);
            Window.open(DmClient.concatURL("dataman/dmToolkit/dmFileUploadButton/servlets/DmFileDownloader_Srvlt", params), "", "");
        } catch (Exception ex) {
        }
    }

    public void show(String rateListCode, String applicableDate) {
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        this.rateListCode = rateListCode;
//        this.applicableDate = applicableDate;
        lblFileName.setText("");
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtApplicableDate.setFocus(true);
            }
        });
    }

    public void hide() {
        clear();
        this.popupPanel.hide();
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtApplicableDate)) {
            if (!DmShared.xNull(txtApplicableDate.getText()).equalsIgnoreCase("")) {
                txtApplicableDate.setText(DmClient.returnDate(txtApplicableDate.getText()));
            }
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnClose)) {
            hide();
        } else if (event.getSource().equals(btnOk)) {
            if (DmShared.dNull(txtApplicableDate.getText()).trim().equalsIgnoreCase("")) {
                new DmAlertMessageBox("Applicable Date is mandatory.", txtApplicableDate).show();
                return;
            } else {
                downloadImport(path, fileName);
                hide();
            }
        }
    }

    private void clear() {
        lblFileName.setText("");
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

    public ImportPopup() {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(PriceListMaster_Srv.class), PriceListMaster.class.getName());
        toolContainer.setTitle("Import");
        btnClose.addClickHandler(this);
        btnOk.addClickHandler(this);
        txtApplicableDate.addBlurHandler(this);
        popupPanel.addAttachHandler(this);
    }

    private void downloadImport(final String path, final String filaName) {
        if (DmShared.dNull(txtApplicableDate.getText()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Applicable Date is mandatory.", txtApplicableDate).show();
            return;
        } else {
            if (errrorNo > 0) {
                new DmConfirmMessageBox("Are you sure download log file?  Imported with " + DmShared.iNull(errrorNo).intValue() + " errors.", new DialogHideEvent.DialogHideHandler() {

                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                            startDownloading(path, filaName);
                        }
                    }
                }).show();
            } else {
                new DmAlertMessageBox("Successful imported.", (DmTextBox) null).show();
            }
            callBack.getNewPriceList(DmShared.dNull(txtApplicableDate.getText()));
        }
    }
}
