package aarogya.master.hsnMaster.client;

import aarogya.master.hsnMaster.shared.GrdITS_Mdl;
import aarogya.master.hsnMaster.shared.GrdRateITS_Mdl;
import aarogya.master.hsnMaster.shared.HSNMaster_Srv;
import aarogya.master.hsnMaster.shared.HSNMaster_SrvAsync;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import java.util.List;

public class ITSGridEdit<T1, T2> extends ITSGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(GrdITS_Mdl opdService);

        public void onDelete();
    }

    ServiceInvoker<HSNMaster_SrvAsync> SrvAsync;
    CallBack callBack;
    GrdITS_Mdl parentRow;
    int hashCode = 0;

    private void helpTextEvents() {
        txtHITS.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHITS(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHITS).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHITS.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHITS).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHAccName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHAccountName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAccName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHAccName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHAccName).show();
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
        GrdITS_Mdl ourRow = (GrdITS_Mdl) row;
        parentRow = new GrdITS_Mdl();

        txtCodeSNO.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtCodeSNO.setTag2nd(DmShared.xNull(ourRow.getsNo()));
        txtCodeSNO.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtHITS.setTag(DmShared.xNull(ourRow.getItsTag()));
        txtHITS.setText(DmShared.xNull(ourRow.getItsText()));
        txtHAccName.setTag(DmShared.xNull(ourRow.getItsAccountTag()));
        txtHAccName.setText(DmShared.xNull(ourRow.getItsAccountText()));

        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtHITS.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdITS_Mdl();
    }

    @Override
    public void onOk() {

        if (DmShared.xNull(txtHITS.getTag()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("ITS is mandatory.", txtHITS).show();
            return;
        }
        if (DmShared.xNull(txtHAccName.getTag()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Account Name is mandatory.", txtHAccName).show();
            return;
        }
        for (GrdITS_Mdl grdReciept_Mdl : (List<GrdITS_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
            if (DmShared.xNull(grdReciept_Mdl.getItsTag()).equalsIgnoreCase(DmShared.xNull(txtHITS.getTag()))) {
                if (!DmClient.vNull(grdReciept_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                    new DmAlertMessageBox(DmShared.xNull(txtHITS.getText()) + " already exists.", txtHITS).show();
                    return;
                }
            }
        }
        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdITS_Mdl ourRow = (GrdITS_Mdl) row;

        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtCodeSNO.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtCodeSNO.getText()));
        ourRow.setsNo(DmShared.xNull(txtCodeSNO.getTag2nd()));
        ourRow.setItsAccountTag(DmShared.xNull(txtHAccName.getTag()));
        ourRow.setItsAccountText(DmShared.xNull(txtHAccName.getText()));
        ourRow.setItsTag(DmShared.xNull(txtHITS.getTag()));
        ourRow.setItsText(DmShared.xNull(txtHITS.getText()));

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

    public ITSGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Rate Detail", "308px", "80px");
        SrvAsync = new ServiceInvoker<HSNMaster_SrvAsync>(GWT.create(HSNMaster_Srv.class), HSNMaster.class.getName());
        helpTextEvents();
    }

}
