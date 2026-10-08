package aarogya.pharmacy.requisitionApproval.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.event.BlurEvent;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;
import aarogya.pharmacy.requisitionApproval.shared.*;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public class Setting<T1, T2> extends Setting_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, ClickHandler, BlurEvent.BlurHandler {

    interface RequisitionApprovalEntryCallBack {

        public void onOk(RequisitionApproval_Mdl approvalEntry_Mdl);
    }

    RequisitionApprovalEntryCallBack requisitionApprovalEntryCallBack;
    ServiceInvoker<RequisitionApproval_SrvAsync> SrvAsync;

    public void addRequisitionApprovalEntryCallBackCallBack(RequisitionApprovalEntryCallBack requisitionApprovalEntryCallBack) {
        this.requisitionApprovalEntryCallBack = requisitionApprovalEntryCallBack;
    }

    public void show(RequisitionApproval_Mdl requisitionApproval, boolean editable) {
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        txtFromDate.setFocus(true);
        flwOk.clear();
        flwOk.setVisible(false);

        if (editable) {
            flwOk.add(btnOk);
            flwOk.setVisible(true);
        }
        onDsiplay(requisitionApproval);
    }

    public void hide() {
        this.popupPanel.hide();
    }

    private void txtKeyEvents() {

        txtFromDate.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                txtFromDate.setText(DmClient.returnDate(txtFromDate.getText()));
            }
        });

        txtToDate.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                txtToDate.setText(DmClient.returnDate(txtToDate.getText()));
            }
        });

