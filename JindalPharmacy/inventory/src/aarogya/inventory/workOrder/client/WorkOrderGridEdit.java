package aarogya.inventory.workOrder.client;

import aarogya.inventory.workOrder.shared.GrdIndentDetail_Mdl;
import aarogya.inventory.workOrder.shared.GrdItemDetail_Mdl;
import aarogya.inventory.workOrder.shared.TxtHItemName_Mdl;
import aarogya.inventory.indentSearch.client.IndentSearch;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
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
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.overHeadLine.client.OverHeadLine;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Shared;
import aarogya.inventory.workOrder.shared.WorkOrder_Vld;
import aarogya.inventory.workOrder.shared.Tolerance_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.itemPropertyPopup.client.ItemPropertyPopup;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemPropertyPopup_Vld;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import aarogya.selfToolkit.stStatic.client.STClient;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import aarogya.inventory.workOrder.shared.WorkOrder_Srv;
import aarogya.inventory.workOrder.shared.WorkOrder_SrvAsync;
import aarogya.selfToolkit.stStatic.shared.STEnum;

public class WorkOrderGridEdit<T1, T2> extends WorkOrderGridEdit_Dsg<T1, T2> implements BlurHandler {

    interface CallBack {

        public void onOk(GrdItemDetail_Mdl opdService);

        public void onDelete();
    }

