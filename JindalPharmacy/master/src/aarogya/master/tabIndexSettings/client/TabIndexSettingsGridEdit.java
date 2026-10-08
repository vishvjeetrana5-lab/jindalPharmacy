package aarogya.master.tabIndexSettings.client;

import aarogya.master.tabIndexSettings.shared.*;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.List;

public class TabIndexSettingsGridEdit<T1, T2> extends TabIndexSettingsGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(GridTabIndexSettings_Mdl grdDdesc);

        public void onDelete();
    }

    private ServiceInvoker<TabIndexSettings_SrvAsync> SrvAsync;
    private CallBack callBack;
    private GridTabIndexSettings_Mdl parentRow;

    public TabIndexSettingsGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Tab Index Edit", "330px", "76px");
        SrvAsync = new ServiceInvoker<TabIndexSettings_SrvAsync>(GWT.create(TabIndexSettings_Srv.class), TabIndexSettings.class.getName());
        txtCaption.setEnabled(false);

    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
    }

    @Override
    public void displayRow(T1 row) {
        GridTabIndexSettings_Mdl ourRow = (GridTabIndexSettings_Mdl) row;
        parentRow = new GridTabIndexSettings_Mdl();

        txtCaption.setText(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtCaption.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtSerialNo.setText(DmShared.xNull(ourRow.getSerialNo()));

        txtSerialNo.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GridTabIndexSettings_Mdl();
    }

    @Override
    public void onOk() {
        if (DmShared.iNull(txtSerialNo.getText()).intValue() <= 0) {
            new DmAlertMessageBox("Serial No. is mandatory.", txtSerialNo).show();
            return;
        }
//        List<GridTabIndexSettings_Mdl> listTab = (List<GridTabIndexSettings_Mdl>) getDmGrid().getGrid().getStore().getAll();
//        int size = listTab.size();
//        if (DmShared.iNull(txtSerialNo.getText()).intValue() > DmShared.iNull(size).intValue()) {
//            new DmAlertMessageBox("Serial No. cannaot be greator than list size. ", txtSerialNo).show();
//            return;
//        }

        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GridTabIndexSettings_Mdl ourRow = (GridTabIndexSettings_Mdl) row;
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtCaption.getText()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtCaption.getText()));
        ourRow.setSerialNo(DmShared.xNull(txtSerialNo.getText()));
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

}
