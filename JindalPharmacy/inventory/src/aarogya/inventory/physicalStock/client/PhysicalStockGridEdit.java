package aarogya.inventory.physicalStock.client;

import aarogya.inventory.physicalStock.shared.GrdItemDetail_Mdl;
import aarogya.inventory.physicalStock.shared.TxtHItemName_Mdl;
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
import aarogya.inventory.physicalStock.shared.PhysicalStock_Srv;
import aarogya.inventory.physicalStock.shared.PhysicalStock_SrvAsync;
import aarogya.selfToolkit.itemPropertyPopup.client.ItemPropertyPopup;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemPropertyPopup_Vld;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;

public class PhysicalStockGridEdit<T1, T2> extends PhysicalStockGridEdit_Dsg<T1, T2> implements BlurHandler, KeyUpHandler {

    //<editor-fold defaultstate="collapsed" desc="CallBack">
    interface CallBack {

        public void onOk(GrdItemDetail_Mdl opdService);

        public void onDelete();
    }
    private CallBack callBack;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
    //</editor-fold>

    private ServiceInvoker<PhysicalStock_SrvAsync> srvAsync;
    private PhysicalStock physicalStock;
    private String isBestBefore = "", selectedItemCode = "", selectedIbCode = "", purchaseUnitConversionRate = "";
    private final ItemPropertyPopup propertyPopup = new ItemPropertyPopup(true);
    private ItemProperty_Mdl propertyPopup_Mdl;
    private int hashCode = 0;

