package aarogya.pharmacy.procurementPlannerDisplay.client;

import aarogya.pharmacy.procurementPlannerDisplay.shared.ProcurementPlannerDisplay_Mdl;
import aarogya.pharmacy.procurementPlannerDisplay.shared.Find_Mdl;
import aarogya.projectMenu.self.shared.pmPharmacy;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasAutoHorizontalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmEnum.DmSortType;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;
import dataman.dmBase.self.shared.MenuLink;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import java.util.ArrayList;
import java.util.List;

public abstract class ProcurementPlannerDisplay_Dsg extends IsWidgetExtended implements ClickHandler {

    final String widgetName = new pmPharmacy().PROCUREMENT_PLANNER_DISPLAY.caption;
    final FlowPanel flwForm = new FlowPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final VerticalPanel vrtInnerForm = new VerticalPanel();

    final FlowPanel flwRefresh = new FlowPanel();
    final FlowPanel flwPrint = new FlowPanel();
    final Button btnRefresh = new Button();
    final Button btnPrint = new Button();
    final DmTextBox txtRequisitionNo = new DmTextBox(50, false);
    final DmTextBox txtDate = new DmTextBox(11, false);
//    final DmTextBox txtSearchValue = new DmTextBox(50, false);
    final List<MenuLink> menuLinkListProcurementPlannerDisplay = new ArrayList<>();
    final FlowPanel flwDepartment = new FlowPanel();
    final FlowPanel flwClose = new FlowPanel();
    final FlowPanel flwFind = new FlowPanel();
    final FlowPanel flwIndentLable = new FlowPanel();
    final Button btnClose = new Button();
    final Button btnFind = new Button();
    final InlineLabel cbCheck = new InlineLabel();
    final InlineLabel menuName = new InlineLabel();
    final InlineLabel planNoDate = new InlineLabel();
    final InlineLabel columsLegends = new InlineLabel();
    final Button btnSetting = new Button();
    final FlowPanel flwSetting = new FlowPanel();
    final DmTextBox txtIndentDocID = new DmTextBox();
    final Anchor aStock = new Anchor("Show Tooltip");

    int headerValue = 0;
    ColumnConfig<ProcurementPlannerDisplay_Mdl, String> selectCheckBox, cancelCheckBox;

    public ProcurementPlannerDisplay_Dsg() {
        iniMenuList();
        ini();
        iniTxtHDepartmentBy();
    }

    private static void addToList(String caption, MenuLink menuLink, List<MenuLink> menuLinkList) {
        MenuLink menuLinkNew;

        if (DmClient.searchMenu(menuLink.moduleName, menuLink.caption) != null) {
            menuLinkNew = new MenuLink(menuLink.moduleName, caption, menuLink.caption);
            menuLinkList.add(menuLinkNew);
        }
    }

    private static void addToList(MenuLink menuLink, List<MenuLink> menuLinkList) {

        addToList(menuLink.caption, menuLink, menuLinkList);
    }

    private void iniMenuList() {
        menuLinkListProcurementPlannerDisplay.clear();
        addToList(new pmPharmacy().INDENT, menuLinkListProcurementPlannerDisplay);
    }

    private void ini() {

        FlowPanel flw;
        flwForm.setStyleName("dm-FlwForm");
        DmClient.setStyle(vrtInnerForm, "100%", "100%", "left", null, DmEnum.DmPosition.NONE, true);
        vrtInnerForm.add(toolContainer);
        vrtInnerForm.add(iniToolControls());

        flw = new FlowPanel();
        flw.setWidth("100%");
        flw.getElement().getStyle().setBackgroundColor("#eff");
        flw.add(workingArea());

        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);

