package dataman.webJobs.smsManager.client;

import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmBase.self.shared.DmDynamicGridRow;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class SmsManagerGridEdit<T1, T2> extends SmsManagerGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(DmDynamicGridRow ddgr, int rowIndex);
    }
    private CallBack callback;
    private int rowIndex;

    @Override
    public void onOk() {
        if (DmShared.xNull(txtMsgArea.getText()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Create message.", txtMsgArea).show();
            return;
        }
        proceedOk();
    }

    @Override
    public void onNew() {
    }

    @Override
    public void onDelete() {
    }

    @Override
    public void onClose() {
        this.hide();
    }

    @Override
    public void manageOk(T1 row) {
        DmDynamicGridRow ourRow = (DmDynamicGridRow) row;
        ourRow.getCells().put("message", DmShared.xNull(txtMsgArea.getText()).trim());
        callback.onOk(ourRow, rowIndex);
    }

    @Override
    public void manageDelete() {
    }

    @Override
    public T1 getNewRow() {
        return (T1) new DmDynamicGridRow();
    }

    @Override
    public void displayRow(T1 row) {
        DmDynamicGridRow ourRow = (DmDynamicGridRow) row;
        txtMsgArea.setText(DmShared.xNull(ourRow.getCells().get("message").toString()));
        txtMsgArea.setFocus(true);
    }

    public void addCallBack(CallBack callback) {
        this.callback = callback;
    }

    public void show(int currentRowIndex, boolean okNeeded) {
        this.rowIndex = currentRowIndex;
        super.showRelativeTo(currentRowIndex, 1, okNeeded, false, false);
        txtMsgArea.setFocus(true);
    }

    public SmsManagerGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Message Edit", "980px", "400px");
    }

}
