package aarogya.inventory.purchaseOrderApproval.client;

import aarogya.inventory.purchaseOrderApproval.shared.*;
import aarogya.inventory.purchaseOrder.client.PurchaseOrder;
import aarogya.inventory.purchaseOrder.shared.PurchaseOrder_Srv;
import aarogya.inventory.purchaseOrder.shared.PurchaseOrder_SrvAsync;
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

public class PurchaseOrderApprovalGridEdit<T1, T2> extends PurchaseOrderApprovalGridEdit_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, ClickHandler {

    interface GridEditCallBack {

        public void onOk(GrdPurchaseOrderApproval_Mdl approvalEntry_Mdl);
    }

    CellClickEvent event1;
    private String closedPurchaseOrderApproval;
    GridEditCallBack gridEditCallBack;
    ServiceInvoker<PurchaseApproval_SrvAsync> SrvAsync;
    ServiceInvoker<PurchaseOrder_SrvAsync> srvAsyncPurchase;
    WalkThrough walkThrough;
    PurchaseOrderApproval parentForm;
    String docId;
    boolean isUnApproved = false;
    private final ReasonGridEdit<GrdItemDetail_Mdl, GrdItemDetail_PrpAcs> reasonGridEdit = new ReasonGridEdit(grdItemlDetails);

    public void addGridEditCallBack(GridEditCallBack identApprovalEntryCallBack) {
        this.gridEditCallBack = identApprovalEntryCallBack;
    }