//        txtSampleFromDate.addValueChangeHandler(new ValueChangeHandler<String>() {
//            @Override
//            public void onValueChange(ValueChangeEvent<String> event) {
//                txtSampleFromDate.setText(DmClient.returnDate(txtSampleFromDate.getText()));
//            }
//        });
    }

    private void textHelpEvents() {
        txtHRequestedBy.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHIndenter(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHRequestedBy).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHRequestedBy.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHRequestedBy).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
                if (blnStatus) {

                }
            }
        });

        txtHStatus.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHStatus(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHStatus).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHStatus.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHStatus).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });

        txtHDepartment.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHDepartment(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDepartment).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDepartment.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDepartment).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
                if (blnStatus) {

                }
            }
        });

        txtHItemName.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHItem(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHItemName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHItemName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHItemName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
                if (blnStatus) {

                }
            }
        });

        txtHRequisitionNo.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHRequisitionNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHRequisitionNo_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHRequisitionNo).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHRequisitionNo_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHRequisitionNo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHRequisitionNo).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
                if (blnStatus) {

                }
            }
        });

    }

    @Override
    public void onClick(ClickEvent event) {

        if (event.getSource() == btnCancel) {
            popupPanel.hide();
        } else if (event.getSource() == btnOk) {

//            if (DmShared.xNull(txtHStatus.getTag()).equalsIgnoreCase("")) {
//                new DmAlertMessageBox("Status is mandatory.", txtFromDate).show();
//                return;
//
//            }
            if (!DmShared.xNull(txtFromDate.getText()).equalsIgnoreCase("") && !DmShared.xNull(txtToDate.getText()).equalsIgnoreCase("")) {
                if ((DmShared.convertDate(txtToDate.getText()).getTime() < (DmShared.convertDate(txtFromDate.getText())).getTime())) {
                    new DmAlertMessageBox("From Date should be less than or equal to To Date.", txtFromDate).show();
                    return;
                }
            }
            fillRecords();

        }
    }

    @Override
    public void onBlur(BlurEvent event) {
//        if (!DmShared.xNull(txtFromDate.getText()).equalsIgnoreCase("") && !DmShared.xNull(txtToDate.getText()).equalsIgnoreCase("")) {
//                if ((DmShared.convertDate(txtToDate.getText()).getTime() < (DmShared.convertDate(txtFromDate.getText())).getTime())) {
//                    new DmAlertMessageBox("From Date should be less than or equal to To Date.", txtFromDate).show();
//                    return;
//                }
//            }
    }

    private void fillRecords() {
        RequisitionApproval_Mdl mrTemp = new RequisitionApproval_Mdl();
        mrTemp.setFromDate(DmShared.xNull(txtFromDate.getText()));
        mrTemp.setToDate(DmShared.xNull(txtToDate.getText()));

        mrTemp.setDepartmentCode(DmShared.xNull(txtHDepartment.getTag()));
        mrTemp.setDepartmentName(DmShared.xNull(txtHDepartment.getText()));

//        mrTemp.setSharedKeyFieldCode(DmShared.xNull(txtHDepartment.getTag()));
//        mrTemp.setItemGroupCode(DmShared.xNull(txtHItemGroup.getTag()));
//        mrTemp.setItemGroupName(DmShared.xNull(txtHItemGroup.getText()));
        mrTemp.setItemCode(DmShared.xNull(txtHItemName.getTag()));
        mrTemp.setItemName(DmShared.xNull(txtHItemName.getText()));

        mrTemp.setStatusCode(DmShared.xNull(txtHStatus.getTag()));
        mrTemp.setStatusName(DmShared.xNull(txtHStatus.getText()));

        mrTemp.setIndenterCode(DmShared.xNull(txtHRequestedBy.getTag()));
        mrTemp.setIndenterName(DmShared.xNull(txtHRequestedBy.getText()));

//        mrTemp.setIndenterName(DmShared.xNull(txtFromDate.getText()));
        mrTemp.setRequisitionDocIds(DmShared.xNull(txtHRequisitionNo.getTag()));
        mrTemp.setRequisitionRecIds(DmShared.xNull(txtHRequisitionNo.getText()));

        requisitionApprovalEntryCallBack.onOk(mrTemp);
        popupPanel.hide();
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

    public Setting() {
        super();
        SrvAsync = new ServiceInvoker<RequisitionApproval_SrvAsync>(GWT.create(RequisitionApproval_Srv.class), RequisitionApproval.class.getName());
        textHelpEvents();
        txtKeyEvents();
        popupPanel.addAttachHandler(this);
        toolContainer.setTitle("Setting");
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();

        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
    }

    private void onDsiplay(RequisitionApproval_Mdl requisitionApproval) {
        if (requisitionApproval == null) {
            txtToDate.setText(DmClient.getLoginDate());
            if (DmShared.xNull(txtHStatus.getTag()).equalsIgnoreCase("")) {
                txtHStatus.setTag("P");
                txtHStatus.setText("Pending");
            }
        } else {
            txtFromDate.setText(requisitionApproval.getFromDate());
            txtToDate.setText(requisitionApproval.getToDate());

            txtHDepartment.setTag(requisitionApproval.getDepartmentCode());
            txtHDepartment.setText(requisitionApproval.getDepartmentName());

//            txtHDepartment.setTag(requisitionApproval.getSharedKeyFieldCode());
//            txtHItemGroup.setTag(requisitionApproval.getItemGroupCode());
//            txtHItemGroup.setText(requisitionApproval.getItemGroupName());
            txtHItemName.setTag(requisitionApproval.getItemCode());
            txtHItemName.setText(requisitionApproval.getItemName());

            txtHStatus.setTag(requisitionApproval.getStatusCode());
            txtHStatus.setText(requisitionApproval.getStatusName());

            txtHRequestedBy.setTag(requisitionApproval.getIndenterCode());
            txtHRequestedBy.setText(requisitionApproval.getIndenterName());

//            txtFromDate.setText(requisitionApproval.getIndenterName());
            txtHRequisitionNo.setTag(requisitionApproval.getRequisitionDocIds());
            txtHRequisitionNo.setText(requisitionApproval.getRequisitionRecIds());
        }

    }

}
