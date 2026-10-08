package aarogya.inventory.purchaseBillDirect.client;

import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;
import aarogya.inventory.purchaseBillDirect.shared.GrdLabEntry_Mdl;
import aarogya.inventory.purchaseBillDirect.shared.PurchaseBillDirect_Srv;
import aarogya.inventory.purchaseBillDirect.shared.PurchaseBillDirect_SrvAsync;
import aarogya.selfToolkit.stStatic.shared.STEnum;

public class LabEntryGridEdit<T1, T2> extends LabEntryGridEdit_Dsg<T1, T2> implements ClickHandler, AttachEvent.Handler {

    interface CallBack {

        public void onOk(GrdLabEntry_Mdl details_Mdl);

        public void onDelete();
    }

    ServiceInvoker<PurchaseBillDirect_SrvAsync> SrvAsync;
    CallBack callBack;
    GrdLabEntry_Mdl parentRow;
    int hashCode;
    LabImpurityPopup labImpurityPopup;
    String isBestBefore = "", previousItemCode = "", valueType = "", helpSource = "";
    boolean flagFromlblClick = false;

    private void textHelpEvents() {
        txtHHelp.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getCreateHelp(helpSource, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHHelp);

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHHelp.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHHelp);
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

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, LabImpurityPopup labImpurityPopup) {
        super.showCenter(currentRowIndex, okNeeded, newNeeded, deleteNeeded);
        this.labImpurityPopup = labImpurityPopup;
        manageDisplay();
    }

    @Override
    public void displayRow(T1 row) {
        GrdLabEntry_Mdl ourRow = (GrdLabEntry_Mdl) row;
        hashCode = ourRow.getSharedKeyFieldHashCode();

        txtItemImpurity.setTag2nd(ourRow.getvSNo());
        txtItemImpurity.setTag(ourRow.getSharedKeyFieldCode());
        txtItemImpurity.setText(ourRow.getSharedKeyFieldName());
        txtRange1.setText(ourRow.getRange1());
        txtRange2.setText(ourRow.getRange2());
        txtFormulaForQty.setText(ourRow.getFormulaForQty());
        txtFormulaForAmt.setText(ourRow.getFormulaForAmt());
        valueType = ourRow.getValueType();
        txtRangeUnit.setText(ourRow.getRangeUnit());
        txtRangeUnit1.setText(ourRow.getRangeUnit());
        txtParameterUnit.setText(ourRow.getRangeUnit());
        txtRangeTextBoxUnit.setText(ourRow.getRangeUnit());
        txtRemark.setText(ourRow.getDefaultRemark());
        txtlabQty.setText(DmShared.xNull(ourRow.getLabQty()));
        txtlabAmt.setText(DmShared.xNull(ourRow.getLabAmt()));

        if (DmShared.xNull(ourRow.getValueType()).equalsIgnoreCase(DmShared.iNull(STEnum.ImpurityValueType.HELP.getId()).toString())) {
            flwContainer.add(flwHelp);
            flwRangeContainer.remove(flwRange);
            flwContainer.remove(flwRemark);
            flwContainer.remove(flwRangeTextBox);

            helpSource = ourRow.getHelpSource();
            textHelpEvents();
            txtHHelp.setText(ourRow.getMainFoundValue());

        } else if (DmShared.xNull(ourRow.getValueType()).equalsIgnoreCase(DmShared.iNull(STEnum.ImpurityValueType.REMARK.getId()).toString())) {
            flwContainer.add(flwRemark);
            flwRangeContainer.remove(flwRange);
            flwContainer.remove(flwHelp);
            flwContainer.remove(flwRangeTextBox);
            txtRemark.setText((ourRow.getMainFoundValue()));

        } else if (DmShared.xNull(ourRow.getValueType()).equalsIgnoreCase(DmShared.iNull(STEnum.ImpurityValueType.RANGE.getId()).toString())) {
            flwContainer.add(flwRangeTextBox);
            flwRangeContainer.add(flwRange);
            flwContainer.remove(flwHelp);
            flwContainer.remove(flwRemark);
            txtRangeTextBox.setText((ourRow.getMainFoundValue()));

        }
        manageDisplay();
    }

    private void manageDisplay() {
        txtItemImpurity.setEnabled(false);
        txtRange1.setEnabled(false);
        txtRange2.setEnabled(false);
        txtHHelp.setEnabled(false);
        txtRemark.setEnabled(false);

        txtlabQty.setEnabled(false);
        txtlabAmt.setEnabled(false);

        txtRangeTextBox.setEnabled(false);
//        if (labImpurityPopup.purchaseBillDirectGridEdit.billDirectEntry.foundValueEditable) {
//            txtRangeTextBox.setEnabled(true);
//        } else {
//            txtRangeTextBox.setEnabled(false);
//        }
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdLabEntry_Mdl();
    }

    @Override
    public void onOk() {
        if (DmShared.xNull(txtItemImpurity.getTag()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Item impurity is mandatory.", txtItemImpurity).show();
            return;
        }

        if (DmShared.xNull(valueType).equalsIgnoreCase(DmShared.iNull(STEnum.ImpurityValueType.RANGE.getId()).toString())) {
            txtMainFoundValue.setText(txtRangeTextBox.getText());
        } else if (DmShared.xNull(valueType).equalsIgnoreCase(DmShared.iNull(STEnum.ImpurityValueType.HELP.getId()).toString())) {
            txtMainFoundValue.setText(txtHHelp.getText());
        } else if (DmShared.xNull(valueType).equalsIgnoreCase(DmShared.iNull(STEnum.ImpurityValueType.REMARK.getId()).toString())) {
            txtMainFoundValue.setText(txtRemark.getText());
        }

        if (DmShared.xNull(txtMainFoundValue.getText()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Found values are mandatory.", txtMainFoundValue).show();
            return;
        }

        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdLabEntry_Mdl ourRow = (GrdLabEntry_Mdl) row;

        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtItemImpurity.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtItemImpurity.getText()));
        ourRow.setvSNo(DmShared.xNull(txtItemImpurity.getTag2nd()));
        ourRow.setRange1(DmShared.xNull(txtRange1.getText()));
        ourRow.setRange2(DmShared.xNull(txtRange2.getText()));
        ourRow.setFormulaForQty(DmShared.xNull(txtFormulaForQty.getText()));
        ourRow.setFormulaForAmt(DmShared.xNull(txtFormulaForAmt.getText()));
        ourRow.setRangeUnit(DmShared.xNull(txtParameterUnit.getText()));
        ourRow.setHelpSource(DmShared.xNull(helpSource));
        ourRow.setLabQty(DmShared.xNull(txtlabQty.getText()));
        ourRow.setLabAmt(DmShared.xNull(txtlabAmt.getText()));

        ourRow.setMainFoundValue(DmShared.xNull(txtMainFoundValue.getText()));

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

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    public LabEntryGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Lab Impurity", "350px", "auto");
        SrvAsync = new ServiceInvoker<>(GWT.create(PurchaseBillDirect_Srv.class), PurchaseBillDirect.class.getName());
    }

}
