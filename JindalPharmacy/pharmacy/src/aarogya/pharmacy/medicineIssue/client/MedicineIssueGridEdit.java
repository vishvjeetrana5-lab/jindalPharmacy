package aarogya.pharmacy.medicineIssue.client;

import aarogya.pharmacy.addBatchSingle.client.AddBatchSingle;
import aarogya.pharmacy.addBatchSingle.shared.TxtHBatch_Mdl;
import aarogya.pharmacy.batchNo.client.BatchNo;
import aarogya.pharmacy.batchNo.shared.BatchNo_Mdl;
import aarogya.pharmacy.medicineIssue.shared.MedicineIssue_Srv;
import aarogya.pharmacy.medicineIssue.shared.MedicineIssue_SrvAsync;
import aarogya.pharmacy.medicineIssue.shared.GrdMedicineDetail_Mdl;
import aarogya.pharmacy.medicineIssue.shared.TxtHMedicineName_Mdl;
import aarogya.pharmacy.medicineSearch.client.MedicineSearch;
import aarogya.pharmacy.medicineSearch.shared.MedicineSearchFilter_Mdl;
import aarogya.pharmacy.medicineSearch.shared.MedicineSearch_Mdl;
import aarogya.pharmacy.self.shared.BarCode_Mdl;
import aarogya.selfToolkit.stStatic.shared.STEnum.*;
import aarogya.selfToolkit.toolTip.client.ToolTip;
import aarogya.selfToolkit.toolTip.shared.ToolTip_Mdl;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
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
import com.google.gwt.user.datepicker.client.CalendarUtil;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import java.util.Date;

public class MedicineIssueGridEdit<T1, T2> extends MedicineIssueGridEdit_Dsg<T1, T2> implements BlurHandler, KeyUpHandler, KeyDownHandler, DmRootEvents, AttachEvent.Handler {

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

    private ServiceInvoker<MedicineIssue_SrvAsync> srvAsync;
    private CallBack callBack;
    private MedicineIssue medicineIssue;
    private final MedicineSearch medicineSearch = new MedicineSearch(null, null);
    private String medicineCode = "", selectedItemCode = "", selectedBarCode = "", reqDocId = "", reqNo = "", reqV_SNo = "", skuCode = "", skuSaleRate = "", skuMrp = "";
    BatchNo batchDetail = new BatchNo();
    private final AddBatchSingle addBatchPopup = new AddBatchSingle();
    public int hashCode = 0;
    String selectedBatchSQL = "", purchaseConversionRate = "", purchaseUnitName = "", skuName = "";
    protected boolean allowNegativeStock = false, isBestBefore = false, isBarcodeActive = false;
    private ToolTip toolTip = new ToolTip();
    private String itemCodeCsv = "";

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

