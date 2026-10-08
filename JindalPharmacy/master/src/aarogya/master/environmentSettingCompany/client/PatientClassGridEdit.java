package aarogya.master.environmentSettingCompany.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import aarogya.master.environmentSettingCompany.shared.EnvironmentSettingCompany_Srv;
import aarogya.master.environmentSettingCompany.shared.EnvironmentSettingCompany_SrvAsync;
import aarogya.master.environmentSettingCompany.shared.GrdPatientClass_Mdl;
import aarogya.master.environmentSettingCompany.shared.GrdPatientInfo_Mdl;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public class PatientClassGridEdit<T1, T2> extends PatientClassGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(GrdPatientClass_Mdl opdService);

        public void onDelete();
    }

    ServiceInvoker<EnvironmentSettingCompany_SrvAsync> SrvAsync;
    CallBack callBack;
    GrdPatientInfo_Mdl parentRow;
    int hashCode = 0;

    private void helpTextEvents() {

        txtHRateList.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHRateList(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHRateList).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHRateList.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHRateList).show();
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
        GrdPatientClass_Mdl ourRow = (GrdPatientClass_Mdl) row;
        parentRow = new GrdPatientInfo_Mdl();
        txtName.setText(DmShared.xNull(ourRow.getPatientClassName()));
        txtName.setTag(DmShared.xNull(ourRow.getPatientClassCode()));
        txtHRateList.setTag(DmShared.xNull(ourRow.getRateListCode()));
        txtHRateList.setText(DmShared.xNull(ourRow.getRateListName()));
        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtHRateList.setFocus(true);
        txtName.setEnabled(false);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdPatientInfo_Mdl();
    }

    @Override
    public void onOk() {

//        if (DmShared.xNull(txtName.getTag()).equalsIgnoreCase("")) {
//            new DmAlertMessageBox("Name is mandatory.", txtName).show();
//            return;
//        }
//        if (DmShared.xNull(txtSrNo.getText()).equalsIgnoreCase("")) {
//            new DmAlertMessageBox("Sr. No. is mandatory.", txtSrNo).show();
//            return;
//        }
//
//        if (DmShared.iNull(txtHIsVisible.getTag()).toString().equalsIgnoreCase("0")) {
//            if (DmShared.iNull(txtHIsMandatory.getTag()).toString().equalsIgnoreCase("1")) {
//                new DmAlertMessageBox("Mandatory is not possible in case of not visible. ", txtSrNo).show();
//                return;
//            }
//
//        }
        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdPatientClass_Mdl ourRow = (GrdPatientClass_Mdl) row;

        ourRow.setPatientClassName(DmShared.xNull(txtName.getText()));
        ourRow.setPatientClassCode(DmShared.xNull(txtName.getTag()));
        ourRow.setRateListCode(DmShared.iNull(txtHRateList.getTag()).toString());
        ourRow.setRateListName(DmShared.xNull(txtHRateList.getText()));
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

    public PatientClassGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Patient Class", "280px", "50px");
        SrvAsync = new ServiceInvoker<EnvironmentSettingCompany_SrvAsync>(GWT.create(EnvironmentSettingCompany_Srv.class), EnvironmentSettingCompany.class.getName());
        helpTextEvents();
    }

}
