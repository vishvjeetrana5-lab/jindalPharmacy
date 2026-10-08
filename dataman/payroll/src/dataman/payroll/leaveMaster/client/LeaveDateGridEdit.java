package dataman.payroll.leaveMaster.client;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.payroll.leaveMaster.shared.*;
import java.util.List;

public class LeaveDateGridEdit<T1, T2> extends LeaveDateGridEdit_Dsg<T1, T2> implements BlurHandler {

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtFromDate)) {
            if (!DmShared.xNull(txtFromDate.getText()).equalsIgnoreCase("")) {
                txtFromDate.setText(DmClient.returnDate(txtFromDate.getText()));
            }
        }
        if (event.getSource().equals(txtToDate)) {
            if (!DmShared.xNull(txtToDate.getText()).equalsIgnoreCase("")) {
                txtToDate.setText(DmClient.returnDate(txtToDate.getText()));
            }
        }
    }

    interface CallBack {

        public void onOk(GrdDate_Mdl opdService);

        public void onDelete();
    }

    private CallBack callBack;
    private int hashCode = 0;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, boolean isDeleteFocus, String currentDate) {
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            @Override
            public void execute() {
                txtFromDate.setFocus(true);
            }
        });
    }

    @Override
    public void displayRow(T1 row) {
        GrdDate_Mdl ourRow = (GrdDate_Mdl) row;

        txtFromDate.setText(DmShared.xNull(ourRow.getFromDate()));
        txtToDate.setText(DmShared.xNull(ourRow.getToDate()));

        hashCode = ourRow.getSharedKeyFieldHashCode();

        txtFromDate.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdDate_Mdl();
    }

    @Override
    public void onOk() {
        try {
            if (DmShared.xNull(txtFromDate.getText()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("From Date is mandatory.", txtFromDate).show();
                return;
            }
            if (DmShared.xNull(txtToDate.getText()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("To Date is mandatory.", txtToDate).show();
                return;
            }
            if (DmShared.convertDate(txtFromDate.getText()).getTime() < DmShared.convertDate(DmClient.getLoginDate()).getTime()) {
                new DmAlertMessageBox("From Date should be greater than  Current Date.", txtFromDate).show();
                return;
            }

            if (DmShared.convertDate(txtToDate.getText()).getTime() < DmShared.convertDate(DmClient.getLoginDate()).getTime()) {
                new DmAlertMessageBox("To Date should be greater than  Current Date.", txtToDate).show();
                return;
            }
            if (DmShared.convertDate(txtToDate.getText()).getTime() < DmShared.convertDate(txtFromDate.getText()).getTime()) {
                new DmAlertMessageBox("To Date should be greater or equal than  From Date.", txtToDate).show();
                return;
            }
            for (GrdDate_Mdl date : (List<GrdDate_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
                if (!DmShared.xNull(date.getFromDate()).equalsIgnoreCase("")) {
                    if (((DmShared.convertDate(txtFromDate.getText()).getTime()) >= DmShared.convertDate(date.getFromDate()).getTime()
                            && (DmShared.convertDate(txtFromDate.getText()).getTime()) <= DmShared.convertDate(date.getToDate()).getTime())
                            || (DmShared.convertDate(txtToDate.getText()).getTime()) >= DmShared.convertDate(date.getFromDate()).getTime()
                            && (DmShared.convertDate(txtToDate.getText()).getTime()) <= DmShared.convertDate(date.getToDate()).getTime()
                            || (DmShared.convertDate(txtFromDate.getText()).getTime()<=DmShared.convertDate(date.getFromDate()).getTime())
                            && DmShared.convertDate(txtToDate.getText()).getTime() >=(DmShared.convertDate(date.getToDate()).getTime())) {
                        if (!DmClient.vNull(date.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                            new DmAlertMessageBox("Date period already exists.", txtFromDate).show();
                            return;
                        }
                    }
                }
            }

//            for (GrdDate_Mdl date : (List<GrdDate_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
//                if (!DmShared.xNull(date.getFromDate()).equalsIgnoreCase("")) {
//                    if (DmShared.convertDate(date.getFromDate()).getTime() == (DmShared.convertDate(txtFromDate.getText()).getTime())) {
//                        if (DmShared.convertDate(date.getToDate()).getTime() == (DmShared.convertDate(txtToDate.getText()).getTime())) {
//                            if (!DmClient.vNull(date.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
//                                new DmAlertMessageBox("Date period already exists.", txtFromDate).show();
//                                return;
//                            }
//                        }
//
//                    }
//                }
//            }
            proceedOk();
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtToDate).show();
        }
    }

    @Override
    public void manageOk(T1 row) {
        GrdDate_Mdl ourRow = (GrdDate_Mdl) row;

        ourRow.setFromDate(DmShared.xNull(txtFromDate.getText()));
        ourRow.setToDate(DmShared.xNull(txtToDate.getText()));

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

    public LeaveDateGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Leave Date", "230px", "90px");
        handleEvents();
    }

    private void handleEvents() {
        txtToDate.addBlurHandler(this);
        txtFromDate.addBlurHandler(this);
    }
}
