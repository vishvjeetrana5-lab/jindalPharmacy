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

public class ItemCategoryGridEdit<T1, T2> extends ItemCategoryGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(KeyFieldExtended costCenterGrid);

        public void onDelete();
    }
    ServiceInvoker<VoucherType_SrvAsync> SrvAsync;
    CallBack callBack;

    private void textHelpEvents() {
        txtHItemCategory.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHItemCategory(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHItemCategory).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHItemCategory.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHItemCategory).show();
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
    }

    @Override
    public void displayRow(T1 row) {
        KeyFieldExtended ourRow = (KeyFieldExtended) row;
        txtHItemCategory.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHItemCategory.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtHItemCategory.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new KeyFieldExtended();
    }

    @Override
    public void onOk() {
        if (!DmClient.isValidText(txtHItemCategory.getTag(), "Item Cat. Filteration", txtHItemCategory)) {
            return;
        }

        List<KeyFieldExtended> lstTemp = (List<KeyFieldExtended>) getDmGrid().getGrid().getStore().getAll();

        for (int i = 0; i < lstTemp.size(); i++) {
            KeyFieldExtended get = lstTemp.get(i);
            if (DmShared.xNull(txtHItemCategory.getTag()).equalsIgnoreCase(DmShared.xNull(get.getSharedKeyFieldCode()))) {
//                if (DmShared.vNull(hashCode).doubleValue() != DmShared.vNull(get.getSharedKeyFieldHashCode()).doubleValue()) {
                new DmAlertMessageBox(txtHItemCategory.getText() + " already exists.", txtHItemCategory).show();
                return;
//                }
            }
        }

        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        KeyFieldExtended ourRow = (KeyFieldExtended) row;
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHItemCategory.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHItemCategory.getText()));
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

    public ItemCategoryGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Item Cat. Filteration", "390px", "50px");
        SrvAsync = new ServiceInvoker<VoucherType_SrvAsync>(GWT.create(VoucherType_Srv.class), VoucherType.class.getName());
        textHelpEvents();
    }

}
