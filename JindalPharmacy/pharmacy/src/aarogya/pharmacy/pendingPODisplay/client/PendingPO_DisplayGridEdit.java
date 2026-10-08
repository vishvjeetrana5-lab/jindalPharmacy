package aarogya.pharmacy.pendingPODisplay.client;

import aarogya.pharmacy.pendingPODisplay.client.*;
import aarogya.pharmacy.pendingPODisplay.shared.*;
import aarogya.pharmacy.purchaseOrder.client.PurchaseOrder;
import aarogya.pharmacy.purchaseOrder.shared.PurchaseOrder_Srv;
import aarogya.pharmacy.purchaseOrder.shared.PurchaseOrder_SrvAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.walkThrough.client.WalkThrough;
import java.util.List;

public class PendingPO_DisplayGridEdit<T1, T2> extends PendingPO_DisplayGridEdit_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, ClickHandler {

    interface GridEditCallBack {

        public void onOk(GrdPendingPO_Display_Mdl approvalEntry_Mdl);
    }

    GridEditCallBack gridEditCallBack;
    ServiceInvoker<PendingPO_Display_SrvAsync> SrvAsync;
    ServiceInvoker<PurchaseOrder_SrvAsync> srvAsyncPurchase;

    WalkThrough walkThrough;
    PendingPO_Display parentForm;

    String docId, pOVNo;

    public void addGridEditCallBack(GridEditCallBack identApprovalEntryCallBack) {
        this.gridEditCallBack = identApprovalEntryCallBack;
    }

    public void show(String docId, PendingPO_Display parentForm) {
        this.docId = docId;
        this.parentForm = parentForm;
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        flwOk.clear();
        flwOk.setVisible(false);
        fillGrid(DmShared.xNull(docId));
        flwOk.add(btnOk);
        flwOk.setVisible(true);
    }

    private void fillTextFeilds(PendingPO_Dispaly_Mdl ourRow) {
        manageDisplay();
        txtApproved.setTag(DmShared.xNull(ourRow.getvSNo()));
        txtApprovedBy.setText(DmShared.xNull(ourRow.getApprovedBy()));
        userControl.setTextBlog(approvedByBlog, DmShared.xNull(ourRow.getApprovedBy()));
        userControl.setTextBlog(approvedDateTimeBlog, DmShared.xNull(ourRow.getApprovedDateTime()));
        txtApprovedDateTime.setText(DmShared.xNull(ourRow.getApprovedDateTime()));
        txtPurchaseNo.setText(DmShared.xNull(ourRow.getPurchaseReccIds()));
        txtPurchaseNo.setTag(DmShared.xNull(ourRow.getPurchaseDocIds()));
        txtPurchaseDate.setText(DmShared.dNull(ourRow.getPurchaseDate()));
        txtStatus.setText(DmShared.xNull(ourRow.getIsStatus()));
        txtQuotationNo.setText(DmShared.xNull(ourRow.getQuotationNo()));
        txtCurrentUser.setText(DmClient.getLoginUserName());
        txtSupplier.setText(DmShared.xNull(ourRow.getSuppleirName()));
        txtCurrency.setText(DmShared.xNull(ourRow.getCurrency()));
        txtWTACode.setText(DmShared.iNull(ourRow.getWtaCode()).toString());

    }

    private void gridEvents() {

        grdMedDetails.getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                try {
                    if (event.getCellIndex() == 1) {
                        hide();
                        parentForm.showPurchaseOrder(docId);
                    }

                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null);
                }
            }

        });
    }

    public void hide() {
        this.popupPanel.hide();
    }

    private void txtKeyEvents() {
        walkThrough.addCallBack(new WalkThrough.CallBack() {
            @Override
            public void onClose() {
                fillGrid(DmShared.iNull(txtPurchaseNo.getTag()).toString());
            }
        });
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnClose) {
            GrdPendingPO_Display_Mdl mainGrd_Mdl = new GrdPendingPO_Display_Mdl();
            gridEditCallBack.onOk(mainGrd_Mdl);
            hide();
        }
    }
    private void manageDisplay() {
        txtApproved.setEnabled(false);
        txtApprovedBy.setEnabled(false);
        txtApprovedDateTime.setEnabled(false);
        txtStatus.setEnabled(false);
        txtRate.setEnabled(false);
        txtMedName.setEnabled(false);
        txtReqDate.setEnabled(false);
        txtSKU.setEnabled(false);
        txtOrdQty.setEnabled(false);
        txtPurchaseNo.setEnabled(false);
        txtPurchaseDate.setEnabled(false);
        txtRemarks.setEnabled(false);
        txtQuotationNo.setEnabled(false);
        txtSupplier.setEnabled(false);
        txtCurrency.setEnabled(false);
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            this.hide();
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    public PendingPO_DisplayGridEdit() {
        super();
        SrvAsync = new ServiceInvoker<PendingPO_Display_SrvAsync>(GWT.create(PendingPO_Display_Srv.class), PendingPO_Display.class.getName());
        srvAsyncPurchase = new ServiceInvoker<PurchaseOrder_SrvAsync>(GWT.create(PurchaseOrder_Srv.class), PurchaseOrder.class.getName());
        walkThrough = new WalkThrough(srvAsyncPurchase);
        txtKeyEvents();
        popupPanel.addAttachHandler(this);
        toolContainer.setTitle("Purchase Detail");
        btnApprovedBy.addClickHandler(this);
        toolContainer.addWidget(flwClose);
        toolContainer.addGap();
        btnClose.addClickHandler(this);
        btnOk.addClickHandler(this);
        gridEvents();
    }

    private void fillGrid(String code) {
        SrvAsync.invokeRPC().fillItemGrid(code, new AsyncCallback<DmResult<PendingPO_Dispaly_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<PendingPO_Dispaly_Mdl> result) {
                if (result.isStatus()) {
                    fillTextFeilds(result.getData());
                    List<GrdMedicineDetail_Mdl> itemDetail_Mdls = result.getData().getGrdMedicineDetail_Mdl();
                    grdMedDetails.getGrid().getStore().clear();
                    grdMedDetails.getGrid().getStore().addAll(itemDetail_Mdls);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

}
