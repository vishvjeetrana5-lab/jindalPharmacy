package dataman.utility.voucherType.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.utility.voucherType.shared.*;
import java.util.List;

public class FormAppliedGridEdit<T1, T2> extends FormAppliedGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(GrdFormApplied_Mdl frmAppliedGrid);

        public void onDelete();
    }
    ServiceInvoker<VoucherType_SrvAsync> SrvAsync;
    CallBack callBack;
    int hashCode;

    private void textHelpEvents() {
        txtHFormApplied.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHFormApplied_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHFormApplied(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHFormApplied_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHFormApplied).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHFormApplied_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHFormApplied.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHFormApplied).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHFormApplied_Mdl tRow) {
                if (blnStatus) {
                    if (tRow != null) {
                        txtHFormApplied.setText(tRow.getSharedKeyFieldCode());
                        txtHFormApplied.setText(tRow.getSharedKeyFieldName());
                        txtTRN.setText(tRow.getAppliedFor());
                        txtLC.setText(tRow.getLocation());
                    }
                }
            }
        });
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
        txtHFormApplied.setFocus(true);
    }

    @Override
    public void displayRow(T1 row) {
        GrdFormApplied_Mdl ourRow = (GrdFormApplied_Mdl) row;
        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtHFormApplied.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtHFormApplied.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtTRN.setTag(DmShared.xNull(ourRow.getAppliedFor()));
        txtLC.setTag(DmShared.xNull(ourRow.getLocation()));
        txtHFormApplied.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdFormApplied_Mdl();
    }

    @Override
    public void onOk() {
        if (!DmClient.isValidText(txtHFormApplied.getTag(), "Name", txtHFormApplied)) {
            return;
        }

        List<GrdFormApplied_Mdl> lstTemp = (List<GrdFormApplied_Mdl>) getDmGrid().getGrid().getStore().getAll();

        for (int i = 0; i < lstTemp.size(); i++) {
            GrdFormApplied_Mdl get = lstTemp.get(i);
            if (DmShared.xNull(txtHFormApplied.getTag()).equalsIgnoreCase(DmShared.xNull(get.getSharedKeyFieldCode()))) {
                if (DmShared.vNull(hashCode).doubleValue() != DmShared.vNull(get.getSharedKeyFieldHashCode()).doubleValue()) {
                    new DmAlertMessageBox(txtHFormApplied.getText() + " already exists.", txtHFormApplied).show();
                    return;
                }
            }
        }
        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdFormApplied_Mdl ourRow = (GrdFormApplied_Mdl) row;
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHFormApplied.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHFormApplied.getText()));
        ourRow.setAppliedFor(DmShared.xNull(txtTRN.getText()));
        ourRow.setLocation(DmShared.xNull(txtLC.getText()));
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

    public FormAppliedGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Form Applied", "300px", "50px");
        SrvAsync = new ServiceInvoker<VoucherType_SrvAsync>(GWT.create(VoucherType_Srv.class), VoucherType.class.getName());
        textHelpEvents();
    }
}
