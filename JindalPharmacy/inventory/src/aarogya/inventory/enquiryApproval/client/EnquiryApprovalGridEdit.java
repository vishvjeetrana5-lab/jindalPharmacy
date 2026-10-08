package aarogya.inventory.enquiryApproval.client;

import aarogya.inventory.enquiryApproval.shared.*;
import aarogya.inventory.enquiry.client.Enquiry;
import aarogya.inventory.enquiry.shared.Enquiry_Srv;
import aarogya.inventory.enquiry.shared.Enquiry_SrvAsync;
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
import dataman.dmToolkit.self.shared.*;
import dataman.dmToolkit.walkThrough.client.WalkThrough;
import java.util.List;

public class EnquiryApprovalGridEdit<T1, T2> extends EnquiryApprovalGridEdit_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, ClickHandler {

    interface GridEditCallBack {

        public void onOk(GrdEnquiryApproval_Mdl approvalEntry_Mdl);
    }

    GridEditCallBack gridEditCallBack;
    ServiceInvoker<EnquiryApproval_SrvAsync> SrvAsync;
    ServiceInvoker<Enquiry_SrvAsync> srvAsyncEnquiry;
    WalkThrough walkThrough;

    public void addGridEditCallBack(GridEditCallBack identApprovalEntryCallBack) {
        this.gridEditCallBack = identApprovalEntryCallBack;
    }

    public void show(String docId) {
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        fillGrid(docId);
    }

    private void fillTextFeilds(EnquiryApproval_Mdl ourRow) {
        manageDisplay();
        txtApprovedBy.setText(DmShared.xNull(ourRow.getApprovedBy()));
        txtEnqDate.setText(DmShared.xNull(ourRow.getEnqDate()));
        txtRemarks.setText(DmShared.xNull(ourRow.getRemark()));
        txtVendorName.setText(DmShared.xNull(ourRow.getVendorName()));
        txtRefNo.setText(DmShared.xNull(ourRow.getRefNo()));
        userControl.setTextBlog(approvedByBlog, DmShared.xNull(ourRow.getApprovedBy()));
        userControl.setTextBlog(approvedDateTimeBlog, DmShared.xNull(ourRow.getApprovedDateTime()));
        txtApprovedDateTime.setText(DmShared.xNull(ourRow.getApprovedDateTime()));
        txtEnqNo.setText(DmShared.xNull(ourRow.getEnquiryRecIds()));
        txtEnqNo.setTag(DmShared.xNull(ourRow.getEnquiryDocIds()));
        txtStatus.setText(DmShared.xNull(ourRow.getIsStatus()));
        txtWTACode.setText(DmShared.iNull(ourRow.getWtaCode()).toString());

        txtSite.setText(DmShared.xNull(ourRow.getSiteName()));

        if (DmShared.xNull(ourRow.getIsStatus()).equalsIgnoreCase("1")) {
            isCancelled.setText("Cancelled");
        } else if (DmShared.xNull(ourRow.getIsStatus()).equalsIgnoreCase("2")) {
            isCancelled.setText("Completed");
        } else {
            isCancelled.setText("");
        }
    }

