package aarogya.selfToolkit.patientRequisition.client;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import aarogya.selfToolkit.patientRequisition.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;
import com.google.gwt.core.client.Scheduler;

public class RequisitionGridEdit<T1, T2> extends RequisitionGridEdit_Dsg<T1, T2> implements BlurHandler {

    interface CallBack {

        public void onOk(GrdRequisitionMedicine_Mdl details_Mdl);

        public void onDelete();
    }

    private final ServiceInvoker<PatientRequisition_SrvAsync> SrvAsync;
    private CallBack callBack;
    private GrdRequisitionMedicine_Mdl parentRow;
    private int hashCode;
    private String admitDate = "", currentTime = "", currentDate = "";

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtReqDate) {
            txtReqDate.setText(DmClient.returnDate(txtReqDate.getText()));
        }
    }

    private void textHelpEvents() {
        txtHMedicineName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHMedicine_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHMedicineName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHMedicine_Mdl>>>() {

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
                reset();
                if (blnStatus && t1Row != null) {
                    txtUnit.setText(DmShared.xNull(t1Row.getUnitName()));
                } else {
                    txtUnit.setText("");
                }
            }

        });
    }

    private void getServerTime() {
        SrvAsync.invokeRPC().getServerDateTime(new AsyncCallback<String[]>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtReqTime).show();
            }

            @Override
            public void onSuccess(String[] result) {
                currentTime = (result[1]);
                currentDate = (result[0]);

            }
        });
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, String admitDate) {
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
        this.admitDate = admitDate;
        txtReqDate.setText(currentDate);
        txtReqTime.setText(currentTime);

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtHMedicineName.setFocus(true);
            }
        });
    }

    @Override
    public void displayRow(T1 row) {
        GrdRequisitionMedicine_Mdl ourRow = (GrdRequisitionMedicine_Mdl) row;
        parentRow = new GrdRequisitionMedicine_Mdl();
        parentRow = ourRow;

        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtHMedicineName.setTag(ourRow.getSharedKeyFieldCode());
        txtHMedicineName.setText(ourRow.getSharedKeyFieldName());
        txtReqTime.setText(ourRow.getReqTime());
        txtReqDate.setText(ourRow.getReqDate());
        txtUnit.setText(DmShared.xNull(ourRow.getUnitName()));
        txtQty.setText(DmClient.vNull(ourRow.getQty(), "0.00"));
        txtRemark.setText(ourRow.getRemark());

        txtHMedicineName.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdRequisitionMedicine_Mdl();
    }

    @Override
    public void onOk() {
        if (DmShared.xNull(parentRow.getIsStatus()).equalsIgnoreCase("1")) {
            new DmAlertMessageBox("Record has been cancelled can't change record.", txtReqDate).show();
            return;
        }
        if (DmShared.xNull(parentRow.getIsStatus()).equalsIgnoreCase("2")) {
            new DmAlertMessageBox("Record has been completed, can't change record.", txtReqDate).show();
            return;
        }

        if (DmShared.xNull(txtHMedicineName.getTag()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Medicine is mandatory.", txtHMedicineName).show();
            return;
        }
        if (DmShared.iNull(txtQty.getText()).doubleValue() == 0) {
            new DmAlertMessageBox("Req. Qty. is mandatory.", txtQty).show();
            return;
        }
        if (DmShared.xNull(txtReqDate.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Req. Date is mandatory.", txtReqDate).show();
            return;
        }

        if (DmShared.xNull(txtReqTime.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Req. Time is mandatory.", txtReqTime).show();
            return;
        }
        if ((!txtReqDate.getText().trim().equalsIgnoreCase(""))) {
            if ((DmShared.convertDate(txtReqDate.getText()).getTime() < (DmShared.convertDate(DmClient.getLoginDate())).getTime())) {
                new DmAlertMessageBox("Req. Date should be greater than or equal to Current Date.", txtReqDate).show();
                return;
            }
        }
        if (!DmShared.xNull(admitDate).equalsIgnoreCase("")) {
            if (DmShared.convertDate(txtReqDate.getText()).getTime() < DmShared.convertDate(admitDate).getTime()) {
                new DmAlertMessageBox("Req. Date can't be less than Admit Date ", txtReqDate).show();
                return;
            }
        }
        if ((!txtReqDate.getText().trim().equalsIgnoreCase(""))) {
            if ((DmShared.convertDate(txtReqDate.getText()).getTime() == (DmShared.convertDate(DmClient.getLoginDate())).getTime())) {
                if (DmShared.iNull(txtReqTime.getText()).doubleValue() < DmShared.iNull(currentTime).doubleValue()) {
                    new DmAlertMessageBox("Req. Time should be greater than or equal to Current Time.", txtReqTime).show();
                    return;
                }
            }
        }

        List<GrdRequisitionMedicine_Mdl> lstTemp = (List<GrdRequisitionMedicine_Mdl>) getDmGrid().getGrid().getStore().getAll();
        for (int i = 0; i < lstTemp.size(); i++) {
            GrdRequisitionMedicine_Mdl get = lstTemp.get(i);
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
        GrdRequisitionMedicine_Mdl ourRow = (GrdRequisitionMedicine_Mdl) row;

        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHMedicineName.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHMedicineName.getText()));
        ourRow.setReqDate(DmShared.xNull(txtReqDate.getText()));
        ourRow.setReqTime(DmShared.xNull(txtReqTime.getText()));
        ourRow.setRemark(DmShared.xNull(txtRemark.getText()));
        ourRow.setUnitName(DmShared.xNull(txtUnit.getText()));
        ourRow.setQty(DmClient.vNull(txtQty.getText(), "0"));

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
        super(grid, "Requisition Detail", "450px", "115px");
        SrvAsync = new ServiceInvoker<PatientRequisition_SrvAsync>(GWT.create(PatientRequisition_Srv.class), PatientRequisition.class.getName());
        textHelpEvents();
        manageEvent();
        manageDisplay();
        getServerTime();
    }

    private void manageEvent() {
        txtReqDate.addBlurHandler(this);
    }

    private void manageDisplay() {
        txtUnit.setEnabled(false);
    }

    private void reset() {
        txtUnit.setText("");
    }

}
