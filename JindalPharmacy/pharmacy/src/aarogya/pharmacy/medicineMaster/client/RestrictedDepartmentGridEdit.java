package aarogya.pharmacy.medicineMaster.client;

import aarogya.pharmacy.medicineMaster.shared.MedicineMaster_Srv;
import aarogya.pharmacy.medicineMaster.shared.MedicineMaster_SrvAsync;
import aarogya.pharmacy.medicineMaster.shared.GrdRestrictedDept_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public class RestrictedDepartmentGridEdit<T1, T2> extends RestrictedDepartmentGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(GrdRestrictedDept_Mdl gridVendor_Mdl);

        public void onDelete();
    }

    private ServiceInvoker<MedicineMaster_SrvAsync> srvAsync;
    private CallBack callBack;
    private int hashCode = 0;

    private void textHelpEvents() {

        txtHRestrictedDepartment.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHRestrictedDepartment(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHRestrictedDepartment).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHRestrictedDepartment.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHRestrictedDepartment).show();
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
    }

    @Override
    public void displayRow(T1 row) {
        GrdRestrictedDept_Mdl ourRow = (GrdRestrictedDept_Mdl) row;

        txtHRestrictedDepartment.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHRestrictedDepartment.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtHRestrictedDepartment.setTag2nd(DmShared.xNull(ourRow.getV_SNo()));
        hashCode = ourRow.getSharedKeyFieldHashCode();

        txtHRestrictedDepartment.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdRestrictedDept_Mdl();
    }

    @Override
    public void onOk() {
        try {
            if (DmShared.xNull(txtHRestrictedDepartment.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Restricted Department is mandatory.", txtHRestrictedDepartment).show();
                return;
            }

            for (GrdRestrictedDept_Mdl detail_Mdl : (List<GrdRestrictedDept_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
                if (DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(txtHRestrictedDepartment.getTag()))) {
                    if (!DmClient.vNull(detail_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                        new DmAlertMessageBox(DmShared.xNull(txtHRestrictedDepartment.getText()) + " already exists.", txtHRestrictedDepartment).show();
                        return;
                    }
                }
            }

            proceedOk();
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtHRestrictedDepartment).show();
        }
    }

    @Override
    public void manageOk(T1 row) {
        GrdRestrictedDept_Mdl ourRow = (GrdRestrictedDept_Mdl) row;

        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHRestrictedDepartment.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHRestrictedDepartment.getText()));
        ourRow.setV_SNo(DmShared.xNull(txtHRestrictedDepartment.getTag2nd()));
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

    public RestrictedDepartmentGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Restricted Department", "390px", "55px");
        srvAsync = new ServiceInvoker<MedicineMaster_SrvAsync>(GWT.create(MedicineMaster_Srv.class), MedicineMaster.class.getName());
        textHelpEvents();
    }

}
