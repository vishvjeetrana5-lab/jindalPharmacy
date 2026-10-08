package dataman.accounts.tdsCategoryMaster.client;

import dataman.libraryMenu.self.shared.lmAccounts;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.accounts.tdsCategoryMaster.shared.*;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;


abstract class TdsCategoryMaster_Dsg {

    final String widgetName = new lmAccounts().TDS_CATEGORY_MASTER.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";

    final FlowPanel flwForm = new FlowPanel();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final DmSuggestBox txtCategoryName = new DmSuggestBox(0, 10, true, "Name", DmDataType.STRING);
    final VerticalPanel vrtInnerForm = new VerticalPanel();

    final DmTextBox txtApplicableDate = new DmTextBox(11, false);
    final FlowPanel flwApplicableFrom = new FlowPanel();
    final Button btnShowAppPupup = new Button("Applicable From");
    final DmTextBox txtSection = new DmTextBox(20, false);
    final DmTextBoxNumeric txtInitialTurnover = new DmTextBoxNumeric(2, false, 12, true);
      
    
    Style stl;

    //<editor-fold defaultstate="collapsed" desc="txtHTdsFor">
    interface TxtHTdsFor_PrpAcs extends KeyField_T_PrpAcs<KeyFieldExtended> {

    }
    final DmTextHelp<KeyFieldExtended, TxtHTdsFor_PrpAcs> txtHTdsFor = new DmTextHelp(GWT.create(TxtHTdsFor_PrpAcs.class), 7, true);

    public void iniTxtHTdsFor() {
        txtHTdsFor.getDmGrid().addTextColumn(txtHTdsFor.getDmGrid().getPropertyAccess().name(), "Name", 150, "Name");
        txtHTdsFor.getDmGrid().initializeGrid(txtHTdsFor.getDmGrid().getPropertyAccess().code(), 150, 200, 1);
        txtHTdsFor.getDmGrid().getGrid().setHeight(150);
    }
//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="txtHTdsOnPreviousTransactions">
    interface TxtHTdsOnPreviousTransactions_PrpAcs extends KeyField_T_PrpAcs<KeyFieldExtended> {

    }
    final DmTextHelp<KeyFieldExtended, TxtHTdsFor_PrpAcs> txtHTdsOnPreviousTransactions = new DmTextHelp(GWT.create(TxtHTdsFor_PrpAcs.class), 7, true);

    public void iniTxtHTdsOnPreviousTransactions() {
        txtHTdsOnPreviousTransactions.getDmGrid().addTextColumn(txtHTdsOnPreviousTransactions.getDmGrid().getPropertyAccess().name(), "Name", 150, "Name");
        txtHTdsOnPreviousTransactions.getDmGrid().initializeGrid(txtHTdsOnPreviousTransactions.getDmGrid().getPropertyAccess().code(), 150, 200, 1);
        txtHTdsOnPreviousTransactions.getDmGrid().getGrid().setHeight(150);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface KeyFields_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("Type")
        ValueProvider<Find_Mdl, String> type();

        @Editor.Path("Section")
        ValueProvider<Find_Mdl, String> section();

        @Editor.Path("TdsFor")
        ValueProvider<Find_Mdl, String> tdsFor();
    }

    final DmFind<Find_Mdl, KeyFields_PrpAcs> dmFind = new DmFind(GWT.create(KeyFields_PrpAcs.class), 18);

