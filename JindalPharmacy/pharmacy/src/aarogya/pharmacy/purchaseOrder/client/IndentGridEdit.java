package aarogya.pharmacy.purchaseOrder.client;

import aarogya.pharmacy.purchaseOrder.shared.GrdIndentDetail_Mdl;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import aarogya.pharmacy.purchaseOrder.shared.PurchaseOrder_Srv;
import aarogya.pharmacy.purchaseOrder.shared.PurchaseOrder_SrvAsync;

public class IndentGridEdit<T1, T2> extends IndentGridEdit_Dsg<T1, T2> implements BlurHandler {

    interface CallBack {

        public void onOk(GrdIndentDetail_Mdl grdIndentEntry);

        public void onDelete();
    }

    ServiceInvoker<PurchaseOrder_SrvAsync> SrvAsync;
    CallBack callBack;
    GrdIndentDetail_Mdl parentRow;
    String dtIndent;
    int hashCode;

    public void manageDisplay() {
        txtAdjustmentQty.setEnabled(false);
        txtIndDate.setEnabled(false);
        txtIndNo.setEnabled(false);
        txtIndSKU.setEnabled(false);
        txtIndSKU.setEnabled(false);
        txtIndentQty.setEnabled(false);
        txtReqDate.setEnabled(false);
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtReqDate)) {
            txtReqDate.setText(DmClient.returnDate(txtReqDate.getText()));
        }
        if (event.getSource().equals(txtOrderQty)) {
            calculate();
        }
    }

    private String getNumberFormat(int decimalPlaces) {
        String returnValue = "0", numberFormat = "";

        if (decimalPlaces > 0) {
            for (int i = 0; i < decimalPlaces; i++) {
                numberFormat += "0";
            }
            returnValue += "." + numberFormat;
        }
        return returnValue;
    }

    private void calculate() {
        txtOrderQty.setText(DmClient.vNull(txtOrderQty.getText(), getNumberFormat(DmShared.iNull(txtIndSKU.getTag2nd()).intValue())));
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, String mode) {
        if (mode.equalsIgnoreCase("Browse")) {
            super.showRelativeTo(currentRowIndex, 1, false, false, false);
        } else {
            super.showRelativeTo(currentRowIndex, 1, okNeeded, false, deleteNeeded);
        }
        txtOrderQty.setFocus(true);
        manageDisplay();
    }

    @Override
    public void displayRow(T1 row) {
        GrdIndentDetail_Mdl ourRow = (GrdIndentDetail_Mdl) row;
        parentRow = new GrdIndentDetail_Mdl();

        parentRow = ourRow;
        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtIndNo.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtIndNo.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtIndDate.setText(DmShared.dNull(ourRow.getIndentDate()));
        txtIndSKU.setText(DmShared.xNull(ourRow.getIndentSKU()));
        txtIndSKU.setTag2nd(DmShared.xNull(ourRow.getDecimalPlaces()));
        txtIndentQty.setText(DmShared.xNull(ourRow.getIndentQty()));
        txtOrderQty.setText(DmShared.xNull(ourRow.getOrderQty()));
        txtReqDate.setText(DmShared.dNull(ourRow.getRequiredDate()));
        txtVSNo.setText(DmShared.xNull(ourRow.getIndentVSNo()));
        txtAdjustmentQty.setText(DmShared.xNull(ourRow.getAdjustedQty()));
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdIndentDetail_Mdl();
    }

    @Override
    public void onOk() {
        if (DmShared.vNull(txtOrderQty.getText()).doubleValue() == 0) {
            new DmAlertMessageBox("Ordered Qty. is mandatory.", txtOrderQty).show();
            return;
        }
        if (DmShared.vNull(txtOrderQty.getText()).doubleValue() > DmShared.vNull(txtIndentQty.getText()).doubleValue()) {
            new DmAlertMessageBox("Ordered Qty. should be less than or equal to Indent Qty.", txtOrderQty).show();
            return;
        }
        if (DmShared.vNull(txtIndentQty.getText()).doubleValue() < (DmShared.vNull(txtOrderQty.getText()).doubleValue() + DmShared.vNull(txtAdjustmentQty.getText()).doubleValue())) {
            new DmAlertMessageBox("Indent Qty. should be less than or equal to Ordered Qty. + Adjusted Qty.", txtOrderQty).show();
            return;
        }
        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdIndentDetail_Mdl ourRow = (GrdIndentDetail_Mdl) row;

        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtIndNo.getTag()));
        ourRow.setIndentDate(DmShared.dNull(txtIndDate.getText()));
        ourRow.setIndentQty(DmShared.xNull(txtIndentQty.getText()));
        ourRow.setIndentSKU(DmShared.xNull(txtIndSKU.getText()));
        ourRow.setDecimalPlaces(DmShared.xNull(txtIndSKU.getTag2nd()));
        ourRow.setIndentVSNo(DmShared.xNull(txtVSNo.getText()));
        ourRow.setOrderQty(DmShared.xNull(txtOrderQty.getText()));
        ourRow.setAdjustedQty(DmShared.xNull(txtAdjustmentQty.getText()));
        ourRow.setRequiredDate(DmShared.dNull(txtReqDate.getText()));
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

    public IndentGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Indent Detail", "500px", "125px");
        SrvAsync = new ServiceInvoker<>(GWT.create(PurchaseOrder_Srv.class), PurchaseOrder.class.getName());
        txtReqDate.addBlurHandler(this);
        txtOrderQty.addBlurHandler(this);
    }
}
