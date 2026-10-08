package dataman.payroll.earningDeductionMaster.client;

import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.payroll.earningDeductionMaster.shared.EarningDeductionMaster_Srv;
import dataman.payroll.earningDeductionMaster.shared.EarningDeductionMaster_SrvAsync;
import dataman.payroll.earningDeductionMaster.shared.GrdEarnDed_Mdl;
import java.util.List;


public class EarningDeductionGridEdit<T1, T2> extends EarningDeductionGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(GrdEarnDed_Mdl earnDedMdl);

        public void onDelete();
    }

    private ServiceInvoker<EarningDeductionMaster_SrvAsync> srvAsync;
    private CallBack callBack;
    private int hashCode = 0;
    private String earnDedName = "";

    private void textHelpEvents() {

        txtHEarnDed.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<GrdEarnDed_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHEarnDed(earnDedName,helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<GrdEarnDed_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHEarnDed).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GrdEarnDed_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHEarnDed.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHEarnDed).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, GrdEarnDed_Mdl t1Row) {
                txtHEarnDed.setText(DmShared.xNull(t1Row.getShortName()));
                txtHEarnDed.setTag(DmShared.xNull(t1Row.getSharedKeyFieldCode()));
            }
        });

    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded,String name) {
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
        earnDedName = name;
    }

    @Override
    public void displayRow(T1 row) {
        GrdEarnDed_Mdl ourRow = (GrdEarnDed_Mdl) row;

        txtHEarnDed.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHEarnDed.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        hashCode = ourRow.getSharedKeyFieldHashCode();

        txtHEarnDed.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdEarnDed_Mdl();
    }

    @Override
    public void onOk() {
        try {
            if (DmShared.xNull(txtHEarnDed.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Earning/Deduction is mandatory.", txtHEarnDed).show();
                return;
            }

            for (GrdEarnDed_Mdl detail_Mdl : (List<GrdEarnDed_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
                if (DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(txtHEarnDed.getTag()))) {
                    if (!DmClient.vNull(detail_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                        new DmAlertMessageBox(DmShared.xNull(txtHEarnDed.getText()) + " already exists.", txtHEarnDed).show();
                        return;
                    }
                }
            }

            proceedOk();
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtHEarnDed).show();
        }
    }

    @Override
    public void manageOk(T1 row) {
        GrdEarnDed_Mdl ourRow = (GrdEarnDed_Mdl) row;

        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHEarnDed.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHEarnDed.getText()));
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

    public EarningDeductionGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Earning/Deduction", "310px", "55px");
        srvAsync = new ServiceInvoker<EarningDeductionMaster_SrvAsync>(GWT.create(EarningDeductionMaster_Srv.class), EarningDeductionMaster.class.getName());
        textHelpEvents();
    }

}
