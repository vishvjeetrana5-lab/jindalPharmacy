package dataman.accounts.bankReconciliationEntry.client;

import dataman.accounts.bankReconciliationEntry.shared.*;
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
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public class Setting<T1, T2> extends Setting_Dsg implements ClickHandler, DmRootEvents, AttachEvent.Handler, BlurHandler {

    interface CallBack {

        public void onOk(Setting_Mdl setting_Mdl);

        public void onClose();

    }

    CallBack callBack;
    GrdBankReconciliationEntry_Mdl parentRow;
    ServiceInvoker<BankReconciliationEntry_SrvAsync> SrvAsync;
    BankReconciliationEntry bankReconciliationEntry;
    protected Setting_Mdl settingValue;

    public void addCallBack(CallBack reconciliationEntryCallBack) {
        this.callBack = reconciliationEntryCallBack;
    }

    public void show(GrdBankReconciliationEntry_Mdl parentRow, boolean editable) {
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

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtHBankName.setFocus(true);
            }
        });
    }

    public void hide() {
        this.popupPanel.hide();
        callBack.onClose();
    }

    @Override
    public void searchRecord(String key) {

    }

    @Override
    public void onClick(ClickEvent event) {
        Setting_Mdl mrTemp = new Setting_Mdl();

        if (event.getSource() == btnCancel) {
            popupPanel.hide();
        } else if (event.getSource() == btnOk) {

            if (DmShared.xNull(txtHBankName.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Bank Name is mandatory.", txtHBankName).show();
                return;
            }

            if ((!DmShared.xNull(txtHStatus.getTag()).equalsIgnoreCase("U")) && (DmShared.xNull(txtFromDate.getText()).equalsIgnoreCase(""))) {
                new DmAlertMessageBox("From Date is mandatory.", txtFromDate).show();
                return;
            }

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

            if (DmShared.xNull(txtHSite.getText()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Site is mandatory.", txtHSite).show();
                return;
            }

            mrTemp.setBankCode(DmShared.xNull(txtHBankName.getTag()));
            mrTemp.setBankName(DmShared.xNull(txtHBankName.getText()));
            mrTemp.setChkentriesCode(DmShared.xNull(txtHHideEntry.getTag()));
            mrTemp.setChkentriesName(DmShared.xNull(txtHHideEntry.getText()));
            mrTemp.setFromDate(DmShared.xNull(txtFromDate.getText()));
            mrTemp.setToDate(DmShared.xNull(txtToDate.getText()));
            mrTemp.setToDate(DmShared.xNull(txtToDate.getText()));
            mrTemp.setStatusCode(DmShared.xNull(txtHStatus.getTag()));
            mrTemp.setStatusName(DmShared.xNull(txtHStatus.getText()));
            mrTemp.setSiteName(DmShared.xNull(txtHSite.getText()));
            mrTemp.setSiteCode(DmShared.xNull(txtHSite.getTag()));

            popupPanel.hide();
            callBack.onOk(mrTemp);
        }
    }

    private void textHelpEvents() {

        txtHBankName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHBank_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHBankName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHBank_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHBankName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHBank_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHBankName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHBankName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHBank_Mdl tRow) {
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
                if (!DmShared.xNull(txtHStatus.getTag()).equalsIgnoreCase("U")) {
                    txtFromDate.setEnabled(true);
                    txtHHideEntry.setEnabled(false);
                } else {
                    txtFromDate.setEnabled(false);
                    txtFromDate.setText("");
                    txtHHideEntry.setEnabled(true);
                }
            }
        });

        txtHHideEntry.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHHideEntry(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHHideEntry).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHHideEntry.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHHideEntry).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });
        
        txtHSite.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSiteName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSite).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSite.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSite).show();
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

    public Setting() {
        super();
        SrvAsync = new ServiceInvoker<BankReconciliationEntry_SrvAsync>(GWT.create(BankReconciliationEntry_Srv.class), BankReconciliationEntry.class.getName());
        textHelpEvents();
        btnOk.addClickHandler(this);
        btnCancel.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        toolContainer.setTitle("Setting");
        toolContainer.addWidget(flwOk);

        txtFromDate.setEnabled(false);

        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();
        txtFromDate.addBlurHandler(this);
        txtToDate.addBlurHandler(this);
        onAdd();
        iniDefaultSetting();
    }

    private void onAdd() {
        txtHHideEntry.setTag("0");
        txtHHideEntry.setText("No");
        txtHStatus.setTag("U");
        txtHStatus.setText("Un-Cleared‎");
    }

    private void iniDefaultSetting() {
        SrvAsync.invokeRPC(true).getDefaultSetting(new AsyncCallback<DmResult<Setting_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<Setting_Mdl> result) {
                if (result.isStatus()) {
                    settingValue = result.getData();
                    txtHSite.setText(settingValue.getSiteName());
                    txtHSite.setTag(settingValue.getSiteCode());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }
}
