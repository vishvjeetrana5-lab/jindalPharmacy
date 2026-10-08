package aarogya.inventory.requisitionApproval.client;

import aarogya.inventory.requisitionApproval.shared.RequisitionApproval_SrvAsync;
import aarogya.inventory.requisitionApproval.shared.RequisitionApproval_Srv;
import aarogya.inventory.requisitionApproval.shared.GrdMedicineDetail_Mdl;
import aarogya.inventory.requisitionApproval.shared.RequisitionApproval_Mdl;
import aarogya.inventory.requisitionApproval.shared.GrdRequisitionApproval_Mdl;
import aarogya.inventory.requisition.client.Requisition;
import aarogya.inventory.requisition.shared.Requisition_Srv;
import aarogya.inventory.requisition.shared.Requisition_SrvAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.self.shared.*;
import dataman.dmToolkit.walkThrough.client.WalkThrough;
import java.util.List;

public class RequisitionApprovalGridEdit<T1, T2> extends RequisitionApprovalGridEdit_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, ClickHandler {

    interface GridEditCallBack {

        public void onOk(GrdRequisitionApproval_Mdl approvalEntry_Mdl);
    }

    GridEditCallBack gridEditCallBack;
    ServiceInvoker<RequisitionApproval_SrvAsync> SrvAsync;
    ServiceInvoker<Requisition_SrvAsync> srvAsyncReq;
    WalkThrough walkThrough;
    private String closedRequisitionRemark;
    private final ReasonGridEdit<GrdMedicineDetail_Mdl, GrdMedicineDetail_PrpAcs> reasonGridEdit = new ReasonGridEdit(grdMedicineDetails);
    CellClickEvent event1;

    public void addGridEditCallBack(GridEditCallBack identApprovalEntryCallBack) {
        this.gridEditCallBack = identApprovalEntryCallBack;
    }

    public void show(String docId) {
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        flwOk.clear();
        flwOk.setVisible(false);
        fillGrid(DmShared.xNull(docId));
        flwOk.add(btnOk);
        flwOk.setVisible(true);
    }

    private void fillTextFeilds(RequisitionApproval_Mdl ourRow) {
        manageDisplay();
        txtApproved.setTag(DmShared.xNull(ourRow.getvSNo()));
        txtApprovedBy.setText(DmShared.xNull(ourRow.getApprovedBy()));
        userControl.setTextBlog(approvedByBlog, DmShared.xNull(ourRow.getApprovedBy()));
        userControl.setTextBlog(approvedDateTimeBlog, DmShared.xNull(ourRow.getApprovedDateTime()));
        txtApprovedDateTime.setText(DmShared.xNull(ourRow.getApprovedDateTime()));
        txtRequitNo.setText(DmShared.xNull(ourRow.getRequitRecId()));
        txtRequitNo.setTag(DmShared.xNull(ourRow.getRequitDocId()));
        txtRequitDate.setText(DmShared.dNull(ourRow.getRequitDate()));
        txtDepartment.setText(DmShared.xNull(ourRow.getDepartmentName()));
        txtRequisitionBy.setTag(DmShared.xNull(ourRow.getRequestedByCode()));
        txtRequisitionBy.setText(DmShared.xNull(ourRow.getRequestedByName()));
        txtStatus.setText(DmShared.xNull(ourRow.getIsStatus()));
        txtCurrentUser.setText(DmClient.getLoginUserName());
        txtWTACode.setText(DmShared.iNull(ourRow.getWtaCode()).toString());
    }

