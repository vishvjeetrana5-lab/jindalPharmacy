package aarogya.pharmacy.purchaseBillDirect.client;

import aarogya.pharmacy.medicineSearch.client.MedicineSearch;
import aarogya.pharmacy.medicineSearch.shared.MedicineSearchFilter_Mdl;
import aarogya.pharmacy.medicineSearch.shared.MedicineSearch_Mdl;
import aarogya.pharmacy.purchaseBillDirect.shared.*;
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
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmEnum.DmLocaleCountry;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PurchaseBillDirectGridEdit<T1, T2> extends PurchaseBillDirectGridEdit_Dsg<T1, T2> implements BlurHandler, KeyDownHandler, ClickHandler, DmRootEvents {

    private final ServiceInvoker<PurchaseBillDirect_SrvAsync> srvAsync;
    private final ImpurityPopup impurityPopup = new ImpurityPopup();
    private final ItemUpdatePopup itemUpdatePopup = new ItemUpdatePopup();
    private final MedicineSearch medicineSearch = new MedicineSearch(null, null);
    private int hashCode = 0;
    private OverHeadLine overHeadLine;
    protected List<GrdImpurity_Mdl> lstImpurity = new ArrayList<>();
    private List<ItemUpdateField_Mdl> lstItemUpdateFields = new ArrayList<>();
    protected PurchaseBillDirect billDirectEntry;
    private String skuCode = "", skuName = "", skuAllowDecimalPlaces = "", purchaseUnitName = "", hsnCode = "", itemGSTNature = "", gstUnitCode = "",
            itsBasedOn = "", isBestBefore = "", isIncludeFreeQtyInSaleRate = "", purchaseUnitConversionRate = "",
            skuRate = "", skuMrp = "", skuSaleRate = "", v_SNo = "", ibCode = "";
    private String masterMrp = "", masterSaleRate = "", masterPurchaseRate = "", masterSchemeOnQty = "", masterSchemeFreeQty = "", masterPurchaseDiscountPer = "";
    private String selectedMedCode = "", selectedOrderNoCode = "", selectedBatchNoCode = "", itemCategoryProffitMargine = "";
    private boolean isFirstShow = true, inventoryAccountingActive = false;
    private String isBarcodeActive = "", isMarkupApplicable = "", podMrp = "";
    boolean isSaleRateEqalToMRP = false;

    public PurchaseBillDirectGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Medicine Detail", "775px", "455px");
        srvAsync = new ServiceInvoker<PurchaseBillDirect_SrvAsync>(GWT.create(PurchaseBillDirect_Srv.class), PurchaseBillDirect.class.getName());
        textHelpEvents();
        popupCallBacks();
        gridEvents();
        manageEvent();
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, PurchaseBillDirect grn, boolean inventoryAccountingActive) {
        resetPopup();
        this.billDirectEntry = grn;
        this.inventoryAccountingActive = inventoryAccountingActive;
        itemCategoryProffitMargine = "";
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
        isSaleRateEqalToMRP = false;
    }

    private void setIts(final String itemCode, final String itemRate, final String location, final String itsBasedOn, final String billDate) {
        srvAsync.invokeRPC(true).getITS(itemCode, itemRate, location, itsBasedOn, billDate, new AsyncCallback<DmResult<List<OverHeadITS_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHMedName).show();
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
                    new DmAlertMessageBox(result.getErrorMessage(), txtHMedName).show();
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
            cbPurchaseReturn.setEnabled(false);
        } else {
            txtHMedName.setEnabled(true);
            txtRate.setEnabled(true);
            cbPurchaseReturn.setEnabled(true);
        }
        if (DmShared.iNull(isBarcodeActive).intValue() == 1) {
            if (!txtBarcode.isEnabled()) {
                txtBarcode.setEnabled(true);
            }
        } else {
            txtBarcode.setEnabled(false);
            if (billDirectEntry.topControl.getMode().equals("Edit")) {
                if (!DmShared.xNull(txtBarcode.getText()).trim().equals("")) {
                    if (!txtBarcode.isEnabled()) {
                        txtBarcode.setEnabled(true);
                    }
                }
            }
        }

        if (billDirectEntry.defaultValue.getLocaleCountry() == DmLocaleCountry.CAMBODIA_USA) {
            txtReceiveQty.setEnabled(false);
            txtRejectedQty.setEnabled(false);
            txtSkuDifferenceQty.setEnabled(false);
            txtMRP.setEnabled(false);
        }
        if (billDirectEntry.defaultValue.isPh_SRateEditInPurchase()) {
            txtSaleRate.setEnabled(false);
        }

    }

    public void calculate() {
        if (!this.billDirectEntry.topControl.getMode().equalsIgnoreCase("Browse")) {
//        if (true) {
            double saleRate = 0;
            String unitDecimalFormat = DmShared.getNumberFormat(DmShared.iNull(txtPurchaseUnit.getTag2nd()).intValue());
            String skuDecimalFormat = DmShared.getNumberFormat(DmShared.iNull(skuAllowDecimalPlaces).intValue());

            //<editor-fold defaultstate="collapsed" desc="Manage Purchase Return">
            if (cbPurchaseReturn.getValue()) {
                txtChallanQty.setText(DmClient.vNull(0 - Math.abs(DmShared.vNull(txtChallanQty.getText())), unitDecimalFormat));
                txtChallanQty.setTag(DmClient.vNull(0 - Math.abs(DmShared.vNull(txtChallanQty.getTag())), unitDecimalFormat));
                txtReceiveQty.setText(DmClient.vNull(0 - Math.abs(DmShared.vNull(txtReceiveQty.getText())), unitDecimalFormat));
                txtReceiveQty.setTag(DmClient.vNull(0 - Math.abs(DmShared.vNull(txtReceiveQty.getTag())), unitDecimalFormat));
                txtRejectedQty.setText(DmClient.vNull(0 - Math.abs(DmShared.vNull(txtRejectedQty.getText())), unitDecimalFormat));
                txtAcceptQty.setText(DmClient.vNull(0 - Math.abs(DmShared.vNull(txtAcceptQty.getText())), unitDecimalFormat));
                txtFreeQty.setText(DmClient.vNull(0 - Math.abs(DmShared.vNull(txtFreeQty.getText())), unitDecimalFormat));
                txtSKUQty.setText(DmClient.vNull(0 - Math.abs(DmShared.vNull(txtSKUQty.getText())), unitDecimalFormat));
            } else {
                txtChallanQty.setText(DmClient.vNull(Math.abs(DmShared.vNull(txtChallanQty.getText())), unitDecimalFormat));
                txtChallanQty.setTag(DmClient.vNull(Math.abs(DmShared.vNull(txtChallanQty.getTag())), unitDecimalFormat));
                txtReceiveQty.setText(DmClient.vNull(Math.abs(DmShared.vNull(txtReceiveQty.getText())), unitDecimalFormat));
                txtReceiveQty.setTag(DmClient.vNull(Math.abs(DmShared.vNull(txtReceiveQty.getTag())), unitDecimalFormat));
                txtRejectedQty.setText(DmClient.vNull(Math.abs(DmShared.vNull(txtRejectedQty.getText())), unitDecimalFormat));
                txtAcceptQty.setText(DmClient.vNull(Math.abs(DmShared.vNull(txtAcceptQty.getText())), unitDecimalFormat));
                txtFreeQty.setText(DmClient.vNull(Math.abs(DmShared.vNull(txtFreeQty.getText())), unitDecimalFormat));
                txtSKUQty.setText(DmClient.vNull(Math.abs(DmShared.vNull(txtSKUQty.getText())), unitDecimalFormat));
            }
            //</editor-fold>

            if (Math.abs(DmShared.vNull(txtReceiveQty.getText())) == 0 && DmShared.vNull(txtFreeQty.getText()) != 0) {
                txtReceiveQty.setText(DmClient.vNull(Math.abs(DmShared.vNull(txtFreeQty.getText())), unitDecimalFormat));
            }

            double freeQtyPerPack = 0;
            double purchaseUnitConvRate = DmShared.vNull(this.purchaseUnitConversionRate);
            double recQty = DmShared.vNull(txtReceiveQty.getText());
            double rejQty = DmShared.vNull(txtRejectedQty.getText());
            double freeQty = DmShared.vNull(txtFreeQty.getText());
            double skuDifferenceQty = DmShared.vNull(txtSkuDifferenceQty.getText());
            double rate = DmShared.vNull(txtRate.getText());
            double mrp = DmShared.vNull(txtMRP.getText());
            double totalTaxRate = 0;
            double totalSkuQty = 0d;

//        if (DmShared.iNull(isIncludeFreeQtyInSaleRate).intValue() == 1 && Math.abs(DmShared.vNull(txtChallanQty.getText())) > 0) {
//            totalSkuQty = (recQty + freeQty) * purchaseUnitConvRate;
//        } else {
//            totalSkuQty = (recQty) * purchaseUnitConvRate;
//        }
            if (Math.abs(DmShared.vNull(txtChallanQty.getText())) > 0) {
                txtAcceptQty.setText(DmClient.vNull(recQty - rejQty, unitDecimalFormat));
                txtSKUQty.setText(DmClient.vNull((DmShared.vNull(txtAcceptQty.getText()) * purchaseUnitConvRate) + skuDifferenceQty + (freeQty * purchaseUnitConvRate), skuDecimalFormat));
            } else {
                txtSKUQty.setText(DmClient.vNull((freeQty * purchaseUnitConvRate), skuDecimalFormat));
                txtAcceptQty.setText(DmClient.vNull((0), skuDecimalFormat));
                txtReceiveQty.setText(DmClient.vNull((txtFreeQty.getText()), skuDecimalFormat));
            }
            if (DmShared.xNull(billDirectEntry.txtHCalculateOn.getTag()).trim().equalsIgnoreCase("0")) {
                txtAmount.setText(DmClient.vNull(DmShared.vNull(txtChallanQty.getText()) * rate, "0.00"));
            } else {
                if (DmShared.vNull(txtAcceptQty.getText()) > 0) {
                    txtAmount.setText(DmClient.vNull(DmShared.vNull(txtAcceptQty.getText()) * rate, "0.00"));
                } else {
                    txtAmount.setText(DmClient.vNull(DmShared.vNull(txtFreeQty.getText()) * rate, "0.00"));
                }
            }

            if (DmShared.vNull(txtAcceptQty.getText()) > 0) {
                freeQtyPerPack = freeQty / DmShared.vNull(txtAcceptQty.getText());
            } else {
                freeQtyPerPack = 0;
            }

            txtRate.setTag2nd(v_SNo);

            skuRate = DmClient.vNull(Math.abs(DmShared.vNull(txtAmount.getText())) / Math.abs(DmShared.vNull(txtSKUQty.getText())), "0.0000000");

            overHeadLine.getDmGrid().commitChanges();

            overHeadLine.calculate(DmClient.vNull(txtAmount.getText(), "0.00"));

            /*<============================Calculate Total Tax Rate for item=======================================>*/
            OverHeadLine_Mdl ohlMdl = OverHeadLine_Shared.getOHByManualCode("CGST", overHeadLine.getOverHeadList());
            totalTaxRate += DmShared.vNull(ohlMdl.getAtRate());
            ohlMdl = OverHeadLine_Shared.getOHByManualCode("SGST", overHeadLine.getOverHeadList());
            totalTaxRate += DmShared.vNull(ohlMdl.getAtRate());
            ohlMdl = OverHeadLine_Shared.getOHByManualCode("IGST", overHeadLine.getOverHeadList());
            totalTaxRate += DmShared.vNull(ohlMdl.getAtRate());
            ohlMdl = OverHeadLine_Shared.getOHByManualCode("UGST", overHeadLine.getOverHeadList());
            totalTaxRate += DmShared.vNull(ohlMdl.getAtRate());
            /*<====================================================================================================>*/

            totalSkuQty = DmShared.vNull(txtSKUQty.getText());

            double disc = 0d, gross = 0d, taxable = 0d, rate2 = 0d;
            if (DmShared.vNull(txtAcceptQty.getText()) > 0) {
                gross = DmShared.vNull(txtRate.getText()) * DmShared.vNull(txtAcceptQty.getText());
            } else {
                gross = DmShared.vNull(txtRate.getText()) * DmShared.vNull(txtFreeQty.getText());
            }

            ohlMdl = OverHeadLine_Shared.getOHByUniqueId("DS", overHeadLine.getOverHeadList());
            disc = DmShared.vNull(ohlMdl.getAmount()).doubleValue();

            if (DmShared.vNull(recQty) > 0) {

                taxable = gross - disc;
                rate2 = DmShared.vNull(gross + (taxable * totalTaxRate) / 100);
                rate2 = rate2 / totalSkuQty;

                saleRate = STClient.getSaleRate(DmShared.vNull(txtMRP.getText()) / purchaseUnitConvRate, rate2, purchaseUnitConvRate, freeQtyPerPack, isIncludeFreeQtyInSaleRate,
                        billDirectEntry.defaultValue.getLstSaleRateRule(), billDirectEntry.defaultValue.getLocaleCountry(), itemCategoryProffitMargine,
                        billDirectEntry.defaultValue.getSaleRateCalculateOn(), isMarkupApplicable);

                saleRate = DmShared.vNull(DmClient.vNull(saleRate, "0.00")) * purchaseUnitConvRate;

            }

            if (DmShared.vNull(txtSaleRate.getText()) == 0) {
                if (!isSaleRateEqalToMRP) {
                    txtSaleRate.setText(DmClient.vNull(saleRate, "0.00"));
                }

                txtSaleRate.setTag(txtSaleRate.getText());
            } else if (!txtSaleRate.isEnabled()) {
                if (!isSaleRateEqalToMRP) {
                    txtSaleRate.setText(DmClient.vNull(saleRate, "0.00"));
                }

                txtSaleRate.setTag(txtSaleRate.getText());
            }

            if (billDirectEntry.defaultValue.getLocaleCountry() == DmLocaleCountry.CAMBODIA_USA) {
                mrp = DmShared.vNull(txtSaleRate.getText()).doubleValue();
                txtMRP.setText(DmClient.vNull(txtSaleRate.getText(), "0.00"));
            }

            skuMrp = DmClient.vNull(mrp / purchaseUnitConvRate, "0.0000000");

            if (!isSaleRateEqalToMRP) {
                skuSaleRate = DmClient.vNull(DmShared.vNull(txtSaleRate.getText()) / purchaseUnitConvRate, "0.0000000");
            }
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
            calculateStaffRate();
        }
    }

    private void calculateStaffRate() {

        txtStaffSaleRate.setText("");
        txtLandedRate.setText("");

        double purchaseUnitConvRate = DmShared.vNull(this.purchaseUnitConversionRate);
        double disc = 0d, gross = 0d, totalTaxRate = 0d, totalSkuQty = 0d, taxable = 0d, rate2 = 0d, staffSaleRate = 0d, landedRate = 0d;
        disc = DmShared.vNull(OverHeadLine_Shared.getOHByUniqueId("DS", overHeadLine.getOverHeadList()).getAmount()).doubleValue();
        double recQty = DmShared.vNull(txtReceiveQty.getText());
        double rejQty = DmShared.vNull(txtRejectedQty.getText());
        double freeQty = DmShared.vNull(txtFreeQty.getText());
        /*<============================Calculate Total Tax Rate for item=======================================>*/
        OverHeadLine_Mdl ohlMdl = OverHeadLine_Shared.getOHByManualCode("CGST", overHeadLine.getOverHeadList());
        totalTaxRate += DmShared.vNull(ohlMdl.getAtRate());
        ohlMdl = OverHeadLine_Shared.getOHByManualCode("SGST", overHeadLine.getOverHeadList());
        totalTaxRate += DmShared.vNull(ohlMdl.getAtRate());
        ohlMdl = OverHeadLine_Shared.getOHByManualCode("IGST", overHeadLine.getOverHeadList());
        totalTaxRate += DmShared.vNull(ohlMdl.getAtRate());
        ohlMdl = OverHeadLine_Shared.getOHByManualCode("UGST", overHeadLine.getOverHeadList());
        totalTaxRate += DmShared.vNull(ohlMdl.getAtRate());
        /*<====================================================================================================>*/

        if (DmShared.vNull(txtAcceptQty.getText()) > 0) {
            gross = DmShared.vNull(txtRate.getText()) * DmShared.vNull(txtAcceptQty.getText());
        } else {
            gross = DmShared.vNull(txtRate.getText()) * DmShared.vNull(txtFreeQty.getText());
        }
        if (DmShared.iNull(isIncludeFreeQtyInSaleRate).intValue() == 1 && Math.abs(DmShared.vNull(txtChallanQty.getText())) > 0) {
            totalSkuQty = (recQty + freeQty) * purchaseUnitConvRate;
        } else {
            totalSkuQty = (recQty) * purchaseUnitConvRate;
        }
        if (totalSkuQty > 0 && gross > 0) {
            taxable = gross - disc;
            rate2 = DmShared.vNull(gross + (taxable * totalTaxRate) / 100);
            rate2 = rate2 / totalSkuQty;
            staffSaleRate = rate2 * purchaseUnitConvRate;
//            staffSaleRate = DmShared.vNull(DmClient.vNull(rate2, "0.00")) * purchaseUnitConvRate;
            landedRate = DmShared.vNull(OverHeadLine_Shared.getOHByManualCode("IV", overHeadLine.getOverHeadList()).getAmount()) / (recQty + freeQty);
            txtStaffSaleRate.setText(DmClient.vNull(staffSaleRate, "0.00"));
            txtLandedRate.setText(DmClient.vNull(landedRate, "0.00"));
        }
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
                new DmAlertMessageBox("Received Qty. is mandatory with Doc. Qty.", txtReceiveQty).show();
                return;
            }
            if (Math.abs(DmShared.vNull(txtReceiveQty.getText()).doubleValue()) > Math.abs(DmShared.vNull(txtChallanQty.getText()).doubleValue()) && (DmShared.vNull(txtChallanQty.getText()) > 0)) {
                new DmAlertMessageBox("Received Qty. should be less than or equal to Doc. Qty.", txtReceiveQty).show();
                return;
            }
            if (Math.abs(DmShared.vNull(txtRejectedQty.getText()).doubleValue()) > Math.abs(DmShared.vNull(txtReceiveQty.getText()).doubleValue())) {
                new DmAlertMessageBox("Rejected Qty. should be less than or equal to Received Qty.", txtRejectedQty).show();
                return;
            }
            if (DmShared.vNull(txtFreeQty.getText()) == 0 && DmShared.vNull(txtAcceptQty.getText()).doubleValue() == 0) {
                new DmAlertMessageBox("At least one is mandatory from Free Qty. and Accept Qty.", txtFreeQty).show();
                return;
            }
            if ((cbPurchaseReturn.getValue() && DmShared.vNull(txtSKUQty.getText()).doubleValue() == 0)
                    || (!cbPurchaseReturn.getValue() && DmShared.vNull(txtSKUQty.getText()).doubleValue() <= 0)) {
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
//            calculate();
            if (billDirectEntry.defaultValue.getLocaleCountry() == DmLocaleCountry.CAMBODIA_USA) {
                if (DmShared.vNull(txtSaleRate.getText()).doubleValue() == 0) {
                    new DmAlertMessageBox("Sale Rate is mandatory.", txtSaleRate).show();
                    return;
                }

                if (DmShared.vNull(txtSaleRate.getText()).doubleValue() < DmShared.vNull(txtRate.getText()).doubleValue()) {
                    new DmAlertMessageBox("Sale rate should be greater than or equal to rate.", txtSaleRate).show();
                    return;
                }
            } else {
                if (DmShared.vNull(txtMRP.getText()).doubleValue() == 0) {
                    new DmAlertMessageBox("MRP is mandatory.", txtMRP).show();
                    return;
                }

                if (billDirectEntry.defaultValue.isPh_SRateEditInPurchase()) {
                    if (DmShared.vNull(txtMRP.getText()) < DmShared.vNull(txtSaleRate.getText())) {
                        new DmConfirmMessageBox("Sale Rate is greater than MRP. Do you want to continue with MRP as sale rate ?", new DialogHideEvent.DialogHideHandler() {
                            @Override
                            public void onDialogHide(DialogHideEvent event) {
                                if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                                    txtSaleRate.setText(txtMRP.getText());
                                    txtSaleRate.setTag(txtMRP.getText());
                                    skuSaleRate = DmClient.vNull(DmShared.vNull(txtSaleRate.getText()) / DmShared.vNull(purchaseUnitConversionRate), "0.0000000");
                                    isSaleRateEqalToMRP = true;
                                } else {
                                    isSaleRateEqalToMRP = false;
                                    txtMRP.setFocus(true);
                                    return;
                                }
                            }
                        }).show();
                        return;
                    }
                } else {
                    if (DmShared.vNull(txtMRP.getText()) < DmShared.vNull(txtRate.getText())) {
                        new DmAlertMessageBox("MRP should be greater than or equal to rate.", txtMRP).show();
                        return;
                    }
                }

                if (DmShared.vNull(txtSaleRate.getText()).doubleValue() == 0) {
                    new DmAlertMessageBox("Sale Rate is mandatory.", txtSaleRate).show();
                    return;
                }
                if (DmShared.vNull(txtSaleRate.getText()).doubleValue() > DmShared.vNull(txtMRP.getText()).doubleValue()) {
                    new DmAlertMessageBox("Sale Rate should be less than or equal to MRP.", txtSaleRate).show();
                    return;
                }
            }
            if (!DmShared.dNull(txtManufacturingDate.getText()).equalsIgnoreCase("")) {
                if (DmShared.convertDate(txtManufacturingDate.getText()).getTime() > DmShared.convertDate(DmShared.dNull(billDirectEntry.txtSupplierBillDate.getText())).getTime()) {
                    new DmAlertMessageBox("Manufacturing date should be less than ref. date.", txtManufacturingDate).show();
                    return;
                }
            }
            if (DmShared.dNull(txtExpDate.getText()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Expiry Date is mandatory.", txtExpDate).show();
                return;
            }
            if (!cbPurchaseReturn.getValue()) {
                if (DmShared.convertDate(txtExpDate.getText()).getTime() <= DmShared.convertDate(DmShared.dNull(billDirectEntry.txtSupplierBillDate.getText())).getTime()) {
                    new DmAlertMessageBox("Expiry date should be greater than ref. date.", txtExpDate.isEnabled() ? txtExpDate : txtManufacturingDate).show();
                    return;
                }
            }
            if (DmShared.iNull(txtHOrderNo.getTag()).longValue() != 0) {
                if (DmShared.vNull(txtAcceptQty.getTag()).doubleValue() > DmShared.vNull(txtAcceptQty.getTag2nd()).doubleValue()) {
                    new DmAlertMessageBox("Accept Qty. should be less than or equal to Pending Qty. (" + DmShared.xNull(txtAcceptQty.getTag2nd()) + ").", txtReceiveQty).show();
                    return;
                }
            }

            if (DmShared.iNull(isBarcodeActive).intValue() == 1) {
                if (DmShared.xNull(txtBarcode.getText()).trim().equalsIgnoreCase("")) {
                    new DmAlertMessageBox("Barcode is mandatory.", txtBarcode).show();
                    return;
                }
            } else {
                if (billDirectEntry.topControl.getMode().equals("Edit")) {
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
            /*==============================<< Check Item dublicacy with Batch No and Order No. >>====================================*/
            for (GrdGrnDetail_Mdl detail_Mdl : (List<GrdGrnDetail_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
                if (DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(txtHMedName.getTag()))
                        && DmShared.xNull(detail_Mdl.getBatchNo()).equalsIgnoreCase(DmShared.xNull(txtHBatchNo.getText()))
                        && DmShared.xNull(detail_Mdl.getBarcode()).equalsIgnoreCase(DmShared.xNull(txtBarcode.getText()))
                        && DmShared.vNull(detail_Mdl.getMrp()).doubleValue() == DmShared.vNull(txtMRP.getText()).doubleValue()
                        && DmShared.xNull(detail_Mdl.getManufacturingDate()).equalsIgnoreCase(DmShared.xNull(txtManufacturingDate.getText()))
                        && DmShared.xNull(detail_Mdl.getExpiryDate()).equalsIgnoreCase(DmShared.xNull(txtExpDate.getText()))
                        && DmShared.xNull(detail_Mdl.getPurchaseUnitCode()).equalsIgnoreCase(DmShared.xNull(txtPurchaseUnit.getTag()))
                        && DmShared.xNull(detail_Mdl.getPoDocId()).equalsIgnoreCase(DmShared.xNull(txtHOrderNo.getTag()))
                        && DmShared.xNull(detail_Mdl.getPoV_SNo()).equalsIgnoreCase(DmShared.xNull(txtHOrderNo.getTag2nd()))
                        && ((DmShared.iNull(detail_Mdl.getIsReturn()).intValue() == 1) == cbPurchaseReturn.getValue())) {
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
            OverHeadLine_Shared.chkGridSingleOH(overHeadLine.getOverHeadList());

            if (!DmShared.vNull(txtSaleRate.getText()).equals(DmShared.vNull(txtSaleRate.getTag()))) {
                new DmConfirmMessageBox("Do you want to continue without updating sale rate to " + DmClient.vNull(txtSaleRate.getTag(), "0.00"), new DialogHideEvent.DialogHideHandler() {
                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.NO) {
                            txtSaleRate.setText(DmClient.vNull(txtSaleRate.getTag(), "0.00"));
                            if (billDirectEntry.defaultValue.getLocaleCountry() == DmLocaleCountry.CAMBODIA_USA) {
                                txtMRP.setText(DmClient.vNull(txtSaleRate.getText(), "0.00"));
                            }
                        }

                        if (DmShared.vNull(txtSaleRate.getText()) > DmShared.vNull(txtMRP.getText())) {
                            new DmAlertMessageBox("Sale Rate should be less than or equal to MRP.", txtSaleRate).show();
                            return;
                        }
                        proceedOnOK();
                    }
                }).show();
            } else {
                proceedOnOK();
            }
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtHMedName).show();
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

        calculate();
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
        ourRow.setSaleRateProffitMargine(itemCategoryProffitMargine);

        //SKU
        ourRow.setSkuCode(DmShared.xNull(skuCode));
        ourRow.setSkuName(DmShared.xNull(skuName));
        ourRow.setSkuDecimalPlaces(DmShared.xNull(skuAllowDecimalPlaces));

        //Purchase Unit
        ourRow.setPurchaseUnitName(DmShared.xNull(purchaseUnitName));
        ourRow.setPurchaseUnitCode(DmShared.xNull(txtPurchaseUnit.getTag()));
        ourRow.setPurchaseUnitDisplayName(DmShared.xNull(txtPurchaseUnit.getText()));
        ourRow.setPurchaseUnitDecimalPlaces(DmShared.xNull(txtPurchaseUnit.getTag2nd()));
        ourRow.setGstUnitCode(DmShared.xNull(gstUnitCode));
        ourRow.setPurchaseUnitConversionRate(DmShared.xNull(purchaseUnitConversionRate));

        //Properties
        ourRow.setBatchNo(DmShared.xNull(txtHBatchNo.getText()));
        ourRow.setIsBarcodeActive(DmShared.xNull(isBarcodeActive));
        ourRow.setBarcode(DmShared.xNull(txtBarcode.getText()));
        ourRow.setManufacturingDate(DmShared.xNull(txtManufacturingDate.getText()));
        ourRow.setBestBeforeDays(DmShared.xNull(txtBestBefore.getText()));
        ourRow.setExpiryDate(DmShared.xNull(txtExpDate.getText()));

        //Order Details
        ourRow.setPoDocId(DmShared.xNull(txtHOrderNo.getTag()));
        ourRow.setPoRecId(DmShared.xNull(txtHOrderNo.getText()));
        ourRow.setPoV_SNo(DmShared.xNull(txtHOrderNo.getTag2nd()));
        ourRow.setPoRefNo(DmShared.xNull(txtPoRefNo.getText()));
        if (!DmShared.xNull(podMrp).equalsIgnoreCase("")) {
            ourRow.setPodMrp(DmShared.xNull(podMrp));
        }
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
        ourRow.setSkuRate(DmShared.xNull(skuRate));
        ourRow.setStaffSaleRate(DmShared.xNull(txtStaffSaleRate.getText()));
        ourRow.setAmount(DmShared.xNull(txtAmount.getText()));

        //Other
        ourRow.setRemark(DmShared.xNull(txtRemark.getText()));
        ourRow.setIsReturn(cbPurchaseReturn.getValue() ? "1" : "0");
        ourRow.setOHLList(overHeadLine.getOverHeadList());

        ourRow.getLstImpurity().clear();
        ourRow.getLstImpurity().addAll(lstImpurity);

        ourRow.getLstItemUpdateFields().clear();
        ourRow.getLstItemUpdateFields().addAll(lstItemUpdateFields);

        ourRow.setIsMarkupApplicable(isMarkupApplicable);
        callBack.onOk(ourRow);
    }

    @Override
    public void displayRow(T1 row) {
        GrdGrnDetail_Mdl ourRow = (GrdGrnDetail_Mdl) row;

        //Clear
        resetPopup();

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
        isMarkupApplicable = ourRow.getIsMarkupApplicable();

        //SKU
        skuCode = ourRow.getSkuCode();
        skuName = ourRow.getSkuName();
        skuAllowDecimalPlaces = ourRow.getSkuDecimalPlaces();

        //Purchase Unit
        purchaseUnitName = ourRow.getPurchaseUnitName();
        txtPurchaseUnit.setTag(ourRow.getPurchaseUnitCode());
        txtPurchaseUnit.setText(ourRow.getPurchaseUnitDisplayName());
        txtPurchaseUnit.setTag2nd(ourRow.getPurchaseUnitDecimalPlaces());
        purchaseUnitConversionRate = ourRow.getPurchaseUnitConversionRate();
        gstUnitCode = ourRow.getGstUnitCode();

        //Properties
        selectedBatchNoCode = DmShared.xNull(ourRow.getIbCode());
        txtHBatchNo.setTag(ourRow.getIbCode());//for temporary purpose
        txtHBatchNo.setText(ourRow.getBatchNo());
        txtBarcode.setText(DmShared.xNull(ourRow.getBarcode()));
        isBarcodeActive = DmShared.xNull(ourRow.getIsBarcodeActive());
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
        skuRate = ourRow.getSkuRate();
        txtAmount.setText(ourRow.getAmount());

        //Other
        txtRemark.setText(ourRow.getRemark());
        cbPurchaseReturn.setValue(DmShared.iNull(ourRow.getIsReturn()).intValue() == 1);
        v_SNo = ourRow.getvSNo();
        hashCode = ourRow.getSharedKeyFieldHashCode();
        overHeadLine.initialize(this.getCurrentRowIndex());

        lstImpurity.clear();
        lstImpurity.addAll(ourRow.getLstImpurity());

        lstItemUpdateFields.clear();
        lstItemUpdateFields.addAll(ourRow.getLstItemUpdateFields());

        manageDisplay();
        txtHOrderNo.setFocus(true);
        calculateStaffRate();
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
                    onMedSelection(true, new TxtHMedicineName_Mdl(), null);
                } else if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER || event.getNativeKeyCode() == KeyCodes.KEY_INSERT
                        || event.getNativeKeyCode() == KeyCodes.KEY_RIGHT || event.getNativeKeyCode() == KeyCodes.KEY_LEFT
                        || event.getNativeKeyCode() == KeyCodes.KEY_UP || event.getNativeKeyCode() == KeyCodes.KEY_DOWN
                        || event.getNativeKeyCode() == KeyCodes.KEY_ALT || event.getNativeKeyCode() == KeyCodes.KEY_CTRL
                        || event.getNativeKeyCode() == KeyCodes.KEY_SHIFT || event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE
                        || event.isAltKeyDown() || event.isControlKeyDown() || event.isShiftKeyDown()) {

                } else {
                    MedicineSearchFilter_Mdl filter = new MedicineSearchFilter_Mdl();
                    filter.setMedicineCode(DmShared.xNull(txtHMedName.getTag()));
                    filter.setDepartmentCode(DmShared.xNull(billDirectEntry.txtHDepartment.getTag()));
                    filter.setDepartmentName(DmShared.xNull(billDirectEntry.txtHDepartment.getText()));
                    filter.setV_Date(DmShared.dNull(billDirectEntry.txtVoucherDate.getText()));
                    medicineSearch.show(filter, String.valueOf((char) event.getNativeKeyCode()), billDirectEntry.txtHVoucherType.getTag());
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
                medMdl.setMasterMrp(medSearchMdl.getMrp());
                medMdl.setMasterSaleRate(medSearchMdl.getSaleRate());
                medMdl.setMasterPurchaseRate(medSearchMdl.getPurchaseRate());
                medMdl.setMasterSchemeOnQty(medSearchMdl.getSchemeOnQty());
                medMdl.setMasterSchemeFreeQty(medSearchMdl.getSchemeFreeQty());
                medMdl.setMasterPurchaseDiscountPer(medSearchMdl.getPurchaseDiscountPer());
                medMdl.setIsIncludeFreeQtyInSaleRate(medSearchMdl.getIsIncludeFreeQtyInSaleRate());
                medMdl.setItemProperty(medSearchMdl.getPropertyList());
                medMdl.setIsBarcodeActive(DmShared.xNull(medSearchMdl.getIsBarcodeActive()));
                medMdl.setSkuCode(medSearchMdl.getSkuCode());
                medMdl.setSkuName(medSearchMdl.getSkuName());
                medMdl.setSkuDecimalPlaces(medSearchMdl.getSkuDecimalPlaces());
                medMdl.setPurchaseUnitCode(medSearchMdl.getPurchaseUnitCode());
                medMdl.setPurchaseUnitName(medSearchMdl.getPurchaseUnitName());
                medMdl.setPurchaseUnitDisplayName(medSearchMdl.getPurchaseUnitName());
                itemCategoryProffitMargine = medSearchMdl.getSaleRateProffitMargine();
                medMdl.setItemCategoryProffitMargine(medSearchMdl.getSaleRateProffitMargine());

                if (!DmShared.xNull(medSearchMdl.getSkuCode()).equalsIgnoreCase(DmShared.xNull(medSearchMdl.getPurchaseUnitCode()))) {
                    medMdl.setPurchaseUnitDisplayName(DmShared.xNull(medSearchMdl.getPurchaseUnitName()) + " = " + DmShared.xNull(medSearchMdl.getPurchaseUnitConversionRate()) + " " + DmShared.xNull(medSearchMdl.getSkuName()));
                }
                medMdl.setPurchaseUnitConversionRate(medSearchMdl.getPurchaseUnitConversionRate());
                medMdl.setPurchaseUnitDecimalPlaces(medSearchMdl.getPurchaseUnitDecimalPlaces());
                medMdl.setGstUnitCode(medSearchMdl.getPurchaseUnitGstUnitCode());

                txtHMedName.setTag(DmShared.xNull(medMdl.getSharedKeyFieldCode()));
                txtHMedName.setText(DmShared.xNull(medMdl.getSharedKeyFieldName()));
                onMedSelection(true, medMdl, null);
                txtHMedName.setFocus(true);
                isMarkupApplicable = DmShared.xNull(medSearchMdl.getIsMarkupApplicable()).toString();
                getCDConfiguration(DmShared.xNull(medMdl.getSharedKeyFieldCode()));
            }

            @Override
            public void close() {
                txtHMedName.setFocus(true);
            }
        });

        txtHOrderNo.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHOrderNo_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHOrderNo(billDirectEntry.txtHSupplier.getTag(), billDirectEntry.txtSupplierBillDate.getText(),
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

                    if (!DmShared.xNull(billDirectEntry.previousOrderNoCode).equalsIgnoreCase(DmShared.xNull(txtHOrderNo.getTag()))) {
                        new DmAlertMessageBox("Only single order is permitted.", txtHOrderNo).show();
                        return;
                    }

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
                    txtAmount.setText(DmShared.xNull(t1Row.getAmount()));
                    isMarkupApplicable = DmShared.xNull(t1Row.getIsMarkupApplicable());
                    podMrp = DmShared.xNull(t1Row.getPodMrp());

                    if (t1Row.getTxtHMedName_Mdl() != null) {
                        txtHMedName.setTag(DmShared.xNull(t1Row.getTxtHMedName_Mdl().getSharedKeyFieldCode()));
                        txtHMedName.setText(DmShared.xNull(t1Row.getTxtHMedName_Mdl().getSharedKeyFieldName()));
                    }

                    onMedSelection(blnStatus, t1Row.getTxtHMedName_Mdl(), t1Row);
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
                if (!DmShared.xNull(selectedBatchNoCode).equalsIgnoreCase(DmShared.xNull(txtHBatchNo.getTag()))) {
                    resetBatchNo();
                    selectedBatchNoCode = DmShared.xNull(txtHBatchNo.getTag());
                }
                if (blnStatus && t1Row != null) {
                    txtBarcode.setText(DmShared.xNull(t1Row.getBarcode()));
                    txtMRP.setText(DmShared.xNull(t1Row.getMrp()));
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
                    ((CellSelectionModel) overHeadLine.getDmGrid().getGrid().getSelectionModel()).selectCell(STClient.getOHLFocusIndex(overHeadLine.getOverHeadList()), 0);
                }
            }
        });
    }

    private void onMedSelection(boolean blnStatus, TxtHMedicineName_Mdl medMdl, TxtHOrderNo_Mdl orderMdl) {
        if (!DmShared.xNull(selectedMedCode).equalsIgnoreCase(DmShared.xNull(txtHMedName.getTag()))) {
            resetItem();
            selectedMedCode = DmShared.xNull(txtHMedName.getTag());
        }
        if (blnStatus && medMdl != null) {
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

            purchaseUnitName = DmShared.xNull(medMdl.getPurchaseUnitName());
            txtPurchaseUnit.setText(DmShared.xNull(medMdl.getPurchaseUnitDisplayName()));
            txtPurchaseUnit.setTag(DmShared.xNull(medMdl.getPurchaseUnitCode()));
            txtPurchaseUnit.setTag2nd(DmShared.xNull(medMdl.getPurchaseUnitDecimalPlaces()));
            gstUnitCode = DmShared.xNull(medMdl.getGstUnitCode());
            purchaseUnitConversionRate = DmShared.xNull(medMdl.getPurchaseUnitConversionRate());
            itemCategoryProffitMargine = DmShared.xNull(medMdl.getItemCategoryProffitMargine());

            if (orderMdl != null) {
                billDirectEntry.ohlHandler.setReference(orderMdl.getOHLListPurchaseOrder(), overHeadLine.getOverHeadList(true));
            }
            setIts(txtHMedName.getTag(), txtRate.getText(), billDirectEntry.location, itsBasedOn, billDirectEntry.txtSupplierBillDate.getText());
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
            txtMrpUnit.setText(purchaseUnitName);
            txtSaleRateUnit.setText(purchaseUnitName);
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
        txtBestBefore.addBlurHandler(this);
        aRejectedQty.addClickHandler(this);
        txtRejectedQty.addKeyDownHandler(this);
        cbCurrentSupplier.addClickHandler(this);
        cbOtherSuppliers.addClickHandler(this);
        cbPurchaseReturn.addClickHandler(this);
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtReceiveQty) {
            calculate();
            onRecievedQtyBlur();
        } else if ((event.getSource() == txtRate)) {
            if (DmShared.iNull(itsBasedOn).intValue() == ItsBasedOn.Rate.toInt()) {
                setIts(txtHMedName.getTag(), txtRate.getText(), billDirectEntry.location, itsBasedOn, billDirectEntry.txtSupplierBillDate.getText());
                if (inventoryAccountingActive) {
                    setStockPostingLedgers(txtHMedName.getTag());
                }
            } else {
                calculate();
            }
        } else if ((event.getSource() == txtManufacturingDate)) {
            txtManufacturingDate.setText(STClient.getDateFromMonthYear(txtManufacturingDate.getText(), false));
            setExpiryDate();
        } else if ((event.getSource() == txtBestBefore)) {
            setExpiryDate();
        } else if ((event.getSource() == txtExpDate)) {
            txtExpDate.setText(STClient.getDateFromMonthYear(txtExpDate.getText(), true));
        } else if (event.getSource() == txtChallanQty) {
            calculate();
            if (!DmShared.vNull(txtChallanQty.getText()).equals(DmShared.vNull(txtChallanQty.getTag()))) {
                txtChallanQty.setTag(txtChallanQty.getText());
                txtReceiveQty.setText(txtChallanQty.getText());
                onRecievedQtyBlur();
            }
        } else if (event.getSource() == txtRejectedQty
                || event.getSource() == txtAcceptQty
                || event.getSource() == txtFreeQty
                || event.getSource() == txtSkuDifferenceQty
                || event.getSource() == txtSKUQty
                || event.getSource() == txtMRP
                || event.getSource() == txtSaleRate) {
            calculate();
        }
    }

    private void onRecievedQtyBlur() {
        calculate();
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
            if (billDirectEntry.defaultValue.getLocaleCountry() != DmLocaleCountry.CAMBODIA_USA) {
                showImporityPopup();
            }
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
        } else if (event.getSource() == cbPurchaseReturn) {
            calculate();
        } else {
            super.onClick(event);
        }
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
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_R) {
            cbPurchaseReturn.setValue(!cbPurchaseReturn.getValue());
            calculate();
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
        lstItemUpdateFields.add(new ItemUpdateField_Mdl("MRP", Field.MRP, masterMrp, txtMRP.getText(), "1", "0"));
        lstItemUpdateFields.add(new ItemUpdateField_Mdl("Sale Rate", Field.SALE_RATE, masterSaleRate, txtSaleRate.getText(), "1", "0"));
        lstItemUpdateFields.add(new ItemUpdateField_Mdl("Purchase Rate", Field.PURCHASE_RATE, masterPurchaseRate, txtRate.getText(), "1", "0"));
        lstItemUpdateFields.add(new ItemUpdateField_Mdl("Scheme", Field.SCHEME, DmClient.vNull(masterSchemeOnQty, unitDecimalPlaces) + " + " + DmClient.vNull(masterSchemeFreeQty, unitDecimalPlaces), DmClient.vNull(Math.abs(DmShared.vNull(txtChallanQty.getText())), unitDecimalPlaces) + " + " + DmClient.vNull(Math.abs(DmShared.vNull(txtFreeQty.getText())), unitDecimalPlaces), "0", "0"));
//        lstItemUpdateFields.add(new ItemUpdateField_Mdl("Purchase Discount %", Field.PURCHASE_DISCOUNT_PERCENTAGE, masterPurchaseDiscountPer, (ohlMdl != null) ? ohlMdl.getAtRate() : "0", "0", "1"));
        lstItemUpdateFields.add(new ItemUpdateField_Mdl("Purchase Discount %", Field.PURCHASE_DISCOUNT_PERCENTAGE, masterPurchaseDiscountPer, (ohlMdl != null) ? ohlMdl.getAtRate() : "0", "1", "0"));

        for (ItemUpdateField_Mdl ifuMdl : lstItemUpdateFields) {
            if (mapTickHistory.containsKey(ifuMdl.getField())) {
                ifuMdl.setSaveToItem(DmShared.iNull(mapTickHistory.get(ifuMdl.getField())[0]).toString());
                ifuMdl.setSaveToCompany(DmShared.iNull(mapTickHistory.get(ifuMdl.getField())[1]).toString());
                ifuMdl.setFinalValue(DmShared.xNull(mapTickHistory.get(ifuMdl.getField())[2]));
            }
        }
        if (billDirectEntry.defaultValue.getLocaleCountry() != DmLocaleCountry.CAMBODIA_USA) {
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
        } else {
            proceedOk();
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
        skuMrp = "";
        skuSaleRate = "";

        txtRemark.setText("");
        txtRemark.setTag("");
        txtRemark.setTag2nd("");

        txtHOrderNo.setText("");
        txtHOrderNo.setTag("");
        txtHOrderNo.setTag2nd("");
        selectedOrderNoCode = "";

        cbPurchaseReturn.setValue(false);

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

        purchaseUnitName = "";
        txtPurchaseUnit.setText("");
        txtPurchaseUnit.setTag("");
        txtPurchaseUnit.setTag2nd("");
        gstUnitCode = "";
        purchaseUnitConversionRate = "";

        grdPrevGrn.getDmGrid().getGrid().getStore().clear();
        cbCurrentSupplier.setValue(false);
        cbOtherSuppliers.setValue(false);

        txtHBatchNo.setText("");
        txtHBatchNo.setTag("");
        txtHBatchNo.setTag2nd("");
        selectedBatchNoCode = "";
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
        txtBarcode.setText("");
        txtBarcode.setTag("");
        txtBarcode.setTag2nd("");

        txtMRP.setText("");
        txtMRP.setTag("");
        txtMRP.setTag2nd("");

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
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="setStockPostingLedgers">
    private void setStockPostingLedgers(String itemCode) {
        srvAsync.invokeRPC(true).getStockPostingLedgers(itemCode, new AsyncCallback<DmResult<List<OverHeadITS_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHMedName).show();
            }

            @Override
            public void onSuccess(DmResult<List<OverHeadITS_Mdl>> result) {
                if (result.isStatus()) {
                    overHeadLine.setITS(result.getData());
                    if (!DmShared.xNull(result.getValue1()).trim().equals("") && !DmShared.xNull(result.getValue2()).trim().equals("")) {
                        // Setting account to one GS + multiple [GS] unique ids
                        List<OverHeadLine_Mdl> lstOHLMdl = OverHeadLine_Shared.getOHByUniqueIds("IV", overHeadLine.getOverHeadList(true));
                        for (OverHeadLine_Mdl ohlMdl : lstOHLMdl) {
                            if (ohlMdl != null) {
                                ohlMdl.setAccountCode(result.getValue1());
                                ohlMdl.setAccountName(result.getValue2());
                            }
                        }
                    }
                    calculate();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtHMedName).show();
                }
            }
        });
    }
//</editor-fold>

    private void getCDConfiguration(String medicineCode) {

        srvAsync.invokeRPC().getCDConfiguration(DmShared.xNull(billDirectEntry.txtHSupplier.getTag()), DmShared.xNull(medicineCode), new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    OverHeadLine_Mdl ohlMdl = OverHeadLine_Shared.getOHByManualCode("DISC", overHeadLine.getOverHeadList(true));
                    ohlMdl.setAtRate(DmClient.vNull(result.getData(), "0.00"));

                    if (DmShared.vNull(result.getData()) > 0) {
                        ohlMdl.setAtRateEditable("0");
                        ohlMdl.setAmountEditable("0");
                    } else {
                        ohlMdl.setAtRateEditable("1");
                        ohlMdl.setAmountEditable("1");
                    }

                    overHeadLine.calculate(txtAmount.getText());
                    overHeadLine.getDmGrid().commitChanges();

                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }
}
