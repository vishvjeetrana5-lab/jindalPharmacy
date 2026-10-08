package aarogya.pharmacy.medicineTransferWholeSale.client;

import aarogya.pharmacy.addBatchSingle.client.AddBatchSingle;
import aarogya.pharmacy.batchNo.client.BatchNo;
import aarogya.pharmacy.batchNo.shared.BatchNo_Mdl;
import aarogya.pharmacy.medicineSearch.client.MedicineSearch;
import aarogya.pharmacy.medicineSearch.shared.MedicineSearchFilter_Mdl;
import aarogya.pharmacy.medicineSearch.shared.MedicineSearch_Mdl;
import aarogya.pharmacy.medicineTransferWholeSale.shared.MedicineTransferWholeSale_Srv;
import aarogya.pharmacy.medicineTransferWholeSale.shared.MedicineTransferWholeSale_SrvAsync;
import aarogya.pharmacy.medicineTransferWholeSale.shared.GrdMedicineDetail_Mdl;
import aarogya.pharmacy.medicineTransferWholeSale.shared.TxtHMedicineName_Mdl;
import aarogya.pharmacy.self.shared.BarCode_Mdl;
import aarogya.selfToolkit.stStatic.shared.STEnum.*;
import aarogya.selfToolkit.toolTip.client.ToolTip;
import aarogya.selfToolkit.toolTip.shared.ToolTip_Mdl;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.List;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;

public class MedicineTransferGridEdit<T1, T2> extends MedicineTransferGridEdit_Dsg<T1, T2> implements BlurHandler, KeyUpHandler, KeyDownHandler, ChangeHandler {

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

    private ServiceInvoker<MedicineTransferWholeSale_SrvAsync> srvAsync;
    private CallBack callBack;
    private MedicineTransferWholeSale medicineTransferWholeSale;
    private TxtHMedicineName_Mdl itemMdl;
    private String previousItem = "", skuCode = "", skuSaleRate = "", skuMrp = "", itemCodeCsv = "", selectedBarCode = "", ConvertedSkuQuantity = "";