        addBatchPopup.addCallBack(new AddBatchSingle.CallBack() {
            @Override
            public void ok(TxtHBatch_Mdl batch_Mdl) {
                setBatchDetail(batch_Mdl);
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        txtQuantity.setFocus(true);
                    }
                });
            }

            @Override
            public void close() {

            }
        });

    }

    private void setMedSelection(MedicineSearch_Mdl medSearchMdl, boolean showBatchPopup) {
        TxtHMedicineName_Mdl medMdl = new TxtHMedicineName_Mdl();
        medMdl.setSharedKeyFieldCode(medSearchMdl.getSharedKeyFieldCode());
        medMdl.setSharedKeyFieldName(medSearchMdl.getSharedKeyFieldName());
        medMdl.setManualCode(medSearchMdl.getManualCode());
        medMdl.setIsBestBefore(medSearchMdl.getIsBestBefore());
        medMdl.setSkuCode(medSearchMdl.getSkuCode());
        medMdl.setSkuName(medSearchMdl.getSkuName());
        medMdl.setSkuDecimalPlaces(medSearchMdl.getSkuDecimalPlaces());
        medMdl.setPurchaseUnitCode(medSearchMdl.getPurchaseUnitCode());
        medMdl.setPurchaseUnitName(medSearchMdl.getPurchaseUnitName());
        medMdl.setPurchaseUnitConversionRate(medSearchMdl.getPurchaseUnitConversionRate());
        medMdl.setStock(medSearchMdl.getCurrentStock());
        medMdl.setAllowNegativeStock(DmShared.iNull(medSearchMdl.getIsAllowNegativeStock()).toString().trim().equals("1"));
        medMdl.setIsBarcodeActive(medSearchMdl.getIsBarcodeActive());
        medMdl.setInventoryAc(medSearchMdl.getInventoryAc());
        medMdl.setConsumptionAc(medSearchMdl.getConsumptionAc());

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
            if (DmShared.iNull(t1Row.getPurchaseUnitCode()).longValue() != DmShared.iNull(t1Row.getSkuCode()).longValue()) {
                txtSaleRatePackSize.setText(DmShared.xNull(t1Row.getPurchaseUnitName()) + " = " + DmShared.xNull(t1Row.getPurchaseUnitConversionRate()) + " " + DmShared.xNull(t1Row.getSkuName()));
            } else {
                txtSaleRatePackSize.setText(DmShared.xNull(t1Row.getPurchaseUnitName()));
            }
            txtSaleRatePackSize.setTag(DmShared.xNull(t1Row.getPurchaseUnitName()));
            txtSaleRatePackSize.setTag2nd(DmShared.xNull(t1Row.getPurchaseUnitCode()));
            allowNegativeStock = t1Row.isAllowNegativeStock();
            isBarcodeActive = DmShared.iNull(t1Row.getIsBarcodeActive()).intValue() == 1;
            isBestBefore = DmShared.iNull(t1Row.getIsBestBefore()).intValue() == 1;
            purchaseConversionRate = t1Row.getPurchaseUnitConversionRate();
            purchaseUnitName = t1Row.getPurchaseUnitName();
            skuName = t1Row.getSkuName();
            txtMedicineAc.setText(t1Row.getInventoryAc());
            txtMedicineAc.setTag(t1Row.getConsumptionAc());
            setBatchLabelDesign();
        }
    }

    private void showAddBatch() {
        if (medicineIssue.isAllowAddBatch && allowNegativeStock && (!medicineIssue.isExpiryItems)) {
            addBatchPopup.show(DmShared.xNull(txtMedicineName.getTag()), DmShared.xNull(medicineIssue.txtHDepartment.getTag()), DmShared.dNull(medicineIssue.txtVoucherDate.getText()), isBestBefore, isBarcodeActive, DmShared.vNull(purchaseConversionRate).toString(), DmShared.xNull(medicineIssue.topControl.getMode()), skuName, DmShared.xNull(purchaseUnitName));  // isBarcodeActive to be manage
        }
    }

    private void setBatchLabelDesign() {
        if (medicineIssue.isAllowAddBatch && allowNegativeStock && (!medicineIssue.isExpiryItems)) {
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

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, MedicineIssue medicineIssue) {
        this.medicineIssue = medicineIssue;
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);

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
                if (!DmShared.xNull(txtBarCode.getText()).trim().equals("") && !medicineIssue.topControl.getMode().equalsIgnoreCase("Browse")) {
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
        allowNegativeStock = ourRow.isAllowNegativeStock();
        isBarcodeActive = ourRow.isBarcodeActive();
        isBestBefore = DmShared.iNull(ourRow.getIsBestBefore()).intValue() == 1;
        selectedItemCode = DmShared.xNull(ourRow.getSharedKeyFieldCode());
        purchaseConversionRate = DmShared.vNull(ourRow.getSkuQtyForRate()).toString();
        purchaseUnitName = ourRow.getPurchaseUnitName();
        skuName = ourRow.getSkuName();
        txtRemark.setText(DmShared.xNull(ourRow.getRemark()));
        txtBatchNo.setTag(DmShared.xNull(ourRow.getIbCode()));
        txtBatchNo.setText(DmShared.xNull(ourRow.getBatchNo()));
        txtExpiryDate.setText(DmShared.dNull(ourRow.getExpiryDate()));
        txtMAnufacturingDate.setText(DmShared.dNull(ourRow.getManufacturingDate()));
        txtMRP.setText(DmClient.vNull(ourRow.getMrp(), "0.00"));
        txtSaleRate.setText(DmClient.vNull(ourRow.getSaleRate(), "0.00"));
        txtQuantity.setText(DmShared.xNull(ourRow.getQuantity()));

        txtMedicineAc.setText(DmShared.xNull(ourRow.getInventoryAc()));
        txtMedicineAc.setTag(DmShared.xNull(ourRow.getConsumptionAc()));

        //SKU
        txtSKU.setText(DmShared.xNull(ourRow.getSkuName()));
        txtSKU.setTag(DmShared.xNull(ourRow.getSkuQtyForRate()));
        txtSKU.setTag2nd(DmShared.xNull(ourRow.getSkuDecimalPlaces()));
        skuCode = DmShared.xNull(ourRow.getSkuCode());
        skuSaleRate = DmShared.xNull(ourRow.getSkuSaleRate());
        skuMrp = DmShared.xNull(ourRow.getSkuMrp());
        //Requisition
        reqDocId = DmShared.xNull(ourRow.getRequisitionDocId());
        reqNo = DmShared.xNull(ourRow.getRequisitionNo());
        reqV_SNo = DmShared.xNull(ourRow.getRequisitionV_SNo());

        if (DmShared.iNull(ourRow.getRequisitionDocId()).longValue() != 0) {
            flwLblReqisitionNoOuter.clear();
            flwLblReqisitionNoOuter.add(flwLblReqisitionNo);
            lblReqisitionNoValue.setText(DmShared.xNull(reqNo));
        } else {
            flwLblReqisitionNoOuter.clear();
        }

        if (DmShared.iNull(ourRow.getPurchaseUnitCode()).longValue() != DmShared.iNull(ourRow.getSkuCode()).longValue()) {
            txtSaleRatePackSize.setText(DmShared.xNull(ourRow.getPurchaseUnitName()) + " = " + DmShared.xNull(ourRow.getSkuQtyForRate()) + " " + DmShared.xNull(ourRow.getSkuName()));
        } else {
            txtSaleRatePackSize.setText(DmShared.xNull(ourRow.getPurchaseUnitName()));
        }
        txtSaleRatePackSize.setTag(DmShared.xNull(ourRow.getPurchaseUnitName()));
        txtSaleRatePackSize.setTag2nd(DmShared.xNull(ourRow.getPurchaseUnitCode()));
        manageBatchSelectedQty();
        setBatchLabelDesign();
        txtBarCode.setText(DmShared.xNull(ourRow.getBarCode()));
        itemCodeCsv = DmShared.xNull(ourRow.getItemCodeCsv());
        selectedBarCode = DmShared.xNull(ourRow.getBarCode());

        manageDisplay();
        if (DmShared.iNull(reqDocId).longValue() == 0) {
            if (DmShared.iNull(medicineIssue.defaultValue.getBarcodeActive()).intValue() == 1) {
                txtBarCode.setFocus(true);
            } else {
                txtMedicineName.setFocus(true);
            }
        } else {
            txtQuantity.setFocus(true);
        }
        txtPurchaseValue.setText(DmShared.xNull(ourRow.getPurchaseValue()));
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
        ourRow.setSkuSaleRate(DmClient.vNull(skuSaleRate, DmShared.getNumberFormat(7)));
        ourRow.setSkuMrp(DmClient.vNull(skuMrp, DmShared.getNumberFormat(7)));
        ourRow.setManufacturingDate(DmShared.dNull(txtMAnufacturingDate.getText()));
        ourRow.setQuantity(DmShared.xNull(txtQuantity.getText()));
        ourRow.setSkuName(DmShared.xNull(txtSKU.getText()));
        ourRow.setSkuCode(DmShared.xNull(skuCode));
        ourRow.setPurchaseUnitCode(DmShared.xNull(txtSaleRatePackSize.getTag2nd()));
        ourRow.setPurchaseUnitName(DmShared.xNull(txtSaleRatePackSize.getTag()));
        ourRow.setSkuDecimalPlaces(DmShared.xNull(txtSKU.getTag2nd()));
        ourRow.setSkuQtyForRate(DmShared.xNull(txtSKU.getTag()));
        ourRow.setAllowNegativeStock(allowNegativeStock);
        ourRow.setBarcodeActive(isBarcodeActive);

        ourRow.setInventoryAc(DmShared.xNull(txtMedicineAc.getText()));
        ourRow.setConsumptionAc(DmShared.xNull(txtMedicineAc.getTag()));
        //Requisition
        ourRow.setRequisitionDocId(DmShared.xNull(reqDocId));
        ourRow.setRequisitionNo(DmShared.xNull(reqNo));
        ourRow.setRequisitionV_SNo(DmShared.xNull(reqV_SNo));
        ourRow.setPurchaseValue(DmShared.xNull(txtPurchaseValue.getText()));

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

    public MedicineIssueGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Medicine Detail", "400px", "300px");
        srvAsync = new ServiceInvoker<MedicineIssue_SrvAsync>(GWT.create(MedicineIssue_Srv.class), MedicineIssue.class.getName());
        textHelpEvents();
        manageEvent();
    }

    private void manageEvent() {
        txtQuantity.addBlurHandler(this);
        txtBarCode.addBlurHandler(this);
        txtBatchNo.addKeyUpHandler(this);
        txtBatchNo.addKeyDownHandler(this);
        aStock.addClickHandler(this);
        lblBatchNo.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                showAddBatch();
            }
        });
    }

    private void manageDisplay() {
        txtExpiryDate.setEnabled(false);
        txtMAnufacturingDate.setEnabled(false);
        txtMRP.setEnabled(false);
        txtSaleRate.setEnabled(false);
        txtSaleRatePackSize.setEnabled(false);
        txtBatchNo.setReadOnly(true);
        txtSKU.setEnabled(false);
        if (DmShared.iNull(reqDocId).longValue() == 0) {
            txtMedicineName.setEnabled(true);
            txtBarCode.setEnabled(DmShared.iNull(medicineIssue.defaultValue.getBarcodeActive()).intValue() == 1);
        } else {
            txtMedicineName.setEnabled(false);
            txtBarCode.setEnabled(false);
        }
    }

    private void setBatchDetail(TxtHBatch_Mdl batch_Mdl) {
        txtBatchNo.setTag(DmShared.xNull(batch_Mdl.getSharedKeyFieldCode()));
        txtBatchNo.setText(DmShared.xNull(batch_Mdl.getSharedKeyFieldName()));
        txtExpiryDate.setText(DmShared.dNull(batch_Mdl.getExpDate()));
        txtMAnufacturingDate.setText(DmShared.dNull(batch_Mdl.getMfgDate()));
        txtMRP.setText(DmClient.vNull(batch_Mdl.getMrp(), "0.00"));
        txtSaleRate.setText(DmClient.vNull(batch_Mdl.getSaleRate(), "0.00"));

        Date tempDate = DmShared.convertDate(DmShared.dNull(txtExpiryDate.getText()));
        CalendarUtil.addDaysToDate(tempDate, (0 - DmShared.iNull(medicineIssue.expiryAlertBeforeDays).intValue()));
        if (tempDate.getTime() <= DmShared.convertDate(medicineIssue.txtVoucherDate.getText()).getTime() && !medicineIssue.isExpiryItems) {
            new DmAlertMessageBox("<b>" + txtMedicineName.getText() + "</b>, Batch No. <b>" + txtBatchNo.getText() + "</b>, Expiring On <b>" + txtExpiryDate.getText() + "</b>", txtQuantity).show();
            txtBatchNo.setFocus(true);
        } else {
            txtBatchNo.setFocus(true);
        }
    }

    private void fillBatchDetail(BatchNo_Mdl batchMdl) {
        txtBatchNo.setTag(DmShared.xNull(batchMdl.getIbCode()));
        txtBatchNo.setText(DmShared.xNull(batchMdl.getBatchNo()));
        txtBarCode.setText(DmShared.xNull(batchMdl.getBarCode()));
        txtExpiryDate.setText(DmShared.dNull(batchMdl.getExpiryDate()));
        txtMAnufacturingDate.setText(DmShared.dNull(batchMdl.getManufacturingDate()));
        txtMRP.setText(DmClient.vNull(batchMdl.getMrp(), "0.00"));
        txtSaleRate.setText(DmClient.vNull(batchMdl.getSaleRate(), "0.00"));

        txtPurchaseValue.setTag(DmShared.vNull(batchMdl.getSkuGrossPurchseRate()).toString());

        Date tempDate = DmShared.convertDate(DmShared.dNull(txtExpiryDate.getText()));
        CalendarUtil.addDaysToDate(tempDate, (0 - DmShared.iNull(medicineIssue.expiryAlertBeforeDays).intValue()));
        if ((tempDate.getTime() <= DmShared.convertDate(medicineIssue.txtVoucherDate.getText()).getTime()) && (medicineIssue.isExpiryItems == false)) {
            new DmAlertMessageBox("<b>" + txtMedicineName.getText() + "</b>, Batch No. <b>" + txtBatchNo.getText() + "</b>, Expiring On <b>" + txtExpiryDate.getText() + "</b>", txtQuantity).show();
            txtBatchNo.setFocus(true);
        } else {
            txtBatchNo.setFocus(true);
        }

    }

    private void resetBatchDetail() {
        txtBatchNo.setTag("");
        txtBatchNo.setText("");
        txtExpiryDate.setText("");
        txtMAnufacturingDate.setText("");
        txtMRP.setText("");
        txtSaleRate.setText("");
        skuMrp = "";
        skuSaleRate = "";
    }

    private void resetMedicineDetail() {
        txtSKU.setText("");
        txtSKU.setTag("");
        txtSKU.setTag2nd("");
        txtMedicineName.setTag2nd("");
        resetBatchDetail();
    }

    private void showBatchDetailPopup() {
        if (!medicineIssue.topControl.getMode().equalsIgnoreCase("Browse")) {
            if (DmShared.iNull(txtMedicineName.getTag()).longValue() != 0) {
//                batchDetail.show(txtMedicineName.getTag(), medicineIssue.txtHDepartment.getTag(), medicineIssue.txtVoucherDate.getText(), selectedBatchSQL, medicineIssue.isExpiryItems, false, DmShared.xNull(txtBarCode.getText()));
                batchDetail.show(txtMedicineName.getTag(), medicineIssue.txtHDepartment.getTag(), medicineIssue.txtVoucherDate.getText(), selectedBatchSQL, medicineIssue.isExpiryItems ? ItemBatchType.EXPIRED_BATCH : ItemBatchType.NON_EXPIRED_BATCH, false, DmShared.xNull(txtBarCode.getText()), null, "", false);
            } else {
                new DmAlertMessageBox("Please select Medicine Name.", txtMedicineName).show();
            }
        }
    }

    private void calculate() {
        skuMrp = "";
        skuSaleRate = "";
        txtQuantity.setText(DmClient.vNull(txtQuantity.getText(), DmShared.getNumberFormat(DmShared.iNull(txtSKU.getTag2nd()).intValue())));
        if (DmShared.vNull(txtSKU.getTag()).doubleValue() != 0) {
            skuSaleRate = DmClient.vNull(DmShared.vNull(txtSaleRate.getText()).doubleValue() / DmShared.vNull(txtSKU.getTag()).doubleValue(), DmShared.getNumberFormat(7));
            skuMrp = DmClient.vNull(DmShared.vNull(txtMRP.getText()).doubleValue() / DmShared.vNull(txtSKU.getTag()).doubleValue(), DmShared.getNumberFormat(7));
        }

        txtPurchaseValue.setText(DmClient.vNull(DmShared.vNull(txtPurchaseValue.getTag()) * DmShared.vNull(txtQuantity.getText()), "0.00"));

    }

    @Override
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

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == aStock && !txtMedicineName.getTag().equalsIgnoreCase("")) {
            aStockShow();
        } else {
            super.onClick(event);
        }
    }

    private void aStockShow() {
        srvAsync.invokeRPC().getToolTipShowStock(txtMedicineName.getTag(), DmShared.xNull(txtBatchNo.getTag()), medicineIssue.txtHDepartment.getTag(), medicineIssue.txtHDepartment.getTag2nd(), new AsyncCallback<DmResult<List<ToolTip_Mdl>>>() {

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
        srvAsync.invokeRPC().getBarCodeSearch(DmShared.xNull(txtBarCode.getText()), DmShared.dNull(medicineIssue.txtVoucherDate.getText()), DmShared.iNull(medicineIssue.txtHDepartment.getTag()).toString(), DmShared.iNull(medicineIssue.defaultValue.getSiteCode()).toString(), medicineIssue.isExpiryItems, new AsyncCallback<DmResult<BarCode_Mdl>>() {

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
        filter.setDepartmentCode(DmShared.xNull(medicineIssue.txtHDepartment.getTag()));
        filter.setDepartmentName(DmShared.xNull(medicineIssue.txtHDepartment.getText()));
        filter.setV_Date(DmShared.dNull(medicineIssue.txtVoucherDate.getText()));
        filter.setItemCodeCsv(DmShared.xNull(itemCodeCsv));
        medicineSearch.show(filter, searchChar, medicineIssue.txtHVoucherType.getTag());
    }

}
