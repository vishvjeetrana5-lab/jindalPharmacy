package dataman.accounts.billHandle.client;

import dataman.accounts.billHandle.shared.BillHandle_Srv;
import dataman.accounts.billHandle.shared.BillHandle_SrvAsync;
import dataman.accounts.billHandle.shared.GrdBillDetail_Mdl;
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

public class BillHandleGridEdit<T1, T2> extends BillHandleGridEdit_Dsg<T1, T2> implements BlurHandler {

    private final ServiceInvoker<BillHandle_SrvAsync> srvAsync;
    BillHandle billhandle;
    private int hashCode;
    String selectedItem = "";

    public BillHandleGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Bill Handle Detail", "570px", "180px");
        textHelpEvents();
        manageDisplay();
        srvAsync = new ServiceInvoker<>(GWT.create(BillHandle_Srv.class), BillHandle.class.getName());
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, BillHandle invc) {
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
         for (GrdBillDetail_Mdl detail_Mdl : (List<GrdBillDetail_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
            if (DmShared.xNull(detail_Mdl.getSharedKeyFieldName()).equalsIgnoreCase(DmShared.xNull(txtBillDate.getText()))) {
                if (!DmClient.vNull(detail_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                    new DmAlertMessageBox(DmShared.xNull(detail_Mdl.getSharedKeyFieldName()) + " already exists.", txtBillDate).show();
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
        GrdBillDetail_Mdl ourRow = (GrdBillDetail_Mdl) row;

        ourRow.setType(DmShared.xNull(txtType.getText()));
        ourRow.setVoucherNo(DmShared.xNull(txtNo.getText()));
        ourRow.setDate(DmShared.xNull(txtDate.getText()));
        ourRow.setPartyBillNo(DmShared.xNull(txtBillNo.getText()));
        ourRow.setPartyDate(DmShared.xNull(txtBillDate.getText()));
        ourRow.setPartyName(DmShared.xNull(txtPartyName.getText()));
//        ourRow.setNetAmt(DmShared.xNull(txtNetAmt.getText()));
        ourRow.setNetAmt(DmClient.vNull(DmShared.vNull(txtNetAmt.getText()).doubleValue(), "0.00"));
        ourRow.setPaymentDueDt(DmShared.xNull(txtPaymentDueDate.getText()));
        ourRow.setLastStatusDt(DmShared.xNull(txtLastStatusDate.getText()));
        ourRow.setRemark(DmShared.xNull(txtRemark.getText()));

        callBack.onOk(ourRow);
    }

    @Override
    public void manageDelete() {
        callBack.onDelete();
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdBillDetail_Mdl();
    }

    @Override
    public void displayRow(T1 row) {
        GrdBillDetail_Mdl ourRow = (GrdBillDetail_Mdl) row;

        txtType.setText(ourRow.getType());
        txtNo.setText(ourRow.getVoucherNo());
        txtDate.setText(ourRow.getDate());
        txtBillNo.setText(ourRow.getPartyBillNo());
        txtBillDate.setText(ourRow.getPartyDate());
        txtPartyName.setText(ourRow.getPartyName());
        txtNetAmt.setText(DmClient.vNull(ourRow.getNetAmt(), "0.00"));
//        txtNetAmt.setText(ourRow.getNetAmt());
        txtPaymentDueDate.setText(ourRow.getPaymentDueDt());
        txtLastStatusDate.setText(ourRow.getLastStatusDt());
        txtRemark.setText(ourRow.getRemark());

//        txtNetAmt.setText(DmClient.vNull(ourRow.getNetAmt(), "0.00"));
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
        txtType.setEnabled(false);
        txtNo.setEnabled(false);
        txtBillDate.setEnabled(false);
        txtBillNo.setEnabled(false);
        txtLastStatusDate.setEnabled(false);
        txtNetAmt.setEnabled(false);
        txtPartyName.setEnabled(false);
        txtPaymentDueDate.setEnabled(false);
        txtDate.setEnabled(false);

    }

    interface CallBack {

        public void onOk(GrdBillDetail_Mdl opdService);

        public void onDelete();
    }
    private CallBack callBack;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
}
