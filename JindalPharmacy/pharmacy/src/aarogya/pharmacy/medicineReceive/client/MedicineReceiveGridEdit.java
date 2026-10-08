package aarogya.pharmacy.medicineReceive.client;

import aarogya.pharmacy.batchNo.client.BatchNo;
import aarogya.pharmacy.batchNo.shared.BatchNo_Mdl;
import aarogya.pharmacy.medicineReceive.shared.MedicineReceive_Srv;
import aarogya.pharmacy.medicineReceive.shared.MedicineReceive_SrvAsync;
import aarogya.pharmacy.medicineReceive.shared.GrdMedicineDetail_Mdl;
import aarogya.pharmacy.medicineReceive.shared.TxtHMedicineName_Mdl;
import aarogya.pharmacy.medicineSearch.client.MedicineSearch;
import aarogya.pharmacy.medicineSearch.shared.MedicineSearchFilter_Mdl;
import aarogya.pharmacy.medicineSearch.shared.MedicineSearch_Mdl;
import aarogya.pharmacy.self.shared.BarCode_Mdl;
import aarogya.selfToolkit.stStatic.shared.STEnum.*;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.List;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;

public class MedicineReceiveGridEdit<T1, T2> extends MedicineReceiveGridEdit_Dsg<T1, T2> implements BlurHandler, KeyUpHandler, KeyDownHandler, AttachEvent.Handler {

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_SPACE) {
            if (event.getSource() == txtBatchNo) {
                showBatchDetailPopup();
            }
        }
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            if (event.getSource() == txtBatchNo) {
                if (DmShared.iNull(txtBatchNo.getTag()).longValue() == 0) {
                    showBatchDetailPopup();
                }
            }
        }
    }

    interface CallBack {

        public void onOk(GrdMedicineDetail_Mdl opdService);

        public void onDelete();
    }

    private ServiceInvoker<MedicineReceive_SrvAsync> srvAsync;
    private CallBack callBack;
    private MedicineReceive medicineReceive;
    private final MedicineSearch medicineSearch = new MedicineSearch(null, null);
    BatchNo batchDetail = new BatchNo();
    String selectedItemCode = "", selectedIbCode = "", skuCode = "", skuSaleRate = "", skuRate = "", skuMrp = "", selectedBarCode = "";
    public int hashCode = 0;
    private String itemCodeCsv = "";

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void textHelpEvents() {

        txtMedicineName.setReadOnly(true);
        txtMedicineName.addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_DELETE || event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE) {
                    txtMedicineName.setTag("");
                    txtMedicineName.setText("");
                    onMedSelection(true, new TxtHMedicineName_Mdl(), false);
                } else if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER || event.getNativeKeyCode() == KeyCodes.KEY_INSERT
                        || event.getNativeKeyCode() == KeyCodes.KEY_RIGHT || event.getNativeKeyCode() == KeyCodes.KEY_LEFT
                        || event.getNativeKeyCode() == KeyCodes.KEY_UP || event.getNativeKeyCode() == KeyCodes.KEY_DOWN
                        || event.getNativeKeyCode() == KeyCodes.KEY_ALT || event.getNativeKeyCode() == KeyCodes.KEY_CTRL
                        || event.getNativeKeyCode() == KeyCodes.KEY_SHIFT || event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE
                        || event.isAltKeyDown() || event.isControlKeyDown() || event.isShiftKeyDown()) {

                } else {
                    showMedicineSearchPopUp(String.valueOf((char) event.getNativeKeyCode()));
                }
            }
        });

        medicineSearch.addCallBack(new MedicineSearch.CallBack() {
            @Override
            public void ok(MedicineSearch_Mdl medSearchMdl) {
                setMedSelection(medSearchMdl, true);
            }

            @Override
            public void close() {
                txtMedicineName.setFocus(true);
            }
        });

        batchDetail.addCallBack(new BatchNo.CallBack() {
            @Override
            public void ok(BatchNo_Mdl batchDetail, List<BatchNo_Mdl> lstBatchDetails) {
                if (batchDetail != null) {
                    if (!DmShared.xNull(selectedIbCode).equalsIgnoreCase(DmShared.xNull(batchDetail.getIbCode()))) {
                        txtBarCode.setText("");
                        resetBatchDetail();
                        setRate(txtMedicineName.getTag(), batchDetail.getIbCode(), medicineReceive.txtHDepartment.getTag(), medicineReceive.txtVoucherDate.getText(), DmShared.xNull(txtSKU.getTag()));
                    }
                    fillBatchDetail(batchDetail);
                } else {
                    txtBarCode.setText("");
                    resetBatchDetail();
                }
            }

            @Override
            public void close(boolean isErrorClose) {
                if (!isErrorClose) {
                    txtBatchNo.setFocus(true);
                }
            }
        });
    }

    private void setMedSelection(MedicineSearch_Mdl medSearchMdl, boolean showBatchPopup) {
        TxtHMedicineName_Mdl medMdl = new TxtHMedicineName_Mdl();
        medMdl.setSharedKeyFieldCode(medSearchMdl.getSharedKeyFieldCode());
        medMdl.setSharedKeyFieldName(medSearchMdl.getSharedKeyFieldName());
        medMdl.setManualCode(medSearchMdl.getManualCode());
        medMdl.setItemProperty(medSearchMdl.getPropertyList());
        medMdl.setIsBestBefore(medSearchMdl.getIsBestBefore());
        medMdl.setSkuCode(medSearchMdl.getSkuCode());
        medMdl.setSkuName(medSearchMdl.getSkuName());
        medMdl.setSkuDecimalPlaces(medSearchMdl.getSkuDecimalPlaces());
        medMdl.setPurchaseUnitCode(medSearchMdl.getPurchaseUnitCode());
        medMdl.setPurchaseUnitName(medSearchMdl.getPurchaseUnitName());
        medMdl.setPurchaseUnitDecimalPlaces(medSearchMdl.getPurchaseUnitDecimalPlaces());
        medMdl.setPurchaseUnitConversionRate(medSearchMdl.getPurchaseUnitConversionRate());
        medMdl.setStock(medSearchMdl.getCurrentStock());

        txtMedicineName.setTag(DmShared.xNull(medMdl.getSharedKeyFieldCode()));
        txtMedicineName.setText(DmShared.xNull(medMdl.getSharedKeyFieldName()));
        onMedSelection(true, medMdl, showBatchPopup);
        txtMedicineName.setFocus(true);

    }

    private void onMedSelection(boolean blnStatus, TxtHMedicineName_Mdl t1Row, boolean showBatchPopup) {
        if (!selectedItemCode.equalsIgnoreCase(DmShared.xNull(txtMedicineName.getTag()))) {
            resetMedicineDetail();
            if (showBatchPopup) {
                showBatchDetailPopup();
            }
        }

        if (blnStatus && t1Row != null) {
            selectedItemCode = DmShared.xNull(txtMedicineName.getTag());
            skuCode = DmShared.xNull(t1Row.getSkuCode());
            txtSKU.setText(DmShared.xNull(t1Row.getSkuName()));
            txtSKU.setTag(DmShared.xNull(t1Row.getPurchaseUnitConversionRate()));
            txtSKU.setTag2nd(DmShared.xNull(t1Row.getSkuDecimalPlaces()));
            txtTotalPacks.setTag(t1Row.getPurchaseUnitDecimalPlaces());
            if (DmShared.iNull(t1Row.getPurchaseUnitCode()).longValue() != DmShared.iNull(t1Row.getSkuCode()).longValue()) {
                txtSaleRatePackSize.setText(DmShared.xNull(t1Row.getPurchaseUnitName()) + " = " + DmShared.xNull(t1Row.getPurchaseUnitConversionRate()) + " " + DmShared.xNull(t1Row.getSkuName()));
                txtTotalPackSizeDisplay.setText(DmShared.xNull(t1Row.getPurchaseUnitName()) + " = " + DmShared.xNull(t1Row.getPurchaseUnitConversionRate()) + " " + DmShared.xNull(t1Row.getSkuName()));
                txtRatePackSize.setText(DmShared.xNull(t1Row.getPurchaseUnitName()) + " = " + DmShared.xNull(t1Row.getPurchaseUnitConversionRate()) + " " + DmShared.xNull(t1Row.getSkuName()));
            } else {
                txtSaleRatePackSize.setText(DmShared.xNull(t1Row.getPurchaseUnitName()));
                txtTotalPackSizeDisplay.setText(DmShared.xNull(t1Row.getPurchaseUnitName()));
                txtRatePackSize.setText(DmShared.xNull(t1Row.getPurchaseUnitName()));
            }
            txtSaleRatePackSize.setTag(DmShared.xNull(t1Row.getPurchaseUnitName()));
            txtSaleRatePackSize.setTag2nd(DmShared.xNull(t1Row.getPurchaseUnitCode()));
            txtTotalPackSizeDisplay.setTag(DmShared.xNull(t1Row.getPurchaseUnitName()));
            txtTotalPackSizeDisplay.setTag2nd(DmShared.xNull(t1Row.getPurchaseUnitCode()));
            txtRatePackSize.setTag(DmShared.xNull(t1Row.getPurchaseUnitName()));
            txtRatePackSize.setTag2nd(DmShared.xNull(t1Row.getPurchaseUnitCode()));
        }
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, MedicineReceive medicineReceive) {
        this.medicineReceive = medicineReceive;
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);

    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtQuantity || event.getSource() == txtRate || event.getSource() == txtAmount) {
            calculate();
        } else if (event.getSource() == txtBarCode) {
            if (!selectedBarCode.equalsIgnoreCase(DmShared.xNull(txtBarCode.getText())) || DmShared.iNull(txtMedicineName.getTag()).longValue() == 0) {
                txtMedicineName.setTag("");
                txtMedicineName.setText("");
                resetMedicineDetail();
                if (!DmShared.xNull(txtBarCode.getText()).trim().equals("") && !medicineReceive.topControl.getMode().equalsIgnoreCase("Browse")) {
                    barCodeSearch();
                } else {
                    itemCodeCsv = "";
                }
            }
        }
    }

    @Override
    public void displayRow(T1 row) {
        GrdMedicineDetail_Mdl ourRow = (GrdMedicineDetail_Mdl) row;

        resetMedicineDetail();

        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtMedicineName.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtMedicineName.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        selectedItemCode = DmShared.xNull(ourRow.getSharedKeyFieldCode());
        txtRemark.setText(DmShared.xNull(ourRow.getRemark()));
        selectedIbCode = DmShared.xNull(ourRow.getIbCode());
        txtBatchNo.setTag(DmShared.xNull(ourRow.getIbCode()));
        txtBatchNo.setText(DmShared.xNull(ourRow.getBatchNo()));
        txtExpiryDate.setText(DmShared.dNull(ourRow.getExpiryDate()));
        txtMAnufacturingDate.setText(DmShared.dNull(ourRow.getManufacturingDate()));
        txtBestBeforeDays.setText(DmShared.xNull(ourRow.getBestBeforeDays()));
        txtMRP.setText(DmClient.vNull(ourRow.getMrp(), "0.00"));
        txtSaleRate.setText(DmClient.vNull(ourRow.getSaleRate(), "0.00"));
        txtQuantity.setText(DmShared.xNull(ourRow.getQuantity()));
        txtAmount.setText(DmClient.vNull(ourRow.getAmount(), "0.00"));
        txtTotalPacks.setText(DmShared.xNull(ourRow.getTotalpacks()));
        txtTotalPacks.setTag(DmShared.xNull(ourRow.getPurchaseUnitDecimalPlaces()));
        txtRate.setText(DmClient.vNull(ourRow.getRate(), "0.00"));
        txtRate.setTag2nd(DmClient.vNull(ourRow.getRate(), "0.00"));
        txtSKU.setText(DmShared.xNull(ourRow.getSkuName()));
        txtSKU.setTag(DmShared.xNull(ourRow.getSkuQtyForRate()));
        txtSKU.setTag2nd(DmShared.xNull(ourRow.getSkuDecimalPlaces()));

        skuCode = DmShared.xNull(ourRow.getSkuCode());
        skuSaleRate = DmShared.xNull(ourRow.getSkuSaleRate());
        skuMrp = DmShared.xNull(ourRow.getSkuMrp());
        skuRate = DmShared.xNull(ourRow.getSkuRate());

        if (DmShared.iNull(ourRow.getPurchaseUnitCode()).longValue() != DmShared.iNull(ourRow.getSkuCode()).longValue()) {
            txtSaleRatePackSize.setText(DmShared.xNull(ourRow.getPurchaseUnitName()) + " = " + DmShared.xNull(ourRow.getSkuQtyForRate()) + " " + DmShared.xNull(ourRow.getSkuName()));
            txtTotalPackSizeDisplay.setText(DmShared.xNull(ourRow.getPurchaseUnitName()) + " = " + DmShared.xNull(ourRow.getSkuQtyForRate()) + " " + DmShared.xNull(ourRow.getSkuName()));
            txtRatePackSize.setText(DmShared.xNull(ourRow.getPurchaseUnitName()) + " = " + DmShared.xNull(ourRow.getSkuQtyForRate()) + " " + DmShared.xNull(ourRow.getSkuName()));
        } else {
            txtSaleRatePackSize.setText(DmShared.xNull(ourRow.getPurchaseUnitName()));
            txtTotalPackSizeDisplay.setText(DmShared.xNull(ourRow.getPurchaseUnitName()));
            txtRatePackSize.setText(DmShared.xNull(ourRow.getPurchaseUnitName()));
        }
        txtTotalPackSizeDisplay.setTag(DmShared.xNull(ourRow.getPurchaseUnitName()));
        txtTotalPackSizeDisplay.setTag2nd(DmShared.xNull(ourRow.getPurchaseUnitCode()));
        txtSaleRatePackSize.setTag(DmShared.xNull(ourRow.getPurchaseUnitName()));
        txtSaleRatePackSize.setTag2nd(DmShared.xNull(ourRow.getPurchaseUnitCode()));
        txtRatePackSize.setTag(DmShared.xNull(ourRow.getPurchaseUnitName()));
        txtRatePackSize.setTag2nd(DmShared.xNull(ourRow.getPurchaseUnitCode()));

        txtBarCode.setText(DmShared.xNull(ourRow.getBarCode()));
        selectedBarCode = DmShared.xNull(ourRow.getBarCode());
        itemCodeCsv = DmShared.xNull(ourRow.getItemCodeCsv());

        manageDisplay();

        if (DmShared.iNull(medicineReceive.defaultValue.getPharmacyBarcodeActive()).intValue() == 1) {
            txtBarCode.setFocus(true);
        } else {
            txtMedicineName.setFocus(true);
        }
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdMedicineDetail_Mdl();
    }

    @Override
    public void onOk() {
        try {
            calculate();
            if (DmShared.xNull(txtMedicineName.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Medicine Name is mandatory.", txtMedicineName).show();
                return;
            }
            if (DmShared.vNull(txtQuantity.getText()).doubleValue() == 0) {
                new DmAlertMessageBox("Quantity is mandatory.", txtQuantity).show();
                return;
            }
            if (DmShared.vNull(txtBatchNo.getTag()).longValue() == 0) {
                new DmAlertMessageBox("Batch No. is mandatory.", txtBatchNo).show();
                return;
            }

            for (GrdMedicineDetail_Mdl detail_Mdl : (List<GrdMedicineDetail_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
                if (!DmClient.vNull(detail_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                    if (DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(txtMedicineName.getTag()))
                            && DmShared.xNull(detail_Mdl.getIbCode()).equalsIgnoreCase(DmShared.xNull(txtBatchNo.getTag()))) {
                        String message = "<div style='float:right; margin-right: 5px;'>";
                        message += "Record already exist with following details - <br>";
                        message += (DmShared.xNull(txtMedicineName.getText()).trim().equals("")) ? "" : "<b>Medicine:</b> " + DmShared.xNull(txtMedicineName.getText()) + "<br>";
                        message += (DmShared.xNull(txtBatchNo.getText()).trim().equals("")) ? "" : "<b>Batch No.:</b> " + DmShared.xNull(txtBatchNo.getText()) + "<br>";
                        message += "</div>";
                        new DmAlertMessageBox(message, txtMedicineName).show();
                        return;
                    }
                }
            }
            proceedOk();

        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtMedicineName).show();
        }
    }

    @Override
    public void manageOk(T1 row) {
        GrdMedicineDetail_Mdl ourRow = (GrdMedicineDetail_Mdl) row;

        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtMedicineName.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtMedicineName.getText()));

        ourRow.setItemCodeCsv(DmShared.xNull(itemCodeCsv));
        ourRow.setRemark(DmShared.xNull(txtRemark.getText()));
        ourRow.setIbCode(DmShared.xNull(txtBatchNo.getTag()));
        ourRow.setBatchNo(DmShared.xNull(txtBatchNo.getText()));
        ourRow.setBarCode(DmShared.xNull(txtBarCode.getText()));
        ourRow.setExpiryDate(DmShared.dNull(txtExpiryDate.getText()));
        ourRow.setMrp(DmClient.vNull(txtMRP.getText(), "0.00"));
        ourRow.setSaleRate(DmClient.vNull(txtSaleRate.getText(), "0.00"));
        ourRow.setManufacturingDate(DmShared.dNull(txtMAnufacturingDate.getText()));
        ourRow.setBestBeforeDays(DmShared.xNull(txtBestBeforeDays.getText()));
        ourRow.setAmount(DmClient.vNull(txtAmount.getText(), "0.00"));
        ourRow.setRate(DmClient.vNull(txtRate.getText(), "0.00"));
        ourRow.setTotalpacks(DmShared.xNull(txtTotalPacks.getText()));
        ourRow.setPurchaseUnitDecimalPlaces(DmShared.xNull(txtTotalPacks.getTag()));
        ourRow.setQuantity(DmShared.xNull(txtQuantity.getText()));
        ourRow.setSkuName(DmShared.xNull(txtSKU.getText()));
        ourRow.setSkuDecimalPlaces(DmShared.xNull(txtSKU.getTag2nd()));
        ourRow.setSkuQtyForRate(DmShared.xNull(txtSKU.getTag()));

        ourRow.setSkuCode(DmShared.xNull(skuCode));
        ourRow.setPurchaseUnitCode(DmShared.xNull(txtSaleRatePackSize.getTag2nd()));
        ourRow.setPurchaseUnitName(DmShared.xNull(txtSaleRatePackSize.getTag()));
        ourRow.setSkuSaleRate(DmClient.vNull(skuSaleRate, DmShared.getNumberFormat(7)));
        ourRow.setSkuMrp(DmClient.vNull(skuMrp, DmShared.getNumberFormat(7)));
        ourRow.setSkuRate(DmClient.vNull(skuRate, DmShared.getNumberFormat(7)));

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

    public MedicineReceiveGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Medicine Detail", "500px", "");
        srvAsync = new ServiceInvoker<MedicineReceive_SrvAsync>(GWT.create(MedicineReceive_Srv.class), MedicineReceive.class.getName());
        textHelpEvents();
        manageEvent();
    }

    private void manageEvent() {
        txtQuantity.addBlurHandler(this);
        txtRate.addBlurHandler(this);
        txtAmount.addBlurHandler(this);
        txtBarCode.addBlurHandler(this);
        txtBatchNo.addKeyUpHandler(this);
        txtBatchNo.addKeyDownHandler(this);

        btnBatchNo.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                showBatchDetailPopup();
            }
        });
    }

    private void manageDisplay() {
        txtBestBeforeDays.setEnabled(false);
        txtExpiryDate.setEnabled(false);
        txtMAnufacturingDate.setEnabled(false);
        txtMRP.setEnabled(false);
        txtSaleRate.setEnabled(false);
        txtSaleRatePackSize.setEnabled(false);
        txtBatchNo.setReadOnly(true);
        txtSKU.setEnabled(false);
        txtTotalPacks.setEnabled(false);
        txtAmount.setEnabled(false);
        txtBarCode.setEnabled(DmShared.iNull(medicineReceive.defaultValue.getPharmacyBarcodeActive()).intValue() == 1);
    }

    private void fillBatchDetail(BatchNo_Mdl batchMdl) {
        selectedIbCode = DmShared.xNull(batchMdl.getIbCode());
        txtBatchNo.setTag(DmShared.xNull(batchMdl.getIbCode()));
        txtBatchNo.setText(DmShared.xNull(batchMdl.getBatchNo()));
        txtBarCode.setText(DmShared.xNull(batchMdl.getBarCode()));
        txtBestBeforeDays.setText(DmShared.xNull(batchMdl.getBestBeforeDays()));
        txtExpiryDate.setText(DmShared.dNull(batchMdl.getExpiryDate()));
        txtMAnufacturingDate.setText(DmShared.dNull(batchMdl.getManufacturingDate()));
        txtMRP.setText(DmClient.vNull(batchMdl.getMrp(), "0.00"));
        txtSaleRate.setText(DmClient.vNull(batchMdl.getSaleRate(), "0.00"));
        txtBatchNo.setFocus(true);
    }

    private void resetBatchDetail() {
        txtBatchNo.setTag("");
        txtBatchNo.setText("");
        txtBestBeforeDays.setText("");
        txtExpiryDate.setText("");
        txtMAnufacturingDate.setText("");
        txtMRP.setText("");
        txtSaleRate.setText("");
        skuMrp = "";
        skuSaleRate = "";
        skuRate = "";
        txtRate.setText("");
        txtRate.setTag("");
        txtRate.setTag2nd("");
        txtAmount.setText("");
        txtAmount.setTag("");
        txtAmount.setTag2nd("");

    }

    private void resetMedicineDetail() {
        txtSKU.setText("");
        txtSKU.setTag("");
        txtSKU.setTag2nd("");

        txtTotalPacks.setText("");
        txtTotalPacks.setTag("");
        txtTotalPacks.setTag2nd("");

        txtTotalPackSizeDisplay.setText("");
        txtTotalPackSizeDisplay.setTag("");
        txtTotalPackSizeDisplay.setTag2nd("");

        txtSaleRatePackSize.setText("");
        txtSaleRatePackSize.setTag("");
        txtSaleRatePackSize.setTag2nd("");

        txtRatePackSize.setText("");
        txtRatePackSize.setTag("");
        txtRatePackSize.setTag2nd("");

        resetBatchDetail();
    }

    private void showBatchDetailPopup() {
        if (!medicineReceive.topControl.getMode().equalsIgnoreCase("Browse")) {
            if (DmShared.iNull(txtMedicineName.getTag()).longValue() != 0) {
//                batchDetail.show(txtMedicineName.getTag(), medicineReceive.txtHDepartment.getTag(), medicineReceive.txtVoucherDate.getText(), "", false, true, DmShared.xNull(txtBarCode.getText()));
                batchDetail.show(txtMedicineName.getTag(), medicineReceive.txtHDepartment.getTag(), medicineReceive.txtVoucherDate.getText(), "", ItemBatchType.NON_EXPIRED_BATCH, true, DmShared.xNull(txtBarCode.getText()), null, "", false);
            } else {
                new DmAlertMessageBox("Please select Medicine Name.", txtMedicineName).show();
            }
        }
    }

    private void setRate(String itemCode, String ibCode, String departmentCode, String v_Date, final String purchaseUnitCoversionRate) {
        srvAsync.invokeRPC(true).getSkuLandedRate(itemCode, ibCode, departmentCode, v_Date, new AsyncCallback<DmResult<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtMedicineName).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    String skuLandedRate = result.getData();
                    double tempSkuRate = DmShared.vNull(skuLandedRate).doubleValue();
                    double tempRate = tempSkuRate * DmShared.vNull(purchaseUnitCoversionRate).doubleValue();
                    txtRate.setText(DmClient.vNull(tempRate, "0.00"));
                    txtRate.setTag2nd(DmClient.vNull(tempRate, "0.00"));
                    skuRate = DmClient.vNull(tempSkuRate, DmShared.getNumberFormat(7));
                    calculate();
                }
            }
        });
    }

    private void calculate() {
        if (DmShared.vNull(txtRate.getText()).doubleValue() != DmShared.vNull(txtRate.getTag2nd()).doubleValue()) {
            double tempSkuRate = DmShared.vNull(txtRate.getText()).doubleValue() / DmShared.vNull(txtSKU.getTag()).doubleValue();
            skuRate = DmClient.vNull(tempSkuRate, DmShared.getNumberFormat(7));
            txtRate.setTag2nd(DmClient.vNull(txtRate.getText(), "0.00"));
        }

        txtRate.setText(DmClient.vNull(txtRate.getText(), "0.00"));
        skuRate = DmClient.vNull(skuRate, DmShared.getNumberFormat(7));
        skuSaleRate = DmClient.vNull(DmShared.vNull(txtSaleRate.getText()).doubleValue() / DmShared.vNull(txtSKU.getTag()).doubleValue(), DmShared.getNumberFormat(7));
        skuMrp = DmClient.vNull(DmShared.vNull(txtMRP.getText()).doubleValue() / DmShared.vNull(txtSKU.getTag()).doubleValue(), DmShared.getNumberFormat(7));

        txtQuantity.setText(DmClient.vNull(txtQuantity.getText(), DmShared.getNumberFormat(DmShared.iNull(txtSKU.getTag2nd()).intValue())));
        txtTotalPacks.setText(DmClient.vNull(DmShared.vNull(txtQuantity.getText()).doubleValue() / DmShared.vNull(txtSKU.getTag()).doubleValue(), DmShared.getNumberFormat(DmShared.iNull(txtTotalPacks.getTag()).intValue())));
        txtAmount.setText(DmClient.vNull(DmShared.vNull(skuRate).doubleValue() * DmShared.vNull(txtQuantity.getText()).doubleValue(), "0.00"));
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            batchDetail.hide();
            medicineSearch.hide();
        }
    }

    private void barCodeSearch() {
        srvAsync.invokeRPC().getBarCodeSearch(DmShared.xNull(txtBarCode.getText()), DmShared.dNull(medicineReceive.txtVoucherDate.getText()), DmShared.iNull(medicineReceive.txtHDepartment.getTag()).toString(), DmShared.iNull(medicineReceive.defaultValue.getSiteCode()).toString(), new AsyncCallback<DmResult<BarCode_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<BarCode_Mdl> result) {
                if (result.isStatus()) {
                    BarCode_Mdl data = result.getData();
                    itemCodeCsv = data.getItemCsv();
                    if (data.getMedicineDetail() != null) {
                        setMedSelection(data.getMedicineDetail(), false);

                        if (data.getBatchDetails() != null) {
                            fillBatchDetail(data.getBatchDetails());
                        } else {
                            showBatchDetailPopup();
                        }
                    } else if (!DmShared.xNull(data.getItemCsv()).trim().equals("")) {
                        showMedicineSearchPopUp("");

                    } else {
                        txtBarCode.setText("");
                        itemCodeCsv = "";
                    }
                    selectedBarCode = DmShared.xNull(txtBarCode.getText());

                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtMedicineName).show();
                }
            }
        });
    }

    private void showMedicineSearchPopUp(String searchChar) {
        MedicineSearchFilter_Mdl filter = new MedicineSearchFilter_Mdl();
        filter.setMedicineCode(DmShared.xNull(txtMedicineName.getTag()));
        filter.setDepartmentCode(DmShared.xNull(medicineReceive.txtHDepartment.getTag()));
        filter.setDepartmentName(DmShared.xNull(medicineReceive.txtHDepartment.getText()));
        filter.setV_Date(DmShared.dNull(medicineReceive.txtVoucherDate.getText()));
        filter.setItemCodeCsv(DmShared.xNull(itemCodeCsv));
        medicineSearch.show(filter, searchChar, medicineReceive.txtHVoucherType.getTag());
    }

}
