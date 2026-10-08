package aarogya.inventory.itemGroupMaster.client;

import aarogya.inventory.itemGroupMaster.shared.GrdDepartmentConsumptionAc_Mdl;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import aarogya.inventory.itemGroupMaster.shared.ItemGroupMaster_Srv;
import aarogya.inventory.itemGroupMaster.shared.ItemGroupMaster_SrvAsync;
import aarogya.inventory.supplierMaster.shared.GrdContactPerson_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;
public class DepartmentConcumptionAcGridEdit<T1, T2> extends DepartmentConcumptionAcGridEdit_Dsg<T1 , T2> {

    interface CallBack {

        public void onOk(GrdDepartmentConsumptionAc_Mdl gridVendor_Mdl);

        public void onDelete();
    }

    private ServiceInvoker<ItemGroupMaster_SrvAsync> srvAsync;
    private CallBack callBack;
    private int hashCode = 0;
    
    public DepartmentConcumptionAcGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Department Wise ConsumptionA/c", "450px", "110px");
        srvAsync = new ServiceInvoker<ItemGroupMaster_SrvAsync>(GWT.create(ItemGroupMaster_Srv.class), ItemGroupMaster.class.getName());
        textHelpEvents();
    }

    private void textHelpEvents() {

        txtHDepartment.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHRestrictedDepartment(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDepartment).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDepartment.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDepartment).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHConsumptionAc.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSubCode("", helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHConsumptionAc).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHConsumptionAc.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHConsumptionAc).show();
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

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
    }
    
    @Override
    public void onOk() {
        try {
            if (DmShared.xNull(txtHDepartment.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Department is mandatory.", txtHDepartment).show();
                return;
            }
            if (DmShared.xNull(txtHConsumptionAc.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Consumption A/c is mandatory.", txtHDepartment).show();
                return;
            }

            for (GrdDepartmentConsumptionAc_Mdl detail_Mdl : (List<GrdDepartmentConsumptionAc_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
                if (DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(txtHDepartment.getTag()))) {
                    if (!DmClient.vNull(detail_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                        new DmAlertMessageBox(DmShared.xNull(txtHDepartment.getText()) + " already exists.", txtHDepartment).show();
                        return;
                    }
                }
            }

            proceedOk();
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtHDepartment).show();
        }
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
        GrdDepartmentConsumptionAc_Mdl ourRow = (GrdDepartmentConsumptionAc_Mdl) row;

        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHDepartment.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHDepartment.getText()));
        ourRow.setConsumptionAcCode(DmShared.xNull(txtHConsumptionAc.getTag()));
        ourRow.setConsumptionAcName(DmShared.xNull(txtHConsumptionAc.getText()));
        callBack.onOk(ourRow);
    }

    @Override
    public void manageDelete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdDepartmentConsumptionAc_Mdl();
    }

    @Override
    public void displayRow(T1 row) {
        GrdDepartmentConsumptionAc_Mdl ourRow = (GrdDepartmentConsumptionAc_Mdl) row;

        txtHDepartment.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHDepartment.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtHConsumptionAc.setTag(DmShared.xNull(ourRow.getConsumptionAcCode()));
        txtHConsumptionAc.setText(DmShared.xNull(ourRow.getConsumptionAcName()));
        
        hashCode = ourRow.getSharedKeyFieldHashCode();

        txtHDepartment.setFocus(true);
    }
}
