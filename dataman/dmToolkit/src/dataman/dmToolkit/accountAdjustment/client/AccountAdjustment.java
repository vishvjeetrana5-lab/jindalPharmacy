package dataman.dmToolkit.accountAdjustment.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.accountAdjustment.shared.*;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.ArrayList;
import java.util.List;

public class AccountAdjustment extends AccountAdjustment_Dsg implements IsWidget, ClickHandler, AttachEvent.Handler, DmRootEvents {

    //<editor-fold defaultstate="collapsed" desc="Callback">
    public interface CallBack {

        public void onOk(List<GrdAdjustment_Mdl> lstAdjustments);

        public void onClose();
    }

    private CallBack callBack;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
    //</editor-fold>

    protected boolean isEditable = false, isPartyCredit = false;
    protected ServiceInvoker<AccountAdjustment_SrvAsync> srvAsync;
    protected SearchPopup searchPopup = new SearchPopup();
    protected HistoryPopup historyPopup = new HistoryPopup();
    protected AccountAdjustmentGridEdit<GrdAdjustment_Mdl, GrdAdjustment_PrpAcs> voucherAdjustmentGridEdit = new AccountAdjustmentGridEdit(grdAdjustment);

    public AccountAdjustment() {
        super();
        srvAsync = new ServiceInvoker<AccountAdjustment_SrvAsync>(GWT.create(AccountAdjustment_Srv.class), AccountAdjustment.class.getName());
        manageCallback();
        manageDisplay();
        manageEvents();
    }

    public void show(AccountAdjustment_Mdl vapMdl) {
        if (!DmShared.xNull(vapMdl.getAcCode()).trim().equalsIgnoreCase("")) {
            clear();
            this.isEditable = vapMdl.getIsEditable();
            this.lblAcName.setText(vapMdl.getAcName());
            this.lblAcName.getElement().setId(vapMdl.getAcCode());

            double amount = DmShared.vNull(vapMdl.getAmtDr()) - DmShared.vNull(vapMdl.getAmtCr());
            isPartyCredit = amount < 0;
            txtWorkingAmount.setText(DmClient.vNull(Math.abs(amount), "0.00"));
            grdAdjustment.getGrid().getStore().addAll(vapMdl.getLstAdjustment());
            calculate();

            flwOk.clear();
            flwFill.clear();
            flwAutoAdjust.clear();
            if (isEditable) {
                flwOk.add(btnOk);
                flwFill.add(btnFill);
                flwAutoAdjust.add(btnAutoAdjust);
            }

            popupPanel.show();
            popupPanel.center();
            popupPanel.setGlassEnabled(true);

            if (vapMdl.getLstAdjustment() != null && vapMdl.getLstAdjustment().size() > 0) {
                focusGrid();
            } else {
                txtWorkingAmount.setFocus(true);
            }

        }
    }

    public void hide() {
        hide(false);
    }

    private void hide(boolean withCallback) {
        popupPanel.hide();
        if (withCallback) {
            callBack.onClose();
        }
    }

    private void clear() {
        isEditable = false;
        lblAcName.setText("");
        lblAcName.getElement().setId("");
        isPartyCredit = false;
        txtWorkingAmount.setText("");
        txtPendingReference.setText("");
        txtTotalReferenced.setText("");
        grdAdjustment.getGrid().getStore().clear();
    }

