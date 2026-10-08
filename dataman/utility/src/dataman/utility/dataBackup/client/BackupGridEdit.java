package dataman.utility.dataBackup.client;

import dataman.utility.dataBackup.shared.*;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class BackupGridEdit<T1, T2> extends BackupGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(GrdDataBackup_Mdl fileMaster_Mdl);

        public void onDelete();
    }

    private final ServiceInvoker<DataBackup_SrvAsync> srvAsync;
    private CallBack callBack;

    private void manageDisplay() {
        txtElementLocation.setReadOnly(true);
        txtElementName.setEnabled(false);
        txtElementType.setEnabled(false);
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
    }

    @Override
    public void displayRow(T1 row) {
        GrdDataBackup_Mdl ourRow = (GrdDataBackup_Mdl) row;

        txtBackupLocation.setText(DmShared.xNull(ourRow.getBackupLocation()));
        txtElementLocation.setText(DmShared.xNull(ourRow.getElementLocation()));
        txtElementName.setText(DmShared.xNull(ourRow.getElementName()));
        txtElementType.setText(DmShared.xNull(ourRow.getElementType()));
        txtElementType.setTag(DmShared.xNull(ourRow.getElementTypeCode()));
        txtFileLocation.setText(DmShared.xNull(ourRow.getBackupFileLocation()));
        manageDisplay();
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdDataBackup_Mdl();
    }

    @Override
    public void onOk() {
        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        final GrdDataBackup_Mdl ourRow = (GrdDataBackup_Mdl) row;
        ourRow.setBackupLocation(DmShared.xNull(txtBackupLocation.getText()));
        ourRow.setElementLocation(DmShared.xNull(txtElementLocation.getText()));
        ourRow.setElementName(DmShared.xNull(txtElementName.getText()));
        ourRow.setElementType(DmShared.xNull(txtElementType.getText()));
        ourRow.setElementTypeCode(DmShared.xNull(txtElementType.getTag()));
        ourRow.setBackupFileLocation(DmShared.xNull(txtFileLocation.getText()));

        srvAsync.invokeRPC().saveRecord(ourRow, new AsyncCallback<DmResult<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtBackupLocation).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    callBack.onOk(ourRow);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtBackupLocation).show();
                }
            }
        });
        callBack.onOk(ourRow);
    }

    @Override
    public void onNew() {
        proceedNew();
    }

    @Override
    public void onDelete() {
        new DmConfirmMessageBox("Are you sure? You want to delete element.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    topControlDelete();
                }
            }
        }).show();
    }

    private void topControlDelete() {
        srvAsync.invokeRPC().deleteRecord(txtElementName.getText(), txtElementType.getTag(), new AsyncCallback<DmResult<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtElementType).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    proceedDelete();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtElementType).show();
                }
            }
        });
    }

    @Override
    public void onClose() {
        hide();
    }

    @Override
    public void manageDelete() {
        callBack.onDelete();
    }

    public BackupGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Backup", "500px", "140px");
        srvAsync = new ServiceInvoker<>(GWT.create(DataBackup_Srv.class), DataBackup.class.getName());
    }

}
