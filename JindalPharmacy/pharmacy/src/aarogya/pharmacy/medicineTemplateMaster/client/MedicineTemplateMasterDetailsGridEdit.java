package aarogya.pharmacy.medicineTemplateMaster.client;

import aarogya.pharmacy.medicineTemplateMaster.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyField;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public class MedicineTemplateMasterDetailsGridEdit<T1, T2> extends MedicineTemplateMasterDetailsGridEdit_Dsg<T1, T2> implements BlurHandler {

    ServiceInvoker<MedicineTemplateMaster_SrvAsync> SrvAsync;
    CallBack callBack;
    GrdMedicineTemplateMasterDetails_Mdl parentRow;
    int hashCode;

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtQty)) {
            txtQty.setText(DmClient.vNull(DmShared.vNull(txtQty.getText()), DmShared.getNumberFormat(DmShared.iNull(txtSku.getTag2nd()).intValue())));
        }
    }

    interface CallBack {

        public void onOk(GrdMedicineTemplateMasterDetails_Mdl details_Mdl);

        public void onDelete();
    }

    private void textHelpEvents() {

        txtHItem.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHItem_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHItem(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHItem_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHItem).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHItem_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHItem.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHItem).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHItem_Mdl t1Row) {
                if (blnStatus && t1Row != null) {
                    txtSku.setText(DmShared.xNull(t1Row.getSku()));
                    txtSku.setTag2nd(DmShared.iNull(t1Row.getSkuDecimalPlaces()).toString());
                }

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
        GrdMedicineTemplateMasterDetails_Mdl ourRow = (GrdMedicineTemplateMasterDetails_Mdl) row;
        parentRow = new GrdMedicineTemplateMasterDetails_Mdl();

        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtHItem.setTag(DmShared.iNull(ourRow.getSharedKeyFieldCode()).toString());
        txtHItem.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtSku.setText(DmShared.xNull(ourRow.getSku()));
        txtSku.setTag2nd(DmShared.iNull(ourRow.getSkuDecimalPlaces()).toString());
        txtQty.setText(DmShared.vNull(ourRow.getQty()).toString());
        txtHItem.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdMedicineTemplateMasterDetails_Mdl();
    }

    @Override
    public void onOk() {
        calculate();
        if (DmShared.iNull(txtHItem.getTag()).toString().trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Medicine is mandatory.", txtHItem).show();
            return;
        }

        if (DmClient.vNull(txtQty.getText().trim(), "0.00").equalsIgnoreCase("0.00")) {
            new DmAlertMessageBox("Quantity is mandatory.", txtQty).show();
            return;
        }

        List<GrdMedicineTemplateMasterDetails_Mdl> lstTemp = (List<GrdMedicineTemplateMasterDetails_Mdl>) getDmGrid().getGrid().getStore().getAll();

        for (int i = 0; i < lstTemp.size(); i++) {
            KeyField get = lstTemp.get(i);
            if (DmShared.iNull(txtHItem.getTag()).toString().equalsIgnoreCase(DmShared.xNull(get.getSharedKeyFieldCode()))) {
                if (DmShared.vNull(hashCode).doubleValue() != DmShared.vNull(get.getSharedKeyFieldHashCode()).doubleValue()) {
                    new DmAlertMessageBox(txtHItem.getText() + " already exists.", txtHItem).show();
                    return;
                }
            }
        }
        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdMedicineTemplateMasterDetails_Mdl ourRow = (GrdMedicineTemplateMasterDetails_Mdl) row;
        ourRow.setSharedKeyFieldCode(DmShared.iNull(txtHItem.getTag()).toString());
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHItem.getText()));
        ourRow.setSku(DmShared.xNull(txtSku.getText()));
        ourRow.setSkuDecimalPlaces(DmShared.iNull(txtSku.getTag2nd()).toString());
        ourRow.setQty(DmShared.vNull(txtQty.getText()).toString());
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

    public MedicineTemplateMasterDetailsGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Medicine Template Details", "450px", "75px");
        SrvAsync = new ServiceInvoker<>(GWT.create(MedicineTemplateMaster_Srv.class), MedicineTemplateMaster.class.getName());
        txtSku.setEnabled(false);
        txtQty.addBlurHandler(this);
        textHelpEvents();

    }

    private void calculate() {
        txtQty.setText(DmClient.vNull(DmShared.vNull(txtQty.getText()), DmShared.getNumberFormat(DmShared.iNull(txtSku.getTag2nd()).intValue())));
    }

}
