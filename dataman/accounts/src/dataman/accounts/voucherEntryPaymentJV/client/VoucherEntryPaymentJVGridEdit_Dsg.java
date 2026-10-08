package dataman.accounts.voucherEntryPaymentJV.client;

import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.accounts.voucherEntryPaymentJV.shared.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class VoucherEntryPaymentJVGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();
    final FlowPanel flwDebitLabel = new FlowPanel();
    final FlowPanel flwDebitText = new FlowPanel();
    final FlowPanel flwCreditLabel = new FlowPanel();
    final FlowPanel flwCreditText = new FlowPanel();
    final FlowPanel flwChequeDetParent = new FlowPanel();
    final FlowPanel flwChequeDetChild = new FlowPanel();
    final DmTextBox txtACManualCode = new DmTextBox();
    final DmTextBox txtTDS = new DmTextBox();
    final DmLabel lblCredit = new DmLabel("Credit");
    final DmLabel lblDebit = new DmLabel("Debit");
    final DmLabel lblLedgerBalance = new DmLabel("BAL Dr 0.00");
    final DmLabel lblYtdDr = new DmLabel("YTD Dr 0.00");
    final DmLabel lblYtdCr = new DmLabel("YTD Cr 0.00");
    final DmLabel lblYtdTdsDr = new DmLabel("TDS Dr 0.00");
    final DmLabel lblYtdTdsCr = new DmLabel("TDS Cr 0.00");

    final DmLabel lblStripChqDetail = new DmLabel("");
    final DmLabel lblStripTDSDetail = new DmLabel("");

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

//    final DmTextBox txtChequeNo = new DmTextBox(20, false);
    final DmTextBox txtTDSAmt = new DmTextBox(10, false);
    final DmTextBox txtCostCenterAmt = new DmTextBox(10, false);
    final DmTextBox txtAdjustment = new DmTextBox();
    final DmTextBox txtItemAdjustment = new DmTextBox();

    final DmTextBoxNumeric txtDebit = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 15, true);
    final DmTextBoxNumeric txtCredit = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 15, true);

    //=====================<Cheque Detail>============================//
    final DmTextBox txtChequeNo = new DmTextBox(20, false);
    final DmTextBox txtChequeDate = new DmTextBox();
    final DmTextBox txtFavourOF = new DmTextBox(50, false);
    final DmTextBox txtClearDate = new DmTextBox();
    final DmTextBox txtBank = new DmTextBox(35, false);
    final DmTextBox txtBranch = new DmTextBox(35, false);
    final DmRemarkTextBox txtBranchAddress = new DmRemarkTextBox(250, false);
    final DmTextBox txtIFSCCode = new DmTextBox(11, false);

    //=====================<TDS Detail>============================//
    final DmTextBoxNumeric txtTdsAmountProposed = new DmTextBoxNumeric(2, false, 15, false);
    final DmTextBoxNumeric txtTdsOnAmount = new DmTextBoxNumeric(2, false, 15, true);
    final DmLabel lblTotalTds = new DmLabel("");

    public VoucherEntryPaymentJVGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        iniTxtHAccountNameGrid();
        iniTxtHYesNo();
        iniTxtHAccountNo();
        iniTxtHTdsDeduct();
        iniTxtHTdsCategory();
        iniTxtHTdsFor();
        container.add(section1());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel section1() {
        FlowPanel flwTable, flwTR, flwTD, flwTDTmp;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "500px", null, null, DmPosition.NONE, true);

        //A/c Code
        flwTR = new FlowPanel();
        flwTR.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, true);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("A/c Code");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "15%", null, "left", null, DmPosition.NONE, false);
        txtACManualCode.setEnabled(false);
        flwTD.add(vl.add(txtACManualCode));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("A/c Name");
        flwTD.getElement().getStyle().setMarginLeft(4, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "51%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHAccountName));
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        ////////////////////////////////
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "98%", null, "left", null, DmPosition.NONE, true);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(lblLedgerBalance));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(lblYtdDr));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(lblYtdCr));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(lblYtdTdsDr));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(lblYtdTdsCr));
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        //Narration
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, true);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Narration");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "85%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtNarration));
        flwTR.add(flwTD);

        flwTable.add(flwTR);
        ////////////////////////////////
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, true);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "15%", null, "left", null, DmPosition.NONE, false);
        flwDebitLabel.add(lblDebit);
        flwTD.add(flwDebitLabel);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        flwDebitText.add(vl.add(txtDebit));
