package dataman.accounts.bankLetterCreation.client;

import dataman.accounts.bankLetterCreation.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.List;

public class BankLetterCreationGridEdit<T1, T2> extends BankLetterCreationGridEdit_Dsg<T1, T2> implements BlurHandler {

    private final ServiceInvoker<BankLetterCreation_SrvAsync> srvAsync;
    BankLetterCreation billhandle;
    private int hashCode;
    String selectedItem = "";

    public BankLetterCreationGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Bank Letter Creation", "570px", "155px");
        textHelpEvents();
        manageDisplay();
        srvAsync = new ServiceInvoker<>(GWT.create(BankLetterCreation_Srv.class), BankLetterCreation.class.getName());
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, BankLetterCreation invc) {
        this.billhandle = invc;

        super.showRelativeTo(currentRowIndex, 2, okNeeded, newNeeded, deleteNeeded);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtRemark.setFocus(true);
            }
        });
    }

    @Override
    public void onOk() {
        for (GrdBankLetterCreation_Mdl detail_Mdl : (List<GrdBankLetterCreation_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
            if (DmShared.xNull(detail_Mdl.getSharedKeyFieldName()).equalsIgnoreCase(DmShared.xNull(txtSupplier.getText()))) {
                if (!DmClient.vNull(detail_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                    new DmAlertMessageBox(DmShared.xNull(detail_Mdl.getSharedKeyFieldName()) + " already exists.", txtSupplier).show();
                    return;
                }
            }

        }
        proceedOk();
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
    public void manageOk(T1 row) {
        GrdBankLetterCreation_Mdl ourRow = (GrdBankLetterCreation_Mdl) row;


        ourRow.setRemark(DmShared.xNull(txtRemark.getText()));

        callBack.onOk(ourRow);
    }

    @Override
    public void manageDelete() {
        callBack.onDelete();
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdBankLetterCreation_Mdl();
    }

    @Override
    public void displayRow(T1 row) {
        GrdBankLetterCreation_Mdl ourRow = (GrdBankLetterCreation_Mdl) row;

        txtInsNo.setText(ourRow.getChqNo());
        txtPayNo.setText(ourRow.getSharedKeyFieldName());
        txtDate.setText(ourRow.getPayDate());
        txtModule.setText(ourRow.getModule());
        txtSupplier.setText(ourRow.getSupplier());
        txtFinalAmt.setText(ourRow.getOrgAmt());
//        txtTDSamt.setText(DmClient.vNull(ourRow.getAmtPaid(), "0.00"));
        txtPaidAmt.setText(DmClient.vNull(ourRow.getAmtPaid(), "0.00"));
        txtPayMode.setText(ourRow.getPayMode());
        txtAccountName.setText(ourRow.getBank());

        txtRemark.setText(ourRow.getRemark());

        hashCode = ourRow.getSharedKeyFieldHashCode();
    }

    private void textHelpEvents() {

    }

    @Override
    public void onBlur(BlurEvent event) {
//        if (event.getSource() == txtQty) {
//
//        } else if (event.getSource() == txtRemark) {
//
//        }
    }

    private void manageDisplay() {
        txtInsNo.setEnabled(false);
        txtPayNo.setEnabled(false);
        txtSupplier.setEnabled(false);
        txtModule.setEnabled(false);
        txtPaidAmt.setEnabled(false);
        txtPayMode.setEnabled(false);
        txtFinalAmt.setEnabled(false);
//        txtTDSamt.setEnabled(false);
        txtDate.setEnabled(false);

    }

    interface CallBack {

        public void onOk(GrdBankLetterCreation_Mdl opdService);

        public void onDelete();
    }
    private CallBack callBack;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
}
