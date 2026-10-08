package dataman.utility.overheadLineConfiguration.client;

import dataman.libraryMenu.self.shared.lmUtility;
import dataman.utility.overheadLineConfiguration.shared.*;
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

abstract class OverheadLineConfiguration_Dsg extends IsWidgetExtended {

    final String widgetName = new lmUtility().OVERHEAD_LINE_CONFIGURATION.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    public final static String applicableByBlog = "Approved By";

    final FlowPanel flwForm = new FlowPanel();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmTextBox txtApplicableDate = new DmTextBox(11, true);
    final DmTextBox txtOverheadName = new DmTextBox(100, true);

    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final Menu menu = new Menu();
    MenuItem mnuExportData, mnuImportData;
    SelectionHandler<MenuItem> selectionHandler;

    abstract void menuClick(SelectionEvent<MenuItem> event);

    final public static Button btnShowAppPupup = new Button("Applicable From");

    //<editor-fold defaultstate="collapsed" desc="txtHVoucherType">
    interface DmTxtHVoucherType_PrpAcs extends KeyField_T_PrpAcs<TxtHVoucherType_Mdl> {

        @Editor.Path("ShortName")
        ValueProvider<TxtHVoucherType_Mdl, String> shortName();

    }

    final DmTextHelp<TxtHVoucherType_Mdl, DmTxtHVoucherType_PrpAcs> txtHVoucherType = new DmTextHelp(GWT.create(DmTxtHVoucherType_PrpAcs.class), 4, true);

    private void iniTxtHEntryType() {
        txtHVoucherType.getDmGrid().addTextColumn(txtHVoucherType.getDmGrid().getPropertyAccess().name(), "Type", 0, "Name");
        txtHVoucherType.getDmGrid().addTextColumn(txtHVoucherType.getDmGrid().getPropertyAccess().shortName(), "Short Name", 150, "short_Name");
        txtHVoucherType.getDmGrid().initializeGrid(txtHVoucherType.getDmGrid().getPropertyAccess().code(), 300, 0, 1);

        txtHVoucherType.getHelper().addSort("Name", DmSortType.ASC);
        txtHVoucherType.getHelper().addSort("short_Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHTableAffected">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHTableAffected = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true);

    private void iniTxtHTableAffected() {
        txtHTableAffected.getDmGrid().addTextColumn(txtHTableAffected.getDmGrid().getPropertyAccess().name(), "Table", 0, "name");
        txtHTableAffected.getDmGrid().initializeGrid(txtHTableAffected.getDmGrid().getPropertyAccess().code(), 300, 0, 1);

        txtHTableAffected.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyFieldHashCode_T_PrpAcs<Find_Mdl> {

        @Editor.Path("OverheadName")
        ValueProvider<Find_Mdl, String> overheadName();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 23);

    public void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Voucher Type", 1, "Name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().overheadName(), "Overhead Name", 400, "overheadName");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().fieldHashCode(), 1260, 0, 1);//183
        dmFind.getHelper().addSort("Name", DmSortType.ASC);
        dmFind.getHelper().addSort("overheadName", DmSortType.ASC);
    }
