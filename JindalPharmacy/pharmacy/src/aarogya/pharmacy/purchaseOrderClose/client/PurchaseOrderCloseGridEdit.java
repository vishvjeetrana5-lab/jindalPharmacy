package aarogya.pharmacy.purchaseOrderClose.client;


import aarogya.pharmacy.purchaseOrderClose.shared.GrdPurchaseOrderClose_Mdl;
import aarogya.pharmacy.purchaseOrderClose.shared.PurchaseOrderClose_Srv;
import aarogya.pharmacy.purchaseOrderClose.shared.PurchaseOrderClose_SrvAsync;
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
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.Date;
import java.util.List;

public class PurchaseOrderCloseGridEdit<T1, T2> extends PurchaseOrderCloseGridEdit_Dsg<T1, T2> implements BlurHandler {

    interface CallBack {

        public void onOk(GrdPurchaseOrderClose_Mdl sampleDetails_Mdl);

        public void onDelete();

        public void onRefresh();
    }

    private final ServiceInvoker<PurchaseOrderClose_SrvAsync> SrvAsync;
    private CallBack callBack;
    private GrdPurchaseOrderClose_Mdl parentRow;
    private PurchaseOrderClose clsParent;

    @Override
    public void onBlur(BlurEvent event) {
//        if (event.getSource() == txtSampleDate) {
//            txtSampleDate.setText(DmClient.returnDate(txtSampleDate.getText()));
//        }
    }

  

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, PurchaseOrderClose purchaseOrderClose) {
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
        GrdPurchaseOrderClose_Mdl ourRow = (GrdPurchaseOrderClose_Mdl) row;
        parentRow = ourRow;


        txtRemark.setText(DmShared.xNull(ourRow.getStatusRemark()));
        txtRemark.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdPurchaseOrderClose_Mdl();
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
        GrdPurchaseOrderClose_Mdl ourRow = (GrdPurchaseOrderClose_Mdl) row;

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

    public PurchaseOrderCloseGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Reason", "400px", "62px");
        SrvAsync = new ServiceInvoker<PurchaseOrderClose_SrvAsync>(GWT.create(PurchaseOrderClose_Srv.class), PurchaseOrderClose.class.getName());
    
    }



}
