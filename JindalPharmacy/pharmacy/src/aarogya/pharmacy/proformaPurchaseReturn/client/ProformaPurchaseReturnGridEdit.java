package aarogya.pharmacy.proformaPurchaseReturn.client;

import aarogya.pharmacy.proformaPurchaseReturn.shared.*;
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
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.overHeadLine.client.OverHeadLine;
import dataman.dmToolkit.overHeadLine.shared.OverHeadITS_Mdl;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Mdl;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Shared;
import java.util.ArrayList;
import java.util.Date;

public class ProformaPurchaseReturnGridEdit<T1, T2> extends ProformaPurchaseReturnGridEdit_Dsg<T1, T2> implements BlurHandler {

    private final ServiceInvoker<ProformaPurchaseReturn_SrvAsync> srvAsync;
    private int hashCode = 0;
    private OverHeadLine overHeadLine;
    private ProformaPurchaseReturn proReturnEntry;

    String selectedItemCode = "", selectedBatchNoText = "", itemGSTNature = "", itsBasedOn = "", hsnCode = "", isBestBefore = "", purchaseUnitName = "",
            purchaseUnitConversionRate = "", gstUnitCode = "", skuCode = "", skuName = "", skuAllowDecimalPlaces = "",
            skuMrp = "", skuSaleRate = "", skuRate = "",
            pbDocId = "", pbRecId = "", pbV_Type = "", pbV_Date = "", pbV_SNo = "", pbNetAmount = "",
            v_SNo = "", ibCode = "", isIncludeFreeQtyInSaleRate = "";
    boolean isFirstShow = true;
    String pBillLandedSkuRate = "", selectedBillTag;

    public ProformaPurchaseReturnGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Medicine Detail", "760px", "370px");
        srvAsync = new ServiceInvoker<ProformaPurchaseReturn_SrvAsync>(GWT.create(ProformaPurchaseReturn_Srv.class), ProformaPurchaseReturn.class.getName());
        textHelpEvents();
        manageEvent();

    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, ProformaPurchaseReturn proReturnEntry) {
        resetPopup();
        this.proReturnEntry = proReturnEntry;
        overHeadLine = this.proReturnEntry.ohlHandler.getWidget();
        super.showCenter(currentRowIndex, okNeeded, newNeeded, deleteNeeded);

//        if (DmShared.iNull(proReturnEntry.txtHBillNo.getTag()).intValue() != 0) {  // <MJ>
//            txtRate.setEnabled(false);
//            txtHBatchNo.setEnabled(false);
//        } else {
//            txtRate.setEnabled(true);
//            txtHBatchNo.setEnabled(true);
//        }
        flwOHL.clear();
        overHeadLine.setBannerVisible(false);
        flwOHL.add(overHeadLine.asWidget());
        if (isFirstShow) {
            isFirstShow = false;
            vl.add(overHeadLine);
        }
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                if (txtHMedicineName.isEnabled()) {
                    txtHMedicineName.setFocus(true);
                } else {
                    txtQuantity.setFocus(true);
                }
            }
        });
    }

    private void setIts(final String itemCode, final String itemRate, final String location, final String itsBasedOn, final String billDate) {
        srvAsync.invokeRPC(true).getITS(itemCode, itemRate, location, itsBasedOn, billDate, new AsyncCallback<DmResult<List<OverHeadITS_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHMedicineName).show();
            }

            @Override
            public void onSuccess(DmResult<List<OverHeadITS_Mdl>> result) {
                if (result.isStatus()) {
                    overHeadLine.setITS(result.getData());
                    if (!DmShared.xNull(result.getValue1()).trim().equals("") && !DmShared.xNull(result.getValue2()).trim().equals("")) {
                        // Setting account to one GS + multiple [GS] unique ids
                        List<OverHeadLine_Mdl> lstOHLMdl = OverHeadLine_Shared.getOHByUniqueIds("GS", overHeadLine.getOverHeadList(true));
                        for (OverHeadLine_Mdl ohlMdl : lstOHLMdl) {
                            if (ohlMdl != null) {
                                ohlMdl.setAccountCode(result.getValue1());
                                ohlMdl.setAccountName(result.getValue2());
                            }
                        }
                    }
                    calculate();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtHMedicineName).show();
                }
            }
        });
    }

    private void manageDisplay() {
        txtHMedicineName.setEnabled(DmShared.xNull(txtHBillNo.getTag()).trim().equals(""));
        txtHMedicineName.setFocus(txtHMedicineName.isEnabled());
//        txtHMedicineName.setEnabled(true);
        txtPurchaseUnit.setEnabled(false);
        txtMedicineCompany.setEnabled(false);
        txtGrnNo.setEnabled(false);
        txtOrderNo.setEnabled(false);
        txtOrderRefNo.setEnabled(false);
        txtAmount.setEnabled(false);
        txtSkuQty.setEnabled(false);
        txtHBatchNo.setEnabled(txtHMedicineName.isEnabled());
        txtMRP.setEnabled(txtHMedicineName.isEnabled());
        txtSaleRate.setEnabled(txtHMedicineName.isEnabled());
        txtSaleRate.setEnabled(false);
        if (DmShared.iNull(isBestBefore).intValue() == 1) {
            txtManufacturingDate.setEnabled(txtHMedicineName.isEnabled());
            txtBestBefore.setEnabled(txtHMedicineName.isEnabled());
            txtExpiryDate.setEnabled(false);
        } else {
            txtManufacturingDate.setEnabled(false);
            txtBestBefore.setEnabled(false);
            txtExpiryDate.setEnabled(txtHMedicineName.isEnabled());
        }

        txtBillDate.setEnabled(true);
//        txtSupplierBillNoNew.setEnabled(true);
        txtSupplierBillDate.setEnabled(true);

    }

    private void calculate() {
        txtQuantity.setText(DmClient.vNull(txtQuantity.getText(), DmShared.getNumberFormat(DmShared.iNull(txtPurchaseUnit.getTag2nd()).intValue())));
        OverHeadLine_Mdl ohlMdl = OverHeadLine_Shared.getOHByUniqueId("IV", overHeadLine.getOverHeadList());
        String unitDecimalFormat = DmShared.getNumberFormat(DmShared.iNull(txtPurchaseUnit.getTag2nd()).intValue());
        String skuDecimalFormat = DmShared.getNumberFormat(DmShared.iNull(skuAllowDecimalPlaces).intValue());

        if (DmShared.vNull(discount) > 0 || DmShared.vNull(discountAt) > 0) {
            OverHeadLine_Shared.getOHByUniqueId("DS", overHeadLine.getOverHeadList(true)).setAmount(DmClient.vNull(discount, "0.00"));
            OverHeadLine_Shared.getOHByUniqueId("DS", overHeadLine.getOverHeadList(true)).setAtRate(DmClient.vNull(discountAt, "0.00"));
        }

        double purchaseUnitConvRate = DmShared.vNull(this.purchaseUnitConversionRate);
        double quantity = DmShared.vNull(txtQuantity.getText());
        double freeQty = DmShared.vNull(txtFreeQty.getText());
        double rate = DmShared.vNull(txtRate.getText());
        double mrp = DmShared.vNull(txtMRP.getText());
//        double amount = quantity * rate;
        double amount = (quantity + freeQty) * rate;

        txtSkuQty.setText(DmClient.vNull(((quantity + freeQty) * purchaseUnitConvRate), skuDecimalFormat));

        txtAmount.setText(DmClient.vNull(amount, "0.00"));
        skuRate = DmClient.vNull(DmShared.vNull(txtAmount.getText()) / DmShared.vNull(txtSkuQty.getText()), "0.0000000");

        final double saleRate = STClient.getSaleRate(mrp, rate, purchaseUnitConvRate, 0, "0", proReturnEntry.defaultValue.getLstSaleRateRule(), "", "", "");
        if (DmShared.vNull(txtSaleRate.getText()) == 0) {
            txtSaleRate.setText(DmClient.vNull(saleRate, "0.00"));
            txtSaleRate.setTag(txtSaleRate.getText());
        } else {
            txtSaleRate.setTag(DmClient.vNull(saleRate, "0.00"));
        }

        skuMrp = DmClient.vNull(mrp / purchaseUnitConvRate, "0.0000000");
        skuSaleRate = DmClient.vNull(DmShared.vNull(txtSaleRate.getText()) / purchaseUnitConvRate, "0.0000000");

        overHeadLine.getDmGrid().commitChanges();
        overHeadLine.calculate(DmClient.vNull(txtAmount.getText(), "0.00"));

        //<editor-fold defaultstate="collapsed" desc="Format decimal places of all quantities">
        txtQuantity.setText(DmClient.vNull(txtQuantity.getText(), unitDecimalFormat));
        txtFreeQty.setText(DmClient.vNull(txtFreeQty.getText(), unitDecimalFormat));
        txtSkuQty.setText(DmClient.vNull(txtSkuQty.getText(), skuDecimalFormat));
        //</editor-fold>
    }

    //<editor-fold defaultstate="collapsed" desc="GridEdit Overrides">
    @Override
    public void onOk() {
        try {
            calculate();
            if (DmShared.xNull(txtHMedicineName.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Medicine Name is mandatory.", txtHMedicineName).show();
                return;
            }
            if (DmShared.iNull(txtPurchaseUnit.getTag()).longValue() == 0) {
                new DmAlertMessageBox("Unit is mandatory.", txtPurchaseUnit).show();
                return;
            }
            if (DmShared.vNull(txtQuantity.getText()) == 0 && DmShared.vNull(txtFreeQty.getText()) == 0) {
                new DmAlertMessageBox("At least one is mandatory from Quantity and Free Qty.", txtQuantity).show();
                return;
            }
            if (DmShared.vNull(txtQuantity.getTag2nd()) > 0) {
                if (DmShared.vNull(txtQuantity.getText()) > DmShared.vNull(txtQuantity.getTag2nd())) {
                    new DmAlertMessageBox("Quantity should be less than or equal to : " + DmShared.vNull(txtQuantity.getTag2nd()), txtQuantity).show();
                    return;
                }
            }
            if (DmShared.vNull(txtFreeQty.getTag2nd()) > 0) {
                if (DmShared.vNull(txtFreeQty.getText()) > DmShared.vNull(txtFreeQty.getTag2nd())) {
                    new DmAlertMessageBox("Free Qty. should be less than or equal to : " + DmShared.vNull(txtFreeQty.getTag2nd()), txtFreeQty).show();
                    return;
                }
            }
            if (DmShared.vNull(txtSkuQty.getText()) <= 0) {
                new DmAlertMessageBox("SKU Qty. is mandatory.", txtQuantity).show();
                return;
            }
            if (DmShared.vNull(txtQuantity.getText()) != 0 && DmShared.vNull(txtRate.getText()) == 0) {
                new DmAlertMessageBox("Rate is mandatory.", txtRate).show();
                return;
            }
            if (DmShared.vNull(txtQuantity.getText()) != 0 && DmShared.vNull(txtAmount.getText()) == 0) {
                new DmAlertMessageBox("Amount is mandatory.", txtRate).show();
                return;
            }
            if (DmShared.xNull(txtHBatchNo.getText()).trim().equals("")) {
                new DmAlertMessageBox("Batch No. is mandatory.", txtHBatchNo).show();
                return;
            }
            if (!txtHMedicineName.isEnabled() && DmShared.xNull(ibCode).trim().equals("")) {
                new DmAlertMessageBox("IbCode is mandatory.", txtHBatchNo).show();
                return;
            }
            if (DmShared.vNull(txtMRP.getText()) == 0) {
                new DmAlertMessageBox("MRP is mandatory.", txtMRP).show();
                return;
            }
            if (DmShared.vNull(txtMRP.getText()) < DmShared.vNull(txtRate.getText())) {
                new DmAlertMessageBox("MRP should be greater than or equal to rate.", txtMRP).show();
                return;
            }
            if (DmShared.vNull(txtSaleRate.getText()) == 0) {
                new DmAlertMessageBox("Sale Rate is mandatory.", txtSaleRate).show();
                return;
            }
            if (DmShared.vNull(txtSaleRate.getText()) > DmShared.vNull(txtMRP.getText())) {
                new DmAlertMessageBox("Sale Rate should be less than or equal to MRP.", txtSaleRate).show();
                return;
            }
            if (DmShared.iNull(isIncludeFreeQtyInSaleRate).intValue() == 0) {
                if (DmShared.vNull(txtSaleRate.getText()) < DmShared.vNull(txtRate.getText())) {
                    new DmAlertMessageBox("Sale rate should not be less than Purchase rate.", txtRate).show();
                    return;
                }
            }
            if (DmShared.dNull(txtExpiryDate.getText()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Expiry Date is mandatory.", txtExpiryDate).show();
                return;
            }

            //===================================<MJ>===============================//
            if (DmShared.xNull(txtSupplierBillNoNew.getText()).trim().equalsIgnoreCase("")) {
                new DmAlertMessageBox("Please fill Ref. No.", txtSupplierBillNoNew).show();
                return;
            }
            if (DmShared.dNull(txtSupplierBillDate.getText()).trim().equalsIgnoreCase("")) {
                new DmAlertMessageBox("Please fill Ref. Date.", txtSupplierBillDate).show();
                return;
            }

            if ((DmShared.convertDate(proReturnEntry.txtVoucherDate.getText()).getTime() < (DmShared.convertDate(txtSupplierBillDate.getText()).getTime()))) {
                new DmAlertMessageBox("Voucher Date should be greater than or equal to Ref. Date.", proReturnEntry.txtVoucherDate).show();
                return;
            }
            //=================================<******>===============================//

            /*==============================<< Check Item dublicacy with Batch No and Order No. >>====================================*/
            for (GrdItemDetail_Mdl detail_Mdl : (List<GrdItemDetail_Mdl>) getDmGrid().getGrid().getStore().getAll()) {

                if (DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(txtHMedicineName.getTag()))
                        && DmShared.xNull(detail_Mdl.getBatchNo()).equalsIgnoreCase(DmShared.xNull(txtHBatchNo.getText()))
                        && DmShared.vNull(detail_Mdl.getMrp()).doubleValue() == DmShared.vNull(txtMRP.getText()).doubleValue()
                        && DmShared.dNull(detail_Mdl.getManufacturingDate()).equalsIgnoreCase(DmShared.dNull(txtManufacturingDate.getText()))
                        && DmShared.dNull(detail_Mdl.getExpiryDate()).equalsIgnoreCase(DmShared.dNull(txtExpiryDate.getText()))
                        && DmShared.xNull(detail_Mdl.getPurchaseUnitCode()).equalsIgnoreCase(DmShared.xNull(txtPurchaseUnit.getTag())) 
                        && DmShared.xNull(detail_Mdl.getPoDocId()).equalsIgnoreCase(DmShared.xNull(txtOrderNo.getTag()))
                        && DmShared.xNull(detail_Mdl.getPoV_SNo()).equalsIgnoreCase(DmShared.xNull(txtOrderNo.getTag2nd())) ){
                    if (!DmClient.vNull(detail_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                        String message = "<div style='float:right; margin-right: 5px;'>";
                        message += "Record already exist with following details - <br>";
                        message += (DmShared.xNull(txtOrderNo.getText()).trim().equals("")) ? "" : "<b>Order No.:</b> " + DmShared.xNull(txtOrderNo.getText()) + "<br>";
                        message += (DmShared.xNull(txtHMedicineName.getText()).trim().equals("")) ? "" : "<b>Medicine:</b> " + DmShared.xNull(txtHMedicineName.getText()) + "<br>";
                        message += (DmShared.xNull(txtPurchaseUnit.getText()).trim().equals("")) ? "" : "<b>Unit:</b> " + DmShared.xNull(txtPurchaseUnit.getText()) + "<br>";
                        message += (DmShared.xNull(txtHBatchNo.getText()).trim().equals("")) ? "" : "<b>Batch No.:</b> " + DmShared.xNull(txtHBatchNo.getText()) + "<br>";
                        message += (DmShared.vNull(txtMRP.getText()) == 0) ? "" : "<b>MRP:</b> " + DmClient.vNull(txtMRP.getText(), "0.00") + "<br>";
                        message += (DmShared.dNull(txtManufacturingDate.getText()).trim().equals("")) ? "" : "<b>Manufacturing Date:</b> " + DmShared.dNull(txtManufacturingDate.getText()) + "<br>";
                        message += (DmShared.dNull(txtExpiryDate.getText()).trim().equals("")) ? "" : "<b>Expiry Date:</b> " + DmShared.dNull(txtExpiryDate.getText()) + "<br>";
                        message += "</div>";
                        new DmAlertMessageBox(message, txtHMedicineName).show();
                        return;
                    }
                }

                //=======================================================Check Item Company======================================================
                if (!DmShared.xNull(detail_Mdl.getItemCompany()).equalsIgnoreCase("") && !DmShared.xNull(detail_Mdl.getItemCompany()).equalsIgnoreCase(DmShared.xNull(txtMedicineCompany.getText()))) {

                    new DmAlertMessageBox("Only " + DmShared.xNull(detail_Mdl.getItemCompany()) + " Company Item can be select.", txtHMedicineName).show();
                    return;
                }

            }

            /*==============================<< Check overheadLine conditions >>====================================*/
            OverHeadLine_Shared.chkGridSingleOH(overHeadLine.getOverHeadList());

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
                        proceedOk();
                        txtSaleRate.setFocus(true);
                    }
                }).show();
            } else {
                proceedOk();
            }
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtHMedicineName).show();
        }
    }

    @Override
    public void manageOk(T1 row) {
        GrdItemDetail_Mdl ourRow = (GrdItemDetail_Mdl) row;
        calculate();

        //Item
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHMedicineName.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHMedicineName.getText()));

        ourRow.setItemCompany(DmShared.xNull(txtMedicineCompany.getText()));

        //Item Details
        ourRow.setItemGSTNature(DmShared.xNull(itemGSTNature));
        ourRow.setItsBasedOn(DmShared.xNull(itsBasedOn));
        ourRow.setHsnCode(DmShared.xNull(hsnCode));
        ourRow.setIsBestBefore(DmShared.xNull(isBestBefore));

        //Bill Detail
        ourRow.setPbDocId(DmShared.xNull(pbDocId));
        ourRow.setPbRecId(DmShared.xNull(pbRecId));
        ourRow.setPbV_Type(DmShared.xNull(pbV_Type));
        ourRow.setPbV_Date(DmShared.xNull(pbV_Date));
        ourRow.setPbV_SNo(DmShared.xNull(pbV_SNo));
        ourRow.setPbNetAmount(DmShared.xNull(pbNetAmount));

        ourRow.setPbRefNo(txtSupplierBillNoNew.getText());
        ourRow.setPbRefDate(txtSupplierBillDate.getText());

        //Challan Details
        ourRow.setPcDocId(DmShared.xNull(txtGrnNo.getTag()));
        ourRow.setPcRecId(DmShared.xNull(txtGrnNo.getText()));
        ourRow.setPcV_SNo(DmShared.xNull(txtGrnNo.getTag2nd()));

        //Order Details
        ourRow.setPoDocId(DmShared.xNull(txtOrderNo.getTag()));
        ourRow.setPoRecId(DmShared.xNull(txtOrderNo.getText()));
        ourRow.setPoV_SNo(DmShared.xNull(txtOrderNo.getTag2nd()));
        ourRow.setPoRefNo(DmShared.xNull(txtOrderRefNo.getText()));

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
        ourRow.setGstUnitCode(DmShared.xNull(gstUnitCode));

        //isIncludedFree qty
        //Quantities
        ourRow.setQuantity(DmShared.xNull(txtQuantity.getText()));
        ourRow.setFreeQty(DmShared.xNull(txtFreeQty.getText()));
        ourRow.setPendingQty(DmShared.xNull(txtQuantity.getTag2nd()));
        ourRow.setPendingFreeQty(DmShared.xNull(txtFreeQty.getTag2nd()));
        ourRow.setSkuQty(DmShared.xNull(txtSkuQty.getText()));

        //Rate
        ourRow.setMrp(DmShared.xNull(txtMRP.getText()));
        ourRow.setSkuMrp(DmShared.xNull(skuMrp));
        ourRow.setRate(DmShared.xNull(txtRate.getText()));
        ourRow.setAmount(DmShared.xNull(txtAmount.getText()));
        ourRow.setSaleRate(DmShared.xNull(txtSaleRate.getText()));
        ourRow.setSkuSaleRate(DmShared.xNull(skuSaleRate));
        ourRow.setSkuRate(DmShared.xNull(skuRate));

        //Properties
        ourRow.setBatchNo(DmShared.xNull(txtHBatchNo.getText()));
        ourRow.setIbCode(DmShared.xNull(txtHBatchNo.getTag()));
        ibCode = DmShared.xNull(txtHBatchNo.getTag());
        ourRow.setManufacturingDate(DmShared.dNull(txtManufacturingDate.getText()));
        ourRow.setBestBeforeDays(DmShared.xNull(txtBestBefore.getText()));
        ourRow.setExpiryDate(DmShared.dNull(txtExpiryDate.getText()));

        //Other
        ourRow.setRemark(DmShared.xNull(txtRemark.getText()).trim());
        ourRow.setV_SNo(DmShared.xNull(v_SNo));
