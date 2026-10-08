package aarogya.master.provisionalStatement.client;

import aarogya.master.provisionalStatement.shared.Find_Mdl;
import aarogya.master.provisionalStatement.shared.GrdProvisionalStatement_Mdl;
import aarogya.projectMenu.self.shared.pmMaster;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class ProvisionalStatement_Dsg extends IsWidgetExtended {

    final String widgetName = new pmMaster().PROVISIONAL_STATEMENT.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final Button btnGenerate = new Button("Generate Statement");

    //<editor-fold defaultstate="collapsed" desc="txtMonth">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHMonth = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true, true);

    private void iniMonths() {
        txtHMonth.getDmGrid().addTextColumn(txtHMonth.getDmGrid().getPropertyAccess().name(), "Months", 0, "");
        txtHMonth.getDmGrid().initializeGrid(txtHMonth.getDmGrid().getPropertyAccess().code(), 200, 0, 1);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHSite">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHSite = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniSites() {
        txtHSite.getDmGrid().addTextColumn(txtHSite.getDmGrid().getPropertyAccess().name(), " Sites", 0, "name");
        txtHSite.getDmGrid().initializeGrid(txtHSite.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHSite.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("Month")
        ValueProvider<Find_Mdl, String> month();

        @Editor.Path("Site")
        ValueProvider<Find_Mdl, String> site();

        @Editor.Path("Year")
        ValueProvider<Find_Mdl, String> year();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 22);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().month(), "Month", 200, "");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().site(), "Site", 500, "siteName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().year(), "Year", 500, "");

        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1280, 0, 1);

        dmFind.getHelper().addSort("monthcode", DmSortType.ASC);
        dmFind.getHelper().addSort("siteName", DmSortType.ASC);

    }
    //</editor-fold>

    public ProvisionalStatement_Dsg() {
        ini();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));
        iniFind();
        iniMonths();
        iniSites();
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.add(iniUserControl());

        vrtInnerForm.setCellHeight(flw, "100%");

        flwForm.add(vrtInnerForm);
    }

    private DmUserControl iniUserControl() {
        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        userControl.addGap(100);
        return userControl;

    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "475px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row FROM AND TO  >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "60px", "left", null, DmPosition.NONE, true);

        //============================< To column( border-left: none )>=======================
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "60px", "left", null, DmPosition.NONE, true);

        flwTd.add(mainSection());
        flwTr.add(flwTd);
        flwTable.add(flwTr);


        /*====================================<2 Row Grid  >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "415px", "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1280px", "415px", "left", null, DmPosition.NONE, true);
        flwTd.add(vl.add(iniGrdService()));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        return flwTable;
    }

    private FlowPanel mainSection() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "55%", "60px", null, null, DmPosition.CENTER, true);

        //======================================1 row======================================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "7%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Month");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "22%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHMonth));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "6%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Site");
        flwTd.getElement().getStyle().setMarginLeft(6, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "31%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSite));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "22%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        flwTd.add(vl.add(btnGenerate));
        btnGenerate.getElement().getStyle().setPadding(1, Style.Unit.PX);
        btnGenerate.getElement().getStyle().setFontSize(10, Style.Unit.PX);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;

    }

    //<editor-fold defaultstate="collapsed" desc="Grd Service">
    interface GridService_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdProvisionalStatement_Mdl> {

        @Editor.Path("PoolName")
        ValueProvider<GrdProvisionalStatement_Mdl, String> poolName();

        @Editor.Path("ConsultantName")
        ValueProvider<GrdProvisionalStatement_Mdl, String> consultantName();

        @Editor.Path("ServiceName")
        ValueProvider<GrdProvisionalStatement_Mdl, String> serviceName();

        @Editor.Path("ShairAmount")
        ValueProvider<GrdProvisionalStatement_Mdl, String> shairAmount();

        @Editor.Path("Unit")
        ValueProvider<GrdProvisionalStatement_Mdl, String> unit();

    }
    final DmGrid<GrdProvisionalStatement_Mdl, GridService_PrpAcs> grdProvStatmnt = new DmGrid(GWT.create(GridService_PrpAcs.class));

    private Grid iniGrdService() {
        grdProvStatmnt.addRowNumberColumn();

        grdProvStatmnt.addTextColumn(grdProvStatmnt.getPropertyAccess().poolName(), "Pool Name", 150, "");
        grdProvStatmnt.addTextColumn(grdProvStatmnt.getPropertyAccess().consultantName(), "Consultant Name", 150, "");
        grdProvStatmnt.addTextColumn(grdProvStatmnt.getPropertyAccess().serviceName(), "Service Name", 150, "");
        grdProvStatmnt.addTextColumn(grdProvStatmnt.getPropertyAccess().unit(), "Unit", 150, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdProvStatmnt.addTextColumn(grdProvStatmnt.getPropertyAccess().shairAmount(), "Shair Amount", 150, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

        grdProvStatmnt.initializeGrid(grdProvStatmnt.getPropertyAccess().fieldHashCode(), 1280, 418, 2);

        return grdProvStatmnt.getGrid();
    }
    //</editor-fold>
}
