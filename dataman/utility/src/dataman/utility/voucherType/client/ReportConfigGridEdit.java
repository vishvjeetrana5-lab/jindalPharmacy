package dataman.utility.voucherType.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.utility.voucherType.shared.*;
import java.util.List;

public class ReportConfigGridEdit<T1, T2> extends ReportConfigGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(GrdReportConfig_Mdl grdReportMdl);

        public void onDelete();
    }

    ServiceInvoker<VoucherType_SrvAsync> SrvAsync;
    CallBack callBack;
    int hashCode;

    private void textHelpEvents() {
        txtHSite.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSite(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSite).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSite.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHSite).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {

            }
        });

        txtHPageSize.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHPageSize(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPageSize).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPageSize.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHPageSize).show();
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
        txtHSite.setFocus(true);
    }

    @Override
    public void displayRow(T1 row) {
        GrdReportConfig_Mdl ourRow = (GrdReportConfig_Mdl) row;
        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtReportName.setText(DmShared.xNull(ourRow.getReportName()));
        txtHSite.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHSite.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtHPageSize.setText(DmShared.xNull(ourRow.getPageSize()));

        txtHSite.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdReportConfig_Mdl();
    }

    @Override
    public void onOk() {
        if (!DmClient.isValidText(txtReportName.getText(), "Report Name", txtReportName)) {
            return;
        }

        List<GrdReportConfig_Mdl> lstTemp = (List<GrdReportConfig_Mdl>) getDmGrid().getGrid().getStore().getAll();

        for (int i = 0; i < lstTemp.size(); i++) {
            GrdReportConfig_Mdl get = lstTemp.get(i);
            if (DmShared.xNull(txtHSite.getTag()).equalsIgnoreCase(DmShared.xNull(get.getSharedKeyFieldCode()))) {
                if (!DmShared.xNull(get.getReportName()).equalsIgnoreCase("")) {
                    if (DmShared.vNull(hashCode).doubleValue() != DmShared.vNull(get.getSharedKeyFieldHashCode()).doubleValue()) {
                        new DmAlertMessageBox("Site is duplicate.", txtHSite).show();
                        return;
                    }
                }
            }
        }
        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdReportConfig_Mdl ourRow = (GrdReportConfig_Mdl) row;
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHSite.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHSite.getText()));
        ourRow.setReportName(DmShared.xNull(txtReportName.getText()));
        ourRow.setPageSize(DmShared.xNull(txtHPageSize.getText()));
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

    public ReportConfigGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Report Config", "350px", "119px");
        SrvAsync = new ServiceInvoker<>(GWT.create(VoucherType_Srv.class), VoucherType.class.getName());
        textHelpEvents();
    }
}
