package aarogya.inventory.workBillDirect.client;

import aarogya.inventory.workBillDirect.shared.GrdImpurity_Mdl;
import aarogya.inventory.workBillDirect.shared.WorkBillDirect_Srv;
import aarogya.inventory.workBillDirect.shared.WorkBillDirect_SrvAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import java.util.List;

public class ImpurityGridEdit<T1, T2> extends ImpurityGridEdit_Dsg<T1, T2> implements BlurHandler {

    //<editor-fold defaultstate="collapsed" desc="Callback">
    interface CallBack {

        public void onOk(GrdImpurity_Mdl impurityMdl);

        public void onDelete();
    }
    private CallBack callBack;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
    //</editor-fold>

    private ServiceInvoker<WorkBillDirect_SrvAsync> srvAsync;
    private GrdImpurity_Mdl parentRow;
    private ImpurityPopup impurityPopup;
    private int hashCode = 0;

    public ImpurityGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Impurity", "300px", "");
        srvAsync = new ServiceInvoker<WorkBillDirect_SrvAsync>(GWT.create(WorkBillDirect_Srv.class), WorkBillDirect.class.getName());
        textHelpEvents();
        manageEvents();
        manageDisplay();
    }

    private void manageEvents() {
        txtAcceptPer.addBlurHandler(this);
        txtNetQty.addBlurHandler(this);
    }

    private void manageDisplay() {
        txtNetPer.setEnabled(false);
    }

    private void calculate() {
        txtSupplierQty.setText(DmClient.vNull(txtSupplierQty.getText(), DmShared.getNumberFormat(DmShared.iNull(impurityPopup.grnGridEdit.txtPurchaseUnit.getTag2nd()).intValue())));
        txtNetQty.setText(DmClient.vNull(txtNetQty.getText(), DmShared.getNumberFormat(DmShared.iNull(impurityPopup.grnGridEdit.txtPurchaseUnit.getTag2nd()).intValue())));
        txtNetPer.setText(DmClient.vNull(DmShared.vNull(txtFoundPer.getText()) - DmShared.vNull(txtAcceptPer.getText()), "0.00"));
        double netPer = DmShared.vNull(txtNetPer.getText());
        if (netPer > 0) {
            double recQty = DmShared.vNull(impurityPopup.grnGridEdit.txtReceiveQty.getText());
            double netQty = recQty * (netPer * 0.01);
            txtNetQty.setText(DmClient.vNull(netQty, DmShared.getNumberFormat(DmShared.iNull(impurityPopup.grnGridEdit.txtPurchaseUnit.getTag2nd()).intValue())));
            txtNetQty.setTag(txtNetQty.getText());
        }
    }

    private void textHelpEvents() {
        txtHImpurity.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHImpurity(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHImpurity).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHImpurity.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHImpurity).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, Object t1Row) {
            }
        });
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, ImpurityPopup impurityPopup) {
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
        this.impurityPopup = impurityPopup;
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtAcceptPer) {
            if (DmShared.vNull(txtAcceptPer.getText()) > DmShared.vNull(txtFoundPer.getText())) {
                new DmAlertMessageBox("Accept % cannot be greater than Found %.", txtAcceptPer).show();
            } else {
                calculate();
            }
        } else if (event.getSource() == txtNetQty) {
            if (DmShared.vNull(txtNetPer.getText()) != 0) {
                txtNetQty.setText(txtNetQty.getTag());
            }
        }
    }

    @Override
    public void onOk() {
        try {
            if (DmShared.iNull(txtHImpurity.getTag()).longValue() == 0) {
                new DmAlertMessageBox("Impurity is mandatory.", txtHImpurity).show();
                return;
            }
            if (DmShared.vNull(txtFoundPer.getText()) > 99.99) {
                new DmAlertMessageBox("Found % cannot be greater than 99.99.%", txtFoundPer).show();
                return;
            }
            if (DmShared.vNull(txtAcceptPer.getText()) > 99.99) {
                new DmAlertMessageBox("Accept % cannot be greater than 99.99.%", txtAcceptPer).show();
                return;
            }
            if (DmShared.vNull(txtNetPer.getText()) > 99.99) {
                new DmAlertMessageBox("Net % cannot be greater than 99.99.%", txtNetPer).show();
                return;
            }
            if (DmShared.vNull(txtNetQty.getText()) == 0) {
                new DmAlertMessageBox("Net Qty. is mandatory.", txtNetQty).show();
                return;
            }
            /*==============================<< Check Impurity dublicacy >>====================================*/
            for (GrdImpurity_Mdl grdIm : (List<GrdImpurity_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
                if (DmShared.xNull(txtHImpurity.getTag()).equalsIgnoreCase(DmShared.xNull(grdIm.getSharedKeyFieldCode()))) {
                    if (!DmClient.vNull(grdIm.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                        new DmAlertMessageBox(DmShared.xNull(grdIm.getSharedKeyFieldName()) + " already exists.", txtHImpurity).show();
                        return;
                    }
                }
            }
            proceedOk();
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtHImpurity).show();
        }
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
    public void manageOk(T1 row) {
        GrdImpurity_Mdl ourRow = new GrdImpurity_Mdl();

        calculate();
        ourRow = parentRow;

        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHImpurity.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHImpurity.getText()));
        ourRow.setFoundPer(DmShared.xNull(txtFoundPer.getText()));
        ourRow.setAcceptPer(DmShared.xNull(txtAcceptPer.getText()));
        ourRow.setNetPer(DmShared.xNull(txtNetPer.getText()));
        ourRow.setNetQty(DmShared.xNull(txtNetQty.getText()));
        ourRow.setSupplierQty(DmShared.xNull(txtSupplierQty.getText()));

        callBack.onOk(ourRow);
    }

    @Override
    public void manageDelete() {
        callBack.onDelete();
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdImpurity_Mdl();
    }

    @Override
    public void displayRow(T1 row) {
        GrdImpurity_Mdl ourRow = (GrdImpurity_Mdl) row;

        parentRow = ourRow;
        txtHImpurity.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHImpurity.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtFoundPer.setText(DmShared.xNull(ourRow.getFoundPer()));
        txtAcceptPer.setText(DmShared.xNull(ourRow.getAcceptPer()));
        txtNetPer.setText(DmShared.xNull(ourRow.getNetPer()));
        txtNetQty.setText(DmShared.xNull(ourRow.getNetQty()));
        txtSupplierQty.setText(DmShared.xNull(ourRow.getSupplierQty()));

        hashCode = ourRow.getSharedKeyFieldHashCode();

        txtHImpurity.setFocus(true);
    }

}
