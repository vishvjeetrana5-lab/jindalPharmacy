package dataman.utility.numberSystem.client;

import dataman.libraryMenu.self.shared.lmUtility;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.utility.numberSystem.shared.Find_Mdl;
import dataman.utility.numberSystem.shared.GrdNumberFormat_Mdl;
import dataman.utility.numberSystem.shared.TxtHVoucherType_Mdl;

public abstract class NumberSystem_Dsg extends IsWidgetExtended {

    final String widgetName = new lmUtility().NUMBER_SYSTEM.caption;
    public final static String modifiedByBlog = "Modified By";
    final DmUserControl userControl = new DmUserControl();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final Button counterPopUpButton = new Button("Show Related Counter");
    final Menu menu = new Menu();
    MenuItem mnuExportData;
    SelectionHandler<MenuItem> selectionHandler;

    abstract void menuClick(SelectionEvent<MenuItem> event);

    //<editor-fold defaultstate="collapsed" desc="txtHNSVtype">
    interface DmTxtHVoucherType_PrpAcs extends KeyField_T_PrpAcs<TxtHVoucherType_Mdl> {

        @Editor.Path("Category")
        ValueProvider<TxtHVoucherType_Mdl, String> category();

    }
    final DmTextHelp<TxtHVoucherType_Mdl, DmTxtHVoucherType_PrpAcs> txtHNSVtype = new DmTextHelp(GWT.create(DmTxtHVoucherType_PrpAcs.class), 10, true);

    public void iniTxtHNSVtype() {
        txtHNSVtype.getDmGrid().addTextColumn(txtHNSVtype.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHNSVtype.getDmGrid().addTextColumn(txtHNSVtype.getDmGrid().getPropertyAccess().category(), "Category", 100, "Category");
        txtHNSVtype.getDmGrid().initializeGrid(txtHNSVtype.getDmGrid().getPropertyAccess().code(), 300, 0, 1); //244

        txtHNSVtype.getHelper().addSort("Description", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("Category")
        ValueProvider<Find_Mdl, String> category();
    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 23);

    public void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Number System", 400, "Name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().category(), "Category", 300, "Category");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1280, 300, 1);
        dmFind.getHelper().addSort("Description", DmSortType.ASC);
    }
    //</editor-fold>

    public NumberSystem_Dsg() {
        ini();
        iniFind();
        iniTxtHNSVtype();
        iniMenu();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);

        userControl.addTextBlog(modifiedByBlog, 200, false, true);
        userControl.addGap(100);

        counterPopUpButton.setWidth("200px");
        counterPopUpButton.addStyleName("dm-button-grad");
        userControl.addGeneralBlog(counterPopUpButton);
        vrtInnerForm.add(userControl);

        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
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

        return menu;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "500px", null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "140px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", "140px", "left", null, DmPosition.NONE, true);
        flwTd.add(leftSection());
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", "140px", "left", null, DmPosition.NONE, true);
        flwTd.add(vl.add(createVoucherGrid()));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "360px", "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(createEntryNumberGrid()));
        flwTable.add(flwTr);

        flwTable.add(flwTr);
        return flwTable;
    }

    private FlowPanel leftSection() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "90%", "20px", null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Voucher Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "85%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHNSVtype));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="Grd Voucher">
    interface grdVoucher_PrpAcs extends KeyFieldHashCode_T_PrpAcs<KeyFieldExtended> {
    }
    final DmGrid<KeyFieldExtended, grdVoucher_PrpAcs> dmVoucherGrd = new DmGrid(GWT.create(grdVoucher_PrpAcs.class));

    private Grid createVoucherGrid() {
        dmVoucherGrd.addRowNumberColumn();
        dmVoucherGrd.addTextColumn(dmVoucherGrd.getPropertyAccess().name(), "Include More Type", 1, "");
        dmVoucherGrd.initializeGrid(dmVoucherGrd.getPropertyAccess().fieldHashCode(), 510, 140, 2);

        return dmVoucherGrd.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Grd Entry">
    interface Grd_Entry extends KeyFieldHashCode_T_PrpAcs<GrdNumberFormat_Mdl> {

        @Editor.Path("NumberFormat")
        ValueProvider<GrdNumberFormat_Mdl, String> numberFormat();

        @Editor.Path("CounterFormat")
        ValueProvider<GrdNumberFormat_Mdl, String> counterFormat();

        @Editor.Path("SiteName")
        ValueProvider<GrdNumberFormat_Mdl, String> siteName();

        @Editor.Path("IsSiteWiseName")
        ValueProvider<GrdNumberFormat_Mdl, String> isSiteWiseName();

        @Editor.Path("IsYearWiseName")
        ValueProvider<GrdNumberFormat_Mdl, String> isYearWiseName();
    }

    final DmGrid<GrdNumberFormat_Mdl, Grd_Entry> dmEntryGrd = new DmGrid(com.google.gwt.core.shared.GWT.create(Grd_Entry.class));

    private Grid createEntryNumberGrid() {
        dmEntryGrd.addRowNumberColumn();
        dmEntryGrd.addTextColumn(dmEntryGrd.getPropertyAccess().numberFormat(), "Number Format", 1, "");
        dmEntryGrd.addTextColumn(dmEntryGrd.getPropertyAccess().counterFormat(), "Counter Format", 200, "");
        dmEntryGrd.addTextColumn(dmEntryGrd.getPropertyAccess().siteName(), "Site Name", 400, "");
        dmEntryGrd.addTextColumn(dmEntryGrd.getPropertyAccess().isSiteWiseName(), "Reset Site Wise", 100, "");
        dmEntryGrd.addTextColumn(dmEntryGrd.getPropertyAccess().isYearWiseName(), "Reset Year Wise", 100, "");
        dmEntryGrd.initializeGrid(dmEntryGrd.getPropertyAccess().fieldHashCode(), 1280, 360, 2);

        return dmEntryGrd.getGrid();
    }
    //</editor-fold>
}
