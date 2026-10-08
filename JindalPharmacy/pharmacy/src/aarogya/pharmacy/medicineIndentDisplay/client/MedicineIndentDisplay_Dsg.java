package aarogya.pharmacy.medicineIndentDisplay.client;

import aarogya.pharmacy.medicineIndentDisplay.shared.SearchValue_Mdl;
import aarogya.pharmacy.medicineIndentDisplay.shared.MedicineIndentDisplay_Mdl;
import aarogya.pharmacy.requisitionSearch.shared.GrdRequisitionDetailsSearch_Mdl;
import aarogya.projectMenu.self.shared.pmIPD;
import aarogya.projectMenu.self.shared.pmMaster;
import aarogya.projectMenu.self.shared.pmNursingStation;
import aarogya.projectMenu.self.shared.pmOPD;
import aarogya.projectMenu.self.shared.pmOT;
import aarogya.projectMenu.self.shared.pmPharmacy;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasAutoHorizontalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmBase.self.shared.MenuLink;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import java.util.ArrayList;
import java.util.List;

public abstract class MedicineIndentDisplay_Dsg extends IsWidgetExtended implements ClickHandler{

    final String widgetName = new pmPharmacy().MEDICINE_INDENT_DISPLAY.caption;
    final FlowPanel flwForm = new FlowPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final VerticalPanel vrtInnerForm = new VerticalPanel();

    final FlowPanel flwRefresh = new FlowPanel();
    final Button btnRefresh = new Button();
    final DmTextBox txtRequisitionNo = new DmTextBox(50, false);
    final DmTextBox txtDate = new DmTextBox(11, false);
    final DmTextBox txtSearchValue = new DmTextBox(50, false);
    final List<MenuLink> menuLinkListMedicineDisplay = new ArrayList<>();
    final FlowPanel flwDepartment = new FlowPanel();
    final FlowPanel flwClose = new FlowPanel();
    final Button btnClose = new Button();
    final InlineLabel cbCheck = new InlineLabel();
    final Button btnSetting=new Button();
    final FlowPanel flwSetting=new FlowPanel();

    int headerValue = 0;
    ColumnConfig<MedicineIndentDisplay_Mdl, String> selectCheckBox, cancelCheckBox;

    public MedicineIndentDisplay_Dsg() {
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
        menuLinkListMedicineDisplay.clear();
        addToList(new pmPharmacy().INDENT, menuLinkListMedicineDisplay);
    }

    private void ini() {

        FlowPanel flw;
        flwForm.setStyleName("dm-FlwForm");
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);
        vrtInnerForm.add(toolContainer);
        vrtInnerForm.add(iniToolControls());

        flw = new FlowPanel();
        flw.setWidth("100%");
        flw.getElement().getStyle().setBackgroundColor("#eff");
        flw.add(workingArea());

        cbCheck.addStyleName("fa fa-square-o");
        cbCheck.getElement().setAttribute("style", "position:absolute; top:30px; left:11px; z-index:50;cursor:pointer;");
        cbCheck.setTitle("Select all");
        flw.add(cbCheck);

        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    //<editor-fold defaultstate="collapsed" desc="txtHDepartmentBy">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDepartmentBy = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, false);

    private void iniTxtHDepartmentBy() {
        txtHDepartmentBy.getDmGrid().addTextColumn(txtHDepartmentBy.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHDepartmentBy.getDmGrid().initializeGrid(txtHDepartmentBy.getDmGrid().getPropertyAccess().code(), 200, 0, 1);

        txtHDepartmentBy.getHelper().addSort("NAME", DmEnum.DmSortType.ASC);
    }

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

        toolContainer.addWidget(txtSearchValue);
        txtSearchValue.setWidth("600px");
        toolContainer.addWidget(flwDepartment);
        toolContainer.addWidget(txtHDepartmentBy);
        toolContainer.addWidget(flwRefresh);
        toolContainer.addWidget(flwClose);
        toolContainer.addWidget(flwSetting);
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

        /*====================================< New Colunm >====================================*/
        flowPanel = new FlowPanel();
        flowPanel.add(rightArea());
        horizontalPanel.add(flowPanel);
        horizontalPanel.setCellHeight(flowPanel, "100%");

        return horizontalPanel;
    }