//        flwTD.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        flwTD.add(vl.add(flwDebitText));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "12%", null, "left", null, DmPosition.NONE, false);
        flwTD.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        flwCreditLabel.add(lblCredit);
        flwTD.add(flwCreditLabel);
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        flwTD = new FlowPanel();

        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
//        flwTD.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        flwCreditText.add(vl.add(txtCredit));
        flwTD.add(vl.add(flwCreditText));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        //////////////////////////////////////////////////////////////////////////////////////////
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, true);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "15%", null, "left", null, DmPosition.NONE, false);
        lblCostCenterAmt.setText("Cost Center");
        lblCostCenterAmt.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        lblCostCenterAmt.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
        lblCostCenterAmt.getElement().getStyle().setColor("blue");
        flwTD.add(lblCostCenterAmt);
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "10%", null, "left", null, DmPosition.NONE, false);
        flwTD.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        txtCostCenterAmt.setReadOnly(true);
        flwTD.add(vl.add(txtCostCenterAmt));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "12%", null, "left", null, DmPosition.NONE, false);
        flwTD.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        lblAdjustment.setText("Account Adj.");
        lblAdjustment.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        lblAdjustment.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
        lblAdjustment.getElement().getStyle().setColor("blue");
        flwTD.add(lblAdjustment);
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "10%", null, "left", null, DmPosition.NONE, false);
        txtAdjustment.setReadOnly(true);
        flwTD.add(vl.add(txtAdjustment));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "12%", null, "left", null, DmPosition.NONE, false);
        flwTD.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        lblItemAdjustment.setText("Item Adj.");
        lblItemAdjustment.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        lblItemAdjustment.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
        lblItemAdjustment.getElement().getStyle().setColor("blue");
        flwTD.add(lblItemAdjustment);
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "10%", null, "left", null, DmPosition.NONE, false);
        flwTD.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        txtItemAdjustment.setReadOnly(true);
        flwTD.add(vl.add(txtItemAdjustment));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", "", "left", null, DmEnum.DmPosition.NONE, true);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "100%", "", "left", null, DmEnum.DmPosition.NONE, true);

        flwChequeDetChild.add(vl.add(chqDetailArea()));
        flwChequeDetParent.add(flwChequeDetChild);
        flwTD.add(vl.add(flwChequeDetParent));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", "20px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTR.addStyleName("dm-StripLabel");
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("TDS Detail :");
        flwTD.add(lbl);
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", "20px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "100%", "", "left", null, DmEnum.DmPosition.NONE, true);
        flwTD.add(fCreateTdsCategoryDetails());
        flwTR.add(flwTD);
        flwTable.add(flwTR);
        return flwTable;
    }

    private FlowPanel chqDetailArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "", "left", null, DmEnum.DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "20px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.addStyleName("dm-StripLabel");
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Cheque Detail :");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        flwTr.getElement().getStyle().setMarginTop(1, Style.Unit.PX);
        DmClient.setStyle(flwTr, "100%", "", "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", "", "left", null, DmPosition.NONE, true);
        lbl = new DmLabel("Cheque No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", "", "left", null, DmPosition.NONE, true);
        flwTd.add(vl.add(txtChequeNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", "", "left", null, DmPosition.NONE, true);
        lbl = new DmLabel("Date");
        lbl.getElement().getStyle().setMarginLeft(45, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", "", "left", null, DmPosition.NONE, true);
        flwTd.add(vl.add(txtChequeDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", "", "left", null, DmPosition.NONE, true);
        lbl = new DmLabel("Clear Dt.");
        lbl.getElement().getStyle().setMarginLeft(50, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", "", "left", null, DmPosition.NONE, true);
        flwTd.add(vl.add(txtClearDate));
        txtClearDate.getElement().getStyle().setMarginLeft(47, Style.Unit.PX);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================<>=======================================//
        flwTr = new FlowPanel();
        flwTr.getElement().getStyle().setMarginTop(1, Style.Unit.PX);
        DmClient.setStyle(flwTr, "100%", "", "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        
        DmClient.setStyle(flwTd, "15%", "", "left", null, DmPosition.NONE, true);
        lbl = new DmLabel("Favour Of");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "72%", "", "left", null, DmPosition.NONE, true);
        flwTd.add(vl.add(txtFavourOF));
        flwTr.add(flwTd);

//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "8%", "", "left", null, DmPosition.NONE, true);
//        lbl = new DmLabel("A/c No. ");
//        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
//        flwTd.add(lbl);
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "40.5%", "", "left", null, DmPosition.NONE, true);
//        flwTd.add(vl.add(txtHAccountNo));
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "12%", "", "left", null, DmPosition.NONE, true);
//        lbl = new DmLabel("Cross Cheque");
//        lbl.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
//        flwTd.add(lbl);
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "14%", "", "left", null, DmPosition.NONE, true);
//        flwTd.add(vl.add(txtHIsCrossCheque));
//        flwTr.add(flwTd);
//        //====================================<>=======================================//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "10%", "", "left", null, DmPosition.NONE, true);
//        lbl = new DmLabel("Bank");
//        flwTd.add(lbl);
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "40%", "", "left", null, DmPosition.NONE, true);
//        flwTd.add(vl.add(txtBank));
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "10%", "", "left", null, DmPosition.NONE, true);
//        lbl = new DmLabel("Branch ");
//        lbl.getElement().getStyle().setMarginLeft(17, Style.Unit.PCT);
//        flwTd.add(lbl);
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "40%", "", "left", null, DmPosition.NONE, true);
//        flwTd.add(vl.add(txtBranch));
//        flwTr.add(flwTd);
//        //====================================<>=======================================//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "10%", "", "left", null, DmPosition.NONE, true);
//        lbl = new DmLabel("Branch Add.");
//        flwTd.add(lbl);
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "60%", "", "left", null, DmPosition.NONE, true);
//        flwTd.add(vl.add(txtBranchAddress));
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "15%", "", "left", null, DmPosition.NONE, true);
//        lbl = new DmLabel("IFSC");
//        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
//        flwTd.add(lbl);
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "15%", "", "left", null, DmPosition.NONE, true);
//        flwTd.add(vl.add(txtIFSCCode));
//        flwTr.add(flwTd);

        flwTable.add(flwTr);
        return flwTable;
    }

    ;
    
    //<editor-fold defaultstate="collapsed" desc="txtHAccountNo">

    interface TxtHAccountNo_PrpAcs extends KeyFieldHashCode_T_PrpAcs<TxtHAccountNo_Mdl> {

        @Editor.Path("Bank")
        ValueProvider<TxtHAccountNo_Mdl, String> bank();

        @Editor.Path("Branch")
        ValueProvider<TxtHAccountNo_Mdl, String> branch();

        @Editor.Path("IfscCode")
        ValueProvider<TxtHAccountNo_Mdl, String> ifscCode();

        @Editor.Path("SwiftNo")
        ValueProvider<TxtHAccountNo_Mdl, String> swiftNo();

    }

    final DmTextHelp<TxtHAccountNo_Mdl, TxtHAccountNo_PrpAcs> txtHAccountNo = new DmTextHelp(GWT.create(TxtHAccountNo_PrpAcs.class), 7, false);

    public void iniTxtHAccountNo() {
        txtHAccountNo.getDmGrid().addTextColumn(txtHAccountNo.getDmGrid().getPropertyAccess().name(), "Account No.", 1, "accountNo");
        txtHAccountNo.getDmGrid().addTextColumn(txtHAccountNo.getDmGrid().getPropertyAccess().bank(), "Bank", 130, "bank");
        txtHAccountNo.getDmGrid().addTextColumn(txtHAccountNo.getDmGrid().getPropertyAccess().branch(), "Branch", 100, "branch");
        txtHAccountNo.getDmGrid().addTextColumn(txtHAccountNo.getDmGrid().getPropertyAccess().ifscCode(), "IFSC Code", 100, "ifscCode");
        txtHAccountNo.getDmGrid().addTextColumn(txtHAccountNo.getDmGrid().getPropertyAccess().swiftNo(), "Swift No.", 100, "swiftNo");
        txtHAccountNo.getDmGrid().initializeGrid(txtHAccountNo.getDmGrid().getPropertyAccess().fieldHashCode(), 600, 0, 1);
        txtHAccountNo.getHelper().addSort("bank", DmSortType.ASC);

    }
