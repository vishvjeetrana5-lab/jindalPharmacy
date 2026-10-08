package aarogya.master.D365ImportExport.client;

import aarogya.master.D365ImportExport.shared.ImportExportData_Srv;
import aarogya.master.D365ImportExport.shared.ImportExportData_SrvAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmBase.self.shared.SetWidgetInvoker;
import dataman.dmBase.self.shared.UserMenu;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public final class ImportExportData extends ImportExportData_Dsg implements BlurHandler, AttachEvent.Handler, ClickHandler, DmRootEvents {

    private final ServiceInvoker<ImportExportData_SrvAsync> SrvAsync;

    public ImportExportData(UserMenu menuPermission, SetWidgetInvoker swi) {
        SrvAsync = new ServiceInvoker<>(GWT.create(ImportExportData_Srv.class), ImportExportData.class.getName());
        this.asWidget().setTitle(widgetName);
        toolContainer.setTitle(this.asWidget().getTitle());
        toolContainer.addWidget(btnClose);
        toolContainer.addGap();
        btnClose.addClickHandler(this);
        btnSave.addClickHandler(this);
        btnExportPaymentDetails.addClickHandler(this);
        btnOPdPosting.addClickHandler(this);
        btnCancelIPDBill.addClickHandler(this);
        txtFromDate.addBlurHandler(this);
        flwForm.addAttachHandler(this);
        btnItemMaster.addClickHandler(this);
        btnDepartmentMaster.addClickHandler(this);
        btnIpdRevenue.addClickHandler(this);
        btnUnBilledService.addClickHandler(this);
        btnStockTransfer.addClickHandler(this);
        btnStockConsumption.addClickHandler(this);
        btnPharmacySaleBill.addClickHandler(this);
        btnTpaSettlement.addClickHandler(this);
        btnUpdateIndentStatus.addClickHandler(this);
        swi.setWidget(this);
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    @Override
    public void searchRecord(String key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (!event.isAttached()) {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);

        } else {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        try {
            if (event.getSource().equals(btnClose)) {
                onClose();
            } else if (event.getSource() == btnItemMaster) {
                syncItemMaster();
            } else if (event.getSource() == btnTpaSettlement) {
                importTpaSettlementBills();
            } else if (event.getSource() == btnCancelIPDBill && !DmShared.xNull(txtFromDate.getText()).equalsIgnoreCase("")) {
                getExportCancelIPDBills();
            } else if (event.getSource() == btnDepartmentMaster) {
                getSyncDepartmentMaster();
            } else if (event.getSource() == btnOPdPosting && !DmShared.xNull(txtFromDate.getText()).equalsIgnoreCase("")) {
                getOpdPostingDetails();
            } else if (event.getSource() == btnIpdRevenue && !DmShared.xNull(txtFromDate.getText()).equalsIgnoreCase("")) {
                getIpdRevenue();
            } else if (event.getSource() == btnExportPaymentDetails && !DmShared.xNull(txtFromDate.getText()).equalsIgnoreCase("")) {
                ExportPaymentDetails();
            } else if (event.getSource() == btnUnBilledService && !DmShared.xNull(txtFromDate.getText()).equalsIgnoreCase("")) {
                ExportUnBilledService();
            } else if (event.getSource() == btnStockTransfer && !DmShared.xNull(txtFromDate.getText()).equalsIgnoreCase("")) {
                exportstockTransfe();
            } else if (event.getSource() == btnStockConsumption && !DmShared.xNull(txtFromDate.getText()).equalsIgnoreCase("")) {
                exportStockComsumption();
            } else if (event.getSource() == btnPharmacySaleBill && !DmShared.xNull(txtFromDate.getText()).equalsIgnoreCase("")) {
                exportPharmacyBill();
            } else if (event.getSource() == btnUpdateIndentStatus) {
                updateIndentStatus();
            } else {
                new DmAlertMessageBox("Please fill from date", txtFromDate).show();
                return;
            }
        } catch (DmException e) {
        }
    }

    private void getExportCancelIPDBills() {
        try {
            SrvAsync.invokeRPC(true).exportCancelIPDBills(DmShared.xNull(txtFromDate.getText()), new AsyncCallback<DmResult<String>>() {

                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), btnSave).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        new DmAlertMessageBox("Susscessfully Export Cancel IPD Bill....", txtFromDate).show();
                    } else {
                        topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void getSyncDepartmentMaster() {
        try {
            SrvAsync.invokeRPC(true).syncDepartmentMaster(new AsyncCallback<DmResult<String>>() {

                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), btnSave).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        new DmAlertMessageBox("Department Master Sync successfully.", txtFromDate).show();
                    } else {
                        topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void exportstockTransfe() {
        try {
            SrvAsync.invokeRPC(true).exportStockTransfer(txtFromDate.getText(), new AsyncCallback<DmResult<String>>() {

                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), btnSave).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        new DmAlertMessageBox("Stocke Transfer successfully..", txtFromDate).show();
                    } else {
                        topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void exportStockComsumption() {
        try {
            SrvAsync.invokeRPC(true).exportStockComsumption(txtFromDate.getText(), new AsyncCallback<DmResult<String>>() {

                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), btnSave).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        new DmAlertMessageBox("Stocke Consumption Data send successfully..", txtFromDate).show();
                    } else {
                        topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void exportPharmacyBill() {
        try {
            SrvAsync.invokeRPC(true).exportPharmacyBillStock(txtFromDate.getText(), new AsyncCallback<DmResult<String>>() {

                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), btnSave).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        new DmAlertMessageBox("Pharmacy sale Bill Stock Data send successfully..", txtFromDate).show();
                    } else {
                        topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void ExportUnBilledService() {
        try {
            SrvAsync.invokeRPC(true).exportUnBilledService(txtFromDate.getText(), new AsyncCallback<DmResult<String>>() {

                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), btnSave).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        new DmAlertMessageBox("UnBilled Service data successfully posted into Staging Database.", txtFromDate).show();
                    } else {
                        topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void ExportPaymentDetails() {
        try {
            SrvAsync.invokeRPC(true).exportPaymentDetails(txtFromDate.getText(), new AsyncCallback<DmResult<String>>() {

                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), btnSave).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        new DmAlertMessageBox("sale data successfully posted into Staging Database.", txtFromDate).show();
                    } else {
                        topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void syncItemMaster() {
        try {
            SrvAsync.invokeRPC(true).getItemDetails(new AsyncCallback<DmResult<String>>() {

                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), btnSave).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        new DmAlertMessageBox("Record saved successfully", txtFromDate).show();
                    } else {
                        topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }

    }

    private void getOpdPostingDetails() {
        try {
            SrvAsync.invokeRPC(true).exportOpdPosting(txtFromDate.getText(), new AsyncCallback<DmResult<String>>() {

                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), btnSave).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        new DmAlertMessageBox("OPD Revenue data Succesefuly data post into Staging...", txtFromDate).show();
                    } else {
                        topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }

    }

    private void getIpdRevenue() {
        SrvAsync.invokeRPC(true).getIpdRevenue(txtFromDate.getText(), new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtFromDate).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    new DmAlertMessageBox("IPD Revenue data Succesefuly data post into Staging...", txtFromDate).show();
                } else {
                    topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                }

            }
        });
    }

    private void importTpaSettlementBills() {
        SrvAsync.invokeRPC(true).getTpaSettlementBills(txtFromDate.getText(), new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtFromDate).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    new DmAlertMessageBox("Import Tpa Settlement bill successfully.", txtFromDate).show();
                } else {
                    topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                }

            }
        });
    }

    private void updateIndentStatus() {
        try {
            SrvAsync.invokeRPC(true).updateIndentStatus(new AsyncCallback<DmResult<String>>() {

                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), btnSave).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        new DmAlertMessageBox("Indent Status updated successfully.", txtFromDate).show();
                    } else {
                        topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void topControlSaveError(int errorNo, String errorMessage) {

        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtFromDate).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtFromDate).show();
        }
    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(ImportExportData.this);
                }
            }
        }).show();
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            onClose();
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtFromDate)) {
            txtFromDate.setText(DmClient.returnDate(txtFromDate.getText()));

        }
    }

}
