package aarogya.inventory.indentApproval.client;

import aarogya.inventory.indent.client.Indent;
import aarogya.inventory.indent.shared.Indent_Srv;
import aarogya.inventory.indent.shared.Indent_SrvAsync;
import aarogya.inventory.indentApproval.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import aarogya.inventory.indentApproval.shared.IndentApproval_Srv;
import aarogya.inventory.indentApproval.shared.IndentApproval_SrvAsync;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.dmToolkit.walkThrough.client.WalkThrough;
import java.util.List;

public class IndentApprovalGridEdit<T1, T2> extends IndentApprovalGridEdit_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, ClickHandler {

    interface GridEditCallBack {

        public void onOk(GrdIndentApproval_Mdl approvalEntry_Mdl);
    }
    CellClickEvent event1;
    private String closedIndentApprovalRemark;
    GridEditCallBack gridEditCallBack;
    ServiceInvoker<IndentApproval_SrvAsync> SrvAsync;
    ServiceInvoker<Indent_SrvAsync> srvAsyncIndent;
    WalkThrough walkThrough;
    final ItemDetailGridEdit itemDetailGridEdit = new ItemDetailGridEdit(grdItemDetails);
    private IndentApproval_Mdl parentRow;
    private final ReasonGridEdit<GrdItemDetail_Mdl, GrdMedicineDetail_PrpAcs> reasonGridEdit = new ReasonGridEdit(grdItemDetails);

    public void addGridEditCallBack(GridEditCallBack identApprovalEntryCallBack) {
        this.gridEditCallBack = identApprovalEntryCallBack;
    }

    public void show(String docId, String isStatus) {
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        fillGrid(DmShared.xNull(docId));
    }

    private void fillTextFeilds(IndentApproval_Mdl ourRow) {
        manageDisplay();

        parentRow = ourRow;
        userControl.setTextBlog(approvedByBlog, DmShared.xNull(ourRow.getApprovedBy()));
        userControl.setTextBlog(approvedDateTimeBlog, DmShared.xNull(ourRow.getApprovedDateTime()));
        txtIndCat.setText(DmShared.xNull(ourRow.getIndType()));
        txtIndDate.setText(DmShared.xNull(ourRow.getIndDate()));
        txtIndNo.setText(DmShared.xNull(ourRow.getIndReccId()));
        txtIndNo.setTag(DmShared.xNull(ourRow.getIndDocId()));
        txtSite.setText(DmShared.xNull(ourRow.getSiteName()));
        txtDepartment.setText(DmShared.xNull(ourRow.getDepartmentName()));
        txtDepartment.setTag(DmShared.xNull(ourRow.getDepartmentCode()));
        if (DmShared.xNull(ourRow.getIsStatus()).equalsIgnoreCase("1")) {
            isCancelled.setText("Cancelled");
        } else if (DmShared.xNull(ourRow.getIsStatus()).equalsIgnoreCase("2")) {
            isCancelled.setText("Completed");
        } else {
            isCancelled.setText("");
        }
    }

