package aarogya.pharmacy.medicineOpening.client;

import aarogya.pharmacy.medicineOpening.shared.MedicineOpening_SrvAsync;
import aarogya.pharmacy.medicineOpening.shared.MedicineOpening_Srv;
import aarogya.pharmacy.medicineOpening.shared.GrdMedicineDetail_Mdl;
import aarogya.pharmacy.medicineOpening.shared.TxtHBatchNo_Mdl;
import aarogya.pharmacy.medicineOpening.shared.TxtHMedicineName_Mdl;
import aarogya.selfToolkit.stStatic.client.STClient;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.List;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.Date;

public class MedicineOpeningGridEdit<T1, T2> extends MedicineOpeningGridEdit_Dsg<T1, T2> implements BlurHandler {

    interface CallBack {

        public void onOk(GrdMedicineDetail_Mdl opdService);

        public void onDelete();
    }

    private ServiceInvoker<MedicineOpening_SrvAsync> srvAsync;
    private CallBack callBack;
    private MedicineOpening medicineOpening;
    String selectedBatchNo = "", isBestBefore = "", isBarcodeActive = "";
    String selectedMedicineCode = "", skuRate = "", skuMrp = "", skuSaleRate = "", skuCode = "", ibCode = "";
    private int hashCode = 0;
    private boolean flagBrowseMode = true;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void textHelpEvents() {
        txtHMedicineName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHMedicineName_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                if (!DmShared.xNull(medicineOpening.txtHDepartment.getTag()).equalsIgnoreCase("")) {
                    srvAsync.invokeRPC(blnCallBackWait).getTxtHMedicineName(DmShared.xNull(medicineOpening.txtHDepartment.getTag()), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHMedicineName_Mdl>>>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            new DmAlertMessageBox(caught.getMessage(), txtHMedicineName).show();
                        }

                        @Override
                        public void onSuccess(DmResultPageing<List<TxtHMedicineName_Mdl>> result) {
                            if (result.isStatus()) {
                                txtHMedicineName.setData(result.getData(), result.getPaging());
                            } else {
                                new DmAlertMessageBox(result.getErrorMessage(), txtHMedicineName).show();
                            }
                        }
                    });
                }
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHMedicineName_Mdl t1Row) {
                if (!selectedMedicineCode.equalsIgnoreCase(DmShared.xNull(txtHMedicineName.getTag()))) {
                    resetItem();
                }
                if (blnStatus && t1Row != null) {
                    selectedMedicineCode = DmShared.xNull(txtHMedicineName.getTag());
                    isBestBefore = DmShared.xNull(t1Row.getIsBestBefore());
                    isBarcodeActive = DmShared.xNull(t1Row.getIsBarcodeActive());
                    skuCode = DmShared.xNull(t1Row.getSkuCode());

                    txtUnit.setTag(DmShared.xNull(t1Row.getPurchaseUnitName()));
                    txtUnit.setTag2nd(DmShared.xNull(t1Row.getPurchaseUnitCode()));
                    if (DmShared.iNull(t1Row.getPurchaseUnitCode()).longValue() != DmShared.iNull(t1Row.getSkuCode()).longValue()) {
                        txtUnit.setText(DmShared.xNull(t1Row.getPurchaseUnitName()) + " = " + DmShared.xNull(t1Row.getPurchaseUnitConversionRate()) + " " + DmShared.xNull(t1Row.getSkuName()));
                    } else {
                        txtUnit.setText(DmShared.xNull(t1Row.getPurchaseUnitName()));
                    }
                    txtDisplayUnitQty.setText(txtUnit.getText());
                    txtDisplayUnitQty.setTag(DmShared.xNull(t1Row.getPurchaseUnitDecimalPlaces()));
                    txtDisplayUnitSaleRate.setText(txtUnit.getText());
                    txtSKUTotalQty.setText(DmShared.xNull(t1Row.getSkuName()));
                    txtSKUTotalQty.setTag(DmShared.xNull(t1Row.getPurchaseUnitConversionRate()));
                    txtSKUTotalQty.setTag2nd(DmShared.xNull(t1Row.getSkuDecimalPlaces()));
                    txtSKUQty.setText(DmShared.xNull(t1Row.getSkuName()));
                    txtSKUQty.setTag(DmShared.xNull(t1Row.getPurchaseUnitConversionRate()));
                    txtSKUQty.setTag2nd(DmShared.xNull(t1Row.getSkuDecimalPlaces()));
                    txtSKUFreeQty.setText(DmShared.xNull(t1Row.getSkuName()));
                    txtSKUFreeQty.setTag(DmShared.xNull(t1Row.getPurchaseUnitConversionRate()));
                    txtSKUFreeQty.setTag2nd(DmShared.xNull(t1Row.getSkuDecimalPlaces()));
                    manageMedicineState(true);
                }
            }
        });

        txtHBatchNo.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHBatchNo_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHBatchNo(txtHMedicineName.getTag(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHBatchNo_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHBatchNo).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHBatchNo_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHBatchNo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHBatchNo).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHBatchNo_Mdl t1Row) {
                if (!DmShared.xNull(selectedBatchNo).equalsIgnoreCase(DmShared.xNull(txtHBatchNo.getText()))) {
                    resetBatchDetails();

                }
                if (blnStatus && t1Row != null) {
                    selectedBatchNo = DmShared.xNull(txtHBatchNo.getText());
                    txtMRP.setText(DmShared.xNull(t1Row.getMrp()));
                    txtBarcode.setText(DmShared.xNull(t1Row.getBarcode()));
                    txtSaleRate.setText(DmShared.xNull(t1Row.getSaleRate()));
                    txtExpiryDate.setText(DmShared.dNull(t1Row.getExpireDate()));
                    txtManufacturingDate.setText(DmShared.dNull(t1Row.getMfgDate()));
                    txtBestBefore.setText(DmShared.xNull(t1Row.getBestBeforeDays()));
                    calculate();
                }
            }
        });

    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, MedicineOpening medicineOpening) {
        this.medicineOpening = medicineOpening;
        super.showCenter(currentRowIndex, okNeeded, newNeeded, deleteNeeded);

        if (okNeeded && newNeeded && deleteNeeded) {
            flagBrowseMode = false;
        } else {
            flagBrowseMode = true;
        }

        manageDisplay(!flagBrowseMode);

        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            @Override
            public void execute() {
                txtHMedicineName.setFocus(true);
            }
        });
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtRefDate) {
            txtRefDate.setText(DmClient.returnDate(txtRefDate.getText()));
        } else if (event.getSource() == txtManufacturingDate) {
            if (!DmShared.xNull(txtManufacturingDate.getText()).equalsIgnoreCase("")) {
                txtManufacturingDate.setText(STClient.getDateFromMonthYear(txtManufacturingDate.getText(), false));
                setExpiryDate();
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        txtBestBefore.setFocus(true);
                    }
                });
            } else {
                setExpiryDate();
            }
        } else if ((event.getSource() == txtRate)
                || (event.getSource() == txtTotalQty) || (event.getSource() == txtMRP) || (event.getSource() == txtSaleRate)) {
            calculate();
        } else if (event.getSource() == txtBestBefore) {
            setExpiryDate();
        } else if (event.getSource() == txtExpiryDate) {
            txtExpiryDate.setText(STClient.getDateFromMonthYear(txtExpiryDate.getText(), true));
        }
    }

    private void setExpiryDate() {
        if (!DmShared.xNull(txtBestBefore.getText()).equalsIgnoreCase("") && !DmShared.xNull(txtManufacturingDate.getText()).equalsIgnoreCase("")) {
            Date tempDate = DmShared.convertDate(DmShared.dNull(txtManufacturingDate.getText()));
            CalendarUtil.addDaysToDate(tempDate, DmShared.iNull(txtBestBefore.getText()).intValue());
            txtExpiryDate.setText(DmShared.dNull(tempDate));
        } else {
            txtExpiryDate.setText("");
        }
    }

    @Override
    public void displayRow(T1 row) {
        clear();

        GrdMedicineDetail_Mdl ourRow = (GrdMedicineDetail_Mdl) row;
        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtHMedicineName.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHMedicineName.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtSKUTotalQty.setTag2nd(DmShared.xNull(ourRow.getSkuDecimalPlaces()));
        txtSKUTotalQty.setText(DmShared.xNull(ourRow.getSkuName()));
        txtSKUTotalQty.setTag(DmShared.xNull(ourRow.getSkuQtyForRate()));
        txtUnitQuantity.setText(DmShared.xNull(ourRow.getPurchaseUnitQuantity()));
        txtSKUFreeQty.setTag2nd(DmShared.xNull(ourRow.getSkuDecimalPlaces()));
        txtSKUFreeQty.setText(DmShared.xNull(ourRow.getSkuName()));
        txtSKUFreeQty.setTag(DmShared.xNull(ourRow.getSkuQtyForRate()));
        txtSKUQty.setTag2nd(DmShared.xNull(ourRow.getSkuDecimalPlaces()));
        txtSKUQty.setText(DmShared.xNull(ourRow.getSkuName()));
        txtSKUQty.setTag(DmShared.xNull(ourRow.getSkuQtyForRate()));
        skuCode = DmShared.xNull(ourRow.getSkuCode());
        txtTotalQty.setText(DmShared.xNull(ourRow.getTotalQuantity()));
        txtRate.setText(DmClient.vNull(ourRow.getRate(), "0.00"));
        txtAmount.setText(DmClient.vNull(ourRow.getSkuValue(), "0.00"));
        txtHBatchNo.setText(DmShared.xNull(ourRow.getBatchNo()));
        ibCode = DmShared.xNull(ourRow.getIbCode());
        txtBarcode.setText(DmShared.xNull(ourRow.getBarcode()));
        txtMRP.setText(DmClient.vNull(ourRow.getMrp(), "0.00"));
        txtSaleRate.setText(DmClient.vNull(ourRow.getSaleRate(), "0.00"));
        if (DmShared.iNull(ourRow.getPurchaseUnitCode()).longValue() != DmShared.iNull(ourRow.getSkuCode()).longValue()) {
            txtUnit.setText(DmShared.xNull(ourRow.getPurchaseUnitName()) + " = " + DmShared.xNull(ourRow.getSkuQtyForRate()) + " " + DmShared.xNull(ourRow.getSkuName()));
        } else {
            txtUnit.setText(DmShared.xNull(ourRow.getPurchaseUnitName()));
        }
        txtUnit.setTag(DmShared.xNull(ourRow.getPurchaseUnitName()));
        txtUnit.setTag2nd(DmShared.xNull(ourRow.getPurchaseUnitCode()));
        txtDisplayUnitQty.setText(txtUnit.getText());
        txtDisplayUnitQty.setTag(DmShared.xNull(ourRow.getPurchaseUnitDecimalPlaces()));
        txtDisplayUnitSaleRate.setText(txtUnit.getText());
        txtManufacturingDate.setText(DmShared.dNull(ourRow.getManufacturingDate()));
        txtBestBefore.setText(DmShared.xNull(ourRow.getBestBeforeDays()));
        txtExpiryDate.setText(DmShared.dNull(ourRow.getExpiryDate()));
        txtRefNo.setText(DmShared.xNull(ourRow.getRefNo()));
        txtRefDate.setText(DmShared.dNull(ourRow.getRefDate()));
        txtRemark.setText(DmShared.xNull(ourRow.getRemark()));
        selectedBatchNo = DmShared.xNull(ourRow.getBatchNo());
        selectedMedicineCode = DmShared.xNull(ourRow.getSharedKeyFieldCode());
        isBestBefore = DmShared.iNull(ourRow.getIsBestBefore()).toString();
        isBarcodeActive = DmShared.xNull(ourRow.getIsBarcodeActive());
        skuRate = DmShared.xNull(ourRow.getSkuRate());
        skuMrp = ourRow.getSkuMrp();
        skuSaleRate = ourRow.getSkuSaleRate();

        txtHMedicineName.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdMedicineDetail_Mdl();
    }

    @Override
    public void onOk() {
        boolean flag = false;
        try {
            calculate();
            if (DmShared.xNull(txtHMedicineName.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Medicine Name is mandatory.", txtHMedicineName).show();
                return;
            }
            if (DmShared.vNull(txtTotalQty.getText()).doubleValue() == 0) {
                new DmAlertMessageBox("Quantity is mandatory.", txtTotalQty).show();
                return;
            }
            if (DmShared.vNull(txtRate.getText()).doubleValue() == 0) {
                new DmAlertMessageBox("Rate is mandatory.", txtRate).show();
                return;
            }
            if (DmShared.vNull(txtAmount.getText()).doubleValue() == 0) {
                new DmAlertMessageBox("Amount cannot be zero", txtRate).show();
                return;
            }
            if (DmShared.xNull(txtHBatchNo.getText()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Batch No. is mandatory.", txtHBatchNo).show();
                return;
            }
            if (DmShared.vNull(txtMRP.getText()).doubleValue() == 0) {
                new DmAlertMessageBox("MRP is mandatory", txtMRP).show();
                return;
            }
            if (DmShared.vNull(txtSaleRate.getText()).doubleValue() == 0) {
                new DmAlertMessageBox("Sale Rate is mandatory.", txtSaleRate).show();
                return;
            }
            if (DmShared.vNull(txtSaleRate.getText()).doubleValue() > DmShared.vNull(txtMRP.getText()).doubleValue()) {
                new DmAlertMessageBox("Sale Rate cannot be greater than MRP.", txtSaleRate).show();
                return;
            }

            if (DmShared.vNull(txtRate.getText()).doubleValue() > DmShared.vNull(txtMRP.getText()).doubleValue()) {
                new DmAlertMessageBox("Rate cannot be greater than MRP.", txtRate).show();
                return;
            }
            if (DmShared.iNull(isBestBefore).intValue() == 1) {
                if (DmShared.dNull(txtManufacturingDate.getText()).equalsIgnoreCase("")) {
                    new DmAlertMessageBox("Manufacturing Date is mandatory.", txtManufacturingDate).show();
                    return;
                }
                if (DmShared.convertDate(txtManufacturingDate.getText()).getTime() > DmShared.convertDate(DmShared.dNull(medicineOpening.txtVoucherDate.getText())).getTime()) {
                    new DmAlertMessageBox("Manufacturing Date cannot be greater than opening date.", txtManufacturingDate).show();
                    return;
                }

                if (DmShared.vNull(txtBestBefore.getText()).longValue() == 0) {
                    new DmAlertMessageBox("Best Before Days is mandatory.", txtBestBefore).show();
                    return;
                }
            }
            if (DmShared.dNull(txtExpiryDate.getText()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Expiry Date is mandatory.", txtExpiryDate).show();
                return;
            }
            if (!DmShared.dNull(txtRefDate.getText()).equalsIgnoreCase("")) {
                if (DmShared.convertDate(txtRefDate.getText()).getTime() > DmShared.convertDate(DmShared.dNull(medicineOpening.txtVoucherDate.getText())).getTime()) {
                    new DmAlertMessageBox("Ref. Date cannot be greater than opening date.", txtRefDate).show();
                    return;
                }
            }

            if (DmShared.iNull(isBarcodeActive).intValue() == 1) {
                if (DmShared.xNull(txtBarcode.getText()).trim().equalsIgnoreCase("")) {
                    new DmAlertMessageBox("Barcode is mandatory.", txtBarcode).show();
                    return;
                }
            } else {
                if (medicineOpening.topControl.getMode().equals("Edit")) {
                    if (!DmShared.xNull(txtBarcode.getText()).trim().equals("")) {
                        new DmConfirmMessageBox("Barcode is not applicable. Do you want to continue?", new DialogHideEvent.DialogHideHandler() {

                            @Override
                            public void onDialogHide(DialogHideEvent event) {
                                if (event.getHideButton() == Dialog.PredefinedButton.NO) {
                                    txtBarcode.setFocus(true);
                                }
                            }
                        }).show();
                    }
                }
            }

            for (GrdMedicineDetail_Mdl detail_Mdl : (List<GrdMedicineDetail_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
                flag = true;
                if (!DmClient.vNull(detail_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                    if (DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(txtHMedicineName.getTag()))
                            && DmShared.vNull(detail_Mdl.getRate()).doubleValue() == DmShared.vNull(txtRate.getText()).doubleValue()
                            && DmShared.xNull(detail_Mdl.getBatchNo()).equalsIgnoreCase(DmShared.xNull(txtHBatchNo.getText()))
                            && DmShared.xNull(detail_Mdl.getBarcode()).equalsIgnoreCase(DmShared.xNull(txtBarcode.getText()))
                            && DmShared.vNull(detail_Mdl.getMrp()).doubleValue() == DmShared.vNull(txtMRP.getText()).doubleValue()
                            && DmShared.vNull(detail_Mdl.getSaleRate()).doubleValue() == DmShared.vNull(txtSaleRate.getText()).doubleValue()
                            && DmShared.convertDate(DmShared.dNull(detail_Mdl.getExpiryDate())).getTime() == DmShared.convertDate(DmShared.dNull(txtExpiryDate.getText())).getTime()
                            && DmShared.dNull(detail_Mdl.getManufacturingDate()).equalsIgnoreCase(DmShared.dNull(txtManufacturingDate.getText()))) {

                        if (!DmShared.dNull(txtManufacturingDate.getText()).trim().equals("")) {
                            if (DmShared.convertDate(DmShared.dNull(detail_Mdl.getManufacturingDate())).getTime() != DmShared.convertDate(DmShared.dNull(txtManufacturingDate.getText())).getTime()) {
                                flag = false;
                            }
                        }

                        if (flag) {
                            String message = "<div style='float:right; margin-right: 5px;'>";
                            message += "Record already exist with following details - <br>";
                            message += (DmShared.xNull(txtHMedicineName.getText()).trim().equals("")) ? "" : "<b>Medicine:</b> " + DmShared.xNull(txtHMedicineName.getText()) + "<br>";
                            message += (DmShared.xNull(txtHBatchNo.getText()).trim().equals("")) ? "" : "<b>Batch No.:</b> " + DmShared.xNull(txtHBatchNo.getText()) + "<br>";
                            message += (DmShared.xNull(txtBarcode.getText()).trim().equals("")) ? "" : "<b>Barcode:</b> " + DmShared.xNull(txtBarcode.getText()) + "<br>";
                            message += (DmShared.vNull(txtRate.getText()) == 0) ? "" : "<b>Rate:</b> " + DmClient.vNull(txtRate.getText(), "0.00") + "<br>";
                            message += (DmShared.vNull(txtMRP.getText()) == 0) ? "" : "<b>MRP:</b> " + DmClient.vNull(txtMRP.getText(), "0.00") + "<br>";
                            message += (DmShared.vNull(txtSaleRate.getText()) == 0) ? "" : "<b>Sale Rate:</b> " + DmClient.vNull(txtSaleRate.getText(), "0.00") + "<br>";
                            message += (DmShared.dNull(txtManufacturingDate.getText()).trim().equals("")) ? "" : "<b>Manufacturing Date:</b> " + DmShared.dNull(txtManufacturingDate.getText()) + "<br>";
                            message += (DmShared.dNull(txtExpiryDate.getText()).trim().equals("")) ? "" : "<b>Expiry Date:</b> " + DmShared.dNull(txtExpiryDate.getText()) + "<br>";
                            message += "</div>";
                            new DmAlertMessageBox(message, txtHMedicineName).show();
                            return;
                        }
                    }
                }
            }

            proceedOk();

        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtHMedicineName).show();
        }
    }

    @Override
    public void manageOk(T1 row) {
        GrdMedicineDetail_Mdl ourRow = (GrdMedicineDetail_Mdl) row;

        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHMedicineName.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHMedicineName.getText()));
        ourRow.setSkuName(DmShared.xNull(txtSKUTotalQty.getText()));
        ourRow.setSkuCode(DmShared.xNull(skuCode));
        ourRow.setTotalQuantity(DmShared.xNull(txtTotalQty.getText()));
        ourRow.setRate(DmClient.vNull(txtRate.getText(), "0.00"));
        ourRow.setSkuValue(DmClient.vNull(txtAmount.getText(), "0.00"));
        ourRow.setBatchNo(DmShared.xNull(txtHBatchNo.getText()));
        ourRow.setIbCode(DmShared.xNull(ibCode));
        ourRow.setIsBarcodeActive(DmShared.xNull(isBarcodeActive));
        ourRow.setBarcode(DmShared.xNull(txtBarcode.getText()));
        ourRow.setSkuQtyForRate(DmShared.xNull(txtSKUTotalQty.getTag()));
        ourRow.setMrp(DmClient.vNull(txtMRP.getText(), "0.00"));
        ourRow.setSaleRate(DmClient.vNull(txtSaleRate.getText(), "0.00"));
        ourRow.setManufacturingDate(DmShared.dNull(txtManufacturingDate.getText()));
        ourRow.setBestBeforeDays(DmShared.xNull(txtBestBefore.getText()));
        ourRow.setExpiryDate(DmShared.dNull(txtExpiryDate.getText()));
        ourRow.setRefNo(DmShared.xNull(txtRefNo.getText()));
        ourRow.setRefDate(DmShared.dNull(txtRefDate.getText()));
        ourRow.setRemark(DmShared.xNull(txtRemark.getText()));
        ourRow.setSkuDecimalPlaces(DmShared.xNull(txtSKUTotalQty.getTag2nd()));
        ourRow.setIsBestBefore(DmShared.xNull(isBestBefore));
        ourRow.setPurchaseUnitCode(DmShared.xNull(txtUnit.getTag2nd()));
        ourRow.setPurchaseUnitName(DmShared.xNull(txtUnit.getTag()));
        ourRow.setPurchaseUnitQuantity(DmShared.xNull(txtUnitQuantity.getText()));
        ourRow.setPurchaseUnitDecimalPlaces(DmShared.xNull(txtDisplayUnitQty.getTag()));
        ourRow.setSkuMrp(DmClient.vNull(skuMrp, DmShared.getNumberFormat(7)));
        ourRow.setSkuSaleRate(DmClient.vNull(skuSaleRate, DmShared.getNumberFormat(7)));
        ourRow.setSkuRate(skuRate);

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

    public MedicineOpeningGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Medicine Detail", "500px", "");
        srvAsync = new ServiceInvoker<MedicineOpening_SrvAsync>(GWT.create(MedicineOpening_Srv.class), MedicineOpening.class.getName());
        textHelpEvents();
        manageEvent();
    }

    private void manageEvent() {
        txtRefDate.addBlurHandler(this);
        txtManufacturingDate.addBlurHandler(this);
        txtSaleRate.addBlurHandler(this);
        txtRate.addBlurHandler(this);
        txtTotalQty.addBlurHandler(this);
        txtMRP.addBlurHandler(this);
        txtBestBefore.addBlurHandler(this);
        txtExpiryDate.addBlurHandler(this);
    }

    private void manageDisplay(boolean flag) {
        txtAmount.setEnabled(false);
        txtUnit.setReadOnly(true);
        txtDisplayUnitQty.setReadOnly(true);
        txtDisplayUnitSaleRate.setReadOnly(true);
        txtUnitQuantity.setEnabled(false);
        manageMedicineState(flag);

    }

    private void clear() {
        resetItem();
    }

    private void manageMedicineState(boolean flag) {
        txtExpiryDate.setEnabled(flag);
        txtManufacturingDate.setEnabled(flag);
        txtBestBefore.setEnabled(flag);

        if (flag) {
            if (DmShared.iNull(isBestBefore).intValue() == 1) {
                txtExpiryDate.setEnabled(false);
            } else {
                txtManufacturingDate.setEnabled(false);
                txtBestBefore.setEnabled(false);
            }
        }

        if (DmShared.iNull(isBarcodeActive).intValue() == 1) {
            if (!txtBarcode.isEnabled()) {
                txtBarcode.setEnabled(true);
            }
        } else {
            txtBarcode.setEnabled(false);
            if (medicineOpening.topControl.getMode().equals("Edit")) {
                if (!DmShared.xNull(txtBarcode.getText()).trim().equals("")) {
                    if (!txtBarcode.isEnabled()) {
                        txtBarcode.setEnabled(true);
                    }
                }
            }
        }
    }

    private void resetItem() {
        ibCode = "";
        isBestBefore = "";
        txtSKUTotalQty.setText("");
        txtSKUTotalQty.setTag("");
        txtSKUTotalQty.setTag2nd("");
        txtUnit.setText("");
        txtUnit.setTag("");
        txtUnit.setTag2nd("");
        txtDisplayUnitQty.setText("");
        txtDisplayUnitQty.setTag("");
        txtDisplayUnitQty.setTag2nd("");
        txtDisplayUnitSaleRate.setText("");
        txtDisplayUnitSaleRate.setTag("");
        txtDisplayUnitSaleRate.setTag2nd("");
        txtUnitQuantity.setText("");
        txtUnitQuantity.setTag("");
        txtUnitQuantity.setTag2nd("");
        skuMrp = "";
        skuSaleRate = "";
        txtHBatchNo.setTag("");
        txtHBatchNo.setText("");
        txtHBatchNo.setTag2nd("");

        resetBatchDetails();
    }

    private void resetBatchDetails() {
        txtBarcode.setText("");

        txtMRP.setText("");
        txtMRP.setTag("");
        txtMRP.setTag2nd("");

        txtSaleRate.setText("");
        txtSaleRate.setTag("");
        txtSaleRate.setTag2nd("");

        txtManufacturingDate.setText("");
        txtManufacturingDate.setTag("");
        txtManufacturingDate.setTag2nd("");

        txtBestBefore.setText("");
        txtBestBefore.setTag("");
        txtBestBefore.setTag2nd("");

        txtExpiryDate.setText("");
        txtExpiryDate.setTag("");
        txtExpiryDate.setTag2nd("");
    }

    private void calculate() {
        txtTotalQty.setText(DmClient.vNull(txtTotalQty.getText(), DmShared.getNumberFormat(DmShared.iNull(txtSKUTotalQty.getTag2nd()).intValue())));

        skuRate = DmClient.vNull(DmShared.vNull(txtRate.getText()).doubleValue() / DmShared.vNull(txtSKUTotalQty.getTag()).doubleValue(), DmShared.getNumberFormat(7));
        txtAmount.setText(DmClient.vNull(DmShared.vNull(txtTotalQty.getText()).doubleValue() * DmShared.vNull(skuRate).doubleValue(), "0.00"));
        txtUnitQuantity.setText(DmClient.vNull(DmShared.vNull(txtTotalQty.getText()).doubleValue() / DmShared.vNull(txtSKUTotalQty.getTag()).doubleValue(), DmShared.getNumberFormat(DmShared.iNull(txtDisplayUnitQty.getTag()).intValue())));
        txtSaleRate.setText(DmClient.vNull(txtSaleRate.getText(), "0.00"));

        skuSaleRate = DmClient.vNull(DmShared.vNull(txtSaleRate.getText()).doubleValue() / DmShared.vNull(txtSKUTotalQty.getTag()).doubleValue(), DmShared.getNumberFormat(7));
        skuMrp = DmClient.vNull(DmShared.vNull(txtMRP.getText()).doubleValue() / DmShared.vNull(txtSKUTotalQty.getTag()).doubleValue(), DmShared.getNumberFormat(7));

    }

}
