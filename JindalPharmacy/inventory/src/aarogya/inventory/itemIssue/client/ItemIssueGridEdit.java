package aarogya.inventory.itemIssue.client;

import aarogya.inventory.itemIssue.shared.GrdItemDetail_Mdl;
import aarogya.inventory.itemIssue.shared.TxtHItemName_Mdl;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.List;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import aarogya.inventory.itemIssue.shared.ItemIssue_Srv;
import aarogya.inventory.itemIssue.shared.ItemIssue_SrvAsync;
import aarogya.selfToolkit.itemPropertyPopup.client.ItemPropertyPopup;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemPropertyPopup_Vld;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import aarogya.selfToolkit.toolTip.client.ToolTip;
import aarogya.selfToolkit.toolTip.shared.ToolTip_Mdl;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import java.util.ArrayList;

public class ItemIssueGridEdit<T1, T2> extends ItemIssueGridEdit_Dsg<T1, T2> implements BlurHandler, KeyUpHandler, KeyDownHandler {

    interface CallBack {

        public void onOk(List<GrdItemDetail_Mdl> opdService);

        public void onDelete();
    }

    private final ServiceInvoker<ItemIssue_SrvAsync> srvAsync;
    private CallBack callBack;
    private ItemIssue itemIssue;
    String isBestBefore = "", iSubCode = "";
    String selectedItemCode = "", reqDocId = "", reqV_SNo = "", skuMrp = "", allowedConsumption = "", currentStock = "";
    private final ItemPropertyPopup propertyPopup = new ItemPropertyPopup(true);
    private ItemProperty_Mdl propertyPopup_Mdl;
    private int hashCode = 0;
    private final ToolTip toolTip = new ToolTip();
    String voucherType = "";
    String exceedValue = "";
    String exceedValueAnother = "";
     

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_SPACE) {
            if (event.getSource() == txtProperties) {
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
                        ItemPropertyPopup_Vld.validateProperties(txtHItemName.getTag2nd(), (DmShared.xNull(isBestBefore).equalsIgnoreCase("1") ? "1" : "0"), itemIssue.txtVoucherDate.getText(), propertyPopup_Mdl);

                    } catch (DmException e) {

                        showPropertyPopUp(true);
                    }
                }
            }

        }
    }

    private void textHelpEvents() {

        propertyPopup.addCallBack(new ItemPropertyPopup.CallBack() {
            @Override
            public void onOk(ItemProperty_Mdl property_Mdl) {
                propertyPopup_Mdl = property_Mdl;
                txtProperties.setText(DmShared.xNull(property_Mdl.getPropertiesCsv()));
                txtProperties.setTag(propertyPopup_Mdl.getIbCode());
                skuMrp = propertyPopup_Mdl.getSkuMrp();
                txtProperties.setFocus(true);
            }

            @Override
            public void onClose() {
                txtProperties.setFocus(true);
            }
        });

        txtHItemName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHItemName_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                if (!DmShared.xNull(itemIssue.txtHDepartment.getTag()).equalsIgnoreCase("")) {
                    srvAsync.invokeRPC(blnCallBackWait).getTxtHItemName(DmShared.xNull(voucherType), DmShared.xNull(itemIssue.txtHDepartment.getTag()), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHItemName_Mdl>>>() {

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
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHItemName_Mdl t1Row) {
                if (!selectedItemCode.equalsIgnoreCase(DmShared.xNull(txtHItemName.getTag()))) {
                    resetItem();
                }

                if (blnStatus && t1Row != null) {
                    selectedItemCode = DmShared.xNull(txtHItemName.getTag());
                    txtHItemName.setTag2nd(DmShared.xNull(t1Row.getItemProperty()));
                    isBestBefore = DmShared.xNull(t1Row.getIsBestBefore());
                    iSubCode = DmShared.xNull(t1Row.getIsubCode());
                    txtSKU.setText(DmShared.xNull(t1Row.getSkuName()));
                    txtSKU.setTag(DmShared.xNull(t1Row.getPurchaseUnitConversionRate()));
                    txtSKU.setTag2nd(DmShared.xNull(t1Row.getSkuDecimalPlaces()));
                    txtGrIrConsumptionAc.setTag(DmShared.xNull(t1Row.getConsumptionAc()));
                    allowedConsumption = DmShared.xNull(t1Row.getAllowedMonthlyConsumption());
                    currentStock = "";
                    currentStock = DmShared.xNull(t1Row.getStock());
                    showPropertyPopUp(true);
                }
            }
        });

    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, ItemIssue storeReceive, String v_type) {
        voucherType = v_type;
        this.itemIssue = storeReceive;
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);

        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            @Override
            public void execute() {
                txtHItemName.setFocus(true);
            }
        });
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtQuantity) {
            txtQuantity.setText(DmClient.vNull(txtQuantity.getText(), DmShared.getNumberFormat(DmShared.iNull(txtSKU.getTag2nd()).intValue())));
        }
    }

    private void resetItem() {
        isBestBefore = "";
        iSubCode = "";
        txtSKU.setText("");
        txtSKU.setTag("");
        txtSKU.setTag2nd("");
        txtProperties.setText("");
        txtProperties.setTag("");
        txtProperties.setTag2nd("");
        propertyPopup_Mdl = new ItemProperty_Mdl();
    }

    private void showPropertyPopUp(boolean isDefaultShow) {
        String property = DmShared.xNull(txtHItemName.getTag2nd());
        if (!DmShared.xNull(txtHItemName.getTag()).equalsIgnoreCase("")
                && !DmShared.xNull(property).equalsIgnoreCase("")) {
            txtProperties.setEnabled(true);
            propertyPopup_Mdl.setItemCode(txtHItemName.getTag());
            propertyPopup_Mdl.setSkuName(txtSKU.getText());
            propertyPopup_Mdl.setSkuQtyForRate(txtSKU.getTag());
          propertyPopup.show(itemIssue.topControl.getMode(), property, isBestBefore, propertyPopup_Mdl, DmShared.dNull(itemIssue.txtVoucherDate.getText()), false,true,itemIssue.isExpiryItems ? STEnum.ItemBatchType.EXPIRED_BATCH : STEnum.ItemBatchType.NON_EXPIRED_BATCH);
        } else {
            txtProperties.setEnabled(false);
            if (!isDefaultShow) {
                new DmAlertMessageBox("Properties not applicable for this item.", txtProperties).show();
            }
            getIbCode();
        }
    }

    private void getIbCode() {
        if (DmShared.iNull(txtProperties.getTag()).longValue() == 0) {
            ItemProperty_Mdl propertyMdl = new ItemProperty_Mdl();
            propertyMdl.setItemCode(txtHItemName.getTag());
            propertyMdl.setItemName(txtHItemName.getText());

            srvAsync.invokeRPC().getIbCode(propertyMdl, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHItemName).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        txtProperties.setTag(result.getData());
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), txtHItemName).show();
                    }
                }
            });
        }
    }

    @Override
    public void displayRow(T1 row) {
        GrdItemDetail_Mdl ourRow = (GrdItemDetail_Mdl) row;

        manageDisplay();
        hashCode = ourRow.getSharedKeyFieldHashCode();

        if (DmShared.xNull(ourRow.getPropertyList()).equalsIgnoreCase("")) {
            txtProperties.setEnabled(false);
        } else if (!DmShared.xNull(ourRow.getPropertyList()).equalsIgnoreCase("")) {
            txtProperties.setEnabled(true);
        }

        //item
        txtHItemName.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHItemName.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtHItemName.setTag2nd(ourRow.getPropertyList());
        isBestBefore = ourRow.getIsBestBefore();
        selectedItemCode = DmShared.xNull(ourRow.getSharedKeyFieldCode());
        //SKU
        txtSKU.setText(DmShared.xNull(ourRow.getSkuName()));
        txtSKU.setTag(DmShared.xNull(ourRow.getSkuQtyForRate()));
        txtSKU.setTag2nd(DmShared.xNull(ourRow.getSkuDecimalPlaces()));
        skuMrp = DmShared.xNull(DmClient.vNull(ourRow.getSkuMrp(), DmShared.getNumberFormat(7)));
        txtGrIrConsumptionAc.setText(DmShared.xNull(ourRow.getGrIrConsumptionAc()));

        txtRemark.setText(DmShared.xNull(ourRow.getRemark()));
        txtQuantity.setText(DmShared.xNull(ourRow.getQuantity()));
        txtQuantity.setTag(DmShared.xNull(ourRow.getQuantity()));
        //Requisition
        reqDocId = DmShared.xNull(ourRow.getRequisitionDocId());
        reqV_SNo = DmShared.xNull(ourRow.getRequisitionV_SNo());

        if (DmShared.iNull(ourRow.getRequisitionDocId()).longValue() != 0) {
            flwLblReqisitionNoOuter.clear();
            flwLblReqisitionNoOuter.add(flwLblReqisitionNo);
            lblReqisitionNoValue.setText(DmShared.xNull(itemIssue.txtHRequisition.getText()));
        } else {
            flwLblReqisitionNoOuter.clear();
        }

        //IAP
        iSubCode = DmShared.xNull(ourRow.getIsubCode());

        //Properties
        propertyPopup_Mdl = new ItemProperty_Mdl();
        propertyPopup_Mdl.setItemCode(ourRow.getSharedKeyFieldCode());
        propertyPopup_Mdl.setBatchNo(ourRow.getBatchNo());
        propertyPopup_Mdl.setLotNo(ourRow.getLotNo());
        propertyPopup_Mdl.setManufacturingDate(ourRow.getManufacturingDate());
        propertyPopup_Mdl.setExpiryDate(ourRow.getExpiryDate());
        propertyPopup_Mdl.setMrp(ourRow.getMrp());
        propertyPopup_Mdl.setSkuMrp(DmClient.vNull(ourRow.getSkuMrp(), DmShared.getNumberFormat(7)));
        propertyPopup_Mdl.setSkuQtyForRate(ourRow.getSkuQtyForRate());
        propertyPopup_Mdl.setSkuName(ourRow.getSkuName());
        propertyPopup_Mdl.setColourCode(ourRow.getColourCode());
        propertyPopup_Mdl.setColourName(ourRow.getColourName());
        propertyPopup_Mdl.setSizeCode(ourRow.getSizeCode());
        propertyPopup_Mdl.setSizeName(ourRow.getSizeName());
        propertyPopup_Mdl.setItemMakeCode(DmShared.xNull(ourRow.getItemMakeCode()));
        propertyPopup_Mdl.setItemMakeName(DmShared.xNull(ourRow.getItemMakeName()));
        propertyPopup_Mdl.setBestBeforeDays(ourRow.getBestBeforeDays());
        txtProperties.setText(propertyPopup_Mdl.getPropertiesCsv());
        txtProperties.setTag(ourRow.getIbCode());

        if (reqDocId.equalsIgnoreCase("")) {
            txtHItemName.setEnabled(true);
            txtHItemName.setFocus(true);
        } else {
            txtHItemName.setEnabled(false);
            if (!DmShared.xNull(txtHItemName.getTag2nd()).trim().equalsIgnoreCase("")) {
                txtProperties.setFocus(true);
            } else {
                txtQuantity.setFocus(true);
            }

        }
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdItemDetail_Mdl();
    }

    @Override
    public void onOk() {
        try {

            if (DmShared.xNull(txtHItemName.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Item Name is mandatory.", txtHItemName).show();
                return;
            }
            if (DmShared.iNull(txtProperties.getTag()).longValue() == 0) {
                new DmAlertMessageBox("Please re-select the item to refresh its properties.", txtHItemName).show();
                return;
            }
            if (DmShared.vNull(txtQuantity.getText()).doubleValue() == 0) {
                new DmAlertMessageBox("Quantity is mandatory.", txtQuantity).show();
                return;
            }

            if (DmShared.iNull(reqDocId).longValue() > 0) {
                if (DmShared.vNull(txtQuantity.getText()) > DmShared.vNull(txtQuantity.getTag())) {
                    new DmAlertMessageBox("Issue quantity cannot be greater than required quantity.", txtQuantity).show();
                    return;

                }
            }

            if (itemIssue.defaultValue.isInventoryAccountingActive()) {
                if (DmShared.iNull(iSubCode).longValue() == 0) {
                    new DmAlertMessageBox("Inventory a/c is required.", txtHItemName).show();
                    return;
                }
            }

            if (DmShared.vNull(txtQuantity.getText()).doubleValue() > DmShared.vNull(currentStock).doubleValue()) {
                new DmAlertMessageBox("Quentity should be less then or equqal Current Stock.", txtQuantity).show();
                return;
            }

            for (GrdItemDetail_Mdl detail_Mdl : (List<GrdItemDetail_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
                if (!DmClient.vNull(detail_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                    if (DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(txtHItemName.getTag()))
                            && DmShared.xNull(detail_Mdl.getBatchNo()).equalsIgnoreCase(DmShared.xNull(propertyPopup_Mdl.getBatchNo()))
                            && DmShared.xNull(detail_Mdl.getLotNo()).equalsIgnoreCase(DmShared.xNull(propertyPopup_Mdl.getLotNo()))
                            && DmShared.xNull(detail_Mdl.getColourCode()).equalsIgnoreCase(DmShared.xNull(propertyPopup_Mdl.getColourCode()))
                            && DmShared.xNull(detail_Mdl.getSizeCode()).equalsIgnoreCase(DmShared.xNull(propertyPopup_Mdl.getSizeCode()))
                            //                               && DmShared.xNull(txtProperty.getTag()).equalsIgnoreCase(DmShared.xNull(detail_Mdl.getItemMakeCode()))
                            && DmShared.xNull(detail_Mdl.getItemMakeCode()).equalsIgnoreCase(DmShared.xNull(propertyPopup_Mdl.getItemMakeCode()))
                            && DmShared.vNull(detail_Mdl.getMrp()).doubleValue() == DmShared.vNull(propertyPopup_Mdl.getMrp()).doubleValue()
                            && DmShared.dNull(detail_Mdl.getExpiryDate()).equalsIgnoreCase(DmShared.dNull(propertyPopup_Mdl.getExpiryDate()))
                            && DmShared.dNull(detail_Mdl.getManufacturingDate()).equalsIgnoreCase(DmShared.dNull(propertyPopup_Mdl.getManufacturingDate()))) {

                        String message = "<div style='float:right; margin-right: 5px;'>";
                        message += "Record already exist with following details - <br>";
                        message += (DmShared.xNull(txtHItemName.getText()).trim().equals("")) ? "" : "<b>Item:</b> " + DmShared.xNull(txtHItemName.getText()) + "<br>";
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

            if (!DmShared.xNull(txtHItemName.getTag2nd()).equalsIgnoreCase("")) {
                try {
                    ItemPropertyPopup_Vld.validateProperties(txtHItemName.getTag2nd(), isBestBefore, itemIssue.txtVoucherDate.getText(), propertyPopup_Mdl);
                } catch (DmException e) {
                    new DmAlertMessageBox("Item Properties are mandatory", txtProperties).show();
                    return;
                }
            }
            aChequeExceedConsumption();

//            proceedOk();
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtHItemName).show();
        }
    }

    @Override
    public void manageOk(T1 row) {
        GrdItemDetail_Mdl ourRow = (GrdItemDetail_Mdl) row;

        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHItemName.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHItemName.getText()));
        ourRow.setSkuName(DmShared.xNull(txtSKU.getText()));
        ourRow.setRemark(DmShared.xNull(txtRemark.getText()));
        ourRow.setQuantity(DmClient.vNull(txtQuantity.getText(), DmShared.getNumberFormat(DmShared.iNull(txtSKU.getTag2nd()).intValue())));
        ourRow.setPropertyList(DmShared.xNull(txtHItemName.getTag2nd()));
        ourRow.setPropertyDisplay(DmShared.xNull(txtProperties.getText()));
        ourRow.setSkuDecimalPlaces(DmShared.xNull(txtSKU.getTag2nd()));
        ourRow.setSkuQtyForRate(DmShared.xNull(txtSKU.getTag()));
        ourRow.setIbCode(DmShared.xNull(txtProperties.getTag()));
        ourRow.setGrIrConsumptionAc(DmShared.xNull(txtGrIrConsumptionAc.getTag()));

        ourRow.setRequisitionDocId(DmShared.xNull(reqDocId));
        ourRow.setRequisitionV_SNo(DmShared.xNull(reqV_SNo));

        //IAP
        ourRow.setIsubCode(iSubCode);

        //Properties
        ourRow.setBatchNo(DmShared.xNull(propertyPopup_Mdl.getBatchNo()));
        ourRow.setLotNo(DmShared.xNull(propertyPopup_Mdl.getLotNo()));
        ourRow.setManufacturingDate(DmShared.dNull(propertyPopup_Mdl.getManufacturingDate()));
        ourRow.setExpiryDate(DmShared.dNull(propertyPopup_Mdl.getExpiryDate()));
        ourRow.setMrp(DmClient.vNull(propertyPopup_Mdl.getMrp(), "0.00"));
        ourRow.setColourCode(DmShared.xNull(propertyPopup_Mdl.getColourCode()));
        ourRow.setColourName(DmShared.xNull(propertyPopup_Mdl.getColourName()));
        ourRow.setSizeCode(DmShared.xNull(propertyPopup_Mdl.getSizeCode()));
        ourRow.setSizeName(DmShared.xNull(propertyPopup_Mdl.getSizeName()));
        ourRow.setItemMakeCode(DmShared.xNull(propertyPopup_Mdl.getItemMakeCode()));
        ourRow.setItemMakeName(DmShared.xNull(propertyPopup_Mdl.getItemMakeName()));
        ourRow.setBestBeforeDays(DmShared.xNull(propertyPopup_Mdl.getBestBeforeDays()));
        ourRow.setIsBestBefore(DmShared.xNull(isBestBefore));
        ourRow.setSkuMrp(DmShared.xNull(skuMrp));

        itemIssue.gridItemDetail.getGrid().getStore().remove(ourRow);

        breakeItemIbCode(ourRow);

    }

    @Override
    public void onNew() {
//        proceedNew();
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

    public ItemIssueGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Item Detail", "450px", "138px");
        srvAsync = new ServiceInvoker<ItemIssue_SrvAsync>(GWT.create(ItemIssue_Srv.class), ItemIssue.class.getName());
        textHelpEvents();
        manageEvent();
    }

    private void manageEvent() {
        txtQuantity.addBlurHandler(this);
        txtProperties.addKeyUpHandler(this);
        txtProperties.addKeyDownHandler(this);
        aProperties.addClickHandler(this);
        aStock.addClickHandler(this);
    }

    private void manageDisplay() {
        txtSKU.setEnabled(false);
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == aProperties) {
            showPropertyPopUp(false);
        } else if (event.getSource() == aStock && !DmShared.xNull(txtHItemName.getTag()).equalsIgnoreCase("")) {
            aStockShow();
        } else {
            super.onClick(event);
        }
    }

    private void aStockShow() {
        srvAsync.invokeRPC().getToolTipShowStock(txtHItemName.getTag(), DmShared.xNull(txtProperties.getTag()), itemIssue.txtHDepartment.getTag(), itemIssue.txtHDepartment.getTag2nd(), new AsyncCallback<DmResult<List<ToolTip_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<ToolTip_Mdl>> result) {
                if (result.isStatus()) {

                    toolTip.show(result.getData(), aStock);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtHItemName).show();
                }

            }
        });

    }

    private void aChequeExceedConsumption() {
        srvAsync.invokeRPC().getCheckExceedConsumption(txtHItemName.getTag(), DmShared.xNull(itemIssue.txtHDepartment.getTag()), txtQuantity.getText(), allowedConsumption, itemIssue.txtVoucherDate.getText(), new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    exceedValueAnother = DmShared.vNull(result.getData()).toString();
                    exceedValue = DmShared.vNull(DmShared.vNull(txtQuantity.getText()) - DmShared.vNull(allowedConsumption)).toString();
                    if (!DmShared.xNull(allowedConsumption).equalsIgnoreCase("0") && DmShared.iNull(itemIssue.defaultValue.getItemIssueExceedCode()).toString().equalsIgnoreCase("1")) {
                        if (DmShared.vNull(exceedValueAnother) < 0) {
                            new DmConfirmMessageBox("Qty. exceeds by " + Math.abs(DmShared.iNull(exceedValueAnother).intValue()) + " than aloted consumption.?", new DialogHideEvent.DialogHideHandler() {
                                @Override
                                public void onDialogHide(DialogHideEvent event) {
                                    if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                                        proceedOk();
                                    }
                                }
                            }).show();
                            return;
                        } else {
                            proceedOk();
                        }
                    }
                    if (!DmShared.xNull(allowedConsumption).equalsIgnoreCase("0") && DmShared.iNull(itemIssue.defaultValue.getItemIssueExceedCode()).toString().equalsIgnoreCase("2")) {
                        if (DmShared.vNull(exceedValueAnother) < 0) {
                            new DmAlertMessageBox("Qty. exceeds by " + Math.abs(DmShared.iNull(exceedValueAnother).intValue()) + " than aloted consumption. Do You want to continue. Please correct Quantity.", txtQuantity).show();
                            return;
                        } else {
                            proceedOk();
                        }
                    }
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtQuantity).show();
                }
                if (DmShared.xNull(allowedConsumption).equalsIgnoreCase("0")) {
                    proceedOk();
                }
            }
        });

    }

    private void breakeItemIbCode(GrdItemDetail_Mdl mrTemp) {
        srvAsync.invokeRPC().getNewIbCode(mrTemp, DmShared.xNull(itemIssue.txtHDepartment.getTag()), new AsyncCallback<DmResult<List<GrdItemDetail_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdItemDetail_Mdl>> result) {
                if (result.isStatus()) {
                    callBack.onOk(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });

    }

}
