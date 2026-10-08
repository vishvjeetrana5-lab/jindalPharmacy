package aarogya.pharmacy.purchaseBill.client;

import aarogya.pharmacy.purchaseBill.shared.GrdItemDetail_Mdl;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import dataman.dmToolkit.overHeadLine.client.OverHeadLine;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Shared;
import aarogya.pharmacy.purchaseBill.shared.PurchaseBill_Srv;
import aarogya.pharmacy.purchaseBill.shared.PurchaseBill_SrvAsync;
import aarogya.pharmacy.purchaseBill.shared.TxtHGrnNo_Mdl;
import aarogya.selfToolkit.stStatic.client.STClient;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.overHeadLine.shared.OverHeadITS_Mdl;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Mdl;
import java.util.List;

public class PurchaseBillGridEdit<T1, T2> extends PurchaseBillGridEdit_Dsg<T1, T2> implements BlurHandler, KeyDownHandler {

    private ServiceInvoker<PurchaseBill_SrvAsync> srvAsync;
    private int hashCode = 0;
    private OverHeadLine overHeadLine;
    private PurchaseBill billEntry;
    String skuAllowDecimalPlaces = "", hsnCode = "", itemGSTNature = "", gstUnitCode = "", itsBasedOn = "",
            skuMrp = "", skuSaleRate = "", purchaseUnitName = "", purchaseUnitDecimalPlaces = "", purchaseUnitConversionRate = "", v_SNo = "";
    boolean isFirstShow = true;

