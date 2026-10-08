package dataman.payroll.employeeMaster.client;

import dataman.payroll.employeeMaster.shared.EmployeeMaster_Srv;
import dataman.payroll.employeeMaster.shared.EmployeeMaster_SrvAsync;
import dataman.payroll.employeeMaster.shared.GrdSoftId_Mdl;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;

public class SoftIdGridEdit<T1, T2> extends SoftIdGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(GrdSoftId_Mdl master_Mdl);

        public void onDelete();

    }

    ServiceInvoker<EmployeeMaster_SrvAsync> SrvAsync;
    CallBack callBack;
    private int hashCode = 0;
    private EmployeeMaster empParent;
    

    private void textHelpEvents() {

    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, EmployeeMaster empMast) {
        this.empParent = empMast;
        int cellIndex = 1;
        cellIndex = getDmGrid().getSelectedCell().getCell();
        super.showRelativeTo(currentRowIndex, cellIndex, okNeeded, false, false);
    }

    @Override
    public void displayRow(T1 row) {
        GrdSoftId_Mdl ourRow = (GrdSoftId_Mdl) row;
        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtName.setTag(DmShared.xNull(empParent.topControl.getDocId()));
        txtName.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        
        

//        txtFromDate.setEnabled(false);
//        txtToDate.setEnabled(false);
//        txtLeave.setEnabled(false);
//        txtLeaves.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdSoftId_Mdl();
    }

    @Override
    public void onOk() {

        if (DmShared.xNull(empParent.txtName.getText()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Employee Master is mandatory.", txtName).show();
            return;
        }
//        if (DmShared.xNull(txtHFreshDirty.getTag()).trim().equalsIgnoreCase("")) {
//            new DmAlertMessageBox("Fresh / Dirty is mandatory.", txtHFreshDirty).show();
//            return;
//        }
//        if (DmShared.vNull(txtQuantity.getText()).doubleValue() == 0) {
//            new DmAlertMessageBox("Quantity is mandatory.", txtQuantity).show();
//            return;
//        }
//        for (GrdConsumable_Mdl consumable_Mdl : (List<GrdConsumable_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
//            if (DmShared.xNull(consumable_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(txtHItemName.getTag()))) {
//                if (!DmClient.vNull(consumable_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
//                    new DmAlertMessageBox(DmShared.xNull(txtHItemName.getText()) + " is already exists.", txtHItemName).show();
//                    return;
//                }
//            }
//        }
        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdSoftId_Mdl ourRow = (GrdSoftId_Mdl) row;
        
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtName.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtName.getText()));
        

        updateRecord(ourRow);
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

    public SoftIdGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Soft Detail", "400px", "70px");
        SrvAsync = new ServiceInvoker<EmployeeMaster_SrvAsync>(GWT.create(EmployeeMaster_Srv.class), EmployeeMaster.class.getName());
        textHelpEvents();
    }
    
    private GrdSoftId_Mdl saveInitialize(GrdSoftId_Mdl leave_Mdl) {

        GrdSoftId_Mdl mrTemp = new GrdSoftId_Mdl();

        mrTemp.setSharedKeyFieldCode(DmShared.xNull(txtName.getTag()));
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtName.getText()));
        mrTemp.setsNo(DmShared.xNull(leave_Mdl.getsNo()));
        
        return mrTemp;
    }
    
    private void updateRecord(GrdSoftId_Mdl SoftId_Mdl) {
        GrdSoftId_Mdl mrTemp;
        try {
                mrTemp = saveInitialize(SoftId_Mdl);
                    SrvAsync.invokeRPC().updateSoftId(mrTemp, DmShared.xNull(empParent.topControl.getDocId()), new AsyncCallback<DmResult<GrdSoftId_Mdl>>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                        }

                        @Override
                        public void onSuccess(DmResult<GrdSoftId_Mdl> result) {
                            if (result.isStatus()) {
                                onRefresh();
                            } else {
                                new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                            }
                        }
                    });
//                }
//            }

        } catch (DmException e) {
            new DmAlertMessageBox(e.getErrorSource(), (DmTextBox) null).show();
        }
    }
    
    private void onRefresh() {
        
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtName.setFocus(true);
            }
        });
    }

}
