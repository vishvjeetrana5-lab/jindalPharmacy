package aarogya.pharmacy.medicineMaster.client;

import aarogya.pharmacy.medicineMaster.shared.GrdMedicineDepartmentInfo_Mdl;
import aarogya.pharmacy.medicineMaster.shared.MedicineMaster_Srv;
import aarogya.pharmacy.medicineMaster.shared.MedicineMaster_SrvAsync;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public class MedicineDetailsGridEdit<T1, T2> extends MedicineDetailsGridEdit_Dsg<T1, T2> implements BlurHandler {

    interface CallBack {

        public void onOk(GrdMedicineDepartmentInfo_Mdl gridItem_Mdl);

        public void onDelete();
    }

    private ServiceInvoker<MedicineMaster_SrvAsync> SrvAsync;
    private CallBack callBack;
    private int hashCode = 0;

    private void textHelpEvents() {
        txtHDepartment.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHDepartment(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDepartment).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDepartment.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDepartment).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHSiteName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSite(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSiteName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSiteName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSiteName).show();
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
        GrdMedicineDepartmentInfo_Mdl ourRow = (GrdMedicineDepartmentInfo_Mdl) row;

        txtMinLvl.setText(DmClient.vNull(ourRow.getMinStock(), "0"));
        txtMaxLvl.setText(DmClient.vNull(ourRow.getMaxStock(), "0"));
        txtReOrder.setText(DmClient.vNull(ourRow.getReOrder(), "0"));
        txtBin.setText(DmShared.xNull(ourRow.getBinName()));
        txtHDepartment.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtHDepartment.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHSiteName.setText(DmShared.xNull(ourRow.getSiteName()));
        txtHSiteName.setTag(DmShared.xNull(ourRow.getSiteCode()));
        hashCode = ourRow.getSharedKeyFieldHashCode();

        txtHDepartment.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdMedicineDepartmentInfo_Mdl();
    }

    @Override
    public void onOk() {

        try {
            if (DmShared.xNull(txtHDepartment.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Department is mandatory.", txtHDepartment).show();
                return;
            }
            if (DmShared.xNull(txtHDepartment.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Site is mandatory.", txtHSiteName).show();
                return;
            }

            if (DmShared.iNull(txtMaxLvl.getText()).longValue() < DmShared.iNull(txtMinLvl.getText()).longValue()) {
                new DmAlertMessageBox("Min Level value must be less than or equal to Max Level value.", txtMinLvl).show();
                return;
            }

//            for (GrdMedicineDepartmentInfo_Mdl detail_Mdl : (List<GrdMedicineDepartmentInfo_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
//                if (DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(txtHDepartment.getTag()))) {
//                    if (!DmClient.vNull(detail_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
//                        new DmAlertMessageBox(DmShared.xNull(txtHDepartment.getText()) + " already exists.", txtHDepartment).show();
//                        return;
//                    }
//                }
//            }
            for (GrdMedicineDepartmentInfo_Mdl config_Mdl : (List<GrdMedicineDepartmentInfo_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
                if (DmShared.xNull(txtHDepartment.getTag()).equalsIgnoreCase(DmShared.xNull(config_Mdl.getSharedKeyFieldCode()))
                        && DmShared.xNull(txtHSiteName.getTag()).equalsIgnoreCase(DmShared.xNull(config_Mdl.getSiteCode()))) {
                    if (DmShared.vNull(hashCode).doubleValue() != DmShared.vNull(config_Mdl.getSharedKeyFieldHashCode()).doubleValue()) {
                        new DmAlertMessageBox("Combination already exist.", txtHDepartment).show();
                        return;
                    }
                }
            }
            proceedOk();

        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtHDepartment).show();
        }
    }

    @Override
    public void manageOk(T1 row) {
        GrdMedicineDepartmentInfo_Mdl ourRow = (GrdMedicineDepartmentInfo_Mdl) row;

        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHDepartment.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHDepartment.getText()));
        ourRow.setSiteCode(DmShared.xNull(txtHSiteName.getTag()));
        ourRow.setSiteName(DmShared.xNull(txtHSiteName.getText()));
        ourRow.setMinStock(DmClient.vNull(txtMinLvl.getText(), "0"));
        ourRow.setMaxStock(DmClient.vNull(txtMaxLvl.getText(), "0"));
        ourRow.setReOrder(DmClient.vNull(txtReOrder.getText(), "0"));
        ourRow.setBinName(DmShared.xNull(txtBin.getText()));

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

    public MedicineDetailsGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Grid Edit", "500px", "120px");
        SrvAsync = new ServiceInvoker<MedicineMaster_SrvAsync>(GWT.create(MedicineMaster_Srv.class), MedicineMaster.class.getName());
        textHelpEvents();

    }

    @Override
    public void onBlur(BlurEvent event) {

    }

}
