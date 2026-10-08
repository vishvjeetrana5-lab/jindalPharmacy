package aarogya.pharmacy.medicineMaster.client;

import aarogya.pharmacy.medicineMaster.shared.GrdAlternativeItems_Mdl;
import aarogya.pharmacy.medicineMaster.shared.TxtHItemsName_Mdl;
import aarogya.pharmacy.medicineMaster.shared.MedicineMaster_Srv;
import aarogya.pharmacy.medicineMaster.shared.MedicineMaster_SrvAsync;
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

public class AlternativeItemsGridEdit<T1, T2> extends AlternativeItemsGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(GrdAlternativeItems_Mdl gridAlternativeItems_Mdl);

        public void onDelete();
    }

    private ServiceInvoker<MedicineMaster_SrvAsync> srvAsync;
    private CallBack callBack;
    private int hashCode = 0;
    MedicineMaster itemMaster;

    private void textHelpEvents() {

        txtHAlternativeItems.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHItemsName_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                String itemCode = "";
                if (!itemMaster.topControl.getMode().equalsIgnoreCase("New")) {
                    itemCode = itemMaster.topControl.getDocId();
                }

                srvAsync.invokeRPC(blnCallBackWait).getTxtHItemName(DmShared.iNull(itemCode).toString(), false, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHItemsName_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAlternativeItems).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHItemsName_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHAlternativeItems.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHAlternativeItems).show();
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
        GrdAlternativeItems_Mdl ourRow = (GrdAlternativeItems_Mdl) row;

        txtHAlternativeItems.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHAlternativeItems.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtHAlternativeItems.setTag2nd(DmShared.xNull(ourRow.getV_SNo()));

        hashCode = ourRow.getSharedKeyFieldHashCode();

        txtHAlternativeItems.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdAlternativeItems_Mdl();
    }

    @Override
    public void onOk() {
        try {
            if (DmShared.xNull(txtHAlternativeItems.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Medicine is mandatory.", txtHAlternativeItems).show();
                return;
            }

            for (GrdAlternativeItems_Mdl detail_Mdl : (List<GrdAlternativeItems_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
                if (DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(txtHAlternativeItems.getTag()))) {
                    if (!DmClient.vNull(detail_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                        new DmAlertMessageBox(DmShared.xNull(txtHAlternativeItems.getText()) + " already exists.", txtHAlternativeItems).show();
                        return;
                    }
                }
            }

            proceedOk();
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtHAlternativeItems).show();
        }
    }

    @Override
    public void manageOk(T1 row) {
        GrdAlternativeItems_Mdl ourRow = (GrdAlternativeItems_Mdl) row;

        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHAlternativeItems.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHAlternativeItems.getText()));

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

    public AlternativeItemsGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Alternative Items", "380px", "55px");
        srvAsync = new ServiceInvoker<MedicineMaster_SrvAsync>(GWT.create(MedicineMaster_Srv.class), MedicineMaster.class.getName());
        textHelpEvents();
    }

}
