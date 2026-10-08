package aarogya.pharmacy.environmentSettingSite.client;

import aarogya.pharmacy.environmentSettingSite.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public class DepartmentSiteLinkGridEdit<T1, T2> extends DepartmentSiteLinkGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(GrdLinkDepartment_Mdl departmentDetail_Mdl);

        public void onDelete();
    }

    private final ServiceInvoker<EnvironmentSettingSite_SrvAsync> SrvAsync;
    private CallBack callBack;
    private GrdLinkDepartment_Mdl parentRow;
    private int hashCode;

    private void textHelpEvents() {
        txtHDepartment.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHDepartment(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDepartment).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDepartment.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDepartment).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHSite.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSite(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSite).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSite.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSite).show();
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

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtHDepartment.setFocus(true);
            }
        });
    }

    @Override
    public void displayRow(T1 row) {
        GrdLinkDepartment_Mdl ourRow = (GrdLinkDepartment_Mdl) row;
        parentRow = new GrdLinkDepartment_Mdl();

        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtHDepartment.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHDepartment.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtHSite.setText(DmShared.xNull(ourRow.getLinkSiteName()));
        txtHSite.setTag(DmShared.xNull(ourRow.getLinkSiteCode()));
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdLinkDepartment_Mdl();
    }

    @Override
    public void onOk() {
        if (DmShared.xNull(txtHDepartment.getTag()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Department is mandatory.", txtHDepartment).show();
            return;
        }
        if (DmShared.xNull(txtHSite.getTag()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Link Site is mandatory.", txtHSite).show();
            return;
        }

        List<GrdLinkDepartment_Mdl> lstTemp = (List<GrdLinkDepartment_Mdl>) getDmGrid().getGrid().getStore().getAll();

        for (int i = 0; i < lstTemp.size(); i++) {
            KeyField get = lstTemp.get(i);
            if (DmShared.xNull(txtHDepartment.getTag()).equalsIgnoreCase(DmShared.xNull(get.getSharedKeyFieldCode()))) {
                if (DmShared.vNull(hashCode).doubleValue() != DmShared.vNull(get.getSharedKeyFieldHashCode()).doubleValue()) {
                    new DmAlertMessageBox(txtHDepartment.getText() + " already exists.", txtHDepartment).show();
                    return;
                }
            }
        }

        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdLinkDepartment_Mdl ourRow = (GrdLinkDepartment_Mdl) row;
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHDepartment.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHDepartment.getText()));
        ourRow.setLinkSiteCode(DmShared.xNull(txtHSite.getTag()));
        ourRow.setLinkSiteName(DmShared.xNull(txtHSite.getText()));
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

    public DepartmentSiteLinkGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Department Site Link", "390px", "75px");
        SrvAsync = new ServiceInvoker<>(GWT.create(EnvironmentSettingSite_Srv.class), EnvironmentSettingSite.class.getName());
        textHelpEvents();
    }
}
