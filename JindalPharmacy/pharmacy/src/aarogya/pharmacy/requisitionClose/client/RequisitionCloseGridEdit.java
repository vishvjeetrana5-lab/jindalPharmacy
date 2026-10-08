package aarogya.pharmacy.requisitionClose.client;



import aarogya.pharmacy.requisitionClose.shared.GrdRequisitionClose_Mdl;
import aarogya.pharmacy.requisitionClose.shared.RequisitionClose_Srv;
import aarogya.pharmacy.requisitionClose.shared.RequisitionClose_SrvAsync;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class RequisitionCloseGridEdit<T1, T2> extends RequisitionCloseGridEdit_Dsg<T1, T2> implements BlurHandler {

    interface CallBack {

        public void onOk(GrdRequisitionClose_Mdl sampleDetails_Mdl);

        public void onDelete();

        public void onRefresh();
    }

    private final ServiceInvoker<RequisitionClose_SrvAsync> SrvAsync;
    private CallBack callBack;
    private GrdRequisitionClose_Mdl parentRow;
    private RequisitionClose clsParent;

    @Override
    public void onBlur(BlurEvent event) {
//        if (event.getSource() == txtSampleDate) {
//            txtSampleDate.setText(DmClient.returnDate(txtSampleDate.getText()));
//        }
    }

  

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, RequisitionClose purchaseOrderClose) {
        this.clsParent = purchaseOrderClose;
//        super.showRelativeTo(currentRowIndex, 1, okNeeded, false, false);
        super.showCenter(currentRowIndex, okNeeded, false, false);

   
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
              
            }
        });
    }

    @Override
    public void displayRow(T1 row) {
        GrdRequisitionClose_Mdl ourRow = (GrdRequisitionClose_Mdl) row;
        parentRow = ourRow;


        txtRemark.setText(DmShared.xNull(ourRow.getStatusRemark()));
        txtRemark.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdRequisitionClose_Mdl();
    }

    @Override
    public void onOk() {
        if (txtRemark.getText().trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Remark is mandatory.", txtRemark).show();
            return;
        }
       
        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdRequisitionClose_Mdl ourRow = (GrdRequisitionClose_Mdl) row;

        ourRow = parentRow;

        ourRow.setStatusRemark(DmShared.xNull(txtRemark.getText()));
  

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

    public RequisitionCloseGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Reason", "400px", "62px");
        SrvAsync = new ServiceInvoker<RequisitionClose_SrvAsync>(GWT.create(RequisitionClose_Srv.class), RequisitionClose.class.getName());
    
    }



}
