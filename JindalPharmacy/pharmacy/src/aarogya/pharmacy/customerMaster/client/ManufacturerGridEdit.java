package aarogya.pharmacy.customerMaster.client;

import aarogya.pharmacy.customerMaster.shared.*;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import java.util.List;

public class ManufacturerGridEdit<T1, T2> extends ManufacturerGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(GrdManufacturer_Mdl manf_Mdl);

        public void onDelete();

    }

    ServiceInvoker<CustomerMaster_SrvAsync> SrvAsync;
    CallBack callBack;
    KeyFieldExtended parentRow;
    private int hashCode = 0;

    private void textHelpEvents() {

        txtHManufacturer.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHManufacturer_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHManufacturer(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHManufacturer_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHManufacturer).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHManufacturer_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHManufacturer.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHManufacturer).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHManufacturer_Mdl t1Row) {
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
    public void displayRow(T1 row) {
        GrdManufacturer_Mdl ourRow = (GrdManufacturer_Mdl) row;
        parentRow = new KeyFieldExtended();

        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtHManufacturer.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtHManufacturer.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHManufacturer.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdManufacturer_Mdl();
    }

    @Override
    public void onOk() {
        if (DmShared.xNull(txtHManufacturer.getTag()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Manufacturer is mandatory.", txtHManufacturer).show();
            return;
        }
        for (KeyFieldExtended keyField : (List<KeyFieldExtended>) getDmGrid().getGrid().getStore().getAll()) {
            if (DmShared.xNull(keyField.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(txtHManufacturer.getTag()))) {
                if (!DmClient.vNull(keyField.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                    new DmAlertMessageBox(DmShared.xNull(txtHManufacturer.getText()) + " is already exists.", txtHManufacturer).show();
                    return;
                }
            }
        }
        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdManufacturer_Mdl ourRow = (GrdManufacturer_Mdl) row;
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHManufacturer.getText()));
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHManufacturer.getTag()));

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

    public ManufacturerGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Manufacturer", "400px", "56px");
        SrvAsync = new ServiceInvoker<CustomerMaster_SrvAsync>(GWT.create(CustomerMaster_Srv.class), CustomerMaster.class.getName());
        textHelpEvents();
    }

}
