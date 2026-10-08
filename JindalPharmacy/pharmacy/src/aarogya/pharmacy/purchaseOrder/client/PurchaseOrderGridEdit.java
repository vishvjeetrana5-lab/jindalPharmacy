package aarogya.pharmacy.purchaseOrder.client;

import aarogya.pharmacy.purchaseOrder.shared.GrdIndentDetail_Mdl;
import aarogya.pharmacy.purchaseOrder.shared.GrdMedicineDetail_Mdl;
import aarogya.pharmacy.purchaseOrder.shared.TxtHMedicineName_Mdl;
import aarogya.pharmacy.indentSearch.client.IndentSearch;
import aarogya.pharmacy.medicineSearch.client.MedicineSearch;
import aarogya.pharmacy.medicineSearch.shared.MedicineSearchFilter_Mdl;
import aarogya.pharmacy.medicineSearch.shared.MedicineSearch_Mdl;
import aarogya.pharmacy.purchaseOrder.shared.GrdGrnPurchaseHistory_Mdl;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.List;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.dmToolkit.overHeadLine.client.OverHeadLine;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Shared;
import aarogya.pharmacy.purchaseOrder.shared.PurchaseOrder_Srv;
import aarogya.pharmacy.purchaseOrder.shared.PurchaseOrder_SrvAsync;
import aarogya.pharmacy.purchaseOrder.shared.Tolerance_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.client.STClient;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.overHeadLine.shared.OverHeadITS_Mdl;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Mdl;
import java.util.ArrayList;

public class PurchaseOrderGridEdit<T1, T2> extends PurchaseOrderGridEdit_Dsg<T1, T2> implements BlurHandler {

    interface CallBack {

        public void onOk(GrdMedicineDetail_Mdl opdService);

        public void onDelete();
    }

    private ServiceInvoker<PurchaseOrder_SrvAsync> srvAsync;
    private CallBack callBack;
    private PurchaseOrder orderEntry;
    private OverHeadLine overHeadLine;
    GrdMedicineDetail_Mdl parentRow;
    String mode = "", isBestBefore = "0", purchaseUnitName = "", purchaseUnitConversionRate = "",
            skuDecimalPlaces = "", selectedItemCode = "", indentRequired = "", itsBasedOn = "";
    final IndentSearch indentSearch = new IndentSearch();
    final IndentGridEdit indentGridEdit = new IndentGridEdit(gridIndentDetail);
    private int hashCode = 0;
    final TolerancePopup tolerancePopup = new TolerancePopup();
    private Tolerance_Mdl toleranceMdl = null;
    boolean isFirstShow = true;
    private final MedicineSearch medicineSearch = new MedicineSearch(null, null);
    private int intCurRowIndex = 0;
    boolean approvedRateYN = false;
    private String purchaseRate = "";

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void textHelpEvents() {
        txtRemark.addKeyDownHandler(new DmRemarkTextBox.TxtRemarkKeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event, Object objSource) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    ((CellSelectionModel) overHeadLine.getDmGrid().getGrid().getSelectionModel()).selectCell(STClient.getOHLFocusIndex(overHeadLine.getOverHeadList()), 0);
                }
            }
        });
        indentSearch.addCallBack(new IndentSearch.CallBack() {
            @Override
            public void ok(List<KeyFieldExtended> listKF) {
                List<KeyFieldExtended> kf = listKF;
                if (gridIndentDetail.getGrid().getStore().size() != 0) {
                    for (int i = 0; i < gridIndentDetail.getGrid().getStore().size(); i++) {
                        GrdIndentDetail_Mdl detail_Mdl = gridIndentDetail.getGrid().getStore().get(i);
                        for (KeyFieldExtended keyField : kf) {
                            if (DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase(keyField.getSharedKeyFieldCode())) {
                                new DmAlertMessageBox("Indent already exist.", txtMedicineName).show();
                                return;
                            }
                        }
                    }
                }
                fillIndentDetails(listKF);
            }

            @Override
            public void close() {
                txtMedicineName.setFocus(true);
            }
        });
        tolerancePopup.addCallBack(new TolerancePopup.CallBack() {
            @Override
            public void onOk(Tolerance_Mdl tMdl) {
                toleranceMdl = tMdl;
                txtTolerance.setText(DmShared.xNull(getToleranceString()));
                txtTolerance.setFocus(true);
            }

            @Override
            public void onClose() {
                txtTolerance.setFocus(true);
            }
        });
        aTolerance.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                showTolerancePopup();
            }
        });
        txtTolerance.addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_SPACE) {
                    showTolerancePopup();
                }
            }
        });
