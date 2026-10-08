package dataman.utility.dataBackup.client;

import dataman.utility.dataBackup.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import java.util.ArrayList;
import java.util.List;

public final class DataBackup extends DataBackup_Dsg implements AttachEvent.Handler, ClickHandler, DmRootEvents {

    private final ServiceInvoker<DataBackup_SrvAsync> SrvAsync;
    private final BackupGridEdit<DataBackup, Grd_PrpAcs> gridEdit = new BackupGridEdit(grdBackup);
    private final AddDirectory addDirectory = new AddDirectory();

    private void gridEvents() {
        gridEdit.addCallBack(new BackupGridEdit.CallBack() {
            @Override
            public void onOk(GrdDataBackup_Mdl grdFileMaster_Mdl) {
                grdBackup.commitChanges();
            }

            @Override
            public void onDelete() {
                grdBackup.commitChanges();
            }
        });
        addDirectory.addCallBack(new AddDirectory.CallBack() {
            @Override
            public void onOk(GrdDataBackup_Mdl grdMdl) {
                int index = -1;
                if (grdMdl != null) {
                    index = grdBackup.getGrid().getStore().indexOf(grdMdl);
                    if (index < 0) {
                        grdBackup.getGrid().getStore().add(grdMdl);
                    } else {
                        grdBackup.getGrid().getStore().update(grdMdl);
                    }
                    grdBackup.commitChanges();
                }
            }
        });
        grdBackup.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                if (event.getColumnIndex() == 0) {
                    manageGridCheck(event.getRowIndex());
                } else {
                    showGridEdit(event.getRowIndex());
                }
            }
        });
        grdBackup.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                int colIndex = ((CellSelectionModel) grdBackup.getGrid().getSelectionModel()).getSelectCell().getCell();
                int rowIndex = grdBackup.getGrid().getStore().indexOf(grdBackup.getGrid().getSelectionModel().getSelectedItem());
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey() && colIndex != 0) {
                    showGridEdit(rowIndex);
                } else if (event.getNativeKeyCode() == KeyCodes.KEY_SPACE) {
                    manageGridCheck(rowIndex);
                }
            }
        }, KeyDownEvent.getType());
    }

    private void manageGridCheck(int rowIndex) {
        boolean flag = grdBackup.getGrid().getStore().get(rowIndex).isChecked();
        if (flag) {
            grdBackup.getGrid().getStore().get(rowIndex).setChecked(false);
        } else if (!flag) {
            grdBackup.getGrid().getStore().get(rowIndex).setChecked(true);
        }
        grdBackup.commitChanges();
    }

    public DataBackup(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(DataBackup_Srv.class), DataBackup.class.getName());
        this.asWidget().setTitle(widgetName);
        flwForm.addAttachHandler(this);
        btnAddDirectory.addClickHandler(this);
        btnBackup.addClickHandler(this);
        btnCancel.addClickHandler(this);
        gridEvents();
        iniDefault(swi);
    }

    private void iniDefault(final SetWidgetInvoker swi) {
        SrvAsync.invokeRPC().getDefaultValue(new AsyncCallback<DmResult<List<GrdDataBackup_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdDataBackup_Mdl>> result) {
                if (result.isStatus()) {
                    grdBackup.getGrid().getStore().clear();
                    grdBackup.getGrid().getStore().addAll(result.getData());
                    swi.setWidget(DataBackup.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void showGridEdit(int currentRowIndex) {
        gridEdit.show(currentRowIndex, true, false, true);
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (!event.isAttached()) {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(DataBackup.this);
                }
            }
        }).show();
    }

    private void topControlSave() {
        List<GrdDataBackup_Mdl> lstElement = new ArrayList<GrdDataBackup_Mdl>();
        try {
            grdBackup.commitChanges();
            for (GrdDataBackup_Mdl bckupMdl : grdBackup.getGrid().getStore().getAll()) {
                if (bckupMdl != null) {
                    lstElement.add(bckupMdl);
                }
            }
            SrvAsync.invokeRPC().backupElement(lstElement, new AsyncCallback<DmResult<List<GrdDataBackup_Mdl>>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<List<GrdDataBackup_Mdl>> result) {
                    if (result.isStatus()) {
                        grdBackup.getGrid().getStore().clear();
                        grdBackup.getGrid().getStore().addAll(result.getData());
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
            case 100:
                new DmAlertMessageBox(errorMessage, (DmTextBox) null).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, (DmTextBox) null).show();
        }
    }

    @Override
    public void searchRecord(String key) {
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnAddDirectory)) {
            addDirectory.show(btnAddDirectory);
        } else if (event.getSource().equals(btnCancel)) {
            onClose();
        } else if (event.getSource().equals(btnBackup)) {
            boolean flag = false;
            for (GrdDataBackup_Mdl mdl : grdBackup.getGrid().getStore().getAll()) {
                if (mdl != null) {
                    if (mdl.isChecked()) {
                        flag = true;
                        break;
                    }
                }
            }
            if (flag) {
                new DmConfirmMessageBox("Are you sure? You want to backup selected files.", new DialogHideEvent.DialogHideHandler() {

                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                            topControlSave();
                        }
                    }
                }).show();
            } else {
                new DmAlertMessageBox("Select anyone element to backup", (DmTextBox) null).show();
            }
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            onClose();
        } else if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_S && rootEvent.getEvent().getNativeEvent().getCtrlKey()) {
            new DmConfirmMessageBox("Are you sure? You want to Backup Data.", new DialogHideEvent.DialogHideHandler() {

                @Override
                public void onDialogHide(DialogHideEvent event) {
                    if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                        topControlSave();
                    }
                }
            }).show();
        }
    }

}
