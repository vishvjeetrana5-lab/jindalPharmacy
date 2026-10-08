package aarogya.pharmacy.saleBillReturnProvisional.client;

import aarogya.pharmacy.addBatchSingle.client.AddBatchSingle;
import aarogya.pharmacy.addBatchSingle.shared.TxtHBatch_Mdl;
import aarogya.pharmacy.batchNo.client.BatchNo;
import aarogya.pharmacy.batchNo.shared.BatchNo_Mdl;
import aarogya.pharmacy.medicineSearch.client.MedicineSearch;
import aarogya.pharmacy.medicineSearch.shared.MedicineSearchFilter_Mdl;
import aarogya.pharmacy.medicineSearch.shared.MedicineSearch_Mdl;
import aarogya.pharmacy.saleBillReturnProvisional.shared.*;
import aarogya.pharmacy.self.shared.BarCode_Mdl;
import aarogya.selfToolkit.stStatic.shared.STEnum.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import dataman.dmBase.dmBaseStatic.client.*;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.overHeadLine.shared.OverHeadITS_Mdl;
import java.util.Date;
import java.util.List;

public class MedicineDetailsEdit<T1, T2> extends MedicineDetailsEdit_Dsg<T1, T2> implements BlurHandler, DmRootEvents, AttachEvent.Handler, KeyUpHandler, KeyDownHandler, FocusHandler {

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

        public void onOk(GrdMedicine_Mdl grdtest);

