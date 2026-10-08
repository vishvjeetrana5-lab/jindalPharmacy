package dataman.utility.walkThroughMaster.client;

import dataman.libraryMenu.self.shared.lmUtility;
import dataman.utility.walkThroughMaster.shared.GrdAuthority_Mdl;
import dataman.utility.walkThroughMaster.shared.Find_Mdl;
import dataman.utility.walkThroughMaster.shared.GrdDefaultDetail_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmEnum.DmPosition;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGUI.client.DmGUI;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.libraryMenu.self.shared.lmUtility;

abstract class WalkThroughMaster_Dsg extends IsWidgetExtended {

    final String widgetName = new lmUtility().WALK_THROUGH_MASTER.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final TabLayoutPanel tabPanel = new TabLayoutPanel(1.6, Style.Unit.EM);
    final DmGUI gui = new DmGUI(topControl, 170);
    final DmSuggestBox txtName = new DmSuggestBox(35, 10, true, "name", DmEnum.DmDataType.STRING);

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyFieldHashCode_T_PrpAcs<Find_Mdl> {

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 17);

    public void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Name", 100, "name");

        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().fieldHashCode(), 1260, 0, 1);

        dmFind.getHelper().addSort("name", DmEnum.DmSortType.ASC);

    }
    //</editor-fold>

    public WalkThroughMaster_Dsg() {
        initialize();

        iniFind();

    }

    private void initialize() {

        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.add(addUserControl());

        vrtInnerForm.setCellHeight(flw, "100%");

        flwForm.add(vrtInnerForm);
    }

    private DmUserControl addUserControl() {

        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 100, true, true);

        return userControl;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "360px", null, null, DmEnum.DmPosition.CENTER, true);

        //======================= Upper Area================================================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "25px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.add(vl.add(entryArea()));
        flwTable.add(flwTr);
        //======================= END ======================================================

        //======================= Grid Authority and Default Deatil ===========================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "335px", "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", "335px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTd.add(vl.add(iniGrdAuthority()));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", "335px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTd.add(vl.add(iniGrdDefault()));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================== END========================================================

        return flwTable;
    }

    private FlowPanel entryArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "25px", null, null, DmEnum.DmPosition.CENTER, true);

        //==============================1st Row===========================================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "9%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Name");

        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtName));
        txtName.getElement().getStyle().setMarginLeft(-2, Style.Unit.PX);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(9, Style.Unit.PX);
        flwTd.setStyleName("dm-StripLabel");
        lbl = new DmLabel("Aproval Default Detail:");
        lbl.setStyleName("dm-HLabel");
        lbl.getElement().getStyle().setMarginTop(8, Style.Unit.PX);
        lbl.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        lbl.getElement().getStyle().setFontSize(12, Style.Unit.PX);
        lbl.setHeight("15px");
        lbl.setWidth("70%");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //================================================================================
        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="gridAuthority">
    interface GridAuthority_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdAuthority_Mdl> {

        @Editor.Path("SrlNo")
        ValueProvider<GrdAuthority_Mdl, String> srlNo();

        @Editor.Path("LinkedUserName")
        ValueProvider<GrdAuthority_Mdl, String> linkedUserName();

    }
    final DmGrid<GrdAuthority_Mdl, GridAuthority_PrpAcs> gridAuthority = new DmGrid(GWT.create(GridAuthority_PrpAcs.class));

    public Grid iniGrdAuthority() {
        //gridAuthority.addRowNumberColumn();
        gridAuthority.addTextColumn(gridAuthority.getPropertyAccess().srlNo(), "S.No.", 40, "");
        gridAuthority.addTextColumn(gridAuthority.getPropertyAccess().name(), "Authority", 130, "");
        gridAuthority.addTextColumn(gridAuthority.getPropertyAccess().linkedUserName(), "Linked User", 200, "");
        gridAuthority.initializeGrid(gridAuthority.getPropertyAccess().fieldHashCode(), 390, 338, 3);

        return gridAuthority.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="gridDefaultDetail">
    interface GridDefault_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdDefaultDetail_Mdl> {

        @Editor.Path("V_TypeName")
        ValueProvider<GrdDefaultDetail_Mdl, String> v_TypeName();

        @Editor.Path("SiteName")
        ValueProvider<GrdDefaultDetail_Mdl, String> siteName();

        @Editor.Path("DepartmentName")
        ValueProvider<GrdDefaultDetail_Mdl, String> departmentName();

        @Editor.Path("ValueUpto")
        ValueProvider<GrdDefaultDetail_Mdl, String> valueUpto();
    }
    final DmGrid<GrdDefaultDetail_Mdl, GridDefault_PrpAcs> gridDefaultDetail = new DmGrid(GWT.create(GridDefault_PrpAcs.class));

    public Grid iniGrdDefault() {
        gridDefaultDetail.addRowNumberColumn();

        gridDefaultDetail.addTextColumn(gridDefaultDetail.getPropertyAccess().name(), "User Name", 110, "");
        gridDefaultDetail.addTextColumn(gridDefaultDetail.getPropertyAccess().v_TypeName(), "v_Type", 110, "");
        gridDefaultDetail.addTextColumn(gridDefaultDetail.getPropertyAccess().siteName(), "Site Name", 130, "");
        gridDefaultDetail.addTextColumn(gridDefaultDetail.getPropertyAccess().departmentName(), "Department", 150, "");
        gridDefaultDetail.addTextColumn(gridDefaultDetail.getPropertyAccess().valueUpto(), "Val.Upto", 70, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridDefaultDetail.initializeGrid(gridDefaultDetail.getPropertyAccess().fieldHashCode(), 588, 338, 4);

        return gridDefaultDetail.getGrid();
    }
    //</editor-fold>
}