    public void onOkOfReason(CellClickEvent event) {
        if (!DmShared.xNull(grdItemDetails.getGrid().getStore().get(event.getRowIndex()).getIsStatus()).equalsIgnoreCase("2")) {
            if (DmShared.xNull(parentRow.getApprovedBy()).equalsIgnoreCase("")) {
                int val = DmShared.vNull(grdItemDetails.getGrid().getStore().get(event.getRowIndex()).getIsChecked()).intValue();
                String vSNo = DmShared.xNull(grdItemDetails.getGrid().getStore().get(event.getRowIndex()).getvSNo());
                if (val == 0) {
                    grdItemDetails.getGrid().getStore().get(event.getRowIndex()).setIsChecked("1");
                    openRecord(false, vSNo);

                } else if (val == 1) {
                    grdItemDetails.getGrid().getStore().get(event.getRowIndex()).setIsChecked("0");
                    openRecord(true, vSNo);
                }
            } else if (!DmShared.xNull(parentRow.getApprovedBy()).equalsIgnoreCase(DmShared.xNull(DmClient.getLoginUserName()))) {
                new DmAlertMessageBox("Record is not updated by this user,so can't update.", (DmTextBox) null).show();
                return;
            } else {
                int val = DmShared.vNull(grdItemDetails.getGrid().getStore().get(event.getRowIndex()).getIsChecked()).intValue();
                String vSNo = DmShared.xNull(grdItemDetails.getGrid().getStore().get(event.getRowIndex()).getvSNo());
                if (val == 0) {

                    openRecord(false, vSNo);

                } else if (val == 1) {
                    grdItemDetails.getGrid().getStore().get(event.getRowIndex()).setIsChecked("0");
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
                if (!DmShared.xNull(grdItemDetail_Mdl.getClosedIndentApprovalRemark()).equalsIgnoreCase("")) {
                    closedIndentApprovalRemark = DmShared.xNull(grdItemDetail_Mdl.getClosedIndentApprovalRemark());
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

        itemDetailGridEdit.addCallBack(new ItemDetailGridEdit.CallBack() {

            @Override
            public void onOk(GrdItemDetail_Mdl grdItemDetail) {
                grdItemDetails.commitChanges();
            }

            @Override
            public void onDelete() {
                grdItemDetails.commitChanges();
            }
        });
        grdItemDetails.getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {

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

        grdItemDetails.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                if (DmShared.iNull(event.getColumnIndex()).intValue() > 1) {
                    itemDetailGridEdit.show(event.getRowIndex(), true, false, false, "Browse");
                }

            }
        });
        grdItemDetails.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event
            ) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
//itemDetailGridEdit.show(event.getNativeKeyCode,true,false,false);
                }
            }
        }, KeyDownEvent.getType());

    }

    private void fillGrid(String code) {
        SrvAsync.invokeRPC().fillItemGrid(code, new AsyncCallback<DmResult<IndentApproval_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<IndentApproval_Mdl> result) {
                if (result.isStatus()) {
                    fillTextFeilds(result.getData());
                    List<GrdItemDetail_Mdl> itemDetail_Mdls = result.getData().getGrdItemDetail_Mdl();
                    grdItemDetails.getGrid().getStore().clear();
                    grdItemDetails.getGrid().getStore().addAll(itemDetail_Mdls);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
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
                fillGrid(DmShared.iNull(txtIndNo.getTag()).toString());
            }
        });
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnClose) {
            GrdIndentApproval_Mdl approval_Mdl = new GrdIndentApproval_Mdl();
            gridEditCallBack.onOk(approval_Mdl);
            hide();
        } else if (event.getSource() == btnApprovedBy) {
            if (DmShared.xNull(parentRow.getIsStatus()).equalsIgnoreCase("1")) {
                new DmAlertMessageBox("Indent is cancelled,Please refresh record.", btnClose).show();
                return;
            } else if (DmShared.xNull(parentRow.getIsStatus()).equalsIgnoreCase("2")) {
                new DmAlertMessageBox("Indent is completed,Please refresh record.", btnClose).show();
                return;
            } else {
                if (DmShared.xNull(userControl.getTextBlog(approvedByBlog)).trim().equalsIgnoreCase("")) {
                    setApproved(DmShared.iNull(txtIndNo.getTag()).toString(), true);
                } else {
                    setApproved(DmShared.iNull(txtIndNo.getTag()).toString(), false);
                }
            }
        }
    }

    private void openRecord(boolean isOpen, String vSNo) {
        try {
            SrvAsync.invokeRPC().openRecord(isOpen, DmShared.xNull(txtIndNo.getTag()), vSNo,closedIndentApprovalRemark, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    return;
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        grdItemDetails.commitChanges();
                        fillGrid(DmShared.xNull(txtIndNo.getTag()));

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
        txtIndCat.setEnabled(false);
        txtIndDate.setEnabled(false);
        txtIndNo.setEnabled(false);
        txtSite.setEnabled(false);
        txtDepartment.setEnabled(false);
    }

    public void setApproved(String docId, boolean isApprove) {
        try {
            if (DmShared.iNull(parentRow.getWtaCode()).longValue() != 0) {
                walkThrough.show(DmShared.iNull(parentRow.getWtaCode()).toString());
            } else {
                srvAsyncIndent.invokeRPC().setApproval(DmClient.getLoginUserName(), isApprove, docId, new AsyncCallback<DmResult<String>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResult<String> result) {
                        if (result.isStatus()) {
                            fillGrid(result.getData());
                            GrdIndentApproval_Mdl approval_Mdl = new GrdIndentApproval_Mdl();
                            gridEditCallBack.onOk(approval_Mdl);
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
                new DmAlertMessageBox(errorMessage, btnClose).show();
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

    public IndentApprovalGridEdit() {
        super();
        SrvAsync = new ServiceInvoker<IndentApproval_SrvAsync>(GWT.create(IndentApproval_Srv.class), IndentApproval.class.getName());
        srvAsyncIndent = new ServiceInvoker<Indent_SrvAsync>(GWT.create(Indent_Srv.class), Indent.class.getName());
        walkThrough = new WalkThrough(srvAsyncIndent);
        txtKeyEvents();
        popupPanel.addAttachHandler(this);
        toolContainer.setTitle("Indent Detail");
        toolContainer.addWidget(flwClose);
        toolContainer.addGap();
        btnApprovedBy.addClickHandler(this);
        btnClose.addClickHandler(this);
        gridEvents();
    }

}