    public void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Category Name", 50, "Name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().type(), "Type", 200, "type");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().section(), "Section", 200, "Section");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().tdsFor(), "Tds For", 200, "tdsFor");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 958, 1);
        dmFind.getHelper().addSort("Name", DmSortType.ASC);
        dmFind.getHelper().addSort("type", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Grd">
    interface Grd_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdTdsCategory_Mdl> {

        @Editor.Path("Description")
        ValueProvider<GrdTdsCategory_Mdl, String> Description();

        @Editor.Path("ManualCode")
        ValueProvider<GrdTdsCategory_Mdl, String> manualCode();

        @Editor.Path("PostingAccount")
        ValueProvider<GrdTdsCategory_Mdl, String> PostingAccount();

        @Editor.Path("Percent")
        ValueProvider<GrdTdsCategory_Mdl, String> Percent();

        @Editor.Path("CalculateOn")
        ValueProvider<GrdTdsCategory_Mdl, String> CalculateOn();

        @Editor.Path("FromAmt")
        ValueProvider<GrdTdsCategory_Mdl, String> fromAmt();

        @Editor.Path("ToAmt")
        ValueProvider<GrdTdsCategory_Mdl, String> toAmt();

        @Editor.Path("PenalityPercentage")
        ValueProvider<GrdTdsCategory_Mdl, String> penalityPercentage();

    }
    final DmGrid<GrdTdsCategory_Mdl, Grd_PrpAcs> dmGrd = new DmGrid(GWT.create(Grd_PrpAcs.class));

    private Grid createGrid() {
        dmGrd.addRowNumberColumn();
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().Description(), "Description", 150, null);
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().manualCode(), "Manual Code", 150, null);
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().PostingAccount(), "Posting A/c", 200, null);
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().fromAmt(), "From Amt.", 100, null);
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().toAmt(), "To Amt.", 100, null);
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().Percent(), "Percentage %", 100, null);
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().penalityPercentage(), "Penality %", 100, null);
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().CalculateOn(), "Calculate On", 150, null);
        dmGrd.initializeGrid(dmGrd.getPropertyAccess().fieldHashCode(), 1258, 320, 2);
        dmGrd.getGrid().getElement().getStyle().setMarginBottom(0, Style.Unit.PX);
        return dmGrd.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHTDS_TCSType">
    interface TxtHType_PrpAcs extends KeyField_T_PrpAcs<KeyFieldExtended> {

    }
    final DmTextHelp<KeyFieldExtended, TxtHType_PrpAcs> txtHType = new DmTextHelp(GWT.create(TxtHType_PrpAcs.class), 5, true);

    private void iniTxtHType() {
        txtHType.getDmGrid().addTextColumn(txtHType.getDmGrid().getPropertyAccess().name(), "Name", 220, "name");
        txtHType.getDmGrid().initializeGrid(txtHType.getDmGrid().getPropertyAccess().code(), 230, 1);
        txtHType.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    public TdsCategoryMaster_Dsg() {
        ini();
        iniTxtHType();
        iniTxtHTdsFor();
        iniTxtHTdsOnPreviousTransactions();
    }

    private void ini() {

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.CENTER, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));
        iniFind();

        FlowPanel flwWorkingArea = new FlowPanel();
        flwWorkingArea.setStyleName("dm-FlwWorkingArea");
        flwWorkingArea.add(workingArea());

        vrtInnerForm.add(flwWorkingArea);
        vrtInnerForm.add(createTDSBottom());
        vrtInnerForm.setCellHeight(flwWorkingArea, "100%");

        flwForm.add(vrtInnerForm);

    }

    private FlowPanel topArea() {
        FlowPanel flwTable;
        FlowPanel flwTr;
        FlowPanel flwTd;
        DmLabel lblTemp;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, "left", null, DmPosition.NONE, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTr.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Category Name");

        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        txtCategoryName.setMandatory(true);
        flwTd.add(vl.add(txtCategoryName));
        txtCategoryName.setMaxLength(50);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Section");
        lblTemp.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSection));
        flwTr.add(flwTd);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Initial Turnover");
        lblTemp.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtInitialTurnover));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("TDS For");
//        lblTemp.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHTdsFor));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Type");
        lblTemp.getElement().getStyle().setMarginLeft(35, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "11%", null, "left", null, DmPosition.NONE, false);
        txtHType.setMandatory(true);
        flwTd.add(vl.add(txtHType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Applicable From");
        lblTemp.getElement().getStyle().setMarginLeft(16, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtApplicableDate));
        flwTr.add(flwTd);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "21%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("TDS on Previous Transactions");
        lblTemp.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "6%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHTdsOnPreviousTransactions));
        flwTr.add(flwTd);

        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
        return flwTable;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable;
        FlowPanel flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", null, "left", null, DmEnum.DmPosition.NONE, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "80%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTr.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd.add(topArea());
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd.add(gridArea());
        flwTr.add(flwTd);
        flwTable.add(flwTr);


        /*====================================< ******* >====================================*/
        return flwTable;
    }

    private FlowPanel gridArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl, lbl2;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", null, "left", null, DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        //flwTr.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTr.getElement().getStyle().setMarginLeft(12, Style.Unit.PX);
        flwTr.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(vl.add(createGrid()));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private DmUserControl createTDSBottom() {

        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(50);

        DmClient.setStyle(flwApplicableFrom, "100%", "100%", null, null, DmPosition.NONE, true);
        flwApplicableFrom.add(btnShowAppPupup);

        userControl.addGeneralBlog(flwApplicableFrom);
        btnShowAppPupup.setWidth("100px");
        btnShowAppPupup.addStyleName("dm-button-grad");
        userControl.addGap(50);

        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        userControl.addGap(100);

        return userControl;

    }
}