    public void onOkOfReason(CellClickEvent event) {
        if (!DmShared.xNull(grdMedicineDetails.getGrid().getStore().get(event.getRowIndex()).getIsStatus()).equalsIgnoreCase("2")) {
            if (DmShared.xNull(txtApprovedBy.getText()).equalsIgnoreCase("")) {
                int val = DmShared.vNull(grdMedicineDetails.getGrid().getStore().get(event.getRowIndex()).getIsChecked()).intValue();
                String vSNo = DmShared.xNull(grdMedicineDetails.getGrid().getStore().get(event.getRowIndex()).getvSNo());
                if (val == 0) {
                    grdMedicineDetails.getGrid().getStore().get(event.getRowIndex()).setIsChecked("1");
                    openRecord(false, vSNo);
                } else if (val == 1) {
                    grdMedicineDetails.getGrid().getStore().get(event.getRowIndex()).setIsChecked("0");
                    openRecord(true, vSNo);
                }
            } else if (!DmShared.xNull(txtApprovedBy.getText()).equalsIgnoreCase(DmShared.xNull(txtCurrentUser.getText()))) {
                new DmAlertMessageBox("Record is not updated by this user,Can't update", txtStatus).show();
                return;
            } else {
                int val = DmShared.vNull(grdMedicineDetails.getGrid().getStore().get(event.getRowIndex()).getIsChecked()).intValue();
                String vSNo = DmShared.xNull(grdMedicineDetails.getGrid().getStore().get(event.getRowIndex()).getvSNo());
                if (val == 0) {
                    checkExistance(DmShared.xNull(txtRequitNo.getTag()), vSNo, event.getRowIndex(), "1");
//                                    grdMedicineDetails.getGrid().getStore().get(event.getRowIndex()).setIsChecked("1");
//                                    openRecord(false, vSNo);
                } else if (val == 1) {
                    checkExistance(DmShared.xNull(txtRequitNo.getTag()), vSNo, event.getRowIndex(), "0");
//                                    grdMedicineDetails.getGrid().getStore().get(event.getRowIndex()).setIsChecked("0");
//                                    openRecord(true, vSNo);
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
            public void onOk(GrdMedicineDetail_Mdl grdMedicineDetail_Mdl) {
                // closedRequisitionRemark=
                if (!DmShared.xNull(grdMedicineDetail_Mdl.getClosedRequisitionRemark()).equalsIgnoreCase("")) {
                    closedRequisitionRemark = DmShared.xNull(grdMedicineDetail_Mdl.getClosedRequisitionRemark());
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
        grdMedicineDetails.getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                try {
                    if (event.getCellIndex() == 1) {

                        popUpReason(event.getRowIndex());
                        event1 = event;

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
                fillGrid(DmShared.iNull(txtRequitNo.getTag()).toString());
            }
        });
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnClose) {
            GrdRequisitionApproval_Mdl mainGrd_Mdl = new GrdRequisitionApproval_Mdl();
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
                    setApproved(DmShared.iNull(txtRequitNo.getTag()).toString(), true);
                } else {
                    setApproved(DmShared.iNull(txtRequitNo.getTag()).toString(), false);
                }
            }
        }
    }

    private void openRecord(boolean isOpen, String vSNo) {
        try {
            SrvAsync.invokeRPC().openRecord(isOpen, DmShared.xNull(txtRequitNo.getTag()), vSNo,closedRequisitionRemark, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    return;
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        fillGrid(DmShared.xNull(txtRequitNo.getTag()));
                        grdMedicineDetails.commitChanges();
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
        txtRequitNo.setEnabled(false);
        txtRequitDate.setEnabled(false);
        txtRequisitionBy.setEnabled(false);
        txtDepartment.setEnabled(false);
        txtCurrency.setEnabled(false);
    }

    private void setApproved(String docId, boolean isApprove) {
        try {
            if (DmShared.iNull(txtWTACode.getText()).longValue() != 0) {
                walkThrough.show(DmShared.iNull(txtWTACode.getText()).toString());
            } else {
                srvAsyncReq.invokeRPC().setApproval(DmClient.getLoginUserName(), isApprove, docId, new AsyncCallback<DmResult<String>>() {
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
                new DmAlertMessageBox(errorMessage, txtRequitDate).show();
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

    public RequisitionApprovalGridEdit() {
        super();
        SrvAsync = new ServiceInvoker<RequisitionApproval_SrvAsync>(GWT.create(RequisitionApproval_Srv.class), RequisitionApproval.class.getName());
        srvAsyncReq = new ServiceInvoker<Requisition_SrvAsync>(GWT.create(Requisition_Srv.class), Requisition.class.getName());
        walkThrough = new WalkThrough(srvAsyncReq);
        txtKeyEvents();
        popupPanel.addAttachHandler(this);
        toolContainer.setTitle("Item Detail");
        btnApprovedBy.addClickHandler(this);
        toolContainer.addWidget(flwClose);
        toolContainer.addGap();
        btnClose.addClickHandler(this);
        btnOk.addClickHandler(this);
        gridEvents();
    }

    private void fillGrid(String code) {
        SrvAsync.invokeRPC().fillItemGrid(code, new AsyncCallback<DmResult<RequisitionApproval_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<RequisitionApproval_Mdl> result) {
                if (result.isStatus()) {
                    fillTextFeilds(result.getData());
                    List<GrdMedicineDetail_Mdl> itemDetail_Mdls = result.getData().getGrdItemDetail_Mdl();
                    grdMedicineDetails.getGrid().getStore().clear();
                    grdMedicineDetails.getGrid().getStore().addAll(itemDetail_Mdls);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void checkExistance(String docID, final String vSno, final int rowNo, final String value) {
        SrvAsync.invokeRPC().getLinkedRecords(docID, vSno, new AsyncCallback<DmResult<Boolean>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                return;
            }

            @Override
            public void onSuccess(DmResult<Boolean> result) {
                if (!result.getData()) {
                    new DmConfirmMessageBox("Indent/Transfer already linked with this item. Are you sure to continue?", new DialogHideEvent.DialogHideHandler() {
                        @Override
                        public void onDialogHide(DialogHideEvent event) {
                            if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                                grdMedicineDetails.getGrid().getStore().get(rowNo).setIsChecked(value);
                                if (value.trim().equalsIgnoreCase("1")) {
                                    openRecord(false, vSno);
                                } else if (value.trim().equalsIgnoreCase("0")) {
                                    openRecord(true, vSno);
                                }
                            }
                        }
                    }).show();
                } else {
                    grdMedicineDetails.getGrid().getStore().get(rowNo).setIsChecked(value);
                    if (value.trim().equalsIgnoreCase("1")) {
                        openRecord(false, vSno);
                    } else if (value.trim().equalsIgnoreCase("0")) {
                        openRecord(true, vSno);
                    }
                }
            }
        });
    }
}
