package aarogya.master.additionalUserPermission.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import aarogya.master.additionalUserPermission.shared.AdditionalUserPermission_Srv;
import aarogya.master.additionalUserPermission.shared.AdditionalUserPermission_SrvAsync;
import aarogya.master.additionalUserPermission.shared.GrdService_Mdl;
import aarogya.master.additionalUserPermission.shared.TxtHService_Mdl;
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

public class ServiceGridEdit<T1, T2> extends ServiceGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(GrdService_Mdl master_Mdl);

        public void onDelete();

    }

    ServiceInvoker<AdditionalUserPermission_SrvAsync> SrvAsync;
    CallBack callBack;
    private int hashCode = 0;

    private void textHelpEvents() {

        txtHService.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHService_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHService(DmShared.xNull(txtHServiceCat.getTag()), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHService_Mdl>>>() {

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
            public void getSelectedRow(boolean blnStatus, TxtHService_Mdl tRow) {
                if (blnStatus && tRow != null) {
                    txtHServiceCat.setTag(DmShared.xNull(tRow.getServiceCatCode()));
                    txtHServiceCat.setText(DmShared.xNull(tRow.getServiceCatName()));
                }
            }
        });

        txtHServiceCat.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHServiceCategory(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHServiceCat).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHServiceCat.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHServiceCat).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
                if (blnStatus && tRow != null) {
                    txtHService.setText("");
                }
            }
        });

    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {
        int cellIndex = 1;
        cellIndex = getDmGrid().getSelectedCell().getCell();
        super.showRelativeTo(currentRowIndex, cellIndex, okNeeded, newNeeded, deleteNeeded);

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtHServiceCat.setFocus(true);
            }
        });
    }

    @Override
    public void displayRow(T1 row) {
        GrdService_Mdl ourRow = (GrdService_Mdl) row;

        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtHServiceCat.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHServiceCat.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtHService.setTag(DmShared.xNull(ourRow.getServiceCode()));
        txtHService.setText(DmShared.xNull(ourRow.getServiceName()));

        txtHServiceCat.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdService_Mdl();
    }

    @Override
    public void onOk() {
        if (DmShared.xNull(txtHServiceCat.getTag()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Service Category is mandatory.", txtHServiceCat).show();
            return;
        }
        for (GrdService_Mdl service_Mdl : (List<GrdService_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
            if (DmShared.iNull(service_Mdl.getSharedKeyFieldCode()).longValue() != 0) {
                if (DmShared.iNull(service_Mdl.getServiceCode()).longValue() != 0) {
                    if (DmShared.xNull(service_Mdl.getServiceCode()).equalsIgnoreCase(DmShared.xNull(txtHService.getTag()))) {
                        if (!DmClient.vNull(service_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                            new DmAlertMessageBox("This service category and service already exists.", txtHService).show();
                            return;
                        }
                    }
                }
            }
            if (DmShared.iNull(service_Mdl.getSharedKeyFieldCode()).longValue() != 0 && DmShared.iNull(txtHService.getTag()).longValue() == 0) {
                if (DmShared.xNull(service_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(txtHServiceCat.getTag()))) {
                    if (!DmClient.vNull(service_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                        new DmAlertMessageBox("Please remove either above services or fill service", txtHService).show();
                        return;
                    }
                }
            }
            if (DmShared.iNull(service_Mdl.getSharedKeyFieldCode()).longValue() != 0 && DmShared.iNull(service_Mdl.getServiceCode()).longValue() == 0) {
                if (DmShared.xNull(service_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(txtHServiceCat.getTag()))) {
                    if (!DmClient.vNull(service_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                        new DmAlertMessageBox("Please fill the service of above category", txtHService).show();
                        return;
                    }
                }
            }
        }
        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdService_Mdl ourRow = (GrdService_Mdl) row;
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHServiceCat.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHServiceCat.getText()));
        ourRow.setServiceCode(DmShared.xNull(txtHService.getTag()));
        ourRow.setServiceName(DmShared.xNull(txtHService.getText()));
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
        super(grid, "Service Category Detail", "440px", "80px");
        SrvAsync = new ServiceInvoker<AdditionalUserPermission_SrvAsync>(GWT.create(AdditionalUserPermission_Srv.class), AdditionalUserPermission.class.getName());
        textHelpEvents();
    }

}
