package aarogya.master.importData.client;

import dataman.dmToolkit.dmImportData.shared.ImportData_Mdl;
import aarogya.master.importData.shared.*;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
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
import dataman.dmToolkit.dmMessageBox.client.*;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import java.util.HashMap;

public class ImportDataGridEdit extends ImportDataGridEdit_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler {

    interface CallBack {

        public void onOk(ImportData_Mdl opdService);

        public void onDelete();
    }

    private final ServiceInvoker<ImportData_SrvAsync> SrvAsync;
    private CallBack callBack;
    private ImportData_Mdl parentRow;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(ImportData_Mdl importData) {
        this.parentRow = importData;
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        fillData(importData);

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                btnUpload.getButton().setFocus(true);
            }
        });

    }

    public void fillData(ImportData_Mdl ourRow) {
        txtImportTo.setText(DmShared.xNull(ourRow.getImportTo()));
        txtImportFrom.setText(DmShared.xNull(ourRow.getImportFrom()));
        txtFileFormat.setText(DmShared.xNull(ourRow.getFileFormat()));
        labelLogFile.setText("Logs:");

        txtImportTo.setEnabled(false);
        txtImportFrom.setEnabled(false);
        txtLog.setEnabled(false);
        txtFileFormat.setEnabled(false);
        readLogFile();
    }

    private void manageEvent() {
        btnClose.addClickHandler(this);
        btnOk.addClickHandler(this);
        btnDiscard.addClickHandler(this);
        btnDownload.addClickHandler(this);
        popupPanel.addAttachHandler(this);

    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnClose) {
            callBack.onOk(parentRow);
            popupPanel.hide();
        } else if (event.getSource() == btnOk) {
            try {
                callBack.onOk(parentRow);
                popupPanel.hide();
            } catch (Exception e) {
                new DmAlertMessageBox(e.getMessage(), btnOk).show();
            }
        } else if (event.getSource() == btnDiscard) {
            new DmConfirmMessageBox("Are you sure? You want to dicard.", new DialogHideEvent.DialogHideHandler() {

                @Override
                public void onDialogHide(DialogHideEvent event) {
                    if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                        callBack.onDelete();
                        popupPanel.hide();
                    }

                }
            }).show();
        } else if (event.getSource() == btnDownload) {
            if (!DmShared.xNull(txtFileFormat.getText()).equalsIgnoreCase("")) {
                getCSVFile();
            } else {
                new DmAlertMessageBox("Please give the format to download file.", btnDownload).show();
                return;
            }

        }
    }

    private void getCSVFile() {
        try {
            SrvAsync.invokeRPC().getCSVFile(parentRow, DmShared.xNull(txtImportTo.getText()).trim(), new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        startDownloading(result.getData(), result.getValue1());
                    } else {
                        topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void readLogFile() {
        try {
            SrvAsync.invokeRPC().readLogFile(parentRow, new AsyncCallback<DmResult<KeyFieldExtended>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<KeyFieldExtended> result) {
                    if (result.isStatus()) {
                        txtLog.setText(result.getData().getSharedKeyFieldName());
                    } else {
                        topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void topControlSaveError(int errorNo, String errorMessage) {
        switch (errorNo) {
            default:
                new DmAlertMessageBox(errorMessage, (DmTextBox) null).show();
                break;
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            callBack.onOk(parentRow);
            this.popupPanel.hide();
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            popupPanel.hide();
        }
    }

    @Override
    public void notifyForUpload(String notifier) {

    }

    @Override
    public void processAfterUploaded(DmIFile_Mdl file) {
        if (file != null) {
            parentRow.setDataFilePath(file.getServerDirectory() + "/" + file.getServerFileName());
            parentRow.setImportFrom(file.getFileName());
            txtImportFrom.setText(file.getFileName());
        }
    }

    private void startDownloading(String downloadFileName, String downloadDirectoryPath) {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("u", DmRequestBuilder.getSessionId());
            params.put("f", downloadFileName);
            params.put("p", downloadDirectoryPath);
            Window.open(DmClient.concatURL("dataman/dmToolkit/dmFileUploadButton/servlets/DmFileDownloader_Srvlt", params), "", "");
        } catch (Exception ex) {
        }
    }

    public ImportDataGridEdit() {
        super();
        toolContainer.setTitle(widgetName);
        SrvAsync = new ServiceInvoker<ImportData_SrvAsync>(GWT.create(ImportData_Srv.class), ImportData.class.getName());
        manageEvent();
        popupPanel.addAttachHandler(this);
    }

}
