package dataman.accounts.finalPaymentEntry.client;

import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.accounts.finalPaymentEntry.shared.FinalPaymentEntry_Srv;
import dataman.accounts.finalPaymentEntry.shared.FinalPaymentEntry_SrvAsync;
import dataman.accounts.finalPaymentEntry.shared.GrdOtherAc_Mdl;
import dataman.accounts.finalPaymentEntry.shared.TxtHOtherAC_Mdl;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;
import java.util.Objects;

public class OtherAcGridEdit<T1, T2> extends OtherAcGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(GrdOtherAc_Mdl grdOtherAc);

        public void onDelete();
    }

    ServiceInvoker<FinalPaymentEntry_SrvAsync> SrvAsync;
    CallBack callBack;
    GrdOtherAc_Mdl parentRow;
    int intHashCode;

    //<editor-fold defaultstate="collapsed" desc="events">
    private void textHelpEvents() {

        txtHOtherAC.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHOtherAC_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHOtherAC(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHOtherAC_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHOtherAC).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHOtherAC_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHOtherAC.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHOtherAC).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHOtherAC_Mdl t1Row) {
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
        GrdOtherAc_Mdl ourRow = (GrdOtherAc_Mdl) row;
        parentRow = new GrdOtherAc_Mdl();
        intHashCode = ourRow.hashCode();

        txtHOtherAC.setTag(ourRow.getSharedKeyFieldCode());
        txtHOtherAC.setTag2nd(ourRow.getAcCode());
        txtHOtherAC.setText(ourRow.getSharedKeyFieldName());

        txtAmount.setText(ourRow.getAmount());
        txtRemak.setText(ourRow.getRemark());

        txtHOtherAC.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdOtherAc_Mdl();
    }

    @Override
    public void onOk() {

        if (!DmClient.isValidText(txtHOtherAC.getText(), "Other A/c", txtHOtherAC)) {
            return;
        }
        if (DmShared.vNull(txtAmount.getText()).doubleValue() == 0) {
            new DmAlertMessageBox("Amount is mandatory.", txtAmount).show();

            return;
        }
        List<GrdOtherAc_Mdl> lstTemp = (List<GrdOtherAc_Mdl>) getDmGrid().getGrid().getStore().getAll();
        for (int i = 0; i < lstTemp.size(); i++) {
            GrdOtherAc_Mdl grdTdsCategory = lstTemp.get(i);
            if (!Objects.equals(intHashCode, grdTdsCategory.getSharedKeyFieldHashCode())) {
                //Condition For Check Duplicacy of List Items
                if (DmShared.xNull(lstTemp.get(i).getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(txtHOtherAC.getTag()))) {
                    new DmAlertMessageBox("Other A/c Already Exists!", txtHOtherAC).show();
                    return;
                }
            }
        }

        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdOtherAc_Mdl ourRow = (GrdOtherAc_Mdl) row;
        ourRow.setAcCode(txtHOtherAC.getTag2nd());
        ourRow.setSharedKeyFieldCode(txtHOtherAC.getTag());
        ourRow.setSharedKeyFieldName(txtHOtherAC.getText());
        ourRow.setAmount(txtAmount.getText());
        ourRow.setRemark(txtRemak.getText());

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

    public OtherAcGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Other A/c", "500px", "100px");
        SrvAsync = new ServiceInvoker<>(GWT.create(FinalPaymentEntry_Srv.class), FinalPaymentEntry.class.getName());
        textHelpEvents();
    }
}
