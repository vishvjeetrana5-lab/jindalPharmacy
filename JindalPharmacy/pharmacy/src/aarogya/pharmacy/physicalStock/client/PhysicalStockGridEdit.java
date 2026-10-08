package aarogya.pharmacy.physicalStock.client;

import aarogya.pharmacy.physicalStock.shared.*;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public class PhysicalStockGridEdit<T1, T2> extends PhysicalStockGridEdit_Dsg<T1, T2> implements BlurHandler {

    //<editor-fold defaultstate="collapsed" desc="CallBack">
    interface CallBack {

        public void onOk(GrdMedicineDetail_Mdl opdService);

        public void onDelete();
    }
    private CallBack callBack;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
    //</editor-fold>

    private ServiceInvoker<PhysicalStock_SrvAsync> srvAsync;
    private PhysicalStock physicalStock;
    private String isBestBefore = "", selectedIbCode = "", selectedMedicineCode = "", purchaseUnitConversionRate = "";
    private int hashCode = 0;

    public PhysicalStockGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Medicine Detail", "600px", "");
        srvAsync = new ServiceInvoker<PhysicalStock_SrvAsync>(GWT.create(PhysicalStock_Srv.class), PhysicalStock.class.getName());
        textHelpEvents();
        manageEvent();
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, PhysicalStock physicalStock) {
        this.physicalStock = physicalStock;
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtHMedicineName.setFocus(true);
            }
        });
    }

    private void textHelpEvents() {
        txtHMedicineName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHMedicineName_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                selectedMedicineCode = DmShared.xNull(txtHMedicineName.getTag());
                if (!DmShared.xNull(physicalStock.txtHDepartment.getTag()).equalsIgnoreCase("")) {
                    srvAsync.invokeRPC(blnCallBackWait).getTxtHMedicineName(physicalStock.txtHDepartment.getTag(), physicalStock.txtHFilterType.getTag(), physicalStock.txtHFilterValue.getTag(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHMedicineName_Mdl>>>() {

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
                if (!DmShared.xNull(selectedMedicineCode).equalsIgnoreCase(DmShared.xNull(txtHMedicineName.getTag()))) {
                    resetMedicine();
                }
                if (blnStatus && t1Row != null) {
                    txtHMedicineName.setTag2nd(DmShared.xNull(t1Row.getItemProperty()));
                    isBestBefore = DmShared.xNull(t1Row.getIsBestBefore());
                    txtSKU.setText(DmShared.xNull(t1Row.getSkuName()));
                    txtSKU.setTag(DmShared.xNull(t1Row.getSkuCode()));
                    txtSKU.setTag2nd(DmShared.xNull(t1Row.getSkuDecimalPlaces()));
                    purchaseUnitConversionRate = DmShared.xNull(t1Row.getPurchaseUnitConversionRate());
                    if (DmShared.iNull(t1Row.getPurchaseUnitCode()).longValue() != DmShared.iNull(t1Row.getSkuCode()).longValue()) {
                        txtSaleRateDisplay.setText(DmShared.xNull(t1Row.getPurchaseUnitName()) + " = " + DmShared.xNull(t1Row.getPurchaseUnitConversionRate()) + " " + DmShared.xNull(t1Row.getSkuName()));
                        txtMRPDisplay.setText(DmShared.xNull(t1Row.getPurchaseUnitName()) + " = " + DmShared.xNull(t1Row.getPurchaseUnitConversionRate()) + " " + DmShared.xNull(t1Row.getSkuName()));
                        txtRateDisplay.setText(DmShared.xNull(t1Row.getPurchaseUnitName()) + " = " + DmShared.xNull(t1Row.getPurchaseUnitConversionRate()) + " " + DmShared.xNull(t1Row.getSkuName()));
                    } else {
                        txtSaleRateDisplay.setText(DmShared.xNull(t1Row.getPurchaseUnitName()));
                        txtMRPDisplay.setText(DmShared.xNull(t1Row.getPurchaseUnitName()));
                        txtRateDisplay.setText(DmShared.xNull(t1Row.getPurchaseUnitName()));
                    }
                    txtSaleRateDisplay.setTag(DmShared.xNull(t1Row.getPurchaseUnitCode()));
                    txtSaleRateDisplay.setTag2nd(DmShared.xNull(t1Row.getPurchaseUnitName()));
                }
            }
        });

        txtHBatchNo.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHBatchNo_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                selectedIbCode = DmShared.xNull(txtHBatchNo.getTag());
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
                if (!DmShared.xNull(selectedIbCode).equalsIgnoreCase(DmShared.xNull(txtHBatchNo.getTag()))) {
                    resetBatchNo();
                    setRate(txtHMedicineName.getTag(), txtHBatchNo.getTag(), physicalStock.txtHDepartment.getTag(), physicalStock.txtVoucherDate.getText(), DmShared.xNull(purchaseUnitConversionRate));
                }
                if (blnStatus && t1Row != null) {
                    txtMRP.setText(DmShared.xNull(t1Row.getMrp()));
                    txtMRP.setTag(DmShared.xNull(t1Row.getSkuMrp()));
                    txtSaleRate.setText(DmShared.xNull(t1Row.getSaleRate()));
                    txtSaleRate.setTag(DmShared.xNull(t1Row.getSkuSaleRate()));
                    txtExpiryDate.setText(DmShared.dNull(t1Row.getExpireDate()));
                    txtExpiryDate.setTag(DmShared.dNull(t1Row.getManufacturingDate()));
                    txtBarcode.setText(DmShared.xNull(t1Row.getBarcode()));
                }
            }
        });
    }

    private void setRate(String itemCode, String ibCode, String departmentCode, String v_Date, final String purchaseUnitCoversionRate) {
        srvAsync.invokeRPC(true).getSkuLandedRate(itemCode, ibCode, departmentCode, v_Date, new AsyncCallback<DmResult<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHMedicineName).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    String skuLandedRate = result.getData();
                    double SkuRate = DmShared.vNull(skuLandedRate).doubleValue();
                    double rate = SkuRate * DmShared.vNull(purchaseUnitCoversionRate).doubleValue();
                    txtRate.setText(DmClient.vNull(rate, "0.00"));
                    txtRate.setTag2nd(DmClient.vNull(rate, "0.00"));
                    txtRate.setTag(DmClient.vNull(SkuRate, DmShared.getNumberFormat(7)));
                }
            }
        });
    }

    private void calculate() {
        if (DmShared.vNull(txtRate.getText()).doubleValue() != DmShared.vNull(txtRate.getTag2nd()).doubleValue()) {
            double SkuRate = DmShared.vNull(txtRate.getText()).doubleValue() / DmShared.vNull(purchaseUnitConversionRate).doubleValue();
            txtRate.setTag(DmClient.vNull(SkuRate, DmShared.getNumberFormat(7)));
            txtRate.setTag2nd(DmClient.vNull(txtRate.getText(), "0.00"));
        }
        txtRate.setText(DmClient.vNull(txtRate.getText(), "0.00"));
        txtRate.setTag(DmClient.vNull(txtRate.getTag(), DmShared.getNumberFormat(7)));
    }

    private void manageDisplay() {
        txtSKU.setEnabled(false);
        txtExpiryDate.setEnabled(false);
        txtMRP.setEnabled(false);
        txtSaleRate.setEnabled(false);
        txtBarcode.setEnabled(false);
    }

    //<editor-fold defaultstate="collapsed" desc="GridEdit Overrides">
    @Override
    public void onOk() {
        try {

            if (DmShared.xNull(txtHMedicineName.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Medicine Name is mandatory.", txtHMedicineName).show();
                return;
            }
//            if (DmShared.vNull(txtQuantity.getText()).doubleValue() == 0) {
//                new DmAlertMessageBox("Quantity is mandatory.", txtQuantity).show();
//                return;
//            }
            if (DmShared.iNull(txtHBatchNo.getTag()).longValue() == 0) {
                new DmAlertMessageBox("Batch No. is mandatory.", txtHBatchNo).show();
                return;
            }
            if (DmShared.vNull(txtMRP.getText()).doubleValue() < DmShared.vNull(txtRate.getText()).doubleValue()) {
                new DmAlertMessageBox("Rate/pack Cannot be greater than MRP.", txtRate).show();
                return;
            }

            if (!physicalStock.cbAllowDuplicate.getValue()) {
                for (GrdMedicineDetail_Mdl detail_Mdl : (List<GrdMedicineDetail_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
                    if (DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(txtHMedicineName.getTag()))
                            && DmShared.xNull(detail_Mdl.getIbCode()).equalsIgnoreCase(DmShared.xNull(txtHBatchNo.getTag()))
                            && DmShared.vNull(detail_Mdl.getRate()).doubleValue() == DmShared.vNull(txtRate.getText()).doubleValue()) {
                        if (!DmClient.vNull(detail_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                            String message = "<div style='float:right; margin-right: 5px;'>";
                            message += "Record already exist with following details - <br>";
                            message += (DmShared.xNull(txtHMedicineName.getText()).trim().equals("")) ? "" : "<b>Medicine:</b> " + DmShared.xNull(txtHMedicineName.getText()) + "<br>";
                            message += (DmShared.xNull(txtHBatchNo.getText()).trim().equals("")) ? "" : "<b>Batch No.:</b> " + DmShared.xNull(txtHBatchNo.getText()) + "<br>";
                            message += (DmShared.dNull(txtExpiryDate.getText()).trim().equals("")) ? "" : "<b>Expiry Date:</b> " + DmShared.dNull(txtExpiryDate.getText()) + "<br>";
                            message += (DmShared.vNull(txtMRP.getText()) == 0) ? "" : "<b>MRP:</b> " + DmClient.vNull(txtMRP.getText(), "0.00") + "<br>";
                            message += (DmShared.vNull(txtSaleRate.getText()) == 0) ? "" : "<b>Sale Rate:</b> " + DmClient.vNull(txtSaleRate.getText(), "0.00") + "<br>";
                            message += (DmShared.vNull(txtRate.getText()) == 0) ? "" : "<b>Rate/Pack:</b> " + DmClient.vNull(txtRate.getText(), "0.00") + "<br>";
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

        //Item
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHMedicineName.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHMedicineName.getText()));

        //Item Details
        ourRow.setPropertyList(DmShared.xNull(txtHMedicineName.getTag2nd()));
        ourRow.setIsBestBefore(DmShared.xNull(isBestBefore));

        //SKU
        ourRow.setSkuCode(DmShared.xNull(txtSKU.getTag()));
        ourRow.setSkuName(DmShared.xNull(txtSKU.getText()));
        ourRow.setSkuDecimalPlaces(DmShared.xNull(txtSKU.getTag2nd()));

        //RATE
        ourRow.setRate(DmClient.vNull(txtRate.getText(), "0.00"));
        ourRow.setSkuRate(DmShared.xNull(txtRate.getTag()));

        //Purchase Unit
        ourRow.setPurchaseUnitConverionRate(DmShared.xNull(purchaseUnitConversionRate));

        ourRow.setPurchaseUnitCode(DmShared.xNull(txtSaleRateDisplay.getTag()));
        ourRow.setPurchaseUnitName(DmShared.xNull(txtSaleRateDisplay.getTag2nd()));

        //Properties
        ourRow.setIbCode(DmShared.xNull(txtHBatchNo.getTag()));
        ourRow.setBatchNo(DmShared.xNull(txtHBatchNo.getText()));
        ourRow.setBarcode(DmShared.xNull(txtBarcode.getText()));
        ourRow.setManufacturingDate(DmShared.dNull(txtExpiryDate.getTag()));
        ourRow.setExpiryDate(DmShared.dNull(txtExpiryDate.getText()));
        ourRow.setMrp(DmClient.vNull(txtMRP.getText(), "0.00"));
        ourRow.setSkuMrp(DmClient.vNull(txtMRP.getTag(), "0.0000000"));
        ourRow.setSaleRate(DmClient.vNull(txtSaleRate.getText(), "0.00"));
        ourRow.setSkuSaleRate(DmClient.vNull(txtSaleRate.getTag(), "0.0000000"));

        //Quantities
        ourRow.setQuantity(DmClient.vNull(txtQuantity.getText(), DmShared.getNumberFormat(DmShared.iNull(txtSKU.getTag2nd()).intValue())));

        callBack.onOk(ourRow);
    }

    @Override
    public void displayRow(T1 row) {
        GrdMedicineDetail_Mdl ourRow = (GrdMedicineDetail_Mdl) row;

        //Clear
        resetPopup();

        //Item
        txtHMedicineName.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHMedicineName.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));

        //Item Details
        txtHMedicineName.setTag2nd(DmShared.xNull(ourRow.getPropertyList()));
        isBestBefore = DmShared.xNull(ourRow.getIsBestBefore());

        //SKU
        txtSKU.setText(DmShared.xNull(ourRow.getSkuName()));
        txtSKU.setTag(DmShared.xNull(ourRow.getSkuCode()));
        txtSKU.setTag2nd(DmShared.xNull(ourRow.getSkuDecimalPlaces()));

        //Rate
        txtRate.setText(DmShared.xNull(ourRow.getRate()));
        txtRate.setTag2nd(DmShared.xNull(ourRow.getRate()));
        txtRate.setTag(DmShared.xNull(ourRow.getSkuRate()));

        //Purchase Unit
        purchaseUnitConversionRate = DmShared.xNull(ourRow.getPurchaseUnitConverionRate());

        if (DmShared.iNull(ourRow.getPurchaseUnitCode()).longValue() != DmShared.iNull(ourRow.getSkuCode()).longValue()) {
            txtSaleRateDisplay.setText(DmShared.xNull(ourRow.getPurchaseUnitName()) + " = " + DmShared.xNull(ourRow.getPurchaseUnitConverionRate()) + " " + DmShared.xNull(ourRow.getSkuName()));
            txtMRPDisplay.setText(DmShared.xNull(ourRow.getPurchaseUnitName()) + " = " + DmShared.xNull(ourRow.getPurchaseUnitConverionRate()) + " " + DmShared.xNull(ourRow.getSkuName()));
            txtRateDisplay.setText(DmShared.xNull(ourRow.getPurchaseUnitName()) + " = " + DmShared.xNull(ourRow.getPurchaseUnitConverionRate()) + " " + DmShared.xNull(ourRow.getSkuName()));
        } else {
            txtSaleRateDisplay.setText(DmShared.xNull(ourRow.getPurchaseUnitName()));
            txtMRPDisplay.setText(DmShared.xNull(ourRow.getPurchaseUnitName()));
            txtRateDisplay.setText(DmShared.xNull(ourRow.getPurchaseUnitName()));
        }
        txtSaleRateDisplay.setTag(DmShared.xNull(ourRow.getPurchaseUnitCode()));
        txtSaleRateDisplay.setTag2nd(DmShared.xNull(ourRow.getPurchaseUnitName()));

        //Properties
        txtHBatchNo.setText(ourRow.getBatchNo());
        txtHBatchNo.setTag(ourRow.getIbCode());
        txtBarcode.setText(DmShared.xNull(ourRow.getBarcode()));
        txtExpiryDate.setTag(ourRow.getManufacturingDate());
        txtExpiryDate.setText(ourRow.getExpiryDate());
        txtMRP.setText(ourRow.getMrp());
        txtMRP.setTag(ourRow.getSkuMrp());
        txtSaleRate.setText(ourRow.getSaleRate());
        txtSaleRate.setTag(ourRow.getSkuSaleRate());

        //Quantities
        txtQuantity.setText(DmShared.xNull(ourRow.getQuantity()));
        txtQuantity.setTag(DmShared.xNull(ourRow.getQuantity()));

        //Other
        hashCode = ourRow.getSharedKeyFieldHashCode();

        manageDisplay();
        txtHMedicineName.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdMedicineDetail_Mdl();
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
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Events">
    private void manageEvent() {
        txtQuantity.addBlurHandler(this);
        txtRate.addBlurHandler(this);
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtQuantity) {
            txtQuantity.setText(DmClient.vNull(txtQuantity.getText(), DmShared.getNumberFormat(DmShared.iNull(txtSKU.getTag2nd()).intValue())));
        } else if (event.getSource() == txtRate) {
            calculate();
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Clear">
    private void resetPopup() {
        hashCode = 0;

        txtHMedicineName.setText("");
        txtHMedicineName.setTag("");
        txtHMedicineName.setTag2nd("");
        resetMedicine();

        txtQuantity.setText("");
        txtQuantity.setTag("");
        txtQuantity.setTag2nd("");
    }

    private void resetMedicine() {
        isBestBefore = "";
        selectedMedicineCode = "";

        txtSKU.setText("");
        txtSKU.setTag("");
        txtSKU.setTag2nd("");
        purchaseUnitConversionRate = "";

        txtHBatchNo.setText("");
        txtHBatchNo.setTag("");
        txtHBatchNo.setTag2nd("");
        resetBatchNo();
    }

    private void resetBatchNo() {
        txtExpiryDate.setText("");
        txtExpiryDate.setTag("");
        txtExpiryDate.setTag2nd("");

        txtRate.setText("");
        txtRate.setTag("");
        txtRate.setTag2nd("");

        txtMRP.setText("");
        txtMRP.setTag("");
        txtMRP.setTag2nd("");

        txtSaleRate.setText("");
        txtSaleRate.setTag("");
        txtSaleRate.setTag2nd("");
    }
    //</editor-fold>

}
