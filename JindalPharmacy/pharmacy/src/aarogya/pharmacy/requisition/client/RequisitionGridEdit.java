package aarogya.pharmacy.requisition.client;

import aarogya.pharmacy.requisition.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.*;
import java.util.List;

public class RequisitionGridEdit<T1, T2> extends RequisitionGridEdit_Dsg<T1, T2> implements BlurHandler {

    interface CallBack {

        public void onOk(GrdMedicineDetail_Mdl details_Mdl);

        public void onDelete();
    }

    ServiceInvoker<Requisition_SrvAsync> SrvAsync;
    CallBack callBack;
    private GrdMedicineDetail_Mdl parentRow;
    private int hashCode;
    private Requisition requisition;
    String previousItemCode = "";
    String toDepartment = "";

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtReqDate) {
            txtReqDate.setText(DmClient.returnDate(txtReqDate.getText()));
        }
        if (event.getSource().equals(txtQty)) {
            calculate();
        }
    }

    private void textHelpEvents() {
        txtHMedicineName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHMedicine_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                String resDep = "";
                if (!DmShared.xNull(requisition.txtHDepartmentBy.getTag()).equalsIgnoreCase("")) {
                    if (!resDep.equalsIgnoreCase("")) {
                        resDep += ",";
                    }
                    resDep += DmShared.xNull(requisition.txtHDepartmentBy.getTag());
                }
                if (!DmShared.xNull(requisition.txtHDepartmentTo.getTag()).equalsIgnoreCase("")) {
                    if (!resDep.equalsIgnoreCase("")) {
                        resDep += ",";
                    }
                    resDep += DmShared.xNull(requisition.txtHDepartmentTo.getTag());
                }
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHMedicineName(helper, blnToEndOfFile, resDep, toDepartment, new AsyncCallback<DmResultPageing<List<TxtHMedicine_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMedicineName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHMedicine_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHMedicineName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMedicineName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHMedicine_Mdl t1Row) {
                if (DmShared.iNull(previousItemCode).longValue() != DmShared.iNull(txtHMedicineName.getTag()).longValue()) {
                    reset();
                    previousItemCode = DmShared.xNull(txtHMedicineName.getTag());
                }
                if (blnStatus && t1Row != null) {
                    txtUnit.setText(DmShared.xNull(t1Row.getSkuName()));
                    txtUnit.setTag(DmShared.xNull(t1Row.getAllowDecimalPlaces()));
                    txtPurchaseUnit.setText(DmShared.xNull(t1Row.getPurchaseUnit()));
                    txtPurchaseUnit.setTag(DmShared.xNull(t1Row.getPurchaseDecimalPlaces()));
                    calculate();
                }
            }

        });
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, String toDepartment, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, Requisition requisition) {
        super.showCenter(currentRowIndex, okNeeded, newNeeded, deleteNeeded);
        txtQty.addBlurHandler(this);
        this.toDepartment = toDepartment;
        this.requisition = requisition;
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtHMedicineName.setFocus(true);
            }
        });
    }

    @Override
    public void displayRow(T1 row) {
        GrdMedicineDetail_Mdl ourRow = (GrdMedicineDetail_Mdl) row;
        parentRow = new GrdMedicineDetail_Mdl();
        parentRow = ourRow;

        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtHMedicineName.setTag2nd(ourRow.getvSNo());
        previousItemCode = DmShared.xNull(ourRow.getSharedKeyFieldCode());
        txtHMedicineName.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHMedicineName.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));

        txtReqDate.setText(ourRow.getDate());
        txtQty.setText(DmShared.xNull(ourRow.getQty()));
        txtUnit.setText(ourRow.getSkuName());
        txtUnit.setTag(ourRow.getAllowDecimalPlaces());
        txtPurchaseUnit.setText(ourRow.getPurchaseUnit());
        txtPurchaseUnit.setTag(ourRow.getPurchaseDecimalPlaces());
        txtRemark.setText(ourRow.getRemark());
        txtCurrPosition.setText(ourRow.getItemCurrPosition());

        txtHMedicineName.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdMedicineDetail_Mdl();
    }

    @Override
    public void onOk() {
        if (DmShared.xNull(parentRow.getIsStatus()).equalsIgnoreCase("1")) {
            new DmAlertMessageBox("Record has been canceled can't change record.", txtReqDate).show();
            return;
        }
        if (DmShared.xNull(parentRow.getIsStatus()).equalsIgnoreCase("2")) {
            new DmAlertMessageBox("Record has been completed, can't change record.", txtReqDate).show();
            return;
        }

        if (DmShared.xNull(txtHMedicineName.getTag()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Medicine Name is mandatory.", txtHMedicineName).show();
            return;
        }
        if (DmShared.vNull(txtQty.getText()).doubleValue() == 0) {
            new DmAlertMessageBox("Quantity is mandatory.", txtQty).show();
            return;
        }

        if (!DmShared.dNull(requisition.txtVDate.getText()).trim().equals("")) {
            if (!DmShared.dNull(txtReqDate.getText()).equalsIgnoreCase("")) {
                if (DmShared.convertDate(txtReqDate.getText()).getTime() < DmShared.convertDate(requisition.txtVDate.getText()).getTime()) {
                    new DmAlertMessageBox("Require Date Can't be Less Then Requisition Date.", txtReqDate).show();
                    return;
                }
            }
        }

        List<GrdMedicineDetail_Mdl> lstTemp = (List<GrdMedicineDetail_Mdl>) getDmGrid().getGrid().getStore().getAll();
        for (int i = 0; i < lstTemp.size(); i++) {
            GrdMedicineDetail_Mdl get = lstTemp.get(i);
            if (DmShared.xNull(txtHMedicineName.getTag()).equalsIgnoreCase(DmShared.xNull(get.getSharedKeyFieldCode()))) {
                if (DmShared.vNull(hashCode).doubleValue() != DmShared.vNull(get.getSharedKeyFieldHashCode()).doubleValue()) {
                    new DmAlertMessageBox(txtHMedicineName.getText() + " already exists.", txtHMedicineName).show();
                    return;
                }
            }
        }

        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdMedicineDetail_Mdl ourRow = (GrdMedicineDetail_Mdl) row;

        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHMedicineName.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHMedicineName.getText()));
        ourRow.setDate(DmShared.xNull(txtReqDate.getText()));
        ourRow.setRemark(DmShared.xNull(txtRemark.getText()));
        ourRow.setSkuName(DmShared.xNull(txtUnit.getText()));
        ourRow.setPurchaseUnit(DmShared.xNull(txtPurchaseUnit.getText()));
        ourRow.setPurchaseDecimalPlaces(DmShared.xNull(txtPurchaseUnit.getTag()));
        ourRow.setQty(DmShared.xNull(txtQty.getText()));
        ourRow.setvSNo(DmShared.xNull(txtHMedicineName.getTag2nd()));
        ourRow.setAllowDecimalPlaces(DmShared.xNull(txtUnit.getTag()));

        callBack.onOk(ourRow);
    }

    @Override
    public void onNew() {
        proceedNew();
    }

    @Override
    public void onDelete() {
        if (DmShared.xNull(parentRow.getIsStatus()).equalsIgnoreCase("1")) {
            new DmAlertMessageBox("Record has been canceled can't delete record.", txtReqDate).show();
            return;
        } else if (DmShared.xNull(parentRow.getIsStatus()).equalsIgnoreCase("2")) {
            new DmAlertMessageBox("Record has been completed, can't change record.", txtReqDate).show();
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

    public RequisitionGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Requisition Detail", "500px", "160px");
        SrvAsync = new ServiceInvoker<>(GWT.create(Requisition_Srv.class), Requisition.class.getName());
        textHelpEvents();
        manageEvent();
        manageDisplay();
    }

    private void manageEvent() {
        txtReqDate.addBlurHandler(this);
        txtQty.addBlurHandler(this);
    }

    private void manageDisplay() {
        txtCurrPosition.setEnabled(false);
        txtQty.setEnabled(true);
    }

    private void reset() {
        txtQty.setTag("");
        txtQty.setText("");
        txtUnit.setText("");
        txtUnit.setTag("");
        txtUnit.setTag2nd("");
        txtPurchaseUnit.setText("");
        txtPurchaseUnit.setTag("");
    }

    private void calculate() {
        txtQty.setText(DmClient.vNull(DmShared.vNull(txtQty.getText()).doubleValue(), DmShared.getNumberFormat(DmShared.iNull(txtUnit.getTag()).intValue())));
    }
}
