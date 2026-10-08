package dataman.accounts.voucherAdjustmentEntry.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.accounts.voucherAdjustmentEntry.shared.GrdLedger_Mdl;
import dataman.accounts.voucherAdjustmentEntry.shared.VoucherAdjustmentEntry_Srv;
import dataman.accounts.voucherAdjustmentEntry.shared.VoucherAdjustmentEntry_SrvAsync;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmBase.self.shared.SetWidgetInvoker;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import java.util.ArrayList;
import java.util.List;

public class VoucherAdjustmentEntry extends VoucherAdjustmentEntry_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, ClickHandler {

    final ServiceInvoker<VoucherAdjustmentEntry_SrvAsync> srvAsync;
    final SearchPopup voucherSearch = new SearchPopup();
    final VoucherAdjustmentEntryGridEdit<GrdLedger_Mdl, GrdLedger_PrpAcs> adjustmentGridEdit = new VoucherAdjustmentEntryGridEdit<GrdLedger_Mdl, GrdLedger_PrpAcs>(grdAdjustment);

    public VoucherAdjustmentEntry(SetWidgetInvoker swi) {
        super();
        srvAsync = new ServiceInvoker<VoucherAdjustmentEntry_SrvAsync>(GWT.create(VoucherAdjustmentEntry_Srv.class), VoucherAdjustmentEntry.class.getName());
        manageEvents();
        manageCallBacks();
        swi.setWidget(this);
    }

    private void manageCallBacks() {
        voucherSearch.addCallBack(new SearchPopup.CallBack() {
            @Override
            public void select(String voucherDocId) {
                fillGrid(voucherDocId);
            }

            @Override
            public void close() {
                focusGrid(0);
            }
        });

    }

    private void fillGrid(String voucherDocId) {
        srvAsync.invokeRPC(true).getLedgerByDocId(voucherDocId, new AsyncCallback<DmResult<List<GrdLedger_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), btnFill).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdLedger_Mdl>> result) {
                if (result.isStatus()) {
                    grdAdjustment.getGrid().getStore().clear();
                    grdAdjustment.getGrid().getStore().addAll(result.getData());
                    focusGrid(0);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), btnFill).show();
                }
            }
        });
    }

    private void focusGrid(int rowindex) {
        try {
            ((CellSelectionModel) grdAdjustment.getGrid().getSelectionModel()).selectCell(rowindex, 1);
        } catch (Exception ex) {
            ((CellSelectionModel) grdAdjustment.getGrid().getSelectionModel()).selectCell(0, 1);
        }
    }

    private void close() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {
            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(VoucherAdjustmentEntry.this);
                }
            }
        }).show();
    }

    private void save() {
        List<GrdLedger_Mdl> lstAdjusted = new ArrayList<>();

        for (GrdLedger_Mdl glMdl : grdAdjustment.getGrid().getStore().getAll()) {
            if (glMdl != null) {
                if (!DmShared.xNull(glMdl.getSharedKeyFieldCode()).trim().equals("")) {
                    if ((glMdl.getLstAdjustment() != null && !glMdl.getLstAdjustment().isEmpty())
                            || (glMdl.getLstItemAdjustment() != null && !glMdl.getLstItemAdjustment().isEmpty())) {
                        lstAdjusted.add(glMdl);
                    }
                }
            }
        }

        if (lstAdjusted.isEmpty()) {
            new DmAlertMessageBox("Please adjust a line before save.", btnSave).show();
            return;
        }

        srvAsync.invokeRPC(true).saveRecord(lstAdjusted, new AsyncCallback<DmResult<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    new DmAlertMessageBox("Record saved successfully!", new HideEvent.HideHandler() {
                        @Override
                        public void onHide(HideEvent event) {
                            grdAdjustment.getGrid().getStore().clear();
                        }
                    }).show();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void manageEvents() {
        gridEvents();
        flwForm.addAttachHandler(this);
        btnFill.addClickHandler(this);
        btnClose.addClickHandler(this);
        btnSave.addClickHandler(this);
    }

    private void gridEvents() {
        adjustmentGridEdit.addCallBack(new VoucherAdjustmentEntryGridEdit.CallBack() {
            @Override
            public void onOk() {
                grdAdjustment.commitChanges();
            }

            @Override
            public void onDelete() {
                grdAdjustment.commitChanges();
            }
        });

        grdAdjustment.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                showAdjustmentGridEdit(event.getRowIndex());
            }
        });
        grdAdjustment.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showAdjustmentGridEdit(grdAdjustment.getGrid().getStore().indexOf(grdAdjustment.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
    }

    private void showAdjustmentGridEdit(int currentRowIndex) {
        adjustmentGridEdit.show(currentRowIndex, true, true, true);
    }

    private void showVoucherSearch() {
        voucherSearch.show(this);
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnFill) {
            showVoucherSearch();
        } else if (event.getSource() == btnSave) {
            save();
        } else if (event.getSource() == btnClose) {
            close();
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            adjustmentGridEdit.hide();
            voucherSearch.hide();
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            close();
        } else if (rootEvent.getEvent().getNativeEvent().getAltKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_F) {
            showVoucherSearch();
        } else if (rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_S) {
            save();
        }
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

}
