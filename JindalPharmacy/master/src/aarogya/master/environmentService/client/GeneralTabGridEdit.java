package aarogya.master.environmentService.client;

import aarogya.master.environmentService.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import java.util.List;

public class GeneralTabGridEdit<T1, T2> extends GeneralTabGridEdit_Dsg<T1, T2> implements BlurHandler {

    ServiceInvoker<EnvironmentService_SrvAsync> SrvAsync;
    private CallBack callBack;
    private GrdServiceCategory_Mdl parentRow;
    private String siteCode;

    @Override
    public void onBlur(BlurEvent event) {

    }

    interface CallBack {

        public void onOk(GrdServiceCategory_Mdl grdTdsEntry);

        public void onDelete();
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, String siteCode) {
        this.siteCode = siteCode;
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtHDefConsultant.setFocus(true);

            }
        });
    }

    private void textHelpEvents() {

        txtHDefConsultant.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHConsultant(siteCode, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDefConsultant).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDefConsultant.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDefConsultant).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHDefDepartment.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHDepartment(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDefDepartment).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDefDepartment.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDefDepartment).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHLinkedSeriveAC.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getServices(helper, blnToEndOfFile, null, "", new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHLinkedSeriveAC).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHLinkedSeriveAC.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHLinkedSeriveAC).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
    }

    @Override
    public void onOk() {

        proceedOk();
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
    public void manageOk(T1 row) {
        GrdServiceCategory_Mdl ourRow = (GrdServiceCategory_Mdl) row;

        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtServiceCategory.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtServiceCategory.getText()));

        ourRow.setDefConsultantTag(DmShared.xNull(txtHDefConsultant.getTag()));
        ourRow.setDefConsultantText(DmShared.xNull(txtHDefConsultant.getText()));

        ourRow.setDefDepartmentTag(DmShared.xNull(txtHDefDepartment.getTag()));
        ourRow.setDefDepartmentText(DmShared.xNull(txtHDefDepartment.getText()));

        ourRow.setLinkedServiceTag(DmShared.xNull(txtHLinkedSeriveAC.getTag()));
        ourRow.setLinkedServiceText(DmShared.xNull(txtHLinkedSeriveAC.getText()));

        callBack.onOk(ourRow);
    }

    @Override
    public void manageDelete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdServiceCategory_Mdl();
    }

    @Override
    public void displayRow(T1 row) {
        GrdServiceCategory_Mdl ourRow = (GrdServiceCategory_Mdl) row;
        parentRow = new GrdServiceCategory_Mdl();
        txtServiceCategory.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtServiceCategory.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtHDefConsultant.setTag(DmShared.xNull(ourRow.getDefConsultantTag()));
        txtHDefConsultant.setText(DmShared.xNull(ourRow.getDefConsultantText()));
        txtHDefDepartment.setTag(DmShared.xNull(ourRow.getDefDepartmentTag()));
        txtHDefDepartment.setText(DmShared.xNull(ourRow.getDefDepartmentText()));

        txtHLinkedSeriveAC.setTag(DmShared.xNull(ourRow.getLinkedServiceTag()));
        txtHLinkedSeriveAC.setText(DmShared.xNull(ourRow.getLinkedServiceText()));

    }

    public GeneralTabGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Details", "470px", "140px");
        SrvAsync = new ServiceInvoker<>(GWT.create(EnvironmentService_Srv.class), EnvironmentService.class.getName());
        textHelpEvents();
        manageDisplay();

    }

    private void manageDisplay() {
        txtServiceCategory.setEnabled(false);
    }
}
