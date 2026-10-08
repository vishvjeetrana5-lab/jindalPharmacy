package dataman.utility.voucherType.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.utility.voucherType.shared.*;
import java.util.List;

public class ReceiptTypeGridEdit<T1, T2> extends ReceiptTypeGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(GrdReceiptType_Mdl grdReceiptTypeMdl);

        public void onDelete();
    }

    ServiceInvoker<VoucherType_SrvAsync> SrvAsync;
    CallBack callBack;
    int hashCode;

    private void textHelpEvents() {
        txtHPaymentMode.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHPaymentMode_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHPaymentMode(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHPaymentMode_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPaymentMode).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHPaymentMode_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHPaymentMode.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHPaymentMode).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHPaymentMode_Mdl tRow) {
                if (blnStatus && tRow != null) {
                    txtHPaymentMode.setTag2nd(DmShared.xNull(tRow.getShortName()));
                    txtHIsDefault.setTag2nd(DmShared.xNull(tRow.getCardType()));
                } else {
                    txtHPaymentMode.setTag2nd("");
                    txtHIsDefault.setTag2nd("");
                }
            }
        });
        txtHIsDefault.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHIsDefault(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIsDefault).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHIsDefault.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIsDefault).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
        txtHPaymentMode.setFocus(true);
    }

    @Override
    public void displayRow(T1 row) {
        GrdReceiptType_Mdl ourRow = (GrdReceiptType_Mdl) row;
        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtHPaymentMode.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHPaymentMode.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtHPaymentMode.setTag2nd(DmShared.xNull(ourRow.getShortName()));
        txtHIsDefault.setTag2nd(DmShared.xNull(ourRow.getCardType()));
        if (DmShared.xNull(ourRow.getIsDefaultCode()).equalsIgnoreCase("")) {
            txtHIsDefault.setText("No");
            txtHIsDefault.setTag("0");
        } else {
            txtHIsDefault.setText(DmShared.xNull(ourRow.getIsDefaultName()));
            txtHIsDefault.setTag(DmShared.xNull(ourRow.getIsDefaultCode()));
        }
        txtHPaymentMode.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdReceiptType_Mdl();
    }

    @Override
    public void onOk() {
        if (!DmClient.isValidText(txtHPaymentMode.getTag(), "Payment Mode", txtHPaymentMode)) {
            return;
        }
        if (DmShared.xNull(txtHIsDefault.getTag()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Is Default is mandatory.", txtHIsDefault).show();
            return;
        }

        List<GrdReceiptType_Mdl> lstTemp = (List<GrdReceiptType_Mdl>) getDmGrid().getGrid().getStore().getAll();

        for (int i = 0; i < lstTemp.size(); i++) {
            GrdReceiptType_Mdl get = lstTemp.get(i);
            if (DmShared.xNull(txtHPaymentMode.getTag()).equalsIgnoreCase(DmShared.xNull(get.getSharedKeyFieldCode()))) {
                if (DmShared.vNull(hashCode).doubleValue() != DmShared.vNull(get.getSharedKeyFieldHashCode()).doubleValue()) {
                    new DmAlertMessageBox(txtHPaymentMode.getText() + " already exists.", txtHPaymentMode).show();
                    return;
                }
            }
        }
        for (GrdReceiptType_Mdl grdReciept_Mdl : (List<GrdReceiptType_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
            if (DmShared.iNull(txtHIsDefault.getTag()).intValue() == 1) {
                if (DmShared.iNull(grdReciept_Mdl.getIsDefaultCode()).intValue() == 1) {
                    if (!DmClient.vNull(grdReciept_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                        new DmAlertMessageBox("Is Default value already selected.", txtHIsDefault).show();
                        return;
                    }
                }
            }
        }
        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdReceiptType_Mdl ourRow = (GrdReceiptType_Mdl) row;
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHPaymentMode.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHPaymentMode.getText()));
        ourRow.setIsDefaultCode(DmShared.xNull(txtHIsDefault.getTag()));
        ourRow.setIsDefaultName(DmShared.xNull(txtHIsDefault.getText()));
        ourRow.setShortName(DmShared.xNull(txtHPaymentMode.getTag2nd()));
        ourRow.setCardType(DmShared.xNull(txtHIsDefault.getTag2nd()));
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

    public ReceiptTypeGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Receipt Type", "350px", "90px");
        SrvAsync = new ServiceInvoker<VoucherType_SrvAsync>(GWT.create(VoucherType_Srv.class), VoucherType.class.getName());
        textHelpEvents();
    }
}
