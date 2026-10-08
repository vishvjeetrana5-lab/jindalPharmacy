package aarogya.master.patientScheduleMaster.client;

import aarogya.master.patientScheduleMaster.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public class PatientScheduleMasterGridEdit<T1, T2> extends PatientScheduleMasterGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(GrdPatntSchdulMster_Mdl patientScheduleMaster_Mdl);

        public void onDelete();
    }

    private final ServiceInvoker<PatientScheduleMaster_SrvAsync> srvAsync;
    private CallBack callBack;
    private int hashCode = 0;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void textHelpEvents() {
        txtHConsultant.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHConsultant(helper, blnToEndOfFile, "", new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHConsultant).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHConsultant.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHConsultant).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHGuiTemplate.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHGuiTemplate(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHGuiTemplate).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHGuiTemplate.setData(result.getData(), result.getPaging());

                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHGuiTemplate).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });

        txtHMachinePlaces.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHMachineBed(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMachinePlaces).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHMachinePlaces.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMachinePlaces).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, PatientScheduleMaster patientScheduleMastr) {
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            @Override
            public void execute() {
                txtDescription.setFocus(true);
            }
        });
    }

    @Override
    public void onOk() {
        try {
            if (DmShared.xNull(txtDescription.getText()).trim().equalsIgnoreCase("")) {
                new DmAlertMessageBox("Procedure is mandatory.", txtDescription).show();
                return;
            }
            if (DmShared.iNull(txtDays.getText()).longValue() <= 0) {
                new DmAlertMessageBox("Day is mandatory and sholud be greater than 0.", txtDays).show();
                return;
            }
            if (DmShared.iNull(txtCyclicMinute.getText()).longValue() <= 0) {
                new DmAlertMessageBox("Cycle Time (Minutes) is mandatory and sholud be greater than 0.", txtCyclicMinute).show();
                return;
            }
//            if (DmShared.iNull(txtHGuiTemplate.getTag()).longValue() <= 0) {
//                new DmAlertMessageBox("Gui Template is mandatory.", txtHGuiTemplate).show();
//                return;
//            }
            for (GrdPatntSchdulMster_Mdl service : (List<GrdPatntSchdulMster_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
                if (DmShared.xNull(service.getProcedures()).equalsIgnoreCase(DmShared.removeSpace(DmShared.xNull(txtDescription.getText().trim())))) {
                    if (!DmClient.vNull(service.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                        new DmAlertMessageBox("Procedure already exists.", txtDescription).show();
                        return;
                    }
                }
            }

            proceedOk();
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtDescription).show();
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
    public void manageOk(T1 row) {
        GrdPatntSchdulMster_Mdl ourRow = (GrdPatntSchdulMster_Mdl) row;

        ourRow.setSharedKeyFieldCode(DmShared.removeSpace(DmShared.xNull(txtDescription.getText().trim())));
        ourRow.setProcedures(DmShared.removeSpace(DmShared.xNull(txtDescription.getText().trim())));
        ourRow.setDays(DmShared.xNull(txtDays.getText()));
        ourRow.setCycleTime(DmShared.xNull(txtCyclicMinute.getText()));
        ourRow.setPrintSNo(DmShared.xNull(txtPrintSNo.getText()));
        ourRow.setConsultant(DmShared.xNull(txtHConsultant.getText()));
        ourRow.setConsultantCode(DmShared.iNull(txtHConsultant.getTag()).toString());
        ourRow.setMachine(DmShared.xNull(txtHMachinePlaces.getText()));
        ourRow.setMachineCode(DmShared.iNull(txtHMachinePlaces.getTag()).toString());
        ourRow.setGuiTemplate(DmShared.xNull(txtHGuiTemplate.getText()));
        ourRow.setGuiTemplateCode(DmShared.iNull(txtHGuiTemplate.getTag()).toString());

        callBack.onOk(ourRow);
    }

    @Override
    public void manageDelete() {
        callBack.onDelete();
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdPatntSchdulMster_Mdl();
    }

    @Override
    public void displayRow(T1 row) {
        GrdPatntSchdulMster_Mdl ourRow = (GrdPatntSchdulMster_Mdl) row;
        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtDescription.setText(DmShared.xNull(ourRow.getProcedures()));
        txtDays.setText(DmShared.xNull(ourRow.getDays()));
        txtCyclicMinute.setText(DmShared.xNull(ourRow.getCycleTime()));
        txtPrintSNo.setText(DmShared.xNull(ourRow.getPrintSNo()));
        txtHConsultant.setTag(DmShared.iNull(ourRow.getConsultantCode()).toString());
        txtHConsultant.setText(DmShared.xNull(ourRow.getConsultant()));
        txtHMachinePlaces.setTag(DmShared.iNull(ourRow.getMachineCode()).toString());
        txtHMachinePlaces.setText(DmShared.xNull(ourRow.getMachine()));
        txtHGuiTemplate.setTag(DmShared.iNull(ourRow.getGuiTemplateCode()).toString());
        txtHGuiTemplate.setText(DmShared.xNull(ourRow.getGuiTemplate()));
    }

    public PatientScheduleMasterGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Procedure Detail", "400px", "168px");
        srvAsync = new ServiceInvoker<PatientScheduleMaster_SrvAsync>(GWT.create(PatientScheduleMaster_Srv.class), PatientScheduleMaster.class.getName());
        textHelpEvents();
    }
}
