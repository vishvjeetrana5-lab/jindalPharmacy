package aarogya.pharmacy.d365StockRecive.client;

import aarogya.pharmacy.d365StockRecive.shared.*;
import aarogya.pharmacy.medicineSearch.client.MedicineSearch;
import aarogya.pharmacy.medicineSearch.shared.MedicineSearchFilter_Mdl;
import aarogya.pharmacy.medicineSearch.shared.MedicineSearch_Mdl;
import aarogya.selfToolkit.itemUpdatePopup.client.ItemUpdatePopup;
import aarogya.selfToolkit.itemUpdatePopup.shared.ItemUpdateField_Mdl;
import aarogya.selfToolkit.itemUpdatePopup.shared.ItemUpdateField_Mdl.Field;
import aarogya.selfToolkit.stStatic.client.STClient;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.List;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmBase.self.shared.DmEnum.ItsBasedOn;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GRNGridEdit<T1, T2> extends GRNGridEdit_Dsg<T1, T2> implements BlurHandler, KeyDownHandler, ClickHandler, AttachEvent.Handler {

    private final ServiceInvoker<GRN_SrvAsync> srvAsync;
    private final ImpurityPopup impurityPopup = new ImpurityPopup();
    private final ItemUpdatePopup itemUpdatePopup = new ItemUpdatePopup();
    private final MedicineSearch medicineSearch = new MedicineSearch(null, null);
    private int hashCode = 0;
    List<GrdImpurity_Mdl> lstImpurity = new ArrayList<>();
    List<ItemUpdateField_Mdl> lstItemUpdateFields = new ArrayList<>();
    GRN grnEntry;
    String skuCode = "", skuName = "", skuAllowDecimalPlaces = "", purchaseUnitName = "", hsnCode = "", itemGSTNature = "",
            itsBasedOn = "", isBestBefore = "", isIncludeFreeQtyInSaleRate = "", purchaseUnitConversionRate = "",
            skuRate = "", skuMrp = "", skuSaleRate = "", skuStaffSaleRate = "", v_SNo = "", ibCode = "", itemCategoryProffitMargine = "";
    String masterMrp = "", masterSaleRate = "", masterPurchaseRate = "", masterSchemeOnQty = "", masterSchemeFreeQty = "", masterPurchaseDiscountPer = "";
    String selectedMedCode = "", selectedOrderNoCode = "", selectedBatchNoText = "";
    boolean isFirstShow = true, inventoryAccountingActive = false;
    String isBarcodeActive = "", podMrp = "";

    public GRNGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Medicine Detail", "775px", "455px");
        srvAsync = new ServiceInvoker<GRN_SrvAsync>(GWT.create(GRN_Srv.class), GRN.class.getName());
        textHelpEvents();
        popupCallBacks();
        gridEvents();
        manageEvent();
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, GRN grn, boolean inventoryAccountingActive) {
        resetPopup();
        this.grnEntry = grn;
        this.inventoryAccountingActive = inventoryAccountingActive;
        itemCategoryProffitMargine = "";
        super.showCenter(currentRowIndex, okNeeded, newNeeded, deleteNeeded);
        flwOHL.clear();
        if (isFirstShow) {
            isFirstShow = false;
        }
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtHOrderNo.setFocus(true);
            }
        });
    }

    private void setIts(final String itemCode, final String itemRate, final String location, final String itsBasedOn, final String billDate) {
//        srvAsync.invokeRPC(true).getITS(itemCode, itemRate, location, itsBasedOn, billDate, new AsyncCallback<DmResult<List<OverHeadITS_Mdl>>>() {
//
//            @Override
//            public void onFailure(Throwable caught) {
//                new DmAlertMessageBox(caught.getMessage(), txtHMedName).show();
//            }
//
//            @Override
//            public void onSuccess(DmResult<List<OverHeadITS_Mdl>> result) {
//                if (result.isStatus()) {
//                    overHeadLine.setITS(result.getData());
//                    calculate();
//                } else {
//                    new DmAlertMessageBox(result.getErrorMessage(), txtHMedName).show();
//                }
//            }
//        });
    }

    private void manageDisplay() {
        setQtyUnits();
        txtAmount.setEnabled(false);
        txtPoRefNo.setEnabled(false);
        txtAcceptQty.setEnabled(false);
        txtSKUQty.setEnabled(false);
        txtSkuQtyUnit.setEnabled(false);
        txtAcceptQtyUnit.setEnabled(false);

        if (DmShared.iNull(isBestBefore).intValue() == 1) {
            txtManufacturingDate.setEnabled(true);
            txtBestBefore.setEnabled(true);
            txtExpDate.setEnabled(false);
        } else {
            txtManufacturingDate.setEnabled(false);
            txtBestBefore.setEnabled(false);
            txtExpDate.setEnabled(true);
        }
        if (!DmShared.xNull(txtHOrderNo.getTag()).equalsIgnoreCase("")) {
            txtHMedName.setEnabled(false);
            txtRate.setEnabled(false);
        } else {
            txtHMedName.setEnabled(true);
            txtRate.setEnabled(true);
        }
        if (DmShared.iNull(isBarcodeActive).intValue() == 1) {
            if (!txtBarcode.isEnabled()) {
                txtBarcode.setEnabled(true);
            }
        } else {
            txtBarcode.setEnabled(false);
            if (grnEntry.topControl.getMode().equals("Edit")) {
                if (!DmShared.xNull(txtBarcode.getText()).trim().equals("")) {
                    if (!txtBarcode.isEnabled()) {
                        txtBarcode.setEnabled(true);
                    }
                }
            }
        }
        if (grnEntry.defaultValue.isPh_SRateEditInPurchase()) {
            txtSaleRate.setEnabled(false);
            txtStaffSaleRate.setEnabled(false);
        }
    }

    public void calculate() {
        double freeQtyPerPack = 0;
        double purchaseUnitConvRate = DmShared.vNull(this.purchaseUnitConversionRate);
        double recQty = DmShared.vNull(txtReceiveQty.getText());
        double rejQty = DmShared.vNull(txtRejectedQty.getText());
        double freeQty = DmShared.vNull(txtFreeQty.getText());
        double skuDifferenceQty = DmShared.vNull(txtSkuDifferenceQty.getText());
        double rate = DmShared.vNull(txtRate.getText());
        double mrp = DmShared.vNull(txtMRP.getText());
        double totalTaxRate = 0;

        String unitDecimalFormat = DmShared.getNumberFormat(DmShared.iNull(txtPurchaseUnit.getTag2nd()).intValue());
        String skuDecimalFormat = DmShared.getNumberFormat(DmShared.iNull(skuAllowDecimalPlaces).intValue());

        /*<============================Calculate Total Tax Rate for item=======================================>*/
//        OverHeadLine_Mdl ohlMdl = OverHeadLine_Shared.getOHByManualCode("CGST", overHeadLine.getOverHeadList());
//        totalTaxRate += DmShared.vNull(ohlMdl.getAtRate());
//        ohlMdl = OverHeadLine_Shared.getOHByManualCode("SGST", overHeadLine.getOverHeadList());
//        totalTaxRate += DmShared.vNull(ohlMdl.getAtRate());
//        ohlMdl = OverHeadLine_Shared.getOHByManualCode("IGST", overHeadLine.getOverHeadList());
//        totalTaxRate += DmShared.vNull(ohlMdl.getAtRate());
//        ohlMdl = OverHeadLine_Shared.getOHByManualCode("UGST", overHeadLine.getOverHeadList());
//        totalTaxRate += DmShared.vNull(ohlMdl.getAtRate());
        /*<====================================================================================================>*/
        if (!grnEntry.defaultValue.isCalculateSaleRate_On_TaxInclusiveValue()) {
            rate = DmShared.vNull(txtRate.getText());
        } else {
            if (DmShared.vNull(recQty) > 0) {
                rate = DmShared.vNull(rate + (rate * totalTaxRate) / 100);
            }
        }
//        ohlMdl = OverHeadLine_Shared.getOHByUniqueId("IV", overHeadLine.getOverHeadList());

        txtRejectedQty.setText(DmClient.vNull(txtRejectedQty.getText(), unitDecimalFormat));

        txtSkuDifferenceQty.setText(DmClient.vNull(txtSkuDifferenceQty.getText(), skuDecimalFormat));

        txtAcceptQty.setText(DmClient.vNull(recQty - rejQty, unitDecimalFormat));

        txtSKUQty.setText(DmClient.vNull((DmShared.vNull(txtAcceptQty.getText()) * purchaseUnitConvRate) + skuDifferenceQty + (freeQty * purchaseUnitConvRate), skuDecimalFormat));

        txtAmount.setText(DmClient.vNull(DmShared.vNull(txtAcceptQty.getText()) * DmShared.vNull(txtRate.getText()), "0.00"));

        if (DmShared.vNull(txtAcceptQty.getText()) > 0) {
            freeQtyPerPack = freeQty / DmShared.vNull(txtAcceptQty.getText());
        } else {
            freeQtyPerPack = 0;
        }
//NOw It will fill from Dynamics
          final double saleRate = 0;
        //final double saleRate = STClient.getSaleRate(mrp, rate, purchaseUnitConvRate, freeQtyPerPack, isIncludeFreeQtyInSaleRate, grnEntry.defaultValue.getLstSaleRateRule(), itemCategoryProffitMargine);

        if (DmShared.vNull(txtSaleRate.getText()) == 0) {
            txtSaleRate.setText(DmClient.vNull(saleRate, "0.00"));
            txtSaleRate.setTag(txtSaleRate.getText());
        } else if (!txtSaleRate.isEnabled()) {
            txtSaleRate.setText(DmClient.vNull(saleRate, "0.00"));
            txtSaleRate.setTag(txtSaleRate.getText());
        }
        if (DmShared.iNull(isIncludeFreeQtyInSaleRate).intValue() == 1) {
            rate = DmShared.vNull(rate).doubleValue() / (1 + DmShared.vNull(freeQtyPerPack).doubleValue());
        }
        txtStaffSaleRate.setText(DmClient.vNull(rate, "0.00"));
        txtStaffSaleRate.setTag(txtStaffSaleRate.getText());

        skuMrp = DmClient.vNull(mrp / purchaseUnitConvRate, "0.0000000");
        skuSaleRate = DmClient.vNull(DmShared.vNull(txtSaleRate.getText()) / purchaseUnitConvRate, "0.0000000");
        skuStaffSaleRate = DmClient.vNull(DmShared.vNull(txtStaffSaleRate.getText()) / purchaseUnitConvRate, "0.0000000");

        skuRate = DmClient.vNull(DmShared.vNull(txtAmount.getText()) / DmShared.vNull(txtSKUQty.getText()), "0.0000000");

//        overHeadLine.getDmGrid().commitChanges();
//        overHeadLine.calculate(DmClient.vNull(txtAmount.getText(), "0.00"));
        //<editor-fold defaultstate="collapsed" desc="Format decimal places of all quantities">
        txtChallanQty.setText(DmClient.vNull(txtChallanQty.getText(), unitDecimalFormat));
        txtReceiveQty.setText(DmClient.vNull(txtReceiveQty.getText(), unitDecimalFormat));
        txtRejectedQty.setText(DmClient.vNull(txtRejectedQty.getText(), unitDecimalFormat));
        txtAcceptQty.setText(DmClient.vNull(txtAcceptQty.getText(), unitDecimalFormat));
        txtFreeQty.setText(DmClient.vNull(txtFreeQty.getText(), unitDecimalFormat));
        txtSkuDifferenceQty.setText(DmClient.vNull(txtSkuDifferenceQty.getText(), skuDecimalFormat));
        txtSKUQty.setText(DmClient.vNull(txtSKUQty.getText(), skuDecimalFormat));
        //</editor-fold>

        manageDisplay();
    }

    //<editor-fold defaultstate="collapsed" desc="GridEdit Overrides">
    @Override
    public void onOk() {
        try {
//            calculate();
            if (DmShared.iNull(txtHMedName.getTag()).longValue() == 0) {
                new DmAlertMessageBox("Medicine Name is mandatory.", txtHMedName).show();
                return;
            }
            if (DmShared.iNull(txtPurchaseUnit.getTag()).longValue() == 0) {
                new DmAlertMessageBox("Unit is mandatory.", txtPurchaseUnit).show();
                return;
            }
            if (DmShared.vNull(txtReceiveQty.getText()) == 0 && DmShared.vNull(txtChallanQty.getText()) != 0) {
                new DmAlertMessageBox("Received Qty. is mandatory with Challan Qty.", txtReceiveQty).show();
                return;
            }
            if (DmShared.vNull(txtReceiveQty.getText()) > DmShared.vNull(txtChallanQty.getText())) {
                new DmAlertMessageBox("Received Qty. should be less than or equal to Challan Qty.", txtReceiveQty).show();
                return;
            }
            if (DmShared.vNull(txtRejectedQty.getText()) > DmShared.vNull(txtReceiveQty.getText())) {
                new DmAlertMessageBox("Rejected Qty. should be less than or equal to Received Qty.", txtRejectedQty).show();
                return;
            }
            if (DmShared.vNull(txtFreeQty.getText()) == 0 && DmShared.vNull(txtAcceptQty.getText()) == 0) {
                new DmAlertMessageBox("At least one is mandatory from Free Qty. and Accept Qty.", txtFreeQty).show();
                return;
            }
            if (DmShared.vNull(txtSKUQty.getText()) <= 0) {
                new DmAlertMessageBox("SKU Qty. is mandatory.", txtSKUQty).show();
                return;
            }
            if (DmShared.vNull(txtAcceptQty.getText()) != 0 && DmShared.vNull(txtRate.getText()) == 0) {
                new DmAlertMessageBox("Rate is mandatory.", txtRate).show();
                return;
            }
            if (DmShared.vNull(txtAcceptQty.getText()) != 0 && DmShared.vNull(txtAmount.getText()) == 0) {
                new DmAlertMessageBox("Amount is mandatory.", txtRate).show();
                return;
            }
            if (DmShared.xNull(txtHBatchNo.getText()).trim().equalsIgnoreCase("")) {
                new DmAlertMessageBox("Batch No. is mandatory.", txtHBatchNo).show();
                return;
            }
            if (DmShared.vNull(txtMRP.getText()) == 0) {
                new DmAlertMessageBox("MRP is mandatory.", txtMRP).show();
                return;
            }

            if (grnEntry.defaultValue.isPh_SRateEditInPurchase()) {
                if (DmShared.vNull(txtMRP.getText()) < DmShared.vNull(txtSaleRate.getText())) {
                    new DmConfirmMessageBox("Sale Rate is greater than MRP. Do you want to continue with MRP as sale rate ?", new DialogHideEvent.DialogHideHandler() {
                        @Override
                        public void onDialogHide(DialogHideEvent event) {
                            if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                                txtSaleRate.setText(txtMRP.getText());
                                txtSaleRate.setTag(txtMRP.getText());
                                skuSaleRate = DmClient.vNull(DmShared.vNull(txtSaleRate.getText()) / DmShared.vNull(purchaseUnitConversionRate), "0.0000000");
                            } else {
                                txtMRP.setFocus(true);
                                return;
                            }
                        }
                    }).show();
                    return;
                }
                if (DmShared.vNull(txtMRP.getText()) < DmShared.vNull(txtStaffSaleRate.getText())) {
                    new DmConfirmMessageBox("Staff Rate is greater than MRP. Do you want to continue with MRP as Staff rate ?", new DialogHideEvent.DialogHideHandler() {
                        @Override
                        public void onDialogHide(DialogHideEvent event) {
                            if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                                txtStaffSaleRate.setText(txtMRP.getText());
                                txtStaffSaleRate.setTag(txtMRP.getText());
                                skuStaffSaleRate = DmClient.vNull(DmShared.vNull(txtStaffSaleRate.getText()) / DmShared.vNull(purchaseUnitConversionRate), "0.0000000");
                            } else {
                                txtMRP.setFocus(true);
                                return;
                            }
                        }
                    }).show();
                    return;
                }
            } else {
                if (DmShared.vNull(txtSaleRate.getText()) > DmShared.vNull(txtMRP.getText())) {
                    new DmAlertMessageBox("Sale Rate should be less than or equal to MRP.", txtSaleRate).show();
                    return;
                }
                if (DmShared.vNull(txtStaffSaleRate.getText()) > DmShared.vNull(txtMRP.getText())) {
                    new DmAlertMessageBox("Staff Rate should be less than or equal to MRP.", txtStaffSaleRate).show();
                    return;
                }
            }

            if (!DmShared.dNull(txtManufacturingDate.getText()).equalsIgnoreCase("")) {
                if (DmShared.convertDate(txtManufacturingDate.getText()).getTime() > DmShared.convertDate(DmShared.dNull(grnEntry.txtChallanDate.getText())).getTime()) {
                    new DmAlertMessageBox("Manufacturing date should be less than or equal to ref. date.", txtManufacturingDate).show();
                    return;
                }
            }
            if (DmShared.dNull(txtExpDate.getText()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Expiry Date is mandatory.", txtExpDate).show();
                return;
            }
            if (DmShared.convertDate(txtExpDate.getText()).getTime() <= DmShared.convertDate(DmShared.dNull(grnEntry.txtChallanDate.getText())).getTime()) {
                new DmAlertMessageBox("Expiry date should be greater than ref. date.", txtExpDate.isEnabled() ? txtExpDate : txtManufacturingDate).show();
                return;
            }
            if (DmShared.iNull(txtHOrderNo.getTag()).longValue() != 0) {
                if (DmShared.vNull(txtAcceptQty.getTag()) > DmShared.vNull(txtAcceptQty.getTag2nd())) {
                    new DmAlertMessageBox("Accept Qty. should be less than or equal to Pending Qty. (" + DmShared.xNull(txtAcceptQty.getTag2nd()) + ").", txtReceiveQty).show();
                    return;
                }
            }
            if (DmShared.vNull(txtSaleRate.getText()).equals(0)) {
                new DmAlertMessageBox("Sale Rate is mandatory.", txtSaleRate).show();
                return;
            }
            if (DmShared.vNull(txtMRP.getText()) < DmShared.vNull(txtRate.getText())) {
                new DmAlertMessageBox("MRP should be greater than or equal to rate.", txtMRP).show();
                return;
            }
            if (DmShared.iNull(isIncludeFreeQtyInSaleRate).intValue() == 0) {
                if (DmShared.vNull(txtSaleRate.getText()) < DmShared.vNull(txtRate.getText())) {
                    new DmAlertMessageBox("Sale rate should not be less than rate.", txtRate).show();
                    return;

                }
            }

            if (DmShared.iNull(isBarcodeActive).intValue() == 1) {
                if (DmShared.xNull(txtBarcode.getText()).trim().equalsIgnoreCase("")) {
                    new DmAlertMessageBox("Barcode is mandatory.", txtBarcode).show();
                    return;
                }
            } else {
                if (grnEntry.topControl.getMode().equals("Edit")) {
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

            /*==============================<< Check Item duplicacy with Batch No and Order No. >>====================================*/
            for (GrdGrnDetail_Mdl detail_Mdl : (List<GrdGrnDetail_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
                if (DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(txtHMedName.getTag()))
                        && DmShared.xNull(detail_Mdl.getBatchNo()).equalsIgnoreCase(DmShared.xNull(txtHBatchNo.getText()))
                        && DmShared.xNull(detail_Mdl.getBarcode()).equalsIgnoreCase(DmShared.xNull(txtBarcode.getText()))
                        && DmShared.vNull(detail_Mdl.getMrp()).doubleValue() == DmShared.vNull(txtMRP.getText()).doubleValue()
                        && DmShared.dNull(detail_Mdl.getManufacturingDate()).equalsIgnoreCase(DmShared.dNull(txtManufacturingDate.getText()))
                        && DmShared.dNull(detail_Mdl.getExpiryDate()).equalsIgnoreCase(DmShared.dNull(txtExpDate.getText()))
                        && DmShared.xNull(detail_Mdl.getPurchaseUnitCode()).equalsIgnoreCase(DmShared.xNull(txtPurchaseUnit.getTag()))
                        && DmShared.xNull(detail_Mdl.getPoDocId()).equalsIgnoreCase(DmShared.xNull(txtHOrderNo.getTag()))
                        && DmShared.xNull(detail_Mdl.getPoV_SNo()).equalsIgnoreCase(DmShared.xNull(txtHOrderNo.getTag2nd()))) {
                    if (!DmClient.vNull(detail_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {

                        String message = "<div style='float:right; margin-right: 5px;'>";
                        message += "Record already exist with following details - <br>";
                        message += (DmShared.xNull(txtHOrderNo.getText()).trim().equals("")) ? "" : "<b>Order No.:</b> " + DmShared.xNull(txtHOrderNo.getText()) + "<br>";
                        message += (DmShared.xNull(txtHMedName.getText()).trim().equals("")) ? "" : "<b>Medicine:</b> " + DmShared.xNull(txtHMedName.getText()) + "<br>";
                        message += (DmShared.xNull(txtPurchaseUnit.getText()).trim().equals("")) ? "" : "<b>Unit:</b> " + DmShared.xNull(txtPurchaseUnit.getText()) + "<br>";
                        message += (DmShared.xNull(txtHBatchNo.getText()).trim().equals("")) ? "" : "<b>Batch No.:</b> " + DmShared.xNull(txtHBatchNo.getText()) + "<br>";
                        message += (DmShared.xNull(txtBarcode.getText()).trim().equals("")) ? "" : "<b>Barcode:</b> " + DmShared.xNull(txtBarcode.getText()) + "<br>";
                        message += (DmShared.vNull(txtMRP.getText()) == 0) ? "" : "<b>MRP:</b> " + DmClient.vNull(txtMRP.getText(), "0.00") + "<br>";
                        message += (DmShared.dNull(txtManufacturingDate.getText()).trim().equals("")) ? "" : "<b>Manufacturing Date:</b> " + DmShared.dNull(txtManufacturingDate.getText()) + "<br>";
                        message += (DmShared.dNull(txtExpDate.getText()).trim().equals("")) ? "" : "<b>Expiry Date:</b> " + DmShared.dNull(txtExpDate.getText()) + "<br>";
                        message += "</div>";
                        new DmAlertMessageBox(message, txtHMedName).show();
                        return;
                    }
                }
            }

            /*==============================<< Check overheadLine conditions >>====================================*/
//            OverHeadLine_Shared.chkGridSingleOH(overHeadLine.getOverHeadList());
            if (!DmShared.vNull(txtSaleRate.getText()).equals(DmShared.vNull(txtSaleRate.getTag()))) {
                new DmConfirmMessageBox("Do you want to continue without updating sale rate to " + DmClient.vNull(txtSaleRate.getTag(), "0.00"), new DialogHideEvent.DialogHideHandler() {
                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.NO) {
                            txtSaleRate.setText(DmClient.vNull(txtSaleRate.getTag(), "0.00"));
                        }
                        if (DmShared.vNull(txtSaleRate.getText()) > DmShared.vNull(txtMRP.getText())) {
                            new DmAlertMessageBox("Sale Rate should be less than or equal to MRP.", txtSaleRate).show();
                            return;
                        }
                        showItemUpdatePopup();
                    }
                }).show();
            } else {
                showItemUpdatePopup();
            }
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtHMedName).show();
        }
    }

    @Override
    public void manageOk(T1 row) {
        GrdGrnDetail_Mdl ourRow = (GrdGrnDetail_Mdl) row;

        //Item
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHMedName.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHMedName.getText()));
        ourRow.setIbCode(DmShared.xNull(ibCode));

        //Item Details
        ourRow.setPropertyList(DmShared.xNull(txtHMedName.getTag2nd()));
        ourRow.setItemGSTNature(DmShared.xNull(itemGSTNature));
        ourRow.setItsBasedOn(DmShared.xNull(itsBasedOn));
        ourRow.setHsnCode(DmShared.xNull(hsnCode));
        ourRow.setIsBestBefore(DmShared.xNull(isBestBefore));
        ourRow.setMasterMrp(DmShared.xNull(masterMrp));
        ourRow.setMasterSaleRate(DmShared.xNull(masterSaleRate));
        ourRow.setMasterPurchaseRate(DmShared.xNull(masterPurchaseRate));
        ourRow.setMasterSchemeOnQty(DmShared.xNull(masterSchemeOnQty));
        ourRow.setMasterSchemeFreeQty(DmShared.xNull(masterSchemeFreeQty));
        ourRow.setMasterPurchaseDiscountPer(DmShared.xNull(masterPurchaseDiscountPer));
        ourRow.setIsIncludeFreeQtyInSaleRate(DmShared.xNull(isIncludeFreeQtyInSaleRate));
        ourRow.setSaleRateProffitMargine(DmShared.xNull(itemCategoryProffitMargine));

        // SET podMrp
        ourRow.setPodMrp(DmShared.xNull(podMrp));

        //SKU
        ourRow.setSkuCode(DmShared.xNull(skuCode));
        ourRow.setSkuName(DmShared.xNull(skuName));
        ourRow.setSkuDecimalPlaces(DmShared.xNull(skuAllowDecimalPlaces));

        //Purchase Unit
        ourRow.setPurchaseUnitCode(DmShared.xNull(txtPurchaseUnit.getTag()));
        ourRow.setPurchaseUnitName(DmShared.xNull(purchaseUnitName));
        ourRow.setPurchaseUnitDisplayName(DmShared.xNull(txtPurchaseUnit.getText()));
        ourRow.setPurchaseUnitDecimalPlaces(DmShared.xNull(txtPurchaseUnit.getTag2nd()));
        ourRow.setPurchaseUnitConversionRate(DmShared.xNull(purchaseUnitConversionRate));

        //Properties
        ourRow.setBatchNo(DmShared.xNull(txtHBatchNo.getText()));
        ourRow.setManufacturingDate(DmShared.xNull(txtManufacturingDate.getText()));
        ourRow.setBestBeforeDays(DmShared.xNull(txtBestBefore.getText()));
        ourRow.setExpiryDate(DmShared.xNull(txtExpDate.getText()));

        //Order Details
        ourRow.setPoDocId(DmShared.xNull(txtHOrderNo.getTag()));
        ourRow.setPoRecId(DmShared.xNull(txtHOrderNo.getText()));
        ourRow.setPoV_SNo(DmShared.xNull(txtHOrderNo.getTag2nd()));
        ourRow.setPoRefNo(DmShared.xNull(txtPoRefNo.getText()));

        //Quantities
        ourRow.setChallanQty(DmShared.xNull(txtChallanQty.getText()));
        ourRow.setRecQty(DmShared.xNull(txtReceiveQty.getText()));
        ourRow.setRejQty(DmShared.xNull(txtRejectedQty.getText()));
        ourRow.setFreeQty(DmShared.xNull(txtFreeQty.getText()));
        ourRow.setSkuDifferenceQty(DmShared.xNull(txtSkuDifferenceQty.getText()));
        ourRow.setSkuQty(DmShared.xNull(txtSKUQty.getText()));
        ourRow.setAcceptQty(DmShared.xNull(txtAcceptQty.getText()));
        ourRow.setPendingQty(DmShared.xNull(txtAcceptQty.getTag2nd()));

        //Rate
        ourRow.setMrp(DmShared.xNull(txtMRP.getText()));
        ourRow.setSkuMrp(DmShared.xNull(skuMrp));
        ourRow.setRate(DmShared.xNull(txtRate.getText()));
        ourRow.setSaleRate(DmShared.xNull(txtSaleRate.getText()));
        ourRow.setSkuSaleRate(DmShared.xNull(skuSaleRate));
        ourRow.setStaffSaleRate(DmShared.xNull(txtStaffSaleRate.getText()));
        ourRow.setSkuStaffSaleRate(DmShared.xNull(skuStaffSaleRate));
        ourRow.setSkuRate(DmShared.xNull(skuRate));
        ourRow.setAmount(DmShared.xNull(txtAmount.getText()));

        //Other
        ourRow.setRemark(DmShared.xNull(txtRemark.getText()));
