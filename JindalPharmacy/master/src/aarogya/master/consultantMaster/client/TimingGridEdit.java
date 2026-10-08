package aarogya.master.consultantMaster.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import aarogya.master.consultantMaster.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public class TimingGridEdit<T1, T2> extends TimingGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(GrdTiming_Mdl grdTiming);

        public void onDelete();
    }

    private final ServiceInvoker<ConsultantMaster_SrvAsync> SrvAsync;
    private CallBack callBack;
    private GrdTiming_Mdl parentRow;
    private int hashCode = 0;

    private void textHelpEvents() {
        txtHWeek.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHWeek(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHWeek).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHWeek.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHWeek).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
        txtHWeek.setFocus(true);
    }

    @Override
    public void displayRow(T1 row) {
        GrdTiming_Mdl ourRow = (GrdTiming_Mdl) row;
        parentRow = new GrdTiming_Mdl();

        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtFrom.setText(DmClient.vNull(ourRow.getFromTime(), "0.00"));
        txtTo.setText(DmClient.vNull(ourRow.getToTime(), "0.00"));
        txtOPD.setText(DmClient.vNull(ourRow.getOpd(), "0"));
        txtAppointmentAdvance.setText(DmClient.vNull(ourRow.getAppointmentAdvance(), "0"));
        txtAppointment.setText(DmClient.vNull(ourRow.getAppointment(), "0"));
        txtEmergency.setText(DmClient.vNull(ourRow.getEmergency(), "0"));
        txtHWeek.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHWeek.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtMinutes.setText(DmClient.vNull(ourRow.getGraceMin(), "0"));
        txtPatient.setText(DmClient.vNull(ourRow.getPatient(), "0"));
        txtReVisit.setText(DmClient.vNull(ourRow.getReVisit(), "0"));
        txtOnlineCharge.setText(DmClient.vNull(ourRow.getOnlineCharge(), "0"));
        txtOnlinePatient.setText(DmClient.vNull(ourRow.getOnlinePatient(), "0"));
        txtPayClinic.setText(DmClient.vNull(ourRow.getPayClinic(), "0"));
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdTiming_Mdl();
    }

    @Override
    public void onOk() {

        if (DmShared.xNull(txtHWeek.getTag()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Day is mandatory.", txtHWeek).show();
            return;
        }

        if (DmShared.vNull(txtFrom.getText()).doubleValue() > DmShared.vNull(txtTo.getText()).doubleValue()) {
            new DmAlertMessageBox("Invalid Time. From-Time is less than To-Time.", txtFrom).show();
            return;
        }
        for (GrdTiming_Mdl timing : (List<GrdTiming_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
            if (timing.getSharedKeyFieldHashCode() != hashCode) {
                if (DmShared.xNull(txtHWeek.getTag()).equalsIgnoreCase(DmShared.xNull(timing.getSharedKeyFieldCode()))) {
                    if ((DmShared.vNull(txtFrom.getText()).doubleValue() >= DmShared.vNull(timing.getFromTime()).doubleValue()) && (DmShared.vNull(txtFrom.getText()).doubleValue() <= DmShared.vNull(timing.getToTime()).doubleValue())) {
                        new DmAlertMessageBox("Duplicate Schedule on same day.", txtFrom).show();
                        return;
                    }
                    if ((DmShared.vNull(txtFrom.getText()).doubleValue() >= DmShared.vNull(timing.getFromTime()).doubleValue()) && (DmShared.vNull(txtTo.getText()).doubleValue() <= DmShared.vNull(timing.getFromTime()).doubleValue())) {
                        new DmAlertMessageBox("Duplicate Schedule on same day.", txtFrom).show();
                        return;
                    }
                    if ((DmShared.vNull(txtTo.getText()).doubleValue() >= DmShared.vNull(timing.getFromTime()).doubleValue()) && (DmShared.vNull(txtTo.getText()).doubleValue() <= DmShared.vNull(timing.getToTime()).doubleValue())) {
                        new DmAlertMessageBox("Duplicate Schedule on same day.", txtTo).show();
                        return;
                    }
                    if ((DmShared.vNull(txtTo.getText()).doubleValue() >= DmShared.vNull(timing.getToTime()).doubleValue()) && (DmShared.vNull(txtFrom.getText()).doubleValue() <= DmShared.vNull(timing.getToTime()).doubleValue())) {
                        new DmAlertMessageBox("Duplicate Schedule on same day.", txtTo).show();
                        return;
                    }
                }
            }
        }

        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdTiming_Mdl ourRow = (GrdTiming_Mdl) row;

        ourRow.setEmergency(DmClient.vNull(txtEmergency.getText(), "0"));
        ourRow.setFromTime(DmClient.vNull(txtFrom.getText(), "0.00"));
        ourRow.setGraceMin(DmClient.vNull(txtMinutes.getText(), "0"));
        ourRow.setOpd(DmClient.vNull(txtOPD.getText(), "0"));
        ourRow.setOnlineCharge(DmClient.vNull(txtOnlineCharge.getText(), "0"));
        ourRow.setOnlinePatient(DmClient.vNull(txtOnlinePatient.getText(), "0"));
        ourRow.setPayClinic(DmClient.vNull(txtPayClinic.getText(), "0"));
        ourRow.setPatient(DmClient.vNull(txtPatient.getText(), "0"));
        ourRow.setAppointment(DmClient.vNull(txtAppointment.getText(), "0"));
        ourRow.setAppointmentAdvance(DmClient.vNull(txtAppointmentAdvance.getText(), "0"));
        ourRow.setReVisit(DmClient.vNull(txtReVisit.getText(), "0"));
        ourRow.setToTime(DmClient.vNull(txtTo.getText(), "0.00"));
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHWeek.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHWeek.getText()));
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

    public TimingGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Timing Detail", "500px", "140px");
        SrvAsync = new ServiceInvoker<>(GWT.create(ConsultantMaster_Srv.class), ConsultantMaster.class.getName());
        textHelpEvents();
    }
}
