package aarogya.master.consultantMaster.client;

import aarogya.master.consultantMaster.shared.*;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class RoomCategoryGridEdit<T1, T2> extends RoomCategoryGridEdit_Dsg<T1, T2> {

    //<editor-fold defaultstate="collapsed" desc="variable dicalrations">
    interface CallBack {

        public void onOk(GrdRoomCategory_Mdl category);

    }

    private final ServiceInvoker<ConsultantMaster_SrvAsync> SrvAsync;
    private CallBack callBack;
    private GrdRoomCategory_Mdl parentRow;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="public methods">
    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {
        super.showRelativeTo(currentRowIndex, 1, okNeeded, false, false);
        txtAmount.setFocus(true);
        txtRoom.setEnabled(false);
    }

    @Override
    public void displayRow(T1 row) {
        GrdRoomCategory_Mdl ourRow = (GrdRoomCategory_Mdl) row;
        parentRow = new GrdRoomCategory_Mdl();
        txtRoom.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtRoom.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtAmount.setText(DmClient.vNull(ourRow.getAmount(), "0.00"));
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdRoomCategory_Mdl();
    }

    @Override
    public void onOk() {
        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdRoomCategory_Mdl ourRow = (GrdRoomCategory_Mdl) row;
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtRoom.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtRoom.getText()));
        ourRow.setAmount(DmClient.vNull(txtAmount.getText(), "0.00"));
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
    }
    //</editor-fold>

    public RoomCategoryGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Room Category", "350px", "80px");
        SrvAsync = new ServiceInvoker<ConsultantMaster_SrvAsync>(GWT.create(ConsultantMaster_Srv.class), ConsultantMaster.class.getName());
    }
}
