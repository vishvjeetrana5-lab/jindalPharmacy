package aarogya.master.additionalUserPermission.client;

import aarogya.master.additionalUserPermission.shared.AdditionalUserPermission_Srv;
import aarogya.master.additionalUserPermission.shared.AdditionalUserPermission_SrvAsync;
import aarogya.master.additionalUserPermission.shared.GridPaymentMode_Mdl;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public class PaymentModeGridEdit<T1, T2> extends PaymentModeGridEdit_Dsg<T1, T2> {

    private final ServiceInvoker<AdditionalUserPermission_SrvAsync> SrvAsync;
    private CallBack callBack;
    private String userName = "";
    private String duplicateValue = "";
    int hashCode;

    interface CallBack {

        public void onOk(GridPaymentMode_Mdl paymentGrid);

        public void onDelete();
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void textHelpEvent() {

        txtHPaymentGrid.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHPaymentMode(helper, blnToEndOfFile,txtHVoucherType.getTag(), new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPaymentGrid.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
                if (blnStatus && tRow != null) {
                    txtHPaymentGrid.setText(DmShared.xNull(tRow.getSharedKeyFieldName()));
                    txtHPaymentGrid.setTag2nd(txtHPaymentGrid.getTag());

                }
            }
        });

        txtHVoucherType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHVoucherType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHVoucherType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
                if (blnStatus && tRow != null) {
                    txtHVoucherType.setText(DmShared.xNull(tRow.getSharedKeyFieldName()));
                    txtHVoucherType.setTag2nd(txtHVoucherType.getTag());

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

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, String duplicateValue, String userName) {
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
        this.userName = DmShared.xNull(userName);
        this.duplicateValue = DmShared.xNull(duplicateValue);
        txtHPaymentGrid.setFocus(true);
    }

    public PaymentModeGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Payment Mode", "344px", "100px");
        SrvAsync = new ServiceInvoker<AdditionalUserPermission_SrvAsync>(com.google.gwt.core.shared.GWT.create(AdditionalUserPermission_Srv.class), AdditionalUserPermission.class.getName());
        textHelpEvent();
    }

    @Override
    public void displayRow(T1 row) {
        GridPaymentMode_Mdl ourRow = (GridPaymentMode_Mdl) row;
        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtHPaymentGrid.setTag(ourRow.getSharedKeyFieldCode());
        txtHPaymentGrid.setText(ourRow.getSharedKeyFieldName());
        txtHVoucherType.setTag(ourRow.getVoucherTypeTag());
        txtHVoucherType.setText(ourRow.getVoucherTypeText());
//        txtHIsDefault.setTag2nd(DmShared.xNull(ourRow.getCardType()));
        if (DmShared.xNull(ourRow.getIsDefaultTag()).equalsIgnoreCase("")) {
            txtHIsDefault.setText("No");
            txtHIsDefault.setTag("0");
        } else {
            txtHIsDefault.setText(DmShared.xNull(ourRow.getIsDefaultText()));
            txtHIsDefault.setTag(DmShared.xNull(ourRow.getIsDefaultTag()));
        }

    }

    @Override
    public T1 getNewRow() {
        return (T1) new GridPaymentMode_Mdl();
    }

    @Override
    public void onOk() {

        if (!DmClient.isValidText(txtHVoucherType.getTag(), "Voucher Type", txtHVoucherType)) {
            return;
        }

        if (!DmClient.isValidText(txtHPaymentGrid.getTag(), "Payment Mode", txtHPaymentGrid)) {
            return;
        }

        if (DmShared.xNull(txtHIsDefault.getTag()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Is Default is mandatory.", txtHIsDefault).show();
            return;
        }
        if (!txtHPaymentGrid.getTag().equalsIgnoreCase(this.duplicateValue)) {
            List<GridPaymentMode_Mdl> lstTemp = (List<GridPaymentMode_Mdl>) getDmGrid().getGrid().getStore().getAll();
            for (int i = 0; i < lstTemp.size(); i++) {
                if (DmShared.xNull(lstTemp.get(i).getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(txtHPaymentGrid.getTag()))
                        && DmShared.xNull(lstTemp.get(i).getVoucherTypeTag()).equalsIgnoreCase(DmShared.xNull(txtHVoucherType.getTag()))
                        && DmShared.xNull(lstTemp.get(i).getUserName()).equalsIgnoreCase(DmShared.xNull(userName))) {
                    new DmAlertMessageBox("Payment type Already Exist.", txtHPaymentGrid).show();
                    return;
                }
            }
        }

        for (GridPaymentMode_Mdl grdPayment_Mdl : (List<GridPaymentMode_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
            if (DmShared.iNull(txtHIsDefault.getTag()).intValue() == 1) {
                if (DmShared.iNull(grdPayment_Mdl.getIsDefaultTag()).intValue() == 1 && DmShared.xNull(txtHVoucherType.getTag()).equalsIgnoreCase(grdPayment_Mdl.getVoucherTypeTag())) {
                    if (!DmClient.vNull(grdPayment_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
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
        GridPaymentMode_Mdl ourRow = (GridPaymentMode_Mdl) row;
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHPaymentGrid.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHPaymentGrid.getText()));
        ourRow.setUserName(DmShared.xNull(userName));
        ourRow.setVoucherTypeTag(DmShared.xNull(txtHVoucherType.getTag()));
        ourRow.setVoucherTypeText(DmShared.xNull(txtHVoucherType.getText()));
        ourRow.setIsDefaultTag(DmShared.xNull(txtHIsDefault.getTag()));
        ourRow.setIsDefaultText(DmShared.xNull(txtHIsDefault.getText()));
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
    public void manageDelete() {
        callBack.onDelete();
    }

    @Override
    public void onClose() {
        hide();
    }

}