    private void gridEvents() {
        walkThrough.addCallBack(new WalkThrough.CallBack() {
            @Override
            public void onClose() {
                fillGrid(DmShared.iNull(txtEnqNo.getTag()).toString());
            }
        });
        grdItemlDetails.getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                try {
                    if (event.getCellIndex() == 1) {
                        if (!DmShared.xNull(grdItemlDetails.getGrid().getStore().get(event.getRowIndex()).getIsStatus()).equalsIgnoreCase("2")) {
                            if (DmShared.xNull(txtApprovedBy.getText()).equalsIgnoreCase("")) {
                                int val = DmShared.vNull(grdItemlDetails.getGrid().getStore().get(event.getRowIndex()).getIsChecked()).intValue();
                                String subCode = DmShared.xNull(grdItemlDetails.getGrid().getStore().get(event.getRowIndex()).getSharedKeyFieldCode());
                                if (val == 0) {
                                    grdItemlDetails.getGrid().getStore().get(event.getRowIndex()).setIsChecked("1");
                                    openRecord(false, DmShared.iNull(subCode).toString());

                                } else if (val == 1) {

                                    grdItemlDetails.getGrid().getStore().get(event.getRowIndex()).setIsChecked("0");
                                    openRecord(true, DmShared.iNull(subCode).toString());
                                }

                            } else if (!DmShared.xNull(txtApprovedBy.getText()).equalsIgnoreCase(DmClient.getLoginUserName())) {
                                new DmAlertMessageBox("Record is not updated by this user,so can't update.", txtStatus).show();
                                return;
                            } else {
                                int val = DmShared.vNull(grdItemlDetails.getGrid().getStore().get(event.getRowIndex()).getIsChecked()).intValue();
                                String subCode = DmShared.xNull(grdItemlDetails.getGrid().getStore().get(event.getRowIndex()).getSharedKeyFieldCode());
                                if (val == 0) {

                                    openRecord(false, DmShared.iNull(subCode).toString());

                                } else if (val == 1) {
                                    grdItemlDetails.getGrid().getStore().get(event.getRowIndex()).setIsChecked("0");
                                    openRecord(true, DmShared.iNull(subCode).toString());
                                }

                            }

                        }
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

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnClose) {
            GrdEnquiryApproval_Mdl mainGrd_Mdl = new GrdEnquiryApproval_Mdl();
            gridEditCallBack.onOk(mainGrd_Mdl);
            popupPanel.hide();
        } else if (event.getSource() == btnApprovedBy) {
            if (DmShared.xNull(txtStatus.getText()).equalsIgnoreCase("1")) {
                new DmAlertMessageBox("Record is cancelled,Please refresh record.", btnClose).show();
                return;
            } else if (DmShared.xNull(txtStatus.getText()).equalsIgnoreCase("2")) {
                new DmAlertMessageBox("Record is completed,Please refresh record.", btnClose).show();
                return;
            } else {
                if (DmShared.xNull(userControl.getTextBlog(approvedByBlog)).trim().equalsIgnoreCase("")) {
                    setApproved(DmShared.iNull(txtEnqNo.getTag()).toString(), true);
                } else {
                    setApproved(DmShared.iNull(txtEnqNo.getTag()).toString(), false);
                }
            }
        }
    }

    private void openRecord(boolean isOpen, String subCode) {
        try {
            SrvAsync.invokeRPC().openRecord(isOpen, DmShared.xNull(txtEnqNo.getTag()), subCode, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    return;
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        grdItemlDetails.commitChanges();
                        fillGrid(DmShared.xNull(txtEnqNo.getTag()));
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
        txtVendorName.setEnabled(false);
        txtApprovedDateTime.setEnabled(false);
        txtRemarks.setEnabled(false);
        txtRefNo.setEnabled(false);
        txtStatus.setEnabled(false);
        txtEnqDate.setEnabled(false);
        txtEnqNo.setEnabled(false);
        txtSite.setEnabled(false);
    }

    private void fillGrid(String code) {
        SrvAsync.invokeRPC().fillVendorGrid(code, new AsyncCallback<DmResult<EnquiryApproval_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<EnquiryApproval_Mdl> result) {
                if (result.isStatus()) {
                    fillTextFeilds(result.getData());
                    List<GrdVendorDetaill_Mdl> itemDetail_Mdls = result.getData().getGrdVendorDetail_Mdl();
                    grdItemlDetails.getGrid().getStore().clear();
                    grdItemlDetails.getGrid().getStore().addAll(itemDetail_Mdls);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    public void setApproved(String docId, boolean isApprove) {
        try {
            if (DmShared.iNull(txtWTACode.getText()).longValue() != 0) {
                walkThrough.show(txtWTACode.getText());
            } else {
                srvAsyncEnquiry.invokeRPC().setApproval(DmClient.getLoginUserName(), isApprove, docId, new AsyncCallback<DmResult<String>>() {
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
                new DmAlertMessageBox(errorMessage, txtRemarks).show();
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

    public EnquiryApprovalGridEdit() {
        super();
        SrvAsync = new ServiceInvoker<EnquiryApproval_SrvAsync>(GWT.create(EnquiryApproval_Srv.class), EnquiryApproval.class.getName());
        srvAsyncEnquiry = new ServiceInvoker<Enquiry_SrvAsync>(GWT.create(Enquiry_Srv.class), Enquiry.class.getName());
        walkThrough = new WalkThrough(srvAsyncEnquiry);
        gridEvents();
        popupPanel.addAttachHandler(this);
        toolContainer.setTitle("Enquiry Detail");
        toolContainer.addWidget(flwClose);
        toolContainer.addGap();
        btnClose.addClickHandler(this);
        btnApprovedBy.addClickHandler(this);
    }
}
