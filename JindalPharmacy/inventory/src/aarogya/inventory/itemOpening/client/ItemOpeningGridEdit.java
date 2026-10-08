package aarogya.inventory.itemOpening.client;

import aarogya.inventory.itemOpening.shared.GrdItemDetail_Mdl;
import aarogya.inventory.itemOpening.shared.TxtHItemName_Mdl;
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
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import aarogya.inventory.itemOpening.shared.ItemOpening_Srv;
import aarogya.inventory.itemOpening.shared.ItemOpening_SrvAsync;
import aarogya.selfToolkit.itemPropertyPopup.client.ItemPropertyPopup;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemPropertyPopup_Vld;
import aarogya.selfToolkit.stStatic.shared.STEnum;

public class ItemOpeningGridEdit<T1, T2> extends ItemOpeningGridEdit_Dsg<T1, T2> implements BlurHandler, AttachEvent.Handler, KeyUpHandler, KeyDownHandler {
String voucherType="";

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            if (event.getSource() == txtProperties) {
                if (!DmShared.xNull(txtHItemName.getTag2nd()).trim().equalsIgnoreCase("")) {
                    try {
                        ItemPropertyPopup_Vld.validateProperties(txtHItemName.getTag2nd(), (DmShared.xNull(isBestBefore).equalsIgnoreCase("1") ? "1" : "0"), itemOpening.txtVoucherDate.getText(), propertyPopup_Mdl);
                    } catch (DmException e) {
                        showPropertyPopUp(true);
                    }
                }
            }
        }
    }

    interface CallBack {

        public void onOk(GrdItemDetail_Mdl opdService);

        public void onDelete();
    }

    private final ServiceInvoker<ItemOpening_SrvAsync> srvAsync;
    private CallBack callBack;
    private final ItemPropertyPopup propertyPopup = new ItemPropertyPopup();
    private ItemProperty_Mdl propertyPopup_Mdl;
    private ItemOpening itemOpening;
    String isBestBefore = "", iSubCode = "";
    String selectedItemCode = "", skuRate = "", skuCode = "";
    private int hashCode = 0;
    private boolean flagBrowseMode = true;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void textHelpEvents() {

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

        txtHItemName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHItemName_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                    srvAsync.invokeRPC(blnCallBackWait).getTxtHItemName(DmShared.xNull(voucherType),DmShared.xNull(itemOpening.txtHDepartment.getTag()), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHItemName_Mdl>>>() {

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

                    skuCode = DmShared.xNull(t1Row.getSkuCode());

                    txtUnit.setTag(DmShared.xNull(t1Row.getPurchaseUnitName()));
                    txtUnit.setTag2nd(DmShared.xNull(t1Row.getPurchaseUnitCode()));
                    if (DmShared.iNull(t1Row.getPurchaseUnitCode()).longValue() != DmShared.iNull(t1Row.getSkuCode()).longValue()) {
                        txtUnit.setText(DmShared.xNull(t1Row.getPurchaseUnitName()) + " = " + DmShared.xNull(t1Row.getPurchaseUnitConversionRate()) + " " + DmShared.xNull(t1Row.getSkuName()));
                    } else {
                        txtUnit.setText(DmShared.xNull(t1Row.getPurchaseUnitName()));
                    }
                    txtDisplayUnit.setText(txtUnit.getText());
                    txtDisplayUnit.setTag(DmShared.xNull(t1Row.getPurchaseUnitDecimalPlaces()));

                    showPropertyPopUp(true);
                }
            }
        });

    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, ItemOpening storeReceive,String v_type) {
        this.itemOpening = storeReceive;
        voucherType=v_type;
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
        if (okNeeded && newNeeded && deleteNeeded) {
            flagBrowseMode = false;
        } else {
            flagBrowseMode = true;
        }

        manageDisplay(!flagBrowseMode);
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            @Override
            public void execute() {
                txtHItemName.setFocus(true);
            }
        });
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtRefDate) {
            txtRefDate.setText(DmClient.returnDate(txtRefDate.getText()));
        } else if (event.getSource() == txtQuantity) {
            txtQuantity.setText(DmClient.vNull(txtQuantity.getText(), DmShared.getNumberFormat(DmShared.iNull(txtSKU.getTag2nd()).intValue())));
            calculate();
        } else if (event.getSource() == txtRate) {
            calculate();
        }
    }

    @Override
    public void displayRow(T1 row) {
        GrdItemDetail_Mdl ourRow = (GrdItemDetail_Mdl) row;
        hashCode = ourRow.getSharedKeyFieldHashCode();

        //Item
        selectedItemCode = DmShared.xNull(ourRow.getSharedKeyFieldCode());
        txtHItemName.setTag(ourRow.getSharedKeyFieldCode());
        txtHItemName.setText(ourRow.getSharedKeyFieldName());

        //Item Details
        txtHItemName.setTag2nd(ourRow.getPropertyList());
        isBestBefore = ourRow.getIsBestBefore();

        //SKU
        txtSKU.setText(DmShared.xNull(ourRow.getSkuName()));
        txtSKU.setTag(DmShared.xNull(ourRow.getSkuQtyForRate()));
        txtSKU.setTag2nd(DmShared.xNull(ourRow.getSkuDecimalPlaces()));
        if (DmShared.iNull(ourRow.getPurchaseUnitCode()).longValue() != DmShared.iNull(ourRow.getSkuCode()).longValue()) {
            txtUnit.setText(DmShared.xNull(ourRow.getPurchaseUnitName()) + " = " + DmShared.xNull(ourRow.getSkuQtyForRate()) + " " + DmShared.xNull(ourRow.getSkuName()));
        } else {
            txtUnit.setText(DmShared.xNull(ourRow.getPurchaseUnitName()));
        }
        txtUnit.setTag(DmShared.xNull(ourRow.getPurchaseUnitName()));
        txtUnit.setTag2nd(DmShared.xNull(ourRow.getPurchaseUnitCode()));
        txtDisplayUnit.setText(txtUnit.getText());
        txtDisplayUnit.setTag(DmShared.xNull(ourRow.getPurchaseUnitDecimalPlaces()));
        txtUnitQuantity.setText(DmShared.xNull(ourRow.getPurchaseUnitQuantity()));

        skuCode = DmShared.xNull(ourRow.getSkuCode());

        //IAP
        iSubCode = DmShared.xNull(ourRow.getIsubCode());

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
        propertyPopup_Mdl.setSkuQtyForRate(ourRow.getSkuQtyForRate());
        propertyPopup_Mdl.setSkuName(ourRow.getSkuName());
        propertyPopup_Mdl.setColourCode(ourRow.getColourCode());
        propertyPopup_Mdl.setColourName(ourRow.getColourName());
        propertyPopup_Mdl.setSizeCode(ourRow.getSizeCode());
        propertyPopup_Mdl.setSizeName(ourRow.getSizeName());
        propertyPopup_Mdl.setItemMakeCode(DmShared.xNull(ourRow.getItemMakeCode()));
        propertyPopup_Mdl.setItemMakeName(DmShared.xNull(ourRow.getItemMakeName()));
        txtProperties.setText(propertyPopup_Mdl.getPropertiesCsv());

        txtHItemName.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHItemName.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtRefDate.setText(DmShared.dNull(ourRow.getRefChallanDate()));
        txtRefNo.setText(DmShared.xNull(ourRow.getRefChallanNo()));
        txtRemark.setText(DmShared.xNull(ourRow.getRemark()));
        txtRate.setText(DmClient.vNull(ourRow.getRate(), "0.00"));
        txtAmount.setText(DmClient.vNull(ourRow.getSkuValue(), "0.00"));
        txtQuantity.setText(DmShared.xNull(ourRow.getQuantity()));
        skuRate = DmShared.xNull(ourRow.getSkuRate());

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

            if (!DmShared.xNull(txtHItemName.getTag2nd()).equalsIgnoreCase("")) {
                if (DmShared.xNull(propertyPopup_Mdl.getPropertiesCsv()).equalsIgnoreCase("")) {
                    new DmAlertMessageBox("Item Properties are mandatory", txtProperties).show();
                    showPropertyPopUp(true);
                    return;
                }
            }

            if (DmShared.vNull(txtQuantity.getText()).doubleValue() == 0) {
                new DmAlertMessageBox("Quantity is mandatory.", txtQuantity).show();
                return;
            }

            if (DmShared.vNull(txtRate.getText()) == 0) {
                new DmAlertMessageBox("Rate is mandatory.", txtRate).show();
                return;
            }

            if (DmShared.vNull(propertyPopup_Mdl.getMrp()) > 0) {
                if (DmShared.vNull(propertyPopup_Mdl.getMrp()) < DmShared.vNull(txtRate.getText())) {
                    new DmAlertMessageBox("MRP should be greater than or equal to rate.", txtRate).show();
                    return;
                }
            }

            if (!DmShared.dNull(txtRefDate.getText()).equalsIgnoreCase("")) {
                if (DmShared.convertDate(txtRefDate.getText()).getTime() >= DmShared.convertDate(DmShared.dNull(itemOpening.txtVoucherDate.getText())).getTime()) {
                    new DmAlertMessageBox("Ref. Date should be less than opening date.", txtRefDate).show();
                    return;
                }
            }

            if (itemOpening.defaultValue.isInventoryAccountingActive()) {
                if (DmShared.iNull(iSubCode).longValue() == 0) {
                    new DmAlertMessageBox("Inventory a/c is required.", txtHItemName).show();
                    return;
                }
            }

            for (GrdItemDetail_Mdl detail_Mdl : (List<GrdItemDetail_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
                if (!DmClient.vNull(detail_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                    if (DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(txtHItemName.getTag()))
                            && DmShared.vNull(detail_Mdl.getRate()).doubleValue() == DmShared.vNull(txtRate.getText()).doubleValue()
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
                    ItemPropertyPopup_Vld.validateProperties(txtHItemName.getTag2nd(), isBestBefore, itemOpening.txtVoucherDate.getText(), propertyPopup_Mdl);
                } catch (DmException e) {
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
        ourRow.setPropertyList(DmShared.xNull(txtHItemName.getTag2nd()));
        ourRow.setPropertyDisplay(DmShared.xNull(txtProperties.getText()));
        ourRow.setSkuName(DmShared.xNull(txtSKU.getText()));
        ourRow.setSkuCode(DmShared.xNull(skuCode));
        ourRow.setRefChallanNo(DmShared.xNull(txtRefNo.getText()));
        ourRow.setRefChallanDate(DmShared.dNull(txtRefDate.getText()));
        ourRow.setRemark(DmShared.xNull(txtRemark.getText()));
        ourRow.setQuantity(DmShared.xNull(txtQuantity.getText()));
        ourRow.setRate(DmClient.vNull(txtRate.getText(), "0.00"));
        ourRow.setSkuValue(DmClient.vNull(txtAmount.getText(), "0.00"));
        ourRow.setSkuRate(skuRate);
        ourRow.setSkuDecimalPlaces(DmShared.xNull(txtSKU.getTag2nd()));
        ourRow.setSkuQtyForRate(DmShared.xNull(txtSKU.getTag()));
        ourRow.setPurchaseUnitCode(DmShared.xNull(txtUnit.getTag2nd()));
        ourRow.setPurchaseUnitName(DmShared.xNull(txtUnit.getTag()));
        ourRow.setPurchaseUnitQuantity(DmShared.xNull(txtUnitQuantity.getText()));
        ourRow.setPurchaseUnitDecimalPlaces(DmShared.xNull(txtDisplayUnit.getTag()));

        //IAP
        ourRow.setIsubCode(iSubCode);

        //Properties
        ourRow.setBatchNo(DmShared.xNull(propertyPopup_Mdl.getBatchNo()));
        ourRow.setLotNo(DmShared.xNull(propertyPopup_Mdl.getLotNo()));
        ourRow.setIbCode(DmShared.xNull(propertyPopup_Mdl.getIbCode()));
        ourRow.setManufacturingDate(DmShared.dNull(propertyPopup_Mdl.getManufacturingDate()));
        ourRow.setExpiryDate(DmShared.dNull(propertyPopup_Mdl.getExpiryDate()));
        ourRow.setMrp(DmClient.vNull(propertyPopup_Mdl.getMrp(), "0.00"));
        ourRow.setSkuMrp(DmClient.vNull(propertyPopup_Mdl.getSkuMrp(), DmShared.getNumberFormat(7)));
        ourRow.setColourCode(DmShared.xNull(propertyPopup_Mdl.getColourCode()));
        ourRow.setColourName(DmShared.xNull(propertyPopup_Mdl.getColourName()));
        ourRow.setSizeCode(DmShared.xNull(propertyPopup_Mdl.getSizeCode()));
        ourRow.setSizeName(DmShared.xNull(propertyPopup_Mdl.getSizeName()));
        ourRow.setItemMakeCode(DmShared.xNull(propertyPopup_Mdl.getItemMakeCode()));
        ourRow.setItemMakeName(DmShared.xNull(propertyPopup_Mdl.getItemMakeName()));
        ourRow.setIsBestBefore(DmShared.xNull(isBestBefore));

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

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == aProperties) {
            showPropertyPopUp(false);
        } else {
            super.onClick(event);
        }
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_SPACE) {
            if (event.getSource() == txtProperties) {
                showPropertyPopUp(false);
            }
        }
    }

    public ItemOpeningGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Item Detail", "500px", "");
        srvAsync = new ServiceInvoker<ItemOpening_SrvAsync>(GWT.create(ItemOpening_Srv.class), ItemOpening.class.getName());
        textHelpEvents();
        manageEvent();
    }

    private void manageEvent() {
        txtRefDate.addBlurHandler(this);
        txtQuantity.addBlurHandler(this);
        txtRate.addBlurHandler(this);
        txtProperties.addKeyUpHandler(this);
        txtProperties.addKeyDownHandler(this);
        aProperties.addClickHandler(this);
    }

    private void manageDisplay(boolean flag) {
        txtSKU.setReadOnly(true);
        txtAmount.setEnabled(false);
        txtUnit.setReadOnly(true);
        txtDisplayUnit.setReadOnly(true);
        txtUnitQuantity.setEnabled(false);
    }

    private void calculate() {
        skuRate = DmClient.vNull(DmShared.vNull(txtRate.getText()).doubleValue() / DmShared.vNull(txtSKU.getTag()).doubleValue(), DmShared.getNumberFormat(7));
        txtAmount.setText(DmClient.vNull(DmShared.vNull(txtQuantity.getText()).doubleValue() * DmShared.vNull(skuRate).doubleValue(), "0.00"));
        txtUnitQuantity.setText(DmClient.vNull(DmShared.vNull(txtQuantity.getText()).doubleValue() / DmShared.vNull(txtSKU.getTag()).doubleValue(), DmShared.getNumberFormat(DmShared.iNull(txtDisplayUnit.getTag()).intValue())));
    }

    private void showPropertyPopUp(boolean isDefaultShow) {
        String property = DmShared.xNull(txtHItemName.getTag2nd());
        if (!DmShared.xNull(txtHItemName.getTag()).equalsIgnoreCase("")
                && !DmShared.xNull(property).equalsIgnoreCase("")) {
            txtProperties.setEnabled(true);
            propertyPopup_Mdl.setItemCode(txtHItemName.getTag());
            propertyPopup_Mdl.setSkuQtyForRate(txtSKU.getTag());
            propertyPopup_Mdl.setSkuName(txtSKU.getText());
          propertyPopup.show(itemOpening.topControl.getMode(), property, isBestBefore, propertyPopup_Mdl, DmShared.dNull(itemOpening.txtVoucherDate.getText()), true,false,STEnum.ItemBatchType.ALL_BATCH);
        } else {
            txtProperties.setEnabled(false);
            if (!isDefaultShow) {
                new DmAlertMessageBox("Properties not applicable for this item.", txtProperties).show();
            }
        }
    }

    private void resetItem() {
        isBestBefore = "";
        iSubCode = "";
        txtSKU.setText("");
        txtSKU.setTag("");
        txtSKU.setTag2nd("");
        txtUnit.setText("");
        txtUnit.setTag("");
        txtUnit.setTag2nd("");
        txtDisplayUnit.setText("");
        txtDisplayUnit.setTag("");
        txtDisplayUnit.setTag2nd("");
        txtUnitQuantity.setText("");
        txtUnitQuantity.setTag("");
        txtUnitQuantity.setTag2nd("");
        txtProperties.setText("");
        txtProperties.setTag("");
        txtProperties.setTag2nd("");
        propertyPopup_Mdl = new ItemProperty_Mdl();
    }
}
