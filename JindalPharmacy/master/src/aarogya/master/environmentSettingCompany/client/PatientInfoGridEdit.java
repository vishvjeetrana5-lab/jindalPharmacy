package aarogya.master.environmentSettingCompany.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import aarogya.master.environmentSettingCompany.shared.EnvironmentSettingCompany_Srv;
import aarogya.master.environmentSettingCompany.shared.EnvironmentSettingCompany_SrvAsync;
import aarogya.master.environmentSettingCompany.shared.GrdPatientInfo_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public class PatientInfoGridEdit<T1, T2> extends PatientInfoGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(GrdPatientInfo_Mdl opdService);

        public void onDelete();
    }

    ServiceInvoker<EnvironmentSettingCompany_SrvAsync> SrvAsync;
    CallBack callBack;
    GrdPatientInfo_Mdl parentRow;
    int hashCode = 0;

    private void helpTextEvents() {

        txtHIsMandatory.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHYesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIsMandatory).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHIsMandatory.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIsMandatory).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHIsVisible.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHYesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIsVisible).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHIsVisible.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIsVisible).show();
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
        GrdPatientInfo_Mdl ourRow = (GrdPatientInfo_Mdl) row;
        parentRow = new GrdPatientInfo_Mdl();

        txtName.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtName.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));

        txtHIsMandatory.setTag(DmShared.xNull(ourRow.getIsMandatoryTag()));
        txtHIsMandatory.setText(DmShared.xNull(ourRow.getIsMandatoryText()));

        txtHIsVisible.setTag(DmShared.xNull(ourRow.getIsVisibleTag()));
        txtHIsVisible.setText(DmShared.xNull(ourRow.getIsVisibleText()));

        txtSrNo.setText(DmShared.xNull(ourRow.getSrNo()));

        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtSrNo.setFocus(true);
        txtName.setEnabled(false);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdPatientInfo_Mdl();
    }

    @Override
    public void onOk() {

        if (DmShared.xNull(txtName.getTag()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Name is mandatory.", txtName).show();
            return;
        }
        if (DmShared.xNull(txtSrNo.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Sr. No. is mandatory.", txtSrNo).show();
            return;
        }

        if (DmShared.iNull(txtHIsVisible.getTag()).toString().equalsIgnoreCase("0")) {
            if (DmShared.iNull(txtHIsMandatory.getTag()).toString().equalsIgnoreCase("1")) {
                new DmAlertMessageBox("Mandatory is not possible in case of not visible. ", txtSrNo).show();
                return;
            }

        }

        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdPatientInfo_Mdl ourRow = (GrdPatientInfo_Mdl) row;

        ourRow.setSharedKeyFieldName(DmShared.xNull(txtName.getText()));
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtName.getTag()));

        ourRow.setIsMandatoryTag(DmShared.iNull(txtHIsMandatory.getTag()).toString());
        ourRow.setIsMandatoryText(DmShared.xNull(txtHIsMandatory.getText()));

        ourRow.setIsVisibleTag(DmShared.iNull(txtHIsVisible.getTag()).toString());
        ourRow.setIsVisibleText(DmShared.xNull(txtHIsVisible.getText()));

        ourRow.setSrNo(DmShared.iNull(txtSrNo.getText()).toString());

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

    public PatientInfoGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Patient Info", "280px", "120px");
        SrvAsync = new ServiceInvoker<EnvironmentSettingCompany_SrvAsync>(GWT.create(EnvironmentSettingCompany_Srv.class), EnvironmentSettingCompany.class.getName());
        helpTextEvents();
    }

}
