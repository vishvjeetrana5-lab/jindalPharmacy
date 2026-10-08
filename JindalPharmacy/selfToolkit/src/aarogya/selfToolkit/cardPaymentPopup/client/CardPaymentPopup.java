package aarogya.selfToolkit.cardPaymentPopup.client;

import aarogya.selfToolkit.cardPaymentPopup.shared.GrdCardPayment_Mdl;
import aarogya.selfToolkit.cardPaymentPopup.shared.CardPaymentPopup_Vld;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.Widget;
import aarogya.selfToolkit.cardPaymentPopup.shared.CardPaymentPopup_Mdl;
import aarogya.selfToolkit.cardPaymentPopup.shared.TxtHReceiptType_VList_Mdl;
import aarogya.selfToolkit.stStatic.shared.STEnum.PaymentSource;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmEnum.CardType;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmBase.self.shared.DmHelper;
import java.util.ArrayList;
import java.util.List;

public class CardPaymentPopup extends CardPaymentPopup_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, KeyUpHandler, ClickHandler {

    public interface CallBack {

        public void onOk(CardPaymentPopup_Mdl cardPaymentMdl);

        public void onCancel(CardPaymentPopup_Mdl cardPaymentMdl);
    }

    private final CardPaymentPopupGridEdit<GrdCardPayment_Mdl, GrdReceipt_PrpAcs> cardPaymentPopupGridEdit = new CardPaymentPopupGridEdit(grdReceipt);
    private CallBack callBack;
    protected CardPaymentPopup_Mdl cardPayMdl = new CardPaymentPopup_Mdl();
    private boolean isCTRAllowed = false, isCTOAllowed = false, isCTPAllowed = false, isNoPaymentAllowed = false, isReceiptAllowed = true;
    private PaymentSource defaultPaymentSource;
    private String status = "";

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void textHelp() {
        txtHRecieptType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHReceiptType_VList_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                txtHRecieptType.setData(cardPayMdl.getTxtHReceiptTypeVList(), helper.getPaging());
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHReceiptType_VList_Mdl t1Row) {
                if (blnStatus && t1Row != null) {
                    int index = -1;
                    txtHRecieptType.setTag(DmShared.xNull(t1Row.getV_type()));
                    txtHRecieptType.setText(DmShared.xNull(t1Row.getDescription()));
                    txtHRecieptType.setTag2nd(DmShared.xNull(t1Row.getPaymentMode()));

                    /*=========================< Set the Default Value of card Payment Mode by selection of Receipt Type >============================*/
                    grdReceipt.getGrid().getStore().clear();
                    if (t1Row.getCardPaymentDefaultMdl() != null) {
                        if (DmShared.iNull(t1Row.getCardPaymentDefaultMdl().getCardTypeCode()).longValue() != 0) {
                            grdReceipt.getGrid().getStore().add(t1Row.getCardPaymentDefaultMdl().clone());
                            if (index == -1) {
                                index = grdReceipt.getGrid().getStore().size() - 1;
                            } else if (index >= 0) {
                                index = -2;
                            }
                        }
                    }
                    grdReceipt.getGrid().getStore().add(new GrdCardPayment_Mdl());
                    if (index >= 0) {
                        grdReceipt.getGrid().getStore().get(index).setAmount(DmClient.vNull(cardPayMdl.getAmount(), "0.00"));
                    }
                    grdReceipt.commitChanges();
                    calculate();
                }
            }
        });
        txtHRecieptType.addFocusHandler(new FocusHandler() {
            @Override
            public void onFocus(FocusEvent event) {
                txtHRecieptType.setFocus(true);
            }
        });

        tabPanel.addSelectionHandler(new SelectionHandler<Integer>() {
            @Override
            public void onSelection(SelectionEvent<Integer> event) {
                if (null != event.getSelectedItem()) {
                    tabPanel.selectTab(event.getSelectedItem());
                    Widget widget = tabPanel.getWidget(event.getSelectedItem());

                    if (widget != null) {
                        if (widget == flwTabReceipt && flwTabReceipt.isAttached()) {
                            grdReceipt.getGrid().getStore().add(new GrdCardPayment_Mdl());
                            setFocus(((DmTextBox) null), ((DmTextBoxNumeric) null), txtHRecieptType);

                        } else if (widget == flwTabCTP && flwTabCTP.isAttached()) {
                            txtTabCTPAmount.setText(DmClient.vNull(txtAmount.getText(), "0.00"));
                            txtCollected.setFocus(true);

                        } else if (widget == flwTabCTO && flwTabCTO.isAttached()) {
                            txtCTOAmount.setText(DmClient.vNull(txtAmount.getText(), "0.00"));
                            txtCollected.setFocus(true);

                        } else if (widget == flwTabCTR && flwTabCTR.isAttached()) {
                            txtCTRAmount.setText(DmClient.vNull(txtAmount.getText(), "0.00"));
                            txtCollected.setFocus(true);

                        } else {
                            txtCollected.setFocus(true);
                        }
                    } else {
                        txtCollected.setFocus(true);
                    }
                    calculate();
                }
            }
        });
    }

    private void setFocus(final DmTextBox txtBox, final DmTextBoxNumeric txtBoxNumeric, final DmTextHelp txtHelp) {
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                if (txtBox != null) {
                    txtBox.setFocus(true);
                } else if (txtBoxNumeric != null) {
                    txtBoxNumeric.setFocus(true);
                } else if (txtHelp != null) {
                    txtHelp.setFocus(true);
                }
            }
        });
    }

    public void hide() {
        popupPanel.hide();
    }

    String entryMode = "";

    public void show(CardPaymentPopup_Mdl cardPaymentMdl, String entryMode) {
        int index = -1;
        cardPayMdl = new CardPaymentPopup_Mdl();
        cardPayMdl = cardPaymentMdl.clone();
        status = "";
        this.entryMode = entryMode;
        status = cardPaymentMdl.getStatus();
        List<GrdCardPayment_Mdl> cardPaymentList = new ArrayList<GrdCardPayment_Mdl>();

        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        manageDisplay();
        flwOk.clear();
        lblPaymentSourceCaption.setText("");
        lblPaymentSourceValue.setText("");
        flwPaymentSourceCaption.clear();
        flwPaymentSourceValue.clear();
        flwOk.setVisible(false);
        grdReceipt.getGrid().getStore().clear();
        txtAmount.setText(DmClient.vNull(cardPaymentMdl.getAmount(), "0.00"));

        if (DmShared.xNull(cardPaymentMdl.getReceiptTypeCode()).equalsIgnoreCase("")) {
            if (cardPaymentMdl.getTxtHReceiptTypeVList() != null) {
                for (int i = 0; i < cardPaymentMdl.getTxtHReceiptTypeVList().size(); i++) {
                    if (!DmShared.xNull(cardPaymentMdl.getTxtHReceiptTypeVList().get(i).getV_type()).equalsIgnoreCase("")) {
                        if (DmShared.iNull(cardPaymentMdl.getTxtHReceiptTypeVList().get(i).getIsDefault()).intValue() == 1) {
                            txtHRecieptType.setText(DmShared.xNull(cardPaymentMdl.getTxtHReceiptTypeVList().get(i).getDescription()));
                            txtHRecieptType.setTag(DmShared.xNull(cardPaymentMdl.getTxtHReceiptTypeVList().get(i).getV_type()));
                            txtHRecieptType.setTag2nd(DmShared.xNull(cardPaymentMdl.getTxtHReceiptTypeVList().get(i).getPaymentMode()));
                            cardPaymentList.add(cardPaymentMdl.getTxtHReceiptTypeVList().get(i).getCardPaymentDefaultMdl());
                        }
                    }
                }
            }
        } else {
            txtHRecieptType.setText(DmShared.xNull(cardPaymentMdl.getReceiptTypeName()));
            txtHRecieptType.setTag(DmShared.xNull(cardPaymentMdl.getReceiptTypeCode()));
            txtHRecieptType.setTag2nd(DmShared.xNull(cardPaymentMdl.getReceiptTypePM()));
            cardPaymentList.addAll(cardPaymentMdl.getGrdCardPaymentList());
        }

        if (cardPaymentMdl.getRegPaymentSource() != null) {
            if (cardPaymentMdl.getRegPaymentSource() != cardPayMdl.getPaymentSource()) {
                lblPaymentSourceCaption.setText("Converted payment source: ");
                flwPaymentSourceCaption.add(lblPaymentSourceCaption);
                lblPaymentSourceValue.setText(cardPaymentMdl.getRegPaymentSource().getEnumName());
                flwPaymentSourceValue.add(lblPaymentSourceValue);
            }
        }

        txtRecieptNo.setText(DmShared.xNull(cardPaymentMdl.getReceiptNo()));
        txtPatient.setText(DmShared.xNull(cardPaymentMdl.getPatientName()));
        txtRegNo.setText(DmShared.xNull(cardPaymentMdl.getRegNo()));
        txtUHIDNo.setText(DmShared.xNull(cardPaymentMdl.getUHIDNo()));
        txtRegNo.setTag(DmShared.xNull(cardPaymentMdl.getRegNoDocid()));
        txtUHIDNo.setTag(DmShared.xNull(cardPaymentMdl.getUHIDDocid()));

        txtCTOAmount.setText("");
        txtCTOCardType.setText(DmShared.xNull(cardPaymentMdl.getCTOCardType()));
        txtCTOCardNo.setText(DmShared.xNull(cardPaymentMdl.getCTOCardNo()));
        txtCTOCardHolderName.setText(DmShared.xNull(cardPaymentMdl.getCTOCardHolderName()));
        txtCTORankNo.setText(DmShared.xNull(cardPaymentMdl.getCTORankNo()));
        txtCTORelation.setText(DmShared.xNull(cardPaymentMdl.getCTORelation()));

        txtCTPAmount.setText("");
        txtCTPAuthBy.setText("");
        txtCTPRemark.setText("");

        txtTabCTPAmount.setText("");
        txtTabCTPAuthBy.setText("");
        txtTabCTPRemark.setText("");

        txtCTRAmount.setText("");
        txtCTRRemark.setText("");

        /*============================< Set the Default Value of card Payment Mode On Show Popup >==========================*/
        if (cardPaymentList != null) {
            for (int i = 0; i < cardPaymentList.size(); i++) {
                if (DmShared.iNull(cardPaymentList.get(i).getCardTypeCode()).longValue() != 0) {
                    grdReceipt.getGrid().getStore().add(cardPaymentList.get(i).clone());
                    if (index == -1) {
                        index = grdReceipt.getGrid().getStore().size() - 1;
                    } else if (index >= 0) {
                        index = -2;
                    }
                }
            }
        }

//        grdReceipt.getGrid().getStore().add(new GrdCardPayment_Mdl());
        if (entryMode.equalsIgnoreCase("Browse")) {
            flwOk.clear();
            flwOk.setVisible(false);
            txtHRecieptType.setEnabled(false);
        } else {
            if (entryMode.equalsIgnoreCase("Edit")) {
                txtHRecieptType.setEnabled(false);
                if (index == 0 && cardPaymentMdl.getPaymentSource() == PaymentSource.PAYMENT) {
                    if (DmShared.vNull(grdReceipt.getGrid().getStore().get(index).getAmount()).doubleValue() != 0) {
                        grdReceipt.getGrid().getStore().get(index).setAmount(DmClient.vNull(cardPaymentMdl.getAmount(), "0.00"));
                    }
                }
            } else {
                txtHRecieptType.setEnabled(true);
                if (index >= 0) {
                    if (DmShared.vNull(grdReceipt.getGrid().getStore().get(index).getAmount()).doubleValue() == 0) {
                        grdReceipt.getGrid().getStore().get(index).setAmount(DmClient.vNull(cardPaymentMdl.getAmount(), "0.00"));
                    }
                }
            }
            flwOk.add(btnOk);
            flwOk.setVisible(true);
        }

        if (entryMode.equalsIgnoreCase("Browse")
                || entryMode.equalsIgnoreCase("Edit")) {

            flwReceiptWidget.removeFromParent();
            flwCTPWidget.removeFromParent();
            tabPanel.clear();

            if (cardPayMdl.getPaymentSource() == PaymentSource.PAYMENT
                    || cardPayMdl.getPaymentSource() == PaymentSource.PARTIAL_PAYMENT
                    || cardPayMdl.getPaymentSource() == PaymentSource.CREDIT_TO_PATIENT) {
                if (cardPayMdl.getGrdCardPaymentList().size() > 0) {
                    addTabReceipt();

                    if (cardPayMdl.getPaymentSource() == PaymentSource.PAYMENT) {
                        flwReceipt.add(flwReceiptWidget);

                    } else if (cardPayMdl.getPaymentSource() == PaymentSource.PARTIAL_PAYMENT
                            || cardPayMdl.getPaymentSource() == PaymentSource.CREDIT_TO_PATIENT) {

                        if (cardPayMdl.getPaymentSource() == PaymentSource.PARTIAL_PAYMENT) {
                            flwReceipt.add(flwReceiptWidget);

                        }
                        flwCTP.add(flwCTPWidget);
                        txtCTPAmount.setText(DmClient.vNull(cardPaymentMdl.getCreditAmount(), "0.00"));
                        txtCTPAuthBy.setText(DmShared.xNull(cardPaymentMdl.getAuthBy()));
                        txtCTPRemark.setText(DmShared.xNull(cardPaymentMdl.getRemark()));
                    }
                }
//                else {
//                    addTabCTP();
//                    txtTabCTPAmount.setText(DmClient.vNull(cardPaymentMdl.getCreditAmount(), "0.00"));
//                    txtTabCTPAuthBy.setText(DmShared.xNull(cardPaymentMdl.getAuthBy()));
//                    txtTabCTPRemark.setText(DmShared.xNull(cardPaymentMdl.getRemark()));
//                } 

            } else if (cardPayMdl.getPaymentSource() == PaymentSource.CREDIT_TO_ORGANIZATION) {
                addTabCTO();
                if (DmShared.vNull(cardPaymentMdl.getAmount()).doubleValue() != DmShared.vNull(cardPaymentMdl.getCreditAmount()).doubleValue()) {
                    txtCTOAmount.setText(DmClient.vNull(cardPaymentMdl.getAmount(), "0.00"));
                } else {
                    txtCTOAmount.setText(DmClient.vNull(cardPaymentMdl.getCreditAmount(), "0.00"));
                }
            } else if (cardPayMdl.getPaymentSource() == PaymentSource.CREDIT_TO_REGISTRATION) {
                addTabCTR();
                if (DmShared.vNull(cardPaymentMdl.getAmount()).doubleValue() != DmShared.vNull(cardPaymentMdl.getCreditAmount()).doubleValue()) {
                    txtCTRAmount.setText(DmClient.vNull(cardPaymentMdl.getAmount(), "0.00"));
                } else {
                    txtCTRAmount.setText(DmClient.vNull(cardPaymentMdl.getCreditAmount(), "0.00"));
                }
                txtCTPRemark.setText(DmShared.xNull(cardPaymentMdl.getRemark()));
            } else if (cardPayMdl.getPaymentSource() == PaymentSource.NO_PAYMENT) {
                addTabNoPayment();
            }
        } else {
            flwReceiptWidget.removeFromParent();
            flwCTPWidget.removeFromParent();
            tabPanel.clear();

            if (isReceiptAllowed) {
                addTabReceipt();
                flwReceipt.add(flwReceiptWidget);
            }

            if (isCTPAllowed) {
                flwCTP.add(flwCTPWidget);
                addTabCTP();
            }

            if (isCTOAllowed) {
                addTabCTO();
                if (defaultPaymentSource == PaymentSource.CREDIT_TO_ORGANIZATION) {
                    grdReceipt.getGrid().getStore().clear();
                    tabPanel.selectTab(flwTabCTO);
                }
            }

            if (isCTRAllowed) {
                addTabCTR();
                if (defaultPaymentSource == PaymentSource.CREDIT_TO_REGISTRATION) {
                    grdReceipt.getGrid().getStore().clear();
                    tabPanel.selectTab(flwTabCTR);
                }
            }

            if (isNoPaymentAllowed) {
                addTabNoPayment();
            }
        }

        grdReceipt.commitChanges();
        setFocus((DmTextBox) null, txtCollected, (DmTextHelp) null);

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                calculate();
            }
        });
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            cardPaymentPopupGridEdit.hide();
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {

            for (GrdCardPayment_Mdl node : grdReceipt.getGrid().getStore().getAll()) {

                if (node.isPaymentSuccess() && entryMode.equalsIgnoreCase("New")) {
                    new DmAlertMessageBox("POS payment has been received , can't be close.", (DmTextBox) null).show();

                    return;

                }

            }

            hide();
        }
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (event.getSource().equals(txtCollected)) {
            calculate();
        } else if (event.getSource().equals(txtCTPAmount)
                || event.getSource().equals(txtCTOAmount)
                || event.getSource().equals(txtCTRAmount)) {
            calculate();
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        CardPaymentPopup_Mdl cardPaymentMdl;
        if (event.getSource() == btnCancel) {

            for (GrdCardPayment_Mdl node : grdReceipt.getGrid().getStore().getAll()) {

                if (node.isPaymentSuccess() && entryMode.equalsIgnoreCase("New")) {
                    new DmAlertMessageBox("POS payment has been received , can't be close.", (DmTextBox) null).show();

                    return;

                }

            }

            cardPaymentMdl = new CardPaymentPopup_Mdl();
            cardPaymentMdl = cardPayMdl.clone();
            cardPaymentMdl.getGrdCardPaymentList().clear();
            cardPaymentMdl.getGrdCardPaymentList().addAll(grdReceipt.getGrid().getStore().getAll());
            cardPaymentMdl.setReceiptTypeCode(DmShared.xNull(txtHRecieptType.getTag()));
            cardPaymentMdl.setReceiptTypeName(DmShared.xNull(txtHRecieptType.getText()));
            cardPaymentMdl.setReceiptTypePM(DmShared.xNull(txtHRecieptType.getTag2nd()));
            callBack.onCancel(cardPaymentMdl);
            hide();

        } else if (event.getSource() == btnOk) {

            for (GrdCardPayment_Mdl node : grdReceipt.getGrid().getStore().getAll()) {

                if (node.isPaymentSuccess() && entryMode.equalsIgnoreCase("Edit")) {
                    new DmAlertMessageBox("POS payment has been received , can't be modify.", (DmTextBox) null).show();
                    return;

                }

            }

            for (GrdCardPayment_Mdl node : grdReceipt.getGrid().getStore().getAll()) {

                if (DmShared.iNull(node.getCardTypeNatureCode()).intValue() == CardType.POS.getEnumValue() && !node.isPaymentSuccess()) {
                    new DmAlertMessageBox("Payment not received yet!", (DmTextBox) null).show();
                    return;
                }

            }

            cardPaymentMdl = new CardPaymentPopup_Mdl();
            cardPaymentMdl = cardPayMdl.clone();
//            calculate();
            if (tabPanel.getWidget(tabPanel.getSelectedIndex()) == flwTabNoPayment) {
                cardPaymentMdl.setPaymentSource(PaymentSource.NO_PAYMENT);

            } else if (tabPanel.getWidget(tabPanel.getSelectedIndex()) == flwTabReceipt
                    || tabPanel.getWidget(tabPanel.getSelectedIndex()) == flwTabCTP) {
                cardPaymentMdl.setPaymentSource(null);

                if (DmShared.vNull(txtReceipt.getText()).doubleValue() != 0) {
                    cardPaymentMdl.setPaymentSource(PaymentSource.PAYMENT);
                } else if (DmShared.vNull(txtReceipt.getText()).doubleValue() == 0) {
                    cardPaymentMdl.setPaymentSource(PaymentSource.NO_PAYMENT);
                }

                if (DmShared.vNull(txtCTPAmount.getText()).doubleValue() != 0
                        || DmShared.vNull(txtTabCTPAmount.getText()).doubleValue() != 0) {
                    if (cardPaymentMdl.getPaymentSource() == PaymentSource.PAYMENT) {
                        cardPaymentMdl.setPaymentSource(PaymentSource.PARTIAL_PAYMENT);
                    } else {
                        cardPaymentMdl.setPaymentSource(PaymentSource.CREDIT_TO_PATIENT);
                    }
                }

            } else if (tabPanel.getWidget(tabPanel.getSelectedIndex()) == flwTabCTO) {
                cardPaymentMdl.setPaymentSource(PaymentSource.CREDIT_TO_ORGANIZATION);

                if (DmShared.xNull(txtCTOCardType.getText()).trim().equalsIgnoreCase("")) {
                    new DmAlertMessageBox("Organization is Mandatory.", txtCTOCardType).show();
                    return;
                }

                if (DmShared.xNull(txtCTOCardNo.getText()).trim().equalsIgnoreCase("")) {
                    new DmAlertMessageBox("Emp./Policy/Serice No. is Mandatory.", txtCTOCardNo).show();
                    return;
                }

                if (DmShared.xNull(txtCTORelation.getText()).trim().equalsIgnoreCase("")) {
                    new DmAlertMessageBox("Relation is Mandatory.", txtCTORelation).show();
                    return;
                }

                //======================<Add status as requred by Mr. Sandeep And and instructed by Boss> -Start==============
                if (DmShared.vNull(txtCTOAmount.getText()).doubleValue() == 0 && !DmShared.xNull(status).equalsIgnoreCase("Revisit")) {
                    new DmAlertMessageBox("Amount is Mandatory.", txtCTOAmount).show();
                    return;
                }
                //======================<Add status as requred by Mr. Sandeep And and instructed by Boss> -End==============
            } else if (tabPanel.getWidget(tabPanel.getSelectedIndex()) == flwTabCTR) {
                cardPaymentMdl.setPaymentSource(PaymentSource.CREDIT_TO_REGISTRATION);

                if (DmShared.vNull(txtCTRAmount.getText()).doubleValue() == 0) {
                    new DmAlertMessageBox("Amount is Mandatory.", txtCTRAmount).show();
                    return;
                }
            }

            if (cardPaymentMdl.getPaymentSource() == PaymentSource.PAYMENT
                    || cardPaymentMdl.getPaymentSource() == PaymentSource.PARTIAL_PAYMENT) {
                if (DmShared.xNull(txtHRecieptType.getTag()).equalsIgnoreCase("")) {
                    new DmAlertMessageBox("Reciept Type is Mandatory.", txtHRecieptType).show();
                    return;
                }
            }

            if (cardPaymentMdl.getPaymentSource() == PaymentSource.PARTIAL_PAYMENT
                    || cardPaymentMdl.getPaymentSource() == PaymentSource.CREDIT_TO_PATIENT) {
                if (tabPanel.getWidget(tabPanel.getSelectedIndex()) == flwTabReceipt) {
                    if (DmShared.vNull(txtCTPAmount.getText()).doubleValue() == 0) {
                        new DmAlertMessageBox("Amount is Mandatory.", txtCTPAmount).show();
                        return;
                    }

                    if (DmShared.xNull(txtCTPAuthBy.getText()).equalsIgnoreCase("")) {
                        new DmAlertMessageBox("Credit To Patient Authorized By is mandatory.", txtCTPAuthBy).show();
                        return;
                    }
                } else {
                    if (DmShared.vNull(txtTabCTPAmount.getText()).doubleValue() == 0) {
                        new DmAlertMessageBox("Amount is Mandatory.", txtCTPAmount).show();
                        return;
                    }

                    if (DmShared.xNull(txtTabCTPAuthBy.getText()).equalsIgnoreCase("")) {
                        new DmAlertMessageBox("Credit To Patient Authorized By is mandatory.", txtCTPAuthBy).show();
                        return;
                    }
                }
            }
//            if (cardPaymentMdl.getPaymentSource() == null) {
//                new DmAlertMessageBox("No Patient Source Choosen.", txtAmount).show();
//                return;
//            }
            try {
                cardPaymentMdl.getGrdCardPaymentList().clear();

                if (null != cardPaymentMdl.getPaymentSource()) {
                    switch (cardPaymentMdl.getPaymentSource()) {
                        case PAYMENT:
                        case PARTIAL_PAYMENT:
                        case CREDIT_TO_PATIENT:
                            if (tabPanel.getWidget(tabPanel.getSelectedIndex()) == flwTabReceipt) {
                                cardPaymentMdl.getGrdCardPaymentList().addAll(grdReceipt.getGrid().getStore().getAll());
                                cardPaymentMdl.setReceiptTypeCode(DmShared.xNull(txtHRecieptType.getTag()));
                                cardPaymentMdl.setReceiptTypeName(DmShared.xNull(txtHRecieptType.getText()));
                                cardPaymentMdl.setReceiptTypePM(DmShared.xNull(txtHRecieptType.getTag2nd()));
                                cardPaymentMdl.setCreditAmount(DmClient.vNull(txtCTPAmount.getText(), "0.00"));
                                cardPaymentMdl.setAuthBy(DmShared.xNull(txtCTPAuthBy.getText()));
                                cardPaymentMdl.setRemark(DmShared.xNull(txtCTPRemark.getText()));
                            } else {
                                cardPaymentMdl.setCreditAmount(DmClient.vNull(txtTabCTPAmount.getText(), "0.00"));
                                cardPaymentMdl.setAuthBy(DmShared.xNull(txtTabCTPAuthBy.getText()));
                                cardPaymentMdl.setRemark(DmShared.xNull(txtTabCTPRemark.getText()));
                            }
                            break;
                        case CREDIT_TO_ORGANIZATION:
                            cardPaymentMdl.setCreditAmount(DmClient.vNull(txtCTOAmount.getText(), "0.00"));
                            cardPaymentMdl.setAuthBy("");
                            cardPaymentMdl.setRemark("");
                            cardPaymentMdl.setCTOCardType(DmShared.xNull(txtCTOCardType.getText()));
                            cardPaymentMdl.setCTOCardNo(DmShared.xNull(txtCTOCardNo.getText()));
                            cardPaymentMdl.setCTOCardHolderName(DmShared.xNull(txtCTOCardHolderName.getText()));
                            cardPaymentMdl.setCTORankNo(DmShared.xNull(txtCTORankNo.getText()));
                            break;
                        case CREDIT_TO_REGISTRATION:
                            cardPaymentMdl.setCreditAmount(DmClient.vNull(txtCTRAmount.getText(), "0.00"));
                            cardPaymentMdl.setAuthBy("");
                            cardPaymentMdl.setRemark(DmShared.xNull(txtCTRRemark.getText()));
                            break;
                        case NO_PAYMENT:
                            cardPaymentMdl.setReceiptTypeCode(DmShared.xNull(txtHRecieptType.getTag()));
                            cardPaymentMdl.setReceiptTypeName(DmShared.xNull(txtHRecieptType.getText()));
                            cardPaymentMdl.setReceiptTypePM(DmShared.xNull(txtHRecieptType.getTag2nd()));
                            break;
                    }
                }
                cardPaymentMdl.setStatus(status);
                chkSave(txtAmount.getText(), cardPaymentMdl);
                callBack.onOk(cardPaymentMdl);
                hide();
            } catch (DmException e) {
                new DmAlertMessageBox(e.getMessage(), txtCollected).show();
            }
        }

    }

    private void gridEvents() {
        cardPaymentPopupGridEdit.addCallBack(new CardPaymentPopupGridEdit.CallBack() {

            @Override
            public void onOk(GrdCardPayment_Mdl grdEntry) {
                grdReceipt.commitChanges();
                calculate();
            }

            @Override
            public void onDelete() {
                grdReceipt.commitChanges();
                calculate();
            }
        });

        grdReceipt.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showCardGridEdit(event.getRowIndex());
            }
        });

        grdReceipt.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showCardGridEdit(grdReceipt.getGrid().getStore().indexOf(grdReceipt.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
    }

    private void showCardGridEdit(int currentRowIndex) {
        if (!flwOk.isVisible()) {
            cardPaymentPopupGridEdit.show(currentRowIndex, false, false, false, getcardTypeArray(DmShared.xNull(txtHRecieptType.getTag2nd())),
                    txtBalance.getText(), txtHRecieptType.getTag(), this, entryMode);
        } else {
            cardPaymentPopupGridEdit.show(currentRowIndex, true, true, true, getcardTypeArray(DmShared.xNull(txtHRecieptType.getTag2nd())),
                    DmClient.vNull((DmShared.vNull(txtAmount.getText()).doubleValue() - DmShared.vNull(txtReceipt.getText()).doubleValue()), "0.00"),
                    txtHRecieptType.getTag(), this, entryMode);
        }
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    public CardPaymentPopup() {
        super();
        gridEvents();
        textHelp();
        txtCollected.addKeyUpHandler(this);
        txtCTOAmount.addKeyUpHandler(this);
        txtCTRAmount.addKeyUpHandler(this);
        txtCTPAmount.addKeyUpHandler(this);
        popupPanel.addAttachHandler(this);
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);

    }

    private void calculate() {
        List<GrdCardPayment_Mdl> mrTemp = grdReceipt.getGrid().getStore().getAll();
        txtReceipt.setText("");
        txtCredited.setText("");
        if (tabPanel.getWidget(tabPanel.getSelectedIndex()) == flwTabReceipt) {
            txtReceipt.setText(DmClient.vNull(CardPaymentPopup_Vld.calculate(mrTemp), "0.00"));

            if (isCTPAllowed && flwCTPWidget.isAttached()) {
                txtTabCTPAmount.setText("0.00");
                txtCTPAmount.setText(DmClient.vNull((DmShared.vNull(txtAmount.getText()).doubleValue() - DmShared.vNull(txtReceipt.getText()).doubleValue()), "0.00"));
            }

            txtCredited.setText(DmClient.vNull(DmShared.vNull(txtCTPAmount.getText()).doubleValue(), "0.00"));

        } else if (tabPanel.getWidget(tabPanel.getSelectedIndex()) == flwTabCTP) {
            if (isCTPAllowed) {
                txtTabCTPAmount.setText(DmClient.vNull(txtAmount.getText(), "0.00"));
            }
            txtCredited.setText(DmClient.vNull(DmShared.vNull(txtTabCTPAmount.getText()).doubleValue(), "0.00"));

        } else if (tabPanel.getWidget(tabPanel.getSelectedIndex()) == flwTabCTO) {
            txtCredited.setText(DmClient.vNull(DmShared.vNull(txtCTOAmount.getText()).doubleValue(), "0.00"));

        } else if (tabPanel.getWidget(tabPanel.getSelectedIndex()) == flwTabCTR) {
            txtCredited.setText(DmClient.vNull(DmShared.vNull(txtCTRAmount.getText()).doubleValue(), "0.00"));
        }

        txtBalance.setText(DmClient.vNull(DmShared.vNull(DmClient.vNull(txtAmount.getText(), "0.00")) - (DmShared.vNull(DmClient.vNull(txtReceipt.getText(), "0.00")) + DmShared.vNull(DmClient.vNull(txtCredited.getText(), "0.00"))), "0.00"));

        calculateRefund();
    }

    private void calculateRefund() {
        txtReturn.setText(DmClient.vNull(DmShared.vNull(txtCollected.getText()).doubleValue() - DmShared.vNull(txtReceipt.getText()).doubleValue(), "0.00"));
    }

    private void manageDisplay() {
        txtCTOAmount.setEnabled(true);
        txtCTRAmount.setEnabled(true);
        txtCTPAmount.setEnabled(false);
        txtAmount.setEnabled(false);
        txtReceipt.setEnabled(false);
        txtCredited.setEnabled(false);
        txtBalance.setEnabled(false);
        txtReturn.setEnabled(false);
        txtRegNo.setEnabled(false);
        txtPatient.setEnabled(false);
        txtRecieptNo.setEnabled(false);
        txtUHIDNo.setEnabled(false);

        txtCTOCardType.setEnabled(false);
        txtCTOCardNo.setEnabled(false);
        txtCTOCardHolderName.setEnabled(false);
        txtCTORankNo.setEnabled(false);
        txtCTORelation.setEnabled(false);
    }

    private String[] getcardTypeArray(String cardTypes) {
        String newCardType = cardTypes.replaceAll("\\|", ",");

        return newCardType.split(",");
    }

    public void setCTRAllowed(boolean isCTRAllowed) {
        this.isCTRAllowed = isCTRAllowed;
    }

    public void setReceiptAllowed(boolean isReceiptAllowed) {
        this.isReceiptAllowed = isReceiptAllowed;
    }

    public void setCTPAllowed(boolean isCTPAllowed) {
        this.isCTPAllowed = isCTPAllowed;
    }

    public void setCTOAllowed(boolean isCTOAllowed) {
        this.isCTOAllowed = isCTOAllowed;
    }

    public void setNoPaymentAllowed(boolean isNoPaymentAllowed) {
        this.isNoPaymentAllowed = isNoPaymentAllowed;
    }

    public void setDefaultPaymentSource(PaymentSource defaultPaymentSource) {
        this.defaultPaymentSource = defaultPaymentSource;
    }

    public void chkSave(String netAmount, CardPaymentPopup_Mdl cardPaymentPopup_Mdl) {
        CardPaymentPopup_Vld.chkSave(netAmount, cardPaymentPopup_Mdl, isCTRAllowed, isCTOAllowed, isCTPAllowed);
    }
}
