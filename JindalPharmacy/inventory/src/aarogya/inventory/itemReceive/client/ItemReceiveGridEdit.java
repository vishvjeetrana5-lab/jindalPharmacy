package aarogya.inventory.itemReceive.client;

import aarogya.inventory.itemReceive.shared.GrdItemDetail_Mdl;
import aarogya.inventory.itemReceive.shared.TxtHItemName_Mdl;
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
import aarogya.inventory.itemReceive.shared.ItemReceive_Srv;
import aarogya.inventory.itemReceive.shared.ItemReceive_SrvAsync;
import aarogya.selfToolkit.itemPropertyPopup.client.ItemPropertyPopup;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;

public class ItemReceiveGridEdit<T1, T2> extends ItemReceiveGridEdit_Dsg<T1, T2> implements BlurHandler, KeyUpHandler, KeyDownHandler {

    String voucherType="";
    
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
                if (DmShared.xNull(txtProperties.getText()).equalsIgnoreCase("")) {
                    showPropertyPopUp(false);
                }
            }
        }
    }

    interface CallBack {

        public void onOk(GrdItemDetail_Mdl opdService);

        public void onDelete();
    }

    private final ServiceInvoker<ItemReceive_SrvAsync> srvAsync;
    private CallBack callBack;
    private ItemReceive itemReceive;
    String isBestBefore = "", iSubCode = "";
    String selectedItemCode = "", selectedIbCode = "", skuMrp = "", skuRate = "", skuCode = "";
    private final ItemPropertyPopup propertyPopup = new ItemPropertyPopup(true);
    private ItemProperty_Mdl propertyPopup_Mdl;
    private int hashCode = 0;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void textHelpEvents() {

        propertyPopup.addCallBack(new ItemPropertyPopup.CallBack() {
            @Override
            public void onOk(ItemProperty_Mdl property_Mdl) {
                if (!selectedIbCode.equalsIgnoreCase(DmShared.xNull(property_Mdl.getIbCode()))) {
                    resetItemProperties();
                    selectedIbCode = DmShared.xNull(property_Mdl.getIbCode());
                    setRate(txtHItemName.getTag(), DmShared.xNull(property_Mdl.getIbCode()), itemReceive.txtHDepartment.getTag(), itemReceive.txtVoucherDate.getText(), DmShared.xNull(txtSKU.getTag()));
                }
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
                if (!DmShared.xNull(itemReceive.txtHDepartment.getTag()).equalsIgnoreCase("")) {
                    srvAsync.invokeRPC(blnCallBackWait).getTxtHItemName(DmShared.xNull(voucherType),DmShared.xNull(itemReceive.txtHDepartment.getTag()), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHItemName_Mdl>>>() {

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
                    txtHItemName.setTag2nd(DmShared.xNull(t1Row.getItemProperty()));
                    isBestBefore = DmShared.xNull(t1Row.getIsBestBefore());
                    iSubCode = DmShared.xNull(t1Row.getIsubCode());
                    txtSKU.setText(DmShared.xNull(t1Row.getSkuName()));
                    txtSKU.setTag(DmShared.xNull(t1Row.getPurchaseUnitConversionRate()));
                    txtSKU.setTag2nd(DmClient.vNull(t1Row.getSkuDecimalPlaces(), "0"));
                    txtTotalPacks.setTag(DmClient.vNull(t1Row.getPurchaseUnitDecimalPlaces(), "0"));
                    skuCode = DmShared.xNull(t1Row.getSkuCode());
                    showPropertyPopUp(true);
                    if (DmShared.iNull(t1Row.getPurchaseUnitCode()).longValue() != DmShared.iNull(t1Row.getSkuCode()).longValue()) {
                        txtTotalPackSizeDisplay.setText(DmShared.xNull(t1Row.getPurchaseUnitName()) + " = " + DmShared.xNull(t1Row.getPurchaseUnitConversionRate()) + " " + DmShared.xNull(t1Row.getSkuName()));
                        txtRatePackSize.setText(DmShared.xNull(t1Row.getPurchaseUnitName()) + " = " + DmShared.xNull(t1Row.getPurchaseUnitConversionRate()) + " " + DmShared.xNull(t1Row.getSkuName()));
                    } else {
                        txtTotalPackSizeDisplay.setText(DmShared.xNull(t1Row.getPurchaseUnitName()));
                        txtRatePackSize.setText(DmShared.xNull(t1Row.getPurchaseUnitName()));
                    }
                    txtTotalPackSizeDisplay.setTag(DmShared.xNull(t1Row.getPurchaseUnitName()));
                    txtTotalPackSizeDisplay.setTag2nd(DmShared.xNull(t1Row.getPurchaseUnitCode()));

                    txtRatePackSize.setTag(DmShared.xNull(t1Row.getPurchaseUnitName()));
                    txtRatePackSize.setTag2nd(DmShared.xNull(t1Row.getPurchaseUnitCode()));

                    if (DmShared.xNull(t1Row.getItemProperty()).trim().equals("")) {
                        if (!selectedItemCode.equalsIgnoreCase(DmShared.xNull(txtHItemName.getTag()))) {
                            setRate(txtHItemName.getTag(), "", itemReceive.txtHDepartment.getTag(), itemReceive.txtVoucherDate.getText(), DmShared.xNull(txtSKU.getTag()));
                        }
                    }
                    selectedItemCode = DmShared.xNull(txtHItemName.getTag());
                }
            }
        });

    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, ItemReceive storeReceive,String v_type) {
        this.itemReceive = storeReceive;
        voucherType=v_type;
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
        if (event.getSource() == txtQuantity || event.getSource() == txtRate || event.getSource() == txtAmount) {
            calculate();
        }
    }

    private void resetItem() {
        isBestBefore = "";
        iSubCode = "";
        txtSKU.setText("");
        txtSKU.setTag("");
        txtSKU.setTag2nd("");

        resetItemProperties();
    }

    private void resetItemProperties() {
        skuMrp = "";
        skuRate = "";

        txtRate.setText("");
        txtRate.setTag("");
        txtRate.setTag2nd("");

        txtAmount.setText("");
        txtAmount.setTag("");
        txtAmount.setTag2nd("");

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
           propertyPopup.show(itemReceive.topControl.getMode(), property, isBestBefore, propertyPopup_Mdl, DmShared.dNull(itemReceive.txtVoucherDate.getText()), false,false,STEnum.ItemBatchType.NON_EXPIRED_BATCH);
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
                        selectedIbCode = DmShared.xNull(result.getData());
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

        //item
        txtHItemName.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHItemName.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtHItemName.setTag2nd(ourRow.getPropertyList());
        isBestBefore = ourRow.getIsBestBefore();
        selectedItemCode = DmShared.xNull(ourRow.getSharedKeyFieldCode());
        txtAmount.setText(DmClient.vNull(ourRow.getAmount(), "0.00"));
        txtTotalPacks.setText(DmShared.xNull(ourRow.getTotalpacks()));
        txtTotalPacks.setTag(DmClient.vNull(ourRow.getPurchaseUnitDecimalPlaces(), "0"));
        txtRate.setText(DmClient.vNull(ourRow.getRate(), "0.00"));
        txtRate.setTag2nd(DmClient.vNull(ourRow.getRate(), "0.00"));

        //SKU
        txtSKU.setText(DmShared.xNull(ourRow.getSkuName()));
        txtSKU.setTag(DmShared.xNull(ourRow.getSkuQtyForRate()));
        txtSKU.setTag2nd(DmShared.xNull(ourRow.getSkuDecimalPlaces()));
        skuMrp = DmShared.xNull(DmClient.vNull(ourRow.getSkuMrp(), DmShared.getNumberFormat(7)));
        skuCode = DmShared.xNull(ourRow.getSkuCode());
        skuRate = DmShared.xNull(ourRow.getSkuRate());

        if (DmShared.iNull(ourRow.getPurchaseUnitCode()).longValue() != DmShared.iNull(ourRow.getSkuCode()).longValue()) {
            txtTotalPackSizeDisplay.setText(DmShared.xNull(ourRow.getPurchaseUnitName()) + " = " + DmShared.xNull(ourRow.getSkuQtyForRate()) + " " + DmShared.xNull(ourRow.getSkuName()));
            txtRatePackSize.setText(DmShared.xNull(ourRow.getPurchaseUnitName()) + " = " + DmShared.xNull(ourRow.getSkuQtyForRate()) + " " + DmShared.xNull(ourRow.getSkuName()));
        } else {
            txtTotalPackSizeDisplay.setText(DmShared.xNull(ourRow.getPurchaseUnitName()));
            txtRatePackSize.setText(DmShared.xNull(ourRow.getPurchaseUnitName()));
        }
        txtTotalPackSizeDisplay.setTag(DmShared.xNull(ourRow.getPurchaseUnitName()));
        txtTotalPackSizeDisplay.setTag2nd(DmShared.xNull(ourRow.getPurchaseUnitCode()));
        txtRatePackSize.setTag(DmShared.xNull(ourRow.getPurchaseUnitName()));
        txtRatePackSize.setTag2nd(DmShared.xNull(ourRow.getPurchaseUnitCode()));

        txtRemark.setText(DmShared.xNull(ourRow.getRemark()));
        txtQuantity.setText(DmClient.vNull(ourRow.getQuantity(), "0.00"));

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
        txtProperties.setText(propertyPopup_Mdl.getPropertiesCsv());
        txtProperties.setTag(ourRow.getIbCode());

        selectedIbCode = DmShared.xNull(ourRow.getIbCode());

        txtHItemName.setFocus(true);
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

            if (itemReceive.defaultValue.isInventoryAccountingActive()) {
                if (DmShared.iNull(iSubCode).longValue() == 0) {
                    new DmAlertMessageBox("Inventory a/c is required.", txtHItemName).show();
                    return;
                }
            }

            for (GrdItemDetail_Mdl detail_Mdl : (List<GrdItemDetail_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
                if (!DmClient.vNull(detail_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                    if (DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(txtHItemName.getTag()))
                            && DmShared.xNull(detail_Mdl.getBatchNo()).equalsIgnoreCase(DmShared.xNull(propertyPopup_Mdl.getBatchNo()))
                            && DmShared.xNull(detail_Mdl.getLotNo()).equalsIgnoreCase(DmShared.xNull(propertyPopup_Mdl.getLotNo()))
                            && DmShared.xNull(detail_Mdl.getColourCode()).equalsIgnoreCase(DmShared.xNull(propertyPopup_Mdl.getColourCode()))
                            && DmShared.xNull(detail_Mdl.getItemMakeCode()).equalsIgnoreCase(DmShared.xNull(propertyPopup_Mdl.getItemMakeCode()))
                            && DmShared.xNull(detail_Mdl.getSizeCode()).equalsIgnoreCase(DmShared.xNull(propertyPopup_Mdl.getSizeCode()))
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
                if (DmShared.xNull(propertyPopup_Mdl.getPropertiesCsv()).equalsIgnoreCase("")) {
                    new DmAlertMessageBox("Item Properties are mandatory", txtProperties).show();
                    return;
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

        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHItemName.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHItemName.getText()));
        ourRow.setSkuName(DmShared.xNull(txtSKU.getText()));
        ourRow.setSkuCode(DmShared.xNull(skuCode));
        ourRow.setRemark(DmShared.xNull(txtRemark.getText()));
        ourRow.setQuantity(DmClient.vNull(txtQuantity.getText(), DmShared.getNumberFormat(DmShared.iNull(txtSKU.getTag2nd()).intValue())));
        ourRow.setPropertyList(DmShared.xNull(txtHItemName.getTag2nd()));
        ourRow.setPropertyDisplay(DmShared.xNull(txtProperties.getText()));
        ourRow.setSkuDecimalPlaces(DmShared.xNull(txtSKU.getTag2nd()));
        ourRow.setSkuQtyForRate(DmShared.xNull(txtSKU.getTag()));
        ourRow.setIbCode(DmShared.xNull(txtProperties.getTag()));
        ourRow.setPurchaseUnitCode(DmShared.xNull(txtRatePackSize.getTag2nd()));
        ourRow.setPurchaseUnitName(DmShared.xNull(txtRatePackSize.getTag()));
        ourRow.setSkuRate(skuRate);
        ourRow.setAmount(DmClient.vNull(txtAmount.getText(), "0.00"));
        ourRow.setRate(DmClient.vNull(txtRate.getText(), "0.00"));
        ourRow.setTotalpacks(DmShared.xNull(txtTotalPacks.getText()));
        ourRow.setPurchaseUnitDecimalPlaces(DmClient.vNull(txtTotalPacks.getTag(), "0"));

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
        ourRow.setIsBestBefore(DmShared.xNull(isBestBefore));
        ourRow.setSkuMrp(DmShared.xNull(skuMrp));

        callBack.onOk(ourRow);
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

    public ItemReceiveGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Item Detail", "500px", "");
        srvAsync = new ServiceInvoker<ItemReceive_SrvAsync>(GWT.create(ItemReceive_Srv.class), ItemReceive.class.getName());
        textHelpEvents();
        manageEvent();
    }

    private void manageEvent() {
        txtQuantity.addBlurHandler(this);
        txtRate.addBlurHandler(this);
        txtAmount.addBlurHandler(this);
        txtProperties.addKeyUpHandler(this);
        txtProperties.addKeyDownHandler(this);
        aProperties.addClickHandler(this);
    }

    private void manageDisplay() {
        txtSKU.setEnabled(false);
        txtTotalPacks.setEnabled(false);
        txtAmount.setEnabled(false);
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == aProperties) {
            showPropertyPopUp(false);
        } else {
            super.onClick(event);
        }
    }

    private void setRate(String itemCode, String ibCode, String departmentCode, String v_Date, final String purchaseUnitCoversionRate) {
        srvAsync.invokeRPC(true).getSkuLandedRate(itemCode, ibCode, departmentCode, v_Date, new AsyncCallback<DmResult<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHItemName).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    String skuLandedRate = result.getData();
                    double tempSkuRate = DmShared.vNull(skuLandedRate).doubleValue();
                    double tempRate = tempSkuRate * DmShared.vNull(purchaseUnitCoversionRate).doubleValue();
                    txtRate.setText(DmClient.vNull(tempRate, "0.00"));
                    txtRate.setTag2nd(DmClient.vNull(tempRate, "0.00"));
                    skuRate = DmClient.vNull(tempSkuRate, DmShared.getNumberFormat(7));
                    calculate();
                }
            }
        });
    }

    private void calculate() {
        if (DmShared.vNull(txtRate.getText()).doubleValue() != DmShared.vNull(txtRate.getTag2nd()).doubleValue()) {
            double tempSkuRate = DmShared.vNull(txtRate.getText()).doubleValue() / DmShared.vNull(txtSKU.getTag()).doubleValue();
            skuRate = DmClient.vNull(tempSkuRate, DmShared.getNumberFormat(7));
            txtRate.setTag2nd(DmClient.vNull(txtRate.getText(), "0.00"));
        }

        txtRate.setText(DmClient.vNull(txtRate.getText(), "0.00"));
        skuRate = DmClient.vNull(skuRate, DmShared.getNumberFormat(7));

        txtQuantity.setText(DmClient.vNull(txtQuantity.getText(), DmShared.getNumberFormat(DmShared.iNull(txtSKU.getTag2nd()).intValue())));
        txtTotalPacks.setText(DmClient.vNull(DmShared.vNull(txtQuantity.getText()).doubleValue() / DmShared.vNull(txtSKU.getTag()).doubleValue(), DmShared.getNumberFormat(DmShared.iNull(txtTotalPacks.getTag()).intValue())));
        txtAmount.setText(DmClient.vNull(DmShared.vNull(skuRate).doubleValue() * DmShared.vNull(txtQuantity.getText()).doubleValue(), "0.00"));
    }

}
