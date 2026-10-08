package dataman.utility.overheadConfiguration.client;

import dataman.libraryMenu.self.shared.lmUtility;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.utility.overheadConfiguration.shared.*;

abstract class OverheadConfiguration_Dsg extends IsWidgetExtended {

    final String widgetName = new lmUtility().OVERHEAD_CONFIGURATION.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    public final static String applicableByBlog = "Approved By";

    final FlowPanel flwForm = new FlowPanel();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final VerticalPanel vrtInnerForm = new VerticalPanel();

    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final DmTextBox txtApplicableDate = new DmTextBox(11, true);
    final DmTextBox txtOverheadName = new DmTextBox(100, true);

    final public static Button btnShowAppPupup = new Button("Applicable From");

    final Menu menu = new Menu();
    MenuItem mnuExportData, mnuImportData;
    SelectionHandler<MenuItem> selectionHandler;

    abstract void menuClick(SelectionEvent<MenuItem> event);

    //<editor-fold defaultstate="collapsed" desc="txtHVoucherType">
    interface TxtHVoucherType_PrpAcs extends KeyField_T_PrpAcs<TxtHVoucherType_Mdl> {

        @Editor.Path("ShortName")
        ValueProvider<TxtHVoucherType_Mdl, String> shortName();

    }

    final DmTextHelp<TxtHVoucherType_Mdl, TxtHVoucherType_PrpAcs> txtHVoucherType = new DmTextHelp(GWT.create(TxtHVoucherType_PrpAcs.class), 4, true);

