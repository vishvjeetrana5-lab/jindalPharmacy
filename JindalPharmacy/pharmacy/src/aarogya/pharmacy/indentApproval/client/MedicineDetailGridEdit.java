package aarogya.pharmacy.indentApproval.client;

import aarogya.pharmacy.indentApproval.shared.*;
import aarogya.selfToolkit.stStatic.client.STClient;
import aarogya.selfToolkit.stStatic.shared.ConvertedQuantity;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;

public class MedicineDetailGridEdit<T1, T2> extends MedicineDetailGridEdit_Dsg<T1, T2> implements BlurHandler {

    interface CallBack {

        public void onOk(GrdMedicineDetail_Mdl grdItemDetail);

        public void onDelete();
    }

    ServiceInvoker<IndentApproval_SrvAsync> SrvAsync;
    CallBack callBack;
    private GrdMedicineDetail_Mdl parentRow;

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtApprovedQty)) {
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
        txtApprovedQty.setText(DmClient.vNull(txtApprovedQty.getText(), getNumberFormat(DmShared.iNull(txtAprUnit.getTag2nd()).intValue())));
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {
        super.showRelativeTo(currentRowIndex, 1, okNeeded, false, deleteNeeded);

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtApprovedQty.setFocus(true);
            }
        });
    }

    @Override
    public void displayRow(T1 row) {
        GrdMedicineDetail_Mdl ourRow = (GrdMedicineDetail_Mdl) row;
        parentRow = ourRow;

        if (DmShared.xNull(ourRow.getIsStatus()).equalsIgnoreCase("1")
                || DmShared.xNull(ourRow.getIsStatus()).equalsIgnoreCase("2")) {
            txtApprovedQty.setEnabled(false);
        } else {
            txtApprovedQty.setEnabled(true);
        }
        txtMedicineName.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtMedicineName.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtReqDate.setText(DmShared.dNull(ourRow.getReqDate()));

        txtAprUnit.setText(DmShared.xNull(ourRow.getSkuName()));
        txtAprUnit.setTag2nd(DmShared.xNull(ourRow.getDecimalPlace()));
        txtIndUnit.setText(DmShared.xNull(ourRow.getSkuName()));

        txtIndQty.setText(DmShared.xNull(ourRow.getIndQty()));
        txtApprovedQty.setText(DmShared.xNull(ourRow.getApprovedQty()));

    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdMedicineDetail_Mdl();
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
        GrdMedicineDetail_Mdl ourRow = (GrdMedicineDetail_Mdl) row;
        ourRow = parentRow;

        ourRow.setApprovedQty(DmShared.xNull(txtApprovedQty.getText()));
        ourRow.setDecimalPlace(DmShared.xNull(txtAprUnit.getTag2nd()));

        parentRow = ourRow;

        Save(ourRow);
    }

    private void Save(GrdMedicineDetail_Mdl approval_Mdl) {
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

    public MedicineDetailGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Indent Detail", "350px", "120px");
        SrvAsync = new ServiceInvoker<>(GWT.create(IndentApproval_Srv.class), IndentApproval.class.getName());
        txtReqDate.addBlurHandler(this);
        txtApprovedQty.addBlurHandler(this);
        manageDisplay();
    }

    public void manageDisplay() {
        txtMedicineName.setEnabled(false);
        txtReqDate.setEnabled(false);
        txtIndQty.setEnabled(false);
        txtAprUnit.setEnabled(false);
        txtIndUnit.setEnabled(false);
    }

}
