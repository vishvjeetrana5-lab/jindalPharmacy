package aarogya.pharmacy.stockLedger.client;

import aarogya.pharmacy.stockLedger.shared.BatchNo_Mdl;
import aarogya.pharmacy.stockLedger.shared.Setting_Mdl;
import aarogya.pharmacy.stockLedger.shared.StockLedger_Srv;
import aarogya.pharmacy.stockLedger.shared.StockLedger_SrvAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
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
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public class Setting<T1, T2> extends Setting_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, ClickHandler, BlurHandler {

    interface CallBack {

        public void onOk(Setting_Mdl setting_Mdl);

        public void onClose();
    }

    CallBack callBack;
    ServiceInvoker<StockLedger_SrvAsync> SrvAsync;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtFromDate) {
            txtFromDate.setText(DmClient.returnDate(txtFromDate.getText()));
        } else if (event.getSource() == txtToDate) {
            txtToDate.setText(DmClient.returnDate(txtToDate.getText()));
        }
    }

    public void show(Setting_Mdl setting_Mdl) {
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtToDate.setFocus(true);
            }
        });

        setData(setting_Mdl);
    }

    private void setData(Setting_Mdl setting_Mdl) {
        txtFromDate.setText(DmShared.dNull(setting_Mdl.getFromDate()));
        txtToDate.setText(DmShared.dNull(setting_Mdl.getToDate()));
        txtHMedicineCategory.setTag(DmShared.xNull(setting_Mdl.getMedicineCatCode()));
        txtHMedicineCategory.setText(DmShared.xNull(setting_Mdl.getMedicineCatName()));
        txtHMedicineGroup.setTag(DmShared.xNull(setting_Mdl.getMedicineGroupCode()));
        txtHMedicineGroup.setText(DmShared.xNull(setting_Mdl.getMedicineGroupName()));
        txtHMedicineCompany.setTag(DmShared.xNull(setting_Mdl.getMedicineCompanyCode()));
        txtHMedicineCompany.setText(DmShared.xNull(setting_Mdl.getMedicineCompanyName()));
        txtHMedicineName.setTag(DmShared.xNull(setting_Mdl.getMedicineCode()));
        txtHMedicineName.setText(DmShared.xNull(setting_Mdl.getMedicineName()));
        txtHDepartment.setTag(DmShared.xNull(setting_Mdl.getDepartmentCode()));
        txtHDepartment.setText(DmShared.xNull(setting_Mdl.getDepartmentName()));
        txtHSiteName.setTag(DmShared.xNull(setting_Mdl.getSiteCode()));
        txtHSiteName.setText(DmShared.xNull(setting_Mdl.getSiteName()));
        txtHValueOn.setTag(DmShared.xNull(setting_Mdl.getValueOn()));
        txtHValueOn.setText(DmShared.xNull(setting_Mdl.getValueOn()));
        txtHNarcotics.setTag(DmShared.xNull(setting_Mdl.getNarcoticsCode()));
        txtHNarcotics.setText(DmShared.xNull(setting_Mdl.getNarcoticsName()));
        txtHConsignmentItem.setTag(DmShared.xNull(setting_Mdl.getConsignmentItemCode()));
        txtHConsignmentItem.setText(DmShared.xNull(setting_Mdl.getConsignmentItemName()));
        txtHHighValue.setTag(DmShared.xNull(setting_Mdl.getHighValueCode()));
        txtHHighValue.setText(DmShared.xNull(setting_Mdl.getHighValueName()));
        txtHBilledItem.setTag(DmShared.xNull(setting_Mdl.getBilledItemCode()));
        txtHBilledItem.setText(DmShared.xNull(setting_Mdl.getBilledItemName()));
        txtHFlowThrough.setTag(DmShared.xNull(setting_Mdl.getFlowThroughCode()));
        txtHFlowThrough.setText(DmShared.xNull(setting_Mdl.getFlowThroughName()));
        txtHLifeSaving.setTag(DmShared.xNull(setting_Mdl.getLifeSavingCode()));
        txtHLifeSaving.setText(DmShared.xNull(setting_Mdl.getLifeSavingName()));

    }

    public void hide() {
        this.popupPanel.hide();
        callBack.onClose();
    }

    private void textHelpEvents() {

        txtHMedicineCategory.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHMedicineCateory(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMedicineCategory).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHMedicineCategory.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMedicineCategory).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHMedicineGroup.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHMedicineGroup(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMedicineGroup).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHMedicineGroup.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMedicineGroup).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHMedicineCompany.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHMedicineCompany(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMedicineCompany).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHMedicineCompany.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMedicineCompany).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHMedicineName.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHMedicineName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMedicineName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHMedicineName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMedicineName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHDepartment.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
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
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });

        txtHSiteName.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSiteName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSiteName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSiteName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSiteName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHValueOn.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHValueOn(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHValueOn).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHValueOn.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHValueOn).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });

        txtHLifeSaving.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, false, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHLifeSaving).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHLifeSaving.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHLifeSaving).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHHighValue.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, false, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHHighValue).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHHighValue.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHHighValue).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHFlowThrough.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, false, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHFlowThrough).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHFlowThrough.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHFlowThrough).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHNarcotics.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, false, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHNarcotics).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHNarcotics.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHNarcotics).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHConsignmentItem.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, false, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHConsignmentItem).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHConsignmentItem.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHConsignmentItem).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHBilledItem.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, false, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHBilledItem).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHBilledItem.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHBilledItem).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            popupPanel.hide();
        } else if (event.getSource() == btnOk) {
            if (DmShared.dNull(txtFromDate.getText()).trim().equals("")) {
                new DmAlertMessageBox("From Date is mandatory.", txtFromDate).show();
                return;
            }
            if (DmShared.dNull(txtToDate.getText()).trim().equals("")) {
                new DmAlertMessageBox("To Date is mandatory.", txtToDate).show();
                return;
            }
            if (DmShared.xNull(txtHSiteName.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Site Name is mandatory.", txtHSiteName).show();
                return;
            }
            if (DmShared.xNull(txtHValueOn.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Value On is mandatory.", txtHValueOn).show();
                return;
            }
            fillRecords();
        }
    }

    private void fillRecords() {
        Setting_Mdl mrTemp = new Setting_Mdl();
        mrTemp.setFromDate(DmShared.dNull(txtFromDate.getText()));
        mrTemp.setToDate(DmShared.dNull(txtToDate.getText()));
        mrTemp.setMedicineGroupCode(DmShared.xNull(txtHMedicineGroup.getTag()));
        mrTemp.setMedicineGroupName(DmShared.xNull(txtHMedicineGroup.getText()));
        mrTemp.setMedicineCatCode(DmShared.xNull(txtHMedicineCategory.getTag()));
        mrTemp.setMedicineCatName(DmShared.xNull(txtHMedicineCategory.getText()));
        mrTemp.setMedicineCompanyCode(DmShared.xNull(txtHMedicineCompany.getTag()));
        mrTemp.setMedicineCompanyName(DmShared.xNull(txtHMedicineCompany.getText()));
        mrTemp.setMedicineCode(DmShared.xNull(txtHMedicineName.getTag()));
        mrTemp.setMedicineName(DmShared.xNull(txtHMedicineName.getText()));
        mrTemp.setDepartmentCode(DmShared.xNull(txtHDepartment.getTag()));
        mrTemp.setDepartmentName(DmShared.xNull(txtHDepartment.getText()));
        mrTemp.setSiteCode(DmShared.xNull(txtHSiteName.getTag()));
        mrTemp.setSiteName(DmShared.xNull(txtHSiteName.getText()));
        mrTemp.setValueOn(DmShared.xNull(txtHValueOn.getTag()));
        mrTemp.setNarcoticsCode(DmShared.xNull(txtHNarcotics.getTag()));
        mrTemp.setNarcoticsName(DmShared.xNull(txtHNarcotics.getText()));
        mrTemp.setConsignmentItemCode(DmShared.xNull(txtHConsignmentItem.getTag()));
        mrTemp.setConsignmentItemName(DmShared.xNull(txtHConsignmentItem.getText()));
        mrTemp.setHighValueCode(DmShared.xNull(txtHHighValue.getTag()));
        mrTemp.setHighValueName(DmShared.xNull(txtHHighValue.getText()));
        mrTemp.setBilledItemCode(DmShared.xNull(txtHBilledItem.getTag()));
        mrTemp.setBilledItemName(DmShared.xNull(txtHBilledItem.getText()));
        mrTemp.setFlowThroughCode(DmShared.xNull(txtHFlowThrough.getTag()));
        mrTemp.setFlowThroughName(DmShared.xNull(txtHFlowThrough.getText()));
        mrTemp.setLifeSavingCode(DmShared.xNull(txtHLifeSaving.getTag()));
        mrTemp.setLifeSavingName(DmShared.xNull(txtHLifeSaving.getText()));

        callBack.onOk(mrTemp);
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
        SrvAsync = new ServiceInvoker<StockLedger_SrvAsync>(GWT.create(StockLedger_Srv.class), StockLedger.class.getName());
        popupPanel.addAttachHandler(this);
        toolContainer.setTitle("Setting");
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();
        textHelpEvents();
        manageEvents();
        onAdd();
    }

    private void manageEvents() {
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
        txtFromDate.addBlurHandler(this);
        txtToDate.addBlurHandler(this);

    }

    private void onAdd() {
        txtFromDate.setText(DmClient.getStartDate());
        txtToDate.setText(DmClient.getLoginDate());
        txtHValueOn.setTag("Landed Rate");
        txtHValueOn.setText("Landed Rate");
    }

}
