package aarogya.selfToolkit.cardPaymentPopup.client;

import aarogya.selfToolkit.cardPaymentPopup.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class CardPaymentPopupGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();
    final DmTextBox txtCardType = new DmTextBox();
    final DmTextBox txtCardTypeShortName = new DmTextBox();
    final DmTextBoxNumeric txtAmount = new DmTextBoxNumeric(2, true, 12, true);
    final DmTextBox txtBalance = new DmTextBox();
    final DmTextBox txtAcName = new DmTextBox();
    final FlowPanel flwCC = new FlowPanel();
    final FlowPanel flwBank = new FlowPanel();
    final FlowPanel flwDigital = new FlowPanel();
    final FlowPanel flwELSE = new FlowPanel();
    final FlowPanel flwWallet = new FlowPanel();
    final FlowPanel flwCurrency = new FlowPanel();
    final FlowPanel flwAccount = new FlowPanel();
    final FlowPanel flwAccountTextHelp = new FlowPanel();
    final FlowPanel flwAccountTextBox = new FlowPanel();
    final FlowPanel flwPayment = new FlowPanel();

    /*=========================< Wallet >============================*/
    final DmTextBox txtWalletNo = new DmTextBox(45, true);
    final DmTextBox txtCardHolderNameWT = new DmTextBox(45, false);
    final DmTextBox txtAuthorizationWT = new DmTextBox(45, false);
    final DmLabel lblMessage = new DmLabel("");

    /*=========================< DIGITAL >============================*/
    final DmTextBox txtTransactionNoDG = new DmTextBox(45, false);

    /*=========================< PAYMENT >============================*/
    final DmTextBox txtMobileNo = new DmTextBox(15, false);
    final FlowPanel flwMakePayment = new FlowPanel();
    final Button btnMakePayment = new Button("Make Payment");
    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancelPayment = new Button("Cancel Payment");
    final Button btnChequePrint = new Button("Cheque Print");
    final Anchor spanCountDown = new Anchor();
    final DmLabel lblProcessing = new DmLabel("");
    final Button btnCheckStatus = new Button("Check Status");

    //<editor-fold defaultstate="collapsed" desc="txtHPaymentMode.">
    DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHPaymentMode = new DmTextHelp<>(com.google.gwt.core.client.GWT.create(KeyField_PrpAcs.class), 5, false);

    private void iniTxtHPaymentMode() {
        txtHPaymentMode.getDmGrid().addTextColumn(txtHPaymentMode.getDmGrid().getPropertyAccess().name(), "Mode", 200, "name");
        txtHPaymentMode.getDmGrid().initializeGrid(txtHPaymentMode.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHPaymentMode.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    /*=========================< BANK  >============================*/
    final DmTextBox txtInstrumentNoBK = new DmTextBox(20, false);
    final DmTextBox txtRemarkBK = new DmTextBox(45, false);
    public final DmTextBox txtDate = new DmTextBox(11, false);
    final DmTextBox txtFavourOf = new DmTextBox(45, false);

    /*=========================< CC >============================*/
    final DmTextBox txtCardNoCC = new DmTextBox(20, false);
    final DmTextBox txtCardHolderNameCC = new DmTextBox(45, false);
    final DmTextBox txtAuthorizationCC = new DmTextBox(45, false);
    /*================================<Currency>=====================*/
    final DmTextBoxNumeric txtConversionRate = new DmTextBoxNumeric(7, false, 14, false);
    final DmTextBoxNumeric txtForeignCurrencyAmount = new DmTextBoxNumeric(2, true, 12, true);
    final DmLabel lblForeignCurrency = new DmLabel("");
    final DmTextBox txtCurrencyRemark = new DmTextBox(45, false);
    /*=========================< ELSE >============================*/
    final DmTextBox txtRemark = new DmTextBox(45, false);
    final FlowPanel mainFlwPanel = new FlowPanel();

    //<editor-fold defaultstate="collapsed" desc="TxtHCardType">
    interface TxtHCardNo_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdCardPayment_Mdl> {

        @Editor.Path("CardTypeName")
        ValueProvider<GrdCardPayment_Mdl, String> cardTypeName();

        @Editor.Path("CardTypeNatureName")
        ValueProvider<GrdCardPayment_Mdl, String> cardTypeNatureName();
    }
    final DmTextHelp<GrdCardPayment_Mdl, TxtHCardNo_PrpAcs> txtHCardPayment = new DmTextHelp(GWT.create(TxtHCardNo_PrpAcs.class), 6, true);

    private void iniTxtHCardPayment() {
        txtHCardPayment.getDmGrid().addTextColumn(txtHCardPayment.getDmGrid().getPropertyAccess().cardTypeName(), "Payment Mode", 150, "cardTypeName");
        txtHCardPayment.getDmGrid().addTextColumn(txtHCardPayment.getDmGrid().getPropertyAccess().cardTypeNatureName(), "Card Type", 140, "cardType");
        txtHCardPayment.getDmGrid().initializeGrid(txtHCardPayment.getDmGrid().getPropertyAccess().fieldHashCode(), 325, 0, 1);
        txtHCardPayment.getHelper().addSort("cardType", DmSortType.ASC);
        txtHCardPayment.getHelper().addSort("cardTypeName", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHAccount">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHAccount = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniTxtHAccount() {
        txtHAccount.getDmGrid().addTextColumn(txtHAccount.getDmGrid().getPropertyAccess().name(), "Name", 150, "name");
        txtHAccount.getDmGrid().initializeGrid(txtHAccount.getDmGrid().getPropertyAccess().code(), 325, 0, 1);
        txtHAccount.getHelper().addSort("name", DmSortType.ASC);
        txtHAccount.getHelper().addSort("nature", DmSortType.ASC);
    }
    //</editor-fold>

    public CardPaymentPopupGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        iniTxtHCardPayment();
        iniTxtHAccount();
        iniTxtHPaymentMode();
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "180px", null, null, DmPosition.CENTER, true);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "170px", "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "55%", "170px", "left", null, DmPosition.NONE, false);
        flwTd.add(leftArea());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "45%", "170px", "left", null, DmPosition.NONE, false);
        flwTd.add(rightArea());
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        lblMessage.setHeight("15Px");
        lblMessage.getElement().getStyle().setColor("red");
        lblMessage.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        lblMessage.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        lblMessage.getElement().getStyle().setMarginTop(6, Style.Unit.PX);
        lblMessage.getElement().getStyle().setMarginLeft(2, Style.Unit.PX);
        lblMessage.getElement().getStyle().setFontSize(12, Style.Unit.PX);

        flwTr.add(lblMessage);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel leftArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "97%", "140px", null, null, DmPosition.CENTER, true);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Payment Mode");
        lbl.setTitle("Payment Mode - PM");
        lbl.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHCardPayment));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtCardType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "24%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtCardTypeShortName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Amount");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Balance");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAmount));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtBalance));
        txtBalance.setEnabled(false);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("A/c Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        DmClient.setStyle(flwAccount, "100%", null, "left", null, DmPosition.NONE, false);
        DmClient.setStyle(flwAccountTextBox, "100%", null, "left", null, DmPosition.NONE, false);
        DmClient.setStyle(flwAccountTextHelp, "100%", null, "left", null, DmPosition.NONE, false);
        flwAccountTextBox.add(vl.add(txtAcName));
        flwAccountTextHelp.add(vl.add(txtHAccount));
        flwAccount.add(flwAccountTextBox);
//        txtAcName.setEnabled(false);
        flwTr.add(flwAccount);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel rightArea() {
        FlowPanel flwTable;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "140px", null, null, DmPosition.CENTER, true);
        DmClient.setStyle(flwCC, "100%", "140px", null, null, null, true);
        DmClient.setStyle(flwBank, "100%", "140px", null, null, null, true);
        DmClient.setStyle(flwDigital, "100%", "140px", null, null, null, true);
        DmClient.setStyle(flwWallet, "100%", "140px", null, null, null, true);
        DmClient.setStyle(flwPayment, "100%", "160px", null, null, null, true);
        DmClient.setStyle(flwELSE, "100%", "140px", null, null, null, true);
        DmClient.setStyle(flwCurrency, "100%", "140px", null, null, null, true);
        DmClient.setStyle(mainFlwPanel, "100%", "140px", null, null, null, true);

        flwCC.add(CC());
        mainFlwPanel.add(flwCC);

        flwWallet.add(Wallet());
        mainFlwPanel.add(flwWallet);

        flwPayment.add(Payment());
        mainFlwPanel.add(flwPayment);

        flwBank.add(Bank());
        mainFlwPanel.add(flwBank);

        flwDigital.add(Digital());
        mainFlwPanel.add(flwDigital);

        flwCurrency.add(Currency());
        mainFlwPanel.add(flwCurrency);

        flwELSE.add(ELSE());
        mainFlwPanel.add(flwELSE);

        flwTable.add(mainFlwPanel);

        return flwTable;
    }

    private FlowPanel Wallet() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "97%", "140px", null, null, DmPosition.CENTER, true);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Wallet No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtWalletNo));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Card Holder Name");
        lbl.getElement().getStyle().setMarginBottom(2, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtCardHolderNameWT));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Authorization");
        lbl.getElement().getStyle().setMarginBottom(2, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAuthorizationWT));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel Digital() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "97%", "140px", null, null, DmPosition.CENTER, true);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Transaction  No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtTransactionNoDG));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel Payment() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "97%", "145px", null, null, DmPosition.CENTER, true);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Mobile No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMobileNo));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Mode");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPaymentMode));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginTop(4, Style.Unit.PCT);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "102px", "20px", "left", null, DmPosition.NONE, false);
        btnMakePayment.addStyleName("dm-button-grad");
        btnMakePayment.setWidth("100px");
        btnMakePayment.setHeight("20px");
        btnMakePayment.getElement().setAttribute("style", btnMakePayment.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnMakePayment.setTitle("Make Payment");
        flwMakePayment.add(btnMakePayment);
        flwTd.add(flwMakePayment);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100px", "20px", "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(4, Style.Unit.PCT);
        btnCancelPayment.addStyleName("dm-button-grad");
        btnCancelPayment.setWidth("100px");
        btnCancelPayment.setHeight("20px");
        btnCancelPayment.getElement().setAttribute("style", btnCancelPayment.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnCancelPayment.setTitle("Cancel Payment");
        flwCancel.add(btnCancelPayment);
        flwTd.add(flwCancel);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
//        flwTr.getElement().getStyle().setMarginTop(4, Style.Unit.PCT);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "48%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(lblProcessing);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(spanCountDown);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginTop(4, Style.Unit.PCT);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "102px", "20px", "left", null, DmPosition.NONE, false);
        btnCheckStatus.addStyleName("dm-button-grad");
        btnCheckStatus.setWidth("100px");
        btnCheckStatus.setHeight("20px");
        btnCheckStatus.getElement().setAttribute("style", btnCheckStatus.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnCheckStatus.setTitle("Check Status");
//        flwCheckStatus.add(btnCheckStatus);
        flwTd.add(btnCheckStatus);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel CC() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "97%", "140px", null, null, DmPosition.CENTER, true);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Card No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtCardNoCC));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Card Holder Name");
        lbl.getElement().getStyle().setMarginBottom(2, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtCardHolderNameCC));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Authorization");
        lbl.getElement().getStyle().setMarginBottom(2, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAuthorizationCC));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel Currency() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "97%", "140px", null, null, DmPosition.CENTER, true);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(lblForeignCurrency);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtForeignCurrencyAmount));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Conversion Rate");
        lbl.getElement().getStyle().setMarginBottom(2, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtConversionRate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        lbl.getElement().getStyle().setMarginBottom(2, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtCurrencyRemark));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel Bank() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "97%", "140px", null, null, DmPosition.CENTER, true);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Instrument No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtInstrumentNoBK));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Favour of");
        lbl.getElement().getStyle().setMarginBottom(2, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtFavourOf));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        lbl.getElement().getStyle().setMarginBottom(2, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRemarkBK));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Date");
        lbl.getElement().getStyle().setMarginBottom(2, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDate));
        flwTr.add(flwTd);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100px", "20px", "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(4, Style.Unit.PCT);
        btnChequePrint.addStyleName("dm-button-grad");
        btnChequePrint.setWidth("100px");
        btnChequePrint.setHeight("20px");
        btnChequePrint.getElement().setAttribute("style", btnChequePrint.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnChequePrint.setTitle("Cancel Payment");
        flwTd.add(btnChequePrint);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        
        

        return flwTable;
    }

    private FlowPanel ELSE() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "97%", "140px", null, null, DmPosition.CENTER, true);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRemark));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

}
