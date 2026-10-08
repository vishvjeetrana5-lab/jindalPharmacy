package aarogya.pharmacy.saleBill.client;

import aarogya.pharmacy.addBatchSingle.client.AddBatchSingle;
import aarogya.pharmacy.addBatchSingle.shared.TxtHBatch_Mdl;
import aarogya.pharmacy.batchNo.client.BatchNo;
import aarogya.pharmacy.batchNo.shared.BatchNo_Mdl;
import aarogya.pharmacy.medicineSearch.client.MedicineSearch;
import aarogya.pharmacy.medicineSearch.shared.MedicineSearchFilter_Mdl;
import aarogya.pharmacy.medicineSearch.shared.MedicineSearch_Mdl;
import aarogya.pharmacy.saleBill.shared.*;
import aarogya.pharmacy.self.shared.BarCode_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.shared.STEnum.*;
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
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
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

    private final ServiceInvoker<SaleBill_SrvAsync> srvAsync;
    private CallBack callBack;
    protected SaleBill saleBill;
    protected int hashCode = 0;
    private String saleAcCode = "";
    private String selectedMedicine = "", skuUnitName = "";
    private String selectedMedicineToFill = "", selectedMedicineNameToFill = "";
    protected TxtHMedicine_Mdl medicineMdl = new TxtHMedicine_Mdl();
    private final AddBatchSingle addBatchPopup = new AddBatchSingle();
    boolean isRateEditable = false, isBarcodeActive = false;
    String isSaleRateEditable = "";
    boolean isReturnableItem = false, isDiscountAllow = false, isEstimate = false, isAllowNegativeStock = false, autoFillBatchFlag = false;
    private String reqDocId = "", reqVSno = "", requisitionNo = "", discountGivenByTag = "", discountReason = "", gstPer = "", gstAmt = "";
    private final BatchNo batchDetail = new BatchNo();
    String selectedBatchSQL = "", currentBatchQtyInSku = "", freeWithItemCode = "", currentBatchQtyInSaleUnit = "";
    double totalQtyBatchList = 0, totalQtyInSKu = 0, totalDiscount = 0;
    List<BatchNo_Mdl> lstMdlBatch;
    List<GrdMedicine_Mdl> lstMdlGrd;
    List<String> selectedIbCodes;
    String batchQtyInSaleUnit = "";
    private String selectedBarCode = "";
    private String itemCodeCsv = "";

    private final MedicineSearch medicineSearch = new MedicineSearch(null, null);
    private String grossPurchaseRate = "";

    private void textHelpEvents() {
        batchDetail.addCallBack(new BatchNo.CallBack() {
            @Override
            public void ok(BatchNo_Mdl batchDetail, List<BatchNo_Mdl> lstBatchDetails) {
                resetBatchDetail();
                totalQtyBatchList = 0;
                grossPurchaseRate = "";
                grossPurchaseRate = DmShared.xNull(batchDetail.getGrossPurchaseRate());
                if (batchDetail != null) {
                    lstMdlBatch = new ArrayList<>();
                    lstMdlBatch.addAll(lstBatchDetails);
                    currentBatchQtyInSku = batchDetail.getBalanceQty();
                    autoFillBatchFlag = true;
                    setBatchModel(batchDetail);
                    if (DmShared.vNull(batchDetail.getBalanceQty()) > 0) {
                        batchQtyInSaleUnit = batchDetail.getBalanceQtyInSaleUnit();
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
                    if (saleBill.freeWithItems_Mdl != null) {
                        if ((saleBill.freeWithItems_Mdl.getLstFreeWithItemCodes().contains(selectedMedicine)) && (DmShared.iNull(selectedMedicine).longValue() != 0)) {
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
        medicineMdl.setDiscountAllow(DmShared.iNull(medSearchMdl.getIsDiscountAllow()).intValue() == 1);
        medicineMdl.setSchemeExists(!DmShared.xNull(medSearchMdl.getSaleSchemeCode()).trim().equals(""));
        medicineMdl.setSaleGSTUnit(DmShared.xNull(medSearchMdl.getSaleUnitGstUnitCode()));
        //</editor-fold>
        isReturnableItem = medicineMdl.isReturnableMedicine();
        isAllowNegativeStock = medicineMdl.isAllowNegativeStock();
        isBarcodeActive = medicineMdl.isBarcodeActive();
        isDiscountAllow = saleBill.organizationMdl != null ? (saleBill.organizationMdl.isPharmacyDiscount() && saleBill.discountEditable && medicineMdl.isDiscountAllow()) : (saleBill.discountEditable && medicineMdl.isDiscountAllow());
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
//        txtConversionRateMRP.setText("");
        if (DmShared.vNull(medicineMdl.getPurchaseUnitConversionRate()).doubleValue() > 0 && !DmShared.xNull(medicineMdl.getSaleUnitName()).equalsIgnoreCase("") && !DmShared.xNull(medicineMdl.getPurchaseUnitName()).equalsIgnoreCase("")) {
            if (DmShared.xNull(medicineMdl.getSkuName()).trim().equalsIgnoreCase(DmShared.xNull(medicineMdl.getSaleUnitName()))) {
                txtConversionRate.setText(medicineMdl.getSaleUnitName());
                txtFreeQtyConversionRate.setText(medicineMdl.getSaleUnitName());
//                txtConversionRateMRP.setText(medicineMdl.getSaleUnitName());
            } else if (!DmShared.xNull(medicineMdl.getPurchaseUnitName()).trim().equalsIgnoreCase(DmShared.xNull(medicineMdl.getSaleUnitName()))) {
                txtConversionRate.setText(medicineMdl.getSaleUnitName() + " = " + medicineMdl.getConversionRate() + " " + medicineMdl.getSkuName());
//                txtConversionRateMRP.setText(medicineMdl.getSaleUnitName() + " = " + medicineMdl.getConversionRate() + " " + medicineMdl.getSkuName());
                txtFreeQtyConversionRate.setText(medicineMdl.getSaleUnitName() + " = " + medicineMdl.getConversionRate() + " " + medicineMdl.getSkuName());

            }
        }
        setDiscount(null, false);

        verifyITS(medicineMdl.getSharedKeyFieldCode(), DmShared.vNull(txtRate.getText()).doubleValue(), medicineMdl.getItsBasedOn(), DmShared.dNull(saleBill.txtDate.getText()));

        if (showBatchFlag) {
            showBatchDetailPopup();
        }
    }

    private void setDiscount(GrdMedicine_Mdl ourRow, boolean isCallFromDisplayRow) {
        if (saleBill.organizationMdl != null) {
            if (saleBill.organizationMdl.isPharmacyDiscount()) {
                txtDiscountPer.setText(DmShared.vNull(medicineMdl.getDiscountPer()).doubleValue() > 0 ? DmClient.vNull(medicineMdl.getDiscountPer(), "0.00") : DmClient.vNull(saleBill.organizationMdl.getPharmacyDiscountPer(), "0.00"));
            } else {
                if (isCallFromDisplayRow) {
                    txtDiscountPer.setText(DmClient.vNull(ourRow.getDiscountPer(), "0.00"));
                } else {
                    txtDiscountPer.setText("");
                }
            }
        } else {
            txtDiscountPer.setText(DmClient.vNull(medicineMdl.getDiscountPer(), "0.00"));
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
        txtSkuRate.setText("");
        txtSkuRate.setTag("");
        txtSkuRate.setTag2nd("");

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
        txtCGST.setText("0.00");
        txtGstRate.setText("0.00");
        txtGstRate.setTag("");
        txtGstRate.setTag2nd("");
        txtGstAmt.setText("0.00");
        txtGstAmt.setTag("");
        txtGstAmt.setTag2nd("");
        saleAcCode = "";
        reqDocId = "";
        reqVSno = "";
        requisitionNo = "";
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
        // vishvjeet.
       if (saleBill.saleOn == MedicineSaleOn.MRP) {
            tempRate = DmShared.vNull(batch_Mdl.getMrp()).doubleValue();
            tempSkuRate = DmShared.vNull(batch_Mdl.getSkuMrp()).doubleValue();
        } else if (saleBill.saleOn == MedicineSaleOn.COST_PRICE) {
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
        CalendarUtil.addDaysToDate(tempDate, (0 - DmShared.iNull(saleBill.expiryAlertBeforeDays).intValue()));

        if (tempDate.getTime() <= DmShared.convertDate(saleBill.txtDate.getText()).getTime()) {
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
                } else if (DmShared.xNull(iTS_Mdl.getOverHeadManualCode()).equalsIgnoreCase("SGST") && !saleBill.defaultValue.isUnionTerritory()) {
                    txtSgstUgstPer.setText(DmShared.xNull(iTS_Mdl.getAtRate()));
                    txtSgstUgstPer.setTag(DmShared.xNull(iTS_Mdl.getAccountCode()));
                } else if (DmShared.xNull(iTS_Mdl.getOverHeadManualCode()).equalsIgnoreCase("UGST") && saleBill.defaultValue.isUnionTerritory()) {
                    txtSgstUgstPer.setText(DmShared.xNull(iTS_Mdl.getAtRate()));
                    txtSgstUgstPer.setTag(DmShared.xNull(iTS_Mdl.getAccountCode()));
//                } else if (DmShared.xNull(iTS_Mdl.getOverHeadManualCode()).equalsIgnoreCase("G")) {
                } else if (DmShared.xNull(iTS_Mdl.getOverHeadManualCode()).equalsIgnoreCase("TV")) {
                    txtTaxableAmt.setTag(DmShared.xNull(iTS_Mdl.getAccountCode()));
                }
            }
            if (!saleAcCode.trim().equals("")) {
                txtTaxableAmt.setTag(DmShared.xNull(saleAcCode));
            }

            calculate();

        } else if (ourRow != null) {
            if (saleBill.defaultValue.isUnionTerritory()) {
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

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, SaleBill saleBill) {
        this.saleBill = saleBill;
        isRateEditable = saleBill.rateEditable;
        super.showCenter(currentRowIndex, okNeeded, newNeeded, deleteNeeded);
        flwAllowReturnValue.clear();
        if (DmShared.xNull(saleBill.voucher_Cat).equalsIgnoreCase(new ProjectVoucherCategory().PHARMACY_ESTIMATE)) {
            isEstimate = true;
        } else {
            isEstimate = false;
            if (this.saleBill.isAllowSaleReturn) {
                flwAllowReturnValue.add(cbReturnSale);
            }
        }

    }

    @Override
    public void displayRow(T1 row) {
        clear();
        lstMdlBatch = null;
        totalQtyBatchList = 0;
        GrdMedicine_Mdl ourRow = (GrdMedicine_Mdl) row;
        autoFillBatchFlag = false;
        hashCode = ourRow.getSharedKeyFieldHashCode();
        medicineMdl = ourRow.getMedicineMdl();
        saleAcCode = DmShared.xNull(ourRow.getSaleAcCode());
        discountGivenByTag = DmShared.xNull(ourRow.getDiscountGivenByTag());
        discountReason = DmShared.xNull(ourRow.getDiscountReason());

        //////////////// Gross Purchase Rate ///////////////////////
        if (!DmShared.xNull(ourRow.getGrossPurchaseRate()).equalsIgnoreCase("")) {
            grossPurchaseRate = DmShared.xNull(ourRow.getGrossPurchaseRate());
        }

        if (medicineMdl != null) {
            selectedMedicine = DmShared.xNull(medicineMdl.getSharedKeyFieldCode());
            selectedMedicineToFill = DmShared.xNull(medicineMdl.getSharedKeyFieldCode());
            selectedMedicineNameToFill = DmShared.xNull(medicineMdl.getSharedKeyFieldName());
            txtMedicineName.setTag(DmShared.xNull(medicineMdl.getSharedKeyFieldCode()));
            txtMedicineName.setText(DmShared.xNull(medicineMdl.getSharedKeyFieldName()));
            txtMedicineName.setTag2nd(DmShared.xNull(medicineMdl.getItemProperty()).trim());

            skuUnitName = DmShared.xNull(medicineMdl.getSkuName());
            isReturnableItem = medicineMdl.isReturnableMedicine();
            isDiscountAllow = saleBill.organizationMdl != null ? (saleBill.organizationMdl.isPharmacyDiscount() && saleBill.discountEditable && medicineMdl.isDiscountAllow()) : (saleBill.discountEditable && medicineMdl.isDiscountAllow());
            isAllowNegativeStock = medicineMdl.isAllowNegativeStock();
            isBarcodeActive = medicineMdl.isBarcodeActive();
            txtTotalQty.setTag(DmClient.vNull(ourRow.getQuantitySKU(), DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowDecimalPlaces()).intValue())));
            txtTotalQty.setText(DmClient.vNull(ourRow.getQuantity(), DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowDecimalPlaces()).intValue())));
            batchQtyInSaleUnit = DmClient.vNull(ourRow.getBatchQuantityInSaleUnit(), DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowDecimalPlaces()).intValue()));
            txtTotalQty.setTag2nd(DmClient.vNull(ourRow.getPrvQty(), DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowDecimalPlaces()).intValue())));

            txtFreeQty.setText(DmClient.vNull(ourRow.getFreeQuantityInSaleUnit(), DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowDecimalPlaces()).intValue())));
            txtFreeQty.setTag2nd(DmClient.vNull(ourRow.getPrvFreeQty(), DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowDecimalPlaces()).intValue())));

