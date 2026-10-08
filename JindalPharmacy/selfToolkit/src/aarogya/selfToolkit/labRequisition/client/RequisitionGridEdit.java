package aarogya.selfToolkit.labRequisition.client;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import aarogya.selfToolkit.labRequisition.shared.*;
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
import com.google.gwt.core.client.Scheduler;

public class RequisitionGridEdit<T1, T2> extends RequisitionGridEdit_Dsg<T1, T2> implements BlurHandler {

    interface CallBack {

        public void onOk(GrdLabRequisition_Mdl details_Mdl);

        public void onDelete();
    }

    private final ServiceInvoker<LabRequisition_SrvAsync> SrvAsync;
    private CallBack callBack;
    private GrdLabRequisition_Mdl parentRow;
    private int hashCode;
    private LabRequisition labRequisition;

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtReqDate) {
            txtReqDate.setText(DmClient.returnDate(txtReqDate.getText()));
        }
    }

    private void textHelpEvents() {
        txtHTest.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHTest_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHTest(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHTest_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHTest).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHTest_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHTest.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHTest).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHTest_Mdl t1Row) {
                if (blnStatus && t1Row != null) {
                    txtHToDepartment.setTag(DmShared.xNull(t1Row.getDefaultDepartmentCode()));
                    txtHToDepartment.setText(DmShared.xNull(t1Row.getDefaultDepartmentName()));
                    if (!DmShared.xNull(t1Row.getSiteCode()).equalsIgnoreCase("")) {
                        txtHSite.setTag(DmShared.xNull(t1Row.getSiteCode()));
                        txtHSite.setText(DmShared.xNull(t1Row.getSiteName()));
                    } else {
                        txtHSite.setTag(DmShared.xNull(labRequisition.dvm.getDefaultSiteCode()));
                        txtHSite.setText(DmShared.xNull(labRequisition.dvm.getDefaultSiteName()));
                    }
                } else {
                    txtHToDepartment.setTag("");
                    txtHToDepartment.setText("");
                    txtHSite.setTag("");
                    txtHSite.setText("");
                }
            }
        });
        txtHToDepartment.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getDepartment(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHToDepartment).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHToDepartment.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHToDepartment).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (blnStatus && t1Row != null) {
                    txtHSite.setTag(DmShared.xNull(t1Row.getSharedModifiedBy()));
                    txtHSite.setText(DmShared.xNull(t1Row.getSharedPreparedBy()));
                } else {
                    txtHSite.setTag("");
                    txtHSite.setText("");
                }
                if (DmShared.iNull(txtHSite.getTag()).longValue() == 0) {
                    txtHSite.setTag(DmShared.xNull(labRequisition.dvm.getDefaultSiteCode()));
                    txtHSite.setText(DmShared.xNull(labRequisition.dvm.getDefaultSiteName()));
                }
            }
        });
        txtHSite.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSiteTo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSite).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSite.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSite).show();
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

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, LabRequisition labRequisition) {
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
        this.labRequisition = labRequisition;

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtHTest.setFocus(true);
            }
        });
    }

    @Override
    public void displayRow(T1 row) {
        GrdLabRequisition_Mdl ourRow = (GrdLabRequisition_Mdl) row;
        parentRow = new GrdLabRequisition_Mdl();
        parentRow = ourRow;

        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtHTest.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHTest.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtHToDepartment.setTag(DmShared.xNull(ourRow.getDefaultDepartmentCode()));
        txtHToDepartment.setText(DmShared.xNull(ourRow.getDefaultDepartmentName()));
        txtHSite.setTag(DmShared.xNull(ourRow.getSiteCode()));
        txtHSite.setText(DmShared.xNull(ourRow.getSiteName()));
        txtRemark.setText(DmShared.xNull(ourRow.getRemark()));

        if (DmShared.dNull(ourRow.getReqDate()).equalsIgnoreCase("")) {
            txtReqDate.setText(labRequisition.currentDT[0]);
            txtReqTime.setText(labRequisition.currentDT[1]);
        } else {
            txtReqDate.setText(ourRow.getReqDate());
            txtReqTime.setText(ourRow.getReqTime());
        }

        txtHTest.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdLabRequisition_Mdl();
    }

    @Override
    public void onOk() {
        if (DmShared.xNull(txtHTest.getTag()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Investigation is mandatory.", txtHTest).show();
            return;
        }
        if (DmShared.xNull(txtHToDepartment.getTag()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Department To is mandatory.", txtHTest).show();
            return;
        }
        if (DmShared.xNull(txtHSite.getTag()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Site To is mandatory.", txtHSite).show();
            return;
        }
        if (DmShared.xNull(txtReqDate.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Req. Date is mandatory.", txtReqDate).show();
            return;
        }

        if (DmShared.xNull(txtReqTime.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Req. Time is mandatory.", txtReqTime).show();
            return;
        }

        if ((!txtReqDate.getText().trim().equalsIgnoreCase(""))) {
            if ((DmShared.convertDate(txtReqDate.getText()).getTime() < (DmShared.convertDate(DmClient.getLoginDate())).getTime())) {
                new DmAlertMessageBox("Required Date should be greater than or equal to Current Date.", txtReqDate).show();
                return;
            }
        }
        if (!DmShared.xNull(labRequisition.admitDate).equalsIgnoreCase("")) {
            if (DmShared.convertDate(txtReqDate.getText()).getTime() < DmShared.convertDate(labRequisition.admitDate).getTime()) {
                new DmAlertMessageBox("Req. Date can't be less than Admit Date ", txtReqDate).show();
                return;
            }
        }
        if ((!txtReqDate.getText().trim().equalsIgnoreCase(""))) {
            if ((DmShared.convertDate(txtReqDate.getText()).getTime() == (DmShared.convertDate(DmClient.getLoginDate())).getTime())) {
                if (DmShared.iNull(txtReqTime.getText()).doubleValue() < DmShared.iNull(labRequisition.currentDT[1]).doubleValue()) {
                    new DmAlertMessageBox("Req. Time should be greater than or equal to Current Time.", txtReqTime).show();
                    return;
                }
            }
        }

        List<GrdLabRequisition_Mdl> lstTemp = (List<GrdLabRequisition_Mdl>) getDmGrid().getGrid().getStore().getAll();
        for (int i = 0; i < lstTemp.size(); i++) {
            GrdLabRequisition_Mdl get = lstTemp.get(i);
            if (DmShared.xNull(txtHTest.getTag()).equalsIgnoreCase(DmShared.xNull(get.getSharedKeyFieldCode()))) {
                if (DmShared.vNull(hashCode).doubleValue() != DmShared.vNull(get.getSharedKeyFieldHashCode()).doubleValue()) {
                    new DmAlertMessageBox(txtHTest.getText() + " already exists.", txtHTest).show();
                    return;
                }
            }
        }

        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdLabRequisition_Mdl ourRow = (GrdLabRequisition_Mdl) row;

        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHTest.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHTest.getText()));
        ourRow.setReqDate(DmShared.xNull(txtReqDate.getText()));
        ourRow.setReqTime(DmShared.xNull(txtReqTime.getText()));
        ourRow.setRemark(DmShared.xNull(txtRemark.getText()));
        ourRow.setDefaultDepartmentCode(DmShared.xNull(txtHToDepartment.getTag()));
        ourRow.setSiteCode(DmShared.xNull(txtHSite.getTag()));
        ourRow.setSiteName(DmShared.xNull(txtHSite.getText()));

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

    public RequisitionGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Lab Requisition Detail", "400px", "150px");
        SrvAsync = new ServiceInvoker<LabRequisition_SrvAsync>(GWT.create(LabRequisition_Srv.class), LabRequisition.class.getName());
        textHelpEvents();
        manageEvent();
    }

    private void manageEvent() {
        txtReqDate.addBlurHandler(this);
    }

}
