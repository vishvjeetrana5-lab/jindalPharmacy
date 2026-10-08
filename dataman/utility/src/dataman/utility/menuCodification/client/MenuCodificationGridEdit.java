package dataman.utility.menuCodification.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.utility.menuCodification.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public class MenuCodificationGridEdit<T1, T2> extends MenuCodificationGridEdit_Dsg<T1, T2> implements KeyPressHandler {

    interface CallBack {

        public void onOk(GrdDescription_Mdl grdDdesc);

        public void onDelete();
    }

    private final ServiceInvoker<MenuCodification_SrvAsync> SrvAsync;
    private CallBack callBack;
    private GrdDescription_Mdl parentRow;
    private int hashCode = 0;
    private String moduleName = "";

    public MenuCodificationGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Menu Name", "330px", "76px");
        SrvAsync = new ServiceInvoker<MenuCodification_SrvAsync>(GWT.create(MenuCodification_Srv.class), MenuCodification.class.getName());
        txtMenuCode.addKeyPressHandler(this);
        textHelpEvents();

    }

    private void textHelpEvents() {
        txtHMenuName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHMenuName_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHMenuName(DmShared.xNull(txtModuleName.getText()), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHMenuName_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMenuName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHMenuName_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHMenuName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHMenuName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHMenuName_Mdl tRow) {
                txtHMenuName.setText(DmShared.xNull(tRow.getSharedKeyFieldName()));
                txtHMenuName.setTag(DmShared.xNull(tRow.getMenuName()));
                txtModuleName.setText(DmShared.xNull(tRow.getModuleName()));
            }
        });
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, String mnuModule) {
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
        txtHMenuName.setFocus(true);
        if (!DmShared.xNull(mnuModule).equalsIgnoreCase("")) {
            moduleName = mnuModule;
        }
        txtModuleName.setText(DmShared.xNull(moduleName));
    }

    @Override
    public void displayRow(T1 row) {
        GrdDescription_Mdl ourRow = (GrdDescription_Mdl) row;
        parentRow = new GrdDescription_Mdl();

        txtHMenuName.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtHMenuName.setTag(DmShared.xNull(ourRow.getMenuName()));
        if (DmShared.xNull(ourRow.getModuleName()).equalsIgnoreCase("")) {
            txtModuleName.setText(DmShared.xNull(moduleName));
        } else {
            txtModuleName.setText(DmShared.xNull(ourRow.getModuleName()));
        }
        txtMenuCode.setText(DmShared.xNull(ourRow.getMenuCode()));

        hashCode = ourRow.getSharedKeyFieldHashCode();
        parentRow = ourRow;
        txtHMenuName.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdDescription_Mdl();
    }

    @Override
    public void onOk() {
        if (DmShared.xNull(txtHMenuName.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Menu Name is mandatory.", txtHMenuName).show();
            return;
        }
        if (DmShared.xNull(txtMenuCode.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Menu Code is mandatory.", txtMenuCode).show();
            return;
        }
        for (GrdDescription_Mdl edu : (List<GrdDescription_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
            if (DmShared.xNull(edu.getMenuCode()).equalsIgnoreCase(DmShared.xNull(txtMenuCode.getText()))) {
                if (!DmClient.vNull(edu.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                    new DmAlertMessageBox("Menu Code already exists.", txtMenuCode).show();
                    return;
                }
            }
        }
        for (GrdDescription_Mdl edu : (List<GrdDescription_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
            if (DmShared.xNull(edu.getSharedKeyFieldName()).equalsIgnoreCase(DmShared.xNull(txtHMenuName.getText()))) {
                if (!DmClient.vNull(edu.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                    new DmAlertMessageBox("Menu Name already exists.", txtHMenuName).show();
                    return;
                }
            }
        }
        topControlSave();
    }

    @Override
    public void onKeyPress(KeyPressEvent event) {
        int charCode = event.getCharCode();
//        String value;

        if ((charCode > 31) && (charCode < 48)) {
            txtMenuCode.cancelKey();
        } else if (charCode > 57 && charCode < 65) {
            txtMenuCode.cancelKey();
        } else if (charCode > 90 && charCode < 97) {
            txtMenuCode.cancelKey();
        } else if (charCode > 121) {
            txtMenuCode.cancelKey();
        }

    }

    private GrdDescription_Mdl topControlSaveInitialize() {
        GrdDescription_Mdl mrTemp = new GrdDescription_Mdl();

        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtHMenuName.getText()));
        mrTemp.setMenuName(DmShared.xNull(txtHMenuName.getTag()));
        mrTemp.setModuleName(DmShared.xNull(txtModuleName.getText()));
        mrTemp.setMenuCode(DmShared.xNull(txtMenuCode.getText()));
        return mrTemp;

    }

    private void topControlSave() {
        GrdDescription_Mdl mrTemp;
        try {
            mrTemp = topControlSaveInitialize();
            MenuCodification_Vld.chkSave(mrTemp);

            SrvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        proceedOk();
                    } else {
                        topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void topControlSaveError(int errorNo, String errorMessage) {

        switch (errorNo) {
            case 0:
                new DmAlertMessageBox(errorMessage, txtMenuCode).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtMenuCode).show();
        }

    }

    @Override
    public void manageOk(T1 row) {
        GrdDescription_Mdl ourRow = (GrdDescription_Mdl) row;

        ourRow.setMenuName(DmShared.xNull(txtHMenuName.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHMenuName.getText()));
        ourRow.setMenuCode(DmShared.xNull(txtMenuCode.getText()));
        ourRow.setModuleName(DmShared.xNull(txtModuleName.getText()));
        callBack.onOk(ourRow);
    }

    @Override
    public void onNew() {
        proceedNew();
    }

    @Override
    public void onDelete() {
        topControlDelete(parentRow);
    }

    private void topControlDelete(GrdDescription_Mdl mrTemp) {
        SrvAsync.invokeRPC().deleteRecord(mrTemp, new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    proceedDelete();
                } else {
                    new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
                }
            }
        });
    }

    @Override
    public void onClose() {
        hide();
    }

    @Override
    public void manageDelete() {
        callBack.onDelete();
    }
}
