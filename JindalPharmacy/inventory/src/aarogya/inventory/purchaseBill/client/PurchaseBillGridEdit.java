package aarogya.inventory.purchaseBill.client;

import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.core.client.Scheduler;
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
import dataman.dmBase.self.shared.DmEnum.ItsBasedOn;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmToolkit.gsts.shared.GSTS_Mdl;
import java.util.List;
import aarogya.inventory.purchaseBill.shared.GrdItemDetail_Mdl;
import aarogya.inventory.purchaseBill.shared.PurchaseBill_Srv;
import aarogya.inventory.purchaseBill.shared.PurchaseBill_SrvAsync;
import aarogya.inventory.purchaseBill.shared.TxtHGrnNo_Mdl;
import aarogya.selfToolkit.itemPropertyPopup.client.ItemPropertyPopup;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import aarogya.selfToolkit.stStatic.shared.STEnum;

public class PurchaseBillGridEdit<T1, T2> extends PurchaseBillGridEdit_Dsg<T1, T2> implements BlurHandler, ClickHandler, KeyUpHandler, KeyDownHandler {

    private ServiceInvoker<PurchaseBill_SrvAsync> srvAsync;
    private int hashCode = 0;
    private final ItemPropertyPopup propertyPopup = new ItemPropertyPopup();
    private ItemProperty_Mdl propertyPopup_Mdl;
    private PurchaseBill billEntry;
    String skuName = "", skuAllowDecimalPlaces = "", itemGSTNature = "", gstUnitCode = "",
            itsBasedOn = "", isBestBefore = "", purchaseUnitName = "", purchaseUnitDecimalPlaces = "", purchaseUnitConversionRate = "", v_SNo = "", skuRate = "";
    private String itemMastPurchaseAc = "";