//        txtMedicineName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHMedicineName_Mdl>() {
//            @Override
//            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
//                srvAsync.invokeRPC(blnCallBackWait).getTxtHMedicine(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHMedicineName_Mdl>>>() {
//
//                    @Override
//                    public void onFailure(Throwable caught) {
//                        new DmAlertMessageBox(caught.getMessage(), txtMedicineName).show();
//                    }
//
//                    @Override
//                    public void onSuccess(DmResultPageing<List<TxtHMedicineName_Mdl>> result) {
//                        if (result.isStatus()) {
//                            txtMedicineName.setData(result.getData(), result.getPaging());
//                        } else {
//                            new DmAlertMessageBox(result.getErrorMessage(), txtMedicineName).show();
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void getSelectedRow(boolean blnStatus, TxtHMedicineName_Mdl t1Row) {
//                if (!DmShared.xNull(selectedItemCode).equalsIgnoreCase(DmShared.xNull(txtMedicineName.getTag()))) {
//                    resetItem();
//                    selectedItemCode = DmShared.xNull(txtMedicineName.getTag());
//                }
//                if (blnStatus && t1Row != null) {
//                    txtPurchaseUnit.setTag2nd(t1Row.getPurchaseUnitDecimalPlaces());
//                    txtPurchaseUnit.setTag(t1Row.getPurchaseUnitCode());
//                    txtPurchaseUnit.setText(t1Row.getPurchaseUnitDisplayName());
//                    purchaseUnitName = t1Row.getPurchaseUnitName();
//
//                    isBestBefore = DmShared.iNull(t1Row.getIsBestBefore()).toString();
//                    purchaseUnitConversionRate = DmShared.xNull(t1Row.getPurchaseUnitConversionRate());
//                    skuDecimalPlaces = DmShared.xNull(t1Row.getSkuDecimalPlaces());
//
//                    txtMedicineName.setTag2nd(t1Row.getItemProperty());
//                    calculate();
//                }
//            }
//        });

        txtMedicineName.addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_DELETE || event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE) {
                    txtMedicineName.setTag("");
                    txtMedicineName.setText("");
                    onMedSelection(true, new TxtHMedicineName_Mdl());
                } else if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER || event.getNativeKeyCode() == KeyCodes.KEY_INSERT
                        || event.getNativeKeyCode() == KeyCodes.KEY_RIGHT || event.getNativeKeyCode() == KeyCodes.KEY_LEFT
                        || event.getNativeKeyCode() == KeyCodes.KEY_UP || event.getNativeKeyCode() == KeyCodes.KEY_DOWN
                        || event.getNativeKeyCode() == KeyCodes.KEY_ALT || event.getNativeKeyCode() == KeyCodes.KEY_CTRL
                        || event.getNativeKeyCode() == KeyCodes.KEY_SHIFT || event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE
                        || event.isAltKeyDown() || event.isControlKeyDown() || event.isShiftKeyDown()) {

                } else {
                    MedicineSearchFilter_Mdl filter = new MedicineSearchFilter_Mdl();
                    filter.setMedicineCode(DmShared.xNull(txtMedicineName.getTag()));
                    filter.setV_Date(DmShared.dNull(orderEntry.txtVoucherDate.getText()));
                    if (!DmShared.xNull(indentRequired).equalsIgnoreCase("1")) {
                        medicineSearch.show(filter, String.valueOf((char) event.getNativeKeyCode()), orderEntry.txtHVoucherType.getTag());
                    }
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
                medMdl.setPurchaseUnitCode(medSearchMdl.getPurchaseUnitCode());
                medMdl.setPurchaseUnitName(medSearchMdl.getPurchaseUnitName());
                purchaseUnitName = (medSearchMdl.getPurchaseUnitName());
                purchaseRate = DmShared.xNull(medSearchMdl.getPurchaseRate());
                medMdl.setPurchaseUnitDisplayName(medSearchMdl.getPurchaseUnitName());
                if (!DmShared.xNull(medSearchMdl.getSkuCode()).equalsIgnoreCase(DmShared.xNull(medSearchMdl.getPurchaseUnitCode()))) {
                    medMdl.setPurchaseUnitDisplayName(DmShared.xNull(medSearchMdl.getPurchaseUnitName()) + " = " + DmShared.xNull(medSearchMdl.getPurchaseUnitConversionRate()) + " " + DmShared.xNull(medSearchMdl.getSkuName()));
                }
                medMdl.setPurchaseUnitConversionRate(medSearchMdl.getPurchaseUnitConversionRate());
                purchaseUnitConversionRate = medSearchMdl.getPurchaseUnitConversionRate();
                medMdl.setPurchaseUnitDecimalPlaces(medSearchMdl.getPurchaseUnitDecimalPlaces());
                medMdl.setSkuName(medSearchMdl.getSkuName());

                medMdl.setSkuDecimalPlaces(medSearchMdl.getSkuDecimalPlaces());
                medMdl.setItemProperty(medSearchMdl.getPropertyList());
                medMdl.setIsBestBefore(medSearchMdl.getIsBestBefore());

                medMdl.setlastPurchaseRate(medSearchMdl.getPurchaseRate());
                medMdl.setlastPurchaseDiscount(medSearchMdl.getPurchaseDiscountPer());

                txtPurchaseUnit.setTag2nd(medMdl.getPurchaseUnitDecimalPlaces());
                txtPurchaseUnit.setTag(medMdl.getPurchaseUnitCode());
                txtPurchaseUnit.setText(medMdl.getPurchaseUnitDisplayName());

                txtMedicineName.setTag(DmShared.xNull(medMdl.getSharedKeyFieldCode()));
                txtMedicineName.setText(DmShared.xNull(medMdl.getSharedKeyFieldName()));
                txtMedCompany.setTag(DmShared.xNull(medSearchMdl.getMakeCode()));
                txtMedCompany.setText(DmShared.xNull(medSearchMdl.getMakeName()));
                txtRate.setText(DmClient.vNull(medMdl.getlastPurchaseRate(), "0.00"));
                itsBasedOn = DmShared.iNull(medSearchMdl.getItsBasedOn()).toString();
                if (DmShared.vNull(medSearchMdl.getApprovedPurchaseRate()).doubleValue() > 0) {
                    txtRate.setText(DmClient.vNull(medSearchMdl.getApprovedPurchaseRate(), "0.00"));
                    approvedRateYN = true;
                    txtRate.setEnabled(false);
                } else {
                    fetchLastRate(DmShared.xNull(medMdl.getSharedKeyFieldCode()));
                    approvedRateYN = false;
                    txtRate.setEnabled(true);
                }
                if (DmShared.vNull(medSearchMdl.getApprovedMRP()).doubleValue() > 0) {
                    txtMRP.setText(DmClient.vNull(medSearchMdl.getApprovedMRP(), "0.00"));

                    txtMRP.setEnabled(false);
                } else {

                    txtMRP.setEnabled(true);
                }

                txtRemark.setText(medSearchMdl.getRemark());
                OverHeadLine_Mdl ohlDiscount = OverHeadLine_Shared.getOHByUniqueId("DS", overHeadLine.getOverHeadList());
                if (!orderEntry.defaultValue.getRequiredLastBillDiscountInPO().trim().equalsIgnoreCase("0")) {
                    ohlDiscount.setAtRate(DmClient.vNull(medMdl.getlastPurchaseDiscount(), "0.00"));
                }
                ohlDiscount.setAmount("");
                List<OverHeadLine_Mdl> lstOhl = new ArrayList<>();
                lstOhl.add(ohlDiscount);
                orderEntry.ohlHandler.setReference(lstOhl, overHeadLine.getOverHeadList(true));
                setIts(txtMedicineName.getTag(), txtRate.getText(), orderEntry.location, itsBasedOn, orderEntry.txtVoucherDate.getText());
//                overHeadLine.getDmGrid().commitChanges();
//
                getCDConfiguration(DmShared.xNull(medMdl.getSharedKeyFieldCode()));

                calculate();
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        txtMedicineName.setFocus(true);
                    }
                });
            }

            @Override
            public void close() {
                txtMedicineName.setFocus(true);
            }

        });

    }

    private void onMedSelection(boolean blnStatus, TxtHMedicineName_Mdl t1Row) {
        if (!DmShared.xNull(selectedItemCode).equalsIgnoreCase(DmShared.xNull(txtMedicineName.getTag()))) {
            resetItem();
            selectedItemCode = DmShared.xNull(txtMedicineName.getTag());
        }

        if (blnStatus && t1Row != null) {
            txtPurchaseUnit.setTag2nd(t1Row.getPurchaseUnitDecimalPlaces());
            txtPurchaseUnit.setTag(t1Row.getPurchaseUnitCode());
            txtPurchaseUnit.setText(t1Row.getPurchaseUnitDisplayName());
            purchaseUnitName = t1Row.getPurchaseUnitName();
            purchaseRate = DmShared.xNull(t1Row.getPurchaseRate());

            isBestBefore = DmShared.iNull(t1Row.getIsBestBefore()).toString();
            purchaseUnitConversionRate = DmShared.xNull(t1Row.getPurchaseUnitConversionRate());
            skuDecimalPlaces = DmShared.xNull(t1Row.getSkuDecimalPlaces());

            txtMedicineName.setTag2nd(t1Row.getItemProperty());
            calculate();
            reQuery();
        }
    }

    private void gridEvents() {
        indentGridEdit.addCallBack(new IndentGridEdit.CallBack() {

            @Override
            public void onOk(GrdIndentDetail_Mdl grdIndentEntry) {
                gridIndentDetail.commitChanges();
                calculateOdrQty();
            }

            @Override
            public void onDelete() {
                gridIndentDetail.commitChanges();
                calculateOdrQty();
            }
        });

        gridIndentDetail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                showPurchaseOrderEntryGridEdit(event.getRowIndex());
            }
        });

        gridIndentDetail.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    showPurchaseOrderEntryGridEdit(gridIndentDetail.getGrid().getStore().indexOf(gridIndentDetail.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());

        grdPrevGrnPurchase.addCallBack(new DmGridPaging.CallBack<String>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, String param) {
                srvAsync.invokeRPC(blnToEndOfFile).getPrevGrn(txtMedicineName.getTag(),
                        cbCurrentSupplier.getValue() || cbOtherSuppliers.getValue() ? orderEntry.txtHSupplier.getTag() : "",
                        cbCurrentSupplier.getValue(),
                        helper, blnToEndOfFile, param, new AsyncCallback<DmResultPageing<List<GrdGrnPurchaseHistory_Mdl>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GrdGrnPurchaseHistory_Mdl>> result) {
                        if (result.isStatus() && result.getData() != null) {
                            grdPrevGrnPurchase.setData(result.getData(), result.getPaging());
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

    private void showPurchaseOrderEntryGridEdit(int currentRowIndex) {
        indentGridEdit.show(currentRowIndex, true, true, true, mode);
        intCurRowIndex = currentRowIndex;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, PurchaseOrder orderEntry, String isMode, String indentRequired) {
        intCurRowIndex = currentRowIndex;
        resetPopup();
        this.orderEntry = orderEntry;
        this.indentRequired = indentRequired;
        mode = isMode;
        overHeadLine = this.orderEntry.ohlHandler.getWidget();
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
                txtMedicineName.setFocus(true);
                reQuery();
            }
        });
    }

    private void showTolerancePopup() {
        if (DmShared.vNull(txtQuantity.getText()) > 0) {
            tolerancePopup.show(PurchaseOrderGridEdit.this, toleranceMdl);
        } else {
            new DmAlertMessageBox("Please fill Order Qty.", txtQuantity).show();
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtRequiredDate) {
            txtRequiredDate.setText(DmClient.returnDate(txtRequiredDate.getText()));
        }
        if ((event.getSource() == txtQuantity) || (event.getSource() == txtRate)) {
//            setIts(txtHMedName.getTag(), txtRate.getText(), grnEntry.location, itsBasedOn, grnEntry.txtChallanDate.getText());
            calculate();
        }
    }

    private String getToleranceString() {
        String tolerance = "";
        if (toleranceMdl != null) {
            if (DmShared.vNull(toleranceMdl.getTl_ExcessQty()) != 0 || DmShared.vNull(toleranceMdl.getTl_ExcessQty()) != 0) {
                tolerance = "+" + DmShared.xNull(toleranceMdl.getTl_ExcessQty()) + "; -" + DmShared.xNull(toleranceMdl.getTl_ShortageQty()) + ";";
            }
        }
        return tolerance;
    }

    @Override
    public void displayRow(T1 row) {
        GrdMedicineDetail_Mdl ourRow = (GrdMedicineDetail_Mdl) row;
        parentRow = new GrdMedicineDetail_Mdl();

        parentRow = ourRow;

        selectedItemCode = ourRow.getSharedKeyFieldCode();
        txtMedicineName.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtMedicineName.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtMedicineName.setTag2nd(DmShared.xNull(ourRow.getItemProperty()));
        txtMedCompany.setTag(DmShared.xNull(ourRow.getMedicineCompanyCode()));
        txtMedCompany.setText(DmShared.xNull(ourRow.getMedicineCompanyName()));
        isBestBefore = DmShared.iNull(ourRow.getIsBestBefore()).toString();
        approvedRateYN = (DmShared.xNull(ourRow.getApprovedRateYN()).trim().equalsIgnoreCase("1") ? true : false);

        purchaseUnitConversionRate = DmShared.xNull(ourRow.getPurchaseUnitConversionRate());
        skuDecimalPlaces = DmShared.xNull(ourRow.getSkuDecimalPlaces());

        txtPurchaseUnit.setTag(DmShared.xNull(ourRow.getPurchaseUnitCode()));
        txtPurchaseUnit.setText(DmShared.xNull(ourRow.getPurchaseUnitDisplayName()));
        txtPurchaseUnit.setTag2nd(DmShared.xNull(ourRow.getPurchaseUnitDecimalPlaces()));
        purchaseUnitName = DmShared.xNull(ourRow.getPurchaseUnitName());
        purchaseRate = DmShared.xNull(ourRow.getPurchaseRate());
        txtRequiredDate.setText(DmShared.dNull(ourRow.getReqDate()));
        txtRemark.setText(DmShared.xNull(ourRow.getRemark()));
        txtRate.setText(DmClient.vNull(ourRow.getRate(), "0.00"));
        txtAmount.setText(DmClient.vNull(ourRow.getAmount(), "0.00"));
        txtMRP.setText(DmClient.vNull(ourRow.getMrp(), "0.00"));
        txtMRP.setText(DmClient.vNull(ourRow.getMrp(), "0.00"));

        txtQuantity.setText(DmShared.xNull(ourRow.getOrderQuantity()));

        txtQuantity.setTag(DmShared.xNull(ourRow.getSkuQty()));

        toleranceMdl = new Tolerance_Mdl();
        toleranceMdl.setOrderQuantity(txtQuantity.getText());
        toleranceMdl.setOrderQuantityWithExcess(ourRow.getToleranceMdl().getOrderQuantityWithExcess());
        toleranceMdl.setOrderQuantityWithShortage(ourRow.getToleranceMdl().getOrderQuantityWithShortage());
        toleranceMdl.setTl_ExcessPer(ourRow.getToleranceMdl().getTl_ExcessPer());
        toleranceMdl.setTl_ExcessQty(ourRow.getToleranceMdl().getTl_ExcessQty());
        toleranceMdl.setTl_ShortagePer(ourRow.getToleranceMdl().getTl_ShortagePer());
        toleranceMdl.setTl_ShortageQty(ourRow.getToleranceMdl().getTl_ShortageQty());
        txtTolerance.setText(DmShared.xNull(getToleranceString()));

        gridIndentDetail.getGrid().getStore().clear();
        gridIndentDetail.getGrid().getStore().addAll(ourRow.getListIndentDetail());

        overHeadLine.initialize(this.getCurrentRowIndex());
        hashCode = ourRow.getSharedKeyFieldHashCode();
        if (!DmShared.xNull(selectedItemCode).trim().equalsIgnoreCase("")) {
            fetchLastRate(DmShared.xNull(selectedItemCode));
        }
        manageDisplay();
        txtMedicineName.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdMedicineDetail_Mdl();
    }

    @Override
    public void onOk() {
        try {
            calculate();
            if (DmShared.xNull(parentRow.getIsStatus()).equalsIgnoreCase("1")) {
                new DmAlertMessageBox("Record has been canceled can't change record.", txtMedicineName).show();
                return;
            }
            if (DmShared.xNull(parentRow.getIsStatus()).equalsIgnoreCase("2")) {
                new DmAlertMessageBox("Record has been completed, can't change record.", txtMedicineName).show();
                return;
            }
            if (!DmShared.xNull(parentRow.getChallanQyt()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Medicine is used, can't change record.", txtMedicineName).show();
                return;
            }

            if (DmShared.xNull(txtMedicineName.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Medicine is mandatory.", txtMedicineName).show();
                return;
            }
            if (DmShared.vNull(txtQuantity.getText()).doubleValue() == 0) {
                new DmAlertMessageBox("Quantity is mandatory.", txtQuantity).show();
                return;
            }
            if (DmShared.xNull(txtPurchaseUnit.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Unit is mandatory.", txtPurchaseUnit).show();
                return;
            }
            if (DmShared.vNull(txtRate.getText()).doubleValue() == 0) {
                new DmAlertMessageBox("Rate is mandatory.", txtRate).show();
                return;
            }
            for (GrdMedicineDetail_Mdl detail_Mdl : (List<GrdMedicineDetail_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
                if (DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(txtMedicineName.getTag()))
                        && DmShared.dNull(txtRequiredDate.getText()).equalsIgnoreCase(DmShared.dNull(detail_Mdl.getReqDate()))
                        && DmShared.vNull(txtRate.getText()).doubleValue() == DmShared.vNull(detail_Mdl.getRate()).doubleValue()) {
                    if (!DmClient.vNull(detail_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                        String message = "<div style='float:right; margin-right: 5px;'>";
                        message += "Record already exist with following details - <br>";
                        message += (DmShared.xNull(txtMedicineName.getText()).trim().equals("")) ? "" : "<b>Medicine:</b> " + DmShared.xNull(txtMedicineName.getText()) + "<br>";
                        message += (DmShared.vNull(txtRate.getText()) == 0) ? "" : "<b>MRP:</b> " + DmClient.vNull(txtRate.getText(), "0.00") + "<br>";
                        message += (DmShared.dNull(txtRequiredDate.getText()).trim().equals("")) ? "" : "<b>Manufacturing Date:</b> " + DmShared.dNull(txtRequiredDate.getText()) + "<br>";
                        message += "</div>";
                        new DmAlertMessageBox(message, txtMedicineName).show();
                        return;
                    }
                }
            }
            double indentOrderSkuQuantity = 0;
            String decimalFormat = DmShared.getNumberFormat(DmShared.iNull(txtPurchaseUnit.getTag2nd()).intValue());
            for (GrdIndentDetail_Mdl detail_Mdl : gridIndentDetail.getGrid().getStore().getAll()) {
                if (!DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                    indentOrderSkuQuantity += DmShared.vNull(detail_Mdl.getOrderQty()).doubleValue();
                }
            }
            indentOrderSkuQuantity = indentOrderSkuQuantity / DmShared.vNull(purchaseUnitConversionRate);
            indentOrderSkuQuantity = DmShared.vNull(DmClient.vNull(indentOrderSkuQuantity, decimalFormat));
            if (indentOrderSkuQuantity != 0) {
                if (DmShared.vNull(txtQuantity.getText()).doubleValue() < DmShared.vNull(indentOrderSkuQuantity).doubleValue()) {
                    new DmAlertMessageBox("Order quantity should not be less than indent's total order sku quantity.", txtQuantity).show();
                    return;
                }
            }

            OverHeadLine_Shared.chkGridSingleOH(overHeadLine.getOverHeadList());
            proceedOk();
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtMedicineName).show();
        }
    }

    @Override
    public void manageOk(T1 row) {
        GrdMedicineDetail_Mdl ourRow = (GrdMedicineDetail_Mdl) row;

        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtMedicineName.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtMedicineName.getText()));

        ourRow.setItemProperty(DmShared.xNull(txtMedicineName.getTag2nd()));
        ourRow.setIsBestBefore(DmShared.iNull(isBestBefore).toString());

        ourRow.setPurchaseUnitCode(DmShared.xNull(txtPurchaseUnit.getTag()));
        ourRow.setPurchaseUnitDisplayName(DmShared.xNull(txtPurchaseUnit.getText()));
        ourRow.setPurchaseUnitDecimalPlaces(DmShared.xNull(txtPurchaseUnit.getTag2nd()));
        ourRow.setPurchaseUnitName(DmShared.xNull(purchaseUnitName));
        ourRow.setPurchaseRate(DmShared.xNull(purchaseRate));
        ourRow.setPurchaseUnitConversionRate(DmShared.xNull(purchaseUnitConversionRate));
        ourRow.setSkuDecimalPlaces(DmShared.xNull(skuDecimalPlaces));

        ourRow.setReqDate(DmShared.dNull(txtRequiredDate.getText()));
        ourRow.setOrderQuantity(DmShared.xNull(txtQuantity.getText()));
        ourRow.setSkuQty(DmShared.xNull(txtQuantity.getTag()));

        ourRow.getToleranceMdl().setOrderQuantity(toleranceMdl.getOrderQuantity());
        ourRow.getToleranceMdl().setOrderQuantityWithExcess(toleranceMdl.getOrderQuantityWithExcess());
        ourRow.getToleranceMdl().setOrderQuantityWithShortage(toleranceMdl.getOrderQuantityWithShortage());
        ourRow.getToleranceMdl().setTl_ExcessPer(toleranceMdl.getTl_ExcessPer());
        ourRow.getToleranceMdl().setTl_ExcessQty(toleranceMdl.getTl_ExcessQty());
        ourRow.getToleranceMdl().setTl_ShortagePer(toleranceMdl.getTl_ShortagePer());
        ourRow.getToleranceMdl().setTl_ShortageQty(toleranceMdl.getTl_ShortageQty());

        ourRow.setRate(DmClient.vNull(txtRate.getText(), "0.00"));
        ourRow.setAmount(DmClient.vNull(txtAmount.getText(), "0.00"));
        ourRow.setMrp(DmClient.vNull(txtMRP.getText(), "0.00"));
        ourRow.setRemark(DmShared.xNull(txtRemark.getText()));
        ourRow.setApprovedRateYN(approvedRateYN ? "1" : "0");
        ourRow.setMedicineCompanyCode(DmShared.xNull(txtMedCompany.getTag()));
        ourRow.setMedicineCompanyName(DmShared.xNull(txtMedCompany.getText()));

        ourRow.getListIndentDetail().clear();
        ourRow.getListIndentDetail().addAll(gridIndentDetail.getGrid().getStore().getAll());

        ourRow.setOHLList(overHeadLine.getOverHeadList());

        if (DmShared.vNull(txtRate.getTag()) > 0 && DmShared.vNull(txtRate.getTag()).doubleValue() > DmShared.vNull(txtRate.getText()).doubleValue()) {
            ourRow.setLastRateComparison("3");// Low Rate from Previous
            ourRow.setLastRateComparisonName("LR");// Low Rate from Previous
        } else if (DmShared.vNull(txtRate.getTag()) > 0 && DmShared.vNull(txtRate.getTag()).doubleValue() < DmShared.vNull(txtRate.getText()).doubleValue()) {
            ourRow.setLastRateComparison("2");// Heigher Rate from Previous
            ourRow.setLastRateComparisonName("HR");// Heigher Rate from Previous
        } else if (DmShared.vNull(txtRate.getTag()) > 0 && (DmShared.vNull(txtRate.getTag()).doubleValue() == DmShared.vNull(txtRate.getText()).doubleValue())) {
            ourRow.setLastRateComparison("1");// Same Rate from Previous
            ourRow.setLastRateComparisonName("SR");// Same Rate from Previous
        } else if (DmShared.vNull(txtRate.getTag()) == 0) {
            ourRow.setLastRateComparison("0");// New Purchase
            ourRow.setLastRateComparisonName("NP");// New Purchase
        }

        callBack.onOk(ourRow);
    }

    @Override
    public void onNew() {
        proceedNew();
    }

    @Override
    public void onDelete() {
        if (DmShared.xNull(parentRow.getIsStatus()).equalsIgnoreCase("1")) {
            new DmAlertMessageBox("Record has been canceled can't delete record.", txtMedicineName).show();
            return;
        } else if (DmShared.xNull(parentRow.getIsStatus()).equalsIgnoreCase("2")) {
            new DmAlertMessageBox("Record has been completed, can't change record.", txtMedicineName).show();
            return;
        } else if (!DmShared.xNull(parentRow.getChallanQyt()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Medicine is used, can't change record.", txtMedicineName).show();
            return;
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

    public PurchaseOrderGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Medicine Detail", "770px", "499px");
        srvAsync = new ServiceInvoker<PurchaseOrder_SrvAsync>(GWT.create(PurchaseOrder_Srv.class), PurchaseOrder.class.getName());
        textHelpEvents();
        gridEvents();
        manageEvents();
        showIndentSearch();
    }

    private void manageDisplay() {
        txtAmount.setEnabled(false);
        txtMRP.setEnabled(false);
        txtMedCompany.setEnabled(false);
        flwAddIndent.clear();
        if (!DmShared.xNull(mode).equalsIgnoreCase("Browse")) {
            flwAddIndent.add(btnAddIndent);
        }
        if (parentRow != null && !DmShared.xNull(parentRow.getQuotationDocId()).equalsIgnoreCase("")) {
            txtRate.setEnabled(false);
            txtMedicineName.setEnabled(false);
        } else if (approvedRateYN) {
            txtRate.setEnabled(false);
        } else {
            txtRate.setEnabled(true);
            if (gridIndentDetail.getGrid().getStore().getAll().size() > 0) {
                txtMedicineName.setEnabled(false);
            } else {
                txtMedicineName.setEnabled(true);
            }
        }
    }

    private void showIndentSearch() {
        btnAddIndent.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (!DmShared.xNull(txtMedicineName.getTag()).trim().equalsIgnoreCase("")) {
                    indentSearch.show(txtMedicineName.getTag(), !DmShared.dNull(orderEntry.txtRefDate.getText()).equalsIgnoreCase("") ? orderEntry.txtRefDate.getText() : orderEntry.txtVoucherDate.getText(), new ProjectVoucherCategory().PHARMACY_PURCHASE_ORDER);
                } else {
                    new DmAlertMessageBox("Please select Item.", txtMedicineName).show();
                }
            }
        });
    }

    private void fillIndentDetails(List<KeyFieldExtended> listKF) {

        srvAsync.invokeRPC().getIndentDetail(listKF, DmShared.xNull(txtMedicineName.getTag()), new AsyncCallback<DmResult<List<GrdIndentDetail_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtMedicineName).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdIndentDetail_Mdl>> result) {
                if (result.isStatus()) {
                    if (result.isStatus()) {
//                        gridIndentDetail.getGrid().getStore().clear();
                        gridIndentDetail.getGrid().getStore().addAll(result.getData());
                        calculateQty();
                        txtMedicineName.setFocus(true);
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), txtMedicineName).show();
                    }
                }
            }
        });
    }

    private void calculate() {
        txtQuantity.setText(DmClient.vNull(txtQuantity.getText(), DmShared.getNumberFormat(DmShared.iNull(txtPurchaseUnit.getTag2nd()).intValue())));
        txtQuantity.setTag(DmClient.vNull(DmShared.vNull(txtQuantity.getText()) * DmShared.vNull(purchaseUnitConversionRate), DmShared.getNumberFormat(DmShared.iNull(skuDecimalPlaces).intValue())));
        txtAmount.setText(DmClient.vNull(DmShared.vNull(txtQuantity.getText()).doubleValue() * DmShared.vNull(txtRate.getText()).doubleValue(), "0.00"));
        overHeadLine.getDmGrid().commitChanges();
        overHeadLine.calculate(txtAmount.getText());
        calculateTolerance();
    }

    private void calculateQty() {
        double quantity = 0;
        for (GrdIndentDetail_Mdl detail_Mdl : gridIndentDetail.getGrid().getStore().getAll()) {
            if (!DmShared.xNull(detail_Mdl.getIndentQty()).trim().equalsIgnoreCase("")) {
                quantity += DmShared.vNull(detail_Mdl.getIndentQty()).doubleValue();
            }
        }

        txtQuantity.setText(DmShared.vNull(quantity / DmShared.vNull(purchaseUnitConversionRate)).toString());
        calculate();
    }

    private void calculateOdrQty() {
        double quantity = 0;
        for (GrdIndentDetail_Mdl detail_Mdl : gridIndentDetail.getGrid().getStore().getAll()) {
            if (!DmShared.xNull(detail_Mdl.getOrderQty()).trim().equalsIgnoreCase("")) {
                quantity += DmShared.vNull(detail_Mdl.getOrderQty()).doubleValue();
            }
        }
        txtQuantity.setText(DmShared.vNull(quantity / DmShared.vNull(purchaseUnitConversionRate)).toString());
        calculate();
    }

    private void calculateTolerance() {
        if (toleranceMdl != null) {
            toleranceMdl.setOrderQuantity(txtQuantity.getText());

            double orderQty = DmShared.vNull(toleranceMdl.getOrderQuantity());
            double tl_ExcessPer = DmShared.vNull(toleranceMdl.getTl_ExcessPer());
            double tl_ShortagePer = DmShared.vNull(toleranceMdl.getTl_ShortagePer());
            String purchaseUnitDecimalFormat = DmShared.getNumberFormat(DmShared.iNull(txtPurchaseUnit.getTag2nd()).intValue());

            if (tl_ExcessPer > 0) {
                toleranceMdl.setTl_ExcessQty(DmShared.vNull(orderQty * (tl_ExcessPer * 0.01)).toString());
            }
            toleranceMdl.setTl_ExcessQty(DmClient.vNull(toleranceMdl.getTl_ExcessQty(), purchaseUnitDecimalFormat));

            if (tl_ShortagePer > 0) {
                toleranceMdl.setTl_ShortageQty(DmShared.vNull(orderQty * (tl_ShortagePer * 0.01)).toString());
            }
            toleranceMdl.setTl_ShortageQty(DmClient.vNull(toleranceMdl.getTl_ShortageQty(), purchaseUnitDecimalFormat));

            double tl_ExcessQty = DmShared.vNull(toleranceMdl.getTl_ExcessQty());
            double tl_ShortageQty = DmShared.vNull(toleranceMdl.getTl_ShortageQty());

            if (tl_ExcessQty > 0 || tl_ShortageQty > 0) {
                toleranceMdl.setOrderQuantityWithExcess(DmClient.vNull(orderQty + tl_ExcessQty, purchaseUnitDecimalFormat));
                toleranceMdl.setOrderQuantityWithShortage(DmClient.vNull(orderQty - tl_ShortageQty, purchaseUnitDecimalFormat));
            }

            txtTolerance.setText(getToleranceString());
        }
    }

    private void resetPopup() {
        mode = "";

        selectedItemCode = "";
        txtMedicineName.setText("");
        txtMedicineName.setTag("");
        txtMedicineName.setTag2nd("");
        resetItem();

    }

    private void resetItem() {
        isBestBefore = "";
        purchaseUnitName = "";
        purchaseRate = "";
        purchaseUnitConversionRate = "";
        skuDecimalPlaces = "";

        txtPurchaseUnit.setText("");
        txtPurchaseUnit.setTag("");
        txtPurchaseUnit.setTag2nd("");

        txtQuantity.setText("");
        txtQuantity.setTag("");
        txtQuantity.setTag2nd("");

        txtTolerance.setText("");
        txtTolerance.setTag("");
        txtTolerance.setTag2nd("");
        toleranceMdl = new Tolerance_Mdl();

        txtRate.setText("");
        txtRate.setTag("");
        txtRate.setTag2nd("");

        txtAmount.setText("");
        txtAmount.setTag("");
        txtAmount.setTag2nd("");

        txtMRP.setText("");
        txtMRP.setTag("");
        txtMRP.setTag2nd("");

        txtRequiredDate.setText("");
        txtRequiredDate.setTag("");
        txtRequiredDate.setTag2nd("");

        txtRemark.setText("");
        txtRemark.setTag("");
        txtRemark.setTag2nd("");

        gridIndentDetail.getGrid().getStore().clear();
        grdPrevGrnPurchase.getDmGrid().getGrid().getStore().clear();
        cbCurrentSupplier.setValue(false);
        cbOtherSuppliers.setValue(false);
    }

    private void reQuery() {
        grdPrevGrnPurchase.getHelper().setRowsStartFrom(0);
        grdPrevGrnPurchase.reQuery();
    }

    private void manageEvents() {
        txtRequiredDate.addBlurHandler(this);
        txtQuantity.addBlurHandler(this);
        txtRate.addBlurHandler(this);
        cbCurrentSupplier.addClickHandler(this);
        cbOtherSuppliers.addClickHandler(this);
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == cbCurrentSupplier) {
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

    private void fetchLastRate(String itemCode) {
        srvAsync.invokeRPC(true).getLastRate(itemCode, new AsyncCallback<DmResult<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtMedicineName).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    txtRate.setTag(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtMedicineName).show();
                }
            }
        });
    }

    private void setIts(final String itemCode, final String itemRate, final String location, final String itsBasedOn, final String billDate) {
        srvAsync.invokeRPC(true).getITS(itemCode, itemRate, location, itsBasedOn, billDate, new AsyncCallback<DmResult<List<OverHeadITS_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtMedicineName).show();
            }

            @Override
            public void onSuccess(DmResult<List<OverHeadITS_Mdl>> result) {
                if (result.isStatus()) {
                    overHeadLine.setITS(result.getData());
                    calculate();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtMedicineName).show();
                }
            }
        });
    }

    private void getCDConfiguration(String medicineCode) {

        srvAsync.invokeRPC().getCDConfiguration(DmShared.xNull(orderEntry.txtHSupplier.getTag()), DmShared.xNull(medicineCode), new AsyncCallback<DmResult<String>>() {

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
