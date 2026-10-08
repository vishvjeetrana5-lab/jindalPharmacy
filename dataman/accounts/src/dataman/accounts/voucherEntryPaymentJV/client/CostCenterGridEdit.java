package dataman.accounts.voucherEntryPaymentJV.client;

import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.accounts.voucherEntryPaymentJV.shared.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public class CostCenterGridEdit<T1, T2> extends CostCenterGridEdit_Dsg<T1, T2> implements AttachEvent.Handler, DmRootEvents {

    interface CallBack {

        public void onOk(CostCenterGrid_Mdl costCenterGrid);

        public void onDelete();
    }
    ServiceInvoker<VoucherEntryPaymentJV_SrvAsync> SrvAsync;
    CallBack callBack;
    private int hashCode = 0;
    private double pendingAmt = 0.0;

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);

        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide();
        }
    }

    private void textHelpEvents() {
        txtHCostCenterGrid.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHCostCenterGrid_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHCostCenterGrid(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHCostCenterGrid_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHCostCenterGrid_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHCostCenterGrid.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHCostCenterGrid_Mdl tRow) {
                if (blnStatus) {
                    if (tRow != null) {
                    }
                }
            }

        });
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, String pendingAmt) {
        this.pendingAmt = DmShared.vNull(pendingAmt);
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);

    }

    @Override
    public void displayRow(T1 row) {
        CostCenterGrid_Mdl ourRow = (CostCenterGrid_Mdl) row;
        txtHCostCenterGrid.setText(ourRow.getName());
        txtHCostCenterGrid.setTag(ourRow.getCode());
        if (DmShared.vNull(ourRow.getAmount()).doubleValue() > 0) {
            txtAmount.setText(DmClient.vNull(ourRow.getAmount(), "0.00"));
        } else {
            txtAmount.setText(DmClient.vNull(pendingAmt, "0.00"));
        }

        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtHCostCenterGrid.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new CostCenterGrid_Mdl();
    }

    @Override
    public void onOk() {

        DmClient.isValidText(txtHCostCenterGrid.getTag(), "Cost Center", txtHCostCenterGrid);

        if (DmShared.xNull(txtHCostCenterGrid.getTag()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Cost Center is mandatory.", txtHCostCenterGrid).show();
            return;
        }

        if (DmShared.vNull(txtAmount.getText()).doubleValue() <= 0) {
            new DmAlertMessageBox("Amount is mandatory.", txtAmount).show();
            return;
        }

        for (CostCenterGrid_Mdl service : (List<CostCenterGrid_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
            if (DmShared.xNull(service.getCode()).equalsIgnoreCase(DmShared.xNull(txtHCostCenterGrid.getTag()))) {
                if (!DmClient.vNull(service.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                    new DmAlertMessageBox("Cost Center already exists.", txtHCostCenterGrid).show();
                    return;
                }
            }
        }

        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        CostCenterGrid_Mdl ourRow = (CostCenterGrid_Mdl) row;
        ourRow.setName(txtHCostCenterGrid.getText());
        ourRow.setCode(txtHCostCenterGrid.getTag());
        ourRow.setAmount(txtAmount.getText());
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

    public CostCenterGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Cost Center", "350px", "75px");
        SrvAsync = new ServiceInvoker<VoucherEntryPaymentJV_SrvAsync>(GWT.create(VoucherEntryPaymentJV_Srv.class), VoucherEntryPaymentJV.class.getName());
        textHelpEvents();
    }

}
