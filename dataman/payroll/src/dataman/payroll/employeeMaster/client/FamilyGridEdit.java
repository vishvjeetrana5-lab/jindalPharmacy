package dataman.payroll.employeeMaster.client;

import dataman.payroll.employeeMaster.shared.EmployeeMaster_Srv;
import dataman.payroll.employeeMaster.shared.EmployeeMaster_SrvAsync;
import dataman.payroll.employeeMaster.shared.GrdFamily_Mdl;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;

public class FamilyGridEdit<T1, T2> extends FamilyGridEdit_Dsg<T1, T2> implements BlurHandler{

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtDob)) {
            txtDob.setText(DmClient.returnDate(txtDob.getText()));

        }
    }

    interface CallBack {

        public void onOk(GrdFamily_Mdl master_Mdl);

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
        GrdFamily_Mdl ourRow = (GrdFamily_Mdl) row;
        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtName.setTag(DmShared.xNull(empParent.topControl.getDocId()));
        txtName.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtDob.setText(DmShared.dNull(ourRow.getDob()));
        txtAge.setText(DmShared.xNull(ourRow.getAge()));
        txtRelation.setText(DmShared.xNull(ourRow.getRelation()));
        

//        txtFromDate.setEnabled(false);
//        txtToDate.setEnabled(false);
//        txtLeave.setEnabled(false);
//        txtLeaves.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdFamily_Mdl();
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
        GrdFamily_Mdl ourRow = (GrdFamily_Mdl) row;
        
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtName.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtName.getText()));
        ourRow.setDob(DmShared.dNull(txtDob.getText()));
        ourRow.setAge(DmShared.xNull(txtAge.getText()));
        
        ourRow.setRelation(DmShared.xNull(txtRelation.getText()));

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

    public FamilyGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Family Detail", "400px", "125px");
        SrvAsync = new ServiceInvoker<EmployeeMaster_SrvAsync>(GWT.create(EmployeeMaster_Srv.class), EmployeeMaster.class.getName());
        textHelpEvents();
        txtDob.addBlurHandler(this);
    }
    
    private GrdFamily_Mdl saveInitialize(GrdFamily_Mdl leave_Mdl) {

        GrdFamily_Mdl mrTemp = new GrdFamily_Mdl();

        mrTemp.setSharedKeyFieldCode(DmShared.xNull(txtName.getTag()));
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtName.getText()));
        mrTemp.setDob(DmShared.dNull(txtDob.getText()));
        mrTemp.setAge(DmShared.xNull(txtAge.getText()));
        
        mrTemp.setRelation(DmShared.xNull(txtRelation.getText()));
        mrTemp.setsNo(DmShared.xNull(leave_Mdl.getsNo()));


        return mrTemp;
    }
    
    private void updateRecord(GrdFamily_Mdl family_Mdl) {
        GrdFamily_Mdl mrTemp;
        try {
                mrTemp = saveInitialize(family_Mdl);
                    SrvAsync.invokeRPC().updateFamily(mrTemp, DmShared.xNull(empParent.topControl.getDocId()), new AsyncCallback<DmResult<GrdFamily_Mdl>>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                        }

                        @Override
                        public void onSuccess(DmResult<GrdFamily_Mdl> result) {
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
    
    private void fillDob(String DOB) {
        if (!DmShared.xNull(DOB).trim().equalsIgnoreCase("")) {

//            STClient.Age age = STClient.getAge(DmShared.dNull(txtDob.), DOB, DmShared.iNull(0).intValue(), DmShared.iNull(0).intValue());
//            txtAge.setText(DmClient.vNull(age.getDay(), "00"));
//            txtAgeMonth.setText(DmClient.vNull(age.getMonth(), "00"));
//            txtAgeYear.setText(DmClient.vNull(age.getYear(), "00"));
        }
    }

}
