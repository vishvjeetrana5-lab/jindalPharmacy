package dataman.accounts.gstVoucherEntry.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.accounts.gstVoucherEntry.shared.TxtHAccountNameGrid_Mdl;
import dataman.accounts.gstVoucherEntry.shared.TxtHHSN_Mdl;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmEnum.DmSortType;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

abstract class AcDetailsGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final DmTextBox txtHSNSAC = new DmTextBox();
    final DmTextBoxNumeric txtGrossAmount = new DmTextBoxNumeric(2, false, 10, true);
    final DmTextBoxNumeric txtTaxableAmount = new DmTextBoxNumeric(2, false, 10, true);
    final DmTextBoxNumeric txtSGSUGSTTPer = new DmTextBoxNumeric(2, false, 5, true);
    final DmTextBoxNumeric txtSGSTUGSTAmount = new DmTextBoxNumeric(2, false, 5, true);
    final DmTextBoxNumeric txtCGSTTPer = new DmTextBoxNumeric(2, false, 5, true);
    final DmTextBoxNumeric txtCGSTAmount = new DmTextBoxNumeric(2, false, 5, true);
    final DmTextBoxNumeric txtIGSTTPer = new DmTextBoxNumeric(2, false, 5, true);
    final DmTextBoxNumeric txtIGSTAmount = new DmTextBoxNumeric(2, false, 5, true);
    final DmTextBoxNumeric txtNetAmount = new DmTextBoxNumeric(2, false, 10, true);

    final DmLabel lblSGSTUGST = new DmLabel("");

    //<editor-fold defaultstate="collapsed" desc="TxtHAccountNameGrid">
    interface TxtHAccount_PrpAcs extends KeyField_T_PrpAcs<TxtHAccountNameGrid_Mdl> {

        @Editor.Path("City")
        ValueProvider<TxtHAccountNameGrid_Mdl, String> City();

        @Editor.Path("SCode")
        ValueProvider<TxtHAccountNameGrid_Mdl, String> SCode();

        @Editor.Path("Nature")
        ValueProvider<TxtHAccountNameGrid_Mdl, String> Nature();

        @Editor.Path("Group")
        ValueProvider<TxtHAccountNameGrid_Mdl, String> Group();
    }

    final DmTextHelp<TxtHAccountNameGrid_Mdl, TxtHAccount_PrpAcs> txtHAccountName = new DmTextHelp(GWT.create(TxtHAccount_PrpAcs.class), 7, true);

    private void iniTxtHAccountNameGrid() {
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().City(), "City", 100, "CityName");
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().Nature(), "Nature", 100, "Nature");
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().Group(), "Group", 150, "GroupName");
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().SCode(), "Code", 100, "ManualCode");
        txtHAccountName.getDmGrid().initializeGrid(txtHAccountName.getDmGrid().getPropertyAccess().code(), 700, 0, 1);
        txtHAccountName.getHelper().addSort("Name", DmSortType.ASC);
        txtHAccountName.getHelper().addSort("CityName", DmSortType.ASC);
        txtHAccountName.getHelper().addSort("Nature", DmSortType.ASC);
    }
