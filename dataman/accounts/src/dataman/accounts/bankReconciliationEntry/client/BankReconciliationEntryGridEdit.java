package dataman.accounts.bankReconciliationEntry.client;

import dataman.accounts.bankReconciliationEntry.shared.*;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class BankReconciliationEntryGridEdit<T1, T2> extends BankReconciliationEntryGridEdit_Dsg<T1, T2> implements BlurHandler {

    interface CallBack {

        public void onOk(GrdBankReconciliationEntry_Mdl bankReconciliationEntry_Mdl);

        public void onDelete();
    }

    ServiceInvoker<BankReconciliationEntry_SrvAsync> SrvAsync;
    CallBack callBack;
    GrdBankReconciliationEntry_Mdl parentRow;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {
        super.showRelativeTo(currentRowIndex, 1, okNeeded, false, false);
    }

    @Override
    public void displayRow(T1 row) {
        GrdBankReconciliationEntry_Mdl ourRow = (GrdBankReconciliationEntry_Mdl) row;
        parentRow = new GrdBankReconciliationEntry_Mdl();

        txtVoucherName.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtDate.setText(DmShared.xNull(ourRow.getVDate()));
        txtFavourOf.setText(DmShared.xNull(ourRow.getFavourOf()));
        txtACName.setText(DmShared.xNull(ourRow.getAccountName()));
        txtChequeNo.setText(DmShared.xNull(ourRow.getChq()));
        txtChequeDate.setText(DmShared.xNull(ourRow.getChqDate()));
        txtClearDate.setText(DmShared.xNull(ourRow.getClearDate()));
        txtCredit.setText(DmShared.xNull(ourRow.getCreditTag()));
        txtDebit.setText(DmShared.xNull(ourRow.getDebitTag()));

        txtClearDate.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdBankReconciliationEntry_Mdl();
    }

    @Override
    public void onOk() {

        if (txtClearDate.getText().trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Clear Date is mandatory", txtClearDate).show();
            return;
        }
        if (!txtChequeDate.getText().trim().equalsIgnoreCase("") && !txtClearDate.getText().trim().equalsIgnoreCase("")) {
            if ((DmShared.convertDate(txtClearDate.getText()).getTime() < (DmShared.convertDate(txtChequeDate.getText())).getTime())) {
                new DmAlertMessageBox("Clear Date should be greater than or equal to Cheque Date.", txtClearDate).show();
                return;
            }
        }
        if (!txtDate.getText().trim().equalsIgnoreCase("") && !txtClearDate.getText().trim().equalsIgnoreCase("")) {
            if ((DmShared.convertDate(txtClearDate.getText()).getTime() < (DmShared.convertDate(txtDate.getText())).getTime())) {
                new DmAlertMessageBox("Clear Date should be greater than or equal to Date.", txtClearDate).show();
                return;
            }
        }

        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdBankReconciliationEntry_Mdl ourRow = (GrdBankReconciliationEntry_Mdl) row;

        ourRow.setClearDate(DmShared.xNull(txtClearDate.getText()));
        ourRow.setIsCleared("1");

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

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtClearDate) {
            txtClearDate.setText(DmClient.returnDate(txtClearDate.getText()));
        }
        if ((DmShared.convertDate(DmClient.getLoginDate()).getTime() < (DmShared.convertDate(txtClearDate.getText())).getTime())) {
            new DmAlertMessageBox("Clear Date should be less than or equal to Current Date.", txtClearDate).show();
            return;
        }
    }

    public BankReconciliationEntryGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Bank Reconciliation Entry", "500px", "140px");
        SrvAsync = new ServiceInvoker<>(GWT.create(BankReconciliationEntry_Srv.class), BankReconciliationEntry.class.getName());
        txtClearDate.addBlurHandler(this);
    }
}