    private ServiceInvoker<WorkOrder_SrvAsync> srvAsync;
    private CallBack callBack;
    private WorkOrder orderEntry;
    private OverHeadLine overHeadLine;
    GrdItemDetail_Mdl parentRow;
    String mode = "", isBestBefore = "0", purchaseUnitName = "", purchaseUnitConversionRate = "", skuName = "",
            skuDecimalPlaces = "", selectedItemCode = "";
    final IndentSearch indentSearch = new IndentSearch();
    final IndentGridEdit indentGridEdit = new IndentGridEdit(gridIndentDetail);
    private int hashCode = 0;
    final TolerancePopup tolerancePopup = new TolerancePopup();
    private Tolerance_Mdl toleranceMdl = null;
    final ItemPropertyPopup propertyPopup = new ItemPropertyPopup();
    private ItemProperty_Mdl propertyPopup_Mdl = new ItemProperty_Mdl();
    boolean isFirstShow = true;
    boolean blnShowStockable = false;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void textHelpEvents() {
        indentSearch.addCallBack(new IndentSearch.CallBack() {
            @Override
            public void ok(List<KeyFieldExtended> listKF) {
                List<KeyFieldExtended> kf = listKF;
                if (gridIndentDetail.getGrid().getStore().size() != 0) {
                    for (int i = 0; i < gridIndentDetail.getGrid().getStore().size(); i++) {
                        GrdIndentDetail_Mdl detail_Mdl = gridIndentDetail.getGrid().getStore().get(i);
                        for (KeyFieldExtended keyField : kf) {
                            if (DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase(keyField.getSharedKeyFieldCode())) {
                                new DmAlertMessageBox("Indent already exist.", txtHItemName).show();
                                return;
                            }
                        }
                    }
                }
                fillIndentDetails(listKF);
            }

            @Override
            public void close() {
                if (txtHItemName.isEnabled()) {
                    txtHItemName.setFocus(true);
                } else {
                    txtQuantity.setFocus(true);
                }
            }
        });
        propertyPopup.addCallBack(new ItemPropertyPopup.CallBack() {
            @Override
            public void onOk(ItemProperty_Mdl property_Mdl) {
                propertyPopup_Mdl = property_Mdl;
                txtProperty.setText(DmShared.xNull(propertyPopup_Mdl.getColourCode()));
                txtProperty.setTag(DmShared.xNull(propertyPopup_Mdl.getSizeCode()));
                txtProperties.setText(DmShared.xNull(propertyPopup_Mdl.getPropertiesCsv()));
                txtProperties.setFocus(true);
            }

            @Override
            public void onClose() {
                txtProperties.setFocus(true);
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

        txtProperties.addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_SPACE) {
                    showPropertyPopUp();
                }
            }
        });
        aProperties.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                showPropertyPopUp();
            }
        });
        txtProperties.addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    if (!DmShared.xNull(txtHItemName.getTag2nd()).trim().equalsIgnoreCase("")) {
                        try {
                            ItemPropertyPopup_Vld.validateProperties(WorkOrder_Vld.getPOApplicableProperties(txtHItemName.getTag2nd()), isBestBefore, orderEntry.txtVoucherDate.getText(), propertyPopup_Mdl);
                        } catch (DmException e) {
                            showPropertyPopUp();
                        }
                    }
                }
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
        txtHItemName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHItemName_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHItem(helper, blnToEndOfFile, blnShowStockable, new AsyncCallback<DmResultPageing<List<TxtHItemName_Mdl>>>() {

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
                if (!DmShared.xNull(selectedItemCode).equalsIgnoreCase(DmShared.xNull(txtHItemName.getTag()))) {
                    resetItem();
                    selectedItemCode = DmShared.xNull(txtHItemName.getTag());
                }
                if (blnStatus && t1Row != null) {
                    txtPurchaseUnit.setTag2nd(t1Row.getPurchaseUnitDecimalPlaces());
                    txtPurchaseUnit.setTag(t1Row.getPurchaseUnitCode());
                    txtPurchaseUnit.setText(t1Row.getPurchaseUnitDisplayName());
                    purchaseUnitName = t1Row.getPurchaseUnitName();

                    isBestBefore = DmShared.iNull(t1Row.getIsBestBefore()).toString();
                    purchaseUnitConversionRate = DmShared.xNull(t1Row.getPurchaseUnitConversionRate());
                    skuDecimalPlaces = DmShared.xNull(t1Row.getSkuDecimalPlaces());
                    skuName = DmShared.xNull(t1Row.getSkuName());

                    txtHItemName.setTag2nd(t1Row.getItemProperty());
                    showPropertyPopUp();
                    calculate();
                }
            }
        });
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
    }

    private void showPurchaseOrderEntryGridEdit(int currentRowIndex) {
        indentGridEdit.show(currentRowIndex, true, true, true, mode);
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, WorkOrder orderEntry, String isMode, boolean blnShowStockable) {
        resetPopup();
        this.orderEntry = orderEntry;
        this.blnShowStockable = blnShowStockable;
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
                if (txtHItemName.isEnabled()) {
                    txtHItemName.setFocus(true);
                } else {
                    txtQuantity.setFocus(true);
                }
            }
        });
    }

    private void showPropertyPopUp() {
        String poApplicableProperties = WorkOrder_Vld.getPOApplicableProperties(txtHItemName.getTag2nd());
        if (!DmShared.xNull(txtHItemName.getTag()).equalsIgnoreCase("")) {
            if (!DmShared.xNull(poApplicableProperties).equalsIgnoreCase("")) {
                txtProperties.setEnabled(true);
                propertyPopup_Mdl.setSkuName(skuName);
                propertyPopup_Mdl.setSkuQtyForRate(purchaseUnitConversionRate);
              propertyPopup.show(mode, poApplicableProperties, isBestBefore, propertyPopup_Mdl, orderEntry.txtVoucherDate.getText(), false,false,STEnum.ItemBatchType.NON_EXPIRED_BATCH);
            } else {
                txtProperties.setEnabled(false);
            }
        }
    }

    private void showTolerancePopup() {
        if (DmShared.vNull(txtQuantity.getText()) > 0) {
            tolerancePopup.show(WorkOrderGridEdit.this, toleranceMdl);
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
        GrdItemDetail_Mdl ourRow = (GrdItemDetail_Mdl) row;
        parentRow = new GrdItemDetail_Mdl();

        parentRow = ourRow;

        selectedItemCode = ourRow.getSharedKeyFieldCode();
        txtHItemName.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHItemName.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtHItemName.setTag2nd(DmShared.xNull(ourRow.getItemProperty()));
        isBestBefore = DmShared.iNull(ourRow.getIsBestBefore()).toString();
        purchaseUnitConversionRate = DmShared.xNull(ourRow.getPurchaseUnitConversionRate());
        skuDecimalPlaces = DmShared.xNull(ourRow.getSkuDecimalPlaces());
        skuName = DmShared.xNull(ourRow.getSkuName());

        txtPurchaseUnit.setTag(DmShared.xNull(ourRow.getPurchaseUnitCode()));
        txtPurchaseUnit.setText(DmShared.xNull(ourRow.getPurchaseUnitDisplayName()));
        txtPurchaseUnit.setTag2nd(DmShared.xNull(ourRow.getPurchaseUnitDecimalPlaces()));
        purchaseUnitName = DmShared.xNull(ourRow.getPurchaseUnitName());

        txtRequiredDate.setText(DmShared.dNull(ourRow.getReqDate()));
        txtRemark.setText(DmShared.xNull(ourRow.getRemark()));
        txtRate.setText(DmClient.vNull(ourRow.getRate(), "0.00"));
        txtAmount.setText(DmClient.vNull(ourRow.getAmount(), "0.00"));
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
        propertyPopup_Mdl.setColourCode(DmShared.xNull(ourRow.getColorCode()));
        propertyPopup_Mdl.setColourName(DmShared.xNull(ourRow.getColor()));
        propertyPopup_Mdl.setSizeCode(DmShared.xNull(ourRow.getSizeCode()));
        propertyPopup_Mdl.setSizeName(DmShared.xNull(ourRow.getSize()));
        txtProperty.setText(DmShared.xNull(ourRow.getColorCode()));
        txtProperty.setTag(DmShared.xNull(ourRow.getSizeCode()));
        txtProperties.setText(propertyPopup_Mdl.getPropertiesCsv());

        manageDisplay();
        if (txtHItemName.isEnabled()) {
            txtHItemName.setFocus(true);
        } else {
            txtQuantity.setFocus(true);
        }
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdItemDetail_Mdl();
    }

    @Override
    public void onOk() {
        try {
            calculate();
            if (DmShared.xNull(txtHItemName.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Item is mandatory.", txtHItemName).show();
                return;
            }
            if (DmShared.xNull(txtPurchaseUnit.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Unit is mandatory.", txtPurchaseUnit).show();
                return;
            }
            if (!DmShared.xNull(txtHItemName.getTag2nd()).trim().equalsIgnoreCase("")) {
                try {
                    ItemPropertyPopup_Vld.validateProperties(WorkOrder_Vld.getPOApplicableProperties(txtHItemName.getTag2nd()), isBestBefore, orderEntry.txtVoucherDate.getText(), propertyPopup_Mdl);
                } catch (DmException e) {
                    new DmAlertMessageBox("Properties : " + e.getMessage(), txtProperties).show();
                    return;
                }
            }
            if (DmShared.vNull(txtQuantity.getText()).doubleValue() == 0) {
                new DmAlertMessageBox("Quantity is mandatory.", txtQuantity).show();
                return;
            }
            if (DmShared.vNull(txtRate.getText()).doubleValue() == 0) {
                new DmAlertMessageBox("Rate is mandatory.", txtRate).show();
                return;
            }
            if (!DmShared.dNull(txtRequiredDate.getText()).equalsIgnoreCase("")) {
                if ((DmShared.convertDate(orderEntry.txtVoucherDate.getText()).getTime() > (DmShared.convertDate(DmShared.dNull(txtRequiredDate.getText())).getTime()))) {
                    new DmAlertMessageBox("Required Date should be greater than or equal to PO Date.", txtRequiredDate).show();
                    return;
                }
            }
            for (GrdItemDetail_Mdl detail_Mdl : (List<GrdItemDetail_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
                if (DmShared.xNull(txtHItemName.getTag()).equalsIgnoreCase(DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()))
                        && DmShared.xNull(txtProperty.getTag()).equalsIgnoreCase(DmShared.xNull(detail_Mdl.getSizeCode()))
                        && DmShared.xNull(txtProperty.getText()).equalsIgnoreCase(DmShared.xNull(detail_Mdl.getColorCode()))
                        && DmShared.dNull(txtRequiredDate.getText()).equalsIgnoreCase(DmShared.dNull(detail_Mdl.getReqDate()))
                        && DmShared.vNull(txtRate.getText()).doubleValue() == DmShared.vNull(detail_Mdl.getRate()).doubleValue()) {
                    if (!DmClient.vNull(detail_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {

                        String message = "<div style='float:right; margin-right: 5px;'>";
                        message += "Record already exist with following details - <br>";
                        message += (DmShared.xNull(txtHItemName.getText()).trim().equals("")) ? "" : "<b>Item:</b> " + DmShared.xNull(txtHItemName.getText()) + "<br>";
                        message += (DmShared.xNull(propertyPopup_Mdl.getColourName()).trim().equals("")) ? "" : "<b>Colour:</b> " + DmShared.xNull(propertyPopup_Mdl.getColourName()) + "<br>";
                        message += (DmShared.xNull(propertyPopup_Mdl.getSizeName()).trim().equals("")) ? "" : "<b>Size:</b> " + DmShared.xNull(propertyPopup_Mdl.getSizeName()) + "<br>";
                        message += (DmShared.vNull(txtRate.getText()) == 0) ? "" : "<b>Rate:</b> " + DmClient.vNull(txtRate.getText(), "0.00") + "<br>";
                        message += (DmShared.dNull(txtRequiredDate.getText()).trim().equals("")) ? "" : "<b>Req. Date:</b> " + DmShared.dNull(txtRequiredDate.getText()) + "<br>";
                        message += "</div>";
                        new DmAlertMessageBox(message, txtHItemName).show();
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
            new DmAlertMessageBox(e.getMessage(), txtHItemName).show();
        }
    }

    @Override
    public void manageOk(T1 row) {
        GrdItemDetail_Mdl ourRow = (GrdItemDetail_Mdl) row;

        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHItemName.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHItemName.getText()));

        ourRow.setItemProperty(DmShared.xNull(txtHItemName.getTag2nd()));
        ourRow.setColorCode(DmShared.xNull(propertyPopup_Mdl.getColourCode()));
        ourRow.setColor(DmShared.xNull(propertyPopup_Mdl.getColourName()));
        ourRow.setSizeCode(DmShared.xNull(propertyPopup_Mdl.getSizeCode()));
        ourRow.setSize(DmShared.xNull(propertyPopup_Mdl.getSizeName()));
        ourRow.setProperties(DmShared.xNull(propertyPopup_Mdl.getPropertiesCsv()));
        ourRow.setIsBestBefore(DmShared.iNull(isBestBefore).toString());

        ourRow.setPurchaseUnitCode(DmShared.xNull(txtPurchaseUnit.getTag()));
        ourRow.setPurchaseUnitDisplayName(DmShared.xNull(txtPurchaseUnit.getText()));
        ourRow.setPurchaseUnitDecimalPlaces(DmShared.xNull(txtPurchaseUnit.getTag2nd()));
        ourRow.setPurchaseUnitName(DmShared.xNull(purchaseUnitName));
        ourRow.setPurchaseUnitConversionRate(DmShared.xNull(purchaseUnitConversionRate));
        ourRow.setSkuDecimalPlaces(DmShared.xNull(skuDecimalPlaces));
        ourRow.setSkuName(DmShared.xNull(skuName));

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
        ourRow.setRemark(DmShared.xNull(txtRemark.getText()));

        ourRow.getListIndentDetail().clear();
        ourRow.getListIndentDetail().addAll(gridIndentDetail.getGrid().getStore().getAll());

        ourRow.setOHLList(overHeadLine.getOverHeadList());

        callBack.onOk(ourRow);
    }

    @Override
    public void onNew() {
        proceedNew();
    }

    @Override
    public void onDelete() {
        if (DmShared.xNull(parentRow.getIsStatus()).equalsIgnoreCase("1")) {
            new DmAlertMessageBox("Record has been canceled can't delete record.", txtHItemName).show();
            return;
        } else if (DmShared.xNull(parentRow.getIsStatus()).equalsIgnoreCase("2")) {
            new DmAlertMessageBox("Record has been completed, can't change record.", txtHItemName).show();
            return;
        } else if (!DmShared.xNull(parentRow.getChallanQyt()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Item is used, can't change record.", txtHItemName).show();
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

    public WorkOrderGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Item Detail", "750px", "338px");
        srvAsync = new ServiceInvoker<WorkOrder_SrvAsync>(GWT.create(WorkOrder_Srv.class), WorkOrder.class.getName());
        textHelpEvents();
        gridEvents();
        txtRequiredDate.addBlurHandler(this);
        txtQuantity.addBlurHandler(this);
        txtRate.addBlurHandler(this);
        showIndentSearch();
    }

    private void manageDisplay() {
        txtAmount.setEnabled(false);
        flwAddIndent.clear();
        if (!DmShared.xNull(mode).equalsIgnoreCase("Browse")) {
            flwAddIndent.add(btnAddIndent);
        }
        if (DmShared.xNull(txtHItemName.getTag2nd()).trim().equalsIgnoreCase("")) {
            txtProperties.setEnabled(false);
        } else {
            txtProperties.setEnabled(true);
        }
        if (parentRow != null && !DmShared.xNull(parentRow.getQuotationDocId()).equalsIgnoreCase("")) {
            txtHItemName.setEnabled(false);
            txtRate.setEnabled(false);
        } else {
            txtRate.setEnabled(true);
            if (gridIndentDetail.getGrid().getStore().getAll().size() > 0) {
                txtHItemName.setEnabled(false);
            } else {
                txtHItemName.setEnabled(true);
            }
        }
    }

    private void showIndentSearch() {
        btnAddIndent.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (!DmShared.xNull(txtHItemName.getTag()).trim().equalsIgnoreCase("")) {
                    indentSearch.show(txtHItemName.getTag(), propertyPopup_Mdl, !DmShared.dNull(orderEntry.txtRefDate.getText()).equalsIgnoreCase("") ? orderEntry.txtRefDate.getText() : orderEntry.txtVoucherDate.getText(), new ProjectVoucherCategory().PURCHASE_ORDER);
                } else {
                    new DmAlertMessageBox("Please select Item.", txtHItemName).show();
                }
            }
        });
    }

    private void fillIndentDetails(List<KeyFieldExtended> listKF) {
        ItemProperty_Mdl iipm_Mdl = new ItemProperty_Mdl();
        iipm_Mdl.setItemCode(DmShared.xNull(txtHItemName.getTag()));
        iipm_Mdl.setColourCode(DmShared.xNull(propertyPopup_Mdl.getColourCode()));
        iipm_Mdl.setSizeCode(DmShared.xNull(propertyPopup_Mdl.getSizeCode()));

        srvAsync.invokeRPC().getIndentDetail(listKF, iipm_Mdl, new AsyncCallback<DmResult<List<GrdIndentDetail_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHItemName).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdIndentDetail_Mdl>> result) {
                if (result.isStatus()) {
                    if (result.isStatus()) {
                        gridIndentDetail.getGrid().getStore().addAll(result.getData());
                        calculateQty();
                        if (txtHItemName.isEnabled()) {
                            txtHItemName.setFocus(true);
                        } else {
                            txtQuantity.setFocus(true);
                        }
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), txtHItemName).show();
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
        manageDisplay();
    }

    private void resetPopup() {
        mode = "";

        selectedItemCode = "";
        txtHItemName.setText("");
        txtHItemName.setTag("");
        txtHItemName.setTag2nd("");
        resetItem();

    }

    private void resetItem() {
        isBestBefore = "";
        purchaseUnitName = "";
        purchaseUnitConversionRate = "";
        skuDecimalPlaces = "";
        skuName = "";

        txtProperties.setText("");
        txtProperties.setTag("");
        txtProperties.setTag2nd("");
        propertyPopup_Mdl = new ItemProperty_Mdl();

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

        txtRequiredDate.setText("");
        txtRequiredDate.setTag("");
        txtRequiredDate.setTag2nd("");

        txtRemark.setText("");
        txtRemark.setTag("");
        txtRemark.setTag2nd("");

        gridIndentDetail.getGrid().getStore().clear();
    }

}