//</editor-fold>   

    //<editor-fold defaultstate="collapsed" desc="TxtHHSN">
    interface TxtHHSN_PrpAcs extends KeyField_T_PrpAcs<TxtHHSN_Mdl> {

        @Editor.Path("Hsn")
        ValueProvider<TxtHHSN_Mdl, String> hsn();

        @Editor.Path("ItemGSTNatureText")
        ValueProvider<TxtHHSN_Mdl, String> gstNature();
    }
    final DmTextHelp<TxtHHSN_Mdl, TxtHHSN_PrpAcs> txtHHSN = new DmTextHelp(GWT.create(TxtHHSN_PrpAcs.class), 6, false);

    private void iniTxtHHSN() {
        txtHHSN.getDmGrid().addTextColumn(txtHHSN.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHHSN.getDmGrid().addTextColumn(txtHHSN.getDmGrid().getPropertyAccess().hsn(), "HSN / SAC", 80, "hsn");
        txtHHSN.getDmGrid().addTextColumn(txtHHSN.getDmGrid().getPropertyAccess().gstNature(), "GST Nature", 80, "");
        txtHHSN.getDmGrid().initializeGrid(txtHHSN.getDmGrid().getPropertyAccess().code(), 350, 0, 1);
        txtHHSN.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="TxtHCgstAccount">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHCgstAccount = new DmTextHelp(com.google.gwt.core.shared.GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniTxtHCgstAccount() {
        txtHCgstAccount.getDmGrid().addTextColumn(txtHCgstAccount.getDmGrid().getPropertyAccess().name(), "Name", 150, "name");
        txtHCgstAccount.getDmGrid().initializeGrid(txtHCgstAccount.getDmGrid().getPropertyAccess().code(), 325, 0, 1);
        txtHCgstAccount.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="TxtHSgstAccount">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHSgstAccount = new DmTextHelp(com.google.gwt.core.shared.GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniTxtHSgstAccount() {
        txtHSgstAccount.getDmGrid().addTextColumn(txtHSgstAccount.getDmGrid().getPropertyAccess().name(), "Name", 150, "name");
        txtHSgstAccount.getDmGrid().initializeGrid(txtHSgstAccount.getDmGrid().getPropertyAccess().code(), 325, 0, 1);
        txtHSgstAccount.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="TxtHSgstAccount">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHIgstAccount = new DmTextHelp(com.google.gwt.core.shared.GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniTxtHIgstAccount() {
        txtHIgstAccount.getDmGrid().addTextColumn(txtHIgstAccount.getDmGrid().getPropertyAccess().name(), "Name", 150, "name");
        txtHIgstAccount.getDmGrid().initializeGrid(txtHIgstAccount.getDmGrid().getPropertyAccess().code(), 325, 0, 1);
        txtHIgstAccount.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    final DmTextBox txtItemAdjustment = new DmTextBox();
    final DmTextBoxNumeric txtAmount = new DmTextBoxNumeric(2, false, 10, true);
    final DmTextBoxNumeric txtDebit = new DmTextBoxNumeric(2, false, 10, true);
    final DmTextBoxNumeric txtCredit = new DmTextBoxNumeric(2, false, 10, true);
    final DmRemarkTextBox txtNarration = new DmRemarkTextBox();

    public AcDetailsGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        ini();
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private void ini() {
        iniTxtHAccountNameGrid();
        iniTxtHHSN();
        iniTxtHIgstAccount();
        iniTxtHSgstAccount();
        iniTxtHCgstAccount();
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lblTemp;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTable.getElement().getStyle().setPadding(2, Style.Unit.PX);
        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("A/c Name");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< 2nd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHAccountName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
//====================================< 3rd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "55%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("HSN Name");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("HSN/SAC");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Taxable Amount");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

//====================================< 4th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "54%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHHSN));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "22%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(txtHSNSAC);
        txtHSNSAC.setEnabled(false);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtTaxableAmount));
        txtTaxableAmount.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
//====================================< 5th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "48%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(lblSGSTUGST);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "48%", null, "right", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("CGST%");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< 6th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "48%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtSGSUGSTTPer));
        flwTd.add(txtSGSTUGSTAmount);
        txtSGSTUGSTAmount.setEnabled(false);
        txtSGSUGSTTPer.getElement().getStyle().setWidth(35, Style.Unit.PCT);
        txtSGSTUGSTAmount.getElement().getStyle().setWidth(62, Style.Unit.PCT);
        txtSGSUGSTTPer.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        txtSGSTUGSTAmount.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "48%", null, "right", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtCGSTTPer));
        flwTd.add(txtCGSTAmount);
        txtCGSTAmount.setEnabled(false);
        txtCGSTTPer.getElement().getStyle().setWidth(35, Style.Unit.PCT);
        txtCGSTAmount.getElement().getStyle().setWidth(62, Style.Unit.PCT);
        txtCGSTTPer.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        txtCGSTAmount.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        
        //====================================< 7th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "48%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("A/c Name");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "48%", null, "right", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("A/c Name");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        
        //====================================< 8th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "48%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSgstAccount));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "48%", null, "right", null, DmEnum.DmPosition.NONE, false);
        txtItemAdjustment.setReadOnly(true);
        flwTd.add(vl.add(txtHCgstAccount));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< 9th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "48%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("IGST%");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);
        
        flwTable.add(flwTr);
        //====================================< 10th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "48%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtIGSTTPer));
        flwTd.add(txtIGSTAmount);
        txtIGSTAmount.setEnabled(false);
        txtIGSTTPer.getElement().getStyle().setWidth(35, Style.Unit.PCT);
        txtIGSTAmount.getElement().getStyle().setWidth(62, Style.Unit.PCT);
        txtIGSTTPer.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        txtIGSTAmount.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< 11th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "48%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("A/c Name");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);
        
        flwTable.add(flwTr);
        //====================================< 12th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "48%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHIgstAccount));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< 14th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "48%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Amount");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "48%", null, "right", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Adjustment");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< 15th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "48%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtAmount));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "48%", null, "right", null, DmEnum.DmPosition.NONE, false);
        txtItemAdjustment.setReadOnly(true);
        flwTd.add(vl.add(txtItemAdjustment));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< 16th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Narration");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< 17th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtNarration));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

}
