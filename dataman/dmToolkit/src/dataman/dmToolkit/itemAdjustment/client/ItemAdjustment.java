package dataman.dmToolkit.itemAdjustment.client;

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
import dataman.dmBase.self.shared.SetWidgetInvoker;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmToolkit.itemAdjustment.shared.*;
import dataman.dmToolkit.self.client.SetWidgetInvokerImpl;
import java.util.ArrayList;
import java.util.List;

public class ItemAdjustment extends ItemAdjustment_Dsg implements IsWidget, ClickHandler, AttachEvent.Handler, DmRootEvents {

    //<editor-fold defaultstate="collapsed" desc="Callback">
    public interface CallBack {

        public void onOk(List<GrdItemAdjustment_Mdl> lstAdjustments);

        public void onClose();
    }

    private CallBack callBack;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
    //</editor-fold>

    protected boolean isEditable = false, isPartyCredit = false;
    protected ServiceInvoker<ItemAdjustment_SrvAsync> srvAsync;
    protected SearchPopup searchPopup = new SearchPopup();
    protected ItemAdjustmentGridEdit<GrdItemAdjustment_Mdl, GrdAdjustment_PrpAcs> itemAdjustmentGridEdit = new ItemAdjustmentGridEdit(grdAdjustment);

    public ItemAdjustment() {
        super();
        srvAsync = new ServiceInvoker<ItemAdjustment_SrvAsync>(GWT.create(ItemAdjustment_Srv.class), ItemAdjustment.class.getName());
        manageCallback();
        manageDisplay();
        manageEvents();
    }