    public PurchaseBillGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Medicine Detail", "750px", "220px");
        srvAsync = new ServiceInvoker<PurchaseBill_SrvAsync>(GWT.create(PurchaseBill_Srv.class), PurchaseBill.class.getName());
        textHelpEvents();
        manageEvent();
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, PurchaseBill billEntry) {
        resetPopup(false);
        this.billEntry = billEntry;
        overHeadLine = this.billEntry.ohlHandler.getWidget();
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
                txtHGrnNo.setFocus(true);
            }
        });
    }

    private void setIts(final String itemCode, final String itemRate, final String location, final String itsBasedOn, final String billDate) {
        srvAsync.invokeRPC(true).getITS(itemCode, itemRate, location, itsBasedOn, billDate, new AsyncCallback<DmResult<List<OverHeadITS_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHGrnNo).show();
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
                    new DmAlertMessageBox(result.getErrorMessage(), txtHGrnNo).show();
                }
            }
        });
    }

    private void manageDisplay() {
        txtAmount.setEnabled(false);
        txtChallanDate.setEnabled(false);
        txtChallanNo.setEnabled(false);
        txtMedName.setEnabled(false);
        txtOrderNo.setEnabled(false);
        txtRate.setEnabled(false);
        txtBatchNo.setEnabled(false);
        txtBarcode.setEnabled(false);
        txtMRP.setEnabled(false);
        txtSaleRate.setEnabled(false);
        txtExpDate.setEnabled(false);
    }

    private void calculate() {
        double purchaseUnitConvRate = DmShared.vNull(this.purchaseUnitConversionRate);
        double docQty = DmShared.vNull(txtDocQty.getText());
        double acptQty = DmShared.vNull(txtAcptQty.getText());
        double freeQty = DmShared.vNull(txtFreeQty.getText());
        double rate = DmShared.vNull(txtRate.getText());
        String purchaseUnitDecimalFormat = DmShared.getNumberFormat(DmShared.iNull(purchaseUnitDecimalPlaces).intValue());
        String skuDecimalFormat = DmShared.getNumberFormat(DmShared.iNull(skuAllowDecimalPlaces).intValue());

        txtDocQty.setText(DmClient.vNull(docQty, purchaseUnitDecimalFormat));
        txtAcptQty.setText(DmClient.vNull(acptQty, purchaseUnitDecimalFormat));
        txtSKUQty.setText(DmClient.vNull(((acptQty + freeQty) * purchaseUnitConvRate), skuDecimalFormat));
        if (DmShared.xNull(billEntry.txtHCalculateOn.getTag()).trim().equalsIgnoreCase("0")) {
            txtAmount.setText(DmClient.vNull(docQty * rate, "0.00"));
        } else {
            txtAmount.setText(DmClient.vNull(acptQty * rate, "0.00"));
        }
        overHeadLine.getDmGrid().commitChanges();
        overHeadLine.calculate(txtAmount.getText());
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
            if (DmShared.xNull(txtMedName.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Medicine is mandatory.", txtHGrnNo).show();
                return;
            }
            if (DmShared.xNull(txtPurchaseUnit.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Unit is mandatory.", txtHGrnNo).show();
                return;
            }
            if (DmShared.vNull(txtDocQty.getText()) == 0 && DmShared.vNull(txtAcptQty.getTag2nd()) != 0) {
                new DmAlertMessageBox("Doc. Qty. is mandatory with Acp. Qty.", txtAcptQty).show();
                return;
            }
            if (DmShared.vNull(txtDocQty.getText()) > DmShared.vNull(txtAcptQty.getTag2nd())) {
                new DmAlertMessageBox("Doc. Qty. should be less than or equal to : " + DmShared.vNull(txtAcptQty.getTag2nd()), txtDocQty).show();
                return;
            }
            if (DmShared.vNull(txtAcptQty.getText()) > DmShared.vNull(txtDocQty.getText())) {
                new DmAlertMessageBox("Acp. Qty. should be less than or equal to Doc. Qty.", txtAcptQty).show();
                return;
            }
            if (DmShared.vNull(txtFreeQty.getText()) > DmShared.vNull(txtFreeQty.getTag2nd())) {
                new DmAlertMessageBox("Free Qty. should be less than or equal to : " + DmShared.vNull(txtFreeQty.getTag2nd()), txtFreeQty).show();
                return;
            }
            if (DmShared.vNull(txtFreeQty.getText()) == 0 && DmShared.vNull(txtAcptQty.getText()) == 0) {
                new DmAlertMessageBox("At least one is mandatory from Acp. Qty. and Free Qty.", txtAcptQty).show();
                return;
            }
            if (DmShared.vNull(txtSKUQty.getText()) == 0) {
                new DmAlertMessageBox("SKU Qty. is mandatory.", txtSKUQty).show();
                return;
            }
            if (DmShared.vNull(txtAcptQty.getText()) != 0 && DmShared.vNull(txtRate.getText()) == 0) {
                new DmAlertMessageBox("Rate is mandatory.", txtHGrnNo).show();
                return;
            }
            if (DmShared.vNull(txtAcptQty.getText()) != 0 && DmShared.vNull(txtAmount.getText()) == 0) {
                new DmAlertMessageBox("Amount is mandatory.", txtHGrnNo).show();
                return;
            }
            if (DmShared.xNull(txtBatchNo.getText()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Batch No. is mandatory.", txtHGrnNo).show();
                return;
            }
            if (DmShared.xNull(txtBatchNo.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("IbCode is mandatory.", txtHGrnNo).show();
                return;
            }
            if (DmShared.vNull(txtMRP.getText()) == 0) {
                new DmAlertMessageBox("MRP is mandatory.", txtHGrnNo).show();
                return;
            }
            if (DmShared.vNull(txtMRP.getText()) < DmShared.vNull(txtRate.getText())) {
                new DmAlertMessageBox("MRP should be greater than or equal to Rate.", txtHGrnNo).show();
                return;
            }
            if (DmShared.vNull(txtSaleRate.getText()) == 0) {
                new DmAlertMessageBox("Sale Rate is mandatory.", txtHGrnNo).show();
                return;
            }
            if (DmShared.vNull(txtSaleRate.getText()) > DmShared.vNull(txtMRP.getText())) {
                new DmAlertMessageBox("Sale Rate should be less than or equal to MRP.", txtHGrnNo).show();
                return;
            }
            if (DmShared.dNull(txtExpDate.getText()).trim().equals("")) {
                new DmAlertMessageBox("Expiry Date is mandatory.", txtHGrnNo).show();
                return;
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

            /*==============================<< Check overheadLine conditions >>====================================*/
            OverHeadLine_Shared.chkGridSingleOH(overHeadLine.getOverHeadList());

            proceedOk();
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtMedName).show();
        }
    }

    @Override
    public void manageOk(T1 row) {
        GrdItemDetail_Mdl ourRow = (GrdItemDetail_Mdl) row;

        //Item
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtMedName.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtMedName.getText()));

        //Item Details
        ourRow.setItemGSTNature(DmShared.xNull(itemGSTNature));
        ourRow.setItsBasedOn(DmShared.xNull(itsBasedOn));
        ourRow.setHsnCode(DmShared.xNull(hsnCode));

        //SKU
        ourRow.setSkuDecimalPlaces(DmShared.xNull(skuAllowDecimalPlaces));

        //Purchase Unit
        ourRow.setPurchaseUnitCode(DmShared.xNull(txtPurchaseUnit.getTag()));
        ourRow.setPurchaseUnitName(DmShared.xNull(purchaseUnitName));
        ourRow.setPurchaseUnitDisplayName(DmShared.xNull(txtPurchaseUnit.getText()));
        ourRow.setPurchaseUnitDecimalPlaces(DmShared.xNull(purchaseUnitDecimalPlaces));
        ourRow.setPurchaseUnitConversionRate(DmShared.xNull(purchaseUnitConversionRate));
        ourRow.setGstUnitCode(DmShared.xNull(gstUnitCode));

        //Properties
        ourRow.setBatchNo(DmShared.xNull(txtBatchNo.getText()));
        ourRow.setIbCode(DmShared.xNull(txtBatchNo.getTag()));

        ourRow.setBarcode(DmShared.xNull(txtBarcode.getText()));

        ourRow.setExpiryDate(DmShared.dNull(txtExpDate.getText()));
        ourRow.setManufacturingDate(DmShared.dNull(txtExpDate.getTag()));

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
        ourRow.setFreeQty(DmShared.xNull(txtFreeQty.getText()));
        ourRow.setPendingQty(DmShared.xNull(txtAcptQty.getTag2nd()));
        ourRow.setPendingFreeQty(DmShared.xNull(txtFreeQty.getTag2nd()));
        ourRow.setSkuQty(DmShared.xNull(txtSKUQty.getText()));

        //Rate
        ourRow.setMrp(DmClient.vNull(txtMRP.getText(), "0.00"));
        ourRow.setSkuMrp(skuMrp);
        ourRow.setRate(DmClient.vNull(txtRate.getText(), "0.00"));
        ourRow.setSaleRate(DmClient.vNull(txtSaleRate.getText(), "0.00"));
        ourRow.setSkuSaleRate(skuSaleRate);
        ourRow.setAmount(DmClient.vNull(txtAmount.getText(), "0.00"));

        //Other
        ourRow.setOHLList(overHeadLine.getOverHeadList());

        callBack.onOk(ourRow);
    }

    @Override
    public void displayRow(T1 row) {
        GrdItemDetail_Mdl ourRow = (GrdItemDetail_Mdl) row;

        //Item
        txtMedName.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtMedName.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));

        //Item Details
        itemGSTNature = ourRow.getItemGSTNature();
        itsBasedOn = ourRow.getItsBasedOn();
        hsnCode = ourRow.getHsnCode();

        //SKU
        skuAllowDecimalPlaces = ourRow.getSkuDecimalPlaces();

        //Purchase Unit
        txtPurchaseUnit.setTag(DmShared.xNull(ourRow.getPurchaseUnitCode()));
        txtPurchaseUnit.setText(DmShared.xNull(ourRow.getPurchaseUnitDisplayName()));
        purchaseUnitName = DmShared.xNull(ourRow.getPurchaseUnitName());
        purchaseUnitDecimalPlaces = ourRow.getPurchaseUnitDecimalPlaces();
        purchaseUnitConversionRate = ourRow.getPurchaseUnitConversionRate();
        gstUnitCode = ourRow.getGstUnitCode();

        //Properties
        txtBatchNo.setText(DmShared.xNull(ourRow.getBatchNo()));
        txtBatchNo.setTag(DmShared.xNull(ourRow.getIbCode()));

        txtBarcode.setText(DmShared.xNull(ourRow.getBarcode()));

        txtExpDate.setText(DmShared.xNull(ourRow.getExpiryDate()));
        txtExpDate.setTag(DmShared.xNull(ourRow.getManufacturingDate()));

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
        txtFreeQty.setText(DmShared.xNull(ourRow.getFreeQty()));
        txtFreeQty.setTag2nd(DmShared.xNull(ourRow.getPendingFreeQty()));
        txtSKUQty.setText(DmShared.xNull(ourRow.getSkuQty()));

        //Rate
        txtMRP.setText(DmClient.vNull(ourRow.getMrp(), "0.00"));
        skuMrp = ourRow.getSkuMrp();
        txtRate.setText(DmClient.vNull(ourRow.getRate(), "0.00"));
        txtSaleRate.setText(DmClient.vNull(ourRow.getSaleRate(), "0.00"));
        skuSaleRate = ourRow.getSkuSaleRate();
        txtAmount.setText(DmClient.vNull(ourRow.getAmount(), "0.00"));

        //Other
        v_SNo = ourRow.getvSNo();
        hashCode = ourRow.getSharedKeyFieldHashCode();
        overHeadLine.initialize(this.getCurrentRowIndex());

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
                srvAsync.invokeRPC(blnCallBackWait).getTxtHGrnNo(
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
                    txtMedName.setText(DmShared.xNull(t1Row.getItemName()));
                    txtMedName.setTag(DmShared.xNull(t1Row.getItemCode()));

                    //Item Details
                    itemGSTNature = DmShared.xNull(t1Row.getItemGSTNature());
                    itsBasedOn = DmShared.iNull(t1Row.getItsBasedOn()).toString();
                    hsnCode = DmShared.xNull(t1Row.getHsnCode());

                    //Sku
                    skuAllowDecimalPlaces = DmShared.xNull(t1Row.getSkuDecimalPlaces());

                    //Purchase Unit
                    txtPurchaseUnit.setTag(DmShared.xNull(t1Row.getPurchaseUnitCode()));
                    txtPurchaseUnit.setText(DmShared.xNull(t1Row.getPurchaseUnitDisplayName()));
                    purchaseUnitName = DmShared.xNull(t1Row.getPurchaseUnitName());
                    purchaseUnitDecimalPlaces = DmShared.xNull(t1Row.getPurchaseUnitDecimalPlaces());
                    purchaseUnitConversionRate = DmShared.xNull(t1Row.getPurchaseUnitConversionRate());
                    gstUnitCode = DmShared.xNull(t1Row.getGstUnitCode());

                    //Properties
                    txtBatchNo.setText(DmShared.xNull(t1Row.getBatchNo()));
                    txtBatchNo.setTag(DmShared.xNull(t1Row.getIbCode()));

                    txtBarcode.setText(DmShared.xNull(t1Row.getBarcode()));

                    txtExpDate.setText(DmShared.xNull(t1Row.getExpiryDate()));
                    txtExpDate.setTag(DmShared.xNull(t1Row.getManufacturingDate()));

                    //Order Details
                    txtOrderNo.setText(DmShared.xNull(t1Row.getPoRecId()));
                    txtOrderNo.setTag(DmShared.xNull(t1Row.getPoDocId()));
                    txtOrderNo.setTag2nd(DmShared.xNull(t1Row.getPoV_SNo()));

                    //Challan Details
                    txtHGrnNo.setText(DmShared.xNull(t1Row.getSharedKeyFieldName()));
                    txtHGrnNo.setTag(DmShared.xNull(t1Row.getSharedKeyFieldCode()));
                    txtHGrnNo.setTag2nd(DmShared.xNull(t1Row.getPcV_SNo()));
                    txtChallanNo.setText(DmShared.xNull(t1Row.getPcRefNo()));
                    txtChallanDate.setText(DmShared.dNull(t1Row.getPcRefDate()));
                    txtChallanDate.setTag2nd(DmShared.xNull(t1Row.getPcDepartmentCode()));

                    //Quantities
                    txtDocQty.setText(DmShared.xNull(t1Row.getPendingQty()));
                    txtAcptQty.setText(DmShared.xNull(t1Row.getPendingQty()));
                    txtAcptQty.setTag2nd(DmShared.xNull(t1Row.getPendingQty()));
                    txtFreeQty.setText(DmShared.xNull(t1Row.getPendingFreeQty()));
                    txtFreeQty.setTag2nd(DmShared.xNull(t1Row.getPendingFreeQty()));
                    txtSKUQty.setText(DmShared.xNull(t1Row.getPendingSkuQty()));

                    //Rate
                    txtMRP.setText(DmShared.xNull(t1Row.getMrp()));
                    skuMrp = t1Row.getSkuMrp();
                    txtRate.setText(DmShared.xNull(t1Row.getRate()));
                    txtSaleRate.setText(DmShared.xNull(t1Row.getSaleRate()));
                    skuSaleRate = t1Row.getSkuSaleRate();
                    txtAmount.setText(DmShared.xNull(t1Row.getAmount()));

                    //Other
                    billEntry.ohlHandler.setReference(t1Row.getOHLListGRN(), overHeadLine.getOverHeadList(true));
                    setIts(txtMedName.getTag(), txtRate.getText(), billEntry.location, itsBasedOn, billEntry.txtSupplierBillDate.getText());
                    calculate();
                }
            }
        });
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Events">
    private void manageEvent() {
        txtDocQty.addBlurHandler(this);
        txtAcptQty.addBlurHandler(this);
        txtFreeQty.addBlurHandler(this);
        txtFreeQty.addKeyDownHandler(this);
    }

    @Override
    public void onBlur(BlurEvent event) {
//        if (event.getSource() == txtDocQty) {
//            if (DmShared.vNull(txtDocQty.getText()) == 0) {
//                txtDocQty.setText(txtAcptQty.getTag2nd());
//            }
//        } else if (event.getSource() == txtAcptQty) {
//            if (DmShared.vNull(txtAcptQty.getText()) == 0) {
//                txtAcptQty.setText(txtDocQty.getText());
//            }
//        } else if (event.getSource() == txtFreeQty) {
//            if (DmShared.vNull(txtFreeQty.getText()) == 0) {
//                txtFreeQty.setText(txtFreeQty.getTag2nd());
//            }
//        }

        if (event.getSource() == txtDocQty
                || event.getSource() == txtAcptQty
                || event.getSource() == txtFreeQty) {
            calculate();
        }
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        event.stopPropagation();
        if (event.getSource() == txtFreeQty) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                ((CellSelectionModel) overHeadLine.getDmGrid().getGrid().getSelectionModel()).selectCell(STClient.getOHLFocusIndex(overHeadLine.getOverHeadList()), 0);
            }
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

        hsnCode = "";
        itemGSTNature = "";
        gstUnitCode = "";
        itsBasedOn = "";
        skuMrp = "";
        skuSaleRate = "";
        purchaseUnitName = "";
        purchaseUnitDecimalPlaces = "";
        purchaseUnitConversionRate = "";
        skuAllowDecimalPlaces = "";

        txtChallanNo.setText("");
        txtChallanNo.setTag("");
        txtChallanNo.setTag("");

        txtChallanDate.setText("");
        txtChallanDate.setTag("");
        txtChallanDate.setTag2nd("");

        txtOrderNo.setText("");
        txtOrderNo.setTag("");
        txtOrderNo.setTag2nd("");

        txtMedName.setText("");
        txtMedName.setTag("");
        txtMedName.setTag2nd("");

        txtPurchaseUnit.setText("");
        txtPurchaseUnit.setTag("");
        txtPurchaseUnit.setTag2nd("");

        txtDocQty.setText("");
        txtDocQty.setTag("");
        txtDocQty.setTag2nd("");

        txtAcptQty.setText("");
        txtAcptQty.setTag("");
        txtAcptQty.setTag2nd("");

        txtFreeQty.setText("");
        txtFreeQty.setTag("");
        txtFreeQty.setTag2nd("");

        txtRate.setText("");
        txtRate.setTag("");
        txtRate.setTag2nd("");

        txtAmount.setText("");
        txtAmount.setTag("");
        txtAmount.setTag2nd("");

        txtBatchNo.setText("");
        txtBatchNo.setTag("");
        txtBatchNo.setTag2nd("");

        txtBarcode.setText("");
        txtBarcode.setTag("");
        txtBarcode.setTag2nd("");

        txtMRP.setText("");
        txtMRP.setTag("");
        txtMRP.setTag2nd("");

        txtSaleRate.setText("");
        txtSaleRate.setTag("");
        txtSaleRate.setTag2nd("");

        txtExpDate.setText("");
        txtExpDate.setTag("");
        txtExpDate.setTag2nd("");

    }
    //</editor-fold>
}
