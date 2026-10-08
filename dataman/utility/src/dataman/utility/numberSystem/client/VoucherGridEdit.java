package dataman.utility.numberSystem.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.utility.numberSystem.shared.NumberSystem_SrvAsync;
import dataman.utility.numberSystem.shared.NumberSystem_Srv;
import dataman.utility.numberSystem.shared.TxtHVoucherType_Mdl;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public class VoucherGridEdit<T1, T2> extends VoucherGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(KeyFieldExtended linkedUserGrid);

        public void onDelete();
    }

    ServiceInvoker<NumberSystem_SrvAsync> SrvAsync;
    CallBack callBack;
    int hashCode;
    String vTypeCode = "";

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, String voucherTypeCode) {
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
        txtHVoucherType.setFocus(true);
        vTypeCode = voucherTypeCode;
    }

    @Override
    public void displayRow(T1 row) {
        KeyFieldExtended ourRow = (KeyFieldExtended) row;
        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtHVoucherType.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHVoucherType.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));

    }

    @Override
    public T1 getNewRow() {
        return (T1) new KeyFieldExtended();
    }

    @Override
    public void onOk() {
        if (!DmClient.isValidText(txtHVoucherType.getTag(), "Voucher Type", txtHVoucherType)) {
            return;
        }
        for (KeyFieldExtended keyNode : (List<KeyFieldExtended>) getDmGrid().getGrid().getStore().getAll()) {
            if (hashCode != keyNode.getSharedKeyFieldHashCode()) {
                if (DmShared.xNull(txtHVoucherType.getTag()).equalsIgnoreCase(DmShared.xNull(keyNode.getSharedKeyFieldCode()))) {
                    new DmAlertMessageBox("Voucher Type is already exists.", txtHVoucherType).show();
                    return;
                }
            }
        }
        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        KeyFieldExtended ourRow = (KeyFieldExtended) row;
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHVoucherType.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHVoucherType.getText()));
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

    public void textHelpEvent() {
        txtHVoucherType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHVoucherType_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHVoucherType(vTypeCode, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHVoucherType_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHVoucherType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHVoucherType_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHVoucherType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHVoucherType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHVoucherType_Mdl tRow) {
                if (blnStatus) {

                }
            }
        });

    }

    public VoucherGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Include More Type", "400px", "60px");
        SrvAsync = new ServiceInvoker<NumberSystem_SrvAsync>(GWT.create(NumberSystem_Srv.class), NumberSystem.class.getName());
        textHelpEvent();
    }
}