        public void onDelete();
    }

    private final ServiceInvoker<SaleBillReturnProvisional_SrvAsync> srvAsync;
    private CallBack callBack;
    protected SaleBillReturnProvisional saleBillReturnProvisional;
    protected int hashCode = 0;
    private String saleAcCode = "";
    private String selectedMedicine = "", skuUnitName = "", sbDocId = "", sbV_sNo = "";
    protected TxtHMedicine_Mdl medicineMdl = new TxtHMedicine_Mdl();
    private final AddBatchSingle addBatchPopup = new AddBatchSingle();
    boolean isRateEditable = false, isAllowNegativeStock = false, isBarcodeActive = false;
    String isSaleRateEditable = "", tempDiscountPer = "", tempDiscountPer2 = "", prvQuantity = "", prvFreeQty = "";
    double totalDiscount = 0;
    private String selectedBarCode = "";
    private String itemCodeCsv = "";

    private final BatchNo batchDetail = new BatchNo();
    private final MedicineSearch medicineSearch = new MedicineSearch(null, null);

    private void textHelpEvents() {
        batchDetail.addCallBack(new BatchNo.CallBack() {
            @Override
            public void ok(BatchNo_Mdl batchDetail, List<BatchNo_Mdl> detailBatchMdl) {
                resetBatchDetail();
                if (batchDetail != null) {
                    setBatchModel(batchDetail);
                }
            }

            @Override
            public void close(boolean isErrorClose) {
                if (!isErrorClose) {
                    txtTotalQty.setFocus(true);
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
                        txtRate.setFocus(true);
                    }
                });
            }

            @Override
            public void close() {

            }
        });
        txtMedicineName.addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_DELETE || event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE) {
                    clear();
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
                onMedicineSelection(medSearchMdl, true);
                txtMedicineName.setFocus(true);
            }

            @Override
            public void close() {
                txtMedicineName.setFocus(true);
            }
        });
    }

    private void onMedicineSelection(MedicineSearch_Mdl medSearchMdl, boolean showBatchFlag) {

        txtMedicineName.setTag(DmShared.xNull(medSearchMdl.getSharedKeyFieldCode()));
        txtMedicineName.setText(DmShared.xNull(medSearchMdl.getSharedKeyFieldName()));

        if (DmShared.iNull(selectedMedicine).longValue() != DmShared.iNull(medSearchMdl.getSharedKeyFieldCode()).longValue()) {
            clearOnMedicineChange();
            if (showBatchFlag) {
                showBatchDetailPopup();
            }
        }

        //<editor-fold defaultstate="collapsed" desc="Set Medicine Mdl">
        medicineMdl = new TxtHMedicine_Mdl();
        medicineMdl.setSharedKeyFieldCode(medSearchMdl.getSharedKeyFieldCode());
        medicineMdl.setSharedKeyFieldName(medSearchMdl.getSharedKeyFieldName());
        medicineMdl.setSkuCode(medSearchMdl.getSkuCode());
        medicineMdl.setSkuName(medSearchMdl.getSkuName());
        medicineMdl.setItemProperty(medSearchMdl.getPropertyList());
        medicineMdl.setAllowDecimalPlaces(DmShared.iNull(medSearchMdl.getSaleUnitDecimalPlaces()).intValue());
        medicineMdl.setAllowSkuDecimalPlaces(DmShared.iNull(medSearchMdl.getSkuDecimalPlaces()).intValue());
        medicineMdl.setRestrictGroup(medSearchMdl.getRestrictedGroupCode());
        medicineMdl.setHsnCode(medSearchMdl.getHsnCode());
        medicineMdl.setHsnName(medSearchMdl.getHsnName());
        medicineMdl.setItemGstNature(medSearchMdl.getItemGSTNature());
        medicineMdl.setItsBasedOn(medSearchMdl.getItsBasedOn());
        medicineMdl.setBestBefore(DmShared.iNull(medSearchMdl.getIsBestBefore()).intValue() == 1);
        medicineMdl.setDiscountPer(medSearchMdl.getSaleDiscountPer());
        medicineMdl.setPurchaseUnitConversionRate(medSearchMdl.getPurchaseUnitConversionRate());
        medicineMdl.setPurchaseUnit(medSearchMdl.getPurchaseUnitCode());
        medicineMdl.setPurchaseUnitName(medSearchMdl.getPurchaseUnitName());
        medicineMdl.setSaleUnitCode(medSearchMdl.getSaleUnitCode());
        medicineMdl.setSaleUnitName(medSearchMdl.getSaleUnitName());
        medicineMdl.setConversionRate(medSearchMdl.getSaleUnitConversionRate());
        medicineMdl.setIsSaleRateEditable(medSearchMdl.getIsSaleRateEditable());
        medicineMdl.setReturnableMedicine(DmShared.iNull(medSearchMdl.getIsReturnableItem()).intValue() == 1);
        medicineMdl.setAllowNegativeStock(DmShared.iNull(medSearchMdl.getIsAllowNegativeStock()).intValue() == 1);
        medicineMdl.setBarcodeActive(DmShared.iNull(medSearchMdl.getIsBarcodeActive()).intValue() == 1);
        medicineMdl.setCompositionCode(medSearchMdl.getCompositionCode());
        medicineMdl.setManualCode(medSearchMdl.getManualCode());
        medicineMdl.setCompositionName(medSearchMdl.getCompositionName());
        medicineMdl.setSaleGSTUnit(DmShared.xNull(medSearchMdl.getSaleUnitGstUnitCode()));
        //</editor-fold>

        selectedMedicine = DmShared.xNull(medicineMdl.getSharedKeyFieldCode());
        txtMedicineName.setTag2nd(DmShared.xNull(medicineMdl.getItemProperty()));
        skuUnitName = DmShared.xNull(medicineMdl.getSkuName());
        isSaleRateEditable = DmShared.xNull(medicineMdl.getIsSaleRateEditable());
        isAllowNegativeStock = medicineMdl.isAllowNegativeStock();
        isBarcodeActive = medicineMdl.isBarcodeActive();
        managaSaleRate();

        setLabelBatchNoView();
        txtConversionRateMRP.setText(medicineMdl.getPurchaseUnitName());
        txtConversionRate.setText("");
//        txtConversionRateMRP.setText("");
        txtFreeQtyConversionRate.setText("");
        lblSaleConversionRate.setText("");
        lblSaleConversionRate.setTitle("");
        if (DmShared.vNull(medicineMdl.getPurchaseUnitConversionRate()).doubleValue() > 0 && !DmShared.xNull(medicineMdl.getSaleUnitName()).equalsIgnoreCase("") && !DmShared.xNull(medicineMdl.getPurchaseUnitName()).equalsIgnoreCase("")) {
            if (DmShared.xNull(medicineMdl.getSkuName()).trim().equalsIgnoreCase(DmShared.xNull(medicineMdl.getSaleUnitName()))) {
                txtConversionRate.setText(medicineMdl.getSaleUnitName());
                txtFreeQtyConversionRate.setText(medicineMdl.getSaleUnitName());
//                txtConversionRateMRP.setText(medicineMdl.getSaleUnitName());
            } else if (!DmShared.xNull(medicineMdl.getPurchaseUnitName()).trim().equalsIgnoreCase(DmShared.xNull(medicineMdl.getSaleUnitName()))) {
                txtConversionRate.setText(medicineMdl.getSaleUnitName() + " = " + medicineMdl.getConversionRate() + " " + medicineMdl.getSkuName());
                txtFreeQtyConversionRate.setText(medicineMdl.getSaleUnitName() + " = " + medicineMdl.getConversionRate() + " " + medicineMdl.getSkuName());
                lblSaleConversionRate.setText(medicineMdl.getSaleUnitName() + " = " + medicineMdl.getConversionRate() + " " + medicineMdl.getSkuName());
                lblSaleConversionRate.setTitle(medicineMdl.getSaleUnitName() + " = " + medicineMdl.getConversionRate() + " " + medicineMdl.getSkuName());
//                txtConversionRateMRP.setText(medicineMdl.getSaleUnitName() + " = " + medicineMdl.getConversionRate() + " " + medicineMdl.getSkuName());
            }
        }
        txtDiscountPer.setText(DmClient.vNull(medicineMdl.getDiscountPer(), "0.00"));
        verifyITS(medicineMdl.getSharedKeyFieldCode(), DmShared.vNull(txtRate.getText()).doubleValue(), medicineMdl.getItsBasedOn(), DmShared.dNull(saleBillReturnProvisional.txtDate.getText()));
        if (DmShared.iNull(saleBillReturnProvisional.defaultValue.getPharmacyBarcodeActive()).intValue() == 1) {
            txtBarcode.setFocus(true);
        } else {
            txtMedicineName.setFocus(true);
        }
    }

    private void managaSaleRate() {

        if (isRateEditable) {
            if (isSaleRateEditable.equalsIgnoreCase("1")) {
                txtRate.setEnabled(true);
            } else {
                txtRate.setEnabled(false);
            }
        } else {
            txtRate.setEnabled(false);
        }
    }

    private void resetBatchDetail() {
        txtBatchNo.setTag("");
        txtBatchNo.setTag2nd("");
        txtBatchNo.setText("");
        txtManufacturingDate.setText("");
        txtManufacturingDate.setTag("");
        txtManufacturingDate.setTag2nd("");
        txtExpiryDate.setText("");
        txtExpiryDate.setTag("");
        txtExpiryDate.setTag2nd("");
        txtMRP.setText("");
        txtMRP.setTag("");
        txtMRP.setTag2nd("");
        txtSkuRate.setTag("");
        txtSkuRate.setTag2nd("");
        txtSkuRate.setText("");
    }

    private void clearOnMedicineChange() {
        txtDiscountPer.setText("");
        txtDiscountPer.setTag("");
        txtDiscountPer.setTag2nd("");
        txtDiscount.setText("0");
        txtDiscount.setTag("");
        txtDiscount.setTag2nd("");
        txtAmount.setText("0");
        txtAmount.setTag("");
        txtAmount.setTag2nd("");
        txtRate.setText("0");
        txtRate.setTag("");
        txtRate.setTag2nd("");
        txtTotalQty.setText("0");
        txtTotalQty.setTag("");
        txtTotalQty.setTag2nd("");
        txtBatchNo.setTag("");
        txtBatchNo.setTag2nd("");
        txtBatchNo.setText("");
        txtNetAmount.setText("0.00");
        txtTaxableAmt.setText("0.00");
        txtSgstUgstPer.setText("");
        txtSgstUgstPer.setTag("");
        txtSgstUgstPer.setTag2nd("");
        txtSgstUgst.setText("0.00");
        txtSgstUgst.setTag("");
        txtSgstUgst.setTag2nd("");
        txtCGSTPer.setText("");
        txtCGSTPer.setTag("");
        txtCGSTPer.setTag2nd("");
        txtCGST.setText("0.00");
        txtCGST.setTag("");
        txtCGST.setTag2nd("");
        txtGstRate.setTag("");
        txtGstRate.setText("");
        txtGstRate.setTag2nd("");
        txtGstAmt.setTag("0.00");
        txtGstAmt.setText("");
        txtGstAmt.setTag2nd("");
        saleAcCode = "";
        txtFreeQty.setText("0");
        txtFreeQty.setTag("");
        txtFreeQty.setTag2nd("");
        txtDiscountPer2.setText("0.00");
        txtDiscountPer2.setTag("");
        txtDiscountPer2.setTag2nd("");
        txtDiscount2.setText("0.00");
        txtDiscount2.setTag("");
        txtDiscount2.setTag2nd("");
        totalDiscount = 0;
        resetBatchDetail();
    }

    private void setBatchDetail(TxtHBatch_Mdl batch_Mdl) {
        double tempRate = 0, tempSkuRate = 0;

        txtBatchNo.setTag(DmShared.xNull(batch_Mdl.getSharedKeyFieldCode()));
        txtBatchNo.setText(DmShared.xNull(batch_Mdl.getSharedKeyFieldName()));
        txtBarcode.setText(DmShared.xNull(batch_Mdl.getBarcode()));
        txtManufacturingDate.setText(DmShared.dNull(batch_Mdl.getMfgDate()));
        txtExpiryDate.setText(DmShared.dNull(batch_Mdl.getExpDate()));
        txtMRP.setText(DmShared.xNull(batch_Mdl.getMrp()));
        txtMRP.setTag(DmClient.vNull(batch_Mdl.getSkuMrp(), DmShared.getNumberFormat(7)));

        if (saleBillReturnProvisional.saleOn == MedicineSaleOn.MRP) {
            tempRate = DmShared.vNull(batch_Mdl.getMrp()).doubleValue();
            tempSkuRate = DmShared.vNull(batch_Mdl.getSkuMrp()).doubleValue();
        } else if (saleBillReturnProvisional.saleOn == MedicineSaleOn.COST_PRICE) {
            tempSkuRate = DmShared.vNull(batch_Mdl.getSkuLandedRate()).doubleValue();
            tempRate = DmShared.vNull(batch_Mdl.getSkuLandedRate()).doubleValue() * DmShared.vNull(medicineMdl.getPurchaseUnitConversionRate()).doubleValue();
        } else {
            tempRate = DmShared.vNull(batch_Mdl.getSaleRate()).doubleValue();
            tempSkuRate = DmShared.vNull(batch_Mdl.getSkuSaleRate()).doubleValue();
        }

        txtRate.setText(DmClient.vNull(tempRate, "0.00"));
        txtRate.setTag(DmClient.vNull(tempSkuRate, DmShared.getNumberFormat(7)));
        txtSkuRate.setText(DmClient.vNull(tempSkuRate, "0.00"));
        Date tempDate = DmShared.convertDate(DmShared.dNull(txtExpiryDate.getText()));
        CalendarUtil.addDaysToDate(tempDate, (0 - DmShared.iNull(saleBillReturnProvisional.expiryAlertBeforeDays).intValue()));

        if (tempDate.getTime() <= DmShared.convertDate(saleBillReturnProvisional.txtDate.getText()).getTime()) {
            new DmAlertMessageBox("<b>" + txtMedicineName.getText() + "</b> Batch No. <b>" + txtBatchNo.getText() + "</b> Expiring On <b>" + txtExpiryDate.getText() + "</b>", txtTotalQty).show();
        } else {

            txtTotalQty.setFocus(true);
        }
        calculate();
    }

    private void verifyITS(String itemCode, double ratePerUnit, String itsBasedOn, String billDate) {
        srvAsync.invokeRPC(true).getITS(itemCode, ratePerUnit, itsBasedOn, billDate, new AsyncCallback<DmResult<List<OverHeadITS_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtMedicineName).show();
            }

            @Override
            public void onSuccess(DmResult<List<OverHeadITS_Mdl>> result) {
                if (result.isStatus()) {
                    saleAcCode = result.getValue1();
                    setITS(result.getData(), null);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtMedicineName).show();
                }
            }
        });
    }

    private void setITS(List<OverHeadITS_Mdl> lstITS, GrdMedicine_Mdl ourRow) {
        if (lstITS != null && lstITS.size() > 0) {
            for (OverHeadITS_Mdl iTS_Mdl : lstITS) {
                if (DmShared.xNull(iTS_Mdl.getOverHeadManualCode()).equalsIgnoreCase("CGST")) {
                    txtCGSTPer.setText(DmShared.xNull(iTS_Mdl.getAtRate()));
                    txtCGSTPer.setTag(DmShared.xNull(iTS_Mdl.getAccountCode()));
                } else if (DmShared.xNull(iTS_Mdl.getOverHeadManualCode()).equalsIgnoreCase("SGST") && !saleBillReturnProvisional.defaultValue.isUnionTerritory()) {
                    txtSgstUgstPer.setText(DmShared.xNull(iTS_Mdl.getAtRate()));
                    txtSgstUgstPer.setTag(DmShared.xNull(iTS_Mdl.getAccountCode()));
                } else if (DmShared.xNull(iTS_Mdl.getOverHeadManualCode()).equalsIgnoreCase("UGST") && saleBillReturnProvisional.defaultValue.isUnionTerritory()) {
                    txtSgstUgstPer.setText(DmShared.xNull(iTS_Mdl.getAtRate()));
                    txtSgstUgstPer.setTag(DmShared.xNull(iTS_Mdl.getAccountCode()));
                } else if (DmShared.xNull(iTS_Mdl.getOverHeadManualCode()).equalsIgnoreCase("TV")) {
                    txtTaxableAmt.setTag(DmShared.xNull(iTS_Mdl.getAccountCode()));
                }
            }
            if (!saleAcCode.trim().equals("")) {
                txtTaxableAmt.setTag(DmShared.xNull(saleAcCode));
            }

            calculate();

        } else if (ourRow != null) {
            if (saleBillReturnProvisional.defaultValue.isUnionTerritory()) {
                txtSgstUgstPer.setTag(DmShared.xNull(ourRow.getUgstAcCode()));
                txtSgstUgstPer.setText(DmClient.vNull(ourRow.getUgstPer(), "0.00"));
            } else {
                txtSgstUgstPer.setTag(DmShared.xNull(ourRow.getSgstAcCode()));
                txtSgstUgstPer.setText(DmClient.vNull(ourRow.getSgstPer(), "0.00"));
            }
            txtCGSTPer.setTag(DmShared.xNull(ourRow.getCgstAcCode()));
            txtCGSTPer.setText(DmClient.vNull(ourRow.getCgstPer(), "0.00"));
            txtTaxableAmt.setTag(DmShared.xNull(ourRow.getSaleAcCode()));
        }
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, SaleBillReturnProvisional saleBillReturnProvisional) {
        this.saleBillReturnProvisional = saleBillReturnProvisional;
        isRateEditable = saleBillReturnProvisional.rateEditable;
        setLabelBatchNoView();
        super.showCenter(currentRowIndex, okNeeded, newNeeded, deleteNeeded);
    }

    private void setLabelBatchNoView() {
        if (DmShared.iNull(saleBillReturnProvisional.txtHSaleBillNo.getTag()).longValue() == 0) {
            if (isAllowNegativeStock) {
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
    }

    @Override
    public void displayRow(T1 row) {
        clear();
        GrdMedicine_Mdl ourRow = (GrdMedicine_Mdl) row;
        hashCode = ourRow.getSharedKeyFieldHashCode();
        medicineMdl = ourRow.getMedicineMdl();
        saleAcCode = DmShared.xNull(ourRow.getSaleAcCode());
        txtTotalQty.setEnabled(false);
        txtFreeQty.setEnabled(false);
        if (DmShared.vNull(ourRow.getFreeQty()).doubleValue() > 0) {
            txtFreeQty.setEnabled(true);
        } else {
            txtTotalQty.setEnabled(true);
        }
        if (medicineMdl != null) {
            selectedMedicine = DmShared.xNull(medicineMdl.getSharedKeyFieldCode());
            txtMedicineName.setTag(DmShared.xNull(medicineMdl.getSharedKeyFieldCode()));
            txtMedicineName.setText(DmShared.xNull(medicineMdl.getSharedKeyFieldName()));
            txtMedicineName.setTag2nd(DmShared.xNull(medicineMdl.getItemProperty()).trim());

            skuUnitName = DmShared.xNull(medicineMdl.getSkuName());
            prvQuantity = DmShared.xNull(ourRow.getPrvQuantity());
            prvFreeQty = DmShared.xNull(ourRow.getPrvFreeQuantity());
            txtTotalQty.setTag2nd(DmClient.vNull(ourRow.getSbQuantity(), DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowDecimalPlaces()).intValue())));
            txtTotalQty.setTag(DmClient.vNull(ourRow.getQuantitySKU(), DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowDecimalPlaces()).intValue())));
            txtTotalQty.setText(DmClient.vNull(ourRow.getQuantity(), DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowDecimalPlaces()).intValue())));
            txtBatchNo.setTag(DmShared.xNull(ourRow.getIbCode()));
            txtBatchNo.setText(DmShared.xNull(ourRow.getBatchNo()));
            txtBarcode.setText(DmShared.xNull(ourRow.getBarcode()));
            selectedBarCode = DmShared.xNull(ourRow.getBarcode());
            itemCodeCsv = DmShared.xNull(ourRow.getItemCodeCsv());
            isAllowNegativeStock = medicineMdl.isAllowNegativeStock();
            isBarcodeActive = medicineMdl.isBarcodeActive();

            txtManufacturingDate.setText(DmShared.dNull(ourRow.getMfgDate()));
            txtExpiryDate.setText(DmShared.dNull(ourRow.getExpDate()));
            txtMRP.setText(DmShared.xNull(ourRow.getMrp()));
            txtMRP.setTag(DmShared.xNull(ourRow.getSkuMrp()));

            txtFreeQty.setText(DmClient.vNull(ourRow.getFreeQty(), DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowDecimalPlaces()).intValue())));
