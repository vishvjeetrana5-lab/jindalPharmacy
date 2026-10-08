package aarogya.inventory.itemMaster.client;

import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;
import aarogya.inventory.itemMaster.shared.GrdUnit_Mdl;
import aarogya.inventory.itemMaster.shared.ItemMaster_Srv;
import aarogya.inventory.itemMaster.shared.ItemMaster_SrvAsync;

public class SecondaryUnitGridEdit<T1, T2> extends SecondaryUnitGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(GrdUnit_Mdl gridVendor_Mdl);

        public void onDelete();
    }

    private final ServiceInvoker<ItemMaster_SrvAsync> srvAsync;
    private CallBack callBack;
    private int hashCode = 0;
    private String sku = "";

    private void textHelpEvents() {
        txtHSecondryUnitSKU.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getSKU(sku, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSecondryUnitSKU).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSecondryUnitSKU.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSecondryUnitSKU).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
                if (blnStatus) {
                }
            }
        });

    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, String sku) {
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
        this.sku = sku;
    }

    @Override
    public void displayRow(T1 row) {
        GrdUnit_Mdl ourRow = (GrdUnit_Mdl) row;

        txtHSecondryUnitSKU.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHSecondryUnitSKU.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtConversionRate.setText(DmShared.xNull(ourRow.getConversionRate()));
        hashCode = ourRow.getSharedKeyFieldHashCode();

        txtHSecondryUnitSKU.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdUnit_Mdl();
    }

    @Override
    public void onOk() {
        try {
            if (DmShared.xNull(txtHSecondryUnitSKU.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Secondry Unit is mandatory.", txtHSecondryUnitSKU).show();
                return;
            }
            if (DmShared.xNull(txtConversionRate.getText()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Conversion Rate is mandatory.", txtConversionRate).show();
                return;
            }

            for (GrdUnit_Mdl detail_Mdl : (List<GrdUnit_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
                if (DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(txtHSecondryUnitSKU.getTag()))) {
                    if (!DmClient.vNull(detail_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                        new DmAlertMessageBox(DmShared.xNull(txtHSecondryUnitSKU.getText()) + " already exists.", txtHSecondryUnitSKU).show();
                        return;
                    }
                }
            }

            proceedOk();
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtHSecondryUnitSKU).show();
        }
    }

    @Override
    public void manageOk(T1 row) {
        GrdUnit_Mdl ourRow = (GrdUnit_Mdl) row;

        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHSecondryUnitSKU.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHSecondryUnitSKU.getText()));
        ourRow.setConversionRate(DmShared.xNull(txtConversionRate.getText()));

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

    public SecondaryUnitGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Grid Edit", "320px", "70px");
        srvAsync = new ServiceInvoker<ItemMaster_SrvAsync>(GWT.create(ItemMaster_Srv.class), ItemMaster.class.getName());
        textHelpEvents();
    }

}