        iniFind();
    }

    //<editor-fold defaultstate="collapsed" desc="txtHDepartmentBy">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDepartmentBy = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, false);

    private void iniTxtHDepartmentBy() {
        txtHDepartmentBy.getDmGrid().addTextColumn(txtHDepartmentBy.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHDepartmentBy.getDmGrid().initializeGrid(txtHDepartmentBy.getDmGrid().getPropertyAccess().code(), 200, 0, 1);

        txtHDepartmentBy.getHelper().addSort("NAME", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    private DmToolContainer iniToolControls() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Setting");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-filter");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnSetting.setTitle("Setting");
        btnSetting.addStyleName("dm-top-control-btnStyle");
        btnSetting.getElement().appendChild(aBtn.getElement());
        btnSetting.getElement().appendChild(label.getElement());
        btnSetting.setWidth("70px");
        flwSetting.add(btnSetting);

        label = new InlineLabel("Refresh");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-refresh");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnRefresh.addStyleName("dm-top-control-btnStyle");
        btnRefresh.getElement().appendChild(aBtn.getElement());
        btnRefresh.getElement().appendChild(label.getElement());
        btnRefresh.setWidth("50px");
        btnRefresh.setTitle("Refresh");
        flwRefresh.add(btnRefresh);

        label = new InlineLabel("Print");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-print");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnPrint.addStyleName("dm-top-control-btnStyle");
        btnPrint.getElement().appendChild(aBtn.getElement());
        btnPrint.getElement().appendChild(label.getElement());
        btnPrint.setWidth("50px");
        btnPrint.setTitle("Print");
        flwPrint.add(btnPrint);

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("50px");
        btnClose.setTitle("Close");
        flwClose.add(btnClose);

        label = new InlineLabel("Find");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-search");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnFind.addStyleName("dm-top-control-btnStyle");
        btnFind.getElement().appendChild(aBtn.getElement());
        btnFind.getElement().appendChild(label.getElement());
        btnFind.setWidth("40px");
        btnFind.setTitle("Find: (Ctrl + F)");
        flwFind.add(btnFind);

        FlowPanel flPlanNoDate = new FlowPanel();
        flPlanNoDate.getElement().setAttribute("style", "width:400%;height:10%;margin:auto;overflow-y:auto;");
        flPlanNoDate.getElement().setAttribute("style", ""
                + ";font-family: arial;font-weight: bold; font-size:15px; float:left; padding:7px 20px; width: 400px; ");
        planNoDate.setText("");
        flPlanNoDate.add(planNoDate);

        flwIndentLable.add(addIndentLabel());

        toolContainer.addWidget(flwRefresh);
        toolContainer.addWidget(flwClose);
        toolContainer.addWidget(flwFind);
        toolContainer.addWidget(flwSetting);
        toolContainer.addWidget(flwPrint);
        toolContainer.addWidget(flPlanNoDate);
        toolContainer.addWidget(flwIndentLable);
        toolContainer.addGap();
        return toolContainer;
    }

    private HorizontalPanel workingArea() {
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        FlowPanel flowPanel;

        DmClient.setStyle(horizontalPanel, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);

        /*====================================< New Colunm >====================================*/
        flowPanel = new FlowPanel();
        flowPanel.add(leftArea());
        horizontalPanel.add(flowPanel);
        horizontalPanel.setCellWidth(flowPanel, "100%");
        horizontalPanel.setCellHeight(flowPanel, "100%");

        return horizontalPanel;
    }

    private FlowPanel leftArea() {
        FlowPanel flwTable;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "570px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTable.getElement().setAttribute("style", flwTable.getElement().getAttribute("style")
                + ";overflow-x:auto;background-color:white;");
        flwTable.add(iniGrdRequisition());

        return flwTable;
    }

    private FlowPanel rightArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "300px", "100%", "left", null, DmEnum.DmPosition.NONE, true);
        flwTable.getElement().setAttribute("style", flwTable.getElement().getAttribute("style")
                + ";overflow-y:auto;background-color:white;");

        return flwTable;
    }

    private FlowPanel addIndentLabel() {
        FlowPanel flMain = new FlowPanel();
        FlowPanel flwBlock;
        flMain.getElement().setAttribute("style", "width:300%;height:7%;margin:auto;overflow-y:auto;");
        for (MenuLink menuLink : menuLinkListProcurementPlannerDisplay) {
            if (menuLink != null) {
                flwBlock = new FlowPanel();
                menuName.setTitle(menuLink.moduleName + " | " + menuLink.menuName);
                menuName.setStyleName("ellipsis");
                menuName.setStyleName("dashboard-menu-link");
                menuName.setText(DmShared.xNull(menuLink.caption));
                flwBlock.getElement().setAttribute("style", ""
                        + ";font-family: arial;font-weight: bold; font-size:15px; float:left; padding:4px 50px; ");
                flwBlock.add(menuName);
                menuName.addClickHandler(this);
                flMain.add(flwBlock);
            }
        }

        flwBlock = new FlowPanel();
        columsLegends.setStyleName("ellipsis");
        columsLegends.setStyleName("dashboard-menu-link");
        columsLegends.setText("Colums Legends");
        flwBlock.getElement().setAttribute("style", ""
                + ";font-family: arial;font-weight: bold; font-size:12px; float:left; padding:4px 50px; ");
        flwBlock.add(columsLegends);
        columsLegends.addClickHandler(this);
        flMain.add(flwBlock);

        return flMain;
    }

    //<editor-fold defaultstate="collapsed" desc="gridRequisition">
    interface GridRequisition_PrpAcs extends KeyFieldHashCode_T_PrpAcs<ProcurementPlannerDisplay_Mdl> {

        @Editor.Path("ItemName")
        ValueProvider<ProcurementPlannerDisplay_Mdl, String> itemName();

        @Editor.Path("SkuName")
        ValueProvider<ProcurementPlannerDisplay_Mdl, String> skuName();

        @Editor.Path("CurrentStkDept")
        ValueProvider<ProcurementPlannerDisplay_Mdl, String> currentStkDept();

        @Editor.Path("CurrentStkSite")
        ValueProvider<ProcurementPlannerDisplay_Mdl, String> currentStkSite();

        @Editor.Path("ConsumptionDept")
        ValueProvider<ProcurementPlannerDisplay_Mdl, String> consumptionDept();

        @Editor.Path("ConsumptionSite")
        ValueProvider<ProcurementPlannerDisplay_Mdl, String> consumptionSite();

        @Editor.Path("SurplusDeficitDept")
        ValueProvider<ProcurementPlannerDisplay_Mdl, String> surplusDeficitDept();

        @Editor.Path("SurplusDeficitSite")
        ValueProvider<ProcurementPlannerDisplay_Mdl, String> surplusDeficitSite();

        @Editor.Path("StkRunDaysDept")
        ValueProvider<ProcurementPlannerDisplay_Mdl, String> stkRunDaysDept();

        @Editor.Path("StkRunDaysSite")
        ValueProvider<ProcurementPlannerDisplay_Mdl, String> stkRunDaysSite();

        @Editor.Path("PendingPOIndentQty")
        ValueProvider<ProcurementPlannerDisplay_Mdl, String> pendingPOIndentQty();

        @Editor.Path("RecommendedQtyDept")
        ValueProvider<ProcurementPlannerDisplay_Mdl, String> recommendedQtyDept();

        @Editor.Path("RecommendedQtySite")
        ValueProvider<ProcurementPlannerDisplay_Mdl, String> recommendedQtySite();

        @Editor.Path("FinalIndentedQty")
        ValueProvider<ProcurementPlannerDisplay_Mdl, String> finalIndentedQty();

        @Editor.Path("EstimatedCost")
        ValueProvider<ProcurementPlannerDisplay_Mdl, String> estimatedCost();

        @Editor.Path("Remark")
        ValueProvider<ProcurementPlannerDisplay_Mdl, String> remark();

    }
    final DmGrid<ProcurementPlannerDisplay_Mdl, GridRequisition_PrpAcs> gridProcurement = new DmGrid(GWT.create(GridRequisition_PrpAcs.class));

    private Grid iniGrdRequisition() {
        ColumnConfig<ProcurementPlannerDisplay_Mdl, String> selectCheckBox;
//        ColumnConfig<ProcurementPlannerDisplay_Mdl, String> addColor;

        gridProcurement.addTextColumn(gridProcurement.getPropertyAccess().itemName(), "Item Name", 250, "itemName");
        gridProcurement.addTextColumn(gridProcurement.getPropertyAccess().skuName(), "Sku Name", 0, "skuName");
        gridProcurement.addTextColumn(gridProcurement.getPropertyAccess().currentStkDept(), "C-STK(S)", 75, "currStkDept").setHorizontalAlignment(HasAutoHorizontalAlignment.ALIGN_RIGHT);  //Cur. Stk. Dept.
        gridProcurement.addTextColumn(gridProcurement.getPropertyAccess().currentStkSite(), "C-STK(G)", 75, "currStkSite").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);  //Cur. Stk. Site
        gridProcurement.addTextColumn(gridProcurement.getPropertyAccess().consumptionDept(), "S/C-(S)", 75, "cnpDept").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);     //Consumption Dept.
        gridProcurement.addTextColumn(gridProcurement.getPropertyAccess().consumptionSite(), "S/C-(G)", 75, "cnpSite").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);      //Consumption Site
        gridProcurement.addTextColumn(gridProcurement.getPropertyAccess().surplusDeficitDept(), "S/G-(S)", 75, "exc_Srt_Dept").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);      //Surplus/Deficit Dept.
        gridProcurement.addTextColumn(gridProcurement.getPropertyAccess().surplusDeficitSite(), "S/G-(G)", 75, "exc_Srt_Site").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);      //Surplus/Deficit Site
        gridProcurement.addTextColumn(gridProcurement.getPropertyAccess().stkRunDaysDept(), "C-STKD(S)", 85, "stkRunDaysDept").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);        //Stock Run Days Dept.
        gridProcurement.addTextColumn(gridProcurement.getPropertyAccess().stkRunDaysSite(), "C-STKD(G)", 85, "stkRunDaysSite").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);      //Stock Run Days Site
        gridProcurement.addTextColumn(gridProcurement.getPropertyAccess().pendingPOIndentQty(), "O-PO/PI", 75, "pendingPoIndent").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);       //Pending PO/Indent
        gridProcurement.addTextColumn(gridProcurement.getPropertyAccess().recommendedQtyDept(), "REC.PI(S)", 85, "recommendedQtyDept").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);        //Rocommended Qty Dept
        gridProcurement.addTextColumn(gridProcurement.getPropertyAccess().recommendedQtySite(), "REC.PI(G)", 85, "recommendedQtySite").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);       //Rocommended Qty Site
        gridProcurement.addTextColumn(gridProcurement.getPropertyAccess().finalIndentedQty(), "FINAl-PI", 75, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);       //Enter Final Indent Qty
        gridProcurement.addTextColumn(gridProcurement.getPropertyAccess().estimatedCost(), "E.V.", 85, "estimatedCost").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);       //Rocommended Qty Site
        gridProcurement.addTextColumn(gridProcurement.getPropertyAccess().remark(), "Remark", 50, "");       //Enter Final Indent Qty

        gridProcurement.initializeGrid(gridProcurement.getPropertyAccess().fieldHashCode(), 1300, 570);

        return gridProcurement.getGrid();
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("VoucherDate")
        ValueProvider<Find_Mdl, String> voucherDate();

        @Editor.Path("RecId")
        ValueProvider<Find_Mdl, String> recId();

        @Editor.Path("PrparedBy")
        ValueProvider<Find_Mdl, String> prparedBy();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 19);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Plan No.", 100, "name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().voucherDate(), "Date", 100, "v_Date");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().prparedBy(), "Prepared By", 100, "prparedBy");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 380, 0, 1);

        dmFind.getHelper().addSort("v_Date", DmSortType.DESC);
    }
    //</editor-fold>
}
