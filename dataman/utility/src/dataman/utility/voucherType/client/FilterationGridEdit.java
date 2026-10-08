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

public class FilterationGridEdit<T1, T2> extends FilterationGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(GrdACFilteration_Mdl costCenterGrid);

        public void onDelete();
    }
    ServiceInvoker<VoucherType_SrvAsync> SrvAsync;
    CallBack callBack;
    int hashCode;

    private void textHelpEvents() {
        txtHNature.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHNature(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHNature).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHNature.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHNature).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
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
        GrdACFilteration_Mdl ourRow = (GrdACFilteration_Mdl) row;
        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtHNature.setText(DmShared.xNull(ourRow.getNature()));
        txtHNature.setTag(DmShared.xNull(ourRow.getNatureCode()));
        txtHNature.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdACFilteration_Mdl();
    }

    @Override
    public void onOk() {
        if (!DmClient.isValidText(txtHNature.getTag(), "A/c Filtration", txtHNature)) {
            return;
        }

        List<GrdACFilteration_Mdl> lstTemp = (List<GrdACFilteration_Mdl>) getDmGrid().getGrid().getStore().getAll();

        for (int i = 0; i < lstTemp.size(); i++) {
            GrdACFilteration_Mdl get = lstTemp.get(i);
            if (DmShared.xNull(txtHNature.getTag()).equalsIgnoreCase(DmShared.xNull(get.getNatureCode()))) {
                if (DmShared.vNull(hashCode).doubleValue() != DmShared.vNull(get.getSharedKeyFieldHashCode()).doubleValue()) {
                    new DmAlertMessageBox(txtHNature.getText() + " already exists.", txtHNature).show();
                    return;
                }
            }
        }

        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdACFilteration_Mdl ourRow = (GrdACFilteration_Mdl) row;
        ourRow.setNatureCode(DmShared.xNull(txtHNature.getTag()));
        ourRow.setNature(DmShared.xNull(txtHNature.getText()));
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

    public FilterationGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "A/c Filteration", "300px", "50px");
        SrvAsync = new ServiceInvoker<VoucherType_SrvAsync>(GWT.create(VoucherType_Srv.class), VoucherType.class.getName());
        textHelpEvents();
    }

}
