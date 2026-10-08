package dataman.dmToolkit.overHead.client;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.overHead.shared.*;
import java.util.List;

public class OverHeadGridMAEdit<T1, T2> extends OverHeadGridMAEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(OverHeadMA_Mdl grdTdsEntry);

        public void onDelete();
    }

    private final ServiceInvoker<OverHead_SrvAsync> srvAsync;
    private CallBack callBack;
    private OverHeadMA_Mdl parentRow;
    private int hashCode = 0;

    private void textHelpEvents() {

        txtHPostingAC.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<AccountHelp_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHPostingAC(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<AccountHelp_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPostingAC).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<AccountHelp_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHPostingAC.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPostingAC).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, AccountHelp_Mdl t1Row) {
            }
        });
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {
        super.showRelativeTo(currentRowIndex, 0, okNeeded, newNeeded, deleteNeeded);
    }

    @Override
    public void displayRow(T1 row) {
        OverHeadMA_Mdl ourRow = (OverHeadMA_Mdl) row;
        parentRow = new OverHeadMA_Mdl();
        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtAmount.setText(DmClient.vNull(ourRow.getAmount(), "0.00"));
        txtHPostingAC.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHPostingAC.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));

        txtHPostingAC.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new OverHeadMA_Mdl();
    }

    @Override
    public void onOk() {
        if (DmShared.xNull(txtHPostingAC.getTag()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("A/c Name is mandatory.", txtHPostingAC).show();
            return;
        }

        if (DmShared.vNull(txtAmount.getText()).doubleValue() == 0) {
            new DmAlertMessageBox("Amount is mandatory.", txtAmount).show();
            return;
        }
        for (OverHeadMA_Mdl overHeadMA : (List<OverHeadMA_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
            if (DmShared.vNull(hashCode).doubleValue() != DmShared.vNull(overHeadMA.getSharedKeyFieldHashCode()).doubleValue()) {
                if (DmShared.xNull(txtHPostingAC.getTag()).equalsIgnoreCase(DmShared.xNull(overHeadMA.getSharedKeyFieldCode()))) {
                    new DmAlertMessageBox("A/c Name is already exists.", txtHPostingAC).show();
                    return;
                }
            }
        }

        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        OverHeadMA_Mdl ourRow = (OverHeadMA_Mdl) row;

        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHPostingAC.getText()));
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHPostingAC.getTag()));
        ourRow.setAmount(DmClient.vNull(txtAmount.getText(), "0.00"));

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

    public OverHeadGridMAEdit(DmGrid<T1, T2> grid) {
        super(grid, "Multi A/c", "335px", "70px");
        srvAsync = new ServiceInvoker<>(GWT.create(OverHead_Srv.class), OverHead.class.getName());
        textHelpEvents();
    }
}
