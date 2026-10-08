package aarogya.master.environmentSettingCompany.client;

import aarogya.master.environmentSettingCompany.shared.EnvironmentSettingCompany_Srv;
import aarogya.master.environmentSettingCompany.shared.EnvironmentSettingCompany_SrvAsync;
import aarogya.master.environmentSettingCompany.shared.GrdPriority_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.core.client.GWT;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.List;

public class PriorityGridEdit<T1, T2> extends PriorityGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(GrdPriority_Mdl priority);

        public void onDelete();
    }

    ServiceInvoker<EnvironmentSettingCompany_SrvAsync> SrvAsync;
    CallBack callBack;
    GrdPriority_Mdl parentRow;
    int hashCode = 0;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {

        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
    }

    @Override
    public void displayRow(T1 row) {
        GrdPriority_Mdl ourRow = (GrdPriority_Mdl) row;
        parentRow = new GrdPriority_Mdl();

        txtPriority.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtDescriprion.setText(DmShared.xNull(ourRow.getDescription()));
        txtSorting.setText(DmShared.xNull(ourRow.getSorting()));
        txtColor.setText(DmShared.xNull(ourRow.getColor()));

        txtSorting.setFocus(true);
        txtPriority.setEnabled(false);
        txtDescriprion.setEnabled(false);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdPriority_Mdl();
    }

    @Override
    public void onOk() {

        for (GrdPriority_Mdl prior : (List<GrdPriority_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
            if (DmShared.xNull(prior.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(txtSorting.getTag()))) {
                if (!DmClient.vNull(prior.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                    new DmAlertMessageBox("order Index already exists.", txtSorting).show();
                    return;
                }
            }
        }

        if (DmShared.xNull(txtSorting.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Sorting is mandatory", txtSorting).show();
            return;
        }

        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdPriority_Mdl ourRow = (GrdPriority_Mdl) row;

        ourRow.setSharedKeyFieldName(DmShared.xNull(txtPriority.getText()));
        ourRow.setDescription(DmShared.xNull(txtDescriprion.getText()));
        ourRow.setSorting(DmShared.iNull(txtSorting.getText()).toString());
        ourRow.setColor(DmShared.xNull(txtColor.getText()));

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

    public PriorityGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Priority", "380px", "120px");
        SrvAsync = new ServiceInvoker<EnvironmentSettingCompany_SrvAsync>(GWT.create(EnvironmentSettingCompany_Srv.class), EnvironmentSettingCompany.class.getName());

    }

}
