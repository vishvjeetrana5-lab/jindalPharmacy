package aarogya.master.referredMaster.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import aarogya.master.referredMaster.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public class ClinicDetailGridEdit<T1, T2> extends ClinicDetailGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(GrdClinicDetail_Mdl grdTdsEntry);

        public void onDelete();
    }

    private ServiceInvoker<ReferredMaster_SrvAsync> srvAsync;
    private CallBack callBack;
    private GrdClinicDetail_Mdl parentRow;

    private void textHelpEvents() {
        txtHCity.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHCity_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHCity(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHCity_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCity).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHCity_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHCity.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCity).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHCity_Mdl t1Row) {
            }
        });
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
        txtSite.setFocus(true);
    }

    @Override
    public void displayRow(T1 row) {
        GrdClinicDetail_Mdl ourRow = (GrdClinicDetail_Mdl) row;
        parentRow = new GrdClinicDetail_Mdl();
        txtAddress.setText(DmShared.xNull(ourRow.getAddress()));
        txtAddress1.setText(DmShared.xNull(ourRow.getAddress1()));
        txtHCity.setTag(DmShared.xNull(ourRow.getCityTag()));
        txtHCity.setText(DmShared.xNull(ourRow.getCityText()));
        txtPhone.setText(DmShared.xNull(ourRow.getPhone()));
        txtSite.setText(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtSite.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtTiming.setText(DmShared.xNull(ourRow.getTiming()));
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdClinicDetail_Mdl();
    }

    @Override
    public void onOk() {
        if (DmShared.xNull(txtSite.getText()).trim().equalsIgnoreCase("")) {
            txtSite.setFocus(true);
            return;
        }
        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdClinicDetail_Mdl ourRow = (GrdClinicDetail_Mdl) row;
        ourRow.setAddress(DmShared.xNull(txtAddress.getText()));
        ourRow.setAddress1(DmShared.xNull(txtAddress1.getText()));
        ourRow.setCityTag(DmShared.xNull(txtHCity.getTag()));
        ourRow.setCityText(DmShared.xNull(txtHCity.getText()));
        ourRow.setPhone(DmShared.xNull(txtPhone.getText()));
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtSite.getText()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtSite.getText()));
        ourRow.setTiming(DmShared.xNull(txtTiming.getText()));

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

    public ClinicDetailGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Clinic Detail", "500px", "140px");
        srvAsync = new ServiceInvoker<>(GWT.create(ReferredMaster_Srv.class), ReferredMaster.class.getName());
        textHelpEvents();
    }
}
