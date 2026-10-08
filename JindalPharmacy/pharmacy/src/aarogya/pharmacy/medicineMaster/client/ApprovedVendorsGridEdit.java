package aarogya.pharmacy.medicineMaster.client;

import aarogya.pharmacy.medicineMaster.shared.*;
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

public class ApprovedVendorsGridEdit<T1, T2> extends ApprovedVendorsGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(GrdApprovedVendors_Mdl gridVendor_Mdl);

        public void onDelete();
    }

    private final ServiceInvoker<MedicineMaster_SrvAsync> srvAsync;
    private CallBack callBack;
    private int hashCode = 0;

    private void textHelpEvents() {
        txtHApprovedVendors.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHApprovedVendors_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHApprovedVendors(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHApprovedVendors_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHApprovedVendors).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHApprovedVendors_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHApprovedVendors.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHApprovedVendors).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHApprovedVendors_Mdl t1Row) {
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
        GrdApprovedVendors_Mdl ourRow = (GrdApprovedVendors_Mdl) row;

        txtHApprovedVendors.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHApprovedVendors.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtHApprovedVendors.setTag2nd(DmShared.xNull(ourRow.getV_SNo()));
        hashCode = ourRow.getSharedKeyFieldHashCode();

        txtHApprovedVendors.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdApprovedVendors_Mdl();
    }

    @Override
    public void onOk() {
        try {
            if (DmShared.xNull(txtHApprovedVendors.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Approved Suppliers is mandatory.", txtHApprovedVendors).show();
                return;
            }

            for (GrdApprovedVendors_Mdl detail_Mdl : (List<GrdApprovedVendors_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
                if (DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(txtHApprovedVendors.getTag()))) {
                    if (!DmClient.vNull(detail_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                        new DmAlertMessageBox(DmShared.xNull(txtHApprovedVendors.getText()) + " already exists.", txtHApprovedVendors).show();
                        return;
                    }
                }
            }

            proceedOk();
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtHApprovedVendors).show();
        }
    }

    @Override
    public void manageOk(T1 row) {
        GrdApprovedVendors_Mdl ourRow = (GrdApprovedVendors_Mdl) row;

        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHApprovedVendors.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHApprovedVendors.getText()));
        ourRow.setV_SNo(DmShared.xNull(txtHApprovedVendors.getTag2nd()));

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

    public ApprovedVendorsGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Approved Suppliers", "390px", "55px");
        srvAsync = new ServiceInvoker<MedicineMaster_SrvAsync>(GWT.create(MedicineMaster_Srv.class), MedicineMaster.class.getName());
        textHelpEvents();
    }

}
