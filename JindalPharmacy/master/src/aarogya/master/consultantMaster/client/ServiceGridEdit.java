package aarogya.master.consultantMaster.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import aarogya.master.consultantMaster.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.ArrayList;
import java.util.List;

public class ServiceGridEdit<T1, T2> extends ServiceGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(GrdService_Mdl operation);

        public void onDelete();
    }

    private final ServiceInvoker<ConsultantMaster_SrvAsync> SrvAsync;
    private CallBack callBack;
    private GrdService_Mdl parentRow;
    protected final RoomCategoryGridEdit<GrdRoomCategory_Mdl, GrdRoomCategory_PrpAcs> gridEdit = new RoomCategoryGridEdit(grdRoomCategory);
    private ConsultantMaster clsMaster;
    private boolean flag;
    private int hashCode = 0;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(ConsultantMaster clsConsultantMaster, int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {
        this.clsMaster = clsConsultantMaster;
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);

        txtHService.setFocus(true);
    }

    @Override
    public void displayRow(T1 row) {
        List<GrdRoomCategory_Mdl> lstCategory = new ArrayList<GrdRoomCategory_Mdl>();
        flag = false;
        GrdService_Mdl ourRow = (GrdService_Mdl) row;
        parentRow = new GrdService_Mdl();
        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtHService.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHService.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtAmount.setText(DmClient.vNull(ourRow.getAmount(), "0.00"));

        grdRoomCategory.getGrid().getStore().clear();
        for (int i = 0; i < clsMaster.roomCategory.size(); i++) {
            GrdRoomCategory_Mdl nodeCategory = new GrdRoomCategory_Mdl();
            GrdRoomCategory_Mdl category = clsMaster.roomCategory.get(i);
            flag = false;
            for (int j = 0; j < ourRow.getLstRoomCategory().size(); j++) {
                GrdRoomCategory_Mdl roomCategory = ourRow.getLstRoomCategory().get(j);
                if (DmShared.xNull(category.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(roomCategory.getSharedKeyFieldCode()))) {
                    nodeCategory = roomCategory.clone();
                    lstCategory.add(nodeCategory);
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                nodeCategory = category.clone();
                if (DmShared.xNull(txtHService.getTag()).equalsIgnoreCase("")) {
                    nodeCategory.setAmount("0.00");
                }
                lstCategory.add(nodeCategory);
            }

        }
        grdRoomCategory.getGrid().getStore().addAll(lstCategory);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdService_Mdl();
    }

    @Override
    public void onOk() {
        if (DmShared.xNull(txtHService.getTag()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Operation is mandatory.", txtHService).show();
            return;
        }
        flag = false;
        for (int i = 0; i < grdRoomCategory.getGrid().getStore().size(); i++) {
            GrdRoomCategory_Mdl get = grdRoomCategory.getGrid().getStore().get(i);
            if (DmShared.vNull(get.getAmount()).doubleValue() > 0) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            if (DmShared.vNull(txtAmount.getText()).doubleValue() <= 0) {
                new DmAlertMessageBox("Fill any one 'Room Category Rate or Service Rate'", txtAmount).show();
                return;
            }
        }

        for (GrdService_Mdl service : (List<GrdService_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
            if (DmShared.xNull(service.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(txtHService.getTag()))) {
                if (!DmClient.vNull(service.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                    new DmAlertMessageBox("Operation already exists.", txtHService).show();
                    return;
                }
            }
        }
        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        flag = false;
        GrdService_Mdl ourRow = (GrdService_Mdl) row;
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHService.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHService.getText()));
        ourRow.getLstRoomCategory().addAll(grdRoomCategory.getGrid().getStore().getAll());
        ourRow.setAmount(DmClient.vNull(txtAmount.getText(), "0.00"));
        ourRow.getLstRoomCategory().clear();
        for (GrdRoomCategory_Mdl category : grdRoomCategory.getGrid().getStore().getAll()) {
            if (!DmShared.xNull(category.getSharedKeyFieldName()).equalsIgnoreCase("")) {
                if (DmShared.vNull(category.getAmount()).doubleValue() > 0) {
                    ourRow.getLstRoomCategory().add(category.clone());
                }
            }
        }
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

    private void gridEvents() {
        gridEdit.addCallBack(new RoomCategoryGridEdit.CallBack() {

            @Override
            public void onOk(GrdRoomCategory_Mdl category) {
                try {
                    grdRoomCategory.commitChanges();

                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), txtHService).show();
                }
            }

        });
        grdRoomCategory.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                try {
                    showGrid(event.getRowIndex());
                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
                }
            }
        });
        grdRoomCategory.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                try {
                    if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                        showGrid(grdRoomCategory.getGrid().getStore().indexOf(grdRoomCategory.getGrid().getSelectionModel().getSelectedItem()));
                    }
                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
                }
            }
        }, KeyDownEvent.getType());
    }

    private void txtHelpEvents() {
        txtHService.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHService_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHService(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHService_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHService).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHService_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHService.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHService).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHService_Mdl t1Row) {
            }
        });
    }

    private void showGrid(int currentRow) {
        if (clsMaster.topControl.getMode().equalsIgnoreCase("Browse")) {
            gridEdit.show(currentRow, false, false, false);
        } else {
            gridEdit.show(currentRow, true, true, true);
        }
    }

    public ServiceGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Operation Detail", "450px", "250px");
        SrvAsync = new ServiceInvoker<ConsultantMaster_SrvAsync>(GWT.create(ConsultantMaster_Srv.class), ConsultantMaster.class.getName());
        gridEvents();
        txtHelpEvents();
    }
}