    private void iniTxtHVoucherType() {
        txtHVoucherType.getDmGrid().addTextColumn(txtHVoucherType.getDmGrid().getPropertyAccess().name(), "Type", 0, "Name");
        txtHVoucherType.getDmGrid().addTextColumn(txtHVoucherType.getDmGrid().getPropertyAccess().shortName(), "Short Name", 150, "short_Name");
        txtHVoucherType.getDmGrid().initializeGrid(txtHVoucherType.getDmGrid().getPropertyAccess().code(), 300, 0, 1);

        txtHVoucherType.getHelper().addSort("Name", DmSortType.ASC, "description");
        txtHVoucherType.getHelper().addSort("short_Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHTable">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHTable = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true);

    private void iniTxtHTableAffected() {
        txtHTable.getDmGrid().addTextColumn(txtHTable.getDmGrid().getPropertyAccess().name(), "Table", 0, "name");
        txtHTable.getDmGrid().initializeGrid(txtHTable.getDmGrid().getPropertyAccess().code(), 300, 0, 1);

        txtHTable.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyFieldHashCode_T_PrpAcs<Find> {

        @Editor.Path("OverheadName")
        ValueProvider<Find, String> overheadName();

    }
    final DmFind<Find, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 23);

    public void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Voucher Type", 1, "Name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().overheadName(), "Overhead Name", 400, "overheadName");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().fieldHashCode(), 1260, 0, 1);//183
        dmFind.getHelper().addSort("Name", DmSortType.ASC);
        dmFind.getHelper().addSort("overheadName", DmSortType.ASC);
    }
    //</editor-fold>

    public OverheadConfiguration_Dsg() {
        ini();
        iniFind();
        iniTxtHVoucherType();
        iniTxtHTableAffected();
    }

    private void ini() {
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.CENTER, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");

        vrtInnerForm.add(vl.add(topControl));

        FlowPanel flwWorkingArea = new FlowPanel();
        flwWorkingArea.setStyleName("dm-FlwWorkingArea");
        vrtInnerForm.add(flwWorkingArea);
        flwWorkingArea.add(workingArea());
        iniMenu();
        //Test Start
        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(50);
        userControl.addControlBlog(applicableByBlog, vl.add(btnShowAppPupup));
        btnShowAppPupup.setWidth("100px");
        btnShowAppPupup.addStyleName("dm-button-grad");
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        vrtInnerForm.add(userControl);
        //Test End

        vrtInnerForm.setCellHeight(flwWorkingArea, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "500px", null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "150px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", "150px", "left", null, DmPosition.NONE, false);
        flwTd.add(leftSection());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", "150px", "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(createSiteGrid()));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "350px", "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(createGrid()));
        flwTable.add(flwTr);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel leftSection() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "90%", "80px", null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Voucher Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHVoucherType));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Overhead Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtOverheadName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Table Affected");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHTable));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Applicable From");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtApplicableDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private Menu iniMenu() {
        selectionHandler = new SelectionHandler<MenuItem>() {

            @Override
            public void onSelection(SelectionEvent<MenuItem> event) {
                menuClick(event);
            }
        };

        mnuExportData = new MenuItem("Export Data", selectionHandler);
        menu.add(mnuExportData);

        mnuImportData = new MenuItem("Import Data", selectionHandler);
        menu.add(mnuImportData);

        return menu;
    }

    //<editor-fold defaultstate="collapsed" desc="Grd">
    interface Grd_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdOverhead_Mdl> {

        @Editor.Path("SrlNo")
        ValueProvider<GrdOverhead_Mdl, String> srlNo();

        @Editor.Path("Overhead")
        ValueProvider<GrdOverhead_Mdl, String> overhead();

        @Editor.Path("ManualCode")
        ValueProvider<GrdOverhead_Mdl, String> manualCode();

        @Editor.Path("Formula")
        ValueProvider<GrdOverhead_Mdl, String> formula();

        @Editor.Path("Nature")
        ValueProvider<GrdOverhead_Mdl, String> nature();

        @Editor.Path("AtRateSaveIn")
        ValueProvider<GrdOverhead_Mdl, String> atRateSaveIn();

        @Editor.Path("AmtSaveIn")
        ValueProvider<GrdOverhead_Mdl, String> amtSaveIn();

        @Editor.Path("PostAC")
        ValueProvider<GrdOverhead_Mdl, String> postAC();

        @Editor.Path("DrCr")
        ValueProvider<GrdOverhead_Mdl, String> drCr();

        @Editor.Path("ContraExpText")
        ValueProvider<GrdOverhead_Mdl, String> contraExpText();

        @Editor.Path("AtRate")
        ValueProvider<GrdOverhead_Mdl, String> atRate();

        @Editor.Path("AtRateEditable")
        ValueProvider<GrdOverhead_Mdl, String> atRateEditable();

        @Editor.Path("AmtEditable")
        ValueProvider<GrdOverhead_Mdl, String> amtEditable();

        @Editor.Path("AcEditable")
        ValueProvider<GrdOverhead_Mdl, String> acEditable();

        @Editor.Path("overheadEditable")
        ValueProvider<GrdOverhead_Mdl, String> overheadEditable();

        @Editor.Path("ShowFormulaValue")
        ValueProvider<GrdOverhead_Mdl, String> showFormulaValue();

        @Editor.Path("RoundOff")
        ValueProvider<GrdOverhead_Mdl, String> roundOff();

        @Editor.Path("OhSetup")
        ValueProvider<GrdOverhead_Mdl, String> ohSetup();

        @Editor.Path("UniqueID")
        ValueProvider<GrdOverhead_Mdl, String> uniqueID();

        @Editor.Path("AtRateMandatory")
        ValueProvider<GrdOverhead_Mdl, String> atRateMandatory();

        @Editor.Path("AmtMandatory")
        ValueProvider<GrdOverhead_Mdl, String> amtMandatory();

        @Editor.Path("Printable")
        ValueProvider<GrdOverhead_Mdl, String> printable();

        @Editor.Path("AtRateAmountText")
        ValueProvider<GrdOverhead_Mdl, String> atRateAmount();
        
        @Editor.Path("PartialPostingText")
        ValueProvider<GrdOverhead_Mdl, String> partialPostingText();

    }
    final DmGrid<GrdOverhead_Mdl, Grd_PrpAcs> dmGrd = new DmGrid(GWT.create(Grd_PrpAcs.class));

    public Grid createGrid() {
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().srlNo(), "S.No.", 40, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().overhead(), "Overhead", 120, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().manualCode(), "Manual Code", 80, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().formula(), "Formula", 75, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().nature(), "Nature", 70, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().atRateSaveIn(), "At Rate Save In", 100, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().amtSaveIn(), "Amt. Save In", 75, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().postAC(), "Post AC", 75, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().drCr(), "Dr/Cr", 45, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().contraExpText(), "Contra Exp", 70, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().atRate(), "At Rate", 70, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().atRateEditable(), "At Rate Editable", 100, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().amtEditable(), "Amt. Editable", 75, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().acEditable(), "AC Editable", 75, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().overheadEditable(), "Overhead Editable", 100, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().showFormulaValue(), "Show Formula Value", 120, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().roundOff(), "Round Off", 70, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().ohSetup(), "OH Setup", 75, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().uniqueID(), "Unique ID", 75, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().atRateMandatory(), "At Rate Mandatory", 100, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().amtMandatory(), "Amt. Mandatory", 90, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().atRateAmount(), "At Rate Amount", 100, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().printable(), "Printable", 90, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().partialPostingText(), "Partial Posting", 90, "");
        dmGrd.initializeGrid(dmGrd.getPropertyAccess().fieldHashCode(), 1280, 350);
        dmGrd.getGrid().getElement().getStyle().setMarginBottom(0, Style.Unit.PX);

        dmGrd.getGrid().getView().setViewConfig(new GridViewConfig<GrdOverhead_Mdl>() {
            @Override
            public String getColStyle(GrdOverhead_Mdl model, ValueProvider<? super GrdOverhead_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = "dm-GridCellFormat ";
                if ((DmShared.iNull(model.getMcColorStatus()).intValue() == 1) && (colIndex == 2)) {
                    styleClass += "dm-GridCellDisable ";
                }
                if ((DmShared.iNull(model.getCeColorStatus()).intValue() == 1) && (colIndex == 9)) {
                    styleClass += "dm-GridCellDisable ";
                }
                if ((DmShared.iNull(model.getPacColorStatus()).intValue() == 1) && (colIndex == 7)) {
                    styleClass += "dm-GridCellDisable ";
                }
                return styleClass;
            }

            @Override
            public String getRowStyle(GrdOverhead_Mdl model, int rowIndex) {
                return "";
            }
        });
        return dmGrd.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Grd Site">
    interface grdSite_PrpAcs extends KeyFieldHashCode_T_PrpAcs<KeyFieldExtended> {
    }
    final DmGrid<KeyFieldExtended, grdSite_PrpAcs> dmSiteGrd = new DmGrid(GWT.create(grdSite_PrpAcs.class));

    private Grid createSiteGrid() {
        dmSiteGrd.addRowNumberColumn();
        dmSiteGrd.addTextColumn(dmSiteGrd.getPropertyAccess().name(), "Applicable To Site", 400, "");
        dmSiteGrd.initializeGrid(dmSiteGrd.getPropertyAccess().fieldHashCode(), 510, 150, 2);

        return dmSiteGrd.getGrid();
    }
    //</editor-fold>
}
