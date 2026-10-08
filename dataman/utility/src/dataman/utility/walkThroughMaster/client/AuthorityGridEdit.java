package dataman.utility.walkThroughMaster.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.utility.walkThroughMaster.shared.GrdAuthority_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import java.util.List;
import dataman.utility.walkThroughMaster.shared.WalkThroughMaster_Srv;
import dataman.utility.walkThroughMaster.shared.WalkThroughMaster_SrvAsync;

public class AuthorityGridEdit<T1, T2> extends AuthorityGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(GrdAuthority_Mdl authority_Mdl);

        public void onDelete();
    }

    private ServiceInvoker<WalkThroughMaster_SrvAsync> SrvAsync;
    private CallBack callBack;
    private int hashCode = 0, srno = 0;

    private void textHelpEvents() {

        txtHLinkedUser.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHUser(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHLinkedUser).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHLinkedUser.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHLinkedUser).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
                if (blnStatus) {
                    txtHLinkedUser.setText(keyName + ",");
                }
            }
        });

    }

    private void gridEvents() {

    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;

    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);

    }

    @Override
    public void displayRow(T1 row) {
        GrdAuthority_Mdl ourRow = (GrdAuthority_Mdl) row;
        txtSrlNo.setText(DmShared.xNull(ourRow.getSrlNo()));
        txtauthorityName.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtauthorityName.setText(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHLinkedUser.setTag(DmShared.xNull(ourRow.getLinkedUserCode()));
        txtHLinkedUser.setText(DmShared.xNull(ourRow.getLinkedUserName()));

        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtSrlNo.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdAuthority_Mdl();
    }

    @Override
    public void onOk() {
        GrdAuthority_Mdl mrTemp;
        mrTemp = new GrdAuthority_Mdl();
        try {
            if (DmShared.xNull(txtSrlNo.getText()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("S.No. is mandatory.", txtSrlNo).show();
                return;
            }

            if (DmShared.xNull(txtauthorityName.getText()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Authority is mandatory.", txtauthorityName).show();
                return;
            }
            if (DmShared.xNull(txtHLinkedUser.getText()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Linked User is mandatory.", txtHLinkedUser).show();
                return;
            }
            for (GrdAuthority_Mdl authority_Mdl : (List<GrdAuthority_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
                if (DmShared.xNull(authority_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(txtauthorityName.getText()).trim())) {
                    if (!DmClient.vNull(authority_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                        new DmAlertMessageBox(DmShared.xNull(txtauthorityName.getText()) + " already exists.", txtauthorityName).show();
                        return;
                    }
                }
            }
//              for (GrdAuthority_Mdl authority_Mdl1 : (List<GrdAuthority_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
//                if (DmShared.xNull(authority_Mdl1.getSrlNo()).equalsIgnoreCase(DmShared.xNull(txtSrlNo.getText()).trim())) {
//                    if (!DmClient.vNull(authority_Mdl1.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
//                        new DmAlertMessageBox(DmShared.xNull(txtSrlNo.getText()) + " already exists.", txtSrlNo).show();
//                        return;
//                    }
//                }
//            }
            proceedOk();

        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtauthorityName).show();
        }
    }

    @Override
    public void manageOk(T1 row) {
        GrdAuthority_Mdl ourRow = (GrdAuthority_Mdl) row;
        ourRow.setSrlNo(DmShared.xNull(txtSrlNo.getText()));

        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtauthorityName.getText()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtauthorityName.getText()));

        ourRow.setLinkedUserCode(DmShared.xNull(txtHLinkedUser.getTag()));
        ourRow.setLinkedUserName(DmShared.xNull(txtHLinkedUser.getText()));

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

    public AuthorityGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Authority", "340px", "90px");
        SrvAsync = new ServiceInvoker<WalkThroughMaster_SrvAsync>(GWT.create(WalkThroughMaster_Srv.class), WalkThroughMaster.class.getName());
        textHelpEvents();

        gridEvents();

    }

}
