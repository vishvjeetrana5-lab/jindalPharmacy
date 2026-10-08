package aarogya.inventory.itemDepartmentInfo.client;

import aarogya.inventory.itemDepartmentInfo.shared.*;
import aarogya.selfToolkit.stStatic.shared.STShared;
import dataman.dmBase.self.shared.*;
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

public class DepartmentInfoDetailsGridEdit<T1, T2> extends DepartmentInfoDetailsGridEdit_Dsg<T1, T2> implements BlurHandler {

    interface CallBack {

        public void onOk(GrdItemDepartmentInfo_Mdl gridItem_Mdl);

        public void onDelete();
    }

    private ServiceInvoker<ItemDepartmentInfo_SrvAsync> SrvAsync;
    private CallBack callBack;
    private int hashCode = 0;
    private ItemDepartmentInfo itemDepartmentInfo;
    private String isHoldReq;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;

    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, ItemDepartmentInfo obj) {
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
        this.itemDepartmentInfo = obj;

    }

    private void textHelpEvents() {
        txtHItemName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHItem_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHItemName(itemDepartmentInfo.txtHSiteName.getTag(), itemDepartmentInfo.txtHDepartment.getTag(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHItem_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHItemName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHItem_Mdl>> result) {

                        if (result.isStatus()) {
                            txtHItemName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHItemName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHItem_Mdl t1Row) {

                if (blnStatus && t1Row != null) {
                    isHoldReq = t1Row.getIsHoldRequirement();
                    txtUnit.setText(DmShared.xNull(t1Row.getSkuName()));
                    txtUnit.setTag(DmShared.xNull(t1Row.getAllowDecimalPlaces()));
                    calculate();
                } else {
                    txtUnit.setText("");
                    txtUnit.setTag("");
                }
            }

        });
    }

    @Override
    public void displayRow(T1 row) {
        clear();
        GrdItemDepartmentInfo_Mdl ourRow = (GrdItemDepartmentInfo_Mdl) row;

        txtMinLvl.setText(DmShared.xNull(ourRow.getMinStock()));
        txtMaxLvl.setText(DmShared.xNull(ourRow.getMaxStock()));
        txtReOrder.setText(DmShared.xNull(ourRow.getReOrder()));
        txtBin.setText(DmShared.xNull(ourRow.getBinName()));
        txtHItemName.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtHItemName.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtUnit.setText(DmShared.xNull(ourRow.getSkuName()));
        txtUnit.setTag(DmShared.xNull(ourRow.getAllowDecimalPlace()));
        calculate();
        hashCode = ourRow.getSharedKeyFieldHashCode();

        txtHItemName.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdItemDepartmentInfo_Mdl();
    }

    @Override
    public void onOk() {

        try {
            calculate();

            if (DmShared.xNull(itemDepartmentInfo.txtHSiteName.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Site Name is mandatory.", itemDepartmentInfo.txtHSiteName).show();
                return;
            }

            if (DmShared.xNull(itemDepartmentInfo.txtHDepartment.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Department is mandatory.", itemDepartmentInfo.txtHDepartment).show();
                return;
            }

            if (DmShared.xNull(txtHItemName.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Item Name is mandatory.", txtHItemName).show();
                return;
            }

            if (DmShared.vNull(txtMaxLvl.getText()).doubleValue() < DmShared.vNull(txtMinLvl.getText()).doubleValue()) {
                new DmAlertMessageBox("Min Level value must be less than or equal to Max Level value.", txtMinLvl).show();
                return;
            }

            if ((DmShared.vNull(txtReOrder.getText()).doubleValue() < DmShared.vNull(txtMinLvl.getText()).doubleValue()) || (DmShared.vNull(txtReOrder.getText()).doubleValue() > DmShared.vNull(txtMaxLvl.getText()).doubleValue())) {
                throw new DmException(600, "Re-Order Level cannot is greater than  Max. Level or less than Min. Level");
            }

            for (GrdItemDepartmentInfo_Mdl config_Mdl : (List<GrdItemDepartmentInfo_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
                if (DmShared.xNull(txtHItemName.getTag()).equalsIgnoreCase(DmShared.xNull(config_Mdl.getSharedKeyFieldCode()))) {
                    if (DmShared.vNull(hashCode).doubleValue() != DmShared.vNull(config_Mdl.getSharedKeyFieldHashCode()).doubleValue()) {
                        new DmAlertMessageBox("Item Name already exist.", txtHItemName).show();
                        return;
                    }
                }

            }
            proceedOk();

        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtHItemName).show();
        }
    }

    @Override
    public void manageOk(T1 row) {
        GrdItemDepartmentInfo_Mdl ourRow = (GrdItemDepartmentInfo_Mdl) row;

        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHItemName.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHItemName.getText()));
        ourRow.setMinStock(DmShared.xNull(txtMinLvl.getText()));
        ourRow.setMaxStock(DmShared.xNull(txtMaxLvl.getText()));
        ourRow.setReOrder(DmShared.xNull(txtReOrder.getText()));
        ourRow.setBinName(DmShared.xNull(txtBin.getText()));
        ourRow.setIsHoldRequirement(DmShared.xNull(isHoldReq));

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

    public DepartmentInfoDetailsGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Grid Edit", "600px", "127px");
        SrvAsync = new ServiceInvoker<ItemDepartmentInfo_SrvAsync>(GWT.create(ItemDepartmentInfo_Srv.class), ItemDepartmentInfo.class.getName());
        textHelpEvents();
        handlerEvents();

    }

    private void handlerEvents() {
        txtMinLvl.addBlurHandler(this);
        txtMaxLvl.addBlurHandler(this);
        txtReOrder.addBlurHandler(this);
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtMinLvl) || event.getSource().equals(txtMaxLvl) || event.getSource().equals(txtReOrder)) {
            calculate();
        }
    }

    private void calculate() {
        txtMinLvl.setText(DmClient.vNull(DmShared.vNull(txtMinLvl.getText()).doubleValue(), DmShared.getNumberFormat(DmShared.iNull(txtUnit.getTag()).intValue())));
        txtMaxLvl.setText(DmClient.vNull(DmShared.vNull(txtMaxLvl.getText()).doubleValue(), DmShared.getNumberFormat(DmShared.iNull(txtUnit.getTag()).intValue())));
        txtReOrder.setText(DmClient.vNull(DmShared.vNull(txtReOrder.getText()).doubleValue(), DmShared.getNumberFormat(DmShared.iNull(txtUnit.getTag()).intValue())));
    }

    private void clear() {
        txtHItemName.setTag("");
        txtHItemName.setText("");
        txtMinLvl.setText("");
        txtMaxLvl.setText("");
        txtReOrder.setText("");
        txtBin.setText("");
        txtUnit.setText("");

    }
}
