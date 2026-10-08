package aarogya.inventory.purchaseBillApproval.client;

import aarogya.inventory.purchaseBillApproval.shared.Setting_Mdl;
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
import aarogya.inventory.purchaseBillApproval.shared.*;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public class Setting<T1, T2> extends Setting_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, ClickHandler, BlurEvent.BlurHandler {

    interface PurchaseApprovalCallBack {

        public void onOk(Setting_Mdl approvalEntry_Mdl);
    }

    PurchaseApprovalCallBack purchaseApprovalCallBack;
    ServiceInvoker<PurchaseBillApproval_SrvAsync> SrvAsync;

    public void addPurchaseApprovalCallBack(PurchaseApprovalCallBack purchaseApprovalCallBack) {
        this.purchaseApprovalCallBack = purchaseApprovalCallBack;
    }

    public void show(boolean editable, String category) {
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        txtFromDate.setFocus(true);
        if (!DmShared.xNull(category).equalsIgnoreCase("")) {
            txtHCategory.setText(category);
        }
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

        txtHSupplier.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSupplier(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSupplier).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
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
                if (blnStatus) {

                }
            }
        });

        txtHGrnNo.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHGrnNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHGrnNo).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHGrnNo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHGrnNo).show();
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

        txtHBillNo.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHBillNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHPurchaseBillNo_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHBillNo).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHPurchaseBillNo_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHBillNo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHBillNo).show();
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

        txtHCategory.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHCategory(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHCategory_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCategory).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHCategory_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHCategory.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCategory).show();
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
        Setting_Mdl mrTemp = new Setting_Mdl();
        mrTemp.setFromDate(DmShared.xNull(txtFromDate.getText()));
        mrTemp.setToDate(DmShared.xNull(txtToDate.getText()));
        mrTemp.setSupplierCode(DmShared.xNull(txtHSupplier.getTag()));
        mrTemp.setSupplierName(DmShared.xNull(txtHSupplier.getText()));
        mrTemp.setCategoryCode(DmShared.xNull(txtHCategory.getTag()));
        mrTemp.setCategoryName(DmShared.xNull(txtHCategory.getText()));
        mrTemp.setGrnNoTag(DmShared.xNull(txtHGrnNo.getTag()));
        mrTemp.setGrnNoText(DmShared.xNull(txtHGrnNo.getText()));
        mrTemp.setItemCode(DmShared.xNull(txtHItemName.getTag()));
        mrTemp.setItemName(DmShared.xNull(txtHItemName.getText()));
        mrTemp.setBillDocIds(DmShared.xNull(txtHBillNo.getTag()));
        mrTemp.setBillReccIds(DmShared.xNull(txtHBillNo.getText()));
        mrTemp.setStatusCode(DmShared.xNull(txtHStatus.getTag()));
        mrTemp.setStatusName(DmShared.xNull(txtHStatus.getText()));

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
        SrvAsync = new ServiceInvoker<PurchaseBillApproval_SrvAsync>(GWT.create(PurchaseBillApproval_Srv.class), PurchaseBillApproval.class.getName());
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
