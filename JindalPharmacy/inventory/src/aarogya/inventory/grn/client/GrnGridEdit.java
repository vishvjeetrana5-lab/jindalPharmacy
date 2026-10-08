package aarogya.inventory.grn.client;

import aarogya.inventory.grn.shared.Grn_SrvAsync;
import aarogya.inventory.grn.shared.TxtHOrderNo_Mdl;
import aarogya.inventory.grn.shared.GrdGrnDetail_Mdl;
import aarogya.inventory.grn.shared.GrdImpurity_Mdl;
import aarogya.inventory.grn.shared.TxtHItemName_Mdl;
import aarogya.inventory.grn.shared.Grn_Srv;
import aarogya.selfToolkit.itemPropertyPopup.client.ItemPropertyPopup;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemPropertyPopup_Vld;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
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
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmEnum.GSTS_Head;
import dataman.dmBase.self.shared.DmEnum.ItsBasedOn;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.gsts.shared.GSTS_Mdl;
import java.util.ArrayList;
import aarogya.inventory.grn.shared.GrdLabEntry_Mdl;
import aarogya.selfToolkit.itemUpdatePopup.client.ItemUpdatePopup;
import aarogya.selfToolkit.itemUpdatePopup.shared.ItemUpdateField_Mdl;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Mdl;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Shared;
import java.util.HashMap;
import java.util.Map;

public class GrnGridEdit<T1, T2> extends GrnGridEdit_Dsg<T1, T2> implements BlurHandler, KeyUpHandler, KeyDownHandler, ClickHandler, AttachEvent.Handler {

    private final ServiceInvoker<Grn_SrvAsync> srvAsync;
    private final ItemPropertyPopup propertyPopup = new ItemPropertyPopup();
    private final ImpurityPopup impurityPopup = new ImpurityPopup();
    private final LabImpurityPopup labImpurityPopup = new LabImpurityPopup();
    private int hashCode = 0;
    private ItemProperty_Mdl propertyPopup_Mdl;
    List<GrdImpurity_Mdl> lstImpurity = new ArrayList<>();
    List<GrdImpurity_Mdl> lstImpurityTemp = new ArrayList<>();
    List<GrdLabEntry_Mdl> lstLabEntry = new ArrayList<>();
    
    private final ItemUpdatePopup itemUpdatePopup = new ItemUpdatePopup();
    List<ItemUpdateField_Mdl> lstItemUpdateFields = new ArrayList<>();
    String masterPurchaseRate = "";

    Grn grnEntry;
    String skuCode = "", skuName = "", skuAllowDecimalPlaces = "", purchaseUnitName = "", itemGSTNature = "",
            itsBasedOn = "", isBestBefore = "", purchaseUnitConversionRate = "", skuRate = "",
            v_SNo = "", ibCode = "";
    String selectedItemCode = "", selectedOrderNoCode = "", isLabRequired = "";
    boolean isFirstShow = true, canShowLabPopup = false;
    private String iSubCode = "", voucherCategory, defaultPurchaseDepartment = "", defaultPurchaseDepartmentText = "";

    public GrnGridEdit(DmGrid<T1, T2> grid, Grn grnEntry) {
        super(grid, "GRN Detail", "850px", "510px");
        srvAsync = new ServiceInvoker<Grn_SrvAsync>(GWT.create(Grn_Srv.class), Grn.class.getName());
        this.grnEntry = grnEntry;
        textHelpEvents();
        popupCallBacks();
        gridEvents();
        manageEvent();
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, String voucherCategory) {
        this.voucherCategory = voucherCategory;
        resetPopup();
        super.showCenter(currentRowIndex, okNeeded, newNeeded, deleteNeeded);

        if (isFirstShow) {
            isFirstShow = false;
        }
        
        if (grnEntry.defaultValue.isInventoryAccountingActive() && !grnEntry.defaultValue.isIncludeOverHeadsInCost()){
//            txtDiscountPer.setEnabled(false);
//            txtDiscount.setEnabled(false);
        }
        
//        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
//            @Override
//            public void execute() {
//                if (grnEntry.departmentEditable) {
//                    txtHDepartment.setFocus(true);
//                } else {
//                    if (txtHItemName.isEnabled()) {
//                        txtHItemName.setFocus(true);
//                    } else {
//                        txtChallanQty.setFocus(true);
//                    }
//                }
//
//            }
//        });

    }

    private void getGSTS(String itemCode, String itemRate, String itsBasedOn, String location, String gstTypeCode, final boolean isManualGst) {
        srvAsync.invokeRPC(true).getGSTS(itemCode, itemRate, itsBasedOn, location, gstTypeCode, isManualGst, new AsyncCallback<DmResult<List<GSTS_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHItemName).show();
            }

