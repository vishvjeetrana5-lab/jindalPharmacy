package dataman.utility.guiConfig.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.utility.guiConfig.shared.*;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public class SiteGridEdit<T1, T2> extends SiteGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(KeyFieldExtended keyField);

        public void onDelete();
    }

    private ServiceInvoker<GUIConfig_SrvAsync> srvAsync;
    private CallBack callBack;
    private int hashCode;

    private void textHelpEvents() {

        txtHSite.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSite(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSite).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSite.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSite).show();
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
        KeyFieldExtended ourRow = (KeyFieldExtended) row;

        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtHSite.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHSite.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));

        txtHSite.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new KeyFieldExtended();
    }

    @Override
    public void onOk() {

        if (DmShared.xNull(txtHSite.getTag()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Site is mandatory.", txtHSite).show();
            return;
        }

        for (KeyFieldExtended field : (List<KeyFieldExtended>) getDmGrid().getGrid().getStore().getAll()) {
            if (DmShared.vNull(hashCode).doubleValue() != DmShared.vNull(field.getSharedKeyFieldHashCode()).doubleValue()) {
                if (DmShared.xNull(txtHSite.getTag()).equalsIgnoreCase(DmShared.xNull(field.getSharedKeyFieldCode()))) {
                    new DmAlertMessageBox(txtHSite.getText() + " already exists.", txtHSite).show();
                    return;
                }
            }
        }

        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        KeyFieldExtended ourRow = (KeyFieldExtended) row;

        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHSite.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHSite.getText()));

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
        super(grid, "Applicable Site", "320px", "60px");
        srvAsync = new ServiceInvoker<GUIConfig_SrvAsync>(GWT.create(GUIConfig_Srv.class), GUIConfig.class.getName());
        textHelpEvents();
    }
}
