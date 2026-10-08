package aarogya.master.environmentService.client;

import aarogya.master.environmentService.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import java.util.List;

public class BloodComponentTabGridEdit<T1, T2> extends BloodComponentTabGridEdit_Dsg<T1, T2> implements BlurHandler {

    ServiceInvoker<EnvironmentService_SrvAsync> SrvAsync;
    private CallBack callBack;
    private GrdBloodComponent_Mdl parentRow;
    private String siteCode;

    @Override
    public void onBlur(BlurEvent event) {

    }

    interface CallBack {

        public void onOk(GrdBloodComponent_Mdl grdTdsEntry);

        public void onDelete();
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, String siteCode) {
        this.siteCode = siteCode;
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtHLinkedSeriveAC.setFocus(true);

            }
        });
    }

    private void textHelpEvents() {
        txtHLinkedSeriveAC.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getServices(helper, blnToEndOfFile, null, "", new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHLinkedSeriveAC).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHLinkedSeriveAC.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHLinkedSeriveAC).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
    }

    @Override
    public void onOk() {

        proceedOk();
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
    public void manageOk(T1 row) {
        GrdBloodComponent_Mdl ourRow = (GrdBloodComponent_Mdl) row;
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtBloodComponent.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtBloodComponent.getText()));
        ourRow.setLinkedServiceTag(DmShared.xNull(txtHLinkedSeriveAC.getTag()));
        ourRow.setLinkedServiceText(DmShared.xNull(txtHLinkedSeriveAC.getText()));
        callBack.onOk(ourRow);
    }

    @Override
    public void manageDelete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdServiceCategory_Mdl();
    }

    @Override
    public void displayRow(T1 row) {
        GrdBloodComponent_Mdl ourRow = (GrdBloodComponent_Mdl) row;
        parentRow = new GrdBloodComponent_Mdl();
        txtBloodComponent.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtBloodComponent.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));

        txtHLinkedSeriveAC.setTag(DmShared.xNull(ourRow.getLinkedServiceTag()));
        txtHLinkedSeriveAC.setText(DmShared.xNull(ourRow.getLinkedServiceText()));

    }

    public BloodComponentTabGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Blood Component", "470px", "100px");
        SrvAsync = new ServiceInvoker<>(GWT.create(EnvironmentService_Srv.class), EnvironmentService.class.getName());
        textHelpEvents();
        manageDisplay();
    }

    private void manageDisplay() {
        txtBloodComponent.setEnabled(false);
    }
}
