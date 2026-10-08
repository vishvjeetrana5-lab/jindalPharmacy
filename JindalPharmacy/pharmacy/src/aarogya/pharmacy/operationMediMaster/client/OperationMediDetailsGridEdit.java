package aarogya.pharmacy.operationMediMaster.client;

import dataman.dmBase.self.shared.*;
import aarogya.pharmacy.operationMediMaster.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public class OperationMediDetailsGridEdit<T1, T2> extends OperationMediDetailsGridEdit_Dsg<T1, T2> implements BlurHandler {

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtQty)) {
            txtQty.setText(DmClient.vNull(DmShared.vNull(txtQty.getText()), DmShared.getNumberFormat(DmShared.iNull(txtSku.getTag2nd()).intValue())));
        }
    }

    interface CallBack {

        public void onOk(GrdOperationDetails_Mdl details_Mdl);

        public void onDelete();
    }

    ServiceInvoker<OperationMediMaster_SrvAsync> SrvAsync;
    CallBack callBack;
    GrdOperationDetails_Mdl parentRow;
    int hashCode;

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
                    txtSku.setText(t1Row.getSku());
                    txtSku.setTag2nd(DmShared.iNull(t1Row.getSkuDecimalPlaces()).toString());
                } else {
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
        GrdOperationDetails_Mdl ourRow = (GrdOperationDetails_Mdl) row;
        parentRow = new GrdOperationDetails_Mdl();

        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtHItem.setTag(ourRow.getSharedKeyFieldCode());
        txtHItem.setText(ourRow.getSharedKeyFieldName());
        // txtHItem.setTag2nd(ourRow.getSku());
        txtSku.setText(ourRow.getSku());
        txtSku.setTag2nd(DmShared.iNull(ourRow.getSkuDecimalPlaces()).toString());
        txtQty.setText(ourRow.getQty());
        txtHItem.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdOperationDetails_Mdl();
    }

    @Override
    public void onOk() {
        calculate();
        if (DmShared.xNull(txtHItem.getTag()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Medicine is mandatory.", txtHItem).show();
            return;
        }

        if (DmClient.vNull(txtQty.getText().trim(), "0.00").equalsIgnoreCase("0.00")) {
            new DmAlertMessageBox("Quantity is mandatory.", txtQty).show();
            return;
        }

        List<GrdOperationDetails_Mdl> lstTemp = (List<GrdOperationDetails_Mdl>) getDmGrid().getGrid().getStore().getAll();

        for (int i = 0; i < lstTemp.size(); i++) {
            KeyField get = lstTemp.get(i);
            if (DmShared.xNull(txtHItem.getTag()).equalsIgnoreCase(DmShared.xNull(get.getSharedKeyFieldCode()))) {
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
        GrdOperationDetails_Mdl ourRow = (GrdOperationDetails_Mdl) row;
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHItem.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHItem.getText()));
        ourRow.setSku(DmShared.xNull(txtSku.getText()));
        ourRow.setSkuDecimalPlaces(DmShared.iNull(txtSku.getTag2nd()).toString());
        ourRow.setQty(DmShared.xNull(txtQty.getText()));
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

    public OperationMediDetailsGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Operation Medicine Details", "450px", "75px");
        SrvAsync = new ServiceInvoker<>(GWT.create(OperationMediMaster_Srv.class), OperationMediMaster.class.getName());
        txtSku.setEnabled(false);
        txtQty.addBlurHandler(this);
        textHelpEvents();
    }

    private void calculate() {
        txtQty.setText(DmClient.vNull(DmShared.vNull(txtQty.getText()), DmShared.getNumberFormat(DmShared.iNull(txtSku.getTag2nd()).intValue())));
    }
}
