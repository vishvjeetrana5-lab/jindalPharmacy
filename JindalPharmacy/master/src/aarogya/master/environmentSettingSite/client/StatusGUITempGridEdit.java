package aarogya.master.environmentSettingSite.client;

import aarogya.master.environmentSettingSite.shared.*;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public class StatusGUITempGridEdit<T1, T2> extends StatusGUITempGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(GrdStatusGUITemp_Mdl statusGUITemp_Mdl);

        public void onDelete();
    }

    private final ServiceInvoker<EnvironmentSettingSite_SrvAsync> SrvAsync;
    private CallBack callBack;
    private GrdStatusGUITemp_Mdl parentRow;
    private int hashCode;

    private void textHelpEvents() {
        txtHGuiTemplet.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHTemplate(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHGuiTemplet).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHGuiTemplet.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHGuiTemplet).show();
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
        GrdStatusGUITemp_Mdl ourRow = (GrdStatusGUITemp_Mdl) row;
        parentRow = new GrdStatusGUITemp_Mdl();

        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtHGuiTemplet.setTag(ourRow.getTemplateCode());
        txtHGuiTemplet.setText(ourRow.getTemplateName());
        txtStatus.setTag(ourRow.getSharedKeyFieldCode());
        txtStatus.setText(ourRow.getSharedKeyFieldName());
        txtHGuiTemplet.setFocus(true);
        txtStatus.setEnabled(false);

    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdStatusGUITemp_Mdl();
    }

    @Override
    public void onOk() {
        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdStatusGUITemp_Mdl ourRow = (GrdStatusGUITemp_Mdl) row;
        ourRow.setTemplateCode(DmShared.xNull(txtHGuiTemplet.getTag()));
        ourRow.setTemplateName(DmShared.xNull(txtHGuiTemplet.getText()));
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtStatus.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtStatus.getText()));
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

    public StatusGUITempGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Status GUI Template", "390px", "95px");
        SrvAsync = new ServiceInvoker<>(GWT.create(EnvironmentSettingSite_Srv.class), EnvironmentSettingSite.class.getName());
        textHelpEvents();
    }
}
