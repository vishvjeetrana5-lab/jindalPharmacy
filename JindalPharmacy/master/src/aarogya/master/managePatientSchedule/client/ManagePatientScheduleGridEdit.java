package aarogya.master.managePatientSchedule.client;

import aarogya.master.managePatientSchedule.shared.*;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public class ManagePatientScheduleGridEdit<T1, T2> extends ManagePatientScheduleGridEdit_Dsg<T1, T2> implements BlurHandler, ClickHandler {

    interface CallBack {

        public void refresh(String schDocId);

    }

    private final ServiceInvoker<ManagePatientSchedule_SrvAsync> srvAsync;
    private CallBack callBack;
    private GrdSchedules_Mdl parentRow;
    private ManagePatientSchedule parantClass;
    private boolean isChangeSchedule = false;
    private String vSno = "", scheduleDocId = "";
    private final ShuffleDays shuffleDays = new ShuffleDays();

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, String scheduleDocId, ManagePatientSchedule patientSchedule) {
        this.parantClass = patientSchedule;
        this.scheduleDocId = scheduleDocId;
        super.showCenter(currentRowIndex, okNeeded, newNeeded, deleteNeeded);

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtDescription.setFocus(true);
            }
        });
        if (DmShared.iNull(vSno).intValue() > 0) {
            flwShuffleDate.add(btnShuffleDate);
        } else {
            flwShuffleDate.clear();
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtScheduleDate)) {
            if (!DmShared.xNull(txtScheduleDate.getText()).equalsIgnoreCase("")) {
                txtScheduleDate.setText(DmClient.returnDate(txtScheduleDate.getText()));
            }
        }

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

    @Override
    public void displayRow(T1 row) {
        isChangeSchedule = false;
        GrdSchedules_Mdl ourRow = (GrdSchedules_Mdl) row;
        parentRow = (GrdSchedules_Mdl) row;
        txtDescription.setTag(DmShared.iNull(scheduleDocId).toString());
        vSno = DmShared.iNull(ourRow.getvSno()).toString();
        txtDescription.setText(DmShared.xNull(ourRow.getDescription()));
        txtScheduleDate.setText(DmShared.dNull(ourRow.getScheduleDate()));
        txtScheduleDate.setTag(DmShared.dNull(ourRow.getScheduleDate()));
        txtCycleTime.setText(DmShared.xNull(ourRow.getCycleTime()));
        txtPrintSNo.setText(DmShared.xNull(ourRow.getPrintSno()));
        txtRemark.setText(DmShared.xNull(ourRow.getRemark()));
        txtHMachinePlaces.setTag(DmShared.iNull(ourRow.getMachineCode()).toString());
        txtHMachinePlaces.setText(DmShared.xNull(ourRow.getMachine()));
        txtHGuiTemplate.setTag2nd(DmShared.iNull(ourRow.getGuiTemplateCode()).toString());
        txtHGuiTemplate.setText(DmShared.xNull(ourRow.getGuiTemplate()));
        txtHGuiTemplate.setTag(DmShared.iNull(ourRow.getGuiConfigCode()).toString());
        txtHConsultant.setTag(DmShared.iNull(ourRow.getConsultantCode()).toString());
        txtHConsultant.setText(DmShared.xNull(ourRow.getConsultantName()));
        txtDescription.setFocus(true);

    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdSchedules_Mdl();
    }

    @Override
    public void onOk() {
        if (DmShared.xNull(parantClass.txtUHID.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("UHID No. is mandatory.", parantClass.txtUHID).show();
            return;
        }
        if (DmShared.xNull(txtDescription.getText()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Procedure is mandatory.", txtDescription).show();
            return;
        }
        if (DmShared.dNull(txtScheduleDate.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Schedule date is mandatory.", txtScheduleDate).show();
            return;
        }
        if ((DmShared.convertDate(txtScheduleDate.getText()).getTime() < (DmShared.convertDate(parantClass.txtEntryDate.getText())).getTime())) {
            new DmAlertMessageBox("Date should be greater than or equal to entry date.", txtScheduleDate).show();
            return;
        }
        if (DmShared.iNull(txtCycleTime.getText()).longValue() <= 0) {
            new DmAlertMessageBox("Cycle Time (Minutes) is mandatory and sholud be greater than 0.", txtCycleTime).show();
            return;
        }

        if (DmShared.iNull(txtHGuiTemplate.getTag()).longValue() <= 0) {
            new DmAlertMessageBox("Gui Template is mandatory.", txtHGuiTemplate).show();
            return;
        }

        if (!DmShared.dNull(parentRow.getScheduleDate()).equalsIgnoreCase("")) {
            isChangeSchedule = DmShared.convertDate(txtScheduleDate.getText()).getTime() != DmShared.convertDate(parentRow.getScheduleDate()).getTime();
        }
        if (isChangeSchedule) {
            new DmConfirmMessageBox("Do you want to re-schedule further dates?", new DialogHideEvent.DialogHideHandler() {
                @Override
                public void onDialogHide(DialogHideEvent event) {
                    if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                        shuffleDateArea();
                    } else if (event.getHideButton() == Dialog.PredefinedButton.NO) {
                        saveSchedule(saveInitialize());
                    }
                }
            }).show();
        } else {
            saveSchedule(saveInitialize());
        }
    }

    @Override
    public void manageOk(T1 row) {
        GrdSchedules_Mdl ourRow = (GrdSchedules_Mdl) row;

        ourRow.setSharedKeyFieldCode(DmShared.iNull(txtDescription.getTag()).toString());
        ourRow.setvSno(DmShared.iNull(vSno).toString());
        ourRow.setDescription(DmShared.xNull(txtDescription.getText()));
        ourRow.setScheduleDate(DmShared.dNull(txtScheduleDate.getText()));
        ourRow.setDoneDate(DmShared.dNull(parentRow.getDoneDate()));
        ourRow.setCycleTime(DmShared.xNull(txtCycleTime.getText()));
        ourRow.setPrintSno(DmShared.xNull(txtPrintSNo.getText()));
        ourRow.setRemark(DmShared.xNull(txtRemark.getText()));
        ourRow.setChangeSchedule(DmShared.convertDate(txtScheduleDate.getText()).getTime() != DmShared.convertDate(parentRow.getScheduleDate()).getTime());
        ourRow.setMachine(DmShared.xNull(txtHMachinePlaces.getText()));
        ourRow.setMachineCode(DmShared.iNull(txtHMachinePlaces.getTag()).toString());
        ourRow.setGuiTemplate(DmShared.xNull(txtHGuiTemplate.getText()));
        ourRow.setGuiTemplateCode(DmShared.iNull(txtHGuiTemplate.getTag2nd()).toString());
        ourRow.setGuiConfigCode(DmShared.iNull(txtHGuiTemplate.getTag()).toString());
        ourRow.setConsultantName(DmShared.xNull(txtHConsultant.getText()));
        ourRow.setConsultantCode(DmShared.iNull(txtHConsultant.getTag()).toString());
        callBack.refresh(DmShared.iNull(txtDescription.getTag()).toString());
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
        srvAsync.invokeRPC().deleteDoneDate(parantClass.txtUHID.getTag(), txtDescription.getTag(), vSno, new AsyncCallback<DmResult<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    hide(true);
                    callBack.refresh(txtDescription.getTag());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    @Override
    public void onClick(ClickEvent event) {
        super.onClick(event);
        if (event.getSource().equals(btnShuffleDate)) {
            if (DmShared.dNull(txtScheduleDate.getText()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Schedule Date is mandatory.", txtScheduleDate).show();
                return;
            }
            shuffleDateArea();
        }
    }

    private GrdSchedules_Mdl saveInitialize() {
        GrdSchedules_Mdl ourRow = new GrdSchedules_Mdl();

        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtDescription.getTag()));
        ourRow.setvSno(vSno);
        ourRow.setDescription(DmShared.xNull(txtDescription.getText()));
        ourRow.setScheduleDate(DmShared.dNull(txtScheduleDate.getText()));
        ourRow.setDoneDate(DmShared.dNull(parentRow.getDoneDate()));
        ourRow.setCycleTime(DmShared.xNull(txtCycleTime.getText()));
        ourRow.setPrintSno(DmShared.xNull(txtPrintSNo.getText()));
        ourRow.setRemark(DmShared.xNull(txtRemark.getText()));
        ourRow.setMachine(DmShared.xNull(txtHMachinePlaces.getText()));
        ourRow.setMachineCode(DmShared.iNull(txtHMachinePlaces.getTag()).toString());
        ourRow.setGuiTemplate(DmShared.xNull(txtHGuiTemplate.getText()));
        ourRow.setGuiTemplateCode(DmShared.iNull(txtHGuiTemplate.getTag2nd()).toString());
        ourRow.setGuiConfigCode(DmShared.iNull(txtHGuiTemplate.getTag()).toString());
        ourRow.setConsultantName(DmShared.xNull(txtHConsultant.getText()));
        ourRow.setConsultantCode(DmShared.iNull(txtHConsultant.getTag()).toString());

        return ourRow;
    }

    private void saveSchedule(GrdSchedules_Mdl grdMdl) {
        srvAsync.invokeRPC().saveSchedule(grdMdl, parantClass.txtUHID.getTag(), new AsyncCallback<DmResult<GrdSchedules_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtRemark).show();
            }

            @Override
            public void onSuccess(DmResult<GrdSchedules_Mdl> result) {
                if (result.isStatus()) {
                    proceedOk();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtRemark).show();
                }
            }
        });
    }

    private void shuffleDateArea() {
        ShuffleDays_Mdl mrTemp = new ShuffleDays_Mdl();
        mrTemp.setDocId(DmShared.xNull(txtDescription.getTag()));
        mrTemp.setSchduleDate(DmShared.dNull(txtScheduleDate.getTag()));
        mrTemp.setShuffleDays(CalendarUtil.getDaysBetween(DmShared.convertDate(txtScheduleDate.getTag()), DmShared.convertDate(txtScheduleDate.getText())));
        shuffleDays.show(mrTemp);
        shuffleDays.addCallBack(new ShuffleDays.CallBack() {
            @Override
            public void ok(String schDocId) {

                ManagePatientScheduleGridEdit.this.hide(true);
                new DmAlertMessageBox("Schedules are suffle successfully.", (DmTextBox) null).show();
                callBack.refresh(schDocId);
            }

            @Override
            public void close() {
                txtRemark.setFocus(true);
            }
        });
    }

    public ManagePatientScheduleGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Manage Schedule", "550px", "300px");
        srvAsync = new ServiceInvoker<ManagePatientSchedule_SrvAsync>(GWT.create(ManagePatientSchedule_Srv.class), ManagePatientSchedule.class.getName());
        txtScheduleDate.addBlurHandler(this);
        btnShuffleDate.addClickHandler(this);
        textHelpEvents();
    }

}
