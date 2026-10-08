package dataman.utility.importProcedure.client;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.utility.importProcedure.shared.ImportProcedure_SrvAsync;
import dataman.utility.importProcedure.shared.GrdProcedure_Mdl;
import dataman.utility.importProcedure.shared.ImportProcedure_Mdl;
import dataman.utility.importProcedure.shared.ImportProcedure_Srv;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import dataman.dmToolkit.dmMessageBox.client.*;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmBase.dmFileManager.shared.DmIFile_Mdl;
import java.util.ArrayList;

public class ImportProcedure extends ImportProcedure_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler {

    ServiceInvoker<ImportProcedure_SrvAsync> SrvAsync;
    Boolean checkAll = false;
    private ImportProcedure_Mdl importProcedure_Mdl;
    List<GrdProcedure_Mdl> grdProcedure_Mdl;
    private String serverFileName = "", ServerDirectory = "";

    private void textHelpEvents() {

    }

    private void gridEvents() {

        grdProcedure.getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                try {
                    if (event.getCellIndex() == 0) {
                        int val = DmShared.vNull(grdProcedure.getGrid().getStore().get(event.getRowIndex()).getIsChecked()).intValue();
                        if (val == 0) {
                            grdProcedure.getGrid().getStore().get(event.getRowIndex()).setIsChecked("1");
                        } else if (val == 1) {
                            grdProcedure.getGrid().getStore().get(event.getRowIndex()).setIsChecked("0");
                        }
                        grdProcedure.commitChanges();
                        for (int i = 0; i < grdProcedure.getGrid().getStore().size(); i++) {
                            GrdProcedure_Mdl grd_Mdl = grdProcedure.getGrid().getStore().get(i);
                            if (DmShared.xNull(grd_Mdl.getIsChecked()).equalsIgnoreCase("0") || DmShared.xNull(grd_Mdl.getIsChecked()).equalsIgnoreCase("")) {
                                checkAll = false;
                                chkCheckAll.setStyleName("fa fa-square-o");
                                break;
                            } else {
                                checkAll = true;
                                chkCheckAll.setStyleName("fa fa-check-square-o");
                            }

                        }

                    }

                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null);
                }
            }
        });

    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            onClose();
        } else if (event.getSource() == btnImport) {

            new DmConfirmMessageBox("Are you sure? You want to import the procedure.", new DialogHideEvent.DialogHideHandler() {

                @Override
                public void onDialogHide(DialogHideEvent event) {
                    if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                        importProcedure();
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
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(ImportProcedure.this);
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

    public ImportProcedure(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<ImportProcedure_SrvAsync>(GWT.create(ImportProcedure_Srv.class), ImportProcedure.class.getName());
        this.asWidget().setTitle(widgetName);
        toolContainer.setTitle(this.asWidget().getTitle());
        toolContainer.addWidget(flwImport);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();
        textHelpEvents();
        manageEvents();
        swi.setWidget(this);
        btnUpload.getButton().setFocus(true);
        txtFilePath.setEnabled(false);

    }

    private void manageEvents() {
        gridEvents();
        btnImport.addClickHandler(this);
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

            if (checkAll == false) {
                grdProcedure.getGrid().getStore().get(i).setIsChecked("0");
            } else if (checkAll == true) {
                grdProcedure.getGrid().getStore().get(i).setIsChecked("1");
            }

        }
        grdProcedure.commitChanges();
    }

    private void importProcedure() {
        importProcedure_Mdl = new ImportProcedure_Mdl();
        try {
            List<GrdProcedure_Mdl> lstRoprot = new ArrayList<GrdProcedure_Mdl>();
            for (GrdProcedure_Mdl grm : grdProcedure.getGrid().getStore().getAll()) {
                if (DmShared.xNull(grm.getIsChecked()).equalsIgnoreCase("1")) {
                    lstRoprot.add(grm);
                }
            }
            importProcedure_Mdl.getGrdReport_Mdl().addAll(lstRoprot);

            if (importProcedure_Mdl.getGrdReport_Mdl().size() > 0) {
                SrvAsync.invokeRPC(true).importProcedure(importProcedure_Mdl, new AsyncCallback<DmResult<String>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResult<String> result) {
                        if (result.isStatus()) {
                            new DmAlertMessageBox("Procedure Imported Successfully.", (DmTextBox) null).show();
                        } else {
                            topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                        }

                    }
                });
            } else {
                topControlSaveError(0, "Please select the procedure.");
            }
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
    public void notifyForUpload(String notifier) {
    }

    @Override
    public void processAfterUploaded(DmIFile_Mdl file) {
        serverFileName = file.getServerFileName();
        ServerDirectory = file.getServerDirectory();
        txtFilePath.setText(file.getFileName());
        fillGrid();
    }

    private void fillGrid() {
        try {
            SrvAsync.invokeRPC(false).getProcedureList(ServerDirectory, serverFileName, new AsyncCallback<DmResultPageing<List<GrdProcedure_Mdl>>>() {

                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResultPageing<List<GrdProcedure_Mdl>> result) {
                    if (result.isStatus()) {
                        grdProcedure.getGrid().getStore().clear();
                        grdProcedure.getGrid().getStore().addAll(result.getData());
                        grdProcedure.commitChanges();
                        for (int i = 0; i < grdProcedure.getGrid().getStore().size(); i++) {
                            GrdProcedure_Mdl grd_Mdl = grdProcedure.getGrid().getStore().get(i);
                            if (DmShared.xNull(grd_Mdl.getIsChecked()).equalsIgnoreCase("0") || DmShared.xNull(grd_Mdl.getIsChecked()).equalsIgnoreCase("")) {
                                checkAll = false;
                                chkCheckAll.setStyleName("fa fa-square-o");
                                break;
                            } else {
                                checkAll = true;
                                chkCheckAll.setStyleName("fa fa-check-square-o");
                            }

                        }
                    } else {
                        topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

}
