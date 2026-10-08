package dataman.dmToolkit.overHeadLine.client;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.overHeadLine.shared.AccountHelp_Mdl;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Mdl;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_SrvAsync;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.ServiceInvoker;
import java.util.List;

public class OverHeadLineGridEdit<T1, T2> extends OverHeadLineGridEdit_Dsg<T1, T2> implements BlurHandler {

    interface CallBack {

        public void onOk(OverHeadLine_Mdl grdTdsEntry);

        public void onDelete();
    }

 private    OverHeadLine overHead;
    private CallBack callBack;
    private OverHeadLine_Mdl parentRow;

    private void textHelpEvents(final ServiceInvoker<OverHeadLine_SrvAsync> SrvAsync) {
        txtHPostingAC.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<AccountHelp_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHPostingAC(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<AccountHelp_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPostingAC).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<AccountHelp_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHPostingAC.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPostingAC).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, AccountHelp_Mdl t1Row) {
            }
        });

        txtAtRate.addBlurHandler(this);

        txtAmount.addBlurHandler(this);
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {
        super.showRelativeTo(currentRowIndex, 0, okNeeded, newNeeded, deleteNeeded);
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtAtRate) || event.getSource().equals(txtAmount)) {
            parentRow.setAtRate(DmClient.vNull(txtAtRate.getText(), "0.00"));
            parentRow.setAmount(DmClient.vNull(txtAmount.getText(), "0.00"));

            overHead.calculateSingleOH(parentRow, overHead.getItemValue());

            txtAtRate.setText(DmClient.vNull(parentRow.getAtRate(), "0.00"));
            txtAmount.setText(DmClient.vNull(parentRow.getAmount(), "0.00"));
        }
    }

    @Override
    public void displayRow(T1 row) {
        OverHeadLine_Mdl ourRow = (OverHeadLine_Mdl) row;
        parentRow = new OverHeadLine_Mdl();

        if (ourRow != null) {
            parentRow = ourRow.clone();
        }

        txtOverHead.setEnabled(false);
        txtOverHead.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));

        if (DmShared.vNull(ourRow.getOverHeadAliasEditable()).doubleValue() == 1) {
            txtOverHeadAlias.setEnabled(true);
        } else {
            txtOverHeadAlias.setEnabled(false);
        }
        txtOverHeadAlias.setText(ourRow.getOverHeadAlias());

        if (DmShared.vNull(ourRow.getAtRateMandatory()).doubleValue() == 1) {
            txtAtRate.setMandatory(true);
        } else {
            txtAtRate.setMandatory(false);
        }

        if (DmShared.vNull(ourRow.getAtRateEditable()).doubleValue() == 0) {
            txtAtRate.setEnabled(false);
        } else {
            txtAtRate.setEnabled(true);
        }
        txtAtRate.setText(ourRow.getAtRate());

        if (DmShared.vNull(ourRow.getAmountMandatory()).doubleValue() == 1) {
            txtAmount.setMandatory(true);
        } else {
            txtAmount.setMandatory(false);
        }
        if (DmShared.vNull(ourRow.getAmountEditable()).doubleValue() == 0) {
            txtAmount.setEnabled(false);
        } else {
            txtAmount.setEnabled(true);
        }
        txtAmount.setText(DmClient.vNull(ourRow.getAmount(), "0.00"));

        if (DmShared.vNull(ourRow.getAcEditable()).doubleValue() == 0) {
            txtHPostingAC.setEnabled(false);
        } else {
            txtHPostingAC.setEnabled(true);
        }
        txtHPostingAC.setText(DmShared.xNull(ourRow.getAccountName()));
        txtHPostingAC.setTag(DmShared.xNull(ourRow.getAccountCode()));

        if (txtAtRate.isEnabled()) {
            txtAtRate.setFocus(true);
        } else if (txtAmount.isEnabled()) {
            txtAmount.setFocus(true);
        } else {

        }
    }

    @Override
    public T1 getNewRow() {
        return (T1) new OverHeadLine_Mdl();
    }

    @Override
    public void onOk() {
        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        OverHeadLine_Mdl ourRow = (OverHeadLine_Mdl) row;

        ourRow.setOverHeadAlias(txtOverHeadAlias.getText());
        ourRow.setAtRate(txtAtRate.getText());
        ourRow.setAmount(txtAmount.getText());
        ourRow.setAccountName(txtHPostingAC.getText());
        ourRow.setAccountCode(txtHPostingAC.getTag());

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

    public OverHeadLineGridEdit(DmGrid<T1, T2> grid, OverHeadLine overHead) {
        super(grid, "Overhead", "335px", "115px");
        textHelpEvents(overHead.OHLHandler.srvAsync);
        this.overHead = overHead;
    }
}
