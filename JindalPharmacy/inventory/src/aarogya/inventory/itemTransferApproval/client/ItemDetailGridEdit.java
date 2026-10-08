package aarogya.inventory.itemTransferApproval.client;

import aarogya.inventory.itemTransferApproval.client.*;
import aarogya.inventory.itemTransferApproval.shared.*;
import aarogya.selfToolkit.itemPropertyPopup.client.ItemPropertyPopup;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;

public class ItemDetailGridEdit<T1, T2> extends ItemDetailGridEdit_Dsg<T1, T2> implements BlurHandler {

    interface CallBack {

        public void onOk(GrdItemDetail_Mdl grdItemDetail);

        public void onDelete();
    }

    ServiceInvoker<ItemTransferApproval_SrvAsync> SrvAsync;
    CallBack callBack;
    private GrdItemDetail_Mdl parentRow;
    String mode;
    final ItemPropertyPopup propertyPopup = new ItemPropertyPopup();
    private ItemProperty_Mdl propertyPopup_Mdl = new ItemProperty_Mdl();

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtApprovedQty)) {
            calculate();
        }
    }

    private void calculate() {
        txtApprovedQty.setText(DmClient.vNull(txtApprovedQty.getText(), DmShared.getNumberFormat(DmShared.iNull(txtAprUnit.getTag2nd()).intValue())));
    }

    private void manageEvent() {
        propertyPopup.addCallBack(new ItemPropertyPopup.CallBack() {
            @Override
            public void onOk(ItemProperty_Mdl property_Mdl) {
                propertyPopup_Mdl = property_Mdl;
                txtItemName.setFocus(true);
            }

            @Override
            public void onClose() {

            }
        });

        btnPopup.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                showPropertyPopUp();
            }
        });
    }

    private void showPropertyPopUp() {
        String propCode = "", property = DmShared.xNull(txtItemName.getTag2nd());
        if (!DmShared.xNull(txtItemName.getTag()).equalsIgnoreCase("")) {
            for (String prop : DmShared.xNull(property).split(",")) {
                if (DmShared.xNull(prop).equalsIgnoreCase(ItemProperty_Mdl.Properties.COLOUR.getPropertyManualCode())
                        || DmShared.xNull(prop).equalsIgnoreCase(ItemProperty_Mdl.Properties.SIZE.getPropertyManualCode())) {
                    if (!DmShared.xNull(propCode).equalsIgnoreCase("")) {
                        propCode += ",";
                    }
                    propCode += prop;
                }
            }
            if (!DmShared.xNull(propCode).equalsIgnoreCase("")) {
                propertyPopup.show(mode, propCode, "0", propertyPopup_Mdl, null, false,false,STEnum.ItemBatchType.NON_EXPIRED_BATCH);
            }
        }
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, String mode) {
        super.showRelativeTo(currentRowIndex, 1, okNeeded, false, deleteNeeded);
        this.mode = mode;

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtApprovedQty.setFocus(true);
            }
        });
    }

    @Override
    public void displayRow(T1 row) {
        GrdItemDetail_Mdl ourRow = (GrdItemDetail_Mdl) row;
        parentRow = ourRow;

        if (DmShared.xNull(ourRow.getIsStatus()).equalsIgnoreCase("1")
                || DmShared.xNull(ourRow.getIsStatus()).equalsIgnoreCase("2")) {
            txtApprovedQty.setEnabled(false);
        } else {
            txtApprovedQty.setEnabled(true);
        }
        txtItemName.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtItemName.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtItemName.setTag2nd(DmShared.xNull(ourRow.getItemProperty()));

        txtAprUnit.setText(DmShared.xNull(ourRow.getSkuName()));
        txtAprUnit.setTag2nd(DmShared.xNull(ourRow.getDecimalPlace()));
        txtIndUnit.setText(DmShared.xNull(ourRow.getSkuName()));

        txtIndQty.setText(DmShared.xNull(ourRow.getIndQty()));
        txtApprovedQty.setText(DmShared.xNull(ourRow.getApprovedQty()));
        propertyPopup_Mdl.setColourCode(ourRow.getColorCode());
        propertyPopup_Mdl.setColourName(ourRow.getColor());
        propertyPopup_Mdl.setSizeCode(ourRow.getSizeCode());
        propertyPopup_Mdl.setSizeName(ourRow.getSize());
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdItemDetail_Mdl();
    }

    @Override
    public void onOk() {
        if (DmShared.vNull(txtApprovedQty.getText()).doubleValue() > DmShared.vNull(txtIndQty.getText()).doubleValue()) {
            new DmAlertMessageBox("Apr. Qty. should be less than or equal to Indent Qty.", txtApprovedQty).show();
            return;
        }
        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdItemDetail_Mdl ourRow = (GrdItemDetail_Mdl) row;
        ourRow = parentRow;

        ourRow.setApprovedQty(DmShared.xNull(txtApprovedQty.getText()));
        ourRow.setDecimalPlace(DmShared.xNull(txtAprUnit.getTag2nd()));
        ourRow.setItemProperty(DmShared.xNull(txtItemName.getTag2nd()));
        ourRow.setColorCode(propertyPopup_Mdl.getColourCode());
        ourRow.setColor(propertyPopup_Mdl.getColourName());
        ourRow.setSizeCode(propertyPopup_Mdl.getSizeCode());
        ourRow.setSize(propertyPopup_Mdl.getSizeName());

        parentRow = ourRow;

        Save(ourRow);
    }

    private void Save(GrdItemDetail_Mdl approval_Mdl) {
        try {
            SrvAsync.invokeRPC().save(approval_Mdl, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        callBack.onOk(parentRow);
                    } else {
                        topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void topControlSaveError(int errorNo, String errorMessage) {
        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtApprovedQty).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtApprovedQty).show();
                break;
        }
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

    public ItemDetailGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Indent Detail", "350px", "120px");
        SrvAsync = new ServiceInvoker<>(GWT.create(ItemTransferApproval_Srv.class), ItemTransferApproval.class.getName());
        txtReqDate.addBlurHandler(this);
        txtApprovedQty.addBlurHandler(this);
        manageDisplay();
        manageEvent();
    }

    public void manageDisplay() {
        txtItemName.setEnabled(false);
        txtReqDate.setEnabled(false);
        txtIndQty.setEnabled(false);
        txtAprUnit.setEnabled(false);
        txtIndUnit.setEnabled(false);
    }

}
