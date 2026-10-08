package dataman.payroll.employeeMaster.client;

import dataman.payroll.employeeMaster.shared.EmployeeMaster_Mdl;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.payroll.employeeMaster.shared.EmployeeMaster_Srv;
import dataman.payroll.employeeMaster.shared.EmployeeMaster_SrvAsync;
import dataman.payroll.employeeMaster.shared.GrdLeave_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public class LeaveGridEdit<T1, T2> extends LeaveGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(GrdLeave_Mdl master_Mdl);

        public void onDelete();

    }

    ServiceInvoker<EmployeeMaster_SrvAsync> SrvAsync;
    CallBack callBack;
    private int hashCode = 0;
    private EmployeeMaster empParent;
    private String duration;

    private void textHelpEvents() {

    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, EmployeeMaster empMast) {
        this.empParent = empMast;
        int cellIndex = 1;
        cellIndex = getDmGrid().getSelectedCell().getCell();
        super.showRelativeTo(currentRowIndex, cellIndex, okNeeded, false, false);
    }

    private void getDuration(String fromDate, String toDate) {

        SrvAsync.invokeRPC().getDuration(fromDate, toDate, new AsyncCallback<DmResult<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    duration = result.getData();
                    if (!DmShared.xNull(duration).equalsIgnoreCase("")) {
                        if (DmShared.iNull(duration).intValue() < DmShared.iNull(txtLeaves.getText()).intValue()) {
                            new DmAlertMessageBox("Leave exceeds the available limit.", txtLeaves).show();
                            return;
                        }
                    }
                    proceedOk();
                }
            }
        });

    }

    @Override
    public void displayRow(T1 row) {
        GrdLeave_Mdl ourRow = (GrdLeave_Mdl) row;
        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtLeaves.setText(DmShared.xNull(ourRow.getLeaves()));
        txtLeaves.setTag(DmShared.xNull(ourRow.getEmployeeLeaveDocId()));
        txtLeave.setText(DmShared.xNull(ourRow.getLeave()));
        txtLeave.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtLeave.setTag2nd(DmShared.xNull(ourRow.getLeaveDocId()));
        txtFromDate.setText(DmShared.dNull(ourRow.getFromDate()));
        txtToDate.setText(DmShared.dNull(ourRow.getToDate()));

        txtFromDate.setEnabled(false);
        txtToDate.setEnabled(false);
        txtLeave.setEnabled(false);
        txtLeaves.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdLeave_Mdl();
    }

    @Override
    public void onOk() {
        getDuration(txtFromDate.getText(), txtToDate.getText());
    }

    @Override
    public void manageOk(T1 row) {
        GrdLeave_Mdl ourRow = (GrdLeave_Mdl) row;

        ourRow.setFromDate(DmShared.dNull(txtFromDate.getText()));
        ourRow.setToDate(DmShared.dNull(txtToDate.getText()));
        ourRow.setLeave(DmShared.xNull(txtLeave.getText()));
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtLeave.getTag()));
        ourRow.setLeaves(DmShared.xNull(txtLeaves.getText()));
        ourRow.setLeaveDocId(DmShared.xNull(txtLeave.getTag2nd()));
        ourRow.setEmployeeLeaveDocId(DmShared.xNull(txtLeaves.getTag()));
        updateRecord(ourRow);
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

    public LeaveGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Leave Detail", "400px", "125px");
        SrvAsync = new ServiceInvoker<EmployeeMaster_SrvAsync>(GWT.create(EmployeeMaster_Srv.class), EmployeeMaster.class.getName());
        textHelpEvents();
    }

    private GrdLeave_Mdl saveInitialize(GrdLeave_Mdl leave_Mdl) {

        GrdLeave_Mdl mrTemp = new GrdLeave_Mdl();

        mrTemp.setLeaveCode(DmShared.xNull(txtLeave.getTag()));
        mrTemp.setLeaves(DmShared.xNull(txtLeaves.getText()));
        mrTemp.setsNo(DmShared.xNull(leave_Mdl.getsNo()));
        mrTemp.setLeaveDocId(DmShared.xNull(txtLeave.getTag2nd()));
        mrTemp.setEmployeeLeaveDocId(DmShared.xNull(txtLeaves.getTag()));
        return mrTemp;
    }

    private void updateRecord(GrdLeave_Mdl leave_Mdl) {
        GrdLeave_Mdl mrTemp;
        try {
            mrTemp = saveInitialize(leave_Mdl);
            SrvAsync.invokeRPC().updateRecord(mrTemp, DmShared.xNull(empParent.topControl.getDocId()), new AsyncCallback<DmResult<GrdLeave_Mdl>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<GrdLeave_Mdl> result) {
                    if (result.isStatus()) {
                        onRefresh();
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                    }
                }
            });
        } catch (DmException e) {
            new DmAlertMessageBox(e.getErrorSource(), (DmTextBox) null).show();
        }
    }

    private void onRefresh() {

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
//                txtSearchValue.setFocus(true);
            }
        });
    }

}
