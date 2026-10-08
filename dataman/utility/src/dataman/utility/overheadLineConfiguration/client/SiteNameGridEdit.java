package dataman.utility.overheadLineConfiguration.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.utility.overheadLineConfiguration.shared.OverheadLineConfiguration_Srv;
import dataman.utility.overheadLineConfiguration.shared.OverheadLineConfiguration_SrvAsync;
import java.util.List;

public class SiteNameGridEdit<T1, T2> extends SiteNameGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(KeyFieldExtended keyField);

        public void onDelete();
    }

    ServiceInvoker<OverheadLineConfiguration_SrvAsync> SrvAsync;
    CallBack callBack;
    int hashCode;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void textHelpEvent() {
        txtHSiteName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSite(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSiteName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(DmShared.xNull(result.getErrorMessage())), txtHSiteName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
        txtHSiteName.setFocus(true);
    }

    @Override
    public void displayRow(T1 row) {
        KeyFieldExtended ourRow = (KeyFieldExtended) row;
        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtHSiteName.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHSiteName.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));

    }

    @Override
    public T1 getNewRow() {
        return (T1) new KeyFieldExtended();
    }

    @Override
    public void onOk() {
        if (DmShared.xNull(txtHSiteName.getTag()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Site is mandatory.", txtHSiteName).show();
            return;
        }
        for (KeyFieldExtended keyNode : (List<KeyFieldExtended>) getDmGrid().getGrid().getStore().getAll()) {
            if (hashCode != keyNode.getSharedKeyFieldHashCode()) {
                if (DmShared.xNull(txtHSiteName.getTag()).equalsIgnoreCase(DmShared.xNull(keyNode.getSharedKeyFieldCode()))) {
                    new DmAlertMessageBox("Site is already exists.", txtHSiteName).show();
                    return;
                }
            }
        }
        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        KeyFieldExtended ourRow = (KeyFieldExtended) row;
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHSiteName.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHSiteName.getText()));
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

    public SiteNameGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Applicable To Site", "400px", "65px");
        SrvAsync = new ServiceInvoker<OverheadLineConfiguration_SrvAsync>(GWT.create(OverheadLineConfiguration_Srv.class), OverheadLineConfiguration.class.getName());
        textHelpEvent();
    }
}
