package aarogya.master.environmentService.client;

import aarogya.master.environmentService.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import java.util.List;

public class ServicePopupGridEdit<T1, T2> extends ServicePopupGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(GrdEnviroSerive_Mdl propertiesMdl);

        public void onDelete();
    }

    private final ServiceInvoker<EnvironmentService_SrvAsync> srvAsync;
    private CallBack callBack;
    private int hashCode = 0;
    private String serviceCatCode, siteCode;
    private GrdEnviroSerive_Mdl parentRow;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void onOk() {
        if (DmShared.xNull(txtHService.getTag()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Service is mandatory.", txtHService).show();
            return;
        } else if (DmShared.xNull(txtHDefConsultant.getTag()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Consultant is mandatory.", txtHDefConsultant).show();
            return;
        }
        for (GrdEnviroSerive_Mdl service : (List<GrdEnviroSerive_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
            if (DmShared.xNull(service.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(txtHService.getTag()))) {
                if (service.getSharedKeyFieldHashCode() != hashCode) {
                    new DmAlertMessageBox("Service already exists.", txtHService).show();
                    return;
                }
            }
        }

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
        GrdEnviroSerive_Mdl ourRow = (GrdEnviroSerive_Mdl) row;
        ourRow.setSharedKeyFieldCode(txtHService.getTag());
        ourRow.setSharedKeyFieldName(txtHService.getText());
        ourRow.setDefConsultantTag(txtHDefConsultant.getTag());
        ourRow.setDefConsultantText(txtHDefConsultant.getText());
        callBack.onOk(ourRow);
    }

    @Override
    public void manageDelete() {
        callBack.onDelete();
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdEnviroSerive_Mdl();
    }

    @Override
    public void displayRow(T1 row) {
        GrdEnviroSerive_Mdl ourRow = (GrdEnviroSerive_Mdl) row;
        parentRow = new GrdEnviroSerive_Mdl();
        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtHService.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHService.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtHDefConsultant.setTag(DmShared.xNull(ourRow.getDefConsultantTag()));
        txtHDefConsultant.setText(DmShared.xNull(ourRow.getDefConsultantText()));
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, String servCatCode, String siteCode) {
        this.serviceCatCode = servCatCode;
        this.siteCode = siteCode;
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtHService.setFocus(true);
            }

        });
    }

    public ServicePopupGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Service Edit", "380px", "85px");
        srvAsync = new ServiceInvoker<>(GWT.create(EnvironmentService_Srv.class), EnvironmentService.class.getName());
        textHelpEvents();

    }

    private void textHelpEvents() {

        txtHService.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getServices(helper, blnToEndOfFile, null, serviceCatCode, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHService).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHService.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHService).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {

            }
        });

        txtHDefConsultant.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHConsultant(siteCode, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDefConsultant).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDefConsultant.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDefConsultant).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

    }
}
