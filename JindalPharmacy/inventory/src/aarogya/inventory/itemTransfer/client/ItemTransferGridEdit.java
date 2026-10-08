package aarogya.inventory.itemTransfer.client;

import aarogya.inventory.itemTransfer.shared.ItemTransfer_Srv;
import aarogya.inventory.itemTransfer.shared.TxtHItem_Mdl;
import aarogya.inventory.itemTransfer.shared.GrdItemDetail_Mdl;
import aarogya.inventory.itemTransfer.shared.ItemTransfer_SrvAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;
import aarogya.selfToolkit.itemPropertyPopup.client.ItemPropertyPopup;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemPropertyPopup_Vld;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import aarogya.selfToolkit.toolTip.shared.ToolTip_Mdl;

public class ItemTransferGridEdit<T1, T2> extends ItemTransferGridEdit_Dsg<T1, T2> implements BlurHandler, KeyDownHandler, KeyUpHandler {
String vType="";
    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtQuantity) {
            calculate();
        }
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            if (event.getSource() == txtProperties) {
                if (!DmShared.xNull(txtHItemName.getTag2nd()).trim().equalsIgnoreCase("")) {
                    try {
                        ItemPropertyPopup_Vld.validateProperties(txtHItemName.getTag2nd(), (bestBefore ? "1" : "0"), itemTransfer.txtVoucherDate.getText(), propertyMdl);
                    } catch (DmException e) {
                        showPropertyPopUp(true);
                    }
                }
            }
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

    interface CallBack {

        public void onOk(GrdItemDetail_Mdl opdService);

        public void onDelete();
    }

    private final ServiceInvoker<ItemTransfer_SrvAsync> srvAsync;
    private CallBack callBack;
    private final ItemPropertyPopup propertyPopup = new ItemPropertyPopup(true);
    private ItemProperty_Mdl propertyMdl;
    private ItemTransfer itemTransfer;
    private String selectedItemCode = "",requisitionMandatoryInTransfer="";
    private boolean bestBefore = false;
    private int hashCode = 0;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void textHelpEvents() {
        propertyPopup.addCallBack(new ItemPropertyPopup.CallBack() {
            @Override
            public void onOk(ItemProperty_Mdl property_Mdl) {
                if (property_Mdl != null) {
                    propertyMdl = property_Mdl;
                    txtProperties.setText(property_Mdl.getPropertiesCsv());
                    txtProperties.setTag(property_Mdl.getIbCode());
                    txtProperties.setFocus(true);
                }
            }

            @Override
            public void onClose() {
                txtHItemName.setFocus(true);
            }
        });
        txtHItemName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHItem_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                if (DmShared.iNull(itemTransfer.txtHDepartmentFrom.getTag()).longValue() == 0 && DmShared.iNull(itemTransfer.txtHDepartmentTo.getTag()).longValue() == 0) {
                    new DmAlertMessageBox("Please select departments first.", txtHItemName).show();
                    return;
                }
                srvAsync.invokeRPC(blnCallBackWait).getTxtHItemName(DmShared.xNull(itemTransfer.txtHDepartmentFrom.getTag()), DmShared.xNull(itemTransfer.txtHDepartmentTo.getTag()),requisitionMandatoryInTransfer,vType, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHItem_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHItemName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHItem_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHItemName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHItemName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHItem_Mdl t1Row) {
                if (DmShared.iNull(selectedItemCode).longValue() != DmShared.iNull(txtHItemName.getTag()).longValue()) {
                    resetItem();
                }
                if (blnStatus && t1Row != null) {
                    selectedItemCode = t1Row.getSharedKeyFieldCode();
                    bestBefore = t1Row.isBestBefore();
                    txtHItemName.setTag2nd(DmShared.xNull(t1Row.getItemProperty()));
                    txtSKU.setText(DmShared.xNull(t1Row.getSkuName()));
                    txtSKU.setTag(DmShared.xNull(t1Row.getPurchaseUnitConversionRate()));
                    txtSKU.setTag2nd(DmShared.iNull(t1Row.getSkuDecimalPlaces()).toString());
                    txtRemark.setTag(DmShared.xNull(t1Row.getStock()));
                    showPropertyPopUp(true);
                }
            }
        });
    }

    private void resetItem() {
        bestBefore = false;
        txtSKU.setText("");
        txtSKU.setTag("");
        txtSKU.setTag2nd("");
        txtProperties.setText("");
        txtProperties.setTag("");
        txtProperties.setTag2nd("");
        propertyMdl = new ItemProperty_Mdl();
    }

    private void showPropertyPopUp(boolean isDefaultShow) {
        String property = DmShared.xNull(txtHItemName.getTag2nd());
        if (!DmShared.xNull(txtHItemName.getTag()).equalsIgnoreCase("")
                && !DmShared.xNull(property).equalsIgnoreCase("")) {
            txtProperties.setEnabled(true);
            propertyMdl.setItemCode(txtHItemName.getTag());
            propertyMdl.setAllowDecimalPlaces(DmShared.iNull(txtSKU.getTag2nd()).intValue());
            propertyMdl.setSkuQtyForRate(txtSKU.getTag());
            propertyMdl.setSkuName(txtSKU.getText());
           propertyPopup.show(itemTransfer.topControl.getMode(), property, bestBefore ? "1" : "0", propertyMdl, itemTransfer.txtVoucherDate.getText(), false,false,STEnum.ItemBatchType.NON_EXPIRED_BATCH);
        } else {
            txtProperties.setEnabled(false);
            if (!isDefaultShow) {
                new DmAlertMessageBox("Properties not applicable for this item.", txtProperties).show();
            }
            getIbCode();
        }
    }

    private void aStockShow() {
        srvAsync.invokeRPC().getToolTipShowStock(txtHItemName.getTag(), DmShared.xNull(txtProperties.getTag()), itemTransfer.txtHDepartmentFrom.getTag(), itemTransfer.txtHDepartmentTo.getTag(), new AsyncCallback<DmResult<List<ToolTip_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void onSuccess(DmResult<List<ToolTip_Mdl>> result) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
//                new AsyncCallback<DmResult<List<ToolTip_Mdl>>>() {
//
//            @Override
//            public void onFailure(Throwable caught) {
//                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
//            }
//
//            @Override
//            public void onSuccess(DmResult<List<ToolTip_Mdl>> result) {
//                if (result.isStatus()) {
//
//                    toolTip.show(result.getData(), aStock);
//                } else {
//                    new DmAlertMessageBox(result.getErrorMessage(), txtHItemName).show();
//                }
//
//            }
//        });

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

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded,String requisitionMandatoryInTransfer,String vType, ItemTransfer storeReceive) {
        this.itemTransfer = storeReceive;
        this.requisitionMandatoryInTransfer=requisitionMandatoryInTransfer;
        this.vType=vType;
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                if (!DmShared.xNull(txtHItemName.getTag()).equalsIgnoreCase("")) {
                    txtProperties.setFocus(true);
                } else {
                    txtHItemName.setFocus(true);
                }
            }
        });
    }

    private void clear() {
        txtHItemName.setTag("");
        txtHItemName.setTag2nd("");
        txtHItemName.setText("");
        txtRemark.setText("");
        txtQuantity.setText("");
        selectedItemCode = "";
        resetItem();
    }

    @Override
    public void displayRow(T1 row) {
        GrdItemDetail_Mdl ourRow = (GrdItemDetail_Mdl) row;
        clear();
        manageDisplay();
        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtHItemName.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHItemName.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtHItemName.setTag2nd(DmShared.xNull(ourRow.getItemProperty()));
        bestBefore = ourRow.isBestBefore();
        txtSKU.setText(DmShared.xNull(ourRow.getSkuName()));
        txtSKU.setTag(DmShared.xNull(ourRow.getPurchaseUnitConversionRate()));
        txtSKU.setTag2nd(DmShared.iNull(ourRow.getSkuDecimalPlaces()).toString());
        propertyMdl = ourRow.getPropertyMdl();
        if (propertyMdl != null) {
            txtProperties.setText(propertyMdl.getPropertiesCsv());
        }
        txtProperties.setTag(ourRow.getIbCode());
        txtRemark.setText(DmShared.xNull(ourRow.getRemark()));
        txtRemark.setTag(DmShared.xNull(ourRow.getFromDeptStk()));
        txtQuantity.setText(DmShared.xNull(ourRow.getQuantity()));

        if (DmShared.iNull(ourRow.getRequisitionDocId()).longValue() != 0) {
            flwLblReqisitionNoOuter.clear();
            flwLblReqisitionNoOuter.add(flwLblReqisitionNo);
            lblReqisitionNoValue.setText(DmShared.xNull(itemTransfer.txtHRequisition.getText()));

            txtHItemName.setEnabled(false);
            if (!DmShared.xNull(txtHItemName.getTag2nd()).trim().equalsIgnoreCase("")) {
                txtProperties.setFocus(true);
            } else {
                txtQuantity.setFocus(true);
            }
        } else {
            flwLblReqisitionNoOuter.clear();

            txtHItemName.setEnabled(true);
            txtHItemName.setFocus(true);
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
            if (DmShared.iNull(txtHItemName.getTag()).longValue() == 0) {
                new DmAlertMessageBox("Item Name is mandatory.", txtHItemName).show();
                return;
            }

            if (DmShared.iNull(txtProperties.getTag()).longValue() == 0) {
                new DmAlertMessageBox("Please re-select the item to refresh its properties.", txtHItemName).show();
                return;
            }

            if (!DmShared.xNull(txtHItemName.getTag2nd()).trim().equalsIgnoreCase("")) {
                try {
                    ItemPropertyPopup_Vld.validateProperties(txtHItemName.getTag2nd(), bestBefore ? "1" : "0", itemTransfer.txtVoucherDate.getText(), propertyMdl);
                } catch (DmException e) {
                    new DmAlertMessageBox(e.getMessage(), txtProperties).show();
                    return;
                }
            }

            if (DmShared.vNull(txtQuantity.getText()).doubleValue() == 0) {
                new DmAlertMessageBox("Quantity is mandatory.", txtQuantity).show();
                return;
            }
        
            for (GrdItemDetail_Mdl detailMdl : ((List<GrdItemDetail_Mdl>) getDmGrid().getGrid().getStore().getAll())) {
                if (DmShared.iNull(txtHItemName.getTag()).longValue() == DmShared.iNull(detailMdl.getSharedKeyFieldCode()).longValue()
                        && DmShared.iNull(txtProperties.getTag()).longValue() == DmShared.iNull(detailMdl.getIbCode()).longValue()
                        && DmShared.xNull(detailMdl.getPropertyMdl().getBatchNo()).equalsIgnoreCase(DmShared.xNull(propertyMdl.getBatchNo()))
                        && DmShared.xNull(detailMdl.getPropertyMdl().getColourCode()).equalsIgnoreCase(DmShared.xNull(propertyMdl.getColourCode()))
                        && DmShared.xNull(detailMdl.getPropertyMdl().getSizeCode()).equalsIgnoreCase(DmShared.xNull(propertyMdl.getSizeCode()))
                        && DmShared.xNull(detailMdl.getPropertyMdl().getItemMakeCode()).equalsIgnoreCase(DmShared.xNull(propertyMdl.getItemMakeCode()))
                        && DmShared.vNull(detailMdl.getPropertyMdl().getMrp()).doubleValue() == DmShared.vNull(propertyMdl.getMrp()).doubleValue()
                        && DmShared.dNull(detailMdl.getPropertyMdl().getManufacturingDate()).equalsIgnoreCase(DmShared.dNull(propertyMdl.getManufacturingDate()))
                        && DmShared.dNull(detailMdl.getPropertyMdl().getExpiryDate()).equalsIgnoreCase(DmShared.dNull(propertyMdl.getExpiryDate()))
                        && hashCode != detailMdl.getSharedKeyFieldHashCode()) {
                    String message = "<div style='float:right; margin-right: 5px;'>";
                    message += "Record already exist with following details - <br>";
                    message += (DmShared.xNull(txtHItemName.getText()).trim().equals("")) ? "" : "<b>Item:</b> " + DmShared.xNull(txtHItemName.getText()) + "<br>";
                    message += (DmShared.xNull(propertyMdl.getBatchNo()).trim().equals("")) ? "" : "<b>Batch No.:</b> " + DmShared.xNull(propertyMdl.getBatchNo()) + "<br>";
                    message += (DmShared.xNull(propertyMdl.getColourName()).trim().equals("")) ? "" : "<b>Colour:</b> " + DmShared.xNull(propertyMdl.getColourName()) + "<br>";

                    message += (DmShared.xNull(propertyMdl.getSizeName()).trim().equals("")) ? "" : "<b>Size:</b> " + DmShared.xNull(propertyMdl.getSizeName()) + "<br>";
                    message += (DmShared.xNull(propertyMdl.getItemMakeName()).trim().equals("")) ? "" : "<b>Item Make:</b> " + DmShared.xNull(propertyMdl.getItemMakeName()) + "<br>";
                    message += (DmShared.vNull(propertyMdl.getMrp()) == 0) ? "" : "<b>MRP:</b> " + DmClient.vNull(propertyMdl.getMrp(), "0.00") + "<br>";
                    message += (DmShared.dNull(propertyMdl.getManufacturingDate()).trim().equals("")) ? "" : "<b>Manufacturing Date:</b> " + DmShared.dNull(propertyMdl.getManufacturingDate()) + "<br>";
                    message += (DmShared.dNull(propertyMdl.getExpiryDate()).trim().equals("")) ? "" : "<b>Expiry Date:</b> " + DmShared.dNull(propertyMdl.getExpiryDate()) + "<br>";
                    message += "</div>";
                    new DmAlertMessageBox(message, txtHItemName).show();
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
        ourRow.setRemark(DmShared.xNull(txtRemark.getText()));
        ourRow.setIbCode(DmShared.xNull(txtProperties.getTag()));
        ourRow.setPropertyDisplay(DmShared.xNull(txtProperties.getText()));
        ourRow.setQuantity(DmShared.xNull(txtQuantity.getText()));
        ourRow.setBestBefore(bestBefore);
        ourRow.setItemProperty(DmShared.xNull(txtHItemName.getTag2nd()));
        ourRow.setPurchaseUnitConversionRate(DmShared.xNull(txtSKU.getTag()));
        ourRow.setSkuDecimalPlaces(DmShared.xNull(txtSKU.getTag2nd()));
        ourRow.setPropertyMdl(propertyMdl);
        ourRow.setFromDeptStk(DmShared.xNull(txtRemark.getTag()));
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
        } else if (event.getSource() == aStock && !DmShared.xNull(txtHItemName.getTag()).equalsIgnoreCase("")) {
            aStockShow();
        } else {
            super.onClick(event);
        }
    }

    public ItemTransferGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Item Detail", "450px", "140px");
        srvAsync = new ServiceInvoker<ItemTransfer_SrvAsync>(GWT.create(ItemTransfer_Srv.class), ItemTransfer.class.getName());
        textHelpEvents();
        aProperties.addClickHandler(this);
        txtProperties.addKeyUpHandler(this);
        txtProperties.addKeyDownHandler(this);
        aStock.addClickHandler(this);
    }

    private void manageDisplay() {
        txtSKU.setEnabled(false);
    }

    private void calculate() {
        txtQuantity.setText(DmClient.vNull(txtQuantity.getText(), DmShared.getNumberFormat(DmShared.iNull(txtSKU.getTag2nd()).intValue())));
    }
}
