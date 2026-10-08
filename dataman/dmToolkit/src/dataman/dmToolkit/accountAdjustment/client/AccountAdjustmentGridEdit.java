package dataman.dmToolkit.accountAdjustment.client;

import dataman.dmBase.self.shared.*;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.accountAdjustment.shared.*;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class AccountAdjustmentGridEdit<T1, T2> extends AccountAdjustmentGridEdit_Dsg<T1, T2> implements AttachEvent.Handler, DmRootEvents {

    //<editor-fold defaultstate="collapsed" desc="Callback">
    public interface CallBack {

        public void onOk();

        public void onDelete();
    }

    private CallBack callBack;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
    //</editor-fold>

    private ServiceInvoker<AccountAdjustment_SrvAsync> srvAsync;
    private int hashCode = 0;

    public AccountAdjustmentGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Adjustment", "400px", "75px");
    }

    public void show(int currentRowIndex, AccountAdjustment parent, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {
        srvAsync = parent.srvAsync;
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
    }

    private void manageDisplay() {
        txtVoucherNo.setEnabled(false);
        txtVoucherDate.setEnabled(false);
        txtVoucherType.setEnabled(false);
        txtRefNo.setEnabled(false);
        txtAcName.setEnabled(false);
        txtCity.setEnabled(false);
        txtNarration.setEnabled(false);
        txtAmount.setEnabled(false);
        txtAdjusted.setEnabled(false);
        txtAmountDrCr.setEnabled(false);
        txtAdjustedDrCr.setEnabled(false);
        txtAdjustmentDrCr.setEnabled(false);
    }

    //<editor-fold defaultstate="collapsed" desc="GridEdit Overrides">
    @Override
    public void onOk() {
        if (DmShared.xNull(txtVoucherNo.getText()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Empty record is not allowed", txtVoucherNo).show();
            return;
        }
        if (DmShared.vNull(txtAdjustment.getText()) <= 0) {
            new DmAlertMessageBox("Adjustment is mandatory.", txtAdjustment).show();
            return;
        }

        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdAdjustment_Mdl ourRow = (GrdAdjustment_Mdl) row;
        ourRow.setAdjustment(DmClient.vNull(txtAdjustment.getText(), "0.00"));
        callBack.onOk();
    }

    @Override
    public void displayRow(T1 row) {
        GrdAdjustment_Mdl ourRow = (GrdAdjustment_Mdl) row;

        txtVoucherNo.setText(ourRow.getSharedKeyFieldName());
        txtVoucherDate.setText(ourRow.getV_Date());
        txtVoucherType.setText(ourRow.getV_TypeName());
        txtRefNo.setText(ourRow.getRefNo());
        txtAcName.setText(ourRow.getAcName());
        txtCity.setText(ourRow.getCityName());
        txtNarration.setText(ourRow.getNarration());
        txtAmount.setText(ourRow.getAmount());
        txtAdjusted.setText(ourRow.getAdjusted());
        txtAdjustment.setText(ourRow.getAdjustment());
        txtAmountDrCr.setText(ourRow.getAmountDrCr());
        txtAdjustedDrCr.setText(ourRow.getAdjustedDrCr());
        txtAdjustmentDrCr.setText(ourRow.getAdjustmentDrCr());

        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtAdjustment.setFocus(true);
        manageDisplay();

    }

    @Override
    public void onNew() {
        proceedNew();
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdAdjustment_Mdl();
    }

    @Override
    public void onDelete() {
        proceedDelete();
    }

    @Override
    public void manageDelete() {
        callBack.onDelete();
    }

    @Override
    public void onClose() {
        hide();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Event">
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
    //</editor-fold>

}
