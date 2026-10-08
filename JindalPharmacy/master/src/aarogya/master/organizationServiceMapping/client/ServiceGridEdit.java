package aarogya.master.organizationServiceMapping.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import aarogya.master.organizationServiceMapping.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import java.util.List;

public class ServiceGridEdit<T1, T2> extends ServiceGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(GrdService_Mdl master_Mdl);

        public void onDelete();
    }

    ServiceInvoker<OrganizationServiceMapping_SrvAsync> SrvAsync;
    CallBack callBack;
    private int hashCode = 0;
    private OrganizationServiceMapping organizationServiceMapping;
    private GrdService_Mdl serviceMdl;

    private void textHelpEvents() {

        txtHMappedService.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHService(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMappedService).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHMappedService.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMappedService).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }

        });

    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, OrganizationServiceMapping organizationServiceMapping) {
        this.organizationServiceMapping = organizationServiceMapping;
        super.showCenter(currentRowIndex, okNeeded, newNeeded, deleteNeeded);

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtRate.setFocus(true);
            }
        });
    }

    @Override
    public void displayRow(T1 row) {
        GrdService_Mdl ourRow = (GrdService_Mdl) row;

        serviceMdl = ourRow;
        manageDisplay();

        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtOrgGroupName.setText(DmShared.xNull(ourRow.getOrgGroupName()));
        txtOrgServiceCode.setText(DmShared.xNull(ourRow.getOrgServiceCode()));
        txtOrgServiceName.setText(DmShared.xNull(ourRow.getOrgServiceName()));
        txtRate.setText(DmClient.vNull(ourRow.getRate(), "0.00"));
        txtHMappedService.setTag(DmShared.xNull(ourRow.getMappedServiceCode()));
        txtHMappedService.setText(DmShared.xNull(ourRow.getMappedServiceName()));

        txtRate.setFocus(true);
    }

    private void manageDisplay() {
        txtOrgGroupName.setEnabled(false);
        txtOrgServiceCode.setEnabled(false);
        txtOrgServiceName.setEnabled(false);
//        txtRate.setEnabled(false);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdService_Mdl();
    }

    @Override
    public void onOk() {
        if (DmShared.xNull(txtHMappedService.getTag()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Service Mapping With is mandatory.", txtHMappedService).show();
            return;
        }
        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdService_Mdl ourRow = (GrdService_Mdl) row;

        ourRow = serviceMdl;
        ourRow.setMappedServiceCode(DmShared.xNull(txtHMappedService.getTag()));
        ourRow.setMappedServiceName(DmShared.xNull(txtHMappedService.getText()));
        ourRow.setRate(DmClient.vNull(txtRate.getText(), "0.00"));

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

    public ServiceGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Services Detail", "450px", "125px");
        SrvAsync = new ServiceInvoker<OrganizationServiceMapping_SrvAsync>(GWT.create(OrganizationServiceMapping_Srv.class), OrganizationServiceMapping.class.getName());
        textHelpEvents();
    }

}
