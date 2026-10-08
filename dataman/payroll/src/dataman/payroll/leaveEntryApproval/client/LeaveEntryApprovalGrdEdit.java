package dataman.payroll.leaveEntryApproval.client;


import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.payroll.leaveEntryApproval.shared.GrdLeaveEntryApproval_Mdl;
import dataman.payroll.leaveEntryApproval.shared.LeaveEntryApproval_Srv;
import dataman.payroll.leaveEntryApproval.shared.LeaveEntryApproval_SrvAsync;
import dataman.payroll.self.shared.PREnum;
import java.util.List;

public class LeaveEntryApprovalGrdEdit<T1, T2> extends LeaveEntryApprovalGrdEdit_Dsg<T1, T2> implements BlurHandler {

    interface CallBack {

        public void onOk(GrdLeaveEntryApproval_Mdl ourRow);
    }

    private final ServiceInvoker<LeaveEntryApproval_SrvAsync> SrvAsync;
    private CallBack callBack;
    private GrdLeaveEntryApproval_Mdl parentRow;
   

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtApplyDate) {
            txtApplyDate.setText(DmClient.returnDate(txtApplyDate.getText()));
        }

    }

    public LeaveEntryApprovalGrdEdit(DmGrid<T1, T2> grid) {
        super(grid, "Grid Edit", "700px", "225px");
        SrvAsync = new ServiceInvoker<LeaveEntryApproval_SrvAsync>(GWT.create(LeaveEntryApproval_Srv.class), LeaveEntryApproval.class.getName());
       // SrvAsync = new ServiceInvoker<>(GWT.create(LeaveEntryApproval_Srv.class), LeaveEntryApproval.class.getName());
        txtApplyDate.addBlurHandler(this);
        textHelpEvents();
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
        manageDisplay();

    }

    private void manageDisplay() {
        txtEmployeeName.setEnabled(false);
        txtApplyDate.setEnabled(false);
        txtFromDate.setEnabled(false);
        txtToDate.setEnabled(false);
        txtEntryNo.setEnabled(false);
        txtDepartment.setEnabled(false);
        txtLeaveName.setEnabled(false);
        txtReason.setEnabled(false);
 
    }

    

    private void setFocusToControl(final DmTextBox txtBox, final String widgetName) {
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtBox.setFocus(true);
                LeaveEntryApprovalGrdEdit.this.setTitle(widgetName);
            }
        });
    }

    @Override
    public void onOk() {
            setValue();
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

    public void setValue() {
        GrdLeaveEntryApproval_Mdl ourRow = new GrdLeaveEntryApproval_Mdl();
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtEntryNo.getTag()));
        ourRow.setApplyDate(DmShared.xNull(txtApplyDate.getText()));
        ourRow.setFromDate(DmShared.xNull(txtFromDate.getText()));
        ourRow.setToDate(DmShared.xNull(txtToDate.getText()));
        ourRow.setEmployeeName(DmShared.xNull(txtEmployeeName.getText()));
        ourRow.setEmployeeCode(DmShared.xNull(txtEmployeeName.getTag()));
        ourRow.setDepartment(DmShared.xNull(txtDepartment.getText()));
        ourRow.setDepartmentCode(DmShared.xNull(txtDepartment.getTag()));
        ourRow.setLeaveName(DmShared.xNull(txtLeaveName.getText()));
        ourRow.setLeaveCode(DmShared.xNull(txtLeaveName.getTag()));
        ourRow.setReason(DmShared.xNull(txtReason.getTag()));
        ourRow.setEntryNo(DmShared.xNull(txtEntryNo.getTag()));
        ourRow.setStatus(DmShared.xNull(txtHStatus.getText()));
        ourRow.setStatusCode(DmShared.xNull(txtHStatus.getTag()));
        
        saveRecord(ourRow);
    }

    @Override
    public void manageOk(T1 row) {

    }
    
    private void textHelpEvents() {
        txtHStatus.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHStatus(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHStatus).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHStatus.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHStatus).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                        txtHStatus.setTag(DmShared.xNull(t1Row.getSharedKeyFieldCode()));
            }
        });
       
    }


    public void saveRecord(final GrdLeaveEntryApproval_Mdl mrTemp) {
        SrvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtEmployeeName).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    callBack.onOk(mrTemp);
                    hide();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtEmployeeName).show();
                }
            }
        });

    }
    

    @Override
    public void manageDelete() {

    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdLeaveEntryApproval_Mdl();
    }

    @Override
    public void displayRow(T1 row) {
       String userEntry = "";
        GrdLeaveEntryApproval_Mdl ourRow = (GrdLeaveEntryApproval_Mdl) row;
         if(DmShared.iNull(ourRow.getStatusCode()).intValue() == PREnum.LEAVE_STATUS.PENDING.getId() ){
             txtEntryNo.setText(DmShared.xNull(ourRow.getEntryNo()));
            txtEntryNo.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
            txtEmployeeName.setText(DmShared.xNull(ourRow.getEmployeeName()));
            txtEmployeeName.setTag(DmShared.xNull(ourRow.getEmployeeCode()));
            txtDepartment.setText(DmShared.xNull(ourRow.getDepartment()));
            txtDepartment.setTag(DmShared.xNull(ourRow.getDepartmentCode()));
            txtFromDate.setText(DmShared.xNull(ourRow.getFromDate()));
            txtToDate.setText(DmShared.xNull(ourRow.getToDate()));
            txtApplyDate.setText(DmShared.xNull(ourRow.getApplyDate()));
            txtHStatus.setText(DmShared.xNull(ourRow.getStatus()));
            txtHStatus.setTag(DmShared.xNull(ourRow.getStatusCode()));
            txtLeaveName.setText(DmShared.xNull(ourRow.getLeaveName()));
            txtLeaveName.setTag(DmShared.xNull(ourRow.getLeaveCode()));
            txtReason.setText(DmShared.xNull(ourRow.getReason()));
            userControl.setTextBlog(modifiedByBlog, userEntry);
         }
         else
         {
             hide();
         }
        
    }

}
