package aarogya.inventory.workBillDirect.client;

import aarogya.inventory.workBillDirect.shared.GrdImpurity_Mdl;
import aarogya.inventory.workBillDirect.shared.WorkBillDirect_Srv;
import aarogya.inventory.workBillDirect.shared.WorkBillDirect_SrvAsync;
import aarogya.inventory.workBillDirect.shared.GrdGrnDetail_Mdl;
import aarogya.inventory.workBillDirect.shared.TxtHOrderNo_Mdl;
import aarogya.inventory.workBillDirect.shared.TxtHItemName_Mdl;
import aarogya.selfToolkit.itemPropertyPopup.client.ItemPropertyPopup;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemPropertyPopup_Vld;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import aarogya.selfToolkit.itemUpdatePopup.client.ItemUpdatePopup;
import aarogya.selfToolkit.itemUpdatePopup.shared.ItemUpdateField_Mdl;
import aarogya.selfToolkit.stStatic.client.STClient;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
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
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.dmBase.self.shared.DmEnum.ItsBasedOn;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
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
import java.util.HashMap;
import java.util.Map;

public class WorkBillDirectGridEdit<T1, T2> extends WorkBillDirectGridEdit_Dsg<T1, T2> implements BlurHandler, KeyUpHandler, KeyDownHandler, ClickHandler {

    private final ServiceInvoker<WorkBillDirect_SrvAsync> srvAsync;
    private final ItemPropertyPopup propertyPopup = new ItemPropertyPopup();
    private final ImpurityPopup impurityPopup = new ImpurityPopup();
    private final ItemUpdatePopup itemUpdatePopup = new ItemUpdatePopup();
    private int hashCode = 0;
    private ItemProperty_Mdl propertyPopup_Mdl;
    private OverHeadLine overHeadLine;
    List<GrdImpurity_Mdl> lstImpurity = new ArrayList<>();
    private List<ItemUpdateField_Mdl> lstItemUpdateFields = new ArrayList<>();
    WorkBillDirect billDirectEntry;
    private String masterMrp = "", masterSaleRate = "", masterPurchaseRate = "", masterSchemeOnQty = "", masterSchemeFreeQty = "", masterPurchaseDiscountPer = "";
    String skuCode = "", skuName = "", skuAllowDecimalPlaces = "", purchaseUnitName = "", hsnCode = "", itemGSTNature = "",
            itsBasedOn = "", isBestBefore = "", purchaseUnitConversionRate = "", skuRate = "", gstUnitCode = "",
            v_SNo = "", ibCode = "", itemTypeCode = "";
    String selectedItemCode = "", selectedOrderNoCode = "";
    boolean isFirstShow = true;
    boolean blnShowStockable = false;