            @Override
            public void onSuccess(DmResult<List<GSTS_Mdl>> result) {
                if (result.isStatus() && result.getData() != null) {
                    if (!isManualGst) {
                        txtHGstType.setText(result.getValue2());
                        txtHGstType.setTag(result.getValue1());
                        txtHGstType.setTag2nd(result.getValue1());
                    }
                    setGSTS(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtHItemName).show();
                }
            }
        });
    }

    private void getQCPaparameter(String itemCode) {
        lstImpurityTemp.clear();
        resetRecievedQty();
        srvAsync.invokeRPC(true).getQCPaparameter(itemCode, new AsyncCallback<DmResult<List<GrdImpurity_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHItemName).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdImpurity_Mdl>> result) {
                if (result.isStatus()) {
                    lstImpurity.addAll(result.getData());
                    lstImpurityTemp.addAll(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtHItemName).show();
                }
            }
        });
    }

    private void manageDisplay() {
        setQtyUnits();
        txtAmount.setEnabled(false);
        txtPoRefNo.setEnabled(false);
        txtAcceptQty.setEnabled(false);
        txtSKUQty.setEnabled(false);
        txtSkuQtyUnit.setEnabled(false);
        txtAcceptQtyUnit.setEnabled(false);

        txtRejectedQty.setEnabled(false);
        txtRejectedQtyUnit.setEnabled(false);

        txtShortageQty.setEnabled(false);
        txtShortageQtyUnit.setEnabled(false);
        txtHGstType.setEnabled(false);

        if (!DmShared.xNull(txtHItemName.getTag()).equalsIgnoreCase("")
                && DmShared.xNull(txtHItemName.getTag2nd()).equalsIgnoreCase("")) {
            txtProperties.setEnabled(false);
        } else {
            txtProperties.setEnabled(true);
        }

        if (!DmShared.xNull(grnEntry.txtHGateEntryNo.getTag()).equalsIgnoreCase("")) {
            txtHDepartment.setEnabled(false);
            txtHOrderNo.setEnabled(false);
            txtHItemName.setEnabled(false);
        } else {
            if (!DmShared.xNull(txtHOrderNo.getTag()).equalsIgnoreCase("")) {
                txtHItemName.setEnabled(false);
                txtRate.setEnabled(false);
            } else {
                txtHDepartment.setEnabled(true);
                txtHOrderNo.setEnabled(true);
                txtHItemName.setEnabled(true);
                txtRate.setEnabled(true);
                if (!grnEntry.departmentEditable) {
                    txtHDepartment.setEnabled(false);
                }

            }
        }

        //Footer Values
        txtTaxableAmt.setEnabled(false);
        txtSgstUgstPer.setEnabled(false);
        txtSGSTPer.setEnabled(false);
        txtSGST.setEnabled(false);
        txtUGSTPer.setEnabled(false);
        txtUGST.setEnabled(false);
        txtCGSTPer.setEnabled(false);
        txtIGSTPer.setEnabled(false);
        txtSubTotal.setEnabled(false);
        txtFinalDiscountPer.setEnabled(false);
        txtFinalDiscount.setEnabled(false);
        txtItemValue.setEnabled(false);
        manageTaxControls();

    }

    private void manageTaxControls() {
        txtHGstType.setEnabled(cbIsManualGstType.getValue());

        txtIGST.setEnabled(false);
        txtSgstUgst.setEnabled(false);
        txtCGST.setEnabled(false);

        if (cbIsManualTax.getValue()) {
            if (DmShared.xNull(grnEntry.location).equalsIgnoreCase(String.valueOf(DmEnum.DmLocation.InterState.getId()))) {
                txtIGST.setEnabled(true);
            } else if (DmShared.xNull(grnEntry.location).equalsIgnoreCase(String.valueOf(DmEnum.DmLocation.IntraState.getId()))) {
                txtSgstUgst.setEnabled(true);
                txtCGST.setEnabled(true);
            }
        }
    }

    protected void calculate() {
        double purchaseUnitConvRate = DmShared.vNull(this.purchaseUnitConversionRate);
        double recQty = DmShared.vNull(txtReceiveQty.getText());
        double qcQty = DmShared.vNull(txtQCQty.getText());
        double skuDifferenceQty = DmShared.vNull(txtSkuDifferenceQty.getText());
        double rate = DmShared.vNull(txtRate.getText());

        String unitDecimalFormat = DmShared.getNumberFormat(DmShared.iNull(txtPurchaseUnit.getTag2nd()).intValue());
        String skuDecimalFormat = DmShared.getNumberFormat(DmShared.iNull(skuAllowDecimalPlaces).intValue());

        txtSGSTPer.setText("");
        txtSGST.setText("");
        txtUGSTPer.setText("");
        txtUGST.setText("");

        //calculate lab rejection qty
        txtLabQty.setText(grnEntry.calculateFormulaForQty(lstLabEntry, txtReceiveQty.getText(), txtRate.getText(), DmShared.iNull(txtPurchaseUnit.getTag2nd()).intValue()));
        double labQty = DmShared.vNull(txtLabQty.getText());

        double rejQty = DmShared.vNull(labQty + qcQty);
        txtRejectedQty.setText(DmShared.vNull(rejQty).toString());

        txtSkuDifferenceQty.setText(DmClient.vNull(txtSkuDifferenceQty.getText(), skuDecimalFormat));

        //calculate accepted qty
        txtAcceptQty.setText(DmClient.vNull(recQty - rejQty, unitDecimalFormat));

        //calculate SKU qty.
        txtSKUQty.setText(DmClient.vNull((DmShared.vNull(txtAcceptQty.getText()) * purchaseUnitConvRate) + skuDifferenceQty, skuDecimalFormat));

        //calculate drCrNote Amount
        txtDrCrNoteAmt.setText(grnEntry.calculateFormulaForAmt(lstLabEntry, txtReceiveQty.getText(), txtAcceptQty.getText(), txtRate.getText()));

        txtAmount.setText(DmClient.vNull(DmShared.vNull(txtAcceptQty.getText()) * rate, "0.00"));
        skuRate = DmClient.vNull(DmShared.vNull(txtAmount.getText()) / DmShared.vNull(txtSKUQty.getText()), "0.0000000");

        if (DmShared.vNull(txtDiscountPer.getText()).doubleValue() > 0) {
            txtDiscount.setText(DmClient.vNull(DmShared.vNull(txtAmount.getText()).doubleValue() * DmShared.vNull(txtDiscountPer.getText()).doubleValue() * 0.01, "0.00"));
        }

        double taxableAmt = DmShared.vNull(txtAmount.getText()).doubleValue() - DmShared.vNull(txtDiscount.getText()).doubleValue();
        txtTaxableAmt.setText(DmClient.vNull(taxableAmt, "0.00"));
        txtSgstUgst.setTag2nd(DmClient.vNull(taxableAmt * DmShared.vNull(txtSgstUgstPer.getText()).doubleValue() * 0.01, "0.00"));
        if (!cbIsManualTax.getValue()) {
            txtSgstUgst.setText(txtSgstUgst.getTag2nd());
        }
        if (!(DmShared.iNull(grnEntry.defaultValue.getIsSiteStateUT()).intValue() == 1)) {
            txtSGSTPer.setText(txtSgstUgstPer.getText());
            txtSGST.setText(txtSgstUgst.getText());
        } else if ((DmShared.iNull(grnEntry.defaultValue.getIsSiteStateUT()).intValue() == 1)) {
            txtUGSTPer.setText(txtSgstUgstPer.getText());
            txtUGST.setText(txtSgstUgst.getText());
        }
        txtCGST.setTag2nd(DmClient.vNull(taxableAmt * DmShared.vNull(txtCGSTPer.getText()).doubleValue() * 0.01, "0.00"));
        txtIGST.setTag2nd(DmClient.vNull(taxableAmt * DmShared.vNull(txtIGSTPer.getText()).doubleValue() * 0.01, "0.00"));
        if (!cbIsManualTax.getValue()) {
            txtCGST.setText(txtCGST.getTag2nd());
            txtIGST.setText(txtIGST.getTag2nd());
        }

        double subTotal = 0;
        if (DmShared.iNull(grnEntry.txtHRCMApplicable.getTag()).intValue() == 1) {
            subTotal = taxableAmt;
        } else {
            subTotal = taxableAmt + DmShared.vNull(txtSgstUgst.getText()).doubleValue() + DmShared.vNull(txtCGST.getText()).doubleValue() + DmShared.vNull(txtIGST.getText()).doubleValue();
        }
        txtSubTotal.setText(DmClient.vNull(subTotal, "0.00"));
        if (DmShared.vNull(txtFinalDiscountPer.getText()) > 0) {
            txtFinalDiscount.setText(DmClient.vNull(taxableAmt * DmShared.vNull(txtFinalDiscountPer.getText()) * 0.01, "0.00"));
        }
        txtItemValue.setText(DmClient.vNull(subTotal - DmShared.vNull(txtFinalDiscount.getText()).doubleValue(), "0.00"));

        //<editor-fold defaultstate="collapsed" desc="Format decimal places of all quantities">
        txtChallanQty.setText(DmClient.vNull(txtChallanQty.getText(), unitDecimalFormat));
        txtReceiveQty.setText(DmClient.vNull(txtReceiveQty.getText(), unitDecimalFormat));
        txtShortageQty.setText(DmClient.vNull(DmShared.vNull(txtChallanQty.getText()) - DmShared.vNull(txtReceiveQty.getText()), unitDecimalFormat));
        txtLabQty.setText(DmClient.vNull(txtLabQty.getText(), unitDecimalFormat));
        txtQCQty.setText(DmClient.vNull(txtQCQty.getText(), unitDecimalFormat));
        txtRejectedQty.setText(DmClient.vNull(txtRejectedQty.getText(), unitDecimalFormat));
        txtAcceptQty.setText(DmClient.vNull(txtAcceptQty.getText(), unitDecimalFormat));
        txtSkuDifferenceQty.setText(DmClient.vNull(txtSkuDifferenceQty.getText(), skuDecimalFormat));
        txtSKUQty.setText(DmClient.vNull(txtSKUQty.getText(), skuDecimalFormat));
        //</editor-fold>

        manageDisplay();
    }

    //<editor-fold defaultstate="collapsed" desc="GridEdit Overrides">
    @Override
    public void onOk() {
        try {
            calculate();
            if (DmShared.xNull(txtHDepartment.getTag()).trim().equalsIgnoreCase("")) {
                new DmAlertMessageBox("Please select Department.", txtHDepartment).show();
                return;
            }

            if (DmShared.iNull(txtHItemName.getTag()).longValue() == 0) {
                new DmAlertMessageBox("Item Name is mandatory.", txtHItemName).show();
                return;
            }
            if (DmShared.iNull(txtPurchaseUnit.getTag()).longValue() == 0) {
                new DmAlertMessageBox("Unit is mandatory.", txtPurchaseUnit).show();
                return;
            }
            if (!DmShared.xNull(txtHItemName.getTag2nd()).trim().equalsIgnoreCase("")) {
                try {
                    ItemPropertyPopup_Vld.validateProperties(txtHItemName.getTag2nd(), isBestBefore, grnEntry.txtVoucherDate.getText(), propertyPopup_Mdl);
                } catch (DmException e) {
                    new DmAlertMessageBox("Properties : " + e.getMessage(), txtProperties).show();
                    return;
                }
            }
            if (DmShared.vNull(txtChallanQty.getText()) == 0) {
                new DmAlertMessageBox("Challan Qty. is mandatory.", txtChallanQty).show();
                return;
            }
            if (DmShared.vNull(txtReceiveQty.getText()) == 0) {
                new DmAlertMessageBox("Received Qty. is mandatory.", txtReceiveQty).show();
                return;
            }
            if (DmShared.vNull(txtChallanQty.getText()) < DmShared.vNull(txtReceiveQty.getText())) {
                new DmAlertMessageBox("Received Qty. should be less than or equal to Challan Qty.", txtReceiveQty).show();
                return;
            }
            if (DmShared.vNull(txtLabQty.getText()).doubleValue() < 0) {
                new DmAlertMessageBox("Lab Rejected Qty should not be negative.", txtLabQty).show();
                return;
            }
            if (DmShared.vNull(txtReceiveQty.getText()) <= DmShared.vNull(txtRejectedQty.getText())) {
                new DmAlertMessageBox("Rejected Qty. should be less than or equal to Received Qty.", txtRejectedQty).show();
                return;
            }
            if (DmShared.vNull(txtAcceptQty.getText()) == 0) {
                new DmAlertMessageBox("Accept Qty. is mandatory.", txtReceiveQty).show();
                return;
            }
            if (DmShared.vNull(txtAcceptQty.getText()) < 0) {
                new DmAlertMessageBox("Accept Qty. can't be negative.", txtReceiveQty).show();
                return;
            }
            if (DmShared.vNull(txtSKUQty.getText()) == 0) {
                new DmAlertMessageBox("SKU Qty. is mandatory.", txtSKUQty).show();
                return;
            }
            if (DmShared.vNull(txtSKUQty.getText()) < 0) {
                new DmAlertMessageBox("SKU Qty. is can't be negative.", txtSKUQty).show();
                return;
            }
            if (DmShared.vNull(txtRate.getText()) == 0) {
                new DmAlertMessageBox("Rate is mandatory.", txtRate).show();
                return;
            }
            if (DmShared.vNull(propertyPopup_Mdl.getMrp()) > 0) {
                if (DmShared.vNull(propertyPopup_Mdl.getMrp()) < DmShared.vNull(txtRate.getText())) {
                    new DmAlertMessageBox("MRP should be greater than or equal to rate.", txtProperties).show();
                    return;
                }
            }
            if (DmShared.iNull(txtHOrderNo.getTag()).longValue() != 0) {
                if (DmShared.vNull(txtAcceptQty.getTag()) > DmShared.vNull(txtAcceptQty.getTag2nd())) {
                    new DmAlertMessageBox("Accept Qty. should be less than or equal to Pending Qty. (" + DmShared.xNull(txtAcceptQty.getTag2nd()) + ").", txtReceiveQty).show();
                    return;
                }
            }
            if (cbIsManualTax.getValue()) {
                if (txtSgstUgst.isEnabled()) {
                    if (DmShared.compute("Math.abs(" + DmClient.vNull(DmShared.vNull(txtSgstUgst.getTag2nd()).doubleValue() - DmShared.vNull(txtSgstUgst.getText()).doubleValue(), "0.00") + ")") >= 1.0) {
                        new DmAlertMessageBox("" + DmShared.xNull(lblSgstUgst.getText()) + " is not valid", txtSgstUgst).show();
                        return;
                    }
                }
                if (txtCGST.isEnabled()) {
                    if (DmShared.compute("Math.abs(" + DmClient.vNull(DmShared.vNull(txtCGST.getTag2nd()).doubleValue() - DmShared.vNull(txtCGST.getText()).doubleValue(), "0.00") + ")") >= 1.0) {
                        new DmAlertMessageBox("CGST is not valid", txtCGST).show();
                        return;
                    }
                }
                if (txtIGST.isEnabled()) {
                    if (DmShared.compute("Math.abs(" + DmClient.vNull(DmShared.vNull(txtIGST.getTag2nd()).doubleValue() - DmShared.vNull(txtIGST.getText()).doubleValue(), "0.00") + ")") >= 1.0) {
                        new DmAlertMessageBox("IGST is not valid", txtIGST).show();
                        return;
                    }
                }

            }

            if (grnEntry.defaultValue.isInventoryAccountingActive()) {
                if (DmShared.iNull(iSubCode).longValue() == 0) {
                    new DmAlertMessageBox("Inventory a/c is required.", txtHItemName).show();
                    return;
                }
            }

            /*==============================<< Check Item dublicacy with Batch No and Order No. >>====================================*/
            for (GrdGrnDetail_Mdl detail_Mdl : (List<GrdGrnDetail_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
                if (DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(txtHItemName.getTag()))
                        && DmShared.xNull(detail_Mdl.getBatchNo()).equalsIgnoreCase(DmShared.xNull(propertyPopup_Mdl.getBatchNo()))
                        && DmShared.xNull(detail_Mdl.getLotNo()).equalsIgnoreCase(DmShared.xNull(propertyPopup_Mdl.getLotNo()))
                        && DmShared.xNull(detail_Mdl.getColourCode()).equalsIgnoreCase(DmShared.xNull(propertyPopup_Mdl.getColourCode()))
                        && DmShared.xNull(detail_Mdl.getSizeCode()).equalsIgnoreCase(DmShared.xNull(propertyPopup_Mdl.getSizeCode()))
                        && DmShared.xNull(detail_Mdl.getItemMakeCode()).equalsIgnoreCase(DmShared.xNull(propertyPopup_Mdl.getItemMakeCode()))
                        && DmShared.vNull(detail_Mdl.getMrp()).doubleValue() == DmShared.vNull(propertyPopup_Mdl.getMrp()).doubleValue()
                        && DmShared.dNull(detail_Mdl.getManufacturingDate()).equalsIgnoreCase(DmShared.dNull(propertyPopup_Mdl.getManufacturingDate()))
                        && DmShared.dNull(detail_Mdl.getExpiryDate()).equalsIgnoreCase(DmShared.dNull(propertyPopup_Mdl.getExpiryDate()))
                        && DmShared.xNull(detail_Mdl.getPurchaseUnitCode()).equalsIgnoreCase(DmShared.xNull(txtPurchaseUnit.getTag()))
                        && DmShared.xNull(detail_Mdl.getDepartmentCode()).equalsIgnoreCase(DmShared.xNull(txtHDepartment.getTag()))
                        && DmShared.xNull(detail_Mdl.getPoDocId()).equalsIgnoreCase(DmShared.xNull(txtHOrderNo.getTag()))
                        && DmShared.xNull(detail_Mdl.getPoV_SNo()).equalsIgnoreCase(DmShared.xNull(txtHOrderNo.getTag2nd()))) {
                    if (!DmClient.vNull(detail_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                        String message = "<div style='float:right; margin-right: 5px;'>";
                        message += "Record already exist with following details - <br>";
                        message += (DmShared.xNull(txtHDepartment.getText()).trim().equals("")) ? "" : "<b>Department:</b> " + DmShared.xNull(txtHDepartment.getText()) + "<br>";
                        message += (DmShared.xNull(txtHOrderNo.getText()).trim().equals("")) ? "" : "<b>Order No.:</b> " + DmShared.xNull(txtHOrderNo.getText()) + "<br>";
                        message += (DmShared.xNull(txtHItemName.getText()).trim().equals("")) ? "" : "<b>Item:</b> " + DmShared.xNull(txtHItemName.getText()) + "<br>";
                        message += (DmShared.xNull(txtPurchaseUnit.getText()).trim().equals("")) ? "" : "<b>Unit:</b> " + DmShared.xNull(txtPurchaseUnit.getText()) + "<br>";
                        message += (DmShared.xNull(propertyPopup_Mdl.getBatchNo()).trim().equals("")) ? "" : "<b>Batch No.:</b> " + DmShared.xNull(propertyPopup_Mdl.getBatchNo()) + "<br>";
                        message += (DmShared.xNull(propertyPopup_Mdl.getLotNo()).trim().equals("")) ? "" : "<b>Lot No.:</b> " + DmShared.xNull(propertyPopup_Mdl.getLotNo()) + "<br>";
                        message += (DmShared.xNull(propertyPopup_Mdl.getColourName()).trim().equals("")) ? "" : "<b>Colour:</b> " + DmShared.xNull(propertyPopup_Mdl.getColourName()) + "<br>";
                        message += (DmShared.xNull(propertyPopup_Mdl.getSizeName()).trim().equals("")) ? "" : "<b>Size:</b> " + DmShared.xNull(propertyPopup_Mdl.getSizeName()) + "<br>";
                        message += (DmShared.xNull(propertyPopup_Mdl.getItemName()).trim().equals("")) ? "" : "<b>Colour:</b> " + DmShared.xNull(propertyPopup_Mdl.getItemName()) + "<br>";
                        message += (DmShared.vNull(propertyPopup_Mdl.getMrp()) == 0) ? "" : "<b>MRP:</b> " + DmClient.vNull(propertyPopup_Mdl.getMrp(), "0.00") + "<br>";
                        message += (DmShared.dNull(propertyPopup_Mdl.getManufacturingDate()).trim().equals("")) ? "" : "<b>Manufacturing Date:</b> " + DmShared.dNull(propertyPopup_Mdl.getManufacturingDate()) + "<br>";
                        message += (DmShared.dNull(propertyPopup_Mdl.getExpiryDate()).trim().equals("")) ? "" : "<b>Expiry Date:</b> " + DmShared.dNull(propertyPopup_Mdl.getExpiryDate()) + "<br>";
                        message += "</div>";
                        new DmAlertMessageBox(message, txtHItemName).show();
                        return;
                    }
                }
            }

            showItemUpdatePopup();
            
//            proceedOk();
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtHItemName).show();
        }
    }

    @Override
    public void manageOk(T1 row) {
        GrdGrnDetail_Mdl ourRow = (GrdGrnDetail_Mdl) row;
        fillRowData(ourRow);
        callBack.onOk(ourRow);
    }

    protected void fillRowData(GrdGrnDetail_Mdl ourRow) {
        //Department
        ourRow.setDepartmentCode(DmShared.xNull(txtHDepartment.getTag()));
        ourRow.setDepartmentName(DmShared.xNull(txtHDepartment.getText()));

        //Item
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHItemName.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHItemName.getText()));
        ourRow.setIbCode(DmShared.xNull(ibCode));
        ourRow.setIsLabRequired(DmShared.xNull(isLabRequired));

        //Item Details
        ourRow.setPropertyList(DmShared.xNull(txtHItemName.getTag2nd()).trim());
        ourRow.setItemGSTNature(DmShared.xNull(itemGSTNature));
        ourRow.setItsBasedOn(DmShared.xNull(itsBasedOn));
        ourRow.setHsnCode(DmShared.xNull(txtHSN.getTag()));
        ourRow.setHsn(DmShared.xNull(txtHSN.getText()));
        ourRow.setIsBestBefore(DmShared.xNull(isBestBefore));
        ourRow.setManualTax(cbIsManualTax.getValue());
        ourRow.setManualGstType(cbIsManualGstType.getValue());
        ourRow.setGstTypeCode(DmShared.xNull(txtHGstType.getTag()));
        ourRow.setGstTypeName(DmShared.xNull(txtHGstType.getText()));

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
        ourRow.setBatchNo(DmShared.xNull(propertyPopup_Mdl.getBatchNo()));
        ourRow.setLotNo(DmShared.xNull(propertyPopup_Mdl.getLotNo()));
        ourRow.setManufacturingDate(DmShared.dNull(propertyPopup_Mdl.getManufacturingDate()));
        ourRow.setExpiryDate(DmShared.dNull(propertyPopup_Mdl.getExpiryDate()));
        ourRow.setMrp(DmClient.vNull(propertyPopup_Mdl.getMrp(), "0.00"));
        ourRow.setSkuMrp(DmClient.vNull(propertyPopup_Mdl.getSkuMrp(), "0.0000000"));
        ourRow.setColourCode(DmShared.xNull(propertyPopup_Mdl.getColourCode()));
        ourRow.setColourName(DmShared.xNull(propertyPopup_Mdl.getColourName()));
        ourRow.setItemMakeCode(DmShared.xNull(propertyPopup_Mdl.getItemMakeCode()));
        ourRow.setItemMakeName(DmShared.xNull(propertyPopup_Mdl.getItemMakeName()));
        ourRow.setSizeCode(DmShared.xNull(propertyPopup_Mdl.getSizeCode()));
        ourRow.setSizeName(DmShared.xNull(propertyPopup_Mdl.getSizeName()));
        ourRow.setPropertiesCsv(DmShared.xNull(propertyPopup_Mdl.getPropertiesCsv()));

        //Order Details
        ourRow.setPoDocId(DmShared.xNull(txtHOrderNo.getTag()));
        ourRow.setPoRecId(DmShared.xNull(txtHOrderNo.getText()));
        ourRow.setPoV_SNo(DmShared.xNull(txtHOrderNo.getTag2nd()));
        ourRow.setPoRefNo(DmShared.xNull(txtPoRefNo.getText()));

        //Quantities
        ourRow.setChallanQty(DmShared.xNull(txtChallanQty.getText()));
        ourRow.setRecQty(DmShared.xNull(txtReceiveQty.getText()));
        ourRow.setShortageQty(DmShared.xNull(txtShortageQty.getText()));

        ourRow.setLabRejectedQty(DmShared.xNull(txtLabQty.getText()));
        ourRow.setQcRejectedQty(DmShared.xNull(txtQCQty.getText()));
        ourRow.setRejQty(DmShared.xNull(txtRejectedQty.getText()));
        ourRow.setDrCrNoteAmt(DmShared.xNull(txtDrCrNoteAmt.getText()));

        ourRow.setSkuDifferenceQty(DmShared.xNull(txtSkuDifferenceQty.getText()));
        ourRow.setSkuQty(DmShared.xNull(txtSKUQty.getText()));
        ourRow.setAcceptQty(DmShared.xNull(txtAcceptQty.getText()));
        ourRow.setPendingQty(DmShared.xNull(txtAcceptQty.getTag2nd()));

        //Rate
        ourRow.setRate(DmClient.vNull(txtRate.getText(), "0.00"));
        ourRow.setSkuRate(DmShared.xNull(skuRate));

        //Amount
        ourRow.setAmount(DmClient.vNull(txtAmount.getText(), "0.00"));
        ourRow.setDiscountPer(DmClient.vNull(txtDiscountPer.getText(), "0.00"));
        ourRow.setDiscount(DmClient.vNull(txtDiscount.getText(), "0.00"));
        ourRow.setTaxableAmt(DmClient.vNull(txtTaxableAmt.getText(), "0.00"));
        ourRow.setSgstUgstPer(DmClient.vNull(txtSgstUgstPer.getText(), "0.00"));
        ourRow.setSgstUgst(DmClient.vNull(txtSgstUgst.getText(), "0.00"));
        ourRow.setSgstPer(DmClient.vNull(txtSGSTPer.getText(), "0.00"));
        ourRow.setSgst(DmClient.vNull(txtSGST.getText(), "0.00"));
        ourRow.setUgstPer(DmClient.vNull(txtUGSTPer.getText(), "0.00"));
        ourRow.setUgst(DmClient.vNull(txtUGST.getText(), "0.00"));
        ourRow.setCgstPer(DmClient.vNull(txtCGSTPer.getText(), "0.00"));
        ourRow.setCgst(DmClient.vNull(txtCGST.getText(), "0.00"));
        ourRow.setIgstPer(DmClient.vNull(txtIGSTPer.getText(), "0.00"));
        ourRow.setIgst(DmClient.vNull(txtIGST.getText(), "0.00"));
        ourRow.setSubTotal(DmClient.vNull(txtSubTotal.getText(), "0.00"));
        ourRow.setFinalDiscountPer(DmClient.vNull(txtFinalDiscountPer.getText(), "0.00"));
        ourRow.setFinalDiscount(DmClient.vNull(txtFinalDiscount.getText(), "0.00"));
        ourRow.setItemValue(DmClient.vNull(txtItemValue.getText(), "0.00"));

        //IAP
        ourRow.setIsubCode(DmShared.xNull(iSubCode));

        //Other
        ourRow.setRemark(DmShared.xNull(txtRemark.getText()).trim());

        ourRow.getLstImpurity().clear();
        ourRow.getLstImpurity().addAll(lstImpurity);

        ourRow.getLstLabEntry().clear();
        ourRow.getLstLabEntry().addAll(lstLabEntry);
        
        ourRow.getLstItemUpdateFields().clear();
        ourRow.getLstItemUpdateFields().addAll(lstItemUpdateFields);

    }

    @Override
    public void displayRow(T1 row) {
        GrdGrnDetail_Mdl ourRow = (GrdGrnDetail_Mdl) row;
        GrdGrnDetail_Mdl previousRow = null;

        //Clear
        resetPopup();

        //Department
        if (DmShared.iNull(ourRow.getSharedKeyFieldCode()).longValue() == 0) {
            if (this.getCurrentRowIndex() > 0) {
                if (this.getCurrentRowIndex() == (grnEntry.grdGRNDetail.getGrid().getStore().size() - 1)) {
                    previousRow = grnEntry.grdGRNDetail.getGrid().getStore().get(this.getCurrentRowIndex() - 1);
                }
            }
        }
        if (previousRow != null) {
            txtHDepartment.setTag(DmShared.xNull(previousRow.getDepartmentCode()));
            txtHDepartment.setText(DmShared.xNull(previousRow.getDepartmentName()));
        } else {
            if (DmShared.iNull(ourRow.getDepartmentCode()).longValue() == 0) {
                if (DmShared.iNull(grnEntry.defaultValue.getDefaultPurchaseDepartment()).longValue() == 0) {
                    txtHDepartment.setTag(grnEntry.defaultValue.getDepartmentCode());
                    txtHDepartment.setText(grnEntry.defaultValue.getDepartmentName());
                } else {
                    txtHDepartment.setTag(grnEntry.defaultValue.getDefaultPurchaseDepartment());
                    txtHDepartment.setText(grnEntry.defaultValue.getDefaultPurchaseDepartmentText());
                }
            } else {
                txtHDepartment.setTag(ourRow.getDepartmentCode());
                txtHDepartment.setText(ourRow.getDepartmentName());
            }
        }
        txtHDepartment.setTag2nd(DmShared.xNull(txtHDepartment.getTag()));

        //Item
        ibCode = DmShared.xNull(ourRow.getIbCode());
        selectedItemCode = ourRow.getSharedKeyFieldCode();
        txtHItemName.setTag(ourRow.getSharedKeyFieldCode());
        txtHItemName.setText(ourRow.getSharedKeyFieldName());
        reQuery();

        //Item Details
        txtHItemName.setTag2nd(ourRow.getPropertyList());
        itemGSTNature = ourRow.getItemGSTNature();
        itsBasedOn = ourRow.getItsBasedOn();
        isBestBefore = ourRow.getIsBestBefore();
        isLabRequired = ourRow.getIsLabRequired();
        cbIsManualTax.setValue(ourRow.isManualTax());
        cbIsManualGstType.setValue(ourRow.isManualGstType());
        txtHGstType.setText(DmShared.xNull(ourRow.getGstTypeName()));
        txtHGstType.setTag(DmShared.xNull(ourRow.getGstTypeCode()));
        txtHGstType.setTag2nd(DmShared.xNull(ourRow.getGstTypeCode()));
        txtHSN.setText(DmShared.xNull(ourRow.getHsn()));
        txtHSN.setTag(DmShared.xNull(ourRow.getHsnCode()));

        //IAP
        iSubCode = DmShared.xNull(ourRow.getIsubCode());

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

        masterPurchaseRate=ourRow.getPurchaseRate();

        //Properties
        propertyPopup_Mdl = new ItemProperty_Mdl();
        propertyPopup_Mdl.setItemCode(ourRow.getSharedKeyFieldCode());
        propertyPopup_Mdl.setBatchNo(ourRow.getBatchNo());
        propertyPopup_Mdl.setLotNo(ourRow.getLotNo());
        propertyPopup_Mdl.setManufacturingDate(ourRow.getManufacturingDate());
        propertyPopup_Mdl.setExpiryDate(ourRow.getExpiryDate());
        propertyPopup_Mdl.setMrp(ourRow.getMrp());
        propertyPopup_Mdl.setSkuMrp(ourRow.getSkuMrp());
        propertyPopup_Mdl.setColourCode(ourRow.getColourCode());
        propertyPopup_Mdl.setColourName(ourRow.getColourName());
        propertyPopup_Mdl.setSizeCode(ourRow.getSizeCode());
        propertyPopup_Mdl.setSizeName(ourRow.getSizeName());
        propertyPopup_Mdl.setItemMakeCode(ourRow.getItemMakeCode());
        propertyPopup_Mdl.setItemMakeName(ourRow.getItemMakeName());
        txtProperties.setText(propertyPopup_Mdl.getPropertiesCsv());

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
        txtShortageQty.setText(ourRow.getShortageQty());
        txtLabQty.setText(ourRow.getLabRejectedQty());
        txtQCQty.setText(ourRow.getQcRejectedQty());
        txtRejectedQty.setText(ourRow.getRejQty());
        txtSkuDifferenceQty.setText(ourRow.getSkuDifferenceQty());
        txtSKUQty.setText(ourRow.getSkuQty());
        txtAcceptQty.setText(ourRow.getAcceptQty());
        txtAcceptQty.setTag2nd(ourRow.getPendingQty());

        //Rate
        txtRate.setText(ourRow.getRate());
        skuRate = ourRow.getSkuRate();
        txtDrCrNoteAmt.setText(ourRow.getDrCrNoteAmt());

        //Amount
        txtAmount.setText(ourRow.getAmount());
        txtDiscountPer.setText(ourRow.getDiscountPer());
        txtDiscount.setText(ourRow.getDiscount());
        txtTaxableAmt.setText(ourRow.getTaxableAmt());
        if (DmShared.vNull(ourRow.getSgstPer()).doubleValue() > 0) {
            lblSgstUgst.setText(GSTS_Head.SGST.getName());
        } else if (DmShared.vNull(ourRow.getUgstPer()).doubleValue() > 0) {
            lblSgstUgst.setText(GSTS_Head.UGST.getName());
        }
        txtSgstUgstPer.setText(ourRow.getSgstUgstPer());
        txtSgstUgst.setText(ourRow.getSgstUgst());
        txtSGSTPer.setText(ourRow.getSgstPer());
        txtSGST.setText(ourRow.getSgst());
        txtUGSTPer.setText(ourRow.getUgstPer());
        txtUGST.setText(ourRow.getUgst());
        txtCGSTPer.setText(ourRow.getCgstPer());
        txtCGST.setText(ourRow.getCgst());
        txtIGSTPer.setText(ourRow.getIgstPer());
        txtIGST.setText(ourRow.getIgst());
        txtSubTotal.setText(ourRow.getSubTotal());
        txtFinalDiscountPer.setText(ourRow.getFinalDiscountPer());
        txtFinalDiscount.setText(ourRow.getFinalDiscount());
        txtItemValue.setText(ourRow.getItemValue());

        //set temp value for validate check
        txtSgstUgst.setTag2nd(ourRow.getSgstUgst());
        txtCGST.setTag2nd(ourRow.getCgst());
        txtIGST.setTag2nd(ourRow.getIgst());

        //Other
        txtRemark.setText(ourRow.getRemark());
        v_SNo = ourRow.getvSNo();
        hashCode = ourRow.getSharedKeyFieldHashCode();

        //lab Rejected Qty label conditions
        managelblLabRejectedQty(ourRow.getIsLabRequired(), ourRow.isLabStatus());

        lstImpurity.clear();
        lstImpurity.addAll(ourRow.getLstImpurity());

        lstImpurityTemp.clear();
        for (GrdImpurity_Mdl grdIm : ourRow.getLstImpurity()) {
            GrdImpurity_Mdl grdTemp = new GrdImpurity_Mdl();
            grdTemp.setSharedKeyFieldCode(grdIm.getSharedKeyFieldCode());
            grdTemp.setSharedKeyFieldName(grdIm.getSharedKeyFieldName());
            lstImpurityTemp.add(grdTemp);
        }

        lstLabEntry.clear();
        lstLabEntry.addAll(ourRow.getLstLabEntry());

        lstItemUpdateFields.clear();
        lstItemUpdateFields.addAll(ourRow.getLstItemUpdateFields());
        
        manageDisplay();
        if (grnEntry.departmentEditable) {
            txtHDepartment.setFocus(true);
        } else {
            if (txtHItemName.isEnabled()) {
                txtHItemName.setFocus(true);
            } else {
                txtChallanQty.setFocus(true);
            }
        }
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdGrnDetail_Mdl();
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

    //<editor-fold defaultstate="collapsed" desc="textHelpEvents & gridEvents">
    private void textHelpEvents() {
        txtHItemName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHItemName_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {

                srvAsync.invokeRPC(blnCallBackWait).getTxtHItem(DmShared.xNull(grnEntry.txtHVoucherType.getTag()), !DmShared.xNull(txtHOrderNo.getTag()).equalsIgnoreCase("") ? skuCode : "",
                        !DmShared.xNull(txtHOrderNo.getTag()).equalsIgnoreCase("") ? txtPurchaseUnit.getTag() : "", txtHDepartment.getTag(),
                        helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHItemName_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHItemName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHItemName_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHItemName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHItemName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHItemName_Mdl t1Row) {
                onItemSelection(blnStatus, t1Row, null);
            }
        });

        txtHOrderNo.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHOrderNo_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHOrderNo(grnEntry.txtHSupplier.getTag(),
                        grnEntry.txtChallanDate.getText(),
                        grnEntry.topControl.getMode().equalsIgnoreCase("New") ? "" : grnEntry.topControl.getDocId(),
                        v_SNo,
                        txtHDepartment.getTag(), voucherCategory,
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
                    txtAcceptQty.setTag2nd(DmShared.xNull(t1Row.getPendingQty()));
                    txtAcceptQty.setTag(DmShared.xNull(t1Row.getPendingQty()));
                    txtAcceptQty.setText(DmShared.xNull(t1Row.getPendingQty()));
                    txtSKUQty.setText(DmShared.xNull(t1Row.getSkuQty()));
                    txtRate.setText(DmShared.xNull(t1Row.getRate()));
                    txtAmount.setText(DmShared.xNull(t1Row.getAmount()));
                    txtDiscountPer.setText(DmShared.xNull(t1Row.getDiscountPer()));
                    txtDiscount.setText(DmShared.xNull(t1Row.getDiscount()));

                    cbIsManualGstType.setValue(t1Row.isManualGstType());
                    txtHGstType.setTag(DmShared.xNull(t1Row.getGstTypeCode()));
                    txtHGstType.setText(DmShared.xNull(t1Row.getGstTypeName()));

                    onItemSelection(blnStatus, t1Row.getTxtHItemName_Mdl(), t1Row);
                }
            }
        });

        txtRemark.addKeyDownHandler(new DmRemarkTextBox.TxtRemarkKeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event, Object objSource) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    txtDiscountPer.setFocus(true);
                }
            }
        });

        txtHDepartment.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHDepartment(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDepartment).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDepartment.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDepartment).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (!DmShared.xNull(txtHDepartment.getTag2nd()).equalsIgnoreCase(DmShared.xNull(txtHDepartment.getTag()))) {
                    if (txtHItemName.isEnabled()) {
                        resetDepartment();
                    }
                    txtHDepartment.setTag2nd(DmShared.xNull(txtHDepartment.getTag()));
                }
            }
        });

        txtHGstType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHGstType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHGstType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHGstType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHGstType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (!DmShared.xNull(txtHGstType.getTag2nd()).equalsIgnoreCase(DmShared.xNull(txtHGstType.getTag()))) {
                    resetItemType();
                    txtHGstType.setTag2nd(DmShared.xNull(txtHGstType.getTag()));
                }
                if (blnStatus && t1Row != null) {
                    //getGSTS
                    getGSTS(txtHItemName.getTag(), txtRate.getText(), itsBasedOn, grnEntry.location, txtHGstType.getTag(), cbIsManualGstType.getValue());
                }
            }
        });
    }

    private void onItemSelection(boolean blnStatus, TxtHItemName_Mdl itemMdl, TxtHOrderNo_Mdl orderMdl) {
        if (!DmShared.xNull(selectedItemCode).equalsIgnoreCase(DmShared.xNull(txtHItemName.getTag()))) {
            resetItem();
            selectedItemCode = DmShared.xNull(txtHItemName.getTag());
        }
        if (blnStatus && itemMdl != null) {
            txtHItemName.setText(DmShared.xNull(itemMdl.getSharedKeyFieldName()));
            txtHItemName.setTag(DmShared.xNull(itemMdl.getSharedKeyFieldCode()));
            txtHItemName.setTag2nd(itemMdl.getItemProperty());

            itemGSTNature = DmShared.xNull(itemMdl.getItemGSTNature());
            itsBasedOn = DmShared.iNull(itemMdl.getItsBasedOn()).toString();
            isBestBefore = DmShared.iNull(itemMdl.getIsBestBefore()).toString();

            skuName = DmShared.xNull(itemMdl.getSkuName());
            skuCode = DmShared.xNull(itemMdl.getSkuCode());
            skuAllowDecimalPlaces = DmShared.xNull(itemMdl.getSkuDecimalPlaces());

            txtPurchaseUnit.setText(DmShared.xNull(itemMdl.getPurchaseUnitDisplayName()));
            txtPurchaseUnit.setTag(DmShared.xNull(itemMdl.getPurchaseUnitCode()));
            txtPurchaseUnit.setTag2nd(DmShared.xNull(itemMdl.getPurchaseUnitDecimalPlaces()));
            purchaseUnitName = DmShared.xNull(itemMdl.getPurchaseUnitName());
            purchaseUnitConversionRate = DmShared.xNull(itemMdl.getPurchaseUnitConversionRate());
            isLabRequired = DmShared.xNull(itemMdl.getIsLabRequired());
            masterPurchaseRate=DmShared.xNull(itemMdl.getPurchaseRate());;

            //Set HSN
            txtHSN.setText(DmShared.xNull(itemMdl.getHsn()));
            txtHSN.setTag(DmShared.xNull(itemMdl.getHsnCode()));

            iSubCode = DmShared.xNull(itemMdl.getIsubCode());

            propertyPopup_Mdl = new ItemProperty_Mdl();
            if (orderMdl != null) {
                propertyPopup_Mdl.setItemCode(DmShared.xNull(orderMdl.getItemCode()));
                propertyPopup_Mdl.setItemName(DmShared.xNull(orderMdl.getItemName()));
                propertyPopup_Mdl.setColourCode(DmShared.xNull(orderMdl.getColourCode()));
                propertyPopup_Mdl.setColourName(DmShared.xNull(orderMdl.getColourName()));
                propertyPopup_Mdl.setSizeCode(DmShared.xNull(orderMdl.getSizeCode()));
                propertyPopup_Mdl.setSizeName(DmShared.xNull(orderMdl.getSizeName()));
                propertyPopup_Mdl.setItemMakeCode(DmShared.xNull(orderMdl.getItemMakeCode()));
                propertyPopup_Mdl.setItemMakeName(DmShared.xNull(orderMdl.getItemMakeName()));

                txtDiscountPer.setText(DmShared.xNull(orderMdl.getDiscountPer()));
                txtDiscount.setText(DmShared.xNull(orderMdl.getDiscount()));
            }
            txtProperties.setText(propertyPopup_Mdl.getPropertiesCsv());

            // label lab rejected qty
            managelblLabRejectedQty(itemMdl.getIsLabRequired(), false);

            //fill QC Parameter
            getQCPaparameter(DmShared.xNull(itemMdl.getSharedKeyFieldCode()));

            //getGSTS
            getGSTS(txtHItemName.getTag(), txtRate.getText(), itsBasedOn, grnEntry.location, txtHGstType.getTag(), cbIsManualGstType.getValue());

            calculate();
            showPropertyPopUp(true);
            reQuery();
            manageDisplay();
        }
    }

    private void gridEvents() {
        grdPrevGrn.addCallBack(new DmGridPaging.CallBack<String>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, String param) {
                srvAsync.invokeRPC(blnToEndOfFile).getPrevGrn(
                        txtHItemName.getTag(),
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
            txtShortageQtyUnit.setText(purchaseUnitName);
            txtRejectedQtyUnit.setText(purchaseUnitName);
            txtLabQtyUnit.setText(purchaseUnitName);
            txtQCQtyUnit.setText(purchaseUnitName);

            txtAcceptQtyUnit.setText(purchaseUnitName);
            txtRateUnit.setText(purchaseUnitName);
            txtSkuDifferenceQtyUnit.setText(skuName);
            txtSkuQtyUnit.setText(skuName);
        } else {
            txtChallanQtyUnit.setText("");
            txtReceiveQtyUnit.setText("");
            txtShortageQtyUnit.setText("");
            txtRejectedQtyUnit.setText("");
            txtLabQtyUnit.setText("");
            txtQCQtyUnit.setText("");

            txtAcceptQtyUnit.setText("");
            txtRateUnit.setText("");
            txtSkuDifferenceQtyUnit.setText("");
            txtSkuQtyUnit.setText("");
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Events">
    private void manageEvent() {
        txtChallanQty.addBlurHandler(this);
        txtReceiveQty.addBlurHandler(this);
        txtLabQty.addBlurHandler(this);
        txtQCQty.addBlurHandler(this);
        txtRejectedQty.addBlurHandler(this);
        txtDiscountPer.addBlurHandler(this);
        txtDiscount.addBlurHandler(this);
        txtSgstUgstPer.addBlurHandler(this);
        txtSgstUgst.addBlurHandler(this);
        txtCGST.addBlurHandler(this);
        txtCGSTPer.addBlurHandler(this);
        txtIGSTPer.addBlurHandler(this);
        txtIGST.addBlurHandler(this);

        txtAcceptQty.addBlurHandler(this);
        txtSkuDifferenceQty.addBlurHandler(this);
        txtSKUQty.addBlurHandler(this);
        txtRate.addBlurHandler(this);
        txtLabQty.addKeyUpHandler(this);
        txtQCQty.addKeyUpHandler(this);

        txtProperties.addKeyUpHandler(this);
        txtProperties.addKeyDownHandler(this);
        aProperties.addClickHandler(this);

        aLabRejectedQty.addClickHandler(this);
        aQCRejectedQty.addClickHandler(this);

        cbCurrentSupplier.addClickHandler(this);
        cbOtherSuppliers.addClickHandler(this);
        cbIsManualGstType.addClickHandler(this);
        cbIsManualTax.addClickHandler(this);
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtReceiveQty) {
            calculate();
            onRecievedQtyBlur();
        } else if ((event.getSource() == txtRate)) {
            if (DmShared.iNull(itsBasedOn).intValue() == ItsBasedOn.Rate.toInt()) {
                getGSTS(txtHItemName.getTag(), txtRate.getText(), itsBasedOn, grnEntry.location, txtHGstType.getTag(), cbIsManualGstType.getValue());
            } else {
                calculate();
            }
        } else if (event.getSource() == txtChallanQty) {
            if (!DmShared.vNull(txtChallanQty.getText()).equals(DmShared.vNull(txtChallanQty.getTag()))) {
                txtChallanQty.setTag(txtChallanQty.getText());
                txtReceiveQty.setText(txtChallanQty.getText());
                onRecievedQtyBlur();
            }
            calculate();
        } else if (event.getSource() == txtQCQty
                || event.getSource() == txtLabQty
                || event.getSource() == txtRejectedQty
                || event.getSource() == txtAcceptQty
                || event.getSource() == txtSkuDifferenceQty
                || event.getSource() == txtSKUQty
                || event.getSource() == txtDiscountPer
                || event.getSource() == txtDiscount
                || event.getSource() == txtSgstUgstPer
                || event.getSource() == txtSgstUgst
                || event.getSource() == txtCGSTPer
                || event.getSource() == txtCGST
                || event.getSource() == txtIGSTPer
                || event.getSource() == txtIGST) {
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

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == aLabRejectedQty) {
            showLabImporityPopup();
        } else if (event.getSource() == aQCRejectedQty) {
            showImporityPopup();
        } else if (event.getSource() == aProperties) {
            showPropertyPopUp(false);
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
        } else if (event.getSource() == cbIsManualGstType) {
            manageTaxControls();
            if (!cbIsManualGstType.getValue()) {
                //getGSTS
                getGSTS(txtHItemName.getTag(), txtRate.getText(), itsBasedOn, grnEntry.location, txtHGstType.getTag(), cbIsManualGstType.getValue());
            }
        } else if (event.getSource() == cbIsManualTax) {
            manageTaxControls();
            calculate();
        } else {
            super.onClick(event);
        }
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_SPACE) {
            if (event.getSource() == txtQCQty) {
                showImporityPopup();
            } else if (event.getSource() == txtLabQty) {
                showLabImporityPopup();
            } else if (event.getSource() == txtProperties) {
                showPropertyPopUp(false);
            }
        }
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            if (event.getSource() == txtProperties) {
                if (!DmShared.xNull(txtHItemName.getTag2nd()).trim().equalsIgnoreCase("")) {
                    try {
                        ItemPropertyPopup_Vld.validateProperties(txtHItemName.getTag2nd(), isBestBefore, grnEntry.txtVoucherDate.getText(), propertyPopup_Mdl);
                    } catch (DmException e) {
                        showPropertyPopUp(true);
                    }
                }
            }
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            propertyPopup.hide();
            impurityPopup.hide();
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Popups">
    private void popupCallBacks() {
        propertyPopup.addCallBack(new ItemPropertyPopup.CallBack() {
            @Override
            public void onOk(ItemProperty_Mdl property_Mdl) {
                propertyPopup_Mdl = property_Mdl;
                txtProperties.setText(DmShared.xNull(property_Mdl.getPropertiesCsv()));
                txtProperties.setFocus(true);
            }

            @Override
            public void onClose() {
                txtProperties.setFocus(true);
            }
        });

        impurityPopup.addCallBack(new ImpurityPopup.CallBack() {
            @Override
            public void onOk(List<GrdImpurity_Mdl> lstImp) {
                double rejQty = 0;
                lstImpurityTemp.clear();
                for (GrdImpurity_Mdl grdIm : lstImp) {
                    rejQty += DmShared.vNull(grdIm.getNetQty());

                    GrdImpurity_Mdl grdTemp = new GrdImpurity_Mdl();
                    grdTemp.setSharedKeyFieldCode(grdIm.getSharedKeyFieldCode());
                    grdTemp.setSharedKeyFieldName(grdIm.getSharedKeyFieldName());
                    lstImpurityTemp.add(grdTemp);
                }
                txtQCQty.setText(DmShared.vNull(rejQty).toString());
                lstImpurity.clear();
                lstImpurity.addAll(lstImp);
                txtQCQty.setFocus(true);
                calculate();
                manageDisplay();
            }

            @Override
            public void onClose() {
                txtQCQty.setFocus(true);
            }
        });

        labImpurityPopup.addCallBack(new LabImpurityPopup.CallBack() {
            @Override
            public void onOk(List<GrdLabEntry_Mdl> lstImp) {
                double labQty = 0, labAmt = 0;
                for (GrdLabEntry_Mdl grdIm : lstImp) {
                    labQty += DmShared.vNull(grdIm.getLabQty());
                    labAmt += DmShared.vNull(grdIm.getLabAmt());
                }
                txtLabQty.setText(DmShared.vNull(labQty).toString());
                txtDrCrNoteAmt.setText(DmShared.vNull(labAmt).toString());
                lstLabEntry.clear();
                lstLabEntry.addAll(lstImp);
                txtLabQty.setFocus(true);
                calculate();
                manageDisplay();
            }

            @Override
            public void onClose() {
                txtLabQty.setFocus(true);
            }
        });
    }

    private void showPropertyPopUp(boolean isDefaultShow) {
        String property = DmShared.xNull(txtHItemName.getTag2nd());
        if (!DmShared.xNull(txtHItemName.getTag()).equalsIgnoreCase("")
                && !DmShared.xNull(property).equalsIgnoreCase("")) {
            txtProperties.setEnabled(true);
            propertyPopup_Mdl.setItemCode(txtHItemName.getTag());
            propertyPopup_Mdl.setSkuName(skuName);
            propertyPopup_Mdl.setSkuQtyForRate(purchaseUnitConversionRate);
            propertyPopup.show(grnEntry.topControl.getMode(), property, isBestBefore, propertyPopup_Mdl, grnEntry.txtVoucherDate.getText(), false,false,STEnum.ItemBatchType.NON_EXPIRED_BATCH);
        } else {
            txtProperties.setEnabled(false);
            if (!isDefaultShow) {
                new DmAlertMessageBox("Properties not applicable for this item.", txtProperties).show();
            }
        }
    }

    private void showImporityPopup() {
        impurityPopup.show(this);
    }

    private void showLabImporityPopup() {
        if (canShowLabPopup) {
            labImpurityPopup.show(this);
        }
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

        cbIsManualGstType.setValue(false);
        cbIsManualTax.setValue(false);

        txtRemark.setText("");
        txtRemark.setTag("");
        txtRemark.setTag2nd("");

        txtHDepartment.setText("");
        txtHDepartment.setTag("");
        txtHDepartment.setTag2nd("");
        resetDepartment();

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

        txtHItemName.setText("");
        txtHItemName.setTag("");
        txtHItemName.setTag2nd("");
        selectedItemCode = "";
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

        txtShortageQty.setTag("");
        txtShortageQty.setText("");
        txtShortageQty.setTag2nd("");
        txtShortageQtyUnit.setTag("");
        txtShortageQtyUnit.setText("");
        txtShortageQtyUnit.setTag2nd("");

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

        resetLab();
    }

    private void resetDepartment() {
        selectedItemCode = "";
        txtHItemName.setText("");
        txtHItemName.setTag("");
        txtHItemName.setTag2nd("");

        resetItem();
    }

    private void resetItem() {
        iSubCode = "";
        ibCode = "";
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

        txtProperties.setText("");
        txtProperties.setTag("");
        txtProperties.setTag2nd("");

        txtHSN.setText("");
        txtHSN.setTag("");
        txtHSN.setTag2nd("");

        grdPrevGrn.getDmGrid().getGrid().getStore().clear();
        cbCurrentSupplier.setValue(false);
        cbOtherSuppliers.setValue(false);

        resetItemType();
    }

    private void resetRecievedQty() {
        txtQCQtyUnit.setText("");
        txtQCQtyUnit.setTag("");
        txtQCQtyUnit.setTag2nd("");

        txtQCQty.setText("");
        txtQCQty.setTag("");
        txtQCQty.setTag2nd("");

        lstImpurity.clear();
        lstImpurity.addAll(lstImpurityTemp);
    }

    private void resetLab() {
        txtLabQtyUnit.setText("");
        txtLabQtyUnit.setTag("");
        txtLabQtyUnit.setTag2nd("");

        txtLabQty.setText("");
        txtLabQty.setTag("");
        txtLabQty.setTag2nd("");

        txtDrCrNoteAmt.setText("");
        txtDrCrNoteAmt.setTag("");
        txtDrCrNoteAmt.setTag2nd("");
    }

    private void resetItemType() {
        txtHGstType.setTag2nd("");

        txtAmount.setText("");
        txtAmount.setTag("");
        txtAmount.setTag2nd("");

        txtItemValue.setText("");
        txtItemValue.setTag("");
        txtItemValue.setTag2nd("");

        resetTaxDetail();
    }

    private void resetTaxDetail() {
        //SGST or UGST
        txtSgstUgstPer.setText("");
        txtSgstUgstPer.setTag("");
        txtSgstUgstPer.setTag2nd("");

        txtSgstUgst.setText("");
        txtSgstUgst.setTag("");
        txtSgstUgst.setTag2nd("");

        //SGST
        txtSGSTPer.setText("");
        txtSGSTPer.setTag("");
        txtSGSTPer.setTag2nd("");

        txtSGST.setText("");
        txtSGST.setTag("");
        txtSGST.setTag2nd("");

        //UGST
        txtUGSTPer.setText("");
        txtUGSTPer.setTag("");
        txtUGSTPer.setTag2nd("");

        txtUGST.setText("");
        txtUGST.setTag("");
        txtUGST.setTag2nd("");

        //CGST
        txtCGSTPer.setText("");
        txtCGSTPer.setTag("");
        txtCGSTPer.setTag2nd("");

        txtCGST.setText("");
        txtCGST.setTag("");
        txtCGST.setTag2nd("");

        //IGST
        txtIGSTPer.setText("");
        txtIGSTPer.setTag("");
        txtIGSTPer.setTag2nd("");

        txtIGST.setText("");
        txtIGST.setTag("");
        txtIGST.setTag2nd("");
    }
    //</editor-fold>

    private void setGSTS(List<GSTS_Mdl> lstITS) {
        String taxPer = "";
        resetTaxDetail();
        if (lstITS != null && lstITS.size() > 0) {
            for (GSTS_Mdl iTS_Mdl : lstITS) {
                taxPer = "";

                taxPer = DmClient.vNull(iTS_Mdl.getAtRate(), "0.00");

                if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(GSTS_Head.SGST.getShortName()) && !(DmShared.iNull(grnEntry.defaultValue.getIsSiteStateUT()).intValue() == 1)) {
                    lblSgstUgst.setText(iTS_Mdl.getGstsHeadName());
                    txtSgstUgstPer.setText(taxPer);
                    txtSGSTPer.setText(taxPer);

                } else if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(GSTS_Head.UGST.getShortName()) && (DmShared.iNull(grnEntry.defaultValue.getIsSiteStateUT()).intValue() == 1)) {
                    lblSgstUgst.setText(iTS_Mdl.getGstsHeadName());
                    txtSgstUgstPer.setText(taxPer);
                    txtUGSTPer.setText(taxPer);

                } else if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(GSTS_Head.CGST.getShortName())) {
                    txtCGSTPer.setText(taxPer);
                } else if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(GSTS_Head.IGST.getShortName())) {
                    txtIGSTPer.setText(taxPer);
                }
            }
        }
        calculate();
    }

    private void managelblLabRejectedQty(String isLabRequired, boolean flagLabStatus) {
        //lab Rejected Qty label conditions
        if (flagLabStatus) {
            canShowLabPopup = true;
            aLabRejectedQty.getElement().getStyle().setColor("blue");
        } else {
            if (DmShared.iNull(isLabRequired).intValue() == STEnum.LabRequired.YES.getId()) {
                canShowLabPopup = true;
                aLabRejectedQty.getElement().getStyle().setColor("red");
            } else if (DmShared.iNull(isLabRequired).intValue() == STEnum.LabRequired.MAY_BE.getId()) {
                canShowLabPopup = true;
                aLabRejectedQty.getElement().getStyle().setColor("darkgreen");
            } else {
                canShowLabPopup = false;
                aLabRejectedQty.getElement().getStyle().setColor("black");
            }
        }

        if (canShowLabPopup) {
            aLabRejectedQty.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
            aLabRejectedQty.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        } else {
            aLabRejectedQty.getElement().getStyle().setTextDecoration(Style.TextDecoration.NONE);
            aLabRejectedQty.getElement().getStyle().setCursor(Style.Cursor.AUTO);
        }
    }

    private void showItemUpdatePopup() {
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
        lstItemUpdateFields.add(new ItemUpdateField_Mdl("Purchase Rate", ItemUpdateField_Mdl.Field.PURCHASE_RATE, masterPurchaseRate, txtRate.getText(), "1", "0"));

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
    
}
