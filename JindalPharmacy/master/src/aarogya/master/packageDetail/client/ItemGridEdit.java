package aarogya.master.packageDetail.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import aarogya.master.packageDetail.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
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

public class ItemGridEdit<T1, T2> extends ItemGridEdit_Dsg<T1, T2> implements BlurHandler {

    interface CallBack {

        public void onOk(GrdServiceMaster_Mdl node);

        public void onDelete();
    }

    ServiceInvoker<PackageDetail_SrvAsync> srvAsync;
    CallBack callBack;
    private int hashCode = 0;
    private boolean isAdd = false;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {
        int cellIndex = 1;
        this.isAdd = newNeeded;
        cellIndex = getDmGrid().getSelectedCell().getCell();
        super.showRelativeTo(currentRowIndex, cellIndex, okNeeded, newNeeded, deleteNeeded);
        txtHItem.setFocus(true);
        txtAmount.setEnabled(false);
    }

    private void textHelpEvents() {
        txtHItem.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHMedicineName_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHItem(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHMedicineName_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHItem).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHMedicineName_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHItem.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHItem).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHMedicineName_Mdl t1Row) {

            }
        });
    }

    @Override
    public void displayRow(T1 row) {
        GrdServiceMaster_Mdl ourRow = (GrdServiceMaster_Mdl) row;

        manageDisplay();
        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtHItem.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHItem.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtQuantity.setText(DmClient.vNull(ourRow.getQuantity(), "0.00"));
        txtRate.setText(DmClient.vNull(ourRow.getRate(), "0.00"));
        txtAmount.setText(DmClient.vNull(ourRow.getAmount(), "0.00"));

        if (txtHItem.isEnabled()) {
            txtHItem.setFocus(true);
        } else {
            txtQuantity.setFocus(true);
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        if ((event.getSource() == txtQuantity) || (event.getSource() == txtRate)) {
            calculate();
        }
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdServiceMaster_Mdl();
    }

    @Override
    public void onOk() {
        calculate();
        if (DmShared.xNull(txtHItem.getTag()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Medicine / Item is mandatory.", txtHItem).show();
            return;
        }
        if (DmShared.vNull(txtQuantity.getText()).doubleValue() == 0) {
            new DmAlertMessageBox("Quantity is mandatory.", txtQuantity).show();
            return;
        }
        for (GrdServiceMaster_Mdl consumable_Mdl : (List<GrdServiceMaster_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
            if ((DmShared.xNull(consumable_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(txtHItem.getTag())))
                    && (DmShared.xNull(consumable_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(txtHItem.getTag())))) {
                if (!DmClient.vNull(consumable_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                    new DmAlertMessageBox(DmShared.xNull(txtHItem.getText()) + "(" + DmShared.xNull(txtHItem.getText()) + ")" + " already exists.", txtHItem).show();
                    return;
                }
            }
        }
        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdServiceMaster_Mdl ourRow = (GrdServiceMaster_Mdl) row;

        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHItem.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHItem.getText()));
        ourRow.setQuantity(DmClient.vNull(txtQuantity.getText(), "0.00"));
        ourRow.setAmount(DmClient.vNull(txtAmount.getText(), "0.00"));
        ourRow.setRate(DmClient.vNull(txtRate.getText(), "0.00"));

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

    public ItemGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Medicine / Item Detail", "400px", "120px");
        srvAsync = new ServiceInvoker<PackageDetail_SrvAsync>(GWT.create(PackageDetail_Srv.class), PackageDetail.class.getName());
        textHelpEvents();
        txtRate.addBlurHandler(this);
        txtQuantity.addBlurHandler(this);
    }

    private void manageDisplay() {
        txtHItem.setEnabled(isAdd);
    }

    private void calculate() {
        txtAmount.setText(DmClient.vNull(DmShared.vNull(txtQuantity.getText()).doubleValue() * DmShared.vNull(txtRate.getText()).doubleValue(), "0.00"));
    }
}
