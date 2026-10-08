package aarogya.master.organizationServiceMapping.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import aarogya.master.organizationServiceMapping.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.*;
import java.util.List;

public class RoomCategoryGridEdit<T1, T2> extends RoomCategoryGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(GrdRoomCategory_Mdl master_Mdl);

        public void onDelete();

    }

    ServiceInvoker<OrganizationServiceMapping_SrvAsync> SrvAsync;
    CallBack callBack;
    private int hashCode = 0;
    private OrganizationServiceMapping organizationServiceMapping;

    private void textHelpEvents() {
        txtHRoomCategory.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHRoomCategory(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHRoomCategory).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHRoomCategory.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHRoomCategory).show();
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

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, OrganizationServiceMapping organizationServiceMapping) {
        this.organizationServiceMapping = organizationServiceMapping;
        int cellIndex = 1;
        cellIndex = getDmGrid().getSelectedCell().getCell();
        super.showRelativeTo(currentRowIndex, cellIndex, okNeeded, newNeeded, deleteNeeded);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtHRoomCategory.setFocus(true);
            }
        });
    }

    @Override
    public void displayRow(T1 row) {
        GrdRoomCategory_Mdl ourRow = (GrdRoomCategory_Mdl) row;

        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtHRoomCategory.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHRoomCategory.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtRatePer.setText(DmClient.vNull(ourRow.getRatePercent(), "0.00"));

        txtHRoomCategory.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdRoomCategory_Mdl();
    }

    @Override
    public void onOk() {
        if (DmShared.xNull(txtHRoomCategory.getTag()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Checklist is mandatory.", txtHRoomCategory).show();
            return;
        }
        for (GrdRoomCategory_Mdl roomCategory_Mdl : (List<GrdRoomCategory_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
            if (DmShared.xNull(roomCategory_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(txtHRoomCategory.getTag()))) {
                if (!DmClient.vNull(roomCategory_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                    new DmAlertMessageBox(DmShared.xNull(txtHRoomCategory.getText()) + " is already exists.", txtHRoomCategory).show();
                    return;
                }
            }
        }
        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdRoomCategory_Mdl ourRow = (GrdRoomCategory_Mdl) row;

        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHRoomCategory.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHRoomCategory.getText()));
        ourRow.setRatePercent(DmClient.vNull(txtRatePer.getText(), "0.00"));

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

    public RoomCategoryGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Room Category Detail", "440px", "80px");
        SrvAsync = new ServiceInvoker<OrganizationServiceMapping_SrvAsync>(GWT.create(OrganizationServiceMapping_Srv.class), OrganizationServiceMapping.class.getName());
        textHelpEvents();
    }

}
