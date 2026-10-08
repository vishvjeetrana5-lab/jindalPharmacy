package dataman.utility.userMaster.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.utility.userMaster.shared.GrdLinkedUser_Mdl;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.utility.userMaster.shared.*;
import java.util.List;

public class LinkedUserGridEdit<T1, T2> extends LinkedUserEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(GrdLinkedUser_Mdl linkedUserGrid);

        public void onDelete();
    }

    ServiceInvoker<UserMaster_SrvAsync> SrvAsync;
    CallBack callBack;
    int hashCode;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
        txtHLinkedUserData.setFocus(true);
    }

    @Override
    public void displayRow(T1 row) {
        GrdLinkedUser_Mdl ourRow = (GrdLinkedUser_Mdl) row;
        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtHLinkedUserData.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHLinkedUserData.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtShortName.setText(DmShared.xNull(ourRow.getDescription()));
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdLinkedUser_Mdl();
    }

    @Override
    public void onOk() {
        if (!DmClient.isValidText(txtHLinkedUserData.getTag(), "Linked User", txtHLinkedUserData)) {
            return;
        }

        for (GrdLinkedUser_Mdl node : (List<GrdLinkedUser_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
            if (DmShared.xNull(txtHLinkedUserData.getTag()).equalsIgnoreCase(DmShared.xNull(node.getSharedKeyFieldCode()))) {
                if (DmShared.vNull(hashCode).doubleValue() != DmShared.vNull(node.getSharedKeyFieldHashCode()).doubleValue()) {
                    new DmAlertMessageBox(txtHLinkedUserData.getText() + " already exists.", txtHLinkedUserData).show();
                    return;
                }
            }
        }

        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdLinkedUser_Mdl ourRow = (GrdLinkedUser_Mdl) row;
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHLinkedUserData.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHLinkedUserData.getText()));
        ourRow.setDescription(DmShared.xNull(txtShortName.getText()));
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

    public void textHelpEvent() {
        txtHLinkedUserData.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<GrdLinkedUser_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHLinkedUser(helper, "", blnToEndOfFile, new AsyncCallback<DmResultPageing<List<GrdLinkedUser_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GrdLinkedUser_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHLinkedUserData.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, GrdLinkedUser_Mdl tRow) {
                if (blnStatus) {
                    if (tRow != null) {
                        txtShortName.setText(DmShared.xNull(tRow.getDescription()));
                    }

                }
            }
        });
    }

    public LinkedUserGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Linked User", "350px", "70px");
        SrvAsync = new ServiceInvoker<UserMaster_SrvAsync>(GWT.create(UserMaster_Srv.class), UserMaster.class.getName());
        textHelpEvent();
    }
}
