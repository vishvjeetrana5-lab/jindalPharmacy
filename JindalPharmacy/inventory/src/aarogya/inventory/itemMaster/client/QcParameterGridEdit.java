package aarogya.inventory.itemMaster.client;

import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;
import aarogya.inventory.itemMaster.shared.GrdQcParameter_Mdl;
import aarogya.inventory.itemMaster.shared.ItemMaster_Srv;
import aarogya.inventory.itemMaster.shared.ItemMaster_SrvAsync;

public class QcParameterGridEdit<T1, T2> extends QcParameterGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(GrdQcParameter_Mdl grdQcParameter_Mdl);

        public void onDelete();
    }

    private final ServiceInvoker<ItemMaster_SrvAsync> srvAsync;
    private CallBack callBack;
    private int hashCode = 0;

    private void textHelpEvents() {
        txtHQCParameter.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHImpurity(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHQCParameter).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHQCParameter.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHQCParameter).show();
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
        GrdQcParameter_Mdl ourRow = (GrdQcParameter_Mdl) row;

        txtHQCParameter.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHQCParameter.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtHQCParameter.setTag2nd(DmShared.xNull(ourRow.getV_SNo()));
        hashCode = ourRow.getSharedKeyFieldHashCode();

        txtHQCParameter.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdQcParameter_Mdl();
    }

    @Override
    public void onOk() {
        try {
            if (DmShared.xNull(txtHQCParameter.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("QC Parameter is mandatory.", txtHQCParameter).show();
                return;
            }

            for (GrdQcParameter_Mdl detail_Mdl : (List<GrdQcParameter_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
                if (DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(txtHQCParameter.getTag()))) {
                    if (!DmClient.vNull(detail_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                        new DmAlertMessageBox(DmShared.xNull(txtHQCParameter.getText()) + " already exists.", txtHQCParameter).show();
                        return;
                    }
                }
            }

            proceedOk();
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtHQCParameter).show();
        }
    }

    @Override
    public void manageOk(T1 row) {
        GrdQcParameter_Mdl ourRow = (GrdQcParameter_Mdl) row;

        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHQCParameter.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHQCParameter.getText()));
        ourRow.setV_SNo(DmShared.xNull(txtHQCParameter.getTag2nd()));

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

    public QcParameterGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "QC Parameter", "420px", "60px");
        srvAsync = new ServiceInvoker<ItemMaster_SrvAsync>(GWT.create(ItemMaster_Srv.class), ItemMaster.class.getName());
        textHelpEvents();
    }

}
