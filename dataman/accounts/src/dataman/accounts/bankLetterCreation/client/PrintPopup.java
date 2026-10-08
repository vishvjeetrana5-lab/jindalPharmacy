package dataman.accounts.bankLetterCreation.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.accounts.bankLetterCreation.shared.ChequePrint_Mdl;
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
    private BankLetterCreation bankLetterCreation;
    private String moduleName = new lmAccounts().moduleName;

    private void eventHandler() {
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        btnCheque.addClickHandler(this);
        btnBankLetter.addClickHandler(this);

    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(BankLetterCreation bankLetterCreation) {
        this.bankLetterCreation = bankLetterCreation;
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        btnOk.setFocus(true);
        boolean enable = DmShared.xNull(bankLetterCreation.userControl.getTextBlog(bankLetterCreation.approvedByBlog)).equalsIgnoreCase("") ? false : true;
        btnCheque.setEnabled(enable);
        cbCheckPrint.setEnabled(enable);

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
        } else if (event.getSource() == btnBankLetter) {
            print(false, btnBankLetter);
            hide(false);
        } else if (event.getSource() == btnOk) {
            if (cbCheckPrint.getValue()) {
                print(true, btnCheque);
            }
            if (cbBankLetterPrint.getValue()) {
                print(true, btnBankLetter);
            }

            hide(true);
        }
    }

    private void print(boolean printToPrinter, Button btn) {
        if (btn.equals(btnCheque)) {

            printCheque(printToPrinter);

        } else if (btn.equals(btnBankLetter)) {
            HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
            hashMapFilter.put("docId", new FieldUDT_Mdl("Bank No.", DmShared.xNull(bankLetterCreation.txtBankLetterNo.getText()), DmShared.xNull(bankLetterCreation.topControl.getDocId())));
            new OpenSourceReport(DmShared.xNull(bankLetterCreation.defaultValue.getBankLetterCreationVTypeCode()), "", "Bank Letter Creation", bankLetterCreation.widgetName, hashMapFilter, printToPrinter, new OpenSourceReport.CallBack() {
                @Override
                public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty) {
                    DmClient.localSettingPrintMap(reportProperty, moduleName, bankLetterCreation.widgetName, "Print");
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
            bankLetterCreation.SrvAsync.invokeRPC().printRecords(mrTemp, new AsyncCallback<DmResult<String>>() {
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
                        }, bankLetterCreation.swi);

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
        mrTemp.setSharedKeyFieldCode(DmShared.iNull(bankLetterCreation.topControl.getDocId()).toString());
//        mrTemp.setVoucherTypeTag(DmShared.iNull(bankLetterCreation.txtVoucherType.getTag()).toString());
//        mrTemp.setHeaderAccountCode(DmShared.iNull(bankLetterCreation.paymnetPopupMdl.getLstPaymentMdl().get(0).getAcNameCode()).toString());
//        mrTemp.setAccountCode(DmShared.iNull(bankLetterCreation.txtHSupplier.getTag()).toString());

        return mrTemp;

    }
}
