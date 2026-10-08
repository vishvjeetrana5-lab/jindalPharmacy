package aarogya.master.servicesApplicableTo.client;

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
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class ServicesApplicableTo_Dsg extends IsWidgetExtended {

    final String widgetName = new pmMaster().SERVICE_CATEGORY_APPLICABLE_TO.caption;
    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final MenuItem mnuExportCard = new MenuItem("Export Service Category Applicable");
    final Menu menu = new Menu();

    //<editor-fold defaultstate="collapsed" desc="txtHServiceCat">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHServiceCat = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniTxtHServiceCat() {
        txtHServiceCat.getDmGrid().addTextColumn(txtHServiceCat.getDmGrid().getPropertyAccess().name(), "Service Category", 352, "name");
        txtHServiceCat.getDmGrid().initializeGrid(txtHServiceCat.getDmGrid().getPropertyAccess().code(), 352, 0, 1);

        txtHServiceCat.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface find_PrpAcs extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedModifiedBy")
        ValueProvider<KeyFieldExtended, String> category();
    }
    final DmFind<KeyFieldExtended, find_PrpAcs> dmFind = new DmFind(GWT.create(find_PrpAcs.class), 24);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Service Category", 150, "serviceName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().category(), "Voucher Category", 800, "applicableCategory");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 250, 1);

        dmFind.getHelper().addSort("serviceName", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHAcknowledge">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHAcknowledge = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true);

    private void iniTxtHAcknowledge() {
        txtHAcknowledge.getDmGrid().addTextColumn(txtHAcknowledge.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHAcknowledge.getDmGrid().initializeGrid(txtHAcknowledge.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    public ServicesApplicableTo_Dsg() {
        iniTxtHServiceCat();
        ini();
        iniMenu();
        iniTxtHAcknowledge();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));
        iniFind();
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);

        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable;
        FlowPanel flwTr;
        FlowPanel flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "552px", "left", null, DmPosition.CENTER, true);
        /*====================================< 1st Row (Main Form) >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "25px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "25px", "left", null, DmPosition.NONE, true);
        flwTd.add(topArea());
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 2nd Row (Voucher Grid)>====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "365px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "521px", "left", null, DmPosition.NONE, true);
        flwTd.add(vl.add(iniGrdVoucher()));

        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
        return flwTable;

    }

    private FlowPanel topArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "20px", null, null, DmPosition.CENTER, true);
        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Service Category");
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "39%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHServiceCat));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("IsAutoAcknowledge");
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "6%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHAcknowledge));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="voucherGrid">
    interface GrdVoucher_PrpAcs extends KeyFieldHashCode_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedModifiedBy")
        ValueProvider<KeyFieldExtended, String> sharedModifiedBy();

    }
    final DmGrid<KeyFieldExtended, GrdVoucher_PrpAcs> grdVoucher = new DmGrid(GWT.create(GrdVoucher_PrpAcs.class));

    private Grid iniGrdVoucher() {
        ColumnConfig<KeyFieldExtended, String> selectCheckBox;
        selectCheckBox = grdVoucher.addTextColumn(grdVoucher.getPropertyAccess().sharedModifiedBy(), "", 30, "");
        grdVoucher.addTextColumn(grdVoucher.getPropertyAccess().name(), "Voucher Type", 150, "description");
        grdVoucher.initializeGrid(grdVoucher.getPropertyAccess().fieldHashCode(), 1280, 521, 2);

        selectCheckBox.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        selectCheckBox.setCellPadding(false);
        selectCheckBox.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                int val = DmShared.vNull(grdVoucher.getGrid().getStore().get(context.getIndex()).getSharedModifiedBy()).intValue();
                if (val == 1) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em'></span>");
                } else if (val == 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em'></span>");
                }
            }
        });

        return grdVoucher.getGrid();
    }
    //</editor-fold>

    private Menu iniMenu() {
        menu.add(mnuExportCard);
        return menu;
    }
}
