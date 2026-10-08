/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataman.payroll.employeeExpenceEntry.client;

import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;

import dataman.dmToolkit.dmGrid.client.DmGrid;

import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.payroll.employeeExpenceEntry.shared.EmployeeExpenceEntry_Srv;
import dataman.payroll.employeeExpenceEntry.shared.EmployeeExpenceEntry_SrvAsync;
import dataman.payroll.employeeExpenceEntry.shared.GridEmployeeExpenceEntry_Mdl;

public class EmployeeExpenceEntryGridEdit<T1, T2> extends EmployeeExpenceEntryGridEdit_Dsg<T1, T2> implements BlurHandler, KeyUpHandler {

    private final ServiceInvoker<EmployeeExpenceEntry_SrvAsync> srvAsync;
    private CallBack callBack;
    private EmployeeExpenceEntry empexpent;
    private int hashCode = 0;
    double total = 0;
    boolean flag=false;

    public EmployeeExpenceEntryGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Value", "156px", "35px");
        srvAsync = new ServiceInvoker<>(GWT.create(EmployeeExpenceEntry_Srv.class), EmployeeExpenceEntry.class.getName());

    }

    interface CallBack {

        public void onOk(GridEmployeeExpenceEntry_Mdl empexpentmdl);

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
        GridEmployeeExpenceEntry_Mdl ourRow = (GridEmployeeExpenceEntry_Mdl) row;

        ourRow.setValue(DmShared.xNull(txtValue.getText()));

        callBack.onOk(ourRow);
    }

    @Override
    public void manageDelete() {
        callBack.onDelete();
    }

    @Override
    public T1 getNewRow() {

        return (T1) new GridEmployeeExpenceEntry_Mdl();
    }

    @Override
    public void displayRow(T1 row) {
        GridEmployeeExpenceEntry_Mdl ourRow = (GridEmployeeExpenceEntry_Mdl) row;

        txtValue.setText(DmShared.foriegnKey(DmShared.xNull(ourRow.getValue())));

        hashCode = ourRow.getSharedKeyFieldHashCode();

        txtValue.setFocus(true);
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, final boolean isDeleteFocus, final EmployeeExpenceEntry empexpent) {
        this.empexpent = empexpent;
        super.showRelativeTo(currentRowIndex, 3, okNeeded, newNeeded, deleteNeeded);
        txtValue.setEnabled(true);

    }



}
