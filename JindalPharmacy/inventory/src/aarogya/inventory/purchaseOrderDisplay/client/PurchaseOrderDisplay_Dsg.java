package aarogya.inventory.purchaseOrderDisplay.client;

import aarogya.inventory.purchaseOrderDisplay.shared.PurchaseOrderDisplay_Mdl;
import aarogya.projectMenu.self.shared.pmInventory;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
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
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmBase.self.shared.MenuLink;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextArea.client.DmTextArea;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import java.util.ArrayList;
import java.util.List;

public abstract class PurchaseOrderDisplay_Dsg extends IsWidgetExtended implements ClickHandler {

    final String widgetName = new pmInventory().PURCHASE_ORDER_DISPLAY.caption;
    final FlowPanel flwForm = new FlowPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmTextArea txtHelp = new DmTextArea();
    final PopupPanel pnlGrd1 = new PopupPanel(true);

    final FlowPanel flwRefresh = new FlowPanel();
    final Button btnRefresh = new Button();
    final DmTextBox txtRequisitionNo = new DmTextBox(50, false);
    final DmTextBox txtDate = new DmTextBox(11, false);
    final DmTextBox txtSearchValue = new DmTextBox(50, false);
    final List<MenuLink> menuLinkListMedicineDisplay = new ArrayList<>();
    final FlowPanel flwDepartment = new FlowPanel();
    final FlowPanel flwClose = new FlowPanel();
    final Button btnClose = new Button();
    final Button btnHelp = new Button();
    final FlowPanel flwHelp = new FlowPanel();
    final InlineLabel cbCheck = new InlineLabel();
    final Button btnSetting = new Button();
    final FlowPanel flwSetting = new FlowPanel();
    final Button btnShortOnSupplier = new Button("Short on Supplier");
    final FlowPanel flwAddShort = new FlowPanel();

    int headerValue = 0, intApprovedSupplier = 0, intCheck = 0;
    ColumnConfig<PurchaseOrderDisplay_Mdl, String> selectCheckBox, cancelCheckBox;

    public PurchaseOrderDisplay_Dsg() {
        iniMenuList();
        ini();
        iniTxtHDepartmentBy();
        pnlGrd1.setWidth("515px");
        pnlGrd1.setHeight("250px");
        pnlGrd1.hide();
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
        addToList(new pmInventory().PURCHASE_ORDER, menuLinkListMedicineDisplay);
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

        label = new InlineLabel("Help");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-question-circle");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnHelp.addStyleName("dm-top-control-btnStyle");
        btnHelp.getElement().appendChild(aBtn.getElement());
        btnHelp.getElement().appendChild(label.getElement());
        btnHelp.setWidth("50px");
        btnHelp.setTitle("Help");
        flwHelp.add(btnHelp);

        toolContainer.addWidget(txtSearchValue);
        txtSearchValue.setWidth("400px");
        txtHDepartmentBy.setHeight("23px");
        txtHDepartmentBy.setWidth("250px");
        toolContainer.addWidget(flwDepartment);
        toolContainer.addWidget(txtHDepartmentBy);
        toolContainer.addWidget(flwHelp);
        toolContainer.addWidget(flwRefresh);
        toolContainer.addWidget(flwSetting);
        toolContainer.addWidget(flwClose);
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
        DmClient.setStyle(flwTable, "100%", "571px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTable.getElement().setAttribute("style", flwTable.getElement().getAttribute("style")
                + ";overflow-x:auto;background-color:white;");
        flwTable.add(iniGrdOrder());
        return flwTable;
    }