//</editor-fold>

    public OverheadLineConfiguration_Dsg() {
        ini();
        iniFind();
        iniTxtHEntryType();
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
        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(50);
        userControl.addControlBlog(applicableByBlog, vl.add(btnShowAppPupup));
        btnShowAppPupup.setWidth("100px");
        btnShowAppPupup.addStyleName("dm-button-grad");
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        vrtInnerForm.add(userControl);

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
        flwTd.add(vl.add(txtHTableAffected));
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
    interface Grd_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdOverheadLine_Mdl> {

        @Editor.Path("SrlNo")
        ValueProvider<GrdOverheadLine_Mdl, String> srlNo();

        @Editor.Path("Overhead")
        ValueProvider<GrdOverheadLine_Mdl, String> overhead();

        @Editor.Path("ManualCode")
        ValueProvider<GrdOverheadLine_Mdl, String> manualCode();

        @Editor.Path("Formula")
        ValueProvider<GrdOverheadLine_Mdl, String> formula();

        @Editor.Path("nature")
        ValueProvider<GrdOverheadLine_Mdl, String> nature();

        @Editor.Path("AtRateSaveIn")
        ValueProvider<GrdOverheadLine_Mdl, String> atRateSaveIn();

        @Editor.Path("AmtSaveIn")
        ValueProvider<GrdOverheadLine_Mdl, String> amtSaveIn();

        @Editor.Path("PostAC")
        ValueProvider<GrdOverheadLine_Mdl, String> postAcc();

        @Editor.Path("overheadSaveIn")
        ValueProvider<GrdOverheadLine_Mdl, String> overheadSaveIn();

        @Editor.Path("acSave")
        ValueProvider<GrdOverheadLine_Mdl, String> acSave();

        @Editor.Path("AtRate")
        ValueProvider<GrdOverheadLine_Mdl, String> atRate();

        @Editor.Path("AtRateEditable")
        ValueProvider<GrdOverheadLine_Mdl, String> atRateEditable();

        @Editor.Path("AmtEditable")
        ValueProvider<GrdOverheadLine_Mdl, String> amtEditable();

        @Editor.Path("overheadEditable")
        ValueProvider<GrdOverheadLine_Mdl, String> overheadEditable();

        @Editor.Path("ShowFormulaValue")
        ValueProvider<GrdOverheadLine_Mdl, String> showFormulaValue();

        @Editor.Path("RoundOff")
        ValueProvider<GrdOverheadLine_Mdl, String> roundOff();

        @Editor.Path("AtRateMandatory")
        ValueProvider<GrdOverheadLine_Mdl, String> atRateMandatory();

        @Editor.Path("AmtMandatory")
        ValueProvider<GrdOverheadLine_Mdl, String> amtMandatory();

        @Editor.Path("Printable")
        ValueProvider<GrdOverheadLine_Mdl, String> printable();

        @Editor.Path("UniqueID")
        ValueProvider<GrdOverheadLine_Mdl, String> uniqueID();

        @Editor.Path("AtRateAmountText")
        ValueProvider<GrdOverheadLine_Mdl, String> atRateAmount();

    }

    final DmGrid<GrdOverheadLine_Mdl, Grd_PrpAcs> dmGrd = new DmGrid(GWT.create(Grd_PrpAcs.class));

    public Grid createGrid() {
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().srlNo(), "S.No.", 50, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().overhead(), "Overhead", 120, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().manualCode(), "Manual Code", 75, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().formula(), "Formula", 75, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().nature(), "Nature", 75, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().atRateSaveIn(), "At Rate Save In", 100, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().amtSaveIn(), "Amt. Save In", 75, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().postAcc(), "Post AC", 75, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().overheadSaveIn(), "Overhead Save In", 100, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().acSave(), "A/C Save in", 75, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().atRate(), "At Rate", 75, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().atRateEditable(), "At Rate Editable", 100, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().amtEditable(), "Amt. Editable", 75, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().overheadEditable(), "Overhead Editable", 100, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().showFormulaValue(), "Show Formula Value", 110, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().roundOff(), "Round Off", 75, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().atRateMandatory(), "At Rate Mandatory", 100, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().amtMandatory(), "Amt. Mandatory", 100, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().printable(), "Printable", 80, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().atRateAmount(), "At Rate Amount", 100, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().uniqueID(), "Unique ID", 75, "");
        dmGrd.initializeGrid(dmGrd.getPropertyAccess().fieldHashCode(), 1280, 340);
        dmGrd.getGrid().getElement().getStyle().setMarginBottom(0, Style.Unit.PX);

        dmGrd.getGrid().getView().setViewConfig(new GridViewConfig<GrdOverheadLine_Mdl>() {
            @Override
            public String getColStyle(GrdOverheadLine_Mdl model, ValueProvider<? super GrdOverheadLine_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = "dm-GridCellFormat ";
                if ((DmShared.iNull(model.getMcColorStatus()).intValue() == 1) && (colIndex == 2)) {
                    styleClass += "dm-GridCellDisable ";
                }
                if ((DmShared.iNull(model.getPacColorStatus()).intValue() == 1) && (colIndex == 7)) {
                    styleClass += "dm-GridCellDisable ";
                }
                return styleClass;
            }

            @Override
            public String getRowStyle(GrdOverheadLine_Mdl model, int rowIndex) {
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
