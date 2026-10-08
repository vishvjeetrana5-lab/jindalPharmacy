package aarogya.master.prescriptionGuiMapping.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import aarogya.master.prescriptionGuiMapping.shared.GridDescription_Mdl;
import aarogya.master.prescriptionGuiMapping.shared.PrescriptionGuiMapping_Srv;
import aarogya.master.prescriptionGuiMapping.shared.PrescriptionGuiMapping_SrvAsync;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public class PrescriptionGuiMappingGridEdit<T1, T2> extends PrescriptionGuiMappingGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(GridDescription_Mdl grdDdesc);

        public void onDelete();
    }

    private final ServiceInvoker<PrescriptionGuiMapping_SrvAsync> SrvAsync;
    private CallBack callBack;
    private GridDescription_Mdl parentRow;
    private int hashCode = 0;

    public PrescriptionGuiMappingGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "GUI Template Name", "280px", "120px");
        SrvAsync = new ServiceInvoker<PrescriptionGuiMapping_SrvAsync>(GWT.create(PrescriptionGuiMapping_Srv.class), PrescriptionGuiMapping.class.getName());
        textHelpEvents();
    }

    private void textHelpEvents() {
        txtHGUITempName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHGUITempName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHGUITempName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHGUITempName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHGUITempName).show();
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
        GridDescription_Mdl ourRow = (GridDescription_Mdl) row;
        parentRow = new GridDescription_Mdl();

        txtHGUITempName.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHGUITempName.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtHGUITempName.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GridDescription_Mdl();
    }

    @Override
    public void onOk() {
        if (DmShared.xNull(txtHGUITempName.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("GUI Template Name mandatory.", txtHGUITempName).show();
            return;
        }

        for (GridDescription_Mdl edu : (List<GridDescription_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
            if (DmShared.xNull(edu.getSharedKeyFieldName()).equalsIgnoreCase(DmShared.xNull(txtHGUITempName.getText()))) {
                if (!DmClient.vNull(edu.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                    new DmAlertMessageBox("Template Name already exists.", txtHGUITempName).show();
                    return;
                }
            }
        }

        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GridDescription_Mdl ourRow = (GridDescription_Mdl) row;

        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHGUITempName.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHGUITempName.getText()));
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

}