    public void show(ItemAdjustment_Mdl vapMdl) {
        if (!DmShared.xNull(vapMdl.getAcCode()).trim().equals("")) {
            clear();
            this.isEditable = vapMdl.getIsEditable();
            this.lblAcName.setText(vapMdl.getAcName());
            this.lblAcName.getElement().setId(vapMdl.getAcCode());

            double amount = DmShared.vNull(vapMdl.getAmtDr()) - DmShared.vNull(vapMdl.getAmtCr());
            isPartyCredit = amount < 0;
            txtWorkingAmount.setText(DmClient.vNull(Math.abs(amount), "0.00"));
            grdAdjustment.getGrid().getStore().addAll(vapMdl.getLstItemAdjustment());
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

            if (vapMdl.getLstItemAdjustment() != null && vapMdl.getLstItemAdjustment().size() > 0) {
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
        txtPendingAmount.setText("");
        txtTotalAdjustment.setText("");
        txtTotalValue.setText("");
        grdAdjustment.getGrid().getStore().clear();
    }

    private void calculate() {
        double workingAmount = DmShared.vNull(txtWorkingAmount.getText());
        double pendingAmount = 0;
        double totalAmount = 0;
        double totalValue = 0;
        double totalValueAbs = 0;

        for (GrdItemAdjustment_Mdl gaMdl : grdAdjustment.getGrid().getStore().getAll()) {
            if (!DmShared.xNull(gaMdl.getSharedKeyFieldCode()).trim().equals("")) {
                totalAmount += Math.abs(DmShared.vNull(gaMdl.getAdjustment()));
                totalValueAbs += Math.abs(DmShared.vNull(gaMdl.getLandedSkuValue()));
                totalValue += DmShared.vNull(gaMdl.getLandedSkuValue());
            }
        }
        pendingAmount = workingAmount - totalAmount;

        txtWorkingAmount.setText(DmClient.vNull(txtWorkingAmount.getText(), "0.00"));
        txtPendingAmount.setText(DmClient.vNull(pendingAmount, "0.00"));
        txtTotalAdjustment.setText(DmClient.vNull(totalAmount, "0.00"));
        txtTotalValue.setText(DmClient.vNull(totalValue, "0.00"));
        txtTotalValue.setTag(DmClient.vNull(totalValueAbs, "0.00"));
    }

    private void focusGrid() {
        ((CellSelectionModel) grdAdjustment.getGrid().getSelectionModel()).selectCell(0, 0);
    }

    private void manageCallback() {
        searchPopup.addCallBack(new SearchPopup.CallBack() {
            @Override
            public void select(List<GrdItemAdjustment_Mdl> selections) {
                grdAdjustment.getGrid().getStore().clear();
                grdAdjustment.getGrid().getStore().addAll(selections);
                calculate();
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
        txtPendingAmount.setReadOnly(true);
        txtTotalAdjustment.setReadOnly(true);
        txtTotalValue.setReadOnly(true);
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
        itemAdjustmentGridEdit.addCallBack(new ItemAdjustmentGridEdit.CallBack() {
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
                if (event.getCellIndex() == viewEntryPointColIndex) {
                    showEntryPoint();
                } else {
                    showAdjustmentGridEdit(event.getRowIndex());
                }
            }
        });

        grdAdjustment.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (!event.getNativeEvent().getCtrlKey() && event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
                    if (((CellSelectionModel) grdAdjustment.getGrid().getSelectionModel()).getSelectCell().getCell() == viewEntryPointColIndex) {
                        showEntryPoint();
                    } else {
                        showAdjustmentGridEdit(grdAdjustment.getGrid().getStore().indexOf(grdAdjustment.getGrid().getSelectionModel().getSelectedItem()));
                    }
                }
            }
        }, KeyDownEvent.getType());

    }

    private void showAdjustmentGridEdit(int currentRowIndex) {
        if (isEditable) {
            itemAdjustmentGridEdit.show(currentRowIndex, this, true, false, true);
        } else {
            itemAdjustmentGridEdit.show(currentRowIndex, this, false, false, false);
        }
    }

    private void onOk() {
        calculate();
        if (DmShared.vNull(txtTotalAdjustment.getText()) > DmShared.vNull(txtWorkingAmount.getText())) {
            new DmAlertMessageBox("You cannot adjust excess amount.", new HideEvent.HideHandler() {
                @Override
                public void onHide(HideEvent event) {
                    focusGrid();
                }
            }).show();
            return;
        }
        if (DmShared.vNull(txtTotalAdjustment.getText()) < DmShared.vNull(txtWorkingAmount.getText())) {
            if (DmShared.vNull(txtTotalAdjustment.getText()) != 0) {
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
        } else {
            manageOk();
        }
    }

    private void manageOk() {
        List<GrdItemAdjustment_Mdl> lstAdjustments = new ArrayList<>();
        for (GrdItemAdjustment_Mdl gaMdl : grdAdjustment.getGrid().getStore().getAll()) {
            lstAdjustments.add(gaMdl.clone());
        }
        callBack.onOk(lstAdjustments);
        hide(false);
    }

    private void showSearchPopup() {
        searchPopup.show(ItemAdjustment.this);
    }

    private void autoAdjust() {
        GrdItemAdjustment_Mdl lastGiaMdl = null;
        double totalAdjustment = 0;

        for (GrdItemAdjustment_Mdl giaMdl : grdAdjustment.getGrid().getStore().getAll()) {
            if (giaMdl != null) {
                if (!DmShared.xNull(giaMdl.getSharedKeyFieldCode()).trim().equals("")) {
                    double adjustment = (DmShared.vNull(giaMdl.getLandedSkuValue()) * DmShared.vNull(txtWorkingAmount.getText())) / DmShared.vNull(txtTotalValue.getTag());

                    if (isPartyCredit) {
                        adjustment = 0 - Math.abs(adjustment);
                    } else {
                        adjustment = Math.abs(adjustment);
                    }
                    giaMdl.setAdjustment(DmClient.vNull(adjustment, "0.00"));

                    totalAdjustment += DmShared.vNull(DmClient.vNull(Math.abs(adjustment), "0.00"));
                    lastGiaMdl = giaMdl;
                }
            }
        }

        if (lastGiaMdl != null) {
            double adjustment = DmShared.vNull(DmClient.vNull(lastGiaMdl.getAdjustment(), "0.00")) - (DmShared.vNull(DmClient.vNull(totalAdjustment, "0.00")) - DmShared.vNull(DmClient.vNull(txtWorkingAmount.getText(), "0.00")));
            lastGiaMdl.setAdjustment(DmClient.vNull(adjustment, "0.00"));
        }

        grdAdjustment.commitChanges();
        calculate();
    }

    private void showEntryPoint() {
        new DmConfirmMessageBox("Are you sure? You want to view entry.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                    final GrdItemAdjustment_Mdl giaMdl = grdAdjustment.getGrid().getSelectionModel().getSelectedItem();

                    if (giaMdl != null) {
                        if (!DmShared.xNull(giaMdl.getV_MnuText()).trim().equals("") && !DmShared.xNull(giaMdl.getV_MnuAttachedInModule()).trim().equals("")) {
                            DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(giaMdl.getV_MnuAttachedInModule(), giaMdl.getV_MnuText(), null, null, new SetWidgetInvokerImpl() {
                                @Override
                                public void setWidget(final IsWidget wgtObject) {
                                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                                    ((IsWidgetExtended) wgtObject).searchRecord(giaMdl.getSharedKeyFieldCode());
                                }
                            });
                        }
                    }
                }
            }
        }).show();
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
            itemAdjustmentGridEdit.hide();
            searchPopup.hide();
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
