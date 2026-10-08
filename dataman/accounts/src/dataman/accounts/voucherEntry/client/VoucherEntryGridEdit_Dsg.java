package dataman.accounts.voucherEntry.client;

import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.accounts.voucherEntry.shared.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class VoucherEntryGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();
    final FlowPanel flwDebitLabel = new FlowPanel();
    final FlowPanel flwDebitText = new FlowPanel();
    final FlowPanel flwCreditLabel = new FlowPanel();
    final FlowPanel flwCreditText = new FlowPanel();
    final DmTextBox txtACManualCode = new DmTextBox();
    final DmTextBox txtPan = new DmTextBox();
    final DmTextBox txtTan = new DmTextBox();
    final DmTextBox txtTDS = new DmTextBox();
    final DmLabel lblCredit = new DmLabel("Credit");
    final DmLabel lblDebit = new DmLabel("Debit");
    final DmLabel lblLedgerBalance = new DmLabel("Dr 0.00");
    final DmLabel lblLedgerFyDr = new DmLabel("FY Dr 0.00");
    final DmLabel lblLedgerFyCr = new DmLabel("FY Cr 0.00");

    //<editor-fold defaultstate="collapsed" desc="TxtHAccountNameGrid">
    interface TxtHAccount_PrpAcs extends KeyField_T_PrpAcs<TxtHAccountNameGrid_Mdl> {

        @Editor.Path("City")
        ValueProvider<TxtHAccountNameGrid_Mdl, String> City();

        @Editor.Path("SCode")
        ValueProvider<TxtHAccountNameGrid_Mdl, String> SCode();

        @Editor.Path("Nature")
        ValueProvider<TxtHAccountNameGrid_Mdl, String> Nature();

        @Editor.Path("CostCenter")
        ValueProvider<TxtHAccountNameGrid_Mdl, String> CostCenter();

        @Editor.Path("Group")
        ValueProvider<TxtHAccountNameGrid_Mdl, String> Group();
    }

    final DmTextHelp<TxtHAccountNameGrid_Mdl, TxtHAccount_PrpAcs> txtHAccountName = new DmTextHelp(GWT.create(TxtHAccount_PrpAcs.class), 7, true);

    public void iniTxtHAccountNameGrid() {
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().City(), "City", 100, "CityName");
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().Nature(), "Nature", 100, "Nature");
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().CostCenter(), "Cost Center", 100, "CostCenter");
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().Group(), "Group", 150, "GroupName");
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().SCode(), "Code", 100, "ManualCode");
        txtHAccountName.getDmGrid().initializeGrid(txtHAccountName.getDmGrid().getPropertyAccess().code(), 700, 0, 1);
        txtHAccountName.getHelper().addSort("Name", DmSortType.ASC);
        txtHAccountName.getHelper().addSort("CityName", DmSortType.ASC);
        txtHAccountName.getHelper().addSort("Nature", DmSortType.ASC);
        txtHAccountName.getHelper().addSort("CostCenter", DmSortType.ASC);
    }
    //</editor-fold>    

    final DmRemarkTextBox txtNarration = new DmRemarkTextBox(200, false);

    final DmLabel lblChequeNo = new DmLabel("");
    final DmLabel lblTDSAmt = new DmLabel("");
    final DmLabel lblCostCenterAmt = new DmLabel("");
    final DmLabel lblAdjustment = new DmLabel("");
    final DmLabel lblItemAdjustment = new DmLabel("");

    final DmTextBox txtChequeNo = new DmTextBox(20, false);
    final DmTextBox txtTDSAmt = new DmTextBox(10, false);
    final DmTextBox txtCostCenterAmt = new DmTextBox(10, false);
    final DmTextBox txtAdjustment = new DmTextBox();
    final DmTextBox txtItemAdjustment = new DmTextBox();

    final DmTextBoxNumeric txtDebit = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 15, true);
    final DmTextBoxNumeric txtCredit = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 15, true);

    public VoucherEntryGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        iniTxtHAccountNameGrid();
        container.add(section1());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel section1() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "240px", null, null, DmPosition.CENTER, true);

        //A/c Code
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, true);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("A/c Code");
        flwTD.add(lbl);
        flwTR.add(flwTD);
        flwTable.add(flwTR);
        ////////////////////////////////
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, true);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "100%", null, "left", null, DmPosition.NONE, false);
        txtACManualCode.setEnabled(false);
        flwTD.add(vl.add(txtACManualCode));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        //A/c Name
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, true);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("A/c Name");
        flwTD.add(lbl);
        flwTR.add(flwTD);
        flwTable.add(flwTR);
        ////////////////////////////////
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, true);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHAccountName));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, true);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "33%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(lblLedgerBalance));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "33%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(lblLedgerFyDr));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "33%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(lblLedgerFyCr));
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        //Narration
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, true);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Narration");
        flwTD.add(lbl);
        flwTR.add(flwTD);
        flwTable.add(flwTR);
        ////////////////////////////////
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, true);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtNarration));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        //Narration
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, true);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        flwDebitLabel.add(lblDebit);
        flwTD.add(flwDebitLabel);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        flwTD.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        flwCreditLabel.add(lblCredit);
        flwTD.add(flwCreditLabel);
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        ////////////////////////////////
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, true);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        flwDebitText.add(vl.add(txtDebit));
        flwTD.add(vl.add(flwDebitText));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();

        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        flwTD.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        flwCreditText.add(vl.add(txtCredit));
        flwTD.add(vl.add(flwCreditText));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        //////////////////////////////////////////////////////////////////////////////////////////
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, true);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        lblChequeNo.setText("Cheque Detail");
        lblChequeNo.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        lblChequeNo.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
        lblChequeNo.getElement().getStyle().setColor("blue");
        flwTD.add(lblChequeNo);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        flwTD.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        lblTDSAmt.setText("TDS");
        lblTDSAmt.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        lblTDSAmt.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
        lblTDSAmt.getElement().getStyle().setColor("blue");
        flwTD.add(lblTDSAmt);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        flwTD.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        lblCostCenterAmt.setText("Cost Center");
        lblCostCenterAmt.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        lblCostCenterAmt.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
        lblCostCenterAmt.getElement().getStyle().setColor("blue");
        flwTD.add(lblCostCenterAmt);
        flwTR.add(flwTD);

        flwTable.add(flwTR);
        ////////////////////////////////
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, true);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        txtChequeNo.setReadOnly(true);
        flwTD.add(vl.add(txtChequeNo));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        flwTD.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        txtTDSAmt.setReadOnly(true);
        flwTD.add(vl.add(txtTDSAmt));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        flwTD.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        txtCostCenterAmt.setReadOnly(true);
        flwTD.add(vl.add(txtCostCenterAmt));
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        //////////////////////////////////////////////////////////////////////////////////////////
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, true);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        lblAdjustment.setText("Account Adj.");
        lblAdjustment.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        lblAdjustment.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
        lblAdjustment.getElement().getStyle().setColor("blue");
        flwTD.add(lblAdjustment);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        flwTD.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        lblItemAdjustment.setText("Item Adj.");
        lblItemAdjustment.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        lblItemAdjustment.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
        lblItemAdjustment.getElement().getStyle().setColor("blue");
        flwTD.add(lblItemAdjustment);
        flwTR.add(flwTD);

        flwTable.add(flwTR);
        //////////////////////////////////////////////////////////////////////////////////////////
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, true);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        txtAdjustment.setReadOnly(true);
        flwTD.add(vl.add(txtAdjustment));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        flwTD.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        txtItemAdjustment.setReadOnly(true);
        flwTD.add(vl.add(txtItemAdjustment));
        flwTR.add(flwTD);

        flwTable.add(flwTR);
        //////////////////////////////////////////////////////////////////////////////////////////

        return flwTable;
    }

}