//            txtFreeQty.setTag(ourRow.getSchemeOnQty());
//            txtFreeQty.setTag2nd(ourRow.getSchemeFreeQty());
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
            txtMRP.setTag(DmShared.xNull(ourRow.getSkuMrp()));

            setDiscount(ourRow, true);

            isSaleRateEditable = DmShared.xNull(medicineMdl.getIsSaleRateEditable());
            txtConversionRate.setText("");
//            txtConversionRateMRP.setText("");
            txtFreeQtyConversionRate.setText("");
            if (DmShared.vNull(medicineMdl.getPurchaseUnitConversionRate()).doubleValue() > 0 && !DmShared.xNull(medicineMdl.getSaleUnitName()).equalsIgnoreCase("") && !DmShared.xNull(medicineMdl.getPurchaseUnitName()).equalsIgnoreCase("")) {
                if (DmShared.xNull(medicineMdl.getSkuName()).trim().equalsIgnoreCase(DmShared.xNull(medicineMdl.getSaleUnitName()))) {
                    txtConversionRate.setText(medicineMdl.getSkuName());
                    txtFreeQtyConversionRate.setText(medicineMdl.getSkuName());
//                    txtConversionRateMRP.setText(medicineMdl.getSkuName());
                } else if (!DmShared.xNull(medicineMdl.getSkuName()).trim().equalsIgnoreCase(DmShared.xNull(medicineMdl.getSaleUnitName()))) {
                    txtConversionRate.setText(medicineMdl.getSaleUnitName() + " = " + medicineMdl.getConversionRate() + " " + medicineMdl.getSkuName());
//                    txtConversionRateMRP.setText(medicineMdl.getSaleUnitName() + " = " + medicineMdl.getConversionRate() + " " + medicineMdl.getSkuName());
                    txtFreeQtyConversionRate.setText(medicineMdl.getSaleUnitName() + " = " + medicineMdl.getConversionRate() + " " + medicineMdl.getSkuName());

                }
            }
            setITS(medicineMdl.getLstITS(), ourRow);
        }
        txtRate.setText(DmClient.vNull(ourRow.getRate(), "0.00"));
        txtRate.setTag(DmShared.xNull(ourRow.getSkuRate()));
        txtSkuRate.setText(DmClient.vNull(ourRow.getSkuRate(), "0.00"));

        txtAmount.setText(DmClient.vNull(ourRow.getAmount(), "0.00"));
        txtNetAmount.setText(DmClient.vNull(ourRow.getNetAmt(), "0.00"));
        txtTaxableAmt.setText(DmClient.vNull(ourRow.getTaxableAmt(), "0.00"));

        totalDiscount = DmShared.vNull(ourRow.getTotalDiscount());
        txtDiscount.setText(DmClient.vNull(ourRow.getDiscount(), "0.00"));
        txtDiscount2.setText(DmClient.vNull(ourRow.getDiscount2(), "0.00"));
        txtDiscountPer2.setText(DmClient.vNull(ourRow.getDiscountPer2(), "0.00"));
        if (saleBill.defaultValue.isUnionTerritory()) {
            txtSgstUgst.setText(DmClient.vNull(ourRow.getUgstAmt(), "0.00"));
        } else {
            txtSgstUgst.setText(DmClient.vNull(ourRow.getSgstAmt(), "0.00"));
        }

        txtCGST.setText(DmClient.vNull(ourRow.getCgstAmt(), "0.00"));
        txtGstRate.setText(DmClient.vNull(ourRow.getGstPer(), "0.00"));
        txtGstAmt.setText(DmClient.vNull(ourRow.getGstAmount(), "0.00"));

        reqDocId = DmShared.xNull(ourRow.getReqDocId());
        reqVSno = DmShared.xNull(ourRow.getReqVSno());
        requisitionNo = DmShared.xNull(ourRow.getRequisitionNo());

        if (DmShared.vNull(ourRow.getQuantity()).doubleValue() < 0 || DmShared.vNull(ourRow.getFreeQuantityInSaleUnit()).doubleValue() < 0) {
            cbReturnSale.setValue(Boolean.TRUE);
        }

        manageBatchSelectedQty();
        setBatchLabelDesign();
        manageDisplay();
        if (DmShared.iNull(reqDocId).longValue() == 0) {
            if (txtBarcode.isEnabled()) {
                txtBarcode.setFocus(true);
            } else {
                txtMedicineName.setFocus(true);
            }

        } else {
            if (ourRow.isSchemeItem()) {
                txtFreeQty.setFocus(true);
            } else {
                txtTotalQty.setFocus(true);
            }
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
            if (DmShared.convertDate(txtExpiryDate.getText()).getTime() <= DmShared.convertDate(saleBill.txtDate.getText()).getTime()) {
                new DmAlertMessageBox("Expiry Date Cannot be less than or equal to bill date.", txtBatchNo).show();
                return;
            }
        }
        if (DmShared.vNull(txtFreeQty.getText()).doubleValue() == 0) {
            if (txtFreeQty.isEnabled()) {
                new DmAlertMessageBox("Free Quantity is Mandatory.", txtFreeQty).show();
                return;
            }
            if (DmShared.vNull(txtTotalQty.getText()).doubleValue() == 0) {
                new DmAlertMessageBox("Quantity is Mandatory.", txtTotalQty).show();
                return;
            }
            if (!autoFillBatchFlag) {
                if (!medicineMdl.isAllowNegativeStock()) {
                    if (DmShared.vNull(batchQtyInSaleUnit).doubleValue() > 0) {
                        if (DmShared.vNull(txtTotalQty.getText()).doubleValue() > DmShared.vNull(batchQtyInSaleUnit).doubleValue()) {
                            new DmAlertMessageBox("Not Enough Stock in this batch.<br/>Available Stock : " + batchQtyInSaleUnit + " " + medicineMdl.getSaleUnitName(), txtTotalQty).show();
                            return;
                        }
                    }
                }
            }

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
        if (DmShared.iNull(medicineMdl.getRestrictGroup()).longValue() > 0) {
            if (!saleBill.txtHIdType.isEnabled()) {
                if (DmShared.xNull(saleBill.txtHIdType.getText()).equalsIgnoreCase("")) {
                    saleBill.txtHIdType.setEnabled(true);
                }
            }
            if (!saleBill.txtIdNo.isEnabled()) {
                if (DmShared.xNull(saleBill.txtIdNo.getText()).equalsIgnoreCase("")) {
                    saleBill.txtIdNo.setEnabled(true);
                }
            }
            if (!saleBill.txtAddress.isEnabled()) {
                if (DmShared.xNull(saleBill.txtAddress.getText()).equalsIgnoreCase("")) {
                    saleBill.txtAddress.setEnabled(true);
                }
            }
            if (!saleBill.txtMobile.isEnabled()) {
                if (DmShared.xNull(saleBill.txtMobile.getText()).equalsIgnoreCase("")) {
                    saleBill.txtMobile.setEnabled(true);
                }
            }
        }
        for (GrdMedicine_Mdl detail : (List<GrdMedicine_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
            if (detail.getSharedKeyFieldHashCode() != hashCode) {
                int rowNo = getDmGrid().getGrid().getStore().getAll().indexOf(detail) + 1;
                if (DmShared.xNull(detail.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(txtMedicineName.getTag()))
                        && DmShared.xNull(detail.getIbCode()).equalsIgnoreCase(DmShared.xNull(txtBatchNo.getTag()))
                        && cbReturnSale.getValue() == detail.isReturnSale()) {
                    if (DmShared.vNull(txtFreeQty.getText()).doubleValue() != 0) {
                        if (DmShared.vNull(detail.getFreeQuantityInSaleUnit()).doubleValue() != 0) {
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
                if (DmShared.iNull(saleBill.defaultValue.getAllowReturnWithoutSale()).intValue() == 0) {
                    if (DmShared.iNull(saleBill.txtRegNo.getTag()).longValue() != 0 || DmShared.iNull(saleBill.txtUHIDNo.getTag()).longValue() != 0) {
                        srvAsync.invokeRPC().isItemEverReturned(txtMedicineName.getTag(), txtBatchNo.getTag(), saleBill.txtRegNo.getTag(), saleBill.txtUHIDNo.getTag(), saleBill.lmCode, txtTotalQty.getTag2nd(), txtFreeQty.getTag2nd(), new AsyncCallback<DmResult<String>>() {
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
                    proceedOk();
                }
            } else {
                new DmAlertMessageBox("Medicine cannot be returned as it is marked <b>'not returnable'</b>.", txtMedicineName).show();
            }
        } else {
            if (totalQtyBatchList > 0) {    //TBC
//                if ((!medicineMdl.isAllowNegativeStock()) && (DmShared.vNull(txtTotalQty.getText()).doubleValue() > totalQtyBatchList)) {
                if ((!medicineMdl.isAllowNegativeStock()) && (DmShared.vNull(txtTotalQty.getTag()).doubleValue() > totalQtyBatchList)) {
                    new DmAlertMessageBox("Cannot enter quantity more than stock quantity. Stock Quantity: " + totalQtyBatchList, txtTotalQty).show();
                } else {
                    proceedOk();
                    int gridLastRow = saleBill.grdMediDetails.getGrid().getStore().getAll().size() - 1;
                    ((CellSelectionModel) saleBill.grdMediDetails.getGrid().getSelectionModel()).selectCell(gridLastRow, 0);

                }
            } else {
                proceedOk();
            }
        }
    }

    @Override
    public void manageOk(T1 row) {

        GrdMedicine_Mdl ourRow = (GrdMedicine_Mdl) row;
        lstMdlGrd = new ArrayList<>();
        if (!cbReturnSale.getValue()) {
            if (DmShared.vNull(ourRow.getFreeQuantityInSaleUnit()).doubleValue() == 0) {
                autoFillBatch();
            }
        }
        medicineMdl.setDiscountPer(DmClient.vNull(txtDiscountPer.getText(), "0.00"));
//        medicineMdl.setIbCode(DmShared.iNull(txtBatchNo.getTag()).toString());
//        medicineMdl.setBatchNo(DmShared.xNull(txtBatchNo.getText()));
//        medicineMdl.setExpDate(DmShared.dNull(txtExpiryDate.getText()));
//        medicineMdl.setMfgDate(DmShared.dNull(txtManufacturingDate.getText()));

        ourRow.setMedicineMdl(medicineMdl);
        ourRow.setSharedKeyFieldCode(medicineMdl.getSharedKeyFieldCode());
        ourRow.setSharedKeyFieldName(medicineMdl.getSharedKeyFieldName());
        ourRow.setDiscountGivenByTag(discountGivenByTag);
        ourRow.setDiscountReason(discountReason);
        ourRow.setMedicineCategory(medicineMdl.getMedicineCategory());
        ourRow.setMedicineGroupCode(medicineMdl.getMedicineGroupCode());
        ourRow.setMedicineGroupName(medicineMdl.getMedicineGroupName());
        ourRow.setUnit(medicineMdl.getSaleUnitName());

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
        ourRow.setFreeQuantityInSaleUnit(DmClient.vNull(txtFreeQty.getText(), DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowDecimalPlaces()).intValue())));
        ourRow.setPrvFreeQty(DmClient.vNull(txtFreeQty.getTag2nd(), DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowDecimalPlaces()).intValue())));
        ourRow.setFreeQuantityInSku(DmClient.vNull((DmShared.vNull(txtFreeQty.getText()).doubleValue() * DmShared.vNull(medicineMdl.getConversionRate()).doubleValue()), DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowDecimalPlaces()).intValue())));
//        ourRow.setSchemeOnQty(DmShared.vNull(txtFreeQty.getTag()).toString());
//        ourRow.setSchemeFreeQty(DmShared.vNull(txtFreeQty.getTag2nd()).toString());
        ourRow.setFreeWithItemCode(freeWithItemCode);

        ourRow.setBatchQuantityInSaleUnit(DmClient.vNull(batchQtyInSaleUnit, DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowDecimalPlaces()).intValue())));
        ourRow.setQuantity(DmClient.vNull(txtTotalQty.getText(), DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowDecimalPlaces()).intValue())));
        ourRow.setPrvQty(DmClient.vNull(txtTotalQty.getTag2nd(), DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowDecimalPlaces()).intValue())));
        ourRow.setQuantitySKU(DmClient.vNull((DmShared.vNull(txtTotalQty.getTag()).doubleValue()), DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowSkuDecimalPlaces()).intValue())));

        totalQtyInSKu = DmShared.vNull(ourRow.getFreeQuantityInSku()).doubleValue() + DmShared.vNull(ourRow.getQuantitySKU()).doubleValue();
        ourRow.setTotalQuantitySKU(DmClient.vNull((DmShared.vNull(totalQtyInSKu).doubleValue()), DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowSkuDecimalPlaces()).intValue())));
        ourRow.setRate(DmClient.vNull(txtRate.getText(), "0.00"));
        ourRow.setMrp(DmClient.vNull(txtMRP.getText(), "0.00"));
        ourRow.setSkuMrp(DmShared.xNull(txtMRP.getTag()));
        ourRow.setSkuRate(DmShared.xNull(txtRate.getTag()));
        ourRow.setAmount(DmClient.vNull(txtAmount.getText(), "0.00"));
        ourRow.setTaxableAmt(DmClient.vNull(txtTaxableAmt.getText(), "0.00"));
        ourRow.setDiscount(DmClient.vNull(txtDiscount.getText(), "0.00"));
        ourRow.setDiscountPer(DmClient.vNull(medicineMdl.getDiscountPer(), "0.00"));
        ourRow.setDiscount2(DmClient.vNull(txtDiscount2.getText(), "0.00"));
        ourRow.setDiscountPer2(DmClient.vNull(txtDiscountPer2.getText(), "0.00"));
        ourRow.setTotalDiscount(DmClient.vNull(totalDiscount, "0.00"));
        ourRow.setCgstAmt(DmClient.vNull(txtCGST.getText(), "0.00"));
        ourRow.setCgstPer(DmClient.vNull(txtCGSTPer.getText(), "0.00"));
        ourRow.setCgstAcCode(DmShared.xNull(txtCGSTPer.getTag()));
        ourRow.setReturnSale(cbReturnSale.getValue());
        if (saleBill.defaultValue.isUnionTerritory()) {
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
        ourRow.setReqDocId(DmShared.xNull(reqDocId));
        ourRow.setReqVSno(DmShared.xNull(reqVSno));
        ourRow.setRequisitionNo(DmShared.xNull(requisitionNo));
        ourRow.setGrossPurchaseRate(DmShared.xNull(grossPurchaseRate));
        callBack.onOk(ourRow, lstMdlGrd);
    }

    @Override
    public void onNew() {
        proceedNew();
    }

    @Override
    public void onDelete() {
        if (saleBill.freeWithItems_Mdl != null) {
            if (saleBill.freeWithItems_Mdl.getLstFreeWithItemCodes().contains(DmShared.iNull(txtMedicineName.getTag()).toString()) && (DmShared.vNull(txtFreeQty.getText()).doubleValue() == 0)) {
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

    public MedicineDetailsEdit(DmGrid<T1, T2> grid) {
        super(grid, "Medicine Details Edit", "410px", "");
        srvAsync = new ServiceInvoker<>(GWT.create(SaleBill_Srv.class), SaleBill.class.getName());
        flwForm.addAttachHandler(this);
        manageEvents();
        textHelpEvents();
    }

    private void setBatchLabelDesign() {
        if (saleBill.isAllowAddBatch && (isAllowNegativeStock || cbReturnSale.getValue())) {
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
        if (saleBill.isAllowAddBatch && (isAllowNegativeStock || cbReturnSale.getValue())) {
            addBatchPopup.show(DmShared.xNull(txtMedicineName.getTag()), DmShared.xNull(saleBill.txtHDepartment.getTag()), DmShared.dNull(saleBill.txtDate.getText()), medicineMdl.isBestBefore(), isBarcodeActive, DmShared.vNull(medicineMdl.getPurchaseUnitConversionRate()).toString(), DmShared.xNull(saleBill.topControl.getMode()), skuUnitName, DmShared.xNull(medicineMdl.getPurchaseUnitName()));
        }
    }

    private void autoFillBatch() {
        if (lstMdlBatch != null) {
            double balanceQty = 0;
            if (DmShared.vNull(currentBatchQtyInSku).doubleValue() > 0) {
                balanceQty = DmShared.vNull(txtTotalQty.getTag()).doubleValue() - DmShared.vNull(currentBatchQtyInSku).doubleValue();
            }

            if (balanceQty > 0) {
                if (medicineMdl != null) {
                    currentBatchQtyInSaleUnit = String.valueOf(DmShared.vNull(currentBatchQtyInSku).doubleValue() / DmShared.vNull(medicineMdl.getConversionRate()).doubleValue());
                    if (lstMdlBatch.size() == 1 && medicineMdl.isAllowNegativeStock()) {
                        txtTotalQty.setText(DmClient.vNull(txtTotalQty.getText(), DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowDecimalPlaces()).intValue())));
                        balanceQty = 0;
                    } else {
                        txtTotalQty.setText(DmClient.vNull(currentBatchQtyInSaleUnit, DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowDecimalPlaces()).intValue())));
                    }

                    calculate();
                }
            }

            GrdMedicine_Mdl ourRow;
            lstMdlGrd = new ArrayList<>();
            if (lstMdlBatch != null && !lstMdlBatch.isEmpty()) {
                int i = 0;
                for (BatchNo_Mdl mdl : lstMdlBatch) {
                    ++i;
                    if (DmShared.iNull(mdl.getIbCode()).longValue() != DmShared.iNull(txtBatchNo.getTag()).longValue()) {
                        double tempRate = 0, tempSkuRate = 0, itemValue = 0, totalTaxRate = 0, taxableAmt = 0, totalTaxAmt = 0, tempTotalDiscount = 0;
                        String sgstUgstAmt = "";
                        if (balanceQty > 0 && DmShared.vNull(mdl.getBalanceQty()).doubleValue() > 0) {
                            ourRow = new GrdMedicine_Mdl();
                            TxtHMedicine_Mdl mdlMedicine = new TxtHMedicine_Mdl();
                            mdlMedicine = medicineMdl.clone();
                            mdlMedicine.setDiscountPer(DmClient.vNull(txtDiscountPer.getText(), "0.00"));
//                            mdlMedicine.setIbCode(DmShared.iNull(mdl.getIbCode()).toString());
//                            mdlMedicine.setBatchNo(DmShared.xNull(mdl.getBatchNo()));
//                            mdlMedicine.setExpDate(DmShared.dNull(mdl.getExpiryDate()));
//                            mdlMedicine.setMfgDate(DmShared.dNull(mdl.getManufacturingDate()));

                            ourRow.setReturnSale(cbReturnSale.getValue());
                            ourRow.setIbCode(DmShared.iNull(mdl.getIbCode()).toString());
                            ourRow.setBatchNo(DmShared.xNull(mdl.getBatchNo()));
                            ourRow.setBestBefore(mdlMedicine.isBestBefore());
                            ourRow.setExpDate(DmShared.dNull(mdl.getExpiryDate()));
                            ourRow.setMfgDate(DmShared.dNull(mdl.getManufacturingDate()));

                            ourRow.setMedicineMdl(mdlMedicine);
                            ourRow.setSharedKeyFieldCode(mdlMedicine.getSharedKeyFieldCode());
                            ourRow.setSharedKeyFieldName(mdlMedicine.getSharedKeyFieldName());
                            ourRow.setMedicineCategory(mdlMedicine.getMedicineCategory());
                            ourRow.setMedicineGroupCode(mdlMedicine.getMedicineGroupCode());
                            ourRow.setMedicineGroupName(mdlMedicine.getMedicineGroupName());
                            ourRow.setUnit(mdlMedicine.getSaleUnitName());

                            if (balanceQty > DmShared.vNull(mdl.getBalanceQty()).doubleValue() || balanceQty == DmShared.vNull(mdl.getBalanceQty()).doubleValue()) {
                                ourRow.setQuantitySKU(DmClient.vNull(mdl.getBalanceQty(), DmShared.getNumberFormat(DmShared.iNull(mdlMedicine.getAllowDecimalPlaces()).intValue())));
                            } else if (balanceQty < DmShared.vNull(mdl.getBalanceQty()).doubleValue()) {
                                ourRow.setQuantitySKU(DmClient.vNull(balanceQty, DmShared.getNumberFormat(DmShared.iNull(mdlMedicine.getAllowDecimalPlaces()).intValue())));
                            }
                            if (i == lstMdlBatch.size() && mdlMedicine.isAllowNegativeStock()) {
                                ourRow.setQuantitySKU(DmClient.vNull(balanceQty, DmShared.getNumberFormat(DmShared.iNull(mdlMedicine.getAllowDecimalPlaces()).intValue())));
                            }
                            //<editor-fold defaultstate="collapsed" desc="QTY IN SALE UNIT">
                            double qty = DmShared.vNull(ourRow.getQuantitySKU()) / DmShared.vNull(mdlMedicine.getConversionRate());
                            ourRow.setQuantity(DmClient.vNull(qty, DmShared.getNumberFormat(DmShared.iNull(mdlMedicine.getAllowDecimalPlaces()).intValue())));
                            //</editor-fold>
                            balanceQty -= DmShared.vNull(mdl.getBalanceQty()).doubleValue();
//                            ourRow.setPrvQty(DmClient.vNull(txtTotalQty.getTag2nd(), DmShared.getNumberFormat(DmShared.iNull(mdlMedicine.getAllowDecimalPlaces()).intValue())));
                            ourRow.setTotalQuantitySKU(DmClient.vNull((DmShared.vNull(ourRow.getQuantitySKU()).doubleValue()), DmShared.getNumberFormat(DmShared.iNull(mdlMedicine.getAllowSkuDecimalPlaces()).intValue())));
                            ourRow.setBatchQuantityInSaleUnit(DmShared.vNull(mdl.getBalanceQtyInSaleUnit()).toString());
//vishvjeet
                            if (saleBill.saleOn == MedicineSaleOn.MRP) {
                                tempRate = DmShared.vNull(mdl.getMrp()).doubleValue();
                                tempSkuRate = DmShared.vNull(mdl.getSkuMrp()).doubleValue();
                            } else if (saleBill.saleOn == MedicineSaleOn.COST_PRICE) {
                                tempSkuRate = DmShared.vNull(mdl.getSkuLandedRate()).doubleValue();
                                tempRate = DmShared.vNull(mdl.getSkuLandedRate()).doubleValue() * DmShared.vNull(medicineMdl.getPurchaseUnitConversionRate()).doubleValue();
                            } else {
                                tempRate = DmShared.vNull(mdl.getSaleRate()).doubleValue();
                                tempSkuRate = DmShared.vNull(mdl.getSkuSaleRate()).doubleValue();
                            }
                            if (!saleBill.isEstimate && saleBill.rateEditable && (DmShared.vNull(txtRate.getText()).doubleValue() < DmShared.vNull(mdl.getMrp()).doubleValue())) {
//                                tempRate = DmShared.vNull(txtRate.getText()).doubleValue();
//                                tempSkuRate = DmShared.vNull(txtRate.getTag()).doubleValue();
                            }
                            ourRow.setRate(DmClient.vNull(tempRate, "0.00"));
                            if (DmShared.vNull(mdlMedicine.getPurchaseUnitConversionRate()).doubleValue() > 0) {
                                tempSkuRate = DmShared.vNull(ourRow.getRate()).doubleValue() / DmShared.vNull(mdlMedicine.getPurchaseUnitConversionRate()).doubleValue();
                            } else {
                                tempSkuRate = 0;
                            }
                            ourRow.setSkuRate(DmClient.vNull(tempSkuRate, DmShared.getNumberFormat(7)));
                            ourRow.setMrp(DmClient.vNull(mdl.getMrp(), "0.00"));
                            ourRow.setSkuMrp(DmShared.xNull(mdl.getSkuMrp()));

                            ourRow.setAmount(DmClient.vNull((DmShared.vNull(ourRow.getQuantitySKU()).doubleValue() * DmShared.vNull(ourRow.getSkuRate()).doubleValue()), "0.00"));

                            ourRow.setDiscountPer(DmClient.vNull(txtDiscountPer.getText(), "0.00"));
                            ourRow.setDiscountPer2(DmClient.vNull(txtDiscountPer2.getText(), "0.00"));
                            if (DmShared.vNull(txtDiscountPer.getText()).doubleValue() > 0) {
                                ourRow.setDiscount(DmClient.vNull((DmShared.vNull(ourRow.getAmount()).doubleValue() * DmShared.vNull(txtDiscountPer.getText()).doubleValue() * 0.01), "0.00"));
                            } else {
//                                ourRow.setDiscount(DmClient.vNull(DmShared.vNull(ourRow.getDiscount()), "0.00"));
                            }
                            if (DmShared.vNull(txtDiscountPer2.getText()).doubleValue() > 0) {
                                double tempAmt = 0;
                                tempAmt = DmShared.vNull(ourRow.getAmount()).doubleValue() - (DmShared.vNull(ourRow.getDiscount()).doubleValue());
                                ourRow.setDiscount2(DmClient.vNull((tempAmt * DmShared.vNull(txtDiscountPer2.getText()).doubleValue() * 0.01), "0.00"));
                            } else {
//                                ourRow.setDiscount2(DmClient.vNull(DmShared.vNull(ourRow.getDiscount2()), "0.00"));
                            }

                            if (DmShared.vNull(ourRow.getDiscount2()).doubleValue() != 0) {
                                ourRow.setDiscountGivenByTag(discountGivenByTag);
                                ourRow.setDiscountReason(discountReason);
                            }

                            tempTotalDiscount = DmShared.vNull(ourRow.getDiscount()).doubleValue() + DmShared.vNull(ourRow.getDiscount2()).doubleValue();
                            itemValue = DmShared.vNull(ourRow.getAmount()).doubleValue() - tempTotalDiscount;
                            totalTaxRate = DmShared.vNull(txtCGSTPer.getText()).doubleValue() + DmShared.vNull(txtSgstUgstPer.getText()).doubleValue();
                            taxableAmt = (itemValue * 100.00) / (100.00 + totalTaxRate);

                            ourRow.setCgstAmt(DmClient.vNull((taxableAmt * DmShared.vNull(txtCGSTPer.getText()).doubleValue() * 0.01), "0.00"));
                            ourRow.setCgstPer(DmClient.vNull(txtCGSTPer.getText(), "0.00"));
                            ourRow.setCgstAcCode(DmShared.xNull(txtCGSTPer.getTag()));
                            sgstUgstAmt = DmClient.vNull((taxableAmt * DmShared.vNull(txtSgstUgstPer.getText()).doubleValue() * 0.01), "0.00");
                            if (saleBill.defaultValue.isUnionTerritory()) {
                                ourRow.setUgstAmt(DmClient.vNull(sgstUgstAmt, "0.00"));
                                ourRow.setUgstPer(DmClient.vNull(txtSgstUgstPer.getText(), "0.00"));
                                ourRow.setUgstAcCode(DmShared.xNull(txtSgstUgstPer.getTag()));
                            } else {
                                ourRow.setSgstAmt(DmClient.vNull(sgstUgstAmt, "0.00"));
                                ourRow.setSgstPer(DmClient.vNull(txtSgstUgstPer.getText(), "0.00"));
                                ourRow.setSgstAcCode(DmShared.xNull(txtSgstUgstPer.getTag()));
                            }
                            ourRow.setTaxableAmt(DmClient.vNull(taxableAmt, "0.00"));
                            ourRow.setNetAmt(DmClient.vNull(itemValue, "0.00"));
                            totalTaxAmt = DmShared.vNull(ourRow.getCgstAmt()).doubleValue() + DmShared.vNull(sgstUgstAmt).doubleValue();
                            ourRow.setGstPer(DmClient.vNull(totalTaxRate, "0.00"));
                            ourRow.setGstAmount(DmClient.vNull(totalTaxAmt, "0.00"));
                            ourRow.setTotalDiscount(DmClient.vNull(tempTotalDiscount, "0.00"));

                            //for managing minor 'paisa' differences in amount.
                            if (DmShared.vNull(ourRow.getNetAmt()).doubleValue() != (DmShared.vNull(ourRow.getTaxableAmt()).doubleValue() + totalTaxAmt)) {
                                taxableAmt = DmShared.vNull(ourRow.getTaxableAmt()).doubleValue() + (DmShared.vNull(ourRow.getNetAmt()).doubleValue() - (DmShared.vNull(ourRow.getTaxableAmt()).doubleValue() + totalTaxAmt));
                                ourRow.setTaxableAmt(DmClient.vNull(taxableAmt, "0.00"));
                            }

                            ourRow.setSaleAcCode(DmShared.xNull(txtTaxableAmt.getTag()));
                            ourRow.setReqDocId(DmShared.xNull(reqDocId));
                            ourRow.setReqVSno(DmShared.xNull(reqVSno));
                            ourRow.setRequisitionNo(DmShared.xNull(requisitionNo));

                            lstMdlGrd.add(ourRow);
                        } else if (medicineMdl.isAllowNegativeStock()) {
                            txtTotalQty.setText(DmClient.vNull(txtTotalQty.getText(), DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowDecimalPlaces()).intValue())));
                            calculate();
                            balanceQty = 0;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtTotalQty || event.getSource() == txtFreeQty || event.getSource() == txtRate || event.getSource() == txtDiscountPer || event.getSource() == txtDiscount || event.getSource() == txtDiscountPer2 || event.getSource() == txtDiscount2) {
            calculate();
        } else if (event.getSource() == txtRate) {
            if (DmShared.iNull(medicineMdl.getItsBasedOn()).intValue() == 1) {
                verifyITS(txtMedicineName.getTag(), DmShared.vNull(txtRate.getText()).doubleValue(), medicineMdl.getItsBasedOn(), DmShared.dNull(saleBill.txtDate.getText()));
            } else {
                calculate();
            }
        } else if (event.getSource() == txtBarcode) {
            if (!selectedBarCode.equalsIgnoreCase(DmShared.xNull(txtBarcode.getText())) || DmShared.iNull(txtMedicineName.getTag()).longValue() == 0) {
                txtMedicineName.setTag("");
                txtMedicineName.setText("");
                clearOnMedicineChange();
                if (!DmShared.xNull(txtBarcode.getText()).trim().equals("") && !saleBill.topControl.getMode().equalsIgnoreCase("Browse")) {
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
            if (saleBill.isAllowAddBatch) {
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

    private void calculate() {
        double purchaseUnitConversionRate = 0, saleUnitConversionRate = 0, itemValue = 0, totalTaxRate = 0, taxableAmt = 0, totalTaxAmt = 0;
        double tempSkuRate = 0;
        int saleUnitDecimalPlaces = 4, skuDecimalPlaces = 4;
        double tempQty = DmShared.vNull(txtTotalQty.getText()).doubleValue();
        double tempFreeQty = DmShared.vNull(txtFreeQty.getText()).doubleValue();

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

        if (cbReturnSale.getValue()) {
            if (tempQty > 0) {
                txtTotalQty.setText(DmShared.vNull(0 - tempQty).toString());
            } else if (tempFreeQty > 0) {
                txtFreeQty.setText(DmShared.vNull(0 - tempFreeQty).toString());
            }
        } else {
            if (tempQty < 0) {
                txtTotalQty.setText(DmShared.vNull((Math.abs(tempQty))).toString());
            } else if (tempFreeQty < 0) {
                txtFreeQty.setText(DmShared.vNull((Math.abs(tempFreeQty))).toString());
            }
        }

        txtFreeQty.setText(DmClient.vNull(txtFreeQty.getText(), DmShared.getNumberFormat(DmShared.iNull(saleUnitDecimalPlaces).intValue())));
        txtTotalQty.setText(DmClient.vNull(txtTotalQty.getText(), DmShared.getNumberFormat(DmShared.iNull(saleUnitDecimalPlaces).intValue())));
        txtTotalQty.setTag(DmClient.vNull((DmShared.vNull(txtTotalQty.getText()).doubleValue() * saleUnitConversionRate), DmShared.getNumberFormat(DmShared.iNull(skuDecimalPlaces).intValue())));

        txtAmount.setText(DmClient.vNull((DmShared.vNull(txtTotalQty.getTag()).doubleValue() * DmShared.vNull(txtRate.getTag()).doubleValue()), "0.00"));
        if (DmShared.vNull(txtDiscountPer.getText()).doubleValue() > 0) {
            txtDiscount.setText(DmClient.vNull((DmShared.vNull(txtAmount.getText()).doubleValue() * DmShared.vNull(txtDiscountPer.getText()).doubleValue() * 0.01), "0.00"));
        } else {
            txtDiscount.setText(DmClient.vNull(DmShared.vNull(txtDiscount.getText()), "0.00"));
        }
        if (DmShared.vNull(txtDiscountPer2.getText()).doubleValue() > 0) {
            double tempAmt = 0;
            tempAmt = DmShared.vNull(txtAmount.getText()).doubleValue() - (DmShared.vNull(txtDiscount.getText()).doubleValue());
            txtDiscount2.setText(DmClient.vNull((tempAmt * DmShared.vNull(txtDiscountPer2.getText()).doubleValue() * 0.01), "0.00"));
        } else {
            txtDiscount2.setText(DmClient.vNull(DmShared.vNull(txtDiscount2.getText()), "0.00"));
        }

        totalDiscount = DmShared.vNull(txtDiscount.getText()).doubleValue() + DmShared.vNull(txtDiscount2.getText()).doubleValue();
        itemValue = DmShared.vNull(txtAmount.getText()).doubleValue() - totalDiscount;
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
        reqDocId = "";
        reqVSno = "";
        requisitionNo = "";
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
        txtSgstUgst.setEnabled(false);
        txtSgstUgstPer.setEnabled(false);
        txtTaxableAmt.setEnabled(false);
        txtNetAmount.setEnabled(false);
        txtAmount.setEnabled(false);
        txtSkuRate.setEnabled(false);
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
        if (saleBill.defaultValue.isUnionTerritory()) {
            lblSgstUgst.setText("UGST Amount");
        } else {
            lblSgstUgst.setText("SGST Amount");
        }

        if (DmShared.vNull(txtFreeQty.getText()).doubleValue() > 0) {
            txtFreeQty.setEnabled(true);
            txtTotalQty.setEnabled(false);
        } else {
            txtTotalQty.setEnabled(true);
            txtFreeQty.setEnabled(false);
        }
        if (DmShared.iNull(reqDocId).longValue() == 0) {
            if (DmShared.vNull(txtFreeQty.getText()).doubleValue() > 0) {
                txtMedicineName.setEnabled(false);
                txtBarcode.setEnabled(false);
            } else {
                txtMedicineName.setEnabled(true);
                txtBarcode.setEnabled(DmShared.iNull(saleBill.defaultValue.getPharmacyBarcodeActive()).intValue() == 1);
            }
        } else {
            txtMedicineName.setEnabled(false);
            txtBarcode.setEnabled(false);
        }
        if (!saleBill.isEstimate) {
            txtTotalQty.setEnabled(false);
            txtFreeQty.setEnabled(false);
        }
    }

    private void showBatchDetailPopup() {
        if (!saleBill.topControl.getMode().equalsIgnoreCase("Browse")) {
            if (DmShared.iNull(txtMedicineName.getTag()).longValue() != 0) {
                batchDetail.show(txtMedicineName.getTag(), saleBill.txtHDepartment.getTag(), saleBill.txtDate.getText(), selectedBatchSQL, ItemBatchType.NON_EXPIRED_BATCH, cbReturnSale.getValue(), DmShared.xNull(txtBarcode.getText()), saleBill.saleOn, "", false);
            } else {
                new DmAlertMessageBox("Please select Medicine Name.", txtMedicineName).show();
            }
        }
    }

    private void barCodeSearch() {
        srvAsync.invokeRPC().getBarCodeSearch(DmShared.xNull(txtBarcode.getText()), DmShared.dNull(saleBill.txtDate.getText()), DmShared.iNull(saleBill.txtHDepartment.getTag()).toString(), DmShared.iNull(saleBill.defaultValue.getSiteCode()).toString(), saleBill.saleOn, new AsyncCallback<DmResult<BarCode_Mdl>>() {

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
        filter.setDepartmentCode(DmShared.xNull(saleBill.txtHDepartment.getTag()));
        filter.setDepartmentName(DmShared.xNull(saleBill.txtHDepartment.getText()));
        filter.setV_Date(DmShared.dNull(saleBill.txtDate.getText()));
        filter.setItemCodeCsv(DmShared.xNull(itemCodeCsv));
        medicineSearch.show(filter, searchChar, DmShared.xNull(saleBill.txtHVType.getTag()));
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
        batch_Mdl.setSkuLandedRate(DmShared.xNull(batchDetail.getSkuLandedRate()));
        setBatchDetail(batch_Mdl);
    }

}
