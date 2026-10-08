package aarogya.pharmacy.medicineTransferApproval.client;

import aarogya.pharmacy.medicineTransferApproval.client.*;
import aarogya.pharmacy.medicineTransfer.client.MedicineTransfer;
import aarogya.pharmacy.medicineTransfer.shared.MedicineTransfer_Srv;
import aarogya.pharmacy.medicineTransfer.shared.MedicineTransfer_SrvAsync;
import aarogya.pharmacy.medicineTransferApproval.shared.*;
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
import aarogya.pharmacy.medicineTransferApproval.shared.MedicineTransferApproval_Srv;
import aarogya.pharmacy.medicineTransferApproval.shared.MedicineTransferApproval_SrvAsync;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.dmToolkit.walkThrough.client.WalkThrough;
import java.util.List;

public class MedicineTransferApprovalGridEdit<T1, T2> extends MedicineTransferApprovalGridEdit_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, ClickHandler {

    interface GridEditCallBack {

        public void onOk(GrdMedicineTransferApproval_Mdl approvalEntry_Mdl);
    }

    GridEditCallBack gridEditCallBack;
    ServiceInvoker<MedicineTransferApproval_SrvAsync> SrvAsync;
    ServiceInvoker<MedicineTransfer_SrvAsync> srvAsyncItemTransfer;
    WalkThrough walkThrough;
    final MedicineDetailGridEdit itemDetailGridEdit = new MedicineDetailGridEdit(grdItemDetails);
    private MedicineTransferApproval_Mdl parentRow;

    public void addGridEditCallBack(GridEditCallBack identApprovalEntryCallBack) {
        this.gridEditCallBack = identApprovalEntryCallBack;
    }

    public void show(String docId, String isStatus) {
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        fillGrid(DmShared.xNull(docId));
    }

    private void fillTextFeilds(MedicineTransferApproval_Mdl ourRow) {
        manageDisplay();

        parentRow = ourRow;
        userControl.setTextBlog(approvedByBlog, DmShared.xNull(ourRow.getApprovedBy()));
        userControl.setTextBlog(approvedDateTimeBlog, DmShared.xNull(ourRow.getApprovedDateTime()));
        txtTrfCat.setText(DmShared.xNull(ourRow.getTrfType()));
        txtTrfDate.setText(DmShared.xNull(ourRow.getTrfDate()));
        txtTrfNo.setText(DmShared.xNull(ourRow.getTrfReccId()));
        txtTrfNo.setTag(DmShared.xNull(ourRow.getTrfDocId()));
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

    private void gridEvents() {
        itemDetailGridEdit.addCallBack(new MedicineDetailGridEdit.CallBack() {

            @Override
            public void onOk(GrdMedicineDetail_Mdl grdItemDetail) {
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
        SrvAsync.invokeRPC().fillItemGrid(code, new AsyncCallback<DmResult<MedicineTransferApproval_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<MedicineTransferApproval_Mdl> result) {
                if (result.isStatus()) {
                    fillTextFeilds(result.getData());
                    List<GrdMedicineDetail_Mdl> itemDetail_Mdls = result.getData().getGrdMedicineDetail_Mdl();
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
                fillGrid(DmShared.iNull(txtTrfNo.getTag()).toString());
            }
        });
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnClose) {
            GrdMedicineTransferApproval_Mdl approval_Mdl = new GrdMedicineTransferApproval_Mdl();
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
                    setApproved(DmShared.iNull(txtTrfNo.getTag()).toString(), true);
                } else {
                    setApproved(DmShared.iNull(txtTrfNo.getTag()).toString(), false);
                }
            }
        }
    }

    private void openRecord(boolean isOpen, String vSNo) {
        try {
            SrvAsync.invokeRPC().openRecord(isOpen, DmShared.xNull(txtTrfNo.getTag()), vSNo, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    return;
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        grdItemDetails.commitChanges();
                        fillGrid(DmShared.xNull(txtTrfNo.getTag()));

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
        txtTrfCat.setEnabled(false);
        txtTrfDate.setEnabled(false);
        txtTrfNo.setEnabled(false);
        txtSite.setEnabled(false);
        txtDepartment.setEnabled(false);
    }

    public void setApproved(String docId, boolean isApprove) {
        try {
            if (DmShared.iNull(parentRow.getWtaCode()).longValue() != 0) {
                walkThrough.show(DmShared.iNull(parentRow.getWtaCode()).toString());
            } else {
                srvAsyncItemTransfer.invokeRPC().setApproval(DmClient.getLoginUserName(), isApprove, docId, new AsyncCallback<DmResult<String>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResult<String> result) {
                        if (result.isStatus()) {
                            fillGrid(result.getData());
                            GrdMedicineTransferApproval_Mdl approval_Mdl = new GrdMedicineTransferApproval_Mdl();
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

    public MedicineTransferApprovalGridEdit() {
        super();
        SrvAsync = new ServiceInvoker<MedicineTransferApproval_SrvAsync>(GWT.create(MedicineTransferApproval_Srv.class), MedicineTransferApproval.class.getName());
        srvAsyncItemTransfer = new ServiceInvoker<MedicineTransfer_SrvAsync>(GWT.create(MedicineTransfer_Srv.class), MedicineTransfer.class.getName());
        walkThrough = new WalkThrough(srvAsyncItemTransfer);
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