    public PhysicalStockGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Item Detail", "450px", "");
        srvAsync = new ServiceInvoker<PhysicalStock_SrvAsync>(GWT.create(PhysicalStock_Srv.class), PhysicalStock.class.getName());
        textHelpEvents();
        manageEvent();
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, PhysicalStock physicalStock) {
        this.physicalStock = physicalStock;
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            @Override
            public void execute() {
                txtHItemName.setFocus(true);
            }
        });
    }

    private void textHelpEvents() {
        propertyPopup.addCallBack(new ItemPropertyPopup.CallBack() {
            @Override
            public void onOk(ItemProperty_Mdl property_Mdl) {
                if (!selectedIbCode.equalsIgnoreCase(DmShared.xNull(property_Mdl.getIbCode()))) {
                    resetItemProperties();
                    selectedIbCode = DmShared.xNull(property_Mdl.getIbCode());
                    setRate(txtHItemName.getTag(), DmShared.xNull(property_Mdl.getIbCode()), physicalStock.txtHDepartment.getTag(), physicalStock.txtVoucherDate.getText(), DmShared.xNull(purchaseUnitConversionRate));
                }
                propertyPopup_Mdl = property_Mdl;
                txtProperties.setText(DmShared.xNull(property_Mdl.getPropertiesCsv()));
                txtProperties.setTag(DmShared.xNull(property_Mdl.getIbCode()));
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
                if (!DmShared.xNull(physicalStock.txtHDepartment.getTag()).equalsIgnoreCase("")) {
                    srvAsync.invokeRPC(blnCallBackWait).getTxtHItemName(physicalStock.txtHDepartment.getTag(), physicalStock.txtHFilterType.getTag(), physicalStock.txtHFilterValue.getTag(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHItemName_Mdl>>>() {

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
                    txtSKU.setText(DmShared.xNull(t1Row.getSkuName()));
                    txtSKU.setTag(DmShared.xNull(t1Row.getSkuCode()));
                    txtSKU.setTag2nd(DmShared.xNull(t1Row.getSkuDecimalPlaces()));
                    purchaseUnitConversionRate = DmShared.xNull(t1Row.getPurchaseUnitConversionRate());
                    showPropertyPopUp(true);
                    if (DmShared.iNull(t1Row.getPurchaseUnitCode()).longValue() != DmShared.iNull(t1Row.getSkuCode()).longValue()) {
                        txtRateDisplay.setText(DmShared.xNull(t1Row.getPurchaseUnitName()) + " = " + DmShared.xNull(t1Row.getPurchaseUnitConversionRate()) + " " + DmShared.xNull(t1Row.getSkuName()));
                    } else {
                        txtRateDisplay.setText(DmShared.xNull(t1Row.getPurchaseUnitName()));
                    }
                    txtRateDisplay.setTag(DmShared.xNull(t1Row.getPurchaseUnitCode()));
                    txtRateDisplay.setTag2nd(DmShared.xNull(t1Row.getPurchaseUnitName()));
                    if (DmShared.xNull(t1Row.getItemProperty()).trim().equals("")) {
                        if (!selectedItemCode.equalsIgnoreCase(DmShared.xNull(txtHItemName.getTag()))) {
                            setRate(txtHItemName.getTag(), "", physicalStock.txtHDepartment.getTag(), physicalStock.txtVoucherDate.getText(), DmShared.xNull(purchaseUnitConversionRate));
                        }
                    }
                    selectedItemCode = DmShared.xNull(txtHItemName.getTag());
                }
            }
        });
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
                    double SkuRate = DmShared.vNull(skuLandedRate).doubleValue();
                    double rate = SkuRate * DmShared.vNull(purchaseUnitCoversionRate).doubleValue();
                    txtRate.setText(DmClient.vNull(rate, "0.00"));
                    txtRate.setTag2nd(DmClient.vNull(rate, "0.00"));
                    txtRate.setTag(DmClient.vNull(SkuRate, DmShared.getNumberFormat(7)));
                }
            }
        });
    }

    private void calculate() {
        if (DmShared.vNull(txtRate.getText()).doubleValue() != DmShared.vNull(txtRate.getTag2nd()).doubleValue()) {
            double SkuRate = DmShared.vNull(txtRate.getText()).doubleValue() / DmShared.vNull(purchaseUnitConversionRate).doubleValue();
            txtRate.setTag(DmClient.vNull(SkuRate, DmShared.getNumberFormat(7)));
            txtRate.setTag2nd(DmClient.vNull(txtRate.getText(), "0.00"));
        }
        txtRate.setText(DmClient.vNull(txtRate.getText(), "0.00"));
        txtRate.setTag(DmClient.vNull(txtRate.getTag(), DmShared.getNumberFormat(7)));
    }

    private void showPropertyPopUp(boolean isDefaultShow) {
        String property = DmShared.xNull(txtHItemName.getTag2nd());
        if (!DmShared.xNull(txtHItemName.getTag()).equalsIgnoreCase("")
                && !DmShared.xNull(property).equalsIgnoreCase("")) {
            txtProperties.setEnabled(true);
            propertyPopup_Mdl.setItemCode(txtHItemName.getTag());
            propertyPopup_Mdl.setSkuName(txtSKU.getText());
            propertyPopup_Mdl.setSkuQtyForRate(purchaseUnitConversionRate);
            propertyPopup.show(physicalStock.topControl.getMode(), property, isBestBefore, propertyPopup_Mdl, DmShared.dNull(physicalStock.txtVoucherDate.getText()), false,false,STEnum.ItemBatchType.NON_EXPIRED_BATCH);
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

    private void manageDisplay() {
        txtSKU.setEnabled(false);
        if (DmShared.xNull(txtHItemName.getTag2nd()).equalsIgnoreCase("")) {
            txtProperties.setEnabled(false);
        } else {
            txtProperties.setEnabled(true);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="GridEdit Overrides">
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
            if (DmShared.vNull(propertyPopup_Mdl.getMrp()).doubleValue() > 0) {
                if (DmShared.vNull(propertyPopup_Mdl.getMrp()).doubleValue() < DmShared.vNull(txtRate.getText()).doubleValue()) {
                    new DmAlertMessageBox("Rate/pack Cannot be greater than MRP.", txtRate).show();
                    return;
                }
            }

            if (!physicalStock.cbAllowDuplicate.getValue()) {
                for (GrdItemDetail_Mdl detail_Mdl : (List<GrdItemDetail_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
                    if (!DmClient.vNull(detail_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                        if (DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(txtHItemName.getTag()))
                                && DmShared.xNull(detail_Mdl.getBatchNo()).equalsIgnoreCase(DmShared.xNull(propertyPopup_Mdl.getBatchNo()))
                                && DmShared.xNull(detail_Mdl.getColourCode()).equalsIgnoreCase(DmShared.xNull(propertyPopup_Mdl.getColourCode()))
                                && DmShared.xNull(detail_Mdl.getSizeCode()).equalsIgnoreCase(DmShared.xNull(propertyPopup_Mdl.getSizeCode()))
                                && DmShared.xNull(detail_Mdl.getItemMakeCode()).equalsIgnoreCase(DmShared.xNull(propertyPopup_Mdl.getItemMakeCode()))
                                && DmShared.vNull(detail_Mdl.getMrp()).doubleValue() == DmShared.vNull(propertyPopup_Mdl.getMrp()).doubleValue()
                                && DmShared.dNull(detail_Mdl.getExpiryDate()).equalsIgnoreCase(DmShared.dNull(propertyPopup_Mdl.getExpiryDate()))
                                && DmShared.dNull(detail_Mdl.getManufacturingDate()).equalsIgnoreCase(DmShared.dNull(propertyPopup_Mdl.getManufacturingDate()))
                                && DmShared.vNull(detail_Mdl.getRate()).doubleValue() == DmShared.vNull(txtRate.getText()).doubleValue()) {

                            String message = "<div style='float:right; margin-right: 5px;'>";
                            message += "Record already exist with following details - <br>";
                            message += (DmShared.xNull(txtHItemName.getText()).trim().equals("")) ? "" : "<b>Item:</b> " + DmShared.xNull(txtHItemName.getText()) + "<br>";
                            message += (DmShared.xNull(propertyPopup_Mdl.getBatchNo()).trim().equals("")) ? "" : "<b>Batch No.:</b> " + DmShared.xNull(propertyPopup_Mdl.getBatchNo()) + "<br>";
                            message += (DmShared.xNull(propertyPopup_Mdl.getColourName()).trim().equals("")) ? "" : "<b>Colour:</b> " + DmShared.xNull(propertyPopup_Mdl.getColourName()) + "<br>";
                            message += (DmShared.xNull(propertyPopup_Mdl.getSizeName()).trim().equals("")) ? "" : "<b>Size:</b> " + DmShared.xNull(propertyPopup_Mdl.getSizeName()) + "<br>";
                            message += (DmShared.xNull(propertyPopup_Mdl.getItemMakeName()).trim().equals("")) ? "" : "<b>Item Make:</b> " + DmShared.xNull(propertyPopup_Mdl.getItemMakeName()) + "<br>";
                            message += (DmShared.vNull(propertyPopup_Mdl.getMrp()) == 0) ? "" : "<b>MRP:</b> " + DmClient.vNull(propertyPopup_Mdl.getMrp(), "0.00") + "<br>";
                            message += (DmShared.dNull(propertyPopup_Mdl.getManufacturingDate()).trim().equals("")) ? "" : "<b>Manufacturing Date:</b> " + DmShared.dNull(propertyPopup_Mdl.getManufacturingDate()) + "<br>";
                            message += (DmShared.dNull(propertyPopup_Mdl.getExpiryDate()).trim().equals("")) ? "" : "<b>Expiry Date:</b> " + DmShared.dNull(propertyPopup_Mdl.getExpiryDate()) + "<br>";
                            message += (DmShared.vNull(txtRate.getText()) == 0) ? "" : "<b>Rate/Pack:</b> " + DmClient.vNull(txtRate.getText(), "0.00") + "<br>";
                            message += "</div>";
                            new DmAlertMessageBox(message, txtHItemName).show();
                            return;
                        }
                    }
                }
            }

            if (!DmShared.xNull(txtHItemName.getTag2nd()).equalsIgnoreCase("")) {
                try {
                    ItemPropertyPopup_Vld.validateProperties(txtHItemName.getTag2nd(), isBestBefore, physicalStock.txtVoucherDate.getText(), propertyPopup_Mdl);
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

        //Item
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHItemName.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHItemName.getText()));

        //Item Details
        ourRow.setPropertyList(DmShared.xNull(txtHItemName.getTag2nd()));
        ourRow.setIsBestBefore(DmShared.xNull(isBestBefore));

        //SKU
        ourRow.setSkuCode(DmShared.xNull(txtSKU.getTag()));
        ourRow.setSkuName(DmShared.xNull(txtSKU.getText()));
        ourRow.setSkuDecimalPlaces(DmShared.xNull(txtSKU.getTag2nd()));

        //RATE
        ourRow.setRate(DmClient.vNull(txtRate.getText(), "0.00"));
        ourRow.setSkuRate(DmShared.xNull(txtRate.getTag()));

        //Purchase Unit
        ourRow.setPurchaseUnitConverionRate(DmShared.xNull(purchaseUnitConversionRate));

        ourRow.setPurchaseUnitCode(DmShared.xNull(txtRateDisplay.getTag()));
        ourRow.setPurchaseUnitName(DmShared.xNull(txtRateDisplay.getTag2nd()));

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
        ourRow.setItemMakeCode(DmShared.xNull(propertyPopup_Mdl.getItemMakeCode()));
        ourRow.setItemMakeName(DmShared.xNull(propertyPopup_Mdl.getItemMakeName()));
        ourRow.setBestBeforeDays(DmShared.xNull(propertyPopup_Mdl.getBestBeforeDays()));
        ourRow.setPropertyDisplay(DmShared.xNull(propertyPopup_Mdl.getPropertiesCsv()));
        ourRow.setIbCode(DmShared.xNull(txtProperties.getTag()));

        //Quantities
        ourRow.setQuantity(DmClient.vNull(txtQuantity.getText(), DmShared.getNumberFormat(DmShared.iNull(txtSKU.getTag2nd()).intValue())));

        callBack.onOk(ourRow);
    }

    @Override
    public void displayRow(T1 row) {
        GrdItemDetail_Mdl ourRow = (GrdItemDetail_Mdl) row;

        //Clear
        resetPopup();

        //Item
        selectedItemCode = DmShared.xNull(ourRow.getSharedKeyFieldCode());
        txtHItemName.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHItemName.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));

        //Item Details
        txtHItemName.setTag2nd(DmShared.xNull(ourRow.getPropertyList()));
        isBestBefore = DmShared.xNull(ourRow.getIsBestBefore());

        //Rate
        txtRate.setText(DmShared.xNull(ourRow.getRate()));
        txtRate.setTag2nd(DmShared.xNull(ourRow.getRate()));
        txtRate.setTag(DmShared.xNull(ourRow.getSkuRate()));

        //SKU
        txtSKU.setText(DmShared.xNull(ourRow.getSkuName()));
        txtSKU.setTag(DmShared.xNull(ourRow.getSkuCode()));
        txtSKU.setTag2nd(DmShared.xNull(ourRow.getSkuDecimalPlaces()));

        //Purchase Unit
        purchaseUnitConversionRate = DmShared.xNull(ourRow.getPurchaseUnitConverionRate());

        if (DmShared.iNull(ourRow.getPurchaseUnitCode()).longValue() != DmShared.iNull(ourRow.getSkuCode()).longValue()) {
            txtRateDisplay.setText(DmShared.xNull(ourRow.getPurchaseUnitName()) + " = " + DmShared.xNull(ourRow.getPurchaseUnitConverionRate()) + " " + DmShared.xNull(ourRow.getSkuName()));
        } else {
            txtRateDisplay.setText(DmShared.xNull(ourRow.getPurchaseUnitName()));
        }
        txtRateDisplay.setTag(DmShared.xNull(ourRow.getPurchaseUnitCode()));
        txtRateDisplay.setTag2nd(DmShared.xNull(ourRow.getPurchaseUnitName()));

        //Properties
        propertyPopup_Mdl = new ItemProperty_Mdl();
        propertyPopup_Mdl.setItemCode(ourRow.getSharedKeyFieldCode());
        propertyPopup_Mdl.setBatchNo(ourRow.getBatchNo());
        propertyPopup_Mdl.setManufacturingDate(ourRow.getManufacturingDate());
        propertyPopup_Mdl.setExpiryDate(ourRow.getExpiryDate());
        propertyPopup_Mdl.setMrp(ourRow.getMrp());
        propertyPopup_Mdl.setSkuMrp(DmClient.vNull(ourRow.getSkuMrp(), "0.0000000"));
        propertyPopup_Mdl.setSkuQtyForRate(ourRow.getPurchaseUnitConverionRate());
        propertyPopup_Mdl.setSkuName(ourRow.getSkuName());
        propertyPopup_Mdl.setColourCode(ourRow.getColourCode());
        propertyPopup_Mdl.setColourName(ourRow.getColourName());
        propertyPopup_Mdl.setSizeCode(ourRow.getSizeCode());
        propertyPopup_Mdl.setSizeName(ourRow.getSizeName());
        propertyPopup_Mdl.setItemMakeCode(DmShared.xNull(ourRow.getItemMakeCode()));
        propertyPopup_Mdl.setItemMakeName(DmShared.xNull(ourRow.getItemMakeName()));
        propertyPopup_Mdl.setBestBeforeDays(ourRow.getBestBeforeDays());
        propertyPopup_Mdl.setIbCode(ourRow.getIbCode());
        selectedIbCode = DmShared.xNull(ourRow.getIbCode());
        txtProperties.setText(propertyPopup_Mdl.getPropertiesCsv());
        txtProperties.setTag(ourRow.getIbCode());

        //Quantities
        txtQuantity.setText(DmShared.xNull(ourRow.getQuantity()));
        txtQuantity.setTag(DmShared.xNull(ourRow.getQuantity()));

        //Other
        hashCode = ourRow.getSharedKeyFieldHashCode();

        manageDisplay();
        txtHItemName.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdItemDetail_Mdl();
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
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Events">
    private void manageEvent() {
        txtQuantity.addBlurHandler(this);
        txtRate.addBlurHandler(this);
        txtProperties.addKeyUpHandler(this);
        aProperties.addClickHandler(this);
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtQuantity) {
            txtQuantity.setText(DmClient.vNull(txtQuantity.getText(), DmShared.getNumberFormat(DmShared.iNull(txtSKU.getTag2nd()).intValue())));
        } else if (event.getSource() == txtRate) {
            calculate();
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
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Clear">
    private void resetPopup() {
        hashCode = 0;

        txtHItemName.setText("");
        txtHItemName.setTag("");
        txtHItemName.setTag2nd("");
        resetItem();

        txtQuantity.setText("");
        txtQuantity.setTag("");
        txtQuantity.setTag2nd("");
    }

    private void resetItem() {
        isBestBefore = "";

        txtSKU.setText("");
        txtSKU.setTag("");
        txtSKU.setTag2nd("");
        purchaseUnitConversionRate = "";

        resetItemProperties();
    }

    private void resetItemProperties() {
        txtRate.setText("");
        txtRate.setTag("");
        txtRate.setTag2nd("");

        txtProperties.setText("");
        txtProperties.setTag("");
        txtProperties.setTag2nd("");
        propertyPopup_Mdl = new ItemProperty_Mdl();

    }

    //</editor-fold>
}
