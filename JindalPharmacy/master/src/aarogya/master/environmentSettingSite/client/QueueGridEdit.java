package aarogya.master.environmentSettingSite.client;

import aarogya.master.environmentSettingSite.shared.EnvironmentSettingSite_Srv;
import aarogya.master.environmentSettingSite.shared.EnvironmentSettingSite_SrvAsync;
import aarogya.master.environmentSettingSite.shared.GrdQueueSequence_Mdl;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class QueueGridEdit<T1, T2> extends QueueGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(GrdQueueSequence_Mdl operation);

        public void onDelete();
    }

    private final ServiceInvoker<EnvironmentSettingSite_SrvAsync> srvAsync;
    private CallBack callBack;
    private GrdQueueSequence_Mdl parentRow;
    private int hashCode = 0;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
        txtSerial.setFocus(true);
        txtQueue.setEnabled(false);
    }

    @Override
    public void displayRow(T1 row) {
        GrdQueueSequence_Mdl ourRow = (GrdQueueSequence_Mdl) row;
        hashCode = ourRow.getSharedKeyFieldHashCode();
        parentRow = new GrdQueueSequence_Mdl();
        txtQueue.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtQueue.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtPatient.setText(DmClient.vNull(ourRow.getTotalPatient(), "0"));
        txtSerial.setText(DmClient.vNull(ourRow.getPrioritySerial(), "0"));
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdQueueSequence_Mdl();
    }

    @Override
    public void onOk() {
        if (DmShared.xNull(txtQueue.getTag()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Queue Type is mandatory.", txtQueue).show();
            return;
        }
        if (DmShared.vNull(txtSerial.getText()).doubleValue() <= 0) {
            new DmAlertMessageBox("S. No. is mandatory.", txtSerial).show();
            return;
        }
        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdQueueSequence_Mdl ourRow = (GrdQueueSequence_Mdl) row;
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtQueue.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtQueue.getText()));
        ourRow.setPrioritySerial(DmClient.vNull(txtSerial.getText(), "0"));
        ourRow.setTotalPatient(DmClient.vNull(txtPatient.getText(), "0"));
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

    public QueueGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Queue Type Detail", "330px", "80px");
        srvAsync = new ServiceInvoker<EnvironmentSettingSite_SrvAsync>(GWT.create(EnvironmentSettingSite_Srv.class), EnvironmentSettingSite.class.getName());
    }
}
