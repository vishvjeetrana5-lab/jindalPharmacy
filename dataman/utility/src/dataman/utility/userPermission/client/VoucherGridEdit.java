package dataman.utility.userPermission.client;

import dataman.utility.userPermission.shared.GrdVoucher_Mdl;
import dataman.utility.userPermission.shared.TxtHVoucher_Mdl;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;
import dataman.utility.userPermission.shared.UserPermission_Srv;
import dataman.utility.userPermission.shared.UserPermission_SrvAsync;

public class VoucherGridEdit<T1, T2> extends VoucherGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(GrdVoucher_Mdl voucherGrid);

        public void onDelete();
    }

    private final ServiceInvoker<UserPermission_SrvAsync> SrvAsync;
    private CallBack callBack;
    private String userName = "";
    private String duplicateValue = "", role;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void textHelpEvent() {
        txtHAuditYN.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHAdministrator(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHAuditYN.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
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
        txtHVoucherGrid.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHVoucher_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHVoucher(helper, "", blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHVoucher_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHVoucher_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHVoucherGrid.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHVoucher_Mdl tRow) {
                if (blnStatus && tRow != null) {
                    txtVoucherCategory.setText(DmShared.xNull(tRow.getVoucherCategory()));
                    txtVoucherType.setText(DmShared.xNull(tRow.getVoucherType()));
                    txtVoucherDescription.setText(DmShared.xNull(tRow.getVoucherDescription()));
                } else {
                    txtVoucherCategory.setText("");
                    txtVoucherType.setText("");
                    txtVoucherDescription.setText("");
                }
            }
        });
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, String duplicateValue, String userName, String role) {
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
        this.userName = DmShared.xNull(userName);
        this.duplicateValue = DmShared.xNull(duplicateValue);
        this.role = role;
        txtHVoucherGrid.setFocus(true);
        txtHAuditYN.setText("No");
        txtHAuditYN.setTag("0");
    }

    @Override
    public void displayRow(T1 row) {
        GrdVoucher_Mdl ourRow = (GrdVoucher_Mdl) row;
        txtHVoucherGrid.setTag(ourRow.getSharedKeyFieldCode());
        txtHVoucherGrid.setText(ourRow.getSharedKeyFieldName());
        txtVoucherCategory.setText(ourRow.getCategory());
        txtVoucherType.setText(ourRow.getShortName());

        txtVoucherDescription.setText(ourRow.getSharedKeyFieldName());

        txtHAuditYN.setTag(ourRow.getAuditAllowCode());
        txtHAuditYN.setText(ourRow.getAuditAllowName());

        txtEntryForDays.setText(ourRow.getViewPrevious());
        txtSaveEntryForDays.setText(ourRow.getSavePrevious());
        txtDeleteEntryForDays.setText(ourRow.getDeletePrevious());
        txtAllowValuesForDays.setText(ourRow.getAllowValueUpto());
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdVoucher_Mdl();
    }

    @Override
    public void onOk() {
        if (!DmClient.isValidText(txtHVoucherGrid.getTag(), "Voucher", txtHVoucherGrid)) {
            return;
        }
        if (!txtHVoucherGrid.getTag().equalsIgnoreCase(this.duplicateValue)) {
            List<GrdVoucher_Mdl> lstTemp = (List<GrdVoucher_Mdl>) getDmGrid().getGrid().getStore().getAll();
            for (int i = 0; i < lstTemp.size(); i++) {
                if (DmShared.xNull(lstTemp.get(i).getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(txtHVoucherGrid.getTag()))) {
                    new DmAlertMessageBox("Voucher Already Exist.", txtHVoucherGrid).show();
                    return;
                }
            }
        }
        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdVoucher_Mdl ourRow = (GrdVoucher_Mdl) row;
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHVoucherGrid.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtVoucherDescription.getText()));
        ourRow.setCategory(DmShared.xNull(txtVoucherCategory.getText()));
        ourRow.setShortName(DmShared.xNull(txtVoucherType.getText()));

        ourRow.setAuditAllowCode(DmShared.xNull(txtHAuditYN.getTag()));
        ourRow.setAuditAllowName(DmShared.xNull(txtHAuditYN.getText()));

        ourRow.setViewPrevious(DmShared.xNull(txtEntryForDays.getText()));
        ourRow.setSavePrevious(DmShared.xNull(txtSaveEntryForDays.getText()));
        ourRow.setDeletePrevious(DmShared.xNull(txtDeleteEntryForDays.getText()));
        ourRow.setAllowValueUpto(DmShared.xNull(txtAllowValuesForDays.getText()));
        saveVoucherPermission(ourRow);
    }

    public VoucherGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Voucher", "350px", "135px");
        SrvAsync = new ServiceInvoker<UserPermission_SrvAsync>(GWT.create(UserPermission_Srv.class), UserPermission.class.getName());
        textHelpEvent();
    }

    protected void saveVoucherPermission(final GrdVoucher_Mdl ourRow) {
        SrvAsync.invokeRPC().saveVoucherPermission(ourRow, DmShared.xNull(userName), DmShared.xNull(role), new AsyncCallback<DmResult<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    callBack.onOk(ourRow);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    protected void deleteVoucherPermission() {
        SrvAsync.invokeRPC().deleteVoucherPermission(DmShared.xNull(userName), DmShared.xNull(txtVoucherType.getText()), DmShared.xNull(role), new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    proceedDelete();
                } else {
                    new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
                }
            }
        });
    }

    @Override
    public void onNew() {
        proceedNew();
    }

    @Override
    public void onDelete() {
        new DmConfirmMessageBox("Are you sure? You want to delete this record.", new DialogHideEvent.DialogHideHandler() {
            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                    deleteVoucherPermission();
                }
            }
        }).show();

    }

    @Override
    public void onClose() {
        hide();
    }

    @Override
    public void manageDelete() {
        callBack.onDelete();
    }
}
