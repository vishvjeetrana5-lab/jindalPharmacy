package dataman.utility.exportProcedure.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.utility.exportProcedure.shared.*;
import java.util.HashMap;
import java.util.List;

public class ExportProcedure extends ExportProcedure_Dsg implements ClickHandler, DmRootEvents, AttachEvent.Handler {

    private ServiceInvoker<ExportProcedure_SrvAsync> srvAsync;
    private boolean checkAll = false;

    private void gridEvents() {
        grdProcedure.getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                try {
                    if (event.getCellIndex() == 0) {
                        grdProcedure.lockScroll();
                        boolean val = grdProcedure.getGrid().getStore().get(event.getRowIndex()).isChecked();
                        grdProcedure.getGrid().getStore().get(event.getRowIndex()).setChecked(!val);
                        grdProcedure.commitChanges();
                        for (GrdProcedure_Mdl model : grdProcedure.getGrid().getStore().getAll()) {
                            if (!model.isChecked()) {
                                checkAll = false;
                                chkCheckAll.setStyleName("fa fa-square-o");
                                break;
                            } else {
                                checkAll = true;
                                chkCheckAll.setStyleName("fa fa-check-square-o");
                            }
                        }
                        grdProcedure.releaseScroll();
                    }
                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            onClose();
        } else if (event.getSource() == btnExport) {
            new DmConfirmMessageBox("Are you sure? You want to export the procedure.", new DialogHideEvent.DialogHideHandler() {

                @Override
                public void onDialogHide(DialogHideEvent event) {
                    if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                        exportProcedure();
                    }
                }
            }).show();
        } else if (event.getSource() == chkCheckAll) {
            selectAll();
        }
    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(ExportProcedure.this);
                }
            }
        }).show();
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            onClose();
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
        return flwForm;
    }

    public ExportProcedure(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        srvAsync = new ServiceInvoker<ExportProcedure_SrvAsync>(GWT.create(ExportProcedure_Srv.class), ExportProcedure.class.getName());
        this.asWidget().setTitle(widgetName);
        toolContainer.setTitle(this.asWidget().getTitle());
        toolContainer.addWidget(flwExport);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();
        manageEvents();
        iniDefaultValue(swi);
    }

    private void manageEvents() {
        gridEvents();
        btnExport.addClickHandler(this);
        btnCancel.addClickHandler(this);
        flwForm.addAttachHandler(this);
        chkCheckAll.addClickHandler(this);
    }

    public void selectAll() {
        if (checkAll == false) {
            checkAll = true;
            chkCheckAll.setStyleName("fa fa-check-square-o");
        } else {
            checkAll = false;
            chkCheckAll.setStyleName("fa fa-square-o");
        }
        for (int i = 0; i < grdProcedure.getGrid().getStore().size(); i++) {
            grdProcedure.getGrid().getStore().get(i).setChecked(checkAll);
        }
        grdProcedure.commitChanges();
    }

    private void exportProcedure() {
        ExportProcedure_Mdl mrTemp = new ExportProcedure_Mdl();
        try {
            for (GrdProcedure_Mdl model : grdProcedure.getGrid().getStore().getAll()) {
                if (model.isChecked()) {
                    mrTemp.getLstProcedure().add(model);
                }
            }
            mrTemp.setSessionId(DmRequestBuilder.getSessionId());

            if (mrTemp.getLstProcedure().size() > 0) {
                srvAsync.invokeRPC(true).exportProcedure(mrTemp, new AsyncCallback<DmResult<KeyFieldExtended>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResult<KeyFieldExtended> result) {
                        if (result.isStatus()) {
                            new DmAlertMessageBox("Procedure Exported Successfully.", (DmTextBox) null).show();
                            KeyFieldExtended field = result.getData();
                            startDownloading(DmShared.xNull(field.getSharedKeyFieldName()), DmShared.xNull(field.getSharedKeyFieldCode()));
                        } else {
                            exportError(result.getErrorNumber(), result.getErrorMessage());
                        }
                    }
                });
            } else {
                exportError(0, "Please select the procedure.");
            }
        } catch (DmException e) {
            exportError(e.getErrorNo(), e.getMessage());
        }

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

    private void iniDefaultValue(final SetWidgetInvoker swi) {
        try {
            srvAsync.invokeRPC(true).getProcedureList(new AsyncCallback<DmResultPageing<List<GrdProcedure_Mdl>>>() {

                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResultPageing<List<GrdProcedure_Mdl>> result) {
                    if (result.isStatus()) {
                        grdProcedure.getGrid().getStore().clear();
                        grdProcedure.getGrid().getStore().addAll(result.getData());
                        swi.setWidget(ExportProcedure.this);
//                        for (int i = 0; i < grdProcedure.getGrid().getStore().size(); i++) {
//                            GrdProcedure_Mdl grd_Mdl = grdProcedure.getGrid().getStore().get(i);
//                            if (DmShared.xNull(grd_Mdl.getIsChecked()).equalsIgnoreCase("0") || DmShared.xNull(grd_Mdl.getIsChecked()).equalsIgnoreCase("")) {
//                                checkAll = false;
//                                chkCheckAll.setStyleName("fa fa-square-o");
//                                break;
//                            } else {
//                                checkAll = true;
//                                chkCheckAll.setStyleName("fa fa-check-square-o");
//                            }
//
//                        }
                    }
                }
            });
        } catch (DmException e) {
            exportError(e.getErrorNo(), e.getMessage());
        }
    }

    private void exportError(int errorNo, String errorMessage) {
        switch (errorNo) {
            default:
                new DmAlertMessageBox(errorMessage, (DmTextBox) null).show();
                break;
        }
    }

    @Override
    public void searchRecord(String key) {
    }

}