    private FlowPanel rightArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "169px", "100%", "left", null, DmEnum.DmPosition.NONE, true);
        flwTable.getElement().setAttribute("style", flwTable.getElement().getAttribute("style")
                + ";overflow-y:auto;background-color:white;");

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "571px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.add(rightSection());
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel rightSection() {
        FlowPanel flMain = new FlowPanel();
        FlowPanel flwTr, flwTd;
        DmLabel lbl;
        InlineLabel menuName;
        FlowPanel flwBlock;

        flMain.getElement().setAttribute("style", "width:95%;height:30%;margin:auto;overflow-y:auto;");

        flwTr = new FlowPanel();

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        flwTd.getElement().getStyle().setMarginRight(60, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "25%", "20px", "left", null, DmEnum.DmPosition.NONE, false);
        btnShortOnSupplier.setWidth("155px");
        btnShortOnSupplier.setHeight("20px");
        btnShortOnSupplier.getElement().setAttribute("style", btnShortOnSupplier.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;margin-top:3px");
        btnShortOnSupplier.setTitle("Short On Supplier");
        flwAddShort.add(btnShortOnSupplier);
        flwTd.add(flwAddShort);
        flwTr.add(flwTd);

        flMain.add(flwTr);

        for (MenuLink menuLink : menuLinkListMedicineDisplay) {
            if (menuLink != null) {

                flwBlock = new FlowPanel();

                menuName = new InlineLabel();
                menuName.setTitle(menuLink.moduleName + " | " + menuLink.menuName);
                menuName.setStyleName("ellipsis");
                menuName.setStyleName("dashboard-menu-link");
                menuName.setText(DmShared.xNull(menuLink.caption));
                flwBlock.getElement().setAttribute("style", ""
                        + ";font-family: arial; font-size:12px; float:left; padding:7px 31px; ");
                flwBlock.add(menuName);
                menuName.addClickHandler(this);
                flMain.add(flwBlock);
            }
        }

        return flMain;
    }

    //<editor-fold defaultstate="collapsed" desc="gridOrder">
    interface GridOrder_PrpAcs extends KeyFieldHashCode_T_PrpAcs<PurchaseOrderDisplay_Mdl> {

        @Editor.Path("DepartmentName")
        ValueProvider<PurchaseOrderDisplay_Mdl, String> departmentName();

        @Editor.Path("ItemName")
        ValueProvider<PurchaseOrderDisplay_Mdl, String> itemName();

        @Editor.Path("ApprovedQty")
        ValueProvider<PurchaseOrderDisplay_Mdl, String> approvedQty();

        @Editor.Path("PendingOrderQty")
        ValueProvider<PurchaseOrderDisplay_Mdl, String> pendingOrderQty();

        @Editor.Path("SkuName")
        ValueProvider<PurchaseOrderDisplay_Mdl, String> skuName();

        @Editor.Path("LastOrderRate")
        ValueProvider<PurchaseOrderDisplay_Mdl, String> lastOrderRate();

        @Editor.Path("LastOrderSupplier")
        ValueProvider<PurchaseOrderDisplay_Mdl, String> lastOrderSupplier();

        @Editor.Path("IsChecked")
        ValueProvider<PurchaseOrderDisplay_Mdl, String> isChecked();

        @Editor.Path("ApprovedSupplier")
        ValueProvider<PurchaseOrderDisplay_Mdl, String> approvedSupplier();

    }
    final DmGrid<PurchaseOrderDisplay_Mdl, GridOrder_PrpAcs> gridOrder = new DmGrid(GWT.create(GridOrder_PrpAcs.class));

    private Grid iniGrdOrder() {
        int colIndex = 0;
        ColumnConfig<PurchaseOrderDisplay_Mdl, String> selectCheckBox;
        ColumnConfig<PurchaseOrderDisplay_Mdl, String> selectDisplay;

        intCheck = colIndex;
        selectCheckBox = gridOrder.addTextColumn(gridOrder.getPropertyAccess().isChecked(), "", 30, "");
        colIndex++;
        gridOrder.addTextColumn(gridOrder.getPropertyAccess().departmentName(), "Department", 115, "toDepartmentName");
        colIndex++;
        gridOrder.addTextColumn(gridOrder.getPropertyAccess().itemName(), "Item N/ame", 320, "itemName");
        colIndex++;
        gridOrder.addTextColumn(gridOrder.getPropertyAccess().approvedQty(), "Indent Qty", 60, "aprQty").setHorizontalAlignment(HasAutoHorizontalAlignment.ALIGN_RIGHT);
        colIndex++;
        gridOrder.addTextColumn(gridOrder.getPropertyAccess().pendingOrderQty(), "Pending Qty", 70, "pendingQty").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        colIndex++;
        gridOrder.addTextColumn(gridOrder.getPropertyAccess().skuName(), "SKU", 40, "skuName");
        colIndex++;
        gridOrder.addTextColumn(gridOrder.getPropertyAccess().lastOrderRate(), "P. Rate", 60, "lastOrderRate").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        colIndex++;
        gridOrder.addTextColumn(gridOrder.getPropertyAccess().lastOrderSupplier(), "Supplier", 260, "lastSupplierName");
        colIndex++;
        intApprovedSupplier = colIndex;
//        gridOrder.addTextColumn(gridOrder.getPropertyAccess().approvedSupplier(), "S.D.", 30, "");
        selectDisplay = gridOrder.addTextColumn(gridOrder.getPropertyAccess().approvedSupplier(), "SD", 25, "");
        colIndex++;
        gridOrder.initializeGrid(gridOrder.getPropertyAccess().fieldHashCode(), 1100, 570);

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

        selectCheckBox.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                int val = DmShared.vNull(gridOrder.getGrid().getStore().get(context.getIndex()).getIsChecked()).intValue();
                if (val == 1) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em'></span>");
                } else if (val == 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em'></span>");
                }
            }
        });

        selectDisplay.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<span class = 'fa fa-eye' style='font-size:1.3em; color:green;'></span>");
            }
        });
        return gridOrder.getGrid();

    }
    //</editor-fold>
//</editor-fold>
}