    public PurchaseBillGridEdit(DmGrid<T1, T2> grid, PurchaseBill billEntry) {
        super(grid, "Item Detail", "750px", "364px");
        srvAsync = new ServiceInvoker<PurchaseBill_SrvAsync>(GWT.create(PurchaseBill_Srv.class), PurchaseBill.class.getName());
        this.billEntry = billEntry;
        textHelpEvents();
        popupCallBacks();
        manageEvent();
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {
        resetPopup(false);
        super.showCenter(currentRowIndex, okNeeded, newNeeded, deleteNeeded);
        
        if (billEntry.defaultValue.isInventoryAccountingActive() && !billEntry.defaultValue.isIncludeOverHeadsInCost()){
//            txtDiscountPer.setEnabled(false);
//            txtDiscount.setEnabled(false);
        }
        
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtHGrnNo.setFocus(true);
            }
        });
    }

    //<editor-fold defaultstate="collapsed" desc="GridEdit Overrides">
    @Override
    public void onOk() {
        try {
            calculate();
            if (DmShared.xNull(txtHGrnNo.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("GRN No. is mandatory.", txtHGrnNo).show();
                return;
            }
            if (DmShared.xNull(txtItemName.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Item is mandatory.", txtHGrnNo).show();
                return;
            }
            if (DmShared.vNull(txtDocQty.getText()) == 0) {
                new DmAlertMessageBox("Doc. Qty. is mandatory.", txtHGrnNo).show();
                return;
            }
            if (DmShared.vNull(txtDocQty.getText()) < 0) {
                new DmAlertMessageBox("Doc. Qty. can't be negative.", txtAcptQty).show();
                return;
            }
            if (DmShared.vNull(txtDocQty.getText()) > DmShared.vNull(txtAcptQty.getTag2nd())) {
                new DmAlertMessageBox("Doc. Qty. should be less than or equal to : " + DmShared.vNull(txtAcptQty.getTag2nd()), txtDocQty).show();
                return;
            }
            if (DmShared.vNull(txtAcptQty.getText()) == 0) {
                new DmAlertMessageBox("Acp. Qty. is mandatory.", txtAcptQty).show();
                return;
            }
            if (DmShared.vNull(txtAcptQty.getText()) < 0) {
                new DmAlertMessageBox("Acp. Qty. can't be negative.", txtAcptQty).show();
                return;
            }
            if (DmShared.vNull(txtAcptQty.getText()) > DmShared.vNull(txtDocQty.getText())) {
                new DmAlertMessageBox("Acp. Qty. should be less than or equal to Doc. Qty.", txtAcptQty).show();
                return;
            }
            if (DmShared.xNull(txtPurchaseUnit.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Unit is mandatory.", txtHGrnNo).show();
                return;
            }
            if (DmShared.vNull(txtRate.getText()) == 0) {
                new DmAlertMessageBox("Rate is mandatory.", txtHGrnNo).show();
                return;
            }
            if (DmShared.vNull(txtAmount.getText()) == 0) {
                new DmAlertMessageBox("Amount is mandatory.", txtHGrnNo).show();
                return;
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

            if (billEntry.defaultValue.isInventoryAccountingActive()) {
                if (DmShared.iNull(txtTaxableAmt.getTag()).longValue() == 0) {
                    new DmAlertMessageBox("Inventory a/c is required.", txtHGrnNo).show();
                    return;
                }
            } else {
                if (DmShared.iNull(txtTaxableAmt.getTag()).longValue() == 0) {
                    new DmAlertMessageBox("Purchase a/c is required.", txtHGrnNo).show();
                    return;
                }
            }

            if (!billEntry.defaultValue.isIncludeTaxInCost()) {
                if (DmShared.vNull(txtSGST.getText()).doubleValue() != 0) {
                    if (DmShared.iNull(txtSGST.getTag()).longValue() == 0) {
                        new DmAlertMessageBox("SGST a/c is required.", txtHGstType).show();
                        return;
                    }
                    if (DmShared.iNull(billEntry.txtHRCMApplicable.getTag()).intValue() == 1) {
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
                    if (DmShared.iNull(billEntry.txtHRCMApplicable.getTag()).intValue() == 1) {
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
                    if (DmShared.iNull(billEntry.txtHRCMApplicable.getTag()).intValue() == 1) {
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
                    if (DmShared.iNull(billEntry.txtHRCMApplicable.getTag()).intValue() == 1) {
                        if (DmShared.iNull(txtIGSTPer.getTag()).longValue() == 0) {
                            new DmAlertMessageBox("IGST-RCM a/c is required.", txtHGstType).show();
                            return;
                        }
                    }
                }
            }

            /*==============================<< Check Item dublicacy with Batch No and Order No. >>====================================*/
            for (GrdItemDetail_Mdl detail_Mdl : (List<GrdItemDetail_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
                if (DmShared.xNull(detail_Mdl.getPcDocId()).equalsIgnoreCase(DmShared.xNull(txtHGrnNo.getTag()))
                        && DmShared.xNull(detail_Mdl.getPcV_SNo()).equalsIgnoreCase(DmShared.xNull(txtHGrnNo.getTag2nd()))) {
                    if (!DmClient.vNull(detail_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                        new DmAlertMessageBox(DmShared.xNull(detail_Mdl.getSharedKeyFieldName()) + " already exists.", txtHGrnNo).show();
                        return;
                    }
                }
            }

            proceedOk();
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtItemName).show();
        }
    }

    @Override
    public void manageOk(T1 row) {
        GrdItemDetail_Mdl ourRow = (GrdItemDetail_Mdl) row;
        fillRowData(ourRow);
        callBack.onOk(ourRow);
    }

    protected void fillRowData(GrdItemDetail_Mdl ourRow) {
        //Item
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtItemName.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtItemName.getText()));

        //Item Details
        ourRow.setItemGSTNature(DmShared.xNull(itemGSTNature));
        ourRow.setItsBasedOn(DmShared.xNull(itsBasedOn));
        ourRow.setIsBestBefore(DmShared.xNull(isBestBefore));
        ourRow.setPropertyList(DmShared.xNull(txtItemName.getTag2nd()));
        ourRow.setHsnCode(DmShared.xNull(txtHSN.getTag()));
        ourRow.setHsn(DmShared.xNull(txtHSN.getText()));
        ourRow.setManualTax(cbIsManualTax.getValue());
        ourRow.setManualGstType(cbIsManualGstType.getValue());
        ourRow.setGstTypeCode(DmShared.xNull(txtHGstType.getTag()));
        ourRow.setGstTypeName(DmShared.xNull(txtHGstType.getText()));
        //SKU
        ourRow.setSkuDecimalPlaces(DmShared.xNull(skuAllowDecimalPlaces));
        ourRow.setSkuName(DmShared.xNull(skuName));

        //Purchase Unit
        ourRow.setPurchaseUnitCode(DmShared.xNull(txtPurchaseUnit.getTag()));
        ourRow.setPurchaseUnitName(DmShared.xNull(purchaseUnitName));
        ourRow.setPurchaseUnitDisplayName(DmShared.xNull(txtPurchaseUnit.getText()));
        ourRow.setPurchaseUnitDecimalPlaces(DmShared.xNull(purchaseUnitDecimalPlaces));
        ourRow.setPurchaseUnitConversionRate(DmShared.xNull(purchaseUnitConversionRate));
        ourRow.setGstUnitCode(DmShared.xNull(gstUnitCode));

        //Properties
        ourRow.setBatchNo(DmShared.xNull(propertyPopup_Mdl.getBatchNo()));
        ourRow.setLotNo(DmShared.xNull(propertyPopup_Mdl.getLotNo()));
        ourRow.setIbCode(DmShared.xNull(propertyPopup_Mdl.getIbCode()));
        ourRow.setManufacturingDate(DmShared.dNull(propertyPopup_Mdl.getManufacturingDate()));
        ourRow.setExpiryDate(DmShared.dNull(propertyPopup_Mdl.getExpiryDate()));
        ourRow.setMrp(DmClient.vNull(propertyPopup_Mdl.getMrp(), "0.00"));
        ourRow.setSkuMrp(DmClient.vNull(propertyPopup_Mdl.getSkuMrp(), "0.0000000"));
        ourRow.setColourCode(DmShared.xNull(propertyPopup_Mdl.getColourCode()));
        ourRow.setColourName(DmShared.xNull(propertyPopup_Mdl.getColourName()));
        ourRow.setSizeCode(DmShared.xNull(propertyPopup_Mdl.getSizeCode()));
        ourRow.setSizeName(DmShared.xNull(propertyPopup_Mdl.getSizeName()));
        ourRow.setItemMakeCode(DmShared.xNull(propertyPopup_Mdl.getItemMakeCode()));
        ourRow.setItemMakeName(DmShared.xNull(propertyPopup_Mdl.getItemMakeName()));
        ourRow.setPropertiesCsv(DmShared.xNull(propertyPopup_Mdl.getPropertiesCsv()));

        //Order Details
        ourRow.setPoRecId(DmShared.xNull(txtOrderNo.getText()));
        ourRow.setPoDocId(DmShared.xNull(txtOrderNo.getTag()));
        ourRow.setPoV_SNo(DmShared.xNull(txtOrderNo.getTag2nd()));

        //Challan Details
        ourRow.setPcRecId(DmShared.xNull(txtHGrnNo.getText()));
        ourRow.setPcDocId(DmShared.xNull(txtHGrnNo.getTag()));
        ourRow.setPcV_SNo(DmShared.xNull(txtHGrnNo.getTag2nd()));
        ourRow.setPcRefNo(DmShared.xNull(txtChallanNo.getText()));
        ourRow.setPcRefDate(DmShared.dNull(txtChallanDate.getText()));
        ourRow.setPcDepartmentCode(DmShared.xNull(txtChallanDate.getTag2nd()));

        //Quantities
        ourRow.setDocQty(DmShared.xNull(txtDocQty.getText()));
        ourRow.setAcceptQty(DmShared.xNull(txtAcptQty.getText()));
        ourRow.setPendingQty(DmShared.xNull(txtAcptQty.getTag2nd()));
        ourRow.setSkuQty(DmShared.xNull(txtSKUQty.getText()));

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
        ourRow.setItemMastPurchaseAc(DmShared.xNull(itemMastPurchaseAc));
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
    }

    @Override
    public void displayRow(T1 row) {
        GrdItemDetail_Mdl ourRow = (GrdItemDetail_Mdl) row;

        //Item
        txtItemName.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtItemName.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));

        //Item Details
        itemGSTNature = ourRow.getItemGSTNature();
        itsBasedOn = ourRow.getItsBasedOn();
        isBestBefore = ourRow.getIsBestBefore();
        txtItemName.setTag2nd(ourRow.getPropertyList());
        cbIsManualTax.setValue(ourRow.isManualTax());
        cbIsManualGstType.setValue(ourRow.isManualGstType());
        txtHGstType.setText(DmShared.xNull(ourRow.getGstTypeName()));
        txtHGstType.setTag(DmShared.xNull(ourRow.getGstTypeCode()));
        txtHGstType.setTag2nd(DmShared.xNull(ourRow.getGstTypeCode()));
        txtHSN.setText(DmShared.xNull(ourRow.getHsn()));
        txtHSN.setTag(DmShared.xNull(ourRow.getHsnCode()));

        //SKU
        skuAllowDecimalPlaces = ourRow.getSkuDecimalPlaces();
        skuName = ourRow.getSkuName();

        //Purchase Unit
        txtPurchaseUnit.setTag(DmShared.xNull(ourRow.getPurchaseUnitCode()));
        txtPurchaseUnit.setText(DmShared.xNull(ourRow.getPurchaseUnitDisplayName()));
        purchaseUnitName = DmShared.xNull(ourRow.getPurchaseUnitName());
        purchaseUnitDecimalPlaces = ourRow.getPurchaseUnitDecimalPlaces();
        purchaseUnitConversionRate = ourRow.getPurchaseUnitConversionRate();
        gstUnitCode = ourRow.getGstUnitCode();

        //Properties
        propertyPopup_Mdl = new ItemProperty_Mdl();
        propertyPopup_Mdl.setItemCode(ourRow.getSharedKeyFieldCode());
        propertyPopup_Mdl.setBatchNo(ourRow.getBatchNo());
        propertyPopup_Mdl.setLotNo(ourRow.getLotNo());
        propertyPopup_Mdl.setIbCode(ourRow.getIbCode());
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
        txtOrderNo.setText(DmShared.xNull(ourRow.getPoRecId()));
        txtOrderNo.setTag(DmShared.xNull(ourRow.getPoDocId()));
        txtOrderNo.setTag2nd(DmShared.xNull(ourRow.getPoV_SNo()));

        //Challan Detail
        txtHGrnNo.setText(DmShared.xNull(ourRow.getPcRecId()));
        txtHGrnNo.setTag(DmShared.xNull(ourRow.getPcDocId()));
        txtHGrnNo.setTag2nd(DmShared.xNull(ourRow.getPcV_SNo()));
        txtChallanNo.setText(DmShared.xNull(ourRow.getPcRefNo()));
        txtChallanDate.setText(DmShared.dNull(ourRow.getPcRefDate()));
        txtChallanDate.setTag2nd(DmShared.xNull(ourRow.getPcDepartmentCode()));

        //Quantities
        txtDocQty.setText(DmShared.xNull(ourRow.getDocQty()));
        txtAcptQty.setText(DmShared.xNull(ourRow.getAcceptQty()));
        txtAcptQty.setTag2nd(DmShared.xNull(ourRow.getPendingQty()));
        txtSKUQty.setText(DmShared.xNull(ourRow.getSkuQty()));

        //Rate
        txtRate.setText(DmClient.vNull(ourRow.getRate(), "0.00"));
        skuRate = ourRow.getSkuRate();

        //Amount
        txtAmount.setText(DmClient.vNull(ourRow.getAmount(), "0.00"));
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
        itemMastPurchaseAc = DmShared.xNull(ourRow.getItemMastPurchaseAc());
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
        v_SNo = ourRow.getvSNo();
        hashCode = ourRow.getSharedKeyFieldHashCode();

        manageDisplay();
        txtHGrnNo.setFocus(true);
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
        GrdItemDetail_Mdl ourRow = new GrdItemDetail_Mdl();
        callBack.onOk(ourRow);
        hide();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="textHelpEvents">
    private void textHelpEvents() {
        txtHGrnNo.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHGrnNo_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHGrnNo(DmShared.xNull(billEntry.txtHVoucherType.getTag()),
                        billEntry.txtHSupplier.getTag(),
                        billEntry.txtSupplierBillDate.getText(),
                        billEntry.topControl.getMode().equalsIgnoreCase("New") ? "" : billEntry.topControl.getDocId(),
                        v_SNo,
                        helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHGrnNo_Mdl>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHGrnNo).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHGrnNo_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHGrnNo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHGrnNo).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHGrnNo_Mdl t1Row) {
                resetPopup(true);
                if (blnStatus && t1Row != null) {

                    //Item
                    txtItemName.setText(DmShared.xNull(t1Row.getItemName()));
                    txtItemName.setTag(DmShared.xNull(t1Row.getItemCode()));

                    //Item Details
                    itemGSTNature = DmShared.xNull(t1Row.getItemGSTNature());
                    itsBasedOn = DmShared.iNull(t1Row.getItsBasedOn()).toString();
                    isBestBefore = DmShared.iNull(t1Row.getIsBestBefore()).toString();
                    txtItemName.setTag2nd(DmShared.xNull(t1Row.getPropertyList()));
                    cbIsManualTax.setValue(t1Row.isManualTax());
                    cbIsManualGstType.setValue(t1Row.isManualGstType());
                    txtHGstType.setText(DmShared.xNull(t1Row.getGstTypeName()));
                    txtHGstType.setTag(DmShared.xNull(t1Row.getGstTypeCode()));
                    txtHGstType.setTag2nd(DmShared.xNull(t1Row.getGstTypeCode()));

                    //Sku
                    skuAllowDecimalPlaces = DmShared.xNull(t1Row.getSkuDecimalPlaces());
                    skuName = DmShared.xNull(t1Row.getSkuName());

                    //Purchase Unit
                    txtPurchaseUnit.setTag(DmShared.xNull(t1Row.getPurchaseUnitCode()));
                    txtPurchaseUnit.setText(DmShared.xNull(t1Row.getPurchaseUnitDisplayName()));
                    purchaseUnitName = DmShared.xNull(t1Row.getPurchaseUnitName());
                    purchaseUnitDecimalPlaces = DmShared.xNull(t1Row.getPurchaseUnitDecimalPlaces());
                    purchaseUnitConversionRate = DmShared.xNull(t1Row.getPurchaseUnitConversionRate());
                    gstUnitCode = DmShared.xNull(t1Row.getGstUnitCode());

                    //Set HSN
                    txtHSN.setText(DmShared.xNull(t1Row.getHsn()));
                    txtHSN.setTag(DmShared.xNull(t1Row.getHsnCode()));

                    //Purchase A/c (Based On Item Master)
                    itemMastPurchaseAc = DmShared.xNull(t1Row.getItemMastPurchaseAc());
                    if (billEntry.defaultValue.isInventoryAccountingActive()) {
                        //IAP
                        txtTaxableAmt.setTag(DmShared.xNull(t1Row.getIsubCode()));
                    } else {
                        if (!DmShared.xNull(billEntry.defaultValue.getPurchaseAcPostingBasedOn()).trim().equalsIgnoreCase("")) {
                            if (STEnum.PurchaseSalesAcPostingBasedOn.getViaKey(DmShared.iNull(billEntry.defaultValue.getPurchaseAcPostingBasedOn()).intValue()).getId() == STEnum.PurchaseSalesAcPostingBasedOn.ITEM.getId()) {
                                txtTaxableAmt.setTag(DmShared.xNull(t1Row.getItemMastPurchaseAc()));
                            }
                        }
                    }

                    //Properties
                    propertyPopup_Mdl = new ItemProperty_Mdl();
                    propertyPopup_Mdl.setItemCode(DmShared.xNull(t1Row.getItemCode()));
                    propertyPopup_Mdl.setItemName(DmShared.xNull(t1Row.getItemName()));
                    propertyPopup_Mdl.setColourCode(DmShared.xNull(t1Row.getColourCode()));
                    propertyPopup_Mdl.setColourName(DmShared.xNull(t1Row.getColourName()));
                    propertyPopup_Mdl.setSizeCode(DmShared.xNull(t1Row.getSizeCode()));
                    propertyPopup_Mdl.setSizeName(DmShared.xNull(t1Row.getSizeName()));
                    propertyPopup_Mdl.setItemMakeCode(DmShared.xNull(t1Row.getItemMakeCode()));
                    propertyPopup_Mdl.setItemMakeName(DmShared.xNull(t1Row.getItemMakeName()));

                    propertyPopup_Mdl.setMrp(DmShared.xNull(t1Row.getMrp()));
                    propertyPopup_Mdl.setSkuMrp(DmShared.xNull(t1Row.getSkuMrp()));
                    propertyPopup_Mdl.setBatchNo(DmShared.xNull(t1Row.getBatchNo()));
                    propertyPopup_Mdl.setLotNo(DmShared.xNull(t1Row.getLotNo()));
                    propertyPopup_Mdl.setIbCode(DmShared.xNull(t1Row.getIbCode()));
                    propertyPopup_Mdl.setExpiryDate(DmShared.xNull(t1Row.getExpiryDate()));
                    propertyPopup_Mdl.setManufacturingDate(DmShared.xNull(t1Row.getManufacturingDate()));
                    txtProperties.setText(propertyPopup_Mdl.getPropertiesCsv());

                    //Order Details
                    txtOrderNo.setText(DmShared.xNull(t1Row.getPoRecId()));
                    txtOrderNo.setTag(DmShared.xNull(t1Row.getPoDocId()));
                    txtOrderNo.setTag2nd(DmShared.xNull(t1Row.getPoV_SNo()));

                    //Challan Details
                    txtHGrnNo.setText(DmShared.xNull(t1Row.getSharedKeyFieldName()));
                    txtHGrnNo.setTag(DmShared.xNull(t1Row.getSharedKeyFieldCode()));
                    txtHGrnNo.setTag2nd(DmShared.xNull(t1Row.getPcV_SNo()));
                    txtChallanNo.setText(DmShared.xNull(t1Row.getPcRefNo()));
                    txtChallanDate.setText(DmShared.xNull(t1Row.getPcRefDate()));
                    txtChallanDate.setTag2nd(DmShared.xNull(t1Row.getPcDepartmentCode()));

                    //Quantities
                    txtDocQty.setText(DmShared.xNull(t1Row.getPendingQty()));
                    txtAcptQty.setText(DmShared.xNull(t1Row.getPendingQty()));
                    txtAcptQty.setTag2nd(DmShared.xNull(t1Row.getPendingQty()));
                    txtSKUQty.setText(DmShared.xNull(t1Row.getPendingSkuQty()));

                    //Rate
                    txtRate.setText(DmShared.xNull(t1Row.getRate()));

                    //Amount
                    txtAmount.setText(t1Row.getAmount());
                    txtDiscountPer.setText(t1Row.getDiscountPer());
                    txtDiscount.setText(t1Row.getDiscount());
                    txtTaxableAmt.setText(t1Row.getTaxableAmt());
                    txtSgstUgstPer.setText(t1Row.getSgstUgstPer());
                    txtSgstUgst.setText(t1Row.getSgstUgst());
                    txtSGSTPer.setText(t1Row.getSgstPer());
                    txtSGST.setText(t1Row.getSgst());
                    txtUGSTPer.setText(t1Row.getUgstPer());
                    txtUGST.setText(t1Row.getUgst());
                    txtCGSTPer.setText(t1Row.getCgstPer());
                    txtCGST.setText(t1Row.getCgst());
                    txtIGSTPer.setText(t1Row.getIgstPer());
                    txtIGST.setText(t1Row.getIgst());
                    txtFinalDiscountPer.setText(t1Row.getFinalDiscountPer());
                    txtFinalDiscount.setText(t1Row.getFinalDiscount());

                    //set temp value for validate check
                    txtSgstUgst.setTag2nd(t1Row.getSgstUgst());
                    txtCGST.setTag2nd(t1Row.getCgst());
                    txtIGST.setTag2nd(t1Row.getIgst());

                    //getGSTS
                    getGSTS(txtItemName.getTag(), txtRate.getText(), itsBasedOn, billEntry.location, txtHGstType.getTag(), cbIsManualGstType.getValue());
                    calculate();
                    manageDisplay();
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
                    getGSTS(txtItemName.getTag(), txtRate.getText(), itsBasedOn, billEntry.location, txtHGstType.getTag(), cbIsManualGstType.getValue());
                }
            }
        });
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Events">
    private void manageEvent() {
        txtDocQty.addBlurHandler(this);
        txtAcptQty.addBlurHandler(this);
        txtRate.addBlurHandler(this);
        txtDiscountPer.addBlurHandler(this);
        txtDiscount.addBlurHandler(this);
        txtSgstUgstPer.addBlurHandler(this);
        txtSgstUgst.addBlurHandler(this);
        txtCGST.addBlurHandler(this);
        txtCGSTPer.addBlurHandler(this);
        txtIGSTPer.addBlurHandler(this);
        txtIGST.addBlurHandler(this);

        cbIsManualGstType.addClickHandler(this);
        cbIsManualTax.addClickHandler(this);

        txtAcptQty.addKeyDownHandler(this);
        txtProperties.addKeyUpHandler(this);
        aProperties.addClickHandler(this);
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtDocQty) {
            if (DmShared.vNull(txtDocQty.getText()) == 0) {
                txtDocQty.setText(txtAcptQty.getTag2nd());
            }
            calculate();
        } else if (event.getSource() == txtAcptQty) {
            if (DmShared.vNull(txtAcptQty.getText()) == 0) {
                txtAcptQty.setText(txtDocQty.getText());
            }
            calculate();
        } else if ((event.getSource() == txtRate)) {
            if (DmShared.iNull(itsBasedOn).intValue() == ItsBasedOn.Rate.toInt()) {
                getGSTS(txtItemName.getTag(), txtRate.getText(), itsBasedOn, billEntry.location, txtHGstType.getTag(), cbIsManualGstType.getValue());
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
                || event.getSource() == txtIGST) {
            calculate();
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == aProperties) {
            showPropertyPopUp();
        } else if (event.getSource() == cbIsManualGstType) {
            manageTaxControls();
            if (!cbIsManualGstType.getValue()) {
                //getGSTS
                getGSTS(txtItemName.getTag(), txtRate.getText(), itsBasedOn, billEntry.location, txtHGstType.getTag(), cbIsManualGstType.getValue());
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
                showPropertyPopUp();
            }
        }
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        event.stopPropagation();
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            if (event.getSource() == txtAcptQty) {
                txtDiscountPer.setFocus(true);
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
                txtProperties.setText(DmShared.xNull(property_Mdl.getPropertiesCsv()));
                txtProperties.setFocus(true);
            }

            @Override
            public void onClose() {
                txtProperties.setFocus(true);
            }
        });
    }

    private void showPropertyPopUp() {
        String property = DmShared.xNull(txtItemName.getTag2nd());
        if (!DmShared.xNull(txtItemName.getTag()).equalsIgnoreCase("")
                && !DmShared.xNull(property).equalsIgnoreCase("")) {
            txtProperties.setEnabled(true);
            propertyPopup_Mdl.setItemCode(txtItemName.getTag());
            propertyPopup_Mdl.setSkuName(skuName);
            propertyPopup_Mdl.setSkuQtyForRate(purchaseUnitConversionRate);
            propertyPopup.show("Browse", property, isBestBefore, propertyPopup_Mdl, billEntry.txtVoucherDate.getText(), false,false,STEnum.ItemBatchType.NON_EXPIRED_BATCH);
        } else {
            txtProperties.setEnabled(false);
            new DmAlertMessageBox("Properties not applicable for this item.", txtProperties).show();
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Callback">
    interface CallBack {

        public void onOk(GrdItemDetail_Mdl opdService);

        public void onDelete();
    }

    private CallBack callBack;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="clear">
    private void resetPopup(boolean isCalledFromGrnNoHelp) {

        if (!isCalledFromGrnNoHelp) {
            v_SNo = "";

            txtHGrnNo.setText("");
            txtHGrnNo.setTag("");
            txtHGrnNo.setTag2nd("");
        }

        cbIsManualGstType.setValue(false);
        cbIsManualTax.setValue(false);

        itemMastPurchaseAc = "";
        itemGSTNature = "";
        gstUnitCode = "";
        itsBasedOn = "";
        isBestBefore = "";
        purchaseUnitName = "";
        purchaseUnitConversionRate = "";
        purchaseUnitDecimalPlaces = "";
        skuAllowDecimalPlaces = "";
        skuName = "";
        skuRate = "";

        txtChallanNo.setText("");
        txtChallanNo.setTag("");
        txtChallanNo.setTag("");

        txtChallanDate.setText("");
        txtChallanDate.setTag("");
        txtChallanDate.setTag2nd("");

        txtOrderNo.setText("");
        txtOrderNo.setTag("");
        txtOrderNo.setTag2nd("");

        txtItemName.setText("");
        txtItemName.setTag("");
        txtItemName.setTag2nd("");

        txtProperties.setText("");
        txtProperties.setTag("");
        txtProperties.setTag2nd("");

        txtDocQty.setText("");
        txtDocQty.setTag("");
        txtDocQty.setTag2nd("");

        txtAcptQty.setText("");
        txtAcptQty.setTag("");
        txtAcptQty.setTag2nd("");

        txtRate.setText("");
        txtRate.setTag("");
        txtRate.setTag2nd("");

        txtAmount.setText("");
        txtAmount.setTag("");
        txtAmount.setTag2nd("");

        txtHSN.setText("");
        txtHSN.setTag("");
        txtHSN.setTag2nd("");

        resetItemType();

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

    private void manageDisplay() {
        txtAmount.setEnabled(false);
        txtChallanDate.setEnabled(false);
        txtChallanNo.setEnabled(false);
        txtItemName.setEnabled(false);
        txtOrderNo.setEnabled(false);
        txtPurchaseUnit.setEnabled(false);
        txtDocQty.setEnabled(false);
        txtAcptQty.setEnabled(false);
        if (!DmShared.xNull(txtItemName.getTag()).equalsIgnoreCase("")
                && DmShared.xNull(txtItemName.getTag2nd()).equalsIgnoreCase("")) {
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
        txtRate.setEnabled(false);
        manageTaxControls();
    }

    private void manageTaxControls() {
        txtHGstType.setEnabled(cbIsManualGstType.getValue());

        txtIGST.setEnabled(false);
        txtSgstUgst.setEnabled(false);
        txtCGST.setEnabled(false);

        if (cbIsManualTax.getValue()) {
            if (DmShared.xNull(billEntry.location).equalsIgnoreCase(String.valueOf(DmEnum.DmLocation.InterState.getId()))) {
                txtIGST.setEnabled(true);
            } else if (DmShared.xNull(billEntry.location).equalsIgnoreCase(String.valueOf(DmEnum.DmLocation.IntraState.getId()))) {
                txtSgstUgst.setEnabled(true);
                txtCGST.setEnabled(true);
            }
        }
    }

    protected void calculate() {
        double purchaseUnitConvRate = DmShared.vNull(this.purchaseUnitConversionRate);
        double docQty = DmShared.vNull(txtDocQty.getText());
        double acptQty = DmShared.vNull(txtAcptQty.getText());
        double rate = DmShared.vNull(txtRate.getText());
        String purchaseUnitDecimalFormat = DmShared.getNumberFormat(DmShared.iNull(purchaseUnitDecimalPlaces).intValue());
        String skuDecimalFormat = DmShared.getNumberFormat(DmShared.iNull(skuAllowDecimalPlaces).intValue());

        txtDocQty.setText(DmClient.vNull(docQty, purchaseUnitDecimalFormat));
        txtAcptQty.setText(DmClient.vNull(acptQty, purchaseUnitDecimalFormat));
        txtSKUQty.setText(DmClient.vNull((acptQty * purchaseUnitConvRate), skuDecimalFormat));
        if (DmShared.xNull(billEntry.txtHCalculateOn.getTag()).trim().equalsIgnoreCase("0")) {
            txtAmount.setText(DmClient.vNull(docQty * rate, "0.00"));
        } else {
            txtAmount.setText(DmClient.vNull(acptQty * rate, "0.00"));
        }
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
        if (!(DmShared.iNull(billEntry.defaultValue.getIsSiteStateUT()).intValue() == 1)) {
            txtSGSTPer.setText(txtSgstUgstPer.getText());
            txtSGST.setText(txtSgstUgst.getText());
        } else if ((DmShared.iNull(billEntry.defaultValue.getIsSiteStateUT()).intValue() == 1)) {
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
        if (DmShared.iNull(billEntry.txtHRCMApplicable.getTag()).intValue() == 1) {
            subTotal = taxableAmt;
        } else {
            subTotal = taxableAmt + DmShared.vNull(txtSgstUgst.getText()).doubleValue() + DmShared.vNull(txtCGST.getText()).doubleValue() + DmShared.vNull(txtIGST.getText()).doubleValue();
        }
        txtSubTotal.setText(DmClient.vNull(subTotal, "0.00"));
        if (DmShared.vNull(txtFinalDiscountPer.getText()) > 0) {
            txtFinalDiscount.setText(DmClient.vNull(taxableAmt * DmShared.vNull(txtFinalDiscountPer.getText()) * 0.01, "0.00"));
        }
        txtItemValue.setText(DmClient.vNull(subTotal - DmShared.vNull(txtFinalDiscount.getText()).doubleValue(), "0.00"));
    }

    //<editor-fold defaultstate="collapsed" desc="GST">
    private void getGSTS(String itemCode, String itemRate, String itsBasedOn, String location, String gstTypeCode, final boolean isManualGst) {
        srvAsync.invokeRPC(true).getGSTS(itemCode, itemRate, itsBasedOn, location, gstTypeCode, isManualGst, new AsyncCallback<DmResult<List<GSTS_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHGrnNo).show();
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
                    new DmAlertMessageBox(result.getErrorMessage(), txtHGrnNo).show();
                }
            }
        });
    }

    private void setGSTS(List<GSTS_Mdl> lstITS) {
        String taxPer = "", taxAcCode = "", rcmAcCode = "";
        resetTaxDetail();
        if (lstITS != null && lstITS.size() > 0) {

            //Get Purchase A/c From GSTS
            if (!billEntry.defaultValue.isInventoryAccountingActive()) {
                if (!DmShared.xNull(billEntry.defaultValue.getPurchaseAcPostingBasedOn()).trim().equalsIgnoreCase("")) {
                    if (STEnum.PurchaseSalesAcPostingBasedOn.getViaKey(DmShared.iNull(billEntry.defaultValue.getPurchaseAcPostingBasedOn()).intValue()).getId() == STEnum.PurchaseSalesAcPostingBasedOn.GSTS.getId()) {
                        for (GSTS_Mdl iTS_Mdl : lstITS) {
                            if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.PURCHASE_ACCOUNT.getShortName())) {
                                txtTaxableAmt.setTag(iTS_Mdl.getPurchaseSalesAcCode());
                                break;
                            }
                        }
                    }
                }
            }

            for (GSTS_Mdl iTS_Mdl : lstITS) {
                taxPer = "";
                taxAcCode = "";
                rcmAcCode = "";

                if (billEntry.defaultValue.isIncludeTaxInCost()) {
                    if (DmShared.iNull(billEntry.txtHRCMApplicable.getTag()).intValue() == 0) {
                        taxPer = DmClient.vNull(iTS_Mdl.getAtRate(), "0.00");
                        if (!billEntry.defaultValue.isInventoryAccountingActive()) {
                            taxAcCode = DmShared.xNull(txtTaxableAmt.getTag());
                        }
                    }
                } else {
                    taxPer = DmClient.vNull(iTS_Mdl.getAtRate(), "0.00");
                    rcmAcCode = DmShared.xNull(iTS_Mdl.getRcmAcCode());
                    taxAcCode = DmShared.xNull(iTS_Mdl.getInputTaxAcCode());
                }

                if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.SGST.getShortName()) && !(DmShared.iNull(billEntry.defaultValue.getIsSiteStateUT()).intValue() == 1)) {
                    lblSgstUgst.setText(iTS_Mdl.getGstsHeadName());
                    txtSgstUgstPer.setText(taxPer);
                    txtSGSTPer.setText(taxPer);
                    txtSGSTPer.setTag(rcmAcCode);
                    txtSGST.setTag(taxAcCode);

                } else if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.UGST.getShortName()) && (DmShared.iNull(billEntry.defaultValue.getIsSiteStateUT()).intValue() == 1)) {
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
    //</editor-fold>
}
