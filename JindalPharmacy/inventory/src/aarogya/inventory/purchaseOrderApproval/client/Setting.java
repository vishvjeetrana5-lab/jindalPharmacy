package aarogya.inventory.purchaseOrderApproval.client;

import aarogya.inventory.purchaseOrder.client.PurchaseOrder;
import aarogya.inventory.purchaseOrder.shared.PurchaseOrder_Srv;
import aarogya.inventory.purchaseOrder.shared.PurchaseOrder_SrvAsync;
import aarogya.inventory.purchaseOrder.shared.TxtHItemName_Mdl;
import aarogya.inventory.purchaseOrder.shared.TxtHSupplierPT_Mdl;
import aarogya.inventory.purchaseOrderApproval.shared.*;
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
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public class Setting<T1, T2> extends Setting_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, ClickHandler, BlurEvent.BlurHandler {

    interface PurchaseApprovalCallBack {

        public void onOk(PurchaseOrderApproval_Mdl approvalEntry_Mdl);
    }

    PurchaseApprovalCallBack purchaseApprovalCallBack;
    ServiceInvoker<PurchaseApproval_SrvAsync> SrvAsync;
    ServiceInvoker<PurchaseOrder_SrvAsync> srvAsyncPO;

    public void addPurchaseApprovalCallBack(PurchaseApprovalCallBack purchaseApprovalCallBack) {
        this.purchaseApprovalCallBack = purchaseApprovalCallBack;
    }

    public void show(boolean editable) {
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
    }

    private void textHelpEvents() {

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

        txtHItemName.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<TxtHItemName_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsyncPO.invokeRPC(blnCallBackWait).getTxtHItem("","","",helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHItemName_Mdl>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHItemName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHItemName_Mdl>> result) {
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
            }
        });

        txtHPurchaseOrder.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHPurchaseOrder(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHPurchaseOrder_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPurchaseOrder).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHPurchaseOrder_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHPurchaseOrder.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPurchaseOrder).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHSupplier.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<TxtHSupplierPT_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsyncPO.invokeRPC(blnCallBackWait).getTxtHSupplierPT("",true, null, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHSupplierPT_Mdl>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSupplier).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHSupplierPT_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHSupplier.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSupplier).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            popupPanel.hide();
        } else if (event.getSource() == btnOk) {
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
    }

    private void fillRecords() {
        PurchaseOrderApproval_Mdl mrTemp = new PurchaseOrderApproval_Mdl();
        mrTemp.setFromDate(DmShared.xNull(txtFromDate.getText()));
        mrTemp.setToDate(DmShared.xNull(txtToDate.getText()));
        mrTemp.setItemCode(DmShared.xNull(txtHItemName.getTag()));
        mrTemp.setItemName(DmShared.xNull(txtHItemName.getText()));
        mrTemp.setSuppleirCode(DmShared.xNull(txtHSupplier.getTag()));
        mrTemp.setSuppleirName(DmShared.xNull(txtHSupplier.getText()));
        mrTemp.setStatusCode(DmShared.xNull(txtHStatus.getTag()));
        mrTemp.setStatusName(DmShared.xNull(txtHStatus.getText()));
        mrTemp.setPurchaseDocIds(DmShared.xNull(txtHPurchaseOrder.getTag()));
        mrTemp.setPurchaseReccIds(DmShared.xNull(txtHPurchaseOrder.getText()));

        purchaseApprovalCallBack.onOk(mrTemp);
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
        SrvAsync = new ServiceInvoker<PurchaseApproval_SrvAsync>(GWT.create(PurchaseApproval_Srv.class), PurchaseOrderApproval.class.getName());
        srvAsyncPO = new ServiceInvoker<PurchaseOrder_SrvAsync>(GWT.create(PurchaseOrder_Srv.class), PurchaseOrder.class.getName());
        textHelpEvents();
        txtKeyEvents();
        popupPanel.addAttachHandler(this);
        toolContainer.setTitle("Setting");
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
        onAdd();
    }

    private void onAdd() {
        txtToDate.setText(DmClient.getLoginDate());
        if (DmShared.xNull(txtHStatus.getTag()).equalsIgnoreCase("")) {
            txtHStatus.setTag("P");
            txtHStatus.setText("Pending");
        }
    }

}