    public WorkBillDirectGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Item Detail", "775px", "455px");
        srvAsync = new ServiceInvoker<WorkBillDirect_SrvAsync>(GWT.create(WorkBillDirect_Srv.class), WorkBillDirect.class.getName());
        textHelpEvents();
        popupCallBacks();
        gridEvents();
        manageEvent();
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, WorkBillDirect billDirectEntry, boolean blnShowStockable) {
        resetPopup();
        this.billDirectEntry = billDirectEntry;
        this.blnShowStockable = blnShowStockable;
        overHeadLine = this.billDirectEntry.ohlHandler.getWidget();
        super.showCenter(currentRowIndex, okNeeded, newNeeded, deleteNeeded);
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
                txtHOrderNo.setFocus(true);
            }
        });
    }

    private void setIts(final String itemCode, final String itemRate, final String location, final String itsBasedOn, final String billDate) {
        srvAsync.invokeRPC(true).getITS(itemCode, itemRate, location, itsBasedOn, billDate, new AsyncCallback<DmResult<List<OverHeadITS_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHItemName).show();
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
        txtAcceptQtyUnit.setEnabled(false);
        txtSKUQty.setEnabled(false);
        txtSkuQtyUnit.setEnabled(false);

        if (!DmShared.xNull(txtHItemName.getTag()).equalsIgnoreCase("")
                && DmShared.xNull(txtHItemName.getTag2nd()).equalsIgnoreCase("")) {
            txtProperties.setEnabled(false);
        } else {
            txtProperties.setEnabled(true);
        }

        if (!DmShared.xNull(txtHOrderNo.getTag()).equalsIgnoreCase("")) {
            txtHItemName.setEnabled(false);
            txtRate.setEnabled(false);
        } else {
            txtHItemName.setEnabled(true);
            txtRate.setEnabled(true);
        }

    }

    public void calculate() {
        double purchaseUnitConvRate = DmShared.vNull(this.purchaseUnitConversionRate);
        double recQty = DmShared.vNull(txtReceiveQty.getText());
        double rejQty = DmShared.vNull(txtRejectedQty.getText());
        double skuDifferenceQty = DmShared.vNull(txtSkuDifferenceQty.getText());
        double rate = DmShared.vNull(txtRate.getText());

        String unitDecimalFormat = DmShared.getNumberFormat(DmShared.iNull(txtPurchaseUnit.getTag2nd()).intValue());
        String skuDecimalFormat = DmShared.getNumberFormat(DmShared.iNull(skuAllowDecimalPlaces).intValue());
        OverHeadLine_Mdl ohlMdl = OverHeadLine_Shared.getOHByUniqueId("IV", overHeadLine.getOverHeadList());

        txtRejectedQty.setText(DmClient.vNull(txtRejectedQty.getText(), unitDecimalFormat));

        txtSkuDifferenceQty.setText(DmClient.vNull(txtSkuDifferenceQty.getText(), skuDecimalFormat));

        txtAcceptQty.setText(DmClient.vNull(recQty - rejQty, unitDecimalFormat));

        txtSKUQty.setText(DmClient.vNull((DmShared.vNull(txtAcceptQty.getText()) * purchaseUnitConvRate) + skuDifferenceQty, skuDecimalFormat));

        //<editor-fold defaultstate="collapsed" desc="Format decimal places of all quantities">
        txtChallanQty.setText(DmClient.vNull(txtChallanQty.getText(), unitDecimalFormat));
        txtReceiveQty.setText(DmClient.vNull(txtReceiveQty.getText(), unitDecimalFormat));
        txtRejectedQty.setText(DmClient.vNull(txtRejectedQty.getText(), unitDecimalFormat));
        txtAcceptQty.setText(DmClient.vNull(txtAcceptQty.getText(), unitDecimalFormat));
        txtSkuDifferenceQty.setText(DmClient.vNull(txtSkuDifferenceQty.getText(), skuDecimalFormat));
        txtSKUQty.setText(DmClient.vNull(txtSKUQty.getText(), skuDecimalFormat));
        //</editor-fold>

        if (DmShared.xNull(billDirectEntry.txtHCalculateOn.getTag()).trim().equalsIgnoreCase("0")) {
            txtAmount.setText(DmClient.vNull(DmShared.vNull(txtChallanQty.getText()) * rate, "0.00"));
        } else {
            txtAmount.setText(DmClient.vNull(DmShared.vNull(txtAcceptQty.getText()) * rate, "0.00"));
        }

        skuRate = DmClient.vNull(DmShared.vNull(txtAmount.getText()) / DmShared.vNull(txtSKUQty.getText()), "0.0000000");

        overHeadLine.getDmGrid().commitChanges();

        overHeadLine.calculate(DmClient.vNull(txtAmount.getText(), "0.00"));

        manageDisplay();
    }

    //<editor-fold defaultstate="collapsed" desc="GridEdit Overrides">
    @Override
    public void onOk() {
        try {
            calculate();
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
                    ItemPropertyPopup_Vld.validateProperties(txtHItemName.getTag2nd(), isBestBefore, billDirectEntry.txtVoucherDate.getText(), propertyPopup_Mdl);
                } catch (DmException e) {
                    new DmAlertMessageBox("Properties : " + e.getMessage(), txtProperties).show();
                    return;
                }
            }
            if (DmShared.vNull(txtChallanQty.getText()) == 0) {
                new DmAlertMessageBox("Doc. Qty. is mandatory.", txtChallanQty).show();
                return;
            }
            if (DmShared.vNull(txtReceiveQty.getText()) == 0) {
                new DmAlertMessageBox("Received Qty. is mandatory.", txtReceiveQty).show();
                return;
            }
            if (DmShared.vNull(txtChallanQty.getText()) < DmShared.vNull(txtReceiveQty.getText())) {
                new DmAlertMessageBox("Received Qty. should be less than or equal to Doc. Qty.", txtReceiveQty).show();
                return;
            }
            if (DmShared.vNull(txtReceiveQty.getText()) < DmShared.vNull(txtRejectedQty.getText())) {
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

            /*==============================<< Check Item dublicacy with Batch No and Order No. >>====================================*/
            for (GrdGrnDetail_Mdl detail_Mdl : (List<GrdGrnDetail_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
                if (DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(txtHItemName.getTag()))
                        && DmShared.xNull(detail_Mdl.getBatchNo()).equalsIgnoreCase(DmShared.xNull(propertyPopup_Mdl.getBatchNo()))
                        && DmShared.xNull(detail_Mdl.getColourCode()).equalsIgnoreCase(DmShared.xNull(propertyPopup_Mdl.getColourCode()))
                        && DmShared.xNull(detail_Mdl.getSizeCode()).equalsIgnoreCase(DmShared.xNull(propertyPopup_Mdl.getSizeCode()))
                        && DmShared.vNull(detail_Mdl.getMrp()).doubleValue() == DmShared.vNull(propertyPopup_Mdl.getMrp()).doubleValue()
                        && DmShared.dNull(detail_Mdl.getManufacturingDate()).equalsIgnoreCase(DmShared.dNull(propertyPopup_Mdl.getManufacturingDate()))
                        && DmShared.dNull(detail_Mdl.getExpiryDate()).equalsIgnoreCase(DmShared.dNull(propertyPopup_Mdl.getExpiryDate()))
                        && DmShared.xNull(detail_Mdl.getPurchaseUnitCode()).equalsIgnoreCase(DmShared.xNull(txtPurchaseUnit.getTag()))
                        && DmShared.xNull(detail_Mdl.getPoDocId()).equalsIgnoreCase(DmShared.xNull(txtHOrderNo.getTag()))
                        && DmShared.xNull(detail_Mdl.getPoV_SNo()).equalsIgnoreCase(DmShared.xNull(txtHOrderNo.getTag2nd()))) {
                    if (!DmClient.vNull(detail_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {

                        String message = "<div style='float:right; margin-right: 5px;'>";
                        message += "Record already exist with following details - <br>";
                        message += (DmShared.xNull(txtHOrderNo.getText()).trim().equals("")) ? "" : "<b>Order No.:</b> " + DmShared.xNull(txtHOrderNo.getText()) + "<br>";
                        message += (DmShared.xNull(txtHItemName.getText()).trim().equals("")) ? "" : "<b>Item:</b> " + DmShared.xNull(txtHItemName.getText()) + "<br>";
                        message += (DmShared.xNull(txtPurchaseUnit.getText()).trim().equals("")) ? "" : "<b>Unit:</b> " + DmShared.xNull(txtPurchaseUnit.getText()) + "<br>";
                        message += (DmShared.xNull(propertyPopup_Mdl.getBatchNo()).trim().equals("")) ? "" : "<b>Batch No.:</b> " + DmShared.xNull(propertyPopup_Mdl.getBatchNo()) + "<br>";
                        message += (DmShared.xNull(propertyPopup_Mdl.getColourName()).trim().equals("")) ? "" : "<b>Colour:</b> " + DmShared.xNull(propertyPopup_Mdl.getColourName()) + "<br>";
                        message += (DmShared.xNull(propertyPopup_Mdl.getSizeName()).trim().equals("")) ? "" : "<b>Size:</b> " + DmShared.xNull(propertyPopup_Mdl.getSizeName()) + "<br>";
                        message += (DmShared.vNull(propertyPopup_Mdl.getMrp()) == 0) ? "" : "<b>MRP:</b> " + DmClient.vNull(propertyPopup_Mdl.getMrp(), "0.00") + "<br>";
                        message += (DmShared.dNull(propertyPopup_Mdl.getManufacturingDate()).trim().equals("")) ? "" : "<b>Manufacturing Date:</b> " + DmShared.dNull(propertyPopup_Mdl.getManufacturingDate()) + "<br>";
                        message += (DmShared.dNull(propertyPopup_Mdl.getExpiryDate()).trim().equals("")) ? "" : "<b>Expiry Date:</b> " + DmShared.dNull(propertyPopup_Mdl.getExpiryDate()) + "<br>";
                        message += "</div>";
                        new DmAlertMessageBox(message, txtHItemName).show();
                        return;
                    }
                }
            }

            /*==============================<< Check overheadLine conditions >>====================================*/
            OverHeadLine_Shared.chkGridSingleOH(overHeadLine.getOverHeadList());

//            proceedOk();
            if (!DmShared.vNull(txtRate.getText()).equals(DmShared.vNull(txtRate.getTag()))) {
                new DmConfirmMessageBox("Do you want to continue without updating purchase rate to " + DmClient.vNull(txtRate.getTag(), "0.00"), new DialogHideEvent.DialogHideHandler() {
                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.NO) {
                            txtRate.setText(DmClient.vNull(txtRate.getTag(), "0.00"));
                        }
                        proceedOnOK();
                    }
                }).show();
            } else {
                proceedOnOK();
            }
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtHItemName).show();
        }
    }

    private void proceedOnOK() {
        if (DmShared.vNull(txtAmount.getText()).doubleValue() > 0) {
            showItemUpdatePopup();
        } else {
            lstItemUpdateFields.clear();
            proceedOk();
        }
    }

    @Override
    public void manageOk(T1 row) {
        GrdGrnDetail_Mdl ourRow = (GrdGrnDetail_Mdl) row;

        //Item
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHItemName.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHItemName.getText()));
        ourRow.setIbCode(DmShared.xNull(ibCode));

        //Item Details
        ourRow.setPropertyList(DmShared.xNull(txtHItemName.getTag2nd()));
        ourRow.setItemGSTNature(DmShared.xNull(itemGSTNature));
        ourRow.setItsBasedOn(DmShared.xNull(itsBasedOn));
        ourRow.setHsnCode(DmShared.xNull(hsnCode));
        ourRow.setIsBestBefore(DmShared.xNull(isBestBefore));
        ourRow.setItemTypeCode(DmShared.xNull(itemTypeCode));
        //SKU
        ourRow.setSkuCode(DmShared.xNull(skuCode));
        ourRow.setSkuName(DmShared.xNull(skuName));
        ourRow.setSkuDecimalPlaces(DmShared.xNull(skuAllowDecimalPlaces));

        //Purchase Unit
        ourRow.setPurchaseUnitCode(DmShared.xNull(txtPurchaseUnit.getTag()));
        ourRow.setPurchaseUnitName(DmShared.xNull(purchaseUnitName));
        ourRow.setPurchaseUnitDisplayName(DmShared.xNull(txtPurchaseUnit.getText()));
        ourRow.setPurchaseUnitDecimalPlaces(DmShared.xNull(txtPurchaseUnit.getTag2nd()));
        ourRow.setGstUnitCode(DmShared.xNull(gstUnitCode));
        ourRow.setPurchaseUnitConversionRate(DmShared.xNull(purchaseUnitConversionRate));

        //Properties
        ourRow.setBatchNo(DmShared.xNull(propertyPopup_Mdl.getBatchNo()));
        ourRow.setManufacturingDate(DmShared.dNull(propertyPopup_Mdl.getManufacturingDate()));
        ourRow.setExpiryDate(DmShared.dNull(propertyPopup_Mdl.getExpiryDate()));
        ourRow.setMrp(DmClient.vNull(propertyPopup_Mdl.getMrp(), "0.00"));
        ourRow.setSkuMrp(DmClient.vNull(propertyPopup_Mdl.getSkuMrp(), "0.0000000"));
        ourRow.setColourCode(DmShared.xNull(propertyPopup_Mdl.getColourCode()));
        ourRow.setColourName(DmShared.xNull(propertyPopup_Mdl.getColourName()));
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
        ourRow.setRejQty(DmShared.xNull(txtRejectedQty.getText()));
        ourRow.setSkuDifferenceQty(DmShared.xNull(txtSkuDifferenceQty.getText()));
        ourRow.setSkuQty(DmShared.xNull(txtSKUQty.getText()));
        ourRow.setAcceptQty(DmShared.xNull(txtAcceptQty.getText()));
        ourRow.setPendingQty(DmShared.xNull(txtAcceptQty.getTag2nd()));

        //Rate
        ourRow.setRate(DmClient.vNull(txtRate.getText(), "0.00"));
        ourRow.setSkuRate(DmShared.xNull(skuRate));
        ourRow.setAmount(DmClient.vNull(txtAmount.getText(), "0.00"));

        //Other
        ourRow.setRemark(DmShared.xNull(txtRemark.getText()));
        ourRow.setOHLList(overHeadLine.getOverHeadList());

        ourRow.getLstImpurity().clear();
        ourRow.getLstImpurity().addAll(lstImpurity);

        ourRow.getLstItemUpdateFields().clear();
        ourRow.getLstItemUpdateFields().addAll(lstItemUpdateFields);

        callBack.onOk(ourRow);
    }

    @Override
    public void displayRow(T1 row) {
        GrdGrnDetail_Mdl ourRow = (GrdGrnDetail_Mdl) row;

        //Clear
        resetPopup();

        //Item
        ibCode = DmShared.xNull(ourRow.getIbCode());
        selectedItemCode = ourRow.getSharedKeyFieldCode();
        txtHItemName.setTag(ourRow.getSharedKeyFieldCode());
        txtHItemName.setText(ourRow.getSharedKeyFieldName());
        reQuery();

        //Item Deails
        txtHItemName.setTag2nd(DmShared.xNull(ourRow.getPropertyList()));
        itemGSTNature = ourRow.getItemGSTNature();
        itsBasedOn = ourRow.getItsBasedOn();
        hsnCode = ourRow.getHsnCode();
        isBestBefore = ourRow.getIsBestBefore();
        itemTypeCode = ourRow.getItemTypeCode();

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

        //Properties
        propertyPopup_Mdl = new ItemProperty_Mdl();
        propertyPopup_Mdl.setItemCode(ourRow.getSharedKeyFieldCode());
        propertyPopup_Mdl.setBatchNo(ourRow.getBatchNo());
        propertyPopup_Mdl.setManufacturingDate(ourRow.getManufacturingDate());
        propertyPopup_Mdl.setExpiryDate(ourRow.getExpiryDate());
        propertyPopup_Mdl.setMrp(ourRow.getMrp());
        propertyPopup_Mdl.setSkuMrp(ourRow.getSkuMrp());
        propertyPopup_Mdl.setColourCode(ourRow.getColourCode());
        propertyPopup_Mdl.setColourName(ourRow.getColourName());
        propertyPopup_Mdl.setSizeCode(ourRow.getSizeCode());
        propertyPopup_Mdl.setSizeName(ourRow.getSizeName());
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
        txtRejectedQty.setText(ourRow.getRejQty());
        txtSkuDifferenceQty.setText(ourRow.getSkuDifferenceQty());
        txtSKUQty.setText(ourRow.getSkuQty());
        txtAcceptQty.setText(ourRow.getAcceptQty());
        txtAcceptQty.setTag2nd(ourRow.getPendingQty());

        //Rate
        txtRate.setText(ourRow.getRate());
        txtRate.setTag(ourRow.getRate());
        skuRate = ourRow.getSkuRate();
        txtAmount.setText(ourRow.getAmount());

        //Other
        txtRemark.setText(ourRow.getRemark());
        v_SNo = ourRow.getvSNo();
        hashCode = ourRow.getSharedKeyFieldHashCode();
        overHeadLine.initialize(this.getCurrentRowIndex());

        lstImpurity.clear();
        lstImpurity.addAll(ourRow.getLstImpurity());

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
        txtHItemName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHItemName_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHItem(!DmShared.xNull(txtHOrderNo.getTag()).equalsIgnoreCase("") ? skuCode : "",
                        !DmShared.xNull(txtHOrderNo.getTag()).equalsIgnoreCase("") ? txtPurchaseUnit.getTag() : "",
                        DmShared.xNull(billDirectEntry.txtHDepartment.getTag()),
                        helper, blnToEndOfFile, blnShowStockable, new AsyncCallback<DmResultPageing<List<TxtHItemName_Mdl>>>() {

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
                srvAsync.invokeRPC(blnCallBackWait).getTxtHOrderNo(billDirectEntry.txtHSupplier.getTag(),
                        billDirectEntry.txtSupplierBillDate.getText(),
                        billDirectEntry.topControl.getMode().equalsIgnoreCase("New") ? "" : billDirectEntry.topControl.getDocId(),
                        v_SNo,
                        billDirectEntry.txtHDepartment.getTag(),
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
                    onItemSelection(blnStatus, t1Row.getTxtHItemName_Mdl(), t1Row);
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

    private void onItemSelection(boolean blnStatus, TxtHItemName_Mdl itemMdl, TxtHOrderNo_Mdl orderMdl) {
        if (!DmShared.xNull(selectedItemCode).equalsIgnoreCase(DmShared.xNull(txtHItemName.getTag()))) {
            resetItem();
            selectedItemCode = DmShared.xNull(txtHItemName.getTag());
        }
        if (blnStatus && itemMdl != null) {
            txtHItemName.setText(DmShared.xNull(itemMdl.getSharedKeyFieldName()));
            txtHItemName.setTag(DmShared.xNull(itemMdl.getSharedKeyFieldCode()));
            txtHItemName.setTag2nd(itemMdl.getItemProperty());

            hsnCode = DmShared.xNull(itemMdl.getHsnCode());
            itemGSTNature = DmShared.xNull(itemMdl.getItemGSTNature());
            itsBasedOn = DmShared.iNull(itemMdl.getItsBasedOn()).toString();
            isBestBefore = DmShared.iNull(itemMdl.getIsBestBefore()).toString();
            itemTypeCode = DmShared.iNull(itemMdl.getItemTypeCode()).toString();

            skuName = DmShared.xNull(itemMdl.getSkuName());
            skuCode = DmShared.xNull(itemMdl.getSkuCode());
            skuAllowDecimalPlaces = DmShared.xNull(itemMdl.getSkuDecimalPlaces());

            txtPurchaseUnit.setText(DmShared.xNull(itemMdl.getPurchaseUnitDisplayName()));
            txtPurchaseUnit.setTag(DmShared.xNull(itemMdl.getPurchaseUnitCode()));
            txtPurchaseUnit.setTag2nd(DmShared.xNull(itemMdl.getPurchaseUnitDecimalPlaces()));
            purchaseUnitName = DmShared.xNull(itemMdl.getPurchaseUnitName());
            gstUnitCode = DmShared.xNull(itemMdl.getGstUnitCode());
            purchaseUnitConversionRate = DmShared.xNull(itemMdl.getPurchaseUnitConversionRate());

            propertyPopup_Mdl = new ItemProperty_Mdl();
            if (orderMdl != null) {
                propertyPopup_Mdl.setItemCode(DmShared.xNull(orderMdl.getItemCode()));
                propertyPopup_Mdl.setItemName(DmShared.xNull(orderMdl.getItemName()));
                propertyPopup_Mdl.setColourCode(DmShared.xNull(orderMdl.getColourCode()));
                propertyPopup_Mdl.setColourName(DmShared.xNull(orderMdl.getColourName()));
                propertyPopup_Mdl.setSizeCode(DmShared.xNull(orderMdl.getSizeCode()));
                propertyPopup_Mdl.setSizeName(DmShared.xNull(orderMdl.getSizeName()));
                billDirectEntry.ohlHandler.setReference(orderMdl.getOHLListPurchaseOrder(), overHeadLine.getOverHeadList(true));
            }
            txtProperties.setText(propertyPopup_Mdl.getPropertiesCsv());

            setIts(txtHItemName.getTag(), txtRate.getText(), billDirectEntry.location, itsBasedOn, billDirectEntry.txtSupplierBillDate.getText());

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
                        billDirectEntry.topControl.getMode().equalsIgnoreCase("New") ? "" : billDirectEntry.topControl.getDocId(),
                        v_SNo,
                        cbCurrentSupplier.getValue() || cbOtherSuppliers.getValue() ? billDirectEntry.txtHSupplier.getTag() : "",
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
            txtSkuDifferenceQtyUnit.setText(skuName);
            txtSkuQtyUnit.setText(skuName);
        } else {
            txtChallanQtyUnit.setText("");
            txtReceiveQtyUnit.setText("");
            txtRejectedQtyUnit.setText("");
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
        txtRejectedQty.addBlurHandler(this);
        txtAcceptQty.addBlurHandler(this);
        txtSkuDifferenceQty.addBlurHandler(this);
        txtSKUQty.addBlurHandler(this);
        txtRate.addBlurHandler(this);
        txtSKUQty.addBlurHandler(this);
        txtSkuDifferenceQty.addBlurHandler(this);
        txtRejectedQty.addKeyUpHandler(this);
        txtProperties.addKeyUpHandler(this);
        txtProperties.addKeyDownHandler(this);
        aProperties.addClickHandler(this);
        aRejectedQty.addClickHandler(this);
        cbCurrentSupplier.addClickHandler(this);
        cbOtherSuppliers.addClickHandler(this);
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtReceiveQty) {
            calculate();
            onRecievedQtyBlur();
        } else if ((event.getSource() == txtRate)) {
            if (DmShared.iNull(itsBasedOn).intValue() == ItsBasedOn.Rate.toInt()) {
                setIts(txtHItemName.getTag(), txtRate.getText(), billDirectEntry.location, itsBasedOn, billDirectEntry.txtSupplierBillDate.getText());
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
        } else if (event.getSource() == txtRejectedQty
                || event.getSource() == txtAcceptQty
                || event.getSource() == txtSkuDifferenceQty
                || event.getSource() == txtSKUQty) {
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
        if (event.getSource() == aRejectedQty) {
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
        } else {
            super.onClick(event);
        }
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_SPACE) {
            if (event.getSource() == txtRejectedQty) {
                showImporityPopup();
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
                        ItemPropertyPopup_Vld.validateProperties(txtHItemName.getTag2nd(), isBestBefore, billDirectEntry.txtVoucherDate.getText(), propertyPopup_Mdl);
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

    private void showPropertyPopUp(boolean isDefaultShow) {
        String property = DmShared.xNull(txtHItemName.getTag2nd());
        if (!DmShared.xNull(txtHItemName.getTag()).equalsIgnoreCase("")
                && !DmShared.xNull(property).equalsIgnoreCase("")) {
            txtProperties.setEnabled(true);
            propertyPopup_Mdl.setItemCode(txtHItemName.getTag());
            propertyPopup_Mdl.setSkuName(skuName);
            propertyPopup_Mdl.setSkuQtyForRate(purchaseUnitConversionRate);
              propertyPopup.show(billDirectEntry.topControl.getMode(), property, isBestBefore, propertyPopup_Mdl, billDirectEntry.txtVoucherDate.getText(), false,false,STEnum.ItemBatchType.NON_EXPIRED_BATCH);
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

    //</editor-fold>
    private void showItemUpdatePopup() {
        OverHeadLine_Mdl ohlMdl = OverHeadLine_Shared.getOHByUniqueId("DS", overHeadLine.getOverHeadList());
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
//        lstItemUpdateFields.add(new ItemUpdateField_Mdl("MRP", ItemUpdateField_Mdl.Field.MRP, masterMrp, txtMRP.getText(), "1", "0"));
//        lstItemUpdateFields.add(new ItemUpdateField_Mdl("Sale Rate", ItemUpdateField_Mdl.Field.SALE_RATE, masterSaleRate, txtSaleRate.getText(), "1", "0"));
        lstItemUpdateFields.add(new ItemUpdateField_Mdl("Purchase Rate", ItemUpdateField_Mdl.Field.PURCHASE_RATE, masterPurchaseRate, txtRate.getText(), "1", "0"));
//        lstItemUpdateFields.add(new ItemUpdateField_Mdl("Scheme", ItemUpdateField_Mdl.Field.SCHEME, DmClient.vNull(masterSchemeOnQty, unitDecimalPlaces) + " + " + DmClient.vNull(masterSchemeFreeQty, unitDecimalPlaces), DmClient.vNull(Math.abs(DmShared.vNull(txtChallanQty.getText())), unitDecimalPlaces) + " + " + DmClient.vNull(Math.abs(DmShared.vNull(txtFreeQty.getText())), unitDecimalPlaces), "0", "0"));
//        lstItemUpdateFields.add(new ItemUpdateField_Mdl("Purchase Discount %", Field.PURCHASE_DISCOUNT_PERCENTAGE, masterPurchaseDiscountPer, (ohlMdl != null) ? ohlMdl.getAtRate() : "0", "0", "1"));
        lstItemUpdateFields.add(new ItemUpdateField_Mdl("Purchase Discount %", ItemUpdateField_Mdl.Field.PURCHASE_DISCOUNT_PERCENTAGE, masterPurchaseDiscountPer, (ohlMdl != null) ? ohlMdl.getAtRate() : "0", "1", "0"));

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
        itemTypeCode = "";
        skuCode = "";
        skuName = "";
        skuAllowDecimalPlaces = "";

        txtPurchaseUnit.setText("");
        txtPurchaseUnit.setTag("");
        txtPurchaseUnit.setTag2nd("");
        purchaseUnitName = "";
        gstUnitCode = "";
        purchaseUnitConversionRate = "";

        txtProperties.setText("");
        txtProperties.setTag("");
        txtProperties.setTag2nd("");

        propertyPopup_Mdl = new ItemProperty_Mdl();
        grdPrevGrn.getDmGrid().getGrid().getStore().clear();
        cbCurrentSupplier.setValue(false);
        cbOtherSuppliers.setValue(false);
    }

    private void resetRecievedQty() {
        txtRejectedQtyUnit.setText("");
        txtRejectedQty.setText("");
        txtRejectedQty.setTag("");
        txtRejectedQty.setTag2nd("");
        lstImpurity.clear();
    }

    //</editor-fold>
}
