package aarogya.pharmacy.procurementPlannerDisplay.client;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import aarogya.pharmacy.procurementPlannerDisplay.shared.ProcurementPlannerDisplay_Mdl;
import com.google.gwt.core.client.GWT;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import aarogya.pharmacy.procurementPlannerDisplay.shared.ProcurementPlannerDisplay_Srv;
import aarogya.pharmacy.procurementPlannerDisplay.shared.ProcurementPlannerDisplay_SrvAsync;

public class MedicineDetail<T1, T2> extends MedicineDetail_Dsg<T1, T2> implements BlurHandler, KeyUpHandler, KeyDownHandler {

    private ServiceInvoker<ProcurementPlannerDisplay_SrvAsync> srvAsync;
    private CallBack callBack;
    private MedicineDetail medicineRequisitionDetail;
    
    interface CallBack {

        public void onOk(ProcurementPlannerDisplay_Mdl opdService);
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
    
    public MedicineDetail(DmGrid<T1, T2> grid) {
        super(grid, "Medicine Detail", "400px", "300px");
        srvAsync = new ServiceInvoker<ProcurementPlannerDisplay_SrvAsync>(GWT.create(ProcurementPlannerDisplay_Srv.class), ProcurementPlannerDisplay.class.getName());
        txtReqDate.addBlurHandler(this);
        txtQuantity.addBlurHandler(this);
        txtEstimatedValue.setEnabled(false);
//        textHelpEvents();
//        manageEvent();
    }

    @Override
    public void onBlur(BlurEvent event) {
        double estdValue=0;
        if (event.getSource()==txtQuantity){
            if (DmShared.vNull(txtRmark.getTag2nd().trim())>0){
                estdValue=DmShared.vNull(txtQuantity.getText())*(DmShared.vNull(txtRmark.getTag2nd())/ DmShared.vNull(txtRmark.getTag()));
                txtEstimatedValue.setText(DmClient.vNull(estdValue,"0.00"));
            }else {
                txtEstimatedValue.setText("0.00");
            }
        }else if (event.getSource()==txtReqDate){
            txtReqDate.setText(DmClient.returnDate(txtReqDate.getText()));
        }
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
//            if (DmShared.vNull(txtQuantity.getText()).doubleValue()>DmShared.vNull(txtQuantity.getTag()).doubleValue()) {
//                new DmAlertMessageBox("Quantity can't be grater than assigned Qty("+DmClient.vNull(txtQuantity.getTag(),"0.00")+").", txtQuantity).show();
//                return;
//            }
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
        ProcurementPlannerDisplay_Mdl ourRow = (ProcurementPlannerDisplay_Mdl) row;
            ourRow.setFinalIndentedQty(DmClient.vNull(txtQuantity.getText(), "0.00"));
            ourRow.setRemark(DmShared.xNull(txtRmark.getText().trim()));
            ourRow.setRequiredDate(DmShared.xNull(txtReqDate.getText().trim()));
            ourRow.setEstimatedCost(DmClient.vNull(txtEstimatedValue.getText(), "0.00"));
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
        ProcurementPlannerDisplay_Mdl ourRow = (ProcurementPlannerDisplay_Mdl) row;

        txtRmark.setText(DmShared.xNull(ourRow.getRemark()));
        
        txtMedicineName.setText(DmShared.xNull(ourRow.getItemName()));
        txtMedicineName.setTag(DmShared.xNull(ourRow.getItemCode()));

        if (DmShared.vNull(ourRow.getFinalIndentedQty()).doubleValue()>0){
            txtQuantity.setText(DmClient.vNull(ourRow.getFinalIndentedQty(), "0.00"));
        }else{
            txtQuantity.setText(DmClient.vNull(ourRow.getRecommendedQtySite(), "0.00"));
        }
        txtReqDate.setText(DmShared.xNull(ourRow.getRequiredDate()));
        txtRmark.setTag(DmShared.vNull(ourRow.getPurchaseUnitConversionRate()).toString());
        txtRmark.setTag2nd(DmShared.vNull(ourRow.getPurchaseRate()).toString());
        txtQuantity.setFocus(true);
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, ProcurementPlannerDisplay procurementPlannerDisplay) {
        this.medicineRequisitionDetail = medicineRequisitionDetail;
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);

    }

    private void clear() {
        txtMedicineName.setTag("");
        txtMedicineName.setText("");
        
        txtRmark.setTag("");
        txtRmark.setTag2nd("");
        txtRmark.setText("");
        
        txtQuantity.setText("");
        txtQuantity.setTag("");
    }
}
