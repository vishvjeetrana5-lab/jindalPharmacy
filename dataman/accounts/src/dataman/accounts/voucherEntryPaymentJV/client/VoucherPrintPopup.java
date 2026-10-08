package dataman.accounts.voucherEntryPaymentJV.client;

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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.accounts.voucherEntryPaymentJV.shared.TxtHAccountName_Mdl;
import dataman.accounts.voucherEntryPaymentJV.shared.VoucherEntryPrint_Mdl;
import dataman.accounts.voucherEntryPaymentJV.shared.VoucherEntryPaymentJV_Srv;
import dataman.accounts.voucherEntryPaymentJV.shared.VoucherEntryPaymentJV_SrvAsync;
import dataman.accounts.voucherEntryPaymentJV.shared.VoucherPrintPaymentJV_Mdl;
import dataman.accounts.voucherEntryPaymentJV.shared.VoucherTypePrintPaymentJV_Mdl;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import java.util.HashMap;
import java.util.List;

public class VoucherPrintPopup extends VoucherPrintPopup_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler, BlurHandler {

    public interface CallBack {

        public void ok(VoucherPrintPaymentJV_Mdl mrTemp);
    }

    private CallBack callBack;
    private ServiceInvoker<VoucherEntryPaymentJV_SrvAsync> SrvAsync;
    private String fromDte, toDte, voucherCat, VoucherTypeTag, voucherTypText, voucherNoTg, voucherNoTxt, printType, headerAccountName, headerAccountCode, widgetName;
    boolean flag;
    final LibraryVoucherCategory lvc = new LibraryVoucherCategory();
    private SetWidgetInvoker swi;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(String fromDate, String toDate, String voucherCategory, String voucherTypeTag,
            String voucherTypeText, String voucherNoText, String voucherNoTag, String headerAccountName, String headerAccountCode, String widgetName, SetWidgetInvoker swi) {
        flag = false;

        chkPayment.setValue(false);
        chkBankLetter.setValue(false);
        chkCheque.setValue(false);
        this.widgetName = widgetName;
        this.swi = swi;
        this.fromDte = fromDate;
        this.toDte = toDate;
        this.voucherCat = voucherCategory;
        this.VoucherTypeTag = voucherTypeTag;
        this.voucherTypText = voucherTypeText;
        this.voucherNoTg = voucherNoTag;
        this.voucherNoTxt = voucherNoText;
        this.headerAccountCode = headerAccountCode;
        this.headerAccountName = headerAccountName;

        flwOk.clear();
        flwOk.add(btnOk);
        displayPrintButton();
        txtFromDate.setText(DmShared.xNull(fromDte));
        txtToDate.setText(DmShared.xNull(toDte));
        txtHVoucherNo.setText(DmShared.xNull(voucherNoTxt));
        txtHVoucherNo.setTag(DmShared.xNull(voucherNoTg));
        txtHHeaderAccountName.setText(DmShared.xNull(headerAccountName));
        txtHHeaderAccountName.setTag(DmShared.iNull(headerAccountCode).toString());
        txtHVoucherType.setText(DmShared.xNull(voucherTypText));
        txtHVoucherType.setTag(DmShared.xNull(VoucherTypeTag));
        txtHVoucherType.setTag2nd(DmShared.xNull(voucherCat));

        manageBankAcName(voucherCategory);
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtFromDate.setFocus(true);
            }
        });

    }

    private void manageBankAcName(String voucherCategory) {
        if (DmShared.xNull(voucherCategory).equalsIgnoreCase(lvc.BANK_RECEIPT) || DmShared.xNull(voucherCategory).equalsIgnoreCase(lvc.BANK_PAYMENT)) {
            if (DmShared.xNull(voucherCategory).trim().equalsIgnoreCase(lvc.BANK_PAYMENT)) {
                lblAccName.setText("Credit A/c");
            } else if (DmShared.xNull(voucherCategory).trim().equalsIgnoreCase(lvc.BANK_RECEIPT)) {
                lblAccName.setText("Debit A/c");
            }
        } else {
            txtHHeaderAccountName.setText("");
            txtHHeaderAccountName.setTag("");
            txtHHeaderAccountName.setTag2nd("");
            flwlblAccountName.clear();
        }
    }

    public void hide() {
        this.popupPanel.hide();
    }

    private void textHelpEvents() {
        txtHVoucherType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<VoucherTypePrintPaymentJV_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHVoucherTypePrint(voucherCat, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<VoucherTypePrintPaymentJV_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHVoucherType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<VoucherTypePrintPaymentJV_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHVoucherType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHVoucherType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, VoucherTypePrintPaymentJV_Mdl tRow) {
                if (blnStatus && tRow != null) {
                    displayPrintButton();
                }
            }

        });
        txtHVoucherNo.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHVoucherNoPrint(helper, blnToEndOfFile, txtFromDate.getText(), txtToDate.getText(), txtHVoucherType.getTag(), txtHAccountName.getTag(), new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHVoucherNo).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHVoucherNo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHVoucherNo).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHAccountName.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<VoucherEntryPrint_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHAccountNamePrint(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<VoucherEntryPrint_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAccountName).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<VoucherEntryPrint_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHAccountName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHAccountName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHHeaderAccountName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHAccountName_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHAccountName(helper, blnToEndOfFile, DmShared.xNull(txtHVoucherType.getTag()), new AsyncCallback<DmResultPageing<List<TxtHAccountName_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHAccountName_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHHeaderAccountName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHAccountName_Mdl t1Row) {
            }
        });
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            popupPanel.hide();
        } else if (event.getSource() == btnPayment) {
            printType = DmShared.xNull(lblPayment.getText());
            print(false);
        } else if (event.getSource() == btnBankLetter) {
            printType = DmShared.xNull(lblBankLetter.getText());
            print(false);
        } else if (event.getSource() == btnCheque) {
            printType = DmShared.xNull(lblCheque.getText());
            print(false);
        } else if (event.getSource() == btnOk) {
            if (DmShared.xNull(txtFromDate.getText()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("From Date is mandatory", txtFromDate).show();
                return;
            }
            if (DmShared.xNull(txtToDate.getText()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("To Date is mandatory", txtFromDate).show();
                return;
            }

            if (DmShared.convertDate(txtFromDate.getText()).getTime() > DmShared.convertDate(txtToDate.getText()).getTime()) {
                new DmAlertMessageBox("From Date should be less than or equal to To Date.", txtFromDate).show();
                return;
            }

            ok();
        }
    }

    private void ok() {
        VoucherPrintPaymentJV_Mdl mrTemp = fillMdl();
        if (mrTemp != null) {
            callBack.ok(mrTemp);
            popupPanel.hide();
        }
    }

    private VoucherPrintPaymentJV_Mdl fillMdl() {
        VoucherPrintPaymentJV_Mdl mrTemp = new VoucherPrintPaymentJV_Mdl();
        mrTemp.setFromDate(DmShared.xNull(txtFromDate.getText()));
        mrTemp.setToDate(DmShared.xNull(txtToDate.getText()));
        mrTemp.setAccountCode(DmShared.xNull(txtHAccountName.getTag()));
        mrTemp.setAccountName(DmShared.xNull(txtHAccountName.getText()));
        mrTemp.setHeaderAccountCode(DmShared.xNull(txtHHeaderAccountName.getTag()));
        mrTemp.setHeaderAccountName(DmShared.xNull(txtHHeaderAccountName.getText()));
        mrTemp.setVoucherNo(DmShared.xNull(txtHVoucherNo.getText()));
        mrTemp.setVoucherNoCode(DmShared.xNull(txtHVoucherNo.getTag()));
        mrTemp.setVoucherTypeTag(DmShared.xNull(txtHVoucherType.getTag()));
        mrTemp.setVoucherTypeText(DmShared.xNull(txtHVoucherType.getText()));
        mrTemp.setVoucherCategory(DmShared.xNull(txtHVoucherType.getTag2nd()));
        mrTemp.setPaymentVoucherChecked(chkPayment.getValue());
        mrTemp.setBankLetterChecked(chkBankLetter.getValue());
        mrTemp.setChqPrintChecked(chkCheque.getValue());
        if (DmShared.xNull(printType).equalsIgnoreCase(lblCheque.getText())) {
            if (DmShared.iNull(txtHHeaderAccountName.getTag()).longValue() == 0) {
                new DmAlertMessageBox(lblAccName.getText() + " is mandatory.", txtHHeaderAccountName).show();
                return null;
            }
        }

        return mrTemp;
    }

    private void print(final boolean printToPrinter) {
        if (!DmShared.xNull(printType).equalsIgnoreCase("Cheque")) {
            String title = "";
            if (printType.equalsIgnoreCase("BankLetter")) {
                title = "Bank Letter";
            } else {
                title = this.widgetName;
            }
            HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
            hashMapFilter.put("fromDate", new FieldUDT_Mdl("From Date", txtFromDate.getText(), txtFromDate.getText()));
            hashMapFilter.put("toDate", new FieldUDT_Mdl("To Date", txtToDate.getText(), txtToDate.getText()));
            if (!printType.equalsIgnoreCase("BankLetter")) {
                hashMapFilter.put("category", new FieldUDT_Mdl("Category", txtHVoucherType.getTag2nd(), txtHVoucherType.getTag2nd()));
                printType = "";
            }

            hashMapFilter.put("voucherType", new FieldUDT_Mdl("Voucher Type", DmShared.xNull(txtHVoucherType.getText()), DmShared.xNull(txtHVoucherType.getTag())));
            hashMapFilter.put("voucherNo", new FieldUDT_Mdl("Voucher No.", txtHVoucherNo.getText(), txtHVoucherNo.getTag()));
            hashMapFilter.put("contraSub", new FieldUDT_Mdl(DmShared.xNull(lblAccName.getText()), txtHHeaderAccountName.getText(), txtHHeaderAccountName.getTag()));
            new OpenSourceReport(txtHVoucherType.getTag(), printType, title, this.widgetName, hashMapFilter, printToPrinter, new OpenSourceReport.CallBack() {
                @Override
                public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty) {
                    DmClient.localSettingPrintMap(reportProperty, "Accounts", widgetName, "Print");
                    return reportProperty;
                }
            }, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));

            hide();
        } else if (DmShared.xNull(printType).equalsIgnoreCase("Cheque")) {
            VoucherPrintPaymentJV_Mdl mrTemp = fillMdl();
            if (mrTemp != null) {
                SrvAsync.invokeRPC().printRecords(mrTemp, new AsyncCallback<DmResult<String>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResult<String> result) {
                        if (result.isStatus()) {
                            new ReportPrint(result.getData(), printToPrinter, new ReportPrint.CallBack() {
                                @Override
                                public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty) {
                                    DmClient.localSettingPrintMap(reportProperty, "Accounts", widgetName, "Print");
                                    return reportProperty;
                                }
                            }, swi);
                            hide();

                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (Button) null).show();
                        }
                    }
                });
            }
        }
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
    public Widget asWidget() {
        return this.popupPanel;
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtFromDate)) {
            txtFromDate.setText(DmClient.returnDate(txtFromDate.getText()));
        }
        if (event.getSource().equals(txtToDate)) {
            txtToDate.setText(DmClient.returnDate(txtToDate.getText()));
        }

    }

    private void handlerEvents() {
        popupPanel.addAttachHandler(this);
        btnBankLetter.addClickHandler(this);
        btnPayment.addClickHandler(this);
        btnCheque.addClickHandler(this);
        txtFromDate.addBlurHandler(this);
        txtToDate.addBlurHandler(this);
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
    }

    public VoucherPrintPopup() {
        super();
        SrvAsync = new ServiceInvoker<VoucherEntryPaymentJV_SrvAsync>(GWT.create(VoucherEntryPaymentJV_Srv.class), VoucherEntryPaymentJV.class.getName());
        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();
        textHelpEvents();
        handlerEvents();
    }

    private void displayPrintButton() {
        if (DmShared.xNull(voucherCat).equalsIgnoreCase(lvc.BANK_PAYMENT)) {
            flwPayment.add(chkPayment);
            flwPayment.add(btnPayment);
            flwBankLetter.add(chkBankLetter);
            flwBankLetter.add(btnBankLetter);
            flwCheque.add(chkCheque);
            flwCheque.add(btnCheque);
        } else {
            flwPayment.clear();
            flwBankLetter.clear();
            flwCheque.clear();
        }
    }
}
