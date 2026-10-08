package dataman.webJobs.emailTemplateMast.client;

import dataman.webJobs.emailTemplateMast.shared.*;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.List;

public class ImageDetailGridEdit<T1, T2> extends ImageDetailGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(GrdImage_Mdl opdService);

        public void onDelete();
    }

    ServiceInvoker<EmailTemplateMast_SrvAsync> SrvAsync;
    CallBack callBack;
    int hashCode = 0;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
        txtImageCode.setFocus(true);
    }

    @Override
    public void displayRow(T1 row) {
        GrdImage_Mdl ourRow = (GrdImage_Mdl) row;

        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtImageCode.setText(DmShared.xNull(ourRow.getImageCode()));
        txtRuleCode.setText(DmShared.xNull(ourRow.getRuleCode()));

        txtImageCode.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdImage_Mdl();
    }

    @Override
    public void onOk() {
        if (DmShared.xNull(txtImageCode.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Image Field is mandatory", txtImageCode).show();
            return;
        }
        if (DmShared.xNull(txtRuleCode.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Rule Code is mandatory", txtRuleCode).show();
            return;
        }

        for (GrdImage_Mdl image_Mdl : (List<GrdImage_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
            if (!DmShared.xNull(image_Mdl.getImageCode()).trim().equalsIgnoreCase("")) {
                if ((DmShared.xNull(image_Mdl.getImageCode()).trim().equalsIgnoreCase(DmShared.xNull(txtImageCode.getText()).trim())
                        && (DmShared.xNull(image_Mdl.getRuleCode()).trim().equalsIgnoreCase(DmShared.xNull(txtRuleCode.getText()).trim())))) {
                    if (!DmClient.vNull(image_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                        new DmAlertMessageBox("Image Field & Rule Code already exists.", txtImageCode).show();
                        return;
                    }
                }
            }
        }

        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdImage_Mdl ourRow = (GrdImage_Mdl) row;

        ourRow.setImageCode(DmShared.xNull(txtImageCode.getText()));
        ourRow.setRuleCode(DmShared.xNull(txtRuleCode.getText()));

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

    public ImageDetailGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Image Detail", "300px", "90px");
        SrvAsync = new ServiceInvoker<>(GWT.create(EmailTemplateMast_Srv.class), EmailTemplateMast.class.getName());
    }

}
