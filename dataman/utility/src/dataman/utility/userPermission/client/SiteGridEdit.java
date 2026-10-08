package dataman.utility.userPermission.client;

import dataman.utility.userPermission.shared.GrdSite_Mdl;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;
import dataman.utility.userPermission.shared.UserPermission_Srv;
import dataman.utility.userPermission.shared.UserPermission_SrvAsync;

public class SiteGridEdit<T1, T2> extends SiteGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(GrdSite_Mdl siteGrid);

        public void onDelete();
    }

    private final ServiceInvoker<UserPermission_SrvAsync> SrvAsync;
    private CallBack callBack;
    private int hashCode;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void textHelpEvent() {
        txtHSite.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<GrdSite_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSite(helper, "", blnToEndOfFile, new AsyncCallback<DmResultPageing<List<GrdSite_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GrdSite_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHSite.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, GrdSite_Mdl tRow) {
                if (blnStatus && tRow != null) {
                    txtShortName.setText(DmShared.xNull(tRow.getShortName()));
                } else {
                    txtShortName.setText("");
                }
            }
        });
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
        txtHSite.setFocus(true);
    }

    @Override
    public void displayRow(T1 row) {
        GrdSite_Mdl ourRow = (GrdSite_Mdl) row;
        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtHSite.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHSite.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtShortName.setText(DmShared.xNull(ourRow.getShortName()));
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdSite_Mdl();
    }

    @Override
    public void onOk() {
        if (!DmClient.isValidText(txtHSite.getTag(), "Site", txtHSite)) {
            return;
        }

        for (GrdSite_Mdl node : (List<GrdSite_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
            if (DmShared.xNull(txtHSite.getTag()).equalsIgnoreCase(DmShared.xNull(node.getSharedKeyFieldCode()))) {
                if (DmShared.vNull(hashCode).doubleValue() != DmShared.vNull(node.getSharedKeyFieldHashCode()).doubleValue()) {
                    new DmAlertMessageBox(txtHSite.getText() + " already exists.", txtHSite).show();
                    return;
                }
            }
        }

        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdSite_Mdl ourRow = (GrdSite_Mdl) row;
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHSite.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHSite.getText()));
        ourRow.setShortName(DmShared.xNull(txtShortName.getText()));
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

    public SiteGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Site", "350px", "70px");
        SrvAsync = new ServiceInvoker<UserPermission_SrvAsync>(GWT.create(UserPermission_Srv.class), UserPermission.class.getName());
        textHelpEvent();
    }
}
