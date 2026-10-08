package aarogya.selfToolkit.cardPaymentPopup.client;

import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import aarogya.selfToolkit.cardPaymentPopup.shared.*;
import aarogya.selfToolkit.stStatic.shared.RegisteredWallet_Mdl;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import dataman.dmBase.self.shared.DmEnum.CardType;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;
import java.util.List;

public class CardPaymentPopupGridEdit<T1, T2> extends CardPaymentPopupGridEdit_Dsg<T1, T2> implements KeyDownHandler, BlurHandler {

    public interface CallBack {

        public void onOk(GrdCardPayment_Mdl grdEntry);

        public void onDelete();
    }

    private final ServiceInvoker<CardPaymentPopup_SrvAsync> SrvAsync;
    private CallBack callBack;
    private int hashCode;
    private CardPaymentPopup parentClass;
    private String receiptType, entryMode;
    private String[] excludeCardType;
    protected String appKey, userName, deviceId, requestUrl, responseUrl, cancelUrl, currentWidgetName;
    private SetWidgetInvoker swi;

    Timer paymentProcessTimer;
    PaymentProcess_Mdl ppmG;
    GrdCardPayment_Mdl gcpm;

    private void initiatePaymentProcess(final PaymentProcess_Mdl ppm) {
        shutDownTimer();
        if (paymentProcessTimer == null) {
            paymentProcessTimer = new Timer() {
                int count = 300; //5*60 seconds

                @Override
                public void run() {
                    int tempCount = count;
                    int minutes = tempCount / 60;
                    int seconds = tempCount % 60;

                    spanCountDown.setHTML("<i class='fa fa-clock-o' style='margin-right:5px;text-weight:bold;'></i>" + DmClient.vNull(minutes, "00") + ":" + DmClient.vNull(seconds, "00"));

                    // Call Check Status function
                    CheckStatus(ppm);
                    if (count == 0) {
                        //Timeout Activity

                        shutDownTimer();

                        //Timeout Other Activity
                    }
                    count--;
                }
            };

            // Schedule the timer to run once in 1 second.
            paymentProcessTimer.scheduleRepeating(2000);

        }

    }

    private void CheckStatus(PaymentProcess_Mdl ppm) {
        SrvAsync.invokeRPC(false).getCheckStatus(ppm, new AsyncCallback<DmResult<PaymentProcess_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<PaymentProcess_Mdl> result) {
                if (result.isStatus()) {
                    ppmG = result.getData();
//                    isPaymentSuccess = result.getData().isPaymentSuccess();
                    lblProcessing.setText(DmShared.xNull(result.getData().getAbstractPaymentStatus()));
                    if (DmShared.xNull(lblProcessing.getText()).equalsIgnoreCase("SUCCESS")) {
                        ppmG.setPaymentSuccess(true);
                        shutDownTimer();
                        txtHCardPayment.setEnabled(false);
                        txtAmount.setEnabled(false);
                    } else if (DmShared.xNull(lblProcessing.getText()).equalsIgnoreCase("FAILED")) {
 
                        ppmG.setPaymentSuccess(false);
                        shutDownTimer();
                    }
                    if (ppmG.isCancelledCalled()) {
                        ppmG.setCancelledCalled(false);
                        new DmAlertMessageBox(ppmG.getRespMdl().getMessage(), (DmTextBox) null).show();

                    }
                }
            }
        });
    }

