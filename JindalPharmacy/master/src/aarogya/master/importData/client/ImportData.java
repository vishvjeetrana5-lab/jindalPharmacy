package aarogya.master.importData.client;

import dataman.dmToolkit.dmImportData.shared.ImportData_Mdl;
import aarogya.master.importData.shared.*;
import com.google.gwt.aria.client.Roles;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkit.dmMessageBox.client.*;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.*;
import java.util.List;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.dmToolkit.dmImportData.shared.DmImportStatus;
import java.util.ArrayList;
import java.util.Collection;

public class ImportData extends ImportData_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler {

    private final ServiceInvoker<ImportData_SrvAsync> srvAsync;
    private int rowIndex = 0;
    private final ImportDataGridEdit gridEditImport = new ImportDataGridEdit();
    private final AddImportData addImportData = new AddImportData();
    private final List<ImportData_Mdl> importDataList = new ArrayList<>();

    private void gridEvents() {
        gridEditImport.addCallBack(new ImportDataGridEdit.CallBack() {

            @Override
            public void onOk(ImportData_Mdl grdImportData_Mdl) {
                grdImportData.getGrid().getStore().remove(rowIndex);
                grdImportData.getGrid().getStore().add(rowIndex, grdImportData_Mdl);
                grdImportData.commitChanges();
            }

            @Override
            public void onDelete() {
                grdImportData.getGrid().getStore().remove(rowIndex);
                grdImportData.commitChanges();
                manageListImport(importDataList);
            }
        });
        addImportData.addCallBack(new AddImportData.CallBack() {

            @Override
            public void onOk(List<ImportData_Mdl> listImportData_Mdl) {
                if (listImportData_Mdl != null) {
                    for (ImportData_Mdl importData_Mdl : listImportData_Mdl) {
                        if (importData_Mdl != null) {
                            if (DmShared.iNull(importData_Mdl.getIsChecked()).intValue() == 1) {
                                if (!grdImportData.getGrid().getStore().getAll().contains(importData_Mdl)) {
                                    grdImportData.getGrid().getStore().add(importData_Mdl);
                                    importDataList.set(importDataList.indexOf(importData_Mdl), importData_Mdl);
                                }
                            }
                        }
                    }
                }
                grdImportData.commitChanges();
            }

            @Override
            public void onClose() {
            }
        });
        grdImportData.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showImportDataGridEdit(grdImportData.getGrid().getStore().get(event.getRowIndex()));;
                rowIndex = event.getRowIndex();
            }
        });
        grdImportData.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showImportDataGridEdit(grdImportData.getGrid().getSelectionModel().getSelectedItem());
                }
            }
        }, KeyDownEvent.getType());
    }

    private void manageListImport(List<ImportData_Mdl> lstImportData_Mdl) {
        List<ImportData_Mdl> iml = grdImportData.getGrid().getStore().getAll();
        for (ImportData_Mdl importData_Mdl : lstImportData_Mdl) {
            if (importData_Mdl != null) {
                if (iml.contains(importData_Mdl)) {
                    importData_Mdl.setIsChecked("1");
                } else {
                    importData_Mdl.setIsChecked("0");
                }
            }
        }
    }

    private void showImportDataGridEdit(ImportData_Mdl grdTableMaster_Mdl) {
        gridEditImport.show(grdTableMaster_Mdl);
    }

    private void showAddImportPopup() {
        addImportData.show(importDataList);
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            onClose();
        } else if (event.getSource() == btnImport) {
            new DmConfirmMessageBox("Are you sure? You want to import data.", new DialogHideEvent.DialogHideHandler() {

                @Override
                public void onDialogHide(DialogHideEvent event) {
                    if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                        topControlSave();
                    }
                }
            }).show();
        } else if (event.getSource() == btnRemove) {
            grdImportData.getGrid().getStore().clear();
            grdImportData.commitChanges();
            if (importDataList != null) {
                for (ImportData_Mdl importData_Mdl : importDataList) {
                    if (importData_Mdl != null) {
                        importData_Mdl.setIsChecked("0");
                    }
                }
            }
        } else if (event.getSource() == btnAddImport) {
            showAddImportPopup();
        }
    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(ImportData.this);
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

    public ImportData(UserMenu menuPermission, final SetWidgetInvoker swi) {
        super();
        srvAsync = new ServiceInvoker<ImportData_SrvAsync>(GWT.create(ImportData_Srv.class), ImportData.class.getName());
        this.asWidget().setTitle(widgetName);
        manageEvents();
        srvAsync.invokeRPC(true).getImportData(DmClient.getStartDate(), new AsyncCallback<DmResult<List<ImportData_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), btnAddImport).show();
            }

            @Override
            public void onSuccess(DmResult<List<ImportData_Mdl>> result) {
                if (result.isStatus()) {
                    importDataList.clear();
                    importDataList.addAll(result.getData());
                    swi.setWidget(ImportData.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), btnAddImport).show();
                }
            }
        });
    }

    private void manageEvents() {
        gridEvents();
        btnImport.addClickHandler(this);
        btnCancel.addClickHandler(this);
        btnRemove.addClickHandler(this);
        btnAddImport.addClickHandler(this);
        flwForm.addAttachHandler(this);
    }

    private void topControlSave() {
        try {
            List<ImportData_Mdl> mrTemp = new ArrayList<ImportData_Mdl>();
            for (ImportData_Mdl grm : grdImportData.getGrid().getStore().getAll()) {
                if (!DmShared.xNull(grm.getImportFrom()).equalsIgnoreCase("")) {
                    mrTemp.add(grm);
                } else {
                    new DmAlertMessageBox("No file found to import in " + grm.getImportTo() + ".", btnAddImport).show();
                    return;
                }
            }

            if (mrTemp.size() > 0) {
                execute(0);
            } else {
                topControlSaveError(0, "No file found to import!");
            }

        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void execute(final int index) {
        ImportData_Mdl importData_Mdl = null;

        if (grdImportData.getGrid().getStore().size() > index) {
            importData_Mdl = grdImportData.getGrid().getStore().get(index);
            grdImportData.getGrid().getView().focusCell(index, 3, true);
            grdImportData.lockScroll();
        }

        if (importData_Mdl != null) {

            srvAsync.invokeRPC().importData(DmClient.getStartDate(), importData_Mdl, new AsyncCallback<DmResult<ImportData_Mdl>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<ImportData_Mdl> result) {
                    ImportData_Mdl localNode = null, resultNode = null;
                    if (result.isStatus()) {
                        resultNode = result.getData();
                        localNode = grdImportData.getGrid().getStore().get(index);
                        localNode.setImportStatus(resultNode.getImportStatus());
                        grdImportData.getGrid().getStore().update(localNode);
                        grdImportData.commitChanges();
                        grdImportData.getGrid().getView().refresh(true);
                        grdImportData.releaseScroll();
                        if (resultNode.getImportStatus() != DmImportStatus.ERROR) {
                            execute(index + 1);
                        }
                    } else {
                        topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });
        }
    }

    private void topControlSaveError(int errorNo, String errorMessage) {
        switch (errorNo) {
            default:
                new DmAlertMessageBox(errorMessage, (DmTextBox) null).show();
                break;
        }
    }

}
