package aarogya.pharmacy.duplicateBarcodeGeneration.client;

import aarogya.pharmacy.duplicateBarcodeGeneration.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import java.util.List;

public class Setting<T1, T2> extends Setting_Dsg implements ClickHandler, DmRootEvents, AttachEvent.Handler, BlurHandler {

    @Override
    public void searchRecord(String key) {

    }

    interface CallBack {

        public void onOk(Setting_Mdl setting_Mdl);

        public void onClose();

    }

    CallBack callBack;
    GrdDuplicateBarcodeGeneration_Mdl parentRow;
    ServiceInvoker<DuplicateBarcodeGeneration_SrvAsync> SrvAsync;
    DuplicateBarcodeGeneration bankReconciliationEntry;

    public void addCallBack(CallBack reconciliationEntryCallBack) {
        this.callBack = reconciliationEntryCallBack;
    }

    public void show(GrdDuplicateBarcodeGeneration_Mdl parentRow, boolean editable, Setting_Mdl mdl) {
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        this.parentRow = parentRow;
        flwOk.clear();
        flwOk.setVisible(false);
        if (editable) {
            flwOk.add(btnOk);
            flwOk.setVisible(true);
        }
        fillData(mdl);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtFromDate.setFocus(true);
            }
        });
    }

    private void fillData(Setting_Mdl mdl) {

        if (DmShared.xNull(txtFromDate.getText()).equalsIgnoreCase("")) {
            txtFromDate.setText(DmClient.getLoginDate());
        } else {
            txtFromDate.setText(DmShared.dNull(mdl.getFromDate()));
        }
        if (DmShared.xNull(txtToDate.getText()).equalsIgnoreCase("")) {
            txtToDate.setText(DmClient.getLoginDate());
        } else {
            txtToDate.setText(DmShared.dNull(mdl.getToDate()));
        }
        txtHPBillNo.setTag(DmShared.xNull(mdl.getpBillNoCode()).toString());
        txtHPBillNo.setText(DmShared.xNull(mdl.getpBillNo()));
        txtHDepartment.setTag(DmShared.xNull(mdl.getDepartmentCode()).toString());
        txtHDepartment.setText(DmShared.xNull(mdl.getDepartment()));
        txtHBarCode.setTag(DmShared.xNull(mdl.getBarCodeDocid()).toString());
        txtHBarCode.setText(DmShared.xNull(mdl.getBarCodeRecid()));

        txtHItem.setTag(DmShared.xNull(mdl.getItemCode()).toString());
        txtHItem.setText(DmShared.xNull(mdl.getItem()));

        txtHColor.setTag(DmShared.xNull(mdl.getColorCode()).toString());
        txtHColor.setText(DmShared.xNull(mdl.getColor()));

        txtHSize.setTag(DmShared.xNull(mdl.getSizeCode()).toString());
        txtHSize.setText(DmShared.xNull(mdl.getSize()));

        txtHManufacturingDate.setTag(DmShared.dNull(mdl.getManufacturingDate()));
        txtHManufacturingDate.setText(DmShared.dNull(mdl.getManufacturingDate()));
        txtHExpiryDate.setTag(DmShared.dNull(mdl.getExpiryDate()));
        txtHExpiryDate.setText(DmShared.dNull(mdl.getExpiryDate()));

        if (!DmShared.xNull(txtHSaleRate.getText()).equalsIgnoreCase("")) {
            txtHSaleRate.setTag(DmShared.vNull(mdl.getSaleRate()).toString());
            txtHSaleRate.setText(DmShared.vNull(mdl.getSaleRate()).toString());
        }
        if (!DmShared.xNull(txtHMRP.getText()).equalsIgnoreCase("")) {
            txtHMRP.setTag(DmShared.vNull(mdl.getMrp()).toString());
            txtHMRP.setText(DmShared.vNull(mdl.getMrp()).toString());
        }
        if (!DmShared.xNull(txtHBatchNo.getText()).equalsIgnoreCase("")) {
            txtHBatchNo.setTag(DmShared.xNull(mdl.getBatch()).toString());
            txtHBatchNo.setText(DmShared.xNull(mdl.getBatch()));
        }
        if (!DmShared.xNull(txtHPBillNo.getText()).equalsIgnoreCase("")) {
            txtHPBillNo.setTag(DmShared.xNull(mdl.getpBillNoCode()).toString());
            txtHPBillNo.setText(DmShared.xNull(mdl.getpBillNo()));
        }

    }

    public void hide() {
        this.popupPanel.hide();
        callBack.onClose();
    }

    @Override
    public void onClick(ClickEvent event) {

        if (event.getSource() == btnCancel) {
            popupPanel.hide();
        } else if (event.getSource() == btnOk) {
            onOK();
        }
    }

    private void onOK() {
        Setting_Mdl mrTemp = new Setting_Mdl();
        if (DmShared.xNull(txtToDate.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("To Date is mandatory.", txtToDate).show();
            return;
        }

        if (!txtFromDate.getText().trim().equalsIgnoreCase("") && !txtToDate.getText().trim().equalsIgnoreCase("")) {
            if ((DmShared.convertDate(txtToDate.getText()).getTime() < (DmShared.convertDate(txtFromDate.getText())).getTime())) {
                new DmAlertMessageBox("From Date should be less than or equal to To Date.", txtToDate).show();
                return;
            }
        }
        mrTemp.setFromDate(DmShared.dNull(txtFromDate.getText()));
        mrTemp.setToDate(DmShared.dNull(txtToDate.getText()));

        mrTemp.setDepartmentCode(DmShared.xNull(txtHDepartment.getTag()).toString());
        mrTemp.setDepartment(DmShared.xNull(txtHDepartment.getText()));

        mrTemp.setBarCodeDocid(DmShared.xNull(txtHBarCode.getTag()).toString());
        mrTemp.setBarCodeRecid(DmShared.xNull(txtHBarCode.getText()));

        mrTemp.setItemCode(DmShared.xNull(txtHItem.getTag()).toString());
        mrTemp.setItem(DmShared.xNull(txtHItem.getText()));

        mrTemp.setBatch(DmShared.xNull(txtHBatchNo.getTag()));

        mrTemp.setColorCode(DmShared.xNull(txtHColor.getTag()).toString());
        mrTemp.setColor(DmShared.xNull(txtHColor.getText()));

        mrTemp.setpBillNoCode(DmShared.xNull(txtHPBillNo.getTag()).toString());
        mrTemp.setpBillNo(DmShared.xNull(txtHPBillNo.getText()));

        mrTemp.setSizeCode(DmShared.xNull(txtHSize.getTag()).toString());
        mrTemp.setSize(DmShared.xNull(txtHSize.getText()));

        mrTemp.setManufacturingDate(DmShared.dNull(txtHManufacturingDate.getTag()));
        mrTemp.setExpiryDate(DmShared.dNull(txtHExpiryDate.getTag()));

        mrTemp.setSaleRate(DmShared.xNull(txtHSaleRate.getTag()));
        mrTemp.setMrp(DmShared.xNull(txtHMRP.getTag()));

        popupPanel.hide();
        callBack.onOk(mrTemp);
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide();
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

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtFromDate) {
            txtFromDate.setText(DmClient.returnDate(txtFromDate.getText()));
        }
        if (event.getSource() == txtToDate) {
            txtToDate.setText(DmClient.returnDate(txtToDate.getText()));
        }
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    private void textHelpEvents() {
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
            }
        });
        txtHBarCode.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHBarCodeHeader(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHBarCode).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHBarCode.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHBarCode).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHItem.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHItem(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHItem).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHItem.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHItem).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHBatchNo.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHBatchNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHBatchNo).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHBatchNo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHBatchNo).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHColor.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHColor(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHColor).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHColor.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHColor).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHSize.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSize(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSize).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSize.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSize).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHManufacturingDate.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHManufacturingDate(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHManufacturingDate).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHManufacturingDate.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHManufacturingDate).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHExpiryDate.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHExpiryDate(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHExpiryDate).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHExpiryDate.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHExpiryDate).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHSaleRate.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSaleRate(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSaleRate).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSaleRate.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSaleRate).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHMRP.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHMRP(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMRP).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHMRP.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMRP).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHPBillNo.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHPBillNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<PBillNoH_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPBillNo).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<PBillNoH_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHPBillNo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPBillNo).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
    }

    public Setting() {
        super();
        SrvAsync = new ServiceInvoker<DuplicateBarcodeGeneration_SrvAsync>(GWT.create(DuplicateBarcodeGeneration_Srv.class), DuplicateBarcodeGeneration.class.getName());
        textHelpEvents();
        btnOk.addClickHandler(this);
        btnCancel.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        toolContainer.setTitle("Setting");
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();
        txtFromDate.addBlurHandler(this);
        txtToDate.addBlurHandler(this);
    }

}
