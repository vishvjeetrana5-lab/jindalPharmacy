package dataman.accounts.budgetAllocationEntry.client;

import dataman.accounts.budgetAllocationEntry.shared.GrdBudgetAllocationDetail_Mdl;
import dataman.accounts.budgetAllocationEntry.shared.TxtHBudgetHead_Mdl;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;
import dataman.accounts.budgetAllocationEntry.shared.BudgetAllocationEntry_Srv;
import dataman.accounts.budgetAllocationEntry.shared.BudgetAllocationEntry_SrvAsync;
import java.util.ArrayList;

import dataman.libraryMenu.self.shared.LibraryVoucherCategory;

public class BudgetAllocationEntryGridEdit<T1, T2> extends BudgetAllocationEntryGridEdit_Dsg<T1, T2> implements BlurHandler {

    private final LibraryVoucherCategory pvc = new LibraryVoucherCategory();

    interface CallBack {

        public void onOk(GrdBudgetAllocationDetail_Mdl grdIndentEntry);

        public void onDelete();
    }

    ServiceInvoker<BudgetAllocationEntry_SrvAsync> SrvAsync;
    CallBack callBack;
    GrdBudgetAllocationDetail_Mdl parentRow;
    private BudgetAllocationEntry bdtAllocation;
    String previousBudgetHeadCode = "", previousCostCenter = "", budgetDescription = "", voucherCategory = "";
    int hashCode;

