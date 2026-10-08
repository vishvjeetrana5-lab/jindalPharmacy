package dataman.dmToolkit.itemAdjustment.client;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.itemAdjustment.shared.*;

public class ItemAdjustmentGridEdit<T1, T2> extends ItemAdjustmentGridEdit_Dsg<T1, T2> implements AttachEvent.Handler, DmRootEvents, BlurHandler {

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

    private boolean isPartyCredit = false;

    public ItemAdjustmentGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Item Adjustment", "400px", "75px");
        manageDisplay();
        manageEvents();
    }

    public void show(int currentRowIndex, ItemAdjustment parent, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {
        this.isPartyCredit = parent.isPartyCredit;
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
    }

    private void manageDisplay() {
        txtVoucherNo.setEnabled(false);
        txtVoucherDate.setEnabled(false);
        txtVoucherType.setEnabled(false);
        txtRefNo.setEnabled(false);
        txtItemName.setEnabled(false);
        txtQty.setEnabled(false);
        txtSku.setEnabled(false);
        txtLandedValue.setEnabled(false);
    }

    //<editor-fold defaultstate="collapsed" desc="GridEdit Overrides">
    @Override
    public void onOk() {
        if (DmShared.xNull(txtVoucherNo.getText()).trim().equals("")) {
            new DmAlertMessageBox("Empty record is not allowed", txtVoucherNo).show();
            return;
        }
        if (DmShared.vNull(txtAdjustment.getText()) == 0) {
            new DmAlertMessageBox("Adjustment is mandatory.", txtAdjustment).show();
            return;
        }

        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdItemAdjustment_Mdl ourRow = (GrdItemAdjustment_Mdl) row;
        ourRow.setAdjustment(DmClient.vNull(txtAdjustment.getText(), "0.00"));
        callBack.onOk();
    }

    @Override
    public void displayRow(T1 row) {
        GrdItemAdjustment_Mdl ourRow = (GrdItemAdjustment_Mdl) row;

        txtVoucherNo.setText(ourRow.getSharedKeyFieldName());
        txtVoucherDate.setText(ourRow.getV_Date());
        txtRefNo.setText(ourRow.getRefNo());
        txtVoucherType.setText(ourRow.getV_TypeName());
        txtItemName.setText(ourRow.getItemName());
        txtQty.setText(ourRow.getSkuQty());
        txtLandedValue.setText(ourRow.getLandedSkuValue());
        txtAdjustment.setText(ourRow.getAdjustment());
        txtAdjustment.setFocus(true);

    }

    @Override
    public void onNew() {
        proceedNew();
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdItemAdjustment_Mdl();
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
    private void manageEvents() {
        txtAdjustment.addBlurHandler(this);
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtAdjustment) {
            if (isPartyCredit) {
                txtAdjustment.setText(DmClient.vNull(0 - Math.abs(DmShared.vNull(txtAdjustment.getText())), "0.00"));
            } else {
                txtAdjustment.setText(DmClient.vNull(Math.abs(DmShared.vNull(txtAdjustment.getText())), "0.00"));
            }
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
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide();
        }
    }
    //</editor-fold>

}
