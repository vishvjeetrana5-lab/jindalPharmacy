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

public class WebJobsGridEdit<T1, T2> extends WebJobsGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(GrdWebJobs_Mdl grdWebJobsMdl);

        public void onDelete();
    }

    ServiceInvoker<VoucherType_SrvAsync> SrvAsync;
    CallBack callBack;
    int hashCode;

    private void textHelpEvents() {
        txtHTemplateName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHTemplate_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHTemplateName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHTemplate_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHTemplateName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHTemplate_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHTemplateName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHTemplateName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHTemplate_Mdl tRow) {
                if (blnStatus && tRow != null) {
                    txtTemplateType.setTag(tRow.getTemplateTypeCode());
                    txtTemplateType.setText(tRow.getWebJobType().getWebJobName());
                } else {
                    txtTemplateType.setTag("");
                    txtTemplateType.setText("");
                }
            }
        });
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
        txtHTemplateName.setFocus(true);
    }

    @Override
    public void displayRow(T1 row) {
        GrdWebJobs_Mdl ourRow = (GrdWebJobs_Mdl) row;
        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtHTemplateName.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHTemplateName.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtTemplateType.setTag(DmShared.xNull(ourRow.getTemplateTypeCode()));
        txtTemplateType.setText(DmShared.xNull(ourRow.getTemplateTypeName()));
        txtTemplateType.setEnabled(false);
        txtHTemplateName.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdWebJobs_Mdl();
    }

    @Override
    public void onOk() {
        if (!DmClient.isValidText(txtHTemplateName.getTag(), "Template  Name", txtHTemplateName)) {
            return;
        }

        List<GrdWebJobs_Mdl> lstTemp = (List<GrdWebJobs_Mdl>) getDmGrid().getGrid().getStore().getAll();

        for (int i = 0; i < lstTemp.size(); i++) {
            GrdWebJobs_Mdl get = lstTemp.get(i);
            if (DmShared.xNull(txtHTemplateName.getTag()).equalsIgnoreCase(DmShared.xNull(get.getSharedKeyFieldCode()))) {
                if (DmShared.vNull(hashCode).doubleValue() != DmShared.vNull(get.getSharedKeyFieldHashCode()).doubleValue()) {
                    new DmAlertMessageBox(txtHTemplateName.getText() + " already exists.", txtHTemplateName).show();
                    return;
                }
            }
        }
        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdWebJobs_Mdl ourRow = (GrdWebJobs_Mdl) row;
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHTemplateName.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHTemplateName.getText()));
        ourRow.setTemplateTypeCode(DmShared.xNull(txtTemplateType.getTag()));
        ourRow.setTemplateTypeName(DmShared.xNull(txtTemplateType.getText()));
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

    public WebJobsGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Web Jobs", "350px", "80px");
        SrvAsync = new ServiceInvoker<VoucherType_SrvAsync>(GWT.create(VoucherType_Srv.class), VoucherType.class.getName());
        textHelpEvents();
    }
}
