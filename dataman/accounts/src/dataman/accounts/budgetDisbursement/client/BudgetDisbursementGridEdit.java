package dataman.accounts.budgetDisbursement.client;

import dataman.accounts.budgetDisbursement.shared.GrdBudgetDisbursementDetail_Mdl;
import dataman.accounts.budgetDisbursement.shared.TxtHBudgetHead_Mdl;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;
import dataman.accounts.budgetDisbursement.shared.BudgetDisbursement_Srv;
import dataman.accounts.budgetDisbursement.shared.BudgetDisbursement_SrvAsync;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;

import dataman.libraryMenu.self.shared.LibraryVoucherCategory;

public class BudgetDisbursementGridEdit<T1, T2> extends BudgetDisbursementGridEdit_Dsg<T1, T2> implements BlurHandler {

    private final LibraryVoucherCategory pvc = new LibraryVoucherCategory();

    interface CallBack {

        public void onOk(GrdBudgetDisbursementDetail_Mdl grdIndentEntry);

        public void onDelete();
    }

    ServiceInvoker<BudgetDisbursement_SrvAsync> SrvAsync;
    CallBack callBack;
    GrdBudgetDisbursementDetail_Mdl parentRow;
    String previousBudgetHeadCode = "", previousCostCenter = "", budgetDescription = "", voucherCategory = "";
    int hashCode;

