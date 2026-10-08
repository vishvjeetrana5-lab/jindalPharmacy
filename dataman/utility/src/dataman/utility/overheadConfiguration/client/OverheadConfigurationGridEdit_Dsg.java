package dataman.utility.overheadConfiguration.client;

import dataman.utility.overheadConfiguration.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextArea.client.DmTextArea;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;

public abstract class OverheadConfigurationGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final DmGrid<GrdOverhead_Mdl, OverheadConfiguration_Dsg.Grd_PrpAcs> dmGrd = new DmGrid(GWT.create(OverheadConfiguration_Dsg.Grd_PrpAcs.class));

    final FlowPanel flwForm = new FlowPanel();

    final DmTextBoxNumeric txtAtRate = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 10, false);
    final DmTextBox txtUniqueID = new DmTextBox(4, false);
    final DmTextArea txtFormula = new DmTextArea(250, false);
    final DmTextBoxNumeric txtSrlNo = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.ROUNDOFF, true, 3, true);
    final PopupPanel pnlGrid = new PopupPanel(true);

    final Button btnformula = new Button();

    //<editor-fold defaultstate="collapsed" desc="txtHNature">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHNature = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, false);

    public void iniNature() {
        txtHNature.getDmGrid().addTextColumn(txtHNature.getDmGrid().getPropertyAccess().name(), "Nature", 0, "Nature");
        txtHNature.getDmGrid().initializeGrid(txtHNature.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHAtRateSaveIn">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHAtRateSaveIn = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, false);

    public void iniAtRateSaveIn() {
        txtHAtRateSaveIn.getDmGrid().addTextColumn(txtHAtRateSaveIn.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHAtRateSaveIn.getDmGrid().initializeGrid(txtHAtRateSaveIn.getDmGrid().getPropertyAccess().code(), 100, 0, 1);

        txtHAtRateSaveIn.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHAmtSaveIn">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHAmtSaveIn = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, false);

    public void iniAmtSaveIn() {
        txtHAmtSaveIn.getDmGrid().addTextColumn(txtHAmtSaveIn.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHAmtSaveIn.getDmGrid().initializeGrid(txtHAmtSaveIn.getDmGrid().getPropertyAccess().code(), 100, 0, 1);

        txtHAmtSaveIn.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHAtRateEditable">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHAtRateEditable = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true);

    public void iniAtRateEditable() {
        txtHAtRateEditable.getDmGrid().addTextColumn(txtHAtRateEditable.getDmGrid().getPropertyAccess().name(), "Type", 0, "Type");
        txtHAtRateEditable.getDmGrid().initializeGrid(txtHAtRateEditable.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHAmtEditable">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHAmtEditable = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true);

    public void iniAmtEditable() {
        txtHAmtEditable.getDmGrid().addTextColumn(txtHAmtEditable.getDmGrid().getPropertyAccess().name(), "Type", 0, "Type");
        txtHAmtEditable.getDmGrid().initializeGrid(txtHAmtEditable.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHACEditable">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHACEditable = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true);

    public void iniACEditable() {
        txtHACEditable.getDmGrid().addTextColumn(txtHACEditable.getDmGrid().getPropertyAccess().name(), "Type", 0, "Type");
        txtHACEditable.getDmGrid().initializeGrid(txtHACEditable.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHShowFormulaValue">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHShowFormulaValue = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true);

    public void iniShowFormulaValue() {
        txtHShowFormulaValue.getDmGrid().addTextColumn(txtHShowFormulaValue.getDmGrid().getPropertyAccess().name(), "Type", 0, "Type");
        txtHShowFormulaValue.getDmGrid().initializeGrid(txtHShowFormulaValue.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHRoundOff">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHRoundOff = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, true);

    public void iniRoundOff() {
        txtHRoundOff.getDmGrid().addTextColumn(txtHRoundOff.getDmGrid().getPropertyAccess().name(), "Type", 0, "Type");
        txtHRoundOff.getDmGrid().initializeGrid(txtHRoundOff.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHAtRateMandatory">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHAtRateMandatory = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true);

    public void iniAtRateMandatory() {
        txtHAtRateMandatory.getDmGrid().addTextColumn(txtHAtRateMandatory.getDmGrid().getPropertyAccess().name(), "Type", 0, "Type");
        txtHAtRateMandatory.getDmGrid().initializeGrid(txtHAtRateMandatory.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHAmtMandatory">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHAmtMandatory = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true);

    public void iniAmtMandatory() {
        txtHAmtMandatory.getDmGrid().addTextColumn(txtHAmtMandatory.getDmGrid().getPropertyAccess().name(), "Type", 0, "Type");
        txtHAmtMandatory.getDmGrid().initializeGrid(txtHAmtMandatory.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHDrCr">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDrCr = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, false);

    public void iniDrCr() {
        txtHDrCr.getDmGrid().addTextColumn(txtHDrCr.getDmGrid().getPropertyAccess().name(), "Dr/Cr", 0, "Dr/Cr");
        txtHDrCr.getDmGrid().initializeGrid(txtHDrCr.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHOverhead">
    interface TxtHOverhead_PrpAcs extends KeyField_T_PrpAcs<TxtHOverhead_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<TxtHOverhead_Mdl, String> manualCode();

    }
    final DmTextHelp<TxtHOverhead_Mdl, TxtHOverhead_PrpAcs> txtHOverhead = new DmTextHelp(GWT.create(TxtHOverhead_PrpAcs.class), 7, true);

    private void iniOverhead() {
        txtHOverhead.getDmGrid().addTextColumn(txtHOverhead.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHOverhead.getDmGrid().addTextColumn(txtHOverhead.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "manualCode");
        txtHOverhead.getDmGrid().initializeGrid(txtHOverhead.getDmGrid().getPropertyAccess().code(), 300, 0, 1);

        txtHOverhead.getHelper().addSort("NAME", DmSortType.ASC);
        txtHOverhead.getHelper().addSort("manualCode", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHPostAC">
    interface TxtHPostAC_PrpAcs extends KeyField_T_PrpAcs<TxtHPostAC_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<TxtHPostAC_Mdl, String> manualCode();

    }
    final DmTextHelp<TxtHPostAC_Mdl, TxtHPostAC_PrpAcs> txtHPostAC = new DmTextHelp(GWT.create(TxtHPostAC_PrpAcs.class), 7);

    private void iniPostAC() {
        txtHPostAC.getDmGrid().addTextColumn(txtHPostAC.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHPostAC.getDmGrid().addTextColumn(txtHPostAC.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "groupCode");
        txtHPostAC.getDmGrid().initializeGrid(txtHPostAC.getDmGrid().getPropertyAccess().code(), 300, 0, 1);

        txtHPostAC.getHelper().addSort("NAME", DmSortType.ASC);
        txtHPostAC.getHelper().addSort("groupCode", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHContraExp">
    interface TTxtHContraExp_PrpAcs extends KeyField_T_PrpAcs<TxtHContraExp_Mdl> {

        @Editor.Path("Description")
        ValueProvider<TxtHContraExp_Mdl, String> description();

    }
    final DmTextHelp<TxtHContraExp_Mdl, TTxtHContraExp_PrpAcs> txtHContraExp = new DmTextHelp(GWT.create(TTxtHContraExp_PrpAcs.class), 7);

    private void iniContraExp() {
        txtHContraExp.getDmGrid().addTextColumn(txtHContraExp.getDmGrid().getPropertyAccess().name(), "Manual Code", 100, "manualCode");
        txtHContraExp.getDmGrid().addTextColumn(txtHContraExp.getDmGrid().getPropertyAccess().description(), "Name", 1, "NAME");
        txtHContraExp.getDmGrid().initializeGrid(txtHContraExp.getDmGrid().getPropertyAccess().code(), 300, 0, 2);

        txtHContraExp.getHelper().addSort("NAME", DmSortType.ASC);
        txtHContraExp.getHelper().addSort("manualCode", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHFormula">
    interface TxtHFormula_PrpAcs extends KeyField_T_PrpAcs<TxtHFormula_Mdl> {

        @Editor.Path("OverHeadCode")
        ValueProvider<TxtHFormula_Mdl, String> overHeadCode();

    }
    final DmGrid<TxtHFormula_Mdl, TxtHFormula_PrpAcs> grdFormula = new DmGrid(GWT.create(TxtHFormula_PrpAcs.class));

    private Grid iniFormula() {
        grdFormula.addTextColumn(grdFormula.getPropertyAccess().name(), "Overhead", 0, "");
        grdFormula.addTextColumn(grdFormula.getPropertyAccess().overHeadCode(), "Overhead Code", 100, "");
        grdFormula.initializeGrid(grdFormula.getPropertyAccess().code(), 300, 0, 1);
        return grdFormula.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHOHSetup">
    interface TxtHOHSetup_PrpAcs extends KeyField_T_PrpAcs<TxtHOHSetup_Mdl> {

        @Editor.Path("BannerName")
        ValueProvider<TxtHOHSetup_Mdl, String> bannername();

    }
    final DmTextHelpMulti<TxtHOHSetup_Mdl, TxtHOHSetup_PrpAcs> txtHOHSetup = new DmTextHelpMulti<>(GWT.create(TxtHOHSetup_PrpAcs.class), 10);

    private void iniOHSetup() {
        txtHOHSetup.getDmGrid().addTextColumn(txtHOHSetup.getDmGrid().getPropertyAccess().bannername(), "Name", 200, "name");
        txtHOHSetup.getDmGrid().addTextColumn(txtHOHSetup.getDmGrid().getPropertyAccess().name(), "Code", 100, "Code");
        txtHOHSetup.getDmGrid().initializeGrid(txtHOHSetup.getDmGrid().getPropertyAccess().code(), 300, 0, 2);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHPrintable">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHPrintable = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true);

    public void iniTxtHPrintable() {
        txtHPrintable.getDmGrid().addTextColumn(txtHPrintable.getDmGrid().getPropertyAccess().name(), "Type", 0, "Type");
        txtHPrintable.getDmGrid().initializeGrid(txtHPrintable.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHCalLValOn">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHCalLValOn = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true);

    public void iniTxtHCalLValOn() {
        txtHCalLValOn.getDmGrid().addTextColumn(txtHCalLValOn.getDmGrid().getPropertyAccess().name(), "Type", 0, "Type");
        txtHCalLValOn.getDmGrid().initializeGrid(txtHCalLValOn.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHOverHeadEditable">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHOverHeadEditable = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true);

    public void iniTxtHOverHeadEditable() {
        txtHOverHeadEditable.getDmGrid().addTextColumn(txtHOverHeadEditable.getDmGrid().getPropertyAccess().name(), "Type", 0, "Type");
        txtHOverHeadEditable.getDmGrid().initializeGrid(txtHOverHeadEditable.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHAtRateAmount">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHAtRateAmount = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true);

    public void iniTxtHAtRateAmount() {
        txtHAtRateAmount.getDmGrid().addTextColumn(txtHAtRateAmount.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHAtRateAmount.getDmGrid().initializeGrid(txtHAtRateAmount.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="txtHPartialPosting">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHPartialPosting = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true);

    public void iniTxtHPartialPosting() {
        txtHPartialPosting.getDmGrid().addTextColumn(txtHPartialPosting.getDmGrid().getPropertyAccess().name(), "Type", 0, "Type");
        txtHPartialPosting.getDmGrid().initializeGrid(txtHPartialPosting.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    public OverheadConfigurationGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        iniACEditable();
        iniAmtEditable();
        iniAmtMandatory();
        iniAmtSaveIn();
        iniAtRateEditable();
        iniAtRateMandatory();
        iniAtRateSaveIn();
        iniContraExp();
        iniDrCr();
        iniNature();
        iniOHSetup();
        iniOverhead();
        iniPostAC();
        iniRoundOff();
        iniShowFormulaValue();
        iniTxtHOverHeadEditable();
        iniTxtHPrintable();
        iniTxtHCalLValOn();
        iniTxtHAtRateAmount();
        iniTxtHPartialPosting();
        pnlGrid.add(iniFormula());
        pnlGrid.setWidth("300px");
        pnlGrid.setHeight("300px");
        pnlGrid.hide();
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "315px", null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("S.No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSrlNo));
        txtSrlNo.setWidth("60px");
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Overhead");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "45%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHOverhead));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "21%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Formula");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "4%", null, "left", null, DmPosition.NONE, false);
        btnformula.setHeight("20px");
        btnformula.getElement().getStyle().setMarginTop(1, Style.Unit.PX);
        btnformula.setWidth("20px");
        btnformula.getElement().setAttribute("style", btnformula.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnformula.setText("...");
        btnformula.addStyleName("dm-button-grad");
        btnformula.setTitle("Formula");
        flwTd.add(btnformula);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtFormula);
        txtFormula.setHeight("55px");
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Post A/C");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPostAC));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Nature");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHNature));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Dr/Cr");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(3, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDrCr));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("At Rate Save In");
        lbl.getElement().getStyle().setColor("blue");
        lbl.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
        lbl.getElement().setPropertyString("title", "At Rate Save In filed must start with [oh_at_] prefix  ");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHAtRateSaveIn));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Amt. Save In");
        lbl.getElement().getStyle().setColor("blue");
        lbl.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
        lbl.getElement().setPropertyString("title", "Amt. Save In filed must start with [oh_amt_] prefix  ");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(3, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHAmtSaveIn));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("At Rate Editable");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHAtRateEditable));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Amt. Editable");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(3, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHAmtEditable));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("A/c Editable");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHACEditable));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Show Formula Value");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(3, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHShowFormulaValue));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("At Rate Mandatory");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHAtRateMandatory));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Amt. Mandatory");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(3, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHAmtMandatory));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Contra Exp");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHContraExp));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Round Off");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(3, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHRoundOff));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("At Rate");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAtRate));
        txtAtRate.setAlignment(ValueBoxBase.TextAlignment.LEFT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Unique ID");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(3, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtUniqueID));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("OverHead Editable");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHOverHeadEditable));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Printable");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(3, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPrintable));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("At Rate Amount");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHAtRateAmount));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Landed Value");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(3, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHCalLValOn));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("OH Setup");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHOHSetup));
        flwTr.add(flwTd);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Partial Posting");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(3, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPartialPosting));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }
}
