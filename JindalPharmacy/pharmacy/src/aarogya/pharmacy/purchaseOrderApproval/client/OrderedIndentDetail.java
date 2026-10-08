package aarogya.pharmacy.purchaseOrderApproval.client;

import aarogya.pharmacy.purchaseOrderApproval.shared.*;
import aarogya.projectMenu.self.shared.pmPharmacy;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import java.util.List;

public class OrderedIndentDetail<T1, T2> extends OrderedIndentDetail_Dsg<T1, T2> implements BlurHandler, KeyUpHandler {

    double addVar;
    String docId, vSno;

    interface GridEditCallBack {

        public void onOk(GrdMedicineDetail_Mdl approvalEntry_Mdl);
    }
    private final UserEntryPOGridEdit<GrdPurchaseOrderDetail_Mdl, GrdPurchaseOrdDet_PrpAcs> userEntryPOGrd = new UserEntryPOGridEdit(grdPurchaseDet);

    GridEditCallBack gridEditCallBack;
    ServiceInvoker<PurchaseApproval_SrvAsync> SrvAsync;

    private CallBack callBack;
    private PurchaseOrderApprovalGridEdit purchaseOrdAppGE;
    private int hashCode = 0;

    GrdMedicineDetail_Mdl grdPurchaseOrderD_Mdl = new GrdMedicineDetail_Mdl();

    public OrderedIndentDetail(DmGrid<T1, T2> grid) {
        super(grid, "Ordered Indent Detail", "356px", "100px");
        SrvAsync = new ServiceInvoker<>(GWT.create(PurchaseApproval_Srv.class), PurchaseOrderApproval.class.getName());
        // textHelpEvents();
        // handleEvents();
        gridEvents();
        txtItem.setEnabled(false);
        txtReceivedQty.setEnabled(false);
    }

    public void calculate() {

        addVar = 0;
        for (GrdPurchaseOrderDetail_Mdl grdPurchD : grdPurchaseDet.getGrid().getStore().getAll()) {
            if (!DmShared.xNull(grdPurchD.getOrderInPurchaseOrder()).equalsIgnoreCase("")) {
                addVar += DmShared.vNull(grdPurchD.getOrderInPurchaseOrder());
            }
        }

    }

    @Override
    public void onOk() {
        try {

//            if (DmShared.xNull(txtsubject.getText()).equals("")) {
//                new DmAlertMessageBox("Subject is mandatory.", txtsubject).show();
//                return;
//            }
            calculate();
            double rec = DmShared.iNull(txtReceivedQty.getText()).doubleValue();

            if (addVar != rec) {
                new DmAlertMessageBox("Ordered Qty should be equal to Received Qty", txtReceivedQty).show();
                return;
            }
            proceedOk();
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtItem).show();
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
        GrdMedicineDetail_Mdl ourRow = (GrdMedicineDetail_Mdl) row;

        ourRow.setMedName(DmShared.xNull(txtItem.getText()));
        ourRow.setChallanQty(DmShared.xNull(txtReceivedQty.getText()));
        ourRow.setIsStatus("1");
        ourRow.getGrdPurchaseDetailList().clear();
        ourRow.getGrdPurchaseDetailList().addAll(grdPurchaseDet.getGrid().getStore().getAll());
        callBack.onOk(ourRow);
    }

    @Override
    public void manageDelete() {
        callBack.onDelete();
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdMedicineDetail_Mdl();
    }

    @Override
    public void displayRow(T1 row) {
        GrdMedicineDetail_Mdl ourRow = (GrdMedicineDetail_Mdl) row;

        txtItem.setText(DmShared.xNull(ourRow.getMedName()));
        txtReceivedQty.setText(DmShared.xNull(ourRow.getChallanQty()));
        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtItem.setFocus(true);
    }

    @Override
    public void onBlur(BlurEvent event) {
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, final boolean isDeleteFocus, final PurchaseOrderApprovalGridEdit purchaseOrdAppGE, String docId, String vSno) {
        this.purchaseOrdAppGE = purchaseOrdAppGE;
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
        fillTextFieldOFOrderedIndentDetail(docId, vSno, grdPurchaseOrderD_Mdl);
        this.docId = docId;
        this.vSno = vSno;
    }

    private void fillTextFieldOFOrderedIndentDetail(String docId, String vSNo, GrdMedicineDetail_Mdl gMdl) {

        try {
            SrvAsync.invokeRPC().fillOrdIndentDetail(docId, vSNo, gMdl, new AsyncCallback<DmResult<GrdMedicineDetail_Mdl>>() {

                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<GrdMedicineDetail_Mdl> result) {
                    List<GrdMedicineDetail_Mdl> itemDetail_MdlLst;
                    if (result.isStatus()) {
                        txtItem.setText(DmShared.xNull(result.getData().getMedName()));
                        txtReceivedQty.setText(DmShared.xNull(result.getData().getChallanQty()));
                        grdPurchaseDet.getGrid().getStore().clear();
                        grdPurchaseDet.getGrid().getStore().addAll(result.getData().getGrdPurchaseDetailList());
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                    }
                }
            });

        } catch (DmException e) {
            //  topControlSaveError(e.getErrorNo(), e.getMessage());
        }

    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    interface CallBack {

        public void onOk(GrdMedicineDetail_Mdl opdService);

        public void onDelete();
    }

    private void showValueGrid(Integer currentRowIndex) {
        userEntryPOGrd.show(currentRowIndex, true, false, false, false, this);
    }

    private void gridEvents() {
        grdPurchaseDet.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {

                    showValueGrid(grdPurchaseDet.getGrid().getStore().indexOf(grdPurchaseDet.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());

        userEntryPOGrd.addCallBack(new UserEntryPOGridEdit.CallBack() {

            @Override
            public void onOk(GrdPurchaseOrderDetail_Mdl opdService) {

                grdPurchaseDet.commitChanges();

            }

            @Override
            public void onDelete() {
                grdPurchaseDet.commitChanges();

            }

        }
        );

        grdPurchaseDet.getGrid()
                .addRowClickHandler(new RowClickEvent.RowClickHandler() {

                    @Override

                    public void onRowClick(RowClickEvent event
                    ) {
                        if (event.getRowIndex() <= grdPurchaseDet.getGrid().getStore().getAll().size()) {
                            if (event.getColumnIndex() == 3) {

                                showOrderIndentDeatail(event.getRowIndex(), true);
                            }
                        }
                    }
                }
                );

    }

    private void showOrderIndentDeatail(int currentRowIndex, boolean isDeleteFocus) {
        if (topControl.getMode().trim().equalsIgnoreCase("Browse")) {
            userEntryPOGrd.show(currentRowIndex, false, false, false, isDeleteFocus, this);
        } else {
            userEntryPOGrd.show(currentRowIndex, true, false, false, isDeleteFocus, this);
        }
    }

}