//        ourRow.setOHLList(overHeadLine.getOverHeadList());

        ourRow.getLstImpurity().clear();
        ourRow.getLstImpurity().addAll(lstImpurity);

        ourRow.getLstItemUpdateFields().clear();
        ourRow.getLstItemUpdateFields().addAll(lstItemUpdateFields);

        ourRow.setIsBarcodeActive(DmShared.xNull(isBarcodeActive));
        ourRow.setBarcode(DmShared.xNull(txtBarcode.getText()));

        callBack.onOk(ourRow);
    }
    private boolean poMrpExist = false;

    @Override
    public void displayRow(T1 row) {

        GrdGrnDetail_Mdl ourRow = (GrdGrnDetail_Mdl) row;

        //Clear
        resetPopup();
        poMrpExist = ourRow.isPoMrpExist();
        //Item
        ibCode = DmShared.xNull(ourRow.getIbCode());
        selectedMedCode = ourRow.getSharedKeyFieldCode();
        txtHMedName.setTag(ourRow.getSharedKeyFieldCode());
        txtHMedName.setText(ourRow.getSharedKeyFieldName());
        reQuery();

        //Item Deails
        txtHMedName.setTag2nd(DmShared.xNull(ourRow.getPropertyList()));
        itemGSTNature = ourRow.getItemGSTNature();
        itsBasedOn = ourRow.getItsBasedOn();
        hsnCode = ourRow.getHsnCode();
        isBestBefore = ourRow.getIsBestBefore();
        masterMrp = DmShared.xNull(ourRow.getMasterMrp());
        masterSaleRate = DmShared.xNull(ourRow.getMasterSaleRate());
        masterPurchaseRate = DmShared.xNull(ourRow.getMasterPurchaseRate());
        masterSchemeOnQty = DmShared.xNull(ourRow.getMasterSchemeOnQty());
        masterSchemeFreeQty = DmShared.xNull(ourRow.getMasterSchemeFreeQty());
        masterPurchaseDiscountPer = DmShared.xNull(ourRow.getMasterPurchaseDiscountPer());
        isIncludeFreeQtyInSaleRate = ourRow.getIsIncludeFreeQtyInSaleRate();
        itemCategoryProffitMargine = ourRow.getSaleRateProffitMargine();

        //podMrp 
        podMrp = DmShared.xNull(ourRow.getPodMrp());

        //SKU
        skuCode = ourRow.getSkuCode();
        skuName = ourRow.getSkuName();
        skuAllowDecimalPlaces = ourRow.getSkuDecimalPlaces();

        //Purchase Unit
        txtPurchaseUnit.setTag(ourRow.getPurchaseUnitCode());
        txtPurchaseUnit.setText(ourRow.getPurchaseUnitDisplayName());
        txtPurchaseUnit.setTag2nd(ourRow.getPurchaseUnitDecimalPlaces());
        purchaseUnitName = ourRow.getPurchaseUnitName();
        purchaseUnitConversionRate = ourRow.getPurchaseUnitConversionRate();

        //Properties
        selectedBatchNoText = ourRow.getBatchNo();
        txtHBatchNo.setText(ourRow.getBatchNo());
        txtManufacturingDate.setText(ourRow.getManufacturingDate());
        txtExpDate.setText(ourRow.getExpiryDate());

        Date mfgDate = DmShared.convertDate(txtManufacturingDate.getText());
        Date expDate = DmShared.convertDate(txtExpDate.getText());
        if (mfgDate != null && expDate != null) {
            txtBestBefore.setText(String.valueOf(CalendarUtil.getDaysBetween(mfgDate, expDate)));
        }

        //Order Details
        selectedOrderNoCode = ourRow.getPoDocId();
        txtHOrderNo.setTag(ourRow.getPoDocId());
        txtHOrderNo.setText(ourRow.getPoRecId());
        txtHOrderNo.setTag2nd(ourRow.getPoV_SNo());
        txtPoRefNo.setText(ourRow.getPoRefNo());

        //Quantities
        txtChallanQty.setText(ourRow.getChallanQty());
        txtChallanQty.setTag(ourRow.getChallanQty());
        txtReceiveQty.setText(ourRow.getRecQty());
        txtReceiveQty.setTag(ourRow.getRecQty());
        txtRejectedQty.setText(ourRow.getRejQty());
        txtFreeQty.setText(ourRow.getFreeQty());
        txtSkuDifferenceQty.setText(ourRow.getSkuDifferenceQty());
        txtSKUQty.setText(ourRow.getSkuQty());
        txtAcceptQty.setText(ourRow.getAcceptQty());
        txtAcceptQty.setTag2nd(ourRow.getPendingQty());

        //Rate
        txtMRP.setText(ourRow.getMrp());
        skuMrp = ourRow.getSkuMrp();
        txtRate.setText(ourRow.getRate());
        txtSaleRate.setText(ourRow.getSaleRate());
        txtSaleRate.setTag(ourRow.getSaleRate());
        skuSaleRate = ourRow.getSkuSaleRate();
        txtStaffSaleRate.setText(ourRow.getStaffSaleRate());
        txtStaffSaleRate.setTag(ourRow.getStaffSaleRate());
        skuStaffSaleRate = ourRow.getSkuStaffSaleRate();
        skuRate = ourRow.getSkuRate();
        txtAmount.setText(ourRow.getAmount());

        //Other
        txtRemark.setText(ourRow.getRemark());
        txtBarcode.setText(DmShared.xNull(ourRow.getBarcode()));
        isBarcodeActive = DmShared.xNull(ourRow.getIsBarcodeActive());

        v_SNo = ourRow.getvSNo();
        hashCode = ourRow.getSharedKeyFieldHashCode();
//        overHeadLine.initialize(this.getCurrentRowIndex());

        lstImpurity.clear();
        lstImpurity.addAll(ourRow.getLstImpurity());

        lstItemUpdateFields.clear();
        lstItemUpdateFields.addAll(ourRow.getLstItemUpdateFields());

        manageDisplay();
        txtHOrderNo.setFocus(true);
    }

    @Override
    public void onNew() {
        proceedNew();
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdGrnDetail_Mdl();
    }

    @Override
    public void manageDelete() {
        callBack.onDelete();
    }

    @Override
    public void onDelete() {
        proceedDelete();
    }

    @Override
    public void onClose() {
        hide();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="textHelpEvents & gridEvents">
    private void textHelpEvents() {

        txtHMedName.setReadOnly(true);
        txtHMedName.addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_DELETE || event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE) {
                    txtHMedName.setTag("");
                    txtHMedName.setText("");
                    onMedSelection(true, new TxtHMedicineName_Mdl(), null, false, grnEntry);
                } else if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER || event.getNativeKeyCode() == KeyCodes.KEY_INSERT
                        || event.getNativeKeyCode() == KeyCodes.KEY_RIGHT || event.getNativeKeyCode() == KeyCodes.KEY_LEFT
                        || event.getNativeKeyCode() == KeyCodes.KEY_UP || event.getNativeKeyCode() == KeyCodes.KEY_DOWN
                        || event.getNativeKeyCode() == KeyCodes.KEY_ALT || event.getNativeKeyCode() == KeyCodes.KEY_CTRL
                        || event.getNativeKeyCode() == KeyCodes.KEY_SHIFT || event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE
                        || event.isAltKeyDown() || event.isControlKeyDown() || event.isShiftKeyDown()) {

                } else {
                    MedicineSearchFilter_Mdl filter = new MedicineSearchFilter_Mdl();
                    filter.setMedicineCode(DmShared.xNull(txtHMedName.getTag()));
                    filter.setDepartmentCode(DmShared.xNull(grnEntry.txtHDepartment.getTag()));
                    filter.setDepartmentName(DmShared.xNull(grnEntry.txtHDepartment.getText()));
                    filter.setV_Date(DmShared.dNull(grnEntry.txtVoucherDate.getText()));
                    medicineSearch.show(filter, String.valueOf((char) event.getNativeKeyCode()), grnEntry.txtHVoucherType.getTag());
                }
            }
        });

        medicineSearch.addCallBack(new MedicineSearch.CallBack() {
            @Override
            public void ok(MedicineSearch_Mdl medSearchMdl) {
                TxtHMedicineName_Mdl medMdl = new TxtHMedicineName_Mdl();
                medMdl.setSharedKeyFieldCode(medSearchMdl.getSharedKeyFieldCode());
                medMdl.setSharedKeyFieldName(medSearchMdl.getSharedKeyFieldName());
                medMdl.setManualCode(medSearchMdl.getManualCode());
                medMdl.setHsnCode(medSearchMdl.getHsnCode());
                medMdl.setItemGSTNature(medSearchMdl.getItemGSTNature());
                medMdl.setItsBasedOn(medSearchMdl.getItsBasedOn());
                medMdl.setIsBestBefore(medSearchMdl.getIsBestBefore());
                medMdl.setIsBarcodeActive(DmShared.xNull(medSearchMdl.getIsBarcodeActive()));
                medMdl.setMasterMrp(medSearchMdl.getMrp());
                medMdl.setMasterSaleRate(medSearchMdl.getSaleRate());
                medMdl.setMasterPurchaseRate(medSearchMdl.getPurchaseRate());
                medMdl.setMasterSchemeOnQty(medSearchMdl.getSchemeOnQty());
                medMdl.setMasterSchemeFreeQty(medSearchMdl.getSchemeFreeQty());
                medMdl.setMasterPurchaseDiscountPer(medSearchMdl.getPurchaseDiscountPer());
                medMdl.setIsIncludeFreeQtyInSaleRate(medSearchMdl.getIsIncludeFreeQtyInSaleRate());
                medMdl.setItemProperty(medSearchMdl.getPropertyList());
                medMdl.setSkuCode(medSearchMdl.getSkuCode());
                medMdl.setSkuName(medSearchMdl.getSkuName());
                medMdl.setSkuDecimalPlaces(medSearchMdl.getSkuDecimalPlaces());
                medMdl.setPurchaseUnitCode(medSearchMdl.getPurchaseUnitCode());
                medMdl.setPurchaseUnitName(medSearchMdl.getPurchaseUnitName());
                medMdl.setPurchaseUnitDisplayName(medSearchMdl.getPurchaseUnitName());
                medMdl.setSaleRateProffitMargine(medSearchMdl.getSaleRateProffitMargine());
                itemCategoryProffitMargine = medSearchMdl.getSaleRateProffitMargine();
                medMdl.setItemCategoryProffitMargine(medSearchMdl.getSaleRateProffitMargine());

                if (!DmShared.xNull(medSearchMdl.getSkuCode()).equalsIgnoreCase(DmShared.xNull(medSearchMdl.getPurchaseUnitCode()))) {
                    medMdl.setPurchaseUnitDisplayName(DmShared.xNull(medSearchMdl.getPurchaseUnitName()) + " = " + DmShared.xNull(medSearchMdl.getPurchaseUnitConversionRate()) + " " + DmShared.xNull(medSearchMdl.getSkuName()));
                }
                medMdl.setPurchaseUnitConversionRate(medSearchMdl.getPurchaseUnitConversionRate());
                medMdl.setPurchaseUnitDecimalPlaces(medSearchMdl.getPurchaseUnitDecimalPlaces());

                txtHMedName.setTag(DmShared.xNull(medMdl.getSharedKeyFieldCode()));
                txtHMedName.setText(DmShared.xNull(medMdl.getSharedKeyFieldName()));
                onMedSelection(true, medMdl, null, false, grnEntry);
                txtHMedName.setFocus(true);
            }

            @Override
            public void close() {
                txtHMedName.setFocus(true);
            }
        });

        txtHOrderNo.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHOrderNo_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHOrderNo(grnEntry.txtHSupplier.getTag(),
                        grnEntry.txtChallanDate.getText(),
                        grnEntry.topControl.getMode().equalsIgnoreCase("New") ? "" : grnEntry.topControl.getDocId(),
                        v_SNo,
                        grnEntry.txtHDepartment.getTag(),
                        helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHOrderNo_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHOrderNo).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHOrderNo_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHOrderNo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHOrderNo).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHOrderNo_Mdl t1Row) {
                if (!DmShared.xNull(selectedOrderNoCode).equalsIgnoreCase(DmShared.xNull(txtHOrderNo.getTag()))) {
                    resetOrder();
                    selectedOrderNoCode = DmShared.xNull(txtHOrderNo.getTag());
                }

                if (blnStatus && t1Row != null) {
                    txtHOrderNo.setTag2nd(DmShared.xNull(t1Row.getPoV_SNo()));
                    txtPoRefNo.setText(DmShared.xNull(t1Row.getPoRefNo()));
                    txtChallanQty.setText(DmShared.xNull(t1Row.getPendingQty()));
                    txtChallanQty.setTag(DmShared.xNull(t1Row.getPendingQty()));
                    txtReceiveQty.setText(DmShared.xNull(t1Row.getPendingQty()));
                    txtReceiveQty.setTag(DmShared.xNull(t1Row.getPendingQty()));
                    txtAcceptQty.setTag2nd(DmShared.xNull(t1Row.getPendingQty())); //Pending Qty.
                    txtAcceptQty.setTag(DmShared.xNull(t1Row.getPendingQty()));
                    txtAcceptQty.setText(DmShared.xNull(t1Row.getPendingQty()));
                    txtSKUQty.setText(DmShared.xNull(t1Row.getSkuQty()));
                    txtRate.setText(DmShared.xNull(t1Row.getRate()));
                    podMrp = DmShared.xNull(t1Row.getPodMrp());
                    txtAmount.setText(DmShared.xNull(t1Row.getAmount()));
                    if (t1Row.getTxtHMedName_Mdl() != null) {
                        txtHMedName.setTag(DmShared.xNull(t1Row.getTxtHMedName_Mdl().getSharedKeyFieldCode()));
                        txtHMedName.setText(DmShared.xNull(t1Row.getTxtHMedName_Mdl().getSharedKeyFieldName()));
                    }
                    onMedSelection(blnStatus, t1Row.getTxtHMedName_Mdl(), t1Row, false, grnEntry);
                }
            }
        });

        txtHBatchNo.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHBatchNo_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHBatchNo(txtHMedName.getTag(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHBatchNo_Mdl>>>() {

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
                if (!DmShared.xNull(selectedBatchNoText).equalsIgnoreCase(DmShared.xNull(txtHBatchNo.getText()))) {
                    resetBatchNo();
                    selectedBatchNoText = DmShared.xNull(txtHBatchNo.getText());
                }
                if (blnStatus && t1Row != null) {
                    txtMRP.setText(DmShared.xNull(t1Row.getMrp()));
                    txtBarcode.setText(DmShared.xNull(t1Row.getBarcode()));
                    txtExpDate.setText(DmShared.dNull(t1Row.getExpireDate()));
                    txtManufacturingDate.setText(DmShared.dNull(t1Row.getManufacturingDate()));
                    Date mfgDate = DmShared.convertDate(txtManufacturingDate.getText());
                    Date expDate = DmShared.convertDate(txtExpDate.getText());
                    if (mfgDate != null && expDate != null) {
                        txtBestBefore.setText(String.valueOf(CalendarUtil.getDaysBetween(mfgDate, expDate)));
                    }
                    calculate();
                }
            }
        });

        txtFreeQty.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                calculate();
            }
        });

        txtRemark.addKeyDownHandler(new DmRemarkTextBox.TxtRemarkKeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event, Object objSource) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
