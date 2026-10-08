package aarogya.master.receiptBookMapping.client;

import aarogya.master.receiptBookMapping.shared.GrdReceiptMapping_Mdl;
import aarogya.master.receiptBookMapping.shared.ReceiptBookMapping_Srv;
import aarogya.master.receiptBookMapping.shared.ReceiptBookMapping_SrvAsync;
import aarogya.master.receiptBookMapping.shared.TxtHVoucherType_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public class ReceiptMappingGridEdit<T1, T2> extends ReceiptMappingGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(GrdReceiptMapping_Mdl grdTdsEntry);

        public void onDelete();
    }

    private final ServiceInvoker<ReceiptBookMapping_SrvAsync> srvAsync;
    private CallBack callBack;
    private GrdReceiptMapping_Mdl parentRow;
    private String entryPoint;
    private int hashCode;

    private void textHelpEvents() {
        txtHReceiptVoucher.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHVoucherType_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHReceiptVoucher(helper, blnToEndOfFile, DmShared.xNull(entryPoint), new AsyncCallback<DmResultPageing<List<TxtHVoucherType_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHReceiptVoucher).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHVoucherType_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHReceiptVoucher.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHReceiptVoucher).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHVoucherType_Mdl t1Row) {
                if (blnStatus && t1Row != null) {
                    txtHReceiptVoucher.setText(DmShared.xNull(t1Row.getSharedKeyFieldName()) + "(" + DmShared.xNull(t1Row.getShortName()) + ")");
                }
            }
        });
        txtHRefundVoucher.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHVoucherType_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHRefundVoucher(helper, blnToEndOfFile, DmShared.xNull(entryPoint), new AsyncCallback<DmResultPageing<List<TxtHVoucherType_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHRefundVoucher).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHVoucherType_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHRefundVoucher.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHRefundVoucher).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHVoucherType_Mdl t1Row) {
                if (blnStatus && t1Row != null) {
                    txtHRefundVoucher.setText(DmShared.xNull(t1Row.getSharedKeyFieldName()) + "(" + DmShared.xNull(t1Row.getShortName()) + ")");
                }
            }
        });
        txtHIsDefault.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHIsDefault(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, String entryPoint) {
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
        this.entryPoint = entryPoint;
        txtHReceiptVoucher.setFocus(true);
    }

    @Override
    public void displayRow(T1 row) {
        GrdReceiptMapping_Mdl ourRow = (GrdReceiptMapping_Mdl) row;
        parentRow = new GrdReceiptMapping_Mdl();
        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtEntryVoucher.setTag(ourRow.getSharedKeyFieldCode());
        txtEntryVoucher.setText(ourRow.getSharedKeyFieldName());
        txtHReceiptVoucher.setTag(ourRow.getReceiptVoucherCode());
        txtHReceiptVoucher.setText(DmShared.xNull(ourRow.getReceiptVoucherName()));
        txtHRefundVoucher.setTag(ourRow.getRefundVoucherCode());
        txtHRefundVoucher.setText(DmShared.xNull(ourRow.getRefundVoucherName()));
        txtHIsDefault.setTag(ourRow.getIsDefaultCode());
        txtHIsDefault.setText(ourRow.getIsDefaultName());
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdReceiptMapping_Mdl();
    }

    @Override
    public void onOk() {
        if (DmShared.xNull(txtHIsDefault.getText()).trim().equalsIgnoreCase("")) {
            txtHIsDefault.setFocus(true);
            return;
        }
        if (DmShared.xNull(txtHIsDefault.getTag()).equalsIgnoreCase("1")) {
            List<GrdReceiptMapping_Mdl> lstTemp = (List<GrdReceiptMapping_Mdl>) getDmGrid().getGrid().getStore().getAll();
            for (int i = 0; i < lstTemp.size(); i++) {
                GrdReceiptMapping_Mdl get = lstTemp.get(i);
                if (hashCode != get.getSharedKeyFieldHashCode()) {
                    if (DmShared.xNull(txtHIsDefault.getTag()).equalsIgnoreCase(DmShared.xNull(get.getIsDefaultCode()))) {
                        new DmAlertMessageBox(txtHIsDefault.getText() + " already exists.", txtHIsDefault).show();
                        return;
                    }
                }
            }
        }
        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdReceiptMapping_Mdl ourRow = (GrdReceiptMapping_Mdl) row;

        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtEntryVoucher.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtEntryVoucher.getText()));

        ourRow.setReceiptVoucherCode(DmShared.xNull(txtHReceiptVoucher.getTag()));
        ourRow.setReceiptVoucherName(DmShared.xNull(txtHReceiptVoucher.getText()));
        
        ourRow.setRefundVoucherCode(DmShared.xNull(txtHRefundVoucher.getTag()));
        ourRow.setRefundVoucherName(DmShared.xNull(txtHRefundVoucher.getText()));

        ourRow.setIsDefaultCode(DmShared.xNull(txtHIsDefault.getTag()));
        ourRow.setIsDefaultName(DmShared.xNull(txtHIsDefault.getText()));

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

    public ReceiptMappingGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Mapping Detail", "400px", "130px");
        srvAsync = new ServiceInvoker<>(GWT.create(ReceiptBookMapping_Srv.class), ReceiptBookMapping.class.getName());
        textHelpEvents();
        txtEntryVoucher.setEnabled(false);

    }
}
