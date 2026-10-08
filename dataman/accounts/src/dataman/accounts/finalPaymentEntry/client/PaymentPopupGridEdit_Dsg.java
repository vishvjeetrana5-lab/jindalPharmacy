package dataman.accounts.finalPaymentEntry.client;

import dataman.accounts.finalPaymentEntry.shared.GrdPayment_Mdl;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmToolkit.dmCheckBox.client.DmCheckBox;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class PaymentPopupGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();
    final DmTextBox txtCardType = new DmTextBox();
    final DmTextBox txtCardTypeShortName = new DmTextBox();
    final DmTextBoxNumeric txtAmount = new DmTextBoxNumeric(2, true, 12, true);
    final DmTextBoxNumeric txtNetAmt = new DmTextBoxNumeric(2, true, 12, true);
    final DmTextBoxNumeric txtTotalTds = new DmTextBoxNumeric(2, true, 12, true);
    final DmTextBoxNumeric txtTdsPer = new DmTextBoxNumeric(2, false, 3, false);
    final DmTextBox txtBalance = new DmTextBox();
    final DmTextBox txtAcName = new DmTextBox();
    final FlowPanel flwBank = new FlowPanel();
    final DmRemarkTextBox txtFavourOf = new DmRemarkTextBox(200, false);
    final FlowPanel flwAccount = new FlowPanel();
    final FlowPanel flwAccountTextHelp = new FlowPanel();
    //  final FlowPanel flwAccountTextBox = new FlowPanel();
    final DmLabel lblTDS = new DmLabel("");
    final FlowPanel flwChk = new FlowPanel();
    final DmCheckBox chk = new DmCheckBox();

    /*=========================< BANK  >============================*/
    final DmTextBox txtInstrumentNoBK = new DmTextBox(20, false);
    final DmRemarkTextBox txtRemark = new DmRemarkTextBox(100, false);
    public final DmTextBox txtInsDate = new DmTextBox(11, false);
    //   final DmTextBoxNumeric txtTdsOnAmount = new DmTextBoxNumeric(2, false, 15, false);

    /*=========================< ELSE >============================*/
    final FlowPanel mainFlwPanel = new FlowPanel();

    // final DmLabel lblTotalTds = new DmLabel("");
    //<editor-fold defaultstate="collapsed" desc="TxtHPaymnetType">
    interface TxtHPaymentType_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdPayment_Mdl> {

        @Editor.Path("CardTypeName")
        ValueProvider<GrdPayment_Mdl, String> cardTypeName();

        @Editor.Path("CardTypeNatureName")
        ValueProvider<GrdPayment_Mdl, String> cardTypeNatureName();
    }
    final DmTextHelp<GrdPayment_Mdl, TxtHPaymentType_PrpAcs> txtHPaymentMode = new DmTextHelp(GWT.create(TxtHPaymentType_PrpAcs.class), 6, true);

    private void iniTxtHCardPayment() {
        txtHPaymentMode.getDmGrid().addTextColumn(txtHPaymentMode.getDmGrid().getPropertyAccess().name(), "Payment Mode", 150, "cardTypeName");
        txtHPaymentMode.getDmGrid().addTextColumn(txtHPaymentMode.getDmGrid().getPropertyAccess().cardTypeNatureName(), "Card Type", 140, "cardType");
        txtHPaymentMode.getDmGrid().initializeGrid(txtHPaymentMode.getDmGrid().getPropertyAccess().fieldHashCode(), 325, 0, 1);
        txtHPaymentMode.getHelper().addSort("cardTypeName", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHAccount">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHAccount = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniTxtHAccount() {
        txtHAccount.getDmGrid().addTextColumn(txtHAccount.getDmGrid().getPropertyAccess().name(), "Name", 150, "name");
        txtHAccount.getDmGrid().initializeGrid(txtHAccount.getDmGrid().getPropertyAccess().code(), 325, 0, 1);
        txtHAccount.getHelper().addSort("name", DmEnum.DmSortType.ASC);
        txtHAccount.getHelper().addSort("nature", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    public PaymentPopupGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        iniTxtHCardPayment();
        iniTxtHAccount();
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "190px", null, null, DmEnum.DmPosition.CENTER, true);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "190px", "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "55%", "190px", "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(leftArea());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "45%", "190px", "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(rightArea());
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel leftArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "97%", "190px", null, null, DmEnum.DmPosition.CENTER, true);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Payment Mode");
        lbl.setTitle("Payment Mode - PM");
        lbl.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPaymentMode));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtCardType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "24%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtCardTypeShortName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Amount");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Balance");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtAmount));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtBalance));
        txtBalance.setEnabled(false);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("A/c Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        DmClient.setStyle(flwAccount, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        //  DmClient.setStyle(flwAccountTextBox, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        DmClient.setStyle(flwAccountTextHelp, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        // flwAccountTextBox.add(vl.add(txtAcName));
        flwAccountTextHelp.add(vl.add(txtHAccount));
        flwAccount.add(flwAccountTextHelp);
        // txtAcName.setEnabled(false);
        txtHAccount.setEnabled(true);
        flwTr.add(flwAccount);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTDS.setText("Tds Deduction");
        lblTDS.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        lblTDS.getElement().getStyle().setWidth(100, Style.Unit.PCT);
        lblTDS.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        lblTDS.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
        lblTDS.getElement().getStyle().setColor("blue");
        flwTd.add(lblTDS);
        flwTr.add(flwTd);

//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
//        lbl = new DmLabel("(%)");
//        flwTd.add(lbl);
//        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Net Pay");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwChk.getElement().setAttribute("style", "min-width:141px;padding:4px;height:13px;");
        chk.getElement().setAttribute("style", "margin-right:6px;margin-top:-1px");
        flwChk.add(chk);

        flwTd.add(flwChk);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtTotalTds));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtTdsPer));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "44%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtNetAmt));
        txtNetAmt.setEnabled(false);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel rightArea() {
        FlowPanel flwTable;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "190px", null, null, DmEnum.DmPosition.CENTER, true);
        DmClient.setStyle(flwBank, "100%", "190px", null, null, null, true);
        DmClient.setStyle(mainFlwPanel, "100%", "150px", null, null, null, true);

        flwBank.add(bank());
        mainFlwPanel.add(flwBank);

        flwTable.add(mainFlwPanel);

        return flwTable;
    }

    private FlowPanel bank() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "97%", "190px", null, null, DmEnum.DmPosition.CENTER, true);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Instrument No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtInstrumentNoBK));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        lbl.getElement().getStyle().setMarginBottom(2, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtRemark));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Date");
        lbl.getElement().getStyle().setMarginBottom(2, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtInsDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Favour Of");
        lbl.getElement().getStyle().setMarginBottom(2, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtFavourOf));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        return flwTable;
    }

}
