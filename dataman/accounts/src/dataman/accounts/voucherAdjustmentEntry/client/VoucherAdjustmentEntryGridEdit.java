package dataman.accounts.voucherAdjustmentEntry.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import dataman.accounts.voucherAdjustmentEntry.shared.GrdLedger_Mdl;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.accountAdjustment.client.AccountAdjustment;
import dataman.dmToolkit.accountAdjustment.shared.*;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.itemAdjustment.client.ItemAdjustment;
import dataman.dmToolkit.itemAdjustment.shared.*;
import java.util.ArrayList;
import java.util.List;

public class VoucherAdjustmentEntryGridEdit<T1, T2> extends VoucherAdjustmentEntryGridEdit_Dsg<T1, T2> implements AttachEvent.Handler, DmRootEvents, KeyUpHandler {

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

    final AccountAdjustment adjustmentPopup = new AccountAdjustment();
    final List<GrdAdjustment_Mdl> lstAdjustments = new ArrayList<GrdAdjustment_Mdl>();

    final ItemAdjustment itemAdjustmentPopup = new ItemAdjustment();
    final List<GrdItemAdjustment_Mdl> lstItemAdjustments = new ArrayList<GrdItemAdjustment_Mdl>();

    public VoucherAdjustmentEntryGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Adjustment", "400px", "75px");
        manageDisplay();
        manageEvents();
        manageCallBacks();
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {
        super.showCenter(currentRowIndex, okNeeded, newNeeded, deleteNeeded);
    }

    private void manageDisplay() {
        txtAcManualCode.setEnabled(false);
        txtAcName.setEnabled(false);
        txtCity.setEnabled(false);
        txtNarration.setEnabled(false);
        txtDebit.setEnabled(false);
        txtCredit.setEnabled(false);
        txtAdjustment.setReadOnly(true);
        txtItemAdjustment.setReadOnly(true);
    }

    private void manageCallBacks() {
        adjustmentPopup.addCallBack(new AccountAdjustment.CallBack() {
            @Override
            public void onOk(List<GrdAdjustment_Mdl> lstAdjustments) {
                VoucherAdjustmentEntryGridEdit.this.lstAdjustments.clear();
                if (lstAdjustments != null) {
                    VoucherAdjustmentEntryGridEdit.this.lstAdjustments.addAll(lstAdjustments);
                }
                calculate();
                txtAdjustment.setFocus(true);
            }

            @Override
            public void onClose() {
                txtAdjustment.setFocus(true);
            }
        });

        itemAdjustmentPopup.addCallBack(new ItemAdjustment.CallBack() {
            @Override
            public void onOk(List<GrdItemAdjustment_Mdl> lstItemAdjustments) {
                VoucherAdjustmentEntryGridEdit.this.lstItemAdjustments.clear();
                if (lstItemAdjustments != null) {
                    VoucherAdjustmentEntryGridEdit.this.lstItemAdjustments.addAll(lstItemAdjustments);
                }
                calculate();
                txtItemAdjustment.setFocus(true);
            }

            @Override
            public void onClose() {
                txtItemAdjustment.setFocus(true);
            }
        });
    }

    private void showAdjustmentPopup() {
        if (!DmShared.xNull(txtAcManualCode.getTag()).trim().equals("")) {
            AccountAdjustment_Mdl vapMdl = new AccountAdjustment_Mdl();
            vapMdl.setIsEditable(true);
            vapMdl.setAcCode(txtAcManualCode.getTag());
            vapMdl.setAcName(txtAcName.getText());
            vapMdl.setAmtDr(txtDebit.getText());
            vapMdl.setAmtCr(txtCredit.getText());
            vapMdl.setLstAdjustment(lstAdjustments);
            adjustmentPopup.show(vapMdl);
        }
    }

    private void showItemAdjustmentPopup() {
        if (!DmShared.xNull(txtAcManualCode.getTag()).trim().equals("")) {
            ItemAdjustment_Mdl iapMdl = new ItemAdjustment_Mdl();
            iapMdl.setIsEditable(true);
            iapMdl.setAcCode(txtAcManualCode.getTag());
            iapMdl.setAcName(txtAcName.getText());
            iapMdl.setAmtDr(txtDebit.getText());
            iapMdl.setAmtCr(txtCredit.getText());
            iapMdl.setLstItemAdjustment(lstItemAdjustments);
            itemAdjustmentPopup.show(iapMdl);
        }
    }

