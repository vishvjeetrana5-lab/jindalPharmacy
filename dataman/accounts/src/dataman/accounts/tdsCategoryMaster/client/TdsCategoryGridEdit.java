package dataman.accounts.tdsCategoryMaster.client;

import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import dataman.accounts.tdsCategoryMaster.shared.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;
import java.util.Objects;

public class TdsCategoryGridEdit<T1, T2> extends TdsCategoryGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(GrdTdsCategory_Mdl grdTdsEntry);

        public void onDelete();
    }

    
    ServiceInvoker<TdsCategoryMaster_SrvAsync> SrvAsync;
    CallBack callBack;
    GrdTdsCategory_Mdl parentRow;
    int intHashCode;
    String initialTurnoverAmt;

    //<editor-fold defaultstate="collapsed" desc="events">
    private void textHelpEvents() {
        txtHDescription.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHDescription(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDescription).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDescription.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDescription).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (blnStatus && t1Row != null) {
                    txtManualCode.setText(DmShared.xNull(t1Row.getSharedModifiedBy()));
                } else {
                    txtManualCode.setText("");
                }

            }

        });
        txtHPostingAC.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHPostingAC_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHPostingAC(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHPostingAC_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPostingAC).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHPostingAC_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHPostingAC.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPostingAC).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHPostingAC_Mdl t1Row) {
            }
        });
    }

    public void onBtnClick(ClickEvent event) {

    }

    private void handlerBtnClick(Button[] btn) {
        for (int i = 0; i < btn.length; i++) {
            btn[i].addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    onBtnClick(event);
                }
            });
        }
    }
    //</editor-fold>

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded,String initialTurnoverAmt) {
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
        this.initialTurnoverAmt=initialTurnoverAmt;
    }

    @Override
    public void displayRow(T1 row) {
        GrdTdsCategory_Mdl ourRow = (GrdTdsCategory_Mdl) row;
        parentRow = new GrdTdsCategory_Mdl();
        intHashCode = ourRow.hashCode();

        txtHDescription.setTag(ourRow.getDescriptionCode());
        txtHDescription.setText(ourRow.getDescription());
        txtManualCode.setText(ourRow.getManualCode());

        txtHPostingAC.setTag(ourRow.getPostingAccountCode());
        txtHPostingAC.setText(ourRow.getPostingAccount());

        txtPercent.setText(ourRow.getPercent());
        txtCalculateOn.setText(ourRow.getCalculateOn());

        txtFromAmt.setText(ourRow.getFromAmt());
        txtToAmt.setText(ourRow.getToAmt());
        txtPenaltyPercentage.setText(ourRow.getPenalityPercentage());

        txtHDescription.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdTdsCategory_Mdl();
    }

    @Override
    public void onOk() {
        boolean flag = true;
        Double nodeFromAmt = DmShared.vNull(DmClient.vNull(txtFromAmt.getText(), "0.00")).doubleValue();
        Double nodeToAmt = DmShared.vNull(DmClient.vNull(txtToAmt.getText(), "0.00")).doubleValue();
        if (!DmClient.isValidText(txtHDescription.getText(), "Description", txtHDescription)) {
            return;
        }
        if (!DmClient.isValidText(txtHPostingAC.getText(), "Posting A/c", txtHPostingAC)) {
            return;
        }
        if (!DmClient.isValidAmount(txtPenaltyPercentage.getText(), "Penality %", txtPenaltyPercentage)) {
            return;
        }
        if (!DmClient.isValidAmount(txtPercent.getText(), "Percentage %", txtPercent)) {
            return;
        }
        if (nodeToAmt <= nodeFromAmt) {
            new DmAlertMessageBox("'To Amount' can't be greater than or equal to 'From Amount'", txtHDescription).show();
            return;
        }

        List<GrdTdsCategory_Mdl> lstTemp = (List<GrdTdsCategory_Mdl>) getDmGrid().getGrid().getStore().getAll();
        for (int i = 0; i < lstTemp.size(); i++) {
            GrdTdsCategory_Mdl grdTdsCategory = lstTemp.get(i);
            if (!Objects.equals(intHashCode, grdTdsCategory.getSharedKeyFieldHashCode())) {

                //Condition For Check Duplicacy of List Items
                if (DmShared.xNull(lstTemp.get(i).getDescription()).equalsIgnoreCase(DmShared.xNull(txtHDescription.getText()))) {

                    Double grdFromAmt = DmShared.vNull(DmClient.vNull(lstTemp.get(i).getFromAmt(), "0.00"));
                    Double grdToAmt = DmShared.vNull(DmClient.vNull(lstTemp.get(i).getToAmt(), "0.00"));
                    try {
                        checkSlabDuplicateAmt(grdFromAmt, grdToAmt, lstTemp.get(i).getDescription());

                    } catch (DmException e) {
                        flag = false;
                        new DmAlertMessageBox(e.getMessage(), txtFromAmt).show();

                    }
                }
            }
        }
        if (flag) {
            proceedOk();
        }

    }

    private void checkSlabDuplicateAmt(double grdFromAmt, double grdToAmt, String desc) throws DmException {
        Double nodeFromAmt = DmShared.vNull(txtFromAmt.getText());
        Double nodeToAmt = Double.parseDouble(DmClient.vNull(txtToAmt.getText(), "0.00"));

        if (nodeFromAmt >= grdFromAmt && nodeFromAmt <= grdToAmt
                || nodeToAmt >= grdFromAmt && nodeToAmt <= grdToAmt
                || grdFromAmt >= nodeFromAmt && grdFromAmt <= nodeToAmt
                || grdToAmt >= nodeFromAmt && grdToAmt <= nodeToAmt) {
            throw new DmException(0, "Slab conflicts with other " + desc + " slab.");
        }
    }

    @Override
    public void manageOk(T1 row) {
        GrdTdsCategory_Mdl ourRow = (GrdTdsCategory_Mdl) row;

        ourRow.setDescriptionCode(txtHDescription.getTag());
        ourRow.setDescription(txtHDescription.getText());
        ourRow.setManualCode(txtManualCode.getText());

        ourRow.setPostingAccountCode(txtHPostingAC.getTag());
        ourRow.setPostingAccount(txtHPostingAC.getText());

        ourRow.setPercent(txtPercent.getText());
        ourRow.setCalculateOn(txtCalculateOn.getText());

        ourRow.setFromAmt(txtFromAmt.getText());
        ourRow.setToAmt(txtToAmt.getText());
        ourRow.setPenalityPercentage(txtPenaltyPercentage.getText());
        
        ourRow.setInitialTurnoverAmount(initialTurnoverAmt);

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

    public TdsCategoryGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Category Detail", "500px", "165px");
        SrvAsync = new ServiceInvoker<>(GWT.create(TdsCategoryMaster_Srv.class), TdsCategoryMaster.class.getName());
        txtManualCode.setEnabled(false);
        textHelpEvents();
    }
}