//    private void getPOSParams() {
//        SrvAsync.invokeRPC(false).getPOSParams(txtHCardPayment.getTag(), new AsyncCallback<DmResult<RazorPayParams_Mdl>>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
//            }
//
//            @Override
//            public void onSuccess(DmResult<RazorPayParams_Mdl> result) {
//                if (result.isStatus()) {
//                    appKey = DmShared.xNull(result.getData().getAppKey());
//                    userName = DmShared.xNull(result.getData().getUserName());
//                    deviceId = DmShared.xNull(result.getData().getDeviceId());
//                    requestUrl = DmShared.xNull(result.getData().getRequestURL());
//                    responseUrl = DmShared.xNull(result.getData().getResponseURL());
//                    cancelUrl = DmShared.xNull(result.getData().getCancelURL());
//                }
//            }
//        });
//    }
    private void shutDownTimer() {

        spanCountDown.setHTML("");
        try {
            if (paymentProcessTimer != null) {
                if (paymentProcessTimer.isRunning()) {
                    paymentProcessTimer.cancel();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                paymentProcessTimer = null;
            } catch (Exception e) {
            }
        }
    }

    private void textHelpEvents() {
        txtHCardPayment.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<GrdCardPayment_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHCardType(excludeCardType, helper, blnToEndOfFile, receiptType, new AsyncCallback<DmResultPageing<List<GrdCardPayment_Mdl>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCardPayment).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GrdCardPayment_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHCardPayment.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCardPayment).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, GrdCardPayment_Mdl tRow) {
                clear(true);
//                txtAmount.setEnabled(true);
                if (blnStatus && tRow != null) {
                    clear(tRow.isAccountEditable());
                    txtCardType.setTag(tRow.getCardTypeNatureCode());
                    txtCardType.setText(tRow.getCardTypeNatureName());
                    txtCardTypeShortName.setText(tRow.getCardTypeShortName());
                    txtAcName.setText(tRow.getAcNameName());
                    txtAcName.setTag(tRow.getAcNameCode());
                    txtAcName.setTag2nd(tRow.getAcNature());
                    txtHAccount.setText(tRow.getAcNameName());
                    txtHAccount.setTag(tRow.getAcNameCode());
                    txtHAccount.setTag2nd(tRow.getAcNature());

                    if (DmShared.vNull(txtAmount.getText()).doubleValue() == 0) {
                        txtAmount.setText(DmClient.vNull(txtAmount.getTag(), "0.00"));
                    }

                    CardType cardType = CardType.getValueOf(DmShared.iNull(tRow.getCardTypeNatureCode()).intValue());
                    mainFlwPanel.clear();
                    if (null == cardType) {
                        mainFlwPanel.add(flwELSE);
                    } else {
                        switch (cardType) {
                            case CREDIT_CARD:
                                mainFlwPanel.add(flwCC);
                                break;
                            case POS:
                                mainFlwPanel.add(flwPayment);
                                txtAmount.setEnabled(false);
                                break;
                            case BANK:
                                mainFlwPanel.add(flwBank);
                                txtDate.setText(DmClient.getLoginDate());
                                txtAmount.setEnabled(false);
                                txtFavourOf.setText(DmShared.xNull(txtHCardPayment.getText()));
                                if (DmShared.xNull(entryMode).equalsIgnoreCase("New")) {
                                    txtFavourOf.setEnabled(true);
                                    txtInstrumentNoBK.setEnabled(true);
                                    txtRemarkBK.setEnabled(true);
                                    txtDate.setEnabled(true);
                                } else {
                                    txtFavourOf.setEnabled(false);
                                    txtInstrumentNoBK.setEnabled(false);
                                    txtRemarkBK.setEnabled(false);
                                    txtDate.setEnabled(false);
                                }
                                break;
                            case DIGITAL:
                                mainFlwPanel.add(flwDigital);
                                break;
                            case WALLET:
                                mainFlwPanel.add(flwWallet);
                                if (parentClass != null) {

                                    txtWalletNo.setText(parentClass.cardPayMdl.getUHIDNo());
                                    getWalletInfo("", parentClass.cardPayMdl.getUHIDDocid());
                                }
                                break;
                            case FOREIGN_CURRENCY:
                                mainFlwPanel.add(flwCurrency);
                                lblForeignCurrency.setText(tRow.getCurrencyName());
                                txtForeignCurrencyAmount.setTag(DmShared.xNull(tRow.getCurrencyCode()));
                                txtForeignCurrencyAmount.setTag2nd(DmShared.xNull(tRow.getCurrencyName()));
                                txtConversionRate.setText(DmClient.vNull(tRow.getConversionRate(), "0.0000"));
                                txtForeignCurrencyAmount.setText(DmClient.vNull(DmShared.vNull(txtAmount.getTag()).doubleValue() * DmShared.vNull(txtConversionRate.getText()).doubleValue(), "0.00"));
                                manageForeignCurrencyAmount();
                                txtForeignCurrencyAmount.setFocus(true);
                                txtConversionRate.setEnabled(false);
                                txtAmount.setEnabled(false);
                                break;
                            default:
                                mainFlwPanel.add(flwELSE);
                                break;
                        }
                    }
                    txtAmount.setFocus(true);
                }
            }
        });
        txtHAccount.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHAccounts(receiptType, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAccount).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHAccount.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHAccount).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });
        txtHPaymentMode.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHPaymentMode(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPaymentMode).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPaymentMode.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPaymentMode).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, String[] excludeCardType,
            final String balanceAmount, String receiptType, CardPaymentPopup parentClass, String entryMode) {
        this.parentClass = parentClass;
        this.entryMode = entryMode;
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
        this.excludeCardType = excludeCardType;
        this.receiptType = receiptType;
        manageDisplay();
        if (DmShared.vNull(txtAmount.getText()).doubleValue() == 0) {
            txtAmount.setText(DmClient.vNull(balanceAmount, "0.00"));

            txtAmount.setTag(DmClient.vNull(balanceAmount, "0.00"));
        } else {
            txtAmount.setTag("0.00");
        }

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                if (txtHCardPayment.isEnabled()) {
                    txtHCardPayment.setFocus(true);
                } else {
                    txtAmount.setFocus(true);
                }
                if (DmShared.xNull(txtCardType.getText()).equalsIgnoreCase(CardType.FOREIGN_CURRENCY.getEnumName())) {
                    txtAmount.setEnabled(false);
                    if (DmShared.vNull(txtForeignCurrencyAmount.getText()).doubleValue() == 0) {
                        txtForeignCurrencyAmount.setText(DmClient.vNull(DmShared.vNull(txtAmount.getTag()).doubleValue() * DmShared.vNull(txtConversionRate.getText()).doubleValue(), "0.00"));
                        manageForeignCurrencyAmount();
                    }
                    txtForeignCurrencyAmount.setFocus(true);
                } else {
//                    txtAmount.setEnabled(true);
                }
            }
        });

        if (DmShared.xNull(entryMode).equalsIgnoreCase("Browse")) {
            txtHCardPayment.setEnabled(false);
            txtFavourOf.setEnabled(false);
        } else {
            if (DmShared.xNull(entryMode).equalsIgnoreCase("Edit")) {
                if (DmShared.xNull(txtCardType.getText()).equalsIgnoreCase(DmShared.xNull(CardType.POS.getEnumName()))
                        || DmShared.xNull(txtCardType.getText()).equalsIgnoreCase(DmShared.xNull(CardType.BANK.getEnumName()))) {
                    txtHCardPayment.setEnabled(false);
                    txtAmount.setEnabled(false);
                }
            }
        }

    }

    private void manageDisplay() {
        txtBalance.setEnabled(false);
        txtCardType.setEnabled(false);
        txtCardTypeShortName.setEnabled(false);
        txtAcName.setEnabled(false);
        txtCardHolderNameWT.setEnabled(false);
        txtAuthorizationWT.setEnabled(false);
        txtConversionRate.setEnabled(false);
        txtHCardPayment.setEnabled(DmShared.iNull(txtAuthorizationWT.getTag()).longValue() == 0);
        txtWalletNo.setEnabled(DmShared.iNull(txtAuthorizationWT.getTag()).longValue() == 0);
    }

    private void handlerEvents() {
        txtWalletNo.addKeyDownHandler(this);
        txtForeignCurrencyAmount.addBlurHandler(this);
        txtDate.addBlurHandler(this);
        newClickHandeler();
    }

    private void newClickHandeler() {
        btnMakePayment.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                txtAmount.setEnabled(false);

                if (!parentClass.entryMode.equalsIgnoreCase("New")) {
                    new DmAlertMessageBox("Can not process.", (DmTextBox) null).show();
                    return;
                }

                if (paymentProcessTimer != null) {
                    if (paymentProcessTimer.isRunning()) {
                        new DmAlertMessageBox("Previous Payment is in process...", (DmTextBox) null).show();
                    }

                } else {
                    if (ppmG == null) {
                        ppmG = new PaymentProcess_Mdl();
                    }

                    if (!ppmG.isPaymentSuccess()) {
                        getPOSSettings();

                    } else if (ppmG.isPaymentSuccess()) {
                        new DmAlertMessageBox("Payment already received.", (DmTextBox) null).show();
                    }
                }

            }
        });
        btnCancelPayment.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                txtAmount.setEnabled(false);
                if (!parentClass.entryMode.equalsIgnoreCase("New")) {
                    new DmAlertMessageBox("Can not process.", (DmTextBox) null).show();
                    return;
                }

                if (!ppmG.isPaymentSuccess()) {
                    shutDownTimer();
                    cancelPayment();
                } else if (ppmG.isPaymentSuccess()) {
                    new DmAlertMessageBox("Payment already received.", (DmTextBox) null).show();
                }
            }
        });

        btnChequePrint.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {

                if (DmShared.vNull(txtAmount.getText()) == 0) {
                    new DmAlertMessageBox("Amount is mandatory.", (DmTextBox) null).show();

                    return;
                }
                if (DmShared.xNull(txtInstrumentNoBK.getText()).equalsIgnoreCase("")) {
                    new DmAlertMessageBox("Instrument No. is mandatory.", (DmTextBox) null).show();

                    return;
                }
                if (DmShared.xNull(txtFavourOf.getText()).equalsIgnoreCase("")) {
                    new DmAlertMessageBox("Favour Of is mandatory.", (DmTextBox) null).show();

                    return;
                }
                if (DmShared.xNull(txtRemarkBK.getText()).equalsIgnoreCase("")) {
                    new DmAlertMessageBox("Remark is mandatory.", (DmTextBox) null).show();

                    return;
                }
                if (DmShared.xNull(txtDate.getText()).equalsIgnoreCase("")) {
                    new DmAlertMessageBox("Date is mandatory.", (DmTextBox) null).show();

                    return;
                }

                if (DmShared.xNull(entryMode).equalsIgnoreCase("Browse")) {
                    gcpm = new GrdCardPayment_Mdl();
                    gcpm.setAcNameCode(DmShared.xNull(txtHAccount.getTag()));
                    gcpm.setAcNameName(DmShared.xNull(txtHAccount.getText()));
//                    gcpm.setCardHolderName(DmShared.xNull(txtRemarkBK.getText()));
                    gcpm.setCardHolderName(DmShared.xNull(txtFavourOf.getText()));
                    gcpm.setCardNo(DmShared.xNull(txtInstrumentNoBK.getText()));
                    gcpm.setCardDate(DmShared.dNull(txtDate.getText()).trim());
                    gcpm.setAmount(DmClient.vNull(txtAmount.getText(), "0.00"));
                    chequePrint();

                } else {
                    new DmAlertMessageBox("Cheque printing is permitted only in Browse mode.", (DmTextBox) null).show();
                }

            }
        });
        btnCheckStatus.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                CheckStatus(ppmG);
            }
        });

    }

    private void chequePrint() {

        SrvAsync.invokeRPC().printRecords(gcpm, new AsyncCallback<DmResult<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    new ReportPrint(result.getData(), false, new ReportPrint.CallBack() {
                        @Override
                        public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty) {
                            DmClient.localSettingPrintMap(reportProperty, "Accounts", currentWidgetName, "Print");
                            return reportProperty;
                        }
                    }, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));

                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (Button) null).show();
                }
            }
        });

    }

    private void getPOSSettings() {
        try {

            String url = "http://localhost:1111/getMac?cmd=read";

            RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, url);
            requestBuilder.setCallback(new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    if (response.getStatusCode() == 200) {
                        getPosSettings_Mdl(DmShared.xNull(response.getText()));

                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {

                }
            });

            requestBuilder.setHeader("Content-type", "application/x-www-form-urlencoded");
            requestBuilder.send();
        } catch (Exception ex) {

        }
    }

    private void getPosSettings_Mdl(String posSettingsJSON) {

        SrvAsync.invokeRPC().posSettingsJSON(posSettingsJSON, new AsyncCallback<RazorPayParams_Mdl>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(RazorPayParams_Mdl posSetting) {

                CardPaymentPopup_Vld.chkPosSeeting(posSetting);

                appKey = DmShared.xNull(posSetting.getAppKey());
                userName = DmShared.xNull(posSetting.getUserName());
                deviceId = DmShared.xNull(posSetting.getDeviceId());
                requestUrl = DmShared.xNull(posSetting.getRequestURL());
                responseUrl = DmShared.xNull(posSetting.getResponseURL());
                cancelUrl = DmShared.xNull(posSetting.getCancelURL());

                makePayment();
            }
        });
    }

    private void makePayment() {

        lblProcessing.setText("INITIATED");
        initiatePayment();
        SrvAsync.invokeRPC(true).getMakePayment(ppmG, new AsyncCallback<DmResult<PaymentProcess_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<PaymentProcess_Mdl> result) {
                if (result.isStatus()) {
                    ppmG = result.getData();
                    initiatePaymentProcess(ppmG);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });

    }

    private PaymentProcess_Mdl initiatePayment() {
        ppmG = new PaymentProcess_Mdl();
        ppmG.setVoucherPrefix(DmClient.getVoucherPrefix());
        ppmG.setAmount(DmShared.xNull(txtAmount.getText()));
        ppmG.setMobile(DmShared.xNull(txtMobileNo.getText()));
        ppmG.setPaymentMode(DmShared.xNull(txtHPaymentMode.getText()));
        ppmG.setAppKey(appKey);
        ppmG.setUserName(userName);
        ppmG.setDeviceId(deviceId);
        ppmG.setRequestURL(requestUrl);
        ppmG.setResponseURL(responseUrl);
        ppmG.setCancelURL(cancelUrl);
        ppmG.setBlnAdd(true);
        ppmG.setCardTypeMastCode(DmShared.iNull(txtHCardPayment.getTag()).toString());
        return ppmG;
    }

    private void cancelPayment() {
        ppmG.setCancelledCalled(true);
        SrvAsync.invokeRPC(true).getCancelPayment(ppmG, new AsyncCallback<DmResult<PaymentProcess_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<PaymentProcess_Mdl> result) {
                if (result.isStatus()) {
                    ppmG = result.getData();
                    ppmG.setPaymentSuccess(result.getData().isPaymentSuccess());
                    lblProcessing.setText(DmShared.xNull(result.getData().getAbstractPaymentStatus()));
                    CheckStatus(ppmG);

                }
            }
        });
    }

    @Override
    public void displayRow(T1 row) {
        GrdCardPayment_Mdl ourRow = (GrdCardPayment_Mdl) row;

        clear(ourRow.isAccountEditable());

        lblProcessing.setText("");

        ppmG = new PaymentProcess_Mdl();
        if (!entryMode.equalsIgnoreCase("New") || ourRow.isPaymentSuccess()) {
            ppmG.setPaymentSuccess(ourRow.isPaymentSuccess());
            ppmG.setPosPaymentDocid(ourRow.getPosPaymentDocid());

        }

        txtHCardPayment.setTag(DmShared.xNull(ourRow.getCardTypeCode()));
        txtHCardPayment.setText(DmShared.xNull(ourRow.getCardTypeName()));
        txtCardType.setTag(DmShared.xNull(ourRow.getCardTypeNatureCode()));
        txtCardType.setText(DmShared.xNull(ourRow.getCardTypeNatureName()));
        txtCardTypeShortName.setText(DmShared.xNull(ourRow.getCardTypeShortName()));
        txtAmount.setText(DmClient.vNull(ourRow.getAmount(), "0.00"));
        txtAcName.setTag(DmShared.xNull(ourRow.getAcNameCode()));
        txtAcName.setText(DmShared.xNull(ourRow.getAcNameName()));
        txtAcName.setTag2nd(DmShared.xNull(ourRow.getAcNature()));
        txtHAccount.setTag(DmShared.xNull(ourRow.getAcNameCode()));
        txtHAccount.setText(DmShared.xNull(ourRow.getAcNameName()));
        txtHAccount.setTag2nd(DmShared.xNull(ourRow.getAcNature()));
        CardType cardType = CardType.getValueOf(DmShared.iNull(ourRow.getCardTypeNatureCode()).intValue());
        mainFlwPanel.clear();
        if (null == cardType) {
            mainFlwPanel.add(flwELSE);
            txtRemark.setText(DmShared.xNull(ourRow.getCardHolderName()));
        } else {
            switch (cardType) {
                case CREDIT_CARD:
                    mainFlwPanel.add(flwCC);
                    txtCardHolderNameCC.setText(DmShared.xNull(ourRow.getCardHolderName()));
                    txtCardNoCC.setText(DmShared.xNull(ourRow.getCardNo()));
                    txtAuthorizationCC.setText(DmShared.xNull(ourRow.getCardAuthorization()));
                    break;
                case POS:
                    mainFlwPanel.add(flwPayment);
                    txtAmount.setEnabled(false);
                    break;
                case BANK:
                    mainFlwPanel.add(flwBank);
                    txtRemarkBK.setText(DmShared.xNull(ourRow.getCardHolderName()));
                    txtFavourOf.setText(DmShared.xNull(ourRow.getCardFavourOf()));
                    txtInstrumentNoBK.setText(DmShared.xNull(ourRow.getCardNo()));
                    txtDate.setText(DmShared.xNull(ourRow.getCardDate()));
                    break;
                case DIGITAL:
                    mainFlwPanel.add(flwDigital);
                    txtTransactionNoDG.setText(DmShared.xNull(ourRow.getCardNo()));
                    break;
                case WALLET:
                    mainFlwPanel.add(flwWallet);
                    RegisteredWallet_Mdl walletMdl = new RegisteredWallet_Mdl();
                    walletMdl.setCardNo(DmShared.xNull(ourRow.getCardNo()));
//                    walletMdl.setPhysicalId(DmShared.xNull(ourRow.getCardPhysicalId()));
                    walletMdl.setLinkAccountCode(DmShared.xNull(ourRow.getAcNameCode()));
                    walletMdl.setLinkAccountName(DmShared.xNull(ourRow.getAcNameName()));
                    walletMdl.setContactPerson(DmShared.xNull(ourRow.getCardHolderName()));
                    walletMdl.setSharedKeyFieldCode(DmShared.xNull(ourRow.getCardAuthorizationDocId()));
                    walletMdl.setSharedKeyFieldName(DmShared.xNull(ourRow.getCardAuthorization()));
                    walletMdl.setBalance(DmShared.vNull(ourRow.getBalance()).doubleValue());
                    setWalletInfo(walletMdl);
                    break;
                case FOREIGN_CURRENCY:
                    mainFlwPanel.add(flwCurrency);
                    lblForeignCurrency.setText(DmShared.xNull(ourRow.getCurrencyName()));
                    txtForeignCurrencyAmount.setTag(DmShared.xNull(ourRow.getCurrencyCode()));
                    txtForeignCurrencyAmount.setTag2nd(DmShared.xNull(ourRow.getCurrencyName()));
                    txtForeignCurrencyAmount.setText(DmClient.vNull(ourRow.getForeignCurrencyAmount(), "0.0000"));
                    txtConversionRate.setText(DmClient.vNull(ourRow.getConversionRate(), "0.0000000"));
                    txtCurrencyRemark.setText(DmShared.xNull(ourRow.getCardHolderName()));
                    break;
                default:
                    mainFlwPanel.add(flwELSE);
                    txtRemark.setText(DmShared.xNull(ourRow.getCardHolderName()));
                    break;
            }
        }

        hashCode = ourRow.getSharedKeyFieldHashCode();

        txtHCardPayment.setFocus(true);
        if (cardType == CardType.POS) {

//            getPOSParams();
//            if (DmShared.vNull(ourRow.getAmount()) != 0) {
//                isPaymentSuccess = true;
//            }
            txtHPaymentMode.setText(DmShared.xNull(ourRow.getPosPaymentMode()));
            txtMobileNo.setText(DmShared.xNull(ourRow.getPosPayByMobile()));

        }

        if (cardType == CardType.POS && ourRow.isPaymentSuccess()) {
            txtAmount.setEnabled(false);
        } else {
            txtAmount.setEnabled(true);
        }
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdCardPayment_Mdl();
    }

    @Override
    public void onOk() {
        CardType cardType = CardType.getValueOf(DmShared.iNull(txtCardType.getTag()).intValue());

        if (!DmClient.isValidText(txtHCardPayment.getTag(), "Payment Mode", txtHCardPayment)) {
            return;
        }
        if (cardType == CardType.FOREIGN_CURRENCY) {
            if (DmShared.vNull(txtForeignCurrencyAmount.getText()).doubleValue() == 0) {
                new DmAlertMessageBox("Currency is mandatory for " + DmShared.xNull(lblForeignCurrency.getText()) + ".", txtForeignCurrencyAmount).show();
                return;
            }

        }
        if (DmShared.vNull(txtAmount.getText()).doubleValue() == 0) {
            new DmAlertMessageBox("Amount is mandatory.", txtAmount).show();
            return;
        }

//        if (cardType == CardType.CreditCard) {
//            if (DmShared.xNull(txtCardNoCC.getText()).trim().equalsIgnoreCase("")) {
//                new DmAlertMessageBox("Card No. is mandatory.", txtCardNoCC).show();
//                return;
//            }
//            if (DmShared.xNull(txtCardHolderNameCC.getText()).trim().equalsIgnoreCase("")) {
//                new DmAlertMessageBox("Holder Name is mandatory.", txtCardHolderNameCC).show();
//                return;
//            }
//        }
        if (cardType == CardType.WALLET) {
            if (DmShared.xNull(txtWalletNo.getText()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Invalid wallet no.", txtWalletNo).show();
                return;
            } else if (DmShared.iNull(txtAuthorizationWT.getTag()).longValue() == 0) {
                new DmAlertMessageBox("Invalid authorization.", txtWalletNo).show();
                return;
            }
        }
        if (cardType == CardType.POS && !ppmG.isPaymentSuccess()) {
            new DmAlertMessageBox("Payment not received yet!", txtHCardPayment).show();
            return;
        }

        List<GrdCardPayment_Mdl> lstTemp = (List<GrdCardPayment_Mdl>) getDmGrid().getGrid().getStore().getAll();

        for (int i = 0; i < lstTemp.size(); i++) {
            GrdCardPayment_Mdl mdl = lstTemp.get(i);
            if (DmShared.iNull(txtHCardPayment.getTag()).longValue() == DmShared.iNull(mdl.getCardTypeCode()).longValue()) {
                if (hashCode != mdl.getSharedKeyFieldHashCode()) {
                    new DmAlertMessageBox(txtHCardPayment.getText() + " already exists.", txtHCardPayment).show();
                    return;
                }
            }
        }

        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdCardPayment_Mdl ourRow = (GrdCardPayment_Mdl) row;

        CardType cardType = CardType.getValueOf(DmShared.iNull(txtCardType.getTag()).intValue());

        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHCardPayment.getTag()).trim());
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHCardPayment.getText()).trim());
        ourRow.setCardTypeCode(DmShared.xNull(txtHCardPayment.getTag()).trim());
        ourRow.setCardTypeName(DmShared.xNull(txtHCardPayment.getText()).trim());
        ourRow.setCardTypeShortName(DmShared.xNull(txtCardTypeShortName.getText()).trim());
        ourRow.setCardTypeNatureCode(DmShared.xNull(txtCardType.getTag()).trim());
        ourRow.setCardTypeNatureName(DmShared.xNull(txtCardType.getText()).trim());

        ourRow.setAmount(DmClient.vNull(txtAmount.getText(), "0.00"));
        ourRow.setAcNameCode(txtAcName.isAttached() ? DmShared.xNull(txtAcName.getTag()).trim() : DmShared.xNull(txtHAccount.getTag()).trim());
        ourRow.setAcNameName(txtAcName.isAttached() ? DmShared.xNull(txtAcName.getText()).trim() : DmShared.xNull(txtHAccount.getText()).trim());
        ourRow.setAcNature(txtAcName.isAttached() ? DmShared.xNull(txtAcName.getTag2nd()).trim() : DmShared.xNull(txtHAccount.getTag2nd()).trim());

        ourRow.setCardAuthorization("");
        ourRow.setCardAuthorizationDocId("");
        ourRow.setCardHolderName("");
        ourRow.setCardNo("");
        ourRow.setCardPhysicalId("");
        ourRow.setCurrencyCode("");
        ourRow.setCurrencyName("");
        ourRow.setForeignCurrencyAmount("");
        ourRow.setConversionRate("");
        if (null == cardType) {
            ourRow.setCardHolderName(DmShared.xNull(txtRemark.getText()));
        } else {
            switch (cardType) {
                case WALLET:
                    ourRow.setCardNo(DmShared.xNull(txtWalletNo.getText()));
                    ourRow.setCardPhysicalId(DmShared.xNull(txtWalletNo.getTag()));
                    ourRow.setCardHolderName(DmShared.xNull(txtCardHolderNameWT.getText()));
                    ourRow.setCardAuthorization(DmShared.xNull(txtAuthorizationWT.getText()));
                    ourRow.setCardAuthorizationDocId(DmShared.xNull(txtAuthorizationWT.getTag()));
                    break;
                case DIGITAL:
                    ourRow.setCardNo(DmShared.xNull(txtTransactionNoDG.getText()));
                    break;
                case CREDIT_CARD:
                    ourRow.setCardHolderName(DmShared.xNull(txtCardHolderNameCC.getText()));
                    ourRow.setCardNo(DmShared.xNull(txtCardNoCC.getText()));
                    ourRow.setCardAuthorization(DmShared.xNull(txtAuthorizationCC.getText()));
                    break;
                case BANK:
                    ourRow.setCardHolderName(DmShared.xNull(txtRemarkBK.getText()));
                    ourRow.setCardFavourOf(DmShared.xNull(txtFavourOf.getText()));
                    ourRow.setCardNo(DmShared.xNull(txtInstrumentNoBK.getText()));
                    ourRow.setCardDate(DmShared.dNull(txtDate.getText()).trim());
                    break;
                case FOREIGN_CURRENCY:
                    ourRow.setCurrencyCode(DmShared.xNull(txtForeignCurrencyAmount.getTag()));
                    ourRow.setCurrencyName(DmShared.xNull(txtForeignCurrencyAmount.getTag2nd()));
                    ourRow.setForeignCurrencyAmount(DmClient.vNull(txtForeignCurrencyAmount.getText(), "0.0000"));
                    ourRow.setConversionRate(DmClient.vNull(txtConversionRate.getText(), "0.0000000"));
                    ourRow.setCardHolderName(DmShared.xNull(txtCurrencyRemark.getText()));
                    break;
                default:
                    ourRow.setCardHolderName(DmShared.xNull(txtRemark.getText()));
                    break;
            }
        }
        ourRow.setPaymentSuccess(ppmG.isPaymentSuccess());
        ourRow.setPosPaymentDocid(ppmG.getPosPaymentDocid());
        ourRow.setPosPaymentMode(txtHPaymentMode.getText());
        ourRow.setPosPayByMobile(txtMobileNo.getText());

        if (ourRow.isPaymentSuccess()) {
            ppmG = new PaymentProcess_Mdl();
        }

        callBack.onOk(ourRow);
    }

    @Override
    public void onNew() {
        proceedNew();
    }

    @Override
    public void onDelete() {
        if (entryMode.equalsIgnoreCase("New") && ppmG.isPaymentSuccess()) {
            new DmAlertMessageBox("POS payment has been received , can't be close.", (DmTextBox) null).show();
            return;
        }
        proceedDelete();
    }

    @Override
    public void onClose() {
        if (entryMode.equalsIgnoreCase("New") && ppmG.isPaymentSuccess()) {
            new DmAlertMessageBox("POS payment has been received , can't be close.", (DmTextBox) null).show();
            return;
        }
        hide();
    }

    @Override
    public void manageDelete() {
        callBack.onDelete();
        clear(false);
        txtHCardPayment.setEnabled(DmShared.iNull(txtAuthorizationWT.getTag()).longValue() == 0);
        txtWalletNo.setEnabled(DmShared.iNull(txtAuthorizationWT.getTag()).longValue() == 0);
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getSource().equals(txtWalletNo)) {
            if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
                if (!DmShared.xNull(txtWalletNo.getText()).equalsIgnoreCase("")) {
                    getWalletInfo(txtWalletNo.getText(), parentClass == null ? "" : parentClass.cardPayMdl.getUHIDDocid());
                }
            }
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtForeignCurrencyAmount)) {
            manageForeignCurrencyAmount();
        } else if (event.getSource().equals(txtDate)) {
            txtDate.setText(DmClient.returnDate(txtDate.getText()));
        }
    }

    private void manageForeignCurrencyAmount() {
        if (DmShared.vNull(txtConversionRate.getText()).doubleValue() != 0) {
            txtAmount.setText(DmClient.vNull(DmShared.vNull(txtForeignCurrencyAmount.getText()).doubleValue() / DmShared.vNull(txtConversionRate.getText()).doubleValue(), "0.00"));
        } else {
            txtAmount.setText("0");
        }
    }

    public CardPaymentPopupGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Payment", "500px", "175px");
        SrvAsync = new ServiceInvoker<>(GWT.create(CardPaymentPopup_Srv.class), CardPaymentPopup.class.getName());
        txtHPaymentMode.setTag("CARD");
        txtHPaymentMode.setText("CARD");
        textHelpEvents();
        handlerEvents();
    }

    private void clear(boolean isAccountEditable) {
        appKey = "";
        userName = "";
        deviceId = "";
        requestUrl = "";
        responseUrl = "";
        cancelUrl = "";
        txtAcName.setTag2nd("");
        txtAcName.setTag("");
        txtAcName.setText("");
        txtHAccount.setTag2nd("");
        txtHAccount.setTag("");
        txtHAccount.setText("");
        txtAuthorizationCC.setText("");
        txtBalance.setText("");
        txtCardHolderNameCC.setText("");
        txtCardNoCC.setText("");
        txtCardType.setTag("");
        txtCardType.setText("");
        txtCardTypeShortName.setText("");
        txtRemark.setText("");
        txtRemarkBK.setText("");
        txtFavourOf.setText("");
        txtInstrumentNoBK.setText("");
        txtTransactionNoDG.setText("");
        txtDate.setText("");

        clearCurrencyInfo();
        clearWalletInfo();
        clearAccountWidget(isAccountEditable);
    }

    private void clearAccountWidget(boolean isAccountEditable) {
        flwAccount.clear();
        if (isAccountEditable) {
            flwAccount.add(flwAccountTextHelp);
        } else {
            flwAccount.add(flwAccountTextBox);
        }
    }

    private void getWalletInfo(String cardNo, String uhid) {
        SrvAsync.invokeRPC().getWalletInfo(cardNo, txtHCardPayment.getTag(), uhid, new AsyncCallback<DmResult<RegisteredWallet_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                clearWalletInfo();
                lblMessage.getElement().getStyle().setColor("red");
                lblMessage.setText(caught.getMessage());
                txtWalletNo.setFocus(true);
            }

            @Override
            public void onSuccess(DmResult<RegisteredWallet_Mdl> result) {
                if (result.isStatus()) {
                    setWalletInfo(result.getData());
                    txtWalletNo.setEnabled(false);
                    txtHCardPayment.setEnabled(false);
                } else {
                    clearWalletInfo();
                    txtWalletNo.setFocus(true);
                    lblMessage.getElement().getStyle().setColor("red");
                    lblMessage.setText(result.getErrorMessage());
                }
            }
        });
    }

    private void clearCurrencyInfo() {
        txtForeignCurrencyAmount.setTag("");
        txtForeignCurrencyAmount.setTag2nd("");
        txtForeignCurrencyAmount.setText("");
        txtConversionRate.setText("");
        lblForeignCurrency.setText("");
        txtCurrencyRemark.setText("");
    }

    private void clearWalletInfo() {
        txtWalletNo.setText("");
        txtWalletNo.setTag("");
        txtCardHolderNameWT.setTag("");
        txtCardHolderNameWT.setText("");
        txtAuthorizationWT.setText("");
        txtAuthorizationWT.setTag("");
        txtAcName.setTag("");
        txtAcName.setText("");
        lblMessage.setText("");
    }

    private void setWalletInfo(RegisteredWallet_Mdl data) {
        clearWalletInfo();
        String balance = "";
        if (data != null) {
            if (parentClass != null) {
                if (DmShared.iNull(parentClass.cardPayMdl.getUHIDDocid()).longValue() != DmShared.iNull(data.getDocIdUHID()).longValue()) {
                    new DmAlertMessageBox("UHID is invalid for wallet", txtWalletNo).show();
                    return;
                }
            }
//            txtWalletNo.setTag(DmShared.xNull(data.getPhysicalId()));
            txtWalletNo.setText(DmShared.xNull(data.getCardNo()));
            txtAuthorizationWT.setTag(DmShared.xNull(data.getSharedKeyFieldCode()));
            txtAuthorizationWT.setText(DmShared.xNull(data.getSharedKeyFieldName()));
            txtCardHolderNameWT.setText(DmShared.xNull(data.getContactPerson()));
            txtAcName.setTag(DmShared.xNull(data.getLinkAccountCode()));
            txtAcName.setText(DmShared.xNull(data.getLinkAccountName()));
//            txtCardHolderNameWT.setTag2nd(DmShared.xNull(data.getValidDate()));
            balance = DmShared.vNull(data.getBalance()).doubleValue() > 0 ? (DmClient.vNull(Math.abs(data.getBalance()), "0.00") + " Dr") : (DmShared.vNull(data.getBalance()).doubleValue() < 0 ? (DmClient.vNull(Math.abs(data.getBalance()), "0.00") + " Cr") : "");
            lblMessage.getElement().getStyle().setColor("green");
            lblMessage.setText(DmShared.vNull(data.getBalance()).doubleValue() != 0 ? ("Balance: " + balance) : "");
        }
    }
}
