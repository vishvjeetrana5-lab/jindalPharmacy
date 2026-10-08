package dataman.utility.fileTypeMaster.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.utility.fileTypeMaster.shared.FileTypeMaster_Srv;
import dataman.utility.fileTypeMaster.shared.GrdFileTypeMaster_Mdl;
import dataman.utility.fileTypeMaster.shared.FileTypeMaster_SrvAsync;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public class FileTypeMasterGridEdit<T1, T2> extends FileTypeMasterGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(GrdFileTypeMaster_Mdl fileMaster_Mdl);

        public void onDelete();
    }

    ServiceInvoker<FileTypeMaster_SrvAsync> SrvAsync;
    CallBack callBack;
    GrdFileTypeMaster_Mdl parentRow;
    int hashCode;

    private void textHelpEvents() {

        txtHDefault.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHDefault(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDefault).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDefault.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDefault).show();
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
        GrdFileTypeMaster_Mdl ourRow = (GrdFileTypeMaster_Mdl) row;

        parentRow = new GrdFileTypeMaster_Mdl();
        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtFileType.setText(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtMaxSize.setText(DmClient.vNull(ourRow.getSharedKeyFieldName(), "0"));
        txtFilter.setText(DmShared.xNull(ourRow.getFilter()));
        txtHDefault.setTag(DmShared.xNull(ourRow.getDefaultCode()));
        txtHDefault.setText(DmShared.xNull(ourRow.getDefaultName()));

        if (DmShared.xNull(ourRow.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
            onAdd();
        }

        txtFileType.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdFileTypeMaster_Mdl();
    }

    @Override
    public void onOk() {

        if (DmShared.xNull(txtFileType.getText().trim()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("File Type is mandatory.", txtFileType).show();
            return;
        }

        if (DmShared.xNull(txtHDefault.getTag()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Default is mandatory.", txtHDefault).show();
            return;
        }

        if (DmShared.vNull(txtMaxSize.getText()).doubleValue() == 0.00) {
            new DmAlertMessageBox("Max. Size is mandatory.", txtMaxSize).show();
            return;
        }

        if (DmShared.xNull(txtFilter.getText().trim()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Filter is mandatory.", txtFilter).show();
            return;
        }

        List<GrdFileTypeMaster_Mdl> lstTemp = (List<GrdFileTypeMaster_Mdl>) getDmGrid().getGrid().getStore().getAll();

        for (int i = 0; i < lstTemp.size(); i++) {
            GrdFileTypeMaster_Mdl get = lstTemp.get(i);
            if (DmShared.xNull(txtFileType.getText().trim()).equalsIgnoreCase(DmShared.xNull(get.getSharedKeyFieldCode()))) {
                if (DmShared.vNull(hashCode).doubleValue() != DmShared.vNull(get.getSharedKeyFieldHashCode()).doubleValue()) {
                    new DmAlertMessageBox(txtFileType.getText() + " already exists.", txtFileType).show();
                    return;
                }
            }
        }

        if (DmShared.vNull(txtHDefault.getTag()).doubleValue() == 1) {
            {
                for (int i = 0; i < lstTemp.size(); i++) {
                    GrdFileTypeMaster_Mdl get = lstTemp.get(i);
                    if (DmShared.vNull(get.getDefaultCode()).doubleValue() == 1) {
                        if (DmShared.vNull(hashCode).doubleValue() != DmShared.vNull(get.getSharedKeyFieldHashCode()).doubleValue()) {
                            new DmAlertMessageBox("There is only one Default.", txtHDefault).show();
                            return;
                        }
                    }
                }
            }
        }

        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdFileTypeMaster_Mdl ourRow = (GrdFileTypeMaster_Mdl) row;

        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtFileType.getText().trim()));
        ourRow.setSharedKeyFieldName(DmClient.vNull(txtMaxSize.getText(), ""));
        ourRow.setFilter(DmShared.xNull(txtFilter.getText().trim()));
        ourRow.setDefaultCode(DmShared.xNull(txtHDefault.getTag()));
        ourRow.setDefaultName(DmShared.xNull(txtHDefault.getText()));

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

    public FileTypeMasterGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "File Type Master", "500px", "100px");
        SrvAsync = new ServiceInvoker<>(GWT.create(FileTypeMaster_Srv.class), FileTypeMaster.class.getName());
        textHelpEvents();
    }

    private void onAdd() {
        txtHDefault.setTag("0");
        txtHDefault.setText("No");
    }

}
