package aarogya.master.packageRateList.client;

import aarogya.master.packageRateList.shared.GrdPackageDetail_Mdl;
import aarogya.master.packageRateList.shared.TxtHPackage_Mdl;
import aarogya.projectMenu.self.shared.pmMaster;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.Grid;
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

abstract class PackageRateList_Dsg extends IsWidgetExtended {

    final String widgetName = new pmMaster().PACKAGE_PRICE_LIST.caption;
    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    final DmTextBox txtOrganizationName = new DmTextBox();
    final DmTextBox txtShortName = new DmTextBox();

    //<editor-fold defaultstate="collapsed" desc="txtHPackage">
    interface TxtHPackage_PrpAcs extends KeyFieldHashCode_T_PrpAcs<TxtHPackage_Mdl> {

        @Editor.Path("ShortName")
        ValueProvider<TxtHPackage_Mdl, String> shortName();

        @Editor.Path("NatureName")
        ValueProvider<TxtHPackage_Mdl, String> natureName();
    }

    final DmTextHelp<TxtHPackage_Mdl, TxtHPackage_PrpAcs> txtHPackage = new DmTextHelp(GWT.create(TxtHPackage_PrpAcs.class), 10, false);

    private void iniTxtHPackage() {
        txtHPackage.getDmGrid().addTextColumn(txtHPackage.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHPackage.getDmGrid().addTextColumn(txtHPackage.getDmGrid().getPropertyAccess().shortName(), "Short Name", 80, "shortName");
        txtHPackage.getDmGrid().addTextColumn(txtHPackage.getDmGrid().getPropertyAccess().natureName(), "Nature", 60, "natureName");
        txtHPackage.getDmGrid().initializeGrid(txtHPackage.getDmGrid().getPropertyAccess().fieldHashCode(), 350, 0, 1);

        txtHPackage.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface find_PrpAcs extends KeyField_T_PrpAcs<KeyFieldExtended> {

    }
    final DmFind<KeyFieldExtended, find_PrpAcs> dmFind = new DmFind(GWT.create(find_PrpAcs.class), 24);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Organization Name", 500, "organizationName");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 250, 1);

        dmFind.getHelper().addSort("organizationName", DmSortType.ASC);
    }
    //</editor-fold>

    public PackageRateList_Dsg() {
        ini();
        iniFind();
        iniTxtHPackage();
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

        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "546px", null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "25px", "left", null, DmPosition.NONE, true);
        flwTr.add(topArea());
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "520px", "left", null, DmPosition.NONE, true);
        flwTr.add(iniGrdPackageDetail());
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel topArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "20px", null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Organization Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "38%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtOrganizationName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "9%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Short Name");
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtShortName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "11%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Search Package");
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPackage));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="Grid Package Detail">
    interface GridPackageDetail_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdPackageDetail_Mdl> {

        @Editor.Path("PackageName")
        ValueProvider<GrdPackageDetail_Mdl, String> packageName();

        @Editor.Path("ShortName")
        ValueProvider<GrdPackageDetail_Mdl, String> shortName();

        @Editor.Path("orgManualCode")
        ValueProvider<GrdPackageDetail_Mdl, String> orgManualCode();

        @Editor.Path("Nature")
        ValueProvider<GrdPackageDetail_Mdl, String> nature();

        @Editor.Path("Active")
        ValueProvider<GrdPackageDetail_Mdl, String> active();

        @Editor.Path("Amount")
        ValueProvider<GrdPackageDetail_Mdl, String> amount();

        @Editor.Path("Applicable")
        ValueProvider<GrdPackageDetail_Mdl, String> applicable();
    }
    final DmGrid<GrdPackageDetail_Mdl, GridPackageDetail_PrpAcs> gridPackageDetail = new DmGrid(GWT.create(GridPackageDetail_PrpAcs.class));

    public Grid iniGrdPackageDetail() {
        ColumnConfig<GrdPackageDetail_Mdl, String> applicable, roomCategory;
        gridPackageDetail.addRowNumberColumn();
        gridPackageDetail.addTextColumn(gridPackageDetail.getPropertyAccess().packageName(), "Package Name", 1, "");
        gridPackageDetail.addTextColumn(gridPackageDetail.getPropertyAccess().shortName(), "Short Name", 100, "");
        gridPackageDetail.addTextColumn(gridPackageDetail.getPropertyAccess().nature(), "Nature", 70, "");
        gridPackageDetail.addTextColumn(gridPackageDetail.getPropertyAccess().code(), "Package Code", 90, "");
        gridPackageDetail.addTextColumn(gridPackageDetail.getPropertyAccess().orgManualCode(), "Org. Code", 100, "");
        gridPackageDetail.addTextColumn(gridPackageDetail.getPropertyAccess().active(), "Active", 55, "");
        gridPackageDetail.addTextColumn(gridPackageDetail.getPropertyAccess().amount(), "Amount", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        applicable = gridPackageDetail.addTextColumn(gridPackageDetail.getPropertyAccess().applicable(), "Applicable", 275, "");
        roomCategory = gridPackageDetail.addTextColumn(gridPackageDetail.getPropertyAccess().code(), "", 25, "");
        gridPackageDetail.initializeGrid(gridPackageDetail.getPropertyAccess().fieldHashCode(), 1280, 520, 2);

        applicable.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        applicable.setCellPadding(false);
        applicable.setCell(new AbstractCell<String>() {

            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<div style='white-space: normal; text-decoration: underline; color :blue; "
                        + "font-size : 12px; margin-top : 3px;'>" + DmShared.xNull(value) + "</div>");
            }
        });

        roomCategory.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        roomCategory.setCellPadding(false);
        roomCategory.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                if (DmShared.iNull(value).longValue() != 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-list' style='font-size:12px; color:black; margin-top:15%;'></span>");
                }
            }
        });

        return gridPackageDetail.getGrid();
    }
    //</editor-fold>

}