//            txtFreeQty.setTag(ourRow.getSchemeOnQty());
//            txtFreeQty.setTag2nd(ourRow.getSchemeFreeQty());
            txtConversionRateMRP.setText(medicineMdl.getPurchaseUnitName());
            totalDiscount = DmShared.vNull(ourRow.getTotalDiscount());
            txtDiscountPer.setText(DmClient.vNull(medicineMdl.getDiscountPer(), "0.00"));
            txtDiscount2.setText(DmClient.vNull(ourRow.getDiscount2(), "0.00"));
            txtDiscountPer2.setText(DmClient.vNull(ourRow.getDiscountPer2(), "0.00"));
            isSaleRateEditable = DmShared.xNull(medicineMdl.getIsSaleRateEditable());
            txtConversionRate.setText("");
//            txtConversionRateMRP.setText("");
            txtFreeQtyConversionRate.setText("");
            lblSaleConversionRate.setText("");
            lblSaleConversionRate.setTitle("");
            if (DmShared.vNull(medicineMdl.getPurchaseUnitConversionRate()).doubleValue() > 0 && !DmShared.xNull(medicineMdl.getSaleUnitName()).equalsIgnoreCase("") && !DmShared.xNull(medicineMdl.getPurchaseUnitName()).equalsIgnoreCase("")) {
                if (DmShared.xNull(medicineMdl.getSkuName()).trim().equalsIgnoreCase(DmShared.xNull(medicineMdl.getSaleUnitName()))) {
                    txtConversionRate.setText(medicineMdl.getSkuName());
                    txtFreeQtyConversionRate.setText(medicineMdl.getSkuName());
//                    txtConversionRateMRP.setText(medicineMdl.getSkuName());
                } else if (!DmShared.xNull(medicineMdl.getSkuName()).trim().equalsIgnoreCase(DmShared.xNull(medicineMdl.getSaleUnitName()))) {
                    txtConversionRate.setText(medicineMdl.getSaleUnitName() + " = " + medicineMdl.getConversionRate() + " " + medicineMdl.getSkuName());
//                    txtConversionRateMRP.setText(medicineMdl.getSaleUnitName() + " = " + medicineMdl.getConversionRate() + " " + medicineMdl.getSkuName());
                    txtFreeQtyConversionRate.setText(medicineMdl.getSaleUnitName() + " = " + medicineMdl.getConversionRate() + " " + medicineMdl.getSkuName());
                    lblSaleConversionRate.setText(medicineMdl.getSaleUnitName() + " = " + medicineMdl.getConversionRate() + " " + medicineMdl.getSkuName());
                    lblSaleConversionRate.setTitle(medicineMdl.getSaleUnitName() + " = " + medicineMdl.getConversionRate() + " " + medicineMdl.getSkuName());
                }
            }
            setITS(medicineMdl.getLstITS(), ourRow);
            tempDiscountPer = DmClient.vNull(medicineMdl.getTempDiscountPer(), "0.00");  //Will executed after set ITS as it is calling calculation function
            tempDiscountPer2 = DmClient.vNull(ourRow.getTempDiscountPer2(), "0.00");  //Will executed after set ITS as it is calling calculation function
        }
        txtRate.setText(DmClient.vNull(ourRow.getRate(), "0.00"));
        txtRate.setTag(DmShared.xNull(ourRow.getSkuRate()));
        txtSkuRate.setText(DmClient.vNull(ourRow.getSkuRate(), "0.00"));
        sbDocId = DmShared.xNull(ourRow.getSbDocId());
        sbV_sNo = DmShared.xNull(ourRow.getSbV_Sno());

        txtAmount.setText(DmClient.vNull(ourRow.getAmount(), "0.00"));
        txtNetAmount.setText(DmClient.vNull(ourRow.getNetAmt(), "0.00"));
        txtTaxableAmt.setText(DmClient.vNull(ourRow.getTaxableAmt(), "0.00"));

        txtDiscount.setText(DmClient.vNull(ourRow.getDiscount(), "0.00"));
        if (saleBillReturnProvisional.defaultValue.isUnionTerritory()) {
            txtSgstUgst.setText(DmClient.vNull(ourRow.getUgstAmt(), "0.00"));
        } else {
            txtSgstUgst.setText(DmClient.vNull(ourRow.getSgstAmt(), "0.00"));
        }

        txtCGST.setText(DmClient.vNull(ourRow.getCgstAmt(), "0.00"));
        txtGstRate.setText(DmClient.vNull(ourRow.getGstPer(), "0.00"));
        txtGstAmt.setText(DmClient.vNull(ourRow.getGstAmount(), "0.00"));

        setLabelBatchNoView();
        manageDisplay();
        if (DmShared.iNull(saleBillReturnProvisional.txtHSaleBillNo.getTag()).longValue() > 0) {
            if (DmShared.vNull(txtFreeQty.getText()).doubleValue() == 0) {
                txtTotalQty.setFocus(true);
            } else {
                txtFreeQty.setFocus(true);
            }
        } else {
            if (DmShared.iNull(saleBillReturnProvisional.defaultValue.getPharmacyBarcodeActive()).intValue() == 1) {
                txtBarcode.setFocus(true);
            } else {
                txtMedicineName.setFocus(true);
            }
        }
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdMedicine_Mdl();
    }

    @Override
    public void onOk() {
        calculate();

        if (DmShared.xNull(txtMedicineName.getTag()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Medicine is Mandatory.", txtMedicineName).show();
            return;
        }
        if (DmShared.xNull(txtBatchNo.getTag()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Medicine Batch is Mandatory.", txtMedicineName).show();
            return;
        }

        if (DmShared.vNull(txtFreeQty.getText()).doubleValue() == 0) {
            if (DmShared.vNull(txtTotalQty.getText()).doubleValue() == 0) {
                new DmAlertMessageBox("Quantity is Mandatory.", txtTotalQty).show();
                return;
            }

            if (DmShared.vNull(txtRate.getText()).doubleValue() == 0) {
                new DmAlertMessageBox("Rate is Mandatory.", txtRate).show();
                return;
            }
            if (DmShared.vNull(txtAmount.getText()).doubleValue() == 0) {
                new DmAlertMessageBox("Amount is Mandatory.", txtRate).show();
                return;
            }
            if (DmShared.vNull(txtAmount.getText()).doubleValue() < (DmShared.vNull(txtDiscount.getText()).doubleValue() + DmShared.vNull(txtDiscount2.getText()).doubleValue())) {
                if (txtDiscount.isEnabled()) {
                    new DmAlertMessageBox("Discount cannot be greater than amount.", txtDiscount).show();
                } else if (txtDiscount2.isEnabled()) {
                    new DmAlertMessageBox("Discount cannot be greater than amount.", txtDiscount2).show();
                } else {
                    new DmAlertMessageBox("Discount cannot be greater than amount.", txtTotalQty).show();
                }
                return;
            }
        }

        if (DmShared.iNull(saleBillReturnProvisional.txtHSaleBillNo.getTag()).longValue() > 0) {
            if (DmShared.vNull(txtTotalQty.getText()).doubleValue() > DmShared.vNull(txtTotalQty.getTag2nd()).doubleValue()) {
                new DmAlertMessageBox("Quantity cannot be greater than sale quantity.", txtTotalQty).show();
                return;
            }
        }

        if (DmShared.iNull(medicineMdl.getRestrictGroup()).longValue() > 0) {
            if (!saleBillReturnProvisional.txtHIdType.isEnabled()) {
                if (DmShared.xNull(saleBillReturnProvisional.txtHIdType.getText()).equalsIgnoreCase("")) {
                    saleBillReturnProvisional.txtHIdType.setEnabled(true);
                }
            }
            if (!saleBillReturnProvisional.txtIdNo.isEnabled()) {
                if (DmShared.xNull(saleBillReturnProvisional.txtIdNo.getText()).equalsIgnoreCase("")) {
                    saleBillReturnProvisional.txtIdNo.setEnabled(true);
                }
            }
            if (!saleBillReturnProvisional.txtAddress.isEnabled()) {
                if (DmShared.xNull(saleBillReturnProvisional.txtAddress.getText()).equalsIgnoreCase("")) {
                    saleBillReturnProvisional.txtAddress.setEnabled(true);
                }
            }
            if (!saleBillReturnProvisional.txtMobile.isEnabled()) {
                if (DmShared.xNull(saleBillReturnProvisional.txtMobile.getText()).equalsIgnoreCase("")) {
                    saleBillReturnProvisional.txtMobile.setEnabled(true);
                }
            }
        }

        for (GrdMedicine_Mdl detail : (List<GrdMedicine_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
            if (detail.getSharedKeyFieldHashCode() != hashCode) {
                if (DmShared.xNull(detail.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(txtMedicineName.getTag()))
                        && DmShared.xNull(detail.getIbCode()).equalsIgnoreCase(DmShared.xNull(txtBatchNo.getTag()))) {
                    if (DmShared.vNull(txtFreeQty.getText()).doubleValue() != 0) {
                        if (DmShared.vNull(detail.getFreeQty()).doubleValue() != 0) {
                            new DmAlertMessageBox("Medicine with this batch already exists.", txtMedicineName).show();
                            return;
                        }
                    } else if (DmShared.vNull(txtTotalQty.getText()).doubleValue() != 0) {
                        if (DmShared.vNull(detail.getQuantity()).doubleValue() != 0) {
                            new DmAlertMessageBox("Medicine with this batch already exists.", txtMedicineName).show();
                            return;
                        }
                    }

                }
            }
        }
        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {

        GrdMedicine_Mdl ourRow = (GrdMedicine_Mdl) row;

        medicineMdl.setTempDiscountPer(DmClient.vNull(tempDiscountPer, DmShared.getNumberFormat(7)));
        ourRow.setTempDiscountPer2(DmClient.vNull(tempDiscountPer2, DmShared.getNumberFormat(7)));
        medicineMdl.setDiscountPer(DmClient.vNull(txtDiscountPer.getText(), "0.00"));
        ourRow.setMedicineMdl(medicineMdl);
        ourRow.setSharedKeyFieldCode(medicineMdl.getSharedKeyFieldCode());
        ourRow.setSharedKeyFieldName(medicineMdl.getSharedKeyFieldName());
        ourRow.setUnit(medicineMdl.getSaleUnitName());
        ourRow.setIbCode(DmShared.xNull(txtBatchNo.getTag()));
        ourRow.setBatchNo(DmShared.xNull(txtBatchNo.getText()));
        ourRow.setBarcode(DmShared.xNull(txtBarcode.getText()));
        ourRow.setItemCodeCsv(DmShared.xNull(itemCodeCsv));
        ourRow.setBestBefore(medicineMdl.isBestBefore());
        ourRow.setExpDate(DmShared.dNull(txtExpiryDate.getText()));
        ourRow.setMfgDate(DmShared.dNull(txtManufacturingDate.getText()));

        ourRow.setPrvQuantity(DmShared.xNull(prvQuantity));
        ourRow.setPrvFreeQuantity(DmShared.xNull(prvFreeQty));
        ourRow.setFreeQty(DmClient.vNull(txtFreeQty.getText(), DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowDecimalPlaces()).intValue())));

        ourRow.setQuantity(DmClient.vNull(txtTotalQty.getText(), DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowDecimalPlaces()).intValue())));
        ourRow.setSbQuantity(DmClient.vNull(txtTotalQty.getTag2nd(), DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowDecimalPlaces()).intValue())));
        ourRow.setQuantitySKU(DmClient.vNull((DmShared.vNull(txtTotalQty.getTag()).doubleValue()), DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowSkuDecimalPlaces()).intValue())));
        ourRow.setRate(DmClient.vNull(txtRate.getText(), "0.00"));
        ourRow.setMrp(DmClient.vNull(txtMRP.getText(), "0.00"));
        ourRow.setSkuMrp(DmShared.xNull(txtMRP.getTag()));
        ourRow.setSkuRate(DmShared.xNull(txtRate.getTag()));
        ourRow.setSbDocId(DmShared.xNull(sbDocId));
        ourRow.setSbV_Sno(DmShared.xNull(sbV_sNo));
        ourRow.setAmount(DmClient.vNull(txtAmount.getText(), "0.00"));
        ourRow.setTaxableAmt(DmClient.vNull(txtTaxableAmt.getText(), "0.00"));
        ourRow.setDiscount(DmClient.vNull(txtDiscount.getText(), "0.00"));
        ourRow.setDiscount2(DmClient.vNull(txtDiscount2.getText(), "0.00"));
        ourRow.setDiscountPer2(DmClient.vNull(txtDiscountPer2.getText(), "0.00"));
        ourRow.setTotalDiscount(DmClient.vNull(totalDiscount, "0.00"));
        ourRow.setCgstAmt(DmClient.vNull(txtCGST.getText(), "0.00"));
        ourRow.setCgstPer(DmClient.vNull(txtCGSTPer.getText(), "0.00"));
        ourRow.setCgstAcCode(DmShared.xNull(txtCGSTPer.getTag()));
        if (saleBillReturnProvisional.defaultValue.isUnionTerritory()) {
            ourRow.setUgstAmt(DmClient.vNull(txtSgstUgst.getText(), "0.00"));
            ourRow.setUgstPer(DmClient.vNull(txtSgstUgstPer.getText(), "0.00"));
            ourRow.setUgstAcCode(DmShared.xNull(txtSgstUgstPer.getTag()));
        } else {
            ourRow.setSgstAmt(DmClient.vNull(txtSgstUgst.getText(), "0.00"));
            ourRow.setSgstPer(DmClient.vNull(txtSgstUgstPer.getText(), "0.00"));
            ourRow.setSgstAcCode(DmShared.xNull(txtSgstUgstPer.getTag()));
        }

        ourRow.setGstPer(DmClient.vNull(DmShared.vNull(txtGstRate.getText()).doubleValue(), "0.00"));
        ourRow.setGstAmount(DmClient.vNull(DmShared.vNull(txtGstAmt.getText()).doubleValue(), "0.00"));

        ourRow.setNetAmt(DmClient.vNull(txtNetAmount.getText(), "0.00"));
        ourRow.setSaleAcCode(DmShared.xNull(txtTaxableAmt.getTag()));

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

    public MedicineDetailsEdit(DmGrid<T1, T2> grid) {
        super(grid, "Medicine Details Edit", "410px", "");
        srvAsync = new ServiceInvoker<>(GWT.create(SaleBillReturnProvisional_Srv.class), SaleBillReturnProvisional.class.getName());
        flwForm.addAttachHandler(this);
        manageEvents();
        textHelpEvents();
    }

    private void manageEvents() {
        txtTotalQty.addBlurHandler(this);
        txtFreeQty.addBlurHandler(this);
        txtBatchNo.addKeyUpHandler(this);
        txtBatchNo.addKeyDownHandler(this);
        txtRate.addBlurHandler(this);
        txtDiscount.addFocusHandler(this);
        txtDiscount.addBlurHandler(this);
        txtDiscountPer.addFocusHandler(this);
        txtDiscountPer.addBlurHandler(this);
        txtDiscount2.addBlurHandler(this);
        txtDiscountPer2.addBlurHandler(this);
        txtBarcode.addBlurHandler(this);
        lblBatchNo.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                showAddBatch();
            }
        });
        btnBatchNo.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                showBatchDetailPopup();
            }
        });
    }

    private void showAddBatch() {
        if (saleBillReturnProvisional.isAllowAddBatch && isAllowNegativeStock) {
            if (DmShared.iNull(saleBillReturnProvisional.txtHSaleBillNo.getTag()).longValue() == 0) {
                addBatchPopup.show(DmShared.xNull(txtMedicineName.getTag()), DmShared.xNull(saleBillReturnProvisional.txtHDepartment.getTag()), DmShared.dNull(saleBillReturnProvisional.txtDate.getText()), medicineMdl.isBestBefore(), isBarcodeActive, DmShared.vNull(medicineMdl.getPurchaseUnitConversionRate()).toString(), DmShared.xNull(saleBillReturnProvisional.topControl.getMode()), skuUnitName, DmShared.xNull(medicineMdl.getPurchaseUnitName()));
            }
        }
    }

    @Override
    public void onFocus(FocusEvent event) {
        if (event.getSource() == txtDiscountPer || event.getSource() == txtDiscount) {
            txtDiscount.setTag(txtDiscount.getText());
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtTotalQty || event.getSource() == txtFreeQty || event.getSource() == txtRate || event.getSource() == txtDiscountPer2 || event.getSource() == txtDiscount2) {
            calculate();
        } else if (event.getSource() == txtDiscountPer) {
            calculate();
            if (DmShared.vNull(txtDiscount.getText()).doubleValue() != DmShared.vNull(txtDiscount.getTag()).doubleValue()) {
                tempDiscountPer = "";
            }
        } else if (event.getSource() == txtDiscount) {
            if (DmShared.vNull(txtDiscount.getText()).doubleValue() != DmShared.vNull(txtDiscount.getTag()).doubleValue()) {
                tempDiscountPer = "";
            }
            calculate();
        } else if (event.getSource() == txtRate) {
            if (DmShared.iNull(medicineMdl.getItsBasedOn()).intValue() == 1) {
                verifyITS(txtMedicineName.getTag(), DmShared.vNull(txtRate.getText()).doubleValue(), medicineMdl.getItsBasedOn(), DmShared.dNull(saleBillReturnProvisional.txtDate.getText()));
            } else {
                calculate();
            }
        } else if (event.getSource() == txtBarcode) {
            if (!selectedBarCode.equalsIgnoreCase(DmShared.xNull(txtBarcode.getText())) || DmShared.iNull(txtMedicineName.getTag()).longValue() == 0) {
                txtMedicineName.setTag("");
                txtMedicineName.setText("");
                clearOnMedicineChange();
                if (!DmShared.xNull(txtBarcode.getText()).trim().equals("") && !saleBillReturnProvisional.topControl.getMode().equalsIgnoreCase("Browse")) {
                    barCodeSearch();
                } else {
                    itemCodeCsv = "";
                }
            }
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            onClose();
        } else if (rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_B) {
            if (saleBillReturnProvisional.isAllowAddBatch) {
                showAddBatch();
            }
        }

    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    private void calculate() {
        double purchaseUnitConversionRate = 0, saleUnitConversionRate = 0, itemValue = 0, totalTaxRate = 0, taxableAmt = 0, totalTaxAmt = 0;
        double tempSkuRate = 0, saleSkuQty = 0, freeSkuQty = 0, totalSkuQty = 0;
        int saleUnitDecimalPlaces = 4, skuDecimalPlaces = 4;
        double tempQty = DmShared.vNull(txtTotalQty.getText()).doubleValue();

        totalDiscount = 0;

        if (medicineMdl != null) {
            saleUnitDecimalPlaces = medicineMdl.getAllowDecimalPlaces();
            skuDecimalPlaces = medicineMdl.getAllowSkuDecimalPlaces();
            saleUnitConversionRate = DmShared.vNull(medicineMdl.getConversionRate()).doubleValue();
            purchaseUnitConversionRate = DmShared.vNull(medicineMdl.getPurchaseUnitConversionRate()).doubleValue();
        }

        if (purchaseUnitConversionRate > 0) {
            tempSkuRate = DmShared.vNull(txtRate.getText()).doubleValue() / purchaseUnitConversionRate;
        } else {
            tempSkuRate = 0;
        }

        if (DmShared.vNull(txtRate.getTag()).doubleValue() != tempSkuRate) {
            txtRate.setTag(DmClient.vNull(tempSkuRate, DmShared.getNumberFormat(7)));
            txtSkuRate.setText(DmClient.vNull(tempSkuRate, "0.00"));
        }

        if (tempQty > 0) {
            txtTotalQty.setText(DmShared.vNull(tempQty).toString());
        }

        txtTotalQty.setText(DmClient.vNull(txtTotalQty.getText(), DmShared.getNumberFormat(DmShared.iNull(saleUnitDecimalPlaces).intValue())));
        txtFreeQty.setText(DmClient.vNull(txtFreeQty.getText(), DmShared.getNumberFormat(DmShared.iNull(saleUnitDecimalPlaces).intValue())));
//        txtTotalQty.setTag(DmClient.vNull((DmShared.vNull(txtTotalQty.getText()).doubleValue() * saleUnitConversionRate), DmShared.getNumberFormat(DmShared.iNull(skuDecimalPlaces).intValue())));
        saleSkuQty = DmShared.vNull(DmClient.vNull((DmShared.vNull(txtTotalQty.getText()).doubleValue() * saleUnitConversionRate), DmShared.getNumberFormat(DmShared.iNull(skuDecimalPlaces).intValue()))).doubleValue();
        freeSkuQty = DmShared.vNull(DmClient.vNull((DmShared.vNull(txtFreeQty.getText()).doubleValue() * saleUnitConversionRate), DmShared.getNumberFormat(DmShared.iNull(skuDecimalPlaces).intValue()))).doubleValue();
        totalSkuQty = DmShared.vNull(DmClient.vNull((saleSkuQty + freeSkuQty), DmShared.getNumberFormat(DmShared.iNull(skuDecimalPlaces).intValue()))).doubleValue();
        txtTotalQty.setTag(DmShared.vNull(totalSkuQty).toString());

        txtAmount.setText(DmClient.vNull((DmShared.vNull(saleSkuQty).doubleValue() * DmShared.vNull(txtRate.getTag()).doubleValue()), "0.00"));
        if (DmShared.vNull(txtDiscountPer.getText()).doubleValue() > 0) {
            txtDiscount.setText(DmClient.vNull((DmShared.vNull(txtAmount.getText()).doubleValue() * DmShared.vNull(txtDiscountPer.getText()).doubleValue() * 0.01), "0.00"));
        } else {
            if (DmShared.vNull(tempDiscountPer).doubleValue() > 0) {
                txtDiscount.setText(DmClient.vNull((DmShared.vNull(txtAmount.getText()).doubleValue() * DmShared.vNull(tempDiscountPer).doubleValue() * 0.01), "0.00"));
            } else {
                txtDiscount.setText(DmClient.vNull(DmShared.vNull(txtDiscount.getText()), "0.00"));
            }
        }
        if (DmShared.vNull(txtDiscountPer2.getText()).doubleValue() > 0) {
            double tempAmt = 0;
            tempAmt = DmShared.vNull(txtAmount.getText()).doubleValue() - (DmShared.vNull(txtDiscount.getText()).doubleValue());
            txtDiscount2.setText(DmClient.vNull((tempAmt * DmShared.vNull(txtDiscountPer2.getText()).doubleValue() * 0.01), "0.00"));
        } else {
            if (DmShared.vNull(tempDiscountPer2).doubleValue() > 0) {
                txtDiscount2.setText(DmClient.vNull((DmShared.vNull(txtAmount.getText()).doubleValue() * DmShared.vNull(tempDiscountPer2).doubleValue() * 0.01), "0.00"));
            } else {
                txtDiscount2.setText(DmClient.vNull(DmShared.vNull(txtDiscount2.getText()), "0.00"));
            }
        }

        totalDiscount = DmShared.vNull(txtDiscount.getText()).doubleValue() + DmShared.vNull(txtDiscount2.getText()).doubleValue();
        itemValue = DmShared.vNull(txtAmount.getText()).doubleValue() - DmShared.vNull(totalDiscount).doubleValue();
        totalTaxRate = DmShared.vNull(txtCGSTPer.getText()).doubleValue() + DmShared.vNull(txtSgstUgstPer.getText()).doubleValue();
        taxableAmt = (itemValue * 100.00) / (100.00 + totalTaxRate);

        txtCGST.setText(DmClient.vNull((taxableAmt * DmShared.vNull(txtCGSTPer.getText()).doubleValue() * 0.01), "0.00"));
        txtSgstUgst.setText(DmClient.vNull((taxableAmt * DmShared.vNull(txtSgstUgstPer.getText()).doubleValue() * 0.01), "0.00"));
        txtTaxableAmt.setText(DmClient.vNull(taxableAmt, "0.00"));
        txtNetAmount.setText(DmClient.vNull(itemValue, "0.00"));
        totalTaxAmt = DmShared.vNull(txtCGST.getText()).doubleValue() + DmShared.vNull(txtSgstUgst.getText()).doubleValue();

        txtGstRate.setText(DmClient.vNull(totalTaxRate, "0.00"));
        txtGstAmt.setText(DmClient.vNull(totalTaxAmt, "0.00"));

        //for managing minor 'paisa' differences in amount.
        if (DmShared.vNull(txtNetAmount.getText()).doubleValue() != (DmShared.vNull(txtTaxableAmt.getText()).doubleValue() + totalTaxAmt)) {
            taxableAmt = DmShared.vNull(txtTaxableAmt.getText()).doubleValue() + (DmShared.vNull(txtNetAmount.getText()).doubleValue() - (DmShared.vNull(txtTaxableAmt.getText()).doubleValue() + totalTaxAmt));
            txtTaxableAmt.setText(DmClient.vNull(taxableAmt, "0.00"));
        }
    }

    private void clear() {
        medicineMdl = null;
        selectedMedicine = "";
        prvQuantity = "";
        prvFreeQty = "";
        txtMedicineName.setTag("");
        txtMedicineName.setText("");
        txtMedicineName.setTag2nd("");
        txtConversionRate.setText("");
        txtConversionRateMRP.setText("");
        lblSaleConversionRate.setText("");
        lblSaleConversionRate.setTitle("");
        txtFreeQtyConversionRate.setText("");
        txtBarcode.setText("");
        selectedBarCode = "";
        itemCodeCsv = "";
        clearOnMedicineChange();
    }

    private void manageDisplay() {
        txtCGST.setEnabled(false);
        txtCGSTPer.setEnabled(false);
        txtSgstUgst.setEnabled(false);
        txtSgstUgstPer.setEnabled(false);
        txtTaxableAmt.setEnabled(false);
        txtGstAmt.setEnabled(false);
        txtGstRate.setEnabled(false);
        txtNetAmount.setEnabled(false);
        txtAmount.setEnabled(false);
        txtConversionRate.setReadOnly(true);
        txtFreeQtyConversionRate.setReadOnly(true);
        managaSaleRate();
        txtBatchNo.setEnabled(false);
        txtMRP.setEnabled(false);
        txtManufacturingDate.setEnabled(false);
        txtExpiryDate.setEnabled(false);
        txtSkuRate.setEnabled(false);

        lblSgstUgst.setText("");
        if (saleBillReturnProvisional.defaultValue.isUnionTerritory()) {
            lblSgstUgst.setText("UGST Amount");
        } else {
            lblSgstUgst.setText("SGST Amount");
        }

        if (DmShared.iNull(saleBillReturnProvisional.txtHSaleBillNo.getTag()).longValue() > 0) {
            txtMedicineName.setEnabled(false);
            txtBarcode.setEnabled(false);
        } else {
            txtMedicineName.setEnabled(true);
            txtBarcode.setEnabled(DmShared.iNull(saleBillReturnProvisional.defaultValue.getPharmacyBarcodeActive()).intValue() == 1);
        }

    }

    private void showBatchDetailPopup() {
        if (!saleBillReturnProvisional.topControl.getMode().equalsIgnoreCase("Browse")) {
            if (DmShared.iNull(saleBillReturnProvisional.txtHSaleBillNo.getTag()).longValue() == 0) {
                if (DmShared.iNull(txtMedicineName.getTag()).longValue() != 0) {
//                    batchDetail.show(txtMedicineName.getTag(), saleBillReturnProvisional.txtHDepartment.getTag(), saleBillReturnProvisional.txtDate.getText(), "", false, false, DmShared.xNull(txtBarcode.getText()));
                    batchDetail.show(txtMedicineName.getTag(), saleBillReturnProvisional.txtHDepartment.getTag(), saleBillReturnProvisional.txtDate.getText(), "", ItemBatchType.NON_EXPIRED_BATCH, true, DmShared.xNull(txtBarcode.getText()), saleBillReturnProvisional.saleOn, "", false);
                } else {
                    new DmAlertMessageBox("Please select Medicine Name.", txtMedicineName).show();
                }
            }
        }
    }

    private void barCodeSearch() {
        srvAsync.invokeRPC().getBarCodeSearch(DmShared.xNull(txtBarcode.getText()), DmShared.dNull(saleBillReturnProvisional.txtDate.getText()), DmShared.iNull(saleBillReturnProvisional.txtHDepartment.getTag()).toString(), DmShared.iNull(saleBillReturnProvisional.defaultValue.getSiteCode()).toString(), saleBillReturnProvisional.saleOn, new AsyncCallback<DmResult<BarCode_Mdl>>() {

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
//                        setMedSelection(data.getMedicineDetail(), false);
                        onMedicineSelection(data.getMedicineDetail(), false);
                        if (data.getBatchDetails() != null) {
                            setBatchModel(data.getBatchDetails());
                        } else {
                            showBatchDetailPopup();
                        }

                    } else if (!DmShared.xNull(data.getItemCsv()).trim().equals("")) {
                        showMedicineSearchPopUp("");
                    } else {
                        clear();

                    }
                    selectedBarCode = DmShared.xNull(txtBarcode.getText());

                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtMedicineName).show();
                }
            }
        });
    }

    private void showMedicineSearchPopUp(String searchChar) {
        MedicineSearchFilter_Mdl filter = new MedicineSearchFilter_Mdl();
        filter.setMedicineCode(DmShared.xNull(txtMedicineName.getTag()));
        filter.setDepartmentCode(DmShared.xNull(saleBillReturnProvisional.txtHDepartment.getTag()));
        filter.setDepartmentName(DmShared.xNull(saleBillReturnProvisional.txtHDepartment.getText()));
        filter.setV_Date(DmShared.dNull(saleBillReturnProvisional.txtDate.getText()));
        filter.setItemCodeCsv(DmShared.xNull(itemCodeCsv));
        medicineSearch.show(filter, searchChar, saleBillReturnProvisional.txtHType.getTag());
    }

    private void setBatchModel(BatchNo_Mdl batchDetail) {
        TxtHBatch_Mdl batch_Mdl = new TxtHBatch_Mdl();
        batch_Mdl.setSaleRate(DmShared.xNull(batchDetail.getSaleRate()));
        batch_Mdl.setMrp(DmShared.xNull(batchDetail.getMrp()));
        batch_Mdl.setSharedKeyFieldCode(DmShared.xNull(batchDetail.getIbCode()));
        batch_Mdl.setSharedKeyFieldName(DmShared.xNull(batchDetail.getBatchNo()));
        batch_Mdl.setExpDate(DmShared.dNull(batchDetail.getExpiryDate()));
        batch_Mdl.setMfgDate(DmShared.dNull(batchDetail.getManufacturingDate()));
        batch_Mdl.setSkuSaleRate(DmShared.xNull(batchDetail.getSkuSaleRate()));
        batch_Mdl.setSkuMrp(DmShared.xNull(batchDetail.getSkuMrp()));
        batch_Mdl.setBarcode(DmShared.xNull(batchDetail.getBarCode()));
        setBatchDetail(batch_Mdl);
    }

}
