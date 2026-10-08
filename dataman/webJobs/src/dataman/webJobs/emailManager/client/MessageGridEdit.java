package dataman.webJobs.emailManager.client;

import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmBase.self.shared.DmDynamicGridRow;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class MessageGridEdit<T1, T2> extends MessageGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(DmDynamicGridRow ddgr, int rowIndex);
    }
    private CallBack callback;
    private int colIndex;
    private int rowIndex;

    @Override
    public void onOk() {
        String errorMsg;
        if (colIndex == 6) {
            errorMsg = "Create subject.";
        } else {
            errorMsg = "Create message";
        }
        if (DmShared.xNull(txtMsgArea.getText()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox(errorMsg, txtMsgArea).show();
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
        if (colIndex == 6) {
            ourRow.getCells().put("subject", DmShared.xNull(txtMsgArea.getText()).trim());
        } else {
            ourRow.getCells().put("message", DmShared.xNull(txtMsgArea.getText()).trim());
        }
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
        if (colIndex == 6) {
            txtMsgArea.setText(DmShared.xNull(ourRow.getCells().get("subject").toString()));
        } else if (colIndex == 7) {
            txtMsgArea.setText(DmShared.xNull(ourRow.getCells().get("message").toString()));
        }
        txtMsgArea.setFocus(true);
    }

    public void addCallBack(CallBack callback) {
        this.callback = callback;
    }

    public void show(int currentRowIndex, boolean okNeeded, int colIndex) {
        this.colIndex = colIndex;
        this.rowIndex = currentRowIndex;
        super.showRelativeTo(currentRowIndex, 1, okNeeded, false, false);
        txtMsgArea.setFocus(true);
    }

    public MessageGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Message Edit", "400px", "120px");
    }

}