    private void textHelpEvents() {
//        txtHBudgetHead.addKeyDownHandler(new DmTextHelp.TxtHelpKeyDownHandler() {
//            @Override
//            public void onKeyDown(KeyDownEvent event, Object objSource) {
//                if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
//                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
//                        @Override
//                        public void execute() {
//                            txtHCostcenter.setFocus(true);
//                        }
//                    });
//                }
//            }
//        });
//        txtHBudgetHeadAdjustment.addKeyDownHandler(new DmTextHelp.TxtHelpKeyDownHandler() {
//            @Override
//            public void onKeyDown(KeyDownEvent event, Object objSource) {
//                if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
//                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
//                        @Override
//                        public void execute() {
//                            txtBudget.setFocus(true);
//                        }
//                    });
//                }
//            }
//        });

        txtHCostcenter.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHCostCenter(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCostcenter).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHCostcenter.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCostcenter).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });

        txtHBudgetHead.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHBudgetHead_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHBudgetHead(voucherCategory, budgetDescription, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHBudgetHead_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHBudgetHead).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHBudgetHead_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHBudgetHead.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHBudgetHead).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHBudgetHead_Mdl t1Row) {
                if (t1Row != null) {
                    txtHBudgetHead.setTag2nd(DmShared.xNull(t1Row.getUnderHeadName()));
                    txtHCostcenter.setFocus(true);
                }
            }
        });
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
                    txtHBudgetHead.setTag(DmShared.xNull(t1Row.getBudgetHeadCode()));
                    txtHBudgetHead.setText(DmShared.xNull(t1Row.getBudgetHeadName()));
                    txtHBudgetHead.setTag2nd(DmShared.xNull(t1Row.getUnderHeadName()));
                    txtHCostcenter.setTag(DmShared.xNull(t1Row.getCostCenterTag()));
                    txtHCostcenter.setText(DmShared.xNull(t1Row.getCostCenterText()));
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

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtFromDate) {
            txtFromDate.setText(DmClient.returnDate(txtFromDate.getText()));
        }
        if (event.getSource() == txtToDate) {
            txtToDate.setText(DmClient.returnDate(txtToDate.getText()));
        }
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(String budgetDescription, String voucherCategory, int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {

        this.budgetDescription = budgetDescription;
        this.voucherCategory = voucherCategory;
        flwBlank.clear();
        if (voucherCategory.equalsIgnoreCase(pvc.BUDGET_ALLOCATION)) {
            flwBlank.add(budgetHead());
        } else {
            flwBlank.add(budgetHeadAdjustment());
        }
        super.showCenter(currentRowIndex, okNeeded, newNeeded, deleteNeeded);
        manageDisplay();
        if (voucherCategory.equalsIgnoreCase(pvc.BUDGET_ALLOCATION)) {
            txtHBudgetHead.setFocus(true);
        } else {
            txtHBudgetHeadAdjustment.setFocus(true);
        }

    }

    @Override
    public void displayRow(T1 row) {
        GrdBudgetAllocationDetail_Mdl ourRow = (GrdBudgetAllocationDetail_Mdl) row;
        parentRow = ourRow;
        hashCode = ourRow.getSharedKeyFieldHashCode();
        previousBudgetHeadCode = DmShared.xNull(ourRow.getBudgetHeadTag());
        txtHBudgetHead.setTag(DmShared.xNull(ourRow.getBudgetHeadTag()));
        txtHBudgetHead.setText(DmShared.xNull(ourRow.getBudgetHeadText()));
        txtHBudgetHeadAdjustment.setTag(DmShared.xNull(ourRow.getBudgetHeadTag()));
        txtHBudgetHeadAdjustment.setText(DmShared.xNull(ourRow.getBudgetHeadText()));
        previousCostCenter = DmShared.xNull(ourRow.getCostcenterTag());
        txtHCostcenter.setTag(DmShared.xNull(ourRow.getCostcenterTag()));
        txtHCostcenter.setText(DmShared.xNull(ourRow.getCostcenterText()));
        txtRemarks.setText(ourRow.getRemarks());
        txtBudget.setText(DmShared.xNull(ourRow.getBudget()));
        if (voucherCategory.equalsIgnoreCase(pvc.BUDGET_ALLOCATION) || voucherCategory.equalsIgnoreCase(pvc.BUDGET_ADDITION)) {
            txtHPlusMinus.setText("+");
            txtHPlusMinus.setTag("1");
        } else if (voucherCategory.equalsIgnoreCase(pvc.BUDGET_REVERSAL)) {
            txtHPlusMinus.setText("-");
            txtHPlusMinus.setTag("0");
        }
        if (!DmShared.xNull(ourRow.getFromDate()).trim().equalsIgnoreCase("")) {
            txtFromDate.setText(ourRow.getFromDate());
            txtToDate.setText(ourRow.getToDate());
        } else {
            txtFromDate.setText(bdtAllocation.txtFromDate.getText());
            txtToDate.setText(bdtAllocation.txtToDate.getText());
        }
        manageDisplay();
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdBudgetAllocationDetail_Mdl();
    }

    @Override
    public void onOk() {
        if (DmShared.xNull(txtHBudgetHead.getTag()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Budget Head is mandatory", txtHBudgetHead).show();
            return;
        }
        if (DmShared.vNull(txtBudget.getText()).doubleValue() == 0) {
            new DmAlertMessageBox("Amt/Qty is mandatory.", txtBudget).show();
            return;
        }

        if (DmShared.xNull(txtFromDate.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("From date mandatory", txtFromDate).show();
            return;
        }
        if (DmShared.xNull(txtToDate.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("To date mandatory", txtToDate).show();
            return;
        }
        if (DmShared.convertDate(txtFromDate.getText()).getTime() > DmShared.convertDate(txtToDate.getText()).getTime()) {
            new DmAlertMessageBox("From Date can't be greater than To Date", txtFromDate).show();
            return;
        }
        if (DmShared.xNull(txtHPlusMinus.getText()).equalsIgnoreCase("-")
                && DmShared.vNull(txtBudget.getText()).doubleValue() > DmShared.vNull(txtBudget.getTag()).doubleValue()) {
            new DmAlertMessageBox("Amount exceeded than balance budget. Balance budget is :" + DmClient.vNull(txtBudget.getTag(), "0.00"), txtBudget).show();
            return;
        }

        List<GrdBudgetAllocationDetail_Mdl> lstTemp = (List<GrdBudgetAllocationDetail_Mdl>) getDmGrid().getGrid().getStore().getAll();
        for (int i = 0; i < lstTemp.size(); i++) {
            GrdBudgetAllocationDetail_Mdl get = lstTemp.get(i);
            if (DmShared.xNull(txtHBudgetHead.getTag()).equalsIgnoreCase(DmShared.xNull(get.getSharedKeyFieldCode()))
                    && DmShared.xNull(txtHCostcenter.getTag()).equalsIgnoreCase(DmShared.xNull(get.getCostcenterTag()))) {
                if (DmShared.vNull(hashCode).doubleValue() != DmShared.vNull(get.getSharedKeyFieldHashCode()).doubleValue()) {
                    new DmAlertMessageBox("Combination of " + txtHBudgetHead.getText() + " and " + txtHCostcenter.getText() + " already exists.", txtHBudgetHead).show();
                    return;
                }
            }
        }

        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdBudgetAllocationDetail_Mdl ourRow = (GrdBudgetAllocationDetail_Mdl) row;

        ourRow = parentRow;
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHBudgetHead.getTag()));
        ourRow.setBudgetHeadTag(DmShared.xNull(txtHBudgetHead.getTag()));
        ourRow.setBudgetHeadText(DmShared.xNull(txtHBudgetHead.getText()));
        ourRow.setUnderHeadText(DmShared.xNull(txtHBudgetHead.getTag2nd()));
        ourRow.setCostcenterTag(DmShared.xNull(txtHCostcenter.getTag()));
        ourRow.setCostcenterText(DmShared.xNull(txtHCostcenter.getText()));
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

    public BudgetAllocationEntryGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Budget Head Detail", "400px", "150px");
        SrvAsync = new ServiceInvoker<>(GWT.create(BudgetAllocationEntry_Srv.class), BudgetAllocationEntry.class.getName());
        txtFromDate.addBlurHandler(this);
        txtToDate.addBlurHandler(this);
        textHelpEvents();
        manageDisplay();
        manageEvent();
    }

    private void manageEvent() {
    }

    private void manageDisplay() {
        if (voucherCategory.equalsIgnoreCase(pvc.BUDGET_TRANSFER)) {
            txtHPlusMinus.setEnabled(true);
        } else {
            txtHPlusMinus.setEnabled(false);
        }
        if (voucherCategory.equalsIgnoreCase(pvc.BUDGET_ALLOCATION)) {
            txtHCostcenter.setEnabled(true);
        } else {
//            txtHCostcenter.setEnabled(false);
        }
    }

}