    private int hashCode = 0;
    BatchNo batchDetail = new BatchNo();
    private final AddBatchSingle addBatchPopup = new AddBatchSingle();
    String selectedBatchSQL = "", purchaseConversionRate = "", purchaseUnitDecimalPlaces = "", purchaseUnitName = "", purchaseUnitCode = "", skuName = "";
    private ToolTip toolTip = new ToolTip();
    private final MedicineSearch medicineSearch = new MedicineSearch(null, null);
    protected boolean allowNegativeStock = false, isBestBefore = false, isBarcodeActive = false;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_SPACE) {
            if (event.getSource() == txtBatchNo) {
                showBatchDetailPopup();
            }
        }
    }

    private void textHelpEvents() {
        txtMedicineName.setReadOnly(true);
        txtMedicineName.addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_DELETE || event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE) {
                    txtMedicineName.setTag("");
                    txtMedicineName.setText("");

                    clear();
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
                txtBarCode.setText("");
                resetBatchDetail();
                if (batchDetail != null) {
                    fillBatchDetail(batchDetail);
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
        medMdl.setBestBefore(medSearchMdl.getIsBestBefore().equals("1") ? true : false);
        medMdl.setSkuCode(medSearchMdl.getSkuCode());
        medMdl.setSkuName(medSearchMdl.getSkuName());
        medMdl.setSkuDecimalPlaces(DmShared.iNull(medSearchMdl.getSkuDecimalPlaces()).intValue());
        medMdl.setPurchaseUnitCode(medSearchMdl.getPurchaseUnitCode());
        medMdl.setPurchaseUnitName(medSearchMdl.getPurchaseUnitName());
        medMdl.setPurchaseUnitConversionRate(medSearchMdl.getPurchaseUnitConversionRate());
        medMdl.setSkuDecimalPlaces(DmShared.iNull(medSearchMdl.getPurchaseUnitDecimalPlaces()).intValue());
        medMdl.setStock(medSearchMdl.getCurrentStock());
        medMdl.setAllowNegativeStock(DmShared.iNull(medSearchMdl.getIsAllowNegativeStock()).toString().trim().equals("1"));
        medMdl.setIsBarcodeActive(medSearchMdl.getIsBarcodeActive());

        txtMedicineName.setTag(DmShared.xNull(medMdl.getSharedKeyFieldCode()));
        txtMedicineName.setText(DmShared.xNull(medMdl.getSharedKeyFieldName()));
        onMedSelection(true, medMdl, showBatchPopup);
        txtMedicineName.setFocus(true);

    }

    private void onMedSelection(boolean blnStatus, TxtHMedicineName_Mdl t1Row, boolean showBatchPopup) {//TBC
        if (!previousItem.equalsIgnoreCase(DmShared.xNull(txtMedicineName.getTag()))) {
            resetMedicineDetail();
            if (showBatchPopup) {
                showBatchDetailPopup();
            }
        }

        if (blnStatus && t1Row != null) {
            previousItem = DmShared.xNull(txtMedicineName.getTag());
            skuCode = DmShared.xNull(t1Row.getSkuCode());
            txtSKU.setText(DmShared.xNull(t1Row.getSkuName()));
            txtSKU.setTag(DmShared.xNull(t1Row.getPurchaseUnitConversionRate()));
            txtSKU.setTag2nd(DmShared.iNull(t1Row.getSkuDecimalPlaces()).toString());

            txtPurchaseUnit.setText(purchaseUnitName);
            txtPurchaseUnit.setTag(purchaseConversionRate);
            txtPurchaseUnit.setTag2nd(DmShared.iNull(t1Row.getSkuDecimalPlaces()).toString());

            if (DmShared.iNull(t1Row.getPurchaseUnitCode()).longValue() != DmShared.iNull(t1Row.getSkuCode()).longValue()) {
                txtSaleRatePackSize.setText(DmShared.xNull(t1Row.getPurchaseUnitName()) + " = " + DmShared.xNull(t1Row.getPurchaseUnitConversionRate()) + " " + DmShared.xNull(t1Row.getSkuName()));
                txtMRPPackSize.setText(DmShared.xNull(t1Row.getPurchaseUnitName()) + " = " + DmShared.xNull(t1Row.getPurchaseUnitConversionRate()) + " " + DmShared.xNull(t1Row.getSkuName()));
            } else {
                txtSaleRatePackSize.setText(DmShared.xNull(t1Row.getPurchaseUnitName()));
                txtMRPPackSize.setText(DmShared.xNull(t1Row.getPurchaseUnitName()));
            }
            txtSaleRatePackSize.setTag(DmShared.xNull(t1Row.getPurchaseUnitName()));
            txtSaleRatePackSize.setTag2nd(DmShared.xNull(t1Row.getPurchaseUnitCode()));
            allowNegativeStock = t1Row.isAllowNegativeStock();
            isBarcodeActive = DmShared.iNull(t1Row.getIsBarcodeActive()).intValue() == 1;
            isBestBefore = t1Row.isBestBefore();
            purchaseConversionRate = t1Row.getPurchaseUnitConversionRate();
            purchaseUnitName = t1Row.getPurchaseUnitName();
            purchaseUnitCode = t1Row.getPurchaseUnitCode();
            purchaseUnitDecimalPlaces = DmShared.iNull(t1Row.getPurchaseUnitDecimalPlaces()).toString();
            skuName = t1Row.getSkuName();
            setRate(txtMedicineName.getTag(), txtBatchNo.getTag(), medicineTransferWholeSale.txtHDepartmentFrom.getTag(), medicineTransferWholeSale.txtVoucherDate.getText(), purchaseConversionRate);
            setBatchLabelDesign();

        }
    }

//    private void fillViaItemMdl() {
//        if (itemMdl != null) {
//            skuCode = DmShared.xNull(itemMdl.getSkuCode());
//            txtSKU.setText(DmShared.xNull(itemMdl.getSkuName()));
//            txtSKU.setTag(DmShared.xNull(itemMdl.getPurchaseUnitConversionRate()));
//            txtSKU.setTag2nd(DmShared.iNull(itemMdl.getSkuDecimalPlaces()).toString());
//            if (DmShared.iNull(itemMdl.getPurchaseUnitCode()).longValue() != DmShared.iNull(itemMdl.getSkuCode()).longValue()) {
//                txtSaleRatePackSize.setText(DmShared.xNull(itemMdl.getPurchaseUnitName()) + " = " + DmShared.xNull(itemMdl.getPurchaseUnitConversionRate()) + " " + DmShared.xNull(itemMdl.getSkuName()));
//            } else {
//                txtSaleRatePackSize.setText(DmShared.xNull(itemMdl.getPurchaseUnitName()));
//            }
//            txtSaleRatePackSize.setTag(DmShared.xNull(itemMdl.getPurchaseUnitName()));
//            txtSaleRatePackSize.setTag2nd(DmShared.xNull(itemMdl.getPurchaseUnitCode()));
//        }
//    }
    private void setBatchLabelDesign() {
        if (medicineTransferWholeSale.isAllowAddBatch && allowNegativeStock) {
            lblBatchNo.getElement().getStyle().setCursor(Style.Cursor.POINTER);
            lblBatchNo.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
            lblBatchNo.getElement().getStyle().setColor("blue");
            lblBatchNo.setTitle("Add Batch (Shortcut : ctrl+B)");
        } else {
            lblBatchNo.getElement().getStyle().setCursor(Style.Cursor.AUTO);
            lblBatchNo.getElement().getStyle().setTextDecoration(Style.TextDecoration.NONE);
            lblBatchNo.getElement().getStyle().setColor("black");
            lblBatchNo.setTitle("");
        }
    }

    private void manageBatchSelectedQty() {
        selectedBatchSQL = "";
        for (GrdMedicineDetail_Mdl service : (List<GrdMedicineDetail_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
            if (service.getSharedKeyFieldHashCode() != hashCode) {
                if (DmShared.iNull(service.getIbCode()).longValue() > 0) {
                    if (!selectedBatchSQL.equalsIgnoreCase("")) {
                        selectedBatchSQL += " UNION ALL ";
                    }
                    selectedBatchSQL += " Select " + DmShared.iNull(service.getIbCode()).longValue() + " as ibCode, " + DmShared.vNull(service.getQuantity()).doubleValue() + " as qty";
                }
            }
        }
    }

    private void resetMedicineDetail() {
        txtMedicineName.setTag2nd("");

        txtSKU.setText("");
        txtSKU.setTag("");
        txtSKU.setTag2nd("");

        txtMRPPackSize.setText("");
        txtMRPPackSize.setTag("");
        txtMRPPackSize.setTag2nd("");

        txtSaleRatePackSize.setText("");
        txtSaleRatePackSize.setTag("");
        txtSaleRatePackSize.setTag2nd("");

        resetBatchDetail();
    }

    private void showBatchDetailPopup() {
        if (!medicineTransferWholeSale.topControl.getMode().equalsIgnoreCase("Browse")) {
            if (DmShared.iNull(txtMedicineName.getTag()).longValue() != 0) {
//                batchDetail.show(txtMedicineName.getTag(), medicineTransfer.txtHDepartmentFrom.getTag(), medicineTransfer.txtVoucherDate.getText(), selectedBatchSQL);
                batchDetail.show(txtMedicineName.getTag(), medicineTransferWholeSale.txtHDepartmentFrom.getTag(), medicineTransferWholeSale.txtVoucherDate.getText(), selectedBatchSQL, ItemBatchType.ALL_BATCH, false, DmShared.xNull(txtBarCode.getText()), null, "", false);
            } else {
                new DmAlertMessageBox("Please select Medicine Name.", txtMedicineName).show();
            }
        }
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, MedicineTransferWholeSale medicineTransferWholeSale) {
        this.medicineTransferWholeSale = medicineTransferWholeSale;
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);

    }

    private void clear() {
        itemMdl = null;
        previousItem = "";
        txtMedicineName.setTag("");
        txtMedicineName.setText("");
        resetMedicineDetail();

        txtBarCode.setText("");
        selectedBarCode = "";
        itemCodeCsv = "";
        txtRemark.setText("");
        txtQuantity.setText("");
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtQuantity) {
            calculate();
        } else if (event.getSource() == txtBarCode) {
            if (!selectedBarCode.equalsIgnoreCase(DmShared.xNull(txtBarCode.getText())) || DmShared.iNull(txtMedicineName.getTag()).longValue() == 0) {
                txtMedicineName.setTag("");
                txtMedicineName.setText("");
                resetMedicineDetail();
                if (!DmShared.xNull(txtBarCode.getText()).trim().equals("") && !medicineTransferWholeSale.topControl.getMode().equalsIgnoreCase("Browse")) {
                    barCodeSearch();
                } else {
                    itemCodeCsv = "";
                }
            }
        }
    }

    @Override
    public void onChange(ChangeEvent event) {
        if (event.getSource() == txtTrfQuantity) {
            calculate();
        }
    }

    @Override
    public void displayRow(T1 row) {
        clear();
        GrdMedicineDetail_Mdl ourRow = (GrdMedicineDetail_Mdl) row;

//        resetMedicineDetail();
        hashCode = ourRow.getSharedKeyFieldHashCode();
        itemMdl = ourRow.getMedicineMdl();
        txtMedicineName.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtMedicineName.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        //SKU
        txtSKU.setText(DmShared.xNull(ourRow.getSkuName()));
        txtSKU.setTag(DmShared.xNull(ourRow.getSkuQtyForRate()));
        if (itemMdl != null) {
            txtMedicineName.setTag2nd(DmShared.iNull(itemMdl.getSkuDecimalPlaces()).toString());
            txtSKU.setTag2nd(DmShared.iNull(itemMdl.getSkuDecimalPlaces()).toString());
        }
        previousItem = DmShared.xNull(ourRow.getSharedKeyFieldCode());
        txtRemark.setText(DmShared.xNull(ourRow.getRemark()));
        txtBatchNo.setTag(DmShared.xNull(ourRow.getIbCode()));
        txtBatchNo.setText(DmShared.xNull(ourRow.getBatchNo()));
        txtBarCode.setText(DmShared.xNull(ourRow.getBarCode()));
        selectedBarCode = DmShared.xNull(ourRow.getBarCode());
        itemCodeCsv = DmShared.xNull(ourRow.getItemCodeCsv());
        txtExpiryDate.setText(DmShared.dNull(ourRow.getExpiryDate()));
        txtMAnufacturingDate.setText(DmShared.dNull(ourRow.getManufacturingDate()));
        txtBestBeforeDays.setText(DmShared.xNull(ourRow.getBestBeforeDays()));
        txtMRP.setText(DmClient.vNull(ourRow.getMrp(), "0.00"));
        txtSaleRate.setText(DmClient.vNull(ourRow.getSaleRate(), "0.00"));
        txtQuantity.setText(DmShared.xNull(ourRow.getQuantity()));
        txtTrfQuantity.setText(DmShared.xNull(ourRow.getTrfQuantity()));
        txtTrfRate.setText(DmShared.xNull(ourRow.getTrfRate()));
        skuCode = DmShared.xNull(ourRow.getSkuCode());
        skuSaleRate = DmShared.xNull(ourRow.getSkuSaleRate());
        skuMrp = DmShared.xNull(ourRow.getSkuMrp());

        if (DmShared.iNull(ourRow.getPurchaseUnitCode()).longValue() != DmShared.iNull(ourRow.getSkuCode()).longValue()) {
            txtSaleRatePackSize.setText(DmShared.xNull(ourRow.getPurchaseUnitName()) + " = " + DmShared.xNull(ourRow.getSkuQtyForRate()) + " " + DmShared.xNull(ourRow.getSkuName()));
            txtMRPPackSize.setText(DmShared.xNull(ourRow.getPurchaseUnitName()) + " = " + DmShared.xNull(ourRow.getSkuQtyForRate()) + " " + DmShared.xNull(ourRow.getSkuName()));
        } else {
            txtSaleRatePackSize.setText(DmShared.xNull(ourRow.getPurchaseUnitName()));
            txtMRPPackSize.setText(DmShared.xNull(ourRow.getPurchaseUnitName()));
        }
        txtSaleRatePackSize.setTag(DmShared.xNull(ourRow.getPurchaseUnitName()));
        txtSaleRatePackSize.setTag2nd(DmShared.xNull(ourRow.getPurchaseUnitCode()));

        manageBatchSelectedQty();

        manageDisplay();

        if (DmShared.iNull(ourRow.getRequisitionDocId()).longValue() != 0) {
            flwLblReqisitionNoOuter.clear();
            flwLblReqisitionNoOuter.add(flwLblReqisitionNo);
            lblReqisitionNoValue.setText(DmShared.xNull(ourRow.getRequisitionNo()));

            txtBarCode.setEnabled(false);
            txtMedicineName.setEnabled(false);
//            txtQuantity.setFocus(true);
            txtTrfQuantity.setFocus(true);
        } else {
            flwLblReqisitionNoOuter.clear();
            txtBarCode.setEnabled(DmShared.iNull(medicineTransferWholeSale.defaultValue.getBarcodeActive()).intValue() == 1);
            txtMedicineName.setEnabled(true);
            if (DmShared.iNull(medicineTransferWholeSale.defaultValue.getBarcodeActive()).intValue() == 1) {
                txtBarCode.setFocus(true);
            } else {
                txtMedicineName.setFocus(true);
            }
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

        ourRow.setRemark(DmShared.xNull(txtRemark.getText()));
        ourRow.setIbCode(DmShared.xNull(txtBatchNo.getTag()));
        ourRow.setBatchNo(DmShared.xNull(txtBatchNo.getText()));
        ourRow.setBarCode(DmShared.xNull(txtBarCode.getText()));
        ourRow.setItemCodeCsv(DmShared.xNull(itemCodeCsv));
        ourRow.setExpiryDate(DmShared.dNull(txtExpiryDate.getText()));
        ourRow.setMrp(DmClient.vNull(txtMRP.getText(), "0.00"));
        ourRow.setSaleRate(DmClient.vNull(txtSaleRate.getText(), "0.00"));
        ourRow.setManufacturingDate(DmShared.dNull(txtMAnufacturingDate.getText()));
        ourRow.setBestBeforeDays(DmShared.xNull(txtBestBeforeDays.getText()));
        ourRow.setQuantity(DmShared.xNull(txtQuantity.getText()));
        ourRow.setSkuName(DmShared.xNull(txtSKU.getText()));
        ourRow.setSkuQtyForRate(DmShared.xNull(txtSKU.getTag()));

        ourRow.setSkuCode(DmShared.xNull(skuCode));
        ourRow.setPurchaseUnitCode(DmShared.xNull(txtSaleRatePackSize.getTag2nd()));
        ourRow.setPurchaseUnitName(DmShared.xNull(txtSaleRatePackSize.getTag()));
        ourRow.setTrfQuantity(DmShared.xNull(txtTrfQuantity.getText()));
        ourRow.setTrfRate(DmShared.xNull(txtTrfRate.getText()));
        ourRow.setSkuSaleRate(DmClient.vNull(skuSaleRate, DmShared.getNumberFormat(7)));
        ourRow.setSkuMrp(DmClient.vNull(skuMrp, DmShared.getNumberFormat(7)));
        ourRow.setMedicineMdl(itemMdl);

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

    public MedicineTransferGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Medicine Detail", "400px", "280px");
        srvAsync = new ServiceInvoker<MedicineTransferWholeSale_SrvAsync>(GWT.create(MedicineTransferWholeSale_Srv.class), MedicineTransferWholeSale.class.getName());
        textHelpEvents();
        manageEvent();
    }

    private void manageEvent() {
        txtQuantity.addBlurHandler(this);
        txtBatchNo.addBlurHandler(this);
        txtBatchNo.addKeyUpHandler(this);
        txtBatchNo.addKeyDownHandler(this);
        txtBarCode.addBlurHandler(this);
        txtTrfQuantity.addChangeHandler(this);
        aStock.addClickHandler(this);

        btnBatchNo.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                showBatchDetailPopup();
            }
        });
        lblBatchNo.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                showAddBatch();
            }
        });

    }

    private void manageDisplay() {
        txtBestBeforeDays.setEnabled(false);
        txtExpiryDate.setEnabled(false);
        txtMAnufacturingDate.setEnabled(false);
        txtMRP.setEnabled(false);
        txtSaleRate.setEnabled(false);
        txtMRPPackSize.setEnabled(false);
        txtSaleRatePackSize.setEnabled(false);
        txtBatchNo.setReadOnly(true);
        txtSKU.setEnabled(false);
        txtQuantity.setEnabled(false);
        txtPurchaseUnit.setEnabled(false);
    }

    private void fillBatchDetail(BatchNo_Mdl batchMdl) {
        txtBatchNo.setTag(DmShared.xNull(batchMdl.getIbCode()));
        txtBatchNo.setText(DmShared.xNull(batchMdl.getBatchNo()));
        txtBarCode.setText(DmShared.xNull(batchMdl.getBarCode()));
        txtBestBeforeDays.setText(DmShared.xNull(batchMdl.getBestBeforeDays()));
        txtExpiryDate.setText(DmShared.dNull(batchMdl.getExpiryDate()));
        txtMAnufacturingDate.setText(DmShared.dNull(batchMdl.getManufacturingDate()));
        txtMRP.setText(DmClient.vNull(batchMdl.getMrp(), "0.00"));
        txtSaleRate.setText(DmClient.vNull(batchMdl.getSaleRate(), "0.00"));
        setRate(txtMedicineName.getTag(), txtBatchNo.getTag(), medicineTransferWholeSale.txtHDepartmentFrom.getTag(), medicineTransferWholeSale.txtVoucherDate.getText(), purchaseConversionRate);
        txtBatchNo.setFocus(true);
    }

    private void resetBatchDetail() {
        txtBatchNo.setText("");
        txtBatchNo.setTag("");
        txtBatchNo.setTag2nd("");

        txtBestBeforeDays.setText("");
        txtBestBeforeDays.setTag("");
        txtBestBeforeDays.setTag2nd("");

        txtExpiryDate.setText("");
        txtExpiryDate.setTag("");
        txtExpiryDate.setTag2nd("");

        txtMAnufacturingDate.setText("");
        txtMAnufacturingDate.setTag("");
        txtMAnufacturingDate.setTag2nd("");

        txtMRP.setText("");
        txtMRP.setTag("");
        txtMRP.setTag2nd("");

        txtSaleRate.setText("");
        txtSaleRate.setTag("");
        txtSaleRate.setTag2nd("");

        txtTrfRate.setText("");
        txtTrfRate.setTag("");
        txtTrfRate.setTag2nd("");

        skuMrp = "";
        skuSaleRate = "";
    }

    private void calculate() {
        ConvertedSkuQuantity = DmClient.vNull(DmShared.vNull(txtTrfQuantity.getText()).doubleValue() * DmShared.vNull(txtSKU.getTag()).doubleValue(), DmShared.getNumberFormat(7));
//        txtQuantity.setText(DmClient.vNull(txtQuantity.getText(), DmShared.getNumberFormat(DmShared.iNull(txtSKU.getTag2nd()).intValue())));
        txtQuantity.setText(ConvertedSkuQuantity);
        if (DmShared.vNull(txtSKU.getTag()).doubleValue() != 0) {
            skuSaleRate = DmClient.vNull(DmShared.vNull(txtSaleRate.getText()).doubleValue() / DmShared.vNull(txtSKU.getTag()).doubleValue(), DmShared.getNumberFormat(7));
            skuMrp = DmClient.vNull(DmShared.vNull(txtMRP.getText()).doubleValue() / DmShared.vNull(txtSKU.getTag()).doubleValue(), DmShared.getNumberFormat(7));
        }

    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == aStock && !txtMedicineName.getTag().equalsIgnoreCase("")) {
            aStockShow();
        } else {
            super.onClick(event);
        }
    }

    private void aStockShow() {
        srvAsync.invokeRPC().getToolTipShowStock(txtMedicineName.getTag(), DmShared.xNull(txtBatchNo.getTag()), medicineTransferWholeSale.txtHDepartmentFrom.getTag(), medicineTransferWholeSale.txtHDepartmentTo.getTag(), new AsyncCallback<DmResult<List<ToolTip_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<ToolTip_Mdl>> result) {
                if (result.isStatus()) {

                    toolTip.show(result.getData(), aStock);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtMedicineName).show();
                }

            }
        });

    }

    private void barCodeSearch() {
        srvAsync.invokeRPC().getBarCodeSearch(DmShared.xNull(txtBarCode.getText()), DmShared.dNull(medicineTransferWholeSale.txtVoucherDate.getText()), DmShared.iNull(medicineTransferWholeSale.txtHDepartmentFrom.getTag()).toString(), DmShared.iNull(medicineTransferWholeSale.defaultValue.getSiteCode()).toString(), new AsyncCallback<DmResult<BarCode_Mdl>>() {

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
                        clear();
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
        filter.setDepartmentCode(DmShared.xNull(medicineTransferWholeSale.txtHDepartmentFrom.getTag()));
        filter.setDepartmentName(DmShared.xNull(medicineTransferWholeSale.txtHDepartmentFrom.getText()));
        filter.setV_Date(DmShared.dNull(medicineTransferWholeSale.txtVoucherDate.getText()));
        filter.setItemCodeCsv(DmShared.xNull(itemCodeCsv));
        medicineSearch.show(filter, searchChar, medicineTransferWholeSale.txtHVoucherType.getTag());
    }

    private void showAddBatch() {
        if (medicineTransferWholeSale.isAllowAddBatch && allowNegativeStock) {
            addBatchPopup.show(DmShared.xNull(txtMedicineName.getTag()), DmShared.xNull(medicineTransferWholeSale.txtHDepartmentFrom.getTag()), DmShared.dNull(medicineTransferWholeSale.txtVoucherDate.getText()), isBestBefore, isBarcodeActive, DmShared.vNull(purchaseConversionRate).toString(), DmShared.xNull(medicineTransferWholeSale.topControl.getMode()), skuName, DmShared.xNull(purchaseUnitName));  // isBarcodeActive to be manage
        }
    }

    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            onClose();
        } else if (rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_B) {
            showAddBatch();
        } else {
            super.rootKeyInvoked(rootEvent);
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
                    txtTrfRate.setText(DmClient.vNull(tempRate, "0.00"));
                    txtTrfRate.setTag2nd(DmClient.vNull(tempRate, "0.00"));
//                    skuRate = DmClient.vNull(tempSkuRate, DmShared.getNumberFormat(7));
                    calculate();
                }
            }
        });
    }

}
