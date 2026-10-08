package aarogya.pharmacy.saleBillWholeSale.client;

import aarogya.pharmacy.addBatchSingle.client.AddBatchSingle;
import aarogya.pharmacy.addBatchSingle.shared.TxtHBatch_Mdl;
import aarogya.pharmacy.batchNo.client.BatchNo;
import aarogya.pharmacy.batchNo.shared.BatchNo_Mdl;
import aarogya.pharmacy.medicineSearch.client.MedicineSearch;
import aarogya.pharmacy.medicineSearch.shared.MedicineSearchFilter_Mdl;
import aarogya.pharmacy.medicineSearch.shared.MedicineSearch_Mdl;
import aarogya.pharmacy.saleBillWholeSale.shared.*;
import aarogya.pharmacy.self.shared.BarCode_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
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
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmBase.dmBaseStatic.client.*;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.overHeadLine.shared.OverHeadITS_Mdl;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MedicineDetailsEdit<T1, T2> extends MedicineDetailsEdit_Dsg<T1, T2> implements BlurHandler, DmRootEvents, AttachEvent.Handler, KeyUpHandler, KeyDownHandler {

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

        public void onOk(GrdMedicine_Mdl grdtest, List<GrdMedicine_Mdl> lstMdl);

        public void onDelete();
    }

    private final ServiceInvoker<SaleBillWholeSale_SrvAsync> srvAsync;
    private CallBack callBack;
    protected SaleBillWholeSale parentClass;
    protected int hashCode = 0;
    private String saleAcCode = "";
    private String selectedMedicine = "", skuUnitName = "";
    private String selectedMedicineToFill = "", selectedMedicineNameToFill = "";
    protected TxtHMedicine_Mdl medicineMdl = new TxtHMedicine_Mdl();
    private final AddBatchSingle addBatchPopup = new AddBatchSingle();
    boolean isRateEditable = false, isBarcodeActive = false;
    String isSaleRateEditable = "";
    boolean isReturnableItem = false, isDiscountAllow = false, isEstimate = false, isAllowNegativeStock = false;
    private String discountGivenByTag = "", discountReason = "", gstPer = "", gstAmt = "";
    private final BatchNo batchDetail = new BatchNo();
    String selectedBatchSQL = "", currentBatchQtyInSku = "", freeWithItemCode = "", currentBatchQtyInPurchaseUnit = "";
    double totalQtyBatchList = 0, totalQtyInSKu = 0, totalDiscount = 0;
    List<BatchNo_Mdl> lstMdlBatch;
    List<GrdMedicine_Mdl> lstMdlGrd;
    List<String> selectedIbCodes;
    String batchQtyInPurchaseUnit = "";
    private String selectedBarCode = "";
    private String itemCodeCsv = "";
    private String location = "";

    private final MedicineSearch medicineSearch = new MedicineSearch(null, null);

    private void textHelpEvents() {
        batchDetail.addCallBack(new BatchNo.CallBack() {
            @Override
            public void ok(BatchNo_Mdl batchDetail, List<BatchNo_Mdl> lstBatchDetails) {
                resetBatchDetail();
                totalQtyBatchList = 0;
                if (batchDetail != null) {
                    lstMdlBatch = new ArrayList<>();
                    lstMdlBatch.addAll(lstBatchDetails);
                    currentBatchQtyInSku = batchDetail.getBalanceQty();
                    setBatchModel(batchDetail);
                    if (DmShared.vNull(batchDetail.getBalanceQty()) > 0) {
                        batchQtyInPurchaseUnit = batchDetail.getBalanceQtyInPurcUnit();
                    }
                    for (BatchNo_Mdl mdl : lstBatchDetails) {
                        totalQtyBatchList += DmShared.vNull(mdl.getBalanceQty());
                    }
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
            public void ok(final MedicineSearch_Mdl medSearchMdl) {
                txtMedicineName.setTag(DmShared.xNull(medSearchMdl.getSharedKeyFieldCode()));
                txtMedicineName.setText(DmShared.xNull(medSearchMdl.getSharedKeyFieldName()));
                if (DmShared.iNull(selectedMedicine).longValue() != DmShared.iNull(medSearchMdl.getSharedKeyFieldCode()).longValue()) {
                    if (parentClass.freeWithItems_Mdl != null) {
                        if ((parentClass.freeWithItems_Mdl.getLstFreeWithItemCodes().contains(selectedMedicine)) && (DmShared.iNull(selectedMedicine).longValue() != 0)) {
                            new DmConfirmMessageBox("Scheme Exists with previous selected medicine.Do you wish to continue ?", new DialogHideEvent.DialogHideHandler() {
                                @Override
                                public void onDialogHide(DialogHideEvent event) {
                                    if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                                        fillMedicineDetail(medSearchMdl, true, true);
                                    } else if (event.getHideButton() == Dialog.PredefinedButton.NO) {
                                        txtMedicineName.setTag(selectedMedicineToFill);
                                        txtMedicineName.setText(selectedMedicineNameToFill);
                                    }
                                }
                            }).show();
                        } else {
                            fillMedicineDetail(medSearchMdl, true, true);
                        }
                    } else {
                        fillMedicineDetail(medSearchMdl, true, true);
                    }
                } else {
                    fillMedicineDetail(medSearchMdl, false, false);
                }
                txtMedicineName.setFocus(true);
            }

            @Override
            public void close() {
                txtMedicineName.setFocus(true);
            }
        });
    }

    private void fillMedicineDetail(MedicineSearch_Mdl medSearchMdl, boolean clearFlag, boolean showBatchFlag) {
        if (clearFlag) {
            clearOnMedicineChange();
        }
        //<editor-fold defaultstate="collapsed" desc="Set Medicine Mdl">
        medicineMdl = new TxtHMedicine_Mdl();
        medicineMdl.setSharedKeyFieldCode(medSearchMdl.getSharedKeyFieldCode());
        medicineMdl.setSharedKeyFieldName(medSearchMdl.getSharedKeyFieldName());
        medicineMdl.setMedicineCategory(medSearchMdl.getCategoryCode());
        medicineMdl.setMedicineGroupCode(medSearchMdl.getGroupCode());
        medicineMdl.setMedicineGroupName(medSearchMdl.getGroupName());
        medicineMdl.setSkuCode(medSearchMdl.getSkuCode());
        medicineMdl.setSkuName(medSearchMdl.getSkuName());
        medicineMdl.setCompositionCode(medSearchMdl.getCompositionCode());
        medicineMdl.setManualCode(medSearchMdl.getManualCode());
        medicineMdl.setCompositionName(medSearchMdl.getCompositionName());
        medicineMdl.setItemProperty(medSearchMdl.getPropertyList());
        medicineMdl.setAllowDecimalPlaces(DmShared.iNull(medSearchMdl.getPurchaseUnitDecimalPlaces()).intValue());
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
        medicineMdl.setIsSaleRateEditable(medSearchMdl.getIsSaleRateEditable());
        medicineMdl.setReturnableMedicine(DmShared.iNull(medSearchMdl.getIsReturnableItem()).intValue() == 1);
        medicineMdl.setAllowNegativeStock(DmShared.iNull(medSearchMdl.getIsAllowNegativeStock()).intValue() == 1);
        medicineMdl.setBarcodeActive(DmShared.iNull(medSearchMdl.getIsBarcodeActive()).intValue() == 1);
        medicineMdl.setDiscountAllow(DmShared.iNull(medSearchMdl.getIsDiscountAllow()).intValue() == 1);
        medicineMdl.setSchemeExists(!DmShared.xNull(medSearchMdl.getSaleSchemeCode()).trim().equals(""));
        medicineMdl.setGstUnit(DmShared.xNull(medSearchMdl.getPurchaseUnitGstUnitCode()));
        //</editor-fold>
        isReturnableItem = medicineMdl.isReturnableMedicine();
        isAllowNegativeStock = medicineMdl.isAllowNegativeStock();
        isBarcodeActive = medicineMdl.isBarcodeActive();
        isDiscountAllow = parentClass.discountEditable && medicineMdl.isDiscountAllow();
        txtDiscount.setEnabled(isDiscountAllow);
        txtDiscountPer.setEnabled(isDiscountAllow);
        selectedMedicine = DmShared.xNull(medicineMdl.getSharedKeyFieldCode());
        txtMedicineName.setTag2nd(DmShared.xNull(medicineMdl.getItemProperty()));
        skuUnitName = DmShared.xNull(medicineMdl.getSkuName());
        isSaleRateEditable = DmShared.xNull(medicineMdl.getIsSaleRateEditable());
        txtPurchaseUnitName.setText(medicineMdl.getPurchaseUnitName());
        txtPurchaseUnitName.setTag(medicineMdl.getPurchaseUnit());
        txtConversionRateMRP.setText(medicineMdl.getPurchaseUnitName());

        manageSaleRate();

        setBatchLabelDesign();
        txtConversionRate.setText("");
        txtFreeQtyConversionRate.setText("");
        if (DmShared.vNull(medicineMdl.getPurchaseUnitConversionRate()).doubleValue() > 0 && !DmShared.xNull(medicineMdl.getPurchaseUnitName()).equalsIgnoreCase("") && !DmShared.xNull(medicineMdl.getPurchaseUnitName()).equalsIgnoreCase("")) {
            if (DmShared.xNull(medicineMdl.getSkuName()).trim().equalsIgnoreCase(DmShared.xNull(medicineMdl.getPurchaseUnitName()))) {
                txtConversionRate.setText(medicineMdl.getPurchaseUnitName());
                txtFreeQtyConversionRate.setText(medicineMdl.getPurchaseUnitName());
            } else if (!DmShared.xNull(medicineMdl.getPurchaseUnitName()).trim().equalsIgnoreCase(DmShared.xNull(medicineMdl.getPurchaseUnitName()))) {
                txtConversionRate.setText(medicineMdl.getPurchaseUnitName() + " = " + medicineMdl.getPurchaseUnitConversionRate() + " " + medicineMdl.getSkuName());
                txtFreeQtyConversionRate.setText(medicineMdl.getPurchaseUnitName() + " = " + medicineMdl.getPurchaseUnitConversionRate() + " " + medicineMdl.getSkuName());

            }
        }
        txtDiscountPer.setText(DmClient.vNull(medicineMdl.getDiscountPer(), "0.00"));

        verifyITS(medicineMdl.getSharedKeyFieldCode(), DmShared.vNull(txtWholeRate.getText()).doubleValue(), medicineMdl.getItsBasedOn(), DmShared.dNull(parentClass.txtDate.getText()), location);

        if (showBatchFlag) {
            showBatchDetailPopup();
        }
    }

    private void manageSaleRate() {

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
        txtManufacturingDate.setTag("");
        txtManufacturingDate.setTag2nd("");
        txtManufacturingDate.setText("");
        txtExpiryDate.setTag("");
        txtExpiryDate.setTag2nd("");
        txtExpiryDate.setText("");
        txtMRP.setTag("0");
        txtMRP.setTag2nd("");
        txtMRP.setText("");
        txtRate.setTag("");
        txtRate.setTag2nd("");
        txtRate.setText("0");
        txtWholeRate.setTag("");
        txtWholeRate.setTag2nd("");
        txtWholeRate.setText("0");

    }

    private void clearOnMedicineChange() {
        txtDiscountPer.setText("0.00");
        txtDiscountPer.setTag("");
        txtDiscountPer.setTag2nd("");
        txtDiscount.setText("0.00");
        txtDiscount.setTag("");
        txtDiscount.setTag2nd("");
        txtDiscountPer2.setText("0.00");
        txtDiscountPer2.setTag("");
        txtDiscountPer2.setTag2nd("");
        txtDiscount2.setText("0.00");
        txtDiscount2.setTag("");
        txtDiscount2.setTag2nd("");
        txtDiscountPerUnit.setText("");
        txtDiscountPerUnit.setTag("");
        txtDiscountPerUnit.setTag2nd("");
        txtAmount.setText("0");
        txtAmount.setTag("");
        txtAmount.setTag2nd("");
        txtTotalQty.setText("0");
        txtTotalQty.setTag("");
        txtTotalQty.setTag2nd("");
        txtFreeQty.setText("0");
        txtFreeQty.setTag("");
        txtFreeQty.setTag2nd("");
        txtBatchNo.setTag("");
        txtBatchNo.setTag2nd("");
        txtBatchNo.setText("");
        txtNetAmount.setText("0.00");
        txtTaxableAmt.setText("0.00");
        txtSgstUgstPer.setTag("");
        txtSgstUgst.setText("0.00");
        txtSgstUgstPer.setText("0.00");
        txtCGSTPer.setTag("");
        txtCGSTPer.setText("0.00");
        txtIGSTPer.setTag("");
        txtIGSTPer.setText("0.00");

        txtCGST.setText("0.00");
        txtIGST.setText("0.00");
        txtGstRate.setText("0.00");
        txtGstRate.setTag("");
        txtGstRate.setTag2nd("");
        txtGstAmt.setText("0.00");
        txtGstAmt.setTag("");
        txtGstAmt.setTag2nd("");
        saleAcCode = "";
        resetBatchDetail();
    }

    private void setBatchDetail(TxtHBatch_Mdl batch_Mdl) {
        double tempRate = 0;

        txtBatchNo.setTag(DmShared.xNull(batch_Mdl.getSharedKeyFieldCode()));
        txtBatchNo.setText(DmShared.xNull(batch_Mdl.getSharedKeyFieldName()));
        txtBarcode.setText(DmShared.xNull(batch_Mdl.getBarcode()));
        txtManufacturingDate.setText(DmShared.dNull(batch_Mdl.getMfgDate()));

        txtExpiryDate.setText(DmShared.dNull(batch_Mdl.getExpDate()));
        txtMRP.setText(DmShared.xNull(batch_Mdl.getMrp()));
        txtMRP.setTag(DmClient.vNull(batch_Mdl.getSkuMrp(), DmShared.getNumberFormat(7)));

        if (parentClass.saleOn == STEnum.MedicineSaleOn.MRP) {
            tempRate = DmShared.vNull(batch_Mdl.getMrp()).doubleValue();
        } else {
            tempRate = DmShared.vNull(batch_Mdl.getSaleRate()).doubleValue();
        }

        txtRate.setText(DmClient.vNull(tempRate, "0.00"));
        txtRate.setTag(DmClient.vNull(tempRate, "0.00"));
        txtWholeRate.setText(DmShared.xNull(batch_Mdl.getWholeSaleRate()));
        txtWholeRate.setTag(DmShared.xNull(batch_Mdl.getWholeSaleRate()));
        if (parentClass.isAllowDiscountToParty) {
            txtDiscountPerUnit.setText(DmShared.xNull(batch_Mdl.getDiscountPerUnit()));
        } else {
            txtDiscountPerUnit.setText("");
        }

        Date tempDate = DmShared.convertDate(DmShared.dNull(txtExpiryDate.getText()));
        CalendarUtil.addDaysToDate(tempDate, (0 - DmShared.iNull(parentClass.expiryAlertBeforeDays).intValue()));

        if (tempDate.getTime() <= DmShared.convertDate(parentClass.txtDate.getText()).getTime()) {
            new DmAlertMessageBox("<b>" + txtMedicineName.getText() + "</b> Batch No. <b>" + txtBatchNo.getText() + "</b> Expiring On <b>" + txtExpiryDate.getText() + "</b>", txtTotalQty).show();
        } else {
            txtTotalQty.setFocus(true);
        }
        calculate();
    }

    private void verifyITS(String itemCode, double ratePerUnit, String itsBasedOn, String billDate, String location) {
        srvAsync.invokeRPC(true).getITS(itemCode, ratePerUnit, itsBasedOn, billDate, location, new AsyncCallback<DmResult<List<OverHeadITS_Mdl>>>() {

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
                } else if (DmShared.xNull(iTS_Mdl.getOverHeadManualCode()).equalsIgnoreCase("SGST") && !parentClass.defaultValue.isUnionTerritory()) {
                    txtSgstUgstPer.setText(DmShared.xNull(iTS_Mdl.getAtRate()));
                    txtSgstUgstPer.setTag(DmShared.xNull(iTS_Mdl.getAccountCode()));
                } else if (DmShared.xNull(iTS_Mdl.getOverHeadManualCode()).equalsIgnoreCase("UGST") && parentClass.defaultValue.isUnionTerritory()) {
                    txtSgstUgstPer.setText(DmShared.xNull(iTS_Mdl.getAtRate()));
                    txtSgstUgstPer.setTag(DmShared.xNull(iTS_Mdl.getAccountCode()));
//                } else if (DmShared.xNull(iTS_Mdl.getOverHeadManualCode()).equalsIgnoreCase("G")) {
                } else if (DmShared.xNull(iTS_Mdl.getOverHeadManualCode()).equalsIgnoreCase("IGST")) {
                    txtIGSTPer.setText(DmShared.xNull(iTS_Mdl.getAtRate()));
                    txtIGSTPer.setTag(DmShared.xNull(iTS_Mdl.getAccountCode()));

                } else if (DmShared.xNull(iTS_Mdl.getOverHeadManualCode()).equalsIgnoreCase("TV")) {
                    txtTaxableAmt.setTag(DmShared.xNull(iTS_Mdl.getAccountCode()));
                }
            }
            if (!saleAcCode.trim().equals("")) {
                txtTaxableAmt.setTag(DmShared.xNull(saleAcCode));
            }

            calculate();

        } else if (ourRow != null) {
            if (parentClass.defaultValue.isUnionTerritory()) {
                txtSgstUgstPer.setTag(DmShared.xNull(ourRow.getUgstAcCode()));
                txtSgstUgstPer.setText(DmClient.vNull(ourRow.getUgstPer(), "0.00"));
            } else {
                txtSgstUgstPer.setTag(DmShared.xNull(ourRow.getSgstAcCode()));
                txtSgstUgstPer.setText(DmClient.vNull(ourRow.getSgstPer(), "0.00"));
            }
            txtCGSTPer.setTag(DmShared.xNull(ourRow.getCgstAcCode()));
            txtCGSTPer.setText(DmClient.vNull(ourRow.getCgstPer(), "0.00"));
            txtIGSTPer.setTag(DmShared.xNull(ourRow.getIgstAcCode()));
            txtIGSTPer.setText(DmClient.vNull(ourRow.getIgstPer(), "0.00"));
            txtTaxableAmt.setTag(DmShared.xNull(ourRow.getSaleAcCode()));
        }
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, String location) {
        this.location = location;
        isRateEditable = parentClass.rateEditable;
        super.showCenter(currentRowIndex, okNeeded, newNeeded, deleteNeeded);
        flwAllowReturnValue.clear();
        if (DmShared.xNull(parentClass.voucher_Cat).equalsIgnoreCase(new ProjectVoucherCategory().PHARMACY_ESTIMATE)) {
            isEstimate = true;
        } else {
            isEstimate = false;
            if (1 == 2) {
                if (this.parentClass.isAllowSaleReturn) {
                    flwAllowReturnValue.add(cbReturnSale);
                }
            }
        }

        txtMedicineName.setFocus(true);
    }

    @Override
    public void displayRow(T1 row) {
        clear();
        lstMdlBatch = null;
        totalQtyBatchList = 0;
        GrdMedicine_Mdl ourRow = (GrdMedicine_Mdl) row;
        hashCode = ourRow.getSharedKeyFieldHashCode();
        medicineMdl = ourRow.getMedicineMdl();
        saleAcCode = DmShared.xNull(ourRow.getSaleAcCode());
        discountGivenByTag = DmShared.xNull(ourRow.getDiscountGivenByTag());
        discountReason = DmShared.xNull(ourRow.getDiscountReasonName());

        if (medicineMdl != null) {
            selectedMedicine = DmShared.xNull(medicineMdl.getSharedKeyFieldCode());
            selectedMedicineToFill = DmShared.xNull(medicineMdl.getSharedKeyFieldCode());
            selectedMedicineNameToFill = DmShared.xNull(medicineMdl.getSharedKeyFieldName());
            txtMedicineName.setTag(DmShared.xNull(medicineMdl.getSharedKeyFieldCode()));
            txtMedicineName.setText(DmShared.xNull(medicineMdl.getSharedKeyFieldName()));
            txtMedicineName.setTag2nd(DmShared.xNull(medicineMdl.getItemProperty()).trim());

            skuUnitName = DmShared.xNull(medicineMdl.getSkuName());
            isReturnableItem = medicineMdl.isReturnableMedicine();
            isDiscountAllow = parentClass.discountEditable && medicineMdl.isDiscountAllow();
            isAllowNegativeStock = medicineMdl.isAllowNegativeStock();
            isBarcodeActive = medicineMdl.isBarcodeActive();
            txtTotalQty.setTag(DmClient.vNull(ourRow.getQuantitySKU(), DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowDecimalPlaces()).intValue())));
            txtTotalQty.setText(DmClient.vNull(ourRow.getQuantity(), DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowDecimalPlaces()).intValue())));
            batchQtyInPurchaseUnit = DmClient.vNull(ourRow.getBatchQuantityInPurchaseUnit(), DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowDecimalPlaces()).intValue()));
            txtTotalQty.setTag2nd(DmClient.vNull(ourRow.getPrvQty(), DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowDecimalPlaces()).intValue())));

            txtFreeQty.setText(DmClient.vNull(ourRow.getFreeQuantityInPurchaseUnit(), DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowDecimalPlaces()).intValue())));
            txtFreeQty.setTag2nd(DmClient.vNull(ourRow.getPrvFreeQty(), DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowDecimalPlaces()).intValue())));

            freeWithItemCode = DmShared.iNull(ourRow.getFreeWithItemCode()).toString();

            txtBatchNo.setTag(DmShared.xNull(ourRow.getIbCode()));
            txtBatchNo.setText(DmShared.xNull(ourRow.getBatchNo()));

            txtBarcode.setText(DmShared.xNull(ourRow.getBarcode()));
            itemCodeCsv = DmShared.xNull(ourRow.getItemCodeCsv());
            selectedBarCode = DmShared.xNull(ourRow.getBarcode());

            txtPurchaseUnitName.setText(DmShared.xNull(medicineMdl.getPurchaseUnitName()));
            txtConversionRateMRP.setText(medicineMdl.getPurchaseUnitName());

            txtManufacturingDate.setText(DmShared.dNull(ourRow.getMfgDate()));
            txtExpiryDate.setText(DmShared.dNull(ourRow.getExpDate()));
            txtMRP.setText(DmShared.xNull(ourRow.getMrp()));

            txtDiscountPer.setText(DmClient.vNull(medicineMdl.getDiscountPer(), "0.00"));

            isSaleRateEditable = DmShared.xNull(medicineMdl.getIsSaleRateEditable());
            txtConversionRate.setText("");
            txtFreeQtyConversionRate.setText("");
            if (DmShared.vNull(medicineMdl.getPurchaseUnitConversionRate()).doubleValue() > 0 && !DmShared.xNull(medicineMdl.getPurchaseUnitName()).equalsIgnoreCase("") && !DmShared.xNull(medicineMdl.getPurchaseUnitName()).equalsIgnoreCase("")) {
                if (DmShared.xNull(medicineMdl.getSkuName()).trim().equalsIgnoreCase(DmShared.xNull(medicineMdl.getPurchaseUnitName()))) {
                    txtConversionRate.setText(medicineMdl.getPurchaseUnitName());
                    txtFreeQtyConversionRate.setText(medicineMdl.getPurchaseUnitName());
                } else if (!DmShared.xNull(medicineMdl.getSkuName()).trim().equalsIgnoreCase(DmShared.xNull(medicineMdl.getPurchaseUnitName()))) {
                    txtConversionRate.setText(medicineMdl.getPurchaseUnitName() + " = " + medicineMdl.getPurchaseUnitConversionRate() + " " + medicineMdl.getSkuName());
                    txtFreeQtyConversionRate.setText(medicineMdl.getPurchaseUnitName() + " = " + medicineMdl.getPurchaseUnitConversionRate() + " " + medicineMdl.getSkuName());
                }
            }
            setITS(medicineMdl.getLstITS(), ourRow);
        }
        txtRate.setText(DmClient.vNull(ourRow.getRate(), "0.00"));
        txtWholeRate.setText(DmShared.xNull(ourRow.getWholeSaleRate()));
        txtWholeRate.setTag(DmShared.xNull(ourRow.getWholeSaleRateActual()));

        txtAmount.setText(DmClient.vNull(ourRow.getAmount(), "0.00"));
        txtNetAmount.setText(DmClient.vNull(ourRow.getNetAmt(), "0.00"));
        txtTaxableAmt.setText(DmClient.vNull(ourRow.getTaxableAmt(), "0.00"));

        totalDiscount = DmShared.vNull(ourRow.getTotalDiscount());
        txtDiscount.setText(DmClient.vNull(ourRow.getDiscount(), "0.00"));
        txtDiscount2.setText(DmClient.vNull(ourRow.getDiscount2(), "0.00"));
        txtDiscountPer2.setText(DmClient.vNull(ourRow.getDiscountPer2(), "0.00"));
        txtDiscountPerUnit.setText(DmShared.xNull(ourRow.getDiscountPerUnit()));
        if (parentClass.defaultValue.isUnionTerritory()) {
            txtSgstUgst.setText(DmClient.vNull(ourRow.getUgstAmt(), "0.00"));
        } else {
            txtSgstUgst.setText(DmClient.vNull(ourRow.getSgstAmt(), "0.00"));
        }

        txtCGST.setText(DmClient.vNull(ourRow.getCgstAmt(), "0.00"));
        txtIGST.setText(DmClient.vNull(ourRow.getIgstAmt(), "0.00"));
        txtGstRate.setText(DmClient.vNull(ourRow.getGstPer(), "0.00"));
        txtGstAmt.setText(DmClient.vNull(ourRow.getGstAmount(), "0.00"));

        if (DmShared.vNull(ourRow.getQuantity()).doubleValue() < 0 || DmShared.vNull(ourRow.getFreeQuantityInPurchaseUnit()).doubleValue() < 0) {
            cbReturnSale.setValue(Boolean.TRUE);
        }

        manageBatchSelectedQty();
        setBatchLabelDesign();
        manageDisplay();

        if (ourRow.isSchemeItem()) {
            txtFreeQty.setFocus(true);
        } else {
            txtTotalQty.setFocus(true);
        }

    }

    private void manageBatchSelectedQty() {
        selectedBatchSQL = "";
        selectedIbCodes = new ArrayList<>();
        for (GrdMedicine_Mdl service : (List<GrdMedicine_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
            selectedIbCodes.add(service.getIbCode());
            if (service.getSharedKeyFieldHashCode() != hashCode) {
                if (DmShared.iNull(service.getIbCode()).longValue() > 0) {
                    if (!selectedBatchSQL.equalsIgnoreCase("")) {
                        selectedBatchSQL += " UNION ALL ";
                    }
                    selectedBatchSQL += " Select " + DmShared.iNull(service.getIbCode()).longValue() + " as ibCode, " + DmShared.vNull(service.getTotalQuantitySKU()).doubleValue() + " as qty";
                }
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
        if (!DmShared.dNull(txtExpiryDate.getText()).equalsIgnoreCase("")) {
            if (DmShared.convertDate(txtExpiryDate.getText()).getTime() <= DmShared.convertDate(parentClass.txtDate.getText()).getTime()) {
                new DmAlertMessageBox("Expiry Date Cannot be less than or equal to bill date.", txtBatchNo).show();
                return;
            }
        }
        if (DmShared.vNull(txtFreeQty.getText()).doubleValue() == 0) {
            if (DmShared.vNull(txtTotalQty.getText()).doubleValue() == 0) {
                new DmAlertMessageBox("Quantity is Mandatory.", txtTotalQty).show();
                return;
            }
        }

        if (!medicineMdl.isAllowNegativeStock()) {
            if (DmShared.vNull(batchQtyInPurchaseUnit).doubleValue() > 0) {
                if ((DmShared.vNull(txtTotalQty.getText()).doubleValue() + DmShared.vNull(txtFreeQty.getText()).doubleValue()) > DmShared.vNull(batchQtyInPurchaseUnit).doubleValue()) {
                    new DmAlertMessageBox("Not Enough Stock in this batch.<br/>Available Stock : " + batchQtyInPurchaseUnit + " " + medicineMdl.getPurchaseUnitName(), txtTotalQty).show();
                    return;
                }
            }
        }

        if (DmShared.vNull(txtTotalQty.getText()).doubleValue() != 0) {
            if (DmShared.vNull(txtRate.getText()).doubleValue() == 0) {
                new DmAlertMessageBox("Rate is Mandatory.", txtRate).show();
                return;
            }
            if (DmShared.vNull(txtAmount.getText()).doubleValue() == 0) {
                new DmAlertMessageBox("Amount is Mandatory.", txtRate).show();
                return;
            }
            if (Math.abs(DmShared.vNull(txtAmount.getText()).doubleValue()) < (Math.abs(DmShared.vNull(txtDiscount.getText()).doubleValue() + DmShared.vNull(txtDiscount2.getText()).doubleValue()))) {
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
        for (GrdMedicine_Mdl detail : (List<GrdMedicine_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
            if (detail.getSharedKeyFieldHashCode() != hashCode) {
                int rowNo = getDmGrid().getGrid().getStore().getAll().indexOf(detail) + 1;
                if (DmShared.xNull(detail.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(txtMedicineName.getTag()))
                        && DmShared.xNull(detail.getIbCode()).equalsIgnoreCase(DmShared.xNull(txtBatchNo.getTag()))
                        && cbReturnSale.getValue() == detail.isReturnSale()) {
                    if (DmShared.vNull(txtFreeQty.getText()).doubleValue() != 0) {
                        if (DmShared.vNull(detail.getFreeQuantityInPurchaseUnit()).doubleValue() != 0) {
                            new DmAlertMessageBox("Medicine with this batch already exists at row no.: " + rowNo, txtMedicineName).show();
                            return;
                        }
                    } else if (DmShared.vNull(txtTotalQty.getText()).doubleValue() != 0) {
                        if (DmShared.vNull(detail.getQuantity()).doubleValue() != 0) {
                            new DmAlertMessageBox("Medicine with this batch already exists at row no.: " + rowNo, txtMedicineName).show();
                            return;
                        }
                    }

                }
            }
        }

        if (cbReturnSale.getValue()) {
            if (isReturnableItem) {
                if (DmShared.iNull(parentClass.defaultValue.getAllowReturnWithoutSale()).intValue() == 0) {
                    srvAsync.invokeRPC().isItemEverReturned(txtMedicineName.getTag(), txtBatchNo.getTag(), txtTotalQty.getTag2nd(), txtFreeQty.getTag2nd(), new AsyncCallback<DmResult<String>>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                        }

                        @Override
                        public void onSuccess(DmResult<String> result) {
                            if (result.isStatus()) {
                                if (DmShared.vNull(result.getData()).doubleValue() > 0) {
                                    if ((0 - (DmShared.vNull(txtTotalQty.getText()).doubleValue() + DmShared.vNull(txtFreeQty.getText()).doubleValue())) > DmShared.vNull(result.getData()).doubleValue()) {
                                        new DmAlertMessageBox("Return Quantity is more than Sale Quantity. Sale Qty : " + result.getData(), txtTotalQty).show();
                                    } else {
                                        proceedOk();
                                    }
                                } else {
                                    new DmAlertMessageBox("Medicine with this batch, never sold to/already returned by, this patient.", txtMedicineName).show();
                                }
                            }
                        }
                    });
                } else {
                    proceedOk();
                }
            } else {
                new DmAlertMessageBox("Medicine cannot be returned as it is marked <b>'not returnable'</b>.", txtMedicineName).show();
            }
        } else {
            if (totalQtyBatchList > 0) {    //TBC
                if ((!medicineMdl.isAllowNegativeStock()) && (DmShared.vNull(txtTotalQty.getTag()).doubleValue() > totalQtyBatchList)) {
                    new DmAlertMessageBox("Cannot enter quantity more than stock quantity. Stock Quantity: " + totalQtyBatchList, txtTotalQty).show();
                } else {
                    proceedOk();
                }
            } else {
                if (DmShared.vNull(txtTotalQty.getTag()).equals(DmShared.vNull(txtTotalQty.getText()))) {
                    proceedOk();
                } else {
                    new DmAlertMessageBox("Please select the batch", txtBatchNo).show();
                }
            }
        }

    }

    @Override
    public void manageOk(T1 row) {
        GrdMedicine_Mdl ourRow = (GrdMedicine_Mdl) row;
        fillRowData(ourRow);
        lstMdlGrd = new ArrayList<>();
        callBack.onOk(ourRow, lstMdlGrd);
    }

    protected void fillRowData(GrdMedicine_Mdl ourRow) {
        medicineMdl.setDiscountPer(DmClient.vNull(txtDiscountPer.getText(), "0.00"));

        ourRow.setMedicineMdl(medicineMdl);
        ourRow.setSharedKeyFieldCode(medicineMdl.getSharedKeyFieldCode());
        ourRow.setSharedKeyFieldName(medicineMdl.getSharedKeyFieldName());
        ourRow.setDiscountGivenByTag(discountGivenByTag);
        ourRow.setDiscountReasonName(discountReason);
        ourRow.setMedicineCategory(medicineMdl.getMedicineCategory());
        ourRow.setMedicineGroupCode(medicineMdl.getMedicineGroupCode());
        ourRow.setMedicineGroupName(medicineMdl.getMedicineGroupName());
        ourRow.setUnit(medicineMdl.getPurchaseUnitName());

        ourRow.setIbCode(DmShared.iNull(txtBatchNo.getTag()).toString());
        ourRow.setBatchNo(DmShared.xNull(txtBatchNo.getText()));
        ourRow.setBarcode(DmShared.xNull(txtBarcode.getText()));
        ourRow.setItemCodeCsv(DmShared.xNull(itemCodeCsv));
        ourRow.setBestBefore(medicineMdl.isBestBefore());
        ourRow.setExpDate(DmShared.dNull(txtExpiryDate.getText()));
        ourRow.setMfgDate(DmShared.dNull(txtManufacturingDate.getText()));

        if (DmShared.vNull(txtFreeQty.getText()).doubleValue() > 0) {
            ourRow.setSchemeItem(true);
        }
        ourRow.setFreeQuantityInPurchaseUnit(DmClient.vNull(txtFreeQty.getText(), DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowDecimalPlaces()).intValue())));
        ourRow.setPrvFreeQty(DmClient.vNull(txtFreeQty.getTag2nd(), DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowDecimalPlaces()).intValue())));
        ourRow.setFreeQuantityInSku(DmClient.vNull((DmShared.vNull(txtFreeQty.getText()).doubleValue() * DmShared.vNull(medicineMdl.getPurchaseUnitConversionRate()).doubleValue()), DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowDecimalPlaces()).intValue())));
        ourRow.setFreeWithItemCode(freeWithItemCode);

        ourRow.setBatchQuantityInPurchaseUnit(DmClient.vNull(batchQtyInPurchaseUnit, DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowDecimalPlaces()).intValue())));
        ourRow.setQuantity(DmClient.vNull(txtTotalQty.getText(), DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowDecimalPlaces()).intValue())));
        ourRow.setPrvQty(DmClient.vNull(txtTotalQty.getTag2nd(), DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowDecimalPlaces()).intValue())));
        ourRow.setQuantitySKU(DmClient.vNull((DmShared.vNull(txtTotalQty.getTag()).doubleValue()), DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowSkuDecimalPlaces()).intValue())));

        totalQtyInSKu = DmShared.vNull(ourRow.getFreeQuantityInSku()).doubleValue() + DmShared.vNull(ourRow.getQuantitySKU()).doubleValue();
        ourRow.setTotalQuantitySKU(DmClient.vNull((DmShared.vNull(totalQtyInSKu).doubleValue()), DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowSkuDecimalPlaces()).intValue())));
        ourRow.setRate(DmClient.vNull(txtRate.getText(), "0.00"));
        ourRow.setWholeSaleRate(DmShared.xNull(txtWholeRate.getText()));
        ourRow.setWholeSaleRateActual(DmShared.xNull(txtWholeRate.getTag()));
        ourRow.setMrp(DmClient.vNull(txtMRP.getText(), "0.00"));
        ourRow.setAmount(DmClient.vNull(txtAmount.getText(), "0.00"));
        ourRow.setTaxableAmt(DmClient.vNull(txtTaxableAmt.getText(), "0.00"));
        ourRow.setDiscount(DmClient.vNull(txtDiscount.getText(), "0.00"));
        ourRow.setDiscountPer(DmClient.vNull(medicineMdl.getDiscountPer(), "0.00"));
        ourRow.setDiscount2(DmClient.vNull(txtDiscount2.getText(), "0.00"));
        ourRow.setDiscountPer2(DmClient.vNull(txtDiscountPer2.getText(), "0.00"));
        ourRow.setDiscountPerUnit(DmShared.xNull(txtDiscountPerUnit.getText()));
        ourRow.setTotalDiscount(DmClient.vNull(totalDiscount, "0.00"));
        ourRow.setCgstAmt(DmClient.vNull(txtCGST.getText(), "0.00"));
        ourRow.setCgstPer(DmClient.vNull(txtCGSTPer.getText(), "0.00"));
        ourRow.setCgstAcCode(DmShared.xNull(txtCGSTPer.getTag()));

        ourRow.setIgstAmt(DmClient.vNull(txtIGST.getText(), "0.00"));
        ourRow.setIgstPer(DmClient.vNull(txtIGSTPer.getText(), "0.00"));
        ourRow.setIgstAcCode(DmShared.xNull(txtIGSTPer.getTag()));

        ourRow.setReturnSale(cbReturnSale.getValue());
        if (parentClass.defaultValue.isUnionTerritory()) {
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
    }

    @Override
    public void onNew() {
        proceedNew();
    }

    @Override
    public void onDelete() {
        if (parentClass.freeWithItems_Mdl != null) {
            if (parentClass.freeWithItems_Mdl.getLstFreeWithItemCodes().contains(DmShared.iNull(txtMedicineName.getTag()).toString()) && (DmShared.vNull(txtFreeQty.getText()).doubleValue() == 0)) {
                new DmConfirmMessageBox("Scheme Exists With this Medicine.Do you wish to Continue for delete ?", new DialogHideEvent.DialogHideHandler() {
                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                            proceedDelete();
                        }
                    }
                }).show();
            } else {
                proceedDelete();
            }
        } else {
            proceedDelete();
        }

    }

    @Override
    public void onClose() {
        hide();
    }

    @Override
    public void manageDelete() {
        callBack.onDelete();
    }

    public MedicineDetailsEdit(DmGrid<T1, T2> grid, SaleBillWholeSale saleBillWholeSale) {
        super(grid, "Medicine Details Edit", "410px", "");
        this.parentClass = saleBillWholeSale;
        srvAsync = new ServiceInvoker<>(GWT.create(SaleBillWholeSale_Srv.class), SaleBillWholeSale.class.getName());
        flwForm.addAttachHandler(this);
        manageEvents();
        textHelpEvents();
    }

    private void setBatchLabelDesign() {
        if (parentClass.isAllowAddBatch && (isAllowNegativeStock || cbReturnSale.getValue())) {
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

    private void manageEvents() {
        txtTotalQty.addBlurHandler(this);
        txtFreeQty.addBlurHandler(this);
        txtBatchNo.addKeyUpHandler(this);
        txtBatchNo.addKeyDownHandler(this);
        txtRate.addBlurHandler(this);
        txtDiscount.addBlurHandler(this);
        txtDiscountPer.addBlurHandler(this);
        txtDiscount2.addBlurHandler(this);
        txtDiscountPer2.addBlurHandler(this);
        txtBarcode.addBlurHandler(this);
        cbReturnSale.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                setBatchLabelDesign();
                calculate();
            }
        });
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
        if (parentClass.isAllowAddBatch && (isAllowNegativeStock || cbReturnSale.getValue())) {
            addBatchPopup.show(DmShared.xNull(txtMedicineName.getTag()), DmShared.xNull(parentClass.txtHDepartment.getTag()), DmShared.dNull(parentClass.txtDate.getText()), medicineMdl.isBestBefore(), isBarcodeActive, DmShared.vNull(medicineMdl.getPurchaseUnitConversionRate()).toString(), DmShared.xNull(parentClass.topControl.getMode()), skuUnitName, DmShared.xNull(medicineMdl.getPurchaseUnitName()));
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtTotalQty || event.getSource() == txtFreeQty || event.getSource() == txtRate || event.getSource() == txtDiscountPer || event.getSource() == txtDiscount || event.getSource() == txtDiscountPer2 || event.getSource() == txtDiscount2) {
            calculate();
        } else if (event.getSource() == txtRate) {
            if (DmShared.iNull(medicineMdl.getItsBasedOn()).intValue() == 1) {
                verifyITS(txtMedicineName.getTag(), DmShared.vNull(txtWholeRate.getText()).doubleValue(), medicineMdl.getItsBasedOn(), DmShared.dNull(parentClass.txtDate.getText()), location);
            } else {
                calculate();
            }
        } else if (event.getSource() == txtBarcode) {
            if (!selectedBarCode.equalsIgnoreCase(DmShared.xNull(txtBarcode.getText())) || DmShared.iNull(txtMedicineName.getTag()).longValue() == 0) {
                txtMedicineName.setTag("");
                txtMedicineName.setText("");
                clearOnMedicineChange();
                if (!DmShared.xNull(txtBarcode.getText()).trim().equals("") && !parentClass.topControl.getMode().equalsIgnoreCase("Browse")) {
                    barCodeSearch();
                } else {
                    itemCodeCsv = "";
                }
            }
        }

        if (event.getSource() == txtRate) {
            if (DmShared.vNull(txtRate.getText()) != DmShared.vNull(txtRate.getTag())) {
                txtWholeRate.setText(txtRate.getText());
            } else {
                txtWholeRate.setText(txtWholeRate.getTag());
            }
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            onClose();
        } else if (rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_B) {
            if (parentClass.isAllowAddBatch) {
                showAddBatch();
            }
        } else if (rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_R) {
            if (!isEstimate) {
                cbReturnSale.setValue(!cbReturnSale.getValue());
                setBatchLabelDesign();
                calculate();
            }
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            batchDetail.hide();
        }
    }

    protected void calculate() {
        double purchaseUnitConversionRate = 0, taxableAmt = 0, totalTaxRate = 0, totalTaxAmt = 0, netAmt = 0;
        double tempDiscountPerUnit = 0;
        int purchaseUnitDecimalPlaces = 4, skuDecimalPlaces = 4;
        double tempQty = DmShared.vNull(txtTotalQty.getText()).doubleValue();
        double tempFreeQty = DmShared.vNull(txtFreeQty.getText()).doubleValue();

        totalDiscount = 0;

        if (medicineMdl != null) {
            purchaseUnitDecimalPlaces = medicineMdl.getAllowDecimalPlaces();
            skuDecimalPlaces = medicineMdl.getAllowSkuDecimalPlaces();
            purchaseUnitConversionRate = DmShared.vNull(medicineMdl.getPurchaseUnitConversionRate()).doubleValue();
        }

        if (cbReturnSale.getValue()) {
            if (tempQty > 0) {
                txtTotalQty.setText(DmShared.vNull(0 - tempQty).toString());
            }
            if (tempFreeQty > 0) {
                txtFreeQty.setText(DmShared.vNull(0 - tempFreeQty).toString());
            }
        } else {
            if (tempQty < 0) {
                txtTotalQty.setText(DmShared.vNull((Math.abs(tempQty))).toString());
            }
            if (tempFreeQty < 0) {
                txtFreeQty.setText(DmShared.vNull((Math.abs(tempFreeQty))).toString());
            }
        }

        txtFreeQty.setText(DmClient.vNull(txtFreeQty.getText(), DmShared.getNumberFormat(DmShared.iNull(purchaseUnitDecimalPlaces).intValue())));
        txtTotalQty.setText(DmClient.vNull(txtTotalQty.getText(), DmShared.getNumberFormat(DmShared.iNull(purchaseUnitDecimalPlaces).intValue())));
        txtTotalQty.setTag(DmClient.vNull((DmShared.vNull(txtTotalQty.getText()).doubleValue() * purchaseUnitConversionRate), DmShared.getNumberFormat(DmShared.iNull(skuDecimalPlaces).intValue())));

        txtAmount.setText(DmClient.vNull(DmShared.vNull(txtTotalQty.getText()).doubleValue() * DmShared.vNull(txtWholeRate.getText()).doubleValue(), "0.00"));
        double discount1 = 0;
        if (parentClass.isAllowDiscountToParty) {
            txtDiscountPer.setText("");
            discount1 = DmShared.vNull(txtTotalQty.getText()).doubleValue() * DmShared.vNull(txtDiscountPerUnit.getText()).doubleValue();
        } else {
            discount1 = DmShared.vNull(txtDiscount.getText()).doubleValue();
            if (DmShared.vNull(txtDiscountPer.getText()).doubleValue() > 0) {
                discount1 = DmShared.vNull(txtAmount.getText()).doubleValue() * DmShared.vNull(txtDiscountPer.getText()).doubleValue() * 0.01;
            }
        }
        txtDiscount.setText(DmClient.vNull(discount1, "0.00"));
        if (DmShared.vNull(txtDiscountPer2.getText()).doubleValue() > 0) {
            double tempAmt = 0;
            tempAmt = DmShared.vNull(txtAmount.getText()).doubleValue() - (DmShared.vNull(txtDiscount.getText()).doubleValue());
            txtDiscount2.setText(DmClient.vNull((tempAmt * DmShared.vNull(txtDiscountPer2.getText()).doubleValue() * 0.01), "0.00"));
        } else {
            txtDiscount2.setText(DmClient.vNull(DmShared.vNull(txtDiscount2.getText()), "0.00"));
        }

        totalDiscount = DmShared.vNull(txtDiscount.getText()).doubleValue() + DmShared.vNull(txtDiscount2.getText()).doubleValue();
        taxableAmt = DmShared.vNull(txtAmount.getText()).doubleValue() - totalDiscount;

        txtCGST.setText(DmClient.vNull((taxableAmt * DmShared.vNull(txtCGSTPer.getText()).doubleValue() * 0.01), "0.00"));
        txtSgstUgst.setText(DmClient.vNull((taxableAmt * DmShared.vNull(txtSgstUgstPer.getText()).doubleValue() * 0.01), "0.00"));
        txtIGST.setText(DmClient.vNull((taxableAmt * DmShared.vNull(txtIGSTPer.getText()).doubleValue() * 0.01), "0.00"));

        totalTaxRate = DmShared.vNull(txtCGSTPer.getText()).doubleValue() + DmShared.vNull(txtSgstUgstPer.getText()).doubleValue() + DmShared.vNull(txtIGSTPer.getText()).doubleValue();
        totalTaxAmt = DmShared.vNull(txtCGST.getText()).doubleValue() + DmShared.vNull(txtSgstUgst.getText()).doubleValue() + DmShared.vNull(txtIGST.getText()).doubleValue();
        netAmt = taxableAmt + totalTaxAmt;

        txtTaxableAmt.setText(DmClient.vNull(taxableAmt, "0.00"));
        txtNetAmount.setText(DmClient.vNull(netAmt, "0.00"));
        txtGstRate.setText(DmClient.vNull(totalTaxRate, "0.00"));
        txtGstAmt.setText(DmClient.vNull(totalTaxAmt, "0.00"));
    }

    private void clear() {
        medicineMdl = null;
        selectedMedicine = "";
        selectedMedicineToFill = "";
        selectedMedicineNameToFill = "";
        isReturnableItem = false;
        txtMedicineName.setTag("");
        txtMedicineName.setText("");
        txtMedicineName.setTag2nd("");
        txtConversionRate.setText("");
        txtConversionRateMRP.setText("");
        txtFreeQtyConversionRate.setText("");
        txtBatchNo.setTag("");
        txtBatchNo.setTag2nd("");
        txtBatchNo.setText("");
        saleAcCode = "";
        cbReturnSale.setValue(false);
        totalDiscount = 0;
        txtBarcode.setText("");
        selectedBarCode = "";
        itemCodeCsv = "";
        clearOnMedicineChange();
    }

    private void manageDisplay() {
        txtCGST.setEnabled(false);
        txtCGSTPer.setEnabled(false);
        txtIGSTPer.setEnabled(false);
        txtSgstUgst.setEnabled(false);
        txtSgstUgstPer.setEnabled(false);
        txtTaxableAmt.setEnabled(false);
        txtNetAmount.setEnabled(false);
        txtAmount.setEnabled(false);
        txtConversionRate.setReadOnly(true);
        txtFreeQtyConversionRate.setReadOnly(true);
        txtPurchaseUnitName.setReadOnly(true);
        txtGstAmt.setEnabled(false);
        txtGstRate.setEnabled(false);

        manageSaleRate();

        txtDiscountPer.setEnabled(isDiscountAllow);
        txtDiscount.setEnabled(isDiscountAllow);

        txtBatchNo.setReadOnly(true);
        txtMRP.setEnabled(false);
        txtManufacturingDate.setEnabled(false);
        txtExpiryDate.setEnabled(false);

        lblSgstUgst.setText("");
        if (parentClass.defaultValue.isUnionTerritory()) {
            lblSgstUgst.setText("UGST Amount");
        } else {
            lblSgstUgst.setText("SGST Amount");
        }

        if (DmShared.vNull(txtFreeQty.getText()).doubleValue() > 0) {
            txtMedicineName.setEnabled(false);
            txtBarcode.setEnabled(false);
        } else {
            txtMedicineName.setEnabled(true);
            txtBarcode.setEnabled(DmShared.iNull(parentClass.defaultValue.getPharmacyBarcodeActive()).intValue() == 1);
        }
    }

    private void showBatchDetailPopup() {
        if (!parentClass.topControl.getMode().equalsIgnoreCase("Browse")) {
            if (DmShared.iNull(txtMedicineName.getTag()).longValue() != 0) {
                batchDetail.show(txtMedicineName.getTag(), parentClass.txtHDepartment.getTag(), parentClass.txtDate.getText(), selectedBatchSQL, STEnum.ItemBatchType.NON_EXPIRED_BATCH, cbReturnSale.getValue(), DmShared.xNull(txtBarcode.getText()), STEnum.MedicineSaleOn.WHOLESALE_RATE, parentClass.profitPercentage, parentClass.isAllowDiscountToParty);
            } else {
                new DmAlertMessageBox("Please select Medicine Name.", txtMedicineName).show();
            }
        }
    }

    private void barCodeSearch() {
        srvAsync.invokeRPC().getBarCodeSearch(DmShared.xNull(txtBarcode.getText()), DmShared.dNull(parentClass.txtDate.getText()), DmShared.iNull(parentClass.txtHDepartment.getTag()).toString(), DmShared.iNull(parentClass.defaultValue.getSiteCode()).toString(), parentClass.saleOn, new AsyncCallback<DmResult<BarCode_Mdl>>() {

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
                        txtMedicineName.setTag(DmShared.xNull(data.getMedicineDetail().getSharedKeyFieldCode()));
                        txtMedicineName.setText(DmShared.xNull(data.getMedicineDetail().getSharedKeyFieldName()));
                        fillMedicineDetail(data.getMedicineDetail(), true, false);

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
        filter.setDepartmentCode(DmShared.xNull(parentClass.txtHDepartment.getTag()));
        filter.setDepartmentName(DmShared.xNull(parentClass.txtHDepartment.getText()));
        filter.setV_Date(DmShared.dNull(parentClass.txtDate.getText()));
        filter.setItemCodeCsv(DmShared.xNull(itemCodeCsv));
        medicineSearch.show(filter, searchChar, parentClass.txtHVType.getTag());
    }

    private void setBatchModel(BatchNo_Mdl batchDetail) {
        TxtHBatch_Mdl batch_Mdl = new TxtHBatch_Mdl();
        batch_Mdl.setSaleRate(DmShared.xNull(batchDetail.getSaleRate()));
        batch_Mdl.setWholeSaleRate(DmShared.xNull(batchDetail.getWholeSaleRate()));
        batch_Mdl.setMrp(DmShared.xNull(batchDetail.getMrp()));
        batch_Mdl.setSharedKeyFieldCode(DmShared.xNull(batchDetail.getIbCode()));
        batch_Mdl.setSharedKeyFieldName(DmShared.xNull(batchDetail.getBatchNo()));
        batch_Mdl.setExpDate(DmShared.dNull(batchDetail.getExpiryDate()));
        batch_Mdl.setMfgDate(DmShared.dNull(batchDetail.getManufacturingDate()));
        batch_Mdl.setSkuSaleRate(DmShared.xNull(batchDetail.getSkuSaleRate()));
        batch_Mdl.setSkuMrp(DmShared.xNull(batchDetail.getSkuMrp()));
        batch_Mdl.setBarcode(DmShared.xNull(batchDetail.getBarCode()));
        batch_Mdl.setDiscountPerUnit(DmShared.xNull(batchDetail.getDiscountPerUnit()));
        setBatchDetail(batch_Mdl);

    }
}