//        ourRow.setIbCode(DmShared.xNull(ibCode));
        ourRow.setOHLList(overHeadLine.getOverHeadList());

        ourRow.setpBillLandedSkuRate(DmClient.vNull(DmShared.xNull(pBillLandedSkuRate), "0.00"));

        callBack.onOk(ourRow);
    }

    @Override
    public void displayRow(T1 row) {
        GrdItemDetail_Mdl ourRow = (GrdItemDetail_Mdl) row;
        displayItemDetail(ourRow);

    }

    //<editor-fold defaultstate="collapsed" desc="comment">
//        pBillLandedSkuRate = ourRow.getpBillLandedSkuRate();
//
//        //Clear
//        resetPopup();
//
//        txtHBillNo.setTag(DmShared.xNull(ourRow.getPbDocId()));
//        txtHBillNo.setTag2nd(DmShared.xNull(ourRow.getPbV_SNo()));
//        txtHBillNo.setText(DmShared.xNull(ourRow.getPbRecId()));
//
//        txtBillDate.setText(DmShared.dNull(ourRow.getPbV_Date()));
//
//        txtSupplierBillNo.setTag(DmShared.xNull(ourRow.getPbRefNo()));
//        txtSupplierBillNo.setText(DmShared.xNull(ourRow.getPbRefNo()));
//
//        txtSupplierBillDate.setTag(DmShared.dNull(ourRow.getPbRefDate()));
//        txtSupplierBillDate.setText(DmShared.dNull(ourRow.getPbRefDate()));
//
//        //Item
//        selectedItemCode = ourRow.getSharedKeyFieldCode();
//        txtHMedicineName.setTag(ourRow.getSharedKeyFieldCode());
//        txtHMedicineName.setText(ourRow.getSharedKeyFieldName());
//
//        //Item Details
//        itemGSTNature = ourRow.getItemGSTNature();
//        itsBasedOn = ourRow.getItsBasedOn();
//        hsnCode = ourRow.getHsnCode();
//        isBestBefore = ourRow.getIsBestBefore();
//
//        //Bill Details
//        pbDocId = ourRow.getPbDocId();
//        pbRecId = ourRow.getPbRecId();
//        pbV_Type = ourRow.getPbV_Type();
//        pbV_Date = ourRow.getPbV_Date();
//        pbV_SNo = ourRow.getPbV_SNo();
//        pbNetAmount = ourRow.getPbNetAmount();
//
//        //Challan Details
//        txtGrnNo.setTag(ourRow.getPcDocId());
//        txtGrnNo.setText(ourRow.getPcRecId());
//        txtGrnNo.setTag2nd(ourRow.getPcV_SNo());
//
//        //Order Details
//        txtOrderNo.setTag(ourRow.getPoDocId());
//        txtOrderNo.setText(ourRow.getPoRecId());
//        txtOrderNo.setTag2nd(ourRow.getPoV_SNo());
//        txtOrderRefNo.setText(ourRow.getPoRefNo());
//
//        //SKU
//        skuCode = ourRow.getSkuCode();
//        skuName = ourRow.getSkuName();
//        skuAllowDecimalPlaces = ourRow.getSkuDecimalPlaces();
//
//        //Purchase Unit
//        txtPurchaseUnit.setTag(ourRow.getPurchaseUnitCode());
//        txtPurchaseUnit.setText(ourRow.getPurchaseUnitDisplayName());
//        txtPurchaseUnit.setTag2nd(ourRow.getPurchaseUnitDecimalPlaces());
//        purchaseUnitName = ourRow.getPurchaseUnitName();
//        purchaseUnitConversionRate = ourRow.getPurchaseUnitConversionRate();
//        gstUnitCode = ourRow.getGstUnitCode();
//
//        //Quantities
//        txtQuantity.setText(ourRow.getQuantity());
//        txtQuantity.setTag2nd(ourRow.getPendingQty());
//        txtFreeQty.setText(DmShared.xNull(ourRow.getFreeQty()));
//        txtFreeQty.setTag2nd(DmShared.xNull(ourRow.getPendingFreeQty()));
//        txtSkuQty.setText(DmShared.xNull(ourRow.getSkuQty()));
//
//        //Rate
//        txtMRP.setText(ourRow.getMrp());
//        skuMrp = ourRow.getSkuMrp();
//        txtSaleRate.setText(ourRow.getSaleRate());
//        txtSaleRate.setTag(ourRow.getSaleRate());
//        skuSaleRate = ourRow.getSkuSaleRate();
//        txtRate.setText(ourRow.getRate());
//        txtAmount.setText(ourRow.getAmount());
//        skuRate = ourRow.getSkuRate();
//
//        //Properties
//        selectedBatchNoText = ourRow.getBatchNo();
//        txtHBatchNo.setText(ourRow.getBatchNo());
//        txtHBatchNo.setTag(ourRow.getIbCode());
//        txtExpiryDate.setText(ourRow.getExpiryDate());
//        txtManufacturingDate.setText(ourRow.getManufacturingDate());
//
//        Date mfgDate = DmShared.convertDate(txtManufacturingDate.getText());
//        Date expDate = DmShared.convertDate(txtExpiryDate.getText());
//        if (mfgDate != null && expDate != null) {
//            txtBestBefore.setText(String.valueOf(CalendarUtil.getDaysBetween(mfgDate, expDate)));
//        }
//
//        //Other
//        txtRemark.setText(ourRow.getRemark());
//        v_SNo = ourRow.getV_SNo();
//        ibCode = ourRow.getIbCode();
//        hashCode = ourRow.getSharedKeyFieldHashCode();
//        overHeadLine.initialize(this.getCurrentRowIndex());
//
//        manageDisplay();
//        txtHMedicineName.setFocus(true);
//</editor-fold>
    @Override
    public void onNew() {
        proceedNew();
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdItemDetail_Mdl();
    }

    @Override
    public void onDelete() {
        proceedDelete();
    }

    @Override
    public void manageDelete() {
        callBack.onDelete();
    }

    @Override
    public void onClose() {
        hide();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="textHelpEvents">
    private void textHelpEvents() {
        txtHBillNo.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<GrdBillDetail_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHBillNo(proReturnEntry.txtHSupplier.getTag(), proReturnEntry.voucherCategory, proReturnEntry.txtVoucherDate.getText(), txtHBillNo.getTag2nd(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<GrdBillDetail_Mdl>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHBillNo).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GrdBillDetail_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHBillNo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHBillNo).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, GrdBillDetail_Mdl t1Row) {
                if (!DmShared.xNull(selectedBillTag).equalsIgnoreCase(DmShared.xNull(txtHBillNo.getTag()))) {
                    selectedBillTag = DmShared.xNull(txtHBillNo.getTag());
                    manageBillChange();
//                    flagFillGrid = true;
//                    ohlHandler.initialize();
                }
                if (blnStatus && t1Row != null) {
                    txtBillDate.setText(DmShared.dNull(t1Row.getPbV_Date()));
                    txtSupplierBillNoNew.setText(DmShared.xNull(t1Row.getPbRefNo()));
                    txtSupplierBillDate.setText(DmShared.dNull(t1Row.getPbRefDate()));
//                    txtSupplierBillNoNew.setEnabled(false);
                    txtSupplierBillDate.setEnabled(false);
                    List<KeyFieldExtended> listKF = new ArrayList<>();
                    KeyFieldExtended node = new KeyFieldExtended();
                    node.setSharedKeyFieldCode(DmShared.iNull(t1Row.getSharedKeyFieldCode()).toString());
                    node.setSharedKeyFieldName(DmShared.iNull(t1Row.getBillV_SNo()).toString());
                    listKF.add(node);
                    getBillDetail(listKF);

                }
            }
        });

        txtHMedicineName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHItemName_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHItem(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHItemName_Mdl>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMedicineName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHItemName_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHMedicineName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMedicineName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHItemName_Mdl t1Row) {
                if (!DmShared.xNull(selectedItemCode).equalsIgnoreCase(DmShared.xNull(txtHMedicineName.getTag()))) {
                    resetItem();
                    selectedItemCode = DmShared.xNull(txtHMedicineName.getTag());
                }
                if (blnStatus && t1Row != null) {
                    txtHMedicineName.setText(DmShared.xNull(t1Row.getSharedKeyFieldName()));
                    txtHMedicineName.setTag(DmShared.xNull(t1Row.getSharedKeyFieldCode()));
                    txtHMedicineName.setTag2nd(t1Row.getPropertyList());

                    txtMedicineCompany.setText(t1Row.getCompanyName());

                    hsnCode = DmShared.xNull(t1Row.getHsnCode());
                    itemGSTNature = DmShared.xNull(t1Row.getItemGSTNature());
                    itsBasedOn = DmShared.iNull(t1Row.getItsBasedOn()).toString();
                    isBestBefore = DmShared.iNull(t1Row.getIsBestBefore()).toString();

                    skuName = DmShared.xNull(t1Row.getSkuName());
                    skuCode = DmShared.xNull(t1Row.getSkuCode());
                    skuAllowDecimalPlaces = DmShared.xNull(t1Row.getSkuDecimalPlaces());

                    txtPurchaseUnit.setText(DmShared.xNull(t1Row.getPurchaseUnitDisplayName()));
                    txtPurchaseUnit.setTag(DmShared.xNull(t1Row.getPurchaseUnitCode()));
                    txtPurchaseUnit.setTag2nd(DmShared.xNull(t1Row.getPurchaseUnitDecimalPlaces()));
                    purchaseUnitName = DmShared.xNull(t1Row.getPurchaseUnitName());
                    purchaseUnitConversionRate = DmShared.xNull(t1Row.getPurchaseUnitConversionRate());
                    isIncludeFreeQtyInSaleRate = DmShared.xNull(t1Row.getIsIncludeFreeQtyInSaleRate());

                    setIts(txtHMedicineName.getTag(), txtRate.getText(), proReturnEntry.location, itsBasedOn, proReturnEntry.txtVoucherDate.getText());

                    calculate();
                    manageDisplay();
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
                if (!DmShared.xNull(selectedBatchNoText).equalsIgnoreCase(DmShared.xNull(txtHBatchNo.getText()))) {
                    resetBatchNo();
                    selectedBatchNoText = DmShared.xNull(txtHBatchNo.getText());
                }
                if (blnStatus && t1Row != null) {
                    txtMRP.setText(DmShared.xNull(t1Row.getMrp()));
                    txtSaleRate.setText(DmShared.xNull(t1Row.getSaleRate()));
                    txtExpiryDate.setText(DmShared.dNull(t1Row.getExpireDate()));
                    txtManufacturingDate.setText(DmShared.dNull(t1Row.getManufacturingDate()));
                    Date mfgDate = DmShared.convertDate(txtManufacturingDate.getText());
                    Date expDate = DmShared.convertDate(txtExpiryDate.getText());
                    if (mfgDate != null && expDate != null) {
                        txtBestBefore.setText(String.valueOf(CalendarUtil.getDaysBetween(mfgDate, expDate)));
                    }
                    calculate();
                    /*--------------------------------- Fetch PBill LandedSkuRate For Particular Item ----------------------------*/
//                    getLandedSkuRateForValidation(DmShared.xNull(t1Row.getSharedKeyFieldCode()), DmShared.xNull(txtHMedicineName.getTag()));
                }
            }
        });

        txtRemark.addKeyDownHandler(new DmRemarkTextBox.TxtRemarkKeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event, Object objSource) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    ((CellSelectionModel) overHeadLine.getDmGrid().getGrid().getSelectionModel()).selectCell(STClient.getOHLFocusIndex(overHeadLine.getOverHeadList()), 0);
                }
            }
        });
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Events">
    private void manageEvent() {
        txtSupplierBillDate.addBlurHandler(this);              // <M> 

        txtQuantity.addBlurHandler(this);
        txtFreeQty.addBlurHandler(this);
        txtRate.addBlurHandler(this);

        txtManufacturingDate.addBlurHandler(this);
        txtBestBefore.addBlurHandler(this);
        txtExpiryDate.addBlurHandler(this);
        txtMRP.addBlurHandler(this);
        txtSaleRate.addBlurHandler(this);

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
    public void onBlur(BlurEvent event) {
        if ((event.getSource() == txtManufacturingDate)) {
            txtManufacturingDate.setText(STClient.getDateFromMonthYear(txtManufacturingDate.getText(), false));
            setExpiryDate();
        } else if ((event.getSource() == txtBestBefore)) {
            setExpiryDate();
        } else if ((event.getSource() == txtExpiryDate)) {
            txtExpiryDate.setText(STClient.getDateFromMonthYear(txtExpiryDate.getText(), true));
        } else if ((event.getSource() == txtRate)) {
            if (DmShared.iNull(itsBasedOn).intValue() == DmEnum.ItsBasedOn.Rate.toInt()) {
                setIts(txtHMedicineName.getTag(), txtRate.getText(), proReturnEntry.location, itsBasedOn, proReturnEntry.txtVoucherDate.getText());
            }
//            if (!DmShared.xNull(txtHMedicineName.getTag()).trim().equalsIgnoreCase("") && inventoryAccountingActive) {
//                setStockPostingLedgers(txtHMedicineName.getTag());
//            }                       //   <MJ>
        } else if (event.getSource() == txtSupplierBillDate) {
            txtSupplierBillDate.setText(DmClient.returnDate(txtSupplierBillDate.getText()));
        }

        if (event.getSource() == txtQuantity
                || event.getSource() == txtFreeQty
                || event.getSource() == txtMRP
                || event.getSource() == txtRate
                || event.getSource() == txtSaleRate) {
            calculate();
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
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CallBack">
    interface CallBack {

        public void onOk(GrdItemDetail_Mdl grdGrnDetail_Mdl);

        public void onDelete();
    }
    private CallBack callBack;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
    //</editor-fold>

    private void getBillDetail(List<KeyFieldExtended> listKF) {
        srvAsync.invokeRPC(true).getItemDetail(DmShared.xNull(proReturnEntry.txtHVoucherType.getTag()), listKF, proReturnEntry.location, proReturnEntry.txtVoucherDate.getText(), proReturnEntry.topControl.getMode().equalsIgnoreCase("New") ? "" : proReturnEntry.topControl.getDocId(), new AsyncCallback<DmResult<ProformaPurchaseReturn_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<ProformaPurchaseReturn_Mdl> result) {

                if (result.isStatus() && result.getData() != null) {
                    if (result.getData().getListItemDetail().size() > 0) {
                        displayItemDetail(result.getData().getListItemDetail().get(0));
                        calculate();
                    }
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });

    }

    public void displayItemDetail(GrdItemDetail_Mdl ourRow) {
        pBillLandedSkuRate = ourRow.getpBillLandedSkuRate();
        //Clear
        resetPopup();
        txtHBillNo.setTag(DmShared.xNull(ourRow.getPbDocId()));
        txtHBillNo.setTag2nd(DmShared.xNull(ourRow.getPbV_SNo()));
        txtHBillNo.setText(DmShared.xNull(ourRow.getPbRecId()));

        txtBillDate.setText(DmShared.dNull(ourRow.getPbV_Date()));

        txtSupplierBillNoNew.setText(DmShared.xNull(ourRow.getPbRefNo()));
        txtSupplierBillDate.setTag(DmShared.dNull(ourRow.getPbRefDate()));
        txtSupplierBillDate.setText(DmShared.dNull(ourRow.getPbRefDate()));

        //Item
        selectedItemCode = ourRow.getSharedKeyFieldCode();
        txtHMedicineName.setTag(ourRow.getSharedKeyFieldCode());
        txtHMedicineName.setText(ourRow.getSharedKeyFieldName());

        txtMedicineCompany.setText(ourRow.getItemCompany());

        //Item Details
        itemGSTNature = ourRow.getItemGSTNature();
        itsBasedOn = ourRow.getItsBasedOn();
        hsnCode = ourRow.getHsnCode();
        isBestBefore = ourRow.getIsBestBefore();

        //Bill Details
        pbDocId = ourRow.getPbDocId();
        pbRecId = ourRow.getPbRecId();
        pbV_Type = ourRow.getPbV_Type();
        pbV_Date = ourRow.getPbV_Date();
        pbV_SNo = ourRow.getPbV_SNo();
        pbNetAmount = ourRow.getPbNetAmount();

        //Challan Details
        txtGrnNo.setTag(ourRow.getPcDocId());
        txtGrnNo.setText(ourRow.getPcRecId());
        txtGrnNo.setTag2nd(ourRow.getPcV_SNo());

        //Order Details
        txtOrderNo.setTag(ourRow.getPoDocId());
        txtOrderNo.setText(ourRow.getPoRecId());
        txtOrderNo.setTag2nd(ourRow.getPoV_SNo());
        txtOrderRefNo.setText(ourRow.getPoRefNo());

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
        gstUnitCode = ourRow.getGstUnitCode();

        //Quantities
        txtQuantity.setText(ourRow.getQuantity());
        txtQuantity.setTag2nd(ourRow.getPendingQty());
        txtFreeQty.setText(DmShared.xNull(ourRow.getFreeQty()));
        txtFreeQty.setTag2nd(DmShared.xNull(ourRow.getPendingFreeQty()));
        txtSkuQty.setText(DmShared.xNull(ourRow.getSkuQty()));

        //Rate
        txtMRP.setText(ourRow.getMrp());
        skuMrp = ourRow.getSkuMrp();
        txtSaleRate.setText(ourRow.getSaleRate());
        txtSaleRate.setTag(ourRow.getSaleRate());
        skuSaleRate = ourRow.getSkuSaleRate();
        txtRate.setText(ourRow.getRate());
        txtAmount.setText(ourRow.getAmount());
        skuRate = ourRow.getSkuRate();

        //Properties
        selectedBatchNoText = ourRow.getBatchNo();
        txtHBatchNo.setText(ourRow.getBatchNo());
        txtHBatchNo.setTag(ourRow.getIbCode());
        ibCode = ourRow.getIbCode();
        txtExpiryDate.setText(ourRow.getExpiryDate());
        txtManufacturingDate.setText(ourRow.getManufacturingDate());

        Date mfgDate = DmShared.convertDate(txtManufacturingDate.getText());
        Date expDate = DmShared.convertDate(txtExpiryDate.getText());
        if (mfgDate != null && expDate != null) {
            txtBestBefore.setText(String.valueOf(CalendarUtil.getDaysBetween(mfgDate, expDate)));
        }

        //Other
        txtRemark.setText(ourRow.getRemark());
        v_SNo = ourRow.getV_SNo();
        ibCode = ourRow.getIbCode();
        hashCode = ourRow.getSharedKeyFieldHashCode();
        overHeadLine.initialize(this.getCurrentRowIndex());

        discount = "";
        discountAt = "";

        OverHeadLine_Mdl ohlMdl = OverHeadLine_Shared.getOHByUniqueId("DS", ourRow.getOHLListPurchaseBill());
        if (ohlMdl != null) {
            discount = DmShared.xNull(ohlMdl.getAmount());
            discountAt = DmShared.xNull(ohlMdl.getAtRate());
        } else {
            OverHeadLine_Mdl ohlMdlGrid = OverHeadLine_Shared.getOHByUniqueId("DS", ourRow.getOHLList());
            if (ohlMdlGrid != null) {
                discount = DmShared.xNull(ohlMdlGrid.getAmount());
                discountAt = DmShared.xNull(ohlMdlGrid.getAtRate());
            }
        }

        setIts(txtHMedicineName.getTag(), txtRate.getText(), proReturnEntry.location, itsBasedOn, proReturnEntry.txtVoucherDate.getText());
        manageDisplay();

    }
    String discount = "";
    String discountAt = "";

    private void manageBillChange() {
        txtBillDate.setText("");
        txtBillDate.setTag("");
        txtBillDate.setTag2nd("");

        txtSupplierBillNoNew.setText("");
//        txtSupplierBillNo.setTag("");
//        txtSupplierBillNo.setTag2nd("");
        txtSupplierBillDate.setText("");
        txtSupplierBillDate.setTag("");
        txtSupplierBillDate.setTag2nd("");

        txtBillDate.setEnabled(true);
//        txtSupplierBillNoNew.setEnabled(true);
        txtSupplierBillDate.setEnabled(true);

//        txtHMedicineName.setEnabled(true);
    }

    //<editor-fold defaultstate="collapsed" desc="Clear">
    private void resetPopup() {
        selectedBillTag = "";                      //<MJ>
        txtHBillNo.setTag("");
        txtHBillNo.setTag2nd("");
        txtHBillNo.setText("");
        txtBillDate.setTag("");
        txtBillDate.setTag2nd("");
        txtBillDate.setText("");

        txtSupplierBillNoNew.setText("");

        txtSupplierBillDate.setTag("");
        txtSupplierBillDate.setTag2nd("");
        txtSupplierBillDate.setText("");

        selectedItemCode = "";
        txtHMedicineName.setText("");
        txtHMedicineName.setTag("");
        txtHMedicineName.setTag2nd("");
        txtMedicineCompany.setText("");
        resetItem();

        pbDocId = "";
        pbRecId = "";
        pbV_Type = "";
        pbV_Date = "";
        pbV_SNo = "";
        pbNetAmount = "";

        txtGrnNo.setText("");
        txtGrnNo.setTag("");
        txtGrnNo.setTag2nd("");

        txtOrderNo.setText("");
        txtOrderNo.setTag("");
        txtOrderNo.setTag2nd("");

        txtOrderRefNo.setText("");
        txtOrderRefNo.setTag("");
        txtOrderRefNo.setTag2nd("");

        txtQuantity.setText("");
        txtQuantity.setTag("");
        txtQuantity.setTag2nd("");

        txtFreeQty.setText("");
        txtFreeQty.setTag("");
        txtFreeQty.setTag2nd("");

        txtSkuQty.setText("");
        txtSkuQty.setTag("");
        txtSkuQty.setTag2nd("");
        skuRate = "";

        txtRate.setText("");
        txtRate.setTag("");
        txtRate.setTag2nd("");

        txtAmount.setText("");
        txtAmount.setTag("");
        txtAmount.setTag2nd("");

        txtRemark.setText("");
        txtRemark.setTag("");
        txtRemark.setTag2nd("");

        v_SNo = "";
    }

    private void resetItem() {

        ibCode = "";
        hsnCode = "";
        itemGSTNature = "";
        itsBasedOn = "";
        isBestBefore = "";

        skuCode = "";
        skuName = "";
        skuAllowDecimalPlaces = "";

        txtPurchaseUnit.setText("");
        txtPurchaseUnit.setTag("");
        txtPurchaseUnit.setTag2nd("");
        purchaseUnitName = "";
        purchaseUnitConversionRate = "";
        gstUnitCode = "";
        isIncludeFreeQtyInSaleRate = "";

        txtHBatchNo.setText("");
        txtHBatchNo.setTag("");
        txtHBatchNo.setTag2nd("");
        selectedBatchNoText = "";
        resetBatchNo();
    }

    private void resetBatchNo() {
        txtMRP.setText("");
        txtMRP.setTag("");
        txtMRP.setTag2nd("");
        skuMrp = "";

        txtSaleRate.setText("");
        txtSaleRate.setTag("");
        txtSaleRate.setTag2nd("");
        skuSaleRate = "";

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

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="getLandedSkuRateForValidation">
    private void getLandedSkuRateForValidation(String ibCode, String itemCode) {
        srvAsync.invokeRPC(true).getLandedSkuRateForValidation(ibCode, itemCode, new AsyncCallback<DmResult<KeyFieldExtended>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHMedicineName).show();
            }

            @Override
            public void onSuccess(DmResult<KeyFieldExtended> result) {
                if (result.isStatus()) {
                    pBillLandedSkuRate = DmShared.xNull(result.getData().getSharedKeyFieldCode());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtHMedicineName).show();
                }
            }
        });
    }
//</editor-fold>
}
