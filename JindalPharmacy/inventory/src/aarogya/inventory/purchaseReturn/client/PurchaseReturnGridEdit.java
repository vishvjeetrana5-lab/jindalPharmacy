package aarogya.inventory.purchaseReturn.client;

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
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmToolkit.gsts.shared.GSTS_Mdl;
import aarogya.inventory.purchaseReturn.shared.GrdItemDetail_Mdl;
import aarogya.inventory.purchaseReturn.shared.PurchaseReturn_Srv;
import aarogya.inventory.purchaseReturn.shared.PurchaseReturn_SrvAsync;
import aarogya.inventory.purchaseReturn.shared.TxtHItemName_Mdl;
import aarogya.selfToolkit.itemPropertyPopup.client.ItemPropertyPopup;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemPropertyPopup_Vld;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import aarogya.selfToolkit.stStatic.shared.STEnum;

public class PurchaseReturnGridEdit<T1, T2> extends PurchaseReturnGridEdit_Dsg<T1, T2> implements BlurHandler, ClickHandler, KeyUpHandler, KeyDownHandler {

    private ServiceInvoker<PurchaseReturn_SrvAsync> srvAsync;
    private int hashCode = 0;
    private final ItemPropertyPopup propertyPopup = new ItemPropertyPopup();
    private ItemProperty_Mdl propertyPopup_Mdl;
    // private OverHeadLine overHeadLine; <TS>
    private PurchaseReturn pReturnEntry;
    String purchaseUnitConversionRate = "", skuName = "", skuQty = "", isBestBefore = "", v_SNo = "", selectedItemCode = "";
    String itemGSTNature = "", itsBasedOn = "", hsnCode = "", skuCode = "", skuAllowDecimalPlaces = "",
            purchaseUnitName = "", skuRate = "", gstUnitCode = "", itemMastPurchaseReturnAc = "",
            pbDocId = "", pbRecId = "", pbV_Type = "", pbV_Date = "", pbV_SNo = "", pbNetAmount = "", ibCode = "";
    boolean isFirstShow = true;

