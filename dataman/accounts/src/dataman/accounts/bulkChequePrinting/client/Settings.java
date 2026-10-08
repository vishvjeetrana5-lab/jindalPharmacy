package dataman.accounts.bulkChequePrinting.client;

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
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.accounts.bulkChequePrinting.shared.BulkChequePrinting_Srv;
import dataman.accounts.bulkChequePrinting.shared.BulkChequePrinting_SrvAsync;
import dataman.accounts.bulkChequePrinting.shared.Settings_Mdl;
import dataman.accounts.bulkChequePrinting.shared.TxtHAccountName_Mdl;
import dataman.accounts.bulkChequePrinting.shared.TxtHBankName_Mdl;
import dataman.accounts.bulkChequePrinting.shared.TxtHVoucherNo_Mdl;
import dataman.accounts.bulkChequePrinting.shared.TxtHVoucherType_Mdl;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import java.util.List;

public class Settings extends Settings_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, ClickHandler, BlurHandler {

    interface CallBack {

        public void onOk(Settings_Mdl values);
    }
    CallBack callBack;
    Settings_Mdl setting_Mdl;
    ServiceInvoker<BulkChequePrinting_SrvAsync> SrvAsync;

    public Settings() {
        super();
        SrvAsync = new ServiceInvoker<BulkChequePrinting_SrvAsync>(GWT.create(BulkChequePrinting_Srv.class), BulkChequePrinting.class.getName());
        textHelpEvents();
        handlerEvents();
        initialize();
        toolContainer.setTitle("Setting");
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();

    }

    private void handlerEvents() {
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
        txtFromDate.addBlurHandler(this);
        txtToDate.addBlurHandler(this);
        popupPanel.addAttachHandler(this);
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtFromDate)) {
            txtFromDate.setText(DmClient.returnDate(txtFromDate.getText()));
        } else if (event.getSource().equals(txtToDate)) {
            txtToDate.setText(DmClient.returnDate(txtToDate.getText()));
        }
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
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
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            this.hide();
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            hide();
        } else if (event.getSource() == btnOk) {
            if (DmShared.dNull(txtFromDate.getText()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("From Date is mandatory", txtFromDate).show();
                return;
            }
            if (DmShared.dNull(txtToDate.getText()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("To Date is mandatory", txtToDate).show();
                return;
            }
            if (!DmShared.xNull(txtFromDate.getText()).equalsIgnoreCase("") && !DmShared.xNull(txtToDate.getText()).equalsIgnoreCase("")) {
                if ((DmShared.convertDate(txtToDate.getText()).getTime() < (DmShared.convertDate(txtFromDate.getText())).getTime())) {
                    new DmAlertMessageBox("From Date should be less than or equal to To Date.", txtFromDate).show();
                    return;
                }
            }
            if (DmShared.iNull(txtHVoucherType.getTag()).longValue() == 0) {
                new DmAlertMessageBox("Voucher Type is mandatory", txtHVoucherType).show();
                return;
            }
            if (DmShared.iNull(txtHBankName.getTag()).longValue() == 0) {
                new DmAlertMessageBox("Bank Name is mandatory", txtHVoucherType).show();
                return;
            }
            fillRecords();

        }
    }

    private void fillRecords() {
        setting_Mdl = new Settings_Mdl();
        setting_Mdl.setFromDate(DmShared.xNull(txtFromDate.getText()));
        setting_Mdl.setToDate(DmShared.xNull(txtToDate.getText()));
        setting_Mdl.setContraSub(txtHBankName.getTag());
        setting_Mdl.setFromDate(txtFromDate.getText());
        setting_Mdl.setFromDate(txtFromDate.getText());
        setting_Mdl.setVoucherNo(txtHVoucherNo.getText());
        setting_Mdl.setVoucherNoDocid(txtHVoucherNo.getTag());
        setting_Mdl.setAccountCode(txtHAccountName.getTag());
        setting_Mdl.setV_type(txtHVoucherType.getTag());
        setting_Mdl.setRecordTypeCode(txtHRecordsType.getTag());
        setting_Mdl.setRecordTypeName(txtHRecordsType.getText());
        callBack.onOk(setting_Mdl);
        hide();
    }

    public void show() {
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtHVoucherType.setFocus(true);
            }
        });
    }

    private void initialize() {
        txtToDate.setText(DmShared.dNull(DmClient.getLoginDate()));
        txtFromDate.setText(DmShared.dNull(DmClient.getLoginDate()));
        txtHRecordsType.setTag("1");
        txtHRecordsType.setText("Debit");

    }

    public void hide() {
        if (callBack != null) {
            this.popupPanel.hide();
        }
    }

    private void textHelpEvents() {
        txtHVoucherType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHVoucherType_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHVoucherType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHVoucherType_Mdl>>>() {
                    @Override
                    public void onFailure(Throwable caught) {

                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHVoucherType_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHVoucherType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHVoucherType_Mdl tRow) {
                if (blnStatus) {
                }
            }

        });

        txtHBankName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHBankName_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHBankName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHBankName_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHBankName_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHBankName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHBankName_Mdl tRow) {
                if (blnStatus) {
                    if (tRow != null) {
                    }
                }
            }
        });

        txtHAccountName.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<TxtHAccountName_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                if (DmShared.dNull(txtFromDate.getText()).equalsIgnoreCase("")) {
                    new DmAlertMessageBox("From Date is mandatory", txtFromDate).show();
                    return;
                }
                if (DmShared.dNull(txtToDate.getText()).equalsIgnoreCase("")) {
                    new DmAlertMessageBox("To Date is mandatory", txtToDate).show();
                    return;
                }
                if (DmShared.iNull(txtHVoucherType.getTag()).longValue() == 0) {
                    new DmAlertMessageBox("Voucher Type is mandatory", txtHVoucherType).show();
                    return;
                }
                if (DmShared.iNull(txtHBankName.getTag()).longValue() == 0) {
                    new DmAlertMessageBox("Bank Name is mandatory", txtHVoucherType).show();
                    return;
                }
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHAccountName(helper, blnToEndOfFile, txtHBankName.getTag(), txtFromDate.getText(), txtToDate.getText(), txtHVoucherNo.getTag(), new AsyncCallback<DmResultPageing<List<TxtHAccountName_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHAccountName_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHAccountName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHVoucherNo.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<TxtHVoucherNo_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                if (DmShared.dNull(txtFromDate.getText()).equalsIgnoreCase("")) {
                    new DmAlertMessageBox("From Date is mandatory", txtFromDate).show();
                    return;
                }
                if (DmShared.dNull(txtToDate.getText()).equalsIgnoreCase("")) {
                    new DmAlertMessageBox("To Date is mandatory", txtToDate).show();
                    return;
                }
                if (DmShared.iNull(txtHVoucherType.getTag()).longValue() == 0) {
                    new DmAlertMessageBox("Voucher Type is mandatory", txtHVoucherType).show();
                    return;
                }
                if (DmShared.iNull(txtHBankName.getTag()).longValue() == 0) {
                    new DmAlertMessageBox("Bank Name is mandatory", txtHVoucherType).show();
                    return;
                }
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHVoucherNo(helper, blnToEndOfFile, txtHBankName.getTag(), txtFromDate.getText(), txtToDate.getText(), new AsyncCallback<DmResultPageing<List<TxtHVoucherNo_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHVoucherNo_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHVoucherNo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHRecordsType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHRecordType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHRecordsType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
    }

}
