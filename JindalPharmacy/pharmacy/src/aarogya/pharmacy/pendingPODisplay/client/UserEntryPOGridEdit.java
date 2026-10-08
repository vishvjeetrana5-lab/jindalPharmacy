/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aarogya.pharmacy.pendingPODisplay.client;

import aarogya.pharmacy.purchaseOrderApproval.client.*;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import aarogya.pharmacy.purchaseOrderApproval.shared.*;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;

import dataman.dmToolkit.dmGrid.client.DmGrid;

import dataman.dmToolkit.dmStatic.shared.DmShared;


public class UserEntryPOGridEdit<T1, T2> extends UserEntryPOGridEdit_Dsg<T1, T2> implements BlurHandler, KeyUpHandler {

    private final ServiceInvoker<PurchaseApproval_SrvAsync> srvAsync;
    private CallBack callBack;
    private OrderedIndentDetail empexpent;
    private int hashCode = 0;
    double total = 0;
    boolean flag=false;

    public UserEntryPOGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "In Purchase Order", "156px", "35px");
        srvAsync = new ServiceInvoker<>(GWT.create(PurchaseApproval_Srv.class), OrderedIndentDetail.class.getName());

    }

    interface CallBack {

        public void onOk(GrdPurchaseOrderDetail_Mdl empexpentmdl);

        public void onDelete();
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void onBlur(BlurEvent event) {
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
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
        GrdPurchaseOrderDetail_Mdl ourRow = (GrdPurchaseOrderDetail_Mdl) row;

        ourRow.setOrderInPurchaseOrder(DmShared.xNull(txtValue.getText()));

        callBack.onOk(ourRow);
    }

    @Override
    public void manageDelete() {
        callBack.onDelete();
    }

    @Override
    public T1 getNewRow() {

        return (T1) new GrdPurchaseOrderDetail_Mdl();
    }

    @Override
    public void displayRow(T1 row) {
        GrdPurchaseOrderDetail_Mdl ourRow = (GrdPurchaseOrderDetail_Mdl) row;

        txtValue.setText(DmShared.foriegnKey(DmShared.xNull(ourRow.getOrderInPurchaseOrder())));

        hashCode = ourRow.getSharedKeyFieldHashCode();

        txtValue.setFocus(true);
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, final boolean isDeleteFocus, final OrderedIndentDetail empexpent) {
        this.empexpent = empexpent;
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
        txtValue.setEnabled(true);

    }



}
