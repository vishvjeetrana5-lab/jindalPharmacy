package aarogya.pharmacy.medicineIndentDisplay.client;

import aarogya.pharmacy.medicineInTransit.client.MedicineInTransitGridEdit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import aarogya.pharmacy.medicineIndentDisplay.shared.MedicineIndentDisplay_Mdl;
import com.google.gwt.core.client.GWT;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging.CallBack;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.List;
import aarogya.pharmacy.medicineIndentDisplay.shared.MedicineIndentDisplay_Srv;
import aarogya.pharmacy.medicineIndentDisplay.shared.MedicineIndentDisplay_SrvAsync;

public class MedicineDetail<T1, T2> extends MedicineDetail_Dsg<T1, T2> implements BlurHandler, KeyUpHandler, KeyDownHandler {

    private ServiceInvoker<MedicineIndentDisplay_SrvAsync> srvAsync;
    private CallBack callBack;
    private MedicineDetail medicineRequisitionDetail;
    
    interface CallBack {

        public void onOk(MedicineIndentDisplay_Mdl opdService);
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
    
    public MedicineDetail(DmGrid<T1, T2> grid) {
        super(grid, "Medicine Detail", "400px", "300px");
        srvAsync = new ServiceInvoker<MedicineIndentDisplay_SrvAsync>(GWT.create(MedicineIndentDisplay_Srv.class), MedicineIndentnDisplay.class.getName());
//        textHelpEvents();
//        manageEvent();
    }

    @Override
    public void onBlur(BlurEvent event) {
        
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        
    }

    @Override
    public void onOk() {
        try {
            if (DmShared.vNull(txtQuantity.getText()).doubleValue()>DmShared.vNull(txtQuantity.getTag()).doubleValue()) {
                new DmAlertMessageBox("Quantity can't be grater than assigned Qty("+DmClient.vNull(txtQuantity.getTag(),"0.00")+").", txtQuantity).show();
                return;
            }
            proceedOk();

        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtMedicineName).show();
        }
    }

    @Override
    public void onNew() {
        
    }

    @Override
    public void onDelete() {
        
    }

    @Override
    public void onClose() {
        hide();
    }

    @Override
    public void manageOk(T1 row) {
        MedicineIndentDisplay_Mdl ourRow = (MedicineIndentDisplay_Mdl) row;

            ourRow.setSharedKeyFieldCode(DmShared.xNull(txtRequisitionNo.getTag()));
            ourRow.setSharedKeyFieldName(DmShared.xNull(txtRequisitionNo.getText()));
            
//            ourRow.setV_SNo(DmShared.xNull(txtRequisitionNo.getTag2nd()));
//
//            ourRow.setItemQty(DmClient.vNull(txtQuantity.getText(), "0.00"));
            
            callBack.onOk(ourRow);
    }

    @Override
    public void manageDelete() {
            }

    @Override
    public T1 getNewRow() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void displayRow(T1 row) {
        clear();
        MedicineIndentDisplay_Mdl ourRow = (MedicineIndentDisplay_Mdl) row;

        txtRequisitionNo.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
//        txtRequisitionNo.setTag2nd(DmShared.xNull(ourRow.getV_SNo()));
        txtRequisitionNo.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        
        txtMedicineName.setText(DmShared.xNull(ourRow.getItemName()));
        txtMedicineName.setTag(DmShared.xNull(ourRow.getItemCode()));

//        txtQuantity.setText(DmClient.vNull(ourRow.getItemQty(), "0.00"));
//        txtQuantity.setTag(DmClient.vNull(ourRow.getItemQty(), "0.00"));
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, MedicineIndentnDisplay medicineIndentDisplay) {
        this.medicineRequisitionDetail = medicineRequisitionDetail;
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);

    }

    private void clear() {
        txtMedicineName.setTag("");
        txtMedicineName.setText("");
        
        txtRequisitionNo.setTag("");
        txtRequisitionNo.setTag2nd("");
        txtRequisitionNo.setText("");
        
        txtQuantity.setText("");
        txtQuantity.setTag("");
    }
}