    public void show(String docId, PurchaseOrderApproval parentForm, boolean isUnApproved) {
        this.docId = docId;
        this.isUnApproved = isUnApproved;
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

    private void fillTextFeilds(PurchaseOrderApproval_Mdl ourRow) {
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

    public void onOkOfReason(CellClickEvent event) {
        if (!DmShared.xNull(grdItemlDetails.getGrid().getStore().get(event.getRowIndex()).getIsStatus()).equalsIgnoreCase("2")) {
            if (DmShared.xNull(txtApprovedBy.getText()).equalsIgnoreCase("")) {
                int val = DmShared.vNull(grdItemlDetails.getGrid().getStore().get(event.getRowIndex()).getIsChecked()).intValue();
                String vSNo = DmShared.xNull(grdItemlDetails.getGrid().getStore().get(event.getRowIndex()).getvSNo());
                if (val == 0) {
                    grdItemlDetails.getGrid().getStore().get(event.getRowIndex()).setIsChecked("1");
                    openRecord(false, vSNo);
                } else if (val == 1) {
                    grdItemlDetails.getGrid().getStore().get(event.getRowIndex()).setIsChecked("0");
                    openRecord(true, vSNo);
                }
            } else if (!DmShared.xNull(txtApprovedBy.getText()).equalsIgnoreCase(DmShared.xNull(txtCurrentUser.getText()))) {
                new DmAlertMessageBox("Record is not updated by this user,Can't update", txtStatus).show();
                return;
            } else {
                int val = DmShared.vNull(grdItemlDetails.getGrid().getStore().get(event.getRowIndex()).getIsChecked()).intValue();
                String vSNo = DmShared.xNull(grdItemlDetails.getGrid().getStore().get(event.getRowIndex()).getvSNo());
                if (val == 0) {
                    grdItemlDetails.getGrid().getStore().get(event.getRowIndex()).setIsChecked("1");
                    openRecord(false, vSNo);
                } else if (val == 1) {
                    grdItemlDetails.getGrid().getStore().get(event.getRowIndex()).setIsChecked("0");
                    openRecord(true, vSNo);
                }
            }
        }

    }

    public void popUpReason(int event) {
        reasonGridEdit.show(event, true, true, true, this);
    }

    private void gridEvents() {

        reasonGridEdit.addCallBack(new ReasonGridEdit.CallBack() {
            @Override
            public void onOk(GrdItemDetail_Mdl grdItemDetail_Mdl) {
                // closedRequisitionRemark=
                if (!DmShared.xNull(grdItemDetail_Mdl.getClosedPurchaseOrderApproval()).equalsIgnoreCase("")) {
                    closedPurchaseOrderApproval = DmShared.xNull(grdItemDetail_Mdl.getClosedPurchaseOrderApproval());
                }
                onOkOfReason(event1);
            }

            @Override
            public void onDelete() {
                reasonGridEdit.getDmGrid().commitChanges();
            }

            @Override
            public void onRefresh() {

            }
        });

        grdItemlDetails.getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                try {
                    if (event.getCellIndex() == 1) {
                        popUpReason(event.getRowIndex());
                        event1 = event;
                    } else if (event.getCellIndex() == 2) {
                        hide();
                        parentForm.showPurchaseOrder(docId);
                    }

                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
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
            GrdPurchaseOrderApproval_Mdl mainGrd_Mdl = new GrdPurchaseOrderApproval_Mdl();
            gridEditCallBack.onOk(mainGrd_Mdl);
            hide();
        } else if (event.getSource() == btnApprovedBy) {
            if (DmShared.xNull(txtStatus.getText()).equalsIgnoreCase("1")) {
                new DmAlertMessageBox("Record is cancelled,Please refresh record.", btnClose).show();
                return;
            } else if (DmShared.xNull(txtStatus.getText()).equalsIgnoreCase("2")) {
                new DmAlertMessageBox("Record is completed,Please refresh record.", btnClose).show();
                return;
            } else {
                if (DmShared.xNull(userControl.getTextBlog(approvedByBlog)).trim().equalsIgnoreCase("")) {
                    setApproved(DmShared.iNull(txtPurchaseNo.getTag()).toString(), true);
                } else {
                    setApproved(DmShared.iNull(txtPurchaseNo.getTag()).toString(), false);
                }
            }
        }
    }

    private void openRecord(boolean isOpen, String vSNo) {
        try {
            SrvAsync.invokeRPC().openRecord(isOpen, DmShared.xNull(txtPurchaseNo.getTag()), vSNo, closedPurchaseOrderApproval, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    return;
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        fillGrid(DmShared.xNull(txtPurchaseNo.getTag()));
                        grdItemlDetails.commitChanges();
                    } else {
                        new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void manageDisplay() {
        txtApproved.setEnabled(false);
        txtApprovedBy.setEnabled(false);
        txtApprovedDateTime.setEnabled(false);
        txtStatus.setEnabled(false);
        txtRate.setEnabled(false);
        txtItemGroup.setEnabled(false);
        txtItemName.setEnabled(false);
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

    private void setApproved(String docId, boolean isApprove) {
        try {
            if (DmShared.iNull(txtWTACode.getText()).longValue() != 0) {
                walkThrough.show(DmShared.iNull(txtWTACode.getText()).toString());
            } else {
                srvAsyncPurchase.invokeRPC().setApproval(DmClient.getLoginUserName(), isApprove, isUnApproved, docId, new AsyncCallback<DmResult<String>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResult<String> result) {
                        if (result.isStatus()) {
                            fillGrid(result.getData());
                        } else {
                            topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                        }
                    }
                });
            }
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void topControlSaveError(int errorNo, String errorMessage) {
        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtOrdQty).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, (DmTextBox) null).show();
                break;
        }
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

    public PurchaseOrderApprovalGridEdit() {
        super();
        SrvAsync = new ServiceInvoker<PurchaseApproval_SrvAsync>(GWT.create(PurchaseApproval_Srv.class), PurchaseOrderApproval.class.getName());
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
        SrvAsync.invokeRPC().fillItemGrid(code, new AsyncCallback<DmResult<PurchaseOrderApproval_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<PurchaseOrderApproval_Mdl> result) {
                if (result.isStatus()) {
                    fillTextFeilds(result.getData());
                    List<GrdItemDetail_Mdl> itemDetail_Mdls = result.getData().getGrdItemDetail_Mdl();
                    grdItemlDetails.getGrid().getStore().clear();
                    grdItemlDetails.getGrid().getStore().addAll(itemDetail_Mdls);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

}