    private void calculate() {
        double workingAmount = DmShared.vNull(txtWorkingAmount.getText());
        double pendingReference = 0;
        double totalReferenced = 0;

        for (GrdAdjustment_Mdl gaMdl : grdAdjustment.getGrid().getStore().getAll()) {
            if (!DmShared.xNull(gaMdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                totalReferenced += DmShared.vNull(gaMdl.getAdjustment());
            }
        }
        pendingReference = workingAmount - totalReferenced;

        txtWorkingAmount.setText(DmClient.vNull(txtWorkingAmount.getText(), "0.00"));
        txtPendingReference.setText(DmClient.vNull(pendingReference, "0.00"));
        txtTotalReferenced.setText(DmClient.vNull(totalReferenced, "0.00"));
    }

    private void focusGrid() {
        ((CellSelectionModel) grdAdjustment.getGrid().getSelectionModel()).selectCell(0, 0);
    }

    private void manageCallback() {
        searchPopup.addCallBack(new SearchPopup.CallBack() {
            @Override
            public void select(List<GrdAdjustment_Mdl> selections) {
                grdAdjustment.getGrid().getStore().clear();
                grdAdjustment.getGrid().getStore().addAll(selections);
                focusGrid();
            }

            @Override
            public void close() {
                focusGrid();
            }
        });
    }

    private void manageDisplay() {
        txtWorkingAmount.setReadOnly(true);
        txtPendingReference.setReadOnly(true);
        txtTotalReferenced.setReadOnly(true);
    }

    private void manageEvents() {
        gridEvents();
        btnClose.addClickHandler(this);
        btnFill.addClickHandler(this);
        btnOk.addClickHandler(this);
        btnAutoAdjust.addClickHandler(this);
        popupPanel.addAttachHandler(this);
    }

    private void gridEvents() {
        voucherAdjustmentGridEdit.addCallBack(new AccountAdjustmentGridEdit.CallBack() {
            @Override
            public void onOk() {
                grdAdjustment.commitChanges();
                calculate();
            }

            @Override
            public void onDelete() {
                grdAdjustment.commitChanges();
                calculate();
            }
        });

        grdAdjustment.getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {
            @Override
            public void onCellClick(CellClickEvent event) {
                if (event.getCellIndex() == viewHistoryColIndex) {
                    String adjDocId = DmShared.xNull(grdAdjustment.getGrid().getSelectionModel().getSelectedItem().getSharedKeyFieldCode());
                    String adjV_SNo = DmShared.xNull(grdAdjustment.getGrid().getSelectionModel().getSelectedItem().getV_SNo());
                    historyPopup.show(adjDocId, adjV_SNo);
                } else {
                    showVoucherEntryGridEdit(event.getRowIndex());
                }
            }
        });

        grdAdjustment.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (!event.getNativeEvent().getCtrlKey() && event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
                    if (((CellSelectionModel) grdAdjustment.getGrid().getSelectionModel()).getSelectCell().getCell() == viewHistoryColIndex) {
                        String adjDocId = DmShared.xNull(grdAdjustment.getGrid().getSelectionModel().getSelectedItem().getSharedKeyFieldCode());
                        String adjV_SNo = DmShared.xNull(grdAdjustment.getGrid().getSelectionModel().getSelectedItem().getV_SNo());
                        historyPopup.show(adjDocId, adjV_SNo);
                    } else {
                        showVoucherEntryGridEdit(grdAdjustment.getGrid().getStore().indexOf(grdAdjustment.getGrid().getSelectionModel().getSelectedItem()));
                    }
                }
            }
        }, KeyDownEvent.getType());

    }

    private void showVoucherEntryGridEdit(int currentRowIndex) {
        if (isEditable) {
            voucherAdjustmentGridEdit.show(currentRowIndex, this, true, false, true);
        } else {
            voucherAdjustmentGridEdit.show(currentRowIndex, this, false, false, false);
        }
    }

    private void onOk() {
        calculate();
        if (DmShared.vNull(txtTotalReferenced.getText()) > DmShared.vNull(txtWorkingAmount.getText())) {
            new DmAlertMessageBox("You cannot adjust excess amount.", new HideEvent.HideHandler() {
                @Override
                public void onHide(HideEvent event) {
                    focusGrid();
                }
            }).show();
            return;
        }
        if (DmShared.vNull(txtTotalReferenced.getText()) < DmShared.vNull(txtWorkingAmount.getText())) {
            new DmConfirmMessageBox("Are you sure? You want to adjust partially.", new DialogHideEvent.DialogHideHandler() {
                @Override
                public void onDialogHide(DialogHideEvent event) {
                    if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                        manageOk();
                    }
                }
            }).show();
        } else {
            manageOk();
        }
    }

    private void manageOk() {
        List<GrdAdjustment_Mdl> lstAdjustments = new ArrayList<>();
        for (GrdAdjustment_Mdl gaMdl : grdAdjustment.getGrid().getStore().getAll()) {
            lstAdjustments.add(gaMdl.clone());
        }
        callBack.onOk(lstAdjustments);
        hide(false);
    }

    private void showSearchPopup() {
        searchPopup.show(AccountAdjustment.this);
    }

    private void autoAdjust() {
        double workingAmount = DmShared.vNull(txtWorkingAmount.getText());
        double amount = 0, adjusted = 0, adjustment = 0;

        for (GrdAdjustment_Mdl gaMdl : grdAdjustment.getGrid().getStore().getAll()) {
            if (gaMdl != null) {
                if (!DmShared.xNull(gaMdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                    amount = DmShared.vNull(gaMdl.getAmount());
                    adjusted = DmShared.vNull(gaMdl.getAdjusted());
                    adjustment = amount - adjusted;

                    if ((workingAmount - adjustment) >= 0) {
                        gaMdl.setAdjustment(DmClient.vNull(adjustment, "0.00"));
                        workingAmount -= adjustment;
                    } else {
                        gaMdl.setAdjustment(DmClient.vNull(workingAmount, "0.00"));
                        workingAmount = 0;
                    }

                }
            }
        }

        grdAdjustment.commitChanges();
        calculate();
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnClose) {
            hide(true);
        } else if (event.getSource() == btnFill) {
            showSearchPopup();
        } else if (event.getSource() == btnOk) {
            onOk();
        } else if (event.getSource() == btnAutoAdjust) {
            autoAdjust();
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            voucherAdjustmentGridEdit.hide();
            searchPopup.hide();
            historyPopup.hide();
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide(true);
        } else if (rootEvent.getEvent().getNativeEvent().getAltKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_F) {
            showSearchPopup();
        } else if (rootEvent.getEvent().getNativeEvent().getAltKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_K) {
            onOk();
        } else if (rootEvent.getEvent().getNativeEvent().getAltKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_A) {
            autoAdjust();
        }
    }

    @Override
    public Widget asWidget() {
        return popupPanel;
    }

}