    private void textHelpEvents() {
        txtHBudgetHeadAdjustment.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHBudgetHead_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHBudgetHeadAdjustment(voucherCategory, budgetDescription, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHBudgetHead_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHBudgetHeadAdjustment).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHBudgetHead_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHBudgetHeadAdjustment.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHBudgetHeadAdjustment).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHBudgetHead_Mdl t1Row) {
                if (t1Row != null) {
                    txtHBudgetHeadAdjustment.setTag2nd(DmShared.xNull(t1Row.getBudgetHeadCode()));
                    txtCostCenter.setTag(DmShared.xNull(t1Row.getCostCenterTag()));
                    txtCostCenter.setText(DmShared.xNull(t1Row.getCostCenterText()));
                    txtBudget.setTag(DmClient.vNull(t1Row.getBalanceBudget(), "0.00"));
                    txtFromDate.setText(DmShared.xNull(t1Row.getFromDate()));
                    txtToDate.setText(DmShared.xNull(t1Row.getToDate()));
                    txtBudget.setFocus(true);
                }
            }
        });

        txtHPlusMinus.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHPlusMinus(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPlusMinus).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPlusMinus.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPlusMinus).show();
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

    public void show(String budgetDescription, String voucherCategory, int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {

        this.budgetDescription = budgetDescription;
        this.voucherCategory = voucherCategory;
        super.showCenter(currentRowIndex, okNeeded, newNeeded, deleteNeeded);
        manageDisplay();
        txtHBudgetHeadAdjustment.setFocus(true);
    }

    @Override
    public void displayRow(T1 row) {
        GrdBudgetDisbursementDetail_Mdl ourRow = (GrdBudgetDisbursementDetail_Mdl) row;
        parentRow = ourRow;
        hashCode = ourRow.getSharedKeyFieldHashCode();
        previousBudgetHeadCode = DmShared.xNull(ourRow.getBudgetHeadTag());
        txtHBudgetHeadAdjustment.setTag2nd(DmShared.xNull(ourRow.getBudgetHeadTag()));
        txtHBudgetHeadAdjustment.setText(DmShared.xNull(ourRow.getBudgetHeadText()));
        previousCostCenter = DmShared.xNull(ourRow.getCostcenterTag());
        txtCostCenter.setTag(DmShared.xNull(ourRow.getCostcenterTag()));
        txtCostCenter.setText(DmShared.xNull(ourRow.getCostcenterText()));
        txtRemarks.setText(ourRow.getRemarks());
        txtBudget.setText(DmShared.xNull(ourRow.getBudget()));

        txtHPlusMinus.setText("-");
        txtHPlusMinus.setTag("0");

        if (!DmShared.xNull(ourRow.getFromDate()).trim().equalsIgnoreCase("")) {
            txtFromDate.setText(ourRow.getFromDate());
            txtToDate.setText(ourRow.getToDate());
        }
        manageDisplay();
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdBudgetDisbursementDetail_Mdl();
    }

    @Override
    public void onOk() {
        if (DmShared.xNull(txtHBudgetHeadAdjustment.getTag()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Budget Head is mandatory", txtHBudgetHeadAdjustment).show();
            return;
        }
        if (DmShared.vNull(txtBudget.getText()).doubleValue() == 0) {
            new DmAlertMessageBox("Budget is mandatory.", txtBudget).show();
            return;
        }

        if (DmShared.xNull(txtHPlusMinus.getText()).equalsIgnoreCase("-")
                && DmShared.vNull(txtBudget.getText()).doubleValue() > DmShared.vNull(txtBudget.getTag()).doubleValue()) {
            new DmAlertMessageBox("Amount exceeded than balance budget. Balance budget is :" + DmClient.vNull(txtBudget.getTag(), "0.00"), txtBudget).show();
            return;
        }

        List<GrdBudgetDisbursementDetail_Mdl> lstTemp = (List<GrdBudgetDisbursementDetail_Mdl>) getDmGrid().getGrid().getStore().getAll();
        for (int i = 0; i < lstTemp.size(); i++) {
            GrdBudgetDisbursementDetail_Mdl get = lstTemp.get(i);
            if (DmShared.xNull(txtHBudgetHeadAdjustment.getTag()).equalsIgnoreCase(DmShared.xNull(get.getSharedKeyFieldCode()))
                    && DmShared.xNull(txtCostCenter.getTag()).equalsIgnoreCase(DmShared.xNull(get.getCostcenterTag()))) {
                if (DmShared.vNull(hashCode).doubleValue() != DmShared.vNull(get.getSharedKeyFieldHashCode()).doubleValue()) {
                    new DmAlertMessageBox("Combination of " + txtHBudgetHeadAdjustment.getText() + " and " + txtCostCenter.getText() + " already exists.", txtHBudgetHeadAdjustment).show();
                    return;
                }
            }
        }

        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdBudgetDisbursementDetail_Mdl ourRow = (GrdBudgetDisbursementDetail_Mdl) row;

        ourRow = parentRow;
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHBudgetHeadAdjustment.getTag()));
        ourRow.setBudgetHeadTag(txtHBudgetHeadAdjustment.getTag2nd());
        ourRow.setBudgetHeadText(DmShared.xNull(txtHBudgetHeadAdjustment.getText()));
        ourRow.setUnderHeadText(DmShared.xNull(txtHBudgetHeadAdjustment.getTag2nd()));
        ourRow.setCostcenterTag(DmShared.xNull(txtCostCenter.getTag()));
        ourRow.setCostcenterText(DmShared.xNull(txtCostCenter.getText()));
        ourRow.setFromDate(DmShared.xNull(txtFromDate.getText()));
        ourRow.setToDate(DmShared.xNull(txtToDate.getText()));
        ourRow.setPlusMinus(DmShared.xNull(txtHPlusMinus.getTag()));
        ourRow.setPlusMinusText(DmShared.xNull(txtHPlusMinus.getText()));
        ourRow.setBudget(DmShared.xNull(txtBudget.getText()));
        ourRow.setRemarks(DmShared.xNull(txtRemarks.getText()));
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

    public BudgetDisbursementGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Budget Head Detail", "400px", "150px");
        SrvAsync = new ServiceInvoker<>(GWT.create(BudgetDisbursement_Srv.class), BudgetDisbursement.class.getName());
        textHelpEvents();
        manageDisplay();
        manageEvent();
    }

    private void manageEvent() {
    }

    private void manageDisplay() {
        txtHPlusMinus.setEnabled(false);
        txtCostCenter.setEnabled(false);
        txtFromDate.setEnabled(false);
        txtToDate.setEnabled(false);
        txtBudget.addBlurHandler(this);
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtBudget) {
//            if (DmShared.xNull(txtHPlusMinus.getText()).equalsIgnoreCase("-")
//                    && DmShared.vNull(txtBudget.getText()).doubleValue() > DmShared.vNull(txtBudget.getTag()).doubleValue()) {
//                new DmConfirmMessageBox("Amount exceeded than balance budget. Balance budget is :" + DmClient.vNull(txtBudget.getTag(), "0.00") + "Want to continue?", new DialogHideEvent.DialogHideHandler() {
//                    @Override
//                    public void onDialogHide(DialogHideEvent event) {
//                        if (event.getHideButton() == Dialog.PredefinedButton.NO) {
//                            txtBudget.setFocus(true);
//                        }
//                    }
//                }).show();
//            }
        }

    }
}