    private void calculate() {
        double total = 0;

        for (GrdAdjustment_Mdl gaMdl : lstAdjustments) {
            if (gaMdl != null) {
                if (!DmShared.xNull(gaMdl.getSharedKeyFieldCode()).trim().equals("")) {
                    total += DmShared.vNull(gaMdl.getAdjustment());
                }
            }
        }
        txtAdjustment.setText(DmClient.vNull(total, "0.00"));

        total = 0;
        for (GrdItemAdjustment_Mdl giaMdl : lstItemAdjustments) {
            if (giaMdl != null) {
                if (!DmShared.xNull(giaMdl.getSharedKeyFieldCode()).trim().equals("")) {
                    total += DmShared.vNull(giaMdl.getAdjustment());
                }
            }
        }
        txtItemAdjustment.setText(DmClient.vNull(total, "0.00"));

    }

    //<editor-fold defaultstate="collapsed" desc="GridEdit Overrides">
    @Override
    public void onOk() {
        if (DmShared.xNull(txtAcManualCode.getTag()).trim().equals("")) {
            new DmAlertMessageBox("Empty record is not allowed", txtAdjustment).show();
            return;
        }
        if (DmShared.vNull(txtAdjustment.getText()) == 0 && DmShared.vNull(txtItemAdjustment.getText()) == 0) {
            new DmAlertMessageBox("Adjustment is mandatory.", txtAdjustment).show();
            return;
        }

        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdLedger_Mdl ourRow = (GrdLedger_Mdl) row;

        List<GrdAdjustment_Mdl> lstAdjustments = new ArrayList<>();
        for (GrdAdjustment_Mdl gaMdl : this.lstAdjustments) {
            if (gaMdl != null) {
                lstAdjustments.add(gaMdl.clone());
            }
        }
        ourRow.setLstAdjustment(lstAdjustments);

        List<GrdItemAdjustment_Mdl> lstItemAdjustments = new ArrayList<>();
        for (GrdItemAdjustment_Mdl gaMdl : this.lstItemAdjustments) {
            if (gaMdl != null) {
                lstItemAdjustments.add(gaMdl.clone());
            }
        }
        ourRow.setLstItemAdjustment(lstItemAdjustments);

        callBack.onOk();
    }

    @Override
    public void displayRow(T1 row) {
        GrdLedger_Mdl ourRow = (GrdLedger_Mdl) row;
        txtAcManualCode.setTag(ourRow.getAcCode());
        txtAcManualCode.setText(ourRow.getAcManualCode());
        txtAcName.setText(ourRow.getAcName());
        txtCity.setText(ourRow.getCityName());
        txtNarration.setText(ourRow.getNarration());
        txtDebit.setText(ourRow.getDebit());
        txtCredit.setText(ourRow.getCredit());

        lstAdjustments.clear();
        if (ourRow.getLstAdjustment() != null) {
            lstAdjustments.addAll(ourRow.getLstAdjustment());
        }

        lstItemAdjustments.clear();
        if (ourRow.getLstItemAdjustment() != null) {
            lstItemAdjustments.addAll(ourRow.getLstItemAdjustment());
        }

        calculate();
        txtAdjustment.setFocus(true);
    }

    @Override
    public void onNew() {
        proceedNew();
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdLedger_Mdl();
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
        txtAdjustment.addKeyUpHandler(this);
        txtItemAdjustment.addKeyUpHandler(this);
        lblAdjustment.addClickHandler(this);
        lblItemAdjustment.addClickHandler(this);
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == lblAdjustment) {
            showAdjustmentPopup();
        } else if (event.getSource() == lblItemAdjustment) {
            showItemAdjustmentPopup();
        } else {
            super.onClick(event);
        }
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_SPACE) {
            if (event.getSource() == txtAdjustment) {
                showAdjustmentPopup();
            } else if (event.getSource() == txtItemAdjustment) {
                showItemAdjustmentPopup();
            }
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            adjustmentPopup.hide();
            itemAdjustmentPopup.hide();
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
