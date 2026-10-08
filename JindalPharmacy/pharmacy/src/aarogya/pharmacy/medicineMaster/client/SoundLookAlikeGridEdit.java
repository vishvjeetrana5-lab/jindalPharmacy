package aarogya.pharmacy.medicineMaster.client;

import aarogya.pharmacy.medicineMaster.shared.MedicineMaster_Srv;
import aarogya.pharmacy.medicineMaster.shared.MedicineMaster_SrvAsync;
import aarogya.pharmacy.medicineMaster.shared.GrdSoundLookAlike_Mdl;
import aarogya.pharmacy.medicineMaster.shared.TxtHItemsName_Mdl;
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

public class SoundLookAlikeGridEdit<T1, T2> extends SoundLookAlikeGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(GrdSoundLookAlike_Mdl gridVendor_Mdl);

        public void onDelete();
    }

    private ServiceInvoker<MedicineMaster_SrvAsync> srvAsync;
    private CallBack callBack;
    private int hashCode = 0;
    MedicineMaster itemMaster;

    private void textHelpEvents() {

        txtHItemName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHItemsName_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                String itemCode = "";
                if (!itemMaster.topControl.getMode().equalsIgnoreCase("New")) {
                    itemCode = itemMaster.topControl.getDocId();
                }
                srvAsync.invokeRPC(blnCallBackWait).getTxtHItemName(DmShared.iNull(itemCode).toString(), false, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHItemsName_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHItemName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHItemsName_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHItemName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHItemName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHItemsName_Mdl t1Row) {
            }
        });

    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, MedicineMaster itemMaster) {
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
        this.itemMaster = itemMaster;
    }

    @Override
    public void displayRow(T1 row) {
        GrdSoundLookAlike_Mdl ourRow = (GrdSoundLookAlike_Mdl) row;

        txtHItemName.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHItemName.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtHItemName.setTag2nd(DmShared.xNull(ourRow.getV_SNo()));
        hashCode = ourRow.getSharedKeyFieldHashCode();

        txtHItemName.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdSoundLookAlike_Mdl();
    }

    @Override
    public void onOk() {
        try {
            if (DmShared.xNull(txtHItemName.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Medicine is mandatory.", txtHItemName).show();
                return;
            }

            for (GrdSoundLookAlike_Mdl detail_Mdl : (List<GrdSoundLookAlike_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
                if (DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(txtHItemName.getTag()))) {
                    if (!DmClient.vNull(detail_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                        new DmAlertMessageBox(DmShared.xNull(txtHItemName.getText()) + " already exists.", txtHItemName).show();
                        return;
                    }
                }
            }

            proceedOk();
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtHItemName).show();
        }
    }

    @Override
    public void manageOk(T1 row) {
        GrdSoundLookAlike_Mdl ourRow = (GrdSoundLookAlike_Mdl) row;

        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHItemName.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHItemName.getText()));

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

    public SoundLookAlikeGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Sound & Look Alike", "380px", "55px");
        srvAsync = new ServiceInvoker<MedicineMaster_SrvAsync>(GWT.create(MedicineMaster_Srv.class), MedicineMaster.class.getName());
        textHelpEvents();
    }

}