    public PurchaseReturnGridEdit(DmGrid<T1, T2> grid, PurchaseReturn pReturn) {
        super(grid, "Item Detail", "760px", "250px");
        srvAsync = new ServiceInvoker<PurchaseReturn_SrvAsync>(GWT.create(PurchaseReturn_Srv.class), PurchaseReturn.class.getName());
        this.pReturnEntry = pReturn;
        textHelpEvents();
        popupCallBacks();
        manageEvent();
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {
        resetPopup();

        super.showCenter(currentRowIndex, okNeeded, newNeeded, deleteNeeded);
        flwOHL.clear();
        if (isFirstShow) {
            isFirstShow = false;
        }
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                if (pReturnEntry.departmentEditable) {
                    txtHDepartment.setFocus(true);
                } else {
                    if (txtHItemName.isEnabled()) {
                        txtHItemName.setFocus(true);
                    } else {
                        txtQuantity.setFocus(true);
                    }
                }
            }
        });
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

    private void setGSTS(List<GSTS_Mdl> lstITS) {
        String taxPer = "", taxAcCode = "", rcmAcCode = "";
        resetTaxDetail();
        if (lstITS != null && lstITS.size() > 0) {

            //Get Purchase A/c From GSTS
            if (!DmShared.xNull(pReturnEntry.defaultValue.getPurchaseAcPostingBasedOn()).trim().equalsIgnoreCase("")) {
                if (STEnum.PurchaseSalesAcPostingBasedOn.getViaKey(DmShared.iNull(pReturnEntry.defaultValue.getPurchaseAcPostingBasedOn()).intValue()).getId() == STEnum.PurchaseSalesAcPostingBasedOn.GSTS.getId()) {
                    for (GSTS_Mdl iTS_Mdl : lstITS) {
                        if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.PURCHASE_RETURN_ACCOUNT.getShortName())) {
                            txtTaxableAmt.setTag(iTS_Mdl.getPurchaseSalesAcCode());
                            break;
                        }
                    }
                }
            }

            for (GSTS_Mdl iTS_Mdl : lstITS) {
                taxPer = "";
                taxAcCode = "";
                rcmAcCode = "";

                if (pReturnEntry.defaultValue.isIncludeTaxInCost()) {
                    if (DmShared.iNull(pReturnEntry.txtHRCMApplicable.getTag()).intValue() == 0) {
                        taxPer = DmClient.vNull(iTS_Mdl.getAtRate(), "0.00");
                        if (!pReturnEntry.defaultValue.isInventoryAccountingActive()) {
                            taxAcCode = DmShared.xNull(txtTaxableAmt.getTag());
                        }
                    }
                } else {
                    taxPer = DmClient.vNull(iTS_Mdl.getAtRate(), "0.00");
                    rcmAcCode = DmShared.xNull(iTS_Mdl.getRcmAcCode());
                    taxAcCode = DmShared.xNull(iTS_Mdl.getInputTaxAcCode());
                }

                if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.SGST.getShortName()) && !(DmShared.iNull(pReturnEntry.defaultValue.getIsSiteStateUT()).intValue() == 1)) {
                    lblSgstUgst.setText(iTS_Mdl.getGstsHeadName());
                    txtSgstUgstPer.setText(taxPer);
                    txtSGSTPer.setText(taxPer);
                    txtSGSTPer.setTag(rcmAcCode);
                    txtSGST.setTag(taxAcCode);

                } else if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.UGST.getShortName()) && (DmShared.iNull(pReturnEntry.defaultValue.getIsSiteStateUT()).intValue() == 1)) {
                    lblSgstUgst.setText(iTS_Mdl.getGstsHeadName());
                    txtSgstUgstPer.setText(taxPer);
                    txtUGSTPer.setText(taxPer);
                    txtUGSTPer.setTag(rcmAcCode);
                    txtUGST.setTag(taxAcCode);

                } else if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.CGST.getShortName())) {
                    txtCGSTPer.setText(taxPer);
                    txtCGSTPer.setTag(rcmAcCode);
                    txtCGST.setTag(taxAcCode);
                } else if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.IGST.getShortName())) {
                    txtIGSTPer.setText(taxPer);
                    txtIGSTPer.setTag(rcmAcCode);
                    txtIGST.setTag(taxAcCode);
                }
            }
        }
        calculate();
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

    private void manageDisplay() {
        if (!pReturnEntry.departmentEditable) {
            txtHDepartment.setEnabled(false);
        }
        txtHItemName.setEnabled(DmShared.xNull(pReturnEntry.txtHBillNo.getTag()).trim().equals(""));
        txtPurchaseUnit.setEnabled(false);
        txtGrnNo.setEnabled(false);
        txtOrderNo.setEnabled(false);
        txtOrderRefNo.setEnabled(false);
        txtHGstType.setEnabled(false);
        txtAmount.setEnabled(false);
        if (!DmShared.xNull(txtHItemName.getTag()).equalsIgnoreCase("")
                && DmShared.xNull(txtHItemName.getTag2nd()).equalsIgnoreCase("")) {
            txtProperties.setEnabled(false);
        } else {
            txtProperties.setEnabled(true);
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

    protected void calculate() {
        txtQuantity.setText(DmClient.vNull(txtQuantity.getText(), DmShared.getNumberFormat(DmShared.iNull(txtPurchaseUnit.getTag2nd()).intValue())));

        double purchaseUnitConvRate = DmShared.vNull(this.purchaseUnitConversionRate);
        double quantity = DmShared.vNull(txtQuantity.getText());
        double rate = DmShared.vNull(txtRate.getText());
        double amount = quantity * rate;

        txtSGSTPer.setText("");
        txtSGST.setText("");
        txtUGSTPer.setText("");
        txtUGST.setText("");

        skuQty = DmClient.vNull((quantity * purchaseUnitConvRate), "0.0000000");
        txtAmount.setText(DmClient.vNull(amount, "0.00"));
        skuRate = DmClient.vNull(DmShared.vNull(txtAmount.getText()) / DmShared.vNull(skuQty), "0.0000000");

        if (DmShared.vNull(txtDiscountPer.getText()).doubleValue() > 0) {
            txtDiscount.setText(DmClient.vNull(DmShared.vNull(txtAmount.getText()).doubleValue() * DmShared.vNull(txtDiscountPer.getText()).doubleValue() * 0.01, "0.00"));
        }

        double taxableAmt = DmShared.vNull(txtAmount.getText()).doubleValue() - DmShared.vNull(txtDiscount.getText()).doubleValue();
        txtTaxableAmt.setText(DmClient.vNull(taxableAmt, "0.00"));
        txtSgstUgst.setTag2nd(DmClient.vNull(taxableAmt * DmShared.vNull(txtSgstUgstPer.getText()).doubleValue() * 0.01, "0.00"));
        if (!cbIsManualTax.getValue()) {
            txtSgstUgst.setText(txtSgstUgst.getTag2nd());
        }
        if (!(DmShared.iNull(pReturnEntry.defaultValue.getIsSiteStateUT()).intValue() == 1)) {
            txtSGSTPer.setText(txtSgstUgstPer.getText());
            txtSGST.setText(txtSgstUgst.getText());
        } else if ((DmShared.iNull(pReturnEntry.defaultValue.getIsSiteStateUT()).intValue() == 1)) {
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
        if (DmShared.iNull(pReturnEntry.txtHRCMApplicable.getTag()).intValue() == 1) {
            subTotal = taxableAmt;
        } else {
            subTotal = taxableAmt + DmShared.vNull(txtSgstUgst.getText()).doubleValue() + DmShared.vNull(txtCGST.getText()).doubleValue() + DmShared.vNull(txtIGST.getText()).doubleValue();
        }
        txtSubTotal.setText(DmClient.vNull(subTotal, "0.00"));
        if (DmShared.vNull(txtFinalDiscountPer.getText()) > 0) {
            txtFinalDiscount.setText(DmClient.vNull(taxableAmt * DmShared.vNull(txtFinalDiscountPer.getText()) * 0.01, "0.00"));
        }
        txtItemValue.setText(DmClient.vNull(subTotal - DmShared.vNull(txtFinalDiscount.getText()).doubleValue(), "0.00"));

        manageDisplay();
    }

    //<editor-fold defaultstate="collapsed" desc="GridEdit Overrides">
    @Override
    public void onOk() {
        try {
            calculate();
            if (DmShared.iNull(txtHDepartment.getTag()).longValue() == 0) {
                new DmAlertMessageBox("Department is mandatory.", txtHDepartment).show();
                return;
            }
            if (DmShared.xNull(txtHItemName.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Item Name is mandatory.", txtHItemName).show();
                return;
            }
            if (DmShared.iNull(txtPurchaseUnit.getTag()).longValue() == 0) {
                new DmAlertMessageBox("Unit is mandatory.", txtPurchaseUnit).show();
                return;
            }
            if (!DmShared.xNull(txtHItemName.getTag2nd()).trim().equalsIgnoreCase("")) {
                try {
                    ItemPropertyPopup_Vld.validateProperties(txtHItemName.getTag2nd(), isBestBefore, pReturnEntry.txtVoucherDate.getText(), propertyPopup_Mdl);
                } catch (DmException e) {
                    new DmAlertMessageBox("Properties : " + e.getMessage(), txtProperties).show();
                    return;
                }
            }
            if (DmShared.vNull(txtQuantity.getText()) <= 0) {
                new DmAlertMessageBox("Quantity is mandatory.", txtQuantity).show();
                return;
            }
            if (DmShared.vNull(txtQuantity.getTag2nd()) > 0) {
                if (DmShared.vNull(txtQuantity.getText()) > DmShared.vNull(txtQuantity.getTag2nd())) {
                    new DmAlertMessageBox("Quantity should be less than or equal to : " + DmShared.vNull(txtQuantity.getTag2nd()), txtQuantity).show();
                    return;
                }
            }
            if (DmShared.vNull(skuQty) <= 0) {
                new DmAlertMessageBox("SKU Qty. is mandatory.", txtQuantity).show();
                return;
            }
            if (DmShared.vNull(txtRate.getText()) == 0) {
                new DmAlertMessageBox("Rate is mandatory.", txtRate).show();
                return;
            }
            if (DmShared.vNull(propertyPopup_Mdl.getMrp()) > 0) {
                if (DmShared.vNull(txtRate.getText()) > DmShared.vNull(propertyPopup_Mdl.getMrp())) {
                    new DmAlertMessageBox("Rate should be less than or equal to MRP.", txtRate).show();
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

            if (pReturnEntry.defaultValue.isInventoryAccountingActive()) {
                if (DmShared.iNull(txtTaxableAmt.getTag()).longValue() == 0) {
                    new DmAlertMessageBox("Inventory a/c is required.", txtHItemName).show();
                    return;
                }
            } else {
                if (DmShared.iNull(txtTaxableAmt.getTag()).longValue() == 0) {
                    new DmAlertMessageBox("Purchase Return a/c is required.", txtHItemName).show();
                    return;
                }
            }

            if (!pReturnEntry.defaultValue.isIncludeTaxInCost()) {
                if (DmShared.vNull(txtSGST.getText()).doubleValue() != 0) {
                    if (DmShared.iNull(txtSGST.getTag()).longValue() == 0) {
                        new DmAlertMessageBox("SGST a/c is required.", txtHGstType).show();
                        return;
                    }
                    if (DmShared.iNull(pReturnEntry.txtHRCMApplicable.getTag()).intValue() == 1) {
                        if (DmShared.iNull(txtSGSTPer.getTag()).longValue() == 0) {
                            new DmAlertMessageBox("SGST-RCM a/c is required.", txtHGstType).show();
                            return;
                        }
                    }
                }
                if (DmShared.vNull(txtUGST.getText()).doubleValue() != 0) {
                    if (DmShared.iNull(txtUGST.getTag()).longValue() == 0) {
                        new DmAlertMessageBox("UGST a/c is required.", txtHGstType).show();
                        return;
                    }
                    if (DmShared.iNull(pReturnEntry.txtHRCMApplicable.getTag()).intValue() == 1) {
                        if (DmShared.iNull(txtUGSTPer.getTag()).longValue() == 0) {
                            new DmAlertMessageBox("UGST-RCM a/c is required.", txtHGstType).show();
                            return;
                        }
                    }
                }
                if (DmShared.vNull(txtCGST.getText()).doubleValue() != 0) {
                    if (DmShared.iNull(txtCGST.getTag()).longValue() == 0) {
                        new DmAlertMessageBox("CGST a/c is required.", txtHGstType).show();
                        return;
                    }
                    if (DmShared.iNull(pReturnEntry.txtHRCMApplicable.getTag()).intValue() == 1) {
                        if (DmShared.iNull(txtCGSTPer.getTag()).longValue() == 0) {
                            new DmAlertMessageBox("CGST-RCM a/c is required.", txtHGstType).show();
                            return;
                        }
                    }
                }
                if (DmShared.vNull(txtIGST.getText()).doubleValue() != 0) {
                    if (DmShared.iNull(txtIGST.getTag()).longValue() == 0) {
                        new DmAlertMessageBox("IGST a/c is required.", txtHGstType).show();
                        return;
                    }
                    if (DmShared.iNull(pReturnEntry.txtHRCMApplicable.getTag()).intValue() == 1) {
                        if (DmShared.iNull(txtIGSTPer.getTag()).longValue() == 0) {
                            new DmAlertMessageBox("IGST-RCM a/c is required.", txtHGstType).show();
                            return;
                        }
                    }
                }
            }

            /*==============================<< Check Item dublicacy with Batch No and Order No. >>====================================*/
            for (GrdItemDetail_Mdl detail_Mdl : (List<GrdItemDetail_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
                if (DmShared.xNull(detail_Mdl.getDepartmentCode()).equalsIgnoreCase(DmShared.xNull(txtHDepartment.getTag()))
                        && DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(txtHItemName.getTag()))
                        && DmShared.xNull(detail_Mdl.getBatchNo()).equalsIgnoreCase(DmShared.xNull(propertyPopup_Mdl.getBatchNo()))
                        && DmShared.xNull(detail_Mdl.getLotNo()).equalsIgnoreCase(DmShared.xNull(propertyPopup_Mdl.getLotNo()))
                        && DmShared.xNull(detail_Mdl.getColourCode()).equalsIgnoreCase(DmShared.xNull(propertyPopup_Mdl.getColourCode()))
                        && DmShared.xNull(detail_Mdl.getSizeCode()).equalsIgnoreCase(DmShared.xNull(propertyPopup_Mdl.getSizeCode()))
                        && DmShared.xNull(detail_Mdl.getItemMakeCode()).equalsIgnoreCase(DmShared.xNull(propertyPopup_Mdl.getItemMakeCode()))
                        && DmShared.vNull(detail_Mdl.getMrp()).doubleValue() == DmShared.vNull(propertyPopup_Mdl.getMrp()).doubleValue()
                        && DmShared.dNull(detail_Mdl.getManufacturingDate()).equalsIgnoreCase(DmShared.dNull(propertyPopup_Mdl.getManufacturingDate()))
                        && DmShared.dNull(detail_Mdl.getExpiryDate()).equalsIgnoreCase(DmShared.dNull(propertyPopup_Mdl.getExpiryDate()))
                        && DmShared.xNull(detail_Mdl.getPurchaseUnitCode()).equalsIgnoreCase(DmShared.xNull(txtPurchaseUnit.getTag()))
                        && DmShared.xNull(detail_Mdl.getPbDocId()).equalsIgnoreCase(DmShared.xNull(pbDocId))
                        && DmShared.xNull(detail_Mdl.getPbV_SNo()).equalsIgnoreCase(DmShared.xNull(pbV_SNo))) {
                    if (!DmClient.vNull(detail_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                        String message = "<div style='float:right; margin-right: 5px;'>";
                        message += "Record already exist with following details - <br>";
                        message += (DmShared.xNull(txtHDepartment.getText()).trim().equals("")) ? "" : "<b>Department:</b> " + DmShared.xNull(txtHDepartment.getText()) + "<br>";
                        message += (DmShared.xNull(txtHItemName.getText()).trim().equals("")) ? "" : "<b>Item:</b> " + DmShared.xNull(txtHItemName.getText()) + "<br>";
                        message += (DmShared.xNull(txtPurchaseUnit.getText()).trim().equals("")) ? "" : "<b>Unit:</b> " + DmShared.xNull(txtPurchaseUnit.getText()) + "<br>";
                        message += (DmShared.xNull(propertyPopup_Mdl.getBatchNo()).trim().equals("")) ? "" : "<b>Batch No.:</b> " + DmShared.xNull(propertyPopup_Mdl.getBatchNo()) + "<br>";
                        message += (DmShared.xNull(propertyPopup_Mdl.getLotNo()).trim().equals("")) ? "" : "<b>Lot No.:</b> " + DmShared.xNull(propertyPopup_Mdl.getLotNo()) + "<br>";
                        message += (DmShared.xNull(propertyPopup_Mdl.getColourName()).trim().equals("")) ? "" : "<b>Colour:</b> " + DmShared.xNull(propertyPopup_Mdl.getColourName()) + "<br>";
                        message += (DmShared.xNull(propertyPopup_Mdl.getSizeName()).trim().equals("")) ? "" : "<b>Size:</b> " + DmShared.xNull(propertyPopup_Mdl.getSizeName()) + "<br>";
                        message += (DmShared.xNull(propertyPopup_Mdl.getItemMakeName()).trim().equals("")) ? "" : "<b>Item Make:</b> " + DmShared.xNull(propertyPopup_Mdl.getItemMakeName()) + "<br>";
                        message += (DmShared.vNull(propertyPopup_Mdl.getMrp()) == 0) ? "" : "<b>MRP:</b> " + DmClient.vNull(propertyPopup_Mdl.getMrp(), "0.00") + "<br>";
                        message += (DmShared.dNull(propertyPopup_Mdl.getManufacturingDate()).trim().equals("")) ? "" : "<b>Manufacturing Date:</b> " + DmShared.dNull(propertyPopup_Mdl.getManufacturingDate()) + "<br>";
                        message += (DmShared.dNull(propertyPopup_Mdl.getExpiryDate()).trim().equals("")) ? "" : "<b>Expiry Date:</b> " + DmShared.dNull(propertyPopup_Mdl.getExpiryDate()) + "<br>";
                        message += "</div>";
                        new DmAlertMessageBox(message, txtHItemName).show();
                        return;
                    }
                }
            }
            proceedOk();
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtHItemName).show();
        }
    }

    @Override
    public void manageOk(T1 row) {
        GrdItemDetail_Mdl ourRow = (GrdItemDetail_Mdl) row;
        fillRowData(ourRow);
        callBack.onOk(ourRow);
    }

    protected void fillRowData(GrdItemDetail_Mdl ourRow) {
        //Department
        ourRow.setDepartmentCode(DmShared.xNull(txtHDepartment.getTag()));
        ourRow.setDepartmentName(DmShared.xNull(txtHDepartment.getText()));

        //Item
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHItemName.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHItemName.getText()));

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

        //Bill Detail
        ourRow.setPbDocId(DmShared.xNull(pbDocId));
        ourRow.setPbRecId(DmShared.xNull(pbRecId));
        ourRow.setPbV_Type(DmShared.xNull(pbV_Type));
        ourRow.setPbV_Date(DmShared.xNull(pbV_Date));
        ourRow.setPbV_SNo(DmShared.xNull(pbV_SNo));
        ourRow.setPbNetAmount(DmShared.xNull(pbNetAmount));

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

        //Quantities
        ourRow.setQuantity(DmShared.xNull(txtQuantity.getText()));
        ourRow.setPendingQty(DmShared.xNull(txtQuantity.getTag2nd()));
        ourRow.setSkuQty(DmShared.xNull(skuQty));

        //Rate
        ourRow.setRate(DmClient.vNull(txtRate.getText(), "0.00"));
        ourRow.setAmount(DmClient.vNull(txtAmount.getText(), "0.00"));
        ourRow.setSkuRate(DmShared.xNull(skuRate));

        //Amount
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

        //Properties
        ourRow.setMrp(DmClient.vNull(propertyPopup_Mdl.getMrp(), "0.00"));
        ourRow.setSkuMrp(DmClient.vNull(propertyPopup_Mdl.getSkuMrp(), "0.0000000"));
        ourRow.setBatchNo(DmShared.xNull(propertyPopup_Mdl.getBatchNo()));
        ourRow.setLotNo(DmShared.xNull(propertyPopup_Mdl.getLotNo()));
        ourRow.setExpiryDate(DmShared.dNull(propertyPopup_Mdl.getExpiryDate()));
        ourRow.setManufacturingDate(DmShared.dNull(propertyPopup_Mdl.getManufacturingDate()));
        ourRow.setColourCode(DmShared.xNull(propertyPopup_Mdl.getColourCode()));
        ourRow.setColourName(DmShared.xNull(propertyPopup_Mdl.getColourName()));
        ourRow.setSizeCode(DmShared.xNull(propertyPopup_Mdl.getSizeCode()));
        ourRow.setSizeName(DmShared.xNull(propertyPopup_Mdl.getSizeName()));
        ourRow.setItemMakeCode(DmShared.xNull(propertyPopup_Mdl.getItemMakeCode()));
        ourRow.setItemMakeName(DmShared.xNull(propertyPopup_Mdl.getItemMakeName()));
        ourRow.setPropertiesCsv(DmShared.xNull(propertyPopup_Mdl.getPropertiesCsv()));

        //IAP
        ourRow.setItemMastPurchaseReturnAc(DmShared.xNull(itemMastPurchaseReturnAc));
        ourRow.setIsubCode(DmShared.xNull(txtTaxableAmt.getTag()));

        //Accounts
        ourRow.setSgstAcCode(DmShared.xNull(txtSGST.getTag()));
        ourRow.setSgstRcmAcCode(DmShared.xNull(txtSGSTPer.getTag()));

        ourRow.setUgstAcCode(DmShared.xNull(txtUGST.getTag()));
        ourRow.setUgstRcmAcCode(DmShared.xNull(txtUGSTPer.getTag()));

        ourRow.setCgstAcCode(DmShared.xNull(txtCGST.getTag()));
        ourRow.setCgstRcmAcCode(DmShared.xNull(txtCGSTPer.getTag()));

        ourRow.setIgstAcCode(DmShared.xNull(txtIGST.getTag()));
        ourRow.setIgstRcmAcCode(DmShared.xNull(txtIGSTPer.getTag()));

        //Other
        ourRow.setRemark(DmShared.xNull(txtRemark.getText()).trim());
        ourRow.setV_SNo(DmShared.xNull(v_SNo));
        ourRow.setIbCode(DmShared.xNull(ibCode));
    }

    @Override
    public void displayRow(T1 row) {
        GrdItemDetail_Mdl ourRow = (GrdItemDetail_Mdl) row;
        GrdItemDetail_Mdl previousRow = null;

        //Clear
        resetPopup();

        //Department
        if (DmShared.iNull(ourRow.getSharedKeyFieldCode()).longValue() == 0) {
            if (this.getCurrentRowIndex() > 0) {
                if (this.getCurrentRowIndex() == (pReturnEntry.grdItemDetail.getGrid().getStore().size() - 1)) {
                    previousRow = pReturnEntry.grdItemDetail.getGrid().getStore().get(this.getCurrentRowIndex() - 1);
                }
            }
        }
        if (previousRow != null) {
            txtHDepartment.setTag(DmShared.xNull(previousRow.getDepartmentCode()));
            txtHDepartment.setText(DmShared.xNull(previousRow.getDepartmentName()));
        } else {
            if (DmShared.iNull(ourRow.getSharedKeyFieldCode()).longValue() == 0) {
                if (DmShared.iNull(pReturnEntry.defaultValue.getDefaultPurchaseDepartment()).intValue() != 0) {
                    txtHDepartment.setTag(pReturnEntry.defaultValue.getDefaultPurchaseDepartment());
                    txtHDepartment.setText(pReturnEntry.defaultValue.getDefaultPurchaseDepartmentText());
                } else {
                    txtHDepartment.setTag(pReturnEntry.defaultValue.getDepartmentCode());
                    txtHDepartment.setText(pReturnEntry.defaultValue.getDepartmentName());
                }
            } else {
                txtHDepartment.setTag(ourRow.getDepartmentCode());
                txtHDepartment.setText(ourRow.getDepartmentName());
            }
        }
        txtHDepartment.setTag2nd(DmShared.xNull(txtHDepartment.getTag()));

        //Item
        selectedItemCode = ourRow.getSharedKeyFieldCode();
        txtHItemName.setTag(ourRow.getSharedKeyFieldCode());
        txtHItemName.setText(ourRow.getSharedKeyFieldName());

        //Item Details
        txtHItemName.setTag2nd(ourRow.getPropertyList());
        itemGSTNature = ourRow.getItemGSTNature();
        itsBasedOn = ourRow.getItsBasedOn();
        cbIsManualTax.setValue(ourRow.isManualTax());
        cbIsManualGstType.setValue(ourRow.isManualGstType());
        txtHGstType.setText(DmShared.xNull(ourRow.getGstTypeName()));
        txtHGstType.setTag(DmShared.xNull(ourRow.getGstTypeCode()));
        txtHGstType.setTag2nd(DmShared.xNull(ourRow.getGstTypeCode()));
        txtHSN.setText(DmShared.xNull(ourRow.getHsn()));
        txtHSN.setTag(DmShared.xNull(ourRow.getHsnCode()));
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
        skuQty = ourRow.getSkuQty();

        //Rate
        txtRate.setText(DmClient.vNull(ourRow.getRate(), "0.00"));
        txtAmount.setText(DmClient.vNull(ourRow.getAmount(), "0.00"));
        skuRate = ourRow.getSkuRate();

        //Properties
        propertyPopup_Mdl.setItemCode(ourRow.getSharedKeyFieldCode());
        propertyPopup_Mdl.setMrp(ourRow.getMrp());
        propertyPopup_Mdl.setSkuMrp(ourRow.getSkuMrp());
        propertyPopup_Mdl.setBatchNo(ourRow.getBatchNo());
        propertyPopup_Mdl.setLotNo(ourRow.getLotNo());
        propertyPopup_Mdl.setManufacturingDate(ourRow.getManufacturingDate());
        propertyPopup_Mdl.setExpiryDate(ourRow.getExpiryDate());
        propertyPopup_Mdl.setColourCode(ourRow.getColourCode());
        propertyPopup_Mdl.setColourName(ourRow.getColourName());
        propertyPopup_Mdl.setSizeCode(ourRow.getSizeCode());
        propertyPopup_Mdl.setSizeName(ourRow.getSizeName());
        propertyPopup_Mdl.setItemMakeCode(ourRow.getItemMakeCode());
        propertyPopup_Mdl.setItemMakeName(ourRow.getItemMakeName());
        txtProperties.setText(propertyPopup_Mdl.getPropertiesCsv());

        //Amount
        txtAmount.setText(ourRow.getAmount());
        txtDiscountPer.setText(ourRow.getDiscountPer());
        txtDiscount.setText(ourRow.getDiscount());
        txtTaxableAmt.setText(ourRow.getTaxableAmt());
        if (DmShared.vNull(ourRow.getSgstPer()).doubleValue() > 0) {
            lblSgstUgst.setText(DmEnum.GSTS_Head.SGST.getName());
        } else if (DmShared.vNull(ourRow.getUgstPer()).doubleValue() > 0) {
            lblSgstUgst.setText(DmEnum.GSTS_Head.UGST.getName());
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

        //IAP
        itemMastPurchaseReturnAc = DmShared.xNull(ourRow.getItemMastPurchaseReturnAc());
        txtTaxableAmt.setTag(DmShared.xNull(ourRow.getIsubCode()));

        //Accounts
        txtSGSTPer.setTag(DmShared.xNull(ourRow.getSgstRcmAcCode()));
        txtSGST.setTag(DmShared.xNull(ourRow.getSgstAcCode()));

        txtUGSTPer.setTag(DmShared.xNull(ourRow.getUgstRcmAcCode()));
        txtUGST.setTag(DmShared.xNull(ourRow.getUgstAcCode()));

        txtCGSTPer.setTag(DmShared.xNull(ourRow.getCgstRcmAcCode()));
        txtCGST.setTag(DmShared.xNull(ourRow.getCgstAcCode()));

        txtIGSTPer.setTag(DmShared.xNull(ourRow.getIgstRcmAcCode()));
        txtIGST.setTag(DmShared.xNull(ourRow.getIgstAcCode()));

        //Other
        txtRemark.setText(ourRow.getRemark());
        v_SNo = ourRow.getV_SNo();
        ibCode = ourRow.getIbCode();
        hashCode = ourRow.getSharedKeyFieldHashCode();
        //   overHeadLine.initialize(this.getCurrentRowIndex());

        manageDisplay();

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                if (pReturnEntry.departmentEditable) {
                    txtHDepartment.setFocus(true);
                } else {
                    if (txtHItemName.isEnabled()) {
                        txtHItemName.setFocus(true);
                    } else {
                        txtQuantity.setFocus(true);
                    }
                }
            }
        });
    }

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
        txtHDepartment.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHDepartment(txtHItemName.isEnabled() ? "" : txtHItemName.getTag(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
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

        txtHItemName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHItemName_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHItem(DmShared.xNull(pReturnEntry.txtHVoucherType.getTag()), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHItemName_Mdl>>>() {
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
                onItemSelection(blnStatus, t1Row);
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
                    getGSTS(txtHItemName.getTag(), txtRate.getText(), itsBasedOn, pReturnEntry.location, txtHGstType.getTag(), cbIsManualGstType.getValue());
                }
            }
        });
    }
    //</editor-fold>

    private void onItemSelection(boolean blnStatus, TxtHItemName_Mdl itemMdl) {
        if (!DmShared.xNull(selectedItemCode).equalsIgnoreCase(DmShared.xNull(txtHItemName.getTag()))) {
            resetItem();
            selectedItemCode = DmShared.xNull(txtHItemName.getTag());
        }
        if (blnStatus && itemMdl != null) {
            txtHItemName.setText(DmShared.xNull(itemMdl.getSharedKeyFieldName()));
            txtHItemName.setTag(DmShared.xNull(itemMdl.getSharedKeyFieldCode()));
            txtHItemName.setTag2nd(itemMdl.getItemProperty());

            hsnCode = DmShared.xNull(itemMdl.getHsnCode());

            //Set HSN
            txtHSN.setText(DmShared.xNull(itemMdl.getHsn()));
            txtHSN.setTag(DmShared.xNull(itemMdl.getHsnCode()));

            //Purchase A/c (Based On Item Master)
            itemMastPurchaseReturnAc = DmShared.xNull(itemMdl.getPurchaseReturnAcCode());
            if (pReturnEntry.defaultValue.isInventoryAccountingActive()) {
                txtTaxableAmt.setTag(DmShared.xNull(itemMdl.getISubCode()));
            } else {
                if (!DmShared.xNull(pReturnEntry.defaultValue.getPurchaseAcPostingBasedOn()).trim().equalsIgnoreCase("")) {
                    if (STEnum.PurchaseSalesAcPostingBasedOn.getViaKey(DmShared.iNull(pReturnEntry.defaultValue.getPurchaseAcPostingBasedOn()).intValue()).getId() == STEnum.PurchaseSalesAcPostingBasedOn.ITEM.getId()) {
                        txtTaxableAmt.setTag(DmShared.xNull(itemMdl.getPurchaseReturnAcCode()));
                    }
                }
            }

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

            //getGSTS
            getGSTS(txtHItemName.getTag(), txtRate.getText(), itsBasedOn, pReturnEntry.location, txtHGstType.getTag(), cbIsManualGstType.getValue());

            calculate();
            showPropertyPopUp(true);
            manageDisplay();
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Event">
    private void manageEvent() {
        txtQuantity.addBlurHandler(this);
        txtRate.addBlurHandler(this);
        txtDiscountPer.addBlurHandler(this);
        txtDiscount.addBlurHandler(this);
        txtSgstUgstPer.addBlurHandler(this);
        txtSgstUgst.addBlurHandler(this);
        txtCGST.addBlurHandler(this);
        txtCGSTPer.addBlurHandler(this);
        txtIGSTPer.addBlurHandler(this);
        txtIGST.addBlurHandler(this);

        aProperties.addClickHandler(this);
        cbIsManualTax.addClickHandler(this);
        cbIsManualGstType.addClickHandler(this);

        txtProperties.addKeyUpHandler(this);
        txtProperties.addKeyDownHandler(this);

    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtQuantity) {
            if (DmShared.vNull(txtQuantity.getText()) == 0) {
                txtQuantity.setText(txtQuantity.getTag2nd());
            }
        } else if ((event.getSource() == txtRate)) {
            if (DmShared.iNull(itsBasedOn).intValue() == DmEnum.ItsBasedOn.Rate.toInt()) {
                getGSTS(txtHItemName.getTag(), txtRate.getText(), itsBasedOn, pReturnEntry.location, txtHGstType.getTag(), cbIsManualGstType.getValue());
            } else {
                calculate();
            }
        } else if (event.getSource() == txtDiscountPer
                || event.getSource() == txtDiscount
                || event.getSource() == txtSgstUgstPer
                || event.getSource() == txtSgstUgst
                || event.getSource() == txtCGSTPer
                || event.getSource() == txtCGST
                || event.getSource() == txtIGSTPer
                || event.getSource() == txtIGST
                || event.getSource() == txtRate
                || event.getSource() == txtQuantity) {
            calculate();
        }

    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == aProperties) {
            showPropertyPopUp(false);
        } else if (event.getSource() == cbIsManualGstType) {
            manageTaxControls();
            if (!cbIsManualGstType.getValue()) {
                //getGSTS
                getGSTS(txtHItemName.getTag(), txtRate.getText(), itsBasedOn, pReturnEntry.location, txtHGstType.getTag(), cbIsManualGstType.getValue());
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
            if (event.getSource() == txtProperties) {
                showPropertyPopUp(false);
            } else if (event.getSource() == txtRemark) {
                txtDiscountPer.setFocus(true);
            }
        }
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            if (!DmShared.xNull(txtHItemName.getTag2nd()).trim().equalsIgnoreCase("")) {
                try {
                    ItemPropertyPopup_Vld.validateProperties(txtHItemName.getTag2nd(), isBestBefore, pReturnEntry.txtVoucherDate.getText(), propertyPopup_Mdl);
                } catch (DmException e) {
                    showPropertyPopUp(true);
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
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="PopupCallBacks">
    private void popupCallBacks() {
        propertyPopup.addCallBack(new ItemPropertyPopup.CallBack() {
            @Override
            public void onOk(ItemProperty_Mdl property_Mdl) {
                propertyPopup_Mdl = property_Mdl;
                txtProperties.setText(DmShared.xNull(propertyPopup_Mdl.getPropertiesCsv()));
                txtProperties.setFocus(true);
            }

            @Override
            public void onClose() {
                txtProperties.setFocus(true);
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
             propertyPopup.show(DmShared.xNull(pReturnEntry.txtHBillNo.getTag()).trim().equals("") ? pReturnEntry.topControl.getMode() : "Browse", property, isBestBefore, propertyPopup_Mdl, pReturnEntry.txtVoucherDate.getText(), false,false,STEnum.ItemBatchType.NON_EXPIRED_BATCH);
        } else {
            txtProperties.setEnabled(false);
            if (!isDefaultShow) {
                new DmAlertMessageBox("Properties not applicable for this item.", txtProperties).show();
            }
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

    private void manageTaxControls() {
        txtHGstType.setEnabled(cbIsManualGstType.getValue());

        txtIGST.setEnabled(false);
        txtSgstUgst.setEnabled(false);
        txtCGST.setEnabled(false);

        if (cbIsManualTax.getValue()) {
            if (DmShared.xNull(pReturnEntry.location).equalsIgnoreCase(String.valueOf(DmEnum.DmLocation.InterState.getId()))) {
                txtIGST.setEnabled(true);
            } else if (DmShared.xNull(pReturnEntry.location).equalsIgnoreCase(String.valueOf(DmEnum.DmLocation.IntraState.getId()))) {
                txtSgstUgst.setEnabled(true);
                txtCGST.setEnabled(true);
            }
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Clear">
    private void resetPopup() {
        txtHDepartment.setText("");
        txtHDepartment.setTag("");
        txtHDepartment.setTag2nd("");
        resetDepartment();

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
        skuQty = "";

        txtRate.setText("");
        txtRate.setTag("");
        txtRate.setTag2nd("");

        txtAmount.setText("");
        txtAmount.setTag("");
        txtAmount.setTag2nd("");
        skuRate = "";

        txtRemark.setText("");
        txtRemark.setTag("");
        txtRemark.setTag2nd("");

        v_SNo = "";
        ibCode = "";
    }

    private void resetDepartment() {
        selectedItemCode = "";
        txtHItemName.setText("");
        txtHItemName.setTag("");
        txtHItemName.setTag2nd("");

        resetItem();
    }

    private void resetItem() {
        hsnCode = "";
        itemGSTNature = "";
        itsBasedOn = "";
        isBestBefore = "";
        itemMastPurchaseReturnAc = "";

        txtHSN.setText("");
        txtHSN.setTag("");
        txtHSN.setTag2nd("");

        skuCode = "";
        skuName = "";
        skuAllowDecimalPlaces = "";

        txtPurchaseUnit.setText("");
        txtPurchaseUnit.setTag("");
        txtPurchaseUnit.setTag2nd("");
        purchaseUnitName = "";
        purchaseUnitConversionRate = "";
        gstUnitCode = "";

        propertyPopup_Mdl = new ItemProperty_Mdl();

        txtProperties.setText("");
        txtProperties.setTag("");
        txtProperties.setTag2nd("");

        resetItemType();
    }

    //</editor-fold>
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

}