//</editor-fold>    
    //<editor-fold defaultstate="collapsed" desc="txtHIsCrossCheque">
    DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHIsCrossCheque = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true);

    private void iniTxtHYesNo() {
        txtHIsCrossCheque.getDmGrid().addTextColumn(txtHIsCrossCheque.getDmGrid().getPropertyAccess().name(), "Name", 200, "");
        txtHIsCrossCheque.getDmGrid().initializeGrid(txtHIsCrossCheque.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHIsCrossCheque.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHTdsCategory">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHTdsCategory = new DmTextHelp(com.google.gwt.core.shared.GWT.create(KeyField_PrpAcs.class), 7, true);

    public void iniTxtHTdsCategory() {
        txtHTdsCategory.getDmGrid().addTextColumn(txtHTdsCategory.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHTdsCategory.getDmGrid().initializeGrid(txtHTdsCategory.getDmGrid().getPropertyAccess().code(), 200, 200, 1);
        txtHTdsCategory.getDmGrid().getGrid().setHeight(200);
        txtHTdsCategory.getHelper().addSort("Name", DmSortType.ASC);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHTdsDeduct">
    interface TxtHTdsDeduct extends KeyField_T_PrpAcs<TxtHTdsDeduct_Mdl> {

        @Editor.Path("City")
        ValueProvider<TxtHTdsDeduct_Mdl, String> City();

        @Editor.Path("SCode")
        ValueProvider<TxtHTdsDeduct_Mdl, String> SCode();

    }
    final DmTextHelp<TxtHTdsDeduct_Mdl, TxtHTdsDeduct> txtHTdsDeduct = new DmTextHelp(com.google.gwt.core.shared.GWT.create(TxtHTdsDeduct.class), 7, true);

    public void iniTxtHTdsDeduct() {
        txtHTdsDeduct.getDmGrid().addTextColumn(txtHTdsDeduct.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHTdsDeduct.getDmGrid().addTextColumn(txtHTdsDeduct.getDmGrid().getPropertyAccess().City(), "City", 100, "CityName");
        txtHTdsDeduct.getDmGrid().addTextColumn(txtHTdsDeduct.getDmGrid().getPropertyAccess().SCode(), "Code", 100, "ManualCode");
        txtHTdsDeduct.getDmGrid().initializeGrid(txtHTdsDeduct.getDmGrid().getPropertyAccess().code(), 400, 200, 1);
        txtHTdsDeduct.getDmGrid().getGrid().setHeight(200);
        txtHTdsDeduct.getHelper().addSort("Name", DmSortType.ASC);
        txtHTdsDeduct.getHelper().addSort("CityName", DmSortType.ASC);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHTdsFor">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHTdsFor = new DmTextHelp(com.google.gwt.core.shared.GWT.create(KeyField_PrpAcs.class), 7, true);

    public void iniTxtHTdsFor() {
        txtHTdsFor.getDmGrid().addTextColumn(txtHTdsFor.getDmGrid().getPropertyAccess().name(), "Name", 150, "Name");
        txtHTdsFor.getDmGrid().initializeGrid(txtHTdsFor.getDmGrid().getPropertyAccess().code(), 150, 200, 1);
        txtHTdsFor.getDmGrid().getGrid().setHeight(150);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TdsCategoryGrid">
    interface TdsCategoryGrid extends KeyFieldHashCode_T_PrpAcs<TdsCategoryGrid_Mdl> {

        @Editor.Path("Description")
        ValueProvider<TdsCategoryGrid_Mdl, String> description();

        @Editor.Path("PostingAccount")
        ValueProvider<TdsCategoryGrid_Mdl, String> postingAccount();

        @Editor.Path("Percent")
        ValueProvider<TdsCategoryGrid_Mdl, String> percent();

        @Editor.Path("Amount")
        ValueProvider<TdsCategoryGrid_Mdl, String> amount();
    }

    final DmGrid<TdsCategoryGrid_Mdl, TdsCategoryGrid> tdsCategoryGrid = new DmGrid(com.google.gwt.core.shared.GWT.create(TdsCategoryGrid.class));

    public Grid iniTdsCategoryGrid() {
        tdsCategoryGrid.addRowNumberColumn();
        tdsCategoryGrid.addTextColumn(tdsCategoryGrid.getPropertyAccess().description(), "Description", 1, null);
        tdsCategoryGrid.addTextColumn(tdsCategoryGrid.getPropertyAccess().postingAccount(), "Posting A/c", 200, null);
        tdsCategoryGrid.addTextColumn(tdsCategoryGrid.getPropertyAccess().percent(), "(%)", 50, null).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        tdsCategoryGrid.addTextColumn(tdsCategoryGrid.getPropertyAccess().amount(), "Amount", 100, null).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        tdsCategoryGrid.initializeGrid(tdsCategoryGrid.getPropertyAccess().fieldHashCode(), 665, 250, 2);
        tdsCategoryGrid.getGrid().setHeight(200);
        return tdsCategoryGrid.getGrid();
    }
//</editor-fold>

    private FlowPanel fCreateTdsCategoryDetails() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lblTemp;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "", null, null, DmPosition.CENTER, true);


        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "", "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", "", "left", null, DmPosition.NONE, false);
        flwTd.add(topLeftArea());
        flwTr.add(flwTd);
        /*====================================< ******* >====================================*/

 /*====================================< 2nd Row >====================================*/
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", "", "left", null, DmPosition.NONE, false);
        flwTd.add(topRightArea());
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

 /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(iniTdsCategoryGrid());
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
 /*====================================< 4rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Total TDS");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "45%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(lblTotalTds));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;

    }

    private FlowPanel topRightArea() {

        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lblTemp;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
//        flwTr = new FlowPanel();
//        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
//        lblTemp = new DmLabel("Amount");
//        lblTemp.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
//        flwTd.add(lblTemp);
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "60%", null, "left", null, DmPosition.NONE, false);
//        flwTd.add(vl.add(txtTdsAmountProposed));
//        flwTr.add(flwTd);
//
//        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "26%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("TDS On Amount");
        lblTemp.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtTdsOnAmount));
        txtTdsOnAmount.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("TDS For");
        lblTemp.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHTdsFor));
        flwTr.add(flwTd);
        /*====================================< ******* >====================================*/
        return flwTable;

    }

    private FlowPanel topLeftArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lblTemp;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "", null, null, DmPosition.CENTER, true);
        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("TDS Category");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHTdsCategory));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
// /*====================================< 2nd Row >====================================*/
//        flwTr = new FlowPanel();
//        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
//        lblTemp = new DmLabel("TDS Deduct Form");
//        flwTd.add(lblTemp);
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
//        flwTd.add(vl.add(txtHTdsDeduct));
//        flwTr.add(flwTd);
//        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
        return flwTable;
    }
}