//                    ((CellSelectionModel) overHeadLine.getDmGrid().getGrid().getSelectionModel()).selectCell(STClient.getOHLFocusIndex(overHeadLine.getOverHeadList()), 0);
                }
            }
        });
    }

    public void onMedSelection(boolean blnStatus, TxtHMedicineName_Mdl medMdl, TxtHOrderNo_Mdl orderMdl, boolean calledBy, GRN grn) {
        if (!DmShared.xNull(selectedMedCode).equalsIgnoreCase(DmShared.xNull(txtHMedName.getTag()))) {
            resetItem();
            selectedMedCode = DmShared.xNull(txtHMedName.getTag());
        }
        if (blnStatus && medMdl != null) {
            if (!DmShared.xNull(selectedMedCode).equalsIgnoreCase(DmShared.xNull(txtHMedName.getTag()))) {
                resetItem();
                selectedMedCode = DmShared.xNull(txtHMedName.getTag());
            }
            txtHMedName.setText(DmShared.xNull(medMdl.getSharedKeyFieldName()));
            txtHMedName.setTag(DmShared.xNull(medMdl.getSharedKeyFieldCode()));
            txtHMedName.setTag2nd(medMdl.getItemProperty());

            hsnCode = DmShared.xNull(medMdl.getHsnCode());
            itemGSTNature = DmShared.xNull(medMdl.getItemGSTNature());
            itsBasedOn = DmShared.iNull(medMdl.getItsBasedOn()).toString();
            isBestBefore = DmShared.iNull(medMdl.getIsBestBefore()).toString();
            isBarcodeActive = DmShared.iNull(medMdl.getIsBarcodeActive()).toString();
            masterMrp = DmShared.xNull(medMdl.getMasterMrp());
            masterSaleRate = DmShared.xNull(medMdl.getMasterSaleRate());
            masterPurchaseRate = DmShared.xNull(medMdl.getMasterPurchaseRate());
            masterSchemeOnQty = DmShared.xNull(medMdl.getMasterSchemeOnQty());
            masterSchemeFreeQty = DmShared.xNull(medMdl.getMasterSchemeFreeQty());
            masterPurchaseDiscountPer = DmShared.xNull(medMdl.getMasterPurchaseDiscountPer());
            isIncludeFreeQtyInSaleRate = DmShared.iNull(medMdl.getIsIncludeFreeQtyInSaleRate()).toString();

            skuName = DmShared.xNull(medMdl.getSkuName());
            skuCode = DmShared.xNull(medMdl.getSkuCode());
            skuAllowDecimalPlaces = DmShared.xNull(medMdl.getSkuDecimalPlaces());

            txtPurchaseUnit.setText(DmShared.xNull(medMdl.getPurchaseUnitDisplayName()));
            txtPurchaseUnit.setTag(DmShared.xNull(medMdl.getPurchaseUnitCode()));
            txtPurchaseUnit.setTag2nd(DmShared.xNull(medMdl.getPurchaseUnitDecimalPlaces()));
            purchaseUnitName = DmShared.xNull(medMdl.getPurchaseUnitName());
            purchaseUnitConversionRate = DmShared.xNull(medMdl.getPurchaseUnitConversionRate());
            itemCategoryProffitMargine = DmShared.xNull(medMdl.getItemCategoryProffitMargine());
            this.grnEntry = grn;
//            overHeadLine = this.grnEntry.ohlHandler.getWidget();
//            if (orderMdl != null) {
//                grnEntry.ohlHandler.setReference(orderMdl.getOHLListPurchaseOrder(), overHeadLine.getOverHeadList(true));
//            }
            if (!calledBy) {
                setIts(txtHMedName.getTag(), txtRate.getText(), grnEntry.location, itsBasedOn, grnEntry.txtChallanDate.getText());
            } else {
                setIts(txtHMedName.getTag(), txtRate.getText(), orderMdl.getSharedCancelledBy(), itsBasedOn, orderMdl.getSharedModifyDate());
            }
            if (!DmShared.xNull(txtHMedName.getTag()).trim().equalsIgnoreCase("") && inventoryAccountingActive) {
                setStockPostingLedgers(txtHMedName.getTag());
            }
            calculate();
            reQuery();
            manageDisplay();
        }
    }

    private void gridEvents() {

        grdPrevGrn.addCallBack(new DmGridPaging.CallBack<String>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, String param) {

                srvAsync.invokeRPC(blnToEndOfFile).getPrevGrn(
                        txtHMedName.getTag(),
                        grnEntry.topControl.getMode().equalsIgnoreCase("New") ? "" : grnEntry.topControl.getDocId(),
                        v_SNo,
                        cbCurrentSupplier.getValue() || cbOtherSuppliers.getValue() ? grnEntry.txtHSupplier.getTag() : "",
                        cbCurrentSupplier.getValue(),
                        helper, blnToEndOfFile, param, new AsyncCallback<DmResultPageing<List<GrdGrnDetail_Mdl>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GrdGrnDetail_Mdl>> result) {
                        if (result.isStatus() && result.getData() != null) {
                            grdPrevGrn.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public String getParam() {
                return "";
            }
        });
    }

    private void reQuery() {
        grdPrevGrn.getHelper().setRowsStartFrom(0);
        grdPrevGrn.reQuery();
    }

    private void setQtyUnits() {
        if (!DmShared.xNull(txtPurchaseUnit.getTag()).equalsIgnoreCase("")
                && !DmShared.xNull(txtPurchaseUnit.getText()).equalsIgnoreCase("")
                && !DmShared.xNull(skuCode).equalsIgnoreCase("")
                && !DmShared.xNull(skuName).equalsIgnoreCase("")) {
            txtChallanQtyUnit.setText(purchaseUnitName);
            txtReceiveQtyUnit.setText(purchaseUnitName);
            txtRejectedQtyUnit.setText(purchaseUnitName);
            txtAcceptQtyUnit.setText(purchaseUnitName);
            txtRateUnit.setText(purchaseUnitName);
            txtMrpUnit.setText(purchaseUnitName);
            txtSaleRateUnit.setText(purchaseUnitName);
            txtStaffSaleRateUnit.setText(purchaseUnitName);
            txtFreeQtyUnit.setText(purchaseUnitName);
            txtSkuDifferenceQtyUnit.setText(skuName);
            txtSkuQtyUnit.setText(skuName);
        } else {
            txtChallanQtyUnit.setText("");
            txtReceiveQtyUnit.setText("");
            txtRejectedQtyUnit.setText("");
            txtAcceptQtyUnit.setText("");
            txtRateUnit.setText("");
            txtMrpUnit.setText("");
            txtSaleRateUnit.setText("");
            txtStaffSaleRateUnit.setText("");
            txtFreeQtyUnit.setText("");
            txtSkuDifferenceQtyUnit.setText("");
            txtSkuQtyUnit.setText("");
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Events">
    private void manageEvent() {
        txtChallanQty.addBlurHandler(this);
        txtReceiveQty.addBlurHandler(this);
        txtRejectedQty.addBlurHandler(this);
        txtAcceptQty.addBlurHandler(this);
        txtFreeQty.addBlurHandler(this);
        txtSkuDifferenceQty.addBlurHandler(this);
        txtSKUQty.addBlurHandler(this);
        txtRate.addBlurHandler(this);
        txtManufacturingDate.addBlurHandler(this);
        txtExpDate.addBlurHandler(this);
        txtMRP.addBlurHandler(this);
        txtSaleRate.addBlurHandler(this);
        txtStaffSaleRate.addBlurHandler(this);
        txtBestBefore.addBlurHandler(this);
        aRejectedQty.addClickHandler(this);
        txtRejectedQty.addKeyDownHandler(this);
        cbCurrentSupplier.addClickHandler(this);
        cbOtherSuppliers.addClickHandler(this);
        btnAddD365.addClickHandler(this);
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtReceiveQty) {
            calculate();
            onRecievedQtyBlur();
        } else if ((event.getSource() == txtRate)) {
            if (DmShared.iNull(itsBasedOn).intValue() == ItsBasedOn.Rate.toInt()) {
                setIts(txtHMedName.getTag(), txtRate.getText(), grnEntry.location, itsBasedOn, grnEntry.txtChallanDate.getText());
                if (inventoryAccountingActive) {
                    setStockPostingLedgers(txtHMedName.getTag());
                }
            } else {
                calculate();
            }
        } else if (event.getSource() == txtChallanQty) {
            calculate();
            if (!DmShared.vNull(txtChallanQty.getText()).equals(DmShared.vNull(txtChallanQty.getTag()))) {
                txtChallanQty.setTag(txtChallanQty.getText());
                txtReceiveQty.setText(txtChallanQty.getText());
                onRecievedQtyBlur();
            }
        } else if ((event.getSource() == txtManufacturingDate)) {
            txtManufacturingDate.setText(STClient.getDateFromMonthYear(txtManufacturingDate.getText(), false));
            setExpiryDate();
        } else if ((event.getSource() == txtBestBefore)) {
            setExpiryDate();
        } else if ((event.getSource() == txtExpDate)) {
            txtExpDate.setText(STClient.getDateFromMonthYear(txtExpDate.getText(), true));
        } else if (event.getSource() == txtRejectedQty
                || event.getSource() == txtAcceptQty
                || event.getSource() == txtFreeQty
                || event.getSource() == txtSkuDifferenceQty
                || event.getSource() == txtSKUQty
                || event.getSource() == txtMRP
                || event.getSource() == txtSaleRate
                || event.getSource() == txtStaffSaleRate) {
            calculate();
        }
    }

    private void onRecievedQtyBlur() {
        if (!DmShared.vNull(txtReceiveQty.getText()).equals(DmShared.vNull(txtReceiveQty.getTag()))) {
            txtReceiveQty.setTag(txtReceiveQty.getText());
            resetRecievedQty();
            manageDisplay();
        }
    }

    private void setExpiryDate() {
        if (!DmShared.xNull(txtBestBefore.getText()).equalsIgnoreCase("") && !DmShared.xNull(txtManufacturingDate.getText()).equalsIgnoreCase("")) {
            Date tempDate = DmShared.convertDate(DmShared.dNull(txtManufacturingDate.getText()));
            CalendarUtil.addDaysToDate(tempDate, DmShared.iNull(txtBestBefore.getText()).intValue());
            txtExpDate.setText(DmShared.dNull(tempDate));
        } else {
            txtExpDate.setText("");
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == aRejectedQty) {
            showImporityPopup();
        } else if (event.getSource() == cbCurrentSupplier) {
            if (cbCurrentSupplier.getValue()) {
                cbOtherSuppliers.setValue(false);
            }
            reQuery();
        } else if (event.getSource() == cbOtherSuppliers) {
            if (cbOtherSuppliers.getValue()) {
                cbCurrentSupplier.setValue(false);
            }
            reQuery();
        } else if (event.getSource() == btnAddD365) {
            getD365Data();
        } else {
            super.onClick(event);
        }
    }

    private void getD365Data() {
//        srvAsync.invokeRPC(true).getD65DataDeatails(new AsyncCallback<DmResult<List<GrdGrnDetail_Mdl>>>() {
//
//            @Override
//            public void onFailure(Throwable caught) {
//                new DmAlertMessageBox(caught.getMessage(), txtHMedName).show();
//            }
//
//            @Override
//            public void onSuccess(DmResult<List<GrdGrnDetail_Mdl>> result) {
//                if (result.isStatus()) {
////                    gridMedicineDetail.getGrid().getStore().addAll(result.getData());
//                } else {
//                    new DmAlertMessageBox(result.getErrorMessage(), txtHMedName).show();
//                }
//            }
//        });
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_SPACE) {
            if (event.getSource() == txtRejectedQty) {
                showImporityPopup();
            }
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            impurityPopup.hide();
            itemUpdatePopup.hide();
            medicineSearch.hide();
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Popups">
    private void popupCallBacks() {
        impurityPopup.addCallBack(new ImpurityPopup.CallBack() {
            @Override
            public void onOk(List<GrdImpurity_Mdl> lstImp) {
                double rejQty = 0;
                for (GrdImpurity_Mdl grdIm : lstImp) {
                    rejQty += DmShared.vNull(grdIm.getNetQty());
                }
                txtRejectedQty.setText(DmShared.vNull(rejQty).toString());
                lstImpurity.clear();
                lstImpurity.addAll(lstImp);
                txtRejectedQty.setFocus(true);
                calculate();
                manageDisplay();
            }

            @Override
            public void onClose() {
                txtRejectedQty.setFocus(true);
            }
        });
    }

    private void showImporityPopup() {
        impurityPopup.show(this);
    }

    private void showItemUpdatePopup() {
//        OverHeadLine_Mdl ohlMdl = OverHeadLine_Shared.getOHByUniqueId("DS", overHeadLine.getOverHeadList());
        String unitDecimalPlaces = DmShared.getNumberFormat(DmShared.iNull(txtPurchaseUnit.getTag2nd()).intValue());
        String[] tickValues;

        Map<ItemUpdateField_Mdl.Field, String[]> mapTickHistory = new HashMap<>();
        for (ItemUpdateField_Mdl iufMdl : lstItemUpdateFields) {
            if (DmShared.iNull(iufMdl.getSaveToItem()).intValue() == 1 || DmShared.iNull(iufMdl.getSaveToCompany()).intValue() == 1) {
                tickValues = new String[3];
                tickValues[0] = DmShared.iNull(iufMdl.getSaveToItem()).toString();
                tickValues[1] = DmShared.iNull(iufMdl.getSaveToCompany()).toString();
                tickValues[2] = DmShared.xNull(iufMdl.getFinalValue());
                mapTickHistory.put(iufMdl.getField(), tickValues);
            }
        }

        lstItemUpdateFields.clear();
        lstItemUpdateFields.add(new ItemUpdateField_Mdl("MRP", Field.MRP, masterMrp, txtMRP.getText(), "1", "0"));
        lstItemUpdateFields.add(new ItemUpdateField_Mdl("Sale Rate", Field.SALE_RATE, masterSaleRate, txtSaleRate.getText(), "1", "0"));
        lstItemUpdateFields.add(new ItemUpdateField_Mdl("Purchase Rate", Field.PURCHASE_RATE, masterPurchaseRate, txtRate.getText(), "1", "0"));
        lstItemUpdateFields.add(new ItemUpdateField_Mdl("Scheme", Field.SCHEME, DmClient.vNull(masterSchemeOnQty, unitDecimalPlaces) + " + " + DmClient.vNull(masterSchemeFreeQty, unitDecimalPlaces), DmClient.vNull(txtChallanQty.getText(), unitDecimalPlaces) + " + " + DmClient.vNull(txtFreeQty.getText(), unitDecimalPlaces), "0", "1"));
//        lstItemUpdateFields.add(new ItemUpdateField_Mdl("Purchase Discount %", Field.PURCHASE_DISCOUNT_PERCENTAGE, masterPurchaseDiscountPer, (ohlMdl != null) ? ohlMdl.getAtRate() : "0", "0", "1"));
//        lstItemUpdateFields.add(new ItemUpdateField_Mdl("Purchase Discount %", Field.PURCHASE_DISCOUNT_PERCENTAGE, masterPurchaseDiscountPer, (ohlMdl != null) ? ohlMdl.getAtRate() : "0", "1", "0"));

        for (ItemUpdateField_Mdl ifuMdl : lstItemUpdateFields) {
            if (mapTickHistory.containsKey(ifuMdl.getField())) {
                ifuMdl.setSaveToItem(DmShared.iNull(mapTickHistory.get(ifuMdl.getField())[0]).toString());
                ifuMdl.setSaveToCompany(DmShared.iNull(mapTickHistory.get(ifuMdl.getField())[1]).toString());
                ifuMdl.setFinalValue(DmShared.xNull(mapTickHistory.get(ifuMdl.getField())[2]));
            }
        }

        itemUpdatePopup.show(lstItemUpdateFields, unitDecimalPlaces, new ItemUpdatePopup.CallBack() {
            @Override
            public void ok(List<ItemUpdateField_Mdl> listIUFs) {
                lstItemUpdateFields.clear();
                lstItemUpdateFields.addAll(listIUFs);
                proceedOk();
            }

            @Override
            public void onClose() {
                txtRemark.setFocus(true);
            }
        });
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Callback">
    interface CallBack {

        public void onOk(GrdGrnDetail_Mdl opdService);

        public void onDelete();
    }
    private CallBack callBack;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Clear">
    private void resetPopup() {
        v_SNo = "";
        skuRate = "";
        skuMrp = "";
        skuSaleRate = "";

        txtRemark.setText("");
        txtRemark.setTag("");
        txtRemark.setTag2nd("");

        txtHOrderNo.setText("");
        txtHOrderNo.setTag("");
        txtHOrderNo.setTag2nd("");
        selectedOrderNoCode = "";

        resetOrder();
    }

    private void resetOrder() {
        txtPoRefNo.setText("");
        txtPoRefNo.setTag("");
        txtPoRefNo.setTag2nd("");

        txtHMedName.setText("");
        txtHMedName.setTag("");
        txtHMedName.setTag2nd("");
        selectedMedCode = "";
        resetItem();

        txtChallanQtyUnit.setText("");
        txtChallanQty.setText("");
        txtChallanQty.setTag("");
        txtChallanQty.setTag2nd("");

        txtReceiveQtyUnit.setText("");
        txtReceiveQty.setText("");
        txtReceiveQty.setTag("");
        txtReceiveQty.setTag2nd("");
        resetRecievedQty();

        txtFreeQtyUnit.setText("");
        txtFreeQty.setText("");
        txtFreeQty.setTag("");
        txtFreeQty.setTag2nd("");

        txtSkuDifferenceQtyUnit.setText("");
        txtSkuDifferenceQty.setText("");
        txtSkuDifferenceQty.setTag("");
        txtSkuDifferenceQty.setTag2nd("");

        txtSkuQtyUnit.setText("");
        txtSKUQty.setText("");
        txtSKUQty.setTag("");
        txtSKUQty.setTag2nd("");

        txtAcceptQtyUnit.setText("");
        txtAcceptQty.setText("");
        txtAcceptQty.setTag("");
        txtAcceptQty.setTag2nd("");

        txtRate.setText("");
        txtRate.setTag("");
        txtRate.setTag2nd("");

        txtAmount.setText("");
        txtAmount.setTag("");
        txtAmount.setTag2nd("");

    }

    private void resetItem() {
        ibCode = "";
        hsnCode = "";
        itemGSTNature = "";
        itsBasedOn = "";
        isBestBefore = "";
        isBarcodeActive = "";
        masterMrp = "";
        masterSaleRate = "";
        masterPurchaseRate = "";
        masterSchemeOnQty = "";
        masterSchemeFreeQty = "";
        masterPurchaseDiscountPer = "";
        isIncludeFreeQtyInSaleRate = "";
        lstItemUpdateFields.clear();

        skuCode = "";
        skuName = "";
        skuAllowDecimalPlaces = "";

        txtPurchaseUnit.setText("");
        txtPurchaseUnit.setTag("");
        txtPurchaseUnit.setTag2nd("");
        purchaseUnitName = "";
        purchaseUnitConversionRate = "";

        grdPrevGrn.getDmGrid().getGrid().getStore().clear();
        cbCurrentSupplier.setValue(false);
        cbOtherSuppliers.setValue(false);

        txtHBatchNo.setText("");
        txtHBatchNo.setTag("");
        txtHBatchNo.setTag2nd("");
        selectedBatchNoText = "";
        resetBatchNo();
    }

    private void resetRecievedQty() {
        txtRejectedQtyUnit.setText("");
        txtRejectedQty.setText("");
        txtRejectedQty.setTag("");
        txtRejectedQty.setTag2nd("");
        lstImpurity.clear();
    }

    private void resetBatchNo() {
        if (!poMrpExist) {
            txtMRP.setText("");
            txtMRP.setTag("");
            txtMRP.setTag2nd("");
        }

        txtSaleRate.setText("");
        txtSaleRate.setTag("");
        txtSaleRate.setTag2nd("");

        txtManufacturingDate.setText("");
        txtManufacturingDate.setTag("");
        txtManufacturingDate.setTag2nd("");

        txtBestBefore.setText("");
        txtBestBefore.setTag("");
        txtBestBefore.setTag2nd("");

        txtExpDate.setText("");
        txtExpDate.setTag("");
        txtExpDate.setTag2nd("");

        txtBarcode.setText("");
        txtBarcode.setTag("");
        txtBarcode.setTag2nd("");
    }
    //</editor-fold>

    private void setStockPostingLedgers(String itemCode) {
//        srvAsync.invokeRPC(true).getStockPostingLedgers(itemCode, new AsyncCallback<DmResult<List<OverHeadITS_Mdl>>>() {
//
//            @Override
//            public void onFailure(Throwable caught) {
//                new DmAlertMessageBox(caught.getMessage(), txtHMedName).show();
//            }
//
//            @Override
//            public void onSuccess(DmResult<List<OverHeadITS_Mdl>> result) {
//                if (result.isStatus()) {
//                    overHeadLine.setITS(result.getData());
//                    if (!DmShared.xNull(result.getValue1()).trim().equals("") && !DmShared.xNull(result.getValue2()).trim().equals("")) {
//                        // Setting account to one GS + multiple [GS] unique ids
//                        List<OverHeadLine_Mdl> lstOHLMdl = OverHeadLine_Shared.getOHByUniqueIds("IV", overHeadLine.getOverHeadList(true));
//                        for (OverHeadLine_Mdl ohlMdl : lstOHLMdl) {
//                            if (ohlMdl != null) {
//                                ohlMdl.setAccountCode(result.getValue1());
//                                ohlMdl.setAccountName(result.getValue2());
//                            }
//                        }
//                    }
//                    calculate();
//                } else {
//                    new DmAlertMessageBox(result.getErrorMessage(), txtHMedName).show();
//                }
//            }
//        });
    }
}
