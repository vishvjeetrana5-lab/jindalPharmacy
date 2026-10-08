package dataman.webJobs.userEmailConfiguration.client;

import dataman.webJobs.userEmailConfiguration.shared.UserEmailConfiguration_SrvAsync;
import dataman.webJobs.userEmailConfiguration.shared.UserEmailConfiguration_Srv;
import com.google.gwt.core.client.GWT;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;

public class UserEmailPopupGridEdit<T1, T2> extends UserEmailPopupGridEdit_Dsg<T1, T2> {

    ServiceInvoker<UserEmailConfiguration_SrvAsync> SrvAsync;
    int hashCode = 0;
    CallBack callBack;

    public UserEmailPopupGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Email", "300px", "40px");
        SrvAsync = new ServiceInvoker<UserEmailConfiguration_SrvAsync>(GWT.create(UserEmailConfiguration_Srv.class), UserEmailConfiguration.class.getName());
        textHelpEvents();
    }

    interface CallBack {

        public void onOk(KeyFieldExtended keyField);

        public void onDelete();
    }

    private void textHelpEvents() {

    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
    }

    @Override
    public void displayRow(T1 row) {

        KeyFieldExtended ourRow = (KeyFieldExtended) row;
        txtEmail.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtEmail.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtEmail.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new KeyFieldExtended();
    }

    @Override
    public void onOk() {
        try {
            if (DmShared.xNull(txtEmail.getText()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Email is mandatory.", txtEmail).show();
                return;
            }
            if (!DmShared.xNull(txtEmail.getText()).equalsIgnoreCase("")) {
                DmShared.isValidEMail(txtEmail.getText(), 100);
            }
            proceedOk();
        } catch (DmException e) {
            saveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void saveError(int errorNo, String errorMessage) {
        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtEmail).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtEmail).show();
                break;
        }
    }

    @Override
    public void manageOk(T1 row) {
        KeyFieldExtended ourRow = (KeyFieldExtended) row;
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtEmail.getText()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtEmail.getText()));

        callBack.onOk(ourRow);
    }

    @Override
    public void onNew() {
        proceedNew();
    }

    @Override
    public void onDelete() {
        proceedDelete();
    }

    @Override
    public void onClose() {
        hide();
    }

    @Override
    public void manageDelete() {
        callBack.onDelete();
    }

}
