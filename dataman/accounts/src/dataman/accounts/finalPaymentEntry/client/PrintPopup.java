package dataman.accounts.finalPaymentEntry.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.accounts.finalPaymentEntry.shared.ChequePrint_Mdl;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import java.util.HashMap;
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;
import dataman.libraryMenu.self.shared.lmAccounts;

public class PrintPopup extends PrintPopup_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler {

    public PrintPopup() {
        super();
        eventHandler();
    }

    public interface CallBack {

        public void hide(boolean isCancel);
    }

    private CallBack callBack;
    private FinalPaymentEntry finalPaymentEntry;
    private String moduleName = new lmAccounts().moduleName;

    private void eventHandler() {
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        btnCheque.addClickHandler(this);
        btnSlip.addClickHandler(this);
        btnVoucher.addClickHandler(this);
        btnPaymentAdvice.addClickHandler(this);

    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(FinalPaymentEntry finalPaymentEntry) {
        this.finalPaymentEntry = finalPaymentEntry;
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        btnOk.setFocus(true);
        boolean enable = DmShared.xNull(finalPaymentEntry.userControl.getTextBlog(finalPaymentEntry.approvedByBlog)).equalsIgnoreCase("") ? false : true;
        btnCheque.setEnabled(enable);
        cbCheckPrint.setEnabled(enable);
        btnPaymentAdvice.setEnabled(enable);
        cbPaymentAdvicePrint.setEnabled(enable);

    }

    public void hide() {
        this.popupPanel.hide();
    }

    private void hide(boolean isCancel) {
        hide();
        callBack.hide(isCancel);
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            hide(true);
        } else if (event.getSource() == btnCheque) {
            print(false, btnCheque);
            hide(false);
        } else if (event.getSource() == btnSlip) {
            print(false, btnSlip);
            hide(false);
        } else if (event.getSource() == btnVoucher) {
            print(false, btnVoucher);
            hide(false);
        } else if (event.getSource() == btnPaymentAdvice) {
            print(false, btnPaymentAdvice);
            hide(false);
        } else if (event.getSource() == btnOk) {
            if (cbCheckPrint.getValue()) {
                print(true, btnCheque);
            }
            if (cbSlipPrint.getValue()) {
                print(true, btnSlip);
            }
            if (cbVoucherPrint.getValue()) {

                print(true, btnVoucher);

            }
            if (cbPaymentAdvicePrint.getValue()) {

                print(true, btnPaymentAdvice);

            }

            hide(true);
        }
    }

    private void print(boolean printToPrinter, Button btn) {
        if (btn.equals(btnCheque)) {

            if (!DmShared.xNull(this.finalPaymentEntry.bankLetterDocid).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Cheque can't be print because Bank Letter has been created.", (DmTextBox) null).show();
                return;
            } else {
                printCheque(printToPrinter);
            }

//            printCheque(printToPrinter);
//            STClient.updatePrintStatus("patientService", "isPrint", DmShared.xNull(advanceEntry.topControl.getDocId()));
        } else if (btn.equals(btnSlip)) {
            HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
            hashMapFilter.put("docId", new FieldUDT_Mdl("Pay.No.", DmShared.xNull(finalPaymentEntry.txtPayNo.getText()), DmShared.xNull(finalPaymentEntry.topControl.getDocId())));
            hashMapFilter.put("bankLedgerBal", new FieldUDT_Mdl("Bank/Cash Ledger Bal", DmShared.xNull(finalPaymentEntry.txtBankCashBalance.getText()), DmShared.xNull(finalPaymentEntry.txtBankCashBalance.getText())));
            hashMapFilter.put("partyAcBal", new FieldUDT_Mdl("Party A/C Bal", DmShared.xNull(finalPaymentEntry.txtPartyAcBalance.getText()), DmShared.xNull(finalPaymentEntry.txtPartyAcBalance.getText())));
            new OpenSourceReport(finalPaymentEntry.txtVoucherType.getTag(), btnSlip.getTitle(), "Final Payment Slip", finalPaymentEntry.widgetName, hashMapFilter, printToPrinter, new OpenSourceReport.CallBack() {
                @Override
                public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty) {
                    DmClient.localSettingPrintMap(reportProperty, moduleName, finalPaymentEntry.widgetName, btnSlip.getText());
                    return reportProperty;
                }
            }, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));

        } else if (btn.equals(btnVoucher)) {
            HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
            hashMapFilter.put("docId", new FieldUDT_Mdl("Final Payment No.", DmShared.xNull(finalPaymentEntry.txtPayNo.getText()), DmShared.xNull(finalPaymentEntry.topControl.getDocId())));

            new OpenSourceReport(finalPaymentEntry.txtVoucherType.getTag(), btnVoucher.getTitle(), "Voucher", finalPaymentEntry.widgetName, hashMapFilter, printToPrinter, new OpenSourceReport.CallBack() {
                @Override
                public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty) {
                    DmClient.localSettingPrintMap(reportProperty, moduleName, finalPaymentEntry.widgetName, btnVoucher.getText());
                    return reportProperty;
                }
            }, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));

        } else if (btn.equals(btnPaymentAdvice)) {
            HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
            hashMapFilter.put("docId", new FieldUDT_Mdl("Final Payment No.", DmShared.xNull(finalPaymentEntry.txtPayNo.getText()), DmShared.xNull(finalPaymentEntry.topControl.getDocId())));
            hashMapFilter.put("bankLedgerBal", new FieldUDT_Mdl("Bank/Cash Ledger Bal", DmShared.xNull(finalPaymentEntry.txtBankCashBalance.getText()), DmShared.xNull(finalPaymentEntry.txtBankCashBalance.getText())));
//            hashMapFilter.put("partyAcBal", new FieldUDT_Mdl("Party A/C Bal", DmShared.xNull(finalPaymentEntry.txtPartyAcBalance.getText()), DmShared.xNull(finalPaymentEntry.txtPartyAcBalance.getText())));
            new OpenSourceReport(finalPaymentEntry.txtVoucherType.getTag(), "Payment_Advice", "Payment Advice", finalPaymentEntry.widgetName, hashMapFilter, printToPrinter, new OpenSourceReport.CallBack() {
                @Override
                public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty) {
                    DmClient.localSettingPrintMap(reportProperty, moduleName, finalPaymentEntry.widgetName, btnPaymentAdvice.getText());
                    return reportProperty;
                }
            }, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));

        }

    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide(false);
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

    private void printCheque(final boolean printToPrinter) {
        ChequePrint_Mdl mrTemp = fillMdl();
        if (mrTemp != null) {
            finalPaymentEntry.SrvAsync.invokeRPC().printRecords(mrTemp, new AsyncCallback<DmResult<String>>() {
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
                        }, finalPaymentEntry.swi);
                        hide();
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), (Button) null).show();
                    }
                }
            });
        }
    }

    private ChequePrint_Mdl fillMdl() {
        ChequePrint_Mdl mrTemp = new ChequePrint_Mdl();
        mrTemp.setSharedKeyFieldCode(DmShared.iNull(finalPaymentEntry.topControl.getDocId()).toString());
        mrTemp.setVoucherTypeTag(DmShared.iNull(finalPaymentEntry.txtVoucherType.getTag()).toString());
        mrTemp.setHeaderAccountCode(DmShared.iNull(finalPaymentEntry.paymnetPopupMdl.getLstPaymentMdl().get(0).getAcNameCode()).toString());
        mrTemp.setAccountCode(DmShared.iNull(finalPaymentEntry.txtHSupplier.getTag()).toString());

        return mrTemp;

    }
}