    private FlowPanel leftArea() {
        FlowPanel flwTable;

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

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "170px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.add(rightSection());
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel rightSection() {
        FlowPanel flMain = new FlowPanel();
        InlineLabel menuName;
        FlowPanel flwBlock;

        flMain.getElement().setAttribute("style", "width:95%;height:13%;margin:auto;overflow-y:auto;");
        for (MenuLink menuLink : menuLinkListMedicineDisplay) {
            if (menuLink != null) {

                flwBlock = new FlowPanel();

                menuName = new InlineLabel();
                menuName.setTitle(menuLink.moduleName + " | " + menuLink.menuName);
                menuName.setStyleName("ellipsis");
                menuName.setStyleName("dashboard-menu-link");
                menuName.setText(DmShared.xNull(menuLink.caption));
                flwBlock.getElement().setAttribute("style", ""
                        + ";font-family: arial; font-size:12px; float:left; padding:1px 2px; ");
                flwBlock.add(menuName);
                menuName.addClickHandler(this);
                flMain.add(flwBlock);
            }
        }
        return flMain;
    }

    //<editor-fold defaultstate="collapsed" desc="gridRequisition">
    interface GridRequisition_PrpAcs extends KeyFieldHashCode_T_PrpAcs<MedicineIndentDisplay_Mdl> {

        @Editor.Path("DepartmentName")
        ValueProvider<MedicineIndentDisplay_Mdl, String> departmentName();

        @Editor.Path("ItemName")
        ValueProvider<MedicineIndentDisplay_Mdl, String> itemName();

        @Editor.Path("RequisitionToDeptQty")
        ValueProvider<MedicineIndentDisplay_Mdl, String> requisitionToDeptQty();

        @Editor.Path("RequisitionFromDeptQty")
        ValueProvider<MedicineIndentDisplay_Mdl, String> requisitionFromDeptQty();
        
        @Editor.Path("PendingIndentQty")
        ValueProvider<MedicineIndentDisplay_Mdl, String> pendingIndentQty();
        
        @Editor.Path("MinQty")
        ValueProvider<MedicineIndentDisplay_Mdl, String> minQty();

        @Editor.Path("CurrentStk")
        ValueProvider<MedicineIndentDisplay_Mdl, String> currentStk();

        @Editor.Path("NetReqQty")
        ValueProvider<MedicineIndentDisplay_Mdl, String> netReqQty();
        
        @Editor.Path("SkuName")
        ValueProvider<MedicineIndentDisplay_Mdl, String> skuName();
        
        @Editor.Path("IsChecked")
        ValueProvider<MedicineIndentDisplay_Mdl, String> isChecked();

    }
    final DmGrid<MedicineIndentDisplay_Mdl, GridRequisition_PrpAcs> gridRequisition = new DmGrid(GWT.create(GridRequisition_PrpAcs.class));

    private Grid iniGrdRequisition() {
        ColumnConfig<MedicineIndentDisplay_Mdl, String> selectCheckBox;
//        ColumnConfig<MedicineIndentDisplay_Mdl, String> addColor;
        selectCheckBox = gridRequisition.addTextColumn(gridRequisition.getPropertyAccess().isChecked(), "", 30, "");

        gridRequisition.addTextColumn(gridRequisition.getPropertyAccess().departmentName(), "Department By", 150, "departmentName");
        gridRequisition.addTextColumn(gridRequisition.getPropertyAccess().itemName(), "Item Name", 350, "itemName");
        gridRequisition.addTextColumn(gridRequisition.getPropertyAccess().requisitionToDeptQty(), "Req.To Qty", 90, "requisitionToQty").setHorizontalAlignment(HasAutoHorizontalAlignment.ALIGN_RIGHT);
        gridRequisition.addTextColumn(gridRequisition.getPropertyAccess().requisitionFromDeptQty(), "Req.From Qty", 90, "requisitionFromQty").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridRequisition.addTextColumn(gridRequisition.getPropertyAccess().pendingIndentQty(), "Pend. Ind. Qty", 90, "pendingIndentQty").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridRequisition.addTextColumn(gridRequisition.getPropertyAccess().currentStk(), "Curr.Stk.", 90, "CurrStk").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridRequisition.addTextColumn(gridRequisition.getPropertyAccess().minQty(), "Min. Qty", 90, "minStock").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridRequisition.addTextColumn(gridRequisition.getPropertyAccess().netReqQty(), "Net Req.", 90, "NetReqQty").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridRequisition.addTextColumn(gridRequisition.getPropertyAccess().skuName(), "Sku Name", 80, "skuName");
        gridRequisition.initializeGrid(gridRequisition.getPropertyAccess().fieldHashCode(), 1200, 500);
        
        selectCheckBox.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        selectCheckBox.setCellPadding(false);

        selectCheckBox.setHeader(new SafeHtml() {
            @Override
            public String asString() {
                headerValue = 0;
                String headerStyle = "<span class = 'fa fa-square-o' style='font-size:1.3em; font-weight:bold !important;'></span>";
                return headerStyle;
            }
        });

//        */*//*/**//**//*/**/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*
//        gridRequisition.getGrid().getView().setViewConfig(new GridViewConfig<MedicineIndentDisplay_Mdl>() {
//            @Override
//            public String getColStyle(MedicineIndentDisplay_Mdl model, ValueProvider<? super MedicineIndentDisplay_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
//                String styleClass = "dm-GridCellFormat ";
//                if (DmShared.xNull(model.getPriorityColor()).equalsIgnoreCase("#FF0000")) {
//                    styleClass += "dm-GridCellSelectedRed "; //"dm-colorRed ";
//                } else if (DmShared.xNull(model.getPriorityColor()).equalsIgnoreCase("#FF6347")) {
//                    styleClass += "dm-GridCellSelectedLightRed ";
//                } else if (DmShared.xNull(model.getPriorityColor()).equalsIgnoreCase("#32CD32")) {
//                    styleClass += "dm-GridCellSelectedLimeGreen ";
//                } else if (DmShared.xNull(model.getPriorityColor()).equalsIgnoreCase("#CD5C5C")) {
//                    styleClass += "dm-GridCellSelectedMaroon ";
//                }
//                return styleClass;
//            }
//
//            @Override
//            public String getRowStyle(MedicineIndentDisplay_Mdl model, int rowIndex) {
//                return "";
//            }
//        });
//        */*//*/**//**//*/**/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*

//        addColor.setCell(new AbstractCell<String>() {
//            @Override
//            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
//                String cellValue, requisition, color;
//                cellValue = "";
//                requisition = DmShared.xNull(gridRequisition.getGrid().getStore().get(context.getIndex()).getSharedKeyFieldName());
//                color = DmShared.xNull(gridRequisition.getGrid().getStore().get(context.getIndex()).getPriorityColor());
//                if (!DmShared.xNull(color).equalsIgnoreCase("")) {
//                    cellValue += "<div style='background-color: " + color + "; height:13px; width:10px;float:left;'></div>"
//                            + "<div style='margin-left:15px;'>" + requisition + "</div>";
//                } else {
//                    cellValue += "<div style='height:13px; width:10px;float:left;float:left;'></div>"
//                            + "<div style='margin-left:15px;'>" + requisition + "</div>";
//                }
//
//                sb.appendHtmlConstant(cellValue);
//            }
//        });

        selectCheckBox.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                int val = DmShared.vNull(gridRequisition.getGrid().getStore().get(context.getIndex()).getIsChecked()).intValue();
                if (val == 1) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em'></span>");
                } else if (val == 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em'></span>");
                }
            }
        });
        return gridRequisition.getGrid();
    }
    //</editor-fold>
//</editor-fold>
}
